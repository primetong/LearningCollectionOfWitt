package com.evguard.main;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.evguard.customview.AppTitleBar;
import com.evguard.customview.AppTitleBar.OnTitleActionClickListener;
import com.evguard.main.Fragment_Alam.CallBack_FragmentAlam;
import com.evguard.main.Fragment_Message.CallBack_FragmentMessage;
import com.evguard.model.MessageInfo;
import com.evguard.tools.DBHelper;
import com.xinghaicom.evguard.R;

public class AC_Message extends AC_BaseLogined {
	private AppTitleBar mTitleBar;
	private Dg_OptionSelect mDg_OptionSelect;
	private RadioGroup rgTitle;
	private RadioButton btMessage;
	private RadioButton btAlam;
	private ViewPager vpContent;
	private FragmentActivity mActivity;
	private ArrayList<MessageInfo> mMessageList;
	private DBHelper mDBHelper = null;
	private static final int MESSAGE_GETTING = 0;
	private static final int MESSAGE_GETTING_OK = 1;
	private static final int MESSAGE_GETTING_FAILED = 2;
	private static final int MESSAGE_GETTING_EXCEPTION = 3;
	private FragmentManager manager;
	private Fragment_Base mCurFragment;
	private Fragment_Message aFragment_Message;
	private Fragment_Alam aFragment_Alam;

	@Override
	public void onCreate(Bundle savedInstancestate) {
		try {
			super.onCreate(savedInstancestate);
			setContentView(R.layout.ac_message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mActivity = this;
		mDBHelper = new DBHelper(mContext);
//		// 初始化数据库
		mDBHelper.initDatabase();
		if(mMessageList == null) mMessageList = new ArrayList<MessageInfo>();
		findViews();
		initViews();
		getMessageList();
		initPage();
	}

	private void findViews() {
		mTitleBar = (AppTitleBar) findViewById(R.id.title_bar);
		rgTitle = (RadioGroup) findViewById(R.id.rg_title);
		btMessage = (RadioButton) findViewById(R.id.bt_message);
		btAlam = (RadioButton) findViewById(R.id.bt_alam);
	}

	@Override
	protected void onResume() {

		super.onResume();
	}

	private void initViews() {
//		mTitleBar.setTitleMode(AppTitleBar.APPTITLEBARMODE_TXTANDBACK,
//				null, null);
		mTitleBar.setTitleMode(null,
				mContext.getResources().getDrawable(R.drawable.icon_back),
				null,false,
				null,null);
		mTitleBar
				.setOnTitleActionClickListener(new OnTitleActionClickListener() {
					@Override
					public void onRightOperateClick() {

					}

					@Override
					public void onTitleClick() {

					}

					@Override
					public void onLeftOperateClick() {
						AC_Message.this.finish();
					}
				});
	}

	
	/**
	 * 获取消息数据
	 */
	private void getMessageList() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		mMessageList = bundle.getParcelableArrayList("MessageList");
	}
	
	
	private void initFragment() {
		aFragment_Message = Fragment_Message.getInstance();
		Bundle bundle1 = new Bundle();
		bundle1.putString("flag", "1");
		bundle1.putParcelableArrayList("MessageList", mMessageList);
		aFragment_Message.setArguments(bundle1);
		aFragment_Message.setCallBack_FragmentEnergy(new CallBack_FragmentMessage() {
			@Override
			public void changeMessageStatus(int ID) {
				mDBHelper.updateStatus(ID);
			}

			@Override
			public void deleteMessage(int ID) {
				mDBHelper.deleteOneMessage(ID);
				
			}
		});
		aFragment_Alam = Fragment_Alam.getInstance();
		Bundle bundle2 = new Bundle();
		bundle2.putString("flag", "2");
		bundle2.putParcelableArrayList("MessageList", mMessageList);
		aFragment_Alam.setArguments(bundle2);
		aFragment_Alam.setCallBack_FragmentAlam(new CallBack_FragmentAlam() {
			
			@Override
			public void changeMessageStatus(int ID) {
				mDBHelper.updateStatus(ID);
			}

			@Override
			public void deleteMessage(int ID) {
				mDBHelper.deleteOneMessage(ID);
			}
		});
		
		manager = this.getSupportFragmentManager();
		manager.beginTransaction().add(R.id.fl_content, aFragment_Message)
				.commit();
	
		mCurFragment = aFragment_Message;
	}

	private void initPage() {
		
		initFragment();
		rgTitle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == btMessage.getId()) {
					rgTitle.setBackgroundResource(R.drawable.bg_message_title_p);
					queryDB();
					showFragment(aFragment_Message);
				} else if (checkedId == btAlam.getId()) {
					rgTitle.setBackgroundResource(R.drawable.bg_alam_title_p);
					queryDB();
					showFragment(aFragment_Alam);
				}
			}
		});
	}
	
	
	private void showFragment(Fragment_Base ment) {
		if (ment == mCurFragment)
			return;
		manager.beginTransaction().detach(mCurFragment)
				.commitAllowingStateLoss();
		if (ment.isDetached()) {
			manager.beginTransaction().attach(ment).commitAllowingStateLoss();
		} else if (!ment.isAdded()) {
			manager.beginTransaction().add(R.id.fl_content, ment)
					.commitAllowingStateLoss();
		}
		mCurFragment = ment;
	}

	@Override
	protected void handleMsg(Message msg) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 读取本地数据库
	 * 
	 */
	private void queryDB() {
		mMessageList.clear();
		// listTag.clear();
		Cursor cursor = mDBHelper.query();
		if (cursor == null || !cursor.moveToFirst() || cursor.getInt(0) <= 0) {
//			startDate = mSimpleDateFormat1.format(new Date());
//			dateTime = startDate + "$" + "00!00!00";
//			maxID = "0";
		} else {
			// 获取本地数据库最新一条记录的时间和ID
//			if (cursor.moveToFirst()) {
//				int ID = cursor.getInt(cursor.getColumnIndex("ID"));
//				String msgDate = cursor.getString(cursor
//						.getColumnIndex("msgDate"));
//				String CreateTime = cursor.getString(
//						cursor.getColumnIndex("CreateTime")).replace("/", "-");
//				// System.out.println("CreateTime1 =" + CreateTime);
//				maxID = String.valueOf(ID);
//				Date currentTime = null;
//				try {
//					Date beginTime = mSimpleDateFormat.parse(CreateTime);
//					currentTime = mSimpleDateFormat.parse(mSimpleDateFormat
//							.format(new Date()));
//					long spanTime = currentTime.getTime() - beginTime.getTime();
//					System.out.println("spanTime =" + spanTime);
//					if (spanTime < 0)
//						return;
//					// 当前时间与最新记录的时间之差：小于三天则取本地数据库最新记录的时间；否则取当前时间
//					if (spanTime <= EndTOBegin) {
//						String hour = String.valueOf(beginTime.getHours());
//						String min = String.valueOf(beginTime.getMinutes());
//						String sec = String.valueOf(beginTime.getSeconds());
//						dateTime = msgDate + "$" + hour + "!" + min + "!" + sec;
//					} else {
//						dateTime = mSimpleDateFormat1.format(new Date()) + "$"
//								+ "00!00!00";
//						maxID = "0";
//					}
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
//			}
			// 将本地数据库的数据放入缓存
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
//				System.out
//						.println("CreateTime2 ="
//								+ cursor.getString(cursor
//										.getColumnIndex("CreateTime")));
				int ID = cursor.getInt(cursor.getColumnIndex("ID"));
//				String msgDate = cursor.getString(cursor
//						.getColumnIndex("msgDate"));
				MessageInfo messaage = new MessageInfo();
				messaage.setID(cursor.getInt(cursor
						.getColumnIndex("ID")));
				messaage.setContent(cursor.getString(cursor
						.getColumnIndex("Content")));
				messaage.setMsgType(cursor.getString(cursor
						.getColumnIndex("MsgType")));
				messaage.setTime(cursor.getString(cursor
						.getColumnIndex("Time")));
				messaage.setTitle(cursor.getString(cursor
						.getColumnIndex("Title")));
				messaage.setIsMsgReaded(cursor.getString(cursor.getColumnIndex("IsMsgReaded")));
				// messaage.setDate(msgDate);
				mMessageList.add(messaage);
				
			}
			Log.i("mMessageList", "mMessageList.size--" + mMessageList.size());
		}
		cursor.close();
	}
}
