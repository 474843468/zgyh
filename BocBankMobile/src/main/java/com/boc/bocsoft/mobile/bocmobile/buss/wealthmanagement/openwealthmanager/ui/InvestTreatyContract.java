package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.openwealthmanager.ui;

/**
 * 开通投资理财公共
 * Created by liuweidong on 2016/12/4.
 */

public class InvestTreatyContract {
    public interface Presenter {
        void queryOpenStatus(OpenStatusI openStatusI, boolean[] needs);
    }
}
