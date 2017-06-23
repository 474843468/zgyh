package com.chinamworld.bocmbci.biz.bocinvt_p603.productqueryandbuy.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt.productlist.AgreementChooseActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.userwidget.LabelTextView;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 中银理财-投资协议申请，协议基本信息页
 * @author Administrator
 *
 */
public class ProductQueryAndBuyAgreementApplyAgreementBaseMessageActivity extends BocInvtBaseActivity{

	private Map<String, Object> map_listview_choose;
	private Map<String, Object> accound_map;
	private Map<String, Object> detailMap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initBaseLayout();
		setContentView(R.layout.product_query_and_buy_agreement_base_message);
		initDate();
		initUI();
	}
	/**
	 * 组件初始化及赋值
	 */
	private void initUI(){
//		getBackgroundLayout().setTitleText(getInstType(map_listview_choose.get(BocInvestControl.INSTTYPE).toString()));
		//初始化
		LabelTextView tv_1 = (LabelTextView) findViewById(R.id.tv_1);
		LabelTextView tv_2 = (LabelTextView) findViewById(R.id.tv_2);
		LabelTextView tv_3 = (LabelTextView) findViewById(R.id.tv_3);
		LabelTextView tv_4 = (LabelTextView) findViewById(R.id.tv_4);
		TextView tv_5 = (TextView) findViewById(R.id.tv_5);
		Button btn_apply = (Button) findViewById(R.id.btn_apply);
		//赋值
		tv_1.setValueText(BocInvestControl.map_agrType_code_key.get(map_listview_choose.get(BocInvestControl.AGRTYPE).toString()));
		tv_2.setValueText(map_listview_choose.get(BocInvestControl.AGRNAME).toString());
		tv_3.setValueText(map_listview_choose.get(BocInvestControl.PRONAM).toString());
		tv_4.setValueText(BocInvestControl.map_productCurCode_toStr.get(map_listview_choose.get(BocInvt.BOC_QUERY_AGREE_PROCUR_RES).toString()));
		final Object obj_instType = map_listview_choose.get(BocInvestControl.INSTTYPE);
		if (StringUtil.isNullOrEmpty(obj_instType)) {
			tv_5.setText("");
		}else {
			switch (Integer.parseInt((String)obj_instType)) {
			case 1:{//周期连续协议 
				tv_5.setText("一次设定，无需反复交易——协议以"+
						BocInvestControl.get_d_m_w_y2((String)map_listview_choose.get(BocInvestControl.PERIODAGR))+
						"为周期，按您指定的金额和选定的期数滚动投资；到期或终止前本金不返回。");
			}break;
			case 2:{//周期不连续协议
				tv_5.setText("滚动申购赎回，满足周期性资金需求——协议以"+
						BocInvestControl.get_d_m_w_y2((String)map_listview_choose.get(BocInvestControl.PERIODAGR))+
						"为周期，每期持有"+
						(String)map_listview_choose.get("periodBal")+//份额持有天数
						"天，按您指定的金额和选定的期数滚动申购和赎回。");
			}break;
			case 3:{//多次购买协议
				tv_5.setText("零存整取，到期赎回——在"+
						BocInvestControl.get_d_m_w_y2((String)map_listview_choose.get(BocInvestControl.PERIODAGR))+
						"之内，每"+
						BocInvestControl.get_d_m_w_y((String)map_listview_choose.get("periodPur"))+//剩余可购买期数频率
						"按您指定的金额申购，协议到期后系统自动赎回或您主动赎回。"
			);
			}break;
			case 4:{//多次赎回协议
				if (((String)map_listview_choose.get("isNeedPur")).equals("0")) {//0：期初购买 1：协议不购买
					tv_5.setText("整存零取，满足周期性用款需求——在"+
							BocInvestControl.get_d_m_w_y2((String)map_listview_choose.get(BocInvestControl.PERIODAGR))+
							"之内，"+
							"按您设定的赎回金额和期数自动进行申购，"+		//如协议申购显示即isNeedDpur值为0
							"每"+
							BocInvestControl.get_d_m_w_y((String)map_listview_choose.get("periodPur"))+//剩余可购买期数频率
							"按您指定的金额赎回。"
							);
				}else {
					tv_5.setText("整存零取，满足周期性用款需求——在"+
							BocInvestControl.get_d_m_w_y2((String)map_listview_choose.get(BocInvestControl.PERIODAGR))+
							"之内，每"+
							BocInvestControl.get_d_m_w_y((String)map_listview_choose.get("periodPur"))+//剩余可购买期数频率
							"按您指定的金额赎回。"
							);
				}
			}break;
			case 5:{//定时定额投资
//				tv_5.setText("协议以"+
//						BocInvestControl.get_d_m_w_y((String)map_listview_choose.get("periodPur"))+//剩余可购买期数频率
//						"为周期，每"+
//						BocInvestControl.get_d_m_w_y((String)map_listview_choose.get(BocInvestControl.PERIODAGR))+
//						"按您指定日期和金额申购/赎回，按照您设定的结束条件停止申购/赎回。"
//						);
				tv_5.setText("协议以周/月/年为周期；您可自主选择协议周期，并按您指定的金额申购/赎回，按您设定的结束条件停止申购/赎回。"
						);
			}break;
			case 6:{//余额理财投资
				tv_5.setText("协议生效后，当您的账户余额低于您设定的赎回触发金额时，系统将赎回您持有的产品，当您的账户余额超出您设定的申购触发金额时，超出部分系统将自动用于申购产品，协议将根据您设置的结束条件自动结束。");
			}break;
			case 7:{//周期滚续协议
				tv_5.setText("协议生效后，按照您指定的金额为您申购产品，共持有您指定的期数，到期后系统将自动赎回全部金额。");
			}break;
			case 8:{//业绩基准周期滚续
				tv_5.setText("协议生效后，按照您指定的金额为您申购产品，共持有您指定的期数，到期后系统将自动赎回全部金额。");
			}break;
			default:
				break;
			}
		}
		btn_apply.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//智能协议调用接口查询协议详情，非智能协议按照P603之前老规则(只在申请的时候调用PsnXpadApplyAgreementResult接口进行协议申请)
				switch (Integer.parseInt((String)obj_instType)) {
				case 1:{//智能协议，周期连续协议
					BaseHttpEngine.showProgressDialog();
					requestCommConversationId();
				}break;
				case 2:{//智能协议，周期不连续协议
					BaseHttpEngine.showProgressDialog();
					requestCommConversationId();
				}break;
				case 3:{//智能协议，多次购买协议 
					BaseHttpEngine.showProgressDialog();
					requestCommConversationId();
				}break;
				case 4:{//智能协议，多次赎回协议
					BaseHttpEngine.showProgressDialog();
					requestCommConversationId();
				}break;
				case 8:{//非智能协议，业绩基准周期滚续(走购买流程，跳转到购买流程页)
					BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.IsFromAgreeApply, "1");
					BocInvestControl.toBuyActivity(ProductQueryAndBuyAgreementApplyAgreementBaseMessageActivity.this, 
							(String)detailMap.get(BocInvestControl.PRODUCTKIND),
							(String)detailMap.get(BocInvt.BOCI_DETAILPRODCODE_RES),
							accound_map,BocInvestControl.SYSTEM_DATE, true, BocInvestControl.ACTIVITY_RESULT_CODE_AGREEMENT_APPLY);
				}break;
				default:{//非智能协议,5:定时定额投资6:余额理财投资7:周期滚续协议(走P603之前老流程)
//					startActivityForResult(
//							new Intent(ProductQueryAndBuyAgreementApplyAgreementBaseMessageActivity.this,ProductQueryAndBuyAgreementApplyStateActivity.class),
//							BocInvestControl.ACTIVITY_RESULT_CODE_AGREEMENT_APPLY);
					startActivityForResult(
							new Intent(ProductQueryAndBuyAgreementApplyAgreementBaseMessageActivity.this,AgreementChooseActivity.class),
							BocInvestControl.ACTIVITY_RESULT_CODE_AGREEMENT_APPLY);
				}break;
				}
//				BaseHttpEngine.showProgressDialog();
//				requestCommConversationId();
			}
		});
	}
	
	/**
	 * 返回数据中 "协议类型"
	 * @return
	 */
//	private String getInstType(String str){
//		switch (Integer.parseInt(str)) {
//		case 1:
//			return "周期连续协议";
//		case 2:
//			return "周期不连续协议";
//		case 3:
//			return "多次购买协议";
//		case 4:
//			return "多次赎回协议";
//		case 5:
//			return "定时定额投资";
//		case 6:
//			return "余额理财投资";
//		case 7:
//			return "周期滚续协议";
//		case 8:
//			return "业绩基准周期滚续";
//		default:
//			return "";
//		}
//	}
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPsnXpadAptitudeTreatyDetailQuery();
	}
	/**
	 * 请求智能协议详情
	 */
	private void requestPsnXpadAptitudeTreatyDetailQuery(){
		Map<String,Object> parms_map=new HashMap<String, Object>();
		parms_map.put(BocInvt.BOCI_FAST_ACCOUNTID_REQ, accound_map.get(BocInvt.BOCIBINDING_ACCOUNTIDF_RES).toString());
		parms_map.put(BocInvestControl.AGRCODE, map_listview_choose.get(BocInvestControl.AGRCODE).toString());
		getHttpTools().requestHttp(BocInvestControl.PSNXPADAPTITUDETREATYDETAILQUERY, "requestPsnXpadAptitudeTreatyDetailQueryCallBack", parms_map, true);
	}
	@SuppressWarnings({ "unchecked", "static-access" })
	public void requestPsnXpadAptitudeTreatyDetailQueryCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(resultObj)) {
			return;
		}
		BocInvestControl.map_agreement_detail.clear();
		BocInvestControl.map_agreement_detail.putAll((Map<String, Object>)getHttpTools().getResponseResult(resultObj));
		startActivityForResult(
				new Intent(ProductQueryAndBuyAgreementApplyAgreementBaseMessageActivity.this,ProductQueryAndBuyAgreementApplyStateActivity.class),
				BocInvestControl.ACTIVITY_RESULT_CODE_AGREEMENT_APPLY);
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
//		result_agreement=BocInvestControl.result_agreement;
//		chooseMap = BociDataCenter.getInstance().getChoosemap();
		detailMap=BociDataCenter.getInstance().getDetailMap();
		accound_map=BocInvestControl.accound_map;
		map_listview_choose=BocInvestControl.map_listview_choose;
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
