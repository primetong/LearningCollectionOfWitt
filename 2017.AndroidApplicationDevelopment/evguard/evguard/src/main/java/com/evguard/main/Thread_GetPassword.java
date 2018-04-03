package com.evguard.main;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.util.Base64;
import android.util.Base64InputStream;

import com.evguard.model.WebReq_GetVerifyCode;
import com.evguard.model.WebReq_SendVerifySMS;
import com.evguard.model.WebRes_GetVerifyCode;
import com.evguard.model.WebRes_SendVerifySMS;
import com.xinghaicom.asynchrony.LoopHandler;

public class Thread_GetPassword extends Thread_Base{

	public static final int EXCEPTION = 0;
	public static final int VERIFYING_SMS_GETTING = 1;
	public static final int VERIFYING_SMS_GOT = 2;
	public static final int GET_PASSWORD = 2;
	protected Vector<Handler_GetPassword> mHandlers = null;
	
	public Thread_GetPassword(Context context, LoopHandler loopHandler) {
		super(context, loopHandler);
	}

	@Override
	protected void onInitializing() {
		super.onInitializing();
		mHandlers = new Vector<Handler_GetPassword>();
	}
	
	
	
	public void getPassword(Handler_GetPassword handler, String tel) {
		if (mHandlers == null)
			return;
		mHandlers.add(handler);
		Map<String, Object> newPWDInfo = new HashMap<String, Object>();
		newPWDInfo.put("Tel", tel);
		request(GET_PASSWORD, newPWDInfo);
	}
	
	@Override
	protected void onRunning(int action, Object obj) {
		// TODO Auto-generated method stub
		if (mHandlers == null || mHandlers.size() <= 0)
			return;
		Handler_GetPassword handler = mHandlers.remove(0);
		if (handler == null)
			return;

		switch (action) {
		
		case VERIFYING_SMS_GETTING: // 处理“获取验证短信”信息
			parseVerifyingSMSGetting(handler, obj.toString());
			break;
		case GET_PASSWORD:

			if (obj == null || !(obj instanceof Map<?, ?>)) {
				parseException(handler, new IllegalArgumentException(
						"PASSWORD_RESETTING的Object无效"));
				break;
			}
			try {
				Map<String, Object> newPWDInfo = (Map<String, Object>) obj;
				String tel = (String) newPWDInfo.get("Tel");
				parseGetPassword(handler, tel);
			} catch (Exception e) {
				parseException(handler, e);
			}

			break;
		}
	}

	private void parseGetPassword(Handler_GetPassword handler, String tel) {
		// TODO 实现
		
	}

	private void parseException(Handler_GetPassword handler,
			Exception e) {
		if (handler == null)
			throw new NullPointerException("PasswordResettingHandler无效");
		Message msg = handler.obtainMessage(
				Handler_GetPassword.EXCEPTION, e);
		handler.sendMessage(msg);
		
	}

	/*
	 * 获取验证短信
	 */
	public void getVerifyingSMS(Handler_GetPassword handler, String tel) {
		if (mHandlers == null)
			return;
		mHandlers.add(handler);
		request(VERIFYING_SMS_GETTING, tel);
	}	
	/*
	 * 获取验证短信，并解析
	 */
	protected void parseVerifyingSMSGetting(Handler_GetPassword handler,
			String tel) {
		if (handler == null)
			throw new NullPointerException("PasswordResettingHandler无效");
		Message msg = null;
		try {
			WebReq_GetVerifyCode aWebReq_GetVerifyCode = new WebReq_GetVerifyCode();
			aWebReq_GetVerifyCode.setUsername(tel);
			WebRes_GetVerifyCode aWebRes_GetVerifyCode = new WebRes_GetVerifyCode();
			execute(aWebReq_GetVerifyCode, aWebRes_GetVerifyCode);
			if (!aWebRes_GetVerifyCode.getResult().equals("0")) {
				String failedDes = "获取验证短信失败";
				if (aWebRes_GetVerifyCode.getMessage() != null) {
					failedDes = aWebRes_GetVerifyCode.getMessage();
				}
				msg = handler.obtainMessage(
						Handler_PasswordResetting.EXCEPTION, new Exception(
								failedDes));
			} else {
				msg = handler.obtainMessage(
						Handler_PasswordResetting.VERIFYING_SMS_GOT);
			}
			handler.sendMessage(msg);
		} catch (Exception e) {
			String se = "ex:获取验证短信失败!" + e.getMessage();
			msg = handler.obtainMessage(Handler_PasswordResetting.EXCEPTION,
					new Exception(se));
			handler.sendMessage(msg);
		}
	}
	
}
