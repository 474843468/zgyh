package com.chinamworld.bocmbci.biz.bocinvt_p603.productqueryandbuy.activity;


import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.userwidget.LabelTextView;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 周期滚续协议申请，成功页
 * @author Administrator
 *
 */
public class ProductQueryAndBuyPeriodAgreementSuccessActivity extends BocInvtBaseActivity{

	private Map<String, Object> result_map;
	private Map<String, Object> accound_map;
	private Map<String, Object> map_listview_choose;
	private Map<String, Object> acctMap;
	private Map<String, String> agreeInputMap;
	private Map<String, Object> chooseMap;
	/** 产品详情 */
	private Map<String, Object> detailMap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_query_and_buy_period_agreement_success);
		setLeftButtonPopupGone();
		getBackgroundLayout().setTitleText("投资协议申请");
		getBackgroundLayout().setLeftButtonText("");
		getBackgroundLayout().setLeftButtonClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		initDate();
		init();
	}
	@SuppressWarnings("unchecked")
	private void initDate(){
		map_listview_choose=BocInvestControl.map_listview_choose;
		accound_map=BocInvestControl.accound_map;
		result_map=(Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(BocInvestControl.PERIODAGREEMENTRESULTMAP);
		acctMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BOCINVT_BUYINIT_MAP);
		agreeInputMap=BociDataCenter.getInstance().getAgreeInputMap();
		detailMap = BociDataCenter.getInstance().getDetailMap();
		chooseMap = BociDataCenter.getInstance().getChoosemap();
	}
	private void init(){
		TextView tv_tip_top = (TextView) findViewById(R.id.tv_tip_top);
		LabelTextView tv_1 = (LabelTextView) findViewById(R.id.tv_1);
		LabelTextView tv_2 = (LabelTextView) findViewById(R.id.tv_2);
		LabelTextView tv_3 = (LabelTextView) findViewById(R.id.tv_3);
		LabelTextView tv_4 = (LabelTextView) findViewById(R.id.tv_4);
		LabelTextView tv_5 = (LabelTextView) findViewById(R.id.tv_5);
		LabelTextView tv_6 = (LabelTextView) findViewById(R.id.tv_6);
		LabelTextView tv_7 = (LabelTextView) findViewById(R.id.tv_7);
		LabelTextView tv_8 = (LabelTextView) findViewById(R.id.tv_8);
		LabelTextView tv_9 = (LabelTextView) findViewById(R.id.tv_9);
		LabelTextView tv_10 = (LabelTextView) findViewById(R.id.tv_10);
		LabelTextView tv_11 = (LabelTextView) findViewById(R.id.tv_11);
		LabelTextView tv_12 = (LabelTextView) findViewById(R.id.tv_12);
		LabelTextView tv_13 = (LabelTextView) findViewById(R.id.tv_13);
		LabelTextView tv_14 = (LabelTextView) findViewById(R.id.tv_14);
		LabelTextView tv_15 = (LabelTextView) findViewById(R.id.tv_15);
		LabelTextView tv_16 = (LabelTextView) findViewById(R.id.tv_16);
		((Button)findViewById(R.id.btn_next)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
//				startActivity(new Intent(ProductQueryAndBuyPeriodAgreementSuccessActivity.this,QueryProductActivity.class));
			}
		});
		//赋值
		tv_tip_top.setText(map_listview_choose.get("agrName").toString()+getResources().getString(R.string.bocinvt_agree_success_title));
		tv_1.setValueText(result_map.get("tranSeq").toString());
		tv_2.setValueText(result_map.get("contractSeq").toString());
		tv_3.setValueText(result_map.get("operateDate").toString());
		tv_4.setValueText(StringUtil.getForSixForString((String) accound_map.get(BocInvt.ACCOUNTNO)));
		tv_5.setValueText(acctMap.get("productName").toString());
		tv_6.setValueText(acctMap.get("serialName").toString());
		tv_7.setValueText(BocInvestControl.map_productCurCode_toStr.get(agreeInputMap.get(BocInvestControl.CURCODE)));
		tv_8.setValueText(BocInvestControl.map_cashRemit_code_key.get(agreeInputMap.get(BocInvestControl.CASHREMIT)));
		tv_9.setValueText(StringUtil.isNullOrEmpty(chooseMap.get("remainCycleCount"))?"-":chooseMap.get("remainCycleCount").toString());
		tv_10.setValueText(agreeInputMap.get(BocInvestControl.BUYCYCLECOUNT));
		tv_11.setValueText(result_map.get("startPeriod").toString());
		tv_12.setValueText(result_map.get("endPeriod").toString());
		tv_13.setValueText(BocInvestControl.map_invest_money_Type_code_key.get(agreeInputMap.get(BocInvestControl.BASEAMOUNTMODE)));
		if (agreeInputMap.get(BocInvestControl.BASEAMOUNTMODE).equals("1")) {//不定额
			tv_14.setVisibility(View.GONE);
			tv_15.setVisibility(View.VISIBLE);
			tv_16.setVisibility(View.VISIBLE);
			tv_15.setValueText(StringUtil.parseStringCodePattern(detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES).toString()
					,agreeInputMap.get(BocInvestControl.MINAMOUNT), 2));
			tv_16.setValueText(StringUtil.parseStringCodePattern(detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES).toString()
					,agreeInputMap.get(BocInvestControl.MAXAMOUNT), 2));
		}else {//定额
			tv_14.setVisibility(View.VISIBLE);
			tv_15.setVisibility(View.GONE);
			tv_16.setVisibility(View.GONE);
			tv_14.setValueText(StringUtil.parseStringCodePattern(detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES).toString()
					, agreeInputMap.get(BocInvestControl.BASEAMOUNT), 2));
		}
	}
	
	@Override
	public void onBackPressed() {
		return;
	}
	

}
