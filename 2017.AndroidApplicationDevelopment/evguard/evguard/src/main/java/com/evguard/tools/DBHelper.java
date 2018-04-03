package com.evguard.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import com.evguard.model.MessageInfo;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	private String TAG="DBHelper";
//	private static String dbName="smartbracelet.sqlite";
	private static String dbName="evguard.sqlite";
	private static int dbVersion=1;
	private Context mContext = null;
	protected SQLiteDatabase mDatabase = null;

	public DBHelper(Context context, String name, CursorFactory factory,
			int version ) {
		super(context, name, factory, version);
		
	}
	
	public DBHelper(Context context)
	{
		super(context, dbName, null, dbVersion);
		mContext = context;
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		try
		{
			//轨迹表
			Log.i(TAG,"create tables Tracks");
			StringBuffer sbStr=new StringBuffer();
			sbStr.append("CREATE TABLE IF NOT EXISTS Tracks");
			sbStr.append("(TrackIndex NUMERIC, Speed NUMERIC, Mileage NUMERIC,Latitude NUMERIC," +
						   "Longitude NUMERIC,IsAccOn BOOL,HasGpsSignal BOOL,IsTranSport BOOL,GpsTime DATETIME," +
						   "Direct NUMERIC,IconType NUMERIC,OffsetLongitude NUMERIC,OffsetLatitude NUMERIC," +
						   "CarNum VARCHAR(100),LineType NUMERIC,HasWarning BOOL,WarningMessages VARCHAR(500))");
			db.execSQL(sbStr.toString());

			Log.i(TAG,"create tables Tracks");
			
//			StringBuffer sbStr2=new StringBuffer();
//			sbStr2.append("CREATE TABLE IF NOT EXISTS KidsHeads");
//			sbStr2.append("(KidIndex INTEGER PRIMARY KEY,KidId VARCHAR(100),KidHead BLOB)");
//			db.execSQL(sbStr2.toString());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(oldVersion==1 && newVersion==2)
		{
			Log.i(TAG,"update tables");
			StringBuffer sbStr=new StringBuffer();
			sbStr.append("CREATE TABLE IF NOT EXISTS Tracks");
			sbStr.append("(TrackIndex NUMERIC, Speed NUMERIC, Mileage NUMERIC,Latitude NUMERIC," +
						   "Longitude NUMERIC,IsAccOn BOOL,HasGpsSignal BOOL,IsTranSport BOOL,GpsTime DATETIME," +
						   "Direct NUMERIC,IconType NUMERIC,OffsetLongitude NUMERIC,OffsetLatitude NUMERIC," +
						   "CarNum VARCHAR(100),LineType NUMERIC,HasWarning BOOL,WarningMessages VARCHAR(500))");
			db.execSQL(sbStr.toString());
			

		}
	}

	
	
    //==========================================================================================//
    


	private byte[] drawableToBlob(Drawable drawable)
	{
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			((BitmapDrawable)drawable).getBitmap().compress(CompressFormat.PNG, 100, baos);//ѹ��ΪPNG��ʽ,100��ʾ��ԭͼ��Сһ��
			byte[] b=baos.toByteArray();
			baos.close();
			return b;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	private Drawable boblToDrawable(byte[] blob)
	{
		try {
			ByteArrayInputStream  bis=new ByteArrayInputStream(blob);
			Drawable drawable=Drawable.createFromStream(bis, "a");
			return drawable;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public void initDatabase() {
		mDatabase = mContext.openOrCreateDatabase("message.sqlite",
				Context.MODE_PRIVATE, null);
		Cursor failuresTableCursor = mDatabase
				.rawQuery(
						"Select count(*) as c from sqlite_master where type ='table' and name ='Messages'",
						null);
		if (failuresTableCursor == null || !failuresTableCursor.moveToFirst()
				|| failuresTableCursor.getInt(0) <= 0) {
			
			mDatabase
					.execSQL("Create table Messages(ID INT,Content VARCHAR(100),MsgType VARCHAR(100),Time VARCHAR(100),Title VARCHAR(100),IsMsgReaded VARCHAR(100))");
		} else {
			
			// mDatabase.execSQL("Delete from Messages");
		}
		if (failuresTableCursor != null)
			failuresTableCursor.close();
	}
	
	public void closeDB() {
		if (mDatabase != null) {
			mDatabase.close();
		}
	}
	
	public void addMessage(MessageInfo messageInfo) {
		try {
			String sql1 = "select * from Messages where ID ="
					+ messageInfo.getID();
			if(!mDatabase.isOpen()){
				this.initDatabase();
			}
			Cursor cursor = mDatabase.rawQuery(sql1, null);
//			boolean ismessageReaded = false;
//			int messageReaded;
			
			if (cursor != null || !cursor.moveToFirst()) {
//				ismessageReaded = messageInfo.isMessageReaded();
//				if(ismessageReaded){
//					messageReaded = 0;
//				} else {
//					messageReaded = 1;
//				}
				System.out.println("insert--curID==" + messageInfo.getID() + ", IsMsgReaded==" + messageInfo.getIsMsgReaded());
				String sql = String
						.format("Insert into Messages(ID,Content,MsgType,Time,Title,IsMsgReaded)"
								+ "values('%s','%s','%s','%s','%s','%s')",
								messageInfo.getID(),messageInfo.getContent(), messageInfo.getMsgType(),
								messageInfo.getTime().replace("/", "-"),
								messageInfo.getTitle(),
								messageInfo.getIsMsgReaded()
//								,pushMessage.getDate()
								);
				mDatabase.execSQL(sql);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mDatabase != null) {
				mDatabase.close();
			}
		}

	}
	
	public void delete() {
		try {
			if (mDatabase != null) {
				mDatabase.execSQL("Delete from Messages");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mDatabase != null) {
				mDatabase.close();
			}
		}
	}

	public Cursor query() {
		try {
			if(!mDatabase.isOpen()){
				this.initDatabase();
			}
			// String sql = "select * from Messages order by CreateTime desc";
			String sql = "select * from Messages order by ID desc";
			System.out.println("do query--sql:" + sql);
			Cursor cursor = mDatabase.rawQuery(sql, null);
			return cursor;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void updateStatus(int ID){
		if(!mDatabase.isOpen()){
			this.initDatabase();
		}
		String sql = "update Messages set IsMsgReaded = "+ ConstantTool.MSG_READED +" where ID=" + ID;
		mDatabase.execSQL(sql);
	}
	public void deleteOneMessage(int ID){
		if(!mDatabase.isOpen()){
			this.initDatabase();
		}
		String sql = "delete from Messages where ID=" + ID;
		System.out.println("sql---" + sql);
		mDatabase.execSQL(sql);
	}
	
	public Cursor queryUnReadMessage(){
		try {
			if(!mDatabase.isOpen()){
				this.initDatabase();
			}
			String sql = "select * from Messages where IsMsgReaded = " + ConstantTool.MSG_UNREAD;
			System.out.println("insert--curID==sql---" + sql);
			Cursor cursor = mDatabase.rawQuery(sql, null);
			return cursor;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
