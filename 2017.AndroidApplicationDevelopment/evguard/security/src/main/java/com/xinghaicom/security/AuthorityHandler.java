package com.xinghaicom.security;

import android.os.Handler;
import android.os.Message;

public abstract class AuthorityHandler extends Handler {
	
	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		
		AuthorityResult ar = null;
		if(msg != null && msg.obj != null && msg.obj instanceof AuthorityResult){
			ar = (AuthorityResult)msg.obj;
		}		
		onChecked(ar);		
	}
	
	public abstract void onChecked(AuthorityResult ar);	
}
