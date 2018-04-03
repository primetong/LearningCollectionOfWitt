package com.evguard.model;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.evguard.tools.CommUtils;

public class WebReq_SetAlarmRemind extends WebReq_Base {

	private String Parameter;
	
	public WebReq_SetAlarmRemind(List<AlamSettings> alamSettings){
		this.Parameter = CommUtils.parseToStr(alamSettings);
		super.setServiceUrl("SetAlarmRemind");
	}

	@Override
	public List<NameValuePair> getParamsData(List<NameValuePair> params) {
		params.add(new BasicNameValuePair("VIN", this.getVIN()));
		params.add(new BasicNameValuePair("Parameter", this.Parameter));
		return params;
	}

}
