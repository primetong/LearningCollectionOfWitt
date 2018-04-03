package com.evguard.main;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.sql.Timestamp;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.HttpHostConnectException;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.evguard.model.ICommonWebResponse;
import com.evguard.model.WebReq_Base;
import com.evguard.model.WebReq_Login;
import com.evguard.model.WebRes_Base;
import com.evguard.model.WebRes_Login;
import com.evguard.tools.ConstantTool;
import com.evguard.tools.LogEx;
import com.xinghaicom.evguard.R;
import com.xinghaicom.security.MD5;

public class WebRequestThreadEx<T extends WebRes_Base> implements Runnable {

	private String TAG = "WebRequestThreadEx";
	private WebReq_Base mWebReq_Base;
	private T mWebRes;
	private ICommonWebResponse<T> mICommonWebResponse;

	private WebReq_Login mWebReq_CheckAuthority = null;
	private WebRes_Login mWebRes_CheckAuthority = null;
	private App_Settings mSettings = null;
	private String ErrorTimeOut = "";
	private String WebResNUll = "";
	private String ReloginNull = "";
	private String ReloginFalied = "";
	private String WebException = "";
	private String WebSocketTimeout = "";
	private String WebUnknownError = "";

	private boolean bIsStop = false;
	private GpsWebClient mWebClient;

	public WebRequestThreadEx(WebReq_Base aWebReq_Base,
			ICommonWebResponse<T> aICommonWebResponse, T aWebRes) {
		this.mWebReq_Base = aWebReq_Base;
		this.mICommonWebResponse = aICommonWebResponse;
		this.mWebRes = aWebRes;
		mWebClient = aWebReq_Base.getWebClient();
//		mWebClient = new GpsWebClient(aWebReq_Base.getServiceUrl(),ConstantTool.WEBREQ_CONNECTTIME, ConstantTool.WEBREQ_TIMEOUT);
		mSettings = new App_Settings(App_Application.getInstance());
		ErrorTimeOut = App_Application.getInstance().getResources().getString(R.string.logintimeoutcode);
		WebResNUll = App_Application.getInstance().getResources().getString(R.string.webresnulll);
		ReloginNull = App_Application.getInstance().getResources().getString(R.string.reloginnull);
		ReloginFalied = App_Application.getInstance().getResources().getString(R.string.reloginfalied);
		WebException = App_Application.getInstance().getResources().getString(R.string.webexception);
		WebSocketTimeout = App_Application.getInstance().getResources().getString(R.string.websockettimeout);
		WebUnknownError = App_Application.getInstance().getResources().getString(R.string.webunknownerror);
		
	}

	public void run() {
		// TODO Auto-generated method stub
		JSONObject jsonObject = null;
		try {
			if (bIsStop)
				return;

			LogEx.i(TAG, "req:" + mWebReq_Base.getParams());
			jsonObject = mWebClient.executeByPostForJsonWithGZipAndEntrpyt(mWebReq_Base.getParams(), mWebReq_Base.getVersionCode(),
					mWebReq_Base.getUsername(), mWebReq_Base.getMethod());
			if (jsonObject == null) {
				LogEx.i(TAG, "res:" + jsonObject);
				if (bIsStop)
					return;
				mICommonWebResponse.WebRequsetFail(mWebRes.getMessage());
				return;
			}
			LogEx.i(TAG, "res:" + mWebRes.getClass().getSimpleName() + "==" + jsonObject);
			mWebRes.ParseJson(jsonObject);
			if (!mWebRes.getResult().equals("0")) {
				// if (!mWebRes.getIsSuccess()&&
				// mWebRes.getErrorCode().equals(ErrorTimeOut)) {
//				Dg_OptionSelect mDialog = new Dg_OptionSelect("登录提示");
				if (bIsStop)
					return;
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				String password = null;
				String username = mSettings.getUserName();
				try 
				{
					MD5 md5 = new MD5();
					password = md5.hashToDigestDes(mSettings.getLoginPwd());
				} catch (Exception e) {
					Log.e("ConstantTool", "MD5加密错误" + e.getMessage());
				}
				String password_encode = username + password + timestamp.toString();
				mWebReq_CheckAuthority = new WebReq_Login(username,password_encode,timestamp.toString());
				mWebRes_CheckAuthority = new WebRes_Login();
				jsonObject = mWebReq_CheckAuthority.getWebClient().executeByPostForJsonWithGZipAndEntrpyt(mWebReq_CheckAuthority.getParams(),
						mWebReq_CheckAuthority.getVersionCode(),mWebReq_CheckAuthority.getUsername(),mWebReq_CheckAuthority.getMethod());
				LogEx.i(TAG, "relogin res:"	+ mWebRes_CheckAuthority.getClass().getSimpleName() + "=="	+ jsonObject);
				if (jsonObject == null) {
					if (bIsStop)
						return;
					mICommonWebResponse.WebRequsetFail(mWebRes_CheckAuthority.getMessage());
					return;
				}
				mWebRes_CheckAuthority.ParseJson(jsonObject);
				if (!mWebRes_CheckAuthority.getResult().equals("0")) {
					if (bIsStop)
						return;
					mICommonWebResponse.WebRequsetFail(mWebRes_CheckAuthority.getMessage());
					return;
				} 
				
				mSettings.setVIN(mWebRes_CheckAuthority.getVIN());
				mSettings.setSecretKey(mWebRes_CheckAuthority.getSecretKey());
				
				LogEx.i(TAG, "again req:" + mWebReq_Base.getParams());
				jsonObject = mWebClient.executeByPostForJsonWithGZipAndEntrpyt(mWebReq_Base.getParams(), mWebReq_Base.getVersionCode(),
						mWebReq_Base.getUsername(), mWebReq_Base.getMethod());
				LogEx.i(TAG, "again res:" + mWebRes.getClass().getSimpleName() + "==" + jsonObject);
				mWebRes.ParseJson(jsonObject);
			}
			if (bIsStop)
				return;
			mICommonWebResponse.WebRequstSucess(mWebRes);
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			mICommonWebResponse.WebRequestException(WebSocketTimeout);

		} catch (HttpHostConnectException e) {
			e.printStackTrace();
			mICommonWebResponse.WebRequestException(WebException);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mICommonWebResponse.WebRequestException("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mICommonWebResponse.WebRequestException("");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mICommonWebResponse.WebRequestException("");
		} catch (Exception e) {
			String s = e.getMessage();
			s = (s.equals(null) || s.equals("")) ? WebUnknownError : s;
			mICommonWebResponse.WebRequestException(s);
		}
	}

	public void qiut() {
		bIsStop = true;
	}
}
