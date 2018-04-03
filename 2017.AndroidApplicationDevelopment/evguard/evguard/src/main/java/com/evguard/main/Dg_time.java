package com.evguard.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.xinghaicom.evguard.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;



public class Dg_time extends DialogFragment {
	protected Context mContext;
	private DatePicker mDatePicker=null;
	private TimePicker mTimePicker=null;
	private TextView tv_ok=null;
	private TextView tv_cancle=null;
	
	private String sDate="";
	SimpleDateFormat format=null;
	private Calendar mCalendar=null;
	int hour=-1;  
     int minute=-1;
	
	public static Dg_time newInstance(String sdate) {
		Dg_time f = new Dg_time();
		Bundle args = new Bundle();
		args.putString("date", sdate);
		f.setArguments(args);
		return f;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    sDate=getArguments().getString("date");
	    format = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
	    Date date=new Date();
	    try {
	    	date=format.parse(sDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mCalendar= Calendar.getInstance();
		mCalendar.setTime(date);
	}
	
	 @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState)  
    {  
	    mContext=App_Application.getInstance();
	    getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);  
        View view = inflater.inflate(R.layout.dg_time, container);  
        mDatePicker=(DatePicker)view.findViewById(R.id.dp_date);
        mTimePicker=(TimePicker)view.findViewById(R.id.tp_time);
        tv_ok=(TextView)view.findViewById(R.id.tv_ok);
        tv_cancle=(TextView)view.findViewById(R.id.tv_cancle);
        initTime();
	    return view;  
    } 
	 private void initTime(){  
         mDatePicker.init(mCalendar.get(Calendar.YEAR),
        		 mCalendar.get(Calendar.MONTH), 
        		 mCalendar.get(Calendar.DAY_OF_MONTH), null);
        
         mTimePicker.setIs24HourView(true);  
         mTimePicker.setCurrentHour(mCalendar.get(Calendar.HOUR_OF_DAY));  
         mTimePicker.setCurrentMinute(mCalendar.get(Calendar.MINUTE));  
       
         System.out.println("mCalendar==" + mCalendar.get(Calendar.YEAR) + ", " +
        		 mCalendar.get(Calendar.MONTH) + ", " + 
        		 mCalendar.get(Calendar.DAY_OF_MONTH) + ", " + mCalendar.get(Calendar.HOUR_OF_DAY)
        		 + ", " + mCalendar.get(Calendar.MINUTE));
         
         tv_ok.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
	             int arrive_year = mDatePicker.getYear();  
	             int arrive_month = mDatePicker.getMonth();  
	             int arrive_day = mDatePicker.getDayOfMonth();  
                 int arrive_hour = mTimePicker.getCurrentHour();  
                 int arrive_min = mTimePicker.getCurrentMinute();  
                 Calendar calendar = Calendar.getInstance();
                 calendar.set(arrive_year, arrive_month, arrive_day,arrive_hour,arrive_min);
                 
                
                 String sTime=format.format(calendar.getTime());
                 
				OnTimeSelectedListener aOnTimeSelectedListener=(OnTimeSelectedListener)getActivity();
				aOnTimeSelectedListener.onTimeSelected(sTime);
				dismissAllowingStateLoss();
			}
        	 
         });
         tv_cancle.setOnClickListener(new OnClickListener(){

 			@Override
 			public void onClick(View v) {
 				// TODO Auto-generated method stub
 				dismissAllowingStateLoss();
 			}
         	 
          });
	 }
	 public interface OnTimeSelectedListener{
		 public void onTimeSelected(String stime);
	 }
}
