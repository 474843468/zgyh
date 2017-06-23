package com.chinamworld.bocmbci.biz.dept;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.BusinessModelControl;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.transquery.CrcdXiaofeiQueryListActivity;
import com.chinamworld.bocmbci.biz.dept.fiexibleinterest.FiexibleInterestActivity;
import com.chinamworld.bocmbci.biz.dept.largecd.LargeCDMenuActivity;
import com.chinamworld.bocmbci.biz.dept.myreg.MyRegSaveActivity;
import com.chinamworld.bocmbci.biz.dept.notmg.NotifyManageActivity;
import com.chinamworld.bocmbci.biz.dept.savereg.SaveRegularActivity;
import com.chinamworld.bocmbci.biz.dept.ydzc.DeptYdzcQueryActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.Dictionary;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

public class DeptBaseActivity extends BaseActivity {
	/** 大额存单 资金账户列表 */
	protected static final String CONTRACT_ACCOUT_LIST = "largeCDSignedAccList";
	/** 我要存定期 */
	protected static final int SAVE_REGULAR = 0;
	/** 我的定期存款 */
	protected static final int MY_REG_SAVE = 1;
	/** 定期约定转存 */
	protected static final int DEPT_DQYDZC_MENU = 2;
	// /** 智能通知存款 */
	// protected static final int DEPT_ZNTZCK_MENU = 3;
	/** 大额存单 */
	protected static final int LARGE_CD_MENU = 3;
	/** 通知管理 */
	protected static final int NOTIFY_MANAGE = 4;
	/** 记录查询 */
	protected static final int RECORD_QUERY = 5;

	/** 立即执行 */
	protected static final int IMMEDIATELY_EXCUTE = 0;
	/** 预约日期执行 */
	protected static final int PRE_DATE_EXCUTE = 1;
	/** 预约周期执行 */
	protected static final int PRE_WEEK_EXCUTE = 2;
	/** 一天或者七天通知存款支取 */
	protected static final int ONE_DAY_EXCUTE = 3;

	// 通讯返回回调标识
	protected static final int WHOLE_SAVE_CALLBACK = 7;// 整存整取
	protected static final int REGULAR_RANDOM_CALLBACK = 8;// 定活两便
	protected static final int NOTIFY_SAVE_CALLBACK = 9;// 一天或者七天通知存款
	protected static final int QUERY_ALL_ACCOUNT_CALLBACK = 10;// 查询所有账户列表
	protected static final int QUERY_ACCOUNT_DETAIL_CALLBACK = 11;// 查询帐户详情
	protected static final int QUERY_ALL_IN_ACCOUNT_CALLBACK = 12;// 查询所有转入账户
	protected static final int QUERY_MYREG_DETAIL_CALLBACK = 13;// 查询定期存单详情
	protected static final int SAVE_WHOLE_CHECKOU_CALLLBAK = 14;// 支取整存整存 支取定活两便
	protected static final int EXTEND_EDUCATION_CALLLBAK = 15;// 支取 教育储蓄续存 零存整取
	protected static final int CREATE_NOTICE_CALLLBAK = 16;// 建立通知
	protected static final int QUERY_NOTIFY_ACCOUNT_DETAIL_CALLBACK = 17;// 查询通知存款详情
	protected static final int DELETE_NOTIFY_CALLBACK = 5;// 查询通知存款详情
	protected static final int NOTIFY_SAVE_CHECKOUT_CALLBACK = 18;// 查询通知存款详情
	protected static final int MODIFY_NICKNAME_CALLBACK = 19;// 修改账户别名
	protected static final int QUERY_ACCOUNT_OUT_CALLBACK = 20;// 我要存定期 转出账户返回
	protected static final int QUERY_ACCOUNT_IN_CALLBACK = 21;// 我要存定期 转入账户返回

	protected static final int NEW_SAVE_WHOLE_CHECKOU_CALLLBAK = 14;// 新增手续费试算
	// 支取整存整存
	// 支取定活两便

	protected static final int WHOLE_SAVE_BOTTOM = 13;// 整存整取底部标示
	protected static final int REGULAR_RANDOM_BOTTOM = 14;// 定活两便底部标示
	protected static final int NOTIFY_ONE_DAY_BOTTOM = 15;// 一天通知存款底部标示
	protected static final int NOTIFY_SEVEN_DAY_BOTTOM = 16;// 七天通知存款底部标示
	protected static final int WHOLE_SAVE_BOTTOM_NEW = 133;// 整存整取1.3倍底部标示

	protected static final int INTEREST_TAG = 13;// 整存整取1.3倍标示
	protected static final int APPLICATION_ACCOUNT = 1;// 存款管理申请定期活期账户标示

	protected static final String BUSINESSTYPE_ONEDAY = "1";// 一天
	protected static final String BUSINESSTYPE_SEVENDAY = "7";// 七天
	protected static final int FLAG_ONEDAY = 1;// 一天 标示位
	protected static final int FLAG_SEVENDAY = 7;// 七天 标示位
	protected static final String DEPT_SELECTEDPOSITION = "selectedPosition";// 用户选择位置

	// 存单类型
	/** 整存整取 */
	public static final String WHOE_SAVE = "110";
	/** 存本取息 */
	public static final String SAVE_INTESERT1 = "140";
	/** 存本取息 */
	public static final String SAVE_INTESERT2 = "913";
	/** 零存整取 */
	public static final String ZERO_SAVE1 = "150";
	/** 零存整取 */
	public static final String ZERO_SAVE2 = "912";
	/** 教育储蓄 */
	public static final String EDUCATION_SAVE1 = "152";
	/** 教育储蓄 */
	public static final String EDUCATION_SAVE2 = "935";
	/** 定活两便 */
	public static final String RANDOM_SAVE = "160";
	/** 通知存款 */
	public static final String NOTIFY_SAVE = "166";
	/** 定期一本通 */
	public static final String RANDOM_ONE_SAVE = "170";

	// //////////// 大额存单 start
	/** 借记卡 */
	public static final String LargeSign_WHOE_SAVE = "119";
	/** 活期一本通 */
	public static final String LargeSign_ONE_SAVE = "188";
	/** 活期一本通 */
	public static final String ACCOUNTCATALOG = "accountCatalog";

	/** 活期一本通 */
	public static final String LargeSign_LIVING = "101";

	// /////////// 大额存单 end

	// 通知状态
	protected static final String NOMARL_STATUS = "A";// 正常
	protected static final String PASS_STATUS = "O";// 已逾期
	protected static final String NOTIFY_STATUS_P = "P";// 已结清
	protected static final String NOTIFY_STATUS_W = "W";// 待销户

	/** 部分支取 */
	public static final String DRAW_MODE_N = "N";
	/** 全部支取 */
	public static final String DRAW_MODE_Y = "Y";
	/** 校验金额-存储金额 */
	public static final int AMOUNT_DEPOSIT = 2;
	/** 校验金额-支取金额 */
	public static final int AMOUNT_OBTAIN = 1;

	/** 预约日期查询 */
	protected static final int PRE_DATE = 1;
	/** 执行日期查询 */
	protected static final int EXE_DATE = 0;

	protected static final String NOTIFY_FLAG = "NotifyMgFlag";
	/** 异省手续费试算是否成功 1成功0失败 */
	protected static final String ZERO = "0";
	/** 异省手续费试算是否成功 1成功0失败 */
	protected static final String ONE = "1";

	/** 所有账户列表 */
	protected List<Map<String, Object>> accountList;
	/** 所有账户详情 */
	private List<Map<String, Object>> accountDetailList = new ArrayList<Map<String, Object>>();
	private int i = 0;// 记录循环查询 当前是第几个

	/** 转入账户列表 */
	protected List<Map<String, Object>> accountInList;

	/** 转出账户详情 */
	protected Map<String, Object> accOutInfoMap;
	/** 转入账户详情 */
	protected Map<String, Object> accInInfoMap;
	/** 定期存款-新增整存整取费用试算 确定 */
	protected boolean isAgree = true;
	/**
	 * 存款管理 人名币 --存期
	 */
	public static final List<String> saveTimeList = new ArrayList<String>() {
		{
			add("三个月");
			add("六个月");
			add("一年");
			add("两年");
			add("三年");
			add("五年");
		}
	};

	/**
	 * 存款管理 人名币 --存期
	 */
	public static final List<String> newSaveTimeList = new ArrayList<String>() {
		{
			add("三个月人行基准1.3倍");
			add("六个月人行基准1.3倍");
			add("一年人行基准1.3倍");
			add("三个月");
			add("六个月");
			add("一年");
		}
	};

	/**
	 *
	 * 存款管理 人名币 --存期 (澳门元 韩元 卢布) 支持1个月、3个月、6个月、1年四个档次的存款
	 */
	public static final List<String> dateList = new ArrayList<String>() {
		{
			add("一个月");
			add("三个月");
			add("六个月");
			add("一年");
		}
	};

	/**
	 *
	 * 存款管理 外币 --存期
	 */
	public static final List<String> saveTimeForeList = new ArrayList<String>() {
		{
			add("一个月");
			add("三个月");
			add("六个月");
			add("一年");
			add("两年");
		}
	};
	/*
	 * 存款管理 外币--新西兰元，丹麦克朗，挪威克朗，瑞典克朗
	 */
	public static final List<String> saveTimeTwoList = new ArrayList<String>() {
		{
			add("一个月");
			add("三个月");
			add("六个月");
			add("一年");
		}
	};

	/**
	 * 执行约定方式
	 */
	public static final List<String> promiseWayList = new ArrayList<String>() {
		{
			add("约定转存");
			add("非约定转存");
		}
	};

	// ===============================================================================
	// add by luqp 2016年3月21日 利率上浮1.3倍专用
	/** 存款管理 人名币 --存期*/
	public static final List<String> saveTimeListNew = new ArrayList<String>() {
		{
			add("三个月人行基准1.3倍");
			add("六个月人行基准1.3倍");
			add("一年人行基准1.3倍");
			add("三个月");
			add("六个月");
			add("一年");
			add("两年");
			add("三年");
			add("五年");
		}
	};

	// ================================================================================

	// ===============================================================================
	// add by luqp 2016年3月21日 利率上浮1.3倍专用
	/** 存款管理 人名币 --存期*/
	public static final Dictionary<String, String, Boolean> interestRateFloatingSpinner = new Dictionary<String, String, Boolean>() {
		{
			put("三个月优惠利率", "3", false);
			put("六个月优惠利率", "6", false);
			put("一年期优惠利率", "12", false);
			put("三个月", "3", true);
			put("六个月", "6", true);
			put("一年", "12", true);
			put("两年", "24", true);
			put("三年", "36", true);
			put("五年", "60", true);

		}
	};
	// ================================================================================

	// ===============================================================================
	// add by luqp 2016年8月24日 定利多
	/** 存款管理 人名币 --存期*/
	public static final Dictionary<String, String, Boolean> fixedDumbledoreSpinner = new Dictionary<String, String, Boolean>() {
		{
			put("三个月", "3", true);
			put("三个月优惠利率", "3", false);
			put("六个月", "6", true);
			put("六个月优惠利率", "6", false);
			put("六个月定利多升级版", "6", false);
			put("一年", "12", true);
			put("一年期优惠利率", "12", false);
			put("一年定利多升级版", "12", false);
			put("两年", "24", true);
			put("两年定利多升级版", "24", false);
			put("三年", "36", true);
			put("五年", "60", true);
		}
	};
	// ================================================================================

	// ================================================================================
	/**
	 *
	 * 存款管理 外币 --存期
	 */
	public static final List<String> queryTranTypeList = new ArrayList<String>() {
		{
			add("全部");
			add("正常");
			add("已转让");
			add("已支取");
			add("止付");
		}
	};
	// ================================================================================

	/** 币种标示 */
	protected static final String CURRENCYCODE_FLAG = "currencyCodeFlag";
	/** 当前选中list中的postion */
	protected static final String CURRENT_POSITION = "currentPosition";

	protected String oneDayTitle;
	protected String sevenDayTitle;
	/** 发起新转账 */
	protected Button newTranBtn;
	/** 返回按钮 */
	protected Button ibBack;
	// 前端验证 错误提示信息
	protected String chooseOutNoMessage;
	protected String chooseInNoMessage;
	protected String inputAmountMessage;
	protected String amountWrong;
	protected String chooseMessage;

	protected LayoutInflater mInflater;
	/** 主视图布局 */
	public LinearLayout tabcontent;// 主Activity显示
	/** 头部右侧按钮 */
	protected Button ibRight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);
		initPulldownBtn(); // 加载上边下拉菜单
		initFootMenu(); // 加载底部菜单栏
		initLeftSideList(this, LocalData.deptStorageCashLeftList); // 加载左边菜单栏
		mInflater = LayoutInflater.from(this);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		ibBack = (Button) this.findViewById(R.id.ib_back);
		ibRight = (Button) this.findViewById(R.id.ib_top_right_btn);
		ibBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		oneDayTitle = this.getResources().getString(R.string.one_day_title);
		sevenDayTitle = this.getResources().getString(R.string.seven_day_title);
		// initData();
		chooseOutNoMessage = this.getResources().getString(R.string.choose_outno_message);
		chooseInNoMessage = this.getResources().getString(R.string.choose_inno_message);
		inputAmountMessage = this.getResources().getString(R.string.input_amount_message);
		amountWrong = this.getResources().getString(R.string.amount_wrong);
		chooseMessage = this.getResources().getString(R.string.please_choose);
	}

	@Override
	protected boolean selectedMenuItemHandler(Activity context, ImageAndText menuItem) {
		super.selectedMenuItemHandler(context, menuItem);
		String menuId = menuItem.MenuID;
		if(menuId.equals("deptStorageCash_1")){// 我要存定期
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof SaveRegularActivity)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
						SaveRegularActivity.class);
				context.startActivity(intent);
			}

		}
		else if(menuId.equals("deptStorageCash_2")){// 我的定期存款
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof MyRegSaveActivity)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
						MyRegSaveActivity.class);
				context.startActivity(intent);
			}

		}
		else if(menuId.equals("deptStorageCash_3")){// 定期约定转存5
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof DeptYdzcQueryActivity)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
						DeptYdzcQueryActivity.class);
				context.startActivity(intent);
			}

		}
		else if(menuId.equals("deptStorageCash_4")){// 大额存单
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof LargeCDMenuActivity)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
						LargeCDMenuActivity.class);
				context.startActivity(intent);
			}

		}
		else if(menuId.equals("deptStorageCash_5")){// 通知管理
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof NotifyManageActivity)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
						NotifyManageActivity.class);
				context.startActivity(intent);
			}

		}
		else if(menuId.equals("deptStorageCash_6")){// 记录查询
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof QureyRecordManagerActivity)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
						QureyRecordManagerActivity.class);
				context.startActivity(intent);
			}
		}
		else if(menuId.equals("deptStorageCash_100")){// 灵活计息
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof FiexibleInterestActivity)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
						FiexibleInterestActivity.class);
				context.startActivity(intent);
			}

		}

		return true;

//		if (LocalData.deptStorageCashLeftList.size()==6) {
//			switch (clickIndex) {
//
//			case SAVE_REGULAR:// 我要存定期
//				if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof SaveRegularActivity)) {
//					ActivityTaskManager.getInstance().removeAllActivity();
//					Intent intent = new Intent();
//					intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
//							SaveRegularActivity.class);
//					startActivity(intent);
//				}
//				break;
//			case MY_REG_SAVE:// 我的定期存款
//				if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof MyRegSaveActivity)) {
//					ActivityTaskManager.getInstance().removeAllActivity();
//					Intent intent = new Intent();
//					intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
//							MyRegSaveActivity.class);
//					startActivity(intent);
//				}
//				break;
//			case NOTIFY_MANAGE:// 通知管理
//				if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof NotifyManageActivity)) {
//					ActivityTaskManager.getInstance().removeAllActivity();
//					Intent intent = new Intent();
//					intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
//							NotifyManageActivity.class);
//					startActivity(intent);
//				}
//				break;
//			case RECORD_QUERY:// 记录查询
//				if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof QureyRecordManagerActivity)) {
//					ActivityTaskManager.getInstance().removeAllActivity();
//					Intent intent = new Intent();
//					intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
//							QureyRecordManagerActivity.class);
//					startActivity(intent);
//				}
//				break;
//			case LARGE_CD_MENU:// 大额存单
//				if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof LargeCDMenuActivity)) {
//					ActivityTaskManager.getInstance().removeAllActivity();
//					Intent intent = new Intent();
//					intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
//							LargeCDMenuActivity.class);
//					startActivity(intent);
//				}
//				break;
//			case DEPT_DQYDZC_MENU:// 定期约定转存5
//				if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof DeptYdzcQueryActivity)) {
//					ActivityTaskManager.getInstance().removeAllActivity();
//					Intent intent = new Intent();
//					intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
//							DeptYdzcQueryActivity.class);
//					startActivity(intent);
//				}
//				break;
//			// case DEPT_ZNTZCK_MENU:// 智能通知存款6
//			// if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof
//			// DeptZntzckThreeMenuActivity)) {
//			// ActivityTaskManager.getInstance().removeAllActivity();
//			// Intent intent = new Intent();
//			// intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
//			// DeptZntzckThreeMenuActivity.class);
//			// startActivity(intent);
//			// }
//			// break;
//			default:
//				break;
//			}
//		} else {
//			switch (clickIndex) {
//
//			case SAVE_REGULAR:// 我要存定期
//				if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof SaveRegularActivity)) {
//					ActivityTaskManager.getInstance().removeAllActivity();
//					Intent intent = new Intent();
//					intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
//							SaveRegularActivity.class);
//					startActivity(intent);
//				}
//				break;
//			case MY_REG_SAVE:// 我的定期存款
//				if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof MyRegSaveActivity)) {
//					ActivityTaskManager.getInstance().removeAllActivity();
//					Intent intent = new Intent();
//					intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
//							MyRegSaveActivity.class);
//					startActivity(intent);
//				}
//				break;
//			case 5:// 通知管理
//				if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof NotifyManageActivity)) {
//					ActivityTaskManager.getInstance().removeAllActivity();
//					Intent intent = new Intent();
//					intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
//							NotifyManageActivity.class);
//					startActivity(intent);
//				}
//				break;
//			case 6:// 记录查询
//				if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof QureyRecordManagerActivity)) {
//					ActivityTaskManager.getInstance().removeAllActivity();
//					Intent intent = new Intent();
//					intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
//							QureyRecordManagerActivity.class);
//					startActivity(intent);
//				}
//				break;
//			case 4:// 大额存单
//				if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof LargeCDMenuActivity)) {
//					ActivityTaskManager.getInstance().removeAllActivity();
//					Intent intent = new Intent();
//					intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
//							LargeCDMenuActivity.class);
//					startActivity(intent);
//				}
//				break;
//			case DEPT_DQYDZC_MENU:// 定期约定转存5
//				if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof DeptYdzcQueryActivity)) {
//					ActivityTaskManager.getInstance().removeAllActivity();
//					Intent intent = new Intent();
//					intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
//							DeptYdzcQueryActivity.class);
//					startActivity(intent);
//				}
//				break;
//
//			case 3:// 灵活计息
//				if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof FiexibleInterestActivity)) {
//					ActivityTaskManager.getInstance().removeAllActivity();
//					Intent intent = new Intent();
//					intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
//							FiexibleInterestActivity.class);
//					startActivity(intent);
//				}
//				break;
//			default:
//				break;
//			}
//		}
	}

	/** 请求所有账户列表信息 */
	public void requestAccOutBankAccountList() {
		// 展示通讯框
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.QRY_ALL_BANK_ACCOUNT);
		HttpManager.requestBii(biiRequestBody, this, "accOutBankAccountListCallBack");
	}

	/**
	 * 请求所有账户列表回调
	 *
	 * @param resultObj
	 */
	public void accOutBankAccountListCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		accountList = (List<Map<String, Object>>) (biiResponseBody.getResult());
		DeptDataCenter.getInstance().setAccountList(accountList);
		communicationCallBack(QUERY_ALL_ACCOUNT_CALLBACK);
	}

	/** 我的定期存款 请求转出账户 */
	public void requestTranoutAccountList() {
		// 展示通讯框
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.QRY_ALL_BANK_ACCOUNT);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		List<String> accountList = new ArrayList<String>();
		accountList.add(ConstantGloble.ACC_TYPE_ORD);// 普通活期
		accountList.add(ConstantGloble.ACC_TYPE_BRO);// 借记卡
		accountList.add(ConstantGloble.ACC_TYPE_RAN);// 活期一本通
		paramsmap.put(Comm.ACCOUNT_TYPE, accountList);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestTranoutAccountListCallBack");
	}

	/**
	 * 我的定期存款 请求转出账户 返回
	 *
	 * @param resultObj
	 */
	public void requestTranoutAccountListCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		List<Map<String, Object>> accountOutList = (List<Map<String, Object>>) (biiResponseBody.getResult());
		DeptDataCenter.getInstance().setAccountOutList(accountOutList);
		communicationCallBack(QUERY_ACCOUNT_OUT_CALLBACK);
	}

	/**
	 * 请求账户余额 调用该请求之前 必须要先调用requestAccBankAccountList 成功返回之后
	 *
	 * @param accountId
	 *            账户标识Id
	 */
	public void requestAccBankAccountDetail(String accountId) {
		// 展示通讯框
		BaseHttpEngine.showProgressDialog();
		List<BiiRequestBody> biiRequestBodyList = new ArrayList<BiiRequestBody>();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.QRY_ACC_BALANCE_API);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Comm.ACCOUNT_ID, accountId);
		biiRequestBody.setParams(paramsmap);
		biiRequestBodyList.add(biiRequestBody);
		HttpManager.requestBii(biiRequestBodyList, this, "accBankAccountDetailCallback");

	}

	/**
	 * 请求账户余额回调
	 *
	 * @param resultObj
	 */

	public void accBankAccountDetailCallback(Object resultObj) {
		// 取消通讯框
		BaseHttpEngine.dissMissProgressDialog();

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> callbackmap = (Map<String, Object>) (biiResponseBody.getResult());
		DeptDataCenter.getInstance().setCurDetailContent(callbackmap);
		communicationCallBack(QUERY_ACCOUNT_DETAIL_CALLBACK);
	}

	/** 请求所有转入账户列表信息 */
	public void requestAccInBankAccountList() {

		// 展示通讯框
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.QRY_ALL_BANK_ACCOUNT);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		List<String> accountList = new ArrayList<String>();
		accountList.add(ConstantGloble.ACC_TYPE_REG);// 定期一本通
		accountList.add(ConstantGloble.ACC_TYPE_EDU);// 教育储蓄
		accountList.add(ConstantGloble.ACC_TYPE_ZOR);// 零存整取
		paramsmap.put(Comm.ACCOUNT_TYPE, accountList);
		biiRequestBody.setParams(paramsmap);

		HttpManager.requestBii(biiRequestBody, this, "requestAccInBankAccountListCallBack");
	}

	/**
	 * 请求所有转入账户列表信息 返回
	 *
	 * @param resultObj
	 */
	public void requestAccInBankAccountListCallBack(Object resultObj) {
		// 取消通讯框
		BaseHttpEngine.dissMissProgressDialog();

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> accountInList = (List<Map<String, Object>>) (biiResponseBody.getResult());
		DeptDataCenter.getInstance().setAccountInList(accountInList);
		communicationCallBack(QUERY_ALL_IN_ACCOUNT_CALLBACK);
	}

	/** 建立通知 自动转账 请求所有转入账户列表信息 */
	public void requestAutoInBankAccountList() {
		// 展示通讯框
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.QRY_ALL_BANK_ACCOUNT);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		List<String> accountList = new ArrayList<String>();
		accountList.add(ConstantGloble.ACC_TYPE_BRO);// 借记卡
		accountList.add(ConstantGloble.ACC_TYPE_RAN);// 活期一本通
		paramsmap.put(Comm.ACCOUNT_TYPE, accountList);
		biiRequestBody.setParams(paramsmap);

		HttpManager.requestBii(biiRequestBody, this, "requestAutoInBankAccountListCallBack");
	}

	/**
	 * 建立通知 自动转账 请求所有转入账户列表信息 返回
	 *
	 * @param resultObj
	 */
	public void requestAutoInBankAccountListCallBack(Object resultObj) {
		// 取消通讯框
		BaseHttpEngine.dissMissProgressDialog();

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> accountInList = (List<Map<String, Object>>) (biiResponseBody.getResult());
		DeptDataCenter.getInstance().setAccountInList(accountInList);
		communicationCallBack(QUERY_ALL_IN_ACCOUNT_CALLBACK);
	}

	/**
	 * 请求新增整取整取
	 *
	 * @param fromAccountId
	 *            转出账户号
	 * @param toAccountId
	 *            转入账户号
	 * @param currency
	 *            币种
	 * @param cashRemit
	 *            钞汇标志
	 * @param cDTerm
	 *            存期
	 * @param amount
	 *            转账金额
	 * @param memo
	 *            附言
	 * @param executeType
	 *            执行方式
	 * @param executeDate
	 *            预约执行日期
	 * @param startDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @param cycleSelect
	 *            执行周期
	 */
	public void requestNewWhoelSaveAndCheckout(String fromAccountId, String toAccountId, String currency, String cashRemit,
											   String cDTerm, String amount, String memo, String executeType, String executeDate, String startDate,
											   String endDate, String cycleSelect, String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.REQUEST_NEW_WHOLE_SAVE_AND_CHECKOUT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		if(amount.contains(",")){
			amount = amount.replace(",", "");
		}
		map.put(Dept.FROM_ACCOUNTID, fromAccountId);
		map.put(Dept.TO_ACCOUNTID, toAccountId);
		map.put(Dept.CURRENCY, currency);
		map.put(Dept.CASHREMIT, cashRemit);
		map.put(Dept.CD_TERM, cDTerm);
		map.put(Dept.AMOUNT, amount);
		map.put(Dept.MEMO, memo);
		map.put(Dept.EXECUTE_TYPE, executeType);
		map.put(Dept.EXECUTE_DATE, executeDate);
		map.put(Dept.START_DATE, startDate);
		map.put(Dept.END_DATE, endDate);
		map.put(Dept.CYCLE_SELECT, cycleSelect);
		map.put(Comm.TOKEN_REQ, token);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestNewWhoelSaveAndCheckoutCallBack");
	}

	/**
	 * 请求新增整取整取 回调
	 *
	 */
	public void requestNewWhoelSaveAndCheckoutCallBack(Object resultObj) {
		// 取消通讯框 TODO
		BaseHttpEngine.dissMissProgressDialog();

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		DeptDataCenter.getInstance().setRequestCallBackMap(resultMap);
		communicationCallBack(WHOLE_SAVE_CALLBACK);
	}

	/**
	 * 请求定活两便
	 *
	 * @param fromAccountId
	 *            转出账户号
	 * @param toAccountId
	 *            转入账户号
	 * @param currency
	 *            币种
	 * @param amount
	 *            转账金额
	 * @param memo
	 *            附言
	 * @param executeType
	 *            执行方式
	 * @param executeDate
	 *            预约执行日期
	 * @param startDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 * @param cycleSelect
	 *            执行周期
	 */
	public void requestNewRegAndRandom(String fromAccountId, String toAccountId, String currency, String amount,
									   String memo, String executeType, String executeDate, String startDate, String endDate, String cycleSelect,
									   String token) {

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.REQUEST_NEW_REG_AND_RANDOW);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Dept.NEW_REG_AND_RANDOW_FROM_ACCOUNTID, fromAccountId);
		map.put(Dept.NEW_REG_AND_RANDOW_TO_ACCOUNTID, toAccountId);
		map.put(Dept.NEW_REG_AND_RANDOW_CURRENCY, currency);
		map.put(Dept.NEW_REG_AND_RANDOW_AMOUNT, amount);
		map.put(Dept.NEW_REG_AND_RANDOW_MEMO, memo);
		map.put(Dept.NEW_REG_AND_RANDOW_EXECUTE_TYPE, executeType);
		map.put(Dept.NEW_REG_AND_RANDOW_EXECUTE_DATE, executeDate);
		map.put(Dept.NEW_REG_AND_RANDOW_START_DATE, startDate);
		map.put(Dept.NEW_REG_AND_RANDOW_END_DATE, endDate);
		map.put(Dept.NEW_REG_AND_RANDOW_CYCLE_SELECT, cycleSelect);
		map.put(Comm.TOKEN_REQ, token);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestNewRegAndRandomCallBack");
	}

	/**
	 * 请求定活两便 回调
	 *
	 * @param resultObj
	 *            返回结果
	 */
	public void requestNewRegAndRandomCallBack(Object resultObj) {
		// 取消通讯框
		BaseHttpEngine.dissMissProgressDialog();

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		DeptDataCenter.getInstance().setRequestCallBackMap(resultMap);
		communicationCallBack(REGULAR_RANDOM_CALLBACK);
	}

	/**
	 * 请求新增通知存款
	 *
	 * @param fromAccountId
	 *            转出账户号
	 * @param toAccountId
	 *            转入账户号
	 * @param businessType
	 *            业务品种
	 * @param convertType
	 *            转存方式
	 * @param currency
	 *            币种
	 * @param cashRemit
	 *            钞汇标识
	 * @param amount
	 *            转账金额
	 * @param transMode
	 *            执行类型
	 * @param memo
	 *            备注
	 * @param date
	 *            执行日期
	 * @param frequency
	 *            周期
	 * @param startDate
	 *            起始日期
	 * @param endDate
	 *            结束日期
	 */
	public void requestNewNotifySave(String fromAccountId, String toAccountId, String businessType, String convertType,
									 String currency, String cashRemit, String amount, String transMode, String memo, String date, String frequency,
									 String startDate, String endDate, String token) {

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.REQUEST_NEW_NOTIFY_SAVE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Dept.NEW_NOTIFY_SAVE_FROM_ACCOUNTID, fromAccountId);
		map.put(Dept.NEW_NOTIFY_SAVE_TO_ACCOUNTID, toAccountId);
		map.put(Dept.NEW_NOTIFY_SAVE_BUSINESS_TYPE, businessType);
		map.put(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE, convertType);
		map.put(Dept.NEW_NOTIFY_SAVE_CURRENCY, currency);
		map.put(Dept.NEW_NOTIFY_SAVE_CASHREMIT, cashRemit);
		map.put(Dept.NEW_NOTIFY_SAVE_AMOUNT, amount);
		map.put(Dept.NEW_NOTIFY_SAVE_TRANSMODE, transMode);
		map.put(Dept.NEW_NOTIFY_SAVE_MEMO, memo);
		map.put(Dept.NEW_NOTIFY_SAVE_DATE, date);
		map.put(Dept.NEW_NOTIFY_SAVE_FREQUENCY, frequency);
		map.put(Dept.NEW_NOTIFY_SAVE_START_DATE, startDate);
		map.put(Dept.NEW_NOTIFY_SAVE_END_DATE, endDate);
		map.put(Comm.TOKEN_REQ, token);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestNewNotifySaveCallBack");
	}

	/**
	 * 请求通知存款 回调
	 *
	 * @param resultObj
	 *            返回结果
	 */
	public void requestNewNotifySaveCallBack(Object resultObj) {
		// 取消通讯框
		BaseHttpEngine.dissMissProgressDialog();

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		DeptDataCenter.getInstance().setRequestNotifySaveCallBackMap(resultMap);
		communicationCallBack(NOTIFY_SAVE_CALLBACK);
	}

	/**
	 * 请求定期存单详情PsnAccountQuerySubAccountDetail
	 *
	 * @param accountId
	 */
	public void requestAccountQuerySubAccountDetail(String accountId, String volumeNumber, String cdNumber) { //
		List<BiiRequestBody> biiRequestBodyList = new ArrayList<BiiRequestBody>();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.REQUEST_QUERY_SUBACCOUNTDETAIL);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Comm.ACCOUNT_ID, accountId);
		paramsmap.put(Dept.VOLUME_NUMBER, volumeNumber);
		paramsmap.put(Dept.CD_NUMBER, cdNumber);
		biiRequestBody.setParams(paramsmap);
		biiRequestBodyList.add(biiRequestBody);
		HttpManager.requestBii(biiRequestBodyList, this, "requestAccountQuerySubAccountDetailCallback");
	}

	/**
	 * 请求定期存单详情---返回
	 *
	 * @param resultObj
	 */
	public void requestAccountQuerySubAccountDetailCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		DeptDataCenter.getInstance().setRequestMyRegDetailCallBackMap(resultMap);
		communicationCallBack(QUERY_MYREG_DETAIL_CALLBACK);
	}

	/** 查询账户列表 */
	public void requestQueryMyRegAccountList() {
		// 展示通讯框
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.QRY_ALL_BANK_ACCOUNT);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		List<String> accountList = new ArrayList<String>();
		accountList.add(ConstantGloble.ACC_TYPE_REG);// 定期一本通
		accountList.add(ConstantGloble.ACC_TYPE_EDU);// 教育储蓄
		accountList.add(ConstantGloble.ACC_TYPE_ZOR);// 零存整取
		// accountList.add("140");//存本取息
		paramsmap.put(Comm.ACCOUNT_TYPE, accountList);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestQueryMyRegAccountListCallBack");
	}

	/**
	 * 查询账户列表 返回------
	 *
	 * @param resultObj
	 */
	public void requestQueryMyRegAccountListCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		accountList = (List<Map<String, Object>>) (biiResponseBody.getResult());
		// 根据账户类型 将列表数据分类储蓄 定期一本通数据
		List<Map<String, Object>> wholesaveAccountList = new ArrayList<Map<String, Object>>();
		// 根据账户类型 将列表数据分类储蓄 教育储蓄
		List<Map<String, Object>> educationAccountList = new ArrayList<Map<String, Object>>();
		// 根据账户类型 将列表数据分类储蓄 零存整取
		List<Map<String, Object>> zerosaveAccountList = new ArrayList<Map<String, Object>>();
		// 定期一本通
		for (int i = 0; i < accountList.size(); i++) {
			if (accountList.get(i).get(Comm.ACCOUNT_TYPE).equals(ConstantGloble.ACC_TYPE_REG)) {// 定期一本通卡
				wholesaveAccountList.add(accountList.get(i));
			} else if (accountList.get(i).get(Comm.ACCOUNT_TYPE).equals(ConstantGloble.ACC_TYPE_EDU)) {// 教育储蓄
				educationAccountList.add(accountList.get(i));
			} else if (accountList.get(i).get(Comm.ACCOUNT_TYPE).equals(ConstantGloble.ACC_TYPE_ZOR)) {// 零存整取
				zerosaveAccountList.add(accountList.get(i));
			}
		}

		DeptDataCenter.getInstance().setWholesaveAccountList(wholesaveAccountList);
		DeptDataCenter.getInstance().setEducationAccountList(educationAccountList);
		DeptDataCenter.getInstance().setZerosaveAccountList(zerosaveAccountList);
		DeptDataCenter.getInstance().setMyRegAccountList(accountList);
		communicationCallBack(QUERY_ALL_ACCOUNT_CALLBACK);
	}

	/**
	 * 我要存定期 转出账户是定期一本通的时候 查询转入账户
	 *
	 * 转入账户选择范围（借记卡、普通活期、活期一本通、长城信用卡）
	 *
	 *
	 * 转入账户是定期教育储蓄的时候 转出账户选择范围（借记卡、普通活期、活期一本通、长城信用卡）
	 */
	public void requestForWholeTranInList() {
		// 展示通讯框
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.QRY_ALL_BANK_ACCOUNT);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		List<String> accountList = new ArrayList<String>();
		accountList.add(ConstantGloble.ACC_TYPE_BRO);
		accountList.add(ConstantGloble.ACC_TYPE_ORD);
		accountList.add(ConstantGloble.ACC_TYPE_RAN);
		accountList.add(ConstantGloble.ACC_TYPE_GRE);
		paramsmap.put(Comm.ACCOUNT_TYPE, accountList);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestForWholeTranInListCallBack");
	}

	/**
	 * 我要存定期 转出账户是定期一本通的时候 查询转入账户(403:QDII2可作为支取定期的转入账户)
	 *
	 * 转入账户选择范围（借记卡、普通活期、活期一本通、长城信用卡）
	 *
	 *
	 * 转入账户是定期教育储蓄的时候 转出账户选择范围（借记卡、普通活期、活期一本通、长城信用卡）
	 */
	public void requestForWholeTranInNewList() {
		// 展示通讯框
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.QRY_ALL_BANK_ACCOUNT);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		List<String> accountList = new ArrayList<String>();
		accountList.add(ConstantGloble.ACC_TYPE_BRO);
		accountList.add(ConstantGloble.ACC_TYPE_ORD);
		accountList.add(ConstantGloble.ACC_TYPE_RAN);
		accountList.add(ConstantGloble.ACC_TYPE_GRE);
		// 优汇通账户
		// accountList.add(AccBaseActivity.YOUHUITONGZH);
		paramsmap.put(Comm.ACCOUNT_TYPE, accountList);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestForWholeTranInListCallBack");
	}

	/**
	 * 查询账户列表 返回------
	 *
	 * @param resultObj
	 */
	public void requestForWholeTranInListCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> tranoutList = (List<Map<String, Object>>) biiResponseBody.getResult();
		DeptDataCenter.getInstance().setTranoutAccountList(tranoutList);
		communicationCallBack(QUERY_ACCOUNT_IN_CALLBACK);
	}

	/**
	 * 根据用户accountId 查询账户详情
	 */
	public void requestQueryAccountDetail(String accountId) {
		// 展示通讯框
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.QRY_ACC_BALANCE_API);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Comm.ACCOUNT_ID, accountId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestQueryAccountDetailCallBack");
	}

	/**
	 * 查询账户详情返回
	 *
	 * @param resultObj
	 */
	public void requestQueryAccountDetailCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		// 将数据放入数据中心
		DeptDataCenter.getInstance().setAccountDetailCallBackMap(resultMap);
		communicationCallBack(QUERY_ACCOUNT_DETAIL_CALLBACK);
	}

	/**
	 * 支取整存整取 PsnTransDepositWithdrawalSubmitAction // request key--------
	 * fromAccountId toAccountId currency amount cashRemit remark;// 备注
	 * passBookNumber;// 存折序号 cdNumber transMode executeDate token
	 */
	public void requestCheckoutWholeSave(String fromAccountId, String toAccountId, String currency, String amount,
										 String cashRemit, String remark, String passBookNumber, String cdNumber, String transMode, String executeDate,
										 String token, String appointedAmount) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.REQUEST_CHECKOUT_WHOLE_SAVE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Dept.FROM_ACCOUNTID, fromAccountId);
		paramsmap.put(Dept.TO_ACCOUNTID, toAccountId);
		paramsmap.put(Dept.CURRENCY, currency);
		paramsmap.put(Dept.AMOUNT, amount);
		paramsmap.put(Dept.CASHREMIT, cashRemit);
		// add by luqp 上送字段MEMO 修改为REMARK
		paramsmap.put(Dept.REMARK, remark);
		paramsmap.put(Dept.PASSBOOK_NUMBER, passBookNumber);
		paramsmap.put(Dept.CD_NUMBER, cdNumber);
		paramsmap.put(Dept.NEW_NOTIFY_SAVE_TRANSMODE, transMode);
		paramsmap.put(Dept.EXECUTE_DATE, executeDate);
		paramsmap.put(Comm.TOKEN_REQ, token);
		paramsmap.put(Dept.APPOINTED_AMOUNT, appointedAmount);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestCheckoutWholeSaveCallBack");
	}

	public void requestCheckoutWholeSaveCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		DeptDataCenter.getInstance().setCheckoutCallBackMap(resultMap);
		communicationCallBack(SAVE_WHOLE_CHECKOU_CALLLBAK);
	}

	/**
	 * 支取定活两便 PsnTransDepositWithdrawalSubmitAction // request key--------
	 * fromAccountId toAccountId currency amount cashRemit remark;// 备注
	 * passBookNumber;// 存折序号 cdNumber transMode executeDate token
	 */
	public void requestCheckoutRegAndRandom(String fromAccountId, String toAccountId, String currency, String amount,
											String cashRemit, String remark, String passBookNumber, String cdNumber, String transMode, String executeDate,
											String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.REQUEST_CHECKOUT_REGULAR_AND_RANDOM);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Dept.FROM_ACCOUNTID, fromAccountId);
		paramsmap.put(Dept.TO_ACCOUNTID, toAccountId);
		paramsmap.put(Dept.CURRENCY, currency);
		paramsmap.put(Dept.AMOUNT, amount);
		paramsmap.put(Dept.CASHREMIT, cashRemit);
		// add by luqp 上送字段MEMO 修改为REMARK
		paramsmap.put(Dept.REMARK, remark);
		paramsmap.put(Dept.PASSBOOK_NUMBER, passBookNumber);
		paramsmap.put(Dept.CD_NUMBER, cdNumber);
		paramsmap.put(Dept.EXECUTE_DATE, executeDate);
		paramsmap.put(Dept.EXECUTE_TYPE, transMode);
		paramsmap.put(Comm.TOKEN_REQ, token);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestCheckoutRegAndRandomCallBack");
	}

	public void requestCheckoutRegAndRandomCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		DeptDataCenter.getInstance().setCheckoutCallBackMap(resultMap);
		communicationCallBack(SAVE_WHOLE_CHECKOU_CALLLBAK);
	}

	/**
	 * 支取通知存款 PsntransCallDepositToCurrentSaving
	 *
	 * @param fromAccountId
	 * @param toAccountId
	 * @param transMode
	 * @param cDType
	 *            存款种类
	 * @param currency
	 * @param amount
	 * @param cashRemit
	 * @param cdTerm
	 *            存期 1天 或者 7天
	 * @param convertType
	 *            转存方式
	 * @param memo
	 * @param passBookNumber
	 * @param cdNumber
	 * @param scheduled
	 * @param scheduleNumber
	 * @param callDepositBalance
	 * @param token
	 */
	public void requestCheckoutNotifySave(String fromAccountId, String toAccountId, String transMode, String cDType,
										  String currency, String amount, String cashRemit, String cdTerm, String convertType, String memo,
										  String passBookNumber, String cdNumber, String scheduled, String scheduleNumber, String callDepositBalance,
										  String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.REQUEST_CHECKOUT_NOTIFY_SAVE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Dept.FROM_ACCOUNTID, fromAccountId);
		paramsmap.put(Dept.TO_ACCOUNTID, toAccountId);
		paramsmap.put(Dept.NEW_NOTIFY_SAVE_TRANSMODE, transMode);
		paramsmap.put(Dept.CDTYPE, cDType);
		paramsmap.put(Dept.CURRENCY, currency);
		paramsmap.put(Dept.AMOUNT, amount);
		paramsmap.put(Dept.CASHREMIT, cashRemit);
		paramsmap.put(Dept.CD_TERM, cdTerm);
		paramsmap.put(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE, cdTerm);
		paramsmap.put(Dept.MEMO, memo);
		paramsmap.put(Dept.PASSBOOK_NUMBER, passBookNumber);
		paramsmap.put(Dept.CD_NUMBER, cdNumber);
		paramsmap.put(Dept.SCHEDULED, scheduled);
		paramsmap.put(Dept.SCHEDULE_NUMBER, scheduleNumber);
		paramsmap.put(Dept.CALLDEPOSIT_BALANCE, callDepositBalance);
		paramsmap.put(Comm.TOKEN_REQ, token);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestCheckoutNotifySaveCallBack");
	}

	public void requestCheckoutNotifySaveCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		DeptDataCenter.getInstance().setCheckoutCallBackMap(resultMap);
		communicationCallBack(NOTIFY_SAVE_CHECKOUT_CALLBACK);
	}

	/**
	 * 教育储蓄 续存
	 *
	 * @param fromAccountId
	 * @param toAccountId
	 * @param amount
	 * @param remark
	 * @param transMode
	 * @param executeDate
	 * @param cycleSelect
	 * @param startDate
	 * @param endDate
	 */
	public void requestExtendEducation(String fromAccountId, String toAccountId, String amount, String remark,
									   String transMode, String executeDate, String cycleSelect, String startDate, String endDate, String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.REQUEST_EXTEND_EDUCATION);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Dept.FROM_ACCOUNTID, fromAccountId);
		paramsmap.put(Dept.TO_ACCOUNTID, toAccountId);
		paramsmap.put(Dept.AMOUNT, amount);
		// add by luqp 上送字段MEMO 修改为REMARK
		paramsmap.put(Dept.REMARK, remark);
		paramsmap.put(Dept.NEW_NOTIFY_SAVE_TRANSMODE, transMode);
		paramsmap.put(Dept.NEW_NOTIFY_SAVE_ASSIGNEDDATE, executeDate);
		paramsmap.put(Dept.NEW_NOTIFY_SAVE_CYCLE_SELECT, cycleSelect);
		paramsmap.put(Dept.START_DATE, startDate);
		paramsmap.put(Dept.END_DATE, endDate);
		paramsmap.put(Comm.TOKEN_REQ, token);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestExtendEducationCallBack");
	}

	public void requestExtendEducationCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		DeptDataCenter.getInstance().setCheckoutCallBackMap(resultMap);
		communicationCallBack(EXTEND_EDUCATION_CALLLBAK);
	}

	/**
	 * 零存整取 续存
	 *
	 * @param fromAccountId
	 * @param toAccountId
	 * @param amount
	 * @param remark
	 * @param transMode
	 * @param executeDate
	 * @param cycleSelect
	 * @param startDate
	 * @param endDate
	 */
	public void requestExtendZeroWhole(String fromAccountId, String toAccountId, String amount, String remark,
									   String transMode, String executeDate, String cycleSelect, String startDate, String endDate, String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.REQUEST_EXTEND_ZORE_WHOLE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Dept.FROM_ACCOUNTID, fromAccountId);
		paramsmap.put(Dept.TO_ACCOUNTID, toAccountId);
		paramsmap.put(Dept.AMOUNT, amount);
		// add by luqp 上送字段MEMO 修改为REMARK
		paramsmap.put(Dept.REMARK, remark);
		paramsmap.put(Dept.NEW_NOTIFY_SAVE_TRANSMODE, transMode);
		paramsmap.put(Dept.NEW_NOTIFY_SAVE_ASSIGNEDDATE, executeDate);
		paramsmap.put(Dept.NEW_NOTIFY_SAVE_CYCLE_SELECT, cycleSelect);
		paramsmap.put(Dept.START_DATE, startDate);
		paramsmap.put(Dept.END_DATE, endDate);
		paramsmap.put(Comm.TOKEN_REQ, token);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestExtendZeroWholeCallBack");
	}

	public void requestExtendZeroWholeCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		DeptDataCenter.getInstance().setCheckoutCallBackMap(resultMap);
		communicationCallBack(EXTEND_EDUCATION_CALLLBAK);
	}

	/**
	 * 请求建立通知
	 *
	 * @param fromAccountId
	 * @param volumeNumber
	 * @param cdNumber
	 * @param cdTerm
	 * @param redrawType
	 * @param toAccountId
	 * @param drawMode
	 * @param drawAmount
	 * @param currency
	 * @param convertType
	 * @param cashRemit
	 * @param depositBalance
	 * @param drawDate
	 * @param token
	 */
	public void requestCreateNotice(String fromAccountId, String volumeNumber, String cdNumber, String cdTerm,
									String redrawType, String toAccountId, String drawMode, String drawAmount, String currency, String convertType,
									String cashRemit, String depositBalance, String drawDate, String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.REQUEST_CREATE_NOTIFY);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Dept.FROM_ACCOUNTID, fromAccountId);
		paramsmap.put(Dept.VOLUME_NUMBER, volumeNumber);
		paramsmap.put(Dept.CD_NUMBER, cdNumber);
		paramsmap.put(Dept.DEPOSIT_TYPE, cdTerm);
		paramsmap.put(Dept.REDRAW_TYPE, redrawType);
		paramsmap.put(Dept.TO_ACCOUNTID, toAccountId);
		paramsmap.put(Dept.DRAW_MODE, drawMode);
		paramsmap.put(Dept.DRAW_AMOUNT, drawAmount);
		paramsmap.put(Dept.CURRENCY, currency);
		paramsmap.put(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE, convertType);
		paramsmap.put(Dept.CASHREMIT, cashRemit);
		paramsmap.put(Dept.DEPOSIT_BALANCE, depositBalance);
		paramsmap.put(Dept.DRAW_DATE, drawDate);
		paramsmap.put(Comm.TOKEN_REQ, token);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestCreateNoticeCallBack");
	}

	public void requestCreateNoticeCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		communicationCallBack(CREATE_NOTICE_CALLLBAK);
	}

	/** 通知管理 查询账户列表 */
	public void requestQueryNotifyAccountList() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.QRY_ALL_BANK_ACCOUNT);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		List<String> accountList = new ArrayList<String>();
		accountList.add(ConstantGloble.ACC_TYPE_REG);// 定期一本通
		paramsmap.put(Comm.ACCOUNT_TYPE, accountList);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestQueryNotifyAccountListCallBack");
	}

	/**
	 * 查询账户列表 返回------
	 *
	 * @param resultObj
	 */
	public void requestQueryNotifyAccountListCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		accountList = (List<Map<String, Object>>) (biiResponseBody.getResult());
		DeptDataCenter.getInstance().setAccountList(accountList);
		communicationCallBack(QUERY_ALL_ACCOUNT_CALLBACK);
	}

	/**
	 * 根据用户accountId 查询账户详情
	 */
	public void requestQueryNotifyAccountDetail(String accountId) {
		// 展示通讯框
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.QRY_ACC_BALANCE_API);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Comm.ACCOUNT_ID, accountId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestQueryNotifyAccountDetailCallBack");
	}

	/**
	 * 查询账户详情返回
	 *
	 * @param resultObj
	 */
	public void requestQueryNotifyAccountDetailCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		// 将数据放入数据中心
		DeptDataCenter.getInstance().setCurDetailContent(resultMap);
		communicationCallBack(QUERY_ACCOUNT_DETAIL_CALLBACK);
	}

	/**
	 * 查询 通知详情
	 */
	public void requestQueryNotify(String accountId, String volumeNumber, String cdNumber) {
		// 展示通讯框
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.REQUEST_QUERY_NOTIFY);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Comm.ACCOUNT_ID, accountId);
		paramsmap.put(Dept.VOLUME_NUMBER, volumeNumber);
		paramsmap.put(Dept.CD_NUMBER, cdNumber);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestQueryNotifyCallBack");
	}

	public void requestQueryNotifyCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		List<Map<String, Object>> resultList = (List<Map<String, Object>>) biiResponseBody.getResult();
		// 将数据放入数据中心
		DeptDataCenter.getInstance().setQueryNotifyCallBackList(resultList);
		communicationCallBack(QUERY_NOTIFY_ACCOUNT_DETAIL_CALLBACK);
	}

	/**
	 * 撤销通知
	 */
	public void requestDeleteNotify(String accountId, String volumeNumber, String cdNumber, String notifyId,
									String currency, String drawMode, String drawAmount, String drawDate, String status, String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.REQUEST_DELETE_NOTIFY);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Comm.ACCOUNT_ID, accountId);
		paramsmap.put(Dept.VOLUME_NUMBER, volumeNumber);
		paramsmap.put(Dept.CD_NUMBER, cdNumber);
		paramsmap.put(Dept.NOTIFY_ID, notifyId);
		paramsmap.put(Dept.CURRENCY, currency);
		paramsmap.put(Dept.DRAW_MODE, drawMode);
		paramsmap.put(Dept.DRAW_AMOUNT, drawAmount);
		paramsmap.put(Dept.DRAW_DATE, drawDate);
		paramsmap.put(Dept.STATUS, status);
		paramsmap.put(Comm.TOKEN_REQ, token);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestDeleteNotifyCallBack");
	}

	public void requestDeleteNotifyCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		// 将数据放入数据中心
		communicationCallBack(DELETE_NOTIFY_CALLBACK);
	}

	/** 请求修改账户别名 */
	public void requestModifyAccountAlias(String accountId, String nickName, String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.ACC_MODIFYACCOUNT_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Acc.MODIFY_ACC_ACCOUNTID_REQ, accountId);
		paramsmap.put(Acc.MODIFY_ACC_ACCOUNTNICKNAME_REQ, nickName);
		paramsmap.put(Acc.MODIFY_ACC_TOKEN_REQ, token);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "modifyAccountAliasCallBack");
	}

	public void modifyAccountAliasCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String result = (String) biiResponseBody.getResult();
		communicationCallBack(MODIFY_NICKNAME_CALLBACK);
	}

	/**
	 * 通讯回调处理
	 *
	 * @param flag
	 *            通讯标示
	 */
	public void communicationCallBack(int flag) {
		// 子类进行覆盖
		// 根据不同的通讯 处理不同的回调
	}

	/**
	 * 关联账户调转
	 */
	protected void goRelevanceAccount() {
//		Intent intent = new Intent();
//		intent.setClass(this, AccInputRelevanceAccountActivity.class);
//		startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
		BusinessModelControl.gotoAccRelevanceAccount(this, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE, null);
	}

	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = response.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
			BiiError biiError = biiResponseBody.getError();
			if (null != biiError) {
				if (Dept.REQUEST_QUERY_NOTIFY.equals(biiResponseBody.getMethod())) {
					if ("ProductException".equals(biiError.getType()) && "BANCS.0188".equals(biiError.getCode())
							&& "未搜索到符合条件的记录".equals(biiError.getMessage())) {
						BiiHttpEngine.dissMissProgressDialog();
						return false;
					}
				}
			}
		}
		return super.httpRequestCallBackPre(resultObj);
	}

	/** 查询定期账户列表 */
	public void requestPsnCommonQueryAllChinaBankAccount(String[] s) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.QRY_ALL_BANK_ACCOUNT);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Comm.ACCOUNT_TYPE, s);
		biiRequestBody.setParams(params);
		biiRequestBody.setConversationId(null);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnCommonQueryAllChinaBankAccountCallBack");
	}

	/** 查询定期账户列表--回调 */
	public void requestPsnCommonQueryAllChinaBankAccountCallBack(Object resultObj) {

	}

	/** 查询账户详情接口 */
	public void requsetPsnAccountQueryAccountDetail(String accountId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.QRY_ACC_BALANCE_API);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Dept.DEPT_ACC_ACCOUNTID_REQ, accountId);
		biiRequestBody.setConversationId(null);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requsetPsnAccountQueryAccountDetailCallback");

	}

	/** 查询账户详情---回调 */
	public void requsetPsnAccountQueryAccountDetailCallback(Object resultObj) {

	}

	/**
	 * 定期约定转存查询-----接口
	 *
	 * @param accountId
	 *            ：账户ID
	 * @param volumeNumber
	 *            ：存折册号
	 * @param cdNumber
	 *            ：存单号
	 */
	public void requsetPsnTimeDepositAppointQuery(String accountId, String volumeNumber, String cdNumber) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.DEPT_PSNTIMEDEPOSITAPPOINTQUERY_API);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Dept.DEPT_ACC_ACCOUNTID_REQ, accountId);
		paramsmap.put(Dept.DEPT_VOLUMENUMBER_RES, volumeNumber);
		paramsmap.put(Dept.DEPT_CDNUMBER_RES, cdNumber);
		biiRequestBody.setParams(paramsmap);
		biiRequestBody.setConversationId(null);
		HttpManager.requestBii(biiRequestBody, this, "requsetPsnTimeDepositAppointQueryCallback");

	}

	/** 定期约定转存查询-----回调 */
	public void requsetPsnTimeDepositAppointQueryCallback(Object resultObj) {

	}

	/**
	 * 智能通签约解约记录查询-----接口
	 *
	 * @param accountId
	 *            :账号id
	 * @param pageSize
	 *            :页面大小
	 * @param currentIndex
	 *            :当前页索引
	 * @param refresh
	 *            :刷新标识
	 */
	public void requsetPsnIntelligentNoticeDepositQuery(String accountId, int currentIndex, boolean refresh) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.DEPT_PSNINTELLIGENTNOTICEDEPOSITQUERY_API);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Dept.DEPT_ACC_ACCOUNTID_REQ, accountId);
		paramsmap.put(Dept.DEPT_CURRENCY_REQ, ConstantGloble.PRMS_CURRENCYCODE_RMB);
		paramsmap.put(Dept.DEPT_CASHREMIT_RES, ConstantGloble.ZERO);
		paramsmap.put(Dept.PAGE_SIZE, ConstantGloble.FOREX_PAGESIZE);
		paramsmap.put(Dept.CURRENT_INDEX, String.valueOf(currentIndex));
		paramsmap.put(Dept.REFRESH, String.valueOf(refresh));
		biiRequestBody.setParams(paramsmap);
		String conversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		biiRequestBody.setConversationId(conversationId);
		HttpManager.requestBii(biiRequestBody, this, "requsetPsnIntelligentNoticeDepositQueryCallback");

	}

	/** 智能通签约解约记录查询-----回调 */
	public void requsetPsnIntelligentNoticeDepositQueryCallback(Object resultObj) {

	}

	// ///////////////存款管理新增手续费试算费用start/////////////////////////////////////
	/**
	 * 定期存款-新增整存整取费用试算
	 *
	 * @param fromAccountId
	 *            :账号ID
	 * @param toAccountId
	 *            :转入账户ID
	 * @param currency
	 *            :币种
	 * @param cashRemit
	 *            :钞汇
	 * @param cDTerm
	 *            :存期
	 * @param amount
	 *            :金额
	 * @param memo
	 *            :备注
	 * @param executeType
	 *            :执行方式
	 */

	public void requsetPsnLumpsumTimeDepositComChge(String fromAccountId, String toAccountId, String currency,
													String cashRemit, String cDTerm, String amount, String memo, String executeType) {
		// TODO Auto-generated method stub
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.Save_PsnLumpsumTimeDepositComChge);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Dept.FROM_ACCOUNTID, fromAccountId);
		map.put(Dept.TO_ACCOUNTID, toAccountId);
		map.put(Dept.CURRENCY, currency);
		map.put(Dept.CASHREMIT, cashRemit);
		map.put(Dept.CD_TERM, cDTerm);
		map.put(Dept.AMOUNT, amount);
		map.put(Dept.MEMO, memo);
		map.put(Dept.EXECUTE_TYPE, executeType);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requsetPsnLumpsumTimeDepositComChgeCallback");
	}

	/** 新增整存整取费用试算-----回调 */
	public void requsetPsnLumpsumTimeDepositComChgeCallback(Object resultObj) {
		// 取消通讯框
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		Map<String, Object> resultList = (Map<String, Object>) biiResponseBody.getResult();
		// 将数据放入数据中心
		DeptDataCenter.getInstance().setCostCalculationMap(resultList);
		communicationCallBack(WHOLE_SAVE_CALLBACK);
	}

	/**
	 * 定活两便 手续费试算
	 *
	 * @param fromAccountId
	 *            :账号ID
	 * @param toAccountId
	 *            :转入账户ID
	 * @param currency
	 *            :币种
	 * @param cashRemit
	 *            :钞汇
	 * @param cDTerm
	 *            :存期
	 * @param amount
	 *            :金额
	 * @param memo
	 *            :备注
	 * @param executeType
	 *            :执行方式
	 * */
	public void requsetPsnConsolidatedTimeAndSavingsComChge(String fromAccountId, String toAccountId, String currency,
															String cashRemit, String cDTerm, String amount, String memo, String executeType) {
		// TODO Auto-generated method stub
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.Save_PsnConsolidatedTimeAndSavingsComChge);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Dept.FROM_ACCOUNTID, fromAccountId);
		map.put(Dept.TO_ACCOUNTID, toAccountId);
		map.put(Dept.CURRENCY, currency);
		map.put(Dept.CASHREMIT, cashRemit);
		map.put(Dept.CD_TERM, cDTerm);
		map.put(Dept.AMOUNT, amount);
		map.put(Dept.MEMO, memo);
		map.put(Dept.EXECUTE_TYPE, executeType);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requsetPsnConsolidatedTimeAndSavingsComChgeCallback");
	}

	/** 定活两便 手续费试算-----回调 */
	public void requsetPsnConsolidatedTimeAndSavingsComChgeCallback(Object resultObj) {
		// 取消通讯框
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) biiResponseBody.getResult();
		DeptDataCenter.getInstance().setRegulaRandomMap(result);
		communicationCallBack(REGULAR_RANDOM_CALLBACK);
	}

	/**
	 * 七天通知存款&一天通知存款 手续费试算
	 *
	 * @param fromAccountId
	 *            转出账户ID
	 * @param toAccountId
	 *            转入账户ID
	 * @param businessType
	 *            业务品种
	 * @param convertType
	 *            转存方式
	 * @param currency
	 *            币种
	 * @param cashRemit
	 *            钞汇
	 * @param amount
	 *            金额
	 * @param memo
	 *            备注
	 * @param transMode
	 *            执行类型
	 */
	public void requsetPsnCurrentSavingToCallDepositComChge(String fromAccountId, String toAccountId, String businessType,
															String convertType, String currency, String cashRemit, String amount, String memo, String transMode) {
		// TODO Auto-generated method stub
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.Save_PsnCurrentSavingToCallDepositComChge);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Dept.FROM_ACCOUNTID, fromAccountId);
		map.put(Dept.TO_ACCOUNTID, toAccountId);
		map.put(Dept.NEW_NOTIFY_SAVE_BUSINESS_TYPE, businessType);
		map.put(Dept.DEPT_CONVERTTYPE_RES, convertType);
		map.put(Dept.CURRENCY, currency);
		map.put(Dept.CASHREMIT, cashRemit);
		map.put(Dept.AMOUNT, amount);
		map.put(Dept.MEMO, memo);
		map.put(Dept.NEW_NOTIFY_SAVE_TRANSMODE, transMode);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requsetPsnCurrentSavingToCallDepositComChgeCallback");
	}

	/** 七天通知存款&一天通知存款 手续费试算-----回调 */
	public void requsetPsnCurrentSavingToCallDepositComChgeCallback(Object resultObj) {

	}

	/**
	 * 支取整存整取费用试算
	 *
	 * @param fromAccountId
	 *            转出账户ID
	 * @param toAccountId
	 *            转入账户ID
	 * @param currency
	 *            币种
	 * @param cashRemit
	 *            钞汇标志
	 * @param amount
	 *            转账金额
	 * @param appointedAmount
	 *            原存单金额
	 * @param memo
	 *            备注
	 * @param passBookNumber
	 *            存折序号
	 * @param cdNumber
	 *            存单号
	 * @param executeType
	 *            执行类型
	 */
	public void requsetPsnDepositWithdrawComChge(String fromAccountId, String toAccountId, String currency,
												 String cashRemit, String amount, String appointedAmount, String memo, String passBookNumber, String cdNumber,
												 String executeType) {
		// TODO Auto-generated method stub
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.Save_PsnDepositWithdrawComChge);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Dept.FROM_ACCOUNTID, fromAccountId);
		map.put(Dept.TO_ACCOUNTID, toAccountId);
		map.put(Dept.CURRENCY, currency);
		map.put(Dept.AMOUNT, amount);
		map.put(Dept.APPOINTED_AMOUNT, appointedAmount);
		map.put(Dept.CASHREMIT, cashRemit);
		map.put(Dept.MEMO, memo);
		map.put(Dept.NEW_NOTIFY_SAVE_TRANSMODE, executeType);
		map.put(Dept.PASSBOOK_NUMBER, passBookNumber);
		map.put(Dept.CD_NUMBER, cdNumber);

		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requsetPsnDepositWithdrawComChgeCallback");
	}

	/** 支取整存整取费用试算-----回调 */
	public void requsetPsnDepositWithdrawComChgeCallback(Object resultObj) {

	}

	/**
	 * 支取定活两便费用试算
	 *
	 * @param fromAccountId
	 *            转出账户ID
	 * @param toAccountId
	 *            转入账户ID
	 * @param currency
	 *            币种
	 * @param cashRemit
	 *            钞汇标志
	 * @param amount
	 *            转账金额
	 * @param memo
	 *            备注
	 * @param passBookNumber
	 *            存折序号
	 * @param cdNumber
	 *            存单号
	 * @param executeType
	 *            执行类型
	 */
	public void requsetPsnTerminalOrCurrentDepositComChge(String fromAccountId, String toAccountId, String currency,
														  String cashRemit, String amount, String memo, String passBookNumber, String cdNumber, String executeType) {
		// TODO Auto-generated method stub
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.Save_PsnTerminalOrCurrentDepositComChge);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Dept.FROM_ACCOUNTID, fromAccountId);
		map.put(Dept.TO_ACCOUNTID, toAccountId);
		map.put(Dept.CURRENCY, currency);
		map.put(Dept.CASHREMIT, cashRemit);
		map.put(Dept.AMOUNT, amount);
		map.put(Dept.MEMO, memo);
		map.put(Dept.PASSBOOK_NUMBER, passBookNumber);
		map.put(Dept.CD_NUMBER, cdNumber);
		map.put(Dept.NEW_NOTIFY_SAVE_TRANSMODE, executeType);

		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requsetPsnTerminalOrCurrentDepositComChgeCallback");
	}

	/** 支取定活两便费用试算-----回调 */
	public void requsetPsnTerminalOrCurrentDepositComChgeCallback(Object resultObj) {

	}

	/**
	 * 零存整取续存费用试算
	 *
	 * @param fromAccountId
	 *            转出账户ID
	 * @param toAccountId
	 *            转入账户ID
	 * @param amount
	 *            金额
	 * @param memo
	 *            备注
	 * @param transMode
	 *            执行类型
	 */
	public void requsetPsnExtendLingcunDepositeComChge(String fromAccountId, String toAccountId, String amount, String memo,
													   String transMode) {
		// TODO Auto-generated method stub
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.Save_PsnExtendLingcunDepositeComChge);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Dept.FROM_ACCOUNTID, fromAccountId);
		map.put(Dept.TO_ACCOUNTID, toAccountId);
		map.put(Dept.AMOUNT, amount);
		map.put(Dept.MEMO, memo);
		map.put(Dept.NEW_NOTIFY_SAVE_TRANSMODE, transMode);

		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requsetPsnExtendLingcunDepositeComChgeCallback");
	}

	/** 零存整取续存便费用试算-----回调 */
	public void requsetPsnExtendLingcunDepositeComChgeCallback(Object resultObj) {

	}

	/**
	 * 教育储蓄续存便费用试算
	 *
	 * @param fromAccountId
	 *            转出账户ID
	 * @param toAccountId
	 *            转入账户ID
	 * @param amount
	 *            金额
	 * @param memo
	 *            备注
	 * @param transMode
	 *            执行类型
	 */
	public void requsetPsnExtendEducationDepositeComChge(String fromAccountId, String toAccountId, String amount,
														 String memo, String transMode) {
		// TODO Auto-generated method stub
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.Save_PsnExtendEducationDepositeComChge);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Dept.FROM_ACCOUNTID, fromAccountId);
		map.put(Dept.TO_ACCOUNTID, toAccountId);
		map.put(Dept.AMOUNT, amount);
		map.put(Dept.MEMO, memo);
		map.put(Dept.NEW_NOTIFY_SAVE_TRANSMODE, transMode);

		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requsetPsnExtendEducationDepositeComChgeCallback");
	}

	/** 教育储蓄续存便费用试算-----回调 */
	public void requsetPsnExtendEducationDepositeComChgeCallback(Object resultObj) {

	}

	/**
	 * 支取通知存款费用试算
	 *
	 * @param fromAccountId
	 *            转出账户ID
	 * @param toAccountId
	 *            转入账户ID
	 * @param currency
	 *            币种
	 * @param cashRemit
	 *            钞汇
	 * @param amount
	 *            金额
	 * @param memo
	 *            备注
	 * @param passBookNumber
	 *            存折序号
	 * @param cdNumber
	 *            存单号
	 * @param cDTerm
	 *            存期
	 * @param convertType
	 *            转存方式
	 * @param scheduled
	 *            是否通知银行
	 * @param scheduleNumber
	 *            通知编号
	 * @param callDepositBalance
	 *            通知存款余额
	 * @param transMode
	 *            执行类型
	 */
	public void requsetPsnCallDepositToCurrentSavingComChge(String fromAccountId, String toAccountId, String currency,
															String cashRemit, String amount, String memo, String passBookNumber, String cdNumber, String cDTerm,
															String convertType, String scheduled, String scheduleNumber, String callDepositBalance, String transMode) {
		// TODO Auto-generated method stub
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.Save_PsnCallDepositToCurrentSavingComChge);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Dept.FROM_ACCOUNTID, fromAccountId); // 转出账户ID
		map.put(Dept.TO_ACCOUNTID, toAccountId); // 转入账户ID
		map.put(Dept.CURRENCY, currency); // 币种
		map.put(Dept.CASHREMIT, cashRemit); // 钞汇
		map.put(Dept.AMOUNT, amount); // 金额
		map.put(Dept.MEMO, memo); // 备注
		map.put(Dept.PASSBOOK_NUMBER, passBookNumber); // 存折序号
		map.put(Dept.CD_NUMBER, cdNumber); // 存单号
		map.put(Dept.CDTERM, cDTerm); // 存期
		map.put(Dept.NEW_NOTIFY_SAVE_CONVERT_TYPE, convertType); // 转存方式
		map.put(Dept.SCHEDULED, scheduled); // 是否通知银行
		map.put(Dept.SCHEDULE_NUMBER, scheduleNumber); // 通知编号
		map.put(Dept.CALLDEPOSIT_BALANCE, callDepositBalance); // 通知存款余额
		map.put(Dept.NEW_NOTIFY_SAVE_TRANSMODE, transMode); // 执行类型

		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requsetPsnCallDepositToCurrentSavingComChgeCallback");
	}

	/** 支取通知存款费用试算-----回调 */
	public void requsetPsnCallDepositToCurrentSavingComChgeCallback(Object resultObj) {
		// // 取消通讯框
		// BaseHttpEngine.dissMissProgressDialog();
		// BiiResponse biiResponse = (BiiResponse) resultObj;
		// List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// // 得到response
		// BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// Map<String, Object> result = (Map<String, Object>)
		// biiResponseBody.getResult();
		// // 将数据放入数据中心
		// DeptDataCenter.getInstance().setEducationStockpileMap(result);
		// communicationCallBack(EXTEND_EDUCATION_CALLLBAK);
	}

	// ///////////////存款管理新增手续费试算费用end/////////////////////////////////////

	// 存款管理 我的定期存款 账户选择
	public static final Dictionary<String, String, Boolean> MyPositMneyAcc = new Dictionary<String, String, Boolean>() {
		{
			put("有效", "00", false);
			put("销户", "01", true);
			put("冻结", "06", true);
			put("全部", "0", true);
		}
	};

	/**
	 * 存款管理 --我的定期存款 账户选择
	 */
	public static final List<String> saveAccList = new ArrayList<String>() {
		{
			add("有效");
			add("销户");
			add("冻结");
			add("全部");
		}
	};

	// ////////////////// 存款管理 格式化优惠户
	/**
	 * 按千位分割格式格式化数字
	 *
	 * @param code
	 *            :币种代码
	 * @param text
	 *            ： 原数字
	 * @param scale
	 *            ： 小数点保留位数
	 * @return 如果code是特殊币种，无论scale是多少，格式化后都没有小数位数
	 */
	public static String parseStringCodePattern(String code, String text, int scale) {
		if (LocalData.codeNoNumber.contains(code)) {
			return parseStringPattern(text, 0);
		} else {
			return parseStringPattern(text, scale);
		}

	}

	/**
	 * 按千位分割格式格式化数字
	 *
	 * @param text
	 *            原数字 如果原数字为nill 为空时返回原数字
	 * @param scale
	 *            小数点保留位数
	 * @return
	 */
	public static String parseStringPattern(String text, int scale) {
		if (text == null || "".equals(text) || "null".equals(text)) {
			return text;
		}
		if (text.contains(",") || text.contains("，")) {
			return text;
		}
		String temp = "###,###,###,###,###,###,###,##0";
		if (scale > 0)
			temp += ".";
		for (int i = 0; i < scale; i++)
			temp += "0";
		DecimalFormat format = new DecimalFormat(temp);
		BigDecimal d = new BigDecimal(text);
		return format.format(d).toString();
	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.OneTask;
	}
	public View addView(int resource) {
		View view = LayoutInflater.from(this).inflate(resource, null);
		tabcontent.addView(view);
		return view;
	}

	public void removeAllViews(View view) {
		tabcontent.removeView(view);
	}
	// //////////// 大额签约 start
	/** 查询账户列表 */
	public void requestQueryAllChinaBankAccount() {
		// 展示通讯框
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.QRY_ALL_BANK_ACCOUNT);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		List<String> accountList = new ArrayList<String>();
		paramsmap.put(Comm.ACCOUNT_TYPE, accountList);
		accountList.add(ConstantGloble.ACC_TYPE_ORD);// 普通活期
		accountList.add(ConstantGloble.ACC_TYPE_BRO);// 借记卡
		accountList.add(ConstantGloble.ACC_TYPE_RAN);// 活期一本通

		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestQueryAllChinaBankAccountCallBack");
	}

	/**
	 * 查询账户列表 返回------
	 *
	 * @param resultObj
	 */
	public void requestQueryAllChinaBankAccountCallBack(Object resultObj) {
//		BaseHttpEngine.dissMissProgressDialog();
//		BiiResponse biiResponse = (BiiResponse) resultObj;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		List<Map<String, Object>> accList = (List<Map<String, Object>>) (biiResponseBody.getResult());
//
//		DeptDataCenter.getInstance().setLargeSignAccountList(accountList);
//		communicationCallBack(QUERY_ALL_ACCOUNT_CALLBACK);

	}

	/** 大额存单签约交易 */
	public void requestPsnLargeCDSignSubmit(String accountId,String token,String conversationId) {
		// 展示通讯框
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.LargeSign_requestPsnLargeCDSignSubmit);
		biiRequestBody.setConversationId(conversationId);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Dept.LargeSign_accountId, accountId);
		map.put(Comm.TOKEN_REQ, token);
		biiRequestBody.setParams(map);

		HttpManager.requestBii(biiRequestBody, this, "requestPsnLargeCDSignSubmitCallBack");
	}

	/** 大额存单签约交易 返回------
	 * @param resultObj
	 */
	public void requestPsnLargeCDSignSubmitCallBack(Object resultObj) {

	}

	/** 查询大额存单签约账号*/
	public void requestPsnLargeCDSignedAccQry() {
		// TODO Auto-generated method stub
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.REQUEST_SIGNED_ACC_QRY);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnLargeCDSignedAccQryCallBack");
	}

	/**
	 * 查询大额存单签约账号 回调
	 * @param resultObj
	 */
	public void requestPsnLargeCDSignedAccQryCallBack(Object resultObj) {

	}
	// ////////////大额签约 end

	////////////////////////////////////////////////////////////////////////////
	// add by luqp 2016年3月4日 修改用户选择的Spinner
	/**
	 * 存款管理 -- 大额存单 -- 查询大额存单 
	 * 选择Spinner {全部,正常,已转让,已赎回,止付(空：全部   0：正常   1：已转让   2: 已赎回  3：止付)}
	 */
	public static final Dictionary<String, String, Boolean> MyTranTypeSp = new Dictionary<String, String, Boolean>() {
		{
			put("全部", " ", false);
			put("正常 ", "0", true);
			put("已转让", "1", true);
			put("已赎回", "2", true);
			put("止付", " 3", true);
		}
	};

	/**
	 * 获取输入后剩余金额
	 * @param allAmount  总金额
	 * @param inputAmount  输入金额
	 * @return 返回剩余金额
	 */
	public  static String getSurplusAmount(String allAmount, String inputAmount){
		Double all = Double.parseDouble(formatMount(allAmount));
		Double input =  Double.parseDouble(formatMount(inputAmount));
		return String.valueOf(all - input);
	}

	/**
	 * 过滤金额千位符里面逗号","
	 * 并且格式化金额小数位最多为两位
	 * @param mount
	 * @return
	 */
	public static String formatMount(String mount){
		if(TextUtils.isEmpty(mount)){
			return mount;
		}
		mount = mount.replace(",", "");
		return StringUtil.append2Decimals(mount,2);
	}


	// =============================================================================================
	// 利率上浮1.3倍 start
	/**
	 * JSON:PsnLumpMoreTimeDepositComChge 定期存款-新增1.3倍整存整取费用试算（查询扩充版专用接口）
	 * @param fromAccountId:账号ID
	 * @param toAccountId:转入账户ID
	 * @param currency:币种
	 * @param cashRemit:钞汇
	 * @param cDTerm:存期
	 * @param amount:金额
	 * @param type:产品类型
	 * @param memo:备注
	 */
	public void requsetPsnLumpMoreTimeDepositComChge(String fromAccountId, String toAccountId, String currency,
													 String cashRemit, String cDTerm, String amount, String type, String memo) {
		// TODO Auto-generated method stub
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.Save_PsnLumpMoreTimeDepositComChge);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Dept.FROM_ACCOUNTID, fromAccountId);
		map.put(Dept.TO_ACCOUNTID, toAccountId);
		map.put(Dept.CURRENCY, currency);
		map.put(Dept.CASHREMIT, cashRemit);
		map.put(Dept.CD_TERM, cDTerm);
		map.put(Dept.AMOUNT, amount);
		map.put(Dept.TYPE, type);
		map.put(Dept.MEMO, memo);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requsetPsnLumpMoreTimeDepositComChgeCallback");
	}

	/**
	 * JSON:PsnTransLumpMoreTimeDepositNew 定期存款-新增1.3倍整存整取（查询版专用接口）
	 * @param fromAccountId:转出账户ID
	 * @param toAccountId:转入账户ID
	 * @param currency:币种
	 * @param cashRemit:钞汇
	 * @param cDTerm:存期
	 * @param amount:金额
	 * @param type:产品类型
	 * @param memo:备注
	 */
	public void requestPsnTransLumpMoreTimeDepositNew(String fromAccountId, String toAccountId, String currency,
													  String cashRemit, String cDTerm, String amount, String type, String memo, String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Dept.Save_PsnTransLumpMoreTimeDepositNew);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Dept.FROM_ACCOUNTID, fromAccountId);
		map.put(Dept.TO_ACCOUNTID, toAccountId);
		map.put(Dept.CURRENCY, currency);
		map.put(Dept.CASHREMIT, cashRemit);
		map.put(Dept.CD_TERM, cDTerm);
		map.put(Dept.AMOUNT, amount);
		map.put(Dept.TYPE, type);
		map.put(Dept.MEMO, memo);
		map.put(Comm.TOKEN_REQ, token);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnTransLumpMoreTimeDepositNewCallBack");
	}

	/** 定期存款-新增1.3倍整存整取（查询版专用接口） 回调 */
	public void requestPsnTransLumpMoreTimeDepositNewCallBack(Object resultObj) {
		// 取消通讯框 TODO
		BaseHttpEngine.dissMissProgressDialog();

		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		DeptDataCenter.getInstance().setRequestCallBackMap(resultMap);
		communicationCallBack(WHOLE_SAVE_CALLBACK);
	}

	// =============================================================================================
}
