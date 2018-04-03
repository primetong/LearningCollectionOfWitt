package com.evguard.customview;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.evguard.customview.TimeWheel.AbstractWheelTextAdapter;
import com.evguard.customview.TimeWheel.OnWheelScrollListener;
import com.evguard.customview.TimeWheel.WheelView;
import com.xinghaicom.evguard.R;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class TimeWheelView extends PopupWindow {
	private Context mContext;
	private View mParent;
	private WheelView YearView, MonthView, DayView ;
	private Button buttonok, buttoncancle;
	private TextView tv_content;

	private String[] years, months, days;
	private List<String> YearList, MonthList, DayList;
	private SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
	private SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
	private SimpleDateFormat dayFormat = new SimpleDateFormat("dd");

	private YearAdapter YearAdapter;
	private MonthAdapter MonthAdapter;
	private DayAdapter DayAdapter;

	private final int MIN_YEAR = 1866;
	private int max_Year;

	private int YearIndex, MonthIndex, DayIndex;
	private Resources res;
	private OnDateSelectedListener mOnDateSelectedListener = null;

	private Calendar mCalendar = null;
	protected Toast mToast = null;

	public TimeWheelView(Context context, View parent,
			OnDateSelectedListener aOnDateSelectedListener) {
		super(context);// android2.3中必须使用有参构造函数。
		mContext = context;
		mParent = parent;
		mOnDateSelectedListener = aOnDateSelectedListener;
		mCalendar = Calendar.getInstance();
		View view = LayoutInflater.from(mContext).inflate(R.layout.wheel_view,
				null);
		view.startAnimation(AnimationUtils.loadAnimation(mContext,
				R.anim.fade_in));
		setContentView(view);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);

		setBackgroundDrawable(null);
		setFocusable(true);
		setOutsideTouchable(true);

		YearView = (WheelView) view.findViewById(R.id.year);
		MonthView = (WheelView) view.findViewById(R.id.month);
		DayView = (WheelView) view.findViewById(R.id.day);
		buttonok = (Button) view.findViewById(R.id.done);
		buttoncancle = (Button) view.findViewById(R.id.cancle);
		tv_content = (TextView) view.findViewById(R.id.whell_view_textview);

		// 设置年月日循环出现
		YearView.setCyclic(true);
		MonthView.setCyclic(true);
		DayView.setCyclic(true);

		buttoncancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		buttonok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mOnDateSelectedListener == null)
					return;
				int year = Integer.parseInt((String) YearAdapter
						.getItemText(YearIndex));
				int month = Integer.parseInt((String) MonthAdapter
						.getItemText(MonthIndex));
				int day = Integer.parseInt((String) DayAdapter
						.getItemText(DayIndex));
				Calendar calendar = Calendar.getInstance();
				calendar.set(year, month - 1, day);
				SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 ");
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				if (calendar.after(mCalendar)) {
					showToast("时间不能大于当前时间");
					return;
				}
				mOnDateSelectedListener.onDateSelected(getDate());
				dismiss();
			}
		});

		res = mContext.getResources();
		months = res.getStringArray(R.array.months);
		days = res.getStringArray(R.array.days_31);
		// final Date date = new Date();
		// String year = yearFormat.format(date);
		// max_Year=Integer.parseInt(year);
		max_Year = mCalendar.get(Calendar.YEAR);
		// 设置年份集合
		YearList = new ArrayList<String>();
		for (int i = MIN_YEAR; i <= max_Year; i++) {
			YearList.add(i + "");
		}
		// 设置月份、天数集合
		MonthList = Arrays.asList(months);
		DayList = Arrays.asList(days);

		YearAdapter = new YearAdapter(mContext, YearList);
		MonthAdapter = new MonthAdapter(mContext, MonthList);
		DayAdapter = new DayAdapter(mContext, DayList);
		YearView.setViewAdapter(YearAdapter);
		MonthView.setViewAdapter(MonthAdapter);
		DayView.setViewAdapter(DayAdapter);
		YearView.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				YearIndex = wheel.getCurrentItem();
				String year = (String) YearAdapter.getItemText(YearIndex);
				String month = (String) MonthAdapter.getItemText(MonthIndex);
				if (Integer.parseInt(month) == 2) {
					if (isLeapYear(year)) {
						// 29 闰年2月29天
						if (DayAdapter.list.size() != 29) {
							DayList = Arrays.asList(res
									.getStringArray(R.array.days_29));
							DayAdapter = new DayAdapter(mContext, DayList);
							DayView.setViewAdapter(DayAdapter);
							if (DayIndex > 28) {
								DayView.setCurrentItem(0);
								DayIndex = 0;
							} else {
								DayView.setCurrentItem(DayIndex);
							}
						}
					} else {
						// 28 非闰年2月28天
						if (DayAdapter.list.size() != 28) {
							DayList = Arrays.asList(res
									.getStringArray(R.array.days_28));
							DayAdapter = new DayAdapter(mContext, DayList);
							DayView.setViewAdapter(DayAdapter);
							if (DayIndex > 27) {
								DayView.setCurrentItem(0);
								DayIndex = 0;
							} else {
								DayView.setCurrentItem(DayIndex);
							}
						}
					}
				}
				tv_content.setText(getDate());
			}
		});

		MonthView.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				MonthIndex = wheel.getCurrentItem();
				String year = (String) YearAdapter.getItemText(YearIndex);
				String month = (String) MonthAdapter.getItemText(MonthIndex);
				int i = Integer.parseInt(month);
				if (i == 1 || i == 3 || i == 5 || i == 7 || i == 8 || i == 10
						|| i == 12) {
					// 31
					if (DayAdapter.list.size() != 31) {
						DayList = Arrays.asList(res
								.getStringArray(R.array.days_31));
						DayAdapter = new DayAdapter(mContext, DayList);
						DayView.setViewAdapter(DayAdapter);
						DayView.setCurrentItem(DayIndex);
					}
				} else if (i == 2) {
					if (isLeapYear(year)) {
						// 29 为闰年2月
						if (DayAdapter.list.size() != 29) {
							DayList = Arrays.asList(res
									.getStringArray(R.array.days_29));
							DayAdapter = new DayAdapter(mContext, DayList);
							DayView.setViewAdapter(DayAdapter);
							if (DayIndex > 28) {
								DayView.setCurrentItem(0);
								DayIndex = 0;
							} else {
								DayView.setCurrentItem(DayIndex);
							}
						}
					} else {
						// 28 为平年2月
						if (DayAdapter.list.size() != 28) {
							DayList = Arrays.asList(res
									.getStringArray(R.array.days_28));
							DayAdapter = new DayAdapter(mContext, DayList);
							DayView.setViewAdapter(DayAdapter);
							if (DayIndex > 27) {
								DayView.setCurrentItem(0);
								DayIndex = 0;
							} else {
								DayView.setCurrentItem(DayIndex);
							}
						}
					}
				} else {
					// 30
					if (DayAdapter.list.size() != 30) {
						DayList = Arrays.asList(res
								.getStringArray(R.array.days_30));
						DayAdapter = new DayAdapter(mContext, DayList);
						DayView.setViewAdapter(DayAdapter);
						if (DayIndex > 29) {
							DayView.setCurrentItem(0);
							DayIndex = 0;
						} else {
							DayView.setCurrentItem(DayIndex);
						}
					}
				}
				tv_content.setText(getDate());
			}
		});
		DayView.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				DayIndex = wheel.getCurrentItem();
				tv_content.setText(getDate());
			}
		});
		// 设置年月日初始值
		YearIndex = YearList.size() - 1;
		MonthIndex = mCalendar.get(Calendar.MONTH);// java月份从0-11
		DayIndex = mCalendar.get(Calendar.DAY_OF_MONTH) - 1;
		YearView.setCurrentItem(YearIndex);
		MonthView.setCurrentItem(MonthIndex);
		DayView.setCurrentItem(DayIndex);
		tv_content.setText(getDate());

	}

	private String getDate() {
		String year = (String) YearAdapter.getItemText(YearIndex);
		String month = (String) MonthAdapter.getItemText(MonthIndex);
		String day = (String) DayAdapter.getItemText(DayIndex);
		return year + " 年 " + month + " 月 " + day + " 日 ";
	}

	protected void showToast(String txt) {
		if (mToast == null)
			mToast = Toast.makeText(mContext, txt, Toast.LENGTH_LONG);
		mToast.setText(txt);
		mToast.show();
	}

	public void showPopup() {

		showAtLocation(mParent, Gravity.BOTTOM, 0, -30);
		update();
	}

	/**
	 * 判断是否是闰年
	 * */
	public static boolean isLeapYear(String str) {
		int year = Integer.parseInt(str);
		return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
	}

	private class YearAdapter extends AbstractWheelTextAdapter {
		/**
		 * Constructor
		 */
		public List<String> list;

		protected YearAdapter(Context context, List<String> list) {
			super(context, R.layout.wheel_view_layout, NO_RESOURCE);
			this.list = list;
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);

			TextView textCity = (TextView) view.findViewById(R.id.textView);
			textCity.setText(list.get(index));
			return view;
		}

		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return list.get(index);
		}
	}

	private class MonthAdapter extends AbstractWheelTextAdapter {
		/**
		 * Constructor
		 */
		public List<String> list;

		protected MonthAdapter(Context context, List<String> list) {
			super(context, R.layout.wheel_view_layout, NO_RESOURCE);
			this.list = list;
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);

			TextView textCity = (TextView) view.findViewById(R.id.textView);
			textCity.setText(list.get(index));
			return view;
		}

		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return list.get(index);
		}
	}

	private class DayAdapter extends AbstractWheelTextAdapter {
		/**
		 * Constructor
		 */
		public List<String> list;

		protected DayAdapter(Context context, List<String> list) {
			super(context, R.layout.wheel_view_layout, NO_RESOURCE);
			this.list = list;
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);

			TextView textCity = (TextView) view.findViewById(R.id.textView);
			textCity.setText(list.get(index));
			return view;
		}

		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return list.get(index);
		}
	}

	public interface OnDateSelectedListener {
		public void onDateSelected(String date);
	}
}
