package com.lll.app.dialog;

import com.lll.app.R;
import com.lll.app.utils.ScreenUtils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MyDialog {
	public static Dialog showChoiseDialog(Context mContext,String num,String str1,String str2,final DialogItemClickListener dialogClickListener){ 
		final Dialog dialog = new Dialog(mContext, R.style.MyDialog);
		View view = View.inflate(mContext, R.layout.dialog_validate,null);
		dialog.setContentView(view);

		Window mWindow=dialog.getWindow();
		WindowManager.LayoutParams lp = mWindow.getAttributes();
		lp.width= ScreenUtils.getScreenWidth(mContext);
		mWindow.setGravity(Gravity.CENTER);
		mWindow.setAttributes(lp);

		final LinearLayout sure = (LinearLayout)view.findViewById(R.id.ll_sure);
		final LinearLayout cancel = (LinearLayout)view.findViewById(R.id.ll_cancel);
		final TextView tvsure = (TextView)view.findViewById(R.id.tv_sure);
		final TextView tvcancel = (TextView)view.findViewById(R.id.tv_cancel);
		final TextView phone = (TextView) view.findViewById(R.id.tv_phone);
		tvsure.setText(str2);
		tvcancel.setText(str1);
		phone.setText(num);
		sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				dialogClickListener.confirm(String.valueOf(1));
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
		return dialog;
	}

	public interface DialogItemClickListener{
		public abstract void confirm(String result);
	}
}
