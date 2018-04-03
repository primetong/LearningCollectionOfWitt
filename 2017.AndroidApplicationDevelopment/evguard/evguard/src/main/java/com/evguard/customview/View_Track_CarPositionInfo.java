package com.evguard.customview;

import java.util.ArrayList;
import java.util.List;

import com.evguard.main.WebRequestThreadEx;
import com.evguard.model.AddressInfo;
import com.evguard.model.AddressPoint;
import com.evguard.model.CarBaseInfo;
import com.evguard.model.ICommonWebResponse;
import com.evguard.model.TrackInfo;
import com.evguard.model.WebReq_GetLocation;
import com.evguard.model.WebRes_GetLocation;
import com.xinghaicom.evguard.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class View_Track_CarPositionInfo extends LinearLayout {
	private Context mContext = null;
	private TextView tv_carnum = null;
	private TextView tv_speed = null;
	private TextView tv_gpstime = null;
	private TextView tv_location = null;
	private TextView tv_showAdd = null;
	protected List<AddressInfo> mAddressInfoList;
	protected List<AddressPoint> mAddressList = new ArrayList<AddressPoint>();

	// private VehicleTrackInfo mVehicleTrackInfo=null;
	// private WebRequestThreadEx<WebRes_GetAddress> tGetAdd=null;
	private final int MESSAGE_GETADDOK = 0;
	private final int MESSAGE_GETADDFAIL = 1;

	@SuppressLint("NewApi")
	public View_Track_CarPositionInfo(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}

	public View_Track_CarPositionInfo(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public View_Track_CarPositionInfo(Context context) {
		super(context);
		mContext = context;
		init();
		// TODO Auto-generated constructor stub
	}

	private void init() {

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		this.setLayoutParams(params);
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_track_carpositioninfo, this);
		tv_carnum = (TextView) findViewById(R.id.tv_carnum);
		tv_speed = (TextView) findViewById(R.id.tv_speed);
		tv_gpstime = (TextView) findViewById(R.id.tv_gpstime);
		tv_location = (TextView) findViewById(R.id.tv_address);
		tv_showAdd = (TextView) findViewById(R.id.tv_showadd);
		tv_showAdd.setVisibility(View.GONE);
		tv_location.setVisibility(View.GONE);
	}

	public void dismiss() {
		this.setVisibility(View.GONE);
	}

	public void show() {
		this.setVisibility(View.VISIBLE);
	}

	public void setData(TrackInfo aTrackInfo) {
		// mVehicleTrackInfo=aCarInfo;
		tv_carnum.setText(aTrackInfo.getCarnum());
		tv_speed.setText(aTrackInfo.getSpeed() + "km/h");
		tv_gpstime.setText(aTrackInfo.getGpsTime());
		mAddressList.clear();
		AddressPoint point = new AddressPoint(aTrackInfo.getLongitude(), aTrackInfo.getLatitude(), 0);
		mAddressList.add(point);
		tv_location.setText("正在查询，请稍后...");
		this.postInvalidate();
	}

	public void setIsCanQueryAddress(boolean b) {
		if (b) {
			tv_showAdd.setVisibility(View.VISIBLE);
			tv_location.setVisibility(View.GONE);
		} else {
			tv_showAdd.setVisibility(View.GONE);
			tv_location.setVisibility(View.GONE);
		}
	}

	public void setOnShowAddListener(OnClickListener alistener) {
		tv_showAdd.setOnClickListener(alistener);
	}

	public void queryAddress() {
		getAddress(mAddressList);
		tv_showAdd.setVisibility(View.GONE);
		tv_location.setVisibility(View.VISIBLE);
	}

	public void stopQueryAddress() {
		quitGetAddress();
		tv_showAdd.setVisibility(View.VISIBLE);
		tv_location.setVisibility(View.GONE);
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == MESSAGE_GETADDOK) {
				tv_location.setText((String) msg.obj);
			}else if(msg.what == MESSAGE_GETADDFAIL){
				tv_location.setText("获取地址失败！");
			}
		}
	};
	private WebRequestThreadEx<WebRes_GetLocation> tGetAdd;

	private void quitGetAddress() {
		 tGetAdd.qiut();
	}

	public void getAddress(List<AddressPoint> addressList) {

		WebReq_GetLocation aWebReq_GetLocation = new WebReq_GetLocation(
				addressList);

		ICommonWebResponse<WebRes_GetLocation> aICommonWebResponse = new ICommonWebResponse<WebRes_GetLocation>() {
			@Override
			public void WebRequestException(String ex) {
				Message msg = mHandler.obtainMessage(MESSAGE_GETADDFAIL);
				mHandler.sendMessage(msg);
			}

			@Override
			public void WebRequsetFail(String sfalied) {
				Message msg = mHandler.obtainMessage(MESSAGE_GETADDFAIL);
				mHandler.sendMessage(msg);
			}

			@Override
			public void WebRequstSucess(WebRes_GetLocation aWebRes) {
				if(aWebRes.getResult().equals("0")){
				Message msg = mHandler.obtainMessage(MESSAGE_GETADDOK);
				mAddressInfoList = aWebRes.getAddressList();
				msg.obj = mAddressInfoList.get(0).getAddress();
				mHandler.sendMessage(msg);
				}else{
					Message msg = mHandler.obtainMessage(MESSAGE_GETADDFAIL);
					mHandler.sendMessage(msg);
				}
			}
		};

		tGetAdd = new WebRequestThreadEx<WebRes_GetLocation>(
				aWebReq_GetLocation, aICommonWebResponse,
				new WebRes_GetLocation());
		new Thread(tGetAdd).start();
	}

}
