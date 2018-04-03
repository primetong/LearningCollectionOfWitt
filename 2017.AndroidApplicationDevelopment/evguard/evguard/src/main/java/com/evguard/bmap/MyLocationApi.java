package com.evguard.bmap;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;


import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class MyLocationApi  implements AMapLocationListener{

	private String TAG="MyLocationApi";
	private static MyLocationApi mSR_MyLocation;
	private static Object lockObject=new Object();

	private LocationManagerProxy mLocationManager;
	private LocationChangedListener mLocationChangedListener;
	
	private Context mContext;
	
	public static MyLocationApi getInstance(Context aContext)
	{
		if(mSR_MyLocation==null)
			synchronized(lockObject)
			{
				mSR_MyLocation=new MyLocationApi( aContext);
			}
		return mSR_MyLocation;
	}
	
	private MyLocationApi(Context aContext)
	{
		mContext=aContext;
	}
	public void init( )
	{
//		mLocationManager=(LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//		//����λ
//		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//				3000, 8, this);
		
		mLocationManager=LocationManagerProxy.getInstance(mContext);
		mLocationManager.setGpsEnable(true);
		mLocationManager.requestLocationData(
	                LocationProviderProxy.AMapNetwork, 2*1000, 15, this);
	}
	public void unInit()
	{
		if(mLocationManager!=null)
		{
			mLocationManager.removeUpdates(this);
			mLocationManager=null;
		}
		mLocationChangedListener=null;
		
	}
	
	
	public void setLocationChangedListener(LocationChangedListener aLocationChangedListener)
	{
		mLocationChangedListener=aLocationChangedListener;
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
	}
	

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	public interface LocationChangedListener{
		public void onMyLocationChanged(Location location);
	}

	@Override
	public void onLocationChanged(AMapLocation arg0) {
		// TODO Auto-generated method stub
		
//		Log.i(TAG,arg0.getLatitude()+";;;"+arg0.getLongitude());
		
		
		mLocationChangedListener.onMyLocationChanged(arg0);
		
	}
}
