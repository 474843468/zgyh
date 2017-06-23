package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.openwealthmanager.ui;

import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.ErrorDialog;

/**
 * 开通投资理财回调
 * Created by liuweidong on 2016/11/16.
 */
public interface OpenStatusI {
    public void openSuccess();

    public void openFail(ErrorDialog dialog);
}
