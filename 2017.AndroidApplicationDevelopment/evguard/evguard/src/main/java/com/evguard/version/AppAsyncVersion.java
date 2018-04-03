package com.evguard.version;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.evguard.main.WebRequestThreadEx;
import com.evguard.model.ICommonWebResponse;
import com.evguard.model.WebReq_CheckVersion;
import com.evguard.model.WebRes_CheckVersion;

public class AppAsyncVersion {

	private static final int MESSAGE_GETTING = 0;
	protected boolean mCancel = false;
	protected Handler mCheckingHandler = null;
	protected AppVersion mNewVersion =null;
	private String mVerionName="";
	private Message msg;
	
	public AppAsyncVersion(
			AppCheckingHandler checkingHandler){
		mCheckingHandler=checkingHandler;
	}
	
	public void check() {
		if(mCheckingHandler != null){
			msg = mCheckingHandler.obtainMessage(AppCheckingHandler.CheckingAction.MASSEGE_GETTING.ordinal());			
			mCheckingHandler.sendMessage(msg);
		}
		WebReq_CheckVersion aWebReq_CheckVersion=new WebReq_CheckVersion();
		mVerionName=aWebReq_CheckVersion.getVersion();
		ICommonWebResponse<WebRes_CheckVersion> aICommonWebResponse=new ICommonWebResponse<WebRes_CheckVersion>(){
			@Override
			public void WebRequstSucess(WebRes_CheckVersion aWebRes) {
				 msg= Message.obtain();		
				if(aWebRes.getResult().equals("0"))
				{
					if(mCancel) return;
					
					if(aWebRes.getIsNeedUpdate().equals("0")){
						if(mCheckingHandler != null){
							
							msg = mCheckingHandler.obtainMessage(AppCheckingHandler.CheckingAction.HAS_NOT_NEW.ordinal());			
							mCheckingHandler.sendMessage(msg);
						}
										
					}else{
						boolean isMust = false;
						if(aWebRes.getIsNeedUpdate().equals("1")){
							isMust = false;
						} else if(aWebRes.getIsNeedUpdate().equals("2")) {
							isMust = true;
						}
						
						mNewVersion=new AppVersion();
						mNewVersion.setIsMustUpdate(isMust);
						mNewVersion.setNewVersion(aWebRes.getNewVersion());
						mNewVersion.setUpdateInfo(aWebRes.getUpdateInfo());
						mNewVersion.setUrl(aWebRes.getFileUrl());
						if(mCheckingHandler != null){
							
							msg.what = isMust?AppCheckingHandler.CheckingAction.HAS_NECESSARY_NEW.ordinal():
								AppCheckingHandler.CheckingAction.HAS_UNNECESSARY_NEW.ordinal();
							msg.obj =mNewVersion;
							mCheckingHandler.sendMessage(msg);					
						}
					}
				}else{
					 
					msg.what = AppCheckingHandler.CheckingAction.FALIED.ordinal();
					msg.obj ="检查更新失败！";
					mCheckingHandler.sendMessage(msg);	
				}
					
			}

			@Override
			public void WebRequestException(String ex) {
				// TODO Auto-generated method stub
				msg= Message.obtain();
				msg.what = AppCheckingHandler.CheckingAction.FALIED.ordinal();
				mCheckingHandler.sendMessage(msg);
			}

			@Override
			public void WebRequsetFail(String sfalied) {
				msg= Message.obtain();	
				msg.what = AppCheckingHandler.CheckingAction.FALIED.ordinal();
				msg.obj ="falied:检查更新失败！"+sfalied;
				mCheckingHandler.sendMessage(msg);
				
			}
		};
		WebRequestThreadEx<WebRes_CheckVersion> aWebRequestThreadEx=new WebRequestThreadEx<WebRes_CheckVersion>(aWebReq_CheckVersion,
				aICommonWebResponse,
				new WebRes_CheckVersion());
    	new Thread(aWebRequestThreadEx).start();
		
		
	}

	public void close(){		
		mCancel = true;		
	}
}
