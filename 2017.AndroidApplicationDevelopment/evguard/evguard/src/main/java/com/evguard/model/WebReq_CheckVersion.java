package com.evguard.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.util.Log;
/**
 * 版本检查
 * @author wlh
 *
 */
public class WebReq_CheckVersion extends WebReq_Base {
	private String Version = "";
	private String AppType = "1";
	
	public WebReq_CheckVersion() {
		this.Version = getVersion();
		Log.i("llj", "Version--" + Version);
		this.AppType = "1";
		super.setServiceUrl("CheckVersion");
	}

	@Override
	public List<NameValuePair> getParamsData(List<NameValuePair> params) {
    	params.add(new BasicNameValuePair("Version",this.Version));
    	params.add(new BasicNameValuePair("AppType", this.AppType));
    	return params;    
	}

}
