package com.chinamworld.bocmbci.biz.lsforex.rate;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.base.application.CommonApplication;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.foreign.StringTools;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseNewActivity;
import com.chinamworld.bocmbci.biz.lsforex.bail.IsForexBailInfoActivity;
import com.chinamworld.bocmbci.biz.lsforex.myrate.IsForexMyRateInfoActivity;
import com.chinamworld.bocmbci.biz.lsforex.query.IsForexCurrenyWTActivity;
import com.chinamworld.bocmbci.biz.lsforex.query.IsForexQueryMenuActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpObserver;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.thread.PollingRequestThread;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.model.httpmodel.sifang.querySingelQuotation.QuerySingelQuotationRequestParams;
import com.chinamworld.bocmbci.model.httpmodel.sifang.querySingelQuotation.QuerySingleQuotationResponseData;
import com.chinamworld.bocmbci.model.httpmodel.sifang.querySingelQuotation.QuerySingleQuotationResult;
import com.chinamworld.bocmbci.model.httpmodel.sifang.querymultiplequotation.QueryMultipleQuotationRequestParams;
import com.chinamworld.bocmbci.model.httpmodel.sifang.querymultiplequotation.QueryMultipleQuotationResponseData;
import com.chinamworld.bocmbci.model.httpmodel.sifang.querymultiplequotation.QueryMultipleQuotationResult;
import com.chinamworld.bocmbci.net.GsonTools;
import com.chinamworld.bocmbci.net.HttpHelp;
import com.chinamworld.bocmbci.net.model.BaseResponseData;
import com.chinamworld.bocmbci.net.model.IHttpErrorCallBack;
import com.chinamworld.bocmbci.net.model.IHttpResponseCallBack;
import com.chinamworld.bocmbci.net.model.IOkHttpErrorCode;
import com.chinamworld.bocmbci.server.LocalDataService;
import com.chinamworld.bocmbci.userwidget.investview.InvestPriceView;
import com.chinamworld.bocmbci.utils.KeyAndValueItem;
import com.chinamworld.bocmbci.utils.ListUtils;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.adapter.CommonAdapter;
import com.chinamworld.bocmbci.widget.adapter.ICommonAdapter;
import com.chinamworld.llbt.userwidget.NewBackGround.NewBackGroundLayout;
import com.chinamworld.llbt.userwidget.NumberStyleTextView;
import com.chinamworld.llbt.userwidget.refreshliseview.DownRefreshLayout;
import com.chinamworld.llbt.userwidget.refreshliseview.IRefreshLayoutListener;
import com.chinamworld.llbt.userwidget.refreshliseview.PullableListView;
import com.chinamworld.llbt.userwidget.refreshliseview.RefreshDataStatus;
import com.chinamworld.llbt.userwidget.scrollview.NewScrollView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 双向宝行情页面 */
public class IsForexTwoWayTreasureNewActivity extends IsForexBaseNewActivity implements OnClickListener,ICommonAdapter<Map<String, Object>>,IRefreshLayoutListener {

	/** 返回 **/
//	private Button btn_base_back;
//	private TextView tv_base_title ; //标题
//	private Button btn_share;//分享
//	private Button btn_more;//更多菜单sys
	private InvestPriceView investPriceView;

	private FrameLayout loginOutLayout;//没有登录布局
	//	private Button btn_login;
	private FrameLayout loginLayout;//已登录布局
//	private FrameLayout ima_help;//帮助
//	private ImageView img_open;//是否显示总金额
//	private TextView tv_refvalue; //
//	private TextView amount_tv1;//总金
//	private TextView amount_tv2;

//	private RelativeLayout lyt_logout_image;//没有登录布局



	//	private RelativeLayout lyt_login_title ;//已登录布局
//	private ImageView ima_help;//帮助
//	private ImageView ima_openeye;//是否显示总金额
//	private TextView tv_refvalue; //
//	private TextView tv_totalnum;//总金额
	private LinearLayout btn_ly;
	private Button btn_query;//交易查询
	private Button btn_accountmanager;//账户管理
	private Button btn_recall;//撤单

	private Button btn_selfchose;//自选
	private View ly_selfchose;
	private Button btn_gold;//贵金属
	private View ly_gold;
	private Button btn_forex;//外汇
	private View ly_forex;
	private TextView tv_edit;//编辑
	private TextView tv_riseorfall;//涨幅跌
	private PullableListView lv_prms;
	private TextView tv_refishtime;//更新时间

	private LinearLayout self_ly; //未登录 自选

	/**贵金属多笔行情查询 登录前后共用的数据*/
//	private List<Map<String, Object>> commDataList;
	/** @customerRateList:用户定制的外汇详情list  自选 */
	private List<Map<String, Object>> customerRateList = null;
	/** @allRateList:全部外汇详情的list */
	private List<Map<String, Object>> allRateList = null;
	/**标识*/
	private String vfgType = "";
	/**适配器*/
	private CommonAdapter<Map<String, Object>> adapter;
//	private InvestAdaper adapter;
	/** 区分用户选中的是那个按钮0： btn_selfchose自选  customerRateList 1： btn_forex 外汇 allRateList 2：btn_gold贵金属*/
	private int coinId = 0;
	/**我的外汇汇率是否有数据(自选)*/
	private Boolean isHasCustomerList = false;
	/** 全部汇率是否有数据 （外汇）*/
	private boolean isHasAllList = false;
	/**涨跌幅标识 默认显示涨跌幅*/
	private boolean iscurrPercentDiff = true;//涨跌幅
	/**货币对代码*/
	private String currencyPairs;
	/**外汇（贵金属）登陆后 合并数据*/
	private List<Map<String, Object>> totalLoginDataList;
	/**外汇（贵金属）登陆前合并数据*/
	private List<Map<String, Object>> totalLogoutDataList;
	private String paritiesType = "";//牌价
	/**单笔行情查询 登录前后共用的数据*/
	private Map<String, Object> singleDataMap;
	/**信息ListView中点击的第几个*/
	private int index = -1;
	/**持仓信息*/
	private List<Map<String, Object>> positionInfoList;
	/**交易查询=0， 账户管理=1，撤单=2 ,自选 = 3*/
	private int btnId = -1;
	/**涨跌幅升=1、降序=2、不排序=0 标识*/
	private int quoteChangeId = 0;
	/**贵金属币种排序*/
//	List<Map<String, Object>> sort_code_List = new ArrayList<Map<String, Object>>();
	List<QueryMultipleQuotationResult.QueryMultipleQuotationItem>  queryMultiplelist;
	private QuerySingleQuotationResult  querySingleItem;
	private List<Map<String, Object>> oldtotalDataList;//老数据
	private List<Map<String, Object>> oldtotalLogoutDataList;//合并后的数据
	//首次进入自选刷新两次四方接口 外汇和贵金属 标识
	private Boolean isCustomer = false;
	DownRefreshLayout mDownRefreshLayout;
	private boolean isLight = false;
//	private boolean isGoBack = false;
	/**未登录情况下跳转到更多页面跳入 再返回标识*/
	private boolean isLogoutMoreBack = false;
	/**登录后跳转到更多页面 在返回 标识*/
	private boolean isLoginMoreBack = false;
	/**判断高亮标识 防止上滑高亮*/
	private boolean isStartAnimator= true;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		taskTag = 4;
		initBaseLayout();
		setContentView(R.layout.isforex_rate_main_new);
		isFlagGoWay = 0;
		initUi();
		initListener();
		initData();

	}

	private void initBaseLayout(){
		setLeftButtonPopupGone();
		getBackGroundLayout().setTitleBackground(R.color.lianlong_color_2797c1);
		getBackGroundLayout().setTitleTextColor(R.color.btn_white);
		getBackGroundLayout().setTitleText(getResources().getString(R.string.mian_menu19));
		getBackGroundLayout().setLeftButtonVisibility(View.VISIBLE);
		getBackGroundLayout().setOnLeftButtonImage(getResources().getDrawable(R.drawable.share_left_arrow));
		getBackGroundLayout().setRightButtonVisibility(View.VISIBLE);
		getBackGroundLayout().setRightButtonImage(getResources().getDrawable(R.drawable.share_more_small));
		getBackGroundLayout().setShareButtonVisibility(View.GONE);
//		getBackGroundLayout().setTitleBackgroundAlpha(0.5f);

//        getBackgroundLayout().setRightShareButtonImage();

		getBackGroundLayout().setOnLeftButtonClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityTaskManager.getInstance().removeAllSecondActivity();
			}
		});
		//更多
		getBackGroundLayout().setOnRightButtonClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(BaseDroidApp.getInstance().isLogin()){
					isLoginMoreBack = true;
				}else {
					isLogoutMoreBack = true;
				}
				Intent intent = new Intent(IsForexTwoWayTreasureNewActivity.this, IsForexMoreActivity.class);
				startActivity(intent);
			}
		});


		getBackGroundLayout().setOnShareButtonClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopPollingFlag();
	}

	@Override
	protected void onStop() {
		super.onStop();
		stopPollingFlag();
	}

	@Override
	protected void onPause() {
		super.onPause();
		stopPollingFlag();
	}

	private void initUi(){
//		btn_base_back = (Button) findViewById(R.id.btn_base_back);
//		tv_base_title = (TextView) findViewById(R.id.tv_base_title); //标题
//		btn_share = (Button) findViewById(R.id.btn_share);//分享
//		btn_more = (Button) findViewById(R.id.btn_more);//更多菜单

		getBackGroundLayout().setContentLayout(true);
		getBackGroundLayout().setTitleBackgroundAlpha(0.0f);
		final NewScrollView sc =(NewScrollView)findViewById(R.id.sc);
		getBackGroundLayout().postDelayed(new Runnable() {
			@Override
			public void run() {
				sc.setPreScrollHeight(getBackGroundLayout().getTitleHeight());
			}
		},300);
		sc.setINewScrollViewListener(new NewScrollView.INewScrollViewListener() {
			@Override
			public boolean isToTop() {
				if((lv_prms.getChildAt(0) == null) ||
						(lv_prms.getFirstVisiblePosition() == 0 && lv_prms.getChildAt(0).getTop() >= 0)) {
					return true;
				}
				return false;
			}
		});
		/**设置滑动标题背景透明度变化**/
		sc.setIScrollChangedListener(new NewScrollView.IScrollChangedListener() {
			@Override
			public boolean onScrollChanged(int x, int y) {
				float alpha =  0.0f + (float)(1.0*y/sc.getMaxScrollHeight());
				if(y != 0 && alpha > 0.5f){
					getBackGroundLayout().setTitleStyle(NewBackGroundLayout.TitleStyle.Black);
				}else{
					getBackGroundLayout().setTitleStyle(NewBackGroundLayout.TitleStyle.White);
				}
				getBackGroundLayout().setTitleBackground(R.color.lianlong_common_cell_color);
				getBackGroundLayout().setTitleBackgroundAlpha(alpha);
				return true;
			}
		});

		investPriceView = (InvestPriceView) findViewById(R.id.investPriceView);
		investPriceView.setBackGoundWithColor(getResources().getColor(R.color.lianlong_color_2797c1));
		loginOutLayout = (FrameLayout) investPriceView.findViewById(R.id.loginOutLayout);
//		btn_login = (Button) investPriceView.findViewById(R.id.login_bt);

		loginLayout = (FrameLayout) investPriceView.findViewById(R.id.loginLayout);
//		tv_refvalue = (TextView) investPriceView.findViewById(R.id.tv_refvalue);
//		img_open = (ImageView) investPriceView.findViewById(R.id.img_open);
//		ima_help = (FrameLayout) investPriceView.findViewById(R.id.ima_help);
//		amount_tv1 = (TextView) investPriceView.findViewById(R.id.amount_tv1);
//		amount_tv2 = (TextView) investPriceView.findViewById(R.id.amount_tv2);
		btn_ly = (LinearLayout)findViewById(R.id.btn_ly);
		tv_riseorfall = (TextView) findViewById(R.id.tv_riseorfall);//涨幅跌

		investPriceView.setLoginOutAdvertiseImage(R.drawable.isforex_adv_img);
		investPriceView.setLoginSuccessCallBack(new LoginTask.LoginCallback() {
			@Override
			public void loginStatua(boolean b) {
				if (b) {
					CommonApplication.getInstance().setCurrentAct(IsForexTwoWayTreasureNewActivity.this);
					btn_ly.setVisibility(View.VISIBLE);
					//登陆隐藏
					loginOutLayout.setVisibility(View.GONE);
					getCustomerButton();
					//显示登陆后数据布局
					loginLayout.setVisibility(View.VISIBLE);
					sc.resetAssignMargin(0);
					getBackGroundLayout().setTitleStyle(NewBackGroundLayout.TitleStyle.White);
					getBackGroundLayout().setTitleBackgroundAlpha(0.0f);
					tv_riseorfall.setOnClickListener(null);
					tv_riseorfall.setCompoundDrawables(null, null, null, null);
					//自选显示
					self_ly.setVisibility(View.VISIBLE);
//					commConversationId = (String)BaseDroidApp.getInstance().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
					// 第一次进入自选
					BaseHttpEngine.showProgressDialogCanGoBack();
					coinId=0;
					btnId=3;
					quoteChangeId=0;
					clearTimes();
					clearDate();
					requestPsnAssetBalanceQuery();
//					if (StringUtil.isNull(commConversationId)) {
//						// 请求登录后的CommConversationId
//						requestCommConversationId();
//					} else {
//						// 判断用户是否开通投资理财服务
//						requestPsnInvestmentManageIsOpen();
//					}
				}
			}
		});

		//
		investPriceView.setAmountText("");
		investPriceView.setHelpMessage(getString(R.string.isforex_help));
		investPriceView.setLoginLayoutClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				intent.setClass(IsForexTwoWayTreasureNewActivity.this, IsForexMyRateInfoActivity.class);
				startActivity(intent);
			}
		});

//		lyt_logout_image = (RelativeLayout) findViewById(R.id.lyt_logout_image);//没有登录布局


//		lyt_login_title = (RelativeLayout) findViewById(R.id.lyt_login_title);//已登录布局
//		ima_help = (ImageView) findViewById(R.id.ima_help);//帮助
//		ima_openeye = (ImageView) findViewById(R.id.ima_openeye);//是否显示总金额
//		tv_refvalue = (TextView) findViewById(R.id.tv_refvalue); //
//		tv_totalnum = (TextView) findViewById(R.id.tv_totalnum);//总金额

		if(BaseDroidApp.getInstance().isLogin()){
			btn_ly.setVisibility(View.VISIBLE);
		}else {
			btn_ly.setVisibility(View.GONE);
		}
		btn_query = (Button) findViewById(R.id.btn_query);//交易查询
		btn_accountmanager = (Button)  findViewById(R.id.btn_accountmanager);//账户管理
		btn_recall = (Button) findViewById(R.id.btn_recall);//撤单

		self_ly = (LinearLayout)findViewById(R.id.self_ly);

		btn_selfchose = (Button) findViewById(R.id.btn_selfchose);//自选
		ly_selfchose = (View)findViewById(R.id.ly_selfchose);
		btn_gold = (Button) findViewById(R.id.btn_gold);//贵金属
		ly_gold = (View)findViewById(R.id.ly_gold);
		btn_forex = (Button) findViewById(R.id.btn_forex);//外汇
		ly_forex = (View)findViewById(R.id.ly_forex);
		tv_edit = (TextView) findViewById(R.id.tv_edit);//编辑
		mDownRefreshLayout = (DownRefreshLayout)findViewById(R.id.pull_down_layout);
		mDownRefreshLayout.setOnRefreshListener(this);
		lv_prms = (PullableListView) findViewById(R.id.lv_prms);
		lv_prms.setIsPullUp(false);

		RelativeLayout loarMore = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.isforex_rate_main_footer, null);
		tv_refishtime = (TextView) loarMore.findViewById(R.id.tv_refishtime);//更新时间
		lv_prms.addFooterView(loarMore);
	}

	public void initData(){
//		tv_base_title.setText("双向宝");
		customerRateList = new ArrayList<Map<String, Object>>();
		allRateList = new ArrayList<Map<String, Object>>();
		totalLoginDataList = new ArrayList<Map<String, Object>>();
		totalLogoutDataList = new ArrayList<Map<String, Object>>();
		singleDataMap = new HashMap<String, Object>();
		positionInfoList = new ArrayList<Map<String, Object>>();
		isLogin();

	}

	@Override
	protected void onResume() {
		super.onResume();
		if(BaseDroidApp.getInstance().isLogin()){
			tv_edit.setVisibility(View.VISIBLE);
		}else {
			tv_edit.setVisibility(View.GONE);
		}
//		if(isGoBack){
//			isGoBack = false;
//			clearDateList(true, null);
//			isLight = false;
//			if(BaseDroidApp.getInstance().isLogin()){
//				if(0 == coinId){
//					refreshCustomerData();
//				}else {
//					refreshAllData(vfgType);
//				}
//			}else {
//				refreshGoldData();
////			requestQueryMultipleQuotation(paritiesType, "");
//			}
//		}
		if(isLoginMoreBack){
			isLoginMoreBack = false;
			clearDateList(true, null);
			isLight = false;
			if(0 == coinId){
				refreshCustomerData();
			}else {
				refreshAllData(vfgType);
			}
		}
		if(isLogoutMoreBack){
			isLogoutMoreBack = false;
			clearDateList(true, null);
			isLight = false;
			if(BaseDroidApp.getInstance().isLogin()){
				CommonApplication.getInstance().setCurrentAct(IsForexTwoWayTreasureNewActivity.this);
				btn_ly.setVisibility(View.VISIBLE);
				//登陆隐藏
				loginOutLayout.setVisibility(View.GONE);
				getCustomerButton();
				//显示登陆后数据布局
				loginLayout.setVisibility(View.VISIBLE);
				tv_riseorfall.setOnClickListener(null);
				tv_riseorfall.setCompoundDrawables(null, null, null, null);
				//自选显示
				self_ly.setVisibility(View.VISIBLE);
//				commConversationId = (String)BaseDroidApp.getInstance().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
				// 第一次进入自选
				BaseHttpEngine.showProgressDialog();
				coinId=0;
				btnId=3;
				quoteChangeId=0;
				clearTimes();
				clearDate();
				requestPsnAssetBalanceQuery();
			}else {
				refreshGoldData();
			}
		}
	}
	private void initListener(){
//		btn_base_back.setOnClickListener(backBtnClick);
		//自选
		btn_selfchose.setOnClickListener(this);
		//外汇
		btn_forex.setOnClickListener(this);
		//贵金属
		btn_gold.setOnClickListener(this);
		/**编辑*/
		tv_edit.setOnClickListener(this);
		/**交易查询*/
		btn_query.setOnClickListener(this);
		/**账户管理*/
		btn_accountmanager.setOnClickListener(this);
		/**撤单*/
		btn_recall.setOnClickListener(this);
	}

	/**判断是否登录*/
	private void isLogin(){
		/**
		 * 功能外置修改,以登陆先判断是否开通理财，未登录获取未登录全部行情 用户点击标题栏登陆按钮:
		 * ->登陆成功回调onActivityResult通过ACTIVITY_REQUEST_LOGIN_CODE判断是标题栏入口，
		 * 然后判断是否开通理财进而进行页面数据刷新 ->登陆失败不做任何事情 用户点击买入或卖出后登陆 ->登陆成功先判断是否已经开通理财和设置账户
		 * ->没有开通理财和设置账户，taskTag = 8调用判断理财是否开通 ->已经开通理财和设置账号直接进行交易 ->登陆失败不做任何事情
		 * 启动轮询查询机制在 onActivityResult 通过ISFOREX_TRADE_CODE_ACTIVITY
		 * 判断是从上一个交易页面返回， 如果全部汇率按钮和我的汇率按钮都没有被点击则默认按下我的汇率
		 */
		if(BaseDroidApp.getInstance().isLogin()){
			//布局显示隐藏
			self_ly.setVisibility(View.VISIBLE);
			//登陆隐藏
			loginOutLayout.setVisibility(View.GONE);
			//显示登陆后数据布局
			loginLayout.setVisibility(View.VISIBLE);
			getBackGroundLayout().setLineDividerVisibility(View.GONE);
//			commConversationId = (String)BaseDroidApp.getInstance().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
			// 第一次进入自选
			BaseHttpEngine.showProgressDialogCanGoBack();
			btnId=3;
			requestPsnAssetBalanceQuery();

		}else{
			//登陆显示
			loginOutLayout.setVisibility(View.VISIBLE);
			getBackGroundLayout().setLineDividerVisibility(View.GONE);
			//登陆后数据布局隐藏
			loginLayout.setVisibility(View.GONE);
			//不显示自选
			self_ly.setVisibility(View.GONE);
			//外汇变红
			btn_forex.setTextColor(getResources().getColor(R.color.red));
			ly_forex.setVisibility(View.VISIBLE);
			//编辑 隐藏
			tv_edit.setVisibility(View.GONE);
			//未登录 默认显示贵金属
			btn_gold.performClick();
		}
	}
	@Override
	public void requestPsnAssetBalanceQueryCallBack(Object resultObj) {
		super.requestPsnAssetBalanceQueryCallBack(resultObj);
//        ShowDialogTools.Instance.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(resultObj)) {
			return;
		}
		Map<String, Object> map_result = getHttpTools().getResponseResult(resultObj);
		//双向宝数值
		String pltmAmt = String.valueOf(map_result.get("forexAmt"));
		//+纸钯金
//		String padmAmt = String.valueOf(map_result.get("padmAmt"));
		double totalSum = 0.00;
		if (this.isShowInSheet(pltmAmt)) {
			totalSum = Double.parseDouble(pltmAmt);
		}
//		if (this.isShowInSheet(padmAmt)) {
//			totalSum += Double.parseDouble(padmAmt);
//		}
		if (isShowInSheet(totalSum + "")) {
			investPriceView.setAmountText(totalSum + "");
		} else {
			investPriceView.setAmountText(0 + "");
		}
		if (StringUtil.isNull(commConversationId)) {
			// 请求登录后的CommConversationId
			requestCommConversationId();
		} else {
			// 判断用户是否开通投资理财服务
			requestPsnInvestmentManageIsOpen();
		}
	}
	private boolean isShowInSheet(String amount) {
		try {
			double e = Double.parseDouble(amount);
			return e > 0.0D;
		} catch (Exception var4) {
			return false;
		}
	}

	/** 查询用户定制的汇率-----回调 */
	public void requestPsnVFGCustomerSetRateCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		customerRateList = (List<Map<String, Object>>) biiResponseBody.getResult();
		if (customerRateList == null || customerRateList.size() <= 0) {
			// 用户定制的汇率为空，查询全部汇率 ,查询虚盘全部行情时上送空
//			requestPsnVFGGetAllRate("");
//			requestPsnVFGCustomerSetRate();
			return;
		} else {
			/**自选 设置显示按钮状态  此处疑问 暂时注掉*/
//			getCustomerButton();
//			BaseHttpEngine.dissMissProgressDialog();
			// 我的汇率数据
			getCustomerDate();
			// 7秒刷新
//			refreshCustomerData();
		}
	}
	/** 用户定制的货币对-自选-7秒刷新 */
	private void refreshCustomerData() {
		if (!PollingRequestThread.pollingFlag) {
			requestCustomerRatesPoling();
		}
	}

	/** 7秒刷新 自选 --->我的双向宝*/
	private void requestCustomerRatesPoling() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGCUSTOMERSETRATE_API);
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setParams(null);
		HttpManager.requestPollingBii(biiRequestBody, allHttpHandler,  ConstantGloble.FOREX_REFRESH_TIMES);// 7秒刷新
	}

	/** 自选数据设置 */
	private void getCustomerDate() {
		customerRateList = getTrueDate(customerRateList);
		if (customerRateList == null || customerRateList.size() <= 0) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		isHasCustomerList = true;
		ListUtils.sortForInvest(customerRateList, new ListUtils.ISynchronousList<Map<String, Object>, KeyAndValueItem>() {
			@Override
			public boolean doSomeThing(Map<String, Object> stringObjectMap, KeyAndValueItem keyAndValueItem) {
				String sourceCurrencyCode = (String) stringObjectMap.get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
				String targetCurrencyCode = (String) stringObjectMap.get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
				if(keyAndValueItem.getKey().equals(sourceCurrencyCode) && keyAndValueItem.getValue().equals(targetCurrencyCode)){
					stringObjectMap.put("formatNumber",keyAndValueItem.getParam());
					return true;
				}
				return false;
			}
		});
		// 刷新时间
		refreshTimes(customerRateList);
		// 监听事件
//		totalLoginDataList = getTotalDate(customerRateList);
		clearDateList(true, null);
//		BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", null);
		totalLoginDataList = customerRateList;
		getCustomerAdapterListener(customerRateList);
//		totalLoginDataList = customerRateList;
		//请求四方的两次接口 然后合并
		requestQueryMultipleQuotation("F","");
	}

	/** 全部汇率数据 G贵金属F外汇*/
	public void requestPsnVFGGetAllRateCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		allRateList = (List<Map<String, Object>>) biiResponseBody.getResult();
		if (allRateList == null || allRateList.size() <= 0) {
			BaseHttpEngine.dissMissProgressDialog();
			getCustomerAdapterListener(new ArrayList<Map<String, Object>>());//清空
//			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null))
			return;
		}
		// 按钮选中效果
//		selectedAllButton(vfgType)
		// 7秒刷新
//		refreshAllData(vfgType);
//		tradeDateList.addAll(allRateList);
//		BaseHttpEngine.dissMissProgressDialog();
		// 全部汇率数据
		selectedAllRates();
	}

	/** 选择全部汇率按钮 */
	private void selectedAllRates() {
		// 处理数据
		allRateList = getTrueDate(allRateList);
		if (allRateList == null || allRateList.size() <= 0) {
			return;
		}
		isHasAllList = true;
//		getAllAdapterListener(allRateList);
		ListUtils.sortForInvest(allRateList, new ListUtils.ISynchronousList<Map<String, Object>, KeyAndValueItem>() {
			@Override
			public boolean doSomeThing(Map<String, Object> stringObjectMap, KeyAndValueItem keyAndValueItem) {
				String sourceCurrencyCode = (String) stringObjectMap.get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
				String targetCurrencyCode = (String) stringObjectMap.get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
				if(keyAndValueItem.getKey().equals(sourceCurrencyCode) && keyAndValueItem.getValue().equals(targetCurrencyCode)){
					stringObjectMap.put("formatNumber",keyAndValueItem.getParam());
					return true;
				}
				return false;
			}
		});
		// 刷新时间
		refreshTimes(allRateList);
//		totalLoginDataList = getTotalDate(allRateList);
		/**适配器初始化暂时使用同一个方法*/
		clearDateList(true, null);
//		BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", null);
		getCustomerAdapterListener(allRateList);
		totalLoginDataList = allRateList;
		requestQueryMultipleQuotationFirst(vfgType, "");

	}

	/**登陆后按钮被选中的效果*/
	private void selectedAllButton(String paritiesType){
		if(paritiesType.equals("F")){
			//显示外汇列
			btn_forex.setTextColor(getResources().getColor(R.color.red));
			ly_forex.setVisibility(View.VISIBLE);
			btn_selfchose.setTextColor(getResources().getColor(R.color.black));
			ly_selfchose.setVisibility(View.GONE);
			btn_gold.setTextColor(getResources().getColor(R.color.black));
			ly_gold.setVisibility(View.GONE);
		}else if (paritiesType.equals("G")){
			//显示贵金属
			btn_gold.setTextColor(getResources().getColor(R.color.red));
			ly_gold.setVisibility(View.VISIBLE);
			btn_selfchose.setTextColor(getResources().getColor(R.color.black));
			ly_selfchose.setVisibility(View.GONE);
			btn_forex.setTextColor(getResources().getColor(R.color.black));
			ly_forex.setVisibility(View.GONE);
		}
	}
	/** 全部汇率-7秒刷新 */
	private void refreshAllData(String vfgType) {
		if (!PollingRequestThread.pollingFlag) {
			requestAllRatesPoling(vfgType);
		}
	}
	/** 7秒刷新-全部汇率 --->外汇*/
	private void requestAllRatesPoling(String vfgType) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		Map<String, String> params = new HashMap<String, String>();
		params.put("vfgType", vfgType);
		biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGGETALLRATE_API);
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setParams(params);
		HttpManager.requestPollingBii(biiRequestBody, allHttpHandler,  ConstantGloble.FOREX_REFRESH_TIMES);// 7秒刷新
	}

	private Handler allHttpHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// http状态码
			String resultHttpCode = (String) ((Map<String, Object>) msg.obj).get(ConstantGloble.HTTP_RESULT_CODE);
			// 返回数据
			Object resultObj = ((Map<String, Object>) msg.obj).get(ConstantGloble.HTTP_RESULT_DATA);
			// 回调对象
			HttpObserver callbackObject = (HttpObserver) ((Map<String, Object>) msg.obj)
					.get(ConstantGloble.HTTP_CALLBACK_OBJECT);
			// 回调方法
			String callBackMethod = (String) ((Map<String, Object>) msg.obj).get(ConstantGloble.HTTP_CALLBACK_METHOD);
			switch (msg.what) {
				// 正常http请求数据返回
				case ConstantGloble.HTTP_STAGE_CONTENT:
					/** 执行全局前拦截器 */
					if (BaseDroidApp.getInstanse().httpRequestCallBackPre(resultObj)) {
						break;
					}

					/** 执行callbackObject回调前拦截器 */
					if (httpRequestCallBackPre(resultObj)) {
						break;
					}
					// 清空更新时间
//					clearTimes();
					// 清空数据
//					clearDate();
					BiiResponse biiResponse = (BiiResponse) ((Map<String, Object>) msg.obj).get(ConstantGloble.HTTP_RESULT_DATA);
					List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
					BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
					if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
						return;
					}
					switch (coinId) {
						case 0://自选
							// 得到customerRateList
							customerRateList = (List<Map<String, Object>>) biiResponseBody.getResult();
							if (customerRateList == null || customerRateList.size() <= 0) {
								isHasCustomerList = false;
								return;
							} else {
								// 将得到的数据进行处理
								customerRateList = getTrueDate(customerRateList);
								if (customerRateList == null || customerRateList.size() <= 0) {
									isHasCustomerList = false;
									return;
								}
								ListUtils.sortForInvest(customerRateList, new ListUtils.ISynchronousList<Map<String, Object>, KeyAndValueItem>() {
									@Override
									public boolean doSomeThing(Map<String, Object> stringObjectMap, KeyAndValueItem keyAndValueItem) {
										String sourceCurrencyCode = (String) stringObjectMap.get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
										String targetCurrencyCode = (String) stringObjectMap.get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
										if(keyAndValueItem.getKey().equals(sourceCurrencyCode) && keyAndValueItem.getValue().equals(targetCurrencyCode)){
											stringObjectMap.put("formatNumber",keyAndValueItem.getParam());
											return true;
										}
										return false;
									}
								});
								isLight = true;
//								getCustomerAdapterListener(customerRateList);
								//自选请求四方接口
								requestQueryMultipleRefreshf("F","");
							}
							break;
						case 1:// 全部外汇--->外汇
							allRateList = (List<Map<String, Object>>) biiResponseBody.getResult();
							if (allRateList == null || allRateList.size() <= 0) {
								isHasAllList = false;
								return;
							} else {
								/**暂时不知道用处 暂时注掉 zxj*/
//								if (tradeDateList != null && !tradeDateList.isEmpty()) {
//									tradeDateList.clear();
//								}
//								tradeDateList.addAll(allRateList);
								// 将得到的数据进行处理
								allRateList = getTrueDate(allRateList);
								ListUtils.sortForInvest(allRateList, new ListUtils.ISynchronousList<Map<String, Object>, KeyAndValueItem>() {
									@Override
									public boolean doSomeThing(Map<String, Object> stringObjectMap, KeyAndValueItem keyAndValueItem) {
										String sourceCurrencyCode = (String) stringObjectMap.get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
										String targetCurrencyCode = (String) stringObjectMap.get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
										if(keyAndValueItem.getKey().equals(sourceCurrencyCode) && keyAndValueItem.getValue().equals(targetCurrencyCode)){
											stringObjectMap.put("formatNumber",keyAndValueItem.getParam());
											return true;
										}
										return false;
									}
								});
								if (allRateList == null || allRateList.size() <= 0) {
									isHasAllList = false;
									return;
								}
								String pSort = null;
								switch (quoteChangeId){
									case 0:
										//外汇请求四方接口
										isLight = true;
//										getCustomerAdapterListener(allRateList);
										requestQueryMultipleRefreshf("F","");
										break;
									case 1:
										pSort = "UP";
										//外汇请求四方接口
										requestQueryMultipleRefreshf("F",pSort);
										break;
									case 2:
										pSort = "DN";
										//外汇请求四方接口
										requestQueryMultipleRefreshf("F",pSort);
										break;
									default:
										break;
								}

							}
							break;
						case 2://贵金属
//					goldRateList = (List<Map<String, Object>>) biiResponseBody.getResult();
//					if(goldRateList == null || goldRateList.size() <=0){
//						isHasGoldList = false;
//						return ;
//					}else {
//
//					}

							allRateList = (List<Map<String, Object>>) biiResponseBody.getResult();
							if (allRateList == null || allRateList.size() <= 0) {
								isHasAllList = false;
								return;
							}else{
								/**不知道用处 暂时注掉 zxj*/
//								if (tradeDateList != null && !tradeDateList.isEmpty()) {
//									tradeDateList.clear();
//								}
//								tradeDateList.addAll(allRateList);
								// 将得到的数据进行处理
								allRateList = getTrueDate(allRateList);
								ListUtils.sortForInvest(allRateList, new ListUtils.ISynchronousList<Map<String, Object>, KeyAndValueItem>() {
									@Override
									public boolean doSomeThing(Map<String, Object> stringObjectMap, KeyAndValueItem keyAndValueItem) {
										String sourceCurrencyCode = (String) stringObjectMap.get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
										String targetCurrencyCode = (String) stringObjectMap.get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
										if(keyAndValueItem.getKey().equals(sourceCurrencyCode) && keyAndValueItem.getValue().equals(targetCurrencyCode)){
											stringObjectMap.put("formatNumber",keyAndValueItem.getParam());
											return true;
										}
										return false;
									}
								});
								if (allRateList == null || allRateList.size() <= 0) {
									isHasAllList = false;
									return;
								}
								String pSort = null;
								switch (quoteChangeId){
									case 0:
//										ListUtils.sortForInvest(allRateList, new ListUtils.ISynchronousList<Map<String, Object>, KeyAndValueItem>() {
//											@Override
//											public boolean doSomeThing(Map<String, Object> stringObjectMap, KeyAndValueItem keyAndValueItem) {
//												String sourceCurrencyCode = (String) stringObjectMap.get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
//												String targetCurrencyCode = (String) stringObjectMap.get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
//												if(keyAndValueItem.getKey().equals(sourceCurrencyCode) && keyAndValueItem.getValue().equals(targetCurrencyCode)){
//													stringObjectMap.put("formatNumber",keyAndValueItem.getParam());
//													return true;
//												}
//												return false;
//											}
//										});
										isLight = true;
//										getCustomerAdapterListener(allRateList);
										requestQueryMultipleRefreshf("G","");
										break;
									case 1:
										pSort = "UP";
										//贵金属请求四方接口
										requestQueryMultipleRefreshf("G",pSort);
										break;
									case 2:
										pSort = "DN";
										//贵金属请求四方接口
										requestQueryMultipleRefreshf("G",pSort);
										break;
									default:
										break;
								}
							}
							break;
						default:
							break;
					}
					/** 执行callbackObject回调后拦截器 */
					if (httpRequestCallBackAfter(resultObj)) {
						break;
					}

					/** 执行全局后拦截器 */
					if (BaseDroidApp.getInstanse().httpRequestCallBackAfter(resultObj)) {
						break;
					}
					break;

				// 请求失败错误情况处理
				case ConstantGloble.HTTP_STAGE_CODE:
					/** 执行code error 全局前拦截器 */
					if (BaseDroidApp.getInstanse().httpCodeErrorCallBackPre(resultHttpCode)) {
						break;
					}

					/** 执行callbackObject error code 回调前拦截器 */
					if (callbackObject.httpCodeErrorCallBackPre(resultHttpCode)) {
						break;
					}
					Method httpCodeCallbackMethod = null;
					try {
						// 回调
						httpCodeCallbackMethod = callbackObject.getClass().getMethod(callBackMethod, String.class);
						httpCodeCallbackMethod.invoke(callbackObject, resultHttpCode);
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
						// add by wez 2012.11.06
						LogGloble.e(TAG, "NullPointerException ", e);
						throw e;
					} catch (ClassCastException e) {
						// add by wez 2012.11.06
						LogGloble.e(TAG, "ClassCastException ", e);
						throw e;
					}

					/** 执行code error 全局后拦截器 */
					if (BaseDroidApp.getInstanse().httpCodeErrorCallBackAfter(resultHttpCode)) {
						break;
					}

					/** 执行callbackObject code error 后拦截器 */
					if (callbackObject.httpCodeErrorCallBackAfter(resultHttpCode)) {
						break;
					}
					break;
				default:
					break;
			}
		}
	};
	private void clearQueryMultiplelist(){
		if(!StringUtil.isNullOrEmpty(queryMultiplelist)){
			queryMultiplelist.clear();
		}
	}
	/**多笔行情查询*/
	protected  void requestQueryMultipleRefreshf(String flag, String pSort){
		QueryMultipleQuotationRequestParams params = new QueryMultipleQuotationRequestParams(flag, "M", pSort);
		params.setCardType(flag);
		params.setCardClass("M");
		params.setpSort(pSort);
		HttpHelp httpHelp = HttpHelp.getInstance();
		httpHelp.postHttpFromSF(this, params);
		if(quoteChangeId==0){
			httpHelp.setHttpErrorCallBack(new IHttpErrorCallBack() {
				@Override
				public boolean onError(String exceptionMessage, Object extendParams) {
					//刷新 ，自选
					switch (coinId){
						case 0://自选刷新
							clearQueryMultiplelist();
							requestQueryMultipleRefreshg("G","");//自选请求贵金属 四方接口请求
							break;
						case 1://外汇刷新
							clearQueryMultiplelist();
							if(BaseDroidApp.getInstance().isLogin()){
								//保存本次所有请求回来的数据
								clearDateList(false,adapter.getSourceList());
								//合并数据
								totalLoginDataList = getTotalDate(allRateList);

								refreshTimes(totalLoginDataList);
								isDelay(isHasAllList, totalLoginDataList);
//								if (isHasAllList) {
////									adapter.dataChaged(allRateList);
//									adapter.setSourceList(totalLoginDataList,0);
//								} else {
//									isHasAllList = true;
////									getAllAdapterListener(allRateList);
//									/**适配器初始化暂时用同一个方法*/
//									getCustomerAdapterListener(totalLoginDataList);
//								}
								//保存本次所有请求回来的数据
//								clearDateList(false, totalLoginDataList);
//							BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", totalLoginDataList);
							}else {
								//保存本次所有请求回来的数据
								clearDateList(false,adapter.getSourceList());
								//合并数据
								totalLogoutDataList = getTotalDate(allRateList);
								refreshTimes(totalLogoutDataList);
								isDelay(isHasAllList, totalLogoutDataList);
//								if (isHasAllList) {
////									adapter.dataChaged(allRateList);
//									adapter.setSourceList(totalLogoutDataList,0);
//								} else {
//									isHasAllList = true;
////									getAllAdapterListener(allRateList);
//									/**适配器初始化暂时用同一个方法*/
//									getCustomerAdapterListener(totalLogoutDataList);
//								}
								//保存本次所有请求回来的数据
//								clearDateList(false,totalLogoutDataList);
//							BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLogoutDataList", totalLogoutDataList);
							}

							break;
						case 2://贵金属刷新
							clearQueryMultiplelist();
							if(BaseDroidApp.getInstance().isLogin()){
								//保存本次所有请求回来的数据
								clearDateList(false,adapter.getSourceList());
								//合并数据
								totalLoginDataList = getTotalDate(allRateList);
								refreshTimes(totalLoginDataList);
								isDelay(isHasAllList, totalLoginDataList);
//								if (isHasAllList) {
//									adapter.setSourceList(totalLoginDataList,0);
//								} else {
//									isHasAllList = true;
////									getAllAdapterListener(allRateList);
//									/**适配器初始化暂时使用同一个方法*/
//									getCustomerAdapterListener(totalLoginDataList);
//								}
								//保存本次所有请求回来的数据
//								clearDateList(false, totalLoginDataList);
//							BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", totalLoginDataList);
							}else {
								//保存本次所有请求回来的数据
								clearDateList(false,adapter.getSourceList());
								//合并数据
								totalLogoutDataList = getTotalDate(allRateList);
								refreshTimes(totalLogoutDataList);
								isDelay(isHasAllList, totalLogoutDataList);
//								if (isHasAllList) {
//									adapter.setSourceList(totalLogoutDataList,0);
//								} else {
//									isHasAllList = true;
////									getAllAdapterListener(allRateList);
//									/**适配器初始化暂时使用同一个方法*/
//									getCustomerAdapterListener(totalLogoutDataList);
//								}
								//保存本次所有请求回来的数据
//								clearDateList(false, totalLogoutDataList);
//							BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLogoutDataList", totalLogoutDataList);
							}
							break;
						default:
							break;
					}
					return true;
				}
			});
			httpHelp.setOkHttpErrorCode(new IOkHttpErrorCode() {
				@Override
				public boolean handlerErrorCode(BaseResponseData responseData, Object extendParams) {
					//刷新 ，自选
					switch (coinId){
						case 0://自选刷新
							clearQueryMultiplelist();
							requestQueryMultipleRefreshg("G","");//自选请求贵金属 四方接口请求
							break;
						case 1://外汇刷新
							clearQueryMultiplelist();
							if(BaseDroidApp.getInstance().isLogin()){
								//保存本次所有请求回来的数据
								clearDateList(false,adapter.getSourceList());
								//合并数据
								totalLoginDataList = getTotalDate(allRateList);
								refreshTimes(totalLoginDataList);
								isDelay(isHasAllList, totalLoginDataList);
//								if (isHasAllList) {
////									adapter.dataChaged(allRateList);
//									adapter.setSourceList(totalLoginDataList,0);
//								} else {
//									isHasAllList = true;
////									getAllAdapterListener(allRateList);
//									/**适配器初始化暂时用同一个方法*/
//									getCustomerAdapterListener(totalLoginDataList);
//								}
								//保存本次所有请求回来的数据
//								clearDateList(false, totalLoginDataList);
//							BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", totalLoginDataList);
							}else {
								//保存本次所有请求回来的数据
								clearDateList(false,adapter.getSourceList());
								//合并数据
								totalLogoutDataList = getTotalDate(allRateList);
								refreshTimes(totalLogoutDataList);
								isDelay(isHasAllList, totalLogoutDataList);
//								if (isHasAllList) {
////									adapter.dataChaged(allRateList);
//									adapter.setSourceList(totalLogoutDataList,0);
//								} else {
//									isHasAllList = true;
////									getAllAdapterListener(allRateList);
//									/**适配器初始化暂时用同一个方法*/
//									getCustomerAdapterListener(totalLogoutDataList);
//								}
								//保存本次所有请求回来的数据
//								clearDateList(false,totalLogoutDataList);
//							BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLogoutDataList", totalLogoutDataList);
							}

							break;
						case 2://贵金属刷新
							clearQueryMultiplelist();
							if(BaseDroidApp.getInstance().isLogin()){
								//保存本次所有请求回来的数据
								clearDateList(false,adapter.getSourceList());
								//合并数据
								totalLoginDataList = getTotalDate(allRateList);
								refreshTimes(totalLoginDataList);
								isDelay(isHasAllList, totalLoginDataList);
//								if (isHasAllList) {
//									adapter.setSourceList(totalLoginDataList,0);
//								} else {
//									isHasAllList = true;
////									getAllAdapterListener(allRateList);
//									/**适配器初始化暂时使用同一个方法*/
//									getCustomerAdapterListener(totalLoginDataList);
//								}
								//保存本次所有请求回来的数据
//								clearDateList(false, totalLoginDataList);
//							BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", totalLoginDataList);
							}else {
								//保存本次所有请求回来的数据
								clearDateList(false,adapter.getSourceList());
								//合并数据
								totalLogoutDataList = getTotalDate(allRateList);
								refreshTimes(totalLogoutDataList);
								isDelay(isHasAllList, totalLogoutDataList);
//								if (isHasAllList) {
//									adapter.setSourceList(totalLogoutDataList,0);
//								} else {
//									isHasAllList = true;
////									getAllAdapterListener(allRateList);
//									/**适配器初始化暂时使用同一个方法*/
//									getCustomerAdapterListener(totalLogoutDataList);
//								}
								//保存本次所有请求回来的数据
//								clearDateList(false, totalLogoutDataList);
//							BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLogoutDataList", totalLogoutDataList);
							}
							break;
						default:
							break;
					}
					return true;
				}
			});
		}else {
			httpHelp.setHttpErrorCallBack(null);
			httpHelp.setOkHttpErrorCode(null);
		}
		httpHelp.setHttpResponseCallBack(new IHttpResponseCallBack() {
			@Override
			public boolean responseCallBack(String response, Object extendParams) {
				QueryMultipleQuotationResponseData result = GsonTools.fromJson(response,QueryMultipleQuotationResponseData.class);
				QueryMultipleQuotationResult body = result.getBody();
				clearQueryMultiplelist();
				queryMultiplelist = body.getItem();
				//刷新 ，自选
				switch (coinId){
					case 0://自选刷新
						requestQueryMultipleRefreshg("G","");//自选请求贵金属 四方接口请求
						break;
					case 1://外汇刷新
						if(BaseDroidApp.getInstance().isLogin()){
							//保存本次所有请求回来的数据
							clearDateList(false,adapter.getSourceList());
							//合并数据
							totalLoginDataList = getTotalDate(allRateList);
							refreshTimes(totalLoginDataList);
							isLight = true;
							isDelay(isHasAllList, totalLoginDataList);
//							if (isHasAllList) {
////									adapter.dataChaged(allRateList);
//								adapter.setSourceList(totalLoginDataList,0);
//							} else {
//								isHasAllList = true;
////									getAllAdapterListener(allRateList);
//								/**适配器初始化暂时用同一个方法*/
//								getCustomerAdapterListener(totalLoginDataList);
//							}
							//保存本次所有请求回来的数据
//							clearDateList(false, totalLoginDataList);
//							BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", totalLoginDataList);
						}else {
							//保存本次所有请求回来的数据
							clearDateList(false,adapter.getSourceList());
							//合并数据
							totalLogoutDataList = getTotalDate(allRateList);
							refreshTimes(totalLogoutDataList);
							isLight = true;
							isDelay(isHasAllList, totalLogoutDataList);
//							if (isHasAllList) {
////									adapter.dataChaged(allRateList);
//								adapter.setSourceList(totalLogoutDataList,0);
//							} else {
//								isHasAllList = true;
////									getAllAdapterListener(allRateList);
//								/**适配器初始化暂时用同一个方法*/
//								getCustomerAdapterListener(totalLogoutDataList);
//							}
							//保存本次所有请求回来的数据
//							clearDateList(false, totalLogoutDataList);
//							BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLogoutDataList", totalLogoutDataList);
						}

						break;
					case 2://贵金属刷新
						if(BaseDroidApp.getInstance().isLogin()){
							//保存本次所有请求回来的数据
							clearDateList(false,adapter.getSourceList());
							//合并数据
							totalLoginDataList = getTotalDate(allRateList);
							refreshTimes(totalLoginDataList);
							isLight = true;
							isDelay(isHasAllList, totalLoginDataList);
//							if (isHasAllList) {
//								adapter.setSourceList(totalLoginDataList,0);
//							} else {
//								isHasAllList = true;
////									getAllAdapterListener(allRateList);
//								/**适配器初始化暂时使用同一个方法*/
//								getCustomerAdapterListener(totalLoginDataList);
//							}
							//保存本次所有请求回来的数据
//							clearDateList(false, totalLoginDataList);
//							BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", totalLoginDataList);
						}else {
							//保存本次所有请求回来的数据
							clearDateList(false,adapter.getSourceList());
							//合并数据
							totalLogoutDataList = getTotalDate(allRateList);
							refreshTimes(totalLogoutDataList);
							isLight = true;
							isDelay(isHasAllList, totalLogoutDataList);
//							if (isHasAllList) {
//								adapter.setSourceList(totalLogoutDataList,0);
//							} else {
//								isHasAllList = true;
////									getAllAdapterListener(allRateList);
//								/**适配器初始化暂时使用同一个方法*/
//								getCustomerAdapterListener(totalLogoutDataList);
//							}
							//保存本次所有请求回来的数据
//							clearDateList(false, totalLogoutDataList);
//							BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLogoutDataList", totalLogoutDataList);
						}
						break;
					default:
						break;
				}
				mDownRefreshLayout.loadmoreCompleted(RefreshDataStatus.Successed);
				return false;
			}
		});
	}
	/**多笔行情查询*/
	protected  void requestQueryMultipleRefreshg(String flag, String pSort){
		QueryMultipleQuotationRequestParams params = new QueryMultipleQuotationRequestParams(flag, "M", pSort);
		params.setCardType(flag);
		params.setCardClass("M");
		params.setpSort(pSort);
		HttpHelp httpHelp = HttpHelp.getInstance();
		httpHelp.postHttpFromSF(this, params);
		httpHelp.setHttpErrorCallBack(new IHttpErrorCallBack() {
			@Override
			public boolean onError(String exceptionMessage, Object extendParams) {
				BaseHttpEngine.dissMissProgressDialog();
//					clearQueryMultiplelist();
				//保存本次所有请求回来的数据
				clearDateList(false,adapter.getSourceList());
				//刷新自选请求接口并，合并
				totalLoginDataList = getTotalDate(customerRateList);
				refreshTimes(totalLoginDataList);
				isDelay(isHasCustomerList, totalLoginDataList);
//					if (isHasCustomerList) {
//						adapter.setSourceList(totalLoginDataList, 0);
//					} else {
//						isHasCustomerList = true;
//						getCustomerAdapterListener(totalLoginDataList);
//					}
				//保存省联号
				String ibkNum = (String) customerRateList.get(0).get(IsForex.ISFOREX_ibkNum);
				LocalDataService.getInstance().saveIbkNum(ConstantGloble.IsForex,ibkNum);
				//保存本次所有请求回来的数据
//					clearDateList(false, totalLoginDataList);
//					BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", totalLoginDataList);
				return true;
			}
		});
		httpHelp.setOkHttpErrorCode(new IOkHttpErrorCode() {
			@Override
			public boolean handlerErrorCode(BaseResponseData responseData, Object extendParams) {
				BaseHttpEngine.dissMissProgressDialog();
//				clearQueryMultiplelist();
				//保存本次所有请求回来的数据
				clearDateList(false,adapter.getSourceList());
				//刷新自选请求接口并，合并
				totalLoginDataList = getTotalDate(customerRateList);
				refreshTimes(totalLoginDataList);
				isDelay(isHasCustomerList, totalLoginDataList);
//				if (isHasCustomerList) {
//					adapter.setSourceList(totalLoginDataList, 0);
//				} else {
//					isHasCustomerList = true;
//					getCustomerAdapterListener(totalLoginDataList);
//				}
				//保存省联号
				String ibkNum = (String) customerRateList.get(0).get(IsForex.ISFOREX_ibkNum);
				LocalDataService.getInstance().saveIbkNum(ConstantGloble.IsForex,ibkNum);
				//保存本次所有请求回来的数据
//				clearDateList(false, totalLoginDataList);
//					BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", totalLoginDataList);
				return true;
			}
		});
		httpHelp.setHttpResponseCallBack(new IHttpResponseCallBack() {
			@Override
			public boolean responseCallBack(String response, Object extendParams) {
				QueryMultipleQuotationResponseData result = GsonTools.fromJson(response,QueryMultipleQuotationResponseData.class);
				QueryMultipleQuotationResult body = result.getBody();
				if(StringUtil.isNullOrEmpty(queryMultiplelist)){
					queryMultiplelist = body.getItem();
				}else {
					queryMultiplelist.addAll(body.getItem());
				}
				BaseHttpEngine.dissMissProgressDialog();
				//保存本次所有请求回来的数据
				clearDateList(false,adapter.getSourceList());
				//刷新自选请求接口并，合并
				totalLoginDataList = getTotalDate(customerRateList);
				// 清空更新时间
				clearTimes();
				refreshTimes(totalLoginDataList);
				isDelay(isHasCustomerList, totalLoginDataList);
//				if (isHasCustomerList) {
//					adapter.setSourceList(totalLoginDataList, 0);
//				} else {
//					isHasCustomerList = true;
//					getCustomerAdapterListener(totalLoginDataList);
//				}
				//保存省联号
				String ibkNum = (String) customerRateList.get(0).get(IsForex.ISFOREX_ibkNum);
				LocalDataService.getInstance().saveIbkNum(ConstantGloble.IsForex,ibkNum);
				//保存本次所有请求回来的数据
//				clearDateList(false, totalLoginDataList);
//					BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", totalLoginDataList);
//				else if("0".equals(coinId)){
//					requestCustomerSetRate();
//				}else {
//					//刷新自选请求接口并，合并
//					totalLoginDataList = getTotalDate(customerRateList);
//					refreshTimes(totalLoginDataList);
//
//					if (isHasCustomerList) {
//						adapter.setSourceList(totalLoginDataList, 0);
//					} else {
//						isHasCustomerList = true;
//						getCustomerAdapterListener(totalLoginDataList);
//					}
//					//保存省联号
//					String ibkNum = (String) customerRateList.get(0).get(IsForex.ISFOREX_ibkNum);
//					LocalDataService.getInstance().saveIbkNum(ConstantGloble.IsForex,ibkNum);
//					//保存本次所有请求回来的数据
//					BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", totalLoginDataList);
//				}
				return false;
			}
		});
	}
	Handler handler = new Handler();
	/**每次刷新完成后 200ms延迟不高亮*/
	private void isDelay(boolean adapterIndentification, List<Map<String, Object>> dateList ){
		isStartAnimator = true;
		if (adapterIndentification) {
			adapter.setSourceList(dateList, 0);
		} else {
			switch (coinId){
				case 0: //自选
					isHasCustomerList = true;
					break;
				case 1://贵金属
					isHasAllList = true;
					break;
				case 2://外汇
					isHasAllList = true;
					break;
				default:
					break;
			}
			getCustomerAdapterListener(dateList);
		}
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				isStartAnimator = false;
			}
		},200);
	}


	/**初始化适配器*/
	private void getCustomerAdapterListener(List<Map<String, Object>> customerRateList){
		if(adapter == null){
			adapter = new CommonAdapter<Map<String, Object>>(IsForexTwoWayTreasureNewActivity.this, customerRateList, this);
//			adapter = new InvestAdaper(this,null);
			lv_prms.setAdapter(adapter);
		}else {
			adapter.setSourceList(customerRateList,0);
		}

	}

	/** 处理货币对，返回的货币对可能没有对应的名称，将其除去 */
	private List<Map<String, Object>> getTrueDate(List<Map<String, Object>> list) {
		int len = list.size();
		List<Map<String, Object>> dateList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < len; i++) {
			Map<String, Object> map = (Map<String, Object>) list.get(i);
			// 得到源货币的代码
			String sourceCurrencyCode = (String) map.get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
			String targetCurrencyCode = (String) map.get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
			if (!StringUtil.isNull(sourceCurrencyCode) && !StringUtil.isNull(targetCurrencyCode)
					&& LocalData.Currency.containsKey(sourceCurrencyCode) && LocalData.Currency.containsKey(targetCurrencyCode)) {
				dateList.add(map);
			}
		}
		return dateList;
	}
	/**合并数据*/
	private List<Map<String, Object>> getTotalDate(List<Map<String, Object>> allRateList){
		List<Map<String, Object>> dateList = new ArrayList<Map<String, Object>>();
		if(quoteChangeId == 0){
			//按BII接口排序
			for (int i=0; i<allRateList.size(); i++){
				Map<String, Object> rateMap = allRateList.get(i);
				String sourceCurrencyCode = (String) rateMap.get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
				String targetCurrencyCode = (String) rateMap.get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
				if(StringUtil.isNullOrEmpty(queryMultiplelist)){
					rateMap.put("ccygrpNm", null);
//						rateMap.put("buyRate", commMap.buyPrice);
//						rateMap.put("sellRate", commMap.sellPrice);
					rateMap.put("currPercentDiff", null);
					rateMap.put("currDiff", null);
					rateMap.put("priceTime", null);
					rateMap.put("tranCode", null);
					rateMap.put("sortPriority", null);
					dateList.add(rateMap);
				}else {

					for(int j=0; j<queryMultiplelist.size(); j++){
						QueryMultipleQuotationResult.QueryMultipleQuotationItem commMap = queryMultiplelist.get(j);
						String sourceCurrencyCode1 = commMap.getSourceCurrencyCode();
						String targetCurrencyCode1 = commMap.getTargetCurrencyCode();
						if(sourceCurrencyCode.equals(sourceCurrencyCode1)&& targetCurrencyCode.equals(targetCurrencyCode1)){
//						添加四方借口字段参数
							rateMap.put("ccygrpNm", commMap.ccygrpNm);
//						rateMap.put("buyRate", commMap.buyPrice);
//						rateMap.put("sellRate", commMap.sellPrice);
							rateMap.put("currPercentDiff", commMap.currPercentDiff);
							rateMap.put("currDiff", commMap.currDiff);
							rateMap.put("priceTime", commMap.priceTime);
							rateMap.put("tranCode", commMap.tranCode);
							rateMap.put("sortPriority", commMap.sortPriority);
							break;
						}else{
							rateMap.put("ccygrpNm", null);
							rateMap.put("currPercentDiff", null);
							rateMap.put("currDiff", null);
							rateMap.put("priceTime", null);
							rateMap.put("tranCode", null);
							rateMap.put("sortPriority", null);
						}
					}
					dateList.add(rateMap);
				}

			}
		}else {
			//按四方接口排序
			for(int i=0; i<queryMultiplelist.size(); i++){
				QueryMultipleQuotationResult.QueryMultipleQuotationItem rateMap = queryMultiplelist.get(i);
				Map<String, Object> rateResultMap = new HashMap<String, Object>();
				String sourceCurrencyCode1 = (String)rateMap.getSourceCurrencyCode();
				String targetCurrencyCode1 = (String)rateMap.getTargetCurrencyCode();
				for(int j=0; j<allRateList.size(); j++){
					Map<String, Object> commMap = allRateList.get(j);
					String sourceCurrencyCode = (String) commMap.get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
					String targetCurrencyCode = (String) commMap.get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
					if(sourceCurrencyCode.equals(sourceCurrencyCode1)&& targetCurrencyCode.equals(targetCurrencyCode1)){
						rateResultMap.putAll(commMap);
						rateResultMap.put("ccygrpNm", rateMap.ccygrpNm);
//						rateResultMap.put("buyRate", rateMap.buyPrice);
//						rateResultMap.put("sellRate", rateMap.sellPrice);
						rateResultMap.put("currPercentDiff", rateMap.currPercentDiff);
						rateResultMap.put("currDiff", rateMap.currDiff);
						rateResultMap.put("priceTime", rateMap.priceTime);
						rateResultMap.put("tranCode", rateMap.tranCode);
						rateResultMap.put("sortPriority", rateMap.sortPriority);
						dateList.add(rateResultMap);
						break;
					}
				}
			}
		}
		return dateList;
	}
	/** 刷新时间 */
	private void refreshTimes(List<Map<String, Object>> list) {
		if(list == null || list.size() <=0  ){
			return;
		}
		Map<String, Object> map = list.get(0);
		String times = (String) map.get(IsForex.ISFOREX_RATE_UPDATEDATE_RES);
		tv_refishtime.setText("数据更新于北京时间"+times+"，具体价格以实际成交为准");
	}
	/**每次查询前清空更新时间*/
	private void clearTimes(){
		tv_refishtime.setText("");
	}

	/**每次点击按钮前都要清空数据*/
	private void clearDate(){
		if(!StringUtil.isNullOrEmpty(allRateList)){
			allRateList.clear();
			getCustomerAdapterListener(new ArrayList<Map<String, Object>>());
//			adapter.notifyDataSetChanged();
		}
		if(!StringUtil.isNullOrEmpty(customerRateList)){
			customerRateList.clear();
			getCustomerAdapterListener(new ArrayList<Map<String, Object>>());
//			adapter.notifyDataSetChanged();
		}
		if(!StringUtil.isNullOrEmpty(totalLogoutDataList)){
			getCustomerAdapterListener(new ArrayList<Map<String, Object>>());
		}
		if(!StringUtil.isNullOrEmpty(totalLoginDataList)){
			totalLoginDataList.clear();
			getCustomerAdapterListener(new ArrayList<Map<String, Object>>());
		}
		if(!StringUtil.isNullOrEmpty(queryMultiplelist)){
			queryMultiplelist.clear();
		}

	}


	/**返回点击事件*/
//	OnClickListener backBtnClick = new OnClickListener(){
//
//		@Override
//		public void onClick(View view) {
//			finish();
//		}
//	};


	@Override
	public void onClick(View view) {
//		Drawable drawable = getResources().getDrawable(R.drawable.prms_chose);
		switch (view.getId()){
			case R.id.ib_back:
				stopPollingFlag();
				ActivityTaskManager.getInstance().removeAllSecondActivity();
				break;
			case R.id.btn_query://交易查询
				btnId = 0;
				stopPollingFlag();
				if(BaseDroidApp.getInstance().isLogin()){
					Intent prmsQueryintent = new Intent(IsForexTwoWayTreasureNewActivity.this,IsForexQueryMenuActivity.class);
					startActivity(prmsQueryintent);
				}else {
					BaseActivity.getLoginUtils(IsForexTwoWayTreasureNewActivity.this).exe(new LoginTask.LoginCallback() {

						@Override
						public void loginStatua(boolean isLogin) {
							// TODO Auto-geneonrated method stub
							if (isLogin) {
								analyzingConditions();
							}
						}
					});
				}
				break;
			case R.id.btn_accountmanager://保证金交易
				btnId = 1;
				stopPollingFlag();
				//保证金交易
				if(BaseDroidApp.getInstance().isLogin()){
					Intent prmsAccMeneintent = new Intent(IsForexTwoWayTreasureNewActivity.this,IsForexBailInfoActivity.class);
					startActivity(prmsAccMeneintent);
				}else {
					BaseActivity.getLoginUtils(IsForexTwoWayTreasureNewActivity.this).exe(new LoginTask.LoginCallback() {

						@Override
						public void loginStatua(boolean isLogin) {
							// TODO Auto-geneonrated method stub
							if (isLogin) {
								analyzingConditions();
							}
						}
					});
				}
				break;
			case R.id.btn_recall://委托交易查询
				btnId = 2;
				stopPollingFlag();
				if(BaseDroidApp.getInstance().isLogin()){
					BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_QUERYTAG, 5);
					Intent isForexCurrentActivity = new Intent(IsForexTwoWayTreasureNewActivity.this,IsForexCurrenyWTActivity.class);
					startActivity(isForexCurrentActivity);
				}else {
					BaseActivity.getLoginUtils(IsForexTwoWayTreasureNewActivity.this).exe(new LoginTask.LoginCallback() {

						@Override
						public void loginStatua(boolean isLogin) {
							// TODO Auto-geneonrated method stub
							if (isLogin) {
								analyzingConditions();
							}
						}
					});
				}
				break;

			case R.id.btn_selfchose:  //自选点击事件
				tv_riseorfall.setOnClickListener(null);
				tv_riseorfall.setCompoundDrawables(null, null, null, null);
				stopPollingFlag();
				clearDateList(true, null);
				isLight = false;
				clearQueryMultiplelist();
				quoteChangeId =0;
				coinId = 0;
				taskTag = 4;
				customerOrTrade = 2;
				// 自选选中效果
				getCustomerButton();
//					getCustomerButton();
//					clearDate();
//					clearTimes();
				// 查询用户定制的汇率 先请求Bii接口
				BaseHttpEngine.showProgressDialog();
				requestCustomerSetRate();
//					requestQueryMultipleQuotation(vfgType, "");
				break;
			case R.id.btn_forex: //外汇点击事件
				stopPollingFlag();
				clearDateList(true, null);
				isLight = false;
				clearQueryMultiplelist();
				quoteChangeId =0;
				tv_riseorfall.setOnClickListener(this);
				Drawable drawablem = getResources().getDrawable(R.drawable.share_flat);
				drawablem.setBounds(0, 0, drawablem.getMinimumWidth(), drawablem.getMinimumHeight());
				tv_riseorfall.setCompoundDrawables(null, null, drawablem, null);
				coinId = 1;
				clearTimes();
				if(BaseDroidApp.getInstance().isLogin()){
					vfgType = "F";
					selectedAllButton(vfgType);
					taskTag = 4;
					customerOrTrade = 3;
//					clearDate();
//					clearTimes();
					// 查询全部汇率 首先调用Bii接口
					BaseHttpEngine.showProgressDialog();
					requestPsnVFGGetAllRate(vfgType);
//					requestQueryMultipleQuotation(vfgType, "")
				}else {
					paritiesType = "F";
					selectedAllButton(paritiesType);
					requestPsnGetAllExchangeRatesOutlay(paritiesType);
//					requestQueryMultipleQuotation(paritiesType, "")
				}
				break;
			case R.id.btn_gold:
				stopPollingFlag();
				clearDateList(true, null);
				isLight = false;
				clearQueryMultiplelist();
				quoteChangeId =0;
				tv_riseorfall.setOnClickListener(this);
				Drawable drawgold = getResources().getDrawable(R.drawable.share_flat);
				drawgold.setBounds(0, 0, drawgold.getMinimumWidth(), drawgold.getMinimumHeight());
				tv_riseorfall.setCompoundDrawables(null, null, drawgold, null);
				coinId = 2;
				clearTimes();
				if(BaseDroidApp.getInstanse().isLogin()){
					vfgType = "G";
					taskTag = 4;
					selectedAllButton(vfgType);
//					clearDate();
//					clearTimes();
					// 查询贵金属
					BaseHttpEngine.showProgressDialog();
					requestPsnVFGGetAllRate(vfgType);
//					requestQueryMultipleQuotation(vfgType, "");
				}else{
					paritiesType = "G";
					selectedAllButton(paritiesType);
					//不排序的时候 先调用BII的接口
					requestPsnGetAllExchangeRatesOutlay(paritiesType);
//					requestQueryMultipleQuotation(paritiesType, "");
				}
				break;
			case R.id.tv_edit://编辑
				// 跳转到汇率定制页面
				stopPollingFlag();
				Intent mateRateIntent = new Intent(IsForexTwoWayTreasureNewActivity.this, IsForexMakeRateNewActivity.class);
				startActivityForResult(mateRateIntent, ConstantGloble.ISFOREX_TRADE_CODE_ACTIVITY);
				break;
			case R.id.tv_riseorfall:
				tv_riseorfall.setText("涨跌幅");
				/**第一次升序 第二次降序 第三次 不排序*/
				switch (quoteChangeId){
					case 0:
						quoteChangeId = 1;
						clearDateList(true, null);
						Drawable drawablerise = getResources().getDrawable(R.drawable.share_rise);
						drawablerise.setBounds(0, 0, drawablerise.getMinimumWidth(), drawablerise.getMinimumHeight());
						tv_riseorfall.setCompoundDrawables(null, null, drawablerise, null);
						stopPollingFlag();
						isLight = false;
						BaseHttpEngine.showProgressDialog();
						if(BaseDroidApp.getInstance().isLogin()){
							requestQueryMultipleQuotationUpOrDown(vfgType, "UP");
//							requestQueryMultipleQuotation(vfgType, "UP");
						}else {
							requestQueryMultipleQuotationUpOrDown(paritiesType, "UP");
						}
						break;
					case 1:
						quoteChangeId = 2;
						clearDateList(true, null);
						Drawable drawablefall = getResources().getDrawable(R.drawable.share_fall);
						drawablefall.setBounds(0, 0, drawablefall.getMinimumWidth(), drawablefall.getMinimumHeight());
						tv_riseorfall.setCompoundDrawables(null, null, drawablefall, null);
						stopPollingFlag();
						isLight = false;
						BaseHttpEngine.showProgressDialog();
						if(BaseDroidApp.getInstance().isLogin()){
							requestQueryMultipleQuotationUpOrDown(vfgType, "DN");
						}else {
							requestQueryMultipleQuotationUpOrDown(paritiesType, "DN");
						}
						break;
					case 2:
						quoteChangeId = 0;
						clearDateList(true, null);
						Drawable drawable = getResources().getDrawable(R.drawable.share_flat);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
						tv_riseorfall.setCompoundDrawables(null, null, drawable, null);
						stopPollingFlag();
						isLight = false;
						BaseHttpEngine.showProgressDialog();
						if(BaseDroidApp.getInstance().isLogin()){
							requestPsnVFGGetAllRate(vfgType);
//							requestQueryMultipleQuotation(vfgType, "")
						}else {
							requestPsnGetAllExchangeRatesOutlay(paritiesType);
//							requestQueryMultipleQuotation(paritiesType, "");
						}
						break;
					default:
						break;
				}
//				    adapter.notifyDataSetChanged();
				break;
//			case R.id.btn_login:
//				BaseActivity.getLoginUtils(IsForexTwoWayTreasureNewActivity.this).exe(new LoginTask.LoginCallback() {
//
//					@Override
//					public void loginStatua(boolean isLogin) {
//						// TODO Auto-geneonrated method stub
//						if (isLogin) {
//
//						}
//					}
//				});
//				break;
			default:
				break;

		}

	}
	private void requestQueryMultipleQuotationUpOrDown(String flag, String pSort){
		QueryMultipleQuotationRequestParams params = new QueryMultipleQuotationRequestParams(flag, "M", pSort);
		params.setCardType(flag);
		params.setCardClass("M");
		params.setpSort(pSort);
		HttpHelp httpHelp = HttpHelp.getInstance();
		httpHelp.postHttpFromSF(this, params);
		httpHelp.setHttpErrorCallBack(null);
		httpHelp.setHttpResponseCallBack(new IHttpResponseCallBack() {
			@Override
			public boolean responseCallBack(String response, Object extendParams) {
				QueryMultipleQuotationResponseData result = GsonTools.fromJson(response,QueryMultipleQuotationResponseData.class);
				QueryMultipleQuotationResult body = result.getBody();
				clearQueryMultiplelist();
				queryMultiplelist = body.getItem();
				BaseHttpEngine.dissMissProgressDialog();
//				quoteChangeId = 1;
				//进行合并数据 排序 并刷新
				if(BaseDroidApp.getInstance().isLogin()){
					totalLoginDataList = getTotalDate(allRateList);
					adapter.setSourceList(totalLoginDataList, 0);
					refreshAllData(vfgType);
				}else {
					totalLogoutDataList = getTotalDate(allRateList);
					adapter.setSourceList(totalLogoutDataList, 0);
					refreshGoldData();
				}

				return false;
			}
		});
	}
	/**交易查询 账户管理 贵金属登陆后点击 判断*/
	private void analyzingConditions(){
//		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		BaseHttpEngine.showProgressDialogCanGoBack();
		if (StringUtil.isNull(commConversationId)) {
			// 请求登录后的CommConversationId
			requestCommConversationId();
		} else {
			// 判断用户是否开通投资理财服务
			requestPsnInvestmentManageIsOpen();
		}
	}
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		if (StringUtil.isNullOrEmpty(commConversationId)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		} else {
			// 判断用户是否开通投资理财服务
			requestPsnInvestmentManageIsOpen();
		}
	}
	/** 判断是否开通投资理财服务--二级菜单----回调 */
	@Override
	public void requestMenuIsOpenCallback(Object resultObj) {
		super.requestMenuIsOpenCallback(resultObj);
		if (isOpen) {
			// 查询是否签约保证金产品
			searchBaillAfterOpen = 1;
			requestPsnVFGBailListQueryCondition();
		} else {
			BaseHttpEngine.dissMissProgressDialog();
			searchBaillAfterOpen = 2;
			getPop();
		}
	}
	/** 查询是否签约保证金产品--------回调 */
	@Override
	public void requestPsnVFGBailListQueryConditionCallback(Object resultObj) {
		super.requestPsnVFGBailListQueryConditionCallback(resultObj);
		List<Map<String, String>> result = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.ISFOREX_RESULT_SIGN);
		if (result == null || result.size() <= 0) {
			isSign = false;
			isSettingAcc = false;
			BaseHttpEngine.dissMissProgressDialog();
			// 弹出任提示框
			getPop();
			return;
		} else {
			isSign = true;
			isCondition = true;
			// 是否设置双向宝账户
			requestPsnVFGGetBindAccount();
		}
	}
	/** 判断是否设置外汇双向宝账户 --------条件判断 */
	@Override
	public void requestConditionAccountCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> accReaultMap = (Map<String, String>) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(accReaultMap)) {
			isSettingAcc = false;
		} else {
			String accountNumber = accReaultMap.get(IsForex.ISFOREX_ACCOUNTNUMBER_REQ);
			if (StringUtil.isNull(accountNumber)) {
				isSettingAcc = false;
			} else {
				isSettingAcc = true;
				BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_ACCREAULTMAP, accReaultMap);
			}
		}

		if (isOpen && isSign && isSettingAcc) {
			// /////////////////////功能外置数据
			switch (btnId) {
				case 0: // 交易查询
					Intent prmsQueryintent = new Intent(IsForexTwoWayTreasureNewActivity.this,IsForexQueryMenuActivity.class);
					startActivity(prmsQueryintent);
					break;
				case 1: // 账户管理
					Intent prmsAccMeneintent = new Intent(IsForexTwoWayTreasureNewActivity.this,IsForexMyRateInfoActivity.class);
					startActivity(prmsAccMeneintent);
					break;
				case 2://撤单===当前有效委托查询IsForexCurrenyWTActivity
					Intent isForexCurrentActivity = new Intent(IsForexTwoWayTreasureNewActivity.this,IsForexCurrenyWTActivity.class);
					startActivity(isForexCurrentActivity);
					break;
				case 3://自选 第一次进入双向宝行情 自选====先调用bii数据显示 再调用两次四方的（贵金属 外汇）
					requestPsnVFGCustomerSetRate();
//					requestQueryMultipleQuotation("F","")
					break;
				default:
					break;
			}
		} else {
			BaseHttpEngine.dissMissProgressDialog();
			// 弹出任提示框
			getPop();
			return;
		}
	}
	/**多笔行情查询*/
	protected  void requestQueryMultipleQuotation(String flag, String pSort){
		QueryMultipleQuotationRequestParams params = new QueryMultipleQuotationRequestParams(flag, "M", pSort);
		params.setCardType(flag);
		params.setCardClass("M");
		params.setpSort(pSort);
		HttpHelp httpHelp = HttpHelp.getInstance();
		httpHelp.postHttpFromSF(this, params);
		httpHelp.setHttpErrorCallBack(new IHttpErrorCallBack() {
			@Override
			public boolean onError(String exceptionMessage, Object extendParams) {
//				BaseHttpEngine.dissMissProgressDialog();
				if(BaseDroidApp.getInstanse().isLogin()){
					clearQueryMultiplelist();
					//首次进入 自选
					if(btnId == 3){
						btnId = -1;
						isCustomer = true;
						requestCustomerQueryMultipleRefreshg("G","");//贵金属 四方接口请求
					}else if(coinId == 0){//点击进入自选
						requestCustomerQueryMultipleRefreshg("G","");//贵金属 四方接口请求
					}
				}
				return true;
			}
		});
		httpHelp.setOkHttpErrorCode(new IOkHttpErrorCode() {
			@Override
			public boolean handlerErrorCode(BaseResponseData responseData, Object extendParams) {
//				BaseHttpEngine.dissMissProgressDialog();
				if(BaseDroidApp.getInstanse().isLogin()){
					clearQueryMultiplelist();
					//首次进入 自选
					if(btnId == 3){
						btnId = -1;
						isCustomer = true;
						requestCustomerQueryMultipleRefreshg("G","");//贵金属 四方接口请求
					}else if(coinId == 0){//点击进入自选
						requestCustomerQueryMultipleRefreshg("G","");//贵金属 四方接口请求
					}
				}
				return true;
			}
		});
		httpHelp.setHttpResponseCallBack(new IHttpResponseCallBack() {
			@Override
			public boolean responseCallBack(String response, Object extendParams) {
				QueryMultipleQuotationResponseData result = GsonTools.fromJson(response,QueryMultipleQuotationResponseData.class);
				QueryMultipleQuotationResult body = result.getBody();
				clearQueryMultiplelist();
				queryMultiplelist = body.getItem();
//				BaseHttpEngine.dissMissProgressDialog();
				if(BaseDroidApp.getInstanse().isLogin()){
					//首次进入 自选
					if(btnId == 3){
						btnId = -1;
						isCustomer = true;
						requestCustomerQueryMultipleRefreshg("G","");//贵金属 四方接口请求
					}else if(coinId == 0){//点击进入自选
						requestCustomerQueryMultipleRefreshg("G","");//贵金属 四方接口请求
					}
				}
				return false;
			}
		});
	}
	//点击刷新
	private void requestCustomerQueryMultipleRefreshg(String flag, String pSort){
		QueryMultipleQuotationRequestParams params = new QueryMultipleQuotationRequestParams(flag, "M", pSort);
		params.setCardType(flag);
		params.setCardClass("M");
		params.setpSort(pSort);
		HttpHelp httpHelp = HttpHelp.getInstance();
		httpHelp.postHttpFromSF(this, params);
		httpHelp.setHttpErrorCallBack(new IHttpErrorCallBack() {
			@Override
			public boolean onError(String exceptionMessage, Object extendParams) {
				BaseHttpEngine.dissMissProgressDialog();
				if(isCustomer){
					isCustomer = false;
					clearQueryMultiplelist();
					//保存本次所有请求回来的数据
					clearDateList(false,adapter.getSourceList());
					//合并数据 并且刷新
					totalLoginDataList = getTotalDate(customerRateList);
//					clearDateList(false, totalLoginDataList);
//					BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", totalLoginDataList);
					adapter.setSourceList(totalLoginDataList,0);
					// 7秒刷新
					refreshCustomerData();
				} else if(0 == coinId){
					//保存本次所有请求回来的数据
					clearDateList(false,adapter.getSourceList());
					//方便 自选页面刷新
					clearQueryMultiplelist();
					//合并数据 并且刷新
					totalLoginDataList = getTotalDate(customerRateList);
//					clearDateList(false, totalLoginDataList);
//					BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", totalLoginDataList);
					adapter.setSourceList(totalLoginDataList,0);
					// 7秒刷新
					refreshCustomerData();
				}
				return true;
			}
		});
		httpHelp.setOkHttpErrorCode(new IOkHttpErrorCode() {
			@Override
			public boolean handlerErrorCode(BaseResponseData responseData, Object extendParams) {
				BaseHttpEngine.dissMissProgressDialog();
				if(isCustomer){
					isCustomer = false;
					clearQueryMultiplelist();
					//保存本次所有请求回来的数据
					clearDateList(false,adapter.getSourceList());
					//合并数据 并且刷新
					totalLoginDataList = getTotalDate(customerRateList);
//					clearDateList(false, totalLoginDataList);
//					BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", totalLoginDataList);
					adapter.setSourceList(totalLoginDataList,0);
					// 7秒刷新
					refreshCustomerData();
				}else if(0 == coinId){
					clearQueryMultiplelist();
					//保存本次所有请求回来的数据
					clearDateList(false,adapter.getSourceList());
					//合并数据 并且刷新
					totalLoginDataList = getTotalDate(customerRateList);
//					clearDateList(false, totalLoginDataList);
//					BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", totalLoginDataList);
					adapter.setSourceList(totalLoginDataList,0);
					// 7秒刷新
					refreshCustomerData();
				}
				return true;
			}
		});
		httpHelp.setHttpResponseCallBack(new IHttpResponseCallBack() {
			@Override
			public boolean responseCallBack(String response, Object extendParams) {
				QueryMultipleQuotationResponseData result = GsonTools.fromJson(response,QueryMultipleQuotationResponseData.class);
				QueryMultipleQuotationResult body = result.getBody();
				if(StringUtil.isNullOrEmpty(queryMultiplelist)){
					queryMultiplelist = body.getItem();
				}else {
					queryMultiplelist.addAll(body.getItem());
				}
				BaseHttpEngine.dissMissProgressDialog();
				if(isCustomer){
					isCustomer = false;
					//保存本次所有请求回来的数据
					clearDateList(false,adapter.getSourceList());
					//合并数据 并且刷新
					totalLoginDataList = getTotalDate(customerRateList);
//					clearDateList(false, totalLoginDataList);
//					BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", totalLoginDataList);
					adapter.setSourceList(totalLoginDataList,0);
					// 7秒刷新
					refreshCustomerData();
				}else if(0 == coinId){
					//保存本次所有请求回来的数据
					clearDateList(false,adapter.getSourceList());
					//合并数据 并且刷新
					totalLoginDataList = getTotalDate(customerRateList);
//					clearDateList(false, totalLoginDataList);
//					BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", totalLoginDataList);
					adapter.setSourceList(totalLoginDataList,0);
					// 7秒刷新
					refreshCustomerData();
				}
				return false;
			}
		});
	}
	/** 自选---点击按钮时触发 */
	@Override
	public void requestCustomerSetRateCallback(Object resultObj) {
		super.requestCustomerSetRateCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		customerRateList = (List<Map<String, Object>>) biiResponseBody.getResult();
		// 自选选中效果
//		getCustomerButton();
//		BaseHttpEngine.dissMissProgressDialog();
		if (customerRateList == null || customerRateList.size() <= 0) {
			//等确认
			// 用户定制的汇率为空,需进行汇率定制
//			makeRateView.setVisibility(View.VISIBLE);
			// 新布局里面的汇率定制按钮事件-------不在使用
//			noMakeButton.setOnClickListener(listener);
			return;
		} else {
			// 我的汇率数据
			getCustomerDate();
			// 7秒刷新
//			refreshCustomerData();
		}
	}
	/**自选 点击效果*/
	private void getCustomerButton(){
		btn_selfchose.setTextColor(getResources().getColor(R.color.red));
		ly_selfchose.setVisibility(View.VISIBLE);
		btn_gold.setTextColor(getResources().getColor(R.color.black));
		ly_gold.setVisibility(View.GONE);
		btn_forex.setTextColor(getResources().getColor(R.color.black));
		ly_forex.setVisibility(View.GONE);

	}
	/** 登陆前 贵金属 外汇 查询全部汇率----货币对 */
	public void requestPsnGetAllExchangeRatesOutlay(String paritiesType) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PsnGetAllExchangeRatesOutlay_API);
		Map<String, String> params = new HashMap<String, String>();
		// 客户登记的交易账户的省行联行号 X-FUND联行号
		params.put(IsForex.ISFOREX_ibknum, LocalDataService.getInstance().getIbkNum(ConstantGloble.IsForex));
		// 牌价类型(F外汇;G黄金;N 930牌价;M向宝双)
		params.put(IsForex.ISFOREX_paritiesType, paritiesType);
		params.put("offerType", "M");
		biiRequestBody.setParams(params);
		HttpManager.requestOutlayBii(biiRequestBody, this,"requestPsnGetAllExchangeRatesOutlayCallback");

		////////////////////////本地挡板
//		HttpManager.requestBii(biiRequestBody, this, "requestPsnGetAllExchangeRatesOutlayCallback");
	}
	/** 全部汇率数据 回调 */
	public void requestPsnGetAllExchangeRatesOutlayCallback(Object resultObj) {
//		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> result = (List<Map<String, Object>>) biiResponseBody.getResult();
		// TODO
		if (StringUtil.isNullOrEmpty(result) || StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			isHasAllList = false;
//			refreshAllAdapterListenerOutlay(new ArrayList<Map<String, Object>>()); // 清空
			//暂时用同一个适配器
			getCustomerAdapterListener(new ArrayList<Map<String, Object>>());//清空
		} else {
//			getWaiOrGoldOutlayButton(paritiesType);
//			selectedAllButton(paritiesType)
			allRateList = result;
			// 将得到的数据进行处理
			allRateList = getTrueDate(allRateList);

			isHasAllList = true;
			ListUtils.sortForInvest(allRateList, new ListUtils.ISynchronousList<Map<String, Object>, KeyAndValueItem>() {
				@Override
				public boolean doSomeThing(Map<String, Object> stringObjectMap, KeyAndValueItem keyAndValueItem) {
					String sourceCurrencyCode = (String) stringObjectMap.get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
					String targetCurrencyCode = (String) stringObjectMap.get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
					if(keyAndValueItem.getKey().equals(sourceCurrencyCode) && keyAndValueItem.getValue().equals(targetCurrencyCode)){
						stringObjectMap.put("formatNumber",keyAndValueItem.getParam());
						return true;
					}
					return false;
				}
			});
			refreshOutlayTimes(allRateList);
			clearDateList(true, null);
//			BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLogoutDataList", null);
			//暂时用同一个适配器
			getCustomerAdapterListener(allRateList);
			totalLogoutDataList = allRateList;
			requestQueryMultipleQuotationFirst(paritiesType, "");
			/**合并数据*/
//			totalLogoutDataList = getTotalDate(allRateList);
			// 保存本次所有请求回来的数据
//			BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLogoutDataList", totalLogoutDataList);

//			refreshAllAdapterListenerOutlay(allRateList);

			//暂时用同一个适配器
//			getCustomerAdapterListener(totalLogoutDataList);
//			refreshGoldData();
		}
	}
	/**多笔行情查询*/
	protected  void requestQueryMultipleQuotationFirst(String flag, String pSort){
		QueryMultipleQuotationRequestParams params = new QueryMultipleQuotationRequestParams(flag, "M", pSort);
		params.setCardType(flag);
		params.setCardClass("M");
		params.setpSort(pSort);
		HttpHelp httpHelp = HttpHelp.getInstance();
		httpHelp.postHttpFromSF(this, params);
		httpHelp.setHttpErrorCallBack(new IHttpErrorCallBack() {
			@Override
			public boolean onError(String exceptionMessage, Object extendParams) {
				BaseHttpEngine.dissMissProgressDialog();
				clearQueryMultiplelist();
				if(BaseDroidApp.getInstance().isLogin()){
					if(coinId == 1||coinId == 2){
						//保存本次所有请求回来的数据
						clearDateList(false,adapter.getSourceList());
						totalLoginDataList = getTotalDate(allRateList);
						/**适配器初始化暂时使用同一个方法*/
//						clearDateList(false, totalLoginDataList);
//						BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", totalLoginDataList);
						adapter.setSourceList(totalLoginDataList,0);
						// 7秒刷新
						refreshAllData(vfgType);
					}
				}else {
					//保存本次所有请求回来的数据
					clearDateList(false,adapter.getSourceList());
					/**合并数据*/
					totalLogoutDataList = getTotalDate(allRateList);
					// 保存本次所有请求回来的数据
//					clearDateList(false, totalLogoutDataList);
//					BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLogoutDataList", totalLogoutDataList);
					adapter.setSourceList(totalLogoutDataList,0);
					refreshGoldData();
				}
				return true;
			}
		});
		httpHelp.setOkHttpErrorCode(new IOkHttpErrorCode() {
			@Override
			public boolean handlerErrorCode(BaseResponseData responseData, Object extendParams) {
				BaseHttpEngine.dissMissProgressDialog();
				clearQueryMultiplelist();
				if(BaseDroidApp.getInstance().isLogin()){
					if(coinId == 1||coinId == 2){
						//保存本次所有请求回来的数据
						clearDateList(false,adapter.getSourceList());
						totalLoginDataList = getTotalDate(allRateList);
						/**适配器初始化暂时使用同一个方法*/
//						clearDateList(false, totalLoginDataList);
//						BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", totalLoginDataList);
						adapter.setSourceList(totalLoginDataList,0);
						// 7秒刷新
						refreshAllData(vfgType);
					}
				}else {
					//保存本次所有请求回来的数据
					clearDateList(false,adapter.getSourceList());
					/**合并数据*/
					totalLogoutDataList = getTotalDate(allRateList);
					// 保存本次所有请求回来的数据
//					clearDateList(false, totalLogoutDataList);
//					BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLogoutDataList", totalLogoutDataList);
					adapter.setSourceList(totalLogoutDataList,0);
					refreshGoldData();
				}
				return true;
			}
		});
//		httpHelp.setHttpErrorCallBack(new IHttpErrorCallBack() {
//			@Override
//			public boolean onError(String exceptionMessage, Object extendParams) {
//				BaseHttpEngine.dissMissProgressDialog();
//				if(BaseDroidApp.getInstance().isLogin()){
//					if(coinId == 1||coinId == 2){
//						totalLoginDataList = getTotalDate(allRateList);
//						/**适配器初始化暂时使用同一个方法*/
//						BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", totalLoginDataList);
//						adapter.setSourceList(totalLoginDataList,0);
//						// 7秒刷新
//						refreshAllData(vfgType);
//					}
//				}else {
//					/**合并数据*/
//					totalLogoutDataList = getTotalDate(allRateList);
//					// 保存本次所有请求回来的数据
//					BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLogoutDataList", totalLogoutDataList);
//					adapter.setSourceList(totalLogoutDataList,0);
//					refreshGoldData();
//				}
//
//				return false;
//			}
//		});
		httpHelp.setHttpResponseCallBack(new IHttpResponseCallBack() {
			@Override
			public boolean responseCallBack(String response, Object extendParams) {
				QueryMultipleQuotationResponseData result = GsonTools.fromJson(response,QueryMultipleQuotationResponseData.class);
				QueryMultipleQuotationResult body = result.getBody();
				clearQueryMultiplelist();
				queryMultiplelist = body.getItem();
				BaseHttpEngine.dissMissProgressDialog();
				if(BaseDroidApp.getInstance().isLogin()){
					if(coinId == 1 ||coinId == 2){
						//保存本次所有请求回来的数据
						clearDateList(false,adapter.getSourceList());
						totalLoginDataList = getTotalDate(allRateList);
						/**适配器初始化暂时使用同一个方法*/
//						clearDateList(false, totalLoginDataList);
//						BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", totalLoginDataList);
						adapter.setSourceList(totalLoginDataList,0);
						// 7秒刷新
						refreshAllData(vfgType);
					}
				}else {
					//保存本次所有请求回来的数据
					clearDateList(false,adapter.getSourceList());
					/**合并数据*/
					totalLogoutDataList = getTotalDate(allRateList);
					// 保存本次所有请求回来的数据
//					clearDateList(false, totalLogoutDataList);
//					BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLogoutDataList", totalLogoutDataList);
					adapter.setSourceList(totalLogoutDataList,0);
					refreshGoldData();
				}
				return false;
			}
		});
	}

	/** 登陆前 刷新外汇、贵金属-7秒刷新 */
	private void refreshGoldData() {
		if (!PollingRequestThread.pollingFlag) {
			requestGoldRatesPoling();
		}
	}
	/** 7秒刷新 登录前 贵金属 外汇刷新*/
	private void requestGoldRatesPoling() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(IsForex.ISFOREX_PsnGetAllExchangeRatesOutlay_API);
		Map<String, String> params = new HashMap<String, String>();
		// 客户登记的交易账户的省行联行号 X-FUND联行号
		params.put(IsForex.ISFOREX_ibknum, LocalDataService.getInstance().getIbkNum(ConstantGloble.IsForex));
		// 牌价类型(F外汇;G黄金;N 930牌价;M向宝双)
		params.put(IsForex.ISFOREX_paritiesType, paritiesType);
		params.put("offerType", "M");
		biiRequestBody.setParams(params);
		HttpManager.requestPollingOutlay(biiRequestBody, allHttpHandler,  ConstantGloble.FOREX_REFRESH_TIMES);// 7秒刷新
	}
	/** 刷新功能外置时间 */
	private void refreshOutlayTimes(List<Map<String, Object>> list) {
		Map<String, Object> map = list.get(0);
		String times = (String) map.get(IsForex.ISFOREX_RATE_UPDATEDATE_RES);
		tv_refishtime.setText("数据更新于北京时间"+times+"，具体价格以实际成交为准");
	}
	/** 停止轮询 */
	private void stopPollingFlag() {
		if (HttpManager.mPollingRequestThread != null && PollingRequestThread.pollingFlag) {
			LogGloble.e(TAG, "onPause() mPollingRequestThread  stopPolling()  ");
			HttpManager.stopPolling();
		}
	}
	/**清空oldTotalLoginDataList  oldtotalLogoutDataList*/
	private void clearDateList(boolean isClear, List<Map<String, Object>> oldtotalDataList){
		if(BaseDroidApp.getInstance().isLogin()){
			if(isClear){
				BaseDroidApp.getInstance().getBizDataMap().put("oldTotalLoginDataList", null);
			}else {
				BaseDroidApp.getInstance().getBizDataMap().put("oldTotalLoginDataList", oldtotalDataList);
			}
		}else {
			if(isClear){
				BaseDroidApp.getInstance().getBizDataMap().put("oldtotalLogoutDataList", null);
			}else {
				BaseDroidApp.getInstance().getBizDataMap().put("oldtotalLogoutDataList", oldtotalDataList);
			}
		}
	}

	@Override
	public View getView(final int arg0, Map<String, Object> currentItem, LayoutInflater inflater, View convertView, ViewGroup viewGroup) {
		ViewHoldler holder = null;
		if (convertView == null) {
			holder = new ViewHoldler();
			convertView = inflater.inflate(R.layout.isforex_main_item,  lv_prms, false);
			holder.detail = (LinearLayout)convertView.findViewById(R.id.detail);
			holder.tv_currency = (TextView) convertView.findViewById(R.id.tv_currency);
			holder.tv_sale = (NumberStyleTextView) convertView.findViewById(R.id.tv_sale);
//			holder.tv_saledecimal = (TextView) convertView.findViewById(R.id.tv_saledecimal);
			holder.tv_buy = (NumberStyleTextView) convertView.findViewById(R.id.tv_buy);
//			holder.tv_buydecimal = (TextView) convertView.findViewById(R.id.tv_buydecimal);
			holder.tv_riseorfall = (TextView) convertView.findViewById(R.id.tv_riseorfall);


			convertView.setTag(holder);
		} else {
			holder = (ViewHoldler) convertView.getTag();
		}
		// 得到源货币的代码
		String sourceCurrencyCode = (String) currentItem.get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
		String sourceDealCode = null;
		/** 得到目标货币代码*/
		String targetCurrencyCode = (String) currentItem.get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
		String targetDealCode = null;
		String temp = "";
		temp = LocalData.code_Map.get(sourceCurrencyCode+targetCurrencyCode);
		if(!StringUtil.isNullOrEmpty(temp)){
			//货币对
			holder.tv_currency.setText(temp);
		}else {
			if (LocalData.Currency.containsKey(sourceCurrencyCode)) {
				sourceDealCode = LocalData.Currency.get(sourceCurrencyCode);
			}
			if (LocalData.Currency.containsKey(targetCurrencyCode)) {
				targetDealCode = LocalData.Currency.get(targetCurrencyCode);
			}
			StringBuilder sb = new StringBuilder(sourceDealCode);
			sb.append("/");
			sb.append(targetDealCode);
			//货币对
			holder.tv_currency.setText(sb.toString().trim());
		}
		if(!StringUtil.isNullOrEmpty(temp)){
			//货币对
			holder.tv_currency.setText(temp);
		}
		//卖出价
		String sellRate = (String) currentItem.get(IsForex.ISFOREX_SELLRATE_RES);

		//买入价
		String buyRate = (String) currentItem.get(IsForex.ISFOREX_BUYRATE_RES);
		Integer formatNumber = (Integer)currentItem.get("formatNumber");
		sellRate = parseStringPattern(sellRate,formatNumber);
		buyRate = parseStringPattern(buyRate,formatNumber);
		holder.tv_buy.setNumberText(sellRate,2,"--");
		holder.tv_sale.setNumberText(buyRate,2,"--");
//		holder.tv_buydecimal.setText(buys[1]);
		//显示涨跌幅 还是涨跌值
		//今日涨跌幅    暂时确认 自选中的涨跌幅 从一个新接口中获取 需组装数据
		String currPercentDiff = (String) currentItem.get("currPercentDiff");
		//今日涨跌值
		String currDiff =  (String) currentItem.get("currDiff");
		oldtotalDataList = new ArrayList<Map<String, Object>>();
		if(BaseDroidApp.getInstance().isLogin()){
			oldtotalDataList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap().get("oldTotalLoginDataList");
		}else {
			oldtotalDataList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap().get("oldtotalLogoutDataList");
		}
		/** 根据涨跌标志，设置买入牌价、卖出牌价的背景图片 图片需重新修改*/
//		int red = IsForexTwoWayTreasureNewActivity.this.getResources().getColor(R.color.red);
//		int black = IsForexTwoWayTreasureNewActivity.this.getResources().getColor(R.color.gray_title);
//		int green = IsForexTwoWayTreasureNewActivity.this.getResources().getColor(R.color.greens);
		if(isStartAnimator){
			if (null != oldtotalDataList && oldtotalDataList.size() != 0) {
				Map<String, Object> oldMap = oldtotalDataList.get(arg0);
				Integer formatNumbert = (Integer)oldMap.get("formatNumber");
				String oldBuyRate = (String) oldMap.get(IsForex.ISFOREX_BUYRATE_RES);
				String oldSellRate = (String) oldMap.get(IsForex.ISFOREX_SELLRATE_RES);
				oldBuyRate = parseStringPattern(oldBuyRate,formatNumbert);
				oldSellRate = parseStringPattern(oldSellRate,formatNumbert);
				if(isLight){
					if (!sellRate.equals(oldSellRate) || !buyRate.equals(oldBuyRate)) {//发生变化
						if(!StringUtil.isNullOrEmpty(currDiff)){
							if (currDiff.contains("-")) {//进行绿色高亮显示
								startAnimation(convertView, R.color.green);
							} else if(currDiff.contains("+")) {//进行红色高亮显示
								startAnimation(convertView, R.color.red); //加载红色动画
							}else{
								startAnimation(convertView,R.color.share_gray_color); //加载灰色动画
							}
						}else {
							startAnimation(convertView,R.color.share_gray_color); //加载灰色动画
						}
					}
				}
			}
		}
		if(!StringUtil.isNullOrEmpty(currDiff)){
			if (currDiff.contains("-")) {//绿色背景
				holder.tv_riseorfall.setBackgroundResource(R.drawable.shape_llbt_round_green);
			} else if(currDiff.contains("+")){//红色背景
				holder.tv_riseorfall.setBackgroundResource(R.drawable.shape_llbt_round_red);
			}else{
				holder.tv_riseorfall.setBackgroundResource(R.drawable.shape_llbt_round_gray);
			}
		}else {
			holder.tv_riseorfall.setBackgroundResource(R.drawable.shape_llbt_round_gray);
		}

		//默认显示涨跌幅
		if(!StringUtil.isNullOrEmpty(currPercentDiff)){
			currPercentDiff = StringTools.parseStringPattern5(currPercentDiff);
			holder.tv_riseorfall.setText(currPercentDiff);
		}else{
			holder.tv_riseorfall.setText("--");
		}
		if(iscurrPercentDiff  == true){
			tv_riseorfall.setText("涨跌幅");
			if(!StringUtil.isNullOrEmpty(currPercentDiff)){
				currPercentDiff = StringTools.parseStringPattern5(currPercentDiff);
				((TextView) holder.tv_riseorfall).setText(currPercentDiff);
			}else {
				((TextView) holder.tv_riseorfall).setText("--");
			}
		}
		else {
			tv_riseorfall.setText("涨跌值");
//			iscurrPercentDiff = true;
//			tv_riseorfall.setText("涨跌幅");
			if(!StringUtil.isNullOrEmpty(currDiff)){
//				if(currDiff.contains("+")){
//					((TextView) holder.tv_riseorfall).setText("+"+currDiff);
//				}else {
				if(currDiff.contains("+")){
					currDiff = StringTools.parseStringPattern(currDiff,5);
					currDiff = StringTools.subZeroAndDot(currDiff);
					currDiff = "+"+currDiff;
				}else{
					currDiff = StringTools.parseStringPattern(currDiff,5);
					currDiff = StringTools.subZeroAndDot(currDiff);
				}
//				currDiff = (String)StringTools.subZeroAndDo(currDiff);
				((TextView) holder.tv_riseorfall).setText(currDiff);
//				}
			}else {
				((TextView) holder.tv_riseorfall).setText("--");
			}
		}

		holder.tv_riseorfall.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				iscurrPercentDiff = !iscurrPercentDiff;
				adapter.notifyDataSetChanged();
				isLight = false;
//				if(iscurrPercentDiff){//涨跌值
//					iscurrPercentDiff = false;
//					tv_riseorfall.setText("涨跌值");
//					if(null != currDiff){
//						if (currDiff.contains("-")) {
//							((TextView) view).setText( currDiff);
//						} else {
//							((TextView) view).setText(currDiff);
//						}
//					}else {
//						((TextView) view).setText("--");
//					}
//				}else{
//					iscurrPercentDiff = true;
//					tv_riseorfall.setText("涨跌幅");
//					if(null != currPercentDiff){
//						if (currPercentDiff.contains("-")) {
//							((TextView) view).setText( currPercentDiff);
//						} else {
//							((TextView) view).setText(currPercentDiff);
//						}
//					}else {
//						((TextView) view).setText("--");
//					}
//
//				}
			}
		});
		holder.detail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				//停止刷新
				stopPollingFlag();

				/**单笔行情查询*/
				BaseHttpEngine.showProgressDialog();
				// yuht.修改
				if(BaseDroidApp.getInstanse().isLogin()){
					currencyPairs = getCodeName(totalLoginDataList, arg0);
				}
				else {
					currencyPairs = getCodeName(totalLogoutDataList, arg0);
				}
//				isGoBack = true;
				if(BaseDroidApp.getInstance().isLogin()){
					isLoginMoreBack = true;
				}else {
					isLogoutMoreBack = true;
				}
				requestQuerySingelQuotation(currencyPairs ,vfgType);
			}
		});

		return convertView;
	}

	@Override
	public void onLoadMore(View pullToRefreshLayout) {
		stopPollingFlag();
		clearDateList(true, null);
		isLight = false;
		if(BaseDroidApp.getInstance().isLogin()){
			if(0 == coinId){
				refreshCustomerData();
			}else {
				refreshAllData(vfgType);
			}
		}else {
			refreshGoldData();
//			requestQueryMultipleQuotation(paritiesType, "");
		}
	}


	class ViewHoldler {
		LinearLayout detail;
		TextView tv_currency;
		NumberStyleTextView tv_sale;
		//		TextView tv_saledecimal;
		NumberStyleTextView tv_buy;
		//		TextView tv_buydecimal;
		TextView tv_riseorfall;
	}

	/**
	 * 涨跌幅 跌时动画显示
	 */
	public void startAnimation(final View view, final int colorID) {
		//创建动画,这里的关键就是使用ArgbEvaluator, 后面2个参数就是 开始的颜色,和结束的颜色.
		final ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), this.getResources().getColor(colorID), Color.WHITE);
		colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {


			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				int color = (int) animation.getAnimatedValue();//之后就可以得到动画的颜色了
				view.setBackgroundColor(color);//设置一下, 就可以看到效果.
			}
		});
		colorAnimator.setDuration(500);
		colorAnimator.start();
	}

//	@Override
//	public void onItemClick(LinearListView linearListView, View view, int position, long l) {
//
//		index = position;
//
//		/**单笔行情查询*/
//		BaseHttpEngine.showProgressDialog();
//		//如果在该页面登陆了， 那么判断双向宝持仓信息， 来判定卖出按钮是否显示
//		currencyPairs = getCodeName(totalLoginDataList, position);
//		requestQuerySingelQuotation(currencyPairs ,vfgType);
//
//	}
	/** 得到用户选择的货币对名称 */
	private String getCodeName(List<Map<String, Object>> lists, int position) {
		index = position;
		String codeNames = null;
		Map<String, Object> map = lists.get(position);
		String sourceCurrencyCode = (String) map.get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
		String targetCurrencyCode = (String) map.get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
		// 货币对
		if (!StringUtil.isNull(sourceCurrencyCode) && !StringUtil.isNull(targetCurrencyCode)) {
			String sourceCurrency = LocalData.Currency.get(sourceCurrencyCode);
			String targetCurrency = LocalData.Currency.get(targetCurrencyCode);
			codeNames = sourceCurrencyCode + targetCurrencyCode;
//			人民币金、银、铂、钯、美元金、银、铂、钯   贵金属的币种
			if(LocalData.goldLists.contains(sourceCurrency)||LocalData.goldLists.contains(targetCurrency)){
				vfgType = "G";
			}else {
				vfgType = "F";
			}
		}
		return codeNames;
	}
	/**单笔行情查询
	 * 货币对、牌价类型 牌价种类（M）*/
	private void requestQuerySingelQuotation(String currencyPairs, String type){
		QuerySingelQuotationRequestParams params = new QuerySingelQuotationRequestParams(currencyPairs, type,"M");
		params.setCcygrp(currencyPairs);
		params.setCardType(type);
		params.setCardClass("M");
		HttpHelp httpHelp = HttpHelp.getInstance();
		httpHelp.postHttpFromSF(this, params);
		httpHelp.setHttpErrorCallBack(new IHttpErrorCallBack() {
			@Override
			public boolean onError(String exceptionMessage, Object extendParams) {
				if(!StringUtil.isNullOrEmpty(querySingleItem)){
					querySingleItem = null;
				}
				singleDataMap = getSingleTotal();
//				singleDataList.add(totalLoginDataList.get(0));
				if(BaseDroidApp.getInstance().isLogin()){
					//判断自选是否为空，如果为空 说明是从外汇或者贵金属跳转到详情页面，再次之前customerRateList已经被清空，需要重新再次请求
					if(StringUtil.isNullOrEmpty(customerRateList)){
						requestSelfCustomer();
					}else {
						/**针对自选中的货币对 添加标识 1为 收藏 0为 空*/
						BaseHttpEngine.dissMissProgressDialog();
						totalLoginDataList = totalCurrencyIdentification();
						Intent intent = new Intent(IsForexTwoWayTreasureNewActivity.this, IsForexTwoWayDetailActivity.class);
						intent.putExtra("position", index);
						//外汇（贵金属）标识
						intent.putExtra("flag", vfgType);
						BaseDroidApp.getInstance().getBizDataMap().put("totalCurrency", totalLoginDataList);
						BaseDroidApp.getInstanse().getBizDataMap().put("singleDataMap", singleDataMap);
						BaseDroidApp.getInstance().getBizDataMap().put("customerRateList", customerRateList);
						startActivity(intent);
					}

				}else {
					BaseHttpEngine.dissMissProgressDialog();
					Intent intent = new Intent(IsForexTwoWayTreasureNewActivity.this, IsForexTwoWayDetailActivity.class);
					intent.putExtra("position", index);
					//外汇（贵金属）标识
					intent.putExtra("flag", vfgType);
					BaseDroidApp.getInstance().getBizDataMap().put("totalCurrency", totalLogoutDataList);
					BaseDroidApp.getInstanse().getBizDataMap().put("singleDataMap", singleDataMap);
					startActivity(intent);
				}
				return true;
			}
		});
		httpHelp.setOkHttpErrorCode(new IOkHttpErrorCode() {
			@Override
			public boolean handlerErrorCode(BaseResponseData responseData, Object extendParams) {
				if(!StringUtil.isNullOrEmpty(querySingleItem)){
					querySingleItem = null;
				}
				singleDataMap = getSingleTotal();
//				singleDataList.add(totalLoginDataList.get(0));
				if(BaseDroidApp.getInstance().isLogin()){
					//判断自选是否为空，如果为空 说明是从外汇或者贵金属跳转到详情页面，再次之前customerRateList已经被清空，需要重新再次请求
					if(StringUtil.isNullOrEmpty(customerRateList)){
						requestSelfCustomer();
					}else {
						BaseHttpEngine.dissMissProgressDialog();
						/**针对自选中的货币对 添加标识 1为 收藏 0为 空*/
						totalLoginDataList = totalCurrencyIdentification();
						Intent intent = new Intent(IsForexTwoWayTreasureNewActivity.this, IsForexTwoWayDetailActivity.class);
						intent.putExtra("position", index);
						//外汇（贵金属）标识
						intent.putExtra("flag", vfgType);
						BaseDroidApp.getInstance().getBizDataMap().put("totalCurrency", totalLoginDataList);
						BaseDroidApp.getInstanse().getBizDataMap().put("singleDataMap", singleDataMap);
						BaseDroidApp.getInstance().getBizDataMap().put("customerRateList", customerRateList);
						startActivity(intent);
					}

				}else {
					BaseHttpEngine.dissMissProgressDialog();
					Intent intent = new Intent(IsForexTwoWayTreasureNewActivity.this, IsForexTwoWayDetailActivity.class);
					intent.putExtra("position", index);
					//外汇（贵金属）标识
					intent.putExtra("flag", vfgType);
					BaseDroidApp.getInstance().getBizDataMap().put("totalCurrency", totalLogoutDataList);
					BaseDroidApp.getInstanse().getBizDataMap().put("singleDataMap", singleDataMap);
					startActivity(intent);
				}
				return true;
			}
		});
//		httpHelp.setHttpErrorCallBack(new IHttpErrorCallBack() {
//			@Override
//			public boolean onError(String exceptionMessage, Object extendParams) {
//				singleDataMap = getSingleTotal();
////				singleDataList.add(totalLoginDataList.get(0));
//				if(BaseDroidApp.getInstance().isLogin()){
//					//判断自选是否为空，如果为空 说明是从外汇或者贵金属跳转到详情页面，再次之前customerRateList已经被清空，需要重新再次请求
//					if(StringUtil.isNullOrEmpty(customerRateList)){
//						requestSelfCustomer();
//					}else {
//						/**针对自选中的货币对 添加标识 1为 收藏 0为 空*/
//						totalLoginDataList = totalCurrencyIdentification();
//						requestPsnVFGPositionInfo();
//					}
//
//				}else {
//					BaseHttpEngine.dissMissProgressDialog();
//					Intent intent = new Intent(IsForexTwoWayTreasureNewActivity.this, IsForexTwoWayDetailActivity.class);
//					intent.putExtra("position", index);
//					//外汇（贵金属）标识
//					intent.putExtra("flag", vfgType);
//					BaseDroidApp.getInstance().getBizDataMap().put("totalCurrency", totalLogoutDataList);
//					BaseDroidApp.getInstanse().getBizDataMap().put("singleDataMap", singleDataMap);
//					startActivity(intent);
//				}
//				return false;
//			}
//		});
		httpHelp.setHttpResponseCallBack(new IHttpResponseCallBack() {
			@Override
			public boolean responseCallBack(String response, Object extendParams) {
				QuerySingleQuotationResponseData result = GsonTools.fromJson(response, QuerySingleQuotationResponseData.class);
				if(!StringUtil.isNullOrEmpty(querySingleItem)){
					querySingleItem = null;
				}
				querySingleItem = result.getBody();
//				querySinglelist = body.getItem();
				singleDataMap = getSingleTotal();
//				singleDataList.add(totalLoginDataList.get(0));
				if(BaseDroidApp.getInstance().isLogin()){
					//判断自选是否为空，如果为空 说明是从外汇或者贵金属跳转到详情页面，再次之前customerRateList已经被清空，需要重新再次请求
					if(StringUtil.isNullOrEmpty(customerRateList)){
						requestSelfCustomer();
					}else {
						/**针对自选中的货币对 添加标识 1为 收藏 0为 空*/
						BaseHttpEngine.dissMissProgressDialog();
						totalLoginDataList = totalCurrencyIdentification();
						Intent intent = new Intent(IsForexTwoWayTreasureNewActivity.this, IsForexTwoWayDetailActivity.class);
						intent.putExtra("position", index);
						//外汇（贵金属）标识
						intent.putExtra("flag", vfgType);
						BaseDroidApp.getInstance().getBizDataMap().put("totalCurrency", totalLoginDataList);
						BaseDroidApp.getInstanse().getBizDataMap().put("singleDataMap", singleDataMap);
						BaseDroidApp.getInstance().getBizDataMap().put("customerRateList", customerRateList);
						startActivity(intent);
					}

				}else {
					BaseHttpEngine.dissMissProgressDialog();
					Intent intent = new Intent(IsForexTwoWayTreasureNewActivity.this, IsForexTwoWayDetailActivity.class);
					intent.putExtra("position", index);
					//外汇（贵金属）标识
					intent.putExtra("flag", vfgType);
					BaseDroidApp.getInstance().getBizDataMap().put("totalCurrency", totalLogoutDataList);
					BaseDroidApp.getInstanse().getBizDataMap().put("singleDataMap", singleDataMap);
					startActivity(intent);
				}
				return false;
			}
		});
	}
	/**获取单笔行情数据*/
	private Map<String, Object> getSingleTotal(){

		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtil.isNullOrEmpty(querySingleItem)){
			map.put("ccygrpNm", "");
			map.put("buyRate", "");
			map.put("sellRate", "");
			map.put("priceTime", "");
			map.put("currPercentDiff", "");
			map.put("currDiff", "");
			map.put("tranCode", "");
			map.put("sortPriority", "");
			map.put("sourceCurrencyCode", "");
			map.put("targetCurrencyCode", "");
			map.put("referPrice","");
			map.put("openPrice", "");//开盘价格
			map.put("maxPrice", "");//最高值
			map.put("minPrice", ""); //最低值
		}else {
			map.put("ccygrpNm", querySingleItem.ccygrpNm);
			map.put("buyRate", querySingleItem.buyRate);
			map.put("sellRate", querySingleItem.sellRate);
			map.put("priceTime", querySingleItem.priceTime);
			map.put("currPercentDiff", querySingleItem.currPercentDiff);
			map.put("currDiff", querySingleItem.currDiff);
			map.put("tranCode", querySingleItem.tranCode);
			map.put("sortPriority", querySingleItem.sortPriority);
			map.put("sourceCurrencyCode", querySingleItem.sourceCurrencyCode);
			map.put("targetCurrencyCode", querySingleItem.targetCurrencyCode);
			map.put("referPrice",querySingleItem.referPrice);
			map.put("openPrice", querySingleItem.openPrice);//开盘价格
			map.put("maxPrice", querySingleItem.maxPrice);//最高值
			map.put("minPrice", querySingleItem.minPrice); //最低值
		}


		return map;
	}

	@Override
	public void requestSelfCustomerCallback(Object resultObj) {
		super.requestSelfCustomerCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// 得到response
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		customerRateList = (List<Map<String, Object>>) biiResponseBody.getResult();
		/**针对自选中的货币对 添加标识 1为 收藏 0为 空*/
		totalLoginDataList = totalCurrencyIdentification();
		Intent intent = new Intent(IsForexTwoWayTreasureNewActivity.this, IsForexTwoWayDetailActivity.class);
		intent.putExtra("position", index);
		//外汇（贵金属）标识
		intent.putExtra("flag", vfgType);
		BaseDroidApp.getInstance().getBizDataMap().put("totalCurrency", totalLoginDataList);
		BaseDroidApp.getInstanse().getBizDataMap().put("singleDataMap", singleDataMap);
		BaseDroidApp.getInstance().getBizDataMap().put("customerRateList", customerRateList);
		startActivity(intent);
	}

	private List<Map<String, Object>> totalCurrencyIdentification(){
		List<Map<String, Object>> dateList = new ArrayList<Map<String, Object>>();
		for(int i=0; i<totalLoginDataList.size(); i++){
			Map<String, Object> rateMap = totalLoginDataList.get(i);
			String sourceCurrencyCode = (String) rateMap.get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
			String targetCurrencyCode = (String) rateMap.get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
			for(int j=0; j<customerRateList.size(); j++){
				Map<String, Object> custoMap = customerRateList.get(j);
				String sourceCurrencyCode1 = (String)custoMap.get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
				String targetCurrencyCode1 = (String)custoMap.get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
				if(sourceCurrencyCode.equals(sourceCurrencyCode1) && targetCurrencyCode.equals(targetCurrencyCode1)){
					rateMap.put("customFlag", "1");
					break;
				}else {
					rateMap.put("customFlag", "0");
				}
			}
			dateList.add(rateMap);
		}
		return dateList;
	}

	/** 判断得到的数据是否正常 */
	private List<Map<String, Object>> dealResultDate(List<Map<String, Object>> lists) {
		int len = lists.size();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < len; i++) {
			Map<String, Object> map = lists.get(i);
			if (StringUtil.isNullOrEmpty(map)) {
				continue;
			}
			// 结算币种
			Map<String, String> settleCurrency = (Map<String, String>) map.get(IsForex.ISFOREX_SETTLECURRENCY_RES);
			if (StringUtil.isNullOrEmpty(settleCurrency)) {
				continue;
			}
			String settle = settleCurrency.get(IsForex.ISFOREX_CODE_RES);
			if (StringUtil.isNull(settle) || !LocalData.Currency.containsKey(settle)) {
				continue;
			}
			List<Map<String, Object>> details = (List<Map<String, Object>>) map.get(IsForex.ISFOREX_DETAILS_RES);
			if (details.size() <= 0 || details == null) {
				continue;
			}
			int lens = details.size();
			for (int j = 0; j < lens; j++) {
				Map<String, Object> detailsMap = details.get(j);
				Map<String, String> currency1 = (Map<String, String>) detailsMap.get(IsForex.ISFOREX_CURRENCY1_RES);
				Map<String, String> currency2 = (Map<String, String>) detailsMap.get(IsForex.ISFOREX_CURRENCY2_RES);
				if (StringUtil.isNullOrEmpty(currency1) || StringUtil.isNullOrEmpty(currency2)) {
					continue;
				}
				String code1 = currency1.get(IsForex.ISFOREX_CODE_RES);
				String code2 = currency2.get(IsForex.ISFOREX_CODE_RES);
				if (StringUtil.isNull(code1) || !LocalData.Currency.containsKey(code1) || StringUtil.isNull(code2)
						|| !LocalData.Currency.containsKey(code2)) {
					continue;
				}
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put(ConstantGloble.ISFOREX_SETTLE, settle);
				resultMap.put(ConstantGloble.ISFOREX_DETAILSMAP, detailsMap);
				list.add(resultMap);
			}
		}
		return list;
	}

}
