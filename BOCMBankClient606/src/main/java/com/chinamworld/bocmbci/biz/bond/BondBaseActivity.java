package com.chinamworld.bocmbci.biz.bond;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Bond;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.bond.acct.CustomerAgreementActivity;
import com.chinamworld.bocmbci.biz.bond.allbond.AllBondListActivity;
import com.chinamworld.bocmbci.biz.bond.historytran.HistoryTransferListActivity;
import com.chinamworld.bocmbci.biz.bond.mybond.MyBondListActivity;
import com.chinamworld.bocmbci.biz.invest.activity.InvesAgreeActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

/**
 * 债券UI基类
 * 
 * @author panwe
 * 
 */
public class BondBaseActivity extends BaseActivity {

	/** 内容布局 */
	protected LinearLayout layoutContent;
	/** 右上角按钮 */
	protected Button btnRight;
	/** 返回按钮 */
	protected View btnBack;
	/** 右侧按钮点击事件 */
	private OnClickListener rightBtnClick;
	/** 开通投资理财标识 */
	public boolean isOpenInvestmentManage = false;
	/** 债券开户标识 */
	public boolean isOpenBondActt = false;
	/** 登记标识 */
	public boolean isBondAcctSetup = false;
	private final int BOND_LIST_BOND = 0;
	private final int BOND_MY_BOND = 1;
	private final int BOND_HISTORY_TRAN = 2;
	/** intent传值 */
	public final String BONDACCT = "bondacct";
	public final String POSITION = "position";
	public final String BANKACCTID = "bankacctid";
	public final String BANKACCTNUM = "bankacctnum";
	public final String ISOPEN = "isopen";
	public final String ISSUCCESS = "issuccess";
	public final String TRANAMOUNT = "tranmount";
	public final String TRANPRICE = "tranprice";
	public final String AMOUNT = "amount";
	public final String SEQ = "seq";
	public final String TRANTYPE = "trantype";
	public final String ISCANCEACCT = "iscanceacct";
	public final String ISBUY = "isbuy";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_606_layout);
		initLeftSideList(this, LocalData.bondLeftListData);
		initPulldownBtn();
		initFootMenu();
		init();
	}

	private void init() {
		layoutContent = (LinearLayout) this.findViewById(R.id.sliding_body);
		btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		btnBack =  findViewById(R.id.ib_back);
		btnRight.setOnClickListener(backMain);
		btnBack.setOnClickListener(backClick);
	}

	/**
	 * 在slidingbody中引入自己布局文件
	 * 
	 * @param v
	 */
	protected void addView(View v) {
		layoutContent.removeAllViews();
		layoutContent.addView(v);
	}

	/** 返回事件 */
	public OnClickListener backClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	};

	/** 弹窗任务关闭 */
	public OnClickListener coloseBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			BondDataCenter.getInstance().finshActivity();
			finish();
		}
	};

	/**
	 * 设置右侧按钮文字
	 * 
	 * @param title
	 */
	public void setText(String title) {
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

	/** 返回主页面 */
	public OnClickListener backMain = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			BondDataCenter.getInstance().clearAllData();
			ActivityTaskManager.getInstance().removeAllSecondActivity();
		}
	};

	/**
	 * 右上角按钮clikListener
	 * 
	 * @param rightBtnClick
	 */
	public void setRightBtnClick(OnClickListener rightBtnClick) {
		this.rightBtnClick = rightBtnClick;
	}

	/** 数据返回异常 **/
	public OnClickListener errorClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			BaseDroidApp.getInstanse().dismissErrorDialog();
			finish();
		}
	};

	/** 隐藏底部tab */
	public void setBottomTabGone() {
		View menuPopwindow = (View) findViewById(R.id.menu_popwindow);
		menuPopwindow.setVisibility(View.GONE);
	}

	/**** 左侧菜单点击事件 **/
	@Override
	protected boolean selectedMenuItemHandler(Activity context, ImageAndText menuItem) {
		super.selectedMenuItemHandler(context, menuItem);
		ActivityTaskManager.getInstance().removeAllSecondActivity();
		Intent it = new Intent();
		String menuId = menuItem.MenuID;
		if(menuId.equals("bond_1")){// 债券行情
			it.setClass(this, AllBondListActivity.class);
		}
		else 	if(menuId.equals("bond_2")){
			// 我的债券
			it.setClass(this, MyBondListActivity.class);
		}
		else 	if(menuId.equals("bond_3")){
			// 交易查询
			it.setClass(this, HistoryTransferListActivity.class);
		}
		context.startActivity(it);
	   return true;
		
		
//		ActivityTaskManager.getInstance().removeAllSecondActivity();
//		Intent it = new Intent();
//		switch (clickIndex) {
//		case BOND_LIST_BOND:
//			// 债券行情
//			it.setClass(this, AllBondListActivity.class);
//			break;
//		case BOND_MY_BOND:
//			// 我的债券
//			it.setClass(this, MyBondListActivity.class);
//			break;
//		case BOND_HISTORY_TRAN:
//			// 交易查询
//			it.setClass(this, HistoryTransferListActivity.class);
//			break;
//		}
//		startActivity(it);
	}

	/**
	 * 显示文本
	 * 
	 * @param text
	 */
	public String commSetText(String text) {
		if (StringUtil.isNull(text)) {
			return "-";
		} else {
			return text;
		}
	}

	/**
	 * 将String转int
	 * 
	 * @param str
	 * @return
	 */
	public String strToint(String str) {
		if (StringUtil.isNull(str)) {
			return "-";
		} else {
			return String.valueOf(Integer.parseInt(str));
		}
	}

	/**
	 * 请求客户信息
	 * 
	 * @param bondAccNum
	 * @param bankAccId
	 */
	public void requestCustomerInfo(String bondAccNum, String bankAccId) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bond.METHOD_GETCUSTMOERINFO);
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put(Bond.BOND_RESULT_BONDACC, bondAccNum);
		parms.put(Bond.BANKACCT_ID, bankAccId);
		biiRequestBody.setParams(parms);
		HttpManager.requestBii(biiRequestBody, this, "customsInfoCallBack");
	}

	/** 客户信息返回处理 */
	public void customsInfoCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		BaseHttpEngine.dissMissProgressDialog();
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		BondDataCenter.getInstance().setCustomInfoMap(result);
	}

	// 获取账户列表 --公共接口
	public void requestBankAcctList() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.QRY_ALL_BANK_ACCOUNT);
		List<String> acctype = new ArrayList<String>();
		acctype.add(BondDataCenter.accountTypeList.get(0));
		acctype.add(BondDataCenter.accountTypeList.get(3));
		acctype.add(BondDataCenter.accountTypeList.get(11));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Comm.ACCOUNT_TYPE, acctype);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "bankAccListCallBack");
	}

	/**
	 * 账户列表返回处理
	 * 
	 * @param resultObj
	 */
	public void bankAccListCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> cardList = (List<Map<String, Object>>) (biiResponseBody
				.getResult());
		if (cardList == null || cardList.size() == 0) {
			return;
		}
		// 临时存储账户列表
		BondDataCenter.getInstance().setBankAccList(cardList);
	}

	/**
	 * 判断是否开通投资理财服务
	 */
	public void requestPsnInvestmentManageIsOpen() {
		BaseHttpEngine.showProgressDialogCanGoBack();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_API);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnInvestmentManageIsOpenCallback");
	}

	/**
	 * 判断是否开通投资理财服务---回调
	 * 
	 * @param resultObj
	 */
	public void requestPsnInvestmentManageIsOpenCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String isopen = String.valueOf(biiResponseBody.getResult());
		if (StringUtil.isNull(isopen)) {
			BiiHttpEngine.dissMissProgressDialog();
			return;
		}
		isOpenInvestmentManage = Boolean.valueOf(isopen);
		// 402
		if (Bond.isMorefunction) {
			if (isOpenInvestmentManage) {
				requestConversationId();
			} else {
				initViewDialog();
			}
		} else {
			if (isOpenInvestmentManage) {
				requestAccInfo();
			} else {
				initViewDialog();
			}
		}
	}

	/**
	 * 请求conversationId 出来登录之外的conversationId
	 */
	private void requestConversationId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.CONVERSATION_ID_API);
		HttpManager.requestBii(biiRequestBody, this,"requestConversationIdCallBack");		
	}

	/**
	 * 请求CommconversationId返回
	 * 
	 * @param resultObj
	 */
	public void requestConversationIdCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String commConversationId = (String) biiResponseBody.getResult();
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.CONVERSATION_ID, commConversationId);
		requestCustomerInfo();
	}

	/*** 获取账户信息 */
	public void requestAccInfo() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bond.METHOD_BONDACCINFO);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(Bond.INVTTPE, Bond.BOND_INVTTPE);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "accInfoCallBack");
	}

	/** 账户信息返回处理 */
	public void accInfoCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		BondDataCenter.getInstance().setAccMap(result);
		if (Bond.isMorefunction) {
			if (result == null) {
				isBondAcctSetup = false;
				initViewDialog();
			} else {
				isBondAcctSetup = true;
				requestSystemDateTime();
			}
		} else {
			if (result == null) {
				BaseDroidApp.getInstanse().showMessageDialog(
						this.getString(R.string.bond_no_acc_tip), errorClick);
				BaseHttpEngine.dissMissProgressDialog();
				return;
			}
			requestSystemDateTime();
		}
	}

	// 判断是否开户
	public void requestCustomerInfo() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Bond.METHOD_CUSTOMERINFO);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, "customerInfoCallBack");
	}

	/** 是否开户返回 */
	public void customerInfoCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			isOpenBondActt = false;
			initViewDialog();
		} else {
			isOpenBondActt = true;
			// 判断是否登记
			requestAccInfo();
		}
	}

	// 弹出开通理财提示框
	private void initViewDialog() {
		BaseHttpEngine.dissMissProgressDialog();
		View contentView = LayoutInflater.from(this).inflate(
				R.layout.bond_notify, null);

		ImageView taskPopCloseButton = (ImageView) contentView
				.findViewById(R.id.top_right_close);

		ImageView invsetImage = (ImageView) contentView
				.findViewById(R.id.invset_image);

		View invsetView = contentView
				.findViewById(R.id.bocinvt_money_button_show);
		View bondAcctView = contentView
				.findViewById(R.id.bocinvt_InvtEvaluationInit_button_show);

		TextView tvAcct = (TextView) contentView.findViewById(R.id.openAcct);
		TextView tvInVset = (TextView) contentView
				.findViewById(R.id.isopenInvset);

		// 退出
		taskPopCloseButton.setOnClickListener(closeClick);
		// 开通投资理财服务
		invsetView.setOnClickListener(manageOpenClick);
		// 开户/登记
		bondAcctView.setOnClickListener(bondAcctClick);
		// 401新功能
		if (Bond.isMorefunction) {
			if (!isOpenInvestmentManage) {
				bondAcctView.setVisibility(View.GONE);
			} else {
				invsetImage.setVisibility(View.INVISIBLE);
				tvInVset.setText(this.getString(R.string.boc_open));
				invsetView.setClickable(false);
				bondAcctView.setVisibility(View.VISIBLE);
				if (isOpenBondActt) {
					tvAcct.setText(R.string.boc_nobinding);
				}
			}
		} else {
			bondAcctView.setVisibility(View.GONE);
		}
		BaseDroidApp.getInstanse().showAccountMessageDialog(contentView);
	}

	/** 开通投资理财监听事件 */
	OnClickListener manageOpenClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 跳转到投资理财服务协议页面
			Intent gotoIntent = new Intent(BondBaseActivity.this,
					InvesAgreeActivity.class);
			startActivityForResult(gotoIntent,
					ConstantGloble.ACTIVITY_RESULT_CODE);
			overridePendingTransition(R.anim.push_up_in, R.anim.no_animation);
		}
	};

	/** 开户监听事件 */
	OnClickListener bondAcctClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BondDataCenter.getInstance().setResetup(false);
			if (isOpenBondActt) {
				// 登记--请求资金账号列表
				requestBankAcctList();
			} else {
				// 开户
				Intent gotoIntent = new Intent(BondBaseActivity.this,
						CustomerAgreementActivity.class);
				boolean isBuy = BondDataCenter.getInstance().isBuy();
				if (isBuy) {
					gotoIntent.putExtra(ISBUY, isBuy);
					startActivityForResult(gotoIntent,
							BondConstant.BOND_REQUEST_OPEN_ACCT_CODE);
				} else {
					startActivityForResult(gotoIntent,
							ConstantGloble.ACTIVITY_RESULT_CODE);
				}
				overridePendingTransition(R.anim.push_up_in,
						R.anim.no_animation);
			}
		}
	};

	private OnClickListener closeClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();
			ActivityTaskManager.getInstance().removeAllSecondActivity();
			finish();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case ConstantGloble.ACTIVITY_RESULT_CODE:
			switch (resultCode) {
			case RESULT_OK:
				// 402新功能
				if (Bond.isMorefunction) {
					isOpenInvestmentManage = true;
					// 判断是否有托管账户
					BaseHttpEngine.showProgressDialogCanGoBack();
					// 获取会话id
					requestConversationId();
					// 开户成功返回
				} else {
					// 请求账户信息
					BaseHttpEngine.showProgressDialogCanGoBack();
					requestAccInfo();
				}

				break;
			case RESULT_CANCELED:
				if (requestCode == ConstantGloble.ACTIVITY_RESULT_CODE)
					initViewDialog();
				break;
			}
			break;

		default:
			break;
		}
	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		return ActivityTaskType.TwoTask;
	}
}
