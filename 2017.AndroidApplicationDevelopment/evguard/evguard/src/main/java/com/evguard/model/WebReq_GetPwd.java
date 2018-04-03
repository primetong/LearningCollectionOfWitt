package com.evguard.model;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class WebReq_GetPwd extends WebReq_Base {
	
	private String username = "";
	private String verifycode = "";
	
	public WebReq_GetPwd(){
		setServiceUrl("GetPwd");
	}

	@Override
	public List<NameValuePair> getParamsData(List<NameValuePair> params) {
		params.add(new BasicNameValuePair("UserName", this.username));
		params.add(new BasicNameValuePair("VerifyCode", this.verifycode));
		return params;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setVerifycode(String verifycode) {
		this.verifycode = verifycode;
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
