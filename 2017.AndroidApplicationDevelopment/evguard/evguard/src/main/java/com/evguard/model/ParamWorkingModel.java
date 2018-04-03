package com.evguard.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ParamWorkingModel implements Parcelable{
		private int Mode=0;
		private int Interval=0;

		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeInt(Mode);
			dest.writeInt(Interval);
		}
		public static final Parcelable.Creator<ParamWorkingModel> CREATOR = new Creator<ParamWorkingModel>() {
	        public ParamWorkingModel createFromParcel(Parcel source) {    
	        	ParamWorkingModel aParamWorkingModel = new ParamWorkingModel();
	        	aParamWorkingModel.Mode=source.readInt();
	        	aParamWorkingModel.Interval=source.readInt();
	        	return aParamWorkingModel;
	        }
	        public ParamWorkingModel[] newArray(int size) {    
	            return new ParamWorkingModel[size];    
	        }    
		};
		
		public int getMode(){
			return this.Mode;
		}
		public int getInterval(){
			return this.Interval;
		}
		public void setInterval(int i){
			 this.Interval=i;
		}
		public void setMode(int i){
			 this.Mode=i;
		}
}
