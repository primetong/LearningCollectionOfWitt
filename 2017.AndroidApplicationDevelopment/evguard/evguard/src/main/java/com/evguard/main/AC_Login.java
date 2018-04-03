package com.evguard.main;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.evguard.customview.KeyboardUtil;
import com.evguard.data.AppDataCache;
import com.evguard.main.Dg_Base.OnCancelListener;
import com.evguard.main.Dg_SelectCarType.OnCarTypeSelectedListener;
import com.evguard.main.Thread_GetServerInfo.CallBack_GetServerInfo;
import com.evguard.model.ICommonWebResponse;
import com.evguard.model.ServerInfo;
import com.evguard.model.WebReq_Login;
import com.evguard.model.WebRes_Login;
import com.evguard.tools.CommUtils;
import com.evguard.tools.ConstantTool;
import com.evguard.tools.LogEx;
import com.xinghaicom.evguard.R;
import com.xinghaicom.security.MD5;

public class AC_Login extends AC_Base {

	private EditText mLoginName;
	private EditText mLoginPwd;
	private TextView mLoginOk;
	protected Button mBTNPwdForget;//忘记密码
	private TextView tv_catype=null;//车型选择
	protected Button mBTNPwdResetting;
	protected ImageButton mBTNPwdRemenber;
	protected Button mBTNSettings;
	protected TextView mTVRegistering;
	ConnectivityManager mConnectivity;
	private Dg_Waiting mDownDialog = null;
	private KeyboardUtil mKeyboardUtil = null;
//	protected Handler_UncaughtException mUncaughtExceptionHandler = null;
	protected final int REGISTERING = 0;
	private ViewFlipper mAdShowBanner;
	
	private List<ServerInfo> mServers=new ArrayList<ServerInfo>();
	private Dg_SelectCarType mDg_SelectCarType=null;
	
	
	private static final int MESSAGE_UI_WAIT = 0;
	private static final int MESSAGE_UI_END = 1;
	private static final int MESSAGE_GETSERVERING=2;
	private static final int MESSAGE_GETSERVEROK=3;
	private static final int MESSAGE_GETSERVERFAILED=4;
	
	private int actiontype = 1;
	private boolean isRememberPwd = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getIntentData();
		setContentView(R.layout.ac_login);
		findViews();
		if(actiontype == 22){
			Bundle bundle = this.getIntent().getExtras();
			login(bundle.getString("username"),bundle.getString("password"));
		}
		handleListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
		AppDataCache.getInstance().getKidCurPositionInfoList().clear();
//		StatService.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mLoginOk.setEnabled(true);
//		StatService.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (mKeyboardUtil.isShow()) {
				mKeyboardUtil.hideKeyboard();
				return true;
			}
			AlertDialog.Builder alertDlgBuilder = new AlertDialog.Builder(this);
			alertDlgBuilder.setTitle(getResources().getString(R.string.exit));
			alertDlgBuilder.setMessage(getResources().getString(
					R.string.confirm_exit));
			alertDlgBuilder.setPositiveButton(
					getResources().getString(R.string.yes),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Intent tExit=new Intent("com.evguard.activity.exit");
							tExit.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							tExit.addCategory(Intent.CATEGORY_DEFAULT);
							startActivity(tExit);
							AC_Login.this.finish();
						}
					});
			alertDlgBuilder.setNegativeButton(
					getResources().getString(R.string.no), null);
			alertDlgBuilder.show();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.login_menu, menu);
		// mMenuSettings = menu.findItem(R.id.menu_settings);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_help:
			Intent helpIntent = new Intent(AC_Login.this, AC_About.class);
			startActivity(helpIntent);
			break;
		}
		return false;
	}

	private void getIntentData() {
		if (this.getIntent() != null) {
			actiontype = this.getIntent().getIntExtra(
					ConstantTool.INTENT_EXTRANAME_DATA1, 1);
		}
	}

	private void findViews() {
		mLoginName = (EditText) findViewById(R.id.et_loginname);
		mLoginPwd = (EditText) findViewById(R.id.et_pwd);
		mLoginOk = (Button) findViewById(R.id.btn_login);
		mBTNPwdForget = (Button) findViewById(R.id.bt_forget_password);
		mBTNPwdRemenber = (ImageButton) findViewById(R.id.bt_rem_pwd);
		mTVRegistering = (TextView) findViewById(R.id.tv_ownerinfo);
		tv_catype=(TextView)findViewById(R.id.tv_catype);
		mLoginName.getText().toString();
		mLoginPwd.getText().toString();
		if(mSettings.getAgentName()==null||mSettings.getAgentName().equals("")){
			tv_catype.setText("选择车型");
		}else{
			tv_catype.setText("车型："+mSettings.getAgentName());
		}
	}


	private void handleListener() {

		mLoginName.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			public void afterTextChanged(Editable s) {
			}
		});

		mKeyboardUtil = new KeyboardUtil(AC_Login.this, mContext, mLoginPwd);
		mLoginPwd.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				int inputback = mLoginPwd.getInputType();
				mKeyboardUtil.showKeyboard();
				CommUtils.hidenSystem(AC_Login.this, mLoginPwd);
				mLoginPwd.setInputType(inputback);
				return false;
			}
		});
		mLoginPwd.setOnFocusChangeListener(new OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus && mKeyboardUtil != null) {
					mKeyboardUtil.hideKeyboard();
				} else {
					int inputback = mLoginPwd.getInputType();
					mKeyboardUtil.showKeyboard();
					CommUtils.hidenSystem(AC_Login.this, mLoginPwd);
					mLoginPwd.setInputType(inputback);
				}
			}
		});
		mLoginOk.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				mLoginOk.setEnabled(false);
				if(!checkLoginData()){
					mLoginOk.setEnabled(true);
					return;
				}
				 else {
						 String loginName = mLoginName.getText().toString();
						String loginPwd = mLoginPwd.getText().toString();
						loginName=loginName.replaceAll(" ","");  
						login(loginName, loginPwd);
				}
			}
		});

		mBTNPwdForget.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(AC_Login.this,AC_PasswordForget.class);
//				startActivity(intent);
				showToast("请联系管理员");
			}
		});
		mBTNPwdRemenber.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(!isRememberPwd){
					isRememberPwd = true;
					mBTNPwdRemenber.setImageResource(R.drawable.icon_rem_pwd);
					mSettings.setIsUserQiutApp(false);
				} else {
					isRememberPwd = false;
					mBTNPwdRemenber.setImageResource(R.drawable.icon_unrem_pwd);
					mSettings.setIsUserQiutApp(true);
				}
			}
		});
		tv_catype.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getServers();
			}
			
		});
	}
	private boolean checkLoginData(){
		if (TextUtils.isEmpty(mLoginName.getText()) ||TextUtils.isEmpty(mLoginPwd.getText())) {
			showToast(mContext.getResources().getString(
					R.string.login_empty_note));
			return false;
		}
		if(mSettings.getAgentName()==null|| mSettings.getAgentName().equals("")){
			showToast("请选择车型");
			return false;
		}
		return true;
		
	}

	/**
	 * 登录服务
	 * 
	 * @param loginName
	 * @param loginPwd
	 */
	private void login(final String loginName, final String loginPwd) {

		Message waitMsg = mHandler.obtainMessage(MESSAGE_UI_WAIT);
		mHandler.sendMessage(waitMsg);
		// 保存用户登录时的帐号和密码
		if ((loginName.equals("") || loginName == null)
				|| (loginPwd.equals("") || loginPwd == null)) {
			showToast("用户名或密码为空，请输入");
			return;
		}
		mSettings.setUserName(loginName);
		mSettings.setLoginPwd(loginPwd);

		// 身份验证请求
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String password = null;
		try 
		{
			MD5 md5 = new MD5();
			password = md5.hashToDigestDes(loginPwd);
		} catch (Exception e) {
			Log.e("ConstantTool", "MD5加密错误" + e.getMessage());
		}
		String password_encode = loginName + password + timestamp.toString();
		WebReq_Login aWebReq_CheckAuthorityAll = new WebReq_Login(loginName,password_encode,timestamp.toString());
//		if (CommUtils.isEmpty(mSettings.getJPushRegisterId())) {
//			String id = JPushInterface.getRegistrationID(mContext);
//			mSettings.setJPushRegisterId(id);
//		}
//		aWebReq_CheckAuthorityAll.setregisterId(mSettings.getJPushRegisterId());
		// web请求结果回调
		ICommonWebResponse<WebRes_Login> aICommonWebResponse = new ICommonWebResponse<WebRes_Login>() {
			@Override
			public void WebRequstSucess(WebRes_Login aWebRes) {
				if (aWebRes.getResult().equals("0")) {					
					// 保存该登录名相关信息
					saveLoginInfo(aWebRes);
//					StatService.onEvent(AC_Login.this,
//							getString(R.string.login_statistic_event),
//							String.format(
//									getString(R.string.login_success_format),
//									mLoginName));

					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("CarNum", aWebRes.getCarNum());
					bundle.putString("AboutUrl", aWebRes.getAboutUrl());
					bundle.putString("HeartInterval", aWebRes.getHeartInterval());
					intent.putExtras(bundle);
					intent.setClass(mContext, AC_Main.class);
//					intent.setClass(AC_Login.this,AC_About.class);
					startActivity(intent);
					// 关闭等待界面
					Message endMsg = mHandler.obtainMessage(MESSAGE_UI_END);
					mHandler.sendMessage(endMsg);

				} else {
//					StatService.onEvent(AC_Login.this,
//							getString(R.string.login_statistic_event),
//							String.format(
//									getString(R.string.login_failed_format),
//									mLoginName));
					Message endMsg = mHandler.obtainMessage(MESSAGE_UI_END);
					endMsg.obj = aWebRes.getMessage();
					mHandler.sendMessage(endMsg);
				}
			}

			@Override
			public void WebRequestException(String ex) {
				Message endMsg = mHandler.obtainMessage(MESSAGE_UI_END);
				endMsg.obj = "登陆失败!";
				mHandler.sendMessage(endMsg);
			}

			@Override
			public void WebRequsetFail(String sfalied) {
				Message endMsg = mHandler.obtainMessage(MESSAGE_UI_END);
				endMsg.obj = "登录失败！" + sfalied;
				mHandler.sendMessage(endMsg);
				
			}
		};
		WebRequestThreadEx<WebRes_Login> aWebRequestThreadEx = new WebRequestThreadEx<WebRes_Login>(
				aWebReq_CheckAuthorityAll, aICommonWebResponse,
				new WebRes_Login());
		new Thread(aWebRequestThreadEx).start();

	}

	protected void saveLoginInfo(WebRes_Login aWebRes) {
		mSettings.setVIN(aWebRes.getVIN());
		mSettings.setSecretKey(aWebRes.getSecretKey());
		mSettings.setAboutUrl(aWebRes.getAboutUrl());
		mSettings.setCarIllegalUrl(aWebRes.getCarIllegalUrl());
	}

	private void getServers(){
	
		mHandler.sendEmptyMessage(MESSAGE_GETSERVERING);
		Thread_GetServerInfo mThread_GetServerInfo=new Thread_GetServerInfo(new CallBack_GetServerInfo(){

			@Override
			public void getServerInfosOk(List<ServerInfo> alist) {
				mServers.clear();
				mServers.addAll(alist);
				mHandler.sendEmptyMessage(MESSAGE_GETSERVEROK);
			}

			@Override
			public void getServerInfosFailed(String serror) {
				Message msg=mHandler.obtainMessage();
				msg.what=MESSAGE_GETSERVERFAILED;
				msg.obj=serror;
				mHandler.sendMessage(msg);
			}
			
		});
		mThread_GetServerInfo.getServerInfo();
	}

	private void showCarType(){
		mDg_SelectCarType=Dg_SelectCarType.newInstance("","");
		mDg_SelectCarType.setOnCarTypeSelectedListener(new OnCarTypeSelectedListener(){

			@Override
			public void onCarTypeSelected(ServerInfo ainfo) {
				tv_catype.setText("车型："+ainfo.getAgentname());
				mSettings.setAgentName(ainfo.getAgentname());
				mSettings.setServerIP(ainfo.getServerUrl());
				mDg_SelectCarType.dismiss();
			}
		});
		mDg_SelectCarType.showDialog(this.getSupportFragmentManager());
	}
	/**
	 * 
	 */
	public boolean checkURL() {
		boolean value = false;
		try {
			new ConstantTool();
			String checkUrl = "";// consTool.getCheckUrl(mContext);

			HttpURLConnection conn = (HttpURLConnection) new URL(checkUrl)
					.openConnection();
			conn.setConnectTimeout(10000);
			int code = conn.getResponseCode();
			if (code != 200) {
				value = false;
			} else {
				value = true;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		LogEx.i(TAG, "connect netwrok result:" + value);
		return value;
	}


	@Override
	public void handleMsg(Message msg) {
		switch (msg.what) {
		case MESSAGE_UI_WAIT:
			if (mDownDialog != null) {
				mDownDialog.dismiss();
			}
			if (mDownDialog == null) {
				mDownDialog = Dg_Waiting.newInstance("登录", "正在登录，请稍候.");
				mDownDialog.setCancelable(true);
				mDownDialog.setOnCancelListener(new OnCancelListener() {
					@Override
					public void onCancle() {
						mLoginOk.setEnabled(true);
					}
				});
			}
			mDownDialog.show(getSupportFragmentManager(), "");
			break;
		case MESSAGE_UI_END:

			if (mDownDialog != null) {
				mDownDialog.dismiss();
				mDownDialog = null;
				mLoginOk.setEnabled(true);
			}
			if (msg.obj != null) {
				String showText = msg.obj.toString();
				showToast(showText);
			}
			break;
		case MESSAGE_GETSERVERING:
			showCarType();
			break;
		case MESSAGE_GETSERVEROK:
			if (mDownDialog != null) {
				mDownDialog.dismiss();
			}
			mDg_SelectCarType.update(mServers);
			break;
		case MESSAGE_GETSERVERFAILED:
			mDg_SelectCarType.showError((String) msg.obj);
			showToast((String) msg.obj);
			break;
		
		}
	}
	
//	@Override
//	public void startActivityForResult(Intent intent, int requestCode,
//			Bundle options) {
//		switch (requestCode) {
//		case 22:
//			Bundle bundle = intent.getExtras();
//			String username = bundle.getString("username");
//			String password = bundle.getString("password");
//			login(username, password);
//			break;
//
//		default:
//			break;
//		}
//	}

}