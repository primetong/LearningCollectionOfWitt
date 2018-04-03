package com.evguard.model;

import java.sql.Timestamp;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.evguard.tools.LogEx;
import com.xinghaicom.security.MD5;



import android.util.Log;

/**
 * 登录Login
 * @author wlh
 *
 */
public class WebReq_Login extends WebReq_Base {

	private String username;
	private String password;
	private String tick;
	


	public WebReq_Login(String username,String password,String timestamp) {
		this.username = username;
		try 
    	{
    		MD5 md5 = new MD5();
    		this.password = md5.hashToDigestDes(password);
		} catch (Exception e) {
			Log.e("ConstantTool", "MD5加密错误" + e.getMessage());
		}
		this.tick = timestamp;
		setServiceUrl("Login");
	}
	
	@Override
	public List<NameValuePair> getParamsData(List<NameValuePair> params) {
    	params.add(new BasicNameValuePair("UserName", this.username));
    	params.add(new BasicNameValuePair("Password", this.password));	
    	params.add(new BasicNameValuePair("Tick", this.tick));	
    	return params;
	}
	@Override
	public void setServiceUrl(String method){
		setMethod(method);
		this.ServiceUrl = this.HeadUrl + this.Method + "?v=" + getVersionCode();
//		if(method.equals("Login") || method.equals("GetVerifyCode") || method.equals("GetPwd")){
//			this.ServiceUrl = this.HeadUrl + this.Method + "?v=" + getVersionCode();
//			return;
//		}
		}
	
}
