package com.chinamworld.bocmbci.biz.remittance.applicationForRemittance;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Inves;
import com.chinamworld.bocmbci.bii.constant.Remittance;
import com.chinamworld.bocmbci.biz.remittance.RemittanceBaseActivity;
import com.chinamworld.bocmbci.biz.remittance.RemittanceDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 汇款成功页
 * 
 * @author Zhi
 */
public class RemittanceSuccessOverActivity extends RemittanceBaseActivity {
	
	/** 登录人证件类型 */
	private String identityType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.remittance_success_over);
		setTitle(this.getString(R.string.remittance_apply_overseas_chian_bank));
		setLeftTopGone();
		initView();
	}

	@SuppressWarnings("unchecked")
	private void initView() {
		identityType = (String) ((Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA)).get(Comm.IDENTITYTYPE);
		Map<String, Object> shiSuan = RemittanceDataCenter.getInstance().getMapPsnTransGetInternationalTransferCommissionCharge();
		Map<String, Object> userInput=RemittanceDataCenter.getInstance().getUserInput();
		Map<String, Object> map = RemittanceDataCenter.getInstance().getMapPsnTransInternationalTransferSubmit();
		Map<String, Object> params = RemittanceDataCenter.getInstance().getSubmitParams();
		((TextView) findViewById(R.id.tv_batSeq)).setText((String) map.get(Remittance.BATSEQ));
		((TextView) findViewById(R.id.tv_transactionId)).setText((String) map.get(Remittance.TRANSACTIONID));
		((TextView) findViewById(R.id.tv_remittanceNumber)).setText((String) map.get(Remittance.HOSTSEQ));// 没有
		((TextView) findViewById(R.id.tv_swiftAccountNumber)).setText(StringUtil.getForSixForString((String) map.get(Remittance.FROMACCOUNTNUMBER)));
		((TextView) findViewById(R.id.tv_remittorName)).setText((String) ((Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA)).get(Inves.CUSTOMERNAME));
		((TextView) findViewById(R.id.tv_remittorENName)).setText((String) userInput.get(Remittance.REMITTORNAME));
		((TextView) findViewById(R.id.tv_remittorAddress)).setText((String) userInput.get(Remittance.REMITTORADDRESS));
		((TextView) findViewById(R.id.tv_remittersZip)).setText((String) params.get(Remittance.REMITTERSZIP));
		((TextView) findViewById(R.id.tv_payerPhone)).setText((String) userInput.get(Remittance.PAYERPHONE));

		//((TextView) findViewById(R.id.tv_gatheringArea)).setText((CharSequence) params.get(Remittance.GATHERINGAREA));
		((TextView) findViewById(R.id.tv_gatheringArea)).setText(RemittanceDataCenter.getInstance().getbankSearsOver());

		List<Map<String, String>> list = RemittanceDataCenter.getInstance().getListPsnQryInternationalTrans4CNYCountry();
		for (Map<String, String> tempMap : list) {
			if (tempMap.get(Remittance.COUNTRYCODE).equals(params.get(Remittance.PAYEEPERMANENTCOUNTRY))) {
				((TextView) findViewById(R.id.tv_payeePermanentCountry)).setText(tempMap.get(Remittance.NAME_CN));
				break;
			}
		}
		if ( params.get(Remittance.GATHERINGAREA).equals("JP") ) {
			findViewById(R.id.ll_rbPhone).setVisibility(View.VISIBLE);
			//findViewById(R.id.ll_payeeBankAdd).setVisibility(View.VISIBLE); // 收款银行地址无
			findViewById(R.id.ll_payeeEnAddress).setVisibility(View.GONE);
			// 日本的时候日本的联系电话是存储在收款人地址字段中的
			((TextView) findViewById(R.id.tv_rbPhone)).setText((String) userInput.get(Remittance.PAYEEENADDRESS));
			//((TextView) findViewById(R.id.tv_payeeBankAdd)).setText((String) userInput.get(Remittance.PAYEEBANKADD)); // 收款银行地址无
		} else {
			findViewById(R.id.ll_rbPhone).setVisibility(View.GONE);
			findViewById(R.id.ll_payeeEnAddress).setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.tv_payeeEnAddress)).setText((String) userInput.get(Remittance.PAYEEENADDRESS));
		}
		((TextView) findViewById(R.id.tv_payeeEnName)).setText((String) userInput.get(Remittance.PAYEEENNAME));
//		((TextView) findViewById(R.id.tv_payeeEnAddress)).setText((String) params.get(Remittance.PAYEEENADDRESS)));
		((TextView) findViewById(R.id.tv_payeeActno)).setText(StringUtil.getForSixForString((String) userInput.get(Remittance.PAYEEACTNO)));
		((TextView) findViewById(R.id.tv_payeeBankSwift)).setText((String) userInput.get(Remittance.PAYEEBANKSWIFT));
		((TextView) findViewById(R.id.tv_payeeBankName)).setText((String) userInput.get(Remittance.PAYEEBANKFULLNAME));

		//收款银行行号 无了
//		if (!StringUtil.isNull((String) userInput.get(Remittance.PAYEEBANKNUM))) {
//			findViewById(R.id.ll_payeeBankNum).setVisibility(View.VISIBLE);;
//			((TextView) findViewById(R.id.tv_payeeBankNum)).setText((String) userInput.get(Remittance.PAYEEBANKNUM));
//		}
		String currency = LocalData.Currency.get(params.get(Remittance.REMITCURRENCYCODE));
		String remit = map.get(Remittance.CASHREMIT).equals("01") ? "现钞" : "现汇";
		((TextView) findViewById(R.id.tv_remitCurrencyCode)).setText(currency+"  "+remit);

//		((TextView) findViewById(R.id.tv_remitCurrencyCode)).setText(LocalData.Currency.get(params.get(Remittance.REMITCURRENCYCODE)));
//		((TextView) findViewById(R.id.tv_cashRemit)).setText(params.get(Remittance.CASHREMIT).equals("01") ? "现钞" : "现汇");
		if (params.get(Remittance.CASHREMIT).equals("02")) {
			findViewById(R.id.ll_cashRemitExchange).setVisibility(View.GONE);
		} else {
			((TextView) findViewById(R.id.tv_cashRemitExchange)).setText(StringUtil.parseStringCodePattern((String) params.get(Remittance.PAYCUR),(String) map.get(Remittance.CASHREMITEXCHANGE),2));
		}
		((TextView) findViewById(R.id.tv_zhuanzhang_num)).setText(StringUtil.parseStringCodePattern((String) params.get(Remittance.REMITCURRENCYCODE), (String) params.get(Remittance.REMITAMOUNT), 2));
		// 优惠后费用，给实收费用的值
		BigDecimal commissionCharge = new BigDecimal((String) map.get(Remittance.COMMISSIONCHARGE));
		BigDecimal postage = new BigDecimal((String) map.get(Remittance.POSTAGE));
		((TextView) findViewById(R.id.tv_favourable)).setText(StringUtil.parseStringCodePattern((String) params.get(Remittance.PAYCUR), commissionCharge.add(postage).toString(), 2));
		// 基准费用，给试算接口的费用
		if(shiSuan.get(Remittance.GETCHARGEFLAG).equals("0")){
			
		} else {
			BigDecimal needCommissionCharge = new BigDecimal((String) shiSuan.get(Remittance.NEEDCOMMISSIONCHARGE));
			BigDecimal needPostage = new BigDecimal((String) shiSuan.get(Remittance.NEEDPOSTAGE));
			BigDecimal benchmarkCost = needCommissionCharge.add(needPostage);
			((TextView) findViewById(R.id.tv_benchmarkCost)).setText(StringUtil.parseStringCodePattern((String) params.get(Remittance.PAYCUR), benchmarkCost.toString(), 2));
		}
		((TextView) findViewById(R.id.tv_payBiZhong)).setText(LocalData.Currency.get((String) params.get(Remittance.PAYCUR)));
		((TextView) findViewById(R.id.tv_chengDanFangShi)).setText("SHA 共同承担");
		((TextView) findViewById(R.id.tv_toPayeeMessage)).setText((String) userInput.get(Remittance.REMITFURINFO2PAYEE));
		if(RemittanceDataCenter.FResident.contains(identityType)
				|| identityType.equals("11")) {
			((LinearLayout) findViewById(R.id.ll_payeeUse)).setVisibility(View.GONE);
		} else {
			((TextView) findViewById(R.id.tv_payeeUse))
			.setText(RemittanceDataCenter.listUseCN
					.get(RemittanceDataCenter.listUseCode.indexOf(params
							.get(Remittance.REMITTANCEINFO))));
		}
		
		if(RemittanceDataCenter.FResident.contains(identityType)
				|| identityType.equals("11")){
			((LinearLayout) findViewById(R.id.ll_payeeUseFull)).setVisibility(View.GONE);
		} else {
			((TextView) findViewById(R.id.tv_payeeUseFull)).setText((String) userInput.get(Remittance.REMITTANCEDESCRIPTION));
		}
		PopupWindowUtils.getInstance().setOnShowAllTextListenerByViewGroup(this, (LinearLayout) findViewById(R.id.ll));

		findViewById(R.id.btnFinish).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RemittanceDataCenter.getInstance().clearAllData();
				setResult(QUIT_CODE);
				finish();
				Intent intent = new Intent(RemittanceSuccessOverActivity.this,
						OverseasChinaBankRemittanceInfoInputActivity.class).putExtra("ISSHOWMUSTKNOW", false);
				startActivity(intent);

			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			RemittanceDataCenter.getInstance().clearAllData();
			setResult(QUIT_CODE);
			finish();
			Intent intent = new Intent(RemittanceSuccessOverActivity.this, OverseasChinaBankRemittanceInfoInputActivity.class).putExtra("ISSHOWMUSTKNOW", false);
			startActivity(intent);
		}
		return super.onKeyDown(keyCode, event);
	}
}
