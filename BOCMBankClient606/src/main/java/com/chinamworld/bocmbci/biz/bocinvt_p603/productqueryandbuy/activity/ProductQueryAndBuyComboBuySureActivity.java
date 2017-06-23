package com.chinamworld.bocmbci.biz.bocinvt_p603.productqueryandbuy.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.userwidget.LabelTextView;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 中银理财-组合购买，确认交易信息页
 * @author Administrator
 *
 */
public class ProductQueryAndBuyComboBuySureActivity extends BocInvtBaseActivity{

	/**用户选择购买的产品  信息集合*/
//	private Map<String, Object> chooseMap;
	/**用户选择购买的产品  详情信息集合*/
	private Map<String, Object> detailMap;
	/**账户详情*/
	private Map<String, Object> accound_map;
	/**持有产品列表查询结果*/
	private Map<String, Object> GuarantyProductList_map;
	/**是否进行过风险评估，回调结果*/
	private Map<String, Object> danger_map;
	/**组合购买，持有产品列表-用户选择、输入要组合购买的产品列表*/
	private List<Map<String, Object>> GuarantyProductList_edit_list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initBaseLayout();
		setContentView(R.layout.product_query_and_buy_combo_buy_product_message);
		initDate();
		initUI();
	}
	/**
	 * 组件初始化及赋值
	 */
	private void initUI(){
		//初始化组件
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
//		Button btn_last = (Button) findViewById(R.id.btn_last);
		Button btn_sure = (Button) findViewById(R.id.btn_sure);
		//赋值
		btn_sure.setOnClickListener(btn_sure_click);
		tv_1.setValueText(detailMap.get(BocInvt.BOCINVT_BUYRES_PRODCODE_RES).toString());
		tv_2.setValueText(detailMap.get(BocInvt.BOCINVT_BUYRES_PRODNAME_RES).toString());
		tv_3.setValueText(BocInvestControl.map_productCurCode_toStr.get(detailMap.get(BocInvt.BOCINVT_HOLDPRO_CURCODE_RES).toString()));
		tv_4.setValueText(getString(GuarantyProductList_map.get(BocInvt.BOCINVT_SIGNRESULT_XPADCASHREMIT_REQ).toString()));
		tv_5.setValueText(detailMap.get(BocInvt.BOCINVT_BUYRES_PRODBEGIN_RES).toString());
		if (detailMap.get(BocInvt.BOCINCT_XPADTRAD_PRODUCTTYPE_RES).toString().equals("2")&&detailMap.get("productTermType").toString().equals("3")) {//净值型产品&&产品期限特性：无限开放式
			tv_6.setValueText("长期");
		}else {
			tv_6.setValueText(detailMap.get(BocInvt.BOCINVT_BUYRES_PRODEND_RES).toString());
		}
		if (detailMap.get(BocInvt.BOCINCT_XPADTRAD_PRODUCTTYPE_RES).toString().equals("2")) {//净值产品
			tv_7.setVisibility(View.GONE);
		}else {
			tv_7.setVisibility(View.VISIBLE);
			tv_7.setValueText(StringUtil.parseStringCodePattern((String)detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES), detailMap.get(BocInvt.BOCINVT_BUYPRE_BUYPRICE_REQ).toString(), 2));
		}
		tv_8.setValueText(StringUtil.parseStringCodePattern((String)detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES), GuarantyProductList_map.get(BocInvestControl.GUARANTYBUYAMOUT).toString(), 2));
		tv_9.setValueText(GuarantyProductList_edit_list.size()+"");
		tv_10.setValueText(BocInvestControl.map_prodRisklvl_toStr.get(detailMap.get(BocInvt.BOCINVT_HOLDPRO_PRODRISKLVL_RES).toString()));
		tv_11.setValueText(BocInvestControl.map_riskLevel.get(danger_map.get(BocInvt.BOCIEVA_RISKLEVEL_RES)));
	}
	private OnClickListener btn_sure_click=new OnClickListener() {
		@Override
		public void onClick(View v) {
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
		}
	};
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId();
	}
	private String getString(String str){
			return BocInvestControl.map_cashRemit_code_key.get("0").equals(str) ? getResources().getString(R.string.bocinvt_tv_41):str;
	}
	/**
	 * 获取tokenId
	 */
	public void requestPSNGetTokenId(){
		getHttpTools().requestHttp(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API, "requestPSNGetTokenIdCallBack", new HashMap<String, Object>(), true);
	}
	/**
	 * 处理获取tokenId的数据得到tokenId
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	
	@SuppressWarnings("static-access")
	public void requestPSNGetTokenIdCallBack(Object reslutObj){
		if (StringUtil.isNullOrEmpty(reslutObj)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		String str_token = getHttpTools().getResponseResult(reslutObj).toString();
		requestPsnXpadGuarantyBuyResult(str_token);
	}
	/**
	 * 请求  组合购买
	 */
	private void requestPsnXpadGuarantyBuyResult(String tokenId){
		Map<String, Object> parms_map=new HashMap<String, Object>();
		parms_map.put(BocInvt.BOCINVT_MATCH_PRODUCTCODE_REQ, detailMap.get(BocInvt.BOCINVT_BUYRES_PRODCODE_RES).toString());
		parms_map.put(BocInvt.BOCINVT_MATCH_PRODUCTCODE_REQ, detailMap.get(BocInvt.BOCINVT_BUYRES_PRODCODE_RES).toString());
		parms_map.put(BocInvt.BOCINVT_BUYRES_PRODUCTNAME_REQ, detailMap.get(BocInvt.BOCINVT_BUYRES_PRODNAME_RES).toString());
		parms_map.put(BocInvestControl.GUARANTYBUYAMOUT, GuarantyProductList_map.get(BocInvestControl.GUARANTYBUYAMOUT).toString());
		parms_map.put(BocInvt.BOCINVT_BUYRES_BUYPRICE_RES, detailMap.get(BocInvt.BOCINVT_BUYPRE_BUYPRICE_REQ).toString());
		parms_map.put(BocInvestControl.PRODUCTCURRENCY, detailMap.get(BocInvt.BOCINVT_HOLDPRO_CURCODE_RES).toString());
		parms_map.put(BocInvt.BOCINVT_REDVER_CASHREMIT_REQ, BocInvestControl.map_cashRemit_code_value.get(BocInvestControl.GuarantyProductList_map.get(BocInvt.BOCINVT_SIGNRESULT_XPADCASHREMIT_REQ)));
		parms_map.put(BocInvestControl.PRODUCTNUM, GuarantyProductList_edit_list.size()+"");
		parms_map.put(BocInvestControl.OVERFLAG, "1");//根据当前校验规则，如果用户不同意的话无法进行后续交易，所以当前参数写成固定值		0/不同意、1/同意
		parms_map.put(BocInvt.BOCINVT_REDVER_TOKEN_REQ, tokenId);
		parms_map.put(BocInvt.BOCINVT_REDVER_PRODCODE_RES, get_prodCodeList());//被组合产品代码
		parms_map.put(BocInvestControl.FREEZEUNIT, get_freezeUnit());//被组合产品份额
		parms_map.put(BocInvt.BOCI_ACCOUNTID_REQ, accound_map.get(BocInvt.BOCIBINDING_ACCOUNTIDF_RES));
		getHttpTools().requestHttp(BocInvestControl.INF_PSNXPADGUARANTYBUYRESULT, "requestPsnXpadGuarantyBuyResultCallBack", parms_map, true);
	}
	/**
	 * 请求  组合购买 回调
	 * @param reslutObj
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	public void requestPsnXpadGuarantyBuyResultCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(resultObj)) {
			return;
		}
		Map<String, Object> result_map = (Map<String, Object>)getHttpTools().getResponseResult(resultObj);	
		BocInvestControl.result_combo_buy.clear();
		BocInvestControl.result_combo_buy.putAll(result_map);
		Intent intent = new Intent(ProductQueryAndBuyComboBuySureActivity.this,ProductQueryAndBuyComboBuySucceedActivity.class);
		startActivityForResult(intent, BocInvestControl.ACTIVITY_RESULT_CODE_COMBO_BUY);
	}
	private String[] get_freezeUnit(){
		String[] str=new String[GuarantyProductList_edit_list.size()];
		for (int i = 0; i < GuarantyProductList_edit_list.size(); i++) {
			str[i]=((Map<String, Object>)GuarantyProductList_edit_list.get(i)).get(BocInvestControl.FREEZEUNIT).toString();
		}
		return str;
	}
	private String[] get_prodCodeList(){
		String[] str=new String[GuarantyProductList_edit_list.size()];
		for (int i = 0; i < GuarantyProductList_edit_list.size(); i++) {
			str[i]=((Map<String, Object>)GuarantyProductList_edit_list.get(i)).get(BocInvt.BOCINVT_AGREE_PRODUCTCODE_REQ).toString();
		}
		return str;
	}
	/**
	 * 初始化数据
	 */
	private void initDate(){
//		chooseMap = BociDataCenter.getInstance().getChoosemap();
		detailMap=BociDataCenter.getInstance().getDetailMap();
		accound_map=BocInvestControl.accound_map;
		GuarantyProductList_map=BocInvestControl.GuarantyProductList_map;
		GuarantyProductList_edit_list=BocInvestControl.GuarantyProductList_edit_list;
		danger_map=BocInvestControl.danger_map;
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
}
