package com.chinamworld.bocmbci.utils;

import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;

/**
 * 底部按钮的样式设置
 * 
 * @author xby
 */
public class BottomButtonUtils {

	/**
	 * 设置按钮底部的单独一行的样式(红色)
	 * @param bottomBtn 
	 */
	public static void setSingleLineStyleRed(Button bottomBtn) {
		((MarginLayoutParams) bottomBtn.getLayoutParams()).setMargins(
				BaseDroidApp.getContext().getResources()
						.getDimensionPixelSize(R.dimen.dp_three_zero), 0,
						BaseDroidApp.getContext().getResources()
						.getDimensionPixelSize(R.dimen.dp_three_zero), 0);
		bottomBtn.setBackgroundResource(R.drawable.btn_red_big_long);
		bottomBtn.requestLayout();
	}
	
	/**
	 * 设置按钮底部的单独一行的样式(灰色)
	 * @param bottomBtn
	 */
	public static void setSingleLineStyleGray(Button bottomBtn) {
		((MarginLayoutParams) bottomBtn.getLayoutParams()).setMargins(
				BaseDroidApp.getContext().getResources()
						.getDimensionPixelSize(R.dimen.dp_three_zero), 0,
						BaseDroidApp.getContext().getResources()
						.getDimensionPixelSize(R.dimen.dp_three_zero), 0);
		bottomBtn.setBackgroundResource(R.drawable.btn_gray_long);
		bottomBtn.requestLayout();
	}
	
	/**
	 * 设置按钮底部的多个并存一行的样式(红色)
	 * @param bottomBtn
	 */
	public static void setTwoButtonLineStyleRed(Button bottomBtn) {
		((MarginLayoutParams) bottomBtn.getLayoutParams()).setMargins(
				BaseDroidApp.getContext().getResources()
						.getDimensionPixelSize(R.dimen.btncommon_marglr), 0,
						BaseDroidApp.getContext().getResources()
						.getDimensionPixelSize(R.dimen.btncommon_marglr), 0);
		bottomBtn.setBackgroundResource(R.drawable.btn_red_big);
		bottomBtn.requestLayout();
	}
	
	/**
	 * 设置按钮底部的多个并存一行的样式(灰色)
	 * @param bottomBtn
	 */
	public static void setTwoButtonLineStyleGray(Button bottomBtn) {
		((MarginLayoutParams) bottomBtn.getLayoutParams()).setMargins(
				BaseDroidApp.getContext().getResources()
						.getDimensionPixelSize(R.dimen.btncommon_marglr), 0,
						BaseDroidApp.getContext().getResources()
						.getDimensionPixelSize(R.dimen.btncommon_marglr), 0);
		bottomBtn.setBackgroundResource(R.drawable.btn_gray);
		bottomBtn.requestLayout();
	}
	
	

}
