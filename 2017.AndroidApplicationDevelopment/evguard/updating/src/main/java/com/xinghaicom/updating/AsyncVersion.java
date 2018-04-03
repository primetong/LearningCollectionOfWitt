package com.xinghaicom.updating;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.TimerTask;
import java.util.Vector;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class AsyncVersion {
	
	protected UpdatingChecking mUpdatingChecking = null;
	
//	/**
//	 * @deprecated
//	 * @param versionURLPath
//	 * @param currentVersionCode
//	 * @param updatingHandler
//	 */
//	public AsyncVersion(String versionURLPath,int currentVersionCode,UpdatingHandler updatingHandler){
//		mUpdatingChecking = new UpdatingChecking(versionURLPath, currentVersionCode, updatingHandler);
//	}
	
	public AsyncVersion(String versionURLPath,int currentVersionCode,CheckingHandler checkingHandler){
		mUpdatingChecking = new UpdatingChecking(versionURLPath, currentVersionCode, checkingHandler);
	}
	
	public void setURL(String versionURLPath){
		mUpdatingChecking.setURL(versionURLPath);
	}

	public void check() {
		// TODO Auto-generated method stub
		mUpdatingChecking.start();
	}
	
	public void close(){
		mUpdatingChecking.quit();
	}

}
