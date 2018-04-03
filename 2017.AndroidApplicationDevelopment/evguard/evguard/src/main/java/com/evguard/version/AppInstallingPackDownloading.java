package com.evguard.version;

import java.io.File;



import android.os.Message;

public class AppInstallingPackDownloading extends Thread {

	protected AppVersion mVersion=null;
	protected DownloadingHandler mDownloadingHandler = null;
	protected boolean mCancel = false;
	protected AppInstallingPackage mInstallingPackage = null;
	
	public AppInstallingPackDownloading(AppVersion version,DownloadingHandler downloadingHandler){
		if(version == null) throw new NullPointerException("Version无效");
		if(downloadingHandler == null) throw new NullPointerException("DownloadingHandler无效");
		mVersion = version;
		mDownloadingHandler = downloadingHandler;
	} 
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		try{
			if(mCancel) return;
			mInstallingPackage = new AppInstallingPackage(mVersion,mDownloadingHandler);
			if(mCancel) return;
			File insallingFile = mInstallingPackage.download();
			if(mCancel) return;
			Message msg = null;
			if(mDownloadingHandler != null){
				msg = mDownloadingHandler.obtainMessage(DownloadingHandler.DownloadingAction.DOWNLOADED.ordinal(), 
						insallingFile);	
				mDownloadingHandler.sendMessage(msg);
			}
		}catch(Exception e){
			if(mInstallingPackage != null)
				mInstallingPackage.cancel();
			if(mCancel) return;
			Message msg = null;
			if(mDownloadingHandler != null){
				msg = mDownloadingHandler.obtainMessage(DownloadingHandler.DownloadingAction.EXCEPTION.ordinal(), 
						e);
				mDownloadingHandler.sendMessage(msg);
			}
		}
	}
	
	public void quit(){
		mCancel = true;
		if(mInstallingPackage != null)
			mInstallingPackage.cancel();
	}
}
