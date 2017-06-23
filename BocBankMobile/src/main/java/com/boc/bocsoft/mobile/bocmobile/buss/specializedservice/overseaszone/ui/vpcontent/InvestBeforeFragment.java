package com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.vpcontent;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.OverseasConst;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.base.BaseVpFragment;


/**
 * Name: cry
 * Time：2016/10/26 15:41.
 * 投资移民-签证前 fragment
 */
public class InvestBeforeFragment extends BaseVpFragment {
    private TextView tDepositCertificationService,tMoreService,tAbroadConsultService,tTips,
            tInsuranceAbroadCard,tImmigrationFundSupervision,tAgentAccountWitness;
    private View mRootView = null;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate( R.layout.boc_fragment_before_invest, null);
        return mRootView;
    }

    @Override
    public void initView() {
        super.initView();
        tDepositCertificationService=(TextView)mRootView.findViewById(R.id.deposit_certification_service);
        tMoreService=(TextView)mRootView.findViewById(R.id.more_service);
        tAbroadConsultService=(TextView)mRootView.findViewById(R.id.abroad_consult_service);
        tTips=(TextView)mRootView.findViewById(R.id.tips);
        tInsuranceAbroadCard=(TextView)mRootView.findViewById(R.id.insurance_abroad_card);
        tImmigrationFundSupervision=(TextView)mRootView.findViewById(R.id.immigration_fund_supervision);
        tAgentAccountWitness=(TextView)mRootView.findViewById(R.id.agent_account_witness);
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
//        addColor(getResources().getColor(R.color.boc_text_click_bg_oversea));
    }

    /**
     * 配置TextView列表 通过标识符CLICKABLE，NORMAL，CLICKED区分为哪种TextView
     */
    @Override
    public void setTotalAL() {
        addTotalAL(tDepositCertificationService, CLICKABLE);
        addTotalAL(tMoreService, NORMAL);
        addTotalAL(tAbroadConsultService, CLICKED);
        addTotalAL(tTips, NORMAL);
        addTotalAL(tInsuranceAbroadCard, CLICKED);
        addTotalAL(tImmigrationFundSupervision, CLICKED);
        addTotalAL(tAgentAccountWitness, CLICKED);
    }

    /**
     * 配置跳转的url
     */
    @Override
    public void setUrlmap() {
        putUrlmap(tDepositCertificationService, OverseasConst.OVERSEA_CERTIFICATE_OF_DEPOSIT_SERVICE);
        putUrlmap(tMoreService, null);
        putUrlmap(tAbroadConsultService, OverseasConst.OVERSEA_CROSS_BORDER_CONSULTANCY_SERVICE);
        putUrlmap(tTips, null);
        putUrlmap(tInsuranceAbroadCard, OverseasConst.OVERSEA_INSURANCE_UNIVERSAL_GUARDIAN_CARD);
        putUrlmap(tImmigrationFundSupervision, OverseasConst.OVERSEA_IMMIGRATION_FUND_SUPERVISION);
        putUrlmap(tAgentAccountWitness, OverseasConst.OVERSEA_AGENT_ACCOUNT_WITNESS);
    }

    /**
     * 配置页面的String数组
     */
    @Override
    public void setStss() {
        this.setStss(getResources().getStringArray(R.array.boc_overseas_abroad_immgration_beforevisa));
    }

}
