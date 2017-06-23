package com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.base.BaseHomeNounFragment;

/**
 * 作者：xwg on 16/10/31 17:37
 *  出国攻略-国际商旅 主界面
 */
public class OverseasTravleNounFragment extends BaseHomeNounFragment {

    @Override
    public String[] getData() {
        return new String[]{"com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.noun.NounPreTravelFragment",
                "com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.noun.NounTravelFragment",
                "com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.noun.NounTravelFinFragment"
        };
    }

    @Override
    public String[] getTitle() {
        return new String[]{getResources().getString(R.string.boc_ovserseas_noun_travel_pre),
                getResources().getString(R.string.boc_ovserseas_noun_travel_ing),
                getResources().getString(R.string.boc_ovserseas_noun_travel_fin)};
    }
}
