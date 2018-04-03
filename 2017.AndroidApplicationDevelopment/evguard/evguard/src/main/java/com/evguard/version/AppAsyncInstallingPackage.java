package com.evguard.version;




public class AppAsyncInstallingPackage {

	protected AppInstallingPackDownloading mDownloading=null;
	public AppAsyncInstallingPackage(AppVersion version,DownloadingHandler downloadingHandler){
		mDownloading = new AppInstallingPackDownloading(version, downloadingHandler);
	}
	
	public void download(){		
		mDownloading.start();
	}
	
	public void close(){		
		mDownloading.quit();
	}
}
