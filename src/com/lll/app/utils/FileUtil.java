package com.lll.app.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import android.graphics.Bitmap;
import android.os.Environment;


public class FileUtil {
	static final String ALBUM_PATH = Environment.getExternalStorageDirectory() + "/uux/";
	
	
	/**
	 * 文件是否存在
	 * @param path
	 * @return
	 */
	public static boolean isExist(String path){
		File file = new File(path);
		if(file!=null){
			return true;
		}
		return false;
	}
	/**
	 * 获取指定文件大小
	 * 
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public static long getFileSize(File file){
		long size = 0;
		if (file.exists()) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				if(fis!=null){
					size = fis.available();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		return size;
	}

	/**
	 * 转换文件大小
	 * 
	 * @param fileS
	 * @return
	 */
	public static String FormetFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		String wrongSize = "0B";
		if (fileS == 0) {
			return wrongSize;
		}
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "KB";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "GB";
		}
		return fileSizeString;
	}
	
	/**
	 * 保存拍摄的照片到手机的sd卡  
	 * 图片使用JPEG 能减少图片大小 
	 * @author liulongling
	 * @param bitmap 
	 * @return 图片名称
	 */
	public static String savePicInLocal(Bitmap bitmap) {  
		FileOutputStream fileOutputStream = null;  
		BufferedOutputStream bufferedOutputStream = null;  
		ByteArrayOutputStream byteArrayOutputStream = null; // 字节数组输出流  
		try {  
			byteArrayOutputStream = new ByteArrayOutputStream();  
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);  
			byte[] byteArray = byteArrayOutputStream.toByteArray();// 字节数组输出流转换成字节数组  
			File dir = new File(ALBUM_PATH);  
			if (!dir.exists()) {  
				dir.mkdir(); // 创建文件夹  
			}  
			String fileName = ALBUM_PATH + System.currentTimeMillis()+".jpg";  
			File file = new File(fileName);  
			file.delete();  
			if (!file.exists()) {  
				file.createNewFile();// 创建文件  
			}  
			// 将字节数组写入到刚创建的图片文件中  
			fileOutputStream = new FileOutputStream(file);  
			bufferedOutputStream = new BufferedOutputStream(fileOutputStream);  
			bufferedOutputStream.write(byteArray);  

			return fileName;
		} catch (Exception e) {  
			e.printStackTrace();  
		} finally {  
			CloseUtils.closeQuietly(fileOutputStream);
			CloseUtils.closeQuietly(bufferedOutputStream);
			CloseUtils.closeQuietly(byteArrayOutputStream);
		}  
		return null;
	}  
}
