package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.myinstallment.model;


import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.LoadMoreListHelper;

/**
 * Created by yangle on 2016/11/21.
 */
public class InstallmentHistoryViewModel extends LoadMoreListHelper.ViewModel<InstallmentRecordModel> {
    private static final int DEFAULT_START_INDEX = 0;
    private static final String REFRESH_TRUE = "true";
    private static final String REFRESH_FALSE = "false";
    private static final int DEFAULT_PAGE_SIZE = 50;

    private String accountId;
    private String _refresh = REFRESH_TRUE; // 刷新标识:true 重新查询 false：从缓存中查询

    public InstallmentHistoryViewModel(String accountId) {
        this.accountId = accountId;
        super.setPageSize(DEFAULT_PAGE_SIZE);
    }

    public InstallmentHistoryViewModel() {
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String get_refresh() {
        return _refresh;
    }

    public String getCurrentIndexStr() {
        return String.valueOf(super.getCurrentIndex());
    }

    public String getPageSizeStr() {
        return String.valueOf(super.getPageSize());
    }

    public boolean isEmpty() {
        return super.getList().isEmpty();
    }

    public boolean isRefreshFlagTrue() {
        return REFRESH_TRUE.equals(_refresh);
    }

    public void setRefreshFlag(boolean isRefresh) {
        _refresh = isRefresh ?  REFRESH_TRUE : REFRESH_FALSE;
    }

    public void reset() {
        getList().clear();
        _refresh = REFRESH_TRUE;
        setCurrentIndex(DEFAULT_START_INDEX);
    }
}
