package com.evguard.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WebRes_GetLocation extends WebRes_Base {
	
	List<AddressInfo> mAddressList = new ArrayList<AddressInfo>();
	

	/**
	 * @return the mAddressList
	 */
	public List<AddressInfo> getAddressList() {
		return mAddressList;
	}

	@Override
	protected void parseData(JSONObject json) throws JSONException {
		JSONArray infos=json.getJSONArray("Info");
		AddressInfo mAddressInfo;
		for (int i = 0; i < json.length(); i++) {
			mAddressInfo = new AddressInfo();
			JSONObject jsonObject = infos.getJSONObject(i);
			mAddressInfo.setAddress(jsonObject.getString("Location"));
			mAddressInfo.setID(jsonObject.getString("ID"));
			mAddressList.add(mAddressInfo);
		}
	}
}
