unit UnitMain;

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, ComCtrls, Tabs, ExtCtrls, ToolWin, ImgList, ActnList, Menus, ADODB,
  DB;

type
  TFormMain = class(TForm)
    ImageList: TImageList;
    MainToolBar: TToolBar;
    ToolButtonLogs: TToolButton;
    ActionList: TActionList;
    Database: TADOConnection;
    PanelMain: TPanel;
    ActionSwitch2TabLogs: TAction;
    ActionSwitch2TabSettings: TAction;
    ToolButtonControl: TToolButton;
    ToolButtonRecords: TToolButton;
    ToolButtonStatistics: TToolButton;
    ToolButtonTitle: TToolButton;
    ActionSwitch2TabTitle: TAction;
    ActionSwitch2TabStatistics: TAction;
    ActionSwitch2TabRecords: TAction;
    ToolButtonSeparator1: TToolButton;
    ADOCommandInsertLog: TADOCommand;
    procedure ActionSwitch2TabLogsExecute(Sender: TObject);
    procedure ActionSwitch2TabSettingsExecute(Sender: TObject);
    procedure ActionSwitch2TabStatisticsExecute(Sender: TObject);
    procedure ActionSwitch2TabTitleExecute(Sender: TObject);
    procedure ActionSwitch2TabRecordsExecute(Sender: TObject);
  private
    { Private declarations }
  public
    { Public declarations }
    constructor Create(AOwner: TComponent) ; override;
    procedure MakeLog(log_type : Word; log_message : WideString);
    function UserName() : string;
  end;

var
  FormMain: TFormMain;

implementation

uses UnitLogs, UnitRecords, UnitTitle, UnitStatistics, UnitControl;

var
  FormTitle: TFrameTitle;
  FormStatistics: TFrameStatistics;
  FormRecords: TFrameRecords;
  FormLogs: TFrameLogs;
  FormControl : TFrameControl;

{$R *.dfm}

procedure TFormMain.ActionSwitch2TabLogsExecute(Sender: TObject);
var
  flag : bool;
begin
  flag := true;
  try
    FormLogs.UpdateDataGrid;
  except on e:Exception do
  begin
     flag := false;
     ShowMessage('Error :' + #13#10 + e.Message);
  end;
  end;

  if flag then
     FormLogs.BringToFront
  else
  begin
    ToolButtonLogs.Down := False;
    ToolButtonTitle.Down := True;
  end;
end;

procedure TFormMain.ActionSwitch2TabRecordsExecute(Sender: TObject);
var
  flag : bool;
begin
  flag := true;
  try
    FormRecords.UpdateDataGrid;
  except on e:Exception do
  begin
     flag := false;
     ShowMessage('Error :' + #13#10 + e.Message);
  end;
  end;

  if flag then
     FormRecords.BringToFront
  else
  begin
    ToolButtonLogs.Down := False;
    ToolButtonTitle.Down := True;
  end;
end;

procedure TFormMain.ActionSwitch2TabSettingsExecute(Sender: TObject);
begin
  FormControl.BringToFront;
end;

procedure TFormMain.ActionSwitch2TabStatisticsExecute(Sender: TObject);
var
  flag : bool;
begin
  flag := true;
  try
    FormStatistics.UpdateDataGrid;
  except on e:Exception do
  begin
     flag := false;
     ShowMessage('Error :' + #13#10 + e.Message);
  end;
  end;

  if flag then
     FormStatistics.BringToFront
  else
  begin
    ToolButtonLogs.Down := False;
    ToolButtonTitle.Down := True;
    FormTitle.BringToFront;
  end;
end;

procedure TFormMain.ActionSwitch2TabTitleExecute(Sender: TObject);
begin
  FormTitle.BringToFront;
end;

{ http://delphi.about.com/od/delphitips2007/qt/tframe_oncreate.htm }
constructor TFormMain.Create(AOwner: TComponent);
begin
  { inital main }
  inherited Create(AOwner);

  { init title }
  FormTitle := TFrameTitle.Create(nil);
  with FormTitle do
  begin
    Parent := PanelMain;
    Align := alClient;
  end;

  if Database.Connected then
  begin
    { init each form }
    FormLogs := TFrameLogs.Create(nil);
    with FormLogs do
    begin
      Parent := PanelMain;
      Align := alClient;
    end;

    FormRecords := TFrameRecords.Create(nil);
    with FormRecords do
    begin
      Parent := PanelMain;
      Align := alClient;
    end;

    FormControl := TFrameControl.Create(nil);
    with FormControl do
    begin
      Parent := PanelMain;
      Align := alClient;
    end;

    FormStatistics := TFrameStatistics.Create(nil);
    with FormStatistics do
    begin
      Parent := PanelMain;
      Align := alClient;
    end;

    { make a log }
    MakeLog(0, 'User ''' + UserName() + ''' login ProcessMonitor.');

    { enable login }

    FormTitle.LabeledEditDataBase.Enabled := true;
    FormTitle.LabeledEditUserName.Enabled := true;

    ToolButtonLogs.Enabled := true;
    ToolButtonControl.Enabled := true;
    ToolButtonRecords.Enabled := true;
    ToolButtonStatistics.Enabled := true;
  end;

  { press button }
  ToolButtonTitle.Down := true;
  { show title }
  FormTitle.BringToFront;
end;

procedure TFormMain.MakeLog(log_type : Word; log_message : WideString);
begin
  with ADOCommandInsertLog do
  begin
    Parameters.ParamByName('Message').Value := log_message;
    Parameters.ParamByName('Type').Value    := log_type;
    try Execute except end;
  end;
end;

function TFormMain.UserName() : string;
const
  cnMaxUserNameLen = 254;
var
  sUserName     : string;
  dwUserNameLen : DWord;
begin
  { http://stackoverflow.com/questions/15555527/how-to-get-the-name-of-the-current-user-in-delphi }
  dwUserNameLen := cnMaxUserNameLen-1;
  SetLength( sUserName, cnMaxUserNameLen );
  Win32Check(GetUserName( PChar(sUserName), dwUserNameLen ));

  result := PChar( sUserName );
end;

end.
