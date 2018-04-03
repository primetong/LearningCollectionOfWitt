package com.xinghaicom.security;

public class AuthorityResult {
	
	protected boolean mAuthorization = false;
	protected String mResultMessage = null;
	
	public AuthorityResult(boolean authorization,String resultMeassage){
		mAuthorization = authorization;
		mResultMessage = resultMeassage;
	}
	
	public boolean hasAuthorization(){
		return mAuthorization;
	}
	
	public String getResultMessage(){
		return mResultMessage;
	}		
}
