package com.chinamworld.bocmbci.biz.finc.accmanager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.utils.ViewUtils;

/**
 * 投资者权益须知
 * @author fsm
 *
 */
public class FincAccInvestorRightsAttentionActivity extends FincBaseActivity {

	private WebView webView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tabcontent.addView(LayoutInflater.from(this).inflate(R.layout.webview_layout, null));
		setTitle(R.string.finc_accMana_investorRightsAttention);
		webView = (WebView)findViewById(R.id.webView1);
		readHtmlFromAssets("file:///android_asset/page/InvestorRightsAttention.htm");
	}
	
	private void readHtmlFromAssets(String url){
		WebSettings webSettings = webView.getSettings();
//		webSettings.setLoadWithOverviewMode(true);
//		webSettings.setUseWideViewPort(true);
		webSettings.setBuiltInZoomControls(true);
//		webSettings.setLightTouchEnabled(true);
//		webSettings.setSupportZoom(true);
		webSettings.setSavePassword(false);
		ViewUtils.initWebView(webView);
		webView.setBackgroundColor(Color.TRANSPARENT);
		webView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		webView.setClickable(false);
		webView.clearView();
//		webView.clearCache(true);
//		webView.setFocusable(false);
		webView.loadUrl(url);
	}
	
	
	
}
