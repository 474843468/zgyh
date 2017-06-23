package com.chinamworld.bocmbci.biz.bocinvt_p603.inverstagreemanager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.MainActivity;
import com.chinamworld.bocmbci.biz.SecondMainActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 协议管理协议详情页
 * 
 * @author niuchf
 * 
 */
public class PeriodAgreeDetailNewActivity extends BociBaseActivity implements
OnClickListener {
	public static final String TAG = "PeriodAgreeDetailNewActivity";
	/** 协议详情信息页 */
	private View view;
	/** 协议详情列表 */
	private Map<String, Object> detailMap;
	/** 协议代码 */
	private TextView tv_agreementCode;
	/** 协议名称 */
	private TextView tv_agreementName;
	/** 币种 */
	private TextView currency1;
	/** 协议类型 */
	private TextView tv_agreementType;
	/** 投资方式 */
	private TextView tv_inverstStyle;
	/** 协议总期数 */
	private TextView tv_agreementCount;
	/** 当前期数 */
	private TextView tv_agreeIssuePeriod;
	/** 协议投资周期 */
	private TextView tv_agreementPeriod;
	/** 最小投资期数 */
	private TextView tv_minPeriodCount;
	/** 单周期投资期限 */
	private TextView tv_singlePeriodLimit;
	/** 购买频率文本 */
	private TextView tv_buyFrequency1;
	/** 购买频率 */
	private TextView tv_buyFrequency;
	/** 下次购买日期 */
	private TextView tv_nextBuyDate;
	/** 赎回频率文本 */
	private TextView tv_redemptionFrequency1;
	/** 赎回频率 */
	private TextView tv_redemptionFrequency;
	/** 下次赎回日期 */
	private TextView tv_nextRedemptionDate;
	/** 投资产品信息 */
	private TextView tv_inverstProductInfo2;
	/** 预计年收益率 */
	private TextView tv_yearlyRR;
	/** 协议投资起点金额 */
	private TextView tv_startCash,startCashTextView;
	/** 金额模式 */
	private TextView tv_cashModel;
	/** 理财交易账户 **/
	private TextView tv_bocAccno;
	/** 协议投资金额文本 */
	private TextView tv_agreementInverstCash1;
	/** 协议投资金额回显值 */
	private TextView tv_agreementInverstCash;
	/** 签约期数文本 */
	private TextView tv_signNumber1;
	/** 签约期数回显值 */
	private TextView tv_signNumber;
	/** 已执行期数 */
	private TextView tv_executedNumber;
	/** 剩余期数 */
	private TextView tv_residueNumber;
	/** 交易方向 */
	private TextView tv_tradeCode;
	/** 协议开始日 */
	private TextView tv_agreement_start_date;
	/** 购买触发金额文本 */
	private TextView tv_buyStartCash1;
	/** 购买触发金额回显值 */
	private TextView tv_buyStartCash;
	/** 赎回触发金额文本 */
	private TextView tv_redemptionStartCash1;
	/** 赎回触发金额回显值 */
	private TextView tv_redemptionStartCash;
	/** 投资方式 */
	private LinearLayout inverstStyle_layout;
	/** 单周期投资期限 */
	private LinearLayout singlePeriodLimit_layout;
	/** 购买频率 */
	private LinearLayout buyFrequency_layout;
	/** 赎回频率 */
	private LinearLayout redemptionFrequency_layout;
	/** 下次赎回日期 */
	private LinearLayout nextRedemptionDate_layout;
	/** 协议总期数 */
	private LinearLayout agreementCount_layout;
	/** 当前期数 */
	private LinearLayout agreeIssuePeriod_layout;
	/** 交易方向 */
	private LinearLayout tradeCode_layout;
	/** 协议投资周期 */
	private LinearLayout agreementPeriod_layout;
	/** 下次购买日期 */
	private LinearLayout nextBuyDate_layout;
	/** 协议开始日 */
	private LinearLayout agreementStartDate_layout;
	/** 赎回触发金额 */
	private LinearLayout redemptionStartCash_layout;
	/** 购买触发金额 */
	private LinearLayout buyStartCash_layout;
	/** 协议投资金额 */
	private LinearLayout agreementInverstCash_layout;
	/** 协议代码 */
	private LinearLayout agreementCode_layout;
	/** 币种、投资方式、签约期数、购买触发金额、赎回触发金额,协议类型 */
	private String currency,instType,signNumber,buyStartCash,redemptionStartCash;
	private OnClickListener setRightButtonClickListener = null;
	private String amountRes,minAmountRes,maxAmountRes;
	//交易明细接口上送字段
	private String agrType,custAgrCode;
	private List<Map<String, Object>> mapList;
	/** 存储客户投资协议 查询结果  **/
	private Map<String,Object> itemMap;
	/** 多次赎回协议协议申购金额 动态显示布局 **/
	private LinearLayout ll_sgje;

	private Button btn_more;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		// 为界面标题赋值
		setTitle(this.getString(R.string.boc_invest_agrquerydetail_title_two));
		//		gonerightBtn();
		setText("主界面");
		setRightBtnClick(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (setRightButtonClickListener != null) {
					setRightButtonClickListener.onClick(v);
					return;
				}
				if (PeriodAgreeDetailNewActivity.this.getActivityTaskType() == ActivityTaskType.TwoTask) {
//					ActivityTaskManager.getInstance().removeAllSecondActivity();
//					Intent intent = new Intent();
//					intent.setClass(PeriodAgreeDetailNewActivity.this, SecondMainActivity.class);
//					PeriodAgreeDetailNewActivity.this.startActivity(intent);
					goToMainActivity();
				} else {
//					ActivityTaskManager.getInstance().removeAllActivity();
//					Intent intent = new Intent();
//					intent.setClass(PeriodAgreeDetailNewActivity.this, MainActivity.class);
//					PeriodAgreeDetailNewActivity.this.startActivity(intent);
					goToMainActivity();
				}
			}
		});
		// 添加布局
		view = addView(R.layout.boc_period_agree_detail_new);
		// 界面初始化
		init();
	}

	@SuppressWarnings("unchecked")
	private void init() {
		//		detailMap = BociDataCenter.getInstance().getPeriodDetailMap();
		detailMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.BOCINVT_AGREEMENTINFOQUERY_MAP);
		itemMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.BOCINVT_CAPACITYQUERY_MAP);
		detailMap.put(BocInvt.BOCINVT_BUYRES_PRODCODE_RES, detailMap.get(BocInvt.PROID_RES).toString());
		detailMap.put(BocInvt.BOCINVT_BUYRES_PRODNAME_RES, detailMap.get(BocInvt.PRONAME_RES).toString());
		BociDataCenter.getInstance().setDetailMap(detailMap);
		/** 投资方式 */
		instType = (String) detailMap.get(BocInvt.INSTTYPE_RES);
		//		agrType = (String) detailMap.get(BocInvt.AGRTYPE_RES);
		currency = (String) detailMap.get(BocInvt.PROCUR_RES);
		buyStartCash = (String) detailMap.get(BocInvt.MAXAMOUNT_RES);//购买触发--maxamount
		redemptionStartCash = (String) detailMap.get(BocInvt.MINAMOUNT_RES);//赎回---minamount
		((RadioGroup)findViewById(R.id.radioGroup)).setOnCheckedChangeListener(chechOncliclk);
		/** 协议信息 */
		tv_agreementCode = (TextView) view.findViewById(R.id.tv_agreement_code);
		tv_agreementName = (TextView) view.findViewById(R.id.tv_agreement_name);
		tv_agreementType = (TextView) view.findViewById(R.id.tv_agreement_type);
		tv_inverstStyle = (TextView) view.findViewById(R.id.tv_inverst_style);
		tv_agreementCount = (TextView) view.findViewById(R.id.tv_agreement_count);
		tv_agreeIssuePeriod = (TextView) view.findViewById(R.id.tv_agree_issuePeriod);
		tv_agreementPeriod = (TextView) view.findViewById(R.id.tv_agreement_period);
		tv_minPeriodCount = (TextView) view.findViewById(R.id.tv_min_period_count);
		tv_singlePeriodLimit = (TextView) view.findViewById(R.id.tv_single_period_limit);
		/** 购买频率 */
		tv_buyFrequency = (TextView) view.findViewById(R.id.tv_buy_frequency);
		tv_buyFrequency1 = (TextView) view.findViewById(R.id.tv_buy_frequency1);
		tv_nextBuyDate = (TextView) view.findViewById(R.id.tv_next_buy_date);
		tv_redemptionFrequency1 = (TextView) view.findViewById(R.id.tv_redemption_frequency1);
		tv_redemptionFrequency = (TextView) view.findViewById(R.id.tv_redemption_frequency);//赎回频率
		tv_nextRedemptionDate = (TextView) view.findViewById(R.id.tv_next_redemption_date);
		/** 投资产品信息 */
		tv_inverstProductInfo2 = (TextView) view.findViewById(R.id.tv_inverst_product_info2);
		tv_yearlyRR = (TextView) view.findViewById(R.id.tv_yearlyRR);
		currency1 = (TextView) view.findViewById(R.id.currency1);
		tv_startCash = (TextView) view.findViewById(R.id.agreement_inverst_start_cash);
		startCashTextView = (TextView) view.findViewById(R.id.agreement_inverst_start_cash_text);
		/** 我的投资信息 */
		tv_bocAccno = (TextView) view.findViewById(R.id.boc_accno);
		tv_cashModel = (TextView) view.findViewById(R.id.cashModel);
		tv_agreementInverstCash1 = (TextView) view.findViewById(R.id.tv_agreementInverstCash);
		tv_agreementInverstCash = (TextView) view.findViewById(R.id.agreementInverstCash);
		tv_signNumber1 = (TextView) view.findViewById(R.id.tv_signNumber);
		tv_signNumber = (TextView) view.findViewById(R.id.signNumber);
		tv_executedNumber = (TextView) view.findViewById(R.id.executedNumber);
		tv_residueNumber = (TextView) view.findViewById(R.id.residueNumber);
		tv_tradeCode = (TextView) view.findViewById(R.id.tv_tradeCode);
		tv_agreement_start_date = (TextView) view.findViewById(R.id.tv_agreement_start_date);
		tv_redemptionStartCash1 = (TextView) view.findViewById(R.id.tv_redemptionStartCash1);
		tv_redemptionStartCash = (TextView) view.findViewById(R.id.tv_redemptionStartCash);
		tv_buyStartCash1 = (TextView) view.findViewById(R.id.tv_buyStartCash1);
		tv_buyStartCash = (TextView) view.findViewById(R.id.tv_buyStartCash);
		singlePeriodLimit_layout = (LinearLayout) view.findViewById(R.id.singlePeriodLimit_layout);
		buyFrequency_layout = (LinearLayout) view.findViewById(R.id.buyFrequency_layout);
		redemptionFrequency_layout = (LinearLayout) view.findViewById(R.id.redemptionFrequency_layout);
		nextRedemptionDate_layout = (LinearLayout) view.findViewById(R.id.nextRedemptionDate_layout);
		inverstStyle_layout = (LinearLayout) view.findViewById(R.id.inverstStyle_layout);
		agreementCount_layout = (LinearLayout) view.findViewById(R.id.agreementCount_layout);
		agreeIssuePeriod_layout = (LinearLayout) view.findViewById(R.id.agreeIssuePeriod_layout);
		tradeCode_layout = (LinearLayout) view.findViewById(R.id.tradeCode_layout);
		agreementPeriod_layout = (LinearLayout) view.findViewById(R.id.agreementPeriod_layout);
		nextBuyDate_layout = (LinearLayout) view.findViewById(R.id.nextBuyDate_layout);
		agreementStartDate_layout = (LinearLayout) view.findViewById(R.id.agreementStartDate_layout);
		redemptionStartCash_layout = (LinearLayout) view.findViewById(R.id.redemptionStartCash_layout);
		buyStartCash_layout = (LinearLayout) view.findViewById(R.id.buyStartCash_layout);
		agreementInverstCash_layout = (LinearLayout) view.findViewById(R.id.agreementInverstCash_layout);
		agreementCode_layout = (LinearLayout) view.findViewById(R.id.agreementCode_layout);
		ll_sgje = (LinearLayout) view.findViewById(R.id.ll_sgje);
		//回显 赋值
		tv_agreementCode.setText((String) detailMap
				.get(BocInvt.AGRCODE_RES));
		tv_agreementName.setText((String) detailMap
				.get(BocInvt.AGRNAME_RES));		
		tv_agreementType.setText(LocalData.inverstAgreementTypeStr.get((String) detailMap
				.get(BocInvt.AGRTYPE_RES)));
		tv_inverstStyle.setText(LocalData.inverstTypeStr.get(instType));
		if(!StringUtil.isNullOrEmpty(instType)){
			/** 1:周期连续 ;2:周期不连续;3:多次购买协议;4:多次赎回;5:定时定额投资；6、余额理财投资；7：周期滚赎;8:周期滚续业绩基准*/
			if(instType.equals("1")){
				buyFrequency_layout.setVisibility(View.GONE);
				redemptionFrequency_layout.setVisibility(View.GONE);
				nextRedemptionDate_layout.setVisibility(View.GONE);
				if("1".equals((String) detailMap.get(BocInvt.AMOUNTTYPE_RES))){//0 定额 1不定额
					agreementInverstCash_layout.setVisibility(View.GONE);
					redemptionStartCash_layout.setVisibility(View.VISIBLE);
					buyStartCash_layout.setVisibility(View.VISIBLE);
					tv_redemptionStartCash1.setText(R.string.bocinvt_tv_55);
					tv_buyStartCash1.setText(R.string.bocinvt_tv_56);
				}
			}
			if(instType.equals("2")){
				if("1".equals((String) detailMap.get(BocInvt.AMOUNTTYPE_RES))){//0 定额 1不定额
					agreementInverstCash_layout.setVisibility(View.GONE);
					redemptionStartCash_layout.setVisibility(View.VISIBLE);
					buyStartCash_layout.setVisibility(View.VISIBLE);
					tv_redemptionStartCash1.setText(R.string.bocinvt_tv_55);
					tv_buyStartCash1.setText(R.string.bocinvt_tv_56);
				}
				
			}
			//多次购买协议
			if(instType.equals("3")){
				singlePeriodLimit_layout.setVisibility(View.GONE);
				tv_redemptionFrequency1.setText(R.string.bocinvt_is_redemption);//是否赎回
				if("1".equals((String) detailMap.get(BocInvt.AMOUNTTYPE_RES))){//0 定额 1不定额
					agreementInverstCash_layout.setVisibility(View.GONE);
					redemptionStartCash_layout.setVisibility(View.VISIBLE);
					buyStartCash_layout.setVisibility(View.VISIBLE);
					tv_redemptionStartCash1.setText(R.string.bocinvt_tv_55);
					tv_buyStartCash1.setText(R.string.bocinvt_tv_56);
				}
			}
			//多次赎回
			if(instType.equals("4")){
				singlePeriodLimit_layout.setVisibility(View.GONE);
				tv_agreementInverstCash1.setText(R.string.boc_invest_amountexec_unit);
				tv_buyFrequency1.setText(R.string.bocinvt_is_buy);//是否申购
			}
			//定时定额投资
			if(instType.equals("5")){
				inverstStyle_layout.setVisibility(View.GONE);
				buyFrequency_layout.setVisibility(View.GONE);
				redemptionFrequency_layout.setVisibility(View.GONE);
				if(String.valueOf(detailMap.get(BocInvt.TRADECODE_RES)).equals("0")){//赎回
					tv_agreementInverstCash1.setText(R.string.boc_invest_tzfe);
					nextBuyDate_layout.setVisibility(View.GONE);
					nextRedemptionDate_layout.setVisibility(View.VISIBLE);
				}else if(String.valueOf(detailMap.get(BocInvt.TRADECODE_RES)).equals("1")){
					tv_agreementInverstCash1.setText(R.string.boc_invest_amountexec_two);
					nextBuyDate_layout.setVisibility(View.VISIBLE);
					nextRedemptionDate_layout.setVisibility(View.GONE);
				}
				//				nextRedemptionDate_layout.setVisibility(View.GONE);
				agreementCount_layout.setVisibility(View.GONE);
				agreeIssuePeriod_layout.setVisibility(View.GONE);
				tradeCode_layout.setVisibility(View.VISIBLE);
			}
			//余额理财投资
			if(instType.equals("6")){
				inverstStyle_layout.setVisibility(View.GONE);
				buyFrequency_layout.setVisibility(View.GONE);
				redemptionFrequency_layout.setVisibility(View.GONE);
				nextRedemptionDate_layout.setVisibility(View.GONE);
				agreementCount_layout.setVisibility(View.GONE);
				agreeIssuePeriod_layout.setVisibility(View.GONE);
				singlePeriodLimit_layout.setVisibility(View.GONE);
				agreementPeriod_layout.setVisibility(View.GONE);
				nextBuyDate_layout.setVisibility(View.GONE);
				agreementInverstCash_layout.setVisibility(View.GONE);
				agreementStartDate_layout.setVisibility(View.VISIBLE);
				redemptionStartCash_layout.setVisibility(View.VISIBLE);
				buyStartCash_layout.setVisibility(View.VISIBLE);	
			}
			//周期滚赎
			if(instType.equals("7")){//表内
				inverstStyle_layout.setVisibility(View.GONE);
				buyFrequency_layout.setVisibility(View.GONE);
				redemptionFrequency_layout.setVisibility(View.GONE);
				nextRedemptionDate_layout.setVisibility(View.GONE);
//				tv_buyStartCash1.setText(R.string.minAmount);
//				tv_redemptionStartCash1.setText(R.string.maxAmount);
				if("1".equals((String) detailMap.get(BocInvt.AMOUNTTYPE_RES))){//0 定额 1不定额
					agreementInverstCash_layout.setVisibility(View.GONE);
					redemptionStartCash_layout.setVisibility(View.VISIBLE);
					buyStartCash_layout.setVisibility(View.VISIBLE);
					tv_redemptionStartCash1.setText(R.string.minAmount);
					tv_buyStartCash1.setText(R.string.maxAmount);
				}
			}
			//业绩基准周期滚续
			if(instType.equals("8")){
				inverstStyle_layout.setVisibility(View.GONE);
				buyFrequency_layout.setVisibility(View.GONE);
				redemptionFrequency_layout.setVisibility(View.GONE);
				nextRedemptionDate_layout.setVisibility(View.GONE);
				agreementCount_layout.setVisibility(View.GONE);
				agreeIssuePeriod_layout.setVisibility(View.GONE);
				agreementCode_layout.setVisibility(View.GONE);
//				tv_buyStartCash1.setText(R.string.minAmount);
//				tv_redemptionStartCash1.setText(R.string.maxAmount);
				if("1".equals((String) detailMap.get(BocInvt.AMOUNTTYPE_RES))){//0 定额 1不定额
					agreementInverstCash_layout.setVisibility(View.GONE);
					redemptionStartCash_layout.setVisibility(View.VISIBLE);
					buyStartCash_layout.setVisibility(View.VISIBLE);
					tv_redemptionStartCash1.setText(R.string.minAmount);
					tv_buyStartCash1.setText(R.string.maxAmount);
				}
			}
		}
		/** 协议总期数 */
		String agreementCount = (String) detailMap.get(BocInvt.PERIODTOTAL_RES);
		if(!StringUtil.isNull(agreementCount)){
			if(agreementCount.equals("-000001") || agreementCount.equals("-1")){
				tv_agreementCount.setText(R.string.bocinvt_noperiod);
			}else{
				tv_agreementCount.setText(agreementCount);
			}
		} 
		tv_agreeIssuePeriod.setText((String) detailMap
				.get(BocInvt.PERIOD_RES));
		String periodAge = String.valueOf(detailMap
				.get(BocInvt.PROIDAGE_RES));
		if(periodAge.trim().substring(0,periodAge.trim().length()-1).equals("0")){
			tv_agreementPeriod.setText("-");
		}else{
			tv_agreementPeriod.setText(BocInvestControl.get_d_m_w_y(periodAge));  
		}
		tv_minPeriodCount.setText((String) detailMap
				.get(BocInvt.MININSPERIOD_RES));
		if(!StringUtil.isNullOrEmpty(instType)){
			String oneperiod = String.valueOf(detailMap.get(BocInvt.ONEPERIOD_RES));
			tv_singlePeriodLimit.setText(BocInvestControl.get_d_m_w_y(oneperiod));
		}
		if(instType.equals("4")){//多次赎回
			tv_buyFrequency.setText(LocalData.isBuyStr.get((String) detailMap
					.get(BocInvt.ISNEEDPUR_RES)));
			tv_buyFrequency.setTextColor(getResources().getColor(R.color.red));
		}else{
			String periodpur = String.valueOf(detailMap.get(BocInvt.PERIODPUR_RES));
			if(periodpur.trim().substring(0,periodpur.trim().length()-1).equals("0")){
				tv_buyFrequency.setText("-");
			}else{
				tv_buyFrequency.setText("每"+BocInvestControl.get_d_m_w_y(periodpur)+"申购");
			}
		}
		//下次购买日
		if(StringUtil.isNullOrEmpty(detailMap.get(BocInvt.FIRSTDATEPUR_RES))){
			tv_nextBuyDate.setText("-");
		}else{
			tv_nextBuyDate.setText((String) detailMap
					.get(BocInvt.FIRSTDATEPUR_RES));
		}
		if(instType.equals("3")){//多次购买
			tv_redemptionFrequency.setText(LocalData.isRedemptionStr.get((String) detailMap
					.get(BocInvt.ISNEEDRED_RES)));
			tv_redemptionFrequency.setTextColor(getResources().getColor(R.color.red));
		}else{
			String periodred =  String.valueOf(detailMap.get(BocInvt.PERIODRED_RES));
			if(periodred.trim().substring(0,periodred.trim().length()-1).equals("0")){
				tv_redemptionFrequency.setText("-");
			}else{
				tv_redemptionFrequency.setText("每"+BocInvestControl.get_d_m_w_y(periodred)+"赎回");//赎回周期
			}

		}
		//下次赎回日
		String firstdatered = (String) detailMap.get(BocInvt.FIRSTDATERED_RES); 
		if(firstdatered == null || firstdatered.trim().equals("")){
			tv_nextRedemptionDate.setText("-");
		}else{
			tv_nextRedemptionDate.setText(firstdatered);
		}
		tv_tradeCode.setText(LocalData.inverstTradeTypeStr.get((String) detailMap
				.get(BocInvt.TRADECODE_RES)));
		// 投资产品信息 
		tv_inverstProductInfo2.setText((String) detailMap
				.get(BocInvt.PRONAME_RES));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_inverstProductInfo2);
		//预计年收益率
		tv_yearlyRR.setText(StringUtil.append2Decimals((String) detailMap.get(BocInvt.RATE_RES).toString(), 2)+"%");
		//去掉链接
		//		tv_yearlyRR.setTextColor(getResources().getColor(R.color.blue));
		//		tv_yearlyRR.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		//		tv_yearlyRR.setOnClickListener(new OnClickListener() {
		//			@Override
		//			public void onClick(View v) {
		//				startActivity(new Intent(PeriodAgreeDetailNewActivity.this,ProductQueryAndBuyYearRateActivity.class));
		//			}
		//		});
		currency1.setText(LocalData.Currency.get(currency));
		String startCash = StringUtil.parseStringCodePattern(currency,
				(String) detailMap.get(BocInvt.AGRPURSTART_RES), 2);
		tv_startCash.setText(startCash); 
		//		tv_startCash.setTextColor(getResources().getColor(R.color.red));
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				startCashTextView);
		// 我的投资信息 
		tv_bocAccno.setText(StringUtil.getForSixForString(
				String.valueOf(itemMap.get(
						BocInvt.BOCINVT_CAPACITYQUERY_ACCNO_RES))));
		tv_cashModel.setText(LocalData.bociAmountTypeMap.get((String) detailMap
				.get(BocInvt.AMOUNTTYPE_RES)));
		if(instType.equals("4")){//多次赎回协议
			tv_agreementInverstCash.setText(StringUtil.parseStringPattern(String.valueOf(detailMap.get(BocInvt.UNIT_RES)),2));
			if(((String) detailMap
					.get(BocInvt.ISNEEDPUR_RES)).equals("1")){//不申购----1
				//显示两位小数
				tv_agreementInverstCash.setText(StringUtil.parseStringPattern(String.valueOf(detailMap.get(BocInvt.UNIT_RES)),2));
			}else if(((String) detailMap
					.get(BocInvt.ISNEEDPUR_RES)).equals("0")){//期初申购 ----0
//				amountRes = StringUtil.parseStringCodePattern(currency,
//						(String) detailMap.get(BocInvt.AMOUNT_RES), 2);
				amountRes = StringUtil.parseStringPattern(
						(String) detailMap.get(BocInvt.AMOUNT_RES), 2);
//				tv_agreementInverstCash.setText(amountRes);//协议赎回份额 固定显示成两位小数
				/**动态显示协议申购金额**/
				ll_sgje.setVisibility(View.VISIBLE);
				TextView tv_agrsgjevalue = (TextView) view.findViewById(R.id.agrsgjevalue);
				amountRes = StringUtil.parseStringCodePattern(currency,
						(String) detailMap.get(BocInvt.AMOUNT_RES), 2);
				tv_agrsgjevalue.setText(amountRes);//协议申购金额 需要日元格式化显示
			}
//		}else if(instType.equals("5") && String.valueOf(detailMap.get(BocInvt.TRADECODE_RES)).equals("0")
//				&& (String.valueOf(detailMap.get(BocInvt.AMOUNT_RES)).equals("-1.00") 
//						|| String.valueOf(detailMap.get(BocInvt.AMOUNT_RES)).equals("-1"))){//定时定额赎回
//			tv_agreementInverstCash.setText("全额赎回");
//		}
		}else if(instType.equals("5")&& String.valueOf(detailMap.get(BocInvt.TRADECODE_RES)).equals("0")){//定时定额赎回
				if(String.valueOf(detailMap.get(BocInvt.AMOUNT_RES)).equals("-1.00") 
						|| String.valueOf(detailMap.get(BocInvt.AMOUNT_RES)).equals("-1")){
					tv_agreementInverstCash.setText("全额赎回");
				}else{
					amountRes = StringUtil.parseStringPattern(
							(String) detailMap.get(BocInvt.AMOUNT_RES), 2);
					tv_agreementInverstCash.setText(amountRes);
				}
		}else{//非定时定额赎回、非多次赎回
			amountRes = StringUtil.parseStringCodePattern(currency,
					(String) detailMap.get(BocInvt.AMOUNT_RES), 2);
			tv_agreementInverstCash.setText(amountRes);
		}
		minAmountRes = StringUtil.parseStringCodePattern(currency,
				(String) detailMap.get(BocInvt.MINAMOUNT_RES), 2);
		maxAmountRes = StringUtil.parseStringCodePattern(currency,
				(String) detailMap.get(BocInvt.MAXAMOUNT_RES), 2);
		//		tv_agreementInverstCash.setText(amountRes);
		/**取消页面金额红色显示**/
		//		tv_agreementInverstCash.setTextColor(getResources().getColor(R.color.red));
		tv_buyStartCash.setText(maxAmountRes);
		//		tv_buyStartCash.setTextColor(getResources().getColor(R.color.red));
		tv_redemptionStartCash.setText(minAmountRes);
		//		tv_redemptionStartCash.setTextColor(getResources().getColor(R.color.red));
		signNumber = (String) detailMap.get(BocInvt.BUYPERIOD_RES);
		if(!StringUtil.isNull(signNumber)){
			if(signNumber.equals("-000001") || signNumber.equals("-1")){
				tv_signNumber.setText(R.string.bocinvt_noperiod);
			}else{
				tv_signNumber.setText(signNumber);
			}
		} 
		tv_executedNumber.setText((String) detailMap
				.get(BocInvt.FINISHPERIOD_RES));
		String remaindperiod = String.valueOf( detailMap.get(BocInvt.REMAINDPERIOD_RES));
		if(!StringUtil.isNull(remaindperiod)){
			if(remaindperiod.equals("-000001") || remaindperiod.equals("-1")){
				tv_residueNumber.setText(R.string.bocinvt_noperiod);
			}else{
				tv_residueNumber.setText(remaindperiod);
			}
		}
		//		tv_residueNumber.setText((String) detailMap
		//				.get(BocInvt.REMAINDPERIOD_RES));
		tv_agreement_start_date.setText((String)detailMap.get(BocInvt.FIRSTDATEPUR_RES));
		Button btn_detailQuery = (Button) view.findViewById(R.id.btn_detailQuery);
		btn_detailQuery.setOnClickListener(this);
		btn_more = (Button) view.findViewById(R.id.btn_more);
		List<String> serlist = new ArrayList<String>();
		// 是否可修改
		String canUpdate = (String) detailMap.get(BocInvt.CANUPDATE_RES);
		// 是否可终止
		String canCancel = (String) detailMap.get(BocInvt.CANCANCLE_RES);
		//是否为业绩基准产品
		String isbenchmark = (String) detailMap.get(BocInvt.ISBENCHMARK_RES);
		if (!StringUtil.isNull(canUpdate)) {
			if (canUpdate.equals(investTypeSubList.get(1))) {
				serlist.add(bocInverstList.get(0));
			}
		}
		if (!StringUtil.isNull(canCancel)) {
			if (canCancel.equals(investTypeSubList.get(1))) {
				serlist.add(bocInverstList.get(1));
			}
		}
		if (!StringUtil.isNull(isbenchmark)) {
			if (isbenchmark.equals(investTypeSubList.get(1))) {
				agreementCode_layout.setVisibility(View.GONE);
				agreementCount_layout.setVisibility(View.GONE);
				agreeIssuePeriod_layout.setVisibility(View.GONE);
			}
		}
		if (serlist == null || serlist.size() == 0) {
			btn_more.setVisibility(View.GONE);
		} else if (serlist.size() == 1) {
			btn_more.setVisibility(View.VISIBLE);
			btn_more.setText(serlist.get(0));
			btn_more.setOnClickListener(this);
		}else{
			btn_more.setVisibility(View.VISIBLE);
			String[] service = new String[serlist.size()];
			for (int k = 0; k < serlist.size(); k++) {
				service[k] = serlist.get(k);
			}
			PopupWindowUtils.getInstance().setshowMoreChooseUpListener(
					BaseDroidApp.getInstanse().getCurrentAct(), btn_more,
					service, btnMoreNullBocinvtClick);
		}	
	}

	/** 更多点击事件 */
	final OnClickListener btnMoreNullBocinvtClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Button tv = (Button) v;
			String text = tv.getText().toString();
			moreClick(text, v);
		}
	};

	/** 判断点击事件 */
	public void moreClick(String text, View v) {
		if (text.equals(bocInverstList.get(0))) {
			/** 修改投资信息**/ 
			Intent intent = new Intent(PeriodAgreeDetailNewActivity.this, InvestAgreementModifyActivity.class);
			startActivityForResult(intent, 100);
		}
		if (text.equals(bocInverstList.get(1))) {
			/**终止投资协议**/
			Intent intent = new Intent(PeriodAgreeDetailNewActivity.this, InvestAgreementCancelActivity.class);
			//			startActivityForResult(intent, 101);
			startActivity(intent);

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//投资明细查询
		case R.id.btn_detailQuery:
			requestPsnXpadCapacityTransList();
			break;
		case R.id.btn_more:
			if("修改投资信息".equals(btn_more.getText().toString())){//修改
				Intent intent = new Intent(PeriodAgreeDetailNewActivity.this, InvestAgreementModifyActivity.class);
				startActivityForResult(intent, 100);
			}else if("终止投资协议".equals(btn_more.getText().toString())){//终止
				Intent intent = new Intent(PeriodAgreeDetailNewActivity.this, InvestAgreementCancelActivity.class);
				startActivity(intent);
			}
			break;
		}
	}
	/** 请求投资明细 */
	@SuppressWarnings("unchecked")
	public void requestPsnXpadCapacityTransList() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.PSNXPADCAPACITYTRANALIST);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> params = new HashMap<String, String>();
		itemMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.BOCINVT_CAPACITYQUERY_MAP);
		agrType = String.valueOf(itemMap.get(BocInvt.BOCINVT_CAPACITYQUERY_AGRTYPE_RES));
		custAgrCode = String.valueOf(itemMap.get(BocInvt.BOCINVT_CAPACITYQUERY_CUSTAGECODE_RES));
		params.put(BocInvt.BOCINVT_TRANALIST_CUSTAGRCODE_REQ, custAgrCode);
		params.put(BocInvt.BOCINVT_TRANALIST_AGRTYPE_REQ, agrType);
		biiRequestBody.setParams(params);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnXpadCapacityTransListCallBack");
	}

	/**
	 * 请求投资明细回调
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnXpadCapacityTransListCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		//		Map<String, Object> map = (Map<String, Object>) BocinvtUtils.httpResponseDeal(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		/**** 返回字段无list ****/
		mapList =  (List<Map<String, Object>>) biiResponseBody.getResult();
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.BOCINVT_CAPACITYTRANSLIST_MAP, mapList);
		//		List<Map<String, Object>> list = (List<Map<String, Object>>) map.get(BocInvt.BOCINVT_LIST_RES);
		/**** 返回字段有list ****/
		//		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		//		if(StringUtil.isNullOrEmpty(resultMap)) return;
		//		final List<Map<String, Object>> mapList = (List<Map<String, Object>>) resultMap.get(BocInvt.BOCINVT_LIST_RES);

		if (StringUtil.isNullOrEmpty(mapList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					this.getString(R.string.acc_transferquery_null));
			return;
		}else{//有数据 跳转页面
			Intent intent = new Intent(PeriodAgreeDetailNewActivity.this, InverstDetailQueryActivity.class);
			intent.putExtra("currency", currency);
			startActivity(intent);
		}
	}

	private OnCheckedChangeListener chechOncliclk = new OnCheckedChangeListener(){
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			/** 协议信息 */
			case R.id.btn1:
				findViewById(R.id.agreementInfo_layout).setVisibility(View.VISIBLE);
				findViewById(R.id.inverstInfo_layout).setVisibility(View.GONE);
				findViewById(R.id.productInfo_layout).setVisibility(View.GONE);
				break;
				/** 投资产品信息 */	
			case R.id.btn2:
				findViewById(R.id.agreementInfo_layout).setVisibility(View.GONE);
				findViewById(R.id.inverstInfo_layout).setVisibility(View.GONE);
				findViewById(R.id.productInfo_layout).setVisibility(View.VISIBLE);
				break;
				/** 我的投资信息 */
			case R.id.btn3:
				findViewById(R.id.agreementInfo_layout).setVisibility(View.GONE);
				findViewById(R.id.inverstInfo_layout).setVisibility(View.VISIBLE);
				findViewById(R.id.productInfo_layout).setVisibility(View.GONE);
				break;
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			//			setResult(RESULT_OK);
			finish();
			break;
		case 1001:
			//得到Activity关闭后返回的数据并重新赋值
			String signNumberResult = data.getExtras().getString("mBuyPeriod");
			String amountResult = data.getExtras().getString("mAmount"); 
			String buyStartCashResult = data.getExtras().getString("mMaxAmount");
			String redemptionStartCashResult = data.getExtras().getString("mMinAmount");
			//新投资期数
			if(!signNumber.equals(signNumberResult)){
				tv_signNumber1.setText(R.string.bocinvt_new_period_count);
				tv_signNumber.setText(signNumberResult);
			}
			//新投资金额   ---定额
			if(!amountRes.equals(amountResult)){
				if(instType.equals("5")){//定时定额
					if(String.valueOf(detailMap.get(BocInvt.TRADECODE_RES)).equals("0")){//赎回
						tv_agreementInverstCash1.setText(R.string.boc_invest_tzfenew);
					}else if(String.valueOf(detailMap.get(BocInvt.TRADECODE_RES)).equals("1")){//购买
						tv_agreementInverstCash1.setText(R.string.boc_invest_amountexecnew_two);
					}
					tv_agreementInverstCash.setText(StringUtil.parseStringCodePattern(currency, amountResult, 2));
					tv_agreementInverstCash.setTextColor(getResources().getColor(R.color.red));
				}
			}
			//新购买触发金额   ---不定额
			if(!buyStartCash.equals(redemptionStartCashResult)){
				tv_buyStartCash1.setText(R.string.bocinvt_buy_start_cash_new);
				tv_buyStartCash.setText(StringUtil.parseStringCodePattern(currency,redemptionStartCashResult, 2));
				//				tv_buyStartCash.setText(StringUtil.parseStringCodePattern(currency,buyStartCashResult, 2));
			}
			//新赎回触发金额   ---不定额
			if(!redemptionStartCash.equals(buyStartCashResult)){
				tv_redemptionStartCash1.setText(R.string.bocinvt_redemption_start_cash_new);
				tv_redemptionStartCash.setText(StringUtil.parseStringCodePattern(currency,buyStartCashResult, 2));
				//				tv_redemptionStartCash.setText(StringUtil.parseStringCodePattern(currency,redemptionStartCashResult, 2));
			}
			break;
		default:
			break;
		}
	}
}
