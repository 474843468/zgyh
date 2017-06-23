package com.chinamworld.bocmbci.biz.bocinvt_p603.inverstagreemanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvestControl;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.userwidget.LabelTextView;
import com.chinamworld.bocmbci.userwidget.LabelTextView.TextColor;
import com.chinamworld.bocmbci.userwidget.TitleAndContentLayout;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 中银理财 投资协议管理 失效协议详细信息页面
 * @author linyl
 *
 */
public class InvestInvalidAgreeQueryDetailAvtivity extends BocInvtBaseActivity implements OnClickListener{

	public static final String TAG = "InvestInvalidAgreeQueryDetailAvtivity";
	/**失效协议详情详细信息**/
	private TitleAndContentLayout mAgreeDetailInfo;
	/**查看投资协议明细按钮**/
	private Button mQueryBtnAgreeListInfo;
	/**后台返回投资方式**/
	private int instTypeRes;
	/** 存储客户投资协议 查询结果  **/
	private Map<String,Object> itemMap;
	/** 存储客户投资协议详情 查询结果 **/
	private Map<String,Object> agrInfoQueryMap;
	/** 存储协议详情详细的变量 用于给页面字段赋值 **/
	private String mAgrName,mAgrCode,mAgrType,mPeriodAge,mProName,mRate,mAgrPurstart,mAmountType,
	mBuyPeriod,mFinishperiod,mAmount,mOneperiod,mIsneedpur,mIsneedred,mTradeCode,
	mMinAmount,mMaxAmount,mAccNo,mAgrmemo,mFirstdatepur,proCurText;
	//交易明细接口上送字段
	private String agrType,custAgrCode;
	private List<Map<String, Object>> mapList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		getBackgroundLayout().setTitleText(R.string.boc_invest_agrquerydetail_title_two);
		getBackgroundLayout().setRightButtonText("主界面");
		setContentView(R.layout.boc_invest_agree_querydetail_layout);
		init();
	}
	/**
	 * 初始化View
	 */
	@SuppressWarnings("unchecked")
	private void init(){
		mAgreeDetailInfo = (TitleAndContentLayout) findViewById(R.id.titleAndContentLayout_detailinfo);
		mQueryBtnAgreeListInfo = (Button) findViewById(R.id.boci_agree_touzi_list);
		itemMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.BOCINVT_CAPACITYQUERY_MAP);
		agrInfoQueryMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.BOCINVT_AGREEMENTINFOQUERY_MAP);
		getAgrInfoQueryMapValue();
		getBackgroundLayout().setTitleText(R.string.boc_invest_agrquerydetail_title_two);
		mAgreeDetailInfo.setTitleVisibility(View.GONE);
		mQueryBtnAgreeListInfo.setOnClickListener(this);
		initAgreeDetailInfoView(instTypeRes);
	}
	
	/**
	 * 获取币种
	 * @return
	 */
	private String getProCurText(){
		proCurText = String.valueOf(itemMap.get(BocInvt.BOCINVT_CAPACITYQUERY_PROCUR_RES));
		if (!StringUtil.isNull(LocalData.Currency.get(proCurText))) {
			return LocalData.Currency.get(proCurText);
		}
		return null;
	}
	/**
	 * 赋值 用于页面展示   获取协议详细信息内容
	 */
	private void getAgrInfoQueryMapValue(){
		mAgrCode = String.valueOf(agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_AGRCODE_RES));//协议代码
		mAgrName = String.valueOf(agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_AGRNAME_RES));//协议名称
		mAgrType = LocalData.bocInvestAgrTypeRes.get(String.valueOf(
				agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_AGRTYPE_RES)));//协议类型
		instTypeRes = Integer.valueOf(String.valueOf(
				agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_INSTTYPE_RES)));//投资方式
		String periodAge = String.valueOf(agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_PERIODAGE_RES));
		mPeriodAge = BocInvestControl.get_d_m_w_y(periodAge);//投资周期
		mProName = String.valueOf(agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_PRONAME_RES));//产品名称
		mRate = StringUtil.append2Decimals(String.valueOf(agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_RATE_RES)), 2) + "%";//预计年收益率
		mAgrPurstart = String.valueOf(agrInfoQueryMap.get(
				BocInvt.BOCINVT_AGRINFOQUERY_AGRPURSTART_RES));//协议投资投资金额
		mAmountType = LocalData.bociAmountTypeMap.get(String.valueOf(
				agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_AMOUNTTYPE_RES)));//金额模式
		mAmount = String.valueOf(agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_AMOUNT_RES));
		String mBuyPeriodRes = String.valueOf(agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_BUYPERIOD_RES));//签约期数
		if(!StringUtil.isNull(mBuyPeriodRes)){
			if(mBuyPeriodRes.equals("-000001") || mBuyPeriodRes.equals("-1")){
				mBuyPeriod = "不限期";
			}else{
				mBuyPeriod = mBuyPeriodRes;
			}
		} 
		mFinishperiod = String.valueOf(agrInfoQueryMap.get(
				BocInvt.BOCINVT_AGRINFOQUERY_FINISHPERIOD_RES));//已执行期数
		String mOneperiodRes = String.valueOf(agrInfoQueryMap.get(
				BocInvt.BOCINVT_AGRINFOQUERY_ONEPERIOD_RES));//单周期投资期限
		mOneperiod = BocInvestControl.get_d_m_w_y(mOneperiodRes);
		if(instTypeRes == 3){//多次购买
			String isneedpur = String.valueOf(agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_PERIODPUR_RES));
			if(isneedpur.trim().substring(0,isneedpur.trim().length()-1).equals("0")){
				mIsneedpur = "-";
			}else{
				mIsneedpur = "每"+BocInvestControl.get_d_m_w_y(isneedpur)+"申购";//购买周期
			}
			mIsneedred = LocalData.bocInvestIsneedredResTwo.get(String.valueOf(agrInfoQueryMap.get(
					BocInvt.BOCINVT_AGRINFOQUERY_ISNEEDRED_RES)));//赎回频率
		}else if(instTypeRes == 4){//多次赎回
//			mIsneedpur = LocalData.bocInvestIsneedpurRes.get(String.valueOf(agrInfoQueryMap.get(
//					BocInvt.BOCINVT_AGRINFOQUERY_ISNEEDPUR_RES)));//购买频率
//			mIsneedred = LocalData.bocInvestIsneedredRes.get(String.valueOf(agrInfoQueryMap.get(
//					BocInvt.BOCINVT_AGRINFOQUERY_ISNEEDRED_RES)));//赎回频率
			mIsneedpur = LocalData.bocInvestIsneedpurResTwo.get(String.valueOf(agrInfoQueryMap.get(
					BocInvt.BOCINVT_AGRINFOQUERY_ISNEEDPUR_RES)));//购买频率
			String isneedred = String.valueOf(agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_PERIODRED_RES));
			if(isneedred.trim().substring(0,isneedred.trim().length()-1).equals("0")){
				mIsneedred = "-";
			}else{
				mIsneedred = "每"+ BocInvestControl.get_d_m_w_y(isneedred)+"赎回";//赎回频率
			}
		}
		mTradeCode = LocalData.bocInvestTradeCodeRes.get(String.valueOf(agrInfoQueryMap.get(
				BocInvt.BOCINVT_AGRINFOQUERY_TRADECODE_RES)));//交易方向
		mMinAmount = String.valueOf(agrInfoQueryMap.get(
				BocInvt.BOCINVT_AGRINFOQUERY_MINANOUNT_RES));//购买触发
		mMaxAmount = String.valueOf(agrInfoQueryMap.get(
				BocInvt.BOCINVT_AGRINFOQUERY_MAXAMOUNT_RES));//赎回触发
		mAccNo = StringUtil.getForSixForString(
				String.valueOf(itemMap.get(
						BocInvt.BOCINVT_CAPACITYQUERY_ACCNO_RES)));//理财交易账户
		if(StringUtil.isNullOrEmpty(itemMap.get(BocInvt.BOCINVT_CAPACITYQUERY_MEMO_RES))){
			mAgrmemo = "-";
		}else{
			mAgrmemo = String.valueOf(
					itemMap.get(BocInvt.BOCINVT_CAPACITYQUERY_MEMO_RES));//失效原因
		}
		mFirstdatepur = String.valueOf(agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_FIRSTDATEPUR_RES));
	}

	/**
	 * 初始化协议详情详细信息页面
	 * 与业务确认 取消详情页面的金额红色显示（TextColor.Red）
	 */
	public void initAgreeDetailInfoView(int instType){
		switch (instType) {
		case 1://周期连续协议
			initViewContinuousCycle();
			break;
		case 2://周期不连续协议
			initViewNoContinuousCycle();
			break;
		case 3://多次购买协议
			initViewRepeatBuy();
			break;
		case 4://多次赎回协议
			initViewRepeatRansom();
			break;
		case 5://定时定额投资
			initViewTimeFixed();
			break;
		case 6://余额理财投资
			initViewBalanceMoney();
			break;
		case 7://周期滚续协议
		case 8://业绩基准周期滚续
			initViewContinueCycle();
			break;
		default:
			break;
		}
	}

	/**
	 * 失效协议详情 -->周期连续
	 */
	private void initViewContinuousCycle(){
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrinfo_title1," ","1:0.8"));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrcode, mAgrCode));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrname_two, mAgrName));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrtype, mAgrType));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_incest_insttype, LocalData.inverstTypeStr.get(String.valueOf(instTypeRes))));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_periodage, mPeriodAge));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrinfo_title2," ","1:0.6"));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_proname, mProName));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_procur_two, getProCurText()));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_yjrate_year, mRate));
		mAgrPurstart = StringUtil.parseStringCodePattern(proCurText,mAgrPurstart,2);
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrpurstart, mAgrPurstart));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrinfo_title3," ","1:0.6"));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_accno,mAccNo));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_amounttype, mAmountType));
		mAmount = StringUtil.parseStringCodePattern(proCurText,mAmount,2);
		mMinAmount = StringUtil.parseStringCodePattern(proCurText,mMinAmount,2);
		mMaxAmount = StringUtil.parseStringCodePattern(proCurText,mMaxAmount,2);
		if(String.valueOf(
				agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_AMOUNTTYPE_RES)).equals("0")){//定额
			    mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_amountexec, mAmount));//单期购买金额
		}else if(String.valueOf(
				agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_AMOUNTTYPE_RES)).equals("1")){//不定额
			    mAgreeDetailInfo.addView(createLabelTextView(R.string.bocinvt_tv_55, mMinAmount));//账户保留余额
			    mAgreeDetailInfo.addView(createLabelTextView(R.string.bocinvt_tv_56, mMaxAmount));//最大购买金额
		}
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_buyperiod, mBuyPeriod));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_finishperiod, mFinishperiod));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrmemo_two,mAgrmemo));
	}

	/**
	 * 失效协议详情-->周期不连续协议
	 */
	private void initViewNoContinuousCycle(){
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrinfo_title1," ","1:0.8"));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrcode, mAgrCode));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrname_two, mAgrName));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrtype, mAgrType));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_incest_insttype, LocalData.inverstTypeStr.get(String.valueOf(instTypeRes))));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_periodage, mPeriodAge));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_oneperiod, mOneperiod));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrinfo_title2," ","1:0.6"));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_proname, mProName));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_procur_two, getProCurText()));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_yjrate_year, mRate));
		mAgrPurstart = StringUtil.parseStringCodePattern(proCurText,mAgrPurstart,2);
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrpurstart, mAgrPurstart));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrinfo_title3," ","1:0.6"));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_accno,mAccNo));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_amounttype, mAmountType));
		mAmount = StringUtil.parseStringCodePattern(proCurText,mAmount,2);
		mMinAmount = StringUtil.parseStringCodePattern(proCurText,mMinAmount,2);
		mMaxAmount = StringUtil.parseStringCodePattern(proCurText,mMaxAmount,2);
		if(String.valueOf(
				agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_AMOUNTTYPE_RES)).equals("0")){//定额
			    mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_amountexec, mAmount));//单期购买金额
		}else if(String.valueOf(
				agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_AMOUNTTYPE_RES)).equals("1")){//不定额
			    mAgreeDetailInfo.addView(createLabelTextView(R.string.bocinvt_tv_55, mMinAmount));//账户保留余额
			    mAgreeDetailInfo.addView(createLabelTextView(R.string.bocinvt_tv_56, mMaxAmount));//最大购买金额
		}
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_buyperiod, mBuyPeriod));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_finishperiod, mFinishperiod));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrmemo_two,mAgrmemo));
	}

	/**
	 * 失效协议详情-->多次购买协议
	 */
	private void initViewRepeatBuy(){
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrinfo_title1," ","1:0.8"));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrcode, mAgrCode));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrname_two, mAgrName));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrtype, mAgrType));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_incest_insttype, LocalData.inverstTypeStr.get(String.valueOf(instTypeRes))));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_periodage, mPeriodAge));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_isneedpur, mIsneedpur));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_isneedred_two, mIsneedred));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrinfo_title2," ","1:0.6"));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_proname, mProName));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_procur_two, getProCurText()));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_yjrate_year, mRate));
		mAgrPurstart = StringUtil.parseStringCodePattern(proCurText,mAgrPurstart,2);
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrpurstart, mAgrPurstart));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrinfo_title3," ","1:0.6"));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_accno,mAccNo));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_amounttype, mAmountType));
		mAmount = StringUtil.parseStringCodePattern(proCurText,mAmount,2);
		mMinAmount = StringUtil.parseStringCodePattern(proCurText,mMinAmount,2);
		mMaxAmount = StringUtil.parseStringCodePattern(proCurText,mMaxAmount,2);
		if(String.valueOf(
				agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_AMOUNTTYPE_RES)).equals("0")){//定额
			    mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_amountexec, mAmount));//单期购买金额
		}else if(String.valueOf(
				agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_AMOUNTTYPE_RES)).equals("1")){//不定额
			    mAgreeDetailInfo.addView(createLabelTextView(R.string.bocinvt_tv_55, mMinAmount));//账户保留余额
			    mAgreeDetailInfo.addView(createLabelTextView(R.string.bocinvt_tv_56, mMaxAmount));//最大购买金额
		}
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_buyperiod, mBuyPeriod));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_finishperiod, mFinishperiod));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrmemo_two,mAgrmemo));
	}

	/**
	 * 失效协议详情-->多次赎回协议
	 */
	private void initViewRepeatRansom(){
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrinfo_title1," ","1:0.8"));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrcode, mAgrCode));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrname_two, mAgrName));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrtype, mAgrType));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_incest_insttype, LocalData.inverstTypeStr.get(String.valueOf(instTypeRes))));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_periodage, mPeriodAge));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_isneedpur_two, mIsneedpur));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_isneedred, mIsneedred));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrinfo_title2," ","1:0.6"));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_proname, mProName));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_procur_two, getProCurText()));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_yjrate_year, mRate));
		mAgrPurstart = StringUtil.parseStringCodePattern(proCurText,mAgrPurstart,2);
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrpurstart, mAgrPurstart));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrinfo_title3," ","1:0.6"));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_accno,mAccNo));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_amounttype, mAmountType));
		if(String.valueOf(agrInfoQueryMap.get(
				BocInvt.BOCINVT_AGRINFOQUERY_ISNEEDPUR_RES)).equals("0")){//期初申购
			mAmount = StringUtil.parseStringPattern(mAmount, 2);
//			mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_amountexec_unit, mAmount));//协议赎回份额---取消日元格式化
			mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_amountexec_unit, StringUtil.parseStringPattern(String.valueOf(agrInfoQueryMap.get(
					BocInvt.BOCINVT_AGRINFOQUERY_UNIT_RES)),2)));//赎回份额
		}else if(String.valueOf(agrInfoQueryMap.get(
				BocInvt.BOCINVT_AGRINFOQUERY_ISNEEDPUR_RES)).equals("1")){//不申购
			mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_amountexec_unit, StringUtil.parseStringPattern(String.valueOf(agrInfoQueryMap.get(
					BocInvt.BOCINVT_AGRINFOQUERY_UNIT_RES)),2)));//赎回份额
		}
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_buyperiod, mBuyPeriod));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_finishperiod, mFinishperiod));
		if(String.valueOf(agrInfoQueryMap.get(
				BocInvt.BOCINVT_AGRINFOQUERY_ISNEEDPUR_RES)).equals("0")){//动态显示  期初申购时展示  取amount
			mAmount = StringUtil.parseStringCodePattern(proCurText,String.valueOf(agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_AMOUNT_RES)),2);
			mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrsgamount, mAmount));//协议申购金额
		}
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrmemo_two,mAgrmemo));
	}

	/**
	 * 失效协议详情 --> 定时定额投资协议 
	 */
	private void initViewTimeFixed(){
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrinfo_title1," ","1:0.8"));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrcode, mAgrCode));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrname_two, mAgrName));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrtype, mAgrType));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_tradeCode, mTradeCode));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_periodage, mPeriodAge));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrinfo_title2," ","1:0.6"));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_proname, mProName));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_procur_two, getProCurText()));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_yjrate_year, mRate));
		mAgrPurstart = StringUtil.parseStringCodePattern(proCurText,mAgrPurstart,2);
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrpurstart, mAgrPurstart));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrinfo_title3," ","1:0.6"));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_accno,mAccNo));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_amounttype, mAmountType));
		if(String.valueOf(agrInfoQueryMap.get(
				BocInvt.BOCINVT_AGRINFOQUERY_TRADECODE_RES)).equals("0")){//赎回---份额
			if(mAmount.equals("-1.00") || mAmount.equals("-1")){//后台返回-1 代表全额赎回 //日元币种 -1
				mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_tzfe, "全额赎回"));
			}else{
//				mAmount = StringUtil.parseStringCodePattern(proCurText,mAmount,2);
				mAmount = StringUtil.parseStringPattern(mAmount, 2);//为0 时 回显 0.00
				mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_tzfe, mAmount));//协议份额 取消日元格式化的
			}
		}else if(String.valueOf(agrInfoQueryMap.get(
				BocInvt.BOCINVT_AGRINFOQUERY_TRADECODE_RES)).equals("1")){//购买---金额
			mAmount = StringUtil.parseStringCodePattern(proCurText,mAmount,2);
			mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_amountexec_two, mAmount));//单期购买金额
		}
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_buyperiod, mBuyPeriod));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_finishperiod, mFinishperiod));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrmemo_two,mAgrmemo));
	}

	/**
	 * 失效协议详情-->余额理财协议
	 */
	private void initViewBalanceMoney(){
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrinfo_title1," ","1:0.8"));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrcode, mAgrCode));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrname_two, mAgrName));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrtype, mAgrType));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrstartdate1, mFirstdatepur));//下次购买日
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrinfo_title2," ","1:0.6"));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_proname, mProName));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_procur_two, getProCurText()));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_yjrate_year, mRate));
		mAgrPurstart = StringUtil.parseStringCodePattern(proCurText,mAgrPurstart,2);
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrpurstart, mAgrPurstart));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrinfo_title3," ","1:0.6"));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_accno,mAccNo));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_amounttype, mAmountType));
		mMaxAmount = StringUtil.parseStringCodePattern(proCurText,mMaxAmount,2);
		mMinAmount = StringUtil.parseStringCodePattern(proCurText,mMinAmount,2);
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_maxamount, mMinAmount));//赎回触发
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_minamount, mMaxAmount));//购买触发
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_buyperiod, mBuyPeriod));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_finishperiod, mFinishperiod));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrmemo_two,mAgrmemo));
	}

	/**
	 * 失效协议详情-->周期滚续协议
	 */
	private void initViewContinueCycle(){
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrinfo_title1," ","1:0.8"));
		if(instTypeRes != 8){//周期滚续 业绩基准无需加载协议代码   
			mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrcode, mAgrCode));
		}
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrname_two, mAgrName));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrtype, mAgrType));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_periodage, mPeriodAge));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrinfo_title2," ","1:0.6"));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_proname, mProName));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_procur_two, getProCurText()));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_yjrate_year, mRate));
		mAgrPurstart = StringUtil.parseStringCodePattern(proCurText,mAgrPurstart,2);
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrpurstart, mAgrPurstart));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrinfo_title3," ","1:0.6"));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_accno,mAccNo));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_amounttype, mAmountType));
		mAmount = StringUtil.parseStringCodePattern(proCurText,mAmount,2);
		mMinAmount = StringUtil.parseStringCodePattern(proCurText,mMinAmount,2);
		mMaxAmount = StringUtil.parseStringCodePattern(proCurText,mMaxAmount,2);
		if(String.valueOf(
				agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_AMOUNTTYPE_RES)).equals("0")){//定额
			    mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_amountexec, mAmount));//单期购买金额
		}else if(String.valueOf(
				agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_AMOUNTTYPE_RES)).equals("1")){//不定额
			    mAgreeDetailInfo.addView(createLabelTextView(R.string.minAmount, mMinAmount));//最低预留金额
			    mAgreeDetailInfo.addView(createLabelTextView(R.string.maxAmount, mMaxAmount));//最大扣款金额
		}
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_buyperiod, mBuyPeriod));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_finishperiod, mFinishperiod));
		mAgreeDetailInfo.addView(createLabelTextView(R.string.boc_invest_agrmemo_two,mAgrmemo));
	}

	/**
	 * 创建LabelTextView控件
	 * @param resid
	 * @param valueText
	 * @param obj
	 * @return
	 */
	private LabelTextView createLabelTextView(int resid, String valueText,Object... obj){
		LabelTextView v = new LabelTextView(this);
		v.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		v.setLabelText(resid);
		v.setValueText(valueText);
		if(obj != null && obj.length > 0){
			if(obj[0] instanceof String){//设置控件weight
				v.setWeightShowRate((String)obj[0]);
				v.setTextViewBackground(R.color.whitefornet);
			}
			if(obj[0] instanceof TextColor){//设置字体颜色
				v.setValueTextColor((TextColor)obj[0]);
			}
		}
		return v;
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
				Intent intent = new Intent(InvestInvalidAgreeQueryDetailAvtivity.this, InverstDetailQueryActivity.class);
				intent.putExtra(BocInvt.BOCINVT_AGRINFOQUERY_AGRTYPE_RES, String.valueOf(
						agrInfoQueryMap.get(BocInvt.BOCINVT_AGRINFOQUERY_AGRTYPE_RES)));
				startActivity(intent);
		}
	}

	
	/**
	 * 协议详情 查看详情信息的按钮点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.boci_agree_touzi_list:
			requestPsnXpadCapacityTransList();
			break;

		default:
			break;
		}

	}
	

}
