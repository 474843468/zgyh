package com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleCode;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.OverseasConst;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.base.BaseVpFragment;


/**
 * Name: liukai
 * Time：2016/10/31 17:28.
 * Created by lk7066 on 2016/10/31.
 * It's used to 跨境专区——来华人士
 */

public class PeopleInChinaFragment extends BaseVpFragment {

    private TextView tExchangeSettlement, tExchangePurchasing, tWealthManagement, tDeposit, tCreditCard, tMoreService, tCleanCollection, tTravellerCheque;
    private View mRootView = null;
    private ImageView imageView,leftIconLHRS;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate( R.layout.fragment_peopleinchina, null);
        return mRootView;
    }

    @Override
    public void initView() {
        super.initView();
        tExchangeSettlement = (TextView)mRootView.findViewById(R.id.tv_exchange_settlement);
        tExchangePurchasing = (TextView)mRootView.findViewById(R.id.tv_exchange_purchasing);
        tWealthManagement = (TextView)mRootView.findViewById(R.id.tv_wealth_management);
        tDeposit = (TextView)mRootView.findViewById(R.id.tv_deposit);
        tCreditCard = (TextView)mRootView.findViewById(R.id.tv_credit_card);
        tMoreService = (TextView)mRootView.findViewById(R.id.tv_more_service);
        tCleanCollection = (TextView)mRootView.findViewById(R.id.tv_clean_collection);
        tTravellerCheque = (TextView)mRootView.findViewById(R.id.tv_traveller_cheque);
        leftIconLHRS = (ImageView)mRootView.findViewById(R.id.leftIconLHRS);
    }

    /**
     * 配置字体颜色，依次为普通字体颜色，可点击部分字体颜色，全部可点击字体颜色，全部可点击背景色（可不配置)
     */
    @Override
    public void setColors() {
        addColor(getResources().getColor(R.color.boc_common_cell_color));
        addColor(getResources().getColor(R.color.boc_overseas_cliktext_study_yellow));
        addColor(getResources().getColor(R.color.boc_overseas_clicktext_blue));
    }

    /**
     * 配置TextView列表 通过标识符CLICKABLE，NORMAL，CLICKED区分为哪种TextView
     */
    @Override
    public void setTotalAL() {
        addTotalAL(tExchangeSettlement, CLICKABLE);
        addTotalAL(tExchangePurchasing, CLICKABLE);
        addTotalAL(tWealthManagement, CLICKABLE);
        addTotalAL(tDeposit, CLICKABLE);
        addTotalAL(tCreditCard, CLICKABLE);
        addTotalAL(tMoreService, NORMAL);
        addTotalAL(tCleanCollection, CLICKED);
        addTotalAL(tTravellerCheque, CLICKED);
    }

    /**
     * 配置跳转的url
     */
    @Override
    public void setUrlmap() {
//        putUrlmap(tExchangeSettlement, OverseasConst.OVERSEA_EXCHANGE_SETTLEMENT);
        putUrlmap(tExchangeSettlement, ModuleCode.MODULE_SB_REMIT_0000);//结购汇
        putUrlmap(tExchangePurchasing,  ModuleCode.MODULE_SB_REMIT_0000);
        putUrlmap(tWealthManagement, ModuleCode.MODULE_BOCINVT_0000);//中银理财
        putUrlmap(tDeposit, ModuleCode.MODULE_DEPOSIT_0000); //定期存款、通知存款
        putUrlmap(tCreditCard, ModuleCode.MODULE_EAPPLY_CREDIT_CARD_0000);//申请信用卡
        putUrlmap(tMoreService, null);
        putUrlmap(tCleanCollection, OverseasConst.OVERSEA_CLEAN_COLLECTION);
        putUrlmap(tTravellerCheque, OverseasConst.OVERSEA_TRAVELLER_CHEQUE);
    }

    /**
     * 配置页面的String数组
     */
    @Override
    public void setStss() {
        this.setStss(getResources().getStringArray(R.array.boc_overseas_abroad_people_in_china));
    }

    @Override
    public void setListener() {
        //title左侧返回按钮
        leftIconLHRS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleLeftIconClick();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        OverseasHomeFragment fragment=findFragment(OverseasHomeFragment.class);
        if (fragment!=null)
            fragment.recoverView();
    }

}
