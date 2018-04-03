package com.evguard.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

/**
 * 获取短信验证
 * 
 * @author wlh
 * 
 */
public class WebReq_SendVerifySMS extends WebReq_Base {

	private String Tel;

	public WebReq_SendVerifySMS() {
		super.Method = "sendverifysms";
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<NameValuePair> getParamsData(List<NameValuePair> params) {
		params.add(new BasicNameValuePair("method", this.Method));
		params.add(new BasicNameValuePair("tel", this.Tel));

		return params;
	}

	public void setTel(String s) {
		this.Tel = s;
	}

	public String getTel() {
		return this.Tel;
	}

}
