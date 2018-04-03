package com.evguard.main;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.util.Log;

public class Handler_UncaughtException implements
		UncaughtExceptionHandler {

protected Context mContext = null;
	
	public Handler_UncaughtException(){
		
	}
	
	public Handler_UncaughtException(Context context){
		mContext = context;
	}
	
	public void uncaughtException(Thread thread, Throwable ex) {
		// TODO Auto-generated method stub
		String threadIDAndName = thread != null?"线程ID" + thread.getId() + ";线程名称" + thread.getName():"未知线程";
		String exDes = "发现未捕获的异常:";
		String eStackTrace = "调用堆栈:";
		if(ex != null){
			String eMsg = ex.getMessage();			
			exDes += (eMsg != null?eMsg:"未知异常信息");
			StackTraceElement[] stackTraceElements = ex.getStackTrace();
			if(stackTraceElements != null){
				for(StackTraceElement stackTraceElement:stackTraceElements){
					if(stackTraceElement == null) continue;
					eStackTrace += stackTraceElement.toString() + ";" ;
				}								
			}
		}else{
			exDes += "未知异常";
		}

		Log.e("GpsClientUncaughtExceptionHandler", exDes + "." + threadIDAndName + "." + eStackTrace);
		Log.e("GpsClientUncaughtExceptionHandler", ex.toString());
		
		android.os.Process.killProcess(android.os.Process.myPid());
		
//		if(mContext == null || !(mContext instanceof Activity)) return;
//		
//		AlertDialog.Builder dlgUncaughtEx = new AlertDialog.Builder(mContext);
//		dlgUncaughtEx.setTitle("发现未捕获异常");
//		dlgUncaughtEx.setMessage(exDes + "." + threadIDAndName + "." + eStackTrace);
//		dlgUncaughtEx.setOnCancelListener(new DialogInterface.OnCancelListener() {
//			
//			public void onCancel(DialogInterface dialog) {
//				// TODO Auto-generated method stub
//				android.os.Process.killProcess(android.os.Process.myPid());
//			}
//		});
//		dlgUncaughtEx.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//			
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				android.os.Process.killProcess(android.os.Process.myPid());
//			}
//		});
//		dlgUncaughtEx.show();
				
	}

}
