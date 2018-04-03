package com.evguard.main;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.xinghaicom.evguard.R;

public class Dg_Base extends DialogFragment {

	protected Context mContext;
	 protected OnCancelListener mOnCancelListener;

	 @Override  
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
	            Bundle savedInstanceState)  
	    {  
		    mContext=App_Application.getInstance();
		    getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);  
	        View view = inflater.inflate(R.layout.dg_base, container);  
	        initialize(view,savedInstanceState);
	       
	        return view;  
	    }  
 
	
	 protected void initialize(View view,Bundle savedInstanceState) {}
	 
	 @Override //仅用于状态跟踪
     public void onCancel(DialogInterface dialog) {  
      
        super.onCancel(dialog); 
    }      

    @Override  //仅用户状态跟踪
    public void onDismiss(DialogInterface dialog) {  
        super.onDismiss(dialog); 
    } 
	    
    @Override
    public void show(FragmentManager manager,String tag){
    	try {
    		  FragmentTransaction ft = manager.beginTransaction();
//    		  if(this.isAdded())  ft.remove(this);
    	      ft.add(this, tag);
    	      ft.commitAllowingStateLoss();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void clear(FragmentManager manager){
    	try {
    		  FragmentTransaction ft = manager.beginTransaction();
    	      ft.remove(this);
    	      ft.commitAllowingStateLoss();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Override
    public void dismiss(){
//    	if(this.isAdded()){
    		super.dismissAllowingStateLoss();
//    	}
    }
	 public void setOnCancelListener(OnCancelListener aOnCancelListener){
		 this.mOnCancelListener=aOnCancelListener;
	 }
	 public interface OnCancelListener{
		 public void onCancle();
	 }
}
