package com.evguard.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class WebRes_GetCurrentPos extends WebRes_Base {
	
	private String carNum = "0";
	private double longitude;
	private double latitude;
	private String gpsTime = "";
	private String direction = "0";
	private String mileage = "0";
	private String speed = "0";
	

	@Override
	protected void parseData(JSONObject jsonobj) throws JSONException {
		JSONObject json=jsonobj.getJSONObject("Info");
		if (json.get("CarNum") != JSONObject.NULL) 
			this.carNum = json.getString("CarNum");
		if (json.get("Direction") != JSONObject.NULL) 
			this.direction = json.getString("Direction");
		if (json.get("Mileage") != JSONObject.NULL) 
			this.mileage = json.getString("Mileage");
		if (json.get("Speed") != JSONObject.NULL) 
			this.speed = json.getString("Speed");
		if (json.get("Longitude") != JSONObject.NULL) 
			this.longitude =  Double.parseDouble(json.getString("Longitude"));
		if (json.get("Latitude") != JSONObject.NULL) 
			this.latitude = Double.parseDouble(json.getString("Latitude"));
		if (json.get("GpsTime") != JSONObject.NULL) 
			this.gpsTime = json.getString("GpsTime");
	}
	

	public String getCarNum() {
		return carNum;
	}


	public Double getLongitude() {
		return longitude;
	}


	public Double getLatitude() {
		return latitude;
	}


	public String getGpsTime() {
		return gpsTime;
	}

	public String getDirection() {
		return direction;
	}

	public String getMileage() {
		return mileage;
	}

	public String getSpeed() {
		return speed;
	}


}
