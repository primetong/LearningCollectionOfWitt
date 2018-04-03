package com.evguard.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

public class DataBaseHelper extends SQLiteOpenHelper {

	private  InputStream in=null;
	private  Context     mContext=null;
	private  BufferedReader mBufferedReader=null;
	public DataBaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		mContext=context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			in = mContext.getAssets().open("country.sql");
			mBufferedReader = new BufferedReader(new InputStreamReader(in));
			String sqlUpdate = null;
		     while ((sqlUpdate = mBufferedReader.readLine()) != null) {
		           if (!TextUtils.isEmpty(sqlUpdate)) {
		        	   db.execSQL(sqlUpdate);
		           }
		     }
		     in = mContext.getAssets().open("weather.sql");
			 mBufferedReader = new BufferedReader(new InputStreamReader(in));
			 while ((sqlUpdate = mBufferedReader.readLine()) != null) {
			      if (!TextUtils.isEmpty(sqlUpdate)) {
			    	   db.execSQL(sqlUpdate);
			      }
			 }
			 mBufferedReader.close();
		     in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
	

}
