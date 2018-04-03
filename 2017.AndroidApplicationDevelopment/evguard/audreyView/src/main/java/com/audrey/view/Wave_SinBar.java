package com.audrey.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class Wave_SinBar extends View {
private Context mContext=null;
	
	private Paint mOutArcPaint=new Paint();
	private Paint mInArcPaint=new Paint();
	private Paint mWavePaintA=new Paint();
	private Paint mWavePaintB=new Paint();
	private Paint mBarValuePaint=new Paint();
	private Paint mPercentPaint=new Paint();
	private Paint mTipPaint=new Paint();
	
	private int mOutArcStrokeWidth=10;
//	private int mInArcStrokeWidth=0;
	
	private float mBarValueSize=10;
	private float mTipSize=10;
	
	private int mOutArcColor=0xff232b2e;
	private int mInArcColor=0xff2f0000;//0xff2e373e;
	private int mWavePaintColorA=0xff09e271;
	private int mWavePaintColorB=0xff09e271;
	private int mBarValueColor=0xffffffff;
	private int mTipColor=0xffffffff;
	
	private String mTip="正在充电";
	
	
	private float mRadius=100;
	private float mCenterX=0.0f;
	private float mCenterY=0.0f;
	private RectF ovl=null;
	
	private int mWaveStartX=0;
	private int mWaveEndX=0;
	private float mWaveStartY=0;
	private float mWaveEndY=0;
	
	private int mMaxValue=100;
	private int mBarValue=20;
	private float mWateLevel=0.0f;
	
	 private Handler mHandler=new Handler() ;
	
	public Wave_SinBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext=context;
		intAttr(attrs);
	}
	public Wave_SinBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		intAttr(attrs);
	}
	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		calRadius();
		calRectF();
		calPoints();
		drawArc(canvas);
		drawStop(canvas);
		drawWave(canvas);
	}
	private void intAttr(AttributeSet attrs){
		initPaint();
	}
	private void initPaint(){
		mOutArcPaint.setAntiAlias(false);
		mOutArcPaint.setStyle(Style.FILL);
		mOutArcPaint.setStrokeWidth(mOutArcStrokeWidth);//mOutArcStrokeWidth
		mOutArcPaint.setColor(mOutArcColor);
		mOutArcPaint.setDither(false);
		
		mInArcPaint.setAntiAlias(false);
		mInArcPaint.setStyle(Style.FILL);
		mInArcPaint.setStrokeWidth(0);//mInArcStrokeWidth
		mInArcPaint.setColor(mInArcColor);
		
		mWavePaintA.setAntiAlias(false);
		mWavePaintA.setStyle(Style.FILL);
		mInArcPaint.setStrokeWidth(0);
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
		
		
	}
	
	private void calRectF(){
		ovl=new RectF();
		ovl.left=mCenterX-mRadius;
		ovl.right=mCenterX+mRadius;
		ovl.top=mCenterY-mRadius;
		ovl.bottom=mCenterY+mRadius;

	}
	private void drawArc(Canvas canvas){
		canvas.drawCircle(mCenterX, mCenterY, mRadius+mOutArcStrokeWidth,mOutArcPaint);
		canvas.drawCircle(mCenterX, mCenterY, mRadius,mInArcPaint);
	}
	boolean mStarted=false;
	 private long c = 0L;
     private final float f = 0.033F;
     private float mAmplitude = 10.0F; // 振幅

     
     private float[] lineBottoms=null;
     private float[] mYPositions=null;
     private int mLineCount=0;
     float mCycleFactorW = 0;  
     float STRETCH_FACTOR_A=20;
     float OFFSET_Y=0;
     
     private int xStart=0;
     private float hy=0;
     private void calPoints(){
    	
    	 xStart=(int) (mCenterX-mRadius);
    	 mWaveStartX=(int) (mCenterX-Math.sqrt(mRadius*mRadius-(Math.abs(1-mWateLevel*2)*mRadius)*(Math.abs(1-mWateLevel*2)*mRadius)))+mOutArcStrokeWidth/2;
 		 mWaveEndX=(int) (mCenterX+Math.sqrt(mRadius*mRadius-(Math.abs(1-mWateLevel*2)*mRadius)*(Math.abs(1-mWateLevel*2)*mRadius)))-mOutArcStrokeWidth/2;

// 		 Log.i("121", "111:mCenterX:"+mCenterX+"::mRadius："+mRadius);
// 		 
// 		 Log.i("121", "111:mWaveStartX:"+mWaveStartX+"::mWaveEndX："+mWaveEndX);
 		 
 	 	mCycleFactorW=(float) (2 * Math.PI )/this.getWidth()/2;

     }
	private void drawWave(Canvas canvas){
		if (this.c >= 8388607L) {
            this.c = 0L;
       }
        // 每次onDraw时c都会自增
		 c = (1L + c);
		 float f1 = this.getHeight()* (1.0F - mWateLevel);
        int top = (int) (f1 + mAmplitude);
       
//        int startX = this.getWidth() / 2 -  this.getWidth() / 4 ;
//        // 波浪效果
//        while (startX <  this.getWidth() / 2 +  this.getWidth() / 4 ) {
//             int startY = (int) (f1 - mAmplitude
//                       * Math.sin(Math.PI
//                                 * (2.0F * (startX + this.c *  this.getWidth() * this.f))
//                                 /  this.getWidth()));
//             canvas.drawLine(startX, startY, startX, top, mWavePaintA);
//             startX++;
//        }
        
        int startX = mWaveStartX ;
        // 波浪效果
        while (startX< mWaveEndX ) {
        	int startY = (int) (f1 - mAmplitude
                  * Math.sin(Math.PI
                            * (2.0F * (startX + this.c *  this.getWidth() * this.f))
                            /  this.getWidth()));
        	if(mWateLevel>1.0f/2){
             canvas.drawLine(startX, startY+mAmplitude, startX, top, mWavePaintA);
             }else{
            	 canvas.drawLine(startX, startY-mAmplitude, startX, top, mWavePaintA);
             }
             startX++;
        }
        canvas.restore();
	}
	
	private void drawStop(Canvas canvas){
		
		mWateLevel=mBarValue*1.00f/mMaxValue;
		float angle=0;
		if(mWateLevel<1.00f/2){
			float param=1-2*mWateLevel;
		    angle=(float) Math.toDegrees((float) (Math.acos(param)));
		    float offsetAngle=90-angle;
		    canvas.drawArc(ovl,offsetAngle, 2*angle, true, mWavePaintA);
		    Path path= new Path();  
	        path.moveTo(mCenterX, mCenterY);  
	        path.lineTo((float) (mCenterX-Math.sqrt(mRadius*mRadius-(param*mRadius)*(param*mRadius))), mCenterY+param*mRadius);  
	        path.lineTo((float) (mCenterX+Math.sqrt(mRadius*mRadius-(param*mRadius)*(param*mRadius))), mCenterY+param*mRadius);  
	        path.close();  
	        canvas.drawPath(path, mInArcPaint);
	        hy=mCenterY+param*mRadius;
		}else{
			float param=2*mWateLevel*1.00f-1.0f;
			angle=(float) Math.toDegrees((float) (Math.acos(param)));
			float offsetAngle=270+angle;
			canvas.drawArc(ovl,offsetAngle, 360-2*angle, true, mWavePaintA);	
			Path path= new Path();  
	        path.moveTo(mCenterX, mCenterY);  
	        path.lineTo((float) (mCenterX-Math.sqrt(mRadius*mRadius-(param*mRadius)*(param*mRadius))), mCenterY-param*mRadius);  
	        path.lineTo((float) (mCenterX+Math.sqrt(mRadius*mRadius-(param*mRadius)*(param*mRadius))), mCenterY-param*mRadius);  
	        path.close();  
	        canvas.drawPath(path, mWavePaintA);
	        hy=mCenterY-param*mRadius;
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
//   					mBarValue+=10;
   				 postInvalidate();
   				 mHandler.postDelayed(this,60L);
   				}
              });
              
          }
     }
}
