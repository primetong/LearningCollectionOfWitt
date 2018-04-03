package com.evguard.tools;

import java.io.File;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.evguard.main.App_Application;


public class PhoneDirHelper {
	private String mFoldernam="cache";
	private File mFLocal=null;
	
	private String mSDDir="";
	private File mFSD=null;
	private String mLocalDir=null;
	
	private static PhoneDirHelper mPhoneDirHelper;
	private static Object lockObj=new Object();
	
	public static PhoneDirHelper getInstance(){
		if(mPhoneDirHelper==null){
			synchronized(lockObj){
				mPhoneDirHelper=new PhoneDirHelper();
			}
		}
		return mPhoneDirHelper;
	}
	
	private PhoneDirHelper(){
		
		mLocalDir=App_Application.getInstance().getFilesDir().getPath();
		System.out.println("mLocalDir==" + mLocalDir);
		mFLocal=new File(mLocalDir,mFoldernam);
		if(!mFLocal.exists())mFLocal.mkdir();
		mSDDir=Environment.getExternalStorageDirectory().getPath();
		if(isHasSD())
		{
			mFSD=new File(mSDDir,mFoldernam);
			if(!mFSD.exists())mFSD.mkdir();
		}
	}
	public File getPhoneMemeryAppDataFile(){
		return mFLocal;
	}
	public File getSDAppDataFile(){
		return mFSD;
	}
	public boolean isHasSD(){
		String status = Environment.getExternalStorageState();
		  if (status.equals(Environment.MEDIA_MOUNTED)) {
		   return true;
		  } else {
		   return false;
		  }
	}
	public Uri getAContentUrl(File storageFile,String filename){

//		File f=new File("data/sdcard",filename);
//		Uri u=Uri.fromFile(f); 
//		Log.i("wlh", u.getPath());
		File f=new File(storageFile,filename);
		if(f.exists())f.delete();
		Uri u=Uri.fromFile(f); 
		Log.i("wlh", u.getPath());
		return u;
	}
	
}
