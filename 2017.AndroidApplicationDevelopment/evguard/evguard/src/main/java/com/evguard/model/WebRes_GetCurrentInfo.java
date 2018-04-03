package com.evguard.model;

import org.json.JSONException;
import org.json.JSONObject;

public class WebRes_GetCurrentInfo extends WebRes_Base {
	
	private String carNum = "";
	private String mileage = "0";
	private String drivingMileage = "0";
	private String SOC = "0";
	
	
	

	@Override
	protected void parseData(JSONObject jsonobj) throws JSONException {
		JSONObject json=jsonobj.getJSONObject("Info");
		String Carnum_get = json.getString("CarNum");
		if(!Carnum_get.equals("") && json.get("Mileage") != JSONObject.NULL)
			this.carNum = Carnum_get;
		if (json.get("Mileage") != JSONObject.NULL) { 
			this.mileage = json.getString("Mileage");
	    }  
		if(json.get("DriVingMileage") != JSONObject.NULL)
			this.drivingMileage = json.getString("DriVingMileage");
		if(json.get("SOC") != JSONObject.NULL)
			this.SOC = json.getString("SOC");
	}

	public String getCarNum() {
		return carNum;
	}

	public String getMileage() {
		if(mileage.equals(""))
			return "0";
		return mileage;
	}

	public String getDrivingMileage() {
		if(drivingMileage.equals(""))
			return "0";
		return drivingMileage;
	}

	public String getSOC() {
		if(SOC.equals(""))
			return "0";
		System.out.println("SOC==" + SOC);
		return SOC;
	}
	

}
