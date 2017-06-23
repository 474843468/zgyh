package com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.ui;

import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRowGroup;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.DetailModelUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.model.FinanceICTransferViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.model.MedicalTransferDetailQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.model.TransDetailViewModel;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

/**
 * 交易明细 -- 详情页面
 * Created by wangf on 2016/6/23.
 */
public class TransDetailInfoFragment extends BussFragment {

    private View rootView;
    private DetailTableHead detailTableHead;
    private DetailContentView detailContentView;
    private DetailTableRowGroup detailTableRowGroup;

    //当前所查询账户详情类型
    private int currentDetailAccountType;


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_account_transdetail_info, null);

        currentDetailAccountType = getArguments().getInt(TransDetailFragment.DETAIL_TYPE, TransDetailFragment.DETAIL_ACCOUNT_TYPE_ALL);

        return rootView;
    }

    @Override
    public void initView() {
        detailTableHead = (DetailTableHead) rootView.findViewById(R.id.head_view);
        detailContentView = (DetailContentView) rootView.findViewById(R.id.body_view);
        detailTableRowGroup = (DetailTableRowGroup) rootView.findViewById(R.id.bottom_view);
        detailTableRowGroup.setVisibility(View.GONE);
    }

    @Override
    public void initData() {

        switch (currentDetailAccountType) {
            case TransDetailFragment.DETAIL_ACCOUNT_TYPE_ALL://大红头进入的全部查询
                TransDetailViewModel.ListBean listBean = getArguments().getParcelable(TransDetailFragment.DETAIL_INFO);
                setDetailCommonData(listBean);
                break;
            case TransDetailFragment.DETAIL_ACCOUNT_TYPE_COMMON://普通单账户查询
                TransDetailViewModel.ListBean commonListBean = getArguments().getParcelable(TransDetailFragment.DETAIL_INFO);
                setDetailCommonData(commonListBean);
                break;
            case TransDetailFragment.DETAIL_ACCOUNT_TYPE_MEDICAL://医保账户明细查询
                MedicalTransferDetailQueryViewModel.ListBean medicalListBean = getArguments().getParcelable(TransDetailFragment.DETAIL_INFO);
                setDetailMedicalData(medicalListBean);
                break;
            case TransDetailFragment.DETAIL_ACCOUNT_TYPE_FINANCE://电子现金账户明细查询
                FinanceICTransferViewModel.ListBean financeListBean = getArguments().getParcelable(TransDetailFragment.DETAIL_INFO);
                setDetailFinanceData(financeListBean);
                break;
        }
    }


    /**
     * 普通活期的详情数据
     */
    private void setDetailCommonData(TransDetailViewModel.ListBean listBean) {
        setInfoHeadData(listBean.getAmount(), listBean.getCurrency(), listBean.getCashRemit());
        if (!StringUtils.isEmpty(listBean.getPayeeAccountNumber())) {
//            if ("转账支出".equals(listBean.getBusinessDigest()) || "转账收入".equals(listBean.getBusinessDigest())) {
                detailTableHead.addDetail("对方账户名称/账号", listBean.getPayeeAccountName() + " " + NumberUtils.formatCardNumberStrong(listBean.getPayeeAccountNumber()));
//            } else {
//                detailTableHead.setDetailVisable(false);
//            }
        } else {
            detailTableHead.setDetailVisable(false);
        }

        if (listBean.getPaymentDate() != null) {
            detailContentView.addDetailRow("交易日期", listBean.getPaymentDate().format(DateFormatters.dateFormatter1));
        }
        if (!StringUtils.isEmpty(listBean.getBusinessDigest())) {
            detailContentView.addDetailRow("业务摘要", listBean.getBusinessDigest());
        }
        if(!StringUtils.isEmpty(listBean.getBalance())){
            detailContentView.addDetailRow("余额", PublicCodeUtils.getCurrency(mContext, listBean.getCurrency()) +
                    getStrCashRemit(listBean.getCashRemit()) + " " +
                    MoneyUtils.transMoneyFormat(listBean.getBalance(), listBean.getCurrency()));
        }
        if (!StringUtils.isEmpty(listBean.getTransChnl())) {
            detailContentView.addDetailRow("交易渠道/场所", listBean.getTransChnl());
        }
        if (!StringUtils.isEmpty(listBean.getFurInfo())) {
            detailContentView.addDetailRowNotLine("附言", listBean.getFurInfo());
        }
    }

    /**
     * 医保账户的详情数据
     */
    private void setDetailMedicalData(MedicalTransferDetailQueryViewModel.ListBean listBean) {
        setInfoHeadData(listBean.getAmount(), listBean.getCurrency(), listBean.getCashRemit());
        if (!StringUtils.isEmpty(listBean.getPayeeAccountNumber())) {
//            if ("转账支出".equals(listBean.getBusinessDigest()) || "转账收入".equals(listBean.getBusinessDigest())) {
                detailTableHead.addDetail("对方账户名称/账号", listBean.getPayeeAccountName() + " " + NumberUtils.formatCardNumberStrong(listBean.getPayeeAccountNumber()));
//            } else {
//                detailTableHead.setDetailVisable(false);
//            }
        } else {
            detailTableHead.setDetailVisable(false);
        }

        if (listBean.getPaymentDate() != null) {
            detailContentView.addDetailRow("交易日期", listBean.getPaymentDate().format(DateFormatters.dateFormatter1));
        }
        if (!StringUtils.isEmpty(listBean.getBusinessDigest())) {
            detailContentView.addDetailRow("业务摘要", listBean.getBusinessDigest());
        }
        if (!StringUtils.isEmpty(listBean.getBalance())){
            detailContentView.addDetailRow("余额", PublicCodeUtils.getCurrency(mContext, listBean.getCurrency()) +
                    getStrCashRemit(listBean.getCashRemit()) + " " +
                    MoneyUtils.transMoneyFormat(listBean.getBalance(), listBean.getCurrency()));
        }
        if (!StringUtils.isEmpty(listBean.getTransChnl())) {
            detailContentView.addDetailRow("交易渠道/场所", listBean.getTransChnl());
        }
        if (!StringUtils.isEmpty(listBean.getFurInfo())) {
            detailContentView.addDetailRowNotLine("附言", listBean.getFurInfo());
        }
    }

    /**
     * 电子现金账户的详情数据
     */
    private void setDetailFinanceData(FinanceICTransferViewModel.ListBean listBean) {
        if (listBean.isAmountFlag()) {
            detailTableHead.updateData("支出金额" + "（" + PublicCodeUtils.getCurrency(mContext, listBean.getCurrency()) +
                            getStrCashRemit(listBean.getCashRemit()) + "）",
                    "-" + MoneyUtils.transMoneyFormat(listBean.getAmount(), listBean.getCurrency()));
        } else {
            detailTableHead.updateData("收入金额" + "（" + PublicCodeUtils.getCurrency(mContext, listBean.getCurrency()) +
                            getStrCashRemit(listBean.getCashRemit()) + "）",
                    MoneyUtils.transMoneyFormat(listBean.getAmount(), listBean.getCurrency()));
        }
//        setInfoHeadData(String.valueOf(listBean.getAmount()), listBean.getCurrency(), listBean.getCashRemit());
        detailTableHead.setDetailVisable(false);

        detailContentView.addDetailRow("交易日期", listBean.getReturnDate().format(DateFormatters.dateFormatter1));
        detailContentView.addDetailRowNotLine("交易类型", DetailModelUtils.getTransferTypeString(listBean.getTransferType()));
//        detailContentView.addDetailRowNotLine("余额", MoneyUtils.transMoneyFormat(listBean.getBalance(), listBean.getCurrency()));
//        detailContentView.addDetailRowNotLine("余额",
//                PublicCodeUtils.getCurrency(mContext, listBean.getCurrency()) +
//                        getStrCashRemit(listBean.getCashRemit()) + " " +
//                        MoneyUtils.transMoneyFormat(listBean.getBalance(), listBean.getCurrency()));
    }


    /**
     * * 设置明细头部的信息
     *
     * @param amount    交易金额
     * @param currency  币种
     * @param cashRemit 钞汇
     */
    private void setInfoHeadData(String amount, String currency, String cashRemit) {
        if (isAmountPay(amount)) {
            detailTableHead.updateData("支出金额" + "（" + PublicCodeUtils.getCurrency(mContext, currency) +
                            getStrCashRemit(cashRemit) + "）",
                    MoneyUtils.transMoneyFormat(getStrAmount(amount), currency));
        } else {
            detailTableHead.updateData("收入金额" + "（" + PublicCodeUtils.getCurrency(mContext, currency) +
                            getStrCashRemit(cashRemit) + "）",
                    MoneyUtils.transMoneyFormat(getStrAmount(amount), currency));
        }
    }

    /**
     * 根据金额判断为支出还是收入
     *
     * @param strAmount
     * @return
     */
    private boolean isAmountPay(String strAmount) {
        return strAmount.contains("-");
    }

    /**
     * 去掉金额前的负号
     *
     * @param strAmount
     * @return
     */
    private String getStrAmount(String strAmount) {
        if (strAmount.indexOf("-") == 0) {
            return strAmount.substring(1, strAmount.length());
        } else {
            return strAmount;
        }
    }

    /**
     * 获取钞汇信息
     *
     * @param cashRemit
     * @return
     */
    private String getStrCashRemit(String cashRemit) {
        String strCashRemit = "";
        if (cashRemit == null || "".equals(cashRemit)) {
            return strCashRemit;
        }
        if ("01".equals(cashRemit)) {
            strCashRemit = "/钞";
        } else if ("02".equals(cashRemit)) {
            strCashRemit = "/汇";
        }
        return strCashRemit;
    }


    @Override
    protected boolean isHaveTitleBarView() {
        return true;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_transfer_details_title);
    }
}
