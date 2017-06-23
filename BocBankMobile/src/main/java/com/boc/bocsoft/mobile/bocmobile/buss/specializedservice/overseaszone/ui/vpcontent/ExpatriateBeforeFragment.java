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
 * 外派工作-出国前 fragment
 */
public class ExpatriateBeforeFragment extends BaseVpFragment{
    private TextView tExchangePurchasing,tApplyforCreditCard,tCertificateOfDeposit,tRecommend,
            tEWCard1,tEWCard2,tEWCard3,tEWCard4,tMoreService,tForeignCurrencyPermit,tTravelCheque;
    private View mRootView = null;
//    private ImageView imageView;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_before_expatriate, null);
        return mRootView;
    }

    @Override
    public void initView() {
        super.initView();
        tExchangePurchasing=(TextView)mRootView.findViewById(R.id.exchange_settlement);
        tApplyforCreditCard=(TextView)mRootView.findViewById(R.id.apply_credit_card);
        tCertificateOfDeposit=(TextView)mRootView.findViewById(R.id.certificate_deposit);
        tRecommend=(TextView)mRootView.findViewById(R.id.recommend);
        tEWCard1=(TextView)mRootView.findViewById(R.id.expatriate_work_card1);
        tEWCard2=(TextView)mRootView.findViewById(R.id.expatriate_work_card2);
        tEWCard3=(TextView)mRootView.findViewById(R.id.expatriate_work_card3);
        tEWCard4=(TextView)mRootView.findViewById(R.id.expatriate_work_card4);
        tMoreService=(TextView)mRootView.findViewById(R.id.more_service);
        tForeignCurrencyPermit=(TextView)mRootView.findViewById(R.id.foreign_currency_permit);
        tTravelCheque=(TextView)mRootView.findViewById(R.id.travel_cheque);

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
        addColor(getResources().getColor(R.color.boc_text_color_dark_gray));
        addColor(getResources().getColor(R.color.boc_text_oversea_ew));
        addColor(getResources().getColor(R.color.boc_text_click_oversea_ew));
//        addColor(getResources().getColor(R.color.boc_text_click_bg_oversea));
    }

    /**
     * 配置TextView列表 通过标识符CLICKABLE，NORMAL，CLICKED区分为哪种TextView
     */
    @Override
    public void setTotalAL() {
        addTotalAL(tExchangePurchasing, CLICKABLE);
        addTotalAL(tApplyforCreditCard, CLICKABLE);
        addTotalAL(tCertificateOfDeposit, CLICKABLE);
        addTotalAL(tRecommend, NORMAL);
        addTotalAL(tEWCard1, CLICKED);
        addTotalAL(tEWCard2, CLICKED);
        addTotalAL(tEWCard3, CLICKED);
        addTotalAL(tEWCard4, CLICKED);
        addTotalAL(tMoreService, NORMAL);
        addTotalAL(tForeignCurrencyPermit, CLICKED);
        addTotalAL(tTravelCheque, CLICKED);
    }

    /**
     * 配置跳转的url
     */
    @Override
    public void setUrlmap() {
//        putUrlmap(tExchangePurchasing, OverseasConst.OVERSEA_EXCHANGE_PURCHASING);
        putUrlmap(tExchangePurchasing, ModuleCode.MODULE_SB_REMIT_0000);//结购汇
        putUrlmap(tApplyforCreditCard, ModuleCode.MODULE_EAPPLY_CREDIT_CARD_0000);//申请信用卡
        putUrlmap(tCertificateOfDeposit, OverseasConst.OVERSEA_SAVING_TESTIFY_SERVICE);
        putUrlmap(tRecommend, null);
        putUrlmap(tEWCard1, OverseasConst.OVERSEA_ALL_THINGS_WELL_CARD);
        putUrlmap(tEWCard2, OverseasConst.OVERSEA_MULTI_CURRENCY_CARD);
        putUrlmap(tEWCard3, OverseasConst.OVERSEA_GLOBAL_CREDIT_CARD);
        putUrlmap(tEWCard4, OverseasConst.OVERSEA_GLOBAL_MULTI_CURRENCY_CARD);
        putUrlmap(tMoreService, null);
        putUrlmap(tForeignCurrencyPermit, OverseasConst.OVERSEA_FOREIGN_CURRENCY_CARRY_PERMIT);
        putUrlmap(tTravelCheque, OverseasConst.OVERSEA_TRAVELLER_CHEQUE);
    }

    /**
     * 配置页面的String数组
     */
    @Override
    public void setStss() {
        this.setStss(getResources().getStringArray(R.array.boc_overseas_abroad_work_before));
    }
}
