package com.chinamworld.bocmbci.biz.finc;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiApi;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.biz.bocinvt.productlist.TestInvtEvaluationAnswerActivity;
import com.chinamworld.bocmbci.biz.finc.accmanager.FincAccManagerMenuActivity;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.biz.finc.fundacc.FincFundAccMainActivity;
import com.chinamworld.bocmbci.biz.finc.fundprice.FincFundDetailActivityNew;
import com.chinamworld.bocmbci.biz.finc.fundprice.FundPricesActivityNew;
import com.chinamworld.bocmbci.biz.finc.fundprice.FundPricesMenuActivity;
import com.chinamworld.bocmbci.biz.finc.myfund.MyFincMainActivity;
import com.chinamworld.bocmbci.biz.finc.orcm.OrcmProductListActivity;
import com.chinamworld.bocmbci.biz.finc.query.FincQueryDQDEActivity;
import com.chinamworld.bocmbci.biz.finc.query.FundDQDEMenuActivity;
import com.chinamworld.bocmbci.biz.finc.query.FundqueryMenuActivity;
import com.chinamworld.bocmbci.biz.finc.trade.FincTradeBuyActivity;
import com.chinamworld.bocmbci.biz.finc.trade.FincTradeScheduledBuyActivity;
import com.chinamworld.bocmbci.biz.invest.activity.InvesAgreeActivity;
import com.chinamworld.bocmbci.biz.invest.constant.InvestConstant;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.GetPhoneInfo;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class FincBaseActivity extends BaseActivity implements OnClickListener, OnItemClickListener{

	private static final String TAG = "FincBaseActivity";
	protected ActivityTaskManager activityTaskManager = ActivityTaskManager
			.getInstance();
	protected FincControl fincControl = FincControl.getInstance();
	/**
	 * 返回按钮
	 */
	protected Button back;
	/**
	 * 返回按钮图片
	 */
	protected ImageView backImage;
	/**
	 * 右边按钮
	 */
	protected Button right;
	/**
	 * 主页面布局
	 */
	protected LinearLayout tabcontent;
	/**
	 * 获取文件
	 */
	protected LayoutInflater mainInflater;
	protected OnClickListener rightBtnOnClickListenerForTrade;
	/** 需要判断 买入和定投的交易 */
	protected int httpFlag;
	/** 快速交易 买入 */
	protected static final int FASTDEALBUY = 91;
	/** 快速交易 定投 */
	protected static final int FASTDEALSCHEDUBUY = 92;
	/** 定投  有效定投申请、已失效定投申请 */
	protected static final int DQDE_AVAILABLE = 1010;
	protected static final int DQDE_UNAVAILABLE = 1020;

	protected static final int FLAG_DDABORT = 2;//撤消
	protected static final int FLAG_PAUSE = 1;//暂停
	protected static final int FLAG_RESUME = 0;//恢复
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		GetPhoneInfo.initActFirst(this);
		super.onCreate(savedInstanceState);
		baseinit();
	}

	public void setRightToMainHome(){
		right.setVisibility(View.VISIBLE);
		right.setText(getString(R.string.go_main));
		right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				activityTaskManager.removeAllSecondActivity();
				goToMainActivity();
			}
		});
	}

	/**
	 * 获取tocken
	 */
	public void requestPSNGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPSNGetTokenIdCallback");
	}

	/**
	 * 获取tokenId----回调
	 * 
	 * @param resultObj
	 *            :返回结果
	 */
	public void requestPSNGetTokenIdCallback(Object resultObj) {
	}

	/**
	 * 请求密码控件随机数
	 */
	public void requestForRandomNumber(String conversationId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.AQUIRE_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId(conversationId);
		// 获取 随机数
		HttpManager.requestBii(biiRequestBody, this,
				"queryRandomNumberCallBack");
	}

	/**
	 * 请求密码控件随机数 回调
	 * 
	 * @param resultObj
	 */
	public void queryRandomNumberCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// 加密控件设置随机数
		fincControl.randomNumber = (String) biiResponseBody.getResult();
	}

	/**
	 * 任务提示框
	 */
	public void getPopup() {
		View popupView = LayoutInflater.from(this).inflate(
				R.layout.finc_task_notify, null);
		// 关闭按钮
		ImageView taskPopCloseButton = (ImageView) popupView
				.findViewById(R.id.top_right_close);
		// 设定账户
		LinearLayout accButtonView = (LinearLayout) popupView
				.findViewById(R.id.forex_acc_button_show);
		LinearLayout accTextView = (LinearLayout) popupView
				.findViewById(R.id.forex_acc_text_hide);
		// TextView setAccButton = (TextView) popupView
		// .findViewById(R.id.forex_acc_button);

		// 理财服务功能
		LinearLayout moneyButtonView = (LinearLayout) popupView
				.findViewById(R.id.forex_money_button_show);
		LinearLayout moneyTextView = (LinearLayout) popupView
				.findViewById(R.id.forex_money_text_hide);

		// 风险评估
		LinearLayout risktestBtnLayout = (LinearLayout) popupView
				.findViewById(R.id.finc_risk_button_show);
		risktestBtnLayout.setVisibility(View.GONE);
		LinearLayout risktestTextLayout = (LinearLayout) popupView
				.findViewById(R.id.finc_risk_text_hide);
		// TextView ristTestButton = (TextView) popupView
		// .findViewById(R.id.finc_risktest_button);

		// 先判断是否开通投资理财服务
		if (fincControl.ifInvestMent && fincControl.ifhaveaccId) {
			// finishPopupWindow();
			return;
		}
		if (fincControl.ifInvestMent) {// 已经开通投资理财
			moneyButtonView.setVisibility(View.GONE);
			moneyTextView.setVisibility(View.VISIBLE);
		} else {// 没有开通投资理财服务开通投资理财服务
			moneyButtonView.setVisibility(View.VISIBLE);
			moneyTextView.setVisibility(View.GONE);
			moneyButtonView.setOnClickListener(new OnClickListener() {
				// @Override
				public void onClick(View v) {
					// 跳转到投资理财服务协议页面
					Intent gotoIntent = new Intent(BaseDroidApp.getInstanse()
							.getCurrentAct(), InvesAgreeActivity.class);
					startActivityForResult(gotoIntent,
							ConstantGloble.ACTIVITY_REQUEST_MANAGE_CODE);

				}
			});
		}
		if (fincControl.ifhaveaccId) {// 有基金账户
			accButtonView.setVisibility(View.GONE);
			accTextView.setVisibility(View.VISIBLE);
		} else {// 没基金账户 设定 基金账户
			accButtonView.setVisibility(View.VISIBLE);
			accTextView.setVisibility(View.GONE);
			if (fincControl.ifInvestMent) {
				accButtonView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 到设定基金账户设定页面
						Intent gotoIntent = new Intent(BaseDroidApp
								.getInstanse().getCurrentAct(),
								FincFundAccMainActivity.class);
						startActivityForResult(
								gotoIntent,
								ConstantGloble.ACTIVITY_REQUEST_SETFINCSACC_CODE);
					}
				});
			} else {
				accButtonView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						CustomDialog.toastInCenter(
								BaseDroidApp.getInstanse().getCurrentAct(),
								BaseDroidApp
								.getInstanse()
								.getCurrentAct()
								.getString(
										R.string.bocinvt_task_toast_1));

					}
				});
			}
		}
		// 关闭按钮事件
		taskPopCloseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseDroidApp.getInstanse().dismissMessageDialog();
				// modi by fsm
				if (BaseDroidApp.getInstanse().getCurrentAct() instanceof FincQueryDQDEActivity)
					BaseDroidApp.getInstanse().getCurrentAct().finish();
				
				// 503add
				if (BaseDroidApp.getInstanse().getCurrentAct() instanceof FincFundDetailActivityNew)
					ActivityTaskManager.getInstance().removeAllActivity();
//					BaseDroidApp.getInstanse().getCurrentAct().finish();
				
				if (BaseDroidApp.getInstanse().getCurrentAct() instanceof FundPricesActivityNew) {
//					ActivityTaskManager.getInstance().removeAllSecondActivity();
					finish();
				}
				//TODO 基金推荐弹框，点击X之后，返回到首页
				if (BaseDroidApp.getInstanse().getCurrentAct() instanceof OrcmProductListActivity) {
					finish();
				}
			}
		});
		// taskPop.setFocusable(true);
		BaseDroidApp.getInstanse().showAccountMessageDialog(popupView);

	}
	
	/**
	 * 风险评估 P601改造任务提示框
	 */
	public void getPopupForRisk() {
		View popupView = LayoutInflater.from(this).inflate(
				R.layout.finc_task_notify, null);
		// 关闭按钮
		ImageView taskPopCloseButton = (ImageView) popupView
				.findViewById(R.id.top_right_close);
		// title
		TextView titleTextView = (TextView) popupView
				.findViewById(R.id.tv_acc_account_accountState);
		titleTextView.setText("您需要完成风险评估才可以进行基金交易");
		
		// 设定账户
		LinearLayout accButtonView = (LinearLayout) popupView
				.findViewById(R.id.forex_acc_button_show);
		accButtonView.setVisibility(View.GONE);

		// 理财服务功能
		LinearLayout moneyButtonView = (LinearLayout) popupView
				.findViewById(R.id.forex_money_button_show);
		moneyButtonView.setVisibility(View.GONE);

		// 风险评估
		LinearLayout risktestBtnLayout = (LinearLayout) popupView
				.findViewById(R.id.finc_risk_button_show);
		risktestBtnLayout.setVisibility(View.VISIBLE);
		LinearLayout risktestTextLayout = (LinearLayout) popupView
				.findViewById(R.id.finc_risk_text_hide);
		
		// 先判断是否开通投资理财服务
		if (fincControl.ifdorisk) {
			// finishPopupWindow();
			risktestBtnLayout.setVisibility(View.GONE);
			risktestTextLayout.setVisibility(View.VISIBLE);
			return;
		}
		// 未风险认证
		// 理财服务并且有账户
		// 开通风险认证可点击
		risktestBtnLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(BaseDroidApp.getInstanse()
						.getCurrentAct(),
						TestInvtEvaluationAnswerActivity.class);
				intent.putExtra(InvestConstant.RISKTYPE,
						InvestConstant.FUNDRISK);
				intent.putExtra(ConstantGloble.BOCINVT_ISNEWEVA, false);
				// intent.putExtra(InvestConstant.FROMMYSELF, true);
				BaseDroidApp
				.getInstanse()
				.getCurrentAct()
				.startActivityForResult(intent,
						InvestConstant.FUNDRISK);

			}
		});
		// 关闭按钮事件
		taskPopCloseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseDroidApp.getInstanse().dismissMessageDialog();
				// modi by fsm
//				if (BaseDroidApp.getInstanse().getCurrentAct() instanceof FincQueryDQDEActivity)
//					BaseDroidApp.getInstanse().getCurrentAct().finish();
//				
//				// 503add
//				if (BaseDroidApp.getInstanse().getCurrentAct() instanceof FincFundDetailActivityNew)
//					ActivityTaskManager.getInstance().removeAllActivity();
////					BaseDroidApp.getInstanse().getCurrentAct().finish();
//				
//				if (BaseDroidApp.getInstanse().getCurrentAct() instanceof FundPricesActivityNew) {
////					ActivityTaskManager.getInstance().removeAllSecondActivity();
//					finish();
//				}
//				//TODO 基金推荐弹框，点击X之后，返回到首页
//				if (BaseDroidApp.getInstanse().getCurrentAct() instanceof OrcmProductListActivity) {
//					finish();
//				}
				
				// 503add
				if (BaseDroidApp.getInstanse().getCurrentAct() instanceof FincFundDetailActivityNew)
					ActivityTaskManager.getInstance().removeAllActivity();
//					BaseDroidApp.getInstanse().getCurrentAct().finish();
				
				if (BaseDroidApp.getInstanse().getCurrentAct() instanceof FundPricesActivityNew) {
//					ActivityTaskManager.getInstance().removeAllSecondActivity();
					finish();
				}
				//TODO 基金推荐弹框，点击X之后，返回到首页
				if (BaseDroidApp.getInstanse().getCurrentAct() instanceof OrcmProductListActivity) {
					finish();
				}
			}
		});
		// taskPop.setFocusable(true);
		BaseDroidApp.getInstanse().showAccountMessageDialog(popupView);

	}

	/**
	 * 请求发送手机验证码到手机
	 * 
	 * @Author xyl
	 */
	protected void sendMSCToMobile(String conversationId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.SET_SENDMSC);
		biiRequestBody.setConversationId(conversationId);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "sendMSCToMobileCallback");
	}

	/**
	 * 请求发送手机验证码到手机 返回结果处理
	 * 
	 * @Author xyl
	 * @param resultObj
	 *            返回结果
	 */
	public void sendMSCToMobileCallback(Object resultObj) {
	}

	/**
	 * 设定基金他账户
	 * 
	 * @Author xyl
	 * @param tokenId
	 *            防重
	 * @param taAccount
	 *            基金他账户
	 * @param regOrgCode
	 */
	protected void fundtaAccSetting(String tokenId, String taAccount,
			String regOrgCode) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_FUNDTAACCOUNT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Finc.TOKEN, tokenId);
		map.put(Finc.FINC_FUNDTAACCOUNT_TAACCOUNT, taAccount);
		map.put(Finc.FINC_FUNDTAACCOUNT_REGORGCODE, regOrgCode);
		biiRequestBody.setParams(map);
		HttpManager
		.requestBii(biiRequestBody, this, "fundtaAccSettingCallback");
	}

	/**
	 * 设定基金他账户
	 * 
	 * @Author xyl
	 * @param resultObj
	 */
	public void fundtaAccSettingCallback(Object resultObj) {
	}

	/**2014-11-3 dxd
	 * 基金快速赎回额度查询
	 * @param fundCode
	 */
	protected void getFastSellFund(String fundCode) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_QUICKSELLQUOTAQUERY);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Finc.I_FUNDCODE, fundCode);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"getFastSellFundCallback");
	}

	/**
	 * 基金快速赎回额度查询 回调处理
	 * @param resultObj
	 */
	public void getFastSellFundCallback(Object resultObj) {

	}

	/**2014-11-3 dxd
	 * 基金基本信息查询
	 * @param fundCode
	 */
	protected void getFincFund(String fundCode) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_GETFUNDDETAIL);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Finc.I_FUNDCODE, fundCode);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"getFincFundCallback");
	}

	/**
	 * 基金基本信息查询  回调处理
	 * @param resultObj
	 */
	public void getFincFundCallback(Object resultObj) {

	}

	/**
	 * 获取所有的基金公司
	 * 
	 * @Author xyl
	 */
	protected void getFundCompanyList() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_GETFUNDCOMPANCYLIST);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this,
				"getFundCompanyListCallback");
	}

	/**
	 * 查询所有的基金公司回调处理
	 * 
	 * @Author xyl
	 * @param resultObj
	 */
	public void getFundCompanyListCallback(Object resultObj) {

	}

	/**
	 * 查询所有关注的基金
	 * 
	 * @Author xyl
	 */
	protected void getAttentionedFund() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_GETUSERPROFILE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Finc.FINC_KEY, ConstantGloble.FINC_KEY);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"getAttentionedFundCallback");
	}

	/**
	 * 查询所有关注的基金 返回结果处理
	 * 
	 * @Author xyl
	 * @param resultObj
	 */
	public void getAttentionedFundCallback(Object resultObj) {
	}

	/**
	 * 设定基金关注
	 * 
	 * @Author xyl
	 * @param tokenId
	 *            防重
	 * @param
	 *            要设定关注的基金代码
	 */
	protected void setAttentionedFund(String tokenId, String fundCode) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_SETUSERPROFILE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Finc.FINC_KEY, ConstantGloble.FINC_KEY);
		map.put(Finc.TOKEN, tokenId);
		map.put(Finc.FINC_VALUE, fundCode);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"setAttentionedFundCallback");
	}

	/**
	 * 设定要关注的基金代码
	 * 
	 * @Author xyl
	 * @param resultObj
	 */
	public void setAttentionedFundCallback(Object resultObj) {
	}

	/**
	 * 基金组合查询
	 * 
	 * @Author xyl
	 */
	protected void combainQueryFundInfos(int currentIndex, int pageSize,
			String currencyCode, String fundCompanyCode, String risklv,
			String fntype , String fundProductTypeStr,String fundState,
			String sortFlag ,String sortField) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.PSNQUERYFUNDDETAIL);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		if (getString(R.string.all).equals(fundCompanyCode)) {// 如果是全部 就传入空
			fundCompanyCode = null;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put(Finc.COMBINQUERY_CURRENTINDEX, String.valueOf(currentIndex));
		params.put(Finc.COMBINQUERY_PAGESIZE, String.valueOf(pageSize));
		params.put(Finc.PSNQUERYFUNDDETAIL_CURRENCYCODE, currencyCode);
		params.put(Finc.PSNQUERYFUNDDETAIL_COMPANY, fundCompanyCode);
		params.put(Finc.PSNQUERYFUNDDETAIL_RISKGRADE, risklv);
		params.put(Finc.PSNQUERYFUNDDETAIL_FUNDKIND, fntype);
		params.put(Finc.PSNQUERYFUNDDETAIL_FUNDTYPE, fundProductTypeStr);
		params.put(Finc.PSNQUERYFUNDDETAIL_FUNDSTATE, fundState);
		params.put(Finc.PSNQUERYFUNDDETAIL_SORTFLAG, sortFlag);
		params.put(Finc.PSNQUERYFUNDDETAIL_SORTFIELD, sortField);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this,
				"combainQueryFundInfosCallback");
	}

	/**
	 * 基金组合查询
	 * */
	public void combainQueryFundInfosCallback(Object resultObj) {
	}
	/**
	 * 基金行情查询 401 快速查询
	 * 
	 * @param fundInfo
	 *            基金代码或名称
	 */
	public void fastQuery(String fundInfo,Integer currentIndex,String fundState,
			String sortFlag ,String sortField) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.PSNQUERYFUNDDETAIL);
//		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
//				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> params = new HashMap<String, String>();
		params.put(Finc.PSNQUERYFUNDDETAIL_FUNDINFO, fundInfo);
		params.put(Finc.COMBINQUERY_CURRENTINDEX, String.valueOf(currentIndex));
		params.put(Finc.COMBINQUERY_PAGESIZE, String.valueOf(10));
		params.put(Finc.PSNQUERYFUNDDETAIL_FUNDTYPE, "00");
		params.put(Finc.PSNQUERYFUNDDETAIL_FUNDKIND, "00");
		params.put(Finc.PSNQUERYFUNDDETAIL_FUNDSTATE, fundState);
		params.put(Finc.PSNQUERYFUNDDETAIL_SORTFLAG, sortFlag);
		params.put(Finc.PSNQUERYFUNDDETAIL_SORTFIELD, sortField);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "fastQueryCallback");
	}

	/**
	 * 
	 * @param resultObj
	 */
	public void fastQueryCallback(Object resultObj) {
	}
	/**
	 * 查询基金行情
	 * 
	 * @Author xyl
	 */
	protected void queryfundinfos() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_QUERYFUNDINFOS);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "queryfundinfosCallback");
	}

	/**
	 * 基金行情 返回结果处理
	 * 
	 * @Author xyl
	 * @param resultObj
	 */
	public void queryfundinfosCallback(Object resultObj) {
	}



	/**
	 * 根据基金代码查询基金详情， FundInfo wms 基金
	 * 
	 * @Author xyl
	 * @param fundCodes
	 *            要查询基金的基金代码
	 */
	public void queryfundDetailByFundCode(List<String> fundCodes) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_FUNDINFO_METHOD);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.FINC_FUNDINFO_FUNDCODES, fundCodes);
		biiRequestBody.setParams(map);
		// HttpManager.requestBii(biiRequestBody, this,
		// "queryfundDetailByFundCodeCallback");
		HttpManager.requestBii_withUrl(BiiApi.FINCADDRESS, biiRequestBody,
				this, "queryfundDetailByFundCodeCallback");
	}

	/**
	 * 根据基金代码查询到的基金详情 fundInfo wms 基金
	 * 
	 * @Author xyl
	 * @param resultObj
	 *            查询到的结果
	 */
	public void queryfundDetailByFundCodeCallback(Object resultObj) {
	}

	/**
	 * 
	 * @Author xyl 根据基金公司代码查询基金详情
	 * @param fundCompanyCode
	 *            基金公司代码
	 * @param filterForex
	 *            买入标识 数字，可以为空 “0”:当日买入标识 “1”: 定期定额申购买入标识
	 */
	protected void queryfundDetailByFundComanyCode(String fundCompanyCode,
			String filterForex) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_QUERYINFOBYCOMPANY);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Finc.FINC_QUERYINFOBYCOMPANY_FUNDCOMPANYCODE, fundCompanyCode);
		map.put(Finc.FINC_QUERYINFOBYCOMPANY_FILTERFOREX, filterForex);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"queryfundDetailByFundComanyCodeCallback");
	}

	/**
	 * 根据基金公司代码查询到的基金详情
	 * 
	 * @Author xyl
	 * @param resultObj
	 *            查询到的结果
	 */
	public void queryfundDetailByFundComanyCodeCallback(Object resultObj) {
	}

	/**
	 * 菜单处理
	 * 
	 * @author xyl
	 */
	@Override
	protected boolean selectedMenuItemHandler(Activity context, ImageAndText menuItem) {
		super.selectedMenuItemHandler(context, menuItem);
		Intent intent = new Intent();
		String menuId = menuItem.MenuID;
		if(menuId.equals("finc_1")){// 基金行情
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof FundPricesMenuActivity)) {
				fincControl.cleanAllData();
				ActivityTaskManager.getInstance().removeAllSecondActivity();
				intent.setClass(this, FundPricesMenuActivity.class);
				context.startActivity(intent);
			}
			
		}
		else if(menuId.equals("finc_2")){// 我的基金
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof MyFincMainActivity)) {
				fincControl.cleanAllData();
				ActivityTaskManager.getInstance().removeAllSecondActivity();
				intent.setClass(this, MyFincMainActivity.class);
				context.startActivity(intent);
			}
			
		}

		else if(menuId.equals("finc_3")){// 账户管理
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof FincAccManagerMenuActivity)) {
				fincControl.cleanAllData();
				ActivityTaskManager.getInstance().removeAllSecondActivity();
				intent.setClass(this, FincAccManagerMenuActivity.class);
				context.startActivity(intent);
			}
			
		}
		else if(menuId.equals("finc_4")){// 基金定投管理
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof FundDQDEMenuActivity)) {
				fincControl.cleanAllData();
				ActivityTaskManager.getInstance().removeAllSecondActivity();
				intent.setClass(this, FundDQDEMenuActivity.class);
				context.startActivity(intent);
			}
			
		}
		else if(menuId.equals("finc_5")){// 交易查询
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof FundqueryMenuActivity)) {
				fincControl.cleanAllData();
				ActivityTaskManager.getInstance().removeAllSecondActivity();
				intent.setClass(this, FundqueryMenuActivity.class);
				context.startActivity(intent);
			}
			
		}
		else if(menuId.equals("finc_6")){// 推荐产品

			if (!((BaseDroidApp.getInstanse().getCurrentAct()) instanceof OrcmProductListActivity)) {
				fincControl.cleanAllData();
				ActivityTaskManager.getInstance().removeAllSecondActivity();
				intent.setClass(this, OrcmProductListActivity.class);
				context.startActivity(intent);
			}
			if (LayoutValue.LEWFTMENUINDEX .equals("finc_2") || LayoutValue.LEWFTMENUINDEX .equals("finc_1") ) {
				FincControl.isRecommend = false;
				fincControl.cleanAllData();
				ActivityTaskManager.getInstance().removeAllSecondActivity();
				intent.setClass(this, OrcmProductListActivity.class);
				context.startActivity(intent);
			}
			
		}
		return true;
		
		
		
//		super.setSelectedMenu(clickIndex);
//		Intent intent = new Intent();
//
//		switch (clickIndex) {
//		case 0:// 我的基金
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof MyFincMainActivity)) {
//				fincControl.cleanAllData();
//				ActivityTaskManager.getInstance().removeAllSecondActivity();
//				intent.setClass(this, MyFincMainActivity.class);
//				startActivity(intent);
//			}
//			break;
//		case 1:// 基金行情
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof FundPricesMenuActivity)) {
//				fincControl.cleanAllData();
//				ActivityTaskManager.getInstance().removeAllSecondActivity();
//				intent.setClass(this, FundPricesMenuActivity.class);
//				startActivity(intent);
//			}
//			break;
//		case 2:// 基金定投管理
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof FundDQDEMenuActivity)) {
//				fincControl.cleanAllData();
//				ActivityTaskManager.getInstance().removeAllSecondActivity();
//				intent.setClass(this, FundDQDEMenuActivity.class);
//				startActivity(intent);
//			}
//			break;
//		case 3:// 账户管理
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof FincAccManagerMenuActivity)) {
//				fincControl.cleanAllData();
//				ActivityTaskManager.getInstance().removeAllSecondActivity();
//				intent.setClass(this, FincAccManagerMenuActivity.class);
//				startActivity(intent);
//			}
//			break;
//		case 4:// 交易查询
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof FundqueryMenuActivity)) {
//				fincControl.cleanAllData();
//				ActivityTaskManager.getInstance().removeAllSecondActivity();
//				intent.setClass(this, FundqueryMenuActivity.class);
//				startActivity(intent);
//			}
//			break;
//
//		case 5:// 推荐产品
//
//			if (!((BaseDroidApp.getInstanse().getCurrentAct()) instanceof OrcmProductListActivity)) {
//				fincControl.cleanAllData();
//				ActivityTaskManager.getInstance().removeAllSecondActivity();
//				intent.setClass(this, OrcmProductListActivity.class);
//				startActivity(intent);
//				break;
//			}
//			if (LayoutValue.LEWFTMENUINDEX == 0 || LayoutValue.LEWFTMENUINDEX == 1 ) {
//				FincControl.isRecommend = false;
//				fincControl.cleanAllData();
//				ActivityTaskManager.getInstance().removeAllSecondActivity();
//				intent.setClass(this, OrcmProductListActivity.class);
//				startActivity(intent);
//				break;
//			}
//
//		default:
//
//			break;
//		}
	}

	/**
	 * 判断是否开通理财服务 判断是否有基金账户， 判断是否进行了风险认证 已经有结果后的弹窗
	 * 
	 * @return
	 */
	protected boolean docheck() {
		BaseDroidApp.getInstanse();
		if (!fincControl.ifInvestMent || !fincControl.ifhaveaccId
				|| !fincControl.ifdorisk) {// 是否开通理财服务
			getPopup();
			return false;
		} else {
			fincControl.iffirstTrade = false;
			return true;
		}
	}

	/**
	 * 判断是否开通理财服务 判断是否有基金账户， 判断是否进行了风险认证 不知道是不是已经做过判定 如果判定失败
	 * 请求doCheckrequestCommConversationId 判定 快速交易 已经封装到里面 ex: if(docheck1()){
	 * todo(); doCheckRequestPsnFundRiskEvaluationQueryResultCallback(){
	 * super(); if(docheck()){ switch(httpflag){ case : ..
	 * 
	 * } } }
	 * 
	 * @return
	 */
	public boolean docheck1() {
		// if (fincControl.ifInvestMent && fincControl.ifhaveaccId
		// && fincControl.ifdorisk) {
		// return true;
		// } else {
		// doCheckrequestCommConversationId();
		// BaseHttpEngine.showProgressDialog();
		// return false;
		// }
		return true;
	}

	/**
	 * 判断是否开通投资理财服务
	 */
	public void doCheckRequestPsnInvestmentManageIsOpen() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_API);
		biiRequestBody.setConversationId(null);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this,
				"doCheckRequestPsnInvestmentManageIsOpenCallback");
	}

	// public void doCheckrequestCommConversationIdCallBack(Object resultObj) {
	// super.requestCommConversationIdCallBack(resultObj);
	// doCheckRequestPsnInvestmentManageIsOpen();
	// }

	/**
	 * 检查时调用的 是否开通中银理财服务 回调
	 * 
	 * @param resultObj
	 */
	public void doCheckRequestPsnInvestmentManageIsOpenCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String isOpenOr = (String) biiResponseBody.getResult();
		if (StringUtil.parseStrToBoolean(isOpenOr)) {
			fincControl.ifInvestMent = true;
		} else {
			fincControl.ifInvestMent = false;
		}
		doCheckRequestQueryInvtBindingInfo();

	}

	/**
	 * 查询基金账户check
	 */
	public void doCheckRequestQueryInvtBindingInfo() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_QUERYINVTBINDINGINFO_API);
		biiRequestBody.setConversationId(null);
		Map<String, String> map = new Hashtable<String, String>();
		map.put(Finc.FINC_INVTTYPE_REQ, ConstantGloble.FINC_SERVICECODE);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"doCheckRequestQueryInvtBindingInfoCallback");
	}

	/**
	 * 这个东西怎么处理
	 * 
	 * @param resultObj
	 */
	public void doCheckRequestQueryInvtBindingInfoCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getResult() == null ) {
			fincControl.accId = null;
			fincControl.ifhaveaccId = false;
			BaseHttpEngine.dissMissProgressDialog();
			getPopup();
			return;
		} else {
			Map<String, String> map = (Map<String, String>) biiResponseBodys
					.get(0).getResult();
			if (!StringUtil.isNullOrEmpty(map.get(Finc.FINC_INVESTACCOUNT_RES))) {
				fincControl.accId = map.get(Finc.FINC_ACCOUNTID_RES);
				fincControl.bankId = map.get(Finc.FINC_BANKID_RES);
				fincControl.invAccId = map.get(Finc.FINC_INVESTACCOUNT_RES);
				fincControl.accNum = map.get(Finc.FINC_ACCOUNT_RES);
				fincControl.accDetailsMap = map;
				fincControl.ifhaveaccId = true;
			}
		}
		if (!fincControl.ifInvestMent) {
			BaseHttpEngine.dissMissProgressDialog();
			getPopup();
			return;
		}
//		else {
//			// 这个接口必须有账户的时候才可以
//			doCheckRequestPsnFundRiskEvaluationQueryResult();
//		}

	}

	/**
	 * 检查是否做了风险认证
	 */
	public void doCheckRequestPsnFundRiskEvaluationQueryResult() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_RISKEVALUATIONQUERYRESULT_API);
		biiRequestBody.setConversationId(null);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this,
				"doCheckRequestPsnFundRiskEvaluationQueryResultCallback");
	}

	/**
	 * 检查是否做了风险认证的回调处理
	 * 
	 * @param resultObj
	 */
	public void doCheckRequestPsnFundRiskEvaluationQueryResultCallback(
			Object resultObj) {
		// BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> resultMap = (Map<String, String>) biiResponseBody
				.getResult();
		if (resultMap == null) {

//			fincControl.ifdorisk = false;
//			BaseHttpEngine.dissMissProgressDialog();
//			getPopupForRisk();

			return;
		}
		String isEvaluated = resultMap.get(Finc.FINC_ISEVALUATED_RES);
		if (StringUtil.parseStrToBoolean(isEvaluated)) {// 做了风险评估
			fincControl.ifdorisk = true;
			fincControl.userRiskLevel = resultMap.get(Finc.FINC_RISKLEVEL_RES);
			BaseDroidApp.getInstanse().getBizDataMap()
			.put(ConstantGloble.BOCINVT_EVALUATION_RESULT, resultMap);
		} else {
			fincControl.ifdorisk = false;
			BaseHttpEngine.dissMissProgressDialog();
			getPopupForRisk();
			return;
		}

	}

	/**
	 * 初始化布局
	 * 
	 * @Author xyl
	 */
	protected void baseinit() {
		setContentView(R.layout.biz_activity_fund_layout);
		initPulldownBtn();
		initFootMenu();
//		2016.11.4 p606隐藏侧边栏 布局中的button颜色设置透明 点击事件取消
//		initLeftSideList(this, LocalData.fincLeftListData);// TODO 改图片
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		mainInflater = LayoutInflater.from(this);
		back = (Button) findViewById(R.id.ib_back);
		back.setOnClickListener(this);
		backImage = (ImageView) findViewById(R.id.im_back);
		backImage.setOnClickListener(this);
		right = (Button) findViewById(R.id.ib_top_right_btn);
		setRightToMainHome();
//		goneLeftView();
	}

	/**
	 * 快速交易按钮初始化
	 */
	protected void initRightBtnForTrade() {
		right.setText(getResources().getString(R.string.boci_fast_trans));
		right.setVisibility(View.VISIBLE);
		rightBtnOnClickListenerForTrade = new OnClickListener() {

			@Override
			public void onClick(View view) {
				int tag = (Integer) view.getTag();
				switch (tag) {
				case CustomDialog.TAG_CANCLE:
					BaseDroidApp.getInstanse().dismissMessageDialog();
					break;
				case CustomDialog.TAG_RELA_ACC_TRAN:// 买入
					if (docheck1()) {
						httpFlag = FASTDEALBUY;
						fincControl.cleanTrade();
						Intent intent = new Intent();
						intent.setClass(BaseDroidApp.getInstanse()
								.getCurrentAct(), FincTradeBuyActivity.class);
						startActivityForResult(intent, 1);
					}
					break;
				case CustomDialog.TAG_COMMON_RECEIVER_TRAN:// 卖出
					if (docheck1()) {
						httpFlag = FASTDEALSCHEDUBUY;
						fincControl.cleanTrade();
						Intent intent = new Intent();
						intent.setClass(BaseDroidApp.getInstanse()
								.getCurrentAct(),
								FincTradeScheduledBuyActivity.class);
						startActivityForResult(intent, 1);
					}
					break;

				default:
					break;
				}

			}
		};
		OnClickListener rightClick = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!StringUtil
						.isNullOrEmpty(FincControl.getInstance().tradeFundDetails)) {
					FincControl.getInstance().tradeFundDetails.clear();
				}
				BaseDroidApp.getInstanse().showSelectBuyOrSaleDialog(
						getString(R.string.prms_buy),
						getString(R.string.finc_inves),
						rightBtnOnClickListenerForTrade);
			}
		};

		right.setOnClickListener(rightClick);

	}

	/**
	 * 初始化设定账户布局
	 */
	protected void settingbaseinit() {
		setContentView(R.layout.biz_activity_fund_layout_withnofooter);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		tabcontent.setPadding(
				getResources().getDimensionPixelSize(R.dimen.fill_margin_left),
				getResources().getDimensionPixelSize(R.dimen.fill_margin_top),
				getResources().getDimensionPixelSize(R.dimen.fill_margin_top),
				getResources().getDimensionPixelSize(
						R.dimen.common_bottom_padding_new));
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		mainInflater = LayoutInflater.from(this);
		back = (Button) findViewById(R.id.ib_back);
		right = (Button) findViewById(R.id.ib_top_right_btn);
		backImage = (ImageView) findViewById(R.id.im_back);
		backImage.setOnClickListener(this);
		back.setOnClickListener(this);
		setLeftButtonPopupGone();

	}

	@Override
	protected void onResume() {
		super.onResume();
		// 把这个Activity放到Activity集中管理
		ActivityTaskManager.getInstance().addActivit(this);
		boolean login = BaseDroidApp.getInstanse().isLogin();
		onResumeFromLogin(login);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:
		case R.id.im_back:
			FincBaseActivity.this.onBackPressed();
			break;

		default:
			break;
		}

	}

	// // 我的基金 请求数据与回调--------------
	// /**
	// * @author 宁焰红 基金持仓--查询风险评估等级 不需要请求回话id xyl
	// */
	public void requestPsnFundRiskEvaluationQueryResult() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		LogGloble.d(TAG, "查询风险评估等级");
		biiRequestBody.setMethod(Finc.FINC_RISKEVALUATIONQUERYRESULT_API);
		biiRequestBody.setConversationId(null);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnFundRiskEvaluationQueryResultCallback");
	}

	/**
	 * 基金持仓--查询风险评估等级---回调
	 * 
	 * @author 宁焰红
	 * @param resultObj
	 */
	public void requestPsnFundRiskEvaluationQueryResultCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, String> resultMap = (Map<String, String>) biiResponseBody
				.getResult();
		if (resultMap == null) {
			return;
		}
		String isEvaluated = resultMap.get(Finc.FINC_ISEVALUATED_RES);
		if (StringUtil.parseStrToBoolean(isEvaluated)) {// 做了风险评估
			fincControl.ifdorisk = true;
			fincControl.userRiskLevel = resultMap.get(Finc.FINC_RISKLEVEL_RES);
			BaseDroidApp.getInstanse().getBizDataMap()
			.put(ConstantGloble.BOCINVT_EVALUATION_RESULT, resultMap);
		} else {
			fincControl.ifdorisk = false;
		}
	}

	/**
	 * @author 宁焰红 基金持仓--查询基金账户 根据投资交易类型，查询交易账户
	 */
	public void requestQueryInvtBindingInfo() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		LogGloble.d(TAG, "查询基金账户");
		biiRequestBody.setMethod(Finc.FINC_QUERYINVTBINDINGINFO_API);
		biiRequestBody.setConversationId(null);
		Map<String, String> map = new Hashtable<String, String>();
		map.put(Finc.FINC_INVTTYPE_REQ, ConstantGloble.FINC_SERVICECODE);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestQueryInvtBindingInfoCallback");
	}

	/**
	 * 基金持仓--查询基金账户---回调
	 * 
	 * @author 宁焰红
	 * @param resultObj
	 */
	public void requestQueryInvtBindingInfoCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getResult() == null) {
			fincControl.accId = null;
			fincControl.ifhaveaccId = false;
			fincControl.ifdorisk = false;
		} else {
			Map<String, String> map = (Map<String, String>) biiResponseBodys
					.get(0).getResult();
			if (!StringUtil.isNullOrEmpty(map.get(Finc.FINC_INVESTACCOUNT_RES))) {
				fincControl.accId = map.get(Finc.FINC_ACCOUNTID_RES);
				fincControl.invAccId = map.get(Finc.FINC_INVESTACCOUNT_RES);
				fincControl.accNum = map.get(Finc.FINC_ACCOUNT_RES);
				fincControl.accDetailsMap = map;
				fincControl.ifhaveaccId = true;
			}
		}
	}

	/**
	 * @author 宁焰红 基金持仓--查询基金持仓信息
	 */
	public void requestPsnFundQueryFundBalance() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_FUNDQUERYFUNDBALANCE_API);
		biiRequestBody.setConversationId(null);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnFundQueryFundBalanceCallback");
	}

	/**
	 * @author 宁焰红 基金持仓--查询基金持仓信息
	 */
	public void requestPsnFundQueryFundBalance(String fundCode) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_FUNDQUERYFUNDBALANCE_API);
		biiRequestBody.setConversationId(null);
		Map<String, String> map = new Hashtable<String, String>();
		map.put(Finc.I_FUNDCODE, fundCode);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnFundQueryFundBalanceCallback");
	}

	/**
	 * @author 宁焰红 基金持仓--查询基金持仓信息--回调
	 */
	public void requestPsnFundQueryFundBalanceCallback(Object resultObj) {

	}

	/***
	 * 根据账户标志，查询账户详细信息 单个账户详情 I02 银行接口 除了长城信用卡 都 用这个
	 * 
	 * @author 宁焰红
	 * @param accountId
	 *            :账户标志
	 */
	public void requestPsnAccountQueryAccountDetail(String accountId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_PSNACCOUNTQUERYACCOUNTDETAIL_API);
		biiRequestBody.setConversationId(null);
		Map<String, String> map = new Hashtable<String, String>();
		map.put(Finc.FINC_ACCOUNT_REQ, accountId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnAccountQueryAccountDetailCallback");
	}

	/**
	 * 根据账户标志，查询账户详细信息----回调
	 * 
	 * @author 宁焰红
	 * @param resultObj
	 */
	public void requestPsnAccountQueryAccountDetailCallback(Object resultObj) {

	}

	/**
	 * 基金卖出
	 * 
	 * @author 宁焰红
	 * @param fundCode
	 *            ：基金代码
	 * @param fundSellFlag
	 *            ：连续赎回
	 * @param feeType
	 *            ：收费方式 1:前收 2：后收
	 * @param sellAmount
	 *            ：赎回份额
	 * @param token
	 *            ：防重标识
	 */
	public void requestPsnFundSell(String fundCode, String fundSellFlag,
			String feeType, String sellAmount, String token,
			String executeType, String assignedDate) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_PSNFUNDSELL_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new Hashtable<String, String>();
		map.put(Finc.FINC_FUNDCODES_REQ, fundCode);
		map.put(Finc.FINC_FUNDSELLFLAG_REQ, fundSellFlag);
		map.put(Finc.FINC_FEETYPE_REQ, feeType);
		map.put(Finc.FINC_SELLAMOUNT_REQ, sellAmount);
		map.put(Finc.FINC_TOCKEN_REQ, token);
		map.put(Finc.FINC_FUNDBUY_EXECUTETYPE, executeType);
		if (assignedDate != null) {
			map.put(Finc.FINC_FUNDBUY_ASSIGNEDDATE, assignedDate);
		}
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnFundSellCallback");
	}

	/**
	 * 基金快速赎回
	 * @author fsm
	 * @param fundCode 基金代码
	 * @param fincSellAmount 赎回份额
	 * @param token 防重标识
	 */
	public void requestPsnFundFastSell(String fundCode, String fincSellAmount,String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_PSNFUNDQUICKSELL_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new Hashtable<String, String>();
		map.put(Finc.FINC_FUNDCODES_REQ, fundCode);
		map.put(Finc.FINC_FUNDSELL_AMOUNT, fincSellAmount);
		map.put(Finc.FINC_TOCKEN_REQ, token);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnFundFastSellCallback");
	}

	/**
	 * 快速赎回回调
	 * @param resultObj
	 */
	public void requestPsnFundFastSellCallback(Object resultObj){

	}

	/**
	 * 基金卖出----回调
	 * 
	 * @param resultObj
	 */
	public void requestPsnFundSellCallback(Object resultObj) {

	}

	/**
	 * 定期定额赎回
	 * 
	 * @param fundCode
	 * @param fundSellFlag
	 * @param dayInMonth
	 * @param eachAmount
	 * @param token
	 */
	public void fincScheduSell(String fundCode, String eachAmount,String fundSellFlag,
			String dealCode, String token, String dsFlag,  String subDate,
			String endFlag, String endContext) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_SCHEDULEDSELL);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new Hashtable<String, String>();
		map.put(Finc.FINC_FUNDCODES_REQ, fundCode);
		map.put(Finc.FINC_FUNDDDABORT_EACHAMOUNT, eachAmount);
		map.put(Finc.FINC_FUNDSELLFLAG_REQ, fundSellFlag);
		map.put(Finc.DEALCODE, dealCode);
		map.put(Finc.FINC_TOCKEN_REQ, token);
		map.put(Finc.DSFLAG, dsFlag);
		map.put(Finc.SUBDATE, subDate);
		map.put(Finc.ENDFLAG, endFlag);
		switch (Integer.parseInt(endFlag)) {
		case 1:
			map.put(Finc.FUNDPOINTENDDATE, endContext);
			break;
		case 2:
			map.put(Finc.ENDSUM, endContext);
			break;
		case 3:
			map.put(Finc.FUNDPOINTENDAMOUNT, endContext);
			break;
		}
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "fincScheduSellCallback");
	}

	/**
	 * 定期定额赎回 回调
	 * 
	 * @param resultObj
	 */
	public void fincScheduSellCallback(Object resultObj) {

	}

	/**
	 * 基金修改分红方式
	 * 
	 * @param fundCode
	 *            :基金编号
	 * @param fundBonusType
	 *            :分红方式 0: 默认 1: 现金2: 红利再投资
	 * @param token
	 */
	public void requestPsnFundBonusResult(String fundCode,
			String fundBonusType, String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		LogGloble.d(TAG, "基金修改分红方式");
		biiRequestBody.setMethod(Finc.FINC_PSNFUNDBONUSRESULT_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new Hashtable<String, String>();
		map.put(Finc.FINC_FUNDCODE_REEQ, fundCode);
		map.put(Finc.FINC_FUNDBONUSTYPE_REQ, fundBonusType);
		map.put(Finc.FINC_TOCKENB_REQ, token);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnFundBonusResultCallback");
	}

	/**
	 * 基金修改分红方式---回调
	 * 
	 * @param resultObj
	 */
	public void requestPsnFundBonusResultCallback(Object resultObj) {

	}

	/**
	 * 基金修改分红方式 挂单
	 * 
	 * @param fundCode
	 * @param fundBonusType
	 * @param token
	 */
	public void requestPsnFundNightBonusResult(String fundCode,
			String fundBonusType, String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_FUNDBONUSNIGHT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new Hashtable<String, String>();
		map.put(Finc.FINC_FUNDCODE_REEQ, fundCode);
		map.put(Finc.FINC_FUNDBONUSTYPE_REQ, fundBonusType);
		map.put(Finc.FINC_TOCKENB_REQ, token);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnFundNightBonusResultCallback");
	}

	/**
	 * 基金修改分红方式 挂单 回调
	 * 
	 * @param resultObj
	 */
	public void requestPsnFundNightBonusResultCallback(Object resultObj) {

	}

	/**
	 * 查询用户的所有账户
	 */
	public void requestPsnCommonQueryAllChinaBankAccount() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		LogGloble.d(TAG, "查询用户的所有账户");
		biiRequestBody.setMethod(Finc.FINC_QUERYALLCHINABANKACCOUNT_API);
		biiRequestBody.setConversationId(null);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnCommonQueryAllChinaBankAccountCallback");
	}

	/***
	 * 查询用户的所有账户---回调
	 */
	public void requestPsnCommonQueryAllChinaBankAccountCallback(
			Object resultObj) {

	}

	/**
	 * 查询所有资金账户
	 */
	public void queryAccList() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_QUERYACCLIST);
		biiRequestBody.setConversationId(null);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "queryAccListCallback");
	}

	/**
	 * 查询所有资金账户回调处理
	 */
	public void queryAccListCallback(Object resultObj) {

	}

	/**
	 * @param accountIdList
	 *            :账户标志 根据账户标志，查询账户详情 获取多个账户的详细信息
	 */
	public void requestPsnAccountQueryAccountDetail(List<String> accountIdList) {
		List<BiiRequestBody> requestBodys = new ArrayList<BiiRequestBody>();
		int len = accountIdList.size();
		for (int i = 0; i < len; i++) {
			BiiRequestBody biiRequestBody = new BiiRequestBody();
			biiRequestBody.setConversationId(null);
			biiRequestBody
			.setMethod(Finc.FINC_PSNACCOUNTQUERYACCOUNTDETAIL_API);
			Map<String, String> map = new HashMap<String, String>();
			String accountId = accountIdList.get(i);
			map.put(Finc.FINC_ACCOUNT_REQ, accountId);
			biiRequestBody.setParams(map);
			requestBodys.add(biiRequestBody);
		}
		HttpManager.requestBii(requestBodys, this,
				"requestAccountDetailListCallback");

	}

	/**
	 * 根据账户标志，查询账户详情 获取多个账户的详细信息----回调
	 * 
	 * @param resultObj
	 */
	public void requestAccountDetailListCallback(Object resultObj) {

	}

	/**
	 * 登记基金账户
	 * 
	 * @param accountId
	 *            :账户标志
	 * @param tockenId
	 */
	public void requestPsnFundRegistFundAccount(String accountId,
			String tockenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_PSNFUNDREGISTFUNDACCOUNT_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new Hashtable<String, String>();
		map.put(Finc.FINC_ACCOUNTIDMM_RES, accountId);
		map.put(Finc.FINC_TOCKENM_REQ, tockenId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnFundRegistFundAccountCallback");
	}

	/** 登记基金账户-----回调 */
	public void requestPsnFundRegistFundAccountCallback(Object resultObj) {

	}

	/**
	 * 根据基金代码查询所有关注的基金基本信息 ----回调
	 * 
	 * @param resultObj
	 */
	public void requstFundInfoCallback(Object resultObj) {

	}

	/**
	 * 根据基金代码查询的基金基本信息 I 10 基金 有判断是否可以买入,定投 等信息 详情
	 * 
	 * @param fundCode
	 */
	public void getFundDetailByFundCode(String fundCode) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setMethod(Finc.FINC_PSNGETFUNDDETAIL_API);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Finc.FINC_FUNDCODEM_REQ, fundCode);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"getFundDetailByFundCodeCallback");
	}


	/**
	 * 根据基金代码查询的短期理财产品到期时间明细
	 * 
	 * @param fundCode
	 */
	public void getFundFundDueDate(String fundCode) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_PSNFUNDDUEDATE);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Finc.FINC_FUNDCODEM_REQ, fundCode);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"getFundDueDateQueryCallback");
	}
	/**
	 * 根据基金代码查询的短期理财产品到期时间明细回调
	 * 
	 * @param fundCode
	 */

	public void getFundDueDateQueryCallback(Object resultObj){

	}

	/**
	 * 根据基金代码查询的基金基本信息 查询基金币种、基金名称等信息 回调 I 10
	 * 
	 * @param resultObj
	 */
	public void getFundDetailByFundCodeCallback(Object resultObj) {

	}

	/** 查询基金币种、基金名称等信息---回调 */
	public void requstPsnGetFundDetailCallback(Object resultObj) {

	}

	/**
	 * 取消关注的基金
	 * 
	 * @param key
	 * @param value
	 *            ：基金代码
	 * @param token
	 */
	public void requestPsnResetUserProfile(String key, String value,
			String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_PSNRESETUSERPROFILE_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Finc.FINC_KEY_RES, key);
		map.put(Finc.FINC_VALUE_RES, value);
		map.put(Finc.FINC_TOKEN_RES, token);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnResetUserProfileCallback");
	}

	/** 取消关注的基金----回调 */
	public void requestPsnResetUserProfileCallback(Object resultObj) {

	}

	/**
	 * 基金买入 需要 conversationId 和tokenId
	 * 
	 * @param buyAmount
	 *            买入分数
	 * @param fundCode
	 *            基金代码
	 * @param feetype
	 *            收费方式 1:前收2:后收
	 * @param affirmFlag
	 *            是否确认风险不匹配 Y:是 N:否
	 * @param token
	 *            防重标识
	 * @param executeType
	 *            0：立即执行 1：指定日期执行
	 * @param assignedDate
	 *            指定日期
	 */
	public void fundBuy(BigDecimal buyAmount, String fundCode, String feetype,
			String affirmFlag, String token, String executeType,
			String assignedDate) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FIC_FUNDBUY);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.FINC_FUNDBUY_BUYAMOUNT, buyAmount.toString());
		map.put(Finc.FINC_FUNDBUY_FEETYPE, feetype);
		map.put(Finc.FINC_FUNDBUY_FUNDCODE, fundCode);
		map.put(Finc.FINC_FUNDBUY_AFFIRMFLAG, affirmFlag);
		map.put(Finc.FINC_FUNDBUY_EXECUTETYPE, executeType);
		if (assignedDate != null) {
			map.put(Finc.FINC_FUNDBUY_ASSIGNEDDATE, assignedDate);
		}
		map.put(Finc.TOKEN, token);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "fundBuyCallback");
	}

	/**
	 * 这个没有 交易方式 executeType 基金买入挂单
	 * 
	 * @param buyAmount
	 * @param fundCode
	 * @param feetype
	 * @param affirmFlag
	 * @param token
	 */
	public void fundNightBuy(BigDecimal buyAmount, String fundCode,
			String feetype, String affirmFlag, String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_FUNDNIGHTBUY);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.FINC_FUNDBUY_BUYAMOUNT, buyAmount.toString());
		map.put(Finc.FINC_FUNDBUY_FEETYPE, feetype);
		map.put(Finc.FINC_FUNDBUY_FUNDCODE, fundCode);
		map.put(Finc.FINC_FUNDBUY_AFFIRMFLAG, affirmFlag);
		map.put(Finc.TOKEN, token);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "fundNightBuyCallback");

	}

	/**
	 * 基金买入的回调处理
	 * 
	 * @param resultObj
	 */
	public void fundBuyCallback(Object resultObj) {

	}

	public void fundNightBuyCallback(Object resultObj) {

	}

	/**
	 * 基金赎回挂单
	 * 
	 * @param sellAmount
	 * @param fundCode
	 * @param feetype
	 * @param fundSellFlag
	 * @param token
	 */
	public void fundNightSell(String sellAmount, String fundCode,
			String feetype, String fundSellFlag, String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_FUNDNIGHTSELL);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.FINC_SELLAMOUNT_REQ, sellAmount);
		map.put(Finc.FINC_FUNDBUY_FEETYPE, feetype);
		map.put(Finc.FINC_FUNDBUY_FUNDCODE, fundCode);
		map.put(Finc.FINC_FUNDSELLFLAG_REQ, fundSellFlag);
		map.put(Finc.TOKEN, token);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "fundNightSellCallback");

	}

	/**
	 * 基金赎回 回调
	 * 
	 * @param resultObj
	 */
	public void fundNightSellCallback(Object resultObj) {

	}

	/**
	 * 当日委托交易查询
	 * 
	 * @param currentIndex
	 *            当前的index
	 * @param pageSize
	 *            每页显示多个
	 */
	public void querytoday(int currentIndex, int pageSize, boolean refresh) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_FUNDTODAYQUERY);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.FINC_CURRENTINDEX, String.valueOf(currentIndex));
		map.put(Finc.FINC_PAGESIZE, String.valueOf(pageSize));
		map.put(Finc.FINC_REFRESH, String.valueOf(refresh));
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "querytodayCallback");

	}

	/**
	 * 当日委托交易查询回调
	 * 
	 * @param resultObj
	 */
	public void querytodayCallback(Object resultObj) {

	}

	/**
	 * 基金撤单 PsnFundConsignAbort 需要 conversationId和tokenId
	 * 
	 * @param fundSeq
	 *            基金交易序号
	 * @param fundAmount
	 *            基金份额/金额
	 * @param originalTransCode
	 *            基金原交易码
	 * @param fundCode
	 *            基金代码
	 * @param date
	 *            交易日期
	 * @param nightFlag
	 *            夜间交易标志
	 * @param token
	 *            防重机制token
	 */
	public void fundConsernDeal(String fundSeq, String fundAmount,
			String originalTransCode, String fundCode, String date,
			String nightFlag, String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_FUNDCONSIGNABORT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.FINC_FUNDCONSIGNABORT_FUNDSEQ, fundSeq);
		map.put(Finc.FINC_FUNDCONSIGNABORT_FUNDAMOUNT, fundAmount);
		map.put(Finc.FINC_FUNDCONSIGNABORT_ORIGINALTRANSCODE, originalTransCode);
		map.put(Finc.FINC_FUNDCONSIGNABORT_FUNDCODE, fundCode);
		map.put(Finc.FINC_FUNDCONSIGNABORT_DATE, date);
		map.put(Finc.FINC_FUNDCONSIGNABORT_NIGHTFLAG, nightFlag);
		map.put(Finc.TOKEN, token);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "fundConsernDealCallback");

	}

	/**
	 * 基金撤单 回调处理
	 * 
	 * @param resultObj
	 */
	public void fundConsernDealCallback(Object resultObj) {

	}

	/**
	 * 基金指定日期撤单
	 * 
	 * @param fundSeq
	 * @param originalTransCode
	 * @param fundCode
	 * @param assignedDate
	 * @param token
	 */
	public void fundAppointCancel(String fundSeq, String originalTransCode,
			String fundCode, String assignedDate, String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.APPOINTCANCEL);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.FINC_FUNDCONSIGNABORT_FUNDSEQ, fundSeq);
		map.put(Finc.APPOINTCANCEL_ORIGINALTRANSCODE, originalTransCode);
		map.put(Finc.FINC_FUNDCONSIGNABORT_FUNDCODE, fundCode);
		map.put(Finc.APPOINTCANCEL_ASSIGNEDDATE, assignedDate);
		map.put(Finc.TOKEN, token);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"fundAppointCancelCallback");

	}

	/**
	 * 基金定期定额撤单
	 * 
	 * @param resultObj
	 */
	public void fundAppointCancelCallback(Object resultObj) {

	}

	/**
	 * 指定日期查询
	 * 
	 * @param startDate
	 * @param endDate
	 * @param currentIndex
	 * @param pageSize
	 * @param flush
	 */
	public void fundQueryExtraDay(String startDate, String endDate,
			int currentIndex, int pageSize, boolean flush) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_QUERYEXTRADAY);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.FINC_CURRENTINDEX, String.valueOf(currentIndex));
		map.put(Finc.FINC_PAGESIZE, String.valueOf(pageSize));
		map.put(Finc.FINC_REFRESH, String.valueOf(flush));
		map.put(Finc.FINC_STARTDATE, startDate);
		map.put(Finc.FINC_ENDDATE, endDate);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"fundQueryExtraDayCallback");
	}

	/**
	 * 指定日期查询
	 * 
	 * @param resultObj
	 */
	public void fundQueryExtraDayCallback(Object resultObj) {

	}

	/**
	 * 在途交易查询
	 * 
	 * @param currentIndex
	 *            当前页
	 * @param pageSize
	 *            每页显示条数
	 * @param flush
	 *            刷新标志 是否刷新
	 */
	public void fundQueryEnTrust(int currentIndex, int pageSize, boolean flush) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.QUERYTRANSONTRAN);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.FINC_CURRENTINDEX, String.valueOf(currentIndex));
		map.put(Finc.FINC_PAGESIZE, String.valueOf(pageSize));
		map.put(Finc.FINC_REFRESH, String.valueOf(flush));
		biiRequestBody.setParams(map);
		HttpManager
		.requestBii(biiRequestBody, this, "fundQueryEnTrustCallback");
	}

	/**
	 * 在途交易查询
	 * 
	 * @param resultObj
	 */
	public void fundQueryEnTrustCallback(Object resultObj) {

	}

	/**
	 * 历史交易查询
	 * 
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @param currentIndex
	 *            当前页
	 * @param size
	 *            每页显示的条数
	 * @param flush
	 *            是否重新查询
	 */
	public void fundQueryHistory(String startDate, String endDate,
			String currentIndex, String pageSize, boolean flush) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_QUERYHISTORY);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.FINC_CURRENTINDEX, currentIndex);
		map.put(Finc.FINC_PAGESIZE, pageSize);
		map.put(Finc.FINC_REFRESH, String.valueOf(flush));
		map.put(Finc.FINC_STARTDATE, startDate);
		map.put(Finc.FINC_ENDDATE, endDate);
		biiRequestBody.setParams(map);
		HttpManager
		.requestBii(biiRequestBody, this, "fundQueryHistoryCallback");

	}

	/**
	 * 
	 * @param resultObj
	 */
	public void fundQueryHistoryCallback(Object resultObj) {

	}

	/**
	 * 历史交易查询 401
	 * 
	 * @param startDate
	 * @param endDate
	 * @param fundCode
	 * @param transType
	 * @param currentIndex
	 * @param pageSize
	 * @param flush
	 */
	public void fundQueryHistory401(String startDate, String endDate,
			String transType, int currentIndex, int pageSize, boolean flush) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.QUERYHISTORY401);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.FINC_CURRENTINDEX, String.valueOf(currentIndex));
		map.put(Finc.FINC_PAGESIZE, String.valueOf(pageSize));
		map.put(Finc.FINC_REFRESH, String.valueOf(flush));
		map.put(Finc.FINC_STARTDATE, startDate);
		map.put(Finc.FINC_ENDDATE, endDate);
		map.put(Finc.FINC_FUNDQUERYDQDT_TRANSTYPE, transType);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"fundQueryHistory401Callback");
	}

	/**
	 * 历史交易查询 401
	 * 
	 * @param resultObj
	 */
	public void fundQueryHistory401Callback(Object resultObj) {

	}

	/**
	 * 定期定额交易明细查询
	 * 
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @param currentIndex
	 *            当前页
	 * @param pageSize
	 *            页码
	 * @param flush
	 *            是否刷新
	 * @param fundCode
	 *            要查询的基金代码
	 */
	public void fundQueryDTransDetails(String startDate, String endDate,
			int currentIndex, int pageSize, boolean flush, String fundCode) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_DTQUERY);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.FINC_CURRENTINDEX, String.valueOf(currentIndex));
		map.put(Finc.FINC_PAGESIZE, String.valueOf(pageSize));
		map.put(Finc.FINC_REFRESH, String.valueOf(flush));
		map.put(Finc.FINC_STARTDATE, startDate);
		map.put(Finc.FINC_ENDDATE, endDate);
		map.put(Finc.FINC_FUNDCODE, fundCode);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"fundQueryDTransDetailsCallback");

	}

	/**
	 * 定期定额交易明细查询 返回结果
	 * 
	 * @param resultObj
	 */
	public void fundQueryDTransDetailsCallback(Object resultObj) {

	}

	/**
	 * 定期定额 查询
	 * 
	 * @param currentIndex
	 *            当前页
	 * @param pageSize
	 *            每页显示的条数
	 */
	public void fundQueryDQDE(String currentIndex, String pageSize,
			boolean refrush, String fundCode, String dtFlag) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_FUNDQUERYDQDT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.FINC_CURRENTINDEX, currentIndex);
		map.put(Finc.FINC_PAGESIZE, pageSize);
		map.put(Finc.FINC_REFRESH, String.valueOf(refrush));
		//		map.put(Finc.FINC_FUNDTODAYQUERY_FUNDCODE, fundCode);
		//		map.put(Finc.DTFLAG, dtFlag);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "fundQueryDQDECallback");
	}

	/**
	 * 定期定额查询回调处理
	 * 
	 * @param resultObj
	 */
	public void fundQueryDQDECallback(Object resultObj) {
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		List<Map<String, Object>> list = (List<Map<String, Object>>) map.get(Finc.FINC_FUNDTODAYQUERY_LIST);
		//		if(StringUtil.isNullOrEmpty(map) || StringUtil.isNullOrEmpty(list)){
		//			BaseHttpEngine.dissMissProgressDialog();
		//			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_query_noresult_error));
		//			return;
		//		}
		fincControl.fundUAvailableResult = map;
		fincControl.fundAvailableList = list;
	}

	/**
	 * 基金推荐查询
	 */
	public void fundQueryCombNominate() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_FUNDNOMINATE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(null);
		biiRequestBody.setParams(null);
		// HttpManager.requestBii(biiRequestBody, this,
		// "fundQueryCombNominateCallback");
		HttpManager.requestBii_withUrl(BiiApi.FINCADDRESS, biiRequestBody,
				this, "fundQueryCombNominateCallback");
	}

	/**
	 * 基金推荐回调处理
	 * 
	 * @param resultObj
	 */
	public void fundQueryCombNominateCallback(Object resultObj) {

	}

	/**
	 * 基金账户开户
	 * 
	 * @param capitalAccountId
	 * @param addressType
	 * @param token
	 * @param Otp
	 * @param smcStr
	 */
	public void regisAcc(String capitalAccountId, String addressType,
			String token, String Otp, String smcStr) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_REGISTACC);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.FINC_REGISTACC_CAPITALACCOUNTID, capitalAccountId);
		map.put(Finc.FINC_REGISTACC_ADDRESSTYPE, addressType);
		map.put(Finc.TOKEN, token);
		map.put(Finc.Otp, Otp);
		map.put(Finc.Otp, smcStr);
		SipBoxUtils.setSipBoxParams(map);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "regisAccCallback");
	}

	/**
	 * 基金账户开户回调
	 * 
	 * @param resultObj
	 */
	public void regisAccCallback(Object resultObj) {

	}

	/**
	 * 基金账户开户确认
	 * 
	 * @param capitalAccountId
	 * @param userName
	 * @param addressType
	 * @param identifyNumber
	 * @param combinId
	 * @param identifyType
	 */
	protected void regisAccConfirm(String capitalAccountId, String userName,
//			String addressType,
			String identifyType, String identifyNumber,
			String combinId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_REGISTACC_CONFIRM);
		biiRequestBody.setConversationId(fincControl.registAccConversationId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.FINC_REGISTACC_CONFIRM_CAPITALACCOUNTID, capitalAccountId);
		map.put(Finc.FINC_REGISTACC_CONFIRM_USERNAME, userName);
//		map.put(Finc.FINC_REGISTACC_ADDRESSTYPE, addressType);
		map.put(Finc.FINC_REGISTACC_CONFIRM_IDENTIFYTYPE, identifyType);
		map.put(Finc.FINC_REGISTACC_CONFIRM_IDENTIFYNUMBER, identifyNumber);
		map.put(Finc.FINC_REGISTACC_CONFIRM_COMBINID, combinId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "regisAccConfirmCallback");
	}

	/**
	 * 基金账户开户确认 回调
	 * 
	 * @param resultObj
	 */
	public void regisAccConfirmCallback(Object resultObj) {

	}

	/**
	 * 登记基金账户 确认信息
	 * 
	 * @param accountId
	 */
	public void checkINAccConfirm(String accountId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_CHECKINACCCONFIRM);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.FINC_ACCOUNTID, accountId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"checkINAccConfirmCallback");
	}

	/**
	 * 登记基金账户 确认信息 回调
	 * 
	 * @param resultObj
	 */
	public void checkINAccConfirmCallback(Object resultObj) {

	}

	/**
	 * 登记基金账户 提交
	 * 
	 * @param accountId
	 * @param fincAccount
	 * @param token
	 */
	public void checkINAccSuccess(String accountId, String fincAccount,
			String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_CHECKINACCSUCCESS);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.FINC_ACCOUNTID, accountId);
		map.put(Finc.FINC_FINCACCOUNT, fincAccount);
		map.put(Finc.TOKEN, token);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"checkINAccSuccessCallback");
	}

	/**
	 * 登记基金账户 提交 回调
	 * 
	 * @param resultObj
	 */
	public void checkINAccSuccessCallback(Object resultObj) {

	}

	/**
	 * 基金定投，定期定额申购
	 * @param scheduledBuyAmount 每次申购金额
	 * @param fundCode 基金代码
	 * @param feetype 
	 * @param token
	 * @param transCycle 定投周期
	 * @param subDate 扣款日期
	 * @param endFlag 结束条件
	 * @param endContext 结束条件值
	 * @param dealCode 指令交易后台交易ID
	 */
	public void fundScheduledBuy(String scheduledBuyAmount, String fundCode,
			String feetype, String token, String transCycle, String subDate,
			String endFlag, String endContext, String dealCode) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_SCHEDULEDBUY);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.FINC_FUNDBUY_FUNDCODE, fundCode);
		map.put(Finc.FINC_SCHEDULEDBUY_SCHEDULEDBUYAMOUNT, scheduledBuyAmount);
		map.put(Finc.FINC_FUNDBUY_AFFIRMFLAG, "Y");
		map.put(Finc.DEALCODE, dealCode);
		map.put(Finc.TRANSCYCLE, transCycle);
		map.put(Finc.SUBDATE, subDate);
		map.put(Finc.ENDFLAG, endFlag);
		switch (Integer.parseInt(endFlag)) {
		case 1:
			map.put(Finc.FUNDPOINTENDDATE, endContext);
			break;
		case 2:
			map.put(Finc.ENDSUM, endContext);
			break;
		case 3:
			map.put(Finc.FUNDPOINTENDAMOUNT, endContext);
			break;
		}
		map.put(Finc.TOKEN, token);
		biiRequestBody.setParams(map);
		HttpManager
		.requestBii(biiRequestBody, this, "fundScheduledBuyCallback");

	}

	/**
	 * 基金定投 回调处理
	 * 
	 * @param resultObj
	 */
	public void fundScheduledBuyCallback(Object resultObj) {

	}

	/**
	 * 基金转入 夜间交易
	 * 
	 * @param fromFundCode
	 * @param toFundCode
	 * @param fundSellFlag
	 * @param amount
	 * @param feetype
	 *            收费方式
	 * @param token
	 */
	public void fundThrowResultNight(String fromFundCode, String toFundCode,
			String fundSellFlag, String amount, String feetype, String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_FUNDCONVERSIONRESULT_NIGHT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.FINC_FUNDCONVERSIONRESULT_FROMFUNDCODE, fromFundCode);
		map.put(Finc.FINC_FUNDCONVERSIONRESULT_TOFUNDCODE, toFundCode);
		map.put(Finc.FINC_FUNDCONVERSIONRESULT_FUNDSELLFLAG, fundSellFlag);
		map.put(Finc.FINC_FUNDCONVERSIONRESULT_AMOUNT, amount);
		map.put(Finc.COMBINQUERY_FEETYPE, feetype);
		map.put(Finc.FINC_FUNDCONVERSIONRESULT_AFFIRMFLAG, "Y");
		map.put(Finc.TOKEN, token);

		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"fundThrowResultNightCallback");

	}

	/**
	 * 基金转入 夜间交易 结果
	 */
	public void fundThrowResultNightCallback(Object resultObj) {

	}

	/**
	 * 基金转入 输入
	 * 
	 * @param fromFundCode
	 */
	public void fundThrowInput(String fromFundCode) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_FUNDCONVERSIONINPUT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.FINC_FUNDCONVERSIONINPUT_FROMFUNDCODE, fromFundCode);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "fundThrowInputCallback");

	}

	/**
	 * 基金转入 输入
	 * 
	 * @param resultObj
	 */
	public void fundThrowInputCallback(Object resultObj) {

	}

	/**
	 * 基金转入 结果
	 * 
	 * @param fromFundCode
	 * @param toFundCode
	 * @param fundSellFlag
	 * @param amount
	 * @param token
	 */
	public void fundThrowResult(String fromFundCode, String toFundCode,
			String fundSellFlag, String amount, String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_FUNDCONVERSIONRESULT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.FINC_FUNDCONVERSIONRESULT_FROMFUNDCODE, fromFundCode);
		map.put(Finc.FINC_FUNDCONVERSIONRESULT_TOFUNDCODE, toFundCode);
		map.put(Finc.FINC_FUNDCONVERSIONRESULT_FUNDSELLFLAG, fundSellFlag);
		map.put(Finc.FINC_FUNDCONVERSIONRESULT_AMOUNT, amount);
		map.put(Finc.FINC_FUNDCONVERSIONRESULT_AFFIRMFLAG, "Y");
		map.put(Finc.TOKEN, token);

		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "fundThrowResultCallback");

	}

	/**
	 * 基金转入 结果
	 * 
	 * @param resultObj
	 */
	public void fundThrowResultCallback(Object resultObj) {

	}

	/**
	 * 查询基金账户余额
	 * 
	 * @param accountId
	 */
	public void queryQccBanlance(String accountId) {
		LogGloble.d(TAG, "queryQccBanlance==PsnFincQueryQccBalance+start");
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_QUERYBANCE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.FINC_QUERYBANCE_ACCOUNTID, accountId);
		biiRequestBody.setParams(map);
		HttpManager
		.requestBii(biiRequestBody, this, "queryQccBanlanceCallback");
	}

	/**
	 * 查询基金账户余额 回调
	 * 
	 * @param resultObj
	 */
	public void queryQccBanlanceCallback(Object resultObj) {
	}

	/**
	 * 定投撤单
	 * @param fundCode 基金代码 
	 * @param oldApplyDate 原定期定额申请日期
	 * @param transSeq transSeq
	 * @param token
	 */
	public void fundDdAbort(String fundCode, String oldApplyDate, String transSeq,
			String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_FUNDDDABORT);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.FINC_FUNDCODE, fundCode);
		map.put(Finc.OLDAPPLYDATE, oldApplyDate);
		map.put(Finc.QUERYHISTORY401_TRANSSEQ, transSeq);
		map.put(Finc.TOKEN, token);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "fundDdAbortCallback");
	}

	/**
	 * 定投撤单 回调
	 * 
	 * @param resultObj
	 */
	public void fundDdAbortCallback(Object resultObj) {
	}

	/**
	 * 定赎撤单
	 * @param fundCode 基金代码
	 * @param oldApplyDate 原定期定额申请日期
	 * @param token 
	 * @param transSeq 原定期定额序号
	 */
	public void fundScheduSellAbort(String fundCode, String oldApplyDate,
			String token, String transSeq) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_SECHEDULEDSELL);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.FINC_FUNDCODE, fundCode);
		map.put(Finc.OLDAPPLYDATE, oldApplyDate);
		map.put(Finc.QUERYHISTORY401_TRANSSEQ, transSeq);
		map.put(Finc.TOKEN, token);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"fundScheduSellAbortCallback");
	}

	/**
	 * 定赎 撤单 回调
	 * 
	 * @param resultObj
	 */
	public void fundScheduSellAbortCallback(Object resultObj) {
	}

	/**
	 * 定投修改
	 * @param fundCode  基金代码
	 * @param transSeq  原定投序号
	 * @param oldFundPaymentDate 原定投申请日期
	 * @param applyAmount 申请金额
	 * @param subDate 定投日期
	 * @param endFlag 结束条件
	 * @param endContext 结束条件描述
	 * @param tokenId 
	 * @param transCycle 交易周期
	 */
	public void fundScheduBuyModify(String fundCode, String transSeq, String oldFundPaymentDate,
			String applyAmount, String subDate, String endFlag, String endContext, 
			String tokenId, String transCycle) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_SECHEDULEDBUYMODIFY);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.FINC_FUNDCODE, fundCode);
		map.put(Finc.QUERYHISTORY401_TRANSSEQ, transSeq);
		map.put(Finc.FINC_SECHEDULEDBUYMODIFY_OLDFUNDPAYMENTDATE, oldFundPaymentDate);
		map.put(Finc.QUERYHISTORY401_APPLYAMOUNT, applyAmount);
		map.put(Finc.SUBDATE, subDate);
		map.put(Finc.ENDFLAG, endFlag);
		switch (Integer.parseInt(endFlag)) {
		case 1:
			map.put(Finc.FUNDPOINTENDDATE, endContext);
			break;
		case 2:
			map.put(Finc.ENDSUM, endContext);
			break;
		case 3:
			map.put(Finc.FUNDPOINTENDAMOUNT, endContext);
			break;
		}
		map.put(Finc.TRANSCYCLE, transCycle);
		map.put(Finc.TOKEN, tokenId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"fundScheduBuyModifyCallback");

	}

	/**
	 * 定投 修改 回调
	 * 
	 * @param resultObj
	 */
	public void fundScheduBuyModifyCallback(Object resultObj) {
	}

	/**
	 * 定赎修改
	 * @param fundCode
	 * @param oldFundPaymentDate 原定期定额申请日期
	 * @param fundSellAmount 赎回份额
	 * @param subDate 赎回日期
	 * @param endFlag
	 * @param endContext
	 * @param token
	 * @param fundSeq原定期定额序号
	 * @param dsFlag定赎周期
	 */
	public void fundScheduSellModify(String fundCode, String oldFundPaymentDate, 
			String fundSellAmount, String subDate, String endFlag, String endContext, 
			String token, String fundSeq, String dsFlag) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_SECHEDULEDSELLMODIFY);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.FINC_FUNDCODE, fundCode);
		map.put(Finc.FINC_SECHEDULEDSELLMODIFY_OLDFUNDPAYMENTDATE, oldFundPaymentDate);
		map.put(Finc.FUNDSELLAMOUNT, fundSellAmount);
		map.put(Finc.FINC_FUNDDDABORT_DAYINMONTH, subDate);
		map.put(Finc.ENDFLAG, endFlag);
		switch (Integer.parseInt(endFlag)) {
		case 1:
			map.put(Finc.FUNDPOINTENDDATE, endContext);
			break;
		case 2:
			map.put(Finc.ENDSUM, endContext);
			break;
		case 3:
			map.put(Finc.FUNDPOINTENDAMOUNT, endContext);
			break;
		}
		map.put(Finc.TAACCCANCEL_FUNDSEQ, fundSeq);
		map.put(Finc.DSFLAG, dsFlag);
		map.put(Finc.TOKEN, token);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"fundScheduSellModifyCallback");

	}

	/**
	 * 定赎回修改 回调
	 * 
	 * @param resultObj
	 */
	public void fundScheduSellModifyCallback(Object resultObj) {
	}

	/***
	 * 登记基金他账户 基金公司查询
	 */
	public void getFundRegCompanyList() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_GETFUNDREGCOMPANYLIST);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this,
				"getFundRegCompanyListCallback");

	}

	/**
	 * 登记基金他账户 基金公司查询 回调
	 * 
	 * @param resultObj
	 */
	public void getFundRegCompanyListCallback(Object resultObj) {
	}

	/**
	 * 浮动盈亏 测算 20 基金35
	 */
	public void getFDYKList(String fundCode, String startDate, String endDate) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_FLOATPROFITANDLOSS);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		if (!fundCode.equals(getString(R.string.finc_allFund))) {// 如果不是全部基金...
			map.put(Finc.FINC_FUNDCODE, fundCode);
		}
		map.put(Finc.FINC_STARTDATE, startDate);
		map.put(Finc.FINC_ENDDATE, endDate);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "getFDYKListCallback");

	}

	/**
	 * 浮动盈亏 测算 20 基金35 回调
	 * 
	 * @param resultObj
	 */
	public void getFDYKListCallback(Object resultObj) {
	}

	/** 查询关注的基金 */
	public void attentionFundQuery() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_ATTENTIONQUERYLIST);
		biiRequestBody.setConversationId(null);
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this,
				"attentionFundQueryCallback");
	}

	/**
	 * 查询关注的基金 返回处理
	 * 
	 * @param resultObj
	 */
	public void attentionFundQueryCallback(Object resultObj) {

	}

	public void attentionFundAdd(String fundCode, String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_ATTENTIONQFUNDADD);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Finc.FINC_FUNDCODE, fundCode);
		map.put(Finc.TOKEN, token);
		biiRequestBody.setParams(map);
		HttpManager
		.requestBii(biiRequestBody, this, "attentionFundAddCallback");
	}

	public void attentionFundAddCallback(Object resultObj) {

	}

	public void attentionFundConsern(String fundCode, String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_ATTENTIONQFUNDCONCEL);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Finc.FINC_FUNDCODE, fundCode);
		map.put(Finc.TOKEN, token);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"attentionFundConsernCallback");
	}

	public void attentionFundConsernCallback(Object resultObj) {

	}

	/**
	 * 401 换卡交易(重新登记资金账户)
	 */
	public void fundChangeCard(String newAccountId, String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.CHANGECARD);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Finc.CHANGECARD_NEWACCOUNTID, newAccountId);
		map.put(Finc.TOKEN, token);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "fundChangeCardCallback");
	}

	/**
	 * 401 换卡交易(重新登记资金账户)
	 * 
	 * @param resultObj
	 */
	public void fundChangeCardCallback(Object resultObj) {
	}

	/**
	 * 基金交易账户销户
	 */
	public void fundAccDischarge(String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.DISCHARGEFINCACC);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Finc.TOKEN, token);
		biiRequestBody.setParams(map);
		HttpManager
		.requestBii(biiRequestBody, this, "fundAccDischargeCallback");
	}

	/**
	 * 基金交易账户销户
	 * 
	 * @param resultObj
	 */
	public void fundAccDischargeCallback(Object resultObj) {

	}

	/**
	 * 查询基金TA账户列表
	 */
	public void queryFundTaAccList() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.QUERYTAACCDETAILLIST);
		HttpManager.requestBii(biiRequestBody, this,
				"queryFundTaAccListCallback");
	}

	/**
	 * 查询基金TA账户列表
	 * 
	 * @param resultObj
	 */
	public void queryFundTaAccListCallback(Object resultObj) {

	}

	/**
	 * kChart
	 * 
	 * @param fundCode
	 * @param endDate
	 */
	public void getKChartDate(String fundCode, String endDate) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC_KCHART);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Finc.FINC_KCHART_FUNDCODE, fundCode);
		map.put(Finc.FINC_KCHART_MONTHS, "1");
		map.put(Finc.FINC_KCHART_ENDDATE, endDate);
		biiRequestBody.setParams(map);
		HttpManager.requestBii_withUrl(BiiApi.FINCADDRESS, biiRequestBody,
				this, "getKChartDateCallback");

	}

	public void getKChartDateCallback(Object resultObj) {
	}

	/**
	 * 基金Ta账户取消关联/销户
	 * 
	 * @param taAccountNo
	 *            他账户号码
	 * @param transType
	 *            交易类型0：取消TA账户关联1：销TA账户
	 * @param fundRegCode
	 *            注册基金公司代码
	 * @param tokenId
	 *            防重机制
	 */
	public void consernFundTaAccRelation(String taAccountNo, String transType,
			String fundRegCode, String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.TAACCCANCEL);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		map.put(Finc.TAACCCANCEL_TAACCOUNTNO, taAccountNo);
		map.put(Finc.TAACCCANCEL_TRANSTYPE, transType);
		map.put(Finc.TAACCCANCEL_FUNDREGCODE, fundRegCode);
		map.put(Finc.TOKEN, tokenId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody,
				this, "consernFundTaAccRelationCallback");
	}

	public void consernFundTaAccRelationCallback(Object resultObj) {
	}

	/**
	 * 基金公司信息查询  P403确认页面添加的提示信息
	 * @param fundCode   基金代码
	 */
	public void requestFundCompanyInfoQuery(String fundCode) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.FINC403_METHOD_PSNFUNDCOMPANYINFOQUERY);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Finc.I_FUNDCODE, fundCode);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody,
				this, "fundCompanyInfoQueryCallback");
	}

	public void fundCompanyInfoQueryCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		fincControl.fundCompanyInfo = (Map<String, Object>) biiResponseBody.getResult();
	}

	/**
	 * 
	 * @param text1
	 * @param text2
	 * @param text3
	 * @return
	 */
	public View getListHeaderView(int text1, int text2, int text3) {
		View headerView = mainInflater.inflate(
				R.layout.finc_query_history_list_header, null);
		TextView tv1 = (TextView) headerView.findViewById(R.id.tv1);
		TextView tv2 = (TextView) headerView.findViewById(R.id.tv2);
		TextView tv3 = (TextView) headerView.findViewById(R.id.tv3);
		tv1.setText(getString(text1));
		tv2.setText(getString(text2));
		tv3.setText(getString(text3));
		return headerView;
	}

	/** 主界面 */
	protected void initRightBtnForMain() {
		// right.setText(getString(R.string.forex_right));
		// right.setVisibility(View.VISIBLE);
		// right.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// activityTaskManager.removeAllActivity();
		// }
		// });
	}

	/** listView 的header view 适用于主页面 */
	protected void initListHeaderView(int text1ResID, int textRes2Id,
			int textRes3Id) {
		View v = findViewById(R.id.finc_listheader_layout);
		TextView headerView1 = (TextView) v.findViewById(R.id.list_header_tv1);
		TextView headerView2 = (TextView) v.findViewById(R.id.list_header_tv2);
		TextView headerView3 = (TextView) v.findViewById(R.id.list_header_tv3);
		headerView1.setText(text1ResID);
		headerView2.setText(textRes2Id);
		headerView3.setText(textRes3Id);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				headerView1);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				headerView2);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				headerView3);
	}

	/** listView 的header view 适用于主页面 */
	protected void initListHeaderView(int text1ResID, int textRes2Id) {
		View v = findViewById(R.id.finc_listheader_layout);
		TextView headerView1 = (TextView) v.findViewById(R.id.list_header_tv1);
		TextView headerView2 = (TextView) v.findViewById(R.id.list_header_tv2);
		TextView headerView3 = (TextView) v.findViewById(R.id.list_header_tv3);
		headerView1.setText(text1ResID);
		headerView2.setText(textRes2Id);
		headerView3.setVisibility(View.GONE);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				headerView1);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				headerView2);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				headerView3);
	}

	/** listView 的 header View 适用于弹窗页面 */
	protected void initListHeaderView(View parentView, int text1ResID,
			int textRes2Id, int textRes3Id) {
		TextView headerView1 = (TextView) parentView
				.findViewById(R.id.list_header_tv1);
		TextView headerView2 = (TextView) parentView
				.findViewById(R.id.list_header_tv2);
		TextView headerView3 = (TextView) parentView
				.findViewById(R.id.list_header_tv3);
		headerView1.setText(text1ResID);
		headerView2.setText(textRes2Id);
		headerView3.setText(textRes3Id);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				headerView1);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				headerView2);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				headerView3);
	}

	/** listView 的 header View 适用于弹窗页面 */
	protected void initListHeaderView(View parentView, int text1ResID,
			int textRes2Id) {
		TextView headerView1 = (TextView) parentView
				.findViewById(R.id.list_header_tv1);
		TextView headerView2 = (TextView) parentView
				.findViewById(R.id.list_header_tv2);
		TextView headerView3 = (TextView) parentView
				.findViewById(R.id.list_header_tv3);
		headerView1.setText(text1ResID);
		headerView2.setText(textRes2Id);
		headerView3.setVisibility(View.GONE);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				headerView1);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				headerView2);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				headerView3);
	}

	public void setFundCompanyInfo(){
		String alertInfo = "该笔交易可能产生费用，详见<name>相关公告，咨询及投诉电话：phone";
		TextView fundCompanyInfo = (TextView)findViewById(R.id.fundCompanyInfo);
		if(fincControl.fundCompanyInfo != null){
			if (fundCompanyInfo != null) {
				fundCompanyInfo.setTextColor(getResources().getColor(R.color.red));
				String companyName = (String) fincControl.fundCompanyInfo
						.get(Finc.COMPANYNAME);
				String companyPhone = (String) fincControl.fundCompanyInfo
						.get(Finc.COMPANYPHONE);
				alertInfo = alertInfo.replace("name", companyName).replace("phone", companyPhone);
				fundCompanyInfo.setText(alertInfo);
			}
		}
	}

	/************************************P405改造新增**************************************/

	/**
	 * 基金对账单持仓查询
	 * @param fundStatementTime   查询年月
	 */
	public void requestFundStatementBalanceQuery(String fundStatementTime) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.METHOD_PSNFUNDSTATEMENTBALANCEQUERY);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Finc.FUNDSTATEMENTTIME, fundStatementTime);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody,
				this, "fundStatementBalanceQueryCallback");
	}

	public void fundStatementBalanceQueryCallback(Object resultObj) {
		fincControl.fundStatementBalance = HttpTools.getResponseResult(resultObj);
	}

	/**
	 * 基金对账单交易明细查询   交易流水    
	 * 根据所选年月获取本月第一天和最后一天进行查询
	 * @param fundStatementTime   查询年月
	 */
	public void requestPersionalTransDetailQuery(String fundStatementTime, boolean isCurrentMonth, 
			String currentMonth) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.METHOD_PSNPERSIONALTRANSDETAILQUERY);
		Map<String, String> map = new HashMap<String, String>();
		String startDate = fundStatementTime + "/01";
		String endDate = QueryDateUtils.getMonthLastDay(startDate);
		map.put(Finc.FINC_STARTDATE, startDate);
		if(isCurrentMonth){
			map.put(Finc.FINC_ENDDATE, currentMonth);
		}else{
			map.put(Finc.FINC_ENDDATE, endDate);
		}
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody,
				this, "persionalTransDetailQueryCallback");
	}

	public void persionalTransDetailQueryCallback(Object resultObj) {
		List<Map<String, Object>> list = HttpTools.getResponseResult(resultObj);
		if(StringUtil.isNullOrEmpty(list)){
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.collect_common_error));
			fincControl.transSactionList = list;
			return;
		}
		fincControl.transSactionList = list;
	}

	/**
	 * 失效定期定额查询
	 * @param currentIndex
	 * @param _refresh
	 */
	public void requestScheduledFundUnavailableQuery(int currentIndex, boolean _refresh) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setMethod(Finc.METHOD_PSNSCHEDULEDFUNDUNAVAILABLEQUERY);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Finc.COMBINQUERY_CURRENTINDEX, currentIndex + "");
		map.put(Finc.FINC_PAGESIZE, ConstantGloble.LOAN_PAGESIZE_VALUE);
		map.put(Finc.FINC_REFRESH, _refresh + "");
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody,
				this, "scheduledFundUnavailableQueryCallback");
	}

	public void scheduledFundUnavailableQueryCallback(Object resultObj) {
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		List<Map<String, Object>> list = (List<Map<String, Object>>) map.get(Finc.FINC_FUNDTODAYQUERY_LIST);
		if(StringUtil.isNullOrEmpty(map) || StringUtil.isNullOrEmpty(list)){
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_query_noresult_error));
			return;
		}
		fincControl.fundUnavailableResult = map;
		fincControl.fundUnavailableList = list;
	}

	/**
	 * 定投申请明细查询
	 * @param fundScheduleDate 
	 * @param scheduleBuyNum
	 */
	public void requestFundScheduledBuyDetailQuery(String fundScheduleDate, String scheduleBuyNum) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.METHOD_PSNFUNDSCHEDULEDBUYDETAILQUERY);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Finc.FUNDSCHEDULEDATE, fundScheduleDate);
		map.put(Finc.SCHEDULEBUYNUM, scheduleBuyNum);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody,
				this, "fundScheduledBuyDetailQueryCallback");
	}

	public void fundScheduledBuyDetailQueryCallback(Object resultObj) {
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		if(StringUtil.isNullOrEmpty(map)){
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_query_noresult_error));
			return;
		}
		fincControl.scheduledBuySellDetail = map;
	}

	/**
	 * 定赎申请明细查询
	 * @param fundScheduleDate 定赎申请日期
	 * @param scheduleBuyNum 定赎序号
	 */
	public void requestFundScheduledSellDetailQuery(String fundScheduleDate, String scheduleSellNum) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.METHOD_PSNFUNDSCHEDULEDSELLDETAILQUERY);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Finc.FUNDSCHEDULEDATE, fundScheduleDate);
		map.put(Finc.SCHEDULESELLNUM, scheduleSellNum);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody,
				this, "fundScheduledSellDetailQueryCallback");
	}

	public void fundScheduledSellDetailQueryCallback(Object resultObj) {
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		if(StringUtil.isNullOrEmpty(map)){
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_query_noresult_error));
			return;
		}
		fincControl.scheduledBuySellDetail = map;
	}

	/**
	 * 定投暂停/开通
	 * @param fundCode  
	 * @param scheduleBuyDate 定投申请日期
	 * @param scheduleBuyNum 定投序号
	 * @param fundTransFlag 暂停/开通标识
	 * @param token
	 */
	public void requestPsnFundScheduledBuyPauseResume(String fundCode, String scheduleBuyDate, String scheduleBuyNum,
			String fundTransFlag, String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.METHOD_PSNFUNDSCHEDULEDBUYPAUSERESUME);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.FINC_FUNDCODE, fundCode);
		map.put(Finc.SCHEDULEBUYDATE, scheduleBuyDate);
		map.put(Finc.SCHEDULEBUYNUM, scheduleBuyNum);
		map.put(Finc.FUNDTRANSFLAG, fundTransFlag);
		map.put(Finc.TOKEN, token);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "fundScheduledBuyPauseResumeCallBack");
	}

	public void fundScheduledBuyPauseResumeCallBack(Object resultObj){
	}

	/**
	 * 定赎暂停/开通
	 * @param fundCode  
	 * @param scheduleBuyDate 定赎申请日期
	 * @param scheduleBuyNum 定赎序号
	 * @param fundTransFlag 暂停/开通标识
	 * @param token
	 */
	public void requestPsnFundScheduledSellPauseResume(String fundCode, String scheduleSellDate, String scheduleSellNum,
			String fundTransFlag, String token) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.METHOD_PSNFUNDSCHEDULEDSELLPAUSERESUME);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.FINC_FUNDCODE, fundCode);
		map.put(Finc.SCHEDULESELLDATE, scheduleSellDate);
		map.put(Finc.SCHEDULESELLNUM, scheduleSellNum);
		map.put(Finc.FUNDTRANSFLAG, fundTransFlag);
		map.put(Finc.TOKEN, token);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "fundScheduledSellPauseResumeCallBack");
	}

	public void fundScheduledSellPauseResumeCallBack(Object resultObj){
	}

	/**
	 * 指令交易产品查询
	 * @param protpye 交易分类
	 * @param tradeType 交易类型
	 * @param pageSize 页面大小
	 * @param currentIndex 当前页索引
	 * @param _refresh
	 */
	public void requestPsnOcrmProductQuery(String protpye, String tradeType, String pageSize,
			String currentIndex, boolean _refresh) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.METHOD_PSNOCRMPRODUCTQUERY);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.PROTPYE, "01");
		map.put(Finc.I_TRADETYPE, tradeType);
		map.put(Finc.FINC_PAGESIZE, pageSize);
		map.put(Finc.COMBINQUERY_CURRENTINDEX, currentIndex);
		map.put(Finc.FINC_REFRESH, String.valueOf(_refresh));
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "ocrmProductQueryCallBack");
	}

	public void ocrmProductQueryCallBack(Object resultObj){
		fincControl.OcrmProductMap = HttpTools.getResponseResult(resultObj);
		if(!StringUtil.isNullOrEmpty(fincControl.OcrmProductMap))
			fincControl.OcrmProductList = (List<Map<String, Object>>) fincControl.OcrmProductMap
			.get(Finc.RESULTLIST);
	}

	public void requestPsnFundIssueScopeQuery(String fundCode) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.METHOD_PSNFUNDISSUESCOPEQUERY);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.I_FUNDCODE, fundCode);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "fundIssueScopeQueryCallBack");
	}

	@SuppressWarnings("unchecked")
	public void fundIssueScopeQueryCallBack(Object resultObj){
		fincControl.fundIssueScopeList = HttpTools.getResponseResult(resultObj);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	/**
	 * 设置日期
	 */
	public OnClickListener fincChooseDateClick = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			TextView tv = (TextView) v;
			String time = tv.getText().toString();
			int startYear = Integer.parseInt(time.substring(0, 4));
			int startMonth = Integer.parseInt(time.substring(5, 7));
			int startDay = Integer.parseInt(time.substring(8, 10));
			// 第二个参数为用户选择设置按钮后的响应事件
			// 最后的三个参数为缺省显示的年度，月份，及日期信息
			DatePickerDialog dialog = new DatePickerDialog(
					FincBaseActivity.this, new OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							StringBuilder date = new StringBuilder();
							date.append(String.valueOf(year));
							date.append("/");
							int month = monthOfYear + 1;
							date.append(((month < 10) ? ("0" + month)
									: (month + "")));
							date.append("/");
							date.append(((dayOfMonth < 10) ? ("0" + dayOfMonth)
									: (dayOfMonth + "")));
							// 为日期赋值
							((TextView) v).setText(String.valueOf(date));
						}
					}, startYear, startMonth - 1, startDay);
			dialog.show();
		}
	};


	/**
	 * 查询是否签约电子约定书
	 */
	public void doCheckRequestPsnFundIsSignedFundStipulation(String fundCode) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.METHOD_PSNFUNDISSIGNEDFUNDSTIPULATION);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.FINC_GETFUNDCOMPANCYLIST_FUNDCOMPANYCODE, fundCode);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"doCheckRequestPsnFundIsSignedFundStipulationCallback");
	}

	public void doCheckRequestPsnFundIsSignedFundStipulationCallback(Object resultObj){

	}


	/**
	 * 查询是否签约电子合同
	 */
	public void doCheckRequestPsnFundIsSignElectronicContract(String fundCode) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.METHOD_PSNFUNDISSIGNELECTRONICCONTRACT);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Finc.FINC_FINCCODE, fundCode);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"doCheckRequestPsnFundIsSignElectronicContractCallback");
	}

	public void doCheckRequestPsnFundIsSignElectronicContractCallback(Object resultObj){

	}

	/**基金可指定日期查询*/
	public void requestPsnFundCanDealDateQuery(String fundCode,String appointFlag) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Finc.METHOD_PSNFUNDCANDEALDATEQUERY);
		Map<String, String> map = new HashMap<String, String>();
		map.put(Finc.FINC_FUNDCODE, fundCode);
		map.put(Finc.APPOINTFLAG, appointFlag);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody,
				this, "requestPsnFundCanDealDateQueryCallback");
	}

	public void requestPsnFundCanDealDateQueryCallback(Object resultObj){

	}

	public void requestPsnFundSignElectronicContractCallback(Object resultObj) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * params week 
	 * 根据week，获取对于的时间id
	 * */
	protected String getValueByWeek(String week) {
		String value = "01";
		if("周一".equals(week)){
			value = "01";
		}else if("周二".equals(week)){
			value = "02";
		}else if("周三".equals(week)){
			value = "03";
		}else if("周四".equals(week)){
			value = "04";
		}else if("周五".equals(week)){
			value = "05";
		}
		return value ;
	}
	
	/**
	 * params week 
	 * 根据week，获取对于的时间id
	 * */
	protected String getWeekByValue(String value) {
		String week = "-";
		if("01".equals(value)){
			week = "周一";
		}else if("02".equals(value)){
			week = "周二";
		}else if("03".equals(value)){
			week = "周三";
		}else if("04".equals(value)){
			week = "周四";
		}else if("05".equals(value)){
			week = "周五";
		}
		return week ;
	}
	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.TwoTask;
	}

}
