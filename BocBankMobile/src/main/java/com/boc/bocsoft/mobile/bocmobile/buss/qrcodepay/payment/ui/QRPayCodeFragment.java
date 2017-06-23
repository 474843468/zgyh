package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.payment.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.payment.model.QRPayGetQRCodeViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.payment.model.QRPayTransInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.payment.presenter.QRPayCodePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.model.PassFreeInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.model.PayQuotaViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.presenter.QRPayBasePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui.QRPayBaseContract;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui.QRPayMainFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui.QRPayResetPayPwdFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.utils.QrCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.widget.QRPayErrorDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.widget.QRPayPwdDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.widget.ScreenshotContentObserver;
import com.boc.bocsoft.mobile.framework.utils.ResUtils;
import com.boc.bocsoft.mobile.framework.zxing.encode.BarCodeEncoder;
import com.boc.bocsoft.mobile.framework.zxing.encode.QRCodeEncoder;
import com.boc.bocsoft.mobile.framework.zxing.utils.BitmapUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 二维码支付 - 付款 - 付款码付款
 * Created by wangf on 2016/8/25.
 */
public class QRPayCodeFragment extends BussFragment implements View.OnClickListener,QRPayCodeContract.QrCodeView,
        QRPayCodeContract.GetQrCodeView, QRPayBaseContract.QrAccountBaseView, QRPayBaseContract.QrSetCardBaseView, QRPayBaseContract.QRQueryPassFreeInfoBaseView{

    private View mRootView;
    private LayoutInflater mInflater;

    /**
     * 页面View
     */

    //全屏的layout
    private FrameLayout flQrPayBigCode;
    //全屏页面的ImageView
    private ImageView ivQrPayBigCode;

    //标题左侧按钮
    private ImageView ivTitleLeft;
    //条形码View
    private ImageView ivCodeBar;
    //二维码View
    private ImageView ivCodeQrCode;
    //刷新二维码按钮
    private ImageView ivRefreshQrCode;
    //默认账户账号
    private TextView tvDefaultPayAccount;
    //修改默认账户按钮
    private TextView tvChangeAccount;



    /**
     * 逻辑处理
     */
    //二维码的构建
    private QRCodeEncoder mQRCodeEncoder;
    private QRCodeEncoder mQRCodeEncoderBig;
    //条形码的构建
    private BarCodeEncoder mBarCodeEncoder;
    private BarCodeEncoder mBarCodeEncoderBig;

    //二维码付款service通信处理类
    private QRPayCodePresenter mQrGetCodePresenter;
    private QRPayCodePresenter mQrPaymentPresenter;
    private QRPayBasePresenter mPayBasePresenter;
    private QRPayBasePresenter mPassFreeInfoBasePresenter;
    private QRPayBasePresenter mSetCardBasePresenter;
    //二维码付款UI层model
    private QRPayGetQRCodeViewModel mQrPayGetQRCodeViewModel;
    //交易确认信息model
    private QRPayTransInfoViewModel mQrayTransInfoViewModel;
    //支付限额
    public PayQuotaViewModel mPayQuotaViewModel;
    //小额免密信息
    public PassFreeInfoViewModel mPassFreeInfoViewModel;

    //错误对话框，并刷新二维码
    private QRPayErrorDialog errorDialogToRefresh;
    //密码输入错误提示框
    private TitleAndBtnDialog mQrPwdErrorDialog;

    /**
     * 数据
     */
    //二维码Code
    private String mPayCode = "";
    //支付确认信息刷新频率
    private int mCodeFreq = 5;
    //二维码刷新频率
    private int mCodeLife = 60;
    //选择的AccountBean
    private AccountBean selectPayAccount;
    // 选择账户页面选中的账户
    private Bundle accountBundle;
    //记录请求确认信息的次数
    private int iConfirmCount;
    private boolean confirmInfoFlag;
    // 是否通过选择账户页面请求数据
    private boolean isSelectAccount = false;

    private SelectAccoutFragment selectAccoutFragment;


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        this.mInflater = mInflater;
        mRootView = View.inflate(mContext, R.layout.boc_fragment_qrpay_code_payment, null);

        mPayCode = getArguments().getString(QRPayMainFragment.QRPAY_BUNDLE_QRCODE, "");
        mCodeFreq = Integer.parseInt(getArguments().getString(QRPayMainFragment.QRPAY_BUNDLE_FREQ, "5"));
        mCodeLife = Integer.parseInt(getArguments().getString(QRPayMainFragment.QRPAY_BUNDLE_CODE_LIFE, "60"));
        selectPayAccount = QRPayMainFragment.mDefaultAccountBean;

        return mRootView;
    }


    @Override
    protected boolean isHaveTitleBarView() {
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
        initQrPayTitleView();

        flQrPayBigCode = (FrameLayout) mRootView.findViewById(R.id.fl_qrpay_code_big);
        ivQrPayBigCode = (ImageView) mRootView.findViewById(R.id.iv_qrpay_code_big);

        ivCodeBar = (ImageView) mRootView.findViewById(R.id.iv_qrpay_code_bar);
        ivCodeQrCode = (ImageView) mRootView.findViewById(R.id.iv_qrpay_code_qrcode);

        tvDefaultPayAccount = (TextView) mRootView.findViewById(R.id.tv_qrpay_code_pay_account);
        tvChangeAccount = (TextView) mRootView.findViewById(R.id.tv_qrpay_code_change_account);

        ivRefreshQrCode = (ImageView) mRootView.findViewById(R.id.iv_qrcode_pay_refresh_code);
    }


    /**
     * 初始化Title
     */
    private void initQrPayTitleView() {
        ivTitleLeft = (ImageView) mRootView.findViewById(R.id.iv_qrpay_title_left);
        ivTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleLeftIconClick();
            }
        });
    }

    @Override
    public void initData() {
        initCodeBuilder();

        mQrGetCodePresenter = new QRPayCodePresenter((QRPayCodeContract.GetQrCodeView)this);
        mQrPaymentPresenter = new QRPayCodePresenter((QRPayCodeContract.QrCodeView)this);
        mPayBasePresenter = new QRPayBasePresenter((QRPayBaseContract.QrAccountBaseView)this);
        mSetCardBasePresenter = new QRPayBasePresenter((QRPayBaseContract.QrSetCardBaseView)this);
        mPassFreeInfoBasePresenter = new QRPayBasePresenter((QRPayBaseContract.QRQueryPassFreeInfoBaseView)this);

        mQrPayGetQRCodeViewModel = new QRPayGetQRCodeViewModel();
        mPayQuotaViewModel = new PayQuotaViewModel();
        mPassFreeInfoViewModel = new PassFreeInfoViewModel();

        setCodeData();
//        tvDefaultPayAccount.setText(NumberUtils.formatCardNumberStrong(selectPayAccount.getAccountNumber()));
        tvDefaultPayAccount.setText(selectPayAccount.getNickName() + "("+QrCodeUtils.getShortCardNum(selectPayAccount.getAccountNumber())+")");

        iConfirmCount = 0;
        
        handler.postDelayed(runConfirmInfo, mCodeFreq * 1000);
        handler.postDelayed(runGetQRCode, mCodeLife * 1000);
        
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	ScreenshotContentObserver.startObserve(mContext);
    }

    @Override
    public void setListener() {
        ivCodeBar.setOnClickListener(this);
        ivCodeQrCode.setOnClickListener(this);
        flQrPayBigCode.setOnClickListener(this);
        ivRefreshQrCode.setOnClickListener(this);
        tvChangeAccount.setOnClickListener(this);
        
        ScreenshotContentObserver.setScreenshotListener(new ScreenshotContentObserver.ScreenshotCallback() {
			
			@Override
			public void doReport() {
				getActivity().runOnUiThread(new Runnable() {
		            @Override
		            public void run() {
		            	showErrorDialog("付款码截屏后将自动失效，以避免资金风险");
		            	Log.i("-----", "截图");
		            }
		        });
				handler.removeCallbacks(runGetQRCode);
		        handler.removeCallbacks(runConfirmInfo);
				mQrGetCodePresenter.loadQRPayGetQRCode(selectPayAccount.getAccountId());
			}
		});
    }



    @Override
    public void reInit() {
        mContentView.removeAllViews();
        View contentView = onCreateView(mInflater);
        if (contentView != null) {
            mContentView.addView(contentView);
        }
        initView();
        initData();
        setListener();

        showLoadingDialog();
        mQrGetCodePresenter.loadQRPayGetQRCode(selectPayAccount.getAccountId());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SecurityVerity.getInstance(getActivity());
    }

    @Override
    public void onDestroyView() {
        mQrGetCodePresenter.unsubscribe();
        mQrPaymentPresenter.unsubscribe();
        handler.removeCallbacks(runConfirmInfo);
        handler.removeCallbacks(runGetQRCode);
        ScreenshotContentObserver.stopObserve();
        super.onDestroyView();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden){
            ScreenshotContentObserver.stopObserve();
        }else{
            ScreenshotContentObserver.startObserve(mContext);
        }
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if (resultCode == SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT){
            selectPayAccount = data.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
//            tvDefaultPayAccount.setText(NumberUtils.formatCardNumberStrong(selectPayAccount.getAccountNumber()));
            tvDefaultPayAccount.setText(selectPayAccount.getNickName() + "("+QrCodeUtils.getShortCardNum(selectPayAccount.getAccountNumber())+")");
            //切换账户后，重新开启截屏监测
            ScreenshotContentObserver.startObserve(mContext);
//            showLoadingDialog();
//            mQrGetCodePresenter.loadQRPayGetQRCode(selectPayAccount.getAccountId());
        }else if(resultCode == SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT_NULL){
//            handler.postDelayed(runConfirmInfo, mCodeFreq * 1000);
//            handler.postDelayed(runGetQRCode, mCodeLife * 1000);
            showLoadingDialog();
            mQrGetCodePresenter.loadQRPayGetQRCode(selectPayAccount.getAccountId());
            ScreenshotContentObserver.startObserve(mContext);
        }
    }


    /**
     * 设置页面二维码和条形码的数据
     */
    private void setCodeData() {
        Bitmap qrCodeBitmap = mQRCodeEncoder.encode(mPayCode);
        if (qrCodeBitmap == null) {
            Toast.makeText(mContext, "二维码生成错误，请重试", Toast.LENGTH_SHORT).show();
            ivCodeQrCode.setImageBitmap(null);
        } else {
            ivCodeQrCode.setImageBitmap(qrCodeBitmap);
        }

        Bitmap barCodeBitmap = mBarCodeEncoder.creatBarcode(mContext, mPayCode);
        if (barCodeBitmap == null) {
            Toast.makeText(mContext, "条形码生成错误，请重试", Toast.LENGTH_SHORT).show();
            ivCodeBar.setImageBitmap(null);
        } else {
            ivCodeBar.setImageBitmap(barCodeBitmap);
        }
    }

    /**
     * 设置全屏的二维码
     */
    private void setBigQrCodeToView(String code) {
        Bitmap qrCodeBitmap = mQRCodeEncoderBig.encode(code);
        if (qrCodeBitmap == null) {
            ivQrPayBigCode.setImageBitmap(null);
            Toast.makeText(mContext, "二维码生成错误，请重试", Toast.LENGTH_SHORT).show();
        } else {
            ivQrPayBigCode.setImageBitmap(qrCodeBitmap);
        }
    }

    /**
     * 设置全屏的条形码
     */
    private void setBigBarCodeToView(String code) {
        Bitmap barCodeBitmap = mBarCodeEncoderBig.creatBarcode(mContext, code);
        if (barCodeBitmap == null) {
            ivQrPayBigCode.setImageBitmap(null);
            Toast.makeText(mContext, "条形码生成错误，请重试", Toast.LENGTH_SHORT).show();
        } else {
            ivQrPayBigCode.setImageBitmap(BitmapUtils.getRotateBitmap(barCodeBitmap, 90));
        }

    }


    /**
     * 初始化条形码和二维码的参数
     */
    private void initCodeBuilder() {
        mQRCodeEncoder = new QRCodeEncoder.Builder()
                .setOutputBitmapHeight(ResUtils.dip2px(mContext, 200))
                .setOutputBitmapWidth(ResUtils.dip2px(mContext, 200))
                .build();
        mQRCodeEncoderBig = new QRCodeEncoder.Builder()
                .setOutputBitmapHeight(ResUtils.dip2px(mContext, 300))
                .setOutputBitmapWidth(ResUtils.dip2px(mContext, 300))
                .build();
        mBarCodeEncoder = new BarCodeEncoder.Builder()
                .setOutputBitmapWidth(ResUtils.dip2px(mContext, 300))
                .setOutputBitmapHeight(ResUtils.dip2px(mContext, 60))
                .setIsDisplayCodeText(true)
                .setDisplayCodeTextSize(18)
                .setOutputMarginBar2Text(10)
                .setIsDisplayCodeTextBold(true)
                .setIsTextDisplayTop(true)
                .setIsFormatCodeNumber(true)
                .build();
        mBarCodeEncoderBig = new BarCodeEncoder.Builder()
                .setOutputBitmapWidth(ResUtils.dip2px(mContext, 500))
                .setOutputBitmapHeight(ResUtils.dip2px(mContext, 150))
                .setIsDisplayCodeText(true)
                .setDisplayCodeTextSize(20)
                .setOutputMarginBar2Text(15)
                .setIsDisplayCodeTextBold(true)
                .setIsTextDisplayTop(true)
                .setIsFormatCodeNumber(true)
                .build();
    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.fl_qrpay_code_big) {//全屏的Layout
            flQrPayBigCode.setVisibility(View.GONE);
        } else if (viewId == R.id.iv_qrpay_code_bar) {//条形码点击事件
            flQrPayBigCode.setVisibility(View.VISIBLE);
            setBigBarCodeToView(mPayCode);
        } else if (viewId == R.id.iv_qrpay_code_qrcode) {//二维码点击事件
            flQrPayBigCode.setVisibility(View.VISIBLE);
            setBigQrCodeToView(mPayCode);
        } else if (viewId == R.id.iv_qrcode_pay_refresh_code) {//刷新二维码
        	handler.removeCallbacks(runGetQRCode);
            handler.removeCallbacks(runConfirmInfo);
            iConfirmCount = 0;
            showLoadingDialog();
            mQrGetCodePresenter.loadQRPayGetQRCode(selectPayAccount.getAccountId());
        } else if (viewId == R.id.tv_qrpay_code_change_account) {//更改默认账户
        	ScreenshotContentObserver.stopObserve();
        	handler.removeCallbacks(runGetQRCode);
            handler.removeCallbacks(runConfirmInfo);
            iConfirmCount = 0;
            changePayAccount();
        }
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
                // added by wangf on 2016-12-6 14:19:11 在选择账户页面请求二维码数据
                isSelectAccount = true;
                showLoadingDialog();
                mQrGetCodePresenter.loadQRPayGetQRCode(accountBean.getAccountId());

                // changed by wangf on 2016-12-6 14:17:29 由于不需要请求账户详情，此处直接返回
//                String accountType = accountBean.getAccountType();
//                if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(accountType) ||
//                        ApplicationConst.ACC_TYPE_GRE.equals(accountType)){
//                    selectAccoutFragment.setFramgentResult(SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT, accountBundle);
//                    selectAccoutFragment.pop();
////                    //信用卡账户详情
////                    showLoadingDialog();
////                    mPayBasePresenter.queryCreditAccountDetail(accountBean.getAccountId(), ApplicationConst.CURRENCY_CNY);
//                }else if (ApplicationConst.ACC_TYPE_BRO.equals(accountType)){
//                    selectAccoutFragment.setFramgentResult(SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT, accountBundle);
//                    selectAccoutFragment.pop();
////                    //借记卡账户详情
////                    showLoadingDialog();
////                    mPayBasePresenter.queryAccountDetails(accountBean.getAccountId());
//                }
            }
        });
    }



    /**
     * 用户输入密码 进行反扫支付
     * @param random
     */
    private void handleConfirmInfo(String random){
        QRPayPwdDialog qrPayPwdDialog = new QRPayPwdDialog(getActivity());
        qrPayPwdDialog.setDialogData("向 " + mQrPayGetQRCodeViewModel.getMerchantName() + " 付款", mQrPayGetQRCodeViewModel.getAmount(), random);
        qrPayPwdDialog.setQRPayDialogListener(new QRPayPwdDialog.QRPayPwdDialogCallBack() {
            @Override
            public void onCancel() {
                showLoadingDialog();
                //请求二维码
                mQrGetCodePresenter.loadQRPayGetQRCode(selectPayAccount.getAccountId());
            }

            @Override
            public void onNumCompleted(String encryptRandomNum, String encryptPassword, String mVersion) {
                showLoadingDialog();
                //反扫支付
                mQrPaymentPresenter.loadQRPayDoScannedPayment(encryptPassword, encryptRandomNum, mQrPayGetQRCodeViewModel.getConfirmInfoConversationID());
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
     * 页面二维码等的定时器
     */
    Handler handler = new Handler();
    Runnable runConfirmInfo = new Runnable() {
        @Override
        public void run() {
            //查询反扫后的交易确认通知
            confirmInfoFlag = false;
            mQrPaymentPresenter.loadQRPayGetConfirmInfo();
            handler.postDelayed(this, mCodeFreq * 1000);
        }
    };
    Runnable runGetQRCode = new Runnable() {
        @Override
        public void run() {
            //获取二维码
            mQrGetCodePresenter.loadQRPayGetQRCode(selectPayAccount.getAccountId());
            handler.postDelayed(this, mCodeLife * 1000);
        }
    };


    /**
     * 获取二维码成功
     * @param qrCodeViewModel
     */
    @Override
    public void loadQRPayGetQRCodeSuccess(QRPayGetQRCodeViewModel qrCodeViewModel) {
        closeProgressDialog();
        handler.removeCallbacks(runGetQRCode);
        handler.removeCallbacks(runConfirmInfo);
        iConfirmCount = 0;
        confirmInfoFlag = false;

        if (isSelectAccount){
            selectAccoutFragment.setFramgentResult(SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT, accountBundle);
            selectAccoutFragment.pop();
            isSelectAccount = false;
        }

        mPayCode = qrCodeViewModel.getSeqNo();
        setCodeData();

        handler.postDelayed(runConfirmInfo, mCodeFreq * 1000);
        handler.postDelayed(runGetQRCode, mCodeLife * 1000);
    }

    /**
     * 获取二维码失败
     * @param biiResultErrorException
     */
    @Override
    public void loadQRPayGetQRCodeFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        showErrorDialog(biiResultErrorException.getErrorMessage());
    }

    /**
     * 查询反扫后的交易确认通知 成功
     */
    @Override
    public void loadQRPayGetConfirmInfoSuccess(QRPayGetQRCodeViewModel qrCodeViewModel) {

        iConfirmCount = 0;

        LogUtils.i("confirmInfoFlag------ " + confirmInfoFlag);

        if (!confirmInfoFlag) {
            confirmInfoFlag = true;
            LogUtils.i("confirmInfoFlag------ " + confirmInfoFlag);
            //ResStatus为0时该二维码有交易，为1时无交易
            if ("1".equals(qrCodeViewModel.getResStatus())) {
                //无交易时继续查询交易信息
            } else if ("0".equals(qrCodeViewModel.getResStatus())) {
                handler.removeCallbacks(runConfirmInfo);
                handler.removeCallbacks(runGetQRCode);

                mQrPayGetQRCodeViewModel = qrCodeViewModel;

                showLoadingDialog();
                //查询支付限额信息
                mPassFreeInfoBasePresenter.loadQRPayGetPayQuota(selectPayAccount.getAccountId(), "01");
            }
        }
    }

    /**
     * 查询反扫后的交易确认通知 失败
     * @param biiResultErrorException
     */
    @Override
    public void loadQRPayGetConfirmInfoFail(BiiResultErrorException biiResultErrorException) {
    	iConfirmCount++; 
    	if(iConfirmCount == 6){
    		showErrorDialogToRefresh(biiResultErrorException.getErrorMessage());
    	}
    }

    /**
     * 反扫支付成功
     */
    @Override
    public void loadQRPayDoScannedPaymentSuccess() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*** 查询反扫支付交易信息 */
                mQrPaymentPresenter.loadQRPayGetTransInfo(mQrPayGetQRCodeViewModel.getSettleKey());
            }
        }, 1000 * 10);
    }

    /**
     * 反扫支付失败
     */
    @Override
    public void loadQRPayDoScannedPaymentFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        if ("qrpassword.wrong".equals(biiResultErrorException.getErrorCode())){
            showPwdErrorDialog();
        }else {
            showErrorDialogToRefresh(biiResultErrorException.getErrorMessage());
        }
    }

    /*** 获取随机数成功 */
    @Override
    public void loadGetRandomSuccess(String random) {
        closeProgressDialog();
        handleConfirmInfo(random);
    }

    /*** 获取随机数失败 */
    @Override
    public void loadGetRandomFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        showErrorDialogToRefresh(biiResultErrorException.getErrorMessage());

    }

    /*** 查询反扫支付交易信息 成功 */
    @Override
    public void loadQRPayGetTransInfoSuccess(QRPayTransInfoViewModel infoViewModel) {
        //若未设置默认卡，此处需要设置默认卡
        if (QRPayMainFragment.isSetDefaultCard){
            closeProgressDialog();
            QRPayCodeResultFragment qrPayCodeResultFragment = new QRPayCodeResultFragment();
            Bundle bundle = new Bundle();
            bundle.putString("AccountNumber", selectPayAccount.getAccountNumber());
            bundle.putParcelable("GetTransInfo", infoViewModel);
            bundle.putParcelable("GetQRCode", mQrPayGetQRCodeViewModel);
            qrPayCodeResultFragment.setArguments(bundle);
            startWithPop(qrPayCodeResultFragment);
        }else{
            mQrayTransInfoViewModel = infoViewModel;
            showLoadingDialog();
            //设置默认卡
            mSetCardBasePresenter.loadQRPaySetDefaultCard(selectPayAccount.getAccountId());
        }
    }

    /*** 查询反扫支付交易信息 失败 */
    @Override
    public void loadQRPayGetTransInfoFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        showErrorDialogToRefresh(biiResultErrorException.getErrorMessage());
    }

    /*** 查询账户详情成功 */
    @Override
    public void queryAccountDetailsSuccess(BigDecimal availableBalance) {
        closeProgressDialog();
        selectAccoutFragment.setFramgentResult(SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT, accountBundle);
        selectAccoutFragment.pop();
    }

    /*** 查询账户详情失败 */
    @Override
    public void queryAccountDetailsFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
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

        QRPayCodeResultFragment qrPayCodeResultFragment = new QRPayCodeResultFragment();
        Bundle bundle = new Bundle();
        bundle.putString("AccountNumber", selectPayAccount.getAccountNumber());
        bundle.putParcelable("GetTransInfo", mQrayTransInfoViewModel);
        bundle.putParcelable("GetQRCode", mQrPayGetQRCodeViewModel);
        qrPayCodeResultFragment.setArguments(bundle);
        startWithPop(qrPayCodeResultFragment);
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

        QRPayCodeResultFragment qrPayCodeResultFragment = new QRPayCodeResultFragment();
        Bundle bundle = new Bundle();
        bundle.putString("AccountNumber", selectPayAccount.getAccountNumber());
        bundle.putParcelable("GetTransInfo", mQrayTransInfoViewModel);
        bundle.putParcelable("GetQRCode", mQrPayGetQRCodeViewModel);
        qrPayCodeResultFragment.setArguments(bundle);
        startWithPop(qrPayCodeResultFragment);
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
            if(QrCodeUtils.isCompareAmountCanNext(mQrPayGetQRCodeViewModel.getAmount(),
                    QrCodeUtils.getFormatTransQuota(mPassFreeInfoViewModel.getPassFreeAmount()))){
                //反扫支付
                mQrPaymentPresenter.loadQRPayDoScannedPayment("", "", mQrPayGetQRCodeViewModel.getConfirmInfoConversationID());
            }else{
                //查询随机数
                mQrPaymentPresenter.loadGetRandom();
            }
        }else if("0".equals(mPassFreeInfoViewModel.getPassFreeFlag())) {
            //查询随机数
            mQrPaymentPresenter.loadGetRandom();
        }
    }

    /**
     * 查询小额免密信息失败
     * @param biiResultErrorException
     */
    @Override
    public void loadQRPayGetPassFreeInfoFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        showErrorDialogToRefresh(biiResultErrorException.getErrorMessage());
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
            if (QrCodeUtils.isCompareAmountCanNext(mQrPayGetQRCodeViewModel.getAmount(), QrCodeUtils.getFormatTransQuota(mPayQuotaViewModel.getCardTransQuota()))){
                //查询小额免密信息
                mPassFreeInfoBasePresenter.loadQRPayGetPassFreeInfo(selectPayAccount.getAccountId());
            }else {
                closeProgressDialog();
                showErrorDialogToRefresh("此账户单笔付款限额为" + MoneyUtils.transMoneyFormat(
                        QrCodeUtils.getFormatTransQuotaNoSign(mPayQuotaViewModel.getCardTransQuota()), ApplicationConst.CURRENCY_CNY)+ "元，已超出");
            }
        }else if (ApplicationConst.ACC_TYPE_BRO.equals(accountType)){
            if (QrCodeUtils.isCompareAmountCanNext(mQrPayGetQRCodeViewModel.getAmount(), QrCodeUtils.getFormatTransQuota(mPayQuotaViewModel.getCardPayQuota()))){
                //查询小额免密信息
                mPassFreeInfoBasePresenter.loadQRPayGetPassFreeInfo(selectPayAccount.getAccountId());
            }else {
                closeProgressDialog();
                showErrorDialogToRefresh("此账户单笔付款限额为" + MoneyUtils.transMoneyFormat(
                        QrCodeUtils.getFormatTransQuotaNoSign(mPayQuotaViewModel.getCardPayQuota()), ApplicationConst.CURRENCY_CNY)+ "元，已超出");
            }
        }

//        if (QrCodeUtils.isCompareAmountCanNext(mQrPayGetQRCodeViewModel.getAmount(), QrCodeUtils.getFormatTransQuota(mPayQuotaViewModel.getCardTransQuota()))){
//            //查询小额免密信息
//            mPassFreeInfoBasePresenter.loadQRPayGetPassFreeInfo(selectPayAccount.getAccountId());
//        }else {
//            closeProgressDialog();
//            showErrorDialog("此账户单笔付款限额为" + MoneyUtils.transMoneyFormat(
//                    QrCodeUtils.getFormatTransQuotaNoSign(mPayQuotaViewModel.getCardTransQuota()), ApplicationConst.CURRENCY_CNY)+ "元，已超出");
//        }
    }

    /**
     * 查询支付限额失败
     */
    @Override
    public void loadQRPayGetPayQuotaFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        showErrorDialogToRefresh(biiResultErrorException.getErrorMessage());
    }


    /**
     * 报错弹出框，并刷新二维码
     */
    private void showErrorDialogToRefresh(String errorMessage) {
        errorDialogToRefresh = new QRPayErrorDialog(mContext);
        errorDialogToRefresh.setBtnText("确认");
        errorDialogToRefresh.setCancelable(false);
        errorDialogToRefresh.setErrorData(errorMessage);
        if (!errorDialogToRefresh.isShowing()) {
            errorDialogToRefresh.show();
        }
        errorDialogToRefresh.setOnBottomViewClickListener(new QRPayErrorDialog.OnBottomViewClickListener() {
            @Override
            public void onBottomViewClick() {
                showLoadingDialog();
                mQrGetCodePresenter.loadQRPayGetQRCode(selectPayAccount.getAccountId());
            }
        });
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
                bundle.putInt(QRPayResetPayPwdFragment.RESET_PASS_FROM_KEY, QRPayResetPayPwdFragment.RESET_PASS_FROM_C2B_FANSAO);
                QRPayResetPayPwdFragment fragment = new QRPayResetPayPwdFragment();
                fragment.setArguments(bundle);
                start(fragment);
                mQrPwdErrorDialog.dismiss();
            }

            @Override
            public void onRightBtnClick(View view) {
                showLoadingDialog();
//                //查询支付限额信息
//                mPassFreeInfoBasePresenter.loadQRPayGetPayQuota(selectPayAccount.getAccountId(), "01");
                //查询随机数
                mQrPaymentPresenter.loadGetRandom();
                mQrPwdErrorDialog.dismiss();
            }
        });
        mQrPwdErrorDialog.show();
    }


}