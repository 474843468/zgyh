package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.unsettledbills.presenter;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryFutureBill.PsnCrcdQueryFutureBillParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryFutureBill.PsnCrcdQueryFutureBillResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryFutureBillTotalIncome.PsnCrcdQueryFutureBillTotalIncomeParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryFutureBillTotalIncome.PsnCrcdQueryFutureBillTotalIncomeResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryUnauthorizedTrans.PsnCrcdQueryUnauthorizedTransParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryUnauthorizedTrans.PsnCrcdQueryUnauthorizedTransResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryUnauthorizedTransTotal.PsnCrcdQueryUnauthorizedTransTotalParams;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryUnauthorizedTransTotal.PsnCrcdQueryUnauthorizedTransTotalResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.service.CrcdService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.unsettledbills.model.CrcdBillQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.unsettledbills.model.CrcdUnsettledBillDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.unsettledbills.model.CrcdUnsettledBillsModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.unsettledbills.ui.CrcdBillNContract;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.presenter.BaseCrcdPresenter;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
*   Created by xwg on "16/12/23". on "16:15".
 *   未出账单 presenter
*/

public class CrcdBillNPresenter extends BaseCrcdPresenter implements CrcdBillNContract.Presenter {
    private CrcdService crcdService;

    private CrcdBillNContract.RecordView queryView;
    private CrcdBillNContract.UnrecordView unrecordView;

    public CrcdBillNPresenter(CrcdBillNContract.RecordView queryView) {
        crcdService = new CrcdService();
        this.queryView = queryView;
    }
    public CrcdBillNPresenter(CrcdBillNContract.UnrecordView queryView) {
        crcdService = new CrcdService();
        this.unrecordView = queryView;
    }

    @Override
    public void crcdQueryFutureBill(final CrcdBillQueryViewModel crcdBillQueryModel) {

        getConversation().flatMap(new Func1<String, Observable<PsnCrcdQueryFutureBillResult>>() {
            @Override
            public Observable<PsnCrcdQueryFutureBillResult> call(String conversationId) {
                setConversationId(conversationId);
                PsnCrcdQueryFutureBillParams params=new PsnCrcdQueryFutureBillParams();
                params.setConversationId(conversationId);
                params.setAccountId(crcdBillQueryModel.getAccountId());
                params.setPageSize(crcdBillQueryModel.getPageSize());
                params.setCurrentIndex(crcdBillQueryModel.getCurrentIndex());
                params.set_refresh(crcdBillQueryModel.get_refresh());
                return crcdService.psnCrcdQueryFutureBill(params);
            }
        }).compose(this.<PsnCrcdQueryFutureBillResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdQueryFutureBillResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdQueryFutureBillResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCrcdQueryFutureBillResult result) {
                        queryView.crcdQueryFutureBill(BeanConvertor.toBean(result,new CrcdUnsettledBillDetailModel()));
                    }
                });

    }

    @Override
    public void crcdQueryFutureBillTotalIncome(final CrcdBillQueryViewModel crcdBillQueryModel) {
        PsnCrcdQueryFutureBillTotalIncomeParams params=new PsnCrcdQueryFutureBillTotalIncomeParams();
        params.setAccountId(crcdBillQueryModel.getAccountId());

        crcdService.psnCrcdQueryFutureBillTotalIncome(params)
                .compose(this.<List<PsnCrcdQueryFutureBillTotalIncomeResult>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnCrcdQueryFutureBillTotalIncomeResult>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnCrcdQueryFutureBillTotalIncomeResult>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(List<PsnCrcdQueryFutureBillTotalIncomeResult> results) {
                        if (results!=null){
                            List<CrcdUnsettledBillsModel> crcdUnsettledBillsModels=new ArrayList<CrcdUnsettledBillsModel>();
                            for (PsnCrcdQueryFutureBillTotalIncomeResult result:results){
                                CrcdUnsettledBillsModel crcdUnsettledBillsModel =new CrcdUnsettledBillsModel();
                                crcdUnsettledBillsModels.add(BeanConvertor.toBean(result,crcdUnsettledBillsModel));
                            }
                            queryView.crcdQueryFutureBillTotalIncome(crcdUnsettledBillsModels);
                        }

                    }
                });

    }

    @Override
    public void crcdQueryUnauthorizedTrans(final CrcdBillQueryViewModel crcdBillQueryModel) {
        getConversation().flatMap(new Func1<String, Observable<PsnCrcdQueryUnauthorizedTransResult>>() {
            @Override
            public Observable<PsnCrcdQueryUnauthorizedTransResult> call(String s) {
                setConversationId(s);
                PsnCrcdQueryUnauthorizedTransParams params=new PsnCrcdQueryUnauthorizedTransParams();
                params.setConversationId(s);
                params.setPageSize(crcdBillQueryModel.getPageSize());
                params.setCurrentIndex(crcdBillQueryModel.getCurrentIndex());
                params.set_refresh(crcdBillQueryModel.get_refresh());
                params.setAccountId(crcdBillQueryModel.getAccountId());
                return crcdService.psnCrcdQueryUnauthorizedTrans(params);
            }
        }).compose(this.<PsnCrcdQueryUnauthorizedTransResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCrcdQueryUnauthorizedTransResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCrcdQueryUnauthorizedTransResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCrcdQueryUnauthorizedTransResult result) {
                        unrecordView.crcdQueryUnauthorizedTrans(BeanConvertor.toBean(result,new CrcdUnsettledBillDetailModel()));
                    }
                });

    }

    @Override
    public void crcdQueryUnauthorizedTransToatal(CrcdBillQueryViewModel crcdBillQueryModel) {
        PsnCrcdQueryUnauthorizedTransTotalParams params=new PsnCrcdQueryUnauthorizedTransTotalParams();
        params.setAccountId(crcdBillQueryModel.getAccountId());

        crcdService.psnCrcdQueryUnauthorizedTransTotal(params)
                .compose(this.<List<PsnCrcdQueryUnauthorizedTransTotalResult>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnCrcdQueryUnauthorizedTransTotalResult>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnCrcdQueryUnauthorizedTransTotalResult>>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(List<PsnCrcdQueryUnauthorizedTransTotalResult> results) {
                        if (results!=null){
                            List<CrcdUnsettledBillsModel> crcdUnsettledBillsModels=new ArrayList<CrcdUnsettledBillsModel>();
                            for (PsnCrcdQueryUnauthorizedTransTotalResult result:results){
                                CrcdUnsettledBillsModel crcdUnsettledBillsModel =new CrcdUnsettledBillsModel();
                                crcdUnsettledBillsModels.add(BeanConvertor.toBean(result,crcdUnsettledBillsModel));
                            }
                           unrecordView.crcdQueryUnauthorizedTransToatal(crcdUnsettledBillsModels);
                        }
                    }
                });
    }
}
