package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui.TaAccount;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model.TaAccountModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * 基金-账户管理-TaAccount 相关接口
 * Created by lyf7084 on 2016/11/30.
 */

public class TaAccountContract {

    //    FundTaAccount-TA子页面Contract
    public interface View {

        void queryTaAccListSuccess(TaAccountModel result);    // 返回报文为空（view-Model封装的result）

        void queryTaAccListFail(BiiResultErrorException biiResultErrorException);

    }

    public interface Presenter extends BasePresenter {
        /**
         * TA账户列表查询请求
         */
        void queryTaAccList();    // 上送参数为空（view-Model封装的params）

    }

}