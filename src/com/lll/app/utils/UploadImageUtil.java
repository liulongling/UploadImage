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
 * ͬ���ϴ�ͼƬ
 * @author liulongling
 *
 */
public class UploadImageUtil {
	private static final String TAG = "UploadImageUtil";

	public static UploadImageUtil mImageUtil;

	/**
	 * ����һ�������ù̶��߳������̳߳أ��Թ�����޽���з�ʽ��������Щ�̡߳�
	 */
	ExecutorService singleThreadExecutor;

	/**
	 * ��¼���������ϴ���ȴ��ϴ�������
	 */
	private Set<UploadWorkerTask> taskCollection;

	public Context mContext;

	//����߳���
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
	 *  ���棺����ж���ͼƬҪ�ϴ� һ��Ҫ�ڶ�ȡ �洢ͼƬ��Ӧ�û���ʱ����ͬ�� ����ᵼ���ڴ����
	 *  ��ͼƬ��ӵ��߳� �ȴ��ϴ�
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
	 * �첽ѹ��ͼƬ����ŵ�Ӧ�û���
	 * 
	 */
	class UploadWorkerTask extends AsyncTask<String, Void, Bitmap> {

		/**
		 * ͼƬ��URL��ַ
		 */
		private String url;
		/**
		 * ԭͼ
		 */
		private String baseImage;

		/**
		 * ���̨����ı�ʾ ���������ϴ�ͼƬ״̬
		 */
		private String index;
		/**
		 * status == 1 ��Ҫ�ϴ���ͼƬ
		 */
		private String status;


		@Override
		protected Bitmap doInBackground(String... params) {
			url = params[0];
			baseImage = params[1];
			index =params[2];
			status =params[3];

			try {
				//�ӱ��ػ�ȡͼƬ
				Bitmap bitmap = BitmapUtil.getLoacalBitmap(url);
				//ͼƬ������ ɾ��
				if(bitmap == null){
					return null;
				}
				//ͼƬ�洢��Ӧ�û�����
				if(bitmap!=null){
					DoubleCache.getInstanse(UploadImageUtil.this.mContext).setBaseImage(baseImage.equals(1)?true:false).put(url,bitmap);
					//ͼƬ�����Ҫ�ͷŵ��ڴ�
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
	 * �ϴ�ͼƬ
	 * @param bean
	 */
	public void uploadImage(LessonPhoto bean){
		if(bean == null){
			return;
		}
		//�ӻ����ж�ȡͼƬ
		Bitmap bitmap = DoubleCache.getInstanse(UploadImageUtil.this.mContext).getBitmapFromMemoryCache(bean.sourcePath);
		if(bitmap == null){
			//������û�� �ӱ��ض�ȡ
			bitmap = DoubleCache.getInstanse(UploadImageUtil.this.mContext).get(bean.sourcePath);
		}
		if(bitmap!=null){
//			threadExecute(ServiceCode.UPLOAD_IMAGE, bitmap, new Json5016Bean(String.valueOf(bean.photoId), bean.id));
		}
	}

	/**
	 * �Զ����̳߳� ÿ��ִ��һ�������Ĳ���
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
					//ͼƬ�����һ��Ҫ�ͷŵ��ڴ�
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
//						AppLog.debug(TAG, bean.index+"��������");
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
