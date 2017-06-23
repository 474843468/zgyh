package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.MClickableSpan;
import com.boc.bocsoft.mobile.bocmobile.base.widget.financelist.FinanceListItemView;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadproductbalancequery.PsnXpadProductBalanceQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.util.FinancialPositionCommonUtil;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseListAdapter;

/**
 * 持仓列表-列表适配器
 * Created by yx on 2016-9-16 18:56:20
 */
public class FinancialPositionListAdapter extends BaseListAdapter<PsnXpadProductBalanceQueryResModel> {

    public FinancialPositionListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FinanceListItemView mFinanceListItemVie =
                convertView == null ? new FinanceListItemView(mContext)
                        : (FinanceListItemView) convertView;
        PsnXpadProductBalanceQueryResModel viewModel = getItem(position);
        mFinanceListItemVie.isShowDividerLine(false);
        /**
         * 产品类型	String	1：现金管理类产品
         * 2：净值开放类产品
         * 3：固定期限产品
         */
        if ("1".equalsIgnoreCase(viewModel.getIssueType())) {//现金管理类产品
            if (!"001".equalsIgnoreCase(viewModel.getCurCode())) {
//                钞汇标识	String	01：钞   02：汇   00：人民币
                if ("01".equalsIgnoreCase(viewModel.getCashRemit())) {
                    mFinanceListItemVie.setTxtHeadLeft("[" + PublicCodeUtils.getCurrency(mContext, viewModel.getCurCode()) + "/钞]" + viewModel.getProdName() + "（" + viewModel.getProdCode() + "）");
                } else if ("02".equalsIgnoreCase(viewModel.getCashRemit())) {
                    mFinanceListItemVie.setTxtHeadLeft("[" + PublicCodeUtils.getCurrency(mContext, viewModel.getCurCode()) + "/汇]" + viewModel.getProdName() + "（" + viewModel.getProdCode() + "）");
                } else if ("00".equalsIgnoreCase(viewModel.getCashRemit())) {
                    mFinanceListItemVie.setTxtHeadLeft(viewModel.getProdName() + "（" + viewModel.getProdCode() + "）");
                }
            } else {
                mFinanceListItemVie.setTxtHeadLeft(viewModel.getProdName() + "（" + viewModel.getProdCode() + "）");
            }
            mFinanceListItemVie.setTxtHeadRight("");
            mFinanceListItemVie.setTxtCenterName("持有份额", "预期年化收益率", "参考收益");
            if (FinancialPositionCommonUtil.isShowMax(viewModel.getYearlyRRMax())) {
//                mFinanceListItemVie.setTxtCenterValue(MoneyUtils.transMoneyFormat(viewModel.getHoldingQuantity(), "001"),
//                        viewModel.getYearlyRR() + "%～" + viewModel.getYearlyRRMax() + "%", MoneyUtils.transMoneyFormat(viewModel.getExpProfit(), viewModel.getCurCode()));
                mFinanceListItemVie.setTxtCenterValue(setMoneyShowType(viewModel.getHoldingQuantity()),
                        viewModel.getYearlyRR() + "%～" + viewModel.getYearlyRRMax() + "%", MoneyUtils.transMoneyFormat(viewModel.getExpProfit(), viewModel.getCurCode()));

            } else {
//                mFinanceListItemVie.setTxtCenterValue(MoneyUtils.transMoneyFormat(viewModel.getHoldingQuantity(), "001"),
//                        viewModel.getYearlyRR() + "%", MoneyUtils.transMoneyFormat(viewModel.getExpProfit(), viewModel.getCurCode()));
                mFinanceListItemVie.setTxtCenterValue(setMoneyShowType(viewModel.getHoldingQuantity()),
                        viewModel.getYearlyRR() + "%", MoneyUtils.transMoneyFormat(viewModel.getExpProfit(), viewModel.getCurCode()));
            }
            mFinanceListItemVie.setValueLAttributeDp(mContext.getResources().getColor(R.color.boc_text_money_color_red), 18);
        } else if ("2".equalsIgnoreCase(viewModel.getIssueType())) {//净值开放类产品
//            001：人民币元
//            014：美元
//            012：英镑
//            013：港币
//            028: 加拿大元
//            029：澳元
//            038：欧元
//            027：日元
            if (!"001".equalsIgnoreCase(viewModel.getCurCode())) {
//                钞汇标识	String	01：钞   02：汇   00：人民币
                if ("01".equalsIgnoreCase(viewModel.getCashRemit())) {
                    mFinanceListItemVie.setTxtHeadLeft("[" + PublicCodeUtils.getCurrency(mContext, viewModel.getCurCode()) + "/钞]" + viewModel.getProdName() + "（" + viewModel.getProdCode() + "）");
                } else if ("02".equalsIgnoreCase(viewModel.getCashRemit())) {
                    mFinanceListItemVie.setTxtHeadLeft("[" + PublicCodeUtils.getCurrency(mContext, viewModel.getCurCode()) + "/汇]" + viewModel.getProdName() + "（" + viewModel.getProdCode() + "）");
                } else if ("00".equalsIgnoreCase(viewModel.getCashRemit())) {
                    mFinanceListItemVie.setTxtHeadLeft(viewModel.getProdName() + "（" + viewModel.getProdCode() + "）");
                }
            } else {
                mFinanceListItemVie.setTxtHeadLeft(viewModel.getProdName() + "（" + viewModel.getProdCode() + "）");
            }
            mFinanceListItemVie.setTxtHeadRight("");
            mFinanceListItemVie.setTxtCenterName("持有份额", "单位净值", "参考收益");
//            mFinanceListItemVie.setTxtCenterValue(MoneyUtils.transMoneyFormat(viewModel.getHoldingQuantity(), "001"), viewModel.getPrice(), MoneyUtils.transMoneyFormat(viewModel.getExpProfit(), viewModel.getCurCode()));
            mFinanceListItemVie.setTxtCenterValue(setMoneyShowType(viewModel.getHoldingQuantity()), MoneyUtils.financialNetValueTransMoneyFormat(viewModel.getPrice(), viewModel.getCurCode()),
                    MoneyUtils.transMoneyFormat(viewModel.getExpProfit(), viewModel.getCurCode()));

            mFinanceListItemVie.setValueLAttributeDp(mContext.getResources().getColor(R.color.boc_text_money_color_red), 18);
            mFinanceListItemVie.setBottomAttribute(R.color.boc_text_color_light_gray, viewModel.getPriceDate());
            mFinanceListItemVie.setBottomLocation(Gravity.CENTER);
        } else if ("3".equalsIgnoreCase(viewModel.getIssueType())) {//固定期限产品
            if (!"001".equalsIgnoreCase(viewModel.getCurCode())) {
//                钞汇标识	String	01：钞   02：汇   00：人民币
                if ("01".equalsIgnoreCase(viewModel.getCashRemit())) {
                    mFinanceListItemVie.setTxtHeadLeft("[" + PublicCodeUtils.getCurrency(mContext, viewModel.getCurCode()) + "/钞]" + viewModel.getProdName() + "（" + viewModel.getProdCode() + "）");
                } else if ("02".equalsIgnoreCase(viewModel.getCashRemit())) {
                    mFinanceListItemVie.setTxtHeadLeft("[" + PublicCodeUtils.getCurrency(mContext, viewModel.getCurCode()) + "/汇]" + viewModel.getProdName() + "（" + viewModel.getProdCode() + "）");
                } else if ("00".equalsIgnoreCase(viewModel.getCashRemit())) {
                    mFinanceListItemVie.setTxtHeadLeft(viewModel.getProdName() + "（" + viewModel.getProdCode() + "）");
                }
            } else {
                mFinanceListItemVie.setTxtHeadLeft(viewModel.getProdName() + "（" + viewModel.getProdCode() + "）");
            }
            mFinanceListItemVie.setTxtHeadRight("");
            //0：非业绩基准产品
            //1：业绩基准-锁定期转低收益
            // 2：业绩基准-锁定期后入账
            // 3：业绩基准-锁定期周期滚续
            // 业绩基准产品允许查看份额明细
            if ("0".equalsIgnoreCase(viewModel.getStandardPro())) {
                mFinanceListItemVie.setTxtCenterName("持有份额", "预期年化收益率", "参考收益");
            } else {
                mFinanceListItemVie.setTxtCenterName("持有份额", "业绩基准", "参考收益");
            }

            if (FinancialPositionCommonUtil.isShowMax(viewModel.getYearlyRRMax())) {
//                mFinanceListItemVie.setTxtCenterValue(MoneyUtils.transMoneyFormat(viewModel.getHoldingQuantity(), "001"),
//                        viewModel.getYearlyRR() + "%～" + viewModel.getYearlyRRMax() + "%", MoneyUtils.transMoneyFormat(viewModel.getExpProfit(), viewModel.getCurCode()));
                mFinanceListItemVie.setTxtCenterValue(setMoneyShowType(viewModel.getHoldingQuantity()),
                        viewModel.getYearlyRR() + "%～" + viewModel.getYearlyRRMax() + "%", MoneyUtils.transMoneyFormat(viewModel.getExpProfit(), viewModel.getCurCode()));
            } else {
//                mFinanceListItemVie.setTxtCenterValue(MoneyUtils.transMoneyFormat(viewModel.getHoldingQuantity(), "001"),
//                        viewModel.getYearlyRR() + "%", MoneyUtils.transMoneyFormat(viewModel.getExpProfit(), viewModel.getCurCode()));
                mFinanceListItemVie.setTxtCenterValue(setMoneyShowType(viewModel.getHoldingQuantity()),
                        viewModel.getYearlyRR() + "%", MoneyUtils.transMoneyFormat(viewModel.getExpProfit(), viewModel.getCurCode()));
            }
            mFinanceListItemVie.setValueLAttributeDp(mContext.getResources().getColor(R.color.boc_text_money_color_red), 18);
        }

        return mFinanceListItemVie;
    }

    /**
     * 份额显示规则{12300----》1.23万；----》亿}
     *
     * @param mHoldingQuantity
     * @return
     */
    private SpannableString setMoneyShowType(String mHoldingQuantity) {
        try {
            SpannableString mSpannableString;
            if (!StringUtils.isEmptyOrNull(mHoldingQuantity)) {
                String[] mMoney = MoneyUtils.convertNumberWanOrYiSplit(mHoldingQuantity);

                if (mMoney != null && mMoney.length > 0) {
                    if (mMoney.length == 1) {
                        LogUtils.d("yx-------mMoney[0]--->" + mMoney[0]);
                        mSpannableString = new SpannableString(MoneyUtils.transMoneyFormat(mHoldingQuantity, "001"));
                        LogUtils.d("yx-------mMoney111--->" + MoneyUtils.transMoneyFormat(mHoldingQuantity, "001"));
                        mSpannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.boc_text_color_red)), 0, MoneyUtils.transMoneyFormat(mMoney[0], "001").length(),
                                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        mSpannableString.setSpan(new AbsoluteSizeSpan(18, true), 0, MoneyUtils.transMoneyFormat(mMoney[0], "001").length(),
                                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        return mSpannableString;
                    } else if (mMoney.length == 2) {
                        LogUtils.d("yx-------mMoney[0]--->" + mMoney[0]);
                        LogUtils.d("yx-------mMoney[1]--->" + mMoney[1]);
                        String mString = mMoney[0] + mMoney[1];//拼接字段   例如 1.23+万
                        mSpannableString = new SpannableString(mString);
                        mSpannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.boc_text_color_red)), 0, mString.length() - 1,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        mSpannableString.setSpan(new AbsoluteSizeSpan(20, true), 0, mString.length() - 1,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        mSpannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.boc_text_color_red)), mString.length() - 1, mString.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        mSpannableString.setSpan(new AbsoluteSizeSpan(13, true), mString.length() - 1, mString.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        return mSpannableString;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SpannableString(mHoldingQuantity);
    }

}
