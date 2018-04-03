package com.evguard.version;

import android.os.Handler;
import android.os.Message;

public abstract class AppCheckingHandler extends Handler {

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		
		if(msg == null){
			onException(new NullPointerException("Message无效"));
			return;
		}
		
		CheckingAction action = CheckingAction.values()[msg.what];
		switch(action){
		case EXCEPTION:
			if(msg.obj == null || !(msg.obj instanceof Exception)){
				onException(new IllegalArgumentException("messaeg无效"));
				return;
			}
			
			onException((Exception)msg.obj);			
			break;
			
		case HAS_UNNECESSARY_NEW:			
			if(msg.obj == null || !(msg.obj instanceof AppVersion)){
				onException(new Exception("Version无效"));
				break;
			}
															
			onHasUnNecessaryNew((AppVersion)msg.obj);			
			break;			
									
		case HAS_NECESSARY_NEW:
			if(msg.obj == null || !(msg.obj instanceof AppVersion)){
				onException(new Exception("Version无效"));
				break;
			}
			
			onHasNecessaryNew((AppVersion)msg.obj);
			break;
			
		case HAS_NOT_NEW:
			onHasNotNew();
			break;
		case FALIED:
			onFalied();
			break;
		case MASSEGE_GETTING:
			onGetting();
			break;
		}	
	}
	
	public abstract void onException(Exception e);
	public abstract void onFalied();
	public abstract void onHasUnNecessaryNew(AppVersion newVersion);
	public abstract void onHasNecessaryNew(AppVersion newVersion);
	public abstract void onHasNotNew();	
	public abstract void onGetting();	
	
	public enum CheckingAction{
		EXCEPTION,
		FALIED,
		HAS_UNNECESSARY_NEW,
		HAS_NECESSARY_NEW,
		HAS_NOT_NEW,
		MASSEGE_GETTING
	}
}
