package com.evguard.model;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.xinghaicom.security.MD5;

public class WebReq_SetPwd extends WebReq_Base {
	
	private String username;
	private String password;
	private String password1;
	
	public WebReq_SetPwd(String username,String password,String password1){
		this.username = username;
		try 
    	{
    		MD5 md5 = new MD5();
    		this.password = md5.hashToDigestDes(password);
    		this.password1 = md5.hashToDigestDes(password1);
		} catch (Exception e) {
			Log.e("ConstantTool", "MD5加密错误" + e.getMessage());
		}
		setServiceUrl("SetPwd");
	}

	@Override
	public List<NameValuePair> getParamsData(List<NameValuePair> params) {
		mParams.add(new BasicNameValuePair("UserName", this.username));
		mParams.add(new BasicNameValuePair("Password", this.password));
		mParams.add(new BasicNameValuePair("Password1", this.password1));
		return mParams;
	}

}
