package com.chinamworld.bocmbci.fidget;


/**
 * 选择服务地区的页面
 */
public class BTCServiceOrgList {
/*extends FidgetBaseActiviy {
	private LinearLayout layContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		btn_right.setVisibility(View.GONE);
		addView(R.layout.mobiletrans);
		setTitle(this
				.getString(BTCFidgetManager.getCityId() == 1 ? R.string.orgset
						: R.string.orgselect));
		// 后退
		// Button backButton = (Button) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				exit();
			}
		});
		ListView listView = BTCFidgetManager.getCityListView(this);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				if (BTCFidgetManager.getCityId() == -1) {// 切换机构
					BTCFidgetManager.setCity(position);
//					Intent intent = new Intent();
//					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					intent.setClass(BTCServiceOrgList.this,
//							FidgetMainActivity.class);
//					startActivity(intent);
					finish();
				} else {
					if (BTCFidgetManager.getCityId() == 1) {
						BTCFidgetManager.setCity(position);
					}
					//TODO test
					BTCFidgetManager.setCity(position);
					//获取程序列表
					new BTCFidgetManager().updataFidget(BTCServiceOrgList.this,
							position);
				}
			}
		});
		layContent = (LinearLayout) findViewById(R.id.mobiletrans_content);
		layContent.setPadding(10, 10, 10, 10);
		layContent.addView(listView);
	}

	public void exit() {
		// BTCSystemLog.i("refresh", "refresh" + BTCFidgetManager.getCityId());
		if (BTCFidgetManager.getCityId() == 0) {
			BTCFidgetManager.setFidgetList(BTCFidgetManager.defaultCityID);
			Intent intent = new Intent();
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			intent.setClass(BTCServiceOrgList.this, BTCServiceGrid.class);
			intent.setClass(BTCServiceOrgList.this, FidgetMainActivity.class);
			startActivity(intent);
		}
		finish();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
		}
		return true;
	}
*/
}
