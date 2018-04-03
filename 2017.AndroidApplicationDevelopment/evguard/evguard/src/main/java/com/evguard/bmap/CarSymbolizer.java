package com.evguard.bmap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.evguard.tools.BitmapUtils;
import com.xinghaicom.evguard.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class CarSymbolizer {

	private static Matrix mMatrix = new Matrix();

	public static Drawable buildTrackCarSymbol(String trackIndex, int dpi) {
		int marginLeft = 8;
		int paddingLeft = 3;
		int paddingRight = 3;
		int iconWidth = 36;
		int iconHeight = 36;

		// ��ó�~ͼƬͼ����Դ
		Drawable iconDraw = Symbolizer.buildCarTrackSymbol();

		// ���ֻ���
		Paint txtPaint = new Paint();
		txtPaint.setColor(Color.BLUE);
		txtPaint.setTextSize(20);

		float textLength = Symbolizer.getFontLength(txtPaint, trackIndex);
		float textHight = Symbolizer.getFontHeight(txtPaint);
		float textAlign = Symbolizer.getFontLeading(txtPaint);

		int iBitmapWith = (int) (marginLeft + paddingLeft + iconWidth
				+ textLength + paddingRight);
		int iBitmapHeight = iconHeight;

		Bitmap bitmap = Bitmap.createBitmap(iBitmapWith, iBitmapHeight,
				Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		int startx = 0;
		int starty = 0;

		// ��ͼ��
		iconDraw.setBounds(startx, starty, startx + iconWidth,
				(starty + iconHeight));
		iconDraw.draw(canvas);

		// ������
		startx = startx + iconWidth;
		starty = iconHeight / 4;
		canvas.drawText(trackIndex, startx, starty + textAlign, txtPaint);
		mMatrix.setScale(dpi / 160, dpi / 160);
		Bitmap scaleBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), mMatrix, true);
		BitmapDrawable bd = new BitmapDrawable(scaleBitmap);

		return bd;
	}

	public static Drawable buildTrackCarSymbol(String trackIndex,
			boolean isAlarm, int dpi) {
		int marginLeft = 8;
		int paddingLeft = 3;
		int paddingRight = 3;
		int iconWidth = 36;
		int iconHeight = 36;

		// ��ó�~ͼƬͼ����Դ
		Drawable iconDraw = Symbolizer.buildCarIconSymbol(isAlarm);

		// ���ֻ���
		Paint txtPaint = new Paint();
		txtPaint.setColor(Color.BLUE);
		txtPaint.setTextSize(20);

		float textLength = Symbolizer.getFontLength(txtPaint, trackIndex);
		float textHight = Symbolizer.getFontHeight(txtPaint);
		float textAlign = Symbolizer.getFontLeading(txtPaint);

		int iBitmapWith = (int) (marginLeft + paddingLeft + iconWidth
				+ textLength + paddingRight);
		int iBitmapHeight = iconHeight;

		Bitmap bitmap = Bitmap.createBitmap(iBitmapWith, iBitmapHeight,
				Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		int startx = 0;
		int starty = 0;

		// ��ͼ��
		iconDraw.setBounds(startx, starty, startx + iconWidth,
				(starty + iconHeight));
		iconDraw.draw(canvas);

		// ������
		startx = startx + iconWidth;
		starty = iconHeight / 4;
		canvas.drawText(trackIndex, startx, starty + textAlign, txtPaint);
		mMatrix.setScale(dpi / 160, dpi / 160);
		Bitmap scaleBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), mMatrix, true);
		BitmapDrawable bd = new BitmapDrawable(scaleBitmap);

		return bd;
	}

	// public static Bitmap buildKidSymbol(Context context,String carNum,Bitmap
	// head,ViewAnchor anchor,int dpi){
	//
	// // Bitmap
	// a=((BitmapDrawable)context.getResources().getDrawable(R.drawable.icon_kidheadnice_a)).getBitmap();
	// Bitmap
	// b=((BitmapDrawable)context.getResources().getDrawable(R.drawable.icon_kidheadnice_b)).getBitmap();
	//
	// ByteArrayOutputStream baos = new ByteArrayOutputStream();
	// b.compress(Bitmap.CompressFormat.PNG, 100, baos);
	// ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
	// BitmapFactory.Options anewOpts = new BitmapFactory.Options();
	// anewOpts.inJustDecodeBounds = true;
	// BitmapFactory.decodeStream(isBm, null, anewOpts);
	//
	// int reqw=anewOpts.outWidth*2/5;
	// int reqh=anewOpts.outWidth*2/5;//anewOpts.outHeight*3/5;
	// Matrix m=new Matrix();
	// Bitmap bitmap=Bitmap.createBitmap(anewOpts.outWidth, anewOpts.outHeight,
	// Config.ARGB_8888);
	// Canvas canvas=new Canvas(bitmap);
	// Paint p=new Paint();
	//
	// canvas.drawBitmap(b, m, p);
	// // canvas.drawBitmap(a, m, p);
	//
	// baos = new ByteArrayOutputStream();
	// head.compress(Bitmap.CompressFormat.PNG, 100, baos);
	// isBm = new ByteArrayInputStream(baos.toByteArray());
	// BitmapFactory.Options newOpts = new BitmapFactory.Options();
	// newOpts.inJustDecodeBounds = true;
	// BitmapFactory.decodeStream(isBm, null, newOpts);
	// int
	// scale=calculateInSampleSize(newOpts,reqw,reqh);//Math.round(newOpts.outWidth/ConstantTool.KIDHEADIMGWIDTH);
	// newOpts.inSampleSize = scale;//设置缩放比例
	// newOpts.inPreferredConfig = Config.RGB_565;//降低图片从ARGB888到RGB565
	// newOpts.inJustDecodeBounds = false;
	// isBm = new ByteArrayInputStream(baos.toByteArray());
	// Bitmap secondbitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
	// canvas.drawBitmap(secondbitmap,anewOpts.outWidth/5, 3, p);
	//
	// mMatrix.setScale(dpi/160,dpi/160);
	// Bitmap scaleBitmap=Bitmap.createBitmap(bitmap, 0, 0,bitmap.getWidth(),
	// bitmap.getHeight(), mMatrix, true);
	//
	// // BitmapUtils.recyleBitmpa(a);
	// BitmapUtils.recyleBitmpa(b);
	// BitmapUtils.recyleBitmpa(bitmap);
	// return scaleBitmap;
	// }


	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;

		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			while ((height / inSampleSize) > reqHeight
					&& (width / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

	/**
	 * ������~��עͼ��
	 * 
	 * @param carNum
	 *            �ı�ͼ��
	 * @param carStatus
	 *            ��~״̬
	 * @param isFill
	 *            �Ƿ��س�
	 * @param gpsValid
	 *            �Ƿ�Gps��Ч
	 * @param accOn
	 *            Acc�Ƿ���4
	 * @param direction
	 * @return
	 */
	public static Drawable buildCarSymbol(String carNum, int iconType,
			double direction, ViewAnchor anchor, int dpi) {
		int marginLeft = 8;
		int paddingLeft = 3;
		int paddingRight = 3;
		int iconWidth = 32;
		int iconHeight = 32;
		// ��ó�~ͼƬͼ����Դ������ͼ��Canvas��
		Drawable iconDraw = Symbolizer.buildCarIconSymbol(iconType, direction);
		// ׼����������
		Paint backPaint = Symbolizer.getCarLabelBackPain(iconType);
		// ׼�����ֻ���
		Paint textPaint = Symbolizer.getCarLabelPaint();
		float textLength = Symbolizer.getFontLength(textPaint, carNum);
		float textHight = Symbolizer.getFontHeight(textPaint);
		float textAlign = Symbolizer.getFontLeading(textPaint);
		int width = (int) (iconWidth + marginLeft + paddingLeft + textLength + paddingRight);
		anchor.x = width / 2;
		int height = iconHeight;
		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmap);
		int startX = 0;
		int startY = 0;
		// ��ͼ��
		iconDraw.setBounds((int) startX, startY, startX + iconWidth,
				(int) (startY + iconHeight));
		iconDraw.draw(canvas);
		// ������
		startX = startX + iconWidth + marginLeft;
		startY += iconHeight / 4;
		backPaint.setShadowLayer(5, 3, 3, 0xffC6C7C8);
		canvas.drawRect(new Rect(startX, startY, (int) (startX + paddingLeft
				+ textLength + paddingRight), (int) (startY + textHight)),
				backPaint);
		startX = startX + paddingLeft;
		canvas.drawText(carNum, startX, startY + textAlign, textPaint);
		mMatrix.setScale(dpi / 160, dpi / 160);
		Bitmap scaleBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), mMatrix, true);
		BitmapDrawable bd = new BitmapDrawable(scaleBitmap);

		return bd;
	}

	/**
	 * �����˶��켣ʱ��ĳ�~ͼ��
	 * 
	 * @param snippet
	 * @param carStatus
	 * @param isFill
	 * @param gpsValid
	 * @param accOn
	 * @param direction
	 * @return
	 */
	public static Drawable buildTrackSymbol(String snippet, int iconType,
			double direction) {
		return Symbolizer.buildCarTrackSymbol();
	}

	/**
	 * ��ȡĬ�ϵĳ�~ͼ��
	 * 
	 * @return
	 */
	public static Drawable getDefaultDrawable() {
		return Symbolizer.buildCarIconSymbol(0, 0);
	}
}
