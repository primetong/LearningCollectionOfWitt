package com.evguard.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.evguard.customview.AppTitleBar;
import com.evguard.customview.AppTitleBar.OnTitleActionClickListener;
import com.evguard.customview.CustomTextButton;
import com.evguard.data.AppDataCache;
import com.evguard.main.Dg_Base.OnCancelListener;
import com.evguard.main.Dg_time.OnTimeSelectedListener;
import com.evguard.main.Thread_DownTrack.CallBack_DownTrack;
import com.evguard.model.ICommonWebResponse;
import com.evguard.model.TrackInfo;
import com.evguard.model.WebReq_GetTrackPlay;
import com.evguard.model.WebReq_GetTrackPlayCount;
import com.evguard.model.WebRes_GetTrackPlay;
import com.evguard.model.WebRes_GetTrackPlayCount;
import com.evguard.tools.ConstantTool;
import com.evguard.tools.LogEx;
import com.xinghaicom.asynchrony.LoopHandler;
import com.xinghaicom.evguard.R;

public class AC_TrackHistoryInfoSet extends AC_BaseLogined implements
		 OnTimeSelectedListener {

	private AppTitleBar app_title = null;
	private CustomTextButton ctb_CarNum = null;
//	private CustomTextButton ctb_Fasttime = null;
	private CustomTextButton ctb_Begintime = null;
	private CustomTextButton ctb_EndTime = null;
	private Button btn_ok = null;
	private boolean mIsBeginTime = true;

	private Calendar mCalendar = null;
	private SimpleDateFormat format = null;

	private String mCarnum = "";
	private String mBegintime = "";
	private String mEndtime = "";
	private String speedvalue = "2";

	private App_Settings mSettings = null;

	private static final int MESSAGE_UI_WAIT = 0;
	private static final int MESSAGE_UI_END = 1;
	private static final int MESSAGE_UI_OK = 2;
	private static final int MESSAGE_PROGRESSING = 3;
	private static final int MESSAGE_PROGRESSED= 4;
	private static final int MESSAGE_PROGRESS_FAIL= 5;
	private static final int MESSAGE_PROGRESSED_ONE= 6;
	protected static final String TAG = "AC_TrackHistoryInfoSet";
	// private static final int MESSAGE_GETTRACK_FAILED=2;
	private Dg_Waiting mDownDialog = null;
	private boolean mIsCanChooseCar = true;
	public int mPageSize = 0;
	public int mTotalCount = 0;

	private WebRequestThreadEx<WebRes_GetTrackPlayCount> mWebRequestThreadEx = null;
	private Dg_Progress mDownProgress;
	private Thread_DownTrack mThread_DownTrack;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_trackhistoryinfoset);
		mSettings = new App_Settings(mContext);
		findViews();
		initView();
		handleListener();
		initThread();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogEx.i("==BACKK==", "BACKK");
		switch (requestCode) {
		
		}
	}

	@Override
	public void handleMsg(Message msg) {
		//TODO
		switch (msg.what) {
		case MESSAGE_UI_WAIT:
			mDownProgress = Dg_Progress.newInstance("轨迹下载", "正在获取轨迹信息", null, null);
			mDownProgress.show(getSupportFragmentManager(), "");
			break;
			
		case MESSAGE_UI_OK:
			if(mDownProgress != null){
				mDownProgress.setMessage("开始下载轨迹数据" );
				Bundle bundle = msg.getData();
				mDownProgress.setMax(bundle.getInt("TrackCount"));
				mDownProgress.setProgress(0);
				mThread_DownTrack.getTrack(bundle.getInt("PageCount"));
			}
			break;
		case MESSAGE_UI_END:
			if(mDownProgress != null){
				mDownProgress.dismiss();
				mDownProgress = null;
			}
			if (msg.obj != null) {
				String showText = msg.obj.toString();
				showToast(showText);
			}
			break;
			
		case MESSAGE_PROGRESSING:
			if(mDownProgress != null){
				mDownProgress.setMessage("正在下载轨迹数据" );
				mDownProgress.setProgress(0);
			}
			break;
		
		case MESSAGE_PROGRESS_FAIL:
			if (mDownProgress != null) {
				mDownProgress.dismiss();
				mDownProgress = null;
			}
			if (msg.obj != null) {
				String showText = msg.obj.toString();
				showToast(showText);
			}
			break;
		case MESSAGE_PROGRESSED:
			if(mDownProgress != null){
				mDownProgress.setMessage("轨迹下载完成" );
				mDownProgress.setCompleted();
				mDownProgress.dismiss();
			}
			Intent t = new Intent("com.evguard.main.activity.trackhistory");
			startActivity(t);
			break;
		
		case MESSAGE_PROGRESSED_ONE:
			if(mDownProgress != null){
				if(msg.obj != null)
				mDownProgress.setProgress((Integer)msg.obj);
			}
			break;
		}
	}

	private void getIntentData() {
	}

	private void findViews() {
		app_title = (AppTitleBar) findViewById(R.id.app_title);
		ctb_CarNum = (CustomTextButton) findViewById(R.id.ctb_carnum);
//		ctb_Fasttime = (CustomTextButton) findViewById(R.id.ctb_fasttime);
		ctb_Begintime = (CustomTextButton) findViewById(R.id.ctb_begintime);
		ctb_EndTime = (CustomTextButton) findViewById(R.id.ctb_endtime);
		btn_ok = (Button) findViewById(R.id.btn_ok);
	}

	private void initView() {
		try {
			String carNum = AppDataCache.getInstance().getCurrentKidCurPositionInfo().getCarNum();
			if(carNum == null || carNum.equals(""))
				mCarnum="0";
			else
				mCarnum=carNum;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mCalendar = Calendar.getInstance();
		format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
			mIsCanChooseCar = true;
			mEndtime = format.format(mCalendar.getTime());
			mCalendar.set(Calendar.HOUR_OF_DAY, 0);
			mCalendar.set(Calendar.MINUTE, 0);
			mBegintime = format.format(mCalendar.getTime());
			
			
		ctb_CarNum.setTextContent(mCarnum);
		ctb_CarNum.setTextDrawUnShow();
//		ctb_Fasttime.setTextContent("今天");
		ctb_Begintime.setTextContent(mBegintime);
		ctb_EndTime.setTextContent(mEndtime);
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
						AC_TrackHistoryInfoSet.this.finish();
					}

					@Override
					public void onRightOperateClick() {

					}

					@Override
					public void onTitleClick() {
						
					}

				});
		if (!mIsCanChooseCar) {
			ctb_CarNum.setTextContentNotClick();
		}
		ctb_CarNum.setTextContentOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}

		});
//		ctb_Fasttime.setTextContentOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Dg_TimeFast aDg_TimeFast = Dg_TimeFast.newInstance();
//				aDg_TimeFast.show(getSupportFragmentManager(), "");
//			}
//
//		});
		ctb_Begintime.setTextContentOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mIsBeginTime = true;
				System.out.println("beginTime==" + ctb_Begintime
						.getTextContent());
				Dg_time aDg_time = Dg_time.newInstance(ctb_Begintime
						.getTextContent());
				aDg_time.show(getSupportFragmentManager(), "");
			}

		});
		ctb_EndTime.setTextContentOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mIsBeginTime = false;
				System.out.println("endTime==" + ctb_EndTime
						.getTextContent());
				Dg_time bDg_time = Dg_time.newInstance(ctb_EndTime
						.getTextContent());
				bDg_time.show(getSupportFragmentManager(), "");
			}

		});
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!checkData()) {
					return;
				}
				getTrack();
			}

		});
	}

	private void setBeginandEndTime(Calendar aCalendar) {
		aCalendar.set(Calendar.HOUR_OF_DAY, 23);
		aCalendar.set(Calendar.MINUTE, 59);
		mEndtime = format.format(aCalendar.getTime());
		aCalendar.set(Calendar.HOUR_OF_DAY, 0);
		aCalendar.set(Calendar.MINUTE, 0);
		mBegintime = format.format(aCalendar.getTime());
	}

//	@Override
//	public void onFastTimeSelected(int key, String value) {
//		if (key == -3) {
//			return;
//		} else {
//			mCalendar = Calendar.getInstance();
//			mCalendar.add(Calendar.DAY_OF_YEAR, key);
//			setBeginandEndTime(mCalendar);
//		}
//		ctb_Fasttime.setTextContent(value);
//		ctb_Begintime.setTextContent(mBegintime);
//		ctb_EndTime.setTextContent(mEndtime);
//	}

	@Override
	public void onTimeSelected(String stime) {
		if (mIsBeginTime) {
			mBegintime = stime;
			ctb_Begintime.setTextContent(mBegintime);
		} else {
			mEndtime = stime;
			ctb_EndTime.setTextContent(mEndtime);
		}
//		ctb_Fasttime.setTextContent("自定义");
	}

	private boolean checkData() {
		try {
			Date begin = format.parse(mBegintime);
			Date end = format.parse(mEndtime);
			long day = (end.getTime() - begin.getTime())
					- ConstantTool.TRACKMAXDAY * (24 * 60 * 60 * 1000);
			if (day > 0) {
				showToast(String.format("不能超过%s天！请重新选择时间！",
						ConstantTool.TRACKMAXDAY));
				return false;
			}
			if (end.getTime() - begin.getTime() < 0) {
				showToast("结束时间不能小于开始时间！");
				return false;
			}
			return true;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}

	private void initThread() {
		mThread_DownTrack = new Thread_DownTrack(mContext, new LoopHandler() {
			@Override
			protected void onException(Exception e) {
				super.onException(e);
			}
			
			@Override
			protected void onLooped() {
				super.onLooped();
			}
		}, new CallBack_DownTrack() {
			//TODO
			@Override
			public void onDownTrackInfoOneOk(int hasDownSize, int ipage) {
				Message Msg = mHandler.obtainMessage(MESSAGE_PROGRESSED_ONE);
				Msg.obj = hasDownSize;
				mHandler.sendMessage(Msg);
				
			}
			
			@Override
			public void onDownTrackInfoOk(WebRes_GetTrackPlay aWebRes_GetTrackPlay,int totalLen) {
				Message Msg = mHandler.obtainMessage(MESSAGE_PROGRESSED);
				mHandler.sendMessage(Msg);
				Log.i(TAG, "totalLen--" + totalLen);
				if(totalLen == 0) return;
				AppDataCache.getInstance().setTrackCount(totalLen);
				try {
					List<TrackInfo> trackInfo = aWebRes_GetTrackPlay.getTrackInfo();
					if(trackInfo != null && trackInfo.size() > 0)
						AppDataCache.getInstance().setTrackList(trackInfo);
					AppDataCache.getInstance().setAddressList(aWebRes_GetTrackPlay.getAddressList());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void onDownTrackInfoIng() {
				Message Msg = mHandler.obtainMessage(MESSAGE_PROGRESSING);
				mHandler.sendMessage(Msg);
			}
			
			@Override
			public void onDownTrackInfoFalied(String smsg, int ipage) {
				Message Msg = mHandler.obtainMessage(MESSAGE_PROGRESS_FAIL);
				Msg.obj = "下载第" + ipage +"页轨迹失败，" + smsg;
				mHandler.sendMessage(Msg);
			}
			
			@Override
			public void onDownTrackInfoException(String e, int ipage) {
				Message Msg = mHandler.obtainMessage(MESSAGE_PROGRESS_FAIL);
				Msg.obj = "下载第" + ipage +"页轨迹异常，" + e;
				mHandler.sendMessage(Msg);
				
			}
			
			@Override
			public void onDownTrackCountOk(int iTrackCount, int ipagecount) {
				Message endMsg = mHandler.obtainMessage(MESSAGE_UI_OK);
				
				Bundle bundle = new Bundle();
				bundle.putInt("TrackCount", iTrackCount);
				bundle.putInt("PageCount", ipagecount);
				endMsg.setData(bundle);
				mHandler.sendMessage(endMsg);
			}
			
			@Override
			public void onDownTrackCountIng() {
				
				Message Msg = mHandler.obtainMessage(MESSAGE_UI_WAIT);
				mHandler.sendMessage(Msg);
				
			}
			
			@Override
			public void onDownTrackCountFailed(String smsg) {
				Message endMsg = mHandler.obtainMessage(MESSAGE_UI_END);
				endMsg.obj = "获取轨迹失败，" + smsg;
				mHandler.sendMessage(endMsg);
			}
			
			@Override
			public void onDowmTrackCountException(Exception e) {
				Message endMsg = mHandler.obtainMessage(MESSAGE_UI_END);
				endMsg.obj = "获取轨迹信息异常，" + e.getMessage();
				mHandler.sendMessage(endMsg);
			}
		});
		mThread_DownTrack.start();
	}
	
	private void getTrack() {
		System.out.println("mBegintime==" + mBegintime + ", mEndtime==" + mEndtime);
		mThread_DownTrack.setTrackDate(mBegintime, mEndtime);
		mThread_DownTrack.getTrackCount();	
	}
}

/*
 Message Msg = mHandler.obtainMessage(MESSAGE_UI_WAIT);
		mHandler.sendMessage(Msg);
		WebReq_GetTrackPlayCount aWebReq_GetTrackPlayCount = new WebReq_GetTrackPlayCount(mBegintime,mEndtime);
	
		ICommonWebResponse<WebRes_GetTrackPlayCount> aICommonWebResponse = new ICommonWebResponse<WebRes_GetTrackPlayCount>() {
			

			@Override
			public void WebRequstSucess(WebRes_GetTrackPlayCount aWebRes) {
				if (aWebRes.getResult().equals("0")) {
					mTotalCount = Integer.parseInt(aWebRes.getTotalCount());
					AppDataCache.getInstance().setTrackCount(mTotalCount);
					mPageSize = Integer.parseInt(aWebRes.getPageSize());
//					AppDataCache.getInstance().setTrackList(aWebRes.getTrackInfo());
					// 关闭等待界面
					Message endMsg = mHandler.obtainMessage(MESSAGE_UI_END);
					mHandler.sendMessage(endMsg);
					
					getTrackInfo();
					
				} else {
					Message endMsg = mHandler.obtainMessage(MESSAGE_UI_END);
					endMsg.obj = aWebRes.getMessage();
					mHandler.sendMessage(endMsg);
				}
			}

			@Override
			public void WebRequestException(String ex) {
				// TODO Auto-generated method stub
				Message endMsg = mHandler.obtainMessage(MESSAGE_UI_END);
				endMsg.obj = "ex:获取轨迹失败！.";
				mHandler.sendMessage(endMsg);
			}

			@Override
			public void WebRequsetFail(String sfalied) {
				// TODO Auto-generated method stub
				Message endMsg = mHandler.obtainMessage(MESSAGE_UI_END);
				endMsg.obj = "falied:获取轨迹失败！" + sfalied;
				mHandler.sendMessage(endMsg);
			}
		};
		mWebRequestThreadEx = new WebRequestThreadEx<WebRes_GetTrackPlayCount>(
				aWebReq_GetTrackPlayCount, aICommonWebResponse,
				new WebRes_GetTrackPlayCount());
		new Thread(mWebRequestThreadEx).start();
	}

	protected void getTrackInfo() {
		Message Msg = mHandler.obtainMessage(MESSAGE_PROGRESSING);
		mHandler.sendMessage(Msg);
		int pageIndex = 1;
		WebReq_GetTrackPlay aWebReq_GetTrackPlay = new WebReq_GetTrackPlay(mBegintime,mEndtime,pageIndex+"");
		ICommonWebResponse<WebRes_GetTrackPlay> aICommonWebResponse = new ICommonWebResponse<WebRes_GetTrackPlay>() {

			@Override
			public void WebRequestException(String ex) {
				Message Msg = mHandler.obtainMessage(MESSAGE_PROGRESS_FAIL);
				mHandler.sendMessage(Msg);
			}

			@Override
			public void WebRequsetFail(String sfalied) {
				Message Msg = mHandler.obtainMessage(MESSAGE_PROGRESS_FAIL);
				mHandler.sendMessage(Msg);
			}

			@Override
			public void WebRequstSucess(WebRes_GetTrackPlay aWebRes) {
				Message Msg = mHandler.obtainMessage(MESSAGE_PROGRESSED);
				Msg.obj = 10;
				mHandler.sendMessage(Msg);
				
//				Intent t = new Intent("com.evguard.main.activity.trackhistory");
//				startActivity(t);
			}
			
		};
		WebRequestThreadEx<WebRes_GetTrackPlay> mWebRequestThreadEx = new WebRequestThreadEx<WebRes_GetTrackPlay>(
				aWebReq_GetTrackPlay, aICommonWebResponse,
				new WebRes_GetTrackPlay());
		new Thread(mWebRequestThreadEx).start();
	}
	*/
