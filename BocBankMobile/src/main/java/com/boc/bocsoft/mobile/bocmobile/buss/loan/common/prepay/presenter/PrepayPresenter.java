package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.presenter;

import android.util.Log;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetRandom.PSNGetRandomParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQueryAllChinaBankAccount.PsnCommonQueryAllChinaBankAccountParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQueryAllChinaBankAccount.PsnCommonQueryAllChinaBankAccountResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepaySubmit.PsnLOANAdvanceRepaySubmitParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepaySubmit.PsnLOANAdvanceRepaySubmitResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepayVerify.PsnLOANAdvanceRepayVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAdvanceRepayVerify.PsnLOANAdvanceRepayVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayerAcountCheck.PsnLOANPayerAcountCheckParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayerAcountCheck.PsnLOANPayerAcountCheckResult;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.model.PrepayAccountDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.model.PrepaySubmitReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.model.PrepaySubmitRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.model.PrepayVerifyReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.model.PrepayVerifyRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.ui.PrepayContract;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.QueryAllChinaBankAccountRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.RepaymentAccountCheckRes;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by xintong on 2016/6/23.
 * 非中银E贷提前还款
 */
public class PrepayPresenter extends RxPresenter implements PrepayContract.Presenter {

    /**
     * 贷款申请service
     */
    private PsnLoanService psnLoanService;

    private PrepayContract.PrepayView prepayView;

    private PrepayContract.PrepayConfirmView prepayConfirmView;

    private GlobalService globalService;
    /**
     * 申请贷款预交易前会话
     */
    private String conversationId;
    private String tokenId;

    private PrepayVerifyReq prepayVerifyReq;
    private PrepaySubmitReq prepaySubmitReq;
    private String currencyCode = "";

    // private AccountService accountService;//银行账户管理

    public PrepayPresenter(PrepayContract.PrepayView prepayView) {
        this.prepayView = prepayView;
        psnLoanService = new PsnLoanService();
        globalService = new GlobalService();
        prepayView.setPresenter(this);
    }

    public PrepayPresenter(PrepayContract.PrepayConfirmView prepayConfirmView) {
        this.prepayConfirmView = prepayConfirmView;
        psnLoanService = new PsnLoanService();
        globalService = new GlobalService();
        prepayConfirmView.setPresenter(this);
    }

    public void setPrepayVerifyReq(PrepayVerifyReq prepayVerifyReq) {
        this.prepayVerifyReq = prepayVerifyReq;
    }

    public void setPrepaySubmitReq(PrepaySubmitReq prepaySubmitReq) {
        this.prepaySubmitReq = prepaySubmitReq;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    /**
     * 设置币种
     */
    public void setcurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    /**
     * 创建页面公共会话
     */
    @Override
    public void creatConversation() {
        // 请求会话ID
        PSNCreatConversationParams conversationPreParams = new PSNCreatConversationParams();
        globalService.psnCreatConversation(conversationPreParams)
                .compose(this.<String>bindToLifecycle())
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<String>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(String s) {
                        conversationId = s;
                        prepayView.obtainConversationSuccess(s);
                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                        ErrorException errorException = new ErrorException();
                        errorException.setErrorCode(e.getErrorCode());
                        errorException.setErrorMessage(e.getErrorMessage());
                        errorException.setErrorType(e.getErrorType());
                        prepayView.obtainConversationFail(errorException);
                    }
                });
    }

    @Override
    public void getSecurityFactor() {
        // 获取安全因子
        PsnGetSecurityFactorParams params = new PsnGetSecurityFactorParams();
        params.setConversationId(conversationId);
        params.setServiceId("PB091");
        globalService
                .psnGetSecurityFactor(params)
                .compose(
                        this
                                .<PsnGetSecurityFactorResult>bindToLifecycle())
                .compose(
                        SchedulersCompat
                                .<PsnGetSecurityFactorResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnGetSecurityFactorResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnGetSecurityFactorResult result) {
                        SecurityFactorModel securityFactorModel = new SecurityFactorModel(
                                result);
                        prepayView
                                .obtainSecurityFactorSuccess(securityFactorModel);
                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                        ErrorException errorException = new ErrorException();
                        errorException.setErrorCode(e.getErrorCode());
                        errorException.setErrorMessage(e.getErrorMessage());
                        errorException.setErrorType(e.getErrorType());
                        prepayView.obtainSecurityFactorFail(errorException);
                    }
                });
    }

    @Override
    public void getRandom() {
        // 获取随机数
        PSNGetRandomParams params = new PSNGetRandomParams();
        params.setConversationId(conversationId);
        globalService.psnGetRandom(params)
                .compose(this.<String>bindToLifecycle())
                .compose(SchedulersCompat.<String>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(String result) {
                        prepayConfirmView.obtainRandomSuccess(result);
                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                        ErrorException errorException = new ErrorException();
                        errorException.setErrorCode(e.getErrorCode());
                        errorException.setErrorMessage(e.getErrorMessage());
                        errorException.setErrorType(e.getErrorType());
                        prepayConfirmView.obtainRandomFail(errorException);
                    }
                });
    }

    // PsnLOANAdvanceRepayVerify提前还款预交易
    @Override
    public void prepayVerify() {
        PsnLOANAdvanceRepayVerifyParams psnLOANAdvanceRepayVerifyParams = buildPrepayVerify(prepayVerifyReq);
        psnLoanService
                .psnLOANAdvanceRepayVerify(psnLOANAdvanceRepayVerifyParams)
                .compose(
                        this
                                .<PsnLOANAdvanceRepayVerifyResult>bindToLifecycle())
                .compose(
                        SchedulersCompat
                                .<PsnLOANAdvanceRepayVerifyResult>applyIoSchedulers())
                .subscribe(
                        new BIIBaseSubscriber<PsnLOANAdvanceRepayVerifyResult>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onNext(
                                    PsnLOANAdvanceRepayVerifyResult result) {
                                PrepayVerifyRes res = new PrepayVerifyRes();
                                res.set_certDN(result.get_certDN());
                                res.setFactorList(result.getFactorList());
                                res.set_plainData(result.get_plainData());
                                res.setSmcTrigerInterval(result
                                        .getSmcTrigerInterval());
                                res.getLoanRepayCount().setAdvanceRepayCapital(
                                        result.getLoanRepayCount()
                                                .getAdvanceRepayCapital());
                                res.getLoanRepayCount()
                                        .setAdvanceRepayInterest(
                                                result.getLoanRepayCount()
                                                        .getAdvanceRepayInterest());
                                res.getLoanRepayCount()
                                        .setRepayAmountInAdvance(
                                                result.getLoanRepayCount()
                                                        .getRepayAmountInAdvance());
                                res.getLoanRepayCount().setEveryTermAmount(
                                        result.getLoanRepayCount()
                                                .getEveryTermAmount());
                                res.getLoanRepayCount()
                                        .setCharges(
                                                result.getLoanRepayCount()
                                                        .getCharges());
                                res.getLoanRepayCount()
                                        .setRemainIssueforAdvance(
                                                result.getLoanRepayCount()
                                                        .getRemainIssueforAdvance());

                                if (prepayView != null) {
                                    // 提前还款预交易成功调用
                                    prepayView.prepayVerifySuccess(res);
                                }
                                if (prepayConfirmView != null) {
                                    prepayConfirmView.prepayVerifySuccess(res);
                                }
                            }

                            @Override
                            public void handleException(
                                    BiiResultErrorException e) {
                                ErrorException errorException = new ErrorException();
                                errorException.setErrorCode(e.getErrorCode());
                                errorException.setErrorMessage(e.getErrorMessage());
                                errorException.setErrorType(e.getErrorType());
                                if (prepayView != null) {
                                    // 提前还款预交易失败调用
                                    prepayView.prepayVerifyFail(errorException);
                                }
                                if (prepayConfirmView != null) {
                                    prepayConfirmView
                                            .prepayVerifyFail(errorException);
                                }
                            }
                        });
    }

    // PsnLOANAdvanceRepaySubmit提前还款提交交易
    @Override
    public void prepaySubmit() {
        // 请求token
        PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
        psnGetTokenIdParams.setConversationId(conversationId);
        globalService
                .psnGetTokenId(psnGetTokenIdParams)
                // 将任务绑定到this上，调用将任务绑定到this.onDestroy即可取消任务
                .compose(this.<String>bindToLifecycle())
                .flatMap(
                        new Func1<String, Observable<PsnLOANAdvanceRepaySubmitResult>>() {
                            @Override
                            public Observable<PsnLOANAdvanceRepaySubmitResult> call(
                                    String token) {
                                tokenId = token;
                                PsnLOANAdvanceRepaySubmitParams psnLOANAdvanceRepaySubmitParams = buildPrepaySubmit(prepaySubmitReq);
                                return psnLoanService
                                        .psnLOANAdvanceRepaySubmit(psnLOANAdvanceRepaySubmitParams);
                            }
                        })
                .compose(
                        SchedulersCompat
                                .<PsnLOANAdvanceRepaySubmitResult>applyIoSchedulers())
                .subscribe(
                        new BIIBaseSubscriber<PsnLOANAdvanceRepaySubmitResult>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onNext(
                                    PsnLOANAdvanceRepaySubmitResult result) {
                                PrepaySubmitRes res = new PrepaySubmitRes();
                                res.setTransNo(result.getTransNo());
                                res.setLoanType(result.getLoanType());
                                res.setLoanAccount(result.getLoanAccount());
                                res.setCurrency(result.getCurrency());
                                res.setLoanAmount(result.getLoanAmount());
                                res.setLoanPeriod(result.getLoanPeriod());
                                res.setLoanToDate(result.getLoanToDate());
                                res.setRepayAmount(result.getRepayAmount());
                                res.setFromAccount(result.getFromAccount());
                                res.setLoanPeriodUnit(result
                                        .getLoanPeriodUnit());
                                res.setCharges(result.getCharges());
                                res.setAfterRepayissueAmount(result
                                        .getAfterRepayissueAmount());
                                res.setAfterRepayRemainAmount(result
                                        .getAfterRepayRemainAmount());

                                // 提前还款提交交易成功调用
                                prepayConfirmView.prepaySubmitSuccess(res);
                            }

                            @Override
                            public void handleException(
                                    BiiResultErrorException e) {
                                ErrorException errorException = new ErrorException();
                                errorException.setErrorCode(e.getErrorCode());
                                errorException.setErrorMessage(e.getErrorMessage());
                                errorException.setErrorType(e.getErrorType());
                                // 提前还款提交交易失败调用
                                prepayConfirmView
                                        .prepaySubmitFail(errorException);
                            }
                        });
    }

    /**
     * 获取中行所有帐户列表
     *
     * @param mListDataAccountType 账户类型
     */
    @Override
    public void queryAllChinaBankAccount(List<String> mListDataAccountType) {
        PsnCommonQueryAllChinaBankAccountParams psnCommonQueryAllChinaBankAccountParams = new PsnCommonQueryAllChinaBankAccountParams();
        psnCommonQueryAllChinaBankAccountParams
                .setAccountType(mListDataAccountType);
        globalService
                .psnCommonQueryAllChinaBankAccount(
                        psnCommonQueryAllChinaBankAccountParams)
                .compose(
                        this
                                .<List<PsnCommonQueryAllChinaBankAccountResult>>bindToLifecycle())
                .compose(
                        SchedulersCompat
                                .<List<PsnCommonQueryAllChinaBankAccountResult>>applyIoSchedulers())
                .subscribe(
                        new BIIBaseSubscriber<List<PsnCommonQueryAllChinaBankAccountResult>>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onNext(
                                    List<PsnCommonQueryAllChinaBankAccountResult> result) {
                                // 获取中行所有帐户列表，成功调用
                                prepayView
                                        .obtainAllChinaBankAccountSuccess(queryAllChinaBankAccountRes(result));
                            }

                            @Override
                            public void handleException(
                                    BiiResultErrorException e) {
                                ErrorException errorException = new ErrorException();
                                errorException.setErrorCode(e.getErrorCode());
                                errorException.setErrorMessage(e.getErrorMessage());
                                errorException.setErrorType(e.getErrorType());
                                // 获取中行所有帐户列表，失败调用
                                prepayView
                                        .obtainAllChinaBankAccountFail(errorException);
                            }
                        });
    }


    // ---------------构造返回参数

    /**
     * 账户列表 119 返回结果数据
     *
     * @param result
     * @return
     */
    private List<QueryAllChinaBankAccountRes> queryAllChinaBankAccountRes(
            List<PsnCommonQueryAllChinaBankAccountResult> result) {
        List<QueryAllChinaBankAccountRes> list = new ArrayList<QueryAllChinaBankAccountRes>();
        for (int i = 0; i < result.size(); i++) {
            QueryAllChinaBankAccountRes queryAllChinaBankAccountRes = new QueryAllChinaBankAccountRes();
            queryAllChinaBankAccountRes.setAccountId(result.get(i)
                    .getAccountId());
            queryAllChinaBankAccountRes.setAccountName(result.get(i)
                    .getAccountName());
            queryAllChinaBankAccountRes.setIsECashAccount(result.get(i)
                    .getIsECashAccount());
            queryAllChinaBankAccountRes.setIsMedicalAccount(result.get(i)
                    .getIsMedicalAccount());
            queryAllChinaBankAccountRes.setAccountNumber(result.get(i)
                    .getAccountNumber());
            queryAllChinaBankAccountRes.setAccountIbkNum(result.get(i)
                    .getAccountIbkNum());
            queryAllChinaBankAccountRes.setAccountType(result.get(i)
                    .getAccountType());
            queryAllChinaBankAccountRes
                    .setBranchId(result.get(i).getBranchId());
            queryAllChinaBankAccountRes
                    .setNickName(result.get(i).getNickName());
            queryAllChinaBankAccountRes.setAccountStatus(result.get(i)
                    .getAccountStatus());
            queryAllChinaBankAccountRes.setCustomerId(result.get(i)
                    .getCustomerId());
            queryAllChinaBankAccountRes.setCurrencyCode(result.get(i)
                    .getCurrencyCode());
            queryAllChinaBankAccountRes.setCurrencyCode2(result.get(i)
                    .getCurrencyCode2());
            queryAllChinaBankAccountRes.setBranchName(result.get(i)
                    .getBranchName());
            queryAllChinaBankAccountRes.setCardDescription(result.get(i)
                    .getCardDescription());
            queryAllChinaBankAccountRes.setHasOldAccountFlag(result.get(i)
                    .getHasOldAccountFlag());
            queryAllChinaBankAccountRes.setCardDescriptionCode(result.get(i)
                    .getCardDescriptionCode());
            queryAllChinaBankAccountRes.setVerifyFactor(result.get(i)
                    .getVerifyFactor());
            queryAllChinaBankAccountRes.setEcard(result.get(i).getEcard());
            list.add(queryAllChinaBankAccountRes);
        }

        return list;
    }

    /**
     * 查询账户详情
     *
     * @param accountID
     */
    @Override
    public void prepayCheckAccountDetail(String accountID) {
        PsnAccountQueryAccountDetailParams params = new PsnAccountQueryAccountDetailParams();
        params.setAccountId(accountID);
        psnLoanService
                .psnAccountQueryAccountDetail(params)
                .compose(
                        this
                                .<PsnAccountQueryAccountDetailResult>bindToLifecycle())
                .compose(
                        SchedulersCompat
                                .<PsnAccountQueryAccountDetailResult>applyIoSchedulers())
                .subscribe(
                        new BIIBaseSubscriber<PsnAccountQueryAccountDetailResult>() {
                            @Override
                            public void handleException(
                                    BiiResultErrorException e) {
                                ErrorException errorException = new ErrorException();
                                errorException.setErrorCode(e.getErrorCode());
                                errorException.setErrorMessage(e.getErrorMessage());
                                errorException.setErrorType(e.getErrorType());
                                prepayView
                                        .prepayCheckAccountDetailFail(errorException);
                            }

                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onNext(
                                    PsnAccountQueryAccountDetailResult result) {
                                PrepayAccountDetailModel.AccountDetaiListBean accountDetaiListBean = new PrepayAccountDetailModel.AccountDetaiListBean();
                                for (PsnAccountQueryAccountDetailResult.AccountDetaiListBean bean : result
                                        .getAccountDetaiList()) {
//									if ("001".equals(bean.getCurrencyCode())) {
//										accountDetaiListBean.setAvailableBalance(bean.getAvailableBalance());
//									}
                                    if (currencyCode.equalsIgnoreCase(bean.getCurrencyCode())) {
                                        LogUtil.d("yx----------默认提前还款做匹配");
                                        //币种
                                        accountDetaiListBean.setCurrencyCode(bean.getCurrencyCode());
                                        //钞汇标识
                                        accountDetaiListBean.setCashRemit(bean.getCashRemit());
                                        //当前余额
                                        accountDetaiListBean.setBookBalance(bean.getBookBalance());
                                        //可用余额
                                        accountDetaiListBean.setAvailableBalance(bean.getAvailableBalance());
                                        //存折册号
                                        accountDetaiListBean.setVolumeNumber(bean.getVolumeNumber());
                                        //存单类型
                                        accountDetaiListBean.setType(bean.getType());
                                        //利率
                                        accountDetaiListBean.setInterestRate(bean.getInterestRate());
                                        //子账户状态
                                        accountDetaiListBean.setStatus(bean.getStatus());
                                        //月存、支金额
                                        accountDetaiListBean.setMonthBalance(bean.getMonthBalance());
                                        //存单号
                                        accountDetaiListBean.setCdNumber(bean.getCdNumber());
                                        //存期
                                        accountDetaiListBean.setCdPeriod(bean.getCdPeriod());
                                        //开户日期
                                        accountDetaiListBean.setOpenDate(bean.getOpenDate());
                                        //起息日
                                        accountDetaiListBean.setInterestStartsDate(bean.getInterestStartsDate());
                                        //到期日
                                        accountDetaiListBean.setInterestEndDate(bean.getInterestEndDate());
                                        //结清日期
                                        accountDetaiListBean.setSettlementDate(bean.getSettlementDate());
                                        //自动转存标识
                                        accountDetaiListBean.setConvertType(bean.getConvertType());
                                        //凭证号码
                                        accountDetaiListBean.setPingNo(bean.getPingNo());
                                        //
                                        accountDetaiListBean.setHoldAmount(bean.getHoldAmount());
                                        //约定转存状态
                                        accountDetaiListBean.setAppointStatus(bean.getAppointStatus());
                                    }
                                }
                                prepayView.prepayCheckAccountDetailSuccess(accountDetaiListBean);
                            }
                        });
    }

    // 构造提前还款预交易，请求参数
    private PsnLOANAdvanceRepayVerifyParams buildPrepayVerify(
            PrepayVerifyReq req) {
        PsnLOANAdvanceRepayVerifyParams params = new PsnLOANAdvanceRepayVerifyParams();
        Log.i("PAOPAO", "提前还款预交易----------params---->" + params.toString());
        params.setConversationId(conversationId);
        params.setLoanType(req.getLoanType());
        params.setLoanAccount(req.getLoanAccount());
        params.setCurrency(req.getCurrency());
        params.setRepayAmount(req.getRepayAmount());
        params.setFromAccountId(req.getFromAccountId());
        params.setAccountNumber(req.getAccountNumber());
        params.set_combinId(req.get_combinId());
        return params;
    }

    // 构造提前还款提交交易，请求参数
    private PsnLOANAdvanceRepaySubmitParams buildPrepaySubmit(
            PrepaySubmitReq req) {
        PsnLOANAdvanceRepaySubmitParams params = new PsnLOANAdvanceRepaySubmitParams();
        LogUtils.i("cq--------------提前还款-----req------->" + req.toString());
        params.setConversationId(conversationId);

        if ("8".equals(req.getFactorId())) {
            // 动态口令
            params.setOtp(req.getOtp());
            params.setOtp_RC(req.getOtp_RC());

        } else if ("32".equals(req.getFactorId())) {
            // 短信验证码
            params.setSmc(req.getSmc());
            params.setSmc_RC(req.getSmc_RC());

        } else if ("40".equals(req.getFactorId())) {
            // 动态口令+短信验证码
            params.setSmc(req.getSmc());
            params.setSmc_RC(req.getSmc_RC());
            params.setOtp(req.getOtp());
            params.setOtp_RC(req.getOtp_RC());

        } else if ("96".equals(req.getFactorId())) {
            // 短信验证码+硬件绑定
            params.setSmc(req.getSmc());
            params.setSmc_RC(req.getSmc_RC());

            params.setDeviceInfo(req.getDeviceInfo());
            params.setDeviceInfo_RC(req.getDeviceInfo_RC());
            params.setDevicePrint(req.getDevicePrint());
        } else if ("4".equals(req.getFactorId())) {
            params.set_signedData(req.get_signedData());
        }

        // params.setSmc(req.getSmc());
        // params.setSmc_RC(req.getSmc_RC());
        // params.setOtp(req.getOtp());
        // params.setSmc_RC(req.getSmc_RC());

        // params.setDeviceInfo(req.getDeviceInfo());
        // params.setDeviceInfo_RC(req.getDeviceInfo_RC());
        // params.setDevicePrint(req.getDevicePrint());

        // cfn版本号和状态
        params.setActiv(SecurityVerity.getInstance().getCfcaVersion());
        params.setState(SecurityVerity.SECURITY_VERIFY_STATE);

        params.setToken(tokenId);

        params.setLoanType(req.getLoanType());
        params.setLoanAccount(req.getLoanAccount());
        params.setCurrency(req.getCurrency());
        params.setLoanAmount(req.getLoanAmount());
        params.setLoanPeriod(req.getLoanPeriod());
        params.setLoanToDate(req.getLoanToDate());
        params.setAdvanceRepayInterest(req.getAdvanceRepayInterest());
        params.setAdvanceRepayCapital(req.getAdvanceRepayCapital());
        params.setRepayAmount(req.getRepayAmount());
        params.setFromAccountId(req.getFromAccountId());
        params.setAccountNumber(req.getAccountNumber());
        params.setLoanPeriodUnit(req.getLoanPeriodUnit());
        params.setRemainCapital(req.getRemainCapital());
        params.setThisIssueRepayInterest(req.getThisIssueRepayInterest());
        params.setRemainIssue(req.getRemainIssue());
        params.setCharges(req.getCharges());
        params.setThisIssueRepayDate(req.getThisIssueRepayDate());
        params.setThisIssueRepayAmount(req.getThisIssueRepayAmount());
        params.setAfterRepayissueAmount(req.getAfterRepayissueAmount());
        params.setInterestType(req.getInterestType());
        params.setAfterRepayRemainAmount(req.getAfterRepayRemainAmount());
        params.setAfterRepayissues(req.getAfterRepayissues());
        params.setOnlineFlag(req.getOnlineFlag());
        params.setCycleType(req.getCycleType());
        return params;
    }

    //=====================2016年10月10日 20:19:05  闫勋 add
//PsnLOANPayerAcountCheck还款账户检查
    @Override
    public void checkRepaymentAccount(QueryAllChinaBankAccountRes queryAllChinaBankAccountRes) {
        PsnLOANPayerAcountCheckParams psnLOANPayerAcountCheckParams
                = buildRepaymentAccountCheck(queryAllChinaBankAccountRes);
        psnLoanService.psnLOANPayerAcountCheck(psnLOANPayerAcountCheckParams)
                .compose(this.<PsnLOANPayerAcountCheckResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnLOANPayerAcountCheckResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnLOANPayerAcountCheckResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onNext(PsnLOANPayerAcountCheckResult psnLOANPayerAcountCheckResult) {
                        RepaymentAccountCheckRes repaymentAccountCheckRes
                                = new RepaymentAccountCheckRes();
                        repaymentAccountCheckRes.setCheckResult(psnLOANPayerAcountCheckResult.getCheckResult());
                        //还款账户检查，成功调用
                        prepayView.doRepaymentAccountCheckSuccess(repaymentAccountCheckRes);
                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                        ErrorException errorException = new ErrorException();
                        errorException.setErrorCode(e.getErrorCode());
                        errorException.setErrorMessage(e.getErrorMessage());
                        errorException.setErrorType(e.getErrorType());
                        //还款账户检查，失败调用
                        prepayView.doRepaymentAccountCheckFail(errorException);
                    }
                });
    }

    //构造还款账户检查,请求参数
    private PsnLOANPayerAcountCheckParams buildRepaymentAccountCheck(QueryAllChinaBankAccountRes req) {
        PsnLOANPayerAcountCheckParams params = new PsnLOANPayerAcountCheckParams();
        params.setConversationId(conversationId);
        params.setAccountId(req.getAccountId());
        params.setCurrencyCode(req.getCurrencyCode());
        return params;
    }
}
