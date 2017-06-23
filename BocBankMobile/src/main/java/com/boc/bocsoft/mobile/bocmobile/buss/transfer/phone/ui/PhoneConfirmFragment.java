package com.boc.bocsoft.mobile.bocmobile.buss.transfer.phone.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.phone.model.PhoneConfirmModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.phone.model.PhoneEditModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.phone.model.PhoneOperateModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.phone.presenter.PhoneConfirmPresenter;
import com.boc.bocsoft.mobile.bocmobile.yun.BocCloudCenter;
import com.boc.bocsoft.mobile.bocmobile.yun.other.AccountType;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.LinkedHashMap;

/**
 * 手机号转账确认页面
 * Created by wangtong on 2016/7/27.
 */
public class PhoneConfirmFragment extends BussFragment implements PhoneConfirmContact.View,
        SecurityVerity.VerifyCodeResultListener {

    private View rootView;
    private PhoneConfirmModel model;
    private boolean isChangedSecurity = false;
    private boolean isInitData = true;
    private ConfirmInfoView confirmDetail;
    private PhoneEditModel editModel;
    private PhoneConfirmPresenter presenter;
    private LinkedHashMap<String, String> confirmMap;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_phone_confirm, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        confirmDetail = (ConfirmInfoView) rootView.findViewById(R.id.confirmMsg);
    }

    @Override
    public void initData() {
        model = new PhoneConfirmModel();
        presenter = new PhoneConfirmPresenter(this);
        editModel = getArguments().getParcelable("model");
        model.setPhoneEditModel(editModel);
        model.setFactorModel(PhoneEditModel.factorModel);
        model.setPrefactorList(PhoneEditModel.prefactorList);

        String transHead = "转账金额" + "(" + PublicCodeUtils.getCurrency(mContext, editModel.getRemainCurrency()) + ")";
        String transCount = MoneyUtils.transMoneyFormat(model.getPhoneEditModel().getTrfAmount(), model.getPhoneEditModel().getTrfCurrency());
        confirmDetail.setHeadValue(transHead, transCount);

        confirmMap = new LinkedHashMap<>();
        String value = "0.00";

        if ("1".equals(editModel.getIsHaveAccount())) {
            presenter.psnTransGetBocTransferCommissionCharge();
        } else {
            value = getResources().getString(R.string.boc_trans_without_account_notice);
            confirmDetail.setHint(getResources().getString(R.string.boc_trans_without_account), R.color.boc_black);
        }
        confirmMap.put("优惠后费用", value);
        confirmMap.put("收款人名称", editModel.getPayeeName());
        confirmMap.put("收款人手机号", NumberUtils.formatMobileNumber(editModel.getPayeeMobile()));
        if (!StringUtils.isEmpty(editModel.getTips())) {
            confirmMap.put("附言", editModel.getTips());
        }
        confirmMap.put("付款账户", NumberUtils.formatCardNumber(editModel.getPayerAccount()));
        confirmDetail.addData(confirmMap);

        SecurityVerity.getInstance().setSecurityVerifyListener(this);
        confirmDetail.updateSecurity(model.getPhoneEditModel().getDefaultFactorName());
    }


    @Override
    public void setListener() {
        confirmDetail.setListener(new ConfirmInfoView.OnClickListener() {

            @Override
            public void onClickConfirm() {
                if (isChangedSecurity) {
                    presenter.psnMobileTransferPre();
                } else {
                    checkPreTradeStatus();
                }
            }

            @Override
            public void onClickChange() {
                SecurityVerity.getInstance().selectSecurityType();
            }
        });
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.trans_phone_confirm_title);
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
    protected void titleLeftIconClick() {
        pop();
    }

    private void checkPreTradeStatus() {
        boolean ret = SecurityVerity.getInstance().confirmFactor(model.getPrefactorList());
        if (ret) {
            SecurityVerity.getInstance().showSecurityDialog(model.getPhoneEditModel().getRandomNum());
        } else {
            showErrorDialog("网络异常，请稍后再试");
        }
    }

    @Override
    public PhoneConfirmModel getModel() {
        return model;
    }

    @Override
    public void psnMobileTransferPreReturned() {
        //当isHaveAccount为0是则表示无绑定账户
        if (model.getPhoneEditModel().getIsHaveAccount().equals("0")) {
            confirmMap.get("优惠后费用").replace("0.00", getResources().getString(R.string.boc_trans_without_account));
            if (!isInitData) {
                checkPreTradeStatus();
            }
            isInitData = false;
        } else {
            presenter.psnTransGetBocTransferCommissionCharge();
        }
    }

    @Override
    public void psnTransGetBocTransferCommissionChargeReturned() {
        if (!isInitData) {
            checkPreTradeStatus();
            confirmMap.get("优惠后费用").replace("0.00", MoneyUtils.transMoneyFormat(model.getCommisionCharge(), "001"));
        }
        isInitData = false;
    }

    @Override
    public void psnAccountQueryAccountDetailReturned() {
        PhoneOperateFragment fragment = new PhoneOperateFragment();
        PhoneOperateModel operateModel = new PhoneOperateModel();
        operateModel.setFromAccoutnNum(model.getPhoneEditModel().getPayerAccount());
        operateModel.setFromAccoutnName(model.getPhoneEditModel().getPayerName());
        operateModel.setReminCurrency(model.getPhoneEditModel().getTrfCurrency());
        operateModel.setPayeeMobel(model.getPhoneEditModel().getPayeeMobile().trim().replaceAll(" ", ""));
        operateModel.setPayeeName(model.getPhoneEditModel().getPayeeName());
        operateModel.setTips(model.getPhoneEditModel().getTips());
        operateModel.setTrfAmount(model.getPhoneEditModel().getTrfAmount());
        operateModel.setIsBundAccount(model.getPhoneEditModel().getIsHaveAccount());
        operateModel.setReminAmount(model.getRemainAmount());
        operateModel.setTransNum(model.getTransNum());
        operateModel.setReminCurrency(model.getRemainCurrency());
        operateModel.setPayeeAccount(model.getPhoneEditModel().getPayeeAccountNum());
        operateModel.setPayeeLbk(model.getFromIbkNum());
        Bundle bundle = new Bundle();
        bundle.putParcelable("model", operateModel);
        fragment.setArguments(bundle);
        start(fragment);
    }

    @Override
    public void queryCreditAccountDetailReturned() {
        PhoneOperateFragment fragment = new PhoneOperateFragment();
        PhoneOperateModel operateModel = new PhoneOperateModel();
        operateModel.setFromAccoutnNum(model.getPhoneEditModel().getPayerAccount());
        operateModel.setFromAccoutnName(model.getPhoneEditModel().getPayerName());
        operateModel.setReminCurrency(model.getRemainCurrency());
        operateModel.setPayeeMobel(model.getPhoneEditModel().getPayeeMobile().trim().replaceAll(" ", ""));
        operateModel.setPayeeName(model.getPhoneEditModel().getPayeeName());
        operateModel.setTips(model.getPhoneEditModel().getTips());
        operateModel.setTrfAmount(model.getPhoneEditModel().getTrfAmount());
        operateModel.setIsBundAccount(model.getPhoneEditModel().getIsHaveAccount());
        operateModel.setReminAmount(model.getRemainAmount());
        operateModel.setTransNum(model.getTransNum());
        operateModel.setPayeeAccount(model.getPhoneEditModel().getPayeeAccountNum());
        Bundle bundle = new Bundle();
        bundle.putParcelable("model", operateModel);
        fragment.setArguments(bundle);
        start(fragment);
    }

    @Override
    public void psnMobileTransferSubmitReturned() {
        if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(model.getPhoneEditModel().getAccountType())) {
            presenter.queryCreditAccountDetail(model.getPhoneEditModel().getAccountId());
        } else if (ApplicationConst.ACC_TYPE_GRE.equals(model.getPhoneEditModel().getAccountType())) {
            presenter.queryCreditAccountDetail(model.getPhoneEditModel().getAccountId());
        } else {
            presenter.psnAccountQueryAccountDetail();
        }
        // 保存账户id
        BocCloudCenter.getInstance().updateLastAccountId(AccountType.ACC_TYPE_PHONETRANS,
                model.getPhoneEditModel().getAccountId());
    }


    @Override
    public void setPresenter(PhoneConfirmContact.Presenter presenter) {

    }

    @Override
    public void onSecurityTypeSelected(CombinListBean bean) {
        confirmDetail.updateSecurity(bean.getName());
        model.getPhoneEditModel().setSelectedFactorId(bean.getId());
        isChangedSecurity = true;
    }

    @Override
    public void onEncryptDataReturned(String factorId, String[] randomNums, String[] encryptPasswords) {
        if (TextUtils.isEmpty(model.getPhoneEditModel().getNeedPassword()) || model.getPhoneEditModel().getNeedPassword().equals("0")) {
            model.setEncryptRandomNums(randomNums);
            model.setEncryptPasswords(encryptPasswords);
            presenter.psnMobileTransferSubmit();
        } else if (factorId.equals(SecurityVerity.SECURITY_VERIFY_PASSWORD)) {
            model.setEncryptRandomNumsPass(randomNums);
            model.setEncryptPasswordsPass(encryptPasswords);
            presenter.psnMobileTransferSubmit();
        } else {
            model.setEncryptRandomNums(randomNums);
            model.setEncryptPasswords(encryptPasswords);
            SecurityVerity.getInstance().showTransPasswordDialog(model.getPhoneEditModel().getNeedPassword());
        }
    }

    @Override
    public void onSignedReturn(String signRetData) {
        model.setmSignData(signRetData);
        presenter.psnMobileTransferSubmit();
    }
}
