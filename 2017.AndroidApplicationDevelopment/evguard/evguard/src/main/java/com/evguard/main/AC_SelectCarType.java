package com.evguard.main;

import java.util.ArrayList;
import java.util.List;

import com.evguard.customview.AppTitleBar;
import com.evguard.customview.AppTitleBar.OnTitleActionClickListener;
import com.evguard.main.Thread_GetServerInfo.CallBack_GetServerInfo;
import com.evguard.model.ServerInfo;
import com.evguard.tools.ImageDownloader;
import com.evguard.tools.LogEx;
import com.evguard.tools.ImageDownloader.OnImageDownload;
import com.xinghaicom.evguard.R;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AC_SelectCarType extends AC_Base {

	private AppTitleBar mTitleBar;
	private ListView lvCarTypes=null;
	private ProgressBar mBar=null;
	private TextView tv_error=null;
	
	private List<ServerInfo> mServers=new ArrayList<ServerInfo>();
	private CarTypeAdapter mCarTypeAdapter=null;
	private ImageDownloader mImageDownloader;  
	
	private ServerInfo mCurServerInfo=null;
	
	
	private static final int MESSAGE_GETSERVERING=2;
	private static final int MESSAGE_GETSERVEROK=3;
	private static final int MESSAGE_GETSERVERFAILED=4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_selectcartype);
		findViews();
		initListView();
		handleListener();
		getServers();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			Intent tLogin=new Intent("com.evguard.activity.login");
			startActivity(tLogin);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	private void findViews() {
		tv_error=(TextView)findViewById(R.id.tv_error);
		mBar=(ProgressBar)findViewById(R.id.pb_waiting);
		lvCarTypes=(ListView)findViewById(R.id.lv_cartype);
		mTitleBar = (AppTitleBar) findViewById(R.id.title_bar);
	}
	private void initListView(){
		mCarTypeAdapter=new CarTypeAdapter();
		lvCarTypes.setAdapter(mCarTypeAdapter);
		lvCarTypes.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				 ListView listView = (ListView)parent;  
				 ServerInfo ainfo = (ServerInfo) listView.getItemAtPosition(position);  
				 mCurServerInfo=ainfo;
				mCarTypeAdapter.setSelectedPosition(position);  
				mCarTypeAdapter.notifyDataSetInvalidated(); 
			}
			
		});
	}
	private void handleListener(){
		mTitleBar.setTitleMode("跳过",
				null,
				"车型选择",false,
				"下一步",null);
		mTitleBar
				.setOnTitleActionClickListener(new OnTitleActionClickListener() {
					@Override
					public void onLeftOperateClick() {
						Intent tLogin=new Intent("com.evguard.activity.login");
						startActivity(tLogin);
					}

					@Override
					public void onRightOperateClick() {
						mSettings.setAgentName(mCurServerInfo.getAgentname());
						mSettings.setServerIP(mCurServerInfo.getServerUrl());
						Intent tLogin=new Intent("com.evguard.activity.login");
						startActivity(tLogin);
					}

					@Override
					public void onTitleClick() {
					}

				});
	}
	
	private void getServers(){
		
		mHandler.sendEmptyMessage(MESSAGE_GETSERVERING);
		Thread_GetServerInfo mThread_GetServerInfo=new Thread_GetServerInfo(new CallBack_GetServerInfo(){

			@Override
			public void getServerInfosOk(List<ServerInfo> alist) {
				mServers.clear();
				mServers.addAll(alist);
				mHandler.sendEmptyMessage(MESSAGE_GETSERVEROK);
			}

			@Override
			public void getServerInfosFailed(String serror) {
				Message msg=mHandler.obtainMessage();
				msg.what=MESSAGE_GETSERVERFAILED;
				msg.obj=serror;
				mHandler.sendMessage(msg);
			}
			
		});
		mThread_GetServerInfo.getServerInfo();
	}
	@Override
	protected void handleMsg(Message msg) {
		switch(msg.what){

			case MESSAGE_GETSERVERING:
				mBar.setVisibility(View.VISIBLE);
				tv_error.setVisibility(View.GONE);
				lvCarTypes.setVisibility(View.GONE);
				break;
			case MESSAGE_GETSERVEROK:
				mCarTypeAdapter.notifyDataSetChanged();
				mBar.setVisibility(View.GONE);
				tv_error.setVisibility(View.GONE);
				lvCarTypes.setVisibility(View.VISIBLE);
				break;
			case MESSAGE_GETSERVERFAILED:
				mBar.setVisibility(View.GONE);
				tv_error.setVisibility(View.VISIBLE);
				lvCarTypes.setVisibility(View.GONE);
				tv_error.setText((String) msg.obj);
				break;
		}

	}

	class CarTypeAdapter extends BaseAdapter{

		private int selectedposition=-1;
		
		public void setSelectedPosition(int i){
			this.selectedposition=i;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mServers.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mServers.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ServerInfo ainfo=(ServerInfo) getItem(position);
			CarTypeViewHolder aCarTypeViewHolder=null;
			if(convertView==null){
				convertView=View.inflate(mContext, R.layout.lv_item_cartype, null);
				aCarTypeViewHolder=new CarTypeViewHolder(convertView);
				convertView.setTag(aCarTypeViewHolder);
			}else{
				aCarTypeViewHolder=(CarTypeViewHolder) convertView.getTag();
			}
			
			aCarTypeViewHolder.icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_launcher));
			aCarTypeViewHolder.icon.setTag(ainfo.getIconUrl());
			aCarTypeViewHolder.agentname.setText(ainfo.getAgentname());
			if(selectedposition==position){
				aCarTypeViewHolder.llparent.setBackgroundColor(mContext.getResources().getColor(R.color.apptheme_yellow));
			}
			else{
				aCarTypeViewHolder.llparent.setBackgroundColor(Color.TRANSPARENT);
			}
			if (mImageDownloader == null) {  
				 mImageDownloader = new ImageDownloader();  
	           }  
			if (mImageDownloader != null) {  
                //异步下载图片  
				mImageDownloader.imageDownload(ainfo.getIconUrl(), aCarTypeViewHolder.icon, "/caricon",AC_SelectCarType.this, new OnImageDownload() {  
                            @Override  
                            public void onDownloadSucc(Bitmap bitmap,  
                                    String c_url,ImageView mimageView) {  
                                ImageView imageView = (ImageView) lvCarTypes.findViewWithTag(c_url);  
                                if (imageView != null) {  
                                    imageView.setImageBitmap(bitmap);  
                                    imageView.setTag("");  
                                }   
                            }  
                        });  
            }  
			return convertView;
		}
		
	}
	class CarTypeViewHolder{
		public LinearLayout llparent=null;
		public ImageView icon=null;
		public TextView agentname=null;
		public CarTypeViewHolder(View contentView){
			llparent= (LinearLayout) contentView.findViewById(R.id.ll_cartypeitem);
			icon=(ImageView) contentView.findViewById(R.id.img_icon);
			agentname=(TextView) contentView.findViewById(R.id.tv_agentname);
		}
	}
}
