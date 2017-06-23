package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui;

import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import java.util.List;

/**
 * 作者：XieDu
 * 创建时间：2016/9/8 15:28
 * 描述：
 */
public class PledgeAccountSelectContract {
    public interface View {

        void onCheckPayeeAccountSuccess(List<String> checkResult);

        void onCheckPayerAccountSuccess(List<String> checkResult);
    }

    public interface Presenter extends BasePresenter {

        void setConversationId(String conversationId);

        void checkPayeeAccount(AccountBean accountBean, String currencyCode);

        void checkPayerAccount(AccountBean accountBean, String currencyCode);
    }
}
