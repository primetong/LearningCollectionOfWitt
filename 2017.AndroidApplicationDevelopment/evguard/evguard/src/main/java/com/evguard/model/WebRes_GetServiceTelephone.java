package com.evguard.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WebRes_GetServiceTelephone extends WebRes_Base {

	private String insuranceTelephone;//保险电话
	private String alarmTelephone;//报警电话
	private String drivingTelephone;//代驾电话
	private String serviceTelephone;//4S服务店电话
	private String rescueTelephone;//救援电话
	
	
	@Override
	protected void parseData(JSONObject jsonobj) throws JSONException {
		JSONObject json=jsonobj.getJSONObject("Info");
		this.insuranceTelephone = json.getString("InsuranceTelephone");
		this.alarmTelephone = json.getString("AlarmTelephone");
		this.drivingTelephone = json.getString("DriVingTelephone");
		this.serviceTelephone = json.getString("ServiceTelephone");
		this.rescueTelephone = json.getString("RescueTelephone");
	}


	public String getInsuranceTelephone() {
		return insuranceTelephone;
	}


	public String getAlarmTelephone() {
		return alarmTelephone;
	}


	public String getDrivingTelephone() {
		return drivingTelephone;
	}


	public String getServiceTelephone() {
		return serviceTelephone;
	}


	public String getRescueTelephone() {
		return rescueTelephone;
	}
	
	
}
