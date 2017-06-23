package com.chinamworld.bocmbci.biz.safety.carsafety;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyBaseActivity;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.biz.safety.safetyhold.SafetyHoldProductQueryActivity;
import com.chinamworld.bocmbci.biz.safety.safetyproduct.SafetyProductListActivity;
import com.chinamworld.bocmbci.biz.safety.safetytemp.SafetyTempProductListActivity;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 缴费成功页面
 * 
 * @author Zhi
 */
public class CarSafetyPaySuccess extends SafetyBaseActivity {

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员变量-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** 主显示视图 */
	private View mMainView;
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------成员方法-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.safety_msgfill_title);
		findViewById(R.id.sliding_body).setPadding(0, 0, 0, 0);
		mLeftButton.setVisibility(View.GONE);
		mMainView = LayoutInflater.from(this).inflate(R.layout.safety_carsafety_paysubmit_success, null);
		initView();
		addView(mMainView);
	}
	
	private void initView() {
		setStep3();
		Map<String, Object> map = SafetyDataCenter.getInstance().getMapAutoInsurancePaySubmit();
		((TextView) mMainView.findViewById(R.id.tv_jiaoyi_num)).setText((String) map.get(Safety.TRANSACTIONID));
		// 车牌号动态显示
		if (SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.NEWCARFLAG).equals("0")) {
			((TextView) mMainView.findViewById(R.id.tv_licenseNo)).setText((String) SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.LICENSENO));
		} else {
			((TextView) mMainView.findViewById(R.id.tv_licenseNo)).setText("新车无车牌号");
		}
		// 交强险保单号一定有
		((TextView) mMainView.findViewById(R.id.tv_jqxPolicyNo)).setText((String) map.get(Safety.JQXPOLICYNO));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) mMainView.findViewById(R.id.tv_jqxPolicyNo));
		if (!StringUtil.isNullOrEmpty(map.get(Safety.BIZPOLICYNO))) {
			((TextView) mMainView.findViewById(R.id.tv_bizPolicyNo)).setText((String) map.get(Safety.BIZPOLICYNO));
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this, (TextView) mMainView.findViewById(R.id.tv_bizPolicyNo));
		} else {
			mMainView.findViewById(R.id.ll_bizPolicyNo).setVisibility(View.GONE);
		}
		((TextView) mMainView.findViewById(R.id.tv_email)).setText((String) SafetyDataCenter.getInstance().getMapCarSafetyUserInput().get(Safety.APPL_EMAIL));
		((TextView) mMainView.findViewById(R.id.tv_tips)).setText(((String) map.get(Safety.TIPS)).trim());
		mMainView.findViewById(R.id.btnConfirm).setOnClickListener(confirmClick);
	}
	
	private void setStep3() {
		mMainView.findViewById(R.id.layout_step1).setBackgroundResource(R.drawable.safety_step3);
		mMainView.findViewById(R.id.layout_step2).setBackgroundResource(R.drawable.safety_step2);
		mMainView.findViewById(R.id.layout_step3).setBackgroundResource(R.drawable.safety_step5);
		((TextView) mMainView.findViewById(R.id.index1)).setTextColor(getResources().getColor(R.color.gray));
		((TextView) mMainView.findViewById(R.id.index2)).setTextColor(getResources().getColor(R.color.gray));
		((TextView) mMainView.findViewById(R.id.index3)).setTextColor(getResources().getColor(R.color.red));
		((TextView) mMainView.findViewById(R.id.text1)).setTextColor(getResources().getColor(R.color.gray));
		((TextView) mMainView.findViewById(R.id.text2)).setTextColor(getResources().getColor(R.color.gray));
		((TextView) mMainView.findViewById(R.id.text3)).setTextColor(getResources().getColor(R.color.red));
	}
	
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			intent();
			SafetyDataCenter.getInstance().clearAllData();
			setResult(4);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	};
	
	/** 跳转逻辑，判断投保流程、暂存单继续投保流程、持有保单续保流程成功后跳转页面，在清空数据中心数据之前调用 */
	private void intent() {
		if (SafetyDataCenter.getInstance().isSaveToThere) {
			startActivity(new Intent(CarSafetyPaySuccess.this, SafetyTempProductListActivity.class));
		} else if (SafetyDataCenter.getInstance().isHoldToThere) {
			startActivity(new Intent(CarSafetyPaySuccess.this, SafetyHoldProductQueryActivity.class));
		} else {
			startActivity(new Intent(CarSafetyPaySuccess.this, SafetyProductListActivity.class));
		}
	}
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO --------------------------------------------控件事件-------------------------------------------------//
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	OnClickListener confirmClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			intent();
			SafetyDataCenter.getInstance().clearAllData();
			setResult(4);
			finish();
		}
	};
}
