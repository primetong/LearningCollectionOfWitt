package com.evguard.model;

import org.json.JSONException;
import org.json.JSONObject;

public class WebRes_Login extends WebRes_Base {

	private String secretKey = "";
	private String carNum = "";
	private String VIN = "";
	private String heartInterval = "";
	private String aboutUrl = "";
	private String carIllegalUrl = "";

	@Override
	protected void parseData(JSONObject jsonobj) throws JSONException {
		JSONObject json=jsonobj.getJSONObject("Info");
		this.secretKey = json.getString("SecretKey");
		this.carNum = json.getString("CarNum");
		this.VIN = json.getString("VIN");
		this.heartInterval = json.getString("HeartInterval");
		this.aboutUrl = json.getString("AboutUrl");
		if(!json.isNull("CarIllegalUrl"))
			this.carIllegalUrl = json.getString("CarIllegalUrl");
	}

	public String getSecretKey() {
		return secretKey;
	}

	public String getCarNum() {
		return carNum;
	}

	public String getVIN() {
		return VIN;
	}

	public String getHeartInterval() {
		return heartInterval;
	}

	public String getAboutUrl() {
		return aboutUrl;
	}

	public String getCarIllegalUrl() {
		return carIllegalUrl;
	}

	
	
	

}
