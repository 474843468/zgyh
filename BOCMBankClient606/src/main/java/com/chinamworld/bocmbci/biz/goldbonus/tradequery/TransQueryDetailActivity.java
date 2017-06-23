package com.chinamworld.bocmbci.biz.goldbonus.tradequery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.goldbonus.GoldBonusBaseActivity;
import com.chinamworld.bocmbci.biz.goldbonus.GoldbonusLocalData;
import com.chinamworld.bocmbci.constant.DictionaryData;
import com.chinamworld.bocmbci.userwidget.TitleAndContentLayout;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.Map;

/**
 * 贵金属积利金交易详情页面
 * @author linyl
 *
 */
public class TransQueryDetailActivity extends GoldBonusBaseActivity {
	
	private TitleAndContentLayout containLayout ;
	private Map<String, Object> detailMap;
	/**交易类型**/
	private String saleType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		View view = LayoutInflater.from(this).inflate(R.layout.goldbonus_traquerydetail_layout, null);
		this.getBackgroundLayout().addView(view);
		getBackgroundLayout().setTitleNewText(R.string.prms_title_query);
		containLayout = (TitleAndContentLayout) view.findViewById(R.id.titleAndContentLayout);
		containLayout.setTitleText("详情信息");
		detailMap = GoldbonusLocalData.getInstance().TradeQueryDetailMap;
		saleType = (String)detailMap.get("saleType");
		initDetailView(Integer.valueOf(saleType.trim()));
	}
    /**
     * 初始化详情页面
     * @param saleType  交易类型
     */
	private void initDetailView(int saleType) {
		switch (saleType) {
		case 0://买入活期贵金属积利
//			if("1".equals((String)detailMap.get("channel")) || "2".equals((String)detailMap.get("channel"))){
//				containLayout.addView(createLabelTextView(R.string.goldbonus_deposit_transactionId, (String)detailMap.get("tradeno"), null));
//			}
			containLayout.addView(createLabelTextView(R.string.goldbonus_tradatetime,(String)detailMap.get("tranDate") + "  " + (String)detailMap.get("tranTime"),null));
			containLayout.addView(createLabelTextView(R.string.goldbonus_busitrade_accno,StringUtil
					.getForSixForString((String) detailMap
							.get("accountNum"))+" "+DictionaryData.getKeyByValue((String)detailMap.get("acctType"),
									DictionaryData.goldbonusAcctTypeList),null));
			containLayout.addView(createLabelTextView(R.string.goldbonus_tratype, "买入活期贵金属积利", null));
			containLayout.addView(createLabelTextView(R.string.goldbonus_timedeposit_proName,(String)detailMap.get("issueName"),null));
			containLayout.addView(createLabelTextView(R.string.goldbonus_tranumber,StringUtil.parseStringPattern(StringUtil.deleateNumber((String)detailMap.get("weight")),0)," 克",REDBLACK));
			containLayout.addView(createLabelTextView(R.string.fixinvest_baseprice,StringUtil.parseStringPattern((String)detailMap.get("tranBfprice"),2)+" 人民币元/克",null));
			containLayout.addView(createLabelTextView(R.string.fixinvest_tranPrice,StringUtil.parseStringPattern((String)detailMap.get("tradePrice"),2)," 人民币元/克",REDBLACK));
			containLayout.addView(createLabelTextView(R.string.fixinvest_traAmounttwo,"人民币元  ",StringUtil.parseStringPattern((String)detailMap.get("saleAmt"),2),BLACKRED));
			containLayout.addView(createLabelTextView(R.string.goldbonus_tramode,StringUtil.isNullOrEmpty((String)detailMap.get("fixId")) ? "客户自主操作" : "银行自动执行",null));
			containLayout.addView(createLabelTextView(R.string.goldbonus_trastatus,DictionaryData.getKeyByValue( (String)detailMap.get("tradeStatus"),DictionaryData.TransStatusList),null,R.color.fonts_pink));
			break;
		case 1://卖出活期贵金属积利
//			if("1".equals((String)detailMap.get("channel")) || "2".equals((String)detailMap.get("channel"))){
//				containLayout.addView(createLabelTextView(R.string.goldbonus_deposit_transactionId, (String)detailMap.get("tradeno"), null));
//			}
			containLayout.addView(createLabelTextView(R.string.goldbonus_tradatetime,(String)detailMap.get("tranDate") + "  " + (String)detailMap.get("tranTime"),null));
			containLayout.addView(createLabelTextView(R.string.goldbonus_busitrade_accno,StringUtil
					.getForSixForString((String) detailMap
							.get("accountNum"))+" "+DictionaryData.getKeyByValue((String)detailMap.get("acctType"),
									DictionaryData.goldbonusAcctTypeList),null));
			containLayout.addView(createLabelTextView(R.string.goldbonus_tratype, "卖出活期贵金属积利", null));
			containLayout.addView(createLabelTextView(R.string.goldbonus_timedeposit_proName,(String)detailMap.get("issueName"),null));
			containLayout.addView(createLabelTextView(R.string.goldbonus_tranumber,StringUtil.parseStringPattern(StringUtil.deleateNumber((String)detailMap.get("weight")),0)," 克",REDBLACK));
			containLayout.addView(createLabelTextView(R.string.fixinvest_baseprice,StringUtil.parseStringPattern((String)detailMap.get("tranBfprice"),2)+" 人民币元/克",null));
			containLayout.addView(createLabelTextView(R.string.fixinvest_tranPrice,StringUtil.parseStringPattern((String)detailMap.get("tradePrice"),2)," 人民币元/克",REDBLACK));
			containLayout.addView(createLabelTextView(R.string.fixinvest_traAmounttwo,"人民币元  ",StringUtil.parseStringPattern((String)detailMap.get("saleAmt"),2),BLACKRED));
			containLayout.addView(createLabelTextView(R.string.goldbonus_tramode,"客户自主操作",null));
			containLayout.addView(createLabelTextView(R.string.goldbonus_trastatus,DictionaryData.getKeyByValue( (String)detailMap.get("tradeStatus"),DictionaryData.TransStatusList),null,R.color.fonts_pink));
			break;
		case 2://贵金属积利活期转定期
//			if("1".equals((String)detailMap.get("channel")) || "2".equals((String)detailMap.get("channel"))){
//				containLayout.addView(createLabelTextView(R.string.goldbonus_deposit_transactionId, (String)detailMap.get("tradeno"), null));
//			}
			containLayout.addView(createLabelTextView(R.string.goldbonus_tradatetime,(String)detailMap.get("tranDate") + "  " + (String)detailMap.get("tranTime"),null));
			containLayout.addView(createLabelTextView(R.string.goldbonus_busitrade_accno,StringUtil
					.getForSixForString((String) detailMap
							.get("accountNum"))+" "+DictionaryData.getKeyByValue((String)detailMap.get("acctType"),
									DictionaryData.goldbonusAcctTypeList),null));
			containLayout.addView(createLabelTextView(R.string.goldbonus_tratype, "贵金属积利活期转为定期", null));
			containLayout.addView(createLabelTextView(R.string.goldbonus_timedeposit_proName,(String)detailMap.get("issueName"),null));
			containLayout.addView(createLabelTextView(R.string.goldbonus_tranumber,StringUtil.parseStringPattern(StringUtil.deleateNumber((String)detailMap.get("weight")),0)," 克",REDBLACK));
			containLayout.addView(createLabelTextView(R.string.goldbonus_deposit_period,String.valueOf(detailMap.get("limitTime")) +
					DictionaryData.getKeyByValue((String)detailMap.get("limitUnit"),DictionaryData.goldbonuslimitUnitList),null));
			containLayout.addView(createLabelTextView(R.string.goldbonus_timedeposit_rate, paseEndZero(String.valueOf(detailMap.get("issueRate")))+"%",null));
			containLayout.addView(createLabelTextView(R.string.goldbonus_timedeposit_startdate1, String.valueOf(detailMap.get("tranDate")),null,R.color.fonts_pink));
			containLayout.addView(createLabelTextView(R.string.goldbonus_timedeposit_enddate1, String.valueOf(detailMap.get("expdate")),null));
			containLayout.addView(createLabelTextView(R.string.goldbonus_shoud_premium, "人民币元  ", "0.00".equals(StringUtil.parseStringPattern(String.valueOf(detailMap.get("regBonus")),2)) ? "-" : StringUtil.parseStringPattern(String.valueOf(detailMap.get("regBonus")),2) ,BLACKRED));
			containLayout.addView(createLabelTextView(R.string.goldbonus_tramode,"客户自主操作",null));
			containLayout.addView(createLabelTextView(R.string.goldbonus_trastatus,DictionaryData.getKeyByValue( (String)detailMap.get("tradeStatus"),DictionaryData.TransStatusList),null,R.color.fonts_pink));
			break;
		case 3://贵金属积利定期转活期
//			if("1".equals((String)detailMap.get("channel")) || "2".equals((String)detailMap.get("channel"))){
//				containLayout.addView(createLabelTextView(R.string.goldbonus_deposit_transactionId, (String)detailMap.get("tradeno"), null));
//			}
			containLayout.addView(createLabelTextView(R.string.goldbonus_tradatetime,(String)detailMap.get("tranDate") + "  " + (String)detailMap.get("tranTime"),null));
			containLayout.addView(createLabelTextView(R.string.goldbonus_busitrade_accno,StringUtil
					.getForSixForString((String) detailMap
							.get("accountNum"))+" "+DictionaryData.getKeyByValue((String)detailMap.get("acctType"),
									DictionaryData.goldbonusAcctTypeList),null));
			containLayout.addView(createLabelTextView(R.string.goldbonus_tratype, "贵金属积利定期转为活期", null));
			containLayout.addView(createLabelTextView(R.string.goldbonus_timedeposit_proName,(String)detailMap.get("issueName"),null));
			containLayout.addView(createLabelTextView(R.string.goldbonus_tranumber,StringUtil.parseStringPattern(StringUtil.deleateNumber((String)detailMap.get("weight")),0)," 克",REDBLACK));
			containLayout.addView(createLabelTextView(R.string.goldbonus_deposit_period,String.valueOf(detailMap.get("limitTime")) +
					DictionaryData.getKeyByValue((String)detailMap.get("limitUnit"),DictionaryData.goldbonuslimitUnitList),null));
			containLayout.addView(createLabelTextView(R.string.goldbonus_timedeposit_rate, paseEndZero(String.valueOf(detailMap.get("issueRate")))+"%",null));
			containLayout.addView(createLabelTextView(R.string.goldbonus_timedeposit_startdate1, String.valueOf(detailMap.get("tranDate")),null));
			containLayout.addView(createLabelTextView(R.string.goldbonus_timedeposit_enddate1, String.valueOf(detailMap.get("expdate")),null,R.color.fonts_pink));
			containLayout.addView(createLabelTextView(R.string.goldbonus_shoud_premium1, "人民币元  ", "0.00".equals(StringUtil.parseStringPattern(String.valueOf(detailMap.get("regBonus")),2)) ? "-" : StringUtil.parseStringPattern(String.valueOf(detailMap.get("regBonus")),2) ,BLACKRED));
			containLayout.addView(createLabelTextView(R.string.goldbonus_tramode,"银行自动执行",null));
			containLayout.addView(createLabelTextView(R.string.goldbonus_trastatus,DictionaryData.getKeyByValue( (String)detailMap.get("tradeStatus"),DictionaryData.TransStatusList),null,R.color.fonts_pink));
			break;
		}
	}
	
}
