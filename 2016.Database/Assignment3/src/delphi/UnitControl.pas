unit UnitControl;

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms, 
  Dialogs, Tabs, ComCtrls, ExtCtrls, StdCtrls, ADODB, UnitMain;

type
  TFrameControl = class(TFrame)
    PageControlMain: TPageControl;
    TabSheetBasicSettings: TTabSheet;
    TabSheetDataControl: TTabSheet;
    GridPanelData: TGridPanel;
    ButtonDeleteLogs: TButton;
    ButtonDeleteRecords: TButton;
    ButtonResetDB: TButton;
    ButtonBackupDB: TButton;
    ButtonRestoreDB: TButton;
    ScrollBoxDate: TScrollBox;
    TabSheetServiceControl: TTabSheet;
    GridPanelService: TGridPanel;
    ButtonStartService: TButton;
    ButtonPauseService: TButton;
    ButtonRemoveService: TButton;
    ScrollBoxService: TScrollBox;
    ScrollBoxBasic: TScrollBox;
    GridPanelBasic: TGridPanel;
    ADOCommandDataControl: TADOCommand;
    TabSheetAbout: TTabSheet;
    RichEditAbout: TRichEdit;
    procedure ButtonDeleteLogsClick(Sender: TObject);
    procedure ButtonDeleteRecordsClick(Sender: TObject);
    procedure DoStroredProc(cmd : widestring; err : widestring);
    procedure ButtonResetDBClick(Sender: TObject);
    procedure ButtonBackupDBClick(Sender: TObject);
    procedure ButtonRestoreDBClick(Sender: TObject);
  private
    { Private declarations }
  public
    { Public declarations }
  end;

implementation

{$R *.dfm}

procedure TFrameControl.ButtonBackupDBClick(Sender: TObject);
var
  { http://www.delphibasics.co.uk/RTL.asp?Name=TSaveDialog }
  saveDialog : TSaveDialog;
  log : WideString;
begin

  saveDialog := TSaveDialog.Create(self);
  with saveDialog do
  begin
    Title       := 'Save backup file';
    InitialDir  := GetCurrentDir;
    Filter      := 'Backup File|*.bak';
    DefaultExt  := 'bak';
    FilterIndex := 1;

    if Execute then
    begin
      with ADOCommandDataControl do
      begin

        log
          := 'User '''
          + FormMain.UserName()
          + ''' try to BACKUP Database'''
          + Connection.DefaultDatabase
          + '''.';

        CommandText
          := 'BACKUP DATABASE '
          + Connection.DefaultDatabase
          + ' TO DISK = '''
          + FileName
          + ''' WITH FORMAT, NAME = ''Created by ProcessMonitor.''';

        try Execute
        except on e: Exception do
          begin
            {http://stackoverflow.com/questions/2398385/sql-server-2008-backup-error-operating-system-error-5failed-to-retrieve-text}
            Showmessage('Cannot BACKUP Datebase: ' + #13#10 + e.Message);
            log := log + ' But finally FILED.';
          end;
        end;

        FormMain.MakeLog(2, log);
      end;
    end;

    Free;
  end;

end;

procedure TFrameControl.ButtonDeleteLogsClick(Sender: TObject);
begin
  DoStroredProc('EXEC DeleteAllLogs', 'Failed to Delete All Logs:');
  FormMain.MakeLog(2, 'User ''' + FormMain.UserName() + ''' has DELETED ALL LOGS.');
end;

procedure TFrameControl.ButtonDeleteRecordsClick(Sender: TObject);
begin
  DoStroredProc('EXEC DeleteAllRecords', 'Failed to Delete All Logs:');
  FormMain.MakeLog(2, 'User ''' + FormMain.UserName() + ''' has DELETED ALL RECORDS.');
end;

procedure TFrameControl.ButtonResetDBClick(Sender: TObject);
begin
  DoStroredProc('EXEC DeleteAll', 'Failed to Reset Database:');
  FormMain.MakeLog(2, 'User ''' + FormMain.UserName() + ''' has RESETED DATABASE.');
end;

procedure TFrameControl.ButtonRestoreDBClick(Sender: TObject);
var
  { http://www.delphibasics.co.uk/RTL.asp?Name=TOpenDialog }
  openDialog : TOpenDialog;
  log : WideString;
begin

  openDialog := TOpenDialog.Create(self);
  with openDialog do
  begin
    InitialDir  := GetCurrentDir;
    Options     := [ofFileMustExist];
    Filter      := 'backup files|*.bak';
    FilterIndex := 1;

    if Execute then
    begin
      with ADOCommandDataControl do
      begin
        log
          := 'User '''
          + FormMain.UserName()
          + ''' try to RESTORE Database'''
          + Connection.DefaultDatabase
          + '''.';

        CommandText
          :=' Use master'
          + ' RESTORE DATABASE ' + Connection.DefaultDatabase
          + ' FROM DISK = ''' + FileName + ''''
          + ' Use ' + Connection.DefaultDatabase;

        try Execute
        except on e: Exception do
          begin
            {http://stackoverflow.com/questions/2398385/sql-server-2008-backup-error-operating-system-error-5failed-to-retrieve-text}
            Showmessage('Cannot RESTORE Datebase:' + #13#10 + e.Message);
            log := log + ' But finally FILED.';
          end;
        end;

        FormMain.MakeLog(2, log);
      end;
    end;

    Free;
  end;

end;

procedure TFrameControl.DoStroredProc(cmd : widestring; err : widestring);
begin
  with ADOCommandDataControl do
  begin
    CommandText := cmd;
    try Execute
    except on e: Exception do
      showmessage(err + #13#10 + e.Message);
    end;
  end;
end;

end.
