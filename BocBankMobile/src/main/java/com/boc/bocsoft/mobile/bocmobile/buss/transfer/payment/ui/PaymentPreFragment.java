package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.ui;

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
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.model.ConfirmPaymentModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.model.PaymentOperatorModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.model.PaymentPreModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.presenter.PaymentPrPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.LinkedHashMap;

/**
 * 付款确认界面
 * Created by wangtong on 2016/6/29.
 */
public class PaymentPreFragment extends BussFragment implements SecurityVerity.VerifyCodeResultListener,
        PaymentPreContact.View {
    private View rootView;
    //数据模型
    private PaymentPreModel model;
    private PaymentPrPresenter presenter;
    //是否需要预交易
    private boolean isStartTransfer = false;
    //预交易结果
    private boolean preTransferRet = false;
    private ConfirmInfoView preMsg;
    private LinkedHashMap<String, String> confirmMap;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = View.inflate(mContext, R.layout.boc_fragment_payment_pre, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        preMsg = (ConfirmInfoView) rootView.findViewById(R.id.preMsg);
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.transout_confirm_title);
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
    public void initData() {
        super.initData();
        ConfirmPaymentModel paymentModel = getArguments().getParcelable("model");
        model = new PaymentPreModel();
        presenter = new PaymentPrPresenter(this);
        model.transFromPaymentModel(paymentModel);
        updateViews();
        presenter.psnGetSecurityFactor();
    }

    private void updateViews() {
        String headKey = "实付金额" + "(" + PublicCodeUtils.getCurrency(mContext, model.getTrfCur()) + ")";
        String headValue = MoneyUtils.transMoneyFormat(model.getTrfAmount(), model.getTrfCur());
        preMsg.setHeadValue(headKey, headValue);
        confirmMap = new LinkedHashMap<>();
        if ("0".equals(model.getDiscountAmount())) {
            confirmMap.put("优惠后费用", "0.00");
        } else if (StringUtils.isEmpty(model.getDiscountAmount())) {
            confirmMap.put("优惠后费用", "0.00");
        } else {
            confirmMap.put("优惠后费用", MoneyUtils.transMoneyFormat(model.getDiscountAmount(), model.getTrfCur()));
        }
        confirmMap.put("收款人名称", model.getPayeeName());
        confirmMap.put("收款账号", model.getPayeeAccountNumber());
        confirmMap.put("收款人手机号", NumberUtils.formatMobileNumber(model.getPayeeMobile()));
        confirmMap.put("指令序号", model.getNotifyId());
        confirmMap.put("收款金额", MoneyUtils.transMoneyFormat(model.getRequestAmount(), model.getTrfCur()));
        // 附言不为空才显示
        if (!StringUtils.isEmpty(model.getFurInfo())) {
            confirmMap.put("附言", model.getFurInfo());
        }
        confirmMap.put("付款人名称", model.getPayerName());
        confirmMap.put("付款账号", NumberUtils.formatCardNumber(model.getPayerAccountNumber()));
        confirmMap.put("付款人手机号", NumberUtils.formatMobileNumber(model.getPayerMobile()));
        preMsg.addData(confirmMap);
    }

    @Override
    public void setListener() {
        preMsg.setListener(new ConfirmInfoView.OnClickListener() {
            @Override
            public void onClickConfirm() {
                if (preTransferRet) {
                    SecurityVerity.getInstance().showSecurityDialog(model.getRandomNum());
                } else {
                    presenter.psnTransActPaymentVerify();
                }
            }

            @Override
            public void onClickChange() {
                SecurityVerity.getInstance().selectSecurityType();
            }
        });
    }


    @Override
    public PaymentPreModel getModel() {
        return model;
    }

    @Override
    public void psnTransActPaymentVerifyReturned() {
        preTransferRet = SecurityVerity.getInstance().confirmFactor(model.getPrefactorList());
        if (preTransferRet && isStartTransfer) {
            if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(model.getPayeeAccountType())) {
                presenter.queryCreditAccountDetail(model.getPayerAccountId());
            } else if (ApplicationConst.ACC_TYPE_GRE.equals(model.getPayeeAccountType())) {
                presenter.queryCreditAccountDetail(model.getPayerAccountId());
            } else {
                presenter.psnAccountQueryAccountDetail();
            }
        } else if (!preTransferRet) {
            showErrorDialog("预交易失败");
        }
        isStartTransfer = true;
    }

    @Override
    public void securityFactorReturned() {

        CombinListBean factorBean = SecurityVerity.getInstance(getActivity())
                .getDefaultSecurityFactorId(model.getFactorModel());
        SecurityVerity.getInstance().setSecurityVerifyListener(this);
        preMsg.updateSecurity(factorBean.getName());
        model.setSelectedFactorId(factorBean.getId());
        presenter.psnTransActPaymentVerify();
    }

    @Override
    public void psnTransActPaymentSubmitReturned() {
        isTransSucc = true;
        if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(model.getPayeeAccountType())) {
            presenter.queryCreditAccountDetail(model.getPayerAccountId());
        } else if (ApplicationConst.ACC_TYPE_GRE.equals(model.getPayeeAccountType())) {
            presenter.queryCreditAccountDetail(model.getPayerAccountId());
        } else {
            presenter.psnAccountQueryAccountDetail();
        }
    }

    private boolean isTransSucc = false;//转账成功标识，用于请求余额后跳转

    @Override
    public void psnAccountQueryAccountDetailReturned() {
        if (isTransSucc) {//转账成功的请求余额，跳转界面
            jumpToTransSucc();
        } else {
            SecurityVerity.getInstance().showSecurityDialog(model.getRandomNum());
        }
    }

    /**
     * 跳转到转账成功页面
     */
    private void jumpToTransSucc() {
        PaymentOperateFragment fragment = new PaymentOperateFragment();
        Bundle bundle = new Bundle();
        PaymentOperatorModel operatorModel = new PaymentOperatorModel();
        operatorModel.setOperatorTitle(MoneyUtils.transMoneyFormat(model.getTrfAmount(), model.getTrfCur()) + "元交易成功");
        operatorModel.setDiscountAmount(model.getDiscountAmount());
        operatorModel.setTransferNum(model.getTransferNum());
        operatorModel.setTrfCur(model.getTrfCur());
        operatorModel.setTransAmount(model.getTrfAmount());
        operatorModel.setPayeeName(model.getPayeeName());
        operatorModel.setPayeeMobel(model.getPayeeMobile());
        operatorModel.setRemainAmount(model.getRemainAmount());
        operatorModel.setPayeeLbk(model.getPayeeIbk());
        operatorModel.setPayerAccount(model.getPayerAccountNumber());
        operatorModel.setPayerName(model.getPayerName());
        //收款账号
        operatorModel.setPayeeAccNum(model.getPayeeAccountNumber());
        //指令序号
        operatorModel.setNotifyId(model.getNotifyId());
        bundle.putParcelable("model", operatorModel);
        fragment.setArguments(bundle);
        start(fragment);
    }

    @Override
    public void queryCreditAccountDetailReturned() {
        if (isTransSucc) {//转账成功后的请求余额，跳转界面
            jumpToTransSucc();
        } else {
            SecurityVerity.getInstance().showSecurityDialog(model.getRandomNum());
        }
    }

    @Override
    public void setPresenter(PaymentPreContact.Presenter presenter) {

    }

    @Override
    public void onSecurityTypeSelected(CombinListBean bean) {
        preMsg.updateSecurity(bean.getName());
        model.setSelectedFactorId(bean.getId());
        preTransferRet = false;
    }

    @Override
    public void onEncryptDataReturned(String factorId, String[] randomNums, String[] encryptPasswords) {
        if (TextUtils.isEmpty(model.getNeedPassword()) || model.getNeedPassword().equals("0")) {
            model.setEncryptRandomNums(randomNums);
            model.setEncryptPasswords(encryptPasswords);
            presenter.psnTransActPaymentSubmit();
        } else if (factorId.equals(SecurityVerity.SECURITY_VERIFY_PASSWORD)) {
            model.setEncryptRandomNumsPass(randomNums);
            model.setEncryptPasswordsPass(encryptPasswords);
            presenter.psnTransActPaymentSubmit();
        } else {
            model.setEncryptRandomNums(randomNums);
            model.setEncryptPasswords(encryptPasswords);
            SecurityVerity.getInstance().showTransPasswordDialog(model.getNeedPassword());
        }
    }

    @Override
    public void onSignedReturn(String signRetData) {
        model.setmPlainData(signRetData);
        presenter.psnTransActPaymentSubmit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
