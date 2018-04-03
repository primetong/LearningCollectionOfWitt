package com.evguard.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.audrey.mode.TableShowViewData;
import com.audrey.view.TableShowSurfaceview;
import com.audrey.view.TableShowSurfaceview.ISurfaceEvent;
import com.evguard.model.ElectricDetail;
import com.evguard.model.ICommonWebResponse;
import com.evguard.model.WebReq_GetEnergy;
import com.evguard.model.WebRes_GetEnergy;
import com.evguard.tools.CommUtils;
import com.xinghaicom.evguard.R;

public class Fragment_Energy1 extends Fragment_Base {
	private String TAG =this.getClass().getSimpleName();
	private static final int MESSAGE_GETTING = 0;
	private static final int MESSAGE_GETTING_OK = 1;
	private static final int MESSAGE_GETTING_FAILED = 2;
	private Handler mHandler;
	private static Fragment_Energy mInstance;
	private static Object lockobj = new Object();
	private TextView tvMileage;
	private TextView tvConsumeElectricity;
	private TextView tvAverageConsumption;
	private TextView tvEconomy;
	private RadioGroup rgTimeQuery;
	private ImageButton btnToLeft;
	private ImageButton btnToRight;
	private int pageIndex = 0;
	private int rbIndex = 0;
	private TextView tvTime;
	private RadioButton rbDay;
	private RadioButton rbWeek;
	private RadioButton rbMonth;
	private Typeface type_aria;
	private int month;
	private int day;
	private int week;
	private int year;
	private int month_day;
	private int week_day;
	private Dg_Waiting mDownDialog = null;
	private boolean mIsUserCancle = false;
	private ArrayList<ElectricDetail> electricInfoList;
	public TableShowSurfaceview aTableShowSurfaceview;
	private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	public static Fragment_Energy getInstance() {
		if (mInstance == null)
			synchronized (lockobj) {
				mInstance = new Fragment_Energy();
			}
		return mInstance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (layoutView != null) {
			return layoutView;
		}
		layoutView = inflater.inflate(R.layout.fagment_energy, container, false);
		initHandler();
		findView();
		return layoutView;
	}

	@Override
	public void initData() {
		Calendar c = Calendar.getInstance();
		month = c.get(Calendar.MONTH)+1;
		day = c.get(Calendar.DAY_OF_MONTH);
		week = c.get(Calendar.WEEK_OF_MONTH);
		year = c.get(Calendar.YEAR);
		month_day = c.get(Calendar.DATE);
		week_day = c.get(Calendar.DAY_OF_WEEK);
		getEnergy(1);
		btnToLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				btnToLeft.setClickable(false);
				if(pageIndex > 0){
					pageIndex = pageIndex - 1;
				}else if(pageIndex == 0){
					pageIndex = 2;
				}
				setCheck(pageIndex);
			}
		});
		btnToRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				btnToRight.setClickable(false);
				if(pageIndex < 2){
					pageIndex = pageIndex + 1;
				}else if(pageIndex == 2){
					pageIndex = 0;
				}
				
				setCheck(pageIndex);
			}
			
		});
		
		rgTimeQuery.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
				if(checkedId == rbDay.getId()){
					getEnergy(1);
					rbIndex = 0;
					rbDay.setChecked(true);
				} 
				if(checkedId == rbWeek.getId()){
					getEnergy(7);
					rbIndex = 1;
					rbWeek.setChecked(true);
				} 
				if(checkedId == rbMonth.getId()){
					getEnergy(30);
					rbIndex = 2;
					rbMonth.setChecked(true);
				} 
			}
		});
		
		
	}
	private void setCheck(int pageIndex) {
		switch (pageIndex) {
		case 0:
			rbDay.setChecked(true);
			break;
		case 1:
			rbWeek.setChecked(true);
			break;
		case 2:
			rbMonth.setChecked(true);
			break;
		default:
			break;
		}
	}
	
	private void setData(int rbIndex, Bundle bundle) {
		if(bundle != null){
			electricInfoList = bundle.getParcelableArrayList("ElectricInfos");
			final List<TableShowViewData> points = getVirData(rbIndex,electricInfoList);
			aTableShowSurfaceview.setISurfaceEvent(new ISurfaceEvent(){

				@Override
				public void onSurfaceViewCreated() {
					aTableShowSurfaceview.setIsDynamicDraw(false);
					//这里设置一次数据用于保持图像
					try {
						aTableShowSurfaceview.setData(points);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
	    		
	    	});

			
			
			float max = CommUtils.ArrayListMax(electricInfoList);
//			aTableShowSurfaceview.setYCheckValue(-1);
			aTableShowSurfaceview.setDrawLineSleepTime(30);
			aTableShowSurfaceview.setIsDynamicDraw(true);
			Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
//			aTableShowSurfaceview.setTypeFace(font);
//			aTableShowSurfaceview.setOnTop(true);
			switch (rbIndex) {
			case 0:
				tvTime.setText(month + " 月" + day + " 日 ");
//				aSplineChartView.setIncrease(30*24*60*60, 50);
//				aSplineChartView.setData(points);
				aTableShowSurfaceview.setIsDynamicDraw(false);
				//这里设置一次数据用于保持图像
				try {
					aTableShowSurfaceview.setData(points);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
				break;
			case 1:
				tvTime.setText(month + " 月    第 " + week + " 周");
//				aSplineChartView.setIncrease(7*24*60*60, 50);
//				aSplineChartView.setData(points);
				aTableShowSurfaceview.setIsDynamicDraw(false);
				//这里设置一次数据用于保持图像
				try {
					aTableShowSurfaceview.setData(points);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 2:
				tvTime.setText(month + " 月   " + year);
//				aSplineChartView.setIncrease(24*60*60, 50);
//				aSplineChartView.setData(points);
				aTableShowSurfaceview.setIsDynamicDraw(false);
				//这里设置一次数据用于保持图像
				try {
					aTableShowSurfaceview.setData(points);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			default:
				break;
		}
			tvMileage.setText(bundle.getString("Mileage"));
			tvConsumeElectricity.setText(bundle.getString("ConsumeElectricity"));
			tvAverageConsumption.setText(bundle.getString("AverageConsumption"));
			tvEconomy.setText(bundle.getString("Economy"));
			
			
		}
		pageIndex = rbIndex;
		System.out.println("pageIndex--" + pageIndex);
	}

	private void getEnergy(int day) {
		Message waitMsg = mHandler.obtainMessage(MESSAGE_GETTING);
		mHandler.sendMessage(waitMsg);
		
		WebReq_GetEnergy aWebReq_GetEnergy = new WebReq_GetEnergy(day);
		ICommonWebResponse<WebRes_GetEnergy> aICommonWebResponse = new ICommonWebResponse<WebRes_GetEnergy>() {

			@Override
			public void WebRequestException(String ex) {
				Message endMsg = mHandler.obtainMessage(MESSAGE_GETTING_FAILED);
				mHandler.sendMessage(endMsg);
			}

			@Override
			public void WebRequsetFail(String sfalied) {
				Message endMsg = mHandler.obtainMessage(MESSAGE_GETTING_FAILED);
				mHandler.sendMessage(endMsg);
			}

			@Override
			public void WebRequstSucess(WebRes_GetEnergy aWebRes) {
				if (aWebRes.getResult().equals("0")) {
					Message endMsg = mHandler.obtainMessage(MESSAGE_GETTING_OK);
					Bundle bundle = new Bundle();
					bundle.putString("Mileage", aWebRes.getMileage());
					bundle.putString("ConsumeElectricity", aWebRes.getConsumeElectricity());
					bundle.putString("AverageConsumption", aWebRes.getAverageConsumption());
					bundle.putString("Economy", aWebRes.getEconomy());
					bundle.putParcelableArrayList("ElectricInfos", aWebRes.getElectricInfo());
					endMsg.setData(bundle);
					mHandler.sendMessage(endMsg);
				} else {
					Message endMsg = mHandler.obtainMessage(MESSAGE_GETTING_FAILED);
					mHandler.sendMessage(endMsg);
				}
			}
		};
		WebRequestThreadEx<WebRes_GetEnergy> aWebRequestThreadEx = new WebRequestThreadEx<WebRes_GetEnergy>(aWebReq_GetEnergy, aICommonWebResponse, new WebRes_GetEnergy());
		new Thread(aWebRequestThreadEx).start();
	}

	@Override
	public void onDestroy()
	{		
		super.onDestroy();
		mInstance=null;
	}

	private void findView() {
		tvMileage = (TextView) layoutView.findViewById(R.id.tv_mileage);
		tvConsumeElectricity = (TextView) layoutView.findViewById(R.id.tv_consume_electricity);
		tvAverageConsumption = (TextView) layoutView.findViewById(R.id.tv_average_consumption);
		aTableShowSurfaceview = (TableShowSurfaceview) layoutView.findViewById(R.id.mytableview);
		tvEconomy = (TextView) layoutView.findViewById(R.id.tv_economy);
		rgTimeQuery = (RadioGroup) layoutView.findViewById(R.id.rg_query_time);
		btnToLeft = (ImageButton) layoutView.findViewById(R.id.btn_to_left);
		btnToRight = (ImageButton) layoutView.findViewById(R.id.btn_to_right);
		tvTime = (TextView) layoutView.findViewById(R.id.time);
		rbDay = (RadioButton) layoutView.findViewById(R.id.rb_day);
		rbWeek = (RadioButton) layoutView.findViewById(R.id.rb_week);
		rbMonth = (RadioButton) layoutView.findViewById(R.id.rb_month);
	}


	private void initHandler() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Log.i("llj", "here--" + msg.what);
				switch (msg.what) {
				case MESSAGE_GETTING:
					if(mDownDialog != null){
						mDownDialog.dismiss();
					}
					if (mDownDialog == null) {
						mDownDialog = Dg_Waiting.newInstance("获取能源信息", "正在获取能源信息，请稍候.");
						mDownDialog.setCancelable(false);
					}
					
					mDownDialog.show(getFragmentManager(), "");
					break;
				case MESSAGE_GETTING_OK:
					if (mDownDialog != null) {
						mDownDialog.dismiss();
						mDownDialog = null;
						
					}
					btnToRight.setClickable(true);
					btnToLeft.setClickable(true);
					
					Bundle data = msg.getData();
					setData(rbIndex,data);
					break;
				case MESSAGE_GETTING_FAILED:
					if (mDownDialog != null){
						mDownDialog.dismiss();
						mDownDialog = null;
					}
					String errorMsg = "获取能源信息失败！";
					btnToRight.setClickable(true);
					btnToLeft.setClickable(true);
					if(msg.obj != null) 
						errorMsg += msg.obj;
					showToast(errorMsg);
					break;
			}
		}};
	}

	private List<TableShowViewData> getVirData(int count, ArrayList<ElectricDetail> aElectricInfoList){
		List<TableShowViewData> alist=new ArrayList<TableShowViewData>();
		float y = 0.0f;
		long x = 0;
		for(int i=0;i<aElectricInfoList.size();i++){
				y = aElectricInfoList.get(i).getValue();
				x = aElectricInfoList.get(i).getDay();
				Log.i("122", (new Date(x)).toString());
				TableShowViewData p=new TableShowViewData(x,y);
			alist.add(p);
//			Log.i("121", "***x:"+x+"::y:"+y);
		}
		return alist;
	}


	public void setCallBack_FragmentEnergy(
			CallBack_FragmentEnergy aOnFenceActionClickListener) {
	}

	public interface CallBack_FragmentEnergy {
	}

	@Override
	protected void restoreState(Bundle saveState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Bundle saveState(Bundle saveState) {
		// TODO Auto-generated method stub
		return null;
	}

}
