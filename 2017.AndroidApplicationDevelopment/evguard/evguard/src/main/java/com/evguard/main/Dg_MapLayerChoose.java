package com.evguard.main;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinghaicom.evguard.R;

public class Dg_MapLayerChoose extends DialogFragment {

	private TextView mTitle;
	private ImageView img_2d;
	private ImageView img_3d;
	private ImageView img_sate;
	private ImageView img_exit;
	private int iMap=1;
	private OnMapChooesdListener mOnMapChooesdListener;

	
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState)  
    {  
	
	 getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);  
        View view = inflater.inflate(R.layout.dg_maplayerchoose, container);  
        createView(view,savedInstanceState);
        return view;  
    }  
	public void createView(View view,Bundle savedInstanceState)
	{
		this.setCancelable(true);
		
		mTitle=(TextView)view.findViewById(R.id.title);
		img_2d=(ImageView)view.findViewById(R.id.img_2d);
		img_3d=(ImageView)view.findViewById(R.id.img_3d);
		img_sate=(ImageView)view.findViewById(R.id.img_sate);
		img_exit=(ImageView)view.findViewById(R.id.img_exit);
		
		mTitle.setText("地图类型");
		img_exit.setOnClickListener(new View.OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		img_2d.setOnClickListener(new View.OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				iMap=0;
				set2d();
				mOnMapChooesdListener.onMapChoosed(iMap);
			}
		});
		img_3d.setOnClickListener(new View.OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				iMap=1;
				set3d();
				mOnMapChooesdListener.onMapChoosed(iMap);
			}
		});
		img_sate.setOnClickListener(new View.OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				iMap=2;
				setSate();
				mOnMapChooesdListener.onMapChoosed(iMap);
			}
		});
		if(iMap==0)
		{
			set2d();
		}
		else if(iMap==1)
		{
			set3d();
		}else if(iMap==2)
		{
			setSate();
		}
	}
	private void set2d()
	{
		img_2d.setBackgroundResource(R.drawable.maplayer_manager_2d_hl);
		img_3d.setBackgroundResource(R.drawable.maplayer_manager_3d);
		img_sate.setBackgroundResource(R.drawable.maplayer_manager_sate);
	}

	private void set3d()
	{
		img_2d.setBackgroundResource(R.drawable.maplayer_manager_2d);
		img_3d.setBackgroundResource(R.drawable.maplayer_manager_3d_hl);
		img_sate.setBackgroundResource(R.drawable.maplayer_manager_sate);
	}
	private void setSate()
	{
		img_2d.setBackgroundResource(R.drawable.maplayer_manager_2d);
		img_3d.setBackgroundResource(R.drawable.maplayer_manager_3d);
		img_sate.setBackgroundResource(R.drawable.maplayer_manager_sate_hl);
	}
	public void setInitMap(int maptype){
		iMap=maptype;
	}
	public void setOnMapChooesdListener(OnMapChooesdListener aOnMapChooesdListener)
	{
		this.mOnMapChooesdListener=aOnMapChooesdListener;
	}
	public interface OnMapChooesdListener
	{
		public void onMapChoosed(int iMap);
	}
}
