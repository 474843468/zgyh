package com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.ui;

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
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.phone.model.PhoneEditModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.model.QrcodeConfirmModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.model.QrcodeOperateModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.model.QrcodeTransModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.presenter.QrcodeConfirmPresenter;
import com.boc.bocsoft.mobile.bocmobile.yun.BocCloudCenter;
import com.boc.bocsoft.mobile.bocmobile.yun.other.AccountType;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.LinkedHashMap;

/**二维码转账确认界面
 * Created by wangtong on 2016/7/28.
 */
public class QrcodeConfirmFragment extends BussFragment implements SecurityVerity.VerifyCodeResultListener,
        QrcodeConfirmContract.View {

    private View rootView;
    private QrcodeConfirmModel model;
    private QrcodeConfirmPresenter presenter;
    private ConfirmInfoView confirm;
    private LinkedHashMap<String, String> confirmMap;
    private boolean isChangedSecurity = false;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_qrcode_confirm, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        confirm = (ConfirmInfoView) rootView.findViewById(R.id.confirmMsg);
    }

    @Override
    public void initData() {
        model = new QrcodeConfirmModel();
        model.setQrcodeTransModel((QrcodeTransModel) getArguments().getParcelable("model"));
        model.setFactorModel(PhoneEditModel.factorModel);
        model.setPrefactorList(PhoneEditModel.prefactorList);
        presenter = new QrcodeConfirmPresenter(this);

        String transHead = "转账金额" + "(" + PublicCodeUtils.getCurrency(mContext, model.getQrcodeTransModel().getTrfCurrency()) + ")";
        String transAmount = MoneyUtils.transMoneyFormat(model.getQrcodeTransModel().getTrfAmount(), model.getQrcodeTransModel().getTrfCurrency());
        model.getQrcodeTransModel().setTrfAmount(transAmount.replace(",", ""));
        confirm.setHeadValue(transHead, transAmount);

        String charge = model.getCommisionCharge();
        String commisionNum = "";
        if ("0".equals(charge)) {
            commisionNum = "0.00";
        } else if (StringUtils.isEmpty(charge)) {
            commisionNum = "0.00";
        } else {
            commisionNum = MoneyUtils.transMoneyFormat(model.getCommisionCharge(), model.getQrcodeTransModel().getTrfCurrency());
        }

        confirmMap = new LinkedHashMap<>();
        confirmMap.put("优惠后费用", commisionNum);
        confirmMap.put("收款人名称", model.getQrcodeTransModel().getPayeeName());
        confirmMap.put("收款账号", model.getQrcodeTransModel().getPayeeAccount());
//        if (!StringUtils.isEmpty(model.getQrcodeTransModel().getPayeeBankNum())) {
//            confirmMap.put("所属地区", PublicCodeUtils.getTransferIbk(getContext(), model.getQrcodeTransModel().getPayeeBankNum()));
//        }
        if (!StringUtils.isEmpty(model.getQrcodeTransModel().getPayeeMobile())) {
            confirmMap.put("收款人手机号", NumberUtils.formatMobileNumber(model.getQrcodeTransModel().getPayeeMobile()));
        }
        if (!StringUtils.isEmpty(model.getQrcodeTransModel().getTips())) {
            confirmMap.put("附言", model.getQrcodeTransModel().getTips());
        }
        confirmMap.put("付款账户", NumberUtils.formatCardNumber(model.getQrcodeTransModel().getPayerAccount()));
        confirm.addData(confirmMap);

        SecurityVerity.getInstance().setSecurityVerifyListener(this);
        confirm.updateSecurity(model.getQrcodeTransModel().getDefaultFactorName());
    }

    @Override
    public void setListener() {
        confirm.setListener(new ConfirmInfoView.OnClickListener() {

            @Override
            public void onClickConfirm() {
                if (isChangedSecurity) {
                    presenter.psnTransBocTransferVerify();
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
    public QrcodeConfirmModel getModel() {
        return model;
    }

    @Override
    public void psnAccountQueryAccountDetailReturned() {
        QrcodeOperateFragment fragment = new QrcodeOperateFragment();
        QrcodeOperateModel operateModel = new QrcodeOperateModel();
        operateModel.setPayeeAccount(model.getQrcodeTransModel().getPayeeAccount());
        operateModel.setPayeeMobel(model.getQrcodeTransModel().getPayeeMobile());
        operateModel.setPayeeName(model.getQrcodeTransModel().getPayeeName());
        operateModel.setTips(model.getQrcodeTransModel().getTips());
        operateModel.setFinalCommissionCharge(model.getFinalCommissionCharge().toString());
        operateModel.setTrfAmount(model.getQrcodeTransModel().getTrfAmount());
        operateModel.setFromAccountNum(model.getQrcodeTransModel().getPayerAccount());
        operateModel.setRemainAmount(model.getRemainAmount());
        operateModel.setRemainCurrency(model.getCurrencyCode());
        operateModel.setTransactionId(model.getTransactionId());
        operateModel.setFinalCommissionCharge(model.getFinalCommissionCharge().toString());
        operateModel.setFromIbkNum(model.getQrcodeTransModel().getPayeeBankNum());
        operateModel.setTransactionId(model.getTransactionId());
        operateModel.setRemainCurrency(model.getQrcodeTransModel().getTrfCurrency());
        Bundle bundle = new Bundle();
        bundle.putParcelable("model", operateModel);
        fragment.setArguments(bundle);
        start(fragment);
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

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.trans_phone_confirm_title);
    }

    @Override
    public void psnTransBocTransferVerifyReturned() {
        checkPreTradeStatus();
    }

    @Override
    public void psnTransBocTransferSubmitReturned() {
        if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(model.getQrcodeTransModel().getPayerAccountType())) {
            presenter.queryCreditAccountDetail(model.getQrcodeTransModel().getAccountId());
        } else if (ApplicationConst.ACC_TYPE_GRE.equals(model.getQrcodeTransModel().getPayerAccountType())) {
            presenter.queryCreditAccountDetail(model.getQrcodeTransModel().getAccountId());
        } else {
            presenter.psnAccountQueryAccountDetail();
        }
        // 保存转账成功用户id
        BocCloudCenter.getInstance().updateLastAccountId(AccountType.ACC_TYPE_QRCODETRANS,
                model.getQrcodeTransModel().getAccountId());
    }

    @Override
    public void queryCreditAccountDetailReturned() {
        QrcodeOperateFragment fragment = new QrcodeOperateFragment();
        QrcodeOperateModel operateModel = new QrcodeOperateModel();
        operateModel.setPayeeAccount(model.getQrcodeTransModel().getPayeeAccount());
        operateModel.setPayeeMobel(model.getQrcodeTransModel().getPayeeMobile());
        operateModel.setPayeeName(model.getQrcodeTransModel().getPayeeName());
        operateModel.setTips(model.getQrcodeTransModel().getTips());
        operateModel.setFinalCommissionCharge(model.getFinalCommissionCharge().toString());
        operateModel.setTrfAmount(model.getQrcodeTransModel().getTrfAmount());
        operateModel.setFromAccountNum(model.getQrcodeTransModel().getPayerAccount());
        operateModel.setRemainAmount(model.getRemainAmount());
        operateModel.setRemainCurrency(model.getCurrency());
        operateModel.setFinalCommissionCharge(model.getFinalCommissionCharge().toString());
        operateModel.setTransactionId(model.getTransactionId());
        operateModel.setRemainCurrency(model.getQrcodeTransModel().getTrfCurrency());
        Bundle bundle = new Bundle();
        bundle.putParcelable("model", operateModel);
        fragment.setArguments(bundle);
        start(fragment);
    }

    @Override
    public void onSecurityTypeSelected(CombinListBean bean) {
        confirm.updateSecurity(bean.getName());
        model.getQrcodeTransModel().setSelectedFactorId(bean.getId());
        isChangedSecurity = true;
    }

    @Override
    public void onEncryptDataReturned(String factorId, String[] randomNums, String[] encryptPasswords) {
        if (TextUtils.isEmpty(model.getQrcodeTransModel().getNeedPassword()) || model.getQrcodeTransModel().getNeedPassword().equals("0")) {
            model.setEncryptRandomNums(randomNums);
            model.setEncryptPasswords(encryptPasswords);
            presenter.psnTransBocTransferSubmit();
        } else if (factorId.equals(SecurityVerity.SECURITY_VERIFY_PASSWORD)) {
            model.setEncryptRandomNumsPass(randomNums);
            model.setEncryptPasswordsPass(encryptPasswords);
            presenter.psnTransBocTransferSubmit();
        } else {
            model.setEncryptRandomNums(randomNums);
            model.setEncryptPasswords(encryptPasswords);
            SecurityVerity.getInstance().showTransPasswordDialog(model.getQrcodeTransModel().getNeedPassword());
        }
    }

    @Override
    public void onSignedReturn(String signRetData) {
        model.setSignedData(signRetData);
        presenter.psnTransBocTransferSubmit();
    }

    @Override
    public void setPresenter(QrcodeConfirmContract.Presenter presenter) {

    }

    private void checkPreTradeStatus() {
        boolean ret = SecurityVerity.getInstance().confirmFactor(model.getPrefactorList());
        if (ret) {
            // 修改中银e盾输入密码报您已取消操作，因为Plaindata为空。对plaindate设置。lgw 2016年11月27日
            EShieldVerify.getInstance(getActivity()).setmPlainData(model.getmPlainData());

            SecurityVerity.getInstance().showSecurityDialog(model.getQrcodeTransModel().getRandomNum());
        } else {
            showErrorDialog("网络异常，请稍后再试");
        }
    }
}
