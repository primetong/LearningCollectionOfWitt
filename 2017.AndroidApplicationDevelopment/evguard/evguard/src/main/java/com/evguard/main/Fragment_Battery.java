package com.evguard.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.audrey.view.CircleBar;
import com.audrey.view.PorterDuffXfermodeView;
import com.evguard.model.ICommonWebResponse;
import com.evguard.model.WebReq_GetChargeManager;
import com.evguard.model.WebRes_GetChargeManager;
import com.xinghaicom.evguard.R;

/**
 * 
 * 
 * @author wlh 2015-4-15
 * 
 */
public class Fragment_Battery extends Fragment_Base {

	private String TAG = this.getClass().getSimpleName();

	private Dg_Waiting mDownDialog = null;
	private static Fragment_Battery mInstance;
	private static Object lockobj = new Object();

	private static final int MESSAGE_GETTING = 0;
	private static final int MESSAGE_GETTING_OK = 1;
	private static final int MESSAGE_GETTING_FAILED = 2;
	private Handler mHandler;
	private Dg_Alert mDg_Alert;
	private TextView tvChargeState;
	private TextView tvChargeRemainderTime_H;
	private TextView tvChargeRemainderTime_M;
	private TextView tvElectricity;
	private TextView tvVoltage;

	private PorterDuffXfermodeView mWaterWaveView;

	private LinearLayout ll_changing;

	private LinearLayout ll_unchange;

	private CircleBar fastCharging;

	private CircleBar rowCharging;

	private String flag="0";

	private String mFastCharge;

	private String mRowCharge;

	private String mChargeRemainderTime;

	private String mElectricity;

	private String mVoltage;

	private String socTxt;

	public static Fragment_Battery getInstance() {
		if (mInstance == null)
			synchronized (lockobj) {
				mInstance = new Fragment_Battery();
			}
		return mInstance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (layoutView != null)
			return layoutView;
		layoutView = inflater.inflate(R.layout.fagment_battery, container,
				false);
		findView();
		initHandler();
		return layoutView;
	}

	// @Override
	// public void setUserVisibleHint(boolean isVisibleToUser) {
	// super.setUserVisibleHint(isVisibleToUser);
	// if (isVisibleToUser) {
	// getMessageList();
	//
	// } else {
	// //相当于Fragment的onPause
	// // if(mThread_Fence!=null)
	// // mThread_Fence.quit();
	// }
	// }

	@Override
	public void initData() {
		getBatteryInfo();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mInstance = null;
	}

	private void findView() {
		ll_changing = (LinearLayout) layoutView.findViewById(R.id.ll_charging);
		ll_unchange = (LinearLayout) layoutView.findViewById(R.id.ll_uncharging);
		fastCharging = (CircleBar) layoutView.findViewById(R.id.fastCharge);
		rowCharging = (CircleBar) layoutView.findViewById(R.id.rowCharge);
		mWaterWaveView = (PorterDuffXfermodeView) layoutView
				.findViewById(R.id.wavebar);
		tvChargeRemainderTime_H = (TextView) layoutView
				.findViewById(R.id.tv_charge_remainder_time_h);
		tvChargeRemainderTime_M = (TextView) layoutView
				.findViewById(R.id.tv_charge_remainder_time_m);
		tvElectricity = (TextView) layoutView.findViewById(R.id.tv_electricity);
		tvVoltage = (TextView) layoutView.findViewById(R.id.tv_voltage);

	}

	private void initHandler() {
		mHandler = new Handler() {
			private String str;

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case MESSAGE_GETTING:
					if (mDownDialog != null){
						mDownDialog.dismiss();
					}
					if (mDownDialog == null) {
						mDownDialog = Dg_Waiting.newInstance("获取充电信息", "正在获取充电信息，请稍候.");
						mDownDialog.setCancelable(false);
					}
					mDownDialog.show(getChildFragmentManager(), "");
					break;

				case MESSAGE_GETTING_FAILED:
					if (mDownDialog != null){
						mDownDialog.dismiss();
						mDownDialog = null;
					}
					String errorMsg = "获取充电信息失败！";
					if(msg.obj != null) 
						errorMsg += msg.obj;
					showToast(errorMsg);
					break;
				case MESSAGE_GETTING_OK:
					if (mDownDialog != null) {
						mDownDialog.dismiss();
						mDownDialog = null;
					}
					Bundle b = msg.getData();
					if (b != null) {
						setData(b);
					}
					break;
				default:
					break;
				}
			}

		};
	}

	protected void setData(Bundle b) {
		flag = b.getString("Flag");
		if(flag.equals("0")){
			ll_changing.setVisibility(View.GONE);
			ll_unchange.setVisibility(View.VISIBLE);
			mFastCharge = b.getString("FastCharge");
			mRowCharge = b.getString("RowCharge");
			int chargeTotal = Integer.parseInt(mFastCharge) + Integer.parseInt(mRowCharge);
			fastCharging.setCircleMaxValue(chargeTotal);
			rowCharging.setCircleMaxValue(chargeTotal);
			fastCharging.setBarValue(Integer.parseInt(mFastCharge));
			rowCharging.setBarValue(Integer.parseInt(mRowCharge));
		}else {
			ll_changing.setVisibility(View.VISIBLE);
			ll_unchange.setVisibility(View.GONE);
			mChargeRemainderTime = b.getString("ChargeRemainderTime");
			int data = Integer.parseInt(mChargeRemainderTime);
			int hour = data / 60;
			int seconds = data % 60;
			tvChargeRemainderTime_H.setText(hour + "");
			tvChargeRemainderTime_M.setText(seconds + "");
			mElectricity = b.getString("Electricity");
			tvVoltage.setText(mElectricity);
			mVoltage = b.getString("Voltage");
			tvElectricity.setText(mVoltage);
			socTxt = b.getString("SOC");
			mWaterWaveView.setBarValue(Integer.parseInt(socTxt));
//			if (!socTxt.equals("")) {
//				tvChargeState.setVisibility(View.VISIBLE);
//				int soc = Integer.parseInt(socTxt);
//				if (soc == 100) {
//					tvChargeState.setText("充电完成");
//				} else {
//					tvChargeState.setText("正在充电");
//				}
//
//			}
		}
		

	}

	private void getBatteryInfo() {
		Message waitMsg = mHandler.obtainMessage(MESSAGE_GETTING);
		mHandler.sendMessage(waitMsg);

		WebReq_GetChargeManager aWebReq_GetChargeManager = new WebReq_GetChargeManager();
		ICommonWebResponse<WebRes_GetChargeManager> aICommonWebResponse = new ICommonWebResponse<WebRes_GetChargeManager>() {

			@Override
			public void WebRequestException(String ex) {
				Message endMsg = mHandler.obtainMessage(MESSAGE_GETTING_FAILED);
				mHandler.sendMessage(endMsg);
			}

			@Override
			public void WebRequsetFail(String sfalied) {
				Message endMsg = mHandler.obtainMessage(MESSAGE_GETTING_FAILED);
				mHandler.sendMessage(endMsg);
			}

			@Override
			public void WebRequstSucess(WebRes_GetChargeManager aWebRes) {
				if (aWebRes.getResult().equals("0")) {
					Message endMsg = mHandler.obtainMessage(MESSAGE_GETTING_OK);
					Bundle bundle = new Bundle();
					bundle.putString("Electricity", aWebRes.getElectricity());
					bundle.putString("SOC", aWebRes.getSOC());
					bundle.putString("Voltage", aWebRes.getVoltage());
					bundle.putString("ChargeRemainderTime",
							aWebRes.getChargeRemainderTime());
					bundle.putString("Flag",aWebRes.getFlag());
					bundle.putString("FastCharge",aWebRes.getFastCharge());
					bundle.putString("RowCharge",aWebRes.getRowCharge());
					endMsg.setData(bundle);
					mHandler.sendMessage(endMsg);
				} else {
					Message endMsg = mHandler
							.obtainMessage(MESSAGE_GETTING_FAILED);
					mHandler.sendMessage(endMsg);
				}
			}
		};
		WebRequestThreadEx<WebRes_GetChargeManager> aWebRequestThreadEx = new WebRequestThreadEx<WebRes_GetChargeManager>(
				aWebReq_GetChargeManager, aICommonWebResponse,
				new WebRes_GetChargeManager());
		new Thread(aWebRequestThreadEx).start();
	}

	@Override
	protected void restoreState(Bundle b) {
	}

	@Override
	protected Bundle saveState(Bundle saveState) {
		return saveState;
	}
}
