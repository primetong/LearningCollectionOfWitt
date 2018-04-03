package com.audrey.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.audrey.mode.TableShowViewData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;

public class TableShowSurfaceview extends SurfaceView implements
		SurfaceHolder.Callback {

	private SurfaceHolder surfaceholder = null;
	private Context mContext = null;
	private int mXstart = 0;
	private int mYstart = 0;
	private int mXend = 0;
	private int mYend = 0;

	private float mCheckValue = -100;// y值参照
	private float mCheckValueYAxisValueWidth = 0;
	private float mYmaxValueYAxisValueWidth = 0;
	private float mAxisValueHeight = 0;

	private Paint mAxisPaint = null;
	private float mAxisWidth = 5;
	private int mAxisColor = 0xffc1c1c1;

	private Paint mDataPaint = null;
	private float mDataLineWidth = 8;
	private int mDataLineColor = 0xff00c1fe;

	private Paint mAxisVlauePaint = null;
	private float mAxisVlaueSize = 30;
	private int mAxisValueColor = 0xff73777a;

	private int mTableBackColor = 0xff2e373e;

	private long xValueMax = 0;
	private long xValueMin = 0;
	private long xIncrease = 7;
	private int xIncreaseCount = 0;
	private float xStepLong = 0;

	private float yValueMax = 0;
	private float yValueMin = 0;
	private float yIncrease = 100;
	private float yIncreaseCount = 0;
	private float yStepLong = 0;

	private float mSpanAxisValueWithXAxis = 3;
	private float mSpanAxisVlaueWithXEnd = 0;
	private float mSpanAxisVlaueWithYEnd = 0;

	private int mDrawLineSleepTime = 0;// ms
	private boolean bIsDynamicDraw = false;

	Semaphore semp = new Semaphore(1);

	// 贝塞尔曲线
	private int iCurIndex = 0;
	private boolean bIsBreak=false;

	private List<TableShowViewData> pSourceData = new ArrayList<TableShowViewData>();
	private List<PointF> pPositionData = new ArrayList<PointF>();

	private SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private int mSurfaceBackColor = 0xff2e373e;
	private DrawThread tDraw = null;
	private ISurfaceEvent mISurfaceEvent = null;

	public TableShowSurfaceview(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initAtrr(attrs);
		initPaint();
		init();
	}

	public TableShowSurfaceview(Context context) {
		super(context);
		mContext = context;
		initPaint();
		init();
	}

	public TableShowSurfaceview(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initAtrr(attrs);
		initPaint();
		init();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		surfaceholder = holder;
		this.setBackgroundColor(Color.TRANSPARENT);
		drawBackgroud();
		try {
			semp.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		tDraw = new DrawThread();
		tDraw.start();
		if (mISurfaceEvent != null)
			mISurfaceEvent.onSurfaceViewCreated();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		this.setBackgroundColor(mTableBackColor);

		xValueMax = 0;
		xValueMin = 0;

		yValueMax = 0;
		yValueMin = 0;

		iCurIndex = 0;

		pSourceData.clear();
		pPositionData.clear();
		tDraw.bIsStop = true;
		semp.release();

	}

	private void init() {
		surfaceholder = this.getHolder();
		surfaceholder.addCallback(this);
//		this.setBackgroundColor(mTableBackColor);//防止surfaceview切换时黑屏，同时所在的activity需要设置getWindow().setFormat(PixelFormat.TRANSLUCENT);
//		this.setZOrderOnTop(true);
		surfaceholder.setFormat(PixelFormat.TRANSLUCENT);
		
	}

	private void initAtrr(AttributeSet attrs) {
		TypedArray typeArray = mContext.getTheme().obtainStyledAttributes(
				attrs, R.styleable.tableshowview, 0, 0);

		mAxisWidth = typeArray.getDimension(
				R.styleable.tableshowview_AxisWidth, mAxisWidth);
		mAxisColor = typeArray.getColor(R.styleable.tableshowview_AxisColor,
				mAxisColor);
		mDataLineWidth = typeArray.getDimension(
				R.styleable.tableshowview_DataLineWidth, mDataLineWidth);
		mDataLineColor = typeArray.getColor(
				R.styleable.tableshowview_DataLineColor, mDataLineColor);
		mAxisVlaueSize = typeArray.getDimension(
				R.styleable.tableshowview_AxisVlaueSize, mAxisVlaueSize);
		mAxisValueColor = typeArray.getColor(
				R.styleable.tableshowview_AxisValueColor, mAxisValueColor);
		mTableBackColor = typeArray.getColor(
				R.styleable.tableshowview_TableBackColor, mTableBackColor);
		mSurfaceBackColor=mTableBackColor;
		if (typeArray != null)
			typeArray.recycle();
	}

	private void initPaint() {
		mAxisPaint = new Paint();
		mAxisPaint.setAntiAlias(false);
		mAxisPaint.setStyle(Style.STROKE);
		mAxisPaint.setColor(mAxisColor);
		mAxisPaint.setStrokeWidth(mAxisWidth);

		mDataPaint = new Paint();
		mDataPaint.setAntiAlias(false);
		mDataPaint.setStyle(Style.STROKE);
		mDataPaint.setColor(mDataLineColor);
		mDataPaint.setStrokeWidth(mDataLineWidth);

		mAxisVlauePaint = new Paint();
		mAxisVlauePaint.setAntiAlias(false);
		mAxisVlauePaint.setStyle(Style.STROKE);
		mAxisVlauePaint.setColor(mAxisValueColor);
		mAxisVlauePaint.setTextSize(mAxisVlaueSize);

	}

	private void calRect() {
		float mXPadding = this.getWidth() / 100;
		float mYPadding = this.getHeight() / 50;
		mSpanAxisVlaueWithXEnd = mXPadding;
		mSpanAxisVlaueWithYEnd = mYPadding;

		if (mCheckValue < 0 || mCheckValue > yValueMax) {
			mCheckValue = (int) (yValueMax / 2);
		}

		mCheckValueYAxisValueWidth = mAxisVlauePaint.measureText(mCheckValue
				+ "", 0, (mCheckValue + "").length());
		mYmaxValueYAxisValueWidth = mAxisVlauePaint.measureText(yValueMax + "",
				0, (yValueMax + "").length());
		FontMetrics fmAxis = mAxisVlauePaint.getFontMetrics();
		mAxisValueHeight = (float) Math.ceil(fmAxis.descent - fmAxis.ascent);

		mXstart = (int) (mXPadding + mCheckValueYAxisValueWidth);
		mYstart = (int) (mYPadding+mAxisValueHeight+mDataLineWidth);// (int) (this.getHeight()-mYPadding);

		mXend = (int) (this.getWidth() - mSpanAxisVlaueWithXEnd - mXstart);
		mYend = (int) (this.getHeight() - mYPadding - 2 * mAxisValueHeight);
//		Log.i("121", "***:mXstart:" + mXstart + "::mYstart:" + mYstart
//				+ "::mXstart:" + mXstart + "::mYend:" + mYend);
//		Log.i("121", "***:mXPadding:" + mXPadding
//				+ "::mCheckValueYAxisValueWidth:" + mCheckValueYAxisValueWidth);
	}

	private void drawBackgroud(){
		Canvas canvas = surfaceholder.lockCanvas();
		canvas.drawColor(mTableBackColor);
		surfaceholder.unlockCanvasAndPost(canvas);
	}
	private void drawAxisLine() {
		Canvas canvas = surfaceholder.lockCanvas();
		// Log.i("121","***:mXstart:"+mXstart+"::mYstart:"+mYstart+"::mXstart:"+mXstart+"::mYend:"+mYend);
		canvas.drawColor(mTableBackColor);
		canvas.drawLine(mXstart - mAxisWidth, mYstart - mSpanAxisVlaueWithYEnd-mDataLineWidth-mAxisValueHeight,
				mXstart - mAxisWidth, mYend + mAxisWidth , mAxisPaint);
		canvas.drawLine(mXstart - mAxisWidth, mYend + mAxisWidth, mXend
				+ mSpanAxisVlaueWithXEnd, mYend + mAxisWidth, mAxisPaint);

		int xShowValue = 0;
		float xValue = 0;
		FontMetrics fmAxis = mAxisVlauePaint.getFontMetrics();
		mAxisValueHeight = (float) Math.ceil(fmAxis.descent - fmAxis.ascent);

		// 只画x轴起始点
		Date dmin = new Date(xValueMin);
		String minVlaue = format.format(dmin);
		canvas.drawText(minVlaue, mXstart - mCheckValueYAxisValueWidth, mYend
				+ mSpanAxisValueWithXAxis + 2 * mAxisWidth + 3
				* mAxisValueHeight / 2, mAxisVlauePaint);

		if (xValueMax - xValueMin != 0) {// 若只有一个点则只画一个
			Date dmax = new Date(xValueMax);
			String maxVlaue = format.format(dmax);
			float txtwidth = mAxisVlauePaint.measureText(maxVlaue, 0,
					maxVlaue.length());
			canvas.drawText(maxVlaue, mXend - txtwidth,  mYend
					+ mSpanAxisValueWithXAxis + 2 * mAxisWidth + 3
					* mAxisValueHeight / 2, mAxisVlauePaint);
		}
		canvas.drawText((int) mCheckValue + "", mXstart - 4
				* mCheckValueYAxisValueWidth / 5, mYend - mCheckValue
				* yStepLong + mAxisWidth , mAxisVlauePaint);
		canvas.drawText((int) yValueMax + "", mXstart - 4
				* mYmaxValueYAxisValueWidth / 5, mYend - yValueMax * yStepLong
				+ mAxisWidth / 2 +mDataLineWidth/2+mAxisValueHeight/2 , mAxisVlauePaint);
		surfaceholder.unlockCanvasAndPost(canvas);
	}

	PointF startp;
	PointF endp;
	PointF p3 = new PointF();
	PointF p4 = new PointF();

	private void drawDataLineAll() {

		synchronized (this) {
			if (pPositionData.size() == 1) {
				Canvas canvas = surfaceholder.lockCanvas();
				canvas.drawPoint(pPositionData.get(0).x,
						pPositionData.get(0).y, mDataPaint);
				surfaceholder.unlockCanvasAndPost(canvas);
				return;
			}
			Path mPath = new Path();
			Rect f = new Rect((int) mXstart, (int) mYstart, mXend, mYend);
			Canvas canvas = surfaceholder.lockCanvas(f);
			iCurIndex = 0;
			while (iCurIndex < pSourceData.size()) {
				if(bIsBreak)break;
				canvas.drawColor(mSurfaceBackColor);
				PointF aPointF = pPositionData.get(iCurIndex);
				if (iCurIndex == 0) {
					mPath.moveTo(aPointF.x, aPointF.y);
					iCurIndex++;
					continue;
				}
				startp = pPositionData.get(iCurIndex - 1);
				endp = aPointF;
				float xCenter = (int) (startp.x + endp.x) / 2;
				float yCenter = (int) (startp.y + endp.y) / 2;
				p3.y = startp.y;
				p3.x = xCenter;
				p4.y = endp.y;
				p4.x = xCenter;
				// 确定曲线的路径
				mPath.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
				iCurIndex++;
			}
			canvas.drawPath(mPath, mDataPaint);
			surfaceholder.unlockCanvasAndPost(canvas);
		}
	}

	private void drawDataLineDynamic() {

		synchronized (this) {
			if (pPositionData.size() == 1) {
				Canvas canvas = surfaceholder.lockCanvas();
				canvas.drawPoint(pPositionData.get(0).x,
						pPositionData.get(0).y, mDataPaint);
				surfaceholder.unlockCanvasAndPost(canvas);
				return;
			}

			iCurIndex = 0;
			while (iCurIndex < pSourceData.size()) {
				if(bIsBreak)break;
				int i = 0;
				Path mPath = new Path();
				Rect f = new Rect((int) mXstart, (int) (mYstart), mXend, mYend);
				Canvas canvas = surfaceholder.lockCanvas(f);
				canvas.drawColor(mSurfaceBackColor);
				while (i <= iCurIndex) {
					if(bIsBreak)break;
					PointF aPointF = pPositionData.get(i);
					if (i == 0) {
						mPath.moveTo(aPointF.x, aPointF.y);
						i++;
						continue;
					}

					startp = pPositionData.get(i - 1);
					endp = aPointF;
					float xCenter = (int) (startp.x + endp.x) / 2;
					float yCenter = (int) (startp.y + endp.y) / 2;
					p3.y = startp.y;
					p3.x = xCenter;
					p4.y = endp.y;
					p4.x = xCenter;
					// 确定曲线的路径
					mPath.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
					i++;
				}
				iCurIndex++;
				canvas.drawPath(mPath, mDataPaint);
				surfaceholder.unlockCanvasAndPost(canvas);
				try {
					Thread.sleep(mDrawLineSleepTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			iCurIndex++;
		}

	}

	/**
	 * 计算x\Y轴的临界值
	 */
	private void calcrucial() {

		for (int p = 0; p < pSourceData.size(); p++) {
			
			TableShowViewData  aPointF= pSourceData.get(p);
			if (xValueMax < aPointF.x || xValueMax == 0) {
				xValueMax = aPointF.x;
			}
			if (xValueMin > aPointF.x || xValueMin == 0) {
				xValueMin = aPointF.x;
			}
			if (yValueMax < aPointF.y || yValueMax == 0) {
				yValueMax = aPointF.y;
			}
			if (yValueMin > aPointF.y || yValueMin == 0) {
				yValueMin = aPointF.y;
			}
		}
//		 Log.i("121","***:xValueMax:"+xValueMax+"::xValueMin:"+xValueMin+"::yValueMax:"+yValueMax+"::yValueMin:"+yValueMin);
	}

	/**
	 * 计算各个数据的x坐标
	 */
	private void calAxisCoordinate() {
		pPositionData.clear();
		for (int p = 0; p < pSourceData.size(); p++) {
			TableShowViewData aPointF = pSourceData.get(p);
			float x = mXstart + (aPointF.x - xValueMin) * xStepLong;
			float y = mYend - (aPointF.y) * yStepLong;
			PointF newPointF = new PointF(x, y);
			pPositionData.add(newPointF);
//			Log.i("121", "mXstart::" + mXstart + ":::p:x:+" + x + "::y:" + y
//					+ "::::");
		}
	}

	/**
	 * 计算坐标轴应显示的坐标个数和实际步长
	 */
	private void calAxis() {
		if (xValueMax - xValueMin == 0) {
			xIncreaseCount = 1;
			xStepLong = (mXend - mXstart) * 1.00f;
			yIncreaseCount = 1;
			yStepLong = (mYend - mYstart) * 1.00f / yValueMax;
		} else {
			xIncreaseCount = (int) ((xValueMax - xValueMin) % xIncrease > 0 ? (xValueMax - xValueMin)
					/ xIncrease + 1
					: (xValueMax - xValueMin) / xIncrease);
			xStepLong = (mXend - mXstart) * 1.00f / ((xValueMax - xValueMin));

			yIncreaseCount = (yValueMax - yValueMin) % yIncrease > 0 ? (yValueMax - yValueMin)
					/ yIncrease + 1
					: (yValueMax - yValueMin) / yIncrease;
			// yStepLong=(mYend-mYstart-mSpanAxisVlaueWithYEnd)*1.00f/((yValueMax-yValueMin));
			yStepLong = (mYend - mYstart-mDataLineWidth) * 1.00f / ((yValueMax));
		}
	}

	public void setDrawLineSleepTime(int itime) {
		mDrawLineSleepTime = itime;
	}

	public void setIsDynamicDraw(boolean b) {
		this.bIsDynamicDraw = b;
	}

	public void setISurfaceEvent(ISurfaceEvent aISurfaceEvent) {
		this.mISurfaceEvent = aISurfaceEvent;
	}

	public void setData(List<TableShowViewData> alist) throws Exception {

		
		// 重新设置数据后界点需要重新计算
		bIsBreak=true;
		xValueMax = 0;
		xValueMin = 0;

		yValueMax = 0;
		yValueMin = 0;

		mCheckValue=-1;
		iCurIndex = 0;

		pSourceData.clear();
		pPositionData.clear();

		if(alist==null)throw new Exception("数据源null");
		pSourceData.addAll(alist);
		calcrucial();
		calRect();
		calAxis();
		calAxisCoordinate();
		semp.release();
	}

	class DrawThread extends Thread {
		public boolean bIsStop = false;

		public DrawThread() {

		}

		@Override
		public void run() {
			while (true) {
				try {
					semp.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (bIsStop) {
					semp.release();
					return;
				}
				bIsBreak=false;
				if (pPositionData.size() == 0)
					return;
				drawAxisLine();
				if (bIsDynamicDraw) {
					drawDataLineDynamic();
				} else {
					drawDataLineAll();
				}
			}

		}
	}

	public interface ISurfaceEvent {
		public void onSurfaceViewCreated();
	}
}
