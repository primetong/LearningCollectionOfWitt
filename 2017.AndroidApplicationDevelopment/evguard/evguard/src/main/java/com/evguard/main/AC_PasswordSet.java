package com.evguard.main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.evguard.customview.AppTitleBar;
import com.evguard.customview.AppTitleBar.OnTitleActionClickListener;
import com.evguard.customview.KeyboardUtil;
import com.evguard.model.ICommonWebResponse;
import com.evguard.model.WebReq_SetPwd2;
import com.evguard.model.WebRes_GetVerifyCode;
import com.evguard.model.WebRes_SetPwd2;
import com.evguard.tools.ConstantTool;
import com.xinghaicom.evguard.R;

public class AC_PasswordSet extends AC_Base {

	private AppTitleBar mTitleBar = null;
	private EditText mETPassword = null;
	private EditText mETPassword1 = null;
	private Button mIVGetSMS = null;
	private Button mBTNModify = null;
	private Toast mToast = null;
	private Context mContext = null;
	private String mCAPTCHA = null;
	private String mSMSCode = null;
	private String mUsername = null;
	private String mVerifyCode = null;
	private String mNewPwd = null;
	private Dg_Waiting mWaitDialog = null;
	private Dg_Waiting waitingDialog;

	private String phonenumregular = "^\\d{11}$";
	private String pwdregular = "^[^\u4e00-\u9fa5]{1,}$";

	private final int GET_VERIFY_SMS_WAITING = 111;
	private final int GET_VERIFY_SMS_OK = 115;
	private final int REGITER_USER_COMPLETED = 110;
	private static final int MESSAGE_UI_WAIT = 0;
	private static final int MESSAGE_UI_END = 1;
	protected static final int PASSWORDSET_LOGIN = 22;
	private KeyboardUtil mKeyboardUtilOld = null;
	private KeyboardUtil mKeyboardUtilNew = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_password_set);
		mContext = this;
		initViews();

//		mTitleBar.setTitleMode(AppTitleBar.APPTITLEBARMODE_TXTANDBACK, "设置密码",
//				null);
		mTitleBar.setTitleMode(null,
				mContext.getResources().getDrawable(R.drawable.icon_back),
				"设置密码",false,
				null,null);
		mTitleBar
				.setOnTitleActionClickListener(new OnTitleActionClickListener() {

					@Override
					public void onLeftOperateClick() {
						AC_PasswordSet.this.finish();
					}

					@Override
					public void onRightOperateClick() {

					}

					@Override
					public void onTitleClick() {
						// TODO Auto-generated method stub
						
					}

				});
		mUsername = getIntent().getStringExtra("UserName");
		mVerifyCode = getIntent().getStringExtra("VerifyCode");
		eventListner();
	}

	@Override
	public void handleMsg(Message msg) {
		Log.i("llj", "msg.what:" + msg.what);
		switch (msg.what) {
		case MESSAGE_UI_WAIT:
			if (mWaitDialog == null) {
				mWaitDialog = Dg_Waiting.newInstance("信息提示", "正在修改中,请稍候");
			}
			mWaitDialog.setTitle("信息提示");
			mWaitDialog.setMessage("正在修改中,请稍候");
			mWaitDialog.setCancelable(false);
			mWaitDialog.show(getSupportFragmentManager(), "");
			break;
		case MESSAGE_UI_END:

			if (mWaitDialog != null) {
				mWaitDialog.dismiss();
				mWaitDialog = null;
			}
			if (msg.obj != null) {
				String showText = msg.obj.toString();
				showToast(showText);
			}
			break;

		}
	
		
	}
	

	private void eventListner() {
		
		// 确认修改密码
		mBTNModify.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				String mPwd = mETPassword.getText().toString();
				if (mPwd == null || mPwd.trim().length() <= 0) {
					showToast("请输入新密码");
					return;
				}
				if (mPwd.length() < 6) {
					showToast("密码强度太弱，请输入6~32位密码");
					return;
				}
				Pattern pattern1 = Pattern.compile(pwdregular);
				Matcher m2 = pattern1.matcher(mPwd);
				if (!m2.matches()) {
					showToast("新密码输入有误，密码不能包含有中文");
					return;
				}
				if (mPwd.contains("-- ") || mPwd.contains("% ")
						|| mPwd.contains(" ")) {
					showToast("新密码输入有误，密码不能包含-- 或者%  或者空格字符，请输入有效的新密码");
					return;
				}
				boolean valid = false;
				PwdCharType curCharType = PwdCharType.UNKNOWN;
				int pwdCharCount = mPwd.length();
				for (int i = 0; i < pwdCharCount; i++) {
					Character pwdChar = mPwd.charAt(i);
					if (pwdChar == null)
						continue;
					switch (pwdChar) {
					case '"':
					case '\\':
					case '\'':
					case ':':
						showToast("新密码输入有误，密码不能包含 \"\\:\' 字符，请输入有效的新密码");
						return;
					}
					switch (curCharType) {
					case UNKNOWN:
						if (Character.isLetter(pwdChar)) {
							curCharType = PwdCharType.LETTER;
						} else if (Character.isDigit(pwdChar)) {
							curCharType = PwdCharType.NUMBER;
						} else {
							curCharType = PwdCharType.OTHER;
						}
						break;
					case LETTER:
						if (Character.isLetter(pwdChar)) {
						} else if (Character.isDigit(pwdChar)) {
							curCharType = PwdCharType.NUMBER;
							valid = true;
						} else {
							curCharType = PwdCharType.OTHER;
							valid = true;
						}
						break;
					case NUMBER:
						if (Character.isLetter(pwdChar)) {
							curCharType = PwdCharType.LETTER;
							valid = true;
						} else if (Character.isDigit(pwdChar)) {
						} else {
							curCharType = PwdCharType.OTHER;
							valid = true;
						}
						break;
					case OTHER:
						if (Character.isLetter(pwdChar)) {
							curCharType = PwdCharType.LETTER;
							valid = true;
						} else if (Character.isDigit(pwdChar)) {
							curCharType = PwdCharType.NUMBER;
							valid = true;
						}
						break;
					}

					if (valid)
						break;
				}
				if (!valid) {
					showToast("密码强度太弱，应至少包含字母、数字及其它字符中的两种");
					return;
				}

				String mPwd1 = mETPassword1.getText().toString();
				if (mPwd1 == null
						|| mPwd1.trim().length() <= 0) {
					showToast("请再次输入新密码");
					return;
				}

				if (!mPwd1.equals(mPwd)) {
					showToast("两次密码输入不一致，请一致输入您的新密码");
					return;
				}
				mNewPwd = mPwd;
				resetPassword();
				
			}
		});
	}

	private void resetPassword() {
		
		Message waitMsg = mHandler.obtainMessage(MESSAGE_UI_WAIT);
		mHandler.sendMessage(waitMsg);
		
		WebReq_SetPwd2 aWebReq_SetPwd2 = new WebReq_SetPwd2(mUsername,mVerifyCode,mNewPwd);
		ICommonWebResponse<WebRes_SetPwd2> aICommonWebResponse = new ICommonWebResponse<WebRes_SetPwd2>() {

			@Override
			public void WebRequestException(String ex) {
				Message endMsg = mHandler.obtainMessage(MESSAGE_UI_END);
				endMsg.obj = "ex:设置密码失败," +ex;
				mHandler.sendMessage(endMsg);
			}

			@Override
			public void WebRequsetFail(String sfalied) {
				Message endMsg = mHandler.obtainMessage(MESSAGE_UI_END);
				endMsg.obj = "falied:设置密码失败！" + sfalied;
				mHandler.sendMessage(endMsg);
			}

			@Override
			public void WebRequstSucess(WebRes_SetPwd2 aWebRes) {
				if (aWebRes.getResult().equals("0")) {
					// 关闭等待界面
					Message endMsg = mHandler.obtainMessage(MESSAGE_UI_END);
					endMsg.obj = aWebRes.getMessage();
					mHandler.sendMessage(endMsg);
					Intent intent = new Intent(AC_PasswordSet.this,AC_Login.class);
					Bundle bundle = new Bundle();
					bundle.putInt(ConstantTool.INTENT_EXTRANAME_DATA1, PASSWORDSET_LOGIN);
					bundle.putString("username",mUsername);
					bundle.putString("password", mNewPwd);
					intent.putExtras(bundle);
//					startActivityForResult(intent, PASSWORDSET_LOGIN);
					startActivity(intent);
				} else {
					Message endMsg = mHandler.obtainMessage(MESSAGE_UI_END);
					endMsg.obj = aWebRes.getMessage();
					mHandler.sendMessage(endMsg);
				}
			}
			
		};
		WebRequestThreadEx<WebRes_SetPwd2> aWebRequestThreadEx = new WebRequestThreadEx<WebRes_SetPwd2>(
				aWebReq_SetPwd2, aICommonWebResponse,
				new WebRes_SetPwd2());
		new Thread(aWebRequestThreadEx).start();

	}


	private void initViews() {
		mTitleBar = (AppTitleBar) findViewById(R.id.title_bar);
		mETPassword = (EditText) findViewById(R.id.et_password);
		mETPassword1 = (EditText) findViewById(R.id.qrmm);
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
