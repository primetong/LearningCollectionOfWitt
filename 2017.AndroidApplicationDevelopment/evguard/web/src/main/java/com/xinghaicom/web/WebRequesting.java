package com.xinghaicom.web;

import java.io.IOException;
import java.util.List;
import java.util.TimerTask;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParserException;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;


public class WebRequesting extends Thread {

	protected WebClient mWebClient = null;
	protected Handler mResponseHandler = null;
	
	protected Looper mLooper = null;
	protected Handler mRequestingHandler = null;
	
	public WebRequesting(String serviceMethodURL,Handler responseHandler){
		if(serviceMethodURL == null || serviceMethodURL.length() <= 0)
			throw new IllegalArgumentException("WebURLŒﬁ–ß");
				
		mWebClient = new WebClient(serviceMethodURL, 10000, 10000);
		mResponseHandler = responseHandler;
	}
		
	@Override
	public void run() {
		// TODO Auto-generated method stub		
		Looper.prepare();		
		mLooper = Looper.myLooper();
				
		mRequestingHandler = new Handler(){
			
			public void handleMessage(android.os.Message msg) {
				try {
					if(mResponseHandler != null){
						Message responseMsg = mResponseHandler.obtainMessage();
						String result = null;
						if(msg.obj != null && msg.obj instanceof List<?>){
							List<BasicNameValuePair> params = (List<BasicNameValuePair>)msg.obj;					
							result = mWebClient.execute(params);
						}										
						responseMsg.obj = result;
						
						mResponseHandler.sendMessage(responseMsg);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e("WebRequesting", "Web«Î«Û ß∞‹£∫" + e.getMessage());
				};
			}
		};
				
		Looper.loop();
	}
	
	public void request(List<BasicNameValuePair> params){
		Message msg = mRequestingHandler.obtainMessage(0, params);
		mRequestingHandler.sendMessage(msg);
	}

	public void quit() {
		// TODO Auto-generated method stub
		if(mWebClient != null)
			mWebClient.release();
		mLooper.quit();
	}






	
}
