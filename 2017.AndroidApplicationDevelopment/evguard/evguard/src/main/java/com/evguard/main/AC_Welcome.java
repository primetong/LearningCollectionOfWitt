package com.evguard.main;

import java.sql.Timestamp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import com.evguard.model.ICommonWebResponse;
import com.evguard.model.WebReq_Login;
import com.evguard.model.WebRes_Login;
import com.evguard.tools.CommUtils;
import com.xinghaicom.evguard.R;
import com.xinghaicom.security.MD5;

public class AC_Welcome extends AC_Base {
	private ImageView mWelcome;
	protected Handler_UncaughtException mUncaughtExceptionHandler = null;
	
	private static final int MESSAGE_UI_WAIT = 0;
	private static final int MESSAGE_UI_END = 1;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		mUncaughtExceptionHandler = new Handler_UncaughtException(this);
		Thread.setDefaultUncaughtExceptionHandler(mUncaughtExceptionHandler);
		super.onCreate(savedInstanceState);
		CheckIsFirstUse();
		setContentView(R.layout.ac_welcome);
		findViews();
		processIntentData();

	}
	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		processIntentData();
	}
	private void processIntentData(){
		if(this.getIntent()!=null
				&&(this.getIntent().getAction().equals("com.evguard.activity.exit")
				||((this.getIntent().getFlags()&Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0))){
			AC_Welcome.this.finish();
			return;
		}else{
			showWelcome();
		}
	}
	private void CheckIsFirstUse() {
		if(mSettings.getIsFirstUse()){
			Intent selectcarTypeintent=new Intent("com.evguard.activity.selectcartype");
			startActivity(selectcarTypeintent);
		}
		mSettings.setIsFirstUse(false);
	}
	private void findViews(){
		mWelcome = (ImageView) findViewById(R.id.img_welcome);
	}
	
	private void showWelcome(){
		AlphaAnimation aAlphaAnimation=new AlphaAnimation(1, 1);
		aAlphaAnimation.setDuration(1500);
		aAlphaAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation animation) {
				System.out.println("mSettings.getIsUserQiutApp()-- " + mSettings.getIsUserQiutApp());
				if(mSettings.getIsUserQiutApp()){
					showWelcomeDismiss();
					
					Intent tLogin=new Intent("com.evguard.activity.login");
					startActivity(tLogin);
				}else{
					login();
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationStart(Animation animation) {
			}

		});
		mWelcome.startAnimation(aAlphaAnimation);
	}
	private void showWelcomeDismiss(){
		AlphaAnimation aAlphaAnimation=new AlphaAnimation(1, 0);
		aAlphaAnimation.setDuration(700);
		mWelcome.startAnimation(aAlphaAnimation);
	}
	

	/**
	 * 登录服务
	 */
	private void login() {
		// 身份验证请求
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String password = null;
		String username = mSettings.getUserName();
		try 
		{
			MD5 md5 = new MD5();
			password = md5.hashToDigestDes(mSettings.getLoginPwd());
		} catch (Exception e) {
			Log.e("ConstantTool", "MD5加密错误" + e.getMessage());
		}
		String password_encode = username + password + timestamp.toString();
		WebReq_Login aWebReq_CheckAuthorityAll = new WebReq_Login(username,password_encode,timestamp.toString());
		// web请求结果回调
		ICommonWebResponse<WebRes_Login> aICommonWebResponse = new ICommonWebResponse<WebRes_Login>() {
			@Override
			public void WebRequstSucess(WebRes_Login aWebRes) {
				if (aWebRes.getResult().equals("0")) {
					// 关闭等待界面
					Message endMsg = mHandler.obtainMessage(MESSAGE_UI_END);
					saveLoginInfo(aWebRes);
					endMsg.obj = aWebRes;
					mHandler.sendMessage(endMsg);
				} else{
					Message endMsg = mHandler.obtainMessage(MESSAGE_UI_END);
					endMsg.obj = aWebRes.getMessage();
					mHandler.sendMessage(endMsg);
				}
			}
			@Override
			public void WebRequestException(String ex) {
				Message endMsg = mHandler.obtainMessage(MESSAGE_UI_END);
				endMsg.obj = "ex:登陆失败，请确认服务器地址设置是否正确，网络连接是否正常.";
				mHandler.sendMessage(endMsg);
			}

			@Override
			public void WebRequsetFail(String sfalied) {
				Message endMsg = mHandler.obtainMessage(MESSAGE_UI_END);
				endMsg.obj = "falied:登录失败！" + sfalied;
				mHandler.sendMessage(endMsg);
			}
		};
		WebRequestThreadEx<WebRes_Login> aWebRequestThreadEx = new WebRequestThreadEx<WebRes_Login>(
				aWebReq_CheckAuthorityAll, aICommonWebResponse,
				new WebRes_Login());
		new Thread(aWebRequestThreadEx).start();

	}
	@Override
	public void handleMsg(Message msg) {
		switch(msg.what){
		case MESSAGE_UI_END:
			if(msg.obj!=null && msg.obj instanceof String){
				showWelcomeDismiss();
				Intent tLogin=new Intent("com.evguard.activity.login");
				startActivity(tLogin);
			}else if(msg.obj!=null && msg.obj instanceof WebRes_Login){
				showWelcomeDismiss();
				WebRes_Login aWebRes = (WebRes_Login)msg.obj;
				Bundle bundle = new Bundle();
				bundle.putString("CarNum", aWebRes.getCarNum());
				bundle.putString("AboutUrl", aWebRes.getAboutUrl());
				bundle.putString("CarIllegalUrl", aWebRes.getCarIllegalUrl());
				bundle.putString("HeartInterval", aWebRes.getHeartInterval());
//				Intent tmain = new Intent(AC_Welcome.this,AC_About.class);
				Intent tmain=new Intent("com.evguard.activity.main");
				startActivity(tmain);
			}
			break;
		
		}
	}
	
	protected void saveLoginInfo(WebRes_Login aWebRes) {
		mSettings.setVIN(aWebRes.getVIN());
		mSettings.setSecretKey(aWebRes.getSecretKey());
		mSettings.setAboutUrl(aWebRes.getAboutUrl());
		mSettings.setCarIllegalUrl(aWebRes.getCarIllegalUrl());
	}
}
