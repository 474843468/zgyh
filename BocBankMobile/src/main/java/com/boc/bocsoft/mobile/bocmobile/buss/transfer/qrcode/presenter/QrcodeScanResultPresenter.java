package com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.service.AccountService;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCurrencyQuery.PsnCrcdCurrencyQueryParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCurrencyQuery.PsnCrcdCurrencyQueryResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAccountDetail.PsnCrcdQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryAccountDetail.PsnCrcdQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.service.CrcdService;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQueryAllChinaBankAccount.PsnCommonQueryAllChinaBankAccountResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocTransferVerify.PsnTransBocTransferVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransBocTransferVerify.PsnTransBocTransferVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetBocTransferCommissionCharge.PsnTransGetBocTransferCommissionChargeParams;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetBocTransferCommissionCharge.PsnTransGetBocTransferCommissionChargeResult;
import com.boc.bocsoft.mobile.bii.bus.transfer.service.TransferService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.model.AccountDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.model.ScanTransferParamViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.ui.QrcodeScanResultContract;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * 二维码扫描结果页面填写提交交易
 * Created by xdy4486 on 2016/6/24.
 */
public class QrcodeScanResultPresenter extends RxPresenter
        implements QrcodeScanResultContract.Presenter {

    private GlobalService mGlobalService;
    private AccountService mAccountService;
    private CrcdService mCrcdService;
    private TransferService mTransferService;
    private QrcodeScanResultContract.View mScanResultView;

    private String mConversationId;

    public QrcodeScanResultPresenter(QrcodeScanResultContract.View scanResultView) {
        mScanResultView = scanResultView;
        mGlobalService = new GlobalService();
        mAccountService = new AccountService();
        mCrcdService = new CrcdService();
        mTransferService = new TransferService();
    }

    @Override
    public void requestTranoutAccountList() {
        List<PsnCommonQueryAllChinaBankAccountResult> list = getTranoutAccountList();
        //TODO 需要转成什么参数类型？
        mScanResultView.setTranoutAccountList();
    }

    @Override
    public void requestAccountDetail(AccountBean cardItem) {
        // 根据账户类型 查询不同接口
        if (ApplicationConst.ACC_TYPE_GRE.equals(cardItem.getAccountType())) {// 信用卡 查询信用卡详情
            requestForCrcdAccountDetail(cardItem.getAccountNumber(),
                    String.valueOf(cardItem.getAccountId()));
        } else {// 其他类型卡
            requestForOtherAccountDetail(String.valueOf(cardItem.getAccountId()));
        }
    }

    @Override
    public void requestForTransBocTransferVerify(final ScanTransferParamViewModel viewModel) {
        PsnTransBocTransferVerifyParams params = new PsnTransBocTransferVerifyParams();
        params.setConversationId(mConversationId);
        params.setCurrency(ApplicationConst.CURRENCY_CNY);
        params.set_combinId(viewModel.getCombinListBean().getId());
        params.setAmount(viewModel.getAmount());
        params.setExecuteType(ApplicationConst.NOWEXE);
        params.setFromAccountId(viewModel.getFromAccountId());
        params.setPayeeActno(viewModel.getPayeeAccountNumber());
        params.setPayeeMobile(viewModel.getPayeeMobile());
        params.setPayeeName(viewModel.getPayeeName());
        params.setRemark(viewModel.getRemark());
        mTransferService.psnTransBocTransferVerify(params)
                        .compose(this.<PsnTransBocTransferVerifyResult>bindToLifecycle())
                        .flatMap(
                                new Func1<PsnTransBocTransferVerifyResult, Observable<PsnTransGetBocTransferCommissionChargeResult>>() {
                                    @Override
                                    public Observable<PsnTransGetBocTransferCommissionChargeResult> call(
                                            PsnTransBocTransferVerifyResult psnTransBocTransferVerifyResults) {
                                        PsnTransGetBocTransferCommissionChargeParams chargeParams =
                                                new PsnTransGetBocTransferCommissionChargeParams();
                                        chargeParams.setServiceId(ApplicationConst.PB031);
                                        chargeParams.setRemark(viewModel.getRemark());
                                        chargeParams.setFromAccountId(viewModel.getFromAccountId());
                                        chargeParams.setAmount(viewModel.getAmount());
                                        chargeParams.setCurrency(ApplicationConst.CURRENCY_CNY);
                                        chargeParams.setPayeeActno(
                                                viewModel.getPayeeAccountNumber());
                                        chargeParams.setPayeeName(viewModel.getPayeeName());
                                        return mTransferService.psnTransGetBocTransferCommissionCharge(
                                                chargeParams);
                                    }
                                })
                        .compose(
                                SchedulersCompat.<PsnTransGetBocTransferCommissionChargeResult>applyIoSchedulers())
                        .subscribe(
                                new BIIBaseSubscriber<PsnTransGetBocTransferCommissionChargeResult>() {
                                    @Override
                                    public void handleException(
                                            BiiResultErrorException biiResultErrorException) {
                                        mScanResultView.onError(
                                                biiResultErrorException.getMessage());
                                    }

                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onNext(
                                            PsnTransGetBocTransferCommissionChargeResult psnTransGetBocTransferCommissionChargeResult) {
                                        mScanResultView.onTransBocTransferVerify();
                                    }
                                });
    }

    @Override
    public void requestSecurityFactor() {
        mGlobalService.psnCreatConversation(new PSNCreatConversationParams())
                      .compose(this.<String>bindToLifecycle())
                      .flatMap(new Func1<String, Observable<PsnGetSecurityFactorResult>>() {
                          @Override
                          public Observable<PsnGetSecurityFactorResult> call(
                                  String conversationId) {
                              mConversationId = conversationId;
                              PsnGetSecurityFactorParams mSecurityFactorParams =
                                      new PsnGetSecurityFactorParams();
                              mSecurityFactorParams.setConversationId(conversationId);
                              //硬件绑定服务码
                              mSecurityFactorParams.setServiceId(ApplicationConst.PB031);
                              return mGlobalService.psnGetSecurityFactor(mSecurityFactorParams);
                          }
                      })
                      .compose(SchedulersCompat.<PsnGetSecurityFactorResult>applyIoSchedulers())
                      .subscribe(new BIIBaseSubscriber<PsnGetSecurityFactorResult>() {
                          @Override
                          public void handleException(
                                  BiiResultErrorException biiResultErrorException) {
                              mScanResultView.onError(biiResultErrorException.getMessage());
                          }

                          @Override
                          public void onCompleted() {

                          }

                          @Override
                          public void onNext(
                                  PsnGetSecurityFactorResult psnGetSecurityFactorResult) {
                              mScanResultView.onSecurityFactorSuccess(psnGetSecurityFactorResult);
                          }
                      });
    }

    /**
     * 查询信用卡详情
     *
     * @param accountNumber 账号
     * @param accountId 账户ID
     */
    private void requestForCrcdAccountDetail(final String accountNumber, final String accountId) {
        PsnCrcdCurrencyQueryParams params = new PsnCrcdCurrencyQueryParams();
        params.setAccountNumber(accountNumber);
        mCrcdService.psnCrcdCurrencyQuery(params)
                    .compose(this.<PsnCrcdCurrencyQueryResult>bindToLifecycle())
                    .flatMap(
                            new Func1<PsnCrcdCurrencyQueryResult, Observable<PsnCrcdCurrencyQueryResult.CurrencyBean>>() {
                                @Override
                                public Observable<PsnCrcdCurrencyQueryResult.CurrencyBean> call(
                                        PsnCrcdCurrencyQueryResult result) {
                                    return Observable.just(result.getCurrency1(),
                                            result.getCurrency2());
                                }
                            })
                    .filter(new Func1<PsnCrcdCurrencyQueryResult.CurrencyBean, Boolean>() {
                        @Override
                        public Boolean call(PsnCrcdCurrencyQueryResult.CurrencyBean currencyBean) {
                            return ApplicationConst.CURRENCY_CNY.equals(currencyBean.getCode());
                        }
                    })
                    .switchIfEmpty(
                            Observable.<PsnCrcdCurrencyQueryResult.CurrencyBean>empty().observeOn(
                                    AndroidSchedulers.mainThread()).doOnCompleted(new Action0() {
                                @Override
                                public void call() {
                                    mScanResultView.onNoCNYTranoutAccount();
                                }
                            }))
                    .flatMap(
                            new Func1<PsnCrcdCurrencyQueryResult.CurrencyBean, Observable<PsnCrcdQueryAccountDetailResult>>() {
                                @Override
                                public Observable<PsnCrcdQueryAccountDetailResult> call(
                                        PsnCrcdCurrencyQueryResult.CurrencyBean currencyBean) {
                                    PsnCrcdQueryAccountDetailParams params =
                                            new PsnCrcdQueryAccountDetailParams();
                                    params.setAccountId(accountId);
                                    params.setCurrency(currencyBean.getCode());
                                    return mCrcdService.psnCrcdQueryAccountDetailResult(params);
                                }
                            })
                    .compose(SchedulersCompat.<PsnCrcdQueryAccountDetailResult>applyIoSchedulers())
                    .subscribe(new BIIBaseSubscriber<PsnCrcdQueryAccountDetailResult>() {
                        @Override
                        public void handleException(
                                BiiResultErrorException biiResultErrorException) {
                            mScanResultView.onError(biiResultErrorException.getMessage());
                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(
                                PsnCrcdQueryAccountDetailResult psnCrcdQueryAccountDetailResult) {
                            AccountDetailModel accountDetailModel =
                                    convertAccountDetailModelFromCrcd(
                                            psnCrcdQueryAccountDetailResult);
                            mScanResultView.setTranoutAccountDetail(accountDetailModel);
                        }
                    });
    }

    /**
     * 查询其他卡（除信用卡）详情 PsnAccountQueryAccountDetail
     *
     * @param accountId 账户Id
     */
    private void requestForOtherAccountDetail(String accountId) {
        PsnAccountQueryAccountDetailParams params = new PsnAccountQueryAccountDetailParams();
        params.setAccountId(accountId);
        mAccountService.psnAccountQueryAccountDetail(params)
                       .compose(this.<PsnAccountQueryAccountDetailResult>bindToLifecycle())
                       .compose(
                               SchedulersCompat.<PsnAccountQueryAccountDetailResult>applyIoSchedulers())
                       .subscribe(new BIIBaseSubscriber<PsnAccountQueryAccountDetailResult>() {
                           @Override
                           public void handleException(
                                   BiiResultErrorException biiResultErrorException) {
                               mScanResultView.onError(biiResultErrorException.getMessage());
                           }

                           @Override
                           public void onCompleted() {

                           }

                           @Override
                           public void onNext(
                                   PsnAccountQueryAccountDetailResult psnAccountQueryAccountDetailResult) {
                               AccountDetailModel model = convertAccountDetailModelFromOther(
                                       psnAccountQueryAccountDetailResult);
                               mScanResultView.setTranoutAccountDetail(model);
                           }
                       });
    }

    private AccountDetailModel convertAccountDetailModelFromOther(
            PsnAccountQueryAccountDetailResult psnAccountQueryAccountDetailResult) {
        //TODO 转换成view层的账户详情model
        return null;
    }

    private AccountDetailModel convertAccountDetailModelFromCrcd(
            PsnCrcdQueryAccountDetailResult psnCrcdQueryAccountDetailResult) {
        //TODO 转换成view层的账户详情model
        return null;
    }

    /**
     * @return 转出账户列表
     */
    private List<PsnCommonQueryAllChinaBankAccountResult> getTranoutAccountList() {
        String[] accountTypeArr = new String[] {
                ApplicationConst.ACC_TYPE_ORD, ApplicationConst.ACC_TYPE_BRO,
                ApplicationConst.ACC_TYPE_RAN, ApplicationConst.ACC_TYPE_GRE
        };
        //TODO 获取转出账户列表
        return null;
    }
}
