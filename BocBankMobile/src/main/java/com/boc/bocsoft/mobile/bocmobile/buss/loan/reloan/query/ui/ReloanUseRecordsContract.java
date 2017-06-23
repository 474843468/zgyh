
package com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.query.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.query.model.ReloanUseRecordsViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by liuzc on 2016/8/20.
 */
public class ReloanUseRecordsContract {

    public interface View extends BaseView<Presenter> {

        /**
         * 查询用款记录成功回调
         */
        void queryUseRecordsSuccess(ReloanUseRecordsViewModel viewModel);

        /**
         * 查询用款记录失败回调
         */
        void queryUseRecordsFail(BiiResultErrorException biiResultErrorException);
    }


    public interface Presenter extends BasePresenter {
        /**
         * 查询用款记录
         */
        void queryUseRecords(ReloanUseRecordsViewModel viewModel);

    }

}
