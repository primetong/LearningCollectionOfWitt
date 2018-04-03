package com.evguard.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evguard.customview.customImageButton;
import com.xinghaicom.evguard.R;

public class Fragment_Menu_Bottom extends Fragment_Base implements
		View.OnClickListener {

	customImageButton customButton_startpage;
	customImageButton customButton_battery;
	customImageButton customButton_track;
	customImageButton customButton_energy;
	customImageButton customButton_diagnose;

	private OnMenuActionClickListener mOnMenuActionClickListener;

	private static Fragment_Menu_Bottom mInstance;
	private static Object lockobj = new Object();

	private boolean mIsLocationShow = false;
	private boolean mIsCanClick = false;
	private boolean mIsNoKid = false;
	private String mCurBtnTag;
	private customImageButton mCurBtn;

	public static Fragment_Menu_Bottom getInstance() {
		if (mInstance == null)
			synchronized (lockobj) {
				mInstance = new Fragment_Menu_Bottom();
			}
		return mInstance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (layoutView != null)
			return layoutView;
		layoutView = inflater.inflate(R.layout.fragment_menu_bottom, container,
				false);
		return layoutView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		findView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mInstance = null;
	}

	private void findView() {
		customButton_startpage = (customImageButton) layoutView
				.findViewById(R.id.custombutton_startpage);
		customButton_battery = (customImageButton) layoutView
				.findViewById(R.id.custombutton_battery);
		customButton_track = (customImageButton) layoutView
				.findViewById(R.id.custombutton_track);
		customButton_energy = (customImageButton) layoutView
				.findViewById(R.id.custombutton_energy);
		customButton_diagnose = (customImageButton) layoutView
				.findViewById(R.id.custombutton_diagnose);

		customButton_startpage.setOnClickListener(this);
		customButton_battery.setOnClickListener(this);
		customButton_track.setOnClickListener(this);
		customButton_energy.setOnClickListener(this);
		customButton_diagnose.setOnClickListener(this);

		mCurBtn = customButton_startpage;
		mCurBtnTag = customButton_startpage.getTag().toString();

		 if(mIsLocationShow){
		 customButton_startpage.setOnFocuse();
		 }

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (mOnMenuActionClickListener == null)
			return;
		if (!mIsCanClick) {
			showToast(mContext.getResources().getString(
					R.string.tip_loadingkids));
			return;
		}
		if (mIsNoKid) {
			showToast(mContext.getResources().getString(R.string.tip_nocar));
			return;
		}
		switch (v.getId()) {
		case R.id.custombutton_startpage:
			mCurBtn = customButton_startpage;
			mCurBtnTag = mCurBtn.getTag().toString();
			customButton_startpage.setOnFocuse();
			customButton_battery.setOnDisFouce();
			customButton_track.setOnDisFouce();
			customButton_diagnose.setOnDisFouce();
			customButton_energy.setOnDisFouce();
			System.out.println("mCurBtnTag==" + mCurBtnTag);
			mOnMenuActionClickListener.onAction1();
			break;
		case R.id.custombutton_battery:
			mCurBtn = customButton_battery;
			mCurBtnTag = mCurBtn.getTag().toString();
			customButton_battery.setOnFocuse();
			customButton_startpage.setOnDisFouce();
			customButton_track.setOnDisFouce();
			customButton_diagnose.setOnDisFouce();
			customButton_energy.setOnDisFouce();
			System.out.println("mCurBtnTag==" + mCurBtnTag);
			mOnMenuActionClickListener.onAction2();
			break;
		case R.id.custombutton_track:
			mCurBtn = customButton_track;
			mCurBtnTag = mCurBtn.getTag().toString();
			customButton_track.setOnFocuse();
			customButton_startpage.setOnDisFouce();
			customButton_battery.setOnDisFouce();
			customButton_diagnose.setOnDisFouce();
			customButton_energy.setOnDisFouce();
			System.out.println("mCurBtnTag==" + mCurBtnTag);
			mOnMenuActionClickListener.onAction4();
			break;
		case R.id.custombutton_energy:
			mCurBtn = customButton_energy;
			mCurBtnTag = mCurBtn.getTag().toString();
			customButton_energy.setOnFocuse();
			customButton_startpage.setOnDisFouce();
			customButton_battery.setOnDisFouce();
			customButton_diagnose.setOnDisFouce();
			customButton_track.setOnDisFouce();
			System.out.println("mCurBtnTag==" + mCurBtnTag);
			mOnMenuActionClickListener.onAction3();
			break;
		case R.id.custombutton_diagnose:
			mCurBtn = customButton_diagnose;
			mCurBtnTag = mCurBtn.getTag().toString();
			customButton_diagnose.setOnFocuse();
			customButton_startpage.setOnDisFouce();
			customButton_battery.setOnDisFouce();
			customButton_energy.setOnDisFouce();
			customButton_track.setOnDisFouce();
			System.out.println("mCurBtnTag==" + mCurBtnTag);
			mOnMenuActionClickListener.onAction5();
			break;
		default:
			break;
		}
	}

	@SuppressLint("NewApi")
	public void locationPerClick() {
		mIsLocationShow = true;
	}

	public void setIsCanClick(boolean b) {
		this.mIsCanClick = b;
	}

	public void setOnMenuActionClickListener(
			OnMenuActionClickListener aOnMenuActionClickListener) {
		this.mOnMenuActionClickListener = aOnMenuActionClickListener;
	}

	public String getCurBtnTag() {
		return mCurBtnTag;
	}

	@SuppressLint("NewApi")
	public void setCurBtnTag(String tag) {
		if (tag.equals(customButton_startpage.getTag().toString())) {
			mCurBtn = customButton_startpage;
		} else if (tag.equals(customButton_battery.getTag().toString())) {
			mCurBtn = customButton_battery;
		} else if (tag.equals(customButton_energy.getTag().toString())) {
			mCurBtn = customButton_energy;
		} else if (tag.equals(customButton_diagnose.getTag().toString())) {
			mCurBtn = customButton_diagnose;
		} else if (tag.equals(customButton_track.getTag().toString())) {
			mCurBtn = customButton_track;
		}
		mCurBtn.setOnFocuse();
	}

	public interface OnMenuActionClickListener {
		public void onAction1();

		public void onAction2();

		public void onAction3();

		public void onAction4();

		public void onAction5();

	}

	@Override
	protected void restoreState(Bundle saveState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Bundle saveState(Bundle saveState) {
		// TODO Auto-generated method stub
		return saveState;
	}

}
