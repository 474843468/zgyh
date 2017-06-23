package com.chinamworld.bocmbci.biz.sbremit.mysbremit;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.utils.ViewUtils;

/**
 * @author Administrator
 *
 */
public class SBRemitSourceProtocolActivity extends BaseActivity {// implements
																	// OnClickListener
																	// {

	private RelativeLayout rl_bank;
	private RelativeLayout view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BaseDroidApp.getInstanse().setDialogAct(true);
		
		// 添加布局
		setContentView(R.layout.acc_for_dialog);
		getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);

		rl_bank = (RelativeLayout) findViewById(R.id.rl_bank);
		android.widget.RelativeLayout.LayoutParams lp = new android.widget.RelativeLayout.LayoutParams(
				LayoutValue.SCREEN_WIDTH, LayoutValue.SCREEN_HEIGHT * 7 / 8);
		rl_bank.setLayoutParams(lp);
		InflaterViewDialog inflaterdialog = new InflaterViewDialog(this);
		view = (RelativeLayout) inflaterdialog.initMessageDialogView(this,
				exitClick);

		rl_bank.removeAllViews();
		rl_bank.addView(view);
	}

	private OnClickListener exitClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setResult(RESULT_OK);
			finish();
		}
	};

	class InflaterViewDialog {
		private Context currentContent;
		private View contentView;
		private WebView webView;

		public InflaterViewDialog(Context currentContent) {
			this.currentContent = currentContent;
		}

		public View initMessageDialogView(Activity act,
				OnClickListener exitClick) {
			contentView = LayoutInflater.from(currentContent).inflate(
					R.layout.sbremit_source_protocol2, null);

			ImageView img_exit = (ImageView) contentView
					.findViewById(R.id.img_exit_accdetail);
			// 退出账户详情点击事件
			img_exit.setOnClickListener(exitClick);

			webView = (WebView) contentView.findViewById(R.id.webView1);
			readHtmlFromAssets("file:///android_asset/page/bremit_protocol.htm");

			return contentView;
		}

		private void readHtmlFromAssets(String url) {
			ViewUtils.initWebView(webView);
			WebSettings webSettings = webView.getSettings();
			// webSettings.setLoadWithOverviewMode(true);
			// webSettings.setUseWideViewPort(true);
			webSettings.setBuiltInZoomControls(true);
			// webSettings.setLightTouchEnabled(true);
			// webSettings.setSupportZoom(true);
			webView.setBackgroundColor(Color.TRANSPARENT);
			webView.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					return true;
				}
			});
			webView.setClickable(false);
			webView.clearView();
			// webView.clearCache(true);
			// webView.setFocusable(false);
			webView.loadUrl(url);
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		BaseDroidApp.getInstanse().setDialogAct(true);
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		BaseDroidApp.getInstanse().setDialogAct(false);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.OneTask;
	}

	// /** 主布局 */
	// private LinearLayout mainView;
	// private WebView webView;
	//
	// @Override
	// protected void onCreate(Bundle savedInstanceState) {
	//
	// super.onCreate(savedInstanceState);
	// setContentView(R.layout.biz_activity_layout);
	// initPulldownBtn(); // 加载上边下拉菜单
	// initFootMenu(); // 加载底部菜单栏
	// // 关闭
	// TextView tv = (TextView) findViewById(R.id.ib_top_right_btn);
	// tv.setText(R.string.close);
	// tv.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// finish();
	// }
	// });
	// setTitle(R.string.protocol_title);
	// // 隐藏返回
	// findViewById(R.id.ib_back).setVisibility(View.INVISIBLE);
	//
	// mainView = (LinearLayout) this.findViewById(R.id.sliding_body);
	// LayoutInflater inflater = LayoutInflater.from(this);
	// View view = inflater.inflate(R.layout.sbremit_source_protocol, null);
	// mainView.addView(view);
	//
	// // 隐藏左侧菜单
	// Button showBtn = (Button) this.findViewById(R.id.btn_show);
	// showBtn.setVisibility(View.GONE);
	//
	// // 隐藏底部菜单
	// LinearLayout footLayout = (LinearLayout)
	// this.findViewById(R.id.foot_layout);
	// footLayout.setVisibility(View.GONE);
	//
	// webView = (WebView)this.findViewById(R.id.webView1);
	// readHtmlFromAssets("file:///android_asset/page/sbremit_protocol.htm");
	// }

	// private void readHtmlFromAssets(String url){
	// WebSettings webSettings = webView.getSettings();
	// // webSettings.setLoadWithOverviewMode(true);
	// // webSettings.setUseWideViewPort(true);
	// webSettings.setBuiltInZoomControls(true);
	// // webSettings.setLightTouchEnabled(true);
	// // webSettings.setSupportZoom(true);
	// webView.setBackgroundColor(Color.TRANSPARENT);
	// webView.setOnLongClickListener(new OnLongClickListener() {
	//
	// @Override
	// public boolean onLongClick(View v) {
	// return true;
	// }
	// });
	// webView.setClickable(false);
	// webView.clearView();
	// // webView.clearCache(true);
	// // webView.setFocusable(false);
	// webView.loadUrl(url);
	// }
	//
	// @Override
	// public void onClick(View paramView) {
	//
	// }

}
