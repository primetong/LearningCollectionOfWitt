package com.xinghaicom.security;

public class Machine {
    protected String mID = null;
    protected boolean mHasDog = false;
    protected String mIpAddress = null;
    
    public Machine(String ipAddress,String machineID) throws Exception{    	
    	if(ipAddress == null || ipAddress.length() <= 0) throw new IllegalArgumentException("电话号码无效！");

    	mIpAddress = ipAddress;
    	if(machineID != null){
    		mID = new MD5().hashTextMD5(machineID).trim();
    	}
    }
        
    public String getID() throws Exception{
    	
        if (mHasDog){
        	throw new Exception("暂未实现硬件狗授权方式！");
        }else{
	    	//if(mID == null) mID = new MD5().hashTextMD5(machineID);//"2013265920" + "0023AE94C69E");
	    	return mID;//.trim();
        }
    }    
            
    public boolean hasDog(){    	
        return mHasDog;
    }
    
    public String getIPAddress(){
    	//if(mIpAddress == null) mIpAddress = null;    	
    	return mIpAddress;
    }
}
