package com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.noun;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.base.BaseNounFragment;

/**
 * 作者：xwg on 16/11/1 10:48
 * 攻略-国际商旅-境外旅游阶段 界面
 */
public class NounTravelFragment extends BaseNounFragment{

    @Override
    public String[] getTitleArr() {
        return  null;
    }

    @Override
    public String[] getContentArr() {
        return getResources().getStringArray(R.array.boc_overseas_noun_travle_ing_content);
    }
}
