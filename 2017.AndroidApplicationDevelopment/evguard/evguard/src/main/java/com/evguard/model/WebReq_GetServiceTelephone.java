package com.evguard.model;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class WebReq_GetServiceTelephone extends WebReq_Base {
	
	public WebReq_GetServiceTelephone(){
		super.setServiceUrl("GetServiceTelephone");
	}

	@Override
	public List<NameValuePair> getParamsData(List<NameValuePair> params) {
		params.add(new BasicNameValuePair("VIN", this.getVIN()));
		params.add(new BasicNameValuePair("SecretKey", this.getSecretKey()));
		params.add(new BasicNameValuePair("Password", this.password));
		
		return params;
	}

}
