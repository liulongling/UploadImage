package com.lll.app.adapter;

import java.util.List;

import com.lll.app.R;
import com.lll.app.bean.ImageItem;
import com.lll.app.finals.CustomConstants;
import com.lll.app.interfaces.UploadImage;
import com.lll.app.ui.ImageChoiseActivity;
import com.lll.app.utils.ToastUtil;


import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;


public class ImageChoiseAdapter extends CommonAdapter<ImageItem>
{

	private Context mContext;

	//选中后将图片发送到activity 等待上传
	private UploadImage backImagePath;


	public void setBackImagePath(UploadImage backImagePath) {
		this.backImagePath = backImagePath;
	}

	public ImageChoiseAdapter(Context context, List<ImageItem> mDatas, int itemLayoutId)
	{
		super(context, mDatas, itemLayoutId);
		this.mContext = context;
	}

	@Override
	public void convert(final ViewHolder helper, final ImageItem item)
	{
		final ImageView mImageView = helper.getView(R.id.id_item_image);
		final ImageView mSelect = helper.getView(R.id.id_item_select);

		//设置no_selected
		helper.setImageResource(R.id.id_item_select,R.drawable.icon_chise_normal);

		if(item.sourcePath.equals("camera")){
			mSelect.setVisibility(View.GONE);
			//设置no_pic
			helper.setImageResource(R.id.id_item_image, R.drawable.btn_camera);
			mImageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					backImagePath.startCamera();
				}
			});
		}else{
			mSelect.setVisibility(View.VISIBLE);
			//设置no_pic
			helper.setImageResource(R.id.id_item_image, R.drawable.bg_image);
			//设置图片
			helper.setImageByUrl(R.id.id_item_image, item.sourcePath);
			mImageView.setColorFilter(null);
			//设置ImageView的点击事件
			mImageView.setOnClickListener(new OnClickListener()
			{
				//选择，则将图片变暗，反之则反之
				@Override
				public void onClick(View v)
				{
					// 已经选择过该图片
					if (ImageChoiseActivity.mSelectedImage.contains(item))
					{
						mSelect.setImageResource(R.drawable.icon_chise_normal);
						mImageView.setColorFilter(null);
						backImagePath.removeImage(item);
					} else
						// 未选择该图片
					{
						
						if(ImageChoiseActivity.mSelectedImage.size() >= CustomConstants.MAX_IMAGE_SIZE){
							ToastUtil.showShort(mContext,"你最多只能选择"+CustomConstants.MAX_IMAGE_SIZE+"张图片");
							return;
						}

						mSelect.setImageResource(R.drawable.icon_chise_hight);
						mImageView.setColorFilter(Color.parseColor("#77000000"));
						backImagePath.addImage(item);
					}
				}
			});
		}

		/**
		 * 已经选择过的图片，显示出选择过的效果
		 */
		if (ImageChoiseActivity.mSelectedImage.contains(item))
		{
			mSelect.setImageResource(R.drawable.icon_chise_hight);
			mImageView.setColorFilter(Color.parseColor("#77000000"));
		}else{
			mSelect.setImageResource(R.drawable.icon_chise_normal);
			mImageView.setColorFilter(null);
		}
	}
}
