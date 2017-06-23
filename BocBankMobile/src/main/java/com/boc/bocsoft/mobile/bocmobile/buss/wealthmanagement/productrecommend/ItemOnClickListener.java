package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.productrecommend;

import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.productrecommend.model.RecommendModel;

/**
 * Created by Wan mengxin on 2016/10/25.
 */
public interface ItemOnClickListener {
    void onClickerCode(int position, RecommendModel.OcrmDetail item, View childView);
    void onClickerBtn(int position, RecommendModel.OcrmDetail item, View childView);
}