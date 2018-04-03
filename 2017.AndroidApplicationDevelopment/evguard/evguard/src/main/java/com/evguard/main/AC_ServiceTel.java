package com.evguard.main;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.evguard.customview.AppTitleBar;
import com.evguard.customview.AppTitleBar.OnTitleActionClickListener;
import com.evguard.model.ICommonWebResponse;
import com.evguard.model.WebReq_GetServiceTelephone;
import com.evguard.model.WebRes_GetServiceTelephone;
import com.xinghaicom.evguard.R;

public class AC_ServiceTel extends AC_Base {
	private TextView tvInsurance;
	private TextView tvAlarm;
	private TextView tvDesignatedDriving;
	private TextView tvService4S;
	private TextView tvRescue;

	private AppTitleBar mTitleBar;

	private static final int MESSAGE_GETTING = 0;
	private static final int MESSAGE_GETTING_OK = 1;
	private static final int MESSAGE_GETTING_FAILED = 2;
	private static final int MESSAGE_GETTING_EXCEPTION = 3;
	private boolean mIsUserCancle = false;

	private Dg_Waiting mDownDialog = null;
	private Bundle b;
	private ImageButton btInsurance;
	private ImageButton btAlarm;
	private ImageButton btDesignatedDriving;
	private ImageButton btService4S;
	private ImageButton btRescue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_service_tel);
		findViews();
		handleListener();
	}

	private void handleListener() {
		btInsurance.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String number = tvInsurance.getText().toString();
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ number));
				startActivity(intent);
			}
		});
		
		btAlarm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String number = tvAlarm.getText().toString();
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ number));
				startActivity(intent);
			}
		});
		
		btDesignatedDriving.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String number = tvDesignatedDriving.getText().toString();
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ number));
				startActivity(intent);
			}
		});
		
		btService4S.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String number = tvService4S.getText().toString();
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ number));
				startActivity(intent);
			}
		});
		
		btRescue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String number = tvRescue.getText().toString();
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ number));
				startActivity(intent);
			}
		});
	}

	private void findViews() {
		tvInsurance = (TextView) findViewById(R.id.tv_insurance_tel);
		tvAlarm = (TextView) findViewById(R.id.tv_alarm_tel);
		tvDesignatedDriving = (TextView) findViewById(R.id.tv_driving_tel);
		tvService4S = (TextView) findViewById(R.id.tv_service_tel);
		tvRescue = (TextView) findViewById(R.id.tv_rescue_tel);
		btInsurance = (ImageButton) findViewById(R.id.bt_insurance_tel);
		btAlarm = (ImageButton) findViewById(R.id.bt_alarm_tel);
		btDesignatedDriving = (ImageButton) findViewById(R.id.bt_driving_tel);
		btService4S = (ImageButton) findViewById(R.id.bt_service_tel);
		btRescue = (ImageButton) findViewById(R.id.bt_rescue_tel);
		mTitleBar = (AppTitleBar) findViewById(R.id.title_bar);
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	private void initData() {
		b = new Bundle();
		BitmapDrawable drawable = new BitmapDrawable(getResources(),
				BitmapFactory.decodeResource(getResources(),
						R.drawable.icon_edit));
//		mTitleBar.setTitleMode(
//				AppTitleBar.APPTITLEBARMODE_TXTANDBACKANDOPERATE, "服务预约",
//				drawable);
		mTitleBar.setTitleMode(null,
				mContext.getResources().getDrawable(R.drawable.icon_back),
				"服务预约",false,
				null,drawable);
		mTitleBar
				.setOnTitleActionClickListener(new OnTitleActionClickListener() {
					@Override
					public void onLeftOperateClick() {
						AC_ServiceTel.this.finish();
					}

					@Override
					public void onRightOperateClick() {
						Intent intent = new Intent(AC_ServiceTel.this,
								AC_EditServiceTel.class);
						intent.putExtras(b);
						startActivity(intent);
					}

					@Override
					public void onTitleClick() {
					}

				});

		getServiceTel();
	}

	private void getServiceTel() {
		Message waitMsg = mHandler.obtainMessage(MESSAGE_GETTING);
		mHandler.sendMessage(waitMsg);
		WebReq_GetServiceTelephone aWebReq_GetServiceTelephone = new WebReq_GetServiceTelephone();

		ICommonWebResponse<WebRes_GetServiceTelephone> aICommonWebResponse = new ICommonWebResponse<WebRes_GetServiceTelephone>() {

			@Override
			public void WebRequestException(String ex) {
				Message endMsg = mHandler
						.obtainMessage(MESSAGE_GETTING_EXCEPTION);
				endMsg.obj = "服务电话获取失败，请重试！";
				mHandler.sendMessage(endMsg);

			}

			@Override
			public void WebRequsetFail(String sfalied) {
				Message endMsg = mHandler.obtainMessage(MESSAGE_GETTING_FAILED);
				endMsg.obj = "服务电话获取失败，请重试！";
				mHandler.sendMessage(endMsg);
			}

			@Override
			public void WebRequstSucess(WebRes_GetServiceTelephone aWebRes) {
				if (aWebRes.getResult().equals("0")) {
					// 关闭等待界面
					Message endMsg = mHandler.obtainMessage(MESSAGE_GETTING_OK);
					b.putString("AlarmTelephone", aWebRes.getAlarmTelephone());
					b.putString("DrivingTelephone",
							aWebRes.getDrivingTelephone());
					b.putString("InsuranceTelephone",
							aWebRes.getInsuranceTelephone());
					b.putString("RescueTelephone", aWebRes.getRescueTelephone());
					b.putString("ServiceTelephone",
							aWebRes.getServiceTelephone());
					endMsg.obj = b;
					mHandler.sendMessage(endMsg);

				} else {
					Message endMsg = mHandler
							.obtainMessage(MESSAGE_GETTING_FAILED);
					endMsg.obj = aWebRes.getMessage();
					mHandler.sendMessage(endMsg);
				}
			}
		};

		WebRequestThreadEx<WebRes_GetServiceTelephone> aWebRequestThreadEx = new WebRequestThreadEx<WebRes_GetServiceTelephone>(
				aWebReq_GetServiceTelephone, aICommonWebResponse,
				new WebRes_GetServiceTelephone());
		new Thread(aWebRequestThreadEx).start();

	}

	@Override
	public void handleMsg(Message msg) {
		switch (msg.what) {
		case MESSAGE_GETTING:
			if (mDownDialog != null) {
				mDownDialog.dismiss();
				mDownDialog = null;
			}
			if (mDownDialog == null) {
				mDownDialog = Dg_Waiting.newInstance("获取服务电话", "正在获取服务电话，请稍候.");
				mDownDialog.setCancelable(false);
			}
			mDownDialog.show(getSupportFragmentManager(), "");
			break;

		case MESSAGE_GETTING_OK:
			if (mDownDialog != null) {
				mDownDialog.dismiss();
				mDownDialog = null;
			}
			Bundle b = (Bundle) msg.obj;
			if (b != null) {
				showData(b);
			}
			break;

		case MESSAGE_GETTING_FAILED:
			if (mDownDialog != null) {
				mDownDialog.dismiss();
				mDownDialog = null;
			}
			showToast((String) msg.obj);
			break;
		case MESSAGE_GETTING_EXCEPTION:
			if (mDownDialog != null) {
				mDownDialog.dismiss();
				mDownDialog = null;
			}
			showToast((String) msg.obj);
			break;
		}
	}

	private void showData(Bundle b) {
		tvAlarm.setText(b.getString("AlarmTelephone"));
		tvDesignatedDriving.setText(b.getString("DrivingTelephone"));
		tvInsurance.setText(b.getString("InsuranceTelephone"));
		tvRescue.setText(b.getString("RescueTelephone"));
		tvService4S.setText(b.getString("ServiceTelephone"));
	}
}
