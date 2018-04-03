package com.evguard.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class WebRes_Base {
	
	private String result;
	private String message;
	
	public WebRes_Base()
	{
		
	}
	public  void ParseJson(JSONObject jsonobj) throws JSONException{
		if(jsonobj==null)return;
		this.setResult(jsonobj.getString("Result"));
		this.setMessage(jsonobj.getString("Message"));
		parseData(jsonobj);
//		if(this.getResult().equals("0")){
//			if(json.get("Info") != JSONObject.NULL)
//				parseData(json.getJSONObject("Info"));
//		}
	}

  protected abstract void parseData(JSONObject json) throws JSONException;
//  protected void parseData(JSONArray json) throws JSONException{}
	
  public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
