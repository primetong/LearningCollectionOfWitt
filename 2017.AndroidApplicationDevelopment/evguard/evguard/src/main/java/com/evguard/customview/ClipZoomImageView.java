package com.evguard.customview;

import com.evguard.tools.LogEx;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.widget.ImageView;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;

/**
 * 
 * @author zhy http://blog.csdn.net/lmj623565791/article/details/39761281
 */
public class ClipZoomImageView extends ImageView implements
		OnScaleGestureListener, OnTouchListener,
		ViewTreeObserver.OnGlobalLayoutListener {
	private String TAG = "ZoomImageView";
	private static float SCALE_MAX = 8.0f;// 最大缩放比
	private static float SCALE_MID = 2.0f;
	private static float initScale = 1.0f;// 初始缩放比
	private boolean bIsEnter = false;

	/**
	 * 用于存放矩阵的9个值
	 */
	private final float[] matrixValues = new float[9];

	/**
	 * 缩放的手势检测
	 */
	private ScaleGestureDetector mScaleGestureDetector = null;

	private final Matrix mScaleMatrix = new Matrix();

	private int lastPointerCount;
	private float mLastX;
	private float mLastY;
	private boolean isCanDrag = false;
	private boolean isCheckTopAndBottom = false;
	private boolean isCheckLeftAndRight = false;
	private int mTouchSlop = 10;

	/**
	 * 水平方向与View的边距
	 */
	private int mHorizontalPadding = 20;
	/**
	 * 垂直方向与View的边距
	 */
	private int mVerticalPadding;

	public ClipZoomImageView(Context context) {
		super(context);
		init(context);
	}

	public ClipZoomImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ClipZoomImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {

		super.setScaleType(ScaleType.MATRIX);// 设置缩放类型为矩阵缩放
		this.setOnTouchListener(this);
		mScaleGestureDetector = new ScaleGestureDetector(context, this);
		// setOnTouchListener(new TouchListener());
	}

	public void setHorizontalPadding(int mHorizontalPadding) {
		this.mHorizontalPadding = mHorizontalPadding;

	}

	/**
	 * 获得当前的缩放比例
	 * 
	 * @return
	 */
	public final float getScale() {
		mScaleMatrix.getValues(matrixValues);
		return matrixValues[Matrix.MSCALE_X];
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		getViewTreeObserver().addOnGlobalLayoutListener(this);
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (Build.VERSION.SDK_INT >= 16) {
			// 使用新的API
			getViewTreeObserver().removeOnGlobalLayoutListener(this);
		} else {
			// 使用旧的API
			getViewTreeObserver().removeGlobalOnLayoutListener(this);
		}
	}

	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		float scale = getScale();
		float detectorScale = detector.getScaleFactor();// 获取当前伸缩率

		if (getDrawable() == null)
			return true;
		if ((scale < SCALE_MAX && detectorScale > 1.0f) || // 缩放比未达到最大且，用户手势未放大
				(scale > initScale && detectorScale < 1.0f)) {// 缩放比为达到最小且用户手势缩小
			/**
			 * 最大值最小值判断
			 */
			if (detectorScale * scale < initScale) {
				detectorScale = initScale / scale;
			}
			if (detectorScale * scale > SCALE_MAX) {
				detectorScale = SCALE_MAX / scale;
			}
			/**
			 * 设置缩放比例
			 */
			mScaleMatrix.postScale(detectorScale, detectorScale,
					detector.getFocusX(), detector.getFocusY());
			checkBorderAndCenterWhenScale();
			setImageMatrix(mScaleMatrix);
		}
		return true;
	}

	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) {
		return true;
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {

	}

	// @SuppressLint("NewApi")
	// @Override
	// public void onGlobalLayout() {//默认会调用两次
	// // if(bIsEnter){
	// // return;
	// // }
	// // bIsEnter=true;
	//
	// //使用旧的API
	// getViewTreeObserver().removeGlobalOnLayoutListener(this);
	//
	// Drawable drawable=getDrawable();
	// if(drawable==null)return;
	// // 计算padding的px
	// mHorizontalPadding = (int) TypedValue.applyDimension(
	// TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding,
	// getResources().getDisplayMetrics());
	// // 垂直方向的边距
	// mVerticalPadding = (getHeight() - (getWidth() - 2 * mHorizontalPadding))
	// / 2;
	//
	// //屏幕宽高
	// int width=this.getWidth();
	// int height=this.getHeight();
	// //图片的宽高
	// int dw=drawable.getIntrinsicWidth();
	// int dh=drawable.getIntrinsicHeight();
	// reqW=width-mHorizontalPadding*2;
	// reqH=(height-mVerticalPadding*2);
	//
	// float scale=1.0f;//缩放比//04-10 17:39:53.913: I/wlh11(18909):
	// width:540;;height:749;;;;dw:2048;;dh:1152
	// // 如果图片的宽或者高大于屏幕，则缩放至屏幕的宽或者高
	// if(dw>reqW && dh<=reqH)
	// {
	// // scale=height*1.0f/dh;
	// // scale = (getHeight() * 1.0f - mVerticalPadding * 2) / dh;
	// scale=reqH*1.0f/dh;
	// }
	// if(dh>reqH && dw<=reqW)
	// {
	// // scale=height*1.0f/dh;
	// // scale = (getWidth() * 1.0f - mHorizontalPadding * 2) / dw;
	// scale=reqW*1.0f/dw;
	// }
	// // 如果宽和高都大于屏幕，则让其按按比例适应屏幕大小
	// if(dh>reqH && dw>reqW)
	// {
	// // scale=Math.min(height*1.0f/dh, width*1.0f/dw);
	// scale=Math.min(reqH*1.0f/dh, reqW*1.0f/dw);
	// }
	// if (dw < reqW && dh < reqH)
	// {
	// float scaleW =reqW *1.0f / dw;
	// float scaleH = reqH *1.0f / dh;
	// // float scaleW =width / dw;
	// // float scaleH = height / dh;
	// scale = Math.max(scaleW, scaleH);
	// }
	//
	// initScale=scale;
	// SCALE_MID = initScale * 1;
	// SCALE_MAX = initScale * 8;
	//
	// // 图片移动至屏幕中心
	// mScaleMatrix.postTranslate((width - dw) / 2, (height - dh) / 2);
	// //图片缩放
	// mScaleMatrix.postScale(scale, scale, getWidth() / 2, getHeight() / 2);
	// setImageMatrix(mScaleMatrix);
	// }
	// /**
	// * 在缩放时，进行图片显示范围的控制
	// */
	// private void checkBorderAndCenterWhenScale()
	// {
	//
	// RectF rect = getMatrixRectF();
	// float deltaX = 0;
	// float deltaY = 0;
	//
	// int width = getWidth();
	// int height = getHeight();
	//
	// // 如果宽或高大于屏幕，则控制范围
	// if (rect.width() >= (width-mHorizontalPadding*2))
	// {
	// if (rect.left >mHorizontalPadding)
	// {
	// deltaX = -rect.left+mHorizontalPadding;
	// }
	// if (rect.right < width-mHorizontalPadding)
	// {
	// deltaX = width -mHorizontalPadding- rect.right;
	// }
	// }
	// if (rect.height() >= (height-mVerticalPadding*2))
	// {
	// if (rect.top > mVerticalPadding)
	// {
	// deltaY = -rect.top+mVerticalPadding;
	// }
	// if (rect.bottom < height-mVerticalPadding)
	// {
	// deltaY = height -mVerticalPadding- rect.bottom;
	// }
	// }
	// // 如果宽或高小于屏幕，则让其居中
	// if (rect.width() < width)
	// {
	// deltaX = width * 0.5f - rect.right + 0.5f * rect.width();
	// }
	// if (rect.height() < height)
	// {
	// deltaY = height * 0.5f - rect.bottom + 0.5f * rect.height();
	// }
	// // Log.e(TAG, "deltaX = " + deltaX + " , deltaY = " + deltaY);
	//
	// mScaleMatrix.postTranslate(deltaX, deltaY);
	//
	// }
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		mScaleGestureDetector.onTouchEvent(event);
		float x = 0, y = 0;
		// 拿到触摸点的个数
		final int pointerCount = event.getPointerCount();
		// 得到多个触摸点的x与y均值
		for (int i = 0; i < pointerCount; i++) {
			x += event.getX(i);
			y += event.getY(i);
		}
		x = x / pointerCount;
		y = y / pointerCount;

		/**
		 * 每当触摸点发生变化时，重置mLasX , mLastY
		 */
		if (pointerCount != lastPointerCount) {
			isCanDrag = false;
			mLastX = x;
			mLastY = y;
		}
		lastPointerCount = pointerCount;

		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			float dx = x - mLastX;
			float dy = y - mLastY;

			if (!isCanDrag) {
				isCanDrag = isCanDrag(dx, dy);
			}
			if (isCanDrag) {
				RectF rectF = getMatrixRectF();
				if (getDrawable() != null) {
					isCheckLeftAndRight = isCheckTopAndBottom = true;
					// 如果宽度小于屏幕宽度，则禁止左右移动
					if (rectF.width() < getWidth()) {
						dx = 0;
						isCheckLeftAndRight = false;
					}
					// 如果高度小雨屏幕高度，则禁止上下移动
					if (rectF.height() < getHeight()) {
						dy = 0;
						isCheckTopAndBottom = false;
					}
					mScaleMatrix.postTranslate(dx, dy);
					checkMatrixBounds();
					setImageMatrix(mScaleMatrix);
				}
			}
			mLastX = x;
			mLastY = y;
			break;

		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			LogEx.e(TAG, "ACTION_UP");
			lastPointerCount = 0;
			break;
		}
		return true;
	}

	/**
	 * 剪切图片，返回剪切后的bitmap对象
	 * 
	 * @return
	 */
	public Bitmap clip() {
		Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		draw(canvas);
		return Bitmap.createBitmap(bitmap, mHorizontalPadding,
				mVerticalPadding, getWidth() - 2 * mHorizontalPadding,
				getWidth() - 2 * mHorizontalPadding);
	}

	@SuppressLint("NewApi")
	@Override
	public void onGlobalLayout() {// 默认会调用两次
		// 使用旧的API
		getViewTreeObserver().removeGlobalOnLayoutListener(this);

		Drawable drawable = getDrawable();
		if (drawable == null)
			return;
		// 计算padding的px
		mHorizontalPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
						.getDisplayMetrics());
		// 垂直方向的边距
		mVerticalPadding = (getHeight() - (getWidth() - 2 * mHorizontalPadding)) / 2;

		// 屏幕宽高
		int width = this.getWidth();
		int height = this.getHeight();
		// 图片的宽高
		int dw = drawable.getIntrinsicWidth();
		int dh = drawable.getIntrinsicHeight();

		float scale = 1.0f;// 缩放比//04-10 17:39:53.913: I/wlh11(18909):
							// width:540;;height:749;;;;dw:2048;;dh:1152
		// 如果图片的宽或者高大于屏幕，则缩放至屏幕的宽或者高
		if (dw > width && dh <= height) {
			scale = width * 1.0f / dw;
		}
		if (dh > height && dw <= width) {
			scale = dh * 1.0f / dh;
		}
		// 如果宽和高都大于屏幕，则让其按按比例适应屏幕大小
		if (dh > height && dw > width) {
			scale = Math.min(height * 1.0f / dh, width * 1.0f / dw);
		}

		initScale = scale;
		SCALE_MID = initScale * 1;
		SCALE_MAX = initScale * 8;

		// 图片移动至屏幕中心
		mScaleMatrix.postTranslate((width - dw) / 2, (height - dh) / 2);
		// 图片缩放
		mScaleMatrix.postScale(scale, scale, getWidth() / 2, getHeight() / 2);
		setImageMatrix(mScaleMatrix);
	}

	/**
	 * 在缩放时，进行图片显示范围的控制
	 */
	private void checkBorderAndCenterWhenScale() {

		RectF rect = getMatrixRectF();
		float deltaX = 0;
		float deltaY = 0;

		int width = getWidth();
		int height = getHeight();

		// 如果宽或高大于屏幕，则控制范围
		if (rect.width() >= (width)) {
			if (rect.left > mHorizontalPadding) {
				deltaX = -rect.left + mHorizontalPadding;
			}
			if (rect.right < width) {
				deltaX = width - mHorizontalPadding - rect.right;
			}
		}
		if (rect.height() >= (height)) {
			if (rect.top > mVerticalPadding) {
				deltaY = -rect.top + mVerticalPadding;
			}
			if (rect.bottom < height) {
				deltaY = height - mVerticalPadding - rect.bottom;
			}
		}
		// 如果宽或高小于屏幕，则让其居中
		if (rect.width() < width) {
			deltaX = width * 0.5f - rect.right + 0.5f * rect.width();
		}
		if (rect.height() < height) {
			deltaY = height * 0.5f - rect.bottom + 0.5f * rect.height();
		}
		// Log.e(TAG, "deltaX = " + deltaX + " , deltaY = " + deltaY);

		mScaleMatrix.postTranslate(deltaX, deltaY);

	}

	/**
	 * 根据当前图片的Matrix获得图片的范围
	 * 
	 * @return
	 */
	private RectF getMatrixRectF() {
		Matrix matrix = mScaleMatrix;
		RectF rect = new RectF();
		Drawable d = getDrawable();
		if (null != d) {
			rect.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
			matrix.mapRect(rect);
		}
		return rect;
	}

	/**
	 * 移动时，进行边界判断，主要判断宽或高大于屏幕的
	 */
	private void checkMatrixBounds() {
		RectF rect = getMatrixRectF();

		float deltaX = 0, deltaY = 0;
		final float viewWidth = getWidth();
		final float viewHeight = getHeight();
		// 判断移动或缩放后，图片显示是否超出屏幕边界
		if (rect.top > 0 && isCheckTopAndBottom) {
			deltaY = -rect.top;
		}
		if (rect.bottom < viewHeight && isCheckTopAndBottom) {
			deltaY = viewHeight - rect.bottom;
		}
		if (rect.left > 0 && isCheckLeftAndRight) {
			deltaX = -rect.left;
		}
		if (rect.right < viewWidth && isCheckLeftAndRight) {
			deltaX = viewWidth - rect.right;
		}
		mScaleMatrix.postTranslate(deltaX, deltaY);
	}

	/**
	 * 是否是推动行为
	 * 
	 * @param dx
	 * @param dy
	 * @return
	 */
	private boolean isCanDrag(float dx, float dy) {
		return Math.sqrt((dx * dx) + (dy * dy)) >= mTouchSlop;
	}

	private final class TouchListener implements OnTouchListener {

		/** 记录是拖拉照片模式还是放大缩小照片模式 */
		private int mode = 0;// 初始状态
		/** 拖拉照片模式 */
		private static final int MODE_DRAG = 1;
		/** 放大缩小照片模式 */
		private static final int MODE_ZOOM = 2;

		/** 用于记录开始时候的坐标位置 */
		private PointF startPoint = new PointF();
		/** 用于记录拖拉图片移动的坐标位置 */
		private Matrix matrix = new Matrix();
		/** 用于记录图片要进行拖拉时候的坐标位置 */
		private Matrix currentMatrix = new Matrix();

		/** 两个手指的开始距离 */
		private float startDis;
		/** 两个手指的中间点 */
		private PointF midPoint;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			/** 通过与运算保留最后八位 MotionEvent.ACTION_MASK = 255 */
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			// 手指压下屏幕
			case MotionEvent.ACTION_DOWN:
				mode = MODE_DRAG;
				// 记录ImageView当前的移动位置
				currentMatrix.set(getImageMatrix());
				startPoint.set(event.getX(), event.getY());
				break;
			// 手指在屏幕上移动，改事件会被不断触发
			case MotionEvent.ACTION_MOVE:
				// 拖拉图片
				if (mode == MODE_DRAG) {
					float dx = event.getX() - startPoint.x; // 得到x轴的移动距离
					float dy = event.getY() - startPoint.y; // 得到x轴的移动距离
					// 在没有移动之前的位置上进行移动
					matrix.set(currentMatrix);
					matrix.postTranslate(dx, dy);
				}
				// 放大缩小图片
				else if (mode == MODE_ZOOM) {
					float endDis = distance(event);// 结束距离
					if (endDis > 10f) { // 两个手指并拢在一起的时候像素大于10
						float scale = endDis / startDis;// 得到缩放倍数
						matrix.set(currentMatrix);
						matrix.postScale(scale, scale, midPoint.x, midPoint.y);
					}
				}
				break;
			// 手指离开屏幕
			case MotionEvent.ACTION_UP:
				// 当触点离开屏幕，但是屏幕上还有触点(手指)
			case MotionEvent.ACTION_POINTER_UP:
				mode = 0;
				break;
			// 当屏幕上已经有触点(手指)，再有一个触点压下屏幕
			case MotionEvent.ACTION_POINTER_DOWN:
				mode = MODE_ZOOM;
				/** 计算两个手指间的距离 */
				startDis = distance(event);
				/** 计算两个手指间的中间点 */
				if (startDis > 10f) { // 两个手指并拢在一起的时候像素大于10
					midPoint = mid(event);
					// 记录当前ImageView的缩放倍数
					currentMatrix.set(getImageMatrix());
				}
				break;
			}
			setImageMatrix(matrix);
			return true;
		}

		/** 计算两个手指间的距离 */
		private float distance(MotionEvent event) {
			float dx = event.getX(1) - event.getX(0);
			float dy = event.getY(1) - event.getY(0);
			/** 使用勾股定理返回两点之间的距离 */
			return FloatMath.sqrt(dx * dx + dy * dy);
		}

		/** 计算两个手指间的中间点 */
		private PointF mid(MotionEvent event) {
			float midX = (event.getX(1) + event.getX(0)) / 2;
			float midY = (event.getY(1) + event.getY(0)) / 2;
			return new PointF(midX, midY);
		}

	}

}
