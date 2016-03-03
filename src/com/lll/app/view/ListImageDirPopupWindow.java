package com.lll.app.view;

import java.util.List;

import com.lll.app.R;
import com.lll.app.adapter.BasePopupWindowForListView;
import com.lll.app.adapter.CommonAdapter;
import com.lll.app.adapter.ViewHolder;
import com.lll.app.bean.ImageFloder;


import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ListImageDirPopupWindow extends BasePopupWindowForListView<ImageFloder>
{
	private ListView mListDir;
	private OnImageDirSelected mImageDirSelected;
	
	public ListImageDirPopupWindow(int width, int height,List<ImageFloder> datas, View convertView)
	{
		super(convertView, width, height, true, datas);
	}

	@Override
	public void initViews()
	{
		mListDir = (ListView) findViewById(R.id.id_list_dir);
		mListDir.setAdapter(new CommonAdapter<ImageFloder>(context, mDatas,R.layout.list_dir_item)
		{
			@Override
			public void convert(ViewHolder helper, ImageFloder item)
			{
				helper.setText(R.id.id_dir_item_name, item.getName());
				helper.setImageByUrl(R.id.id_dir_item_image,item.getFirstImagePath());
				if(item.getCount() > 0){
					helper.setText(R.id.id_dir_item_count, item.getCount() + "уе");
				}else{
					helper.setText(R.id.id_dir_item_count, null);
				}
			}
		});
	}

	public interface OnImageDirSelected
	{
		void selected(ImageFloder floder);
	}


	public void setOnImageDirSelected(OnImageDirSelected mImageDirSelected)
	{
		this.mImageDirSelected = mImageDirSelected;
	}

	@Override
	public void initEvents()
	{
		mListDir.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{

				if (mImageDirSelected != null)
				{
					mImageDirSelected.selected(mDatas.get(position));
				}
			}
		});
	}

	@Override
	public void init()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void beforeInitWeNeedSomeParams(Object... params)
	{
		// TODO Auto-generated method stub
	}

}
