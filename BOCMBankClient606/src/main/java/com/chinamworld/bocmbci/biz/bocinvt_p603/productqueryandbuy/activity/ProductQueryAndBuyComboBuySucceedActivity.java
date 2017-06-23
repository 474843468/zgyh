package com.chinamworld.bocmbci.biz.bocinvt_p603.productqueryandbuy.activity;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.userwidget.LabelTextView;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 中银理财-组合购买，成功页
 * @author Administrator
 *
 */
public class ProductQueryAndBuyComboBuySucceedActivity extends BocInvtBaseActivity{

	/**组合购买结果*/
	private Map<String, Object> result_succeed_map;
	/**持有产品列表-用户选择、输入要组合购买的产品列表*/
	private List<Map<String, Object>> GuarantyProductList_edit_list;
	private Map<String, Object> detailMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initBaseLayout();
		setContentView(R.layout.product_query_and_buy_combo_buy_succeed);
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
		Button btn_sure = (Button) findViewById(R.id.btn_sure);
		//赋值
		tv_1.setValueText(result_succeed_map.get(BocInvt.BOCINVT_SIGNRESULT_TRANSEQ_RES).toString());
		tv_2.setValueText(result_succeed_map.get(BocInvt.BOCINVT_BUYRES_PRODUCTCODE_REQ).toString());
		tv_3.setValueText(result_succeed_map.get(BocInvt.BOCINVT_SIGNINIT_PRODUCTNAME_REQ).toString());
		tv_4.setValueText(BocInvestControl.map_productCurCode_toStr.get(result_succeed_map.get(BocInvt.BOCINVT_CANCEL_CURRENCY_REQ).toString()));
		tv_5.setValueText(getCashRemit(result_succeed_map.get(BocInvt.BOC_QUERY_AGREE_CASHREMIT_RES).toString()));
		tv_6.setValueText(result_succeed_map.get(BocInvestControl.PRODUCTBEGINDATE).toString());
		if (detailMap.get(BocInvt.BOCINCT_XPADTRAD_PRODUCTTYPE_RES).toString().equals("2")&&detailMap.get("productTermType").toString().equals("3")) {
			tv_7.setValueText("长期");
		}else {
			tv_7.setValueText(result_succeed_map.get(BocInvestControl.PRODUCTENDDATE).toString());
		}
		if (detailMap.get(BocInvt.BOCINCT_XPADTRAD_PRODUCTTYPE_RES).toString().equals("2")) {
			tv_8.setVisibility(View.GONE);
		}else {
			tv_8.setVisibility(View.VISIBLE);
			tv_8.setValueText(StringUtil.parseStringCodePattern((String)result_succeed_map.get(BocInvt.BOCINVT_XPADQUERY_CURRENCY_RES), result_succeed_map.get(BocInvt.BOCI_DETAILBUYPRICE_RES).toString(), 2));
		}
		tv_9.setValueText(StringUtil.parseStringCodePattern((String)result_succeed_map.get(BocInvt.BOCINVT_XPADQUERY_CURRENCY_RES)
				, result_succeed_map.get(BocInvestControl.BUYAMOUNT).toString(), 2));
		tv_10.setValueText(GuarantyProductList_edit_list.size()+"");
		tv_11.setValueText(StringUtil.parseStringCodePattern((String)result_succeed_map.get(BocInvt.BOCINVT_XPADQUERY_CURRENCY_RES), getStr()+"", 2));
		btn_sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
//				startActivity(new Intent(ProductQueryAndBuyComboBuySucceedActivity.this,QueryProductActivity.class));
			}
		});
	}
	private String getCashRemit(String str){
		if (StringUtil.isNullOrEmpty(str)) {
			return "";	
		}
		return str.equals("0")?"-":BocInvestControl.map_cashRemit_code_key.get(str);
	}
	private double getStr(){
		double temp = 0;
		for (Map<String, Object> itemMap : GuarantyProductList_edit_list) {
			temp+=Double.parseDouble(itemMap.get(BocInvestControl.FREEZEUNIT).toString());
		}
		return temp;
	}
	/**
	 * 初始化数据
	 */
	private void initDate(){
		detailMap=BociDataCenter.getInstance().getDetailMap();
		GuarantyProductList_edit_list=BocInvestControl.GuarantyProductList_edit_list;
		result_succeed_map=BocInvestControl.result_combo_buy;
	}
	/**
	 * 初始化基类布局s
	 */
	private void initBaseLayout(){
		setLeftButtonPopupGone();
		getBackgroundLayout().setTitleText("组合购买");
		getBackgroundLayout().setLeftButtonText("");
	}
	
	@Override
	public void onBackPressed() {
		return;
	}

}
