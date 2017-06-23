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
 * Time：2016/10/26 17:27.
 * Created by lk7066 on 2016/10/26.
 * It's used to 国际商旅——出国中
 */

public class TravelInFragment extends BaseVpFragment {

    private TextView tCreditCardLoss, tDebitCardLoss;
    private View mRootView = null;
//    private ImageView imageView;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.fragment_abroadtravel_in, null);
        return mRootView;
    }

    @Override
    public void initView() {
        super.initView();
        tCreditCardLoss = (TextView)mRootView.findViewById(R.id.tv_credit_card_loss);
        tDebitCardLoss = (TextView)mRootView.findViewById(R.id.tv_debit_card_loss);
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
        addTotalAL(tCreditCardLoss, CLICKABLE, 2, 4);
        addTotalAL(tDebitCardLoss, CLICKABLE);
    }

    /**
     * 配置跳转的url
     */
    @Override
    public void setUrlmap() {
        putUrlmap(tCreditCardLoss, ModuleCode.MODULE_CREDIT_CARD_0000);//信用卡挂失
        putUrlmap(tCreditCardLoss, OverseasConst.OVERSEA_CASH_IMMERGENCY);
        putUrlmap(tDebitCardLoss, ModuleCode.MODULE_ACCOUNT_0500);//借记卡挂失
    }

    /**
     * 配置页面的String数组
     */
    @Override
    public void setStss() {
        this.setStss(getResources().getStringArray(R.array.boc_overseas_abroad_travel_in));
    }

}
