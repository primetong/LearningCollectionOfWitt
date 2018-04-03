package com.evguard.main;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.evguard.model.WebReq_GetVerifyCode;
import com.evguard.model.WebReq_SendVerifySMS;
import com.evguard.model.WebReq_SetPwd;
import com.evguard.model.WebRes_GetVerifyCode;
import com.evguard.model.WebRes_SendVerifySMS;
import com.evguard.model.WebRes_SetPwd;
import com.xinghaicom.asynchrony.LoopHandler;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.util.Base64;
import android.util.Base64InputStream;

public class Thread_PasswordResetting extends Thread_Base {

	public static final int CAPTCHA_GETTING = 0;
	public static final int VERIFYING_SMS_GETTING = 1;
	public static final int PASSWORD_RESETTING = 2;

	protected Vector<Handler_PasswordResetting> mHandlers = null;

	public Thread_PasswordResetting(Context context, LoopHandler loopHandler) {
		super(context, loopHandler);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	@Override
	protected void onInitializing() {
		super.onInitializing();
		mHandlers = new Vector<Handler_PasswordResetting>();
	}

	public void getCAPTCHA(Handler_PasswordResetting handler) {
		if (mHandlers == null)
			return;
		mHandlers.add(handler);
		request(CAPTCHA_GETTING, null);
	}

	/*
	 * 获取验证短信
	 */
	public void getVerifyingSMS(Handler_PasswordResetting handler, String tel) {
		if (mHandlers == null)
			return;
		mHandlers.add(handler);
		request(VERIFYING_SMS_GETTING, tel);
	}

	public void resetPassword(Handler_PasswordResetting handler, String tel,
			String newPWD, String smsCode) {
		if (mHandlers == null)
			return;
		mHandlers.add(handler);
		Map<String, Object> newPWDInfo = new HashMap<String, Object>();
		newPWDInfo.put("Tel", tel);
		newPWDInfo.put("NewPWD", newPWD);
		newPWDInfo.put("SMSCode", smsCode);
		request(PASSWORD_RESETTING, newPWDInfo);
	}
	public void resetPassword(Handler_PasswordResetting handler, String password,
			String password1) {
		if (mHandlers == null)
			return;
		mHandlers.add(handler);
		Map<String, Object> newPWDInfo = new HashMap<String, Object>();
		newPWDInfo.put("password", password);
		newPWDInfo.put("password1", password1);
		request(PASSWORD_RESETTING, newPWDInfo);
	}

	@Override
	protected void onRunning(int action, Object obj) {
		// TODO Auto-generated method stub
		if (mHandlers == null || mHandlers.size() <= 0)
			return;
		Handler_PasswordResetting handler = mHandlers.remove(0);
		if (handler == null)
			return;

		switch (action) {
		case CAPTCHA_GETTING:
			parseCAPTCHAGetting(handler);
			break;
		case VERIFYING_SMS_GETTING: // 处理“获取验证短信”信息
			parseVerifyingSMSGetting(handler, obj.toString());
			break;
		case PASSWORD_RESETTING:

			if (obj == null || !(obj instanceof Map<?, ?>)) {
				parseException(handler, new IllegalArgumentException(
						"PASSWORD_RESETTING的Object无效"));
				break;
			}
			try {
				Map<String, Object> newPWDInfo = (Map<String, Object>) obj;
				String password = (String) newPWDInfo.get("password");
				String password1 = (String) newPWDInfo.get("password1");
				parsePasswordResetting(handler, password, password1);
			} catch (Exception e) {
				parseException(handler, e);
			}

			break;
		}
	}

	protected void parseException(Handler_PasswordResetting handler, Exception e) {
		if (handler == null)
			throw new NullPointerException("PasswordResettingHandler无效");
		Message msg = handler.obtainMessage(
				Handler_PasswordResetting.EXCEPTION, e);
		handler.sendMessage(msg);
	}

	protected void parsePasswordResetting(Handler_PasswordResetting handler,
			String password, String password1) {
		if (handler == null)
			throw new NullPointerException("PasswordResettingHandler无效");
		Message msg = null;
		try {
			WebReq_SetPwd aWebReq_SetPwd = new WebReq_SetPwd(mSettings.getUserName(), password, password1);
			WebRes_SetPwd aWebRes_SetPwd = new WebRes_SetPwd();
			execute(aWebReq_SetPwd, aWebRes_SetPwd);

			if (!aWebRes_SetPwd.getResult().equals("0")) {
				String failedDes = "重设密码失败";
				if (aWebRes_SetPwd.getMessage() != null) {
					failedDes = aWebRes_SetPwd.getMessage();
				}
				msg = handler.obtainMessage(
						Handler_PasswordResetting.EXCEPTION, new Exception(
								failedDes));
			} else {
				msg = handler.obtainMessage(
						Handler_PasswordResetting.PASSWORD_RESET, null);
			}
			handler.sendMessage(msg);
		} catch (Exception e) {
			String se = "ex:重设密码失败!" + e.getMessage();
			msg = handler.obtainMessage(Handler_PasswordResetting.EXCEPTION,
					new Exception(se));
			handler.sendMessage(msg);
		}
	}

	/*
	 * 获取验证短信，并解析
	 */
	protected void parseVerifyingSMSGetting(Handler_PasswordResetting handler,
			String tel) {
		if (handler == null)
			throw new NullPointerException("PasswordResettingHandler无效");
		Message msg = null;
		try {
			WebReq_SendVerifySMS aWebReq_SendVerifySMS = new WebReq_SendVerifySMS();
			aWebReq_SendVerifySMS.setTel(tel);
			WebRes_SendVerifySMS aWebRes_SendVerifySMS = new WebRes_SendVerifySMS();
			execute(aWebReq_SendVerifySMS, aWebRes_SendVerifySMS);
			if (!aWebRes_SendVerifySMS.getResult().equals("0")) {
				String failedDes = "获取验证短信失败";
				if (aWebRes_SendVerifySMS.getMessage() != null) {
					failedDes = aWebRes_SendVerifySMS.getMessage();
				}
				msg = handler.obtainMessage(
						Handler_PasswordResetting.EXCEPTION, new Exception(
								failedDes));
			} else {
				if (aWebRes_SendVerifySMS.getCode() == null) {
					msg = handler.obtainMessage(
							Handler_PasswordResetting.EXCEPTION,
							new IllegalArgumentException("获取的短信验证码无效"));
				}

				msg = handler.obtainMessage(
						Handler_PasswordResetting.VERIFYING_SMS_GOT,
						aWebRes_SendVerifySMS.getCode());
			}
			handler.sendMessage(msg);
		} catch (Exception e) {
			String se = "ex:获取验证短信失败!" + e.getMessage();
			msg = handler.obtainMessage(Handler_PasswordResetting.EXCEPTION,
					new Exception(se));
			handler.sendMessage(msg);
		}
	}

	@TargetApi(8)
	protected void parseCAPTCHAGetting(Handler_PasswordResetting handler) {
		if (handler == null)
			throw new NullPointerException("PasswordResettingHandler无效");
		Message msg = null;
		try {

			WebReq_GetVerifyCode aWebReq_GetVerifyImgCode = new WebReq_GetVerifyCode();
			WebRes_GetVerifyCode aWebRes_GetVerifyImgCode = new WebRes_GetVerifyCode();
			execute(aWebReq_GetVerifyImgCode, aWebRes_GetVerifyImgCode);
			if (!aWebRes_GetVerifyImgCode.getResult().equals("0")) {
				String failedDes = "获取验证码失败";
				Object cmdError = aWebRes_GetVerifyImgCode.getMessage();
				if (cmdError != null && cmdError instanceof String) {
					failedDes = (String) cmdError;
				}
				msg = handler.obtainMessage(
						Handler_PasswordResetting.EXCEPTION, new Exception(
								failedDes));
			} else {

//				if (aWebRes_GetVerifyImgCode.getImgBase64() == null) {
//					msg = handler.obtainMessage(
//							Handler_PasswordResetting.EXCEPTION,
//							new IllegalArgumentException("获取的验证码图像无效"));
//					handler.sendMessage(msg);
//					return;
//				}
//				String captchaImgSting = aWebRes_GetVerifyImgCode
//						.getImgBase64();
//
//				InputStream captchaImgStream = new Base64InputStream(
//						new ByteArrayInputStream(
//								captchaImgSting.getBytes("utf-8")),
//						Base64.DEFAULT);
//				Drawable captchaImg = Drawable.createFromStream(
//						captchaImgStream, null);

				Map<String, Object> captchaInfo = new HashMap<String, Object>();
//				captchaInfo.put("CAPTCHA", captcha);
//				captchaInfo.put("CAPTCHAImg", captchaImg);
//				msg = handler.obtainMessage(
//						Handler_PasswordResetting.CAPTCHA_GOT, captchaInfo);
			}
			handler.sendMessage(msg);

		} catch (Exception e) {
			String se = "ex:获取验证码失败!" + e.getMessage();
			msg = handler.obtainMessage(Handler_PasswordResetting.EXCEPTION,
					new Exception(se));
			handler.sendMessage(msg);
		}
	}
}
