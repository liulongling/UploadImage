package com.lll.app.bean;

import java.io.Serializable;

/**
 * 
 * @author liulongling
 * 
 */
public class LessonPhoto extends Lesson implements Serializable{
	private static final long serialVersionUID = -7188270558443739436L;
	
	public long photoId;		//自增ID
	public String sourcePath ;	//本地地址
	public String image_url;	//图片网络下载地址
	public String bignoteimg;   //大图
	public String smallnoteimg; //小图
	public int 	upload_status;	//0 上传失败  1上传成功 2正在上传
	public String creation_date;//存储时间
	public long imageSize;	//图片大小
	public int status;
	
}
