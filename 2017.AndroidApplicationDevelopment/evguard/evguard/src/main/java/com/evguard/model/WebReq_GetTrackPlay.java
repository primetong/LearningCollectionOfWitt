package com.evguard.model;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class WebReq_GetTrackPlay extends WebReq_Base {
	
	private String beginTime = "";
	private String endTime = "";
	private String pageIndex = "";
	
	public WebReq_GetTrackPlay(String beginTime,String endTime,String pageIndex){
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.pageIndex = pageIndex;
		
		super.setServiceUrl("GetTrackPlay");
	}

	@Override
	public List<NameValuePair> getParamsData(List<NameValuePair> params) {
		params.add(new BasicNameValuePair("VIN", this.getVIN()));
		params.add(new BasicNameValuePair("BeginTime", this.beginTime));
		params.add(new BasicNameValuePair("EndTime", this.endTime));
		params.add(new BasicNameValuePair("PageIndex", this.pageIndex));
		return params;
	}


}
