package com.evguard.bmap;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.evguard.model.Position_Base;
import com.evguard.tools.CommUtils;
import com.xinghaicom.evguard.R;

public abstract class MapFragment_Base extends Fragment {
	private String TAG="MapFragment_Base";
	protected Context mContext=null;
	protected View layoutView;
	private LinearLayout ll_map;
	protected MapView bmapView=null;
	protected BaiduMap mBaiduMap=null;
	protected MapStatus initMapStatus=null;
	private OnMarkerClickListener mOnMarkerClickListener=null;
	protected OnMapEventListener mOnMapEventListener=null;
	protected int iLineWith=5;
	protected Toast mToast=null;
	public MapFragment_Base(){
		super();

	}
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
	{
		Log.i(TAG, "=============createview");
		layoutView=inflater.inflate(R.layout.fragment_map, container,false);
		mContext=getActivity();
		initMap();
		findView();
		ll_map.addView(bmapView);

		return layoutView;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		 Log.i(TAG, "=============onActivityCreated");
		super.onActivityCreated(savedInstanceState);
		mBaiduMap=bmapView.getMap();
		mBaiduMap.setOnMapLoadedCallback(new OnMapLoadedCallback() {
			@Override
			public void onMapLoaded() {
				if(mOnMapEventListener==null)return;
				mOnMapEventListener.onMapLoaded();

			}
			
		});
		addMarkerListener();
	}
	@Override
	public void onPause() {
		 Log.i(TAG, "=============onPause");
		super.onPause();
		// activity 暂停时同时暂停地图控件
		bmapView.onPause();
	}

	@Override
	public void onResume() {
		 Log.i(TAG, "=============onResume");
		super.onResume();
		// activity 恢复时同时恢复地图控件
		bmapView.onResume();
		
	}
	@Override
	public void onDestroy() {
		Log.i(TAG, "=============onDestroy");
		super.onDestroy();
		// activity 销毁时同时销毁地图控件
		((ViewGroup) bmapView.getParent()).removeView(layoutView);
		((ViewGroup)layoutView).removeAllViews();
		mBaiduMap.removeMarkerClickListener(mOnMarkerClickListener);
		bmapView.onDestroy();
		
		layoutView=null;

	}
	protected void showToast(String txt){
		if(mToast==null)mToast=Toast.makeText(mContext, txt, Toast.LENGTH_LONG);
		mToast.setText(txt);
		mToast.show();
	}
	private void findView(){
		ll_map=(LinearLayout)layoutView.findViewById(R.id.ll_map);
	}
	
	private void initMap(){
		initMapStatus = new MapStatus.Builder().overlook(0).zoom(15).build();
		BaiduMapOptions bo = new BaiduMapOptions().mapStatus(initMapStatus)
		.compassEnabled(false).zoomControlsEnabled(false).scaleControlEnabled(false);
		bmapView=new MapView(mContext, bo);
		
	}
	private void addMarkerListener(){
		mOnMarkerClickListener=new OnMarkerClickListener(){

			@Override
			public boolean onMarkerClick(Marker arg0) {
				onMapMarkerClick(arg0);
				return false;
			}
		};
		mBaiduMap.setOnMarkerClickListener(mOnMarkerClickListener);
	}
	
	public void zoomIn(){
		 MapStatusUpdate aMapStatusUpdate=MapStatusUpdateFactory.zoomIn();
		 mBaiduMap.animateMapStatus(aMapStatusUpdate);
	}
	public void zoomOut(){

		 MapStatusUpdate aMapStatusUpdate=MapStatusUpdateFactory.zoomOut();
		 mBaiduMap.animateMapStatus(aMapStatusUpdate);
	}
	public void setCenter(LatLng apoint){
		MapStatusUpdate aMapStatusUpdate=MapStatusUpdateFactory.newLatLng(apoint);
		 mBaiduMap.animateMapStatus(aMapStatusUpdate);
	}
	public void setCenter(LatLngBounds abounds){
		MapStatusUpdate aMapStatusUpdate=MapStatusUpdateFactory.newLatLngBounds(abounds,(int)CommUtils.screen_width,(int)CommUtils.screen_height);
		mBaiduMap.animateMapStatus(aMapStatusUpdate,100);
	}
	
	
	
	protected abstract void onMapMarkerClick(Marker marker);
	
	public void setOnMapEventListener(OnMapEventListener aOnMapEventListener){
		this.mOnMapEventListener=aOnMapEventListener;
	}
	
	public interface OnMapEventListener{
		public void onMapLoaded();
		public void onMarkerClick(Position_Base aPosition_Base);
	}

}
