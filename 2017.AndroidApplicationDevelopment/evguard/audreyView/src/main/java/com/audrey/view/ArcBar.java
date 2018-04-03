package com.audrey.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class ArcBar extends View {

	private Context mContext=null;
	private Paint mArcBottom=new Paint();
	private Paint mArcPaint=new Paint();
	private Paint mTipPaint=new Paint();
	private Paint mBarValuePaint=new Paint();
	private Paint mPercentPaint=new Paint();
	private Paint mPaintCircleBottom=new Paint();//底部圆弧两个小圆角画笔
	private Paint mPaintCircle=new Paint();//高亮圆弧两个小圆角画笔
	
	private int mArcBottomColor=0xff22292f;//底部弧形颜色
	private int mArcColor=0xff09e271;//高亮弧形颜色
	private int mTipColor=0xff57616a;//提示字样颜色
	private int mBarValueColor=0xffebebeb;//主字样颜色
	
	private float mArcStrokeWidth=15;//高亮弧形线条宽度
	private float mArcBottomStrokeWidth=10;//底部弧形线条宽度
	
	private float mBarValueTextSize=150;//主字样字体大小
	private float mTipTextSize=30;//提示字样字体大小
	
	private String tip="剩余电量";//提示字样
	
	private int mMaxBarValue=100;//最大进度值
	private int mMinBarValue=0;//最小进度值
	private int mBarValue=0;//进度值
	
	private float mArcRadius=100;//圆弧半径
	private float centerX=0;//圆弧中心点
	private float centerY=0;//圆弧中心点
	
	protected float mFloatScale=3f/5;//经过圆弧起始角度点的割线和半径组成角的正弦值
	private float minAngle=0f;//圆弧的最小角度值
	private float maxAngle=0;//圆弧的最大角度值
	private float offSetAngle=0;//偏移角度值
	private float mPerDegreeValue=0;//每进度值所占的度数
	private RectF oval =null;//绘制圆弧的区域
	private  FontMetrics fmValue =null; 
	private  FontMetrics fmTip =null; 
	
	
	public ArcBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext=context;
		initAttr(attrs);
	}
	public ArcBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		initAttr(attrs);
	}


	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		calRadius();
		calRetF();
		drawBottomArc(canvas);
		if (mBarValue >= 0 ) {  
			drawArc(canvas);
             drawBarValue(canvas);
             drawTip(canvas); 
		}  
	}
	

	protected void initAttr(AttributeSet attrs){
		TypedArray typeArray = mContext.getTheme().obtainStyledAttributes(attrs,  
                R.styleable.ArcBar, 0, 0);  

		mArcBottomStrokeWidth = typeArray.getDimension(R.styleable.ArcBar_ArcBottomStrokeWidth, mArcBottomStrokeWidth);  
		mArcBottomColor = typeArray.getColor(R.styleable.ArcBar_ArcBottomColor,mArcBottomColor);
		mArcStrokeWidth = typeArray.getDimension(R.styleable.ArcBar_ArcStrokeWidth, mArcStrokeWidth);  
		mArcColor = typeArray.getColor(R.styleable.ArcBar_ArcColor, mArcColor);  
		mTipColor = typeArray.getColor(R.styleable.ArcBar_ArcTipColor, mTipColor);  
		tip=typeArray.getString(R.styleable.ArcBar_ArcTip);
		mBarValueColor=typeArray.getColor(R.styleable.ArcBar_ArcBarValueColor, mBarValueColor);
		mBarValueTextSize=typeArray.getDimension(R.styleable.ArcBar_ArcBarValueTextSize, mBarValueTextSize);
		mTipTextSize=typeArray.getDimension(R.styleable.ArcBar_ArcTipTextSize, mTipTextSize);
		mMaxBarValue=typeArray.getInt(R.styleable.ArcBar_ArcMaxValue, mMaxBarValue);
		mMinBarValue=typeArray.getInt(R.styleable.ArcBar_ArcMinValue, mMinBarValue);
		if(typeArray!=null)typeArray.recycle();
		initPaint();
 
	}
	private void initPaint(){
		mArcBottom.setColor(mArcBottomColor);
		mArcBottom.setStyle(Style.STROKE);
		mArcBottom.setAntiAlias(false);
		mArcBottom.setStrokeWidth(mArcBottomStrokeWidth);
		
		mArcPaint.setColor(mArcColor);
		mArcPaint.setStyle(Style.STROKE);
		mArcPaint.setAntiAlias(false);
		mArcPaint.setStrokeWidth(mArcStrokeWidth);
		
		mTipPaint.setColor(mTipColor);
		mTipPaint.setStyle(Style.FILL);
		mTipPaint.setAntiAlias(false);
		mTipPaint.setTextSize(mTipTextSize);
		mTipPaint.setStrokeWidth(mArcStrokeWidth);
		
		mBarValuePaint.setColor(mBarValueColor);
		mBarValuePaint.setStyle(Style.FILL);
		mBarValuePaint.setAntiAlias(false);
		mBarValuePaint.setTextSize(mBarValueTextSize);
		
		mPercentPaint.setColor(mBarValueColor);
		mPercentPaint.setStyle(Style.FILL);
		mPercentPaint.setAntiAlias(false);
		mPercentPaint.setTextSize(mBarValueTextSize/2);
		
		
		 mPaintCircleBottom = new Paint();
		 mPaintCircleBottom.setAntiAlias(false);
		 mPaintCircleBottom.setColor(mArcBottomColor);
		 
		 mPaintCircle = new Paint();
		 mPaintCircle.setAntiAlias(false);
		 mPaintCircle.setColor(mArcColor);
	}
	private void calRadius(){
		int width=this.getWidth();
		int height=this.getHeight();
		float ipadding=mArcBottomStrokeWidth>mArcStrokeWidth?mArcBottomStrokeWidth:mArcStrokeWidth;
		
		float factwidth=(width-2.0f*ipadding);
		float factheight= (height-2.0f*ipadding);
		
		if(factwidth>factheight){
			mArcRadius=factheight/2;
		}else{
			mArcRadius=factwidth/2;
		}
		centerX=(float)width/2;
		centerY=(float) (height-mArcRadius-ipadding);//(int) (height-mBottom-mFloatScale*mCircleRadius);
		offSetAngle=(float) Math.toDegrees(Math.acos(mFloatScale));
		minAngle=(float) (90+offSetAngle);
		maxAngle=360+(90-offSetAngle);
		mPerDegreeValue=(360-2*offSetAngle)/100f;
	}
	private void calRetF(){
		 oval = new RectF();  
         oval.left = (centerX - mArcRadius);  
         oval.top = (centerY - mArcRadius);  
         oval.right = centerX + mArcRadius;//mCircleRadius * 2 + (centerWidth - mCircleRadius);  
         oval.bottom = mArcRadius * 2 + (centerY - mArcRadius);  
	}
	private float calAngle(int ivalue){
		return ivalue*mPerDegreeValue;
	}
	
	private void drawBottomArc(Canvas canvas){
		 canvas.drawArc(oval, minAngle, maxAngle-minAngle, false, mArcBottom); 
	     canvas.drawCircle(
	                (float) (centerX + mArcRadius * Math.cos(minAngle * 3.14 / 180)),
	                (float) (centerY + mArcRadius * Math.sin(minAngle * 3.14 / 180)),
	                mArcBottomStrokeWidth / 2, mPaintCircleBottom);// 小圆
	 
	      canvas.drawCircle(
	                (float) (centerX + mArcRadius
	                        * Math.cos((180 - minAngle) * 3.14 / 180)),
	                (float) (centerY + mArcRadius
	                        * Math.sin((180 - minAngle) * 3.14 / 180)),
	                        mArcBottomStrokeWidth / 2, mPaintCircleBottom);// 小圆
	}
	private void drawArc(Canvas canvas){
		 canvas.drawArc(oval, minAngle, calAngle(mBarValue), false, mArcPaint); //  
	     canvas.drawCircle(
	                (float) (centerX + mArcRadius * Math.cos(minAngle * 3.14 / 180)),
	                (float) (centerY + mArcRadius * Math.sin(minAngle * 3.14 / 180)),
	                mArcStrokeWidth / 2, mPaintCircle);// 小圆
	      canvas.drawCircle(
	                (float) (centerX + mArcRadius
	                        * Math.cos((calAngle(mBarValue)+minAngle) * 3.14 / 180)),
	                (float) (centerY + mArcRadius
	                        * Math.sin((calAngle(mBarValue)+minAngle) * 3.14 / 180)),
	                        mArcStrokeWidth / 2, mPaintCircle);// 小圆
	}
	
	private void drawBarValue(Canvas canvas){
		  fmValue=mBarValuePaint.getFontMetrics();  
		  String txtBarValue = mBarValue + "";  
          float mTxtHeight = (int) Math.ceil(fmValue.descent - fmValue.ascent); 
          float mTxtWidth = mBarValuePaint.measureText(txtBarValue, 0, txtBarValue.length());  
          canvas.drawText(txtBarValue, centerX - mTxtWidth / 2, centerY + mTxtHeight / 4, mBarValuePaint);  
         
          fmTip=mTipPaint.getFontMetrics();
          FontMetrics fm2 = mPercentPaint.getFontMetrics(); 
          int mTxtHeight2 = (int) Math.ceil(fm2.descent - fm2.ascent); 
          canvas.drawText("%", centerX + mTxtWidth / 2+5, centerY + mTxtHeight2 / 2, mPercentPaint);  
	}
	private void drawTip(Canvas canvas){
		float w=0,h=0;
		w=(float) Math.sqrt(mArcRadius*mArcRadius-(mFloatScale*mArcRadius)*(mFloatScale*mArcRadius));
		h=mFloatScale*mArcRadius;
		float mTxtHeight,mTxtWidth=0.0f;
		mTxtHeight= (int) Math.ceil(fmTip.descent - fmTip.ascent); 
		
		String tip1=mMinBarValue+"";
        mTxtWidth = mTipPaint.measureText(tip1, 0, tip1.length()); 
		canvas.drawText(tip1, centerX -w- mTxtWidth / 2, centerY + h+mTxtHeight , mTipPaint);  
		
		String tip2=mMaxBarValue+"";
		mTxtWidth = mTipPaint.measureText(tip2, 0, tip2.length());
		canvas.drawText(tip2, centerX +w- mTxtWidth / 2, centerY + h+mTxtHeight , mTipPaint); 
		
		String tip3=tip==null?"":tip;
		mTxtWidth = mTipPaint.measureText(tip3, 0, tip3.length());
		canvas.drawText(tip3, centerX - mTxtWidth / 2, centerY + h , mTipPaint); 
	}
	public void setBarValue(int iValue){
		this.mBarValue=iValue;
		this.postInvalidate();
	}
	public int getBarValue(){
		return this.mBarValue;
	}
}
