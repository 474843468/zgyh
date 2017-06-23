package com.boc.bocsoft.mobile.bocmobile.buss.login.ui;

import com.boc.bocsoft.mobile.bii.bus.global.model.CurrentDeviceCheck.CurrentDeviceCheckParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.CurrentDeviceCheck.CurrentDeviceCheckResult;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.GlobalMsgBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.User;
import com.boc.bocsoft.mobile.bocmobile.buss.login.model.CustomerCombinInforViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

import java.util.List;

/**
 * Created by feibin on 2016/5/21.
 * 登录功能公用接口类
 */
public class LoginBaseContract {

    public interface View  {
        /**查询随机数成功回调*/
        CurrentDeviceCheckParams randomSuccess(String conversation, String random);
        /**查询操作员信息成功回调*/
        void oprLoginInfoSuccess(User user);
        void commonFail();
        /**查询欢迎页面全局消息列表成功回调*/
        void globalMsgSuccess(List<GlobalMsgBean> globalMsg);
        /**检查本机是否绑定成功回调*/
        void currentDeviceCheckSuccess(CurrentDeviceCheckResult currentDeviceCheckResult);
        /**查询中行所有帐户列表成功回调*/
        void allChinaBankAccountSuccess(List<AccountBean> result);
        /**查询版电子卡签约用户的借记卡帐户列表成功回调*/
        void ecardBankAccountSuccess(List<AccountBean> result);
        /**退出成功回调*/
        void logoutSuccess();
        /**登录后获取客户合并后信息成功回调*/
        void customerCombinInfoSuccess(CustomerCombinInforViewModel customerCombinInforViewModel);
        /**消息推送绑定设备成功回调 */
        void queryBindingDeviceForPushServiceSuccess();
        /**消息推送绑定设备失败回调 */
        void queryBindingDeviceForPushServiceFail();
    }

    public interface Presenter extends BasePresenter {
        /**查询操作员信息*/
        void queryOprLoginInfo();
        /**查询欢迎页面全局消息列表*/
        void queryGlobalMsgList();
        /**检查本机是否绑定*/
        void queryCurrentDeviceCheck();
        /**查询中行所有帐户列表*/
        void queryAllChinaBankAccount();
        /**查询版电子卡签约用户的借记卡帐户列表*/
        void queryEcardBankAccount();
        /**获取服务器时间*/
        void querySystemDateTime();
        /**退出*/
        void queryLogout();
        /**登录后获取客户合并后信息*/
        void queryPsnCustomerCombinInfo();
        /**消息推送绑定设备 */
        void queryBindingDeviceForPushService();
        /**获取活动信息 */
        void queryActivityAction();
    }
}
