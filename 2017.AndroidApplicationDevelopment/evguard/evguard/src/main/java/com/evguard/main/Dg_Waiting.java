package com.evguard.main;

import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

import com.evguard.tools.LogEx;
import com.xinghaicom.evguard.R;

public class Dg_Waiting extends Dg_Base {

	protected ProgressBar mProgressView = null;
	protected TextView mMessageView = null;
	private TextView mTitleBar = null;
	private LinearLayout ll_content;
	private String stitle;
	private String sMessage;

	public static Dg_Waiting newInstance(String sTitle, String sMsg) {
		Dg_Waiting f = new Dg_Waiting();
		Bundle args = new Bundle();
		args.putString("title", sTitle);
		args.putString("message", sMsg);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		stitle = getArguments().getString("title");
		sMessage = getArguments().getString("message");

	}

	@Override
	public void onSaveInstanceState(Bundle outInstanceState) {
		outInstanceState.putString("stitle", stitle);
		outInstanceState.putString("sMessage", sMessage);
		super.onSaveInstanceState(outInstanceState);
	}

	public void initialize(View baseview, Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			stitle = savedInstanceState.getString("stitle");
			sMessage = savedInstanceState.getString("sMessage");
		}
		mTitleBar = (TextView) baseview.findViewById(R.id.tv_dgtip);
		LogEx.i("bgs", "mTitleBar =" + mTitleBar);
		ll_content = (LinearLayout) baseview.findViewById(R.id.ll_dgcontent);
		mTitleBar.setText(stitle);

		TableLayout view = new TableLayout(mContext);
		TableLayout.LayoutParams tlLP = new TableLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1);
		view.setLayoutParams(tlLP);
		view.setPadding(6, 35, 6, 35);
		view.setColumnStretchable(1, true);
		TableRow tr = new TableRow(mContext);
		TableRow.LayoutParams lp = new TableRow.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1);
		lp.gravity = Gravity.CENTER_VERTICAL;
		mTitleBar = new TextView(mContext);
		LayoutParams titleBarParam = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		titleBarParam.setMargins(0, 0, 0, 30);

		mProgressView = new ProgressBar(mContext);
		mProgressView.setLayoutParams(lp);
		mMessageView = new TextView(mContext);
		mMessageView.setLayoutParams(lp);
		mMessageView.setGravity(Gravity.CENTER_VERTICAL);
		mMessageView.setTextColor(Color.BLACK);
		mMessageView.setTextSize(16);
		mMessageView.setText(sMessage);

		tr.addView(mProgressView);
		tr.addView(mMessageView);
		view.addView(tr);
		ll_content.addView(view);

		this.setCancelable(false);
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

	public void setTitle(String title) {
		stitle = title;
		if (mTitleBar == null)
			return;
		mTitleBar.setText(stitle);
	}

	public void setMessage(String msg) {
		sMessage = msg;
		if (mMessageView == null)
			return;
		mMessageView.setText(msg);
	}

	
}
