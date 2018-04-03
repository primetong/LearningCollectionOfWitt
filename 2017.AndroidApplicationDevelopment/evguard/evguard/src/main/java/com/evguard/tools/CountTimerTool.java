package com.evguard.tools;

import android.os.CountDownTimer;
import android.widget.Button;

public class CountTimerTool extends CountDownTimer {
	
	public static final int TIME_COUNT = 61000;
	private Button btn;
	private String endStrRid;
	public CountTimerTool(long millisInFuture, long countDownInterval,Button btn,String endStrRid) {
		super(millisInFuture, countDownInterval);
		this.btn = btn;
		this.endStrRid = endStrRid;
	}
	
	public CountTimerTool(Button btn,String endStrRid) {
		super(TIME_COUNT, 1000);
		this.btn = btn;
		this.endStrRid = endStrRid;
	}

	@Override
	public void onFinish() {
		btn.setClickable(true);
		btn.setText(endStrRid);
	}

	@Override
	public void onTick(long millisUntilFinished) {
		btn.setClickable(false);
		btn.setText(millisUntilFinished / 1000 + "秒后获取");
	}

}
