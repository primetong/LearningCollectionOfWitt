package com.evguard.model;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * 获取轨迹总数
 * 
 * @author wlh
 * 
 */
public class WebReq_GetTrackPlayCount extends WebReq_Base {
	private String beginTime;
	private String endTime;

	public WebReq_GetTrackPlayCount(String beginTime, String endTime) {
		this.beginTime = beginTime;
		this.endTime = endTime;
		super.setServiceUrl("GetTrackPlayCount");
	}

	@Override
	public List<NameValuePair> getParamsData(List<NameValuePair> params) {
		
		params.add(new BasicNameValuePair("VIN", this.getVIN()));
		params.add(new BasicNameValuePair("BeginTime", this.beginTime));
		params.add(new BasicNameValuePair("EndTime", this.endTime));
		return params;
	}

}
