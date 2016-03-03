package com.lll.app.adapter;

import java.util.ArrayList;
import java.util.List;

import com.lll.app.R;
import com.lll.app.bean.LessonPhoto;
import com.lll.app.cache.DoubleCache;
import com.lll.app.finals.ClientCode;
import com.lll.app.finals.CustomConstants;
import com.lll.app.utils.ImageDisplayer;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImagePublishAdapter extends BaseAdapter
{
	public List<LessonPhoto> mDataList = new ArrayList<LessonPhoto>();
	private Context mContext;
	private int flag = 0;

	public ImagePublishAdapter(Context context, List<LessonPhoto> dataList)
	{
		this.mContext = context;
		this.mDataList = dataList;
	}

	public void updateDataList(List<LessonPhoto> list){ 
		this.mDataList=list;
		this.notifyDataSetChanged();
	}

	public int getCount()
	{
		// 多返回一个用于展示添加图标
		if (mDataList == null)
		{
			return 1;
		}
		else if (mDataList.size() == CustomConstants.MAX_IMAGE_SIZE)
		{
			return CustomConstants.MAX_IMAGE_SIZE;
		}
		else
		{
			return mDataList.size() + 1;
		}
	}

	public Object getItem(int position)
	{
		if (mDataList != null
				&& mDataList.size() == CustomConstants.MAX_IMAGE_SIZE)
		{
			return mDataList.get(position);
		}

		else if (mDataList == null || position - 1 < 0
				|| position > mDataList.size())
		{
			return null;
		}
		else
		{
			return mDataList.get(position - 1);
		}
	}

	public long getItemId(int position)
	{
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		//所有Item展示不满一页，就不进行ViewHolder重用了，避免了一个拍照以后添加图片按钮被覆盖的奇怪问题
		convertView = View.inflate(mContext, R.layout.item_publish, null);
		ImageView imageIv = (ImageView) convertView.findViewById(R.id.item_grid_image);
		TextView mUpload = (TextView) convertView.findViewById(R.id.tv_upload);
		mUpload.setVisibility(View.GONE);
		if (position == 0)
		{
			imageIv.setImageResource(R.drawable.btn_camera_add_image);
			imageIv.setBackgroundResource(R.color.gray);
		}
		else
		{
			final LessonPhoto lesson = mDataList.get(position-1);
			switch (lesson.upload_status) {
			case ClientCode.LessonPhoto.UPLOADING:
			case ClientCode.LessonPhoto.WAIT_UPLOAD:
				//上传中
				mUpload.setText(lesson.upload_status == ClientCode.LessonPhoto.UPLOADING?"上传中...":"等待上传");
				imageIv.setColorFilter(Color.parseColor("#77000000"));
				mUpload.setVisibility(View.VISIBLE);
				if(lesson.sourcePath != null){
					ImageDisplayer.getInstance(mContext).displayBmp(imageIv, null, lesson.sourcePath);
				}
				break;
			case ClientCode.LessonPhoto.SUCCESS:
				//已上传成功 先从本地路径读取
				if(lesson.sourcePath != null){
					ImageDisplayer.getInstance(mContext).displayBmp(imageIv, null, lesson.sourcePath);
				}else if(lesson.image_url != null){
					DoubleCache.getInstanse(mContext).loadBitmaps(imageIv, lesson.image_url+lesson.smallnoteimg);
				}
				break;

			default:
				break;
			}
		}

		return convertView;
	}

}
