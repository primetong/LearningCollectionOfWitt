package com.evguard.model;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class WebReq_GetCarMedical extends WebReq_Base {
	private String item;
	
	public WebReq_GetCarMedical(String item){
		this.item = item;
		super.setServiceUrl("GetCarMedical");
	}

	@Override
	public List<NameValuePair> getParamsData(List<NameValuePair> params) {
		params.add(new BasicNameValuePair("VIN", this.getVIN()));
		params.add(new BasicNameValuePair("Item", this.item));
		
		return params;
	}

}
