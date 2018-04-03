package com.evguard.model;

public class AlamSettings {

	private String Param="";
	private String Value="";
	
	public AlamSettings(){}
	
	public AlamSettings(String param, String value) {
		super();
		Param = param;
		Value = value;
	}
	/**
	 * @return the param
	 */
	public String getParam() {
		return Param;
	}
	/**
	 * @param param the param to set
	 */
	public void setParam(String param) {
		Param = param;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return Value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		Value = value;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "{\"Param\":\"" + this.Param + "\", \"Value\":\"" + this.Value + "\"}";
	}
	
	
	
}
