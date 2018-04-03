package com.evguard.model;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class WebReq_GetCurrentInfo extends WebReq_Base {

	public WebReq_GetCurrentInfo() {
		super.setServiceUrl("GetCurrentInfo");
	}

	@Override
	public List<NameValuePair> getParamsData(List<NameValuePair> params) {
		params.add(new BasicNameValuePair("VIN", this.getVIN()));
		return params;
	}

}
