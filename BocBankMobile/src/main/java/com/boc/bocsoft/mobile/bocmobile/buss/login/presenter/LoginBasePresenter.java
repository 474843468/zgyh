package com.boc.bocsoft.mobile.bocmobile.buss.login.presenter;


import android.util.Log;

import com.boc.bocsoft.mobile.bii.bus.global.model.CurrentDeviceCheck.CurrentDeviceCheckParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.CurrentDeviceCheck.CurrentDeviceCheckResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetRandom.PSNGetRandomParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQueryAllChinaBankAccount.PsnCommonQueryAllChinaBankAccountParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQueryAllChinaBankAccount.PsnCommonQueryAllChinaBankAccountResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQuerySystemDateTime.PsnCommonQuerySystemDateTimeParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQuerySystemDateTime.PsnCommonQuerySystemDateTimeResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCustomerCombinInfo.PsnCustomerCombinInfoParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCustomerCombinInfo.PsnCustomerCombinInfoResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnMobileIsSignedAgent.PsnMobileIsSignedAgentParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnMobileIsSignedAgent.PsnMobileIsSignedAgentResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.login.model.Logout.LogoutParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.Logout.LogoutResult;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnCommonQueryOprLoginInfo.PsnCommonQueryOprLoginInfoParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnCommonQueryOprLoginInfo.PsnCommonQueryOprLoginInfoResult;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSetMobileInfo.PsnSetMobileInfoParams;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnWMSQueryWealthRank.PsnWMSQueryWealthRankParams;
import com.boc.bocsoft.mobile.bii.bus.login.service.PsnLoginService;
import com.boc.bocsoft.mobile.bii.bus.setting.model.psnSvrGlobalMsgList.PsnSvrGlobalMsgListParams;
import com.boc.bocsoft.mobile.bii.bus.setting.model.psnSvrGlobalMsgList.PsnSvrGlobalMsgListResult;
import com.boc.bocsoft.mobile.bii.bus.setting.service.PsnSettingService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.GlobalMsgBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.User;
import com.boc.bocsoft.mobile.bocmobile.base.utils.AccountUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DeviceInfoUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.login.model.CustomerCombinInforViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.login.ui.LoginBaseContract;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;
import com.boc.bocsoft.mobile.mbcg.MBCGHeader;
import com.boc.bocsoft.mobile.mbcg.MBCGService;
import com.boc.bocsoft.mobile.mbcg.activityinfo.ActivityInfoParams;
import com.boc.bocsoft.mobile.mbcg.activityinfo.ActivityInfoResult;
import com.chinamworld.boc.commonlib.BaseCommonTools;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by feibin on 2016/5/17.
 * 登录公共BII通信逻辑处理
 */
public class LoginBasePresenter extends RxPresenter implements LoginBaseContract.Presenter {

    private LoginBaseContract.View mLoginBaseView;
    /**
     * 登录service
     */
    private PsnLoginService psnLoginService;
    /**
     * 个人设定service
     */
    private PsnSettingService psnSettingService;
    /**
     * 公共service
     */
    private GlobalService globalService;
    /**
     * MBCG service
     */
    private MBCGService mbcgService;
    /**
     * 会话
     */
    private String conversation;
    /**
     * 随机数
     */
    private String random;
    /**
     * 客户等级
     */
    private String custLevel;

    public LoginBasePresenter(LoginBaseContract.View loginView) {
        mLoginBaseView = loginView;
        psnLoginService = new PsnLoginService();
        psnSettingService = new PsnSettingService();
        globalService = new GlobalService();
        mbcgService = new MBCGService();
    }


    /**
     * 获取客户等级及信息
     */
    @Override
    public void queryOprLoginInfo() {
        //查询客户等级 04 中银理财客户 05 特殊理财客户 06 财富管理客户 07 特殊财富客户 08 私人银行客户
        PsnWMSQueryWealthRankParams wealthRankParams = new PsnWMSQueryWealthRankParams();
        psnLoginService.psnWMSQueryWealthRank(wealthRankParams)
                .compose(this.<String>bindToLifecycle())
                //上个接口调用失败，则继续执行下个接口
                .onErrorResumeNext(Observable.<String>just(null))
                //查询操作员信息
                .flatMap(new Func1<String, Observable<PsnCommonQueryOprLoginInfoResult>>() {
                    @Override
                    public Observable<PsnCommonQueryOprLoginInfoResult> call(String wealthRank) {
                        // 记录客户等级
                        custLevel = wealthRank;
                        PsnCommonQueryOprLoginInfoParams  oprLoginInfoParams = new PsnCommonQueryOprLoginInfoParams();
                        return psnLoginService.psnCommonQueryOprLoginInfo(oprLoginInfoParams);
                    }
                })
                .compose(SchedulersCompat.<PsnCommonQueryOprLoginInfoResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCommonQueryOprLoginInfoResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                        mLoginBaseView.commonFail();
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnCommonQueryOprLoginInfoResult oprLoginInfoResult) {
                        //查询操作员信息成功，设置用户登录信息
                        User user = new User();
                        user.setCustLevel(custLevel);
                        BeanConvertor.toBean(oprLoginInfoResult,user);
                        mLoginBaseView.oprLoginInfoSuccess(user);
                    }
                });
    }

    /**
     * 欢迎页面全局消息列表
     */
    @Override
    public void queryGlobalMsgList() {
       PsnSvrGlobalMsgListParams params = new PsnSvrGlobalMsgListParams();
        psnSettingService.psnSvrGlobalMsgList(params)
                .compose(this.<List<PsnSvrGlobalMsgListResult>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnSvrGlobalMsgListResult>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnSvrGlobalMsgListResult>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                        mLoginBaseView.commonFail();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(List<PsnSvrGlobalMsgListResult> psnSvrGlobalMsgListResults) {
                        List<GlobalMsgBean>globalMsg = new ArrayList<GlobalMsgBean>();
                        for(PsnSvrGlobalMsgListResult item:psnSvrGlobalMsgListResults){
                            String popupFlag = item.getPopupFlag();
                            if (popupFlag==null||popupFlag.equals("0")) {
                                continue;
                            }
                            GlobalMsgBean msg = new GlobalMsgBean();
                            BeanConvertor.toBean(item,msg);
                            globalMsg.add(msg);
                        }

                        mLoginBaseView.globalMsgSuccess(globalMsg);
                    }
                });

    }

    /**
     * 检查本机是否绑定
     */
    @Override
    public void queryCurrentDeviceCheck() {

        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversationResult) {
                        conversation = conversationResult;
                        PSNGetRandomParams psnGetRandomParams = new PSNGetRandomParams();
                        psnGetRandomParams.setConversationId(conversation);
                        return globalService.psnGetRandom(psnGetRandomParams);
                    }
                })
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .map(new Func1<String, CurrentDeviceCheckParams>() {
                    @Override
                    public CurrentDeviceCheckParams call(String randomReu) {
                        random = randomReu;
                        return mLoginBaseView.randomSuccess(conversation,random);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<CurrentDeviceCheckParams, Observable<CurrentDeviceCheckResult>>() {
                    @Override
                    public Observable<CurrentDeviceCheckResult> call(CurrentDeviceCheckParams currentDeviceCheckParams) {
                        return globalService.currentDeviceCheck(currentDeviceCheckParams);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BIIBaseSubscriber<CurrentDeviceCheckResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                        mLoginBaseView.commonFail();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(CurrentDeviceCheckResult currentDeviceCheckResult) {

                        mLoginBaseView.currentDeviceCheckSuccess(currentDeviceCheckResult);
                    }
                });
    }

    /**
     * 查询中行所有帐户列表 + 代理点签约判断
     */
    @Override
    public void queryAllChinaBankAccount() {

      globalService.psnMobileIsSignedAgent(new PsnMobileIsSignedAgentParams())
          .compose(this.<PsnMobileIsSignedAgentResult>bindToLifecycle())
          .onErrorReturn(new Func1<Throwable, PsnMobileIsSignedAgentResult>() {
            @Override public PsnMobileIsSignedAgentResult call(Throwable throwable) {
              return null;
            }
          })
          .flatMap(new Func1<PsnMobileIsSignedAgentResult, Observable<List<PsnCommonQueryAllChinaBankAccountResult>>>() {
            @Override
            public Observable<List<PsnCommonQueryAllChinaBankAccountResult>> call(PsnMobileIsSignedAgentResult result) {
              User user = ApplicationContext.getInstance().getUser();
              if(user != null){
                if(result == null){
                  user.setMobileIsSignedAgent(false);
                }else{
                  user.setMobileIsSignedAgent("true".equalsIgnoreCase(result.getFlag()));
                }
              }

              PsnCommonQueryAllChinaBankAccountParams mParams = new PsnCommonQueryAllChinaBankAccountParams();
              mParams.setAccountType(null);

              return globalService.psnCommonQueryAllChinaBankAccount(mParams);
            }
          }).compose(this.<List<PsnCommonQueryAllChinaBankAccountResult>>bindToLifecycle())
          .compose(SchedulersCompat.<List<PsnCommonQueryAllChinaBankAccountResult>>applyIoSchedulers())
          .subscribe(new BIIBaseSubscriber<List<PsnCommonQueryAllChinaBankAccountResult>>() {
            @Override
            public void handleException(BiiResultErrorException biiResultErrorException) {

              mLoginBaseView.commonFail();
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(List<PsnCommonQueryAllChinaBankAccountResult> result) {
              LogUtils.d("feib","-----------PsnCommonQueryAllChinaBankAccountResult---------"+result);
              ApplicationContext.getInstance().setChinaBankAccountList(AccountUtils.convertBIIAccount2ViewModel(result));
              mLoginBaseView.allChinaBankAccountSuccess(AccountUtils.convertBIIAccount2ViewModel(result));
            }
          });

    }
    /**
     * 查询中行所有帐户列表
     */
    @Override
    public void queryEcardBankAccount() {
        PsnCommonQueryAllChinaBankAccountParams mParams = new PsnCommonQueryAllChinaBankAccountParams();
        List<String> accountType = new ArrayList<>();
        //借记卡类型
        accountType.add("119");
        mParams.setAccountType(accountType);
        globalService.psnCommonQueryAllChinaBankAccount(mParams)
                .compose(this.<List<PsnCommonQueryAllChinaBankAccountResult>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnCommonQueryAllChinaBankAccountResult>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnCommonQueryAllChinaBankAccountResult>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                        mLoginBaseView.commonFail();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(List<PsnCommonQueryAllChinaBankAccountResult> result) {
                        Log.i("feib","-----------借记卡类型账户请求结果---------"+result);
                        mLoginBaseView.ecardBankAccountSuccess(AccountUtils.convertBIIAccount2ViewModel(result));
                    }
                });

    }

    /**
     * 获取服务器时间
     */
    @Override
    public void querySystemDateTime() {
        PsnCommonQuerySystemDateTimeParams mParams = new PsnCommonQuerySystemDateTimeParams();
        globalService.psnCommonQuerySystemDateTime(mParams)
                .compose(this.<PsnCommonQuerySystemDateTimeResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCommonQuerySystemDateTimeResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCommonQuerySystemDateTimeResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                        mLoginBaseView.commonFail();
                    }
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onNext(PsnCommonQuerySystemDateTimeResult systemDateTime) {
                        //设置服务器时间
                        ApplicationContext.systemDataTime= systemDateTime.getDateTme();
                        //设置当前设备时间
                        ApplicationContext.deviceDataTime = LocalDateTime.now();
                    }
                });
    }

    /**
     * 退出
     */
    @Override
    public void queryLogout() {
        LogoutParams logoutParams = new LogoutParams();
        psnLoginService.logout(logoutParams)
                .compose(this.<LogoutResult>bindToLifecycle())
                .compose(SchedulersCompat.<LogoutResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<LogoutResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mLoginBaseView.logoutSuccess();
                    }
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onNext(LogoutResult logoutResult) {
                        mLoginBaseView.logoutSuccess();
                    }
                });
    }

    /**登录后获取客户合并后信息*/
    @Override
    public void queryPsnCustomerCombinInfo() {
        PsnCustomerCombinInfoParams params = new PsnCustomerCombinInfoParams();
        globalService.psnCustomerCombinInfo(params)
                .compose(this.<PsnCustomerCombinInfoResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCustomerCombinInfoResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCustomerCombinInfoResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                        mLoginBaseView.commonFail();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCustomerCombinInfoResult psnCustomerCombinInfoResult) {
                        CustomerCombinInforViewModel customerCombinInforViewModel = new CustomerCombinInforViewModel();
                        BeanConvertor.toBean(psnCustomerCombinInfoResult,customerCombinInforViewModel);
                        mLoginBaseView.customerCombinInfoSuccess(customerCombinInforViewModel);
                    }
                });
    }

    /**获取转账活动信息*/
    @Override
    public void queryActivityAction() {
        ActivityInfoParams params = new ActivityInfoParams();
        params.setProductName("BOCMBC_V01.1A");
        MBCGHeader header = new MBCGHeader();
        header.setProductName("BOCMBC_V01.1A");
        header.setCustNo("");
        header.setFirstUse("");
        header.setModel(android.os.Build.MODEL);
        header.setOsVersion(android.os.Build.VERSION.RELEASE);
        header.setRAM("");
        header.setROM("");
        header.setResolution("");
        header.setUsedROM(DeviceInfoUtils.getAvailableInternalMemorySize());
        header.setVersionNo(ApplicationContext.getInstance().getVersion());

        mbcgService.getActivityAction(params,header)
                .compose(this.<ActivityInfoResult>bindToLifecycle())
                .compose(SchedulersCompat.<ActivityInfoResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<ActivityInfoResult>() {
                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(ActivityInfoResult activityInfoResult) {
                        //缓存活动信息
                        ApplicationContext.activityInfoList = activityInfoResult.getActivityList();
                    }
                });
    }


    /**
     * 硬件绑定消息推送
     */
    @Override
    public void queryBindingDeviceForPushService() {
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(psnCreatConversationParams)
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String conversationResult) {
                        conversation = conversationResult;
                        PSNGetTokenIdParams psnGetTokenIdParamsParams = new PSNGetTokenIdParams();
                        psnGetTokenIdParamsParams.setConversationId(conversation);
                        return globalService.psnGetTokenId(psnGetTokenIdParamsParams);
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String token) {
                        PsnSetMobileInfoParams psnSetMobileInfoParams = new PsnSetMobileInfoParams();
                        psnSetMobileInfoParams.setConversationId(conversation);
                        psnSetMobileInfoParams.setToken(token);
                        psnSetMobileInfoParams.setBindFlag("A");
                        psnSetMobileInfoParams.setDeviceInfo(BaseCommonTools.getInstance().getdeviceInfo());
                        psnSetMobileInfoParams.setDevicestyle("03");
                        psnSetMobileInfoParams.setPushAddress(BaseCommonTools.getInstance().getpushAddress());
                        psnSetMobileInfoParams.setDevicesubstyle("01");
                        return globalService.psnSetMobileInfo(psnSetMobileInfoParams);
                    }
                })

                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<String>() {

                  @Override
                  public void commonHandleException(BiiResultErrorException biiResultErrorException) {

                  }

                  @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
//                        commonHandleException(biiResultErrorException);
                      mLoginBaseView.queryBindingDeviceForPushServiceFail();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(String s) {
                        mLoginBaseView.queryBindingDeviceForPushServiceSuccess();
                    }
                });
    }

}
