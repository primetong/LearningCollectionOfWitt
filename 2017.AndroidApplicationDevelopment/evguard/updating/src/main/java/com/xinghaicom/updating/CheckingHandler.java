package com.xinghaicom.updating;

import android.os.Handler;
import android.os.Message;

public abstract class CheckingHandler extends Handler {
	
	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		
		if(msg == null){
			onException(new NullPointerException("Message��Ч"));
			return;
		}
		
		CheckingAction action = CheckingAction.values()[msg.what];
		switch(action){
		case EXCEPTION:
			if(msg.obj == null || !(msg.obj instanceof Exception)){
				onException(new IllegalArgumentException("�쳣��Ϣ��Ч"));
				return;
			}
			
			onException((Exception)msg.obj);			
			break;
			
		case HAS_UNNECESSARY_NEW:			
			if(msg.obj == null || !(msg.obj instanceof Version)){
				onException(new Exception("Version��Ч"));
				break;
			}
															
			onHasUnNecessaryNew((Version)msg.obj);			
			break;			
									
		case HAS_NECESSARY_NEW:
			if(msg.obj == null || !(msg.obj instanceof Version)){
				onException(new Exception("Version��Ч"));
				break;
			}
			
			onHasNecessaryNew((Version)msg.obj);
			break;
			
		case HAS_NOT_NEW:
			onHasNotNew();
			break;
		}		
	}
	
	public abstract void onException(Exception e);
	public abstract void onHasUnNecessaryNew(Version newVersion);
	public abstract void onHasNecessaryNew(Version newVersion);
	public abstract void onHasNotNew();	
	
	public enum CheckingAction{
		EXCEPTION,
		HAS_UNNECESSARY_NEW,
		HAS_NECESSARY_NEW,
		HAS_NOT_NEW
	}
}
