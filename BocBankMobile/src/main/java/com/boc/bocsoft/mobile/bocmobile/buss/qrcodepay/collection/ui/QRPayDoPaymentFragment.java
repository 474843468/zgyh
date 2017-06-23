package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.collection.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayDoTransfer.QRPayDoTransferResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetAccountCatalog.QRPayGetAccountCatalogResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditDialogWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.moneyinputview.MoneyInputTextView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.collection.model.QRPayGetPayeeInfoModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.collection.presenter.QRCollectionCodePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.payment.model.QRPayGetQRCodeViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.scan.ui.QRPayScanFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.model.PassFreeInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.model.PayQuotaViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.presenter.QRPayBasePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.presenter.QRPayGetAccountCatalogPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui.QRPayBaseContract;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui.QRPayGetAccountCatalogContract;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui.QRPayMainFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.utils.QrCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.widget.QRPayPwdDialog;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanbin on 16/10/14.
 */
public class QRPayDoPaymentFragment extends BussFragment implements QRPayBaseContract.QRQueryPassFreeInfoBaseView
        , QRPayBaseContract.QrAccountBaseView, QRCollectionCodeContract.QrTransCodeView, QRPayGetAccountCatalogContract.QRPayGetAccountCatalogBaseView {
    private View mRootView;
    private LayoutInflater mInflater;
    private TextView collection_money_parcelable, collection_name, collection_cardnum,
            collection_beizhu, collection_bankname,
            collection_cardinformation, collection_change, collection1;
    private View fengexian;
    private String tranAmount, payeeActNum, payeeActNam, payeeComments, payeeIbkNam;
    private String cardinformation;
    private LinearLayout linearLayout_boc_fragment_qrpaydopayment, linearLayout_beizhu;
    private EditDialogWidget editDialogWidget;
    private ImageView iv_xiaotubiao;
    private SelectAccoutFragment selectAccoutFragment;
    // 选择账户页面选中的账户
    private Bundle accountBundle;
    private QRPayBasePresenter mPassFreeInfoBasePresenter;
    private QRPayBasePresenter mPayBasePresenter;
    private QRCollectionCodePresenter mQRCollectionCodePresenter;
    private QRPayGetAccountCatalogPresenter mPayGetAccountCatalogPresenter;

    private Button bt_boc_fragment_qrpaydopayment_queding;
    //选择的AccountBean
    private AccountBean selectPayAccount;
    private AccountBean noselectPayAccount;
    //支付限额
    public PayQuotaViewModel mPayQuotaViewModel;
    //二维码付款UI层model
    private QRPayGetQRCodeViewModel mQrPayGetQRCodeViewModel;
    private PassFreeInfoViewModel mPassFreeInfoViewModel;
    private QRPayGetPayeeInfoModel qrPayGetPayeeInfoModel;
    private EditMoneyInputWidget et_boc_fragment_qrpaydopayment;
    private MoneyInputTextView keyongyue;
    private String fukuanbeizhu ="";
    //扫一扫界面传过来的二维码流水号 转账交易上送需要
    private String qrNo="";

    //二三类帐户的错误提示框    用于进入收款页面判断
    private TitleAndBtnDialog mQrNoUseAccountErrorDialog;

    public static String conversationId="";
    private LinearLayout yu_e_linear,color_linearlayout;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        this.mInflater = mInflater;
        mRootView = View.inflate(mContext, R.layout.boc_fragment_qrpaydopayment, null);
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SecurityVerity.getInstance(getActivity());
    }

    @Override
    public void initView() {
        collection_money_parcelable = (TextView) mRootView.findViewById(R.id.collection_money_parcelable);
        collection_name = (TextView) mRootView.findViewById(R.id.collection_name);
        collection_cardnum = (TextView) mRootView.findViewById(R.id.collection_cardnum);
        collection_beizhu = (TextView) mRootView.findViewById(R.id.collection_beizhu);
        collection_bankname = (TextView) mRootView.findViewById(R.id.collection_bankname);
        collection_cardinformation = (TextView) mRootView.findViewById(R.id.collection_cardinformation);
        collection_change = (TextView) mRootView.findViewById(R.id.collection_change);
        collection1 = (TextView) mRootView.findViewById(R.id.collection1);
        linearLayout_boc_fragment_qrpaydopayment = (LinearLayout) mRootView.findViewById(R.id.linearLayout_boc_fragment_qrpaydopayment);
        bt_boc_fragment_qrpaydopayment_queding = (Button) mRootView.findViewById(R.id.bt_boc_fragment_qrpaydopayment_queding);
        et_boc_fragment_qrpaydopayment = (EditMoneyInputWidget) mRootView.findViewById(R.id.et_boc_fragment_qrpaydopayment);
        linearLayout_beizhu = (LinearLayout) mRootView.findViewById(R.id.linearLayout_beizhu);
        keyongyue= (MoneyInputTextView) mRootView.findViewById(R.id.keyongyue);
        yu_e_linear= (LinearLayout) mRootView.findViewById(R.id.yu_e_linear);
        color_linearlayout= (LinearLayout) mRootView.findViewById(R.id.color_linearlayout);
        fengexian=mRootView.findViewById(R.id.fengexian);
        et_boc_fragment_qrpaydopayment.setMaxLeftNumber(11);
        et_boc_fragment_qrpaydopayment.setMaxRightNumber(2);
        et_boc_fragment_qrpaydopayment.setClearIconVisible(false);

    }

    @Override
    public void initData() {
        mPassFreeInfoBasePresenter = new QRPayBasePresenter((QRPayBaseContract.QRQueryPassFreeInfoBaseView) this);
        mPayBasePresenter = new QRPayBasePresenter((QRPayBaseContract.QrAccountBaseView) this);
        mQRCollectionCodePresenter = new QRCollectionCodePresenter((QRCollectionCodeContract.QrTransCodeView) this);
        mPayGetAccountCatalogPresenter = new QRPayGetAccountCatalogPresenter((QRPayGetAccountCatalogContract.QRPayGetAccountCatalogBaseView) this);
        mQrPayGetQRCodeViewModel = new QRPayGetQRCodeViewModel();
        selectPayAccount = QRPayMainFragment.mDefaultAccountBean;
        qrPayGetPayeeInfoModel = getArguments().getParcelable(QRPayScanFragment.C2C_PAYEE_INFO);
//        selectPayAccount = getArguments().getParcelable(QRPayScanFragment.C2C_ACCOUNT_BEAN);
        qrNo = getArguments().getString(QRPayScanFragment.C2C_QRCODE_CONTENT);
        if (qrPayGetPayeeInfoModel != null) {
            tranAmount = qrPayGetPayeeInfoModel.getTranAmount();
            payeeActNum = qrPayGetPayeeInfoModel.getPayeeActNum();
            payeeActNam = qrPayGetPayeeInfoModel.getPayeeActNam();
            payeeComments = qrPayGetPayeeInfoModel.getPayeeComments();
            payeeIbkNam = qrPayGetPayeeInfoModel.getPayeeIbkNam();
            collection_name.setText(payeeActNam);
            collection_cardnum.setText(payeeActNum);
            collection_bankname.setText(payeeIbkNam);
        }

        if (!TextUtils.isEmpty(tranAmount)&&!"0.00".equals(tranAmount)&&!"0".equals(tranAmount)) {
            collection1.setVisibility(View.VISIBLE);
            collection_money_parcelable.setVisibility(View.VISIBLE);
            fengexian.setVisibility(View.VISIBLE);
            linearLayout_beizhu.setVisibility(View.VISIBLE);
            linearLayout_boc_fragment_qrpaydopayment.setVisibility(View.GONE);
            BigDecimal bd=new BigDecimal(tranAmount);
            NumberFormat currency = NumberFormat.getCurrencyInstance();
            String tranAmount_money= currency.format(bd);
            collection_money_parcelable.setText(tranAmount_money);
            collection_beizhu.setText(payeeComments);
            color_linearlayout.setBackgroundColor(getResources().getColor(R.color.boc_common_cell_color));
            if (!TextUtils.isEmpty(payeeComments.trim())){
                linearLayout_beizhu.setVisibility(View.VISIBLE);
            }else{
                linearLayout_beizhu.setVisibility(View.GONE);
            }


        } else {
            collection1.setVisibility(View.GONE);
            fengexian.setVisibility(View.GONE);
            collection_money_parcelable.setVisibility(View.GONE);
            linearLayout_beizhu.setVisibility(View.GONE);
            linearLayout_boc_fragment_qrpaydopayment.setVisibility(View.VISIBLE);
            color_linearlayout.setBackgroundColor(getResources().getColor(R.color.boc_main_bg_color));
        }

        if (selectPayAccount!=null){
            collection_cardinformation.setText(selectPayAccount.getNickName() + "(" + QrCodeUtils.getShortCardNum(selectPayAccount.getAccountNumber()) + ")");
            String accountType = selectPayAccount.getAccountType();
            if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(accountType) ||
                    ApplicationConst.ACC_TYPE_GRE.equals(accountType)) {
//                    //信用卡账户详情
                //信用卡不显示余额
                showLoadingDialog();
                mPayBasePresenter.queryCreditAccountDetail(selectPayAccount.getAccountId(), ApplicationConst.CURRENCY_CNY);
            } else if (ApplicationConst.ACC_TYPE_BRO.equals(accountType)) {
                //借记卡账户详情
                showLoadingDialog();
                mPayBasePresenter.queryAccountDetails(selectPayAccount.getAccountId());
            }
        }


    }
    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if (resultCode == SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT) {
            selectPayAccount = data.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
            collection_cardinformation.setText(selectPayAccount.getNickName() + "(" + QrCodeUtils.getShortCardNum(selectPayAccount.getAccountNumber()) + ")");
        }
    }
    @Override
    public void setListener() {

        bt_boc_fragment_qrpaydopayment_queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String money = et_boc_fragment_qrpaydopayment.getContentMoney().trim();

                if (!TextUtils.isEmpty(money)) {
                    tranAmount = money;
                }

                //查询支付限额信息
                mPassFreeInfoBasePresenter.loadQRPayGetPayQuota(selectPayAccount.getAccountId(), "02");
                showLoadingDialog();
            }
        });
        collection_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePayAccount();
            }
        });


    }

    /**
     * 用户输入密码 进行转账交易
     *
     * @param random
     */
    private void handleConfirmInfo(String random) {

        QRPayPwdDialog qrPayPwdDialog = new QRPayPwdDialog(getActivity());

        qrPayPwdDialog.setDialogData("向 "+qrPayGetPayeeInfoModel.getPayeeActNam()+" 付款", tranAmount, random);
        qrPayPwdDialog.setTitle("请输入支付密码");
        qrPayPwdDialog.setQRPayDialogListener(new QRPayPwdDialog.QRPayPwdDialogCallBack() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onNumCompleted(String encryptRandomNum, String encryptPassword, String mVersion) {
                showLoadingDialog();
                //转账交易
                mQRCollectionCodePresenter.loadQRPayDoTransfer(selectPayAccount.getAccountId(), encryptPassword, encryptRandomNum, tranAmount, qrNo, fukuanbeizhu, selectPayAccount.getAccountNumber(), selectPayAccount.getAccountName());
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

    /**
     * 更改默认支付账户
     */
    private void changePayAccount() {
        ArrayList<String> accountTypeList = new ArrayList<String>();
        accountTypeList.add(ApplicationConst.ACC_TYPE_BRO);//借记卡
        accountTypeList.add(ApplicationConst.ACC_TYPE_ZHONGYIN);//中银系列信用卡
        accountTypeList.add(ApplicationConst.ACC_TYPE_GRE);//长城信用卡
//        selectAccoutFragment = new SelectAccoutFragment().newInstance(accountTypeList);
        List<AccountBean> accountBeansList = QRPayMainFragment.getRelativeBankAccountList(accountTypeList);
        selectAccoutFragment = new SelectAccoutFragment().newInstanceWithData((ArrayList) accountBeansList);

        selectAccoutFragment.isRequestNet(true);
        startForResult(selectAccoutFragment, SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT);
        selectAccoutFragment.setOnItemListener(new SelectAccoutFragment.ItemListener() {
            @Override
            public void onItemClick(Bundle bundle) {
                accountBundle = bundle;
                AccountBean accountBean = bundle.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
                if (accountBean != null) {
                    selectPayAccount=accountBean;
//                    查询是否是二三类帐户
//                    noselectPayAccount = accountBean;
//                    mPayGetAccountCatalogPresenter.qRPayGetAccountCatalog(accountBean.getAccountId());
//                    showLoadingDialog();
                    String accountType = selectPayAccount.getAccountType();
                    if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(accountType) ||
                            ApplicationConst.ACC_TYPE_GRE.equals(accountType)) {
                        selectAccoutFragment.setFramgentResult(SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT, accountBundle);
                        selectAccoutFragment.pop();
                        //信用卡账户详情  信用卡不显示余额
                        showLoadingDialog();
                        mPayBasePresenter.queryCreditAccountDetail(selectPayAccount.getAccountId(), ApplicationConst.CURRENCY_CNY);
                    } else if (ApplicationConst.ACC_TYPE_BRO.equals(accountType)) {
                        //借记卡账户详情
                        showLoadingDialog();
                        selectAccoutFragment.setFramgentResult(SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT, accountBundle);
                        selectAccoutFragment.pop();
                        mPayBasePresenter.queryAccountDetails(selectPayAccount.getAccountId());
                    }

                }

            }
        });
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_qrpay_payment);
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    /**
     * 查询小额免密信息成功
     *
     * @param infoViewModel
     */
    @Override
    public void loadQRPayGetPassFreeInfoSuccess(PassFreeInfoViewModel infoViewModel) {

        mPassFreeInfoViewModel = infoViewModel;

        String accountType = selectPayAccount.getAccountType();
        if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(accountType) ||
                ApplicationConst.ACC_TYPE_GRE.equals(accountType) ) {
            // 小额免密开关状态 0：未开通 1：已开通
            if ("1".equals(mPassFreeInfoViewModel.getPassFreeFlag())) {
                if (QrCodeUtils.isCompareAmountCanNext(mQrPayGetQRCodeViewModel.getAmount(),
                        QrCodeUtils.getFormatTransQuota(mPassFreeInfoViewModel.getPassFreeAmount()))) {
                    //转账交易
                    mQRCollectionCodePresenter.loadQRPayDoTransfer(selectPayAccount.getAccountId(), "", "", tranAmount, qrNo, fukuanbeizhu, selectPayAccount.getAccountNumber(), selectPayAccount.getAccountName());
                } else {
                    //查询随机数
                    mQRCollectionCodePresenter.loadGetRandom();
                }
            } else if ("0".equals(mPassFreeInfoViewModel.getPassFreeFlag())) {
                //查询随机数
                mQRCollectionCodePresenter.loadGetRandom();
            }

        } else if (ApplicationConst.ACC_TYPE_BRO.equals(accountType)) {
            // 小额免密开关状态 0：未开通 1：已开通
            if ("1".equals(mPassFreeInfoViewModel.getPassFreeFlag())) {
                if (QrCodeUtils.isCompareAmountCanNext(mQrPayGetQRCodeViewModel.getAmount(),
                        QrCodeUtils.getFormatTransQuota(mPassFreeInfoViewModel.getPassFreeAmount()))) {
                    //转账交易
                    mQRCollectionCodePresenter.loadQRPayDoTransfer(selectPayAccount.getAccountId(), "", "", tranAmount, qrNo, fukuanbeizhu, selectPayAccount.getAccountNumber(), selectPayAccount.getAccountName());

                } else {
                    //查询随机数
                    mQRCollectionCodePresenter.loadGetRandom();
                }
            } else if ("0".equals(mPassFreeInfoViewModel.getPassFreeFlag())) {
                //查询随机数
                mQRCollectionCodePresenter.loadGetRandom();
            }
        }
    }

    /**
     * 查询小额免密信息失败
     *
     * @param biiResultErrorException
     */
    @Override
    public void loadQRPayGetPassFreeInfoFail(BiiResultErrorException biiResultErrorException) {

        closeProgressDialog();
    }

    /***
     * 查询支付限额成功
     */
    @Override
    public void loadQRPayGetPayQuotaSuccess(PayQuotaViewModel quotaViewModel) {
        mPayQuotaViewModel = quotaViewModel;
        String accountType = selectPayAccount.getAccountType();
        if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(accountType) ||
                ApplicationConst.ACC_TYPE_GRE.equals(accountType)) {

            if (QrCodeUtils.isCompareAmountCanNext(tranAmount, QrCodeUtils.getFormatTransQuota(mPayQuotaViewModel.getCardTransQuota()))) {
                //查询小额免密信息
                mPassFreeInfoBasePresenter.loadQRPayGetPassFreeInfo(selectPayAccount.getAccountId());
            } else {
                closeProgressDialog();
                showErrorDialog("此账户单笔付款限额为" + QrCodeUtils.getFormatTransQuotaNoSign(mPayQuotaViewModel.getCardTransQuota()) + "元，已超出");
            }

        } else if (ApplicationConst.ACC_TYPE_BRO.equals(accountType)) {
            if (QrCodeUtils.isCompareAmountCanNext(tranAmount, QrCodeUtils.getFormatTransQuota(mPayQuotaViewModel.getCardPayQuota()))) {
                //查询小额免密信息
                mPassFreeInfoBasePresenter.loadQRPayGetPassFreeInfo(selectPayAccount.getAccountId());
            } else {
                closeProgressDialog();
                showErrorDialog("此账户单笔付款限额为" + QrCodeUtils.getFormatTransQuotaNoSign(mPayQuotaViewModel.getCardTransferQuota()) + "元，已超出");
            }
        }
    }

    /***
     * 查询支付限额失败
     */
    @Override
    public void loadQRPayGetPayQuotaFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /***
     * 查询账户详情成功
     */
    @Override
    public void queryAccountDetailsSuccess(BigDecimal availableBalance) {
        closeProgressDialog();
        keyongyue.setInputMoney(availableBalance.toString());
//        selectAccoutFragment.setFramgentResult(SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT, accountBundle);
//        selectAccoutFragment.pop();
    }

    /***
     * 查询账户详情失败
     */
    @Override
    public void queryAccountDetailsFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /***
     * 查询信用卡账户详情成功
     */
    @Override
    public void queryCreditAccountDetailSuccess(BigDecimal availableBalance) {
        closeProgressDialog();
        yu_e_linear.setVisibility(View.GONE);

//        keyongyue.setInputMoney(availableBalance.toString()); //信用卡不显示余额
//        selectAccoutFragment.setFramgentResult(SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT, accountBundle);
//        selectAccoutFragment.pop();
    }

    /***
     * 查询信用卡账户详情失败
     */
    @Override
    public void queryCreditAccountDetailFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void loadQRPayDoTransferSuccess(QRPayDoTransferResult ViewModel) {

        closeProgressDialog();
//        if ("0".equals(ViewModel.getStatus())){
        QRPayDoPaymentSuccessFragment qrPayDoPaymentSuccessFragment = new QRPayDoPaymentSuccessFragment();
        Bundle bundle = new Bundle();
        bundle.putString("payeeName", ViewModel.getPayeeName());//二维码内容
        bundle.putString("amount",ViewModel.getTranAmount());
        bundle.putString("acount",selectPayAccount.getAccountNumber());
        bundle.putString("orderTime",ViewModel.getOrderTime());

        qrPayDoPaymentSuccessFragment.setArguments(bundle);
        start(qrPayDoPaymentSuccessFragment);
//        }

    }

    @Override
    public void loadQRPayDoTransferFail(BiiResultErrorException biiResultErrorException) {

        closeProgressDialog();
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

    @Override
    public void loadQRPayGetAccountCatalogSuccess(QRPayGetAccountCatalogResult qrPayGetAccountCatalogResult) {

        if ("1".equals(qrPayGetAccountCatalogResult.getAccountCatalog())) {
            selectPayAccount = noselectPayAccount;
            collection_cardinformation.setText(selectPayAccount.getNickName() + "(" + QrCodeUtils.getShortCardNum(selectPayAccount.getAccountNumber()) + ")");
            String accountType = selectPayAccount.getAccountType();
            if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(accountType) ||
                    ApplicationConst.ACC_TYPE_GRE.equals(accountType)) {
                selectAccoutFragment.setFramgentResult(SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT, accountBundle);
                selectAccoutFragment.pop();
                //信用卡账户详情  信用卡不显示余额
                showLoadingDialog();
                mPayBasePresenter.queryCreditAccountDetail(selectPayAccount.getAccountId(), ApplicationConst.CURRENCY_CNY);
            } else if (ApplicationConst.ACC_TYPE_BRO.equals(accountType)) {
                //借记卡账户详情
                showLoadingDialog();
                selectAccoutFragment.setFramgentResult(SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT, accountBundle);
                selectAccoutFragment.pop();
                mPayBasePresenter.queryAccountDetails(selectPayAccount.getAccountId());
            }
        } else if ("2".equals(qrPayGetAccountCatalogResult.getAccountCatalog()) || "3".equals(qrPayGetAccountCatalogResult.getAccountCatalog())) {
            showNoUseAccountErrorDialog();
        }
    }

    @Override
    public void loadQRPayGetAccountCatalogFail(BiiResultErrorException biiResultErrorException) {

        closeProgressDialog();
    }

    /**
     * 显示请选择一类帐户对话框
     */
    private void showNoUseAccountErrorDialog() {
        if (mQrNoUseAccountErrorDialog != null && mQrNoUseAccountErrorDialog.isShowing()) {
            return;
        }
        mQrNoUseAccountErrorDialog = new TitleAndBtnDialog(mContext);
        mQrNoUseAccountErrorDialog.isShowTitle(false);
        mQrNoUseAccountErrorDialog.setCanceledOnTouchOutside(false);
        mQrNoUseAccountErrorDialog.setNoticeContent("您选择的为二三类帐户，请选择一类帐户");
        mQrNoUseAccountErrorDialog.setDialogBtnClickListener(new TitleAndBtnDialog.DialogBtnClickCallBack() {
            @Override
            public void onLeftBtnClick(View view) {
                mQrNoUseAccountErrorDialog.dismiss();
            }

            @Override
            public void onRightBtnClick(View view) {
                mQrNoUseAccountErrorDialog.dismiss();
            }
        });
        mQrNoUseAccountErrorDialog.show();
    }
}
