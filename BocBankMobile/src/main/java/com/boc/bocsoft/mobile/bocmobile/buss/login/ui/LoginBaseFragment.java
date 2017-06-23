package com.boc.bocsoft.mobile.bocmobile.buss.login.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.boc.bocsoft.mobile.bii.bus.global.model.CurrentDeviceCheck.CurrentDeviceCheckParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.CurrentDeviceCheck.CurrentDeviceCheckResult;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.GlobalMsgBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.User;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DeviceInfoUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MapUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.SpUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.ErrorDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.buss.common.eventbus.BocEventBus;
import com.boc.bocsoft.mobile.bocmobile.buss.login.model.CustomerCombinInforViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.login.model.LoginViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.login.presenter.LoginBasePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.system.main.eventbus.event.LoginedEvent;
import com.boc.bocsoft.mobile.bocmobile.yun.BocCloudCenter;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;
import java.util.List;

/**
 * Created by feibin on 16/8/16.
 * 登录功能基本fragment
 */
public class LoginBaseFragment extends BussFragment implements LoginBaseContract.View {
    //登录service通信处理类
    protected LoginBasePresenter mLoginBasePresenter;
    //登录UI层model
    protected LoginViewModel mLoginViewModel;
    //用户信息
    protected User mCustomerInfor;
    //合并状态
    protected String combinStatus;
    //注册类型
    protected String regtype;

    /**
     * 登录operationId
     */
    protected String operationId = "";
    /**
     * 本地保存的的手机号
     */
    protected String preferencePhone = "";
    /**
     * 加密后的手机号 3****3
     */
    protected String encryptPhone = "";
    //合并后信息
    protected CustomerCombinInforViewModel customerCombinInfor;

    /**
     * 数据初始化
     */
    @Override
    public void initData() {
        mLoginBasePresenter = new LoginBasePresenter(this);
        mLoginViewModel = new LoginViewModel();
    }


    /**
     * 判断是否需要修改用户名密码
     */

    protected void judgeModifyPassword() {
        // 强制修改密码
        if (ApplicationConst.LOGIN_STATUS_1.equals(mLoginViewModel.getLoginStatus()) ||
                ApplicationConst.LOGIN_STATUS_2.equals(mLoginViewModel.getLoginStatus())) {
            closeProgressDialog();
            start(new ModifyPasswordFragment());
            return;
        }

        // 查询客户信息
        mLoginBasePresenter.queryOprLoginInfo();
    }


    /**
     * 随机数获取成功回调
     *
     * @param random
     * @return
     */
    @Override
    public CurrentDeviceCheckParams randomSuccess(String conversation, String random) {
        CurrentDeviceCheckParams currentDeviceCheckParams = new CurrentDeviceCheckParams();


        String deviceInfo = "";
        String deviceInfo_RC = "";
        try {
            DeviceInfoModel deviceInfoModel = DeviceInfoUtils.getDeviceInfo(getContext(), random, mCustomerInfor.getOperatorId());
            deviceInfo_RC = deviceInfoModel.getDeviceInfo_RC();
            deviceInfo = deviceInfoModel.getDeviceInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        currentDeviceCheckParams.setDeviceInfo(deviceInfo);
        currentDeviceCheckParams.setDeviceInfo_RC(deviceInfo_RC);
        currentDeviceCheckParams.setActiv(ApplicationContext.getInstance().getActive());
        currentDeviceCheckParams.setState("41943040");
        currentDeviceCheckParams.setConversationId(conversation);
        return currentDeviceCheckParams;
    }

    /**
     * 请求登录后获取客户合并后信息成功回调
     */
    @Override
    public void customerCombinInfoSuccess(CustomerCombinInforViewModel customerCombinInforViewModel) {
        customerCombinInfor = customerCombinInforViewModel;
        closeProgressDialog();
        ProtocolFragment protocolFragment = new ProtocolFragment();
        start(protocolFragment);
        return;

    }

    /**
     * 消息推送硬件信息查询成功
     */
    @Override
    public void queryBindingDeviceForPushServiceSuccess() {
        //为消息推送绑定硬件 调用联龙接口 先注释掉
        LoginContext.instance.bindingDeviceForPushService(getActivity());
        toNextStep();
    }
    /**
     * 消息推送硬件信息查询失败
     */
    @Override
    public void queryBindingDeviceForPushServiceFail() {
        toNextStep();
    }

    /**
     * 客户信息请求成功回调
     *
     * @param user
     */
    @Override
    public void oprLoginInfoSuccess(User user) {
        // 客户信息
        mCustomerInfor = user;
        //保存到context
        ApplicationContext.getInstance().setUser(user);
        //设置用户segmentId
        LoginContext.instance.saveSegmentId(mCustomerInfor.getSegmentId());
        //调用联龙接口设置客户信息（包含客户等级）  移动支付需要使用（联龙）
        LoginContext.instance.saveLoginCommonInfo(MapUtils.clzzField2Map(mCustomerInfor));
//        //调用联龙接口，设备信息统计
//        LoginContext.instance.sendFunctionUsedAction(getActivity(),mCustomerInfor.getCifNumber());

        //登录成功检测是否开通音频key(中银E盾)
        String caMerchId = mCustomerInfor.getCaMerchId();
        String certUid = mCustomerInfor.getCertUid();
        String certExpire = mCustomerInfor.getCertExpire();
        if (StringUtils.isEmpty(caMerchId)
                && StringUtils.isEmpty(certUid)
                && StringUtils.isEmpty(certExpire)) {
            //调用联龙接口设置CA未开通标识
            LoginContext.instance.saveEKeyOpenStatus(false);
        } else {
            //调用联龙接口设置CA未开通标
            LoginContext.instance.saveEKeyOpenStatus(true);
        }


        // 查询版用户处理,不需要硬件绑定  1：理财 10：查询 66：VIP
        if ("10".equals(mCustomerInfor.getSegmentId())) {
            //查询版查询版新客户（电子卡签约）
            if ("2".equals(mCustomerInfor.getQryCustType())) {
                //请求借记卡类型的转出账户列表
                mLoginBasePresenter.queryEcardBankAccount();
            } else {
                //请求最新消息(必读消息)的列表
                mLoginBasePresenter.queryGlobalMsgList();
            }
            return;
        }
        //判断是否是否绑定了设备，如果没有，则进入硬件绑定流程，如果绑定了则校验绑定正确性
        String hasBindingDevice = mCustomerInfor.getHasBindingDevice();
        if ("1".equals(hasBindingDevice)) {
            //1:绑定了硬件
            // 本地判断是否绑定了该设备  如果本地判断通过，则请求服务器判断
            String localBindInfo = SpUtils.getLNSpString(mContext, ApplicationConst.SHARED_PREF_LOCAL_BIND_INFO, "");// IMEI
            String cfcaString = DeviceInfoUtils.getLocalCAOperatorId(mContext, mCustomerInfor.getOperatorId(), 1);

            String localBindInfo_mac = SpUtils.getLNSpString(mContext, ApplicationConst.SHARED_PREF_LOCAL_BIND_INFO_MAC, "");// MAC
            String cfcamacString = DeviceInfoUtils.getLocalCAOperatorId(mContext, mCustomerInfor.getOperatorId(), 2);// mac

            if ((!"".equals(localBindInfo)
                    && cfcaString.equals(localBindInfo))
                    || (!"".equals(localBindInfo_mac)
                    && cfcamacString.equals(localBindInfo_mac))) {
                //服务器检查是否本机绑定
                mLoginBasePresenter.queryCurrentDeviceCheck();
            } else {
                showFaultDialog(mContext.getResources().getString(R.string.boc_login_binding_other_devices));
            }

        } else if ("0".equals(hasBindingDevice)) {
            closeProgressDialog();
            //0:没有绑定硬件 进入硬件绑定流程
            BindingDeviceFragment bindingDeviceFragment = new BindingDeviceFragment();
            Bundle bundle = new Bundle();
            try {
                bundle.putParcelable("mCustomerInfor",
                        mCustomerInfor);
            } catch (Exception e) {
                e.printStackTrace();
            }
            bindingDeviceFragment.setArguments(bundle);
            start(bindingDeviceFragment);
        }


    }

    /**
     * 登录接口成功后，后续接口失败处理
     */
    @Override
    public void commonFail() {
        mLoginBasePresenter.queryLogout();
    }

    /**
     * 欢迎页面全局消息列表成功回调
     *
     * @param globalMsg
     */
    @Override
    public void globalMsgSuccess(List<GlobalMsgBean> globalMsg) {
        if (null != globalMsg && globalMsg.size() > 0) {
            LoginMsgDialogView msgDialogView = new LoginMsgDialogView(mContext);
            msgDialogView.setMsgData(globalMsg);
            msgDialogView.setDialogBtnClickListener(new LoginMsgDialogView.DialogBtnClickCallBack() {
                @Override
                public void onConfirmBtnClick(View view) {
                    //获取系统时间
                    mLoginBasePresenter.querySystemDateTime();
                    //获取账户信息
                    mLoginBasePresenter.queryAllChinaBankAccount();
                    //后管平台获取活动信息 activityInfo.action
                    mLoginBasePresenter.queryActivityAction();
                }
            });
            if(null!=ActivityManager.getAppManager().currentActivity()
                    &&(!ActivityManager.getAppManager().currentActivity().isFinishing())){
                msgDialogView.show();
            }
        } else {
            //获取系统时间
            mLoginBasePresenter.querySystemDateTime();
            //获取账户信息
            mLoginBasePresenter.queryAllChinaBankAccount();
            //后管平台获取活动信息
            mLoginBasePresenter.queryActivityAction();
        }

    }

    /**
     * 检查本机是否绑定成功回调
     *
     * @param currentDeviceCheckResult
     */
    @Override
    public void currentDeviceCheckSuccess(CurrentDeviceCheckResult currentDeviceCheckResult) {

        if ("1".equals(currentDeviceCheckResult.getCurrentDeviceFlag())) {

            //请求欢迎页面全局消息列表
            mLoginBasePresenter.queryGlobalMsgList();
        } else {
            //绑定非本机，提示
            showFaultDialog(mContext.getResources().getString(R.string.boc_login_binding_other_devices));
        }
    }

    /**
     * 查询中行所有帐户列表 成功回调
     *
     * @param result
     */
    @Override
    public void allChinaBankAccountSuccess(List<AccountBean> result) {
        //消息推送硬件绑定
        if(ApplicationContext.getInstance().hasLianLong()){
            mLoginBasePresenter.queryBindingDeviceForPushService();
        }else{
            toNextStep();
        }
    }

    /**
     * 查询版用户电子卡签约
     * 借记卡类型账户请求成功回调
     *
     * @param result
     */
    @Override
    public void ecardBankAccountSuccess(List<AccountBean> result) {
        //如果含有借记卡调用联龙接口，设置标识
        if (null != result && result.size() > 0) {
            for (AccountBean account : result) {
                //String eCard = account.getEcard();
                String accountCatalog = account.getAccountCatalog();
                String accountType = account.getAccountType();
                //if ("1".equals(eCard) && "119".equals(accountType)) {
                //    //调用联龙接口  设置电子卡标识
                //    LoginContext.instance.saveECardFlag(
                //            MapUtils.clzzField2Map(account),true);
                //}
                if ((("2".equals(accountCatalog))||("3".equals(accountCatalog)))
                    && "119".equals(accountType)) {
                    //调用联龙接口  设置电子卡标识
                    LoginContext.instance.saveECardFlag(
                        MapUtils.clzzField2Map(account),true);
                }
            }
        }
        //请求欢迎页面全局消息列表
        mLoginBasePresenter.queryGlobalMsgList();
    }

    /**
     * 登录成功后处理
     */
    private void toNextStep() {
        closeProgressDialog();
        //保存登录成功的用户名
        SpUtils.saveLNPhoneString(mContext, "loginName", ApplicationContext.getInstance().getUser().
            getLoginName());

        //保存登录成功的证件类型
        SpUtils.saveString(mContext, SpUtils.SPKeys.KEY_LOGINIDENTITYTYPE, ApplicationContext.getInstance().getUser().
                getIdentityType());
        //保存登录成功的证件号码
        SpUtils.saveString(mContext, SpUtils.SPKeys.KEY_LOGINIDENTITYNUMBER, ApplicationContext.getInstance().getUser().
                getIdentityNumber());

        //保存cookie到联龙 联调时打开
        LoginContext.instance.saveCookiesToLianLong();
        //调用联龙接口，设备信息统计
        LoginContext.instance.sendFunctionUsedAction(getActivity(),ApplicationContext.getInstance().getUser().getCifNumber());
        //更改登录状态 登录成功
        ApplicationContext.getInstance().setLogin(true);
        ApplicationContext.getInstance().getUser().setLogin(true);
        //为联龙提供
        getActivity().setResult(Activity.RESULT_OK);
        ActivityManager.getAppManager().finishActivity();

        mLoginBasePresenter.unsubscribe();
        if(null!=LoginContext.instance.getCallback()){
            LoginContext.instance.getCallback().success();
        }
        LoginContext.instance.setCallback(null);

        User user = ApplicationContext.getInstance().getUser();

        if(user != null){
            //登录后初始化云备份 , 使用登录名作为手机号
            BocCloudCenter.getInstance().init(user.getCifNumber(),user.getCustomerId(),user.getLoginName());
            //登陆后记录登录信息
            BocCloudCenter.getInstance().updateUserLoginInfo(false);
            BocCloudCenter.getInstance().checkUpload();
        }

        // 2016-11-01 zhx 添加(将常用联系人列表置为null) start
        ApplicationContext applicationContext = (ApplicationContext) mActivity.getApplicationContext();
        applicationContext.setPayeeEntityList(null);
        BocEventBus.getInstance().post(new LoginedEvent());
        // 2016-11-01 zhx 添加(将常用联系人列表置为null) end
    }


    /**
     * 退出成功回调
     */
    @Override
    public void logoutSuccess() {
        ApplicationContext.getInstance().logout();

    }
    
    private ErrorDialog errorDialog;
    /**
     * 报错弹出框
     */
    private void showFaultDialog(String errorMessage){
    	      closeProgressDialog();
    	      errorDialog = new ErrorDialog(mContext);
            errorDialog.setBtnText("确认");
            errorDialog.setCancelable(false);
            errorDialog.setErrorData(errorMessage);
            if (!errorDialog.isShowing()) {
                errorDialog.show();
            }

            errorDialog.setOnBottomViewClickListener(new ErrorDialog.OnBottomViewClickListener() {
                @Override
                public void onBottomViewClick() {
                 //点击确定按钮   调用退出接口
                    if(null!=mLoginBasePresenter){
                        mLoginBasePresenter.queryLogout();
                    }
                }
            });
    	
    	}

    @Override
    public boolean onBackPress() {
        if(null!=errorDialog&&errorDialog.isShowing()){
        	return true;
        }
        return false;
    }

}
