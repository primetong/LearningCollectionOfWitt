package com.evguard.main;

import java.io.EOFException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.conn.HttpHostConnectException;

import android.content.Context;
import android.util.Log;

import com.evguard.model.CarInfo;
import com.evguard.model.WebReq_GetTrackPlay;
import com.evguard.model.WebReq_GetTrackPlayCount;
import com.evguard.model.WebRes_GetTrackPlay;
import com.evguard.model.WebRes_GetTrackPlayCount;
import com.evguard.tools.LogEx;
import com.xinghaicom.asynchrony.LoopHandler;

public class Thread_DownTrack extends Thread_Base {

	private String TAG = "Thread_DownTrack";
	private SimpleDateFormat mSimpleDateFormat;
	private String starttime = "";
	private String endtime = "";
	private int mTrackCount = 0;
	private int mPageSize = 0;
	private int mTackPageCount = 0;
	private int mDefalutPerPageCount = 500;
	private int mInFactPerPageCount = 500;
	private int mHasDownSize = 0;
	private boolean isQuit = false;
	private CallBack_DownTrack mCallBack_DownTrack;
	private App_Settings mSettings;
	private CarInfo mKidBaseInfo;
	private Calendar mCalendar;

	private static final int GETTRACKCOUNT = 0;
	private static final int GETTRACKINFO = 1;

	public Thread_DownTrack(Context context, LoopHandler loopHandler,
			CallBack_DownTrack aCallBack_DownTrack) {

		super(context, loopHandler);
		mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		mCalendar = Calendar.getInstance();
		mSettings = new App_Settings(context);
		this.mCallBack_DownTrack = aCallBack_DownTrack;
	}

	public void setTrackDate(String startDate,String endData) {
		Date d_start = new Date();
		Date d_end = new Date();
		try {
			d_start = mSimpleDateFormat.parse(startDate + ":00");
			d_end = mSimpleDateFormat.parse(endData + ":00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("d_start==" + d_start + ", d_end==" + d_end);
		mCalendar.clear();
		mCalendar.setTime(d_start);
		starttime = mSimpleDateFormat.format(mCalendar.getTime());
		mCalendar.clear();
		mCalendar.setTime(d_end);
		endtime = mSimpleDateFormat.format(mCalendar.getTime());

	}

	public void setKidBaseInfo(CarInfo aKidBaseInfo) {
		mKidBaseInfo = aKidBaseInfo;
	}

	public void stopDown() {
		isQuit = true;
	}

	public void getTrackCount() {

		WebReq_GetTrackPlayCount aWebReq_GetTrackCount = new WebReq_GetTrackPlayCount(starttime,endtime);
		this.request(GETTRACKCOUNT, aWebReq_GetTrackCount);

	}

	public void getTrack(int pageCount) {

//		WebReq_GetTrackPlay aWebRes_GetTrackPlay = new WebReq_GetTrackPlay(starttime, endtime, pageIndex+"");

		this.request(GETTRACKINFO, pageCount);

	}

//	public void getTrack() {
//
//		this.request(GETTRACKINFO, null);
//
//	}

	@Override
	protected void onRunning(int action, Object obj) {
		Log.i("llj", "action>>>" + action);
		switch (action) {

		case GETTRACKCOUNT:
			WebReq_GetTrackPlayCount aWebReq_GetTrackCount = (WebReq_GetTrackPlayCount) obj;
			getTrackCount(aWebReq_GetTrackCount);
			break;
		case GETTRACKINFO:
			if(obj != null){
				getTrackInfo((Integer)obj);
			}else {
				
				getTrackInfo(1);
			}
			break;
		default:
			break;
		}
	}

	private void getTrackCount(WebReq_GetTrackPlayCount aWebReq_GetTrackCount) {
		if (null == mCallBack_DownTrack)			
			return;
		mCallBack_DownTrack.onDownTrackCountIng();
		WebRes_GetTrackPlayCount aWebRes_GetTrackCount = new WebRes_GetTrackPlayCount();
		try {
			LogEx.i(TAG, "trackcount==" + aWebReq_GetTrackCount.getParams());
			executeWithConfirmLogon(aWebReq_GetTrackCount, aWebRes_GetTrackCount);
			if (!aWebRes_GetTrackCount.getResult().equals("0")) {
				mCallBack_DownTrack.onDownTrackCountFailed(aWebRes_GetTrackCount
								.getMessage());
				return;
			}
			mTrackCount = Integer.parseInt(aWebRes_GetTrackCount.getTotalCount());
			mPageSize = Integer.parseInt(aWebRes_GetTrackCount.getPageSize());
			caculatePageInfo();
			mCallBack_DownTrack.onDownTrackCountOk(
					Integer.parseInt(aWebRes_GetTrackCount.getTotalCount()), mTackPageCount);
		} catch (EOFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mCallBack_DownTrack.onDowmTrackCountException(new Exception(
					"EOFException"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mCallBack_DownTrack.onDowmTrackCountException(new Exception(
					"获取轨迹总数异常：" + e.getMessage()));
		}
	}

	// private void getTrackInfo(WebReq_getcarcaretracklist
	// aWebReq_GetTrackInfo){
	// if(null==mCallBack_DownTrack)return;
	// mCallBack_DownTrack.onDownTrackInfoIng();
	// mWebClient=getWebClient(aWebReq_GetTrackInfo);
	// JSONObject result;
	// try {
	// result =
	// mWebClient.executeByPostForJsonWithGZipAndEntrpyt(aWebReq_GetTrackInfo.getParams());
	// if(result == null){
	// // mCallBack_DownTrack.onDownTrackInfoException(new
	// NullPointerException("获取轨迹失败：执行返回值无效"));
	// return;
	// }
	// WebRes_GetTrackInfo aWebRes_GetTrackInfo=new WebRes_GetTrackInfo();
	// aWebRes_GetTrackInfo.ParseJson(result);
	// if(!aWebRes_GetTrackInfo.getIsSuccess()){
	// mCallBack_DownTrack.onDownTrackCountFailed(aWebRes_GetTrackInfo.getCmdError());
	// return;
	// }
	// mCallBack_DownTrack.onDownTrackInfoOk();
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// // mCallBack_DownTrack.onDownTrackInfoException(e);
	// }
	// }
	private void getTrackInfo(int pageCount) {
		if (null == mCallBack_DownTrack)
			return;
		mCallBack_DownTrack.onDownTrackInfoIng();
		WebRes_GetTrackPlay aWebRes_GetTrackPlay = null;
		int totalLen = 0;
		for (int index = 1; index <= pageCount; index++) {
			if (isQuit)
				break;
			try {
				aWebRes_GetTrackPlay = new WebRes_GetTrackPlay();
//				 mInFactPerPageCount=(mTrackCount-mHasDownSize>mDefalutPerPageCount)?
//				 mDefalutPerPageCount:mTrackCount-mHasDownSize;
				WebReq_GetTrackPlay aWebReq_GetTrackPlay = new WebReq_GetTrackPlay(starttime, endtime, index+"");
				if (isQuit)
					break;
				executeWithConfirmLogon(aWebReq_GetTrackPlay, aWebRes_GetTrackPlay);
				if (isQuit)
					break;
				if (!aWebRes_GetTrackPlay.getResult().equals("0")) {
					mCallBack_DownTrack.onDownTrackInfoFalied(
							aWebRes_GetTrackPlay.getMessage(), index);
					return;
				}
				if (isQuit)
					break;
				totalLen += aWebRes_GetTrackPlay.getTrackInfo().size();
				mCallBack_DownTrack.onDownTrackInfoOneOk(index*100/pageCount, index);
			} catch (HttpHostConnectException e) {

				e.printStackTrace();
				mCallBack_DownTrack.onDownTrackInfoException("与服务器连接中断，请检查网络！",
						index);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mCallBack_DownTrack.onDownTrackInfoException(
						"ex:获取轨迹失败：" + e.getMessage(), index);
			}
		}
		if (isQuit)
			return;
		
		 mCallBack_DownTrack.onDownTrackInfoOk(aWebRes_GetTrackPlay,totalLen);
	}

	private void caculatePageInfo() {
		mTackPageCount = mTrackCount % mPageSize == 0 ? mTrackCount
				/ mPageSize : mTrackCount / mPageSize + 1;
	}

	public interface CallBack_DownTrack {
		public void onDownTrackCountIng();

		public void onDownTrackCountOk(int iTrackCount, int ipagecount);

		public void onDownTrackCountFailed(String smsg);

		public void onDowmTrackCountException(Exception e);

		public void onDownTrackInfoIng();

		public void onDownTrackInfoOneOk(int hasDownSize, int ipage);

		public void onDownTrackInfoOk(WebRes_GetTrackPlay aWebRes_GetTrackPlay, int totalLen);

		public void onDownTrackInfoFalied(String smsg, int ipage);

		public void onDownTrackInfoException(String e, int ipage);

	}
}
