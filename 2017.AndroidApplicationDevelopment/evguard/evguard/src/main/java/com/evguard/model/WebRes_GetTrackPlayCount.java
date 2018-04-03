package com.evguard.model;

import org.json.JSONException;
import org.json.JSONObject;

public class WebRes_GetTrackPlayCount extends WebRes_Base {

	private String totalCount = "";
	private String pageSize = "";

	@Override
	protected void parseData(JSONObject jsonobj) throws JSONException {
		JSONObject json=jsonobj.getJSONObject("Info");
		this.totalCount = json.getString("TotalCount");
		this.pageSize = json.getString("PageSize");
	}

	public String getTotalCount() {
		return this.totalCount;
	}
	
	public String getPageSize() {
		return this.pageSize;
	}

}
