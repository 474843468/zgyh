package com.chinamworld.bocmbci.biz.crcd.mycrcd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.mytransfer.currenttran.TransferManagerActivity1;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.BottomButtonUtils;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 已出账单查询详情页面 双币种页面----显示币种详细信息
 */
public class CrcdTransDetailActivity extends CrcdBaseActivity implements OnClickListener {
	private static final String TAG = "CrcdTransDetailActivity";
	private View view;

	private TextView finc_accNumber, finc_fenqidate, finc_miaoshus, finc_validatime, finc_remiannomoney,
			finc_zhangdanriqi, finc_daoqiday;
	/** 当期应还金额 */
	private TextView finc_nextdate;
	/** 当期最低还款额 */
	private TextView finc_huanluanmoney;
	RadioButton btn_renmibi, btn_meiyuan;
	/** 上前账面余额标记 */
	private TextView finc_miaoshus_tag = null;
	/** 当前应还金额标记 */
	private TextView finc_nextdate_tag = null;
	int black;
	int gray;
	int white;
	int red;

	LinearLayout ll_btnswitch;
	Button btn_renminbi, btn_waibi;

	Button detailButton, zhuanButton,gohuiButton, moreButton;
	/** 银行卡类型 */
	public static String cardType;
	/** 当前币种名称 */
	public static String currentStrCurrency;
	/*** 信用卡详情数据 */
	private Map<String, Object> detailReturnMap = null;
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
	/** 是否是当前月份 */
	private boolean isLastMonth = false;
	private boolean isBillExist = false;
	
	/** 当前币种 */
	private String currencyCode = null;
	/** 卡号 */
	private String cardNo = null;
	/** 1-信用卡还款，2-信用卡购汇还款 ，3-信用卡还款统一*/
	private int tag = 1;
	private String accountId = null;
	private String accountType=null;
	
	private String accountIbkNum = null;
	private String nickName = null;
	// 购汇还款账户币种
	private String currType = null;
	
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
	/** 1-第二次查询账单明细，2-购汇还款 ,3- */
	private int ttan = 1;
	/** 是否是第一次请求账单明细 */
	private boolean isFirstRequestDetail = true;
	private String numMonth = null;
	private String haveNotRepayAmout = null; // 本期未还款金额
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.card_account_search));
		view = addView(R.layout.crcd_trans_details);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		black = this.getResources().getColor(R.color.black);
		gray = this.getResources().getColor(R.color.gray);
		white = this.getResources().getColor(R.color.white);
		red = this.getResources().getColor(R.color.red);
		transResult = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CRCD_YCTRANS_RESULT);
		isLastMonth = getIntent().getBooleanExtra(ConstantGloble.CRCD_ISLASTMONTH, false);
		isBillExist= getIntent().getBooleanExtra(Crcd.CRCD_ISBILLEXIST_REQ, false);
		accountId = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
		accountType=getIntent().getStringExtra(Crcd.CRCD_ACCOUNTTYPE_RES);
		accountIbkNum = getIntent().getStringExtra(Tran.ACCOUNTIBKNUM_RES);
		nickName=getIntent().getStringExtra(Crcd.CRCD_NICKNAME_RES);
		numMonth = getIntent().getStringExtra(Crcd.CRCD_STATEMENTMONTHN_REQ);
		if(!StringUtil.isNullOrEmpty(numMonth)&&numMonth.length()>=7){
			setTitle(formatStr(numMonth));	
		}
		
		if (StringUtil.isNullOrEmpty(transResult)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.acc_transferquery_null));
			return;
		}
		crcdAccountInfoList = (List<Map<String, String>>) transResult.get(Crcd.CRCD_CRCDACCOUNTINFOLIST_RES);
		if (crcdAccountInfoList == null || crcdAccountInfoList.size() <= 0) {
			return;
		}
		// statementMonth = (String) transResult.get(Crcd.CRCD_BILLDATE_RES);
		firstMap = crcdAccountInfoList.get(0);
		firstCode = firstMap.get(Crcd.CRCD_ACNTTYPE_RES);
		if(firstCode.equals("CNY")){
			firstCode="001";
		}
		if (StringUtil.isNullOrEmpty(firstMap) || StringUtil.isNull(firstCode)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.acc_transferquery_null));
			return;
		}
		if (crcdAccountInfoList.size() > 1) {
			secondMap = crcdAccountInfoList.get(1);
			secondCode = secondMap.get(Crcd.CRCD_ACNTTYPE_RES);
			if (StringUtil.isNullOrEmpty(secondMap) || StringUtil.isNull(secondCode)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.acc_transferquery_null));
				return;
			}
		}
		Map<String, String> crcdCustomerInfo = (Map<String, String>) transResult.get(Crcd.CRCD_CRCDCUSTOMERINFO_RES);
		if (StringUtil.isNullOrEmpty(crcdCustomerInfo)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.acc_transferquery_null));
			return;
		} else {
			cardNo = crcdCustomerInfo.get(Crcd.CRCD_CADRNO_RES);
			creditcardId = crcdCustomerInfo.get(Crcd.CRCD_CREDITCARDID_RES);
			statementMonth = (String) crcdCustomerInfo.get(Crcd.CRCD_BILLDATE_RES);
		}
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

	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};

	public void init() {
		finc_accNumber = (TextView) view.findViewById(R.id.finc_accNumber);
		finc_fenqidate = (TextView) view.findViewById(R.id.finc_fenqidate);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_fenqidate);
		finc_miaoshus = (TextView) view.findViewById(R.id.finc_miaoshus);
		finc_miaoshus_tag = (TextView) view.findViewById(R.id.finc_miaoshus_tag);
		finc_miaoshus_tag.setVisibility(View.GONE);
		finc_validatime = (TextView) view.findViewById(R.id.finc_validatime);
		finc_remiannomoney = (TextView) view.findViewById(R.id.finc_remiannomoney);
		finc_nextdate = (TextView) view.findViewById(R.id.finc_nextdate);
		finc_nextdate_tag = (TextView) view.findViewById(R.id.finc_nextdate_tag);
		finc_huanluanmoney = (TextView) view.findViewById(R.id.finc_huanluanmoney);
		finc_zhangdanriqi = (TextView) view.findViewById(R.id.finc_zhangdanriqi);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_zhangdanriqi);
		finc_daoqiday = (TextView) view.findViewById(R.id.finc_daoqiday);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_daoqiday);
		// 账单明细
		detailButton = (Button) view.findViewById(R.id.fuButton);
		// 转账还款
		zhuanButton = (Button) view.findViewById(R.id.zhuanButton);
		zhuanButton.setOnClickListener(this);
		// 购回还款
		gohuiButton = (Button) view.findViewById(R.id.gohuiButton);
		// 更多
		moreButton = (Button) view.findViewById(R.id.moreButton);
		detailButton.setOnClickListener(this);
		gohuiButton.setOnClickListener(this);
		// 更多按钮
		String zdFenQiButton = getString(R.string.mycrcd_account_divide);
		String hkButton = getString(R.string.mycrcd_credit_huan);
		String[] selectors = new String[] { zdFenQiButton, hkButton };
		PopupWindowUtils.getInstance().setshowMoreChooseUpListener(CrcdTransDetailActivity.this, moreButton, selectors,
				moreButtonClickListener);
		ll_btnswitch = (LinearLayout) findViewById(R.id.ll_btnswitch);
		btn_renminbi = (Button) findViewById(R.id.btn_renminbi);
		btn_waibi = (Button) findViewById(R.id.btn_waibi);
		isShowOrNot();
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
			codeDecideButton(currencyCode);
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
				codeDecideButton(currencyCode);
				setFirstCodeValue(firstMap, firstCode);
			} else if (LocalData.rmbCodeList.contains(secondCode)) {
				// 第二币种是人民币
				currencyCode = secondCode;
				btn_renminbi.setText(strCurrency2);
				btn_waibi.setText(strCurrency1);
				codeDecideButton(currencyCode);
				setFirstCodeValue(secondMap, secondCode);
			}

		}
	}

	/**
	 * 根据币种代码显示底部按钮 所有的底部按钮均在当前月份显示，其余月份不显示
	 */
	private void codeDecideButton(String code) {
		if (isLastMonth&&isBillExist) {
			if (ConstantGloble.FOREX_RMB_TAG1.equals(code) || ConstantGloble.FOREX_RMB_CNA_TAG2.equals(code)) {
				// 币种1为人民币
				// 人民币显示---账单分期、信用卡还款、账单明细
				
				// sunh 账单明细 转账还款
				moreButton.setVisibility(View.GONE);
				zhuanButton.setVisibility(View.VISIBLE);
				gohuiButton.setVisibility(View.GONE);
				detailButton.setVisibility(View.VISIBLE);
			} else {
				// 币种1为外币
				zhuanButton.setVisibility(View.VISIBLE);
				gohuiButton.setVisibility(View.GONE);
				detailButton.setVisibility(View.VISIBLE);
				moreButton.setVisibility(View.GONE);
			}
		} else {
			zhuanButton.setVisibility(View.GONE);
			gohuiButton.setVisibility(View.GONE);
			detailButton.setVisibility(View.VISIBLE);
			moreButton.setVisibility(View.GONE);
			BottomButtonUtils.setSingleLineStyleRed(detailButton);
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
				codeDecideButton(currencyCode);
				setFirstCodeValue(firstMap, firstCode);
			} else if (crcdAccountInfoList.size() > 1) {
				if (LocalData.rmbCodeList.contains(firstCode)) {
					// 人民币是第一币种
					currencyCode = firstCode;
					codeDecideButton(currencyCode);
					setFirstCodeValue(firstMap, firstCode);
				} else {
					// 人民币是第二币种
					currencyCode = secondCode;
					codeDecideButton(currencyCode);
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
				codeDecideButton(currencyCode);
				setFirstCodeValue(secondMap, secondCode);
			} else {
				// 外是第一币种
				currencyCode = firstCode;
				codeDecideButton(currencyCode);
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

	/** 账单分期输入 */
	public void psnCrcdDividedPayBillSetInput(String accountId, String currency) {
		// 通讯开始,展示通讯框
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDCIVIDEDPAYBILLSETINPUT_API);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		map.put(Crcd.CRCD_CURRENCYCODE, currency);// 人民币代码
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "PsnCrcdDividedPayBillSetInputCallBack");
	}

	/** 账单分期输入-----返回结果 */
	private Map<String, Object> returnMap;
	/** 可分期金额上限 */
	public static String upMoney;
	/** 可分期金额下限 */
	public static String lowMoney;

	/** 账单分期输入------回调 */
	public void PsnCrcdDividedPayBillSetInputCallBack(Object resultObj) {
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		returnMap = (Map<String, Object>) biiResponseBody.getResult();
		upMoney = String.valueOf(returnMap.get(Crcd.CRCD_UPINSTMTAMOUNT));
		lowMoney = String.valueOf(returnMap.get(Crcd.CRCD_LOWINSTMTAMOUNT));

		// 账单分期
		Intent it = new Intent(CrcdTransDetailActivity.this, CrcdAccountDividedActivity.class);
		it.putExtra("currenncy", currency);
		it.putExtra(Crcd.CRCD_ACCOUNTNUMBER_RES, MyCrcdDetailActivity.bankNumber);
		startActivity(it);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fuButton:// 账单明细
			if (StringUtil.isNull(creditcardId) || StringUtil.isNull(statementMonth) || StringUtil.isNull(currencyCode)) {
				return;
			}
			BaseHttpEngine.showProgressDialog();
			if (!isFirstRequestDetail) {
				ttan = 1;
				requestCommConversationId();
			} else {
				isFirstRequestDetail = false;
				requestPsnCrcdQueryBilledTransDetail(creditcardId, statementMonth, currencyCode,
						ConstantGloble.CRCD_PAGENO, null, ConstantGloble.FOREX_PAGESIZE);
			}

			break;
		case R.id.gohuiButton:// 购汇还款
			tag = 2;
			ttan = 2;
			if (StringUtil.isNull(periodAvailbleCreditLimitflag)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.mycrcd_waibi_nojieqian));
				return;
			}
			if (ConstantGloble.CRCD_SEARCH_ZERO.equals(periodAvailbleCreditLimitflag)) {
				// “0”-欠款
				// 当期应还金额----- 判断账面余额是否>0
				requestPsnCrcdQueryAccountDetail(accountId, currencyCode);
			} else {
				// “1”-存款 “2”-余额0
				BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.mycrcd_waibi_nojieqian));
				return;
			}
			break;
			
		case R.id.zhuanButton:// 转账还款 sunh
			tag = 3;
			ttan = 3;
			requestPsnCrcdQueryGeneralInfo();
			
			break;
		}

	}

	
	

	/** 信用卡综合信息查询 */
	private void requestPsnCrcdQueryGeneralInfo() {
		
		
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDQUERYGENERALINFO);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnCrcdQueryGeneralInfoCallBack");

	}
	@SuppressWarnings("unchecked")
	public void requestPsnCrcdQueryGeneralInfoCallBack(Object resultObj) {
		Map<String, Object> resultmap = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(resultmap)) {
			BiiHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_transferquery_null));
			return;
		}
		// 信用卡详情，跳转到转账页面
		resultmap.put(Crcd.CRCD_ACCOUNTTYPE_RES, accountType);
		resultmap.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		resultmap.put(Tran.ACCOUNTIBKNUM_RES, accountIbkNum);
		resultmap.put(Crcd.CRCD_NICKNAME_RES, nickName);
		
		TranDataCenter.getInstance().setAccInInfoMap(resultmap);
		TranDataCenter.getInstance().setRelCrcdBuyCallBackMap(null);
		List<Map<String, Object>> actlist = (List<Map<String, Object>>) resultmap.get(Crcd.CRCD_ACTLIST);
		
		for (int i = 0; i < actlist.size(); i++) {

			if (!"001".equals(String.valueOf(actlist.get(i).get(
					Crcd.CRCD_CURRENCY_RES)))) {
				// 得到外币 账单信息
				haveNotRepayAmout = (String) actlist.get(i).get(
						Crcd.CRCD_HAVENOTREPAYAMOUT); // 本期未还款金额
				currType = (String) actlist.get(i).get(Crcd.CRCD_CURRENCY); // 本期未还款金额
			}
		}
		if (!StringUtil.isNull(haveNotRepayAmout)
				&& Double.parseDouble(haveNotRepayAmout) > 0) {
			ttan =3;
			// 外币有欠款 请求购汇信息
			BaseHttpEngine.showProgressDialog();
			requestCommConversationId();
		} else {
			// 无欠款 直接到转账界面
			Intent intent = new Intent(CrcdTransDetailActivity.this,
					TransferManagerActivity1.class);
			intent.putExtra(ConstantGloble.JUMP_TO_TRAN_FLAG,
					ConstantGloble.CREDIT_TO_TRAN);
			startActivity(intent);
		}
		

		
		
	
		
	}
	
	
	/** 查询信用卡已出账单交易明细 */
	private void requestPsnCrcdQueryBilledTransDetail(String creditcardId, String statementMonth, String accountType,
			String pageNo, String primary, String lineNum) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDQUERYBIIEDTRANSNDETAIL_API);
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
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
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		List<Map<String, String>> transList = (List<Map<String, String>>) result.get(Crcd.CRCD_TRANSLIST_RES);
		if (transList == null || transList.size() <= 0) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.CRCD_TRANSMAP, result);
		Intent intent = new Intent(this, CrcdQueryBilledTransDetailActivity.class);
		intent.putExtra(Crcd.CRCD_CREDITCARDID_REQ, creditcardId);
		intent.putExtra(Crcd.CRCD_STATEMENTMONTHN_REQ, statementMonth);
		intent.putExtra(Crcd.CRCD_ACCOUNTTYPR_REQ, currencyCode);
		intent.putExtra(Crcd.CRCD_PERIODAVAILBLECRCDITLIMIT_RES, periodAvailbleCreditLimit);
		intent.putExtra(Crcd.CRCD_LOWESTREPAYAMOUNT_RES, lowestRepayAmount);
		intent.putExtra(Crcd.CRCD_CADRNO_RES, cardNo);
		intent.putExtra(Crcd.CRCD_PERIODAVAILBLECRCDITLIMITFLAG_RES, periodAvailbleCreditLimitflag);
		startActivity(intent);
		BaseHttpEngine.dissMissProgressDialog();
	}

	/** 更多按钮监听事件 */
	private OnClickListener moreButtonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Integer tags = (Integer) v.getTag();
			switch (tags) {
			case 0:// 账单分期
					// 办理账单分期输入
				psnCrcdDividedPayBillSetInput(accountId, currencyCode);
				break;
			case 1:// 信用卡还款
				tag = 1;
				requestPsnCrcdQueryAccountDetail(accountId, currencyCode);
				break;

			}
		}
	};

	/** 查询信用卡详情 */
	private void requestPsnCrcdQueryAccountDetail(String accountId, String currency) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_ACCOUNTDETAIL_API);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		map.put(Crcd.CRCD_CURRENCY, currency);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "PsnCrcdQueryAccountDetailCallBack");
	}

	public void PsnCrcdQueryAccountDetailCallBack(Object resultObj) {
		// 通讯结束,关闭通讯框
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		detailReturnMap = (Map<String, Object>) biiResponseBody.getResult();
		// 信用卡详情，跳转到转账页面
		if (tag == 1) {
			// 信用卡还款
			Intent it = new Intent(CrcdTransDetailActivity.this, TransferManagerActivity1.class);
			it.putExtra(ConstantGloble.TRANS_ACCOUNT_TYPE, Crcd.TRANS_CREDIT);
			TranDataCenter.getInstance().setCurrInDetail(detailReturnMap);// 信用卡详情，跳转到转账页面
			Crcd.tranState = Crcd.TRANS_IN;
			TranDataCenter.getInstance().setModuleType(ConstantGloble.CRCE_TYPE);
			BaseHttpEngine.dissMissProgressDialog();
			it.putExtra(ConstantGloble.JUMP_TO_TRAN_FLAG, ConstantGloble.REL_CRCD_REPAY);
			startActivity(it);
		} else if (tag == 2) {
			// 信用卡购汇还款
			requestCommConversationId();
		}

	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		if (ttan == 1) {
			psnCrcdQueryBilledTrans(accountId, numMonth);
		} else if (ttan == 2) {
			// 购汇还款
			// 查询信用卡购汇还款信息
			requestForCrcdForeignPayOff();
		}
		else if (ttan == 3) {
			// 转账还款统一
			// 查询信用卡购汇还款信息
			requesPsnCrcdQueryForeignPayOff();
		}

	}

	
	/**
	 * 信用卡查询购汇还款信息 sunh
	 */
	public void requesPsnCrcdQueryForeignPayOff() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNQUERYDOREIGNPAYOFF);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Crcd.CRCD_ACCOUNTID_RES, accountId);// 转入账户标识 accountId
		map.put(Crcd.CRCD_CURRTYPE_REQ, currType);// 购汇还款账户币种 currType
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requesPsnCrcdQueryForeignPayOffCallBack");
	}

	
	/**
	 * 信用卡查询购汇还款信息返回 sunh
	 * 
	 * @param resultObj
	 */
	public void requesPsnCrcdQueryForeignPayOffCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		TranDataCenter.getInstance().setRelCrcdBuyCallBackMap(result);
		BaseHttpEngine.dissMissProgressDialog();
		Intent intent = new Intent(CrcdTransDetailActivity.this, TransferManagerActivity1.class);
		intent.putExtra(ConstantGloble.JUMP_TO_TRAN_FLAG, ConstantGloble.CREDIT_TO_TRAN);	
		startActivity(intent);
	}
	
	/**
	 * 信用卡查询购汇还款信息
	 */
	public void requestForCrcdForeignPayOff() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.CRCDFOREIGNPAYOFF);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Tran.CREDITCARD_CRCDID_REQ, accountId);// 转入账户信息 crcdId
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestForCrcdForeignPayOffCallBack");
	}

	/**
	 * 信用卡查询购汇还款信息返回
	 * 
	 * @param resultObj
	 */
	public void requestForCrcdForeignPayOffCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		TranDataCenter.getInstance().setRelCrcdBuyCallBackMap(result);
		Intent intent = new Intent(CrcdTransDetailActivity.this, TransferManagerActivity1.class);
		intent.putExtra(ConstantGloble.TRANS_ACCOUNT_TYPE, Crcd.TRANS_CREDIT);
		TranDataCenter.getInstance().setCurrInDetail(detailReturnMap);// 信用卡详情，跳转到转账页面
		TranDataCenter.getInstance().setModuleType(ConstantGloble.CRCE_TYPE);
		intent.putExtra(ConstantGloble.JUMP_TO_TRAN_FLAG, ConstantGloble.REL_CRCD_BUY);
		intent.putExtra(ConstantGloble.CRCD_CURRENCY2, currencyCode);
		BaseHttpEngine.dissMissProgressDialog();
		startActivity(intent);
	}

	/** 查询已出账单 */
	public void psnCrcdQueryBilledTrans(String accountId, String month) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDQUERYBIIEDTRANSN_API);
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Crcd.CRCD_ACCOUNTIDN_REQ, accountId);
		params.put(Crcd.CRCD_STATEMENTMONTHN_REQ, month);

		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnCrcdQueryBilledTransCallBack");
	}

	/** 查询已出账单-----回调 */
	public void requestPsnCrcdQueryBilledTransCallBack(Object obj) {
		BiiResponse response = (BiiResponse) obj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		Map<String, Object> result = (Map<String, Object>) body.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.acc_transferquery_null));
			return;
		}
		requestPsnCrcdQueryBilledTransDetail(creditcardId, statementMonth, currencyCode, ConstantGloble.CRCD_PAGENO,
				null, ConstantGloble.FOREX_PAGESIZE);
	}

}
