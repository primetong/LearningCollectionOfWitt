package com.evguard.main;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.evguard.model.MenuItem;
import com.evguard.model.WeatherQuery;
import com.xinghaicom.evguard.R;

public class Fragment_Menu_Left extends Fragment_Base implements
		OnClickListener {

	protected static final int WEATHER_GET_SUCCESS = 0;
	protected static final int WEATHER_GET_FAIL = 1;
	private static Fragment_Menu_Left mInstance;
	private static Object lockobj = new Object();

	private TextView tvName = null;
	private TextView tv_city = null;
	private ListView lvMenu;
	private List<MenuItem> menuList;

	public static Fragment_Menu_Left getInstance() {
		if (mInstance == null)
			synchronized (lockobj) {
				mInstance = new Fragment_Menu_Left();
			}
		return mInstance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (layoutView != null)
			return layoutView;
		layoutView = inflater.inflate(R.layout.fragment_menu_left, container,
				false);
		return layoutView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		findView();
		getWeather();
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (getUserVisibleHint()) {
		} else {
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mInstance = null;
	}

	private void findView() {
		tvName = (TextView) layoutView.findViewById(R.id.tv_name);
		lvMenu = (ListView) layoutView.findViewById(R.id.lv_left_menu);
		tv_weather = (TextView) layoutView.findViewById(R.id.tv_weather);
		tv_temp = (TextView) layoutView.findViewById(R.id.tv_temp);
		tv_city = (TextView) layoutView.findViewById(R.id.tv_city);
		ivWeather = (ImageView) layoutView.findViewById(R.id.iv_weather);
		menuList = new ArrayList<MenuItem>();
		menuList.add(new MenuItem("违章查询", R.drawable.icon_wz_query));
		menuList.add(new MenuItem("服务电话", R.drawable.icon_service_tel));
		menuList.add(new MenuItem("报警设置", R.drawable.icon_setting));
		menuList.add(new MenuItem("密码修改", R.drawable.icon_pwd_reset));
		menuList.add(new MenuItem("关于EV卫士", R.drawable.icon_about));
		menuList.add(new MenuItem("退出", R.drawable.icon_exit));
		// lvMenu.setAdapter(new LeftMenuAdapter());
		tvName.setText(mSettings.getUserName());
		lvMenu.setAdapter(new CommonAdapter<MenuItem>(mContext, menuList,
				R.layout.lv_left_menu) {

			@Override
			public void convert(CommonViewHolder aCommonViewHolder,
					MenuItem item) {
				aCommonViewHolder.setText(R.id.tv_title, item.getText());
				aCommonViewHolder.setImageResource(R.id.menu_icon,
						item.getResource());
			}
		});
		lvMenu.setOnItemClickListener(new OnItemClickListener() {

			private Intent intent;

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					intent = new Intent(mContext, AC_IllegalQuery.class);
					mContext.startActivity(intent);
					break;
				case 1:
					intent = new Intent(mContext, AC_ServiceTel.class);
					mContext.startActivity(intent);
					break;
				case 2:
					intent = new Intent(mContext, AC_AlamSetting.class);
					mContext.startActivity(intent);
					break;
				case 3:
					intent = new Intent(mContext, AC_PasswordReset.class);
					mContext.startActivity(intent);
					break;
				case 4:
					intent = new Intent(mContext, AC_About.class);
					mContext.startActivity(intent);
					break;
				case 5:
					AC_Main activity = (AC_Main) getActivity();
					activity.confirExitApp(true);
					break;
				default:
					break;
				}
			}

		});

	}

	private double latitude = 0.0;
	private double longitude = 0.0;

	private void getWeather() {


		String address = getAddress(mContext);
		String addr = "";
		if (address.equals("") || address == null) {
			addr = "福州";
		} else {
			addr = address;
		}

		System.out.println("addr--" + addr);
		final String city = addr;
		new Thread() {
			public void run() {
				String httpUrl = "http://apis.baidu.com/apistore/weatherservice/weather";
				String httpArg = "";
				try {

					httpArg = "cityname=" + URLEncoder.encode(city, "UTF-8");
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String jsonResult = WeatherQuery.request(httpUrl, httpArg);
				try {
					if (jsonResult != null) {
						JSONObject jsonObject = new JSONObject(jsonResult);
						if (jsonObject.getString("errMsg").equals("success")
								&& jsonObject.getInt("errNum") == 0) {
							JSONObject json_weather = jsonObject
									.getJSONObject("retData");
							Log.i("llj", "json_weather--" + json_weather);
							int temp = json_weather.getInt("temp");
							String weather = json_weather.getString("weather");
							String city = json_weather.getString("city");
							Bundle bundle = new Bundle();
							bundle.putString("weather", weather);
							bundle.putString("city", city);
							bundle.putInt("temp", temp);
							Message msg = mHandler
									.obtainMessage(WEATHER_GET_SUCCESS);
							msg.setData(bundle);
							mHandler.sendMessage(msg);
						}
					} else {
						Message msg = mHandler.obtainMessage(WEATHER_GET_FAIL);
						mHandler.sendMessage(msg);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();
	}


	public void setCallBack_FragmentMenuLeft(
			CallBack_FragmentMenuLeft aCallBack_FragmentKidActoin) {
	}

	public interface CallBack_FragmentMenuLeft {

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	private Handler mHandler = new Handler() {

		@Override
		public void dispatchMessage(Message msg) {
			Bundle bundle = msg.getData();
			String weather = bundle.getString("weather");
			String city = bundle.getString("city");
			int temp = bundle.getInt("temp");
			tv_weather.setText(weather);
			tv_temp.setText("" + temp);
			tv_city.setText(city);
			if (weather.contains("晴")) {
				ivWeather.setImageResource(R.drawable.icon_weather_1);
			} else if (weather.contains("云")) {
				ivWeather.setImageResource(R.drawable.icon_weather_2);
			} else if (weather.contains("雨")) {
				ivWeather.setImageResource(R.drawable.icon_weather_3);
			}
		}

	};
	private TextView tv_weather;
	private TextView tv_temp;
	private ImageView ivWeather;

	private String doWork(Context context) {
		String addres = "";
		Criteria criteria = new Criteria();
		// 获得最好的定位效果
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(false);
		// 使用省电模式
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		String provider = locationManager.getBestProvider(criteria, true);
		// 获得当前位置 location为空是一直取 从onLocationChanged里面取
		if(locationManager.getLastKnownLocation(provider) == null){
			Log.i("addr", "addr--无地址");
			return ""; 
		} 
		if (location == null) {
			location = locationManager.getLastKnownLocation(provider);
		}
		// locationListener
		LocationListener locationListener = new LocationListener() {

			public void onLocationChanged(Location location) {
				Fragment_Menu_Left.this.location = location;
			}

			@Override
			public void onProviderDisabled(String provider) {

			}

			@Override
			public void onProviderEnabled(String provider) {

			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {

			}

		};
		locationManager.requestLocationUpdates(provider, 1000, 10,
				locationListener);

		Geocoder geo = new Geocoder(context, Locale.getDefault());
		try {
			List<Address> address = geo.getFromLocation(location.getLatitude(),
					location.getLongitude(), 1);
			if (address.size() > 0) {
				Address adsLocation = address.get(0);
				addres = adsLocation.getLocality().substring(0,
						adsLocation.getLocality().length() - 1);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("addr", "addr--" + addres);
		return addres;
	}

	private Location location;
	private LocationManager locationManager;

	public static LocationManager getLocationManager(Context context) {
		return (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
	}

	// 获取位置信息
	public String getAddress(Context context) {
		
		PackageManager pkm = getActivity().getPackageManager();
		boolean has_permission = (PackageManager.PERMISSION_GRANTED == pkm.checkPermission("android.permission.ACCESS_COARSE_LOCATION", "com.xinghaicom.evguard"));
		if (has_permission) {
		// 这里才开始真的干活的
			Log.i("1001", "1001--here111");
			locationManager = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);
			// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
			boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
			boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			if (gps || network) {
				return doWork(context);
			}
		}else {
		// showToast("没有权限");
			Log.i("1001", "1001--here没有权限");
			return "";
		//		Toast.makeText(getActivity(), "没有设置权限", Toast.LENGTH_SHORT).show();
		}
		
//
//		Log.i("1001", "1001--here");
		return "";
		
	}

	@Override
	protected void restoreState(Bundle saveState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Bundle saveState(Bundle saveState) {
		// TODO Auto-generated method stub
		return saveState;
	}
}
