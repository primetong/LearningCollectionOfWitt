package com.evguard.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.evguard.data.AppDataCache;
import com.evguard.model.PushMessage;
import com.evguard.tools.ConstantTool;
import com.xinghaicom.evguard.R;

public abstract class AC_BaseLogined extends AC_Base {

	protected Dg_Alert mReLoginDlgAlert = null;
	protected boolean mIsRelogin = false;
	protected boolean bIsRestore = false;
//	protected KidBaseInfo mKidBaseInfo;
	/* 消息推送 */
	private PushMessage pushMessage = null;
	private Dg_Alert mAlert = null;

	protected boolean isLoginTimeOut() {
		long cur = System.currentTimeMillis();
		long span = cur - AppDataCache.getInstance().getLastReqTime();
		if (span > ConstantTool.LOGINTIMEOUT) {
			mIsRelogin = true;
			return true;
		}
		mIsRelogin = false;
		return false;
	}

	protected void ShowRelogin() {
		if (mReLoginDlgAlert == null) {
			mReLoginDlgAlert = Dg_Alert.newInstance("提示", "登录超时，请重新登录", "确定");
		}
		mReLoginDlgAlert.setCancelable(false);
		mReLoginDlgAlert.setTitle(getResources()
				.getString(R.string.tip_relogin));
		mReLoginDlgAlert.setMessage(getResources().getString(
				R.string.tip_reloginmessage));
		mReLoginDlgAlert.setPositiveButton(getResources()
				.getString(R.string.ok), new View.OnClickListener() {
			public void onClick(View v) {
				mReLoginDlgAlert.dismiss();
				Intent tent = new Intent(AC_BaseLogined.this, AC_Login.class);
				startActivity(tent);
				AC_BaseLogined.this.finish();
			}
		});
		mReLoginDlgAlert.show(getSupportFragmentManager(), "");
	}
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		if(savedInstanceState != null)
			bIsRestore = true;
		else 
			bIsRestore = false;
//		registerMessageReceiver();
	}
	@Override
	protected void onPause() {
//		StatService.onPause(this);
		super.onPause();
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
//		StatService.onResume(this);
		super.onResume();
//		if (isLoginTimeOut()) {
//			ShowRelogin();
//		}
	}
	@Override
	protected void onDestroy(){
//		if (mMessageReceiver != null)
//			unRegisterMessageReceiver();
		super.onDestroy();
	}
	// =============================================
	// =================接收推送消息开始=================
	// =============================================
	// for receive customer msg from jpush server
	/*
	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.xinghaicom.smartbracelet.receivepush";
	public static final String KEY_DATA = "message";

	public void registerMessageReceiver() {
		if (mMessageReceiver != null)
			unRegisterMessageReceiver();
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}

	public void unRegisterMessageReceiver() {
		unregisterReceiver(mMessageReceiver);
	}

	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
				PushMessage amsg = intent.getParcelableExtra(KEY_DATA);
				processAlterPushMessage(amsg);
			}
		}
	}
	
	private void processAlterPushMessage(PushMessage amsg) {
		mSettings.setIsHasNewAlarm(true);
		if (mAlert == null) {
			mAlert = Dg_Alert.newInstance("推送通知", amsg.getMessageContent(),
					"确定");
		}
		mAlert.setCancelable(true);
		mAlert.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(View v) {
				mAlert.dismiss();
			}
		});
		mAlert.setMessage(amsg.getMessageContent());
		mAlert.show(getSupportFragmentManager(), "");
	}
	*/
	// =============================================
	// =================接收推送消息结束=================
	// =============================================


}
