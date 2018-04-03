package com.evguard.model;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.evguard.tools.CommUtils;

public class WebReq_GetAlarmRemind extends WebReq_Base {
	
	
	public WebReq_GetAlarmRemind(){
		super.setServiceUrl("GetAlarmRemind");
	}

	@Override
	public List<NameValuePair> getParamsData(List<NameValuePair> params) {
		params.add(new BasicNameValuePair("VIN", this.getVIN()));
		return params;
	}

}
