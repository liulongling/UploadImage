package com.lll.app.utils;

import android.util.Log;

/**
 * log ͳһ������
 * @author liulongling
 *
 */
public class AppLog {
	
	 public static boolean DEBUG=true;
	    
	  /**
      * �����ɫ����ɫ
      * @param tag
      * @param msg
      */
     public static void debug(String tag,String msg){
     	if(DEBUG){
     		Log.d(tag, msg);
     	}
     }
     
     /**
      * ����Ǻ�ɫ
      * @param tag
      * @param msg
      */
     public static void logv(String tag,String msg){
      	if(DEBUG){
      		Log.v(tag, msg);
      	}
      }
     
     /**
      * ����ǳ�ɫ
      * @param tag
      * @param msg
      */
     public static void logw(String tag,String msg){
      	if(DEBUG){
      		Log.v(tag, msg);
      	}
      }
      
     
     /**
      * ���Ϊ��ɫ
      * @param tag
      * @param msg
      */
     public static void logi(String tag,String msg){
     	if(DEBUG){
     		Log.i(tag, msg);
     	}
     }
     
     /**
      * ���Ϊ��ɫ
      * @param tag
      * @param msg
      */
     public static void loge(String tag,String msg){
     	if(DEBUG){
     		Log.e(tag, msg);
     	}
     }
}
