package com.evguard.model;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.evguard.bmap.BaiduMapUtils;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * 
 */
public class CarCurPositionInfo extends Position_Base implements Parcelable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7855893242245625839L;
	
	
	private String mCarNum = "0";
	private String mGpsTime = null;
	private double mLatitude = 0;
	private double mLongitude = 0;
	public String mAddress = "0";
	private LatLng bdLatlng;
	private String mDirection = "0";
	private String mMileage = "0";
	private String mSpeed = "0";
	

	public CarCurPositionInfo() {
		
	}	

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeString(mCarNum);
		dest.writeString(mGpsTime);
		dest.writeDouble(mLatitude);
		dest.writeDouble(mLongitude);
	}
	public static final Parcelable.Creator<CarCurPositionInfo> CREATOR = new Creator<CarCurPositionInfo>() {
        public CarCurPositionInfo createFromParcel(Parcel source) {    
        	CarCurPositionInfo newCarInfo = new CarCurPositionInfo();
        	
        	newCarInfo.mCarNum = source.readString();
        	newCarInfo.mGpsTime = source.readString();
        	newCarInfo.mLatitude = source.readDouble();
        	newCarInfo.mLongitude = source.readDouble();
            return newCarInfo;    
        }    
        public CarCurPositionInfo[] newArray(int size) {    
            return new CarCurPositionInfo[size];    
        }    
    };

    public LatLng getBDLatlng(){
    	return bdLatlng;
    }
	
    public CarCurPositionInfo (CarCurPositionInfo aKidCurPositionInfo){
    	
    	this.mCarNum=aKidCurPositionInfo.getCarNum();
    	this.mGpsTime=aKidCurPositionInfo.getGpsTime();
    	this.mLatitude=aKidCurPositionInfo.getLatitude();
    	this.mLongitude=aKidCurPositionInfo.getLongitude();
    }
    
	public void setCarNum(String carNum) {
		mCarNum = carNum;
	}
	
	public String getCarNum() {
		return mCarNum;
	}
	
	public void setGpsTime(String gpsTime) {
		mGpsTime = gpsTime;
	}
	
	public String getGpsTime() {
		return mGpsTime;
	}
	
	public void setLatitude(double latitude) {
		mLatitude = latitude;
	}
	
	public double getLatitude() {
		return mLatitude;
	}
	
	public void setLongitude(double longitude) {
		mLongitude = longitude;
	}
	
	public double getLongitude() {
		return mLongitude;
	}
	
	public void setBaiduLatlng(double latitude,double longitude){
		LatLng aLatLng=new LatLng(latitude,longitude);
		bdLatlng = BaiduMapUtils.ConvetTobdll09(aLatLng);
		setAddress(bdLatlng);
	}
	
	public void setAddress(LatLng bdLatlng){
		mAddress = getAddress(bdLatlng);
	}
	
	public String getAddress(){
		return mAddress;
	}
	
	public String getAddress(LatLng latlng){
		GeoCoder geoCoder = GeoCoder.newInstance();
		if(geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latlng))){
			geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
				@Override
				public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
					mAddress = result.getAddress();
				}
				
				@Override
				public void onGetGeoCodeResult(GeoCodeResult result) {
					
				}
			});
		}
		
		return mAddress;
	}

	
	public float getDirection() {
		
		return Float.parseFloat(mDirection);
	}

	public void setDirection(String direction) {
		this.mDirection = direction;
	}

	public String getMileage() {
		return mMileage;
	}

	public void setMileage(String mileage) {
		this.mMileage = mileage;
	}

	public String getSpeed() {
		return mSpeed;
	}

	public void setSpeed(String speed) {
		this.mSpeed = speed;
	}
	
	
	
}
