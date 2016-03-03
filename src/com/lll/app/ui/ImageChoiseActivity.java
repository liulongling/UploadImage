package com.lll.app.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.lll.app.R;
import com.lll.app.adapter.ImageChoiseAdapter;
import com.lll.app.bean.ImageFloder;
import com.lll.app.bean.ImageItem;
import com.lll.app.finals.CustomConstants;
import com.lll.app.interfaces.UploadImage;
import com.lll.app.ui.base.BaseActivity;
import com.lll.app.utils.AppManager;
import com.lll.app.utils.FileUtil;
import com.lll.app.utils.ScreenUtils;
import com.lll.app.utils.ToastUtil;
import com.lll.app.view.ListImageDirPopupWindow;
import com.lll.app.view.ListImageDirPopupWindow.OnImageDirSelected;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ѡ��ͼƬ
 * @author liulongling
 *
 */
public class ImageChoiseActivity extends BaseActivity implements OnImageDirSelected,UploadImage{

	private final String TAG = "ImageChoiseActivity";

	/*****ѡ�����******/
	@Bind(R.id.tv_choose_dir)
	TextView mChooseDir;

	/*****����ͼƬ******/
	@Bind(R.id.gv_gridView)
	GridView mGirdView;

	/*******�ϴ�ͼƬ����*******/
	@Bind(R.id.tv_total_count)
	TextView mImageCount;

	@Bind(R.id.ll_bottom_ly)
	LinearLayout mBottomLy;

	@Bind(R.id.iv_not_select)
	ImageView notSelect;
	//ԭͼ��С
	@Bind(R.id.tv_image_base)
	TextView mBaseImageSize;

	/***Ԥ��ͼƬ**/
	@Bind(R.id.tv_preview)
	TextView mPreview;

	private ProgressDialog mProgressDialog;

	/*****ȫ��ͼƬ******/
	private List<ImageItem> mAllImgs;

	private ImageChoiseAdapter mAdapter;
	/**
	 * ��ʱ�ĸ����࣬���ڷ�ֹͬһ���ļ��еĶ��ɨ��
	 */
	private HashSet<String> mDirPaths = new HashSet<String>();

	/**
	 * ɨ���õ����е�ͼƬ�ļ���
	 */
	private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();

	int totalCount = 0;

	private int mScreenHeight;
	private ListImageDirPopupWindow mListImageDirPopupWindow;

	/**
	 * �û�ѡ���ͼƬ���洢ΪͼƬ������·��
	 */
	public static List<ImageItem> mSelectedImage = new LinkedList<ImageItem>();


	public long lessonId = 0;

	//ԭͼ
	private boolean mBaseImage;
	//����ͼƬ��ĵ�һ��ͼƬ·��
	private String firstImagePath;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_images);
		ButterKnife.bind(this);

		initData();
		getImages();
		setImageCount();
	}


	private void initData(){
		mSelectedImage.clear();

		mScreenHeight = ScreenUtils.getScreenHeight(this);
		//�ײ�͸��
		mBottomLy.setAlpha((float)0.8);
	}


	private Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			mProgressDialog.dismiss();
			// ΪView������
			data2View();
			// ��ʼ��չʾ�ļ��е�popupWindw
			initListDirPopupWindw();
		}
	};


	/**
	 * ΪView������
	 */
	private void data2View()
	{
		if(mAllImgs == null){
			mAllImgs = new ArrayList<ImageItem>();
			for(ImageFloder floder:mImageFloders){
				if(floder.getImageItems()!=null){
					mAllImgs.addAll(floder.getImageItems());
				}
			}
		}

		Collections.sort(mAllImgs, new ImageItemComparator());

		if(firstImagePath == null&&mAllImgs.size() > 0){
			firstImagePath = mAllImgs.get(0).sourcePath;
		}

		//ȫ��ͼƬ��һ����ʾ���
		ImageItem item = new ImageItem();
		item.sourcePath = "camera";
		mAllImgs.add(0, item);

		boolean firstFloder = false;
		for(ImageFloder floder:mImageFloders){
			if(floder.getDir() == null){
				firstFloder = true;
				break;
			}
		}
		if(!firstFloder){
			ImageFloder floder = new ImageFloder();
			floder.setFirstImagePath(firstImagePath);
			floder.setName("����ͼƬ");
			mImageFloders.add(0, floder);
		}

		mAdapter = new ImageChoiseAdapter(getApplicationContext(), mAllImgs,R.layout.grid_item);
		mAdapter.setBackImagePath(this);
		mGirdView.setAdapter(mAdapter);
	};


	//��ʱ���С��������
	public class ImageItemComparator implements Comparator<ImageItem>{  
		public int compare(ImageItem o1, ImageItem o2) {  
			if(o1!=null&&o2!=null)  
			{  
				return o2.imageTime > o1.imageTime ? 1 : -1;
			} 
			return 0;  
		}  
	}  

	/**
	 * ��ʼ��չʾ�ļ��е�popupWindw
	 */
	private void initListDirPopupWindw()
	{
		mListImageDirPopupWindow = new ListImageDirPopupWindow(
				LayoutParams.MATCH_PARENT, (int) (mScreenHeight * 0.7),
				mImageFloders, LayoutInflater.from(getApplicationContext())
				.inflate(R.layout.list_dir, null));

		mListImageDirPopupWindow.setOnDismissListener(new OnDismissListener()
		{

			@Override
			public void onDismiss()
			{
				// ���ñ�����ɫ�䰵
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1.0f;
				getWindow().setAttributes(lp);
			}
		});
		// ����ѡ���ļ��еĻص�
		mListImageDirPopupWindow.setOnImageDirSelected(this);
	}


	/**
	 * ����ContentProviderɨ���ֻ��е�ͼƬ���˷��������������߳��� ���ͼƬ��ɨ�裬���ջ��jpg�����Ǹ��ļ���
	 */
	private void getImages()
	{
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
		{
			Toast.makeText(this, "�����ⲿ�洢", Toast.LENGTH_SHORT).show();
			return;
		}
		// ��ʾ������
		mProgressDialog = ProgressDialog.show(this, null, "���ڼ���...");

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{

				String firstImage = null;

				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = ImageChoiseActivity.this.getContentResolver();

				// �����������
				String columns[] = new String[] { Media._ID, Media.BUCKET_ID,
						Media.DATA, Media.BUCKET_DISPLAY_NAME,Media.DATE_MODIFIED };
				// �õ�һ���α�
				Cursor mCursor = mContentResolver.query(
						Media.EXTERNAL_CONTENT_URI, columns, null, null, null);

				// ��ȡָ���е�����
				int photoPathIndex = mCursor.getColumnIndexOrThrow(Media.DATA);
				int bucketDisplayNameIndex = mCursor.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);

				while (mCursor.moveToNext())
				{
					// ��ȡͼƬ��·��
					String path = mCursor.getString(photoPathIndex);
					String bucketName = mCursor.getString(bucketDisplayNameIndex);

					// �õ���һ��ͼƬ��·��
					if (firstImage == null)
						firstImage = path;

					// ��ȡ��ͼƬ�ĸ�·����
					File file = new File(path);

					File parentFile = file.getParentFile();
					if (parentFile == null)
						continue;

					long time = file.lastModified();

					String dirPath = parentFile.getAbsolutePath();

					ImageItem imageItem = new ImageItem();
					imageItem.imageTime = time;
					imageItem.sourcePath = path;
					imageItem.imageSize = FileUtil.getFileSize(file);


					ImageFloder imageFloder = null;
					// ����һ��HashSet��ֹ���ɨ��ͬһ���ļ��У���������жϣ�ͼƬ�����������൱�ֲ���~~��
					if (mDirPaths.contains(dirPath))
					{
						for(ImageFloder floder:mImageFloders){
							if(floder.getDir().equals(dirPath)){
								floder.addImageItem(imageItem);
								break;
							}
						}
						continue;
					} else
					{
						mDirPaths.add(dirPath);
						// ��ʼ��imageFloder
						imageFloder = new ImageFloder();
						imageFloder.setDir(dirPath);
						imageFloder.setName(bucketName);
						imageFloder.setFirstImagePath(path+"");
						imageFloder.addImageItem(imageItem);
					}

					mImageFloders.add(imageFloder);
				}
				mCursor.close();

				// ɨ����ɣ�������HashSetҲ�Ϳ����ͷ��ڴ���
				mDirPaths = null;

				// ֪ͨHandlerɨ��ͼƬ���
				mHandler.sendEmptyMessage(0x110);

			}
		}).start();

	}



	@Override
	public void selected(ImageFloder floder)
	{
		if(floder.getImageItems() == null){
			mAdapter = new ImageChoiseAdapter(getApplicationContext(),mAllImgs,R.layout.grid_item);
		}else{
			mAdapter = new ImageChoiseAdapter(getApplicationContext(),floder.getImageItems(),R.layout.grid_item);
		}

		mAdapter.setBackImagePath(this);
		mGirdView.setAdapter(mAdapter);
		mChooseDir.setText(floder.getName());
		mListImageDirPopupWindow.dismiss();
	}


	@OnClick({R.id.ll_upload,R.id.iv_not_select,R.id.tv_choose_dir,R.id.tv_preview})
	public void onClick(View view){
		switch (view.getId()) {
		case R.id.ll_upload:
			if(mSelectedImage.size() == 0){
				ToastUtil.showShort(this, "������ѡ��һ��ͼƬ");
				return;
			}
			AppManager.getAppManager().finishActivity(this);
			final Activity curAct = AppManager.getAppManager().currentActivity();
			if(curAct!=null&&curAct instanceof MainActivity){
				((MainActivity)curAct).updateImageData(mSelectedImage,mBaseImage);
				mSelectedImage.clear();
			}
			break;
		case R.id.iv_not_select:
			mBaseImage = !mBaseImage;
			if(mBaseImage){
				setBaseImageSize();
				notSelect.setImageResource(R.drawable.btn_hightedchose_green);
			}else{
				notSelect.setImageResource(R.drawable.btn_normalchose_gray);
				mBaseImageSize.setText("ԭͼ");
			}
			
			break;
		case R.id.tv_choose_dir:
			mListImageDirPopupWindow.setAnimationStyle(R.style.anim_popup_dir);
			mListImageDirPopupWindow.showAsDropDown(mBottomLy, 0, 0);

			// ���ñ�����ɫ�䰵
			WindowManager.LayoutParams lp = getWindow().getAttributes();
			lp.alpha = .3f;
			getWindow().setAttributes(lp);
			break;
		case R.id.tv_preview:
			if(mSelectedImage.size() == 0){
				ToastUtil.showShort(this, "������ѡ��һ��ͼƬ");
				return;
			}
			//�ϴ�ǰ��ͼƬ�洢���������ݿ�
			String[] photos = new String[mSelectedImage.size()];
			int i=0;
			for(ImageItem item:mSelectedImage){
				photos[i]  = item.sourcePath;
				i++;
			}
			Bundle bundle = new Bundle();
			bundle.putStringArray(CustomConstants.EXTRA_IMAGE_LIST, photos);
			openActivity(ImageZoomActivity.class,bundle);
			break;

		default:
			break;
		}
	}

	/**
	 * ��ʾԭͼ��С
	 */
	public void setBaseImageSize(){
		if(!mBaseImage){
			return;
		}
		long size = 0;
		for(ImageItem item:mSelectedImage){
			size +=item.imageSize;
		}
		mBaseImageSize.setText("ԭͼ"+(size > 0 ?"("+FileUtil.FormetFileSize(size)+")":""));
	}

	/**
	 * ���ͼƬ·��������
	 */
	@Override
	public void addImage(ImageItem path) {
		mSelectedImage.add(path);
		setImageCount();
		setBaseImageSize();
	}

	/**
	 * �ӻ������Ƴ���ͼƬ
	 */
	@Override
	public void removeImage(ImageItem path) {
		mSelectedImage.remove(path);
		setImageCount();
		setBaseImageSize();
	}

	/**
	 * ��ʾ�ϴ�����/�ϴ��������
	 */
	public void setImageCount(){
		mImageCount.setText("("+mSelectedImage.size()+"/"+CustomConstants.MAX_IMAGE_SIZE+")");
	}

	/**
	 * �������
	 */
	@Override
	public void startCamera() {
		String state = Environment.getExternalStorageState(); // �ж��Ƿ����sd��  
		if (state.equals(Environment.MEDIA_MOUNTED)) { // ֱ�ӵ���ϵͳ�������  
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
			startActivityForResult(intent, CustomConstants.CAMERA_REQUEST_CODE);
		} else {  
			Toast.makeText(ImageChoiseActivity.this, "�����ֻ��Ƿ���SD��", Toast.LENGTH_LONG).show();  
		}  
	}

	/**
	 * ���շ��ؽ��
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		} else {
			switch (requestCode) {
			case CustomConstants.CAMERA_REQUEST_CODE:// ����
				Bitmap photo = null;  
				// ���ַ�ʽ���������Ƭ�ķ���ֵ  
				Uri uri = data.getData();  
				if (uri != null) {  
					photo = BitmapFactory.decodeFile(uri.getPath());  
				}  
				if (photo == null) {  
					Bundle bundle = data.getExtras();  
					if (bundle != null) {  
						photo = (Bitmap) bundle.get("data");  
						String imageName = FileUtil.savePicInLocal(photo);// ���浽����  
						ImageItem item = new ImageItem();
						item.sourcePath = imageName;
						mSelectedImage.add(item);
//						startImageZoomActivity(lessonId,item.sourcePath);
					} else {  
						ToastUtil.showShort(this, "δ������Ƭ");
					}  
				}  
				break;
			}
		}
	}


	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}
}

