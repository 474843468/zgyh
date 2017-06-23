package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.draw.presenter;

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
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANCycleLoanEApplySubmit.PsnLOANCycleLoanEApplySubmitParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANCycleLoanEApplySubmit.PsnLOANCycleLoanEApplySubmitResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANCycleLoanEApplyVerify.PsnLOANCycleLoanEApplyVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANCycleLoanEApplyVerify.PsnLOANCycleLoanEApplyVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayeeAcountCheck.PsnLOANPayeeAcountCheckParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayeeAcountCheck.PsnLOANPayeeAcountCheckResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanRepaymentPlan.PsnLoanRepaymentPlanParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanRepaymentPlan.PsnLoanRepaymentPlanResult;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.model.PrepayAccountDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.CollectionAccountCheckReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.CollectionAccountCheckRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.QueryAllChinaBankAccountRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.draw.model.LOANCycleLoanEApplySubmitReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.draw.model.LOANCycleLoanEApplyVerifyReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.draw.model.LOANCycleLoanEApplyVerifyRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.draw.model.LoanRepaymentPlanReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.draw.model.LoanRepaymentPlanRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.draw.ui.DrawContract;
import com.boc.bocsoft.mobile.common.utils.LoggerUtils;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * BII通信逻辑处理 Created by xintong on 2016/6/13.
 */
public class DrawPresenter extends RxPresenter implements DrawContract.Presenter {
	
	/**
	 * 贷款申请service
	 */
	private PsnLoanService psnLoanService;
	private GlobalService globalService;
	//用款申请界面
	private DrawContract.DrawView drawView;
	//确认界面
	private DrawContract.DrawConfirmView drawConfirmView;
	//结果界面
	private DrawContract.DrawOperatingResultsView drawOperatingResultsView;

	/**
	 * 申请贷款预交易前会话
	 */
	private String conversationId;
	private String tokenId;

	// 接口请求参数model
	private LoanRepaymentPlanReq loanRepaymentPlanReq;
	private CollectionAccountCheckReq collectionAccountCheckReq;
	private LOANCycleLoanEApplyVerifyReq loanCycleLoanEApplyVerifyReq;
	private LOANCycleLoanEApplySubmitReq loanCycleLoanEApplySubmitReq;

	/**
	 * 账户类型 获取中行所有帐户列表，请求参数
	 */
	private List<String> accountType;

	public DrawPresenter(DrawContract.DrawView drawView) {
		this.drawView = drawView;
		psnLoanService = new PsnLoanService();
		globalService = new GlobalService();
		drawView.setPresenter(this);

	}

	public DrawPresenter(DrawContract.DrawConfirmView drawConfirmView) {
		this.drawConfirmView = drawConfirmView;
		psnLoanService = new PsnLoanService();
		globalService = new GlobalService();
		drawConfirmView.setPresenter(this);

	}
	public DrawPresenter(DrawContract.DrawOperatingResultsView drawOperatingResultsView) {
		this.drawOperatingResultsView = drawOperatingResultsView;
		psnLoanService = new PsnLoanService();
		globalService = new GlobalService();
		drawOperatingResultsView.setPresenter(this);

	}


	public void setLoanRepaymentPlanReq(
			LoanRepaymentPlanReq loanRepaymentPlanReq) {
		this.loanRepaymentPlanReq = loanRepaymentPlanReq;
	}

	public void setLoanCycleLoanEApplyVerifyReq(
			LOANCycleLoanEApplyVerifyReq loanCycleLoanEApplyVerifyReq) {
		this.loanCycleLoanEApplyVerifyReq = loanCycleLoanEApplyVerifyReq;
	}

	public void setLoanCycleLoanEApplySubmitReq(
			LOANCycleLoanEApplySubmitReq loanCycleLoanEApplySubmitReq) {
		this.loanCycleLoanEApplySubmitReq = loanCycleLoanEApplySubmitReq;
	}

	public void setCollectionAccountCheckReq(
			CollectionAccountCheckReq collectionAccountCheckReq) {
		this.collectionAccountCheckReq = collectionAccountCheckReq;
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}

	public void setAccountType(List<String> accountType) {
		this.accountType = accountType;
	}

	/**
	 * 创建页面公共会话
	 */
	@Override
	public void creatConversation() {
		// 请求会话ID
		PSNCreatConversationParams conversationPreParams = new PSNCreatConversationParams();
		globalService.psnCreatConversation(conversationPreParams)
				.compose(this.<String> bindToLifecycle())
				.compose(SchedulersCompat.<String> applyIoSchedulers())
				.subscribe(new BIIBaseSubscriber<String>() {

					@Override
					public void onCompleted() {

					}

					@Override
					public void onNext(String s) {
						conversationId = s;
						drawView.obtainConversationSuccess(s);
					}

					@Override
					public void handleException(BiiResultErrorException e) {
						ErrorException errorException = new ErrorException();
						errorException.setErrorCode(e.getErrorCode());
						errorException.setErrorMessage(e.getErrorMessage());
						errorException.setErrorType(e.getErrorType());
						drawView.obtainConversationFail(errorException);
					}
				});
	}

	@Override
	public void getSecurityFactor() {
		// 获取安全因子
		PsnGetSecurityFactorParams params = new PsnGetSecurityFactorParams();
		params.setConversationId(conversationId);
		params.setServiceId("PB122");
		globalService
				.psnGetSecurityFactor(params)
				.compose(
						this
								.<PsnGetSecurityFactorResult> bindToLifecycle())
				.compose(
						SchedulersCompat
								.<PsnGetSecurityFactorResult> applyIoSchedulers())
				.subscribe(new BIIBaseSubscriber<PsnGetSecurityFactorResult>() {

					@Override
					public void onCompleted() {

					}

					@Override
					public void onNext(PsnGetSecurityFactorResult result) {
						SecurityFactorModel securityFactorModel = new SecurityFactorModel(
								result);
						drawView.obtainSecurityFactorSuccess(securityFactorModel);
					}

					@Override
					public void handleException(BiiResultErrorException e) {
						ErrorException errorException = new ErrorException();
						errorException.setErrorCode(e.getErrorCode());
						errorException.setErrorMessage(e.getErrorMessage());
						errorException.setErrorType(e.getErrorType());
						drawView.obtainSecurityFactorFail(errorException);
					}
				});
	}

	@Override
	public void getRandom() {
		// 获取随机数
		PSNGetRandomParams params = new PSNGetRandomParams();
		params.setConversationId(conversationId);
		globalService.psnGetRandom(params)
				.compose(this.<String> bindToLifecycle())
				.compose(SchedulersCompat.<String> applyIoSchedulers())
				.subscribe(new BIIBaseSubscriber<String>() {

					@Override
					public void onCompleted() {

					}

					@Override
					public void onNext(String result) {
						drawConfirmView.obtainRandomSuccess(result);
					}

					@Override
					public void handleException(BiiResultErrorException e) {
						ErrorException errorException = new ErrorException();
						errorException.setErrorCode(e.getErrorCode());
						errorException.setErrorMessage(e.getErrorMessage());
						errorException.setErrorType(e.getErrorType());
						drawConfirmView.obtainRandomFail(errorException);
					}
				});
	}

	/**
	 * PsnLoanRepaymentPlan还款计划试算 605废弃，606使用
	 */
	@Override
	public void queryLoanRepaymentPlan() {
		PsnLoanRepaymentPlanParams psnLoanRepaymentPlanParams = buildRepaymentPlan(loanRepaymentPlanReq);
		psnLoanService
				.psnLoanRepaymentPlan(psnLoanRepaymentPlanParams)
				.compose(
						this
								.<PsnLoanRepaymentPlanResult> bindToLifecycle())
				.compose(
						SchedulersCompat
								.<PsnLoanRepaymentPlanResult> applyIoSchedulers())
				.subscribe(new BIIBaseSubscriber<PsnLoanRepaymentPlanResult>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onNext(PsnLoanRepaymentPlanResult result) {
						LoanRepaymentPlanRes res = new LoanRepaymentPlanRes();
						res.setPlanLis(result.getList());
						res.setTotalAmount(result.getTotalAmount());
						res.setTotalCapita(result.getTotalCapital());
						res.setTotalInterest(result.getTotalInterest());
						// 获取还款计划试算成功调用
						drawView.obtainLoanRepaymentPlanSuccess(res);

					}

					@Override
					public void handleException(BiiResultErrorException e) {
						ErrorException errorException = new ErrorException();
						errorException.setErrorCode(e.getErrorCode());
						errorException.setErrorMessage(e.getErrorMessage());
						errorException.setErrorType(e.getErrorType());
						// 获取还款计划试算失败调用
						drawView.obtainLoanRepaymentPlanFail(errorException);
					}
				});
	}

	// PsnLOANCycleLoanEApplyVerify中银E贷用款预交易
	@Override
	public void drawApplyVerify() {
		PsnLOANCycleLoanEApplyVerifyParams psnLOANCycleLoanEApplyVerifyParams = buildDrawVerify(loanCycleLoanEApplyVerifyReq);
		LogUtils.i("cq----用款上送参数-----params----》"
				+ psnLOANCycleLoanEApplyVerifyParams.toString());
		psnLoanService
				.psnLOANCycleLoanEApplyVerify(
						psnLOANCycleLoanEApplyVerifyParams)
				.compose(
						this
								.<PsnLOANCycleLoanEApplyVerifyResult> bindToLifecycle())
				.compose(
						SchedulersCompat
								.<PsnLOANCycleLoanEApplyVerifyResult> applyIoSchedulers())
				.subscribe(
						new BIIBaseSubscriber<PsnLOANCycleLoanEApplyVerifyResult>() {

							@Override
							public void onCompleted() {

							}

							@Override
							public void onNext(
									PsnLOANCycleLoanEApplyVerifyResult result) {
								LOANCycleLoanEApplyVerifyRes res = new LOANCycleLoanEApplyVerifyRes();
								res.set_certDN(result.get_certDN());
								res.set_plainData(result.get_plainData());
								res.setFactorList(result.getFactorList());
								res.setSmcTrigerInterval(result
										.getSmcTrigerInterval());
								if (drawView != null) {
									// 中银E贷用款预交易成功调用
									drawView.drawApplyVerifySuccess(res);
								}
								if (drawConfirmView != null) {
									drawConfirmView.drawApplyVerifySuccess(res);
								}

							}

							@Override
							public void handleException(
									BiiResultErrorException e) {
								ErrorException errorException = new
										ErrorException();
								errorException.setErrorCode(e.getErrorCode());
								errorException.setErrorMessage(e.getErrorMessage());
								errorException.setErrorType(e.getErrorType());
								if (drawView != null) {
									// 中银E贷用款预交易失败调用
									drawView.drawApplyVerifyFail(errorException);
								}
								if (drawConfirmView != null) {
									drawConfirmView
											.drawApplyVerifyFail(errorException);
								}
							}
						});
	}

	// PsnLOANCycleLoanEApplySubmit 中银E贷用款提交交易
	@Override
	public void drawApplySubmit() {
		// 请求token
		PSNGetTokenIdParams psnGetTokenIdParams = new PSNGetTokenIdParams();
		psnGetTokenIdParams.setConversationId(conversationId);
		globalService
				.psnGetTokenId(psnGetTokenIdParams)
				// 将任务绑定到this上，调用将任务绑定到this.onDestroy即可取消任务
				.compose(this.<String> bindToLifecycle())
				.flatMap(
						new Func1<String, Observable<PsnLOANCycleLoanEApplySubmitResult>>() {
							@Override
							public Observable<PsnLOANCycleLoanEApplySubmitResult> call(
									String token) {
								tokenId = token;

								PsnLOANCycleLoanEApplySubmitParams psnLOANCycleLoanEApplySubmitParams = buildDrawSubmit(loanCycleLoanEApplySubmitReq);
								if (psnLOANCycleLoanEApplySubmitParams
										.getDeviceInfo() != null) {
									LoggerUtils
											.Info("ASen------>"
													+ psnLOANCycleLoanEApplySubmitParams
													.getDeviceInfo());
								} else {
									LoggerUtils.Info("ASen------>null");
								}

								return psnLoanService
										.psnLOANCycleLoanEApplySubmit(psnLOANCycleLoanEApplySubmitParams);
							}
						})
				.compose(
						SchedulersCompat
								.<PsnLOANCycleLoanEApplySubmitResult> applyIoSchedulers())
				.subscribe(
						new BIIBaseSubscriber<PsnLOANCycleLoanEApplySubmitResult>() {

							@Override
							public void onCompleted() {

							}

							@Override
							public void onNext(
									PsnLOANCycleLoanEApplySubmitResult result) {

								// 中银E贷用款提交交易成功调用
								drawConfirmView.drawApplySubmitSuccess();

							}

							@Override
							public void handleException(
									BiiResultErrorException e) {
								ErrorException errorException = new ErrorException();
								errorException.setErrorCode(e.getErrorCode());
								errorException.setErrorMessage(e
										.getErrorMessage());
								errorException.setErrorType(e.getErrorType());
								// 中银E贷用款提交交易失败调用
								drawConfirmView
										.drawApplySubmitFail(errorException);
							}
						});
	}

	// 构造还款计划试算请求参数
	private PsnLoanRepaymentPlanParams buildRepaymentPlan(
			LoanRepaymentPlanReq req) {
		PsnLoanRepaymentPlanParams params = new PsnLoanRepaymentPlanParams();
		params.setProductBigType(req.getActType());
		params.setProductCatType(req.getCat());
		params.setLoanRate(req.getLoanRate());
		params.setLoanPeriod(req.getLoanPeriod());
		params.setRepayFlag(req.getPayType());
		params.setLoanRepayAccount(req.getLoanRepayAccount());
		params.setIssueRepayDate(req.getIssueRepayDate());
		params.setAmount(req.getAmount());
		return params;
	}

	// 构造收款账户检查,请求参数
	private PsnLOANPayeeAcountCheckParams buildCollectionAccountCheck(
			QueryAllChinaBankAccountRes  req) {
		PsnLOANPayeeAcountCheckParams params = new PsnLOANPayeeAcountCheckParams();
		params.setConversationId(conversationId);
		params.setAccountId(req.getAccountId());
		params.setCurrencyCode(req.getCurrencyCode());
		return params;
	}

	// 构造中银E贷用款预交易请求参数
	private PsnLOANCycleLoanEApplyVerifyParams buildDrawVerify(
			LOANCycleLoanEApplyVerifyReq req) {
		PsnLOANCycleLoanEApplyVerifyParams params = new PsnLOANCycleLoanEApplyVerifyParams();
		params.setConversationId(conversationId);
		params.set_combinId(req.get_combinId());
		params.setQuoteType(req.getQuoteType());
		params.setLoanType(req.getLoanType());
		params.setQuoteNo(req.getQuoteNo());
		params.setLoanCycleAvaAmount(req.getLoanCycleAvaAmount());
		params.setCurrencyCode(req.getCurrencyCode());
		params.setAmount(req.getAmount());
		params.setRemark("手机银行提款;" + req.getRemark());
		params.setLoanCycleToActNum(req.getLoanCycleToActNum());
		params.setToAccountId(req.getToAccountId());
		params.setLoanPeriod(req.getLoanPeriod());
		params.setPayType(req.getPayType());
		params.setLoanRate(req.getLoanRate());
		params.setIssueRepayDate(req.getIssueRepayDate());
		params.setPayAccount(req.getPayAccount());
		params.setNextRepayDate(req.getNextRepayDate());
		return params;
	}

	// 构造中银E贷用款提交交易请求参数
	private PsnLOANCycleLoanEApplySubmitParams buildDrawSubmit(
			LOANCycleLoanEApplySubmitReq req) {
		PsnLOANCycleLoanEApplySubmitParams params = new PsnLOANCycleLoanEApplySubmitParams();
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
			LogUtils.i("cq---------->动态口令+短信验证码-------" + req.getSmc()
					+ "----------" + req.getSmc_RC() + "----------"
					+ req.getOtp() + "----------" + req.getOtp_RC());

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
		// params.setOtp_RC(req.getOtp_RC());
		// params.setDeviceInfo(req.getDeviceInfo());
		// params.setDeviceInfo_RC(req.getDeviceInfo_RC());
		// params.setDevicePrint(req.getDevicePrint());
		// cfn版本号和状态
		params.setActiv(SecurityVerity.getInstance().getCfcaVersion());
		params.setState(SecurityVerity.SECURITY_VERIFY_STATE);
		params.setToken(tokenId);
		params.setQuoteType(req.getQuoteType());
		params.setLoanType(req.getLoanType());
		params.setQuoteNo(req.getQuoteNo());
		params.setLoanCycleAvaAmount(req.getLoanCycleAvaAmount());
		params.setCurrencyCode(req.getCurrencyCode());
		params.setAmount(req.getAmount());
		params.setRemark("手机银行提款;" + req.getRemark());
		params.setLoanCycleToActNum(req.getLoanCycleToActNum());
		params.setToAccountId(req.getToAccountId());
		params.setLoanPeriod(req.getLoanPeriod());
		params.setPayType(req.getPayType());
		params.setLoanRate(req.getLoanRate());
		params.setIssueRepayDate(req.getIssueRepayDate());
		params.setPayAccount(req.getPayAccount());
		params.setNextRepayDate(req.getNextRepayDate());
		return params;
	}


	/**
	 * 获取中行所有帐户列表
	 */
	@Override
	public void queryAllChinaBankAccount() {
		PsnCommonQueryAllChinaBankAccountParams psnCommonQueryAllChinaBankAccountParams = new PsnCommonQueryAllChinaBankAccountParams();
		psnCommonQueryAllChinaBankAccountParams.setAccountType(accountType);
		globalService
				.psnCommonQueryAllChinaBankAccount(
						psnCommonQueryAllChinaBankAccountParams)
				.compose(
						this
								.<List<PsnCommonQueryAllChinaBankAccountResult>> bindToLifecycle())
				.compose(
						SchedulersCompat
								.<List<PsnCommonQueryAllChinaBankAccountResult>> applyIoSchedulers())
				.subscribe(
						new BIIBaseSubscriber<List<PsnCommonQueryAllChinaBankAccountResult>>() {
							@Override
							public void onCompleted() {
							}

							@Override
							public void onNext(
									List<PsnCommonQueryAllChinaBankAccountResult> result) {
								// 获取中行所有帐户列表，成功调用
								drawView.obtainAllChinaBankAccountSuccess(queryAllChinaBankAccountRes(result));
							}

							@Override
							public void handleException(
									BiiResultErrorException e) {
								ErrorException errorException = new ErrorException();
								errorException.setErrorCode(e.getErrorCode());
								errorException.setErrorMessage(e
										.getErrorMessage());
								errorException.setErrorType(e.getErrorType());
								// 获取中行所有帐户列表，失败调用
								drawView.obtainAllChinaBankAccountFail(errorException);
							}
						});
	}
	/**
	 * 查询账户详情-余额
	 *
	 * @param accountID
	 * @date 2016年10月10日 14:46:09
	 * @author yx
	 */
	@Override
	public void prepayCheckAccountDetail(String accountID) {
		PsnAccountQueryAccountDetailParams params = new PsnAccountQueryAccountDetailParams();
		params.setAccountId(accountID);
		psnLoanService
				.psnAccountQueryAccountDetail(params)
				.compose(
						this
								.<PsnAccountQueryAccountDetailResult> bindToLifecycle())
				.compose(
						SchedulersCompat
								.<PsnAccountQueryAccountDetailResult> applyIoSchedulers())
				.subscribe(
						new BIIBaseSubscriber<PsnAccountQueryAccountDetailResult>() {
							@Override
							public void handleException(
									BiiResultErrorException e) {
								ErrorException errorException = new ErrorException();
								errorException.setErrorCode(e.getErrorCode());
								errorException.setErrorMessage(e.getErrorMessage());
								errorException.setErrorType(e.getErrorType());
								drawOperatingResultsView
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
									if ("001".equals(bean.getCurrencyCode())) {
										accountDetaiListBean.setAvailableBalance(bean.getAvailableBalance());
									}
								}
								drawOperatingResultsView.prepayCheckAccountDetailSuccess(accountDetaiListBean);
							}
						});
	}
	// ---------------构造返回参数
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
	//==================2016年10月10日 17:13:50 yx  add
	//PsnLOANPayeeAcountCheck收款账户检查
	@Override
	public void checkCollectionAccount(QueryAllChinaBankAccountRes queryAllChinaBankAccountRes) {
		PsnLOANPayeeAcountCheckParams psnLOANPayeeAcountCheckParams
				= buildCollectionAccountCheck(queryAllChinaBankAccountRes);
		psnLoanService.psnLOANPayeeAcountCheck(psnLOANPayeeAcountCheckParams)
				.compose(this.<PsnLOANPayeeAcountCheckResult>bindToLifecycle())
				.compose(SchedulersCompat.<PsnLOANPayeeAcountCheckResult>applyIoSchedulers())
				.subscribe(new BIIBaseSubscriber<PsnLOANPayeeAcountCheckResult>() {
					@Override
					public void onCompleted() {
					}
					@Override
					public void onNext(PsnLOANPayeeAcountCheckResult psnLOANPayeeAcountCheckResult) {
						CollectionAccountCheckRes collectionAccountCheckRes
								= new CollectionAccountCheckRes();
						collectionAccountCheckRes.setCheckResult(psnLOANPayeeAcountCheckResult.getCheckResult());
						//收款账户检查，成功调用
						drawView.doCollectionAccountCheckSuccess(collectionAccountCheckRes);
					}
					@Override
					public void handleException(BiiResultErrorException e) {
						ErrorException errorException = new ErrorException();
						errorException.setErrorCode(e.getErrorCode());
						errorException.setErrorMessage(e.getErrorMessage());
						errorException.setErrorType(e.getErrorType());
						//收款账户检查，失败调用
						drawView.doCollectionAccountCheckFail(errorException);
					}
				});
	}

}
