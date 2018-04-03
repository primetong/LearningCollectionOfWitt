package com.xinghaicom.security;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Passport {

	protected String mSystemID = null;
	protected GLSClient mGLSClient = null; //new GLSClient( "http://120.35.20.145:8181/MirrorWebServices/MirrorGLSService.asmx");
	protected int mIMaxOnline = 0;
	protected Machine mMachine = null;
	protected AuthorityHandler mAuthorityHandler = null;
	protected Thread mAuthorityChecking = null;
	
	public Passport(String systemID,String glsAddress,int connectionTimeout,int soTimeout){
		mSystemID = systemID;
		mGLSClient = new GLSClient(glsAddress,connectionTimeout,soTimeout);			
	}
	
	public void resetServiceURI(String serviceURI){
		mGLSClient.resetServiceURI(serviceURI);
	}
				
	public AuthorityResult checkAuthority(Machine machine) throws Exception
	{
		if(machine == null) throw new Exception("Machine��Ч");
		
		mMachine = machine;
		
		String machineID = mMachine.getID();
		boolean hasDog = mMachine.hasDog();
		String ipAddress = mMachine.getIPAddress();							
		
		GLSClient.EInfo eInfo = mGLSClient.getEInfo(machineID, hasDog, mSystemID, ipAddress);
		if(eInfo == null){
			return null;
		}
		
		int e = eInfo.getE();
		if(e != 0){
			String resultMsg = eInfo.getErrMsg();
			if(resultMsg != null) resultMsg.trim();			
			return new AuthorityResult(false,resultMsg);
		}
		
		String authorityID = eInfo.getEmpowerCd();

		return checkReceivedAuthorityID(authorityID,hasDog,machineID);
	}
	
	public void startCheckAuthority(Machine machine,AuthorityHandler authorityHandler) throws Exception{
		if(machine == null) throw new Exception("Machine��Ч");
		if(authorityHandler == null) throw new Exception("AuthorityHandler��Ч");
		
		mMachine = machine;
		mAuthorityHandler = authorityHandler;
		mAuthorityChecking  = new Thread(){
			public void run() {
				super.run();
				Message msg  = mAuthorityHandler.obtainMessage();				
				try {
					msg.obj = checkAuthority(mMachine);
				} catch (Exception e) {
					// TODO Auto-generated catch block					
					Log.e("Passport", "��Ȩʧ�ܣ�" + e.getMessage());
					msg.obj = new AuthorityResult(false, e.getMessage());
				}
				mAuthorityHandler.sendMessage(msg);
			}
		};
		mAuthorityChecking.start();
	}
	
	protected AuthorityResult checkReceivedAuthorityID(String authorityID,boolean hasDog,String machineID) throws Exception{
		
        //�м��ܹ�������Ȩ���ǿյģ���ͨ����
        if (hasDog && (authorityID == null || authorityID.length() <= 0)) {
            mIMaxOnline = 5000;
            return new AuthorityResult(true, "��Ȩͨ��");
        }
        
        //return new AuthorityResult(true, "��Ȩͨ����");
        
        if (authorityID == null || authorityID.length() <= 0) return new AuthorityResult(false, "������Ȩ��ʧ��");
        int index = authorityID.indexOf('|');    //��ȡƽ̨����Ȩ��
        if (index <= 0) return new AuthorityResult(false, "������Ȩ��ʧ��");
             
        AuthorityIDDecipherer authorityIDDecipherer = new AuthorityIDDecipherer();
        String deAuthorityID = authorityIDDecipherer.decipher(authorityID.substring(0, index));
        if (deAuthorityID == null || deAuthorityID.length() <= 0) return new AuthorityResult(false, "������Ȩ��ʧ��");

        if (deAuthorityID.length() < 24 + 10 + 15) return new AuthorityResult(false, "������Ȩ��ʧ��");

        index = deAuthorityID.indexOf("|");
        if (-1 == index) return new AuthorityResult(false, "������Ȩ��ʧ��");
        String recivedMachineID = deAuthorityID.substring(0, index);
        deAuthorityID = deAuthorityID.substring(index + 1);

        index = deAuthorityID.indexOf("|");
        if (-1 == index) return new AuthorityResult(false, "������Ȩ��ʧ��");
        String recivedSystemId = deAuthorityID.substring(0, index);
        deAuthorityID = deAuthorityID.substring(index + 1);

        index = deAuthorityID.indexOf("|");
        if (-1 == index) return new AuthorityResult(false, "������Ȩ��ʧ��");
        String recivedDateTime = deAuthorityID.substring(0, index);
        deAuthorityID = deAuthorityID.substring(index + 1);

        index = deAuthorityID.indexOf("|");
        if (-1 == index) return new AuthorityResult(false, "������Ȩ��ʧ��");
        String recivedDays = deAuthorityID.substring(0, index);
        deAuthorityID = deAuthorityID.substring(index + 1);

        index = deAuthorityID.indexOf("|");
        if (-1 == index)
        {
            mIMaxOnline = Integer.parseInt(deAuthorityID);
        }
        else
        {
            mIMaxOnline = Integer.parseInt(deAuthorityID.substring(0, index));
        }
        
        
        //�������Ƿ�ƥ��
        if (!machineID.contentEquals(recivedMachineID))
            return new AuthorityResult(false, "������Ȩ������ʧ��");

        if (!mSystemID.contentEquals(recivedSystemId))
            return new AuthorityResult(false, "������Ȩϵͳ��ʶʧ��");
        
        long recivedDateTimeValue = -1;
        try{
        	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        	recivedDateTimeValue = dateFormat.parse(recivedDateTime).getTime();
        	//recivedDateTimeValue = Date.parse(recivedDateTime);
        }catch(Exception e){
        	return new AuthorityResult(false, "��Чʱ���ʽ����");
        }
                
//        if (recivedDateTimeValue < 0)
//            return new AuthorityResult(false, "��Ч����Ȩ����");
        
        int recivedDaysValue = -1;
        try{
        	recivedDaysValue = Integer.parseInt(recivedDays);
        }catch(Exception e){
        	return new AuthorityResult(false, "��Ч����Ȩ����Ч����");
        }        
        if(recivedDaysValue < 0)
        	return new AuthorityResult(false, "��Ч����Ȩ����Ч����");
               
        long beginDate = recivedDateTimeValue - (long)recivedDaysValue*24*3600*1000;
        long now = System.currentTimeMillis();

        long timespanEnd = recivedDateTimeValue - now;
        long timespanBegin = now - beginDate;
        if (timespanEnd < 0 || timespanBegin < 0)
            return new AuthorityResult(false, "��Ȩ�ѹ���");
        
        return new AuthorityResult(true, "��Ȩͨ��");
	}
	
	public void release(){
		if(mGLSClient != null) mGLSClient.release();
	}
}
