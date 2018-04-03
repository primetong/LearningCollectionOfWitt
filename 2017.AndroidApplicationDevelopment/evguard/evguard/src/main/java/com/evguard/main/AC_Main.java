package com.evguard.main;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;

import com.evguard.customview.AppTitleBar;
import com.evguard.customview.AppTitleBar.OnTitleActionClickListener;
import com.evguard.data.AppDataCache;
import com.evguard.main.Fragment_Menu_Bottom.OnMenuActionClickListener;
import com.evguard.model.ICommonWebResponse;
import com.evguard.model.MessageInfo;
import com.evguard.model.WebReq_GetNotice;
import com.evguard.model.WebRes_GetNotice;
import com.evguard.tools.DBHelper;
import com.xinghaicom.evguard.R;

public class AC_Main extends AC_CheckUpdate {

	private DrawerLayout mDrawerLayout;
	private Fragment_Menu_Bottom aFragment_Menu = null;
	private Fragment_StartPage aFragment_StartPage = null;
	private Fragment_Menu_Left aFFragment_Menu_Left = null;
	private Fragment_Track aFragment_Track;
	private Fragment_Energy aFragment_Energy;
	private Fragment_Battery aFragment_Battery;
	private Fragment_Diagnose aFragment_Diagnose;
	private Fragment mCurFragment = null;
	private FragmentManager manager;
	private AppTitleBar mtitlebar = null;

	private Dg_Waiting mDownDialog = null;
	boolean isDrawerShow = false;
	private static final int MESSAGE_GETCARLIST = 0;
	private static final int MESSAGE_GETCARLISTOK = 1;
	private static final int MSG_READED = 2;
	private static final int MSG_UNREAD = 3;
	public String carNum;
	public String heartInterval;
	public String aboutUrl;
	public Typeface typeFace_ARIAL;
	private Thread mCheckThread;
	DBHelper mDBHelper;
	private static final int MESSAGE_GETTING = 11;
	private static final int MESSAGE_GETTING_OK = 12;
	private static final int MESSAGE_GETTING_FAILED = 14;
	private static final int MESSAGE_GETTING_EXCEPTION = 15;
	private ArrayList<MessageInfo> mMessageList;

	@Override
	public void onCreate(Bundle savedInstancestate) {
		try {
			super.onCreate(savedInstancestate);
			getWindow().setFormat(PixelFormat.TRANSLUCENT);
			setContentView(R.layout.ac_main);
			isSilentCheck=true;
			checkVersion();
			getExtras();
			findView();
			initMain();
			mDBHelper = new DBHelper(mContext);
			mDBHelper.initDatabase();
			mMessageList = new ArrayList<MessageInfo>();
			getMessageList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle saveInstance) {
		saveInstance.putString("curfargment", mCurFragment.getClass().getSimpleName());
		saveInstance.putString("curmenuactiontag", aFragment_Menu.getCurBtnTag());
		super.onSaveInstanceState(saveInstance);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle saveInstance) {
		super.onRestoreInstanceState(saveInstance);

		restoreFragment();
		loadFragmentListener();
		
		
		String curfragment = saveInstance.getString("curfargment");
		System.out.println("curfragment== " + curfragment);
		String curmenutag=saveInstance.getString("curmenuactiontag");
		aFragment_Menu.setCurBtnTag(curmenutag);
		aFragment_Menu.setIsCanClick(true);
		
		resetFragment(curfragment);
//		setHead();
		
	}
	
	private void getExtras() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			carNum = bundle.getString("CarNum");
			heartInterval = bundle.getString("HeartInterval");
			aboutUrl = bundle.getString("AboutUrl");
		}
	}

	private void initMain() {
		manager = this.getSupportFragmentManager();
		initDrawerLayout();
		if (bIsRestore)
			return;
		initFragment();
		setDefaultFragment();
		initThread();
		loadFragmentListener();
	}

	@Override
	public void onResume() {
		checkMessageStatus();
		super.onResume();
	}

	public void onPause() {
		if (mCheckThread != null) {
			mCheckThread.interrupt();
			mCheckThread = null;
		}
		super.onPause();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			confirExitApp(false);
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void findView() {
		mtitlebar = (AppTitleBar) findViewById(R.id.titlebar);
		mtitlebar.setHeadDrawable(mContext.getResources().getDrawable(
				R.drawable.icon_left_menu));
		aFFragment_Menu_Left = (Fragment_Menu_Left) this
				.getSupportFragmentManager()
				.findFragmentById(R.id.id_left_menu);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerLayout);
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
				Gravity.LEFT);
		mDrawerLayout.setDrawerShadow(R.drawable.bg_drawer_shadow,
				GravityCompat.START);
	}

	/**
	 * 退出app
	 */
	public void confirExitApp(final boolean isUnlogin) {
		if (isUnlogin) {
			AlertDialog.Builder alertDlgBuilder = new AlertDialog.Builder(this);
			alertDlgBuilder.setTitle(getResources().getString(R.string.exit));
			alertDlgBuilder.setMessage(getResources().getString(
					R.string.confirm_exit_to_login));
			alertDlgBuilder.setPositiveButton(
					getResources().getString(R.string.yes),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							mSettings.setIsUserQiutApp(true);
							Intent tExitapp = new Intent(
									"com.evguard.activity.login");
							tExitapp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							tExitapp.addCategory(Intent.CATEGORY_DEFAULT);
							startActivity(tExitapp);
							AC_Main.this.finish();

						}
					});
			alertDlgBuilder.setNegativeButton(
					getResources().getString(R.string.no), null);
			alertDlgBuilder.show();
			return;
		}
		AlertDialog.Builder alertDlgBuilder = new AlertDialog.Builder(this);
		alertDlgBuilder.setTitle(getResources().getString(R.string.exit));
		alertDlgBuilder.setMessage(getResources().getString(
				R.string.confirm_exit));
		alertDlgBuilder.setPositiveButton(getResources()
				.getString(R.string.yes),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent tExitapp = new Intent(
								"com.evguard.activity.exit");
						tExitapp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						tExitapp.addCategory(Intent.CATEGORY_DEFAULT);
						startActivity(tExitapp);
						AC_Main.this.finish();

					}
				});
		alertDlgBuilder.setNegativeButton(
				getResources().getString(R.string.no), null);
		alertDlgBuilder.show();
	}

	private void initDrawerLayout() {

		mDrawerLayout.setDrawerListener(new DrawerListener() {
			@Override
			public void onDrawerStateChanged(int newState) {
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				// View mContent = mDrawerLayout.getChildAt(0);
				// View mMenu = drawerView;
				// float scale = 1 - slideOffset;
				// if (drawerView.getTag().equals("LEFT"))
				// {
				// // float leftScale = 1 - 0.3f * scale;
				// // ViewHelper.setScaleX(mMenu, leftScale);
				// // ViewHelper.setScaleY(mMenu, leftScale);
				// // ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
				// ViewHelper.setTranslationX(mContent,
				// mMenu.getMeasuredWidth() * (1 - scale));
				// ViewHelper.setPivotX(mContent, 0);
				// ViewHelper.setPivotY(mContent,
				// mContent.getMeasuredHeight() / 2);
				// mContent.invalidate();
				// // ViewHelper.setScaleX(mContent, rightScale);
				// // ViewHelper.setScaleY(mContent, rightScale);
				// } else
				// {
				// ViewHelper.setTranslationX(mContent,
				// -mMenu.getMeasuredWidth() * slideOffset);
				// ViewHelper.setPivotX(mContent, mContent.getMeasuredWidth());
				// ViewHelper.setPivotY(mContent,
				// mContent.getMeasuredHeight() / 2);
				// mContent.invalidate();
				// // ViewHelper.setScaleX(mContent, rightScale);
				// // ViewHelper.setScaleY(mContent, rightScale);
				// }

			}

			@Override
			public void onDrawerOpened(View drawerView) {
				isDrawerShow = true;
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				isDrawerShow = false;
			}
		});
	}

	@Override
	public void handleMsg(Message msg) {
		Log.i(TAG, "msg.what" + msg.what);
		switch (msg.what) {
		case MESSAGE_GETCARLIST:
			if (mDownDialog != null)
				mDownDialog.dismiss();
			mDownDialog = Dg_Waiting.newInstance("获取信息", "获取信息，请稍候...");
			mDownDialog.setCancelable(false);
			mDownDialog.show(getSupportFragmentManager(), "");
			break;

		case MESSAGE_GETCARLISTOK:
			if (msg.obj != null && msg.obj instanceof String) {
				showToast((String) msg.obj);
			}
			if (mDownDialog != null)
				mDownDialog.dismiss();
			break;

		case MSG_READED:
			mtitlebar.setOperateDrawable(mContext.getResources().getDrawable(
					R.drawable.icon_message_new));
			break;
		case MSG_UNREAD:
			mtitlebar.setOperateDrawable(mContext.getResources().getDrawable(
					R.drawable.icon_message));
			break;
		case MESSAGE_GETTING:
			break;
		case MESSAGE_GETTING_OK:
			List<MessageInfo> message = (List<MessageInfo>) msg.obj;
			Log.i("msg", "msg.size==" + message.size());
			if (message.size() > 0) {
				for (int i = 0; i < message.size(); i++) {
					
					// 将新记录添加到数据源最上方
					mMessageList.add(i, message.get(i));
					// 数据存入本地数据
					mDBHelper.addMessage(message.get(i));
				}
			}
			break;
		case MESSAGE_GETTING_FAILED:
			break;
		case MESSAGE_GETTING_EXCEPTION:
			break;
		default:
			break;
		}
	}

	

	private void initFragment() {
		aFragment_Menu = Fragment_Menu_Bottom.getInstance();
		aFragment_StartPage = Fragment_StartPage.getInstance();
		aFragment_Track = Fragment_Track.getInstance();
		aFragment_Energy = Fragment_Energy.getInstance();
		aFragment_Battery = Fragment_Battery.getInstance();
		aFragment_Diagnose = Fragment_Diagnose.getInstance();

		
		manager.beginTransaction().replace(R.id.ll_menu, aFragment_Menu, 
				aFragment_Menu.getClass().getSimpleName())
				.commit();

		
	}
	
	private void setDefaultFragment() {
		manager.beginTransaction()
				.add(R.id.ll_content, aFragment_StartPage, 
						aFragment_StartPage.getClass().getSimpleName())
				.commit();
		mCurFragment = aFragment_StartPage;
		aFragment_Menu.locationPerClick();
		setStartPageTitle();
		aFragment_Menu.setIsCanClick(true);
	}
	
	private void restoreFragment() {

		aFragment_Menu = Fragment_Menu_Bottom.getInstance();
		aFragment_StartPage = Fragment_StartPage.getInstance();
		aFragment_Track = Fragment_Track.getInstance();
		aFragment_Energy = Fragment_Energy.getInstance();
		aFragment_Battery = Fragment_Battery.getInstance();
		aFragment_Diagnose = Fragment_Diagnose.getInstance();
		
		if (manager.findFragmentByTag(aFragment_Menu.getClass().getSimpleName()) != null) {
			aFragment_Menu = (Fragment_Menu_Bottom) manager.findFragmentByTag(aFragment_Menu.getClass().getSimpleName());
		}
		if (manager.findFragmentByTag(aFragment_StartPage.getClass().getSimpleName()) != null) {
			aFragment_StartPage = (Fragment_StartPage) manager.findFragmentByTag(aFragment_StartPage.getClass().getSimpleName());
		}
		if (manager.findFragmentByTag(aFragment_Track.getClass().getSimpleName()) != null) {
			aFragment_Track = (Fragment_Track) manager.findFragmentByTag(aFragment_Track.getClass().getSimpleName());
		}
		if (manager.findFragmentByTag(aFragment_Energy.getClass().getSimpleName()) != null) {
			aFragment_Energy = (Fragment_Energy) manager.findFragmentByTag(aFragment_Energy.getClass().getSimpleName());
		}
		if (manager.findFragmentByTag(aFragment_Battery.getClass().getSimpleName()) != null) {
			aFragment_Battery = (Fragment_Battery) manager.findFragmentByTag(aFragment_Battery.getClass().getSimpleName());
		}
		if (manager.findFragmentByTag(aFragment_Diagnose.getClass().getSimpleName()) != null) {
			aFragment_Diagnose = (Fragment_Diagnose) manager.findFragmentByTag(aFragment_Diagnose.getClass().getSimpleName());
		}
	}
	
	private void resetFragment(String aFragmentName) {
		if(mCurFragment == null) System.out.println("mCurFragment == null");
		else System.out.println("mCurFragment== " + mCurFragment);
		if (aFragment_StartPage != null
				&& aFragmentName.equals(aFragment_StartPage.getClass()
						.getSimpleName())) {
			setStartPageTitle();
			mCurFragment = aFragment_StartPage;
			
			
			manager.beginTransaction().show(aFragment_StartPage).commit();
			hidenFragment(aFragment_Track);
			hidenFragment(aFragment_Energy);
			hidenFragment(aFragment_Battery);
			hidenFragment(aFragment_Diagnose);
			return;
		}
		if (aFragment_Track != null
				&& aFragmentName.equals(aFragment_Track.getClass().getSimpleName())) {
			setStartPageTitle();
			mCurFragment = aFragment_Track;
			manager.beginTransaction().show(aFragment_Track).commit();
			hidenFragment(aFragment_StartPage);
			hidenFragment(aFragment_Energy);
			hidenFragment(aFragment_Battery);
			hidenFragment(aFragment_Diagnose);
			return;
		}
		if (aFragment_Energy != null
				&& aFragmentName.equals(aFragment_Energy.getClass().getSimpleName())) {
			setStartPageTitle();
			mCurFragment = aFragment_Energy;
			manager.beginTransaction().show(aFragment_Energy).commit();
			hidenFragment(aFragment_StartPage);  
			hidenFragment(aFragment_Track);
			hidenFragment(aFragment_Battery);
			hidenFragment(aFragment_Diagnose);
			return;
		}
		if (aFragment_Battery != null
				&& aFragmentName.equals(aFragment_Battery.getClass().getSimpleName())) {
			setStartPageTitle();
			mCurFragment = aFragment_Battery;
			manager.beginTransaction().show(aFragment_Battery).commit();
			hidenFragment(aFragment_StartPage);  
			hidenFragment(aFragment_Track);
			hidenFragment(aFragment_Energy);
			hidenFragment(aFragment_Diagnose);
			return;
		}
		if (aFragment_Diagnose != null
				&& aFragmentName.equals(aFragment_Diagnose.getClass().getSimpleName())) {
			setStartPageTitle();
			mCurFragment = aFragment_Diagnose;
			manager.beginTransaction().show(aFragment_Diagnose).commit();
			hidenFragment(aFragment_StartPage);  
			hidenFragment(aFragment_Track);
			hidenFragment(aFragment_Energy);
			hidenFragment(aFragment_Battery);
			return;
		}
		
	}

	private void hidenFragment(Fragment ment) {

		if (ment.isAdded()) {
			Fragment f = manager.findFragmentByTag(ment.getClass()
					.getSimpleName());
			manager.beginTransaction().hide(f).commitAllowingStateLoss();
		}
	}
	
	private void showFragment(Fragment ment) {
		System.out.println("11-mCurFragment== " + mCurFragment +" , ment== " + ment);
		if (isDrawerShow) {
			mDrawerLayout.closeDrawer(Gravity.LEFT);
		}
		
		if (mCurFragment == null) {
			manager.beginTransaction()
					.add(R.id.ll_content, ment, ment.getClass().getSimpleName())
					.commitAllowingStateLoss();
			mCurFragment = ment;
			return;
		}
		if (ment.isAdded()) {
			mCurFragment = manager.findFragmentByTag(mCurFragment.getClass()
					.getSimpleName());
			manager.beginTransaction().hide(mCurFragment)
					.commitAllowingStateLoss();
			Fragment f = manager.findFragmentByTag(ment.getClass()
					.getSimpleName());
			manager.beginTransaction().show(f).commit();
			mCurFragment = f;
			
		} else {
			mCurFragment = manager.findFragmentByTag(mCurFragment.getClass()
					.getSimpleName());
			manager.beginTransaction().hide(mCurFragment)
					.commitAllowingStateLoss();
			manager.beginTransaction()
					.add(R.id.ll_content, ment, ment.getClass().getSimpleName())
					.commitAllowingStateLoss();
			mCurFragment = ment;
		}
	}

	private void loadFragmentListener() {
		aFragment_Menu
				.setOnMenuActionClickListener(new OnMenuActionClickListener() {

					@Override
					public void onAction1() {
						showFragment(aFragment_StartPage);
					}

					@Override
					public void onAction2() {
						showFragment(aFragment_Battery);

					}

					@Override
					public void onAction3() {
						showFragment(aFragment_Energy);
					}

					@Override
					public void onAction4() {
						showFragment(aFragment_Track);
					}

					@Override
					public void onAction5() {
						showFragment(aFragment_Diagnose);

					}
				});

	}

	private void setStartPageTitle() {
//		mtitlebar.setTitleMode(
//				AppTitleBar.APPTITLEBARMODE_IMGANDBACKANDOPERATE, "首页",
//				mContext.getResources().getDrawable(R.drawable.icon_message_new));
		mtitlebar.setTitleMode(null,
				mContext.getResources().getDrawable(R.drawable.icon_left_menu),
				null,true,
				null,mContext.getResources().getDrawable(R.drawable.icon_message_new));
		mtitlebar
				.setOnTitleActionClickListener(new OnTitleActionClickListener() {

					@Override
					public void onLeftOperateClick() {
						mDrawerLayout.openDrawer(Gravity.LEFT);
						mSettings.setIsHasNewAlarm(true);
					}

					@Override
					public void onRightOperateClick() {
						queryDB();
						final Bundle bundle = new Bundle();
						bundle.putParcelableArrayList("MessageList",
								mMessageList);
						Intent t = new Intent("com.evguard.activity.message");
						t.putExtras(bundle);
						startActivity(t);
						// mtitlebar.setOperateDrawable(mContext.getResources()
						// .getDrawable(R.drawable.icon_message));// 模拟报警消息推送

					}

					@Override
					public void onTitleClick() {
						
					}
				});
	}


	private void checkMessageStatus() {
		mCheckThread = new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Cursor cursor = mDBHelper.queryUnReadMessage();
				if (cursor == null || !cursor.moveToFirst()) {
					Message msg = mHandler.obtainMessage(MSG_READED);
					mHandler.sendMessage(msg);

				} else {
					Message msg = mHandler.obtainMessage(MSG_UNREAD);
					mHandler.sendMessage(msg);

				}

				Cursor cursor1 = mDBHelper.query();
				if (cursor1 != null && cursor1.moveToFirst()) {
					int ID = cursor1.getInt(cursor.getColumnIndex("ID")) + 1;
					AppDataCache.getInstance().setMessageId(ID);
					cursor.close();
				}
				if (cursor != null) {
					cursor.close();
				}
			}
		};
		
		mCheckThread.start();
	}

	private void initThread() {
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 获取消息数据
	 */
	private void getMessageList() {
		mMessageList.clear();
		Message endMsg = mHandler.obtainMessage(MESSAGE_GETTING);
		mHandler.sendMessage(endMsg);
		String serverTime = AppDataCache.getInstance().getServerTime();
		WebReq_GetNotice aWebReq_GetNotice = null;
		if (serverTime == null || serverTime.equals("")) {
			aWebReq_GetNotice = new WebReq_GetNotice("");
		} else {
			aWebReq_GetNotice = new WebReq_GetNotice(serverTime);
		}
		// web请求结果回调
		ICommonWebResponse<WebRes_GetNotice> aICommonWebResponse = new ICommonWebResponse<WebRes_GetNotice>() {
			@Override
			public void WebRequstSucess(WebRes_GetNotice aWebRes) {
				if (aWebRes.getResult().equals("0")) {
					// 关闭等待界面
					Message endMsg = mHandler.obtainMessage();
					endMsg.what = MESSAGE_GETTING_OK;
					endMsg.obj = aWebRes.getMessageList();
					mHandler.sendMessage(endMsg);
					AppDataCache.getInstance().setServerTime(
							aWebRes.getServerTime());
				} else {
					Message endMsg = mHandler
							.obtainMessage(MESSAGE_GETTING_FAILED);
					mHandler.sendMessage(endMsg);
				}
			}

			@Override
			public void WebRequestException(String ex) {
				Message endMsg = mHandler
						.obtainMessage(MESSAGE_GETTING_EXCEPTION);
				endMsg.obj = "ex:请求信息列表失败:" + ex;
				mHandler.sendMessage(endMsg);
			}

			@Override
			public void WebRequsetFail(String sfalied) {
				Message endMsg = mHandler.obtainMessage(MESSAGE_GETTING_FAILED);
				endMsg.obj = "falied:请求信息列表失败:" + sfalied;
				mHandler.sendMessage(endMsg);
			}
		};
		WebRequestThreadEx<WebRes_GetNotice> aWebRequestThreadEx = new WebRequestThreadEx<WebRes_GetNotice>(
				aWebReq_GetNotice, aICommonWebResponse, new WebRes_GetNotice());
		new Thread(aWebRequestThreadEx).start();
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
			// startDate = mSimpleDateFormat1.format(new Date());
			// dateTime = startDate + "$" + "00!00!00";
			// maxID = "0";
		} else {
			// 获取本地数据库最新一条记录的时间和ID
			// if (cursor.moveToFirst()) {
			// int ID = cursor.getInt(cursor.getColumnIndex("ID"));
			// String msgDate = cursor.getString(cursor
			// .getColumnIndex("msgDate"));
			// String CreateTime = cursor.getString(
			// cursor.getColumnIndex("CreateTime")).replace("/", "-");
			// // System.out.println("CreateTime1 =" + CreateTime);
			// maxID = String.valueOf(ID);
			// Date currentTime = null;
			// try {
			// Date beginTime = mSimpleDateFormat.parse(CreateTime);
			// currentTime = mSimpleDateFormat.parse(mSimpleDateFormat
			// .format(new Date()));
			// long spanTime = currentTime.getTime() - beginTime.getTime();
			// System.out.println("spanTime =" + spanTime);
			// if (spanTime < 0)
			// return;
			// // 当前时间与最新记录的时间之差：小于三天则取本地数据库最新记录的时间；否则取当前时间
			// if (spanTime <= EndTOBegin) {
			// String hour = String.valueOf(beginTime.getHours());
			// String min = String.valueOf(beginTime.getMinutes());
			// String sec = String.valueOf(beginTime.getSeconds());
			// dateTime = msgDate + "$" + hour + "!" + min + "!" + sec;
			// } else {
			// dateTime = mSimpleDateFormat1.format(new Date()) + "$"
			// + "00!00!00";
			// maxID = "0";
			// }
			// } catch (ParseException e) {
			// e.printStackTrace();
			// }
			// }
			// 将本地数据库的数据放入缓存
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				// System.out
				// .println("CreateTime2 ="
				// + cursor.getString(cursor
				// .getColumnIndex("CreateTime")));
				int ID = cursor.getInt(cursor.getColumnIndex("ID"));
				// String msgDate = cursor.getString(cursor
				// .getColumnIndex("msgDate"));
				MessageInfo messaage = new MessageInfo();
				messaage.setID(cursor.getInt(cursor.getColumnIndex("ID")));
				messaage.setContent(cursor.getString(cursor
						.getColumnIndex("Content")));
				messaage.setMsgType(cursor.getString(cursor
						.getColumnIndex("MsgType")));
				messaage.setTime(cursor.getString(cursor.getColumnIndex("Time")));
				messaage.setTitle(cursor.getString(cursor
						.getColumnIndex("Title")));
				messaage.setIsMsgReaded(cursor.getString(cursor
						.getColumnIndex("IsMsgReaded")));
				// messaage.setDate(msgDate);
				mMessageList.add(messaage);

			}
			Log.i("mMessageList", "mMessageList.size--" + mMessageList.size());
		}
		cursor.close();
	}
}
