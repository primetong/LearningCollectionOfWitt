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
 * ��Ϣѭ��
 * ����Ϣѭ�����߳� 
 *
 */
public abstract class Looping extends Thread {

	protected Looper mLooper = null;
	protected Handler mRequestingHandler = null;
	protected Vector<LoopHandler> mLoopHandlers = null;
	protected boolean mQuit = false;
	
	/**
	 * ��ѭ���������Ĺ��췽�������ڵ����̣߳���ʱ���ձ����߳�����������ѭ������Ϣ
	 * @param loopHandler
	 * 		ѭ�������������ڻط��߳�����������������Ϣѭ��״̬
	 */

	public Looping(LoopHandler loopHandler) {
		super();
		mLoopHandlers = new Vector<LoopHandler>();
		mLoopHandlers.add(loopHandler);
	}
	
	/**
	 * ����ѭ���������Ĺ��췽���������߳����д����߳������������Ϣѭ��
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
	 * ��Ϣ����
	 * �����̷߳���������Ϣ���ڱ����߳��д��������������������Ϊ
	 * @param action
	 * 		�����־
	 * @param requestInfo
	 * 		��Ϣ����
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
	 * �˳���Ϣѭ��
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