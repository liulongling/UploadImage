package com.lll.app.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import com.lll.app.R;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.ImageView;


public class ImageDisplayer
{
	public final String TAG = getClass().getSimpleName();

	private static ImageDisplayer instance;
	private Context context;
	private static final int THUMB_WIDTH = 256;
	private static final int THUMB_HEIGHT = 256;
	public Handler h = new Handler();
	private HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();

	public static ImageDisplayer getInstance(Context context)
	{
		if (instance == null)
		{
			synchronized (ImageDisplayer.class)
			{
				instance = new ImageDisplayer(context);
			}
		}
		return instance;
	}

	public ImageDisplayer(Context context)
	{
		if (context!= null)
		{
			this.context = context;
		}
		else
		{
			this.context = context;
		}

	}


	public void put(String key, Bitmap bmp)
	{
		if (!TextUtils.isEmpty(key) && bmp != null)
		{
			imageCache.put(key, new SoftReference<Bitmap>(bmp));
		}
	}

	/**
	 * ��ʾͼƬ
	 * @param iv
	 * @param thumbPath    ����ͼ·��
	 * @param sourcePath   ͼƬ·��
	 */
	public void displayBmp(final ImageView iv, final String thumbPath,
			final String sourcePath)
	{
		displayBmp(iv, thumbPath, sourcePath, true);
	}

	public void displayBmp(final ImageView iv, final String thumbPath,final String sourcePath, final boolean showThumb)
	{
		if (TextUtils.isEmpty(thumbPath) && TextUtils.isEmpty(sourcePath))
		{
			AppLog.debug(TAG, "no paths pass in");
			return;
		}

		if (iv.getTag() != null && iv.getTag().equals(sourcePath))
		{
			return;
		}

//		showDefault(iv);

		final String path;
		if (!TextUtils.isEmpty(thumbPath) && showThumb)
		{
			path = thumbPath;
		}
		else if (!TextUtils.isEmpty(sourcePath))
		{
			path = sourcePath;
		}
		else
		{
			return;
		}

		iv.setTag(path);

		if (imageCache.containsKey(showThumb ? path + THUMB_WIDTH+ THUMB_HEIGHT : path))
		{
			SoftReference<Bitmap> reference = imageCache.get(showThumb ? path+ THUMB_WIDTH + THUMB_HEIGHT : path);
			Bitmap imgInCache = reference.get();
			if (imgInCache != null)
			{
				refreshView(iv, imgInCache, path);
				return;
			}
		}
		iv.setImageBitmap(null);

		new Thread()
		{
			Bitmap img;

			public void run()
			{

				try
				{
					//					if (path != null && path.equals(thumbPath))
					if (path != null&& path.equals(thumbPath))
					{
						img = BitmapFactory.decodeFile(path);
					}
					if (img == null)
					{
						img = compressImg(sourcePath, showThumb);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

				if (img != null)
				{
					put(showThumb ? path + THUMB_WIDTH + THUMB_HEIGHT : path,img);

				}
				h.post(new Runnable()
				{
					@Override
					public void run()
					{
						refreshView(iv, img, path);
					}
				});
			}
		}.start();

	}

	private void refreshView(ImageView imageView, Bitmap bitmap, String path)
	{
		if (imageView != null && bitmap != null)
		{
			if (path != null)
			{
				((ImageView) imageView).setImageBitmap(bitmap);
				imageView.setTag(path);
			}
		}
	}

	private void showDefault(ImageView iv)
	{
		iv.setBackgroundResource(R.drawable.bg_image);
	}

	public Bitmap compressImg(String path, boolean showThumb)throws IOException{
		AppLog.debug(TAG, "path:"+path);
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
		BitmapFactory.Options opt = new BitmapFactory.Options();
		// �������������Ϊbitmap�����ڴ�ռ䣬ֻ��¼һЩ��ͼƬ����Ϣ������ͼƬ��С�����ڴ��Ż�  
		opt.inJustDecodeBounds = true;
		// ͨ������ͼƬ�ķ�ʽ��ȡ��options������
		BitmapFactory.decodeStream(in, null, opt);
		//�ر���
		in.close();

		int i = 0;
		Bitmap bitmap = null;
		if (showThumb)
		{
			while (true)
			{
				if ((opt.outWidth >> i <= THUMB_WIDTH)&& (opt.outHeight >> i <= THUMB_HEIGHT))
				{
					in = new BufferedInputStream(new FileInputStream(new File(path)));
					opt.inSampleSize = (int) Math.pow(2.0D, i);
					// ����֮ǰ����Ϊ��true������Ҫ��Ϊfalse������ʹ�������ͼƬ 
					opt.inJustDecodeBounds = false;
					bitmap = BitmapFactory.decodeStream(in, null, opt);
					break;
				}
				i += 1;
			}
		}
		else
		{
			while (true)
			{
				if ((opt.outWidth >> i <= ScreenUtils.getScreenWidth((Activity)context))&& (opt.outHeight >> i <=ScreenUtils.getScreenHeight((Activity)context)))
				{
					in = new BufferedInputStream(new FileInputStream(new File(path)));
					opt.inSampleSize = (int) Math.pow(2.0D, i);
					// ����֮ǰ����Ϊ��true������Ҫ��Ϊfalse������ʹ�������ͼƬ 
					opt.inJustDecodeBounds = false;
					bitmap = BitmapFactory.decodeStream(in, null, opt);
					break;
				}
				i += 1;
			}
		}
		return bitmap;
	}

	public interface ImageCallback
	{
		public void imageLoad(ImageView imageView, Bitmap bitmap,
				Object... params);
	}
}
