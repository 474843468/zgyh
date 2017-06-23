package com.chinamworld.bocmbci.biz.thridmanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Third;
import com.chinamworld.bocmbci.biz.SecondMainActivity;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.biz.invest.activity.InvesAgreeActivity;
import com.chinamworld.bocmbci.biz.thridmanage.cecuritytrade.CecurityTradeActivity;
import com.chinamworld.bocmbci.biz.thridmanage.historytrade.HistoryTradeActivity;
import com.chinamworld.bocmbci.biz.thridmanage.openacct.OpenAccActivity;
import com.chinamworld.bocmbci.biz.thridmanage.platforacct.PlatforAcctActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.GetPhoneInfo;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.UsbKeyText;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

/**
 * 第三方存管UI基类
 * 
 * @author panwe
 * 
 */
public class ThirdManagerBaseActivity extends BaseActivity {

	private static final int REQUEST_OPENINVESTMENT_CODE = 102;
	/** 返回主页按钮 */
	protected View btnBack;
	/** 右上角按钮 */
	protected Button btnRight;
	/** 右侧按钮点击事件 */
	private OnClickListener rightBtnClick;
	/** 内容布局 */
	protected LinearLayout layoutContent;
	/** 加密控件 **/
	// public SipBox sipBox;
	/** 查询页面使用的 PopupWindow */
	public PopupWindow popupWindow;
	public LinearLayout upLayout;

	/** 更多 */
	public View mFootView;
	public ProgressBar proBar;
	public TextView tvGetMore;
	public boolean isAddfootView = false;
	public boolean isFirstQuery = true;
	/** 自助关联上送服务码 */
	protected String relevanceServiceId = "PB010";
	/** 优汇通专户 */
	public static final String YOUHUITONGZH = "199";
	protected static final String MEDICALACC = "医保账户";
	/**中银E盾*/
	public UsbKeyText usbKeytext;

	/** 日志标识 */
	// private static final String TAG = "ThirdManagerBaseActivity";
	// /** 退出动画 */
	// public Animation animation_out;
	// /** 进入动画 */
	// public Animation animation_in;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_606_layout);
		// 初始化弹窗按钮
		initPulldownBtn();
		// 初始化左边菜单
		initLeftSideList(this, LocalData.thirdManangerLeftListData);
		// 初始化底部菜单栏
		initFootMenu();
		init();
	}

	private void init() {
		btnBack = (View) findViewById(R.id.ib_back);
		btnBack.setOnClickListener(backClick);
		btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		layoutContent = (LinearLayout) this.findViewById(R.id.sliding_body);

		// animation_out = AnimationUtils.loadAnimation(this,
		// R.anim.query_exit);
		// animation_in = AnimationUtils.loadAnimation(this,
		// R.anim.query_enter);

		mFootView = LayoutInflater.from(this).inflate(R.layout.third_listview_getmore, null);
		proBar = (ProgressBar) mFootView.findViewById(R.id.progressBar);
		tvGetMore = (TextView) mFootView.findViewById(R.id.tv_get_more);

		btnRight.setVisibility(View.GONE);
	}

	/** 返回主页Listener */
	private OnClickListener backClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			titleBackClick();
		}
	};

	/***
	 * 标题栏返回按钮事件
	 */
	protected void titleBackClick() {
		finish();
	}

	/** 关闭提示框 */
	public OnClickListener errorClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};

	/**
	 * 在slidingbody中引入自己布局文件
	 * 
	 * @param resource
	 * @return 引入布局
	 */
	protected void addView(View v) {
		layoutContent.addView(v);
	}

	/**
	 * 设置右侧按钮文字
	 * 
	 * @param title
	 */
	public void setTitleRightText(String title) {
		btnRight.setVisibility(View.VISIBLE);
		btnRight.setText(title);
		btnRight.setTextColor(Color.WHITE);
		btnRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (rightBtnClick != null) {
					rightBtnClick.onClick(v);
				}
			}
		});
	}

	public void goMainActivity() {
//		ActivityTaskManager.getInstance().removeAllSecondActivity();
//		Intent it = new Intent(this, SecondMainActivity.class);
//		startActivity(it);
//		finish();
		goToMainActivity();
	}

	/**
	 * 右上角按钮clikListener
	 * 
	 * @param rightBtnClick
	 */
	public void setRightBtnClick(OnClickListener rightBtnClick) {
		this.rightBtnClick = rightBtnClick;
	}

	/**** 左侧菜单点击事件 **/
	@Override
	protected boolean selectedMenuItemHandler(Activity context, ImageAndText menuItem) {
		super.selectedMenuItemHandler(context, menuItem);
		ActivityTaskManager.getInstance().removeAllSecondActivity();
		Intent it = new Intent();
		String menuId = menuItem.MenuID;
		if(menuId.equals("thirdMananger_1")){
			// 第三方存管
			it.setClass(this, CecurityTradeActivity.class);
		}
		else if(menuId.equals("thirdMananger_2")){
			// 历史交易
			it.setClass(this, HistoryTradeActivity.class);
		}
		else if(menuId.equals("thirdMananger_3")){
			// 台账
			it.setClass(this, PlatforAcctActivity.class);
		}
		else if(menuId.equals("thirdMananger_4")){
			// 开户
			it.setClass(this, OpenAccActivity.class);
		}
		context.startActivity(it);
		return true;
		
//		ActivityTaskManager.getInstance().removeAllSecondActivity();
//		Intent it = new Intent();
//		switch (clickIndex) {
//		case 0:
//			// 第三方存管
//			it.setClass(this, CecurityTradeActivity.class);
//			break;
//		case 1:
//			// 历史交易
//			it.setClass(this, HistoryTradeActivity.class);
//			break;
//		case 2:
//			// 台账
//			it.setClass(this, PlatforAcctActivity.class);
//			break;
//		case 3:
//			// 开户
//			it.setClass(this, OpenAccActivity.class);
//			break;
//		}
//		startActivity(it);
	}

	/**
	 * 获取账户列表 --公共接口
	 * 
	 * @param acctype
	 */
	public void getAllBankList(List<String> acctype/*, boolean isBack*/) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.QRY_ALL_BANK_ACCOUNT);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Comm.ACCOUNT_TYPE, acctype);
		biiRequestBody.setParams(params);
		// 通讯开始,展示通讯框
//		if (isBack) {
//			BaseHttpEngine.showProgressDialogCanGoBack();
//		} else {
//			BaseHttpEngine.showProgressDialog();
//		}
		HttpManager.requestBii(biiRequestBody, this, "allBankAccListCallBack");
	}

	/**
	 * 账户列表返回处理
	 * 
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void allBankAccListCallBack(Object resultObj) {
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> cardList = (List<Map<String, Object>>) (biiResponseBody.getResult());
		if (cardList == null) {
			return;
		}
		// 临时存储账户列表
		ThirdDataCenter.getInstance().setBankAccountList(cardList);
	}

	/**
	 * 根据银行账户获取证券资金账户 ---公共接口
	 * 
	 * @param accNum
	 */
	public void requestCecurityForBankAcc(String accNum) {
		// 通讯开始,展示通讯框
		BaseHttpEngine.showProgressDialogCanGoBack();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Third.METHOD_CECURITYTRADE_CECACCLIST);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Third.CECURITYTRADE_BANKACCNUM, accNum);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "cecurityListCallBack");
	}

	
	/**
	 * 证券保证金列表返回
	 * 
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void cecurityListCallBack(Object resultObj) {
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> cecurityList = (List<Map<String, Object>>) (biiResponseBody.getResult());
		if (cecurityList == null) {
			return;
		}
		ThirdDataCenter.getInstance().setCecAccountList(cecurityList);
	}

	// 初始化sinper数据
	public List<String> initSinnerData() {
		List<Map<String, Object>> cecList = ThirdDataCenter.getInstance().getCecAccountList();
		List<String> mList = new ArrayList<String>();
		mList.add(getText(R.string.third_opendacc_com_tip).toString());
		for (int i = 0; i < cecList.size(); i++) {
			mList.add(cecList.get(i).get(Third.CECURITYTRADE_COMPANY).toString() 
					/* + "/" + cecList . get ( i ) . get ( Third . CECURITYTRADE_BANKACCNUM_RE )*/);
			// mList.add((String)cecList.get(i).get(Third.CECURITYTRADE_BANKACCNUM_RE));
		}
		return mList;
	}

	// -------------------------------------------------------------------------------

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_OPENINVESTMENT_CODE) {
			if (resultCode == RESULT_OK) {
				BaseDroidApp.getInstanse().dismissMessageDialogFore();
				serviceOpenState();
			}
		}
	}

	/**
	 * 判断是否开通投资理财服务
	 */
	private void requestPsnInvestmentManageIsOpen() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "requestIsOpenCallback");
	}

	/** 判断是否开通投资理财服务 */

	public void requestIsOpenCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 得到result
		String isOpenOr = (String) biiResponseBody.getResult();
		// isOpen
		checkServiceStateResult(Boolean.valueOf(isOpenOr));
	}

	private void checkServiceStateResult(boolean isopen) {
		if (isopen) {
			serviceOpenState();
		} else {
			BiiHttpEngine.dissMissProgressDialog();
			showOpenServiceDialog();
		}
	}

	private void showOpenServiceDialog() {
		View popupView = LayoutInflater.from(this).inflate(R.layout.third_open_service_dialog, null);
		View taskPopCloseButton = popupView.findViewById(R.id.top_right_close);
		// moneyButtonView:理财服务按钮
		View investmentView = popupView.findViewById(R.id.ll_investment);

		// 开通投资理财
		investmentView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 跳转到投资理财服务协议页面
				Intent gotoIntent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(), InvesAgreeActivity.class);
				startActivityForResult(gotoIntent, REQUEST_OPENINVESTMENT_CODE);

			}
		});
		// 右上角的关闭按钮
		taskPopCloseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 强制关闭任务提示框，返回到九宫格
				BaseHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().dismissMessageDialogFore();
//				ActivityTaskManager.getInstance().removeAllSecondActivity();
//				Intent intent = new Intent(ThirdManagerBaseActivity.this, SecondMainActivity.class);
//				startActivity(intent);
//				finish();
				goToMainActivity();

			}
		});
		BaseDroidApp.getInstanse().showForexMessageDialog(popupView);

	}

	public void checkServiceState(ServiceType services) {
		if (services == ServiceType.InvestmentService) {
			requestPsnInvestmentManageIsOpen();
		}
	}

	/**
	 * 服务打开状态
	 */
	public void serviceOpenState() {

	}

	public void gonerightBtn() {
		btnRight.setVisibility(View.GONE);
	}
	
	/**
	 * 账户类型
	 */
	@SuppressWarnings("serial")
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
	
//	public void showTimeOutDialog(BiiError biiError) {
//		BaseDroidApp.getInstanse().showMessageDialog(biiError.getMessage(),
//				new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						BaseDroidApp.getInstanse().dismissErrorDialog();
//						ActivityTaskManager.getInstance().removeAllActivity();
//						Intent intent = new Intent();
//						intent.setClass(ThirdManagerBaseActivity.this,
//								LoginActivity.class);
//						startActivityForResult(intent,
//								ConstantGloble.ACTIVITY_RESULT_CODE);
//					}
//				});
//	}

	/** 是否有电子现金账户 */
	public static final List<String> isHaveECashAccountList = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;

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
	@SuppressWarnings("unchecked")
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
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.TwoTask;
	}
}
