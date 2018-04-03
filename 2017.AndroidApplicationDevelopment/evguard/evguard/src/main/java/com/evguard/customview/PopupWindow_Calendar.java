package com.evguard.customview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.evguard.customview.KCalendar.OnCalendarClickListener;
import com.evguard.customview.KCalendar.OnCalendarDateChangedListener;
import com.xinghaicom.evguard.R;

public class PopupWindow_Calendar extends PopupWindow {
	

	String date = null;// 设置默认选中的日期  格式为 “2014-04-05” 标准DATE格式   
	private View mParent;
	private OnPopupWindowCalendarActionListener monPopupWindowCalendarActionListener;
	public PopupWindow_Calendar(Context mContext, View parent,OnPopupWindowCalendarActionListener aonPopupWindowCalendarActionListener) {
		super(mContext);//android2.3中必须使用有参构造函数。
		mParent=parent;
		monPopupWindowCalendarActionListener=aonPopupWindowCalendarActionListener;
		
		View view =LayoutInflater.from(mContext).inflate( R.layout.popupwindow_calendar,null);
		view.startAnimation(AnimationUtils.loadAnimation(mContext,
				R.anim.fade_in));
		LinearLayout ll_popup = (LinearLayout) view
				.findViewById(R.id.ll_popup);
		ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
				R.anim.push_bottom_in_1));
		setContentView(view);		   
//		setWidth((int) (CommUtils.screen_width*9/10));
//		setHeight((int) (CommUtils.screen_height*7/10));
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setBackgroundDrawable(null);
		setFocusable(true);
		setOutsideTouchable(true);
//		
//		showAtLocation(parent, Gravity.BOTTOM, 0, 0);
//		update();

		final TextView popupwindow_calendar_month = (TextView) view
				.findViewById(R.id.popupwindow_calendar_month);
		final KCalendar calendar = (KCalendar) view
				.findViewById(R.id.popupwindow_calendar);
		Button popupwindow_calendar_bt_enter = (Button) view
		.findViewById(R.id.popupwindow_calendar_bt_enter);
        Button popupwindow_calendar_bt_quit=(Button)view
        .findViewById(R.id.popupwindow_calendar_bt_quit);
        
		popupwindow_calendar_month.setText(calendar.getCalendarYear() + "年"
				+ calendar.getCalendarMonth() + "月");

		List<String> list = new ArrayList<String>(); //设置标记列表
		list.add("2015-04-01");
		list.add("2015-04-02");
		calendar.addMarks(list, 0);

		//监听所选中的日期
		calendar.setOnCalendarClickListener(new OnCalendarClickListener() {

			public void onCalendarClick(int row, int col, String dateFormat) {
				int month = Integer.parseInt(dateFormat.substring(
						dateFormat.indexOf("-") + 1,
						dateFormat.lastIndexOf("-")));
				
				if (calendar.getCalendarMonth() - month == 1//跨年跳转
						|| calendar.getCalendarMonth() - month == -11) {
					calendar.lastMonth();
					
				} else if (month - calendar.getCalendarMonth() == 1 //跨年跳转
						|| month - calendar.getCalendarMonth() == -11) {
					calendar.nextMonth();
					
				} else {
					calendar.removeAllBgColor(); 
					calendar.setCalendarDayBgColor(dateFormat,
							R.drawable.calendar_date_focused);
					date = dateFormat;//最后返回给全局 date
				}
			}
		});

		//监听当前月份
		calendar.setOnCalendarDateChangedListener(new OnCalendarDateChangedListener() {
			public void onCalendarDateChanged(int year, int month) {
				popupwindow_calendar_month
						.setText(year + "年" + month + "月");
			}
		});
		
		//上月监听按钮
		RelativeLayout popupwindow_calendar_last_month = (RelativeLayout) view
				.findViewById(R.id.popupwindow_calendar_last_month);
		popupwindow_calendar_last_month
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						calendar.lastMonth();
					}

				});
		
		//下月监听按钮
		RelativeLayout popupwindow_calendar_next_month = (RelativeLayout) view
				.findViewById(R.id.popupwindow_calendar_next_month);
		popupwindow_calendar_next_month
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						calendar.nextMonth();
					}
				});
		
		//关闭窗口
		popupwindow_calendar_bt_enter
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						dismiss();
						if(monPopupWindowCalendarActionListener!=null)
							monPopupWindowCalendarActionListener.onSure(date);
					}
				});
		popupwindow_calendar_bt_quit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
			
		});
		
	}
	public void showPopup(){

		showAtLocation(mParent, Gravity.BOTTOM, 0, -30);
		update();
	}
	
	public interface OnPopupWindowCalendarActionListener{
		public void onSure(String date);
	}
}
