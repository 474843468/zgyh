package com.chinamworld.bocmbci.biz.remittance.templateManagement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Remittance;
import com.chinamworld.bocmbci.biz.BusinessModelControl;
import com.chinamworld.bocmbci.biz.remittance.RemittanceBaseActivity;
import com.chinamworld.bocmbci.biz.remittance.RemittanceContent;
import com.chinamworld.bocmbci.biz.remittance.RemittanceDataCenter;
import com.chinamworld.bocmbci.biz.remittance.applicationForRemittance.OverseasChinaBankRemittanceInfoInputActivity;
import com.chinamworld.bocmbci.biz.remittance.applicationForRemittance.RemittanceInfoInputActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 模板详情页
 * 
 * @author Zhi
 */
public class RemittanceTemplateDetailActivity extends RemittanceBaseActivity {
	/** 汇款人名称（英文或拼音） */
	private TextView tvRemittorName;
	/** 收款人常驻国家（地区） */
	private TextView tvPayeePermanentCountry;
	/** 收款地区 */
	private String gatheringArea;
	/** 证件类型 */
	private String identityType;
	private LinearLayout tv_remittanceDescription_layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.remittance_template_detail));
		addView(R.layout.remittance_template_detail);
		initView();
	}

	//收款银行swift代码
	private String swift ;
	private void initView() {
		identityType = (String) ((Map<String, Object>) BaseDroidApp
				.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA)).get(Comm.IDENTITYTYPE);
		tv_remittanceDescription_layout=(LinearLayout) findViewById(R.id.tv_remittanceDescription_layout);
		final Map<String, Object> detailMap = RemittanceDataCenter
				.getInstance()
				.getMapPsnInternationalTransferTemplateDetailQuery();

		swift = (String) detailMap.get(Remittance.PAYEEBANKSWIFT);
		swift = swift.substring(0,8);

		if (!(detailMap.get(Remittance.SWIFTACCLINKED)).equals("true")) {
			// findViewById(R.id.btn_accRelevance).setVisibility(View.VISIBLE);
			findViewById(R.id.tv_there).setVisibility(View.VISIBLE);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
					(TextView) findViewById(R.id.tv_there));
			findViewById(R.id.btn_accRelevance).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// startActivityForResult(new
							// Intent(RemittanceTemplateDetailActivity.this,
							// AccInputRelevanceAccountActivity.class).putExtra(Acc.ACC_ACCOUNTNUMBER_RES,
							// (String)
							// detailMap.get(Remittance.SWIFTACCOUNTNUMBER)),
							// 0);
							Map<String, Object> mapData = new HashMap<String, Object>();
							mapData.put(Acc.ACC_ACCOUNTNUMBER_RES, detailMap
									.get(Remittance.SWIFTACCOUNTNUMBER));
							BusinessModelControl.gotoAccRelevanceAccount(
									RemittanceTemplateDetailActivity.this, 0,
									mapData);
						}
					});
		}
		gatheringArea = (String) detailMap.get(Remittance.GATHERINGAREA);
		Log.v("100","--====="+gatheringArea);
		LogGloble.v("100","--====="+gatheringArea);
		((TextView) findViewById(R.id.tv_swiftAccountNumber))
				.setText(StringUtil.getForSixForString((String) detailMap
						.get(Remittance.SWIFTACCOUNTNUMBER)));
		tvRemittorName = (TextView) findViewById(R.id.remittorName);
		tvRemittorName.setEllipsize(TruncateAt.MIDDLE);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tvRemittorName);
		((TextView) findViewById(R.id.tv_remittorName))
				.setText((String) detailMap.get(Remittance.REMITTORNAME));
		((TextView) findViewById(R.id.tv_remittorAddress))
				.setText((String) detailMap.get(Remittance.REMITTORADDRESS));
		((TextView) findViewById(R.id.tv_remittersZip))
				.setText((String) detailMap.get(Remittance.REMITTERSZIP));
		((TextView) findViewById(R.id.tv_payerPhone))
				.setText((String) detailMap.get(Remittance.PAYERPHONE));
		((TextView) findViewById(R.id.tv_gatheringArea))
				.setText(RemittanceDataCenter.payeeArea
						.get(RemittanceDataCenter.payeeAreaCode
								.indexOf(detailMap
										.get(Remittance.GATHERINGAREA))));
		tvPayeePermanentCountry = (TextView) findViewById(R.id.payeePermanentCountry);
		tvPayeePermanentCountry.setEllipsize(TruncateAt.MIDDLE);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tvPayeePermanentCountry);
		List<Map<String, String>> payeeCountryList = RemittanceDataCenter
				.getInstance().getListPsnQryInternationalTrans4CNYCountry();
		String payeeCountry = (String) detailMap
				.get(Remittance.PAYEEPERMANENTCOUNTRY);
		if (StringUtil.isNull(payeeCountry)) {
			((TextView) findViewById(R.id.tv_payeePermanentCountry))
					.setText("-");
		} else {
			for (int i = 0; i < payeeCountryList.size(); i++) {
				Map<String, String> payeeCountryMap = payeeCountryList.get(i);
				if (payeeCountry.equals(payeeCountryMap
						.get(Remittance.COUNTRYCODE))) {
					((TextView) findViewById(R.id.tv_payeePermanentCountry))
							.setText(payeeCountryMap.get(Remittance.NAME_CN));
					break;
				}
			}
		}
		((TextView) findViewById(R.id.tv_payeeEnName))
				.setText((String) detailMap.get(Remittance.PAYEEENNAME));
		// 收款人地址日本取payBankAdd字段，非日本：取payeeEnAddress字段
		if (gatheringArea.equals("JP")) {
			// 收款人地址为日本.代号没有收款人地址
			((LinearLayout) findViewById(R.id.tv_payeeEnAddress_layout))
					.setVisibility(View.GONE);
			// ((TextView)
			// findViewById(R.id.tv_payeeEnAddress)).setText((String)
			// detailMap.get(Remittance.PAYEEBANKADD));
			// 有收款人联系电话
			((LinearLayout) findViewById(R.id.tv_payeePhonenum_layout))
					.setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.tv_payeePhonenum))
					.setText((String) detailMap.get(Remittance.PAYEEENADDRESS));
			// 有收款银行地址
			((LinearLayout) findViewById(R.id.bank_address_layout))
					.setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.bank_address))
					.setText((String) detailMap.get(Remittance.PAYEEBANKADD));
		} else {
			// 收款人地址不为日本
			((LinearLayout) findViewById(R.id.tv_payeeEnAddress_layout))
					.setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.tv_payeeEnAddress))
					.setText((String) detailMap.get(Remittance.PAYEEENADDRESS));
			// 除日本外没有有收款人联系电话
			((LinearLayout) findViewById(R.id.tv_payeePhonenum_layout))
					.setVisibility(View.GONE);
			// 没有有收款银行地址
			((LinearLayout) findViewById(R.id.bank_address_layout))
					.setVisibility(View.GONE);
		}

		((TextView) findViewById(R.id.tv_payeeActno)).setText(StringUtil
				.getForSixForString((String) detailMap
						.get(Remittance.PAYEEACTNO)));
		((TextView) findViewById(R.id.tv_payeeBankSwift))
				.setText((String) detailMap.get(Remittance.PAYEEBANKSWIFT));
		((TextView) findViewById(R.id.tv_payeeBankName))
				.setText((String) detailMap.get(Remittance.PAYEEBANKNAME));
		// 收款行行号：欧盟 英国 日本 其他 这几个地区不显示此字段

		Log.v("100","-----"+gatheringArea);
		LogGloble.v("100","--------"+gatheringArea);
		if (gatheringArea.equals("EU") || gatheringArea.equals("GB")
				|| gatheringArea.equals("JP") || gatheringArea.equals("ZZ")) {
			// 欧盟 英国 日本 其他 这几个地区不显示此字段
			((LinearLayout) findViewById(R.id.ll_payeeBankNum))
					.setVisibility(View.GONE);

		} else {
			((TextView) findViewById(R.id.tv_payeeBankNum))
					.setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.tv_payeeBankNum))
					.setText((String) detailMap.get(Remittance.PAYEEBANKNUM));
		}

		((TextView) findViewById(R.id.tv_remitCurrencyCode))
				.setText((String) detailMap.get(Remittance.REMITCURRENCYCODE));
		// 收款地址为澳洲没有收款银行行号
//		String payeeBankNum = (String) detailMap.get(Remittance.PAYEEBANKNUM);
//		if (StringUtil.isNull(payeeBankNum)) {
//			((LinearLayout) findViewById(R.id.ll_payeeBankNum))
//					.setVisibility(View.GONE);
//		} else {
//			((TextView) findViewById(R.id.tv_payeeBankNum))
//					.setText(payeeBankNum);
//		}
		((TextView) findViewById(R.id.tv_remitCurrencyCode))
				.setText(LocalData.Currency.get(detailMap
						.get(Remittance.REMITCURRENCYCODE)));
		((TextView) findViewById(R.id.tv_cashRemit)).setText(detailMap.get(
				Remittance.CASHREMIT).equals("01") ? "现钞" : "现汇");
		((TextView) findViewById(R.id.tv_remitFurInfo2Payee))
				.setText((String) detailMap.get(Remittance.REMITFURINFO2PAYEE));
		((TextView) findViewById(R.id.tv_feeMode)).setText((String) detailMap
				.get(Remittance.FEEMODE) + " 共同承担");
		if(identityType.equals("11") || RemittanceDataCenter.FResident.contains(identityType)){
			tv_remittanceDescription_layout.setVisibility(View.GONE);
			
		}else {
			tv_remittanceDescription_layout.setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.tv_remittanceDescription))
			.setText((String) detailMap
					.get(Remittance.REMITTANCEDESCRIPTION));
		}
		
		
		// 为所有的textview添加气泡显示
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) findViewById(R.id.tv_remittorName));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) findViewById(R.id.tv_remittorAddress));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) findViewById(R.id.tv_payerPhone));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) findViewById(R.id.tv_payeePermanentCountry));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) findViewById(R.id.tv_payeeEnName));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) findViewById(R.id.tv_payeeActno));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) findViewById(R.id.tv_payeeBankSwift));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) findViewById(R.id.tv_payeeBankName));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) findViewById(R.id.bank_address));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) findViewById(R.id.tv_payeeBankNum));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) findViewById(R.id.tv_remitFurInfo2Payee));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) findViewById(R.id.tv_remittanceDescription));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) findViewById(R.id.tv_remittersZip));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) findViewById(R.id.tv_gatheringArea));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) findViewById(R.id.tv_payeePhonenum));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) findViewById(R.id.tv_payeeEnAddress));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				(TextView) findViewById(R.id.tv_swiftAccountNumber));

		findViewById(R.id.btnNext).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// if (!detailMap.get(Remittance.SWIFTACCLINKED).equals("true"))
				// {
				// BaseDroidApp.getInstanse().showInfoMessageDialog("该账户未关联电子银行");
				// return;
				// }

				requestPsnBOCPayeeBankInfoQuery();

//				BaseHttpEngine.showProgressDialog();
//				getHttpTools().requestHttp(
//						Remittance.PSNQRYINTERNATIONALTRANS4CNYCOUNTRY,
//						"requestPsnQryInternationalTrans4CNYCountryCallBack",
//						null, false);
			}
		});
	}

	/**
	 * 调用查询境外中行收款银行信息接口
	 */
	public void requestPsnBOCPayeeBankInfoQuery() {

		BaseHttpEngine.showProgressDialog();
		Map<String, Object> params = new HashMap<String, Object>();
		getHttpTools().requestHttp("PsnBOCPayeeBankInfoQuery", "requestPsnBOCPayeeBankInfoQueryCallBack", params,false);

	}

	/**
	 * isOverseasChinaBank 通过swift是否是境外中行模板
	 * bocPayeeBankRegionCN 收款行所在国家（地区）
	 */
	private boolean isOverseasChinaBank;
	private String bocPayeeBankRegionCN;


	/**
	 * 境外中行收款行信息查询接口回调
	 * @param resultObj
	 */
	public void requestPsnBOCPayeeBankInfoQueryCallBack(Object resultObj) {

		if (StringUtil.isNullOrEmpty(resultObj)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}

		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = (List<Map<String, Object>>) result
				.get("bocPayeeBankInfoList");

		for (int i = 0; i < list.size() ; i++) {
			String bocPayeeBankSwift = (String) list.get(i).get("bocPayeeBankSwift");
			bocPayeeBankSwift = bocPayeeBankSwift.substring(0,8);
			if(swift.equals(bocPayeeBankSwift)) {
				isOverseasChinaBank = true;
				bocPayeeBankRegionCN = (String) list.get(i).get("bocPayeeBankRegionCN");

				//RemittanceDataCenter.getInstance().setmapPayeeBankOver(list.get(i));
				break;
			} else {
				isOverseasChinaBank = false;
				continue;
			}
		}


		BaseHttpEngine.showProgressDialog();
		getHttpTools().requestHttp(
						Remittance.PSNQRYINTERNATIONALTRANS4CNYCOUNTRY,
						"requestPsnQryInternationalTrans4CNYCountryCallBack",
						null, false);


	}



	/** 查询收款人常驻国家列表回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnQryInternationalTrans4CNYCountryCallBack(
			Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = getHttpTools().getResponseResult(
				resultObj);
		List<Map<String, String>> resultList = (List<Map<String, String>>) resultMap
				.get(Remittance.COUNTRYLIST);
		if (StringUtil.isNullOrEmpty(resultList)) {
			return;
		}
		ActivityTaskManager.getInstance().removeAllActivity();
		RemittanceDataCenter.getInstance()
				.setListPsnQryInternationalTrans4CNYCountry(resultList);

		if (isOverseasChinaBank) {
//			startActivity(new Intent(RemittanceTemplateDetailActivity.this,
//					OverseasChinaBankRemittanceInfoInputActivity.class).putExtra(
//					RemittanceContent.JUMPFLAG, true).putExtra("bocPayeeBankRegionCN",bocPayeeBankRegionCN));

			startActivity(new Intent(RemittanceTemplateDetailActivity.this,
					OverseasChinaBankRemittanceInfoInputActivity.class).putExtra(
					RemittanceContent.JUMPFLAG, true));
		} else {
			startActivity(new Intent(RemittanceTemplateDetailActivity.this,
					RemittanceInfoInputActivity.class).putExtra(
					RemittanceContent.JUMPFLAG, true));
		}
		finish();
	}
}
