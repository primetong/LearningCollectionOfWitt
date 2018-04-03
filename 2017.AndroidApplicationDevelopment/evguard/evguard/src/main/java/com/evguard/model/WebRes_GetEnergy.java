package com.evguard.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.evguard.tools.CommUtils;

public class WebRes_GetEnergy extends WebRes_Base {
	
	private String mileage = "0";
	private String consumeElectricity = "0";
	private String averageConsumption = "0";
	private String economy = "0";
	ArrayList<ElectricDetail> mElectricInfo = new ArrayList<ElectricDetail>();
	
	public  void ParseJson(JSONObject json) throws JSONException{
		if(json==null)return;
		this.setResult(json.getString("Result"));
		this.setMessage(json.getString("Message"));
		if (json.get("Info") != JSONObject.NULL)
			parseData(json.getJSONObject("Info"));
	}
	
	
	@Override
	protected void parseData(JSONObject json) throws JSONException {
	
		if (json.get("Mileage") != JSONObject.NULL) 
			this.mileage = json.getString("Mileage");
		if (json.get("ConsumeElectricity") != JSONObject.NULL) 
			this.consumeElectricity = json.getString("ConsumeElectricity");
		if (json.get("AverageConsumption") != JSONObject.NULL) 
			this.averageConsumption = json.getString("AverageConsumption");
		if (json.get("Economy") != JSONObject.NULL) 
			this.economy = json.getString("Economy");
		if (json.get("Rows") != JSONObject.NULL){
			JSONArray infoArray = json.getJSONArray("Rows");
			if(mElectricInfo == null) mElectricInfo = new ArrayList<ElectricDetail>();
			for (int i = 0; i < infoArray.length(); i++) {
				JSONObject infoObject = infoArray.getJSONObject(i);
				ElectricDetail info = new ElectricDetail();
				String daytime = infoObject.getString("Day");
				info.setDay(CommUtils.getTime(daytime));
				String value = infoObject.getString("Val");
				info.setValue(Float.parseFloat(value));
				mElectricInfo.add(info);
			}
		}
	}


	public String getMileage() {
		return mileage;
	}


	public String getConsumeElectricity() {
		return consumeElectricity;
	}


	public String getAverageConsumption() {
		return averageConsumption;
	}


	public String getEconomy() {
		return economy;
	}

	public ArrayList<ElectricDetail> getElectricInfo(){
		return mElectricInfo;
	}

}
