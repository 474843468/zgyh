package com.chinamworld.bocmbci.biz.bocinvt_p603.productqueryandbuy.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.userwidget.LabelTextView;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 
 * 中银理财-组合购买页
 * @author Administrator
 *
 */
public class ProductQueryAndBuyComboBuyActivity extends BocInvtBaseActivity{

	/**用户选择的账户 产品信息*/
//	private Map<String, Object> chooseMap; 
	/** 产品详情列表 */
	private Map<String, Object> detailMap;
	/** 用户选择的账户信息 */
	private Map<String, Object> accound_map;
	private LabelTextView tv_1;
	private LabelTextView tv_2;
	private LabelTextView tv_3;
	private LabelTextView tv_4;
	private TextView tv_5;
//	private Spinner sp_1;
	private LabelTextView tv_6;
	private LabelTextView tv_7;
	private LabelTextView tv_8;
	private LabelTextView tv_9;
	private LabelTextView tv_10;
	private EditText et_1;
//	private Button btn_cancel;
	private Button btn_next;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initBaseLayout();
		setContentView(R.layout.product_query_and_buy_combo_buy);
		initDate();
		initUI();
	}
	/**
	 * 组件初始化及赋值
	 */
	private void initUI(){
		//初始化组件
		tv_1 = (LabelTextView) findViewById(R.id.tv_1);
		tv_2 = (LabelTextView) findViewById(R.id.tv_2);
		tv_3 = (LabelTextView) findViewById(R.id.tv_3);
		tv_4 = (LabelTextView) findViewById(R.id.tv_4);
		tv_5 = (TextView) findViewById(R.id.tv_5);
//		sp_1 = (Spinner) findViewById(R.id.sp_1);
		rg_ch = (RadioGroup) findViewById(R.id.rg_ch);
		rb_c = (RadioButton) findViewById(R.id.rb_c);
		tv_6 = (LabelTextView) findViewById(R.id.tv_6);
		tv_7 = (LabelTextView) findViewById(R.id.tv_7);
		tv_8 = (LabelTextView) findViewById(R.id.tv_8);
		tv_9 = (LabelTextView) findViewById(R.id.tv_9);
		tv_10 = (LabelTextView) findViewById(R.id.tv_10);
		et_1 = (EditText) findViewById(R.id.et_1);
//		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_next = (Button) findViewById(R.id.btn_next);
		//赋值
//		btn_cancel.setOnClickListener(backClickListener);
		btn_next.setOnClickListener(nextClickListener);
//		setAdapterForSpinner(sp_1);
		tv_1.setValueText(StringUtil.getForSixForString(accound_map.get(BocInvt.ACCOUNTNO).toString()));
		tv_2.setValueText(detailMap.get(BocInvt.BOCI_DETAILPRODCODE_RES).toString());
		tv_3.setValueText(detailMap.get(BocInvt.BOCI_DETAILPRODNAME_RES).toString());
		tv_4.setValueText(BocInvestControl.map_productCurCode_toStr.get(detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES).toString()));
		if (isRMB()) {
			tv_5.setVisibility(View.VISIBLE);
//			sp_1.setVisibility(View.GONE);
			rg_ch.setVisibility(View.GONE);
			tv_5.setText(getResources().getString(R.string.bocinvt_tv_41));
		}else {
			tv_5.setVisibility(View.GONE);
//			sp_1.setVisibility(View.VISIBLE);
			rg_ch.setVisibility(View.VISIBLE);
		}
		tv_6.setValueText(StringUtil.parseStringCodePattern((String)detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES), detailMap.get(BocInvt.SUBAMOUNT).toString(), 2));
		tv_7.setValueText(StringUtil.parseStringCodePattern((String)detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES), detailMap.get(BocInvt.ADDAMOUNT).toString(), 2));
		tv_8.setValueText(detailMap.get(BocInvt.BOCINVT_BUYRES_PRODBEGIN_RES).toString());
		if (detailMap.get(BocInvt.BOCINCT_XPADTRAD_PRODUCTTYPE_RES).toString().equals("2")) {//净值产品
			tv_10.setVisibility(View.GONE);
			if (detailMap.get("productTermType").toString().equals("3")) {//&&产品期限特性,3：无限开放式
				tv_9.setValueText("长期");
			}else {
				tv_9.setValueText(detailMap.get(BocInvt.BOCINVT_BUYRES_PRODEND_RES).toString());
			}
		}else {//非净值产品
			tv_10.setVisibility(View.VISIBLE);
			tv_10.setValueText(StringUtil.parseStringCodePattern((String)detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES), detailMap.get(BocInvt.BOCINVT_BUYPRE_BUYPRICE_REQ).toString(), 2));
			tv_9.setValueText(detailMap.get(BocInvt.BOCINVT_BUYRES_PRODEND_RES).toString());
		}
		EditTextUtils.relateNumInputToChineseShower(et_1, (TextView)findViewById(R.id.money_chinese));
	}
	/**
	 * 判断产品币种  是不是"人民币元"
	 * @return
	 */
	private boolean isRMB(){
		return detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES).toString().equals(BocInvestControl.map_productCurCode.get(BocInvestControl.list_productCurCode.get(1)).toString());
	}
	/**
	 * spinner 设置 Adapter
	 * @param spinner
	 */
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	private void setAdapterForSpinner(Spinner spinner){
//		// 钞/汇
//				ArrayAdapter<ArrayList<String>> sp_adapter = new ArrayAdapter(this,
//						R.layout.custom_spinner_item, BocInvestControl.list_cashRemit);
//				sp_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//				spinner.setAdapter(sp_adapter);
//				spinner.setSelection(0);
//	}
	/**
	 * 初始化数据
	 */
	private void initDate(){
//		chooseMap = BociDataCenter.getInstance().getChoosemap();
		detailMap=BociDataCenter.getInstance().getDetailMap();
		accound_map=BocInvestControl.accound_map;
	}
	/**
	 * 初始化基类布局
	 */
	private void initBaseLayout(){
		setLeftButtonPopupGone();
		getBackgroundLayout().setTitleText("组合购买");
		getBackgroundLayout().setLeftButtonText("返回");
		getBackgroundLayout().setLeftButtonClickListener(backClickListener);
	}
	/**
	 * 监听事件，返回
	 */
	private OnClickListener backClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};
	/**
	 * 监听事件，下一步
	 */
	private OnClickListener nextClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			ArrayList<RegexpBean> arrayList = new ArrayList<RegexpBean>();
//			RegexpBean rb = new RegexpBean("组合买入金额", et_1.getText().toString(), "profitCompute");
			RegexpBean rb = BocInvestControl.getRegexpBean(detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES).toString(),
					"组合买入金额", et_1.getText().toString().trim(), null);
			arrayList.add(rb);
			if (RegexpUtils.regexpDate(arrayList)) {
				//校验通过
				requestGuarantyProductList();
			}
		}
	};
	private RadioGroup rg_ch;
	private RadioButton rb_c;
	/**
	 * 请求 持有产品列表
	 */
	private void requestGuarantyProductList(){
		HashMap<String, Object> parms_map = new HashMap<String, Object>();
		parms_map.put(BocInvt.BOCINVT_AGREE_PRODUCTCODE_REQ, detailMap.get(BocInvt.BOCI_DETAILPRODCODE_RES).toString());
		parms_map.put(BocInvt.BOCI_ACCOUNTID_REQ, accound_map.get(BocInvt.BOCI_ACCOUNTID_REQ).toString());
		BaseHttpEngine.showProgressDialog();
		getHttpTools().requestHttp(BocInvt.PSNXPADQUERYGUARANTYPRODUCTLIST, "requestGuarantyProductListCallBack", parms_map, false);
	}
	/**
	 * 回调	请求持有产品列表
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	public void requestGuarantyProductListCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(resultObj)) {
			return;
		}
//		返回报文{
//			"result":{"list":[{"productName":"1","productCode":"1","cashShare":1.000000,"remitShare":1.000000}]
//			"accountNumber":"6013***********5897"},
//			"error":null
//			}
		Map<String, Object> httpResponseDeal = getHttpTools().getResponseResult(resultObj);
		List<Map<String, Object>> re_map=(List<Map<String, Object>>)httpResponseDeal.get(BocInvt.BOCI_LIST_RES);
		if (re_map.size()==0) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("没有可用于组合购买的产品");
			return;
		}
		BocInvestControl.GuarantyProductList_map.clear();
		BocInvestControl.GuarantyProductList_map.putAll(httpResponseDeal);
		if (isRMB()) {
			BocInvestControl.GuarantyProductList_map.put(BocInvt.BOCINVT_SIGNRESULT_XPADCASHREMIT_REQ, /*tv_5.getText().toString()*/BocInvestControl.map_cashRemit_code_key.get("0"));
		}else {
			BocInvestControl.GuarantyProductList_map.put(BocInvt.BOCINVT_SIGNRESULT_XPADCASHREMIT_REQ, 
					rb_c.isChecked()?BocInvestControl.list_cashRemit.get(0):BocInvestControl.list_cashRemit.get(1)/*sp_1.getSelectedItem()*/);
		}
		BocInvestControl.GuarantyProductList_map.put(BocInvestControl.GUARANTYBUYAMOUT, et_1.getText().toString().trim());
		Intent intent = new Intent(ProductQueryAndBuyComboBuyActivity.this,ProductQueryAndBuyComboBuyGuarantyProductListActivity.class);
		startActivityForResult(intent, BocInvestControl.ACTIVITY_RESULT_CODE_COMBO_BUY);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			if (BocInvestControl.ACTIVITY_RESULT_CODE_COMBO_BUY==requestCode) {
				setResult(RESULT_OK);
				finish();
			}
			break;

		default:
			break;
		}
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode()==KeyEvent.KEYCODE_ENTER) {
			et_1.clearFocus();
		}
		return super.dispatchKeyEvent(event);
	}
}






