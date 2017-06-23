package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetAccountCatalog.QRPayGetAccountCatalogResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.AccountTypeUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.collection.presenter.QRCollectionCodePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.collection.ui.QRCollectionCodeContract;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.collection.ui.QRCollectionCodeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.collection.ui.QRPayDoPaymentFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.payment.model.QRPayGetQRCodeViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.payment.presenter.QRPayCodePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.payment.ui.QRPayCodeContract;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.payment.ui.QRPayCodeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.presenter.QRPayBasePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.presenter.QRPayGetAccountCatalogPresenter;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 二维码支付 - 主页面
 * Created by wangf on 2016/8/20.
 */
public class QRPayMainFragment extends BussFragment implements QRPayBaseContract.QrQueryBaseView, QRPayCodeContract.GetQrCodeView
        , QRCollectionCodeContract.GetQrCollectionCodeView, QRPayGetAccountCatalogContract.QRPayGetAccountCatalogBaseView {
    private View mRootView;

    //扫一扫
    private LinearLayout mLayoutQrCodeScan;
    //付款
    private LinearLayout mLayoutQrCodePayment;
    //收款
    private LinearLayout mLayoutQrCodeCollection;

    //关联账户中的银联账户
    private static volatile List<AccountBean> relativeBankAccountList;
    //默认账户
    public static AccountBean mDefaultAccountBean;
    //是否设置支付密码
    public static boolean isSetPayPass = false;
    //是否设置默认卡
    public static boolean isSetDefaultCard = false;
    //是否有一类帐户
    public static boolean isAccountCatalog = false;

    //获取二维码的会话ID
    public static String qRCodeConversationID;

    private QRPayBasePresenter qrPayBasePresenter;
    private QRPayCodePresenter mQrGetCodePresenter;
    private QRCollectionCodePresenter mQRCollectionCodePresenter;
    private QRPayGetAccountCatalogPresenter mPayGetAccountCatalogPresenter;


    //未设置支付密码的错误提示框
    private TitleAndBtnDialog mQrNoPassErrorDialog;
    //无可操作账户的错误提示框
    private TitleAndBtnDialog mQrNoAccountErrorDialog;
    //无可用账户的错误提示框    用于进入收款页面判断
    private TitleAndBtnDialog mQrNoUseAccountErrorDialog;
    //用于标识账户类别的请求次数
    private int iAccountCatalog;

    List<AccountBean> accountBeansList;
    //C2C选中的账户信息
    private AccountBean c2cSelectAccount;
    public static final String C2C_ACCOUNT_BEAN = "C2C_ACCOUNT_BEAN";
    /**
     * 页面数据传递
     */
    public static final String QRPAY_BUNDLE_QRCODE = "qrcode";
    public static final String QRPAY_BUNDLE_FREQ = "infofreq";
    public static final String QRPAY_BUNDLE_CODE_LIFE = "codeLife";

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = View.inflate(mContext, R.layout.boc_fragment_qrpay_main, null);
        //调试使用
//        QRCollectionCodeFragment paymentFragment = new QRCollectionCodeFragment();
//        QRPayCodeFragment paymentFragment = new QRPayCodeFragment();
//        QRPayDoPaymentFragment paymentFragment = new QRPayDoPaymentFragment();
//         QRCollectionSetMoneyFragment paymentFragment = new QRCollectionSetMoneyFragment();
//        start(paymentFragment);
        return mRootView;
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_qrpay);
    }

    @Override
    public void beforeInitView() {
        super.beforeInitView();
    }

    @Override
    public void initView() {
        mLayoutQrCodeScan = (LinearLayout) mRootView.findViewById(R.id.ll_qrcode_pay_scan);
        mLayoutQrCodePayment = (LinearLayout) mRootView.findViewById(R.id.ll_qrcode_pay_payment);
        mLayoutQrCodeCollection = (LinearLayout) mRootView.findViewById(R.id.ll_qrcode_pay_collection);

    }

    @Override
    public void initData() {
        qrPayBasePresenter = new QRPayBasePresenter(this);
        mQrGetCodePresenter = new QRPayCodePresenter(this);
        mQRCollectionCodePresenter = new QRCollectionCodePresenter(this);
        mPayGetAccountCatalogPresenter = new QRPayGetAccountCatalogPresenter(this);
        mDefaultAccountBean = new AccountBean();
        isSetPayPass = false;
        isSetDefaultCard = false;

        showLoadingDialog();
        qrPayBasePresenter.queryQRPayGetRelativedAcctList();

//        if (judgeHaveAccount()) {
//            showLoadingDialog();
//            //查询客户是否开通二维码服务
//            qrPayBasePresenter.loadQRServiceIsOpen();
//        }
    }


    @Override
    public void reInit() {
        showLoadingDialog();
        //查询客户是否开通二维码服务
        qrPayBasePresenter.loadQRServiceIsOpen();
    }

    @Override
    public void setListener() {
        //扫一扫
        mLayoutQrCodeScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QRPayDoPaymentFragment paymentFragment = new QRPayDoPaymentFragment();
                start(paymentFragment);
            }
        });

        //付款
        mLayoutQrCodePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isSetPayPass) {
                    showLoadingDialog();
                    mQrGetCodePresenter.loadQRPayGetQRCode(mDefaultAccountBean.getAccountId());
                } else {
                    showNoPassErrorDialog();
                }

            }
        });

        //收款
        mLayoutQrCodeCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //由于C2C转账不能是二三类帐户 所以与付款不一致要特殊处理
//                qRPayGetAccountCatalog();
                showLoadingDialog();
                mQRCollectionCodePresenter.loadQRPayGetQRCode(c2cSelectAccount.getAccountId(), "", "");

            }
        });

    }

    @Override
    protected void titleRightIconClick() {
        if (isSetPayPass) {
            start(new QRPayMoreFragment());
        } else {
            showNoPassErrorDialog();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    /**
     * 根据查询默认卡接口返回的流水号获取accountBean  并且将其设置成第一个帐户  流水号为空就让第一个设置成默认帐号
     *
     * @param actSeq
     */
    private void handleDefaultAccountBean(String actSeq) {
        List<AccountBean> accountList = QRPayMainFragment.getRelativeBankAccountList(null);
//        List<AccountBean> accountList = ApplicationContext.getInstance().getChinaBankAccountList(null);
        if (null != accountList) {
            ArrayList<String> accountTypeList = new ArrayList<String>();
            accountTypeList.add(ApplicationConst.ACC_TYPE_BRO);//借记卡
            accountTypeList.add(ApplicationConst.ACC_TYPE_ZHONGYIN);//中银系列信用卡
            accountTypeList.add(ApplicationConst.ACC_TYPE_GRE);//长城信用卡
            accountBeansList = QRPayMainFragment.getRelativeBankAccountList(accountTypeList);
//            accountBeansList = ApplicationContext.getInstance().getChinaBankAccountList(accountTypeList);
            //默认卡的流水号为空  设置第一个为默认卡
            if (StringUtils.isEmpty(actSeq)) {
                if (accountBeansList.size() != 0) {
                    mDefaultAccountBean = accountBeansList.get(0);
                    c2cSelectAccount = mDefaultAccountBean;
                } else {
                    showNoAccountErrorDialog(null);
                }
                isSetDefaultCard = false;
                return;
            }
            //不为空就获取这个账户信息
            Iterator<AccountBean> iterator = accountBeansList.iterator();
            while (iterator.hasNext()) {
                AccountBean accountItem = iterator.next();
                if (actSeq.equals(accountItem.getAccountId())) {
                    iterator.remove();
                    mDefaultAccountBean = accountItem;
                    c2cSelectAccount = mDefaultAccountBean;
                    isSetDefaultCard = true;
                }
            }
            if (isSetDefaultCard) {
                accountBeansList.add(0, mDefaultAccountBean);
            }
        } else {
            showNoAccountErrorDialog(null);
        }
    }


    /**
     * 判断是否有可使用账户
     */
    private boolean judgeHaveAccount() {
        ArrayList<String> accountTypeList = new ArrayList<String>();
        accountTypeList.add(ApplicationConst.ACC_TYPE_BRO);//借记卡
        accountTypeList.add(ApplicationConst.ACC_TYPE_ZHONGYIN);// 中银系列信用卡
        accountTypeList.add(ApplicationConst.ACC_TYPE_GRE);// 长城信用卡
        List<AccountBean> accountBeansList = QRPayMainFragment.getRelativeBankAccountList(accountTypeList);
//        List<AccountBean> accountBeansList = ApplicationContext.getInstance().getChinaBankAccountList(accountTypeList);
        if (accountBeansList.size() == 0) {
            showNoAccountErrorDialog(null);
            return false;
        }
        return true;
    }


    /***
     * 查询客户是否开通二维码服务 成功
     * 0：未开通 1：已开通
     */
    @Override
    public void loadQRServiceIsOpenSuccess(String flag) {
        if ("0".equals(flag)) {
            handleDefaultAccountBean(null);
            closeProgressDialog();
            start(new QRPayContractFragment());
        } else if ("1".equals(flag)) {
            //查询默认卡
            qrPayBasePresenter.loadQRPayGetDefaultCard();
        }
    }

    /***
     * 查询客户是否开通二维码服务 失败
     */
    @Override
    public void loadQRServiceIsOpenFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /***
     * 查询默认卡成功
     */
    @Override
    public void loadQRPayGetDefaultCardSuccess(String actSeq) {
        handleDefaultAccountBean(actSeq);
        //查询是否设置支付密码
        qrPayBasePresenter.loadQRPayIsPassSet();
    }

    /***
     * 查询默认卡失败
     */
    @Override
    public void loadQRPayGetDefaultCardFail(BiiResultErrorException biiResultErrorException) {
        handleDefaultAccountBean(null);
        //查询是否设置支付密码
        qrPayBasePresenter.loadQRPayIsPassSet();
    }

    /***
     * 查询是否设置支付密码成功
     * 0：未设置 1：已设置
     */
    @Override
    public void loadQRPayIsPassSetSuccess(String flag) {
        closeProgressDialog();
        if ("0".equals(flag)) {
            isSetPayPass = false;
        } else if ("1".equals(flag)) {
            isSetPayPass = true;
        }
    }

    /***
     * 查询是否设置支付密码失败
     */
    @Override
    public void loadQRPayIsPassSetFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /**
     * 获取二维码成功
     *
     * @param qrCodeViewModel
     */
    @Override
    public void loadQRPayGetQRCodeSuccess(QRPayGetQRCodeViewModel qrCodeViewModel) {
        closeProgressDialog();
        Bundle bundle = new Bundle();
        bundle.putString(QRPAY_BUNDLE_QRCODE, qrCodeViewModel.getSeqNo());
        bundle.putString(QRPAY_BUNDLE_FREQ, qrCodeViewModel.getGetConfirmInfoFreq());
        bundle.putString(QRPAY_BUNDLE_CODE_LIFE, qrCodeViewModel.getLifeTime());
        QRPayCodeFragment paymentFragment = new QRPayCodeFragment();
        paymentFragment.setArguments(bundle);
        start(paymentFragment);
    }

    /**
     * 获取二维码失败
     *
     * @param biiResultErrorException
     */
    @Override
    public void loadQRPayGetQRCodeFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        showErrorDialog(biiResultErrorException.getErrorMessage());
    }


    /**
     * 显示未设置支付密码的错误对话框
     */
    private void showNoPassErrorDialog() {
        if (mQrNoPassErrorDialog != null && mQrNoPassErrorDialog.isShowing()) {
            return;
        }
        mQrNoPassErrorDialog = new TitleAndBtnDialog(mContext);
        mQrNoPassErrorDialog.isShowTitle(false);
        mQrNoPassErrorDialog.setCanceledOnTouchOutside(false);
        mQrNoPassErrorDialog.setNoticeContent("使用该功能需要您设置支付密码");
        mQrNoPassErrorDialog.setDialogBtnClickListener(new TitleAndBtnDialog.DialogBtnClickCallBack() {
            @Override
            public void onLeftBtnClick(View view) {
                mQrNoPassErrorDialog.dismiss();
            }

            @Override
            public void onRightBtnClick(View view) {
                start(new QRPaySetPayPwdFragment());
                mQrNoPassErrorDialog.dismiss();
            }
        });
        mQrNoPassErrorDialog.show();
    }


    /**
     * 显示无可操作账户的错误对话框
     */
    private void showNoAccountErrorDialog(String errorMsg) {
        if (mQrNoAccountErrorDialog != null && mQrNoAccountErrorDialog.isShowing()) {
            return;
        }
        mQrNoAccountErrorDialog = new TitleAndBtnDialog(mContext);
        mQrNoAccountErrorDialog.setBtnName(new String[]{getString(R.string.boc_common_sure)});
        mQrNoAccountErrorDialog.isShowTitle(false);
        mQrNoAccountErrorDialog.setCanceledOnTouchOutside(false);
        mQrNoAccountErrorDialog.setCancelable(false);
        if (StringUtils.isEmptyOrNull(errorMsg)){
            mQrNoAccountErrorDialog.setNoticeContent("无可操作账户");
        }else{
            mQrNoAccountErrorDialog.setNoticeContent(errorMsg);
        }
        mQrNoAccountErrorDialog.setDialogBtnClickListener(new TitleAndBtnDialog.DialogBtnClickCallBack() {
            @Override
            public void onLeftBtnClick(View view) {
            }

            @Override
            public void onRightBtnClick(View view) {
                ActivityManager.getAppManager().finishActivity();
                mQrNoAccountErrorDialog.dismiss();
            }
        });
        mQrNoAccountErrorDialog.show();
    }


    @Override
    public void loadQRCollectionGetQRCodeSuccess(QRPayGetQRCodeViewModel qrCodeViewModel) {
        closeProgressDialog();
        Bundle bundle = new Bundle();
        bundle.putString(QRPAY_BUNDLE_QRCODE, qrCodeViewModel.getSeqNo());
        bundle.putParcelable(C2C_ACCOUNT_BEAN, c2cSelectAccount);//账户
        QRCollectionCodeFragment paymentFragment = new QRCollectionCodeFragment();
        paymentFragment.setArguments(bundle);
        start(paymentFragment);

    }

    @Override
    public void loadQRCollectionGetQRCodeFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    //查询是否有一类帐户
    private void qRPayGetAccountCatalog() {
        showLoadingDialog();
        if (accountBeansList != null && accountBeansList.size() != 0) {
            mPayGetAccountCatalogPresenter.qRPayGetAccountCatalog(c2cSelectAccount.getAccountId());
        } else {
            showNoAccountErrorDialog(null);
        }

    }

    //在成功回调里面 如果查询AccountCatalog字段为1的话就说明c2cSelectAccount帐户为1类帐户
    @Override
    public void loadQRPayGetAccountCatalogSuccess(QRPayGetAccountCatalogResult qrPayGetAccountCatalogResult) {
        //由于测试数据没有一类账户  所以先注掉

        if ("1".equals(qrPayGetAccountCatalogResult.getAccountCatalog())) {
            mQRCollectionCodePresenter.loadQRPayGetQRCode(c2cSelectAccount.getAccountId(), "", "");
        } else {
            iAccountCatalog++;
            if (iAccountCatalog < accountBeansList.size()) {
                c2cSelectAccount = accountBeansList.get(iAccountCatalog);
                mPayGetAccountCatalogPresenter.qRPayGetAccountCatalog(c2cSelectAccount.getAccountId());
            } else {
                closeProgressDialog();
                showErrorDialog("没有可用帐户");
            }
        }
    }

    @Override
    public void loadQRPayGetAccountCatalogFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }


    /*** 查询关联账户中的银联账户列表成功 */
    @Override
    public void queryRelativeAccountListSuccess() {
        if (judgeHaveAccount()) {
            showLoadingDialog();
            //查询客户是否开通二维码服务
            qrPayBasePresenter.loadQRServiceIsOpen();
        }
    }

    /*** 查询关联账户中的银联账户列表失败 */
    @Override
    public void queryRelativeAccountListFail(BiiResultErrorException biiResultErrorException) {
        showNoAccountErrorDialog(biiResultErrorException.getErrorMessage());
    }


    /**
     * 根据账户类型获取中行所有账户
     *
     * @return
     */
    public static List<AccountBean> getRelativeBankAccountList(List<String> accountTypeList) {
        List<AccountBean> filterAccountList = new ArrayList<AccountBean>();
        if (null != accountTypeList) {
            if (null != relativeBankAccountList) {
                for (AccountBean accountItem : relativeBankAccountList) {
                    if (accountTypeList.contains(accountItem.getAccountType())) {
                        filterAccountList.add(accountItem);
                    }
                }
            }
        } else {
            filterAccountList = relativeBankAccountList;
        }
        return filterAccountList;
    }

    /**
     * 更新中行所有账户数据
     *
     * @return
     */
    public static void setRealtiveBankAccountList(List<AccountBean> chinaBankAccountList) {
        relativeBankAccountList = chinaBankAccountList;

        List<AccountBean> accountBeans = new ArrayList<>();

        //获取借记卡账户
        accountBeans.addAll(getRelativeBankAccountList(AccountTypeUtil.getBroType()));
        //获取信用卡账户
        accountBeans.addAll(getRelativeBankAccountList(AccountTypeUtil.getCreditType()));
        //获取活期账户
        accountBeans.addAll(getRelativeBankAccountList(AccountTypeUtil.getCurrentTypeWithOutBro()));
        //获取定期账户
        accountBeans.addAll(getRelativeBankAccountList(AccountTypeUtil.getRegularType()));
        //获取电子现金账户
        accountBeans.addAll(getRelativeBankAccountList(AccountTypeUtil.getFinanceType()));
        //获取虚拟卡账户
        accountBeans.addAll(getRelativeBankAccountList(AccountTypeUtil.getVirtualType()));
        //获取网上理财专属账户
        accountBeans.addAll(getRelativeBankAccountList(AccountTypeUtil.getMoneyType()));

        relativeBankAccountList = accountBeans;
    }
}
