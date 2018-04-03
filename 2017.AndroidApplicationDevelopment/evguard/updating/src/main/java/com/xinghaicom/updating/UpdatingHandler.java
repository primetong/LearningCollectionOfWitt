package com.xinghaicom.updating;

import java.io.File;
import java.util.Date;
import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * @deprecated 
 */
public abstract class UpdatingHandler extends Handler {
	
	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		
		if(msg == null){
			onException(new NullPointerException("Message无效"));
			return;
		}
		
		UpdatingAction action = UpdatingAction.values()[msg.what];
		switch(action){
		case EXCEPTION:
			if(msg.obj == null || !(msg.obj instanceof Exception)){
				onException(new IllegalArgumentException("异常信息无效"));
				return;
			}
			
			onException((Exception)msg.obj);			
			break;
			
		case HAS_UNNECESSARY_NEW:			
			if(msg.obj == null || !(msg.obj instanceof Version)){
				onException(new Exception("Version无效"));
				break;
			}
															
			onHasUnNecessaryNew((Version)msg.obj);			
			break;			
									
		case HAS_NECESSARY_NEW:
			if(msg.obj == null || !(msg.obj instanceof Version)){
				onException(new Exception("Version无效"));
				break;
			}
			
			onHasNecessaryNew((Version)msg.obj);
			break;
			
		case HAS_NOT_NEW:
			onHasNotNew();
			break;
			
		case DOWNLOADING:
			if(msg.obj == null || !(msg.obj instanceof Integer)){
				onException(new Exception("进度无效"));
				break;
			}
			
			onDownloading((Integer)msg.obj);
			break;
			
		case DOWNLOADED:
			if(msg.obj == null || !(msg.obj instanceof File)){
				onException(new Exception("File无效"));
				break;
			}
			
			onDownloaded((File)msg.obj);
			break;
		}				
	}
	
	public abstract void onException(Exception e);
	public abstract void onHasUnNecessaryNew(Version newVersion);
	public abstract void onHasNecessaryNew(Version newVersion);
	public abstract void onHasNotNew();
	public abstract void onDownloading(int percent);
	public abstract void onDownloaded(File installingFile);
	
	public enum UpdatingAction{
		EXCEPTION,
		HAS_UNNECESSARY_NEW,
		HAS_NECESSARY_NEW,
		HAS_NOT_NEW,
		DOWNLOADING,
		DOWNLOADED
	}
}
