package com.evguard.bmap;

import java.util.List;

import android.graphics.Point;
import android.location.Geocoder;

import com.baidu.mapapi.map.Projection;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.model.LatLngBounds.Builder;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionResult.SuggestionInfo;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.evguard.tools.CommUtils;

public class BaiduMapUtils {

	

	public static LatLng ConvetTobdll09(LatLng sourceLatLng){
		CoordinateConverter converter  = new CoordinateConverter();  
		converter.from(CoordType.GPS);  
		converter.coord(sourceLatLng);  
		LatLng desLatLng = converter.convert();
		return desLatLng;
	}
	public static float getBaiduRoate(float directtion){
		float f=360-directtion;
		return f;
	}
	
	public static boolean IsOut(Projection aProjection,LatLng apoint){
		Point p=aProjection.toScreenLocation(apoint);
		if((p.x<=0|| p.x>=CommUtils.screen_width) ||
				(p.y<=0 ||p.y>=CommUtils.screen_height-50)){
			return true;
		}
		return false;
	}
	
}
