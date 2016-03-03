package com.lll.app.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.lll.app.R;
import com.lll.app.bean.LessonPhoto;
import com.lll.app.dialog.MyDialog;
import com.lll.app.dialog.MyDialog.DialogItemClickListener;
import com.lll.app.finals.CustomConstants;
import com.lll.app.fragment.ImageDetailFragment;
import com.lll.app.ui.base.BaseActivity;
import com.lll.app.utils.AppManager;
import com.lll.app.utils.BitmapUtil;
import com.lll.app.utils.DensityUtils;
import com.lll.app.utils.FileUtil;
import com.lll.app.utils.ScreenUtils;
import com.lll.app.view.HackyViewPager;



import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ImageZoomActivity extends BaseActivity
{
	private static final String TAG = "ImageZoomActivity";

	@Bind(R.id.viewpager)
	HackyViewPager pager;

	@Bind(R.id.rl_titlebar)
	RelativeLayout titlebarLayout;

	//底部布局 
	@Bind(R.id.rl_bottombar)
	LinearLayout bottombarLayout;

	//原图
	@Bind(R.id.btn_base_image)
	Button mBaseImage;

	//上传图片按钮
	@Bind(R.id.ll_upload)
	LinearLayout uploadLayout;

	//删除笔记图片
	@Bind(R.id.iv_delete)
	ImageView mDelete;

	@Bind(R.id.tv_pos)
	TextView mPos;

	//标题高度
	public int titleHeight = 0;

	boolean mOnclick = false;

	private ImagePagerAdapter  mAdapter;
	private int currentPosition;

	//拍照过来的图片
	private String mCameraImage;
	//预览本地图片(未上传)
	private String mPreview;

	private boolean mBaseImageBool = false;

	private List<LessonPhoto> mDataList = new ArrayList<LessonPhoto>();


	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zoom);
		ButterKnife.bind(this);

		initData();
		initView();
	}

	private void initData()
	{
		Intent intent = this.getIntent();

		mCameraImage  =intent.getStringExtra(CustomConstants.EXTRA_CAMERA_IMG);
		mPreview=intent.getStringExtra(CustomConstants.EXTRA_PREVIEW); 
		if(mCameraImage!=null){
			mPos.setText("1/1");
			mDelete.setVisibility(View.GONE);
			uploadLayout.setVisibility(View.VISIBLE);

			LessonPhoto item = new LessonPhoto();
			item.sourcePath = mCameraImage;

			// 获取该图片的父路径名
			File file = new File(item.sourcePath);
			if (file != null) 
			{
				item.imageSize = FileUtil.getFileSize(file);
				mDataList.add(item);
			}

		}else{
			//1.删除按钮显示(上传后)
			if(mPreview == null){
				mDelete.setVisibility(View.VISIBLE);
			}else{
				mDelete.setVisibility(View.GONE);
			}

			//2.上传按钮 原图选项隐藏
			uploadLayout.setVisibility(View.GONE);
			bottombarLayout.setVisibility(View.GONE);

			String[] photos = getIntent().getExtras().getStringArray(CustomConstants.EXTRA_IMAGE_LIST);
			for(String photo:photos){
				LessonPhoto bean = new LessonPhoto();
				bean.sourcePath = photo;
				mDataList.add(bean);
			}
			
			currentPosition = getIntent().getIntExtra(CustomConstants.EXTRA_CURRENT_IMG_POSITION, 0);

			if(mDataList == null || mDataList.size() == 0){
				return;
			}
			mPos.setText(currentPosition+1+"/"+mDataList.size());
			//			mDataList = LessonInfoActivity.mDataList;
		}
	}

	public void initView(){
		//photo_relativeLayout.setVisibility(View.GONE);
		//photo_relativeLayout.setBackgroundColor(0x70000000);

		titleHeight = DensityUtils.dp2px(this, 50);

		titlebarLayout.setAlpha((float)0.8);
		titlebarLayout.setY(-titleHeight);

		if(mBaseImage!=null){
			bottombarLayout.setAlpha((float)0.8);
			bottombarLayout.setY(ScreenUtils.getScreenHeight(this)+titleHeight);
		}

		pager.setOnPageChangeListener(pageChangeListener);

		mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), mDataList);
		pager.setAdapter(mAdapter);
		pager.setCurrentItem(currentPosition);
	}


	private void removeImgs()
	{
		mDataList.clear();
	}

	private void removeImg(int location)
	{
		if (location + 1 <= mDataList.size())
		{
			mDataList.remove(location);
		}
	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener()
	{

		public void onPageSelected(int arg0)
		{
			currentPosition = arg0;
			mPos.setText(currentPosition+1+"/"+mDataList.size());
		}

		public void onPageScrolled(int arg0, float arg1, int arg2)
		{

		}

		public void onPageScrollStateChanged(int arg0)
		{

		}
	};

	/**
	 * 删除图片
	 */
	public void deleteImage(){
		LessonPhoto bean = mDataList.get(currentPosition);
		if(bean!=null){
			if (mDataList.size() == 1)
			{
				removeImgs();
				AppManager.getAppManager().finishActivity(ImageZoomActivity.this);
			}
			else
			{
				removeImg(currentPosition);
				pager.removeAllViews();
				mAdapter.notifyDataSetChanged();
				mPos.setText(currentPosition+1+"/"+mDataList.size());
			}
			//			LessonPhotoDAO.deletePhotoById(bean.getId());
		}
	}


	@OnClick({R.id.iv_back,R.id.iv_delete,R.id.ll_upload,R.id.btn_base_image})
	public void onClick(View view){
		switch (view.getId()) {
		case R.id.iv_back:
			backLastActivity();
			break;
		case R.id.iv_delete:
			MyDialog.showChoiseDialog(this,"删除这张图片?","取消","确定", new DialogItemClickListener() {
				@Override
				public void confirm(String result) {
					LessonPhoto bean = mDataList.get(currentPosition);
					if(bean!=null){
						deleteImage();
					}
				}
			});
			break;
		case R.id.ll_upload:
			if(mDataList.size() > 0){
				//上传前将图片存储到本地数据库
				List<LessonPhoto> photos = new ArrayList<LessonPhoto>();

				LessonPhoto entity = new LessonPhoto();
//				entity.setId(lessonId);
				entity.sourcePath = mDataList.get(0).sourcePath;
				photos.add(entity);
//				LessonPhotoDAO.addLessonPhoto(photos);
				AppManager.getAppManager().finishActivity(this);
			}
			break;
		case R.id.btn_base_image:
			mBaseImageBool =!mBaseImageBool;
			if(mBaseImageBool){
				mBaseImage.setCompoundDrawables(BitmapUtil.getDrawble(this,R.drawable.btn_hightedchose_green), null, null, null);
				if(mDataList.size()>0){
					LessonPhoto item = mDataList.get(0);
					mBaseImage.setText("原图("+FileUtil.FormetFileSize(item.imageSize)+")");
				}
			}else{
				mBaseImage.setCompoundDrawables(BitmapUtil.getDrawble(this,R.drawable.btn_normalchose_gray), null, null, null);
			}

			break;
		default:
			break;
		}
	}

	private class ImagePagerAdapter extends FragmentPagerAdapter implements ImageDetailFragment.OnImageClickListener{

		private List<LessonPhoto> dataList ;

		public ImagePagerAdapter(FragmentManager fm, List<LessonPhoto> dataList) {
			super(fm);
			this.dataList = dataList;
		}

		@Override
		public int getItemPosition(Object object){
			return PagerAdapter.POSITION_NONE;
		}

		@Override
		public int getCount() {
			return dataList == null ? 0 : dataList.size();
		}

		@Override
		public Fragment getItem(int position) {
			LessonPhoto bean = dataList.get(position);
			return ImageDetailFragment.newInstance(bean.sourcePath,bean.image_url+bean.bignoteimg,this);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			super.destroyItem(container, position, object);
			FragmentManager manager = ((Fragment)object).getFragmentManager();
			android.support.v4.app.FragmentTransaction trans = manager.beginTransaction();
			trans.remove((Fragment)object);
			trans.commit();
		}

		@Override
		public void onImageClick() {
			// TODO Auto-generated method stub
			if(!mOnclick){
				titlebarLayout.setY(0);
				if(mCameraImage!=null){
					bottombarLayout.setVisibility(View.VISIBLE);
					bottombarLayout.setY(ScreenUtils.getScreenHeight(ImageZoomActivity.this)-titleHeight-ScreenUtils.getStatusHeight(ImageZoomActivity.this));
				}else{
					bottombarLayout.setVisibility(View.GONE);
				}
			}else{
				titlebarLayout.setY(-titleHeight);
				if(mCameraImage!=null){
					bottombarLayout.setVisibility(View.VISIBLE);
					bottombarLayout.setY(ScreenUtils.getScreenHeight(ImageZoomActivity.this));
				}else{
					bottombarLayout.setVisibility(View.GONE);
				}
			}
			mOnclick = !mOnclick;
		}

	}

	//返回到上一个UI
	public void backLastActivity(){
		this.onBackPressed();
		AppManager.getAppManager().finishActivity(this);
	}
}