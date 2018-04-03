package com.evguard.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 截取框
 * @author wlh
 *2015-4-15
 */
public class ClipImageBorderView extends SurfaceView implements SurfaceHolder.Callback {

	private SurfaceHolder mHolder=null;
	
	private float width=0;
	private float heigt=0;
	private float xCenterPoint=0;
	private float yCenterPoint=0;
	private float wRect=0;

	private float mHorizontalPadding=30;
	private float hPadding=30;
	private float wStroke=5;
	
	private Paint mPaintOverlay=null;
	private Paint mPaintImage=null;
	

	public ClipImageBorderView(Context context) {
		super(context);
		init();
	}
	public ClipImageBorderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public ClipImageBorderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}
	 public void setHorizontalPadding(int mHorizontalPadding)  
	    {  
	        this.mHorizontalPadding = mHorizontalPadding;  
	    }  
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		width=this.getWidth();
		heigt=this.getHeight();
		xCenterPoint=width/2;
		yCenterPoint=heigt/2;
		wRect=width-2*mHorizontalPadding;
		hPadding=(heigt-wRect)/2;
		
		new Thread(){
			
			@Override
			public void run(){
				Canvas canvas=mHolder.lockCanvas();
				canvas.drawColor(Color.TRANSPARENT,Mode.CLEAR);   
				canvas.drawRect(0, 0, mHorizontalPadding, heigt, mPaintOverlay);//左边阴影
				canvas.drawRect(width-mHorizontalPadding, 0, width, heigt, mPaintOverlay);//右边阴影
				canvas.drawRect(mHorizontalPadding, 0, width-mHorizontalPadding, hPadding, mPaintOverlay);//上中阴影
				canvas.drawRect(mHorizontalPadding,heigt-hPadding, width-mHorizontalPadding, heigt, mPaintOverlay);//下中阴影
			
				canvas.drawRect(xCenterPoint-wRect/2,yCenterPoint-wRect/2, xCenterPoint+wRect/2, yCenterPoint+wRect/2, mPaintImage);
				mHolder.unlockCanvasAndPost(canvas);
			}
		}.start();
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		
		
	}

	private void init()
	{
		
		mHolder=this.getHolder();
		mHolder.addCallback(this);
		//设置surfaceview背景透明
		setZOrderOnTop(true);
		mHolder.setFormat(PixelFormat.TRANSLUCENT);   
		
		mPaintOverlay=new Paint();
		mPaintOverlay.setAntiAlias(true);
		mPaintOverlay.setStyle(Style.FILL);
		mPaintOverlay.setColor(Color.parseColor("#aa000000"));  
		
		mPaintImage=new Paint();
		mPaintImage.setAntiAlias(true);
		mPaintImage.setStyle(Style.STROKE);
		mPaintImage.setStrokeWidth(wStroke);
		mPaintImage.setColor(Color.WHITE);
		
	}

}
