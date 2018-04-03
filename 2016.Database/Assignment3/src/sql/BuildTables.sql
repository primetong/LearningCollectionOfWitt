
-- 标题：Assignment 3
-- 日期：2015.12.08
-- 作者：Witt



------------
-- 创建表 --
------------

-- 用户列表
create table UserTable (
	name			varchar(127)	not null primary key,
	authority		int				not null,
)


-- 进程列表
create table ProcessTable (
	name			varchar(127)	not null,
	creator			varchar(127)	not null foreign key references UserTable(name) on update cascade on delete cascade,
	is_recording	bit				not null,
	date_begin		date			not null,
	date_end		date			not null,
	constraint pk_ProcessTable primary key (name, creator)
)


-- 运行时段列表
create table DateTable (
	the_date		date			not null primary key,
)


-- 统计结果列表
create table StatisticTable (
	name			varchar(127)	not null,
	creator			varchar(127)	not null,
	the_date		date			not null foreign key references DateTable(the_date) on update cascade on delete cascade,
	constraint fk_StatisticTable foreign key (name, creator) 
								references ProcessTable(name, creator) on update cascade on delete cascade,
	constraint pk_StatisticTable primary key (name, creator, the_date),
)


-- 原始记录列表
create table RecordTable (
	name			varchar(127)	not null,
	creator			varchar(127)	not null,
	record_date		date			not null,
	period			time(3)			not null,
	cpu_usage		int				not null,
	mem_usage		int				not null,
	duration		smallint		not null,
	constraint fk_RecordTable foreign key (name, creator, record_date) 
								references StatisticTable(name, creator, the_date) on update cascade on delete cascade,
)


-- 本程序运行日志列表
create table LogTable (
	log_id			bigint			IDENTITY(1, 1) primary key,
	log_datetime	datetime		not null,
	log_level		tinyint			not null,
	log_message		varchar(512)	not null,
)


------------
-- 触发器 --
------------

GO
CREATE TRIGGER DisableInsertDate
ON DateTable INSTEAD OF INSERT, UPDATE
AS
RAISERROR('Can not modify the table directly.',10,1) 
GO

GO
CREATE TRIGGER DisableInsertUser
ON UserTable INSTEAD OF INSERT, UPDATE
AS
RAISERROR('Can not modify the table directly.',10,1) 
GO

GO
CREATE TRIGGER DisableInsertProcess
ON ProcessTable INSTEAD OF INSERT
AS
RAISERROR('Can not modify the table directly.',10,1) 
GO

GO
CREATE TRIGGER DisableInsertStatistic
ON StatisticTable INSTEAD OF INSERT
AS
RAISERROR('Can not modify the table directly.',10,1) 
GO

GO
CREATE TRIGGER DisableInsertRecord
ON RecordTable INSTEAD OF INSERT
AS
RAISERROR('Can not modify the table directly.',10,1) 
GO

GO
CREATE TRIGGER DisableInsertLog
ON LogTable INSTEAD OF INSERT, UPDATE
AS
RAISERROR('Can not modify the table directly.',10,1) 
GO


--------------
-- 存储过程 --
--------------

-- 插入一条记录
GO
IF (EXISTS (SELECT * FROM SYS.OBJECTS WHERE NAME = 'InsertRecord'))
    DROP PROC InsertRecord
GO
CREATE PROC InsertRecord
    (@name			varchar(127)
	,@creator		varchar(127)
	,@record_date	date		
	,@period		tinyint		
	,@cpu_usage		int			
	,@mem_usage		int			
	,@duration		smallint)
AS
BEGIN
	DECLARE 
		@OldDateEnd date,
		@OldDateBegin date,
		@IsRecording bit
	
	ALTER TABLE DateTable		DISABLE TRIGGER ALL
	ALTER TABLE UserTable		DISABLE TRIGGER ALL
	ALTER TABLE ProcessTable	DISABLE TRIGGER ALL
	ALTER TABLE StatisticTable	DISABLE TRIGGER ALL
	ALTER TABLE RecordTable		DISABLE TRIGGER ALL

	SET NOCOUNT ON

	-- 检查时间节点是否存在，不存在则创建
	IF NOT EXISTS(SELECT the_date FROM DateTable WHERE the_date = @record_date)
		INSERT INTO DateTable VALUES(@record_date)

	-- 检查用户是否存在，不存在则创建
	IF NOT EXISTS(SELECT name FROM UserTable WHERE name = @creator)
		INSERT INTO UserTable VALUES(@creator, 100)

	-- 获取进程信息
	SELECT TOP(1) 
		@OldDateEnd = date_end
		, @OldDateBegin = date_end
		, @IsRecording = is_recording 
	FROM 
		ProcessTable 
	WHERE 
		name = @name AND creator = @creator
	
	-- 尝试更新进程记录
	IF @OldDateEnd IS NULL
		INSERT INTO ProcessTable VALUES(@name, @creator, 1, @record_date, @record_date)
	ELSE IF @IsRecording = 1	-- 如果这个进程保持监视中，则开始更新数据
	BEGIN
		-- 此时进程已经存在，需要检查进程日期是否需要更新
		IF (@OldDateEnd < @record_date)
			UPDATE ProcessTable SET date_end = @record_date WHERE name = @name AND creator = @creator
		
		IF (@OldDateBegin > @record_date)
			UPDATE ProcessTable SET date_begin = @record_date WHERE name = @name AND creator = @creator

		-- 检查进程、日期对应的数据头是否存在，不存在则创建
		IF NOT EXISTS(SELECT name FROM StatisticTable WHERE name = @name AND creator = @creator AND the_date = @record_date)
			INSERT INTO StatisticTable VALUES(@name, @creator, @record_date)

		-- 记录这条数据
		INSERT INTO RecordTable VALUES(@name, @creator, @record_date, DATEADD(ss, @period*900, 0), @cpu_usage, @mem_usage, @duration)
	END

	SET NOCOUNT OFF

	ALTER TABLE DateTable		ENABLE TRIGGER ALL
	ALTER TABLE UserTable		ENABLE TRIGGER ALL
	ALTER TABLE ProcessTable	ENABLE TRIGGER ALL
	ALTER TABLE StatisticTable	ENABLE TRIGGER ALL
	ALTER TABLE RecordTable		ENABLE TRIGGER ALL
END



-- 获取一段时间内的统计数据
GO
IF (EXISTS (SELECT * FROM SYS.OBJECTS WHERE NAME = 'SelectStatisticsSingle'))
    DROP PROC SelectStatisticsSingle
GO
CREATE PROC SelectStatisticsSingle
    (@name			varchar(127)
	,@creator		varchar(127)
	,@date_begin	date	
	,@date_end		date)
AS
BEGIN
	-- 尽可能高精度
	-- http://dba.stackexchange.com/questions/51536/datatype-bigger-than-bigint
	DECLARE  -- PS. 仍然存在溢出的风险！
		@sum_cpu_usage DECIMAL(38,0),
		@sum_mem_usage DECIMAL(38,0),
		@sum_duration  DECIMAL(38,0),
		@record_count  BIGINT

	-- 从记录中获取基础数据
	SELECT 
		@sum_cpu_usage = SUM(cpu_usage),
		@sum_mem_usage = SUM(mem_usage),
		@sum_duration  = SUM(duration),
		@record_count  = COUNT(mem_usage)
	FROM
		RecordTable
	WHERE
		creator = @creator AND
		name = @name AND
		@date_begin <= record_date AND
		record_date <= @date_end

	-- 防止除以 0 
	IF @sum_duration < 1
		SET @sum_duration = 1

	IF @record_count < 1
		SET @record_count = 1

	-- 处理并返回数据
	-- http://www.cnblogs.com/luluping/archive/2012/07/26/2610705.html

	SELECT
		@name									AS 'Process', 
		@creator								AS 'Owner',
		'Average CPU Usage(%)'					AS 'Property', 
		@sum_cpu_usage * 100 / @sum_duration	AS 'Value'
	UNION ALL SELECT
		@name									AS 'Process',
		@creator								AS 'Owner',
		'Average Memary Usage(M)'				AS 'Property', 
		@sum_mem_usage / @record_count			AS 'Value'
	UNION ALL SELECT
		@name									AS 'Process',
		@creator								AS 'Owner',
		'Total Time (s)'						AS 'Property',
		@sum_duration							AS 'Value'
END


-- 生成一条日志
GO
IF (EXISTS (SELECT * FROM SYS.OBJECTS WHERE NAME = 'InsertLog'))
    DROP PROC InsertLog
GO
CREATE PROC InsertLog
    (@level		tinyint
	,@message	varchar(512)
	,@date		datetime = NULL)
AS
BEGIN
	ALTER TABLE LogTable DISABLE TRIGGER ALL

	IF @date IS NULL
		SET @date = GETDATE()
	INSERT INTO LogTable VALUES(@date, @level, @message)

	ALTER TABLE LogTable ENABLE TRIGGER ALL
END

-- 删除全部日志记录
GO
IF (EXISTS (SELECT * FROM SYS.OBJECTS WHERE NAME = 'DeleteAllLogs'))
    DROP PROC DeleteAllLogs
GO
CREATE PROC DeleteAllLogs 
AS
BEGIN
	DELETE FROM LogTable
END

-- 删除全部原始数据记录
GO
IF (EXISTS (SELECT * FROM SYS.OBJECTS WHERE NAME = 'DeleteAllRecords'))
    DROP PROC DeleteAllRecords
GO
CREATE PROC DeleteAllRecords AS
BEGIN
	DELETE FROM UserTable
END

-- 删除全部记录
GO
IF (EXISTS (SELECT * FROM SYS.OBJECTS WHERE NAME = 'DeleteAll'))
    DROP PROC DeleteAll
GO
CREATE PROC DeleteAll AS
BEGIN
	DELETE FROM LogTable
	DELETE FROM RecordTable
	DELETE FROM StatisticTable
	DELETE FROM ProcessTable
	DELETE FROM DateTable
	DELETE FROM UserTable
END
GO

------------
-- 测试表 --
------------

--EXEC SelectStatisticsSingle 'Chrome.exe', 'MYLS', '20000101', '30000101'

--EXEC InsertLog 0, 'Make a Log!', '2015-12-13 08:23:59'
--EXEC InsertLog 0, 'Make a Log!'

------------
-- 销毁表 --
------------

--DROP TABLE LogTable
--DROP TABLE RecordTable
--DROP TABLE StatisticTable
--DROP TABLE ProcessTable
--DROP TABLE DateTable
--DROP TABLE UserTable
