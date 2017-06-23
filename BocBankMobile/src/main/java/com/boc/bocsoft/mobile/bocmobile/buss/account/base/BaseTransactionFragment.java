package com.boc.bocsoft.mobile.bocmobile.buss.account.base;

import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DeviceInfoUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.account.SecurityModel;

/**
 * @author wangyang
 *         16/8/19 20:26
 *         交易基础界面
 */
public abstract class BaseTransactionFragment<T extends BaseTransactionPresenter> extends BaseAccountFragment<T> implements BaseTransactionView, SecurityVerity.VerifyCodeResultListener, ConfirmInfoView.OnClickListener {

    protected ConfirmInfoView confirmInfoView;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        confirmInfoView = new ConfirmInfoView(getContext());
        return confirmInfoView;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_fragment_confirm_title);
    }

    @Override
    public void setListener() {
        confirmInfoView.setListener(this);
    }

    /**
     * 获取安全因子
     */
    @Override
    public void getSecurityCombin(String serviceId) {
        //获取安全因子
        showLoadingDialog(false);
        getPresenter().getSecurityCombin(serviceId, this);
    }

    @Override
    public void psnCombinSuccess(SecurityFactorModel securityModel) {
        closeProgressDialog();

        //设置默认安全因子
        CombinListBean combinListBean = SecurityVerity.getInstance(getActivity()).getDefaultSecurityFactorId(securityModel);
        SecurityVerity.getInstance().setSecurityVerifyListener(this);

        //设置安全因子按钮
        confirmInfoView.updateSecurity(combinListBean.getName());
    }

    @Override
    public void psnPreTransactionSuccess(SecurityModel securityModel) {
        //设置安全因子,请求随机数
        SecurityVerity.getInstance().confirmFactor(securityModel.getFactorList());
        getPresenter().getRandom(this);
    }

    @Override
    public void psnGetRandomSuccess(String random, String conversationId) {
        closeProgressDialog();

        //设置监听,开启安全输入框
        SecurityVerity.getInstance().setConversationId(conversationId);
        SecurityVerity.getInstance().showSecurityDialog(random);
    }

    @Override
    public void onSecurityTypeSelected(CombinListBean bean) {
        confirmInfoView.updateSecurity(bean.getName());
    }

    @Override
    public void onEncryptDataReturned(String factorId, String[] randomNums, String[] encryptPasswords) {
        showLoadingDialog(false);
        DeviceInfoModel deviceInfoModel = null;
        if (Integer.valueOf(SecurityVerity.SECURITY_VERIFY_DEVICE).toString().equals(factorId))
            deviceInfoModel = DeviceInfoUtils.getDeviceInfo(getActivity(), SecurityVerity.getInstance().getRandomNum());
        submitTransactionWithSecurity(deviceInfoModel, factorId, randomNums, encryptPasswords);
    }

    @Override
    public void onSignedReturn(String signRetData) {
        showLoadingDialog(false);
        String[] randomNums = {signRetData};
        submitTransactionWithSecurity(null, Integer.valueOf(SecurityVerity.SECURITY_VERIFY_E_TOKEN).toString(), randomNums, null);
    }

    public String getCurrentFactorId() {
        return SecurityVerity.getInstance().getCurrentSecurityVerifyTypeId();
    }

    @Override
    public void onClickChange() {
        SecurityVerity.getInstance().selectSecurityType();
    }
}
