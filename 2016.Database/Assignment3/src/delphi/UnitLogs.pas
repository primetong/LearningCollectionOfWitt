unit UnitLogs;

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms, 
  Dialogs, DBCGrids, DB, ADODB, Grids, DBGrids, ExtCtrls, StdCtrls, ComCtrls, UnitMain;

type
  TFrameLogs = class(TFrame)
    DataSourceLogs: TDataSource;
    DBGridLogs: TDBGrid;
    GridPanelLeft: TGridPanel;
    GridPanelFilter: TGridPanel;
    LabelLogTimeBegin: TLabel;
    LabelLogTimeEnd: TLabel;
    LabelLogType: TLabel;
    EditLogType: TEdit;
    ButtonClear: TButton;
    ButtonDelete: TButton;
    LabelCondition: TLabel;
    DateTimePickerBegin: TDateTimePicker;
    DateTimePickerEnd: TDateTimePicker;
    ADOCommandDelete: TADOCommand;
    ADOQueryLogs: TADOQuery;
    procedure OnDateTimePickerChange(Sender: TObject);
    procedure OnEditLogFliterChange(Sender: TObject);
    procedure ButtonDeleteClick(Sender: TObject);
    procedure ButtonClearClick(Sender: TObject);
    procedure DBGridLogsKeyDown(Sender: TObject; var Key: Word;
      Shift: TShiftState);
  private
    { Private declarations }
  public
    { Public declarations }
    procedure UpdateDataGrid();
  end;

implementation

{$R *.dfm}

procedure TFrameLogs.UpdateDataGrid();
begin
  with ADOQueryLogs do
  begin
    Close;
    Parameters.ParamByName('DateBegin').Value := DateTimePickerBegin.Date;
    Parameters.ParamByName('DateEnd').Value   := DateTimePickerEnd.Date;
    Open;
  end;
end;

procedure TFrameLogs.ButtonClearClick(Sender: TObject);
begin
  DateTimePickerBegin.Date := StrToDateTime('1970/01/01');
  DateTimePickerEnd.Date   := StrToDateTime('2070/01/01');
  EditLogType.Text         := '';
  UpdateDataGrid();
end;

procedure TFrameLogs.ButtonDeleteClick(Sender: TObject);
begin
  with ADOCommandDelete do
  begin
    CommandText
      := 'DELETE'
      + ' FROM LogTable'
      + ' WHERE :DateBegin < log_datetime AND log_datetime < :DateEnd';

    if EditLogType.Text <> '' then
      CommandText := CommandText + ' AND log_level = ' + EditLogType.Text;

    Parameters.ParamByName('DateBegin').Value := DateTimePickerBegin.Date;
    Parameters.ParamByName('DateEnd').Value   := DateTimePickerEnd.Date;

    try
      Execute
    except
      ShowMessage('Error: Failed to Delete Current Logs!');
    end;

    FormMain.MakeLog(1, 'User ''' + FormMain.UserName() + ''' has DELETE some logs.');
  end;
  UpdateDataGrid();
end;

procedure TFrameLogs.DBGridLogsKeyDown(Sender: TObject; var Key: Word;
  Shift: TShiftState);
begin
  { http://stackoverflow.com/questions/2392149/capturing-a-delete-key-press }
  if Key = VK_DELETE then
    if not ADOQueryLogs.IsEmpty then
      try ADOQueryLogs.Delete except end;
      { http://www.delphigroups.info/2/e7/235630.html }
end;

procedure TFrameLogs.OnDateTimePickerChange(Sender: TObject);
begin
  UpdateDataGrid();
end;


procedure TFrameLogs.OnEditLogFliterChange(Sender: TObject);
var
  cmd : WideString;
begin
  if EditLogType.Text <> '' then
    cmd := 'Type = ' + EditLogType.Text
  else
    cmd := '';

  with ADOQueryLogs do
  begin
    Filtered := False;
    Filter   := cmd;
    Filtered := True;
  end;
end;

end.
