package com.evguard.customview;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.model.LatLng;
import com.evguard.main.WebRequestThreadEx;
import com.evguard.model.AddressInfo;
import com.evguard.model.AddressPoint;
import com.evguard.model.CarCurPositionInfo;
import com.evguard.model.ICommonWebResponse;
import com.evguard.model.WebReq_GetLocation;
import com.evguard.model.WebRes_GetLocation;
import com.xinghaicom.evguard.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class View_Location_CarPositionInfo extends RelativeLayout {

	private Context mContext=null;
	private TextView tv_carnum=null;
	private TextView tv_speed=null;
	private TextView tv_gpstime=null;
	private TextView tv_location=null;
	private TextView tv_close=null;
	private TextView tv_mileage = null;
	protected List<AddressInfo> mAddressInfoList;
	protected List<AddressPoint> mAddressList = new ArrayList<AddressPoint>();
	private final int MESSAGE_GETADDOK = 0;
	private final int MESSAGE_GETADDFAIL = 1;
	
	@SuppressLint("NewApi")
	public View_Location_CarPositionInfo(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext=context;
		init();
	}
	public View_Location_CarPositionInfo(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		init();
	}
	public View_Location_CarPositionInfo(Context context) {
		super(context);
		mContext=context;
		init();
	}
	private void init(){
		
		RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		this.setLayoutParams(params);
		LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_location_carpositioninfo,this);
		tv_carnum=(TextView)findViewById(R.id.tv_carnum);
		tv_gpstime=(TextView)findViewById(R.id.tv_gpstime);
		tv_location=(TextView)findViewById(R.id.tv_location);
		tv_close=(TextView)findViewById(R.id.tv_close);
		tv_mileage = (TextView)findViewById(R.id.tv_mileage);
		tv_speed = (TextView)findViewById(R.id.tv_speed);
	}
	public void setData(CarCurPositionInfo abaseinfo){
		tv_carnum.setText(abaseinfo.getCarNum());
		tv_speed.setText(abaseinfo.getSpeed()+"km/h");
		tv_gpstime.setText(abaseinfo.getGpsTime());
		mAddressList.clear();
		tv_location.setText("正在获取地址，请稍后...");
		tv_mileage.setText("里程：" + abaseinfo.getMileage() + "km");
		AddressPoint point = new AddressPoint(abaseinfo.getLongitude(), abaseinfo.getLatitude(), 0);
		mAddressList.add(point);
		getAddress(mAddressList);
		this.postInvalidate();
	}
	public void setOnClosedListener(OnClickListener alisenter){
		tv_close.setOnClickListener(alisenter);
	}
	public void dismiss(){
		this.setVisibility(View.GONE);
	}
	public void show(){
		this.setVisibility(View.VISIBLE);
	}
	
	
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == MESSAGE_GETADDOK) {
				tv_location.setText((String) msg.obj);
			} else if(msg.what == MESSAGE_GETADDFAIL){
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
