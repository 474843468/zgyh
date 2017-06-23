package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.financelist.FinanceListItemView;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.PsnXpadQuantityDetail.PsnXpadQuantityDetailResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.util.DateUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseListAdapter;

import org.threeten.bp.LocalDateTime;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 中银理财 -- 业绩基准
 * Created by zn on 2016/9/18.
 */
public class FinancialTypeOutStandDetaileListAdapter extends BaseListAdapter<PsnXpadQuantityDetailResModel.ListEntity> {
    public static SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy/MM/dd");

    public FinancialTypeOutStandDetaileListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FinanceListItemView mFinanceListItemVie =
                convertView == null ? new FinanceListItemView(mContext)
                        : (FinanceListItemView) convertView;
        PsnXpadQuantityDetailResModel.ListEntity viewModel = getItem(position);
        mFinanceListItemVie.isShowDividerLine(false);
        String mYearlyRR = "";
        if (viewModel.getProdEnd().equals("-1")) {
            mFinanceListItemVie.setTxtHeadLeft("产品到期日：" + "长期");
            if ("0".equalsIgnoreCase(viewModel.getCanRedeem())) {
                mFinanceListItemVie.setValueLAttributeDp(R.color.boc_divide_color, R.dimen.boc_text_size_big);
                mFinanceListItemVie.setTxtHeadRight("可赎回");
            }
            mYearlyRR = "无固定收益";
        } else {
            mFinanceListItemVie.setTxtHeadLeft("产品到期日：" + viewModel.getProdEnd());
           //  格式化当前时间
            String mProdEnd = viewModel.getProdEnd();
            Date mProdEndDate = DateUtils.formatStrToDate(mProdEnd);
            // 系统时间
            LocalDateTime mLocalDateTime = ApplicationContext.getInstance().getCurrentSystemDate();
            String mSystem = mLocalDateTime.format(DateFormatters.dateFormatter1);
            Date mSystemDate = DateUtils.formatStrToDate(mSystem);
            if(!DateUtils.isDateBeforeNotEqual(mSystem,mProdEnd)){
                mFinanceListItemVie.setValueLAttributeDp(R.color.boc_divide_color, R.dimen.boc_text_size_big);
                mFinanceListItemVie.setTxtHeadRight(mContext.getResources().getString(R.string.boc_trans_financial_earnbuild_ended));
            }else{
                int days = DateUtils.getIntervalDays(mSystemDate, mProdEndDate);
                mFinanceListItemVie.setValueLAttributeDp(R.color.boc_divide_color, R.dimen.boc_text_size_big);
                mFinanceListItemVie.setAppendTextColor(mContext.getResources().getColor(R.color.boc_text_money_color_red), 1,
                        "还剩", days + "", "天可赎回");
            }
            if(isShowMax(viewModel.getYearlyRRMax())){
                mYearlyRR = viewModel.getYearlyRR() + "%"+"~"+viewModel.getYearlyRRMax()+"%";
            }else{
                mYearlyRR = viewModel.getYearlyRR() + "%";
            }

        }
        String HoldingQuantity = MoneyUtils.transMoneyFormat(viewModel.getHoldingQuantity(), viewModel.getCurCode());
        LogUtils.i("持有份额：" + HoldingQuantity);

        mFinanceListItemVie.setTxtCenterName("持有份额", "预期年化收益率", "参考收益");
//        mFinanceListItemVie.setTxtCenterValue(MoneyUtils.transMoneyFormat(HoldingQuantity,"001"),mYearlyRR
//               , MoneyUtils.transMoneyFormat(viewModel.getExpProfit(),viewModel.getCurCode()));
        mFinanceListItemVie.setTxtCenterValue(setMoneyShowType(viewModel.getHoldingQuantity()),mYearlyRR
                , MoneyUtils.transMoneyFormat(viewModel.getExpProfit(),viewModel.getCurCode()));
        mFinanceListItemVie.setValueLAttributeDp(mContext.getResources().getColor(R.color.boc_text_color_dark_gray), 18);
        LogUtils.i(viewModel.getHoldingQuantity() + "," + viewModel.getYearlyRR() + "%" + "," + viewModel.getExpProfit());
        return mFinanceListItemVie;
    }

    /**
     * 预计年收益率（最大值）	String	不带%号，如果不为0，与yearlyRR字段组成区间
     *
     * @param mYearlyRRMax
     * @return true 不等于0 false等于0 或者格式转换异常
     */
    private boolean isShowMax(String mYearlyRRMax) {
        if(StringUtils.isEmptyOrNull(mYearlyRRMax)){
            return false;
        }
        try {
            float yearlyRRMax = Float.parseFloat(mYearlyRRMax);
            if (yearlyRRMax != 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            if (!"0".equalsIgnoreCase(mYearlyRRMax) && !"0.00".equalsIgnoreCase(mYearlyRRMax)
                    && !"0.".equalsIgnoreCase(mYearlyRRMax) && !"0.0".equalsIgnoreCase(mYearlyRRMax)) {
                return true;
            } else {
                return false;
            }
        }

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
                        mSpannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.boc_text_color_dark_gray)), 0, MoneyUtils.transMoneyFormat(mMoney[0], "001").length(),
                                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        mSpannableString.setSpan(new AbsoluteSizeSpan(18, true), 0, MoneyUtils.transMoneyFormat(mMoney[0], "001").length(),
                                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        return mSpannableString;
                    } else if (mMoney.length == 2) {
                        LogUtils.d("yx-------mMoney[0]--->" + mMoney[0]);
                        LogUtils.d("yx-------mMoney[1]--->" + mMoney[1]);
                        String mString = mMoney[0] + mMoney[1];//拼接字段   例如 1.23+万
                        mSpannableString = new SpannableString(mString);
                        mSpannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.boc_text_color_dark_gray)), 0, mString.length() - 1,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        mSpannableString.setSpan(new AbsoluteSizeSpan(18, true), 0, mString.length() - 1,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        mSpannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.boc_text_color_dark_gray)), mString.length() - 1, mString.length(),
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
