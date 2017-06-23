package com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.fragment;

import android.support.v4.app.Fragment;

import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BOCProductForHoldingInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BaseReferProfitInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.ReferenceProfitForCashInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.ReferenceProfitForValueInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.util.HoldingBOCProductInfoUtil;

/**
 * 用于生成用于显示中银理财产品参考收益的fragment
 * 
 * @author HVZHUNG
 *
 */
public class ReferenceProfitFragmentFactory {
	/***
	 * 获取理财产品对应的参考收益显示Fragment
	 * 
	 * @param info
	 * @return
	 */
	public static Fragment newInstance(BOCProductForHoldingInfo info,
			BaseReferProfitInfo referProfitInfo) {
		Fragment instance = null;
//		if (BOCProductForHoldingInfo.PROD_TYPE_VALUE.equals(info.issueType)) {// 净值型
		if ("1".equals(info.productKind)) {// 净值型
			instance = ReferenceProfitForValueFragment.newInstance(info,
					(ReferenceProfitForValueInfo) referProfitInfo);
		} else {
			if("1".equals(info.progressionflag)){//收益累进
				instance = ReferenceProfitForProgressionFragment
						.newInstance(info,(ReferenceProfitForCashInfo) referProfitInfo);
			}
//			if (BOCProductForHoldingInfo.PROD_TYPE_CASH.equals(info.issueType)) {// 现金管理类
//				switch (HoldingBOCProductInfoUtil.getCashProTypeInt(info)) {
//				case BOCProductForHoldingInfo.CASH_PRO_TYPE_MULTIPLYING:// 日积月累
//					instance = ReferenceProfitForHaveDetailFragment
//							.newInstance(
//									info,
//									new ReferenceProfitForStateInfo(
//											(ReferenceProfitForCashInfo) referProfitInfo),
//									true);
//					break;
//				case BOCProductForHoldingInfo.CASH_PRO_TYPE_PROGRESSION:// 收益累进
//					instance = ReferenceProfitForProgressionFragment
//							.newInstance(
//									info,
//									(ReferenceProfitForCashInfo) referProfitInfo);
//					break;
//				case BOCProductForHoldingInfo.CASH_PRO_TYPE_GATHER:// TODO 与时俱金
//																	// 非净值型
//				default:
//
//					// 非净值型
//					break;
//				}
//			} 
//			else if (BOCProductForHoldingInfo.PROD_TYPE_FIXATION
//					.equals(info.issueType)) {// 固定期限类
			else if (HoldingBOCProductInfoUtil.isStandardPro(info)) {// 业绩基准型
					instance = ReferenceProfitForHaveDetailFragment.newInstance(info,
									(ReferenceProfitForCashInfo) referProfitInfo,false);
				} else if("AMRJYL01".equals(info.prodCode)){//日积月累
					instance = ReferenceProfitForHaveDetailFragment.newInstance(info,
							(ReferenceProfitForCashInfo) referProfitInfo,true);
				}
//				else {// TODO 非净值型
//
//				}
//			} else {// TODO 非净值
//
//			}
		}
		return instance;
	}
}
