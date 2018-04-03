package com.evguard.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.evguard.tools.CommUtils;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class WebRes_GetAdvertising extends WebRes_Base {
	
	private List<String> imageList = new ArrayList<String>();

	@Override
	protected void parseData(JSONObject jsonobj) throws JSONException {
		JSONObject json=jsonobj.getJSONObject("Info");
		JSONArray array = json.getJSONArray("Image");
		for(int i=0; i<array.length();i++){
			JSONObject image = array.getJSONObject(i);
			String src = image.getString("Src");
			imageList.add(src);
		}
	}

	public List<String> getImageList() {
		Log.i("llj", "imageList>>" + imageList.size());
		return imageList;
	}
	
}
