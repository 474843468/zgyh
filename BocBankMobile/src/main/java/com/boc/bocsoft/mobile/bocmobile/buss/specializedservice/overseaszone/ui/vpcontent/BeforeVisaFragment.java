package com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.vpcontent;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleCode;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.OverseasConst;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.base.BaseVpFragment;


/**
 * 作者：lq7090
 * 创建时间：2016/10/26.
 * 用途：签证前界面
 */
public class BeforeVisaFragment extends BaseVpFragment {
    private View mRootView = null;
    private TextView tDepositService,tExchangePurchasing, tStudentLoan, tCreditCard, tRecommendation, tGlobleZhuoJunCard, tAllThingWellCard, tMultiCurrencyCard, tStudyCard, tMoreService, tGICCertificationOfDeposit, tInsuranceAbroadStudyCard, tTravellerCheque, tCurrencyCarryPermit, tAgentAccountWitness;
//    private ImageView strategyIm;
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate( R.layout.fragment_beforevisa, null);
        return mRootView;
    }
    /**
     * 是否显示标题栏，默认显示
     * 子类可以重写此方法，控制是否显示标题栏
     */
    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    public void initView() {
        super.initView();
        ScrollView scrollView = (ScrollView) mRootView.findViewById(R.id.applyschool_scro);
        scrollView.setVerticalScrollBarEnabled(false);
//        strategyIm = (ImageView) mRootView.findViewById(R.id.strategy);
        tDepositService =(TextView)mRootView.findViewById(R.id.tv_deposit_service);
        tExchangePurchasing=(TextView)mRootView.findViewById(R.id.tv_exchange_purchasing);
        tStudentLoan=(TextView)mRootView.findViewById(R.id.tv_student_loan);
        tCreditCard=(TextView)mRootView.findViewById(R.id.tv_credit_card);
        tRecommendation=(TextView)mRootView.findViewById(R.id.tv_recommendation);
        tGlobleZhuoJunCard =(TextView)mRootView.findViewById(R.id.tv_globle_zhuojun_card);
        tAllThingWellCard =(TextView)mRootView.findViewById(R.id.tv_allthingwell_card);
        tMultiCurrencyCard =(TextView)mRootView.findViewById(R.id.tv_multi_currency_card);
        tStudyCard =(TextView)mRootView.findViewById(R.id.tv_study_card);
        tMoreService =(TextView)mRootView.findViewById(R.id.tv_more_service);
        tGICCertificationOfDeposit =(TextView)mRootView.findViewById(R.id.tv_GIC_certification_of_deposit);
        tInsuranceAbroadStudyCard =(TextView)mRootView.findViewById(R.id.tv_insurance_abroad_study_card);
        tTravellerCheque =(TextView)mRootView.findViewById(R.id.tv_traveller_cheque);
        tCurrencyCarryPermit =(TextView)mRootView.findViewById(R.id.tv_currency_carry_permit);
        tAgentAccountWitness =(TextView)mRootView.findViewById(R.id.tv_agent_account_witness);
    }

    @Override
    public void setListener() {
        super.setListener();
//        strategyIm.setOnClickListener(this);
    }


    /**
     * 配置字体颜色，依次为普通字体颜色，可点击部分字体颜色，全部可点击字体颜色，全部可点击背景色（可不配置)
     */
    @Override
    public void setColors() {
        addColor(getResources().getColor(R.color.boc_text_color_dark_gray));
        addColor(getResources().getColor(R.color.boc_overseas_cliktext_study_blue));
        addColor(getResources().getColor(R.color.boc_overseas_cliktext_study_yellow));
    }

    @Override
    public void setTotalAL() {
        addTotalAL(tDepositService,CLICKABLE);
        addTotalAL(tExchangePurchasing,CLICKABLE);
        addTotalAL(tStudentLoan,CLICKABLE);
        addTotalAL(tCreditCard,CLICKABLE);

        addTotalAL(tRecommendation,NORMAL);

        addTotalAL(tGlobleZhuoJunCard,CLICKED);
        addTotalAL(tAllThingWellCard,CLICKED);
        addTotalAL(tMultiCurrencyCard,CLICKED);
        addTotalAL(tStudyCard,CLICKED);

        addTotalAL(tMoreService,NORMAL);

        addTotalAL(tGICCertificationOfDeposit,CLICKED);
        addTotalAL(tInsuranceAbroadStudyCard,CLICKED);
        addTotalAL(tTravellerCheque,CLICKED);
        addTotalAL(tCurrencyCarryPermit,CLICKED);
        addTotalAL(tAgentAccountWitness,CLICKED);

    }

    @Override
    public void setUrlmap() {
        putUrlmap(tDepositService,OverseasConst.OVERSEA_CERTIFICATE_OF_DEPOSIT_SERVICE);
//        putUrlmap(tExchangePurchasing,OverseasConst.OVERSEA_EXCHANGE_PURCHASING);
        putUrlmap(tExchangePurchasing, ModuleCode.MODULE_SB_REMIT_0000);//结购汇
        putUrlmap(tStudentLoan,ModuleCode.MODULE_LOAN_0000);//申请留学贷款

        putUrlmap(tCreditCard,ModuleCode.MODULE_EAPPLY_CREDIT_CARD_0000);//申请信用卡

        putUrlmap(tGlobleZhuoJunCard,OverseasConst.OVERSEA_MULTI_ZHUO_CARD);
        putUrlmap(tAllThingWellCard,OverseasConst.OVERSEA_ALL_THINGS_WELL_CARD);
        putUrlmap(tMultiCurrencyCard,OverseasConst.OVERSEA_MULTI_CURRENCY_CARD);
        putUrlmap(tStudyCard,OverseasConst.OVERSEA_THEGREATWALL_STUDY_CARD);

        putUrlmap(tGICCertificationOfDeposit,OverseasConst.OVERSEA_BANKOFCHINA_CANADA_GIC_CERTIFICATION_OF_DEPOSIT);
        putUrlmap(tInsuranceAbroadStudyCard,OverseasConst.OVERSEA_INSURANCE_ABROADSTUDY_CARD);
        putUrlmap(tTravellerCheque,OverseasConst.OVERSEA_TRAVELLER_CHEQUE);
        putUrlmap(tCurrencyCarryPermit,OverseasConst.OVERSEA_FORERGN_CURRENCY_CARRY_PERMIT);
        putUrlmap(tAgentAccountWitness,OverseasConst.OVERSEA_AGENT_ACCOUNT_WITNESS);

    }

    @Override
    public void setStss() {
        setStss(getResources().getStringArray(R.array.boc_overseas_abroad_study_beforevisa));

    }


}


