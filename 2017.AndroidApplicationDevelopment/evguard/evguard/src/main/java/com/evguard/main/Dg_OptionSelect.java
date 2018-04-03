package com.evguard.main;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinghaicom.evguard.R;

public class Dg_OptionSelect extends Dg_Base {

	protected TextView mTVMessage = null;
	protected Button mBTNPositive = null;
	protected Button mBTNCancle = null;
	private TextView mTitleBar = null;
	private LinearLayout ll_content;

	private String stitle;
	private String sMessage;
	private String sposbtntip;
	private String scnlbtntip;
	private View.OnClickListener aposbtnlistener;
	private View.OnClickListener acnlbtnlistener;

	public static Dg_OptionSelect newInstance(String sTitle, String sMsg,
			String posbtntip, String cnlbtntip) {
		Dg_OptionSelect f = new Dg_OptionSelect();
		Bundle args = new Bundle();
		args.putString("title", sTitle);
		args.putString("message", sMsg);
		args.putString("posbtntip", posbtntip);
		args.putString("cnlbtntip", cnlbtntip);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		stitle = getArguments().getString("title");
		sMessage = getArguments().getString("message");
		sposbtntip = getArguments().getString("posbtntip");
		scnlbtntip = getArguments().getString("cnlbtntip");
	}

	@Override
	public void onSaveInstanceState(Bundle outInstanceState) {
		outInstanceState.putString("stitle", stitle);
		outInstanceState.putString("sMessage", sMessage);
		outInstanceState.putString("posbtntip", sposbtntip);
		outInstanceState.putString("cnlbtntip", scnlbtntip);
		super.onSaveInstanceState(outInstanceState);
	}

	public void initialize(View baseview, Bundle savedInstanceState) {

		if (savedInstanceState != null) {
			stitle = savedInstanceState.getString("stitle");
			sMessage = savedInstanceState.getString("sMessage");
			sposbtntip = savedInstanceState.getString("posbtntip");
			scnlbtntip = savedInstanceState.getString("cnlbtntip");
		}

		this.setCancelable(false);
		mTitleBar = (TextView) baseview.findViewById(R.id.tv_dgtip);
		ll_content = (LinearLayout) baseview.findViewById(R.id.ll_dgcontent);

		mTitleBar.setText(stitle);

		LinearLayout view = new LinearLayout(mContext);
		LayoutParams tlLP = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(tlLP);
		view.setPadding(5, 0, 5, 0);

		view.setOrientation(LinearLayout.VERTICAL);

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

		LinearLayout view1 = new LinearLayout(mContext);
		LinearLayout.LayoutParams tlLP1 = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		view1.setLayoutParams(tlLP1);
		tlLP1.weight = 0;
		view1.setPadding(5, 0, 5, 0);
		tlLP1.gravity = Gravity.BOTTOM;
		view1.setOrientation(LinearLayout.HORIZONTAL);

		mBTNPositive = new Button(mContext);
		LinearLayout.LayoutParams btnPosLP = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		btnPosLP.setMargins(0, 15, 0, 0);
		btnPosLP.gravity = Gravity.BOTTOM;
		btnPosLP.weight = 1;
		mBTNPositive.setText(sposbtntip);
		mBTNPositive.setOnClickListener(aposbtnlistener);
		mBTNPositive.setGravity(Gravity.CENTER);
		mBTNPositive.setLayoutParams(btnPosLP);
		view1.addView(mBTNPositive);

		mBTNCancle = new Button(mContext);
		LinearLayout.LayoutParams btnPosLC = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		btnPosLC.setMargins(0, 15, 0, 0);
		btnPosLC.gravity = Gravity.BOTTOM;
		btnPosLC.weight = 1;
		mBTNCancle.setText(scnlbtntip);
		mBTNCancle.setOnClickListener(acnlbtnlistener);
		mBTNCancle.setGravity(Gravity.CENTER);
		mBTNCancle.setLayoutParams(btnPosLP);
		view1.addView(mBTNCancle);

		view.addView(view1);

		ll_content.addView(view);

		// getWindow().setLayout(LayoutParams.FILL_PARENT,
		// LayoutParams.WRAP_CONTENT);
	}

	public void setTitle(CharSequence title) {
		stitle = (String) title;
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
		sposbtntip = text;
		aposbtnlistener = onClickListener;
		if (mBTNPositive == null)
			return;
		mBTNPositive.setOnClickListener(onClickListener);
	}

	public void setCancleButton(String text,
			View.OnClickListener onClickListener) {
		scnlbtntip = text;
		acnlbtnlistener = onClickListener;
		if (mBTNCancle == null)
			return;
		mBTNCancle.setOnClickListener(onClickListener);
	}
}
