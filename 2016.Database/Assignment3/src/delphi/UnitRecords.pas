unit UnitRecords;

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms, 
  Dialogs, StdCtrls, Grids, DBGrids, ExtCtrls, DB, ADODB, ComCtrls, UnitMain;

type
  TFrameRecords = class(TFrame)
    GridPanelRight: TGridPanel;
    DBGridRecords: TDBGrid;
    GridPanelLeft: TGridPanel;
    ButtonClear: TButton;
    ButtonDelete: TButton;
    LabelCondition: TLabel;
    DataSourceRecords: TDataSource;
    ADODataSetRecords: TADODataSet;
    GridPanelFilter: TGridPanel;
    LabelRecordTimeBegin: TLabel;
    LabelRecordTimeEnd: TLabel;
    LabelRecordType: TLabel;
    EditRecordName: TEdit;
    DateTimePickerBegin: TDateTimePicker;
    DateTimePickerEnd: TDateTimePicker;
    LabelOwner: TLabel;
    EditRecordOwner: TEdit;
    ADOCommandDelete: TADOCommand;
    procedure DateTimePickerChange(Sender: TObject);
    procedure ButtonDeleteClick(Sender: TObject);
    procedure EditRecordFilterChange(Sender: TObject);
    procedure ButtonClearClick(Sender: TObject);
    procedure DBGridLogsKeyDown(Sender: TObject; var Key: Word; Shift: TShiftState);
  private
    { Private declarations }
  public
    { Public declarations }
    procedure UpdateDataGrid();
  end;

implementation

{$R *.dfm}

procedure TFrameRecords.ButtonClearClick(Sender: TObject);
begin
  DateTimePickerBegin.Date := StrToDateTime('1970/01/01');
  DateTimePickerEnd.Date   := StrToDateTime('2070/01/01');
  EditRecordName.Text      := '';
  EditRecordOwner.Text     := '';
  UpdateDataGrid();
end;

procedure TFrameRecords.ButtonDeleteClick(Sender: TObject);
var
  log : WideString;
begin
  with ADOCommandDelete do
  begin
    CommandText
      := 'DELETE'
      + ' FROM RecordTable'
      + ' WHERE :DateBegin < record_date AND record_date < :DateEnd';

    if EditRecordName.Text <> '' then
      CommandText := CommandText + ' AND name Like ''' + EditRecordName.Text + '%''';
    if EditRecordOwner.Text <> '' then
      CommandText := CommandText + ' AND creator Like ''' + EditRecordOwner.Text + '%''';

    Parameters.ParamByName('DateBegin').Value := DateTimePickerBegin.Date;
    Parameters.ParamByName('DateEnd').Value   := DateTimePickerEnd.Date;

    log
      := 'User '''
      + FormMain.UserName()
      + ''' try to DELETE some records.';

    try
      Execute;
    except
      log := log + 'But failed.';
      showmessage('Error: Failed to Delete Current Records!');
    end;

    FormMain.MakeLog(0, log);
  end;
  UpdateDataGrid();
end;

procedure TFrameRecords.DBGridLogsKeyDown(Sender: TObject; var Key: Word;
  Shift: TShiftState);
begin
  if Key = VK_DELETE then
    if not ADODataSetRecords.IsEmpty then
      ADODataSetRecords.Delete;
end;

procedure TFrameRecords.DateTimePickerChange(Sender: TObject);
begin
  UpdateDataGrid();
end;

procedure TFrameRecords.EditRecordFilterChange(Sender: TObject);
var
  cmd : WideString;
begin

  if EditRecordName.Text <> '' then
    cmd := 'Process Like ''' + EditRecordName.Text + '%'''
  else
    cmd := '';

  if EditRecordOwner.Text <> '' then
  begin
    if cmd <> '' then
      cmd := cmd + ' AND ';
    cmd := cmd + 'Owner Like ''' + EditRecordOwner.Text + '%''';
  end;

  with ADODataSetRecords do
  begin
    Filtered := False;
    Filter   := cmd;
    Filtered := True;
  end;
end;

procedure TFrameRecords.UpdateDataGrid();
begin
  with ADODataSetRecords do
  begin
    Close;
    Parameters.ParamByName('DateBegin').Value := DateTimePickerBegin.Date;
    Parameters.ParamByName('DateEnd').Value   := DateTimePickerEnd.Date;
    Open;
  end;
end;

end.
