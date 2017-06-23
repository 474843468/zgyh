package com.chinamworld.bocmbci.biz.finc.orcm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.FincUtils;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.biz.finc.myfund.MyFincScheduSellActivity;
import com.chinamworld.bocmbci.biz.finc.myfund.MyFincSellSubmitActivity;
import com.chinamworld.bocmbci.biz.finc.trade.FincTradeBuyActivity;
import com.chinamworld.bocmbci.biz.finc.trade.FincTradeScheduledBuyActivity;
import com.chinamworld.bocmbci.biz.invest.constant.InvestConstant;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * 指令交易详情页
 * 
 * @author fsm
 * 
 */
public class OrcmProductDetailActivity extends FincBaseActivity {
	private String TAG = getClass().getName();
	/**
	 * 基金代码显示文本
	 */
	private TextView fundCodeTextView;
	/**
	 * 基金名字显示文本
	 */
	private TextView fundNameTextView;
	/**
	 * 基金公司显示文本
	 */
	private TextView fundcompanyTextView;
	/**
	 * 币种显示文本
	 */
	private TextView currencyTextView;
	/**
	 * 单位净值显示文本
	 */
	private TextView netvalueTextView;
	/**
	 * 单位净值增长率显示文本
	 */
	private TextView netvalueChTextView;
	/**
	 * 总净值显示文本
	 */
	private TextView totalvalueTextView;
	/**
	 * 每万份基金单位收益
	 */
	private TextView everytenthousendTextView;
	/**
	 * 买入按钮
	 */
	private Button buyBtn;
	/**
	 * 定投安妮
	 */
	private Button inversBtn;
	/**
	 * 当前基金代码
	 */
	private String fundCodeStr;
	private String fundNameStr;
	private String fundCompanyStr;
	private String currencyStr;
	private String cashFlagCode;
	private String netValueStr;
	private String netValueChStr;
	private String totalValueStr;
	private String everyTenThousandStr;

	/** 是否可买入 ,是否可定投，是否可赎回， 是否可定赎*/
	private boolean canBuy, canScheduleBuy, canSell, canScheduleSell;
	private String operationFlag;
	private Map<String, Object> fundDetail;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		inversBtn.setVisibility(View.GONE);
		try {
			initData();
		} catch (Exception e) {
			LogGloble.d(TAG, e.toString());
		}
	}


	/**
	 * 初始化布局
	 * 
	 * @Author xyl
	 */
	private void init() {
		setRightToMainHome();
		View childview = mainInflater.inflate(R.layout.finc_funddetail_activity, null);
		tabcontent.addView(childview);
		findViewById(R.id.finc_attention).setVisibility(View.GONE);
		findViewById(R.id.chart).setVisibility(View.GONE);
		findViewById(R.id.titleDetail).setVisibility(View.VISIBLE);
		setTitle(R.string.finc_ocrm_product);
		fundCodeTextView = (TextView) childview.findViewById(R.id.finc_fundcode);
		fundNameTextView = (TextView) childview.findViewById(R.id.finc_fundname);
		fundcompanyTextView = (TextView) childview.findViewById(R.id.finc_fundcompany);
		currencyTextView = (TextView) childview.findViewById(R.id.finc_fundcurrency_type);
		netvalueTextView = (TextView) childview.findViewById(R.id.finc_netvalue);
		netvalueChTextView = (TextView) childview.findViewById(R.id.finc_netvaluech);
		totalvalueTextView = (TextView) childview.findViewById(R.id.finc_totlevalue);
		everytenthousendTextView = (TextView) childview.findViewById(R.id.finc_everytenthousand);
		buyBtn = (Button) childview.findViewById(R.id.finc_buy);
		inversBtn = (Button) childview.findViewById(R.id.finc_invesment);
		//不需要显示的字段屏蔽了
		findViewById(R.id.finc_ransom_floor_layout).setVisibility(View.GONE);
		findViewById(R.id.finc_daylimit_layout).setVisibility(View.GONE);
		findViewById(R.id.finc_sellLowLimit_colon_layout).setVisibility(View.GONE);
		findViewById(R.id.finc_myfinc_holdQutyLowLimit_layout).setVisibility(View.GONE);
		findViewById(R.id.forex_myfinc_day_toplimit_layout).setVisibility(View.GONE);
		findViewById(R.id.finc_schedubuyLimit_colon_layout).setVisibility(View.GONE);
		findViewById(R.id.fund_detail_layout).setVisibility(View.GONE);//产品属性横条
		findViewById(R.id.finc_fundtype_colon_layout).setVisibility(View.GONE);//产品种类
		findViewById(R.id.finc_fundstate_colon_layout).setVisibility(View.GONE);//产品状态
		findViewById(R.id.finc_myfinc_netPriceDate_layout).setVisibility(View.GONE);//净值截止日期
		findViewById(R.id.finc_fundbuy_attribute).setVisibility(View.GONE);//购买属性横条
		findViewById(R.id.finc_fundfirst_buy_floor_layout).setVisibility(View.GONE);//首次认购下限
		findViewById(R.id.finc_fundadd_buy_floor_layout).setVisibility(View.GONE);//追加认购下限
		findViewById(R.id.finc_ransom_attribute_layout).setVisibility(View.GONE);//赎回属性
		findViewById(R.id.finc_lately_can_ransom_layout).setVisibility(View.GONE);//最近可赎回日期
		
		
		
		
		buyBtn.setOnClickListener(this);
		inversBtn.setOnClickListener(this);
		initRightBtnForMain();
		everytenthousendTextView.setText("-");
		totalvalueTextView.setText("-");
		netvalueChTextView.setText("-");
		netvalueTextView.setText("-");
	}

	/**
	 * 初始化数据
	 * 
	 */
	private void initData() throws Exception{
		if(!StringUtil.isNullOrEmpty(fincControl.OcrmProductDetailMap)){
			fundDetail = (Map<String, Object>)fincControl.OcrmProductDetailMap.get(Finc.FINC_FUNDINFO);
		}
		fundCodeStr = (String) fundDetail.get(Finc.FINC_FUNDCODE);
		fundNameStr = (String) fundDetail.get(Finc.FINC_FUNDNAME);
		currencyStr = (String) fundDetail.get(Finc.FINC_CURRENCY);
		cashFlagCode = (String) fundDetail.get(Finc.FINC_CASHFLAG );
		fundCompanyStr = (String) fundDetail.get(Finc.FINC_FUNDCOMPANYNAME);
		fundCodeTextView.setText(fundCodeStr);
		fundNameTextView.setText(fundNameStr);
//		currencyTextView.setText(LocalData.Currency.get(currencyStr));
		currencyTextView.setText(FincControl.fincCurrencyAndCashFlag(
				currencyStr, cashFlagCode));
		fundcompanyTextView.setText(fundCompanyStr);
		canBuy = StringUtil.parseStrToBoolean((String) fundDetail.get(Finc.CANBUY));
		canScheduleBuy = StringUtil.parseStrToBoolean((String) fundDetail
						.get(Finc.CANSCHEDULEBUY));
		canSell = StringUtil.parseStrToBoolean((String) fundDetail.get(Finc.ISSALE));
		canScheduleSell = StringUtil.parseStrToBoolean((String) fundDetail.get(Finc.ISADDSALE));
		netValueStr = (String) fundDetail.get(Finc.FINC_NETPRICE);
		netvalueTextView.setText(StringUtil.parseStringPattern(netValueStr, 4));

		netValueChStr = (String) fundDetail.get(Finc.FINC_DAYINCOMERATIO);
		totalValueStr = (String) fundDetail.get(Finc.FINC_ADDUPNETVAL);
		everyTenThousandStr = (String) fundDetail.get(Finc.FINC_FUNDINCOMEUNIT);
		netvalueChTextView.setText(StringUtil.valueOf1(netValueChStr));
		totalvalueTextView.setText(StringUtil.parseStringPattern(totalValueStr,4));
		everytenthousendTextView.setText(StringUtil.valueOf1(everyTenThousandStr));
//		TextView everyTenThousandTv = (TextView) findViewById(R.id.finc_everytenthousand_tv);
//		FincControl.setOnShowAllTextListener(this, everyTenThousandTv, fundcompanyTextView, fundNameTextView);
		
		operationFlag = (String) fincControl.OcrmProductDetailMap.get(Finc.I_TRANSTYPE);
		initOperationButton();
	}
	
	private void initOperationButton(){
		if(FincUtils.isStrEquals(operationFlag, "01")){
			buyBtn.setText(getString(R.string.finc_buy));
		}
		if(FincUtils.isStrEquals(operationFlag, "02")){
			buyBtn.setText(getString(R.string.finc_myfinc_button_sell));
		}
		if(FincUtils.isStrEquals(operationFlag, "03")){
			buyBtn.setText(getString(R.string.finc_myfinc_button_throw));
		}
		if(FincUtils.isStrEquals(operationFlag, "04")){
			buyBtn.setText(getString(R.string.finc_myfinc_scheduedsellFound));
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.finc_buy:
			BaseHttpEngine.showProgressDialog();
			requestPsnFundIssueScopeQuery(fundCodeStr);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 检查是否做了风险认证的回调处理
	 * 
	 * @param resultObj
	 */
	public void doCheckRequestPsnFundRiskEvaluationQueryResultCallback(
			Object resultObj) {
		super.doCheckRequestPsnFundRiskEvaluationQueryResultCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		fincControl.fundDetails = fundDetail;
		if(fincControl.ifdorisk){
			Intent intent = null;
			if("01".equals(operationFlag)){ // 买入
				intent = new Intent(this, FincTradeBuyActivity.class);
			}else if("03".equals(operationFlag)){ //定期定额申购
				intent = new Intent(this, FincTradeScheduledBuyActivity.class);
			}
			intent.putExtra(Finc.orcmflag, true);
			startActivityForResult(intent, 1);
		}
	}
	
	@Override
	public void fundIssueScopeQueryCallBack(Object resultObj) {
		super.fundIssueScopeQueryCallBack(resultObj);
		if(StringUtil.isNullOrEmpty(fincControl.fundIssueScopeList)){
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_query_noresult_error));
			return;
		}
//		BaseHttpEngine.dissMissProgressDialog();
//		if (FincUtils.isListContainsStr(fincControl.fundIssueScopeList, fincControl.bankId, "00000")) {
//			if(FincUtils.isStrEquals(operationFlag, "02") || FincUtils.isStrEquals(operationFlag, "04")){
//				if(!isInBalance()){
//					BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_ocrm_cant_sell));
//					return;
//				}
//			}
//			startActivity();
//		} else{
//			BaseDroidApp.getInstanse().showInfoMessageDialog(getErrorInfo());
//		}

		switch (Integer.parseInt(operationFlag)) {
		case 1:
		case 3:
			/**QueryInvtBindingInfo*/
			doCheckRequestAccInfoForOrcm();
			break;
		case 2:
		case 4:
			requestPsnFundQueryFundBalance(fundCodeStr);
			break;
		}

	}
	
	/**
	 * 查询基金账户check
	 */
	public void doCheckRequestAccInfoForOrcm() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_QUERYINVTBINDINGINFO_API);
		biiRequestBody.setConversationId(null);
		Map<String, String> map = new Hashtable<String, String>();
		map.put(Finc.FINC_INVTTYPE_REQ, ConstantGloble.FINC_SERVICECODE);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"doCheckRequestAccInfoForOrcmCallback");
	}

	public void doCheckRequestAccInfoForOrcmCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		Map<String, String> map = (Map<String, String>) biiResponseBodys
				.get(0).getResult();
		if (!StringUtil.isNullOrEmpty(map.get(Finc.FINC_INVESTACCOUNT_RES))) {
			fincControl.accId = map.get(Finc.FINC_ACCOUNTID_RES);
			fincControl.invAccId = map.get(Finc.FINC_INVESTACCOUNT_RES);
			fincControl.accNum = map.get(Finc.FINC_ACCOUNT_RES);
			fincControl.accDetailsMap = map;
			fincControl.ifhaveaccId = true;
			fincControl.fundDetails = fundDetail;
			startActivity();
		}else{
			BaseDroidApp.getInstanse().showInfoMessageDialog(getErrorInfo());
		}
	}
	
	
	public void requestPsnFundQueryFundBalanceCallback(Object resultObj) {
		super.requestPsnFundQueryFundBalanceCallback(resultObj);
		Map<String,Object> resultMap = HttpTools.getResponseResult(resultObj);
		List<Map<String,Object>> array = (List<Map<String,Object>>)resultMap.get("fundBalance");
		fincControl.fundBalancList = array;
		
			if (FincUtils.isListContainsStr(fincControl.fundIssueScopeList, fincControl.bankId, "00000")) {
				if(FincUtils.isStrEquals(operationFlag, "02") || FincUtils.isStrEquals(operationFlag, "04")){
					if(!isInBalance()){
						BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_ocrm_cant_sell));
						return;
					}
				}
				startActivity();
			} else{
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().showInfoMessageDialog(getErrorInfo());
			}
	}
	
	private void startActivity(){
		fincControl.tradeFundDetails = fundDetail;
		Intent intent = null;
		switch (Integer.parseInt(operationFlag)) {
		case 1:
			doCheckRequestPsnFundRiskEvaluationQueryResult();
			break;
		case 2:
			BaseHttpEngine.dissMissProgressDialog();
			intent = new Intent(this, MyFincSellSubmitActivity.class);
			intent.putExtra(Finc.orcmflag, true);
			startActivityForResult(intent, 1);
			break;
		case 3:
			doCheckRequestPsnFundRiskEvaluationQueryResult();
			break;
		case 4:
			BaseHttpEngine.dissMissProgressDialog();
			intent = new Intent(this, MyFincScheduSellActivity.class);
			intent.putExtra(Finc.orcmflag, true);
			startActivityForResult(intent, 1);
			break;
		}
		
	}
	
	private String getErrorInfo(){
		String resInfo = getString(R.string.finc_ocrm_operation_error_info);
		String areaString = "";
		CharSequence cs = "";
		switch (Integer.parseInt(operationFlag)) {
		case 1:
			cs = getString(R.string.finc_buy);
			break;
		case 2:
			cs = getString(R.string.finc_myfinc_button_sell);
			break;
		case 3:
			cs = getString(R.string.finc_myfinc_button_throw);
			break;
		case 4:
			cs = getString(R.string.finc_myfinc_scheduedsellFound);
			break;
		default:
			break;
		}
		resInfo = resInfo.replace("X", cs);
		areaString = getAreaCompoundStr();
		resInfo = resInfo.replace("Y", areaString);
		return resInfo;
	}
	
	/**
	 * 组合基金发行地区，结果以字符串形式反回，地区间以顿号隔开
	 * @return 
	 */
	private String getAreaCompoundStr(){
		StringBuffer stringBuffer = new StringBuffer();
		for(Map<String, String> map : fincControl.fundIssueScopeList){
			stringBuffer.append(LocalData.FincBankIDMap.get(map.get(Finc.BRANCH)));
			stringBuffer.append("、");
		}
		stringBuffer.deleteCharAt(stringBuffer.length() - 1);
		return stringBuffer.toString();
	}
	
	/**
	 * 判断推荐基金是否在持仓内
	 * @return
	 */
	private boolean isInBalance(){
		if(StringUtil.isNullOrEmpty(fincControl.fundBalancList)){
			return false;
		}
		for(Map<String, Object> map : fincControl.fundBalancList){
			if(FincUtils.isStrEquals(fundCodeStr, (String)map.get(Finc.I_FUNDCODE))){
				fundDetail.put(Finc.FINC_TOTALAVAILABLEBALANCE,
						map.get(Finc.FINC_TOTALAVAILABLEBALANCE));
				fundDetail.put(Finc.FINC_CURRENTCAPITALISATION_REQ,
						map.get(Finc.FINC_CURRENTCAPITALISATION_REQ));
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			if(requestCode == InvestConstant.FUNDRISK){
				Intent intent = null;
				if("01".equals(operationFlag)){ // 买入
					intent = new Intent(this, FincTradeBuyActivity.class);
				}else if("03".equals(operationFlag)){ //定期定额申购
					intent = new Intent(this, FincTradeScheduledBuyActivity.class);
				}
				intent.putExtra(Finc.orcmflag, true);
				startActivityForResult(intent, 1);
				break;
			}
			
			setResult(RESULT_OK);
			finish();
			break;

		default:
			if(requestCode == InvestConstant.FUNDRISK ){// 基金风险评估
				fincControl.ifdorisk = false;
				getPopupForRisk();
				break;
			}
			break;
		}
	}
}
