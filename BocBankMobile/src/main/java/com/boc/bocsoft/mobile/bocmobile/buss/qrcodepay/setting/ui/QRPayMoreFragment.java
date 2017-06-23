package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.recordquery.ui.QRPayPaymentRecordFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.model.PassFreeInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.model.PayQuotaViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.presenter.QRPayBasePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.widget.QRPayErrorDialog;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 二维码支付 - 更多
 * Created by wangf on 2016/8/22.
 */
public class QRPayMoreFragment extends BussFragment implements QRPayBaseContract.QRQueryPassFreeInfoBaseView{

    private View mRootView;

    private LinearLayout layout_qrpay_more;
    private EditChoiceWidget ecwSetPwd;
    private EditChoiceWidget ecwDefaultAccount;
    private EditChoiceWidget ecwFreePwd;
    private EditChoiceWidget ecwPayRecord;

    //账户信息
    private String firstDebitCardId;//用户第一张借记卡
    private String firstCreditCardId;//用户第一张信用卡

    private int currentRequestCard = -1;
    private boolean isDebitHaveAmount;//借记卡是否有限额数据
    private boolean isCreditHaveAmount;//信用卡是否有限额数据
    private QRPayBasePresenter mPassFreeInfoBasePresenter;

    //错误对话框
    private QRPayErrorDialog qrPayErrorDialog;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = View.inflate(mContext, R.layout.boc_fragment_qrpay_more, null);
        return mRootView;
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_qrpay_more);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    public void initView() {
        layout_qrpay_more = (LinearLayout) mRootView.findViewById(R.id.layout_qrpay_more);
        ecwSetPwd = (EditChoiceWidget) mRootView.findViewById(R.id.qrcode_pay_more_pwd);
        ecwDefaultAccount = (EditChoiceWidget) mRootView.findViewById(R.id.qrcode_pay_more_default_account);
        ecwFreePwd = (EditChoiceWidget) mRootView.findViewById(R.id.qrcode_pay_more_free_pwd);
        ecwPayRecord = (EditChoiceWidget) mRootView.findViewById(R.id.qrcode_pay_more_pay_record);

        ecwSetPwd.setBottomLineVisibility(true);
        ecwDefaultAccount.setBottomLineVisibility(true);
        ecwFreePwd.setBottomLineVisibility(true);
        ecwPayRecord.setBottomLineVisibility(true);
        ecwFreePwd.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        mPassFreeInfoBasePresenter = new QRPayBasePresenter(this);
        //默认账户
        if (QRPayMainFragment.isSetDefaultCard){
            ecwDefaultAccount.setChoiceTextContent(NumberUtils.formatCardNumberStrong(QRPayMainFragment.mDefaultAccountBean.getAccountNumber()));
        }else{
            ecwDefaultAccount.setChoiceTextContent("");
        }

        getFirstCardFreePass();

        showLoadingDialog();
        if (!StringUtils.isEmptyOrNull(firstDebitCardId)){
            currentRequestCard = 1;
            mPassFreeInfoBasePresenter.loadQRPayGetPassFreeInfo(firstDebitCardId);
        }else{
            currentRequestCard = 2;
            mPassFreeInfoBasePresenter.loadQRPayGetPassFreeInfo(firstCreditCardId);
        }
    }

    /**
     * 取出用户 第一张信用卡 和 第一张借记卡
     */
    private void getFirstCardFreePass(){
        ArrayList<String> accountTypeList = new ArrayList<String>();
        accountTypeList.add(ApplicationConst.ACC_TYPE_BRO);//借记卡
        List<AccountBean> accountBeen = QRPayMainFragment.getRelativeBankAccountList(accountTypeList);
        if (accountBeen.size() > 0){
            firstDebitCardId = accountBeen.get(0).getAccountId();
        }else{
            firstDebitCardId = "";
        }
        accountTypeList.clear();
        accountBeen.clear();
        accountTypeList.add(ApplicationConst.ACC_TYPE_ZHONGYIN);//中银系列信用卡
        accountTypeList.add(ApplicationConst.ACC_TYPE_GRE);//长城信用卡
        accountBeen = QRPayMainFragment.getRelativeBankAccountList(accountTypeList);
        if (accountBeen.size() > 0){
            firstCreditCardId = accountBeen.get(0).getAccountId();
        }else{
            firstCreditCardId = "";
        }
    }


    @Override
    public void setListener() {
        //支付密码
        ecwSetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(new QRPayChangePayPwdFragment());
            }
        });

        //默认支付账户
        ecwDefaultAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(new QRPayDefaultAccountFragment());
            }
        });

        //小额免密
        ecwFreePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isDebitHaveAmount", isDebitHaveAmount);
                bundle.putBoolean("isCreditHaveAmount", isCreditHaveAmount);
                QRPayFreePwdFragment fragment = new QRPayFreePwdFragment();
                fragment.setArguments(bundle);
                start(fragment);
            }
        });

        //支付记录
        ecwPayRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(new QRPayPaymentRecordFragment());
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    /**
     * 查询小额免密信息成功
     * @param infoViewModel
     */
    @Override
    public void loadQRPayGetPassFreeInfoSuccess(PassFreeInfoViewModel infoViewModel) {
        if (currentRequestCard == 1){//借记卡
            if("0".equals(infoViewModel.getPassFreeAmount())){
                isDebitHaveAmount = false;
            }else{
                isDebitHaveAmount = true;
                ecwFreePwd.setVisibility(View.VISIBLE);
            }

            if (!StringUtils.isEmptyOrNull(firstCreditCardId)){
                currentRequestCard = 2;
                mPassFreeInfoBasePresenter.loadQRPayGetPassFreeInfo(firstCreditCardId);
            }else{
                closeProgressDialog();
                layout_qrpay_more.setVisibility(View.VISIBLE);
            }

        }else if (currentRequestCard == 2){//信用卡
            if("0".equals(infoViewModel.getPassFreeAmount())){
                isCreditHaveAmount = false;
            }else{
                isCreditHaveAmount = true;
                ecwFreePwd.setVisibility(View.VISIBLE);
            }
            closeProgressDialog();
            layout_qrpay_more.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 查询小额免密信息失败
     * @param biiResultErrorException
     */
    @Override
    public void loadQRPayGetPassFreeInfoFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        showQrErrorDialog(biiResultErrorException.getErrorMessage());
    }


    /**
     * 查询支付限额接口 -- 此接口在此处不用
     * @param quotaViewModel
     */
    @Override
    public void loadQRPayGetPayQuotaSuccess(PayQuotaViewModel quotaViewModel) {

    }

    @Override
    public void loadQRPayGetPayQuotaFail(BiiResultErrorException biiResultErrorException) {

    }


    /**
     * 报错弹出框
     */
    private void showQrErrorDialog(String errorMessage) {
        qrPayErrorDialog = new QRPayErrorDialog(mContext);
        qrPayErrorDialog.setBtnText("确认");
        qrPayErrorDialog.setCancelable(false);
        qrPayErrorDialog.setErrorData(errorMessage);
        if (!qrPayErrorDialog.isShowing()) {
            qrPayErrorDialog.show();
        }
        qrPayErrorDialog.setOnBottomViewClickListener(new QRPayErrorDialog.OnBottomViewClickListener() {
            @Override
            public void onBottomViewClick() {
                titleLeftIconClick();
            }
        });
    }

}
