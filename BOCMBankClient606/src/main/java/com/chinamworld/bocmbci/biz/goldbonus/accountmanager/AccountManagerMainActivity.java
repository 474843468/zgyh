package com.chinamworld.bocmbci.biz.goldbonus.accountmanager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.GoldBonus;
import com.chinamworld.bocmbci.biz.goldbonus.GoldBonusBaseActivity;
import com.chinamworld.bocmbci.biz.goldbonus.GoldbonusLocalData;
import com.chinamworld.bocmbci.biz.goldbonus.busitrade.BusiTradeAvtivity;
import com.chinamworld.bocmbci.biz.goldbonus.timedepositmanager.TimeDepositManagerActivity;
import com.chinamworld.bocmbci.biz.investTask.GoldBonusTask;
import com.chinamworld.bocmbci.biz.investTask.IAction;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpObserver;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountManagerMainActivity extends GoldBonusBaseActivity implements
		OnCheckedChangeListener, OnClickListener, OnItemClickListener {
	private static final String TAG = "PsnGoldBonusPriceListQuery";
	private LayoutInflater mainInflater;
	/** 主界面的radiogroup */
	private RadioGroup radioGroup;
	/** 基本信息 */
	private RadioButton baseMessage;
	/** 活期定期持仓信息 */
	private RadioButton positionMessage;
	/** 基本信息的View */
	private RelativeLayout base_message_layout;
	/** 基本信息的View涨或跌文字 */
	private TextView ups_or_downs;
	/** 基本信息的View涨或跌的图片 */
	private ImageView ups_or_downs_pic;
	/** 基本信息的View中的银行买入价 */
	private TextView prms_price_listiterm1_buyprice;
	/** 基本信息的View中的银行卖出价 */
	private TextView prms_price_listiterm1_saleprice;
	/** 基本信息的View中的持仓数排名 */
	private TextView number_rank;
	/** 基本信息的View中的教育量排名 */
	private TextView educate_rank;
	/** 基本信息的View中的交易账户 */
	private TextView goldbonus_account;
	/** 可用余额 */
	private TextView goldbonus_available_balance;
	/** 活期定期持仓的View */
	private RelativeLayout position_message_layout;
	/** 活期定期持仓的View的种类 */
	private TextView variety;
	/** 活期定期持仓的View的持有数量 */
	private TextView position_amount;
	/** 子布局的linerlayout */
	private LinearLayout body_layout;
	/** 累积红利 */
	private TextView rmb_available;
	/** 交易账户类型 */
	private TextView goldbonus_account_type;
	/** 账户查询金接口返回集合 （第二个接口） */
	private Map<String, Object> resultMap;
	// /** 采用通用适配器接口实现 **/
	// private CommonAdapter<Map<String, Object>> adapter;
	/** 定投查询列表listview **/
	private ListView quotations_lv;
	/** 适配器集合 **/
	private List<Map<String, Object>> positionList;
	private int recordNumber = 0;
	/** 当前页数 */
	private int currentIndex = 0;
	/** 查询接口上送数据项 */
	private Map<String, Object> ParamMap = new HashMap<String, Object>();
	/** 排序方式 */
	private String ordType = null;
	/** 定投页面的利率查询按钮 **/
	private Button rate_check;
	/** 定投页面的再次买入 **/
	private TextView buy_again;
	/** 定投页面的转为定期 **/
	private TextView change_position;
	/** 定投页面的卖出 **/
	private TextView sell;
	/** 浮动盈亏试算 **/
	// private Button float_exchange;
	/** 浮动盈亏试算 **/
	private ImageView start_date_refreash;
	/** 浮动盈亏试算 **/
	private ImageView end_date_refreash;
	// 查询交易信息列表最后一个字段。开始为true点击更多为false
	private String isMore_new = "true";
	private String startFlag = "1";
	private boolean start = true;
	private String endFlag = "3";
	private boolean end = true;
	// 信息行情接口是否刷新标志
	private boolean isrefesh = true;
	/** 买入活期贵金属按钮 **/
	private Button finc_buy;
	/** 变更交易账户按钮 **/
	private Button change_account;
	private String accountID;
	// 状态
	private String statueString;
	private GoldBonusAccountAdapter adapter2;
	/** 查询更多，交易列表的Footer */
	private View footerView;

	private GoldBonusTask task = null;
	/** 长城借记卡 */
	protected static final String GREATWALL_CREDIT = "119";
	/** 活一本 */
	protected static final String HUOQIBENTONG = "188";
	// 第一次调用7秒查询
	private boolean isfirstError = true;
	// 活期持仓布局
	private LinearLayout data_normal;
	// 无活期持仓布局提示信息
	private TextView no_live_position;
	private TextView no_position;
	private TextView message_reminder_red;
	//人民币子账户集合
	List<Map<String, Object>> rmbAccountList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Button back = (Button) findViewById(R.id.ib_back);
		// back.setVisibility(View.GONE);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ActivityTaskManager.getInstance().removeAllSecondActivity();
			}
		});
		Button right = (Button) findViewById(R.id.ib_top_right_btn);
		right.setVisibility(View.GONE);
		/******** 仅开通投资理财、登记账户 **********/
		task = GoldBonusTask.getInstance(this);
		registActivityEvent(task);
		task.doTask(new IAction() {
			@Override
			public void SuccessCallBack(Object param) {
				/** 签约标示关联判断 **/
				// 返回按钮去掉
				getHttpTools()
						.requestHttp(GoldBonus.PSNGOLDBONUSSIGNINFOQUERY,
								"requestPsnGoldBonusSignInfoQueryCallBack",
								null, false);
			}
		}, 2);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CANCELED) {
			task.showTask();
		}
	}

	public void requestPsnGoldBonusSignInfoQueryCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		GoldbonusLocalData.getInstance().psnGoldBonusSignInfoQuery = resultMap;
		statueString = (String) resultMap.get(GoldBonus.LINKACCTFLAG);
		accountID = (String) resultMap.get(GoldBonus.ACCOUNTID);
		GoldbonusLocalData.getInstance().accountIdOld = accountID;
		GoldbonusLocalData.getInstance().accountNumberOld = (String) resultMap
				.get(GoldBonus.ACCOUNTNUM);
		if (statueString.equals("0")) {
			// 状态为“0”未签约 跳转到账户选择界面
			Intent intent = new Intent();
			intent.setClass(AccountManagerMainActivity.this,
					GoldAcountChoseActivity.class);
			// 2为第一次签约
			intent.putExtra("isFirst", "2");
			startActivity(intent);

		} else if (statueString.equals("2")) {
			// 状态为“2”已签约未关联 跳转到统一界面
			Intent intent = new Intent(AccountManagerMainActivity.this,
					GoldbounsReminderActivity.class);
			intent.putExtra("title", "账户管理");
			startActivity(intent);
		} else {
			// 状态为“1”已签约已关联 进入正常界面
			initMianView();
			// 初始化基本信息
			initBaseMessageLayout();
			setBodyLayoutChildView(base_message_layout);
			// 初始化定投信息
			initPositionMessageLayout();

			// 调用登陆后牌价行情查询，此接口7秒刷新一次，上半部分数据实时更新
			goldBonusPriceListPollingQuery();
			// 先申请conversionId
			requestCommConversationId();

		}

	}

	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPsnGoldBonusTransDetailQuery(startFlag, 0, isMore_new);
	}

	// 登陆后排挤行情查询回调
	public void requestPsnGoldBonusPriceListQueryCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}

		GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery = resultMap;
		prms_price_listiterm1_buyprice.setText(paseEndZero((String) resultMap
				.get(GoldBonus.BIDPRICE)));
		prms_price_listiterm1_saleprice.setText(paseEndZero((String) resultMap
				.get(GoldBonus.ASKPRICE)));
		// 平判断涨或跌 大于0表示涨
		if ((Integer.parseInt((String) resultMap.get(GoldBonus.UPDOWN))) > 0) {

			prms_price_listiterm1_buyprice.setTextColor(getResources().getColor(R.color.fonts_pink));
			prms_price_listiterm1_saleprice.setTextColor(getResources().getColor(R.color.fonts_pink));
		} else if ((Integer.parseInt((String) resultMap.get(GoldBonus.UPDOWN))) < 0) {
			prms_price_listiterm1_buyprice
					.setTextColor(AccountManagerMainActivity.this
							.getResources().getColor(R.color.fonts_green));
			prms_price_listiterm1_saleprice
					.setTextColor(AccountManagerMainActivity.this
							.getResources().getColor(R.color.fonts_green));
		} else {
			prms_price_listiterm1_buyprice.setTextColor(Color.BLACK);
			prms_price_listiterm1_saleprice.setTextColor(Color.BLACK);
		}
		initPositionMessageLayoutData();
	}

	// 账户查询接口，包括第一次，排序，更多等ps同样的接口名称，此方法用于起息日，到期日箭头所用
	private void requestPsnGoldBonusTransDetailQueryAgain(String ordType,
			int currentInexAgain, String isRefresh) {
		ParamMap.put(GoldBonus.ORDTYPE, ordType);
		ParamMap.put(
				GoldBonus.CURRENTINDEX,
				StringUtil.isNullOrEmpty(positionList) ? "0" : positionList
						.size() + "");
		ParamMap.put(GoldBonus.PAGESIZE, "10");
		ParamMap.put(GoldBonus.REFRESH, isRefresh);

		getHttpTools().requestHttp(GoldBonus.PSNGOLDBONUSACCOUNTQUERY,
				"requestPsnGoldBonusAccountQueryCallBackAgain", ParamMap, true);

	}

	// 贵金属积利账户查询接口callback
	public void requestPsnGoldBonusAccountQueryCallBackAgain(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		resultMap = (Map<String, Object>) biiResponseBody.getResult();
		GoldbonusLocalData.getInstance().PsnGoldBonusAccountQuery = resultMap;
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		List<Map<String, Object>> positionList2 = (List<Map<String, Object>>) (GoldbonusLocalData
				.getInstance().PsnGoldBonusAccountQuery.get(GoldBonus.LIST_NEW));
		if (StringUtil.isNullOrEmpty(positionList)) {
			positionList = positionList2;
		} else {
			positionList.addAll(positionList2);
		}
		addFooterView();
//		adapter2 = new GoldBonusAccountAdapter(this, positionList);
		adapter2.notifyDataSetChanged();
//		quotations_lv.setAdapter(adapter2);
		recordNumber = Integer.parseInt((String) resultMap
				.get(GoldBonus.RECORDNUMBER));

	}

	// 账户查询接口，包括第一次，排序，更多等
	private void requestPsnGoldBonusTransDetailQuery(String ordType,
			int currentInex, String isRefresh) {
		ParamMap.put(GoldBonus.ORDTYPE, ordType);
		ParamMap.put(
				GoldBonus.CURRENTINDEX,
				StringUtil.isNullOrEmpty(positionList) ? "0" : positionList
						.size() + "");
		ParamMap.put(GoldBonus.PAGESIZE, "10");
		ParamMap.put(GoldBonus.REFRESH, isRefresh);

		getHttpTools().requestHttpWithNoDialog(
				GoldBonus.PSNGOLDBONUSACCOUNTQUERY,
				"requestPsnGoldBonusAccountQueryCallBack", ParamMap, true);
		getHttpTools().registErrorCode(GoldBonus.PSNGOLDBONUSACCOUNTQUERY,
				"XPADG.EG052");// 停牌
	}

	// 贵金属积利账户查询接口callback
	public void requestPsnGoldBonusAccountQueryCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		resultMap = (Map<String, Object>) biiResponseBody.getResult();
		GoldbonusLocalData.getInstance().PsnGoldBonusAccountQuery = resultMap;
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}

		number_rank.setText("第" + (String) resultMap.get(GoldBonus.STOCKRANK));
		educate_rank.setText("第"
				+ (String) resultMap.get(GoldBonus.TRADECNTRANK));

		rmb_available.setText(getResources().getString(R.string.goldbonus_account_rmb_yuan)
				+ StringUtil.parseStringPattern((String) resultMap.get(GoldBonus.HEAPBONUS),2));
		goldbonus_account.setText(StringUtil
				.getForSixForString((String) resultMap
						.get(GoldBonus.ACCOUNTNUM)));
		goldbonus_account_type.setText(GoldbonusLocalData.accountType
				.get((String) resultMap.get(GoldBonus.ACCTTYPE)));
		positionList = (List<Map<String, Object>>) (GoldbonusLocalData
				.getInstance().PsnGoldBonusAccountQuery.get(GoldBonus.LIST_NEW));
		recordNumber = Integer.parseInt((String) resultMap
				.get(GoldBonus.RECORDNUMBER));
		addFooterView();

		adapter2 = new GoldBonusAccountAdapter(this, positionList);
		quotations_lv.setAdapter(adapter2);

		// 调用公共接口，查询账户余额，上送accountId,不要conversion
		Map<String, Object> ParamMapthis = new HashMap<String, Object>();
		ParamMapthis.put(GoldBonus.ACCOUNTID, accountID);
		getHttpTools().requestHttp(GoldBonus.PSNACCOUNTQUERYACCOUNTDETAIL,
				"requestPsnAccountQueryAccountDetailCallBack", ParamMapthis,
				false);
//		//公共接口报错，显示-
//		if (StringUtil.isNullOrEmpty(GoldbonusLocalData.getInstance().availbalance) ){
//			BaseDroidApp.getInstanse().showInfoMessageDialog(
//					getResources().getString(R.string.goldbonus_no_rmb));
//			goldbonus_available_balance.setText("-");
//		} else {
//			goldbonus_available_balance.setText("人民币元"
//					+ StringUtil.parseStringPattern(
//							GoldbonusLocalData.getInstance().availbalance, 2));
//		}

	}

	// 公共接口callback
	public void requestPsnAccountQueryAccountDetailCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		List<Map<String, Object>> AccDetailList = (List<Map<String, Object>>) resultMap
				.get("accountDetaiList");
		rmbAccountList=new ArrayList<Map<String,Object>>();
		for (int i = 0; i < (AccDetailList).size(); i++) {
			if (AccDetailList.get(i).get("currencyCode").equals("001")) {
				GoldbonusLocalData.getInstance().availbalance = (String) AccDetailList
						.get(i).get("availableBalance");
				rmbAccountList.add(AccDetailList.get(i));
			}
		}
		// 如果账户没有人民币币种，显示提示信息，报错，显示-，人民币为空显示—
		if (StringUtil.isNullOrEmpty(rmbAccountList) ){
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(R.string.goldbonus_no_rmb));
			goldbonus_available_balance.setText(getResources().getString(R.string.goldbonus_account_rmb_yuan)+"(无子账户)");
		} else {
			goldbonus_available_balance.setText(getResources().getString(R.string.goldbonus_account_rmb_yuan)
					+ StringUtil.parseStringPattern(
							GoldbonusLocalData.getInstance().availbalance, 2));
		}

	}

	private void initMianView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.gold_account_managert);
//		setTitle(R.string.goldbonus_account_manager);
		//设置标题文本
		getBackgroundLayout().setTitleNewText(R.string.goldbonus_account_manager);
		//设置标题右边主界面按钮（默认为显示 主界面）
		getBackgroundLayout().setRightButtonNewText(null);

		setLeftSelectedPosition("goldbonusManager_1");// 定投管理
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		baseMessage = (RadioButton) findViewById(R.id.base_message);
		positionMessage = (RadioButton) findViewById(R.id.position_message);
		mainInflater = LayoutInflater.from(this);
		body_layout = (LinearLayout) findViewById(R.id.body_layout);
		radioGroup.setOnCheckedChangeListener(this);

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.base_message:// 基本信息
			setBodyLayoutChildView(base_message_layout);

			break;
		case R.id.position_message:// 活期定期持仓信息
			initPositionMessageLayout();
			Map<String, Object> resultMap2 = GoldbonusLocalData.getInstance().PsnGoldBonusAccountQuery;
			positionList = (List<Map<String, Object>>) (GoldbonusLocalData
					.getInstance().PsnGoldBonusAccountQuery
					.get(GoldBonus.LIST_NEW));
			// 判断有没有持仓信息，如果有正常进入，没有弹出提示信息，并进入买卖,活期定期都没有
			if (StringUtil.isNullOrEmpty(positionList)&& 
					((StringUtil.isNullOrEmpty(resultMap.get(GoldBonus.CTISSUENAME))))) {
				BaseDroidApp.getInstanse().showGoldBounsDialog(
						"您暂无持仓信息，我们将引导您进入“买卖交易”购入贵金属积利产品。",
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(
										AccountManagerMainActivity.this,
										BusiTradeAvtivity.class);
								intent.putExtra(GoldBonus.PASSFLAG,
										REQUEST_LOGIN_CODE_BUY);
								startActivity(intent);
							}
						});

			} else if ((!StringUtil.isNullOrEmpty(positionList))
					&& (StringUtil.isNullOrEmpty(resultMap
							.get(GoldBonus.CTISSUENAME)))
					&& ((StringUtil.isNullOrEmpty(resultMap
							.get(GoldBonus.BALANCE))) || (resultMap
							.get(GoldBonus.BALANCE).equals("0")))) {// 活期没有，有定期，显示活期提示信息
				setBodyLayoutChildView(position_message_layout);
				no_live_position = (TextView) findViewById(R.id.no_live_position);
				data_normal = (LinearLayout) findViewById(R.id.data_normal);
				no_position = (TextView) findViewById(R.id.no_position);
				data_normal.setVisibility(View.GONE);
				no_live_position.setVisibility(View.VISIBLE);
				startFlag = "1";
				isMore_new = "true";
				requestPsnGoldBonusTransDetailQuery(startFlag, 0, isMore_new);

			} else if (StringUtil.isNullOrEmpty(positionList)
					&& (!StringUtil.isNullOrEmpty((resultMap
							.get(GoldBonus.CTISSUENAME))))) {// 活期有定期没有
				setBodyLayoutChildView(position_message_layout);
				no_position = (TextView) findViewById(R.id.no_position);
				no_live_position = (TextView) findViewById(R.id.no_live_position);
				data_normal = (LinearLayout) findViewById(R.id.data_normal);
				data_normal.setVisibility(View.VISIBLE);
				no_live_position.setVisibility(View.GONE);
				no_position.setVisibility(View.VISIBLE);
				quotations_lv.setVisibility(View.GONE);
				variety.setText((String) (GoldbonusLocalData.getInstance().PsnGoldBonusAccountQuery
						.get(GoldBonus.CTISSUENAME)));
				position_amount
						.setText(StringUtil.parseStringPattern(
								(String) (GoldbonusLocalData.getInstance().PsnGoldBonusAccountQuery
										.get(GoldBonus.BALANCE)), 0)
								+ "  " + "克");// 去掉两位小数
			} else {
				setBodyLayoutChildView(position_message_layout);
				no_live_position = (TextView) findViewById(R.id.no_live_position);
				data_normal = (LinearLayout) findViewById(R.id.data_normal);
				data_normal.setVisibility(View.VISIBLE);
				no_live_position.setVisibility(View.GONE);
				initPositionMessageLayoutData();
				// 如果为第一次则掉接口，重新刷新接口
				// 初始化定投信息
				startFlag = "1";
				isMore_new = "true";
				requestPsnGoldBonusTransDetailQuery(startFlag, 0, isMore_new);

			}
			break;
		default:
			break;
		}

	}

	// 初始化持仓信息界面的数据
	private void initPositionMessageLayoutData() {
		// TODO Auto-generated method stub

		variety.setText((String) (GoldbonusLocalData.getInstance().PsnGoldBonusAccountQuery
				.get(GoldBonus.CTISSUENAME)));
		position_amount
				.setText(StringUtil.parseStringPattern(
						(String) (GoldbonusLocalData.getInstance().PsnGoldBonusAccountQuery
								.get(GoldBonus.BALANCE)), 0)
						+ "  " + "克");// 去掉两位小数
		addFooterView();
		adapter2 = new GoldBonusAccountAdapter(this, positionList);
		quotations_lv.setAdapter(adapter2);

	}

	private void initBaseMessageLayout() {
		base_message_layout = (RelativeLayout) mainInflater.inflate(
				R.layout.goldbonus_account_base_message, null);
		prms_price_listiterm1_buyprice = (TextView) base_message_layout
				.findViewById(R.id.prms_price_listiterm1_buyprice);
		prms_price_listiterm1_saleprice = (TextView) base_message_layout
				.findViewById(R.id.prms_price_listiterm1_saleprice);
		prms_price_listiterm1_buyprice.setOnClickListener(this);
		prms_price_listiterm1_saleprice.setOnClickListener(this);
		message_reminder_red=(TextView)base_message_layout. findViewById(R.id.message_reminder_red);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				message_reminder_red);

		number_rank = (TextView) base_message_layout
				.findViewById(R.id.number_rank);
		educate_rank = (TextView) base_message_layout
				.findViewById(R.id.educate_rank);
		rmb_available = (TextView) base_message_layout
				.findViewById(R.id.rmb_available);
		goldbonus_account = (TextView) base_message_layout
				.findViewById(R.id.goldbonus_account);
		goldbonus_account_type = (TextView) base_message_layout
				.findViewById(R.id.goldbonus_account_type);
		goldbonus_available_balance = (TextView) base_message_layout
				.findViewById(R.id.goldbonus_available_balance);
		finc_buy = (Button) base_message_layout.findViewById(R.id.finc_buy);
		finc_buy.setOnClickListener(this);
		change_account = (Button) base_message_layout
				.findViewById(R.id.change_account);
		change_account.setOnClickListener(this);
	}

	private void initPositionMessageLayout() {
		position_message_layout = (RelativeLayout) mainInflater.inflate(
				R.layout.goldbonus_position_message, null);
		footerView = LayoutInflater.from(this).inflate(
				R.layout.epay_tq_list_more, null);
		variety = (TextView) position_message_layout.findViewById(R.id.variety);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				variety);
		position_amount = (TextView) position_message_layout
				.findViewById(R.id.position_amount);
		rate_check = (Button) position_message_layout
				.findViewById(R.id.rate_check);
		// float_exchange = (Button) position_message_layout
		// .findViewById(R.id.float_exchange);
		// float_exchange.setOnClickListener(this);
		quotations_lv = (ListView) position_message_layout
				.findViewById(R.id.quotations_lv);
		quotations_lv.setOnItemClickListener(this);
		buy_again = (TextView) position_message_layout
				.findViewById(R.id.buy_again);
		change_position = (TextView) position_message_layout
				.findViewById(R.id.change_position);
		sell = (TextView) position_message_layout.findViewById(R.id.sell);
		buy_again.setOnClickListener(this);
		change_position.setOnClickListener(this);
		no_live_position = (TextView) findViewById(R.id.no_live_position);
		data_normal = (LinearLayout) findViewById(R.id.data_normal);
		sell.setOnClickListener(this);

		start_date_refreash = (ImageView) position_message_layout
				.findViewById(R.id.start_date_refreash);
		start_date_refreash.setOnClickListener(this);
		end_date_refreash = (ImageView) position_message_layout
				.findViewById(R.id.end_date_refreash);
		end_date_refreash.setOnClickListener(this);
	}

	/**
	 * 为body_layout添加view
	 * 
	 * @param view
	 */
	private void setBodyLayoutChildView(RelativeLayout view) {
		body_layout.removeAllViews();
		body_layout.addView(view);
		body_layout.invalidate();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.prms_price_listiterm1_buyprice:
			// 跳转买入界面
			Intent intent_buy = new Intent(AccountManagerMainActivity.this,
					BusiTradeAvtivity.class);
			intent_buy.putExtra(GoldBonus.PASSFLAG, REQUEST_LOGIN_CODE_SELL);
			startActivity(intent_buy);

			break;
		case R.id.prms_price_listiterm1_saleprice:
			// 跳转卖出界面
			Intent intent_sell = new Intent(AccountManagerMainActivity.this,
					BusiTradeAvtivity.class);
			intent_sell.putExtra(GoldBonus.PASSFLAG, REQUEST_LOGIN_CODE_BUY);
			startActivity(intent_sell);
			break;
		// case R.id.float_exchange:
		// //功能已去掉
		// BaseDroidApp.getInstanse().dissmissFootChooseDialog();
		// View dialogView = View.inflate(AccountManagerMainActivity.this,
		// R.layout.goldbouns_float_try, null);
		// ((TextView) (dialogView.findViewById(R.id.ctIssueName)))
		// .setText(StringUtil.parseStringPattern((String) resultMap
		// .get(GoldBonus.CTISSUENAME) == null ? ""
		// : (String) resultMap.get(GoldBonus.CTISSUENAME), 0));
		// ((TextView) (dialogView.findViewById(R.id.amont)))
		// .setText(StringUtil.parseStringPattern((String) resultMap
		// .get(GoldBonus.BALANCE) == null ? ""
		// : (String) resultMap.get(GoldBonus.BALANCE), 0));
		// if (Double.valueOf((String) resultMap.get(GoldBonus.FLOATVALUE)) > 0)
		// {
		// ((TextView) dialogView.findViewById(R.id.updown))
		// .setTextColor(Color.RED);
		// } else {
		// ((TextView) dialogView.findViewById(R.id.updown))
		// .setTextColor(AccountManagerMainActivity.this
		// .getResources().getColor(R.color.greens));
		// }
		// ((TextView) dialogView.findViewById(R.id.updown))
		// .setText((String) resultMap.get(GoldBonus.FLOATVALUE));
		// ((ImageView) dialogView.findViewById(R.id.img_exit_accdetail))
		// .setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// BaseDroidApp.getInstanse().closeAllDialog();
		// }
		// });
		//
		// BaseDroidApp.getInstanse().showDialog(dialogView);
		// break;
		// 买入活期贵金属积利
		case R.id.finc_buy:
			Intent intent_finc_buy = new Intent();
			intent_finc_buy.setClass(AccountManagerMainActivity.this,
					BusiTradeAvtivity.class);
			intent_finc_buy
					.putExtra(GoldBonus.PASSFLAG, REQUEST_LOGIN_CODE_BUY);
			startActivity(intent_finc_buy);

			break;
		// b变更交易账户
		case R.id.change_account:
			/**
			 * 查询用户的所有账户,在这判断有没有课变更的账户
			 */
			requestPsnCommonQueryAllChinaBankAccount();

			break;
		// 再次买入
		case R.id.buy_again:
			Intent intentbuy_again = new Intent();
			intentbuy_again.setClass(AccountManagerMainActivity.this,
					BusiTradeAvtivity.class);
			intentbuy_again
					.putExtra(GoldBonus.PASSFLAG, REQUEST_LOGIN_CODE_BUY);
			startActivity(intentbuy_again);
			break;
		// 转为定投
		case R.id.change_position:
			Intent intent_position = new Intent();
			intent_position.setClass(AccountManagerMainActivity.this,
					TimeDepositManagerActivity.class);
			startActivity(intent_position);
			break;
		// 卖出
		case R.id.sell:
			Intent intentsell = new Intent(AccountManagerMainActivity.this,
					BusiTradeAvtivity.class);
			intentsell.putExtra(GoldBonus.PASSFLAG, REQUEST_LOGIN_CODE_SELL);
			startActivity(intentsell);

			break;
		// 起始日期排序按钮
		case R.id.start_date_refreash:
			// 起息日默认为降序，点击为升序
			positionList.clear();
			currentIndex = 0;
			if (start) {
				startFlag = "2";
				start_date_refreash.setImageDrawable(getResources()
						.getDrawable(R.drawable.bocinvt_sort_up));

				start = false;
			} else {
				startFlag = "1";
				start_date_refreash.setImageDrawable(getResources()
						.getDrawable(R.drawable.bocinvt_sort_down));
				start = true;
			}
			end_date_refreash.setImageDrawable(getResources().getDrawable(
					R.drawable.bocinvt_sort_un));
			if (!StringUtil.isNullOrEmpty(positionList)) {
				positionList.clear();
			}
			isMore_new = "true";
			requestPsnGoldBonusTransDetailQuery(startFlag, currentIndex,
					isMore_new);
			break;
		// 结束日期排序按钮
		case R.id.end_date_refreash:
			positionList.clear();
			currentIndex = 0;
			// 第一次点击为排序为降序3升序4
			if (end) {
				endFlag = "3";
				end_date_refreash.setImageDrawable(getResources().getDrawable(
						R.drawable.bocinvt_sort_down));
				end = false;
			} else {
				endFlag = "4";
				end_date_refreash.setImageDrawable(getResources().getDrawable(
						R.drawable.bocinvt_sort_up));
				end = true;
			}
			start_date_refreash.setImageDrawable(getResources().getDrawable(
					R.drawable.bocinvt_sort_un));
			if (!StringUtil.isNullOrEmpty(positionList)) {
				positionList.clear();
			}
			isMore_new = "true";
			requestPsnGoldBonusTransDetailQuery(endFlag, currentIndex,
					isMore_new);

			break;
		default:
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(AccountManagerMainActivity.this,
				AccountManagerDetialActivity.class);
		intent.putExtra(GoldBonus.ISSUENAME,
				positionList.get(arg2).get(GoldBonus.ISSUENAME) == null ? ""
						: positionList.get(arg2).get(GoldBonus.ISSUENAME)
								.toString());// 产品名称
		intent.putExtra(GoldBonus.TRADEDATE,
				positionList.get(arg2).get(GoldBonus.TRADEDATE) == null ? ""
						: positionList.get(arg2).get(GoldBonus.TRADEDATE)
								.toString());// 起息日
		intent.putExtra(GoldBonus.LINITTIME,
				positionList.get(arg2).get(GoldBonus.LINITTIME) == null ? ""
						: positionList.get(arg2).get(GoldBonus.LINITTIME)
								.toString());// 期限
		intent.putExtra(GoldBonus.EXPDATE,
				positionList.get(arg2).get(GoldBonus.EXPDATE) == null ? ""
						: positionList.get(arg2).get(GoldBonus.EXPDATE)
								.toString());// 到期日
		// 暂时不知道调用哪个字段
		intent.putExtra(GoldBonus.TRADEWEIGHT,
				positionList.get(arg2).get(GoldBonus.TRADEWEIGHT) == null ? ""
						: positionList.get(arg2).get(GoldBonus.TRADEWEIGHT)
								.toString());
		// 数量
		intent.putExtra(GoldBonus.ISSUERATE,
				positionList.get(arg2).get(GoldBonus.ISSUERATE) == null ? ""
						: positionList.get(arg2).get(GoldBonus.ISSUERATE)
								.toString());// 年化利率
		intent.putExtra(GoldBonus.REGBONUS,
				positionList.get(arg2).get(GoldBonus.REGBONUS) == null ? ""
						: positionList.get(arg2).get(GoldBonus.REGBONUS)
								.toString());// 应付红利
		intent.putExtra(GoldBonus.LIMITUNIT,
				positionList.get(arg2).get(GoldBonus.LIMITUNIT) == null ? ""
						: positionList.get(arg2).get(GoldBonus.LIMITUNIT)
								.toString());// 期限单位
		startActivity(intent);
	}

	/** 7秒轮询 */
	private void goldBonusPriceListPollingQuery() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(GoldBonus.PSNGOLDBONUSPRICELISTQUERYLOGIN);
		// //先调用一次
		HttpManager.requestPollingBii(biiRequestBody, pollingHandler,
				ConstantGloble.FOREX_REFRESH_TIMES);

	}

	static int i = 0;
	private Handler pollingHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			// http状态码
			String resultHttpCode = (String) ((Map<String, Object>) msg.obj)
					.get(ConstantGloble.HTTP_RESULT_CODE);
			// 返回数据
			Object resultObj = ((Map<String, Object>) msg.obj)
					.get(ConstantGloble.HTTP_RESULT_DATA);
			// 回调对象
			HttpObserver callbackObject = (HttpObserver) ((Map<String, Object>) msg.obj)
					.get(ConstantGloble.HTTP_CALLBACK_OBJECT);
			// 回调方法
			String callBackMethod = (String) ((Map<String, Object>) msg.obj)
					.get(ConstantGloble.HTTP_CALLBACK_METHOD);

			switch (msg.what) {
			case ConstantGloble.HTTP_STAGE_CONTENT:
				// 执行全局前拦截器
				if (BaseDroidApp.getInstanse()
						.httpRequestCallBackPre(resultObj)) {
					break;
				}
				// 执行callbackObject回调前拦截器
				if (httpRequestCallBackPre(resultObj)) {
					break;
				}
				// 清空更新时间
				// clearTimes();

				BiiResponse biiResponse = (BiiResponse) ((Map<String, Object>) msg.obj)
						.get(ConstantGloble.HTTP_RESULT_DATA);
				List<BiiResponseBody> biiResponseBodys = biiResponse
						.getResponse();
				BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
				Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
						.getResult();
				if (StringUtil.isNullOrEmpty(resultMap)) {
					return;
				}

				GoldbonusLocalData.getInstance().PsnGoldBonusPriceListQuery = resultMap;
				GoldbonusLocalData.getInstance().BankBuyPrice = StringUtil
						.parseStringPattern2(
								(String) resultMap.get(GoldBonus.BIDPRICE), 2);
				GoldbonusLocalData.getInstance().bankSellPrice = StringUtil
						.parseStringPattern2(
								(String) resultMap.get(GoldBonus.ASKPRICE), 2);
				// 平判断涨或跌 大于0表示涨
				if ((Double.parseDouble((String) resultMap
						.get(GoldBonus.UPDOWN))) > 0) {

					prms_price_listiterm1_buyprice.setTextColor(getResources().getColor(R.color.fonts_pink));
					prms_price_listiterm1_saleprice.setTextColor(getResources().getColor(R.color.fonts_pink));
				} else if ((Double.parseDouble((String) resultMap
						.get(GoldBonus.UPDOWN))) < 0) {
					prms_price_listiterm1_buyprice
							.setTextColor(AccountManagerMainActivity.this
									.getResources().getColor(R.color.fonts_green));
					prms_price_listiterm1_saleprice
							.setTextColor(AccountManagerMainActivity.this
									.getResources().getColor(R.color.fonts_green));
				} else {
					prms_price_listiterm1_buyprice.setTextColor(Color.BLACK);
					prms_price_listiterm1_saleprice.setTextColor(Color.BLACK);
				}
				// 银行买入卖出价为0，显示”-“
				if (Double.parseDouble((String) resultMap.get(GoldBonus.BIDPRICE))==0) {
					prms_price_listiterm1_buyprice.setText("-");
					prms_price_listiterm1_buyprice.setTextColor(Color.BLACK);
				} else {
					prms_price_listiterm1_buyprice
							.setText(paseEndZero((String) resultMap
									.get(GoldBonus.BIDPRICE)));
				}
				if (Double.parseDouble((String) resultMap.get(GoldBonus.ASKPRICE))==0) {
					prms_price_listiterm1_saleprice.setText("-");
					prms_price_listiterm1_saleprice.setTextColor(Color.BLACK);
				} else {
					prms_price_listiterm1_saleprice
							.setText(paseEndZero((String) resultMap
									.get(GoldBonus.ASKPRICE)));
				}

				// 执行callbackObject回调后拦截器
				if (httpRequestCallBackAfter(resultObj)) {
					break;
				}

				// 执行全局后拦截器
				if (BaseDroidApp.getInstanse().httpRequestCallBackAfter(
						resultObj)) {
					break;
				}
				break;
			case ConstantGloble.HTTP_STAGE_CODE:
				// 执行code error 全局前拦截器
				if (BaseDroidApp.getInstanse().httpCodeErrorCallBackPre(
						resultHttpCode)) {
					break;
				}
				// 执行callbackObject error code 回调前拦截器
				if (callbackObject.httpCodeErrorCallBackPre(resultHttpCode)) {

					break;
				}
				Method httpCodeCallbackMethod = null;
				try {
					// 回调
					httpCodeCallbackMethod = callbackObject.getClass()
							.getMethod(callBackMethod, String.class);
					httpCodeCallbackMethod.invoke(callbackObject,
							resultHttpCode);
				} catch (SecurityException e) {
					LogGloble.e(TAG, "SecurityException ", e);
				} catch (NoSuchMethodException e) {
					LogGloble.e(TAG, "NoSuchMethodException ", e);
				} catch (IllegalArgumentException e) {
					LogGloble.e(TAG, "IllegalArgumentException ", e);
				} catch (IllegalAccessException e) {
					LogGloble.e(TAG, "IllegalAccessException ", e);
				} catch (InvocationTargetException e) {
					LogGloble.e(TAG, "InvocationTargetException ", e);
				} catch (NullPointerException e) {
					LogGloble.e(TAG, "NullPointerException ", e);
					throw e;
				} catch (ClassCastException e) {
					LogGloble.e(TAG, "ClassCastException ", e);
					throw e;
				}
				// 执行code error 全局后拦截器
				if (BaseDroidApp.getInstanse().httpCodeErrorCallBackAfter(
						resultHttpCode)) {
					break;
				}
				// 执行callbackObject code error 后拦截器
				if (callbackObject.httpCodeErrorCallBackAfter(resultHttpCode)) {
					break;
				}
				break;

			default:
				break;
			}
		};
	};

	protected void onPause() {
		super.onPause();
		if (!StringUtil.isNull(statueString)) {
			if ("1".equals(statueString)) {
				HttpManager.stopPolling();
			}
		}

	};

	/** 为列表添加更多 */
	private void addFooterView() {
		int size = Integer.valueOf(recordNumber);
		if (positionList.size() < size) {
			if (quotations_lv.getFooterViewsCount() <= 0) {
				quotations_lv.addFooterView(footerView);
				quotations_lv.setClickable(true);
			}
		} else {
			if (quotations_lv.getFooterViewsCount() > 0) {
				quotations_lv.removeFooterView(footerView);
				footerView.setClickable(false);
			}
		}

		footerView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestPsnGoldBonusTransDetailQueryAgain(startFlag,
						currentIndex, "false");
				;
			}
		});
	}

	public void requestPsnCommonQueryAllChinaBankAccount() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		getHttpTools().requestHttp(GoldBonus.PSNCOMMONQUERYFILTEREDACCOUNTS,
				"requestPsnCommonQueryAllChinaBankAccountCallBack", paramMap,
				false);
	}

	/** 获取借记卡列表----回调 */
	public void requestPsnCommonQueryAllChinaBankAccountCallBack(
			Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> result = (List<Map<String, Object>>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			return;
		}
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		if (result != null && result.size() > 0) {
			for (int i = 0; i < result.size(); i++) {
				if (GREATWALL_CREDIT.equals(result.get(i).get(
						Crcd.CRCD_ACCOUNTTYPE_RES))
						|| HUOQIBENTONG.equals(result.get(i).get(
								Crcd.CRCD_ACCOUNTTYPE_RES))) {
					resultList.add(result.get(i));
				}
			}
			for (int i = 0; i < result.size(); i++) {
				// 账号变更时重复账号不能显示
				if ((GoldbonusLocalData.getInstance().accountIdOld
						.equals(result.get(i).get(GoldBonus.ACCOUNTID)))) {
					resultList.remove(result.get(i));
				}
			}
		}
		if (resultList != null && resultList.size() > 0) {
			GoldbonusLocalData.getInstance().requestPsnCommonQueryAllChinaBankAccountList = resultList;
			Intent intent_change_account = new Intent();
			intent_change_account.setClass(AccountManagerMainActivity.this,
					GoldAcountChoseActivity.class);
			// 1为变更交易账户
			intent_change_account.putExtra("isFirst", "1");
			startActivity(intent_change_account);
		} else {
			// 没有可变更账户弹出错误信息
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getResources().getString(
							R.string.goldbouns_no_change_account));
			return;

		}

	}

	@Override
	public boolean doBiihttpRequestCallBackPre(BiiResponse response) {
		BaseHttpEngine.dissMissProgressDialog();
		List<BiiResponseBody> biiResponseBodyList = response.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodyList.get(0);
		if (!StringUtil.isNullOrEmpty(biiResponseBodyList)) {
			for (BiiResponseBody body : biiResponseBodyList) {
				if (GoldBonus.PSNGOLDBONUSPRICELISTQUERYLOGIN
						.equals(biiResponseBody.getMethod())) {
					BiiHttpEngine.dissMissProgressDialog();
					BiiError biiError = biiResponseBody.getError();
					// 判断是否存在error
					if (biiError != null && biiError.getCode() != null) {

						if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
							return super.doBiihttpRequestCallBackPre(response);
						}
						if (biiError.getCode().equals("XPADG.EG051")
								|| biiError.getCode().equals("XPADG.EG052")) {
							prms_price_listiterm1_buyprice.setText("-");
							prms_price_listiterm1_saleprice.setText("-");
							// 第一次报错显示报错信息对话框，之后不显示
							if (isfirstError) {
								isfirstError = false;
								return super
										.doBiihttpRequestCallBackPre(response);
							}
							// requestCommConversationId();
						}

						if (StringUtil.isNullOrEmpty(GoldbonusLocalData
								.getInstance().PsnGoldBonusPriceListQuery)) {
							prms_price_listiterm1_buyprice.setText("-");
							prms_price_listiterm1_saleprice.setText("-");
							if (StringUtil.isNullOrEmpty(GoldbonusLocalData
									.getInstance().PsnGoldBonusAccountQuery)) {
								number_rank.setText("-");
								educate_rank.setText("-");
							}
						}
						return true;
					}
					//查帐户人民币余额报错信息
				}else if (GoldBonus.PSNACCOUNTQUERYACCOUNTDETAIL.equals(biiResponseBody.getMethod())) {
					BiiHttpEngine.dissMissProgressDialog();
					BiiError biiError = biiResponseBody.getError();
					// 判断是否存在error
					if (biiError != null && biiError.getCode() != null) {
						if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
							return super.doBiihttpRequestCallBackPre(response);
						}
						if (biiError.getCode().equals("AccQueryDetailAction.NoSubAccount")) {
							//这个报错屏蔽，并显示
							goldbonus_available_balance.setText(getResources().getString(R.string.goldbonus_account_rmb_yuan)+"(无子账户)");
							
							return true;
						}else {
							//其他报错不屏蔽，显示-
							goldbonus_available_balance.setText(getResources().getString(R.string.goldbonus_account_rmb_yuan)+"-");
							return super.doBiihttpRequestCallBackPre(response);
						}
						
					}else {
						return super.doBiihttpRequestCallBackPre(response);
					}
				}
			}

		} else {
			return super.doBiihttpRequestCallBackPre(response);
		}

		return super.doBiihttpRequestCallBackPre(response);
	}

}
