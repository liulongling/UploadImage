package com.lll.app.interfaces;

import android.graphics.Bitmap;
import android.widget.ImageView;

public interface ImageCache {
	public void loadBitmaps(ImageView view,String url);
	public void put(String url,Bitmap bitmap);
	public Bitmap get(String url);
}
