package com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.base.BaseHomeNounFragment;

/**
 * 作者：xwg on 16/10/31 17:37
 *  出国攻略-外派工作 主界面
 */
public class OverseasWorkNounFragment extends BaseHomeNounFragment {

    @Override
    public String[] getData() {
        return new String[]{"com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.noun.NounCustemFragment",
                "com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.noun.NounConvenientFragment"
        };
    }

    @Override
    public String[] getTitle() {
        return new String[]{getResources().getString(R.string.boc_ovserseas_noun_custem),
                getResources().getString(R.string.boc_ovserseas_noun_convenient)};
    }
}
