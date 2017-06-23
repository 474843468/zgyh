package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.util;

import android.content.res.Resources;
import android.text.TextUtils;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BOCProductForHoldingInfo;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 中银理财工具类，对中银理财数据进行一些转换
 * 
 * @author HVZHUNG
 *
 */
public class HoldingBOCProductInfoUtil {

	/**
	 * 判断理财产品是否为净值类产品
	 * 
	 * @param info
	 * @return
	 */
	public static boolean isValueType(BOCProductForHoldingInfo info) {
		if (info == null) {
			return false;
		}
		return BOCProductForHoldingInfo.PROD_TYPE_VALUE
				.equals(info.issueType);

	}

	/**
	 * 是否为现金管理类理财产品
	 * 
	 * @param info
	 * @return
	 */
	public static boolean isCashType(BOCProductForHoldingInfo info) {
		if (info == null) {
			return false;
		}
		return BOCProductForHoldingInfo.PROD_TYPE_CASH.equals(info.issueType);
	}

	/**
	 * 是否为固定期限类产品
	 * 
	 * @param info
	 * @return
	 */
	public static boolean isFixationType(BOCProductForHoldingInfo info) {
		if (info == null) {
			return false;
		}
		return BOCProductForHoldingInfo.PROD_TYPE_FIXATION
				.equals(info.issueType);
	}

	/**
	 * 理财产品类型文字显示
	 * 
	 * @return 如果该产品是现金管理类型，返回相应收益类型的名称，否则返回“-”
	 */
	public static String getFriendlyCashProType(BOCProductForHoldingInfo info) {

		String result = "-";
		if (!isCashType(info)) {// 不是现金理财类产品，不显示收益类型
			return result;
		}
		if ((BOCProductForHoldingInfo.CASH_PRO_TYPE_MULTIPLYING + "")
				.equals(info.cashProType)) {
			result = BaseDroidApp.context
					.getString(R.string.bocinvt_cash_type_0);
		} else if ((BOCProductForHoldingInfo.CASH_PRO_TYPE_PROGRESSION + "")
				.equals(info.cashProType)) {
			result = BaseDroidApp.context
					.getString(R.string.bocinvt_cash_type_1);
		} else if ((BOCProductForHoldingInfo.CASH_PRO_TYPE_GATHER + "")
				.equals(info.cashProType)) {
			result = BaseDroidApp.context
					.getString(R.string.bocinvt_cash_type_2);
		}
		return result;
	}

	/**
	 * 友好的汇钞标识显示
	 * 
	 * @return
	 */
	public static String getFriendlyCashRemit(BOCProductForHoldingInfo info) {
		String result = "";
		if (BOCProductForHoldingInfo.CASH_REMIT_CASH.equals(info.cashRemit)) {
			result = BaseDroidApp.context.getResources().getString(
					R.string.bocinvt_cash_remit_cash);
		} else if (BOCProductForHoldingInfo.CASH_REMIT_RMB
				.equals(info.cashRemit)) {
			result = BaseDroidApp.context.getResources().getString(
					R.string.bocinvt_cash_remit_rmb);
		} else if (BOCProductForHoldingInfo.CASH_REMIT_REMIT
				.equals(info.cashRemit)) {
			result = BaseDroidApp.context.getResources().getString(
					R.string.bocinvt_cash_remit_remit);
		}
		return result;
	}

	/**
	 * 友好的年收益率范围显示
	 * 
	 * @return
	 */
	public static String getFriendlyYearlyRRRange(BOCProductForHoldingInfo info) {
		if(!StringUtil.isNullOrEmpty(info.yearlyRRMax)){
			if(Double.parseDouble(info.yearlyRRMax) > 0){
				return info.yearlyRR + "%-" + info.yearlyRRMax + "%";
			}
		}
		return info.yearlyRR + "%";
	}

	public static String getFriendlyYearlyRR(BOCProductForHoldingInfo info) {
		return info.yearlyRR + "%";

	}

	/**
	 * 返回经过处理的资金账户，显示为1111******1111，即4 6 4格式
	 * 
	 * @return
	 */
	public static String getHiddenBancAccount(BOCProductForHoldingInfo info) {
		return StringUtil.getForSixForString(info.bancAccount);
	}

	/**
	 * 
	 * @return 是否为业绩基准型产品,是返回true，否为false
	 */
	public static boolean isStandardPro(BOCProductForHoldingInfo info) {
		if (info == null) {
			return false;
		}
		if (!isFixationType(info)) {
			return false;
		}
		return BOCProductForHoldingInfo.PROD_TYPE_FIXATION
				.equals(info.issueType)
				&& (BOCProductForHoldingInfo.STANDARD_YES_1
						.equals(info.standardPro)
						|| BOCProductForHoldingInfo.STANDARD_YES_2
								.equals(info.standardPro) || BOCProductForHoldingInfo.STANDARD_YES_3
							.equals(info.standardPro));
	}

	/**
	 * 
	 * @param info
	 * @return 业绩基准型产品的业绩基准类型名称
	 */
	public static String getStandardProName(BOCProductForHoldingInfo info) {
		Resources resources = BaseDroidApp.context.getResources();
		String result = resources.getString(R.string.bocinvt_standard_type_no);
		if (!isStandardPro(info)) {
			return result;
		}

		if (BOCProductForHoldingInfo.STANDARD_YES_1.equals(info.standardPro)) {
			result = resources.getString(R.string.bocinvt_standard_type_1);
		} else if (BOCProductForHoldingInfo.STANDARD_YES_2
				.equals(info.standardPro)) {
			result = resources.getString(R.string.bocinvt_standard_type_2);
		} else if (BOCProductForHoldingInfo.STANDARD_YES_3
				.equals(info.standardPro)) {
			result = resources.getString(R.string.bocinvt_standard_type_3);
		} else {
			result = resources
					.getString(R.string.bocinvt_standard_type_unknown);
		}
		return result;
	}

	/**
	 * 产品到期时间显示
	 * 
	 * @return 产品的到期时间
	 */
	public static String getFriendlyProdEnd(BOCProductForHoldingInfo info) {
		if (null == info.prodEnd) {
			return "-";
		}
//		if ("-1".equals(info.prodEnd)) {
//			return BaseDroidApp.context.getString(R.string.bocinvt_undated);
//		}
		if(HoldingBOCProductInfoUtil.isStandardPro(info)){
			return "-";
		}
		//业绩基准型产品一定有效，非业绩基准型的用termtype判断
		if(!HoldingBOCProductInfoUtil.isStandardPro(info)&&"04".equals(info.termType)){
			return BaseDroidApp.context.getString(R.string.bocinvt_longTime);
		}
		return info.prodEnd;
	}

	/**
	 * 获取现金管理类产品的收益类型
	 * 
	 * @return 返回值包括{@link BOCProductForHoldingInfo#CASH_PRO_TYPE_MULTIPLYING},
	 *         {@link BOCProductForHoldingInfo#CASH_PRO_TYPE_PROGRESSION},
	 *         {@link BOCProductForHoldingInfo#CASH_PRO_TYPE_GATHER}
	 *         ，不是现金管理类产品返回-1
	 */
	public static int getCashProTypeInt(BOCProductForHoldingInfo info) {
		int result = -1;
		if ("0".equals(info.cashProType)) {
			result = BOCProductForHoldingInfo.CASH_PRO_TYPE_MULTIPLYING;
		} else if ("1".equals(info.cashProType)) {
			result = BOCProductForHoldingInfo.CASH_PRO_TYPE_PROGRESSION;
		} else if ("2".equals(info.cashProType)) {
			result = BOCProductForHoldingInfo.CASH_PRO_TYPE_GATHER;
		}
		return result;
	}

	/**
	 * 持有份额格式化的显示
	 * 
	 * @param info
	 * @return
	 */
	public static String getFriendlyHoldingQuantity(
			BOCProductForHoldingInfo info) {
//		return StringUtil.parseStringCodePattern(info.curCode.numberCode,info.holdingQuantity, 2);
		return StringUtil.parseStringPattern(info.holdingQuantity, 2);
	}

	/**
	 * 可用份额显示
	 * 
	 * @param info
	 * @return
	 */
	public static String getFriendlyAvailableQuantity(
			BOCProductForHoldingInfo info) {
//		return StringUtil.parseStringCodePattern(info.curCode.numberCode,info.availableQuantity, 2);
		return StringUtil.parseStringPattern(info.availableQuantity, 2);
	}

	/**
	 * 格式化份额
	 * 
	 * @param quantity
	 * @return
	 */
	public static String formatQuantity(String quantity,BOCProductForHoldingInfo info) {
		if (TextUtils.isEmpty(quantity)) {

			return "";
		}
//		return StringUtil.parseStringCodePattern(info.curCode.numberCode,quantity, 2);
		return StringUtil.parseStringPattern(quantity, 2);
	}

	/**
	 * 最小持有份额的显示
	 * 
	 * @param info
	 * @return
	 */
	public static String getFriendlyMinHoldingQuantity(
			BOCProductForHoldingInfo info) {
//		return StringUtil.parseStringCodePattern(info.curCode.numberCode,info.lowestHoldQuantity, 2);
		return StringUtil.parseStringPattern(info.lowestHoldQuantity, 2);
	}

	/**
	 * 赎回起点份额,最小赎回份额显示
	 * 
	 * @param info
	 * @return
	 */
	public static String getFriendlyMinRedeemQuantity(
			BOCProductForHoldingInfo info) {
//		return StringUtil.parseStringCodePattern(info.curCode.numberCode,info.redeemStartingAmount, 2);
		return StringUtil.parseStringPattern(info.redeemStartingAmount, 2);
	}

	/***
	 * 判断理财产品是否可查询历史净值
	 * 
	 * @param info
	 * @return
	 */
	public static boolean canQueryHistoryValue(BOCProductForHoldingInfo info) {
		if (info == null) {
			return false;
		}
		return "1".equals(info.progressionflag)
				|| BOCProductForHoldingInfo.PROD_TYPE_VALUE
						.equals(info.issueType);
	}

	/**
	 * 理财产品是否可赎回
	 * 
	 * @param info
	 * @return
	 */
	public static boolean canRedeem(BOCProductForHoldingInfo info) {

		if (info == null) {
			return false;
		}
		if ("0".equals(info.canRedeem)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 是否允许撤单
	 * 
	 * @param info
	 * @return
	 */
	public static boolean canRevoke(BOCProductForHoldingInfo info) {
		if (info == null) {
			return false;
		}
		if ("0".equals(info.enableRevoke)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断产品是否可显示参考收益
	 * 
	 * @return
	 */
	public static boolean canShowReferenceYield(BOCProductForHoldingInfo info) {
		if (info == null) {
			return false;
		}
		// 固定期限类，净值型，可显示
		if (BOCProductForHoldingInfo.PROD_TYPE_FIXATION.equals(info.issueType)
				|| BOCProductForHoldingInfo.PROD_TYPE_VALUE
						.equals(info.issueType)) {
			return true;
		} else if (BOCProductForHoldingInfo.PROD_TYPE_CASH
				.equals(info.issueType)) {
			// 现金管理类，日积月累，收益累进可显示
			switch (getCashProTypeInt(info)) {
			case BOCProductForHoldingInfo.CASH_PRO_TYPE_MULTIPLYING:
			case BOCProductForHoldingInfo.CASH_PRO_TYPE_PROGRESSION:
				return true;
			default:
				break;
			}
		}

		return false;
	}

	/**
	 * 是否可更改分红方式
	 * 
	 * @param info
	 * @return
	 */
	public static boolean canChangeBonusMode(BOCProductForHoldingInfo info) {
		if (info == null) {
			return false;

		}
		return "0".equals(info.canChangeBonusMode);
	}

	/**
	 * 是否可投资协议管理
	 * 
	 * @param info
	 * @return
	 */
	public static boolean canAgreementMange(BOCProductForHoldingInfo info) {
		if (info == null) {
			return false;

		}
		return "1".equals(info.canAgreementMange);
	}
}
