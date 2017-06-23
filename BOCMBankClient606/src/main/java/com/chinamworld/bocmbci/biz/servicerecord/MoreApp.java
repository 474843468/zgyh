package com.chinamworld.bocmbci.biz.servicerecord;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;

public class MoreApp extends ServiceRecordBaseActivity implements
		OnClickListener {
	// 中行易商
	private LinearLayout china_bank_shop;
	private Button btn_shop;

	// 中过银行缤纷生活
	private LinearLayout china_bank_live;
	private Button btn_live;
	// 是否下载商店
	private boolean shopisdown = false;
	// 是否下载缤纷生活
	private boolean liveisdown = false;
	// g更多
	private TextView shop_more, live_more;
	private Context context;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 判断是否安装中银商店
		if (isAvilible(MoreApp.this, "com.boc.bocop.container")) {
			shopisdown = true;
		}
		// 判断是否安装中银缤纷生活
		if (isAvilible(MoreApp.this, "com.forms")) {
			liveisdown = true;
		}

		View childView = mainInflater.inflate(R.layout.more_app, null);
		tabcontent.addView(childView);
		setTitle(R.string.more_app);
		right.setVisibility(View.GONE);
		china_bank_shop = (LinearLayout) findViewById(R.id.china_bank_shop);
		china_bank_live = (LinearLayout) findViewById(R.id.china_bank_live);
		shop_more = (TextView) findViewById(R.id.shop_more);
		live_more = (TextView) findViewById(R.id.live_more);
		btn_shop = (Button) findViewById(R.id.btn_shop);
		if (shopisdown) {
			btn_shop.setText("打开");
			btn_shop.setBackgroundResource(R.drawable.open);
			btn_shop.setTextColor(Color.RED);
		} else {
			btn_shop.setText("下载");
			btn_shop.setBackgroundResource(R.drawable.download);
			btn_shop.setTextColor(Color.BLUE);
		}
		btn_live = (Button) findViewById(R.id.btn_live);
		if (liveisdown) {
			btn_live.setText("打开");
			btn_live.setBackgroundResource(R.drawable.open);
			btn_live.setTextColor(Color.RED);
		} else {
			btn_live.setText("下载");
			btn_live.setBackgroundResource(R.drawable.download);
			btn_live.setTextColor(Color.BLUE);
		}
		btn_shop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (shopisdown) {
					// 暂时不知道包名
					MoreApp.this.startActivity(MoreApp.this.getPackageManager()
							.getLaunchIntentForPackage(
									"com.boc.bocop.container"));
				} else {
					Uri uri = Uri.parse(SHOP_URL);
					Intent it = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(it);
				}

			}
		});

		btn_live.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (liveisdown) {
					// Intent i = new Intent();
					// ComponentName cn = new ComponentName("com.forms",
					// "com.tencent.mm.WeiXinActivity");
					// i.setComponent(cn);
					// startActivityForResult(i, RESULT_OK);
					MoreApp.this.startActivity(MoreApp.this.getPackageManager()
							.getLaunchIntentForPackage("com.forms"));
				} else {
					Uri uri = Uri
							.parse(LIVE_URL);
					Intent it = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(it);
				}
			}
		});
		china_bank_shop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intentshop = new Intent(MoreApp.this,
						ShopSourceActivity.class);
				startActivity(intentshop);
			}
		});
		china_bank_live.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intentlive = new Intent(MoreApp.this,
						LiveSourceActivity.class);
				startActivity(intentlive);

			}
		});
		shop_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intentshop = new Intent(MoreApp.this,
						ShopSourceActivity.class);
				startActivity(intentshop);
			}
		});
		live_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intentlive = new Intent(MoreApp.this,
						LiveSourceActivity.class);
				startActivity(intentlive);

			}
		});

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		// 判断是否安装中银商店
		if (isAvilible(MoreApp.this, "com.boc.bocop.container")) {
			shopisdown = true;
		}
		// 判断是否安装中银缤纷生活
		if (isAvilible(MoreApp.this, "com.forms")) {
			liveisdown = true;
		}

		if (shopisdown) {
			btn_shop.setText("打开");
			btn_shop.setBackgroundResource(R.drawable.open);
			btn_shop.setTextColor(Color.RED);
		} else {
			btn_shop.setText("下载");
			btn_shop.setBackgroundResource(R.drawable.download);
			btn_shop.setTextColor(Color.BLUE);
		}
		if (liveisdown) {
			btn_live.setText("打开");
			btn_live.setBackgroundResource(R.drawable.open);
			btn_live.setTextColor(Color.RED);
		} else {
			btn_live.setText("下载");
			btn_live.setBackgroundResource(R.drawable.download);
			btn_live.setTextColor(Color.BLUE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
