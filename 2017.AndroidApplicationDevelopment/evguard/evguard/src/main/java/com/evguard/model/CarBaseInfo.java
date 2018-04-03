package com.evguard.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class CarBaseInfo implements Parcelable{

	private String mCarId = null;
	private String mSimNum = null;
	private String mCarNum = null;
	private String mCarPwd="651ba3a35e91b5e1909513c7";
//	private String mKidName="";
	private String mKidBirthday="";
	private String mPhoteFileName="";
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(mCarId);
		dest.writeString(mSimNum);
		dest.writeString(mCarNum);
//		dest.writeString(mOwner);
		dest.writeString(mCarPwd);
//		dest.writeString(mKidName);
		dest.writeString(mKidBirthday);
		dest.writeString(mPhoteFileName);
	}
	public static final Parcelable.Creator<CarBaseInfo> CREATOR = new Creator<CarBaseInfo>() {
        public CarBaseInfo createFromParcel(Parcel source) {    
        	CarBaseInfo newCarInfo = new CarBaseInfo();
        	
        	newCarInfo.mCarId=source.readString();
        	newCarInfo.mSimNum=source.readString();
        	newCarInfo.mCarNum = source.readString();
//        	newCarInfo.mOwner = source.readString();
        	newCarInfo.mCarPwd = source.readString();
//        	newCarInfo.mKidName = source.readString();
        	newCarInfo.mKidBirthday = source.readString();
        	newCarInfo.mPhoteFileName = source.readString();
        	return newCarInfo;
        }
        public CarBaseInfo[] newArray(int size) {    
            return new CarBaseInfo[size];    
        }    
	};
	public void setCarNum(String carNum) {
		mCarNum = carNum;
	}
	
	public String getCarNum() {
		return mCarNum;
	}
	
//	public void setOwner(String owner) {
//		mOwner = owner;
//	}	
//	public String getOwner() {
//		return mOwner;
//	}
	public void setCarId(String carId) {
		mCarId = carId;
	}
	
	public String getCarId() {
		return mCarId;
	}
	
	public void setSimNum (String simNum) {
		mSimNum = simNum;
	}
	
	public String getSimNum() {
		return mSimNum;
	}
	public void setCarPwd (String pwd) {
		mCarPwd = pwd;
	}
	
	public String getCarPwd() {
		return mCarPwd;
	}
//	public void setKidName(String s)
//	{
//		this.mKidName=s;
//	}
//	public String getKidName()
//	{
//		return this.mKidName;
//	}
	public void setKidBirthday (String s) {
		mKidBirthday = s;
	}
	
	public String getKidBirthday() {
		return mKidBirthday;
	}
	public void setPhotoname (String s) {
		mPhoteFileName = s;
	}
	
	public String getPhotoname() {
		return mPhoteFileName;
	}
}
