package com.boc.bocsoft.mobile.bocmobile.buss.account.notify.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.contact.ConfirmEdieContact;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.model.ConfirmEditModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.model.EditResultModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.model.SmsNotifyEditModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.presenter.ConfirmEdiePresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.LinkedHashMap;

/**
 * 开通、添加手机号、修改短信通知 确认信息页面
 * Created by wangtong on 2016/6/17.
 */
public class ConfirmInfoFragment extends BussFragment implements ConfirmEdieContact.View,
        SecurityVerity.VerifyCodeResultListener {

    protected View rootView;
    //数据模型
    protected ConfirmEditModel model;
    //业务处理
    private ConfirmEdiePresenter presenter;
    //是否为修改模式
    private boolean isEdit = true;
    //是否为首次开通
    private boolean isFirstOpen = false;
    private boolean isChangedSecurityFactor = false;
    private boolean isFirstPre = true;

    protected ConfirmInfoView confirmInfoView;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_confirm, null);
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
        model = new ConfirmEditModel();
        isEdit = getArguments().getBoolean(SmsNotifyEditFragment.KEY_IS_EDIT);
        isFirstOpen = getArguments().getBoolean(SmsNotifyEditFragment.KEY_IS_FIRST);
        model.setEditModel((SmsNotifyEditModel) getArguments().getParcelable("model"));
        if (isEdit) {
            model.setOldEditModel((SmsNotifyEditModel) getArguments().getParcelable(SmsNotifyEditFragment.KEY_OLD_MODEL));
        }

        presenter = new ConfirmEdiePresenter(this);
        presenter.psnGetSecurityFactor();

        LinkedHashMap<String, CharSequence> datas = new LinkedHashMap<>();
        datas.put("开通账户", NumberUtils.formatCardNumber(model.getEditModel().getSignAccount().getAccountNumber()));
        datas.put("客户姓名", model.getEditModel().getUserName());
        String phone = NumberUtils.formatMobileNumber(model.getEditModel().getPhoneNum());
        datas.put("手机号", phone);
        String moneyRang = MoneyUtils.transMoneyFormat(model.getEditModel().getMinMoney(), "001") + "元~"
                + MoneyUtils.transMoneyFormat(model.getEditModel().getMaxMoney(), "001") + "元";
        datas.put("金额范围", moneyRang);
        datas.put("缴费账户", NumberUtils.formatCardNumber(model.getEditModel().getFeeAccount().getAccountNumber()));
        if (isFirstOpen) {
            datas.put("收费标准", getResources().getString(R.string.boc_account_fee_type, "2.00"));
        }
        confirmInfoView.addData(datas, false, true);
    }


    @Override
    public void setListener() {
        super.setListener();
        confirmInfoView.setListener(new ConfirmInfoView.OnClickListener() {
            @Override
            public void onClickConfirm() {
                // 第一次预交易或者安全因子是否改变，重新请求
                if (isFirstPre || isChangedSecurityFactor) {
                    isFirstPre = false;
                    if (isEdit) {// 修改
                        presenter.psnSsmMessageChangePre();
                    } else if (isFirstOpen) {// 开通
                        presenter.psnSsmSignPre();
                    } else {
                        presenter.psnSsmMadePre();
                    }
                } else {//弹出安全工具框
                    SecurityVerity.getInstance().showSecurityDialog(model.getRandomNum());
                }
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


    @Override
    public ConfirmEditModel getUiModel() {
        return model;
    }

    @Override
    public void smsMadePreReturned() {
        confirmSecurity();
    }

    @Override
    public void smsMadeReturned() {
        ResultFragment fragment = new ResultFragment();
        Bundle bundle = new Bundle();
        EditResultModel model = new EditResultModel(this.model);
        model.setHeadName("申请成功");
        bundle.putParcelable(SmsNotifyEditFragment.KEY_MODEL, model);
        fragment.setArguments(bundle);
        start(fragment);
    }

    @Override
    public void psnSsmSignPreReturned() {
        confirmSecurity();
    }

    @Override
    public void psnSsmSignReturned() {
        ResultFragment fragment = new ResultFragment();
        Bundle bundle = new Bundle();
        EditResultModel model = new EditResultModel(this.model);
        model.setHeadName("申请成功");
        bundle.putParcelable(SmsNotifyEditFragment.KEY_MODEL, model);
        fragment.setArguments(bundle);
        start(fragment);
    }

    @Override
    public void smsMessageChangePreReturned() {
        confirmSecurity();
    }

    @Override
    public void securityFactorReturned() {
        CombinListBean factor = SecurityVerity.getInstance(getActivity())
                .getDefaultSecurityFactorId(model.getFactorModel());
        SecurityVerity.getInstance().setConversationId(model.getEditModel().getConversitionId());
        SecurityVerity.getInstance().setSecurityVerifyListener(this);
        model.setSelectedFactorId(factor.getId());
        confirmInfoView.updateSecurity(factor.getName());
    }

    private void confirmSecurity() {
        boolean isSecurity = SecurityVerity.getInstance().confirmFactor(model.getPreFactorList());
        if (isSecurity) {
            Activity activity = getActivity();
            EShieldVerify.getInstance(activity).setmPlainData(model.getmPlainData());
            SecurityVerity.getInstance().showSecurityDialog(model.getRandomNum());
        } else {
            showErrorDialog("您的交易环境存在风险，请排除风险后重试!");
        }
    }

    @Override
    public void smsMessageChangeReturned() {
        ResultFragment fragment = new ResultFragment();
        Bundle bundle = new Bundle();
        EditResultModel model = new EditResultModel(this.model);
        model.setHeadName("短信通知修改成功");
        bundle.putParcelable(SmsNotifyEditFragment.KEY_MODEL, model);
        fragment.setArguments(bundle);
        start(fragment);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unsubscribe();
    }

    @Override
    public void setPresenter(ConfirmEdieContact.Presenter presenter) {

    }

    @Override
    public void onSecurityTypeSelected(CombinListBean bean) {
        confirmInfoView.updateSecurity(bean.getName());
        model.setSelectedFactorId(bean.getId());
        isChangedSecurityFactor = true;
    }

    @Override
    public void onEncryptDataReturned(String factorId, String[] randomNums, String[] encryptPasswords) {
        model.setEncryptRandomNums(randomNums);
        model.setEncryptPasswords(encryptPasswords);
        isChangedSecurityFactor = true;

        if (isEdit) {
            presenter.psnSsmMessageChange();
        } else if (isFirstOpen) {
            presenter.psnSsmSign();
        } else {
            presenter.psnSsmMade();
        }
    }

    @Override
    public void onSignedReturn(String signRetData) {
        model.setmSignData(signRetData);
        isChangedSecurityFactor = true;

        if (isEdit) {
            presenter.psnSsmMessageChange();
        } else if (isFirstOpen) {
            presenter.psnSsmSign();
        } else {
            presenter.psnSsmMade();
        }
    }
}
