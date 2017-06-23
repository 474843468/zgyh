package com.chinamworld.bocmbci.biz.plps.smallservice;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.ViewUtils;

/**民生缴费，商户简介-webview
 * @author Administrator zxj*/
public class LowServiceWebviewActivity extends PlpsBaseActivity{
	private static final String TAG = "WebViewForUrl"; 
	private WebView mWebView;
	private WebSettings mWebSettings;
	/** 主视图布局 */
	private LinearLayout tabcontent;// 主Activity显示
	/** 左侧返回按钮 */
	private Button back;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		tabcontent.setPadding(0, 0, 0, 0);
		hineLeftSideMenu();
		View menuPopwindow = (View) findViewById(R.id.menu_popwindow);
		menuPopwindow.setVisibility(View.VISIBLE);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		hineTitlebarLoginButton();
	}

	/**
	 * 初始化页面控件
	 */
	private void initView() {
		mRightButton.setVisibility(View.INVISIBLE);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		TextView tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setText("商户简介");
		addView(R.layout.plps_service_webview);
		Button btn_show = (Button) findViewById(R.id.btn_show);
		btn_show.setVisibility(View.GONE);
		back = (Button) findViewById(R.id.ib_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mWebView = (WebView) findViewById(R.id.webView1);
		mWebSettings = mWebView.getSettings();
		ViewUtils.initWebView(mWebView);
//		mWebSettings.setPluginsEnabled(true);
		mWebSettings.setJavaScriptEnabled(true);
		

		mWebView.setWebViewClient(new WebViewClient() {     
			@Override     
			public boolean shouldOverrideUrlLoading(WebView view, String url) {     
				view.loadUrl(url);
				return false;     
			}  
		    
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				LogGloble.i(TAG, "PageStart == url:" + url);
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				LogGloble.i(TAG, "PageFinish == url:" + url);
			}
			
			@Override
		    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//		    	handler.proceed();//接受证书
		    	LogGloble.e(TAG, error.toString());
		    }
		});
		mWebView.setWebChromeClient(new WebChromeClient());
		mWebSettings.setPluginState(PluginState.ON);
		// 设置支持JavaScript等
		mWebSettings.setBuiltInZoomControls(true);
		mWebSettings.setLightTouchEnabled(true);
		mWebSettings.setSupportZoom(false);
		mWebView.setHapticFeedbackEnabled(false);

//		String postData = getIntent().getStringExtra(WEB_POSTDATA);
//		mWebView.postUrl(SystemConfig.quickOpenJumpWebURL, EncodingUtils.getBytes(postData, "base64"));
//		mWebView.postUrl(SystemConfig.quickOpenJumpWebURL, postData.getBytes());
//		LogGloble.i(TAG, "URL:" + SystemConfig.quickOpenJumpWebURL);
//		LogGloble.i(TAG, getIntent().getStringExtra(WEB_POSTDATA));
		mWebView.loadUrl(getIntent().getStringExtra("infourl"));
		((Button) findViewById(R.id.ib_back)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LowServiceWebviewActivity.this.finish();
			}
		});

	}

	/**
	 * 在slidingbody中引入自己布局文件
	 * 
	 * @param resource
	 * @return 引入布局
	 */
	public View addView(int resource) {
		View view = LayoutInflater.from(this).inflate(resource, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		return view;
	}
}
