package com.evguard.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.audrey.view.ArcBar;
import com.evguard.model.ICommonWebResponse;
import com.evguard.model.WebReq_GetAdvertising;
import com.evguard.model.WebReq_GetCurrentInfo;
import com.evguard.model.WebRes_GetAdvertising;
import com.evguard.model.WebRes_GetCurrentInfo;
import com.evguard.tools.PhoneDirHelper;
import com.viewpagerindicator.CirclePageIndicator;
import com.xinghaicom.evguard.R;
import com.xinghaicom.security.MD5;

public class Fragment_StartPage extends Fragment_Base {

	private static final int MESSAGE_AD_GETTING = 0;
	protected static final int MESSAGE_AD_GETTINGFAIL = 1;
	protected static final int MESSAGE_AD_GETTINGSUCCESS = 2;
	private static final int MESSAGE_INFO_GETTING = 3;
	protected static final int MESSAGE_INFO_GETTINGFAIL = 4;
	protected static final int MESSAGE_INFO_GETTINGSUCCESS = 5;
	private static Fragment_StartPage mInstance;
	private static Object lockobj = new Object();
	private ViewPager adPagers;
	private CirclePageIndicator adPageIndicator;
	private List<String> drawableLists;
	private ImageView ivBattery;
	int i = 0;
	public Drawable drawable;
	public AdPagerAdapter adPagerAdapter;

	public static Fragment_StartPage getInstance() {
		if (mInstance == null)
			synchronized (lockobj) {
				mInstance = new Fragment_StartPage();
			}
		return mInstance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (layoutView != null) {
			return layoutView;
		}
		layoutView = inflater.inflate(R.layout.fragment_startpage, container,
				false);
		findViews();
		return layoutView;
	}

	private void findViews() {
		adPagers = (ViewPager) layoutView.findViewById(R.id.vp_ad);
		adPageIndicator = (CirclePageIndicator) layoutView
				.findViewById(R.id.page_indicator);
		tvCarNum = (TextView) layoutView.findViewById(R.id.tv_carnum);
		tvMileage = (TextView) layoutView.findViewById(R.id.tv_mileage);
		tvDrivingMileage = (TextView) layoutView
				.findViewById(R.id.tv_DrivingMileage);
		mArcBar = (ArcBar) layoutView.findViewById(R.id.cbar);
		ll_loading = (LinearLayout) layoutView.findViewById(R.id.ll_loading);
		mProgressBar = (ProgressBar) layoutView.findViewById(R.id.progressbar);
		tvTips = (TextView) layoutView.findViewById(R.id.tv_loading_tips);
		rl_load_success = (RelativeLayout) layoutView
				.findViewById(R.id.rl_load_success);
		cache = PhoneDirHelper.getInstance().getPhoneMemeryAppDataFile();

		if (!cache.exists()) {
			cache.mkdirs();
		}

	}

	@Override
	public void initData() {
		drawableLists = new ArrayList<String>();
		getAdvertisingList();
		getCurrentInfo();
	}

	/**
	 * 获取车辆当前信息
	 */
	private void getCurrentInfo() {
		Message waitMsg = mHandler.obtainMessage(MESSAGE_INFO_GETTING);
		mHandler.sendMessage(waitMsg);
		WebReq_GetCurrentInfo aWebReq_GetCurrentInfo = new WebReq_GetCurrentInfo();
		// web请求结果回调
		ICommonWebResponse<WebRes_GetCurrentInfo> aICommonWebResponse = new ICommonWebResponse<WebRes_GetCurrentInfo>() {

			@Override
			public void WebRequestException(String ex) {
				Message waitMsg = mHandler
						.obtainMessage(MESSAGE_INFO_GETTINGFAIL);
				mHandler.sendMessage(waitMsg);
			}

			@Override
			public void WebRequsetFail(String sfalied) {
				Message waitMsg = mHandler
						.obtainMessage(MESSAGE_INFO_GETTINGFAIL);
				mHandler.sendMessage(waitMsg);
			}

			@Override
			public void WebRequstSucess(WebRes_GetCurrentInfo aWebRes) {
				if (aWebRes.getResult().equals("0")) {
					Message waitMsg = mHandler
							.obtainMessage(MESSAGE_INFO_GETTINGSUCCESS);
					Bundle bundle = new Bundle();
					bundle.putString("CarNum", aWebRes.getCarNum());
					bundle.putString("DrivingMileage",
							aWebRes.getDrivingMileage());
					bundle.putString("Mileage", aWebRes.getMileage());
					bundle.putString("SOC", aWebRes.getSOC());
					waitMsg.setData(bundle);
					mHandler.sendMessage(waitMsg);
				} else {
					Message waitMsg = mHandler
							.obtainMessage(MESSAGE_INFO_GETTINGFAIL);
					mHandler.sendMessage(waitMsg);
				}
			}

		};
		WebRequestThreadEx<WebRes_GetCurrentInfo> aWebRequestThreadEx = new WebRequestThreadEx<WebRes_GetCurrentInfo>(
				aWebReq_GetCurrentInfo, aICommonWebResponse,
				new WebRes_GetCurrentInfo());
		new Thread(aWebRequestThreadEx).start();
	}

	/**
	 * 获取广告列表
	 */
	private void getAdvertisingList() {
		Message waitMsg = mHandler.obtainMessage(MESSAGE_AD_GETTING);
		mHandler.sendMessage(waitMsg);
		drawableLists.clear();
		if (adPagerAdapter != null)
			adPagerAdapter.notifyDataSetChanged();
		WebReq_GetAdvertising aWebReq_GetAdvertising = new WebReq_GetAdvertising();
		// web请求结果回调
		ICommonWebResponse<WebRes_GetAdvertising> aICommonWebResponse = new ICommonWebResponse<WebRes_GetAdvertising>() {

			@Override
			public void WebRequestException(String ex) {
				System.out.println("WebRequestException" + ex);
				Message waitMsg = mHandler
						.obtainMessage(MESSAGE_AD_GETTINGFAIL);
				waitMsg.obj = ex;
				mHandler.sendMessage(waitMsg);
			}

			@Override
			public void WebRequsetFail(String sfalied) {
				System.out.println("WebRequsetFail");
				Message waitMsg = mHandler
						.obtainMessage(MESSAGE_AD_GETTINGFAIL);
				waitMsg.obj = sfalied;
				mHandler.sendMessage(waitMsg);
			}

			@Override
			public void WebRequstSucess(WebRes_GetAdvertising aWebRes) {
				if (aWebRes.getResult().equals("0")) {
					System.out.println("WebRequstSucess");
					drawableLists = aWebRes.getImageList();
					Message waitMsg = mHandler
							.obtainMessage(MESSAGE_AD_GETTINGSUCCESS);
					mHandler.sendMessage(waitMsg);

				}
			}
		};

		WebRequestThreadEx<WebRes_GetAdvertising> aWebRequestThreadEx = new WebRequestThreadEx<WebRes_GetAdvertising>(
				aWebReq_GetAdvertising, aICommonWebResponse,
				new WebRes_GetAdvertising());
		new Thread(aWebRequestThreadEx).start();
	}

	class AdPagerAdapter extends PagerAdapter {
		private List<String> mList;
		private File cache;
		private Map<Integer, ImageView> mImgList = new HashMap<Integer, ImageView>();

		public AdPagerAdapter(List<String> drawableLists, File fileCache) {
			mList = drawableLists;
			urls = (String[]) mList.toArray(new String[mList.size()]);
			cache = fileCache;
		}

		private String[] urls;

		@Override
		public int getCount() {

			return mList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {

			// if(mImgList.containsKey(position)){
			// container.addView(mImgList.get(position));
			// return mImgList.get(position);
			// }
			final ImageView iva;
			iva = new ImageView(mContext);
			iva.setScaleType(ScaleType.CENTER_CROP);
			asyncloadImage(iva, urls[position]);
			mImgList.put(position, iva);

			// Drawable cachedImage = asyncImageLoader.loadDrawable(
			// urls[position], new ImageCallback() {
			//
			// public void imageLoaded(Drawable imageDrawable,
			// String imageUrl) {
			// System.out.println(imageUrl);
			// iva.setImageDrawable(imageDrawable);
			// mImgList.put(position, iva);
			//
			// }
			// });

			// new DownloadImageTask().execute(drawableLists.get(position));
			// iva.setImageDrawable(cachedImage);

			container.addView(iva);

			return iva;
		}

		private void asyncloadImage(ImageView iv_header, String path) {
			AsyncImageTask task = new AsyncImageTask(iv_header);
			task.execute(path);
		}

		private final class AsyncImageTask extends
				AsyncTask<String, Integer, Uri> {

			private ImageView iv_header;

			public AsyncImageTask(ImageView iv_header) {

				this.iv_header = iv_header;
			}

			// 后台运行的子线程子线程
			@Override
			protected Uri doInBackground(String... params) {
				try {
					return getImageURI(params[0], cache);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			// 这个放在在ui线程中执行
			@Override
			protected void onPostExecute(Uri result) {
				super.onPostExecute(result);
				// 完成图片的绑定
				if (iv_header != null && result != null) {
					iv_header.setImageURI(result);
				}
			}
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mInstance = null;
	}

	private Handler mHandler = new Handler() {

		private Dg_Waiting mDownDialog;

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MESSAGE_AD_GETTING:
				ll_loading.setVisibility(View.VISIBLE);
				mProgressBar.setVisibility(View.VISIBLE);
				tvTips.setText("正在下载图片");
				rl_load_success.setVisibility(View.GONE);
				break;
			case MESSAGE_AD_GETTINGFAIL:
				System.out.println("MESSAGE_AD_GETTINGFAIL-- here");
				ll_loading.setVisibility(View.VISIBLE);
				mProgressBar.setVisibility(View.GONE);
				tvTips.setVisibility(View.VISIBLE);
				tvTips.setText("暂无广告！");
				tvTips.setTextColor(0X86000000);
				rl_load_success.setVisibility(View.GONE);
				break;
			case MESSAGE_AD_GETTINGSUCCESS:
				ll_loading.setVisibility(View.GONE);
				rl_load_success.setVisibility(View.VISIBLE);
				showAd();
				break;
			case MESSAGE_INFO_GETTING:
				if (mDownDialog != null) {
					mDownDialog.dismiss();
				}
				if (mDownDialog == null) {
					mDownDialog = Dg_Waiting.newInstance("获取信息",
							"正在获取当前车辆信息，请稍候.");
					mDownDialog.setCancelable(false);
				}
				mDownDialog.show(getChildFragmentManager(), "");
				break;

			case MESSAGE_INFO_GETTINGFAIL:
				if (mDownDialog != null) {
					mDownDialog.dismiss();
					mDownDialog = null;
				}
				String errorMsg = "获取当前车辆信息失败！";
				if (msg.obj != null)
					errorMsg += msg.obj;
				showToast(errorMsg);
				break;
			case MESSAGE_INFO_GETTINGSUCCESS:
				if (mDownDialog != null) {
					mDownDialog.dismiss();
					mDownDialog = null;
				}
				Bundle bundle = msg.getData();
				if (bundle != null) {
					setData(bundle);
				}
				break;
			default:
				break;
			}
		}

	};

	private void showAd() {
		adPagers.setVisibility(View.VISIBLE);
		adPageIndicator.setVisibility(View.VISIBLE);
		adPagerAdapter = new AdPagerAdapter(drawableLists, cache);
		adPagers.setAdapter(adPagerAdapter);
		adPagers.setCurrentItem(-1);
		adPageIndicator.setViewPager(adPagers);
		adPagerAdapter.notifyDataSetChanged();
	}

	private TextView tvCarNum;
	private TextView tvMileage;
	private TextView tvDrivingMileage;
	private TextView tvSOC;
	private ArcBar mArcBar;
	private LinearLayout ll_loading;
	private RelativeLayout rl_load_success;
	private File cache;
	private ProgressBar mProgressBar;
	private TextView tvTips;

	protected void setData(Bundle bundle) {
		tvCarNum.setText(bundle.getString("CarNum"));
		tvMileage.setText(bundle.getString("DrivingMileage"));
		tvDrivingMileage.setText(bundle.getString("Mileage"));
		mArcBar.setBarValue(Integer.parseInt(bundle.getString("SOC")));
	}

	/*
	 * 从网络上获取图片，如果图片在本地存在的话就直接拿，如果不存在再去服务器上下载图片 这里的path是图片的地址
	 */
	public Uri getImageURI(String path, File cache) throws Exception {
		String name = path.substring(path.lastIndexOf("/") + 1);
		File file = new File(cache, name);
		// 如果图片存在本地缓存目录，则不去服务器下载
		if (file.exists()) {
			Log.i("startPage", "file is exits!  -- " + name);
			return Uri.fromFile(file);// Uri.fromFile(path)这个方法能得到文件的URI
		} else {
			Log.i("startPage", "file is not exits!  -- " + name);
			// 从网络上获取图片
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			if (conn.getResponseCode() == 200) {

				InputStream is = conn.getInputStream();
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
				is.close();
				fos.close();
				// 返回一个URI对象
				return Uri.fromFile(file);
			}
		}
		return null;
	}

	@Override
	protected void restoreState(Bundle saveState) {
		
	}

	@Override
	protected Bundle saveState(Bundle saveState) {
		return saveState;
	}
}
