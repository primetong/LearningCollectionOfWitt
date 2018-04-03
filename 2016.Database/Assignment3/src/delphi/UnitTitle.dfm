object FrameTitle: TFrameTitle
  Left = 0
  Top = 0
  Width = 540
  Height = 360
  TabOrder = 0
  object ScrollBoxTitle: TScrollBox
    Left = 0
    Top = 0
    Width = 540
    Height = 360
    Align = alClient
    BevelInner = bvNone
    BevelOuter = bvNone
    BorderStyle = bsNone
    TabOrder = 0
    ExplicitHeight = 348
    object PanelTitle: TPanel
      Left = 0
      Top = 0
      Width = 540
      Height = 185
      Align = alTop
      BevelOuter = bvNone
      Caption = 'PanelTitle'
      ShowCaption = False
      TabOrder = 0
      object LabelTitle: TLabel
        Left = 32
        Top = 24
        Width = 39
        Height = 19
        Caption = 'Info:'
        Font.Charset = DEFAULT_CHARSET
        Font.Color = clWindowText
        Font.Height = -16
        Font.Name = 'Tahoma'
        Font.Style = [fsBold]
        ParentFont = False
      end
      object LabeledEditDataBase: TLabeledEdit
        Left = 32
        Top = 80
        Width = 256
        Height = 21
        TabStop = False
        EditLabel.Width = 122
        EditLabel.Height = 16
        EditLabel.Caption = 'Connected Database:'
        EditLabel.Font.Charset = DEFAULT_CHARSET
        EditLabel.Font.Color = clWindowText
        EditLabel.Font.Height = -13
        EditLabel.Font.Name = 'Tahoma'
        EditLabel.Font.Style = []
        EditLabel.ParentFont = False
        Enabled = False
        ReadOnly = True
        TabOrder = 0
      end
      object LabeledEditUserName: TLabeledEdit
        Left = 32
        Top = 135
        Width = 256
        Height = 21
        EditLabel.Width = 68
        EditLabel.Height = 16
        EditLabel.Caption = 'User Name:'
        EditLabel.Font.Charset = DEFAULT_CHARSET
        EditLabel.Font.Color = clWindowText
        EditLabel.Font.Height = -13
        EditLabel.Font.Name = 'Tahoma'
        EditLabel.Font.Style = []
        EditLabel.ParentFont = False
        Enabled = False
        ReadOnly = True
        TabOrder = 1
      end
    end
  end
end
