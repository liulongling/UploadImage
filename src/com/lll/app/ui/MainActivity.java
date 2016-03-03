package com.lll.app.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.lll.app.R;
import com.lll.app.adapter.ImagePublishAdapter;
import com.lll.app.bean.ImageItem;
import com.lll.app.bean.LessonPhoto;
import com.lll.app.finals.ClientCode;
import com.lll.app.finals.CustomConstants;
import com.lll.app.utils.UploadImageUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity{
	private static final String TAG = "MainActivity";

	/*****���******/
	@Bind(R.id.gv_photo)
	GridView mGridView;

	/****�γ�ͼƬ*****/
	private ImagePublishAdapter mAdapter;

	//���
	private List<LessonPhoto> mDataList = new ArrayList<LessonPhoto>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		initData();
		initView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	public void initView(){
		mAdapter = new ImagePublishAdapter(this, mDataList);
		mGridView.setAdapter(mAdapter);
		mGridView.setSelector(new ColorDrawable(Color.WHITE)); // ����ȡ��gridview�ı���ѡ��
		mGridView.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				if (position == 0)
				{
					startImageChoiseActivity();
				}
				else
				{
					Intent intent = new Intent(MainActivity.this,ImageZoomActivity.class);
					intent.putExtra(CustomConstants.EXTRA_IMAGE_LIST,(Serializable) mDataList);
					intent.putExtra(CustomConstants.EXTRA_CURRENT_IMG_POSITION, position-1);
					startActivity(intent);
				}
			}
		});
	}
	
	//ѡ��ͼƬ
	public void startImageChoiseActivity() {
		Intent intent = new Intent(this, ImageChoiseActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.startActivity(intent);
	}


	private void initData(){
		//���
		showPhotos();
	}


	/**
	 * �������
	 * @param photo
	 */
	public void update(LessonPhoto photo)
	{
		showPhotos();
	}

	/**
	 * ��ʾ���
	 */
	public void showPhotos(){
		mDataList.clear();
		mDataList.addAll(mDataList);
		if(mAdapter!=null){
			mAdapter.updateDataList(mDataList);
		}
	}

	/**
	 * �Ƿ��������ϴ���ͼƬ
	 * @return
	 */
	public boolean hasUploadingPhoto(){
		for(LessonPhoto photo:mDataList){
			if(photo.status == ClientCode.LessonPhoto.UPLOADING){
				return true;
			}
		}
		return false;
	}



	/**
	 * ��ʾҪ�ϴ���ͼƬ���ϴ�����̨
	 * @param list
	 * @param mBaseImage true == ԭͼ 
	 */
	public void updateImageData(List<ImageItem> list,boolean mBaseImage){
		if(list == null){
			return;
		}

		//�ϴ�ǰ��ͼƬ�洢���������ݿ�
		List<LessonPhoto> photos = new ArrayList<LessonPhoto>();

		//���������ϴ��е�ͼƬ �����ϴ�ͼƬ ȫ������Ϊ�ȴ��ϴ� �� ��һ��ͼƬ����Ϊ�ȴ��ϴ�
		boolean hasUploadingPhoto = hasUploadingPhoto();

		int i = 0;
		for(ImageItem item:list){
			LessonPhoto photo = new LessonPhoto();
//			photo.setId(lessonId);
			photo.sourcePath = item.sourcePath;
			photo.upload_status = (!hasUploadingPhoto&&i == 0)? ClientCode.LessonPhoto.WAIT_UPLOAD :ClientCode.LessonPhoto.UPLOADING;
			photos.add(photo);
			i++;
		}

		//�洢ͼƬ
//		photos = LessonPhotoDAO.addLessonPhoto(photos);
		if(photos!=null){
			mDataList.addAll(photos);
			mAdapter.updateDataList(mDataList);

			//�ͷ��ڴ�
			list = null;
			photos = null;

			//��ʼ�ϴ�
			uploadImage(mBaseImage);
		}
	}


	/**
	 * �ϴ�ͼƬ
	 * @param mBaseImage true ==  ԭͼ
	 */
	public void uploadImage(boolean mBaseImage){
		int size = mDataList.size();
		for(int i=0;i<size;i++){
			LessonPhoto photo = mDataList.get(i);
			if(photo.upload_status == ClientCode.LessonPhoto.UPLOADING || photo.upload_status ==  ClientCode.LessonPhoto.WAIT_UPLOAD){
				//ͼƬ��ӵ��߳� �ȴ��ϴ�
				UploadImageUtil.getInstanse(this.getApplicationContext()).putImage(photo.sourcePath, mBaseImage, photo.photoId,ClientCode.LessonPhoto.UPLOADING);
			}
		}
	}
}
