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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.biz.prms.PrmsBaseActivity;
import com.chinamworld.bocmbci.biz.prms.PrmsNewBaseActivity;
import com.chinamworld.bocmbci.biz.prms.adapter.PrmsPricesListAdapter1;
import com.chinamworld.bocmbci.biz.prms.adapter.PrmsPricesListPreLoginAdapter1;
import com.chinamworld.bocmbci.biz.prms.myaccount.PrmsAccMeneActivity;
import com.chinamworld.bocmbci.biz.prms.myaccount.PrmsAccSettingActivity;
import com.chinamworld.bocmbci.biz.prms.query.PrmsQueryActivity;
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
import com.chinamworld.bocmbci.userwidget.investview.InvestHelpActivity;
import com.chinamworld.bocmbci.utils.CardWelcomGuideUtil;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.LoginTask.LoginCallback;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 贵金属更多页面
 * 
 * @author wh
 * zhaozq PrmsBaseActivity 换成 PrmsNewBaseActivity
 */
public class PrmsMoreActivity extends PrmsNewBaseActivity implements OnClickListener{
	private static final String TAG = "PrmsMoreActivity";

	private RelativeLayout buy_layout;
	private RelativeLayout sale_layout;
	private RelativeLayout query_layout;
	private RelativeLayout accmanager_layout;
	private RelativeLayout problem_layout;
	//	private RelativeLayout layout_prms;
	private boolean isLogin;
	private int flag;
	/** 快速交易买入 */
	private static final int FASTBUY = 2;
	/** 快速交易卖出 */
	private static final int FASTSALE = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//zhaozq 11yue30 annotation
		//View childView = mainInflater.inflate(R.layout.prms_more, null);
		//tabcontent.addView(childView);

		//zhaozq 11yue30 add
		setContentView(R.layout.prms_more);

		setTitle("更多");
		initUi();
		initData();
		initListener();
	}


	private void initUi() {
		buy_layout = (RelativeLayout) findViewById(R.id.buy_layout);
		sale_layout = (RelativeLayout) findViewById(R.id.sale_layout);
		query_layout = (RelativeLayout) findViewById(R.id.query_layout);
		accmanager_layout = (RelativeLayout) findViewById(R.id.accmanager_layout);
		problem_layout = (RelativeLayout) findViewById(R.id.problem_layout);
//		layout_prms = (RelativeLayout)findViewById(R.id.layout_prms);

	}

	private void initData() {
		isLogin = BaseDroidApp.getInstanse().isLogin();


	}

	private void initListener() {
		buy_layout.setOnClickListener(this);
		sale_layout.setOnClickListener(this);
		query_layout.setOnClickListener(this);
		accmanager_layout.setOnClickListener(this);
//		problem_layout.setOnClickListener(this);
//	layout_prms.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		//zhaozq 11yue30 annotation
		//super.onClick(v);
		switch (v.getId()) {
			case R.id.buy_layout:
			flag=FASTBUY;

				if (BaseDroidApp.getInstanse().isLogin()) {
					BaseHttpEngine.showProgressDialog();
					queryPrmsAccBalance();
				} else {
					BaseActivity.getLoginUtils(PrmsMoreActivity.this).exe(new LoginTask.LoginCallback() {

						@Override
						public void loginStatua(boolean isLogin) {
							if (isLogin) {
//								BaseHttpEngine.showProgressDialog();
//								queryPrmsAccBalance();
								BaseHttpEngine.showProgressDialog();
//                                queryPrmsAccBalance();
								checkRequestPsnInvestmentManageIsOpen();
							}
						}
					});
				}

//					BaseDroidApp.getInstanse().getBizDataMap()
//							.put(Prms.PRMS_PRICE, dataList);

				break;
			case R.id.sale_layout://
				flag=FASTSALE;
				if (BaseDroidApp.getInstanse().isLogin()) {
					BaseHttpEngine.showProgressDialog();
					queryPrmsAccBalance();
				} else {
					BaseActivity.getLoginUtils(PrmsMoreActivity.this).exe(new LoginTask.LoginCallback() {

						@Override
						public void loginStatua(boolean isLogin) {
							if (isLogin) {
//								BaseHttpEngine.showProgressDialog();
//								queryPrmsAccBalance();
								BaseHttpEngine.showProgressDialog();
//                                queryPrmsAccBalance();
								checkRequestPsnInvestmentManageIsOpen();
							}
						}
					});
				}


				break;
//				case R.id.layout_prms:
//					Intent prmsIntent = new Intent(PrmsMoreActivity.this, PrmsNewPricesActivity.class);
//					startActivity(prmsIntent);
//					finish();
//					break;
			case R.id.query_layout://交易查询
				if (BaseDroidApp.getInstanse().isLogin()) {
					Intent prmsQueryintent = new Intent(PrmsMoreActivity.this, PrmsQueryActivity.class);
					startActivity(prmsQueryintent);
					finish();
				} else {

					BaseActivity.getLoginUtils(PrmsMoreActivity.this).exe(new LoginTask.LoginCallback() {

						@Override
						public void loginStatua(boolean isLogin) {
							if (isLogin) {
								Intent prmsQueryintent = new Intent(PrmsMoreActivity.this, PrmsQueryActivity.class);
								startActivity(prmsQueryintent);
								finish();
							}
						}
					});
				}


				break;
			case R.id.accmanager_layout://我的账户贵金属
				if (BaseDroidApp.getInstanse().isLogin()) {
					Intent prmsAccMeneintent = new Intent(PrmsMoreActivity.this, PrmsAccMeneActivity.class);
					startActivity(prmsAccMeneintent);
					finish();
				} else {

					BaseActivity.getLoginUtils(PrmsMoreActivity.this).exe(new LoginTask.LoginCallback() {

						@Override
						public void loginStatua(boolean isLogin) {
							if (isLogin) {
								Intent prmsAccMeneintent = new Intent(PrmsMoreActivity.this, PrmsAccMeneActivity.class);
								startActivity(prmsAccMeneintent);
								finish();
							}
						}
					});
				}


				break;
			case R.id.problem_layout://帮助
//				InvestHelpActivity.showHelpMessage(this,getString(R.string.prms_help_message));
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
	public void queryPrmsAccCallBack(Object resultObj) {
		super.queryPrmsAccCallBack(resultObj);

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
				queryPrmsAccBalance();



		}
	}


	@Override
	public void queryPrmsAccBalanceCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.queryPrmsAccBalanceCallBack(resultObj);
		if (isbalance)
			return;
		Intent intent;
		switch (flag) {
            case FASTBUY:// 快速交易买入
				PrmsNewPricesActivity.prmsFlagGoWay = 2;
                prmsControl.getBuyCurrencyList(prmsControl.accBalanceList);
                intent = new Intent();
                intent.setClass(this, PrmsTradeBuyActivity.class);
                intent.putExtra(Prms.PRMS_IFFROMPRICE, true);
                startActivity(intent);
                break;
            case FASTSALE:// 快速交易没有 持仓
				PrmsNewPricesActivity.prmsFlagGoWay = 2;
                if (prmsControl.getSaleCurrencyList(prmsControl.accBalanceList)
                        .size() < 1) {
                    BaseDroidApp.getInstanse().showInfoMessageDialog(
                            getString(R.string.prms_balanceAll_null_error));
                    return;
                }
                intent = new Intent();
                intent.setClass(this, PrmsTradeSaleActivity.class);
                intent.putExtra(Prms.PRMS_IFFROMPRICE, true);
                startActivity(intent);
                break;

			default:
				break;
		}

	}
}
