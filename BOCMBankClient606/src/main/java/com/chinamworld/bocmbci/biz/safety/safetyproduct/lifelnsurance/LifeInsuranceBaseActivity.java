package com.chinamworld.bocmbci.biz.safety.safetyproduct.lifelnsurance;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.safety.SafetyBaseActivity;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public abstract class LifeInsuranceBaseActivity extends SafetyBaseActivity {
	/** 主布局 */
	protected View mMainView;
	/** 初始化控件 */
	protected abstract void findView();
	/** 初始化控件设置 */
	protected abstract void viewSet();

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
		overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
	}
	
	/**
	 * 选填项显示
	 * 
	 * @param str
	 *            要显示的数据，可能为空
	 * @param llId
	 *            TextView的父容器id
	 * @param tvId
	 *            TextView的id
	 */
	protected void optionalShow(String str, int llId, int tvId) {
		if (StringUtil.isNull(str)) {
			mMainView.findViewById(llId).setVisibility(View.GONE);
		} else {
			mMainView.findViewById(llId).setVisibility(View.VISIBLE);
			((TextView) mMainView.findViewById(tvId)).setText(str);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (LinearLayout) mMainView.findViewById(llId));
		}
	}
	
	/**
	 * 为TextView设置文本，直接传TextView的id
	 * 
	 * @param tvId
	 *            TextView的id
	 * @param text
	 *            要设置的文本
	 */
	protected void setTVText(int tvId, Object text) {
		((TextView) mMainView.findViewById(tvId)).setText((String) text);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) mMainView.findViewById(tvId));
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		if (resultCode == SafetyConstant.QUIT_RESULT_CODE) {
//			this.finish();
//		}
	}
}
