package com.evguard.main;

import android.content.Context;
import android.content.SharedPreferences;
import com.evguard.version.UpdatingMode;
import com.xinghaicom.web.AESHelper;

/**
 * 
 */
public class App_Settings {

	protected Context mContext = null;
	protected SharedPreferences mSettings = null;
	protected SharedPreferences.Editor mSettingsEditor = null;

	public App_Settings(Context context) {
		if (context == null)
			throw new NullPointerException("Context无效");

		try {
			mContext = context;
			mSettings = mContext.getSharedPreferences(mContext.getPackageName(),
					Context.MODE_PRIVATE);
			mSettingsEditor = mSettings.edit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getAppId()
	{
		
		String appid="0101";
		return appid;
	}
	public Boolean getIsUserQiutApp(){
		if (mSettings == null)
			throw new NullPointerException("SharedPreferences无效");
		return mSettings.getBoolean("IsUserQiutApp", true);
	}
	public void setIsUserQiutApp(boolean b){
		if (mSettingsEditor == null)
			throw new NullPointerException("SharedPreferences.Editor无效");
		
		mSettingsEditor.putBoolean("IsUserQiutApp", b);
		mSettingsEditor.commit();
	}
	
	public   String getServerSwitchIp(){
//		return "http://192.168.62.2:10323/App/";
		return "http://120.35.20.145:10323/App/";
	}
	public String getAgentName(){
		if (mSettings == null)
			throw new NullPointerException("SharedPreferences无效");
		String AgentName=mSettings.getString("AgentName", null);
		if(AgentName==null || AgentName.equals("") ) return AgentName;
		AgentName=AESHelper.method_DeEncrpt_AES_CBC_PKCS5Padding(AgentName);	
		return AgentName;
	}
	public void setAgentName(String AgentName ){
		if (mSettingsEditor == null)
			throw new NullPointerException("SharedPreferences.Editor无效");
		AgentName=AESHelper.method_AES_CBC_PKCS5Padding(AgentName);
		mSettingsEditor.putString("AgentName", AgentName);
		mSettingsEditor.commit();
	}
	public String getServerIP() {
		if (mSettings == null)
			throw new NullPointerException("SharedPreferences无效");
		
		String serverIP = mSettings.getString("ServerIP",null);
		if(serverIP==null||"".equals(serverIP))
		{
			
			serverIP="192.168.81.123:10385";
//			serverIP="120.35.20.145:10385";
//			serverIP="183.250.187.89:84";
//			serverIP="192.168.81.123:8888";
//			serverIP="192.168.2.62:9420";
			setServerIP(serverIP);
		}
		else
		{
			serverIP=AESHelper.method_DeEncrpt_AES_CBC_PKCS5Padding(serverIP);
		}
		return serverIP;
	}

	/**
	 * 
	 * 
	 * @param serverIp
	 *         
	 */
	public void setServerIP(String serverIp) {
		if (mSettingsEditor == null)
			throw new NullPointerException("SharedPreferences.Editor无效");
		serverIp=AESHelper.method_AES_CBC_PKCS5Padding(serverIp);
		mSettingsEditor.putString("ServerIP", serverIp);
		mSettingsEditor.commit();
	}
	/**
	 *登录密码
	 * 
	 * @param pwd
	 */
	public void setLoginPwd(String pwd) {
		if (mSettingsEditor == null)
			throw new NullPointerException("");
		
		pwd=AESHelper.method_AES_CBC_PKCS5Padding(pwd);
		mSettingsEditor.putString("LoginPwd", pwd);
		mSettingsEditor.commit();
	}

	/**
	 *登录密码
	 * 
	 * @return
	 */
	public String getLoginPwd() {
		if (mSettings == null)
			throw new NullPointerException("SharedPreferences无效");
		String pwd=mSettings.getString("LoginPwd", null);
		if(pwd==null || pwd.equals("") ) return pwd;
		
		pwd=AESHelper.method_DeEncrpt_AES_CBC_PKCS5Padding(pwd);
		
		return pwd;
	}

	public void setUserName(String name) {
		if (mSettingsEditor == null)
			throw new NullPointerException("SharedPreferences无效");
		name=AESHelper.method_AES_CBC_PKCS5Padding(name);
		mSettingsEditor.putString("UserName", name);
		mSettingsEditor.commit();
	}

	public String getUserName() {
		if (mSettings == null)
			throw new NullPointerException("SharedPreferences无效");
		String name=mSettings.getString("UserName", null);
		name=AESHelper.method_DeEncrpt_AES_CBC_PKCS5Padding(name);
		return name;
	}
	
	

	/**
	 * 
	 * 
	 * @param isFirstUse
	 *          
	 */
	public void setIsFirstUse(boolean isFirstUse) {
		if (mSettingsEditor == null)
			throw new NullPointerException("");
		mSettingsEditor.putBoolean("IsFirstUse", isFirstUse);
		mSettingsEditor.commit();
	}
	
	public boolean getIsFirstUse(){
		if (mSettings == null)
			throw new NullPointerException("SharedPreferences无效");
		return mSettings.getBoolean("IsFirstUse", true);
	}
	/**
	 * 
	 * 
	 * @return
	 */
	public String getJPushRegisterId() {
		if (mSettings == null)
			throw new NullPointerException("SharedPreferences无效");
		String name=mSettings.getString("JPushRegisterId", null);
		name=AESHelper.method_DeEncrpt_AES_CBC_PKCS5Padding(name);
		return name;
	}
	
	/**
	 * 
	 */
	public void setJPushRegisterId(String registerid)
	{
		if (mSettingsEditor == null)
			throw new NullPointerException("SharedPreferences无效");
		registerid=AESHelper.method_AES_CBC_PKCS5Padding(registerid);
		mSettingsEditor.putString("JPushRegisterId", registerid);
		mSettingsEditor.commit();
	}
	/**
	 *
	 */
	public void setIsMessageSound(boolean isSound)
	{
		if (mSettingsEditor == null)
			throw new NullPointerException("SharedPreferences无效");
		mSettingsEditor.putBoolean("IsMessageSound", isSound);
		mSettingsEditor.commit();
	}
	/**
	 *
	 * @return
	 */
	public boolean getIsMessageSound()
	{
		if(mSettings==null)
			throw new NullPointerException("SharedPreferences无效");
		return mSettings.getBoolean("IsMessageSound", false);
	}
	/**
	 *
	 */
	public void setIsMessageVerb(boolean isVerb)
	{
		if (mSettingsEditor == null)
			throw new NullPointerException("SharedPreferences无效");
		mSettingsEditor.putBoolean("IsMessageVerb", isVerb);
		mSettingsEditor.commit();
	}
	/**
	 *
	 * @return
	 */
	public boolean getIsMessageVerb()
	{
		if(mSettings==null)
			throw new NullPointerException("SharedPreferences无效");
		return mSettings.getBoolean("IsMessageVerb", false);
	}
	
	public UpdatingMode getUpdatingMode() {
		if (mSettings == null)
			throw new NullPointerException("SharedPreferences无效");
		int updatingMode = mSettings.getInt("UpdatingMode",
				UpdatingMode.HAS_NOT_NEW.ordinal());
		return UpdatingMode.values()[updatingMode];
	}

	public void setUpdatingMode(UpdatingMode hasUnnecessaryNew) {
		if (mSettingsEditor == null)
			throw new NullPointerException("SharedPreferences无效");
		mSettingsEditor.putInt("UpdatingMode", hasUnnecessaryNew.ordinal());
		mSettingsEditor.commit();
	}
	
	
	public String getPlaceCode(){
		if (mSettings == null)
			throw new NullPointerException("SharedPreferences无效");
		String place=mSettings.getString("PlaceCode", null);
		place=AESHelper.method_DeEncrpt_AES_CBC_PKCS5Padding(place);
		return place;
	}
	
	public void setPlaceCode(String placeCode){
		if (mSettingsEditor == null)
			throw new NullPointerException("SharedPreferences.Editor��Ч");		
		placeCode=AESHelper.method_AES_CBC_PKCS5Padding(placeCode);
		mSettingsEditor.putString("PlaceCode", placeCode);
		mSettingsEditor.commit();
	}
	
	public String getPlaceName(){
		if (mSettings == null)
			throw new NullPointerException("SharedPreferences无效");
		String placename=mSettings.getString("PlaceName", null);
		placename=AESHelper.method_DeEncrpt_AES_CBC_PKCS5Padding(placename);
		return placename;
	}
	
	public void setPlaceName(String placeName){
		if (mSettingsEditor == null)
			throw new NullPointerException("SharedPreferences无效");
		placeName=AESHelper.method_AES_CBC_PKCS5Padding(placeName);
		mSettingsEditor.putString("PlaceName", placeName);
		mSettingsEditor.commit();
	}
		
	public double getStartupLongitude(){
		if (mSettings == null)
			throw new NullPointerException("SharedPreferences无效");
		return mSettings.getFloat("StartupLongitude", Float.NaN);
	}
	public void setStartupLongitude(double startupLongitude){
		if (mSettingsEditor == null)
			throw new NullPointerException("SharedPreferences无效");
		mSettingsEditor.putFloat("StartupLongitude", (float)startupLongitude);
		mSettingsEditor.commit();
	}
	
	public double getStartupLatitude(){
		if (mSettings == null)
			throw new NullPointerException("SharedPreferences无效");
		return mSettings.getFloat("StartupLatitude", Float.NaN);
	}
	public void setStartupLatitude(double startupLatitude){
		if (mSettingsEditor == null)
			throw new NullPointerException("SharedPreferences无效");
		mSettingsEditor.putFloat("StartupLatitude", (float)startupLatitude);
		mSettingsEditor.commit();
	}
	public void setIsHasNewAlarm(boolean b){
		if (mSettingsEditor == null)
			throw new NullPointerException("SharedPreferences无效");
		mSettingsEditor.putBoolean("IsHasNewAlarm", b);
		mSettingsEditor.commit();
	}
	public boolean getIsHasNewAlarm(){
		if (mSettings == null)
			throw new NullPointerException("SharedPreferences无效");
		return mSettings.getBoolean("IsHasNewAlarm", Boolean.FALSE);
	}
//	public void setIsEncrypt(boolean b)
//	{
//		if (mSettingsEditor == null)
//			throw new NullPointerException("");
//		mSettingsEditor.putBoolean("IsEncrypt", b);
//		mSettingsEditor.commit();
//	}
//	public boolean getIsEncrypt()
//	{
//		if (mSettings == null)
//			throw new NullPointerException("SharedPreferences��Ч");
//		return mSettings.getBoolean("IsEncrypt", false);
//	}
	
	
//	public String getOldVersion(){
//		if (mSettings == null)
//			throw new NullPointerException("SharedPreferences��Ч");
//		return mSettings.getString("OldVersion", "");
//	}
//	public void setOldVersion(String oldVersion){
//		if (mSettingsEditor == null)
//			throw new NullPointerException("");
//		mSettingsEditor.putString("OldVersion", oldVersion);
//		mSettingsEditor.commit();
//	}
//	public int getOldeVersionCode(){
//		if (mSettings == null)
//			throw new NullPointerException("SharedPreferences��Ч");
//		return mSettings.getInt("OldVersionCode", 0);
//	}
//	/**
//	 * �ɰ汾��
//	 * @param time
//	 */
//	public void setOldeVersionCode(int versionCode){
//		if (mSettingsEditor == null)
//			throw new NullPointerException("");
//		mSettingsEditor.putInt("OldVersionCode", versionCode);
//		mSettingsEditor.commit();
//	}
//	

	
	public String getVIN() {
		if (mSettingsEditor == null)
			throw new NullPointerException("");
		return mSettings.getString("VIN", null);
	}

	public void setVIN(String VIN) {
		if (mSettingsEditor == null)
			throw new NullPointerException("");
		mSettingsEditor.putString("VIN", VIN);
		mSettingsEditor.commit();
	}
	
	public String getSecretKey(){
		if (mSettingsEditor == null)
			throw new NullPointerException("");
		return mSettings.getString("SecretKey", null);
	}
	
	public void setSecretKey(String key){
		if (mSettingsEditor == null)
			throw new NullPointerException("");
		mSettingsEditor.putString("SecretKey", key);
		mSettingsEditor.commit();
	}
	
	
	public String getAboutUrl(){
		if (mSettingsEditor == null)
			throw new NullPointerException("");
		return mSettings.getString("AboutUrl", null);
	}
	public void setAboutUrl(String url){
		if (mSettingsEditor == null)
			throw new NullPointerException("");
		mSettingsEditor.putString("AboutUrl", url);
		mSettingsEditor.commit();
	}
	
	public String getCarIllegalUrl(){
		if (mSettingsEditor == null)
			throw new NullPointerException("");
		return mSettings.getString("CarIllegalUrl", null);
	}
	public void setCarIllegalUrl(String url){
		if (mSettingsEditor == null)
			throw new NullPointerException("");
		mSettingsEditor.putString("CarIllegalUrl", url);
		mSettingsEditor.commit();
	}
}
