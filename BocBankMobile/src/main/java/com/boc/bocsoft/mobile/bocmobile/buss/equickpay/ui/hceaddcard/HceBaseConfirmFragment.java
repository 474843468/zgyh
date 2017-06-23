package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hceaddcard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.FactorBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DeviceInfoUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.HceTransactionViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.SubmitModel;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangle on 2016/12/14.
 * 描述: 申请或激活确认界面
 */
public abstract class HceBaseConfirmFragment<R> extends MvpBussFragment<HceBaseConfirmContract.Present> implements ConfirmInfoView.OnClickListener, SecurityVerity.VerifyCodeResultListener, HceBaseConfirmContract.View<R> {

    protected static final String KEY_MODEL = "key_model";

    protected ConfirmInfoView mConfirmInfoView;// 整个确认的view
    private SecurityVerity mSecurityVerity;//安全控件
    protected HceTransactionViewModel mViewModel;// 界面的model
    private boolean mIsSecurityChanged;
    private SubmitModel mSubmitModel;// 加密model

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mViewModel =  getArguments().getParcelable(KEY_MODEL);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mConfirmInfoView = new ConfirmInfoView(mContext);
    }

    @Override
    public void initData() {
        super.initData();
        // 安全工具实例
        mSecurityVerity = SecurityVerity.getInstance(getActivity());
        mSubmitModel = new SubmitModel();

        // 获取安全因子
        getPresenter().getSecurityFactor();
        setConfirmData();
    }

    protected abstract void setConfirmData();

    @Override
    public void setListener() {
        super.setListener();
        mConfirmInfoView.setListener(this);
        mSecurityVerity.setSecurityVerifyListener(this);
    }

    @Override
    protected String getTitleValue() {
        return "确认信息";
    }

    @Override
    public void onClickConfirm() {
        // 点击确实,先预交易
        ToastUtils.show("======点击确实==先预交易=======");
         getPresenter().verify();
    }

    @Override
    public void onClickChange() {
        // 更改安全认证方式
        ToastUtils.show("======更改安全工具=========");
        mSecurityVerity.selectSecurityType();
    }

    @Override
    public HceTransactionViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public void onSecurityTypeSelected(CombinListBean bean) {
        mIsSecurityChanged = !mViewModel.get_combinId().equals(bean.getId());
        if (mIsSecurityChanged) {
            updateSecurityAndSaveId(bean);
        }
    }

    /**
     *  安全工具加密完回调
     * @param factorId
     * @param randomNums
     * @param encryptPasswords
     */
    @Override
    public void onEncryptDataReturned(String factorId, String[] randomNums, String[] encryptPasswords) {
        mSubmitModel.reset();
        mSubmitModel.setState(mSecurityVerity.SECURITY_VERIFY_STATE);
        mSubmitModel.setActiv(mSecurityVerity.getCfcaVersion());
        int type = Integer.parseInt(factorId);
        switch (type) {
            case SecurityVerity.SECURITY_VERIFY_SMS:
                mSubmitModel.setSmc(encryptPasswords[0]);
                mSubmitModel.setSmc_RC(randomNums[0]);
                break;
            case SecurityVerity.SECURITY_VERIFY_TOKEN:
                mSubmitModel.setOtp(encryptPasswords[0]);
                mSubmitModel.setOtp_RC(randomNums[0]);
                break;
            case SecurityVerity.SECURITY_VERIFY_SMS_AND_TOKEN:
                mSubmitModel.setOtp(encryptPasswords[0]);
                mSubmitModel.setOtp_RC(randomNums[0]);
                mSubmitModel.setSmc(encryptPasswords[1]);
                mSubmitModel.setSmc_RC(randomNums[1]);
                break;
            case SecurityVerity.SECURITY_VERIFY_DEVICE:
                mSubmitModel.setSmc(encryptPasswords[0]);
                mSubmitModel.setSmc_RC(randomNums[0]);
                DeviceInfoModel deviceInfoModel = DeviceInfoUtils.getDeviceInfo(mContext, mSecurityVerity.getRandomNum());
                mSubmitModel.setDeviceInfo(deviceInfoModel.getDeviceInfo());
                mSubmitModel.setDeviceInfo_RC(deviceInfoModel.getDeviceInfo_RC());
                mSubmitModel.setDevicePrint(DeviceInfoUtils.getDevicePrint(getActivity()));
                break;
            default:
                break;
        }
        //  加密完成后,提交交易
        getPresenter().submit(mSubmitModel);
    }

    @Override
    public void onSignedReturn(String signRetData) {
        //  CA 认证
        mSubmitModel.reset();
        mSubmitModel.set_signedData(signRetData);
        getPresenter().submit(mSubmitModel);
    }

    @Override
    public void onGetSecurityFactorSuccess(SecurityFactorModel securityFactorModel) {
        //  获取默认安全因子
        PublicUtils.checkNotNull(securityFactorModel, "securityFactorModel == null");
        CombinListBean combinBean = mSecurityVerity.getDefaultSecurityFactorId(securityFactorModel);
        updateSecurityAndSaveId(combinBean);
    }

    @Override
    public void onVerifySuccess(VerifyBean verifyBean, String random) {
        EShieldVerify.getInstance(getActivity()).setmPlainData(verifyBean.get_plainData());
        // 预交易成功再次确认安全因子
        if (mSecurityVerity.confirmFactor(copyFactorList(verifyBean.getFactorList()))) {
            // 显示安全工具dialog
            mSecurityVerity.setConversationId(mViewModel.getConversationId());
            mSecurityVerity.showSecurityDialog(random);
        } else {
            showErrorDialog("安全认证错误,请重试");
        }
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
    public void showLoading() {
        super.showLoadingDialog();
    }

    @Override
    public void closeLoading() {
        super.closeProgressDialog();
    }

    private void updateSecurityAndSaveId(CombinListBean combinBean) {
        mConfirmInfoView.updateSecurity(combinBean.getName());
        mViewModel.set_combinId(combinBean.getId());
    }

    public static List<FactorListBean> copyFactorList(List<FactorBean> factorList) {
        List<FactorListBean> result = new ArrayList<>();
        for (FactorBean factorBean : factorList) {
            result.add(BeanConvertor.fromBean(factorBean, new FactorListBean()));
        }
        return result;
    }

}
