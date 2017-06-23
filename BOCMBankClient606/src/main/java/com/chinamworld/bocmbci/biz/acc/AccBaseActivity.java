package com.chinamworld.bocmbci.biz.acc;

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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.acc.applytermdeposite.ApplyTermDepositeActivity;
import com.chinamworld.bocmbci.biz.acc.financeicaccount.FinanceMenuActivity;
import com.chinamworld.bocmbci.biz.acc.lossreport.AccDebitCardLossActivity;
import com.chinamworld.bocmbci.biz.acc.medical.MedicalMenuActivity;
import com.chinamworld.bocmbci.biz.acc.mybankaccount.AccManageActivity;
import com.chinamworld.bocmbci.biz.acc.relevanceaccount.AccInputRelevanceAccountActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.virtualservice.VirtualBCServiceMenuActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.GetPhoneInfo;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.UsbKeyText;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

/**
 * 账户管理基类
 * 
 * @author wangmengmeng
 * 
 */
public class AccBaseActivity extends BaseActivity {
	/** 主视图布局 */
	protected LinearLayout tabcontent;// 主Activity显示
	/** 左侧返回按钮 */
	protected Button back;
	/** 左侧返回按钮点击事件 */
	private OnClickListener backBtnClick;
	/** 右侧按钮点击事件 */
	private OnClickListener rightBtnClick;
	/** 右上角按钮 */
	protected Button btn_right;
	/** 选择的账户详情 */
	protected Map<String, Object> chooseBankAccount;
	/** 修改账户别名返回 */
	protected String nickname = null;
	/** 请求回来的账户列表信息 */
	protected List<Map<String, Object>> bankAccountList;
	/** 请求回来的电子现金账户列表 */
	protected List<Map<String, Object>> financeIcAccountList;
	/** 自助关联上送服务码 */
	protected String relevanceServiceId = "PB010";
	/** 临时挂失上送服务码 */
	protected String lossReportServiceId = "PB010C";
	/** 新建签约上送服务码 */
	public String financeIcSignCreatServiceId = "PB013";
	/** 为他人充值上送服务码 */
	protected String financeIcTransferServiceId = "PB012";
	/** 退出动画 */
	protected Animation animation_up;
	/** 进入动画 */
	protected Animation animation_down;
	/** 中银信用卡 */
	protected static final String ZHONGYIN = "103";
	/** 长城信用卡 */
	public static final String GREATWALL = "104";
	/** 单外币信用卡 */
	protected static final String SINGLEWAIBI = "107";
	/** 纯IC卡 */
	protected static final String ICCARD = "300";
	/** 根据账户查投资服务 */
	public static final String QUERYTYPE = "1";
	/** 借记虚拟卡 */
	protected static final String JIEJIXN = "110";
	/** 优汇通专户 */
	public static final String YOUHUITONGZH = "199";
	/**中银E盾*/
	public UsbKeyText usbKeytext;
	/**申请定期活期账户*/
	public static final String ALLNONE="0000000000";
	/**中国     台湾    香港   澳门*/
	public static final String CHINA="CN";
	public static final String TAIWAN="TW";
	public static final String HONGKONG="HK";
	public static final String AOMEN="MO";
	/**需要过滤的卡：虚拟信用卡、借记虚拟卡、优汇通专户*/
	public List<String> XNCARDLIST=new ArrayList<String>(){
		{
//			add("108");
//			add("109");
			add("110");
			add("199");
		}
	};
	/** 关联账户转账 */
	protected static final int TRANTYPE_REL_ACCOUNT = 1;
	protected static final String TRANS_TYPE = "tranType";
	/** 转账方式 */
	protected int tranTypeFlag;
	/** 本人关联信用卡还款 */
	protected static final int TRANTYPE_REL_CRCD_REPAY = 2;
	/** 信用卡购汇还款 */
	protected static final int TRANTYPE_REL_CRCD_BUY = 3;
	/** 存款管理申请定期活期账户标示*/
	protected static final int APPLICATION_ACCOUNT = 1;
	// P403 普通账户详情列表信息
	public List<Map<String, Object>> tranOutaccountDetailList;
	// P403 信用卡详情列表信息
	public List<Map<String, Object>> tranOutdetailList = new ArrayList<Map<String, Object>>();
	/** 403 请求回来的医保账户列表 */
	protected List<Map<String, Object>> medicalAccountList;
	protected static final String MEDICALACC = "医保账户";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);
		// 初始化弹窗按钮
		initPulldownBtn();
		// 初始化左边菜单
		initLeftSideList(this, LocalData.accountManagerlistData);
		// 初始化底部菜单栏
		initFootMenu();
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		btn_right = (Button) findViewById(R.id.ib_top_right_btn);
		back = (Button) findViewById(R.id.ib_back);
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

	public void gonerightBtn() {
		btn_right.setVisibility(View.GONE);
	}

	private void initanimation() {
		animation_up = new AnimationUtils().loadAnimation(this,
				R.anim.scale_out);
		animation_down = new AnimationUtils().loadAnimation(this,
				R.anim.scale_in);
	}

	/** 左侧返回按钮点击事件 */
	OnClickListener backClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};

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

	/**
	 * 在slidingbody中引入自己布局文件
	 * 
	 * @param resource
	 * @return 引入布局
	 */
	public View addView(int resource) {
		View view = LayoutInflater.from(this).inflate(resource, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		return view;
	}

	@Override
	protected boolean selectedMenuItemHandler(Activity context, ImageAndText menuItem) {
		// 注意：虚拟银行卡服务模块继承的是CrcdAccBaseActivity父类，当要在这里添加case时需要同时在那个父类里添加
		// 否则在虚拟银行卡模块中点击左侧菜单新增加的模块会直接跳转到九宫格
		super.selectedMenuItemHandler(context, menuItem);
		ActivityTaskManager.getInstance().removeAllActivity();
		Intent intent;
		String menuId = menuItem.MenuID;
		if(menuId.equals("accountManager_1")){
			// 我的账户
			intent = new Intent(this, AccManageActivity.class);
			context.startActivity(intent);
		}
		else if(menuId.equals("accountManager_2")){
			// 自助关联
			intent = new Intent(this, AccInputRelevanceAccountActivity.class);
			intent.putExtra(ConstantGloble.ACC_ISMY, true);
			context.startActivity(intent);
		}
		else if(menuId.equals("accountManager_3")){
			// 临时挂失
			intent = new Intent(this, AccDebitCardLossActivity.class);
			context.startActivity(intent);
		}
		else if(menuId.equals("accountManager_4")){
			// 电子现金账户
			intent = new Intent(this, FinanceMenuActivity.class);
			context.startActivity(intent);
		}
		else if(menuId.equals("accountManager_5")){
			// 虚拟银行卡服务
			intent = new Intent(this, VirtualBCServiceMenuActivity.class);
			context.startActivity(intent);
		}
		else if(menuId.equals("accountManager_6")){	// 医保账户
			intent = new Intent(this, MedicalMenuActivity.class);
			context.startActivity(intent);

		}
		else if(menuId.equals("accountManager_7")){	// 申请定期活期账户
			intent=new Intent(this,ApplyTermDepositeActivity.class);
			context.startActivity(intent);
			
		}
		return true;
		// 注意：虚拟银行卡服务模块继承的是CrcdAccBaseActivity父类，当要在这里添加case时需要同时在那个父类里添加
		// 否则在虚拟银行卡模块中点击左侧菜单新增加的模块会直接跳转到九宫格
//		ActivityTaskManager.getInstance().removeAllActivity();
//		Intent intent;
//		switch (clickIndex) {
//		case 0:
//			// 我的账户
//			intent = new Intent(this, AccManageActivity.class);
//			startActivity(intent);
//			break;
////		case 1:
////			// 自助关联
////			intent = new Intent(this, AccInputRelevanceAccountActivity.class);
////			intent.putExtra(ConstantGloble.ACC_ISMY, true);
////			startActivity(intent);
////			// ActivityTaskManager.getInstance().removeAllActivity();
////			break;
//		case 1:
//			// 临时挂失
//			intent = new Intent(this, AccDebitCardLossActivity.class);
//			startActivity(intent);
//			// ActivityTaskManager.getInstance().removeAllActivity();
//			break;
//		case 2:
//			// 虚拟银行卡服务
//			intent = new Intent(this, VirtualBCServiceMenuActivity.class);
//			startActivity(intent);
//			// ActivityTaskManager.getInstance().removeAllActivity();
//			break;
//		case 3:
//			// 电子现金账户
//			intent = new Intent(this, FinanceMenuActivity.class);
//			startActivity(intent);
////			ActivityTaskManager.getInstance().removeAllActivity();
//			break;
//		case 4:
//			// 医保账户
//			intent = new Intent(this, MedicalMenuActivity.class);
//			startActivity(intent);
////			ActivityTaskManager.getInstance().removeAllActivity();
//			break;
//		case 5:
//			// 申请定期活期账户
//			intent=new Intent(this,ApplyTermDepositeActivity.class);
//			startActivity(intent);
//			break;
//		default:
//			break;
//		}
	}

	/** 退出账户详情监听事件 */
	protected OnClickListener exitAccDetailClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			BaseDroidApp.getInstanse().dismissMessageDialog();
		}
	};

	/** 请求修改账户别名 */
	public void requestModifyAccountAlias(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.ACC_MODIFYACCOUNT_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Acc.MODIFY_ACC_ACCOUNTID_REQ,
				String.valueOf(chooseBankAccount.get(Acc.ACC_ACCOUNTID_RES)));
		paramsmap.put(Acc.MODIFY_ACC_ACCOUNTNICKNAME_REQ, nickname);
		paramsmap.put(Acc.MODIFY_ACC_TOKEN_REQ, tokenId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"modifyAccountAliasCallBack");
	}

	/**
	 * 请求修改账户别名回调
	 * 
	 * @param resultObj
	 */
	public void modifyAccountAliasCallBack(Object resultObj) {
		// 子类进行覆盖
	}

	/** 请求所有电子现金账户列表信息 */
	public void requestFinanceIcAccountList() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.QRY_ACC_LIST_API);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		List<String> paramslist = new ArrayList<String>();
		// 传递能作为转出账户的列表
		paramslist.add(AccBaseActivity.accountTypeList.get(3));
		paramslist.add(AccBaseActivity.accountTypeList.get(1));
		paramslist.add(AccBaseActivity.accountTypeList.get(13));
		paramslist.add(AccBaseActivity.accountTypeList.get(2));
		paramsmap.put(Acc.ACC_ACCOUNTTYPE_REQ, paramslist);
		biiRequestBody.setParams(paramsmap);
		// 通讯开始,展示通讯框
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestFinanceIcAccountListCallBack");
	}

	/**
	 * 请求所有电子现金账户列表信息回调
	 * 
	 * @param resultObj
	 */
	public void requestFinanceIcAccountListCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		financeIcAccountList = (List<Map<String, Object>>) (biiResponseBody
				.getResult());
		if (financeIcAccountList == null || financeIcAccountList.size() == 0) {
			// 通讯结束,关闭通讯框
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse()
					.showInfoMessageDialog(
							AccBaseActivity.this
									.getString(R.string.acc_financeic_null));
			return;
		}
		AccDataCenter.getInstance().setFinanceIcAccountList(
				financeIcAccountList);

	}

	/** 请求所有电子现金账户列表信息 */
	public void requestMedicalAccountList() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.QRY_ACC_LIST_API);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		List<String> paramslist = new ArrayList<String>();
		// 传递能作为转出账户的列表
		paramslist.add(ConstantGloble.ACC_TYPE_BRO);
		paramsmap.put(Acc.ACC_ACCOUNTTYPE_REQ, paramslist);
		biiRequestBody.setParams(paramsmap);
		// 通讯开始,展示通讯框
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestMedicalAccountListCallBack");
	}

	/**
	 * 请求所有医保账户列表信息回调
	 * 
	 * @param resultObj
	 */
	public void requestMedicalAccountListCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		medicalAccountList = (List<Map<String, Object>>) (biiResponseBody
				.getResult());
		AccDataCenter.getInstance().setMedicalAccountList(medicalAccountList);

	}

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

	public void setRightBtnClick(OnClickListener rightBtnClick) {
		this.rightBtnClick = rightBtnClick;
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
	/**
	 * 电子现金账户状态
	 */
	public static final Map<String, String> accountState = new HashMap<String, String>() {
		{

			put("A", "正常状态");
			put("D", "销卡状态");
			put("Q", "预销卡状态");
			put("C", "回收状态");
			put("H", "临时挂失状态");
			put("L", "正式挂失状态");
			put("J", "补卡登记");
			put("K", "预补卡");
			put("O", "换卡登记");
			put("P", "预换卡");
			put("I", "初始状态");
			put("E", "待领卡");
		}
	};
	/** 是否有电子现金账户 */
	public static final List<String> isHaveECashAccountList = new ArrayList<String>() {
		{
			add("0");
			add("1");
			add("2");
		}
	};
	/** 关联标志 */
	public static final List<String> linkAcctFlagList = new ArrayList<String>() {
		{
			add("1");
			add("2");
			add("3");
		}
	};
	/** 临时挂失期限 */
	public static final List<String> lossDaysList = new ArrayList<String>() {
		{
			add("1");
			add("2");
			add("3");

		}
	};
	public static final Map<String, String> lossDaysMap = new HashMap<String, String>() {
		{
			put("1", "5天");
			put("2", "长期");
			put("3", "失败");

		}
	};
	
	/**是否同时冻结主账户*/
	public static final List<String> masterAccountList = new ArrayList<String>() {
		{
			add("1");
			add("2");
		}
	};
	public static final Map<String, String> masterAccountMap = new HashMap<String, String>() {
		{
			put("1", "是");
			put("2", "否");
		}
	};
	
	/** 钞汇标志list */
	public static List<String> cashMapList = new ArrayList<String>() {

		{
			add("00");
			add("01");
			add("02");
		}
	};
	public static List<String> cashNullList = new ArrayList<String>() {

		{
			add("01");
			add("02");
		}
	};
	public static final List<String> accCurrencyList = new ArrayList<String>() {
		{
			add("不可选择");
			add("人民币元");
			add("英镑");
			add("港币");
			add("美元");
			add("瑞士法郎");
			add("德国马克");
			add("法国法郎");
			add("新加坡元");
			add("荷兰盾");
			add("瑞典克朗");
			add("丹麦克朗");
			add("挪威克朗");
			add("奥地利先令");
			add("比利时法郎");
			add("意大利里拉");
			add("日元");
			add("加拿大元");
			add("澳大利亚元");
			add("欧元");
			add("芬兰马克");
			add("澳门元");
			add("菲律宾比索");
			add("泰国铢");
			add("新西兰元");
			add("韩元");
			add("记账美元");
			add("清算瑞士法郎");
			add("印尼盾");
			add("越南盾");
			add("阿联酋迪拉姆");
			add("阿根廷比索");
			add("巴西雷亚尔");
			add("埃及磅");
			add("印度卢比");
			add("约旦第纳尔");
			add("蒙古图格里克");
			add("尼日尼亚奈拉");
			add("罗马尼亚列伊");
			add("土耳其里拉");
			add("乌克兰格里夫纳");
			add("南非兰特");
			add("哈萨克斯坦坚戈");
			add("赞比亚克瓦查");
			add("匈牙利福林");
			add("林吉特");
			add("卢布");
			add("通配");
			add("美元指数");
			add("印尼卢比");
			add("巴西里亚尔");
			add("新台币");
			add("马来西亚林吉特");
//			add("印度卢比");
//			add("阿联酋迪拉姆");
		}
	};
	/**
	 * 账户类型
	 */
	public static Map<String, String> bankAccountType = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("-1", "他行");
			put("-2", "全部");
			put("00", "对公账户");
			put("01", "对公虚拟账户");
			put("02", "对公定期");
			put("03", "对公通知存款");
			put("04", "对私账户");
			put("05", "现金管理虚拟账户");
			put("06", "托管账户");
			put("07", "虚拟子账户自定义账号");
			put("08", "对公账户");
			put("101", "普通活期");
			put("103", "中银系列信用卡");
			put("104", "长城信用卡");
			put("106", "长城人民币信用卡（分行）");
			put("107", "单外币信用卡");
			put("119", "长城电子借记卡");
			put("140", "存本取息");
			put("150", "零存整取");
			put("152", "教育储蓄");
			put("170", "定期一本通");
			put("188", "活期一本通");
			put("190", "网上专属理财账户");
			put("201", "活期");
			put("202", "定期");
			put("203", "支票");
			put("204", "贷款");
			put("205", "保证金");
			put("206", "二十四小时通知账户");
			put("207", "现金管理账户");
			put("208", "7天通知账户");
			put("209", "30天通知账户");
			put("300", "电子现金账户");
			put("108", "虚拟信用卡");
			put("109", "虚拟信用卡");
			put("110", "借记虚拟卡");
			put("199", "优汇通专户");
			put("医保账户", "医保账户");
		};
	};

	/**
	 * 行内手续费试算
	 */
	public void requestForTransferCommissionCharge(String serviceId) {
		/** 转出账户信息 */
		Map<String, Object> transOutMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		/** 转入账户信息 */
		Map<String, Object> transInMap = TranDataCenter.getInstance()
				.getAccInInfoMap();
		String fromAccountId = (String) transOutMap.get(Tran.ACCOUNTID_RES);
		String toAccountId = (String) transInMap.get(Tran.ACCOUNTID_RES);
		String payeeactNo = (String) transInMap.get(Tran.ACCOUNTNUMBER_RES);
		String payeeName = (String) transInMap.get(Tran.ACCOUNTNAME_RES);
		Map<String, String> userInputMap = TranDataCenter.getInstance()
				.getUserInputMap();
		String currency = userInputMap.get(Tran.INPUT_CURRENCY_CODE);
		String cashremit = userInputMap.get(Tran.INPUT_CASHREMIT_CODE);
		String amount = userInputMap.get(Tran.INPUT_TRANSFER_AMOUNT);
		String memo = userInputMap.get(Tran.INPUT_TRANSFER_REMARK);
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.TRANSFER_COMMISSIONCHARGE_API);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Tran.SERVICEID_REQ, serviceId);
		map.put(Tran.RELTRANS_FROMACCOUNTID_REQ, fromAccountId);
		map.put(Tran.RELTRANS_TOACCOUNTID_REQ, toAccountId);
		map.put(Tran.RELTRANS_CURRENCY_REQ, currency);
		map.put(Tran.RELTRANS_AMOUNT_REQ, amount);
		map.put(Tran.RELTRANS_CASHREMIT_REQ, cashremit);
		map.put(Tran.RELTRANS_REMARK_REQ, memo);
		map.put(Tran.RELTRANS_PAYEEACTNO_REQ, payeeactNo);
		map.put(Tran.RELTRANS_PAYEENAME_REQ, payeeName);
		map.put(Tran.NOTIFYID, null);
		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestForTransferCommissionChargeCallBack");
	}

	/**
	 * 行内手续费试算返回
	 * 
	 * @param resultObj
	 */
	public void requestForTransferCommissionChargeCallBack(Object resultObj) {

	}

	public void setBackBtnClick(OnClickListener backBtnClick) {
		this.backBtnClick = backBtnClick;
	}

//	public void showTimeOutDialog(BiiError biiError) {
//		BaseDroidApp.getInstanse().showMessageDialog(biiError.getMessage(),
//				new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						BaseDroidApp.getInstanse().dismissErrorDialog();
//						ActivityTaskManager.getInstance().removeAllActivity();
//						Intent intent = new Intent();
//						intent.setClass(AccBaseActivity.this,
//								LoginActivity.class);
//						startActivityForResult(intent,
//								ConstantGloble.ACTIVITY_RESULT_CODE);
//					}
//				});
//	}

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
	 * 为TextView赋值
	 */
	public void accountValue(List<String> list,TextView tv,StringBuffer sb){
		for(int i=0;i<list.size();i++){
			if(i==0){
				sb.append(list.get(i));
			}else{
				sb.append("、"+list.get(i));
			}
		}
		tv.setText(sb);
	}
	/**
	 * TextView(开户原因的显示)
	 */
	public void showTitle(TextView tv,String countryCode){
		if(countryCode.equals("TW")||countryCode.equals("HK")
				||countryCode.equals("MO")){
			tv.setText(this.getString(R.string.acc_account_open));
		}else{
			tv.setText(this.getString(R.string.acc_account_open_reason));
		}
	}
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

	// 403请求余额
	/**
	 * 根据币种和钞汇标志找到对应的余额
	 * 
	 * @param tran_success_out_balance
	 *            转出账户余额的LinearLayout
	 * @param tv_tran_success_out_balance
	 *            转出账户余额字段的TextView
	 * @param accountDetailList
	 *            请求回来的账户余额数据
	 * @param currency
	 *            转出币种
	 * @param cashRemit
	 *            转出钞汇
	 */
	public void refreshTranOutBalance(boolean isCrcd,
			LinearLayout tran_success_out_balance,
			TextView tv_tran_success_out_balance,
			List<Map<String, Object>> accountDetailList, String currency,
			String cashRemit) {
		if (isCrcd) {
			Map<String, Object> detailMap = new HashMap<String, Object>();
			detailMap = accountDetailList.get(0);
			final String currentBalance = (String) detailMap
					.get(Crcd.CRCD_LOANBALANCELIMIT);
			String currentflag = (String) detailMap
					.get(Crcd.LOANBALANCELIMITFLAG);
			tran_success_out_balance.setVisibility(View.VISIBLE);
			if (StringUtil.isNull(currentflag)) {
				tv_tran_success_out_balance.setText(StringUtil
						.parseStringCodePattern(currency, currentBalance, 2));
			} else {
				if (currentflag.equals(ConstantGloble.ACC_CURRENTINDEX_VALUE)) {
					tv_tran_success_out_balance
							.setText(StringUtil.parseStringCodePattern(
									currency, currentBalance, 2)
									+ ConstantGloble.JIEYU);
				} else if (currentflag
						.equals(ConstantGloble.LOAN_CURRENTINDEX_VALUE)) {
					tv_tran_success_out_balance
							.setText(StringUtil.parseStringCodePattern(
									currency, currentBalance, 2)
									+ ConstantGloble.TOUZHI);
				} else {
					tv_tran_success_out_balance
							.setText(StringUtil.parseStringCodePattern(
									currency, currentBalance, 2));
				}
			}

		} else {
			for (int i = 0; i < accountDetailList.size(); i++) {
				String currencyout = (String) accountDetailList.get(i).get(
						Acc.DETAIL_CURRENCYCODE_RES);
				String cashremitout = (String) accountDetailList.get(i).get(
						Acc.DETAIL_CASHREMIT_RES);
				String balance = (String) accountDetailList.get(i).get(
						Acc.DETAIL_AVAILABLEBALANCE_RES);
				if (!StringUtil.isNullOrEmpty(currencyout)
						&& currencyout.equals(currency)) {
					if ((ConstantGloble.PRMS_CODE_RMB).equals(currency)) {
						tran_success_out_balance.setVisibility(View.VISIBLE);
						tv_tran_success_out_balance
								.setText(StringUtil.parseStringCodePattern(
										currencyout, balance, 2));
						break;
					} else {
						if (!StringUtil.isNullOrEmpty(cashremitout)
								&& cashremitout.equals(cashRemit)) {
							tran_success_out_balance
									.setVisibility(View.VISIBLE);
							tv_tran_success_out_balance.setText(StringUtil
									.parseStringCodePattern(currencyout,
											balance, 2));
							break;
						}
					}

				}
			}
		}

	}

	/**
	 * 请求账户余额
	 */
	public void requestAccBankOutDetail(String accountId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.QRY_ACC_BALANCE_API);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Acc.DETAIL_ACC_ACCOUNTID_REQ, accountId);
		BiiHttpEngine.showProgressDialog();
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestAccBankOutDetailCallback");

	}

	/**
	 * 请求账户余额回调
	 * 
	 * @param resultObj
	 *            返回结果
	 */
	public void requestAccBankOutDetailCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> callbackmap = (Map<String, Object>) (biiResponseBody
				.getResult());
		if (StringUtil.isNullOrEmpty(callbackmap)) {
			BiiHttpEngine.dissMissProgressDialog();
			return;
		}
		BiiHttpEngine.dissMissProgressDialog();
		tranOutaccountDetailList = (List<Map<String, Object>>) (callbackmap
				.get(ConstantGloble.ACC_DETAILIST));
	}

	/**
	 * 请求信用卡余额信息
	 * 
	 * @param accountId
	 *            账户ID
	 * @param currency
	 *            查询币种
	 */
	public void requestPsnCrcdOutDetail(String accountId, String currency) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_ACCOUNTDETAIL_API);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		paramsmap.put(Crcd.CRCD_CURRENCY, currency);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnCrcdOutDetailCallBack");
	}

	/**
	 * 请求信用卡余额信息返回
	 * 
	 * @param resultObj
	 */
	public void requestPsnCrcdOutDetailCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultDetail = (Map<String, Object>) biiResponseBody
				.getResult();
		BiiHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(resultDetail)) {
			return;
		}
		Map<String, Object> detailMap = new HashMap<String, Object>();
		tranOutdetailList = (List<Map<String, Object>>) resultDetail
				.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
		if (StringUtil.isNullOrEmpty(tranOutdetailList)) {
			return;
		}
		detailMap = tranOutdetailList.get(0);
		if (StringUtil.isNullOrEmpty(detailMap)) {
			return;
		}
	}
	
	/**
	 * 为请求网络提供统一的请求方法
	 * 
	 * @param requestMethod
	 *            要请求的接口
	 * @param responseMethod
	 *            请求成功后的回调方法
	 * @param params
	 *            参数列表，子类准备此参数
	 * @param needConversationId
	 *            是否需要ConversationId
	 */
	public void requestHttp(String requestMethod, String responseMethod, Map<String, Object> params, boolean needConversationId){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(requestMethod);
		biiRequestBody.setParams(params);
		// 如果需要ConversationId
		if (needConversationId)
			biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, responseMethod);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 10023) {
			setResult(10023);
			finish();
		}
	}
	
	@Override
	public ActivityTaskType getActivityTaskType() {
		return ActivityTaskType.OneTask;
	}
}
