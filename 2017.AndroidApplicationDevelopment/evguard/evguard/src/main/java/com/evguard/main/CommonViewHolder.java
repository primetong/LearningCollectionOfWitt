package com.evguard.main;



import com.evguard.image.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 通用listview viewholder
 * @author Administrator
 *
 */
public class CommonViewHolder {
	
	private SparseArray<View> mViews;
	private View mConverView;
	private int mposition=0;
//	private ImageLoader mImageLoader=null;
	
	
	protected CommonViewHolder(Context mContext,int position,View convertView,ViewGroup parent,int converViewId)
	{
			mViews=new SparseArray<View>();
//			mConverView=View.inflate(mContext, converViewId, parent);
			mConverView=LayoutInflater.from(mContext).inflate(converViewId, parent,  
	                false);
			mConverView.setTag(this);
			mposition=position;
			
	}
	
	public static CommonViewHolder getViewHolder(Context mContext,int position,View convertView,ViewGroup parent,int converViewId)
	{
		if(convertView==null)
		{
			return new CommonViewHolder(mContext,position,convertView,parent,converViewId);
		}
		return (CommonViewHolder) convertView.getTag();
	}
	protected <T extends View> T getView(int viewid)
	{
		View v=mViews.get(viewid);
		if(v==null)
		{
			v=mConverView.findViewById(viewid);
			mViews.put(viewid, v);
		}
		return (T)v;
	}

	public View getConverView()
	{
		return this.mConverView;
	}
	public int getPosition()
	{
		return this.mposition;
	}
	public void setText(int ViewId,String s)
	{
		TextView t=getView(ViewId);
		t.setText(s);
	}
	
	public void setColor(int ViewId, int color)
	{
		TextView t=getView(ViewId);
		t.setTextColor(color);
	}
	public void setImageResource(int viewId, int drawableId)  
    {  
        ImageView view = getView(viewId);  
        view.setImageResource(drawableId);  
    } 
	public void setImageByDrawable(int viewId, Drawable drawable)  
    {  
        ImageView view = getView(viewId);  
        view.setImageDrawable(drawable);
    } 
	public void setImageByBitmap(int viewId, Bitmap bmp)  
    {  
        ImageView view = getView(viewId);  
        view.setImageBitmap(bmp);
    } 
	
	public void setImageByUrl(int viewId,String url)
	{
		 ImageView view = getView(viewId);  
		 ImageLoader.getInstance().loadImage(url, view);

	}
	public void setImageVisibility(int viewId,int visibility)
	{
		 ImageView view = getView(viewId);  
		 view.setVisibility(visibility);
	}
	
	public void setOnClickListener(int viewId,View.OnClickListener alistener)
	{
		View v=getView(viewId);  
		v.setOnClickListener(alistener);
	}
}
