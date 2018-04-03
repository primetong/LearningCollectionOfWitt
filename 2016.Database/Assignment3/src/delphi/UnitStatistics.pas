unit UnitStatistics;

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms, 
  Dialogs, ComCtrls, StdCtrls, Grids, DBGrids, ExtCtrls, DB, ADODB, Provider, Math;

type
  TFrameStatistics = class(TFrame)
    GridPanelLeft: TGridPanel;
    GridPanelFilter: TGridPanel;
    LabelCondition: TLabel;
    LabelStatisticsDateBegin: TLabel;
    DateTimePickerBegin: TDateTimePicker;
    LabelStatisticsDateEnd: TLabel;
    DateTimePickerEnd: TDateTimePicker;
    ADODataSetProcess: TADODataSet;
    DataSourceProcess: TDataSource;
    GridPanelRight: TGridPanel;
    DBGridProcess: TDBGrid;
    DBGridStatistics: TDBGrid;
    SplitterMid: TSplitter;
    ADODataSetStatisticsSingle: TADODataSet;
    DataSourceStatistics: TDataSource;
    LabelStatisticsProcess: TLabel;
    EditStatisticsProcess: TEdit;
    ComboBoxIsRecording: TComboBox;
    LabelIsRecording: TLabel;
    LabelOwner: TLabel;
    EditStatisticsOwner: TEdit;
    ButtonClear: TButton;
    ButtonSwitchIsRecording: TButton;
    procedure SplitterMidCanResize(Sender: TObject; var NewSize: Integer;
      var Accept: Boolean);
    procedure OnDateTimePickerBeginChange(Sender: TObject);
    procedure OnDateTimePickerEndChange(Sender: TObject);
    procedure ComboBoxIsRecordingChange(Sender: TObject);
    procedure EditStatisticFilterChange(Sender: TObject);
    procedure ButtonClearClick(Sender: TObject);
    procedure ButtonSwitchIsRecordingClick(Sender: TObject);
  private
    { Private declarations }
  public
    { Public declarations }
    procedure UpdateDataGrid();
  end;

implementation

{$R *.dfm}

procedure TFrameStatistics.ButtonClearClick(Sender: TObject);
begin
  DateTimePickerBegin.Date    := StrToDateTime('1970/01/01');
  DateTimePickerEnd.Date      := StrToDateTime('2070/01/01');
  EditStatisticsProcess.Text  := '';
  EditStatisticsOwner.Text    := '';
  ComboBoxIsRecording.ItemIndex := 0;
  UpdateDataGrid();
  ComboBoxIsRecordingChange(nil);
end;

procedure TFrameStatistics.ButtonSwitchIsRecordingClick(Sender: TObject);
begin
  with ADODataSetProcess do
  begin
    { http://www.delphigroups.info/2/16/40983.html }
    if not IsEmpty then
    begin
      if not (State in [dsEdit, dsInsert]) then Edit;
      { http://delphi.about.com/library/rtl/blrtlIfThen.htm }
      { https://www.devexpress.com/Support/Center/Question/Details/A302 }
      FieldByName('Recording').Value := IfThen(FieldByName('Recording').Value = 0, 1);
      Post;
      UpdateDataGrid();
    end;
  end;
end;

procedure TFrameStatistics.ComboBoxIsRecordingChange(Sender: TObject);
begin
  with ADODataSetProcess do
  begin
    Close;

    if ComboBoxIsRecording.ItemIndex <> 2 then
      Parameters.ParamByName('SkipIsRecording').Value := 0
    else
      Parameters.ParamByName('SkipIsRecording').Value := 1;

    if ComboBoxIsRecording.ItemIndex = 0 then
      Parameters.ParamByName('IsRecording').Value := true
    else if ComboBoxIsRecording.ItemIndex = 1 then
      Parameters.ParamByName('IsRecording').Value := false;

    Open;
  end;
end;

procedure TFrameStatistics.OnDateTimePickerBeginChange(Sender: TObject);
begin
  UpdateDataGrid();
end;

procedure TFrameStatistics.OnDateTimePickerEndChange(Sender: TObject);
begin
  UpdateDataGrid();
end;

procedure TFrameStatistics.SplitterMidCanResize(Sender: TObject;
  var NewSize: Integer; var Accept: Boolean);
begin
  if NewSize < 320 then
    GridPanelRight.ColumnCollection.Items[0].Value := NewSize;
end;

procedure TFrameStatistics.EditStatisticFilterChange(Sender: TObject);
var
  cmd : WideString;
begin

  if EditStatisticsProcess.Text <> '' then
    cmd := 'Process Like ''' + EditStatisticsProcess.Text + '%'''
  else
    cmd := '';

  if EditStatisticsOwner.Text <> '' then
  begin
    if cmd <> '' then
      cmd := cmd + ' AND ';
    cmd := cmd + 'Owner Like ''' + EditStatisticsOwner.Text + '%''';
  end;

  with ADODataSetProcess do
  begin
    Filtered := False;
    Filter   := cmd;
    Filtered := True;
  end;
end;

procedure TFrameStatistics.UpdateDataGrid();
begin
  with ADODataSetProcess do
  begin
    Close;
    Parameters.ParamByName('DateBegin').Value := DateTimePickerBegin.Date;
    Parameters.ParamByName('DateEnd').Value   := DateTimePickerEnd.Date;
    Open;
  end;
  with ADODataSetStatisticsSingle do
  begin
    Close;
    Parameters.ParamByName('DateBegin').Value := DateTimePickerBegin.Date;
    Parameters.ParamByName('DateEnd').Value   := DateTimePickerEnd.Date;
    Open;
  end;
end;

end.
