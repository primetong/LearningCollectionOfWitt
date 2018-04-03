package com.xinghaicom.updating;

public class AsyncInstallingPackage {
	
	protected InstallingPackDownloading mDownloading = null;
	
	public AsyncInstallingPackage(Version version,DownloadingHandler downloadingHandler){
		mDownloading = new InstallingPackDownloading(version, downloadingHandler);
	}
	
	public void download(){		
		mDownloading.start();
	}
	
	public void close(){		
		mDownloading.quit();
	}
}
