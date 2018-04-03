package com.evguard.model;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class WebReq_GetEnergy extends WebReq_Base {
	private int day;
	
	public WebReq_GetEnergy(int day){
		this.day = day;
		super.setServiceUrl("GetEnergy");
	}

	@Override
	public List<NameValuePair> getParamsData(List<NameValuePair> params) {
		params.add(new BasicNameValuePair("VIN", this.getVIN()));
		params.add(new BasicNameValuePair("Day", this.day+""));
		
		return params;
	}

}
