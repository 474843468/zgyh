package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.shareconversion.presenter;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryRiskMatch.PsnXpadQueryRiskMatchParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryRiskMatch.PsnXpadQueryRiskMatchResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadShareTransitionVerify.PsnXpadShareTransitionVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadShareTransitionVerify.PsnXpadShareTransitionVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.service.WealthManagementService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.PsnXpadQuantityDetail.PsnXpadQuantityDetailResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.shareconversion.util.ShareConversionModelUtil;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zn
 *
 * @description 中银理财---份额转换---Presenter
 * @date 2016/9/12
 */
public class ShareConversionPresenter extends RxPresenter
        implements ShareConversionContract.ShareConversionPresenter {
    /**
     * 中银理财-service
     */
    private WealthManagementService mWealthManagementService;
    /**
     * 贷款申请service
     */
    private PsnLoanService psnLoanService;
    /**
     * 公共service
     */
    private GlobalService globalService;
    /**
     *
     */
    private ShareConversionModelUtil mShareConversionModelUtil;
    /**
     * 中银理财-份额转换 接口处理回调方法
     */
    private ShareConversionContract.ShareConversionView mShareConversionView;

    /**
     * 构造方法 做初始化
     *
     * @param mShareConversionView
     */
    public ShareConversionPresenter(ShareConversionContract.ShareConversionView mShareConversionView) {
        globalService = new GlobalService();
        psnLoanService = new PsnLoanService();
        mWealthManagementService = new WealthManagementService();
        this.mShareConversionView = mShareConversionView;
    }

    /**
     * 初始化
     */
    private void init() {

    }

    /**
     * I42-4.40 040产品详情查询 PsnXpadProductDetailQuery
     *
     * @param productCode  产品代码
     * @param ibknum{省行联行号 String	O
     *                     返回项需展示(剩余额度、工作时间、挂单时间)，此项必输
     *                     根据PsnXpadAccountQuery接口的返回项进行上送}
     * @param productKind  产品性质
     */
    @Override
    public void getPsnXpadProductDetailQuery(String productCode, String ibknum, String productKind) {

    }

    /**
     * 查询客户风险等级与产品风险等级是否匹配  PsnXpadQueryRiskMatch
     *
     * @param serialCode  周期性产品系列编号
     * @param productCode 产品代码
     * @param digitalCode 产品数字代码
     * @param accountKey  账号缓存标识
     */
    @Override
    public void getPsnXpadQueryRiskMatch(String serialCode, String productCode, String digitalCode, String accountKey) {
        PsnXpadQueryRiskMatchParams matchParams = new PsnXpadQueryRiskMatchParams();
        matchParams.setAccountKey(accountKey);
        matchParams.setSerialCode(serialCode);
        matchParams.setProductCode(productCode);
        matchParams.setDigitalCode(digitalCode);
        mWealthManagementService.psnXpadQueryRiskMatch(matchParams)
                .compose(this.<PsnXpadQueryRiskMatchResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnXpadQueryRiskMatchResult>applyComputationSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadQueryRiskMatchResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mShareConversionView.obtainPsnXpadQueryRiskMatchFail();
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnXpadQueryRiskMatchResult psnXpadQueryRiskMatchResult) {
                        mShareConversionView.obtainPsnXpadQueryRiskMatchSuccess(
                                mShareConversionModelUtil.transverterPsnXpadQueryRiskMatchViewModel(
                                        psnXpadQueryRiskMatchResult));
                    }

                    @Override
                    public void commonHandleException(BiiResultErrorException biiResultErrorException) {
                        super.commonHandleException(biiResultErrorException);
                    }
                });
    }

    /**
     * 获取会话
     */
    @Override
    public void getPSNCreatConversation() {
        PSNCreatConversationParams mParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(mParams)
                .compose(this.<String>bindToLifecycle())
                .compose(SchedulersCompat.<String>applyComputationSchedulers())
                .subscribe(new BIIBaseSubscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(String s) {
                        mShareConversionView.obtainConversationSuccess(s);
                    }

                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mShareConversionView.obtainConversationFail();
                    }
                });
    }

    /**
     * 4.70 070份额转换预交易  PsnXpadShareTransitionVerify
     * <p/>
     * accountKey     帐号缓存标识
     * proId          产品代码
     * tranUnit       转换份额
     * token          防重标识
     * charCode       钞汇类型
     * serialNo       持仓流水号
     * conversationId 会话id
     */
    public void getPsnXpadShareTransitionVerify(final String mConversationId,
                                                final PsnXpadQuantityDetailResModel.ListEntity mListInfo,
                                                final String mContentMoney,
                                                final String productCode) {
        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
        psnGetTokenIdParams.setConversationId(mConversationId);
        // 查询TokenID
        globalService.psnGetTokenId(psnGetTokenIdParams)
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnXpadShareTransitionVerifyResult>>() {
                    @Override
                    public Observable<PsnXpadShareTransitionVerifyResult> call(String tokenID) {
                        PsnXpadShareTransitionVerifyParams mParams = mShareConversionModelUtil.
                                buildPsnXpadShareTransitionVerifyBiiParams(mListInfo, mContentMoney,productCode);
                        mParams.setConversationId(mConversationId);
                        mParams.setToken(tokenID);
                        mParams.setAccountKey(mListInfo.getBancAccountKey());
                        return mWealthManagementService.PsnXpadShareTransitionVerify(mParams);
                    }

                }).compose(SchedulersCompat.<PsnXpadShareTransitionVerifyResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnXpadShareTransitionVerifyResult>() {
                    @Override
                    public void handleException(BiiResultErrorException biiResultErrorException) {
                        mShareConversionView.obtainPsnXpadShareTransitionVerifyFail();
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnXpadShareTransitionVerifyResult doPaymentResult) {
                        mShareConversionView.obtainPsnXpadShareTransitionVerifySuccess(
                                ShareConversionModelUtil.transverterPsnXpadShareTransitionVerifyViewModel(doPaymentResult));
                    }
                });
    }


//    /**
//     * 中国银行理财产品总协议书
//     */
//    @Override
//    public void getBOCProductAgreementContract(String conversationId,CreditContractReq req) {
//        PsnCreditContractQueryParams params = new PsnCreditContractQueryParams();
//        params.setConversationId(conversationId);
//        params.setContractNo(req.getContractNo());
//        params.seteLanguage(req.geteLanguage());
//        psnLoanService.psnCreditContractQuery(params)
//                .compose(this.<PsnCreditContractQueryResult>bindToLifecycle())
//                .compose(SchedulersCompat.<PsnCreditContractQueryResult>applyIoSchedulers())
//                .subscribe(new BIIBaseSubscriber<PsnCreditContractQueryResult>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//                    @Override
//                    public void onNext(PsnCreditContractQueryResult result) {
//                        CreditContractRes res = new CreditContractRes();
//                        res.setContractNo(result.getContractNo());
//                        res.setCreditContrac(result.getCreditContrac());
//                        mShareConversionView.obtainProductAgreementSuccess(res);
//                    }
//
//                    @Override
//                    public void handleException(BiiResultErrorException e) {
//                        ErrorException errorException = new ErrorException();
//                        errorException.setErrorCode(e.getErrorCode());
//                        errorException.setErrorMessage(e.getErrorMessage());
//                        errorException.setErrorType(e.getErrorType());
//                        mShareConversionView.obtainProductAgreementFail(errorException);
//                    }
//                });
//    }
//    /**
//     *产品说明书
//     */
//    @Override
//    public void getProductDirectionContract(String conversationId,LoanContractReq req) {
//        PsnLoanContractQueryParams params = new PsnLoanContractQueryParams();
//        params.setConversationId(conversationId);
//        params.setContractNo(req.getContractNo());
//        params.seteLanguage(req.geteLanguage());
//        psnLoanService.psnLoanContractQuery(params)
//                .compose(this.<PsnLoanContractQueryResult>bindToLifecycle())
//                .compose(SchedulersCompat.<PsnLoanContractQueryResult>applyIoSchedulers())
//                .subscribe(new BIIBaseSubscriber<PsnLoanContractQueryResult>() {
//
//                    @Override
//                    public void onCompleted() {
//                    }
//                    @Override
//                    public void onNext(PsnLoanContractQueryResult result) {
//                        LoanContractRes res = new LoanContractRes();
//                        res.setContractNo(result.getContractNo());
//                        res.setLoanContract(result.getLoanContract());
//                        mShareConversionView.obtainProductDirectionSuccess(res);
//                    }
//                    @Override
//                    public void handleException(BiiResultErrorException e) {
//                        ErrorException errorException = new ErrorException();
//                        errorException.setErrorCode(e.getErrorCode());
//                        errorException.setErrorMessage(e.getErrorMessage());
//                        errorException.setErrorType(e.getErrorType());
//                        mShareConversionView.obtainProductDirectionFail(errorException);
//                    }
//                });
//    }
}

