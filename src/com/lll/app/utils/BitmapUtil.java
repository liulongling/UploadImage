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
 * ����Bitmap��byte[] Drawable����ת��
 */
public class BitmapUtil {
	
	/**
	 * ��ȡͼƬ
	 * @param id
	 * @return
	 */
	public static Drawable getDrawble(Context context,int id){
		Drawable drawable=context.getResources().getDrawable(id);
		/// ��һ������Ҫ��,���򲻻���ʾ.
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		return drawable;
	}
	
	/**
	 * ����ʡ�ڴ�ķ�ʽ��ȡ������Դ��ͼƬ
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
		// ��ȡ��ԴͼƬ
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * @param drawable
	 *            drawable ת Bitmap
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		// ȡ drawable �ĳ���
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();

		// ȡ drawable ����ɫ��ʽ
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
		// ������Ӧ bitmap
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		// ������Ӧ bitmap �Ļ���
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		// �� drawable ���ݻ���������
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * @param bitmap
	 * @param roundPx
	 *            ��ȡԲ��ͼƬ
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
	 * ԭͷ��
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap, int width, int height) {
		// �����εı߳�
		int r = 0;
		// ȡ��̱����߳�
		if (width > height) {
			r = height;
		} else {
			r = width;
		}
		// ����һ��bitmap
		Bitmap backgroundBmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		// newһ��Canvas����backgroundBmp�ϻ�ͼ
		Canvas canvas = new Canvas(backgroundBmp);
		Paint paint = new Paint();
		// ���ñ�Ե�⻬��ȥ�����
		paint.setAntiAlias(true);
		// �����ȣ���������
		RectF rect = new RectF(0, 0, r, r);
		// ͨ���ƶ���rect��һ��Բ�Ǿ��Σ���Բ��X�᷽��İ뾶����Y�᷽��İ뾶ʱ��
		// �Ҷ�����r/2ʱ����������Բ�Ǿ��ξ���Բ��
		canvas.drawRoundRect(rect, r / 2, r / 2, paint);
		// ���õ�����ͼ���ཻʱ��ģʽ��SRC_INΪȡSRCͼ���ཻ�Ĳ��֣�����Ľ���ȥ��
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		// canvas��bitmap����backgroundBmp��
		canvas.drawBitmap(bitmap, null, rect, paint);
		// �����Ѿ��滭�õ�backgroundBmp
		return backgroundBmp;
	}

	/**
	 * ���ر���ͼƬ
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
	 * ���͸��ͼ
	 * 
	 * @param width
	 * @param height
	 * @param alpha
	 * @return
	 */
	public static Bitmap getAlphaBitmap(int width, int height, int alpha) {
		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		int[] argb = new int[bitmap.getWidth() * bitmap.getHeight()];
		bitmap.getPixels(argb, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());// ���ͼƬ��ARGBֵ
		alpha = alpha * 255 / 100;
		for (int i = 0; i < argb.length; i++) {
			argb[i] = (alpha << 24) | (argb[i] & 0x00FFFFFF);
		}
		Bitmap b = bitmap.copy(Bitmap.Config.ARGB_8888, true);
		return b;
	}
	
	/**
	 * ����ͼƬΪJPEG
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
	 * bitmapתΪbase64
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
