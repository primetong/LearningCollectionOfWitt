package com.evguard.service;

import java.util.List;
import cn.jpush.android.api.JPushInterface;

import com.evguard.data.AppDataCache;
import com.evguard.main.App_Settings;
import com.evguard.model.PushMessage;
import com.evguard.tools.ConstantTool;
import com.evguard.tools.LogEx;
import com.xinghaicom.evguard.R;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

public class JPushReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";
	private App_Settings settings;
	private NotificationManager nm=null;
	
	@Override
	public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
		settings=new App_Settings(context);
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            AppDataCache.getInstance().setRegisterId(regId);
            settings.setJPushRegisterId(regId);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
                        
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
        	Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

        
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            processPushMessage(context,bundle);
            
        	
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
        	 int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
        	Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
        	//打开自定义的Activity
        	processUserAction(context,bundle);
        	
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));

        	
        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        	Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
        	Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
	}
	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} 
			else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	

	private void processPushMessage(Context context, Bundle bundle){
		PushMessage amsg=getPushMessage(bundle);//mapMessage.remove(notifactionId);
		brocastPsuhMessage(context, amsg);
	}
	private void processUserAction(Context context, Bundle bundle){
		try {
			PushMessage amsg=getPushMessage(bundle);//mapMessage.remove(notifactionId);
			Intent target=getTargetIntent(context, amsg);
			context.startActivity(target);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void brocastPsuhMessage(Context context, PushMessage amsg){
		Intent brocastIntent = new Intent(ConstantTool.PUSH_BROCAST);
		brocastIntent.putExtra(ConstantTool.PUSH_INTENTDATA, amsg);
		context.sendBroadcast(brocastIntent);
	}
    private PushMessage getPushMessage(Bundle bundle){
    	PushMessage aPushMessage=new PushMessage();
//    	aPushMessage.setMessageId(bundle.getString(JPushInterface.EXTRA_MSG_ID));
    	
    	aPushMessage.setNotificationId(bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID));

    	aPushMessage.setMessageContent(bundle.getString(JPushInterface.EXTRA_ALERT));

    	aPushMessage.setMessageTitle(bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE));

    	aPushMessage.setMessageType(bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE));


    	return aPushMessage;
    }
	private Intent getTargetIntent(Context context, PushMessage amsg) {
		ActivityManager acm = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> tasks = acm.getRunningTasks(100);
		for (ActivityManager.RunningTaskInfo arunning : tasks) {
			// 找到当前应用的task，并启动task的栈顶activity，达到程序切换到前台
			if (arunning.topActivity.getPackageName().equals(
					context.getPackageName())) {
				try {
					Intent resultIntent;
					resultIntent = new Intent(context,Class.forName(arunning.topActivity.getClassName()));
					resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_SINGLE_TOP);
					Bundle b = new Bundle();
					b.putParcelable(ConstantTool.PUSH_INTENTDATA, amsg);
					resultIntent.putExtras(b);
					return resultIntent;
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		LogEx.i("ac__", "Launch app");
		// 若没有找到运行的task，用户结束了task或被系统释放，则重新启动
		Intent LaunchIntent = context.getPackageManager()
				.getLaunchIntentForPackage("com.xinghaicom.evguard");
		Bundle b = new Bundle();
		b.putParcelable("PushMessage", amsg);
		LaunchIntent.putExtras(b);
		return LaunchIntent;
	}

	//如果是自定义消息自己控制通知栏
	private void notifyUser(Context context,PushMessage aPushMessage){
		if(nm==null){
			nm=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		Notification myNotify=new Notification();  
		myNotify.icon = R.drawable.icon_logonotify;  
        myNotify.tickerText = aPushMessage.getMessageTitle();  
        myNotify.when = System.currentTimeMillis();  
        myNotify.defaults =Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE;
        myNotify.flags = Notification.FLAG_AUTO_CANCEL ;// 不能够自动清除  
        RemoteViews rv = new RemoteViews(context.getPackageName(),  
                R.layout.my_notification);  
        rv.setTextViewText(R.id.text_content, aPushMessage.getMessageContent());  
        rv.setTextViewText(R.id.title, aPushMessage.getMessageTitle());
        myNotify.contentView = rv;  
        
        Intent target =getTargetIntent(context,aPushMessage);  
        PendingIntent contentIntent = PendingIntent.getActivity(context, 1,  
        		target, 1);  
        myNotify.contentIntent = contentIntent;  
        nm.notify(aPushMessage.getNotificationId(), myNotify);
	}
}
