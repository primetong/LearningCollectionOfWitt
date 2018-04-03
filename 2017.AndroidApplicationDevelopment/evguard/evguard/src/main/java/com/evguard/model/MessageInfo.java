package com.evguard.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.evguard.data.AppDataCache;

public class MessageInfo implements Parcelable{

	private int ID;
	private String MsgType;
	private String Title;
	private String Content;
	private String Time;
	private String isMsgReaded = "0";//0--代表未读，1--代表已读
			
			
	public String getMsgType() {
		return MsgType;
	}

	public void setMsgType(String msgType) {
		MsgType = msgType;
	}
	
	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}
	
	public String getContent() {
		return Content;
	}
	
	public void setContent(String content) {
		Content = content;
	}
	
	public String getTime() {
		return Time;
	}
	
	public void setTime(String time) {
		Time = time;
	}

	public int getID() {
//		this.ID = AppDataCache.getInstance().getMessageId();
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
//		AppDataCache.getInstance().setMessageId(ID);
	}

	public String getIsMsgReaded() {
		return isMsgReaded;
	}

	public void setIsMsgReaded(String isMsgReaded) {
		this.isMsgReaded = isMsgReaded;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(ID);
		dest.writeString(Content);
		dest.writeString(Title);
		dest.writeString(Time);
		dest.writeString(MsgType);
		dest.writeString(isMsgReaded);
	}
	public static final Parcelable.Creator<MessageInfo> CREATOR = new Creator<MessageInfo>() {
		@Override
		public MessageInfo createFromParcel(Parcel source) {
			MessageInfo aMessageInfo = new MessageInfo();
			aMessageInfo.ID = source.readInt();
			aMessageInfo.Content = source.readString();
			aMessageInfo.Title = source.readString();
			aMessageInfo.Time = source.readString();
			aMessageInfo.MsgType = source.readString();
			aMessageInfo.isMsgReaded = source.readString();
			return aMessageInfo;
		}

		@Override
		public MessageInfo[] newArray(int size) {
			return new MessageInfo[size];
		}
		
	};
}
