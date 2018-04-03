package com.evguard.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

/**
 * 轨迹信息
 */
public class TrackInfo extends Position_Base implements Parcelable {

	private String mGpsTime = "111";
	private String mDirection = "222";
	private String mMileage = "333";
	private String mSpeed = "444";
	private String mCarStatus = "555";
	private String mLatitude = "22.564114";
	private String mLongitude = "113.252424";
	private String mCarnum = "888";
	private String mAddress = "";
	
	public TrackInfo() {

	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(mGpsTime);
		dest.writeString(mMileage);
		dest.writeString(mSpeed);
		dest.writeString(mDirection);
		dest.writeString(mCarStatus);
		dest.writeString(mLatitude);
		dest.writeString(mLongitude);
	}

	public static final Parcelable.Creator<TrackInfo> CREATOR = new Creator<TrackInfo>() {
		public TrackInfo createFromParcel(Parcel source) {
			TrackInfo newCarInfo = new TrackInfo();
			newCarInfo.mDirection = source.readString();
			newCarInfo.mCarStatus = source.readString();
			newCarInfo.mGpsTime = source.readString();
			newCarInfo.mMileage = source.readString();
			newCarInfo.mSpeed = source.readString();
			newCarInfo.mLongitude = source.readString();
			newCarInfo.mLatitude = source.readString();
			return newCarInfo;
		}

		public TrackInfo[] newArray(int size) {
			return new TrackInfo[size];
		}
	};

	public void setMileage(String mileage) {
		mMileage = mileage;
	}

	public String getMileage() {
		return mMileage;
	}

	public void setCarStatus(String s) {
		mCarStatus = s;
	}

	public String getCarStatus() {
		return mCarStatus;
	}

	public void setDirect(String direct) {
		mDirection = direct;
	}

	public int getDirect() {
		if(mDirection.equals(""))
			return 0;
		return Integer.parseInt(mDirection);
	}

	public void setGpsTime(String gpsTime) {
		mGpsTime = gpsTime;
	}

	public String getGpsTime() {
		return mGpsTime;
	}

	public void setSpeed(String speed) {
		mSpeed = speed;
	}

	public String getSpeed() {
		return mSpeed;
	}
	
	public Double getLatitude() {
		if(mLatitude.equals(""))
			return 0.0;
		return Double.parseDouble(mLatitude);
	}

	public void setLatitude(String mLatitude) {
		this.mLatitude = mLatitude;
	}

	public Double getLongitude() {
		if(mLongitude.equals(""))
			return 0.0;
		return Double.parseDouble(mLongitude);
	}

	public void setLongitude(String mLongitude) {
		this.mLongitude = mLongitude;
	}

	public String getCarnum() {
		return mCarnum;
	}

	public void setCarnum(String mCarnum) {
		this.mCarnum = mCarnum;
	}

}
