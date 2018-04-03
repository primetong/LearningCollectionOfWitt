package com.evguard.model;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class WebReq_GetNotice extends WebReq_Base {
	private String time;
	
	public WebReq_GetNotice(String time){
		this.time = time;
		super.setServiceUrl("GetNotice");
	}

	@Override
	public List<NameValuePair> getParamsData(List<NameValuePair> params) {
		params.add(new BasicNameValuePair("Time", this.time));
		return params;
	}

}
