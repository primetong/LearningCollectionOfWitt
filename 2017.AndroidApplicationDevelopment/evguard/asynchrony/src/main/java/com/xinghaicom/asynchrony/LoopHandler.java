package com.xinghaicom.asynchrony;

import android.os.Handler;
import android.os.Message;

/**
 * 
 * @author Administrator
 * 
 * 循环处理器，用于回发线程已启动，并处于消息循环状态
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
			onException(new NullPointerException("Message无效"));
			return;
		}
		
		int action = msg.what;
		switch(action){
		case EXCEPTION:
			if(msg.obj == null || !(msg.obj instanceof Exception)){
				onException(new IllegalArgumentException("异常信息无效"));
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
