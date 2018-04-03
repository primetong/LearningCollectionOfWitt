package com.xinghaicom.asynchrony;

import android.os.Handler;
import android.os.Message;

/**
 * 
 * @author Administrator
 * 
 * ѭ�������������ڻط��߳�����������������Ϣѭ��״̬
 * 
 */
public abstract class LoopHandler extends Handler {

	public static final int EXCEPTION = 0;
	public static final int LOOPED = 1;
	
	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		
		if(msg == null){
			onException(new NullPointerException("Message��Ч"));
			return;
		}
		
		int action = msg.what;
		switch(action){
		case EXCEPTION:
			if(msg.obj == null || !(msg.obj instanceof Exception)){
				onException(new IllegalArgumentException("�쳣��Ϣ��Ч"));
				return;
			}			
			onException((Exception)msg.obj);
			
			break;
			
		case LOOPED:
			onLooped();
			
			break;
			
		}
		
	}
	
	protected void onException(Exception e){}
	protected void onLooped(){};
}
