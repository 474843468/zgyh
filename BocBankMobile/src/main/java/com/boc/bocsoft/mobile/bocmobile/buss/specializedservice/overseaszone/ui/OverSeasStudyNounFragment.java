package com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.base.BaseHomeNounFragment;

/**
 * 作者：lq7090
 * 创建时间：2016/11/3.
 * 用途：出国留学攻略主界面
 */
public class OverSeasStudyNounFragment extends BaseHomeNounFragment {


    @Override
    public String[] getData() {
        return new String[]{ "com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.noun.NounStudyBranchFragment",
                "com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.noun.NounStudyKnowCountryFragment",
                "com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.noun.NounStudyPreGoFragment",
                "com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.noun.NounStudyBackFragment"
        };
    }

    @Override
    public String[] getTitle() {
        return new String[]{getResources().getString(R.string.boc_ovserseas_noun_study_1),
                getResources().getString(R.string.boc_ovserseas_noun_study_2),
                getResources().getString(R.string.boc_ovserseas_noun_study_3),
                getResources().getString(R.string.boc_ovserseas_noun_study_4)};
    }
}
