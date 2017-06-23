package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.scan.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetAccountCatalog.QRPayGetAccountCatalogResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.common.LoginCallback;
import com.boc.bocsoft.mobile.bocmobile.buss.login.ui.LoginBaseActivity;
import com.boc.bocsoft.mobile.bocmobile.buss.login.ui.LoginContext;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.collection.model.QRPayGetPayeeInfoModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.collection.ui.QRPayDoPaymentFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.scan.presenter.QRPayScanPaymentPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.presenter.QRPayBasePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.presenter.QRPayGetAccountCatalogPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui.QRPayBaseContract;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui.QRPayContractFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui.QRPayGetAccountCatalogContract;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui.QRPayMainFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui.QRPaySetPayPwdFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthDetailsFragment;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;
import com.boc.bocsoft.mobile.framework.zxing.scan.CameraView;
import com.boc.bocsoft.mobile.framework.zxing.scan.CaptureManager;
import com.boc.bocsoft.mobile.framework.zxing.scan.ScanBoxView;
import com.boc.bocsoft.mobile.framework.zxing.scan.itf.ICapturer;
import com.chinamworld.boc.commonlib.ModuleManager;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * 二维码支付 - 扫描二维码
 * Created by wangf on 2016/8/20.
 */
public class QRPayScanFragment extends BussFragment implements  QRPayBaseContract.QrQueryBaseView, QRPayGetPayeeInfoContract.QRPayGetPayeeInfoView,
        QRPayGetAccountCatalogContract.QRPayGetAccountCatalogBaseView, View.OnClickListener, ICapturer {

    private static final String TAG = QRPayScanFragment.class.getSimpleName();
    public static final int RESULT_DECODE_SUCCESS = 101;
    private final int RESULT_CHOOSE_FROM_GALLERY = 1;

    public static final String RESULT_KEY = "result_model";

    /*** 页面View */
    private View rootView;
    private LayoutInflater mInflater;

    protected CameraView cameraPreview;
    protected ImageView leftIconIv;
    protected ImageView bottomIconIv;
    protected TextView bottomTextLight;
    protected ScanBoxView viewScanBox;

    protected RelativeLayout scanContainer;

    //CameraView和ScanBoxView的管理
    private CaptureManager captureManager;

    //相机是否打开的标识
    private boolean openFlashLight;
    //相机打开失败的错误提示框
    private TitleAndBtnDialog mQrErrorDialog;
    //无可操作账户的错误提示框
    private TitleAndBtnDialog mQrNoAccountErrorDialog;
    //未设置支付密码的错误提示框
    private TitleAndBtnDialog mQrNoPassErrorDialog;
    //二维码无商品信息的错误提示框
    private TitleAndBtnDialog mQRNoParseErrorDialog;
    //二三类账户的错误提示框
    private TitleAndBtnDialog mAccountCatalogErrorDialog;

    //二维码可用的账户数据
    private List<AccountBean> mAccountBeansList;

    /*** 二维码的类型 */
    private int currentQrCodeId = -1;//二维码类型
    private QRCodeDataDecode.QRCodeModel currentQrCodeModel ;//解析出的二维码数据模型

    /*** 请求网络逻辑处理 */
    private QRPayBasePresenter qrPayBasePresenter;
    private QRPayScanPaymentPresenter mPayeeInfoPresenter;
    private QRPayGetAccountCatalogPresenter mAccountCatalogPresenter;

    /*** 页面数据传递 */
    public static final String C2B_QRCODE_CONTENT = "C2B_QRCODE_CONTENT";
    public static final String C2C_QRCODE_CONTENT = "C2C_QRCODE_CONTENT";
    public static final String C2C_ACCOUNT_BEAN = "C2C_ACCOUNT_BEAN";
    public static final String C2C_PAYEE_INFO = "C2C_PAYEE_INFO";

    public static final String SCAN_FROM_KEY = "SCAN_FROM_KEY";
    public static final int SCAN_FROM_SCAN = 201;

    //用于标识账户类别的请求次数
    private int iAccountCatalog;
    //C2C选中的账户信息
    private AccountBean c2cSelectAccount;

    /**
     * 初始化布局
     *
     * @param mInflater
     * @return
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
    	this.mInflater = mInflater;
//        Window window = getActivity().getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        rootView = View.inflate(mContext, R.layout.boc_fragment_qrpay_scan, null);
        return rootView;
    }

    /**
     * 初始化数据
     */
    @Override
    public void initView() {
        cameraPreview = (CameraView) rootView.findViewById(R.id.camera_preview);
        leftIconIv = (ImageView) rootView.findViewById(R.id.leftIconIv);
        bottomIconIv = (ImageView) rootView.findViewById(R.id.bottomIconIv);
        bottomTextLight = (TextView) rootView.findViewById(R.id.bottom_text_light);
        viewScanBox = (ScanBoxView) rootView.findViewById(R.id.view_scan_box);
        scanContainer = (RelativeLayout) rootView.findViewById(R.id.scan_container);
    }

    @Override
    public void initData() {
        qrPayBasePresenter = new QRPayBasePresenter(this);
        mPayeeInfoPresenter = new QRPayScanPaymentPresenter(this);
        mAccountCatalogPresenter = new QRPayGetAccountCatalogPresenter(this);

        captureManager = new CaptureManager(this, cameraPreview, viewScanBox, scanContainer);
//        captureManager.start();
//        openFlashLight = false;
//        setFlashLight(openFlashLight);
    }

    @Override
    public void setListener() {
        leftIconIv.setOnClickListener(this);
        bottomIconIv.setOnClickListener(this);
    }

    // 由于在CameraView中会对相机的开关做处理，因此不需要重新reInit()
    // modify by wangf on 2016-11-4 11:17:37
//    @Override
//    public void reInit() {
//        mContentView.removeAllViews();
//        View contentView = onCreateView(mInflater);
//        if (contentView != null) {
//            mContentView.addView(contentView);
//        }
//        initView();
//        initData();
//        setListener();
//    }

    /**
     * 是否显示标题栏
     */
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        captureManager.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        captureManager.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setFlashLight(openFlashLight = false);
        captureManager.destroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        // 在fragment切换的时候，由于不会执行生命周期，所以需要手动的去开启/关闭闪光灯，
        // 当返回到该页面时，需要重新打开扫描事件
        LogUtil.i("--------- hidden = " + hidden);
        if (hidden) {
            setFlashLight(false);
        } else {
            captureManager.restartPreviewAfterDelay(1000);
            setFlashLight(openFlashLight);
        }
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.leftIconIv) {
            titleLeftIconClick();
        } else if (id == R.id.bottomIconIv) {
            bottomIconClick();
        }
    }

    /**
     * 标题栏下面图标点击事件
     * 设置闪光灯
     */
    protected void bottomIconClick() {
        openFlashLight = !openFlashLight;
        setFlashLight(openFlashLight);
    }

    private void setFlashLight(boolean openFlashLight) {
        int flashLightDrawable = openFlashLight ? R.drawable.icon_qrpay_scan_close : R.drawable.icon_qrpay_scan_open;
        int strId = openFlashLight ? R.string.boc_qrpay_scan_light_open : R.string.boc_qrpay_scan_light_close;
        bottomIconIv.setImageResource(flashLightDrawable);
        bottomTextLight.setText(getString(strId));
        captureManager.setFlashlight(openFlashLight);
    }

    @Override
    public void finish() {
        //结束fragment
//        pop();
        ActivityManager.getAppManager().finishActivity();
    }

    @Override
    public void handleDecode(Result rawResult, Bundle bundle) {
        Log.i("------ 二维码数据", rawResult.getText());
        setFlashLight(openFlashLight = false);
        handleQRCodeFragment(rawResult.getText());
    }

    /**
     * 根据二维码解析内容，进入不同页面
     * @param rawResult
     */
    private void handleQRCodeFragment(String rawResult){
        currentQrCodeModel = QRCodeDataDecode.getQrContent(rawResult);
        currentQrCodeId = currentQrCodeModel.getQrCodeId();
        switch (currentQrCodeId){
            case QRCodeDataDecode.QRCODE_ID_C2B://C2B
            case QRCodeDataDecode.QRCODE_ID_C2C://C2C
                if (ApplicationContext.getInstance().isLogin()) {
                    showLoadingDialog();
                    qrPayBasePresenter.queryQRPayGetRelativedAcctList();
                } else {
                    startToLogin(getActivity(), new LoginCallback() {
                        @Override
                        public void success() {
                            showLoadingDialog();
                            qrPayBasePresenter.queryQRPayGetRelativedAcctList();
                        }
                    });
                }
                break;
            case QRCodeDataDecode.QRCODE_ID_WEALTH://理财
                WealthDetailsFragment wealthDetailsFragment = new WealthDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable(WealthDetailsFragment.DETAILS_REQUEST, currentQrCodeModel.getQrContentWealth());
                bundle.putBoolean(WealthDetailsFragment.OTHER, true);
                wealthDetailsFragment.setArguments(bundle);
                startWithPop(wealthDetailsFragment);
                break;
            case QRCodeDataDecode.QRCODE_ID_LL://联龙二维码模块 - 基金type为2，外汇type为3，双向宝为4，账户贵金属为5
                try{
                    if (ModuleManager.instance.dispatchQRModule(getActivity(), currentQrCodeModel.getQrContentLL())){
                        // TODO: 2016/11/14 需要调试关闭扫描页面
                    }else{
                        showQRNoParseErrorDialog();
                    }
                }catch (Exception e){
                    showErrorDialog("跳转联龙模块异常" + e.toString());
                }
                break;
            case QRCodeDataDecode.QRCODE_ID_OTHER://其他
                showQRNoParseErrorDialog();
                break;
            default:
                showQRNoParseErrorDialog();
        }
    }

    @Override
    public void onCamaraOpenError() {
    	if (mQrErrorDialog != null && mQrErrorDialog.isShowing()){
            return;
        }
        mQrErrorDialog = new TitleAndBtnDialog(mContext);
        mQrErrorDialog.isShowTitle(false);
        mQrErrorDialog.setBtnName(new String[] {getString(R.string.boc_common_sure)});
        mQrErrorDialog.setCanceledOnTouchOutside(false);
        mQrErrorDialog.setCancelable(false);
        mQrErrorDialog.setNoticeContent(getString(R.string.boc_qrpay_camera_open_failed));
        mQrErrorDialog.setDialogBtnClickListener(new TitleAndBtnDialog.DialogBtnClickCallBack() {
            @Override
            public void onLeftBtnClick(View view) {
            }

            @Override
            public void onRightBtnClick(View view) {
            	mQrErrorDialog.cancel();
                finish();
            }
        });
        mQrErrorDialog.show();
    }

    /**
     * 判断是否有可使用账户
     */
    private boolean judgeHaveAccount() {
        ArrayList<String> accountTypeList = new ArrayList<String>();
         accountTypeList.add(ApplicationConst.ACC_TYPE_BRO);//借记卡
        accountTypeList.add(ApplicationConst.ACC_TYPE_ZHONGYIN);// 中银系列信用卡
        accountTypeList.add(ApplicationConst.ACC_TYPE_GRE);// 长城信用卡
        mAccountBeansList = QRPayMainFragment.getRelativeBankAccountList(accountTypeList);
//        mAccountBeansList = ApplicationContext.getInstance().getChinaBankAccountList(accountTypeList);
        if (mAccountBeansList.size() == 0) {
            showNoAccountErrorDialog(null);
            return false;
        }
        return true;
    }

    /**
     * 根据流水号获取accountBean
     * @param actSeq
     */
    private void handleDefaultAccountBean(String actSeq){
        List<AccountBean> accountList = QRPayMainFragment.getRelativeBankAccountList(null);
//        List<AccountBean> accountList = ApplicationContext.getInstance().getChinaBankAccountList(null);
        if(null!=accountList){
            if (StringUtils.isEmpty(actSeq)){
                if (mAccountBeansList.size() != 0){
                    QRPayMainFragment.mDefaultAccountBean = mAccountBeansList.get(0);
                }else{
                    showNoAccountErrorDialog(null);
                }
                QRPayMainFragment.isSetDefaultCard = false;
                return;
            }
            for(AccountBean accountItem : accountList){
                if (actSeq.equals(accountItem.getAccountId())){
                    QRPayMainFragment.mDefaultAccountBean = accountItem;
                    QRPayMainFragment.isSetDefaultCard = true;
                }
            }
            if (QRPayMainFragment.isSetDefaultCard){
                changeDefaultAccountList();
            }
        }else{
            showNoAccountErrorDialog(null);
        }
    }

    /**
     * 若有默认卡，则将默认卡顺序放在list的第一位置
     */
    private void changeDefaultAccountList(){
        AccountBean accountBean;
        for (int i = 0; i < mAccountBeansList.size(); i++){
            if (QRPayMainFragment.mDefaultAccountBean.getAccountId().equals(mAccountBeansList.get(i).getAccountId())){
                accountBean = mAccountBeansList.get(i);
                mAccountBeansList.remove(i);
                mAccountBeansList.add(0, accountBean);
                return;
            }
        }
    }

    /**
     * 跳转到登录页面
     */
    public static void startToLogin(Activity activity, LoginCallback callback){
        Intent intent = new Intent();
        intent.setClass(activity, LoginBaseActivity.class);
        if (callback != null) {
            LoginContext.instance.setCallback(callback);
        }

        activity.startActivity(intent);
        activity.overridePendingTransition( R.anim.boc_infromright, R.anim.boc_outtoleft);
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

    /*** 查询客户是否开通二维码服务 成功
     * 0：未开通 1：已开通
     */
    @Override
    public void loadQRServiceIsOpenSuccess(String flag) {
        if ("0".equals(flag)){
            closeProgressDialog();
            QRPayContractFragment fragment = new QRPayContractFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(SCAN_FROM_KEY, SCAN_FROM_SCAN);
            fragment.setArguments(bundle);
            startWithPop(fragment);
        }else if ("1".equals(flag)){
            //查询默认卡
            qrPayBasePresenter.loadQRPayGetDefaultCard();
        }
    }

    /*** 查询客户是否开通二维码服务 失败 */
    @Override
    public void loadQRServiceIsOpenFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /*** 查询默认卡成功 */
    @Override
    public void loadQRPayGetDefaultCardSuccess(String actSeq) {
        handleDefaultAccountBean(actSeq);
        //查询是否设置支付密码
        qrPayBasePresenter.loadQRPayIsPassSet();
    }

    /*** 查询默认卡失败*/
    @Override
    public void loadQRPayGetDefaultCardFail(BiiResultErrorException biiResultErrorException) {
        handleDefaultAccountBean(null);
        //查询是否设置支付密码
        qrPayBasePresenter.loadQRPayIsPassSet();
    }

    /*** 查询是否设置支付密码成功
     * 0：未设置 1：已设置
     */
    @Override
    public void loadQRPayIsPassSetSuccess(String flag) {
        if ("0".equals(flag)){
            closeProgressDialog();
            QRPayMainFragment.isSetPayPass = false;
            showNoPassErrorDialog();
        }else if ("1".equals(flag)){
            QRPayMainFragment.isSetPayPass = true;
            switch (currentQrCodeId){
                case QRCodeDataDecode.QRCODE_ID_C2B:
                    closeProgressDialog();
                    QRPayScanPaymentFragment scanPaymentFragment = new QRPayScanPaymentFragment();
                    Bundle bundleQrPay = new Bundle();
                    bundleQrPay.putParcelable(C2B_QRCODE_CONTENT, currentQrCodeModel.getQrContentC2B());
                    scanPaymentFragment.setArguments(bundleQrPay);
                    start(scanPaymentFragment);
                    break;
                case QRCodeDataDecode.QRCODE_ID_C2C:
                    //查询收款人信息
                    mPayeeInfoPresenter.loadGetPayeeInfo(currentQrCodeModel.getQrContentC2C());
                    //现在不需要查询二三类账户信息
//                    iAccountCatalog = 0;
//                    c2cSelectAccount = mAccountBeansList.get(iAccountCatalog);
//                    mAccountCatalogPresenter.qRPayGetAccountCatalog(c2cSelectAccount.getAccountId());
                    break;
                default:
                    closeProgressDialog();
            }

        }
    }

    /*** 查询是否设置支付密码失败 */
    @Override
    public void loadQRPayIsPassSetFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /*** 查询收款人信息 成功 */
    @Override
    public void loadGetPayeeInfoSuccess(QRPayGetPayeeInfoModel viewModel) {
        closeProgressDialog();
        QRPayDoPaymentFragment payDoPaymentFragment = new QRPayDoPaymentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(C2C_QRCODE_CONTENT, currentQrCodeModel.getQrContentC2C());//二维码内容
//        bundle.putParcelable(C2C_ACCOUNT_BEAN, c2cSelectAccount);//账户
        bundle.putParcelable(C2C_PAYEE_INFO, viewModel);//收款人信息
        payDoPaymentFragment.setArguments(bundle);
        start(payDoPaymentFragment);
    }

    /*** 查询收款人信息 失败 */
    @Override
    public void loadGetPayeeInfoFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /*** 查询账户类别成功 */
    @Override
    public void loadQRPayGetAccountCatalogSuccess(QRPayGetAccountCatalogResult qrPayGetAccountCatalogResult) {
        if (!"2".equals(qrPayGetAccountCatalogResult.getAccountCatalog()) &&
                !"3".equals(qrPayGetAccountCatalogResult.getAccountCatalog())){
            mPayeeInfoPresenter.loadGetPayeeInfo(currentQrCodeModel.getQrContentC2C());
        }else{
            iAccountCatalog ++;
            if (iAccountCatalog < mAccountBeansList.size()){
                c2cSelectAccount = mAccountBeansList.get(iAccountCatalog);
                mAccountCatalogPresenter.qRPayGetAccountCatalog(c2cSelectAccount.getAccountId());
            }else{
                closeProgressDialog();
                showAccountCatalogErrorDialog();
            }
        }
    }

    /*** 查询账户类别失败 */
    @Override
    public void loadQRPayGetAccountCatalogFail(BiiResultErrorException biiResultErrorException) {
        iAccountCatalog ++;
        if (iAccountCatalog < mAccountBeansList.size()){
            c2cSelectAccount = mAccountBeansList.get(iAccountCatalog);
            mAccountCatalogPresenter.qRPayGetAccountCatalog(c2cSelectAccount.getAccountId());
        }else {
            closeProgressDialog();
            showAccountCatalogErrorDialog();
        }
    }

    /**
     * 显示无可操作账户的错误对话框
     */
    private void showNoAccountErrorDialog(String errorMsg) {
        if (mQrNoAccountErrorDialog != null && mQrNoAccountErrorDialog.isShowing()){
            return;
        }
        mQrNoAccountErrorDialog = new TitleAndBtnDialog(mContext);
        mQrNoAccountErrorDialog.setBtnName(new String[] {getString(R.string.boc_common_sure)});
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
            public void onLeftBtnClick(View view) { }

            @Override
            public void onRightBtnClick(View view) {
                ActivityManager.getAppManager().finishActivity();
                mQrNoAccountErrorDialog.dismiss();
            }
        });
        mQrNoAccountErrorDialog.show();
    }


    /**
     * 显示二三类账户错误对话框
     */
    private void showAccountCatalogErrorDialog() {
        if (mAccountCatalogErrorDialog != null && mAccountCatalogErrorDialog.isShowing()){
            return;
        }
        mAccountCatalogErrorDialog = new TitleAndBtnDialog(mContext);
        mAccountCatalogErrorDialog.setBtnName(new String[] {getString(R.string.boc_common_sure)});
        mAccountCatalogErrorDialog.isShowTitle(false);
        mAccountCatalogErrorDialog.setCanceledOnTouchOutside(false);
        mAccountCatalogErrorDialog.setCancelable(false);
        mAccountCatalogErrorDialog.setNoticeContent("您的账户是二三类账户，无法使用该功能");
        mAccountCatalogErrorDialog.setDialogBtnClickListener(new TitleAndBtnDialog.DialogBtnClickCallBack() {
            @Override
            public void onLeftBtnClick(View view) { }

            @Override
            public void onRightBtnClick(View view) {
                ActivityManager.getAppManager().finishActivity();
                mQrNoAccountErrorDialog.dismiss();
            }
        });
        mQrNoAccountErrorDialog.show();
    }


    /**
     * 显示未设置支付密码的错误对话框
     */
    private void showNoPassErrorDialog() {
        if (mQrNoPassErrorDialog != null && mQrNoPassErrorDialog.isShowing()){
            return;
        }
        mQrNoPassErrorDialog = new TitleAndBtnDialog(mContext);
        mQrNoPassErrorDialog.setBtnName(new String[] {getString(R.string.boc_common_sure)});
        mQrNoPassErrorDialog.isShowTitle(false);
        mQrNoPassErrorDialog.setCanceledOnTouchOutside(false);
        mQrNoPassErrorDialog.setCancelable(false);
        mQrNoPassErrorDialog.setNoticeContent("使用该功能需要您设置支付密码");
        mQrNoPassErrorDialog.setDialogBtnClickListener(new TitleAndBtnDialog.DialogBtnClickCallBack() {
            @Override
            public void onLeftBtnClick(View view) {
                mQrNoPassErrorDialog.dismiss();
            }

            @Override
            public void onRightBtnClick(View view) {
                QRPaySetPayPwdFragment fragment = new QRPaySetPayPwdFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(SCAN_FROM_KEY, SCAN_FROM_SCAN);
                fragment.setArguments(bundle);
                startWithPop(fragment);
                mQrNoPassErrorDialog.dismiss();
            }
        });
        mQrNoPassErrorDialog.show();
    }


    /**
     * 显示二维码无商品信息的错误对话框
     */
    private void showQRNoParseErrorDialog() {
        if (mQRNoParseErrorDialog != null && mQRNoParseErrorDialog.isShowing()){
            return;
        }
        mQRNoParseErrorDialog = new TitleAndBtnDialog(mContext);
        mQRNoParseErrorDialog.setBtnName(new String[] {getString(R.string.boc_common_sure)});
        mQRNoParseErrorDialog.isShowTitle(false);
        mQRNoParseErrorDialog.setCanceledOnTouchOutside(false);
        mQRNoParseErrorDialog.setCancelable(false);
        mQRNoParseErrorDialog.setNoticeContent("暂时无法识别您提供的二维码");
        mQRNoParseErrorDialog.setDialogBtnClickListener(new TitleAndBtnDialog.DialogBtnClickCallBack() {
            @Override
            public void onLeftBtnClick(View view) {
            }

            @Override
            public void onRightBtnClick(View view) {
                ActivityManager.getAppManager().finishActivity();
                mQRNoParseErrorDialog.dismiss();
            }
        });
        mQRNoParseErrorDialog.show();
    }


}