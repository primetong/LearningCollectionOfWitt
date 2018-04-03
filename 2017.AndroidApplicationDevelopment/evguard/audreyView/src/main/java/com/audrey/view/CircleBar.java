package com.audrey.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class CircleBar extends View {



	private Context mContext=null;
	private Paint mCircleBottomPaint=new Paint();
	private Paint mCirclePaint=new Paint();
	private Paint mTextValuePaint=new Paint();
	private Paint mTextTipPaint=new Paint();
	private Paint mTextTitlePaint=new Paint();
	
	private int mCircleBottomColor=0xff4a5861;
	private int mCircleColor=0xfff5a800;
	private int mTextTipColor=0xff57616a;
	private int mTextValueColor=0xfff5a800;
	
	private float mCircleBottomStorkeWidth=10;
	private float mCircleStorkeWidth=10;
	
	private float mTextValueSize=15;
	private float mTextTipSize=15;
	
	private String mTextTip="次";
	private String mTextTitle;
	private int mTextTitleColor = 0xff97b1c2;
	private float mTextTitleSize = 15;
	
	private int CircleMaxValue=50;
	private int mBarValue=0;
	
	private float satrtAngle=0f;//圆弧的起始角度值

	private float centerX=0;
	private float centerY=0;
	private float mRadius=25;
	private float mPercentDegree=0;
	private RectF oval =null;//绘制圆弧的区域
	private  FontMetrics fmValue =null; 
	private  FontMetrics fmTip =null;
	private FontMetrics fmTitle; 
	
	public CircleBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext=context;
		initAttr(attrs);
	}
	public CircleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		initAttr(attrs);
	}
	
	private void initAttr(AttributeSet attrs){
		TypedArray typeArray = mContext.getTheme().obtainStyledAttributes(attrs,  
                R.styleable.CircleBar, 0, 0);  
		mCircleBottomColor=typeArray.getColor(R.styleable.CircleBar_CircleBottomColor, mCircleBottomColor);
		mCircleColor=typeArray.getColor(R.styleable.CircleBar_CircleColor, mCircleColor);
		mTextTipColor=typeArray.getColor(R.styleable.CircleBar_TextTipColor, mTextTipColor);
		mTextValueColor=typeArray.getColor(R.styleable.CircleBar_TextValueColor, mTextValueColor);
		
		mTextTitle=typeArray.getString(R.styleable.CircleBar_TextTitle);
		mTextTitleColor = typeArray.getColor(R.styleable.CircleBar_TextTitleColor, mTextTitleColor);
		mTextTitleSize=typeArray.getDimension(R.styleable.CircleBar_TextTitleSize, mTextTitleSize);
		
		mCircleBottomStorkeWidth=typeArray.getDimension(R.styleable.CircleBar_CircleBottomStorkeWidth, mCircleBottomStorkeWidth);
		mCircleStorkeWidth=typeArray.getDimension(R.styleable.CircleBar_CircleStorkeWidth, mCircleStorkeWidth);
		mTextValueSize=typeArray.getDimension(R.styleable.CircleBar_TextValueSize, mTextValueSize);
		mTextTipSize=typeArray.getDimension(R.styleable.CircleBar_TextTipSize, mTextValueSize);
		setCircleMaxValue(typeArray.getInt(R.styleable.CircleBar_CircleMaxValue, getCircleMaxValue()));
		
		if(typeArray!=null)typeArray.recycle();
		initPaint();
	}
	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		calRadius();
		calRetF();
		 canvas.drawArc(oval, satrtAngle,360, false, mCircleBottomPaint); 
		if (mBarValue >= 0 ) {  
            canvas.drawArc(oval, satrtAngle, calAngle(mBarValue), false, mCirclePaint); //  
            drawBarValue(canvas);
		}  
	}
	
	private void initPaint(){
		mCircleBottomPaint.setAntiAlias(false);
		mCircleBottomPaint.setStrokeWidth(mCircleBottomStorkeWidth);
		mCircleBottomPaint.setStyle(Style.STROKE);
		mCircleBottomPaint.setColor(mCircleBottomColor);
		
		mCirclePaint.setAntiAlias(false);
		mCirclePaint.setStrokeWidth(mCircleStorkeWidth);
		mCirclePaint.setStyle(Style.STROKE);
		mCirclePaint.setColor(mCircleColor);
		
		mTextValuePaint.setAntiAlias(false);
		mTextValuePaint.setStyle(Style.FILL);
		mTextValuePaint.setColor(mTextValueColor);
		mTextValuePaint.setTextSize(mTextValueSize);
		
		mTextTipPaint.setAntiAlias(false);
		mTextTipPaint.setStyle(Style.FILL);
		mTextTipPaint.setColor(mTextTipColor);
		mTextTipPaint.setTextSize(mTextTipSize);
		
		mTextTitlePaint.setAntiAlias(false);
		mTextTitlePaint.setStyle(Style.FILL);
		mTextTitlePaint.setColor(mTextTitleColor);
		mTextTitlePaint.setTextSize(mTextTitleSize);
	}
	private void calRadius(){
		int w=this.getWidth();
		int h=this.getHeight();
		float ipadding=mCircleBottomStorkeWidth>mCircleStorkeWidth?mCircleBottomStorkeWidth:mCircleStorkeWidth;
		int factwidth=(int) (w-2*ipadding);
		int factheight=(int) (h-2*ipadding);
		
		if(factwidth>factheight){
			mRadius=factheight/2;
		}else{
			mRadius=factwidth/2;
		}
		centerX=w/2;
		centerY=h/2;
		
		mPercentDegree=360f/getCircleMaxValue();
		satrtAngle=270;
	}
	private void calRetF(){
		 oval = new RectF();  
        oval.left = (centerX - mRadius);  
        oval.top = (centerY - mRadius);  
        oval.right = centerX + mRadius;//mCircleRadius * 2 + (centerWidth - mCircleRadius);  
        oval.bottom = mRadius * 2 + (centerY - mRadius);  
	}
	private float calAngle(int ivalue){
		return ivalue*mPercentDegree;
	}
	private void drawBarValue(Canvas canvas){
		 fmValue=mTextValuePaint.getFontMetrics(); 
		  String txtBarValue = mBarValue + "";  
         float mTxtHeight = (int) Math.ceil(fmValue.descent - fmValue.ascent); 
         float mTxtWidth = mTextValuePaint.measureText(txtBarValue, 0, txtBarValue.length());  
         canvas.drawText(txtBarValue, centerX - mTxtWidth / 2, centerY +mTxtHeight / 4, mTextValuePaint);  
        
         fmTip=mTextTipPaint.getFontMetrics();
         int mTxtHeight2 = (int) Math.ceil(fmTip.descent - fmTip.ascent); 
         float mTxtWidth2 = mTextValuePaint.measureText(mTextTip, 0, mTextTip.length());  
         canvas.drawText(mTextTip, centerX-mTxtWidth2/4 , centerY +3*mTxtHeight/4+mTxtHeight2/4, mTextTipPaint); 
         
         fmTitle = mTextTitlePaint.getFontMetrics();
         int mTxtHeight3 = (int) Math.ceil(fmTitle.descent - fmTitle.ascent); 
         float mTxtWidth3 = mTextValuePaint.measureText(mTextTitle, 0, mTextTitle.length());  
         canvas.drawText(mTextTitle, centerX-mTxtWidth3/4, 
        		 centerY + mRadius + 3*mCircleStorkeWidth, mTextTitlePaint);
	}
	public void setBarValue(int iValue){
		this.mBarValue=iValue;
		this.postInvalidate();
	}
	public int getBarValue(){
		return this.mBarValue;
	}
	public int getCircleMaxValue() {
		return CircleMaxValue;
	}
	public void setCircleMaxValue(int circleMaxValue) {
		CircleMaxValue = circleMaxValue;
	}

	
	
}
