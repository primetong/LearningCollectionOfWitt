package com.evguard.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinghaicom.evguard.R;

public class CustomTextButton extends LinearLayout {

	private Context mContext=null;
	private TextView tv_tip=null;
	private EditText et_content=null;
	
	private String sTip="";
	private String sContent="";
	
	
	@SuppressLint("NewApi")
	public CustomTextButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext=context;
		pareAttr(attrs);
		init();
		// TODO Auto-generated constructor stub
	}
	public CustomTextButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		pareAttr(attrs);
		init();
	}
	public CustomTextButton(Context context) {
		super(context);
		mContext=context;
		init();
	}
	private void pareAttr(AttributeSet attrs){
		if(isInEditMode()){return;}
		try {
			TypedArray a = mContext.obtainStyledAttributes(attrs,R.styleable.CustomTextButton); 
			sTip=a.getString(R.styleable.CustomTextButton_TextButton_Tip);
			sContent=a.getString(R.styleable.CustomTextButton_TextButton_Context);
			a.recycle();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void init(){
		if(isInEditMode()){return;}
		this.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		this.setLayoutParams(p);
		LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.customtextbutton,this);
		
		tv_tip=(TextView)this.findViewById(R.id.tv_customtextbutton_tip);
		et_content=(EditText)this.findViewById(R.id.tv_customtextbutton_content);
		tv_tip.setText(sTip);
		et_content.setText(sContent);
	}
	public void setTextContentEditable(){
		et_content.setFocusable(true);
		et_content.setFocusableInTouchMode(true);
		et_content.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
	}
	public void setTextContentNotClick(){
		et_content.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
	}
	public String getTextContent(){
		return this.et_content.getText().toString();
	}
	public void setTextContent(String content){
		this.sContent=content;
		et_content.setText(sContent);
	}
	public void setTextContentOnClickListener(View.OnClickListener listener){
		et_content.setOnClickListener(listener);
	}
	public void setTextDrawUnShow(){
		et_content.setCompoundDrawables(null, null, null, null);
	}
}
