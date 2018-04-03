package com.evguard.main;

import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evguard.tools.LogEx;
import com.xinghaicom.evguard.R;

public class Dg_Alert extends Dg_Base {

	protected TextView mTVMessage = null;
	protected Button mBTNPositive = null;
	private TextView mTitleBar = null;
	private LinearLayout ll_content;

	private String stitle;
	private String sMessage;
	private String sbtntip;
	private View.OnClickListener aposbtnlistener;

	public static Dg_Alert newInstance(String sTitle, String sMsg, String btntip) {
		Dg_Alert f = new Dg_Alert();
		Bundle args = new Bundle();
		args.putString("title", sTitle);
		args.putString("message", sMsg);
		args.putString("btntip", btntip);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		stitle = getArguments().getString("title");
		sMessage = getArguments().getString("message");
		sbtntip = getArguments().getString("btntip");
	}

	@Override
	public void onSaveInstanceState(Bundle outInstanceState) {
		outInstanceState.putString("stitle", stitle);
		outInstanceState.putString("sMessage", sMessage);
		outInstanceState.putString("sbtntip", sbtntip);
		super.onSaveInstanceState(outInstanceState);
	}

	public void initialize(View baseview, Bundle savedInstanceState) {

		if (savedInstanceState != null) {
			stitle = savedInstanceState.getString("stitle");
			sMessage = savedInstanceState.getString("sMessage");
		}
		mTitleBar = (TextView) baseview.findViewById(R.id.tv_dgtip);
		ll_content = (LinearLayout) baseview.findViewById(R.id.ll_dgcontent);

		mTitleBar.setText(stitle);
		View timepickerview = View.inflate(mContext, R.layout.datepicker, null);
		LinearLayout view = new LinearLayout(mContext);
		LayoutParams tlLP = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(tlLP);
		view.setPadding(5, 0, 5, 0);
		view.setOrientation(LinearLayout.VERTICAL);
		view.addView(timepickerview);
		mTVMessage = new TextView(mContext);
		LinearLayout.LayoutParams msgLP = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		msgLP.gravity = Gravity.CENTER_HORIZONTAL;
		msgLP.weight = 1;
		msgLP.setMargins(0, 10, 0, 0);
		mTVMessage.setLayoutParams(msgLP);
		mTVMessage.setGravity(Gravity.CENTER_HORIZONTAL);
		mTVMessage.setSingleLine(false);
		mTVMessage.setTextSize(16);
		mTVMessage.setTextColor(Color.BLACK);
		mTVMessage.setText(sMessage);
		view.addView(mTVMessage);

		mBTNPositive = new Button(mContext);
		LinearLayout.LayoutParams btnPosLP = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		btnPosLP.setMargins(0, 15, 0, 0);
		btnPosLP.gravity = Gravity.BOTTOM;
		btnPosLP.weight = 0;
		mBTNPositive.setText(sbtntip);
		mBTNPositive.setOnClickListener(aposbtnlistener);
		mBTNPositive.setGravity(Gravity.CENTER_HORIZONTAL);
		mBTNPositive.setLayoutParams(btnPosLP);
		view.addView(mBTNPositive);

		ll_content.addView(view);
		this.getDialog().setOnKeyListener(new OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (mOnCancelListener != null)
						mOnCancelListener.onCancle();
				}
				return false; // pass on to be processed as normal
			}
		});
		// getWindow().setLayout(LayoutParams.FILL_PARENT,
		// LayoutParams.WRAP_CONTENT);

	}

	public void setTitle(CharSequence title) {
		stitle = (String) title;
		LogEx.i("bgs1", "mTitleBar =" + mTitleBar);
		if (mTitleBar == null)
			return;
		mTitleBar.setText(title);
	}

	public void setMessage(String msg) {
		sMessage = msg;
		if (mTVMessage == null)
			return;
		mTVMessage.setText(msg);

	}

	

	public void setPositiveButton(String text,
			View.OnClickListener onClickListener) {
		sbtntip = text;
		aposbtnlistener = onClickListener;
		if (mBTNPositive == null)
			return;
		mBTNPositive.setOnClickListener(onClickListener);
	}

}