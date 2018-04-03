package com.evguard.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.xinghaicom.evguard.R;

/**
 * 
 * @author wlh 2015-4-15
 */
public class CustomSwitchButton extends Button {

	private Context mContext;
	private boolean bIsOn = false;

	public CustomSwitchButton(Context context) {
		super(context);
		mContext = context;
//		initButton();
		// TODO Auto-generated constructor stub
	}

	public CustomSwitchButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		//	initButton();
	}

	public CustomSwitchButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		//initButton();
	}


	public void setIsOn(boolean b) {
		bIsOn = b;
		if (bIsOn) {
			setBackgroundResource(R.drawable.icon_aa_on);
		} else {
			setBackgroundResource(R.drawable.icon_aa_off);
		}
	}

	public boolean getIsOn() {
		return this.bIsOn;
	}

}
