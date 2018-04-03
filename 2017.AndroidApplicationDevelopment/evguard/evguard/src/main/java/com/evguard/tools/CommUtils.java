package com.evguard.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.NameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml.Encoding;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import cn.jpush.android.data.s;

import com.evguard.main.App_Application;
import com.evguard.model.AddressInfo;
import com.evguard.model.AddressPoint;
import com.evguard.model.ElectricDetail;
import com.evguard.model.ICommonWebResponse;
import com.evguard.model.WebReq_GetLocation;
import com.evguard.model.WebRes_GetLocation;

public class CommUtils {

	private static Toast mToast;
	private static float mDensity = DisplayMetrics.DENSITY_DEFAULT;
	private static int mDensityDPI=DisplayMetrics.DENSITY_MEDIUM;
	public static float screen_width;
	public static float screen_height;
	private static final int DAY_MILL = 24 * 60 * 60 * 1000;
	private static final int WEEK = 7 * 24 * 60 * 60 * 1000;
	private static SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static boolean value = false;
	private static Thread checkThread;
	
	public static boolean isEmpty(String s) {
        if (null == s)
            return true;
        if (s.length() == 0)
            return true;
        if (s.trim().length() == 0)
            return true;
        return false;
    }
	
	public static int getDensityDPI() {
		
		return mDensityDPI;
	}
	public static float getDensity() {
		
		return mDensity;
	}
	public static int dip2px(Context context, float dipValue) {
		mDensity = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (dipValue * mDensity + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		return (int) (pxValue / mDensity + 0.5f);
	}
	
	public static int px2sp(Context context, float pxValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (pxValue / fontScale + 0.5f);  
    }
	/**
	 * 获取屏幕宽高
	 * @param act
	 */
	public static void getScreenMetrics(Activity act){
		DisplayMetrics dm = new DisplayMetrics();
		act.getWindowManager().getDefaultDisplay().getMetrics(dm);
		screen_width = dm.widthPixels;
		screen_height = dm.heightPixels;
		mDensity=dm.density;
		mDensityDPI=dm.densityDpi;
	}
	@SuppressLint("NewApi")
	public static void hidenSystem(Activity act,EditText ed){
		InputMethodManager imm = (InputMethodManager)act.getSystemService(Context.INPUT_METHOD_SERVICE); 
		imm.hideSoftInputFromWindow(ed.getWindowToken(), 0); 
		act.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
	        int currentVersion = android.os.Build.VERSION.SDK_INT;
	        String methodName = null;
	        if(currentVersion >= 16){
	            // 4.2
	            methodName = "setShowSoftInputOnFocus";
	        }
	        else if(currentVersion >= 14){
	            // 4.0
	            methodName = "setSoftInputShownOnFocus";
	        }
	         
	        if(methodName == null){
	            ed.setInputType(InputType.TYPE_NULL);  
	        }
	        else{
	            Class<EditText> cls = EditText.class;  
	            Method setShowSoftInputOnFocus;  
	            try {
	                setShowSoftInputOnFocus = cls.getMethod(methodName, boolean.class);
	                setShowSoftInputOnFocus.setAccessible(true);  
	                setShowSoftInputOnFocus.invoke(ed, false); 
	            } catch (NoSuchMethodException e) {
	                ed.setInputType(InputType.TYPE_NULL);
	                e.printStackTrace();
	            } catch (IllegalAccessException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            } catch (IllegalArgumentException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            } catch (InvocationTargetException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }  
	        }  
	}
	
	public static String getPackageName(){
		return App_Application.getInstance().getPackageName();
	}
	
	public static boolean checkURL(final String checkUrl) {
		
		checkThread = new Thread(){
			@Override
			public void run() {
				try {
					HttpURLConnection conn = (HttpURLConnection) new URL(checkUrl)
							.openConnection();
					conn.setConnectTimeout(10000);
					int code = conn.getResponseCode();
					if (code != 200) {
						value = false;
					} else {
						value = true;
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		};
		checkThread.start();
		
		LogEx.i("CONNECT", "connect netwrok result:" + value);
		return value;
	}
	
	public static String paramsToStr(List<NameValuePair> mParams){
		
		StringBuilder asb=new StringBuilder();
		for (int i = 0; i < mParams.size(); i++) {
			NameValuePair param = mParams.get(i);
			asb.append(param.getName());
			asb.append("=");
			asb.append(param.getValue());
			if(i < mParams.size()-1){
				asb.append("&");
			}
		}
		
		return asb.toString();
	}
	
	public static <T> String parseToStr(List<T> mList) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < mList.size(); i++) {
			sb.append(mList.get(i).toString());
			if(i != mList.size()-1)
				sb.append(",");
		}
		sb.append("]");
		Log.i("llj", "return--" + sb.toString());
		return sb.toString();
		
	}
	
	public static String getShowTime(String time){
		Date beginTime = null;
		Date currentTime = null;
		try {
			beginTime = mSimpleDateFormat.parse(time);
			currentTime = mSimpleDateFormat.parse(mSimpleDateFormat
					.format(new Date()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		long spanTime = currentTime.getTime() - beginTime.getTime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(beginTime);
		if(spanTime < DAY_MILL){
			return (new SimpleDateFormat("HH:mm")).format(calendar.getTime());
		}else if(spanTime >= DAY_MILL){
			return (new SimpleDateFormat("MM-dd")).format(calendar.getTime());
		}
		return (new SimpleDateFormat("MM-dd")).format(calendar.getTime());
		
	}
	
	//获取ArrayList中的最大值
	public static int ArrayListMax(ArrayList<ElectricDetail> list)
	 {
	            try
	            {
	            	int maxDevation = 0;
	                int totalCount = list.size();
	                if (totalCount >= 1)
	                {
	                    float max = list.get(0).getValue();
	                    for (int i = 0; i < totalCount; i++)
	                    {
	                    	float temp = list.get(i).getValue();
	                        if (temp > max)
	                        {
	                            max = temp;
	                        }
	                    } maxDevation = (int) max;
	                }
	                if(maxDevation%2 != 0){
	                	return maxDevation+1;
	                }
	                return maxDevation;
	            }
	            catch (Exception ex)
	            {
	                return 0;
	            }
	  }
	 
	//获取ArrayList中的最小值
	public static float ArrayListMin(ArrayList<ElectricDetail> list)
	 {
	            try
	            {
	            	float mixDevation = 0.0f;
	                int totalCount = list.size();
	                if (totalCount >= 1)
	                {
	                	float min = list.get(0).getValue();
	                        for (int i = 0; i < totalCount; i++)
	                        {
	                        	float temp = list.get(i).getValue();
	                            if (min > temp)
	                            {
	                                min = temp;
	                            }
	                        } mixDevation = min;
	                }
	                return mixDevation;
	            }
	            catch (Exception ex)
	            {
	            	return 0;
	            }
				
	        }
	//获取ArrayList中的最大值
	public static long ArrayListMaxDate(ArrayList<ElectricDetail> list)
	{
		try
		{
			long maxDate = 0;
			int totalCount = list.size();
			if (totalCount >= 1)
			{
				long max = list.get(0).getDay();
				for (int i = 0; i < totalCount; i++)
				{
					long temp = list.get(i).getDay();
					if (temp > max)
					{
						max = temp;
					}
				} maxDate =  max;
			}
			return maxDate;
		}
		catch (Exception ex)
		{
			return 0;
		}
	}
	
	//获取ArrayList中的最小值
	public static long ArrayListMinDate(ArrayList<ElectricDetail> list)
	{
		try
		{
			long mixDate = 0;
			int totalCount = list.size();
			if (totalCount >= 1)
			{
				long min = list.get(0).getDay();
				for (int i = 0; i < totalCount; i++)
				{
					long temp = list.get(i).getDay();
					if (min > temp)
					{
						min = temp;
					}
				} mixDate = min;
			}
			return mixDate;
		}
		catch (Exception ex)
		{
			return 0;
		}
		
	}
	
	
	public static long getTime(String time){
		Date d;
		long l = 0;
		try {
			d = mSimpleDateFormat.parse(time);
			l = d.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return l;
	}
	
	public static List<Drawable> getDrawableFromURL(List<String> urlList){
		List<Drawable> drawableList = new ArrayList<Drawable>();
		for (String url : urlList) {
			drawableList.add(loadImageFromNetwork(url));
		}
		System.out.println("drawableList--" + drawableList.size());
		return drawableList;
	}
	
	
	private static Drawable loadImageFromNetwork(String imageUrl) {
		Drawable drawable = null;
		try {
			// 可以在这里通过文件名来判断，是否本地有此图片
			drawable = Drawable.createFromStream(
					new URL(imageUrl).openStream(), null);
		} catch (IOException e) {
			Log.d("test", e.getMessage());
		}
		if (drawable == null) {
			Log.d("test", "null drawable");
		} else {
			Log.d("test", "not null drawable");
		}

		return drawable;
	}
	
	private static class DownloadImageTask extends AsyncTask<String, Void, Drawable> {
		
		
		protected Drawable doInBackground(String... urls) {
			return loadImageFromNetwork(urls[0]);
		}
		
		protected void onPostExecute(Drawable result) {
//			drawableList.add(result);
		}
	}
	
	public static long getCategoryMax(int flag) {
		long maxTime = 0;
		Calendar c = Calendar.getInstance();
		Date time = c.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int date = c.get(Calendar.DATE);
		int month = c.get(Calendar.MONTH) + 1;
		int year = c.get(Calendar.YEAR);
		int maxDateInMonth = c.getMaximum(Calendar.DAY_OF_MONTH);
		try {
			switch (flag) {
			case 0:
				maxTime = format.parse(year + "-" + month + "-" + date + " 23:59:59").getTime();
				break;
			case 1:
				maxTime = getWeekEnd()/1000 * 1000;
				break;
			case 2:
				maxTime = format.parse(year + "-" + month + "-" + date + " 00:00:00").getTime();
				break;

			default:
				break;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("maxTime" + format.format(new Date(maxTime)));
		return maxTime;
	}
	
	
	public static long getCategoryMin(int flag) {
		long minTime = 0;
		Calendar c = Calendar.getInstance();
		Date time = c.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int date = c.get(Calendar.DATE);
		int month = c.get(Calendar.MONTH) + 1;
		int year = c.get(Calendar.YEAR);
		int minDateInMonth = c.getMinimum(Calendar.DAY_OF_MONTH);
		try {
			switch (flag) {
			case 0:
				minTime = format.parse(year + "-" + month + "-" + date + " 00:00:00").getTime();
				break;
			case 1:
				minTime = getWeekStart()/1000 * 1000;
				break;
			case 2:
				minTime = format.parse(year + "-" + (month-1) + "-" + date + " 00:00:00").getTime();
				break;

			default:
				break;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("minTime" + minTime);
		return minTime;
	}

	
	  /**
     * 功能：获取本周的开始时间
     * 示例：2013-05-13 00:00:00
     */   
    private static long getWeekStart() {// 当周开始时间
            Calendar currentDate = Calendar.getInstance();
            currentDate.setFirstDayOfWeek(Calendar.MONDAY);
            currentDate.set(Calendar.HOUR_OF_DAY, 0);
            currentDate.set(Calendar.MINUTE, 0);
            currentDate.set(Calendar.SECOND, 0);
            currentDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            return  currentDate.getTime().getTime();
    }
    
    /**
     * 功能：获取本周的结束时间
     * 示例：2013-05-19 23:59:59
     */   
    private static long getWeekEnd() {// 当周结束时间
            Calendar currentDate = Calendar.getInstance();
            currentDate.setFirstDayOfWeek(Calendar.MONDAY);
            currentDate.set(Calendar.HOUR_OF_DAY, 23);
            currentDate.set(Calendar.MINUTE, 59);
            currentDate.set(Calendar.SECOND, 59);
            currentDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            return currentDate.getTime().getTime();
    }
}
