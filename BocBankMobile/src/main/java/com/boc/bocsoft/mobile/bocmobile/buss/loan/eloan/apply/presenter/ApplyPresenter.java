package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.presenter;


import android.util.Log;

import com.boc.bocsoft.mobile.bii.bus.global.model.PSNCreatConversation.PSNCreatConversationParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetRandom.PSNGetRandomParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PSNGetTokenId.PSNGetTokenIdParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorParams;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnCityDistrictCodeQuery.PsnCityDistrictCodeQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnCityDistrictCodeQuery.PsnCityDistrictCodeQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnCreditContractQuery.PsnCreditContractQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnCreditContractQuery.PsnCreditContractQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayerAcountCheck.PsnLOANPayerAcountCheckParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanContractQuery.PsnLoanContractQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanContractQuery.PsnLoanContractQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanRegisterSumbitInterfaces.PsnLoanRegisterSumbitInterfacesParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanRegisterSumbitInterfaces.PsnLoanRegisterSumbitInterfacesResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanRegisterVerifyInterfaces.PsnLoanRegisterVerifyInterfacesParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanRegisterVerifyInterfaces.PsnLoanRegisterVerifyInterfacesResult;
import com.boc.bocsoft.mobile.bii.bus.loan.service.PsnLoanService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.model.CreditContractReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.model.CreditContractRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.model.DistrictRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.model.LoanContractReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.model.LoanContractRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.model.LoanRegisterSumbitReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.model.PreRegisterVerifyReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.model.PreRegisterVerifyRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.ui.ApplyContract;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.RepaymentAccountCheckReq;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.ui.RxPresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * BII通信逻辑处理
 * Created by xintong on 2016/6/13.
 */
public class ApplyPresenter  extends RxPresenter implements ApplyContract.Presenter{
    /**
     * 贷款申请service
     */
    private PsnLoanService psnLoanService;

    private ApplyContract.ApplyView applyView;
    private ApplyContract.ApplyConfirmView applyConfirmView;
    private ApplyContract.DistrictSelectView districtSelectView;
    private GlobalService globalService;
    /**
     * 申请贷款预交易前会话
     */
    private String conversationId;
    private String tokenId;

    private RepaymentAccountCheckReq repaymentAccountCheckReq;
    private CreditContractReq creditContractReq;
    private LoanContractReq loanContractReq;
    private PreRegisterVerifyReq preRegisterVerifyReq;
    private LoanRegisterSumbitReq loanRegisterSumbitReq;

    public ApplyPresenter(ApplyContract.ApplyView applyView) {
        this.applyView = applyView;
        psnLoanService = new PsnLoanService();
        globalService = new GlobalService();
        applyView.setPresenter(this);
    }

    public ApplyPresenter(ApplyContract.ApplyConfirmView applyConfirmView) {
        this.applyConfirmView = applyConfirmView;
        psnLoanService = new PsnLoanService();
        globalService = new GlobalService();
        applyConfirmView.setPresenter(this);
    }

    public ApplyPresenter(ApplyContract.DistrictSelectView districtSelectView) {
        this.districtSelectView = districtSelectView;
        psnLoanService = new PsnLoanService();
        globalService = new GlobalService();
        districtSelectView.setPresenter(this);
    }

    public ApplyPresenter setDistrictSelectView(ApplyContract.DistrictSelectView districtSelectView) {
        this.districtSelectView = districtSelectView;
        districtSelectView.setPresenter(this);
        return this;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public void setRepaymentAccountCheckReq(RepaymentAccountCheckReq repaymentAccountCheckReq) {
        this.repaymentAccountCheckReq = repaymentAccountCheckReq;
    }

    public void setPreRegisterVerifyReq(PreRegisterVerifyReq req) {
        this.preRegisterVerifyReq = req;
    }

    public void setCreditContractReq(CreditContractReq creditContractReq) {
        this.creditContractReq = creditContractReq;
    }

    public void setLoanContractReq(LoanContractReq loanContractReq) {
        this.loanContractReq = loanContractReq;
    }

    public void setLoanRegisterSumbitReq(LoanRegisterSumbitReq loanRegisterSumbitReq) {
        this.loanRegisterSumbitReq = loanRegisterSumbitReq;
    }


    /**
     * 创建页面公共会话
     */
    @Override
    public void creatConversation() {
        //请求会话ID
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
                        applyView.obtainConversationSuccess(s);
                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                        ErrorException errorException = new ErrorException();
                        errorException.setErrorCode(e.getErrorCode());
                        errorException.setErrorMessage(e.getErrorMessage());
                        errorException.setErrorType(e.getErrorType());
                        applyView.obtainConversationFail(errorException);
                    }
                });
    }

    @Override
    public void getSecurityFactor() {
        //获取安全因子
        PsnGetSecurityFactorParams params = new PsnGetSecurityFactorParams();
        params.setConversationId(conversationId);
        params.setServiceId("PB121");
        globalService.psnGetSecurityFactor(params)
                .compose(this.<PsnGetSecurityFactorResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnGetSecurityFactorResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnGetSecurityFactorResult>() {


                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onNext(PsnGetSecurityFactorResult result) {
                            		   SecurityFactorModel securityFactorModel
                                       = new SecurityFactorModel(result);
                               applyView.obtainSecurityFactorSuccess(securityFactorModel);
                                   
                               }

                               @Override
                               public void handleException(BiiResultErrorException e) {
                            	   ErrorException errorException = new ErrorException();
                                   errorException.setErrorCode(e.getErrorCode());
                                   errorException.setErrorMessage(e.getErrorMessage());
                                   errorException.setErrorType(e.getErrorType());
                                   applyView.obtainSecurityFactorFail(errorException);
                               }
                       });
           }

    @Override
    public void getRandom() {
        //获取随机数
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
                        applyConfirmView.obtainRandomSuccess(result);
                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                    	 ErrorException errorException = new ErrorException();
                         errorException.setErrorCode(e.getErrorCode());
                         errorException.setErrorMessage(e.getErrorMessage());
                         errorException.setErrorType(e.getErrorType());
                        applyConfirmView.obtainRandomFail(errorException);
                    }
                });
    }

    /**
     * PsnCityDistrictCodeQuery省市区联动查询
     */
    @Override
    public void queryDistrict(String zoneCode) {
        PsnCityDistrictCodeQueryParams params = new PsnCityDistrictCodeQueryParams();
        params.setZoneCode(zoneCode);

        psnLoanService.psnCityDistrictCodeQuery(params)
                .compose(this.<List<PsnCityDistrictCodeQueryResult>>bindToLifecycle())
                .compose(SchedulersCompat.<List<PsnCityDistrictCodeQueryResult>>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<List<PsnCityDistrictCodeQueryResult>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(List<PsnCityDistrictCodeQueryResult> result) {
                        //获取地区列表成功调用
                        districtSelectView.obtainDistrictSuccess(districtCodeQueryres(result));
                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                    	 ErrorException errorException = new ErrorException();
                         errorException.setErrorCode(e.getErrorCode());
                         errorException.setErrorMessage(e.getErrorMessage());
                         errorException.setErrorType(e.getErrorType());
                        //获取地区列表失败调用
                        districtSelectView.obtainDistrictFail(errorException);
                    }
                });
    }

    /**
     * 查询征信授权协议模板
     */
    @Override
    public void queryCreditContract() {
        PsnCreditContractQueryParams psnCreditContractQueryParams
                = buildCreditContract(creditContractReq);
        psnLoanService.psnCreditContractQuery(psnCreditContractQueryParams)
                .compose(this.<PsnCreditContractQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnCreditContractQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnCreditContractQueryResult>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnCreditContractQueryResult result) {
                        CreditContractRes res = new CreditContractRes();
                        res.setContractNo(result.getContractNo());
                        res.setCreditContrac(result.getCreditContrac());
                        //获取征信授权协议成功调用
                        applyView.obtainCreditContractSuccess(res);

                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                    	 ErrorException errorException = new ErrorException();
                         errorException.setErrorCode(e.getErrorCode());
                         errorException.setErrorMessage(e.getErrorMessage());
                         errorException.setErrorType(e.getErrorType());
                        //获取征信授权协议失败调用
                        applyView.obtainCreditContractFail(errorException);
                    }
                });
    }

    /**
     * 查询贷款合同模板
     */
    @Override
    public void queryLoanContract() {
            PsnLoanContractQueryParams psnLoanContractQueryParams =
                    buildLoanContract(loanContractReq);
            psnLoanService.psnLoanContractQuery(psnLoanContractQueryParams)
                .compose(this.<PsnLoanContractQueryResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnLoanContractQueryResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnLoanContractQueryResult>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnLoanContractQueryResult result) {
                        LoanContractRes res = new LoanContractRes();
                        res.setContractNo(result.getContractNo());
                        res.setLoanContract(result.getLoanContract());
                        //获取贷款合同成功调用
                        applyView.obtainLoanContractSuccess(res);

                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                    	 ErrorException errorException = new ErrorException();
                         errorException.setErrorCode(e.getErrorCode());
                         errorException.setErrorMessage(e.getErrorMessage());
                         errorException.setErrorType(e.getErrorType());
                        //获取贷款合同失败调用
                        applyView.obtainLoanContractFail(errorException);
                    }
                });
    }

    /**
     * 额度签约申请预交易
     */
    @Override
    public void preLoanRegisterVerify() {
                PsnLoanRegisterVerifyInterfacesParams params
                        = buildVerifyParams(preRegisterVerifyReq);
                psnLoanService.psnLoanRegisterVerifyInterfaces(params)
                .compose(this.<PsnLoanRegisterVerifyInterfacesResult>bindToLifecycle())
                .compose(SchedulersCompat.<PsnLoanRegisterVerifyInterfacesResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnLoanRegisterVerifyInterfacesResult>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnLoanRegisterVerifyInterfacesResult result) {
                        PreRegisterVerifyRes res = new PreRegisterVerifyRes();
                        res.set_certDN(result.get_certDN());
                        res.set_plainData(result.get_plainData());
                        res.setFactorList(result.getFactorList());
                        res.setSmcTrigerInterval(result.getSmcTrigerInterval());
                        if(applyView!=null){
                            //额度签约申请预交易成功调用
                            applyView.preLoanRegisterVerifySuccess(res);
                        }
                        if(applyConfirmView!=null){
                            applyConfirmView.preLoanRegisterVerifySuccess(res);
                        }


                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                    	 ErrorException errorException = new ErrorException();
                         errorException.setErrorCode(e.getErrorCode());
                         errorException.setErrorMessage(e.getErrorMessage());
                         errorException.setErrorType(e.getErrorType());
                        if(applyView!=null){
                            //额度签约申请预交易失败调用
                            applyView.preLoanRegisterVerifyFail(errorException);
                        }
                        if(applyConfirmView!=null){
                            //额度签约申请预交易失败调用
                            applyConfirmView.preLoanRegisterVerifyFail(errorException);
                        }
                    }
                });

    }

    /**
     * 额度签约申请提交交易
     */
    @Override
    public void loanRegisterSumbit() {
        //请求token
        PSNGetTokenIdParams  psnGetTokenIdParams = new PSNGetTokenIdParams ();
        psnGetTokenIdParams.setConversationId(conversationId);
        globalService.psnGetTokenId(psnGetTokenIdParams)
                //将任务绑定到this上，调用将任务绑定到this.onDestroy即可取消任务
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<PsnLoanRegisterSumbitInterfacesResult>>() {
                    @Override
                    public Observable<PsnLoanRegisterSumbitInterfacesResult> call(String token) {
                        tokenId = token;

                        PsnLoanRegisterSumbitInterfacesParams psnLoanRegisterSumbitInterfacesParams
                                = buildSumbitParams(loanRegisterSumbitReq);
                        LogUtils.i("---激活功能----------->"+psnLoanRegisterSumbitInterfacesParams.toString());
                        return psnLoanService.psnLoanRegisterSumbitInterfaces(psnLoanRegisterSumbitInterfacesParams);
                    }
                })
                .compose(SchedulersCompat.<PsnLoanRegisterSumbitInterfacesResult>applyIoSchedulers())
                .subscribe(new BIIBaseSubscriber<PsnLoanRegisterSumbitInterfacesResult>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(PsnLoanRegisterSumbitInterfacesResult result) {
                        //额度签约申请提交交易成功调用
                        applyConfirmView.loanRegisterSumbitSuccess();

                    }

                    @Override
                    public void handleException(BiiResultErrorException e) {
                    	 ErrorException errorException = new ErrorException();
                         errorException.setErrorCode(e.getErrorCode());
                         errorException.setErrorMessage(e.getErrorMessage());
                         errorException.setErrorType(e.getErrorType());
                        //额度签约申请提交交易失败调用
                        applyConfirmView.loanRegisterSumbitFail(errorException);
                    }
                });

    }


    //---------------构造上传参数

    //构造还款账户检查,请求参数
    private PsnLOANPayerAcountCheckParams buildRepaymentAccountCheck(RepaymentAccountCheckReq req){
        PsnLOANPayerAcountCheckParams params = new PsnLOANPayerAcountCheckParams();
        params.setConversationId(req.getConversationId());
        params.setAccountId(req.getAccountId());
        params.setCurrencyCode(req.getCurrencyCode());
        return params;
    }


    //构造查询征信授权协议模板请求参数
    private PsnCreditContractQueryParams buildCreditContract(CreditContractReq req){
        PsnCreditContractQueryParams params = new PsnCreditContractQueryParams();
        params.setConversationId(conversationId);
        params.setContractNo(req.getContractNo());
        params.seteLanguage(req.geteLanguage());
        return params;
    }
    //构造查询贷款合同模板请求参数
    private PsnLoanContractQueryParams buildLoanContract(LoanContractReq req){
        PsnLoanContractQueryParams params = new PsnLoanContractQueryParams();
        params.setConversationId(conversationId);
        params.setContractNo(req.getContractNo());
        params.seteLanguage(req.geteLanguage());
        return params;
    }

    //构造额度签约申请预交易请求参数
    private PsnLoanRegisterVerifyInterfacesParams buildVerifyParams(PreRegisterVerifyReq req){
        PsnLoanRegisterVerifyInterfacesParams params = new PsnLoanRegisterVerifyInterfacesParams();
        LogUtils.i("cq---------->params------"+ params);
        params.setConversationId(conversationId);
        params.set_combinId(req.get_combinId());
        params.setLoanPrdNo(req.getLoanPrdNo());
        params.setCustName(req.getCustName());
        params.setCerType(req.getCerType());
        params.setCerNo(req.getCerNo());
        params.setZoneCode(req.getZoneCode());
        params.setMobile(req.getMobile());
        params.setStreetInfo(req.getStreetInfo());
        params.setLinkAddress(req.getLinkAddress());
        params.setLinkRelation(req.getLinkRelation());
        params.setLinkName(req.getLinkName());
        params.setLinkMobile(req.getLinkMobile());
        params.setLoanRepayAccountId(req.getLoanRepayAccountId());
        params.setLoanRepayAccount(req.getLoanRepayAccount());
        params.setContractFormId(req.getContractFormId());
        params.setApplyQuotet(req.getApplyQuotet());
        params.setCurrencyCode(req.getCurrencyCode());
        params.setThreeContractNo(req.getThreeContractNo());
        params.seteLanguage(req.geteLanguage());
        return params;
    }

    //额度签约申请提交交易
    private PsnLoanRegisterSumbitInterfacesParams buildSumbitParams(LoanRegisterSumbitReq req){
        PsnLoanRegisterSumbitInterfacesParams params = new PsnLoanRegisterSumbitInterfacesParams();
        Log.i("presenter","conversationId1="+conversationId);
        params.setConversationId(conversationId);
        
        if("8".equals(req.getFactorId())){
        	//动态口令
        	params.setOtp(req.getOtp());
            params.setOtp_RC(req.getOtp_RC());
        	
        }else if("32".equals(req.getFactorId())){
        	//短信验证码
        	params.setSmc(req.getSmc());
            params.setSmc_RC(req.getSmc_RC());
        	
        }else if("40".equals(req.getFactorId())){
        	//动态口令+短信验证码
        	params.setSmc(req.getSmc());
            params.setSmc_RC(req.getSmc_RC());
            params.setOtp(req.getOtp());
            params.setOtp_RC(req.getOtp_RC());
        	
        }else if("96".equals(req.getFactorId())){
        	//短信验证码+硬件绑定
        	params.setSmc(req.getSmc());
            params.setSmc_RC(req.getSmc_RC());
            
            params.setDeviceInfo(req.getDeviceInfo());
            params.setDeviceInfo_RC(req.getDeviceInfo_RC());
            params.setDevicePrint(req.getDevicePrint());
        }else if("4".equals(req.getFactorId())){
        	params.set_signedData(req.get_signedData());
        }
        
//      params.setSmc(req.getSmc());
//      params.setSmc_RC(req.getSmc_RC());
//      params.setOtp(req.getOtp());
//      params.setOtp_RC(req.getOtp_RC());
        
//        params.setDeviceInfo(req.getDeviceInfo());
//        params.setDeviceInfo_RC(req.getDeviceInfo_RC());
//        params.setDevicePrint(req.getDevicePrint());
        
      //cfn版本号和状态
        params.setActiv(SecurityVerity.getInstance().getCfcaVersion());
        params.setState(SecurityVerity.SECURITY_VERIFY_STATE);

        params.setToken(tokenId);
        
        params.setLoanPrdNo(req.getLoanPrdNo());
        params.setCustName(req.getCustName());
        params.setCerType(req.getCerType());
        params.setCerNo(req.getCerNo());
        params.setZoneCode(req.getZoneCode());
        params.setMobile(req.getMobile());
        params.setStreetInfo(req.getStreetInfo());
        params.setLinkAddress(req.getLinkAddress());
        params.setLinkRelation(req.getLinkRelation());
        params.setLinkName(req.getLinkName());
        params.setLinkMobile(req.getLinkMobile());
        params.setLoanRepayAccountId(req.getLoanRepayAccountId());
        params.setLoanRepayAccount(req.getLoanRepayAccount());
        params.setContractFormId(req.getContractFormId());
        params.setApplyQuotet(req.getApplyQuotet());
        params.setCurrencyCode(req.getCurrencyCode());
        params.setThreeContractNo(req.getThreeContractNo());
        params.seteLanguage(req.geteLanguage());
        return params;
    }

//---------------构造返回参数

    private List<DistrictRes>  districtCodeQueryres(List<PsnCityDistrictCodeQueryResult> result){
        List<DistrictRes> list = new ArrayList<DistrictRes>();
        for (int i = 0; i < result.size(); i++) {
            DistrictRes res = new DistrictRes();
            res.setOrgCode(result.get(i).getOrgCode());
            res.setOrgName(result.get(i).getOrgName());
            res.setPrivCode(result.get(i).getPrivCode());
            res.setPrivName(result.get(i).getPrivName());
            res.setCityCode(result.get(i).getCityCode());
            res.setCityName(result.get(i).getCityName());
            res.setAreaCode(result.get(i).getAreaCode());
            res.setAreaName(result.get(i).getAreaName());
            res.setZoneCode(result.get(i).getZoneCode());
            list.add(res);
        }
        return list;
    }
}
