package com.evguard.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.evguard.tools.LogEx;

public class WebRes_GetCarMedical extends WebRes_Base {
	private String analysisResult = "";
	private String diagnoseResult = "";
	private String score = "";
	private String time = "";
	private String checkItemNum = "";
	
	@Override
	protected void parseData(JSONObject jsonobj) throws JSONException {
		JSONObject json=jsonobj.getJSONObject("Info");
		this.analysisResult = json.getString("AnalysisResult");
		this.diagnoseResult = json.getString("Result");
		this.score = json.getString("Score");
		this.time = json.getString("Time");
		this.checkItemNum = json.getString("CheckItemNum");
	}
	public String getDiagnoseResult() {
		return diagnoseResult;
	}
	public void setDiagnoseResult(String diagnoseResult) {
		this.diagnoseResult = diagnoseResult;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getCheckItemNum() {
		return checkItemNum;
	}
	public void setCheckItemNum(String checkItemNum) {
		this.checkItemNum = checkItemNum;
	}
	public String getAnalysisResult() {
		return analysisResult;
	}

	public void setAnalysisResult(String result) {
		this.analysisResult = result;
	}
}
