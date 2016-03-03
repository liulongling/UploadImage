package com.lll.app.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.lll.app.R;
import com.lll.app.utils.FileUtil;
import com.lll.app.view.PhotoViewAttacher;
import com.lll.app.view.PhotoViewAttacher.OnPhotoTapListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;


/**
 * 查看大图
 * @author liulongling
 *
 */
public class ImageDetailFragment extends Fragment {
	//图片下载地址 ，本地存储地址
	private String mImageUrl,mLocalUrl;

	@Bind(R.id.iv_image)
	ImageView mImageView;
	@Bind(R.id.pro_loading)
	ProgressBar progressBar;
	//控制图片放大缩小
	private PhotoViewAttacher mAttacher;

	private OnImageClickListener mOnImageClickListener;

	public interface OnImageClickListener{
		void onImageClick();
	}

	private void setOnImageClickListener(OnImageClickListener clickListener){
		this.mOnImageClickListener = clickListener;
	}

	public static ImageDetailFragment newInstance(String localUrl,String imageUrl,OnImageClickListener clickListener) {
		final ImageDetailFragment f = new ImageDetailFragment();
		final Bundle args = new Bundle();
		args.putString("local_url", localUrl);
		args.putString("image_url", imageUrl);
		f.setArguments(args);
		f.setOnImageClickListener(clickListener);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLocalUrl = getArguments() != null ? getArguments().getString("local_url") : null;
		mImageUrl = getArguments() != null ? getArguments().getString("image_url") : null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.fragment_image_detail, container, false);
		ButterKnife.bind(this,v);
		mAttacher = new PhotoViewAttacher(mImageView);
		mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {
			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
				ImageDetailFragment.this.mOnImageClickListener.onImageClick();
			}
		});
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if(mLocalUrl!=null&&FileUtil.isExist(mLocalUrl)){
			ImageLoader.getInstance().displayImage("file:///"+mLocalUrl, mImageView, new SimpleImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					// TODO Auto-generated method stub
					progressBar.setVisibility(View.VISIBLE);
				}
				
				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					// TODO Auto-generated method stub
					progressBar.setVisibility(View.GONE);
					mAttacher.update();
				}
				
				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					// TODO Auto-generated method stub
					
				}
			});
		}else{
			ImageLoader.getInstance().displayImage(mImageUrl, mImageView, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					progressBar.setVisibility(View.VISIBLE);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					String message = null;
					switch (failReason.getType()) {
					case IO_ERROR:
						message = "下载错误";
						break;
					case DECODING_ERROR:
						message = "图片无法显示";
						break;
					case NETWORK_DENIED:
						message = "网络有问题，无法下载";
						break;
					case OUT_OF_MEMORY:
						message = "图片太大无法显示";
						break;
					case UNKNOWN:
						message = "未知的错误";
						break;
					}
					Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
					progressBar.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					progressBar.setVisibility(View.GONE);
					mAttacher.update();
				}
			});
		}
	}
}
