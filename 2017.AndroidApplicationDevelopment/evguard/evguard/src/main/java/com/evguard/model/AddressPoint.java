package com.evguard.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.JsonReader;

public class AddressPoint {

	private double mLon;
	private double mLat;
	private int ID=0;
	
	public AddressPoint(double lon,double lat,int index){
		this.mLon=lon;
		this.mLat=lat;
		this.ID=index;
	}
	public double getLon(){
		return mLon;
	}
	public double getLat(){
		return mLat;
	}
	public double getIndex(){
		return this.ID;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "{\"ID\":\"" + this.ID + "\",\"Lon\":\"" + this.mLon + "\",\"Lat\":\"" + this.mLat + "\"}";
	}
	
	
}
