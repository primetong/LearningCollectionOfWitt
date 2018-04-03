package com.xinghaicom.updating;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

import android.util.Log;

public class Downloading extends Thread {
	
	protected InstallingPackage mInstallingPack = null;
	
	public Downloading(InstallingPackage installingPack) {
		mInstallingPack = installingPack;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		try{
			mInstallingPack.download();
		}catch(Exception e){
			Log.e("Downloading", "ÏÂÔØ³ö´í£º" + e.getMessage());
		}
	}
	
	public void quit(){
		if(mInstallingPack != null)
			mInstallingPack.cancel();
	}
	
	

}
