package com.chinamworld.bocmbci.biz.acc.mybankaccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.biz.acc.mybankaccount.creditcardrepay.RelCreditCardRemitConfirmActivity1;
import com.chinamworld.bocmbci.biz.acc.mybankaccount.creditcardrepay.RelSelfCreditCardConfirmActivity1;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.BottomButtonUtils;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 信用卡还款
 * 
 * @author wangmengmeng
 * 
 */
public class AccCrcdTransManagerActivity extends AccBaseActivity {
	/** 账户转账页 */
	private View view;
	/** 转出账户类型 */
	private TextView acc_type_value_out;
	/** 转出账户账号 */
	private TextView acc_account_num_out;
	/** 转出账户别名 */
	private TextView acc_account_nickname_out;
	/** 转入账户类型 */
	private TextView acc_type_value_in;
	/** 转入账户账号 */
	private TextView acc_account_num_in;
	/** 转入账户别名 */
	private TextView acc_account_nickname_in;
	/** 底部布局 */
	private LinearLayout bottomLayout = null;
	private int outposition;
	private int inposition;
	/** 转出账户信息 */
	private Map<String, Object> transOutMap;
	/** 转入账户信息 */
	private Map<String, Object> transInMap;
	/** 账户列表 */
	private List<Map<String, Object>> bankAccountList;
	/** 是否是双币种 */
	private boolean ishavecurrencytwo = false;
	/** 第一币种 */
	private static String currency1 = "";
	/** 第二币种 */
	private static String currency2 = "";
	/** 转入账户id */
	private String toPayeeId;
	/** 转账金额 */
	private String amount;
	/** 还款金额设定方式 */
	private String repayAmountSet;
	/** 开通全球人民币记账标识 */
	private boolean openFlag = false;
	/** 转入转出账户交集 */
	List<String> intersectionList;
	/** 根据两张卡币种交集取得的钞汇数据 */
	private Map<String, List<String>> cashRemitInfo;
	/** 要在转账还款确认接口上送的币种代码 */
	String currencyCode;
	/** 要在转账还款确认接口上送的钞汇标识 */
	String cashRemit = "00";
	/** 选择一个币种后从币种钞汇数据里取出的该币种对应的钞汇列表 */
	List<String> cashRemitList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.tran_my_trans));
		// 添加布局
		view = addView(R.layout.acc_crcd_trans_view);
		setLeftSelectedPosition("accountManager_1");
		// 初始化界面
		init();
	}

	/** 初始化界面 */
	@SuppressWarnings("unchecked")
	private void init() {
		outposition = this.getIntent().getIntExtra(ConstantGloble.ACC_POSITION, 0);
		inposition = this.getIntent().getIntExtra(ConstantGloble.ACC_ISMY, 0);
		currency1 = this.getIntent().getStringExtra(ConstantGloble.ACC_CRCD_CURRENCY1);
		currency2 = this.getIntent().getStringExtra(ConstantGloble.ACC_CRCD_CURRENCY2);
		ishavecurrencytwo = this.getIntent().getBooleanExtra(ConstantGloble.CRCD_FLAG, false);
		bankAccountList = AccDataCenter.getInstance().getBankAccountList();
		transOutMap = bankAccountList.get(outposition);
		transInMap = bankAccountList.get(inposition);
		TranDataCenter.getInstance().setAccOutInfoMap(transOutMap);
		TranDataCenter.getInstance().setAccInInfoMap(transInMap);
		acc_type_value_out = (TextView) view.findViewById(R.id.acc_type_value_out);
		acc_account_num_out = (TextView) view.findViewById(R.id.acc_account_num_out);
		acc_account_nickname_out = (TextView) view.findViewById(R.id.acc_account_nickname_out);
		acc_type_value_in = (TextView) view.findViewById(R.id.acc_type_value_in);
		acc_account_num_in = (TextView) view.findViewById(R.id.acc_account_num_in);
		acc_account_nickname_in = (TextView) view.findViewById(R.id.acc_account_nickname_in);
		bottomLayout = (LinearLayout) view.findViewById(R.id.ll_for_crcd_tran);
		toPayeeId = (String) transInMap.get(Acc.ACC_ACCOUNTID_RES);
		// 赋值
		String typeOut = (String) transOutMap.get(Acc.ACC_ACCOUNTTYPE_RES);
		acc_type_value_out.setText(LocalData.AccountType.get(typeOut));
		String numOut = (String) transOutMap.get(Acc.ACC_ACCOUNTNUMBER_RES);
		acc_account_num_out.setText(StringUtil.getForSixForString(numOut));
		acc_account_nickname_out.setText((String) transOutMap.get(Acc.ACC_NICKNAME_RES));
		String typeIn = (String) transInMap.get(Acc.ACC_ACCOUNTTYPE_RES);
		acc_type_value_in.setText(LocalData.AccountType.get(typeIn));
		String numIn = (String) transInMap.get(Acc.ACC_ACCOUNTNUMBER_RES);
		acc_account_num_in.setText(StringUtil.getForSixForString(numIn));
		acc_account_nickname_in.setText((String) transInMap.get(Acc.ACC_NICKNAME_RES));
//		String type = (String) transInMap.get(Acc.ACC_ACCOUNTTYPE_RES);
		
		Map<String, Object> cardDetail;
		List<Map<String, Object>> detailList;
		Map<String, Object> inCardDetail = (Map<String, Object>) bankAccountList.get(inposition).get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
		List<Map<String, Object>> inDetailList = (List<Map<String, Object>>) inCardDetail.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
		
		if (typeOut.equals(ConstantGloble.ACC_TYPE_GRE)
				|| typeOut.equals(ConstantGloble.ZHONGYIN)// P601 转出增加103  107
				|| typeOut.equals(ConstantGloble.SINGLEWAIBI)) {
			cardDetail = AccDataCenter.getInstance().getResultDetail();
			detailList = (List<Map<String, Object>>) cardDetail.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
			intersectionList = getIntersectionList(true, detailList, inDetailList);
		} else {
			cardDetail = (Map<String, Object>) bankAccountList.get(outposition);
			detailList = (List<Map<String, Object>>) cardDetail.get(ConstantGloble.ACC_DETAILIST);
			intersectionList = getIntersectionList(false, detailList, inDetailList);
		}
		
		if (typeOut.equals(ConstantGloble.ACC_TYPE_BRO)) {
			cashRemitInfo = getCurrencyAndCashRemitList(detailList);
		}
		// 显示信用卡底部视图
		initCrcdView();
	}
	
	/** 初始化卡片显示数据 */
	@SuppressWarnings("unchecked")
	private void initCardShowInfo(boolean isCrcdToCrcd, String outType) {
		Map<String, Object> cardDetail;
		List<Map<String, Object>> detailList;
		if (isCrcdToCrcd) {
			cardDetail = AccDataCenter.getInstance().getResultDetail();
			detailList = (List<Map<String, Object>>) cardDetail.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
		} else {
			cardDetail = (Map<String, Object>) bankAccountList.get(outposition);
			detailList = (List<Map<String, Object>>) cardDetail.get(ConstantGloble.ACC_DETAILIST);
		}

		Map<String, Object> inCardDetail = (Map<String, Object>) bankAccountList.get(inposition).get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
		List<Map<String, Object>> inDetailList = (List<Map<String, Object>>) inCardDetail.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
		
		// 如果开通了全球人民币记账功能，只显示人民币
		if (openFlag) {
			for (int i = 0; i < intersectionList.size(); i++) {
				if (!intersectionList.get(i).equals("001")) {
					intersectionList.remove(i);
				}
			}
		}
		
		if (!isCrcdToCrcd) {
			// 非信用卡转信用卡的情况
			if (outType.equals(ConstantGloble.ACC_TYPE_BRO)) {
				// 借记卡转信用卡的情况
				initJJKtoCrcd(intersectionList, inDetailList, detailList);
			} else {
				// 非借记卡非信用卡转信用卡的情况，这种情况暂时没有，转入是信用卡时转出只支持借记卡和长城信用卡
				initDefaltCardShow();
			}
		} else {
			// 信用卡转信用卡的情况
			initCrcdToCrcd(intersectionList, inDetailList, detailList);
		}
	}
	
	/** 当借记卡转信用卡或信用卡转信用卡时获取转入转出账户的交集 */
	private List<String> getIntersectionList(boolean isCrcdToCrcd, List<Map<String, Object>> outList, List<Map<String, Object>> inList) {
		List<String> list = new ArrayList<String>();
		if (!isCrcdToCrcd) {
			for (int i = 0; i < outList.size(); i++) {
				Map<String, Object> outMap = outList.get(i);
				for (int j = 0; j < inList.size(); j++) {
					Map<String, Object> inMap = inList.get(j);
					if (outMap.get(ConstantGloble.FOREX_CURRENCYCODE).equals(inMap.get(Crcd.CRCD_CURRENCY))) {
						list.add((String) outMap.get(ConstantGloble.FOREX_CURRENCYCODE));
					}
				}
			}
		} else {
			for (int i = 0; i < outList.size(); i++) {
				Map<String, Object> outMap = outList.get(i);
				for (int j = 0; j < inList.size(); j++) {
					Map<String, Object> inMap = inList.get(j);
					if (outMap.get(Crcd.CRCD_CURRENCY).equals(inMap.get(Crcd.CRCD_CURRENCY))) {
						list.add((String) outMap.get(Crcd.CRCD_CURRENCY));
					}
				}
			}
		}
		// 去掉重复币种代码
		Set<String> set = new LinkedHashSet<String>();
		set.addAll(list);
		list.clear();
		list.addAll(set);
		return list;
	}
	
	/** 根据两张卡取得的币种交集中的币种组装成{币种代码[钞汇标识1]，[钞汇标识2]}的形式(借记卡才有) */
	private Map<String, List<String>> getCurrencyAndCashRemitList(List<Map<String, Object>> tranOutList) {
		// 要返回的组装好的数据
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		
		for (int i = 0; i < intersectionList.size(); i++) {
			List<String> cashRemitList = new ArrayList<String>();
			for (int j = 0; j < tranOutList.size(); j++) {
				Map<String, Object> detailMap = tranOutList.get(j);
				if (intersectionList.get(i).equals(detailMap.get(ConstantGloble.FOREX_CURRENCYCODE))) {
					cashRemitList.add((String) detailMap.get(Acc.DETAIL_CASHREMIT_RES));
				}
			}
			map.put(intersectionList.get(i), cashRemitList);
		}
		return map;
	}
	
	/**
	 * 初始化信用卡转信用卡的卡片显示数据
	 * 
	 * @param intersectionList
	 *            信用卡与信用卡取交集后的币种列表
	 * @param inList
	 *            转入账户详情列表
	 * @param outList
	 *            转出账户详情列表
	 */
	private void initCrcdToCrcd(List<String> intersectionList, List<Map<String, Object>> inList, List<Map<String, Object>> outList) {
		// 信用卡与信用卡、信用卡与借记卡交集最多2个币种
		if (StringUtil.isNullOrEmpty(intersectionList)) {
			return;
		}
		Map<String, Object> outMap1 = null;
		Map<String, Object> outMap2 = null;
		Map<String, Object> inMap1 = null;
		Map<String, Object> inMap2 = null;
		
		for (int i = 0; i < intersectionList.size(); i++) {
			String currency = intersectionList.get(i);
			// 从转出账户详情取出交集币种信息
			for (int j = 0; j < outList.size(); j++) {
				if (currency.equals(outList.get(j).get(Crcd.CRCD_CURRENCY))) {
					if (StringUtil.isNullOrEmpty(outMap1)) {
						outMap1 = outList.get(j);
						break;
					} else {
						outMap2 = outList.get(j);
						break;
					}
				}
			}
			// 从转入账户详情取出交集币种信息
			for (int j = 0; j < inList.size(); j++) {
				if (currency.equals(inList.get(j).get(Crcd.CRCD_CURRENCY))) {
					if (StringUtil.isNullOrEmpty(inMap1)) {
						inMap1 = inList.get(j);
						break;
					} else {
						inMap2 = inList.get(j);
						break;
					}
				}
			}
		}
		if (!StringUtil.isNullOrEmpty(outMap1))
			setTvData(true, true, R.id.ll_out_currency_rmb, R.id.tv_key_for_out_currency_rmb, R.id.tv_out_currency_rmb, outMap1);
		if (!StringUtil.isNullOrEmpty(outMap2))
			setTvData(true, true, R.id.ll_out_currency_MYXC, R.id.tv_keyForMY, R.id.tv_out_currency_MYXC, outMap2);
		if (!StringUtil.isNullOrEmpty(inMap1))
			setTvData(false, true, R.id.ll_in_currency_rmb, R.id.tv_key_for_in_currency_rmb, R.id.tv_in_currency_rmb, inMap1);
		if (!StringUtil.isNullOrEmpty(inMap2))
			setTvData(false, true, R.id.ll_in_currency_MY, R.id.tv_key_for_in_currency_MY, R.id.tv_in_currency_MY, inMap2);
	}
	
	/**
	 * 初始化借记卡转信用卡的卡片显示数据
	 * 
	 * @param intersectionList
	 *            借记卡与信用卡取交集后的币种列表
	 * @param inList
	 *            转入账户详情列表
	 * @param outList
	 *            转出账户详情列表
	 */
	private void initJJKtoCrcd(List<String> intersectionList, List<Map<String, Object>> inList, List<Map<String, Object>> outList) {
		// 信用卡与信用卡、信用卡与借记卡交集最多2个币种
		if (StringUtil.isNullOrEmpty(intersectionList)) {
			return;
		}
		Map<String, Object> outMap1 = null;
		Map<String, Object> outMap2 = null;
		Map<String, Object> outMap3 = null;
		Map<String, Object> inMap1 = null;
		Map<String, Object> inMap2 = null;
		for (int i = 0; i < intersectionList.size(); i++) {
			String currency = intersectionList.get(i);
			// 从转出账户详情取出交集币种信息
			for (int j = 0; j < outList.size(); j++) {
				Map<String, Object> map = outList.get(j);
				if (currency.equals(map.get(ConstantGloble.FOREX_CURRENCYCODE))) {
					LogGloble.i("outMap", map.toString());
					if (StringUtil.isNullOrEmpty(outMap1)) {
						outMap1 = map;
					} else if (StringUtil.isNullOrEmpty(outMap2)) {
						outMap2 = map;
					} else {
						outMap3 = map;
					}
				}
			}
			// 从转入账户详情取出交集币种信息
			for (int j = 0; j < inList.size(); j++) {
				if (currency.equals(inList.get(j).get(Crcd.CRCD_CURRENCY))) {
					if (StringUtil.isNullOrEmpty(inMap1)) {
						inMap1 = inList.get(j);
						break;
					} else {
						inMap2 = inList.get(j);
						break;
					}
				}
			}
		}
		if (!StringUtil.isNullOrEmpty(outMap1))
			setTvData(true, false, R.id.ll_out_currency_rmb, R.id.tv_key_for_out_currency_rmb, R.id.tv_out_currency_rmb, outMap1);
		if (!StringUtil.isNullOrEmpty(outMap2))
			setTvData(true, false, R.id.ll_out_currency_MYXC, R.id.tv_keyForMY, R.id.tv_out_currency_MYXC, outMap2);
		if (!StringUtil.isNullOrEmpty(outMap3))
			setTvData(true, false, R.id.ll_out_currency_MYXH, R.id.tv_key_for_out_currency_MYXH, R.id.tv_out_currency_MYXH, outMap3);
		if (!StringUtil.isNullOrEmpty(inMap1))
			setTvData(false, false, R.id.ll_in_currency_rmb, R.id.tv_key_for_in_currency_rmb, R.id.tv_in_currency_rmb, inMap1);
		if (!StringUtil.isNullOrEmpty(inMap2))
			setTvData(false, false, R.id.ll_in_currency_MY, R.id.tv_key_for_in_currency_MY, R.id.tv_in_currency_MY, inMap2);
	}
	
	/**
	 * 设置卡片一行的数据
	 * 
	 * @param isOut
	 *            要操作的卡片是否是转出账户
	 * @param outIsCrcd
	 *            转出账户是否是信用卡
	 * @param visibleId
	 *            要显示的控件id
	 * @param keyId
	 *            字段名tv的id
	 * @param valueId
	 *            字段值tv的id
	 * @param map
	 *            设置字段的数据源
	 */
	private void setTvData(boolean isOut, boolean outIsCrcd, int visibleId, int keyId,int valueId, Map<String, Object> map) {
		view.findViewById(visibleId).setVisibility(View.VISIBLE);
		if (isOut) {
			if (!outIsCrcd) {
				// 转出账户非信用卡
				if (map.get(ConstantGloble.FOREX_CASEREMIT).equals(ConstantGloble.CASHRMIT_CASH)) {
					// 币种现钞
					((TextView) view.findViewById(keyId)).setText(LocalData.Currency.get(map.get(ConstantGloble.FOREX_CURRENCYCODE)) + "现钞");
				} else if (map.get(ConstantGloble.FOREX_CASEREMIT).equals(ConstantGloble.CASHRMIT_PARITIES)) {
					// 币种现汇
					((TextView) view.findViewById(keyId)).setText(LocalData.Currency.get(map.get(ConstantGloble.FOREX_CURRENCYCODE)) + "现汇");
				} else {
					// 人民币的情况
					((TextView) view.findViewById(keyId)).setText(LocalData.Currency.get(map.get(ConstantGloble.FOREX_CURRENCYCODE)));
				}
				((TextView) view.findViewById(valueId)).setText(StringUtil.parseStringPattern((String) map.get(ConstantGloble.FOREX_AVAILABLEBALANCE), 2));
			} else {
				// 转出账户是信用卡
				((TextView) view.findViewById(keyId)).setText(LocalData.Currency.get(map.get(Crcd.CRCD_CURRENCY)));
				((TextView) view.findViewById(valueId)).setText("可用余额 " + StringUtil.parseStringPattern((String) map.get(Crcd.CRCD_LOANBALANCELIMIT), 2));
			}
		} else {
			// 转入账户是信用卡
			String tip = "";
			if (map.get(Crcd.LOANBALANCELIMITFLAG).equals("0"))
				tip += "欠款 ";
			else if (map.get(Crcd.LOANBALANCELIMITFLAG).equals("1"))
				tip += "存款 ";
			((TextView) view.findViewById(keyId)).setText(LocalData.Currency.get(map.get(Crcd.CRCD_CURRENCY)));
			((TextView) view.findViewById(valueId)).setText(tip + StringUtil.parseStringPattern((String) map.get(Crcd.CRCD_LOANBALANCELIMIT), 2));
		}
	}
	
	private void initDefaltCardShow() {
		view.findViewById(R.id.ll_out_currency).setVisibility(View.GONE);
		view.findViewById(R.id.ll_account_nickname_out).setVisibility(View.VISIBLE);
		view.findViewById(R.id.ll_in_currency).setVisibility(View.GONE);
		view.findViewById(R.id.ll_account_nickname_in).setVisibility(View.VISIBLE);
	}

	/**
	 * 初始化关联信用卡 底部视图
	 */
	@SuppressWarnings("unchecked")
	private void initCrcdView() {
		View v = LayoutInflater.from(this).inflate(R.layout.tran_relation_credit_card_trans_seting_mytransfer, null);
		// 本人关联信用卡还款
		Button relCrcdRepayBtn = (Button) v.findViewById(R.id.btn_self_rel_creditCard_transSeting);
		relCrcdRepayBtn.setText("转账还款");
		((TextView) v.findViewById(R.id.tv_transTitle)).setText("转账还款");
		String typeOut = (String) transOutMap.get(Acc.ACC_ACCOUNTTYPE_RES);

		String type = (String) transInMap.get(Acc.ACC_ACCOUNTTYPE_RES);
		// 关联信用卡购汇还款
		Button relCrcdBuyBtn = (Button) v.findViewById(R.id.btn_payRemit_rel_creditCard_transSeting);
		if ((intersectionList.size() == 1) && (intersectionList.get(0).equals("001"))) {
			relCrcdBuyBtn.setVisibility(View.GONE);
		}
		if (typeOut.equals(ConstantGloble.SINGLEWAIBI)) {
			//转出单外币 只能还款 不能购汇
			relCrcdRepayBtn.setVisibility(View.VISIBLE);
			relCrcdRepayBtn.setEnabled(true);
			relCrcdRepayBtn.setTextColor(getResources().getColor(R.color.black));
			relCrcdRepayBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					tranTypeFlag = TRANTYPE_REL_CRCD_REPAY;
					View view = (ViewGroup) LayoutInflater.from(AccCrcdTransManagerActivity.this).inflate(
							R.layout.tran_rel_transfer_seting_credit_card_in, null);
					initRelCrcdTran(view);
					bottomLayout.removeAllViews();
					bottomLayout.addView(view);
				}
			});
			BottomButtonUtils.setSingleLineStyleGray(relCrcdBuyBtn);
			relCrcdBuyBtn.setVisibility(View.GONE);
			relCrcdBuyBtn.setEnabled(false);
			relCrcdBuyBtn.setTextColor(getResources().getColor(R.color.gray));
			
			initCardShowInfo(true, typeOut);
			
		
		}else{
//		if (type.equals(ConstantGloble.SINGLEWAIBI)) {
//			// 单外币信用卡
//			relCrcdRepayBtn.setVisibility(View.GONE);
//			relCrcdRepayBtn.setEnabled(false);
//			relCrcdRepayBtn.setTextColor(getResources().getColor(R.color.gray));
//			
//			BottomButtonUtils.setSingleLineStyleGray(relCrcdBuyBtn);
//			relCrcdBuyBtn.setVisibility(View.VISIBLE);
//			relCrcdBuyBtn.setEnabled(true);
//			relCrcdBuyBtn.setTextColor(getResources().getColor(R.color.black));
//			relCrcdBuyBtn.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					tranTypeFlag = TRANTYPE_REL_CRCD_BUY;
//					// 查询第二币种详情
//					toPayeeId = (String) transInMap.get(Comm.ACCOUNT_ID);
//					currency2 = currency1;
//					requestForCrcdCurrencyDetail(toPayeeId);
//				}
//			});
//		} else {
			relCrcdRepayBtn.setVisibility(View.VISIBLE);
			relCrcdRepayBtn.setEnabled(true);
			relCrcdRepayBtn.setTextColor(getResources().getColor(R.color.black));
			relCrcdRepayBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					tranTypeFlag = TRANTYPE_REL_CRCD_REPAY;
//					View view = (ViewGroup) LayoutInflater.from(AccCrcdTransManagerActivity.this).inflate(
//									R.layout.tran_relation_self_credit_card_trans_seting_mytransfer, null);
					View view = (ViewGroup) LayoutInflater.from(AccCrcdTransManagerActivity.this).inflate(
							R.layout.tran_rel_transfer_seting_credit_card_in, null);
					
//					initRelCrcdRepayView(view);
					initRelCrcdTran(view);
					bottomLayout.removeAllViews();
					bottomLayout.addView(view);
				}
			});
			
			if (ishavecurrencytwo) {
				relCrcdBuyBtn.setVisibility(View.VISIBLE);
				relCrcdBuyBtn.setEnabled(true);
				relCrcdBuyBtn.setTextColor(getResources().getColor(R.color.black));
				relCrcdBuyBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						tranTypeFlag = TRANTYPE_REL_CRCD_BUY;
						// 查询第二币种详情
						toPayeeId = (String) transInMap.get(Comm.ACCOUNT_ID);
						requestForCrcdCurrencyDetail(toPayeeId);
					}
				});
			} else {
				relCrcdRepayBtn.setVisibility(View.VISIBLE);
				BottomButtonUtils.setSingleLineStyleGray(relCrcdRepayBtn);
				relCrcdBuyBtn.setVisibility(View.GONE);
				relCrcdBuyBtn.setTextColor(getResources().getColor(R.color.gray));
				relCrcdBuyBtn.setEnabled(false);
			}
			
			if (type.equals(ConstantGloble.ZHONGYIN) || type.equals(ConstantGloble.GREATWALL)) {
				Map<String, Object> inCardDetail = (Map<String, Object>) bankAccountList.get(inposition).get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
				List<Map<String, Object>> inDetailList = (List<Map<String, Object>>) inCardDetail.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
				if (inDetailList.size() > 1) {
					BaseHttpEngine.showProgressDialog();
					Map<String, Object> params = new HashMap<String, Object>();
					params.put(Crcd.CRCD_ACCOUNTID_REQ, bankAccountList.get(inposition).get(Crcd.CRCD_ACCOUNTID_REQ));
					requestHttp(Crcd.CRCD_RMBACCOUNTQUERY_API, "requestPsnCrcdChargeOnRMBAccountQueryCallBack", params, false);
				}
			} else{
				initCardShowInfo(true, typeOut);
			}
		}
		bottomLayout.removeAllViews();
		bottomLayout.addView(v);
	}

//	/**
//	 * 初始化关联信用卡还款
//	 */
//	private void initRelCrcdRepayView(View v) {
//		Button nextBtn = (Button) v.findViewById(R.id.btn_rel_self_creditCard_transSeting);
//		final EditText repayAmountEt = (EditText) v.findViewById(R.id.et_repayAmountValue_relSelf_creditCard_transSeting);
//		TextView tv_for_amount = (TextView) v.findViewById(R.id.tv_for_amount);
//		EditTextUtils.relateNumInputToChineseShower(repayAmountEt, tv_for_amount);
//		nextBtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				amount = repayAmountEt.getText().toString().trim();
//				boolean flag = judgeUserData(amount);
//				if (!flag) {
//					return;
//				}
//
//				Map<String, String> userInputMap = new HashMap<String, String>();
//				userInputMap.put(Tran.INPUT_TRANSFER_AMOUNT, amount);
//				TranDataCenter.getInstance().setUserInputMap(userInputMap);
//				Intent intent = new Intent();
//				intent.setClass(AccCrcdTransManagerActivity.this, RelSelfCreditCardConfirmActivity1.class);
//				startActivity(intent);
//			}
//		});
//	}
	
	/**
	 * 初始化底部视图 信用卡模块进入 非 信用卡还款底部视图
	 */
	private void initRelCrcdTran(final View v) {
		// initRelView11(v);
		((TextView) v.findViewById(R.id.tv_transTitle)).setText("转账还款");
		Button nextBtn = (Button) v.findViewById(R.id.btn_rel_self_creditCard_transSeting);
		final EditText repayAmountEt = (EditText) v.findViewById(R.id.et_repayAmountValue_relSelf_creditCard_transSeting);
		TextView tv_for_amount = (TextView) v.findViewById(R.id.tv_for_amount);
		EditTextUtils.relateNumInputToChineseShower(repayAmountEt, tv_for_amount);
		final List<String> currencyCodeFlagList = new ArrayList<String>();
		for (int i = 0; i < intersectionList.size(); i++) {
			String currencyCode = intersectionList.get(i);
			currencyCodeFlagList.add(LocalData.Currency.get(currencyCode));
		}
		// 币种
		Spinner currencySp = (Spinner) v.findViewById(R.id.sp_rel_trans_currency_tranSeting);
		// 钞汇标志
		final Spinner cashRemitSp = (Spinner) v.findViewById(R.id.sp_rel_trans_cashRemit_tranSeting);
		final List<String> cashRemitFlagList = new ArrayList<String>();

		ArrayAdapter<String> currencyAdapter = new ArrayAdapter<String>(this, R.layout.custom_spinner_item, currencyCodeFlagList);
		currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		currencySp.setAdapter(currencyAdapter);
		// 设置币种
		currencySp.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				currencyCode = intersectionList.get(position);
				if (currencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) {
					cashRemitSp.setEnabled(false);
					cashRemitSp.setBackgroundDrawable(AccCrcdTransManagerActivity.this.getResources().getDrawable(R.drawable.bg_spinner_default));
				} else {
					cashRemitSp.setEnabled(true);
					cashRemitSp.setBackgroundDrawable(AccCrcdTransManagerActivity.this.getResources().getDrawable(R.drawable.bg_spinner));
				}
				System.out.println("cashRemitInfo-->" + cashRemitInfo);
				cashRemitFlagList.clear();
				if (transOutMap.get(Acc.ACC_ACCOUNTTYPE_RES).equals(ConstantGloble.GREATWALL)
						||transOutMap.get(Acc.ACC_ACCOUNTTYPE_RES).equals(ConstantGloble.ZHONGYIN)// P601 增加 103  107
						||transOutMap.get(Acc.ACC_ACCOUNTTYPE_RES).equals(ConstantGloble.SINGLEWAIBI)) {
					v.findViewById(R.id.sp_rel_trans_cashRemit_tranSeting_layout).setVisibility(View.GONE);
				}
				if (StringUtil.isNullOrEmpty(cashRemitInfo)) {
					cashRemitFlagList.add("-");
				} else {
					cashRemitList = cashRemitInfo.get(currencyCode);
					for (int i = 0; i < cashRemitList.size(); i++) {
						String cashRemit = (String) cashRemitList.get(i);
						cashRemitFlagList.add(LocalData.cashRemitBackMap.get(cashRemit));
					}
				}

				ArrayAdapter<String> cashRemitAdapter = new ArrayAdapter<String>(AccCrcdTransManagerActivity.this, R.layout.custom_spinner_item, cashRemitFlagList);
				cashRemitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				cashRemitSp.setAdapter(cashRemitAdapter);
				cashRemitSp.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
						if (currencyCode.equals("001")) {
							cashRemit = "00";
						} else {
							if (StringUtil.isNullOrEmpty(cashRemitList)) {
								cashRemit = "00";
							} else {
								cashRemit = cashRemitList.get(position);
							}
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						if (currencyCode.equals("001")) {
							cashRemit = "00";
						} else {
							if (StringUtil.isNullOrEmpty(cashRemitList)) {
								cashRemit = "00";
							} else {
								cashRemit = cashRemitList.get(0);
							}
						}
					}
				});
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				currencyCode = intersectionList.get(0);// 默认上送人民币
				if (currencyCode.equals("001")) {
					cashRemit = "00";
				} else {
					cashRemit = cashRemitInfo.get(currencyCode).get(0);
				}
			}
		});
		
		nextBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				amount = repayAmountEt.getText().toString().trim();
				boolean flag = judgeUserData(amount);// 校验用户输入是否合法
				if (!flag) {
					return;
				}

				Map<String, String> userInputMap = new HashMap<String, String>();
				userInputMap.put(Tran.INPUT_TRANSFER_AMOUNT, amount);
				TranDataCenter.getInstance().setUserInputMap(userInputMap);
				
				
				requestPsnTransGetBocTransferCommissionCharge(ConstantGloble.PB021);
				
			
			}
		});

		bottomLayout.removeAllViews();
		bottomLayout.addView(v);
	}

	/**
	 * 判断用户输入数据
	 * 
	 * @return true 合格 false 不合格
	 */
	private boolean judgeUserData(String amount) {
		ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
		RegexpBean transAmountReg = new RegexpBean(getResources().getString(R.string.reg_transferAmount), amount, "amount");
		lists.add(transAmountReg);
		if (!RegexpUtils.regexpDate(lists)) {
			return false;
		}
		return true;
	}

	/**
	 * 信用卡购汇还款
	 * 
	 * @param v
	 */
	private void initRelCrcdBuyView(View v) {
		RadioGroup remitTypeRg = (RadioGroup) v.findViewById(R.id.rg_creditType_transSeting);
		RadioButton allRepayRb = (RadioButton) v.findViewById(R.id.rb_all_transSeting);
		final RadioButton partRepayRb = (RadioButton) v.findViewById(R.id.rb_part_transSeting);
		TextView currencyTv = (TextView) v.findViewById(R.id.relation_credit_card_currency);

		final EditText repayAmountEt = (EditText) v.findViewById(R.id.et_amount_rel_creditCard_transSeting);
		Button nextBtn = (Button) v.findViewById(R.id.btn_next_creditCard_transSeting);
		// 还款金额设定方式
		// Map<String, Object> currOutDetail = TranDataCenter.getInstance()
		// .getCurrOutDetail();
		Map<String, Object> currOutDetail = TranDataCenter.getInstance().getRelCrcdBuyCallBackMap();
		amount = (String) currOutDetail.get(Tran.CREDITCARD_BALLANCEAMT_RES);
		final String currentBalance = amount;
		TextView tv_for_amount = (TextView) v.findViewById(R.id.tv_for_amount);
		EditTextUtils.relateNumInputToChineseShower(repayAmountEt, tv_for_amount);
		currencyTv.setText(LocalData.Currency.get(currency2));
		allRepayRb.setChecked(true);
		repayAmountSet = ConstantGloble.REPAY_ALL;
		repayAmountEt.setText(StringUtil.parseStringPattern(amount, 2));
		repayAmountEt.setEnabled(false);
		remitTypeRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_all_transSeting:// 全部
					repayAmountSet = ConstantGloble.REPAY_ALL;
					repayAmountEt.setText(StringUtil.parseStringPattern(currentBalance, 2));
					amount = currentBalance;
					repayAmountEt.setEnabled(false);
					break;
				case R.id.rb_part_transSeting:// 部分
					repayAmountSet = ConstantGloble.REPAY_PART;
					repayAmountEt.setText(null);
					repayAmountEt.setEnabled(true);
					break;
				default:
					break;
				}
			}
		});
		// 下一步
		nextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (partRepayRb.isChecked()) {
					amount = repayAmountEt.getText().toString().trim();
					boolean flag = judgeUserData(amount);
					if (!flag) {
						return;
					}
				}
				// 其他模块
				Map<String, String> userInputMap = new HashMap<String, String>();
				userInputMap.put(Tran.INPUT_TRANSFER_AMOUNT, amount);
				userInputMap.put(Tran.INPUT_CURRENCY_CODE, currency2);
				userInputMap.put(Tran.CREDITCARD_FOREIGN_CRCDAUTOREPAYMODE_REQ, repayAmountSet);
				TranDataCenter.getInstance().setUserInputMap(userInputMap);
				
				
				requestPsnCrcdForeignPayOffFee();
				
			}
		});
	}

	/**
	 * 查询信用卡详情 PsnCrcdQueryAccountDetail 外币详情
	 * 
	 * @param accountId
	 */
	private void requestForCrcdCurrencyDetail(String accountId) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		Map<String, String> paramsmap = new HashMap<String, String>();
		biiRequestBody.setMethod(Tran.CRCDQUERYACCOUNTDETAIL);
		paramsmap.put(Tran.QUERY_CONDITION_CRCD_ID, accountId);
		// 外币
		paramsmap.put(Tran.QUERY_CONDITION_CRCD_CURRENCY, currency2);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestForCrcdCurrencyDetailCallBack");
	}

	@SuppressWarnings("unchecked")
	public void requestForCrcdCurrencyDetailCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		Map<String, String> detailMap = new HashMap<String, String>();
		List<Map<String, String>> detailList = new ArrayList<Map<String, String>>();
		detailList = (List<Map<String, String>>) resultMap.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
		if (StringUtil.isNullOrEmpty(detailList)) {
			BiiHttpEngine.dissMissProgressDialog();
			return;
		}
		detailMap = detailList.get(0);
		if (StringUtil.isNullOrEmpty(detailMap)) {
			BiiHttpEngine.dissMissProgressDialog();
			return;
		}
		// 首先判断信用卡余额
		String currentflag = (String) detailMap.get(Crcd.CRCD_CURRENTBALANCEFLAG);
		if (StringUtil.isNull(currentflag)) {
			return;
		}
		if (currentflag.equals(ConstantGloble.LOAN_CURRENTINDEX_VALUE)) {
			TranDataCenter.getInstance().setCurrInDetail(resultMap);
			requestCommConversationId();
		} else {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().createDialog(null, R.string.crcd_foreign_no_owe);
			return;
		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);// 信用卡购汇还款
		requestForCrcdForeignPayOff();
	}

	/**
	 * 信用卡查询购汇还款信息
	 */
	public void requestForCrcdForeignPayOff() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.CRCDFOREIGNPAYOFF);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Tran.CREDITCARD_CRCDID_REQ, toPayeeId);// 转入账户信息 crcdId
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestForCrcdForeignPayOffCallBack");
	}

	/**
	 * 信用卡查询购汇还款信息返回
	 * 
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void requestForCrcdForeignPayOffCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		TranDataCenter.getInstance().setRelCrcdBuyCallBackMap(result);
//		Map<String, Object> resultMap = TranDataCenter.getInstance().getCurrInDetail();
//		Map<String, String> detailMap = new HashMap<String, String>();
//		List<Map<String, String>> detailList = new ArrayList<Map<String, String>>();
//		detailList = (List<Map<String, String>>) resultMap.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
//		detailMap = detailList.get(0);
//		String currentBalance = (String) detailMap.get(Tran.CRCD_CURRENTBALANCE);
		BaseDroidApp.getInstanse().dismissMessageDialog();
		View view = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.tran_relation_credit_card_remit_trans_seting_mytransfer, null);
		initRelCrcdBuyView(view);
		bottomLayout.removeAllViews();
		bottomLayout.addView(view);
	}
	
	/** 全球人民币记账接口返回 */
	@SuppressWarnings("unchecked")
	public void requestPsnCrcdChargeOnRMBAccountQueryCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		if (result.get(Crcd.CRCD_OPENFLAG_RES).equals(ConstantGloble.CRCD_TRUE_KEY)) {
			openFlag = true;
		} else {
			openFlag = false;
		}
		
		transOutMap = bankAccountList.get(outposition);
		String typeOut = (String) transOutMap.get(Acc.ACC_ACCOUNTTYPE_RES);
		if (typeOut.equals(ConstantGloble.ACC_ACTYPEGRCAS)
				|| typeOut.equals(ConstantGloble.ZHONGYIN)// P601 转出增加103  107
				|| typeOut.equals(ConstantGloble.SINGLEWAIBI)	
				) {
			initCardShowInfo(true, typeOut);
		} else {
			initCardShowInfo(false, typeOut);
		}
		BaseHttpEngine.dissMissProgressDialog();
	}

	
	

	/** 信用卡购汇还款手续费试算 */
	private void requestPsnCrcdForeignPayOffFee() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDFOREIGNOFFFEE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		Map<String, String> map = new HashMap<String, String>();
		map.put(Crcd.CRCD_CRCDID, (String) accInInfoMap.get(Comm.ACCOUNT_ID));
		map.put(Crcd.CRCD_CRCDACCTNAME, (String) accInInfoMap.get(Tran.ACCOUNTNAME_RES));
		map.put(Crcd.CRCD_CRCDACCTNO, (String) accInInfoMap.get(Tran.ACCOUNTNUMBER_RES));
		map.put(Crcd.CRCD_CRCDRMBACCID, (String) accOutInfoMap.get(Comm.ACCOUNT_ID));
		map.put(Crcd.CRCD_CRCDAUTOREPAYMODE, repayAmountSet);
		map.put(Crcd.CRCD_CRCDAMOUNT, amount);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnCrcdForeignPayOffFeeCallBack");
		
	}
	public void requestPsnCrcdForeignPayOffFeeCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		TranDataCenter.getInstance().setCommissionChargeMap(result);
		Intent intent = new Intent(AccCrcdTransManagerActivity.this, RelCreditCardRemitConfirmActivity1.class);
		startActivity(intent);
		
	}
	private void requestPsnTransGetBocTransferCommissionCharge(String serviceId) {
		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		String fromAccountId = (String) accOutInfoMap.get(Tran.ACCOUNTID_RES);
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
	
		String	toAccountId = (String) accInInfoMap.get(Crcd.CRCD_ACCOUNTID_RES);
	
		String payeeactNo = (String) accInInfoMap.get(Tran.ACCOUNTNUMBER_RES);
		String payeeName = (String) accInInfoMap.get(Tran.ACCOUNTNAME_RES);

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.TRANSFER_COMMISSIONCHARGE_API);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Tran.SERVICEID_REQ, serviceId);
		map.put(Tran.RELTRANS_FROMACCOUNTID_REQ, fromAccountId);
		map.put(Tran.RELTRANS_TOACCOUNTID_REQ, toAccountId);
		map.put(Tran.RELTRANS_CURRENCY_REQ, currencyCode);
		map.put(Tran.RELTRANS_AMOUNT_REQ, amount);
		map.put(Tran.RELTRANS_CASHREMIT_REQ, cashRemit);
		map.put(Tran.RELTRANS_REMARK_REQ, "");
		map.put(Tran.RELTRANS_PAYEEACTNO_REQ, payeeactNo);
		map.put(Tran.RELTRANS_PAYEENAME_REQ, payeeName);
		map.put(Tran.NOTIFYID, null);

		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnTransGetBocTransferCommissionChargeCallBack");
	}
	public void requestPsnTransGetBocTransferCommissionChargeCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		TranDataCenter.getInstance().setCommissionChargeMap(result);
		Intent intent = new Intent()
		.putExtra(ConstantGloble.TRAN_CASHREMIT, cashRemit)
		.putExtra(Acc.DETAIL_CURRENCYCODE_RES, currencyCode);
		intent.setClass(AccCrcdTransManagerActivity.this, RelSelfCreditCardConfirmActivity1.class);
		startActivity(intent);// TODO 下一步跳转页面
		
	}
}

