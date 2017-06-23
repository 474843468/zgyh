package com.boc.bocsoft.mobile.bocmobile.base.activity.anim;

import com.boc.bocsoft.mobile.bocmobile.R;
/**
 * Created by YoKeyword on 16/2/5.
 */
public class DefaultHorizontalAnimator extends FragmentAnimator {

    public DefaultHorizontalAnimator() {
        enter = R.anim.boc_fragment_infromleft;
        exit = R.anim.boc_fragment_outtoright;
        popEnter = R.anim.boc_fragment_infromright;
        popExit = R.anim.boc_fragment_outtoleft;
    }
}
