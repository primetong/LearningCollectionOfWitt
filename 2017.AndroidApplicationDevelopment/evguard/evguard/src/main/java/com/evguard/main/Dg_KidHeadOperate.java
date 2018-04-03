package com.evguard.main;

import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinghaicom.evguard.R;

public class Dg_KidHeadOperate extends Dg_Base{

	private LinearLayout ll_content;
	private TextView mTitle;
	private TextView tv_Camaral;
	private TextView tv_Photo;
	private String sphoto="图片";
	private OnKidHeadOperateClickListener mOnKidHeadOperateClickListener;
	

	public static Dg_KidHeadOperate newInstance() {
		Dg_KidHeadOperate f = new Dg_KidHeadOperate();
		Bundle args = new Bundle();
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public void onSaveInstanceState(Bundle outInstanceState) {
		
		super.onSaveInstanceState(outInstanceState);
	}

	public void initialize(View baseview, Bundle savedInstanceState) {
		
		mTitle=(TextView)baseview.findViewById(R.id.tv_dgtip);
		ll_content=(LinearLayout)baseview.findViewById(R.id.ll_dgcontent);
		mTitle.setText(mContext.getResources().getString(R.string.dg_updatekidhead));

		tv_Camaral=new TextView(mContext);
		LinearLayout.LayoutParams tvparams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		tv_Camaral.setLayoutParams(tvparams);
		tv_Camaral.setGravity(Gravity.CENTER);
		tv_Camaral.setBackgroundResource(R.drawable.bg_txt);
		tv_Camaral.setTextSize(16);
		tv_Camaral.setTextColor(Color.BLACK);
		tv_Camaral.setText("拍照");
		ll_content.addView(tv_Camaral);
		
		tv_Photo=new TextView(mContext);
		LinearLayout.LayoutParams tvparams1=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		tv_Photo.setLayoutParams(tvparams1);
		tv_Photo.setGravity(Gravity.CENTER);
		tv_Photo.setBackgroundResource(R.drawable.bg_txt);
		tv_Photo.setTextSize(16);
		tv_Photo.setTextColor(Color.BLACK);
		tv_Photo.setText(sphoto);
		ll_content.addView(tv_Photo);
		
		loadListener();
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
	private void loadListener(){
		tv_Camaral.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mOnKidHeadOperateClickListener!=null)mOnKidHeadOperateClickListener.onCameralClick();
				
			}
		});
		tv_Photo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mOnKidHeadOperateClickListener!=null)mOnKidHeadOperateClickListener.onPhotoClick();
				
			}
		});
	}
	
	public void setOnKidHeadOperateClickListener(OnKidHeadOperateClickListener aOnKidHeadOperateClickListener){
		this.mOnKidHeadOperateClickListener=aOnKidHeadOperateClickListener;
	} 
	public interface OnKidHeadOperateClickListener{
		public void onCameralClick();
		public void onPhotoClick();
	}
}
