package com.evguard.main;

import java.util.Map;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

public class Handler_GetPassword extends Handler {

	public static final int EXCEPTION = 0;
	public static final int CAPTCHA_GOT = 1;
	public static final int GET_PASSWORD = 2;

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);

		if (msg == null) {
			onException(new NullPointerException("Message无效"));
			return;
		}

		int action = msg.what;
		switch (action) {
		case EXCEPTION:
			if (msg.obj == null || !(msg.obj instanceof Exception)) {
				onException(new IllegalArgumentException("异常信息无效"));
				return;
			}
			onException((Exception) msg.obj);
			break;

		case CAPTCHA_GOT:
			if (msg.obj == null || !(msg.obj instanceof Map<?, ?>)) {
				onException(new IllegalArgumentException("获取验证码信息无效"));
				return;
			}
			Map<String, Object> captchaInfo = null;
			try {
				captchaInfo = (Map<String, Object>) msg.obj;
			} catch (Exception e) {
				onException(e);
			}
			Object captchaObj = captchaInfo.get("CAPTCHA");
			if (captchaObj == null || !(captchaObj instanceof String)) {
				onException(new IllegalArgumentException("获取的验证码无效"));
				return;
			}
			Object captchaImgObj = captchaInfo.get("CAPTCHAImg");
			if (captchaImgObj == null || !(captchaImgObj instanceof Drawable)) {
				onException(new IllegalArgumentException("获取的验证码图像无"));
				return;
			}
			onCAPTCHAGot((String) captchaObj, (Drawable) captchaImgObj);
			break;
		case GET_PASSWORD:
			onGetPassword();
			break;
		}

	}

	protected void onException(Exception e) {}
	protected void onCAPTCHAGot(String captcha, Drawable captchaImg) {}
	protected void onGetPassword() {}

	protected void onVerifyingSMSGot(String smsCode) {
		
	}
}
