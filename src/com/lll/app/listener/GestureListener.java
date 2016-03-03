package com.lll.app.listener;

import android.content.Context;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * ʵ�ּ������һ������¼����ĸ�view��Ҫ��ʱ��ֱ��setOnTouchListener�Ϳ�������
 *
 */
public class GestureListener extends SimpleOnGestureListener{
	private static final String TAG = "GestureListener";
	private final static int VERTICALMINISTANCE = 200 ; //��ʾ���󻬶�����С����
	private final static int MINVELOCITY = 10 ;  //��ʾ���󻬶�����С�ļ��ٶ�
	private GestureDetector gestureDetector;

	public GestureListener(Context context) {
		super();
		gestureDetector = new GestureDetector(context, this);
	}
	
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		/*// ����velocityX��x�����ϵļ��ٶȣ���velocityY��y�����ϵļ��ٶȣ�
		// ���������ٶȵĴ�С�ȽϿ����жϳ����������Ҳ໬�������»���
		if (Math.abs(velocityX) > Math.abs(velocityY) ) {
			//��ʾ
		}*/

		//�����趨����С�������벢����ˮƽ/��ֱ�����ٶȾ���ֵ�����趨����С�ٶȣ���ִ����Ӧ��������ʾ���󻬶���
		if(e1.getX()-e2.getX() > VERTICALMINISTANCE && Math.abs(velocityX) > MINVELOCITY){
			//��һϵ�еĲ���  
			left();
		}

		//��ʾ�һ�
		if (e2.getX()-e1.getX() > VERTICALMINISTANCE && Math.abs(velocityX) > MINVELOCITY) {
			//�һ�һϵ�еĲ���    
			right();
		}
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * ���󻬵�ʱ����õķ���������Ӧ����д
	 * @return
	 */
	public boolean left() {
		return false;
	}

	/**
	 * ���һ���ʱ����õķ���������Ӧ����д
	 * @return
	 */
	public boolean right() {
		return false;
	}

	public GestureDetector getGestureDetector() {
		return gestureDetector;
	}

	public void setGestureDetector(GestureDetector gestureDetector) {
		this.gestureDetector = gestureDetector;
	}
	
}