package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
//import com.chinamworld.bocmbci.biz.crcd.adapter.CrcdGlobalRMBAccAdapter;

/**
 * 信用卡设定-全球交易人民币记账功能设置--选择信用卡页面
 * 
 * @author 宁焰红
 * 注释此类 因 dex方法超出限制  sunh 
 */
public class CrcdGlobalRMBAccActivity extends CrcdBaseActivity {/*
	private static String TAG = "CrcdGlobalRMBAccActivity";
	*//** 页面布局 *//*
	private View rmbView = null;
	*//** 信用卡istView *//*
	private ListView dateListView = null;
	*//** 下一步 *//*
	private Button nextButton = null;
	*//** 返回按钮 *//*
	private Button backButton = null;
	RelativeLayout cardLayout;
	LinearLayout nocardLayout;

	private CrcdGlobalRMBAccAdapter adapter = null;
	*//** 信用卡列表数据 *//*
	private List<Map<String, String>> resultList = null;
	*//** 网银账户标识 *//*
	private String accountId = null;
	*//** 账号 *//*
	private String accountNumber = null;
	*//** 币种结果 *//*
	private List<Map<String, Object>> currencyList = null;
	*//** 信用卡账户 *//*
	private List<String> accNumberList = null;
	*//** 具有双币种的信用卡 *//*
	private List<Map<String, String>> currencyAccNumberList = null;
	*//** 请求的次数 *//*
	private int n = -1;
	*//** 信用卡账户集合的长度 *//*
	private int accLen = 0;
	private View footView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.mycrcd_creditcard_global_code));
		resultList = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CRCD_RMG_ACCLIST);
		backButton = (Button) findViewById(R.id.ib_back);
		// 返回按钮
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		if (resultList != null && resultList.size() > 0) {
			init();
			initOnClick();
			return;
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}

	}

	private void init() {
		rmbView = LayoutInflater.from(this).inflate(R.layout.crcd_mycard_setup_list, null);
		tabcontent.addView(rmbView);
		TextView titleText = (TextView) rmbView.findViewById(R.id.tv_service_title);
		titleText.setText(R.string.mycrcd_creditcard_choise_card);
		dateListView = (ListView) rmbView.findViewById(R.id.crcd_mycrcdlist);
		footView = LayoutInflater.from(this).inflate(R.layout.crcd_rmb_footview, null);
		dateListView.addFooterView(footView);
		nextButton = (Button) rmbView.findViewById(R.id.sureButton);
		currencyList = new ArrayList<Map<String, Object>>();
		accNumberList = new ArrayList<String>();
		currencyAccNumberList = new ArrayList<Map<String, String>>();
		// 取出卡片选中效果
		CrcdGlobalRMBAccAdapter.selectedPosition = -1;
		cardLayout = (RelativeLayout) findViewById(R.id.cardLayout);
		nocardLayout = (LinearLayout) findViewById(R.id.nocardLayout);
		nocardLayout.setVisibility(View.GONE);
		cardLayout.setVisibility(View.VISIBLE);
		adapter = new CrcdGlobalRMBAccAdapter(this, resultList);
		dateListView.setAdapter(adapter);
	}

	private void initOnClick() {
		dateListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				LogGloble.d(TAG + "===== position", String.valueOf(position));
				LogGloble.d(TAG + "===== resultList", String.valueOf(resultList.size()));
				if (position >= resultList.size()) {
					return;
				}
				Map<String, String> map = resultList.get(position);
				CrcdGlobalRMBAccAdapter.selectedPosition = position;
				accountId = map.get(Crcd.CRCD_ACCOUNTID_RES);
				accountNumber = map.get(Crcd.CRCD_ACCOUNTNUMBER_RES);
				// 更新数据显示
				adapter.notifyDataSetChanged();
			}

		});
		// 下一步
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 没有选择信用卡
				if (CrcdGlobalRMBAccAdapter.selectedPosition < 0) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							CrcdGlobalRMBAccActivity.this.getString(R.string.crcd_notselectacard_error));
					return;
				}
				// 查询信用卡币种
				BaseHttpEngine.showProgressDialog();
				requestPsnCrcdCurrencyQuery(accountNumber);
			}
		});
	}

	*//**
	 * 查询信用卡币种
	 * 
	 * @param accountNumber
	 *//*
	private void requestPsnCrcdCurrencyQuery(String accountNumber) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDCURRENCYQUERY_API);
		Map<String, String> params = new HashMap<String, String>();
		params.put(Crcd.CRCD_ACCOUNTNUMBER_REQ, accountNumber);
		biiRequestBody.setParams(params);
		biiRequestBody.setConversationId(null);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnCrcdCurrencyQueryCallBack");
	}

	public void requestPsnCrcdCurrencyQueryCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.crcd_emg_rmb_golable));
			return;
		}
		Map<String, Object> currency1 = (Map<String, Object>) result.get(Crcd.CRCD_CURRENCY1_REQ);
		Map<String, Object> currency2 = (Map<String, Object>) result.get(Crcd.CRCD_CURRENCY2_REQ);
		if (StringUtil.isNullOrEmpty(currency1) || StringUtil.isNullOrEmpty(currency2)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.crcd_emg_rmb_golable));
			return;
		}
		String code1 = (String) currency1.get(Crcd.CRCD_CODE);
		String code2 = (String) currency2.get(Crcd.CRCD_CODE);
		if (StringUtil.isNullOrEmpty(code1) || StringUtil.isNullOrEmpty(code2)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.crcd_emg_rmb_golable));
			return;
		}
		// 跳转到下一页面
		Intent intent = new Intent(CrcdGlobalRMBAccActivity.this, CrcdGlobalRMBAccConfirmActivity.class);
		intent.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);
		startActivityForResult(intent, ConstantGloble.CRCD_ACTIVITY_CODE);// 101

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			// 失去选中效果
			if (requestCode == ConstantGloble.CRCD_ACTIVITY_CODE) {
				// 人民币记账功能
				CrcdGlobalRMBAccAdapter.selectedPosition = -1;
				if (adapter != null) {
					adapter.changeDate(resultList);
				} else {
					adapter = new CrcdGlobalRMBAccAdapter(this, resultList);
					dateListView.setAdapter(adapter);
				}
			}
			break;
		case RESULT_CANCELED:
			// 保留选中效果
			break;
		default:
			break;
		}

	}

*/}
