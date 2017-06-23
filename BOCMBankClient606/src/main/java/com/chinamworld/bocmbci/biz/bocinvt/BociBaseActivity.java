package com.chinamworld.bocmbci.biz.bocinvt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.BusinessModelControl;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt.acctmanager.BocinvtAcctListActivity;
import com.chinamworld.bocmbci.biz.bocinvt.dealhistory.QueryHistoryProductActivity;
import com.chinamworld.bocmbci.biz.bocinvt.myproduct.OcrmProductListActivity;
import com.chinamworld.bocmbci.biz.bocinvt.productlist.InvtBindingChooseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.productlist.InvtEvaluationInputActivity;
import com.chinamworld.bocmbci.biz.bocinvt.productlist.QueryProductActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.inverstagreemanager.InvestInvalidAgreeQueryActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.MyInvetProductActivity;
import com.chinamworld.bocmbci.biz.invest.activity.InvesAgreeActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.ErrorCode;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.GetPhoneInfo;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.bocmbci.widget.UsbKeyText;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

/**
 * 中银理财基类，初始化左边二级菜单、底部菜单栏和头部的下拉菜单，处理通信
 * 
 * @author wangchao
 * 
 */
public class BociBaseActivity extends BaseActivity {
	protected Button back; // 头部返回按钮
	public LayoutInflater mInflater;
	public LinearLayout tabcontent = null;// 加载布局
	/** 右上角按钮 */
	protected Button btn_right;
	/** 右侧按钮点击事件 */
	private OnClickListener rightBtnClick;
	/** 退出动画 */
	protected Animation animation_up;
	/** 进入动画 */
	protected Animation animation_down;
	/** 左侧返回按钮点击事件 */
	private OnClickListener backBtnClick;
	/** 产品详情购买 */
	public static final int ACTIVITY_BUY_CODE = 11;
	public static final String MATCH = "0";
	public static final String NOMATCHCAN = "1";
	public static final String NOMATCH = "2";
	public static final String PER = "%";
	public static final String DAY = "天";
	public Button btn_show;
	/** 自助关联上送服务码 */
	protected String relevanceServiceId = "PB010";
	
	/** 优汇通专户 */
	public static final String YOUHUITONGZH = "199";
	protected static final String MEDICALACC = "医保账户";
	// 购买弹出框
	/** 是否开通投资理财 */
	public boolean isOpenBefore;
	/** 是否有风险评估经验 */
	public boolean isevaluated;
	/** 登记账户信息 */
	public List<Map<String, Object>> investBinding;
	/** 任务弹出框视图 */
	public RelativeLayout dialogView2;
	/** 不限期 */
	public static final String NOPERIODSTR = "-1";
	public String progressRecordNumber;
	public int position;
	public boolean isTask;
	/**中银E盾*/
	public UsbKeyText usbKeytext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);
		// 初始化弹窗按钮
		initPulldownBtn();
		// 初始化左边菜单
		initLeftSideList(this, LocalData.bocinvtManagerLeftList);
		// 初始化底部菜单栏
		initFootMenu();
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body); // 加载布局
		mInflater = LayoutInflater.from(this);
		btn_right = (Button) findViewById(R.id.ib_top_right_btn);
		back = (Button) findViewById(R.id.ib_back); // 头部返回按钮
		btn_show = (Button) findViewById(R.id.btn_show);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (backBtnClick != null) {
					backBtnClick.onClick(v);
				} else if (backClick != null) {
					backClick.onClick(v);
				}
			}
		});
		initanimation();
	}
	
	public void goneLeftButton(){
		back.setVisibility(View.GONE);
	}

	public void gonerightBtn() {
		btn_right.setVisibility(View.GONE);
	}

	OnClickListener backClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};

	private void initanimation() {
		animation_up = new AnimationUtils().loadAnimation(this,
				R.anim.scale_out);
		animation_down = new AnimationUtils().loadAnimation(this,
				R.anim.scale_in);
	}

	/**
	 * 设置右侧按钮文字
	 * 
	 * @param title
	 */
	public void setText(String title) {
		btn_right.setVisibility(View.VISIBLE);
		btn_right.setText(title);
		btn_right.setTextColor(Color.WHITE);
		btn_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (rightBtnClick != null) {
					rightBtnClick.onClick(v);
				}
			}
		});
	}
	
	/** 隐藏底部tab */
	public void setBottomTabGone() {
		View menuPopwindow = (View) findViewById(R.id.menu_popwindow);
		menuPopwindow.setVisibility(View.GONE);
	}

	// protected OnClickListener exitDialogClick = new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// BaseDroidApp.getInstanse().dismissMessageDialog();
	// }
	// };

	/**
	 * 改变输入
	 * 
	 * @param currency
	 */
	public void checkCurrency(String currency, EditText et) {
		if (!StringUtil.isNull(currency)) {
			if (japList.contains(currency)) {
				// et.setInputType(InputType.TYPE_CLASS_NUMBER);
			}
		}

	}

	/**
	 * 是否是日元
	 * 
	 * @param currency
	 * @return
	 */
	public boolean checkJap(String currency) {
		if (!StringUtil.isNull(currency)) {
			if (japList.contains(currency)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 日元校验
	 * 
	 * @param currency
	 *            币种
	 * @param text1
	 *            校验一
	 * @param text2
	 *            校验二
	 * @return 校验
	 */
	public RegexpBean checkJapReg(String currency, String text1, String text2) {
		RegexpBean rebnew = null;
		if (checkJap(currency)) {
			rebnew = new RegexpBean(text1, text2, "spetialAmount");
		} else {
			rebnew = new RegexpBean(text1, text2, "amount");
		}
		return rebnew;
	}

	/**
	 * 日元最低、高限额校验
	 * 
	 * @param currency
	 *            币种
	 * @param text1
	 *            校验一
	 * @param text2
	 *            校验二
	 * @return 校验
	 */
	public RegexpBean checkJapRegForMin(String currency, String text1,
			String text2) {
		RegexpBean rebnew = null;
		if (checkJap(currency)) {
			rebnew = new RegexpBean(text1, text2, "jpnminAmount2");
		} else {
			rebnew = new RegexpBean(text1, text2, "minAmount2");
		}
		return rebnew;
	}

	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
			if (BocInvt.PSNINVTEVALUATIONINIT_API.equals(biiResponseBody
					.getMethod())) {
				if (biiResponse.isBiiexception()) {// 代表返回数据异常
					BiiHttpEngine.dissMissProgressDialog();
					BiiError biiError = biiResponseBody.getError();
					// 判断是否存在error
					if (biiError != null) {
						if (biiError.getCode() != null) {
							if (biiResponseBody.getError().getCode()
									.equals(ErrorCode.BOC_FIRST_EVALUATION)) {
								BiiHttpEngine.dissMissProgressDialog();
								BaseDroidApp.getInstanse()
										.createDialog(
												"",
												biiResponseBody.getError()
														.getMessage(),
												new OnClickListener() {

													@Override
													public void onClick(View v) {
														BaseDroidApp
																.getInstanse()
																.dismissErrorDialog();
														ActivityTaskManager
																.getInstance()
																.removeAllSecondActivity();
													}
												});
								return true;
							}
							return super.httpRequestCallBackPre(resultObj);
						}
						return super.httpRequestCallBackPre(resultObj);
					}
					return true;
				}
				return false;// 没有异常
			} else {
				if (biiResponse.isBiiexception()) {// 代表返回数据异常
					BiiHttpEngine.dissMissProgressDialog();
					BiiError biiError = biiResponseBody.getError();
					// 判断是否存在error
					if (biiError != null) {
						if (biiError.getCode() != null) {
							if (biiResponseBody.getError().getCode()
									.equals(ErrorCode.BOC_NO_REL)) {
								BiiHttpEngine.dissMissProgressDialog();
								BaseDroidApp
										.getInstanse()
										.createDialog(
												"",
												BaseDroidApp
														.getInstanse()
														.getCurrentAct()
														.getString(
																R.string.bocinvt_error_in),
												new OnClickListener() {

													@Override
													public void onClick(View v) {
														BaseDroidApp
																.getInstanse()
																.dismissErrorDialog();
														ActivityTaskManager
																.getInstance()
																.removeAllSecondActivity();
													}
												});
								return true;
							}
							return super.httpRequestCallBackPre(resultObj);
						}
						return super.httpRequestCallBackPre(resultObj);
					}
					return true;
				}
				return false;
			}
		}
		return super.httpRequestCallBackPre(resultObj);
	}

//	public void showTimeOutDialog(BiiError biiError) {
//		BaseDroidApp.getInstanse().showMessageDialog(biiError.getMessage(),
//				new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						BaseDroidApp.getInstanse().dismissErrorDialog();
//						ActivityTaskManager.getInstance().removeAllSecondActivity();
//						Intent intent = new Intent();
//						intent.setClass(BociBaseActivity.this,
//								LoginActivity.class);
//						startActivityForResult(intent,
//								ConstantGloble.ACTIVITY_RESULT_CODE);
//					}
//				});
//	}

	/**
	 * 在slidingbody中引入自己布局文件
	 * 
	 * @param resource
	 * @return 引入布局
	 */
	public View addView(int resource) {
		View view = LayoutInflater.from(this).inflate(resource, null);
		tabcontent.addView(view);
		return view;
	}

	/**
	 * 左边菜单栏
	 */
	@Override
	protected boolean selectedMenuItemHandler(Activity context, ImageAndText menuItem) {
		super.selectedMenuItemHandler(context, menuItem);
		ActivityTaskManager.getInstance().removeAllSecondActivity();
		Intent intent = new Intent();
		String menuId = menuItem.MenuID;
		if(menuId.equals("bocinvtManager_1")){
			intent.setClass(this, BocinvtAcctListActivity.class);
		}
		else if(menuId.equals("bocinvtManager_2")){
			intent.setClass(this, MyInvetProductActivity.class);
		}
		else if(menuId.equals("bocinvtManager_3")){
			intent.setClass(this, QueryProductActivity.class);
		}
		else if(menuId.equals("bocinvtManager_4")){
			intent.setClass(this, InvestInvalidAgreeQueryActivity.class);
		}
		else if(menuId.equals("bocinvtManager_5")){
			intent.setClass(this, OcrmProductListActivity.class);
			intent.putExtra("flag", true);
		}
		else if(menuId.equals("bocinvtManager_6")){
			intent.setClass(this, QueryHistoryProductActivity.class);
		}
		context.startActivity(intent);
		return true;
		
		
		
//		super.setSelectedMenu(clickIndex);
//		ActivityTaskManager.getInstance().removeAllSecondActivity();
//		Intent intent = new Intent();
//		switch (clickIndex) {
//		case 0:
//			intent.setClass(this, MyInvetProductActivity.class);
//			break;
//		case 1:
//			intent.setClass(this, QueryProductActivity.class);
//			break;
//		case 2:
////			intent.setClass(this, QueryAgreeActivity.class);
//			intent.setClass(this, InvestInvalidAgreeQueryActivity.class);
//			break;
//		case 3:
//			intent.setClass(this, BocinvtAcctListActivity.class);
//			break;
//		case 4:
//			intent.setClass(this, QueryHistoryProductActivity.class);
//			break;
//		case 5:
//			intent.setClass(this, OcrmProductListActivity.class);
//			intent.putExtra("flag", true);
//			break;
//		}
//		startActivity(intent);
	}

	/**
	 * @param rightBtnClick
	 *            the rightBtnClick to set
	 */
	public void setRightBtnClick(OnClickListener rightBtnClick) {
		this.rightBtnClick = rightBtnClick;
	}

	

//	/**
//	 * 隐藏左侧菜单栏
//	 */
//	public void goneLeftView() {
//		// 隐藏左侧菜单
//		LinearLayout slidingTab = (LinearLayout) findViewById(R.id.sliding_tab);
//		// Button btn_show = (Button) findViewById(R.id.btn_show);
//		Button btn_hide = (Button) findViewById(R.id.btn_hide);
//		Button btn_fill_show = (Button) findViewById(R.id.btn_fill_show);
//		slidingTab.setVisibility(View.GONE);
//		btn_show.setVisibility(View.GONE);
//		setLeftButtonPopupGone();
//		btn_hide.setVisibility(View.GONE);
//		btn_fill_show.setVisibility(View.GONE);
//	}

	/**
	 * @param backBtnClick
	 *            the backBtnClick to set
	 */
	public void setBackBtnClick(OnClickListener backBtnClick) {
		this.backBtnClick = backBtnClick;
	}

	public static Map<String, String> prodTimeLimitMap = new HashMap<String, String>() {
		{
			put("0", "有投资经验");
			put("1", "所有客户");
		}
	};
	/**
	 * 证件类型
	 */
	public static Map<String, String> myidentityType = new HashMap<String, String>() {
		{
			put("0", "请选择");
			put("1", "身份证");
			put("2", "临时居民身份证");
			put("3", "户口簿");
			put("4", "军人身份证");
			put("5", "武装警察身份证");
			put("6", "港澳居民通行证");
			put("7", "台湾居民通行证");
			put("8", "护照");
			put("9", "其他证件");
			put("10", "港澳台居民往来内地通行证");
			put("11", "外交人员身份证");
			put("12", "外国人居留许可证");
			put("13", "边民出入境通行证");
			put("47", "港澳居民来往内地通行证");
			put("48", "港澳居民来往内地通行证");
			put("49", "台湾居民来往大陆通行证");
		}
	};

	/**
	 * 判断是否开通投资理财服务
	 */
	public void requestPsnInvestmentisOpenBefore() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_API);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnInvestmentisOpenBeforeCallback");
	}

	/**
	 * 判断是否开通投资理财服务---回调
	 * 
	 * @param resultObj
	 */
	public void requestPsnInvestmentisOpenBeforeCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String isOpenresult = String.valueOf(biiResponseBody.getResult());
		if (StringUtil.isNull(isOpenresult)) {
			BiiHttpEngine.dissMissProgressDialog();
			return;
		}
		boolean isOpenBeforeOr = Boolean.valueOf(isOpenresult);
		// isOpenBefore
		if (isOpenBeforeOr) {
			isOpenBefore = true;
		} else {
			isOpenBefore = false;
		}
		// 请求风险评估
		requestInvtEva();
	}

	/**
	 * 请求是否进行过风险评估
	 */
	public void requestInvtEva() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.PSNINVTEVALUATIONINIT_API);
		HttpManager.requestBii(biiRequestBody, this, "requestInvtEvaCallback");
	}

	/**
	 * 请求是否进行过风险评估---回调
	 * 
	 * @param resultObj
	 */
	public void requestInvtEvaCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> responseMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(responseMap)) {
			BiiHttpEngine.dissMissProgressDialog();
			return;
		}
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.BOICNVT_ISBEFORE_RESULT, responseMap);
		String status = (String) responseMap.get(BocInvt.BOCIEVA_STATUS_RES);
		if (!StringUtil.isNull(status)
				&& status.equals(ConstantGloble.BOCINVT_EVA_SUC_STATUS)) {
			isevaluated = true;
		} else {
			isevaluated = false;
		}
		requestBociAcctList("1","0");
	}

	/**
	 * 请求理财账户信息
	 * @param acctSatus
	 * @param acctType
	 */
	public void requestBociAcctList(String acctSatus,String acctType) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.BOCINVTACCTINFO);
		Map<String, String> map = new Hashtable<String, String>();
		map.put(BocInvt.ACCOUNTSATUS, acctSatus);
		map.put(BocInvt.QUERYTYPE, acctType);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,"bocinvtAcctCallback");
	}

	@SuppressWarnings("unchecked")
	public void bocinvtAcctCallback(Object resultObj) {
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		investBinding = (List<Map<String, Object>>) map.get(BocInvt.BOCI_LIST_RES);
		BociDataCenter.getInstance().setBocinvtAcctList(investBinding);
	}
	
	/**
	 * 请求账户
	 * @param acctype
	 */
	public void requestBankAcctList(List<String> acctype) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.QRY_ALL_BANK_ACCOUNT);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Comm.ACCOUNT_TYPE, acctype);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "bankAccListCallBack");
	}
	
	@SuppressWarnings("unchecked")
	public void bankAccListCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		List<Map<String, Object>> list = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(list)) {
			if (isTask) {
				CustomDialog.toastInCenter(this, getString(R.string.bocinvt_binding_relevance)); return;
			}
			showNotiDialog(); return;
		}
		if (StringUtil.isNullOrEmpty(BociDataCenter.getInstance().getBocinvtAcctList())) {
			BociDataCenter.getInstance().setUnSetAcctList(list);
		}else{
			ArrayList<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
			StringBuffer b = new StringBuffer();
			for(Map<String, Object> map : BociDataCenter.getInstance().getBocinvtAcctList()){
				b.append(map.get(BocInvt.ACCOUNTNO));
			}
			for (int i = 0; i < list.size(); i++) {
				String allAcctNum = (String) list.get(i).get(Comm.ACCOUNTNUMBER);
				if (!b.toString().contains(allAcctNum)) {
					newList.add(list.get(i));
				}
			}
			if (StringUtil.isNullOrEmpty(newList)) {
				showNotiDialog();
				return;
			}
			BociDataCenter.getInstance().setUnSetAcctList(newList);
		}
		startActivityForResult(new Intent(this, InvtBindingChooseActivity.class),ConstantGloble.ACTIVITY_REQUEST_INVTBINDING_CODE);
		overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
	}
	
	
	private void showNotiDialog(){
		BaseDroidApp.getInstanse().showErrorDialog(getString(R.string.bocinvt_binding_relevance),
				R.string.cancle, R.string.acc_myaccount_relevance_title,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						switch (Integer.parseInt(v.getTag() + "")) {
						case CustomDialog.TAG_SURE:
							BaseDroidApp.getInstanse().dismissErrorDialog();
//							startActivityForResult(new Intent(BociBaseActivity.this,
//									AccInputRelevanceAccountActivity.class),
//									ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
							
							BusinessModelControl.gotoAccRelevanceAccount(BociBaseActivity.this, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE, null);
							
							break;
						case CustomDialog.TAG_CANCLE:
							BaseDroidApp.getInstanse().dismissErrorDialog();
							break;
						}
					}
				});
	}

	/**
	 * 请求登记资金账户列表信息
	 */
//	public void requestXpadReset() {
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(BocInvt.PSNXPADRESET_API);
//		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
//				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
//		// 通讯开始,展示通讯框
//		BiiHttpEngine.showProgressDialog();
//		HttpManager
//				.requestBii(biiRequestBody, this, "requestXpadResetCallback");
//	}

	/**
	 * 请求登记资金账户列表信息---回调
	 * 
	 * @param resultObj
	 */
//	public void requestXpadResetCallback(Object resultObj) {
//		BiiResponse biiResponse = (BiiResponse) resultObj;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		// 得到response
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		// 通讯结束,隐藏通讯框
//		BiiHttpEngine.dissMissProgressDialog();
//		if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
//			// 没有可登记账户
//			BaseDroidApp.getInstanse().showInfoMessageDialog(
//					this.getString(R.string.bocinvt_binding_relevance));
//		} else {
//			List<Map<String, Object>> responseList = (List<Map<String, Object>>) biiResponseBody
//					.getResult();
//			// 有可登记账户
//			BaseDroidApp.getInstanse().getBizDataMap()
//					.put(ConstantGloble.BOCINVT_XPADRESET_LIST, responseList);
//			Intent intent = new Intent(this, InvtBindingChooseActivity.class);
//			startActivityForResult(intent,
//					ConstantGloble.ACTIVITY_REQUEST_INVTBINDING_CODE);
//			overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
//		}
//
//	}

	/** 开通投资理财监听事件 */
	protected OnClickListener manageOpenClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 跳转到投资理财服务协议页面
			Intent gotoIntent = new Intent(BociBaseActivity.this,
					InvesAgreeActivity.class);
			startActivityForResult(gotoIntent,
					ConstantGloble.ACTIVITY_RESULT_CODE);
			overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
		}
	};
	/** 登记账户监听事件 */
	protected OnClickListener invtBindingClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 请求登记资金账户信息
			isTask = true;
			BaseHttpEngine.showProgressDialog();
			List<String> acctype = new ArrayList<String>();
			acctype.add("119");
			acctype.add("188");
			requestBankAcctList(acctype);
		}
	};
	/** 风险评估监听事件 */
	protected OnClickListener invtEvaluationClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(BociBaseActivity.this,
					InvtEvaluationInputActivity.class);
			startActivityForResult(intent,
					ConstantGloble.ACTIVITY_REQUEST_INVTEVALUATION_CODE);
			overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
		}
	};
	protected OnClickListener exitDialogClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();
		}
	};
	/**
	 * 定投类型
	 */
	public static final List<String> timeInvestTypeList = new ArrayList<String>() {
		{
			add("购买");// 0
			add("赎回");// 1

		}
	};
	public static final Map<String, String> timeInvestTypeMap = new HashMap<String, String>() {
		{
			put("0", "购买");
			put("1", "赎回");
		}
	};
	/** 定投频率类型—— d:天, w:周, m:月, y:年； */
	public static final List<String> timeInvestRateFlagList = new ArrayList<String>() {
		{
			add("天");
			add("周");
			add("月");
			add("年");
		}
	};
	/** 定投频率类型—— d:天, w:周, m:月, y:年； */
	public static final List<String> timeInvestRateFlagSubList = new ArrayList<String>() {
		{
			add("d");
			add("w");
			add("m");
			add("y");
		}
	};
	public static final Map<String, String> timeInvestRateFlagSubMap = new HashMap<String, String>() {
		{
			put("d", "天");
			put("w", "周");
			put("m", "月");
			put("y", "年");
		}
	};
	/** 投资方式 */
	public static final List<String> investTypeList = new ArrayList<String>() {
		{
			add("定时定额");// 0
			add("自动投资");// 1
		}
	};
	public static final Map<String, String> investTypeMap = new HashMap<String, String>() {
		{
			put("0", "定时定额");
			put("1", "自动投资");
		}
	};
	/** 投资方式_定投类型 */
	public static final List<String> investTypeSubList = new ArrayList<String>() {
		{
			add("0");// 0
			add("1");// 1
		}
	};
	/** 钞汇 */
	public static final List<String> agreeCashRemitList = new ArrayList<String>() {
		{
			add("0");// -
			add("1");// 现钞
			add("2");// 现汇
		}
	};
	/** 钞汇 */
	public static final Map<String, String> agreeCashRemitMap = new HashMap<String, String>() {
		{
			put("0", "-");// -
			put("1", "现钞");// 现钞
			put("2", "现汇");// 现汇
		}
	};
	/** 续约状态 */
	public static final Map<String, String> agreecontStatusMap = new HashMap<String, String>() {
		{
			put("00", "正常");
			put("01", "最后一期");
			put("02", "续约结束");
			put("03", "协议终止");
			put("04", "协议失效");
			put("05", "协议撤销");
		}
	};
	/** 日元币种 */
	public static final List<String> japList = new ArrayList<String>() {
		{
			add("027");
			add("JPY");
			add("088");
			add("KRW");
			add("064");
			add("VND");
		}
	};
	/** 投资状态 */
	public static final Map<String, String> contStatusMap = new HashMap<String, String>() {
		{

			put("0", "正常");
			put("1", "暂停");
			put("2", "撤销");
			put("3", "到期");
		}
	};
	/** 本期状态 */
	public static final Map<String, String> lastStatusMap = new HashMap<String, String>() {
		{
			put("0", "待处理");
			put("1", "本期成功");
			put("2", "本期失败");
		}
	};
	public static final List<String> bocSerlistList = new ArrayList<String>() {
		{
			add("修改");
			add("暂停");
			add("开始");
			add("撤销");
		}
	};
	/** 协议管理 */
	public static final List<String> bocInverstList = new ArrayList<String>() {
		{
			add("修改投资信息");
			add("终止投资协议");
		}
	};
	/** 持仓管理 */
	public static final List<String> bocMyProductList = new ArrayList<String>() {
		{
			add("追加购买");
			add("继续购买");
			add("赎回");
			add("份额转换");
			add("投资协议管理");
			add("设置分红方式");
		}
	};
	/** 维护标识 */
	public static final List<String> bocSerListUp = new ArrayList<String>() {
		{
			add("0");
			add("1");
			add("2");
			add("3");
		}
	};
	/** 提示信息 */
	public static final List<String> dingeditSuccessList = new ArrayList<String>() {
		{
			add("");
			add("定时定额投资协议暂停操作成功");
			add("定时定额投资协议开始操作成功");
			add("定时定额投资协议撤销操作成功");
		}
	};
	public static final List<String> dingeditConfirmList = new ArrayList<String>() {
		{
			add("");
			add("定时定额投资协议暂停，请确认");
			add("定时定额投资协议开始，请确认");
			add("定时定额投资协议撤销，请确认");
		}
	};
	public static final List<String> zidongeditSuccessList = new ArrayList<String>() {
		{
			add("");
			add("自动投资协议暂停操作成功");
			add("自动投资协议开始操作成功");
			add("自动投资协议撤销操作成功");
		}
	};
	public static final List<String> zidongeditConfirmList = new ArrayList<String>() {
		{
			add("");
			add("自动投资协议暂停，请确认");
			add("自动投资协议开始，请确认");
			add("自动投资协议撤销，请确认");
		}
	};
	
	/**
	 * 网上专属理财资金账户信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getOFBankAccountInfo(){
		Map<String, Object> acctOFmap = BociDataCenter.getInstance().getAcctOFmap();
		if (StringUtil.isNullOrEmpty(acctOFmap)) return null;
		if (acctOFmap.containsKey(BocInvt.BANKACCOUNT)) {
			return (Map<String, Object>) acctOFmap.get(BocInvt.BANKACCOUNT);
		}
		return (Map<String, Object>) acctOFmap.get(BocInvt.MAINACCOUNT);
	}

	// 401协议维护/////////////////////////////////////////
	public void requestPsnXpadAutomaticAgreementMaintainResult(String agreementType) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody
				.setMethod(BocInvt.BOCINVT_PSNXPADAUTOMATICAGREEMENTMAINTAINRESULT_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap = BociDataCenter.getInstance().getAgreeInputMap();
		paramsmap.put(BocInvt.BOC_AGREE_CANCEL_TOKEN_REQ, (String) BaseDroidApp
				.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID));
		paramsmap.put(BocInvt.BOCINVT_BINDING_ACCOUNTNUMBER_RES, (String)BociDataCenter.
				getInstance().getPeriodDetailMap().get(BocInvt.BANCACCOUNT));
		paramsmap.put(BocInvt.BOC_EXTEND_AGREEMENTTYPE_REQ, agreementType);
		biiRequestBody.setParams(paramsmap);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnXpadAutomaticAgreementMaintainResultCallBack");
	}

	/** 请求协议维护回调 */
	public void requestPsnXpadAutomaticAgreementMaintainResultCallBack(
			Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		BiiHttpEngine.dissMissProgressDialog();
		Map<String, Object> autoMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(autoMap)) {
			return;
		}
		BociDataCenter.getInstance().setAutoResultMap(autoMap);
	}

	/**
	 *  收益累进
	 * @param accountId
	 * @param productCode
	 * @param currentIndex
	 * @param refresh
	 */
	public void requestProgress(String accountKey,String productCode,String currentIndex,boolean refresh) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.METHOD_PROGRESSQUERY);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HashMap<String, Object> param = new HashMap<String, Object>();
//		param.put(Comm.ACCOUNT_ID, accountId);
		param.put(BocInvt.ACCOUNTQUERY_ACCOUNTKEY_RES, accountKey);//p603 accountId修改为accountKey
		param.put(BocInvt.BOCI_PRODUCTCODE_REQ, productCode);
		param.put(BocInvt.BOCINCT_XPADTRAD_CURRENTINDEX_REQ, currentIndex);
		param.put(BocInvt.BOCINCT_XPADTRAD_PAGESIZE_REQ, ConstantGloble.LOAN_PAGESIZE_VALUE);
		param.put(BocInvt.BOCINCT_XPADTRAD_REFRESH_REQ, String.valueOf(refresh));
		biiRequestBody.setParams(param);
		HttpManager.requestBii(biiRequestBody, this, "progressQueryCallBack");
	}

	@SuppressWarnings("unchecked")
	public void progressQueryCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(result))return;
		List<Map<String, Object>> progressionList = (List<Map<String, Object>>) result.get(BocInvt.PROGRESS_LIST);
		progressRecordNumber = (String) result.get(BocInvt.PROGRESS_RECORDNUM);
		if (StringUtil.isNullOrEmpty(progressionList))return;
		BociDataCenter.getInstance().setProgressionList(progressionList);
	}
	
	
	/**
	 *   功能外置 收益累进 
	 * @param accountId
	 * @param productCode
	 * @param currentIndex
	 * @param refresh
	 */
	public void requestProgressOutlay(String productCode,String currentIndex,boolean refresh) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.METHOD_PROGRESSQUERYOUTLAY);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.LOGIN_OUTLAY_PRECONVERSATIONID));
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put(BocInvt.BOCI_PRODUCTCODE_REQ, productCode);
		param.put(BocInvt.BOCINCT_XPADTRAD_CURRENTINDEX_REQ, currentIndex);
		param.put(BocInvt.BOCINCT_XPADTRAD_PAGESIZE_REQ, ConstantGloble.LOAN_PAGESIZE_VALUE);
		param.put(BocInvt.BOCINCT_XPADTRAD_REFRESH_REQ, String.valueOf(refresh));
		biiRequestBody.setParams(param);
		HttpManager.requestOutlayBii(biiRequestBody, this, "progressQueryOutlayCallBack");
	}

	@SuppressWarnings("unchecked")
	public void progressQueryOutlayCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(result))return;
		List<Map<String, Object>> progressionList = (List<Map<String, Object>>) result.get(BocInvt.PROGRESS_LIST);
		progressRecordNumber = (String) result.get(BocInvt.PROGRESS_RECORDNUM);
		if (StringUtil.isNullOrEmpty(progressionList))return;
		BociDataCenter.getInstance().setProgressionList(progressionList);
	}
	
	
	/**
	 * 请求指令交易产品查询
	 * @param index
	 * @param refresh
	 */
	public void requestOcrmProductQuery(int index,String refresh) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.PSNOCRMPRODUCTQUERY);
		biiRequestBody.setConversationId(BociDataCenter.getInstance().getOcrmConversationId());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(BocInvt.BOCI_CURRENTINDEX_REQ, String.valueOf(index));
		params.put(BocInvt.BOCI_PAGESIZE_REQ,ConstantGloble.LOAN_PAGESIZE_VALUE);
		params.put(BocInvt.BOCI_REFRESH_REQ,refresh);
		params.put(BocInvt.PROTPYE,"02");
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this,"responseOcrmProductQueryCallback");
	}
	
	@SuppressWarnings("unchecked")
	public void responseOcrmProductQueryCallback(Object resultObj) {
		BociDataCenter.getInstance().setOcrmMap((Map<String, Object>) HttpTools.getResponseResult(resultObj));
	}
	
	/**
	 * 账户类型
	 */
	public static List<String> accountTypeList = new ArrayList<String>() {
		{
			add("101");
			add("103");
			add("104");
			add("119");
			add("107");
			add("108");
			add("109");
			add("140");
			add("150");
			add("152");
			add("170");
			add("188");
			add("190");
			add("300");

		};
	};

	/** 是否有电子现金账户 */
	public static final List<String> isHaveECashAccountList = new ArrayList<String>() {
		{
			add("0");
			add("1");
			add("2");
		}
	};
	
	/**
	 * 请求自助关联提交交易
	 * 
	 * @param accountType
	 *            账户类型
	 * @param accountNumber
	 *            账号
	 * @param mainAccountNumber
	 *            待关联借记卡主账户
	 * @param isHaveEleCashAcct
	 *            是否有电子现金账户
	 * @param linkAcctFlag
	 *            关联标识
	 * @param currencyCode2
	 *            待关联账户的第二币种
	 * @param currencyCode
	 *            待关联账户的第一币种
	 * @param cardDescription
	 *            待关联账户卡描述
	 * @param branchId
	 *            待关联账户机构号
	 * @param selectList
	 *            勾选的要关联的账户
	 * @param devicePrint
	 *            设备指纹
	 * @param token
	 *            防重标志
	 * @param signedData
	 *            CA密文
	 * @param Smc
	 *            手机验证码
	 * @param Otp
	 *            动态口令
	 */
	public void requestPsnRelevanceAccountResult(
			String accountType, String accountNumber, String mainAccountNumber,
			String isHaveEleCashAcct, String linkAcctFlag,
			String currencyCode2, String currencyCode, String cardDescription,
			String branchId, List<Map<String, String>> selectList,
			String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.ACC_PSNRELEVANCEACCOUNTRESULT_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Acc.RELEVANCEACCRES_ACCOUNTTYPE_REQ, accountType);
		map.put(Acc.RELEVANCEACCRES_ACCOUNTNUMBER_REQ, accountNumber);
		map.put(Acc.RELEVANCEACCRES_MAINACCOUNTNUMBER_REQ, mainAccountNumber);
		map.put(Acc.RELEVANCEACCRES_ISHAVEELECASHACCT_REQ, isHaveEleCashAcct);
		map.put(Acc.RELEVANCEACCRES_LINKACCTFLAG_REQ, linkAcctFlag);
		map.put(Acc.RELEVANCEACCRES_CURRENCYCODE2_REQ, currencyCode2);
		map.put(Acc.RELEVANCEACCRES_CURRENCYCODE_REQ, currencyCode);
		map.put(Acc.RELEVANCEACCRES_BRANCHID_REQ, branchId);
		map.put(Acc.RELEVANCEACCRES_SELECTEDACCOUNTARRAY_REQ, selectList);
//		map.put(Acc.RELEVANCEACCRES_SIGNEDDATA_REQ, signedData);// 密文
		map.put(Acc.RELEVANCEACCRES_TOKEN_REQ, token);
		/** 安全工具参数获取 */
		usbKeytext.InitUsbKeyResult(map);
		SipBoxUtils.setSipBoxParams(map);
		GetPhoneInfo.initActFirst(this);
		GetPhoneInfo.addPhoneInfo(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnRelevanceAccountResultCallback");
	}
	/**
	 * 账户自助关联提交交易——回调
	 * 
	 * @param resultObj
	 */
	public void requestPsnRelevanceAccountResultCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {

		} else {
			Map<String, Object> debitSuccessMap = (Map<String, Object>) biiResponseBody
					.getResult();
			AccDataCenter.getInstance().setDebitlistSuccessMap(debitSuccessMap);
		}

		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();

	}
	@Override
	protected void onResume() {
		super.onResume();
		boolean login = BaseDroidApp.getInstanse().isLogin();
		onResumeFromLogin(login);
	}
	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.TwoTask;
	}
}
