package com.evguard.main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.evguard.customview.DelSlideListView;
import com.evguard.data.AppDataCache;
import com.evguard.main.Dg_Base.OnCancelListener;
import com.evguard.main.Fragment_Energy.CallBack_FragmentEnergy;
import com.evguard.model.ICommonWebResponse;
import com.evguard.model.MessageInfo;
import com.evguard.model.WebReq_GetNotice;
import com.evguard.model.WebRes_GetNotice;
import com.evguard.tools.CommUtils;
import com.xinghaicom.evguard.R;

public class Fragment_Message extends Fragment_Base {

	private static Fragment_Message mInstance;
	private static Object lockobj = new Object();
	private DelSlideListView lvMessage;
	private List<MessageInfo> mMessageList;
	private List<MessageInfo> mList;
	private CommonAdapter<MessageInfo> mAdapter;
	private CallBack_FragmentMessage mCallBack_FragmentMessage;
	
	
	public static Fragment_Message getInstance() {
		if (mInstance == null)
			synchronized (lockobj) {
				mInstance = new Fragment_Message();
			}
		return mInstance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (layoutView != null) {
			return layoutView;
		}
		layoutView = inflater.inflate(R.layout.fragment_message, container,
				false);
		lvMessage = (DelSlideListView) layoutView.findViewById(R.id.lv_message);
		return layoutView;
	}

	@Override
	public void initData() {
		Bundle bundle = this.getArguments();
		final String flag = bundle.getString("flag");
		mMessageList = bundle.getParcelableArrayList("MessageList");
		if (mList == null)
			mList = new ArrayList<MessageInfo>();
		mList.clear();
		for (int i = 0; i < mMessageList.size(); i++) {
			MessageInfo messageInfo = mMessageList.get(i);
			String type = messageInfo.getMsgType();
			if (type.equals(flag)) {
				mList.add(messageInfo);
			}
		}
		Log.i("llj", "mList -- " + mList.size());

		mAdapter = new CommonAdapter<MessageInfo>(mContext, mList,
				R.layout.list_item) {

			@Override
			public void convert(CommonViewHolder aCommonViewHolder,
					final MessageInfo item) {
				if (item.getMsgType().equals(flag)) {
					aCommonViewHolder.setText(R.id.tv_messagetitle,
							item.getTitle());
					aCommonViewHolder.setText(R.id.tv_messagedetail,
							item.getContent());
					String messageTime = item.getTime();
					aCommonViewHolder.setText(R.id.tv_time,
							CommUtils.getShowTime(messageTime));
					if (item.getIsMsgReaded().equals("1")) {
						aCommonViewHolder.setImageVisibility(R.id.iv_icon,
								View.INVISIBLE);
						aCommonViewHolder.setColor(
								R.id.tv_messagetitle,
								getResources().getColor(
										R.color.message_textColor_pressed));
					} else {
						aCommonViewHolder.setImageVisibility(R.id.iv_icon,
								View.VISIBLE);
						aCommonViewHolder.setColor(R.id.tv_messagetitle,
								getResources().getColor(R.color.apptheme));
					}
				}
				aCommonViewHolder.setOnClickListener(R.id.delete_action,new OnClickListener() {
							@Override
							public void onClick(View v) {
								final Dg_Alert mDg_Alert = Dg_Alert.newInstance(
										"删除消息", "是否删除此消息", "确认");
								mDg_Alert.setCancelable(true);
								mDg_Alert.setPositiveButton("确认", new OnClickListener() {

									@Override
									public void onClick(View v) {
										mDg_Alert.dismiss();
										Log.i("llj", "删除！！");
										Iterator<MessageInfo> iter = mList
												.iterator();
										while (iter.hasNext()) {
											if (iter.next().getID() == item.getID()) {
												iter.remove();
											}
										}
										mCallBack_FragmentMessage.deleteMessage(item.getID());
										mAdapter.notifyDataSetChanged();
									}
								});
								mDg_Alert.setOnCancelListener(new OnCancelListener() {
									
									public void onCancle() {
										mDg_Alert.dismiss();
									}
								});
										
								mDg_Alert.show(getChildFragmentManager(), "");
								lvMessage.reset();
							}
				});
			}
		};

		lvMessage.setAdapter(mAdapter);
		lvMessage.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MessageInfo messageInfo = mList.get(position);
				messageInfo.setIsMsgReaded("1");
				mCallBack_FragmentMessage.changeMessageStatus(messageInfo
						.getID());
				mAdapter.notifyDataSetChanged();

				final Dg_Alert mDg_Alert = Dg_Alert.newInstance(
						messageInfo.getTitle(), messageInfo.getContent(), "确认");
				mDg_Alert.setCancelable(true);
				mDg_Alert.setPositiveButton("确认", new OnClickListener() {

					@Override
					public void onClick(View v) {
						mDg_Alert.dismiss();
					}
				});

				mDg_Alert.show(getChildFragmentManager(), "");
			}

		});
	}

	public void setCallBack_FragmentEnergy(
			CallBack_FragmentMessage aOnMessageActionClickListener) {
		mCallBack_FragmentMessage = aOnMessageActionClickListener;
	}

	public interface CallBack_FragmentMessage {
		public abstract void changeMessageStatus(int ID);
		public abstract void deleteMessage(int ID);
	}
	
	@Override  
    public void onDetach() {  
        super.onDetach();  
        try {  
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");  
            childFragmentManager.setAccessible(true);  
            childFragmentManager.set(this, null);  
  
        } catch (NoSuchFieldException e) {  
            throw new RuntimeException(e);  
        } catch (IllegalAccessException e) {  
            throw new RuntimeException(e);  
        }  
      
    }

	@Override
	protected void restoreState(Bundle saveState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Bundle saveState(Bundle saveState) {
		// TODO Auto-generated method stub
		return saveState;
	}  
	
}
