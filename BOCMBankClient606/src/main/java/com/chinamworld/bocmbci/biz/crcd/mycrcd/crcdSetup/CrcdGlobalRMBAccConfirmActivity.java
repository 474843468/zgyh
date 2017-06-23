package com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup;

import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;

/***
 * 信用卡设定-全球交易人民币记账功能设置--信用卡页面
 * 
 * @author 宁焰红
 * 
 */
public class CrcdGlobalRMBAccConfirmActivity extends CrcdBaseActivity {/*
	private static final String TAG = "CrcdGlobalRMBAccConfirmActivity";
	*//** 页面布局 *//*
	private View rmbView = null;
	*//** 开通或关闭按钮 *//*
	private Button nextButton = null;
	*//** 返回按钮 *//*
	private Button backButton = null;
	*//** 卡号 *//*
	private TextView numberText = null;
	*//** 产品名称 *//*
	private TextView nameText = null;
	*//** 别名 *//*
	private TextView nickText = null;
	*//** 功能 *//*
	private TextView functionText = null;
	private TextView textText = null;
	*//** 产品名称 *//*
	private String description = null;
	*//** 账户别名 *//*
	private String alias = null;
	*//** 账号 *//*
	private String displayNum = null;
	*//** 网银账户标识 *//*
	private String accountId = null;
	*//** 开通标志 *//*
	private String openFlag = null;
	*//** 开通标志:是、否 *//*
	private String openFlagName = null;
	*//** 用于区分是第一次请求还是第二次请求 *//*
	private int tag = 1;
	private String token = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		setTitle(getResources().getString(R.string.mycrcd_creditcard_global_code));
		initDate();
		if (StringUtil.isNull(accountId)) {
			return;
		}
		BaseHttpEngine.showProgressDialogCanGoBack();
		// 全球交易人民币记账功能查询
		requestPsnCrcdChargeOnRMBAccountQuery(accountId);

	}

	private void init() {
		rmbView = LayoutInflater.from(this).inflate(R.layout.crcd_setup_rmb_confirm, null);
		tabcontent.addView(rmbView);
		nextButton = (Button) findViewById(R.id.sureButton);
		backButton = (Button) findViewById(R.id.ib_back);
		numberText = (TextView) findViewById(R.id.finc_accNumber);
		nameText = (TextView) findViewById(R.id.finc_accId);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, numberText);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, nameText);
		nickText = (TextView) findViewById(R.id.tv_cardNumber);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, nickText);
		functionText = (TextView) findViewById(R.id.finc_fincName);
		textText = (TextView) findViewById(R.id.function_text);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this, textText);
	}

	*//** 上一页面传递的数据 得到accountId *//*
	private void initDate() {
		Intent intent = getIntent();
		if (intent == null) {
			return;
		}
		accountId = intent.getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
	}

	*//**
	 * 全球交易人民币记账功能查询
	 * 
	 * @param accountId
	 *            :网银账户标志
	 *//*
	private void requestPsnCrcdChargeOnRMBAccountQuery(String accountId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_RMBACCOUNTQUERY_API);
		Map<String, String> params = new HashMap<String, String>();
		params.put(Crcd.CRCD_ACCOUNTID_REQ, accountId);
		biiRequestBody.setParams(params);
		biiRequestBody.setConversationId(null);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnCommonQueryAllChinaBankAccountCallBack");
	}

	public void requestPsnCommonQueryAllChinaBankAccountCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		Map<String, String> result = (Map<String, String>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			return;
		}
		description = result.get(Crcd.CRCD_DESCRIPTION_RES);
		displayNum = result.get(Crcd.CRCD_DISPLAYNUM_RES);
		if (StringUtil.isNull(displayNum)) {
			return;
		} else {
			displayNum = StringUtil.getForSixForString(displayNum);
		}
		alias = result.get(Crcd.CRCD_ALIAS_RES);
		openFlag = result.get(Crcd.CRCD_OPENFLAG_RES);
		if (StringUtil.isNull(openFlag)) {
			return;
		}
		if (openFlag.equals(ConstantGloble.CRCD_TRUE_KEY)) {
			openFlagName = ConstantGloble.CRCD_YES_KEY;
		} else {
			openFlagName = ConstantGloble.CRCD_NO_KEY;
		}
		init();
		setViewValue();
		initClick();
	}

	*//** 为控件赋值 *//*
	private void setViewValue() {
		numberText.setText(displayNum);
		nameText.setText(description);
		nickText.setText(alias);
		functionText.setText(openFlagName);
		if (openFlag.equals(ConstantGloble.CRCD_TRUE_KEY)) {
			// 开通
			nextButton.setText(getResources().getString(R.string.crcd_setUp_confirm_close));
		} else {
			// 未开通
			nextButton.setText(getResources().getString(R.string.crcd_setUp_confirm_opene));
		}
	}

	*//** 初始化监听事件 *//*
	private void initClick() {
		// 返回按钮
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// tag = 2;
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();
			}
		});
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		// 获取tockenId
		requestPSNGetTokenId(commConversationId);
	}

	*//** tokenId---回调 *//*
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		token = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
		if (StringUtil.isNull(token)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		String openTag = null;
		if (openFlag.equals(ConstantGloble.CRCD_FALSE_KEY)) {
			// 开通上送：ADTE
			openTag = ConstantGloble.CRCD_OPEN_KEY;
		} else {
			// 关闭上送：MINP
			openTag = ConstantGloble.CRCD_CLOSE_KEY;
		}
		requestPsnCrcdChargeOnRMBAccountSet(accountId, openTag, token);
	}

	*//**
	 * 全球交易人民币记账功能设置（开通/关闭）
	 * 
	 * @param accountId
	 *            ：账户标志
	 * @param openFlag
	 *            ：开通标志
	 * @param token
	 *//*
	private void requestPsnCrcdChargeOnRMBAccountSet(String accountId, String openFlag, String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_RMBACCOUNTSET_API);
		Map<String, String> params = new HashMap<String, String>();
		params.put(Crcd.CRCD_ACCOUNTIDS_REQ, accountId);
		params.put(Crcd.CRCD_OPENFLAG_REQ, openFlag);
		params.put(Crcd.CRCD_TOCKEN_REQ, token);
		biiRequestBody.setParams(params);
		biiRequestBody.setConversationId(commConversationId);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnCrcdChargeOnRMBAccountSetCallBack");
	}

	*//** 全球交易人民币记账功能设置（开通/关闭）---回调 *//*
	public void requestPsnCrcdChargeOnRMBAccountSetCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		Object result = biiResponseBody.getResult();
		String card = getResources().getString(R.string.crcd_setUp_confirm_card);
		String rmb = getResources().getString(R.string.crcd_setUp_confirm_rmb);
		String open = getResources().getString(R.string.crcd_setUp_confirm_rmb_opene);
		String close = getResources().getString(R.string.crcd_setUp_confirm_rmb_close);
		String openText = card + displayNum + rmb + open;
		String closeText = card + displayNum + rmb + close;
		// 返回true
		if (openFlag.equals(ConstantGloble.CRCD_TRUE_KEY)) {
			// 开通记账功能,现在需关闭记账功能
			CustomDialog.toastInCenter(this, closeText);
		} else {
			// 关闭记账功能,现在需开通记账功能
			CustomDialog.toastInCenter(this, openText);
		}
		setResult(RESULT_OK);
		finish();
	}
*/}
