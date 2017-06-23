package com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.vpcontent;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleCode;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.base.BaseVpFragment;


/**
 * Name: liukai
 * Time：2016/10/26 17:28.
 * Created by lk7066 on 2016/10/26.
 * It's used to 国际商旅——出国后
 */

public class TravelAfterFragment extends BaseVpFragment {

    private TextView tExchangeSettlement;
    private View mRootView = null;
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.fragment_abroadtravel_after, null);
        return mRootView;
    }

    @Override
    public void initView() {
        super.initView();
        tExchangeSettlement = (TextView)mRootView.findViewById(R.id.tv_exchange_settlement);
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
        addColor(getResources().getColor(R.color.boc_common_cell_color));
        addColor(getResources().getColor(R.color.boc_text_color_yellow));
        addColor(getResources().getColor(R.color.boc_text_oversea_ew));
    }

    /**
     * 配置TextView列表 通过标识符CLICKABLE，NORMAL，CLICKED区分为哪种TextView
     */
    @Override
    public void setTotalAL() {
        addTotalAL(tExchangeSettlement, CLICKABLE);
    }

    /**
     * 配置跳转的url
     */
    @Override
    public void setUrlmap() {
//        putUrlmap(tExchangeSettlement, OverseasConst.OVERSEA_EXCHANGE_SETTLEMENT);
        putUrlmap(tExchangeSettlement, ModuleCode.MODULE_SB_REMIT_0000);//结购汇
    }

    /**
     * 配置页面的String数组
     */
    @Override
    public void setStss() {
        this.setStss(getResources().getStringArray(R.array.boc_overseas_abroad_travel_after));
    }

}
