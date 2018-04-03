package com.evguard.main;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;

public class App_Application extends Application {
	private String TAG="App_Application";
	private static App_Application mInstance = null;
	 public boolean m_bKeyRight = true;
	
	@Override
    public void onCreate() {
	    super.onCreate();
		mInstance = this;
		initEngineManager(this);

	}
		
	@Override
	public void onTerminate() {
		
		super.onTerminate();
	}
	
	public void initEngineManager(Context context) {
//		Log.i(TAG, "JPush init...");
//		JPushInterface.setDebugMode(true); 	// 
//        JPushInterface.init(this);     		// JPush
//        Log.i(TAG, "JPush init ok");
        SDKInitializer.initialize(mInstance); 
//		Thread_KidHeadLoader.getInstance().setOnThreadInitOkLisntener(new OnThreadInitOkLisntener(){
//
//			@Override
//			public void onThreadInitOk() {
//				// TODO Auto-generated method stub
//				Log.i(TAG, "kidhead looped!");
//       		   Thread_KidHeadLoader.getInstance().getAllKidHeads();//头像缓存
//			}
//
//			@Override
//			public void onThreadInitException(Exception ex) {
//				// TODO Auto-generated method stub
//				Log.i(TAG, "kidhead loop exception!"+ex.getMessage());
//				ex.printStackTrace();
//			}
//			@Override
//			public void onGetFilesOK() {
//				// TODO Auto-generated method stub
//				Log.i(TAG, "kidhead get ok!");
//			}
//			
//		});
//		Thread_KidHeadLoader.getInstance().initThread();
//    	
       
	}
	
	public static App_Application getInstance() {
		return mInstance;
	}
	
	
}
