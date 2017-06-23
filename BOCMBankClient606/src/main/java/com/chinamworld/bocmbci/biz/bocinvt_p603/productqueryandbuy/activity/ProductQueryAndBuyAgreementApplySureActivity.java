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
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 中银理财-投资协议申请，确认页
 * @author Administrator
 *
 */
public class ProductQueryAndBuyAgreementApplySureActivity extends BocInvtBaseActivity{

	private Map<String, Object> map_agreement_result_sure;
	private Map<String, Object> map_agreement_detail;
	private Map<String, Object> map_listview_choose;
	private Map<String, Object> accound_map;
	/** 底部提示语 */
	private TextView tv_confirm;
	/** 等级是否匹配 */
	private Map<String, Object> riskMatchMap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initBaseLayout();
		setContentView(R.layout.product_query_and_buy_agreement_sure);
		initDate();
		initUI();
	}
	/**
	 * 组件初始化及赋值
	 */
	private void initUI(){
		//初始化
		TextView tv_title_1 = (TextView) findViewById(R.id.tv_title_1);
		TextView tv_content_1 = (TextView) findViewById(R.id.tv_content_1);
		Button btn_sure = (Button) findViewById(R.id.btn_sure);
		tv_confirm = (TextView) findViewById(R.id.tv_confirm);
		String type = (String) riskMatchMap.get(BocInvt.BOCINVT_MATCH_RISKMSG_RES);
		if (type.equals(/*investTypeSubList.get(0)*/"0")) {
			tv_confirm.setText(this.getString(R.string.bocinvt_confirm_bottom_1));
		} else if (type.equals(/*investTypeSubList.get(1)*/"1")) {
			tv_confirm.setText(this.getString(R.string.bocinvt_confirm_bottom_2));
		} else {
			tv_confirm.setText(this.getString(R.string.bocinvt_confirm_bottom_3));
		}
		//赋值
		tv_title_1.setText(setContentForTitleTV());
		tv_content_1.setText(setContentForContentTV());
		btn_sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BaseHttpEngine.showProgressDialog();
				//获取token
				getHttpTools().requestHttp(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API, "requestPSNGetTokenIdCallBack", new HashMap<String, Object>(), true);
			}
		});
	}
	/**
	 * 组装内容数据
	 * @return
	 */
	private String setContentForContentTV(){
		String temp_str="";
//		1、定额 
//		您申请的【agrName】，将于【firstDatePur】申购【amount】并于【 firstDateRed】赎回；之后每【periodAgr】循环一次，遇节假日顺延；共执行【 totalPeriod】次，并于【 endDate   】到期；每期将持有产品【 eachpbalDays   】； 
//		协议非正常终止：因您账户原因连续申购/赎回失败【failCount】次，协议将自动终止。 
//		2、不定额 
//		您申请的【agrName】，将于【firstDatePur】申购，账户中超过【 MINamount  】的部分将用于申购产品，最多申购【 maxamount 】，并于【 firstDateRed   】赎回；之后每【periodAgr】循环一次，遇节假日顺延；共执行【totalPeriod】次，并于【endDate   】到期；每期将持有产品【eachpbalDays   】； 
//		协议非正常终止：因您账户原因连续申购/赎回失败【failCount】次，协议将自动终止。 
//		周期连续协议 
//		您申请的【agrName】，将于【firstDatePur】申购【单期投资金额】；共持有【totalPeriod】期，每期【eachpbalDays   】，预计共【 lastDays   】，遇节假日顺延；协议到期或终止后，将按照您的协议投资金额赎回返还。 
//		协议非正常终止：因您账户原因连续申购/赎回失败【failCount】次，协议将自动终止。 
//		多次购买协议 
//		您申请的【agrName】，将于【firstDatePur】发起首次申购，后续每【periodAgr】申购【 amount   】，遇节假日顺延，如您已持有协议投资产品，系统将按照您设定的投资金额进行申购；如您未持有协议投资产品，且您的协议投资金额小于产品购买起点金额，系统将按照产品购买起点金额进行申购，请确保您账户余额充足。您选择签约【totalPeriod】期，预计供执行【  willPurCount      】次申购和【  willRedCount   】次赎回，协议期【 lastDays   】，预计【endDate  】到期终止。 
//		协议非正常终止：因您账户原因连续申购/赎回失败【failCount】次，协议将自动终止。 
//		多次赎回协议 
//		您今日申请【agrName】，（协议发起申购增加显示这句话：将于【firstDatePur】申购【 amount   】，请您保证账户内余额充足。）【firstDateRed   】首次赎回【amount   】，后续每【periodAgr】赎回【oneTmredAmt   】，遇节假日顺延。您选择签约【totalPeriod】期，预计供执行【willPurCount      】次申购和【willRedCount  】次赎回，协议期【lastDays】，预计【endDate   】到期终止。 
//		请注意，当您的持有份额小于最低持有份额时，系统将全部赎回产品。 
//		协议非正常终止：因您账户原因连续申购/赎回失败【failCount】次，协议将自动终止。
		switch (investType) {//1:周期连续协议、2:周期不连续协议、3:多次购买协议、4:多次赎回协议
		case 1:{
			temp_str=/*BocInvestControl.getNewString(*/"  您申请的"+String.valueOf(map_agreement_result_sure.get("agrName"))
					+"，将于"+String.valueOf(map_agreement_result_sure.get("firstDatePur"))
					+"申购"+StringUtil.parseStringCodePattern(String.valueOf(map_listview_choose.get(BocInvestControl.PROCUR)), String.valueOf(map_agreement_result_sure.get("amount")), 2)
					+String.valueOf(BocInvestControl.map_productCurCode_toStr.get(String.valueOf(map_listview_choose.get(BocInvestControl.PROCUR))))
					+"；共持有"+String.valueOf(map_agreement_result_sure.get("totalPeriod"))
					+"期，每期"
					+addDay(BocInvestControl.get_d_m_w_y(String.valueOf(map_agreement_result_sure.get("eachpbalDays"))))
					+"，预计共"
					+BocInvestControl.get_d_m_w_y(String.valueOf(map_agreement_result_sure.get("lastDays")))
					+"，遇节假日顺延；协议到期或终止后，将按照您的协议投资金额赎回返还。\n协议非正常终止：因您账户原因连续申购/赎回失败"
					+String.valueOf(map_agreement_result_sure.get("failMax"))+"次，协议将自动终止。 "/*)*/;
		}break;
		case 2:{
			if (isFlag) {//不定额
				temp_str=/*BocInvestControl.getNewString(*/"  您申请的"+String.valueOf(map_agreement_result_sure.get("agrName"))
						+"，将于"+String.valueOf(map_agreement_result_sure.get("firstDatePur"))
						+"申购，账户中超过"+StringUtil.parseStringCodePattern(String.valueOf(map_listview_choose.get(BocInvestControl.PROCUR)), String.valueOf(map_agreement_result_sure.get("minAmount")), 2)
						+String.valueOf(BocInvestControl.map_productCurCode_toStr.get(String.valueOf(map_listview_choose.get(BocInvestControl.PROCUR))))
						+"的部分将用于申购产品，最多申购"
						+StringUtil.parseStringCodePattern(String.valueOf(map_listview_choose.get(BocInvestControl.PROCUR)), String.valueOf(map_agreement_result_sure.get("maxAmount")), 2)
						+String.valueOf(BocInvestControl.map_productCurCode_toStr.get(String.valueOf(map_listview_choose.get(BocInvestControl.PROCUR))))
						+"，并于"+String.valueOf(map_agreement_result_sure.get("firstDateRed"))
						+"赎回；之后每"+BocInvestControl.get_d_m_w_y(String.valueOf(map_agreement_result_sure.get("periodAgr")))+"循环一次，遇节假日顺延；共执行"
						+String.valueOf(map_agreement_result_sure.get("totalPeriod"))+"次，并于"+String.valueOf(map_agreement_result_sure.get("endDate"))
						+"到期；每期将持有产品"
						+addDay(BocInvestControl.get_d_m_w_y(String.valueOf(map_agreement_result_sure.get("eachpbalDays"))))
						+"；\n协议非正常终止：因您账户原因连续申购/赎回失败"
						+String.valueOf(map_agreement_result_sure.get("failMax"))+"次，协议将自动终止。"/*)*/;
			}else {//定额
				temp_str=/*BocInvestControl.getNewString(*/"  您申请的"+String.valueOf(map_agreement_result_sure.get("agrName"))+"，将于"
						+String.valueOf(map_agreement_result_sure.get("firstDatePur"))
						+"申购"+StringUtil.parseStringCodePattern(String.valueOf(map_listview_choose.get(BocInvestControl.PROCUR)), String.valueOf(map_agreement_result_sure.get("amount")), 2)
						+String.valueOf(BocInvestControl.map_productCurCode_toStr.get(String.valueOf(map_listview_choose.get(BocInvestControl.PROCUR))))
						+"并于"+String.valueOf(map_agreement_result_sure.get("firstDateRed"))
						+"赎回；之后每"+BocInvestControl.get_d_m_w_y(String.valueOf(map_agreement_result_sure.get("periodAgr")))+"循环一次，遇节假日顺延；共执行"
						+String.valueOf(map_agreement_result_sure.get("totalPeriod"))+"次，并于"
						+String.valueOf(map_agreement_result_sure.get("endDate"))
						+"到期；每期将持有产品"+addDay(BocInvestControl.get_d_m_w_y(String.valueOf(map_agreement_result_sure.get("eachpbalDays"))))
						+"；\n协议非正常终止：因您账户原因连续申购/赎回失败"
						+String.valueOf(map_agreement_result_sure.get("failMax"))+"次，协议将自动终止。"/*)*/;
			}
		}break;
		case 3:{
			temp_str="  您申请的"+String.valueOf(map_agreement_result_sure.get("agrName"))+"，将于"
					+String.valueOf(map_agreement_result_sure.get("firstDatePur"))
					+"发起首次申购，后续每"+BocInvestControl.get_d_m_w_y(String.valueOf(map_agreement_result_sure.get("periodAgr")))+"申购"
					+StringUtil.parseStringCodePattern(String.valueOf(map_listview_choose.get(BocInvestControl.PROCUR)), String.valueOf(map_agreement_result_sure.get("amount")), 2)
					+String.valueOf(BocInvestControl.map_productCurCode_toStr.get(String.valueOf(map_listview_choose.get(BocInvestControl.PROCUR))))
					+"，遇节假日顺延，如您已持有协议投资产品，系统将按照您设定的投资金额进行申购；如您未持有协议投资产品，且您的协议投资金额小于产品购买起点金额，系统将按照产品购买起点金额进行申购，请确保您账户余额充足。您选择签约"
					+String.valueOf(map_agreement_result_sure.get("totalPeriod"))
					+"期，协议期"
					+BocInvestControl.get_d_m_w_y(String.valueOf(map_agreement_result_sure.get("lastDays")))
					+"，预计共执行"
					+String.valueOf(map_agreement_result_sure.get("willPurCount"))
					+"次申购和"+String.valueOf(map_agreement_result_sure.get("willRedCount"))+"次赎回。\n协议非正常终止：因您账户原因连续申购/赎回失败"
//					+BocInvestControl.get_d_m_w_y(String.valueOf(map_agreement_result_sure.get("lastDays")))
//					+"，预计"+String.valueOf(map_agreement_result_sure.get("endDate"))+"到期终止。\n协议非正常终止：因您账户原因连续申购/赎回失败"
					+String.valueOf(map_agreement_result_sure.get("failMax"))+"次，协议将自动终止。";
		}break;
		case 4:{
			temp_str=/*BocInvestControl.getNewString(*/"  您今日申请"+String.valueOf(map_agreement_result_sure.get("agrName"))+"，"
					+getStrForApplyBuy()+String.valueOf(map_agreement_result_sure.get("firstDateRed"))
					+"首次赎回"+StringUtil.parseStringCodePattern(String.valueOf(map_listview_choose.get(BocInvestControl.PROCUR)), String.valueOf(map_agreement_result_sure.get("oneTmredAmt")), 2)
					+String.valueOf(BocInvestControl.map_productCurCode_toStr.get(String.valueOf(map_listview_choose.get(BocInvestControl.PROCUR))))
					+"，后续每"+BocInvestControl.get_d_m_w_y(String.valueOf(map_agreement_result_sure.get("periodAgr")))
					+"赎回"+StringUtil.parseStringCodePattern(String.valueOf(map_listview_choose.get(BocInvestControl.PROCUR)), String.valueOf(map_agreement_result_sure.get("oneTmredAmt")),2)
					+String.valueOf(BocInvestControl.map_productCurCode_toStr.get(String.valueOf(map_listview_choose.get(BocInvestControl.PROCUR))))+"，遇节假日顺延。您选择签约"
					+String.valueOf(map_agreement_result_sure.get("totalPeriod"))
//					+"期，预计供执行"
					+"期，协议期"
					+BocInvestControl.get_d_m_w_y(String.valueOf(map_agreement_result_sure.get("lastDays")))
					+"，预计共执行"
					+String.valueOf(map_agreement_result_sure.get("willPurCount"))
					+"次申购和"+String.valueOf(map_agreement_result_sure.get("willRedCount"))
//					+"次赎回，协议期"
//					+BocInvestControl.get_d_m_w_y(String.valueOf(map_agreement_result_sure.get("lastDays")))
					+"次赎回。\n请注意，当您的持有份额小于最低持有份额时，系统将全部赎回产品。\r\n协议非正常终止：因您账户原因连续申购/赎回失败"
//					+"，预计"+String.valueOf(map_agreement_result_sure.get("endDate"))
//					+"到期终止。\n请注意，当您的持有份额小于最低持有份额时，系统将全部赎回产品。\r\n协议非正常终止：因您账户原因连续申购/赎回失败"
					+String.valueOf(map_agreement_result_sure.get("failMax"))+"次，协议将自动终止。"/*)*/;
		}break;
		}
		return temp_str;
	}
	/**
	 * 字串不包含  "天" 自动补上  "天"
	 * @param str
	 * @return
	 */
	private String addDay(String str){
		if (!str.contains("天")) {
			return str+"天";
		}
		return str;
	}
	/**
	 * 当协议发起申购时 返回的拼接字段
	 * @return
	 */
	private String getStrForApplyBuy(){
		if (String.valueOf(map_agreement_detail.get(BocInvestControl.ISNEEDPUR)).equals("0")) {
			return "将于"+String.valueOf(map_agreement_result_sure.get("firstDatePur"))+"申购"+StringUtil.parseStringCodePattern(String.valueOf(map_listview_choose.get(BocInvestControl.PROCUR)), String.valueOf(map_agreement_result_sure.get("amount")),2)
					+String.valueOf(BocInvestControl.map_productCurCode_toStr.get(String.valueOf(map_listview_choose.get(BocInvestControl.PROCUR))))
					+"，请您保证账户内余额充足。\n";
		}
		return "";
	}
	/**
	 * 组装标题数据
	 * @return
	 */
	private String setContentForTitleTV(){
		return "您申请的"+map_agreement_result_sure.get(BocInvestControl.AGRNAME).toString()+"信息如下，请确认";
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
			return;
		}
		String str_token = getHttpTools().getResponseResult(reslutObj).toString();
		requestPsnXpadAptitudeTreatyApplyCommit(str_token);
	}
	/**
	 * 请求 智能协议申请交易
	 * @param token
	 */
	private void requestPsnXpadAptitudeTreatyApplyCommit(String token){
		Map<String, Object> parms_map=new HashMap<String, Object>();
		parms_map.put(BocInvt.BOCI_ACCOUNTID_REQ, accound_map.get(BocInvt.BOCIBINDING_ACCOUNTIDF_RES).toString());
		parms_map.put(BocInvt.BOCINVT_SIGNINIT_PRODUCTCODE_REQ, map_agreement_detail.get(BocInvt.BOCINVT_SIGNINIT_PRODUCTCODE_RES).toString());
		parms_map.put(BocInvt.BOCINVT_SIGNINIT_PRODUCTNAME_REQ, map_agreement_detail.get(BocInvt.BOCINVT_SIGNINIT_PRODUCTNAME_RES).toString());
		parms_map.put(BocInvt.BOCINVT_AGREE_PROCUR_RES, (String)map_listview_choose.get(BocInvestControl.PROCUR));
		parms_map.put(BocInvt.BOCINVT_XPADRESULT_TOKEN_REQ, token);
		getHttpTools().requestHttp(BocInvestControl.PSNXPADAPTITUDETREATYAPPLYCOMMIT, "requestPsnXpadAptitudeTreatyApplyCommitCallBack", parms_map, true);
	}
	@SuppressWarnings({ "unchecked", "static-access" })
	public void requestPsnXpadAptitudeTreatyApplyCommitCallBack(Object resultObj){
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(resultObj)) {
			return;
		}
		Map<String , Object> responseDeal = (Map<String , Object>)getHttpTools().getResponseResult(resultObj);
		BocInvestControl.map_agreement_result_succeed.clear();
		BocInvestControl.map_agreement_result_succeed.putAll(responseDeal);
		startActivityForResult(new Intent(
				ProductQueryAndBuyAgreementApplySureActivity.this,ProductQueryAndBuyAgreementApplySucceedActivity.class)
		.putExtra(BocInvestControl.key_isFlag_agreement_apply, isFlag)
		.putExtra(BocInvestControl.key_investType_agreement_apply, investType), 
				BocInvestControl.ACTIVITY_RESULT_CODE_AGREEMENT_APPLY);
	}
	/**
	 * 初始化数据
	 */
	private void initDate(){
//		chooseMap = BociDataCenter.getInstance().getChoosemap();
//		detailMap=BociDataCenter.getInstance().getDetailMap();
//		result_agreement=BocInvestControl.result_agreement;
		accound_map=BocInvestControl.accound_map;
		map_agreement_detail=BocInvestControl.map_agreement_detail;
		map_listview_choose=BocInvestControl.map_listview_choose;
		map_agreement_result_sure=BocInvestControl.map_agreement_result_sure;
		riskMatchMap = BociDataCenter.getInstance().getRiskMatchMap();
		getIntentDate();
	}
	/**
	 * 获取上一界面传递过来的数据
	 */ 
	private void getIntentDate(){
		Intent intent = getIntent();
		if (intent!=null) {
			isFlag = intent.getBooleanExtra(BocInvestControl.key_isFlag_agreement_apply, false);
			investType = intent.getIntExtra(BocInvestControl.key_investType_agreement_apply, -1);
		}
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
	private boolean isFlag;
	private int investType;
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

}
