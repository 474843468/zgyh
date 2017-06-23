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
 * 用途：签证后界面
 */
public class AfterVisaFragment extends BaseVpFragment {
    private View mRootView = null;
    private TextView tCreditCard,tCardService,tGlobleZhuoJunCard,tStudyCard,tGlobleCreditCard,tGlobleMultiCard;
//    private ImageView strategyIm;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate( R.layout.fragment_aftervisa, null);
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
//        strategyIm = (ImageView) mRootView.findViewById(R.id.strategy);
        tCreditCard=(TextView)mRootView.findViewById(R.id.tv_credit_card);
        tCardService=(TextView)mRootView.findViewById(R.id.tv_card_service);
        tGlobleZhuoJunCard=(TextView)mRootView.findViewById(R.id.tv_globle_zhuojun_card);
        tStudyCard=(TextView)mRootView.findViewById(R.id.tv_study_card);
        tGlobleCreditCard=(TextView)mRootView.findViewById(R.id.tv_globle_credit_card);
        tGlobleMultiCard=(TextView)mRootView.findViewById(R.id.tv_globle_multi_card);
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
        addTotalAL(tCreditCard,CLICKABLE);
        addTotalAL(tCardService,NORMAL);
        addTotalAL(tGlobleZhuoJunCard,CLICKED);
        addTotalAL(tStudyCard,CLICKED);
        addTotalAL(tGlobleCreditCard,CLICKED);
        addTotalAL(tGlobleMultiCard,CLICKED);


    }

    @Override
    public void setUrlmap() {
        putUrlmap(tCreditCard, ModuleCode.MODULE_EAPPLY_CREDIT_CARD_0000);//申请信用卡
        putUrlmap(tGlobleZhuoJunCard,OverseasConst.OVERSEA_MULTI_ZHUO_CARD);
        putUrlmap(tStudyCard, OverseasConst.OVERSEA_THEGREATWALL_STUDY_CARD);
        putUrlmap(tGlobleCreditCard,OverseasConst.OVERSEA_GLOBAL_CREDIT_CARD);
        putUrlmap(tGlobleMultiCard,OverseasConst.OVERSEA_GLOBAL_MULTI_CURRENCY_CARD);

    }

    @Override
    public void setStss() {
        this.setStss(getResources().getStringArray(R.array.boc_overseas_abroad_study_aftervisa));

    }

}

