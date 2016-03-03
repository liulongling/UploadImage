package com.lll.app.bean;

import java.io.Serializable;

/**
 * ͼƬ
 *
 */
public class ImageItem extends LessonPhoto implements Serializable
{
	private static final long serialVersionUID = -7188270558443739436L;
	public String imageId;
	public long imageSize;
	public long imageTime;
	public boolean isSelected = false;
	
}
