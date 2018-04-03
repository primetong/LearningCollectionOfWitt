package com.evguard.main;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.sql.Timestamp;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.HttpHostConnectException;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.evguard.model.WebReq_Base;
import com.evguard.model.WebReq_Login;
import com.evguard.model.WebRes_Base;
import com.evguard.model.WebRes_Login;
import com.evguard.tools.LogEx;
import com.xinghaicom.asynchrony.LoopHandler;
import com.xinghaicom.asynchrony.Looping;
import com.xinghaicom.evguard.R;
import com.xinghaicom.security.MD5;

public class Thread_Base extends Looping {
	private String TAG=this.getClass().getSimpleName();
	protected Context mContext = null;
	protected GpsWebClient mWebClient = null;
	protected App_Settings mSettings = null;

	private String ErrorTimeOut="";
	private String WebResNUll="";
	private String ReloginNull="";
	private String ReloginFalied="";
	private String WebException="";
	private String WebSocketTimeout="";
	private String WebUnknownError="";
	

	public Thread_Base(Context context) {
		super();
		mContext = context;
		init();
	}

	public Thread_Base(Context context, LoopHandler loopHandler) {
		super(loopHandler);
		if (context == null)
			throw new NullPointerException("Context无效");
		mContext = context;
		init();
	}

	private void init() {
		mSettings = new App_Settings(mContext);
		ErrorTimeOut=mContext.getString(R.string.logintimeoutcode);
		WebResNUll=mContext.getString(R.string.webresnulll);
		ReloginNull=mContext.getString(R.string.reloginnull);
		ReloginFalied=mContext.getString(R.string.reloginfalied);
		WebException=mContext.getResources().getString(R.string.webresnulll);
		WebSocketTimeout=mContext.getResources().getString(R.string.websockettimeout);
		WebUnknownError=mContext.getResources().getString(R.string.webunknownerror);
	}

	@Override
	protected void onRunning(int action, Object obj) {

	}

	@Override
	public void quit() {
		super.quit();
		mWebClient = null;
	}

	protected GpsWebClient getWebClient(WebReq_Base abase) {
			mWebClient = abase.getWebClient();
		return mWebClient;
	}

	protected void executeWithConfirmLogon(WebReq_Base abase,
			WebRes_Base aWebRes_Base) throws Exception {
		try {
			mWebClient = getWebClient(abase);
			JSONObject result = mWebClient
					.executeByPostForJsonWithGZipAndEntrpyt(abase.getParams(),abase.getVersionCode(),abase.getUsername(),abase.getMethod());
			LogEx.i(TAG, "webres:"+result);
			if (result == null) {
				throw new Exception(WebResNUll);
			}
			aWebRes_Base.ParseJson(result);
			if (aWebRes_Base.getResult().equals("0"))
				return;

			if (!aWebRes_Base.getResult().equals("0")) {//&& aWebRes_Base.getErrorCode().equals(ErrorTimeOut))
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
				WebReq_Login aWebReq_CheckAuthority = new WebReq_Login(username,password_encode,timestamp.toString());
				result = aWebReq_CheckAuthority.getWebClient()
						.executeByPostForJsonWithGZipAndEntrpyt(aWebReq_CheckAuthority
								.getParams(),aWebReq_CheckAuthority.getVersionCode(),aWebReq_CheckAuthority.getUsername(),aWebReq_CheckAuthority.getMethod());
				if (result == null) {
					throw new Exception(ReloginNull);
				}
				WebRes_Login aWebRes_CheckAuthority = new WebRes_Login();
				aWebRes_CheckAuthority.ParseJson(result);

				if (!aWebRes_CheckAuthority.getResult().equals("0")
						|| (aWebRes_CheckAuthority.getMessage() != null && aWebRes_CheckAuthority
								.getMessage().equals(
										mContext.getString(R.string.login_timeout)))) {
					throw new Exception(ReloginFalied);
				}
				mSettings.setVIN(aWebRes_CheckAuthority.getVIN());
				mSettings.setSecretKey(aWebRes_CheckAuthority.getSecretKey());
				mSettings.setAboutUrl(aWebRes_CheckAuthority.getAboutUrl());
				mSettings.setCarIllegalUrl(aWebRes_CheckAuthority.getCarIllegalUrl());

				result = mWebClient.executeByPostForJsonWithGZipAndEntrpyt(abase
						.getParams(),abase.getVersionCode(),abase.getUsername(),abase.getMethod());
				if (result == null) {
					throw new Exception(ReloginNull);
				}
				aWebRes_Base.ParseJson(result);
				if (!aWebRes_Base.getResult().equals("0")
						|| (aWebRes_Base.getMessage() != null && aWebRes_Base
								.getMessage().equals(
										mContext.getString(R.string.login_timeout)))) {
					throw new Exception(WebException);
				}
				
			} else {
				throw new Exception(aWebRes_Base.getMessage());
			}
		}catch(SocketTimeoutException e){
			e.printStackTrace();
			throw new Exception(WebSocketTimeout);
		
		}catch(HttpHostConnectException e){
			
			e.printStackTrace();
			throw new Exception(WebException);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("");
		}catch(Exception e){
			String s=e.getMessage();
			s=(s.equals(null)||s.equals(""))?WebUnknownError:s;
			throw new Exception(s);
		}
	}

	protected void execute(WebReq_Base abase, WebRes_Base aWebRes_Base)
			throws Exception {
		try {
			mWebClient = getWebClient(abase);
			JSONObject result = mWebClient
					.executeByPostForJsonWithGZipAndEntrpyt(abase.getParams(),abase.getVersionCode(),abase.getUsername(),abase.getMethod());
			
			if (result == null) {
				throw new Exception(WebResNUll);
			}
			aWebRes_Base.ParseJson(result);
		}catch(SocketTimeoutException e){
			e.printStackTrace();
			throw new Exception(WebSocketTimeout);
		
		}catch(HttpHostConnectException e){
			e.printStackTrace();
			throw new Exception(WebException);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("");
		}catch(Exception e){
			String s=e.getMessage();
			s=(s.equals(null)||s.equals(""))?WebUnknownError:s;
			throw new Exception(s);
		}
	}
}
