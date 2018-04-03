package com.evguard.image;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.evguard.image.ImageLoader.Type;
import com.evguard.main.CommonViewHolder;

public class ImageViewHolder extends CommonViewHolder {

	private ImageLoader mImageLoader;
	private ImageViewHolder(Context mContext,int position,View convertView,ViewGroup parent,int converViewId)
	{
			super(mContext, position, convertView, parent, converViewId);
			mImageLoader=ImageLoader.getInstance(3, Type.LIFO);
	}
	public void setImageByUrl(int viewId,String url)
	{
		 ImageView view = getView(viewId);  
//	     view.setImageURI(new Uri());
		 mImageLoader.loadImage(url, view);
	}
}
