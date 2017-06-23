package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.QueryPayerListViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * 查询付款人列表
 * Created by zhx on 2016/6/30.
 */
public class QueryPayerListContract {
    public interface View extends BaseView<Presenter> {
        /**
         * 成功回调:查询付款人列表
         */
        void queryPayerListSuccess(QueryPayerListViewModel queryPayerListViewModel);

        /**
         * 失败回调:查询付款人列表
         */
        void queryPayerListFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 查询付款人列表
         */
        void queryPayerList(QueryPayerListViewModel queryPayerListViewModel);
    }
}
