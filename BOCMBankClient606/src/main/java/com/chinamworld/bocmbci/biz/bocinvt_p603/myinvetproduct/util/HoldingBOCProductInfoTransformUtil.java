package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.text.TextUtils;

import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BOCCurrency;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BOCProductForHoldingInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BocProductForDeprecateInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BocProductForFixationHoldingInfo;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 中银理财产品转换类
 * 
 * @author HVZHUNG
 *
 */
public class HoldingBOCProductInfoTransformUtil {

	public static final String TAG = HoldingBOCProductInfoTransformUtil.class
			.getSimpleName();

	/**
	 * 将用map保存的理财产品数据列表转换为 ArrayList<BOCProductInfo>数据
	 * 
	 * @param maps
	 * @return
	 */
	public static ArrayList<BOCProductForHoldingInfo> maps2BocProductInfos(
			List<Map<String, Object>> maps) {
		if (maps == null || maps.size() <= 0) {
			return null;

		}
		ArrayList<BOCProductForHoldingInfo> infos = new ArrayList<BOCProductForHoldingInfo>();
		BOCProductForHoldingInfo info;
		for (Map<String, Object> map : maps) {
			info = map2BocProductInfo(map);
			if (info != null) {
				// LogGloble.d(TAG, "productinfo = " + info);
				infos.add(info);
			}
		}
		return infos;
	}

	/**
	 * 将存储在map中的理财产品数据转换为BOCProductInfo
	 * 
	 * @param map
	 * @return
	 */
	public static BOCProductForHoldingInfo map2BocProductInfo(
			Map<String, Object> map) {
		if (map == null || map.size() <= 0)
			return null;
		BOCProductForHoldingInfo info = null;
		if ("2".equals((String) map.get("productKind"))) {
			info = new BocProductForFixationHoldingInfo();
		} else {
			info = new BOCProductForHoldingInfo();
		}
		info.accountKey = (String) map.get("accountKey");
		info.allowAssignDate = (String) map.get("allowAssignDate");
		info.availableQuantity = (String) map.get("availableQuantity");
		info.bancAccount = (String) map.get("bancAccount");
		String canAnddBugStr = null;
		if (map.containsKey("canAddBuy"))
			canAnddBugStr = (String) map.get("canAddBuy");
		info.canAddBuy = "0".equals(canAnddBugStr) ? true : false;
		info.canAgreementMange = (String) map.get("canAgreementMange");
		info.canChangeBonusMode = (String) map.get("canChangeBonusMode");
		info.canPartlyRedeem = (String) map.get("canPartlyRedeem");
		info.canRedeem = (String) map.get("canRedeem");
		info.cashProType = (String) map.get("cashProType");
		info.cashRemit = (String) map.get("cashRemit");
		info.curCode = BOCCurrency.getInstanceByNumberCode(
				BaseDroidApp.context, (String) map.get("curCode"));
		info.currentBonusMode = (String) map.get("currentBonusMode");
		info.expAmt = (String) map.get("expAmt");
		info.expProfit = (String) map.get("expProfit");
		info.holdingQuantity = (String) map.get("holdingQuantity");
		info.lowestHoldQuantity = (String) map.get("lowestHoldQuantity");
		info.price = (String) map.get("price");
		info.priceDate = (String) map.get("priceDate");
		info.prodBegin = (String) map.get("prodBegin");
		info.prodCode = (String) map.get("prodCode");
		info.prodEnd = (String) map.get("prodEnd");
		info.prodName = (String) map.get("prodName");
		info.issueType = (String) map.get("issueType");
		info.productKind = (String) map.get("productKind");
		info.productTerm = (String) map.get("productTerm");
		info.progressionflag = (String) map.get("progressionflag");
		info.redeemStartingAmount = (String) map.get("redeemStartingAmount");
		info.sellPrice = (String) map.get("sellPrice");
		info.shareValue = (String) map.get("shareValue");
		info.standardPro = (String) map.get("standardPro");
		info.termType = (String) map.get("termType");
		info.xpadAccount = (String) map.get("xpadAccount");
		info.yearlyRR = (String) map.get("yearlyRR");

		info.bancAccountKey = (String) map.get("bancAccountKey");
		info.currPeriod = (String) map.get("currPeriod");
		info.totalPeriod = (String) map.get("totalPeriod");
		info.yearlyRRMax = (String) map.get("yearlyRRMax");
		info.tranSeq = (String)map.get("tranSeq");
		return info;
	}

	/**
	 * 将理财产品信息转换为Map保存
	 * 
	 * @param info
	 * @return
	 */
	public static HashMap<String, Object> BocProductInfo2map(
			BOCProductForHoldingInfo info) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("accountKey", info.accountKey);
		map.put("allowAssignDate", info.allowAssignDate);
		map.put("availableQuantity", info.availableQuantity);
		map.put("bancAccount", info.bancAccount);
		map.put("canAddBuy", info.canAddBuy ? "0" : "1");
		map.put("canAgreementMange", info.canAgreementMange);
		map.put("canChangeBonusMode", info.canChangeBonusMode);
		map.put("canPartlyRedeem", info.canPartlyRedeem);
		map.put("canRedeem", info.canRedeem);
		map.put("cashProType", info.cashProType);
		map.put("cashRemit", info.cashRemit);
		map.put("curCode", info.curCode.numberCode);
		map.put("currentBonusMode", info.currentBonusMode);
		map.put("expAmt", info.expAmt);
		map.put("expProfit", info.expProfit);
		map.put("holdingQuantity", info.holdingQuantity);
		map.put("lowestHoldQuantity", info.lowestHoldQuantity);
		map.put("price", info.price);
		map.put("priceDate", info.priceDate);
		map.put("prodBegin", info.prodBegin);
		map.put("prodCode", info.prodCode);
		map.put("prodEnd", info.prodEnd);
		map.put("prodName", info.prodName);
		map.put("issueType", info.issueType);
		map.put("productKind", info.productKind);
		map.put("productTerm", info.productTerm);
		map.put("progressionflag", info.progressionflag);
		map.put("redeemStartingAmount", info.redeemStartingAmount);
		map.put("sellPrice", info.sellPrice);
		map.put("shareValue", info.shareValue);
		map.put("standardPro", info.standardPro);
		map.put("termType", info.termType);
		map.put("xpadAccount", info.xpadAccount);
		map.put("yearlyRR", info.yearlyRR);
		map.put("bancAccountKey", info.bancAccountKey);
		map.put("currPeriod", info.currPeriod);
		map.put("totalPeriod", info.totalPeriod);
		map.put("yearlyRRMax", info.yearlyRRMax);
		return map;
	}
	
	/**
	 * 将存储在map中的到期产品数据转换为BocProductForDeprecateInfo
	 * 
	 * @param map
	 * @return
	 */
	public static BocProductForDeprecateInfo map2BocProductForDeprecateInfo(
			Map<String, Object> map) {
		if (map == null || map.size() <= 0)
			return null;
		BocProductForDeprecateInfo info = new BocProductForDeprecateInfo();
		info.proId = (String) map.get("proId");
		info.eDate = (String) map.get("eDate");
		info.proname = (String) map.get("proname");
		info.currency = (String) map.get("procur");
		info.procur = BOCCurrency.getInstanceByNumberCode(BaseDroidApp.context,
				(String) map.get("procur"));
		if (info.procur == null) {
			info.procur = BOCCurrency.getInstanceByNumberCode(
					BaseDroidApp.context, "000");
		}
		info.buyMode = (String) map.get("buyMode");
		info.proterm = (String) map.get("proterm");
		String amountStr = (String) map.get("amount");
		if (!TextUtils.isEmpty(amountStr))
			info.amount = new BigDecimal(amountStr);
		String payProfitStr = (String) map.get("payProfit");
		if (!TextUtils.isEmpty(payProfitStr))
			info.payProfit = new BigDecimal(payProfitStr);
		String payRateStr = (String) map.get("payRate");
		if (!TextUtils.isEmpty(payRateStr))
			info.payRate = new BigDecimal(payRateStr);
		info.kind = (String) map.get("kind");
		info.payFlag = (String) map.get("payFlag");
		info.accno = (String) map.get("accno");
		info.extFiled = (String) map.get("extFiled");
		return info;
	}
	
	/**
	 * 合并份额明细数据和持仓列表数据
	 * @param map1 持仓列表数据
	 * @param map2 份额明细数据
	 * @return
	 */
	public static Map<String, Object> Combine2map(
			Map<String, Object> map1,Map<String, Object> map2) {
		if(StringUtil.isNullOrEmpty(map2)){
			return map1;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(BocInvt.BANCACCOUNT, !StringUtil.isNullOrEmpty(map2.get("xpadAccount")) ? 
				map2.get("xpadAccount") : map1.get(BocInvt.BANCACCOUNT));//交易账户
		map.put(BocInvt.BOCINVT_HOLDPRO_PRODCODE_RES, map1.get(BocInvt.BOCINVT_HOLDPRO_PRODCODE_RES));//产品代码
		map.put(BocInvt.BOCINVT_HOLDPRO_PRODNAME_RES, !StringUtil.isNullOrEmpty(map2.get("prodName")) ? 
				map2.get("prodName") : map1.get(BocInvt.BOCINVT_HOLDPRO_PRODNAME_RES));//产品名称
		map.put(BocInvt.BOCINVT_HOLDPRO_CURCODE_RES, !StringUtil.isNullOrEmpty(map2.get("curCode")) ? 
				map2.get("curCode") : map1.get(BocInvt.BOCINVT_HOLDPRO_CURCODE_RES));//币种
		map.put(BocInvt.BOCINVT_HOLDPRO_CASHREMIT_RES, !StringUtil.isNullOrEmpty(map2.get("cashRemit")) ? 
				map2.get("cashRemit") : map1.get(BocInvt.BOCINVT_HOLDPRO_CASHREMIT_RES));//炒汇标志
		map.put("productTerm", !StringUtil.isNullOrEmpty(map2.get("productTerm")) ? 
				map2.get("productTerm") : map1.get("productTerm"));//产品期限
		map.put("prodEnd", !StringUtil.isNullOrEmpty(map2.get("prodEnd")) ? 
				map2.get("prodEnd") : map1.get("prodEnd"));//产品到期日
		map.put("yearlyRR", !StringUtil.isNullOrEmpty(map2.get("yearlyRR")) ? 
				map2.get("yearlyRR") : map1.get("yearlyRR"));//预计年收益率
		map.put("price",  map1.get("price"));//单位净值
		map.put("priceDate", map1.get("priceDate"));//净值更新日期
		map.put("holdingQuantity", !StringUtil.isNullOrEmpty(map2.get("holdingQuantity")) ? 
				map2.get("holdingQuantity") : map1.get("holdingQuantity"));//持有份额
		map.put("availableQuantity", !StringUtil.isNullOrEmpty(map2.get("availableQuantity")) ? 
				map2.get("availableQuantity") : map1.get("availableQuantity"));//可用份额
		map.put("expProfit", !StringUtil.isNullOrEmpty(map2.get("expProfit")) ? 
				map2.get("expProfit") : map1.get("expProfit"));//参考收益
		map.put("bancAccount", !StringUtil.isNullOrEmpty(map2.get("bancAccount")) ? 
				map2.get("bancAccount") : map1.get("bancAccount"));//资金账号
		map.put("prodBegin", !StringUtil.isNullOrEmpty(map2.get("prodBegin")) ? 
				map2.get("prodBegin") : map1.get("prodBegin"));//产品起息日
		map.put("prodEnd", !StringUtil.isNullOrEmpty(map2.get("prodEnd")) ? 
				map2.get("prodEnd") : map1.get("prodEnd"));//产品到期日
		map.put("canRedeem", !StringUtil.isNullOrEmpty(map2.get("canRedeem")) ? 
				map2.get("canRedeem") : map1.get("canRedeem"));//是否可赎回
		map.put("canPartlyRedeem", !StringUtil.isNullOrEmpty(map2.get("canPartlyRedeem")) ? 
				map2.get("canPartlyRedeem") : map1.get("canPartlyRedeem"));//是否允许部分赎回
		map.put("canChangeBonusMode",map1.get("canChangeBonusMode"));//是否可修改分红方式
		map.put("currentBonusMode",map1.get("currentBonusMode"));//当前分红方式
		map.put("lowestHoldQuantity", !StringUtil.isNullOrEmpty(map2.get("lowestHoldQuantity")) ? 
				map2.get("lowestHoldQuantity") : map1.get("lowestHoldQuantity"));//最低持有份额
		map.put("redeemStartingAmount", !StringUtil.isNullOrEmpty(map2.get("redeemStartingAmount")) ? 
				map2.get("redeemStartingAmount") : map1.get("redeemStartingAmount"));//赎回起点金额
		map.put("progressionflag",map1.get("progressionflag"));//是否收益累计产品
		map.put("sellPrice",map1.get("sellPrice"));//赎回价格
		map.put("productKind",map1.get("productKind"));//产品性质
		map.put("expAmt", map1.get("expAmt"));//参考市值
		map.put("issueType",map1.get("issueType"));//产品类型
		map.put("termType", map1.get("termType"));//产品期限特性
		map.put("canAddBuy", map1.get("canAddBuy"));//是否可追加购买
		map.put("standardPro", map1.get("standardPro"));//是否为业绩基准产品
		map.put("canAgreementMange", !StringUtil.isNullOrEmpty(map2.get("canAgreementMange")) ? 
				map2.get("canAgreementMange") : map1.get("canAgreementMange"));//是否可投资协议管理
		map.put("bancAccountKey", !StringUtil.isNullOrEmpty(map2.get("bancAccountKey")) ? 
				map2.get("bancAccountKey") : map1.get("bancAccountKey"));//资金账号缓存标识
		map.put("currPeriod", !StringUtil.isNullOrEmpty(map2.get("currPeriod")) ? 
				map2.get("currPeriod") : map1.get("currPeriod"));//当前期数
		map.put("totalPeriod", !StringUtil.isNullOrEmpty(map2.get("totalPeriod")) ? 
				map2.get("totalPeriod") : map1.get("totalPeriod"));//总期数
		map.put("yearlyRRMax", map1.get("yearlyRRMax"));//预计年收益率（最大值）
		map.put("canQuantityExchange",map2.get("canQuantityExchange"));//是否可份额转换
		map.put("tranSeq", map2.get("tranSeq"));//交易流水号
		return map;
	}
}
