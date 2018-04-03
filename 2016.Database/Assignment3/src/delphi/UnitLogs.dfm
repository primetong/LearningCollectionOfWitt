object FrameLogs: TFrameLogs
  Left = 0
  Top = 0
  Width = 540
  Height = 360
  TabOrder = 0
  object DBGridLogs: TDBGrid
    AlignWithMargins = True
    Left = 173
    Top = 3
    Width = 364
    Height = 354
    Align = alClient
    DataSource = DataSourceLogs
    ReadOnly = True
    TabOrder = 0
    TitleFont.Charset = DEFAULT_CHARSET
    TitleFont.Color = clWindowText
    TitleFont.Height = -11
    TitleFont.Name = 'Tahoma'
    TitleFont.Style = []
    OnKeyDown = DBGridLogsKeyDown
    Columns = <
      item
        Expanded = False
        FieldName = 'Time'
        Width = 128
        Visible = True
      end
      item
        Expanded = False
        FieldName = 'Type'
        Width = 32
        Visible = True
      end
      item
        Expanded = False
        FieldName = 'Message'
        Width = 220
        Visible = True
      end>
  end
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
        Control = ButtonClear
        Row = 4
      end
      item
        Column = 0
        Control = ButtonDelete
        Row = 5
      end
      item
        Column = 0
        Control = LabelCondition
        Row = 0
      end>
    RowCollection = <
      item
        Value = 10.461657282441060000
      end
      item
        Value = 41.574000336528840000
      end
      item
        Value = 20.964342381030100000
      end
      item
        Value = 9.000000000000000000
      end
      item
        Value = 9.000000000000000000
      end
      item
        Value = 9.000000000000000000
      end
      item
        SizeStyle = ssAuto
      end>
    ShowCaption = False
    TabOrder = 1
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
          Control = LabelLogTimeBegin
          Row = 0
        end
        item
          Column = 0
          Control = LabelLogTimeEnd
          Row = 1
        end
        item
          Column = 0
          Control = LabelLogType
          Row = 2
        end
        item
          Column = 1
          Control = EditLogType
          Row = 2
        end
        item
          Column = 1
          Control = DateTimePickerBegin
          Row = 0
        end
        item
          Column = 1
          Control = DateTimePickerEnd
          Row = 1
        end>
      RowCollection = <
        item
          Value = 20.000000000000000000
        end
        item
          Value = 20.000000000000000000
        end
        item
          Value = 20.000000000000000000
        end
        item
          Value = 20.000000000000000000
        end
        item
          Value = 20.000000000000000000
        end>
      ShowCaption = False
      TabOrder = 2
      DesignSize = (
        170
        149)
      object LabelLogTimeBegin: TLabel
        Left = 10
        Top = 8
        Width = 26
        Height = 13
        Anchors = []
        Caption = 'Begin'
        ExplicitLeft = 13
      end
      object LabelLogTimeEnd: TLabel
        Left = 14
        Top = 37
        Width = 18
        Height = 13
        Anchors = []
        Caption = 'End'
        ExplicitLeft = 20
        ExplicitTop = 30
      end
      object LabelLogType: TLabel
        Left = 11
        Top = 66
        Width = 24
        Height = 13
        Anchors = []
        Caption = 'Type'
        ExplicitLeft = 17
        ExplicitTop = 55
      end
      object EditLogType: TEdit
        Left = 53
        Top = 62
        Width = 110
        Height = 21
        Anchors = []
        NumbersOnly = True
        TabOrder = 2
        TextHint = '0'
        OnChange = OnEditLogFliterChange
        ExplicitLeft = 52
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
        TabOrder = 0
        OnChange = OnDateTimePickerChange
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
        OnChange = OnDateTimePickerChange
      end
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
    object ButtonDelete: TButton
      AlignWithMargins = True
      Left = 3
      Top = 328
      Width = 164
      Height = 26
      Align = alClient
      Caption = '&Delete Current Logs'
      TabOrder = 1
      OnClick = ButtonDeleteClick
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
      ExplicitLeft = 66
      ExplicitTop = 10
    end
  end
  object DataSourceLogs: TDataSource
    DataSet = ADOQueryLogs
    Left = 448
    Top = 280
  end
  object ADOCommandDelete: TADOCommand
    CommandText = #13#10'    '
    Connection = FormMain.Database
    Parameters = <>
    Left = 344
    Top = 280
  end
  object ADOQueryLogs: TADOQuery
    Active = True
    Connection = FormMain.Database
    CursorType = ctStatic
    Parameters = <
      item
        Name = 'DateBegin'
        Attributes = [paNullable]
        DataType = ftDateTime
        NumericScale = 3
        Precision = 23
        Size = 16
        Value = 25569d
      end
      item
        Name = 'DateEnd'
        Attributes = [paNullable]
        DataType = ftDateTime
        NumericScale = 3
        Precision = 23
        Size = 16
        Value = 62094d
      end>
    SQL.Strings = (
      'SELECT'
      '    log_datetime as '#39'Time'#39','
      '    log_level as '#39'Type'#39','
      '    log_message as '#39'Message'#39
      'FROM'
      '    LogTable'
      'WHERE'
      '    :DateBegin <= log_datetime AND log_datetime <= :DateEnd')
    Left = 240
    Top = 280
  end
end
