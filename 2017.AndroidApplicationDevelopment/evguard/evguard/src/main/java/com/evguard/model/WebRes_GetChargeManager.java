package com.evguard.model;

import org.json.JSONException;
import org.json.JSONObject;

public class WebRes_GetChargeManager extends WebRes_Base {
	
	private String SOC = "0";
	private String chargeRemainderTime = "0";
	private String electricity = "0";
	private String voltage = "0";
	private String flag = "0";
	private String fastCharge = "0";
	private String rowCharge = "0";
	

	@Override
	protected void parseData(JSONObject jsonobj) throws JSONException {
		JSONObject json=jsonobj.getJSONObject("Info");
		if (json.get("SOC") != JSONObject.NULL) 
			this.SOC = json.getString("SOC");
		if (json.get("ChargeRemainderTime") != JSONObject.NULL) 
			this.chargeRemainderTime = json.getString("ChargeRemainderTime");
		if (json.get("Electricity") != JSONObject.NULL) 
			this.electricity = json.getString("Electricity");
		if (json.get("Voltage") != JSONObject.NULL) 
			this.voltage = json.getString("Voltage");
		if (json.get("Flag") != JSONObject.NULL) 
			this.flag = json.getString("Flag");
		if (json.get("FastCharge") != JSONObject.NULL) 
			this.fastCharge = json.getString("FastCharge");
		if (json.get("RowCharge") != JSONObject.NULL) 
			this.rowCharge = json.getString("RowCharge");
	}


	public String getSOC() {
		return SOC;
	}


	public String getChargeRemainderTime() {
		return chargeRemainderTime;
	}


	public String getElectricity() {
		return electricity;
	}


	public String getVoltage() {
		return voltage;
	}
	
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getFastCharge() {
		return fastCharge;
	}

	public void setFastCharge(String fastCharge) {
		this.fastCharge = fastCharge;
	}
	public String getRowCharge() {
		return rowCharge;
	}

	public void setRowCharge(String rowCharge) {
		this.rowCharge = rowCharge;
	}



}
