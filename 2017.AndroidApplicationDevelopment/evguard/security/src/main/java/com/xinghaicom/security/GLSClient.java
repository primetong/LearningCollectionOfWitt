package com.xinghaicom.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParserException;

import com.xinghaicom.web.WebClient;

public class GLSClient {
	
	final protected String GETEMETHODNAME = "getE";	
	protected String mServiceURI = "";
	protected WebClient mGetEClient = null;
	protected int mConnectionTimeout = 0;
	protected int mSoTimeout = 0;
	
	public GLSClient(String initServiceURI,int connectionTimeout,int soTimeout){		
		mServiceURI = initServiceURI;
		mConnectionTimeout = connectionTimeout;
		mSoTimeout = soTimeout;
	}
	
	public void resetServiceURI(String serviceURI){
		mServiceURI = serviceURI;
		mGetEClient = null;
	}
	
	public EInfo getEInfo(String sComputerId, boolean hasDog, String sSystemId, String sIp) throws Exception{
		if(mGetEClient == null){
			mGetEClient = new WebClient(mServiceURI + "/" + GETEMETHODNAME,mConnectionTimeout,mSoTimeout);
		}
		
		List params = new ArrayList();
		//String refErrMsg = null;
		//String refEmpowerCd = null;
		params.add(new BasicNameValuePair("sComputerId",sComputerId));
		params.add(new BasicNameValuePair("sDogSg",hasDog?"1":"0"));
		params.add(new BasicNameValuePair("sSystemId",sSystemId));
		params.add(new BasicNameValuePair("sIp",sIp));
		//params.add(new BasicNameValuePair("sErrMsg",refErrMsg));
		//params.add(new BasicNameValuePair("sEmpowerCd",refEmpowerCd));	
				
//		BasicNameValuePair d = new BasicNameValuePair()
//		d.
				
		List<String> results = mGetEClient.Execute(params);
		if(results == null || results.size() <= 0) return null;
				
		String eInfoDesColStr = results.get(0);
		if(eInfoDesColStr == null || eInfoDesColStr.length() <= 0) return null;
		
		String[] eInfoDesCol = eInfoDesColStr.split("::::");
		if(eInfoDesCol == null || eInfoDesCol.length < 3) throw new Exception("获取授权无效，授权信息不全！"); 

		int e = Integer.parseInt(eInfoDesCol[0]);						
		String errMsg = eInfoDesCol[1];
		String empowerCd = eInfoDesCol[2];
						
		return new EInfo(e, errMsg, empowerCd);
	}
		
	public void release(){
		if(mGetEClient != null) mGetEClient.release();		
	}
	
	public class EInfo{		
		protected int mE = 0;
		protected String mErrMsg = null;
		protected String mEmpowerCd = null;
		
		public EInfo(int e,String errMsg,String empowerCd){
			mE = e;
			mErrMsg = errMsg;
			mEmpowerCd = empowerCd;
		}
		
		public int getE(){
			return mE;
		}
		
		public String getErrMsg(){
			return mErrMsg;
		}
		
		public String getEmpowerCd(){
			return mEmpowerCd;
		}		
	}
}
