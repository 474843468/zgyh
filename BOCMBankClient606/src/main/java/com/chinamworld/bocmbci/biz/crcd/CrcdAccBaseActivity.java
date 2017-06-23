package com.chinamworld.bocmbci.biz.crcd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.acc.financeicaccount.FinanceMenuActivity;
import com.chinamworld.bocmbci.biz.acc.lossreport.AccDebitCardLossActivity;
import com.chinamworld.bocmbci.biz.acc.medical.MedicalMenuActivity;
import com.chinamworld.bocmbci.biz.acc.mybankaccount.AccManageActivity;
import com.chinamworld.bocmbci.biz.acc.relevanceaccount.AccInputRelevanceAccountActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.virtualservice.VirtualBCServiceMenuActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.widget.adapter.LeftSideListAdapter;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

/**
 * 账户管理中的虚拟卡
 * 
 * @author huangyuchao
 * 
 */
public class CrcdAccBaseActivity extends BaseActivity {

	/** 主视图布局 */
	private LinearLayout tabcontent;// 主Activity显示
	/** 右侧按钮点击事件 */
	private OnClickListener rightBtnClick;

	public static String currency = ConstantGloble.PRMS_CODE_RMB;
	protected static String strCurrency;

	/** 请求回来的账户列表信息 (我的信用卡) */
	public static List<Map<String, Object>> bankAccountList = new ArrayList<Map<String, Object>>();

	/** 请求回来的账户列表信息 (信用卡设定) */
	public List<Map<String, Object>> bankSetupList = new ArrayList<Map<String, Object>>();

	/** 当前账户 */
	public static Map<String, Object> currentBankList = new HashMap<String, Object>();;

	/** 二级菜单 */
	private LinearLayout slidingView;
	/** 展示按钮 */
	private Button btnShow;
	/** 隐藏按钮 */
	private Button btnHide;

	/** 左侧返回按钮 */
	protected Button back;
	/** 右上角按钮 */
	protected Button btn_right;

	/** 中银信用卡 */
	public static final String ZHONGYIN = "103";
	/** 长城信用卡 */
	public static final String GREATWALL = "104";
	/** 单外币信用卡 */
	public static final String SINGLEWAIBI = "107";

	/** 长城借记卡 */
	protected static final String GREATWALL_CREDIT = "119";
	/** 普通活期 */
	protected static final String COMMONHUOQI = "101";
	/** 活期一本通 */
	protected static final String HUOQIBENTONG = "188";

	/** 所有结欠均以人民币还款 */
	protected static final int ALL_REN = 0;
	/** 人民币与外币结欠分别以相应账户还款 */
	protected static final int ALL_FOREIGN = 1;
	/** 外币结欠以相应账户还款 */
	protected static final int ALL_RENANDFOREIGN = 2;
	ArrayList<Activity> actLists = new ArrayList<Activity>();

	// 分期服务码
	public String psnDividedsecurityId = "PB057C1";
	// 设置交易限额服务码
	public String psnSetupsecurityId = "PB057C2";
	// 消费服务设置服务码
	public String psnSersecurityId = "PB057C3";
	// 对账单服务码
	public String psnChecksecurityId = "PB057C4";

	// 信用卡激活服务码
	public String psnActivesecurityId = "PB060D1";
	// 信用卡挂失服务码
	public String psnGuashisecurityId = "PB060D2";

	// 虚拟卡服务码
	public String psnVirsericurityId = "PB059";

	private void removeAllEceptFirst() {
		for (int i = 0; i < actLists.size(); i++) {
			if (i != 0) {
				actLists.remove(i);
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		actLists.add(this);
		setContentView(R.layout.biz_activity_layout);

		tabcontent = (LinearLayout) findViewById(R.id.sliding_body);
		btn_right = (Button) findViewById(R.id.ib_top_right_btn);
		strCurrency = this.getString(R.string.mycrcd_renmibi);
		// 初始化弹窗按钮
		initPulldownBtn();
		// 初始化左边菜单
		// initAccLeftSideList(this, LocalData.myCrcdListData);

		// 初始化左边菜单
		initLeftSideList(this, LocalData.accountManagerlistData);
		// 初始化底部菜单栏
		initFootMenu();

		back = (Button) findViewById(R.id.ib_back);
		// 坐上角返回点击事件
		clickTopLeftClick();
	}

	/** 左上角返回点击事件 */
	public void clickTopLeftClick() {
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * 初始化左边菜单
	 * 
	 * @param context
	 * @param listData
	 */
	public void initAccLeftSideList(final Context context, final ArrayList<ImageAndText> listData) {
		final LinearLayout llleftmenu = (LinearLayout) findViewById(R.id.llleftmenu);
		slidingView = (LinearLayout) findViewById(R.id.sliding_tab);
		btnShow = (Button) findViewById(R.id.btn_show);
		btnHide = (Button) findViewById(R.id.btn_hide);

		if (getIntent().getBooleanExtra(ConstantGloble.COM_FROM_MAIN, false)) {
			btnShow.setVisibility(View.GONE);
			slidingView.setVisibility(View.VISIBLE);
			btnHide.setVisibility(View.VISIBLE);
			Animation animation = AnimationUtils.loadAnimation(this, R.anim.push_left_in);
			llleftmenu.startAnimation(animation);
		}

		final ListView leftSideListView = (ListView) findViewById(R.id.lv_main);
		leftSideListView.setAdapter(new LeftSideListAdapter(context, listData, LayoutValue.LEWFTMENUINDEX));
		leftSideListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				slidingView.setVisibility(View.GONE);
				btnShow.setVisibility(View.VISIBLE);
				selectedMenuItemHandler(CrcdAccBaseActivity.this,listData.get(position));
			}
		});
		btnShow.setVisibility(View.VISIBLE);
		btnHide.setVisibility(View.GONE);

		btnShow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btnShow.setVisibility(View.GONE);
				slidingView.setVisibility(View.VISIBLE);
				btnHide.setVisibility(View.VISIBLE);
			}
		});

		btnHide.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btnShow.setVisibility(View.VISIBLE);
				btnHide.setVisibility(View.GONE);
				slidingView.setVisibility(View.GONE);
			}
		});

	}

	protected boolean selectedMenuItemHandler(Activity context, ImageAndText menuItem) {
		
		
		super.selectedMenuItemHandler(context, menuItem);
		ActivityTaskManager.getInstance().removeAllActivity();
		Intent intent;
		String menuId = menuItem.MenuID;
		if(menuId.equals("accountManager_1")){
			// 我的账户
			intent = new Intent(this, AccManageActivity.class);
			startActivity(intent);
		}
		else if(menuId.equals("accountManager_2")){
			// 自助关联
			intent = new Intent(this, AccInputRelevanceAccountActivity.class);
			intent.putExtra(ConstantGloble.ACC_ISMY, true);
			startActivity(intent);
		}
		else if(menuId.equals("accountManager_3")){
			// 临时挂失
			intent = new Intent(this, AccDebitCardLossActivity.class);
			startActivity(intent);
		}
		else if(menuId.equals("accountManager_4")){
			// 电子现金账户
			intent = new Intent(this, FinanceMenuActivity.class);
			startActivity(intent);
		}
		else if(menuId.equals("accountManager_5")){
			// 虚拟银行卡服务
			intent = new Intent(this, VirtualBCServiceMenuActivity.class);
			startActivity(intent);
		}
		else if(menuId.equals("accountManager_6")){	// 医保账户
			intent = new Intent(this, MedicalMenuActivity.class);
			startActivity(intent);

		}
		else if(menuId.equals("accountManager_7")){	// 申请定期活期账户
//			intent=new Intent(this,ApplyTermDepositeActivity.class);
//			startActivity(intent);
			
		}
		return true;
//		
//		ActivityTaskManager.getInstance().removeAllActivity();
//		switch (clickIndex) {
//		case 0:
//			// 我的账户
//			Intent intent1 = new Intent(this, AccManageActivity.class);
//			startActivity(intent1);
//			break;
////		case 1:
////			// 自助关联
////			Intent intent2 = new Intent(this,
////					AccInputRelevanceAccountActivity.class);
////			intent2.putExtra(ConstantGloble.ACC_ISMY, true);
////			startActivity(intent2);
////			// ActivityTaskManager.getInstance().removeAllActivity();
////			break;
//		case 1:
//			// 临时挂失
//			Intent intent3 = new Intent(this, AccDebitCardLossActivity.class);
//			startActivity(intent3);
//			// ActivityTaskManager.getInstance().removeAllActivity();
//			break;
//		case 2:
//			// 虚拟银行卡服务
//			Intent intent5 = new Intent(this,
//					VirtualBCServiceMenuActivity.class);
//			startActivity(intent5);
//			// ActivityTaskManager.getInstance().removeAllActivity();
//			break;
//		case 3:
//			// 电子现金账户
//			Intent intent4 = new Intent(this, FinanceMenuActivity.class);
//			startActivity(intent4);
//			ActivityTaskManager.getInstance().removeAllActivity();
//			break;
//		case 4:
//			// 医保账户
//			Intent intent0 = new Intent(this, MedicalMenuActivity.class);
//			startActivity(intent0);
////			ActivityTaskManager.getInstance().removeAllActivity();
//			break;
//		default:
//			break;
//		}
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
				// TODO Auto-generated method stub
				if (rightBtnClick != null) {
					rightBtnClick.onClick(v);
				}
			}
		});
	}

	/**
	 * 设置右侧按钮文字
	 * 
	 * @param title
	 */
	public void setTextTag(String tag) {
		btn_right.setTag(tag);
	}

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

	public void setRightBtnClick(OnClickListener rightBtnClick) {
		this.rightBtnClick = rightBtnClick;
	}

	public void removeAllViews(View view) {
		tabcontent.removeView(view);
	}

	/**
	 * 获取安全因子
	 */
	public void requestCrcdTransSecurity() {
		// BiiHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.GETSECURITYFACTOR);
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);// 获取conversationId

		Map<String, String> map = new HashMap<String, String>();
		map.put(Tran.SERVICEID_REQ, "PB057C1");// 办理分期确认服务码
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "crcdTransSecurityCallBack");
	}

	public static List<Map<String, Object>> _combinList;

	public void crcdTransSecurityCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();

		_combinList = (List<Map<String, Object>>) result.get(Tran._COMBINLIST_RES);
	}

	protected String tokenId;

	/**
	 * 获取tokenId
	 */
	public void pSNGetTokenId() {
		BiiHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		HttpManager.requestBii(biiRequestBody, this, "aquirePSNGetTokenIdCallBack");
	}

	/**
	 * 处理获取tokenId的数据得到tokenId
	 * 
	 * @param resultObj
	 *            服务器返回数据
	 */
	public void aquirePSNGetTokenIdCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		tokenId = (String) biiResponseBody.getResult();
	}

	public void sendMSCToMobile() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.SET_SENDMSC);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "sendMSCToMobileCallback");
	}

	public void sendMSCToMobileCallback(Object resultObj) {
	}

	/** 查询信用卡-----快捷键启动 */
	public void requestCommonQueryAllChinaBankAccount() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.QRY_CRCD_LIST_API);
		Map<String, Object> params = new HashMap<String, Object>();
		String[] s = { ZHONGYIN, GREATWALL, SINGLEWAIBI };
		params.put(Crcd.CRCD_ACCOUNTTYPE_REQ, s);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "requestCommonQueryAllChinaBankAccountCallBack");
	}

	/*** 查询信用卡-----回调-----快捷键 */
	public void requestCommonQueryAllChinaBankAccountCallBack(Object resultObj) {
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		List<Map<String, Object>> returnList = (List<Map<String, Object>>) body.getResult();
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.CRCD_CRCDACCOUNTRETURNLIST, returnList);
	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.OneTask;
	}
}
