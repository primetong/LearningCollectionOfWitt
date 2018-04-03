package com.evguard.main;

import java.text.NumberFormat;

import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.xinghaicom.evguard.R;

public class Dg_Progress extends Dg_Base {


	protected TextView mTVMessage ;
	protected ProgressBar mProgressBar ;
	protected TextView mTVPercentProgress ;
	protected TextView mTVCountProgress ;
	private   TextView mTitleBar;	
	
	private LinearLayout ll_content;
	private String stitle;
	private String smsg;
	private int imax;
	private String percentDes ;
	private String countPgDes;
	private int iprogress;

	public void setMessage(String msg){
		smsg=msg;
		if(mTVMessage==null)return;
		mTVMessage.setText(msg);
	}
	public void setMax(int max){
		imax=max;
		if(mProgressBar==null)return;
		mProgressBar.setMax(imax);
	}

	public void setProgress(int progress){
		iprogress=progress;
		NumberFormat numFormator = NumberFormat.getPercentInstance();
		percentDes = numFormator.format((double)progress/(double)imax);
		countPgDes = String.format("%d/%d", progress,imax);
		if(mProgressBar==null)return;
		mProgressBar.setProgress(progress);
		mTVPercentProgress.setText(percentDes);
		mTVCountProgress.setText(countPgDes);		
	}
	
	public void incrementProgress(int diff){
		if(mProgressBar==null)return;
		mProgressBar.incrementProgressBy(diff);
		
		int progress = mProgressBar.getProgress();
		NumberFormat numFormator = NumberFormat.getPercentInstance();
		percentDes = numFormator.format((double)progress/(double)mProgressBar.getMax());
		countPgDes = String.format("%d/%d", progress,mProgressBar.getMax());
		
		mTVPercentProgress.setText(percentDes);
		mTVCountProgress.setText(countPgDes);		
	}
	
	public void setCompleted(){
		if(mProgressBar==null)return;
		setProgress(imax);	
	}
	
	public boolean isCompleted(){
		if(mProgressBar==null)return true;
		return mProgressBar.getProgress() >= mProgressBar.getMax();
	}
	public static Dg_Progress newInstance(String sTitle, String sMsg,
			String posbtntip, String cnlbtntip) {
		Dg_Progress f = new Dg_Progress();
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
		smsg = getArguments().getString("message");

	}

	@Override
	public void onSaveInstanceState(Bundle outInstanceState) {
		outInstanceState.putString("stitle", stitle);
		outInstanceState.putString("sMessage", smsg);
		
		super.onSaveInstanceState(outInstanceState);
	}

	public void initialize(View baseview, Bundle savedInstanceState) {

		if (savedInstanceState != null) {
			stitle = savedInstanceState.getString("stitle");
			smsg = savedInstanceState.getString("sMessage");
			
		}

	
		mTitleBar=(TextView)baseview.findViewById(R.id.tv_dgtip);
		ll_content=(LinearLayout)baseview.findViewById(R.id.ll_dgcontent);
		mTitleBar.setText(stitle);
		
		TableLayout view  = new TableLayout(mContext);
		TableLayout.LayoutParams tlLP = new TableLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1);
		view.setLayoutParams(tlLP);
		view.setPadding(13, 0, 13, 0);
		view.setColumnStretchable(1, true);
		view.setColumnShrinkable(1, true);
		view.setGravity(Gravity.CENTER);
		
		
		TableRow trWaitingMsg = new TableRow(mContext);
				
		RelativeLayout rl = new RelativeLayout(mContext);		
		ProgressBar pbWaiting = new ProgressBar(mContext);
		RelativeLayout.LayoutParams pbWaitingLP = new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		pbWaitingLP.addRule(RelativeLayout.CENTER_IN_PARENT);
		pbWaiting.setLayoutParams(pbWaitingLP);
		rl.addView(pbWaiting);
		
		mTVMessage = new TextView(mContext);
		TableRow.LayoutParams tDesLP = new TableRow.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		tDesLP.gravity = Gravity.CENTER_VERTICAL;
		mTVMessage.setLayoutParams(tDesLP);		
		mTVMessage.setSingleLine(false);
		mTVMessage.setTextSize(16);
		mTVMessage.setTextColor(Color.BLACK);		
		
		trWaitingMsg.addView(rl);
		trWaitingMsg.addView(mTVMessage);
		mTVMessage.setPadding(5, 5, 5, 5);
		
		mProgressBar = new ProgressBar(mContext,null,android.R.attr.progressBarStyleHorizontal);
		TableLayout.LayoutParams pbLP = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		pbLP.gravity = Gravity.CENTER_VERTICAL;
		mProgressBar.setLayoutParams(pbLP);		
		
		
		TableRow tr = new TableRow(mContext);
				
		mTVPercentProgress = new TextView(mContext);
		TableRow.LayoutParams percentLP = new TableRow.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		percentLP.gravity = Gravity.LEFT;
		mTVPercentProgress.setLayoutParams(percentLP);
		mTVPercentProgress.setTextColor(Color.BLACK);

		mTVCountProgress = new TextView(mContext);
		TableRow.LayoutParams countLP = new TableRow.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		countLP.gravity = Gravity.RIGHT;
		mTVCountProgress.setLayoutParams(countLP);
		mTVCountProgress.setTextColor(Color.BLACK);
		
		tr.addView(mTVPercentProgress);
		tr.addView(mTVCountProgress);
		view.addView(trWaitingMsg);
//		view.addView(mTVMessage);
		view.addView(mProgressBar);
		view.addView(tr);
		
		mProgressBar.setMax(imax);
		mProgressBar.setProgress(0);
		mTVMessage.setText(smsg);
		mTVPercentProgress.setText(percentDes);
		mTVCountProgress.setText(countPgDes);			
		
		ll_content.addView(view);
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
	
	public void setTitle(CharSequence title) {
		stitle=(String) title;
		if(mTitleBar==null)return;
		mTitleBar.setText(title);
	}
}
