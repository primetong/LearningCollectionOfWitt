package com.evguard.customview;

import java.util.ArrayList;
import java.util.List;

import com.evguard.tools.BitmapUtils;
import com.evguard.tools.ConstantTool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
/**
 * 孩子集合控件
 * @author wlh
 * 2015-4-15
 */
public class KidsSetView extends LinearLayout {

	private LinearLayout.LayoutParams params;
	private int ikidcount = 0;
	private float prespace = 10;
	private Context mContext;
	private int iIndex=0;
	private int iCurIndex=-1;//当前选中的customiamgeview的下标
	private int iPreIndex=-1;//保存上一次选中的customiamgview的下标
	private List<String> addedKids = new ArrayList<String>();

	private OnKidSelectedActionListener mOnKidSelectedActionListener;
	private String sCurKidid;

	@SuppressLint("NewApi")
	public KidsSetView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public KidsSetView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
		// TODO Auto-generated constructor stub
	}

	public KidsSetView(Context context) {
		super(context);
		initView(context);
		// TODO Auto-generated constructor stub
	}

	private void initView(Context context) {
		mContext = context;
		params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		this.setPadding(20, 70, 10, 30);
		this.setLayoutParams(params);
		this.setScrollbarFadingEnabled(true);
		this.setVerticalScrollBarEnabled(true);
		this.setVerticalFadingEdgeEnabled(true);
//		this.setVerticalScrollbarPosition(SCROLLBAR_POSITION_RIGHT);
		
		this.setOrientation(LinearLayout.VERTICAL);


	}

	public void setOnKidSelectedActionListener(
			OnKidSelectedActionListener aOnKidSelectedActionListener) {
		this.mOnKidSelectedActionListener = aOnKidSelectedActionListener;
	}

	public void addAKid(String kidid, Bitmap bm) {
		if (addedKids.contains(kidid)) {
			return;
		}
		sCurKidid = kidid;
		addedKids.add(kidid);
		final CustomImageView img = new CustomImageView(mContext);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 20, 0, 20);
		img.setLayoutParams(params);
		img.setImageBitmap(getSmallBitmap(bm));
		img.setIndex(iIndex);
		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(iCurIndex!=img.getIndex())
				{
					iPreIndex=iCurIndex;
					iCurIndex=img.getIndex();
					changeImageState();
				}
				mOnKidSelectedActionListener.OnKidSelectedAction(sCurKidid);
				// AppDataCache.getInstance().setCurrentCar(mKidCurPositionInfo);
			}
		});
		this.addView(img);
		iIndex++;
	}

	private void changeImageState(){
		if(this.getChildCount()==1)return;
		for(int index=0;index<this.getChildCount();index++){
			CustomImageView cib=(CustomImageView)this.getChildAt(index);
			if(iPreIndex==cib.getIndex()){//上一次被选中的imageview下标
				cib.setIsClicked(false);
				cib.postInvalidate();
			}
			else if(cib.getIndex()==iCurIndex ){
				cib.setIsClicked(true);
				cib.postInvalidate();
			}
		}
	}
	private Bitmap getSmallBitmap(Bitmap bm){
		Bitmap newbitmap=bm;
		
		try {
			newbitmap=BitmapUtils.scaleBitmap(bm, ConstantTool.KIDHEADIMGWIDTHMIN,ConstantTool.KIDHEADIMGWIDTHMIN);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newbitmap;
	}
	public interface OnKidSelectedActionListener {
		public void OnKidSelectedAction(String kidid);
	}
}
