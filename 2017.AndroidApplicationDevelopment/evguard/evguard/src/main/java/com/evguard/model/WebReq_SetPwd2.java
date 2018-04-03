package com.evguard.model;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.xinghaicom.security.MD5;

public class WebReq_SetPwd2 extends WebReq_Base {
	
	private String username;
	private String verifyCode;
	private String password;
	
	public WebReq_SetPwd2(String username,String verifyCode,String password){
		this.username = username;
		this.verifyCode = verifyCode;
		try 
    	{
    		MD5 md5 = new MD5();
    		this.password = md5.hashToDigestDes(password);
		} catch (Exception e) {
			Log.e("ConstantTool", "MD5加密错误" + e.getMessage());
		}
		setServiceUrl("SetPwd2");
	}

	@Override
	public List<NameValuePair> getParamsData(List<NameValuePair> params) {
		mParams.add(new BasicNameValuePair("UserName", this.username));
		mParams.add(new BasicNameValuePair("VerifyCode", this.verifyCode));
		mParams.add(new BasicNameValuePair("Password", this.password));
		return mParams;
	}

}
