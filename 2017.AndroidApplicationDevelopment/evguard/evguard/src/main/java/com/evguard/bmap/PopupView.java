package com.evguard.bmap;


import java.net.URL;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.NinePatchDrawable;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 
* @ClassName: PopupView 
* @Description: TODO
* @author
* 
*
 */
public class PopupView extends LinearLayout {
	
//	private static PopupView mPopupView=null;
	public  TextView mCarNum=null;//孩子昵称
	private TextView mGpsTime=null;//GPS时间
	private TextView mFirstMsg=null;//
	private TextView mSecondMsg=null;//
	private TextView mAddress=null;//地址
	private NinePatchDrawable mBubbleDrawable = null;
//	public static PopupView getInstance(Context context){
////		if(mPopupView==null)
//			mPopupView=new PopupView(context);
//		return mPopupView;
//		
//	}
	public PopupView(Context context){
		super(context);
		this.setOrientation(VERTICAL);				
		URL bubbleURL = PopupView.class.getResource("symbol/bubble.9.png");		
		if(mBubbleDrawable == null) mBubbleDrawable =Symbolizer.buildNinePatchSymbol(bubbleURL);
		this.setBackgroundDrawable(mBubbleDrawable);
		this.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		this.setPadding(10, 5, 10, 20);
		//昵称					
		mCarNum = new TextView(context);
		mCarNum.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mCarNum.setGravity(Gravity.LEFT);
		mCarNum.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);
		mCarNum.setTextColor(Color.BLACK);
		this.addView(mCarNum);
		//GPS时间
		mGpsTime = new TextView(context);
		mGpsTime.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mGpsTime.setGravity(Gravity.LEFT);
		mGpsTime.setTextColor(Color.BLACK);
		mGpsTime.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);
		this.addView(mGpsTime);
		//消息
		mFirstMsg = new TextView(context);
		mFirstMsg.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mFirstMsg.setGravity(Gravity.LEFT);
		mFirstMsg.setTextColor(Color.BLACK);
		mFirstMsg.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);
		this.addView(mFirstMsg);
		//消息
		mSecondMsg = new TextView(context);
		mSecondMsg.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mSecondMsg.setGravity(Gravity.LEFT);
		mSecondMsg.setTextColor(Color.BLACK);
		mSecondMsg.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);
		this.addView(mSecondMsg);
		//地址
		mAddress = new TextView(context);
		mAddress.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mAddress.setGravity(Gravity.LEFT);
		mAddress.setTextColor(Color.BLUE);
		mAddress.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);
		this.addView(mAddress);
	}
	public void setCarNum(String carNum){
		mCarNum.setText("昵称："+carNum);
	}
	public void setGpsTime(String gpsTime){
		mGpsTime.setText("GPS时间："+gpsTime);
	}
	public void setFirstMsg(String msg){//
		mFirstMsg.setText(msg);
	}
	public void setSecondMsg(String msg){//
		mSecondMsg.setText(msg);
	}
	public void setAddress(String address){
		if(address.length()>16){
			String newStr="";
			int index=0;
			while(index+1<address.length()){
				newStr+=address.substring(index, index+16>address.length()?address.length():index+16)+"\n";
				index=index+16;
			}
			mAddress.setText(newStr);
		}else{
			mAddress.setText(address);
		}
	}
}
