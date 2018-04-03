package com.evguard.bmap;

import java.util.ArrayList;
import java.util.List;

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
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.evguard.data.AppDataCache;
import com.evguard.model.CarCurPositionInfo;
import com.xinghaicom.evguard.R;

public class Fragment_RealMap extends MapFragment_Base {
	
	private BitmapDescriptor bdCar=null;
	private BitmapDescriptor bdTrackCar=null;
	private OverlayOptions ooA=null;
	private Marker mMarkerCar=null;
	private Marker mMarkerTrack=null;
	private List<LatLng> mTrackPoints=new ArrayList<LatLng>();
	
	
	public static Fragment_RealMap newInstance()
	{
		return new Fragment_RealMap();
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		bdCar=BitmapDescriptorFactory.fromResource(R.drawable.icon_carposition);
		bdTrackCar=BitmapDescriptorFactory.fromResource(R.drawable.icon_direction);
		
	}
	@Override
	public void onDestroy(){
		super.onDestroy();
		bdCar.recycle();
	}
	public void clearAll(){
		System.out.println("clearAll");
		mBaiduMap.clear();
		if(mMarkerTrack!=null){
			mMarkerTrack.remove();
			mMarkerTrack=null;
		}
		if(mMarkerCar!=null){
			mMarkerCar.remove();
			mMarkerCar=null;
		}
		if(mTrackPoints!=null)mTrackPoints.clear();
	}
	public void drawCar(CarCurPositionInfo aCarCurPositionInfo){
		if(aCarCurPositionInfo==null)return;
		Bundle carinfo=new Bundle();
		LatLng aLatLng=new LatLng(aCarCurPositionInfo.getLatitude(),aCarCurPositionInfo.getLongitude());
		LatLng bdLatlng=BaiduMapUtils.ConvetTobdll09(aLatLng);
		carinfo.putParcelable("car", aCarCurPositionInfo);
		
		if(mMarkerCar==null){
			 ooA = new MarkerOptions().position(bdLatlng).icon(bdCar).anchor(0.5f, 0.5f)
					 .rotate(BaiduMapUtils.getBaiduRoate(aCarCurPositionInfo.getDirection())).zIndex(9).draggable(false).extraInfo(carinfo);
			
			mMarkerCar = (Marker) (mBaiduMap.addOverlay(ooA));
		}
		else{
			float roate=BaiduMapUtils.getBaiduRoate(aCarCurPositionInfo.getDirection());
			mMarkerCar.setRotate(roate);
			mMarkerCar.setPosition(bdLatlng);
			mMarkerCar.setExtraInfo(carinfo);
		}
		MapStatusUpdate aMapStatusUpdate=MapStatusUpdateFactory.newLatLng(bdLatlng);
		mBaiduMap.animateMapStatus(aMapStatusUpdate,700);
	}
	public void trackCar(CarCurPositionInfo aCarCurPositionInfo){
		if(mMarkerCar!=null){
			mMarkerCar.remove();
			mMarkerCar=null;
		}
		if(aCarCurPositionInfo==null)return;
		Bundle carinfo=new Bundle();
		carinfo.putParcelable("car", aCarCurPositionInfo);
		LatLng gpspoint=new LatLng(aCarCurPositionInfo.getLatitude(),aCarCurPositionInfo.getLongitude());
		LatLng bdpoint=BaiduMapUtils.ConvetTobdll09(gpspoint);
		if(mMarkerTrack==null){
			OverlayOptions	 ooB = new MarkerOptions().position(bdpoint).icon(bdTrackCar).anchor(0.5f, 0.5f)
					.rotate(BaiduMapUtils.getBaiduRoate(aCarCurPositionInfo.getDirection())).zIndex(9).draggable(false).extraInfo(carinfo);
			 mMarkerTrack = (Marker) (mBaiduMap.addOverlay(ooB));
			LatLngBounds abound=new LatLngBounds.Builder().include(bdpoint).build();
			MapStatusUpdate aMapStatusUpdate=MapStatusUpdateFactory.newLatLngBounds(abound);
			mBaiduMap.animateMapStatus(aMapStatusUpdate,100);
		}else{
			float roate=BaiduMapUtils.getBaiduRoate(aCarCurPositionInfo.getDirection());
			mMarkerTrack.setRotate(roate);
			mMarkerTrack.setExtraInfo(carinfo);
			mMarkerTrack.setPosition(bdpoint);
		}
		mTrackPoints.add(bdpoint);
		if(mTrackPoints.size()<=1)return;

		OverlayOptions ooPolyline = new PolylineOptions().width(iLineWith)
		.color(mContext.getResources().getColor(R.color.line)).points(mTrackPoints);
		mBaiduMap.addOverlay(ooPolyline);
		mTrackPoints.clear();
		mTrackPoints.add(bdpoint);
		setCenter(bdpoint);
	}
	@Override
	protected void onMapMarkerClick(Marker marker) {
		Bundle carinfo=marker.getExtraInfo();
		CarCurPositionInfo aCarDetailInfo=carinfo.getParcelable("car");
		if(mOnMapEventListener!=null)
			mOnMapEventListener.onMarkerClick(aCarDetailInfo);
		
	}
	
	public boolean isMaxZoom(){
	  return mBaiduMap.getMapStatus().zoom >= mBaiduMap.getMaxZoomLevel();
	}
	
	public boolean isMinZoom(){
		  return mBaiduMap.getMapStatus().zoom <= mBaiduMap.getMinZoomLevel();
	}
		
	
}

