package com.evguard.bmap;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.util.Log;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.evguard.model.TrackInfo;
import com.xinghaicom.evguard.R;

public class Fragment_TrackMap extends MapFragment_Base {

	private BitmapDescriptor bdTrackCar=null;
	private BitmapDescriptor bdStartCar=null;
	private BitmapDescriptor bdEndCar=null;
	private List<LatLng> points=new ArrayList<LatLng>();
	private List<LatLng> mAllPoints=new ArrayList<LatLng>();
	private LatLngBounds mLatLngBounds=null;
	private List<TrackInfo>  mTrackList=new ArrayList<TrackInfo>();
	private Marker mMarkerTrack=null;
	private Marker mMarkerStart=null;
	private Marker mMarkerEnd=null;
	private boolean mIsShowAll=true;
	private boolean mIsPlayOver=false;
	
	
	public static Fragment_TrackMap newInstance()
	{
		return new Fragment_TrackMap();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		bdTrackCar=BitmapDescriptorFactory.fromResource(R.drawable.icon_carposition);
		bdStartCar=BitmapDescriptorFactory.fromResource(R.drawable.icon_start);
		bdEndCar=BitmapDescriptorFactory.fromResource(R.drawable.icon_end);
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		bdTrackCar.recycle();
		bdStartCar.recycle();
		bdEndCar.recycle();
	}
	@Override
	protected void onMapMarkerClick(Marker marker) {
		Bundle carinfo=marker.getExtraInfo();
		TrackInfo aVehicleTrackInfo=carinfo.getParcelable("car");
		if(mOnMapEventListener!=null)
			mOnMapEventListener.onMarkerClick(aVehicleTrackInfo);
		
	}
	private void clearTrackingCar(){
		mBaiduMap.clear();
		if(mMarkerTrack!=null){
			mMarkerTrack.remove();
			mMarkerTrack=null;
		}
		if(mMarkerStart!=null){
			mMarkerStart.remove();
			mMarkerStart=null;
		}
		if(mMarkerEnd!=null){
			mMarkerEnd.remove();
			mMarkerEnd=null;
		}
		
	}
	private void drawStartCar(TrackInfo aTrackInfo){
		Bundle carinfo=new Bundle();
		carinfo.putParcelable("car", aTrackInfo);
		LatLng aLatLng=new LatLng(aTrackInfo.getLatitude(),aTrackInfo.getLongitude());
		LatLng bdLatlng=BaiduMapUtils.ConvetTobdll09(aLatLng);
		OverlayOptions ooA = new MarkerOptions().position(bdLatlng).icon(bdStartCar)
			.zIndex(9).draggable(false).extraInfo(carinfo);
		mMarkerStart = (Marker) (mBaiduMap.addOverlay(ooA));
	}
	private void drawEndCar(TrackInfo aTrackInfo){
		Bundle carinfo=new Bundle();
		carinfo.putParcelable("car", aTrackInfo);
		LatLng aLatLng=new LatLng(aTrackInfo.getLatitude(),aTrackInfo.getLongitude());
		LatLng bdLatlng=BaiduMapUtils.ConvetTobdll09(aLatLng);
		
		OverlayOptions ooA = new MarkerOptions().position(bdLatlng).icon(bdEndCar)
			.zIndex(9).draggable(false).extraInfo(carinfo);
		mMarkerEnd = (Marker) (mBaiduMap.addOverlay(ooA));
	}
	private void drawCar(TrackInfo aTrackInfo){
		Bundle carinfo=new Bundle();
		carinfo.putParcelable("car", aTrackInfo);
		LatLng aLatLng=new LatLng(aTrackInfo.getLatitude(),aTrackInfo.getLongitude());
		LatLng bdLatlng=BaiduMapUtils.ConvetTobdll09(aLatLng);
		if(mMarkerTrack==null){
			OverlayOptions ooA = new MarkerOptions().position(bdLatlng).icon(bdTrackCar).anchor(0.5f,0.5f)
				.rotate(BaiduMapUtils.getBaiduRoate(aTrackInfo.getDirect())).zIndex(9).draggable(false).extraInfo(carinfo);
			
			 mMarkerTrack = (Marker) (mBaiduMap.addOverlay(ooA));
			LatLngBounds abound=new LatLngBounds.Builder().include(bdLatlng).build();
			MapStatusUpdate aMapStatusUpdate=MapStatusUpdateFactory.newLatLngBounds(abound);
			mBaiduMap.animateMapStatus(aMapStatusUpdate,100);
		}
		else{
			float roate=BaiduMapUtils.getBaiduRoate(aTrackInfo.getDirect());
			mMarkerTrack.setRotate(roate);
			mMarkerTrack.setPosition(bdLatlng);
			mMarkerTrack.setExtraInfo(carinfo);
		}
		
	}
	public void setTrackList(List<TrackInfo> alist){
		mAllPoints.clear();
		this.mTrackList=alist;
	}
	public void showTrackCar(int iIndex){

		if(mIsShowAll){
			mIsShowAll=false;
			clearTrackingCar();
			points.clear();
		}
		if(mIsPlayOver){
			clearTrackingCar();
			points.clear();
			mIsPlayOver=false;
		}
		if(mTrackList==null||mTrackList.size()<=0)return;
		TrackInfo aTrackInfo=mTrackList.get(iIndex);
		if(iIndex==0){
			drawStartCar(aTrackInfo);
		}else if(iIndex==mTrackList.size()-1){
			drawEndCar(aTrackInfo);
		}
		LatLng apoint=new LatLng(aTrackInfo.getLatitude(),aTrackInfo.getLongitude());
		LatLng bdpoint=BaiduMapUtils.ConvetTobdll09(apoint);
		points.add(bdpoint);
		if(points.size()<=1){
			return;
		}
		OverlayOptions ooPolyline = new PolylineOptions().width(iLineWith)
		.color(mContext.getResources().getColor(R.color.line)).points(points);
		mBaiduMap.addOverlay(ooPolyline);
		drawCar(aTrackInfo);
		if(BaiduMapUtils.IsOut(mBaiduMap.getProjection(), bdpoint)){
			setCenter(bdpoint);
		}
		points.clear();
		if(iIndex>=mTrackList.size()-1){
			mIsPlayOver=true;
			return;
		}
		mIsPlayOver=false;
		points.add(bdpoint);
		setCenter(bdpoint);
		
		
	}
	public void showAllTrack(){
		try {
			mIsShowAll=true;
			clearTrackingCar();
			if(mTrackList==null||mTrackList.size()<=0)return;
			if(mLatLngBounds==null){
				LatLngBounds.Builder builder=new LatLngBounds.Builder();
				for(TrackInfo aVehicleTrackInfo:mTrackList){
					LatLng apoint=new LatLng(aVehicleTrackInfo.getLatitude(),aVehicleTrackInfo.getLongitude());
					LatLng bdpoint=BaiduMapUtils.ConvetTobdll09(apoint);
					
					mAllPoints.add(bdpoint);
					builder.include(bdpoint);
				}
				mLatLngBounds=builder.build();
			}
			OverlayOptions ooPolyline = new PolylineOptions().width(iLineWith)
			.color(mContext.getResources().getColor(R.color.line)).points(mAllPoints);
			mBaiduMap.addOverlay(ooPolyline);
			drawStartCar(mTrackList.get(0));
			drawEndCar(mTrackList.get(mTrackList.size()-1));
			setCenter(mLatLngBounds);
//			MapStatusUpdate aMapStatusUpdate=MapStatusUpdateFactory.newLatLngBounds(mLatLngBounds,(int)CommUtils.screen_width,(int)CommUtils.screen_height);
//			mBaiduMap.animateMapStatus(aMapStatusUpdate,100);
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
