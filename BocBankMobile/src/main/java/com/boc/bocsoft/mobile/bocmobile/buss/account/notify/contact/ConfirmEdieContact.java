package com.boc.bocsoft.mobile.bocmobile.buss.account.notify.contact;

import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.model.ConfirmEditModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by wangtong on 2016/6/25.
 */
public class ConfirmEdieContact {
    public interface View extends BaseView<Presenter> {
        //短信开通成功
        void psnSsmSignReturned();

        //短信开通预交易成功
        void psnSsmSignPreReturned();

        void smsMadeReturned();

        void smsMadePreReturned();

        //短信详情修改
        void smsMessageChangeReturned();

        //获取UImodel
        ConfirmEditModel getUiModel();

        //短信详情修改预交易
        void smsMessageChangePreReturned();

        //安全因子返回
        void securityFactorReturned();
    }

    public interface Presenter extends BasePresenter {

        //短信通知开通预交易
        void psnSsmSignPre();

        //开通短信
        void psnSsmSign();

        //获取安全因子
        void psnGetSecurityFactor();

        //短信通知修改预交易
        void psnSsmMessageChangePre();

        //短信通知信息修改
        void psnSsmMessageChange();

        public void psnSsmMade();

        public void psnSsmMadePre();
    }
}
