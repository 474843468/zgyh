package com.chinamworld.bocmbci.biz.crcd.mycrcd.virtualservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdAccBaseActivity;
import com.chinamworld.bocmbci.biz.crcd.adapter.CrcdVirtualCardAdapter;
import com.chinamworld.bocmbci.biz.crcd.dialogActivity.CrcdVirtualDialogActivity;
import com.chinamworld.bocmbci.biz.crcd.view.InflaterViewDialog;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.BaseSwipeListViewListener;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.bocmbci.widget.SwipeListView;

/**
 * 我的虚拟卡-------虚拟银行卡列表 -------选择虚拟银行卡
 * 
 * @author huangyuchao
 * 
 */
public class VirtualBCListActivity extends CrcdAccBaseActivity {
	private static final String TAG = "VirtualBCListActivity";
	private View view = null;

	Button sureButton;

	/** 信用卡列表 */
	private SwipeListView myListView;

	CrcdVirtualCardAdapter adapter;

	String cardType;
	String cardNumber;
	private int conid;
	/** 虚拟银行卡查询结果 */
	static List<Map<String, Object>> virCardList;

	/** 取消关联点击 */
	private boolean click = true;
	static String currencyCode;
	static String nickName;
	private int accNum = 0;
	TextView tv_service_title;
	/** 虚拟银行卡查询结果returnMap将值赋给map */
	static Map<String, Object> map = new HashMap<String, Object>();

	View diallogView;
	/** 用户选择的虚拟银行卡 */
	public static Map<String, Object> virCardItem = new HashMap<String, Object>();

	/** 添加虚拟卡申请视图 */
	private View addVirtualView;

	int maxNum;
	int currentIndex = 0;
	int pageSize = 10;
	private boolean isRefresh = true;
	/** 是否注销虚拟卡 --点击注销"确定"，isCancel=true */
	private boolean isCancel = false;
	/** 虚拟银行卡卡号 */
	static String virtualCardNo;
	/** 账户户名 */
	private String accountName = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "OnCreate");
		// 包含向右滑动的listview 目的是屏蔽左侧菜单的滑动事件
		setContainsSwipeListView(true);
		cardType = MyVirtualBCListActivity.strAccountType;
		cardNumber = StringUtil.getForSixForString(String.valueOf(MyVirtualBCListActivity.accountNumber));
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_virtual_myxunicard));

		if (view == null) {
			view = addView(R.layout.crcd_virtual_list);
		}
		// 右上角按钮赋值
		setText(this.getString(R.string.mycrcd_step_zhuxiao));
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		accountName = getIntent().getStringExtra(Crcd.CRCD_ACCOUNTNAME_RES);
		init();
	}

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (click) {
				rightButtonIsFinish();
			} else {
				// 点击注销按钮，进行注销操作
				rightButtonIsZX();
			}
		}
	};

	/** 显示完成按钮 */
	private void rightButtonIsFinish() {
		conid = 2;
		click = false;
		if (virCardList == null && virCardList.size() == 0) {
			return;
		}
		setText(VirtualBCListActivity.this.getString(R.string.finish));
		adapter = new CrcdVirtualCardAdapter(VirtualBCListActivity.this, virCardList, map, conid);
		myListView.setAdapter(adapter);
		adapter.setOnItemCanceListener(onCancelListener);
		myListView.setAllPositionClickable(click);
	}

	/** 显示注销按钮---注销事件 */
	private void rightButtonIsZX() {
		conid = 1;
		// 右上角按钮赋值
		setText(VirtualBCListActivity.this.getString(R.string.mycrcd_step_zhuxiao));
		click = true;
		adapter = new CrcdVirtualCardAdapter(VirtualBCListActivity.this, virCardList, map, conid);
		myListView.setAdapter(adapter);
		// 虚拟信用卡详情
		adapter.setOncrcdDetailListener(oncrcdDetailListener);
		adapter.setOntransDetailListener(ontransDetailListener);
		myListView.setAllPositionClickable(click);
	}

	/** 注销虚拟信用卡----监听事件 */
	OnItemClickListener onCancelListener = new OnItemClickListener() {

		public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
			virCardItem = virCardList.get(position);

			// 弹出对话框,是否取消
			BaseDroidApp.getInstanse().showErrorDialog(
					VirtualBCListActivity.this.getString(R.string.mycrcd_confirm_zhuxiao_xunicard), R.string.cancle,
					R.string.confirm, new OnClickListener() {
						@Override
						public void onClick(View v) {
							switch (Integer.parseInt(v.getTag() + "")) {
							case CustomDialog.TAG_SURE:
								// 确定取消关联
								BaseDroidApp.getInstanse().dismissErrorDialog();

								requestCommConversationId();
								BaseHttpEngine.showProgressDialog();
								break;
							case CustomDialog.TAG_CANCLE:
								// 取消操作
								BaseDroidApp.getInstanse().dismissErrorDialog();
								break;
							}
						}
					});

		};
	};

	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		// 获取TokenId
		requestPSNGetTokenId(String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID)));
	}

	/**
	 * 登录后的tokenId 请求返回
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		// 虚拟银行卡注销
		psnCrcdVirtualCardCancel();
	}

	/** 虚拟银行卡注销 */
	private void psnCrcdVirtualCardCancel() {
		BiiHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDVIRTUALCARDCANCEL);
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);// 获取conversationId

		Map<String, String> map = new HashMap<String, String>();
		map.put(Crcd.CRCD_ACCOUNTID_RES, MyVirtualBCListActivity.accountId);//
		map.put(Crcd.CRCD_VIRTUALCARDNO, String.valueOf(virCardItem.get(Crcd.CRCD_VIRTUALCARDNO)));
		String startDate = String.valueOf(virCardItem.get(Crcd.CRCD_STARTDATE_REQ));
		String ss = QueryDateUtils.getCurrentDates(startDate);
		map.put(Crcd.CRCD_VIRCARDSTARTDATE, ss);
		String endDate = String.valueOf(virCardItem.get(Crcd.CRCD_ENDDATE_REQ));
		map.put(Crcd.CRCD_VIRCARDENDDATE, QueryDateUtils.getCurrentDates(endDate));
		map.put(Crcd.CRCD_SINGLEEMT, String.valueOf(virCardItem.get(Crcd.CRCD_SINGLEEMT)));
		map.put(Crcd.CRCD_TOTALEAMT, String.valueOf(virCardItem.get(Crcd.CRCD_TOTALEAMT)));
		map.put(Crcd.CRCD_TOKEN,
				String.valueOf(BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID)));
		map.put(Crcd.CRCD_LASTUPDATEUSER, String.valueOf(virCardItem.get(Crcd.CRCD_LASTUPDATEUSER)));
		map.put(Crcd.CRCD_LASTUPDATES, String.valueOf(virCardItem.get(Crcd.CRCD_LASTUPDATES)));
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdVirtualCardCancelCallBack");
	}

	/** 注销虚拟信用卡返回结果 */
	Map<String, Object> returnMap;

	/** 注销虚拟信用卡------回调 */
	public void psnCrcdVirtualCardCancelCallBack(Object resultObj) {
		// 通讯结束,关闭通讯框
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		returnMap = (Map<String, Object>) biiResponseBody.getResult();

		// 右上角按钮赋值
		CustomDialog.toastShow(this, this.getString(R.string.mycrcd_zhuxiao_xunicard_success));
		isRefresh = true;
		currentIndex = 0;
		isCancel = true;
		if (virCardList != null && !virCardList.isEmpty()) {
			virCardList.clear();
		}
		// 查询虚拟银行卡数据
		psnCrcdVirtualCardQuery(String.valueOf(isRefresh));

	}

	/** 弹出-----虚拟银行卡详情 */
	OnItemClickListener oncrcdDetailListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			InflaterViewDialog dialog = new InflaterViewDialog(VirtualBCListActivity.this);
			if (null != virCardList) {
				virCardItem = virCardList.get(position);
				virtualCardNo = String.valueOf(virCardItem.get(Crcd.CRCD_VIRTUALCARDNO));
				Intent it = new Intent(VirtualBCListActivity.this, CrcdVirtualDialogActivity.class);
				startActivity(it);
			}

		}
	};

	String[] selectors;

	OnClickListener guanlianClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			PopupWindowUtils.getInstance().setshowMoreChooseDownListener(VirtualBCListActivity.this, v, selectors,
					popListener);

		}
	};

	OnClickListener popListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Integer tag = (Integer) v.getTag();
			switch (tag) {
			case 0:
				// 展示短信的dialog
				showSmsDialog();
				break;
			case 1:
				// 信用卡关联网银
				Intent it = new Intent(VirtualBCListActivity.this, MyVirtualGuanLianConfirmActivity.class);
				startActivity(it);
				break;
			}

		}
	};

	/** 初始化界面 */
	private void init() {

		selectors = new String[] { this.getString(R.string.mycrcd_virtualcard_sms_tongzhi),
				this.getString(R.string.mycrcd_creditcard_guanlian) };
		if (virCardList != null && !virCardList.isEmpty()) {
			virCardList.clear();
		}

		virCardList = MyVirtualBCListActivity.virCardList;

		tv_service_title = (TextView) findViewById(R.id.tv_service_title);
		tv_service_title.setText(cardType + cardNumber + this.getString(R.string.mycrcd_xiashu_xuni_list));

		map = MyVirtualBCListActivity.returnMap;
		myListView = (SwipeListView) view.findViewById(R.id.crcd_mycrcdlist);

		addVirtualView = LayoutInflater.from(this).inflate(R.layout.list_more, null);
		maxNum = MyVirtualBCListActivity.maxNum;
		Button btn_more = (Button) addVirtualView.findViewById(R.id.btn_more);
		// 加载更多
		btn_more.setOnClickListener(goMoreClickListener);
		myListView.addFooterView(addVirtualView);
		// 设置列表底部视图
		if (maxNum > pageSize) {
			addVirtualView.setVisibility(View.VISIBLE);
		} else {
			addVirtualView.setVisibility(View.INVISIBLE);
		}

		adapter = new CrcdVirtualCardAdapter(this, virCardList, map, 1);
		myListView.setAdapter(adapter);
		adapter.setOncrcdDetailListener(oncrcdDetailListener);
		adapter.setOntransDetailListener(ontransDetailListener);

		myListView.setLastPositionClickable(false);
		myListView.setAllPositionClickable(true);
		myListView.setSwipeListViewListener(new BaseSwipeListViewListener() {
			@Override
			public void onOpened(int position, boolean toRight) {

			}

			@Override
			public void onClosed(int position, boolean fromRight) {
			}

			@Override
			public void onListChanged() {
			}

			@Override
			public void onMove(int position, float x) {
			}

			@Override
			public void onStartOpen(int position, int action, boolean right) {
				// 虚拟卡明细查询
				LogGloble.d("swipe", String.format("onStartOpen %d - action %d", position, action));
				Intent it = new Intent(VirtualBCListActivity.this, MyVivtualQueryActivity.class);
				it.putExtra(ConstantGloble.ACC_POSITION, position);
				virCardItem = virCardList.get(position);
				virtualCardNo = String.valueOf(virCardItem.get(Crcd.CRCD_VIRTUALCARDNO));
				it.putExtra(Crcd.CRCD_VIRTUALCARDNO, virtualCardNo);
				it.putExtra(Crcd.CRCD_ACCOUNTNAME_RES, accountName);
				it.putExtra(ConstantGloble.FALSE, true);
				BaseDroidApp.getInstanse().getBizDataMap()
						.put(ConstantGloble.CRCD_VIRCRCDLIST, virCardList);
				startActivity(it);

			}

			@Override
			public void onStartClose(int position, boolean right) {
				LogGloble.d("swipe", String.format("onStartClose %d", position));
			}

			@Override
			public void onClickFrontView(int position) {
				LogGloble.d("swipe", String.format("onClickFrontView %d", position));
				virCardItem = virCardList.get(position);
				virtualCardNo = String.valueOf(virCardItem.get(Crcd.CRCD_VIRTUALCARDNO));
				if (click) {
					Intent it = new Intent(VirtualBCListActivity.this, CrcdVirtualDialogActivity.class);
					startActivity(it);
				}

			}

			@Override
			public void onClickBackView(int position) {
				LogGloble.d("swipe", String.format("onClickBackView %d", position));
			}

			@Override
			public void onDismiss(int[] reverseSortedPositions) {
				adapter.notifyDataSetChanged();
			}
		});

	}

	OnClickListener goMoreClickListener = new OnClickListener() {
		public void onClick(View v) {
			currentIndex += pageSize;
			isRefresh = false;
			// 查询信用卡下的虚拟卡
			BaseHttpEngine.showProgressDialog();
			psnCrcdVirtualCardQuery(String.valueOf(isRefresh));
		};
	};

	private void psnCrcdVirtualCardQuery(String isRefresh) {
		// 通讯开始,展示通讯框
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDVIRTUALCARDQUERY);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Crcd.CRCD_ACCOUNTID_RES, MyVirtualBCListActivity.accountId);
		params.put(Crcd.CRCD_CURRENTINDEX, String.valueOf(currentIndex));
		params.put(Crcd.CRCD_PAGESIZE, String.valueOf(pageSize));
		params.put(Crcd.CRCD_REFRESH, isRefresh);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdVirtualCardQueryCallBack");
	}

	public void psnCrcdVirtualCardQueryCallBack(Object returnObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse response = (BiiResponse) returnObj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		returnMap = (Map<String, Object>) body.getResult();
		List<Map<String, Object>> returnList = (List<Map<String, Object>>) returnMap.get(Crcd.CRCD_VIRCARDLIST);
		for (Map<String, Object> map : returnList) {
			virCardList.add(map);
		}
		String recordNum = String.valueOf(returnMap.get(Crcd.CRCD_RECORDNUMBER));

		maxNum = Integer.valueOf(recordNum);

		if (isCancel) {
			isCancel = false;
			if (maxNum > pageSize) {
				// myListView.addFooterView(addVirtualView);
				addVirtualView.setVisibility(View.VISIBLE);
			}
			rightButtonIsZX();
		} else {
			if ((currentIndex + pageSize) >= maxNum) {
				addVirtualView.setVisibility(View.INVISIBLE);
				// myListView.removeFooterView(addVirtualView);
			}
			adapter.notifyDataSetChanged();
		}

	}

	/** 虚拟信用卡协议 */
	OnClickListener goVirtualClickListener = new OnClickListener() {
		public void onClick(View v) {

			startActivity(new Intent(VirtualBCListActivity.this, VirtualBCServiceReadActivity.class));
		};
	};
	/** 虚拟信用卡设定 */
	OnClickListener gotoSutepClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();

			Intent it = new Intent(VirtualBCListActivity.this, MyVirtualSetupActivity.class);
			startActivity(it);
		}
	};

	OnClickListener exitCick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BaseDroidApp.getInstanse().dismissMessageDialog();
		}
	};

	View smsView;
	OnClickListener gotoSmsClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			showSmsDialog();

		}
	};

	public void showSmsDialog() {
		Map<String, Object> returnMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA);

		String mobile = String.valueOf(returnMap.get(Crcd.CRCD_MOBILE));

		// 弹出对话框
		BaseDroidApp.getInstanse().createSmsDialog(
				getString(R.string.mycrcd_xunixinxi_mianfei_send_phone) + "\n"
						+ getString(R.string.mycrcd_yuliu_phone_code) + mobile, new OnClickListener() {

					@Override
					public void onClick(View v) {
						switch (Integer.parseInt(v.getTag() + "")) {
						case CustomDialog.TAG_SURE:
							// 发送短信
							psnCrcdVirtualCardSendMessage();
							break;
						case CustomDialog.TAG_CANCLE:
							BaseDroidApp.getInstanse().dismissErrorDialog();
						}
					}
				});

	}

	/** 进入到虚拟信用卡明细查询页面 */
	OnItemClickListener ontransDetailListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			virCardItem = virCardList.get(position);
			virtualCardNo = String.valueOf(virCardItem.get(Crcd.CRCD_VIRTUALCARDNO));

			Intent it = new Intent(VirtualBCListActivity.this,
					MyVivtualQueryActivity.class);
			it.putExtra(ConstantGloble.FALSE, true);
			it.putExtra(ConstantGloble.ACC_POSITION, position);
			it.putExtra(Crcd.CRCD_VIRTUALCARDNO, virtualCardNo);
			it.putExtra(Crcd.CRCD_ACCOUNTNAME_RES, accountName);
			startActivity(it);
		}
	};

	public void psnCrcdVirtualCardSendMessage() {
		BiiHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDVIRTUALCARDSENDMESSAGE);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Crcd.CRCD_ACCOUNTID_RES, MyVirtualBCListActivity.accountId);//
		map.put(Crcd.CRCD_VIRCARDNO, String.valueOf(virCardItem.get(Crcd.CRCD_VIRTUALCARDNO)));
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "psnCrcdVirtualCardSendMessageCallBack");
	}

	public void psnCrcdVirtualCardSendMessageCallBack(Object resultObj) {
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String result = (String) biiResponseBody.getResult();
		// BaseDroidApp.getInstanse().dismissMessageDialog();
		// BaseDroidApp.getInstanse().dismissErrorDialog();
		CustomDialog.toastShow(this, this.getString(R.string.mycrcd_sms_has_send_success));
		// 对列表内的数据进行刷新
		CrcdVirtualCardAdapter cvcAdapter = new CrcdVirtualCardAdapter(this, virCardList, map, 1);
		myListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

//	@Override
//	protected void onResume() {
//		super.onResume();
//	}

	@Override
	protected void onDestroy() {
		BaseHttpEngine.dissMissProgressDialog();
		BaseDroidApp.getInstanse().dismissMessageDialog();
		BaseDroidApp.getInstanse().dismissErrorDialog();
		super.onDestroy();
	}
}
