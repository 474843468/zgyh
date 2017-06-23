package com.boc.bocsoft.mobile.bocmobile.buss.account.notify.ui;

import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.contact.EditFeeAccountContact;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.model.EditFeeAccountModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.presenter.EditFeeAccountPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.LinkedHashMap;

/**
 * 修改缴费账号确认信息页面，修改完成返回首页
 * Created by wangtong on 2016/8/11.
 */
public class ModifyPayAccConfirmFragment extends BussFragment implements EditFeeAccountContact.View,
        SecurityVerity.VerifyCodeResultListener {

    private View rootView;
    private EditFeeAccountModel model;
    private EditFeeAccountPresenter presenter;
    protected ConfirmInfoView confirmInfoView;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_feeaccount_confirm, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        confirmInfoView = (ConfirmInfoView) rootView.findViewById(R.id.confirm_view);
    }

    @Override
    public void initData() {
        super.initData();
        model = new EditFeeAccountModel();
        model.setAccountNew((AccountBean) getArguments().getParcelable("newAccount"));
        model.setSignedAccount((AccountBean) getArguments().getParcelable("signedAccount"));
        model.setAccountOld(getArguments().getString("oldAccountNum"));
        model.setCustomerName(getArguments().getString("customerName"));

        presenter = new EditFeeAccountPresenter(this);
        presenter.psnGetSecurityFactor();
    }

    @Override
    public void setListener() {
        super.setListener();
        confirmInfoView.setListener(new ConfirmInfoView.OnClickListener() {
            @Override
            public void onClickConfirm() {
                presenter.psnSsmAccountChangePre();
            }

            @Override
            public void onClickChange() {
                SecurityVerity.getInstance().selectSecurityType();
            }
        });
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected void titleLeftIconClick() {
        pop();
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return "确认信息";
    }

    private void updateView() {
        LinkedHashMap<String, String> datas = new LinkedHashMap<>();
        datas.put("开通账户", NumberUtils.formatCardNumber(model.getSignedAccount().getAccountNumber()));
        datas.put("客户姓名", model.getCustomerName());
        datas.put("新缴费账号", NumberUtils.formatCardNumber(model.getAccountNew().getAccountNumber()));
        datas.put("原缴费账号", NumberUtils.formatCardNumber(model.getAccountOld()));
        confirmInfoView.addData(datas, false, true);
    }

    @Override
    public void securityFactorReturned() {
        CombinListBean factorBean = SecurityVerity.getInstance(getActivity())
                .getDefaultSecurityFactorId(model.getFactorModel());
        SecurityVerity.getInstance().setSecurityVerifyListener(this);
        model.setSelectedFactorId(factorBean.getId());
        confirmInfoView.updateSecurity(factorBean.getName());
        updateView();
    }

    @Override
    public EditFeeAccountModel getUiModel() {
        return model;
    }

    @Override
    public void psnSsmAccountChangePreReturned() {
        boolean result = SecurityVerity.getInstance().confirmFactor(model.getPrefactorList());
        if (result) {
            EShieldVerify.getInstance(getActivity()).setmPlainData(model.getmPlainData());
            SecurityVerity.getInstance().showSecurityDialog(model.getRandomNum());
        } else {
            showErrorDialog("预交易失败");
        }
    }

    @Override
    public void psnSsmAccountChangeReturned() {
        showToast("缴费账户更改成功");
        findFragment(AccSmsNotifyHomeFragment.class).
                refreshFeeAccount(model.getAccountNew().getAccountNumber());
        popTo(AccSmsNotifyHomeFragment.class, false);
    }

    @Override
    public void setPresenter(EditFeeAccountContact.Presenter presenter) {

    }

    @Override
    public void onSecurityTypeSelected(CombinListBean bean) {
        confirmInfoView.updateSecurity(bean.getName());
        model.setSelectedFactorId(bean.getId());
    }

    @Override
    public void onEncryptDataReturned(String factorId, String[] randomNums, String[] encryptPasswords) {
        model.setEncryptRandomNums(randomNums);
        model.setEncryptPasswords(encryptPasswords);
        presenter.psnSsmAccountChange();
    }

    @Override
    public void onSignedReturn(String signRetData) {
        model.setmSignedData(signRetData);
        presenter.psnSsmAccountChange();
    }
}
