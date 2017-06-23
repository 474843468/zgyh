package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.bocinvt.myproduct.MyProductListActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BOCProductForHoldingInfo;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.userwidget.LabelTextView;
import com.chinamworld.bocmbci.userwidget.LabelTextView.TextColor;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 份额转换完成页面
 * 
 * @author HVZHUNG
 *
 */
public class QuantityTransitionSuccessActivity extends BocInvtBaseActivity {

	private BOCProductForHoldingInfo info;
	Map<String, Object> detailMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.bocinvt_quantity_transition_success_activity_p603);
		setTitle(R.string.bocinvt_holding_detail_lot_transform);
//		getBackgroundLayout().setRightButtonText(null);
		getBackgroundLayout().setLeftButtonText(null);

		info = ((BocInvestControl) BocInvestControl.getInstance()).curBOCProductForHoldingInfo;

		detailMap = (Map<String, Object>) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.BOCINVT_PRODUCT_DETAIL_MAP);
		
		((LabelTextView) findViewById(R.id.ltv_transaction_code)).setValueText((String)info.psnXpadShareTransitionCommitResponseMap.get("transactionId"));
		((LabelTextView) findViewById(R.id.ltv_product_code)).setValueText(info.prodCode);
		((LabelTextView) findViewById(R.id.ltv_product_name)).setValueText(info.prodName);
		String curcode = (String)detailMap.get("curCode");
		/** 产品币种 */
		((LabelTextView) findViewById(R.id.ltv_currency)).setValueText(LocalData.Currency.get(curcode));	
		((LabelTextView) findViewById(R.id.ltv_cash_remittance)).setValueText(LocalData.cashRemitMapValue.get(String.valueOf(info.cashRemit)));
		((LabelTextView) findViewById(R.id.ltv_product_deadline)).setValueText("最低持有"+detailMap.get("prodTimeLimit")+"天");
//		if("04".equals(info.termType)&&"-1".equals(info.productTerm)){
//			((LabelTextView) findViewById(R.id.ltv_product_deadline)).setValueText("无固定期限");
//		}else{
//			((LabelTextView) findViewById(R.id.ltv_product_deadline)).setValueText("最低持有"+info.productTerm+"天");
//		}
		((LabelTextView) findViewById(R.id.ltv_transition_enable_redeem_date)).setValueText((String)info.psnXpadShareTransitionCommitResponseMap.get("eDate"));
		
		LabelTextView tranUnit = (LabelTextView) findViewById(R.id.ltv_transform);
//		tranUnit.setValueText(StringUtil.parseStringCodePattern(curcode,  info.transferUnit, 2) );
		tranUnit.setValueText(StringUtil.parseStringPattern(info.transferUnit, 2));
		tranUnit.setValueTextColor(TextColor.Red);
		
		((LabelTextView) findViewById(R.id.ltv_estimate_apy)).setValueText(info.yearlyRR + "%");
		
		
		
		
		

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_finish:
			ActivityTaskManager.getInstance().removeAllSecondActivity();
			Intent intent = new Intent(this, MyProductListActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
