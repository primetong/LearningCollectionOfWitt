package com.evguard.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.util.Base64;

import com.evguard.main.App_Application;

public class BitmapUtils {

	public static byte[] bitmap2Array(Bitmap bmp) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public static Bitmap array2Bitmap(byte[] array) {
		Bitmap bitmap = BitmapFactory.decodeByteArray(array, 0, array.length);
		return bitmap;
	}

	public static Bitmap getRoundAndSpecialSizeBitmap(Bitmap bmp, int reqw,
			int reqh) throws Exception {

		Bitmap bitmap = getRoundBitmap(bmp);
		Bitmap finalbitmap = scaleBitmap(bitmap, reqw, reqh);
		return finalbitmap;// compressBitmap(finalbitmap);
	}

	/**
	 * 获取圆形图片
	 * 
	 * @param aBitmap
	 * @return
	 * @throws Exception
	 */
	private static Bitmap getRoundBitmap(Bitmap aBitmap) throws Exception {
		if (aBitmap == null)
			throw new Exception("资源图无效！");
		int w = aBitmap.getWidth();
		int h = aBitmap.getHeight();

		int strokwith = 10;
		float radius = (w - strokwith) / 2 * 1.0f;

		Bitmap newbitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(newbitmap);
		Paint p = new Paint();
		BitmapShader mBitmapShader = new BitmapShader(aBitmap, TileMode.CLAMP,
				TileMode.CLAMP);
		p.setShader(mBitmapShader);
		p.setAntiAlias(true);

		Paint p1 = new Paint();
		p1.setAntiAlias(true);
		p1.setStyle(Style.STROKE);
		p1.setStrokeWidth(strokwith);
		p1.setColor(Color.WHITE);
		p1.setAntiAlias(true);

		canvas.drawCircle(w / 2, h / 2, radius, p1);
		canvas.drawCircle(w / 2, h / 2, radius - strokwith, p);

		return newbitmap;

	}

	/**
	 * 缩放图片
	 * 
	 * @param aBitmap
	 * @return
	 * @throws Exception
	 */
	public static Bitmap scaleBitmap(Bitmap aBitmap, int reqw, int reqh)
			throws Exception {

		if (aBitmap == null)
			throw new Exception("资源图无效！");

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		aBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		// Log.i("wlh", "imgesieze:"+(baos.toByteArray().length / 1024)+"");
		if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			aBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(isBm, null, newOpts);
		int scale = calculateInSampleSize(newOpts, reqw, reqh);// Math.round(newOpts.outWidth/ConstantTool.KIDHEADIMGWIDTH);
		newOpts.inSampleSize = scale;// 设置缩放比例
		newOpts.inPreferredConfig = Config.RGB_565;// 降低图片从ARGB888到RGB565
		newOpts.inJustDecodeBounds = false;

		isBm = new ByteArrayInputStream(baos.toByteArray());
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);

		return createScaleBitmap(bitmap, reqw, reqh);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

	/**
	 * 计算合适的缩放比
	 * 
	 * @param options原本Bitmap的options
	 * @param minSideLength希望生成的缩略图的宽高中的较小的值
	 * @param maxNumOfPixels希望生成的缩量图的总像素
	 * @return
	 */
	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	// 如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响
	private static Bitmap createScaleBitmap(Bitmap src, int dstWidth,
			int dstHeight) {
		Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, true);
		Matrix m = new Matrix();
		m.setScale(CommUtils.getDensityDPI() / 160,
				CommUtils.getDensityDPI() / 160);
		Bitmap res = Bitmap.createBitmap(dst, 0, 0, dstWidth, dstHeight, m,
				true);
		if (src != dst) { // 如果没有缩放，那么不回收
			src.recycle(); // 释放Bitmap的native像素数组
		}
		if (res != dst) { // 如果没有缩放，那么不回收
			dst.recycle(); // 释放Bitmap的native像素数组
		}
		return res;
	}

	public static Bitmap compressBitmap(Bitmap aBitmap) throws Exception {
		if (aBitmap == null)
			throw new Exception("资源图无效！");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 100;
		aBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 此处只能为jpeg，png的话，无论多大都不会压缩
		LogEx.i("wlh", "uncompressimgesieze:"
				+ (baos.toByteArray().length / 1024) + "");
		while ((baos.toByteArray().length / 1024 > ConstantTool.KIDHEADIMGSIZE)) {
			baos.reset();// 重置baos即清空baos
			options -= 5;
			if (options <= 0)
				break;
			aBitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap finalbitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		LogEx.i("wlh", "compressimgesieze:"
				+ (baos.toByteArray().length / 1024) + "");
		return finalbitmap;
	}

	public static byte[] compressBitmapToByte(Bitmap aBitmap) throws Exception {
		if (aBitmap == null)
			throw new Exception("资源图无效！");

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 100;
		aBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 此处只能为jpeg，png的话，无论多大都不会压缩
		LogEx.i("wlh", "uncompressimgesieze:"
				+ (baos.toByteArray().length / 1024) + "");
		while ((baos.toByteArray().length / 1024 > ConstantTool.KIDHEADIMGSIZE)) {
			baos.reset();// 重置baos即清空baos
			options -= 5;
			if (options <= 0)
				break;
			aBitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
		}
		byte[] bres = new byte[baos.toByteArray().length];
		bres = baos.toByteArray().clone();
		baos.flush();
		baos.close();
		LogEx.i("wlh", "compressimgesieze:" + (bres.length / 1024) + "");
		return bres;
	}

	public static void recyleBitmpa(Bitmap bm) {
		if (bm != null && !bm.isRecycled()) {
			bm.recycle();
			bm = null;
			System.gc();
		}
	}

	public static String Byte2Base64(byte[] b) {
		String base64 = Base64.encodeToString(b, Base64.NO_WRAP);
		// writelog(base64);
		return base64;
	}

	public static String bitmap2Base64(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		try {
			baos.flush();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		LogEx.i("wlh", "save base64size:" + baos.toByteArray().length / 1024);
		byte[] bitmapBytes = baos.toByteArray();
		String base64 = Base64.encodeToString(bitmapBytes, Base64.NO_WRAP);
		LogEx.i("wlh", "base64:" + base64);

		return base64;
	}

	private static void writelog(String s) {
		try {
			File f = new File(App_Application.getInstance().getFilesDir()
					.getPath()
					+ "/txt.txt");
			LogEx.i("wlh11", "base64:" + f.getAbsolutePath());
			f.createNewFile();
			RandomAccessFile raf = new RandomAccessFile(f, "rw");
			raf.seek(f.length());
			raf.write(s.getBytes());
			raf.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Bitmap getBitmap(String path){

		URL url;
		try {
			url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == 200) {
			InputStream inputStream = conn.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			return bitmap;
		}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
