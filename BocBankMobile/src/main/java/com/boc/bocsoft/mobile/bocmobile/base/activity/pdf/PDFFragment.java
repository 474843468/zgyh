package com.boc.bocsoft.mobile.bocmobile.base.activity.pdf;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;

/**
 * PDF 显示Fragment
 * @dingeryue
 */
public class PDFFragment extends BussFragment implements PDFContract.PDFView{

    public static String PARAM_URI = "URIPARAM";

    public static PDFFragment newInstance(Uri pdfUri){
        PDFFragment pdfFragment = new PDFFragment();
        Bundle bundle = new Bundle();
        if(pdfUri != null){
            bundle.putParcelable(PARAM_URI,pdfUri);
        }
        pdfFragment.setArguments(bundle);
        return pdfFragment;
    }

    private View mRoot;
    private WebView mWebView;
    private Button btnKnow;

    private PDFContract.PDFPresenter pdfPresenter;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRoot = new View(mInflater.getContext());
        return mRoot;
    }

    @Override
    public void initView() {
       /* mWebView = (WebView) mRoot.findViewById(R.id.view_web);
        btnKnow = (Button) mRoot.findViewById(R.id.bt_know);

        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.setScrollbarFadingEnabled(false);
        //webView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        //mWebView.getSettings().setSupportZoom(true);
        //mWebView.getSettings().setBuiltInZoomControls(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }

      *//*  mWebView.setWebViewClient(new WebViewClient() {
            //If you will not use this method url links are opeen in new brower not in webview
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });*/


    }

    //private  String pdfUrl = "file:///android_asset/pdf/pdf.html?file=";
    private String pdfUrl = "file:///android_asset/androidpdf/web/viewer.html?file=";
    @Override
    public void initData() {
        //mWebView.setDefaultLoadUrl(URL + getArguments().get(PROD_CODE));

        Bundle arguments = getArguments();
        if(arguments == null)pop();
        Uri uri = arguments.getParcelable(PARAM_URI);
        if(uri == null)return;
        pdfPresenter = new PDFPresenterImpl(this);
        LogUtils.d("dding","加载pdf:"+uri);
        pdfPresenter.loadPDF(uri);
    }

    @Override
    public void setListener() {
    /*    btnKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });*/
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
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override public void onDestroy() {
        super.onDestroy();
        pdfPresenter.unsubscribe();
        //mWebView.getWebView().destroy();
    }

    @Override public void showLoading(String msg) {
        showLoadingDialog();
    }

    @Override public void closeLoading() {
        closeProgressDialog();
    }

    @Override public void onLoadSuccess(Uri uri) {

        if(uri == null){
            onLoadFaile();
            return;
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(uri,"application/pdf");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);

    }

    @Override public void onLoadFaile() {
        ToastUtils.show("加载失败");
        pop();
    }

    @Override
    public void onCloseLoadingDialog() {
        super.onCloseLoadingDialog();
        pdfPresenter.unsubscribe();
        pop();
    }

    @Override public void setPresenter(PDFContract.PDFPresenter presenter) {

    }
}
