package com.evguard.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PushMessage implements Parcelable{
	
	private String SimNum="";
	private String UserId="";
	private String RegisterId="";
	private String Message_Id="";
	
	private String Message_Title="";
	private String Message_Content="";
	private String Message_Type="1";

	private String Message_Time="";
	private boolean MessageReaded = false;
	private int Notification_Id=0;
	
	public void setSimNum(String s){
		this.SimNum=s;
	}
	public String getSimNum()
	{
		return this.SimNum;
	}
	public void setUserId(String s){
		this.UserId=s;
	}
	public String getUserId()
	{
		return this.UserId;
	}
	public void setRegisterId(String s){
		this.RegisterId=s;
	}
	public String getRegisterId()
	{
		return this.RegisterId;
	}
	public void setMessageId(String s){
		this.Message_Id=s;
	}
	public String getMessageId()
	{
		return this.Message_Id;
	}
	public void setNotificationId(int s){
		this.Notification_Id=s;
	}
	public int getNotificationId(){
		return this.Notification_Id;
	}
	
	public void setMessageTitle(String title){
		this.Message_Title=title;
	}
	public String getMessageTitle()
	{
		return this.Message_Title;
	}
	public void setMessageContent(String s)
	{
		this.Message_Content=s;
	}
	public String getMessageContent(){
		return this.Message_Content;	
	}
	
	public void setMessageType(String i)
	{
		this.Message_Type=i;
	}
	public String getMessageType()
	{
		return this.Message_Type;
	}
	public void setMessageTime(String s)
	{
		this.Message_Time=s;
	}
	public String getMessageTime()
	{
		return this.Message_Time;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(SimNum);
		dest.writeString(UserId);
		dest.writeString(RegisterId);
		dest.writeString(Message_Id);
	
		dest.writeString(Message_Content);
		dest.writeString(Message_Time);
		dest.writeString(Message_Title);
		dest.writeString(Message_Type);
		
		dest.writeInt(Notification_Id);
	}
	public boolean isMessageReaded() {
		return MessageReaded;
	}
	public void setMessageReaded(boolean messageReaded) {
		MessageReaded = messageReaded;
	}
	public static final Parcelable.Creator<PushMessage> CREATOR = new Creator<PushMessage>() {
        public PushMessage createFromParcel(Parcel source) {    
        	PushMessage newPushMessgae = new PushMessage();
        	
        	newPushMessgae.SimNum=source.readString();
        	newPushMessgae.UserId=source.readString();
        	newPushMessgae.RegisterId = source.readString();
        	newPushMessgae.Message_Id = source.readString();
        	
        	newPushMessgae.Message_Content = source.readString();
        	newPushMessgae.Message_Time = source.readString();
        	newPushMessgae.Message_Title = source.readString();
        	newPushMessgae.Message_Type = source.readString();
        	
        	newPushMessgae.Notification_Id=source.readInt();
        	return newPushMessgae;
        }
        public PushMessage[] newArray(int size) {    
            return new PushMessage[size];    
        }    
	};
}
