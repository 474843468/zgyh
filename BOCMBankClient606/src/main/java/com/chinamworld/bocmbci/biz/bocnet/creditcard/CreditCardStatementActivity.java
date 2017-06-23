package com.chinamworld.bocmbci.biz.bocnet.creditcard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Bocnet;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.bocnet.BocnetBaseActivity;
import com.chinamworld.bocmbci.biz.bocnet.BocnetDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 已出账单查询详情页面 双币种页面----显示币种详细信息
 */
public class CreditCardStatementActivity extends BocnetBaseActivity{
	private TextView finc_accNumber, finc_fenqidate, finc_miaoshus, finc_validatime, finc_remiannomoney,
			finc_zhangdanriqi, finc_daoqiday;
	/** 当期应还金额 */
	private TextView finc_nextdate;
	/** 当期最低还款额 */
	private TextView finc_huanluanmoney;
	/** 上前账面余额标记 */
	private TextView finc_miaoshus_tag = null;
	/** 当前应还金额标记 */
	private TextView finc_nextdate_tag = null;
	int black;
	int gray;
	int white;
	int red;

	private Button btn_renminbi, btn_waibi;

	private Button detailButton;
	/** 银行卡类型 */
	public static String cardType;
	/** 当前币种名称 */
	public static String currentStrCurrency;
	/** 已出账单查询结果 */
	private Map<String, Object> transResult = null;
	/** 账户信息 */
	private List<Map<String, String>> crcdAccountInfoList = null;
	/** map1 */
	private Map<String, String> firstMap = null;
	/** map1--code */
	private String firstCode = null;
	/** map2 */
	private Map<String, String> secondMap = null;
	/** map2--code */
	private String secondCode = null;
	/** 当前币种 */
	private String currencyCode = null;
	/** 卡号 */
	private String cardNo = null;
	/** 当前应还金额 */
	private String periodAvailbleCreditLimit = null;
	/** 当期最低还款额 */
	private String lowestRepayAmount = null;
	/** 信用卡账户标识 */
	private String creditcardId = null;
	/** 已出账单月份 */
	private String statementMonth = null;
	/** 当前应还款金额标记 */
	private String periodAvailbleCreditLimitflag = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateLayout(R.layout.crcd_trans_details);
		setTitle(this.getString(R.string.bocnet_bill_query));
		String numMonth = getIntent().getStringExtra(Crcd.CRCD_STATEMENTMONTHN_REQ);
		if(!StringUtil.isNullOrEmpty(numMonth)&&numMonth.length()>=7){
			setTitle(formatStr(numMonth));	
		}	
		if(!initResource()){return;}
		init();
	}
//	2014/03
	private String formatStr(String numMonth) {
		
		// 年
		String year = numMonth.substring(0, 4);
		// 月
		String month = numMonth.substring(5, 7);
		if("0".equals(month.substring(0,1))){
			month=month.substring(1, 2);	
		}
		 return  year+"年"+month + "月" + "账单" ;
	}
	/**
	 * 初始化所需数据  true:初始化成功   false:初始化不成功
	 * @return
	 */
	private boolean initResource(){
		black = this.getResources().getColor(R.color.black);
		gray = this.getResources().getColor(R.color.gray);
		white = this.getResources().getColor(R.color.white);
		red = this.getResources().getColor(R.color.red);
		transResult = BocnetDataCenter.getInstance().getCrcdStatement();
		if(StringUtil.isNullOrEmpty(transResult)){
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.acc_transferquery_null));
			return false;
		}
		crcdAccountInfoList = (List<Map<String, String>>) transResult.get(Bocnet.CRCDACCOUNTINFOLIST);
		if(StringUtil.isNullOrEmpty(crcdAccountInfoList)){
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.acc_transferquery_null));
			return false;
		}
		firstMap = crcdAccountInfoList.get(0);
		if(StringUtil.isNullOrEmpty(firstMap)){
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.acc_transferquery_null));
			return false;
		}
		firstCode = firstMap.get(Bocnet.ACNTTYPE);
		
		if (crcdAccountInfoList.size() > 1) {
			secondMap = crcdAccountInfoList.get(1);
			secondCode = secondMap.get(Bocnet.ACNTTYPE);
			if (StringUtil.isNullOrEmpty(secondMap) || StringUtil.isNull(secondCode)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.acc_transferquery_null));
				return false;
			}
		}
		
		Map<String, String> crcdCustomerInfo = (Map<String, String>) transResult.get(Bocnet.CRCDCUSTOMERINFO);
		if (StringUtil.isNullOrEmpty(crcdCustomerInfo)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.acc_transferquery_null));
			return false;
		} else {
			cardNo = crcdCustomerInfo.get(Bocnet.CARDNO);
			creditcardId = crcdCustomerInfo.get(Bocnet.CREDITCARDID);
			statementMonth = (String) crcdCustomerInfo.get(Bocnet.BILLDATE);
		}
		return true;
	}

	public void init() {
		finc_accNumber = (TextView)findViewById(R.id.finc_accNumber);
		finc_fenqidate = (TextView) findViewById(R.id.finc_fenqidate);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_fenqidate);
		finc_miaoshus = (TextView) findViewById(R.id.finc_miaoshus);
		finc_miaoshus_tag = (TextView) findViewById(R.id.finc_miaoshus_tag);
		finc_miaoshus_tag.setVisibility(View.GONE);
		finc_validatime = (TextView) findViewById(R.id.finc_validatime);
		finc_remiannomoney = (TextView) findViewById(R.id.finc_remiannomoney);
		finc_nextdate = (TextView) findViewById(R.id.finc_nextdate);
		finc_nextdate_tag = (TextView) findViewById(R.id.finc_nextdate_tag);
		finc_huanluanmoney = (TextView) findViewById(R.id.finc_huanluanmoney);
		finc_zhangdanriqi = (TextView) findViewById(R.id.finc_zhangdanriqi);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_zhangdanriqi);
		finc_daoqiday = (TextView) findViewById(R.id.finc_daoqiday);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_daoqiday);
		// 账单明细
		detailButton = (Button) findViewById(R.id.fuButton);
		detailButton.setVisibility(View.VISIBLE);
		btn_renminbi = (Button) findViewById(R.id.btn_renminbi);
		btn_waibi = (Button) findViewById(R.id.btn_waibi);
		setViewListeners();
		isShowOrNot();
	}
	
	private void setViewListeners(){
		detailButton.setOnClickListener(this);
		btn_renminbi.setOnClickListener(renminbiClick);
		btn_waibi.setOnClickListener(waibiClick);
	}

	/** 根据账户信息CrcdAccountInfoList的长度显示顶部按钮 */
	private void isShowOrNot() {
		if (crcdAccountInfoList.size() == 1) {
			// 只有一个币种
			currencyCode = firstCode;
			btn_renminbi.setVisibility(View.VISIBLE);
			btn_waibi.setVisibility(View.INVISIBLE);
			String strCurrency1 = LocalData.Currency.get(firstCode);
			btn_renminbi.setText(strCurrency1);
			setFirstCodeValue(firstMap, currencyCode);
		} else if (crcdAccountInfoList.size() > 1) {
			// 返回两个币种
			btn_renminbi.setVisibility(View.VISIBLE);
			btn_waibi.setVisibility(View.VISIBLE);
			String strCurrency1 = LocalData.Currency.get(firstCode);
			String strCurrency2 = LocalData.Currency.get(secondCode);
			if (LocalData.rmbCodeList.contains(firstCode)) {
				// 第一币种是人民币
				currencyCode = firstCode;
				btn_renminbi.setText(strCurrency1);
				btn_waibi.setText(strCurrency2);
				setFirstCodeValue(firstMap, firstCode);
			} else if (LocalData.rmbCodeList.contains(secondCode)) {
				// 第二币种是人民币
				currencyCode = secondCode;
				btn_renminbi.setText(strCurrency2);
				btn_waibi.setText(strCurrency1);
				setFirstCodeValue(secondMap, secondCode);
			}

		}
	}

	/** 点击人民币监听事件 */
	OnClickListener renminbiClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			btn_renminbi.setBackgroundResource(R.drawable.btn_tranoselect);
			btn_renminbi.setTextColor(black);
			btn_waibi.setBackgroundResource(R.drawable.btn_transelect);
			btn_waibi.setTextColor(white);
			if (crcdAccountInfoList.size() == 1) {
				currencyCode = firstCode;
				setFirstCodeValue(firstMap, firstCode);
			} else if (crcdAccountInfoList.size() > 1) {
				if (LocalData.rmbCodeList.contains(firstCode)) {
					// 人民币是第一币种
					currencyCode = firstCode;
					setFirstCodeValue(firstMap, firstCode);
				} else {
					// 人民币是第二币种
					currencyCode = secondCode;
					setFirstCodeValue(secondMap, secondCode);
				}
			}
		}
	};
	/** 点击外币监听事件 */
	OnClickListener waibiClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			btn_renminbi.setBackgroundResource(R.drawable.btn_transelect);
			btn_renminbi.setTextColor(white);
			btn_waibi.setBackgroundResource(R.drawable.btn_tranoselect);
			btn_waibi.setTextColor(black);
			if (LocalData.rmbCodeList.contains(firstCode)) {
				// 外是第二币种
				currencyCode = secondCode;
				setFirstCodeValue(secondMap, secondCode);
			} else {
				// 外是第一币种
				currencyCode = firstCode;
				setFirstCodeValue(firstMap, firstCode);
			}
		}
	};

	/** 为控件赋值 */
	private void setFirstCodeValue(Map<String, String> map, String code) {
		// 账号
		finc_accNumber.setText(StringUtil.getForSixForString(cardNo));
		// 币种
		finc_fenqidate.setText(LocalData.Currency.get(code));
		// 上前账面余额
		String lastTermAcntBal = map.get(Crcd.CRCD_LASTTERMACNTBAL_RES);
		String lastTermAcntBals = null;
		if (StringUtil.isNull(lastTermAcntBal)) {
			lastTermAcntBals = "-";
		} else {
			lastTermAcntBals = StringUtil.parseStringCodePattern(code, lastTermAcntBal, 2);
		}
		// 上前账面余额
		finc_miaoshus.setText(lastTermAcntBals);
		String lastTermAcntBalflag = map.get(Crcd.CRCD_LASTTERMACNTBALFLAG_RES);
		if (!StringUtil.isNull(lastTermAcntBalflag)) {
			if (ConstantGloble.CRCD_SEARCH_ZERO.equals(lastTermAcntBalflag)) {
				finc_miaoshus_tag.setVisibility(View.VISIBLE);
				finc_miaoshus_tag.setText(ConstantGloble.TOUZHI);
			} else if (ConstantGloble.CRCD_SEARCH_ONE.equals(lastTermAcntBalflag)) {
				finc_miaoshus_tag.setVisibility(View.VISIBLE);
				finc_miaoshus_tag.setText(ConstantGloble.JIEYU);
			} else if (ConstantGloble.CRCD_SEARCH_TWO.equals(lastTermAcntBalflag)) {
				finc_miaoshus_tag.setVisibility(View.GONE);
			}
		}
		// 支出总计
		String totalExpend = map.get(Crcd.CRCD_TOTALEXPEND_RES);
		String totalExpends = null;
		if (StringUtil.isNull(totalExpend)) {
			totalExpends = "-";
		} else {
			totalExpends = StringUtil.parseStringCodePattern(code, totalExpend, 2);
		}
		finc_validatime.setText(totalExpends);
		// 收入总计
		String totalDeposit = map.get(Crcd.CRCD_TOTALDEPOSIT_RES);
		String totalDeposits = null;
		if (StringUtil.isNull(totalDeposit)) {
			totalDeposits = "-";
		} else {
			totalDeposits = StringUtil.parseStringCodePattern(code, totalDeposit, 2);
		}
		finc_remiannomoney.setText(totalDeposits);
		// 当期应还金额
		periodAvailbleCreditLimit = map.get(Crcd.CRCD_PERIODAVAILBLECRCDITLIMIT_RES);
		String periodAvailbleCreditLimits = null;
		if (StringUtil.isNull(periodAvailbleCreditLimit)) {
			periodAvailbleCreditLimits = "-";
		} else {
			periodAvailbleCreditLimits = StringUtil.parseStringCodePattern(code, periodAvailbleCreditLimit, 2);
		}
		finc_nextdate.setText(periodAvailbleCreditLimits);
		periodAvailbleCreditLimitflag = map.get(Crcd.CRCD_PERIODAVAILBLECRCDITLIMITFLAG_RES);
		if (!StringUtil.isNull(periodAvailbleCreditLimitflag)) {
			if (ConstantGloble.CRCD_SEARCH_ZERO.equals(periodAvailbleCreditLimitflag)) {
				finc_nextdate_tag.setText(ConstantGloble.TOUZHI);
			} else if (ConstantGloble.CRCD_SEARCH_ONE.equals(periodAvailbleCreditLimitflag)) {
				finc_nextdate_tag.setText(ConstantGloble.JIEYU);
			} else if (ConstantGloble.CRCD_SEARCH_TWO.equals(periodAvailbleCreditLimitflag)) {
				finc_nextdate_tag.setVisibility(View.GONE);
			}
		}

		// 当期最低还款额
		lowestRepayAmount = map.get(Crcd.CRCD_LOWESTREPAYAMOUNT_RES);
		String lowestRepayAmounts = null;
		if (StringUtil.isNull(lowestRepayAmount)) {
			lowestRepayAmounts = "-";
		} else {
			lowestRepayAmounts = StringUtil.parseStringCodePattern(code, lowestRepayAmount, 2);
		}
		finc_huanluanmoney.setText(lowestRepayAmounts);
		// 账单日期
		String billDt = map.get(Crcd.CRCD_BILLDT_RES);
		finc_zhangdanriqi.setText(billDt);
		// 到期还款日
		String reapyDt = map.get(Crcd.CRCD_REAPYDT_RES);
		finc_daoqiday.setText(reapyDt);
	}

	@Override
	public void onClick(View v) {
//		super.onClick(v);
		switch (v.getId()) {
		case R.id.ib_back:
			setResult(RESULT_CANCELED);
			finish();
			break;
		case R.id.fuButton:// 账单明细
			if (StringUtil.isNull(creditcardId) || StringUtil.isNull(statementMonth) || StringUtil.isNull(currencyCode)) {
				return;
			}
			BaseHttpEngine.showProgressDialog();
			requestPsnCrcdQueryBilledTransDetail(creditcardId, statementMonth, currencyCode,
					ConstantGloble.CRCD_PAGENO, null, ConstantGloble.FOREX_PAGESIZE);
			break;
		}

	}

	/** 查询信用卡已出账单交易明细 */
	private void requestPsnCrcdQueryBilledTransDetail(String creditcardId, String statementMonth, String accountType,
			String pageNo, String primary, String lineNum) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bocnet.METHODACCBOCNETQRYCRCDSTATEMENTDETAIL);
		biiRequestBody.setConversationId(BocnetDataCenter.getInstance().getConversationId());
		Map<String, String> map = new HashMap<String, String>();
		map.put(Crcd.CRCD_CREDITCARDID_REQ, creditcardId);
		map.put(Crcd.CRCD_STATEMENTMONTHN_REQ, statementMonth);
		map.put(Crcd.CRCD_ACCOUNTTYPR_REQ, accountType);
		map.put(Crcd.CRCD_PAGENO_REQ, pageNo);
		map.put(Crcd.CRCD_PRIMARY_REQ, primary);
		map.put(Crcd.CRCD_LINENUM_REQ, lineNum);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnCrcdQueryBilledTransDetailCallBack");
	}

	/** 查询信用卡已出账单交易明细-----回调 */
	public void requestPsnCrcdQueryBilledTransDetailCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		BocnetDataCenter.getInstance().setCrcdStatementDetail(result);
		if (StringUtil.isNullOrEmpty(result)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_query_noresult_error));
			return;
		}
		List<Map<String, String>> transList = (List<Map<String, String>>) result.get(Bocnet.TRANSLIST);
		if (StringUtil.isNullOrEmpty(transList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		Intent intent = new Intent(this, CreditCardStatementDetailActivity.class);
		intent.putExtra(Crcd.CRCD_CREDITCARDID_REQ, creditcardId);
		intent.putExtra(Crcd.CRCD_STATEMENTMONTHN_REQ, statementMonth);
		intent.putExtra(Crcd.CRCD_ACCOUNTTYPR_REQ, currencyCode);
		intent.putExtra(Crcd.CRCD_PERIODAVAILBLECRCDITLIMIT_RES, periodAvailbleCreditLimit);
		intent.putExtra(Crcd.CRCD_LOWESTREPAYAMOUNT_RES, lowestRepayAmount);
		intent.putExtra(Crcd.CRCD_CADRNO_RES, cardNo);
		intent.putExtra(Crcd.CRCD_PERIODAVAILBLECRCDITLIMITFLAG_RES, periodAvailbleCreditLimitflag);
		startActivity(intent);
	}


	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// 账单明细
		requestPsnCrcdQueryBilledTransDetail(creditcardId, statementMonth, currencyCode,
				ConstantGloble.CRCD_PAGENO, null, ConstantGloble.FOREX_PAGESIZE);

	}

}
