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

	//ѡ�к�ͼƬ���͵�activity �ȴ��ϴ�
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

		//����no_selected
		helper.setImageResource(R.id.id_item_select,R.drawable.icon_chise_normal);

		if(item.sourcePath.equals("camera")){
			mSelect.setVisibility(View.GONE);
			//����no_pic
			helper.setImageResource(R.id.id_item_image, R.drawable.btn_camera);
			mImageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					backImagePath.startCamera();
				}
			});
		}else{
			mSelect.setVisibility(View.VISIBLE);
			//����no_pic
			helper.setImageResource(R.id.id_item_image, R.drawable.bg_image);
			//����ͼƬ
			helper.setImageByUrl(R.id.id_item_image, item.sourcePath);
			mImageView.setColorFilter(null);
			//����ImageView�ĵ���¼�
			mImageView.setOnClickListener(new OnClickListener()
			{
				//ѡ����ͼƬ�䰵����֮��֮
				@Override
				public void onClick(View v)
				{
					// �Ѿ�ѡ�����ͼƬ
					if (ImageChoiseActivity.mSelectedImage.contains(item))
					{
						mSelect.setImageResource(R.drawable.icon_chise_normal);
						mImageView.setColorFilter(null);
						backImagePath.removeImage(item);
					} else
						// δѡ���ͼƬ
					{
						
						if(ImageChoiseActivity.mSelectedImage.size() >= CustomConstants.MAX_IMAGE_SIZE){
							ToastUtil.showShort(mContext,"�����ֻ��ѡ��"+CustomConstants.MAX_IMAGE_SIZE+"��ͼƬ");
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
		 * �Ѿ�ѡ�����ͼƬ����ʾ��ѡ�����Ч��
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
