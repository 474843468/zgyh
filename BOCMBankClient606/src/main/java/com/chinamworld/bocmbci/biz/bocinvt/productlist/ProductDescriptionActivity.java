package com.chinamworld.bocmbci.biz.bocinvt.productlist;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;
import com.chinamworld.bocmbci.fidget.BTCConstant;
import com.chinamworld.bocmbci.utils.ViewUtils;


/**
 * 理财产品说明书
 * 
 * @author wangmengmeng
 * 
 */
public class ProductDescriptionActivity extends BociBaseActivity {
	private static final String TAG = "ProductDescriptionActivity";
	/** 产品说明书页 */
	private View view;
	private WebView mWebView;
	private WebSettings mWebSettings;
	private String prodCode=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 为界面标题赋值
		setTitle(this.getString(R.string.description));
		// 右上角按钮赋值
		hineTitlebarLoginButton();
		setText(this.getString(R.string.close));
		// 添加布局
		view = addView(R.layout.bocinvt_description_activity);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		goneLeftView();
		prodCode=getIntent().getStringExtra(BocInvt.BOCINVT_BUYRES_PRODCODE_RES);
//		Toast.makeText(ProductDescriptionActivity.this, "http://srh.bankofchina.com/search/finprod/getProdPage.jsp?keyword="+prodCode,Toast.LENGTH_SHORT ).show();
		// 界面初始化
		initView();
	}

	/**
	 * 初始化页面控件
	 */
	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		View menuPopwindow = (View) findViewById(R.id.menu_popwindow);
		menuPopwindow.setVisibility(View.GONE);
		back.setVisibility(View.GONE);	
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		tabcontent.setPadding(0, 0, 0, 0);
		addView(R.layout.webview_layout);
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
//		mWebSettings.setPluginsEnabled(true);
		mWebSettings.setJavaScriptEnabled(true);
		ViewUtils.initWebView(mWebView);
		

		mWebView.setWebViewClient(new WebViewClient() {     
		      @Override     
		      public boolean shouldOverrideUrlLoading(WebView view, String url)     
		      {     
		        view.loadUrl(url);     
		        return true;     
		      }     
		    });     
		/**
		 * 当WebView内容影响UI时调用WebChromeClient的方法
		 */
		mWebView.setWebChromeClient(new WebChromeClient() {

		
			/**
			 * 处理JavaScript Alert事件
			 */
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					final JsResult result) {
				// 用Android组件替换
				BaseDroidApp.getInstanse().showMessageDialog(message,
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								BaseDroidApp.getInstanse()
										.dismissMessageDialog();
								result.confirm();
							}
						});
				return true;
			}

		});
		mWebSettings.setPluginState(PluginState.ON);
		// 设置支持JavaScript等
		mWebSettings.setBuiltInZoomControls(true);
		mWebSettings.setLightTouchEnabled(true);
		mWebSettings.setSupportZoom(false);
		mWebSettings.setSavePassword(false);
		mWebView.setHapticFeedbackEnabled(false);
		mWebView.loadUrl(BTCConstant.FIDGET_WEB_URL_CHANPINSHUOMINGSHU+prodCode);
//		mWebView.loadUrl("http://192.168.1.2:9194/BIISimulate/createFile");// 测试
			


	}
	
	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 关闭
			finish();
			overridePendingTransition(R.anim.no_animation,
					R.anim.slide_down_out);
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		hineTitlebarLoginButton();
		setText(this.getString(R.string.close));
	}

}
