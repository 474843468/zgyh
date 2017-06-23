package com.chinamworld.bocmbci.biz.safety.safetyhold;

import java.util.Map;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.safety.SafetyBaseActivity;
import com.chinamworld.bocmbci.biz.safety.SafetyDataCenter;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 退保成功
 * @author fsm
 *
 */
public class SafetyInsuranceReturnActivity extends SafetyBaseActivity{

	/** 控件 收款账户，退保金额， 生效日期*/
	private TextView receive_acc, return_amount, effective_date;
	/** 完成按钮*/
	private Button ok;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.safety_hold_pro_detail_btn_quit));// 为界面标题赋值
		addView(R.layout.safety_insurance_return_succ);// 添加布局
		setLeftTopGone();
		initViews();
		initParamsInfo();
	}
	
	private void initViews(){
		receive_acc = (TextView)findViewById(R.id.receive_acc);
		return_amount = (TextView)findViewById(R.id.return_amount);
		effective_date = (TextView)findViewById(R.id.effective_date);
		ok = (Button)findViewById(R.id.sbremit_sremit_success_ok);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, receive_acc);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, return_amount);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, effective_date);
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
			}
		});
	}
	
	/**
	 * 接收调用页的信息，信息完整则初始本页面
	 */
	private void initParamsInfo(){
		Map<String, Object> map = SafetyDataCenter.getInstance().getHoldProDetail();
		if(map != null){
			try {
				receive_acc.setText(StringUtil.getForSixForString((String)map.get(Safety.ACCINFO)));
				return_amount.setText(StringUtil.parseStringCodePattern(
						(String)map.get(Safety.SAFETY_HOLD_CURRENCY), (String)map.get(Safety.BACK_PREM), 2));
				effective_date.setText(StringUtil.valueOf1((String)map.get(Safety.EFFECTIVE_DATA)));
			} catch (Exception e) {
				LogGloble.exceptionPrint(e);
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
