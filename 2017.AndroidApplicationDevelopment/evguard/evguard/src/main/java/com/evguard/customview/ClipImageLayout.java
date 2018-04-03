package com.evguard.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

public class ClipImageLayout extends RelativeLayout {
	 private ClipZoomImageView mZoomImageView;  
	 private ClipImageBorderView mClipImageView;  
	 private int mHorizontalPadding = 20;  
	    
	public ClipImageLayout(Context context) {
		super(context );
		init(context);
	}
	public ClipImageLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	public ClipImageLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	public void setDrawable(Drawable d)
	{
		mZoomImageView.setImageDrawable(d);
	}
	public void setImageUrl(Uri url)
	{
//		ImageLoader.getInstance().loadImage(url, mZoomImageView);
		mZoomImageView.setImageURI(url);
	}
	public void setImage(Bitmap bp){
		mZoomImageView.setImageBitmap(bp);
	}
	private void init(Context context)
	{
		mZoomImageView = new ClipZoomImageView(context);  
        mClipImageView = new ClipImageBorderView(context);  
  
        android.view.ViewGroup.LayoutParams lp = new LayoutParams(  
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,  
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);  
          
          
        this.addView(mZoomImageView, lp);  
        this.addView(mClipImageView, lp);  
  
          
        // 计算padding的px  
        mHorizontalPadding = (int) TypedValue.applyDimension(  
                TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()  
                        .getDisplayMetrics());  
        mZoomImageView.setHorizontalPadding(mHorizontalPadding);  
        mClipImageView.setHorizontalPadding(mHorizontalPadding);  
	}
	/** 
     * 对外公布设置边距的方法,单位为dp 
     *  
     * @param mHorizontalPadding 
     */  
    public void setHorizontalPadding(int mHorizontalPadding)  
    {  
        this.mHorizontalPadding = mHorizontalPadding;  
    }  
  
    /** 
     * 裁切图片 
     *  
     * @return 
     */  
    public Bitmap clip()  
    {  
        return mZoomImageView.clip();  
    }  
}
