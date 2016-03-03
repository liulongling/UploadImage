package com.lll.app.utils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lll.app.bean.LessonPhoto;
import com.lll.app.cache.DoubleCache;
import com.lll.app.finals.ClientCode;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

/**
 * 同步上传图片
 * @author liulongling
 *
 */
public class UploadImageUtil {
	private static final String TAG = "UploadImageUtil";

	public static UploadImageUtil mImageUtil;

	/**
	 * 创建一个可重用固定线程数的线程池，以共享的无界队列方式来运行这些线程。
	 */
	ExecutorService singleThreadExecutor;

	/**
	 * 记录所有正在上传或等待上传的任务。
	 */
	private Set<UploadWorkerTask> taskCollection;

	public Context mContext;

	//最大线程数
	private int MAX_THREAD   = 3;

	private UploadImageUtil(){}

	public static UploadImageUtil getInstanse(Context context){
		if(mImageUtil == null){
			synchronized (DoubleCache.class) {
				if(mImageUtil == null){
					mImageUtil = new UploadImageUtil(context);
				}
			}
		}
		return mImageUtil;
	}


	private UploadImageUtil(Context context){
		this.mContext = context;
		taskCollection = new HashSet<UploadWorkerTask>();
		singleThreadExecutor = Executors.newSingleThreadExecutor();
	}

	/**
	 * 
	 *  警告：如果有多张图片要上传 一定要在读取 存储图片到应用缓存时加上同步 否则会导致内存溢出
	 *  将图片添加到线程 等待上传
	 * @param url
	 * @param baseImage
	 * @param id
	 * @param lessonId
	 */
	public void putImage(String url,boolean baseImage,long id,int status){
		UploadWorkerTask task = new UploadWorkerTask();
		taskCollection.add(task);
		task.execute(url,baseImage==true?"1":"",String.valueOf(id),String.valueOf(status));
	}


	/**
	 * 异步压缩图片并存放到应用缓存
	 * 
	 */
	class UploadWorkerTask extends AsyncTask<String, Void, Bitmap> {

		/**
		 * 图片的URL地址
		 */
		private String url;
		/**
		 * 原图
		 */
		private String baseImage;

		/**
		 * 与后台定义的标示 用来更新上传图片状态
		 */
		private String index;
		/**
		 * status == 1 需要上传的图片
		 */
		private String status;


		@Override
		protected Bitmap doInBackground(String... params) {
			url = params[0];
			baseImage = params[1];
			index =params[2];
			status =params[3];

			try {
				//从本地获取图片
				Bitmap bitmap = BitmapUtil.getLoacalBitmap(url);
				//图片不存在 删除
				if(bitmap == null){
					return null;
				}
				//图片存储到应用缓存中
				if(bitmap!=null){
					DoubleCache.getInstanse(UploadImageUtil.this.mContext).setBaseImage(baseImage.equals(1)?true:false).put(url,bitmap);
					//图片用完后要释放掉内存
					if(bitmap.isRecycled()){
						bitmap.recycle();
					}
				}
				if(Integer.parseInt(status) == ClientCode.LessonPhoto.UPLOADING){
//					LessonPhoto bean = LessonPhotoDAO.getLessonPhotoById(Long.parseLong(index));
//					if(bean!=null){
//						uploadImage(bean);
//					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			super.onPostExecute(bitmap);

		}
	}

	/**
	 * 上传图片
	 * @param bean
	 */
	public void uploadImage(LessonPhoto bean){
		if(bean == null){
			return;
		}
		//从缓存中读取图片
		Bitmap bitmap = DoubleCache.getInstanse(UploadImageUtil.this.mContext).getBitmapFromMemoryCache(bean.sourcePath);
		if(bitmap == null){
			//缓存中没有 从本地读取
			bitmap = DoubleCache.getInstanse(UploadImageUtil.this.mContext).get(bean.sourcePath);
		}
		if(bitmap!=null){
//			threadExecute(ServiceCode.UPLOAD_IMAGE, bitmap, new Json5016Bean(String.valueOf(bean.photoId), bean.id));
		}
	}

	/**
	 * 自定义线程池 每次执行一定数量的操作
	 * @param activeId
	 * @param bitmap
	 * @param object
	 */
	public void threadExecute(final int activeId, final Bitmap bitmap, final Object object) {
		singleThreadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					String icon = null;
					//图片用完后一定要释放掉内存
					if(bitmap!=null){
						icon = BitmapUtil.bitmapToBase64(bitmap);
						if(bitmap.isRecycled()){
							bitmap.recycle();
						}
					}
					if(icon == null){
						return;
					}
					switch (activeId) {
//					case ServiceCode.UPLOAD_IMAGE:
//						Json5016Bean bean = ((Json5016Bean)object);
//						AppLog.debug(TAG, bean.index+"正在运行");
//						bean.noteimg = icon;
//						Service.getInstanse().getSender().put(activeId, bean);
//						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
