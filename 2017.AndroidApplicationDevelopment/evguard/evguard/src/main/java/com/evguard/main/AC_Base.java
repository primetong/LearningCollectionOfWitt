package com.evguard.main;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.evguard.tools.CommUtils;
import com.evguard.tools.LogEx;

public abstract class AC_Base extends FragmentActivity {

	protected String TAG=this.getClass().getName();
	protected Context mContext = null;
	protected Toast mToast = null;
	protected Handler mHandler = null;
	protected App_Settings mSettings = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_NoTitleBar);
		mContext = this;
		mSettings = new App_Settings(this);
		CommUtils.getScreenMetrics(this);
		initHandler();
		LogEx.i(TAG, this.getClass().getSimpleName()+" onCreate");
	}

	@Override
	protected void onResume() {
		super.onResume();
//		StatService.onResume(this);
		LogEx.i(TAG, this.getClass().getSimpleName()+" onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
//		StatService.onPause(this);
		LogEx.i(TAG, this.getClass().getSimpleName()+" onPause");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mToast != null)
			mToast.cancel();
		LogEx.i(TAG, this.getClass().getSimpleName()+" onDestroy");
	}

	protected void showToast(String txt) {
		if (mToast == null)
			mToast = Toast.makeText(mContext, txt, Toast.LENGTH_LONG);
		mToast.setText(txt);
		mToast.show();
	}
	private  void initHandler() {
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg){
				super.handleMessage(msg);
				handleMsg(msg);
				
			}
		};
	}
	protected abstract void handleMsg(Message msg);
}
