package com.boc.bocsoft.mobile.bocmobile.buss.easybuss.ui;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConfig;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.anim.FragmentAnimator;
import com.boc.bocsoft.mobile.bocmobile.base.activity.web.model.WebInfoProxy;
import com.boc.bocsoft.mobile.bocmobile.base.activity.web.ui.WebFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.ErrorDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleCode;
import com.boc.bocsoft.mobile.bocmobile.buss.easybuss.model.EasyBussWebInfoProxy;
import com.boc.bocsoft.mobile.bocmobile.buss.easybuss.model.RedirectEzucBean;
import com.boc.bocsoft.mobile.bocmobile.buss.easybuss.presenter.EasyBussPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.openwealthmanager.ui.InvestTreatyFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.data.WealthViewData;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;

import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 中银E商入口页面
 */
public class EasyBussFragment extends WebFragment<RedirectEzucBean, EasyBussPresenter>
        implements EasyBussContract.View {

    @Override
    protected void setWebView() {
        super.setWebView();
        mWebView.setKey("app_ezdb");
    }

    @Override
    public void initData() {
        super.initData();
        Observable.timer(500, TimeUnit.MILLISECONDS)
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Action1<Long>() {
                      @Override
                      public void call(Long aLong) {
                          judgeInvestOpenState();
                      }
                  });
    }

    private void judgeInvestOpenState() {
        showLoadingDialog(true);
        getPresenter().qryInvestOpenState();
    }

    @Override
    public void onQryInvestOpenStateFailed(String errorMessage) {
        closeProgressDialog();
        ErrorDialog errorDialog = new ErrorDialog(mContext);
        errorDialog.setErrorData(errorMessage)
                   .setBtnText("确认")
                   .setOnBottomViewClickListener(new ErrorDialog.OnBottomViewClickListener() {
                       @Override
                       public void onBottomViewClick() {
                           EasyBussFragment.this.onClosed();
                       }
                   })
                   .show();
    }

    @Override
    public void onQryInvestOpenStateSuccess(Boolean result) {
        if (result) {
            isInvestProtocolOpened();
        } else {
            closeProgressDialog();
            isInvestProtocolClosed(WealthViewData.needsStatus(ModuleCode.MODULE_BALANCE_0000));
        }
    }

    private void isInvestProtocolClosed(boolean[] needs) {
        InvestTreatyFragment fragment = new InvestTreatyFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(FRAGMENTATION_ANI_SETTING,
                new FragmentAnimator(R.anim.boc_fragment_no_anim, R.anim.boc_fragment_outtoright,
                        R.anim.boc_fragment_infromleft, R.anim.boc_fragment_outtoleft));
        fragment.setArguments(bundle);
        fragment.setDefaultInvestFragment(needs, new EasyBussFragment());
        startWithPop(fragment);
    }

    private void isInvestProtocolOpened() {
        showLoadingDialog(true);
        getPresenter().onQryRedirectEzuc();
    }

    @Override
    protected WebInfoProxy<RedirectEzucBean> createWebInfoProxy() {
        return new EasyBussWebInfoProxy();
    }

    @Override
    protected EasyBussPresenter initPresenter() {
        return new EasyBussPresenter(this);
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    public void onQryRedirectEzucSuccess(RedirectEzucBean redirectEzucBean) {
        redirectEzucBean.setBocnetLoginName(
                ApplicationContext.getInstance().getUser().getLoginName());
        redirectEzucBean.setHeaderFlag("1");
        redirectEzucBean.setAccessChannel("mobileBank");
        mWebInfoProxy.setInfo(redirectEzucBean);
        mWebView.getWebView().clearCache(true);
        mWebView.setInfoProxy(mWebInfoProxy);
        mWebView.loadUrl(ApplicationConfig.EASY_BUSS_PATH);
    }

    @Override
    public void onQryRedirectEzucFailed(String errorMessage) {
        closeProgressDialog();
        showLocalErrorDialog(errorMessage);
    }

    private void showLocalErrorDialog(String errorMessage) {
        ErrorDialog errorDialog =
                new ErrorDialog(mContext).setBtnText("确认").setErrorData(errorMessage);
        errorDialog.setOnBottomViewClickListener(new ErrorDialog.OnBottomViewClickListener() {
            @Override
            public void onBottomViewClick() {
                EasyBussFragment.this.onClosed();
            }
        });
        errorDialog.show();
    }

    @Override
    public void onClosed() {
        ActivityManager.getAppManager().finishActivity();
        //ModuleActivityDispatcher.popToHomePage();
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void setWebSettings() {
        mWebView.clearDefaultSetting();
        WebSettings webSettings = mWebView.getWebView().getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowFileAccessFromFileURLs(true);
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        }
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        webSettings.setJavaScriptEnabled(true);// 用于JS回调
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);// 提高渲染的优先级
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setGeolocationEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings.setDefaultTextEncodingName("UTF-8");
        mWebView.getWebView().setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description,
                    String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                showLocalErrorDialog("页面加载失败");
            }
        });
    }

    /**
     * 设定当前Fragmemt动画,优先级比在SupportActivity里高
     */
    protected FragmentAnimator onCreateFragmentAnimation() {
        FragmentAnimator animator = new FragmentAnimator();
        animator.setEnter(R.anim.boc_fragment_no_anim);
        animator.setExit(R.anim.boc_fragment_no_anim);
        animator.setPopEnter(R.anim.boc_fragment_no_anim);
        animator.setPopExit(R.anim.boc_fragment_no_anim);
        return animator;
    }
    //@Override
    //public void reInit() {
    //    super.reInit();
    //    isInvestProtocolOpened();
    //}
}