unit UnitTitle;

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms, 
  Dialogs, StdCtrls, ExtCtrls, UnitMain;

type
  TFrameTitle = class(TFrame)
    ScrollBoxTitle: TScrollBox;
    PanelTitle: TPanel;
    LabeledEditDataBase: TLabeledEdit;
    LabeledEditUserName: TLabeledEdit;
    LabelTitle: TLabel;
  private
    { Private declarations }
  public
    { Public declarations }
    constructor Create(AOwner: TComponent) ; override;
  end;

implementation

{$R *.dfm}

constructor TFrameTitle.Create(AOwner: TComponent);
begin
  { inital main }
  inherited Create(AOwner);

  LabeledEditDataBase.Text := FormMain.Database.DefaultDatabase;
  LabeledEditUserName.Text := FormMain.UserName;
end;

end.
