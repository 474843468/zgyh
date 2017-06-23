package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.ui;

import com.boc.bocsoft.mobile.bocmobile.buss.MenuFragment;

/**
 * 账户管理更多界面
 */
public class AccountManagementMenuFragment extends MenuFragment {
    private boolean isReInit = false;

    @Override
    protected final String getTitleValue() {
        return "管理";
    }

    @Override
    public void reInit() {
        isReInit = true;
        super.reInit();
    }

    @Override
    protected void loadData() {
        // todo 待添加逻辑
    }

    @Override
    public boolean onBackPress() {
        if (isReInit) {
            popToAndReInit(AccountManagementFragment.class);
        }
        return false;
    }

    @Override
    protected void titleLeftIconClick() {
        if (isReInit) {
            popToAndReInit(AccountManagementFragment.class);
        }
    }
}
