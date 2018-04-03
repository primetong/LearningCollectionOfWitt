package com.evguard.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WebRes_GetAlarmRemind extends WebRes_Base {

	List<AlamSettings> mAlamSettings = new ArrayList<AlamSettings>();

	
	public List<AlamSettings> getAlamSettings(){
		return mAlamSettings;
	}

	@Override
	protected void parseData(JSONObject json) throws JSONException {
		
		JSONArray infos=json.getJSONArray("Info");
		AlamSettings mSetting;
		for (int i = 0; i < json.length(); i++) {
			mSetting = new AlamSettings();
			JSONObject jsonObject = infos.getJSONObject(i);
			mSetting.setParam(jsonObject.getString("Param"));
			if (!jsonObject.get("Value").equals("")) 
				mSetting.setValue(jsonObject.getString("Value"));
			else
				mSetting.setValue("0");
			mAlamSettings.add(mSetting);
		}
	}
}
