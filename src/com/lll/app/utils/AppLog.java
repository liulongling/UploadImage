package com.lll.app.utils;

import android.util.Log;

/**
 * log 统一管理类
 * @author liulongling
 *
 */
public class AppLog {
	
	 public static boolean DEBUG=true;
	    
	  /**
      * 输出颜色是蓝色
      * @param tag
      * @param msg
      */
     public static void debug(String tag,String msg){
     	if(DEBUG){
     		Log.d(tag, msg);
     	}
     }
     
     /**
      * 输出是黑色
      * @param tag
      * @param msg
      */
     public static void logv(String tag,String msg){
      	if(DEBUG){
      		Log.v(tag, msg);
      	}
      }
     
     /**
      * 输出是橙色
      * @param tag
      * @param msg
      */
     public static void logw(String tag,String msg){
      	if(DEBUG){
      		Log.v(tag, msg);
      	}
      }
      
     
     /**
      * 输出为绿色
      * @param tag
      * @param msg
      */
     public static void logi(String tag,String msg){
     	if(DEBUG){
     		Log.i(tag, msg);
     	}
     }
     
     /**
      * 输出为红色
      * @param tag
      * @param msg
      */
     public static void loge(String tag,String msg){
     	if(DEBUG){
     		Log.e(tag, msg);
     	}
     }
}
