package com.boc.bocsoft.mobile.bocmobile.buss.login.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.PsnGetSecurityFactorResult;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.PsnSvrRegisterDevicePreResult;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDeviceSubmit.PsnSvrRegisterDeviceSubmitParams;
import com.boc.bocsoft.mobile.bii.common.client.BIIClient;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BaseMobileActivity;
import com.boc.bocsoft.mobile.bocmobile.base.model.User;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DeviceInfoUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.SpUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.ErrorDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.login.presenter.BindingDevicePresenter;
import com.boc.bocsoft.mobile.common.utils.ButtonClickLock;

/**
 * Created by feibin on 16/8/15.
 * 硬件绑定
 */
public class BindingDeviceFragment extends LoginBaseFragment implements BindingDeviceContrct.View,
        SecurityVerity.VerifyCodeResultListener, View.OnClickListener {

    //硬件绑定service通信处理类
    private BindingDevicePresenter mBindingPresent;
    protected View rootView;
    protected TextView txtCombinValue;
    protected TextView txtChange;
    protected Button btnConfirm;

    //用户已经绑定的安全认证方式
    private PsnGetSecurityFactorResult mSecurityFactorResult;
    //用户选择的安全认证方式
    private CombinListBean mSelectSecurityFactor;
    private String bindingRandom;
    /**
     * 防重锁
     */
    private String clickOprLock = "click_more";
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.boc_fragment_binding_device,
                null);

        return rootView;
    }

    @Override
    public void initView() {
        txtCombinValue = (TextView) rootView.findViewById(R.id.txt_combin_value);
        txtChange = (TextView) rootView.findViewById(R.id.txt_change);
        btnConfirm = (Button) rootView.findViewById(R.id.btn_confirm);
    }

    @Override
    public void beforeInitView() {
        super.beforeInitView();
        Bundle arguments = getArguments();
        if (arguments != null) {// 接收传入model
            mCustomerInfor = (User) arguments
                    .getParcelable("mCustomerInfor");
        }
    }

    /**
     * 数据初始化
     */
    @Override
    public void initData() {
        super.initData();
        ButtonClickLock.getLock(clickOprLock).lockDuration = 1000;
        mBindingPresent = new BindingDevicePresenter(this);
        SecurityVerity.getInstance(getActivity()).setSecurityVerifyListener(this);
        mBindingPresent.querySecurityFactor();
    }

    @Override
    public void setListener() {
        txtChange.setOnClickListener(BindingDeviceFragment.this);
        btnConfirm.setOnClickListener(BindingDeviceFragment.this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.txt_change) {
            // 防暴力点击下一步按钮
            if (!ButtonClickLock.isCanClick(clickOprLock)) {
                return;
            }
            if(null != mSecurityFactorResult){
                //更换安全认证方式
                SecurityVerity.getInstance(getActivity()).selectSecurityType();
            }


        } else if (view.getId() == R.id.btn_confirm) {
            // 防暴力点击下一步按钮
            if (!ButtonClickLock.isCanClick(clickOprLock)) {
                return;
            }
            //下一步按钮
            //硬件绑定预交易
            if(null != mSelectSecurityFactor){
                showLoadingDialog(false);
                mBindingPresent.queryPsnSvrRegisterDevicePre(mSelectSecurityFactor);
            }

        }
    }

    /**--------后台接口请求回调开始-----------**/
    /**
     * 获取安全因子成功回调
     */
    @Override
    public void securityFactorSuccess(PsnGetSecurityFactorResult psnGetSecurityFactorResult) {
        closeProgressDialog();
        if (null == psnGetSecurityFactorResult) {
            showFaultDialog("该用户没有绑定安全工具，请先去绑定安全工具");
            return;
        }
        //获取的安全认证方式
        mSecurityFactorResult = psnGetSecurityFactorResult;
        //转换为view层Model
        SecurityFactorModel securityFactorModel = new SecurityFactorModel(mSecurityFactorResult);
        if (null == mSecurityFactorResult.get_combinList()) {
            showFaultDialog("该用户没有绑定安全工具，请先去绑定安全工具");
            return;
        }
        //获取默认的安全工具
        mSelectSecurityFactor = SecurityVerity.getInstance(getActivity()).getDefaultSecurityFactorId(securityFactorModel);
        txtCombinValue.setText(mSelectSecurityFactor.getName());
    }

    /**
     * 硬件注册预交易成功回调
     *
     * @param psnSvrRegisterDevicePreResult
     */
    @Override
    public void svrRegisterDevicePreSuccess(PsnSvrRegisterDevicePreResult psnSvrRegisterDevicePreResult) {
        //确认安全认证工具
        boolean available = SecurityVerity.getInstance().confirmFactor(psnSvrRegisterDevicePreResult.getFactorList());
        EShieldVerify.getInstance(getActivity()).setmPlainData(psnSvrRegisterDevicePreResult.get_plainData());
        if (available) {
            //获取随机数
            mBindingPresent.queryRandom();
        } else {
            closeProgressDialog();
            //获取确定的安全因子失败
            ApplicationContext.getInstance().setLogin(false);
            BIIClient.instance.clearCookies();
    
        }
    }

    /**
     * 获取随机数成功回调
     *
     * @param random
     */
    @Override
    public void randomSuccess(String random) {
        closeProgressDialog();
        bindingRandom = random;
        //弹出选择的安全认证工具
        SecurityVerity.getInstance().showSecurityDialog(random);

    }

   /**
     * 硬件绑定提交交易失败回调
     */
    @Override
    public void svrRegisterDeviceSubmitFail() {
    }
    
    /**
     * 硬件绑定提交交易成功回调
     */
    @Override
    public void svrRegisterDeviceSubmitSuccess() {
        closeProgressDialog();
        // 绑定设备成功，存入本机设备信息
        SpUtils.saveLNString(mContext,
                ApplicationConst.SHARED_PREF_LOCAL_BIND_INFO, DeviceInfoUtils.getLocalCAOperatorId(getActivity(),mCustomerInfor.getOperatorId(),1));
        SpUtils.saveLNString(mContext,
                ApplicationConst.SHARED_PREF_LOCAL_BIND_INFO_MAC, DeviceInfoUtils.getLocalCAOperatorId(getActivity(),mCustomerInfor.getOperatorId(),2));
        mCustomerInfor.setHasBindingDevice("1");
        //跳转到绑定成功结果页面
        BindingDeviceResultFragment bindingDeviceResultFragment = new BindingDeviceResultFragment();
        start(bindingDeviceResultFragment);
        return;
    }

    @Override
    public void logoutSuccess() {
       ApplicationContext.getInstance().logout();
    }
    /**--------后台接口请求回调结束-----------**/


    /**
     * --------安全认证工具回调开始-----------
     **/
    @Override
    public void onSecurityTypeSelected(CombinListBean bean) {
        mSelectSecurityFactor = bean;
        txtCombinValue.setText(mSelectSecurityFactor.getName());
    }

    @Override
    public void onEncryptDataReturned(String factorId, String[] randomNums, String[] encryptPasswords) {
        //封装硬件绑定提交参数
        PsnSvrRegisterDeviceSubmitParams mParams = bulidBindingSubmitModel(factorId, randomNums, encryptPasswords);
        showLoadingDialog(false);
        //请求硬件绑定交易
        mBindingPresent.queryPsnSvrRegisterDeviceSubmit(mParams);
    }

    @Override
    public void onSignedReturn(String signRetData) {
        //封装硬件绑定提交参数
        PsnSvrRegisterDeviceSubmitParams mParams = bulidBindingSubmitModelForCA(signRetData);
        showLoadingDialog(false);
        //请求硬件绑定交易
        mBindingPresent.queryPsnSvrRegisterDeviceSubmit(mParams);
    }

    /**
     * --------安全认证工具回调结束-----------
     **/

    /**
     * 硬件绑定提交参数封装
     */
    private PsnSvrRegisterDeviceSubmitParams bulidBindingSubmitModel(
            String factorId, String[] randomNums, String[] encryptPasswords) {
        PsnSvrRegisterDeviceSubmitParams mParams = new PsnSvrRegisterDeviceSubmitParams();
        if ("8".equals(factorId)) {
            //动态口令
            mParams.setOtp(encryptPasswords[0]);
            mParams.setOtp_RC(randomNums[0]);
        } else if ("32".equals(factorId)) {
            //短信验证码
            mParams.setSmc(encryptPasswords[0]);
            mParams.setSmc_RC(randomNums[0]);
        } else if ("40".equals(factorId)) {
            //动态口令+短信验证码
            mParams.setOtp(encryptPasswords[0]);
            mParams.setOtp_RC(randomNums[0]);
            mParams.setSmc(encryptPasswords[1]);
            mParams.setSmc_RC(randomNums[1]);
        }
        mParams.setActiv(ApplicationContext.getInstance().getActive());
        mParams.setState(ApplicationContext.getInstance().getState());

        //硬件信息参数
        String deviceInfo = "";
        String deviceInfo_RC = "";
        try {
            DeviceInfoModel deviceInfoModel =  DeviceInfoUtils.getDeviceInfo(getContext(),bindingRandom,mCustomerInfor.getOperatorId());
            deviceInfo_RC = deviceInfoModel.getDeviceInfo_RC();
            deviceInfo = deviceInfoModel.getDeviceInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mParams.setDeviceInfo(deviceInfo);
        mParams.setDeviceInfo_RC(deviceInfo_RC);

        return mParams;
    }

    /**
     * 硬件绑定提交参数封装 for CA
     */
    private PsnSvrRegisterDeviceSubmitParams bulidBindingSubmitModelForCA( String signRetData) {
        PsnSvrRegisterDeviceSubmitParams mParams = new PsnSvrRegisterDeviceSubmitParams();
        mParams.set_signedData(signRetData);

        mParams.setActiv(ApplicationContext.getInstance().getActive());
        mParams.setState(ApplicationContext.getInstance().getState());
        //硬件信息参数
        String deviceInfo = "";
        String deviceInfo_RC = "";
        try {
            DeviceInfoModel deviceInfoModel =  DeviceInfoUtils.getDeviceInfo(getContext(),bindingRandom,mCustomerInfor.getOperatorId());
            deviceInfo_RC = deviceInfoModel.getDeviceInfo_RC();
            deviceInfo = deviceInfoModel.getDeviceInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mParams.setDeviceInfo(deviceInfo);
        mParams.setDeviceInfo_RC(deviceInfo_RC);

        return mParams;
    }

    /**
     * 是否有标题头
     * @return
     */
    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButtonClickLock.removeLock(clickOprLock);
    }

    @Override
    public boolean onBackPress() {
        return true;
    }
    /**
     * 报错弹出框
     */
    private ErrorDialog errorDialog;
    private void showFaultDialog(String errorMessage){
        closeProgressDialog();
        errorDialog = new ErrorDialog(mContext);
        errorDialog.setBtnText("确认");
        errorDialog.setCancelable(false);
        errorDialog.setErrorData(errorMessage);
        if (!errorDialog.isShowing()) {
            errorDialog.show();
        }

        errorDialog.setOnBottomViewClickListener(new ErrorDialog.OnBottomViewClickListener() {
            @Override
            public void onBottomViewClick() {
                //点击确定按钮   调用退出接口
                if(null!=mBindingPresent){
                    mBindingPresent.queryLogout();
                }
            }
        });

    }
}
