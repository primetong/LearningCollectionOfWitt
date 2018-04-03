package com.evguard.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.evguard.tools.CommUtils;
import com.xinghaicom.evguard.R;

public class Dg_FenceTimeSelect extends Dg_Base {

	private LinearLayout ll_content;
	private TextView mTitle;
	private ListView mListTime;
	private Button mBtnOK;
	private BaseAdapter adapter;
	private List<String> weeks;
	private List<String> weeksSelected;
	private OnDgFenceSelectActionListener mOnDgFenceSelectActionListener;
	private String week1="星期一";
	private String week2="星期二";
	private String week3="星期三";
	private String week4="星期四";
	private String week5="星期五";
	private String week6="星期六";
	private String week7="星期日";
	public static Dg_FenceTimeSelect newInstance() {
		Dg_FenceTimeSelect f = new Dg_FenceTimeSelect();
		Bundle args = new Bundle();
		f.setArguments(args);
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

	
	public void setOnDgFenceSelectActionListener(OnDgFenceSelectActionListener aOnDgFenceSelectActionListener)
	{
		this.mOnDgFenceSelectActionListener=aOnDgFenceSelectActionListener;
	}

	private void initDataSource()
	{
		if(weeksSelected==null){
			weeksSelected=new ArrayList<String>();
		}
		weeksSelected.clear();
		if(null==weeks){
			weeks=new ArrayList<String>();
			weeks.add(0,week1);
			weeks.add(1,week2);
			weeks.add(2,week3);
			weeks.add(3,week4);
			weeks.add(4,week5);
			weeks.add(5,week6);
			weeks.add(6,week7);
		}
		if(null==adapter){
			adapter=new BaseAdapter(){
	
				@Override
				public int getCount() {
					// TODO Auto-generated method stub
					return weeks.size();
				}
	
				@Override
				public Object getItem(int position) {
					// TODO Auto-generated method stub
					return weeks.get(position);
				}
	
				@Override
				public long getItemId(int position) {
					// TODO Auto-generated method stub
					return 0;
				}
	
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					// TODO Auto-generated method stub
					if(convertView==null)
					{
						convertView=View.inflate(mContext, R.layout.listviewitem_fentimeselect, null);
					}
					TextView tv_week=(TextView)convertView.findViewById(R.id.tv_week);
					CheckBox cb_select=(CheckBox)convertView.findViewById(R.id.cb_select);
					
					final String week=(String)getItem(position);
					tv_week.setText(week);
					cb_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
	
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// TODO Auto-generated method stub
							if(isChecked)
							{
								if(weeksSelected.contains(week))return;
								weeksSelected.add(week);
							}
							else
							{
								if(weeksSelected.contains(week))
								{
									weeksSelected.remove(week);
								}
							}
						}
					});
					if(weeksSelected.contains(week)){
						cb_select.setChecked(true);
					}else{
						cb_select.setChecked(false);
					}
					return convertView;
				}		
			};
		}
	}
	public void initialize(View baseview, Bundle savedInstanceState) {
//		initDataSource();
		mTitle=(TextView)baseview.findViewById(R.id.tv_dgtip);
		ll_content=(LinearLayout)baseview.findViewById(R.id.ll_dgcontent);
		mTitle.setText(mContext.getResources().getString(R.string.dg_fencetimeselect_title));
		
		mListTime=new ListView(mContext);
		LinearLayout.LayoutParams listparams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		listparams.weight=1;
		mListTime.setLayoutParams(listparams);
		mListTime.setCacheColorHint(mContext.getResources().getColor(android.R.color.transparent));
		mListTime.setAdapter(adapter);
		ll_content.addView(mListTime);
		
		mBtnOK=new Button(mContext);
		mBtnOK.setText(mContext.getResources().getString(R.string.ok));
		LinearLayout.LayoutParams btnparams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		btnparams.weight=0;
		mBtnOK.setLayoutParams(btnparams);
		mBtnOK.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				if(mOnDgFenceSelectActionListener!=null)
				{
					String showdate="";
					String valuedate="";
					
					for(int i=0;i<weeks.size();i++)
					{
						if(weeksSelected.contains(weeks.get(i)))
						{
							showdate+=weeks.get(i)+",";
							valuedate=i+",";
						}
					}
					if(showdate.endsWith(",")){
						showdate=showdate.substring(0, showdate.length()-1);
						valuedate=valuedate.substring(0, valuedate.length()-1);
					}
					mOnDgFenceSelectActionListener.onOkClick(showdate,valuedate);
				}
			}
		});
		ll_content.addView(mBtnOK);
		
		this.setCancelable(true);
		this.getDialog().setOnKeyListener(new OnKeyListener()
	        {
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event){
	        	   if (keyCode == KeyEvent.KEYCODE_BACK)
		           {
		        	   if(mOnCancelListener!=null)mOnCancelListener.onCancle();
		           }
		            return false; // pass on to be processed as normal
	           }
	       });
	}
	public void setInitValue(String fencetime){
		initDataSource();
		if(CommUtils.isEmpty(fencetime))return;
		String[] times=fencetime.trim().split(",");

		weeksSelected.addAll(Arrays.asList(times));
	}
	public interface OnDgFenceSelectActionListener
	{
		public void onOkClick(String show,String value);
	}
	
}
