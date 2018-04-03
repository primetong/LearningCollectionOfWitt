package com.evguard.customview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NewViewPaper extends ViewPager {

	private boolean DisableScroll=false;
	public NewViewPaper(Context context ) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public NewViewPaper(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public void setDisableScroll(boolean b)
	{
		this.DisableScroll=b;
	}
	
	@Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(DisableScroll)
        {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }
	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		if(this.DisableScroll)
		{
			return false;
		}
		return super.onTouchEvent(e);
	}
}
