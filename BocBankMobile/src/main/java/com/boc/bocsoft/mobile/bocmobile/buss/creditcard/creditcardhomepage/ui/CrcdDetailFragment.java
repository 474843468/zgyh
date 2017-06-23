package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.CrcdMenuFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.model.CrcdInfoBean;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.model.CrcdModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.utils.CrcdResultConvertUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.widget.DetailListDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.widget.DrawRoundView;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDate;

/**
 * 信用卡详情
 * Created by wangf on 2016/11/22.
 */
public class CrcdDetailFragment extends BussFragment {
    private View mRootView;

    private TextView tv_detail_more_limit;
    private DrawRoundView drv_detail_round_limit;
    private LinearLayout ll_detail_list;
    private TextView btn_detail_report;

    private CrcdModel mCrcdModel;//信用卡首页主model

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = View.inflate(mContext, R.layout.boc_fragment_credit_card_detail, null);
        return mRootView;
    }

    @Override
    public void initView() {
        tv_detail_more_limit = (TextView) mRootView.findViewById(R.id.tv_detail_more_limit);
        drv_detail_round_limit = (DrawRoundView) mRootView.findViewById(R.id.drv_detail_round_limit);
        ll_detail_list = (LinearLayout) mRootView.findViewById(R.id.ll_detail_list);
        btn_detail_report = (TextView) mRootView.findViewById(R.id.btn_detail_report);
    }

    @Override
    public void initData() {
        showDrawRoundView();
        showDetailListView(buildCardName(), buildCardValues());
        btn_detail_report.setText(getString(R.string.boc_crcd_card_more_loss));
    }

    @Override
    protected String getTitleValue() {
        if (mCrcdModel == null)
            mCrcdModel = getArguments().getParcelable(CrcdHomeFragment.CRCD_INFO);
        return NumberUtils.formatCardNumberStrong(mCrcdModel.getAccountBean().getAccountNumber());
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    public void setListener() {
        tv_detail_more_limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DetailListDialog(mContext, buildLimitName(), buildLimitValues()).show();
            }
        });

        btn_detail_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show("挂失/补卡");
            }
        });
    }


    @Override
    protected void titleRightIconClick() {
        super.titleRightIconClick();
        Bundle bundle = new Bundle();
        bundle.putInt(CrcdMenuFragment.MENU, CrcdMenuFragment.CREDIT_MORE_CARD);
        bundle.putParcelable(CrcdMenuFragment.MENU_ACC, mCrcdModel.getAccountBean());
        CrcdMenuFragment crcdMenuFragment = new CrcdMenuFragment();
        crcdMenuFragment.setArguments(bundle);
        start(crcdMenuFragment);
    }

    /**
     * 设置页面球体数据
     */
    private void showDrawRoundView() {
        boolean isTotalLimtGreater = CrcdResultConvertUtils.isCompareAmountCanNext(mCrcdModel.getActList().get(0).getToltalBalance(),
                mCrcdModel.getActList().get(0).getTotalLimt());
        if (mCrcdModel.getActList().size() > 1) {
            String currencySymbol0 = PublicCodeUtils.getCurrencySymbol(mContext, mCrcdModel.getActList().get(0).getCurrency());
            String toltalBalance0 = MoneyUtils.transMoneyFormat(mCrcdModel.getActList().get(0).getToltalBalance(), mCrcdModel.getActList().get(0).getCurrency());
            String totalLimit0 = MoneyUtils.transMoneyFormat(mCrcdModel.getActList().get(0).getTotalLimt(), mCrcdModel.getActList().get(0).getCurrency());
            String currencySymbol1 = PublicCodeUtils.getCurrencySymbol(mContext, mCrcdModel.getActList().get(1).getCurrency());
            String toltalBalance1 = MoneyUtils.transMoneyFormat(mCrcdModel.getActList().get(1).getToltalBalance(), mCrcdModel.getActList().get(1).getCurrency());
            String totalLimit1 = MoneyUtils.transMoneyFormat(mCrcdModel.getActList().get(1).getTotalLimt(), mCrcdModel.getActList().get(1).getCurrency());
            String[] textAvailableLimit = new String[]{getString(R.string.boc_crcd_detail_total_balance), currencySymbol0 + toltalBalance0, currencySymbol1 + toltalBalance1};
            String[] textCreditLimit = new String[]{getString(R.string.boc_crcd_detail_total_limit), currencySymbol0 + totalLimit0, currencySymbol1 + totalLimit1};
            drv_detail_round_limit.setLimitData(!isTotalLimtGreater, textAvailableLimit, textCreditLimit);
        } else {
            String currencySymbol = PublicCodeUtils.getCurrencySymbol(mContext, mCrcdModel.getActList().get(0).getCurrency());
            String toltalBalance = MoneyUtils.transMoneyFormat(mCrcdModel.getActList().get(0).getToltalBalance(), mCrcdModel.getActList().get(0).getCurrency());
            String totalLimit = MoneyUtils.transMoneyFormat(mCrcdModel.getActList().get(0).getTotalLimt(), mCrcdModel.getActList().get(0).getCurrency());
            String[] textAvailableLimit = new String[]{getString(R.string.boc_crcd_detail_total_balance), currencySymbol + toltalBalance};
            String[] textCreditLimit = new String[]{getString(R.string.boc_crcd_detail_total_limit), currencySymbol + totalLimit};
            drv_detail_round_limit.setLimitData(!isTotalLimtGreater, textAvailableLimit, textCreditLimit);
        }
    }


    /**
     * 设置页面卡详情数据
     *
     * @param names
     * @param values
     */
    private void showDetailListView(String[] names, String[] values) {
        ll_detail_list.removeAllViews();
        for (int i = 0; i < names.length; i++) {
            View view = View.inflate(mContext, R.layout.boc_fragment_credit_card_detail_item, null);
            TextView txtName = (TextView) view.findViewById(R.id.txt_name);
            TextView txtValue = (TextView) view.findViewById(R.id.txt_content);
            txtName.setText(names[i]);
            txtValue.setText(values[i]);
            ll_detail_list.addView(view);
        }
    }

    /**
     * 更多额度 title
     * @return
     */
    public String[] buildLimitName() {
        String[] names = new String[5];
        int index = 0;
        names[index++] = getString(R.string.boc_crcd_detail_total_limit);
        names[index++] = getString(R.string.boc_crcd_detail_total_balance);
        names[index++] = getString(R.string.boc_crcd_detail_cash_limit);
        names[index++] = getString(R.string.boc_crcd_detail_cash_balance);
        names[index++] = getString(R.string.boc_crcd_detail_pay_avai_balance);
        return names;
    }

    /**
     * 更多额度 values
     * @return
     */
    public String[] buildLimitValues() {
        String[] names = new String[5];
        int index = 0;
        if (mCrcdModel.getActList().size() > 1) {
            CrcdInfoBean infoBean0 = mCrcdModel.getActList().get(0);
            CrcdInfoBean infoBean1 = mCrcdModel.getActList().get(1);
            String currency0 = PublicCodeUtils.getCurrency(mContext, infoBean0.getCurrency());
            String currency1 = PublicCodeUtils.getCurrency(mContext, infoBean1.getCurrency());
            names[index++] = currency0 + MoneyUtils.transMoneyFormat(infoBean0.getTotalLimt(), infoBean0.getCurrency()) + "\n" +
                    currency1 + MoneyUtils.transMoneyFormat(infoBean1.getTotalLimt(), infoBean1.getCurrency());// 授信额度
            names[index++] = currency0 + MoneyUtils.transMoneyFormat(infoBean0.getToltalBalance(), infoBean0.getCurrency()) + "\n" +
                    currency1 + MoneyUtils.transMoneyFormat(infoBean1.getToltalBalance(), infoBean1.getCurrency());// 可用额度
            names[index++] = currency0 + MoneyUtils.transMoneyFormat(infoBean0.getCashLimit(), infoBean0.getCurrency()) + "\n" +
                    currency1 + MoneyUtils.transMoneyFormat(infoBean1.getCashLimit(), infoBean1.getCurrency());// 取现额度
            names[index++] = currency0 + MoneyUtils.transMoneyFormat(infoBean0.getCashBalance(), infoBean0.getCurrency()) + "\n" +
                    currency1 + MoneyUtils.transMoneyFormat(infoBean1.getCashBalance(), infoBean1.getCurrency());// 取现可用额
            names[index++] = currency0 + MoneyUtils.transMoneyFormat(infoBean0.getDividedPayAvaiBalance(), infoBean0.getCurrency()) + "\n" +
                    currency1 + MoneyUtils.transMoneyFormat(infoBean1.getDividedPayAvaiBalance(), infoBean1.getCurrency());// 分期可用额
        } else {
            CrcdInfoBean infoBean = mCrcdModel.getActList().get(0);
            String currency = PublicCodeUtils.getCurrency(mContext, infoBean.getCurrency());
            names[index++] = currency + MoneyUtils.transMoneyFormat(infoBean.getTotalLimt(), infoBean.getCurrency());// 授信额度
            names[index++] = currency + MoneyUtils.transMoneyFormat(infoBean.getToltalBalance(), infoBean.getCurrency());// 可用额度
            names[index++] = currency + MoneyUtils.transMoneyFormat(infoBean.getCashLimit(), infoBean.getCurrency());// 取现额度
            names[index++] = currency + MoneyUtils.transMoneyFormat(infoBean.getCashBalance(), infoBean.getCurrency());// 取现可用额
            names[index++] = currency + MoneyUtils.transMoneyFormat(infoBean.getDividedPayAvaiBalance(), infoBean.getCurrency());// 分期可用额
        }
        return names;
    }

    /**
     * 账户详情 title
     * @return
     */
    public String[] buildCardName() {
        String[] names = new String[8];
        int index = 0;
        names[index++] = getString(R.string.boc_crcd_detail_acc_name);
        names[index++] = getString(R.string.boc_crcd_detail_acc_bank);
        names[index++] = getString(R.string.boc_crcd_detail_due_date);
        names[index++] = getString(R.string.boc_crcd_detail_crcd_point);
        names[index++] = getString(R.string.boc_crcd_detail_car_status);
        names[index++] = getString(R.string.boc_crcd_detail_car_flag);
        names[index++] = getString(R.string.boc_crcd_detail_start_date);
        names[index++] = getString(R.string.boc_crcd_detail_annual_fee);
        return names;
    }


    /**
     * 账户详情 values
     * @return
     */
    public String[] buildCardValues() {
        String[] values = new String[8];
        int index = 0;
        values[index++] = mCrcdModel.getAcctName();
        values[index++] = mCrcdModel.getAcctBank();
        if (!StringUtils.isEmptyOrNull(mCrcdModel.getDueDate()))
            values[index++] = LocalDate.parse(mCrcdModel.getDueDate(), DateFormatters.dateFormatter2).format(DateFormatters.dateFormatter1);
        else
            values[index++] = "";
        values[index++] = mCrcdModel.getCrcdPoint();
        values[index++] = CrcdResultConvertUtils.convertCarStatus(mCrcdModel.getCarStatus());
        values[index++] = "1".equals(mCrcdModel.getCarFlag())? "主卡" : "副卡";
        if (!StringUtils.isEmptyOrNull(mCrcdModel.getStartDate()))
            values[index++] = LocalDate.parse(mCrcdModel.getStartDate(), DateFormatters.dateFormatter2).format(DateFormatters.dateFormatter1);
        else
            values[index++] = "";
        values[index++] = "1".equals(mCrcdModel.getAnnualFee())? "免年费" : "不免年费";
        return values;
    }


}
