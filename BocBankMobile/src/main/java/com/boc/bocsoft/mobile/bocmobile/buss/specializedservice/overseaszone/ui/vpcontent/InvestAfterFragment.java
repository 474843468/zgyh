package com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.vpcontent;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleCode;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.OverseasConst;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.base.BaseVpFragment;

/**
 * Name: cry
 * Time：2016/10/26 15:41.
 * 投资移民-签证后 fragment
 */
public class InvestAfterFragment extends BaseVpFragment {
    private TextView tExchangeSettlement, tExchangePurchasing, tRemit, tApplyForCreditCard, tInward,
            tCommand, tIICard1, tIICard2, tIICard3, tIICard4, tMoreService, tForeignCurrencyPermit,
            tAbroadPersonalLoan_sg, tCreditInformationService, tMalaysiaPlan;
    private View mRootView = null;
    private TextView tAbroadPersonalLoan_my,tAbroadPersonalLoan_ca,tAbroadPersonalLoan_uk;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_after_invest, null);
        return mRootView;
    }

    @Override
    public void initView() {
        super.initView();
        tExchangeSettlement = (TextView) mRootView.findViewById(R.id.exchange_settlement);
        tExchangePurchasing = (TextView) mRootView.findViewById(R.id.exchange_purchasing);
        tRemit = (TextView) mRootView.findViewById(R.id.remit);
        tApplyForCreditCard = (TextView) mRootView.findViewById(R.id.apply_credit_card);
        tInward = (TextView) mRootView.findViewById(R.id.inward);
        tCommand = (TextView) mRootView.findViewById(R.id.command);
        tIICard1 = (TextView) mRootView.findViewById(R.id.Invest_card1);
        tIICard2 = (TextView) mRootView.findViewById(R.id.invest_card2);
        tIICard3 = (TextView) mRootView.findViewById(R.id.invest_card3);
        tIICard4 = (TextView) mRootView.findViewById(R.id.invest_card4);
        tMoreService = (TextView) mRootView.findViewById(R.id.more_service);
        tForeignCurrencyPermit = (TextView) mRootView.findViewById(R.id.foreign_currency_permit);
        tAbroadPersonalLoan_sg = (TextView) mRootView.findViewById(R.id.abroad_personal_loan_sg);
        tAbroadPersonalLoan_my = (TextView) mRootView.findViewById(R.id.abroad_personal_loan_my);
        tAbroadPersonalLoan_ca = (TextView) mRootView.findViewById(R.id.abroad_personal_loan_ca);
        tAbroadPersonalLoan_uk = (TextView) mRootView.findViewById(R.id.abroad_personal_loan_uk);
        tCreditInformationService = (TextView) mRootView.findViewById(R.id.credit_information_service);
        tMalaysiaPlan = (TextView) mRootView.findViewById(R.id.malaysia_plan);

    }

    @Override
    public void setListener() {
        super.setListener();
    }

    /**
     * 配置字体颜色，依次为普通字体颜色，可点击部分字体颜色，全部可点击字体颜色，全部可点击背景色（可不配置)
     */
    @Override
    public void setColors() {
        addColor(getResources().getColor(R.color.boc_text_color_dark_gray));
        addColor(getResources().getColor(R.color.boc_text_oversea_ew));
        addColor(getResources().getColor(R.color.boc_text_click_oversea_ew));
    }

    /**
     * 配置TextView列表 通过标识符CLICKABLE，NORMAL，CLICKED区分为哪种TextView
     */
    @Override
    public void setTotalAL() {
        addTotalAL(tExchangeSettlement, CLICKABLE);
        addTotalAL(tExchangePurchasing, CLICKABLE);
        addTotalAL(tRemit, CLICKABLE);
        addTotalAL(tApplyForCreditCard, CLICKABLE);
        addTotalAL(tInward, CLICKABLE);
        addTotalAL(tCommand, NORMAL);
        addTotalAL(tIICard1, CLICKED);
        addTotalAL(tIICard2, CLICKED);
        addTotalAL(tIICard3, CLICKED);
        addTotalAL(tIICard4, CLICKED);
        addTotalAL(tMoreService, NORMAL);
        addTotalAL(tForeignCurrencyPermit, CLICKED);
        addTotalAL(tAbroadPersonalLoan_sg, CLICKED);
        addTotalAL(tAbroadPersonalLoan_my, CLICKED);
        addTotalAL(tAbroadPersonalLoan_ca, CLICKED);
        addTotalAL(tAbroadPersonalLoan_uk, CLICKED);
        addTotalAL(tCreditInformationService, CLICKED);
        addTotalAL(tMalaysiaPlan, CLICKED);
    }
    /**
     * 配置跳转的url
     */
    @Override
    public void setUrlmap() {
        putUrlmap(tExchangeSettlement, ModuleCode.MODULE_SB_REMIT_0000);//结购汇
        putUrlmap(tExchangePurchasing, ModuleCode.MODULE_SB_REMIT_0000);//结购汇
        putUrlmap(tRemit,  ModuleCode.MODULE_CROSS_BORDER_REMIT_0400);//向境外中行汇款
        putUrlmap(tRemit,  ModuleCode.MODULE_CROSS_BORDER_REMIT_0100);//向境外他行汇款
        putUrlmap(tApplyForCreditCard, ModuleCode.MODULE_EAPPLY_CREDIT_CARD_0000);//申请信用卡
        putUrlmap(tInward, OverseasConst.OVERSEA_ABOUCHEMENT);
        putUrlmap(tCommand, null);
        putUrlmap(tIICard1, OverseasConst.OVERSEA_ALL_THINGS_WELL_CARD);
        putUrlmap(tIICard2, OverseasConst.OVERSEA_MULTI_CURRENCY_CARD);
        putUrlmap(tIICard3, OverseasConst.OVERSEA_GLOBAL_CREDIT_CARD);
        putUrlmap(tIICard4, OverseasConst.OVERSEA_GLOBAL_MULTI_CURRENCY_CARD);
        putUrlmap(tMoreService, null);
        putUrlmap(tForeignCurrencyPermit, OverseasConst.OVERSEA_FOREIGN_CURRENCY_CARRY_PERMIT);
        putUrlmap(tAbroadPersonalLoan_sg, OverseasConst.OVERSEA_PERSONAL_LOAN_SINGAPORE);
        putUrlmap(tAbroadPersonalLoan_my, OverseasConst.OVERSEA_PERSONAL_LOAN_MALAYSIA);
        putUrlmap(tAbroadPersonalLoan_ca, OverseasConst.OVERSEA_PERSONAL_LOAN_CANADA);
        putUrlmap(tAbroadPersonalLoan_uk, OverseasConst.OVERSEA_PERSONAL_LOAN_ENGLISH);
        putUrlmap(tCreditInformationService, OverseasConst.OVERSEA_INFORMATION_SERVICE);
        putUrlmap(tMalaysiaPlan, OverseasConst.OVERSEA_MALAYSIA_MY_SECOND_HOME);
    }
    /**
     * 配置页面的String数组
     */
    @Override
    public void setStss() {
        this.setStss(getResources().getStringArray(R.array.boc_overseas_abroad_immgration_aftervisa));
    }

}
