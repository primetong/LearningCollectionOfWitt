package com.evguard.main;

import android.content.Context;

import com.evguard.model.WebReq_GetCurrentPos;
import com.evguard.model.WebRes_GetCurrentPos;
import com.xinghaicom.asynchrony.LoopHandler;

public class Thread_GetLastPosition extends Thread_Base {

	private CallBack_GetLastPosition mCallBack_GetLastPosition=null;

	private final int ACTION_GETCURRENTPOSITION=0;
	public Thread_GetLastPosition(Context context, LoopHandler loopHandler,
			CallBack_GetLastPosition aCallBack_GetLastPosition) {
		super(context, loopHandler);
		if (context == null)
			throw new NullPointerException("Context无效");
		mCallBack_GetLastPosition = aCallBack_GetLastPosition;
		
	}
	public void getCurrentPostion(){
		WebReq_GetCurrentPos aWebReq_GetCurrentPos = new WebReq_GetCurrentPos();
		super.request(ACTION_GETCURRENTPOSITION, aWebReq_GetCurrentPos);
	}
	@Override
	protected void onRunning(int action, Object obj) {
		if(action==ACTION_GETCURRENTPOSITION){
			getCurrentPostion_web((WebReq_GetCurrentPos)obj);
		}
	}
	private void getCurrentPostion_web(WebReq_GetCurrentPos aWebReq_GetCurrentPos){
		WebRes_GetCurrentPos aWebRes_GetCurrentPos=new WebRes_GetCurrentPos();
		try {
			super.executeWithConfirmLogon(aWebReq_GetCurrentPos, aWebRes_GetCurrentPos);
			if(mCallBack_GetLastPosition==null)return;
			if(aWebRes_GetCurrentPos.getResult().equals("0")){
				mCallBack_GetLastPosition.getLastPositionOK(aWebRes_GetCurrentPos);
			}else{
				mCallBack_GetLastPosition.getLastPostionFailed(aWebRes_GetCurrentPos.getMessage());
			}
		} catch (Exception e) {
			
			e.printStackTrace();
			mCallBack_GetLastPosition.getLastPostionFailed(e.getMessage());
		}
		
	}
	public interface CallBack_GetLastPosition{
		public void getLastPositionOK(WebRes_GetCurrentPos aWebRes_GetCurrentPos);
		public void getLastPostionFailed(String sError);
	}
}
