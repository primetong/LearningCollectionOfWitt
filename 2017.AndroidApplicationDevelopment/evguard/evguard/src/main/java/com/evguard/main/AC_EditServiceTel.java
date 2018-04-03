package com.evguard.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.evguard.customview.AppTitleBar;
import com.evguard.customview.AppTitleBar.OnTitleActionClickListener;
import com.evguard.main.Dg_Base.OnCancelListener;
import com.evguard.model.ICommonWebResponse;
import com.evguard.model.WebReq_SetServiceTelephone;
import com.evguard.model.WebRes_SetServiceTelephone;
import com.xinghaicom.evguard.R;

public class AC_EditServiceTel extends AC_Base {
	private EditText etInsurance;
	private EditText etAlarm;
	private EditText etDesignatedDriving;
	private EditText etService4S;
	private EditText etRescue;
	private Context mContext;
	private AppTitleBar mTitleBar;

	private static final int MESSAGE_SETTING = 0;
	private static final int MESSAGE_SETTING_OK = 1;
	private static final int MESSAGE_SETTING_FAILED = 2;
	private static final int MESSAGE_SETTING_EXCEPTION = 3;
	private boolean mIsUserCancle = false;

	private Dg_Waiting mDownDialog = null;
	private ImageView ivInsurance;
	private ImageView ivAlarm;
	private ImageView ivDesignatedDriving;
	private ImageView ivService4S;
	private ImageView ivRescue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_edit_service_tel);
		findViews();
		initViews();

	}

	private void initViews() {
		
//		mTitleBar.setTitleMode(
//				AppTitleBar.APPTITLEBARMODE_TXTANDTXTANDTXT, "编辑资料",
//				null);
		mTitleBar.setTitleMode("取消",
				null,
				"编辑资料",false,
				"完成",null);
		mTitleBar
				.setOnTitleActionClickListener(new OnTitleActionClickListener() {

					@Override
					public void onLeftOperateClick() {
						AC_EditServiceTel.this.finish();
					}

					@Override
					public void onRightOperateClick() {
						doSave();
					}

					@Override
					public void onTitleClick() {
						// TODO Auto-generated method stub

					}
				});
	}

	private void findViews() {
		mTitleBar = (AppTitleBar) findViewById(R.id.title_bar);
		etInsurance = (EditText) findViewById(R.id.et_insurance_tel);
		etAlarm = (EditText) findViewById(R.id.et_alarm_tel);
		etDesignatedDriving = (EditText) findViewById(R.id.et_driving_tel);
		etService4S = (EditText) findViewById(R.id.et_service_tel);
		etRescue = (EditText) findViewById(R.id.et_rescue_tel);
		ivInsurance = (ImageView) findViewById(R.id.iv_insurance_tel);
		ivAlarm = (ImageView) findViewById(R.id.iv_alarm_tel);
		ivDesignatedDriving = (ImageView) findViewById(R.id.iv_driving_tel);
		ivService4S = (ImageView) findViewById(R.id.iv_service_tel);
		ivRescue = (ImageView) findViewById(R.id.iv_rescue_tel);
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	private void initData() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		etInsurance.setText(bundle.getString("InsuranceTelephone"));
		etAlarm.setText(bundle.getString("AlarmTelephone"));
		etDesignatedDriving.setText(bundle.getString("DrivingTelephone"));
		etService4S.setText(bundle.getString("ServiceTelephone"));
		etRescue.setText(bundle.getString("RescueTelephone"));
		etInsurance.setSelection(etInsurance.getText().toString().length());
		etInsurance.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View view, boolean isFocus) {
				if(isFocus){
					ivInsurance.setImageResource(R.drawable.img_cross_p);
					
				} else {
					ivInsurance.setImageResource(R.drawable.img_cross_n);
				}
			}
		});
		etAlarm.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View view, boolean isFocus) {
				if(isFocus){
					ivAlarm.setImageResource(R.drawable.img_cross_p);
				} else {
					ivAlarm.setImageResource(R.drawable.img_cross_n);
				}
			}
		});
		etDesignatedDriving.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View view, boolean isFocus) {
				if(isFocus){
					ivDesignatedDriving.setImageResource(R.drawable.img_cross_p);
				} else {
					ivDesignatedDriving.setImageResource(R.drawable.img_cross_n);
				}
			}
		});
		etRescue.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View view, boolean isFocus) {
				if(isFocus){
					ivRescue.setImageResource(R.drawable.img_cross_p);
				} else {
					ivRescue.setImageResource(R.drawable.img_cross_n);
				}
			}
		});
		etService4S.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View view, boolean isFocus) {
				if(isFocus){
					ivService4S.setImageResource(R.drawable.img_cross_p);
				} else {
					ivService4S.setImageResource(R.drawable.img_cross_n);
				}
			}
		});
	}

	protected void doSave() {
		Message waitMsg = mHandler.obtainMessage(MESSAGE_SETTING);
		mHandler.sendMessage(waitMsg);
		
		String alarmTelephone = etAlarm.getText().toString();
		if(alarmTelephone == null){
			showToast("报警电话为空！");
			return;
		}
		String insuranceTelephone = etInsurance.getText().toString();
		if(insuranceTelephone == null){
			showToast("保险电话为空！");
			return;
		}
		String serviceTelephone = etService4S.getText().toString();
		if(serviceTelephone == null){
			showToast("4s服务电话为空！");
			return;
		}
		String drivingTelephone = etDesignatedDriving.getText().toString();
		if(drivingTelephone == null){
			showToast("报警号码为空！");
		}
		String rescueTelephone = etRescue.getText().toString();
		if(rescueTelephone == null){
			showToast("报警号码为空！");
		}
		WebReq_SetServiceTelephone aWebReq_SetServiceTel = new WebReq_SetServiceTelephone(insuranceTelephone,
				alarmTelephone,drivingTelephone,serviceTelephone,rescueTelephone);

		ICommonWebResponse<WebRes_SetServiceTelephone> aICommonWebResponse = new ICommonWebResponse<WebRes_SetServiceTelephone>() {

			@Override
			public void WebRequestException(String ex) {
				Message endMsg = mHandler
						.obtainMessage(MESSAGE_SETTING_EXCEPTION);
				endMsg.obj = "ex:号码保存失败，请确认服务器地址设置是否正确，网络连接是否正常.";
				mHandler.sendMessage(endMsg);

			}

			@Override
			public void WebRequsetFail(String sfalied) {
				Message endMsg = mHandler.obtainMessage(MESSAGE_SETTING_FAILED);
				endMsg.obj = "falied:号码保存失败！" + sfalied;
				mHandler.sendMessage(endMsg);
			}

			@Override
			public void WebRequstSucess(WebRes_SetServiceTelephone aWebRes) {
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
		WebRequestThreadEx<WebRes_SetServiceTelephone> aWebRequestThreadEx = new WebRequestThreadEx<WebRes_SetServiceTelephone>(
				aWebReq_SetServiceTel, aICommonWebResponse,
				new WebRes_SetServiceTelephone());
		new Thread(aWebRequestThreadEx).start();
	}

	@Override
	public void handleMsg(Message msg) {
		switch (msg.what) {
		case MESSAGE_SETTING:
			if (mDownDialog != null) {
				mDownDialog.dismiss();
			}
			if (mDownDialog == null) {
				mDownDialog = Dg_Waiting.newInstance("修改服务电话", "正在修改服务电话，请稍候.");
				mDownDialog.setCancelable(true);
				mDownDialog.setOnCancelListener(new OnCancelListener() {
					@Override
					public void onCancle() {
						mIsUserCancle = true;
					}
				});
			}
			mDownDialog.show(getSupportFragmentManager(), "");
			break;

		case MESSAGE_SETTING_OK:
			if (mIsUserCancle)
				break;
			if (mDownDialog != null) {
				mDownDialog.dismiss();
			}
			final Dg_Alert mDg_Alert = Dg_Alert.newInstance("修改服务电话", "服务电话修改成功！", "确认");
			mDg_Alert.setCancelable(true);
			mDg_Alert.setPositiveButton("确认", new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mDg_Alert.dismiss();
					AC_EditServiceTel.this.finish();
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
			if (mIsUserCancle)
				break;
			if (mDownDialog != null)
				mDownDialog.dismiss();
			showToast((String) msg.obj);
			break;
		}
	}

}
