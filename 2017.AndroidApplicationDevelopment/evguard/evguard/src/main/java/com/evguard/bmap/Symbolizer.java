package com.evguard.bmap;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.FontMetrics;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.TypedValue;

public class Symbolizer {

//	/**
//	 * ����ʿ--��~ͼ��
//	 * @param carStatus ��~״̬
//	 * @param isFill �Ƿ����س�
//	 * @param gpsValid GPS�Ƿ�����
//	 * @param accOn Acc�Ƿ���
//	 * @param direction ����
//	 * @return
//	 */
//	public static Drawable buildCarIconSymbol(int carStatus, boolean isFill, boolean gpsValid, boolean accOn, double direction){
//		String carSymbolPath = "symbol";
//		if(carStatus != 1 && !gpsValid){
//			carSymbolPath += "/1/2.png";			
//		}
//		else{
//			switch (carStatus) {
//				case 1:
//					carSymbolPath += "/1/";											
//					break;
//				case 2:							
//					if (isFill) {
//						carSymbolPath += "/3/";
//					} else {
//						carSymbolPath += "/2/";
//					}
//					break;
//				default:
//					carSymbolPath += "/1/";											
//					break;
//			}
//			if (!accOn) {
//				carSymbolPath += "0.png";
//			} else {
//				direction %= 360;
//				if(direction < 0) direction += 360;				
//				int angleTimes = (int) (direction / 22.5);
//				double remainder = direction - angleTimes*22.5;
//				if (remainder > 11.25) {
//					angleTimes++;
//				}
//				carSymbolPath += "1" + String.valueOf(angleTimes) + ".png";
//			}
//		}
//		URL carSymbolURL = CarSymbolizer.class.getResource(carSymbolPath);
//		Drawable carSymbol = buildSymbol(carSymbolURL);
//		return carSymbol;
//	}
	
	public static Drawable buildCarIconSymbol(boolean isAlarm)
	{
		String carSymbolPath = "symbol";
		if(isAlarm)
		{
			carSymbolPath += "/poi_red.png";
		}
		else
		{
			carSymbolPath += "/poi_blue.png";
		}
		URL carSymbolURL = CarSymbolizer.class.getResource(carSymbolPath);
		Drawable carSymbol = buildSymbol(carSymbolURL);
		return carSymbol;
	}
	/**
	 * ��������--��~ͼ��
	 * @param iconType
	 * @param direction
	 * @return
	 */
	public static Drawable buildCarIconSymbol(int iconType, double direction){
		String carSymbolPath = "symbol";
		if(iconType==2){
			carSymbolPath += "/1/2.png";
		}else{
			switch (iconType) {
				case 0:
					carSymbolPath += "/2/";											
					break;
				case 1:
					carSymbolPath += "/1/";											
					break;
				default:
					carSymbolPath += "/2/";											
					break;	
			}
			direction %= 360;
			if(direction < 0) direction += 360;				
			int angleTimes = (int) (direction / 22.5);
			double remainder = direction - angleTimes*22.5;
			if (remainder > 11.25) {
				angleTimes++;
			}
			carSymbolPath += "1" + String.valueOf(angleTimes) + ".png";
		}
		URL carSymbolURL = CarSymbolizer.class.getResource(carSymbolPath);
		Drawable carSymbol = buildSymbol(carSymbolURL);
		return carSymbol;
		
	} 
	
	public static Drawable buildCarTrackSymbol(){
		String carSymbolPath = "symbol/pin.png";
		URL carSymbolURL = CarSymbolizer.class.getResource(carSymbolPath);
		Drawable carSymbol = buildSymbol(carSymbolURL);
		return carSymbol;
	}
	
	@SuppressLint("NewApi")
	public  static Drawable buildSymbol(URL symbolURL) {		
		if (symbolURL == null)
			throw new NullPointerException("ͼ����Դ�����ã�");
		InputStream symbolStream = null;
		try {
			symbolStream = symbolURL.openStream();
		} catch (IOException e) {
			return null;
		}
		TypedValue typedValue = new TypedValue();
		typedValue.density = TypedValue.DENSITY_DEFAULT;
		Drawable resultDrawable = Drawable.createFromResourceStream(null, typedValue, symbolStream, symbolURL.getFile());
		try {
			symbolStream.close();
		} catch (IOException e) {
		}
		return resultDrawable;		
	}
	/**
	 * ��ȡͼƬ��Դ
	 * @param symbolURL
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static NinePatchDrawable buildNinePatchSymbol(URL symbolURL) {		
		if (symbolURL == null)
			throw new NullPointerException("9Patchͼ����Դ�����ã�");
		InputStream symbolStream = null;
		try {
			symbolStream = symbolURL.openStream();
		} catch (IOException e) {
			return null;
		}
		Bitmap bitmap = BitmapFactory.decodeStream(symbolStream);
		byte[] chunk = bitmap.getNinePatchChunk();
		if(chunk == null || !NinePatch.isNinePatchChunk(chunk)) return null;
		return new NinePatchDrawable(bitmap, chunk, new Rect(), null); 
	}
	public static Paint getCarLabelBackPain(int iconType) {
		int color = Color.BLUE;
		switch(iconType){
			case 1:
				color = Color.RED;
				break;
			case 2:
				color = Color.RED;
				break;
		}
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setColor(color);
		paint.setAlpha(160);
		paint.setStrokeWidth(2);		
		paint.setStrokeJoin(Paint.Join.ROUND);		
		paint.setShadowLayer(2, 0, 0, Color.WHITE);
		return paint;
	}
	public static Paint getCarLabelPaint() {
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);		
		paint.setTextSize(20);
		return paint;	
	}
	/**
	 * �����ַ���
	 * @param paint
	 * @param str
	 * @return
	 */
	public static float getFontLength(Paint paint,String str){
		return paint.measureText(str);
	}
	/**
	 * �����ַ�߶�
	 * @param paint
	 * @return
	 */
	public static float getFontHeight(Paint paint){
		FontMetrics fm=paint.getFontMetrics(); 
		return fm.descent-fm.ascent; 
	}
	/**
	 * ���������׼�߸߶�
	 * @param paint
	 * @return
	 */
	public static float getFontLeading(Paint paint)  {    
        FontMetrics fm = paint.getFontMetrics();   
        return fm.leading- fm.ascent;    
    }  
}