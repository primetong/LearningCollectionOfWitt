object FrameRecords: TFrameRecords
  Left = 0
  Top = 0
  Width = 540
  Height = 360
  TabOrder = 0
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
        Value = 100.000000000000000000
      end>
    ControlCollection = <
      item
        Column = 0
        Control = DBGridRecords
        Row = 0
      end>
    RowCollection = <
      item
        Value = 100.000000000000000000
      end>
    ShowCaption = False
    TabOrder = 0
    object DBGridRecords: TDBGrid
      AlignWithMargins = True
      Left = 3
      Top = 3
      Width = 364
      Height = 354
      Align = alClient
      DataSource = DataSourceRecords
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
          FieldName = 'Process'
          Width = 128
          Visible = True
        end
        item
          Expanded = False
          FieldName = 'Owner'
          Width = 57
          Visible = True
        end
        item
          Expanded = False
          FieldName = 'Date'
          Visible = True
        end
        item
          Expanded = False
          FieldName = 'Time'
          Width = 48
          Visible = True
        end
        item
          Expanded = False
          FieldName = 'CPU Usage (s)'
          Visible = True
        end
        item
          Expanded = False
          FieldName = 'Memary Usage (M)'
          Visible = True
        end
        item
          Expanded = False
          FieldName = 'Duration (s)'
          Visible = True
        end>
    end
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
      end
      item
        Column = 0
        Control = GridPanelFilter
        Row = 1
      end>
    RowCollection = <
      item
        Value = 10.488342912361060000
      end
      item
        Value = 41.553799865627960000
      end
      item
        Value = 20.957861528488750000
      end
      item
        Value = 8.999997364018238000
      end
      item
        Value = 8.999998765703433000
      end
      item
        Value = 8.999999563800550000
      end
      item
        SizeStyle = ssAuto
      end>
    ShowCaption = False
    TabOrder = 1
    DesignSize = (
      170
      360)
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
      Caption = '&Delete Current Records'
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
      ExplicitLeft = 69
      ExplicitTop = 12
    end
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
          Control = LabelRecordTimeBegin
          Row = 0
        end
        item
          Column = 0
          Control = LabelRecordTimeEnd
          Row = 1
        end
        item
          Column = 0
          Control = LabelRecordType
          Row = 2
        end
        item
          Column = 1
          Control = EditRecordName
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
        end
        item
          Column = 0
          Control = LabelOwner
          Row = 3
        end
        item
          Column = 1
          Control = EditRecordOwner
          Row = 3
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
      object LabelRecordTimeBegin: TLabel
        Left = 10
        Top = 8
        Width = 26
        Height = 13
        Anchors = []
        Caption = 'Begin'
        ExplicitLeft = 13
      end
      object LabelRecordTimeEnd: TLabel
        Left = 14
        Top = 37
        Width = 18
        Height = 13
        Anchors = []
        Caption = 'End'
        ExplicitLeft = 18
        ExplicitTop = 126
      end
      object LabelRecordType: TLabel
        Left = 4
        Top = 66
        Width = 37
        Height = 13
        Anchors = []
        Caption = 'Process'
        ExplicitLeft = 7
        ExplicitTop = 8
      end
      object EditRecordName: TEdit
        Left = 53
        Top = 62
        Width = 110
        Height = 21
        Anchors = []
        TabOrder = 2
        TextHint = 'Sample.exe'
        OnChange = EditRecordFilterChange
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
        OnChange = DateTimePickerChange
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
        OnChange = DateTimePickerChange
      end
      object LabelOwner: TLabel
        Left = 7
        Top = 95
        Width = 32
        Height = 13
        Anchors = []
        Caption = 'Owner'
        ExplicitLeft = 15
        ExplicitTop = 36
      end
      object EditRecordOwner: TEdit
        Left = 53
        Top = 91
        Width = 110
        Height = 21
        Anchors = []
        TabOrder = 3
        TextHint = 'System'
        OnChange = EditRecordFilterChange
      end
    end
  end
  object DataSourceRecords: TDataSource
    DataSet = ADODataSetRecords
    Left = 328
    Top = 280
  end
  object ADODataSetRecords: TADODataSet
    Active = True
    Connection = FormMain.Database
    CursorType = ctStatic
    CommandText = 
      'SELECT '#13#10'    name as '#39'Process'#39','#13#10'    creator as '#39'Owner'#39','#13#10'    re' +
      'cord_date as '#39'Date'#39','#13#10'    convert(varchar(32), period, 24) as '#39'T' +
      'ime'#39','#13#10'    cpu_usage as '#39'CPU Usage (s)'#39','#13#10'    mem_usage as '#39'Mema' +
      'ry Usage (M)'#39','#13#10'    duration as '#39'Duration (s)'#39#13#10'FROM'#13#10'    Record' +
      'Table'#13#10'WHERE'#13#10'    :DateBegin <= record_date AND record_date <= :' +
      'DateEnd'
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
      end>
    Left = 224
    Top = 280
  end
  object ADOCommandDelete: TADOCommand
    Connection = FormMain.Database
    Parameters = <>
    Left = 432
    Top = 280
  end
end
