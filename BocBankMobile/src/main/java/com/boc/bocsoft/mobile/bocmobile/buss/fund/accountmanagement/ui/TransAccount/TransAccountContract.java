package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui.TransAccount;

import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnCancelFundAccount.PsnCancelFundAccountResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model.ChangeCardModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model.TaAccountModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

public class TransAccountContract {

    public interface View {

        //获取会话ID，成功调用
        void obtainConversationSuccess(String conversationId);
        //获取会话ID，失败调用

        void obtainConversationFail(BiiResultErrorException biiResultErrorException);

        void queryCardListSuccess(ChangeCardModel result);

        void queryCardListFail(BiiResultErrorException biiResultErrorException);

        void queryTaAccListSuccess(TaAccountModel result);

        void queryTaAccListFail(BiiResultErrorException biiResultErrorException);

        void transAccCancelSubmitSuccess(PsnCancelFundAccountResult result);

        void transAccCancelSubmitFail(BiiResultErrorException biiResultErrorException);

    }

    public interface Presenter extends BasePresenter {
        /**
         * 变更资金账户列表请求
         */
        void queryCardList();

        /**
         * TA账户列表查询请求
         */
        void queryTaAccList();                                                        // 上送参数为空（view-Model封装的params）

        /**
         * 基金交易账户销户请求
         */
        void transAccCancelSubmit();                                             // 上送参数为空（view-Model封装的params）

    }

}