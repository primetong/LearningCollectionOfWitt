package com.evguard.model;

public class ServerInfo {

	private String url="";
	private String agentname="";
	private String icon="";
	
	public void setServerUrl(String s){
		this.url=s;
	}
	public String getServerUrl(){
		return this.url;
	}
	public void setAgentname(String s){
		this.agentname=s;
	}
	public String getAgentname(){
		return this.agentname;
	}
	public void setIconUrl(String s){
		this.icon=s;
	}
	public String getIconUrl(){
		return this.icon;
	}
	
}
