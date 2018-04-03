package com.evguard.model;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class CarInfo implements Parcelable{

	private String CarId = null;
	private String SimNum = null;
	private String CarNum = null;
	private boolean Lock=false;
	private List<ParamClassSilence> ClassSilenceList=new ArrayList<ParamClassSilence>();
	private String LowBattery=null;
	private int RingingTimes=0;
	private ParamTrackModel TrackModel =null;
	private ParamWorkingModel  WorkingModel  =null;
	private int LocationType  =0;
	private int Limitexhale =0;
	private List<String> Contacts =new ArrayList<String>();
	private List<String> FamilyNumbers=new ArrayList<String>();
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(CarId);
		dest.writeString(SimNum);
		dest.writeString(CarNum);
		dest.writeByte((byte) (Lock?1:0));
		ParamClassSilence[] tParamClassSilence=new ParamClassSilence[ClassSilenceList.size()];
		ClassSilenceList.toArray(tParamClassSilence);
		dest.writeParcelableArray(tParamClassSilence, 0);
		
		dest.writeString(LowBattery);
		dest.writeInt(RingingTimes);
		dest.writeParcelable(TrackModel, 0);
		dest.writeParcelable(WorkingModel, 0);
		dest.writeInt(LocationType);
		dest.writeInt(Limitexhale);
		dest.writeStringList(Contacts);
		dest.writeStringList(FamilyNumbers);
	}
	public static final Parcelable.Creator<CarInfo> CREATOR = new Creator<CarInfo>() {
        public CarInfo createFromParcel(Parcel source) {    
        	CarInfo aCarInfo = new CarInfo();
        	aCarInfo.CarId=source.readString();
        	aCarInfo.SimNum=source.readString();
        	aCarInfo.CarNum=source.readString();
        	Parcelable[] tParcelables=source.readParcelableArray(ParamClassSilence.class.getClassLoader());
        	for(int i = 0; i < tParcelables.length; i++){
        		aCarInfo.ClassSilenceList.add((ParamClassSilence) tParcelables[i]);
        	}
        	return aCarInfo;
        }
        public CarInfo[] newArray(int size) {    
            return new CarInfo[size];    
        }    
	};
	public void setCarNum(String carNum) {
		CarNum = carNum;
	}
	
	public String getCarNum() {
		return CarNum;
	}
	public void setCarId(String carId) {
		CarId = carId;
	}
	
	public String getCarId() {
		return CarId;
	}
	
	public void setSimNum (String simNum) {
		SimNum = simNum;
	}
	public String getSimNum() {
		return SimNum;
	}
	
	public boolean getLock() {
		return Lock;
	}
	public void setLock (boolean b) {
		Lock = b;
	}

	public List<ParamClassSilence> getClassSilenceList() {
		return ClassSilenceList;
	}
	public void setClassSilenceList (List<ParamClassSilence> alist) {
		ClassSilenceList = alist;
	}
	public void setLowBattery (String s) {
		LowBattery = s;
	}
	public String getLowBattery() {
		return LowBattery;
	}
	public void setRingingTimes (int i) {
		RingingTimes = i;
	}
	public int getRingingTimes() {
		return RingingTimes;
	}
	public ParamTrackModel getTrackModel() {
		return TrackModel;
	}
	public void setTrackModel (ParamTrackModel aParamTrackModel) {
		TrackModel = aParamTrackModel;
	}
	public ParamWorkingModel getWorkingModel() {
		return WorkingModel;
	}
	public void setWorkingModel (ParamWorkingModel aParamWorkingModel) {
		WorkingModel = aParamWorkingModel;
	}
	public void setLocationType (int i) {
		LocationType = i;
	}
	public int getLocationType() {
		return LocationType;
	}
	public void setLimitexhale (int i) {
		Limitexhale = i;
	}
	public int getLimitexhale() {
		return Limitexhale;
	}
	public void setContacts (List<String> alist) {
		Contacts = alist;
	}
	public List<String> getContacts() {
		return Contacts;
	}
	public void setFamilyNumbers (List<String> alist) {
		Contacts = alist;
	}
	public List<String> getFamilyNumbers() {
		return Contacts;
	}
	
}
