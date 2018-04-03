package com.evguard.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.text.format.DateFormat;

public class OperateRecord {

	private int OperateIndex=-1;
	private String CarId="";
	private String OperateTime;
	private int OperateType=-1;
	private int OperateRes=-1;
	
	public OperateRecord(int iindex,String scarid,String operatedate,int operatetype,int Operateres)
	{
		this.OperateIndex=iindex;
		this.CarId=scarid;
		this.OperateType=operatetype;
		this.OperateRes=Operateres;
	    this.OperateTime=operatedate;
		
	}
	
	public int getOperateIndex()
	{
		return this.OperateIndex;
	}
	
	public String getCarId()
	{
		return this.CarId;
	}
	
	public String getOperateTime()
	{
		return this.OperateTime;
	}
	
	public int getOperateType()
	{
		return this.OperateType;	
	}
	
	public int getOperateRes()
	{
		return this.OperateRes;
	}
}
