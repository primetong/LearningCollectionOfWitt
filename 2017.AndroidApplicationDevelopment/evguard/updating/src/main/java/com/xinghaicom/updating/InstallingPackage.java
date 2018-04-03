package com.xinghaicom.updating;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpConnection;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.util.Log;

public class InstallingPackage {

	protected Version mVersion = null;
	
	protected HttpURLConnection mPackHttpURLConnection = null;	
	protected InputStream mInputStream = null;
	protected FileOutputStream mFileOutputStream = null;
	protected File mDownloadFile = null;
	
	protected DownloadingHandler mDownloadingHandler = null;
	protected int mInputSize = 0;	

	
	public InstallingPackage(Version version, DownloadingHandler downloadingHandler) throws Exception{
		this(version);
		mDownloadingHandler = downloadingHandler;
	}

	public InstallingPackage(Version version) throws Exception{
		if(version == null) throw new NullPointerException("安装包版本信息无效！");		
		mVersion = version;
	}
	
	public File download() throws Exception{
		
		String externalStorageState = Environment.getExternalStorageState();
		if(!externalStorageState.equalsIgnoreCase(Environment.MEDIA_MOUNTED)) throw new Exception("SD卡未准备好！");
		String downloadDirPath = Environment.getExternalStorageDirectory() + "/" + mVersion.getApplicationDirName() + "/Update";
		new File(downloadDirPath).mkdirs();
		
		String downloadingApkName = mVersion.getApkName();
		int downloadingApkNameWithoutExCount = downloadingApkName.lastIndexOf('.');
		if(downloadingApkNameWithoutExCount < 0) downloadingApkNameWithoutExCount = downloadingApkName.length();
		String downloadApkPath = downloadingApkName.substring(0,downloadingApkNameWithoutExCount) + ".apk";
		mDownloadFile = new File(downloadDirPath,downloadApkPath);	
		
		
		String packageURLPath = mVersion.getVersionSiteURLPath() + "/" + mVersion.getApkName();
		if(packageURLPath == null || packageURLPath.length() <= 0) 
			throw new IllegalArgumentException("安装包的URL路径无效！");
		
		URL packageURL = new URL(packageURLPath);
		
		mPackHttpURLConnection = (HttpURLConnection) packageURL.openConnection();
		if(mPackHttpURLConnection == null) throw new NullPointerException("安装包URLConnection无效！");
		mInputSize = mPackHttpURLConnection.getContentLength();		
		mInputStream = mPackHttpURLConnection.getInputStream();
		
		
		
		
						
		mFileOutputStream = new FileOutputStream(mDownloadFile);		
		byte[] buff = new byte[1024];
		int readCount = -1;
		while((readCount = mInputStream.read(buff)) != -1){			
			mFileOutputStream.write(buff, 0, readCount);
			
			Message msg = null;
			if(mDownloadingHandler != null){
				msg = mDownloadingHandler.obtainMessage(DownloadingHandler.DownloadingAction.DOWNLOADING.ordinal(),
						(int)((double)mDownloadFile.length()/mInputSize*100));
				mDownloadingHandler.sendMessage(msg);
			}
			
		}
		
		mFileOutputStream.flush();
		
		return mDownloadFile;
	}
	
	public void cancel(){
		if(mInputStream != null){
			try {
				mInputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("InstallingPackage", "关闭要下载的InputStream失败：" + e.getMessage());
			}
		}
		
		if(mFileOutputStream != null){
			try {
				mFileOutputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("InstallingPackage", "关闭正下载的FileOutputStream失败：" + e.getMessage());

			}
		}
		
		if(mPackHttpURLConnection != null){
			mPackHttpURLConnection.disconnect();
		}
			
	}
}
