object FrameControl: TFrameControl
  Left = 0
  Top = 0
  Width = 540
  Height = 360
  TabOrder = 0
  object PageControlMain: TPageControl
    Left = 0
    Top = 0
    Width = 540
    Height = 360
    ActivePage = TabSheetAbout
    Align = alClient
    Font.Charset = DEFAULT_CHARSET
    Font.Color = clWindowText
    Font.Height = -13
    Font.Name = 'Tahoma'
    Font.Style = []
    ParentFont = False
    TabOrder = 0
    object TabSheetBasicSettings: TTabSheet
      Caption = '&Basic Settings'
      Font.Charset = DEFAULT_CHARSET
      Font.Color = clWindowText
      Font.Height = -11
      Font.Name = 'Tahoma'
      Font.Style = []
      ParentFont = False
      object ScrollBoxBasic: TScrollBox
        Left = 0
        Top = 0
        Width = 532
        Height = 329
        Align = alClient
        BevelInner = bvNone
        BevelOuter = bvNone
        BorderStyle = bsNone
        ParentBackground = True
        TabOrder = 0
        object GridPanelBasic: TGridPanel
          Left = 0
          Top = 0
          Width = 532
          Height = 297
          Align = alTop
          BevelOuter = bvNone
          Caption = 'GridPanelData'
          ColumnCollection = <
            item
              Value = 100.000000000000000000
            end>
          ControlCollection = <>
          RowCollection = <
            item
              SizeStyle = ssAbsolute
              Value = 48.000000000000000000
            end
            item
              SizeStyle = ssAbsolute
              Value = 48.000000000000000000
            end
            item
              SizeStyle = ssAbsolute
              Value = 48.000000000000000000
            end
            item
              SizeStyle = ssAbsolute
              Value = 48.000000000000000000
            end
            item
              SizeStyle = ssAbsolute
              Value = 48.000000000000000000
            end
            item
              SizeStyle = ssAbsolute
              Value = 48.000000000000000000
            end
            item
              SizeStyle = ssAbsolute
              Value = 48.000000000000000000
            end
            item
              SizeStyle = ssAuto
            end>
          ShowCaption = False
          TabOrder = 0
        end
      end
    end
    object TabSheetDataControl: TTabSheet
      Caption = '&Data Control'
      ImageIndex = 1
      object ScrollBoxDate: TScrollBox
        Left = 0
        Top = 0
        Width = 532
        Height = 329
        Align = alClient
        BevelInner = bvNone
        BevelOuter = bvNone
        BorderStyle = bsNone
        ParentBackground = True
        TabOrder = 0
        object GridPanelData: TGridPanel
          Left = 0
          Top = 0
          Width = 532
          Height = 297
          Align = alTop
          BevelOuter = bvNone
          Caption = 'GridPanelData'
          ColumnCollection = <
            item
              Value = 100.000000000000000000
            end>
          ControlCollection = <
            item
              Column = 0
              Control = ButtonDeleteLogs
              Row = 0
            end
            item
              Column = 0
              Control = ButtonDeleteRecords
              Row = 1
            end
            item
              Column = 0
              Control = ButtonResetDB
              Row = 2
            end
            item
              Column = 0
              Control = ButtonBackupDB
              Row = 4
            end
            item
              Column = 0
              Control = ButtonRestoreDB
              Row = 5
            end>
          RowCollection = <
            item
              SizeStyle = ssAbsolute
              Value = 48.000000000000000000
            end
            item
              SizeStyle = ssAbsolute
              Value = 48.000000000000000000
            end
            item
              SizeStyle = ssAbsolute
              Value = 48.000000000000000000
            end
            item
              SizeStyle = ssAbsolute
              Value = 48.000000000000000000
            end
            item
              SizeStyle = ssAbsolute
              Value = 48.000000000000000000
            end
            item
              SizeStyle = ssAbsolute
              Value = 48.000000000000000000
            end
            item
              SizeStyle = ssAbsolute
              Value = 48.000000000000000000
            end
            item
              SizeStyle = ssAuto
            end>
          ShowCaption = False
          TabOrder = 0
          object ButtonDeleteLogs: TButton
            AlignWithMargins = True
            Left = 3
            Top = 3
            Width = 526
            Height = 42
            Align = alClient
            BiDiMode = bdLeftToRight
            Caption = 'Delete All Logs'
            ParentBiDiMode = False
            Style = bsCommandLink
            TabOrder = 0
            OnClick = ButtonDeleteLogsClick
          end
          object ButtonDeleteRecords: TButton
            AlignWithMargins = True
            Left = 3
            Top = 51
            Width = 526
            Height = 42
            Align = alClient
            BiDiMode = bdLeftToRight
            Caption = 'Delete All Records'
            ParentBiDiMode = False
            Style = bsCommandLink
            TabOrder = 1
            OnClick = ButtonDeleteRecordsClick
          end
          object ButtonResetDB: TButton
            AlignWithMargins = True
            Left = 3
            Top = 99
            Width = 526
            Height = 42
            Align = alClient
            BiDiMode = bdLeftToRight
            Caption = 'Reset Datebase'
            ParentBiDiMode = False
            Style = bsCommandLink
            TabOrder = 2
            OnClick = ButtonResetDBClick
          end
          object ButtonBackupDB: TButton
            AlignWithMargins = True
            Left = 3
            Top = 195
            Width = 526
            Height = 42
            Align = alClient
            BiDiMode = bdLeftToRight
            Caption = 'Backup Datebase'
            ParentBiDiMode = False
            Style = bsCommandLink
            TabOrder = 3
            OnClick = ButtonBackupDBClick
          end
          object ButtonRestoreDB: TButton
            AlignWithMargins = True
            Left = 3
            Top = 243
            Width = 526
            Height = 42
            Align = alClient
            BiDiMode = bdLeftToRight
            Caption = 'Restore Datebase'
            ParentBiDiMode = False
            Style = bsCommandLink
            TabOrder = 4
            OnClick = ButtonRestoreDBClick
          end
        end
      end
    end
    object TabSheetServiceControl: TTabSheet
      Caption = 'S&ervice Control'
      ImageIndex = 2
      object ScrollBoxService: TScrollBox
        Left = 0
        Top = 0
        Width = 532
        Height = 329
        Align = alClient
        BevelInner = bvNone
        BevelOuter = bvNone
        BorderStyle = bsNone
        ParentBackground = True
        TabOrder = 0
        object GridPanelService: TGridPanel
          Left = 0
          Top = 0
          Width = 532
          Height = 153
          Align = alTop
          BevelOuter = bvNone
          Caption = 'GridPanelData'
          ColumnCollection = <
            item
              Value = 100.000000000000000000
            end>
          ControlCollection = <
            item
              Column = 0
              Control = ButtonStartService
              Row = 0
            end
            item
              Column = 0
              Control = ButtonPauseService
              Row = 1
            end
            item
              Column = 0
              Control = ButtonRemoveService
              Row = 2
            end>
          RowCollection = <
            item
              SizeStyle = ssAbsolute
              Value = 48.000000000000000000
            end
            item
              SizeStyle = ssAbsolute
              Value = 48.000000000000000000
            end
            item
              SizeStyle = ssAbsolute
              Value = 48.000000000000000000
            end
            item
              SizeStyle = ssAbsolute
              Value = 48.000000000000000000
            end
            item
              SizeStyle = ssAbsolute
              Value = 48.000000000000000000
            end
            item
              SizeStyle = ssAbsolute
              Value = 48.000000000000000000
            end
            item
              SizeStyle = ssAbsolute
              Value = 48.000000000000000000
            end
            item
              SizeStyle = ssAuto
            end>
          ShowCaption = False
          TabOrder = 0
          object ButtonStartService: TButton
            AlignWithMargins = True
            Left = 3
            Top = 3
            Width = 526
            Height = 42
            Align = alClient
            BiDiMode = bdLeftToRight
            Caption = 'Start Service'
            Enabled = False
            ParentBiDiMode = False
            Style = bsCommandLink
            TabOrder = 0
            OnClick = ButtonDeleteLogsClick
          end
          object ButtonPauseService: TButton
            AlignWithMargins = True
            Left = 3
            Top = 51
            Width = 526
            Height = 42
            Align = alClient
            BiDiMode = bdLeftToRight
            Caption = 'Pause Service'
            Enabled = False
            ParentBiDiMode = False
            Style = bsCommandLink
            TabOrder = 1
            OnClick = ButtonDeleteRecordsClick
          end
          object ButtonRemoveService: TButton
            AlignWithMargins = True
            Left = 3
            Top = 99
            Width = 526
            Height = 42
            Align = alClient
            BiDiMode = bdLeftToRight
            Caption = 'Remove Service'
            Enabled = False
            ParentBiDiMode = False
            Style = bsCommandLink
            TabOrder = 2
            OnClick = ButtonResetDBClick
          end
        end
      end
    end
    object TabSheetAbout: TTabSheet
      Caption = '&About'
      object RichEditAbout: TRichEdit
        AlignWithMargins = True
        Left = 20
        Top = 20
        Width = 492
        Height = 289
        Margins.Left = 20
        Margins.Top = 20
        Margins.Right = 20
        Margins.Bottom = 20
        Align = alClient
        BevelInner = bvNone
        BevelOuter = bvNone
        BorderStyle = bsNone
        Font.Charset = GB2312_CHARSET
        Font.Color = clWindowText
        Font.Height = -13
        Font.Name = 'Tahoma'
        Font.Style = []
        Lines.Strings = (
          'Welcome to Process Monitor!'
          ''
          'This software is designed for my assigement of datebase course'
          '-- ASSIGNMENT 3 : Design and Implementation.'
          ''
          'by MYLS')
        ParentFont = False
        ReadOnly = True
        ScrollBars = ssVertical
        TabOrder = 0
        WantTabs = True
      end
    end
  end
  object ADOCommandDataControl: TADOCommand
    CommandTimeout = 10
    Connection = FormMain.Database
    Parameters = <>
    Left = 456
    Top = 16
  end
end
