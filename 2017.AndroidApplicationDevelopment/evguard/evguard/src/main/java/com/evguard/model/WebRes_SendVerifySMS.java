package com.evguard.model;

import org.json.JSONException;
import org.json.JSONObject;

public class WebRes_SendVerifySMS extends WebRes_Base {

	private String Code = "";

	@Override
	protected void parseData(JSONObject jsonobj) throws JSONException {
		JSONObject json=jsonobj.getJSONObject("Info");
		Code = json.getString("Code");
	}

	public String getCode() {
		return this.Code;
	}

}
