package com.chinamworld.bocmbci.biz.servicerecord;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.utils.ViewUtils;

public class ShopSourceActivity extends ServiceRecordBaseActivity {
	private RelativeLayout rl_bank;
	private RelativeLayout view;
	// 是否下载商店
	private boolean shopisdown = false;
	private Context context;
	private TextView down;
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
		if (isAvilible(ShopSourceActivity.this, "com.boc.bocop.container")) {
			shopisdown = true;
		}
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
					R.layout.shop_source_protocol, null);

			ImageView img_exit = (ImageView) contentView
					.findViewById(R.id.img_exit_accdetail);
			// 判断是否下载，并显示字体
			// 判断是否安装中银商店
			

			down = (TextView) contentView.findViewById(R.id.down_shop);
			if (shopisdown) {
				down.setText("打开(您已下载)");
				down.setTextColor(Color.RED);
				down.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
//						Intent i = new Intent();
//						ComponentName cn = new ComponentName("com.boc.bocop.container",
//								"com.boc.bocop.container.bocopshell.activity.ShellSplashActivity");
//						i.setComponent(cn);
//						startActivityForResult(i, RESULT_OK);
						//包名不对
						ShopSourceActivity.this.startActivity(ShopSourceActivity.this.getPackageManager()
								.getLaunchIntentForPackage("com.boc.bocop.container"));
					}
				});
			} else {
				down.setText("去下载");
				down.setTextColor(Color.BLUE);
				down.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Uri uri = Uri
								.parse(SHOP_URL);
						Intent it = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(it);
					}
				});
			}

			// 退出账户详情点击事件
			img_exit.setOnClickListener(exitClick);

			webView = (WebView) contentView.findViewById(R.id.webView1);
			ViewUtils.initWebView(webView);
			readHtmlFromAssets("file:///android_asset/page/shop_protocol.htm");

			return contentView;
		}

		private void readHtmlFromAssets(String url) {
			WebSettings webSettings = webView.getSettings();
			webSettings.setBuiltInZoomControls(true);
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
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		if (isAvilible(ShopSourceActivity.this, "com.boc.bocop.container")) {
			shopisdown = true;
		}
		if (shopisdown) {
			down.setText("打开(您已下载)");
			down.setTextColor(Color.RED);
			down.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					ShopSourceActivity.this.startActivity(ShopSourceActivity.this.getPackageManager()
							.getLaunchIntentForPackage("com.boc.bocop.container"));
				}
			});
		} else {
			down.setText("去下载");
			down.setTextColor(Color.BLUE);
			down.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Uri uri = Uri
							.parse(SHOP_URL);
					Intent it = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(it);
				}
			});
		}
		
		
		
		
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

}
