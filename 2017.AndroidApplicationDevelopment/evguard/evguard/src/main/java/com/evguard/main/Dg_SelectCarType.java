package com.evguard.main;

import java.util.ArrayList;
import java.util.List;

import com.evguard.model.ServerInfo;
import com.evguard.tools.LogEx;
import com.xinghaicom.evguard.R;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Dg_SelectCarType extends Dg_BaseSimpleStyle {

	private LinearLayout ll_content;
	private TextView tv_error=null;
	private ProgressBar mBar=null;
	private ListView carTypeListView=null;
	private CarTypeAdapter mCarTypeAdapter=null;
	private List<ServerInfo> mDatas=new ArrayList<ServerInfo>();
	private OnCarTypeSelectedListener mOnCarTypeSelectedListener=null;
	
	public static Dg_SelectCarType newInstance(String sTitle,
			String posbtntip) {
		Dg_SelectCarType f = new Dg_SelectCarType();
		Bundle args = new Bundle();

		return f;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	@Override
	public void onSaveInstanceState(Bundle outInstanceState) {
		
		super.onSaveInstanceState(outInstanceState);
	}

	public void initialize(View baseview, Bundle savedInstanceState) {
	
		DisplayMetrics metric = new DisplayMetrics();
		Window window =this.getActivity().getWindow();
		getActivity().getWindow().getWindowManager().getDefaultDisplay().getMetrics(metric);
//		window.setLayout(metric.widthPixels*9/10, metric.heightPixels*8/10);
//		window.setGravity(Gravity.CENTER);
		
		ll_content = (LinearLayout) baseview.findViewById(R.id.ll_dgcontent);

		LinearLayout contentview=new LinearLayout(mContext);
		contentview.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams .WRAP_CONTENT);
		contentview.setLayoutParams(params);
		
		TextView tv=new TextView(mContext);
		LinearLayout.LayoutParams param1=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		tv.setLayoutParams(param1);
		tv.setText("车型选择");
		tv.setTextSize(18);
		tv.setTextColor(mContext.getResources().getColor(R.color.black));
		tv.setPadding(5*iDiptopx, 15*iDiptopx, 5*iDiptopx, 15*iDiptopx);
		tv.setGravity(Gravity.CENTER);
		contentview.addView(tv);
		
		LinearLayout llshow=new LinearLayout(mContext);
		LinearLayout.LayoutParams llshowParam=new LinearLayout.LayoutParams(metric.widthPixels*2/3,LinearLayout.LayoutParams.WRAP_CONTENT);
		llshow.setOrientation(LinearLayout.VERTICAL);
		llshow.setLayoutParams(llshowParam);
		llshow.setGravity(Gravity.CENTER);
		
		mBar=new ProgressBar(mContext);
		LinearLayout.LayoutParams barParam=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		barParam.gravity=Gravity.CENTER;
		mBar.setLayoutParams(barParam);
		
		llshow.addView(mBar);
		
		tv_error=new TextView(mContext);
		LinearLayout.LayoutParams errorParam=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		errorParam.gravity=Gravity.CENTER;
		tv_error.setLayoutParams(errorParam);
		tv_error.setTextSize(20);
		tv_error.setTextColor(mContext.getResources().getColor(R.color.black));
		tv_error.setPadding(5*iDiptopx, 15*iDiptopx, 5*iDiptopx, 15*iDiptopx);
		tv_error.setGravity(Gravity.CENTER);
		tv_error.setVisibility(View.GONE);
		llshow.addView(tv_error);
		
		
		//车辆列表栏
		carTypeListView=new ListView(mContext);
		LinearLayout.LayoutParams listViewParam=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,metric.heightPixels*6/10);
		carTypeListView.setLayoutParams(listViewParam);
		carTypeListView.setCacheColorHint(Color.TRANSPARENT);
//		carTypeListView.setDivider(mContext.getResources().getDrawable(R.drawable.bg_divider_apptheme_grey));
		carTypeListView.setDivider(new ColorDrawable(R.color.apptheme_grey));  
		carTypeListView.setDividerHeight(1);  
		mCarTypeAdapter=new CarTypeAdapter();
		carTypeListView.setAdapter(mCarTypeAdapter);
		carTypeListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(mOnCarTypeSelectedListener==null)return;
				ServerInfo aServer=mDatas.get(position);
				mOnCarTypeSelectedListener.onCarTypeSelected(aServer);
				
			}
			
		} );
		carTypeListView.setVisibility(View.GONE);
		llshow.addView(carTypeListView);
		
		contentview.addView(llshow);
		
//		LinearLayout operateview=new LinearLayout(mContext);
//		operateview.setOrientation(LinearLayout.HORIZONTAL);
//		LinearLayout.LayoutParams params2=new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams .MATCH_PARENT,LinearLayout.LayoutParams .WRAP_CONTENT);
//		operateview.setLayoutParams(params2);		
//		TextView tv_cancel=new TextView(mContext);
//		LinearLayout.LayoutParams params3=new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams .WRAP_CONTENT,LinearLayout.LayoutParams .WRAP_CONTENT);
//		tv_cancel.setLayoutParams(params3);
//		tv_cancel.setText("取消");
//		tv_cancel.setTextSize(16);
//		operateview.addView(tv_cancel);	
//		TextView tv_ok=new TextView(mContext);
//		LinearLayout.LayoutParams params4=new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams .WRAP_CONTENT,LinearLayout.LayoutParams .WRAP_CONTENT);
//		tv_ok.setLayoutParams(params4);
//		tv_ok.setText("选好了");
//		tv_ok.setTextSize(16);
//		operateview.addView(tv_ok);
		
//		contentview.addView(operateview);
//		contentview.addView(tv_cancel);
		ll_content.addView(contentview);
	}
	
	
	class CarTypeAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mDatas.size();
		}

		@Override
		public ServerInfo getItem(int position) {
			// TODO Auto-generated method stub
			return mDatas.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ServerInfo ainfo=getItem(position);
			
			TextView tv=new TextView(mContext);
			
//			tv.setLayoutParams(param1);
			tv.setTextColor(mContext.getResources().getColor(R.color.apptheme_black));
			tv.setText(ainfo.getAgentname());
			tv.setTextSize(16);
			tv.setPadding(5*iDiptopx, 15*iDiptopx, 5*iDiptopx, 15*iDiptopx);
			tv.setGravity(Gravity.CENTER);
			return tv;
		}
		
	}
	public void setOnCarTypeSelectedListener(OnCarTypeSelectedListener aOnCarTypeSelectedListener){
		this.mOnCarTypeSelectedListener=aOnCarTypeSelectedListener;
	}
	public void showDialog(FragmentManager manager){

		super.show(manager, "");
	}
	public void update(List<ServerInfo> infos){
		carTypeListView.setVisibility(View.VISIBLE);
		mBar.setVisibility(View.GONE);
		tv_error.setVisibility(View.GONE);
		mDatas.clear();
		mDatas.addAll(infos);
		if(mCarTypeAdapter!=null){
			mCarTypeAdapter.notifyDataSetChanged();
		}
		carTypeListView.setVisibility(View.VISIBLE);
	}
	public void showError(String s){
		carTypeListView.setVisibility(View.GONE);
		mBar.setVisibility(View.GONE);
		tv_error.setText(s);
		tv_error.setVisibility(View.VISIBLE);
		
	}
	public interface OnCarTypeSelectedListener{
		public void onCarTypeSelected(ServerInfo ainfo);
	}
}
