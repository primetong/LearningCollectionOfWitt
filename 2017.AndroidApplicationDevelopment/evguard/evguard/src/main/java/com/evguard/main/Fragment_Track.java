package com.evguard.main;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.evguard.bmap.Fragment_RealMap;
import com.evguard.bmap.MapFragment_Base.OnMapEventListener;
import com.evguard.customview.View_Location_CarPositionInfo;
import com.evguard.data.AppDataCache;
import com.evguard.main.Thread_GetLastPosition.CallBack_GetLastPosition;
import com.evguard.model.CarCurPositionInfo;
import com.evguard.model.Position_Base;
import com.evguard.model.WebRes_GetCurrentPos;
import com.evguard.tools.ConstantTool;
import com.xinghaicom.asynchrony.LoopHandler;
import com.xinghaicom.evguard.R;

public class Fragment_Track extends Fragment_Base {

	private String TAG = "Fragment_StartPage";
	private static Fragment_Track mInstance;
	private static Object lockobj = new Object();
	private Fragment_RealMap mFragment_RealMap=null;
	private LinearLayout ll_map;
	private LinearLayout ll_zoom;

	private View_Location_CarPositionInfo mCarPostionInfoView=null;
	private TextView tv_tip;
	private ImageView img_maplayer;
//	private ImageView img_traffic;
	private ImageView img_zoomin;
	private ImageView img_zoomout;
	public MapView mGPSMap;

	private double mStartupMinLon = Double.NaN;
	private double mStartupMinLat = Double.NaN;
	private double mStartupMaxLon = Double.NaN;
	private double mStartupMaxLat = Double.NaN;

	

//	private AMap mAmap;
	private Handler mHandler;
	private Message msg;
	private TimerTask mRealtimeCarShowing = null;
	private Timer mRealtimeShowingTimer = null;
	private boolean isTraffic = false;
	private int iCurMap = 0;
	private boolean mMapUsed = false;

	private CarCurPositionInfo mKidCurPositionInfo;
	private CallBack_FragmentTrack mCallBack_FragmentTrack;
	
	private Thread_GetLastPosition mThread_GetLastPosition=null;


	private static final int MESSAGE_DOWNINGDATA = 0;// 正在下载末次位置信息
	private static final int MESSAGE_DOWNEDDATA = 1;// 末次位置下载完成

	private static final int MESSAGE_ADDKID_ONMAP = 8;// 添加孩子头像到地图
	private static final int MESSAGE_SET_CAR_AS_CENTER = 9;// 设置所有车辆在可见范围
//	private static final int MESSAGE_LOCATINGCAR = 10;
//	private static final int MESSAGE_LOCATECAR_OK = 11;
//	private static final int MESSAGE_LOCATECAR_FALIED = 12;
//	private static final int MESSAGE_LOCATECAR_EXCEPTION = 13;

	private final static int MESSAGE_GETCARINFOFAILED = 14;// 获取孩子信息失败
	private final static int MESSAGE_GETCARINFOEXCEPTION = 15;// 获取孩子信息异常

	String errMsg = "";
	private ImageView iv_locate;
	private boolean mIsHidden;

	public static Fragment_Track getInstance() {
		if (mInstance == null)
			synchronized (lockobj) {
				mInstance = new Fragment_Track();
			}
		return mInstance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layoutView = inflater.inflate(R.layout.fragment_track,
				container, false);
		initMap(savedInstanceState);
		initHandler();
		findView();
		return layoutView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {	
		super.onActivityCreated(savedInstanceState);
		
		
		loadListener();
//		if (AppDataCache.getInstance().getKidCurPositionInfoList() != null
//				&& AppDataCache.getInstance().getKidCurPositionInfoList()
//						.size() > 0) {
//			setMapBounds(mStartupMinLon, mStartupMinLat, mStartupMaxLon,
//					mStartupMaxLat);
//		}
		
	}

	@Override
	public void onStop() {
		
		super.onStop();
//		if (mRealtimeCarShowing != null)
//			mRealtimeCarShowing.cancel();
//		mRealtimeCarShowing = null;
	}

	@Override
	public void onResume() {
		
		super.onResume();
		if(mIsHidden){
			return;
		}
		if(mRealtimeShowingTimer == null)
			mRealtimeShowingTimer = new Timer();
		if(mThread_GetLastPosition ==null){
			System.out.println("120---here");
			initThread();
		}
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if(hidden){
			mIsHidden = true;
			if (mRealtimeCarShowing != null){
				mRealtimeCarShowing.cancel();
				mRealtimeCarShowing= null;
				
			}
			if (mRealtimeShowingTimer != null){
				mRealtimeShowingTimer.cancel();
				mRealtimeShowingTimer = null;
			}
			if(mThread_GetLastPosition!=null){
				System.out.println("121---here");
				mThread_GetLastPosition.interrupt();
				mThread_GetLastPosition = null;
			}
		}else{
			mIsHidden = false;
			if(mRealtimeShowingTimer == null)
				mRealtimeShowingTimer = new Timer();
			if(mThread_GetLastPosition ==null){
				System.out.println("120---here");
				initThread();
			}
		}
		
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if(mIsHidden){
			return;
		}
		if (mRealtimeCarShowing != null){
			mRealtimeCarShowing.cancel();
			mRealtimeCarShowing= null;
			
		}
		if (mRealtimeShowingTimer != null){
			mRealtimeShowingTimer.cancel();
			mRealtimeShowingTimer = null;
		}
		if(mThread_GetLastPosition!=null){
			System.out.println("122---here");
			mThread_GetLastPosition.interrupt();
			mThread_GetLastPosition = null;
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		mInstance=null;
	}

	public void setCallBack_FragmentTrack(
			CallBack_FragmentTrack aCallBack_FragmentTrack) {
		this.mCallBack_FragmentTrack = aCallBack_FragmentTrack;
	}

	private void findView() {
		ll_map = (LinearLayout) layoutView.findViewById(R.id.ll_map);
		mCarPostionInfoView=(View_Location_CarPositionInfo)layoutView.findViewById(R.id.view_carpostioninfo);
//		tv_tip = (TextView) layoutView.findViewById(R.id.tv_tip);
//		tv_tip.getBackground().setAlpha(180);
//		ll_maplayer = (LinearLayout) layoutView.findViewById(R.id.ll_maplayer);
		ll_zoom = (LinearLayout) layoutView.findViewById(R.id.ll_zoom);
	
		img_maplayer = (ImageView) layoutView.findViewById(R.id.img_maplayer);
//		img_traffic = (ImageView) layoutView.findViewById(R.id.img_transport);
		img_zoomout = (ImageView) layoutView.findViewById(R.id.img_zoomout);
		img_zoomin = (ImageView) layoutView.findViewById(R.id.img_zoomin);
		iv_locate = (ImageView) layoutView.findViewById(R.id.iv_locate);
	
	}

	private void initMap(Bundle savedInstanceState) {
		if(mFragment_RealMap == null){
			mFragment_RealMap=Fragment_RealMap.newInstance();
			this.getChildFragmentManager().beginTransaction().replace(R.id.ll_map, mFragment_RealMap).commit();
			mFragment_RealMap.setOnMapEventListener(new OnMapEventListener(){
				
				@Override
				public void onMapLoaded() {
					
				}
				
				@Override
				public void onMarkerClick(Position_Base aPosition_Base) {
					CarCurPositionInfo aCarCurPositionInfo=(CarCurPositionInfo)aPosition_Base;
					mCarPostionInfoView.setData(aCarCurPositionInfo);
					mCarPostionInfoView.show();
					mFragment_RealMap.setCenter(aCarCurPositionInfo.getBDLatlng());
				}
				
			});
		}
	}

	private void initHandler() {

		mHandler = new Handler() {
//			private boolean isShow = true;

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case MESSAGE_DOWNINGDATA:
					
//					tv_tip.setVisibility(View.VISIBLE);
					
					break;
				case MESSAGE_DOWNEDDATA:
//					tv_tip.setVisibility(View.GONE);
					if(msg.obj!=null && msg.obj instanceof String ){
						errMsg = "获取当前车辆位置信息失败," + (String) msg.obj;
						 AppDataCache.getInstance().setCurrentKidCurPositionInfo(null);
//						 mFragment_RealMap.clearAll();
						showToast(errMsg);
					}
					break;
				case MESSAGE_ADDKID_ONMAP:// 添加marker
					if (msg.obj != null
							&& msg.obj instanceof CarCurPositionInfo) {
						CarCurPositionInfo aCarCurPositionInfo = (CarCurPositionInfo) msg.obj;
						CarCurPositionInfo positionInfo = AppDataCache.getInstance().getCurrentKidCurPositionInfo();
						if(!aCarCurPositionInfo.getCarNum().equals("")){
							if(positionInfo == null || !positionInfo.getBDLatlng().equals(aCarCurPositionInfo.getBDLatlng())){
								mFragment_RealMap.clearAll();
								mFragment_RealMap.drawCar(aCarCurPositionInfo);
							} 
						}
						
						AppDataCache.getInstance().setCurrentKidCurPositionInfo(aCarCurPositionInfo);
					}
					break;
				default:
					break;

				}
				super.handleMessage(msg);
			}
		};
	}

	private void initTimerTask() {
		if(mRealtimeCarShowing == null){
			mRealtimeCarShowing = new TimerTask() {
				@Override
				public void run() {
					mHandler.sendEmptyMessage(MESSAGE_DOWNINGDATA);
					mThread_GetLastPosition.getCurrentPostion();
				}
			};
			mRealtimeShowingTimer.schedule(mRealtimeCarShowing, 0, ConstantTool.REALTIMEPOSITION_SPAN);
		}
	}

	private void initThread(){
		mThread_GetLastPosition=new Thread_GetLastPosition(mContext,new LoopHandler(){
			protected void onException(Exception e){}
			protected void onLooped(){
				initTimerTask();
			}
		}, new CallBack_GetLastPosition(){

			@Override
			public void getLastPositionOK(WebRes_GetCurrentPos aWebRes) {

					handlePositionData(aWebRes);
					mHandler.sendEmptyMessage(MESSAGE_DOWNEDDATA);
				if (mCallBack_FragmentTrack != null){}
//					mCallBack_FragmentTrack.onRealTimeCarLoadOK();
			}
			@Override
			public void getLastPostionFailed(String sError) {
				msg = mHandler.obtainMessage();
				msg.what = MESSAGE_DOWNEDDATA;
				msg.obj = sError;
				mHandler.sendMessage(msg);
			}
			
		});
		mThread_GetLastPosition.start();
		
	}
	private void loadListener() {
		mCarPostionInfoView.setOnClosedListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				mCarPostionInfoView.dismiss();
			}
			
		});
		img_maplayer.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent ttrack=new Intent("com.evguard.main.trackhistoryinfoset");
				startActivity(ttrack);
			}
		});
//		img_traffic.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				isTraffic = !isTraffic;
//				mGPSMap.getMap().setTrafficEnabled(isTraffic);
//				if (isTraffic) {
//					img_traffic.setImageDrawable(mContext.getResources()
//							.getDrawable(R.drawable.icon_roadcondition_on));
//				} else {
//					img_traffic.setImageDrawable(mContext.getResources()
//							.getDrawable(R.drawable.icon_roadcondition_off));
//				}
//			}
//
//		});
		img_zoomout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				
				if (!mFragment_RealMap.isMinZoom()) {
					mFragment_RealMap.zoomOut();
				} else {
					showToast("已经达到最小级别");
				}
			}

		});
		img_zoomin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				if (!mFragment_RealMap.isMaxZoom()) {
					mFragment_RealMap.zoomIn();
				} else {
					showToast("已经达到最大级别");
				}
			}

		});
		
		iv_locate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mFragment_RealMap != null) {
					try {
						LatLng bdLatlng = AppDataCache.getInstance().getCurrentKidCurPositionInfo().getBDLatlng();
						if(bdLatlng != null)
							mFragment_RealMap.setCenter(bdLatlng);
						else
							return;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	
	}

	private void handlePositionData(WebRes_GetCurrentPos aWebRes){
		CarCurPositionInfo aCarCurPositionInfo = new CarCurPositionInfo();
		aCarCurPositionInfo.setCarNum(aWebRes.getCarNum());
		aCarCurPositionInfo.setGpsTime(aWebRes.getGpsTime());
		aCarCurPositionInfo.setBaiduLatlng(aWebRes.getLatitude(), aWebRes.getLongitude());
		aCarCurPositionInfo.setLatitude(aWebRes.getLatitude());
		aCarCurPositionInfo.setLongitude(aWebRes.getLongitude());
		aCarCurPositionInfo.setDirection(aWebRes.getDirection());
		aCarCurPositionInfo.setMileage(aWebRes.getMileage());
		aCarCurPositionInfo.setSpeed(aWebRes.getSpeed());
		Message msg=mHandler.obtainMessage();
		msg.obj=aCarCurPositionInfo;
		msg.what=MESSAGE_ADDKID_ONMAP;
		mHandler.sendMessage(msg);
		AppDataCache.getInstance().setKidCurPositionInfoList(aCarCurPositionInfo);
		
	}

	protected void setMapBounds(double minLon, double minLat, double maxLon,
			double maxLat) {
		Map<String, Double> extent = new HashMap<String, Double>();
		extent.put("minLon", minLon);
		extent.put("minLat", minLat);
		extent.put("maxLon", maxLon);
		extent.put("maxLat", maxLat);
		Message extentSettingMsg = mHandler.obtainMessage(
				MESSAGE_SET_CAR_AS_CENTER, extent);
		mHandler.sendMessage(extentSettingMsg);
	}



	public interface CallBack_FragmentTrack {
	}



	@Override
	protected void restoreState(Bundle saveState) {
		CarCurPositionInfo aCarCurPositionInfo = (CarCurPositionInfo) saveState.getParcelableArrayList("carinfo").get(0);
		CarCurPositionInfo positionInfo = AppDataCache.getInstance().getCurrentKidCurPositionInfo();
		if(!aCarCurPositionInfo.getCarNum().equals("")){
			if(positionInfo == null || !positionInfo.getBDLatlng().equals(aCarCurPositionInfo.getBDLatlng())){
				mFragment_RealMap.clearAll();
				mFragment_RealMap.drawCar(aCarCurPositionInfo);
			} 
		}
		
		AppDataCache.getInstance().setCurrentKidCurPositionInfo(aCarCurPositionInfo);
	}

	@Override
	protected Bundle saveState(Bundle saveState) {
		saveState.putParcelableArrayList("carinfo", AppDataCache.getInstance().getKidCurPositionInfoList());
		return saveState;
	}
}
