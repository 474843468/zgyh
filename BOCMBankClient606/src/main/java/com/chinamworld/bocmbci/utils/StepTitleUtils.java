package com.chinamworld.bocmbci.utils;

import android.app.Activity;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;

/**
 * 分步骤的进度框
 * 
 * @author xiabaoying
 * 
 *         2013-4-26
 * 
 */
public class StepTitleUtils {
	private static StepTitleUtils stu;

	private StepTitleUtils() {
	};

	public static StepTitleUtils getInstance() {
		if (stu == null)
			stu = new StepTitleUtils();
		return stu;
	}

	/**
	 * 三个步骤选项
	 */
	private Button btn1, btn2, btn3;
	/**
	 * 整体背景
	 */
	LinearLayout titlell;

	/**
	 * 初始化步骤标题
	 * 
	 * @param context
	 *            当前上下文
	 * @param titles
	 *            步骤文本
	 */
	public void initTitldStep(Activity context, String[] titles) {
		if (titles.length == 3) {
			titlell = (LinearLayout) context.findViewById(R.id.stepbar);
			if ((LinearLayout) context.findViewById(R.id.sliding_body) != null)
				((LinearLayout) context.findViewById(R.id.sliding_body))
						.setPadding(0, 0, 0,  context.getResources().getDimensionPixelSize(R.dimen.common_bottom_padding_new));
//			
//			btn1 = ((Button) context.findViewById(R.id.btnstep1));
//			btn1.setText(titles[0]);
//			btn2 = ((Button) context.findViewById(R.id.btnstep2));
//			btn2.setText(titles[1]);
//			btn3 = ((Button) context.findViewById(R.id.btnstep3));
//			btn3.setText(titles[2]);
//			btn3.setPressed(false);
//
		}
	}

	/**
	 * 改变步骤选中项
	 * 
	 * @param step
	 *            第几部(1,2,3)
	 */
	public void setTitleStep(int step) {
//		switch (step) {
//		case 1:
//			titlell.setBackgroundResource(R.drawable.step_bg1);
//			break;
//		case 2:
//			titlell.setBackgroundResource(R.drawable.step_bg2);
//			break;
//		case 3:
//			titlell.setBackgroundResource(R.drawable.step_bg3);
//			break;
//
//		}

	}

}
