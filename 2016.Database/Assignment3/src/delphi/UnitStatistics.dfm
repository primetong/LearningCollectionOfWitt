object FrameStatistics: TFrameStatistics
  Left = 0
  Top = 0
  Width = 540
  Height = 360
  TabOrder = 0
  object GridPanelLeft: TGridPanel
    Left = 0
    Top = 0
    Width = 170
    Height = 360
    Align = alLeft
    BevelOuter = bvNone
    Caption = 'GridPanelLeft'
    ColumnCollection = <
      item
        Value = 100.000000000000000000
      end>
    ControlCollection = <
      item
        Column = 0
        Control = GridPanelFilter
        Row = 1
      end
      item
        Column = 0
        Control = LabelCondition
        Row = 0
      end
      item
        Column = 0
        Control = ButtonClear
        Row = 4
      end
      item
        Column = 0
        Control = ButtonSwitchIsRecording
        Row = 5
      end>
    RowCollection = <
      item
        Value = 10.454423219954890000
      end
      item
        Value = 41.568728304157130000
      end
      item
        Value = 20.985203860818730000
      end
      item
        Value = 8.997214871689756000
      end
      item
        Value = 8.997214871689756000
      end
      item
        Value = 8.997214871689756000
      end
      item
        SizeStyle = ssAuto
      end>
    ShowCaption = False
    TabOrder = 0
    DesignSize = (
      170
      360)
    object GridPanelFilter: TGridPanel
      Left = 0
      Top = 37
      Width = 170
      Height = 149
      Align = alClient
      BevelOuter = bvNone
      Caption = 'GridPanelFilter'
      ColumnCollection = <
        item
          Value = 27.095125962531180000
        end
        item
          Value = 72.904874037468820000
        end>
      ControlCollection = <
        item
          Column = 0
          Control = LabelStatisticsDateBegin
          Row = 0
        end
        item
          Column = 1
          Control = DateTimePickerBegin
          Row = 0
        end
        item
          Column = 0
          Control = LabelStatisticsDateEnd
          Row = 1
        end
        item
          Column = 1
          Control = DateTimePickerEnd
          Row = 1
        end
        item
          Column = 0
          Control = LabelStatisticsProcess
          Row = 2
        end
        item
          Column = 1
          Control = EditStatisticsProcess
          Row = 2
        end
        item
          Column = 1
          Control = ComboBoxIsRecording
          Row = 4
        end
        item
          Column = 0
          Control = LabelIsRecording
          Row = 4
        end
        item
          Column = 0
          Control = LabelOwner
          Row = 3
        end
        item
          Column = 1
          Control = EditStatisticsOwner
          Row = 3
        end>
      RowCollection = <
        item
          Value = 19.999999999999990000
        end
        item
          Value = 19.999999999999990000
        end
        item
          Value = 20.000000000000010000
        end
        item
          Value = 20.000000000000010000
        end
        item
          Value = 20.000000000000010000
        end>
      ShowCaption = False
      TabOrder = 2
      DesignSize = (
        170
        149)
      object LabelStatisticsDateBegin: TLabel
        Left = 10
        Top = 8
        Width = 26
        Height = 13
        Anchors = []
        Caption = 'Begin'
        ExplicitLeft = 0
      end
      object DateTimePickerBegin: TDateTimePicker
        Left = 53
        Top = 4
        Width = 110
        Height = 21
        Anchors = []
        Date = 25569.000000000000000000
        Time = 25569.000000000000000000
        DateFormat = dfLong
        ParentShowHint = False
        ShowHint = False
        TabOrder = 0
        OnChange = OnDateTimePickerBeginChange
      end
      object LabelStatisticsDateEnd: TLabel
        Left = 14
        Top = 37
        Width = 18
        Height = 13
        Anchors = []
        Caption = 'End'
        ExplicitLeft = 0
      end
      object DateTimePickerEnd: TDateTimePicker
        Left = 53
        Top = 33
        Width = 110
        Height = 21
        Anchors = []
        Date = 62094.000000000000000000
        Time = 62094.000000000000000000
        DateFormat = dfLong
        TabOrder = 1
        OnChange = OnDateTimePickerEndChange
      end
      object LabelStatisticsProcess: TLabel
        Left = 4
        Top = 66
        Width = 37
        Height = 13
        Anchors = []
        Caption = 'Process'
        ExplicitLeft = 0
      end
      object EditStatisticsProcess: TEdit
        Left = 53
        Top = 62
        Width = 110
        Height = 21
        Anchors = []
        TabOrder = 2
        TextHint = 'Sample.exe'
        OnChange = EditStatisticFilterChange
      end
      object ComboBoxIsRecording: TComboBox
        Left = 53
        Top = 121
        Width = 110
        Height = 22
        Style = csOwnerDrawFixed
        Anchors = []
        ItemIndex = 0
        TabOrder = 4
        Text = 'Enabled'
        OnChange = ComboBoxIsRecordingChange
        Items.Strings = (
          'Enabled'
          'Disabled'
          'All')
      end
      object LabelIsRecording: TLabel
        Left = 1
        Top = 127
        Width = 43
        Height = 11
        Anchors = []
        Caption = 'Monitoring'
        Font.Charset = DEFAULT_CHARSET
        Font.Color = clWindowText
        Font.Height = -9
        Font.Name = 'Tahoma'
        Font.Style = []
        ParentFont = False
        ExplicitLeft = 0
        ExplicitTop = 126
      end
      object LabelOwner: TLabel
        Left = 7
        Top = 95
        Width = 32
        Height = 13
        Anchors = []
        Caption = 'Owner'
        ExplicitLeft = 0
      end
      object EditStatisticsOwner: TEdit
        Left = 53
        Top = 91
        Width = 110
        Height = 21
        Anchors = []
        TabOrder = 3
        TextHint = 'System'
        OnChange = EditStatisticFilterChange
      end
    end
    object LabelCondition: TLabel
      Left = 62
      Top = 9
      Width = 46
      Height = 18
      Anchors = []
      Caption = 'Filter:'
      Font.Charset = DEFAULT_CHARSET
      Font.Color = clWindowText
      Font.Height = -15
      Font.Name = 'Tahoma'
      Font.Style = [fsBold]
      ParentFont = False
      ExplicitLeft = 69
      ExplicitTop = 12
    end
    object ButtonClear: TButton
      AlignWithMargins = True
      Left = 3
      Top = 296
      Width = 164
      Height = 26
      Align = alClient
      Caption = 'Reset &Filter'
      TabOrder = 0
      OnClick = ButtonClearClick
    end
    object ButtonSwitchIsRecording: TButton
      AlignWithMargins = True
      Left = 3
      Top = 328
      Width = 164
      Height = 26
      Align = alClient
      Caption = '[Dis/En]&able Process Recording'
      TabOrder = 1
      OnClick = ButtonSwitchIsRecordingClick
    end
  end
  object GridPanelRight: TGridPanel
    Left = 170
    Top = 0
    Width = 370
    Height = 360
    Align = alClient
    BevelOuter = bvNone
    Caption = 'GridPanelRight'
    ColumnCollection = <
      item
        SizeStyle = ssAbsolute
        Value = 196.000000000000000000
      end
      item
        SizeStyle = ssAuto
      end
      item
        Value = 100.000000000000000000
      end>
    ControlCollection = <
      item
        Column = 0
        Control = DBGridProcess
        Row = 0
      end
      item
        Column = 2
        Control = DBGridStatistics
        Row = 0
      end
      item
        Column = 1
        Control = SplitterMid
        Row = 0
      end>
    RowCollection = <
      item
        Value = 100.000000000000000000
      end>
    ShowCaption = False
    TabOrder = 1
    object DBGridProcess: TDBGrid
      AlignWithMargins = True
      Left = 3
      Top = 3
      Width = 193
      Height = 354
      Margins.Right = 0
      Align = alClient
      DataSource = DataSourceProcess
      ReadOnly = True
      TabOrder = 0
      TitleFont.Charset = DEFAULT_CHARSET
      TitleFont.Color = clWindowText
      TitleFont.Height = -11
      TitleFont.Name = 'Tahoma'
      TitleFont.Style = []
      Columns = <
        item
          Expanded = False
          FieldName = 'Process'
          Width = 112
          Visible = True
        end
        item
          Expanded = False
          FieldName = 'Owner'
          Width = 43
          Visible = True
        end>
    end
    object DBGridStatistics: TDBGrid
      AlignWithMargins = True
      Left = 199
      Top = 3
      Width = 168
      Height = 354
      Margins.Left = 0
      Align = alClient
      DataSource = DataSourceStatistics
      ReadOnly = True
      TabOrder = 1
      TitleFont.Charset = DEFAULT_CHARSET
      TitleFont.Color = clWindowText
      TitleFont.Height = -11
      TitleFont.Name = 'Tahoma'
      TitleFont.Style = []
      Columns = <
        item
          Expanded = False
          FieldName = 'Property'
          Width = 136
          Visible = True
        end
        item
          Expanded = False
          FieldName = 'Value'
          Width = 48
          Visible = True
        end>
    end
    object SplitterMid: TSplitter
      Left = 196
      Top = 0
      Height = 360
      OnCanResize = SplitterMidCanResize
    end
  end
  object ADODataSetProcess: TADODataSet
    Active = True
    Connection = FormMain.Database
    CursorType = ctStatic
    CommandText = 
      'SELECT '#13#10'    name as '#39'Process'#39','#13#10'    creator as '#39'Owner'#39','#13#10'    is' +
      '_recording as '#39'Recording'#39#13#10'FROM  '#13#10'    ProcessTable'#13#10'WHERE'#13#10'    ' +
      ':DateBegin <= date_end AND '#13#10'    date_begin <= :DateEnd AND'#13#10'   ' +
      ' (:SkipIsRecording = 1 OR is_recording = :IsRecording)'
    Parameters = <
      item
        Name = 'DateBegin'
        Attributes = [paNullable]
        DataType = ftDateTime
        Precision = 10
        Size = 6
        Value = 25569d
      end
      item
        Name = 'DateEnd'
        Attributes = [paNullable]
        DataType = ftDateTime
        Precision = 10
        Size = 6
        Value = 62094d
      end
      item
        Name = 'SkipIsRecording'
        Attributes = [paSigned, paNullable]
        DataType = ftInteger
        Precision = 10
        Size = 4
        Value = 0
      end
      item
        Name = 'IsRecording'
        Attributes = [paNullable]
        DataType = ftBoolean
        NumericScale = 255
        Precision = 255
        Size = 2
        Value = True
      end>
    Left = 248
    Top = 304
  end
  object DataSourceProcess: TDataSource
    DataSet = ADODataSetProcess
    Left = 248
    Top = 248
  end
  object ADODataSetStatisticsSingle: TADODataSet
    Active = True
    Connection = FormMain.Database
    CursorType = ctStatic
    CommandText = 
      'EXEC SelectStatisticsSingle :Process, :Owner, :DateBegin, :DateE' +
      'nd'
    DataSource = DataSourceProcess
    IndexFieldNames = 'Process;Owner'
    MasterFields = 'Process;Owner'
    Parameters = <
      item
        Name = 'Process'
        Attributes = [paNullable]
        DataType = ftString
        NumericScale = 255
        Precision = 255
        Size = 127
        Value = 'chrome.exe'
      end
      item
        Name = 'Owner'
        Attributes = [paNullable]
        DataType = ftString
        NumericScale = 255
        Precision = 255
        Size = 127
        Value = 'Administrator'
      end
      item
        Name = 'DateBegin'
        Attributes = [paNullable]
        DataType = ftDateTime
        Precision = 10
        Size = 6
        Value = 25569d
      end
      item
        Name = 'DateEnd'
        Attributes = [paNullable]
        DataType = ftDateTime
        Precision = 10
        Size = 6
        Value = 62094d
      end>
    Left = 440
    Top = 296
  end
  object DataSourceStatistics: TDataSource
    DataSet = ADODataSetStatisticsSingle
    Left = 440
    Top = 248
  end
end
