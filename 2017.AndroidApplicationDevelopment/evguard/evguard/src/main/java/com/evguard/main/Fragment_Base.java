package com.evguard.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.evguard.data.AppDataCache;
import com.evguard.model.CarInfo;
import com.evguard.tools.LogEx;

public abstract class Fragment_Base extends Fragment {

	private String TAG="Fragment_Base";
	protected Context mContext=null;
	protected View layoutView;
	protected  boolean bIsPause=false;
	protected Toast mToast=null;
	protected CarInfo mKidBaseInfo;
	protected App_Settings mSettings;
	Bundle savedState;
	
	@Override
	public void onAttach(Activity activity) {
		 super.onAttach(activity);
         LogEx.i(TAG, this.getClass().getName()+" onAttach");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		try {
			LogEx.i(TAG, this.getClass().getName()+" onCreate");
			super.onCreate(savedInstanceState);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogEx.i(TAG, this.getClass().getName()+" onCreateView");
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	

	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		LogEx.i(TAG , this.getClass().getName()+" onActivityCreated");
		super.onActivityCreated(savedInstanceState);
		mContext=this.getActivity();
		mSettings = new App_Settings(mContext);
		if(savedInstanceState !=null){
			restoreStateFromArguments();
			return;
		}
		initData();
	}
	@Override
	public void onResume()
	{
		LogEx.i(TAG, this.getClass().getName()+" onResume");
		super.onResume();
	}
	
	 @Override
	   public void onHiddenChanged(boolean hidden){
		   LogEx.i(TAG, this.getClass().getName()+" onHiddenChanged");
			super.onHiddenChanged(hidden);
			if(hidden){
				
			} else {
				initData();
			}
			
	   }
   
	@Override
	public void onPause()
	{
		LogEx.i(TAG, this.getClass().getName()+" onPause");
		super.onPause();
		
	}
	@Override
	public void onSaveInstanceState(Bundle outState){
		LogEx.i(TAG, this.getClass().getName()+" onSaveInstanceState");
		super.onSaveInstanceState(outState); 
		saveStateToArguments();
	}
	@Override
	public void onStop()
	{
		LogEx.i(TAG, this.getClass().getName()+" onStop");
		bIsPause=true;
		super.onStop();
		
	}
	 @Override
	 public void onDestroyView() { 
		 LogEx.i(TAG , this.getClass().getName()+" onDestroyView");
		 super .onDestroyView();
		 if (null != layoutView) {
			 ((ViewGroup) layoutView.getParent()).removeView(layoutView);
			 } 
	}
	@Override
	public void onDestroy()
	{		
		LogEx.i(TAG , this.getClass().getName()+" onDestroy");
		super.onDestroy();

	}
	public void setCarInfo(CarInfo aCarInfo){
		this.mKidBaseInfo=aCarInfo;
	}
	protected void showToast(String txt){
		if(mToast==null)mToast=Toast.makeText(mContext, txt, Toast.LENGTH_LONG);
		mToast.setText(txt);
		mToast.show();
	}

	
	public void initData(){}
	
	private void saveStateToArguments() {
		   if (getView() != null){
			   System.out.println("llj-021-here");
			   Bundle b=new Bundle();
		      savedState = saveState(b);
		   }
		   if (savedState != null) {
		      Bundle b = getArguments();
		      System.out.println("llj-022-here");
		      if(b == null)  System.out.println("llj-023-here");
		      else{
		    	  b.putBundle("savedState", savedState);
		    	  System.out.println("llj-024-here"); 
		      }
		      
		   }
		}
	private boolean restoreStateFromArguments() {
		   Bundle b = getArguments();
		   savedState = b.getBundle("savedState");
		   if (savedState != null) {
		      restoreState(savedState);
		      return true;
		   }
		   return false;
		}
	protected abstract void restoreState(Bundle saveState); 
		//////////////////////////////
		// 淇濆瓨鐘舵�佹暟鎹�
		//////////////////////////////
	protected abstract Bundle saveState(Bundle saveState);
	
}
