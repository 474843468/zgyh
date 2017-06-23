package com.chinamworld.bocmbci.biz.sbremit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.widget.Button;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.SBRemit;
import com.chinamworld.bocmbci.biz.sbremit.histrade.HistoryTradeQueryActivity;
import com.chinamworld.bocmbci.biz.sbremit.mysbremit.ChooseAccountActivity;
import com.chinamworld.bocmbci.biz.sbremit.rate.SBRemitRateInfoOutlayActivity;
import com.chinamworld.bocmbci.constant.CheckRegExp;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

/**
 * 结售汇基类
 * 
 * @author fsm
 * 
 */
public class SBRemitBaseActivity extends BaseActivity {

	public static final String TAG = "SBRemitBaseActivity";
	/** 头部返回按钮 */
	protected Button back;
	/** 右上角按钮 */
	protected Button btn_right;
	/** 右上角按钮事件 */
	protected OnClickListener btnRightClickListener;
	/** 加载业务布局 */
	public LinearLayout tabcontent = null;
	/** 进入动画 */
	protected Animation animation_down;
	/** 退出动画 */
	protected Animation animation_up;
	/** 账户列表 */
	protected List<Map<String, Object>> accList;

	/** 查询账户列表 */
	protected Map<String, Object> resultAccMap;
	/** 最大金额试算集合 */
	protected Map<String, Object> trymap;
	private Object commConversationId;

	/** 获取所有中国银行账户返回标识 */
	protected static final int GET_ACCOUNT_IN_CALLBACK = 51;
	/** 初始化额度信息 */
	protected static final int INIT_AMOUNT_INFO_CALLBACK = 1;
	/** 下一步 */
	protected static final int NEXT_STEP_CALLBACK = 2;
	/** 交易详情 */
	protected static final int QUERY_TRADE_DETAIL = 3;
	/** 结汇操作成功标志 */
	protected static final int SREMIT_OPERATION = 4;
	/** 购汇操作成功标志 */
	protected static final int BREMIT_OPERATION = 5;
	/** 查询历史交易 */
	protected static final int QUERY_HISTORY_TRADE = 6;
	/** 查询历史交易详情 */
	protected static final int QUERY_HISTORY_TRADE_DETAIL = 7;
	/** 购汇操作现汇代号 */
	protected static final String CUR_REMIT = "02";
	/** 结汇标志 */
	public static final String S_REMIT = "01";
	/** 购汇标志 */
	public static final String B_REMIT = "02";
	/** 结汇标志 */
	public static final String S_REMIT_NEW = "S";
	/** 购汇标志 */
	public static final String B_REMIT_NEW = "B";
	/** 中国代号 */
	public static final String CHINA = "CHN";
	/** 年已用年剩余默认单位 */
	public static String MEIYUAN = "美元";
	public static String DENGZHI = "等值";// "等值";
	/** 黑色十六进制 */
	public static final String COLOR_BLACK = "#000000";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);
		// 初始化弹窗按钮
		initPulldownBtn();
		// 初始化左边菜单
		initLeftSideList(this, LocalData.sbRemitLeftList);
		// 初始化底部菜单栏
		initFootMenu();
		back = (Button) findViewById(R.id.ib_back);
		btn_right = (Button) findViewById(R.id.ib_top_right_btn);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		initanimation();
		initDataCenter();
	}

	@Override
	protected boolean selectedMenuItemHandler(Activity context, ImageAndText menuItem) {
		super.selectedMenuItemHandler(context, menuItem);
		ActivityTaskManager.getInstance().removeAllActivity();
		String menuId = menuItem.MenuID;
		if(menuId.equals("sbRemit_1")){
			// 我的账户
			Intent intent1 = new Intent(this, ChooseAccountActivity.class);
			context.startActivity(intent1);
		}
		else if(menuId.equals("sbRemit_2")){
			// 历史交易查询
			Intent intent2 = new Intent(this, HistoryTradeQueryActivity.class);
			context.startActivity(intent2);
		}
		else if(menuId.equals("sbRemit_3")){
			// 参考汇率查询
			Intent intent3 = new Intent(this,
					SBRemitRateInfoOutlayActivity.class);
			context.startActivity(intent3);
		}
		return true;
//		ActivityTaskManager.getInstance().removeAllActivity();
//		switch (clickIndex) {
//		case 0:
//			// 我的账户
//			Intent intent1 = new Intent(this, ChooseAccountActivity.class);
//			startActivity(intent1);
//			break;
//		case 1:
//			// 历史交易查询
//			Intent intent2 = new Intent(this, HistoryTradeQueryActivity.class);
//			startActivity(intent2);
//			break;
//		case 2:
//			// 参考汇率查询
//			Intent intent3 = new Intent(this,
//					SBRemitRateInfoOutlayActivity.class);
//			startActivity(intent3);
//			break;
//		default:
//			break;
//		}
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

	/**
	 * 设置右侧按钮文字
	 * 
	 * @param title
	 */
	public void setRightBtnText(String title) {
		btn_right.setVisibility(View.VISIBLE);
		btn_right.setText(title);
		btn_right.setTextColor(Color.WHITE);
		btn_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (btnRightClickListener != null) {
					btnRightClickListener.onClick(v);
				}
			}
		});
	}

	/**
	 * 是否需要调用可结售汇金额上限试算接口。美元不需要调用此接口
	 * 
	 * @return false:为美元，不需要调用试算接口
	 */
	public boolean isRequestbiggesttry(String fessFlag, String cashRemit,
			String availableBalanceRMB, String currencyCode, String annRmeAmtUSD) {
		if ("014".equals(currencyCode) || "CAD".equals(currencyCode)) {
			Map<String, Object> trymap = new HashMap<String, Object>(); // 直接保存美元的值
			trymap.put("availableBalanceCUR", availableBalanceRMB);
			trymap.put("annRmeAmtCUR", annRmeAmtUSD);
			SBRemitDataCenter.getInstance().setTryMap(trymap);
			return false;
		}
//		requestbiggesttry(fessFlag, cashRemit, availableBalanceRMB,
//				currencyCode, annRmeAmtUSD);
		return true;
	}

	/**
	 * @Title: requestbiggesttry
	 * @Description: 请求可结售汇金额上限试算
	 * @param
	 * @return void
	 */
	public void requestbiggesttry(String fessFlag, String cashRemit,
			String availableBalanceRMB, String currencyCode, String annRmeAmtUSD) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(SBRemit.BIGGEST_TRY);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		//后台可能返回3位小数需要格式化两位
		String availableBalanceRMB_new=StringUtil.append2Decimals(availableBalanceRMB, 2);
		paramsmap.put(SBRemit.FESS_FLAG, fessFlag);
		paramsmap.put(SBRemit.CASH_REMIT, cashRemit);
		paramsmap.put(SBRemit.AVAILABLE_BALANCERMB, availableBalanceRMB_new);
		paramsmap.put(SBRemit.CURRENCY_CODE, currencyCode);
		paramsmap.put(SBRemit.ANNRMEAMTUSD, annRmeAmtUSD);
		biiRequestBody.setParams(paramsmap);

		HttpManager.requestBii(biiRequestBody, this,
				"requestbiggesttryCallBack");
	}

	/**
	 * @Title: requestForCardListCallBack
	 * @Description: 请求最大金额试算返回
	 * @param @param resultObj
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void requestbiggesttryCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		trymap = (Map<String, Object>) biiResponseBody.getResult();
		SBRemitDataCenter.getInstance().setTryMap(trymap);
		if (!StringUtil.isNullOrEmpty(trymap)) {
			LogGloble.d("fsm", "----" + trymap.toString());
			LogGloble.d("fsm", "----+"
					+ SBRemitDataCenter.getInstance().getTryMap().toString());
			// communicationCallBack(GET_ACCOUNT_IN_CALLBACK);
		} else {
			LogGloble.d("fsm", "《《《《" + trymap.toString());
		}
	}

	/**
	 * @Title: requestForCardList
	 * @Description: 请求中行所有账户列表 活一本 借记卡
	 * @param
	 * @return void
	 */
	public void requestForCardList() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(SBRemit.SBREMIT_ACC_LIST);
		// 加上这句话意思是公用同一个conversationID
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this,
				"requestForCardListCallBack");
	}

	/**
	 * @Title: requestForCardListCallBack
	 * @Description: 请求中行所有账户列表返回
	 * @param @param resultObj
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void requestForCardListCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		resultAccMap = (Map<String, Object>) biiResponseBody.getResult();
		LogGloble.e("asd", "resultAccMap" + resultAccMap);
		accList = (List<Map<String, Object>>) resultAccMap
				.get(SBRemit.ACC_LIST);
		LogGloble.e("asd", "accList" + accList);
		if (!StringUtil.isNullOrEmpty(accList)) {
			LogGloble.d("fsm", accList.toString());
			communicationCallBack(GET_ACCOUNT_IN_CALLBACK);
		} else {
			BaseDroidApp.getInstanse().showMessageDialog(
					"因您名下的关联账户中无可用的活一本或主账户是活一本的借记卡,故无法使用该功能服务。",
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							// finish();
						}
					});
		}
	}

	/**
	 * @Title: communicationCallBack
	 * @Description: 通讯回调 需要子类进行覆盖
	 * @param @param flag
	 * @return void
	 */
	public void communicationCallBack(int flag) {
		// 子类进行覆盖
	}

	@SuppressWarnings("static-access")
	private void initanimation() {
		animation_up = new AnimationUtils().loadAnimation(this,
				R.anim.scale_out);
		animation_down = new AnimationUtils().loadAnimation(this,
				R.anim.scale_in);
	}

	/**
	 * 初始化数据中心
	 */
	@SuppressWarnings("static-access")
	private void initDataCenter() {
		SBRemitDataCenter.getInstance().setCapitalUse(capitalUse);
		SBRemitDataCenter.getInstance().setMoneyType(moneyType);
		SBRemitDataCenter.getInstance().setTransStatus(transStatus);
		SBRemitDataCenter.getInstance().setSbRemit(sbRemitValueKey_New);
		SBRemitDataCenter.getInstance().setRmbCode(rmbCode);
	}

	/**
	 * 判断字符串是否空串
	 * 
	 * @param 原字符串
	 * @return 不为空原串返回，为空则返回-
	 */
	protected String strIsNull(String str) {
		return (str != null && !str.equals("") ? str : "-");
	}

	/**
	 * 判断Obj是否为空
	 * 
	 * @param 原obj
	 * @return 不为空原串返回，为空则返回-
	 */
	protected String isObjNull(Object obj) {
		return (obj != null ? strIsNull(obj.toString()) : "");
	}

	/**
	 * 判断Obj是否为空
	 * 
	 * @param obj
	 *            原串
	 * @param str
	 *            默认串
	 * @return 为空则返回默认字符串，不为空则返回原字符串
	 */
	protected String isObjNull(Object obj, String str) {
		return (obj != null ? strIsNull(obj.toString()) : str);
	}

	/**
	 * 判斷兩個字符串是否一样
	 * 
	 * @param orig
	 *            要判断的字符串
	 * @param newStr
	 *            目的串
	 * @return true:一样，false:不一样
	 */
	protected boolean isStrEquals(String orig, String newStr) {
		if (orig == null || newStr == null || orig.equals("")
				|| newStr.equals(""))
			return false;
		if (!orig.equals(newStr))
			return false;
		return true;
	}

	/**
	 * 设置文字颜色,颜色为空时默认设置黑色，
	 * 
	 * @param text
	 *            文本
	 * @param color
	 *            目标色
	 * @param isMoney
	 *            判断是否是金额
	 * @return 加了颜色的文本，设置文本的时候调用 Html.fromHtml()
	 */
	protected String colorText(String text, String color, boolean isMoney) {
		if (text == null || text.equals(""))
			return "";
		if (isMoney) {
			String str = StringUtil.parseStringPattern(text,
					SBRemitDataCenter.getInstance().CASH_PATTERN_PARAM);
			if (color == null || color.equals("")) {
				return "<font color=\'#000000\'>" + str + "</font>";
			}
			return "<font color=\'" + color + "\'>" + str + "</font>";
		} else {
			return "<font color=\'#000000\'>" + text + "</font>";
		}

	}

	/**
	 * 根据币种 和炒汇 判断显示 如果不是人民币 显示币种和炒汇
	 * 
	 * @param currencyCode
	 *            币种
	 * @param cashFlagCode
	 *            炒汇
	 * @return
	 */
	public static String fincCurrencyAndCashFlag(String currencyCode,
			String cashFlagCode) {
		if (currencyCode == null) {
			return "-";
		}
		if (ConstantGloble.PRMS_CODE_RMB.equals(currencyCode)) {
			return LocalData.Currency.get(currencyCode);
		} else {
			String currency = LocalData.CurrencyCashremit.get(cashFlagCode);
			return LocalData.Currency.get(currencyCode)
					+ (currency != null && !currency.equals("-") ? "/"
							+ currency : "");
		}
	}

	/** 资金用途 */
	public Map<String, String> capitalUse = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("3222", "因私旅游");
			put("3221", "自费出境学习");
			put("3223", "公务及商务出国");
			put("3225", "旅游项下其他");
			put("331", "职工报酬和赡家款");
			put("323", "金融和保险服务");
			put("324", "专有权利使用费和特许费");
			put("325", "咨询服务");
			put("326", "其他服务");
			put("310", "货物贸易");

			put("321", "运输");
			put("332", "投资收益");
			put("333", "其他经常转移");
			put("410", "资本账户");
			put("424", "投资资本金");
			put("42A", "其他直接投资");
			put("431", "对境外证券投资");
			put("440", "其他投资");
			put("450", "国内外汇贷款");
			put("470", "经批准的资本其他");

			// put("01", "境外旅游");
			// put("02", "自费出境学习");
			// put("04", "探亲");
			// put("05", "商务考察");
			// put("06", "朝觐");
			// put("07", "出境定居");
			// put("08", "境外就医");
			// put("09", "被聘工作");
			// put("10", "境外邮购");
			// put("11", "缴纳国际组织会员费");
			// put("12", "境外直系亲属救助");
			// put("13", "国际交流");
			// put("14", "境外培训");
			// put("16", "外派劳务");
			// put("17", "外汇理财");
			// // put("18", "货物贸易及相关费用");
			// put("19", "境外咨询");
			// // put("20", "其他服务贸易费用");
			// // put("24", "补购外汇");
			// // put("25", "境外个人经常项目收入购汇");
			// // put("26", "境外个人原币兑回");
			// put("99", "其他");
		}
	};

	/** 境内个人购汇资金用途显示 */
	public Map<String, String> capitalUseValueKey = new HashMap<String, String>() {
		/**
		 * 王东凯 2015年11月21日15:20:00
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{

			put("请选择", "-1");
			put("因私旅游", "3222");
			put("自费出境学习", "3221");
			put("公务及商务出国", "3223");
			put("旅游项下其他", "3225");
			put("职工报酬和赡家款", "331");
			put("金融和保险服务", "323");
			put("专有权利使用费和特许费", "324");
			put("咨询服务", "325");
			put("其他服务", "326");
			put("货物贸易", "310");
			put("运输", "321");
			put("投资收益", "332");
			put("其他经常转移", "333");
			put("资本账户", "410");
			put("投资资本金", "421");
			put("其他直接投资", "424");
			put("对境外证券投资", "431");
			put("其他投资", "440");
			put("国内外汇贷款", "450");
			put("经批准的资本其他", "470");
			// put("请选择", "00");
			// put("境外旅游", "01");
			// put("自费出境学习", "02");
			// put("探亲", "04");
			// put("商务考察", "05");
			// put("朝觐", "06");
			// put("出境定居", "07");
			// put("境外就医", "08");
			// put("被聘工作", "09");
			// put("境外邮购", "10");
			// put("缴纳国际组织会员费", "11");
			// put("境外直系亲属救助", "12");
			// put("国际交流", "13");
			// put("境外培训", "14");
			// put("外派劳务", "16");
			// put("外汇理财", "17");
			// // put("货物贸易及相关费用", "18");
			// put("境外咨询", "19");
			// // put("其他服务贸易费用", "20");
			// // put("补购外汇", "24");
			// // put("境外个人经常项目收入购汇", "25");
			// // put("境外个人原币兑回", "26");
			// put("其他", "99");
		}
	};

	/** 境内个人购汇资金用途 */
	public ArrayList<String> capitalUseList = new ArrayList<String>() {
		/**
		 * 王东凯2015年11月21日15:19:09
		 */
		private static final long serialVersionUID = 1L;

		{

			add("请选择");
			add("因私旅游");
			add("自费出境学习");
			add("公务及商务出国");
			add("旅游项下其他");
			add("职工报酬和赡家款");
			add("金融和保险服务");
			add("专有权利使用费和特许费");
			add("咨询服务");
			add("其他服务");
//			add("货物贸易");
			add("运输");
			add("投资收益");
			add("其他经常转移");
//			add("资本账户");
//			add("投资资本金");
//			add("其他直接投资");
//			add("对境外证券投资");
//			add("其他投资");
//			add("国内外汇贷款");
//			add("经批准的资本其他");
			// add("请选择");
			// add("境外旅游");
			// add("自费出境学习");
			// add("探亲");
			// add("商务考察");
			// add("朝觐");
			// add("出境定居");
			// add("境外就医");
			// add("被聘工作");
			// add("境外邮购");
			// add("缴纳国际组织会员费");
			// add("境外直系亲属救助");
			// add("国际交流");
			// add("境外培训");
			// add("外派劳务");
			// add("外汇理财");
			// // add("货物贸易及相关费用");
			// add("境外咨询");
			// // add("其他服务贸易费用");
			// // add("补购外汇");
			// // add("境外个人经常项目收入购汇");
			// // add("境外个人原币兑回");
			// add("其他");
		}
	};
	/** 境内个人结汇资金用途 */
	public ArrayList<String> capitalUseListIn = new ArrayList<String>() {
		/**
		 * 王东凯 2015年11月21日15:31:29
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("请选择");
			add("职工报酬和赡家款");
			add("旅游");
			add("金融和保险服务");
			add("专有权利使用费和特许费");
			add("咨询服务");
			add("其他服务");
			add("投资收益");
			add("其他经常转移");
//			add("货物贸易");
			add("运输");
//			add("资本账户");
//			add("直接投资撤资");
//			add("其他直接投资");
//			add("对境外证券投资撤回");
//			add("证券筹资");
//			add("其他投资");
//			add("国内外汇贷款");
//			add("经批准的资本其他");

		}
	};
	/** 境内个人结汇资金用途显示 */
	public Map<String, String> capitalUseValueKeyIn = new HashMap<String, String>() {
		/**
		 * 王东凯 2015年11月21日15:42:23
		 */
		private static final long serialVersionUID = 1L;

		{

			put("请选择", "-1");
			put("旅游", "122");
			put("自费出境学习", "3221");
			put("公务及商务出国", "3223");
			put("旅游项下其他", "3225");
			put("职工报酬和赡家款", "131");
			put("金融和保险服务", "123");
			put("专有权利使用费和特许费", "124");
			put("咨询服务", "125");
			put("其他服务", "126");
			put("货物贸易", "110");
			put("运输", "121");
			put("投资收益", "132");
			put("其他经常转移", "133");
			put("资本账户", "210");
			put("投资资本金", "421");
			put("其他直接投资", "224");
			put("直接投资撤资", "222");
			put("对境外证券投资撤回", "231");
			put("其他投资", "240");
			put("国内外汇贷款", "250");
			put("经批准的资本其他", "270");
			put("证券筹资", "232");
		}
	};
	/** 境外个人结汇资金用途 */
	public ArrayList<String> capitalUseListOut = new ArrayList<String>() {
		/**
		 * 王东凯 2015年11月21日15:31:29
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("请选择");
//			add("职工报酬和赡家款");
			add("旅游");
			add("金融和保险服务");
			add("专有权利使用费和特许费");
			add("咨询服务");
			add("其他服务");
			add("投资收益");
			add("其他经常转移");
//			add("货物贸易");
			add("运输");
//			add("资本账户");
//			add("投资资本金");
//			add("房地产");
//			add("其他直接投资");
//			add("证券筹资");
//			add("其他投资");
//			add("国内外汇贷款");
//			add("经批准的资本其他");
		}
	};
	/** 境外个人结汇资金用途显示 */
	public Map<String, String> capitalUseValueKeyOut = new HashMap<String, String>() {
		/**
		 * 王东凯 2015年11月21日15:42:23
		 */
		private static final long serialVersionUID = 1L;

		{

			put("请选择", "-1");
			put("旅游", "122");
			put("职工报酬和赡家款", "131");
			put("金融和保险服务", "123");
			put("专有权利使用费和特许费", "124");
			put("咨询服务", "125");
			put("其他服务", "126");
			put("货物贸易", "110");
			put("运输", "121");
			put("投资收益", "132");
			put("其他经常转移", "133");
			put("资本账户", "210");
			put("投资资本金", "221");
			put("其他直接投资", "224");
			put("其他投资", "240");
			put("房地产", "223");
			put("国内外汇贷款", "250");
			put("经批准的资本其他", "270");
			put("证券筹资", "232");
		}
	};
	/** 资金来源 */
	public Map<String, String> capitalResource = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("110", "货物贸易");
			put("121", "运输");
			put("122", "旅游");
			put("123", "金融和保险服务");
			put("124", "专有权利使用费和特许费");
			put("125", "咨询服务");
			put("126", "其他服务");
			put("131", "职工报酬和赡家款");
			put("132", "投资收益");
			put("133", "其他经常转移");
			put("210", "资本账户");
			put("22A", "其他直接投资");
			put("222", "直接投资撤资");
			put("231", "对境外证券投资撤回");
			put("232", "证券筹资");
			put("24A", "其他投资");
			put("250", "国内外汇贷款");
			put("270", "经批准的资本其他");
			put("223", "房地产");
			put("221", "投资资本金");

		}
	};

	/** 资金来源 */
	public Map<String, String> capitalResourceValueKey = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("请选择", "-1");
			put("职工报酬和赡家款", "131");
			put("旅游", "122");
			put("金融和保险服务", "123");
			put("专有权利使用费和特许费", "124");
			put("咨询服务", "125");
			put("其他服务", "126");
			put("投资收益", "132");
			put("其他经常转移", "133");
			put("货物贸易", "110");
			put("运输", "121");
			put("资本账户", "210");
			put("直接投资撤资", "222");
			put("其他直接投资", "22A");
			put("对境外证券投资撤回", "231");
			put("证券筹资", "232");
			put("其他投资", "24A");
			put("国内外汇贷款", "250");
			put("经批准的资本其他", "270");

			// put("职工报酬和赡家费", "131");
			// put("旅游", "122");
			// put("投资收益", "132");
			// put("金融和保险服务", "123");
			// put("咨询服务", "125");
			// put("专有权利使用费和特许费", "124");
			// put("运输", "121");
			// put("其他服务", "126");
			// put("其他经常转移", "133");
		}
	};
	/** 境内个人结汇资金来源 */
	public ArrayList<String> capitalResourceList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("请选择");
			add("职工报酬和赡家款");
			add("旅游");
			add("金融和保险服务");
			add("专有权利使用费和特许费");
			add("咨询服务");
			add("其他服务");
			add("投资收益");
			add("其他经常转移");
			add("货物贸易");
			add("运输");
			add("资本账户");
			add("直接投资撤资");
			add("其他直接投资");
			add("对境外证券投资撤回");
			add("证券筹资");
			add("其他投资");
			add("国内外汇贷款");
			add("经批准的资本其他");

			// add("请选择");
			// add("职工报酬和赡家费");
			// add("旅游");
			// add("投资收益");
			// add("金融和保险服务");
			// add("咨询服务");
			// add("专有权利使用费和特许费");
			// add("运输");
			// add("其他服务");
			// add("其他经常转移");
		}
	};

	/** 人民币 */
	public Map<String, String> rmbCode = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("001", "人民币元");
			put("CNY", "人民币元");
		}
	};

	/** 币种 */
	public ArrayList<String> moneyType = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("012");// , "英镑");
			add("GBP");// , "英镑");
			add("014");// , "美元");
			add("USD");// , "美元");
			add("013");// , "港币");
			add("HKD");// , "港币");
			add("015");// , "瑞士法郎");
			add("CHF");// , "瑞士法郎");
			add("018");// , "新加坡元");
			add("SGD");// , "新加坡元");
			add("027");// , "日元");
			add("JPY");// , "日元");
			add("028");// , "加拿大元");
			add("CAD");// , "加拿大元");
			add("029");// , "澳大利亚元");
			add("AUD");// , "澳大利亚元");
			add("038");// , "欧元");
			add("EUR");// , "欧元");
			// P502 ADD
			add("023");// , "挪威克朗");
			add("NOK");// , "挪威克朗");
			add("021");// , "瑞典克朗");
			add("SEK");// , "瑞典克朗");
			add("022");// , "丹麦克朗");
			add("DKK");// , "丹麦克朗");
			add("087");// , "新西兰元");
			add("NZD");// , "新西兰元");
			// 601 新增币种
			add("081");// , "澳门元");
			add("MOP");// , "澳门元");
			add("088");// , "韩国元");
			add("KRW");// , "韩国元");
			add("196");// , "俄罗斯卢布");
			add("RUB");// , "俄罗斯卢布");
			add("070");// , "南非兰特");
			add("ZAR");// , "南非兰特");
			add("084");// , "泰国铢");
			add("THB");// , "泰国铢");
			add("082");// , "菲律宾比索");
			add("PHP");// , "菲律宾比索");
			add("032");// , "马来西亚林吉特");
			add("MYR");// , "马来西亚林吉特");
			add("056");// , "印尼卢比");
			add("IDR");// , "印尼卢比");
			add("134");// , "巴西里亚尔");
			add("BRL");// , "巴西里亚尔");
			add("213");// , "新台币");
			add("TWD");// , "新台币");
			add("085");// , "印度卢比");
			add("INR");// , "印度卢比");
			add("096");// , "阿联酋迪拉姆");
			add("AED");// , "阿联酋迪拉姆");
		}
	};

	/** 币种值 key对照 */
	public Map<String, String> moneyTypeValueKey = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			// put("美元", "014");//, );
			// put("欧元", "038");//, );
			// put("英镑", "012");//, );
			// put("港元", "013");//, );
			// put("瑞士法郎", "015");//, );
			// put("新加坡元", "018");//, );
			// put("日元", "027");//, );
			// put("加拿大元", "028");//, );
			// put("澳大利亚元", "029");//, );
			// // P502 ADD
			// put("挪威克朗", "023");//, );
			// put("瑞典克朗", "021");//, );
			// put("丹麦克朗", "022");//, );
			// put("新西兰元", "087");//, );
			// 601
			put("请选择", "-1");
			put("欧元", "038");// , );
			put("美元", "014");// , );
			put("英镑", "012");// , );
			put("澳大利亚元", "029");// , );
			put("日元", "027");// , );
			put("加拿大元", "028");// , );
			put("港币", "013");// , );
			put("新加坡元", "018");// , );
			put("瑞士法郎", "015");
			put("澳门元", "081");
			put("韩元", "088");
			put("卢布", "196");
			put("挪威克朗", "023");
			put("瑞典克朗", "021");
			put("丹麦克朗", "022");
			put("新西兰元", "087");
			put("南非兰特", "070");
			put("泰国铢", "084");
			put("菲律宾比索", "082");
			put("林吉特", "032");
			put("印尼卢比", "056");
			put("巴西里亚尔", "134");
			put("新台币", "213");
			put("印度卢比", "085");
			put("阿联酋迪拉姆", "096");

		}
	};

	/** 币种List */
	public ArrayList<String> moneyTypeList = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			// add("美元");
			// add("欧元");
			// add("英镑");
			// add("港元");
			// add("瑞士法郎");
			// add("新加坡元");
			// add("日元");
			// add("加拿大元");
			// add("澳大利亚元");
			// // P502 ADD
			// add("挪威克朗");
			// add("瑞典克朗");
			// add("丹麦克朗");
			// add("新西兰元");
			// 603顺序修改
			add("请选择");
			add("美元");
			add("澳大利亚元");
			add("加拿大元");
			add("港币");
			add("英镑");
			add("欧元");
			add("日元");
			add("新西兰元");
			add("新加坡元");
			add("泰国铢");
			add("韩元");
			add("新台币");
			add("瑞士法郎");
			add("瑞典克朗");
			add("丹麦克朗");
			add("卢布");
			add("挪威克朗");
			add("菲律宾比索");
			add("澳门元");
//			add("林吉特");在603屏蔽
			add("印尼卢比");
			add("巴西里亚尔");
			add("阿联酋迪拉姆");
			add("印度卢比");
			add("南非兰特");
		}
	};

	/** 交易状态 */
	public Map<String, String> status = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("A", "交易成功");
			put("B", "交易失败");
			put("K", "银行已受理");
			put("G", "银行处理中");
		}
	};
	/** 交易状态新 */
	public Map<String, String> statusnew = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("00", "交易成功");
			put("01", "交易失败");
			put("02", "交易未明");
		}
	};

	/** 交易渠道新 */
	public Map<String, String> channelnew = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("00", "柜台");
			put("01", "网上银行");
			put("02", "银企对接/网上商城");
			put("03", "短信");
			put("04", "电话银行(自助)");
			put("05", "电话银行(人工)");
			put("06", "传真");
			put("07", "自助终端");
			put("08", "其他");
			put("09", "ATM");
			put("10", "手机银行");
			put("11", "家居银行");
			put("12", "POS机");
			put("13", "柜台");
			put("14", "银行客户端");
			put("15", "柜台");
			put("16", "中银开放平台");
			put("17", "即时通讯(微信)");
			put("18", "网上银行");
			put("99", "其他");

		}
	};
	/** 交易失败原因 */
	public Map<String, String> tranRetCodeNew = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{

			put("0001", "接收前端数据转换失败");
			put("0002", "查询无记录");
			put("0003", "该记录不存在");
			put("0004", "读取配置数据转换失败");
			put("0006", "外管局机构信息未维护，请确认后再试");
			put("0010", "发送外管局超时");
			put("0011", "接收外管局超时");
			put("0012", "发送外管局失败");
			put("0013", "接收外管局失败");
			put("0017", "前置机处理失败");
			put("0018", "不在业务受理时间范围内,请确认后再试");
			put("0019", "时间参数未维护，请确认后再试");
			put("0021", "查询间隔天数未维护，请联系维护人员");
			put("0023", "发送IPS报文失败");
			put("0024", "接收IPS报文失败");
			put("0025", "核心汇率信息查询失败");
			put("0029", "该交易限制使用,请联系业务人员");
			put("00103", "数据库操作失败");
			put("00104", "美元折算率查询失败");
			put("00108", "证件类型或证件号码校验失败,请核对");
			put("00113", "接收渠道失败");
			put("00122", "交易状态不成功，不能查询");
			put("00123", "结售汇详细信息查询失败");
			put("00201", "开始日期应在1自然年内");
			put("00202", "结束日期距开始日期不应大于3个月");
			put("00401", "该证件类型不支持结汇,请确认后再试");
			put("00402", "结汇金额不可小于最低限额");
			put("00403", "最小结汇金额参数未维护,请确认后再试");
			put("00404", "记录结汇交易流水表失败");
			put("00405", "更新结汇交易流水表失败");
			put("00406", "发送核心结汇失败");
			put("00407", "发送核心结汇未明");
			put("00501", "该证件类型不支持售汇，请确认后再试");
			put("00502", "售汇金额不可小于最低限额");
			put("00503", "最小售汇金额参数未维护，请确认后再试");
			put("00504", "记录售汇交易流水表失败");
			put("00505", "更新售汇交易流水表失败");
			put("00506", "发送核心购汇失败");
			put("00507", "发送核心购汇未明");
			put("99998", "外管局交易未明");
			put("99999", "外管局返回失败");

		}
	};

	public final String tradIng = "02";

	/** 交易状态 */
	public Map<String, String> transStatus = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("00", "交易成功");
			put("01", "交易失败");
			put("02", "银行处理中");
		}
	};
	/** 交易状态 */
	public Map<String, String> transStatus_new = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("A", "交易成功");
			put("B", "交易失败");
			put("K", "银行处理中");
		}
	};

	/** 交易状态 */
	public final Map<String, String> sbRemitValueKey_New = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("结汇", "01");
			put("购汇", "11");
		}
	};

	/** 交易状态 */
	public Map<String, String> sbRemitValueKey = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("结汇", "S");
			put("购汇", "B");
		}
	};

	public static String getKeyByValue(Map<String, String> map, String value) {
		if (map == null)
			return null;
		for (String key : map.keySet()) {
			if (value == null && map.get(key) == null)
				return key;
			if (value == null)
				continue;
			if (value.equals(map.get(key)))
				return key;
		}

		return null;
	}

	// /** 交易渠道 */
	// public Map<String, String> sbRemitTradeChannel = new HashMap<String,
	// String>() {
	// /**
	// *
	// */
	// private static final long serialVersionUID = 1L;
	//
	// {
	// put("01","Internet渠道");
	// put("02","银企对接/网上商城");
	// put("03","短信");
	// put("04","IVR");
	// put("05","CSR");
	// put("06","FAX");
	// put("07","自助终端");
	// put("08","第三方渠道，指第三方系统直连我行系统的情况");
	// put("09","ATM");
	// put("10","手机银行");
	// put("11","家居银行");
	// put("12","POS");
	// put("13","OTC");
	// put("14","客户端");
	// put("15","银行卡柜台前端（GCS）");
	// put("16","中银易商");
	// put("17","即时通讯(微信、易信、往来、LINE等即时通讯渠道)");
	// put("18","PAD");
	// put("99","分行特色");
	//
	//
	// // put("1", "网上银行");
	// // put("2", "手机银行");
	// // put("6", "银企对接");
	// }
	// };
	private Map<String, Object> resultAmount;

	public void setBtnRightClickListener(OnClickListener btnRightClickListener) {
		this.btnRightClickListener = btnRightClickListener;
	}

	// P502结购汇改造
	/**
	 * 结汇购汇：客户首次使用风险确认
	 */
	public void requestExchangeIsOpen() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(SBRemit.SBREMIT_EXCHANGE_IS_OPEN);
		HttpManager.requestBii(biiRequestBody, this,
				"requestExchangeIsOpenCallback");
	}

	/**
	 * 打开账户选择页面
	 */
	protected void startChooseAccountActivity(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, ChooseAccountActivity.class);
		startActivity(intent);
	}

	/**
	 * = 请求conversationId 来登录之外的conversation出Id
	 */
	public void requestSbremitCommConversationId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.CONVERSATION_ID_API);
		HttpManager.requestBii(biiRequestBody, this,
				"requestSbremitCommConversationIdCallBack");
	}

	/**
	 * 请求CommconversationId返回
	 * 
	 * @param resultObj
	 */
	public void requestSbremitCommConversationIdCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String commConversationId = (String) biiResponseBody.getResult();
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.CONVERSATION_ID, commConversationId);
		communicationCallBack(RESULT_OK);
	}

	/**
	 * @Title: importantIssuesCallback
	 * @Description: 重点关注对象确认书回调
	 * @param @param resultObj
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void importantIssuesCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> bRemitInfo = (Map<String, Object>) biiResponseBody
				.getResult();
	}

	/**
	 * 判断是否为18位身份证
	 */
	public boolean isMainLandId(String identityNumber) {
		Pattern p = Pattern.compile(CheckRegExp.IDENTITY_NUMBER);
		Matcher m = p.matcher(identityNumber);
		return m.matches();
	}

	/**
	 * @Title: requestForCommonData
	 * @Description: 请求已用额度、剩余额度
	 * @param
	 * @return void
	 */
	public void requestForCommonData(String type, String acountId) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(SBRemit.SBREMIT_MONEY_FORM_NEW);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(SBRemit.ACCOUNT_ID, acountId);
		paramsmap.put(SBRemit.FESS_FLAG, type);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForCommonDataCallBack");
	}

	/**
	 * @Title: requestForAccRemainCallBack
	 * @Description: 请求已用额度剩余额度余额回调
	 * @param @param resultObj
	 * @return void
	 */

	@SuppressWarnings("unchecked")
	public void requestForCommonDataCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		resultAmount = (Map<String, Object>) biiResponseBody.getResult();
		LogGloble.e("asd", "resultAmount" + resultAmount);
		SBRemitDataCenter.getInstance().setResultAmount(resultAmount);
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
		return ActivityTaskType.OneTask;
	}
}
