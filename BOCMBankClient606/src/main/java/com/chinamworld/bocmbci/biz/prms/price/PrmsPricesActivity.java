package com.chinamworld.bocmbci.biz.prms.price;

import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.biz.prms.PrmsBaseActivity;
import com.chinamworld.bocmbci.biz.prms.adapter.PrmsPricesListAdapter1;
import com.chinamworld.bocmbci.biz.prms.adapter.PrmsPricesListPreLoginAdapter1;
import com.chinamworld.bocmbci.biz.prms.myaccount.PrmsAccSettingActivity;
import com.chinamworld.bocmbci.biz.prms.trade.PrmsTradeBuyActivity;
import com.chinamworld.bocmbci.biz.prms.trade.PrmsTradeSaleActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpObserver;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.thread.PollingRequestThread;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.server.LocalDataService;
import com.chinamworld.bocmbci.utils.CardWelcomGuideUtil;
import com.chinamworld.bocmbci.utils.LoginTask.LoginCallback;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 贵金属行情页面
 * 
 * @author xyl
 * 
 */
public class PrmsPricesActivity extends PrmsBaseActivity {
	private static final String TAG = "PrmsPricesActivity";
	/**
	 * 贵金属详情View
	 */
	private View prmsPricesView = null;
	/**
	 * 更新时间提示信息
	 */
	private TextView headerView;
	/**
	 * 显示行情的listview
	 */
	private ListView listView;
	/**
	 * 存放行情 的数据 未处理
	 */
	private List<Map<String, String>> dataList;
	
	/**
	 * 贵金属行情适配器
	 */
	private PrmsPricesListAdapter1 adapter;
	/**
	 * 买入按钮的点击事件
	 */
	private OnItemClickListener buyOnItermClickListener;
	/**
	 * 卖出按钮的点击事件
	 */
	private OnItemClickListener saleOnItermClickListener;
	/**
	 * 快速交易按钮的点击事件
	 */
	private OnClickListener rightBtnOnClickListenerForTrade,
			rightBtnOnClickListenerForLogin;
	/**
	 * 标记点击事件是 哪个交易
	 */
	private int flag;
	/** 买入 */
	private static final int BUY = 4;
	/** 卖出 */
	private static final int SALE = 1;
	/** 快速交易买入 */
	private static final int FASTBUY = 2;
	/** 快速交易卖出 */
	private static final int FASTSALE = 3;
	/** 目标币种 */
	private String targetCurrencyStr;
	/** 源币种 */
	private String sourceCurrencyCodeStr;
	private boolean isshowguid = true;

	// wuhan
	private TextView tvReNewTime;
	private Button btnReNew;
	private int myPosition;
	private LinearLayout left_title2_ll,left_title_ll;
	private OnItemClickListener saleonclick, buyonclick;
	private final String  PARITIESTYP = "G";
//	private Map<String, Object> preLoginDataMap = new HashMap<String, Object>();
	
	private List<Map<String, Object>> preLoginDataList = new ArrayList<Map<String,Object>>();
	//获取省联号工具类   ConstantGloble.Forex不同模块不同
	PrmsPricesListPreLoginAdapter1 myAdapter ;
	private TextView left_title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	@Override
	public void queryPrmsAccsCallBack(Object resultObj) {
		super.queryPrmsAccsCallBack(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
			BaseHttpEngine.dissMissProgressDialog();
			// 如果没有可设定的账户
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.prms_noprmsAcc_error));
			return;
		} else {
			prmsControl.prmsAccList = (List<Map<String, String>>) (biiResponseBody
					.getResult());
			startActivityForResult(new Intent(this,PrmsAccSettingActivity.class),ConstantGloble.ACTIVITY_REQUEST_SETPRMSACC_CODE);
			BaseHttpEngine.dissMissProgressDialog();
		}
	}

//	@Override
//	public void requestCommConversationIdCallBack(Object resultObj) {
//		super.requestCommConversationIdCallBack(resultObj);
////		queryPrmsPricePoling();
//		// wuhan
//		queryPrmsPricePoliPreLogin();
//	}

	protected void queryPrmsPricePoling() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Prms.QUERY_TRADERATE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setParams(null);
		HttpManager.requestPollingBii(biiRequestBody, httpHandler, 7);// 7秒刷新
	}
	

	@Override
	public void queryPrmsPreLoginCallBack(Object resultObj) {
		super.queryPrmsPreLoginCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		
		if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
			BaseHttpEngine.dissMissProgressDialog();
//			BaseDroidApp.getInstanse().showInfoMessageDialog("请求失败，稍后再试！");
			return;
		}
		String preLoginDetail = (String) biiResponseBody.getResult().toString();
		LogGloble.i("info", "biiResponseBody.getResult()==="+preLoginDetail);
		preLoginDataList = (List<Map<String,Object>>)biiResponseBody.getResult();
		
		// /add by fsm判断行情列表是否为空
		
		if (StringUtil.isNullOrEmpty(preLoginDataList)) {
			BaseDroidApp.getInstanse().showMessageDialog(
					getString(R.string.prms_no_price),
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							PrmsPricesActivity.this.finish();
						}
					});
		}

		if (myAdapter == null) {
			myAdapter = new PrmsPricesListPreLoginAdapter1(
					PrmsPricesActivity.this, preLoginDataList);

			// wuhan
			myAdapter.setsaleOnClickListener(saleonclick);
			myAdapter.setbuyonclick(buyonclick);
			myAdapter.notifyDataSetChanged();
			listView.setAdapter(myAdapter);

		} else {
			myAdapter.setsaleOnClickListener(saleonclick);
			myAdapter.setbuyonclick(buyonclick);
			myAdapter.datachanged(preLoginDataList);
			
		}
		// 设置更新时间
		// wuhan
		int i = myAdapter.getList().size();
		Map<String, Object> lastMap = myAdapter.getList().get(i - 1);
		tvReNewTime.setText(lastMap.get(Prms.PRELOGIN_QUERY_UPDATEDATE).toString());
		showGuide();
		
	}
	
	
	
	
	
	@Override
	protected void onResume() {
		super.onResume();
		boolean isLogin = BaseDroidApp.getInstanse().isLogin();
		if (isLogin) {
			headerView.setVisibility(View.VISIBLE);
			left_title2_ll.setVisibility(View.GONE);
			left_title_ll.setVisibility(View.GONE);
			btnReNew.setVisibility(View.GONE);
			hineTitlebarLoginButton();
			// 发送获取贵金属数据的报文
			buyOnItermClickListener = new OnItemClickListener() {// 买入

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					flag = BUY;
					targetCurrencyStr = adapter.getList().get(position)
							.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
					sourceCurrencyCodeStr = adapter.getList().get(position)
							.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
					BaseHttpEngine.showProgressDialog();
					queryPrmsAccBalance();
				}
			};
			saleOnItermClickListener = new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					flag = SALE;
					targetCurrencyStr = adapter.getList().get(position)
							.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
					sourceCurrencyCodeStr = adapter.getList().get(position)
							.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
					BaseHttpEngine.showProgressDialog();
					queryPrmsAccBalance();
				}
			};
		} else {
			headerView.setVisibility(View.GONE);
			left_title2_ll.setVisibility(View.VISIBLE);
			left_title_ll.setVisibility(View.GONE);
			left_title.setText(getResources().getString(R.string.for_reference_only));
			btnReNew.setVisibility(View.VISIBLE);

			saleonclick = new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					myPosition = position;
					flag = SALE;
					BaseActivity.getLoginUtils(PrmsPricesActivity.this).exe(new LoginCallback() {

						@Override
						public void loginStatua(boolean isLogin) {
							// TODO Auto-generated method stub
							if (isLogin) {
								// 如果以登陆直接调用,如果没有登陆打开登陆页面
								
								// 登陆成功
								loginSuccess();
							}
						}
					});
				}
			};

			buyonclick = new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					flag = BUY;
					myPosition = position;
					BaseActivity.getLoginUtils(PrmsPricesActivity.this).exe(new LoginCallback() {

						@Override
						public void loginStatua(boolean isLogin) {
							// TODO Auto-generated method stub
							if (isLogin) {
								// 如果以登陆直接调用,如果没有登陆打开登陆页面
								
								// 登陆成功
								loginSuccess();
							}
						}
					});
				}
			};
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (HttpManager.mPollingRequestThread != null
				&& PollingRequestThread.pollingFlag) {
			LogGloble
					.e(TAG, "onPause() mPollingRequestThread  stopPolling()  ");
			LogGloble.e(TAG, "onResume() mPollingRequestThread  restart()  ");
			HttpManager.stopPolling();
		}
	}

	private Handler httpHandler = new Handler() {
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
			LogGloble.i("info", "resultHttpCode =" +resultHttpCode +"callbackObject =="+callbackObject);
			switch (msg.what) {

			// 正常http请求数据返回
			case ConstantGloble.HTTP_STAGE_CONTENT:
				BiiResponse biiResponse = (BiiResponse) ((Map<String, Object>) msg.obj)
						.get(ConstantGloble.HTTP_RESULT_DATA);
				List<BiiResponseBody> biiResponseBodys = biiResponse
						.getResponse();
				BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
				if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
					BaseHttpEngine.dissMissProgressDialog();
					return;
				}
				
				dataList = (List<Map<String, String>>) (biiResponseBody
						.getResult());
				//wuhan保存一下省连号：
//				String ibkNum = dataList.get("accountIbkNum");
//				LogGloble.i("info", "ibkNum=="+ibkNum);
//				LocalDataService.getInstance().saveIbkNum(ConstantGloble.Prms, ibkNum);
				// /add by fsm判断行情列表是否为空
				if (StringUtil.isNullOrEmpty(dataList)) {
					BaseDroidApp.getInstanse().showMessageDialog(
							getString(R.string.prms_no_price),
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									PrmsPricesActivity.this.finish();
								}
							});
				}

				if (adapter == null) {
					adapter = new PrmsPricesListAdapter1(
							PrmsPricesActivity.this, dataList);

					// wuhan
					adapter.setBuyOnClickListener(buyOnItermClickListener);
					adapter.setSaleOnClickListener(saleOnItermClickListener);
					adapter.notifyDataSetChanged();
					listView.setAdapter(adapter);

				} else {
					adapter.setBuyOnClickListener(buyOnItermClickListener);
					adapter.setSaleOnClickListener(saleOnItermClickListener);
					adapter.datachanged(dataList);
					
				}
				
				if(ffff&&prmsControl.ifhavPrmsAcc &&prmsControl.ifInvestMent){
					queryPrmsAccBalance();
					ffff = false;
				}
				// 设置更新时间

				 int i = adapter.getList().size();
				 Map<String, String> lastMap = adapter.getList().get(i - 1);
				 headerView.setText(getResources().getString(R.string.prms_str_price_header)
				 + lastMap.get(Prms.QUERY_TRADERATE_CREADTEDATE));
				showGuide();
				break;

			// 请求失败错误情况处理
			case ConstantGloble.HTTP_STAGE_CODE:

				BaseHttpEngine.dissMissProgressDialog();
				/**
				 * 执行code error 全局前拦截器
				 */
				if (BaseDroidApp.getInstanse().httpCodeErrorCallBackPre(
						resultHttpCode)) {
					break;
				}

				/**
				 * 执行callbackObject error code 回调前拦截器
				 */
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
					// add by wez 2012.11.06
					LogGloble.e(TAG, "NullPointerException ", e);
					throw e;
				} catch (ClassCastException e) {
					// add by wez 2012.11.06
					LogGloble.e(TAG, "ClassCastException ", e);
					throw e;
				}

				/**
				 * 执行code error 全局后拦截器
				 */
				if (BaseDroidApp.getInstanse().httpCodeErrorCallBackAfter(
						resultHttpCode)) {
					break;
				}

				/**
				 * 执行callbackObject code error 后拦截器
				 */
				if (callbackObject.httpCodeErrorCallBackAfter(resultHttpCode)) {
					break;
				}

				break;

			default:
				break;
			}

		}
	};

	@Override
	public void queryPrmsAccCallBack(Object resultObj) {
		super.queryPrmsAccCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;

		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		if (biiResponseBody.getResult() == null) {
			prmsControl.ifhavPrmsAcc = false;
			prmsControl.accMessage = null;
			prmsControl.accId = null;
		} else {
			//wuhan保存一下省连号：

			prmsControl.accMessage = (Map<String, String>) biiResponseBodys
					.get(0).getResult();
			String ibkNum = String.valueOf(prmsControl.accMessage.get(Prms.QUERY_PRMSACC_IBKNUM));
			LocalDataService.getInstance().saveIbkNum(ConstantGloble.Prms, ibkNum);
			prmsControl.accNum = String.valueOf(prmsControl.accMessage
					.get(Prms.QUERY_PRMSACC_ACCOUNT));
			prmsControl.accId = String.valueOf(prmsControl.accMessage
					.get(Prms.QUERY_PRMSACC_ACCOUNTID));
			prmsControl.ifhavPrmsAcc = true;
		}
		if (!prmsControl.ifhavPrmsAcc || !prmsControl.ifInvestMent) {
			getPopup();
		} else {
			queryPrmsPricePoling();
		}
	}


	private void showGuide() {
		getWindow().getDecorView().postDelayed(new Runnable() {

			@Override
			public void run() {
				if (isshowguid) {
					View textView = listView.findViewById(R.id.prms_price_listiterm1_buyprice);
					if (textView != null) {
						isshowguid = false;
						int[] location = new int[2];
						textView.getLocationInWindow(location);
						int diff = 10;
						RectF src = new RectF(location[0] - diff, location[1]
								- diff, location[0] + textView.getWidth()
								+ diff, location[1] + textView.getHeight()
								+ diff);
						CardWelcomGuideUtil.showCardPriceGuid(
								PrmsPricesActivity.this, src);
					} else {
						LogGloble.d(TAG, "testView is null");
					}
				}
			}
		}, 500);
	}
	boolean ffff = false;
	public void loginSuccess(){
		btnReNew.setVisibility(View.GONE);
		left_title2_ll.setVisibility(View.GONE);
		left_title_ll.setVisibility(View.GONE);
		headerView.setVisibility(View.VISIBLE);
		
		targetCurrencyStr = (String) myAdapter.getList().get(myPosition).get(Prms.PRELOGIN_QUERY_TARGETCURRENCYCODE);
		sourceCurrencyCodeStr = (String) myAdapter.getList().get(myPosition).get(Prms.PRELOGIN_QUERY_SOURCECURRENCYCODE);
		BaseHttpEngine.showProgressDialog();
		setLeftSelectedPosition(null);
		//再发一次获取贵金属信息的报文。//wuhan
//		queryPrmsPricePoling();
		checkRequestPsnInvestmentManageIsOpen();
		if(targetCurrencyStr !=null && !"".equals(targetCurrencyStr)){
			ffff = true;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		BaseDroidApp.getInstanse().setCurrentAct(this);
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
//		case 99:
//			switch (resultCode) {
//			case 111://wuhan item跳转
//				btnReNew.setVisibility(View.GONE);
//				left_title2_ll.setVisibility(View.GONE);
//				left_title_ll.setVisibility(View.GONE);
//				headerView.setVisibility(View.VISIBLE);
//				
//				targetCurrencyStr = (String) myAdapter.getList().get(myPosition).get(Prms.PRELOGIN_QUERY_TARGETCURRENCYCODE);
//				sourceCurrencyCodeStr = (String) myAdapter.getList().get(myPosition).get(Prms.PRELOGIN_QUERY_SOURCECURRENCYCODE);
//				targetCurrencyStr = "001";
//				sourceCurrencyCodeStr = "008";
//				BaseHttpEngine.showProgressDialog();
//				//再发一次获取贵金属信息的报文。//wuhan
//				queryPrmsPricePoling();
//				
//				queryPrmsAccBalance();
//				break;
//
//			default:
//				break;
//			}
//			break;
		
		case ConstantGloble.ACTIVITY_REQUEST_SETPRMSACC_CODE:// 设置贵金属账户
			switch (resultCode) {
			case RESULT_OK:
				prmsControl.ifhavPrmsAcc = true;
				BaseDroidApp.getInstanse().dismissMessageDialog();
				queryPrmsPricePoling();
				break;
			default:
				prmsControl.ifhavPrmsAcc = false;
				getPopup();
				break;
			}
			break;
		case ConstantGloble.ACTIVITY_REQUEST_MANAGE_CODE:// 开通理财服务
			switch (resultCode) {
			case RESULT_OK:
				prmsControl.ifInvestMent = true;
				if (prmsControl.ifhavPrmsAcc) {
					BaseDroidApp.getInstanse().dismissMessageDialog();
					queryPrmsPricePoling();
				} else {
					getPopup();
				}
				break;
			default:
				prmsControl.ifInvestMent = false;
				getPopup();
				break;
			}
			break;
		default:
			break;
		}
	}
	private boolean fromLogin = false;
	
	/**
	 * 初始化布局
	 */
	private void init() {
		
		String title = getResources().getString(R.string.prms_title_price_new);
		setTitle(title);
		prmsPricesView = mainInflater.inflate(R.layout.prms_listview, null);
		tabcontent.addView(prmsPricesView);
		left_title = (TextView) prmsPricesView.findViewById(R.id.left_title);
		// wuhan
		left_title2_ll = (LinearLayout) prmsPricesView.findViewById(R.id.left_title2_ll);
		left_title_ll = (LinearLayout) prmsPricesView.findViewById(R.id.left_title_ll);
		tvReNewTime = (TextView) prmsPricesView.findViewById(R.id.update_time);
		btnReNew = (Button) prmsPricesView.findViewById(R.id.manual_refresh);
		btnReNew.setOnClickListener(this);
		left_title = (TextView) prmsPricesView.findViewById(R.id.left_title);
		listView = (ListView) prmsPricesView.findViewById(R.id.listview);
		headerView = (TextView) findViewById(R.id.prms_priceheaderview);
		dataList  = new ArrayList<Map<String,String>>();
		boolean isLogin = BaseDroidApp.getInstanse().isLogin();
		if (isLogin) {
			BaseHttpEngine.showProgressDialogCanGoBack();
			checkRequestPsnInvestmentManageIsOpen();//是否开通理财
			//发送已登录的黄金行情报文
			queryPrmsPricePoling();
			btnReNew.setVisibility(View.GONE);
			left_title2_ll.setVisibility(View.GONE);
			left_title_ll.setVisibility(View.GONE);
			headerView.setVisibility(View.VISIBLE);
			hineTitlebarLoginButton();
			
			buyOnItermClickListener = new OnItemClickListener() {// 买入

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					flag = BUY;
					targetCurrencyStr = adapter.getList().get(position)
							.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
					sourceCurrencyCodeStr = adapter.getList().get(position)
							.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
					BaseHttpEngine.showProgressDialog();
					queryPrmsAccBalance();
				}
			};
			saleOnItermClickListener = new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					flag = SALE;
					targetCurrencyStr = adapter.getList().get(position)
							.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
					sourceCurrencyCodeStr = adapter.getList().get(position)
							.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
					BaseHttpEngine.showProgressDialog();
					queryPrmsAccBalance();
				}
			};

		} else {
			
			//发送没有登录时黄金行情。
			BaseHttpEngine.showProgressDialogCanGoBack();
			queryPrmsPricePoliPreLogin();
			btnReNew.setVisibility(View.VISIBLE);
			headerView.setVisibility(View.GONE);
			left_title2_ll.setVisibility(View.VISIBLE);
			left_title_ll.setVisibility(View.VISIBLE);
			left_title.setText(getResources().getString(R.string.for_reference_only));
			showTitlebarLoginButton();
			
			
			saleonclick = new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					myPosition = position;
					flag = SALE;
					fromLogin = true;
					BaseActivity.getLoginUtils(PrmsPricesActivity.this).exe(new LoginCallback() {

						@Override
						public void loginStatua(boolean isLogin) {
							// TODO Auto-generated method stub
							if (isLogin) {
								// 如果以登陆直接调用,如果没有登陆打开登陆页面

								// 登陆成功
								loginSuccess();
							}
						}
					});

					
					
					
					
//					Intent intent = new Intent(PrmsPricesActivity.this,LoginActivity.class);
//					intent.putExtra("isGold", true);
//					startActivityForResult(intent, 99);
//					BaseDroidApp.getInstanse().setMainItemAutoClick(true);
				}
			};

			buyonclick = new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					flag = BUY;
					myPosition = position;
					fromLogin = true;
					BaseActivity.getLoginUtils(PrmsPricesActivity.this).exe(new LoginCallback() {

						@Override
						public void loginStatua(boolean isLogin) {
							// TODO Auto-geneonrated method stub
							if (isLogin) {
								// 如果以登陆直接调用,如果没有登陆打开登陆页面
								
								// 登陆成功
								loginSuccess();
							}
						}
					});
//					Intent intent = new Intent(PrmsPricesActivity.this,
//							LoginActivity.class);
//					intent.putExtra("isGold", true);
//					startActivityForResult(intent, 99);
//					BaseDroidApp.getInstanse().setMainItemAutoClick(true);
				}
			};
		}

		// initRightButtonForTrade();
		// buyOnItermClickListener = new OnItemClickListener() {// 买入
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view, int
		// position, long id) {
		// flag = BUY;
		// targetCurrencyStr =
		// adapter.getList().get(position).get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
		// sourceCurrencyCodeStr =
		// adapter.getList().get(position).get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
		// BaseHttpEngine.showProgressDialog();
		// queryPrmsAccBalance();
		// }
		// };
		// saleOnItermClickListener = new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view, int
		// position, long id) {
		// flag = SALE;
		// targetCurrencyStr =
		// adapter.getList().get(position).get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
		// sourceCurrencyCodeStr =
		// adapter.getList().get(position).get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
		// BaseHttpEngine.showProgressDialog();
		// queryPrmsAccBalance();
		// }
		// };
	}
	
	
	@Override
	public void queryPrmsAccBalanceCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.queryPrmsAccBalanceCallBack(resultObj);
		if (isbalance)
			return;
		Intent intent;
		switch (flag) {
		case BUY:// 买入
			List<Map<String, Object>> buyList = prmsControl
					.getBuyCurrencyList(prmsControl.accBalanceList);
			for (Map<String, Object> map : buyList) {
				if (map.get(Prms.QUERY_PEMSACTBALANCE_CODE).equals(
						targetCurrencyStr)) {// 买入的时候
												// 用目标币种和
												// 持仓比对
					BaseDroidApp.getInstanse().getBizDataMap()
							.put(Prms.PRMS_PRICE, dataList);
					intent = new Intent();
					intent.setClass(this, PrmsTradeBuyActivity.class);
					intent.putExtra(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE,
							targetCurrencyStr);
					intent.putExtra(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE,
							sourceCurrencyCodeStr);
					intent.putExtra(Prms.PRMS_IFFROMPRICE, true);
					startActivity(intent);
					return;
				}
			}
			StringBuffer errorInfoSb = new StringBuffer();
			errorInfoSb
					.append(getString(R.string.prms_balancesale_null_error1));
			errorInfoSb.append(LocalData.Currency.get(targetCurrencyStr));
			errorInfoSb
					.append(getString(R.string.prms_balancesale_null_error2));
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					errorInfoSb.toString());
			break;
		case SALE:// 卖出
			List<Map<String, Object>> saleList = prmsControl
					.getSaleCurrencyList(prmsControl.accBalanceList);
			if (saleList.size() < 1) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getString(R.string.prms_balanceOne_null_error));
				return;
			}
			for (Map<String, Object> map : saleList) {// 卖出用 源币种比对
				if (map.get(Prms.QUERY_PEMSACTBALANCE_CODE).equals(
						sourceCurrencyCodeStr)) {
					BaseDroidApp.getInstanse().getBizDataMap()
							.put(Prms.PRMS_PRICE, dataList);
					intent = new Intent();
					intent.setClass(this, PrmsTradeSaleActivity.class);
					intent.putExtra(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE,
							sourceCurrencyCodeStr);
					intent.putExtra(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE,
							targetCurrencyStr);
					intent.putExtra(Prms.PRMS_IFFROMPRICE, true);
					startActivity(intent);
					return;
				}
			}
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.prms_balanceOne_null_error));
			break;
		case FASTBUY:// 快速交易买入
			prmsControl.getBuyCurrencyList(prmsControl.accBalanceList);
			BaseDroidApp.getInstanse().getBizDataMap()
					.put(Prms.PRMS_PRICE, dataList);
			intent = new Intent();
			intent.setClass(this, PrmsTradeBuyActivity.class);
			intent.putExtra(Prms.PRMS_IFFROMPRICE, true);
			startActivity(intent);
			break;
		case FASTSALE:// 快速交易没有 持仓
			if (prmsControl.getSaleCurrencyList(prmsControl.accBalanceList)
					.size() < 1) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getString(R.string.prms_balanceAll_null_error));
				return;
			}
			intent = new Intent();
			intent.setClass(this, PrmsTradeSaleActivity.class);
			BaseDroidApp.getInstanse().getBizDataMap()
					.put(Prms.PRMS_PRICE, dataList);
			intent.putExtra(Prms.PRMS_IFFROMPRICE, true);
			startActivity(intent);
			break;

		default:
			break;
		}

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ib_top_right_btn:// 快速交易
			if(right.getText().toString().equals("快速交易")){
				BaseDroidApp.getInstanse().showSelectBuyOrSaleDialog(getString(R.string.prms_buy),
						getString(R.string.prms_sale), rightBtnOnClickListenerForTrade);
			}
			else if(right.getText().toString().equals("登录")){
//				Intent intent = new Intent(PrmsPricesActivity.this, LoginActivity.class);
//				startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
				BaseActivity.getLoginUtils(PrmsPricesActivity.this).exe(new LoginCallback() {
					@Override
					public void loginStatua(boolean isLogin) {
					}
				});
				BaseDroidApp.getInstanse().setMainItemAutoClick(true);
			}
//			BaseDroidApp.getInstanse().showSelectBuyOrSaleDialog(
//					getString(R.string.prms_buy),
//					getString(R.string.prms_sale),
//					rightBtnOnClickListenerForTrade);
			break;
			//wuhan
		case R.id.manual_refresh:
			BaseHttpEngine.showProgressDialogCanGoBack();
			// 发送获取数据请求 并修改
			queryPrmsPricePoliPreLogin();
			break;
		default:
			break;
		}
	}

	@Override
	public void checkRequestPsnInvestmentManageIsOpenCallback(Object resultObj) {
		super.checkRequestPsnInvestmentManageIsOpenCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String isOpenOr = (String) biiResponseBody.getResult();
		prmsControl.ifInvestMent = StringUtil.parseStrToBoolean(isOpenOr);
		queryPrmsAcc();
	}

	@Override
	protected void hineTitlebarLoginButton() {
		// TODO Auto-generated method stub
		super.hineTitlebarLoginButton();
		right.setVisibility(View.VISIBLE);
		right.setText(getResources().getString(
				R.string.prms_str_trade_rightaway));
		right.setOnClickListener(this);
		rightBtnOnClickListenerForTrade = new OnClickListener() {
			@Override
			public void onClick(View view) {
				int tag = (Integer) view.getTag();
				switch (tag) {
				case CustomDialog.TAG_CANCLE:
					BaseDroidApp.getInstanse().dismissMessageDialog();
					break;
				case CustomDialog.TAG_RELA_ACC_TRAN:// 买入
					flag = FASTBUY;
					BaseDroidApp.getInstanse().dismissMessageDialog();
					BaseHttpEngine.showProgressDialog();
					queryPrmsAccBalance();
					break;
				case CustomDialog.TAG_COMMON_RECEIVER_TRAN:// 快速交易卖出
					BaseDroidApp.getInstanse().dismissMessageDialog();
					BaseHttpEngine.showProgressDialog();
					flag = FASTSALE;
					queryPrmsAccBalance();
					break;
				}

			}
		};
	}
	
	@Override
	protected void showTitlebarLoginButton() {
		// TODO Auto-generated method stub
		fromLogin= true;
		super.showTitlebarLoginButton();
		
	}
	
//	protected void initRightButtonForTrade() {
//		right.setVisibility(View.VISIBLE);
//		right.setText(getResources().getString(
//				R.string.prms_str_trade_rightaway));
//		right.setOnClickListener(this);
//		rightBtnOnClickListenerForTrade = new OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				int tag = (Integer) view.getTag();
//				switch (tag) {
//				case CustomDialog.TAG_CANCLE:
//					BaseDroidApp.getInstanse().dismissMessageDialog();
//					break;
//				case CustomDialog.TAG_RELA_ACC_TRAN:// 买入
//					flag = FASTBUY;
//					BaseDroidApp.getInstanse().dismissMessageDialog();
//					BaseHttpEngine.showProgressDialog();
//					queryPrmsAccBalance();
//					break;
//				case CustomDialog.TAG_COMMON_RECEIVER_TRAN:// 快速交易卖出
//					BaseDroidApp.getInstanse().dismissMessageDialog();
//					BaseHttpEngine.showProgressDialog();
//					flag = FASTSALE;
//					queryPrmsAccBalance();
//					break;
//				}
//
//			}
//		};
//
//	}

//	/**
//	 * wuhan 登录按钮初始化
//	 */
//	protected void initRightButtonForLogin() {
//		right.setVisibility(View.VISIBLE);
//		right.setText("登录");
//		right.setOnClickListener(this);
//		rightBtnOnClickListenerForLogin = new OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				Intent intent = new Intent(PrmsPricesActivity.this,
//						LoginActivity.class);
//				startActivityForResult(intent,
//						ConstantGloble.ACTIVITY_RESULT_CODE);
//				BaseDroidApp.getInstanse().setMainItemAutoClick(true);
//			}
//		};
//
//	}

}
