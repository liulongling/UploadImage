package com.lll.app.bean;

import java.io.Serializable;

/**
 * 
 * @author liulongling
 * 
 */
public class LessonPhoto extends Lesson implements Serializable{
	private static final long serialVersionUID = -7188270558443739436L;
	
	public long photoId;		//����ID
	public String sourcePath ;	//���ص�ַ
	public String image_url;	//ͼƬ�������ص�ַ
	public String bignoteimg;   //��ͼ
	public String smallnoteimg; //Сͼ
	public int 	upload_status;	//0 �ϴ�ʧ��  1�ϴ��ɹ� 2�����ϴ�
	public String creation_date;//�洢ʱ��
	public long imageSize;	//ͼƬ��С
	public int status;
	
}
