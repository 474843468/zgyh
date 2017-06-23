package com.chinamworld.bocmbci.biz.crcd.mycrcd.transquery;

import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;

/**
 * 交易查询菜单选择界面
 * 
 * @author huangyuchao
 * 注释此类 因 dex方法超出限制  sunh 
 */
public class MyCardTransMenuActivity extends CrcdBaseActivity {
//
//	/** 电子现金账户列表页 */
//	private View view;
//	// 点击三级菜单后判断进入哪个功能
//	private int go_menu_id;
	public static String goType;
//	private View menuView = null;
//	private View noCrcdView = null;
//	private Button glButton = null;
//	private boolean isShowView = false;
//	/** 1-账单分期，2-消费分期，3-分期历史查询 */
//	private int searchTag = 1;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//
//		// 为界面标题赋值
//		setTitle(this.getString(R.string.prms_title_query));
//		initView();
//		btn_right.setVisibility(View.GONE);
//
//		back = (Button) findViewById(R.id.ib_back);
//		back.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				ActivityTaskManager.getInstance().closeAllActivityExceptOne("MainActivity");
//				Intent intent = new Intent(MyCardTransMenuActivity.this, MainActivity.class);
//				startActivity(intent);
//				finish();
//			}
//		});
//		isShowView = false;
//		setLeftSelectedPosition(2);
//		iscomformFootFastOrOther();
//	}
//
//	private void initView() {
//		// 添加布局
//		view = addView(R.layout.mycard_trans_menu);
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
//			hasDate();
//		}
//	}
//
//	/** 有信用卡数据 */
//	private void hasDate() {
//		setTitle(this.getString(R.string.prms_title_query));
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
//		if (StringUtil.isNullOrEmpty(returnList)) {
//			setTitle(this.getString(R.string.mycrcd1));
//			menuView.setVisibility(View.GONE);
//			noCrcdView.setVisibility(View.VISIBLE);
//			setLeftButtonPopupGone();
//			glButton.setOnClickListener(glOnClick);
//		} else {
//			hasDate();
//		}
//	}
//
//	/** 关联信用卡 */
//	private OnClickListener glOnClick = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			startActivityForResult((new Intent(MyCardTransMenuActivity.this, AccInputRelevanceAccountActivity.class)),
//					ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
//		}
//	};
//
//	/** 初始化界面 */
//	private void init() {
//		LinearLayout crcd_consume_setup_ll = (LinearLayout) view.findViewById(R.id.crcd_consume_setup_ll);
//		crcd_consume_setup_ll.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// 未出账单查询
//				go_menu_id = 1;
//				Intent it = new Intent(MyCardTransMenuActivity.this, CrcdNoTransQueryListActivity.class);
//				startActivity(it);
//			}
//		});
//
//		LinearLayout crcd_billservice_setup_ll = (LinearLayout) view.findViewById(R.id.crcd_billservice_setup_ll);
//		crcd_billservice_setup_ll.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// 已出账单查询
//				go_menu_id = 2;
//				Intent it = new Intent(MyCardTransMenuActivity.this, CrcdHasTransQueryListActivity.class);
//				startActivity(it);
//			}
//		});
//
//		LinearLayout mycrcd_zhangdan_ll = (LinearLayout) view.findViewById(R.id.mycrcd_zhangdan_ll);
//		mycrcd_zhangdan_ll.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// 账单分期
//				go_menu_id = 3;
//				goType = "fromZhangDan";
//				searchTag = 1;
//				BaseHttpEngine.showProgressDialog();
//				requestPsnCommonQueryAllChinaBankAccount();
//
//			}
//		});
//
//		LinearLayout mycrcd_xiao_fei_ll = (LinearLayout) view.findViewById(R.id.mycrcd_xiao_fei_ll);
//		mycrcd_xiao_fei_ll.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// 消费账单查询
//				go_menu_id = 4;
//				goType = "fromXiaoFei";
//				searchTag = 2;
//				BaseHttpEngine.showProgressDialog();
//				requestPsnCommonQueryAllChinaBankAccount();
//			}
//		});
//
//		LinearLayout mycrcd_fushu_ll = (LinearLayout) view.findViewById(R.id.mycrcd_fushu_ll);
//		mycrcd_fushu_ll.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// 附属卡交易明细
//				go_menu_id = 5;
//				Intent it = new Intent(MyCardTransMenuActivity.this, CrcdfuShuQueryListActivity.class);
//				startActivity(it);
//			}
//		});
//
//		LinearLayout mycrcd_cut_money_setup_ll = (LinearLayout) view.findViewById(R.id.mycrcd_cut_money_setup_ll);
//		mycrcd_cut_money_setup_ll.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// 分期历史查询及结清
//				go_menu_id = 6;
//				searchTag = 3;
//				BaseHttpEngine.showProgressDialog();
//				requestPsnCommonQueryAllChinaBankAccount();
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
//		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
//		if (result != null && result.size() > 0) {
//			for (int i = 0; i < result.size(); i++) {
//				if (ZHONGYIN.equals(result.get(i).get(Crcd.CRCD_ACCOUNTTYPE_RES))
//						|| GREATWALL.equals(result.get(i).get(Crcd.CRCD_ACCOUNTTYPE_RES))) {
//					resultList.add(result.get(i));
//				}
//
//			}
//			gotoActivity(resultList);
//		} else {
//			BaseHttpEngine.dissMissProgressDialog();
//			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
//			return;
//		}
//
//	}
//
//	/** 跳转页面 */
//	private void gotoActivity(List<Map<String, String>> resultList) {
//		if (resultList != null && resultList.size() > 0) {
//			BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.CRCD_SEARCH_ACCLIST, resultList);
//			if (searchTag == 1) {
//				// 账单分期
//				Intent it = new Intent(MyCardTransMenuActivity.this, CrcdHasQueryListActivity.class);
//				startActivity(it);
//			} else if (searchTag == 2) {
//				// 消费分期
//				Intent it = new Intent(MyCardTransMenuActivity.this, CrcdXiaofeiQueryListActivity.class);
//				startActivity(it);
//			} else if (searchTag == 3) {
//				// 分期历史查询
//				Intent it = new Intent(MyCardTransMenuActivity.this, CrcdDividedHistoryQueryList.class);
//				startActivity(it);
//			}
//			return;
//		} else {
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
//			setLeftSelectedPosition(2);
//			initLeftSideList(BaseDroidApp.getInstanse().getCurrentAct(), LocalData.myCrcdListData);
//			requestCommonQueryAllChinaBankAccount();
//		}
//	}
}
