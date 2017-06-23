package com.boc.bocsoft.mobile.bocmobile.buss.account.apply.presenter;


import com.boc.bocsoft.mobile.bii.bus.account.model.PsnApplyTermDepositeConfirm.PsnApplyTermDepositeConfirmParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnApplyTermDepositeConfirm.PsnApplyTermDepositeConfirmResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnApplyTermDepositeResult.PsnApplyTermDepositeParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnApplyTermDepositeResult.PsnApplyTermDepositeResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCountryCodeQueryApplyPre.PsnCountryCodeQueryApplyPreParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCountryCodeQueryApplyPre.PsnCountryCodeQueryApplyPreResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.apply.model.ApplyAccountModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseTransactionPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;

import rx.Observable;
import rx.functions.Func1;


/**
 * 申请账户BII通信逻辑处理
 * Created by liuyang on 2016/6/8.
 */
public class ApplyPresenter extends BaseTransactionPresenter implements ApplyContract.Presenter {

    //申请界面逻辑处理
    private ApplyContract.QueryAccountView queryAccountView;

    //申请交易界面逻辑处理
    private ApplyContract.ApplyAccountView applyAccountView;

    //申请账户Service
    private AccountService accountService;

    public ApplyPresenter(ApplyContract.QueryAccountView view) {
        super(view);
        queryAccountView = view;
        accountService = new AccountService();
    }

    public ApplyPresenter(ApplyContract.ApplyAccountView view) {
        super(view);
        applyAccountView = view;
        accountService = new AccountService();
    }

    /**
     * 查询个人客户国籍信息
     */
    public void queryCountryCode(String accountId) {
        accountService.psnCountryCodeQuery(new PsnCountryCodeQueryApplyPreParams(accountId))
                .compose(this.<PsnCountryCodeQueryApplyPreResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCountryCodeQueryApplyPreResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnCountryCodeQueryApplyPreResult>() {
                    @Override
                    public void onNext(PsnCountryCodeQueryApplyPreResult result) {
                        queryAccountView.queryCountryCodeSuccess(result.getCountryCode());
                    }
                });

    }

    @Override
    public void psnApplyTermDepositeConfirm(final ApplyAccountModel applyAccountModel, final String factorId) {
        //请求会话
        getConversation().flatMap(new Func1<String, Observable<PsnApplyTermDepositeConfirmResult>>() {
            @Override
            public Observable<PsnApplyTermDepositeConfirmResult> call(String conversationId) {
                //发送预交易
                PsnApplyTermDepositeConfirmParams params = ModelUtil.generateApplyTermDepositeConfirmParams(applyAccountModel,conversationId,factorId);
                return accountService.psnApplyTermDepositeConfirm(params);
            }
        }).compose(this.<PsnApplyTermDepositeConfirmResult>bindToLifecycle())
          .compose(SchedulersCompat.<PsnApplyTermDepositeConfirmResult>applyIoSchedulers())
          .subscribe(new BaseAccountSubscriber<PsnApplyTermDepositeConfirmResult>() {
              @Override
              public void onNext(PsnApplyTermDepositeConfirmResult result) {
                  //回调界面
                  preTransactionSuccess(ModelUtil.generateSecurityModel(result));
              }
          });
    }

    @Override
    public void psnApplyTermDepositeResult(final ApplyAccountModel applyAccountModel, final DeviceInfoModel deviceInfoModel, final String factorId, final String[] randomNums, final String[] encryptPasswords) {
        //获取token
        getToken().flatMap(new Func1<String, Observable<PsnApplyTermDepositeResult>>() {
            @Override
            public Observable<PsnApplyTermDepositeResult> call(String token) {
                //生成参数,提交交易
                PsnApplyTermDepositeParams params = ModelUtil.generateApplyTermDepositeParams(applyAccountModel,getConversationId(),token,deviceInfoModel,factorId,randomNums,encryptPasswords);
                return accountService.psnApplyTermDepositeResult(params);
            }
        }).compose(this.<PsnApplyTermDepositeResult>bindToLifecycle())
          .compose(SchedulersCompat.<PsnApplyTermDepositeResult>applyIoSchedulers())
          .subscribe(new BaseAccountSubscriber<PsnApplyTermDepositeResult>() {
              @Override
              public void onNext(PsnApplyTermDepositeResult result) {
                  //回调界面
                  clearConversation();
                  updateChinaBankAccountList(ModelUtil.generateApplyAccountModel(applyAccountModel,result));
              }
          });
    }

    @Override
    public void afterUpdateChinaBankAccountList(Object object) {
        applyAccountView.depositeResultSuccess((ApplyAccountModel) object);
    }
}


