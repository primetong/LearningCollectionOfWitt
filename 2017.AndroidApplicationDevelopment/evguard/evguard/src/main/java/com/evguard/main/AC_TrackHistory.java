package com.evguard.main;

import java.util.List;

import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.evguard.bmap.Fragment_TrackMap;
import com.evguard.bmap.MapFragment_Base.OnMapEventListener;
import com.evguard.customview.AppTitleBar;
import com.evguard.customview.AppTitleBar.OnTitleActionClickListener;
import com.evguard.customview.View_Track_CarPositionInfo;
import com.evguard.data.AppDataCache;
import com.evguard.model.AddressPoint;
import com.evguard.model.CarCurPositionInfo;
import com.evguard.model.Position_Base;
import com.evguard.model.TrackInfo;
import com.xinghaicom.evguard.R;

public class AC_TrackHistory extends AC_BaseLogined implements OnClickListener {
	private AppTitleBar app_title = null;
	private View_Track_CarPositionInfo mView_Track_CarPositionInfo = null;
	private List<TrackInfo> mTrackList=null;
	private List<AddressPoint> mAddressList = null; 
	private LinearLayout ll_map = null;
	private ImageView mImg_Location = null;
	private ImageView mImg_ZoomIn = null;
	private ImageView mImg_ZoomOut = null;
	private ImageView mPlay = null;
	private ImageView mStop = null;
	private ProgressBar mBar = null;
	private TextView mSpeed = null;
	private Fragment_TrackMap mFragment_TrackMap = null;
	private boolean mIsRunningFlag=true;

	private Thread mPlayTrack = null;
	private int iCurPlayTrackSpeed = 300;
	private int iCurTrackIndex = 0;
	private int mTrackcount = 0;
	private boolean mIsStopPlay = true;
	private Object lockObejct=new Object();


	private final int MESSAGE_UPDATEPROGRESSBAR = 0;
	private final int MESSAGE_UPDATECARPOSITIONINFO = 1;
	private final int MESSAGE_SHOWALLTRACK = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.ac_trackhistory);
			mTrackcount = AppDataCache.getInstance().getTrackCount();
			Log.i("llj", "mTrackcount=" + mTrackcount);
			mTrackList = AppDataCache.getInstance().getTrackList();
			mAddressList = AppDataCache.getInstance().getAddressList();
			findViews();
			initMap(savedInstanceState);
			handleListener();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mIsRunningFlag=false;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(keyCode==KeyEvent.KEYCODE_BACK){
			mIsRunningFlag=false;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void handleMsg(Message msg) {
		switch (msg.what) {
		case MESSAGE_UPDATEPROGRESSBAR:
			int ivalue = msg.arg1;
			if (ivalue >= mTrackcount) {
				ivalue = mTrackcount;
				synchronized (lockObejct) {
					iCurTrackIndex = 0;
					mIsStopPlay = true;
				}
				mPlay.setImageResource(R.drawable.icon_play);
			}
			mBar.setProgress(ivalue);
			break;
		case MESSAGE_UPDATECARPOSITIONINFO:
			int iIndex = msg.arg1;
			mFragment_TrackMap.showTrackCar(iIndex);
			mView_Track_CarPositionInfo.setData(mTrackList.get(iIndex));
			break;
		case MESSAGE_SHOWALLTRACK:
			mFragment_TrackMap.showAllTrack();
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.tv_playspeed:
			if (iCurPlayTrackSpeed == 300) {
				mSpeed.setText("速度X2");
				iCurPlayTrackSpeed = 200;
			} else if (iCurPlayTrackSpeed == 200) {
				mSpeed.setText("速度X3");
				iCurPlayTrackSpeed = 100;
			} else if (iCurPlayTrackSpeed == 100) {
				mSpeed.setText("速度X1");
				iCurPlayTrackSpeed = 300;
			}
			break;
		case R.id.img_play:
			mIsStopPlay = !mIsStopPlay; 
			Log.i("llj", "mIsStopPlay>>" + mIsStopPlay);
			if (mIsStopPlay) {
				mPlay.setImageResource(R.drawable.icon_play);
				mView_Track_CarPositionInfo.setIsCanQueryAddress(true);
			} else {
				mPlay.setImageResource(R.drawable.icon_pause);
				playTrack();
			}
			break;
		case R.id.img_stop:
			synchronized (lockObejct) {
				mIsStopPlay = true;
				iCurTrackIndex = 0;
			}
			mView_Track_CarPositionInfo.setVisibility(View.GONE);
			mPlay.setImageResource(R.drawable.icon_play);
			mBar.setProgress(0);
			Message msg = mHandler.obtainMessage();
			msg.what = MESSAGE_SHOWALLTRACK;
			mHandler.sendMessage(msg);
			break;
		}
	}

	private void findViews() {
		app_title = (AppTitleBar) findViewById(R.id.app_title);
		mView_Track_CarPositionInfo = (View_Track_CarPositionInfo) findViewById(R.id.view_carpostioninfo);
		ll_map=(LinearLayout) findViewById(R.id.ll_map);
		mPlay = (ImageView) findViewById(R.id.img_play);
		mStop = (ImageView) findViewById(R.id.img_stop);
		mSpeed = (TextView) findViewById(R.id.tv_playspeed);
		mBar = (ProgressBar) findViewById(R.id.pb_bar);
		mBar.setMax(mTrackcount);

		mPlay.setOnClickListener(this);
		mStop.setOnClickListener(this);
		mSpeed.setOnClickListener(this);

	}

	private void initMap(Bundle savedInstanceState) {

		try {
			mFragment_TrackMap=Fragment_TrackMap.newInstance();
			mFragment_TrackMap.setTrackList(mTrackList);
			mFragment_TrackMap.setOnMapEventListener(new OnMapEventListener() {
				
				@Override
				public void onMarkerClick(Position_Base aPosition_Base) {
					Message msg = mHandler.obtainMessage();
					msg.what = MESSAGE_SHOWALLTRACK;
					mHandler.sendMessage(msg);
				}
				
				@Override
				public void onMapLoaded() {
					CarCurPositionInfo currentKidCurPositionInfo = AppDataCache.getInstance().getCurrentKidCurPositionInfo();
					if(currentKidCurPositionInfo != null)
						mFragment_TrackMap.setCenter(currentKidCurPositionInfo.getBDLatlng());
					else {
						Location location = AppDataCache.getInstance().getLocation();
						if(location != null){
							mFragment_TrackMap.setCenter(new LatLng(location.getLatitude(), location.getLongitude()));
						}
					}
				}
			});
			FragmentManager manager=this.getSupportFragmentManager();
			manager.beginTransaction().replace(R.id.ll_map, mFragment_TrackMap).commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleListener() {
//		app_title.setTitleMode(AppTitleBar.APPTITLEBARMODE_TXTANDBACK, "轨迹回放", null);
		app_title.setTitleMode(null,
				mContext.getResources().getDrawable(R.drawable.icon_back),
				"轨迹回放",false,
				null,null);
		app_title
				.setOnTitleActionClickListener(new OnTitleActionClickListener() {

					@Override
					public void onLeftOperateClick() {
						mIsRunningFlag=false;
						AC_TrackHistory.this.finish();
					}

					@Override
					public void onRightOperateClick() {
					}

					@Override
					public void onTitleClick() {
						// TODO Auto-generated method stub
						
					}
				});
		mView_Track_CarPositionInfo.setOnShowAddListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mView_Track_CarPositionInfo.queryAddress();
			}

		});
	}

	private void playTrack() {
		mIsStopPlay = false;
		mPlay.setImageResource(R.drawable.icon_pause);
		mView_Track_CarPositionInfo.setVisibility(View.VISIBLE);
		mView_Track_CarPositionInfo.setIsCanQueryAddress(false);
		mPlayTrack = new Thread(new Runnable() {
			@Override
			public void run() {
					
					while (iCurTrackIndex < mTrackcount && !mIsStopPlay) {
						try {
							if(!mIsRunningFlag)break;
							synchronized (lockObejct) {
							Message msg = mHandler.obtainMessage();
							msg.what = MESSAGE_UPDATECARPOSITIONINFO;
							msg.arg1 = iCurTrackIndex;
							mHandler.sendMessage(msg);
							iCurTrackIndex++;
							Message msg2 = mHandler.obtainMessage();
							msg2.what = MESSAGE_UPDATEPROGRESSBAR;
							msg2.arg1 = iCurTrackIndex;
							mHandler.sendMessage(msg2);
							}
							Thread.sleep(iCurPlayTrackSpeed);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
		});
		mPlayTrack.start();
	}


}
