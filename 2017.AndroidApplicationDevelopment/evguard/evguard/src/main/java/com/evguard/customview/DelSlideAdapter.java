package com.evguard.customview;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evguard.model.MessageInfo;
import com.xinghaicom.evguard.R;

public class DelSlideAdapter extends BaseAdapter {

	private Context mContext;
	private List<MessageInfo> mlist = null;

	// private Button curDel_btn = null;

	// private UpdateDate mUpdateDate = null;


	public DelSlideAdapter(Context mContext, List<MessageInfo> mlist) {
		this.mContext = mContext;
		this.mlist = mlist;

	}

	public int getCount() {

		return mlist.size();
	}

	public Object getItem(int pos) {
		return mlist.get(pos);
	}

	public long getItemId(int pos) {
		return pos;
	}

	public View getView(final int pos, View convertView, ViewGroup p) {

		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, null);
			viewHolder = new ViewHolder();
//			viewHolder.name = (TextView) convertView.findViewById(R.id.item_text);
			viewHolder.delete_action = (TextView) convertView.findViewById(R.id.delete_action);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final OnClickListener mOnClickListener = new OnClickListener() {

			@Override
			public void onClick(View view) {
				Log.i("Adapter", "DELETE");
			}
		};
		viewHolder.delete_action.setOnClickListener(mOnClickListener);
//		viewHolder.name.setText(mlist.get(pos));
		return convertView;
	}

	public static class ViewHolder {
		public TextView name;
		public TextView delete_action;

	}
	
}
