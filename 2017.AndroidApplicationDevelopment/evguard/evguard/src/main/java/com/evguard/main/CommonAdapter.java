package com.evguard.main;

import java.util.List;

import com.evguard.tools.LogEx;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
/**
 * 通用listview适配器-泛型
 * @author Administrator
 *
 * @param <T>
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

	private List<T> mList;
	private int mlayoutid=0;
	private Context mContext;
	
	public void setList(List<T> aList)
	{
		this.mList.clear();
		this.mList.addAll(aList);
	}
	public CommonAdapter(Context aContext,List<T> alist,int layoutid)
	{
		this.mList=alist;
		this.mlayoutid=layoutid;
		this.mContext=aContext;
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public T getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommonViewHolder aCommonViewHolder=CommonViewHolder.getViewHolder(mContext, position, convertView, parent, mlayoutid);
		convert(aCommonViewHolder,getItem(position));
		return aCommonViewHolder.getConverView();
	}
	public abstract void convert(CommonViewHolder aCommonViewHolder,T item);

}
