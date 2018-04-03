package com.evguard.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class CustomCircle extends View {

	private float mRadius=150;
	private float mRadiusCenterCircle=10;
	private float mStorkeWith=5;
	private float mXCenter=0.0f;
	private float mYCenter=0.0f;
	private int r=90;
	private int g=202;
	private int b=154;
	private int alphaCircle=70;
	private int alphaStorke=200;
	
	private Paint paint;
	private Paint paint0;
	private Paint paint1;
	private Paint paint2;
	
	public CustomCircle(Context context) {
		super(context);
		init();
	}
	public CustomCircle(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public CustomCircle(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
//	public void setRadiusCenterCircle(float radius)
//	{
//		this.mRadiusCenterCircle=radius;
//	}
//	public void setRadius(float radius)
//	{
//		this.mRadius=radius;
//	}
	public float getRadius()
	{
		return this.mRadius;
	}
//	public void setXCenter(float x)
//	{
//		this.mXCenter=x;
//	}
//	public void setYCenter(float y)
//	{
//		this.mYCenter=y;
//	}

	private void init(){
		paint=new Paint();
		paint.setARGB(alphaCircle,r, g, b);
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
		paint.setDither(true);
		
		paint0=new Paint();
		paint0.setARGB(alphaStorke, r, g, b);
		paint0.setAntiAlias(true);
		paint0.setStrokeWidth(mStorkeWith);
		paint0.setStyle(Style.STROKE);
		
		paint1=new Paint();
		paint1.setARGB(alphaStorke, r, g, b);
		paint1.setAntiAlias(true);
		paint1.setStrokeWidth(mStorkeWith);
		paint1.setStyle(Style.FILL);
		
		paint2=new Paint();
		paint2.setColor(Color.WHITE);
		paint2.setAntiAlias(true);
		paint2.setStrokeWidth(mStorkeWith);
		paint2.setStyle(Style.STROKE);
	}
	@Override
	public void onDraw(Canvas canvas)
	{
//		if(isInEditMode()){return;}
	
		 mXCenter=((View)this.getParent()).getWidth()/2;
		 mYCenter=((View)this.getParent()).getHeight()/2;
		 mRadius=((View)this.getParent()).getWidth()/3;
		
		canvas.drawCircle(mXCenter, mYCenter, mRadius, paint);

		canvas.drawCircle(mXCenter, mYCenter, mRadius, paint0);

		canvas.drawCircle(mXCenter, mYCenter, mRadiusCenterCircle, paint1);

		canvas.drawCircle(mXCenter, mYCenter, mRadiusCenterCircle, paint2);

	}
}
