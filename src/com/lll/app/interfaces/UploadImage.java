package com.lll.app.interfaces;

import com.lll.app.bean.ImageItem;


public interface UploadImage {
	void startCamera();//����
	void addImage(ImageItem path);//���ͼƬ
	void removeImage(ImageItem path);//ɾ��ͼƬ
}
