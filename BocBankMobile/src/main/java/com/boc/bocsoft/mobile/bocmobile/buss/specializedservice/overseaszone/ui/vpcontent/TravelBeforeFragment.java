package com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.vpcontent;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleCode;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.OverseasConst;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.base.BaseVpFragment;


/**
 * Name: liukai
 * Time：2016/10/26 17:26.
 * Created by lk7066 on 2016/10/26.
 * It's used to 国际商旅——出国前
 */

public class TravelBeforeFragment extends BaseVpFragment {

    private TextView tDepositService, tExchangePurchasing, tAccidentInsurance, tCreditCard, tRecommendation, tAllThingWellCard, tMultiCurrencyCard, tGlobleCreditCard, tGlobleMultiCard, tGreatWallCard, tOutboundService, tFreeCard, tGuardianCard, tMoreService, tCurrencyCarryPermit, tTravellerCheque;
    private View mRootView = null;
//    private ImageView imageView;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.fragment_abroadtravel_before, null);
        return mRootView;
    }

    @Override
    public void initView() {
        super.initView();
        tDepositService = (TextView)mRootView.findViewById(R.id.tv_deposit_service);
        tExchangePurchasing = (TextView)mRootView.findViewById(R.id.tv_exchange_purchasing);
        tAccidentInsurance = (TextView)mRootView.findViewById(R.id.tv_accident_insurance);
        tCreditCard = (TextView)mRootView.findViewById(R.id.tv_credit_card);
        tRecommendation = (TextView)mRootView.findViewById(R.id.tv_recommendation);
        tAllThingWellCard = (TextView)mRootView.findViewById(R.id.tv_allthingwell_card);
        tGlobleMultiCard = (TextView)mRootView.findViewById(R.id.tv_globle_multi_card);
        tGlobleCreditCard = (TextView)mRootView.findViewById(R.id.tv_globle_credit_card);
        tMultiCurrencyCard = (TextView)mRootView.findViewById(R.id.tv_multi_currency_card);
        tGreatWallCard = (TextView)mRootView.findViewById(R.id.tv_greatwall_card);
        tOutboundService = (TextView)mRootView.findViewById(R.id.tv_out_bound_service);
        tFreeCard = (TextView)mRootView.findViewById(R.id.tv_free_card);
        tGuardianCard = (TextView)mRootView.findViewById(R.id.tv_guardian_card);
        tMoreService = (TextView)mRootView.findViewById(R.id.tv_more_service);
        tCurrencyCarryPermit = (TextView)mRootView.findViewById(R.id.tv_currency_carry_permit);
        tTravellerCheque = (TextView)mRootView.findViewById(R.id.tv_traveller_cheque);
//        imageView = (ImageView) mRootView.findViewById(R.id.strategy);
    }

    @Override
    public void setListener() {
        super.setListener();
//        imageView.setOnClickListener(this);
    }

    /**
     * 配置字体颜色，依次为普通字体颜色，可点击部分字体颜色，全部可点击字体颜色，全部可点击背景色（可不配置)
     */
    @Override
    public void setColors() {
        addColor(getResources().getColor(R.color.boc_common_cell_color));
        addColor(getResources().getColor(R.color.boc_text_color_yellow));
        addColor(getResources().getColor(R.color.boc_text_oversea_ew));
    }

    /**
     * 配置TextView列表 通过标识符CLICKABLE，NORMAL，CLICKED区分为哪种TextView
     */
    @Override
    public void setTotalAL() {
        addTotalAL(tDepositService, CLICKABLE);
        addTotalAL(tExchangePurchasing, CLICKABLE);
        addTotalAL(tAccidentInsurance, CLICKABLE);
        addTotalAL(tCreditCard, CLICKABLE);
        addTotalAL(tRecommendation, NORMAL);
        addTotalAL(tAllThingWellCard, CLICKED);
        addTotalAL(tGlobleMultiCard, CLICKED);
        addTotalAL(tGlobleCreditCard, CLICKED);
        addTotalAL(tMultiCurrencyCard, CLICKED);
        addTotalAL(tGreatWallCard, CLICKED);
        addTotalAL(tOutboundService, NORMAL);
        addTotalAL(tFreeCard, CLICKED);
        addTotalAL(tGuardianCard, CLICKED);
        addTotalAL(tMoreService, NORMAL);
        addTotalAL(tCurrencyCarryPermit, CLICKED);
        addTotalAL(tTravellerCheque, CLICKED);
    }

    /**
     * 配置跳转的url
     */
    @Override
    public void setUrlmap() {
        putUrlmap(tDepositService, OverseasConst.OVERSEA_CERTIFICATE_OF_DEPOSIT_SERVICE);
//        putUrlmap(tExchangePurchasing, OverseasConst.OVERSEA_EXCHANGE_PURCHASING);
        putUrlmap(tExchangePurchasing, ModuleCode.MODULE_SB_REMIT_0000);//结购汇
        putUrlmap(tAccidentInsurance, ModuleCode.MODULE_SAFETY_0000);//保险
        putUrlmap(tCreditCard, ModuleCode.MODULE_EAPPLY_CREDIT_CARD_0000);//申请信用卡
        putUrlmap(tRecommendation, null);
        putUrlmap(tAllThingWellCard, OverseasConst.OVERSEA_ALL_THINGS_WELL_CARD);
        putUrlmap(tGlobleMultiCard, OverseasConst.OVERSEA_MULTI_CURRENCY_CARD);
        putUrlmap(tGlobleCreditCard, OverseasConst.OVERSEA_GLOBAL_CREDIT_CARD);
        putUrlmap(tMultiCurrencyCard, OverseasConst.OVERSEA_GLOBAL_MULTI_CURRENCY_CARD);
        putUrlmap(tGreatWallCard, OverseasConst.OVERSEA_THEGREATWALL_INTERNATIONAL_CARD);
        putUrlmap(tOutboundService, null);
        putUrlmap(tFreeCard, OverseasConst.OVERSEA_INSURANCE_FREE_CARD);
        putUrlmap(tGuardianCard, OverseasConst.OVERSEA_INSURANCE_UNIVERSAL_GUARDIAN_CARD);
        putUrlmap(tMoreService, null);
        putUrlmap(tCurrencyCarryPermit, OverseasConst.OVERSEA_FORERGN_CURRENCY_CARRY_PERMIT);
        putUrlmap(tTravellerCheque, OverseasConst.OVERSEA_TRAVELLER_CHEQUE);
    }

    /**
     * 配置页面的String数组
     */
    @Override
    public void setStss() {
        this.setStss(getResources().getStringArray(R.array.boc_overseas_abroad_travel_before));
    }

}