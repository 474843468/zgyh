package com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.base.BaseHomeNounFragment;

/**
 * 作者：xwg on 16/10/31 17:37
 *  出国攻略-投资移民 主界面
 */
public class OverseasInvserNounFragment extends BaseHomeNounFragment {


    @Override
    public String[] getData() {
        return new String[]{ "com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.noun.NounBankIntroduceFragment",
                "com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.noun.NounPreMigrateFragment",
                "com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.ui.noun.NounMigrateSucFragment"
        };
    }

    @Override
    public String[] getTitle() {
        return new String[]{getResources().getString(R.string.boc_ovserseas_noun_bank_introduce),
                getResources().getString(R.string.boc_ovserseas_noun_invser_pre),
                getResources().getString(R.string.boc_ovserseas_noun_invser_suc)};
    }
}
