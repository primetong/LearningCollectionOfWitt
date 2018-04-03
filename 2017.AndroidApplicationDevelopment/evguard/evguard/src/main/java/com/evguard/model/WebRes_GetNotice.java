package com.evguard.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.evguard.data.AppDataCache;

public class WebRes_GetNotice extends WebRes_Base {
	
	private String ServerTime = "";
	private List<MessageInfo> mMessageList;
	

	@Override
	protected void parseData(JSONObject jsonobj) throws JSONException {
		JSONObject json=jsonobj.getJSONObject("Info");
		this.ServerTime = json.getString("ServerTime");
		JSONArray items = json.getJSONArray("Items");
		if(mMessageList == null) 
			mMessageList = new ArrayList<MessageInfo>();
		MessageInfo mMessageInfo;
		int curID = AppDataCache.getInstance().getMessageId();
		System.out.println("curID==" + curID);
		for (int i = 0; i < items.length(); i++) {
			JSONObject item = items.getJSONObject(i);
			mMessageInfo = new MessageInfo();
			System.out.println(i + "--curID==" + curID);
			mMessageInfo.setID(curID++);
			mMessageInfo.setMsgType(item.getString("MsgType"));
			mMessageInfo.setTitle(item.getString("Title"));
			mMessageInfo.setContent(item.getString("Content"));
			mMessageInfo.setTime(item.getString("Time"));
			mMessageInfo.setIsMsgReaded("0");
			
			mMessageList.add(mMessageInfo);
		}
		System.out.println("after--curID==" + curID);
		AppDataCache.getInstance().setMessageId(curID);
	}


	public String getServerTime() {
		return ServerTime;
	}
	
	public List<MessageInfo> getMessageList() {
		return mMessageList;
	}
}
