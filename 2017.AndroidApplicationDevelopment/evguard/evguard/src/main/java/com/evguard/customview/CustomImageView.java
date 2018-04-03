package com.evguard.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.xinghaicom.evguard.R;

/**
 * 
 * @author wlh
 * 2015-4-15
 *
 */
public class CustomImageView extends ImageView {

	private Context mContext;
	private boolean mIsClick=false;//该控件是否被选中
	private int mIndex=0;//该控件的下标
	
	public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext=context;
		// TODO Auto-generated constructor stub
	}
	public CustomImageView(Context context) {
		super(context);
		mContext=context;
	}
	public CustomImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
	}
	
	public void setIsClicked(boolean b){
		this.mIsClick=b;
	}
	public void setIndex(int i){
		this.mIndex=i;
	}
	public int getIndex(){
		return this.mIndex;
	}
	@Override
	public void onDraw(Canvas canvas){
		if(!mIsClick){
			super.onDraw(canvas);
			return;
		}
		if(null==this.getDrawable())return;
		Bitmap b=((BitmapDrawable)this.getDrawable()).getBitmap();
		int w=b.getWidth();
		int h=b.getHeight();
		Paint P=new Paint();
		P.setStyle(Style.STROKE);
		P.setStrokeWidth(10);
		P.setColor(mContext.getResources().getColor(R.color.green));

		canvas.drawCircle(w/2, h/2, w/2+5,  P);
		super.onDraw(canvas);
		
	}

}
