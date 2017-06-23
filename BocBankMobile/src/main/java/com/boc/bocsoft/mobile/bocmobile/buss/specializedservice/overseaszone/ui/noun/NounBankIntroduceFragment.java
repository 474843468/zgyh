package com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.noun;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.base.BaseNounFragment;

/**
 * 作者：xwg on 16/11/1 10:48
 * 攻略-投资移民- 分行机构介绍 界面
 */
public class NounBankIntroduceFragment extends BaseNounFragment{

    @Override
    public String[] getTitleArr() {
        return  getResources().getStringArray(R.array.boc_overseas_noun_bank_ntroduce_title);
    }

    @Override
    public String[] getContentArr() {
        return getResources().getStringArray(R.array.boc_overseas_noun_bank_ntroduce_content);
    }
}
