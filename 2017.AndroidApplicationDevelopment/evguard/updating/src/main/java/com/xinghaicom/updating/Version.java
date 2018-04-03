package com.xinghaicom.updating;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.entity.StringEntity;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class Version implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4276078869364270830L;
	protected String mVersionURLPath = null;
	protected String mVersionSiteURLPath = null;		
	protected String mApplicationName = null;
	protected String mApplicationDirName = null;
	protected String mApkName = null;
	protected String mVersionName = null;
	protected int mVersionCode = 0;
	protected boolean mNecessary = false;
	
	public Version(String versionUrlPath) throws Exception {
		if(versionUrlPath == null || versionUrlPath.length() <= 0) throw new IllegalArgumentException("版本URL信息无效！");
		mVersionURLPath = versionUrlPath;
		int countofSiteURL = mVersionURLPath.lastIndexOf("/");
		if(countofSiteURL >= 0) mVersionSiteURLPath = mVersionURLPath.substring(0,countofSiteURL);				
		URL versionURL = new URL(mVersionURLPath);
		URLConnection versionURLConnection = versionURL.openConnection();
		if(versionURLConnection == null) throw new NullPointerException("获取最新版本失败：版本URL无法访问！");
			
		versionURLConnection.setConnectTimeout(10000);
		versionURLConnection.setReadTimeout(10000);
		
		InputStream versionInputStream = versionURLConnection.getInputStream();	
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		if(factory == null) throw new NullPointerException("获取最新版本失败：解析版本Xml的XmlPullParserFactory无效！");				
		factory.setNamespaceAware(true);								
		XmlPullParser parser = factory.newPullParser();
		if(parser == null) throw new NullPointerException("获取最新版本失败：解析版本Xml的XmlPullParser无效！");
		parser.setInput(versionInputStream,"utf-8");
		
		int eventType = parser.getEventType();			
		while (eventType != XmlPullParser.END_DOCUMENT) {
			
			switch(eventType){
			
			case XmlPullParser.START_TAG:
				String name = parser.getName();
				if(name == null) break;
				
				if(name.equalsIgnoreCase("ApplicationName"))
					mApplicationName = parser.nextText().trim();
				else if(name.equalsIgnoreCase("ApplicationDirName"))
					mApplicationDirName = parser.nextText().trim();
				else if(name.equalsIgnoreCase("ApkName"))
					mApkName = parser.nextText().trim();
				else if(name.equalsIgnoreCase("VersionName"))
					mVersionName = parser.nextText().trim();
				else if(name.equalsIgnoreCase("VersionCode"))
					mVersionCode = Integer.parseInt(parser.nextText().trim());
				else if(name.equalsIgnoreCase("Necessary"))
					mNecessary = Boolean.parseBoolean(parser.nextText().trim());								
				break;
				
			}
			
			try{
				eventType = parser.next();
			}catch(Exception e){				
				String message = "";
				String stackTrace = "";
//				if(e != null){
//					message = e.getMessage();
//					stackTrace = e.getStackTrace().toString();
//				}
				Log.e("Version", "读取URL上的XML失败，重试！" + message + "调用堆栈：" + stackTrace);				
			}
		}
	}
	
	public String getVersionSiteURLPath(){
		return mVersionSiteURLPath;
	}

	public String getApplicationName() {
		return mApplicationName;
	}
	
	public String getApplicationDirName(){
		return mApplicationDirName;
	}
	
	public String getApkName() {
		return mApkName;
	}
		
	public String getVersionName() {
		return mVersionName;
	}
	
	public int getVersionCode() {
		return mVersionCode;
	}
	
	public boolean isNecessary(){
		return mNecessary;
	}
		
}
