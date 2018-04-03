package com.evguard.model;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * ����Track����
 * @author Administrator
 *
 */
public class Tracks {
	
	private String TAG="Tracks";
	protected final long MAX_COUNT = 3000;
	protected long mCount = 0;
	protected Context mContext = null;
	protected SQLiteDatabase  mDatabase = null;
	protected HashMap<Long,TrackInfo> mTrackInfos = null;
	
	public Tracks(Context context){		
		if(context == null) throw new NullPointerException("Context为空");
		
		mContext = context;
		mTrackInfos = new HashMap<Long,TrackInfo>();
		mDatabase = mContext.openOrCreateDatabase("gps_client.sqlite", Context.MODE_PRIVATE, null);
		
		Cursor failuresTableCursor = mDatabase.rawQuery("Select count(*) as c from sqlite_master where type ='table' and name ='Tracks'", null);
		if(failuresTableCursor == null || !failuresTableCursor.moveToFirst() || failuresTableCursor.getInt(0) <= 0){
			mDatabase.execSQL("Create table Tracks(TrackIndex NUMERIC, Speed NUMERIC, Mileage NUMERIC,Latitude NUMERIC," +
						   "Longitude NUMERIC,IsAccOn BOOL,HasGpsSignal BOOL,IsTranSport BOOL,GpsTime DATETIME," +
						   "Direct NUMERIC,IconType NUMERIC,OffsetLongitude NUMERIC,OffsetLatitude NUMERIC," +
						   "CarNum VARCHAR(100),LineType NUMERIC,HasWarning BOOL,WarningMessages VARCHAR(500))");									
		}else{
			mDatabase.execSQL("Delete from Tracks");
		}
		if(failuresTableCursor != null) failuresTableCursor.close();
	}
	
	public synchronized void addTrackInfo(TrackInfo trackInfo){
		
//		trackInfo.setIndex(mCount);
//		
//		if(mCount<MAX_COUNT){			
//			mTrackInfos.put(trackInfo.getIndex(), trackInfo);		
//		}
//							
//		String indexStr = String.valueOf(trackInfo.getIndex());
//		String speedStr = String.valueOf(trackInfo.getSpeed());
//		String mileageStr = String.valueOf(trackInfo.getDistanceDiff());
//		String latitudeStr = String.valueOf(trackInfo.getLatitude());
//		String LongitudeStr = String.valueOf(trackInfo.getLongitude());
//		String isAccOnStr = String.valueOf(trackInfo.getAcc()?1:0);
//		
//		String hasGpsSignalStr = String.valueOf(trackInfo.getGpsSignal()?1:0);
//		String isTranSportStr = String.valueOf(trackInfo.getTranSport()?1:0);
//		String gpsTimeStr = trackInfo.getGpsTime();
//		String directStr = String.valueOf(trackInfo.getDirect());
//		String iconTypeStr = String.valueOf(trackInfo.getIconType());
//		String offsetLongitudeStr = String.valueOf(trackInfo.getOffsetLongitude());
//		String offsetLatitudeStr = String.valueOf(trackInfo.getOffsetLatitude());
//		String carNum = trackInfo.getCarNum();
//		
//		String lineTypeStr = String.valueOf(trackInfo.getLineType());
//		String hasWarningStr = String.valueOf(trackInfo.getHasWarning()?1:0);
//		String warningMessages = trackInfo.getWarningMessages();			
//		
//		String trackInsertingSQL = String.format("Insert into Tracks(TrackIndex, Speed, Mileage,Latitude,Longitude,IsAccOn," +
//						"HasGpsSignal,IsTranSport,GpsTime,Direct,IconType,OffsetLongitude,OffsetLatitude,CarNum," +
//						"LineType,HasWarning,WarningMessages) " +
//						"values(%s,%s,%s,%s,%s,%s," +
//						"%s,%s,'%s',%s,%s,%s,%s,'%s'," +
//						"%s,'%s','%s')",
//						indexStr,speedStr,mileageStr,latitudeStr,LongitudeStr,isAccOnStr,
//						hasGpsSignalStr,isTranSportStr,gpsTimeStr,directStr,iconTypeStr,offsetLongitudeStr,offsetLatitudeStr,carNum,
//						lineTypeStr,hasWarningStr,warningMessages);	
//		mDatabase.execSQL(trackInsertingSQL);
//		
		mCount++;					
	}
	
	public void addTrackInfoList(List<TrackInfo> aList)
	{
		try
		{
			mDatabase.beginTransaction();   
			for(TrackInfo trackInfo : aList)
			{
				addTrackInfo(trackInfo);
			}
			mDatabase.setTransactionSuccessful();      
			mDatabase.endTransaction();        
		}
		catch(Exception ex)
		{
			Log.e(TAG, "addTrackInfoList 出错",ex);
		}
	}
	
	public long size(){
		return mCount;
	}
		
	public TrackInfo get(long index){
		if(!mTrackInfos.containsKey(index)){
			long lastIdx = index + MAX_COUNT -1;
			String trackInfoGettingSql = String.format("Select * from Tracks where TrackIndex>=%s and TrackIndex<=%s", 
					String.valueOf(index),String.valueOf(lastIdx));
			
			loadTrackInfo(trackInfoGettingSql);
		}
		
		return mTrackInfos.get(index);		
	}
	
	protected void loadTrackInfo(String gettingSql){
		
		mTrackInfos.clear();;
		
		Cursor trackInfoGettingCursor = mDatabase.rawQuery(gettingSql, null);
		if(trackInfoGettingCursor == null)
			return;
		
//		Log.d("Tracks", String.format("tracksCountFromDB��%d",trackInfoGettingCursor.getCount()));
		if(trackInfoGettingCursor.getCount() <= 0){
			trackInfoGettingCursor.close();
			return;
		}
				
		
		trackInfoGettingCursor.moveToFirst();
		do{							
			long trackInfoIndex = -1;
			int columnIndex = trackInfoGettingCursor.getColumnIndex("TrackIndex");
			if(columnIndex >=0)
				trackInfoIndex = trackInfoGettingCursor.getLong(columnIndex);
			else{
				continue;
			}				

			float speed = -1;
			columnIndex = trackInfoGettingCursor.getColumnIndex("Speed");
			if(columnIndex >=0)
				speed = trackInfoGettingCursor.getFloat(columnIndex);
			
			long mileage = -1;
			columnIndex = trackInfoGettingCursor.getColumnIndex("Mileage");
			if(columnIndex >=0)
				mileage = trackInfoGettingCursor.getLong(columnIndex);
			
			double latitude = Double.NaN;
			columnIndex = trackInfoGettingCursor.getColumnIndex("Latitude");
			if(columnIndex >=0)
				latitude = trackInfoGettingCursor.getDouble(columnIndex);
			
			double longitude = Double.NaN;
			columnIndex = trackInfoGettingCursor.getColumnIndex("Longitude");
			if(columnIndex >=0)
				longitude = trackInfoGettingCursor.getDouble(columnIndex);
			
			boolean isAccOn = false;
			columnIndex = trackInfoGettingCursor.getColumnIndex("IsAccOn");
			if(columnIndex >=0)
				isAccOn = trackInfoGettingCursor.getInt(columnIndex) == 1?true:false;
			
			boolean hasGpsSignal = false;
			columnIndex = trackInfoGettingCursor.getColumnIndex("HasGpsSignal");
			if(columnIndex >=0)
				hasGpsSignal = trackInfoGettingCursor.getInt(columnIndex) == 1?true:false;
			
			boolean isTranSport = false;
			columnIndex = trackInfoGettingCursor.getColumnIndex("IsTranSport");
			if(columnIndex >=0)
				isTranSport = trackInfoGettingCursor.getInt(columnIndex) == 1?true:false;		
			
			String gpsTime = null;
			columnIndex = trackInfoGettingCursor.getColumnIndex("GpsTime");
			if(columnIndex >=0)
				gpsTime = trackInfoGettingCursor.getString(columnIndex);
			
			int direct = 0;
			columnIndex = trackInfoGettingCursor.getColumnIndex("Direct");
			if(columnIndex >=0)
				direct = trackInfoGettingCursor.getInt(columnIndex);
			
			int iconType = -1;
			columnIndex = trackInfoGettingCursor.getColumnIndex("IconType");
			if(columnIndex >=0)
				iconType = trackInfoGettingCursor.getInt(columnIndex);	
			
			double offsetLongitude = Double.NaN;
			columnIndex = trackInfoGettingCursor.getColumnIndex("OffsetLongitude");
			if(columnIndex >=0)
				offsetLongitude = trackInfoGettingCursor.getDouble(columnIndex);			
			
			double offsetLatitude = Double.NaN;
			columnIndex = trackInfoGettingCursor.getColumnIndex("OffsetLatitude");
			if(columnIndex >=0)
				offsetLatitude = trackInfoGettingCursor.getDouble(columnIndex);			
		
			String carNum = null;
			columnIndex = trackInfoGettingCursor.getColumnIndex("CarNum");
			if(columnIndex >=0)
				carNum = trackInfoGettingCursor.getString(columnIndex);			
			
			int lineType = -1;
			columnIndex = trackInfoGettingCursor.getColumnIndex("LineType");
			if(columnIndex >=0)
				lineType = trackInfoGettingCursor.getInt(columnIndex);		
			
			boolean hasWarning = false;
			columnIndex = trackInfoGettingCursor.getColumnIndex("HasWarning");
			if(columnIndex >=0)
				hasWarning = trackInfoGettingCursor.getInt(columnIndex) == 1?true:false;		
			
			String warningMessages = null;
			columnIndex = trackInfoGettingCursor.getColumnIndex("WarningMessages");
			if(columnIndex >=0)
				warningMessages = trackInfoGettingCursor.getString(columnIndex);		
			
			TrackInfo trackInfo = new TrackInfo();
//			trackInfo.setIndex(trackInfoIndex);
//			trackInfo.setSpeed(speed);
//			trackInfo.setDistanceDiff(mileage);
//			trackInfo.setLatitude(latitude);
//			trackInfo.setLongitude(longitude);
//			trackInfo.setAcc(isAccOn);
//			trackInfo.setGpsSignal(hasGpsSignal);
//			trackInfo.setTranSport(isTranSport);
//			trackInfo.setGpsTime(gpsTime);
//			trackInfo.setDirect(direct);
//			trackInfo.setIconType(iconType);
//			trackInfo.setOffsetLongitude(offsetLongitude);
//			trackInfo.setOffsetLatitude(offsetLatitude);
//			trackInfo.setCarNum(carNum);
//			trackInfo.setLineType(lineType);
//			trackInfo.setHasWarning(hasWarning);
//			trackInfo.setWarningMessages(warningMessages);
			
			mTrackInfos.put(trackInfoIndex, trackInfo);
		
		}while(trackInfoGettingCursor.moveToNext());
		
		trackInfoGettingCursor.close();
	}
	
	public void clear(){
		if(mTrackInfos != null){
			mTrackInfos.clear();
		}			
	}
	
	public void release(){
		if(mDatabase != null && mDatabase.isOpen()){
			mDatabase.close();
		}
	}

}
