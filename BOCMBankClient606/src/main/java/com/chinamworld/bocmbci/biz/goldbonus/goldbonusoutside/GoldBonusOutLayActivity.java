package com.chinamworld.bocmbci.biz.goldbonus.goldbonusoutside;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.GoldBonus;
import com.chinamworld.bocmbci.bii.constant.Safety;
import com.chinamworld.bocmbci.biz.goldbonus.GoldBonusBaseActivity;
import com.chinamworld.bocmbci.biz.goldbonus.GoldbonusLocalData;
import com.chinamworld.bocmbci.biz.goldbonus.accountmanager.AccountManagerMainActivity;
import com.chinamworld.bocmbci.biz.goldbonus.busitrade.BusiTradeAvtivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpObserver;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.LoginTask.LoginCallback;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 积利金功能外置界面（wdk）
 * 
 */
public class GoldBonusOutLayActivity extends GoldBonusBaseActivity {

	private static final String TAG = "GoldBonusOutLayActivity";
	private View view;
	/** 更新时间 */
	private TextView beijing_time;
	/** 银行买入价 */
	private TextView prms_price_listiterm1_buyprice;
	/** 银行卖出价 */
	private TextView prms_price_listiterm1_saleprice;
	/** 刷新按钮 */
	private Button re_freash;
	/** 更新时间 */
	private String updateTime;
	/** 银行卖出价 */
	private String askPriceString;
	/** 银行买入价 */
	private String bidPriceString;
	/** 更新日期 */
	private String dateString;
	/** 涨跌判断 */
	private String upDown;

	private GoldBonusOutLayAdapter adapter;
	private ListView gold_bouns_lv;
	/** 产品信息总笔数 */
	private String recordNumber;
	/** 图标大小 */
	private int iconSize;
	/** 年化利率标题 */
	private TextView tvIssueRate;
	/** 排序方式 */
	private String ordType = "2";
	/** 产品列表 */
	private List<Map<String, Object>> productList;
	/** 查询更多，交易列表的Footer */
	private View footerView;
	private ImageView start_date_refreash;
	// 外置登陆请求吗
	public static final int QEQUESTCODELOGIN = 1;
	/** 是否登录 */
	private boolean isLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setTitle(getString(R.string.goldbonus_outside));
		getBackgroundLayout().setTitleNewText(R.string.goldbonus_outside);
		setContentView(R.layout.goldbonus_out_lay);
		// findViewById(R.id.sliding_body).setPadding(0, 0, 0, 0);
		// 隐藏左侧侧滑栏
		goneLeftView();
		Button back = (Button) findViewById(R.id.ib_back);
		// back.setVisibility(View.GONE);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ActivityTaskManager.getInstance().removeAllSecondActivity();
			}
		});
		getBackgroundLayout().setPaddingWithParent(0, 0, 0, 0);
		Button right = (Button) findViewById(R.id.ib_top_right_btn);
//		right.setText("登录");
		getBackgroundLayout().setRightButtonNewText("登录");
//		right.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
////				Intent intent = new Intent();
////				intent.setClass(GoldBonusOutLayActivity.this,
////						LoginActivity.class);
//				 GoldbonusLocalData.getInstance().ISSELLLOGIN="0";
////				startActivityForResult(intent, QEQUESTCODELOGIN);
//				BaseActivity.getLoginUtils(GoldBonusOutLayActivity.this).exe(new LoginCallback() {
//
//							@Override
//							public void loginStatua(boolean arg0) {
//								// TODO Auto-generated method stub
//								Intent intent = new Intent(
//										GoldBonusOutLayActivity.this,
//										AccountManagerMainActivity.class);
//								startActivity(intent);
//								finish();
//							}
//						});
//			}
//		});
		/**设置登录按钮的点击事件**/
		getBackgroundLayout().setRightButtonNewClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent();
//				intent.setClass(GoldBonusOutLayActivity.this,
//						LoginActivity.class);
				GoldbonusLocalData.getInstance().ISSELLLOGIN="0";
//				startActivityForResult(intent, QEQUESTCODELOGIN);
				BaseActivity.getLoginUtils(GoldBonusOutLayActivity.this).exe(new LoginCallback() {

					@Override
					public void loginStatua(boolean arg0) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(
								GoldBonusOutLayActivity.this,
								AccountManagerMainActivity.class);
						startActivity(intent);
						finish();
					}
				});
			}
		});
		Button showBtn = (Button) this.findViewById(R.id.btn_show);
		showBtn.setVisibility(View.GONE);
		iconSize = getResources().getDimensionPixelSize(R.dimen.dp_two_zero) / 2;
		tvIssueRate = (TextView) findViewById(R.id.tv_issueRate);
		footerView = LayoutInflater.from(this).inflate(
				R.layout.epay_tq_list_more, null);

		start_date_refreash = (ImageView) findViewById(R.id.start_date_refreash);
		start_date_refreash.setOnClickListener(issueRateListener);

		beijing_time = (TextView) findViewById(R.id.time);
		re_freash = (Button) findViewById(R.id.manual_refresh);
		re_freash.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseHttpEngine.showProgressDialog();
				httpTools.requestHttpOutlay(Safety.PSNBMPSCREATCONVERSATION,
						"requestPSNBmpsCreatConversationCallBack", null, false);
			}
		});
		prms_price_listiterm1_buyprice = (TextView) findViewById(R.id.prms_price_listiterm1_buyprice);
		prms_price_listiterm1_saleprice = (TextView) findViewById(R.id.prms_price_listiterm1_saleprice);
		gold_bouns_lv = (ListView) findViewById(R.id.gold_bouns_lv);
		prms_price_listiterm1_buyprice
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
//						 Intent intent = new Intent(
//						 GoldBonusOutLayActivity.this,
//						 LoginActivity.class);
						 GoldbonusLocalData.getInstance().ISSELLLOGIN="1";
//						 startActivityForResult(intent,
//						 REQUEST_LOGIN_CODE_BUY);

						BaseActivity.getLoginUtils(GoldBonusOutLayActivity.this).exe(new LoginCallback() {
							@Override
							public void loginStatua(boolean arg0) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(
										GoldBonusOutLayActivity.this,
										BusiTradeAvtivity.class);
								startActivity(intent);
								intent.putExtra(GoldBonus.PASSFLAG,
										REQUEST_LOGIN_CODE_SELL);
								finish();
							}
						});
					}
				});

		prms_price_listiterm1_saleprice
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
//						 Intent intent = new Intent(
//						 GoldBonusOutLayActivity.this,
//						 LoginActivity.class);
						 GoldbonusLocalData.getInstance().ISSELLLOGIN="2";
//						 startActivityForResult(intent,
//						 REQUEST_LOGIN_CODE_SELL);
						BaseActivity.getLoginUtils(GoldBonusOutLayActivity.this).exe(new LoginCallback() {

							@Override
							public void loginStatua(boolean arg0) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(
										GoldBonusOutLayActivity.this,
										BusiTradeAvtivity.class);
								startActivity(intent);
								intent.putExtra(GoldBonus.PASSFLAG, REQUEST_LOGIN_CODE_BUY);
								finish();
							}
						});
					}
				});
		// 请求登陆之前测conversation
		BaseHttpEngine.showProgressDialog();
		httpTools.requestHttpOutlay(Safety.PSNBMPSCREATCONVERSATION,
				"requestPSNBmpsCreatConversationCallBack", null, false);
	}

	/** 排序后的标题字段显示效果 */
	private void sortByIssueRate() {
		Drawable drawableSelect = null;
		if (ordType.equals("1")) {
			drawableSelect = getResources().getDrawable(
					R.drawable.bocinvt_sort_down);
		} else {
			drawableSelect = getResources().getDrawable(
					R.drawable.bocinvt_sort_up);
		}
		drawableSelect.setBounds(0, 0, iconSize, iconSize);
		// tvIssueRate.setCompoundDrawables(null, null, drawableSelect, null);
	}

	/** 为列表添加更多 */
	private void addFooterView() {
		int size = Integer.valueOf(recordNumber);
		if (productList.size() < size) {
			if (gold_bouns_lv.getFooterViewsCount() <= 0) {
				gold_bouns_lv.addFooterView(footerView);
				footerView.setClickable(true);
			}
		} else {
			if (gold_bouns_lv.getFooterViewsCount() > 0) {
				gold_bouns_lv.removeFooterView(footerView);
				footerView.setClickable(false);
			}
		}

		footerView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// requestPsnGoldBonusProductInfoQueryOutlay(false);

				BaseHttpEngine.showProgressDialog();
				httpTools.requestHttpOutlay(Safety.PSNBMPSCREATCONVERSATION,
						"requestPSNBmpsCreatConversationCallBackInfo", null,
						false);

			}
		});
	}

	/** 未登录会话ID返回 */
	public void requestPSNBmpsCreatConversationCallBackInfo(Object resultObj) {
		this.getHttpTools();
		String conversationId = (String) HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNull(conversationId)) {
			return;
		}
		BaseDroidApp
				.getInstanse()
				.getBizDataMap()
				.put(ConstantGloble.LOGIN_OUTLAY_PRECONVERSATIONID,
						conversationId);
		requestPsnGoldBonusProductInfoQueryOutlayMore(false);
	}

	/** 登陆前请求产品信息 */
	private void requestPsnGoldBonusProductInfoQueryOutlayMore(boolean refresh) {
		sortByIssueRate();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(GoldBonus.XPADGPERIODTYPE, "");// 期限类型
		params.put(GoldBonus.ORDTYPE, ordType);// 排序方式
		params.put(
				GoldBonus.CURRENTINDEX,
				StringUtil.isNullOrEmpty(productList) ? "0" : productList
						.size() + "");// 当前页
		params.put(GoldBonus.PAGESIZE, "10");// 每页显示条数
		params.put(GoldBonus.REFRESH, refresh + "");// 刷新标志
		this.getHttpTools().requestHttpOutlay(
				GoldBonus.PSNGOLDBONUSPRODUCTINFOQUERYOUTLAY,
				"requestPsnGoldBonusProductInfoQueryOutlayMoreCallback",
				params, true);
	}

	// 登陆前请求产品信息回调
	@SuppressWarnings("unchecked")
	public void requestPsnGoldBonusProductInfoQueryOutlayMoreCallback(
			Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(resultMap)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		recordNumber = (String) resultMap.get(GoldBonus.RECORDNUMBER);
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) resultMap
				.get(GoldBonus.LIST_NEW);
		if (StringUtil.isNullOrEmpty(productList)) {
			productList = resultList;
		} else {
			productList.addAll(resultList);
		}
		addFooterView();
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		} else {
			adapter = new GoldBonusOutLayAdapter(this, productList);
			gold_bouns_lv.setAdapter(adapter);
		}
		// tvIssueRate.setOnClickListener(issueRateListener);
	}

	/** 年化利率标题点击事件 */
	private OnClickListener issueRateListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (ordType.equals("1")) {
				ordType = "2";
				start_date_refreash.setImageDrawable(getResources()
						.getDrawable(R.drawable.bocinvt_sort_up));
			} else {
				ordType = "1";
				start_date_refreash.setImageDrawable(getResources()
						.getDrawable(R.drawable.bocinvt_sort_down));
			}
			sortByIssueRate();
			if (!StringUtil.isNullOrEmpty(productList)) {
				productList.clear();
			}
			requestPsnGoldBonusProductInfoQueryOutlay(true);
		}
	};

	// 登陆前请求牌价回调
	public void requestPsnGoldBonusPriceListQueryOutlayCallback(Object resultObj) {
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		GoldbonusLocalData.getInstance().psnGoldBonusPriceListQuery = resultMap;
		dateString = StringUtil.valueOf1((String) resultMap
				.get(GoldBonus.QUOTEDATE));
		askPriceString = StringUtil.append2Decimals(
				(String) resultMap.get(GoldBonus.ASKPRICE), 2);
		bidPriceString = StringUtil.append2Decimals(
				(String) resultMap.get(GoldBonus.BIDPRICE), 2);
		updateTime = StringUtil.valueOf1((String) resultMap
				.get(GoldBonus.QUOTETIME));
		upDown = (String) resultMap.get(GoldBonus.UPDOWN);
		beijing_time.setText(dateString + " " + updateTime);
		prms_price_listiterm1_buyprice.setText(paseEndZero(bidPriceString));
		prms_price_listiterm1_saleprice.setText(paseEndZero(askPriceString));
		Double upDownInt = Double.parseDouble(upDown);
		if (upDownInt > 0) {
			prms_price_listiterm1_buyprice.setTextColor(getResources()
					.getColor(R.color.fonts_pink));
			prms_price_listiterm1_saleprice.setTextColor(getResources()
					.getColor(R.color.fonts_pink));
		} else if (upDownInt < 0) {
			prms_price_listiterm1_buyprice.setTextColor(getResources()
					.getColor(R.color.fonts_green));
			prms_price_listiterm1_saleprice.setTextColor(getResources()
					.getColor(R.color.fonts_green));
		} else {
			prms_price_listiterm1_buyprice.setTextColor(getResources()
					.getColor(R.color.black));
			prms_price_listiterm1_saleprice.setTextColor(getResources()
					.getColor(R.color.black));
		}
		// goldBonusPriceListPollingQuery();
		ordType = "1";
		if (!StringUtil.isNullOrEmpty(productList)) {
			productList.clear();
		}
		requestPsnGoldBonusProductInfoQueryOutlay(true);
	}

	/** 登陆前请求产品信息 */
	private void requestPsnGoldBonusProductInfoQueryOutlay(boolean refresh) {
		sortByIssueRate();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(GoldBonus.XPADGPERIODTYPE, "");// 期限类型
		params.put(GoldBonus.ORDTYPE, ordType);// 排序方式
		params.put(
				GoldBonus.CURRENTINDEX,
				StringUtil.isNullOrEmpty(productList) ? "0" : productList
						.size() + "");// 当前页
		params.put(GoldBonus.PAGESIZE, "10");// 每页显示条数
		params.put(GoldBonus.REFRESH, refresh + "");// 刷新标志
		this.getHttpTools().requestHttpOutlay(
				GoldBonus.PSNGOLDBONUSPRODUCTINFOQUERYOUTLAY,
				"requestPsnGoldBonusProductInfoQueryOutlayCallback", params,
				true);
	}

	// 登陆前请求产品信息回调
	@SuppressWarnings("unchecked")
	public void requestPsnGoldBonusProductInfoQueryOutlayCallback(
			Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNullOrEmpty(resultMap)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
		recordNumber = (String) resultMap.get(GoldBonus.RECORDNUMBER);
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) resultMap
				.get(GoldBonus.LIST_NEW);
		if (StringUtil.isNullOrEmpty(productList)) {
			productList = resultList;
		} else {
			productList.addAll(resultList);
		}
		addFooterView();
		if (adapter != null) {
			adapter.datachanged(productList);
			gold_bouns_lv.setAdapter(adapter);
		} else {
			adapter = new GoldBonusOutLayAdapter(this, productList);
			gold_bouns_lv.setAdapter(adapter);
		}
		// tvIssueRate.setOnClickListener(issueRateListener);
	}

	/** 未登录会话ID返回 */
	public void requestPSNBmpsCreatConversationCallBack(Object resultObj) {
		this.getHttpTools();
		String conversationId = (String) HttpTools.getResponseResult(resultObj);
		if (StringUtil.isNull(conversationId)) {
			return;
		}
		BaseDroidApp
				.getInstanse()
				.getBizDataMap()
				.put(ConstantGloble.LOGIN_OUTLAY_PRECONVERSATIONID,
						conversationId);
		// 登陆前请求牌价的接口
		this.getHttpTools().requestHttpOutlay(
				GoldBonus.PSNGOLDBONUSPRICELISTQUERYOUTLAY,
				"requestPsnGoldBonusPriceListQueryOutlayCallback", null, false);
	}

	/** 7秒轮询 */
	private void goldBonusPriceListPollingQuery() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(GoldBonus.PSNGOLDBONUSPRICELISTQUERYOUTLAY);
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
				clearTimes();

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

				GoldbonusLocalData.getInstance().psnGoldBonusPriceListQuery = resultMap;
				dateString = StringUtil.valueOf1((String) resultMap
						.get(GoldBonus.QUOTEDATE));
				askPriceString = StringUtil.append2Decimals(
						(String) resultMap.get(GoldBonus.ASKPRICE), 2);
				bidPriceString = StringUtil.append2Decimals(
						(String) resultMap.get(GoldBonus.BIDPRICE), 2);
				updateTime = StringUtil.valueOf1((String) resultMap
						.get(GoldBonus.QUOTETIME));
				upDown = (String) resultMap.get(GoldBonus.UPDOWN);
				beijing_time.setText(dateString + " " + updateTime);
				i++;
				prms_price_listiterm1_buyprice.setText(bidPriceString);
				prms_price_listiterm1_saleprice.setText(askPriceString);
				int upDownInt = Integer.valueOf(upDown);
				if (upDownInt > 0) {
					prms_price_listiterm1_buyprice.setTextColor(getResources()
							.getColor(R.color.fonts_pink));
					prms_price_listiterm1_saleprice.setTextColor(getResources()
							.getColor(R.color.fonts_pink));
				} else if (upDownInt < 0) {
					prms_price_listiterm1_buyprice.setTextColor(getResources()
							.getColor(R.color.fonts_green));
					prms_price_listiterm1_saleprice.setTextColor(getResources()
							.getColor(R.color.fonts_green));
				} else {
					prms_price_listiterm1_buyprice.setTextColor(getResources()
							.getColor(R.color.black));
					prms_price_listiterm1_saleprice.setTextColor(getResources()
							.getColor(R.color.black));
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

	/** 清空更新时间 */
	private void clearTimes() {
		beijing_time.setText("");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (BaseDroidApp.getInstanse().isLogin()) { // 登陆状态
			if("1".equals( GoldbonusLocalData.getInstance().ISSELLLOGIN)){
				Intent intent = new Intent(GoldBonusOutLayActivity.this,
						BusiTradeAvtivity.class);
				intent.putExtra(GoldBonus.PASSFLAG, REQUEST_LOGIN_CODE_SELL);
				GoldbonusLocalData.getInstance().ISSELLLOGIN="0";
				startActivity(intent);
			}else if("2".equals(GoldbonusLocalData.getInstance().ISSELLLOGIN)){
				Intent intent = new Intent(GoldBonusOutLayActivity.this,
						BusiTradeAvtivity.class);
				intent.putExtra(GoldBonus.PASSFLAG, REQUEST_LOGIN_CODE_BUY);
				GoldbonusLocalData.getInstance().ISSELLLOGIN="0";
				startActivity(intent);
			}else {
				GoldbonusLocalData.getInstance().ISSELLLOGIN="0";
				Intent intent=new Intent(GoldBonusOutLayActivity.this, AccountManagerMainActivity.class);
				startActivity(intent);
			}
			
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// HttpManager.stopPolling();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_LOGIN_CODE_BUY:
			switch (resultCode) {
			case RESULT_OK:
				// 跳转买入界面
				Intent intent = new Intent(GoldBonusOutLayActivity.this,
						BusiTradeAvtivity.class);
				intent.putExtra(GoldBonus.PASSFLAG, REQUEST_LOGIN_CODE_SELL);
				startActivity(intent);
				finish();
				break;
			default:
				break;
			}
			break;
		case REQUEST_LOGIN_CODE_SELL:
			switch (resultCode) {
			case RESULT_OK:
				// 跳转卖出界面
				Intent intent = new Intent(GoldBonusOutLayActivity.this,
						BusiTradeAvtivity.class);
				intent.putExtra(GoldBonus.PASSFLAG, REQUEST_LOGIN_CODE_BUY);
				startActivity(intent);
				finish();
				break;

			default:
				break;
			}
		case QEQUESTCODELOGIN:
			switch (resultCode) {
			case RESULT_OK:
				// 跳转买入界面
				Intent intent = new Intent(GoldBonusOutLayActivity.this,
						AccountManagerMainActivity.class);
				startActivity(intent);
				finish();
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}

	public boolean doBiihttpRequestCallBackPre(BiiResponse response) {
		BaseHttpEngine.dissMissProgressDialog();
		List<BiiResponseBody> biiResponseBodyList = response.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodyList.get(0);
		if (!StringUtil.isNullOrEmpty(biiResponseBodyList)) {
			for (BiiResponseBody body : biiResponseBodyList) {
				if (GoldBonus.PSNGOLDBONUSPRICELISTQUERYOUTLAY
						.equals(biiResponseBody.getMethod())) {
					BiiHttpEngine.dissMissProgressDialog();
					BiiError biiError = biiResponseBody.getError();
					// 判断是否存在error
					if (biiError != null && biiError.getCode() != null) {

						if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
							return super.doBiihttpRequestCallBackPre(response);
						} else if (biiError.getCode().equals("XPADG.EG003")) {
							// 报错继续掉接口,屏蔽第一个接口的错误
							prms_price_listiterm1_buyprice.setText("-");
							prms_price_listiterm1_buyprice
									.setTextColor(getResources().getColor(
											R.color.black));
							prms_price_listiterm1_saleprice.setText("-");
							prms_price_listiterm1_saleprice
									.setTextColor(getResources().getColor(
											R.color.black));
							beijing_time.setText("-");
							requestPsnGoldBonusProductInfoQueryOutlay(true);
							return true;

						} else {
							// 报错继续掉接口
							prms_price_listiterm1_buyprice.setText("-");
							prms_price_listiterm1_buyprice
									.setTextColor(getResources().getColor(
											R.color.black));
							prms_price_listiterm1_saleprice.setText("-");
							prms_price_listiterm1_saleprice
									.setTextColor(getResources().getColor(
											R.color.black));
							beijing_time.setText("-");
							// requestPsnGoldBonusProductInfoQueryOutlay(true);
							return super.doBiihttpRequestCallBackPre(response);
						}

					}
				}
			}

		} else {
			return super.doBiihttpRequestCallBackPre(response);
		}

		return super.doBiihttpRequestCallBackPre(response);
	}

}
