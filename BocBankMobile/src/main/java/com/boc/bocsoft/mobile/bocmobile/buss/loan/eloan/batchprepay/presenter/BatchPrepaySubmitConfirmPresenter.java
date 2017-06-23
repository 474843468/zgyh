package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.batchprepay.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANBatchRepaySubmit.PsnELOANBatchRepaySubmitParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANBatchRepaySubmit.PsnELOANBatchRepaySubmitResultBean;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.batchprepay.ui.submit.BatchPrepaySubmitConfirmContract;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * 贷款管理-中银E贷-批量提前还款确认
 * Created by liuzc on 2016/9/8.
 */
public class BatchPrepaySubmitConfirmPresenter extends RxPresenter implements BatchPrepaySubmitConfirmContract.Presenter{

    private BatchPrepaySubmitConfirmContract.View mContractView;
    private PsnLoanService mLoanService;
    private GlobalService globalService;

    /**
     * 会话Id-一种交易获取token,预交易,交易使用一个conversationId
     */
    private String conversationId;

    public BatchPrepaySubmitConfirmPresenter(BatchPrepaySubmitConfirmContract.View view){
        mContractView = view;
        mContractView.setPresenter(this);

        mLoanService = new PsnLoanService();
        globalService = new GlobalService();
    }

    @Override
    public void batchPrepaySubmit(final PsnELOANBatchRepaySubmitParams params) {
//        List<PsnELOANBatchRepaySubmitResultBean> result = new List<PsnELOANBatchRepaySubmitResultBean>();
//        List<List<PsnELOANBatchRepaySubmitResultBean>.ListBean> list =
//                new LinkedList<List<PsnELOANBatchRepaySubmitResultBean>.ListBean>();
//        for(int i = 0; i < params.getListBeen().size(); i ++){
//            List<PsnELOANBatchRepaySubmitResultBean>.ListBean bean = new List<PsnELOANBatchRepaySubmitResultBean>.ListBean();
//            bean.setPayAccount("123244143214");
//            bean.setAdvanceRepayCapital("3000");
//            bean.setAdvanceRepayInterest("200");
//            bean.setLoanAccount("2432142413");
//            bean.setStatus("A");
//            bean.setTransactionId("1232412341");
//            if(i == 1){
//                bean.setStatus("B");
//                bean.setErrorCode("12334");
//                bean.setErrorMsg("网络不通无法提交");
//            }
//            list.add(bean);
//        }
//        result.setBatchRepayList(list);
//        mContractView.batchPrepaySubmitSuccess(result);

        getToken().flatMap(new Func1<String, Observable<List<PsnELOANBatchRepaySubmitResultBean>>>() {
                    @Override
                    public Observable<List<PsnELOANBatchRepaySubmitResultBean>> call(String token) {
                        params.setToken(token);
                        params.setConversationId(conversationId);
                        return mLoanService.psnELOANBatchRepaySubmit(params);
                    }
                })
                .compose(SchedulersCompat.<List<PsnELOANBatchRepaySubmitResultBean>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnELOANBatchRepaySubmitResultBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(List<PsnELOANBatchRepaySubmitResultBean> result) {
                        mContractView.batchPrepaySubmitSuccess(result);
                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                        ErrorException errorException = new ErrorException();
                        errorException.setErrorCode(e.getErrorCode());
                        errorException.setErrorMessage(e.getErrorMessage());
                        errorException.setErrorType(e.getErrorType());
                        mContractView.batchPrepaySubmitFail(errorException);
                    }
                });

    }

    public void setConversationId(String value){
        conversationId = value;
    }

    /**
     * 获取会话,如果已经存在会话ID,则直接返回不用请求
     *
     * @return
     */
    public Observable<String> getConversation() {
        if (!StringUtils.isEmpty(conversationId))
            return Observable.just(conversationId);

        //生成ConversationId
        PSNCreatConversationParams psnCreatConversationParams = new PSNCreatConversationParams();
        return globalService.psnCreatConversation(psnCreatConversationParams);
    }

    /**
     * 获取Token
     *
     * @return
     */
    public Observable<String> getToken() {
        //根据ConversationId生成Token
        return getConversation().flatMap(new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String conversationResult) {
                conversationId = conversationResult;
                PSNGetTokenIdParams params = new PSNGetTokenIdParams();
                params.setConversationId(conversationResult);
                return globalService.psnGetTokenId(params);
            }
        });
    }
}
