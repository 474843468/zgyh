package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;

/**
 * 信用卡设定菜单选择界面
 * 
 * @author huangyuchao
 * 注释此类 因 dex方法超出限制  sunh 
 */
public class MyCardSetupMenuActivity extends CrcdBaseActivity {
//
//	/** 电子现金账户列表页 */
//	private View view;
//	// 点击三级菜单后判断进入哪个功能
//	private int go_menu_id;
//	private View menuView = null;
//	private View noCrcdView = null;
//	private Button glButton = null;
//	private boolean isShowView = false;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		// 为界面标题赋值
//		setTitle(this.getString(R.string.mycrcd_setup));
//		setLeftSelectedPosition(1);
//		initView();
//		btn_right.setVisibility(View.GONE);
//		back = (Button) findViewById(R.id.ib_back);
//		back.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				ActivityTaskManager.getInstance().closeAllActivityExceptOne("MainActivity");
//				Intent intent = new Intent(MyCardSetupMenuActivity.this, MainActivity.class);
//				startActivity(intent);
//				finish();
//			}
//		});
//		isShowView = false;
//		iscomformFootFastOrOther();
//
//	}
//
//	private void initView() {
//		// 添加布局
//		view = addView(R.layout.mycard_setup_menu);
//		menuView = findViewById(R.id.menu_layout);
//		noCrcdView = findViewById(R.id.no_crcd_layout);
//		glButton = (Button) findViewById(R.id.btn_description);
//	}
//
//	/** 用于是否从快捷键 */
//	private void iscomformFootFastOrOther() {
//		if (getIntent().getBooleanExtra(ConstantGloble.COMEFROMFOOTFAST, false)) {
//			// 快捷键启动
//			// 查询信用卡
//			BaseHttpEngine.showProgressDialogCanGoBack();
//			requestCommonQueryAllChinaBankAccount();
//		} else {
//			// 初始化界面
//			hasDate();
//		}
//	}
//
//	/** 有信用卡数据 */
//	private void hasDate() {
//		setTitle(this.getString(R.string.mycrcd_setup));
//		menuView.setVisibility(View.VISIBLE);
//		noCrcdView.setVisibility(View.GONE);
//		init();
//	}
//
//	@Override
//	public void requestCommonQueryAllChinaBankAccountCallBack(Object resultObj) {
//		super.requestCommonQueryAllChinaBankAccountCallBack(resultObj);
//		BaseHttpEngine.dissMissProgressDialog();
//		List<Map<String, Object>> returnList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap()
//				.get(ConstantGloble.CRCD_CRCDACCOUNTRETURNLIST);
//		if (isShowView) {
//			// 请求信用卡列表
//			removeAllViews(view);
//			initView();
//		}
//		if (returnList != null&&returnList.size()> 0) {
//			hasDate();
//		} else {
//			setTitle(this.getString(R.string.mycrcd1));
//			menuView.setVisibility(View.GONE);
//			noCrcdView.setVisibility(View.VISIBLE);
//			setLeftButtonPopupGone();
//			glButton.setOnClickListener(glOnClick);
//			
//		}
//	}
//
//	/** 关联信用卡 */
//	private OnClickListener glOnClick = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			startActivityForResult((new Intent(MyCardSetupMenuActivity.this, AccInputRelevanceAccountActivity.class)),
//					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
//		}
//	};
//
//	/** 初始化界面 */
//	private void init() {
//		// 消费服务设置
//		LinearLayout crcd_consume_setup_ll = (LinearLayout) view.findViewById(R.id.crcd_consume_setup_ll);
//		crcd_consume_setup_ll.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// 消费服务设置
//				go_menu_id = 1;
//				Intent it = new Intent(MyCardSetupMenuActivity.this, CrcdServiceSetupListActivity.class);
//				startActivity(it);
//			}
//		});
//
//		LinearLayout crcd_billservice_setup_ll = (LinearLayout) view.findViewById(R.id.crcd_billservice_setup_ll);
//		crcd_billservice_setup_ll.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// 对账单服务
//				go_menu_id = 2;
//				Intent it = new Intent(MyCardSetupMenuActivity.this, CrcdPsnQueryCheckList.class);
//				startActivity(it);
//			}
//		});
//
//		LinearLayout mycrcd_xiao_fei_ll = (LinearLayout) view.findViewById(R.id.mycrcd_xiao_fei_ll);
//		mycrcd_xiao_fei_ll.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// 还款方式设定
//				go_menu_id = 3;
//				Intent it = new Intent(MyCardSetupMenuActivity.this, MyCrcdSetupListActivity.class);
//				startActivity(it);
//			}
//		});
//
//		LinearLayout mycrcd_fushu_ll = (LinearLayout) view.findViewById(R.id.mycrcd_fushu_ll);
//		mycrcd_fushu_ll.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// 附属卡服务定制
//				go_menu_id = 4;
//				Intent it = new Intent(MyCardSetupMenuActivity.this, MySupplymentListActivity.class);
//				startActivity(it);
//			}
//		});
//
//		LinearLayout mycrcd_save_ll = (LinearLayout) view.findViewById(R.id.mycrcd_save_ll);
//		mycrcd_save_ll.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// 信用卡激活
//				go_menu_id = 5;
//				Intent it = new Intent(MyCardSetupMenuActivity.this, CrcdActiveInfo.class);
//				startActivity(it);
//			}
//		});
//
//		LinearLayout mycrcd_guashi_ll = (LinearLayout) view.findViewById(R.id.mycrcd_guashi_ll);
//		mycrcd_guashi_ll.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// 信用卡挂失
//				go_menu_id = 6;
//				Intent it = new Intent(MyCardSetupMenuActivity.this, CrcdGuashiListActivity.class);
//				startActivity(it);
//			}
//		});
//		LinearLayout global_rmb_acc = (LinearLayout) view.findViewById(R.id.mycrcd_global_code);
//		global_rmb_acc.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// 全球交易人民币记账功能设置
//				go_menu_id = 7;
//				BaseHttpEngine.showProgressDialog();
//				requestPsnCommonQueryAllChinaBankAccount();
//
//			}
//		});
//	}
//
//	/** 获取信用卡列表 103、104类型 */
//	private void requestPsnCommonQueryAllChinaBankAccount() {
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(Crcd.QRY_CRCD_LIST_API);
//		Map<String, Object> params = new HashMap<String, Object>();
//		String[] s = { ZHONGYIN, GREATWALL };
//		params.put(Crcd.CRCD_ACCOUNTTYPE_REQ, s);
//		biiRequestBody.setParams(params);
//		biiRequestBody.setConversationId(null);
//		HttpManager.requestBii(biiRequestBody, this, "requestPsnCommonQueryAllChinaBankAccountCallBack");
//	}
//
//	/** 获取信用卡列表----回调 */
//	public void requestPsnCommonQueryAllChinaBankAccountCallBack(Object resultObj) {
//		BiiResponse biiResponse = (BiiResponse) resultObj;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		// 得到response
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		// 得到result
//		List<Map<String, String>> result = (List<Map<String, String>>) biiResponseBody.getResult();
//		BaseHttpEngine.dissMissProgressDialog();
//		if (result != null && result.size() > 0) {
//			List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
//			for (int i = 0; i < result.size(); i++) {
//				if (ZHONGYIN.equals(result.get(i).get(Crcd.CRCD_ACCOUNTTYPE_RES))
//						|| GREATWALL.equals(result.get(i).get(Crcd.CRCD_ACCOUNTTYPE_RES))) {
//					resultList.add(result.get(i));
//				}
//
//			}
//			if (resultList != null && resultList.size() > 0) {
//				BaseHttpEngine.dissMissProgressDialog();
//				Intent it = new Intent(MyCardSetupMenuActivity.this, CrcdGlobalRMBAccActivity.class);
//				BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.CRCD_RMG_ACCLIST, resultList);
//				startActivity(it);
//			} else {
//				BaseHttpEngine.dissMissProgressDialog();
//				BaseDroidApp.getInstanse().showInfoMessageDialog(
//						getResources().getString(R.string.acc_transferquery_null));
//				return;
//			}
//
//		} else {
//			BaseHttpEngine.dissMissProgressDialog();
//			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
//			return;
//		}
//
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (requestCode == ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE && resultCode == RESULT_OK) {
//			BaseHttpEngine.showProgressDialogCanGoBack();
//			isShowView = true;
//			setLeftSelectedPosition(1);
//			initLeftSideList(BaseDroidApp.getInstanse().getCurrentAct(), LocalData.myCrcdListData);
//			requestCommonQueryAllChinaBankAccount();
//		}
//	}
}
