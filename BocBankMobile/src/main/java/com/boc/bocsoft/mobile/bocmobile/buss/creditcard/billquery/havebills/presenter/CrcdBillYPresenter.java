package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.havebills.presenter;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayBillSetInput.PsnCrcdDividedPayBillSetInputParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayBillSetInput.PsnCrcdDividedPayBillSetInputResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryBilledTrans.PsnCrcdQueryBilledTransParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryBilledTrans.PsnCrcdQueryBilledTransResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryBilledTransDetail.PsnCrcdQueryBilledTransDetailParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryBilledTransDetail.PsnCrcdQueryBilledTransDetailResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQuerySettingsInfo.PsnCrcdQuerySettingsInfoParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQuerySettingsInfo.PsnCrcdQuerySettingsInfoResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnQueryCrcdBillIsExist.PsnQueryCrcdBillIsExistParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnQueryCrcdBillIsExist.PsnQueryCrcdBillIsExistResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnReSetEmailPaperCheck.PsnReSetEmailPaperCheckParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnReSetEmailPaperCheck.PsnReSetEmailPaperCheckResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnReSetSmsPaperCheck.PsnReSetSmsPaperCheckParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnReSetSmsPaperCheck.PsnReSetSmsPaperCheckResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.service.CrcdService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.havebills.model.CrcdBillQueryModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.havebills.model.CrcdBilledDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.havebills.model.CrcdBilledModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.havebills.model.CrcdSetingsInfoModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.havebills.ui.CrcdBillYContract;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.presenter.BaseCrcdPresenter;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.common.utils.RxUtils;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * 信用卡已出账单与历史账单网络请求
 * Created by liuweidong on 2016/12/14.
 */
public class CrcdBillYPresenter extends BaseCrcdPresenter implements CrcdBillYContract.Presenter {

    private CrcdService crcdService;
    private CrcdBillYContract.CrcdBillQueryView billQueryView;// 已出账单
    private CrcdBillYContract.CrcdBillHistoryView historyView;// 历史账单

    public CrcdBillYPresenter() {
        crcdService = new CrcdService();
    }

    public CrcdBillYPresenter(CrcdBillYContract.CrcdBillQueryView view) {// 已出账单
        this();
        this.billQueryView = view;
    }

    public CrcdBillYPresenter(CrcdBillYContract.CrcdBillHistoryView historyView) {// 历史账单
        this();
        this.historyView = historyView;
    }

    /**
     * 4.94 094 信用卡当月是否已出账单查询PsnQueryCrcdBillIsExist
     *
     * @param accountId
     */
    @Override
    public void queryCrcdBillIsExist(String accountId) {
        PsnQueryCrcdBillIsExistParams params = new PsnQueryCrcdBillIsExistParams();
        params.setAccountId(accountId);
        crcdService.psnQueryCrcdBillIsExist(params)
                .compose(this.<PsnQueryCrcdBillIsExistResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnQueryCrcdBillIsExistResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnQueryCrcdBillIsExistResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnQueryCrcdBillIsExistResult result) {
                        billQueryView.queryCrcdBillIsExistSuccess(result.getIsBillExist());
                    }
                });
    }

    /**
     * 4.7 007查询信用卡已出账单PsnCrcdQueryBilledTrans
     *
     * @param queryModle
     */
    @Override
    public void crcdQueryBilledTrans(final CrcdBillQueryModel queryModle) {
//        GlobalService.psnCommonQuerySystemDateTime(new PsnCommonQuerySystemDateTimeParams())
//                .flatMap(new Func1<PsnCommonQuerySystemDateTimeResult, Observable<String>>() {
//                    @Override
//                    public Observable<String> call(PsnCommonQuerySystemDateTimeResult result) {
//                        queryModle.setStatementMonth(result.getDateTme().toLocalDate().format(DateFormatters.monthFormatter2));
//                        return globalService.psnCreatConversation(new PSNCreatConversationParams());
//                    }
//                })
        getConversation()
                .flatMap(new Func1<String, Observable<PsnCrcdQueryBilledTransResult>>() {

                    @Override
                    public Observable<PsnCrcdQueryBilledTransResult> call(String conversationId) {
                        setConversationId(conversationId);
                        PsnCrcdQueryBilledTransParams params = new PsnCrcdQueryBilledTransParams();
                        params.setConversationId(conversationId);
                        params.setAccountId(queryModle.getAccountId());
                        params.setStatementMonth(queryModle.getStatementMonth());
                        return crcdService.psnCrcdQueryBilledTrans(params);
                    }
                })
                .compose(this.<PsnCrcdQueryBilledTransResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdQueryBilledTransResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdQueryBilledTransResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCrcdQueryBilledTransResult result) {
                        CrcdBilledModel billedModel = new CrcdBilledModel();
                        billedModel = BeanConvertor.toBean(result, billedModel);
                        billedModel.setCrcdCustomerInfo(BeanConvertor.toBean(result.getCrcdCustomerInfo(), new CrcdBilledModel.CrcdCustomerInfoBean()));
                        billQueryView.crcdQueryBilledTrans(billedModel);
                    }
                });
    }

    /**
     * 4.8 008查询信用卡已出账单交易明细PsnCrcdQueryBilledTransDetail
     *
     * @param queryModle
     */
    @Override
    public void crcdQueryBilledTransDetail(final CrcdBillQueryModel queryModle) {
        getConversation()
                .flatMap(new Func1<String, Observable<PsnCrcdQueryBilledTransDetailResult>>() {
                    @Override
                    public Observable<PsnCrcdQueryBilledTransDetailResult> call(String converSationId) {
                        PsnCrcdQueryBilledTransDetailParams params = new PsnCrcdQueryBilledTransDetailParams();
                        setConversationId(converSationId);
                        params.setConversationId(converSationId);
                        params.setAccountType(queryModle.getAccountType());
                        params.setStatementMonth(queryModle.getStatementMonth());
                        params.setCreditcardId(queryModle.getCreditcardId());
                        params.setPrimary(queryModle.getPrimary());
                        params.setLineNum(queryModle.getLineNum());
                        params.setPageNo(queryModle.getPageNo());
                        return crcdService.psnCrcdQueryBilledTransDetail(params);
                    }
                }).compose(this.<PsnCrcdQueryBilledTransDetailResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdQueryBilledTransDetailResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdQueryBilledTransDetailResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        billQueryView.crcdQueryBilledTransDetail(null);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCrcdQueryBilledTransDetailResult result) {
                        CrcdBilledDetailModel crcdBilledDetailModel = new CrcdBilledDetailModel();
                        crcdBilledDetailModel.setDealCount(result.getDealCount());
                        crcdBilledDetailModel.setPageNo(result.getPageNo());
                        crcdBilledDetailModel.setPrimary(result.getPrimary());
                        crcdBilledDetailModel.setSumNo(result.getSumNo());

                        List<CrcdBilledDetailModel.TransListBean> listBeen = new ArrayList<CrcdBilledDetailModel.TransListBean>();
                        for (PsnCrcdQueryBilledTransDetailResult.TransListBean bean : result.getTransList()) {
                            listBeen.add(BeanConvertor.toBean(bean, (new CrcdBilledDetailModel.TransListBean())));
                        }
                        crcdBilledDetailModel.setTransList(listBeen);
                        billQueryView.crcdQueryBilledTransDetail(crcdBilledDetailModel);
                    }
                });
    }

    /**
     * 4.29 029办理账单分期输入PsnCrcdDividedPayBillSetInput
     *
     * @param accountBean
     */
    @Override
    public void crcdDividedPayBillSetInput(AccountBean accountBean) {
        PsnCrcdDividedPayBillSetInputParams params = new PsnCrcdDividedPayBillSetInputParams();
        params.setAccountId(accountBean.getAccountId());
        params.setCurrencyCode(ApplicationConst.CURRENCY_CNY.equals(accountBean.getCurrencyCode()) ? accountBean.getCurrencyCode() : accountBean.getCurrencyCode2());

        crcdService.psnCrcdDividedPayBillSetInput(params)
                .compose(this.<PsnCrcdDividedPayBillSetInputResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdDividedPayBillSetInputResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdDividedPayBillSetInputResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCrcdDividedPayBillSetInputResult result) {
                        billQueryView.crcdDividedPayBillSetInput(result.getUpInstmtAmount(), result.getLowInstmtAmount());
                    }
                });
    }

    /**
     * 并发查询信用卡半年历史账单
     */
    @Override
    public void queryCrcdHistoryBill(final List<String> dates, final String accountID) {
        Observable.just(dates).compose(this.<List<String>>bindToLifecycle())
                .compose(RxUtils.concurrentInIOListRequestTransformer(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String date) {
                        PsnCrcdQueryBilledTransParams params = new PsnCrcdQueryBilledTransParams();
                        params.setAccountId(accountID);
                        params.setStatementMonth(date);
                        crcdService.psnCrcdQueryBilledTrans(params)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new BIIBaseSubscriber<PsnCrcdQueryBilledTransResult>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onNext(PsnCrcdQueryBilledTransResult result) {

                                    }

                                    @Override
                                    public void handleException(BiiResultErrorException biiResultErrorException) {

                                    }

                                    @Override
                                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {

                                    }
                                });
//                        return Observable.just(String);
                        return null;
                    }
                })).subscribe();
    }

    @Override
    public void querySettingsInfo(final String accountId) {

        getConversation().flatMap(new Func1<String, Observable<PsnCrcdQuerySettingsInfoResult>>() {
            @Override
            public Observable<PsnCrcdQuerySettingsInfoResult> call(String conversationId) {
                PsnCrcdQuerySettingsInfoParams params = new PsnCrcdQuerySettingsInfoParams();
                params.setAccountId(accountId);
                params.setConversationId(conversationId);
                return crcdService.psnCrcdQuerySettingsInfo(params);
            }
        }).compose(this.<PsnCrcdQuerySettingsInfoResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdQuerySettingsInfoResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdQuerySettingsInfoResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCrcdQuerySettingsInfoResult result) {
                        CrcdSetingsInfoModel model = new CrcdSetingsInfoModel();
                        model.setEmailStatment(result.getEmailStatment());
                        model.setEmailStatmentStaus(result.getEmailStatment());
                        model.setPhoneStatment(result.getPhoneStatment());
                        model.setPhoneStatmentStaus(result.getPhoneStatment());
                        billQueryView.querySettingsInfo(model);
                    }
                });


    }

    @Override
    public void reSetEmailPagerCheck(String accountId, String billDate, String billAddress) {
        PsnReSetEmailPaperCheckParams params = new PsnReSetEmailPaperCheckParams();
        params.setAccountId(accountId);
        params.setBillAddress(billAddress);
        params.setBillDate(billDate);

        crcdService.psnReSetEmailPaperCheck(params)
                .compose(this.<PsnReSetEmailPaperCheckResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnReSetEmailPaperCheckResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnReSetEmailPaperCheckResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnReSetEmailPaperCheckResult psnReSetEmailPaperCheckResult) {
                        billQueryView.reSetEmailPagerCheck();
                    }
                });
    }

    @Override
    public void reSetSmsPagerCheck(String accountId, String billDate, String billAddress) {
        PsnReSetSmsPaperCheckParams params = new PsnReSetSmsPaperCheckParams();
        params.setAccountId(accountId);
        params.setBillAddress(billAddress);
        params.setBillDate(billDate);
        crcdService.psnReSetSmsPaperCheck(params)
                .compose(this.<PsnReSetSmsPaperCheckResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnReSetSmsPaperCheckResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnReSetSmsPaperCheckResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnReSetSmsPaperCheckResult psnReSetEmailPaperCheckResult) {
                        billQueryView.reSetSmsPagerCheck();
                    }
                });
    }

}
