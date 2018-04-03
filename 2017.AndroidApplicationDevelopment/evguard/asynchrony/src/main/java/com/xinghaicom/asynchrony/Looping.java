package com.xinghaicom.asynchrony;

import java.util.Vector;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * @author Administrator
 * 
 * 消息循环
 * 带消息循环的线程 
 *
 */
public abstract class Looping extends Thread {

	protected Looper mLooper = null;
	protected Handler mRequestingHandler = null;
	protected Vector<LoopHandler> mLoopHandlers = null;
	protected boolean mQuit = false;
	
	/**
	 * 带循环处理器的构造方法，用于调用线程，及时接收本类线程启动并进入循环的消息
	 * @param loopHandler
	 * 		循环处理器，用于回发线程已启动，并处于消息循环状态
	 */

	public Looping(LoopHandler loopHandler) {
		super();
		mLoopHandlers = new Vector<LoopHandler>();
		mLoopHandlers.add(loopHandler);
	}
	
	/**
	 * 不带循环处理器的构造方法，本类线程自行处理线程启动与进入消息循环
	 */
	public Looping(){
		super();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub		
		Looper.prepare();
		
		synchronized (this) {
			if(mQuit) return;
			mLooper = Looper.myLooper();
		}
		
		onInitializing();
		
		mRequestingHandler = new Handler(){
			
			public void handleMessage(android.os.Message msg) {				
				if(msg == null) return;
				onRunning(msg.what, msg.obj);
			}
		};
		
		onLooped();
		if(mLoopHandlers != null){
			synchronized (mLoopHandlers) {
				for(LoopHandler looperHandler:mLoopHandlers){
					if(looperHandler == null)
						continue;
					Message msg = looperHandler.obtainMessage();
					msg.what = LoopHandler.LOOPED;
	//				msg.obj = intializedObj;
					looperHandler.sendMessage(msg);
				}
			}
		}
				
		Looper.loop();
	}
	
	/**
	 * 消息请求
	 * 调用线程发送请求消息，在本类线程中处理各种请求，做出各种行为
	 * @param action
	 * 		请求标志
	 * @param requestInfo
	 * 		消息数据
	 */
	protected void request(int action, Object requestInfo){
		if(mRequestingHandler == null) return;
		try{
			Message msg = mRequestingHandler.obtainMessage(action, requestInfo);		
			mRequestingHandler.sendMessage(msg);
		}catch(Exception e){
			Log.e("Looping", e.getMessage());
		}
	}

	/**
	 * 退出消息循环
	 */
	public void quit() {
		// TODO Auto-generated method stub
		synchronized(this){
			mQuit = true;
			if(mLooper != null) mLooper.quit();
		}
	}
	
	
	protected void onInitializing(){}
	protected void onLooped(){}
	
	protected abstract void onRunning(int action, Object obj);

}