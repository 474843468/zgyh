package com.boc.bocsoft.mobile.bocmobile.base.activity.anim;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * Created by YoKeyword on 16/2/5.
 */
public class DefaultVerticalAnimator extends FragmentAnimator {


    public DefaultVerticalAnimator() {
        enter = R.anim.boc_fragment_infromright ;
        exit = R.anim.boc_fragment_outtoright;
        popEnter = R.anim.boc_fragment_infromleft;
        popExit = R.anim.boc_fragment_outtoleft;

    }
}
