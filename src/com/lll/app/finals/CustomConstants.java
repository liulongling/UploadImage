package com.lll.app.finals;

public class CustomConstants
{

	//测试地址
	public static String httpUrl = "http://112.25.14.124/portal";
	public static String downloadPicUrl = "http://112.25.14.124/image";


	//单次�?多发送图片数
	public static final int MAX_IMAGE_SIZE = 9;
	//首�?�项:临时图片
	public static final String PREF_TEMP_IMAGES = "pref_temp_images";
	//相册中图片对象集�?
	public static final String EXTRA_IMAGE_LIST = "image_list";
	//相册名称
	public static final String EXTRA_BUCKET_NAME = "buck_name";
	//可添加的图片数量
	public static final String EXTRA_CAN_ADD_IMAGE_SIZE = "can_add_image_size";
	//当前选择的照片位�?
	public static final String EXTRA_CURRENT_IMG_POSITION = "current_img_position";
	//拍照
	public static final String EXTRA_CAMERA_IMG 	= "camera_img";
	//预览
	public static final String EXTRA_PREVIEW 		= "preview";


	/**
	 * �?服务器上传图片最大大�?
	 */
	public static  final  int   UPLOAD_IMAGE_SIZE = 1024*5;


	public static final int IMAGE_REQUEST_CODE = 0;  
	public static final int CAMERA_REQUEST_CODE = 1;  
	public static final int RESIZE_REQUEST_CODE = 2;
}
