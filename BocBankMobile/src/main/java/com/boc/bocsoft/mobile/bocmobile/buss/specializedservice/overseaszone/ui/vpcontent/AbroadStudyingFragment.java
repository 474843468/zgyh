package com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.vpcontent;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleCode;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.OverseasConst;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.base.BaseVpFragment;


/**
 * 作者：lq7090
 * 创建时间：2016/10/26.
 * 用途：留学中界面
 */
public class AbroadStudyingFragment extends BaseVpFragment  {
    private View mRootView = null;
    private TextView tRemittance, tCreditCardLoss, tDebitCardLoss;
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.fragment_abroadstudying, null);
        return mRootView;
    }

    @Override
    public void initView() {
        super.initView();
        tRemittance = (TextView) mRootView.findViewById(R.id.tv_remittance);
        tCreditCardLoss = (TextView) mRootView.findViewById(R.id.tv_credit_card_loss);
        tDebitCardLoss = (TextView) mRootView.findViewById(R.id.tv_debit_card_loss);
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
        addColor(getResources().getColor(R.color.boc_overseas_cliktext_study_blue));
        addColor(getResources().getColor(R.color.boc_overseas_cliktext_study_yellow));
    }

    /**
     * 配置TextView列表 通过标识符CLICKABLE，NORMAL，CLICKED区分为哪种TextView
     */
    @Override
    public void setTotalAL() {
        addTotalAL(tRemittance,CLICKABLE);
        addTotalAL(tCreditCardLoss,CLICKABLE,2,4);
        addTotalAL(tDebitCardLoss,CLICKABLE);
    }

    /**
     * 配置跳转的url
     */
    @Override
    public void setUrlmap() {
        putUrlmap(tRemittance, ModuleCode.MODULE_CROSS_BORDER_REMIT_0400);//向境外中行汇款
        putUrlmap(tRemittance, ModuleCode.MODULE_CROSS_BORDER_REMIT_0100);//向境外他行汇款
        putUrlmap(tCreditCardLoss, ModuleCode.MODULE_CREDIT_CARD_0000);//信用卡挂失
        putUrlmap(tCreditCardLoss, OverseasConst.OVERSEA_CASH_IMMERGENCY);
        putUrlmap(tDebitCardLoss, ModuleCode.MODULE_ACCOUNT_0500);//借记卡挂失
    }

    /**
     * 配置页面的String数组
     */
    @Override
    public void setStss() {
        this.setStss(getResources().getStringArray(R.array.boc_overseas_abroad_study_studying));
    }

}


