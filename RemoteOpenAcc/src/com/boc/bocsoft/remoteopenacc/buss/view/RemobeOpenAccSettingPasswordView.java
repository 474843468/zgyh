package com.boc.bocsoft.remoteopenacc.buss.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.boc.bocma.global.config.MAGlobalConfig;
import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.remoteopenacc.R;
import com.boc.bocsoft.remoteopenacc.buss.model.queryelecaccopeningbank.QueryElecAccOpeningBankResponseModel.BocBankInfo;

import android.webkit.JavascriptInterface;

/**
 * 设置密码视图
 *
 * @author lxw
 */
public class RemobeOpenAccSettingPasswordView extends LinearLayout {

    private Context mContext;
    private WebView mWebView;
    private Handler mHandler = new Handler();
    private SettingPasswordCallback mCallBack;
    private BocBankInfo mBankInfo;
    private String mAccUse;
    private String mOpenAccReason;

    public RemobeOpenAccSettingPasswordView(Context context, String serverRandom) {
        super(context);
        mContext = context;
        initView();
        initData();
    }

    public RemobeOpenAccSettingPasswordView(Context context) {
        super(context);
        mContext = context;
        initView();
        initData();
    }

    private void initView() {
        mWebView = new WebView(mContext);
        initWebView(mWebView);
        addView(mWebView);
    }

    public void initSipbox(String serverRandom) {
        mWebView.loadUrl("javascript:init('" + serverRandom + "')");
    }

    /**
     * 设置webview
     *
     * @param webView
     */
    @SuppressLint({"SetJavaScriptEnabled"})
    private void initWebView(final WebView webView) {

        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        int header = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 197, mContext.getResources()
                        .getDisplayMetrics());
        int height = wm.getDefaultDisplay().getHeight();

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, height - header);
        webView.setLayoutParams(lp);
        // webView.loadUrl("file:///android_asset/bocroa_www/index.html");
        WebSettings webSettings = webView.getSettings();
        // 设置支持js
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSaveFormData(false);

        webView.setWebChromeClient(new SettingPasswordWebChromeClient());
        webView.loadUrl("file:///android_asset/bocroa_www/index.html");
        webView.addJavascriptInterface(new SettingPasswordJavaScriptInterface(
                webView), "settingPassword");

    }

    private void initData() {

    }

    public void setmCallBack(SettingPasswordCallback mCallBack) {
        this.mCallBack = mCallBack;
    }

    final class SettingPasswordJavaScriptInterface {

        private WebView jsWebview;

        public SettingPasswordJavaScriptInterface(WebView webView) {
            jsWebview = webView;
        }

        /**
         * 设置密码提交
         *
         * @param passwordValue
         * @param passwordRamdom
         * @param affirmPassValue
         * @param affirmPassRandom
         */
        @JavascriptInterface
        public void settingSubmit(final String passwordValue,
                                  final String passwordRamdom, final String affirmPassValue,
                                  final String affirmPassRandom) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mCallBack != null) {
                        // if (StringUtil.isNullOrEmpty(mAccUse)) {
                        // mCallBack.onError(
                        // "",
                        // mContext.getString(R.string.bocroa_select_acc_use_null_message));
                        // return;
                        // }
                        if (mBankInfo == null) {
                            if (MAGlobalConfig.ISDEMO) {
                                mBankInfo = new BocBankInfo();
                                mBankInfo.orgCode = "00";
                                mBankInfo.orgName = "beijingfenahng";
                                mBankInfo.orgType = "0";
                            } else {
                                mCallBack.onError(
                                        "",
                                        mContext.getString(R.string.bocroa_select_open_bank_null_message));
                                return;
                            }
                        }
                        if (StringUtil.isNullOrEmpty(mOpenAccReason)) {
                            mCallBack.onError(
                                    "",
                                    mContext.getString(R.string.bocroa_select_open_reason_null_message));
                            return;
                        }
                        if (mBankInfo != null) {
                            if (MAGlobalConfig.ISDEMO) {
                                mBankInfo.orgCode = "00";
                                mBankInfo.orgName = "beijingfenahng";
                                mBankInfo.orgType = "";
                            }
                            mCallBack.onSettingPassword(passwordValue,
                                    passwordRamdom, affirmPassValue,
                                    affirmPassRandom, mBankInfo);
                        }
                    }
                }
            });

        }

        /**
         * 选择开户行
         */
        @JavascriptInterface
        public void selectOpenBank() {
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    if (mCallBack != null) {
                        mCallBack.onQueryOpenAccBank();
                    }

                }
            });

        }

        /**
         *
         */
        // public void selectAccUse() {
        // mHandler.post(new Runnable() {
        //
        // @Override
        // public void run() {
        // if (mCallBack != null) {
        // mCallBack.onAccUse();
        // }
        //
        // }
        // });
        //
        // }

        /**
         * 选择开卡类型
         */
        @JavascriptInterface
        public void selectOpenAccType() {
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    if (mCallBack != null) {
                        mCallBack.onOpenAccType();
                    }

                }
            });

        }

        /**
         * 加密出错处理
         *
         * @param errorCode
         * @param errorMessage
         */
        @JavascriptInterface
        public void hastErrorHandler(final String errorCode,
                                     final String errorMessage) {
            Log.i("hastErrorHandler", errorCode + ":" + errorMessage);
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    if (mCallBack != null) {
                        mCallBack.onError(errorCode, errorMessage);
                    }

                }
            });
        }
    }

    final class SettingPasswordWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }

    }

    public void setSelectedOpenBank(final BocBankInfo pos) {
        mBankInfo = pos;
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                mWebView.loadUrl("javascript:setSelectedOpenBank('"
                        + pos.orgName + "')");

            }

        });
    }

    /**
     * 设置账户用途
     *
     * @param accUse
     */
    // public void setAccuse(final String accUse) {
    // mHandler.post(new Runnable() {
    //
    // @Override
    // public void run() {
    // mAccUse = accUse;
    // mWebView.loadUrl("javascript:setSelectAccUse('" + accUse + "')");
    // }
    //
    // });
    // }

    /**
     * 设置开户原因
     *
     * @param openAccReason
     */
    public void setOpenAccType(final String openAccReason) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                mOpenAccReason = openAccReason;
                mWebView.loadUrl("javascript:setSelectOpenAccType('"
                        + openAccReason + "')");
            }

        });
    }

    /**
     * 设置密码回调
     *
     * @author lxw
     */
    public static interface SettingPasswordCallback {
        public void onQueryOpenAccBank();

        public void onSettingPassword(String password, String password_RC,
                                      String affirmPass, String affirmPass_RC, BocBankInfo mBankInfo);

        public void onError(String errorCode, String errorMessage);

        /**
         * 账户用途
         *
         * @author lgw
         */
        // public void onAccUse();

        /**
         * 开户原因
         *
         * @author lgw
         */
        public void onOpenAccType();
    }
}
