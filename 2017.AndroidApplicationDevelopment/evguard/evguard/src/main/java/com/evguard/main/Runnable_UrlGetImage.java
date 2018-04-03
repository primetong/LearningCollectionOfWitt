package com.evguard.main;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Runnable_UrlGetImage implements Runnable{

	private String TAG="Runnable_UrlGetImage";
	private String mUrl="";
	private String mId;
	protected HttpURLConnection mKidHeadURLConnection = null;	
	protected int mInputSize = 0;
	protected InputStream mInputStream = null;
	protected FileOutputStream mFileOutputStream = null;
	private OnGetKidHeadListener mOnGetKidHeadListener;
	
	public Runnable_UrlGetImage(String id,String url,OnGetKidHeadListener aOnGetKidHeadListener){
		
		this.mId=id;
		this.mUrl=url;
		this.mOnGetKidHeadListener=aOnGetKidHeadListener;
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(mUrl == null || mUrl.length() <= 0) {
			Log.i(TAG, "头像的URL路径无效！");
			throw new IllegalArgumentException("头像的URL路径无效！");
			}
		
		try {
			URL headURL = new URL(mUrl);
			mKidHeadURLConnection = (HttpURLConnection) headURL.openConnection();
			if(mKidHeadURLConnection == null) {
				if(mOnGetKidHeadListener!=null)mOnGetKidHeadListener.onFileNotFound(mId);
				return;
			}
			Log.i(TAG, "头像开始下载:"+mUrl);
			mKidHeadURLConnection.setConnectTimeout(5000);
			mKidHeadURLConnection.setRequestMethod("GET");
			Log.i("wlh", "URLConnection.getResponseCode:"+mKidHeadURLConnection.getResponseCode());
			if(mKidHeadURLConnection.getResponseCode() == 200){
			    InputStream inputStream = mKidHeadURLConnection.getInputStream();
			    Bitmap bmp = BitmapFactory.decodeStream(inputStream);
			    if(mOnGetKidHeadListener!=null)mOnGetKidHeadListener.onFileDown(mId,bmp);
			}else{
				mOnGetKidHeadListener.onFileNotFound(mId);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			mOnGetKidHeadListener.onFileNotFound(mId);
		} catch (ProtocolException e) {
			e.printStackTrace();
			mOnGetKidHeadListener.onFileNotFound(mId);
		} catch (IOException e) {
			e.printStackTrace();
			mOnGetKidHeadListener.onFileNotFound(mId);
		}catch(Exception e){
			e.printStackTrace();
			mOnGetKidHeadListener.onFileNotFound(mId);
		}
	}
	public interface OnGetKidHeadListener{
		public void onFileNotFound(String simnum);
		public void onFileDown(String simnum,Bitmap bm);
	}

}
