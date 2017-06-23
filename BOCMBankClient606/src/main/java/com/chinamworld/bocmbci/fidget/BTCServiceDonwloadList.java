package com.chinamworld.bocmbci.fidget;

/**
 * 添加特色服务
 *
 */
public class BTCServiceDonwloadList {
/*extends FidgetBaseActiviy {
	private LinearLayout layContent;
	MyAdapter checkedTextViewAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tabcontent.setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.common_bottom_padding_new));
		addView(R.layout.mobiletrans);
//		layTop = (RelativeLayout) this.findViewById(R.id.mobiletrans_top);
//		TextView title = new TextView(this);
//		title.setTextColor(Color.WHITE);
//		title.setTextSize(16);
//		title.setText(this.getString(R.string.fidgetadd));
//		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//				ViewGroup.LayoutParams.WRAP_CONTENT, // width
//				ViewGroup.LayoutParams.WRAP_CONTENT); // height
//		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
//		layTop.addView(title, lp);
		setTitle(this.getString(R.string.fidgetadd));
		// 后退
//		Button backButton = (Button) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				exit();
			}
		});

		BTCFidgetManager.setFidgetList(BTCFidgetManager.defaultCityID);
		btn_right.setVisibility(View.GONE);
		layContent = (LinearLayout) findViewById(R.id.mobiletrans_content);
		ListView listView = new ListView(this);
		listView.setFadingEdgeLength(0);
		listView.setScrollingCacheEnabled(false);
		checkedTextViewAdapter = new MyAdapter(this);
		listView.setAdapter(checkedTextViewAdapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setCacheColorHint(Color.TRANSPARENT);
		layContent.addView(listView);
		if (BTCFidgetManager.fidgetDownloadList.size() <= 0){
			BaseDroidApp.getInstanse().createDialog("", this.getResources().getString(R.string.orgService), new OnClickListener() {
				@Override
				public void onClick(View v) {
					BaseDroidApp.getInstanse().dismissErrorDialog();
					finish();
				}
			});
		}
	}

	public void exit() {
		finish();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
		}
		return true;
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		checkedTextViewAdapter.notifyDataSetChanged();
		
	}
	

	public class MyAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public MyAdapter(Context c) {
			this.inflater = LayoutInflater.from(c);
		}

		@Override
		public int getCount() {
			return BTCFidgetManager.fidgetDownloadList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		*//**
		 * 设置数据源与行View关联 设置行中个组件的事件响应 返回设置好的View
		 *//*
		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// 取得要显示的行View
			View myView = inflater.inflate(R.layout.order_list_item, null);
			TextView tv = (TextView) myView.findViewById(R.id.textCity);
			final BTCFidget fid = BTCFidgetManager.fidgetDownloadList.get(arg0);
			tv.setText(fid.getName());
			final int arg = arg0;
			Button detail = (Button) myView.findViewById(R.id.ButtonDetail);
			
			detail.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					BTCFidgetManager.fidgetID = arg;
					new BTCFidgetManager().updataDetail(
							BTCServiceDonwloadList.this, arg);
				}
			});
			Button download = (Button) myView.findViewById(R.id.ButtonDownload);
			download.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if(!BTCFidgetManager.fidgetListId.contains(fid.getID())){
					BTCFidgetManager.fidgetID = arg;
					new BTCFidgetManager().download(
							BTCServiceDonwloadList.this, -1);
					}
				}
			});
			if(BTCFidgetManager.fidgetListId.contains(fid.getID())){
				download.setText(R.string.haveDowned);
				download.setTextColor(getResources().getColor(R.color.gray));
				download.setEnabled(false);
			}else{
				download.setText(R.string.downed);
				download.setEnabled(true);
				download.setTextColor(getResources().getColor(R.color.black));
				
			}
			return myView;
		}
	}

	// 处理下载
		public Handler handlerDownload = new Handler(new Callback() {

			@Override
			public boolean handleMessage(Message msg) {
				// TODO Auto-generated method stub
				checkedTextViewAdapter.notifyDataSetChanged();
				CustomDialog.toastShow(BaseDroidApp.getContext(), getResources().getString(
						R.string.loadAccomplished));
				return true;
			}
		});
*/
}
