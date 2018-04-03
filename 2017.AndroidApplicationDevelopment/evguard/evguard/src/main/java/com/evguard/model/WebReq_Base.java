package com.evguard.model;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import com.evguard.main.App_Application;
import com.evguard.main.App_Settings;
import com.evguard.main.GpsWebClient;
import com.evguard.tools.CommUtils;
import com.evguard.tools.ConstantTool;
import com.evguard.tools.LogEx;
import com.xinghaicom.security.MD5;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * web请求基类
 * 
 * @author wulh 2015-04-15
 */
public abstract class WebReq_Base {

	protected App_Settings mSettings = null;
	protected String Method;
	protected String RegisterId;
	protected String VIN;
	protected String SecretKey;
	protected String username;
	protected String password;
	protected List<NameValuePair> mParams = null;

	protected String Imei;
	protected String Version;
	protected String OSType;
	protected int VersionCode;
	protected String ServiceUrl;
	protected String HeadUrl;
	protected final int OS_TYPE_ANDROID = 1;
	private String mPackageName = "";
	private GpsWebClient mWebClient = null;

	public WebReq_Base() {

		Context mContext = App_Application.getInstance();
		mPackageName = mContext.getPackageName();
		mSettings = new App_Settings(mContext);

		this.Version = getVersion(mContext);
		this.Imei = getPhoneImei(mContext);
		this.VersionCode = getVersionCode(mContext);
		this.OSType = String.valueOf(OS_TYPE_ANDROID);
//		RegisterId = mSettings.getJPushRegisterId();
//		if (CommUtils.isEmpty(RegisterId)) {
//			RegisterId = JPushInterface.getRegistrationID(mContext);
//			mSettings.setJPushRegisterId(RegisterId);
//		}
		String ip = mSettings.getServerIP();
		this.username = mSettings.getUserName();
		this.password = mSettings.getLoginPwd();
		this.SecretKey = mSettings.getSecretKey();
		
		this.HeadUrl = mSettings.getServerIP();//"http://" + ip + "/App2/";
		
	}

	
	private String getVersion(Context mContext) {
		String version = "";
		try {
			version = mContext.getPackageManager().getPackageInfo(mPackageName,
					0).versionName;

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			String msg = e.getMessage();
			if (msg == null)
				msg = "无法获取版本信息";
			LogEx.e("ConstantTool", msg);
		}
		return version;
	}

	private int getVersionCode(Context mContext) {
		int mVersionCode = 0;
		try {

			mVersionCode = mContext.getPackageManager().getPackageInfo(
					mPackageName, 0).versionCode;
		} catch (Exception e) {
			String msg = e.getMessage();
			if (msg == null)
				msg = "无法获取版本信息";
			LogEx.e("ConstantTool", msg);
		}
		return mVersionCode;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	private String getPhoneImei(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String subscriberId = tm.getSubscriberId(); // For GSM
		String imei = tm.getDeviceId(); // For GSM
		int phoneType = tm.getPhoneType(); // If this Phone is gsm or cdma or
											// sip
		String phoneNumber = tm.getSimSerialNumber(); // Phone Number
		return imei;
	}

	public String getServiceUrl() {
		return this.ServiceUrl;
	}

	public String getVIN() {
		this.VIN = mSettings.getVIN();
		return this.VIN;
	}
	public String getSecretKey() {
		this.SecretKey = mSettings.getSecretKey();
		return this.SecretKey;
	}
	
	public String getPassword(){
		String password = mSettings.getLoginPwd();
		MD5 md5 = new MD5();
		try {
			password = md5.hashToDigestDes(password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return password;
	}

	public List<NameValuePair> getParams() {
		mParams = new ArrayList<NameValuePair>();
		return getParamsData(mParams);
	}

	public abstract List<NameValuePair> getParamsData(List<NameValuePair> params);

	public int getVersionCode() {
		return this.VersionCode;
	}

	public String getVersion() {
		return this.Version;
	}

	public String getMethod() {
		return this.Method;
	}

	public GpsWebClient getWebClient() {
		mWebClient = new GpsWebClient(ServiceUrl, ConstantTool.WEBREQ_CONNECTTIME, ConstantTool.WEBREQ_TIMEOUT);
		Log.i("llj", "ServiceUrl--" + ServiceUrl);
		return mWebClient;
	}

	public GpsWebClient getWebClient(int iconnecttime, int timeout) {
		mWebClient = new GpsWebClient(ServiceUrl, iconnecttime, timeout);
		return mWebClient;
	}

	public String getUsername() {
		return 	this.username;
	}
	
	public void setMethod(String method){
		this.Method = method;
	}
	
	public void setServiceUrl(String method){
		setMethod(method);
//		if(method.equals("Login") || method.equals("GetVerifyCode") || method.equals("GetPwd")){
//			this.ServiceUrl = this.HeadUrl + this.Method + "?v=" + getVersionCode();
//			return;
//		}
		MD5 md5 = new MD5();
		String sdata = CommUtils.paramsToStr(getParams()) + getSecretKey()+ getPassword();
		Log.i("llj", "sdata>>>>" + sdata);
		String sign = "";
		try {
			sign = md5.hashToDigestDes(sdata);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.ServiceUrl = this.HeadUrl + this.Method + "?v=" + getVersionCode() +"&UserName=" + getUsername() +"&sign="+sign;
		Log.i("llj", "ServiceUrl>>>>" + ServiceUrl);
	}
	
}
