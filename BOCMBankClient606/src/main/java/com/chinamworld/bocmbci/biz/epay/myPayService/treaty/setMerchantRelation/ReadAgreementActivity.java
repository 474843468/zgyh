package com.chinamworld.bocmbci.biz.epay.myPayService.treaty.setMerchantRelation;

import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.epay.EPayBaseActivity;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.context.Context;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.constant.ConstantGloble;

public class ReadAgreementActivity extends EPayBaseActivity {

	private View readAgreement;

	private Button bt_agree;
	private Button bt_cancel;
	
	private TextView tv_first_part;
	
//	private String merchantName;
	private Context treatyContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		treatyContext = TransContext.getTreatyTransContext();
		readAgreement = LayoutInflater.from(this).inflate(R.layout.epay_treaty_agreement, null);
		
		super.setType(0);
		super.setShowBackBtn(false);
		super.setTitleName(PubConstants.TITLE_TREATY);
		super.setContentView(readAgreement);
		super.onCreate(savedInstanceState);
		setLeftButtonPopupGone();
		hideFoot();
		initTitleRightButton("关闭", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				treatyContext.setData("readAndAllow", true);
				cancel();
			}
		});
		// // 初始化导航条
		// EpayPubUtil.initStepBar(this, 2, new String[] { "修改限额", "确认信息", "修改成功" });
		getTransData();
	}

	private void getTransData() {
//		merchantName = getIntent().getStringExtra("merchantName");
		// 初始化当前页
		initCurPage();
	}

	private void initCurPage() {
		tv_first_part = (TextView) readAgreement.findViewById(R.id.tv_first_part);
		
		Map<String,Object> data = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA);
		String customerName = (String) data.get(Login.CUSTOMER_NAME);
		tv_first_part.setText(customerName);
		
		bt_agree = (Button) readAgreement.findViewById(R.id.bt_agree);
		bt_agree.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				treatyContext.setData("readAndAllow", false);
				cancel();
			}
		});
		
		bt_cancel = (Button) readAgreement.findViewById(R.id.bt_cancel);
		bt_cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				treatyContext.setData("readAndAllow", true);
				cancel();
			}
		});
	}
	
	private void cancel() {
		finish();
		overridePendingTransition(R.anim.no_animation, R.anim.n_pop_exit_bottom_down);
	}

}
