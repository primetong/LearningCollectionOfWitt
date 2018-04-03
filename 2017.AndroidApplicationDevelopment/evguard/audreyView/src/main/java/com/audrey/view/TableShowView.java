package com.audrey.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.audrey.mode.TableShowViewData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TableShowView  extends View{
	private Context mContext=null;
	private int mXstart=0;
	private int mYstart=0;
	private int mXend=0;
	private int mYend=0;
	
	private float mCheckValue=-100;//y值参照
	private float mCheckValueYAxisValueWidth=0;
	private float mYmaxValueYAxisValueWidth=0;
	private float mAxisValueHeight=0;
	
	
	private Paint  mAxisPaint=null;
	private float mAxisWidth=5;
	private int mAxisColor=0xffc1c1c1;
	
	private Paint mDataPaint=null;	
	private float mDataLineWidth=8;
	private int mDataLineColor=0xff00c1fe;
	
	private Paint mAxisVlauePaint=null;
	private float mAxisVlaueSize=30;
	private int mAxisValueColor=0xff73777a;
	
	private int mTableBackColor=0xff2e373e;
	
	private long xValueMax=0;
	private long xValueMin=0;
	private long xIncrease=7;
	private int xIncreaseCount=0;
	private float xStepLong=0;
	
	private float yValueMax=0;
	private float yValueMin=0;
	private float yIncrease=100;
	private float yIncreaseCount=0;
	private float yStepLong=0;
	
	private float mSpanAxisValueWithXAxis=3;
	private float mSpanAxisVlaueWithXEnd=0;
	private float mSpanAxisVlaueWithYEnd=0;
	
	private int mDrawLineSleepTime=30;//ms
	
	//贝塞尔曲线
	private int iCurIndex=0;

	
	private List<TableShowViewData> pSourceData=new ArrayList<TableShowViewData>();
	private List<PointF> pPositionData=new ArrayList<PointF>();
	
	private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	Handler mHanlder=new Handler();
	private ChangePosition DrawLineThrea=null;

	public TableShowView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		initAtrr(attrs);
		initPaint();

	}
	public TableShowView(Context context) {
		super(context);
		mContext=context;
		initPaint();
	}
	public TableShowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext=context;
		initAtrr(attrs);
		initPaint();

	}
	
	@Override
	public void onDraw(Canvas canvas){		
		calcrucial();
		calRect();
		calAxis();
		calAxisCoordinate();
		super.onDraw(canvas);
		if(pPositionData.size()==0)return;
		drawAxisLine(canvas);
		drawDyaLine(canvas);
		
	}


	private void initAtrr(AttributeSet attrs){
		TypedArray typeArray = mContext.getTheme().obtainStyledAttributes(
				attrs, R.styleable.tableshowview, 0, 0);
		
		mAxisWidth=typeArray.getDimension(R.styleable.tableshowview_AxisWidth, mAxisWidth);
		mAxisColor=typeArray.getColor(R.styleable.tableshowview_AxisColor, mAxisColor);
		mDataLineWidth=typeArray.getDimension(R.styleable.tableshowview_DataLineWidth, mDataLineWidth);
		mDataLineColor=typeArray.getColor(R.styleable.tableshowview_DataLineColor, mDataLineColor);
		mAxisVlaueSize=typeArray.getDimension(R.styleable.tableshowview_AxisVlaueSize, mAxisVlaueSize);
		mAxisValueColor=typeArray.getColor(R.styleable.tableshowview_AxisValueColor, mAxisValueColor);
		mTableBackColor=typeArray.getColor(R.styleable.tableshowview_TableBackColor, mTableBackColor);

		if(typeArray!=null)typeArray.recycle();
	}
	private void initPaint(){
		mAxisPaint=new Paint();
		mAxisPaint.setAntiAlias(false);
		mAxisPaint.setStyle(Style.STROKE);
		mAxisPaint.setColor(mAxisColor);
		mAxisPaint.setStrokeWidth(mAxisWidth);
		
		mDataPaint=new Paint();
		mDataPaint.setAntiAlias(false);
		mDataPaint.setStyle(Style.STROKE);
		mDataPaint.setColor(mDataLineColor);
		mDataPaint.setStrokeWidth(mDataLineWidth);
		
		mAxisVlauePaint=new Paint();
		mAxisVlauePaint.setAntiAlias(false);
		mAxisVlauePaint.setStyle(Style.STROKE);
		mAxisVlauePaint.setColor(mAxisValueColor);
		mAxisVlauePaint.setTextSize(mAxisVlaueSize);

	}
	private void calRect(){
		float mXPadding=this.getWidth()/100;
		float mYPadding=this.getHeight()/50;
		mSpanAxisVlaueWithXEnd=mXPadding;
		mSpanAxisVlaueWithYEnd=mYPadding;
		
		if(mCheckValue<0||mCheckValue>yValueMax){
			mCheckValue=(int) (yValueMax/2);
		}
		 mCheckValueYAxisValueWidth = mAxisVlauePaint.measureText(mCheckValue+"", 0,(mCheckValue+"").length());
		 mYmaxValueYAxisValueWidth= mAxisVlauePaint.measureText(yValueMax+"", 0,(yValueMax+"").length());
		 FontMetrics fmAxis = mAxisVlauePaint.getFontMetrics();
		  mAxisValueHeight =  (float) Math.ceil(fmAxis.descent - fmAxis.ascent);
		 
		mXstart= (int) (mXPadding+3*mCheckValueYAxisValueWidth);
		mYstart=(int) mYPadding;//(int) (this.getHeight()-mYPadding);

		mXend=(int) (this.getWidth()-mSpanAxisVlaueWithXEnd-mXstart);
		mYend=(int) (this.getHeight()-mYPadding-2*mAxisValueHeight);
	}

	private void drawAxisLine(Canvas canvas){
//		Log.i("121","***:mXstart:"+mXstart+"::mYstart:"+mYstart+"::mXstart:"+mXstart+"::mYend:"+mYend);
		canvas.drawColor(mTableBackColor);
		canvas.drawLine(mXstart, mYstart-mSpanAxisVlaueWithYEnd, mXstart, mYend+mAxisWidth/2, mAxisPaint);
		canvas.drawLine(mXstart, mYend, mXend+mSpanAxisVlaueWithXEnd, mYend, mAxisPaint);
//		canvas.drawLine(mXstart, mYend-mCheckValue*yStepLong, mXend, mYend-mCheckValue*yStepLong, mAxisPaint);
	
		int xShowValue=0;
		float xValue=0;
	    FontMetrics fmAxis = mAxisVlauePaint.getFontMetrics();
	    mAxisValueHeight =  (float) Math.ceil(fmAxis.descent - fmAxis.ascent);
		 
		 //只画x轴起始点
		 Date dmin=new Date(xValueMin);
		 String minVlaue=format.format(dmin);
		 canvas.drawText(minVlaue,mXstart-mCheckValueYAxisValueWidth, mYend+mSpanAxisValueWithXAxis+2*mAxisWidth+3*mAxisValueHeight/2, mAxisVlauePaint);
		 
		 if(xValueMax-xValueMin!=0){//若只有一个点则只画一个
			 Date dmax=new Date(xValueMax);
			 String maxVlaue=format.format(dmax);
			 float txtwidth =  mAxisVlauePaint.measureText(maxVlaue,0,maxVlaue.length());
			 canvas.drawText(maxVlaue,mXend-txtwidth, mYend+mSpanAxisValueWithXAxis+mAxisWidth+3*mAxisValueHeight/2, mAxisVlauePaint);
		 }
//		for(int i=0;i<xIncreaseCount+1;i++){
//			xShowValue=xValueMin+i*xIncrease;
//			xShowValue=xShowValue>xValueMax?xValueMax:xShowValue;
//			if(xShowValue>=xValueMax){
//				if((xValueMax-xValueMin)%xIncrease==0){
//					xValue=mXstart+i*xIncrease*xStepLong;
//				}else{
//					xValue= mXstart+(i-1)*xIncrease*xStepLong+((xValueMax-xValueMin)%xIncrease)*xStepLong;
//				}
//
//			}else{
//				xValue=mXstart+i*xIncrease*xStepLong;
//			}
//			canvas.drawText(xShowValue+"",xValue, mYend+mSpanAxisValueWithXAxis+2*mAxisWidth+mTxtHeight2/2, mAxisVlauePaint);
//		}
		canvas.drawText((int)mCheckValue+"",mXstart-3*mCheckValueYAxisValueWidth/2, mYend-mCheckValue*yStepLong+mAxisWidth/2, mAxisVlauePaint);
		canvas.drawText((int)yValueMax+"",mXstart-3*mYmaxValueYAxisValueWidth/2, mYend-yValueMax*yStepLong+mAxisWidth/2+mAxisValueHeight/2, mAxisVlauePaint);
	}
	/**
	 * 计算x\Y轴的临界值
	 */
	private void calcrucial(){
	
		for(int p=0;p<pSourceData.size();p++){
			TableShowViewData aPointF=pSourceData.get(p);
			if(xValueMax<aPointF.x||xValueMax==0){
				xValueMax=aPointF.x;
			}
			if(xValueMin>aPointF.x ||xValueMin==0){
				xValueMin= aPointF.x;
			}
			if(yValueMax<aPointF.y||yValueMax==0){
				yValueMax= aPointF.y;
			}
			if(yValueMin>aPointF.y||yValueMin==0){
				yValueMin= aPointF.y;
			}
		}
//	 Log.i("121","***:xValueMax:"+xValueMax+"::xValueMin:"+xValueMin+"::yValueMax:"+yValueMax+"::yValueMin:"+yValueMin);
	}
	/**
	 * 计算各个数据的x坐标
	 */
	private void calAxisCoordinate(){
		pPositionData.clear();
		for(int p=0;p<pSourceData.size();p++){
			TableShowViewData aPointF=pSourceData.get(p);
			float x=mXstart+(aPointF.x-xValueMin)*xStepLong;
			float y=mYend-(aPointF.y)*yStepLong;
			PointF newPointF=new PointF(x,y);
			pPositionData.add(newPointF);
//			Log.i("121","mXstart::"+mXstart+ ":::p:x:+"+x+"::y:"+y+"::::");
		}
	}
	/**
	 * 计算坐标轴应显示的坐标个数和实际步长
	 */
	private void calAxis(){
		if(xValueMax-xValueMin==0){
			xIncreaseCount=1;
			 xStepLong=(mXend-mXstart)*1.00f;
			 yIncreaseCount=1;
			 yStepLong=(mYend-mYstart)*1.00f/yValueMax;
		}else{
			 xIncreaseCount=(int) ((xValueMax-xValueMin)%xIncrease>0?(xValueMax-xValueMin)/xIncrease+1:(xValueMax-xValueMin)/xIncrease);
			 xStepLong=(mXend-mXstart)*1.00f/((xValueMax-xValueMin));
			
			 yIncreaseCount=(yValueMax-yValueMin)%yIncrease>0?(yValueMax-yValueMin)/yIncrease+1:(yValueMax-yValueMin)/yIncrease;
//			 yStepLong=(mYend-mYstart-mSpanAxisVlaueWithYEnd)*1.00f/((yValueMax-yValueMin));
			 yStepLong=(mYend-mYstart)*1.00f/((yValueMax));
		}
	}
	

	 PointF startp;  
	 PointF endp;  
	 PointF p3 = new PointF();  
	 PointF p4 = new PointF();  
	private void drawDyaLine(Canvas canvas){
		if(pPositionData.size()==1){
			canvas.drawPoint(pPositionData.get(0).x,pPositionData.get(0).y,mDataPaint);
			return;
		}
		Path mPath=new Path();
		for(int i=0;i<iCurIndex;i++){
			PointF aPointF=pPositionData.get(i);
			if(i==0){
				mPath.moveTo(aPointF.x, aPointF.y);
			}
			else{
//	            startp = aPointF;  
//	            endp = pPositionData.get(i+1);      
				startp = pPositionData.get(i-1);  
		        endp = aPointF; 
	            float xCenter = (int)(startp.x + endp.x) / 2;  
	            float yCenter = (int)(startp.y + endp.y) / 2;  
	            p3.y = startp.y;  
	            p3.x = xCenter;  
	            p4.y = endp.y;  
	            p4.x = xCenter;  
	            // 确定曲线的路径        
	            mPath.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);  
	              
	            // path.quadTo(xCenter, yCenter,  endp.x, endp.y);  
			}

		}
		canvas.drawPath(mPath, mDataPaint);
	}
	/**
	 * 外部设置步长
	 * @param xincrease
	 * @param yincrease
	 */
	public void setIncrease(long xincrease,long yincrease){
		this.xIncrease=xincrease;
		this.yIncrease=yincrease;
	}
	/**
	 * 设置y的参照值
	 * @param f
	 */
	public void setYCheckValue(float f){
//		this.mCheckValue=f;
	}
	
	public void setDrawLineSleepTime(int itime){
		mDrawLineSleepTime=itime;
	}
	public void setData(List<TableShowViewData> alist){
		if(DrawLineThrea!=null){
			DrawLineThrea.mIsReset=true;
			try {
				Thread.sleep(mDrawLineSleepTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DrawLineThrea=null;
		}
		//重新设置数据后界点需要重新计算
		 xValueMax=0;
		 xValueMin=0;

		 yValueMax=0;
		 yValueMin=0;

		iCurIndex=0;
		
		pSourceData.clear();
		pPositionData.clear();
		this.invalidate();
		pSourceData.addAll(alist);
		
		
		
		DrawLineThrea=new ChangePosition();
		DrawLineThrea.start();

	}

	 class ChangePosition extends Thread{
		 public boolean mIsReset=false;
		 @Override  
		    public void run() {  
		        synchronized (this) {  
		        	while(iCurIndex<pSourceData.size()){
						if(mIsReset){
							break;
						}
						postInvalidate();
						iCurIndex++;
						try {
							Thread.sleep(mDrawLineSleepTime);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
		        }  
		  
		    }  
		 
	 }
	 
}
