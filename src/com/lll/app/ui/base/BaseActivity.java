package com.lll.app.ui.base;


import com.lll.app.R;
import com.lll.app.utils.AppManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public abstract class BaseActivity extends FragmentActivity {

	public static final String TAG = BaseActivity.class.getSimpleName();

	protected Handler mHandler = null;
	protected InputMethodManager imm;
	private TelephonyManager tManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppManager.getAppManager().addActivity(this);
		if (!ImageLoader.getInstance().isInited()) {
			
			 DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
			.showImageForEmptyUri(R.drawable.ic_uux) //
			.showImageOnFail(R.drawable.ic_uux) //
			.cacheInMemory(true) //
			.cacheOnDisk(true) //
			.build();//
			 
			ImageLoaderConfiguration config = new ImageLoaderConfiguration//
					.Builder(getApplicationContext())//
			.defaultDisplayImageOptions(defaultOptions)//
			.diskCacheSize(50 * 1024 * 1024)//
			.diskCacheFileCount(100)// ����һ����ͼƬ
			.writeDebugLogs()//
			.build();//
			ImageLoader.getInstance().init(config);
		}
		tManager=(TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		imm=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	/**
	 * ��ʼ���ؼ�
	 */
	protected abstract void initView();

	/**
	 * ͨ����������Activity
	 * 
	 * @param pClass
	 */
	protected void openActivity(Class<?> pClass) {
		openActivity(pClass, null);
	}

	/**
	 * ͨ����������Activity�����Һ���Bundle����
	 * 
	 * @param pClass
	 * @param pBundle
	 */
	protected void openActivity(Class<?> pClass, Bundle pBundle) {
		Intent intent = new Intent(this, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}

	/**
	 * ͨ��Action����Activity
	 * 
	 * @param pAction
	 */
	protected void openActivity(String pAction) {
		openActivity(pAction, null);
	}

	/**
	 * ͨ��Action����Activity�����Һ���Bundle����
	 * 
	 * @param pAction
	 * @param pBundle
	 */
	protected void openActivity(String pAction, Bundle pBundle) {
		Intent intent = new Intent(pAction);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}


	protected void hideOrShowSoftInput(boolean isShowSoft,EditText editText) {
		if (isShowSoft) {
			imm.showSoftInput(editText, 0);
		}else {
			imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
		}
	}
}
