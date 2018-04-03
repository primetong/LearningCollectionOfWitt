package com.evguard.main;

import android.content.Context;

import com.evguard.model.WebReq_GetCarMedical;
import com.evguard.model.WebReq_GetCurrentPos;
import com.evguard.model.WebRes_GetCarMedical;
import com.evguard.model.WebRes_GetCurrentPos;
import com.xinghaicom.asynchrony.LoopHandler;

public class Thread_GetDiagnose extends Thread_Base {

	private CallBack_GetDiagnose mCallBack_GetDiagnose=null;

	private final int ACTION_GETDIAGNOSE_WHOLECAR=0;
	private final int ACTION_GETDIAGNOSE_ELE_CONTROL=1;
	private final int ACTION_GETDIAGNOSE_BATTERY=2;
	private final int ACTION_GETDIAGNOSE_MACHINE=3;
	public Thread_GetDiagnose(Context context, LoopHandler loopHandler,
			CallBack_GetDiagnose aCallBack_GetDiagnose) {
		super(context, loopHandler);
		if (context == null)
			throw new NullPointerException("Context无效");
		mCallBack_GetDiagnose = aCallBack_GetDiagnose;
		
	}
	public void getWholeCar(){
		WebReq_GetCarMedical aWebReq_GetCarMedical = new WebReq_GetCarMedical("1");
		super.request(ACTION_GETDIAGNOSE_WHOLECAR, aWebReq_GetCarMedical);
	}
	public void getEleControl(){
		WebReq_GetCarMedical aWebReq_GetCarMedical = new WebReq_GetCarMedical("2");
		super.request(ACTION_GETDIAGNOSE_ELE_CONTROL, aWebReq_GetCarMedical);
	}
	public void getBattery(){
		WebReq_GetCarMedical aWebReq_GetCarMedical = new WebReq_GetCarMedical("3");
		super.request(ACTION_GETDIAGNOSE_BATTERY, aWebReq_GetCarMedical);
	}
	public void getMachine(){
		WebReq_GetCarMedical aWebReq_GetCarMedical = new WebReq_GetCarMedical("4");
		super.request(ACTION_GETDIAGNOSE_MACHINE, aWebReq_GetCarMedical);
	}
	@Override
	protected void onRunning(int action, Object obj) {
		switch (action) {
		
		case ACTION_GETDIAGNOSE_WHOLECAR: // 处理整车
			getDiagnose_WholeCar((WebReq_GetCarMedical)obj);
			break;
		case ACTION_GETDIAGNOSE_ELE_CONTROL:
			getDiagnose_EleControl((WebReq_GetCarMedical)obj);
			break;
		case ACTION_GETDIAGNOSE_BATTERY:
			getDiagnose_Battery((WebReq_GetCarMedical)obj);
			break;
		case ACTION_GETDIAGNOSE_MACHINE:
			getDiagnose_Machine((WebReq_GetCarMedical)obj);
			break;
		}

	}
	private void getDiagnose_WholeCar(WebReq_GetCarMedical aWebReq_GetCarMedical){
		mCallBack_GetDiagnose.getDiagnoseing();
		WebRes_GetCarMedical aWebRes_GetCarMedical=new WebRes_GetCarMedical();
		try {
			Thread.sleep(3000);
			super.executeWithConfirmLogon(aWebReq_GetCarMedical, aWebRes_GetCarMedical);
			if(mCallBack_GetDiagnose==null)return;
			if(aWebRes_GetCarMedical.getResult().equals("0")){
				mCallBack_GetDiagnose.getDiagnoseOK(aWebRes_GetCarMedical,1);
				Thread.sleep(3000);
				this.getEleControl();
			}else{
				mCallBack_GetDiagnose.getDiagnoseFailed(aWebRes_GetCarMedical.getMessage());
			}
		} catch (Exception e) {
			
			e.printStackTrace();
			mCallBack_GetDiagnose.getDiagnoseFailed(e.getMessage());
		}
		
	}
	private void getDiagnose_EleControl(WebReq_GetCarMedical aWebReq_GetCarMedical){
		WebRes_GetCarMedical aWebRes_GetCarMedical=new WebRes_GetCarMedical();
		try {
			super.executeWithConfirmLogon(aWebReq_GetCarMedical, aWebRes_GetCarMedical);
			if(mCallBack_GetDiagnose==null)return;
			if(aWebRes_GetCarMedical.getResult().equals("0")){
				mCallBack_GetDiagnose.getDiagnoseOK(aWebRes_GetCarMedical,2);
				Thread.sleep(3000);
				this.getBattery();
			}else{
				mCallBack_GetDiagnose.getDiagnoseFailed(aWebRes_GetCarMedical.getMessage());
			}
		} catch (Exception e) {
			
			e.printStackTrace();
			mCallBack_GetDiagnose.getDiagnoseFailed(e.getMessage());
		}
		
	}
	private void getDiagnose_Battery(WebReq_GetCarMedical aWebReq_GetCarMedical){
		WebRes_GetCarMedical aWebRes_GetCarMedical=new WebRes_GetCarMedical();
		try {
			super.executeWithConfirmLogon(aWebReq_GetCarMedical, aWebRes_GetCarMedical);
			if(mCallBack_GetDiagnose==null)return;
			if(aWebRes_GetCarMedical.getResult().equals("0")){
				mCallBack_GetDiagnose.getDiagnoseOK(aWebRes_GetCarMedical,3);
				Thread.sleep(3000);
				this.getMachine();
			}else{
				mCallBack_GetDiagnose.getDiagnoseFailed(aWebRes_GetCarMedical.getMessage());
			}
		} catch (Exception e) {
			
			e.printStackTrace();
			mCallBack_GetDiagnose.getDiagnoseFailed(e.getMessage());
		}
		
	}
	private void getDiagnose_Machine(WebReq_GetCarMedical aWebReq_GetCarMedical){
		WebRes_GetCarMedical aWebRes_GetCarMedical=new WebRes_GetCarMedical();
		try {
			super.executeWithConfirmLogon(aWebReq_GetCarMedical, aWebRes_GetCarMedical);
			if(mCallBack_GetDiagnose==null)return;
			if(aWebRes_GetCarMedical.getResult().equals("0")){
				mCallBack_GetDiagnose.getDiagnoseOK(aWebRes_GetCarMedical,4);
			}else{
				mCallBack_GetDiagnose.getDiagnoseFailed(aWebRes_GetCarMedical.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			mCallBack_GetDiagnose.getDiagnoseFailed(e.getMessage());
		}
	}
	public interface CallBack_GetDiagnose{
		public void getDiagnoseing();
		public void getDiagnoseOK(WebRes_GetCarMedical aWebRes_GetCarMedical,int flag);
		public void getDiagnoseFailed(String sError);
	}
	
	 
}
