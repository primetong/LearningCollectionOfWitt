package com.evguard.model;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.evguard.tools.CommUtils;

public class WebReq_GetLocation extends WebReq_Base {
	
	private String param;
	
	public WebReq_GetLocation(List<AddressPoint> addressList){
		param = CommUtils.parseToStr(addressList);
		Log.i("llj", "param--" + param);
		super.setServiceUrl("GetLocation");
	}

	@Override
	public List<NameValuePair> getParamsData(List<NameValuePair> params) {
		params.add(new BasicNameValuePair("param", this.param));
		return params;
	}

}
