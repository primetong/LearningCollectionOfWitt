package com.evguard.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinghaicom.evguard.R;
/**
 * 
 * @author wlh
 * 2015-4-15
 *
 */
public class customImageButton extends LinearLayout {

	private Context mContext;
	private ImageView img;
	private TextView txt;
	
	//自定义属性
	private Drawable defaultimg;
	private Drawable lightimg;
	private int defalutTxtColor;
	private int lightTxtColor;
	private String textString;

	
	public customImageButton(Context context) {
		super(context);
		mContext=context;
		init();
		// TODO Auto-generated constructor stub
	}

	public customImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		init();
		pareAttr(attrs);
	}
	

	private void init(){
		LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inflater.inflate(R.layout.custombutton,this);
			img=(ImageView) findViewById(R.id.img);
			txt=(TextView)findViewById(R.id.txt);
	}
	
	private void pareAttr(AttributeSet attrs){
		if(isInEditMode()){return;}
		TypedArray a = mContext.obtainStyledAttributes(attrs,R.styleable.CustomImageButton); 
		int defaultimgid = a.getResourceId(R.styleable.CustomImageButton_defalutImg, -1);
		defaultimg=mContext.getResources().getDrawable(defaultimgid);
		int lightimgid = a.getResourceId(R.styleable.CustomImageButton_lightImg, -1);
		lightimg=mContext.getResources().getDrawable(lightimgid);
		
		textString=a.getString(R.styleable.CustomImageButton_TextContext);
		defalutTxtColor=a.getColor(R.styleable.CustomImageButton_defalutTextColor, R.color.white);
		lightTxtColor=a.getColor(R.styleable.CustomImageButton_lightTextColor, R.color.red);
		a.recycle();
		img.setImageDrawable(defaultimg);
		txt.setText(textString);
		txt.setTextColor(defalutTxtColor);
		txt.setTextSize(12);
		
	}
	
	public void setOnFocuse(){
		img.setImageDrawable(lightimg);
		txt.setTextColor(lightTxtColor);
	}
	public void setOnDisFouce(){
		img.setImageDrawable(defaultimg);
		txt.setTextColor(defalutTxtColor);
	}

}
