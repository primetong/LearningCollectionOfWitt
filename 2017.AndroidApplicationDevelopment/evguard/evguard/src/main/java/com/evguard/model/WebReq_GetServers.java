package com.evguard.model;

import java.util.List;

import org.apache.http.NameValuePair;

import com.evguard.tools.LogEx;

public class WebReq_GetServers extends WebReq_Base {

	public WebReq_GetServers(){
		setServiceUrl("GetServers");
	}
	@Override
	public List<NameValuePair> getParamsData(List<NameValuePair> params) {
		// TODO Auto-generated method stub
		return params;
	}
	@Override
	public void setServiceUrl(String method){
		setMethod(method);
		this.ServiceUrl = mSettings.getServerSwitchIp() + this.Method + "?v=" + getVersionCode();
		LogEx.i("wwlh", ServiceUrl);
	}
}
