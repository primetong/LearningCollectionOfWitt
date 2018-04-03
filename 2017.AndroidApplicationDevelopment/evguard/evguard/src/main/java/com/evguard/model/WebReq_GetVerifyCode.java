package com.evguard.model;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * 获取图片验证码
 * 
 * @author wlh
 * 
 */
public class WebReq_GetVerifyCode extends WebReq_Base {
	
	private String Username;

	public WebReq_GetVerifyCode() {
		setServiceUrl("GetVerifyCode");
	}

	@Override
	public List<NameValuePair> getParamsData(List<NameValuePair> params) {
		
		params.add(new BasicNameValuePair("UserName", this.Username));
		return params;
	}


	public void setUsername(String username) {
		Username = username;
	}
	public void setServiceUrl(String method){
		setMethod(method);
		this.ServiceUrl = this.HeadUrl + this.Method + "?v=" + getVersionCode();
//		if(method.equals("Login") || method.equals("GetVerifyCode") || method.equals("GetPwd")){
//			this.ServiceUrl = this.HeadUrl + this.Method + "?v=" + getVersionCode();
//			return;
//		}
		}
}
