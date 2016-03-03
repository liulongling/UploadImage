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
 * 选择图片
 * @author liulongling
 *
 */
public class ImageChoiseActivity extends BaseActivity implements OnImageDirSelected,UploadImage{

	private final String TAG = "ImageChoiseActivity";

	/*****选择相册******/
	@Bind(R.id.tv_choose_dir)
	TextView mChooseDir;

	/*****所有图片******/
	@Bind(R.id.gv_gridView)
	GridView mGirdView;

	/*******上传图片数量*******/
	@Bind(R.id.tv_total_count)
	TextView mImageCount;

	@Bind(R.id.ll_bottom_ly)
	LinearLayout mBottomLy;

	@Bind(R.id.iv_not_select)
	ImageView notSelect;
	//原图大小
	@Bind(R.id.tv_image_base)
	TextView mBaseImageSize;

	/***预览图片**/
	@Bind(R.id.tv_preview)
	TextView mPreview;

	private ProgressDialog mProgressDialog;

	/*****全部图片******/
	private List<ImageItem> mAllImgs;

	private ImageChoiseAdapter mAdapter;
	/**
	 * 临时的辅助类，用于防止同一个文件夹的多次扫描
	 */
	private HashSet<String> mDirPaths = new HashSet<String>();

	/**
	 * 扫描拿到所有的图片文件夹
	 */
	private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();

	int totalCount = 0;

	private int mScreenHeight;
	private ListImageDirPopupWindow mListImageDirPopupWindow;

	/**
	 * 用户选择的图片，存储为图片的完整路径
	 */
	public static List<ImageItem> mSelectedImage = new LinkedList<ImageItem>();


	public long lessonId = 0;

	//原图
	private boolean mBaseImage;
	//所有图片里的第一张图片路径
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
		//底部透明
		mBottomLy.setAlpha((float)0.8);
	}


	private Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			mProgressDialog.dismiss();
			// 为View绑定数据
			data2View();
			// 初始化展示文件夹的popupWindw
			initListDirPopupWindw();
		}
	};


	/**
	 * 为View绑定数据
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

		//全部图片第一张显示相机
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
			floder.setName("所有图片");
			mImageFloders.add(0, floder);
		}

		mAdapter = new ImageChoiseAdapter(getApplicationContext(), mAllImgs,R.layout.grid_item);
		mAdapter.setBackImagePath(this);
		mGirdView.setAdapter(mAdapter);
	};


	//将时间从小到大排序
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
	 * 初始化展示文件夹的popupWindw
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
				// 设置背景颜色变暗
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1.0f;
				getWindow().setAttributes(lp);
			}
		});
		// 设置选择文件夹的回调
		mListImageDirPopupWindow.setOnImageDirSelected(this);
	}


	/**
	 * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
	 */
	private void getImages()
	{
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
		{
			Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
			return;
		}
		// 显示进度条
		mProgressDialog = ProgressDialog.show(this, null, "正在加载...");

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{

				String firstImage = null;

				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = ImageChoiseActivity.this.getContentResolver();

				// 构造相册索引
				String columns[] = new String[] { Media._ID, Media.BUCKET_ID,
						Media.DATA, Media.BUCKET_DISPLAY_NAME,Media.DATE_MODIFIED };
				// 得到一个游标
				Cursor mCursor = mContentResolver.query(
						Media.EXTERNAL_CONTENT_URI, columns, null, null, null);

				// 获取指定列的索引
				int photoPathIndex = mCursor.getColumnIndexOrThrow(Media.DATA);
				int bucketDisplayNameIndex = mCursor.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);

				while (mCursor.moveToNext())
				{
					// 获取图片的路径
					String path = mCursor.getString(photoPathIndex);
					String bucketName = mCursor.getString(bucketDisplayNameIndex);

					// 拿到第一张图片的路径
					if (firstImage == null)
						firstImage = path;

					// 获取该图片的父路径名
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
					// 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
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
						// 初始化imageFloder
						imageFloder = new ImageFloder();
						imageFloder.setDir(dirPath);
						imageFloder.setName(bucketName);
						imageFloder.setFirstImagePath(path+"");
						imageFloder.addImageItem(imageItem);
					}

					mImageFloders.add(imageFloder);
				}
				mCursor.close();

				// 扫描完成，辅助的HashSet也就可以释放内存了
				mDirPaths = null;

				// 通知Handler扫描图片完成
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
				ToastUtil.showShort(this, "请至少选择一张图片");
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
				mBaseImageSize.setText("原图");
			}
			
			break;
		case R.id.tv_choose_dir:
			mListImageDirPopupWindow.setAnimationStyle(R.style.anim_popup_dir);
			mListImageDirPopupWindow.showAsDropDown(mBottomLy, 0, 0);

			// 设置背景颜色变暗
			WindowManager.LayoutParams lp = getWindow().getAttributes();
			lp.alpha = .3f;
			getWindow().setAttributes(lp);
			break;
		case R.id.tv_preview:
			if(mSelectedImage.size() == 0){
				ToastUtil.showShort(this, "请至少选择一张图片");
				return;
			}
			//上传前将图片存储到本地数据库
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
	 * 显示原图大小
	 */
	public void setBaseImageSize(){
		if(!mBaseImage){
			return;
		}
		long size = 0;
		for(ImageItem item:mSelectedImage){
			size +=item.imageSize;
		}
		mBaseImageSize.setText("原图"+(size > 0 ?"("+FileUtil.FormetFileSize(size)+")":""));
	}

	/**
	 * 添加图片路径到缓存
	 */
	@Override
	public void addImage(ImageItem path) {
		mSelectedImage.add(path);
		setImageCount();
		setBaseImageSize();
	}

	/**
	 * 从缓存中移除该图片
	 */
	@Override
	public void removeImage(ImageItem path) {
		mSelectedImage.remove(path);
		setImageCount();
		setBaseImageSize();
	}

	/**
	 * 显示上传数量/上传最大数量
	 */
	public void setImageCount(){
		mImageCount.setText("("+mSelectedImage.size()+"/"+CustomConstants.MAX_IMAGE_SIZE+")");
	}

	/**
	 * 开启相机
	 */
	@Override
	public void startCamera() {
		String state = Environment.getExternalStorageState(); // 判断是否存在sd卡  
		if (state.equals(Environment.MEDIA_MOUNTED)) { // 直接调用系统的照相机  
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
			startActivityForResult(intent, CustomConstants.CAMERA_REQUEST_CODE);
		} else {  
			Toast.makeText(ImageChoiseActivity.this, "请检查手机是否有SD卡", Toast.LENGTH_LONG).show();  
		}  
	}

	/**
	 * 拍照返回结果
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		} else {
			switch (requestCode) {
			case CustomConstants.CAMERA_REQUEST_CODE:// 现拍
				Bitmap photo = null;  
				// 两种方式获得拍摄照片的返回值  
				Uri uri = data.getData();  
				if (uri != null) {  
					photo = BitmapFactory.decodeFile(uri.getPath());  
				}  
				if (photo == null) {  
					Bundle bundle = data.getExtras();  
					if (bundle != null) {  
						photo = (Bitmap) bundle.get("data");  
						String imageName = FileUtil.savePicInLocal(photo);// 保存到本地  
						ImageItem item = new ImageItem();
						item.sourcePath = imageName;
						mSelectedImage.add(item);
//						startImageZoomActivity(lessonId,item.sourcePath);
					} else {  
						ToastUtil.showShort(this, "未拍摄照片");
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

