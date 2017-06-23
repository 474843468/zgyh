package com.chinamworld.bocmbci.biz.bocinvt_p603.productqueryandbuy.activity;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.userwidget.LabelTextView;

/**
 * 中银理财-投资协议申请，协议信息页
 * @author Administrator
 *
 */
public class ProductQueryAndBuyAgreementApplyAgreementMessageActivity extends BocInvtBaseActivity{

//	private Map<String, Object> map_listview_choose;
	private Map<String, Object> map_agreement_detail;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initBaseLayout();
		setContentView(R.layout.product_query_and_buy_agreement_messages);
		initDate();
		initUI();
	}
	/**
	 * 组件初始化及赋值
	 */
	private void initUI(){
		//初始化
		TextView tv_introduce =  (TextView) findViewById(R.id.tv_introduce);
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
		Button btn_next = (Button) findViewById(R.id.btn_next);
		//赋值
		boolean gone_9=false;//是否不显示，true/不显示
		boolean gone_10=false;
		boolean gone_12=false;
		boolean gone_13=false;
		boolean gone_14=false;
		boolean gone_15=false;
		switch (Integer.parseInt(map_agreement_detail.get(BocInvestControl.INVESTTYPE).toString())) {
//		1:周期连续协议 
//		2:周期不连续协议 
//		3:多次购买协议 
//		4:多次赎回协议
		case 1:{
			tv_10.setVisibility(View.GONE);
			tv_12.setVisibility(View.GONE);
			tv_13.setVisibility(View.GONE);
			tv_14.setVisibility(View.GONE);
			tv_15.setVisibility(View.GONE);
			gone_10=true;
			gone_12=true;
			gone_13=true;
			gone_14=true;
			gone_15=true;
		}break;
		case 2:{
			tv_10.setVisibility(View.GONE);
			tv_12.setVisibility(View.GONE);
			gone_10=true;
			gone_12=true;
		}break;
		case 3:{
			tv_9.setVisibility(View.GONE);
			tv_10.setVisibility(View.GONE);
			tv_15.setVisibility(View.GONE);
			gone_9=true;
			gone_10=true;
			gone_15=true;
		}break;
		case 4:{
			tv_9.setVisibility(View.GONE);
			tv_12.setVisibility(View.GONE);
			tv_14.setVisibility(View.GONE);
			gone_9=true;
			gone_12=true;
			gone_14=true;
		}break;
		default:
			break;
		}
		tv_introduce.setText("        "+String.valueOf(map_agreement_detail.get("memo")));//协议介绍
		tv_1.setValueText(String.valueOf(map_agreement_detail.get(BocInvestControl.AGRCODE)));
		tv_2.setValueText(String.valueOf(map_agreement_detail.get(BocInvestControl.AGRNAME)));
		tv_3.setValueText(String.valueOf(BocInvestControl.map_agrType_code_key.get(String.valueOf(map_agreement_detail.get(BocInvestControl.AGRTYPE)))));
		tv_4.setValueText(String.valueOf(BocInvestControl.map_instType_all_code_key.get(String.valueOf(map_agreement_detail.get(BocInvestControl.INVESTTYPE)))));
		tv_5.setValueText(String.valueOf(map_agreement_detail.get(BocInvestControl.AGRPERIOD)));
		tv_6.setValueText(String.valueOf(map_agreement_detail.get(BocInvestControl.AGRCURRPERIOD)));
		tv_7.setValueText(BocInvestControl.get_d_m_w_y(String.valueOf(map_agreement_detail.get(BocInvestControl.PERIODAGR))));//投资周期
		tv_8.setValueText(String.valueOf(map_agreement_detail.get(BocInvestControl.MININVESTPERIOD)));
		
		if (!gone_9) {
			tv_9.setValueText(BocInvestControl.get_d_m_w_y(String.valueOf(map_agreement_detail.get(BocInvestControl.SINGLEINVESTPERIOD))));
		}
		if (!gone_10) {
			tv_10.setValueText(String.valueOf(BocInvestControl.map_isNeedPur_code_key.get(String.valueOf(map_agreement_detail.get(BocInvestControl.ISNEEDPUR)))));
		}
		if (!gone_12) {
			tv_12.setValueText(String.valueOf(BocInvestControl.map_isNeedRed_code_key.get(String.valueOf(map_agreement_detail.get(BocInvestControl.ISNEEDRED)))));
		}
		if (!gone_13) {
			tv_13.setValueText(String.valueOf(map_agreement_detail.get(BocInvestControl.FIRSTDATERED)));
		}
		if (!gone_14) {
			tv_14.setValueText("每"+BocInvestControl.get_d_m_w_y(String.valueOf(map_agreement_detail.get("periodPur")))+"申购");
//			tv_14.setValueText(String.valueOf(BocInvestControl.map_isNeedPur_code_key.get(String.valueOf(map_agreement_detail.get(BocInvestControl.ISNEEDPUR)))));
		}
		if (!gone_15) {
			tv_15.setValueText("每"+BocInvestControl.get_d_m_w_y(String.valueOf(map_agreement_detail.get("periodRed")))+"赎回");
//			tv_15.setValueText(String.valueOf(BocInvestControl.map_isNeedRed_code_key.get(String.valueOf(map_agreement_detail.get(BocInvestControl.ISNEEDRED)))));
		}
		tv_11.setValueText(String.valueOf(map_agreement_detail.get(BocInvestControl.FIRSTDATEPUR)));
		btn_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(
						new Intent(ProductQueryAndBuyAgreementApplyAgreementMessageActivity.this,ProductQueryAndBuyAgreementApplyProductMessageActivity.class), 
						BocInvestControl.ACTIVITY_RESULT_CODE_AGREEMENT_APPLY);
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;
		default:
			break;
		}
	}
	/**
	 * 初始化数据
	 */
	private void initDate(){
//		chooseMap = BociDataCenter.getInstance().getChoosemap();
//		detailMap=BociDataCenter.getInstance().getDetailMap();
//		accound_map=BocInvestControl.accound_map;
//		result_agreement=BocInvestControl.result_agreement;
		map_agreement_detail=BocInvestControl.map_agreement_detail;
//		map_listview_choose=BocInvestControl.map_listview_choose;
	}
	/**
	 * 初始化基类布局
	 */
	private void initBaseLayout(){
		setLeftButtonPopupGone();
		getBackgroundLayout().setTitleText("投资协议申请");
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

}
