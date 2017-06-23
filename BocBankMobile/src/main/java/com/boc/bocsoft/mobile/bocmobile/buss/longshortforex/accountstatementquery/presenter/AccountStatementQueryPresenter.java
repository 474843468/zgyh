package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountstatementquery.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGGetRegCurrency.PsnVFGGetRegCurrencyParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGTradeInfoQuery.PsnVFGTradeInfoQueryParams;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGTradeInfoQuery.PsnVFGTradeInfoQueryResult;
import com.boc.bocsoft.mobile.bii.bus.longshortforex.service.LongShortForexService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountstatementquery.model.AccountStatementQueryModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountstatementquery.ui.AccountStatementQueryContract;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.model.XpadPsnVFGGetRegCurrencyModel;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 *双向宝-对账单查询  通信逻辑处理
 * Created by wjk on 2016/11/25
 */

public class AccountStatementQueryPresenter extends RxPresenter implements AccountStatementQueryContract.Presenter {

    private AccountStatementQueryContract.View mAccountStatementQueryView;
    private LongShortForexService mLongShortForexService;

    /**
     * 会话
     */
    public String conversationID=null;
    /**
     * 公共service
     */
    private GlobalService globalService;

    public AccountStatementQueryPresenter(AccountStatementQueryContract.View accountStatementQueryView) {
        this.mAccountStatementQueryView = accountStatementQueryView;
        mAccountStatementQueryView.setPresenter(this);
        globalService=new GlobalService();
        mLongShortForexService=new LongShortForexService();
    }


    @Override
    public void psnXpadGetRegCurrency(final XpadPsnVFGGetRegCurrencyModel viewmodel) {
        PsnVFGGetRegCurrencyParams psnVFGGetRegCurrencyParams = new PsnVFGGetRegCurrencyParams();
        mLongShortForexService.psnXpadGetRegCurrency(psnVFGGetRegCurrencyParams)
                .compose(SchedulersCompat.<List<String>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<String>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        //                        ((BussFragment) mWealthHistoryListView).closeProgressDialog();

                        mAccountStatementQueryView.psnXpadGetRegCurrencyFail(biiResultErrorException);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(List<String> psnVFGGetRegCurrencyResults) {
                        mAccountStatementQueryView.psnXpadGetRegCurrencySuccess(psnVFGGetRegCurrencyResults);
                    }
                });
    }

    @Override
    public void queryAccountStatementList (final AccountStatementQueryModel model){
        if(conversationID == null){
            queryWithoutConversation(model);
        }
        else{
            queryWithConversation(model);
        }
    }

    private void queryWithConversation(final AccountStatementQueryModel accountStatementQueryModel){
        PsnVFGTradeInfoQueryParams params=buildParamsFromView(accountStatementQueryModel);
        params.setConversationId(conversationID);
        params.set_refresh("false");
        mLongShortForexService.psnXpadTradeInfoQuery(params)
                .compose(this.<PsnVFGTradeInfoQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnVFGTradeInfoQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnVFGTradeInfoQueryResult>() {
                    //重写请求失败弹窗处理
                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnVFGTradeInfoQueryResult psnVFGTradeInfoQueryResult) {
                        AccountStatementQueryModel viewModel = buildViewModelFromResult(psnVFGTradeInfoQueryResult);
                        mAccountStatementQueryView.queryAccountStatementListSuccess(viewModel);
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mAccountStatementQueryView.queryAccountStatementListFail(biiResultErrorException);
                    }
                });
    }
    private void queryWithoutConversation(final AccountStatementQueryModel accountStatementQueryModel){
        globalService.psnCreatConversation(new PSNCreatConversationParams())
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnVFGTradeInfoQueryResult>>() {
                    @Override
                    public Observable<PsnVFGTradeInfoQueryResult> call(
                            String conversationId) {
                        conversationID = conversationId;
                        PsnVFGTradeInfoQueryParams params =
                                buildParamsFromView(accountStatementQueryModel);
                        params.setConversationId(conversationId);
                        return mLongShortForexService.psnXpadTradeInfoQuery(params);
                    }
                })
                .compose(SchedulersCompat.<PsnVFGTradeInfoQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnVFGTradeInfoQueryResult>() {

                    //重写请求失败弹窗处理

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnVFGTradeInfoQueryResult psnVFGTradeInfoQueryResult) {
                        AccountStatementQueryModel viewModel = buildViewModelFromResult(psnVFGTradeInfoQueryResult);
                        mAccountStatementQueryView.queryAccountStatementListSuccess(viewModel);
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mAccountStatementQueryView.queryAccountStatementListFail(biiResultErrorException);
                    }
                });
    }

    //根据屏幕数据构造请求参数
    private PsnVFGTradeInfoQueryParams buildParamsFromView(AccountStatementQueryModel accountStatementQueryModel) {
        PsnVFGTradeInfoQueryParams result=new PsnVFGTradeInfoQueryParams();
        result.set_refresh(accountStatementQueryModel.get_refresh());
        result.setQueryType(accountStatementQueryModel.getQueryType());
        result.setStartDate(accountStatementQueryModel.getStartDate());
        result.setEndDate(accountStatementQueryModel.getEndDate());
        result.setCurrencyCode(accountStatementQueryModel.getCurrencyCode());
        result.setPageSize(accountStatementQueryModel.getPageSize());
        result.setCurrentIndex(accountStatementQueryModel.getCurrentIndex());
        return result;
    }

    //根据网络返回结果构造屏幕显示数据
    private AccountStatementQueryModel buildViewModelFromResult(PsnVFGTradeInfoQueryResult psnVFGTradeInfoQueryResult) {
        AccountStatementQueryModel result=new AccountStatementQueryModel();
        result.setRecordNumber(psnVFGTradeInfoQueryResult.getRecordNumber());

        List<AccountStatementQueryModel.AccountStatementQueryBean> viewList=new LinkedList<>();
        List<PsnVFGTradeInfoQueryResult.XpadPsnVFGTradeInfoQueryResultEntity> returnList=psnVFGTradeInfoQueryResult.getList();

        if (returnList!=null){
            for (int i=0;i<returnList.size();i++){
                AccountStatementQueryModel.AccountStatementQueryBean viewBean=new AccountStatementQueryModel.AccountStatementQueryBean();
                PsnVFGTradeInfoQueryResult.XpadPsnVFGTradeInfoQueryResultEntity returnBean=returnList.get(i);

                AccountStatementQueryModel.AccountStatementQueryBean.FundCurrencyEntity fundCurrencyEntity=
                        new AccountStatementQueryModel.AccountStatementQueryBean.FundCurrencyEntity();
                BeanConvertor.toBean(returnBean.getFundCurrency(),fundCurrencyEntity);


                viewBean.setFundSeq(returnBean.getFundSeq());
                viewBean.setFundTransferType(returnBean.getFundTransferType());
                viewBean.setNoteCashFlag(returnBean.getNoteCashFlag());
                viewBean.setTransferAmount(returnBean.getTransferAmount());
                viewBean.setTransferDate(returnBean.getTransferDate());
                viewBean.setTransferDir(returnBean.getTransferDir());
                viewBean.setFundCurrency(fundCurrencyEntity);
                viewBean.setBalance(returnBean.getBalance());

                viewList.add(viewBean);
            }
        }
        result.setList(viewList);
        return result;
    }
}
