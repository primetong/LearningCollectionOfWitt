package com.evguard.model;

import org.json.JSONException;
import org.json.JSONObject;

public class WebRes_CheckVersion extends WebRes_Base {

	private String NewVersion="";
	private String FileUrl="";
	private String IsNeedUpdate="";
	private String UpdateInfo="";

	@Override
	protected void parseData(JSONObject jsonobj) throws JSONException {
		JSONObject json=jsonobj.getJSONObject("Info");
		this.setIsNeedUpdate(json.getString("IsNeedUpdate"));
		this.FileUrl=json.getString("FileUrl");
		this.NewVersion=json.getString("NewVersion");
		this.UpdateInfo=json.getString("UpdateInfo");
	}

	/**
	 * @return the newVersion
	 */
	public String getNewVersion() {
		return NewVersion;
	}

	/**
	 * @param newVersion the newVersion to set
	 */
	public void setNewVersion(String newVersion) {
		NewVersion = newVersion;
	}

	/**
	 * @return the fileUrl
	 */
	public String getFileUrl() {
		return FileUrl;
	}

	/**
	 * @param fileUrl the fileUrl to set
	 */
	public void setFileUrl(String fileUrl) {
		FileUrl = fileUrl;
	}


	/**
	 * @return the updateInfo
	 */
	public String getUpdateInfo() {
		return UpdateInfo;
	}

	/**
	 * @param updateInfo the updateInfo to set
	 */
	public void setUpdateInfo(String updateInfo) {
		UpdateInfo = updateInfo;
	}

	public String getIsNeedUpdate() {
		return IsNeedUpdate;
	}

	public void setIsNeedUpdate(String isNeedUpdate) {
		IsNeedUpdate = isNeedUpdate;
	}
	
}
