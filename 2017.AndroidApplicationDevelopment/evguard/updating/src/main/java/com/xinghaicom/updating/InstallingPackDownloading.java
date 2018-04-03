package com.xinghaicom.updating;

import java.io.File;

import android.os.Message;

public class InstallingPackDownloading extends Thread {
	
	protected Version mVersion = null;

	protected DownloadingHandler mDownloadingHandler = null;
	protected boolean mCancel = false;
	protected InstallingPackage mInstallingPackage = null;

	
	public InstallingPackDownloading(Version version,DownloadingHandler downloadingHandler){
		if(version == null) throw new NullPointerException("Version��Ч");
		if(downloadingHandler == null) throw new NullPointerException("DownloadingHandler��Ч");
		mVersion = version;
		mDownloadingHandler = downloadingHandler;
	} 
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		try{
			if(mCancel) return;
			mInstallingPackage = new InstallingPackage(mVersion,mDownloadingHandler);
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
