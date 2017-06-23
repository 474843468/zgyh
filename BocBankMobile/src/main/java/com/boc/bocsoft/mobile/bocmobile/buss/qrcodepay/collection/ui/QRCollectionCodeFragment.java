package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.collection.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetAccountCatalog.QRPayGetAccountCatalogResult;
import com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPayeeResult.QRPayGetPayeeResultResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.collection.adapter.QrcollectionAdapterAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.collection.presenter.QRCollectionCodePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.payment.model.QRPayGetQRCodeViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.presenter.QRPayBasePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.presenter.QRPayGetAccountCatalogPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui.QRPayBaseContract;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui.QRPayGetAccountCatalogContract;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui.QRPayMainFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.utils.QrCodeUtils;
import com.boc.bocsoft.mobile.framework.utils.ResUtils;
import com.boc.bocsoft.mobile.framework.zxing.encode.QRCodeEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by fanbin on 16/9/29.
 */
public class QRCollectionCodeFragment extends BussFragment implements View.OnClickListener
        , QRCollectionCodeContract.GetQrCollectionCodeView, QRPayBaseContract.QrAccountBaseView
        , QRCollectionCodeContract.QrCollectionCodeView, QRPayGetAccountCatalogContract.QRPayGetAccountCatalogBaseView {
    private View mRootView;
    private LayoutInflater mInflater;

    /**
     * 页面View
     */

    //全屏的layout
    private FrameLayout flQrCollectionBigCode;
    //全屏页面的ImageView
    private ImageView ivQrCollectionBigCode;

    //标题左侧按钮
    private ImageView ivTitleLeft;
    //标题右侧 设置金额/清除金额按钮
    private static TextView iv_qrcollection_title_right;

    //二维码View
    private ImageView ivCodeQrCode;
    //全屏二维码View
    private ImageView ivBigCodeQrCode;

    private RelativeLayout shoukuanzongji_relative;
    //收款总计
    private TextView tv_shoukuanzongji;
    //收款个人集合
    private ListView lv_boc_fragment_qrcollection;

    private QrcollectionAdapterAdapter mQrcollectionAdapterAdapter;
    //刷新二维码按钮
    private ImageView ivRefreshQrCode;
    //默认账户账号
    private TextView tvDefaultPayAccount;
    //修改默认账户按钮
    private TextView tvChangeAccount;
    //扫二维码向我转账
    private static TextView tv_aboveto_qrcode1;
    //显示的金额
    private static TextView tv_aboveto_qrcode2;
    //备注
    private static TextView tv_aboveto_qrcode3;
    //白框显示的金额
    private TextView show_money;
    //白框显示的付款人姓名
    private TextView show_payeename;
    //白框
    private RelativeLayout show_relativelayout;

    /**
     * 数据
     */
    //二维码Code
    private String mCollectionCode = "";
    //二维码的构建
    private QRCodeEncoder mQRCodeEncoder;
    private QRCodeEncoder mQRCodeEncoderBig;
    //选择的AccountBean
    private AccountBean selectCollectionAccount;
    private AccountBean noselectCollectionAccount;
    // 选择账户页面选中的账户
    private Bundle accountBundle;

    private SelectAccoutFragment selectAccoutFragment;

    private QRCollectionCodePresenter mQRCollectionCodePresenter;
    private QRPayBasePresenter mPayBasePresenter;
    private QRCollectionCodePresenter mQRCollectPayeeResultPresenter;
    private QRPayGetAccountCatalogPresenter mPayGetAccountCatalogPresenter;

    private String str_money = "";
    private String str_beizhu = "";

    private Timer mTimer = new Timer();
    private Timer mTimer2 = new Timer();

    //二三类帐户的错误提示框    用于进入收款页面判断
    private TitleAndBtnDialog mQrNoUseAccountErrorDialog;
    private List<QRPayGetPayeeResultResult.ResultBean> mResultBeanList;
    private List<QRPayGetPayeeResultResult.ResultBean> mFrontResultBeanList;
    private List<QRPayGetPayeeResultResult.ResultBean> mShowResultBeanList;
    private Map<String, List<QRPayGetPayeeResultResult.ResultBean>> mMap;
    private ObjectAnimator anim;
    //用来记录走到第几个数据
    private int num;
    private boolean isQuery=true;
    private static final int SHOW_ANIMATION = 121291;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == SHOW_ANIMATION) {
                if (num < mShowResultBeanList.size()) {
                    num++;
                    anim.setDuration(3000);
                    anim.start();
                    show_money.setText(mShowResultBeanList.get(num - 1).getAmount() + "元");
                    show_payeename.setText(mShowResultBeanList.get(num - 1).getAccName());
                    int a = mFrontResultBeanList.size();
                    mFrontResultBeanList.add(mResultBeanList.get(a + num - 2));
                    mQrcollectionAdapterAdapter.updataList(mFrontResultBeanList);
                }
            }
        }
    };

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        this.mInflater = mInflater;
        mRootView = View.inflate(mContext, R.layout.boc_fragment_qrcollection_code_payment, null);
        mCollectionCode = getArguments().getString(QRPayMainFragment.QRPAY_BUNDLE_QRCODE, "");
        selectCollectionAccount = getArguments().getParcelable(QRPayMainFragment.C2C_ACCOUNT_BEAN);
        //调试使用
//        QRCollectionSuccessFragment qrCollectionSuccessFragment=new QRCollectionSuccessFragment();
//        start(qrCollectionSuccessFragment);

        return mRootView;
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if (resultCode == SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT) {
            selectCollectionAccount = data.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
//            tvDefaultPayAccount.setText(NumberUtils.formatCardNumberStrong(selectCollectionAccount.getAccountNumber()));
            tvDefaultPayAccount.setText(selectCollectionAccount.getNickName() + "("+ QrCodeUtils.getShortCardNum(selectCollectionAccount.getAccountNumber())+")");
            //切换账户后，需要重新请求二维码信息
            showLoadingDialog();
            mQRCollectionCodePresenter.loadQRPayGetQRCode(selectCollectionAccount.getAccountId(), str_money, str_beizhu);
            mResultBeanList = mMap.get(selectCollectionAccount.getAccountId());
            if (mResultBeanList != null)
                mQrcollectionAdapterAdapter.updataList(mResultBeanList);
        }
        if (resultCode == QRCollectionSetMoneyFragment.SET_MONEY_FRAGMENT) {
            str_money = (String) data.getCharSequence("str_money");
            str_beizhu = (String) data.getCharSequence("str_beizhu");

            setMoneyAndBeizhu(str_money, str_beizhu);
        }
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

        flQrCollectionBigCode = (FrameLayout) mRootView.findViewById(R.id.fl_qrcollection_code_big);
        ivQrCollectionBigCode = (ImageView) mRootView.findViewById(R.id.iv_qrcollection_code_big);

        ivCodeQrCode = (ImageView) mRootView.findViewById(R.id.iv_qrcollection_code_qrcode);
        ivBigCodeQrCode = (ImageView) mRootView.findViewById(R.id.iv_qrcollection_code_big);
        tvDefaultPayAccount = (TextView) mRootView.findViewById(R.id.tv_qrcollection_code_pay_account);
        tvChangeAccount = (TextView) mRootView.findViewById(R.id.tv_qrcollection_code_change_account);
        tv_aboveto_qrcode1 = (TextView) mRootView.findViewById(R.id.tv_aboveto_qrcode1);
        tv_aboveto_qrcode2 = (TextView) mRootView.findViewById(R.id.tv_aboveto_qrcode2);
        tv_aboveto_qrcode3 = (TextView) mRootView.findViewById(R.id.tv_aboveto_qrcode3);

        ivRefreshQrCode = (ImageView) mRootView.findViewById(R.id.iv_qrcode_collection_refresh_code);
        shoukuanzongji_relative = (RelativeLayout) mRootView.findViewById(R.id.shoukuanzongji_relative);
        tv_shoukuanzongji = (TextView) mRootView.findViewById(R.id.tv_shoukuanzongji);
        lv_boc_fragment_qrcollection = (ListView) mRootView.findViewById(R.id.lv_boc_fragment_qrcollection);
        mResultBeanList = new ArrayList<>();
        mFrontResultBeanList = new ArrayList<>();
        mShowResultBeanList = new ArrayList<>();
        mMap = new HashMap<>();
        show_money = (TextView) mRootView.findViewById(R.id.show_money);
        show_payeename = (TextView) mRootView.findViewById(R.id.show_payeename);
        show_relativelayout = (RelativeLayout) mRootView.findViewById(R.id.show_relativelayout);
    }

    @Override
    public void initData() {
        initCodeBuilder();
        mQRCollectionCodePresenter = new QRCollectionCodePresenter((QRCollectionCodeContract.GetQrCollectionCodeView) this);
        mPayBasePresenter = new QRPayBasePresenter((QRPayBaseContract.QrAccountBaseView) this);
        mQRCollectPayeeResultPresenter = new QRCollectionCodePresenter((QRCollectionCodeContract.QrCollectionCodeView) this);
        mPayGetAccountCatalogPresenter = new QRPayGetAccountCatalogPresenter(this);
        setCodeData();
        tvDefaultPayAccount.setText(selectCollectionAccount.getNickName() + "(" + QrCodeUtils.getShortCardNum(selectCollectionAccount.getAccountNumber()) + ")");
        mQrcollectionAdapterAdapter = new QrcollectionAdapterAdapter(mContext, mResultBeanList);
        lv_boc_fragment_qrcollection.setAdapter(mQrcollectionAdapterAdapter);
        setAnimation(show_relativelayout);

        mTimer2.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isQuery){

                    mQRCollectPayeeResultPresenter.loadQRPayGetTransRecord(mCollectionCode);
                }
            }
        }, 3000, 3000);

    }


    /**
     * 初始化Title
     */
    private void initQrPayTitleView() {
        ivTitleLeft = (ImageView) mRootView.findViewById(R.id.iv_qrcollection_title_left);

        ivTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleLeftIconClick();
            }
        });
        iv_qrcollection_title_right = (TextView) mRootView.findViewById(R.id.iv_qrcollection_title_right);

        iv_qrcollection_title_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleRightClick();
            }
        });

    }

    private void titleRightClick() {
        String rightText = iv_qrcollection_title_right.getText().toString();
        if ("设置金额".equals(rightText)) {
            QRCollectionSetMoneyFragment paymentFragment = new QRCollectionSetMoneyFragment();
            startForResult(paymentFragment, QRCollectionSetMoneyFragment.SET_MONEY_FRAGMENT);
        }
        if ("清除金额".equals(rightText)) {
            iv_qrcollection_title_right.setText("设置金额");
            tv_aboveto_qrcode1.setVisibility(View.VISIBLE);
            tv_aboveto_qrcode2.setVisibility(View.GONE);
            tv_aboveto_qrcode3.setVisibility(View.GONE);
            mQRCollectionCodePresenter.loadQRPayGetQRCode(selectCollectionAccount.getAccountId(), "", "");//
            showLoadingDialog();
        }
    }

    public void setMoneyAndBeizhu(String money, String beizhu) {

        if (!TextUtils.isEmpty(money)) {
            iv_qrcollection_title_right.setText("清除金额");
            tv_aboveto_qrcode2.setVisibility(View.VISIBLE);
            tv_aboveto_qrcode2.setText(money + "元");
            if ("添加付款备注".equals(beizhu) || TextUtils.isEmpty(beizhu)) {
                tv_aboveto_qrcode3.setVisibility(View.VISIBLE);
                tv_aboveto_qrcode3.setText(beizhu);
                beizhu = "";
            }

            mQRCollectionCodePresenter.loadQRPayGetQRCode(selectCollectionAccount.getAccountId(), money, beizhu);//yuanToFen(money)
            showLoadingDialog();
        }
        if (!"添加付款备注".equals(beizhu) || TextUtils.isEmpty(beizhu)) {
            tv_aboveto_qrcode3.setVisibility(View.VISIBLE);
            tv_aboveto_qrcode3.setText(beizhu);
        }

    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    /**
     * 设置页面二维码的数据
     */
    private void setCodeData() {
        Bitmap qrCodeBitmap = mQRCodeEncoder.encode(mCollectionCode);
        if (qrCodeBitmap == null) {
            Toast.makeText(mContext, "二维码生成错误，请重试", Toast.LENGTH_SHORT).show();
            ivCodeQrCode.setImageBitmap(null);
        } else {
            ivCodeQrCode.setImageBitmap(qrCodeBitmap);
        }

    }

    /**
     * 设置全屏的二维码
     */
    private void setBigQrCodeToView(String code) {
        Bitmap qrCodeBitmap = mQRCodeEncoderBig.encode(code);
        if (qrCodeBitmap == null) {
            ivBigCodeQrCode.setImageBitmap(null);
            Toast.makeText(mContext, "二维码生成错误，请重试", Toast.LENGTH_SHORT).show();
        } else {
            ivBigCodeQrCode.setImageBitmap(qrCodeBitmap);
        }
    }


    /**
     * 初始化二维码的参数
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

    }

    @Override
    public void setListener() {
        ivCodeQrCode.setOnClickListener(this);
        ivBigCodeQrCode.setOnClickListener(this);
        ivRefreshQrCode.setOnClickListener(this);
        tvChangeAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.fl_qrcollection_code_big) {//全屏的Layout
            flQrCollectionBigCode.setVisibility(View.GONE);
        } else if (viewId == R.id.iv_qrcollection_code_qrcode) {//二维码点击事件
            flQrCollectionBigCode.setVisibility(View.VISIBLE);
            setBigQrCodeToView(mCollectionCode);
        }
//        else if (viewId == R.id.iv_qrcode_collection_refresh_code) {//刷新二维码
//            showLoadingDialog();
//            mQRCollectionCodePresenter.loadQRPayGetQRCode(selectCollectionAccount.getAccountId(),str_money,str_beizhu);//
//        }
        else if (viewId == R.id.tv_qrcollection_code_change_account) {//更改默认账户
            changePayAccount();
        }
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
        mQRCollectionCodePresenter.loadQRPayGetQRCode(selectCollectionAccount.getAccountId(), str_money, str_beizhu);//"183157877"
    }

    @Override
    public void onDestroyView() {
        mQRCollectionCodePresenter.unsubscribe();
        mTimer.cancel();
        mTimer2.cancel();
        super.onDestroyView();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            isQuery=false;
        }else {
            isQuery=true;
        }
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
                    selectCollectionAccount = accountBean;
//                    noselectCollectionAccount = accountBean;
//                    mPayGetAccountCatalogPresenter.qRPayGetAccountCatalog(accountBean.getAccountId());
//                    showLoadingDialog("正在查询帐户信息,请稍等");
                    selectAccoutFragment.setFramgentResult(SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT, accountBundle);
                    selectAccoutFragment.pop();
                }

            }
        });
    }


    @Override
    public void loadQRCollectionGetQRCodeSuccess(QRPayGetQRCodeViewModel qrCodeViewModel) {
        closeProgressDialog();
//        mHandler.removeCallbacks(runGetQRCode);
//        mHandler.postDelayed(runGetQRCode, 60 * 1000);
        mCollectionCode = qrCodeViewModel.getSeqNo();

        setCodeData();


    }

    //获取二维码失败
    @Override
    public void loadQRCollectionGetQRCodeFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /***
     * 查询账户详情成功
     */
    @Override
    public void queryAccountDetailsSuccess(BigDecimal availableBalance) {
        closeProgressDialog();
        selectAccoutFragment.setFramgentResult(SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT, accountBundle);
        selectAccoutFragment.pop();
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
        selectAccoutFragment.setFramgentResult(SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT, accountBundle);
        selectAccoutFragment.pop();
    }

    /***
     * 查询信用卡账户详情失败
     */
    @Override
    public void queryCreditAccountDetailFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void loadQRPayGetTransRecordSuccess(QRPayGetPayeeResultResult ViewModel) {
        closeProgressDialog();
        if (isQuery){
            List<QRPayGetPayeeResultResult.ResultBean> beanList = ViewModel.getResultBeanList();
            mFrontResultBeanList = mResultBeanList;
            if (null!=mResultBeanList){
                mResultBeanList.clear();
            }
            if (beanList != null && beanList.size() != 0) {
                for (QRPayGetPayeeResultResult.ResultBean bean : beanList) {
                    if ("0".equals(bean.getTranStatus())) {
                        mResultBeanList.add(bean);
                        if (!mFrontResultBeanList.contains(bean)) {
                            mShowResultBeanList.add(bean);
                        }
                    }
                }
                showResultDialog();
                mMap.put(selectCollectionAccount.getAccountId(), mResultBeanList);
                if (mResultBeanList.size() <= 1) {
                    shoukuanzongji_relative.setVisibility(View.GONE);
                } else {
                    shoukuanzongji_relative.setVisibility(View.VISIBLE);
                }
            }

        }


    }

    @Override
    public void loadQRPayGetTransRecordFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }


    @Override
    public void loadQRPayGetAccountCatalogSuccess(QRPayGetAccountCatalogResult qrPayGetAccountCatalogResult) {
        closeProgressDialog();
        if ("1".equals(qrPayGetAccountCatalogResult.getAccountCatalog())) {
            selectCollectionAccount = noselectCollectionAccount;
            selectAccoutFragment.setFramgentResult(SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT, accountBundle);
            selectAccoutFragment.pop();
        } else if ("2".equals(qrPayGetAccountCatalogResult.getAccountCatalog()) || "3".equals(qrPayGetAccountCatalogResult.getAccountCatalog())) {
            showErrorDialog("二三类帐户不支持");
        } else {
            showErrorDialog("二三类帐户不支持");

        }
    }

    @Override
    public void loadQRPayGetAccountCatalogFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }


    //弹出结果框
    private void showResultDialog() {
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(SHOW_ANIMATION);
            }
        }, 0, 2000);


    }

    public void setAnimation(final View view) {
        anim = ObjectAnimator.ofFloat(view, "alpha", 0f, 0.8f, 0f);
        anim.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                view.setVisibility(View.GONE);
            }
        });

    }

    //把19.10元 变成1910分  服务器上送字段以分为单位的
    private String yuanToFen(String yuan) {
        String fen = "";
        String str1 = "";
        String str2 = "";
        if (!TextUtils.isEmpty(yuan)) {
            if (yuan.contains(".")) {
                String[] strs = yuan.split("\\.");
                if (strs.length == 2) {
                    str1 = strs[0];
                    if (yuan.contains(".00")) {
                        fen=str1+"00";
                    }else{
                        str2 = "0." + strs[1];
                        str2 = Double.parseDouble(str2) * 100 + "";

                        str2 = str2.substring(0, str2.length() -2);

                        fen=str1+str2;

                    }

                }

            }
        }
        return fen;
    }

}
