package com.evguard.main;

import java.util.List;
import org.apache.http.NameValuePair;
import org.json.JSONObject;

import com.evguard.data.AppDataCache;
import com.xinghaicom.web.WebClient;

public class GpsWebClient extends WebClient {

	boolean bIsEnctry = true;

	public GpsWebClient(String initServiceMethodURI, int connectionTimeout,
			int soTimeout) {
		super(initServiceMethodURI, connectionTimeout, soTimeout);
		// TODO Auto-generated constructor stub
	}

	public GpsWebClient(String initServiceMethodURI) {
		super(initServiceMethodURI);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JSONObject executeByPostForJsonWithGZipAndEntrpyt(List<NameValuePair> params, int version, String username, String method)
			throws Exception {

		AppDataCache.getInstance().setLastReqTime(System.currentTimeMillis());
		if (bIsEnctry) {
				return super.executeByPostForJsonWithGZipAndEntrpyt(params,
						version, username,method);
		} else {
			return super
					.executeByPostForJsonWithGZip(params);
		}
	
	}
}
