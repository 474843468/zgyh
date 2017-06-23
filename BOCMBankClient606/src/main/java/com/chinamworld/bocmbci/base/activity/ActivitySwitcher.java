package com.chinamworld.bocmbci.base.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bocmobile.buss.common.lianlong.BocCommonTools;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.abstracttools.AbstractActivitySwitcher;
import com.chinamworld.bocmbci.abstracttools.AbstractLoginTool;
import com.chinamworld.bocmbci.abstracttools.BaseHttpManager;
import com.chinamworld.bocmbci.abstracttools.ShowDialogTools;
import com.chinamworld.bocmbci.base.activity.BaseActivity.ActivityTaskType;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.DrawMoney;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.bii.constant.Push;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.biz.acc.mybankaccount.AccManageActivity;
import com.chinamworld.bocmbci.biz.assetmanager.capitalmanager.AssetManagerBalanceSheetActivity;
import com.chinamworld.bocmbci.biz.audio.AudioKeyInfoActivity;
import com.chinamworld.bocmbci.biz.blpt.BillPaymentMainActivity;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt.productlist.QueryProductOutlayActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.ui.MyInvetProductActivity;
import com.chinamworld.bocmbci.biz.bond.BondDataCenter;
import com.chinamworld.bocmbci.biz.bond.allbond.AllBondListActivity;
import com.chinamworld.bocmbci.biz.cashbank.mycash.CashBankMainActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.MyCreditCardActivity;
import com.chinamworld.bocmbci.biz.dept.DeptDataCenter;
import com.chinamworld.bocmbci.biz.dept.savereg.SaveRegularActivity;
import com.chinamworld.bocmbci.biz.drawmoney.DrawMoneyData;
import com.chinamworld.bocmbci.biz.drawmoney.remitout.RemitCardListActivity;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.biz.finc.finc_p606.FundPricesActivity;
import com.chinamworld.bocmbci.biz.finc.fundprice.FundPricesActivityNew;
import com.chinamworld.bocmbci.biz.finc.myfund.MyFincMainActivity;
import com.chinamworld.bocmbci.biz.foreign.details.ForeignExchangeRateActivity;
import com.chinamworld.bocmbci.biz.forex.rate.ForexRateInfoOutlayActivity;
import com.chinamworld.bocmbci.biz.gatherinitiative.GatherInitiativeData;
import com.chinamworld.bocmbci.biz.gatherinitiative.gatherquery.GatherQueryActivity;
import com.chinamworld.bocmbci.biz.goldbonus.accountmanager.AccountManagerMainActivity;
import com.chinamworld.bocmbci.biz.goldbonus.goldbonusoutside.GoldBonusOutLayActivity;
import com.chinamworld.bocmbci.biz.loan.LoanDataCenter;
import com.chinamworld.bocmbci.biz.loan.loanQuery.LoanQueryMenuActivity;
import com.chinamworld.bocmbci.biz.lsforex.rate.IsForexTwoWayTreasureNewActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.biz.plps.payment.project.PaymentAllProject;
import com.chinamworld.bocmbci.biz.preciousmetal.PreciousmetalLayoutActivity;
import com.chinamworld.bocmbci.biz.preciousmetal.goldstoremainpage.GoldstoreMainActivity;
import com.chinamworld.bocmbci.biz.prms.control.PrmsControl;
import com.chinamworld.bocmbci.biz.prms.price.PrmsNewPricesActivity;
import com.chinamworld.bocmbci.biz.prms.price.PrmsPricesActivity;
import com.chinamworld.bocmbci.biz.quickOpen.quickopen.StockThirdQuickOpenMustKnowActivity;
import com.chinamworld.bocmbci.biz.remittance.applicationForRemittance.RemittanceInfoInputActivity;
import com.chinamworld.bocmbci.biz.safety.safetyproduct.SafetyProductListActivity;
import com.chinamworld.bocmbci.biz.sbremit.SBRemitBaseActivity;
import com.chinamworld.bocmbci.biz.sbremit.SBRemitDataCenter;
import com.chinamworld.bocmbci.biz.sbremit.mysbremit.ChooseAccountActivity;
import com.chinamworld.bocmbci.biz.sbremit.rate.SBRemitRateInfoOutlayActivity;
import com.chinamworld.bocmbci.biz.setting.accmanager.AccountManagerActivity;
import com.chinamworld.bocmbci.biz.thridmanage.ThirdDataCenter;
import com.chinamworld.bocmbci.biz.thridmanage.cecuritytrade.CecurityTradeActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.atmremit.AtmRemitChooseActivity;
import com.chinamworld.bocmbci.biz.tran.collect.CollectData;
import com.chinamworld.bocmbci.biz.tran.collect.CollectMainActivity;
import com.chinamworld.bocmbci.biz.tran.mobiletrans.MobileTranActivity;
import com.chinamworld.bocmbci.biz.tran.mytransfer.currenttran.TransferManagerActivity1;
import com.chinamworld.bocmbci.biz.tran.twodimentrans.TwoDimenTransActivity1;
import com.chinamworld.bocmbci.boc.ModelBoc;
import com.chinamworld.bocmbci.constant.BaseLocalData;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpObserver;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.LoginTask.LoginCallback;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.Utils;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.chinamworld.bocmbci.biz.finc.control.FincControl;
//import com.chinamworld.bocmbci.biz.finc.fundprice.FundPricesActivityNew;
//import com.chinamworld.bocmbci.biz.finc.myfund.MyFincMainActivity;
//import com.boc.bocsoft.remoteopenacc.buss.activity.RemoteOpenAccActivity;
/**
 * @Title: ActivityS.java
 * @Description: activity公共模块跳转
 * @author : luql
 * @date: 2015年2月12日 下午4:47:28
 */

public class ActivitySwitcher extends AbstractActivitySwitcher implements HttpObserver {
	private static final String TAG = "ActivitySwitcher";
	/** 是否签约代理点 */
	public static boolean isSigned = false;

//	public Activity intentContext;
	
//	private BaseActivity activity;

	private String itemName;
	private boolean isFast;

	public ActivitySwitcher(BaseActivity activity) {
		Instance=this;
		this.activity = activity;
		intentContext  = activity;
	}

    /** activity ：上下文    
     * flag ：无用参数，主要是为了区分两个构造方法参数 */
	public ActivitySwitcher(Activity activity,boolean flag){
		Instance=this;
		this.activity = null;
		intentContext  = activity;
	}
	
	/**
	 * @Description: 打开快捷方式
	 * @param imageAndText
	 * @param fastIndex
	 * @author: luql
	 * @date: 2015年2月13日 上午10:50:13
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void openFast(ImageAndText imageAndText, int fastIndex) {
		if (imageAndText == null) {
			return;
		}
		ActivityTaskType type = ActivityTaskType.None;
		try {
			Class<?>  c = Class.forName(imageAndText.getClassName());
			type =( (BaseActivity)c.newInstance()).getActivityTaskType();
		} catch (Exception e) {
			return;
		} 
		if(type == ActivityTaskType.OneTask)
			ActivityTaskManager.getInstance().removeAllActivity();
		else if(type == ActivityTaskType.TwoTask){
			ActivityTaskManager.getInstance().removeAllSecondActivity();
		}
		// 交易限额设置需判断有无权限
		if (imageAndText
				.getClassName()
				.equals("com.chinamworld.bocmbci.biz.setting.limit.LimitSettingActivity")) {

			Map<String, Object> returnMap = (Map<String, Object>) BaseDroidApp
					.getInstanse().getBizDataMap()
					.get(ConstantGloble.BIZ_LOGIN_DATA);
			String segmentId = null;
			if (!StringUtil.isNullOrEmpty(returnMap)) {
				segmentId = (String) returnMap.get(Crcd.CRCD_SEGMENTID);
			}
			if (StringUtil.isNull(segmentId)
					|| ConstantGloble.CRCD_TEN.equals(segmentId)) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						intentContext.getString(R.string.crcd_no_crcd));
				return;
			}
		}
		this.isFast = true; // 是否主页面

		Intent intent = new Intent();
		if (imageAndText.getOutlayClassName() != null
				&& !BaseDroidApp.getInstanse().isLogin()) {
			ComponentName comp = new ComponentName(ConstantGloble.PACKAGE_NAME,
					imageAndText.getOutlayClassName());// 第一个参数是目标包名，第二个参数是目标包的activity名
			intent.setComponent(comp);
			intent.putExtra(ConstantGloble.COMEFROMFOOTFAST, true);
			intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
			intentContext.startActivity(intent);
		} else {
			if (imageAndText.getClassName().equals(
					RemitCardListActivity.class.getName())) {
				requestPsnMobileIsSignedAgent(); // 汇往取款人
				return;
			}
			ComponentName comp = new ComponentName(ConstantGloble.PACKAGE_NAME,
					imageAndText.getClassName());// 第一个参数是目标包名，第二个参数是目标包的activity名
			intent.setComponent(comp);
			intent.putExtra(ConstantGloble.COMEFROMFOOTFAST, true);
			intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
			intentContext.startActivity(intent);
		}

		LayoutValue.LEWFTMENUINDEX = imageAndText.MenuID;
		// 清楚转账模块数据
		TranDataCenter.getInstance().clearTranData();
		BaseDroidApp.getInstanse().setFastItemCHoosed(null);
	}




	/**
	 * @Description: 打开 模块activity
	 * @param name
	 */
	public void openModule(String name, boolean isFast) {
		this.itemName = name;
		this.isFast = isFast;
		LogGloble.i("必读消息", ActivitySwitcher.class.getSimpleName()+"类openModule(),"+"itmeName="+itemName+",isFast="+isFast);
		LayoutValue.LEWFTMENUINDEX="Default_First";
		if (intentContext.getString(R.string.mian_menu1).equals(itemName)) {// 账户管理
			if (isLogin()) {
				AccDataCenter.getInstance().clearAccData();
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
				intent.setClass(intentContext, AccManageActivity.class);
				intentContext.startActivity(intent);
			}
		} else if (intentContext.getString(R.string.mian_menu2).equals(itemName)) {// 转账汇款
			if (isLogin()) {
				TranDataCenter.getInstance().clearTranData();
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
				intent.setClass(intentContext, TransferManagerActivity1.class);
				intentContext.startActivity(intent);
			}
		} else if (intentContext.getString(R.string.mian_menu3).equals(itemName)) {// 存款管理
			if (isLogin()) {
				DeptDataCenter.getInstance().clearDeptData();
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
				intent.setClass(intentContext, SaveRegularActivity.class);
				intentContext.startActivity(intent);
			}
		} else if (intentContext.getString(R.string.mian_menu4).equals(itemName)) {// 贷款管理
			if (isLogin()) {
				LoanDataCenter.getInstance().clearLoanData();
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
				intent.setClass(intentContext, LoanQueryMenuActivity.class);
				intentContext.startActivity(intent);
			}
		} else if (intentContext.getString(R.string.mian_menu5).equals(itemName)) {// 信用卡
			if (isLogin()) {
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
				intent.setClass(intentContext, MyCreditCardActivity.class);
				intentContext.startActivity(intent);
			}
		} else if (intentContext.getString(R.string.mian_menu7).equals(itemName)) {// 账单缴付
			if (isLogin()) {
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
				intent.setClass(intentContext, BillPaymentMainActivity.class);
				// intent.putExtra(Blpt.KEY_TAG,
				// BillPaymentMainActivity.TAG_FORMAIN);
				intentContext.startActivity(intent);
			}
		} else if (intentContext.getString(R.string.mian_menu8).equals(itemName)) {// 外汇
			// if (isLogin()) {
//			Intent intent = new Intent();
//			intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
//			intent.setClass(intentContext, ForexRateInfoOutlayActivity.class);
//			intentContext.startActivity(intent);
			// }
			Intent intent = new Intent();
			intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
			intent.setClass(intentContext, ForeignExchangeRateActivity.class);
			intentContext.startActivity(intent);
		} else if (intentContext.getString(R.string.mian_menu9).equals(itemName)) { // 贵金属买卖
			// if (isLogin()) {
			// PrmsControl.getInstance().cleanAll();
			// Intent intent = new Intent();
			// intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
			// intent.setClass(activity, PrmsPricesActivity.class);
			// startActivity(intent);
			// }
			// wuhan
			boolean isLogin = BaseDroidApp.getInstanse().isLogin();
			if (isLogin) {
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
				intent.setClass(intentContext, PrmsNewPricesActivity.class);
				intentContext.startActivity(intent);
			} else {
				PrmsControl.getInstance().cleanAll();
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
				intent.setClass(intentContext, PrmsNewPricesActivity.class);
				intentContext.startActivity(intent);
			}

		} else if (intentContext.getString(R.string.mian_menu10).equals(itemName)) {// 基金
			// if (isLogin()) {
			// FincControl.getInstance().cleanAllData();
			// Intent intent = new Intent();
			// intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
			// intent.setClass(activity, MyFincMainActivity.class);
			// // intent.setClass(activity, //
			// // MyFincMainActivity.class);//nyh
			// activity.startActivity(intent);
			// }

			// P503基金功能外置
			FincControl.getInstance().cleanAllData();
//			if (BaseDroidApp.getInstanse().isLogin()) { // 我的基金
//				Intent intent = new Intent();
//				intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
//				intent.setClass(intentContext, MyFincMainActivity.class);
//				intentContext.startActivity(intent);
//			} else { // 全部行情(基金组合查询)
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
				// intent.setClass(activity, FundPricesActivity.class);
//				intent.setClass(intentContext, FundPricesActivityNew.class);
				intent.setClass(intentContext, FundPricesActivity.class);
				intentContext.startActivity(intent);
//			}
		} else if (intentContext.getString(R.string.mian_menu11).equals(itemName)) {// 理财计划
			if (BaseDroidApp.getInstanse().isLogin()) {
				BociDataCenter.getInstance().clearBociData();
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
				intent.setClass(intentContext, MyInvetProductActivity.class);
				// 默认进入我的理财产品页面
				intent.putExtra(ConstantGloble.COMEFROMFOOTFAST, false);
				intentContext.startActivity(intent);
			} else {
				BociDataCenter.getInstance().clearBociData();
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);

				intent.setClass(intentContext, QueryProductOutlayActivity.class);//P603原理财
//				intent.setClass(activity, ProductQueryAndBuyOutlayActivity.class);//P603新理财(业务确认使用原外置页面)
				//
				// intent.putExtra(ConstantGloble.COMEFROMFOOTFAST, true);
				intentContext.startActivity(intent);
			}
			// } else if
			// (activity.getString(R.string.mian_menu12).equals(itemName)) {//
			// 投资理财
			// if (isLogin()) {
			// Intent intent = new Intent();
			// intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
			// intent.setClass(activity, InvesHasOpenActivity.class);
			// activity.startActivity(intent);
			// }
		} else if (intentContext.getString(R.string.mian_menu13).equals(itemName)) {// 服务设定
			if (isLogin()) {
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
				intent.setClass(intentContext, AccountManagerActivity.class);
				intentContext.startActivity(intent);
			}
		} else if (intentContext.getString(R.string.mian_menu14).equals(itemName)) {// B股银期转账

		} else if (intentContext.getString(R.string.mian_menu15).equals(itemName)) {// B股银证转账

		} else if (intentContext.getString(R.string.mian_menu16).equals(itemName)) {// 第三方存管
			if (isLogin()) {
				ThirdDataCenter.getInstance().clear();
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
				intent.setClass(intentContext, CecurityTradeActivity.class);
				intentContext.startActivity(intent);
			}
		} else if (intentContext.getString(R.string.mian_menu17).equals(itemName)) {// 黄金双向宝

		} else if (intentContext.getString(R.string.mian_menu18).equals(itemName)) {// 手机取款
			if (isLogin()) {
				requestPsnMobileIsSignedAgent();
			}
		} else if (intentContext.getString(R.string.mian_menu19).equals(itemName)) {// 双向宝
//			if (BaseDroidApp.getInstanse().isLogin()) {
//				Intent intent = new Intent();
//				intent.putExtra(ConstantGloble.COM_FROM_MAIN, false);
//				intent.setClass(activity, IsForexRateInfoActivity.class);
//				activity.startActivity(intent);
//			} else {
//			}
			// P503功能外置
			Intent intent = new Intent();
			intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
			intent.setClass(intentContext, IsForexTwoWayTreasureNewActivity.class);
			intentContext.startActivity(intent);

		} else if (intentContext.getString(R.string.mian_menu21).equals(itemName)) {// 债券
			// if (isLogin()) {
			// Intent intent = new Intent();
			// intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
			// intent.setClass(activity, AllBondListActivity.class);
			// activity.startActivity(intent);
			// }

			// P503功能外置
			BondDataCenter.getInstance().clearAllData();
			Intent intent = new Intent();
			intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
			intent.setClass(intentContext, AllBondListActivity.class);
			intentContext.startActivity(intent);
		} else if (intentContext.getString(R.string.main_menu22).equals(itemName)) {// 手机号转账
			if (isLogin()) {
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
				intent.setClass(intentContext, MobileTranActivity.class);
				intentContext.startActivity(intent);
			}
		} else if (intentContext.getString(R.string.main_menu23).equals(itemName)) {// 二维码转账
			if (isLogin()) {
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
				intent.setClass(intentContext, TwoDimenTransActivity1.class);
				intentContext.startActivity(intent);
			}
		} else if (intentContext.getString(R.string.main_menu24).equals(itemName)) {// 主动收款
			if (isLogin()) {
				GatherInitiativeData.getInstance().clearGatherData();
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
				intent.setClass(intentContext, GatherQueryActivity.class);
				intentContext.startActivity(intent);
			}
		} else if (intentContext.getString(R.string.main_menu33).equals(itemName)) {//资产管理
			if (isLogin()) {
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
				intent.setClass(intentContext, AssetManagerBalanceSheetActivity.class);
				intentContext.startActivity(intent);
			}
		} else if (intentContext.getString(R.string.main_menu25).equals(itemName)) {// 结售汇
			// P502功能外置
			SBRemitDataCenter.getInstance().clearDrawMoneyData();
			if (BaseDroidApp.getInstanse().isLogin()) { // 进入我的结汇购汇


				if (BaseDroidApp.getInstanse().getActFirst() instanceof SBRemitBaseActivity) {

					Intent intent = new Intent();
					intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
					intent.setClass(intentContext, BaseDroidApp.getInstanse()
							.getActFirst().getClass());
					intentContext.startActivity(intent);

				} else {
					Intent intent = new Intent();
					intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
					intent.setClass(intentContext, ChooseAccountActivity.class);
					intentContext.startActivity(intent);
				}

			} else { // 进入未登录外汇牌价页面
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
				intent.setClass(intentContext, SBRemitRateInfoOutlayActivity.class);
				intentContext.startActivity(intent);

			}
		}else if (intentContext.getString(R.string.main_menu32).equals(itemName)) {//积利金
			if(BaseDroidApp.getInstanse().isLogin()){
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
				intent.setClass(intentContext, AccountManagerMainActivity.class);
				intentContext.startActivity(intent);
			} else {
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
				intent.setClass(intentContext, GoldBonusOutLayActivity.class);
				intentContext.startActivity(intent);
			}
		}else if (intentContext.getString(R.string.main_menu34).equals(itemName)) {//贵金属积存
			if(BaseDroidApp.getInstanse().isLogin()){
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
				intent.setClass(intentContext, GoldstoreMainActivity.class);
				intentContext.startActivity(intent);
			} else {
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
				intent.setClass(intentContext,PreciousmetalLayoutActivity.class);
				intentContext.startActivity(intent);
			}


		}else if (intentContext.getString(R.string.main_menu31).equals(itemName)) {// 跨境汇款
			if (isLogin()) {
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
				intent.setClass(intentContext, RemittanceInfoInputActivity.class);
				intentContext.startActivity(intent);
			}
		}  else if (intentContext.getString(R.string.main_menu26).equals(itemName)) {// 保险
			// if (isLogin()) {
			Intent intent = new Intent();
			intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
			intent.setClass(intentContext, SafetyProductListActivity.class);
			intentContext.startActivity(intent);
			// }
		} else if (intentContext.getString(R.string.zj_dianzizhifu).equals(itemName)) {// 移动支付
			boolean isLogin = BaseDroidApp.getInstanse().isLogin();
			if (!isLogin) {
//				Intent it = new Intent(intentContext, LoginActivity.class);
//				intentContext.startActivityForResult(it,
//						ConstantGloble.ACTIVITY_REQUEST_CODE_ZHIFU);
				new LoginTask(intentContext).exe(new LoginCallback() {
					@Override
					public void loginStatua(boolean isLogin) {
						ActivityTaskManager.getInstance().removeAllActivity();
						ActivityTaskManager.getInstance().removeAllSecondActivity();
//						Utils.startPayPlui(intentContext);
						Utils.aboutMapQuery(intentContext, ConstantGloble.AppPath_zhifu,
								ConstantGloble.ActAllPath_zhifu,
								ConstantGloble.ApkName_zhifu,
								ConstantGloble.ActAllPath_zhifu);
					}
				});
			} else {
				Utils.aboutMapQuery(intentContext, ConstantGloble.AppPath_zhifu,
						ConstantGloble.ActAllPath_zhifu,
						ConstantGloble.ApkName_zhifu,
						ConstantGloble.ActAllPath_zhifu);
			}
		} else if (intentContext.getString(R.string.main_menu28).equals(itemName)) {// ATM无卡取款
			if (isLogin()) {
				requestAccBankAccountList();
			}
		} else if (intentContext.getString(R.string.mian_collect).equals(itemName)) { // 跨行资金归集
			if (isLogin()) {
				CollectData.getInstance().clear();
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
				intent.setClass(intentContext, CollectMainActivity.class);
				intentContext.startActivity(intent);
			}
		} else if (intentContext.getString(R.string.main_menu30).equals(itemName)) {// 音频key(中银E盾)
			if (isLogin()) {
				Intent intent = new Intent();
				intent.setClass(intentContext, AudioKeyInfoActivity.class);
				intentContext.startActivity(intent);
			}
		}else if (intentContext.getString(R.string.main_menu29).equals(itemName)) {// 民生服务
			if (isLogin()) {
				requestPlpsMenu();
			}
		} else if (intentContext.getString(R.string.zj_zhongyin).equals(itemName)) {// 民生服务
			if (isLogin()) {
				Intent intent = new Intent(intentContext, StockThirdQuickOpenMustKnowActivity.class);
				intentContext.startActivity(intent);
			}
		} 
//		else if (activity.getString(R.string.online_service).equals(itemName)) {//在线客服
////			if(isLogin()){
//				Intent intent = new Intent(activity, OnlineServiceWebviewActivity.class);
//				activity.startActivity(intent);
////			}
//		}
		else if (intentContext.getString(R.string.main_menu27).equals(itemName)) {// 现金宝

			 if (isLogin()) {
			Intent intent = new Intent();
			intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
			intent.setClass(intentContext, CashBankMainActivity.class);
			intentContext.startActivity(intent);
			 }

		}
//		 else if (activity.getString(R.string.zj_zhongyin).equals(itemName)) { // 中银证券开户
//			// 判断当前设备是否安装了中银证券
//			BaseDroidApp.getInstanse().showZhongyinDialog(
//					activity.getString(R.string.zj_zhongyin_no_instal),
//					new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							BaseDroidApp.getInstanse().dismissErrorDialog();
//							// 下载并安装
//							PackageInfo packageInfo;
//							try {
//								packageInfo = activity
//										.getPackageManager()
//										.getPackageInfo(
//												"com.thinkive.mobile.account_zygj",
//												0);
//							} catch (NameNotFoundException e) {
//								packageInfo = null;
//								e.printStackTrace();
//							}
//							/** 未安装 */
//							if (packageInfo == null) {// 提示用户下载安装
//								Intent serviceIntent = new Intent(activity,
//										APPDownLoadService.class);
//								activity.startService(serviceIntent);
//							} else { // 直接打开
//								PackageManager packageManager = activity
//										.getPackageManager();
//								Intent intent = packageManager
//										.getLaunchIntentForPackage("com.thinkive.mobile.account_zygj");
//								if (intent == null) {
//									return;
//								}
//								activity.startActivity(intent);
//							}
//
//						}
//
//					});
//
//
//		}
		 else if (intentContext.getString(R.string.online_open_an_account).equals(itemName)) 
		{// 远程开户
			 
			 ModelBoc.BocStartActivityForResult(intentContext, "com.boc.bocsoft.remoteopenacc.buss.activity.RemoteOpenAccActivity");
//			Intent intent = new Intent();
//			intent.setClass(intentContext, RemoteOpenAccActivity.class);
//			activity.startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_CODE_REMOTE);



		}
		// else if (activity.getString(R.string.main_menu29).equals(itemName))
		// {// 民生服务
		// if (isLogin()) {
		// requestPlpsMenu();
		// }
		// }
	}

	/**
	 * 请求民生服务菜单
	 */
	private void requestPlpsMenu() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Plps.METHODLIVESERVICEMENUS);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "plpsMenuCallBack");
	}

	@SuppressWarnings("unchecked")
	public void plpsMenuCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		List<Map<String, Object>> mList = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(mList))
			return;
		PlpsDataCenter.getInstance().setLiveMenus(mList);
		// activity.startActivity(new Intent(activity,
		// LiveServiceMainActivity.class));
		intentContext.startActivity(new Intent(intentContext, PaymentAllProject.class).putExtra(ConstantGloble.COM_FROM_MAIN, true)
			.putExtra("qentry","0"));
	}

	private boolean isLogin() {
		boolean isLogin = BaseDroidApp.getInstanse().isLogin();
		LogGloble.i("必读消息", "从"+ActivitySwitcher.class.getSimpleName()+"类isLogin()"+",isLogin="+isLogin);
		if (!isLogin) {
			// Intent intent = new Intent(activity, LoginActivity.class);
			// activity.startActivityForResult(intent,
			// ConstantGloble.ACTIVITY_RESULT_CODE);
			new LoginTask(intentContext).exe(new LoginCallback() {
				@Override
				public void loginStatua(boolean isLogin) {
					if (isLogin) {
						openModule(itemName, isFast);
						LogGloble.i("必读消息", "从"+ActivitySwitcher.class.getSimpleName()+"类isLogin()请求必读消息");
					}
				}
			});
			BaseDroidApp.getInstanse().setMainItemAutoClick(true);
		}
		return isLogin;
	}

	// --------------------------------------------------------------------------------
	// ATM无卡取款 跳转
	/** 请求所有借记卡账户列表信息 */
	private void requestAccBankAccountList() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.QRY_ACC_LIST_API);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		List<String> paramslist = new ArrayList<String>();
		// 传递能作为转出账户的列表
		paramslist.add(ConstantGloble.ACC_TYPE_BRO);
		paramslist.add(ConstantGloble.ACC_TYPE_ORD);
		paramslist.add(ConstantGloble.ACC_TYPE_RAN);
		paramsmap.put(Acc.ACC_ACCOUNTTYPE_REQ, paramslist);
		biiRequestBody.setParams(paramsmap);
		// 通讯开始,展示通讯框
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"accBankAccountListCallBack");
	}

	/**
	 * 请求所有借记卡账户列表回调
	 * 
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void accBankAccountListCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> bankAccountList = (List<Map<String, Object>>) (biiResponseBody
				.getResult());
		BaseHttpEngine.dissMissProgressDialog();
		if (bankAccountList == null || bankAccountList.size() == 0) {
			BaseDroidApp.getInstanse().showMessageDialog(
					intentContext.getString(R.string.trans_no_atm_choose),
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							BaseDroidApp.getInstanse().dismissErrorDialog();
						}
					});
			return;
		}
		TranDataCenter.getInstance().setAccountList(bankAccountList);
		Intent intent = new Intent(intentContext, AtmRemitChooseActivity.class);
		intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
		intent.putExtra(ConstantGloble.COMEFROMFOOTFAST, this.isFast);
		intentContext.startActivity(intent);
	}

	// --------------------------------------------------------------------------------
	// 手机取款跳转
	/**
	 * @Title: requestPsnMobileIsSignedAgent
	 * @Description: 请求“代理点签约判断”接口
	 * @param
	 * @return void
	 */
	private void requestPsnMobileIsSignedAgent() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(DrawMoney.PSN_MOBILE_IS_SIGNED_AGENT);
		Map<String, Object> map = new HashMap<String, Object>();
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnMobileIsSignedAgentCallback");
	}

	/**
	 * @Title: requestPsnMobileIsSignedAgentCallback
	 * @Description: 请求“代理点签约判断”接口的回调
	 * @param @param resultObj
	 * @return void
	 */
	public void requestPsnMobileIsSignedAgentCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		String flag = (String) result.get(DrawMoney.FLAG);
		if (flag.equals("true")) {
			isSigned = true;
		} else {
			isSigned = false;
		}
		DrawMoneyData.getInstance().clearDrawMoneyData();
		Intent intent = new Intent();
		intent.putExtra(ConstantGloble.COM_FROM_MAIN, true);
		intent.putExtra(ConstantGloble.COMEFROMFOOTFAST, this.isFast);
		// intent.putExtra(DrawBaseActivity.IsSigned, Boolean.valueOf(flag));
		intent.setClass(intentContext, RemitCardListActivity.class);
		intentContext.startActivity(intent);
	}

	// /////////////////////////////////////////////////////////////////////////
	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
//		if(activity == null)
//			return false;
//		return activity.httpRequestCallBackPre(resultObj);
//		
		

		boolean stopFlag = false;
		if (resultObj instanceof BiiResponse) {
			// Bii请求前拦截
			stopFlag = doBiihttpRequestCallBackPre((BiiResponse) resultObj);
		} else if (resultObj instanceof String) {

		} else if (resultObj instanceof Map) {

		} else if (resultObj instanceof Bitmap) {

		} else if (resultObj instanceof File) {

		} else {
			// do nothing
		}

		return stopFlag;
	
	}

	@Override
	public boolean httpRequestCallBackAfter(Object resultObj) {
//		if(activity == null)
//			return false;
//		return activity.httpRequestCallBackAfter(resultObj);

		boolean stopFlag = false;

		if (resultObj instanceof BiiResponse) {
			// Bii请求后拦截
			stopFlag = doBiihttpRequestCallBackAfter((BiiResponse) resultObj);

		} else if (resultObj instanceof String) {

		} else if (resultObj instanceof Map) {

		} else if (resultObj instanceof Bitmap) {

		} else if (resultObj instanceof File) {

		} else {
			// do nothing
		}

		return stopFlag;
	
	}

	public boolean doBiihttpRequestCallBackAfter(BiiResponse resultObj) {
		return false;
	}
	@Override
	public boolean httpCodeErrorCallBackPre(String code) {
//		if(activity == null)
//			return false;
//		return activity.httpCodeErrorCallBackPre(code);
		return false;
	}

	@Override
	public boolean httpCodeErrorCallBackAfter(String code) {
//		if(activity == null)
//			return false;
//		return activity.httpCodeErrorCallBackAfter(code);
		return false;
	}

	@Override
	public void commonHttpErrorCallBack(final String requestMethod) {
//		if(activity == null)
//			return;
//		activity.commonHttpErrorCallBack(requestMethod);

		if (Login.LOGOUT_API.equals(requestMethod)) {// 退出的请求 不做任何处理
			return;
		}
		if (Push.PNS001.equals(requestMethod)) {// PSN001请求 不做任何处理
			return;
		}

		String message = intentContext.getResources().getString(R.string.communication_fail);
		LogGloble.e(TAG, "请求失败的接口名称" + requestMethod);
		ShowDialogTools.Instance.showMessageDialog(message,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						ShowDialogTools.Instance.dismissErrorDialog();
						if (BaseHttpManager.Instance.getcanGoBack()
								|| BaseLocalData.queryCardMethod
										.contains(requestMethod)) {
							intentContext.finish();
							BaseHttpManager.Instance.setCanGoBack(false);
						}

					}
				});
		
	
	}

	@Override
	public void commonHttpResponseNullCallBack(String requestMethod) {
//		if(activity == null)
//			return ;
//		activity.commonHttpResponseNullCallBack(requestMethod);
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {// 覆盖handleMessage方法
			Toast.makeText(intentContext, "中银证券开户下载失败，请重新下载", Toast.LENGTH_LONG)
					.show();
		}
	};
	/**
	 * Bii请求前拦截-可处理统一的错误弹出框 有返回数据（response）<br>
	 * 对于包含exception 的业务错误提示数据进行统一弹框提示<br>
	 * 
	 * 屏蔽错误码不要再覆盖这个方法了！！！！！！！！！<br>
	 * 只需要子Activity把需要屏蔽的错误码add到listShieldErrorCode这个列表里，这个列表已经实例化了一个空列表，
	 * 不需要子Activity实例化<br>
	 * 然后再覆盖doShieldErrorCode这个方法做拦截后的处理！！！！！！！！！！！！！
	 * 
	 * @paramBiiResponse
	 *   resultObj
	 * @return 是否终止业务流程    true:终止流程   ，false :流程继续
	 */
	public boolean doBiihttpRequestCallBackPre(BiiResponse response) {
		List<BiiResponseBody> biiResponseBodyList = response.getResponse();

		if(StringUtil.isNullOrEmpty(biiResponseBodyList))
			return false;
		for (BiiResponseBody body : biiResponseBodyList) {
			if(ConstantGloble.STATUS_SUCCESS.equals(body.getStatus()))
				continue;
				// 消除通信框
			ShowDialogTools.Instance.dissMissProgressDialog();
			if (Login.LOGOUT_API.equals(body.getMethod())) {// 退出的请求
				return false;
			}
			if (Login.OUTLAY_CLOSECONVERSATION_ID_API.equals(body
					.getMethod())
					|| Tran.PSNGETTICKETFORMESSAGE.equals(body
							.getMethod())
					|| Tran.INSERTTRANSEQ.equals(body.getMethod())) {// 关闭回话
				return false;
			}
			BiiError biiError = body.getError();
			// 判断是否存在error
			if(biiError == null) {

				ShowDialogTools.Instance.dismissErrorDialog();
				// 避免没有错误信息返回时给个默认的提示

				ShowDialogTools.Instance.createDialog(
						"",
						intentContext.getResources()
								.getString(R.string.request_error),
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								ShowDialogTools.Instance.dismissErrorDialog();
								if (BaseHttpManager.Instance.getcanGoBack()) {
									intentContext.finish();
									BaseHttpManager.Instance.setCanGoBack(false) ;
								}
							}
						});
				return true;
			}
			if(biiError.getCode() == null) {
				// 弹出公共的错误框
				showHttpErrorDialog("",biiError.getMessage());			
				return true;
			}
			// 过滤错误

			BaseLocalData.Code_Error_Message.errorToMessage(body);
			if (BaseLocalData.timeOutCode.contains(biiError.getCode())/*LocalData.timeOutCode.contains(biiError.getCode())*/) {// 表示回话超时,要重新登录
				showTimeOutDialog(biiError.getMessage());
			//	HttpHandlerTools.a();
				return true;
			} 
//			// 非会话超时错误拦截
//			if(httpTools != null && httpTools.IsInterceptErrorCode(body.getMethod(),biiError.getCode())) {  // 需要拦截错误码
////				doShieldErrorCode(biiError.getCode());
//				return doHttpErrorHandler(body.getMethod(),biiError);
//			}
//			
			
			showHttpErrorDialog(biiError.getCode(),biiError.getMessage());
			
			return true;
		}
		return false;
	}
	/**
	 * 网络异常时弹出错误提示框
	 */
	protected void showHttpErrorDialog(String errorCode, String message) {
		ShowDialogTools.Instance.dismissErrorDialog();
		ShowDialogTools.Instance.createDialog(errorCode,message, new OnClickListener() {
			@Override
			public void onClick(View v) {
				ShowDialogTools.Instance.dismissErrorDialog();
				if (BaseHttpManager.Instance.getcanGoBack()) {
					((Activity) intentContext).finish();
					BaseHttpManager.Instance.setCanGoBack(false);
				}
			}
		});
	}
	/*	
	 * 会话超时 提示信息
	 * 
	 * */
		protected void showTimeOutDialog(String message) {

			
			ShowDialogTools.Instance.showMessageDialog(message, new OnClickListener() {
				@Override
				public void onClick(View v) {
					ShowDialogTools.Instance.dismissErrorDialog();
					ActivityTaskManager.getInstance().removeAllActivity();
					ActivityTaskManager.getInstance().removeAllSecondActivity();;
//					Intent intent = new Intent();
////					intent.setClass(BaseActivity.this, LoginActivity.class);
//					intent.setClassName(intentContext,"com.chinamworld.bocmbci.biz.login.LoginActivity");
//					intentContext.startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
					AbstractLoginTool.Instance.Login(intentContext, new LoginTask.LoginCallback(){
						@Override
						public void loginStatua(boolean isLogin) {
						}
					});

				}
			});
		}

	@Override
	public void goToMainActivity() {

		//		Class<?> classType = null;
//		String className=null;
//		if(getActivityTaskType() == ActivityTaskType.OneTask)
//		{
//			ActivityTaskManager.getInstance().removeAllActivity();
//			className="com.chinamworld.bocmbci.biz.MainActivity";
//		}
//		else if(getActivityTaskType() == ActivityTaskType.TwoTask){
//			ActivityTaskManager.getInstance().removeAllSecondActivity();
//
//			className="com.chinamworld.bocmbci.biz.SecondMainActivity";
//		}
//

//		ActivityIntentTools.intentToActivityByClassName(BaseActivity.this, className);
		ActivityTaskManager.getInstance().removeAllActivity();
		ActivityTaskManager.getInstance().removeAllSecondActivity();;
		intentContext.finish();
		new BocCommonTools().toHomePage();
	}

	@Override
	public void goToHomePage() {
		new BocCommonTools().toHomePage();
	}

}
