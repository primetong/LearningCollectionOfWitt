package com.evguard.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.evguard.customview.AppTitleBar;
import com.evguard.customview.AppTitleBar.OnTitleActionClickListener;
import com.evguard.customview.KeyboardUtil;
import com.evguard.model.ICommonWebResponse;
import com.evguard.model.WebReq_GetVerifyCode;
import com.evguard.model.WebRes_GetVerifyCode;
import com.evguard.tools.CountTimerTool;
import com.evguard.tools.LogEx;
import com.xinghaicom.asynchrony.LoopHandler;
import com.xinghaicom.evguard.R;

public class AC_PasswordForget extends AC_Base {

	private AppTitleBar mTitleBar = null;
	private EditText mETPhoneNum = null;
	private EditText mETSMS = null;
	private Button mIVGetSMS = null;
	private Button mBTNModify = null;
	private Toast mToast = null;
	private Context mContext = null;
	private String mCAPTCHA = null;
	private String mSMSCode = null;
	private String mTel = null;
	private String mNewPWD = null;
	private Thread_GetPassword mPWDGetting = null;
	private Dg_Waiting mWaitDialog = null;
	private Dg_Waiting waitingDialog;

	private String phonenumregular = "^\\d{11}$";
	private String pwdregular = "^[^\u4e00-\u9fa5]{1,}$";

	private final int GET_VERIFY_SMS_WAITING = 111;
	private final int GET_VERIFY_SMS_OK = 115;
	private final int REGITER_USER_COMPLETED = 110;
	private static final int MESSAGE_UI_WAIT = 0;
	private static final int MESSAGE_UI_END = 1;
	private KeyboardUtil mKeyboardUtilOld = null;
	private KeyboardUtil mKeyboardUtilNew = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_get_password);
		mContext = this;
		initViews();

//		mTitleBar.setTitleMode(AppTitleBar.APPTITLEBARMODE_TXTANDBACK, "忘记密码",
//				null);
		mTitleBar.setTitleMode(null,
				mContext.getResources().getDrawable(R.drawable.icon_back),
				"忘记密码",false,
				null,null);
		mTitleBar
				.setOnTitleActionClickListener(new OnTitleActionClickListener() {

					@Override
					public void onLeftOperateClick() {
						AC_PasswordForget.this.finish();
					}

					@Override
					public void onRightOperateClick() {

					}

					@Override
					public void onTitleClick() {
						// TODO Auto-generated method stub

					}

				});
		mTel = getIntent().getStringExtra("TEL");
		initialGetting();
		initial(mTel);
		eventListner();
	}

	@Override
	public void handleMsg(Message msg) {
		switch (msg.what) {
		case MESSAGE_UI_WAIT:
			if (waitingDialog != null) {
				waitingDialog.dismiss();
			}
			if (waitingDialog == null) {
				waitingDialog = Dg_Waiting.newInstance("获取验证码", "正在获取...");
			}
			waitingDialog.show(getSupportFragmentManager(), "");
			break;
		case MESSAGE_UI_END:

			if (waitingDialog != null) {
				waitingDialog.dismiss();
				waitingDialog = null;
			}
			if (msg.obj != null) {
				String showText = msg.obj.toString();
				showToast(showText);
			}
			break;

		}

	}

	private void initial(String tel) {
		if (mETPhoneNum != null && mTel != null) {
			mETPhoneNum.setText(mTel);
		}
		if (mETPhoneNum != null && (mTel == null || mTel.length() <= 0)) {
			mETPhoneNum.requestFocus();
		}
	}

	private void initialGetting() {
		mPWDGetting = new Thread_GetPassword(mContext, new LoopHandler() {
			@Override
			protected void onException(Exception e) {
				super.onException(e);
				LogEx.e("PasswordResettingDialog", "与服务端连接失败" + e.getMessage());
			}

			@Override
			protected void onLooped() {
				super.onLooped();
			}
		});
		mPWDGetting.start();
	}

	private void eventListner() {

		// 获取短信验证码
		mIVGetSMS.setOnClickListener(new OnClickListener() {
			private CountTimerTool countTimer;

			public void onClick(View v) {
				mTel = mETPhoneNum.getText().toString();
				if (mTel == null || mTel.length() <= 0) {
					showToast("请输入手机号");
					return;
				}
				mHandler.sendEmptyMessage(GET_VERIFY_SMS_WAITING);
				countTimer = new CountTimerTool(mIVGetSMS, "获取验证码");
				countTimer.start();
				getVerifyingSMS();
			}
		});
		// 确认修改密码
		mBTNModify.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mSMSCode = mETSMS.getText().toString();
				if (mSMSCode == null || mSMSCode.length() <= 0) {
					showToast("请输入验证码！");
					return;
				}
				getPassword();

			}

		});
	}

	private void getPassword() {
		Intent intent = new Intent(AC_PasswordForget.this, AC_PasswordSet.class);
		intent.putExtra("VerifyCode", mSMSCode);
		intent.putExtra("UserName", mTel);
		startActivity(intent);
	}

	private void getVerifyingSMS() {

		Message waitMsg = mHandler.obtainMessage(MESSAGE_UI_WAIT);
		mHandler.sendMessage(waitMsg);

		WebReq_GetVerifyCode aWebReq_GetVerifyCode = new WebReq_GetVerifyCode();
		aWebReq_GetVerifyCode.setUsername(mTel);
		ICommonWebResponse<WebRes_GetVerifyCode> aICommonWebResponse = new ICommonWebResponse<WebRes_GetVerifyCode>() {

			@Override
			public void WebRequestException(String ex) {
				Message endMsg = mHandler.obtainMessage(MESSAGE_UI_END);
				endMsg.obj = "ex:验证码获取失败," + ex;
				mHandler.sendMessage(endMsg);
			}

			@Override
			public void WebRequsetFail(String sfalied) {
				Message endMsg = mHandler.obtainMessage(MESSAGE_UI_END);
				endMsg.obj = "falied:验证码获取失败！" + sfalied;
				mHandler.sendMessage(endMsg);
			}

			@Override
			public void WebRequstSucess(WebRes_GetVerifyCode aWebRes) {
				if (aWebRes.getResult().equals("0")) {
					// 关闭等待界面
					Message endMsg = mHandler.obtainMessage(MESSAGE_UI_END);
					mHandler.sendMessage(endMsg);
				} else {
					Message endMsg = mHandler.obtainMessage(MESSAGE_UI_END);
					endMsg.obj = aWebRes.getMessage();
					mHandler.sendMessage(endMsg);
				}
			}

		};
		WebRequestThreadEx<WebRes_GetVerifyCode> aWebRequestThreadEx = new WebRequestThreadEx<WebRes_GetVerifyCode>(
				aWebReq_GetVerifyCode, aICommonWebResponse,
				new WebRes_GetVerifyCode());
		new Thread(aWebRequestThreadEx).start();

	}

	private void initViews() {
		mTitleBar = (AppTitleBar) findViewById(R.id.title_bar);
		mETPhoneNum = (EditText) findViewById(R.id.et_username);
		mETSMS = (EditText) findViewById(R.id.dxyzm);
		mIVGetSMS = (Button) findViewById(R.id.hqdxyzm);
		mBTNModify = (Button) findViewById(R.id.qrxg);
	}

	@Override
	public void finish() {
		if (mToast != null) {
			mToast.cancel();
		}
		super.finish();
	}

	public enum PwdCharType {
		UNKNOWN, LETTER, NUMBER, OTHER
	}

}
