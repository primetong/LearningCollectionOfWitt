package com.evguard.version;

import java.io.Serializable;


public class AppVersion implements Serializable {

	private static final long serialVersionUID = 940230049450975852L;
//	public String mCmdError=null;
	private String mNewVersion=null;
	private String mUpdateInfo=null;
	private String mMessage=null;
	private boolean mIsMustUpdate=false;
	private String mUrl=null;
	private String mApkName="SmartBracelet";
	public AppVersion(){
		
	}
	public void setNewVersion(String s){
		this.mNewVersion=s;
	}
	public String getNewVersion(){
		return this.mNewVersion;
	}
	public void setUpdateInfo(String s){
		this.mUpdateInfo=s;
	}
	public String getUpdateInfo(){
		return this.mUpdateInfo;
	}
	public void setMessage(String s){
		this.mMessage=s;
	}
	public String getMessage(){
		return this.mMessage;
	}
	public void setUrl(String s){
		this.mUrl=s;
	}
	public String getUrl(){
		return this.mUrl;
	}
	public void setIsMustUpdate(boolean b){
		this.mIsMustUpdate=b;
	}
	public boolean getIsMustUpdate(){
		return this.mIsMustUpdate;
	}
	public String getApkName(){
		return this.mApkName;
	}
	
//	public AppVersion(String serviceUrl,List<NameValuePair> checkUpdateParams){
//		WebClient webClient=new WebClient(serviceUrl);
//		JSONObject jsonObject=null;
//		try {
////			String textStr="{CmdError: null,ErrorCode:null,isSuccess: true,NewVersion: \"1.2.0\",UpdateInfo:\"�޸������˵�bug\",Message:\"��Ϣ\",IsMustUpdate:true,Url:\"www.fjcwt.cn\"}";
//			jsonObject=webClient.executeByPostForJsonWithGZipAndEntrpyt(checkUpdateParams);
//			Log.i("wlh","version:"+jsonObject);
//			if(jsonObject!=null){
//				if(jsonObject.getBoolean("isSuccess")){
//					mNewVersion=jsonObject.getString("NewVersion");
//					mUpdateInfo=jsonObject.getString("UpdateInfo");
//					mMessage=jsonObject.getString("Message");
//					mIsMustUpdate=jsonObject.getBoolean("IsMustUpdate");
//					mUrl=jsonObject.getString("AndroidUrl");
//				}else{
//					mCmdError=jsonObject.getString("CmdError");
//				}
//			}
//		} 
//		catch (ClientProtocolException e) {
//		} catch (IOException e) {
//		
//		} 
//		catch (JSONException e) {
//		}
//	}
}
