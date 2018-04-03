package com.evguard.model;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class WebReq_GetChargeManager extends WebReq_Base {
	
	public WebReq_GetChargeManager(){
		super.setServiceUrl("GetChargeManager");
	}

	@Override
	public List<NameValuePair> getParamsData(List<NameValuePair> params) {
		params.add(new BasicNameValuePair("VIN", this.getVIN()));
		params.add(new BasicNameValuePair("SecretKey", this.getSecretKey()));
		params.add(new BasicNameValuePair("Password", this.password));
		return params;
	}

}
