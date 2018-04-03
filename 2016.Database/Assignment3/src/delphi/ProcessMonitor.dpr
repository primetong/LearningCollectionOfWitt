program ProcessMonitor;

uses
  Forms,
  UnitMain in 'UnitMain.pas' {FormMain},
  UnitTitle in 'UnitTitle.pas' {FrameTitle: TFrame},
  UnitStatistics in 'UnitStatistics.pas' {FrameStatistics: TFrame},
  UnitRecords in 'UnitRecords.pas' {FrameRecords: TFrame},
  UnitLogs in 'UnitLogs.pas' {FrameLogs: TFrame},
  UnitControl in 'UnitControl.pas' {FrameControl: TFrame};

{$R *.res}

begin
  Application.Initialize;
  Application.MainFormOnTaskbar := True;
  Application.Title := 'Process Monitor';
  Application.CreateForm(TFormMain, FormMain);
  Application.Run;
end.
