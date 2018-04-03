package com.evguard.model;

import android.os.Parcel;
import android.os.Parcelable;


public class ElectricDetail implements Parcelable{

	private long day;
	private float value;
	
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(day);
		dest.writeFloat(value);
	}
	
	public static final Parcelable.Creator<ElectricDetail> CREATOR = new Creator<ElectricDetail>() {

		@Override
		public ElectricDetail createFromParcel(Parcel source) {
			ElectricDetail mileageInfo = new ElectricDetail();
			mileageInfo.day = source.readLong();
			mileageInfo.value = source.readFloat();
			return mileageInfo;
		}

		@Override
		public ElectricDetail[] newArray(int size) {
			return new ElectricDetail[size];
		}
	};
	
	public long getDay() {
		System.out.println("day==" + day);
		return day;
	}
	public void setDay(long day) {
		this.day = day;
	}
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}
}
