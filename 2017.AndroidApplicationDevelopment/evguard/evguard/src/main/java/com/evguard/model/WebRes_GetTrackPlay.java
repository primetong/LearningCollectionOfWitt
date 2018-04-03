package com.evguard.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.evguard.main.WebRequestThreadEx;

public class WebRes_GetTrackPlay extends WebRes_Base {
	private String carNum = "";
	private String VIN = "";
	private List<TrackInfo> mTrackInfoList = new ArrayList<TrackInfo>();
	private List<AddressPoint> mAddressList = new ArrayList<AddressPoint>();

	

	@Override
	protected void parseData(JSONObject jsonobj) throws JSONException {
		JSONObject json=jsonobj.getJSONObject("Info");
		this.carNum = json.getString("CarNum");
		this.VIN = json.getString("Vin");
		if (mTrackInfoList == null)
			mTrackInfoList = new ArrayList<TrackInfo>();
		if (mAddressList == null)
			mAddressList = new ArrayList<AddressPoint>();
		mTrackInfoList.clear();
		mAddressList.clear();
		JSONArray jsonArray = json.getJSONArray("Rows");
		JSONObject jsonObject = null;
		
		AddressPoint mPoint;
		for (int i = 0; i < jsonArray.length(); i++) {
			jsonObject = jsonArray.getJSONObject(i);
			TrackInfo trackInfo = new TrackInfo();
			trackInfo.setCarnum(this.carNum);
			trackInfo.setGpsTime(jsonObject.getString("GpsTime"));
			trackInfo.setCarStatus(jsonObject.getString("CarStatu"));
			String direct = jsonObject.getString("Direction");
			if(direct != null) trackInfo.setDirect(direct);
			else trackInfo.setDirect("0");
			trackInfo.setMileage(jsonObject.getString("Mileage"));
			String speed = jsonObject.getString("Speed");
			if(speed != null) trackInfo.setSpeed(speed);
			else trackInfo.setSpeed("0");
			String lat = jsonObject.getString("Latitude");
			trackInfo.setLatitude(lat);
			String lon = jsonObject.getString("Longitude");
			trackInfo.setLongitude(lon);
			mPoint = new AddressPoint(Double.parseDouble(lon),
					Double.parseDouble(lat), i);
			mAddressList.add(mPoint);
			mTrackInfoList.add(trackInfo);
		}
	}

	public List<TrackInfo> getTrackInfo() {
		if(mTrackInfoList == null)
			mTrackInfoList = new ArrayList<TrackInfo>();
		return this.mTrackInfoList;
	}
	/**
	 * @return the mAddressList
	 */
	public List<AddressPoint> getAddressList() {
		if(mAddressList == null)
			mAddressList = new ArrayList<AddressPoint>();
		return mAddressList;
	}
}
