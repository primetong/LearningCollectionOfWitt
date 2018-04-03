package com.audrey.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class Wave_StanderSin extends View {

	private Context mContext=null;
	
	private Paint mOutArcPaint=new Paint();
	private Paint mInArcPaint=new Paint();
	private Paint mWavePaintA=new Paint();
	private Paint mWavePaintB=new Paint();
	private Paint mBarValuePaint=new Paint();
	private Paint mPercentPaint=new Paint();
	private Paint mTipPaint=new Paint();
	
	private int mOutArcStrokeWidth=10;
	private int mInArcStrokeWidth=0;
	
	private float mBarValueSize=10;
	private float mTipSize=10;
	
	private int mOutArcColor=0xff232b2e;
	private int mInArcColor=0xff2e373e;
	private int mWavePaintColorA=0xff09e271;
	private int mWavePaintColorB=0xff09e271;
	private int mBarValueColor=0xffffffff;
	private int mTipColor=0xffffffff;
	
	private String mTip="正在充电";
	
	
	private float mRadius=100;
	private float mCenterX=0.0f;
	private float mCenterY=0.0f;
	private RectF ovl=null;
	
	private float mWaveStartX=0;
	private float mWaveEndX=0;
	private float mWaveStartY=0;
	private float mWaveEndY=0;
	
	private int mMaxValue=100;
	private int mBarValue=0;
	
	 private Handler mHandler=new Handler() ;
	
	public Wave_StanderSin(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext=context;
		intAttr(attrs);
	}
	public Wave_StanderSin(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		intAttr(attrs);
	}
	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		calRadius();
		calRectF();
		drawArc(canvas);
		drawWave(canvas);
	}
	private void intAttr(AttributeSet attrs){
		initPaint();
	}
	private void initPaint(){
		mOutArcPaint.setAntiAlias(false);
		mOutArcPaint.setStyle(Style.STROKE);
		mOutArcPaint.setStrokeWidth(mOutArcStrokeWidth);
		mOutArcPaint.setColor(mOutArcColor);
		mOutArcPaint.setDither(false);
		
		mInArcPaint.setAntiAlias(false);
		mInArcPaint.setStyle(Style.FILL);
		mInArcPaint.setStrokeWidth(mInArcStrokeWidth);
		mInArcPaint.setColor(mInArcColor);
		
		mWavePaintA.setAntiAlias(false);
		mWavePaintA.setStyle(Style.FILL);
		mWavePaintA.setColor(mWavePaintColorA);
		mWavePaintA.setDither(false);
		
		mWavePaintB.setAntiAlias(false);
		mWavePaintB.setStyle(Style.STROKE);
		mWavePaintB.setColor(mWavePaintColorB);
		
		mBarValuePaint.setAntiAlias(false);
		mBarValuePaint.setStyle(Style.STROKE);
		mBarValuePaint.setColor(mBarValueColor);
		mBarValuePaint.setTextSize(mBarValueSize);
		
		mPercentPaint.setAntiAlias(false);
		mPercentPaint.setStyle(Style.STROKE);
		mPercentPaint.setColor(mBarValueColor);
		mPercentPaint.setTextSize(mBarValueSize/2);
		
		mTipPaint.setAntiAlias(false);
		mTipPaint.setStyle(Style.STROKE);
		mTipPaint.setColor(mTipColor);
		mTipPaint.setTextSize(mTipSize);
		
	}

	private void calRadius(){
		int w=this.getWidth();
		int h=this.getHeight();
		if(w>h){
			mRadius=(h-(2.0f*mOutArcStrokeWidth))/2.0f;
		}else{
			mRadius=(w-(2.0f*mOutArcStrokeWidth))/2.0f;
		}
		mCenterX=w/2.0f;
		mCenterY=(float)(h-mRadius-mOutArcStrokeWidth);
		mWaveStartX=mCenterX-mRadius+mOutArcStrokeWidth;
		mWaveEndX=mCenterX+mRadius-mOutArcStrokeWidth;
		mWaveStartY=mCenterY-mRadius-mOutArcStrokeWidth;
		mWaveEndY=mCenterY+mRadius-mOutArcStrokeWidth;
		
	}
	
	private void calRectF(){
		ovl=new RectF();
		ovl.left=mCenterX-mRadius+mOutArcStrokeWidth;
		ovl.right=mCenterX+mRadius-mOutArcStrokeWidth;
		ovl.top=mCenterY-mRadius+mOutArcStrokeWidth;
		ovl.bottom=mCenterY+mRadius-mOutArcStrokeWidth;

	}
	private void drawArc(Canvas canvas){
		canvas.drawArc(ovl, 0, 360, false, mOutArcPaint);
		canvas.drawArc(ovl, 0, 360, false, mInArcPaint);
//		canvas.drawCircle(mCenterX, mCenterY, mRadius-mOutArcStrokeWidth,mInArcPaint);
	}
	boolean mStarted=false;
	 private long c = 0L;
     private final float f = 0.033F;
     private float mAmplitude = 10.0F; // 振幅
     private float mWateLevel = 0.5F;// 水高(0~1)
     
	private void drawWave(Canvas canvas){
		
//      // 如果未开始（未调用startWave方法）,绘制一个扇形
//      if ((!mStarted) || (mCenterX == 0) || (mCenterY == 0)) {
//           RectF oval = new RectF(mCenterX-mRadius ,
//        		   mCenterY-mRadius,
//                     mCenterX+mRadius,
//                     mCenterY+mRadius);// 设置个新的长方形，扫描测量
//      }
//           canvas.drawArc(ovl, 0, 180, true, mWavePaintA);
//           
		
		
           // 绘制,即水面静止时的高度
			drawStop(canvas);
           if (this.c >= 8388607L) {
               this.c = 0L;
          }
          // 每次onDraw时c都会自增
          c = (1L + c);
           float f1 = this.getWidth() * (1.0F - mWateLevel);
           int top = (int) (f1 + mAmplitude);
           int startX =(int) mWaveStartX;
           while (startX < mWaveEndY) {
              int startY = (int) (f1 - mAmplitude
                        * Math.sin(Math.PI
                                  * (2.0F * (startX + this.c * this.getWidth() * this.f))
                                  / this.getWidth()));
//              canvas.drawLine(startX, startY, startX, top, mWavePaintA);
              startX++;
         }

           canvas.restore();
	}
	
	private void drawStop(Canvas canvas){
		
		float f=mBarValue/mMaxValue*1.00f;
		
		if(f<1/2){
			float param=1-2*mBarValue/mMaxValue*1.00f;
			if(param<-1.0)param=-1.0f;
			else if(param>1.0)param=1.0f;
			 float offsetAngle=(float)(Math.asin(param));
		    canvas.drawArc(ovl,offsetAngle, 180-2*offsetAngle, true, mWavePaintA);
		}else{
			 float offsetAngle=(float) Math.toDegrees(Math.sin(2*mBarValue/mMaxValue*1.00f-1));
			 canvas.drawArc(ovl,360-offsetAngle, 180+2*offsetAngle, true, mWavePaintA);	
		}
        
	}
	 /**
     * @category 开始波动
     */
     public void startWave() {
          if (!mStarted) {
               this.c = 0L;
               mStarted = true;
               mHandler.post(new Runnable(){
   				@Override
   				public void run() {
   					if(mBarValue>=100)mBarValue=0;
   					mBarValue+=10;
   				 postInvalidate();
   				 mHandler.postDelayed(this,60L);
   				}
              });
              
          }
     }

}
