package com.evguard.main;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

import com.evguard.customview.AppTitleBar;
import com.evguard.customview.AppTitleBar.OnTitleActionClickListener;
import com.evguard.model.AlamSettings;
import com.evguard.model.ICommonWebResponse;
import com.evguard.model.WebReq_GetAlarmRemind;
import com.evguard.model.WebReq_SetAlarmRemind;
import com.evguard.model.WebRes_GetAlarmRemind;
import com.evguard.model.WebRes_SetAlamRemind;
import com.xinghaicom.evguard.R;

public class AC_AlamSetting extends AC_Base {

	private static final int MESSAGE_GETTING = 0;
	private static final int MESSAGE_GETTING_OK = 1;
	private static final int MESSAGE_GETTING_FAILED = 2;
	private static final int MESSAGE_GETTING_EXCEPTION = 3;
	private static final int MESSAGE_SETTING = 4;
	private static final int MESSAGE_SETTING_OK = 5;
	private static final int MESSAGE_SETTING_FAILED = 6;
	private static final int MESSAGE_SETTING_EXCEPTION = 7;
	private AppTitleBar mTitleBar;
	private TextView tvBatteryLow;
	private TextView tvDoorUnclosedMin;
	private ImageButton btHighTemp;
	private ImageButton btTirePressure;
	private ImageButton btChargeState;
	private String mBatteryTemperatureTooHight="";
	private String mTirePressure="";
	private String mChargeStatu="";
	private String mElecTooRow="";
	private String mDoorStatu="";

	private boolean mIsUserCancle = false;

	private Dg_Waiting mDownDialog = null;
	private Bundle bundle;
	private List<AlamSettings> mAlamSettings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_alam_setting);
		findViews();
		initViews();
		handleListener();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	

	private void handleListener() {
		tvBatteryLow.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				String mElecTooRowTex = tvBatteryLow.getText().toString();
			}
		});
		tvBatteryLow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				NumberPicker mPicker = new NumberPicker(AC_AlamSetting.this);
                mPicker.setMinValue(0);
                mPicker.setMaxValue(100);
                mPicker.setOnValueChangedListener(new OnValueChangeListener() {
                    
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal)
                    {
                        // TODO Auto-generated method stub
                    	tvBatteryLow.setText(String.valueOf(newVal) + "%");
                    	mElecTooRow = String.valueOf(newVal);
                    }
                });
                
                
                AlertDialog mAlertDialog = new AlertDialog.Builder(AC_AlamSetting.this)
                .setTitle("请选择电量提醒百分比").setView(mPicker).setPositiveButton("ok",null).create();
                mAlertDialog.show();
			}   
		});
		tvDoorUnclosedMin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				NumberPicker mPicker = new NumberPicker(AC_AlamSetting.this);
				mPicker.setMinValue(0);
				mPicker.setMaxValue(60);
				mPicker.setOnValueChangedListener(new OnValueChangeListener() {
					
					@Override
					public void onValueChange(NumberPicker picker, int oldVal, int newVal)
					{
						// TODO Auto-generated method stub
						tvDoorUnclosedMin.setText(String.valueOf(newVal) + "分钟");
						mDoorStatu = String.valueOf(newVal);
					}
				});
				
				
				AlertDialog mAlertDialog = new AlertDialog.Builder(AC_AlamSetting.this)
				.setTitle("请选择车门未关提醒分钟数").setView(mPicker).setPositiveButton("ok",null).create();
				mAlertDialog.show();
			}	
		});
		
		
		
		tvDoorUnclosedMin.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				String mDoorStatuTex = tvDoorUnclosedMin.getText().toString();
			}
		});
		
		btHighTemp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i("llj", "mBatteryTemperatureTooHight--" + mBatteryTemperatureTooHight);
				if (mBatteryTemperatureTooHight.equals("1")) {
					mBatteryTemperatureTooHight = "0";
					btHighTemp.setImageResource(R.drawable.icon_aa_off);
				} else if (mBatteryTemperatureTooHight.equals("0"))  {
					mBatteryTemperatureTooHight = "1";
					btHighTemp.setImageResource(R.drawable.icon_aa_on);
				}

			}
		});
		btTirePressure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mTirePressure.equals("1")) {
					mTirePressure = "0";
					btTirePressure.setImageResource(R.drawable.icon_aa_off);
				} else if (mTirePressure.equals("0"))  {
					mTirePressure = "1";
					btTirePressure.setImageResource(R.drawable.icon_aa_on);
				}
			}
		});
		btChargeState.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mChargeStatu.equals("1")) {
					mChargeStatu = "0";
					btChargeState.setImageResource(R.drawable.icon_aa_off);
				} else if (mChargeStatu.equals("0"))  {
					mChargeStatu = "1";
					btChargeState.setImageResource(R.drawable.icon_aa_on);
				}
			}
		});
	}

	private void initViews() {
		getAlamSettings();
//		mAlamSettings = new ArrayList<AlamSettings>();
//		AlamSettings mSetting1 = new AlamSettings("ElecTooRow",this.mElecTooRow);
//		AlamSettings mSetting2 = new AlamSettings("BatteryTemperatureTooHight",this.mBatteryTemperatureTooHight);
//		AlamSettings mSetting3 = new AlamSettings("DoorStatu",this.mDoorStatu);
//		AlamSettings mSetting4 = new AlamSettings("TirePressure",this.mTirePressure);
//		AlamSettings mSetting5 = new AlamSettings("ChargeStatu",this.mChargeStatu);
//		mAlamSettings.add(mSetting1);
//		mAlamSettings.add(mSetting2);
//		mAlamSettings.add(mSetting3);
//		mAlamSettings.add(mSetting4);
//		mAlamSettings.add(mSetting5);
//		mTitleBar.setTitleMode(
//				AppTitleBar.APPTITLEBARMODE_TXTANDTXTANDTXT, "报警设置",
//				null);
		mTitleBar.setTitleMode("取消",
				 null,
				"报警设置",false,
				"完成",null);
		mTitleBar
				.setOnTitleActionClickListener(new OnTitleActionClickListener() {

					private EditText etBatteryLow;

					@Override
					public void onLeftOperateClick() {
						AC_AlamSetting.this.finish();
					}

					@Override
					public void onRightOperateClick() {
//						mElecTooRow = tvBatteryLow.getText().toString();
//						if (mElecTooRow.equals("") || mElecTooRow == null) {
//							showToast("最低电量不能为空,请重新设置！");
//							return;
//						}
//						mDoorStatu = tvDoorUnclosedMin.getText().toString();
//						if (mDoorStatu.equals("") || mDoorStatu == null) {
//							showToast("车门未关时间不能为空,请重新设置！");
//							return;
//						}
						if(mAlamSettings == null)
							mAlamSettings = new ArrayList<AlamSettings>();
						mAlamSettings.clear();
						AlamSettings mSetting1 = new AlamSettings("ElecTooRow",mElecTooRow);
						AlamSettings mSetting2 = new AlamSettings("BatteryTemperatureTooHight",mBatteryTemperatureTooHight);
						AlamSettings mSetting3 = new AlamSettings("DoorStatu",mDoorStatu);
						AlamSettings mSetting4 = new AlamSettings("TirePressure",mTirePressure);
						AlamSettings mSetting5 = new AlamSettings("ChargeStatu",mChargeStatu);
						mAlamSettings.add(mSetting1);
						mAlamSettings.add(mSetting2);
						mAlamSettings.add(mSetting3);
						mAlamSettings.add(mSetting4);
						mAlamSettings.add(mSetting5);
						doSave(mAlamSettings);
					}

					@Override
					public void onTitleClick() {

					}
				});
		
	}

	private void getAlamSettings() {
		Message waitMsg = mHandler.obtainMessage(MESSAGE_GETTING);
		mHandler.sendMessage(waitMsg);
		
		
		WebReq_GetAlarmRemind aWebReq_GetAlam = new WebReq_GetAlarmRemind();

		ICommonWebResponse<WebRes_GetAlarmRemind> aICommonWebResponse = new ICommonWebResponse<WebRes_GetAlarmRemind>() {

			@Override
			public void WebRequestException(String ex) {
				Message endMsg = mHandler
						.obtainMessage(MESSAGE_GETTING_EXCEPTION);
				endMsg.obj = "ex:报警设置信息获取失败，请确认服务器地址设置是否正确，网络连接是否正常.";
				mHandler.sendMessage(endMsg);
			}

			@Override
			public void WebRequsetFail(String sfalied) {
				Message endMsg = mHandler.obtainMessage(MESSAGE_GETTING_FAILED);
				endMsg.obj = "falied:报警设置信息获取失败！" + sfalied;
				mHandler.sendMessage(endMsg);
			}

			@Override
			public void WebRequstSucess(WebRes_GetAlarmRemind aWebRes) {
				if (aWebRes.getResult().equals("0")) {
					// 关闭等待界面
					Message endMsg = mHandler.obtainMessage(MESSAGE_GETTING_OK);
					endMsg.obj = aWebRes.getAlamSettings();
					mHandler.sendMessage(endMsg);
				} else {
					Message endMsg = mHandler
							.obtainMessage(MESSAGE_GETTING_FAILED);
					endMsg.obj = aWebRes.getMessage();
					mHandler.sendMessage(endMsg);
				}
			}
		};
		
		WebRequestThreadEx<WebRes_GetAlarmRemind> aWebRequestThreadEx = new WebRequestThreadEx<WebRes_GetAlarmRemind>(
				aWebReq_GetAlam, aICommonWebResponse,
				new WebRes_GetAlarmRemind());
		new Thread(aWebRequestThreadEx).start();

	}

	private void findViews() {
		mTitleBar = (AppTitleBar) findViewById(R.id.title_bar);
		tvBatteryLow = (TextView) findViewById(R.id.tv_battery_low);
		tvDoorUnclosedMin = (TextView) findViewById(R.id.tv_door_unclose_minutes);
		btHighTemp = (ImageButton) findViewById(R.id.bt_high_temp);
		btTirePressure = (ImageButton) findViewById(R.id.bt_tire_pressure);
		btChargeState = (ImageButton) findViewById(R.id.bt_charge_state);
	}

	protected void doSave(List<AlamSettings> alamSettings) {
		Message waitMsg = mHandler.obtainMessage(MESSAGE_SETTING);
		mHandler.sendMessage(waitMsg);

		WebReq_SetAlarmRemind aWebReq_SetAlam = new WebReq_SetAlarmRemind(alamSettings);

		ICommonWebResponse<WebRes_SetAlamRemind> aICommonWebResponse = new ICommonWebResponse<WebRes_SetAlamRemind>() {

			@Override
			public void WebRequestException(String ex) {
				Message endMsg = mHandler
						.obtainMessage(MESSAGE_SETTING_EXCEPTION);
				endMsg.obj = "ex:报警设置信息保存失败，请确认服务器地址设置是否正确，网络连接是否正常.";
				mHandler.sendMessage(endMsg);
			}

			@Override
			public void WebRequsetFail(String sfalied) {
				Message endMsg = mHandler.obtainMessage(MESSAGE_SETTING_FAILED);
				endMsg.obj = "falied:报警设置信息保存失败！" + sfalied;
				mHandler.sendMessage(endMsg);
			}

			@Override
			public void WebRequstSucess(WebRes_SetAlamRemind aWebRes) {
				if (aWebRes.getResult().equals("0")) {
					// 关闭等待界面
					Message endMsg = mHandler.obtainMessage(MESSAGE_SETTING_OK);
					mHandler.sendMessage(endMsg);
				} else {
					Message endMsg = mHandler
							.obtainMessage(MESSAGE_SETTING_FAILED);
					endMsg.obj = aWebRes.getMessage();
					mHandler.sendMessage(endMsg);
				}
			}
		};
		
		WebRequestThreadEx<WebRes_SetAlamRemind> aWebRequestThreadEx = new WebRequestThreadEx<WebRes_SetAlamRemind>(
				aWebReq_SetAlam, aICommonWebResponse,
				new WebRes_SetAlamRemind());
		new Thread(aWebRequestThreadEx).start();
	}

	@Override
	protected void handleMsg(Message msg) {
		switch (msg.what) {
		case MESSAGE_GETTING:
			if (mDownDialog != null) {
				mDownDialog.dismiss();
			}
			if (mDownDialog == null) {
				mDownDialog = Dg_Waiting.newInstance("报警设置获取",
						"正在获取报警设置信息，请稍候.");
				mDownDialog.setCancelable(false);
			}
			mDownDialog.show(getSupportFragmentManager(), "");
			break;

		case MESSAGE_GETTING_OK:
			if (mDownDialog != null) {
				mDownDialog.dismiss();
				mDownDialog = null;
			}
			setData((List<AlamSettings>)msg.obj);
			break;

		case MESSAGE_GETTING_FAILED:
			if (mDownDialog != null){
				mDownDialog.dismiss();
				mDownDialog = null;
			}
				
			showToast((String) msg.obj);
			break;
		case MESSAGE_GETTING_EXCEPTION:
			if (mDownDialog != null)
				mDownDialog.dismiss();
			showToast((String) msg.obj);
			break;
			
		case MESSAGE_SETTING:
			if (mDownDialog == null) {
				mDownDialog = Dg_Waiting.newInstance("修改报警设置", "正在修改报警设置，请稍候.");
			}
			mDownDialog.show(getSupportFragmentManager(), "");
			break;

		case MESSAGE_SETTING_OK:
			if (mDownDialog != null) {
				mDownDialog.dismiss();
			}
			
			final Dg_Alert mDg_Alert = Dg_Alert.newInstance("修改报警设置", "报警设置修改成功！", "确认");
			mDg_Alert.setCancelable(false);
			mDg_Alert.setPositiveButton("确认", new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mDg_Alert.dismiss();
					AC_AlamSetting.this.finish();
				}
			});
			
			mDg_Alert.show(getSupportFragmentManager(),"");
			break;

		case MESSAGE_SETTING_FAILED:
			if (mDownDialog != null)
				mDownDialog.dismiss();
			showToast((String) msg.obj);
			break;
		case MESSAGE_SETTING_EXCEPTION:
			if (mDownDialog != null)
				mDownDialog.dismiss();
			showToast((String) msg.obj);
			break;
		}
		
		
	}
	private void setData(List<AlamSettings> alamSettings) {
		if(alamSettings.size() > 0){
			for (AlamSettings settings : alamSettings) {
				if(settings.getParam().equals("ElecTooRow")){
					mElecTooRow = settings.getValue();
					Log.i("llj", "mElecTooRow -- " + mElecTooRow);
					tvBatteryLow.setText(mElecTooRow + "%");
				}
				if(settings.getParam().equals("BatteryTemperatureTooHight")){
					if(settings.getValue().equals("0")){
						mBatteryTemperatureTooHight = "0";
						btHighTemp.setImageResource(R.drawable.icon_aa_off);
					}else{
						mBatteryTemperatureTooHight = "1";
						btHighTemp.setImageResource(R.drawable.icon_aa_on);
					}
				}
				if(settings.getParam().equals("DoorStatu")){
					mDoorStatu = settings.getValue();
					tvDoorUnclosedMin.setText(mDoorStatu + "分钟");
				}
				
				if(settings.getParam().equals("TirePressure")){
					if(settings.getValue().equals("0")){
						mTirePressure = "0";
						btTirePressure.setImageResource(R.drawable.icon_aa_off);
					}else{
						mTirePressure = "1";
						btTirePressure.setImageResource(R.drawable.icon_aa_on);
					}
				}
				
				if(settings.getParam().equals("ChargeStatu")){
					if(settings.getValue().equals("0")){
						mChargeStatu = "0";
						btChargeState.setImageResource(R.drawable.icon_aa_off);
					}else{
						mChargeStatu = "1";
						btChargeState.setImageResource(R.drawable.icon_aa_on);
					}
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
