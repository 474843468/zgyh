package com.chinamworld.bocmbci.biz.onlineservice;

import java.util.Map;

import android.net.http.SslError;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.utils.ViewUtils;

/**
 * 在线客服
 * @author zxj*/
public class OnlineServiceWebviewActivity extends LineServiceBaseActivity{
	
	//主视图布局
	private LinearLayout tabcontent;
	private WebView webView;
	private WebSettings mWebSettings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		initView();
		tabcontent.setPadding(0, 0, 0, 0);
		setLeftButtonPopupGone();
	}
	private void initView(){
		tabcontent = (LinearLayout)findViewById(R.id.sliding_body);
		TextView titleText = (TextView)findViewById(R.id.tv_title);
		titleText.setText(R.string.online_service);
		addView(R.layout.online_service_webview);
		webView = (WebView)findViewById(R.id.webView1);
		mWebSettings = webView.getSettings();
		ViewUtils.initWebView(webView);
		mWebSettings.setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() {     
		      @Override     
		      public boolean shouldOverrideUrlLoading(WebView view, String url)     
		      {     
		    	  
		        view.loadUrl(url);     
		        return true;     
		      }  
		      public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){
               //handler.cancel(); // Android默认的处理方式
               handler.proceed();  // 接受所有网站的证书
               //handleMessage(Message msg); // 进行其他处理
		      }
		    });     
		
		webView.setWebChromeClient(new WebChromeClient());
//		mWebSettings.setPluginState(PluginState.ON);
//		// 可触摸放大缩小
		mWebSettings.setBuiltInZoomControls(true);
		
//		mWebSettings.setLightTouchEnabled(true);
//		mWebSettings.setSupportZoom(false);
//		webView.setHapticFeedbackEnabled(false);
		//排版适应屏幕
		mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		//webView双击变大，在双击后变小，当手动放大后，双击后可以恢复到原始大小
		mWebSettings.setUseWideViewPort(true); 
		//可以加载更过格式页面
		mWebSettings.setLoadWithOverviewMode(true);
		//是否保存数据
		mWebSettings.setSaveFormData(true);
		//设置滚动条隐藏
		mWebSettings.setGeolocationEnabled(true);
		//设置是否启用了DOM storage API
		mWebSettings.setDomStorageEnabled(true);
		
		webView.requestFocus();
		webView.setScrollBarStyle(0);
		
		//http://172.22.136.1/601ipad/onlineAN/onlineCustomer.html
		boolean isLogin = BaseDroidApp.getInstanse().isLogin();
		if(isLogin){
			Map<String, Object> loginMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
					.get(ConstantGloble.BIZ_LOGIN_DATA);
//			String customerName = (String) loginMap.get(Login.CUSTOMER_NAME);
//			String gender=LocalData.gender.get(loginMap.get(Login.GENDER));
			String loginNameMobile = (String)loginMap.get("mobile");
			webView.loadUrl(SystemConfig.OnLineService + "index.html?customerName="+loginNameMobile);
		}else {
			webView.loadUrl(SystemConfig.OnLineService + "index.html?customerName=");
//			webView.loadUrl("https://22.188.159.17/BOC_MOBILE/");
		}

		webView.setWebChromeClient(new WebChromeClient(){

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
		
				
				int height=(int) (webView.getHeight()/OnlineServiceWebviewActivity.this.getResources().getDisplayMetrics().density+ 0.5f );
//				String height=webView.getHeight()+"";
//				String height="300";
				// 页面加载完成后再调用JS方法
				if (newProgress == 100) {
					webView.loadUrl("javascript:AndroidSetCustomerHeight('" + height + "')");
//					webView.loadUrl("javascript:var num=445;var allContent = document.getElementById('allContent'); var keyboard = document.getElementById('keyboard');var keyHeight = parseFloat(getStyle(keyboard, 'height'));allContent.style.height=num-keyHeight+'px';allContent.style.bottom=keyHeight + 'px';allContent.style.position='fixed';");
				}
				super.onProgressChanged(view, newProgress);
			
			}
		});

	}
	
	private View addView(int resource){
		View view = LayoutInflater.from(this).inflate(resource, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		return view;
	}
}
