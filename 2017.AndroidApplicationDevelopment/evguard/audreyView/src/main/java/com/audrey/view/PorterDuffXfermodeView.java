package com.audrey.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

public class PorterDuffXfermodeView extends View {
	private static final int WAVE_TRANS_SPEED = 2;

	private Paint mBitmapPaint, mPicPaint;
	private int mTotalWidth, mTotalHeight;
	private int mCenterX, mCenterY;
	private int mSpeed;

	private Bitmap mSrcBitmap;
	private Rect mSrcRect, mDestRect;

	private PorterDuffXfermode mPorterDuffXfermode;
	private Bitmap mMaskBitmap;
	private Rect mMaskSrcRect, mMaskDestRect;
	private PaintFlagsDrawFilter mDrawFilter;
	private Context mContext = null;
	private int mCurrentPosition;

	private Paint mOutArcPaint = new Paint();
	private Paint mInArcPaint = new Paint();
	private int mOutArcColor = 0xff232b2e;
	private int mInArcColor = 0xff2e373e;
	private int mOutArcStrokeWidth = 10;

	private int mMaxValue = 100;
	private int mBarValue = 0;
	private float mFValueScale = 0.30f;

	private float mBarValueSize = 50;
	private int mBarValueColor = 0xffffffff;
	private int mTipColor = 0xffffffff;
	private float mTipSize = 20;

	private Paint mBarValuePaint = new Paint();
	private Paint mPercentPaint = new Paint();
	private Paint mTipPaint = new Paint();

	private String mTip = "正在充电";

	public PorterDuffXfermodeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initAttr(attrs);
		initPaint();
		initBitmap();
		mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
		mSpeed = UiUtils.dipToPx(mContext, WAVE_TRANS_SPEED);
		mDrawFilter = new PaintFlagsDrawFilter(Paint.ANTI_ALIAS_FLAG,
				Paint.DITHER_FLAG);
		new Thread() {
			public void run() {
				while (true) {
					// 不断改变绘制的波浪的位置
					mCurrentPosition += mSpeed;
					if (mCurrentPosition >= mSrcBitmap.getWidth()) {
						mCurrentPosition = 0;
					}
					try {
						// 为了保证效果的同时，尽可能将cpu空出来，供其他部分使用
						Thread.sleep(30);
					} catch (InterruptedException e) {
					}
					postInvalidate();
				}
			};
		}.start();
	}

	public void setBarValue(int value) {
		this.mBarValue = value;
		mFValueScale = this.mBarValue * 1.00f / mMaxValue;
		calDestRect();

	}

	public void setMaxValue(int value) {
		this.mMaxValue = value;
	}

	private void calDestRect() {
		mDestRect = new Rect(0, (int) (mTotalHeight - mFValueScale
				* mTotalHeight), mTotalWidth, mTotalHeight);
	}

	private void initAttr(AttributeSet attrs) {
		TypedArray typeArray = mContext.getTheme().obtainStyledAttributes(
				attrs, R.styleable.wavePorterduffxfermode, 0, 0);

		mOutArcStrokeWidth = (int) typeArray.getDimension(R.styleable.wavePorterduffxfermode_OutArcStrokeWidth,mOutArcStrokeWidth);
		mOutArcColor = typeArray.getColor(R.styleable.wavePorterduffxfermode_OutArcColor, mOutArcColor);
		mInArcColor = typeArray.getColor(R.styleable.wavePorterduffxfermode_InArcColor, mInArcColor);
//		// mBarValueColor=typeArray.getColor(R.styleable.w, mBarValueColor);
//		// mTipColor=typeArray.getColor(R.styleable.wavePorterduffxfermode_InArcColor,
//		// mTipColor);
		mMaxValue = typeArray.getInt(R.styleable.wavePorterduffxfermode_WaveMaxValue, mMaxValue);
		mTipSize = typeArray.getDimension(R.styleable.wavePorterduffxfermode_WaveTextTipSize, mTipSize);
		mBarValueSize = typeArray.getDimension(R.styleable.wavePorterduffxfermode_WaveTextValueSize,mBarValueSize);
		mTip = typeArray.getString(R.styleable.wavePorterduffxfermode_WaveTextTip);
		mTip=mTip==null?"":mTip;
		if(typeArray!=null)typeArray.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawCircle(mCenterX, mCenterY, radius + mOutArcStrokeWidth,
				mOutArcPaint);
		canvas.drawCircle(mCenterX, mCenterY, radius, mInArcPaint);

		// 从canvas层面去除锯齿
		canvas.setDrawFilter(mDrawFilter);
		canvas.drawColor(Color.TRANSPARENT);

		/*
		 * 将绘制操作保存到新的图层
		 */
		// int sc = canvas.saveLayer(0, 0, mTotalWidth, mTotalHeight, null,
		// Canvas.ALL_SAVE_FLAG);
		int sc = canvas.saveLayer(mCenterX - radius, mCenterY - radius,
				mCenterX + radius, mCenterY + radius, null,
				Canvas.ALL_SAVE_FLAG);

		// 设定要绘制的波纹部分
		// mSrcRect.set(mCurrentPosition, 0, mCurrentPosition + mCenterX,
		// mTotalHeight); (mTotalHeight-mFValueScale*mTotalHeight)
		mSrcRect.set(mCurrentPosition, 0,mCurrentPosition + mCenterX, mTotalHeight);
		// 绘制波纹部分
		canvas.drawBitmap(mSrcBitmap, mSrcRect, mDestRect, mBitmapPaint);

		// 设置图像的混合模式
		mBitmapPaint.setXfermode(mPorterDuffXfermode);
		// 绘制遮罩圆
		canvas.drawBitmap(mMaskBitmap, mMaskSrcRect, mMaskDestRect,
				mBitmapPaint);

		mBitmapPaint.setXfermode(null);
		drawBarValue(canvas);
		canvas.restoreToCount(sc);

	}

	private void drawBarValue(Canvas canvas) {

		FontMetrics fmValue = mBarValuePaint.getFontMetrics();
		String txtBarValue = mBarValue + "";
		float mTxtHeight = (int) Math.ceil(fmValue.descent - fmValue.ascent);
		float mTxtWidth = mBarValuePaint.measureText(txtBarValue, 0,	txtBarValue.length());
		canvas.drawText(txtBarValue, mCenterX - mTxtWidth / 2, mCenterY	+ mTxtHeight / 4, mBarValuePaint);

		FontMetrics fmpercent = mPercentPaint.getFontMetrics();
		String txtprecent = " %";
		float mtxtprecentHeight = (int) Math.ceil(fmpercent.descent- fmpercent.ascent);
		float mtxtprecentWidth = mPercentPaint.measureText(txtprecent, 0,	txtprecent.length());
		canvas.drawText(txtprecent, mCenterX + mTxtWidth /2 - mtxtprecentWidth/4, mCenterY + mtxtprecentHeight / 2, mPercentPaint);

		FontMetrics fmTip = mTipPaint.getFontMetrics();
		int mTxtHeight2 = (int) Math.ceil(fmTip.descent - fmTip.ascent);
		float mTxtWidth2 = mTipPaint.measureText(mTip, 0, mTip.length());
		canvas.drawText(mTip, mCenterX - mTxtWidth2 / 2, mCenterY + 3* mTxtHeight / 4 + mTxtHeight2 / 4 , mTipPaint);
	}

	// 初始化bitmap
	private void initBitmap() {
		mSrcBitmap = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.wave_2000)).getBitmap();
		mMaskBitmap = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.circle_500)).getBitmap();
	}

	// 初始化画笔paint
	private void initPaint() {

		mBitmapPaint = new Paint();
		// 防抖动
		mBitmapPaint.setDither(true);
		// 开启图像过滤
		mBitmapPaint.setFilterBitmap(true);

		mPicPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPicPaint.setDither(true);
		mPicPaint.setColor(Color.RED);

		mOutArcPaint.setAntiAlias(false);
		mOutArcPaint.setStyle(Style.FILL);
		mOutArcPaint.setStrokeWidth(0);// mOutArcStrokeWidth
		mOutArcPaint.setColor(mOutArcColor);
		mOutArcPaint.setDither(false);

		mInArcPaint.setAntiAlias(false);
		mInArcPaint.setStyle(Style.FILL);
		mInArcPaint.setStrokeWidth(0);// mInArcStrokeWidth
		mInArcPaint.setColor(mInArcColor);

		mBarValuePaint.setAntiAlias(false);
		mBarValuePaint.setStyle(Style.STROKE);
		mBarValuePaint.setColor(mBarValueColor);
		mBarValuePaint.setTextSize(mBarValueSize);

		mPercentPaint.setAntiAlias(false);
		mPercentPaint.setStyle(Style.STROKE);
		mPercentPaint.setColor(mBarValueColor);
		mPercentPaint.setTextSize(mBarValueSize / 2);

		mTipPaint.setAntiAlias(false);
		mTipPaint.setStyle(Style.STROKE);
		mTipPaint.setColor(mTipColor);
		mTipPaint.setTextSize(mTipSize);
	}

	private int radius = 0;

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mTotalWidth = w;
		mTotalHeight = h;

		radius = mTotalWidth > mTotalHeight ? (mTotalHeight - 2 * mOutArcStrokeWidth) / 2
				: (mTotalWidth - 2 * mOutArcStrokeWidth) / 2;
		mCenterX = mTotalWidth / 2;
		mCenterY = (int) (mTotalHeight - radius - mOutArcStrokeWidth);

		mSrcRect = new Rect();
		calDestRect();
		// mDestRect = new Rect(0, 0, mTotalWidth, mTotalHeight);
		// mDestRect = new Rect(mCenterX-radius, mCenterY-radius,
		// mCenterX+radius, mCenterY+radius);

		// int maskWidth = mMaskBitmap.getWidth();
		// int maskHeight = mMaskBitmap.getHeight();
		// mMaskSrcRect = new Rect(0, 0, maskWidth, maskHeight);
		mMaskDestRect = new Rect(mCenterX - radius, mCenterY - radius, mCenterX
				+ radius, mCenterY + radius);
	}

}
