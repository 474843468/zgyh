package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.ui;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DeviceInfoUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRowButton;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.model.LoanRegisterSumbitReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.model.PreRegisterVerifyReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.model.PreRegisterVerifyRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.presenter.ApplyPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui.LoanActivatedFragment;
import com.boc.bocsoft.mobile.common.utils.ButtonClickLock;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.LinkedHashMap;

/**
 * 贷款管理—中银E贷-贷款申请确认信息页面fragment
 * Created by xintong on 2016/6/7.
 */
public class ApplyConfirmInfoFragment extends MvpBussFragment<ApplyPresenter> implements ApplyContract.ApplyConfirmView{
    private static final String TAG = "ApplyConfirmInfoFragmen";
    //页面名称
    private static String PAGENAME;
    //安全认证组件
    private DetailTableRowButton bocBtntv;
//    private ApplyPresenter applyPresenter;
  //预交易后，再次确认安全因子  为true调用预交易接口
    private boolean available;
    //页面根视图
    private View mRoot;
    private LinearLayout content;
    //详情内容组件
    private DetailContentView detail;
    //确认按钮
    Button confirm;
    //会话id
    private String conversationId;
    //头控件
    private DetailTableHead title;
    private PreRegisterVerifyReq preRegisterVerifyReq;
  //安全组件
    private SecurityVerity securityVerity;
  //获取安全因子成功后返回的默认安全因子组合id
    private String _combinId,combinId_name;
    //随机数
    private String random;
    private LoanRegisterSumbitReq req;
    
    /** 提交按钮 防暴力点击 */
    private String click_more = "click_more";

    //确认界面公共组件
    private ConfirmInfoView confirmInfoView;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRoot = mInflater.inflate(R.layout.fragment_eloan_apply_confirminfo, null);
        return mRoot;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }

    public void set_combinId_Name(String combinId_name) {
        this.combinId_name = combinId_name;
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
        return getString(R.string.boc_eloan_applyconfirmation_pagename);
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

//  public void setPreRegisterVerifyReq(PreRegisterVerifyReq req) {
//  this.preRegisterVerifyReq = req;
//}
    
    @Override
    public void beforeInitView() {

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
//        applyPresenter = new ApplyPresenter(this);
        securityVerity = SecurityVerity.getInstance(getActivity());
        //初始化组件
        content = (LinearLayout) mRoot.findViewById(R.id.content);

//        title = new DetailTableHead(mContext);
//        detail = new DetailContentView(mContext);
//        bocBtntv = new DetailTableRowButton(mContext);
//        confirm = (Button) mRoot.findViewById(R.id.confirm);

        confirmInfoView=new ConfirmInfoView(mContext);

    }

    @Override
    public void initData() {
    	ButtonClickLock.getLock(click_more).lockDuration = EloanConst.CLICK_MORE_TIME;

    	preRegisterVerifyReq=(PreRegisterVerifyReq) getArguments().getSerializable("PreRegisterVerifyReq");
        String type=preRegisterVerifyReq.getLinkRelation();
        LogUtils.i(TAG,"conversationId="+conversationId);

        getPresenter().setConversationId(conversationId);
        securityVerity.setConversationId(conversationId);

        LinkedHashMap<String,String> map=new LinkedHashMap<>();
        LinkedHashMap<String,String> map_devide=new LinkedHashMap<>();
        if(null!=preRegisterVerifyReq){

            //为组建添加默认数据
            confirmInfoView.setHeadValue(getString(R.string.boc_eloan_apply_limit)+"  (人民币元)"
                    , MoneyUtils.transMoneyFormat(preRegisterVerifyReq.getApplyQuotet().toString(), "021"),false);

            //确认页显示数据
            map.put(getString(R.string.boc_eloan_apply_name) , preRegisterVerifyReq.getCustName());//姓名
            map.put(getString(R.string.boc_eloan_apply_IDnum) , NumberUtils.formatIDNumber(preRegisterVerifyReq.getCerNo()));//身份证
            map.put(getString(R.string.boc_eloan_apply_tel) , NumberUtils.formatMobileNumberWithAsterrisk(preRegisterVerifyReq.getMobile()));//电话
            map.put(getString(R.string.boc_eloan_apply_address) , preRegisterVerifyReq.getLinkAddress());//地址
            map.put(getString(R.string.boc_eloan_apply_repaymentAccount) ,NumberUtils.formatCardNumber(preRegisterVerifyReq.getLoanRepayAccount()) );//还款账户

            confirmInfoView.addData(map,true);

            if ("01".equals(type)) {
                map_devide.put(getString(R.string.boc_eloan_apply_link_relationship) ,"父母" );//关系
            } else if ("02".equals(type) ) {
                map_devide.put(getString(R.string.boc_eloan_apply_link_relationship) , "配偶");//关系
            } else if ("03".equals(type) ) {
                map_devide.put(getString(R.string.boc_eloan_apply_link_relationship) ,"兄弟" );//关系
            } else if ("04".equals(type) ) {
                map_devide.put(getString(R.string.boc_eloan_apply_link_relationship) ,"姐妹" );//关系
            }else{
                map_devide.put(getString(R.string.boc_eloan_apply_link_relationship) , "其他");//关系
            }
            map_devide.put(getString(R.string.boc_eloan_apply_link_name) , preRegisterVerifyReq.getLinkName());//联系人姓名
            map_devide.put(getString(R.string.boc_eloan_apply_link_tel) , NumberUtils.formatMobileNumberWithAsterrisk(preRegisterVerifyReq.getLinkMobile()));//联系人电话

            confirmInfoView.addData(map_devide,false,true);
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
                    showLoadingDialog(false);
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

                       preRegisterVerifyReq.set_combinId(_combinId);
                        getPresenter().setPreRegisterVerifyReq(preRegisterVerifyReq);
                       showLoadingDialog(false);
                       //调用预交易接口
                        getPresenter().preLoanRegisterVerify();
            		}
            	}  
            }

            //安全组件点击确定触发
            @Override
            public void onEncryptDataReturned(String factorId, String[] randomNums, String[] encryptPasswords) {
                showLoadingDialog(false);
                buildSubmitReq(factorId, randomNums, encryptPasswords);
            }

            @Override
            public void onSignedReturn(String signRetData) {
            	showLoadingDialog(false);
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
    public void preLoanRegisterVerifySuccess(PreRegisterVerifyRes result) {
        Log.i(TAG, "-------->预交易接口调用成功！");
        closeProgressDialog();
        available = securityVerity.confirmFactor(result.getFactorList());
        
        //音频key加密
        EShieldVerify.getInstance(getActivity()).setmPlainData(result.get_plainData());
        
//        if (available) {
//            //获取随机数
//            applyPresenter.getRandom();
//            //音频key加密
//            EShieldVerify.getInstance(getActivity()).setmPlainData(result.get_plainData());
////            showLoadingDialog();
//        } else {
//            //TODO 获取确定的安全因子失败
//        }
    }

    @Override
    public void preLoanRegisterVerifyFail(ErrorException e) {
        Log.i(TAG, "-------->预交易接口调用失败！");
        closeProgressDialog();
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
    public void loanRegisterSumbitSuccess() {
        Log.i(TAG,"-------->交易接口调用成功！");
//        showLoadingDialog();
        Toast.makeText(mContext,"您已完成申请，请等待审批结果。",Toast.LENGTH_LONG).show();
//        EloanStatusFragment.isStartPage=true;
//        popTo(EloanStatusFragment.class,false);
        
       // popToAndReInit(EloanStatusFragment.class);
        popToAndReInit(LoanActivatedFragment.class);
//        start(new EloanStatusFragment());
        closeProgressDialog();
    }

    @Override
    public void loanRegisterSumbitFail(ErrorException e) {
        Log.i(TAG,"-------->交易接口调用失败！");
//        start(new EloanStatusFragment());
//        popTo(EloanStatusFragment.class,false);
        closeProgressDialog();
    }

    @Override
    public void setPresenter(ApplyContract.Presenter presenter) {
    }

    /**
     *  构造交易上送参数
     */
    private void buildSubmitReq(String factorId, String[] randomNums, String[] encryptPasswords){

    	req= new LoanRegisterSumbitReq();
        
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
                LogUtils.i("cq---------->动态口令+短信验证码-------"
                +encryptPasswords[0]+"----------"
                		+randomNums[0]+"----------"
                +encryptPasswords[1]+"----------"
                		+randomNums[1]);
                req.setFactorId(factorId);
                buildSubmit();
//        	}
        } else if ("96".equals(factorId)) {
//        	if(check(factorId,encryptPasswords)){
        		//短信验证码
                req.setSmc(encryptPasswords[0]);
                req.setSmc_RC(randomNums[0]);
                req.setFactorId(factorId);
//                DeviceInfoModel infoModel = DeviceInfoUtils.getDeviceInfo(getActivity()
//                        ,random, SpUtils.getSpString(getContext(),EloanConst.OPETATOR_ID,null));
//
//                req.setDeviceInfo(infoModel.getDeviceInfo());
//                req.setDeviceInfo_RC(infoModel.getDeviceInfo_RC());
//                req.setDevicePrint(DeviceInfoUtils.getDevicePrint(getActivity()));
            DeviceInfoModel info = DeviceInfoUtils.getDeviceInfo(getActivity(), random);
            req.setDeviceInfo(info.getDeviceInfo());
            req.setDeviceInfo_RC(info.getDeviceInfo_RC());
            req.setDevicePrint(DeviceInfoUtils.getDevicePrint(getActivity()));
                buildSubmit();
//        	}
        }else if ("4".equals(factorId)) {
            //TODO 音频key
//            req.set_signedData(req.get_signedData());
        }     
 
    }
    
    private void buildSubmitReq(String signRetData){
    	//TODO 音频key
        req = new LoanRegisterSumbitReq();
        req.set_signedData(signRetData);
        req.setFactorId("4");
        buildSubmit();
    }
    
    private void buildSubmit(){
    	 //cfn版本号和状态
        req.setActiv(SecurityVerity.getInstance(getActivity()).getCfcaVersion());
        req.setState(SecurityVerity.SECURITY_VERIFY_STATE);

        req.setConversationId(conversationId);
        Log.i(TAG,"conversationId1="+conversationId);
        req.setLoanPrdNo("OC-LOAN");
        req.setCustName(preRegisterVerifyReq.getCustName());
        req.setCerType(preRegisterVerifyReq.getCerType());
        req.setCerNo(preRegisterVerifyReq.getCerNo());
        req.setZoneCode(preRegisterVerifyReq.getZoneCode());
        req.setMobile(preRegisterVerifyReq.getMobile());
        req.setStreetInfo(preRegisterVerifyReq.getStreetInfo());
        req.setLinkAddress(preRegisterVerifyReq.getLinkAddress());
        req.setLinkRelation(preRegisterVerifyReq.getLinkRelation());
        req.setLinkName(preRegisterVerifyReq.getLinkName());
        req.setLinkMobile(preRegisterVerifyReq.getLinkMobile());
        req.setLoanRepayAccountId(preRegisterVerifyReq.getLoanRepayAccountId());
        req.setLoanRepayAccount(preRegisterVerifyReq.getLoanRepayAccount());
        req.setCurrencyCode(preRegisterVerifyReq.getCurrencyCode());
        req.setContractFormId(preRegisterVerifyReq.getContractFormId());
        req.setApplyQuotet(preRegisterVerifyReq.getApplyQuotet());
        req.setThreeContractNo(preRegisterVerifyReq.getThreeContractNo());
        req.seteLanguage("CHN");
        getPresenter().setLoanRegisterSumbitReq(req);
        getPresenter().loanRegisterSumbit();
//        showLoadingDialog();
    }

    @Override
    protected ApplyPresenter initPresenter() {
        return new ApplyPresenter(this);
    }
}
