package com.chinamworld.bocmbci.biz.crcd.mycrcd;

import com.chinamworld.bocmbci.base.activity.BaseActivity;
//import com.chinamworld.bocmbci.biz.crcd.adapter.CrcdAccountAdapter;

/**
 * 没有信用卡的展示
 * 
 * @author huangyuchao
 * 注释此类 因 dex方法超出限制  sunh 
 */
public class NoCreditCardActivity extends BaseActivity {
//
//	/** 主视图布局 */
//	private LinearLayout tabcontent;
//
//	View view;
//
//	Button btn_right;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//
//		setContentView(R.layout.biz_activity_layout);
//
//		tabcontent = (LinearLayout) findViewById(R.id.sliding_body);
//		// 为界面标题赋值
//		setTitle(this.getString(R.string.mycrcd1));
//		view = LayoutInflater.from(this).inflate(R.layout.crcd_nocreditcard,
//				null);
//		tabcontent.addView(view);
//
//		// 初始化底部菜单栏
//		initFootMenu();
//
//		btn_right = (Button) findViewById(R.id.ib_top_right_btn);
//
//		btn_right.setVisibility(View.GONE);
//		init();
//	}
//
//	LinearLayout nocardLayout;
//	Button btn_description;
//
//	/** 左侧返回按钮 */
//	protected Button back;
//
//	String fromInner;
//
//	TextView tv_inner;
//
//	public void init() {
//
//		fromInner = this.getIntent().getStringExtra("fromInner");
//
//		tv_inner = (TextView) view.findViewById(R.id.tv_inner);
//
//		// if ("fromInner".equals(fromInner)) {
//		// tv_inner.setText(this
//		// .getString(R.string.mycrcd_credit_nocard_inner));
//		// }
//
//		nocardLayout = (LinearLayout) view.findViewById(R.id.nocardLayout);
//
//		btn_description = (Button) view.findViewById(R.id.btn_description);
//		// 进入账户关联
//		btn_description.setOnClickListener(goRelevanceClickListener);
//
//		back = (Button) findViewById(R.id.ib_back);
//		// 坐上角返回点击事件
//		clickTopLeftClick();
//
//		Button btn = (Button) findViewById(R.id.btn_show);
//		btn.setVisibility(View.GONE);
//	}
//
//	/** 左上角返回点击事件 */
//	public void clickTopLeftClick() {
//		back.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});
//	}
//
//	/** 进行自助关联监听事件 */
//	OnClickListener goRelevanceClickListener = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			// Intent intent = new Intent(MyCreditCardActivity.this,
//			// AccInputRelevanceAccountActivity.class);
//			// startActivity(intent);
//
//			startActivityForResult((new Intent(NoCreditCardActivity.this,
//					AccInputRelevanceAccountActivity.class)),
//					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
//			// finish();
//
//		}
//	};
//
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (requestCode == ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE
//				&& resultCode == RESULT_OK) {
//			// 请求信用卡列表
//			requestCrcdList();
//		}
//
//		super.onActivityResult(requestCode, resultCode, data);
//	};
//
//	public void requestCrcdList() {
//		// 通讯开始,展示通讯框
//		BaseHttpEngine.showProgressDialog();
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(Crcd.QRY_CRCD_LIST_API);
//		Map<String, Object> params = new HashMap<String, Object>();
//		String[] s = { ConstantGloble.ZHONGYIN, ConstantGloble.GREATWALL,
//				ConstantGloble.SINGLEWAIBI };
//		params.put(Crcd.CRCD_ACCOUNTTYPE_REQ, s);
//		biiRequestBody.setParams(params);
//		HttpManager.requestBii(biiRequestBody, this, "requestCrcdListCallBack");
//	}
//
//	/** 请求回来的账户列表信息 (我的信用卡) */
//	public static List<Map<String, Object>> bankAccountList = new ArrayList<Map<String, Object>>();
//
//	/**
//	 * 请求信用卡列表回调
//	 */
//	public void requestCrcdListCallBack(Object resultObj) {
//		BaseHttpEngine.dissMissProgressDialog();
//		BiiResponse response = (BiiResponse) resultObj;
//		List<BiiResponseBody> list = response.getResponse();
//		BiiResponseBody body = list.get(0);
//		List<Map<String, Object>> returnList = (List<Map<String, Object>>) body
//				.getResult();
//		if (StringUtil.isNullOrEmpty(returnList) || returnList.size() == 0) {
//			BaseHttpEngine.dissMissProgressDialog();
//		}
//		for (int i = 0; i < returnList.size(); i++) {
//			if (ConstantGloble.ZHONGYIN.equals(returnList.get(i).get(
//					Crcd.CRCD_ACCOUNTTYPE_RES))
//					|| ConstantGloble.GREATWALL.equals(returnList.get(i).get(
//							Crcd.CRCD_ACCOUNTTYPE_RES))
//					|| ConstantGloble.SINGLEWAIBI.equals(returnList.get(i).get(
//							Crcd.CRCD_ACCOUNTTYPE_RES))) {
//				bankAccountList.add(returnList.get(i));
//			}
//		}
//
//		if (bankAccountList == null || bankAccountList.size() == 0) {
//
//		} else {
//			Intent it = new Intent(NoCreditCardActivity.this,
//					MyCreditCardActivity.class);
//			startActivity(it);
//			finish();
//		}
//
//	}
//
	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.OneTask;
	}
//
	

}
