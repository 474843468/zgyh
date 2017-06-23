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
 * 外派工作-出国后fragment
 */
public class ExpatriateAfterFragment extends BaseVpFragment {
    private TextView tExchangeSettlement,tMoreService,tCleanCollection;
    private View mRootView = null;
//    private ImageView imageView;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate( R.layout.boc_fragment_after_expatriate, null);
        return mRootView;
    }

    @Override
    public void initView() {
        super.initView();
        tExchangeSettlement=(TextView)mRootView.findViewById(R.id.exchange_settlement);
        tMoreService=(TextView)mRootView.findViewById(R.id.more_service);
        tCleanCollection=(TextView)mRootView.findViewById(R.id.clean_collection);

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
        addTotalAL(tMoreService, NORMAL);
        addTotalAL(tCleanCollection, CLICKED);
    }

    /**
     * 配置跳转的url
     */
    @Override
    public void setUrlmap() {
//        putUrlmap(tExchangeSettlement, OverseasConst.OVERSEA_EXCHANGE_SETTLEMENT);
        putUrlmap(tExchangeSettlement, ModuleCode.MODULE_SB_REMIT_0000);//结购汇
        putUrlmap(tMoreService, null);
        putUrlmap(tCleanCollection, OverseasConst.OVERSEA_CLEAN_COLLECTION);
    }

    /**
     * 配置页面的String数组
     */
    @Override
    public void setStss() {
        this.setStss(getResources().getStringArray(R.array.boc_overseas_abroad_work_after));
    }
}
