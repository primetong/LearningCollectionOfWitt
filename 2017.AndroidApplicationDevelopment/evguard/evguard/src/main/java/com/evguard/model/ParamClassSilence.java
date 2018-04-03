package com.evguard.model;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class ParamClassSilence implements Parcelable{

	private boolean Status=false;
	private String Day="";
	private List<String> Time=new ArrayList<String>();
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeByte((byte) (Status?1:0));
		dest.writeString(Day);
		dest.writeStringList(Time);
	}
	public static final Parcelable.Creator<ParamClassSilence> CREATOR = new Creator<ParamClassSilence>() {
        public ParamClassSilence createFromParcel(Parcel source) {    
        	ParamClassSilence aParamClassSilence = new ParamClassSilence();
        	aParamClassSilence.Status=source.readByte()==1?true:false;
        	aParamClassSilence.Day=source.readString();
        	source.readStringList(aParamClassSilence.Time);

        	return aParamClassSilence;
        }
        public ParamClassSilence[] newArray(int size) {    
            return new ParamClassSilence[size];    
        }    
	};
	public void setStatus(boolean b){
		this.Status=b;
	}
	public boolean getStatus(){
		return this.Status;
	}
	public void setDay(String s){
		this.Day=s;
	}
	public String getDay(){
		return this.Day;
	}
	public void setTime(List<String> alist){
		this.Time=alist;
	}
	public List<String> getTime(){
		return this.Time;
	}
	
	
}
