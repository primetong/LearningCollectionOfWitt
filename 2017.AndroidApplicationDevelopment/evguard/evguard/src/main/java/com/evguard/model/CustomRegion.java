package com.evguard.model;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class CustomRegion implements Parcelable {

	private String mRegionId = "";
	private String mRegionName = "";
	private double mCenterLon = 0.0;
	private double mCenterLat = 0.0;
	private double mRadius = 10;
	private String mValidDate = "";

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(mRegionId);
		dest.writeString(mRegionName);
		dest.writeDouble(mCenterLon);
		dest.writeDouble(mCenterLat);
		dest.writeDouble(mRadius);
		dest.writeString(mValidDate);
	}

	public static final Parcelable.Creator<CustomRegion> CREATOR = new Creator<CustomRegion>() {
		public CustomRegion createFromParcel(Parcel source) {
			CustomRegion newCarInfo = new CustomRegion();
			newCarInfo.mRegionId = source.readString();
			newCarInfo.mRegionName = source.readString();
			newCarInfo.mCenterLon = source.readDouble();
			newCarInfo.mCenterLat = source.readDouble();
			newCarInfo.mRadius = source.readDouble();
			newCarInfo.mValidDate = source.readString();

			return newCarInfo;
		}

		public CustomRegion[] newArray(int size) {
			return new CustomRegion[size];
		}
	};

	public CustomRegion() {

	}

	public CustomRegion(String s) {
		this.mRegionName = s;

	}

	public void setRegionId(String s) {
		this.mRegionId = s;
	}

	public String getRegionId() {
		return this.mRegionId;
	}

	public void setRegionName(String s) {
		this.mRegionName = s;
	}

	public String getRegionName() {
		return this.mRegionName;
	}

	public void setCenterLon(double d) {
		this.mCenterLon = d;
	}

	public double getCenterLon() {
		return this.mCenterLon;
	}

	public void setCenterLat(double d) {
		this.mCenterLat = d;
	}

	public double getCenterLat() {
		return this.mCenterLat;
	}

	public void setRadius(double f) {
		this.mRadius = f;
	}

	public double getRadius() {
		return this.mRadius;
	}

	public void setValidDate(String s) {
		this.mValidDate = s;
	}

	public String getValidDate() {
		return this.mValidDate;
	}

}
