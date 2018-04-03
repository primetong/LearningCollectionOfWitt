package com.evguard.model;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class WebReq_SetHeart extends WebReq_Base {
	private String username;
	
	public WebReq_SetHeart(){
		super.setServiceUrl("SetHeart");
	}
	
	
	public void setUsername(String username) {
		this.username = username;
	}


	@Override
	public List<NameValuePair> getParamsData(List<NameValuePair> params) {
		params.add(new BasicNameValuePair("UserName", this.username));
		return params;
	}

}
