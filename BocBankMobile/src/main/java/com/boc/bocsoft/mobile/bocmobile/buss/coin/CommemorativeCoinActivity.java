package com.boc.bocsoft.mobile.bocmobile.buss.coin;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.WebUrl;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BaseMobileActivity;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;

/**
 * 纪念币activity
 * 
 * @author Administrator
 *
 */
public class CommemorativeCoinActivity extends BaseMobileActivity{


	private WebView webView;
	private Handler mHandler;
	private String url;
	protected static final String URL = "url";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mHandler = new Handler();
		//this.setContentView(R.layout.boc_activity_commemorative_coin);
		webView = (WebView)this.findViewById(R.id.webview);
		url = WebUrl.LIFE_SOUVENIRCOIN;
		startWebView(url);
	}
	
	@Override
	protected int getLayoutId() {
		
		return R.layout.boc_activity_commemorative_coin;
	}

	private void startWebView(String url) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			CookieManager.getInstance().setAcceptThirdPartyCookies(webView,true);
		}

		webView.setWebViewClient(new WebViewClient() {
	            
	            //If you will not use this method url links are opeen in new brower not in webview
	            @Override
	            public boolean shouldOverrideUrlLoading(WebView view, String url) {             
	                view.loadUrl(url);
	                return true;
	            }

	        });
	          
	         // Javascript inabled on webview 
	        webView.getSettings().setJavaScriptEnabled(true);
	         
	        // Other webview options

	        webView.getSettings().setLoadWithOverviewMode(true);
	        webView.getSettings().setUseWideViewPort(true);
	        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
	        webView.setScrollbarFadingEnabled(false);
	        //webView.getSettings().setBuiltInZoomControls(true);
	        webView.getSettings().setDomStorageEnabled(true);

					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
						webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
					}

		//Load url in webview
	        webView.setWebChromeClient(new WebChromeClient());
	        webView.addJavascriptInterface(new NativeAPI(), "NativeAPI");
	        webView.loadUrl(url);

	    }
	 

    final class NativeAPI{
    	@JavascriptInterface
        public void NATIVE_goBack(){
           mHandler.post(new Runnable() {
               @Override
               public void run() {
                   ActivityManager.getAppManager().finishActivity();
               }
           });
        }
    }
    
    /**
     * 是否显示标题栏，默认显示
     * 子类可以重写此方法，控制是否显示标题栏
     * @return
     */
    protected boolean isHaveTitleBarView() {
        return false;
    }
    
}
