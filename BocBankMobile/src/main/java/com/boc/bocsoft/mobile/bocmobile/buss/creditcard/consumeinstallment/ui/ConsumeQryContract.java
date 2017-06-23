package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.model.ConsumeTransModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.model.CrcdConsumeQueryModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * 作者：lq7090
 * 创建时间：2016/12/27.
 * 用途：
 */
public class ConsumeQryContract {

    /**
     *消费分期查询页面所实现接口
     */

    public interface BaseView<Presenter> {

        /**
         * 消费账单查询
         *
         */
        void crcdDividedPayConsumeQrySuccess(ConsumeTransModel result);
        void crcdDividedPayConsumeQryFailed(BiiResultErrorException exception);

    }

    /**
     *消费分期Presenter实现此接口
     */
    public interface Presenter extends BasePresenter {

        /**
         * 消费分期提交
         */
        void crcdDividedPayConsumeQry(CrcdConsumeQueryModel queryModel);


    }
}
