package com.evguard.image;

public class ImageFolder {
	
	private String sFolderName="";
	private String sFolderDir="";
	private int iFolderImageCount=0;
	private String sFirstImagePath="";
	
	
	
	public void setFolderName(String s)
	{
		this.sFolderName=s;
	}
	public String getFolderName()
	{
		return this.sFolderName;
	}
	
	public void setFolderDir(String s)
	{
		this.sFolderDir=s;
	}
	public String getFolderDir(){
		return this.sFolderDir;
	}
	public void setFolderImageCount(int i)
	{
		this.iFolderImageCount=i;
	}
	public int getFolderImageCount(){
		return this.iFolderImageCount;
	}
	public void setFirstImagePath(String s)
	{
		this.sFirstImagePath=s;
	}
	public String getFirstImagePath()
	{
		return this.sFirstImagePath;
	}
	
	

}
