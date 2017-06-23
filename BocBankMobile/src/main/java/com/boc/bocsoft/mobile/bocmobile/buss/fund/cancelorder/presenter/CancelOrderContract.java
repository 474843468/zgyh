package com.boc.bocsoft.mobile.bocmobile.buss.fund.cancelorder.presenter;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.cancelorder.model.PsnFundQueryTransOntranModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.cancelorder.model.PsnFundStatusDdApplyQueryModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by taoyongzhen on 2016/11/21.
 */

public class CancelOrderContract {
    public interface PsnFundQueryTransOntranView extends BaseView<BasePresenter> {
        /**
         * 基金在途交易查询成功
         * @param model
         */
        void psnFundQueryTransOntranSuccess(PsnFundQueryTransOntranModel model);

        /**
         * 基金在途交易查询失败
         * @param error
         */
        void psnFundQueryTransOntranFail(BiiResultErrorException error);

    }

    public interface PsnFundStatusDdApplyQueryView extends BaseView<BasePresenter> {
        /**
         *有效定投查询成功
         * @param model
         */
        void psnFundStatusDdApplyQuerySuccess(PsnFundStatusDdApplyQueryModel model);

        /**
         * 有效定投查询失败
         * @param error
         */
        void psnFundStatusDdApplyQueryFail(BiiResultErrorException error);
    }


    public interface PsnFundQueryTransOntranPresenter extends BasePresenter{
        /**
         * 基金在途交易查询
         * @param model
         */
        void psnFundQueryTransOntran(PsnFundQueryTransOntranModel model);
    }

    public interface PsnFundStatusDdApplyQueryPresenter extends BasePresenter {
        /**
         *有效定投查询
         * @param model
         */
        void psnFundStatusDdApplyQuery(PsnFundStatusDdApplyQueryModel model);
    }

}
