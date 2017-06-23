package com.chinamworld.bocmbci.biz.bocinvt.productlist;

import com.chinamworld.bocmbci.biz.bocinvt.BociBaseActivity;

/**
 * 功能外置产品详情页面
 * 
 * @author sunh
 * 注释此类 因 dex方法超出限制  sunh 
 */
public class ProductDetailOutlayActivity extends BociBaseActivity 
//  implements OnClickListener 
  {
//	/** 登陆 */
//	public static final int ACTIVITY_BUY_LOGIN = 12;
//	/** 选择账号 */
//	public static final int ACTIVITY_BUY_CHOOSEACC = 13;
//	/** 查询信息页 */
//	private View view;
//	/** 产品详情列表 */
//	private Map<String, Object> detailMap;
//	/** 产品代码 */
//	private TextView tv_prodCode_detail;
//	/** 产品名称 */
//	private TextView tv_prodName_detail;
//	/** 产品币种 */
//	private TextView tv_curCode_detail;
//	/** 购买价格 */
//	private TextView tv_buyPrice_detail;
//	/** 产品期限 */
//	private TextView tv_prodTimeLimit_detail;
//	/** 适用对象 */
//	private TextView tv_applyObj_detail;
//	/** 产品销售状态 */
//	// private TextView tv_status_detail;
//	/** 风险等级 */
//	// private TextView tv_prodRisklvl_detail;
//	/** 周期属性 */
//	private TextView tv_periodical_detail;
//	/** 预计年收益率 */
//	private TextView tv_yearlyRR_detail;
//	/** 产品种类 */
//	// private TextView tv_brandName_detail;
//	/** 认购起点金额 */
//	private TextView tv_buyStartingAmount_detail;
//	/** 追加申购起点金额 */
//	private TextView tv_appendStartingAmount_detail;
//	/** 产品销售期—开始 */
//	private TextView tv_sellingDate_start;
//	/** 产品销售期—结束 */
//	private TextView tv_sellingDate_end;
//	/** 产品成立日 */
//	private TextView tv_prodBegin_detail;
//	/** 产品到期日 */
//	private TextView tv_prodEnd_detail;
//	/** 交易渠道 **/
//	// private TextView tvTransferChannel;
//	/** 非交易时间挂单 **/
//	// private TextView tvOutTimeOrder;
//	/** 本金到期日 **/
//	// private TextView tvPaymentDate;
//	/** 付息规则 **/
//	// private TextView tvCouponPay;
//	/** 赎回起点份额 **/
//	// private TextView tvLowlimitAmount;
//	/** 最低持有份额 **/
//	// private TextView tvLimitholdBalance;
//	/** 赎回开放规则 **/
//	// private TextView tvSellType;
//	/** 赎回本金到账规则 **/
//	// private TextView tvSellPaymentType;
//	/** 赎回收益到账规则 **/
//	// private TextView tvProFit;
//	// 按钮
//	/** 购买 */
//	private Button btn_buy_buydetail;
//
//	/** 理财产品说明书 */
//	private Button btn_description_buydetail;
//	private Map<String, Object> chooseMap;
//	/** 追加认申购起点金额 */
//	private TextView tv_appending;
//	private String accountId;
//	private String status;
//	private boolean isRed;
//	boolean btn_buy_buydetail_outlay;
//	boolean isbuy;// 购买 签约
//	// boolean iscontract;// 投资协议申请
//	// 购买弹出框
//	/** 是否开通投资理财 */
//	private boolean isOpen;
//	/** 是否有风险评估经验 */
//	private boolean isevaluatedBefore;
//	/** 登记账户信息 */
//	private List<Map<String, Object>> investBindingInfo;
//	/** 任务弹出框视图 */
//	private RelativeLayout dialogView;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setTitle(this.getString(R.string.bocinvt_detail_title));
//		view = addView(R.layout.bocinvt_queryproduct_detail_outlay);
//
//		accountId = getIntent().getStringExtra(Comm.ACCOUNT_ID); 
//		if (BaseDroidApp.getInstanse().isLogin()) {
//			setText(this.getString(R.string.acc_rightbtn_go_main));
//			setRightBtnClick(rightBtnClick);
//		}
//		init();
//		setBackBtnClick(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (BaseDroidApp.getInstanse().isLogin()) {
//					BociDataCenter.getInstance().clearBociData();
//					Intent intent = new Intent();
//					intent.setClass(ProductDetailOutlayActivity.this,
//							QueryProductActivity.class);
//					startActivity(intent);
//					ProductDetailOutlayActivity.this.finish();
//
//				} else {
//					setResult(RESULT_CANCELED);
//					finish();
//				}
//
//			}
//		});
//	}
//
//	@SuppressWarnings("unchecked")
//	private void init() {
//		chooseMap = BociDataCenter.getInstance().getChoosemap();
//		detailMap = (Map<String, Object>) BaseDroidApp.getInstanse()
//				.getBizDataMap().get(ConstantGloble.BOCINVT_PRODUCTDETAIL_LIST);
//		BociDataCenter.getInstance().setDetailMap(detailMap);
//		tv_prodCode_detail = (TextView) view
//				.findViewById(R.id.tv_prodCode_detail);
//		tv_prodName_detail = (TextView) view
//				.findViewById(R.id.tv_prodName_detail);
//		tv_curCode_detail = (TextView) view
//				.findViewById(R.id.tv_curCode_detail);
//		tv_buyPrice_detail = (TextView) view
//				.findViewById(R.id.tv_buyPrice_detail);
//		tv_prodTimeLimit_detail = (TextView) view
//				.findViewById(R.id.tv_prodTimeLimit_detail);
//		tv_applyObj_detail = (TextView) view
//				.findViewById(R.id.tv_applyObj_detail);
//
//		tv_periodical_detail = (TextView) view
//				.findViewById(R.id.tv_periodical_detail);
//		tv_yearlyRR_detail = (TextView) view
//				.findViewById(R.id.tv_yearlyRR_detail);
//		tv_buyStartingAmount_detail = (TextView) view
//				.findViewById(R.id.tv_buyStartingAmount_detail);
//		tv_appendStartingAmount_detail = (TextView) view
//				.findViewById(R.id.tv_appendStartingAmount_detail);
//		tv_sellingDate_start = (TextView) view
//				.findViewById(R.id.tv_sellingDate_start);
//		tv_sellingDate_end = (TextView) view
//				.findViewById(R.id.tv_sellingDate_end);
//		tv_prodBegin_detail = (TextView) view
//				.findViewById(R.id.tv_prodBegin_detail);
//		tv_prodEnd_detail = (TextView) view
//				.findViewById(R.id.tv_prodEnd_detail);
//		tv_appending = (TextView) view.findViewById(R.id.appendStrating);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
//				tv_appending);
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
//				(TextView) findViewById(R.id.outtimeorder_title));
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
//				(TextView) findViewById(R.id.paymentdate_title));
//		// 赋值操作
//		tv_prodCode_detail.setText((String) detailMap
//				.get(BocInvt.BOCI_DETAILPRODCODE_RES));
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
//				tv_prodCode_detail);
//		tv_prodName_detail.setText((String) detailMap
//				.get(BocInvt.BOCI_DETAILPRODNAME_RES));
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
//				tv_prodName_detail);
//		String curcode = (String) detailMap.get(BocInvt.BOCI_DETAILCURCODE_RES);
//		/** 产品币种 */
//		tv_curCode_detail.setText(LocalData.Currency.get(curcode));
//		String buyPrice = (String) detailMap
//				.get(BocInvt.BOCI_DETAILBUYPRICE_RES);
//		tv_buyPrice_detail.setText(StringUtil.parseStringCodePattern(curcode,
//				buyPrice, 2));
//		String timeLimit = (String) detailMap
//				.get(BocInvt.BOCI_DETAILPRODTIMELIMIT_RES);
//		tv_prodTimeLimit_detail.setText(timeLimit + DAY);
//		tv_applyObj_detail.setText(prodTimeLimitMap.get((String) detailMap
//				.get(BocInvt.BOCI_DETAILAPPLYOBJ_RES)));
//		status = (String) detailMap.get(BocInvt.BOCI_DETAILSTATUS_RES);
//
//		String buyStartingAmount = (String) detailMap.get(BocInvt.SUBAMOUNT);
//		tv_buyStartingAmount_detail.setText(StringUtil.parseStringCodePattern(
//				curcode, buyStartingAmount, 2));
//		findViewById(R.id.ll_btn).setVisibility(View.VISIBLE);
//		String appendStartingAmount = (String) detailMap.get(BocInvt.ADDAMOUNT);
//		tv_appendStartingAmount_detail.setText(StringUtil
//				.parseStringCodePattern(curcode, appendStartingAmount, 2));
//		tv_sellingDate_start.setText((String) detailMap
//				.get(BocInvt.BOCI_DETAILSELLINGSTARTINGDATE_RES)
//				+ ConstantGloble.BOCINVT_DATE_ADD);
//		tv_sellingDate_end.setText((String) detailMap
//				.get(BocInvt.BOCI_DETAILSELLINGENDINGDATE_RES));
//		tv_prodBegin_detail.setText((String) detailMap
//				.get(BocInvt.BOCI_DETAILPRODBEGIN_RES));
//		tv_prodEnd_detail.setText((String) detailMap
//				.get(BocInvt.BOCI_DETAILPRODEND_RES));
//		btn_buy_buydetail = (Button) view
//				.findViewById(R.id.btn_buy_buydetail_outlay);
//		btn_description_buydetail = (Button) view
//				.findViewById(R.id.btn_description_buydetail_outlay);
//		((TextView) view.findViewById(R.id.prodRiskType))
//				.setText(BociDataCenter.prodRiskType.get(detailMap
//						.get(BocInvt.PRODRISKTYPE)));
//		((TextView) view.findViewById(R.id.baseAmount))
//				.setText((String) detailMap
//						.get(BocInvt.BOCINVT_SIGNRESULT_BASEAMOUNT_REQ));
//
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
//				(TextView) view.findViewById(R.id.yearlyRR_detail));
//
//		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
//				(TextView) view.findViewById(R.id.buyStartingAmount_detail));
//		String periodical = (String) detailMap
//				.get(BocInvt.BOCI_DETAILPERIODICAL_RES);
//		// tv_periodical_detail.setText(LocalData.periodicalList.get(Integer
//		// .valueOf(periodical)));
//
//		if (!isBuyCheck()) {
//			// 为true是可签约
//
//			btn_buy_buydetail.setText(getResources().getString(
//					R.string.product_qianyue));
//			tv_periodical_detail.setText(LocalData.periodicalList.get(1));
//			btn_buy_buydetail.setOnClickListener(this);
//		} else {
//			// 非周期性产品可购买
//			tv_periodical_detail.setText(LocalData.periodicalList.get(0));
//			// TODO 401 判断是否允许定投与自动投资
//			if (!StringUtil.isNullOrEmpty(chooseMap)
//					&& LocalData.orderTimeMap.get(0).equals(
//							chooseMap.get(BocInvt.BOCI_AUTOPERMIT_RES))) {
//				btn_buy_buydetail.setText(getResources().getString(
//						R.string.product_more));
//				btn_buy_buydetail.setOnClickListener(btnMoreNullBocinvtClick);
//				String[] service = {
//						getResources().getString(R.string.product_buy),
//						getResources().getString(R.string.product_contract) };
//				PopupWindowUtils.getInstance().setshowMoreChooseUpListener(
//						BaseDroidApp.getInstanse().getCurrentAct(),
//						btn_buy_buydetail, service, btnMoreNullBocinvtClick);
//			} else {
//				btn_buy_buydetail.setText(getResources().getString(
//						R.string.product_buy));
//				btn_buy_buydetail.setOnClickListener(this);
//			}
//		}
//
//		btn_description_buydetail.setOnClickListener(this);
//
//		setProgress();
//
//	}
//
//	private boolean isBuyCheck() {
//		if (!StringUtil.isNullOrEmpty(chooseMap)) {
//			if (chooseMap.get(BocInvt.BOCI_DETAILPERIODICAL_RES)
//					.equals("false")) {
//				return true;
//			}
//		}
//
//		return false;
//	}
//
//	/** 预计年收益率 */
//	private void setProgress() {
//		String progressionflag = (String) detailMap
//				.get(BocInvt.PROGRESSIONFLAG);
//		if (StringUtil.isNull(progressionflag))
//			return;
//		if (progressionflag.equals("0")) {
//			tv_yearlyRR_detail.setText(StringUtil.append2Decimals(
//					(String) detailMap.get(BocInvt.BOCI_DETAILYEARLYRR_RES), 2)
//					+ PER);
//		} else if (progressionflag.equals("1")) {
//
//			tv_yearlyRR_detail.setText(StringUtil.append2Decimals(
//					(String) detailMap.get(BocInvt.BOCI_DETAILYEARLYRR_RES), 2)
//					+ PER);
//
//			tv_yearlyRR_detail.setTextColor(this.getResources().getColor(
//					R.color.blue));
//			tv_yearlyRR_detail.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//			tv_yearlyRR_detail.setText(getString(R.string.progression));
//			tv_yearlyRR_detail.setOnClickListener(mClickListener);
//		}
//	}
//
//	/** 收益累进 */
//	private OnClickListener mClickListener = new OnClickListener() {
//		@Override
//		public void onClick(View v) {
//
//			BaseHttpEngine.showProgressDialog();
//			requestLoginPreOutlayConversationId();
//		}
//	};
//
//	/**功能外置
//	 * 登录之前的conversationId 返回
//	 */
//	public void requestLoginPreOutlayConversationIdCallBack(Object resultObj) {
//		super.requestLoginPreOutlayConversationIdCallBack(resultObj);
//		requestProgressOutlay(
//				(String) detailMap.get(BocInvt.BOCI_DETAILPRODCODE_RES), "0",
//				true);
//	}
//
//	@Override
//	public void progressQueryOutlayCallBack(Object resultObj) {
//		super.progressQueryOutlayCallBack(resultObj);
//		startActivity(new Intent(this, ProgressInfoOutlayActivity.class)
//				.putExtra(BocInvt.PROGRESS_RECORDNUM, progressRecordNumber)
//				.putExtra(BocInvt.BOCI_DETAILPRODNAME_RES,
//						(String) detailMap.get(BocInvt.BOCI_DETAILPRODNAME_RES))
//				.putExtra(BocInvt.BOCI_PRODUCTCODE_REQ,
//						(String) detailMap.get(BocInvt.BOCI_DETAILPRODCODE_RES)));
//	}
//
//	/** 右侧按钮点击事件 */
//	OnClickListener rightBtnClick = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			// 主界面
////			Intent intent = new Intent(ProductDetailOutlayActivity.this,
////					MainActivity.class);
////			startActivity(intent);
//			ActivityTaskManager.getInstance().removeAllSecondActivity();
//		}
//	};
//
//	@Override
//	public void onClick(View v) {
//
//		if (v.getId() == R.id.btn_description_buydetail_outlay) {
//			Intent intent = new Intent(ProductDetailOutlayActivity.this,
//					ProductDescriptionActivity.class);
//			startActivity(intent);
//			overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
//
//		}
//		if (v.getId() == R.id.btn_buy_buydetail_outlay) {
//
//			if (!StringUtil.isNull(status)
//					&& LocalData.boci_StatusMap.get(status).equalsIgnoreCase(
//							LocalData.bocinvtXpadStatus.get(1))) {
//
//				// 签约 购买
//				btn_buy_buydetail_outlay = true;
//				isbuy = true;
//				if (BaseDroidApp.getInstanse().isLogin()) {
//					if (isOpen && !StringUtil.isNullOrEmpty(investBindingInfo)
//							&& isevaluatedBefore) {
//						List<Map<String, Object>> list = BociDataCenter
//								.getInstance().getBocinvtAcctList();
//						if (list != null && list.size() == 1) {
//							BaseDroidApp
//									.getInstanse()
//									.getBizDataMap()
//									.put(ConstantGloble.BOCINVT_BUYINIT_MAP,
//											BociDataCenter.getInstance()
//													.getBocinvtAcctList()
//													.get(0));
//
//							Intent intent = new Intent(
//									ProductDetailOutlayActivity.this,
//									BuyProductChooseActivity.class);
//							intent.putExtra(
//									ConstantGloble.BOICNVT_ISDETAIL_BUY, false);
//							startActivityForResult(intent, ACTIVITY_BUY_CODE);
//
//						} else {
//							Intent intent = new Intent(
//									ProductDetailOutlayActivity.this,
//									BuyProductChooseCardOutlayActivity.class);
//							startActivityForResult(intent,
//									ACTIVITY_BUY_CHOOSEACC);
//						}
//
//					} else {
//						BiiHttpEngine.showProgressDialog();
//						requestPsnInvestmentisOpenBefore();
//					}
//
//				} else {
//
//					Intent intent = new Intent(
//							ProductDetailOutlayActivity.this,
//							LoginActivity.class);
//
//					startActivityForResult(intent,
//							ConstantGloble.ACTIVITY_REQUEST_LOGIN_CODE);
//				}
//
//			} else {
//				BaseDroidApp.getInstanse().showInfoMessageDialog(
//						ProductDetailOutlayActivity.this
//								.getString(R.string.bocinvt_error_buy));
//			}
//
//		}
//	}
//
//	/** 更多点击事件 */
//	final OnClickListener btnMoreNullBocinvtClick = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			Button tv = (Button) v;
//			String text = tv.getText().toString();
//			moreClick(text, v);
//
//		}
//	};
//
//	/** 判断点击事件 */
//	public void moreClick(String text, View v) {
//		if (text.equals(getResources().getString(R.string.product_buy))) {
//			// "购买"
//			buyClick.onClick(v);
//		}
//		if (text.equals(getResources().getString(R.string.product_contract))) {
//			// "投资协议申请"
//			contractClick.onClick(v);
//		}
//
//	}
//
//	// 购买
//	final OnClickListener buyClick = new OnClickListener() {
//
//		@Override
//		public void onClick(View arg0) {
//
//			if (!StringUtil.isNull(status)
//					&& LocalData.boci_StatusMap.get(status).equalsIgnoreCase(
//							LocalData.bocinvtXpadStatus.get(1))) {
//				btn_buy_buydetail_outlay = true;
//				isbuy = true;
//				if (BaseDroidApp.getInstanse().isLogin()) {
//					if (isOpen && !StringUtil.isNullOrEmpty(investBindingInfo)
//							&& isevaluatedBefore) {
//						List<Map<String, Object>> list = BociDataCenter
//								.getInstance().getBocinvtAcctList();
//						if (list != null && list.size() == 1) {
//							BaseDroidApp
//									.getInstanse()
//									.getBizDataMap()
//									.put(ConstantGloble.BOCINVT_BUYINIT_MAP,
//											BociDataCenter.getInstance()
//													.getBocinvtAcctList()
//													.get(0));
//
//							Intent intent = new Intent(
//									ProductDetailOutlayActivity.this,
//									BuyProductChooseActivity.class);
//							intent.putExtra(
//									ConstantGloble.BOICNVT_ISDETAIL_BUY, false);
//							startActivityForResult(intent, ACTIVITY_BUY_CODE);
//
//						} else {
//							Intent intent = new Intent(
//									ProductDetailOutlayActivity.this,
//									BuyProductChooseCardOutlayActivity.class);
//							startActivityForResult(intent,
//									ACTIVITY_BUY_CHOOSEACC);
//						}
//
//					} else {
//						BiiHttpEngine.showProgressDialog();
//						requestPsnInvestmentisOpenBefore();
//					}
//
//				} else {
//					Intent intent = new Intent(
//							ProductDetailOutlayActivity.this,
//							LoginActivity.class);
//					startActivityForResult(intent,
//							ConstantGloble.ACTIVITY_REQUEST_LOGIN_CODE);
//				}
//
//			} else {
//				BaseDroidApp.getInstanse().showInfoMessageDialog(
//						ProductDetailOutlayActivity.this
//								.getString(R.string.bocinvt_error_buy));
//			}
//
//		}
//	};
//	// "投资协议申请"
//	final OnClickListener contractClick = new OnClickListener() {
//
//		@Override
//		public void onClick(View arg0) {
//			if (!StringUtil.isNull(status)
//					&& LocalData.boci_StatusMap.get(status).equalsIgnoreCase(
//							LocalData.bocinvtXpadStatus.get(1))) {
//			btn_buy_buydetail_outlay = true;
//			if (BaseDroidApp.getInstanse().isLogin()) {
//				if (isOpen && !StringUtil.isNullOrEmpty(investBindingInfo)
//						&& isevaluatedBefore) {
//
//					List<Map<String, Object>> list = BociDataCenter
//							.getInstance().getBocinvtAcctList();
//					if (list != null && list.size() == 1) {
//						BaseDroidApp
//								.getInstanse()
//								.getBizDataMap()
//								.put(ConstantGloble.BOCINVT_BUYINIT_MAP,
//										BociDataCenter.getInstance()
//												.getBocinvtAcctList().get(0));
//						Intent intent = new Intent(
//								ProductDetailOutlayActivity.this,
//								AgreementChooseActivity.class);
//						startActivity(intent);
//					} else {
//						Intent intent = new Intent(
//								ProductDetailOutlayActivity.this,
//								BuyProductChooseCardOutlayActivity.class);
//						startActivityForResult(intent, ACTIVITY_BUY_CHOOSEACC);
//					}
//
//				} else {
//					BiiHttpEngine.showProgressDialog();
//					requestPsnInvestmentisOpenBefore();
//				}
//
//			} else {
//				Intent intent = new Intent(ProductDetailOutlayActivity.this,
//						LoginActivity.class);
//				startActivityForResult(intent,
//						ConstantGloble.ACTIVITY_REQUEST_LOGIN_CODE);
//			}
//
//		}else{
//			BaseDroidApp.getInstanse().showInfoMessageDialog(
//					ProductDetailOutlayActivity.this
//							.getString(R.string.bocinvt_error_buy));
//			}
//		}
//	};
//
//	/**
//	 * 请求查询登记账户---回调
//	 * 
//	 * @param resultObj
//	 */
//	@Override
//	public void bocinvtAcctCallback(Object resultObj) {
//		super.bocinvtAcctCallback(resultObj);
//		isOpen = isOpenBefore;
//		isevaluatedBefore = isevaluated;
//		investBindingInfo = investBinding;
//		BiiResponse biiResponse = (BiiResponse) resultObj;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		// 得到response
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//
//		if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
//
//		} else {
//			// 得到result
//			Map<String, Object> map = (Map<String, Object>) BocinvtUtils
//					.httpResponseDeal(resultObj);
//			investBindingInfo = (List<Map<String, Object>>) map
//					.get(BocInvt.BOCI_LIST_RES);
//
//		}
//		BaseDroidApp
//				.getInstanse()
//				.getBizDataMap()
//				.put(ConstantGloble.BOCINVT_XPADRESET_CHOOSE, investBindingInfo);
//		if (isOpen && !StringUtil.isNullOrEmpty(investBindingInfo)
//				&& isevaluatedBefore) {
//			BaseHttpEngine.dissMissProgressDialog();
//			if (btn_buy_buydetail_outlay) {
//
//				List<Map<String, Object>> list = BociDataCenter.getInstance()
//						.getBocinvtAcctList();
//				if (list != null && list.size() == 1) {
//					BaseDroidApp
//							.getInstanse()
//							.getBizDataMap()
//							.put(ConstantGloble.BOCINVT_BUYINIT_MAP,
//									BociDataCenter.getInstance()
//											.getBocinvtAcctList().get(0));
//					if (isbuy) {
//						Intent intent = new Intent(
//								ProductDetailOutlayActivity.this,
//								BuyProductChooseActivity.class);
//						intent.putExtra(ConstantGloble.BOICNVT_ISDETAIL_BUY,
//								false);
//						startActivityForResult(intent, ACTIVITY_BUY_CODE);
//					} else {
//						Intent intent = new Intent(this,
//								AgreementChooseActivity.class);
//						startActivity(intent);
//					}
//
//				} else {
//					Intent intent = new Intent(
//							ProductDetailOutlayActivity.this,
//							BuyProductChooseCardOutlayActivity.class);
//					startActivityForResult(intent, ACTIVITY_BUY_CHOOSEACC);
//				}
//			}
//
//		} else {
//
//			BaseHttpEngine.dissMissProgressDialog();
//			InflaterViewDialog inflater = new InflaterViewDialog(
//					ProductDetailOutlayActivity.this);
//			dialogView = (RelativeLayout) inflater.judgeViewDialog(isOpen,
//					investBindingInfo, isevaluatedBefore, manageOpenClick,
//					invtBindingClick, invtEvaluationClick, exitDialogClick);
//			TextView tv = (TextView) dialogView
//					.findViewById(R.id.tv_acc_account_accountState);
//			tv.setText(this.getString(R.string.bocinvt_query_title));
//			BaseDroidApp.getInstanse().showAccountMessageDialog(dialogView);
//
//		}
//	}
//
//	protected OnClickListener exitDialogClick = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			BaseDroidApp.getInstanse().dismissMessageDialog();
//		}
//	};
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		switch (resultCode) {
//		case RESULT_OK:
//
//			if (requestCode == ACTIVITY_BUY_CODE) {
//				BociDataCenter.getInstance().clearBociData();
//				Intent intent = new Intent();
//				intent.setClass(ProductDetailOutlayActivity.this,
//						QueryProductActivity.class);
//				startActivity(intent);
//				ProductDetailOutlayActivity.this.finish();
//				return;
//			}
//			if (requestCode == ConstantGloble.ACTIVITY_REQUEST_LOGIN_CODE) {
//				if (btn_buy_buydetail_outlay) {
//					BiiHttpEngine.showProgressDialog();
//					requestPsnInvestmentisOpenBefore();
//				}else{
//					BociDataCenter.getInstance().clearBociData();
//					Intent intent = new Intent();
//					intent.setClass(ProductDetailOutlayActivity.this,
//							QueryProductActivity.class);
//					startActivity(intent);
//					ProductDetailOutlayActivity.this.finish();
//				}
////				setText(this.getString(R.string.acc_rightbtn_go_main));
////				setRightBtnClick(rightBtnClick);
//			
//				return;
//			}
//			if (requestCode == ACTIVITY_BUY_CHOOSEACC) {
//
//				if (isbuy) {
//					Intent intent = new Intent(
//							ProductDetailOutlayActivity.this,
//							BuyProductChooseActivity.class);
//					intent.putExtra(ConstantGloble.BOICNVT_ISDETAIL_BUY, false);
//					startActivityForResult(intent, ACTIVITY_BUY_CODE);
//				} else {
//					Intent intent = new Intent(this,
//							AgreementChooseActivity.class);
//					startActivity(intent);
//				}
//
//				return;
//			}
//			if (requestCode == ConstantGloble.ACTIVITY_RESULT_CODE) {
//				// 开通成功的响应
//				isOpen = true;
//
//			} else if (requestCode == ConstantGloble.ACTIVITY_REQUEST_INVTBINDING_CODE) {
//				// 登记账户成功的响应
//				investBindingInfo = BociDataCenter.getInstance()
//						.getUnSetAcctList();
//
//			} else if (requestCode == ConstantGloble.ACTIVITY_REQUEST_INVTEVALUATION_CODE) {
//				// 风险评估成功的响应
//				if (BociDataCenter.getInstance().getI() == 1) {
//
//				} else {
//					isevaluatedBefore = true;
//				}
//
//			}
//			if (isOpen && !StringUtil.isNullOrEmpty(investBindingInfo)
//					&& isevaluatedBefore) {
//				BaseHttpEngine.showProgressDialog();
//				requestBociAcctList("1", "1");
//			} else {
//				InflaterViewDialog inflater = new InflaterViewDialog(
//						ProductDetailOutlayActivity.this);
//				dialogView = (RelativeLayout) inflater.judgeViewDialog(isOpen,
//						investBindingInfo, isevaluatedBefore, manageOpenClick,
//						invtBindingClick, invtEvaluationClick, exitDialogClick);
//				TextView tv = (TextView) dialogView
//						.findViewById(R.id.tv_acc_account_accountState);
//				tv.setText(this.getString(R.string.bocinvt_query_title));
//				BaseDroidApp.getInstanse().showAccountMessageDialog(dialogView);
//			}
//
//			break;
//		case RESULT_CANCELED:
//			if (BaseDroidApp.getInstanse().isLogin()) {
//				if (isOpen && !StringUtil.isNullOrEmpty(investBindingInfo)
//						&& isevaluatedBefore) {
//				} else {
//					InflaterViewDialog inflater = new InflaterViewDialog(
//							ProductDetailOutlayActivity.this);
//					dialogView = (RelativeLayout) inflater.judgeViewDialog(
//							isOpen, investBindingInfo, isevaluatedBefore,
//							manageOpenClick, invtBindingClick,
//							invtEvaluationClick, exitDialogClick);
//					TextView tv = (TextView) dialogView
//							.findViewById(R.id.tv_acc_account_accountState);
//					tv.setText(this.getString(R.string.bocinvt_query_title));
//					BaseDroidApp.getInstanse().showAccountMessageDialog(
//							dialogView);
//
//				}
//			}
//			btn_buy_buydetail_outlay = false;
//			isbuy = false;
//			break;
//		default:
//			break;
//		}
//	}
//
//	/*
//	 * 返回键 未登陆 直接finish 登陆 跳转到之原始查询页面
//	 */
//	public void onBackPressed() {
//		if (BaseDroidApp.getInstanse().isLogin()) {
//			BociDataCenter.getInstance().clearBociData();
//			Intent intent = new Intent();
//			intent.setClass(ProductDetailOutlayActivity.this,
//					QueryProductActivity.class);
//			startActivity(intent);
//			ProductDetailOutlayActivity.this.finish();
//
//		} else {
//			setResult(RESULT_CANCELED);
//			finish();
//		}
//
//	}
}
