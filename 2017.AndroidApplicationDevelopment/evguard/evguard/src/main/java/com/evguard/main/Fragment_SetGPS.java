package com.evguard.main;

//import smartbracelet.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xinghaicom.evguard.R;

public class Fragment_SetGPS extends Fragment_Base {

	private TextView tv_SetGPS;
	private TextView tv_NoSetGPS;

	private OnGpsChoiceClickListener mOnGpsChoiceClickListener;
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
	{
		if (layoutView != null) return layoutView;
		layoutView=inflater.inflate(R.layout.fragment_setgps, container,false);
		initView();
		loadListener();
		return layoutView;
	}
	@Override
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState); 
	
	}
	@Override
	public void onDestroy()
	{		
		super.onDestroy();
	}
	private void initView() {
//		anim = AnimationUtils.loadAnimation(mContext, R.anim.welcome_rotate);

		tv_SetGPS = (TextView)layoutView.findViewById(R.id.tv_setgps);
		tv_NoSetGPS = (TextView)layoutView.findViewById(R.id.tv_nosetgps);
	}
	private void loadListener()
	{
		tv_SetGPS.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mOnGpsChoiceClickListener!=null)mOnGpsChoiceClickListener.onActionClick(0);
			}
		});
		tv_NoSetGPS.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mOnGpsChoiceClickListener!=null)mOnGpsChoiceClickListener.onActionClick(1);
			}
			
		});
	}
	public void setOnGpsChoiceClickListener(OnGpsChoiceClickListener aOnGpsChoiceClickListener) {
		// TODO Auto-generated method stub
		this.mOnGpsChoiceClickListener=aOnGpsChoiceClickListener;
	}
	public interface OnGpsChoiceClickListener
	{
		public void onActionClick(int i);
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
