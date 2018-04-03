package com.evguard.main;

import android.os.Bundle;
import android.os.Message;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.evguard.customview.AppTitleBar;
import com.evguard.customview.AppTitleBar.OnTitleActionClickListener;
import com.evguard.tools.LogEx;
import com.xinghaicom.evguard.R;

/**
 * 车辆违章查询
 * 
 */

public class AC_IllegalQuery extends AC_Base {

	private WebView mWebView;
	private AppTitleBar mTitleBar;
	private String illegalURL;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_illegal_query);
		illegalURL = mSettings.getCarIllegalUrl();
		
		initViews();
	}

	private void initViews() {
		mTitleBar = (AppTitleBar) findViewById(R.id.title_bar);
//		mTitleBar.setTitleMode(AppTitleBar.APPTITLEBARMODE_TXTANDBACK, "违章查询",
//				null);
		mTitleBar.setTitleMode(null,
				mContext.getResources().getDrawable(R.drawable.icon_back),
				"违章查询",false,
				null,null);
		mTitleBar
				.setOnTitleActionClickListener(new OnTitleActionClickListener() {

					@Override
					public void onLeftOperateClick() {
						AC_IllegalQuery.this.finish();
					}

					@Override
					public void onRightOperateClick() {

					}

					@Override
					public void onTitleClick() {
						// TODO Auto-generated method stub
						
					}

				});
		
		mWebView = (WebView) findViewById(R.id.webview);
		if(illegalURL.equals("")){
			illegalURL = "about:blank";
		}
		mWebView.loadUrl(illegalURL);
		mWebView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
	}


	@Override
	protected void handleMsg(Message msg) {

	}

}
