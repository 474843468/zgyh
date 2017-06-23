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
 * 外派工作-出国中 fragment
 */
public class ExpatriateInFragment extends BaseVpFragment {
    private TextView tExchangeSettlement, tExchangePurchasing, tRemit, tCreditCardLoss, tDebitCardLoss,
            tInward, tManageMoney, tMoreService, tPreSettlementRemittance, tCreditInformationService;
    private View mRootView = null;
//    private ImageView imageView;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_in_expatriate, null);
        return mRootView;
    }

    @Override
    public void initView() {
        super.initView();
        tExchangeSettlement = (TextView) mRootView.findViewById(R.id.exchange_settlement);
        tExchangePurchasing = (TextView) mRootView.findViewById(R.id.exchange_purchasing);
        tRemit = (TextView) mRootView.findViewById(R.id.remit);
        tCreditCardLoss = (TextView) mRootView.findViewById(R.id.credit_card_loss);
        tDebitCardLoss = (TextView) mRootView.findViewById(R.id.debit_card_loss);
        tInward = (TextView) mRootView.findViewById(R.id.inward);
        tManageMoney = (TextView) mRootView.findViewById(R.id.manage_money);
        tMoreService = (TextView) mRootView.findViewById(R.id.more_service);
        tPreSettlementRemittance = (TextView) mRootView.findViewById(R.id.pre_settlement_remittance);
        tCreditInformationService = (TextView) mRootView.findViewById(R.id.credit_information_service);

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
        addTotalAL(tExchangeSettlement, CLICKABLE);
        addTotalAL(tExchangePurchasing, CLICKABLE);
        addTotalAL(tRemit, CLICKABLE);
        addTotalAL(tCreditCardLoss, CLICKABLE,2,4);
        addTotalAL(tDebitCardLoss, CLICKABLE);
        addTotalAL(tInward, CLICKABLE);
        addTotalAL(tManageMoney, CLICKABLE);
        addTotalAL(tMoreService, NORMAL);
        addTotalAL(tPreSettlementRemittance, CLICKED);
        addTotalAL(tCreditInformationService, CLICKED);
    }

    /**
     * 配置跳转的url
     */
    @Override
    public void setUrlmap() {
//        putUrlmap(tExchangeSettlement, OverseasConst.OVERSEA_EXCHANGE_SETTLEMENT);
//        putUrlmap(tExchangePurchasing, OverseasConst.OVERSEA_EXCHANGE_PURCHASING);
        putUrlmap(tExchangeSettlement, ModuleCode.MODULE_SB_REMIT_0000);//结购汇
        putUrlmap(tExchangePurchasing, ModuleCode.MODULE_SB_REMIT_0000);//结购汇
        putUrlmap(tRemit,  ModuleCode.MODULE_CROSS_BORDER_REMIT_0400);//向境外中行汇款
        putUrlmap(tRemit,  ModuleCode.MODULE_CROSS_BORDER_REMIT_0100);//向境外他行汇款
        putUrlmap(tCreditCardLoss, ModuleCode.MODULE_CREDIT_CARD_0000);//信用卡挂失
        putUrlmap(tCreditCardLoss, OverseasConst.OVERSEA_CASH_IMMERGENCY);
        putUrlmap(tDebitCardLoss,  ModuleCode.MODULE_ACCOUNT_0500);//借记卡挂失
        putUrlmap(tInward, OverseasConst.OVERSEA_ABOUCHEMENT);
        putUrlmap(tManageMoney, ModuleCode.MODULE_BOCINVT_0000);//中银理财
        putUrlmap(tMoreService, null);
        putUrlmap(tPreSettlementRemittance, OverseasConst.OVERSEA_PRE_EXCHANGE_SETTLEMENT);
        putUrlmap(tCreditInformationService, OverseasConst.OVERSEA_INFORMATION_SERVICE);
    }

    /**
     * 配置页面的String数组
     */
    @Override
    public void setStss() {
        this.setStss(getResources().getStringArray(R.array.boc_overseas_abroad_work_in));
    }

}
