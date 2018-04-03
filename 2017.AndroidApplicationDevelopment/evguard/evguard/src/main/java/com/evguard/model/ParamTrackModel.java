package com.evguard.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ParamTrackModel implements Parcelable{
	private int Frequency=0;
	private int Duration=0;

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(Frequency);
		dest.writeInt(Duration);
	}
	public static final Parcelable.Creator<ParamTrackModel> CREATOR = new Creator<ParamTrackModel>() {
        public ParamTrackModel createFromParcel(Parcel source) {    
        	ParamTrackModel aParamTrackModel = new ParamTrackModel();
        	aParamTrackModel.Frequency=source.readInt();
        	aParamTrackModel.Duration=source.readInt();
        	return aParamTrackModel;
        }
        public ParamTrackModel[] newArray(int size) {    
            return new ParamTrackModel[size];    
        }    
	};
	
	public int getFrequency(){
		return this.Frequency;
	}
	public int getDuration(){
		return this.Duration;
	}
	public void setDuration(int i){
		 this.Duration=i;
	}
	public void setFrequency(int i){
		 this.Frequency=i;
	}
}
