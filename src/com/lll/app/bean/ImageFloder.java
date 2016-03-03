package com.lll.app.bean;

import java.util.ArrayList;
import java.util.List;

public class ImageFloder
{
	/**
	 * ͼƬ���ļ���·��
	 */
	private String dir;

	/**
	 * ��һ��ͼƬ��·��
	 */
	private String firstImagePath;

	/**
	 * �ļ��е�����
	 */
	private String name;

	/**
	 * ����ͼƬ
	 */
	private List<ImageItem> imageItems;
	

	public String getDir()
	{
		return dir;
	}

	public void setDir(String dir)
	{
		this.dir = dir;
		int lastIndexOf = this.dir.lastIndexOf("/");
//		this.name = this.dir.substring(lastIndexOf);
	}
	
	public void setName(String name){
		this.name = name;
	}

	public String getFirstImagePath()
	{
		return firstImagePath;
	}

	public void setFirstImagePath(String firstImagePath)
	{
		this.firstImagePath = firstImagePath;
	}

	public String getName()
	{
		return name;
	}
	public int getCount()
	{
		if(imageItems!=null){
			return imageItems.size();
		}
		return 0;
	}

	public void addImageItem(ImageItem item){
		if(imageItems == null){
			imageItems = new ArrayList<ImageItem>();
		}
		imageItems.add(item);
	}

	public List<ImageItem> getImageItems() {
		return imageItems;
	}
}
