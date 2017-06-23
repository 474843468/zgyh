package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.ui;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DeviceInfoUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRowButton;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.model.PrepaySubmitReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.model.PrepaySubmitRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.model.PrepayVerifyReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.model.PrepayVerifyRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.presenter.PrepayPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanAccountListModel;
import com.boc.bocsoft.mobile.common.utils.ButtonClickLock;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

/**
 * 贷款管理-非中银E贷-提前还款确认信息Fragment
 * Created by xintong on 2016/6/7.
 */
public class PrepayConfirmInfoFragment extends MvpBussFragment<PrepayPresenter> implements PrepayContract.PrepayConfirmView {
    private static final String TAG = "PrepaytConfirmInfoFragm";
    //页面名称
    private static String PAGENAME;
    private LinearLayout content;
    //详情内容组件
    private DetailContentView detailContentView;
  //获取安全因子成功后返回的默认安全因子组合id
    private String _combinId,combinId_name;
    //页面根视图
    private View mRoot;
  //安全认证组件
    private DetailTableRowButton bocBtntv;
    //确认按钮
    private Button confirm;
    //会话id
    private String conversationId;
//    private PrepayPresenter prepayPresenter;
    private PrepayVerifyReq prepayVerifyReq;
  //预交易后，再次确认安全因子  为true调用预交易接口
    private boolean available;
    private EloanAccountListModel.PsnLOANListEQueryBean mPrepayDetailModel;
    private PrepayVerifyRes prepayVerifyRes;
    //安全组件
    private SecurityVerity securityVerity;
    //结果页
    private PrepayResultFragment resultsFragment;
    //随机数
    private String random;
    private PrepaySubmitReq req;
    //头部组件
    private DetailTableHead detailTableHead;

    /** 提交按钮 防暴力点击 */
	private String click_more = "click_more";

    //确认界面公共组件
    private ConfirmInfoView confirmInfoView;
    //还款总额 ---》本金+利息+手续费
    private String totalMoney = "";

    //还款本金
    private String mAdvanceRepayCapital = "";
    //还款利息
    private String mAdvanceRepayInterest = "";
    //还款总额是否需要添加 手续费
    private boolean isaddCharges = false;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRoot = mInflater.inflate(R.layout.fragment_eloan_prepay_confirminfo, null);
        return mRoot;
    }

    @Override
    protected void titleLeftIconClick() {
        super.titleLeftIconClick();
    }

    @Override
    protected void titleRightIconClick() {
        super.titleRightIconClick();
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_eloan_prepayconfirmation_pagename);
    }

    public void setPrepayVerifyRes(PrepayVerifyRes prepayVerifyRes) {
        this.prepayVerifyRes = prepayVerifyRes;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }

    public void set_combinId_Name(String combinId_name) {
        this.combinId_name = combinId_name;
    }

    @Override
    public void beforeInitView() {

    }

    public void setmPrepayDetailModel(EloanAccountListModel.PsnLOANListEQueryBean mPrepayDetailModel) {
        this.mPrepayDetailModel = mPrepayDetailModel;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    public void initView() {
//        prepayPresenter = new PrepayPresenter(this);
        securityVerity = SecurityVerity.getInstance(getActivity());
        getPresenter().setConversationId(conversationId);
        securityVerity.setConversationId(conversationId);

        //初始化组件
        content = (LinearLayout) mRoot.findViewById(R.id.content);

//        detailTableHead = new DetailTableHead(mContext);
//        detailContentView = new DetailContentView(mContext);
//        bocBtntv = new DetailTableRowButton(mContext);
//        confirm = (Button) mRoot.findViewById(R.id.confirm);

        confirmInfoView=new ConfirmInfoView(mContext);

        resultsFragment=new PrepayResultFragment();

    }

    public void setPrepayVerifyReq(PrepayVerifyReq req) {
        this.prepayVerifyReq = req;
    }

    @Override
    public void initData() {
    	ButtonClickLock.getLock(click_more).lockDuration = EloanConst.CLICK_MORE_TIME;
        LinkedHashMap<String,String> map=new LinkedHashMap<>();
        //定义页面名称
        PAGENAME = getString(R.string.boc_eloan_prepayconfirmation_pagename);

        if(null!=prepayVerifyReq && null!=prepayVerifyRes){

            if("R".equalsIgnoreCase(mPrepayDetailModel.getCycleType()+"")&&"1".equalsIgnoreCase(mPrepayDetailModel.getOnlineFlag()+"")){
                isaddCharges = false;
                confirmInfoView.setHeadValue("还款总额"+"（"+PublicCodeUtils.getCurrencyWithLetter(mContext,prepayVerifyReq.getCurrency())+"）",
                        MoneyUtils.transMoneyFormat(prepayVerifyReq.getRepayAmount().toString(), prepayVerifyReq.getCurrency()),false);
            }else{
                isaddCharges = true;
                BigDecimal b1 = new BigDecimal(prepayVerifyReq.getRepayAmount().toString());//本息合计
                BigDecimal b2 = new BigDecimal(prepayVerifyRes.getLoanRepayCount().getCharges().toString()); //手续费
                b2 = b2.setScale(2, BigDecimal.ROUND_HALF_UP);
                totalMoney = (b2.add(b1)).toString();
                //为组建添加默认数据
//            confirmInfoView.setHeadValue(getString(R.string.boc_eloan_prepayAmount)+"（人民币元）",
//                    MoneyUtils.transMoneyFormat(totalMoney, "001"),false);
                confirmInfoView.setHeadValue("还款总额"+"（"+PublicCodeUtils.getCurrencyWithLetter(mContext,prepayVerifyReq.getCurrency())+"）",
                        MoneyUtils.transMoneyFormat(totalMoney, prepayVerifyReq.getCurrency()),false);
//                confirmInfoView.setHeadValue("还款总额"+"（人民币元）",
//                        MoneyUtils.transMoneyFormat(totalMoney, "001"),false);
            }

            //确认页显示数据
            //提前还款本金
            mAdvanceRepayCapital = MoneyUtils.transMoneyFormat(prepayVerifyRes.getLoanRepayCount()
                    .getAdvanceRepayCapital().toString(), prepayVerifyReq.getCurrency());
            map.put("还款本金",mAdvanceRepayCapital );
            //提前还款利息
            mAdvanceRepayInterest =  MoneyUtils.transMoneyFormat(prepayVerifyRes.getLoanRepayCount()
                    .getAdvanceRepayInterest().toString(), prepayVerifyReq.getCurrency());
            map.put("还款利息",mAdvanceRepayInterest );
            //提前还款手续费
            map.put("手续费" ,
                    MoneyUtils.transMoneyFormat(prepayVerifyRes.getLoanRepayCount().getCharges().toString(), prepayVerifyReq.getCurrency()));
            if("R".equalsIgnoreCase(mPrepayDetailModel.getCycleType()+"")&&"1".equalsIgnoreCase(mPrepayDetailModel.getOnlineFlag()+"")){
                if("027".equalsIgnoreCase(prepayVerifyReq.getCurrency())||"JPY".equalsIgnoreCase(prepayVerifyReq.getCurrency())){
                    map.put("优惠后手续费" ,  "0");
                }else{
                    map.put("优惠后手续费" ,  "0.00");
                }
            }
            //还款账户
            map.put(getString(R.string.boc_eloan_prepay_repaymentAccount) ,
                    NumberUtils.formatCardNumberStrong(prepayVerifyReq.getAccountNumber()));
            //还款后剩余金额
            map.put(getString(R.string.boc_eloan_prepayAccount) ,
                    MoneyUtils.transMoneyFormat( prepayVerifyRes.getLoanRepayCount()
                            .getRepayAmountInAdvance().toString(), prepayVerifyReq.getCurrency()));

            confirmInfoView.addData(map,true);
            // 设置默认的安全因子名称
            confirmInfoView.updateSecurity(combinId_name);
            content.addView(confirmInfoView);
        }
    }

    @Override
    public void setListener() {

        confirmInfoView.setListener(new ConfirmInfoView.OnClickListener() {
            @Override
            public void onClickConfirm() {
                if (!ButtonClickLock.isCanClick(click_more)) {
                    return;
                }
                if (available) {
                    showLoadingDialog();
                    //获取随机数
                    getPresenter().getRandom();
                }
            }

            @Override
            public void onClickChange() {
                if (!ButtonClickLock.isCanClick(click_more)) {
                    return;
                }
                securityVerity.selectSecurityType();
            }
        });

        //安全组件监听
        securityVerity.setSecurityVerifyListener(new SecurityVerity.VerifyCodeResultListener() {
            //选择安全组件
            @Override
            public void onSecurityTypeSelected(CombinListBean bean) {

            	if(!StringUtils.isEmpty(_combinId)){
            		if(!_combinId.equalsIgnoreCase(bean.getId())){
            			 _combinId = bean.getId();
                        combinId_name=bean.getName();
                        // 设置更改的安全因子名称
                        confirmInfoView.updateSecurity(combinId_name);

                         prepayVerifyReq.set_combinId(_combinId);
                         //调用预交易接口
                        getPresenter().setPrepayVerifyReq(prepayVerifyReq);
                         showLoadingDialog();
                        getPresenter().prepayVerify();
            		}
            	}             
            }

            //安全组件点击确定触发
            @Override
            public void onEncryptDataReturned(String factorId, String[] randomNums, String[] encryptPasswords) {
                showLoadingDialog();
                buildSubmitReq(factorId, randomNums, encryptPasswords);
            }

            @Override
            public void onSignedReturn(String signRetData) {
            	showLoadingDialog();
            	buildSubmitReq(signRetData);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getPresenter().unsubscribe();
    }

    @Override
    public void obtainRandomSuccess(String random) {
        this.random=random;
        Log.i(TAG, "获取随机数成功！");
        closeProgressDialog();
        //弹出选择的安全认证工具
        securityVerity.showSecurityDialog(random);
    }

    @Override
    public void obtainRandomFail(ErrorException e) {
        Log.i(TAG, "获取随机数失败！");
        closeProgressDialog();
    }

    @Override
    public void prepayVerifySuccess(PrepayVerifyRes result) {
        Log.i(TAG, "-------->预交易接口调用成功！");
        closeProgressDialog();
        available = securityVerity.confirmFactor(result.getFactorList());
        
      //音频key加密
      EShieldVerify.getInstance(getActivity()).setmPlainData(result.get_plainData());  
        
//        if (available) {
////        	 showLoadingDialog();
//            //获取随机数
//            prepayPresenter.getRandom();
//            //音频key加密
//            EShieldVerify.getInstance(getActivity()).setmPlainData(result.get_plainData());           
//        } else {
//            //TODO 获取确定的安全因子失败
//        }
    }

    @Override
    public void prepayVerifyFail(ErrorException e) {
        Log.i(TAG, "-------->预交易接口调用失败！");
        closeProgressDialog();
    }

    @Override
    public void prepaySubmitSuccess(PrepaySubmitRes result) {
        Log.i(TAG, "提前还款交易成功！");
        closeProgressDialog();
        resultsFragment. setRepayCapitalAndInterest(mAdvanceRepayCapital,mAdvanceRepayInterest,isaddCharges);
        resultsFragment.setStatus(OperationResultHead.Status.SUCCESS);
        Bundle bundle=new Bundle();
        bundle.putSerializable(EloanConst.ELON_PREPAY_COMMIT,result);
        resultsFragment.setArguments(bundle);
        start(resultsFragment);

    }

    @Override
    public void prepaySubmitFail(ErrorException e) {
        Log.i(TAG, "提前还款交易失败！");
        closeProgressDialog();
//        resultsFragment.setStatus(OperationResultHead.Status.FAIL);
//        start(resultsFragment);

    }

    @Override
    public void setPresenter(PrepayContract.Presenter presenter) {

    }

    private void buildSubmitReq(String factorId, String[] randomNums, String[] encryptPasswords){
    	
         req = new PrepaySubmitReq();   
         
        if ("8".equals(factorId)) {
//        	if(check(factorId,encryptPasswords)){
        		//动态口令
                req.setOtp(encryptPasswords[0]);
                req.setOtp_RC(randomNums[0]);
                req.setFactorId(factorId);
                buildSubmit();
//        	}
        } else if ("32".equals(factorId)) {
//        	if(check(factorId,encryptPasswords)){
        		 //短信验证码
                req.setSmc(encryptPasswords[0]);
                req.setSmc_RC(randomNums[0]);
                req.setFactorId(factorId);
                buildSubmit();
//        	}
        } else if ("40".equals(factorId)) {
//        	if(check(factorId,encryptPasswords)){
        		  //动态口令+短信验证码
                req.setOtp(encryptPasswords[0]);
                req.setOtp_RC(randomNums[0]);
                req.setSmc(encryptPasswords[1]);
                req.setSmc_RC(randomNums[1]);
                req.setFactorId(factorId);
                buildSubmit();
//        	}
        } else if ("96".equals(factorId)) {
//        	if(check(factorId,encryptPasswords)){
        		//短信验证码
                req.setSmc(encryptPasswords[0]);
                req.setSmc_RC(randomNums[0]);
                req.setFactorId(factorId);
            DeviceInfoModel info = DeviceInfoUtils.getDeviceInfo(getActivity(), random);
            req.setDeviceInfo(info.getDeviceInfo());
            req.setDeviceInfo_RC(info.getDeviceInfo_RC());
            req.setDevicePrint(DeviceInfoUtils.getDevicePrint(getActivity()));
//                DeviceInfoModel infoModel = DeviceInfoUtils.getDeviceInfo(getActivity()
//                        ,random, SpUtils.getSpString(getContext(),EloanConst.OPETATOR_ID,null));
//
//                req.setDeviceInfo(infoModel.getDeviceInfo());
//                req.setDeviceInfo_RC(infoModel.getDeviceInfo_RC());
//                req.setDevicePrint(DeviceInfoUtils.getDevicePrint(getActivity()));
                buildSubmit();
//        	}
        }else if ("4".equals(factorId)) {
            //TODO 音频key
//            req.set_signedData(req.get_signedData());
        }  
    }
    
    private void buildSubmitReq(String signRetData){
    	//TODO 音频key
        req = new PrepaySubmitReq();
        req.set_signedData(signRetData);
        req.setFactorId("4");
        buildSubmit();
    }
    
    private void buildSubmit(){
    	
    	//cfn版本号和状态
    	req.setActiv(SecurityVerity.getInstance().getCfcaVersion());
        req.setState(SecurityVerity.SECURITY_VERIFY_STATE);

        req.setConversationId(conversationId);
        req.setLoanType(prepayVerifyReq.getLoanType());
        req.setLoanAccount(prepayVerifyReq.getLoanAccount());
        req.setCurrency(prepayVerifyReq.getCurrency());

        //金额格式带修改
        String money=MoneyUtils.getRoundNumber(mPrepayDetailModel.getLoanCycleAppAmount(),2);

        req.setLoanAmount(BigDecimal.valueOf(Double.parseDouble(money)));

        req.setLoanPeriod(Integer.parseInt
                (mPrepayDetailModel.getLoanCycleLifeTerm()));
        req.setLoanToDate(mPrepayDetailModel.getLoanCycleMatDate());
        req.setAdvanceRepayInterest(prepayVerifyRes.getLoanRepayCount()
                .getAdvanceRepayInterest());
        req.setAdvanceRepayCapital(prepayVerifyRes.getLoanRepayCount()
                .getAdvanceRepayCapital());
        req.setRepayAmount(prepayVerifyReq.getRepayAmount());
        req.setFromAccountId(prepayVerifyReq.getFromAccountId());
        req.setAccountNumber(prepayVerifyReq.getAccountNumber());
        req.setLoanPeriodUnit(mPrepayDetailModel.getLoanPeriodUnit());
        req.setRemainCapital(prepayVerifyRes.getLoanRepayCount()
                .getRepayAmountInAdvance());
        req.setThisIssueRepayInterest(prepayVerifyRes.getLoanRepayCount()
                .getAdvanceRepayInterest());
        req.setRemainIssue(prepayVerifyRes.getLoanRepayCount()
                .getRemainIssueforAdvance());
        req.setCharges(prepayVerifyRes.getLoanRepayCount().getCharges());
        req.setThisIssueRepayDate(mPrepayDetailModel.getThisIssueRepayDate());
        req.setThisIssueRepayAmount(BigDecimal.valueOf(Double.parseDouble
                (mPrepayDetailModel.getThisIssueRepayAmount())));
        req.setAfterRepayRemainAmount(prepayVerifyRes.getLoanRepayCount()
                .getRepayAmountInAdvance());
        req.setAfterRepayissues(prepayVerifyRes.getLoanRepayCount()
                .getRemainIssueforAdvance());
        req.setOnlineFlag(mPrepayDetailModel.getOnlineFlag());
        req.setCycleType(mPrepayDetailModel.getCycleType());
        //计息方式
        req.setInterestType(mPrepayDetailModel.getInterestType());
        //提前还款后每期应还款额
        req.setAfterRepayissueAmount(prepayVerifyRes.getLoanRepayCount().getEveryTermAmount());

        getPresenter().setPrepaySubmitReq(req);
        showLoadingDialog();
        getPresenter().prepaySubmit();
       
    }

    @Override
    protected PrepayPresenter initPresenter()   {
        return new PrepayPresenter(this);
    }
}
