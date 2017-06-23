package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.ui;

import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGTradeDetailQuery.PsnVFGTradeDetailQueryResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.model.XpadPsnVFGCancelOrderModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.model.XpadVFGTradeDetailQueryModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * 双向宝交易查询--有效委托查询详情界面
 * Created by zc on 2016/11/17
 */
public class EffectivEntrDetailContract {

    public interface View extends BaseView<Presenter>{
        /**
         * 成功回调：
         * 有效委托详情查询
         */
        void psnXpadEffectiveDetailQuerySuccess(PsnVFGTradeDetailQueryResult psnVFGTradeDetailQueryResult);

        /**
         * 失败回调：
         * 有效委托详情查询
         */
        void psnXpadEffectiveDetailQueryFail(BiiResultErrorException biiResultErrorException);
        /**
         * 成功回调：
         * 撤单
         */
        void psnXpadCancelOrderSuccess(XpadPsnVFGCancelOrderModel xpadPsnVFGCancelOrderModel);

        /**
         * 失败回调：
         * 撤单
         */
        void psnXpadCancelOrderFail(BiiResultErrorException biiResultErrorException);

    }

    public interface Presenter extends BasePresenter {
        /**
         * 接口请求
         * 有效委托详情查询
         */
        void psnXpadEffectiveDetailQuery(XpadVFGTradeDetailQueryModel model);
        /**
         * 接口请求
         * 撤单
         */
        void psnXpadCancelOrder(XpadPsnVFGCancelOrderModel model);

    }

}
