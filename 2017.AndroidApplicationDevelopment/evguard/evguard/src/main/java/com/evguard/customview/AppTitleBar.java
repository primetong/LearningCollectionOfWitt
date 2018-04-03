package com.evguard.customview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinghaicom.evguard.R;

/**
 * 
 * @author Administrator
 * 
 */
public class AppTitleBar extends LinearLayout implements View.OnClickListener {

	private static final String TAG = "GpsTitleBar";
	private ImageView img_leftoperate;
//	private ImageView img_head;
	private ImageView img_rightoperate;
	private ImageView img_index_title;
	private TextView tv_title;
	private View layoutView;
	private Context mContext;
	private int iTitleMode = 0;
	private OnTitleActionClickListener mOnTitleActionClickListener;

//	public static final int APPTITLEBARMODE_TXT = 0;
//	public static final int APPTITLEBARMODE_TXTANDLEFTOPERATE = 1;
//	public static final int APPTITLEBARMODE_TXTANDLEFTOPERATE = 1;
//	public static final int APPTITLEBARMODE_TXTANDBACK = 2;
//	public static final int APPTITLEBARMODE_TXTANDBACKANDOPERATE = 3;
//	public static final int APPTITLEBARMODE_TXTANDHEAD= 4;
//	public static final int APPTITLEBARMODE_TXTANDHEADANDOPERATE = 5;
//	public static final int APPTITLEBARMODE_BACKANDOPERATE = 6;
//	public static final int APPTITLEBARMODE_IMGANDBACKANDOPERATE = 7;
//	public static final int APPTITLEBARMODE_TXTANDTXTANDTXT = 8;
	private TextView tv_leftoperate;
	private TextView tv_rightoperate;
	

	public AppTitleBar(Context context, int appType) {
		super(context);
		// Try using the context-application instead of a context-activity
		// to avoid memory leak
		mContext = context;
		initView();
	}

	public AppTitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
	}

	public AppTitleBar(Context context) {
		super(context);
		mContext = context;
		initView();
	}

	// private Animation anim;

	private void initView() {
		if (isInEditMode())
			return;
		// anim = AnimationUtils.loadAnimation(mContext, R.anim.welcome_rotate);
		layoutView = View.inflate(mContext, R.layout.apptitlebar, null);
		LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		layoutView.setLayoutParams(params);
		addView(layoutView);
		img_leftoperate = (ImageView) layoutView.findViewById(R.id.img_leftoperate);
		img_rightoperate = (ImageView) layoutView.findViewById(R.id.img_rightoperate);
		tv_title = (TextView) layoutView.findViewById(R.id.tv_title);
		tv_leftoperate = (TextView) layoutView.findViewById(R.id.tv_leftoperate);
		tv_rightoperate = (TextView) layoutView.findViewById(R.id.tv_rightoperate);
		img_index_title = (ImageView) layoutView.findViewById(R.id.img_index);
		if (isInEditMode())
			return;
		img_leftoperate.setOnClickListener(this);
		img_rightoperate.setOnClickListener(this);
//		img_head.setOnClickListener(this);
		tv_leftoperate.setOnClickListener(this);
		tv_rightoperate.setOnClickListener(this);
		
	}

	/**
	 * imode==0:ֻ只有文本 
	 * imode==1:文本+操作
	 * imode==2:文本+后退
	 * imode==3:文本+后退+操作
	 * imode==4:文本+头像
	 * imode==5:文本+头像+操作
	 * imode==6:后退+操作
	 * imode==7:图片标题+后退+操作
	 * imode==8:后退文本+标题文本+操作文本
	 * @param iMode模式
	 * @param sTile标题
	 * @param operate操作图标
	 */
	public void setTitleMode(String sleftoperate,Drawable leftoperate,
			String sTile, boolean isimgindexvisiable,
			String srightoperate,Drawable rightoperate) {
//		tv_title.setText(sTile);
//		img_rightoperate.setImageDrawable(rightoperate);
		if(sleftoperate!=null){
			tv_leftoperate.setVisibility(View.VISIBLE);
			tv_leftoperate.setText(sleftoperate);
		}else{
			tv_leftoperate.setVisibility(View.GONE);
		}
		if(leftoperate!=null){
			img_leftoperate.setVisibility(View.VISIBLE);
			img_leftoperate.setImageDrawable(leftoperate);
		}else{
			img_leftoperate.setVisibility(View.GONE);
		}
		if(sTile==null ||sTile.equals("")){
			tv_title.setVisibility(View.GONE);
			img_index_title.setVisibility(View.VISIBLE);
		}else{
			tv_title.setVisibility(View.VISIBLE);
			tv_title.setText(sTile);
			img_index_title.setVisibility(View.GONE);
		}
		if(srightoperate!=null){
			tv_rightoperate.setVisibility(View.VISIBLE);
			tv_rightoperate.setText(srightoperate);
		}else{
			tv_rightoperate.setVisibility(View.GONE);
		}
		if(rightoperate!=null){
			img_rightoperate.setVisibility(View.VISIBLE);
			img_rightoperate.setImageDrawable(rightoperate);
		}else{
			img_rightoperate.setVisibility(View.GONE);
		}
		if(isimgindexvisiable){
			img_index_title.setVisibility(View.VISIBLE);
		}else{
			img_index_title.setVisibility(View.GONE);
		}
		
//		switch(iMode){
//		case APPTITLEBARMODE_TXT: {
//			img_leftoperate.setVisibility(View.GONE);
//			img_head.setVisibility(View.GONE);
//			img_rightoperate.setVisibility(View.GONE);
////			img_index_title.setVisibility(View.GONE);
//			tv_leftoperate.setVisibility(View.GONE);
//			tv_rightoperate.setVisibility(View.GONE);
//			break;
//		}
//		case APPTITLEBARMODE_TXTANDOPERATE: {
//			img_leftoperate.setVisibility(View.GONE);
//			img_head.setVisibility(View.GONE);
//			img_rightoperate.setVisibility(View.VISIBLE);
////			img_index_title.setVisibility(View.GONE);
//			tv_leftoperate.setVisibility(View.GONE);
//			tv_rightoperate.setVisibility(View.GONE);
//			break;
//		}
//		case APPTITLEBARMODE_TXTANDBACK: {
//			img_leftoperate.setVisibility(View.VISIBLE);
//			img_head.setVisibility(View.GONE);
//			img_rightoperate.setVisibility(View.GONE);
////			img_index_title.setVisibility(View.GONE);
//			tv_leftoperate.setVisibility(View.GONE);
//			tv_rightoperate.setVisibility(View.GONE);
//			break;
//		}
//		case APPTITLEBARMODE_TXTANDBACKANDOPERATE:{
//			img_leftoperate.setVisibility(View.VISIBLE);
//			img_rightoperate.setVisibility(View.VISIBLE);
//			img_head.setVisibility(View.GONE);
////			img_index_title.setVisibility(View.GONE);
//			tv_leftoperate.setVisibility(View.GONE);
//			tv_rightoperate.setVisibility(View.GONE);
//			break;
//		}
//		case APPTITLEBARMODE_TXTANDHEAD: {
//			img_head.setVisibility(View.VISIBLE);
//			img_leftoperate.setVisibility(View.GONE);
//			img_rightoperate.setVisibility(View.GONE);
////			img_index_title.setVisibility(View.GONE);
//			tv_leftoperate.setVisibility(View.GONE);
//			tv_rightoperate.setVisibility(View.GONE);
//			break;
//		}
//		case APPTITLEBARMODE_TXTANDHEADANDOPERATE:{
//			img_head.setVisibility(View.VISIBLE);
//			img_leftoperate.setVisibility(View.GONE);
//			img_rightoperate.setVisibility(View.VISIBLE);
////			img_index_title.setVisibility(View.GONE);
//			tv_leftoperate.setVisibility(View.GONE);
//			tv_rightoperate.setVisibility(View.GONE);
//			break;
//		}
//		case APPTITLEBARMODE_BACKANDOPERATE:{
//			img_head.setVisibility(View.GONE);
//			img_leftoperate.setVisibility(View.VISIBLE);
//			img_rightoperate.setVisibility(View.VISIBLE);
////			img_index_title.setVisibility(View.GONE);
//			tv_leftoperate.setVisibility(View.GONE);
//			tv_rightoperate.setVisibility(View.GONE);
//			break;
//		}
//		case APPTITLEBARMODE_IMGANDBACKANDOPERATE:{
//			img_head.setVisibility(View.VISIBLE);
//			img_leftoperate.setVisibility(View.GONE);
//			img_index_title.setVisibility(View.VISIBLE);
//			img_rightoperate.setVisibility(View.VISIBLE);
//			tv_title.setVisibility(View.GONE);
//			tv_leftoperate.setVisibility(View.GONE);
//			tv_rightoperate.setVisibility(View.GONE);
//			break;
//		}
//		case APPTITLEBARMODE_TXTANDTXTANDTXT:{
//			img_head.setVisibility(View.GONE);
//			img_leftoperate.setVisibility(View.GONE);
////			img_index_title.setVisibility(View.GONE);
//			img_rightoperate.setVisibility(View.GONE);
//			tv_title.setVisibility(View.VISIBLE);
//			tv_leftoperate.setVisibility(View.VISIBLE);
//			tv_rightoperate.setVisibility(View.VISIBLE);
//			break;
//		}
//		}
	}

	public void setTitleText(String sTile){
		tv_title.setText(sTile);
	}
	public void setBackDrawable(Drawable d) {
		img_leftoperate.setImageDrawable(d);
	}
	public void setOperateDrawable(Drawable d){
		img_rightoperate.setImageDrawable(d);
	}
	public void setHeadDrawable(Drawable d) {
//		img_head.setImageDrawable(d);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_leftoperate:
		case R.id.tv_leftoperate:
			mOnTitleActionClickListener.onLeftOperateClick();
			break;
		/*case R.id.img_left_menu:
			mOnTitleActionClickListener.onHeadClick();
			break;*/
		case R.id.img_rightoperate:
		case R.id.tv_rightoperate:
			mOnTitleActionClickListener.onRightOperateClick();
			break;
		default:
			break;

		}
	}

	public void setOnTitleActionClickListener(
			OnTitleActionClickListener aOnTitleActionClickListener) {
		this.mOnTitleActionClickListener = aOnTitleActionClickListener;
	}

	public interface OnTitleActionClickListener {
		public void onLeftOperateClick();
		public void onTitleClick();
		public void onRightOperateClick();

	}

}
