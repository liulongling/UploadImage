package com.lll.app.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Base64;

/**
 * 关于Bitmap与byte[] Drawable互相转换
 */
public class BitmapUtil {
	
	/**
	 * 获取图片
	 * @param id
	 * @return
	 */
	public static Drawable getDrawble(Context context,int id){
		Drawable drawable=context.getResources().getDrawable(id);
		/// 这一步必须要做,否则不会显示.
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		return drawable;
	}
	
	/**
	 * 以最省内存的方式读取本地资源的图片
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * @param drawable
	 *            drawable 转 Bitmap
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		// 取 drawable 的长宽
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();

		// 取 drawable 的颜色格式
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
		// 建立对应 bitmap
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		// 建立对应 bitmap 的画布
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		// 把 drawable 内容画到画布中
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * @param bitmap
	 * @param roundPx
	 *            获取圆角图片
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, w, h);
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 原头像
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap, int width, int height) {
		// 正方形的边长
		int r = 0;
		// 取最短边做边长
		if (width > height) {
			r = height;
		} else {
			r = width;
		}
		// 构建一个bitmap
		Bitmap backgroundBmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		// new一个Canvas，在backgroundBmp上画图
		Canvas canvas = new Canvas(backgroundBmp);
		Paint paint = new Paint();
		// 设置边缘光滑，去掉锯齿
		paint.setAntiAlias(true);
		// 宽高相等，即正方形
		RectF rect = new RectF(0, 0, r, r);
		// 通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，
		// 且都等于r/2时，画出来的圆角矩形就是圆形
		canvas.drawRoundRect(rect, r / 2, r / 2, paint);
		// 设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		// canvas将bitmap画在backgroundBmp上
		canvas.drawBitmap(bitmap, null, rect, paint);
		// 返回已经绘画好的backgroundBmp
		return backgroundBmp;
	}

	/**
	 * 加载本地图片
	 * 
	 * @param url
	 * @return
	 */
	public static synchronized Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得透明图
	 * 
	 * @param width
	 * @param height
	 * @param alpha
	 * @return
	 */
	public static Bitmap getAlphaBitmap(int width, int height, int alpha) {
		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		int[] argb = new int[bitmap.getWidth() * bitmap.getHeight()];
		bitmap.getPixels(argb, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());// 获得图片的ARGB值
		alpha = alpha * 255 / 100;
		for (int i = 0; i < argb.length; i++) {
			argb[i] = (alpha << 24) | (argb[i] & 0x00FFFFFF);
		}
		Bitmap b = bitmap.copy(Bitmap.Config.ARGB_8888, true);
		return b;
	}
	
	/**
	 * 保存图片为JPEG
	 * 
	 * @param bitmap
	 * @param path
	 */
	public static void saveJPGE_After(Bitmap bitmap, String path) {
		File file = new File(path);
		FileOutputStream bos = null;
		try {
			bos = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)) {
				bos.flush();
				bos.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			CloseUtils.closeQuietly(bos);
		}
	}
	

	/**
	 * bitmap转为base64
	 * 
	 * @param bitmap
	 * @return
	 */
	public static String bitmapToBase64(Bitmap bitmap) {

		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

				baos.flush();
				baos.close();

				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static Bitmap getBitmapFromSD(String path) {
		Bitmap bitmap = null;
		try {
			File file = new File(path);
			if (file.exists()) {
				bitmap = BitmapFactory.decodeFile(path);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return bitmap;
	}
}
