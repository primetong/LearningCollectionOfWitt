package com.evguard.image;

import java.util.List;

import android.content.Context;

import com.evguard.main.CommonAdapter;
import com.evguard.main.CommonViewHolder;
import com.xinghaicom.evguard.R;

public class GridViewAdapter extends CommonAdapter<String>
{

//	private List<String> mFilename=new ArrayList<String>();
	private String mParentDir="";
	
	public GridViewAdapter(Context aContext, List<String> alist, int layoutid,String parentDir) {
		super(aContext, alist, layoutid);
		this.mParentDir=parentDir;
//		mFilename.addAll(alist);
	}

	@Override
	public void convert(CommonViewHolder aCommonViewHolder, String item) {

		// 设置no_pic  
		aCommonViewHolder.setImageResource(R.id.img_scan, R.drawable.friends_sends_pictures_no);  
       
        // 设置图片  
		aCommonViewHolder.setImageByUrl(R.id.img_scan, mParentDir + "/" + item);  
	}
	public String getImageUrlByPostion(int position){
		return this.mParentDir + "/" + getItem(position);
	}
}
