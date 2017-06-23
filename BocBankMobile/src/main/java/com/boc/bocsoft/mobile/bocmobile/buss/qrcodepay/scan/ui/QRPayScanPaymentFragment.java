package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.scan.ui;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.base.widget.moneyinputview.MoneyInputTextView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.scan.model.QRCodeContentBean;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.scan.model.QRPayScanPaymentViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.scan.presenter.QRPayScanPaymentPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.model.PassFreeInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.model.PayQuotaViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.presenter.QRPayBasePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui.QRPayBaseContract;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui.QRPayMainFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui.QRPayResetPayPwdFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.utils.QrCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.widget.QRPayPwdDialog;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 二维码支付 - 付款 - 正扫 - 扫码成功后的付款
 * Created by wangf on 2016/8/23.
 */
public class QRPayScanPaymentFragment extends BussFragment implements QRPayScanPaymentContract.QRPayScanPaymentView,
        QRPayBaseContract.QrAccountBaseView, QRPayBaseContract.QrSetCardBaseView, QRPayBaseContract.QRQueryPassFreeInfoBaseView{

    private View mRootView;

    //商户名称的Layout（无图标）
    private LinearLayout llPaymentNameNO;
    //商户名称（无图标）
    private TextView tvPaymentNameNO;
    //金额输入
    private MoneyInputTextView mtvInputMoney;
    //确认按钮
    private Button btnPaymentConfirm;
    //支付账户
    private TextView tvPaymentAccount;
    //修改支付账户
    private TextView tvPaymentAccountChange;
    //可用余额的layout
    private LinearLayout llQrpayScanPaymentAvailable;
    //可用余额的TextView
    private TextView tvQrpayScanPaymentAvailable;


    //二维码数据
    private QRCodeContentBean qrCodeContentBean;
    private QRPayScanPaymentViewModel mPayScanPaymentViewModel;//正扫支付返回数据
    private QRPayScanPaymentPresenter mPayScanPaymentPresenter;
    private QRPayBasePresenter mPassFreeInfoBasePresenter;
    private QRPayBasePresenter mPayBasePresenter;
    private QRPayBasePresenter mSetCardBasePresenter;

    //支付限额
    public PayQuotaViewModel mPayQuotaViewModel;
    //小额免密信息
    public PassFreeInfoViewModel mPassFreeInfoViewModel;

    //选择的AccountBean
    private AccountBean selectPayAccount;
    private boolean isSelectAccount;
    //可用余额
    private BigDecimal mAvailableBalance;

    // 选择账户页面选中的账户
    private Bundle accountBundle;
    private SelectAccoutFragment selectAccoutFragment;

    //密码输入错误提示框
    private TitleAndBtnDialog mQrPwdErrorDialog;


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = View.inflate(mContext, R.layout.boc_fragment_qrpay_scan_payment, null);
        qrCodeContentBean = getArguments().getParcelable(QRPayScanFragment.C2B_QRCODE_CONTENT);
        return mRootView;
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_qrpay_payment);
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
        llPaymentNameNO = (LinearLayout) mRootView.findViewById(R.id.ll_qrpay_scan_payment_name1);
        tvPaymentNameNO = (TextView) mRootView.findViewById(R.id.tv_qrpay_scan_payment_name);
        mtvInputMoney = (MoneyInputTextView) mRootView.findViewById(R.id.mtv_qrpay_scan_payment_money);
        btnPaymentConfirm = (Button) mRootView.findViewById(R.id.btn_qrpay_scan_payment_confirm);
        tvPaymentAccount = (TextView) mRootView.findViewById(R.id.tv_qrpay_scan_payment_account);
        tvPaymentAccountChange = (TextView) mRootView.findViewById(R.id.tv_qrpay_scan_payment_account_change);
        llQrpayScanPaymentAvailable = (LinearLayout) mRootView.findViewById(R.id.ll_qrpay_scan_payment_available);
        tvQrpayScanPaymentAvailable = (TextView) mRootView.findViewById(R.id.tv_qrpay_scan_payment_available);
    }

    @Override
    public void initData() {
        mPayScanPaymentViewModel = new QRPayScanPaymentViewModel();
        mPayScanPaymentPresenter = new QRPayScanPaymentPresenter(this);
        mPayBasePresenter = new QRPayBasePresenter((QRPayBaseContract.QrAccountBaseView)this);
        mSetCardBasePresenter = new QRPayBasePresenter((QRPayBaseContract.QrSetCardBaseView)this);
        mPassFreeInfoBasePresenter = new QRPayBasePresenter((QRPayBaseContract.QRQueryPassFreeInfoBaseView)this);

        mPayQuotaViewModel = new PayQuotaViewModel();
        mPassFreeInfoViewModel = new PassFreeInfoViewModel();

        selectPayAccount = QRPayMainFragment.mDefaultAccountBean;

        mtvInputMoney.setMaxLeftNumber(10);
        mtvInputMoney.setMaxRightNumber(2);
        
        SpannableString ss = new SpannableString("请输入");
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(15, true);
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mtvInputMoney.setHint(new SpannableString(ss));
        
        tvPaymentNameNO.setText("向  " + qrCodeContentBean.getN() + " 付款");
        tvPaymentAccount.setText(selectPayAccount.getNickName() + "("+QrCodeUtils.getShortCardNum(selectPayAccount.getAccountNumber())+")");


        //信用卡不需要查询余额信息，借记卡需要查询余额信息
        String accountType = selectPayAccount.getAccountType();
        if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(accountType) ||
                ApplicationConst.ACC_TYPE_GRE.equals(accountType)) {
            llQrpayScanPaymentAvailable.setVisibility(View.GONE);
//            //信用卡账户详情 -- 不需查询
//            showLoadingDialog();
//            isSelectAccount = false;
//            mPayBasePresenter.queryCreditAccountDetail(QRPayMainFragment.mDefaultAccountBean.getAccountId(), ApplicationConst.CURRENCY_CNY);
        } else if (ApplicationConst.ACC_TYPE_BRO.equals(accountType)) {
            //借记卡账户详情
            showLoadingDialog();
            isSelectAccount = false;
            mPayBasePresenter.queryAccountDetails(selectPayAccount.getAccountId());
        }


    }

    @Override
    public void setListener() {
        //确认
        btnPaymentConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	if(StringUtils.isEmpty(mtvInputMoney.getInputMoney())){
            		showErrorDialog("付款金额不能为空");
            		return;
            	}
            	if("0".equals(mtvInputMoney.getInputMoney())){
            		showErrorDialog("付款金额不能为0");
            		return;
            	}
            	if("0.00".equals(mtvInputMoney.getInputMoney())){
            		showErrorDialog("付款金额不能为0");
            		return;
            	}
            	
                showLoadingDialog();
                //查询支付限额信息
                mPassFreeInfoBasePresenter.loadQRPayGetPayQuota(selectPayAccount.getAccountId(), "01");
            }
        });

        //更改账户
        tvPaymentAccountChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePayAccount();
            }
        });
    }
    
//    @Override
//    protected void titleLeftIconClick() {
//        popToAndReInit(QRPayScanFragment.class);
//    }


//    @Override
//    public boolean onBack() {
//        popToAndReInit(QRPayScanFragment.class);
//        return false;
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SecurityVerity.getInstance(getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    /**
     * 更改默认支付账户
     */
    private void changePayAccount(){
        ArrayList<String> accountTypeList = new ArrayList<String>();
        accountTypeList.add(ApplicationConst.ACC_TYPE_BRO);//借记卡
        accountTypeList.add(ApplicationConst.ACC_TYPE_ZHONGYIN);//中银系列信用卡
        accountTypeList.add(ApplicationConst.ACC_TYPE_GRE);//长城信用卡
        List<AccountBean> accountBeansList = QRPayMainFragment.getRelativeBankAccountList(accountTypeList);
        selectAccoutFragment = new SelectAccoutFragment().newInstanceWithData((ArrayList) accountBeansList);
//        selectAccoutFragment = new SelectAccoutFragment().newInstance(accountTypeList);
        selectAccoutFragment.isRequestNet(true);
        startForResult(selectAccoutFragment, SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT);
        selectAccoutFragment.setOnItemListener(new SelectAccoutFragment.ItemListener() {
            @Override
            public void onItemClick(Bundle bundle) {
                accountBundle = bundle;
                AccountBean accountBean = bundle.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
                String accountType = accountBean.getAccountType();
                if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(accountType) ||
                        ApplicationConst.ACC_TYPE_GRE.equals(accountType)){
                    //信用卡不需要查询余额信息，直接获取账户信息后返回
                    mAvailableBalance = null;
                    llQrpayScanPaymentAvailable.setVisibility(View.GONE);
                    selectAccoutFragment.setFramgentResult(SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT, accountBundle);
                    selectAccoutFragment.pop();
//                    //信用卡账户详情
//                    showLoadingDialog();
//                    mPayBasePresenter.queryCreditAccountDetail(accountBean.getAccountId(), ApplicationConst.CURRENCY_CNY);
                }else if (ApplicationConst.ACC_TYPE_BRO.equals(accountType)){
                    //借记卡需要查询余额信息
                    showLoadingDialog();
                    mAvailableBalance = null;
                    isSelectAccount = true;
                    mPayBasePresenter.queryAccountDetails(accountBean.getAccountId());
                }
            }
        });
    }


    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if (resultCode == SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT){
            selectPayAccount = data.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
            tvPaymentAccount.setText(selectPayAccount.getNickName() + "("+QrCodeUtils.getShortCardNum(selectPayAccount.getAccountNumber())+")");
            if (mAvailableBalance != null){
                llQrpayScanPaymentAvailable.setVisibility(View.VISIBLE);
                tvQrpayScanPaymentAvailable.setText(MoneyUtils.transMoneyFormat(mAvailableBalance, ApplicationConst.CURRENCY_CNY));
            }else{
                llQrpayScanPaymentAvailable.setVisibility(View.GONE);
            }
        }
    }



    /**
     * 封装页面数据
     * @param encryptRandomNum
     * @param encryptPassword
     * @return
     */
    private QRPayScanPaymentViewModel buildQRPayDoPaymentViewModel(String encryptRandomNum, String encryptPassword){
        QRPayScanPaymentViewModel paymentViewModel = new QRPayScanPaymentViewModel();
        paymentViewModel.setActSeq(selectPayAccount.getAccountId());
        paymentViewModel.setMerchantNo(qrCodeContentBean.getM());
        paymentViewModel.setMerchantName(qrCodeContentBean.getN());
        paymentViewModel.setTerminalId(qrCodeContentBean.getT());
        paymentViewModel.setPassword(encryptPassword);
        paymentViewModel.setPassword_RC(encryptRandomNum);
        paymentViewModel.setTranAmount(mtvInputMoney.getInputMoney());
        paymentViewModel.setPassType("01");
        paymentViewModel.setState(SecurityVerity.SECURITY_VERIFY_STATE);
        paymentViewModel.setActiv(SecurityVerity.getInstance().getCfcaVersion());

        return paymentViewModel;
    }

    /**
     * 用户输入密码 进行正扫支付
     * @param random
     */
    private void handleConfirmInfo(String random){
        QRPayPwdDialog qrPayPwdDialog = new QRPayPwdDialog(getActivity());
        qrPayPwdDialog.setDialogData("向  " + qrCodeContentBean.getN() + " 付款", mtvInputMoney.getInputMoney(), random);
        qrPayPwdDialog.setQRPayDialogListener(new QRPayPwdDialog.QRPayPwdDialogCallBack() {
            @Override
            public void onCancel() {
                Log.i("wf", "onCancel");
            }

            @Override
            public void onNumCompleted(String encryptRandomNum, String encryptPassword, String mVersion) {
                showLoadingDialog();
                //正扫支付
                mPayScanPaymentPresenter.loadQRPayDoPayment(buildQRPayDoPaymentViewModel(encryptRandomNum, encryptPassword));
            }

            @Override
            public void onErrorMessage(boolean isShow) {
                Log.i("wf", "onErrorMessage");
            }

            @Override
            public void onCompleteClicked(String inputString) {
                Log.i("wf", "onInputError");
            }
        });
        qrPayPwdDialog.show();
    }


    @Override
    public void loadGetRandomSuccess(String random) {
        closeProgressDialog();
        handleConfirmInfo(random);
    }

    @Override
    public void loadGetRandomFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /*** 正扫支付成功 */
    @Override
    public void loadQRPayDoPaymentSuccess(QRPayScanPaymentViewModel viewModel) {
        mPayScanPaymentViewModel = viewModel;
        if (QRPayMainFragment.isSetDefaultCard){
            closeProgressDialog();
            QRPayScanResultFragment fragment = new QRPayScanResultFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("ScanPaymentViewModel", mPayScanPaymentViewModel);
            bundle.putParcelable("ContentBean", qrCodeContentBean);
            bundle.putString("AccountNum", selectPayAccount.getAccountNumber());
            bundle.putString("InputMoney", mtvInputMoney.getInputMoney());
            fragment.setArguments(bundle);
            start(fragment);
        }else{
            showLoadingDialog();
            //设置默认卡
            mSetCardBasePresenter.loadQRPaySetDefaultCard(selectPayAccount.getAccountId());
        }
    }

    /*** 正扫支付失败 */
    @Override
    public void loadQRPayDoPaymentFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        if ("qrpassword.wrong".equals(biiResultErrorException.getErrorCode())){
            showPwdErrorDialog();
        }else {
            showErrorDialog(biiResultErrorException.getErrorMessage());
        }
    }

    /*** 查询账户详情成功 */
    @Override
    public void queryAccountDetailsSuccess(BigDecimal availableBalance) {
        closeProgressDialog();
        mAvailableBalance = availableBalance;
        if (isSelectAccount) {
            selectAccoutFragment.setFramgentResult(SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT, accountBundle);
            selectAccoutFragment.pop();
        }else{
            if (mAvailableBalance != null){
                llQrpayScanPaymentAvailable.setVisibility(View.VISIBLE);
                tvQrpayScanPaymentAvailable.setText(MoneyUtils.transMoneyFormat(mAvailableBalance, ApplicationConst.CURRENCY_CNY));
            }else{
                llQrpayScanPaymentAvailable.setVisibility(View.GONE);
            }
        }
    }

    /*** 查询账户详情失败 */
    @Override
    public void queryAccountDetailsFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        llQrpayScanPaymentAvailable.setVisibility(View.GONE);
        if (isSelectAccount) {
            selectAccoutFragment.setFramgentResult(SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT, accountBundle);
            selectAccoutFragment.pop();
        }
    }

    /*** 查询信用卡账户详情成功 */
    @Override
    public void queryCreditAccountDetailSuccess(BigDecimal availableBalance) {
        closeProgressDialog();
        selectAccoutFragment.setFramgentResult(SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT, accountBundle);
        selectAccoutFragment.pop();
    }

    /*** 查询信用卡账户详情失败 */
    @Override
    public void queryCreditAccountDetailFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /**
     * 设置默认卡成功
     */
    @Override
    public void loadQRPaySetDefaultCardSuccess() {
        closeProgressDialog();
        QRPayMainFragment.mDefaultAccountBean = selectPayAccount;
        QRPayMainFragment.isSetDefaultCard = true;

        QRPayScanResultFragment fragment = new QRPayScanResultFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("ScanPaymentViewModel", mPayScanPaymentViewModel);
        bundle.putParcelable("ContentBean", qrCodeContentBean);
        bundle.putString("AccountNum", selectPayAccount.getAccountNumber());
        bundle.putString("InputMoney", mtvInputMoney.getInputMoney());
        fragment.setArguments(bundle);
        start(fragment);
    }

    /**
     * 设置默认卡失败
     * @param biiResultErrorException
     */
    @Override
    public void loadQRPaySetDefaultCardFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        QRPayMainFragment.mDefaultAccountBean = selectPayAccount;
        QRPayMainFragment.isSetDefaultCard = true;

        QRPayScanResultFragment fragment = new QRPayScanResultFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("ScanPaymentViewModel", mPayScanPaymentViewModel);
        bundle.putParcelable("ContentBean", qrCodeContentBean);
        bundle.putString("AccountNum", selectPayAccount.getAccountNumber());
        bundle.putString("InputMoney", mtvInputMoney.getInputMoney());
        fragment.setArguments(bundle);
        start(fragment);
    }

    /**
     * 查询小额免密信息成功
     * @param infoViewModel
     */
    @Override
    public void loadQRPayGetPassFreeInfoSuccess(PassFreeInfoViewModel infoViewModel) {
        mPassFreeInfoViewModel = infoViewModel;

        //606
        // 小额免密开关状态 0：未开通 1：已开通
        if ("1".equals(mPassFreeInfoViewModel.getPassFreeFlag())){
            if(QrCodeUtils.isCompareAmountCanNext(mtvInputMoney.getInputMoney(),
                    QrCodeUtils.getFormatTransQuota(mPassFreeInfoViewModel.getPassFreeAmount()))){
                //正扫支付
                mPayScanPaymentPresenter.loadQRPayDoPaymentFreePass(buildQRPayDoPaymentViewModel("", ""));
            }else{
                //查询随机数
                mPayScanPaymentPresenter.loadGetRandom();
            }

        }else if("0".equals(mPassFreeInfoViewModel.getPassFreeFlag())) {
            //查询随机数
            mPayScanPaymentPresenter.loadGetRandom();
        }

        // X610
//        String accountType = selectPayAccount.getAccountType();
//        if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(accountType) ||
//                ApplicationConst.ACC_TYPE_GRE.equals(accountType)  ||
//                ApplicationConst.ACC_TYPE_SINGLEWAIBI.equals(accountType)){
//            // 小额免密开关状态 0：未开通 1：已开通
//            if ("1".equals(mPassFreeInfoViewModel.getCreditCardFlag())){
//            	if(QrCodeUtils.isCompareAmountCanNext(mtvInputMoney.getInputMoney(),
//            			QrCodeUtils.getFormatTransQuota(mPassFreeInfoViewModel.getCreditCardPassFreeAmount()))){
//            		//正扫支付
//                    mPayScanPaymentPresenter.loadQRPayDoPaymentFreePass(buildQRPayDoPaymentViewModel("", ""));
//            	}else{
//            		//查询随机数
//                    mPayScanPaymentPresenter.loadGetRandom();
//            	}
//
//            }else if("0".equals(mPassFreeInfoViewModel.getCreditCardFlag())) {
//                //查询随机数
//                mPayScanPaymentPresenter.loadGetRandom();
//            }
//
//        }else if (ApplicationConst.ACC_TYPE_BRO.equals(accountType)){
//            // 小额免密开关状态 0：未开通 1：已开通
//            if ("1".equals(mPassFreeInfoViewModel.getDebitCardFlag())){
//            	if(QrCodeUtils.isCompareAmountCanNext(mtvInputMoney.getInputMoney(),
//            			QrCodeUtils.getFormatTransQuota(mPassFreeInfoViewModel.getDebitCardPassFreeAmount()))){
//            		//正扫支付
//                    mPayScanPaymentPresenter.loadQRPayDoPaymentFreePass(buildQRPayDoPaymentViewModel("", ""));
//            	}else{
//            		//查询随机数
//                    mPayScanPaymentPresenter.loadGetRandom();
//            	}
//            }else if("0".equals(mPassFreeInfoViewModel.getDebitCardFlag())) {
//                //查询随机数
//                mPayScanPaymentPresenter.loadGetRandom();
//            }
//        }
    }

    /**
     * 查询小额免密信息失败
     * @param biiResultErrorException
     */
    @Override
    public void loadQRPayGetPassFreeInfoFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        showErrorDialog(biiResultErrorException.getErrorMessage());
    }

    /***
     * 查询支付限额成功
     */
    @Override
    public void loadQRPayGetPayQuotaSuccess(PayQuotaViewModel quotaViewModel) {
        mPayQuotaViewModel = quotaViewModel;

        //606
        String accountType = selectPayAccount.getAccountType();
        if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(accountType) || ApplicationConst.ACC_TYPE_GRE.equals(accountType)){
            if (QrCodeUtils.isCompareAmountCanNext(mtvInputMoney.getInputMoney(), QrCodeUtils.getFormatTransQuota(mPayQuotaViewModel.getCardTransQuota()))){
                //查询小额免密信息
                mPassFreeInfoBasePresenter.loadQRPayGetPassFreeInfo(selectPayAccount.getAccountId());
            }else {
                closeProgressDialog();
                showErrorDialog("此账户单笔付款限额为" + MoneyUtils.transMoneyFormat(
                        QrCodeUtils.getFormatTransQuotaNoSign(mPayQuotaViewModel.getCardTransQuota()), ApplicationConst.CURRENCY_CNY)+ "元，请重新输入");
            }
        }else if (ApplicationConst.ACC_TYPE_BRO.equals(accountType)){
            if (QrCodeUtils.isCompareAmountCanNext(mtvInputMoney.getInputMoney(), QrCodeUtils.getFormatTransQuota(mPayQuotaViewModel.getCardPayQuota()))){
                //查询小额免密信息
                mPassFreeInfoBasePresenter.loadQRPayGetPassFreeInfo(selectPayAccount.getAccountId());
            }else {
                closeProgressDialog();
                showErrorDialog("此账户单笔付款限额为" + MoneyUtils.transMoneyFormat(
                        QrCodeUtils.getFormatTransQuotaNoSign(mPayQuotaViewModel.getCardPayQuota()), ApplicationConst.CURRENCY_CNY)+ "元，请重新输入");
            }
        }

//        if (QrCodeUtils.isCompareAmountCanNext(mtvInputMoney.getInputMoney(), QrCodeUtils.getFormatTransQuota(mPayQuotaViewModel.getCardTransQuota()))){
//            //查询小额免密信息
//            mPassFreeInfoBasePresenter.loadQRPayGetPassFreeInfo(selectPayAccount.getAccountId());
//        }else {
//            closeProgressDialog();
//            showErrorDialog("此账户单笔付款限额为" + MoneyUtils.transMoneyFormat(
//                    QrCodeUtils.getFormatTransQuotaNoSign(mPayQuotaViewModel.getCardTransQuota()), ApplicationConst.CURRENCY_CNY)+ "元，请重新输入");
//        }

        // X610
//        String accountType = selectPayAccount.getAccountType();
//        if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(accountType) ||
//                ApplicationConst.ACC_TYPE_GRE.equals(accountType)  ||
//                ApplicationConst.ACC_TYPE_SINGLEWAIBI.equals(accountType)){
//
//            if (QrCodeUtils.isCompareAmountCanNext(mtvInputMoney.getInputMoney(), QrCodeUtils.getFormatTransQuota(mPayQuotaViewModel.getCreditCardTransQuota()))){
//                //查询小额免密信息
//                mPassFreeInfoBasePresenter.loadQRPayGetPassFreeInfo(selectPayAccount.getAccountId());
//            }else {
//                closeProgressDialog();
//                showErrorDialog("此账户单笔付款限额为" + QrCodeUtils.getFormatTransQuotaNoSign(mPayQuotaViewModel.getCreditCardTransQuota()) + "元，请重新输入" );
//            }
//
//        }else if (ApplicationConst.ACC_TYPE_BRO.equals(accountType)){
//            if (QrCodeUtils.isCompareAmountCanNext(mtvInputMoney.getInputMoney(), QrCodeUtils.getFormatTransQuota(mPayQuotaViewModel.getDebitCardTransQuota()))){
//                //查询小额免密信息
//                mPassFreeInfoBasePresenter.loadQRPayGetPassFreeInfo(selectPayAccount.getAccountId());
//            }else {
//                closeProgressDialog();
//                showErrorDialog("此账户单笔付款限额为" + QrCodeUtils.getFormatTransQuotaNoSign(mPayQuotaViewModel.getDebitCardTransQuota()) + "元，请重新输入");
//            }
//        }
    }

    /**
     * 查询支付限额失败
     */
    @Override
    public void loadQRPayGetPayQuotaFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        showErrorDialog(biiResultErrorException.getErrorMessage());
    }


    /**
     * 显示密码输入错误对话框
     */
    private void showPwdErrorDialog() {
        if (mQrPwdErrorDialog != null && mQrPwdErrorDialog.isShowing()){
            return;
        }
        mQrPwdErrorDialog = new TitleAndBtnDialog(mContext);
        mQrPwdErrorDialog.setBtnName(new String[] {"忘记密码", "再试一次"});
        mQrPwdErrorDialog.setNoticeContent("密码错误");
        mQrPwdErrorDialog.isShowTitle(false);
        mQrPwdErrorDialog.setCanceledOnTouchOutside(false);
        mQrPwdErrorDialog.setCancelable(false);
        mQrPwdErrorDialog.setLeftBtnTextBgColor(getResources().getColor(R.color.boc_common_cell_color),
                getResources().getColor(R.color.boc_text_color_red),
                getResources().getColor(R.color.boc_text_color_red),
                getResources().getColor(R.color.boc_common_cell_color));
        mQrPwdErrorDialog.setRightBtnTextBgColor(getResources().getColor(R.color.boc_text_color_red),
                getResources().getColor(R.color.boc_common_cell_color),
                getResources().getColor(R.color.boc_common_cell_color),
                getResources().getColor(R.color.boc_text_color_red));
        mQrPwdErrorDialog.setDialogBtnClickListener(new TitleAndBtnDialog.DialogBtnClickCallBack() {
            @Override
            public void onLeftBtnClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt(QRPayResetPayPwdFragment.RESET_PASS_FROM_KEY, QRPayResetPayPwdFragment.RESET_PASS_FROM_PAY);
                QRPayResetPayPwdFragment fragment = new QRPayResetPayPwdFragment();
                fragment.setArguments(bundle);
                start(fragment);
                mQrPwdErrorDialog.dismiss();
            }

            @Override
            public void onRightBtnClick(View view) {
                showLoadingDialog();
                //查询支付限额信息
                mPassFreeInfoBasePresenter.loadQRPayGetPayQuota(selectPayAccount.getAccountId(), "01");
                mQrPwdErrorDialog.dismiss();
            }
        });
        mQrPwdErrorDialog.show();
    }


}