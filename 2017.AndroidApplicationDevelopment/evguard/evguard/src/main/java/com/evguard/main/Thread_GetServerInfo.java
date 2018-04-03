package com.evguard.main;

import java.util.List;

import com.evguard.model.ICommonWebResponse;
import com.evguard.model.ServerInfo;
import com.evguard.model.WebReq_GetServers;
import com.evguard.model.WebRes_GetServers;

public class Thread_GetServerInfo {
	CallBack_GetServerInfo mCallBack_GetServerInfo=null;
	public Thread_GetServerInfo(CallBack_GetServerInfo aCallBack_GetServerInfo){
		mCallBack_GetServerInfo=aCallBack_GetServerInfo;
	}
	public  void getServerInfo(){
		WebReq_GetServers aWebReq_GetServers = new WebReq_GetServers();
//		
		ICommonWebResponse<WebRes_GetServers> aICommonWebResponse = new ICommonWebResponse<WebRes_GetServers>() {
			@Override
			public void WebRequstSucess(WebRes_GetServers aWebRes) {
				if (aWebRes.getResult().equals("0")) {		
					
					mCallBack_GetServerInfo.getServerInfosOk(aWebRes.getServers());
				} else {
					mCallBack_GetServerInfo.getServerInfosFailed(aWebRes.getMessage());
				}
			}

			@Override
			public void WebRequestException(String ex) {
				mCallBack_GetServerInfo.getServerInfosFailed(ex);
			}

			@Override
			public void WebRequsetFail(String sfalied) {
				mCallBack_GetServerInfo.getServerInfosFailed(sfalied);
			}
		};
		WebRequestThreadEx<WebRes_GetServers> aWebRequestThreadEx = new WebRequestThreadEx<WebRes_GetServers>(
				aWebReq_GetServers, aICommonWebResponse,
				new WebRes_GetServers());
		new Thread(aWebRequestThreadEx).start();

	}
	public interface CallBack_GetServerInfo{
		public void getServerInfosOk(List<ServerInfo> alist);
		public void getServerInfosFailed(String serror);
	}
}
