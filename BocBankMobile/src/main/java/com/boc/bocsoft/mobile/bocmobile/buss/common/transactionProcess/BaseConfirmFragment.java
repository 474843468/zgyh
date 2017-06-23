package com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.BaseFillInfoBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.BaseSubmitBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.FactorBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DeviceInfoUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：XieDu
 * 创建时间：2016/9/18 13:49
 * 描述：
 * @param <T> 确认页面viewmodel
 * @param <D> 提交交易接口返回的结果类型
 */
public abstract class BaseConfirmFragment<T extends BaseFillInfoBean, D>
        extends MvpBussFragment<BaseConfirmContract.Presenter<T>>
        implements BaseConfirmContract.View<D>, ConfirmInfoView.OnClickListener,
        SecurityVerity.VerifyCodeResultListener {
    protected ConfirmInfoView confirmInfoView;

    protected static final String PARAM_INFO_FILL = "1";
    protected static final String PARAM_VERIFY = "2";
    protected T mFillInfoBean;
    protected BaseSubmitBean mSubmitBean;
    protected boolean securityTypeChanged;

    protected static <T extends BaseFillInfoBean> Bundle getBundleForNew(T fillInfo,
            VerifyBean verifyBean) {
        Bundle args = new Bundle();
        args.putParcelable(PARAM_INFO_FILL, fillInfo);
        args.putParcelable(PARAM_VERIFY, verifyBean);
        return args;
    }

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        confirmInfoView = new ConfirmInfoView(mContext);
        return confirmInfoView;
    }

    @Override
    public void initData() {
        mFillInfoBean = getArguments().getParcelable(PARAM_INFO_FILL);
        VerifyBean verifyBean = getArguments().getParcelable(PARAM_VERIFY);
        mSubmitBean = new BaseSubmitBean();
        if (verifyBean != null) {
            mSubmitBean.set_signedData(verifyBean.get_plainData());
            EShieldVerify.getInstance(getActivity()).setmPlainData(verifyBean.get_plainData());
        }
        SecurityVerity.getInstance(getActivity()).setConversationId(mFillInfoBean.getConversationId());
        setConfirmViewData();
        confirmInfoView.updateSecurity(mFillInfoBean.getCombinName());
    }

    /**
     * 给确认view设置数据
     */
    protected abstract void setConfirmViewData();

    @Override
    public void setListener() {
        confirmInfoView.setListener(this);
        SecurityVerity.getInstance(getActivity()).setSecurityVerifyListener(this);
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_fragment_confirm_title);
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
    public void onClickConfirm() {
        showLoadingDialog(false);
        getPresenter().verify(securityTypeChanged, mFillInfoBean);
    }

    @Override
    public void onClickChange() {
        //显示安全认证选择对话框
        SecurityVerity.getInstance().selectSecurityType();
    }

    @Override
    public void onVerifySuccess(VerifyBean verifyBean, String mRandom, String tokenId) {
        closeProgressDialog();
        if (securityTypeChanged) {
            mSubmitBean.set_signedData(verifyBean.get_plainData());
            EShieldVerify.getInstance(getActivity()).setmPlainData(verifyBean.get_plainData());
            securityTypeChanged = false;
        }
        mSubmitBean.setToken(tokenId);

        // 显示安全认证对话框
        if (SecurityVerity.getInstance()
                          .confirmFactor(copyFactorList(verifyBean.getFactorList()))) {
            SecurityVerity.getInstance().showSecurityDialog(mRandom);
        }
    }

    private List<FactorListBean> copyFactorList(List<FactorBean> factorList) {
        List<FactorListBean> result = new ArrayList<>();
        for (FactorBean factorBean : factorList) {
            result.add(BeanConvertor.fromBean(factorBean, new FactorListBean()));
        }
        return result;
    }

    @Override
    public abstract void onSubmitSuccess(D submitResult);

    @Override
    public void onSecurityTypeSelected(CombinListBean bean) {
        // 设置更改的安全因子名称
        securityTypeChanged = !mFillInfoBean.get_combinId().equals(bean.getId());
        mFillInfoBean.set_combinId(bean.getId());
        mFillInfoBean.setCombinName(bean.getName());
        confirmInfoView.updateSecurity(bean.getName());
    }

    @Override
    public void onEncryptDataReturned(String factorId, String[] randomNums,
            String[] encryptPasswords) {
        showLoadingDialog(false);
        mSubmitBean.setState(SecurityVerity.SECURITY_VERIFY_STATE);
        mSubmitBean.setActiv(SecurityVerity.getInstance().getCfcaVersion());
        int type = Integer.parseInt(factorId);
        switch (type) {
            case SecurityVerity.SECURITY_VERIFY_SMS:
                mSubmitBean.setSmc(encryptPasswords[0]);
                mSubmitBean.setSmc_RC(randomNums[0]);
                break;
            case SecurityVerity.SECURITY_VERIFY_TOKEN:
                mSubmitBean.setOtp(encryptPasswords[0]);
                mSubmitBean.setOtp_RC(randomNums[0]);

                break;
            case SecurityVerity.SECURITY_VERIFY_SMS_AND_TOKEN:
                mSubmitBean.setOtp(encryptPasswords[0]);
                mSubmitBean.setOtp_RC(randomNums[0]);
                mSubmitBean.setSmc(encryptPasswords[1]);
                mSubmitBean.setSmc_RC(randomNums[1]);
                break;
            case SecurityVerity.SECURITY_VERIFY_DEVICE:
                mSubmitBean.setSmc(encryptPasswords[0]);
                mSubmitBean.setSmc_RC(randomNums[0]);
                DeviceInfoModel deviceInfoModel = DeviceInfoUtils.getDeviceInfo(mContext,
                        SecurityVerity.getInstance().getRandomNum());
                mSubmitBean.setDeviceInfo(deviceInfoModel.getDeviceInfo());
                mSubmitBean.setDeviceInfo_RC(deviceInfoModel.getDeviceInfo_RC());
                mSubmitBean.setDevicePrint(DeviceInfoUtils.getDevicePrint(getActivity()));
                break;
            default:
                break;
        }
        getPresenter().submit(mFillInfoBean, mSubmitBean);
    }

    /**
     * 音频key怎么处理？
     */
    @Override
    public void onSignedReturn(String signRetData) {
        showLoadingDialog(false);
        mSubmitBean.set_signedData(signRetData);
        getPresenter().submit(mFillInfoBean, mSubmitBean);
    }
}
