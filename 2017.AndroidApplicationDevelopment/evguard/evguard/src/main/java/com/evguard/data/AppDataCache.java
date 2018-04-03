package com.evguard.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.location.Location;

import com.amap.api.location.AMapLocation;
import com.baidu.mapapi.model.LatLng;
import com.evguard.model.AddressPoint;
import com.evguard.model.CarCurPositionInfo;
import com.evguard.model.CarInfo;
import com.evguard.model.TrackInfo;

public class AppDataCache {
 
	
	private static AppDataCache mAppDataCache;
	private static Object lockObj=new Object();
	
	
	public static AppDataCache getInstance()
	{
		if(mAppDataCache==null)
		synchronized(lockObj)
		{
			mAppDataCache=new AppDataCache();
		}
		return mAppDataCache;
	}
	private  long LastReqTime=System.currentTimeMillis();
	//当前孩子的末次位置
	private CarCurPositionInfo mCurrentKidCurPositionInfo=null;
	//末次位置信息表
	private ArrayList<CarCurPositionInfo> mKidCurPositionInfoList=new ArrayList<CarCurPositionInfo>();
	//当前孩子基础信息
//	private CarBaseInfo mCurrentKidBaseInfo=null;
	private CarInfo mCurrentKidInfo=null;
	//孩子基础信息表
//	private Map<String,CarBaseInfo> mKidBaseInfoList=new HashMap<String,CarBaseInfo>();
	private Map<String,CarInfo> mKidInfoList=new HashMap<String,CarInfo>();

	private AMapLocation mAMapLocation;
	
	private String mRegisterId="";
	private String mCurCityCode="";
	private int mDefaultMapZoomLevel=12;
	
	
	public ArrayList<CarCurPositionInfo> getKidCurPositionInfoList()
	{
		return this.mKidCurPositionInfoList;
	}
	public void setKidCurPositionInfoList(CarCurPositionInfo aCarCurPositionInfo){
		 this.mKidCurPositionInfoList.clear();
		 this.mKidCurPositionInfoList.add(aCarCurPositionInfo);
	}
	public Map<String,CarInfo> getKidInfoList()
	{
		return this.mKidInfoList;
	}
	public void setKidInfoList(Map<String,CarInfo> alist)
	{
		 this.mKidInfoList.clear();
		 this.mKidInfoList.putAll(alist);
	}
	
	public void setCurrentKidCurPositionInfo(CarCurPositionInfo aCarCurPositionInfo)
	{
		mCurrentKidCurPositionInfo=aCarCurPositionInfo;
	}
	
	public CarCurPositionInfo getCurrentKidCurPositionInfo()
	{
		return mCurrentKidCurPositionInfo;
	}
	public void setCurrentKidInfo(CarInfo aKidBaseInfo)
	{
		this.mCurrentKidInfo=aKidBaseInfo;
	}
	public CarInfo getCurrentKidInfo()
	{
		return this.mCurrentKidInfo;
	}
	
	public void setCurCityCode(String s)
    {
    	this.mCurCityCode=s;
    }
    public String getCurCityCode()
    {
    	return this.mCurCityCode;
    }
    public int getDefaultMapZoomLevel()
    {
    	return this.mDefaultMapZoomLevel;
    }
    public String getRegisterId(){
    	return this.mRegisterId;
    }
    public void setRegisterId(String s)
    {
    	this.mRegisterId=s;
    }
    public void setAMapLocation(AMapLocation aAMapLocation)
    {
    	this.mAMapLocation=aAMapLocation;
    }
    public AMapLocation getAMapLocation()
    {
    	return this.mAMapLocation;
    }
    public void setLastReqTime(long l){
    	this.LastReqTime=l;
    }
    public long getLastReqTime(){
    	return this.LastReqTime;
    }

    private int TrackCount=0;
    public void setTrackCount(int i){
    	this.TrackCount=i;
    }
	public int getTrackCount() {
		// TODO Auto-generated method stub
		return this.TrackCount;
	}

	private  List<TrackInfo> TrackList=new ArrayList<TrackInfo>();
	public void setTrackList(List<TrackInfo> alist){
		this.TrackList.clear();
		this.TrackList.addAll(alist);
	}
	public List<TrackInfo> getTrackList() {
		// TODO Auto-generated method stub
		return this.TrackList;
	}
	private  List<AddressPoint> AddressList=new ArrayList<AddressPoint>();
	public void setAddressList(List<AddressPoint> alist){
		this.AddressList.clear();
		this.AddressList.addAll(alist);
	}
	
	public List<AddressPoint> getAddressList() {
		return this.AddressList;
	}
	
	
	private String serverTime = null;
	public String getServerTime() {
		return serverTime;
	}
	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}
	
	
	private int messageId = 1;
	public int getMessageId() {
		return messageId;
	}
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
	
	private LatLng mLatlng;
	public LatLng getLatlng() {
		return mLatlng;
	}
	public void setLatlng(LatLng mLatlng) {
		this.mLatlng = mLatlng;
	}
	
	
	private int score;
	private int checkItemNum;
	private String time = "";
	private int lastScore;
	private int lastCheckItemNum;
	private String lastTime = "";

	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getCheckItemNum() {
		return checkItemNum;
	}
	public void setCheckItemNum(int checkItemNum) {
		this.checkItemNum = checkItemNum;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getLastScore() {
		return lastScore;
	}
	public void setLastScore(int lastScore) {
		this.lastScore = lastScore;
	}
	public int getLastCheckItemNum() {
		return lastCheckItemNum;
	}
	public void setLastCheckItemNum(int lastCheckItemNum) {
		this.lastCheckItemNum = lastCheckItemNum;
	}
	public String getLastTime() {
		return lastTime;
	}
	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}
	
	public boolean isDiagnose() {
		return isDiagnose;
	}
	public void setDiagnose(boolean isDiagnose) {
		this.isDiagnose = isDiagnose;
	}
	public boolean isDiagnoseDone() {
		return isDiagnoseDone;
	}
	public void setDiagnoseDone(boolean isDiagnoseDone) {
		this.isDiagnoseDone = isDiagnoseDone;
	}
	private boolean isDiagnose = false;
	private boolean isDiagnoseDone = false;
	
	private Location location = null;
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	
}
