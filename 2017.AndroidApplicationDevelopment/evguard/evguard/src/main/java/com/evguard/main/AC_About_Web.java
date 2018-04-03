package com.evguard.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.evguard.customview.AppTitleBar;
import com.evguard.customview.AppTitleBar.OnTitleActionClickListener;
import com.xinghaicom.evguard.R;

public class AC_About_Web extends AC_Base {

	private String aboutURL;
	private WebView mWebView;
	private AppTitleBar mTitleBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_about_web);
		aboutURL = mSettings.getAboutUrl();
		initialView();
	}
	
	protected void initialView(){
		mTitleBar = (AppTitleBar) findViewById(R.id.title_bar);
		mTitleBar.setTitleMode(null,
				mContext.getResources().getDrawable(R.drawable.icon_back),
				"关于EV卫士",false,
				null,null);
		mTitleBar
				.setOnTitleActionClickListener(new OnTitleActionClickListener() {

					@Override
					public void onLeftOperateClick() {
						AC_About_Web.this.finish();
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
		mWebView.loadUrl(aboutURL);
		mWebView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
//		StatService.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
//		StatService.onPause(this);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==0&&resultCode==0){
//			setResult(1);
			this.finish();
		}
	}

	@Override
	public void handleMsg(Message msg) {
		// TODO Auto-generated method stub
		
	}
}
