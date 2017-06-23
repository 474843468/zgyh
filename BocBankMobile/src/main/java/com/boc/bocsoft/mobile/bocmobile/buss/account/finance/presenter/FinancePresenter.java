package com.boc.bocsoft.mobile.bocmobile.buss.account.finance.presenter;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICAccount.PsnFinanceICAccountParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICAccount.PsnFinanceICAccountResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICAccountDetail.PsnFinanceICAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICAccountDetail.PsnFinanceICAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICIsSign.PsnFinanceICIsSignParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICSignCancel.PsnFinanceICSignCancelParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICSignCreat.PsnFinanceICSignCreatParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICSignCreat.PsnFinanceICSignCreatResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICTransferDetail.PsnFinanceICTransferDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICTransferDetail.TransDetail;
import com.boc.bocsoft.mobile.bii.bus.account.service.FinanceService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.transactionlist.TransactionBean;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseTransactionPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.model.FinanceModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.model.TransDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.ModelUtil;
import com.boc.bocsoft.mobile.common.utils.RxUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.exceptions.CompositeException;
import rx.functions.Func1;

/**
 * @author wangyang
 *         16/6/20 16:00
 *         电子现金账户业务逻辑处理
 */
public class FinancePresenter extends BaseTransactionPresenter implements FinanceContract.Presenter {

    /**
     * 账户列表界面View
     */
    private FinanceContract.FinanceAccountView financeAccountView;
    /**
     * 交易明细界面View
     */
    private FinanceContract.FinanceAccountTransferView financeAccountTransferView;
    /**
     * 签约界面
     */
    private FinanceContract.FinanceAccountSignView financeAccountSignView;
    /**
     * 电子现金账户Service
     */
    private FinanceService financeService;

    public FinancePresenter(FinanceContract.FinanceAccountView financeAccountView) {
        super(financeAccountView);
        this.financeAccountView = financeAccountView;
        init();
    }

    public FinancePresenter(FinanceContract.FinanceAccountTransferView financeAccountTransferView) {
        super(financeAccountTransferView);
        this.financeAccountTransferView = financeAccountTransferView;
        init();
    }

    public FinancePresenter(FinanceContract.FinanceAccountSignView financeAccountSignView) {
        super(financeAccountSignView);
        this.financeAccountSignView = financeAccountSignView;
        init();
    }

    private void init() {
        financeService = new FinanceService();
    }

    /**
     * 电子账户信息概览
     */
    @Override
    public void psnFinanceAccountView() {
        //请求账户列表
        financeService.psnFinanceICAccountView(new PsnFinanceICAccountParams())
                .compose(this.<List<PsnFinanceICAccountResult>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnFinanceICAccountResult>>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<List<PsnFinanceICAccountResult>>() {
                    @Override
                    public void onNext(List<PsnFinanceICAccountResult> results) {
                        financeAccountView.psnFinanceAccountView(ModelUtil.generateFinanceModel(results));
                    }
                });
    }

    @Override
    public void psnFinanceICAccountDetail(List<FinanceModel> financeModels) {
        //并发请求每个账户的详情
        Observable.just(financeModels).compose(RxUtils.concurrentInIOListRequestTransformer(new Func1<FinanceModel, Observable<PsnFinanceICAccountDetailResult>>() {
            @Override
            public Observable<PsnFinanceICAccountDetailResult> call(FinanceModel financeModel) {
                return queryFinanceICAccountDetail(financeModel);
            }
        })).compose(this.<PsnFinanceICAccountDetailResult>bindToLifecycle()).subscribe(new FinanceAccountSubscriber());
    }

    @Override
    public void queryAllOfFinance(FinanceModel financeModel) {
        Observable<Object> observable = Observable.mergeDelayError(psnFinanceTransferDetail(financeModel.getAccountId()), psnFinanceSignQuery(financeModel));

        if (financeModel.getSupplyBalance() == null)
            observable = Observable.mergeDelayError(queryFinanceICAccountDetail(financeModel), psnFinanceTransferDetail(financeModel.getAccountId()), psnFinanceSignQuery(financeModel));
        observable.compose(bindToLifecycle()).subscribe(new FinanceSubscriber(financeModel));
    }

    @Override
    public void psnFinanceICAccountDetail(final FinanceModel financeModel) {
        financeService.psnFinanceICAccountDetail(new PsnFinanceICAccountDetailParams(financeModel.getAccountId()))
                .compose(SchedulersCompat.<PsnFinanceICAccountDetailResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnFinanceICAccountDetailResult>() {
                    @Override
                    public void onNext(PsnFinanceICAccountDetailResult result) {
                        financeAccountView.psnFinanceAccountBalanceView(ModelUtil.generateFinanceModel(financeModel, result));
                    }

                    @Override
                    public void handleException(BiiResultErrorException error) {
                        financeAccountView.psnFinanceAccountBalanceView(financeModel);
                    }
                });
    }

    private Observable<PsnFinanceICAccountDetailResult> queryFinanceICAccountDetail(final FinanceModel financeModel) {
        return financeService.psnFinanceICAccountDetail(new PsnFinanceICAccountDetailParams(financeModel.getAccountId()))
                .compose(SchedulersCompat.<PsnFinanceICAccountDetailResult>applyIoSchedulers());
    }

    /**
     * 获取账户最近交易明细
     *
     * @param accountId
     */
    private Observable<PsnFinanceICTransferDetailResult> psnFinanceTransferDetail(final String accountId) {
        //获取账户交易明细
        return getConversation().
                flatMap(new Func1<String, Observable<PsnFinanceICTransferDetailResult>>() {
                    @Override
                    public Observable<PsnFinanceICTransferDetailResult> call(String conversationId) {
                        return financeService.psnFinanceICTransferDetail(ModelUtil.generatePsnFinanceICTransferDetailParams(accountId, conversationId));
                    }
                }).compose(SchedulersCompat.<PsnFinanceICTransferDetailResult>applyIoSchedulers());
    }

    /**
     * 查询绑定关系
     *
     * @param financeModel
     */
    private Observable<String> psnFinanceSignQuery(FinanceModel financeModel) {
        if (!financeModel.isFinanceAccount())
            return Observable.just(financeModel.getFinanICType());

        //查询绑定关系
        int financeICAccountId = Integer.parseInt(financeModel.getAccountId());
        return financeService.psnFinanceICIsSign(new PsnFinanceICIsSignParams(financeICAccountId))
                //设置线程
                .compose(SchedulersCompat.<String>applyIoSchedulers());
    }

    /**
     * 解除绑定关系
     *
     * @param financeModel
     */
    @Override
    public void psnFinanceICSignCancel(final FinanceModel financeModel) {
        //获取token,绑定生命周期
        getToken().compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String token) {
                        //发送请求
                        PsnFinanceICSignCancelParams params = ModelUtil.generatePsnFinanceICSignCancelParams(financeModel, getConversationId(), token);
                        return financeService.psnFinanceICSignCancel(params);
                    }
                }).compose(SchedulersCompat.<String>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<String>() {
                    @Override
                    public void onNext(String result) {
                        //回调界面,清除会话
                        clearConversation();
                        financeAccountTransferView.cancelSignSuccess();
                    }
                });
    }

    /**
     * 新建签约关系-预交易
     *
     * @param financeModel
     * @param factorId
     */
    @Override
    public void psnFinanceICSignCreate(final FinanceModel financeModel, final String factorId) {
        //获取Token并发送预交易请求
        getToken().flatMap(new Func1<String, Observable<PsnFinanceICSignCreatResult>>() {
            @Override
            public Observable<PsnFinanceICSignCreatResult> call(String token) {
                PsnFinanceICSignCreatParams params = ModelUtil.generatePsnFinanceICSignCreatParams(financeModel, getConversationId(), token, factorId);
                return financeService.psnFinanceICSignCreat(params);
            }
        })
                .compose(this.<PsnFinanceICSignCreatResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnFinanceICSignCreatResult>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<PsnFinanceICSignCreatResult>() {
                    @Override
                    public void onNext(PsnFinanceICSignCreatResult result) {
                        //回调界面
                        preTransactionSuccess(ModelUtil.generateSecurityModel(result));
                    }
                });
    }


    /**
     * 新建签约关系-交易
     *
     * @param financeModel
     * @param deviceInfoModel
     * @param factorId
     * @param randomNums
     * @param encryptPasswords
     */
    @Override
    public void psnFinanceICSignCreateRes(final FinanceModel financeModel, final DeviceInfoModel deviceInfoModel, final String factorId, final String[] randomNums, final String[] encryptPasswords) {
        //获取Token,发送交易请求
        getToken()
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String token) {
                        return financeService.psnFinanceICSignCreatRes(ModelUtil.generatePsnFinanceICSignCreatResParams(financeModel, getConversationId(), token, deviceInfoModel, factorId, randomNums, encryptPasswords));
                    }
                }).compose(SchedulersCompat.<String>applyIoSchedulers())
                .subscribe(new BaseAccountSubscriber<String>() {
                    @Override
                    public void onNext(String result) {
                        //回调界面,清除会话
                        clearConversation();
                        financeAccountSignView.psnFinanceICSignCreateResSuccess();
                    }
                });
    }

    /**
     * @author wangyang
     * @time 16/7/6 14:58
     * <p/>
     * 请求账户详情处理逻辑
     */
    private class FinanceAccountSubscriber extends BIIBaseSubscriber<PsnFinanceICAccountDetailResult> {

        @Override
        public void handleException(BiiResultErrorException error) {
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onNext(PsnFinanceICAccountDetailResult result) {
            //将账户详情,更新到账户列表中,并刷新界面
            FinanceModel financeModel = financeAccountView.getFinanceModel(ModelUtil.generateFinanceModelOnlyICNumber(result));
            financeAccountView.psnFinanceAccountBalanceView(ModelUtil.generateFinanceModel(financeModel, result));
        }
    }

    private class FinanceSubscriber extends BaseAccountSubscriber<Object> {

        private PsnFinanceICAccountDetailResult detailResult;

        private PsnFinanceICTransferDetailResult transResult;

        private String bankAccountId;

        private FinanceModel financeModel;

        public FinanceSubscriber(FinanceModel financeModel) {
            this.financeModel = financeModel;
        }

        @Override
        public void onNext(Object o) {
            if (o == null) {
                handleException(null);
                return;
            }
            if (o instanceof PsnFinanceICAccountDetailResult) {
                detailResult = (PsnFinanceICAccountDetailResult) o;
                financeAccountTransferView.psnFinanceAccountDetail(ModelUtil.generateFinanceModel(financeModel, detailResult));
            } else if (o instanceof PsnFinanceICTransferDetailResult) {
                transResult = (PsnFinanceICTransferDetailResult) o;

                if (transResult.getRecordNumber() == 0) {
                    financeAccountTransferView.psnFinanceTransferDetail(null, null);
                    return;
                }

                //将交易明细转换为TransactionView的TransactionBean
                List<TransactionBean> transactionList = new ArrayList<>();
                //将交易明细转换为TransDetailModel
                List<TransDetailModel> detailModels = new ArrayList<>();
                for (TransDetail transDetail : transResult.getTransDetails()) {
                    transactionList.add(ModelUtil.generateTransactionBean(transDetail));
                    detailModels.add(ModelUtil.generateTransDetailModel(transDetail));
                }

                //回调界面
                financeAccountTransferView.psnFinanceTransferDetail(detailModels, transactionList);
            } else {
                bankAccountId = o.toString();
                if (!FinanceModel.TYPE_NOT_ACCOUNT.equals(bankAccountId))
                    financeAccountTransferView.psnFinanceSignQuery(bankAccountId);
            }
        }

        @Override
        public void handleException(BiiResultErrorException error) {
            if (detailResult == null && financeModel.getSupplyBalance() == null)
                financeAccountTransferView.psnFinanceAccountDetail(null);

            if (transResult == null)
                financeAccountTransferView.psnFinanceTransferDetail(null, null);

            if (StringUtils.isEmptyOrNull(bankAccountId))
                financeAccountTransferView.psnFinanceSignQuery(null);
        }

        @Override
        public void onError(Throwable e) {
            if (e instanceof CompositeException) {
                CompositeException compositeException = (CompositeException) e;
                e = compositeException.getExceptions().get(0);
            }
            super.onError(e);
        }

        @Override
        public void onCompleted() {
            detailResult = null;
            transResult = null;
            bankAccountId = null;
            ((BussFragment) financeAccountTransferView).closeProgressDialog();
        }
    }
}
