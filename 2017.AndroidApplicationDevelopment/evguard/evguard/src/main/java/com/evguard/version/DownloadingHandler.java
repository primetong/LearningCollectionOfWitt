package com.evguard.version;

import java.io.File;

import android.os.Handler;
import android.os.Message;

public abstract class DownloadingHandler extends Handler {
	
	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		
		if(msg == null){
			onException(new NullPointerException("Message��Ч"));
			return;
		}
		
		DownloadingAction action = DownloadingAction.values()[msg.what];
		switch(action){
		case EXCEPTION:
			if(msg.obj == null || !(msg.obj instanceof Exception)){
				onException(new IllegalArgumentException("�쳣��Ϣ��Ч"));
				return;
			}
			
			onException((Exception)msg.obj);			
			break;
		
		case DOWNLOADING:
			if(msg.obj == null || !(msg.obj instanceof Integer)){
				onException(new Exception("�����Ч"));
				break;
			}
			
			onDownloading((Integer)msg.obj);
			break;
			
		case DOWNLOADED:
			if(msg.obj == null || !(msg.obj instanceof File)){
				onException(new Exception("File��Ч"));
				break;
			}
			
			onDownloaded((File)msg.obj);
			break;	
			
		}
	}
	
	public abstract void onException(Exception e);
	public abstract void onDownloading(int percent);
	public abstract void onDownloaded(File installingFile);
	
	public enum DownloadingAction{
		EXCEPTION,
		DOWNLOADING,
		DOWNLOADED
	}
}
