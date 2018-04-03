package com.evguard.model;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class WebReq_SetServiceTelephone extends WebReq_Base {

	private String insuranceTelephone;//保险电话
	private String alarmTelephone;//报警电话
	private String drivingTelephone;//代驾电话
	private String serviceTelephone;//4S服务店电话
	private String rescueTelephone;//救援电话
	
	public WebReq_SetServiceTelephone(String insuranceTelephone,String alarmTelephone,String drivingTelephone,
			String serviceTelephone,String rescueTelephone){
		this.insuranceTelephone = insuranceTelephone;
		this.alarmTelephone = alarmTelephone;
		this.drivingTelephone = drivingTelephone;
		this.serviceTelephone = serviceTelephone;
		this.rescueTelephone = rescueTelephone;
		super.setServiceUrl("SetServiceTelephone");
	}

	@Override
	public List<NameValuePair> getParamsData(List<NameValuePair> params) {
		params.add(new BasicNameValuePair("VIN", this.getVIN()));
		params.add(new BasicNameValuePair("InsuranceTelephone", this.insuranceTelephone));
		params.add(new BasicNameValuePair("AlarmTelephone", this.alarmTelephone));
		params.add(new BasicNameValuePair("DrivingTelephone", this.drivingTelephone));
		params.add(new BasicNameValuePair("ServiceTelephone", this.serviceTelephone));
		params.add(new BasicNameValuePair("RescueTelephone", this.rescueTelephone));
		params.add(new BasicNameValuePair("SecretKey", this.getSecretKey()));
		params.add(new BasicNameValuePair("Password", this.password));
		return params;
	}

	public void setInsuranceTelephone(String insuranceTelephone) {
		this.insuranceTelephone = insuranceTelephone;
	}

	public void setAlarmTelephone(String alarmTelephone) {
		this.alarmTelephone = alarmTelephone;
	}

	public void setDrivingTelephone(String drivingTelephone) {
		this.drivingTelephone = drivingTelephone;
	}

	public void setServiceTelephone(String serviceTelephone) {
		this.serviceTelephone = serviceTelephone;
	}

	public void setRescueTelephone(String rescueTelephone) {
		this.rescueTelephone = rescueTelephone;
	}
	
	

}
