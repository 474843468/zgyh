package com.chinamworld.bocmbci.constant;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Blpt;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.bii.constant.Bond;
import com.chinamworld.bocmbci.bii.constant.CashBank;
import com.chinamworld.bocmbci.bii.constant.Collect;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Order;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.bii.constant.SBRemit;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.bii.constant.Third;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt.productlist.QueryProductOutlayActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.MyInvetProductActivity;
import com.chinamworld.bocmbci.biz.bocnet.BocnetDataCenter;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.prms.control.PrmsControl;
import com.chinamworld.bocmbci.exception.CodeMessageList;
import com.chinamworld.bocmbci.server.BocMBCManager.BocModule;
import com.chinamworld.bocmbci.utils.RUtil;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



/**
 * @author xbybaoying
 * 
 */
public class LocalData {

	/**
	 * curtain fake data
	 */
	public static ArrayList<Map<String, String>> newMessagelistData = null;
	static {
		newMessagelistData = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("content", "1.携程联手推出旅游出行30%优惠，查看详情");
		map.put("isRead", "00");

		newMessagelistData.add(map);
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("content", "2.携程联手推出旅游出行30%优惠，查看详情");
		map2.put("isRead", "00");
		newMessagelistData.add(map2);
		Map<String, String> map3 = new HashMap<String, String>();
		map3.put("content", "3.中国银行手机银行重装登场，全新界面，炫酷");
		map3.put("isRead", "00");
		newMessagelistData.add(map3);
		Map<String, String> map4 = new HashMap<String, String>();
		map4.put("content", "4.中国银行手机银行重装登场，全新界面，炫酷");
		map4.put("isRead", "00");
		newMessagelistData.add(map4);
		Map<String, String> map5 = new HashMap<String, String>();
		map5.put("content", "5.中国银行手机银行重装登场，全新界面，炫酷");
		map5.put("isRead", "00");
		newMessagelistData.add(map5);
	}
	/** PAYMENTACTIVITY: 账单缴付页面 */
	public static final String PAYMENTACTIVITY = "com.chinamworld.bocmbci.biz.blpt.BillPaymentMainActivity";
	/**
	 * curtain fake data
	 */
	public static ArrayList<Map<String, String>> notifyListData = null;
	static {
		notifyListData = new ArrayList<Map<String, String>>();

		Map<String, String> map = new HashMap<String, String>();
		map.put("content", "主动收款纪录3");
		map.put("isRead", "00");
		notifyListData.add(map);
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("content", "距你最近一次修改密码时间为3个月");
		map2.put("isRead", "00");
		notifyListData.add(map2);
		Map<String, String> map3 = new HashMap<String, String>();
		map3.put("content", "本期应还信用卡余额300.00");
		map3.put("isRead", "00");
		notifyListData.add(map3);
	}
//	/**
//	 * curtain fake data
//	 */
//	public static ArrayList<String> historyListData = null;
//	static {
//		historyListData = new ArrayList<String>();
//		historyListData.add("2012-09-12 16:34");
//		historyListData.add("2012-09-12 16:34");
//		historyListData.add("2012-09-12 16:34");
//	}

	/**
	 * 贵金属 xyl
	 */
	@BocModule("贵金属")
	public static ArrayList<ImageAndText> prmsManagerlistData = null;
	static {
		prmsManagerlistData = new ArrayList<ImageAndText>();
		ImageAndText icon1 = new ImageAndText(R.drawable.prms_icon_price, "账户贵金属行情","prmsManage_1");
		icon1.setClassName("com.chinamworld.bocmbci.biz.prms.price.PrmsPricesActivity");
		ImageAndText icon2 = new ImageAndText(R.drawable.prms_icon_mycount, "我的账户贵金属", false,"prmsManage_2");
		icon2.setClassName("com.chinamworld.bocmbci.biz.prms.myaccount.PrmsAccMeneActivity");
		ImageAndText icon3 = new ImageAndText(R.drawable.prms_icon_query, "交易查询","prmsManage_3");
		icon3.setFastShowText("贵金属交易查询");

		icon3.setClassName("com.chinamworld.bocmbci.biz.prms.query.PrmsQueryActivity");
		icon3.setMustLogin(false);
		prmsManagerlistData.add(icon1);
		prmsManagerlistData.add(icon2);
		prmsManagerlistData.add(icon3);
	}

	// wenlin add start
	@BocModule("存款管理")
	public static ArrayList<ImageAndText> deptStorageCashLeftList = null;
	static {

		deptStorageCashLeftList = new ArrayList<ImageAndText>();
		ImageAndText icon1 = new ImageAndText(R.drawable.savereg, "我要存定期","deptStorageCash_1");
		icon1.setClassName("com.chinamworld.bocmbci.biz.dept.savereg.SaveRegularActivity");
		ImageAndText icon2 = new ImageAndText(R.drawable.myreg, "我的定期存款","deptStorageCash_2");
		icon2.setClassName("com.chinamworld.bocmbci.biz.dept.myreg.MyRegSaveActivity");
		ImageAndText icon3 = new ImageAndText(R.drawable.icon_left_dqydzc, "定期约定转存","deptStorageCash_3");
		icon3.setClassName("com.chinamworld.bocmbci.biz.dept.ydzc.DeptYdzcQueryActivity");
		// ImageAndText icon4 = new ImageAndText(R.drawable.icon_left_zntzck,
		// "智能通知存款");
		// icon4.setClassName("com.chinamworld.bocmbci.biz.dept.zntzck.DeptZntzckThreeMenuActivity");
		ImageAndText icon5 = new ImageAndText(R.drawable.icon_left_decd, "大额存单","deptStorageCash_4");
		icon5.setClassName("com.chinamworld.bocmbci.biz.dept.largecd.LargeCDMenuActivity");
		ImageAndText icon6 = new ImageAndText(R.drawable.notify_manage, "通知管理","deptStorageCash_5");
		icon6.setClassName("com.chinamworld.bocmbci.biz.dept.notmg.NotifyManageActivity");
		ImageAndText icon7 = new ImageAndText(R.drawable.regular_query, "定期存款查询","deptStorageCash_6");
		icon7.setClassName("com.chinamworld.bocmbci.biz.dept.QureyRecordManagerActivity");

		deptStorageCashLeftList.add(icon1);
		deptStorageCashLeftList.add(icon2);
		deptStorageCashLeftList.add(icon3);
		// deptStorageCashLeftList.add(icon4);
		deptStorageCashLeftList.add(icon5);
		deptStorageCashLeftList.add(icon6);
		deptStorageCashLeftList.add(icon7);

	}

	// 电子支付
	@BocModule("电子支付")
	public static ArrayList<ImageAndText> ePayLeftList = null;
	static {

		ePayLeftList = new ArrayList<ImageAndText>();
		ImageAndText icon1 = new ImageAndText(R.drawable.icon_left_epay_exc, PubConstants.MENU_LEFT_ONE,"ePay_1");
		icon1.setClassName("com.chinamworld.bocmbci.biz.epay.EPayMainActivity");
		ImageAndText icon2 = new ImageAndText(R.drawable.icon_left_epay_query, PubConstants.MENU_LEFT_TWO,"ePay_2");
		icon2.setClassName("com.chinamworld.bocmbci.biz.epay.transquery.TransQueryActivity");
		ePayLeftList.add(icon1);
		ePayLeftList.add(icon2);
	}

	// 现金宝
	@BocModule("现金宝")
	public static ArrayList<ImageAndText> cashBankLeftList = null;
	static {

		cashBankLeftList = new ArrayList<ImageAndText>();
		ImageAndText icon1 = new ImageAndText(R.drawable.ic_left_my_cash_bank, "我的现金宝","cashBank_1");
		icon1.setClassName("com.chinamworld.bocmbci.biz.cashbank.mycash.CashBankMainActivity");
		ImageAndText icon2 = new ImageAndText(R.drawable.ic_left_cash_bank_trans_query, "交易明细查询","cashBank_2");
		icon2.setClassName("com.chinamworld.bocmbci.biz.cashbank.cashquery.CashBankQueryMainActivity");
		icon2.setFastShowText("现金宝交易明细查询");
		cashBankLeftList.add(icon1);
		cashBankLeftList.add(icon2);
	}

	// wenlin add end
	/**
	 * 账户管理 wangmengmeng add start
	 */
	/** 菜单树 */
	@BocModule("账户管理")
	public static ArrayList<ImageAndText> accountManagerlistData = null;
	static {
		accountManagerlistData = new ArrayList<ImageAndText>();
		ImageAndText icon_account = new ImageAndText(R.drawable.icon_left_zhanghu, "我的账户","accountManager_1");
		icon_account.setClassName("com.chinamworld.bocmbci.biz.acc.mybankaccount.AccManageActivity");
		ImageAndText icon_relevance = new ImageAndText(R.drawable.icon_left_guanlian, "自助关联","accountManager_2");
		icon_relevance.setClassName("com.chinamworld.bocmbci.biz.acc.relevanceaccount.AccInputRelevanceAccountActivity");
		ImageAndText icon_loss = new ImageAndText(R.drawable.icon_left_guashi, "临时挂失","accountManager_3");
		icon_loss.setClassName("com.chinamworld.bocmbci.biz.acc.lossreport.AccDebitCardLossActivity");

		ImageAndText icon_financeic = new ImageAndText(R.drawable.icon_left_dianzixianjin, "电子现金服务","accountManager_4");
		icon_financeic.setClassName("com.chinamworld.bocmbci.biz.acc.financeicaccount.FinanceMenuActivity");

		ImageAndText icon_credit = new ImageAndText(R.drawable.icon_xnyhk, "虚拟银行卡服务","accountManager_5");
		icon_credit.setClassName("com.chinamworld.bocmbci.biz.crcd.mycrcd.virtualservice.VirtualBCServiceMenuActivity");
		ImageAndText icon_medical = new ImageAndText(R.drawable.icon_left_ybzh, "医保账户","accountManager_6");
		icon_medical.setClassName("com.chinamworld.bocmbci.biz.acc.medical.MedicalMenuActivity");
		
		ImageAndText icon_apply=new ImageAndText(R.drawable.acc_left_apply,"申请定期/活期账户","accountManager_7");
		icon_apply.setClassName("com.chinamworld.bocmbci.biz.acc.applytermdeposite.ApplyTermDepositeActivity");
		accountManagerlistData.add(icon_account);
//		accountManagerlistData.add(icon_relevance);
		accountManagerlistData.add(icon_loss);
		accountManagerlistData.add(icon_credit);
		accountManagerlistData.add(icon_financeic);
		accountManagerlistData.add(icon_medical);
		accountManagerlistData.add(icon_apply);
	}

	/**
	 * 我的信用卡
	 */
	@BocModule("我的信用卡")
	public static ArrayList<ImageAndText> myCrcdListData = null;
	static {
		myCrcdListData = new ArrayList<ImageAndText>();
		ImageAndText icon1 = new ImageAndText(R.drawable.icon_left_chaxun, "信用卡查询","myCrcd_1");
		icon1.setClassName("com.chinamworld.bocmbci.biz.crcd.mycrcd.MyCreditCardActivity");
		ImageAndText icon2 = new ImageAndText(R.drawable.icon_left_huankuan, "信用卡还款","myCrcd_2");
		icon2.setClassName("com.chinamworld.bocmbci.biz.crcd.mycrcd.MyCardPaymentActivity");
		ImageAndText icon3 = new ImageAndText(R.drawable.icon_left_fenqi, "分期业务","myCrcd_3");
		icon3.setClassName("com.chinamworld.bocmbci.biz.crcd.mycrcd.MyCardInstalmentActivity");
		ImageAndText icon4 = new ImageAndText(R.drawable.icon_left_fushuka, "信用卡附属卡管理","myCrcd_4");
		icon4.setClassName("com.chinamworld.bocmbci.biz.crcd.mycrcd.MyMasterAndSupplInfoListActivity");
		ImageAndText icon5 = new ImageAndText(R.drawable.icon_left_xunika, "虚拟银行卡服务","myCrcd_5");
		icon5.setClassName("com.chinamworld.bocmbci.biz.crcd.mycrcd.virtualservice.VirtualBCServiceMenuActivity");
		ImageAndText icon6 = new ImageAndText(R.drawable.icon_left_jihuo, "信用卡激活","myCrcd_6");
		icon6.setClassName("com.chinamworld.bocmbci.biz.crcd.mycrcd.crcdSetup.CrcdActiveInfo");
		myCrcdListData.add(icon1);
		myCrcdListData.add(icon2);
		myCrcdListData.add(icon3);
		myCrcdListData.add(icon4);
		myCrcdListData.add(icon5);
		myCrcdListData.add(icon6);
	}

	/**
	 * 服务设定 菜单栏 add by xyl
	 */
	@BocModule("服务设定")
	public static ArrayList<ImageAndText> settingManagerlistData = null;
	static {
		settingManagerlistData = new ArrayList<ImageAndText>();
		ImageAndText icon1 = new ImageAndText(R.drawable.icon_left_guanjia, "账户管家","settingManager_1");
		icon1.setClassName("com.chinamworld.bocmbci.biz.setting.accmanager.AccountManagerActivity");
		ImageAndText icon2 = new ImageAndText(R.drawable.icon_left_shezhi, "交易限额设置","settingManager_2");
		icon2.setClassName("com.chinamworld.bocmbci.biz.setting.limit.LimitSettingActivity");
		ImageAndText icon3 = new ImageAndText(R.drawable.icon_left_mima, "修改登录密码","settingManager_3");
		icon3.setClassName("com.chinamworld.bocmbci.biz.setting.pass.EditLoginPassActivity");
		ImageAndText icon4 = new ImageAndText(R.drawable.icon_left_xinxi, "预留信息","settingManager_4");
		icon4.setClassName("com.chinamworld.bocmbci.biz.setting.obligate.EditObligateMessageActivity");
		ImageAndText icon5 = new ImageAndText(R.drawable.icon_left_tcsjsd, "退出时间设定","settingManager_5");
		icon5.setClassName("com.chinamworld.bocmbci.biz.setting.exittime.ExitTimeSettingActivity");
		// 移动电子支付到 服务设定菜单
		ImageAndText icon6 = new ImageAndText(R.drawable.icon_left_epay_exc, PubConstants.MENU_LEFT_ONE,"settingManager_6");
		icon6.setClassName("com.chinamworld.bocmbci.biz.epay.EPayMainActivity");
		ImageAndText icon7 = new ImageAndText(R.drawable.icon_left_epay_query, PubConstants.MENU_LEFT_TWO,"settingManager_7");
		icon7.setClassName("com.chinamworld.bocmbci.biz.epay.transquery.TransQueryActivity");
		ImageAndText icon8 = new ImageAndText(R.drawable.setting_setaccount_icon, "设置默认账户","settingManager_8");
		icon8.setClassName("com.chinamworld.bocmbci.biz.setting.setacct.SettingAccountActivity");
		ImageAndText icon9 = new ImageAndText(R.drawable.inves_left_icon, "开通投资理财","settingManager_9");
		icon9.setClassName("com.chinamworld.bocmbci.biz.invest.activity.InvesHasOpenActivity");
		// P501 add音频key(中银e盾)
		ImageAndText icon10 = new ImageAndText(R.drawable.audio_key_info_icon, "查看中银e盾信息","settingManager_10");
		icon10.setClassName("com.chinamworld.bocmbci.biz.audio.AudioKeyInfoActivity");
		ImageAndText icon11 = new ImageAndText(R.drawable.audio_key_modify_pwd_icon, "修改中银e盾密码","settingManager_11");
		icon11.setClassName("com.chinamworld.bocmbci.biz.audio.AudioKeyPwdActivity");
		ImageAndText icon12 = new ImageAndText(R.drawable.manage_bind_device, "管理绑定设备","settingManager_12");
		icon12.setClassName("com.chinamworld.bocmbci.biz.setting.hardwarebinding.HardwareBindingActivity");
		ImageAndText icon13 = new ImageAndText(R.drawable.safety_tools_setting, "安全工具设置","settingManager_13");
		icon13.setClassName("com.chinamworld.bocmbci.biz.setting.safetytools.SafetyToolsActivity");
		settingManagerlistData.add(icon1);
		settingManagerlistData.add(icon8);
		settingManagerlistData.add(icon9);
		settingManagerlistData.add(icon2);
		settingManagerlistData.add(icon3);
		settingManagerlistData.add(icon4);
		settingManagerlistData.add(icon5);
		settingManagerlistData.add(icon6);
		settingManagerlistData.add(icon7);
		settingManagerlistData.add(icon10);
		settingManagerlistData.add(icon11);
		settingManagerlistData.add(icon12);
		settingManagerlistData.add(icon13);
	}
	/**
	 * 基金左侧菜单栏 add by xyl
	 */
	@BocModule("基金")
	public static ArrayList<ImageAndText> fincLeftListData = null;
	static {
		fincLeftListData = new ArrayList<ImageAndText>();
		ImageAndText icon1 = new ImageAndText(R.drawable.icon_finc_fundprice, "行情查询及购买", false,"finc_1");
		ImageAndText icon2 = new ImageAndText(R.drawable.icon_finc_myfund, "我的基金", true,"finc_2");
		ImageAndText icon3 = new ImageAndText(R.drawable.icon_finc_accmanager, "账户管理", true,"finc_3");
		ImageAndText icon4 = new ImageAndText(R.drawable.icon_finc_sechedued, "基金定投管理", true,"finc_4");
		ImageAndText icon5 = new ImageAndText(R.drawable.prms_icon_query, "交易查询", true,"finc_5");
		ImageAndText icon6 = new ImageAndText(R.drawable.fund_recommend, "基金推荐", true,"finc_6");

		// TODO ClassName 加上 现在的不对
		icon1.setClassName("com.chinamworld.bocmbci.biz.finc.fundprice.FundPricesMenuActivity");
		icon1.setOutlayClassName("com.chinamworld.bocmbci.biz.finc.fundprice.FundPricesActivityNew");
		icon2.setClassName("com.chinamworld.bocmbci.biz.finc.myfund.MyFincMainActivity");
		icon3.setClassName("com.chinamworld.bocmbci.biz.finc.accmanager.FincAccManagerMenuActivity");
		icon4.setClassName("com.chinamworld.bocmbci.biz.finc.query.FundDQDEMenuActivity");
		icon5.setClassName("com.chinamworld.bocmbci.biz.finc.query.FundqueryMenuActivity");
		icon6.setClassName("com.chinamworld.bocmbci.biz.finc.orcm.OrcmProductListActivity");
		// icon1.setMustLogin(false);
		// icon2.setMustLogin(false);
		// icon3.setMustLogin(false);
		// icon4.setMustLogin(false);
		// icon5.setMustLogin(false);
		icon5.setFastShowText("基金交易查询");
		icon3.setFastShowText("基金账户管理");

		fincLeftListData.add(icon2);
		fincLeftListData.add(icon1);
		fincLeftListData.add(icon4);
		fincLeftListData.add(icon3);
		fincLeftListData.add(icon5);
		fincLeftListData.add(icon6);
	}

	/*** 第三方存管 左侧菜单 **/
	@BocModule("第三方存管")
	public static ArrayList<ImageAndText> thirdManangerLeftListData = null;
	static {
		thirdManangerLeftListData = new ArrayList<ImageAndText>();
		ImageAndText icon1 = new ImageAndText(R.drawable.icon_left_third_cecuritytrade, "第三方存管","thirdMananger_1");
		icon1.setClassName("com.chinamworld.bocmbci.biz.thridmanage.cecuritytrade.CecurityTradeActivity");
		ImageAndText icon2 = new ImageAndText(R.drawable.icon_left_third_historytrade, "历史交易查询","thirdMananger_2");
		icon2.setClassName("com.chinamworld.bocmbci.biz.thridmanage.historytrade.HistoryTradeActivity");
		ImageAndText icon3 = new ImageAndText(R.drawable.icon_left_third_platforacct, "台账查询","thirdMananger_3");
		icon3.setClassName("com.chinamworld.bocmbci.biz.thridmanage.platforacct.PlatforAcctActivity");
		ImageAndText icon4 = new ImageAndText(R.drawable.icon_left_third_openacct, "预约开户及查询","thirdMananger_4");
		icon4.setClassName("com.chinamworld.bocmbci.biz.thridmanage.openacct.OpenAccActivity");
		thirdManangerLeftListData.add(icon1);
		thirdManangerLeftListData.add(icon2);
		thirdManangerLeftListData.add(icon3);
		thirdManangerLeftListData.add(icon4);
	}

	/*** 中银证券便捷开户 左侧菜单 **/
	@BocModule("中银证券开户")
	public static ArrayList<ImageAndText> quickOpenLeftListData = null;
	static {
		quickOpenLeftListData = new ArrayList<ImageAndText>();
		ImageAndText icon1 = new ImageAndText(R.drawable.quickopen_quickopen, "中银国际\n证券开户","quickOpen_1");
		icon1.setClassName("com.chinamworld.bocmbci.biz.quickOpen.quickopen.StockThirdQuickOpenActivity");
		ImageAndText icon2 = new ImageAndText(R.drawable.quickopen_openquery, "开户结果\n查询","quickOpen_2");
		icon2.setClassName("com.chinamworld.bocmbci.biz.quickOpen.openQuery.StockThirdQuickOpenQueryActivity");
		quickOpenLeftListData.add(icon1);
		quickOpenLeftListData.add(icon2);
	}

	/*** 债券 左侧菜单 **/
	@BocModule("债券")
	public static ArrayList<ImageAndText> bondLeftListData = null;
	static {
		bondLeftListData = new ArrayList<ImageAndText>();

		ImageAndText icon1 = new ImageAndText(R.drawable.bond_icon_hq, "债券行情", false,"bond_1");

		icon1.setClassName("com.chinamworld.bocmbci.biz.bond.allbond.AllBondListActivity");
		icon1.setOutlayClassName("com.chinamworld.bocmbci.biz.bond.allbond.AllBondListActivity");

		ImageAndText icon2 = new ImageAndText(R.drawable.bond_icon_mybond, "我的债券", true,"bond_2");

		icon2.setClassName("com.chinamworld.bocmbci.biz.bond.mybond.MyBondListActivity");
		ImageAndText icon3 = new ImageAndText(R.drawable.prms_icon_query, "交易状况查询", true,"bond_3");
		icon3.setClassName("com.chinamworld.bocmbci.biz.bond.historytran.HistoryTransferListActivity");
		bondLeftListData.add(icon1);
		bondLeftListData.add(icon2);
		bondLeftListData.add(icon3);
	}

	/*** 保险 左侧菜单 **/
	@BocModule("保险")
	public static ArrayList<ImageAndText> safetyLeftListData = null;
	static {
		safetyLeftListData = new ArrayList<ImageAndText>();

		ImageAndText icon1 = new ImageAndText(R.drawable.safety_product_icon, "保险产品查询", false,"safety_1");
		icon1.setClassName("com.chinamworld.bocmbci.biz.safety.safetyproduct.SafetyProductListActivity");

		ImageAndText icon2 = new ImageAndText(R.drawable.safety_temp_icon, "暂存投保单管理","safety_2");
		icon2.setClassName("com.chinamworld.bocmbci.biz.safety.safetytemp.SafetyTempProductListActivity");

		ImageAndText icon3 = new ImageAndText(R.drawable.safety_hold_icon, "持有保单查询","safety_3");
		icon3.setClassName("com.chinamworld.bocmbci.biz.safety.safetyhold.SafetyHoldProductQueryActivity");

		ImageAndText icon4 = new ImageAndText(R.drawable.safety_history_icon, "历史交易查询","safety_4");
		icon4.setClassName("com.chinamworld.bocmbci.biz.safety.safetyhistorytran.SafetyHoldHisQueryActivity");
		safetyLeftListData.add(icon1);
		safetyLeftListData.add(icon2);
		safetyLeftListData.add(icon3);
		safetyLeftListData.add(icon4);
	}
	/*** 积利金 左侧菜单 **/
	@BocModule("积利金")
	public static ArrayList<ImageAndText> goldboundsLeftListData = null;
	static {
		goldboundsLeftListData = new ArrayList<ImageAndText>();

		ImageAndText icon1 = new ImageAndText(R.drawable.icon_left_zhanghu, "账户管理","goldbonusManager_1");
		icon1.setClassName("com.chinamworld.bocmbci.biz.goldbonus.accountmanager.AccountManagerMainActivity");

		ImageAndText icon2 = new ImageAndText(R.drawable.goldbonus_left_busi, "买卖交易","goldbonusManager_2");
		icon2.setClassName("com.chinamworld.bocmbci.biz.goldbonus.busitrade.BusiTradeAvtivity");

		ImageAndText icon3 = new ImageAndText(R.drawable.goldbonus_left_fixinvest, "定投管理","goldbonusManager_3");
		icon3.setClassName("com.chinamworld.bocmbci.biz.goldbonus.fixinvestmanager.FixInvestManagerActivity");

		ImageAndText icon4 = new ImageAndText(R.drawable.goldbonus_left_timepisotion, "定存管理","goldbonusManager_4");
		icon4.setClassName("com.chinamworld.bocmbci.biz.goldbonus.timedepositmanager.TimeDepositManagerActivity");
		
		ImageAndText icon5 = new ImageAndText(R.drawable.goldbonus_left_tradequery, "交易查询","goldbonusManager_5");
		icon5.setClassName("com.chinamworld.bocmbci.biz.goldbonus.tradequery.TransQueryActivity");

		ImageAndText icon6 = new ImageAndText(R.drawable.goldbonus_left_extra, "贵金属实物提取","goldbonusManager_6");
		icon6.setClassName("com.chinamworld.bocmbci.biz.goldbonus.realextract.RealExtractActivity");
		
		goldboundsLeftListData.add(icon1);
		goldboundsLeftListData.add(icon2);
		goldboundsLeftListData.add(icon3);
		goldboundsLeftListData.add(icon4);
		goldboundsLeftListData.add(icon5);
		goldboundsLeftListData.add(icon6);

	}
	
	/*** 资产管理 左侧菜单 **/
	@BocModule("资产管理")
	public static ArrayList<ImageAndText> list_imageAndText=null;
	static{
		list_imageAndText=new ArrayList<ImageAndText>();
		ImageAndText icon_assetManager_index = new ImageAndText(R.drawable.assetmanager_assetman, "我的资产","asssetManager_1");
		icon_assetManager_index.setClassName("com.chinamworld.bocmbci.biz.assetmanager.capitalmanager.AssetManagerBalanceSheetActivity");
//		icon_assetManager_index.setClassName("com.chinamworld.bocmbci.biz.assetmanager.capitalmanager.AssetmanagerIndexActivity");
		
		ImageAndText icon_accountmanager = new ImageAndText(R.drawable.assetmanager_accountmanager, "账户管理","asssetManager_2");
		icon_accountmanager .setClassName("com.chinamworld.bocmbci.biz.assetmanager.accountmanager.AssertsAccountManagerActivity");
		
		ImageAndText icon_capitalManager = new ImageAndText(R.drawable.assetmanager_fundmanager, "资金管理","asssetManager_3");
		icon_capitalManager.setClassName("com.chinamworld.bocmbci.biz.assetmanager.capitalmanager.AssetManagerServiceActivity");
//		ImageAndText icon = new ImageAndText(R.drawable.assetmanager_banlance, "资产负债报告（资产报告）","asssetManager_4");
//		icon.setClassName("com.chinamworld.bocmbci.biz.assetmanager.capitalmanager.AssetManagerBalanceSheetActivity");
//		ImageAndText icon_account = new ImageAndText(R.drawable., "资产负债报告","asssetManager_4");
//		icon_account.setClassName("");
		
		list_imageAndText.add(icon_assetManager_index);
		list_imageAndText.add(icon_accountmanager);
		list_imageAndText.add( icon_capitalManager);
//		list_imageAndText.add(icon);
		
	}

	/*** 开户送电子银行左侧菜单 **/
	@BocModule("开户送电子银行")
	public static ArrayList<ImageAndText> bocnetLeftListData = null;
	static {
		bocnetLeftListData = new ArrayList<ImageAndText>();

		ImageAndText icon1 = new ImageAndText(R.drawable.icon_left_zhanghu, "我的账户","bocnet_1");
		icon1.setClassName(BocnetDataCenter.getInstance().getIntentAction());
		icon1.setMustLogin(false);

		ImageAndText icon2 = new ImageAndText(R.drawable.icon_left_dianzixianjin, "电子现金服务","bocnet_2");
		icon2.setClassName("com.chinamworld.bocmbci.biz.bocnet.cashacct.CashAcctActivity");
		icon2.setMustLogin(false);

		bocnetLeftListData.add(icon1);
		bocnetLeftListData.add(icon2);
	}

	/*** 民生服务左侧菜单 **/
	@BocModule("民生服务")
	public static ArrayList<ImageAndText> plpsLeftListData = null;
	static {
		plpsLeftListData = new ArrayList<ImageAndText>();

		ImageAndText icon1 = new ImageAndText(R.drawable.plps_payment, "我的民生缴费","plps_1");
		// icon1.setClassName("com.chinamworld.bocmbci.biz.plps.liveservice.LiveServiceMainActivity");
		icon1.setClassName("com.chinamworld.bocmbci.biz.plps.payment.project.PaymentAllProject");
		// ImageAndText icon2 = new ImageAndText(R.drawable.safety_temp_icon,
		// "养老金服务");
		// icon2.setClassName("com.chinamworld.bocmbci.biz.plps.annuity.AnnuityMainActivity");
		// ImageAndText icon3 = new ImageAndText(R.drawable.safety_temp_icon,
		// "签约代缴服务");
		// icon3.setClassName("com.chinamworld.bocmbci.biz.plps.payment.PaymentMainActivity");
		ImageAndText icon4 = new ImageAndText(R.drawable.plps_payment_search, "缴费记录查询","plps_2");
		icon4.setClassName("com.chinamworld.bocmbci.biz.plps.liveservice.CommServiceActivity");

		plpsLeftListData.add(icon1);
		// plpsLeftListData.add(icon2);
		// plpsLeftListData.add(icon3);
		plpsLeftListData.add(icon4);
	}

	/** 取款模块的左侧菜单数据 */
	@BocModule("手机取款")
	public static ArrayList<ImageAndText> DrawMoneyLeftList = null;
	static {

		DrawMoneyLeftList = new ArrayList<ImageAndText>();
		ImageAndText icon1 = new ImageAndText(R.drawable.icon_left_remit, "汇往取款人","DrawMoney_1");
		icon1.setClassName("com.chinamworld.bocmbci.biz.drawmoney.remitout.RemitCardListActivity");
		ImageAndText icon2 = new ImageAndText(R.drawable.icon_left_remit_query, "汇出查询","DrawMoney_2");
		icon2.setClassName("com.chinamworld.bocmbci.biz.drawmoney.remitquery.RemitQueryChooseCardActivity");

		ImageAndText icon3 = new ImageAndText(R.drawable.icon_left_draw_agency, "代理点取款","DrawMoney_3");
		icon3.setClassName("com.chinamworld.bocmbci.biz.drawmoney.drawfromagencey.DrawInputInfoAcitivity");
		ImageAndText icon4 = new ImageAndText(R.drawable.icon_left_draw_query, "取款查询","DrawMoney_4");
		icon4.setClassName("com.chinamworld.bocmbci.biz.drawmoney.drawquery.DrawQueryAcitivity");

		DrawMoneyLeftList.add(icon1);
		DrawMoneyLeftList.add(icon2);

		DrawMoneyLeftList.add(icon3);
		DrawMoneyLeftList.add(icon4);

	}
	/** 取款模块的左侧菜单数据 (未签约代理点时) */
	public static ArrayList<ImageAndText> DrawMoneyLeftListNoSigned = null;
	static {

		DrawMoneyLeftListNoSigned = new ArrayList<ImageAndText>();
		ImageAndText icon1 = new ImageAndText(R.drawable.icon_left_remit, "汇往取款人","DrawMoneyLeftListNoSigned_1");
		icon1.setClassName("com.chinamworld.bocmbci.biz.drawmoney.remitout.RemitCardListActivity");
		ImageAndText icon2 = new ImageAndText(R.drawable.icon_left_remit_query, "汇出查询","DrawMoneyLeftListNoSigned_2");
		icon2.setClassName("com.chinamworld.bocmbci.biz.drawmoney.remitquery.RemitQueryChooseCardActivity");
		DrawMoneyLeftListNoSigned.add(icon1);
		DrawMoneyLeftListNoSigned.add(icon2);
	}
	/** 主动收款左侧菜单数据 */
	@BocModule("主动收款")
	public static ArrayList<ImageAndText> GatherInitiativeLeftList = null;
	static {

		GatherInitiativeLeftList = new ArrayList<ImageAndText>();
		ImageAndText icon1 = new ImageAndText(R.drawable.icon_left_gather_query, "收款指令查询","GatherInitiative_1");
		icon1.setClassName("com.chinamworld.bocmbci.biz.gatherinitiative.gatherquery.GatherQueryActivity");
		ImageAndText icon2 = new ImageAndText(R.drawable.icon_left_pay_query, "付款指令查询","GatherInitiative_2");
		icon2.setClassName("com.chinamworld.bocmbci.biz.gatherinitiative.payquery.PayQueryActivity");
		ImageAndText icon3 = new ImageAndText(R.drawable.icon_left_commen_payer, "常用付款人","GatherInitiative_3");
		icon3.setClassName("com.chinamworld.bocmbci.biz.gatherinitiative.commenpayer.CommenPayerActivity");

		GatherInitiativeLeftList.add(icon1);
		GatherInitiativeLeftList.add(icon2);
		GatherInitiativeLeftList.add(icon3);
	}
	/** 跨行资金左侧菜单数据 */
	@BocModule("跨行资金归集")
	public static ArrayList<ImageAndText> CollectLeftList = null;
	static {
		CollectLeftList = new ArrayList<ImageAndText>();
		ImageAndText icon1 = new ImageAndText(R.drawable.icon_left_khzjgj, "跨行资金归集","Collect_1");
		icon1.setClassName("com.chinamworld.bocmbci.biz.tran.collect.CollectMainActivity");
		CollectLeftList.add(icon1);
	}

	/*** 跨境汇款 左侧菜单 **/
	@BocModule("跨境汇款")
	public static ArrayList<ImageAndText> crossBorderRemitLeftListData = null;
	static {
		crossBorderRemitLeftListData = new ArrayList<ImageAndText>();
		ImageAndText icon1 = new ImageAndText(R.drawable.remittance_apply, "向境外他行\n汇款","crossBorderRemit_1");
		icon1.setClassName("com.chinamworld.bocmbci.biz.remittance.applicationForRemittance.RemittanceInfoInputActivity");
		ImageAndText icon2 = new ImageAndText(R.drawable.remittance_manage, "汇款模板\n管理","crossBorderRemit_2");
		icon2.setClassName("com.chinamworld.bocmbci.biz.remittance.templateManagement.RemittanceTemplateListActivity");
		ImageAndText icon3 = new ImageAndText(R.drawable.remittance_query, "汇款记录\n查询","crossBorderRemit_3");
		icon3.setClassName("com.chinamworld.bocmbci.biz.remittance.recordQuery.RemittanceRecordQueryListActivity");

		ImageAndText icon4 = new ImageAndText(R.drawable.remittance_apply_over, "向境外中行\n汇款","crossBorderRemit_4");
		icon4.setClassName("com.chinamworld.bocmbci.biz.remittance.applicationForRemittance.OverseasChinaBankRemittanceInfoInputActivity");

		crossBorderRemitLeftListData.add(icon1);
		crossBorderRemitLeftListData.add(icon4);
		crossBorderRemitLeftListData.add(icon2);
		crossBorderRemitLeftListData.add(icon3);

	}
	
	/** 账单缴费 */
	@BocModule("账单缴费")
	public static ArrayList<ImageAndText> PaymentLeftList = null;
	static {
		PaymentLeftList = new ArrayList<ImageAndText>();
		ImageAndText imageAndText = new ImageAndText(R.drawable.fast_zdjf, RUtil.getString(R.string.mian_menu7),"Payment_1");
		imageAndText.setClassName(LocalData.PAYMENTACTIVITY);
		PaymentLeftList.add(imageAndText);
	}
	/** 关联账户转账转出支持类型 */
	public final static List<String> transOut = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;

		{
			add(ConstantGloble.ACC_TYPE_ORD);
			add(ConstantGloble.ACC_TYPE_BRO);
			add(ConstantGloble.ACC_TYPE_RAN);
			add(ConstantGloble.ACC_TYPE_GRE);
			add(ConstantGloble.ZHONGYIN);// P601 增加 103
			add(ConstantGloble.SINGLEWAIBI);// P601 增加 107
			add(AccBaseActivity.YOUHUITONGZH);
		}
	};
	/** 关联账户转账转入支持类型 */
	public final static List<String> transIn = new ArrayList<String>() {
		{
			add(ConstantGloble.ACC_TYPE_BRO);
			add(ConstantGloble.ACC_TYPE_ORD);
			add(ConstantGloble.ACC_TYPE_RAN);
			add(AccBaseActivity.YOUHUITONGZH);
			add(ConstantGloble.GREATWALL);
			add(ConstantGloble.ZHONGYIN);
			add(ConstantGloble.SINGLEWAIBI);
			add(ConstantGloble.ACC_TYPE_BOCINVT);// 网上专属理财账户——只能作为关联账户转账的转入账户
		}
	};
	/** ic卡关联检验 */
	public final static Map<String, String> linkFlag = new HashMap<String, String>() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "未关联");
			put("1", "已关联");
		}
	};
	public final static List<String> linkList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("未关联");
			add("已关联");
		}
	};
	/** 自助关联 待关联账户类型 */
	public final static List<String> relevanceAccountTypeList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("借记卡");
			add("信用卡");
			add("芯片卡");
		}
	};
	/** 明细查询 币种 */
	public final static List<String> queryCurrencyList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("人民币元");
			add("美元");
			add("日元");
			add("欧元");
		}
	};
	/** 明细查询 币种 ------将币种名称转换成币种代码 */
	public final static Map<String, String> queryCurrencyMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("人民币元", "001");
			put("美元", "014");
			put("日元", "027");
			put("欧元", "038");
		}
	};
	/** 投资理财服务 */
	public static final Map<String, String> serviceMap = new HashMap<String, String>() {
		{
			put("04-005", "买债券");
			put("04-006", "买基金");
			put("04-012", "买外汇");
			put("04-016", "买理财");
		}
	};
	/** 投资理财服务 */
	public static final List<String> serviceMapList = new ArrayList<String>() {
		{
			add("买债券");
			add("买基金");
			add("买外汇");
			add("买理财");
		}
	};
	/** 投资理财服务 */

	public static final List<String> serviceCodelist = new ArrayList<String>() {
		{
			add("04-005");
			add("04-006");
			add("04-012");
			add("04-016");
		}
	};
	/** 自助关联 带关联账户证件类型 */
	public final static List<String> relevanceIdentityTypeList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("身份证");
			add("临时居民身份证");
			add("户口簿");
			add("军人身份证");
			add("武装警察身份证");
			add("港澳居民通行证");
			add("台湾居民通行证");
			add("护照");
			add("其他证件");
			add("港澳台居民往来内地通行证");
			add("外交人员身份证");
			add("外国人居留许可证");
			add("边民出入境通行证");
			add("其他");
		}
	};
	/**
	 * 关联账户类型代码
	 */
	public static final List<String> cardcodeList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("119");
			add("103");
			add("104");
			add("300");
		}
	};
	/**
	 * 关联账户类型
	 */
	public final static Map<String, String> accountType = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("借记卡", "119");
			put("信用卡", "103");
			put("芯片卡", "104");
		}
	};
	/** 排序 */
	public final static String[] sortMap = new String[] {

	("全部"), ("收入"), ("支出")

	};
	/** 汇款查询的筛选 */
	public final static String[] filterMap = new String[] {

	("全部"), ("未收款"), ("已收款")

	};
	/** 汇款查询列表的数据筛选 */
	public final static String[] filterRemitStatus = new String[] {

	("全部"), ("汇款成功"), ("汇款失败")

	};

	/** 现金宝查询列表的数据筛选 */
	public final static String[] filterCashBankTransStatus = new String[] {

	("全部"), ("转入"), ("转出")

	};

	/** 排序图标 */
	public final static int[] sortIconMap = new int[] {

	R.drawable.icon_paixu_time, R.drawable.icon_paixu_shouru, R.drawable.icon_paixu_zhichu

	};

	/** dept排序 */
	public final static String[] deptSortMap = new String[] {

	("时间排序"), ("正常在前"), ("已逾期在前"), ("已结清前")

	};
	/** 账户管理 wangmengmeng add end */
	/**
	 * 中银理财 wangmengmeng add start
	 */
	@BocModule("中银理财")
	public static ArrayList<ImageAndText> bocinvtManagerLeftList = null;
	static {
		bocinvtManagerLeftList = new ArrayList<ImageAndText>();
		ImageAndText icon1 = new ImageAndText(R.drawable.icon_left_bocivt_acct, "账户管理","bocinvtManager_1");
		icon1.setClassName("com.chinamworld.bocmbci.biz.bocinvt.acctmanager.BocinvtAcctListActivity");
		ImageAndText icon2 = new ImageAndText(R.drawable.icon_left_wdlccp, "我的理财产品","bocinvtManager_2");
		icon2.setClassName(MyInvetProductActivity.class.getName());
//		icon2.setClassName(MyProductListActivity.class.getName());
//		icon2.setClassName("com.chinamworld.bocmbci.biz.bocinvt.myproduct.MyProductListActivity");
		icon2.setOutlayClassName(QueryProductOutlayActivity.class.getName());//P603原理财
//		icon2.setOutlayClassName(ProductQueryAndBuyOutlayActivity.class.getName());//P603新理财(业务确认使用原外置页面)
		ImageAndText icon3 = new ImageAndText(R.drawable.icon_left_lccplb, "产品查询与购买", false,"bocinvtManager_3");
		icon3.setClassName("com.chinamworld.bocmbci.biz.bocinvt.productlist.QueryProductActivity");
		icon3.setOutlayClassName("com.chinamworld.bocmbci.biz.bocinvt.productlist.QueryProductOutlayActivity");
		ImageAndText icon5 = new ImageAndText(R.drawable.icon_left_xycx, "投资协议管理","bocinvtManager_4");
//		icon5.setClassName("com.chinamworld.bocmbci.biz.bocinvt.queryagreement.QueryAgreeActivity");
		icon5.setClassName("com.chinamworld.bocmbci.biz.bocinvt_p603.inverstagreemanager.InvestInvalidAgreeQueryActivity");
		ImageAndText icon6 = new ImageAndText(R.drawable.icon_left_lctj, "理财推荐","bocinvtManager_5");
		icon6.setClassName("com.chinamworld.bocmbci.biz.bocinvt.myproduct.OcrmProductListActivity");

		// TODO 是否已做？
		ImageAndText icon4 = new ImageAndText(R.drawable.icon_left_lijy, "历史交易查询","bocinvtManager_6");
		icon4.setClassName("com.chinamworld.bocmbci.biz.bocinvt.dealhistory.QueryHistoryProductActivity");
		bocinvtManagerLeftList.add(icon2);
		bocinvtManagerLeftList.add(icon3);
		bocinvtManagerLeftList.add(icon5);
		bocinvtManagerLeftList.add(icon1);
		bocinvtManagerLeftList.add(icon4);
		bocinvtManagerLeftList.add(icon6);
	}
	// 组合查询
	/** 产品币种 */
	public static final List<String> bocinvtProductCurCode = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("全部");
			add("人民币元");
			add("美元");
			add("英镑");
			add("港币");
			add("加拿大元");
			add("澳大利亚元");
			add("欧元");
			add("日元");
		}
	};
	public static final Map<String, String> bociCurcodeMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("全部", "000");
			put("人民币元", "001");
			put("美元", "014");
			put("英镑", "012");
			put("港币", "013");
			put("加拿大元", "028");
			put("澳大利亚元", "029");
			put("欧元", "038");
			put("日元", "027");
			// @"1": @"美元",
			// @"2": @"日元",
			// @"3": @"欧元",
			// @"4": @"港币",
			// @"5": @"英镑",
			// @"6": @"澳元",
			// @"7": @"加元"

		}
	};

	/** 产品属性分类 */

	public static final List<String> bocinvtProductType = new ArrayList<String>() {
		/**
			 * 
			 */
		private static final long serialVersionUID = 1L;

		{
			add("全部");
			add("自营产品");
			add("代销产品");

		}
	};
	public static final Map<String, String> bociProductTypeMap = new HashMap<String, String>() {
		/**
			 * 
			 */
		private static final long serialVersionUID = 1L;

		{
			put("全部", "0");
			put("自营产品", "1");
			put("代销产品", "2");

		}
	};
	public static final Map<String, String> boci_ProductTypeMap = new HashMap<String, String>() {
		/**
			 * 
			 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "全部");
			put("1", "自营产品");
			put("2", "代销产品");

		}
	};

	/** 产品风险类型 */
	public static final List<String> bocinvtProductRiskType = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("全部");
			add("保本固定收益");
			add("保本浮动收益");
			add("非保本浮动收益");
		}
	};
	public static final Map<String, String> bociRiskTypeMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("全部", "0");
			put("保本固定收益", "1");
			put("保本浮动收益", "2");
			put("非保本浮动收益", "3");
		}
	};
	public static final Map<String, String> boci_RiskTypeMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "全部");
			put("1", "保本固定收益");
			put("2", "保本浮动收益");
			put("3", "非保本浮动收益");
		}
	};
	/** 产品期限 */
	public static final List<String> bocinvtProdTimeLimit = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("全部");
			add("3个月以内");
			add("3个月-6个月");
			add("6个月-12个月");
			add("12个月-24个月");
			add("24个月以上");
		}
	};
	public static final Map<String, String> bocitimeLimitMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("全部", "0");
			put("3个月以内", "1");
			put("3个月-6个月", "2");
			put("6个月-12个月", "3");
			put("12个月-24个月", "4");
			put("24个月以上", "5");
		}
	};
	public static final Map<String, String> boci_timeLimitMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "全部");
			put("1", "3个月以内");
			put("2", "3个月-6个月");
			put("3", "6个月-12个月");
			put("4", "12个月-24个月");
			put("5", "24个月以上");
		}
	};
	/** 产品销售状态 */
	public static final List<String> bocinvtXpadStatus = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("全部产品");
			add("在售产品");
			add("停售产品");
		}
	};
	public static final Map<String, String> bociStatusMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("全部产品", "0");
			put("在售产品", "1");
			put("停售产品", "2");
		}
	};
	public static final Map<String, String> boci_StatusMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "全部产品");
			put("1", "在售产品");
			put("2", "停售产品");
		}
	};
	public static final List<String> periodicalList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("非周期性产品");
			add("周期性产品");
		}
	};
	/** 产品风险级别 */
	public static final Map<String, String> boci_prodRisklvlMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "低风险产品");
			put("1", "中低风险产品");
			put("2", "中等风险产品");
			put("3", "中高风险产品");
			put("4", "高风险产品");
		}
	};

	/** 产品风险级别 */
	public static final Map<String, String> boci_prodRisklvl_OutlayMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "低风险");
			put("1", "中低风险");
			put("2", "中等风险");
			put("3", "中高风险");
			put("4", "高风险");
		}
	};
	/** 风险评估1——女;2——男 */
	public static List<String> genderList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("1");
			add("2");
		}
	};

	// 风险评估问题//////////////TODO
	// 要更改问题以及答案/////////////////////////////////////////////////////////////////////
	public static final List<String> questionTitleList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("1.您的年龄是？ ");
			add("2.您的家庭年收入为（折合人民币）？  ");
			add("3.在您每年的家庭收入中，可用于作金融投资（储蓄存款除外）的比例为？ ");
			add("4.以下哪项最能说明您的投资经验？ ");
			add("5.您有多少年投资股票、基金、外汇、金融衍生产品等风险投资品的经验？ ");
			add("6.以下哪项描述最符合您的投资态度？  ");
			add("7.以下情况，您会选择哪一种？  ");
			add("8.您计划的投资期限是多久？ ");
			add("9.您的投资目的是？  ");
			add("10.您投资产品的价值出现何种程度的波动时，您会呈现明显的焦虑？ ");
		}
	};
	// 选项
	public static final List<List<String>> answerList = new ArrayList<List<String>>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add(new ArrayList<String>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				{
					add("18-30");
					add("31-50");
					add("51-64");
					add("65岁及以上");
				}
			});
			add(new ArrayList<String>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				{
					add("5万元以下");
					add("5-20万元");
					add("20-50万元");
					add("50-100万元");
					add("100万元以上");
				}
			});
			add(new ArrayList<String>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				{
					add("小于10%");
					add("10%至25%");
					add("25%至50%");
					add("大于50%");
				}
			});
			add(new ArrayList<String>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				{
					add("除存款、债券外，我几乎不投资其他金融产品 ");
					add("大部分投资于存款、债券等，较少投资于股票、基金等风险产品 ");
					add("资产均衡地分布于存款、债券、银行理财产品、信托产品、股票、基金等");
					add("大部分投资于股票、基金、外汇等高风险产品，较少投资于存款、债券");
				}
			});
			add(new ArrayList<String>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				{
					add("没有经验");
					add("少于2年");
					add("2至5年");
					add("5至8年");
					add("8年以上");
				}
			});
			add(new ArrayList<String>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				{
					add("厌恶风险，不希望本金损失，希望获得稳定回报");
					add("保守投资，不希望本金损失，愿意承担一定幅度的收益波动 ");
					add("寻求资金的较高收益和成长性，愿意为此承担有限本金损失");
					add("希望赚取高回报，愿意为此承担较大本金损失");
				}
			});
			add(new ArrayList<String>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				{
					add("有100%的机会赢取1000元现金 ");
					add("有50%的机会赢取5万元现金");
					add("有25%的机会赢取50万元现金 ");
					add("有10%的机会赢取100万元现金");
				}
			});
			add(new ArrayList<String>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				{
					add("1年以下");
					add("1-3年");
					add("3-5年");
					add("5年以上");
				}
			});
			add(new ArrayList<String>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				{
					add("资产保值");
					add("资产稳健增长");
					add("资产迅速增长 ");
				}
			});
			add(new ArrayList<String>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				{
					add("本金无损失，但收益未达预期 ");
					add("出现轻微本金损失");
					add("本金10％以内的损失 ");
					add("本金20-50％的损失");
					add("本金50％以上损失");
				}
			});

		}
	};
	// 分数
	public static final List<List<Integer>> riskScoreList = new ArrayList<List<Integer>>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add(new ArrayList<Integer>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				{
					add(-2);
					add(0);
					add(-4);
					add(-10);
				}
			});
			add(new ArrayList<Integer>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				{
					add(0);
					add(2);
					add(6);
					add(8);
					add(10);
				}
			});
			add(new ArrayList<Integer>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				{
					add(2);
					add(4);
					add(8);
					add(10);
				}
			});
			add(new ArrayList<Integer>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				{
					add(0);
					add(2);
					add(6);
					add(10);
				}
			});
			add(new ArrayList<Integer>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				{
					add(0);
					add(2);
					add(6);
					add(8);
					add(10);
				}
			});
			add(new ArrayList<Integer>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				{
					add(0);
					add(4);
					add(8);
					add(10);
				}
			});
			add(new ArrayList<Integer>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				{
					add(0);
					add(4);
					add(6);
					add(10);
				}
			});
			add(new ArrayList<Integer>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				{
					add(4);
					add(6);
					add(8);
					add(10);
				}
			});
			add(new ArrayList<Integer>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				{
					add(2);
					add(6);
					add(10);
				}
			});
			add(new ArrayList<Integer>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				{
					add(-5);
					add(5);
					add(10);
					add(15);
					add(20);
				}
			});

		}
	};
	/** 风险评估等级 */
	public static final List<String> riskLevelList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("保守型投资者");
			add("稳健型投资者");
			add("平衡型投资者");
			add("成长型投资者");
			add("进取型投资者");
		}
	};
	public static final List<String> riskLevelCodeList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("1");
			add("2");
			add("3");
			add("4");
			add("5");
		}
	};
	public static final Map<String, String> riskLevelMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("1", "保守型投资者");
			put("2", "稳健型投资者");
			put("3", "平衡型投资者");
			put("4", "成长型投资者");
			put("5", "进取型投资者");
		}
	};
	public static final Map<String, String> myriskLevelMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("1", "我是保守型投资者");
			put("2", "我是稳健型投资者");
			put("3", "我是平衡型投资者");
			put("4", "我是成长型投资者");
			put("5", "我是进取型投资者");
		}
	};
	public static final Map<String, String> riskLevelDetailMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("1",
					"您的风险容忍程度非常低，在任何投资中，保护本金不受损失和保持资产的流动性是您的首要目标。您对投资的态度是希望投资收益极度稳定，不愿承担高风险以换取高收益，通常不太在意资金是否有较大增值，不愿意承受投资波动对心理的煎熬，追求稳定。您比较适合投资存款、债券、保本保收益或收益波动性较小的理财产品、保障型保险产品、保本基金和货币型基金。 ");
			put("2",
					"您的风险承受能力较低。在任何投资中，稳定是您首要考虑的因素，一般您希望在保证本金安全的基础上能有一些增值收入，追求较低的风险，对投资回报的要求不高。您比较适合投资存款、债券、保本型理财产品、保障型保险产品、债券型基金和保本基金等，也可配置少部分混合基金。");
			put("3",
					"在任何投资中，在风险较小的情况下获得一定的收益是您主要的投资目的。您通常愿意使本金面临一定的风险，但在做投资决定时，会仔细的对将要面临的风险进行认真的分析。您对风险总是客观存在的道理有清楚的认识。总体来看，愿意承受市场的平均风险，您比较适合投资债券、保本型、浮动型理财产品、保障型保险产品、债券型基金、混合型基金、FOF型基金，也可配置少部分股票型基金。");
			put("4",
					"在任何投资中，您希望有较高的投资收益，但又不愿承受较大的风险；可以承受一定的投资波动，但是希望自己的投资风险小于市场的整体风险。您有较高的收益目标，且对风险有清醒的认识，您比较适合投资与利率、汇率、商品等挂钩的浮动型中银理财计划I产品、保障型保险产品、投连险、混合型基金、FOF基金、封闭式基金、QDII产品、股票型基金、指数型基金等。");
			put("5",
					"在任何投资中，您通常专注于投资的长期增值，并愿意为此承受较大的风险。短期的投资波动并不会对您造成大的影响，追求超高的回报才是您关注的目标。您比较适合投资浮动收益型理财产品、保障型保险产品、投连险、QDII产品、股票型基金、指数型基金、股票、权证、衍生工具等另类投资产品。");
		}
	};
	/** 是否有投资经验 */
	public static final Map<String, String> hasInvestExperienceMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "具备");
			put("1", "不具备");
		}
	};
	/** 风险评估——收入 */
	public static final List<String> bocirevenueList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("5万以下");
			add("5-10万");
			add("10-20万");
			add("20-50万");
			add("50万以上");
		}
	};
	/** 风险评估——教育 */
	public static final List<String> bocieduList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("小学");
			add("高中/中专");
			add("大学");
			add("研究生");
			add("研究生以上");
		}
	};
	/** 风险评估——行业 */
	public static final List<String> bocioccList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("政府部门");
			add("金融");
			add("房地产");
			add("商贸");
			add("计算机服务/软件业");
			add("制造业");
			add("科教文");
			add("传媒/艺术/文体");
			add("服务业");
			add("自由职业");
		}
	};
	/** 是否处于挂单时间——0:是;1:否 */
	public static final List<String> orderTimeMap = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("0");
			add("1");
		}
	};
	/** 成交类型 */
	public static final Map<String, String> tranFlagMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "正常");
			put("1", "挂单");
		}
	};
	/** 基础金额模式 */
	public static final Map<String, String> bociAmountTypeMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "定额");
			put("1", "不定额");
		}
	};
	/** 分红方式 */
	public static final Map<String, String> bocimodeMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("现金分红", "1");
			put("红利再投资", "0");
		}
	};
	public static final Map<String, String> bocicurrentmodeMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("1", "现金分红");
			put("0", "红利再投资");
		}
	};
	/** 交易渠道 */
	public static final Map<String, String> bociChannelMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("00", "理财系统交易");
			put("01", "（核心系统 OFP）柜面");
			put("02", "网上银行");
			put("03", "电话银行自助");
			put("04", "电话银行人工");
			put("05", "手机银行");
			put("06", "家居银行");
			put("07", "微信银行");
			put("08", "自助终端");
			put("09", "OCRM");
			put("10", "中银易商平台");
			put("11", "开放平台移动客户端");
			put("0", "理财系统交易");
			put("1", "（核心系统 OFP）柜面");
			put("2", "网上银行");
			put("3", "电话银行自助");
			put("4", "电话银行人工");
			put("5", "手机银行");
			put("6", "家居银行");
			put("7", "微信银行");
			put("8", "自助终端");
			put("9", "OCRM");
		}
	};
	/** 币种过滤 */
	public static Map<String, String> currencyboci = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("034", "美元金");
			put("XAU", "美元金");
			put("035", "人民币金");
			put("GLD", "人民币金");
			put("068", "人民币银");
			put("036", "美元银");
			put("045", "美元铂金");
			put("845", "人民币铂金");
			put("841", "美元钯金");
			put("844", "人民币钯金");
		}
	};
	/** 状态 */
	public static final Map<String, String> bociHisStatusMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "委托待处理");
			put("1", "成功");
			put("2", "失败");
			put("3", "已撤销");
			put("4", "已冲正");
			put("5", "已赎回");

		}
	};
	
	/** 状态 */
	public static final Map<String, String> bociGuarantyMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "正常");
			put("1", "已撤销");
		}
	};
	
	/** 理财交易详情 交易状态 **/
	public static final Map<String,String> bociIndoDetailStatusMap = new HashMap<String, String>(){

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		{
			put("0", "委托待处理");
			put("1", "成功");
			put("2", "失败");
			put("3", "已撤销");
			put("4", "已冲正");
			put("5", "已赎回");
			put("6", "已失效");
			put("7", "已暂停");
			put("8", "已到期（已结束）");
			put("9", "已终止");
			put("10", "赎回失败");
			put("11", "购买失败");
			put("12", "强制赎回失败");
			put("13", "认购失败");
			put("14", "部分撤销");
		}
		
	};
	
	/** 交易属性 */
	public static final Map<String, String> bociTranAtrrMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "常规交易");
			put("1", "自动续约交易");
			put("2", "预约交易");
			put("3", "组合购买");
			put("4", "自动投资交易");
			put("5", "委托交易");
			put("6", "短信委托");
			put("7", "产品转入");
			put("8", "产品转出");
			put("9", "组合购买");
			put("00", "常规交易");
			put("01", "自动续约交易");
			put("02", "预约交易");
			put("03", "组合购买");
			put("04", "自动投资交易");
			put("05", "委托交易");
			put("06", "短信委托");
			put("07", "产品转入");
			put("08", "产品转出");
			put("09", "组合购买");
			put("10", "委托交易");
			put("11", "产品转让");
			put("12", "周期投资");
			put("13", "本金摊还");

		}
	};
	/** 交易类型 */
	public static final Map<String, String> bociTrfTypeMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("00", "认购");
			put("01", "申购");
			put("02", "赎回");
			put("03", "红利再投");
			put("04", "红利发放");
			put("05", "（经过）利息返还");
			put("06", "本金返还");
			put("07", "起息前赎回");
			put("08", "利息折份额");
			put("09", "赎回亏损");
			put("10", "赎回盈利");
			put("11", "产品转让");
			put("12", "份额转换");
		}
	};
	
	/** 交易类型 */
	public static final Map<String, String> bociHisTrfTypeMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("00", "认购");
			put("01", "申购");
			put("02", "赎回");
			put("03", "红利再投");
			put("04", "红利发放");
			put("05", "（经过）利息返还");
			put("06", "本金返还");
			put("07", "起息前赎回");
			put("08", "利息折份额");
			put("09", "赎回亏损");
			put("10", "赎回盈利");
			put("11", "产品转让");
			put("12", "份额转换");
		}
	};
	
	/** 部分赎回 */
	public static final List<String> bocicanPartlyRedeem = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("是");
			add("否");
		}
	};
	/** 中银理财 wangmengmeng add end */
	/**
	 * 贷款管理 wangmengmeng add start
	 */

	/** 提前还款方式 */
	public static final Map<String, String> loancountType = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("还款额不变", "1");
			put("期数不变", "2");
		}
	};
	/** 提前还款方式 */
	public static final List<String> loancountTypeList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("还款额不变");
			add("期数不变");
		}
	};
	/** 贷款管理 wangmengmeng add end */
	/**
	 * 转账汇款
	 * 
	 * @author wangchao
	 */
	@BocModule("转账汇款")
	public static ArrayList<ImageAndText> tranManagerLeftList = null;
	static {
		tranManagerLeftList = new ArrayList<ImageAndText>();
		ImageAndText icon1 = new ImageAndText(R.drawable.tran_mytrans, "我要转账","tranManager_1");
		icon1.setClassName("com.chinamworld.bocmbci.biz.tran.mytransfer.currenttran.TransferManagerActivity1");

		ImageAndText icon2 = new ImageAndText(R.drawable.tran_mobile, "手机号转账","tranManager_2");
		// icon2.setMustLogin(false);
		icon2.setClassName("com.chinamworld.bocmbci.biz.tran.mobiletrans.MobileTransThirdMenu");

		ImageAndText icon3 = new ImageAndText(R.drawable.tran_two_dimen, "二维码转账","tranManager_3");
		// icon3.setMustLogin(false);
		icon3.setClassName("com.chinamworld.bocmbci.biz.tran.twodimentrans.TwoDimenTransActivity1");

		ImageAndText icon5 = new ImageAndText(R.drawable.tran_manage, "转账管理","tranManager_4");
		// icon5.setMustLogin(false);
		icon5.setClassName("com.chinamworld.bocmbci.biz.tran.managetrans.ManageTransActivity");
		ImageAndText icon6 = new ImageAndText(R.drawable.icon_left_atm, "ATM无卡取款","tranManager_5");
		// icon6.setMustLogin(false);
		icon6.setClassName("com.chinamworld.bocmbci.biz.tran.atmremit.AtmThirdMenu");
		ImageAndText iconRemit = new ImageAndText(R.drawable.icon_left_hkbstc, "汇款套餐","tranManager_6");
		iconRemit.setClassName("com.chinamworld.bocmbci.biz.tran.remit.RemitThirdMenu");
		// 跨行归集
		ImageAndText iconCollect = new ImageAndText(R.drawable.icon_kuahangzijinguijie, "跨行资金归集","tranManager_7");
		iconCollect.setClassName("com.chinamworld.bocmbci.biz.tran.collect.CollectMainActivity");


		// 电子卡 转账
		ImageAndText iconBind = new ImageAndText(R.drawable.icon_dianzikazhuanzhang, "电子卡转账","tranManager_8");
		iconBind.setClassName("com.chinamworld.bocmbci.biz.tran.bind.TransferEcardActivity");

		tranManagerLeftList.add(icon1);
		tranManagerLeftList.add(icon2);
		tranManagerLeftList.add(icon3);
		tranManagerLeftList.add(icon5);
		// TODO 转账401需屏蔽
		tranManagerLeftList.add(icon6);
		// TODO 汇款笔数套餐402需屏蔽

		tranManagerLeftList.add(iconRemit);
		tranManagerLeftList.add(iconCollect);
		
//		yuht.屏蔽601电子卡转账
		tranManagerLeftList.add(iconBind);// 电子卡转账



	}
	public static ArrayList<String> functionListData = null;
	static {
		functionListData = new ArrayList<String>();
		functionListData.add("1.我的账户");
		functionListData.add("2.我的账户");
		functionListData.add("3.我的账户");
		functionListData.add("4.我的账户");
		functionListData.add("5.我的账户");
	}

	public static ArrayList<ArrayList<String>> functionsData = null;

	private static ArrayList<String> first_menu = new ArrayList<String>() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("我的账户1");
			add("我的账户1");
			add("我的账户1");
			add("我的账户1");
		}
	};
	private static ArrayList<String> second_menu = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("我的账户2");
			add("我的账户2");
			add("我的账户2");
			add("我的账户2");
		}
	};
	private static ArrayList<String> three_menu = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("我的账户3");
			add("我的账户3");
			add("我的账户3");
			add("我的账户3");
		}
	};
	private static ArrayList<String> four_menu = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("我的账户4");
			add("我的账户4");
			add("我的账户4");
			add("我的账户4");
		}
	};
	private static ArrayList<String> five_menu = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("我的账户5");
			add("我的账户5");
			add("我的账户5");
			add("我的账户5");
		}
	};
	private static ArrayList<String> six_menu = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("我的账户6");
			add("我的账户6");
			add("我的账户6");
			add("我的账户6");
		}
	};

	static {
		functionsData = new ArrayList<ArrayList<String>>();
		functionsData.add(first_menu);
		functionsData.add(second_menu);
		functionsData.add(three_menu);
		functionsData.add(four_menu);
		functionsData.add(five_menu);
		functionsData.add(six_menu);
	}

	/**
	 * 银行卡类型
	 */
	public static final List<String> cardList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("长城借记卡");
			add("信用卡");
			add("准贷记卡");
			add("单外币信用卡");
		}
	};

	/**
	 * 银行卡类型 相应的值
	 */
	public static final Map<String, String> cardMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("长城借记卡", "119");
			put("信用卡", "103");
			put("准贷记卡", "104");
			put("单外币信用卡", "107");
		}
	};

	/**
	 * 证件类型
	 */
	public static List<String> idList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("身份证");
			add("军人身份证");
			add("护照");
			add("其他证件");
		}
	};

	/**
	 * 配置地址
	 */
	public static LinkedHashMap<String, String> addressApi = new LinkedHashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{

			if (SystemConfig.ENV.equals(SystemConfig.ENV_SIT)) {
				// SIT
				put("403 SIT 不验密", "http://22.188.20.118:9088/BII/");
				put("403 SIT 验密", "http://22.188.20.117:9086/BII/");
			} else {


//				put("宣武门挡板", "http://60.253.195.94:8099/BIISimulate/"); // 新挡板
				put("广源", "http://22.18.61.188:9194/BIISimulate/"); // 新挡板
				put("资产管理", "http://22.11.147.98:8080/BII/");
				put("广源1", "http://22.18.61.188:9194/BIISimulate1/"); // 新挡板
				put("宣武门挡板", "http://boc.lanjianhui.cn:8099/BIISimulate/_bfwajax.do");//宣武门挡板
//				put("大额存单", "http://22.11.140.206:8080/BII/"); // 联调地址
//		    	put("UT3", "http://22.188.178.31:9083/BII/");// 新挡板T3
//				put("UT2", "http://22.188.137.21:8189/BII/");// 新挡板T2
//				put("UT1", "http://22.188.130.126/BII/");// 新挡板T1
//				put("T1不验密", "http://22.188.130.120:9080/BII/"); // 新挡板
//				put("信用卡_18", "http://22.11.147.18:8080/BII/");// 信用卡联调
//				put("信用卡_188", "http://22.11.147.188:8080/BII/");// 信用卡联调
//				put("信用卡BII地址", "http://22.11.147.188:8080/BII/");// 信用卡联调
//				put("外网T2", "http://180.168.146.66:81/BII/");// 外网T2
//				put("内网T2", "http://22.188.137.21:8190/BII/");// 内网T2
				put("T1(https)", "https://22.188.135.115/BII/");
				put("T1不验密", "http://22.188.130.126:2468/BII/");
				put("T2", "https://22.188.151.87/BII/");// 内网T2 2016年5月17日
				put("T2不验密", "https://22.188.151.87:8190/BII/");// 内网T2 2016年5月17日
//				put("内网T2", "http://22.188.137.21:8189/BII/");// 内网T2
//				put("电子卡", "http://22.11.140.205:8080/BII/");// 新挡板T1
//				put("手续费", "http://22.11.64.166:8080/BII/");// 新挡板T1
//				put("外网T4", "http://180.168.146.79/BII/");
//				put("内网T4", "http://22.188.24.218/BII/");
				put("T5验密", "https://22.188.151.167:8042/BII/");
				put("T5不验密", "https://22.188.151.167:10002/BII/");	
				put("资产管理", "http://22.11.147.98:8080/BII/");			
				put("积利金联调地址", "http://22.11.147.90:8080/BII/");
//				put("D6验密", "http://22.188.20.139:9089/BII/"); // 新挡板	
//				put("D6不验密", "http://22.188.20.138:9081/BII/"); // 新挡板	
//				put("P603刘舒","http://22.11.65.215:8080/BII/"); 
//				22.11.65.215 8080
//				put("P603","http://22.11.140.206:8080/BII/");
				put("现金宝","http://22.11.147.30:8080/BII/");
//				http://22.11.140.20m,.m6:8080/BII/
//				http://22.18.61.194:8603/boc15/
//				put("P603结汇购汇测试","http://22.11.147.49:8080/BII/");

//				22.11.147.133  端口8080
				// put("测试", "http://192.168.1.108:8080/BIISimulate/");
				// put("音频key挡板地址", "http://22.188.20.183:9082/BII/");
				// put("U1/T1", "http://22.188.130.126/BII/");
				// put("UAT2", "http://22.188.137.21:9092/BII/");
				// put("P502专用", "http://22.188.137.21:8189/BII/");
				// put("T3", "http://22.188.178.31:9083/BII/");
				// put("T3功能外翻", "http://22.11.147.49:9090");
				// put("T3功能外翻", "http://22.11.147.49:9090/BII/");
				// put("个贷申请", "http://22.18.61.197:8520/BII/");
				// put("个贷申请UAT", "http://22.188.137.21:8189/BII/");
				//
				// put("生产", "https://ebsnew.boc.cn/BII/");
				// put("公网", "http://219.236.241.211:9192/BIISimulate/");
				// put("SIT 307 不验密", "http://22.188.20.98:9081/BII/");
				// put("SIT 307 验密", "http://22.188.16.32:9083/BII/");
				// put("SIT 117", "http://22.188.20.117:9095/BII/");
				// put("SIT 118", "http://22.188.20.118:9095/BII/");
				// put("SIT 248", "http://22.11.97.248:8080/BII/");
				// put("246", "http://22.11.97.246:8080/BII/");
				// put("基金401", "http://22.11.97.246/BIIgyg/");
				// put("默认账户设定", "http://22.11.64.81:8080/BII/");
				// put("信用卡", "http://22.11.64.146:8080/BII/");
				// put("跨行资金归集挡板", "http://22.11.64.171:8080/BII/");
				// put("功能外置", "http://22.188.137.21:8189/BII/");
				// put("UAT2", "http://22.188.137.21:9092/BII/");
				// put("联调大额存单挡板", "http://22.11.64.24:8080/BII/");
				// put("投产演练", "https://ebsnewppt.boc.cn:6443/BII/");
				// put("T43开户送电子银行", "http://22.11.64.115:8085/BII/");

				// put("生产", "https://ebsnew.boc.cn/BII/");
				// put("P502个贷", "http://22.18.61.197:8520/BII/");
				// put("套餐", "http://22.188.137.21:8189/BII/");
				put("中银理财P603-联调", "http://22.11.147.99:8080/BII/");
//				put("UT1不验密", "http://22.188.130.120:9080/BII/");// 新挡板T1不验密
			}
		}
	};

	/**
	 * 配置地址
	 */
	public static List<String> addressApiSIT = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{

			add("SIT 401 不验密");
			add("SIT 401 验密");

		}
	};

	/**
	 * 性别
	 */
	public static Map<String, String> gender = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "先生/女士");
			put("1", "先生");
			put("2", "女士");
		}
	};

	/**
	 * 修改交易限额 服务码
	 */
	public static Map<String, String> serviceCodeMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("PB021", "关联账户转账");
			put("PB031", "行内汇款服务");
			put("PB032", "跨行汇款服务");
			put("PB200", "电子支付");
			put("PB040", "自助缴费");
			put("PB022", "定期存款转账");
			put("PB071", "银期转账");
			put("PB012", "他人电子现金账户充值");
			put("PB011", "本人电子现金账户充值");
			put("PB035", "手机号转账");
			// put("PB118", "跨行实时定向付款");//pwe
			put("PB113", "跨行实时付款");
			put("PB034", "国内跨行定向汇款");
			put("PB033", "中行内定向转账汇款");
		}
	};

	/**
	 * 修改交易限额 服务码 11个
	 */
	public static List<String> serviceCodeList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("PB021");
			add("PB031");
			add("PB032");
			add("PB200");
			add("PB040");
			add("PB022");
			add("PB071");
			add("PB012");
			add("PB011");
			add("PB035");// 手机号转账
			// add("PB118");// pwe
			add("PB113");
			add("PB034");
			add("PB033");
		}
	};

	/**
	 * 贵金属交易方式 add by xyl
	 */
	public static List<String> prmsTradeMethodList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("市价即时");
			add("限价即时");
			if (PrmsControl.is401) {
				add("获利委托");
				add("止损委托");
				add("二选一委托");
			}
//			 &&PrmsControl.isSale
			if(PrmsControl.is603){
				add("追击止损委托");
			}
			
		}
	};
	public static List<String> prmsTradeMethodCodeList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{

			add("08");
			add("07");
			if (PrmsControl.is401) {
				add("03");
				add("04");
				add("05");
			}
		}
	};
	/**
	 * 贵金属交易方式 相应的值 add by xyl
	 */
	public static Map<String, String> prmsTradeMethodMaptoCode = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("市价即时", "08");
			put("限价即时", "07");
			put("获利委托", "03");
			put("止损委托", "04");
			put("二选一委托", "05");
		}
	};
	/**
	 * 贵金属交易方式 把代码变成中文显示 add by xyl
	 */
	public static Map<String, String> prmsTradeMethodMaptoChi = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("08", "市价即时");
			put("07", "限价即时");
			put("03", "获利委托");
			put("04", "止损委托");
			put("05", "二选一委托");
			put("11","追击止损委托");
		}
	};
	/**
	 * 贵金属交易种类 add by xyl
	 */
	public static List<String> prmsTradeStyleList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("黄金");
			add("白银");
			add("钯金");
			add("铂金");
		}
	};
	// /** 黄金 币种炒汇标示 列表 */
	// public static List<String> prmsGoldCurrencyList = new ArrayList<String>()
	// {
	// /**
	// *
	// */
	// private static final long serialVersionUID = 1L;
	//
	// {
	// add("人民币金");
	// add("美元金  现钞");
	// add("美元金  现汇");
	// }
	// };
	// /** 白银 币种炒汇标示 列表 */
	// public static Map<String, String> prmsCurrencyMap = new HashMap<String,
	// String>() {
	// /**
	// *
	// */
	// private static final long serialVersionUID = 1L;
	//
	// {
	// put("001", "人民币");
	// put("014", "美元");
	// }
	// };
	// /** 买入状态 币种炒汇标示 列表 */
	// public static List<String> prmsbuyCurrencyList = new ArrayList<String>()
	// {
	// /**
	// *
	// */
	// private static final long serialVersionUID = 1L;
	//
	// {
	// add("人民币元");
	// add("美元   现钞");
	// add("美元   现汇");
	// }
	// };
	/**
	 * 贵金属交易种类code add by xyl
	 */
	public static List<String> prmsTradeStyleCodeList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("035");
			add("068");
			add("034");
			add("036");
			add("844");
			add("845");
			add("841");
			add("045");
		}
	};
	/*
	 * 存款管理特殊的四个币种 新西兰元，丹麦克朗，挪威克朗，瑞典克朗 存期没有两年 澳大利亚元 韩元 卢布
	 */
	public static List<String> prmsTradeStyleCodeCurrencyList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("087");
			add("022");
			add("023");
			add("021");
			add("081");
			add("088");
			add("196");
		}
	};
	// /**
	// * 贵金属交易种类 把中文显示变成代码 add by xyl
	// */
	// public static Map<String, String> prmsTradeStyleMaptoCode = new
	// HashMap<String, String>() {
	// /**
	// *
	// */
	// private static final long serialVersionUID = 1L;
	//
	// {
	// put("人民币金", "035");
	// put("人民币银", "068");
	// put("美元金", "034");
	// put("美元银", "036");
	// put("人民币钯金", "844");
	// put("人民币铂金", "845");
	// put("美元钯金", "841");
	// put("美元铂金", "045");
	// }
	// };
	/**
	 * 贵金属交易种类 把代码变成中文显示 add by xyl
	 */
	public static Map<String, String> prmsTradeStyleMaptoChi = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("844", "人民币钯金");
			put("845", "人民币铂金");
			put("841", "美元钯金");
			put("045", "美元铂金");
			put("035", "人民币金");
			put("068", "人民币银");
			put("034", "美元金");
			put("036", "美元银");
			put("001", "人民币元");
			put("014", "美元");
		}
	};
	/**
	 * 贵金属交易种类 把代码变成中文显示 add by xyl
	 */
	public static Map<String, String> prmsTradeStyle1MaptoChi = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("844", "人民币钯金");
			put("845", "人民币铂金");
			put("841", "美元钯金");
			put("045", "美元铂金");
			put("035", "人民币金");
			put("068", "人民币银");
			put("034", "美元金");
			put("036", "美元银");
		}
	};
	/**
	 * 贵金属 根据原币种
	 */
	public static Map<String, String> prmsStyle1MaptoChi = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("844", ConstantGloble.PRMS_TRADETYPE_BAG);
			put("845", ConstantGloble.PRMS_TRADETYPE_BOG);
			put("841", ConstantGloble.PRMS_TRADETYPE_BAG);
			put("045", ConstantGloble.PRMS_TRADETYPE_BOG);
			put("035", ConstantGloble.PRMS_TRADETYPE_GODE);
			put("068", ConstantGloble.PRMS_TRADETYPE_SILVER);
			put("034", ConstantGloble.PRMS_TRADETYPE_GODE);
			put("036", ConstantGloble.PRMS_TRADETYPE_SILVER);
		}
	};
	// /**
	// * 获取贵金属的码
	// */
	// public static Map<String, String> prmsTradeStyleGSMap = new
	// HashMap<String, String>() {
	// /**
	// *
	// */
	// private static final long serialVersionUID = 1L;
	//
	// {
	// put("844", "人民币钯金");
	// put("845", "人民币铂金");
	// put("841", "美元钯金");
	// put("045", "美元铂金");
	// put("035", "人民币金");
	// put("068", "人民币银");
	// put("034", "美元金");
	// put("036", "美元银");
	// }
	// };
	/**
	 * 获取贵金属的显示
	 */
	public static Map<String, String> prmsTradeStyleGSMapShow = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("844", "钯金");
			put("845", "铂金");
			put("841", "钯金");
			put("045", "铂金");
			put("035", "黄金");
			put("068", "白银");
			put("034", "黄金");
			put("036", "白银");
		}
	};
	/**
	 * 获取贵金属的显示
	 */
	public static Map<String, String> prmsTradeStyleToS = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("钯金", ConstantGloble.PRMS_TRADETYPE_BAG);
			put("铂金", ConstantGloble.PRMS_TRADETYPE_BOG);
			put("黄金", ConstantGloble.PRMS_TRADETYPE_GODE);
			put("白银", ConstantGloble.PRMS_TRADETYPE_SILVER);
		}
	};
	/**
	 * 贵金属价格单位 从代码到 中文 add by xyl 一般页面显示
	 */
	public static Map<String, String> prmsTradePricUnitMaptoChi = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("844", "/克");
			put("845", "/克");
			put("841", "/盎司");
			put("045", "/盎司");
			put("035", "/克");
			put("068", "/克");
			put("034", "/盎司");
			put("036", "/盎司");
			put("014", "/盎司");
			put("001", "/克");
		}
	};

	/**
	 * 贵金属仓储单位 从代码到 中文 add by xyl
	 */
	public static Map<String, String> prmsUnitMaptoChi = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("844", "克");
			put("845", "克");
			put("841", "盎司");
			put("045", "盎司");
			put("035", "克");
			put("068", "克");
			put("034", "盎司");
			put("036", "盎司");
			put("001", "人民币元");
			put("014", "美元");
		}
	};
	public static Map<String, String> prmsUnitMaptoChi1 = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("844", "克");
			put("845", "克");
			put("841", "盎司");
			put("045", "盎司");
			put("035", "克");
			put("068", "克");
			put("034", "盎司");
			put("036", "盎司");
			put("001", "克");
			put("014", "盎司");
		}
	};
	/*
	 * 账户余额从代码到 中文 add by xyl
	 */
	public static Map<String, String> prmsAccUnitMaptoChi = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("844", "人民币元");
			put("845", "人民币元");
			put("841", "美元");
			put("045", "美元");
			put("035", "人民币元");
			put("068", "人民币元");
			put("034", "美元");
			put("036", "美元");
			put("001", "人民币元");
			put("014", "美元");
		}
	};
	public static Map<String, String> prmsBuyCurrencyMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("001", "人民币");
			put("014", "美元");
		}
	};
	/**
	 * 贵金属种类 代码 黄金/白银 add by xyl
	 */
	public static Map<String, String> prmsStyleMaptoChi = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("G", "黄金");
			put("S", "白银");
			put("D", "钯金");
			put("T", "铂金");
		}
	};
	public static Map<String, String> prmsBuyStyleMaptoChi = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("G", "金");
			put("S", "银");
			put("D", "钯金");
			put("T", "铂金");
		}
	};

	/** 币种 ArrayList */
	public static List<String> transCurrencyList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("人民币");
			add("美元");
		}
	};
	/** 币种 Map */
	public static Map<String, String> transCurrencyMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("人民币", "001");
			put("美元", "014");
		}
	};

	public static Map<String, String> entrustSatetMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("U", "委托失败");
			put("N", "待成交");
			put("R", "正在处理");
			put("S", "已成交");
			put("F", "交易金额不足");
			put("X", "交易失败");
			put("C", "已撤单");
			put("E", "到期未成交");
			put("O", "其它原因(待确定)");
		}
	};
	public static List<String> fincCurrencyCodeStrList = new ArrayList<String>() {
		{
			add("人民币");
			add("英镑");
			add("港币");
			add("美元");
			add("日元");
			add("欧元");
		}
	};
	public static List<String> fincCurrencyCodeList = new ArrayList<String>() {
		{
			add("001");
			add("012");
			add("013");
			add("014");
			add("027");
			add("038");
		}
	};

	/**
	 * 基金产品类型
	 * 
	 * */
	public static List<String> fundProductType = new ArrayList<String>() {

		{
			add("股票基金");
			add("债券基金");
			add("货币基金");
			add("QDII基金");
			add("其他基金");
		}

	};

	/**
	 * dxd 15.5.4 基金产品类型
	 * 
	 * */
	public static List<String> fundProductTypeNew = new ArrayList<String>() {

		{
			add("股票型基金");
			add("债券型基金");
			add("货币型基金");
			add("混合型基金");
			add("QDII基金");
			add("保本型基金");
			add("指数型基金");
			add("理财型基金");
			add("ETF基金");
			add("其他基金");
			// add("QDII");
			// add("其他型基金");
			// add("集合理财");
			// add("中银理财");
			// add("信托");
			// add("保险");
			// add("短期理财");
			// add("中银理财");
			// add("信托");
			// add("保险");
			// add("其他");
		}

	};

	/**
	 * 基金类型 xyl
	 */
	public static List<String> fincFundType = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("开放式基金产品");
			add("资管计划产品");
			add("信托产品");
			// add("理财型基金");
			// add("QDII");
			// add("ETF");
			// add("保本型基金");
			// add("指数型基金");
			// add("货币型基金");
			// add("股票型基金");
			// add("债券型基金");
			// add("混合型基金");
			// add("其他型基金");
			// add("集合理财");
			// add("中银理财");
			// add("信托");
			// add("保险");
			// add("短期理财");
			// add("中银理财");
			// add("信托");
			// add("保险");
			// add("其他");
		}
	};
	/**
	 * 基金类型 xyl
	 */
	public static List<String> fincFundTypeCode = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{

			add("00");// 全部
			add("01");// 开放式基金产品
			add("07");// 资管计划产品
			add("04");// 信托产品
			// add("01");// 理财型基金
			// add("02");//QDII
			// add("03");//ETF
			// add("04");//保本型基金
			// add("05");//指数型基金
			// add("06");//货币型基金
			// add("07");//股票型基金
			// add("08");//债券型基金
			// add("09");//混合型基金
			// add("10");//其他型基金
		}
	};

	/**
	 * 产品类型
	 * */

	public static List<String> fundProductTypeList = new ArrayList<String>() {

		private static final long serialVersionUID = 1L;

		{

			add("00");// 全部
			add("07");// QDII基金
			add("08");// 货币基金
			add("06");// 股票基金
			add("02");// 债券基金
			add("20");// 其他基金
		}

	};
	/**
	 * dxd 15\5\5 产品类型
	 * */

	public static List<String> fundProductTypeListNew = new ArrayList<String>() {

		private static final long serialVersionUID = 1L;

		{
			add("00");// 全部
			add("07");// 股票基金
			add("08");// 债券基金
			add("06");// 货币基金
			add("09");// 混合型基金
			add("02");// QDII基金
			add("04");// 保本型基金
			add("05");// 指数型基金
			add("01");// 理财型基金
			add("03");// ETF基金
			add("20");// 其他基金
		}

	};

	public static Map<String, String> fincFundTypeCodeToStr = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("00", "全部");
			put("01", "开放式基金产品");
			put("04", "信托产品");
			put("07", "资管计划产品");
			put("02", "一对多");
			put("03", "劵商");
			put("05", "银行理财");
			put("06", "保险");

			// put("01", "理财型");
			// put("02", "QDII");
			// put("03", "ETF");
			// put("04", "保本型");
			// put("05", "指数型");
			// put("06", "货币型");
			// put("07", "股票型");
			// put("08", "债券型");
			// put("09", "混合型");
			// put("10", "其他型");

		}
	};
	/**
	 * 基金状态 xyl
	 */
	public static List<String> fincFundState = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("正常开放");// 0
			add("可认购");// 1
			add("发行成功");// 2
			add("发行失败");// 3
			add("暂停交易");// 4
			add("暂停申购");// 5
			add("暂停赎回");// 6
			add("权益登记");// 7
			add("红利发放");// 8
			add("基金封闭");// 9
			add("基金终止");// a
			add("停止开户");// b
		}
	};
	/**
	 * 基金状态 xyl
	 */
	public static List<String> fincFundStateCode = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("0");
			add("1");// 1
			add("2");// 2
			add("3");// 3
			add("4");// 4
			add("5");// 5
			add("6");// 6
			add("7");// 7
			add("8");// 8
			add("9");// 9
			add("a");// a
			add("b");// b
		}
	};
	/** 基金状态代码 到显示内容 */
	public static Map<String, String> fincFundStateCodeToStr = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "正常开放");// shen
			put("1", "可认购");// 1
			put("2", "发行成功");// 2
			put("3", "发行失败");// 3
			put("4", "暂停交易");// 4
			put("5", "暂停申购");// 5
			put("6", "暂停赎回");// 6
			put("7", "权益登记");// 7
			put("8", "红利发放");// 8
			put("9", "基金封闭");// 9
			put("a", "基金终止");// a
			put("b", "停止开户");// b
		}
	};

	/** 基金状态代码 到显示内容 */
	public static Map<String, String> fincStatusTypeCodeToStr = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "未处理");
			put("S", "成功");// 1
			put("F", "失败");// 2
		}
	};
	/** 历史交易查询 基金状态代码 到显示内容 */
	public static Map<String, String> fincHistoryStatusTypeCodeToStr = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "成功");
			put("1", "部分成功");
			put("2", "失败");
			put("3", "已撤单");
		}
	};
	/**
	 * 基金交易方式 交易类型
	 */
	public static List<String> fincTransTypeCodeList = new ArrayList<String>() {
		{
			add("0");
			add("1");
			add("2");
			add("3");
			add("4");
			add("5");
			add("6");
			add("7");
		}
	};
	/**
	 * 基金交易方式 交易类型
	 */
	public static List<String> fincTransTypeNameList = new ArrayList<String>() {
		{
			add("全部");
			add("购买");
			add("赎回");
			add("基金定投");
			add("设置分红方式");
			add("基金转换");
			add("基金分红");
			add("基金账户管理");
		}
	};

	/**
	 * 基金交易方式 交易类型
	 */
	public static Map<String, String> fincTradeTypeCodeToStr = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("020", "认购申请");
			put("022", "申购申请");
			put("024", "赎回申请");
			put("026", "转托管转出（一步）申请");
			put("027", "转托管转入（两步）申请");
			put("028", "转托管转出（两步）申请");
			put("029", "修改分红方式申请");
			put("036", "基金转换申请（同机构）");
			put("038", "基金转换申请（不同机构）");
			put("039", "定期定额申购申请");
			put("059", "定期定额申请");
			put("060", "定期定额撤销");
			put("061", "定期定额修改");
			put("062", "定期定额赎回申请");
			put("063", "定期定额赎回撤销");
			put("064", "定期定额赎回修改");
			put("120", "认购首次确认");
			put("122", "申购确认");
			put("124", "赎回确认");
			put("126", "转托管转出（一步）确认");
			put("127", "转托管转入（两步）确认");
			put("128", "转托管转出（两步）确认");
			put("129", "修改分红方式确认");
			put("130", "认购二次确认");
			put("131", "基金份数冻结");
			put("132", "基金份数解冻");
			put("133", "非交易过户");
			put("134", "非交易过户转入");
			put("135", "非交易过户转出");
			put("138", "基金转换确认");
			put("139", "定期定额申购确认");
			put("142", "强行赎回");
			put("144", "强增");
			put("145", "强减");
			put("149", "募集失败");
			put("150", "清盘");
			put("611", "现金分红");
			put("612", "再投资分红");
			put("501", "中行开户申请");
			put("511", "中行开户确认");
			put("001", "TA开户申请");
			put("502", "中行销户申请");
			put("512", "中行销户确认");
			put("002", "TA销户申请");
			put("503", "中行修改客户资料申请");
			put("513", "中行修改客户资料确认");
			put("003", "TA修改客户资料申请");
			put("008", "登记TA账号申请");
			put("009", "取消TA账号申请");
			put("101", "TA开户确认");
			put("102", "TA销户确认");
			put("103", "TA修改客户资料确认");
			put("104", "基金账户冻结");
			put("105", "基金账户解冻");
			put("108", "登记TA账号确认");
			put("109", "取消TA账号确认");
			put("507", "对公资金账号维护	");
			put("607", "撤单");
			put("608", "调账");
			put("640", "风险测评申请");
			put("650", "电子签名约定书申请");
			put("660", "电子签名合同申请");
		}
	};
	/**
	 * 基金Ta账户 账户状态
	 */
	public static Map<String, String> fincTaAccTypeMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("00", "正常");
			put("01", "销户处理中");
			put("02", "取消关联处理中");
			put("03", "冻结");
		}
	};

	/** 赎回处理方式 finc xyl */
	public static List<String> fundSellFlagStr = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("顺延赎回");// 1
			add("取消赎回");// 0
		}
	};
	/** 赎回处理方式 finc xyl */
	public static List<String> fundSellFlag = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("1");// 1
			add("0");// 0
		}
	};
	/** 赎回处理方式 finc xyl */
	public static Map<String, String> fundSellFlagCodeToStr = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("1", "顺延赎回");// 1
			put("0", "取消赎回");
		}
	};
	/**
	 * 基金收费方式 代码
	 */
	public static List<String> fundFeeTypeCodeList = new ArrayList<String>() {
		{
			add("1");
			add("2");
		}
	};
	/**
	 * 基金收费方式 名称
	 */
	public static List<String> fundFeeTypeStrList = new ArrayList<String>() {
		{
			add("前收");
			add("后收");
		}
	};
	/** 基金收费方式 */
	public static Map<String, String> fundfeeTypeCodeToStr = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("1", "前收");// 1
			put("2", "后收");
			put("3", "可前可后");// TODO 已经不用了
		}
	};
	/** 交易状态 定期定额明细查询 */
	public static Map<String, String> fundTradeStateStr = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "成功");// 1
			put("1", "失败");
		}
	};
	/** 基金是否可以的显示 */
	public static Map<Boolean, String> fundBooleanMap = new HashMap<Boolean, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put(true, "是");// 1
			put(false, "否");
		}
	};
	/**
	 * 基金风险等级
	 */
	public static List<String> fincRiskLevel = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("保守型");
			add("稳健型");
			add("平衡型");
			add("成长型");
			add("进取型");
		}
	};

	/**
	 * 基金风险代码
	 */
	public static List<String> fincRiskLevelCode = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("1");
			add("2");
			add("3");
			add("4");
			add("5");
		}
	};

	/**
	 * 基金定投管理,交易类型
	 */
	public static Map<String, String> fincDQDETransType = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "定期定额申购");
			put("1", "定期定额赎回");
		}
	};
	/**
	 * 基金风险代码到代码
	 */
	public static Map<String, String> fincRiskLevelCodeToStr = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("1", "保守型");
			put("2", "稳健型");
			put("3", "平衡型");
			put("4", "成长型");
			put("5", "进取型");
		}
	};
	public static Map<String, String> fincRiskLevelCodeToStrFUND = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("1", "保守型");
			put("2", "稳健型");
			put("3", "平衡型");
			put("4", "成长型");
			put("5", "进取型");
		}
	};

	/**
	 * 基金产品类型
	 * */
	public static Map<String, String> fundProductTypeMap = new HashMap<String, String>() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("00", "全部");
			put("02", "QDII基金");
			put("06", "货币型基金");
			put("07", "股票型基金");
			put("08", "债券型基金");
			put("20", "其他基金");
			put("01", "理财型基金");
			put("03", "ETF基金");
			put("04", "保本型基金");
			put("05", "指数型基金");
			put("09", "混合型基金");
			put("10", "其他基金");
		}

	};

	/**
	 * 基金交易状态
	 * 
	 * @author xiaoyl
	 */
	public static Map<String, String> fincTradeStatusCodeToStr = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("Y", "已撤单");
			put("N", "未撤单");
		}
	};
	// 转账汇款 add by wangchao
	public static Map<String, String> bocFlag = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "跨行转账");
			put("1", "中行内转账");
		}
	};

	public static Map<String, String> FincAddressTypeValue = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("家庭地址", "01");
			put("单位地址", "02");
			put("临时地址", "03");
			put("其它地址", "04");

		}
	};
	/**
	 * 开户行
	 */
	public static List<String> kBankList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			// add("中国银行");
			add("中国工商银行");
			add("中国建设银行");
			add("中国农业银行");
			add("招商银行");
			add("国家开发银行");
			add("中国进出口银行");
			add("中国农业发展银行");
			add("交通银行");
			add("中信银行");
			add("中国光大银行");
			add("华夏银行");
			add("中国民生银行");
			add("广发银行股份有限公司");
			add("平安银行");
			add("兴业银行");
			add("上海浦东发展银行");
			add("中国邮政储蓄银行");
			// add("中国人民银行");
			add("其它银行");
		}
	};
	/**
	 * 开户行Map
	 */
	public static Map<String, String> kBankListMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("中国银行", "104");
			put("中国工商银行", "102");
			put("中国建设银行", "105");
			put("中国农业银行", "103");
			put("招商银行", "308");
			put("国家开发银行", "201");
			put("中国进出口银行", "202");
			put("中国农业发展银行", "203");
			put("交通银行", "301");
			put("中信银行", "302");
			put("中国光大银行", "303");
			put("华夏银行", "304");
			put("中国民生银行", "305");
			put("广发银行股份有限公司", "306");
			put("平安银行", "307");
			put("兴业银行", "309");
			put("上海浦东发展银行", "310");
			put("中国邮政储蓄银行", "403");
			put("中国人民银行", "PBC");
			put("其它银行", "OTHER");
		}
	};
	/** 手机号转账状态 */
	public static Map<String, String> mobileTranDownStatus = new HashMap<String, String>() {
		{
			put("01", "提交成功");
			put("02", "已回复");
			put("03", "交易失败");
			put("05", "已过期");
			put("A", "交易成功");
			put("B", "交易失败");
		}
	};
	/** 手机号转账状态 */
	public static Map<String, String> mobileTranStatusUpmap = new HashMap<String, String>() {
		{
			put("全部", "");
			put("提交成功", "01");
			put("交易失败", "03B");
			put("已过期", "05");
			put("交易成功", "A");
		}
	};
	public static Map<String, String> mobileTranStatusUpmap2 = new HashMap<String, String>() {
		{
			put("", "全部");
			put("01", "提交成功");
			put("03B", "交易失败");
			put("05", "已过期");
			put("A", "交易成功");
		}
	};
	/** 手机号转账状态 */
	public static List<String> mobileTransStatusUpList = new ArrayList<String>() {
		{
			add("全部");
			add("提交成功");
			add("交易失败");
			add("已过期");
			add("交易成功");

		}
	};
	// 数据字典 add by wjp
	// 预约执行方式周期
	public static Map<String, String> Frequency = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("W", "单周");
			put("D", "双周");
			put("M", "月");
		}
	};
	public static Map<String, String> FrequencyValue = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("单周", "W");
			put("双周", "D");
			put("月", "M");
		}
	};

	// 支取通知存款-约定方式
	public static Map<String, String> ConventionConvertType = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("N", "非约定转存");
			put("R", "约定转存");
		}
	};
	// #转存方式
	public static Map<String, String> ConvertType = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("N", "非自动转存");
			put("R", "自动转存");
			put("", "-");
		}
	};
	// 通知存款存期
	public static Map<String, String> CallCDTerm = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("1", "一天通知存款");
			put("7", "七天通知存款");
		}
	};

	// 通知存款存期简称
	public static Map<String, String> CallCDTermShort = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("1", "一天");
			put("7", "七天");
		}
	};
	// #账号详情:存期
	// 定期存期
	public static Map<String, String> CDPeriod = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "无");
			put("1", "一个月");
			put("3", "三个月");
			put("6", "半年");
			put("12", "一年");
			put("24", "两年");
			put("36", "三年");
			put("60", "五年");
			put("72", "六年");
		}
	};


	public static Map<String, String> CDPeriodValue = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("无", "0");
			put("一个月", "1");
			put("三个月", "3");
			put("半年", "6");
			put("一年", "12");
			put("两年", "24");
			put("三年", "36");
			put("五年", "60");
			put("六年", "72");
		}
	};
	// 定期存期 半年修改为六个月
		public static Map<String, String> SaveRegularCDPeriodValue = new HashMap<String, String>() {
			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			{
				put("无", "0");
				put("一个月", "1");
				put("三个月", "3");
				put("六个月", "6");
				put("一年", "12");
				put("两年", "24");
				put("三年", "36");
				put("五年", "60");
				put("六年", "72");
			}
		};
	// 通知存款存期
	public static Map<String, String> InfoDeposit = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "无");
			put("1", "一天");
			put("7", "七天");
		}
	};
	// 支取
	public static Map<String, String> drawMode = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("N", "部分支取");
			put("Y", "全部支取");
		}
	};
	// 执行
	public static Map<String, String> TransModeDisplay = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "立即执行");
			put("1", "预约日期执行");
			put("2", "预约周期执行");
		}
	};
	// 账户状态
	public static Map<String, String> SubAccountsStatus = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("V", "有效");
			put("D", "逻辑删除(注销)");
			put("E", "待复核");
			put("00", "有效");
			put("01", "已销户");
			put("02", "止付");
			put("03", "长期不动户");
			put("04", "开户即冲正");
			put("05", "挂失");
			put("06", "冻结");
			put("07", "已转期");
			put("08", "已结清");
			put("09", "其它");

		}
	};
	/**
	 * 证件类型
	 */
	public static Map<String, String> identityType = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("请选择", "0");
			put("身份证", "1");
			put("临时居民身份证", "2");
			put("户口簿", "3");
			put("军人身份证", "4");
			put("武装警察身份证", "5");
			put("港澳居民通行证", "6");
			put("台湾居民通行证", "7");
			put("护照", "8");
			put("其他证件", "9");
			put("港澳台居民往来内地通行证", "10");
			put("外交人员身份证", "11");
			put("外国人居留许可证", "12");
			put("边民出入境通行证", "13");
			put("港澳居民来往内地通行证", "47");
			put("港澳居民来往内地通行证", "48");
			put("台湾居民来往大陆通行证", "49");
		}
	};

	/**
	 * 证件类型
	 */
	public static Map<String, String> IDENTITYTYPE = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
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
			put("47", "港澳居民来往内地通行证（香港）");
			put("48", "港澳居民来往内地通行证（澳门）");
			put("49", "台湾居民来往大陆通行证");
		}
	};
	/**
	 * 货币
	 */
	public static Map<String, String> handMade = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("9", "未处理");
			put("0", "手工处理成功");
			put("1", "原交易已删除");


		}
	};
	
	
	/**
	 * 贵金属的币种
	 */
	public static List<String> goldLists = new  ArrayList<String>(){
//		put("844", "人民币钯金");
//		put("845", "人民币铂金");
//		put("841", "美元钯金");
//		put("045", "美元铂金");
//		put("035", "人民币金");
//		put("068", "人民币银");
//		put("034", "美元金");
//		put("036", "美元银");
		{add("人民币钯金");
		add("人民币铂金");
		add("美元钯金");
		add("美元铂金");
		add("人民币金");
		add("人民币银");
		add("美元金");
		add("美元银");}
	};
	
	/**
	 * 货币
	 */
	public static Map<String, String> Currency = new LinkedHashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{//欧元、港币、日元、澳元
			put("", "不可选择");
			put("000", "不可选择");
			put("001", "人民币元");
			put("CNY", "人民币元");
			put("012", "英镑");
			put("GBP", "英镑");
//			put("AUD","澳大利亚元");
			// put("013", "港币");
			// put("HKD", "港币");
			// change sunh
			put("013", "港币");
			put("HKD", "港币");
			put("014", "美元");
			put("USD", "美元");
			put("015", "瑞士法郎");
			put("CHF", "瑞士法郎");
			put("016", "德国马克");
			put("DEM", "德国马克");
			put("017", "法国法郎");
			put("FRF", "法国法郎");
			put("018", "新加坡元");
			put("SGD", "新加坡元");
			put("020", "荷兰盾");
			put("NLG", "荷兰盾");
			put("021", "瑞典克朗");
			put("SEK", "瑞典克朗");
			put("022", "丹麦克朗");
			put("DKK", "丹麦克朗");
			put("023", "挪威克朗");
			put("NOK", "挪威克朗");
			put("024", "奥地利先令");
			put("ATS", "奥地利先令");
			put("025", "比利时法郎");
			put("BEF", "比利时法郎");
			put("026", "意大利里拉");
			put("ITL", "意大利里拉");
			put("027", "日元");
			put("JPY", "日元");
			put("028", "加拿大元");
			put("CAD", "加拿大元");
			put("029", "澳大利亚元");
			put("AUD", "澳大利亚元");
			put("068", "人民币银");
			put("036", "美元银");
			put("034", "美元金");
			put("XAU", "美元金");
			put("035", "人民币金");
			put("GLD", "人民币金");
			put("844", "人民币钯金");
			put("845", "人民币铂金");
			put("841", "美元钯金");
			put("045", "美元铂金");
			put("038", "欧元");
			put("EUR", "欧元");
			put("042", "芬兰马克");
			put("FIM", "芬兰马克");
			put("081", "澳门元");
			put("MOP", "澳门元");
			put("082", "菲律宾比索");
			put("PHP", "菲律宾比索");
			put("084", "泰国铢");
			put("THB", "泰国铢");
			put("087", "新西兰元");
			put("NZD", "新西兰元");
			put("088", "韩元");
			put("KRW", "韩元");
			put("094", "记账美元");
			put("095", "清算瑞士法郎");
			put("056", "印尼盾");
			put("IDR", "印尼盾");
			put("064", "越南盾");
			put("VND", "越南盾");
			// put("777", "阿联酋迪拉姆");
			put("096", "阿联酋迪拉姆");
			put("AED", "阿联酋迪拉姆");
			put("126", "阿根廷比索");
			put("ARP", "阿根廷比索");
			put("134", "巴西雷亚尔");
			put("BRL", "巴西雷亚尔");
			put("053", "埃及磅");
			put("EGP", "埃及磅");
			put("085", "印度卢比");
			put("INR", "印度卢比");
			put("057", "约旦第纳尔");
			put("JOD", "约旦第纳尔");
			put("179", "蒙古图格里克");
			put("MNT", "蒙古图格里克");
			put("076", "尼日尼亚奈拉");
			put("NGN", "尼日尼亚奈拉");
			put("062", "罗马尼亚列伊");
			put("ROL", "罗马尼亚列伊");
			put("093", "土耳其里拉");
			put("TRL", "土耳其里拉");
			put("246", "乌克兰格里夫纳");
			put("UAH", "乌克兰格里夫纳");
			put("070", "南非兰特");
			put("ZAR", "南非兰特");
			put("101", "哈萨克斯坦坚戈");
			put("KZT", "哈萨克斯坦坚戈");
			put("080", "赞比亚克瓦查");
			put("ZMK", "赞比亚克瓦查");
			put("065", "匈牙利福林");
			put("HUF", "匈牙利福林");
			// put("032", "林吉特");
			// put("MYR", "林吉特");
			// change sunh
			put("032", "林吉特");
			put("MYR", "林吉特");
			put("-1", "通配");
			// put("096", "美元指数");
			put("251", "阿富汗尼");
			put("AFN", "阿富汗尼");
			put("061", "阿尔巴尼亚列克");
			put("ALL", "阿尔巴尼亚列克");
			put("041", "阿尔及利亚第纳尔");
			put("DZD", "阿尔及利亚第纳尔");
			put("128", "阿塞拜疆马纳特");
			put("AZM", "阿塞拜疆马纳特");
			put("135", "巴哈马元");
			put("BSD", "巴哈马元");
			put("009", "巴林第纳尔");
			put("BHD", "巴林第纳尔");
			put("010", "孟加拉国塔卡");
			put("BDT", "孟加拉国塔卡");
			put("123", "亚美尼亚德拉姆");
			put("AMD", "亚美尼亚德拉姆");
			put("129", "巴巴多斯元");
			put("BBD", "巴巴多斯元");
			put("130", "百慕大元");
			put("BMD", "百慕大元");
			put("132", "玻利维亚玻利瓦诺");
			put("BOB", "玻利维亚玻利瓦诺");
			put("138", "伯利兹元");
			put("BZD", "伯利兹元");
			put("199", "所罗门群岛元");
			put("SBD", "所罗门群岛元");
			put("066", "保加利亚列弗");
			put("BGL", "保加利亚列弗");
			put("178", "缅元");
			put("MMK", "缅元");
			put("051", "布隆迪法郎");
			put("BIF", "布隆迪法郎");
			put("144", "佛得角埃斯库多");
			put("CVE", "佛得角埃斯库多");
			put("168", "开曼群岛元");
			put("KYD", "开曼群岛元");
			put("040", "斯里兰卡卢比");
			put("LKR", "斯里兰卡卢比");
			put("140", "智利比索");
			put("CLP", "智利比索");
			put("141", "哥伦比亚比索");
			put("COP", "哥伦比亚比索");
			put("167", "科摩罗法郎");
			put("KMF", "科摩罗法郎");
			put("142", "哥斯达黎加科郎");
			put("CRC", "哥斯达黎加科郎");
			put("159", "克罗地亚库纳");
			put("HRK", "克罗地亚库纳");
			put("052", "塞浦路斯镑");
			put("CYP", "塞浦路斯镑");
			put("145", "捷克克朗");
			put("CZK", "捷克克朗");
			put("147", "多米尼加比索");
			put("DOP", "多米尼加比索");
			put("206", "萨尔瓦多科郎");
			put("SVC", "萨尔瓦多科郎");
			put("054", "埃塞俄比亚比尔");
			put("ETB", "埃塞俄比亚比尔");
			put("235", "厄立特里亚纳克法");
			put("ERN", "厄立特里亚纳克法");
			put("150", "爱沙尼亚克罗姆");
			put("EEK", "爱沙尼亚克罗姆");
			put("152", "福克兰群岛镑");
			put("FKP", "福克兰群岛镑");
			put("151", "斐济元");
			put("FJD", "斐济元");
			put("146", "吉布提法郎");
			put("DJF", "吉布提法郎");
			put("154", "冈比亚达拉西");
			put("GMD", "冈比亚达拉西");
			put("153", "直布罗陀镑");
			put("GIP", "直布罗陀镑");
			put("155", "危地马拉格查尔");
			put("GTQ", "危地马拉格查尔");
			put("050", "几内亚法郎");
			put("GNF", "几内亚法郎");
			put("157", "圭亚那元");
			put("GYD", "圭亚那元");
			put("160", "海地古德");
			put("HTG", "海地古德");
			put("158", "洪都拉斯伦皮拉");
			put("HNL", "洪都拉斯伦皮拉");
			put("163", "冰岛克朗");
			put("ISK", "冰岛克朗");
			put("048", "伊朗里亚尔");
			put("IRR", "伊朗里亚尔");
			put("044", "伊拉克第纳尔");
			put("IQD", "伊拉克第纳尔");
			put("162", "新以色列谢克尔");
			put("ILS", "新以色列谢克尔");
			put("164", "牙买加元");
			put("JMD", "牙买加元");
			put("058", "肯尼亚先令");
			put("KES", "肯尼亚先令");
			put("063", "北朝鲜圆");
			put("KPW", "北朝鲜圆");
			put("059", "科威特第纳尔");
			put("KWD", "科威特第纳尔");
			put("165", "吉尔吉斯斯坦索姆");
			put("KGS", "吉尔吉斯斯坦索姆");
			put("169", "老挝基普");
			put("LAK", "老挝基普");
			put("170", "黎巴嫩镑");
			put("LBP", "黎巴嫩镑");
			put("175", "拉脱维亚拉特");
			put("LVL", "拉脱维亚拉特");
			put("171", "利比里亚元");
			put("LRD", "利比里亚元");
			put("071", "利比亚第纳尔");
			put("LYD", "利比亚第纳尔");
			put("173", "立陶宛立特");
			put("LTL", "立陶宛立特");
			put("183", "马维拉克瓦查");
			put("MWK", "马维拉克瓦查");
			put("182", "马尔代夫卢菲亚");
			put("MVR", "马尔代夫卢菲亚");
			put("181", "马尔他里拉");
			put("MTL", "马尔他里拉");
			put("180", "毛里塔尼亚乌吉亚");
			put("MRO", "毛里塔尼亚乌吉亚");
			put("075", "毛里求斯卢比");
			put("MUR", "毛里求斯卢比");
			put("184", "墨西哥比索");
			put("MXN", "墨西哥比索");
			put("176", "摩尔多瓦列伊");
			put("MDL", "摩尔多瓦列伊");
			put("046", "摩洛哥迪拉姆");
			put("MAD", "摩洛哥迪拉姆");
			put("185", "莫桑比克麦梯卡尔");
			put("MZM", "莫桑比克麦梯卡尔");
			put("188", "阿曼里亚尔");
			put("OMR", "阿曼里亚尔");
			put("186", "纳米比亚元");
			put("NAD", "纳米比亚元");
			put("049", "尼泊尔卢比");
			put("NPR", "尼泊尔卢比");
			put("124", "荷属安的列斯盾");
			put("ANG", "荷属安的列斯盾");
			put("127", "阿鲁巴盾");
			put("AWG", "阿鲁巴盾");
			put("219", "瓦努阿图瓦图");
			put("VUV", "瓦努阿图瓦图");
			put("187", "尼加拉瓜金科多巴");
			put("NIO", "尼加拉瓜金科多巴");
			put("019", "巴基斯坦卢比");
			put("PKR", "巴基斯坦卢比");
			put("189", "巴拿马巴波亚");
			put("PAB", "巴拿马巴波亚");
			put("077", "巴布亚新几内亚基那");
			put("PGK", "巴布亚新几内亚基那");
			put("194", "巴拉圭瓜拉尼");
			put("PYG", "巴拉圭瓜拉尼");
			put("190", "秘鲁索尔");
			put("PEN", "秘鲁索尔");
			put("156", "几内亚比绍比索");
			put("GWP", "几内亚比绍比索");
			put("211", "东帝汶埃斯库多");
			put("TPE", "东帝汶埃斯库多");
			put("195", "卡塔尔里亚尔");
			put("QAR", "卡塔尔里亚尔");
			put("078", "卢旺达法郎");
			put("RWF", "卢旺达法郎");
			put("200", "圣赫勒拿镑");
			put("SHP", "圣赫勒拿镑");
			put("205", "圣多美和普林西比多布拉");
			put("STD", "圣多美和普林西比多布拉");
			put("198", "沙特里亚尔");
			put("SAR", "沙特里亚尔");
			put("079", "塞舌尔卢比");
			put("SCR", "塞舌尔卢比");
			put("047", "塞拉利昂利昂");
			put("SLL", "塞拉利昂利昂");
			put("202", "斯洛伐克克朗");
			put("SKK", "斯洛伐克克朗");
			put("201", "斯洛文尼亚托拉尔");
			put("SIT ", "斯洛文尼亚托拉尔");
			put("203", "索马里先令");
			put("SOS", "索马里先令");
			put("224", "津巴布韦元");
			put("ZWD", "津巴布韦元");
			put("242", "苏丹第纳尔");
			put("SDD", "苏丹第纳尔");
			put("207", "斯威士兰里兰吉尼");
			put("SZL", "斯威士兰里兰吉尼");
			put("089", "叙利亚镑");
			put("SYP", "叙利亚镑");
			put("210", "汤加邦加");
			put("TOP", "汤加邦加");
			put("212", "特立尼达和多巴哥元");
			put("TTD", "特立尼达和多巴哥元");
			put("091", "突尼斯第纳尔");
			put("TND", "突尼斯第纳尔");
			put("209", "土库曼斯坦马纳特");
			put("TMM", "土库曼斯坦马纳特");
			put("215", "乌干达先令");
			put("UGX", "乌干达先令");
			put("177", "马其顿第纳尔");
			put("MKD", "马其顿第纳尔");
			put("030", "坦桑尼亚先令");
			put("TZS", "坦桑尼亚先令");
			put("216", "乌拉圭比索");
			put("UYU", "乌拉圭比索");
			put("217", "乌兹别克斯坦苏姆");
			put("UZS", "乌兹别克斯坦苏姆");
			put("247", "委内瑞拉玻利瓦尔");
			put("VEF", "委内瑞拉玻利瓦尔");
			put("220", "萨摩亚塔拉");
			put("WST", "萨摩亚塔拉");
			put("098", "也门里亚尔");
			put("YER", "也门里亚尔");
			put("221", "南斯拉夫第纳尔");
			put("YUM", "南斯拉夫第纳尔");
			put("252", "加纳塞地");
			put("GHC", "加纳塞地");
			put("245", "新土耳其里拉");
			put("TRY", "新土耳其里拉");
			put("820", "CAF 法郎 BEAC");
			put("XAF", "CAF 法郎 BEAC");
			put("825", "东加勒比元");
			put("XCD", "东加勒比元");
			put("099", "CFA 法郎 BCEAO");
			put("XOF", "CFA 法郎 BCEAO");
			put("842", "CFP 法郎");
			put("XPF", "CFP 法郎");
			put("248", "马达加斯加阿里亚里");
			put("MGA", "马达加斯加阿里亚里");
			put("244", "塔吉克索莫尼");
			put("TJS", "塔吉克索莫尼");
			put("250", "白俄罗斯卢布");
			put("BYR", "白俄罗斯卢布");
			put("229", "保加利亚列弗");
			put("BGN", "保加利亚列弗");
			put("236", "格鲁吉亚拉里");
			put("GEL", "格鲁吉亚拉里");
			put("191", "波兰兹罗提");
			put("PLN", "波兰兹罗提");
			put("226", "安哥拉宽扎");
			put("AOA", "安哥拉宽扎");
			put("039", "博茨瓦纳普拉");
			put("BWP", "博茨瓦纳普拉");
			put("131", "文莱币");
			put("BND", "文莱币");
			put("166", "柬埔寨瑞尔");
			put("KHR", "柬埔寨瑞尔");
			// put("213", "新台湾元");
			// put("TWD", "新台湾元");
			put("ZZZ", "其他币种");
			put("zzz", "其他币种");

			put("072", "俄罗斯贸易卢布");
			put("RUR", "俄罗斯贸易卢布");

			put("196", "卢布");
			put("RUB", "卢布");

			// add sunh
			put("056", "印尼卢比");
			put("IDR", "印尼卢比");
			put("134", "巴西里亚尔");
			put("BRL", "巴西里亚尔");
			put("213", "新台币");
			put("TWD", "新台币");

		}
	};
	/**贵金属币种翻译*/
	public static Map<String, String> code_Map = new HashMap() {
		{
			this.put("035001", "黄金(克)/人民币");
			this.put("068001", "白银(克)/人民币");
			this.put("845001", "铂金(克)/人民币");
			this.put("844001", "钯金(克)/人民币");
			this.put("034014", "黄金(盎司)/美元");
			this.put("036014", "白银(盎司)/美元");
			this.put("045014", "铂金(盎司)/美元");
			this.put("841014", "钯金(盎司)/美元");
		}
	};
	/**贵金属币种翻译*/
	public static Map<String, String> code_Map_Left = new HashMap() {
		{
			this.put("035", "黄金(克)");
			this.put("068", "白银(克)");
			this.put("845", "铂金(克)");
			this.put("844", "钯金(克)");
			this.put("034", "黄金(盎司)");
			this.put("036", "白银(盎司)");
			this.put("045", "铂金(盎司)");
			this.put("841", "钯金(盎司)");
		}
	};
	/**贵金属币种翻译*/
	public static Map<String, String> code_Map_Right = new HashMap() {
		{
			this.put("001", "人民币");
			this.put("014", "美元");

		}
	};
	/**
	 * 货币---部分货币进行精简
	 */
	public static Map<String, String> CurrencyShort = new HashMap<String, String>() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("", "不可选择");
			put("000", "不可选择");
			put("001", "人民币元");
			put("CNY", "人民币元");
			put("012", "英镑");
			put("GBP", "英镑");
			// change sunh
			put("013", "港币");
			put("HKD", "港币");
			put("014", "美元");
			put("USD", "美元");
			put("015", "瑞士法郎");
			put("CHF", "瑞士法郎");
			put("016", "德国马克");
			put("DEM", "德国马克");
			put("017", "法国法郎");
			put("FRF", "法国法郎");
			put("018", "新加坡元");
			put("SGD", "新加坡元");
			put("020", "荷兰盾");
			put("NLG", "荷兰盾");
			put("021", "瑞典克朗");
			put("SEK", "瑞典克朗");
			put("022", "丹麦克朗");
			put("DKK", "丹麦克朗");
			put("023", "挪威克朗");
			put("NOK", "挪威克朗");
			put("024", "奥地利先令");
			put("ATS", "奥地利先令");
			put("025", "比利时法郎");
			put("BEF", "比利时法郎");
			put("026", "意大利里拉");
			put("ITL", "意大利里拉");
			put("027", "日元");
			put("JPY", "日元");
			put("028", "加拿大元");
			put("CAD", "加拿大元");
			put("029", "澳大利亚元");
			put("AUD", "澳大利亚元");
			put("068", "人民币银");
			put("036", "美元银");
			put("034", "美元金");
			put("XAU", "美元金");
			put("035", "人民币金");
			put("GLD", "人民币金");
			put("844", "人民币钯金");
			put("845", "人民币铂金");
			put("841", "美元钯金");
			put("045", "美元铂金");
			put("038", "欧元");
			put("EUR", "欧元");
			put("042", "芬兰马克");
			put("FIM", "芬兰马克");
			put("081", "澳门元");
			put("MOP", "澳门元");
			put("082", "菲律宾比索");
			put("PHP", "菲律宾比索");
			put("084", "泰国铢");
			put("THB", "泰国铢");
			put("087", "新西兰元");
			put("NZD", "新西兰元");
			put("088", "韩元");
			put("KRW", "韩元");
			put("094", "记账美元");
			put("095", "清算瑞士法郎");
			put("056", "印尼盾");
			put("IDR", "印尼盾");
			put("064", "越南盾");
			put("VND", "越南盾");
			put("096", "阿联酋迪拉姆");
			put("AED", "阿联酋迪拉姆");
			put("126", "阿根廷比索");
			put("ARP", "阿根廷比索");
			put("134", "巴西雷亚尔");
			put("BRL", "巴西雷亚尔");
			put("053", "埃及磅");
			put("EGP", "埃及磅");
			put("085", "印度卢比");
			put("INR", "印度卢比");
			put("057", "约旦第纳尔");
			put("JOD", "约旦第纳尔");
			put("179", "蒙古图格里克");
			put("MNT", "蒙古图格里克");
			put("076", "尼日尼亚奈拉");
			put("NGN", "尼日尼亚奈拉");
			put("062", "罗马尼亚列伊");
			put("ROL", "罗马尼亚列伊");
			put("093", "土耳其里拉");
			put("TRL", "土耳其里拉");
			put("246", "乌克兰格里夫纳");
			put("UAH", "乌克兰格里夫纳");
			put("070", "南非兰特");
			put("ZAR", "南非兰特");
			put("101", "哈萨克斯坦坚戈");
			put("KZT", "哈萨克斯坦坚戈");
			put("080", "赞比亚克瓦查");
			put("ZMK", "赞比亚克瓦查");
			put("065", "匈牙利福林");
			put("HUF", "匈牙利福林");
			// change sunh
			put("032", "林吉特");
			put("MYR", "林吉特");
			put("072", "俄罗斯贸易卢布");
			put("RUR", "俄罗斯贸易卢布");

			put("196", "卢布");
			put("RUB", "卢布");
			put("-1", "通配");
			// put("096", "美元指数");
			put("091", "突尼斯第纳尔");
			put("TND", "突尼斯第纳尔");
			put("059", "科威特第纳尔");
			put("KWD", "科威特第纳尔");
			put("098", "也门里亚尔");
			put("YER", "也门里亚尔");

			// add sunh
			put("056", "印尼卢比");
			put("IDR", "印尼卢比");
			put("134", "巴西里亚尔");
			put("BRL", "巴西里亚尔");
			put("213", "新台币");
			put("TWD", "新台币");

		}
	};

	/**
	 * 转账类型
	 */
	public static Map<String, String> transType = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("全部", "PB999");
			put("关联转账", "PB021");
			put("行内转账", "PB031");
			put("国内跨行转账", "PB032");
			put("定期存款", "PB022");
			put("信用卡行内转账", "PB052");
			put("手机号转账", "PB035");
			// put("跨境转账", "PB036");
			put("外币跨境汇款(向境外他行汇款)", "PB036");
			put("外币跨境汇款(向境外中行汇款)", "PB047");
			put("人民币跨境汇款", "PB026");
			put("跨行实时收付款", "PB113");
			put("中银财互通汇至境内", "PB141");
			put("中银财互通汇至境外", "PB142");
			put("中行内定向转账", "PB033");
			put("国内跨行定向转账", "PB034");
			put("电子现金充值", "PB011");
		}
	};
	public static List<String> transTypeList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{

			add("全部");
			add("关联转账");
			add("行内转账");
			add("国内跨行转账");
			add("定期存款");
			add("信用卡行内转账");
			add("手机号转账");
			// add("跨境转账");
			add("外币跨境汇款(向境外他行汇款)");
			add("外币跨境汇款(向境外中行汇款)");
			add("人民币跨境汇款");
			add("跨行实时收付款");
			add("中银财互通汇至境内");
			add("中银财互通汇至境外");
			add("中行内定向转账");
			add("国内跨行定向转账");
			add("电子现金充值");

		}
	};
	/**
	 * 账户类型
	 */
	public static Map<String, String> AccountType = new HashMap<String, String>() {
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
		};
	};
	/**
	 * 双向宝--币种--人民币
	 */
	public static Map<String, String> CurrencyRmb = new HashMap<String, String>() {

		private static final long serialVersionUID = 1L;

		{

			put("001", "人民币元");
			put("CNY", "人民币元");

		}
	};
	/**
	 * 钞汇标致 双向宝--人民币
	 * 
	 */
	public static Map<String, String> CurrencyCashremitO = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{

			put("0", "-");
			put("00", "-");

		}
	};
	/**
	 * 钞汇标致 双向宝--美元----现汇钞
	 * 
	 */
	public static Map<String, String> CurrencyCashremitT = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{

			put("01", "现钞");
			put("1", "现钞");
			put("11", "现钞");
			put("21", "现钞");
			put("02", "现汇");
			put("2", "现汇");
			put("10", "现汇");
			put("20", "现汇");
			put("CAS", "现钞");// 基金 持仓 返回
			put("TRN", "现汇");
		}
	};
	/**
	 * 钞汇标致 双向宝--美元--现汇
	 * 
	 */
	public static Map<String, String> CurrencyCashremitTh = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{

		}
	};
	/**
	 * 钞汇标志 字典
	 */
	public static Map<String, String> CurrencyCashremit = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("", "-");
			put("undefined", "-");
			put("0", "-");
			put("00", "-");
			put("01", "现钞");
			put("1", "现钞");
			put("11", "现钞");
			put("21", "现钞");
			put("02", "现汇");
			put("2", "现汇");
			put("10", "现汇");
			put("20", "现汇");
			put("CAS", "现钞");// 基金 持仓 返回
			put("TRN", "现汇");
		}
	};

	// 外汇 宁焰红 add start
	/**
	 * forexStorageCashLeftList:外汇左边菜单栏
	 */
	@BocModule("外汇")
	public static ArrayList<ImageAndText> forexStorageCashLeftList = null;
	static {

		forexStorageCashLeftList = new ArrayList<ImageAndText>();
		ImageAndText icon1 = new ImageAndText(R.drawable.forex_rate_info_img, "行情与购买", false,"forexStorageCash_1"); // 可以不登陆
		icon1.setClassName("com.chinamworld.bocmbci.biz.forex.rate.ForexRateInfoOutlayActivity");
		ImageAndText icon2 = new ImageAndText(R.drawable.forex_customer, "我的外汇","forexStorageCash_2");
		icon2.setClassName("com.chinamworld.bocmbci.biz.forex.customer.ForexCustomerRateInfoActivity");
		ImageAndText icon4 = new ImageAndText(R.drawable.forex_query, "成交状况查询","forexStorageCash_3");
		icon4.setFastShowText("外汇成交状况查询");
		icon4.setClassName("com.chinamworld.bocmbci.biz.forex.strike.ForexStrikeQueryActivity");
		ImageAndText icon3 = new ImageAndText(R.drawable.icon_left_wtjycx, "委托状况查询","forexStorageCash_4");
		icon3.setFastShowText("委托状况查询");
		icon3.setClassName("com.chinamworld.bocmbci.biz.forex.quash.ForexQuashQueryActivity");
		forexStorageCashLeftList.add(icon1);
		forexStorageCashLeftList.add(icon2);
		forexStorageCashLeftList.add(icon4);
		forexStorageCashLeftList.add(icon3);// 401功能
	}
	/**
	 * 外汇交易方式名称:08---市价即时,07---限价即时,03---获利委托,04---止损委托,05---二选一委托,11---追击止损委托
	 */
	public static List<String> forexTradeStyleList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("市价即时");
			add("限价即时");
			add("获利委托");// 新功能401
			add("止损委托");// 新功能401
			add("二选一委托");// 新功能401
			add("追击止损委托");// 新功能603
		}
	};
	/**
	 * 外汇交易方式名称:08---市价即时,07---限价即时
	 */
	public static List<String> forexTradeNoTwoList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("市价即时");
			add("限价即时");
			add("获利委托");// 新功能401
			add("止损委托");// 新功能401
			add("追击止损委托");// 新功能603
		}
	};
	/**
	 * 外汇交易方式:08---市价即时,07---限价即时,03---获利委托 ,04--止损委托,05--二选一委托,11--追击止损委托
	 */
	public static Map<String, String> forexTradeStyleCodeMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("08", "市价即时");
			put("07", "限价即时");
			put("03", "获利委托");
			put("04", "止损委托");
			put("05", "二选一委托");
			put("11", "追击止损委托");
		}
	};
	/** 时间 */
	public static List<String> forexTimesList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("00");
			add("01");
			add("02");
			add("03");
			add("04");
			add("05");
			add("06");
			add("07");
			add("08");
			add("09");
			add("10");
			add("11");
			add("12");
			add("13");
			add("14");
			add("15");
			add("16");
			add("17");
			add("18");
			add("19");
			add("20");
			add("21");
			add("22");
			add("23");
		}
	};
	/**
	 * 外汇交易方式对应的编码08---市价即时,07---限价即时,03---获利委托 ,04--止损委托,05--二选一委托,11--追击止损委托
	 */
	public static List<String> forexTradeStyleTransList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("08");
			add("07");
			add("03");
			add("04");
			add("05");
			add("11");
		}
	};
	/** 买卖标志 0-买入，1-卖出 */
	public static List<String> forexTradeSellOrBuyList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("0");
			add("1");

		}
	};
	/** 定期存单类型 */
	public static Map<String, String> fixAccTypeMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("140", "存本取息");
			put("110", "整存整取");
			put("150", "零存整取");
			put("160", "定活两便");
			put("166", "通知存款");
			put("152", "教育储蓄");
			put("912", "零存整取");
			put("935", "教育储蓄");
			put("913", "存本取息");
		}
	};

	/** 风险评估等级 */
	public static Map<String, String> fincRiskLevelList = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("1", "保守型投资者");
			put("2", "稳健型投资者");
			put("3", "平衡型投资者");
			put("4", "成长型投资者");
			put("5", "进取型投资者");

		}
	};
	/** 定期账户----存期 */
	public static Map<String, String> cdperiodMap = new HashMap<String, String>() {
		{
			put("01", "一个月");
			put("03", "三个月");
			put("06", "六个月");
			put("1", "一个月");
			put("3", "三个月");
			put("6", "六个月");
			put("12", "一年");
			put("24", "两年");
			put("36", "三年");
			put("60", "五年");
			put("72", "六年");
			put("00", "不固定期限");
			put("0", "不固定期限");
		}
	};

	// wjp add start
	public static String[] deptLeftList = new String[] { "我要存定期", "我的定期存款", "通知管理" };
	// wjp add end
	/** 钞汇列表 现钞、现汇 */
	public final static List<String> cashremitList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("现钞");
			add("现汇");
		}
	};
	public final static List<String> tranCashremitList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("-");
			add("现钞");
			add("现汇");
		}
	};
	public final static List<String> nullcashremitList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("-");
		}
	};
	public static final Map<String, String> cashRemitBackMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("00", "-");
			put("01", "现钞");
			put("02", "现汇");
		}
	};
	/**
	 * 钞汇标志
	 */
	public static Map<String, String> cashRemitMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("-", "00");
			put("现钞", "01");
			put("现汇", "02");
		}
	};

	/** 巨额赎回处理方式 */
	public static List<String> fundSellDealTypeList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("顺延赎回");
			add("取消赎回");

		}
	};
	/** 巨额赎回处理方式代码 1-顺延赎回，0-取消赎回 */
	public static List<String> fundSellDealTypeCodeList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("1");
			add("0");

		}
	};
	public static Map<String, String> ffundSellDealTypeMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("1", "顺延赎回");
			put("0", "取消赎回");
		}
	};

	/**
	 * 基金状态 xyl
	 */
	public static Map<String, String> fincFundStateMap = new Hashtable<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "正常开放");// 0
			put("1", "可认购");// 1
			put("2", "发行成功");// 2
			put("3", "发行失败");// 3
			put("4", "暂停交易");// 4
			put("5", "暂停申购");// 5
			put("6", "暂停赎回");// 6
			put("7", "权益登记");// 7
			put("8", "红利发放");// 8
			put("9", "基金封闭");// 9
			put("a", "基金终止");// a
			put("b", "停止开户");// b
		}
	};
	/**
	 * 交易方式
	 */
	public static List<String> tradeTypeCodeList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("0");
			add("1");

		}
	};
	public static List<String> tradeTypeNameList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("定期定额申购");
			add("定期定额赎回");

		}
	};
	public static Map<String, String> tradeTypeCodeToStrMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "定期定额申购");
			put("1", "定期定额赎回");
		}
	};
	/**
	 * 分红方式
	 */
	public static Map<String, String> bonusTypeMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "默认");// 0
			put("1", "现金分红");// 1
			put("2", "红利再投资");// 2
		}
	};
	/**
	 * 分红方式 0-默认,1-现金分红,2-红利再投资
	 */
	public static List<String> bonusTypeList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("0");
			add("1");
			add("2");

		}
	};
	/**
	 * 约定方式
	 */
	public static Map<String, String> promiseWayMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("约定转存", "R");
			put("非约定转存", "N");
		}
	};
	public static List<String> weekListTrans = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("单周");
			add("双周");
			add("月");
		}
	};

	/**
	 * 省份
	 */
	public static Map<String, String> Province = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{

			put("-", "-");
			put("-2", "-");
			put("", "北京");
			put("undefined", "北京");
			put("40142", "北京");
			put("40202", "天津");
			put("40303", "上海");
			put("40005", "上海总部");
			put("40308", "上海自贸区");
			put("40600", "西藏");
			put("40740", "河北");
			put("41041", "山西");
			put("41405", "内蒙古");
			put("41785", "辽宁");
			put("42208", "吉林");
			put("42465", "黑龙江");
			put("43016", "陕西");
			put("43251", "甘肃");
			put("43347", "宁夏");
			put("43469", "青海");
			put("43600", "新疆");
			put("43810", "山东");
			put("44433", "江苏");
			put("44899", "安徽");
			put("45129", "浙江");
			put("45481", "福建");
			put("46243", "河南");
			put("46405", "湖北");
			put("46955", "湖南");
			put("47370", "江西");
			put("47504", "广东（不含深圳）");
			put("47669", "深圳");
			put("47806", "海南");
			put("48051", "广西");
			put("48631", "四川");
			put("48642", "重庆");
			put("48882", "贵州");
			put("49146", "云南");
			put("00000", "未知");
			put("40004", "总行");
			put("40308", "上海自贸区");
			put("40208", "天津自贸区");
			put("49998", "广东自贸区");
			put("49404", "深圳自贸区");
			put("45599", "福建自贸区");
		}
	};
	/** 钞汇标志 */
	public static final Map<String, String> cashMapValue = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("00", "-");
			put("01", "现钞");
			put("02", "现汇");
		}
	};
	/** 钞汇标志list */
	public static List<String> cashMapValueList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("00");
			add("01");
			add("02");
		}
	};

	// add by wjp 2013-5-7 18:13:21
	public static final Map<String, String> userChannel = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("1", "网上银行");
			put("2", "手机银行");
			put("6", "银企对接");
			put("4", "家居银行");
			put("5", "微信银行");

		}
	};
	/** 通知状态 */
	public static final Map<String, String> notifyStatus = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("D", "已撤销");
			put("A", "正常");
			put("P", "已结清");
			put("W", "正常");
			put("O", "已逾期");
			put("B", "违约");
		}
	};
	/** 汇款状态 */
	public static final Map<String, String> remitStatus = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("A", "汇款成功");
			put("B", "汇款失败");
			put("RD", "待汇出");
			put("OU", "已汇款未收款");
			put("CR", "已汇款未收款");
			put("OK", "已成功收款");
			put("CL", "已取消汇款");
			put("L3", "密码错3次锁定");
			put("L6", "永久锁定");
		}
	};
	/** 汇款状态(列表查询时) */
	public static final Map<String, String> remitStatusThree = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("A", "汇款成功");
			put("B", "汇款失败");
			put("K", "状态未明");
		}
	};
	/** 取款状态 */
	public static final Map<String, String> drawStatus = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("A", "取款成功");
			put("B", "取款失败");
			put("RD", "待汇出");
			put("OU", "已汇款未收款");
			put("CR", "已汇款未收款");
			put("OK", "已成功收款");
			put("CL", "已取消汇款");
			put("L3", "密码错3次锁定");
			put("L6", "永久锁定");
		}
	};
	/** 催款状态 */
	public static final Map<String, String> gatherTranStatus = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("1", "未付");
			put("2", "已付");
			put("3", "已撤销");
			put("4", "状态未明");
			put("5", "过期未付");
		}
	};

	/** 现金宝交易状态 */
	public static final Map<String, String> cashBankTranStatus = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "成功");
			put("1", "进行中");
			put("2", "失败");
		}
	};
	/** 现金宝交易渠道 */
	public static final Map<String, String> cashBankFrontType = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("01", "网银");
			put("10", "手机银行");
		}
	};

	/** 付款人类型 */
	public static final Map<String, String> payerType = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("1", "网上银行用户");
			put("2", "手机银行用户");
		}
	};
	/** 发起渠道 */
	public static final Map<String, String> startWay = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("1", "网上银行");
			put("2", "手机银行");
		}
	};

	/** 提款类型 */
	public static final Map<String, String> redrawType = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "现金");
			put("1", "手动转账");
			put("2", "自动转账");
		}
	};

	/** 建卡渠道类型 */
	public static final Map<String, String> channelType = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("4", "家具银行");
			put("2", "手机银行");
			put("1", "网上银行");
			put("CSR", "电话银行");
			put("EISS", "客服人工");
			put("JFEN", "积分365");
			put("BCSP", "缤纷生活");
		}
	};

	/** 收取方式 */
	public static final Map<String, String> chargeModeType = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "一次性收取");
		}
	};

	/** POS消费验证方式 */
	public static final Map<String, String> posValidateType = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "签名");
			put("1", "密码+签名");
		}
	};

	/** 安全因子组合数据 */
	public static final Map<String, String> factorListMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("Smc", "手机验证码:");
			put("Otp", "动态口令:");
		}
	};

	/** 还款方式 */
	public static final Map<String, String> paymentType = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "主动还款");
			put("1", "自动还款");
		}
	};

	/** 还款金额设定 */
	public static final Map<String, String> paymentModeType = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("FULL", "全额还款");
			put("MINP", "最低还款额还款");
			put("PART", "部分还款");
		}
	};

	/** 回话超时的code */
	public static final ArrayList<String> timeOutCode = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("otp.token.false.lock");
			add("otp.token.true.lock");
			add("role.invalid_user");
			add("validation.session_invalid");
			add("conversationMap.null");
			add("validation.session_timeout");
		}
	};

	/** 不能取消通信的交易方法名称 */
	public static final ArrayList<String> noDissMissMethod = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;

		{
			add(Login.LOGIN_INFO_API);// 登录接口
			add("PsnICTransferNoRelevanceRes");// 为他人充值提交
			add("PsnVFGTrade");// 双向宝---PsnVFGTrade
			add("PsnVFGBailTransfer");// 双向宝----PsnVFGBailTransfer
			add("PsnVFGCancelOrder");
			add("PsnTransLinkTransferSubmit");
			add("PsnFinanceICTransfer");// 为本人充值提交
			add("PsnICTransferNoRelevance");
			add("PsnXpadProductBuyResult");// 产品购买
			add("PsnXpadSignResult");// 周期性产品续约协议签约/签约结束
			add("PsnXpadHoldProductAndRedeem");// 持有产品赎回
			add("PsnXpadCancelTrad");// 撤单确认
			add("PsnTransBocTransferSubmit");
			add("PsnTransNationalTransferSubmit");
			add("PsnCrcdTransferExternalResult");
			add("PsnCrcdForeignPayOffResult");
			add("PsnCrcdTransferPayOffResult");
			add("PsnCrcdServiceSetResult");
			add("PsnCrcdAppertainTranSetResult");
			add("PsnCrcdDividedPayBillSetResult");
			add("PsnCrcdDividedPayConsumeResult");
			add("PsnCrcdDividedPayAdvanceResult");
			add("PsnCrcdVirtualCardFunctionSetSubmit");
			add("PsnCrcdVirtualCardApplySubmit");
			add("PsnForexTrade");
			add("PsnForexQuashSubmit");
			add("PsnTransLumpsumTimeDepositNew");
			add("PsnTransConsolidatedTimeAndSavingsNew");
			add("PsnTransDepositWithdrawalSubmitAction");
			add("PsnTransTerminalOrCurrentDepositSubmit");
			add("PsnTransExtendLingcunDeposite");
			add("PsnTransExtendEducationDeposite");
			add("PsnTransCurrentSavingToCallDeposit");
			add("PsntransCallDepositToCurrentSaving");
			add("PsnTransActCollectionSubmit");
			add("PsnTransActPaymentSubmit");
			add("PsnSVRCancelAccRelation");// 服务设定 取消关联

			add(BocInvt.OFAFINANCETRANSFER);// 理财资金转出

			add(Prms.PRMS_TTADE_RESOUT);// 贵金属交易
			add(Prms.PRMS_QUSH_SUBMIT);// 贵金属撤单

			add("PsnFundScheduledBuy");// 基金定投
			add("PsnFundBuy");// 基金买入
			add("PsnFundNightBuy");// 基金买入挂单
			add("PsnFundSell");// 基金赎回
			add("PsnFundNightSell");// 基金赎回挂单
			add("PsnFundConversionResult");// 基金转换结果
			add("PsnFundNightConversionResult");// 基金转换挂单
			add("PsnFundAttentionAdd");// 添加基金关注
			add("PsnFundAttentionCancel");// 取消基金关注
			add("PsnFundBonusResult");// 基金分红
			add("PsnFundNightBonusResult");// 基金分红挂单
			add("PsnFundScheduledSell");// 定期定额赎回
			add("PsnFundConsignAbort");// 当日交易基金撤单
			add("PsnFundAppointCancel");// 指定日期交易撤单
			add("PsnFundDdAbort");// 基金定投撤单
			add("PsnFundScheduleSellCancel");// 定赎撤单
			add("PsnFundScheduledBuyModify");// 基金定期定额申购修改
			add("PsnFundScheduledSellModify");// 基金定期定额赎回修改

			add(Bond.METHOD_BOND_BUYRESULT);// 债券买入
			add(Bond.METHOD_SELL_RESULT);// 债券卖出

			add(Safety.METHOD_BUY_SUBMIT);// 保险投保提交

			add(Blpt.METHOD_NEWAPPLY_RESULT);// 账单缴费申请服务
			add(Blpt.METHOD_PAY_RESULT);// 账单缴费提交
			add(Blpt.METHOD_CANCER_RESULT);// 账单撤销缴费

			add(SBRemit.SBREMIT_BREMIT);// 结售汇 购汇
			add(SBRemit.SBREMIT_SREMIT);// 结售汇 结汇

			add(Prms.PRMS_QUSH_SUBMIT);// 贵金属 撤单
			// 401
			add(Tran.TRAN_PSNPASSWORDREMITPAYMENT_API);// atm汇款提交
			// add(Tran.TRAN_PSNPASSWORDREMITFREECANCEL_API);// 密码汇款取消汇款
			add(BocInvt.BOCINVT_PSNXPADAPPLYAGREEMENTRESULT_API);// 投资协议申请
			add(BocInvt.BOCINVT_PSNXPADAGREEMENTMODIFYRESULT_API);// 周期性产品续约协议修改提交
			add(BocInvt.BOCINVT_PSNXPADAGREEMENTCANCEL_API);// 周期性产品续约协议解约
			add(BocInvt.BOCINVT_PSNXPADAUTOMATICAGREEMENTMAINTAINRESULT_API);// 协议维护
			add(Tran.TRANS_PSNTRANSNATIONALCHANGEBOOKING_API);// 国内跨行转账非工作时间转预约
			add("PsnMobileTransferSubmit");//
			add("PsnTransSetUpNotifySubmit");//
			add("PsnMobileCancelTrans");//
			add("PsnMobileWithdrawal");//

			// 402
			add(Tran.REMITSETMEALAPPLY_API);// 汇款笔数套餐签约交易
			add(Tran.TRAN_REMITSETMEALMODIFY_API);// 汇款笔数套餐修改提交交易
			add(Tran.TRAN_REMITSETMEALCANCEL_API);// 汇款笔数套餐解除自动续约提交交易
			add(Safety.SafetyInsuranceCancel);// 保险撤保
			add(Safety.SafetyInsuranceReturn);// 保险退保
			add(Safety.SafetyInsuranceNewSubmit);// 保险续保提交交易
			// add(Safety.SafetyInsuranceMaintainSubmit);// 保险续期缴费提交交易

			add(CashBank.PSN_CASH_BANK_SIGN); // 开通现金宝接口
			add(CashBank.PSN_CASH_BANK_IN); // 现金宝转入资金接口
			add(CashBank.PSN_CASH_BANK_OUT); // 现金宝转出资金接口

			add(Loan.PSN_LOAN_CYCLELOAN_APPLYSUBMIT); // 个人循环贷款提交交易接口
			add(Loan.PSN_LOAN_CHANGE_LOANREPAY_ACCOUNTSUBMIT); // 变更还款账户提交交易接口

			add(Order.METHOD_BRANCHORDER_TAKEON);// 网点排队-远程取号
			add(Order.METHOD_BRANCHORDER_APPLY);// 网点排队-预约排队

			add(Collect.PsnCBCollectAdd);// 跨行资金归集设置
			add(Collect.PsnCBCollectModify);// 跨行资金归集修改
			add(Collect.PsnCBCollectDel);// 跨行资金归集删除
			add(Collect.PSN_CBCOLLECT_ONTIME_API);// 跨行资金归集一键归集
			// T43
			add(Tran.TRANSDIRBOCTRANSFERSUBMIT);// 中行内定向转账提交
			add(Tran.TRANSDIRNATIONALTRANSFERSUBMIT);// 国内跨行汇款定向提交
			add(Tran.TRANSDIREBPSNATIONALTRANSFERSUBMIT);// 国内跨行实时定向提交
			add(Tran.PSNEBPSREALTIMEPAYMENTTRANSFER_API);// 国内跨行实时提交

			add(Plps.METHODANNUITYACCSIGN);// 养老金开户
			add(Plps.METHODANNUITYAPPLYSUBMIT);// 信息变更提交
			add(Plps.METHODSIGNSUBMIT);// 签约提交
			add(Plps.CUSTOMERINFOUPDATE);// 手机号修改
			add(Plps.METHODSIGNINFOCHANGE);// 签约信息修改

			// P405基金
			add(Finc.METHOD_PSNFUNDSCHEDULEDBUYPAUSERESUME);
			add(Finc.METHOD_PSNFUNDSCHEDULEDSELLPAUSERESUME);
			
			// P503 结汇购汇   
			add(SBRemit.SBREMIT_BREMIT);
			add(SBRemit.SBREMIT_SREMIT);
			// P505 结汇购汇   
			add("PsnFessBuyExchangeHibs");//购汇(HIBS新)
			add("PsnFessSellExchangeHibs");//结汇（HIBS新)
			add("PsnFessGetUpperLimitOfForeignCurr");//最大金额试算
		}
	};

	/** 安全因子提示 */
	public static final Map<String, String> securityMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("Smc", "手机验证码:");
			put("Otp", "动态口令:");

		}
	};
	// 外汇双向宝 宁焰红 start
	@BocModule("双向宝")
	public static ArrayList<ImageAndText> isForexStorageCashLeftList = null;
	static {
		isForexStorageCashLeftList = new ArrayList<ImageAndText>();
		ImageAndText icon4 = new ImageAndText(R.drawable.icon_left_sxbhq, "双向宝行情", false,"isForexStorageCash_1");
		icon4.setClassName("com.chinamworld.bocmbci.biz.lsforex.rate.IsForexTwoWayTreasureActivity");
		ImageAndText icon1 = new ImageAndText(R.drawable.icon_left_bzjjy, "保证金交易","isForexStorageCash_2");
		icon1.setClassName("com.chinamworld.bocmbci.biz.lsforex.bail.IsForexBailInfoActivity");
		ImageAndText icon2 = new ImageAndText(R.drawable.icon_left_wdsxb, "我的双向宝","isForexStorageCash_3");
		icon2.setClassName("com.chinamworld.bocmbci.biz.lsforex.myrate.IsForexMyRateInfoActivity");
		ImageAndText icon3 = new ImageAndText(R.drawable.icon_left_jyzkcx, "交易状况查询","isForexStorageCash_4");
		icon3.setClassName("com.chinamworld.bocmbci.biz.lsforex.query.IsForexQueryMenuActivity");
		ImageAndText icon5 = new ImageAndText(R.drawable.icon_left_qygl, "签约管理","isForexStorageCash_5");
		icon5.setClassName("com.chinamworld.bocmbci.biz.lsforex.manageacc.IsForexBailProduceActivity");
		isForexStorageCashLeftList.add(icon4);
		isForexStorageCashLeftList.add(icon2);
		isForexStorageCashLeftList.add(icon1);
		isForexStorageCashLeftList.add(icon5);
		isForexStorageCashLeftList.add(icon3);

	}
	/** 外汇双向宝 委托类型 */
	public static final Map<String, String> isForexExchangeTranType = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("MI", "市价即时");
			put("LI", "限价即时");
			put("PO", "获利委托");
			put("SO", "止损委托");
			put("OO", "二选一委托");
			put("IO", "追加委托");
			put("TO", "连环委托");
			put("MO", "多选一委托");
			put("CO", "委托撤单");
			put("FO","追击止损委托");
		}
	};

	/** 外汇双向宝 交易状态 */
	public static final Map<String, String> isForexOrderStatusMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{

			put("N", "有效");
			put("U", "未生效");
//			put("S", "平仓标志");
			put("S", "成交");
//			put("R", "已到价");
			put("R", "成交");
			put("C", "撤销");
			put("E", "过期失效");
			put("O", "选择失效");
//			put("X", "入账失败");
			put("X", "失败");
		}
	};
	/** 外汇双向宝 交易渠道 */
	public static final Map<String, String> isForexchannelTypeMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("C", "柜台");
			put("P", "电话");
			put("N", "网银");
			put("T", "自助终端");
			put("A", "客户端");
			put("S", "系统斩仓");
		}
	};
	/** 未平仓交易状况查询---成交类型 */
	public static final Map<String, String> isForexFirstTypeMap = new HashMap<String, String>() {
		{
			put("P", "获利");
			put("S", "止损");
		}
	};
	private static final long serialVersionUID = 1L;
	/** 外汇双向宝 买卖方向 B买入、S卖出 */
	public static final Map<String, String> isForexdirectionMap = new HashMap<String, String>() {
		{
			put("B", "买入");
			put("S", "卖出");
		}
	};
	/** 外汇双向宝 买卖方向名称 B买入、S卖出 */
	public static final List<String> isForexSellTagList = new ArrayList<String>() {
		{
			add("买入");
			add("卖出");
		}
	};
	/** 外汇双向宝 买卖方向代码 B买入、S卖出 */
	public static final List<String> isForexSellTagCodeList = new ArrayList<String>() {
		{
			add("B");
			add("S");
		}
	};
	/** 外汇双向宝交易方式名称 MI市价即时、LI限价即时PO获利委托、SO止损委托、OO二选一委托、IO追加委托、TO连环委托 */
	public static final List<String> isForexExchangeTypeList = new ArrayList<String>() {
		{
			add("市价即时");
			add("限价即时");
			add("获利委托");// 402双向宝
			add("止损委托");// 402双向宝
			add("二选一委托");// 402双向宝
			// add("追加委托");
			// add("连环委托");
		}
	};
	/** 外汇双向宝交易方式代码 MI市价即时、LI限价即时 PO获利委托、SO止损委托、OO二选一委托、IO追加委托、TO连环委托 */
	public static final List<String> isForexExchangeTypeCodeList = new ArrayList<String>() {
		{
			add("MI");
			add("LI");
			add("PO");
			add("SO");
			add("OO");
			add("IO");// IO追加委托
			add("TO");// TO连环委托
			add("FO");
		}
	};
	/** 外汇双向宝 交易方式代码 MI市价即时、LI限价即时 */
	public static final Map<String, String> isForexExchangeTypeCodeMap = new HashMap<String, String>() {

		{
			put("MI", "市价即时");
			put("LI", "限价即时");
			put("PO", "获利委托");
			put("SO", "止损委托");
			put("OO", "二选一委托");
			put("IO", "追加委托");
			put("TO", "连环委托");
			put("FO","追击止损委托");
		}
	};

	/** 追加委托、连环委托 ：时使用P获利，S止损 */
	public static final Map<String, String> isForexHuoliAndZhisunMap = new HashMap<String, String>() {

		{
			put("P", "获利");
			put("S", "止损");
		}
	};
	/** 追加委托、连环委托 ：时使用P获利，S止损 */
	public static final List<String> isForexHuoliAndZhisunList = new ArrayList<String>() {
		{
			add("P");
			add("S");
		}
	};

	/** 外汇双向宝建仓标志 */
//	public static final List<String> isForexJcTagList = new ArrayList<String>() {
//		{
//			add("Y");
//			add("N");
//		}
//	};
	
	/** 外汇双向宝建仓标志 */
	public static final List<String> isForexJcTagzhuijizhisunList = new ArrayList<String>() {
		{
			add("建仓");
			add("先开先平");//先开先平
			add("指定平仓");//指定平仓
		}
	};

	
	public static final List<String> isForexJczhuijizhisunTag = new ArrayList<String>() {
		{
			add("Y");
			add("N");//先开先平
			add("C");//指定平仓
		}
	};
	
	public static final Map<String,String > isForexJczhuijizhisunMap = new  HashMap<String, String>() {
		{
			put("Y","建仓");
			put("N","先开先平");//先开先平
			put("C","指定平仓");//指定平仓
		}
	};
	
	public static final Map<String, String> mapTradeBack = new HashMap<String, String>(){
		{
		put("O", "建仓");
		put("N", "先开先平");
		put("C", "指定平仓");
		put("L", "司法强制处理");
		put("S", "系统斩仓");
		}
	};
	
	/** 金额格式化时没有小数点的币种 */
	public static List<String> codeNoNumber = new ArrayList<String>() {
		{
			add("027");// 日元
			add("JPY");
			add("088");// 韩元
			add("KRW");
			add("064");// 越南盾
			add("VND");
			// add("068");//人民币银
			// add("845");//人民币铂金
			// add("844");//人民币钯金
			// add("036");//美元银

		}
	};
	/** 金额格式化时保留一位小数点的币种 */
	public static List<String> codeOneNumber = new ArrayList<String>() {
		{
			add("045");// 美元铂金
			add("841");// 美元钯金
		}
	};
	/** 特殊币种-日元 */
	public static List<String> spetialCodeList = new ArrayList<String>() {
		{
			add("027");
			add("JPY");
		}
	};
	/** 对账单 转账类型 */
	public static final Map<String, String> isForexfundTransferTypeMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("TF", "资金转账");
			put("PL", "损益结转保证金");
			put("TD", "交易利息结转保证金");
		}
	};
	/** 对账单 转账方向 */
	public static final Map<String, String> isForextransferDirMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("I", "转入");
			put("O", "转出");
		}
	};

	/**
	 * 保证金存入/转出 I:银行资金账户转证券保证金账户,O:证券保证金账户转银行资金账户
	 */
	public static final Map<String, String> isForexfundTransferDirMap = new HashMap<String, String>() {
		{
			put("I", "资金账户转保证金账户");
			put("O", "保证金账户转资金账户");
		}
	};
	/**
	 * 保证金存入/转出 I:银行资金账户转证券保证金账户,O:证券保证金账户转银行资金账户
	 */
	public static List<String> isForexfundTransferDirList = new ArrayList<String>() {
		{
			add("I");
			add("O");
		}
	};
	/** 人民币不显示钞汇标志 */
	public static List<String> rebList = new ArrayList<String>() {
		{
			add("001");
			add("CNY");
			add("035");
			add("GLD");
			add("844");
			add("845");
			add("068");
		}
	};
	/**
	 * 保证金转入转出： 1:钞；2:汇；0:人民币
	 */
	public static final Map<String, String> isForexcashRemitMap = new HashMap<String, String>() {
		{
			put("1", "现钞");
			put("2", "现汇");
			put("0", "");
		}
	};
	/**
	 * 保证金转入转出： 1:钞；2:汇；0:人民币
	 */
	public static List<String> isForexcashRemitList = new ArrayList<String>() {
		{
			add("1");
			add("2");
			add("0");
		}
	};
	/** 人民币 */
	public static List<String> rmbCodeList = new ArrayList<String>() {
		{
			add("001");
			add("CNY");
		}
	};
	/** 美元 */
	public static List<String> myCodeList = new ArrayList<String>() {
		{
			add("014");
			add("USD");
		}
	};
	/**欧元**/
	public static List<String> ouCodeList = new ArrayList<String>() {
		{
			add("038");
			add("EUR");
		}
	};
	/**港币**/
	public static List<String> gangCodeList = new ArrayList<String>() {
		{
			add("013");
			add("HKD");
		}
	};
	
	/**027日元**/
	public static List<String> riCodeList = new ArrayList<String>() {
		{
			add("027");
			add("JPY");
		}
	};
	
	/**029澳元**/
	public static List<String> aOCodeList = new ArrayList<String>() {
		{
			add("029");
			add("AUD");
		}
	};
	/** 双向宝----平仓按钮---保留一位小数 */
	public static final List<String> goldList = new ArrayList<String>() {
		{
			// add("068");// , "人民币银"
			// add("036");// , "美元银"
			add("034");// , "美元金"
			add("XAU");// ", "美元金"
			// add("035");// , "人民币金"
			// add("GLD");// , "人民币金"
			// add("844");// , "人民币钯金"
			// add("845");// , "人民币铂金"
			add("841");// , "美元钯金"
			add("045");// , "美元铂金"
		}
	};
	/** 双向宝----平仓按钮---保留一位小数 */
	public static final List<String> rmbGoldList = new ArrayList<String>() {
		{
			// add("068");// , "人民币银"
			// add("036");// , "美元银"
			// add("034");// , "美元金"
			// add("XAU");// ", "美元金"
			add("035");// , "人民币金"
			add("GLD");// , "人民币金"
			// add("844");// , "人民币钯金"
			// add("845");// , "人民币铂金"
			// add("841");// , "美元钯金"
			// add("045");// , "美元铂金"
		}
	};
	/** 双向宝---双向宝交易----交易金额 */
	public static final List<String> silverGoldList = new ArrayList<String>() {
		{
			add("068"); // 人民币银
		}
	};
	/** 双向宝---双向宝交易----交易金额 */
	public static final List<String> platinumGoldList = new ArrayList<String>() {
		{
			add("844");// 人民币钯金
			add("845");// 人民币铂金
		}
	};
	/** 双向宝---双向宝交易----交易金额 */
	public static final List<String> dollarGoldList = new ArrayList<String>() {
		{
			add("036");// 美元银
		}
	};
	/** 双向宝---双向宝交易----交易金额 */
	public static final List<String> dollarPlatGoladList = new ArrayList<String>() {
		{
			add("841");// , "美元钯金"
			add("045");// , "美元铂金"
		}
	};

	/** 外汇---委托状况查询---委托状态 */
	public static final Map<String, String> isForexQuashStateMap = new HashMap<String, String>() {
		{
			put("U", "委托失败");
			put("N", "待成交");
			put("R", "正在处理");
			put("S", "已成交");
			put("F", "交易金额不足");
			put("X", "交易失败");
			put("C", "已撤单");
			put("E", "到期未成交");
			put("O", "其它原因");
		}
	};
	// 外汇双向宝 宁焰红 end

	/** 转账状态 */
	public static final Map<String, String> tranStatusMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("A", "交易成功");
			put("8", "已删除");
			put("B", "交易失败");
			put("12", "交易失败");
			put("G", "银行处理中");
			put("7", "提交成功");
			put("H", "银行处理中");
			put("F", "待银行处理");
			put("I", "提交成功");
			put("K", "支付未明");
			put("L", "银行已受理");
		}
	};
	/** 附属卡查询--借方---支出 */
	public static final List<String> debtList = new ArrayList<String>() {
		{
			add("DEBT");
			add("NMON");
		}
	};
	/** 虚拟银行卡的状态 */
	public static final Map<String, String> virtualCrcdStatusMap = new HashMap<String, String>() {
		{
			put("1", "有效");
			put("0", "已注销");
		}
	};

	// 结售汇 冯思敏 start
	@BocModule("结售汇")
	public static ArrayList<ImageAndText> sbRemitLeftList = null;
	static {
		sbRemitLeftList = new ArrayList<ImageAndText>();
		ImageAndText icon1 = new ImageAndText(R.drawable.sbremit_sb, "我的结汇购汇", true,"sbRemit_1");
		icon1.setClassName("com.chinamworld.bocmbci.biz.sbremit.mysbremit.ChooseAccountActivity");
		ImageAndText icon2 = new ImageAndText(R.drawable.sbremit_query_history, "历史交易查询", true,"sbRemit_2");
		icon2.setClassName("com.chinamworld.bocmbci.biz.sbremit.histrade.HistoryTradeQueryActivity");
		ImageAndText icon3 = new ImageAndText(R.drawable.sbremit_rateinfo_outlay, "结汇购汇行情", false,"sbRemit_3");
		icon3.setClassName("com.chinamworld.bocmbci.biz.sbremit.rate.SBRemitRateInfoOutlayActivity");
		icon3.setOutlayClassName("com.chinamworld.bocmbci.biz.sbremit.rate.SBRemitRateInfoOutlayActivity");
		sbRemitLeftList.add(icon1);
		sbRemitLeftList.add(icon2);
		sbRemitLeftList.add(icon3);

	}

	/** 业务类型 */
	public static final Map<String, String> bizTypeMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("结汇购汇", "1");
		}
	};

	// P502功能外置 结售汇 币种名称数据字典
	public static List<String> currencyList = new ArrayList<String>() {
		{
			add("-");
			add("欧元");
			add("美元");
			add("英镑");
			add("澳大利亚元");
			add("日元");
			add("加拿大元");
			add("港币");
			add("新加坡元");
			add("瑞士法郎");
			add("挪威克朗");
			add("瑞典克朗");
			add("丹麦克朗");
			add("新西兰元");
			// 601 新增币种
			add("澳门元");
			add("林吉特");
			add("菲律宾比索");
			add("卢布");
			add("泰国铢");
			add("巴西里亚尔");
			add("印尼卢比");
			add("韩国元");
			add("新台币");
		}
	};

	// P502功能外置 结售汇牌价(排序)
	public static Map<String, Object> currencySort = new HashMap<String, Object>() {
		{
			// 美元
			put("014", 0);
			put("USD", 0);

			// 澳大利亚元
			put("029", 1);
			put("AUD", 1);

			// 加拿大元
			put("028", 2);
			put("CAD", 2);

			// 港币
			put("013", 3);
			put("HKD", 3);

			// 英镑
			put("012", 4);
			put("GBP", 4);
			// 欧元
			put("038", 5);
			put("EUR", 5);

			// 日元
			put("027", 6);
			put("JPY", 6);
			// 新西兰元
			put("087", 7);
			put("NZD", 7);

			// 新加坡元
			put("018", 8);
			put("SGD", 8);
			// 泰国铢
			put("084", 9);
			put("THB", 9);
			// 韩元
			put("088", 10);
			put("KRW", 10);
			// 新台币
			put("213", 11);
			put("TWD", 11);

			// 瑞士法郎
			put("015", 12);
			put("CHF", 12);
			// 瑞典克朗
			put("021", 13);
			put("SEK", 13);
			// 丹麦克朗
			put("022", 14);
			put("DKK", 14);
			// 卢布
			put("196", 15);
			put("RUB", 15);
			// 挪威克朗
			put("023", 16);
			put("NOK", 16);
			// 菲律宾比索
			put("082", 17);
			put("PHP", 17);
			// 澳门元
			put("081", 18);
			put("MOP", 18);

			// 印尼卢比
			put("056", 19);
			put("IDR", 19);
			// 巴西里亚尔
			put("134", 20);
			put("BRL", 20);

			// 阿联酋迪拉姆
			put("096", 21);
			put("AED", 21);
			// 印度卢比
			put("085", 22);
			put("INR", 22);
			// 南非兰特
			put("070", 23);
			put("ZAR", 23);

			// 林吉特
			// put("032", 19);603屏蔽林吉特
			// put("MYR", 19);

		}
	};

	/** P502结售汇功能外置 币种key-value对照 */
	public static Map<String, String> moneyTypeValueKey = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("014", "美元");
			put("USD", "美元");
			put("029", "澳大利亚元");
			put("AUD", "澳大利亚元");
			put("028", "加拿大元");
			put("CAD", "加拿大元");
			put("013", "港币");
			put("HKD", "港币");
			put("012", "英镑");
			put("GBP", "英镑");
			
			put("038", "欧元");
			put("EUR", "欧元");
			put("027", "日元");
			put("JPY", "日元");
			put("087", "新西兰元");
			put("NZD", "新西兰元");
			put("018", "新加坡元");
			put("SGD", "新加坡元");
			put("084", "泰国铢");
			put("THB", "泰国铢");
			put("088", "韩元");
			put("KRW", "韩元");
			put("213", "新台币");
			put("TWD", "新台币");
			put("015", "瑞士法郎");
			put("CHF", "瑞士法郎");
			put("021", "瑞典克朗");
			put("SEK", "瑞典克朗");
			put("022", "丹麦克朗");
			put("DKK", "丹麦克朗");
			// P502 ADD
			put("196", "卢布");
			put("RUB", "卢布");
			put("023", "挪威克朗");
			put("NOK", "挪威克朗");
			
			put("082", "菲律宾比索");
			put("PHP", "菲律宾比索");
			// 601 新增币种
			put("081", "澳门元");
			put("MOP", "澳门元");
			put("056", "印尼卢比");
			put("IDR", "印尼卢比");
			put("134", "巴西里亚尔");
			put("BRL", "巴西里亚尔");
			put("096", "阿联酋迪拉姆");
			put("AED", "阿联酋迪拉姆");
			put("085", "印度卢比");
			put("INR", "印度卢比");
			
			put("070", "南非兰特");
			put("ZAR", "南非兰特");
			
			
			
			
			
			put("032", "林吉特");
			put("MYR", "林吉特");
			
			

		}
	};

	/** 汇款笔数套餐----套餐类型 */
	public static Map<String, String> remitSignTypeMap = new HashMap<String, String>() {
		{
			put("100/1/486", "100笔/1月-486元");
			put("500/3/1886", "500笔/3月-1886元");
			put("1500/6/3886", "1500笔/6月-3886元");
			put("3000/12/5886", "3000笔/12月-5886元");
			put("10000/12/8886", "10000笔/12月-8886元");
			put("100/12/1086", "100笔/12月-1086元");
		}
	};
	/** 汇款笔数套餐----套餐类型 */
	public static List<String> remitSignTypeTrans = new ArrayList<String>() {
		{
			add("100笔/1月-486元");
			add("500笔/3月-1886元");
			add("1500笔/6月-3886元");
			add("3000笔/12月-5886元");
			add("10000笔/12月-8886元");
			add("100笔/12月-1086元");
		}

	};
	/** 汇款笔数套餐----套餐属性 */
	public static Map<String, String> remitSignAttributeMap = new HashMap<String, String>() {
		{
			put("1", "付款方套餐");
			put("2", "收款方套餐");
			put("3", "双向套餐");
		}
	};

	/** 汇款笔数套餐----套餐属性 */
	public static List<String> remitSignAttributeTrans = new ArrayList<String>() {
		{
			add("1");
			add("2");
			add("3");
		}

	};
	/** 汇款笔数套餐-----是否自动续约 */
	public static Map<String, String> remitautoFlagMap = new HashMap<String, String>() {
		{
			put("Y", "是");
			put("N", "否");
		}
	};
	public static List<String> rmbNameList = new ArrayList<String>() {
		{

			add("人民币元");
		}
	};
	/** 汇款笔数套餐明细查询----交易状态 */
	public static final Map<String, String> remitTranStatusMap = new HashMap<String, String>() {
		{
			put("0", "正常");
			put("1", "已冲正");
		}
	};
	/** 汇款笔数套餐明细查询----业务类型 */
	public static final Map<String, String> remitBussinessTypeMap = new HashMap<String, String>() {
		{
			put("T", "转账汇款");
			put("C", "异地取款");
		}
	};
	/** 汇款笔数套餐明细查询----交易类型 */
	public static final Map<String, String> remitTranTypeMap = new HashMap<String, String>() {
		{
			put("0", "原交易");
			put("1", "冲正交易");
		}
	};

	/**
	 * 滑动卡片请求相关信息的请求接口
	 */
	public static List<String> queryCardMethod = new ArrayList<String>() {
		{
			add(Third.METHOD_CECURITYTRADE_CECACCLIST);// 第三方存管 根据银行账户查询证券资金账户
		}
	};
	/** 信用卡----已出账单月份 */
	public static final List<String> monthNameList = new ArrayList<String>() {
		{
			add("一月份");
			add("二月份");
			add("三月份");
			add("四月份");
			add("五月份");
			add("六月份");
			add("七月份");
			add("八月份");
			add("九月份");
			add("十月份");
			add("十一月份");
			add("十二月份");
		}
	};
	public static final Map<String, String> monthNameMap = new HashMap<String, String>() {
		{
			put("一月份", "01");
			put("二月份", "02");
			put("三月份", "03");
			put("四月份", "04");
			put("五月份", "05");
			put("六月份", "06");
			put("七月份", "07");
			put("八月份", "08");
			put("九月份", "09");
			put("十月份", "10");
			put("十一月份", "11");
			put("十二月份", "12");
		}
	};
	@BocModule("贷款管理")
	public static ArrayList<ImageAndText> loanLeftMenuList = null;
	static {
		loanLeftMenuList = new ArrayList<ImageAndText>();
		ImageAndText ico3 = new ImageAndText(R.drawable.icon_left_daikuanguanli, "贷款查询","loan_1");
		ico3.setClassName("com.chinamworld.bocmbci.biz.loan.loanQuery.LoanQueryMenuActivity");
		ImageAndText ico1 = new ImageAndText(R.drawable.icon_left_shenqingdaikuan, "贷款用款","loan_2");
		ico1.setClassName("com.chinamworld.bocmbci.biz.loan.loanUse.LoanUseMenuActivity");
		ImageAndText ico2 = new ImageAndText(R.drawable.icon_left_tiqianhuankuan, "贷款还款","loan_3");
		ico2.setClassName("com.chinamworld.bocmbci.biz.loan.loanRepay.LoanRepayMenuActivity");
		ImageAndText ico4 = new ImageAndText(R.drawable.icon_left_daikuanshenqing, "在线申请贷款","loan_4");
		ico4.setClassName("com.chinamworld.bocmbci.biz.loan.loanApply.LoanApplyMenuActivity");
		loanLeftMenuList.add(ico3);
		loanLeftMenuList.add(ico1);
		loanLeftMenuList.add(ico2);
		loanLeftMenuList.add(ico4);

	};

	public static final List<String> loanMonthList = new ArrayList<String>() {
		{
			add("01");
			add("02");
			add("03");
			add("04");
			add("05");
			add("06");
			add("07");
			add("08");
			add("09");
			add("10");
			add("11");
			add("12");
		}
	};
	/** 贷款--额度查询----额度状态 */
	public static final Map<String, String> quotaStatusMap = new HashMap<String, String>() {
		{
			put("05", "正常");
			put("10", "取消");
			put("20", "冻结");
			put("40", "关闭");
		}
	};

	/** 开户送电子银行--电子现金账户状态 */
	public static final Map<String, String> bocnetAccountStatus = new HashMap<String, String>() {
		{
			put("A", "正常状态");
			put("B", "主账户已销户状态");
			put("S", "已关户状态");
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

	/** 开户送电子银行---电子现金账户----业务摘要 */
	public static final Map<String, String> bocnetTransferType = new HashMap<String, String>() {
		{
			put("201", "圈存");
			put("404", "圈存");
			put("502", "换卡");
			put("506", "换卡");
			put("514", "补卡");
			put("518", "补卡");
			put("528", "转卡");
			put("530", "转卡");
			put("531", "转卡");
			put("711", "圈存");
			put("721", "圈存");
			put("731", "圈存");
			put("751", "圈存");
			put("760", "圈存");
			put("770", "圈存");
			put("781", "圈提");
			put("782", "圈提");
			put("784", "圈提");
			put("785", "圈提");
			put("790", "消费");
			put("791", "圈存");
			put("803", "圈提");
			put("805", "圈提");
			put("807", "圈提");
			put("809", "圈提");
			put("811", "回收");
			put("812", "调账");
			put("813", "调账");
			put("BTI", "转卡");
			put("BTO", "转卡");
			put("PCS", "消费");
			put("741", "补登");
			put("820", "退货");
			put("821", "退货");
			put("002", "退货");
			put("552", "换卡");
			put("556", "换卡");
			put("560", "换卡");
			put("568", "补卡");
			put("564", "补卡");
			put("572", "补卡");
			put("738", "跨行指定账户圈存");
			put("729", "跨行签约账户圈存");
			put("FEE", "手续费");
			put("799", "其他");
			put("798", "其他");
		}
	};

	/************************************* P405基金 ********************************/

	/**
	 * 定投定赎周期
	 */
	public static final Map<String, Object> transCycleMap = new LinkedHashMap<String, Object>() {
		{
			put("0", "按月");
			put("1", "按周");
		}
	};
	/**
	 * 定投结束条件
	 */
	public static final Map<String, Object> dtEndFlagMap = new LinkedHashMap<String, Object>() {
		{
			put("0", "无");
			put("1", "指定结束日期");
			put("2", "累计交易次数");
			put("3", "累计交易金额");
			// put("1", "指定日期");
			// put("2", "累计定投成功次数");
			// put("3", "累计成功扣款金额");
		}
	};

	/**
	 * 定赎结束条件
	 */
	public static final Map<String, Object> dsEndFlagMap = new LinkedHashMap<String, Object>() {
		{
			put("0", "无");
			put("1", "指定结束日期");
			put("2", "累计交易次数");
			put("3", "累计交易份额");
			// put("1", "指定日期");
			// put("2", "累计定赎成功次数");
			// put("3", "累计成功扣款金额");
		}
	};

	/**
	 * 交易类型
	 */
	public static final Map<String, Object> fundTranTypeMap = new HashMap<String, Object>() {
		{
			put("001", "TA开户申请");
			put("002", "TA销户申请");
			put("003", "修改客户资料申请");
			put("008", "登记基金账号申请");
			put("009", "取消TA关联关系申请");
			put("020", "认购申请");
			put("022", "申购申请");
			put("024", "赎回申请");
			put("026", "转托管转出一步申请");
			put("027", "转托管转入申请");
			put("028", "转托管转出两步申请");
			put("029", "修改分红方式申请");
			put("036", "基金转换(同机构)申请");
			put("037", "基金转换转入(不同机构)申请");
			put("038", "基金转换转出(不同机构)申请");
			put("039", "定期定额申购申请");
			put("059", "定投申请");
			put("060", "定投取消");
			put("061", "定投修改");
			put("062", "定赎申请");
			put("063", "定赎取消");
			put("064", "定赎修改");
			put("098", "快速赎回");
			put("101", "TA开户确认");
			put("102", "TA销户确认");
			put("103", "修改客户资料确认");
			put("104", "帐户冻结确认");
			put("105", "帐户解冻确认");
			put("108", "登记基金账号确认");
			put("109", "取消TA关联关系确认");
			put("120", "认购首次确认");
			put("122", "申购确认");
			put("124", "赎回确认");
			put("126", "转托管转出(一步)确认");
			put("127", "转托管转入确认");
			put("128", "转托管转出(两步)确认");
			put("129", "修改分红方式确认");
			put("130", "认购二次确认");
			put("131", "冻结确认");
			put("132", "解冻确认");
			put("133", "非交易过户确认");
			put("134", "非交易过户转入确认");
			put("135", "非交易过户转出确认");
			put("136", "基金转换(同机构)确认");
			put("137", "基金转换转入确认");
			put("138", "基金转换转出确认");
			put("139", "定期定额申购确认");
			put("142", "强制赎回确认");
			put("144", "强增确认");
			put("145", "强减确认");
			put("149", "募集失败");
			put("150", "基金清盘");
			put("198", "快速赎回确认");
			put("501", "中行开户申请");
			put("502", "中行销户申请");
			put("503", "中行修改客户资料申请");
			put("507", "对公资金账号维护申请");
			put("511", "中行开户确认");
			put("512", "中行销户确认");
			put("513", "中行修改客户资料确认");
			put("514", "短信通知签约");
			put("515", "短信通知解约");
			put("516", "对账单签约");
			put("517", "对账单解约");
			put("607", "冲正");
			put("608", "调账");
			put("609", "快速赎回重发");
			put("611", "现金分红确认");
			put("612", "再投资分红确认");
			put("620", "定投暂停");
			put("621", "定投开通");
			put("622", "定赎暂停");
			put("623", "定赎开通");
			put("640", "风险测评申请");
			put("650", "电子签名约定书申请");
			put("660", "电子签名合同申请");
		}
	};

	/**
	 * 申请类别
	 */
	public static final Map<String, Object> applyTypeMap = new HashMap<String, Object>() {
		{
			put("1", "定期定额申购");
			put("2", "定期定额赎回");
		}
	};

	/**
	 * 定投状态
	 */
	public static final Map<String, Object> scheduledBuyStatusMap = new HashMap<String, Object>() {
		{
			put("0", "正常");
			put("1", "暂停");
			put("2", "主动撤销");
			put("3", "达到预设结束日期失效");
			put("4", "达到预设累计扣款金额失效");
			put("5", "达到预设累计扣款次数失效");
			put("6", "扣款三次失败失效");
		}
	};

	/**
	 * 定赎状态
	 */
	public static final Map<String, Object> scheduledSellStatusMap = new HashMap<String, Object>() {
		{
			put("0", "正常");
			put("1", "暂停");
			put("2", "主动撤销");
			put("3", "达到预设结束日期失效");
			put("4", "达到预设累计赎回份额失效");
			put("5", "达到预设累计赎回次数失效");
			put("6", "余额不足失效");
		}
	};
	/** 约定转存状态 */
	public static Map<String, String> appointStatusremitautoFlagMap = new HashMap<String, String>() {
		{
			put("Y", "已约定");
			put("N", "未约定");
		}
	};
	/** 约定存期列表 --人民币 */
	public static final List<String> appointTermList = new ArrayList<String>() {
		{
			add("三个月");
			add("六个月");
			add("一年");
			add("两年");
			add("三年");
			add("五年");
		}
	};
	/** 约定存期列表----外币 */
	public static final List<String> wbappointTermList = new ArrayList<String>() {
		{
			add("一个月");
			add("三个月");
			add("六个月");
			add("一年");
			add("两年");
		}
	};
	/** 约定存期列表----外币 新西兰元　丹麦克朗　瑞典克朗　挪威克朗 没有两年 */
	public static final List<String> waappointTermListc = new ArrayList<String>() {
		{
			add("一个月");
			add("三个月");
			add("六个月");
			add("一年");
		}
	};
	/** 约定存期 */
	public static Map<String, String> appointTermMap = new HashMap<String, String>() {
		{
			put("一个月", "1");
			put("三个月", "3");
			put("六个月", "6");
			put("一年", "12");
			put("两年", "24");
			put("三年", "36");
			put("五年", "60");
		}
	};
	/** 约定方式列表 */
	public static final List<String> appointTypeList = new ArrayList<String>() {
		{
			add("请选择");
			add("增加本金");
			add("减少本金");
		}
	};
	/** 约定方式 */
	public static Map<String, String> appointTypeMap = new HashMap<String, String>() {
		{
			put("增加本金", "FR");
			put("减少本金", "TO");
		}
	};
	/** 约定方式 */
	public static Map<String, String> appointTypeDateMap = new HashMap<String, String>() {
		{
			put("FR", "加本转存");
			put("TO", "减本转存");
			put("CL", "全额减本");
			put("R", "本金全额转存");
		}
	};
	/** 利息转存方式列表 */
	public static final List<String> interestTypeList = new ArrayList<String>() {
		{
			add("请选择");
			add("转出利息");
		}
	};
	/** 利息转存方式 */
	public static Map<String, String> interestTypeMap = new HashMap<String, String>() {
		{
			put("请选择", "R");
			put("转出利息", "T");
		}
	};
	/** 利息转存方式 */
	public static Map<String, String> interestTypeDateMap = new HashMap<String, String>() {
		{
			put("R", "存入本金");
			put("T", "转出利息");
			put("I", "按指令付息");
		}
	};
	/** 执行方式 */
	public static Map<String, String> termExecuteTypegMap = new HashMap<String, String>() {
		{
			put("1", "仅本次到期");
			put("2", "历次到期");
		}
	};
	/** 签约渠道标识 */
	public static Map<String, String> signChnlFlagMap = new HashMap<String, String>() {
		{
			put("00", "柜台");
			put("99", "其他");
			put("01", "网银渠道");
			put("02", "银企对接/网上商城");
			put("03", "短信");
			put("04", "电话银行");
			put("05", "CSR");
			put("06", "FAX");
			put("07", "自助终端");
			put("08", "第三方渠道");
			put("09", "ATM");
			put("10", "手机银行");
			put("11", "家居银行");
			put("12", "POS");
			put("13", "OTC");
			put("14", "客户端");
			put("15", "银行卡柜台前端（GCS）");
			put("16", "中银易商");
			put("17", "即时通讯");
			put("18", "PAD");
		}
	};

	/** 省行机构号 */
	public static Map<String, String> FincBankIDMap = new HashMap<String, String>() {
		{
			put("2009", "北京");
			put("1367", "天津");
			put("879", "广东");
			put("261", "山东");
			put("806", "浙江");
			put("1368", "上海");
			put("762", "海南");
			put("1233", "福建");
			put("1953", "江苏");
			put("1958", "湖南");
			put("1370", "安徽");
			put("811", "辽宁");
			put("1131", "云南");
			put("1952", "广西");
			put("1369", "重庆");
			put("1942", "陕西");
			put("1132", "四川");
			put("300", "贵州");
			put("832", "宁夏");
			put("1945", "新疆");
			put("808", "河北");
			put("1957", "河南");
			put("1304", "山西");
			put("1949", "吉林");
			put("2008", "黑龙");
			put("836", "内蒙");
			put("833", "青海");
			put("524", "湖北");
			put("880", "江西");
			put("2065", "甘肃");
			put("15", "西藏");
			put("1302", "深圳");
			put("854", "总行");
			put("79218", "宁波");
			put("79162", "厦门");
			put("50092", "FTU中国银行上海市分行");
			put("82611", "HX中国银行新疆维吾尔自治区分行");
			put("00168", "中国银行上海总部");
			put("00345", "中国银行总行营业部");
		}
	};

	/**
	 * code转换message
	 */
	public static final CodeMessageList Code_Error_Message = new CodeMessageList() {
		{
			this.put("validation.no.relating.acc", Loan.LOAN_ACCOUNT_LIST_API, "暂未查询到您名下相关可变更还款账户的信息，请前往中行网点或咨询95566.");
			this.put("BANCS.0188", Loan.PSN_LOAN_CYCLELOAN_ACCOUNTLISTQUERY, "暂未查询到您名下相关的个人循环贷款用款申请，请前往中行网点或咨询95566.");
			this.put("BANCS.0344", Loan.LOAN_PSNLOANACCOUNTLISTANDDETAILQUERY_API, "暂未查询到该贷款额度下的用款记录,如有问题请前往中行网点或咨询95566.");
		}
	};


	/**
	 * 存款管理 - 整存整取过滤币种code add by lqp
	 */
	public static List<String> SaveRegularIlterCodeList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("001"); // -人民币
			add("014"); // -美元
			add("029"); // -澳大利元
			add("028"); // -加拿大元
			add("015"); // -瑞士法郎
			add("038"); // -欧元
			add("012"); // -英镑
			add("013"); // 港币
			add("027"); // -日元
			add("018"); // 新加坡元
			add("022"); // 丹麦克朗
			add("021"); // 瑞典克朗
			add("023"); // 挪威克朗
			add("087"); // 新西兰元
			add("088"); // 韩元
			add("081"); // 澳门元
			add("196"); // 卢布
		}
	};

	/**
	 * 存款管理 - 七天通知存款 只支持十种币种code add by lqp
	 */
	public static List<String> SaveRegularCurrencyCodeList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("001"); // -人民币
			add("014"); // -美元
			add("029"); // -澳大利元
			add("028"); // -加拿大元
			add("015"); // -瑞士法郎
			add("038"); // -欧元
			add("012"); // -英镑
			add("013"); // 港币
			add("027"); // -日元
			add("018"); // 新加坡元
		}
	};
	/** 交易状态 */
	public static Map<String, String> sbRemitValueKey_New = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("结汇", "01");
			put("购汇", "11");
		}
	};
	/**
	 * 证件类型  结汇购汇
	 */
	public static List<String> identityTypecollection = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{

			add("03");// 全部
			add("47");// 开放式基金产品
			add("48");// 资管计划产品
			add("49");// 信托产品
		}
	};

	// 定期存期 半年修改为六个月
	public static Map<String, String> SaveRegularCDPeriod = new HashMap<String, String>() {
		/**
			 * 
			 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "无");
			put("1", "一个月");
			put("3", "三个月");
			put("6", "六个月");
			put("12", "一年");
			put("24", "两年");
			put("36", "三年");
			put("60", "五年");
			put("72", "六年");
		}
	};




	/**
	 * 存款管理 - 七天通知存款过滤币种code add by lqp 新西兰元、丹麦克朗、挪威克朗、瑞典克朗币种
	 */
	public static List<String> SaveRegularCodeList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("096");
			add("134");
			add("056");
			add("085");
			add("032");
			add("082");
			add("084");
			add("213");
			add("070");
			add("088");
			add("196");
			add("081");
			add("021");
			add("022");
			add("023");
			add("087");
		}
	};
	
	
	/** 我的定期存款 定期一本通 存单详情列表 存期 */
	public static Map<String, String> depositReceipt = new HashMap<String, String>() {
		
		private static final long serialVersionUID = 1L;

		{
			put("0", "-");
			put("1", "一个月");
			put("3", "三个月");
			put("6", "半年");
			put("12", "一年");
			put("24", "两年");
			put("36", "三年");
			put("60", "五年");
			put("72", "六年");
		}
	};

	
	/**
	 * 信用卡
	 */
	public static final List<String> mycardList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			
			add("103");
			add("104");
			add("107");
		}
	};

		/**
	 * 灵活结息
	 * 结息牌价类型 1：起息日牌价 2：结息日牌价
	 */
	public static Map<String, String> marketType = new HashMap<String, String>() {

		private static final long serialVersionUID = 1L;

		{

			put("1", "起息日牌价");
			put("2", "结息日牌价");

		}
	};
	/**
	 * 灵活结息
	 * 结息牌价类型 1：起息日牌价 2：结息日牌价
	 */
	public static Map<String, String> interestProductType = new HashMap<String, String>() {

		private static final long serialVersionUID = 1L;

		{

			put("15", "中银步步高");
			put("16", "中银聚财通");

		}
	};
	
	
	public static Map<String, String> signedChannel = new HashMap<String, String>() {

		private static final long serialVersionUID = 1L;
		{
			put("00", "柜面");
			put("99", "其他");
			put("01", "Internet渠道");
			put("02", "银企对接/网上商城");
			put("10", "手机银行");
			put("11", "家居银行");
			put("18", "PAD");			
			put("17", "即时通讯(微信、易信、往来、LINE等即时通讯渠道)");

		}
	};
	
	public static Map<String, String> Status = new HashMap<String, String>() {

		private static final long serialVersionUID = 1L;
		{
			put("D", "删除");
			put("E", "已生效");
			put("C", "已失效");
			put("X", "已关户");


		}
	};
	/**
	 * 理财交易类型  常规/组合
	 */
	public static final Map<String,String> bociTraType = new HashMap<String, String>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("常规交易","01");
			put("组合购买","02");
		}
	};
	
	/**
	 * 中银理财 投资协议管理 失效协议 协议类型 request
	 */
	public static final Map<String,String> bocInvestAgrType = new HashMap<String, String>(){

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		{
			put("全部", "0");
			put("智能投资", "1");
			put("定时定额", "2");
			put("周期滚续", "3");
			put("余额理财", "4");
		}
	};
	/**
	 * 中银理财 投资协议管理 失效协议 协议类型 response
	 */
	public static final Map<String,String> bocInvestAgrTypeRes = new HashMap<String, String>(){

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		{
			put("1","智能投资");
			put("2", "定时定额投资");
			put("3", "周期滚续投资");
			put("4", "余额理财投资");
			put("5", "周期滚续投资");
		}
	};
	/**
	 * 中银理财 投资协议管理 失效协议 交易方向 response
	 */
	public static final Map<String,String> bocInvestTradeCodeRes = new HashMap<String, String>(){

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		{
			put("0", "赎回");
			put("1", "购买");
		}
	};
	/**
	 * 中银理财 投资协议管理 失效协议 购买频率 response
	 */
	public static final Map<String,String> bocInvestIsneedpurRes = new HashMap<String, String>(){

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		{
			put("0", "期初购买");
			put("1", "协议不购买");
		}
	};
	/**
	 * 中银理财 投资协议管理 失效协议 购买频率 response 二
	 */
	public static final Map<String,String> bocInvestIsneedpurResTwo = new HashMap<String, String>(){
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		{
			put("0", "期初申购");
			put("1", "不申购");
		}
	};
	
	/**
	 * 中银理财 投资协议管理 失效协议 赎回频率response
	 */
	public static final Map<String,String> bocInvestIsneedredRes = new HashMap<String, String>(){

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		{
			put("0", "到期赎回");
			put("1", "协议不赎回");
		}
	};
	/**
	 * 中银理财 投资协议管理 失效协议 赎回频率response 二
	 */
	public static final Map<String,String> bocInvestIsneedredResTwo = new HashMap<String, String>(){
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		{
			put("0", "期末赎回");
			put("1", "不赎回");
		}
	};
	/**
	 * 中银理财 投资协议管理 协议状态
	 */
	public static final Map<String,String> bocInvestAgrState = new HashMap<String, String>(){

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		{
			put("全部","0");
			put("正常","1");
			put("失效", "2");
		}
	};
	

	/** 交易类型  投资明细查询 */
	public static Map<String, String> inverstTradeTypeStr = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "赎回");
			put("1", "购买");
		}
	};
	/** 协议类型  投资协议管理 */
	public static Map<String, String> inverstAgreementTypeStr = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("1", "智能投资");
			put("2", "定时定额投资");
			put("3", "周期滚续投资");
			put("4", "余额理财投资");
			put("5", "周期滚续投资");
		}
	};
	/** 投资方式  投资协议管理 */
	public static Map<String, String> inverstTypeStr = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("1", "周期连续");
			put("2", "周期不连续");
			put("3", "多次购买");
			put("4", "多次赎回");
			put("5", "定时定额投资");
			put("6", "余额理财投资");
			put("7", "周期滚续");
			put("8", "周期滚续");
		}
	};
	/** 判断  投资协议管理 */
	public static Map<String, String> inverstJudgeStr = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "不判断");
			put("1", "判断");
		}
	};
	/** 赎回频率  投资协议管理 */
	public static Map<String, String> isNeedRedStr = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "到期赎回");
			put("1", "协议不赎回");
		}
	};
	/** 购买频率  投资协议管理 */
	public static Map<String, String> isNeedPurStr = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "期初购买");
			put("1", "协议不购买");
		}
	};
	
	/** 是否赎回  投资协议管理 */
	public static Map<String, String> isRedemptionStr = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "期末赎回");
			put("1", "不赎回");
		}
	};
	/** 是否购买  投资协议管理 */
	public static Map<String, String> isBuyStr = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "期初申购");
			put("1", "不申购");
		}
	};
	/**
	 * 通过字典中的value值，取得指定的指点中的第一对应的Key值。如果有重复的Value值，则取列表中第一个value对应的key值
	 * @return
	 */
	public static String getKeyByValueFromData(Map<String,String> map, String value){
		if(map == null)
			return  null;
		for(String key : map.keySet()){
			if(value == null && map.get(key) == null){
					return key;
			}
				
			if(value != null && value.equals(map.get(key))){
				   return key;
			}
		}
		return null;
	}
	
	
	// 定期存期 半年修改为六个月(利率上浮)
	public static Map<String, String> SaveRegularInterestRateFloating = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("无", "0");
			put("一个月", "1");
			put("三个月人行基准1.3倍", "33");
			put("六个月人行基准1.3倍", "66");
			put("一年人行基准1.3倍", "1212");
			put("三个月", "3");
			put("六个月", "6");
			put("一年", "12");
			put("两年", "24");
			put("三年", "36");
			put("五年", "60");
			put("六年", "72");
		}
	};
	
	
	// 定期存期 半年修改为六个月(利率上浮)
	public static Map<String, String> newSaveRegularCDPeriod = new HashMap<String, String>() {
		
		private static final long serialVersionUID = 1L;

		{
			put("0", "无");
			put("1", "一个月");
			put("33", "三个月人行基准1.3倍");
			put("66", "六个月人行基准1.3倍");
			put("1212", "一年人行基准1.3倍");
			put("3", "三个月");
			put("6", "六个月");
			put("12", "一年");
			put("24", "两年");
			put("36", "三年");
			put("60", "五年");
			put("72", "六年");
		}
	};
	/** 是否允许撤单*/
	public static Map<String, String> isCanCancleStr = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("0", "不允许撤单");
			put("1", "只允许当日撤单");
			put("2", "允许撤单");
		}
	};
	
	/** 钞汇标志 */
	public static final Map<String, String> cashRemitMapValue = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("null", "-");
			put("00", "-");
			put("01", "现钞");
			put("02", "现汇");
			put("0", "-");
			put("1", "现钞");
			put("2", "现汇");
		}
	};
	
	
		
	/** {全部= , 正常=0 , 已转让=1 , 已支取=2 , 止付=3}*/
	public static Map<String, String> LargeQueryTranTypeMap = new HashMap<String, String>() {
		/**
			 * 
			 */
		private static final long serialVersionUID = 1L;

		{
			put("全部", "");
			put("正常", "0");
			put("已转让", "1");
			put("已支取", "2");
			put("止付", "3");
		}
	};
	/**
	 * 理财被组合购买列表 交易状态
	 */
	public static Map<String,String> GuarantyImpawnPermitMap = new HashMap<String, String>(){
		private static final long serialVersionUID = 1L;
		{
			put("0","未成交");
			put("1","成交成功");
			put("2","成交失败");
		}
	};
	/**
	 * 服务查询记录方式 交易类型
	 */
	public static List<String> ServiceRecordTypeNameList = new ArrayList<String>() {
		{
			add("全部");    
			add("登录"); 
			add("退出"); 
			add("ATM无卡取款"); 
			add("B股银证转账"); 
			add("白银交易"); 
			add("保险"); 
			add("变更还款账户"); 
			add("铂金交易"); 
			add("存款质押贷款");
			add("大额存单");
			add("大额到账提醒"); 
			add("第三方存管"); 
			add("电子现金服务"); 
			add("电子支付"); 
			add("定期存款"); 
			add("非税收入收缴"); 
			add("个人智慧主办帐户"); 
			add("公益基金捐款"); 
			add("公用服务缴费"); 
			add("贵金属代理"); 
			add("贵金属积存"); 
			add("贵金属积利");
			add("黄金交易"); 
			add("汇款公司跨境汇款"); 
			add("汇款套餐");
			add("行内资金归集");
			add("基金交易"); 
			add("结汇购汇"); 
			add("跨行现金管理"); 
			add("跨行资金归集"); 
			add("理财产品到期提醒"); 
			add("理财直付"); 
			add("密码汇款"); 
			add("民生缴费"); 
			add("钯金交易"); 
			add("期权"); 
			add("企业家服务注销"); 
			add("全球账户管理");
			add("认证登录"); 
			add("设置类交易"); 
			add("申请定期/活期账户"); 
			add("申请开立存款证明"); 
			add("手机号转帐"); 
			add("手机交易码"); 
			add("手机取款"); 
			
			add("手机设备绑定/解绑设置"); 
			add("授信登录"); 
			add("双向宝"); 
			add("提前还款");
			add("特色存款");
			add("外汇交易");
			add("微银行消贷申请"); 
			add("小微企业对公账户批量转账"); 
			add("小微企业对公账户转账"); 
			add("协议支付"); 
			add("信用卡挂失"); 
			add("信用卡激活"); 
			add("信用卡交易"); 
			add("虚拟银行卡服务"); 
			add("循环贷款用款"); 
			add("异地通"); 
			add("银行本票/汇票申请"); 
			add("银联跨行无卡支付");
			add("银期转账"); 
			add("银商转账"); 
			add("银医通"); 
			add("预付卡充值"); 
			
			add("预设日期提前还款"); 
			add("预约换借记卡"); 
			add("预约指令"); 
			add("在线批量转账");
			add("在线申请贷款"); 
			add("债券交易"); 
			add("账户自助关联"); 
			add("中银财互通服务");
			add("中银理财计划I"); 
			add("中银理财计划II"); 
			add("中银易房通"); 
			add("中银易商交易(待支付)"); 
			add("中银易商交易(已支付)"); 
			add("中银易商交易(已撤销)"); 
			add("主动收款"); 
			add("转账汇款"); 
			add("资金划转"); 
			add("账户授权管理"); 
			 
		}
	};
	/**
	 * 服务查询记录方式 交易类型
	 */
	public static List<String>  ServiceRecordTypeCodeList = new ArrayList<String>() {
		{	
			add("01");
			add("16");
			add("17");
			add("83");
			add("15");
			add("53");
			add("75");
			add("92");
			add("70");
			add("74");
			add("99");
			add("78");
			add("14");
			add("58");
			add("20");
			add("04");
			add("p3");
			add("86");
			add("87");
			add("06");
			add("37");
			add("68");
			add("P5");
			
			add("09"); 
			add("90"); 
			add("62");
			add("35");
			add("10"); 
			add("25");
			add("28"); 
			add("93"); 
			add("79"); 
			add("36"); 
			add("61"); 
			add("81"); 
			add("71"); 
			add("69"); 
			add("V3");
			add("52");
			add("29"); 
			add("02"); 
			add("64"); 
			add("94"); 
			add("27"); 
			add("32"); 
			add("82"); 
			
			add("P1"); 
			add("80"); 
			add("43"); 
			add("72");
			add("P8");
			add("08");
			add("97"); 
			add("V2"); 
			add("V1"); 
			add("38"); 
			add("67"); 
			add("66"); 
			add("07"); 
			add("65"); 
			add("91"); 
			add("33"); 
			add("57"); 
			add("56");
			add("21"); 
			add("84"); 
			add("50"); 
			add("63"); 
			
			add("73"); 
			add("85"); 
			add("05"); 
			add("76"); 
			add("34"); 
			add("11"); 
			add("30"); 
			add("55");
			add("26"); 
			add("24"); 
			add("98"); 
			add("SvrRecType_01"); 	
			add("SvrRecType_02"); 
			add("SvrRecType_a03"); 
			add("31"); 
			add("03"); 
			add("P6");
			add("46");
			
			
		}
	};
	/**
	 * 服务记录查询类型
	 */
	public final static Map<String, String> service_type = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("01", "交易成功");
			put("02", "交易未成功");
			put("03", "交易指令被拒绝");
			put("04", "交易指令已受理");
			put("05", "交易指令已受理");
			put("06", "交易状态不明");
			put("07", "交易状态银行处理中");
			put("08", "提交成功");
		}
	};
	/**
	 * 服务记录查询类型
	 */
	public final static Map<String, String> service_channle = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("1", "网上银行");
			put("2", "手机银行");
			put("", "网上银行");
			put("5", "微信银行");
			put("6", "对接");
		}
	};
	/**
	 * 服务记录查询反显Map
	 */
	public static final Map<String, String> service_record = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("01", "全部");
			put("16", "登录");
			put("17", "退出");
			put("83", "ATM无卡取款");
			put("15", "B股银证转账");
			put("53", "白银交易");
			put("75", "保险");
			put("92", "变更还款账户");
			put("70", "铂金交易");
			put("74", "存款质押贷款");
			put("99", "大额存单");
			put("78", "大额到账提醒");
			put("14", "第三方存管");
			put("58", "电子现金服务");
			put("20", "电子支付");
			put("04", "定期存款");
			put("P3", "非税收入收缴");
			put("86", "个人智慧主办帐户");
			put("87", "公益基金捐款");
			put("06", "公用服务缴费");
			put("37", "贵金属代理");
			put("68", "贵金属积存");
			put("P5", "贵金属积利");
			put("09", "黄金交易");
			put("90", "汇款公司跨境汇款");
			put("62", "汇款套餐");
			put("35", "行内资金归集");
			put("10", "基金交易");
			put("25", "结汇购汇");
			put("28", "跨行现金管理");
			put("93", "跨行资金归集");
			put("79", "理财产品到期提醒");
			put("36", "理财直付");
			put("61", "密码汇款");
			put("81", "民生缴费");
			put("71", "钯金交易");
			put("69", "期权");
			put("V3", "企业家服务注销");
			put("52", "全球账户管理");
			put("29", "认证登录");
			put("02", "设置类交易");
			put("64", "申请定期/活期账户");
			put("94", "申请开立存款证明");
			put("27", "手机号转帐");
			put("32", "手机交易码");
			put("82", "手机取款");
			put("P1", "手机设备绑定/解绑设置");
			put("80", "授信登录");
			put("43", "双向宝");
			put("72", "提前还款");
			put("P8", "特色存款");
			put("08", "外汇交易");
			put("97", "微银行消贷申请");
			put("V2", "小微企业对公账户批量转账");
			put("V1", "小微企业对公账户转账");
			put("38", "协议支付");
			put("67", "信用卡挂失");
			put("66", "信用卡激活");
			put("07", "信用卡交易");
			put("65", "虚拟银行卡服务");
			put("91", "循环贷款用款");
			put("33", "异地通");
			put("57", "银行本票/汇票申请");
			put("56", "银联跨行无卡支付");
			put("21", "银期转账");
			put("84", "银商转账");
			put("50", "银医通");
			put("63", "预付卡充值");
			put("73", "预设日期提前还款");
			put("85", "预约换借记卡");
			put("05", "预约指令");
			put("P7", "原油宝");
			put("76", "在线批量转账");
			put("34", "在线申请贷款");
			put("11", "债券交易");
			put("30", "账户自助关联");
			put("55", "中银财互通服务");
			put("26", "中银理财计划I");
			put("24", "中银理财计划II");
			put("98", "中银易房通");
			put("SvrRecType_01", "中银易商交易（待支付）");
			put("SvrRecType_02", "中银易商交易（已支付）");
			put("SvrRecType_a03", "中银易商交易（已撤销）");
			put("31", "主动收款");
			put("03", "转账汇款");
			put("P6", "资金划转");
			put("46", "账户授权管理");
		}
	};
	/**
	 * 错误码前缀
	 */
	public static List<String> ErrorCodeList = new ArrayList<String>() {
		{
			add("EZTDG");    
			add("UTTM"); 
			add("OFDC"); 
			add("ALDS"); 
			add("CCMS"); 
			add("MICRLO"); 
			add("AMLMAS"); 
			add("BPS"); 
			add("OCRS"); 
			add("ATMP"); 
			add("PNS"); 
			add("WMS"); 
			add("EBCT"); 
			add("RCMS"); 
			add("SPTA"); 
			add("OIBS"); 
			add("TIPS"); 
			add("AUTD"); 
			add("XPADG"); 
			add("XPADO"); 
			add("XPAD2"); 
			add("EOMGCS"); 
			add("ICCD");
			add("SCFP"); 
			add("WES"); 
			add("ACS");
			add("IBAS");
			add("X-PADC"); 
			add("EPMS"); 
			add("RCPS"); 
			add("CDS"); 
			add("GCS"); 
			add("XPAD"); 
			add("CASS"); 
			add("HIBS"); 
			add("CPS"); 
			add("FXCC"); 
			add("GCMS"); 
			add("IST");
			add("EOM"); 
			add("CSPB"); 
			add("CCSS"); 
			add("IPS"); 
			add("FOCS"); 
			add("CTIS"); 
			add("CSP"); 			
			add("ETMS"); 
			add("ETOKEN"); 
			add("BFTS"); 
			add("TPCC"); 
			add("BOC2000");
			add("TPTS"); 
			add("CFIB"); 
			add("NFXS"); 
			add("FUND"); 
			add("BOND"); 
			add("BNCARD"); 
			add("BANCS"); 
			add("MCIS"); 
			 
			 
		}
	};

	/** 外汇只支持一下币种*/
	public static List<String> ForeignCurrencyCodeList = new ArrayList<String>() {

		private static final long serialVersionUID = 1L;

		{
			add("038014"); // 欧元/美元
			add("014027"); // 美元/日元
			add("029014"); // 澳元/美元
			add("012014"); // 英镑/美元
			add("014028"); // 美元/加元
			add("038013"); // 欧元/港币
			add("014013"); // 美元/港币
			add("029027"); // 澳元/日元
			add("013027"); // 港币/日元
			add("014015"); // 美元/瑞士法郎
			add("038029"); // 欧元/澳元
			add("014018"); // 美元/新加坡元
			add("038012"); // 欧元/英镑
			add("012027"); // 英镑/日元
			add("029028"); // 澳元/加元
			add("038027"); // 欧元/日元
			add("012013"); // 英镑/港币
			add("038028"); // 欧元/加元
			add("028027"); // 加元/日元
			add("029013"); // 澳元/港币
			add("028013"); // 加元/港币
			add("018013"); // 新加坡元/港币
			add("012029"); // 英镑/澳元
			add("038015"); // 欧元/瑞士法郎
			add("012028"); // 英镑/加元
			add("029015"); // 澳元/瑞士法郎
			add("015028"); // 瑞士法郎/加元
			add("015013"); // 瑞士法郎/港币
			add("038018"); // 欧元/新加坡元
			add("018027"); // 新加坡元/日元
			add("012018"); // 英镑/新加坡元
			add("015027"); // 瑞士法郎/日元
			add("012015"); // 英镑/瑞士法郎
			add("029018"); // 澳元/新加坡元
			add("028018"); // 加元/新加坡元
			add("014081"); // 美元/澳门元
			add("015018"); // 瑞士法郎/新加坡元
		}
	};


	/**
	 * 外汇货币
	 */
	public static Map<String, String> ForeignCurrency = new LinkedHashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{//欧元、港币、日元、澳元
			put("", "不可选择");
			put("000", "不可选择");
			put("001", "人民币元");
			put("CNY", "人民币元");
			put("012", "英镑");
			put("GBP", "英镑");
			put("013", "港币");
			put("HKD", "港币");
			put("014", "美元");
			put("USD", "美元");
			put("015", "瑞士法郎");
			put("CHF", "瑞士法郎");
			put("018", "新加坡元");
			put("SGD", "新加坡元");
			put("027", "日元");
			put("JPY", "日元");
			put("028", "加拿大元");
			put("CAD", "加拿大元");
			put("029", "澳元");
			put("AUD", "澳元");
			put("038", "欧元");
			put("EUR", "欧元");
			put("081", "澳门元");
			put("MOP", "澳门元");
		}
	};
}
