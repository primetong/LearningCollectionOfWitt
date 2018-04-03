package com.evguard.model;

import com.evguard.model.WebRes_Base;

public interface ICommonWebResponse<T extends WebRes_Base>   {

	
	public void WebRequestException(String ex);
	public void WebRequsetFail(String sfalied);
	public  void WebRequstSucess(T aWebRes);
	
}
