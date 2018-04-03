package com.xinghaicom.updating;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class UpdatingChecking extends Thread {
	
	protected String mVersionURLPath = null;
	protected int mCurrentVersionCode = 0;
//	/**
//	 * @deprecated
//	 */
//	protected Handler mUpdatingHandler = null;
	protected Handler mCheckingHandler = null;
	protected boolean mCancel = false;
	
//	/**
//	 * @deprecated
//	 * @param versionURLPath
//	 * @param currentVersionCode
//	 * @param updatingHandler
//	 */
//	public UpdatingChecking(String versionURLPath,int currentVersionCode,UpdatingHandler updatingHandler){
//		if(versionURLPath == null || versionURLPath.length() <= 0) throw new IllegalArgumentException("最新版本URL无效！");
//		if(updatingHandler == null) throw new NullPointerException("UpdatingHandler无效！");
//		
//		mVersionURLPath = versionURLPath;
//		mCurrentVersionCode = currentVersionCode;		
//		mUpdatingHandler = updatingHandler;		
//	}
	
	public UpdatingChecking(String versionURLPath,int currentVersionCode,CheckingHandler checkingHandler){
		if(versionURLPath == null || versionURLPath.length() <= 0) throw new IllegalArgumentException("最新版本URL无效！");
		if(checkingHandler == null) throw new NullPointerException("CheckingHandler无效！");
		
		mVersionURLPath = versionURLPath;
		mCurrentVersionCode = currentVersionCode;		
		mCheckingHandler = checkingHandler;		
	}
			
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		try{
			if(mCancel) return;
			
			Version newVersion = new Version(mVersionURLPath);  
			int newVersionCode = newVersion.getVersionCode();
			
			if(mCancel) return;
			
			Message msg = Message.obtain();		
			Log.i("update...", "mCurrentVersionCode:"+mCurrentVersionCode+";;newVersionCode:"+newVersionCode);
			if(mCurrentVersionCode >= newVersionCode){
//				if(mUpdatingHandler != null){
//					msg = mUpdatingHandler.obtainMessage(UpdatingHandler.UpdatingAction.HAS_NOT_NEW.ordinal());			
//					mUpdatingHandler.sendMessage(msg);
//				}
				if(mCheckingHandler != null){
					msg = mCheckingHandler.obtainMessage(CheckingHandler.CheckingAction.HAS_NOT_NEW.ordinal());			
					mCheckingHandler.sendMessage(msg);
				}
								
			}else{
				boolean isNecessary = newVersion.isNecessary();
//				if(mUpdatingHandler != null){
//					msg.what = isNecessary?UpdatingHandler.UpdatingAction.HAS_NECESSARY_NEW.ordinal():
//						UpdatingHandler.UpdatingAction.HAS_UNNECESSARY_NEW.ordinal();
//					msg.obj = newVersion;
//					mUpdatingHandler.sendMessage(msg);
//				}
				if(mCheckingHandler != null){
					msg.what = isNecessary?CheckingHandler.CheckingAction.HAS_NECESSARY_NEW.ordinal():
						CheckingHandler.CheckingAction.HAS_UNNECESSARY_NEW.ordinal();
					msg.obj = newVersion;
					mCheckingHandler.sendMessage(msg);					
				}
			}
		}catch(Exception e){
			Message msg  = null;
//			if(mUpdatingHandler != null){
//				msg = mUpdatingHandler.obtainMessage(UpdatingHandler.UpdatingAction.EXCEPTION.ordinal(), 
//						e);
//				mUpdatingHandler.sendMessage(msg);
//			}
			if(mCheckingHandler != null){
				msg = mCheckingHandler.obtainMessage(CheckingHandler.CheckingAction.EXCEPTION.ordinal(), 
						e);
				mCheckingHandler.sendMessage(msg);
			}
		}
	}
	
	public void setURL(String versionURLPath){
		mVersionURLPath = versionURLPath;
	}
	
	public void quit(){		
		mCancel = true;		
	}
}
