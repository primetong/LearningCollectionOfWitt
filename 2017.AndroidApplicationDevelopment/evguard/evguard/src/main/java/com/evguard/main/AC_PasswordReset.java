package com.evguard.main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.evguard.customview.AppTitleBar;
import com.evguard.customview.AppTitleBar.OnTitleActionClickListener;
import com.evguard.customview.KeyboardUtil;
import com.evguard.model.ICommonWebResponse;
import com.evguard.model.WebReq_SetPwd;
import com.evguard.model.WebRes_SetPwd;
import com.evguard.tools.LogEx;
import com.xinghaicom.asynchrony.LoopHandler;
import com.xinghaicom.evguard.R;
import com.xinghaicom.security.MD5;

public class AC_PasswordReset extends AC_Base {

	private AppTitleBar mTitleBar = null;
	private EditText mETOrignalPwd = null;
	private EditText mETCaptCha = null;
	private ProgressBar mPBGettingCaptCha = null;
	private ImageView mIVCaptCha = null;
	private TextView mTVCaptChaGotFailed = null;
//	private EditText mETSMS = null;
	private TextView mIVGetSMS = null;
	private EditText mETNewPws = null;
	private EditText mETEnsurePws = null;
	private Button mBTNModify = null;
	private Toast mToast = null;
	private Context mContext = null;
	private String mCAPTCHA = null;
//	private String mSMSCode = null;
	private String mPwd = null;
	private String mTel = null;
	private String mNewPWD = null;
	private Thread_PasswordResetting mPWDReseting = null;
	private Dg_Waiting mWaitDialog = null;
	private Dg_Waiting waitingDialog;

	private String phonenumregular = "^\\d{11}$";
	private String pwdregular = "^[^\u4e00-\u9fa5]{1,}$";

	private final int GET_VERIFY_SMS_WAITING = 111;
	private final int GET_VERIFY_SMS_OK = 115;
	private final int REGITER_USER_COMPLETED = 110;
	private static final int MESSAGE_GETTING = 0;
	private static final int MESSAGE_GETTING_OK = 1;
	private static final int MESSAGE_GETTING_FAILED = 2;

	private KeyboardUtil mKeyboardUtilOld = null;
	private KeyboardUtil mKeyboardUtilNew = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_passwordreset);
		mContext = this;
		initViews();

//		mTitleBar.setTitleMode(AppTitleBar.APPTITLEBARMODE_TXTANDBACK, "密码重置",
//				null);
		mTitleBar.setTitleMode(null,
				mContext.getResources().getDrawable(R.drawable.icon_back),
				"密码重置",false,
				null,null);
		mTitleBar
				.setOnTitleActionClickListener(new OnTitleActionClickListener() {

					@Override
					public void onLeftOperateClick() {
						AC_PasswordReset.this.finish();
					}

					@Override
					public void onRightOperateClick() {

					}

					@Override
					public void onTitleClick() {
						
					}

				});
		mTel = getIntent().getStringExtra("TEL");
		initialResetting();
		eventListner();
	}

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			AC_PasswordReset.this.finish();
//		}
//		return super.onKeyDown(keyCode, event);
//	}
	@Override
	public void handleMsg(Message msg) {
		switch (msg.what) {
		case MESSAGE_GETTING:
			if (mWaitDialog != null) {
				mWaitDialog.dismiss();
			}
			if (mWaitDialog == null) {
				mWaitDialog = Dg_Waiting.newInstance("密码修改", "正在修改中,请稍候");
			}
			mWaitDialog.setTitle("密码修改");
			mWaitDialog.setMessage("正在修改中,请稍候");
			mWaitDialog.setCancelable(false);
			mWaitDialog.show(getSupportFragmentManager(), "");
			break;
		case MESSAGE_GETTING_OK:
			if (mWaitDialog != null) {
				mWaitDialog.dismiss();
				mWaitDialog = null;
			}
			mSettings.setLoginPwd(mNewPWD);
			showToast("密码修改成功！");
			break;
		case MESSAGE_GETTING_FAILED:
			if (mWaitDialog != null){
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
				mPwd = mETOrignalPwd.getText().toString();
				if (mPwd == null || mPwd.length() <= 0) {
					showToast("请输入原始密码");
					return;
				} else {//
					
					Log.i("llj", "原始密码:" +  mSettings.getLoginPwd() + "，输入密码为:" + mPwd);
					if (!mPwd.equals(mSettings.getLoginPwd())) {//
						showToast("原始密码与账号不匹配，请重新输入！");
						return;
					}
				}

				String newPWD = mETNewPws.getText().toString();
				if (newPWD == null || newPWD.trim().length() <= 0) {
					showToast("请输入新密码");
					return;
				}
				if (newPWD.length() < 6) {
					showToast("密码强度太弱，请输入6~32位密码");
					return;
				}
				Pattern pattern1 = Pattern.compile(pwdregular);
				Matcher m2 = pattern1.matcher(newPWD);
				if (!m2.matches()) {
					showToast("新密码输入有误，密码不能包含有中文");
					return;
				}
				if (newPWD.contains("-- ") || newPWD.contains("% ")
						|| newPWD.contains(" ")) {
					showToast("新密码输入有误，密码不能包含-- 或者%  或者空格字符，请输入有效的新密码");
					return;
				}
				boolean valid = false;
				PwdCharType curCharType = PwdCharType.UNKNOWN;
				int pwdCharCount = newPWD.length();
				for (int i = 0; i < pwdCharCount; i++) {
					Character pwdChar = newPWD.charAt(i);
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

				String newEnsuringPWD = mETEnsurePws.getText().toString();
				if (newEnsuringPWD == null
						|| newEnsuringPWD.trim().length() <= 0) {
					showToast("请再次输入新密码");
					return;
				}

				if (!newEnsuringPWD.equals(newPWD)) {
					showToast("两次密码输入不一致，请一致输入您的新密码");
					return;
				}
				mNewPWD = newPWD;
				resetPassword(mNewPWD);
			}
		});
	}

	private void resetPassword(String password) {
		
		
		Message waitMsg = mHandler.obtainMessage(MESSAGE_GETTING);
		mHandler.sendMessage(waitMsg);
		WebReq_SetPwd aWebReq_SetPwd = new WebReq_SetPwd(mSettings.getUserName(),mPwd,password);
//		aWebReq_SetPwd.setUsername(mSettings.getUserName());
//		aWebReq_SetPwd.setPassword(mPwd);
//		aWebReq_SetPwd.setPassword1(mNewPWD);
		
		
		ICommonWebResponse<WebRes_SetPwd> aICommonWebResponse = new ICommonWebResponse<WebRes_SetPwd>() {
			@Override
			public void WebRequstSucess(WebRes_SetPwd aWebRes) {
				if (aWebRes.getResult().equals("0")) {
					Message endMsg = mHandler.obtainMessage(MESSAGE_GETTING_OK);
					mHandler.sendMessage(endMsg);
				}else{
					Message endMsg = mHandler.obtainMessage(MESSAGE_GETTING_FAILED);
					mHandler.sendMessage(endMsg);
				}
			}

			@Override
			public void WebRequestException(String ex) {
				Message endMsg = mHandler.obtainMessage(MESSAGE_GETTING_FAILED);
				mHandler.sendMessage(endMsg);
				
			}

			@Override
			public void WebRequsetFail(String sfalied) {
				Message endMsg = mHandler.obtainMessage(MESSAGE_GETTING_FAILED);
				mHandler.sendMessage(endMsg);
				
			}
		};
		WebRequestThreadEx<WebRes_SetPwd> aWebRequestThreadEx = new WebRequestThreadEx<WebRes_SetPwd>(
				aWebReq_SetPwd, aICommonWebResponse,
				new WebRes_SetPwd());
		new Thread(aWebRequestThreadEx).start();
		
		
//		mPWDReseting.resetPassword(new Handler_PasswordResetting() {
//			@Override
//			protected void onException(Exception e) {
//				super.onException(e);
//				if (mWaitDialog != null)
//					mWaitDialog.dismiss();
//				String eDes = "未知异常";
//				if (e != null) {
//					eDes = e.getMessage();
//					if (eDes == null || eDes.length() <= 0) {
//						eDes = "重设密码失败";
//					}
//				}
//				showToast(eDes != null ? eDes : "未知异常");
//				StatService
//						.onEvent(
//								AC_PasswordReset.this,
//								getString(R.string.forget_password_statistic_event),
//								String.format(
//										getString(R.string.password_resetting_failed_format),
//										mPwd, e != null ? e.getMessage() : "δ֪"));
//			}
//
//			@Override
//			protected void onPasswordReset() {
//				super.onPasswordReset();
//				
//				
//				if (mWaitDialog != null)
//					mWaitDialog.dismiss();
//				showToast("密码重设成功，您的新密码已生效！");
//				StatService
//						.onEvent(
//								AC_PasswordReset.this,
//								getString(R.string.forget_password_statistic_event),
//								String.format(
//										getString(R.string.password_resetting_success_format),
//										mTel));
//				AC_PasswordReset.this.finish();
//			}
//		}, mPwd, mNewPWD);
	}

//	private void getVerifyingSMS() {
//		mPWDReseting.getVerifyingSMS(new Handler_PasswordResetting() {
//			@Override
//			protected void onException(Exception e) {
//				mHandler.sendEmptyMessage(GET_VERIFY_SMS_OK);
//				super.onException(e);
//				mSMSCode = null;
//				String eDes = "未知异常";
//				if (e != null) {
//					eDes = e.getMessage();
//					if (eDes == null || eDes.length() <= 0) {
//						eDes = "获取验证短信失败";
//					}
//				}
//				showToast(eDes != null ? eDes : "未知异常");
//
//				mIVGetSMS.setEnabled(true);
//				mIVGetSMS.setBackgroundResource(R.drawable.bg_btn);
//			}
//
//			@Override
//			protected void onVerifyingSMSGot(String smsCode) {
//				mHandler.sendEmptyMessage(GET_VERIFY_SMS_OK);
//				super.onVerifyingSMSGot(smsCode);
//				LogEx.i("==AAAAAAAAAA===", smsCode);
//				mSMSCode = smsCode;
//				mIVGetSMS.setEnabled(true);
//				mIVGetSMS.setText("获取短信验证");
//				// mIVGetSMS.setBackgroundResource(R.drawable.btn_msm_code);
//				showToast("已发送短信验证码,请注意查收！");
//			}
//		}, mTel);
//	}

	private void initViews() {
		mTitleBar = (AppTitleBar) findViewById(R.id.title_bar);
		mETOrignalPwd = (EditText) findViewById(R.id.ymm);
		mETNewPws = (EditText) findViewById(R.id.xmm);
		mETEnsurePws = (EditText) findViewById(R.id.qrxmm);
		mBTNModify = (Button) findViewById(R.id.qrxg);

	}

	private void initialResetting() {
		mPWDReseting = new Thread_PasswordResetting(mContext,
				new LoopHandler() {
					@Override
					protected void onException(Exception e) {
						super.onException(e);
						LogEx.e("PasswordResettingDialog",
								"与服务端连接失败" + e.getMessage());
					}

					@Override
					protected void onLooped() {
						super.onLooped();
					}
				});
		mPWDReseting.start();
	}

//	private void reGetCAPTCHA() {
//		mCAPTCHA = null;
//		mPBGettingCaptCha.setVisibility(View.VISIBLE);
//		mIVCaptCha.setVisibility(View.GONE);
//		mTVCaptChaGotFailed.setVisibility(View.GONE);
//		getCAPTCHA();
//	}

//	private void getCAPTCHA() {
//		mPWDReseting.getCAPTCHA(new Handler_PasswordResetting() {
//			@Override
//			protected void onException(Exception e) {
//				super.onException(e);
//				mCAPTCHA = null;
//				String eDes = "未知异常";
//				if (e != null) {
//					eDes = e.getMessage();
//				}
//				LogEx.e("PasswordResettingDialog", "获取验证码失败"
//						+ (eDes != null ? eDes : "未知异常"));
//			}
//
//			@Override
//			protected void onCAPTCHAGot(String captcha, Drawable captchaImg) {
//				super.onCAPTCHAGot(captcha, captchaImg);
//				mCAPTCHA = captcha;
//				mIVCaptCha.setVisibility(View.VISIBLE);
//				mIVCaptCha.setImageDrawable(captchaImg);
//				mPBGettingCaptCha.setVisibility(View.GONE);
//				mTVCaptChaGotFailed.setVisibility(View.GONE);
//			}
//		});
//	}

	@Override
	public void finish() {
		if (mPWDReseting != null) {
			mPWDReseting.quit();
		}
		if (mToast != null) {
			mToast.cancel();
		}
		super.finish();
	}

	public enum PwdCharType {
		UNKNOWN, LETTER, NUMBER, OTHER
	}

}
