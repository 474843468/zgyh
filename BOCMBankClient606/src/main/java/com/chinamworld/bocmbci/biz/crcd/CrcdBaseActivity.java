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
import com.chinamworld.bocmbci.biz.crcd.mycrcd.MyCardInstalmentActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.MyCardPaymentActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.MyCreditCardActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.MyMasterAndSupplInfoListActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup.CrcdActiveInfo;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.virtualservice.VirtualBCServiceMenuActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.adapter.LeftSideListAdapter;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

/**
 * 信用卡管理基类
 * 
 * @author huangyuchao
 * 
 */
public class CrcdBaseActivity extends BaseActivity {

	/** 主视图布局 */
	public LinearLayout tabcontent;// 主Activity显示
	/** 右侧按钮点击事件 */
	private OnClickListener rightBtnClick;
	/** 人民币代码001 */
	public static String currency = ConstantGloble.PRMS_CODE_RMB;
	/** 人民币元 */
	protected static String strCurrency;

	/** 请求回来的账户列表信息 (我的信用卡) */
	public List<Map<String, Object>> bankAccountList = new ArrayList<Map<String, Object>>();

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
	
	
//	
//	信用卡查询密码设置 PB130
	public String psnChaxunsecurityId = "PB130";
//	信用卡消费密码设置 PB131
	public String psnXisofeisecurityId = "PB131";
//	信用卡外币账单自动购汇设置 
	public String psnGouhuisecurityId = "PB132";
	// 虚拟卡服务码
	public String psnVirsericurityId = "PB059";

	private void removeAllEceptFirst() {
		for (int i = 0; i < actLists.size(); i++) {
			if (i != 0) {
				actLists.remove(i);
			}
		}
	}

	/** 登陆后的conversationId */
	public String commConversationId = null;

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

		initLeftSideList(this, LocalData.myCrcdListData);
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
				selectedMenuItemHandler(CrcdBaseActivity.this, listData.get(position));
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

	@Override
	protected boolean selectedMenuItemHandler(Activity context, ImageAndText menuItem) {
		super.selectedMenuItemHandler(context, menuItem);
		String menuId = menuItem.MenuID;
		if(menuId.equals("myCrcd_1")){
			// 信用卡查询
			ActivityTaskManager.getInstance().removeAllActivity();
			Intent intent1 = new Intent(this, MyCreditCardActivity.class);
			intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent1);
		}
		else if(menuId.equals("myCrcd_2")){
			// 信用卡还款
			ActivityTaskManager.getInstance().removeAllActivity();
			Intent intent2 = new Intent(this, MyCardPaymentActivity.class);
			intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent2);
		}
		else if(menuId.equals("myCrcd_3")){
			// 分期业务
			// Intent intent3 = new Intent(this,
			// TransactionQueryActivity.class);
			ActivityTaskManager.getInstance().removeAllActivity();
			Intent intent3 = new Intent(this, MyCardInstalmentActivity.class);
			intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent3);
		}
		else if(menuId.equals("myCrcd_4")){
			// 信用卡附属卡管理
			ActivityTaskManager.getInstance().removeAllActivity();		
			Intent intent4 = new Intent(this, MyMasterAndSupplInfoListActivity.class);
			intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent4);
	
		}
		else if(menuId.equals("myCrcd_5")){
			// 虚拟银行卡服务
			ActivityTaskManager.getInstance().removeAllActivity();
			Intent intent5 = new Intent(this, VirtualBCServiceMenuActivity.class);
			intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent5);

		}
		else if(menuId.equals("myCrcd_6")){
			// 信用卡激活
			ActivityTaskManager.getInstance().removeAllActivity();
			Intent intent6 = new Intent(this, CrcdActiveInfo.class);
			intent6.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent6);
	
		}
		return true;
		
		
//		switch (clickIndex) {
//		case 0:
//			// 信用卡查询
//			ActivityTaskManager.getInstance().removeAllActivity();
//			Intent intent1 = new Intent(this, MyCreditCardActivity.class);
//			intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent1);
//			break;
//		case 1:
//			// 信用卡还款
//			ActivityTaskManager.getInstance().removeAllActivity();
//			Intent intent2 = new Intent(this, MyCardPaymentActivity.class);
//			intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent2);
//			break;
//		case 2:
//			// 分期业务
//			// Intent intent3 = new Intent(this,
//			// TransactionQueryActivity.class);
//			ActivityTaskManager.getInstance().removeAllActivity();
//			Intent intent3 = new Intent(this, MyCardInstalmentActivity.class);
//			intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent3);
//			break;
//			
//		case 3:
//			// 信用卡附属卡管理
//			ActivityTaskManager.getInstance().removeAllActivity();		
//			Intent intent4 = new Intent(this, MyMasterAndSupplInfoListActivity.class);
//			intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent4);
//			
//			break;
//		case 4:
//			// 虚拟银行卡服务
//			ActivityTaskManager.getInstance().removeAllActivity();
//			Intent intent5 = new Intent(this, VirtualBCServiceMenuActivity.class);
//			intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent5);
//		
//			break;
//		case 5:
//			// 信用卡激活
//			ActivityTaskManager.getInstance().removeAllActivity();
//			Intent intent6 = new Intent(this, CrcdActiveInfo.class);
//			intent6.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent6);
//			break;
//			
////		case 0:
////			// 我的信用卡
////			ActivityTaskManager.getInstance().removeAllActivity();
////			Intent intent1 = new Intent(this, MyCreditCardActivity.class);
////			intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////			startActivity(intent1);
////			break;
////		case 1:
////			// 信用卡设定
////			ActivityTaskManager.getInstance().removeAllActivity();
////			Intent intent2 = new Intent(this, MyCardSetupMenuActivity.class);
////			intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////			startActivity(intent2);
////			break;
////		case 2:
////			// 交易查询
////			// Intent intent3 = new Intent(this,
////			// TransactionQueryActivity.class);
////			ActivityTaskManager.getInstance().removeAllActivity();
////			Intent intent3 = new Intent(this, MyCardTransMenuActivity.class);
////			intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////			startActivity(intent3);
////			break;
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

	public void removeAllViews(View view) {
		tabcontent.removeView(view);
	}

	public void setRightBtnClick(OnClickListener rightBtnClick) {
		this.rightBtnClick = rightBtnClick;
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
		// 通讯结束,关闭通讯框
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String resultSmc = (String) biiResponseBody.getResult();
		LogGloble.d("crcd", resultSmc + "<><><><><><>");
		// if (!resultSmc.equals(smcString)) {
		// BaseDroidApp.getInstanse().showInfoMessageDialog(
		// getResources().getString(R.string.set_smc_notequle_error));
		// BaseHttpEngine.dissMissProgressDialog();
		// return;
		// }
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
	
	
	/** 卡状态*/
	public  Map<String, String> carStatusTypegMap = new HashMap<String, String>() {
		{
			put("ACCC", "销户销卡");
			put("ACTP", "卡未激活");
			put("CANC", "卡片取消");
			put("DCFR", "欺诈拒绝");
			put("FRAU", "欺诈冻结");
			put("FUSA", "首次使用超限");
			put("LOST", "挂失");
			put("LOCK", "锁卡");
			put("PIFR", "没收卡");			
			put("PINR", "密码输入错误超次");			
			put("QCPB", "卡预销户");			
			put("REFR", "欺诈卡，需联系发卡行");			
			put("STOL", "偷窃卡");			
			put("STPP", "止付");			
			put("BFRA", "分行冻结");
			put("CFRA", "催收冻结");
			put("SFRA", "客服冻结");
//			put("", "正常");
		}
	};
	
	public String flagConvert(String flag) {
		if ("0".equals(flag)) {
			flag = "欠款";
		} else if ("1".equals(flag)) {
			flag = "存款";
		} else if ("2".equals(flag)) {
			flag = "";
		}

		return flag;
	}
	
	public boolean doBiihttpRequestCallBackPre(BiiResponse response) {
		List<BiiResponseBody> biiResponseBodyList = response.getResponse();
		if (!StringUtil.isNullOrEmpty(biiResponseBodyList)) {
			for (BiiResponseBody body : biiResponseBodyList) {

				if (Crcd.CRCD_PSNQUERYCRCDPOINT.equals(body.getMethod())) {
					// 查询积分 不显示
						return false;
					
				} else {
					return super.doBiihttpRequestCallBackPre(response);
				}
			}
		}

		return false;
	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.OneTask;
	}
}
