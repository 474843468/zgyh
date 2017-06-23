package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundstatement.ui;

import android.widget.BaseAdapter;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundstatement.model.PersionaltrsModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundstatement.model.StatementModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.List;

/**
 * Created by huixiaobo on 2016/11/22.
 * 网络请求回调
 */
public class StatementContract {
    /**基金对账单*/
    public interface StatementView extends BaseView<Presenter> {
        /**持仓信息查询失败*/
       void fundStatementFail(BiiResultErrorException biiResultErrorException);
        /**持仓信息查询成功*/
       void fundStatementSuccess(StatementModel statementList);
        /**交易流水查询失败*/
        void fundPersionalTransFail(BiiResultErrorException biiResultErrorException);
        /**交易流水查询成功*/
        void fundPersionalTransSuccess(List<PersionaltrsModel> persionalList);
    }

    /**交易流水*/
    public interface PersionalTransView extends BaseView<Presenter> {
        /**交易流水查询失败*/
        void fundPersionalTransFail(BiiResultErrorException biiResultErrorException);
        /**交易流水查询成功*/
        void fundPersionalTransSuccess(List<PersionaltrsModel> persionalList);
    }

    public interface Presenter extends BasePresenter {
        /**持仓列表*/
        void queryFundStatement(String fundStatementTime);
        /**交易流水列表*/
        void queryPersionalTrans(String startDate, String endDate);
    }
}
