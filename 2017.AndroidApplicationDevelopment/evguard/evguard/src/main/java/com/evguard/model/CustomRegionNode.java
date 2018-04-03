package com.evguard.model;

public class CustomRegionNode {
	
	private double mLongitude=0.0;
	private double mLatitude=0.0;
	
	public CustomRegionNode(double latitude,double longtitude )
	{
		this.mLongitude=longtitude;
		this.mLatitude=latitude;
	}
	
	public double getLongitude()
	{
		return this.mLongitude;
	}
	public void setLongitude(double d)
	{
		this.mLongitude=d;
	}
	
	public double getLatitude()
	{
		return this.mLatitude;
	}
	public void setLatitude(double d)
	{
		this.mLatitude=d;
	}

}
