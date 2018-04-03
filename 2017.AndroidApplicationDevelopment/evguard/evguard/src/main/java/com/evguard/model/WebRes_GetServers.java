package com.evguard.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.evguard.tools.LogEx;

public class WebRes_GetServers extends WebRes_Base {

	List<ServerInfo> servers=new ArrayList<ServerInfo>();
	
	@Override
	protected void parseData(JSONObject jsonobj) throws JSONException {
		servers.clear();
		JSONObject json=jsonobj.getJSONObject("Info");
		JSONArray serverinfo=json.getJSONArray("ServersUrl");
		for(int i=0;i<serverinfo.length();i++){
			JSONObject obj=serverinfo.getJSONObject(i);
			ServerInfo aServerInfo=new ServerInfo();
			aServerInfo.setAgentname(obj.getString("AgentName"));
			aServerInfo.setIconUrl(obj.getString("Icon"));
//			String iconurl=obj.getString("Icon");
//			String head=iconurl.substring(0, iconurl.indexOf(":")+1);
//			iconurl=head+"//"+iconurl.substring(iconurl.indexOf(":")+1,iconurl.length());
//			aServerInfo.setIconUrl(iconurl);
			aServerInfo.setServerUrl(obj.getString("Url"));
			servers.add(aServerInfo);
		}
	}
	public List<ServerInfo> getServers(){
		return this.servers;
	}

	

	
}
