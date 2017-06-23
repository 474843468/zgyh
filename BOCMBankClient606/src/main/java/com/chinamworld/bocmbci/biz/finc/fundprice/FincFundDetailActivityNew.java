
package com.chinamworld.bocmbci.biz.finc.fundprice;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.biz.finc.trade.FincTradeBuyActivity;
import com.chinamworld.bocmbci.biz.finc.trade.FincTradeScheduledBuyActivity;
import com.chinamworld.bocmbci.biz.invest.constant.InvestConstant;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

import java.util.List;
import java.util.Map;

/**
 * 显示查询到的基金详情页面
 * 
 * @author xyl
 * 
 */
public class FincFundDetailActivityNew extends FincBaseActivity implements
		OnCheckedChangeListener {
	private static final String TAG = "FincFundDetailActivity";

	/** 无操作 */
	private final int OPERATION_NONE = 0;
	/** 关注 */
	private final int OPERATION_ATTENTION = 1;
	/** 买入 */
	private final int OPERATION_BUY = 2;
	/** 定投 */
	private final int OPERATION_INVERS = 3;
	/** 从基金关注进入　 */
	public static final int ATTENTION = 2;

	/**
	 * 主布局
	 */
	private View mainView;
	/**
	 * 登录按钮
	 */
	private Button login;
	/**
	 * 基金名称和代码
	 */
	private TextView fundNameAndCodeTextView;
	/**
	 * 基金单位净值
	 */
	private TextView fundNetValueTextView;
	/**
	 * 关注
	 */
	private LinearLayout fundAttentionLayout;
	private ImageView fundAttentionImage;
	private TextView fundAttentionTextView;
	/**
	 * 净值走势图
	 */
	private LinearLayout fundValueChartLayout;
	private TextView fundValueChartTextView;
	/**
	 * 基金状态
	 */
	private TextView fundStatus;
	/**
	 * 基金公司
	 */
	private TextView fundCompanyTextView;
	/**
	 * 定投按钮
	 */
	private Button inversButton;
	/**
	 * 买入按钮
	 */
	private Button buyButton;
	private RadioGroup radioGroup;
	/**
	 * 产品属性
	 */
	private RadioButton productButton;
	/**
	 * 购买属性
	 */
	private RadioButton byButton;
	/**
	 * 赎回属性
	 */
	private RadioButton redeemButton;
	/**
	 * 子布局：显示产品属性、购买属性和赎回属性
	 */
	private LinearLayout body_layout;

	// ---------------------产品属性---------------------
	/**
	 * 产品属性布局
	 */
	private View productView;
	/**
	 * 日净值增长率
	 */
	private TextView fincn_daynetvaluerate_label;
	private TextView tv_fincn_daynetvaluerate;
	/**
	 * 累计净值
	 */
	private TextView finc_totlevalue_label;
	private TextView tv_finc_totlevalue;
	/**
	 * 净值截止日期
	 */
	private TextView tv_finc_myfinc_netPriceDate;
	/**
	 * 交易币种
	 */
	private TextView tv_finc_fundcurrency_type;
	/**
	 * 产品种类
	 */
	private TextView tv_finc_productkind;
	/**
	 * 产品类型
	 */
	private TextView tv_finc_producttype;
	/**
	 * 基金状态
	 */
	private LinearLayout finc_productstatus_ll;
	private TextView tv_finc_productstatus;
	/**
	 * 默认分红方式
	 */
	private TextView tv_finc_share_way;
	/**
	 * 手续费率
	 */
	private TextView tv_finc_procedure_rate;
	/**
	 * 优惠信息
	 */
	private TextView tv_finc_privilege_info;

	// ---------------------购买属性---------------------
	/**
	 * 购买属性布局
	 */
	private View buyView;
	/**
	 * 首次认购下限
	 */
	private TextView tv_finc_fundfirst_buy_floor;
	/**
	 * 追加认购下限
	 */
	private TextView tv_finc_fundadd_buy_floor;
	/**
	 * 申购下限
	 */
	private TextView tv_finc_ransom_floor;
	/**
	 * 定期定额申购下限
	 */
	private TextView tv_finc_schedubuyLimit_colon;
	/**
	 * 单日购买上限
	 */
	private TextView tv_finc_daylimit;
	/**
	 * 收费方式
	 */
	private TextView tv_finc_feetype;

	// ---------------------赎回属性---------------------
	/**
	 * 赎回属性布局
	 */
	private View redeemView;
	/**
	 * 赎回下限
	 */
	private TextView tv_finc_sellLowLimit;
	/**
	 * 最低持有份额
	 */
	private TextView tv_finc_myfinc_holdQutyLowLimit;
	/**
	 * 单日赎回上限
	 */
	private TextView tv_finc_myfinc_day_toplimit;
	/**
	 * 最近可赎回日期
	 */
	private LinearLayout finc_lately_can_ransom_ll;
	private TextView tv_finc_lately_can_ransom;

	/** 币种 */
	private String currencyStr;
	/** 基金代码 */
	private String fundCodeStr;
	private boolean isLogin;
	/** 是否可买入 */
	private boolean canBuy;
	/** 是否可定投 */
	private boolean canScheduleBuy;
	/** 操作标识--默认无操作 */
	private int operation_flag = OPERATION_NONE;
	
	/**是否登录后立马进入买入/定投页面*/
	private boolean isLoginCallback = false ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int flag = getIntent().getIntExtra(Finc.I_ATTENTIONFLAG, -1);
		if(flag == ATTENTION){
			fincControl.fundDetails = fincControl.fincFundDetails;  
		}
		
		initMainView();
		initProductPropView();
		initBuyPropView();
		initRedeemPropView();

		// 默认显示产品属性
		setBodyLayoutChildView(productView);

		initData();

		initListener();
	}

	/**
	 * 初始化事件监听器
	 */
	private void initListener() {
//		login.setOnClickListener(loginListener);
		radioGroup.setOnCheckedChangeListener(this);
		fundAttentionLayout.setOnClickListener(mAttentionListener);
		fundValueChartLayout.setOnClickListener(mValueChartListener);
		inversButton.setOnClickListener(mInversListener);
		buyButton.setOnClickListener(mBuyListener);
	}

	private OnClickListener loginListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			startLogin();
		}
	};

	/** 买入 */
	private OnClickListener mBuyListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			operation_flag = OPERATION_BUY;
			if (isLogin) {
				/**基金买入，判断是否未风险评估*/
				BaseHttpEngine.showProgressDialog();
				doCheckRequestPsnFundRiskEvaluationQueryResult();
			} else {
				startLogin();
			}
		}
	};

	protected void startBuyActivity() {
		operation_flag = OPERATION_NONE;
		if (canBuy) {
			fincControl.tradeFundDetails = fincControl.fundDetails;
			Intent intent = new Intent();
			intent.setClass(this, FincTradeBuyActivity.class);
			intent.putExtra(Finc.I_ATTENTIONFLAG, ATTENTION);
			startActivityForResult(intent, 1);
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.finc_canbuy_error));
			return;
		}
	}

	/** 定投 */
	private OnClickListener mInversListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			operation_flag = OPERATION_INVERS;
			if (isLogin) {
				/**基金买入，判断是否未风险评估*/
				BaseHttpEngine.showProgressDialog();
				doCheckRequestPsnFundRiskEvaluationQueryResult();
			} else {
				startLogin();
			}
		}
	};

	protected void startInversActivity() {
		operation_flag = OPERATION_NONE;
		if (canScheduleBuy) {
			fincControl.tradeFundDetails = fincControl.fundDetails;
			Intent intent = new Intent();
			intent.setClass(this, FincTradeScheduledBuyActivity.class);
			startActivityForResult(intent, 1);
		} else {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.finc_cansecheduedbuy_error));
			return;
		}
	}

	/** 基金走势图 */
	private OnClickListener mValueChartListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			BiiHttpEngine.showProgressDialog();
			if (isLogin) {
				requestSystemDateTime();	
			} else {
				getKChartDate(fundCodeStr, "2015/05/27");
			}
			 
//			 KchartUtils.showKLine(xData, yData);
//			 // 传入数据
//			 startActivity(new Intent(this,
//			 FincFundPricesChartActivity.class));
		}
	};
	
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		String currentTime = QueryDateUtils.getcurrentDate(dateTime);
		getKChartDate(fundCodeStr, currentTime);
	}
	
	public void getKChartDateCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.getKChartDateCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, String>> resultList = (List<Map<String, String>>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.finc_no_history_netprice));
			return;
		}
		String[] xData = new String[resultList.size()];
		float[] yData = new float[resultList.size()];
		int i = 0;
		for (Map<String, String> map : resultList) {
			xData[i] = map.get(Finc.FINC_KCHART_PUBDATE);
			yData[i] = Float.valueOf((String) map
					.get(Finc.FINC_KCHART_NETVALUE));
			i++;
		}
//		KchartUtils.showKLine(xData, yData);
	}

	/** 关注 */
	private OnClickListener mAttentionListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (isLogin) {
				modifyAttentionStatus();
			} else {
				operation_flag = OPERATION_ATTENTION;
				startLogin();
			}
		}
	};

	/** 修改基金关注状态 */
	protected void modifyAttentionStatus() {
		if (fincControl.getAttentionFlag()) {// 已经关注
			BaseDroidApp.getInstanse().showErrorDialog(
					getResources().getString(
							R.string.finc_attention_conern_confirm),
					R.string.cancle, R.string.confirm, new OnClickListener() {
						@Override
						public void onClick(View v) {
							switch (Integer.parseInt(v.getTag() + "")) {
							case CustomDialog.TAG_SURE:
								// 确认取消关注
								BaseDroidApp.getInstanse().dismissErrorDialog();
								requestCommConversationId();
								BaseHttpEngine.showProgressDialog();
								break;
							case CustomDialog.TAG_CANCLE:
								operation_flag = OPERATION_NONE;
								BaseDroidApp.getInstanse().dismissErrorDialog();
								break;
							}
						}
					});
		} else {// 未关注
			if (fincControl.getAttentionCount() >= 10) {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getString(R.string.finc_setattention_num_error));
			} else {
				BaseHttpEngine.showProgressDialog();
				requestCommConversationId();

			}

		}
	}

	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		requestPSNGetTokenId();
	}

	@Override
	public void requestPSNGetTokenIdCallback(Object resultObj) {
		super.requestPSNGetTokenIdCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String tokenId = (String) biiResponseBody.getResult();
		if (StringUtil.isNullOrEmpty(tokenId)) {
			return;
		}
		if (fincControl.getAttentionFlag()) {// 已经关注，取消关注
			attentionFundConsern(fundCodeStr, tokenId);
		} else {// 未关注，关注
			attentionFundAdd(fundCodeStr, tokenId);

		}
	}

	@Override
	public void attentionFundAddCallback(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		super.attentionFundAddCallback(resultObj);
		FincControl.getInstance().addAttentionCount1();
		CustomDialog.toastShow(this,
				getResources()
						.getString(R.string.finc_setattentionfundsuccesse));
		fincControl.setAttentionFlag(true);
		// fincControl.addAttentionCount1();
		operation_flag = OPERATION_NONE;
		setAttentionIconAndText(fincControl.getAttentionFlag());

	}

	@Override
	public void attentionFundConsernCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		super.attentionFundConsernCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		// Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
		// .getResult();
		// String fundCode = (String) resultMap.get(Finc.FINC_FUNDCODE);
		fincControl.setAttentionFlag(false);
		fincControl.minusAttentionCount1();
		operation_flag = OPERATION_NONE;
		setAttentionIconAndText(fincControl.getAttentionFlag());
		CustomDialog.toastShow(this,
				getResources()
						.getString(R.string.finc_myfinc_follow_cancelfinc));
	}

	protected void startLogin() {
//		Intent intent = new Intent();
//		intent.setClass(FincFundDetailActivityNew.this, LoginActivity.class);
//		startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_LOGIN_CODE);
		BaseActivity.getLoginUtils(FincFundDetailActivityNew.this).exe(new LoginTask.LoginCallback() {

			@Override
			public void loginStatua(boolean isLogin) {
				if(isLogin){
					switch (operation_flag) {
						case OPERATION_NONE:
							ActivityTaskManager.getInstance().removeAllSecondActivity();
							Intent intent = new Intent(FincFundDetailActivityNew.this,FundPricesActivityNew.class);
							startActivity(intent);
							break;
						default:
							isLoginCallback = true ;
							BaseHttpEngine.showProgressDialog();
							doCheckRequestPsnInvestmentManageIsOpen();
							break;
					}
				}
			}
		});
	}

	/**
	 * 初始化页面数据
	 */
	private void initData() {
		initMainViewData();
		initProductPropViewData();
		initBuyPropViewData();
		initRedeemPropViewData();
	}

	/**
	 * 初始化赎回属性数据
	 */
	private void initRedeemPropViewData() {
		// 赎回下限
		String fincSellLowLimitColon = (String) fincControl.fundDetails
				.get(Finc.FINC_SELLLOWLIMIT);
		if (StringUtil.isNull(fincSellLowLimitColon)) {
			fincSellLowLimitColon = "-";
		} else {
			fincSellLowLimitColon = StringUtil.parseStringCodePattern(
					currencyStr, fincSellLowLimitColon, 2);
		}
		tv_finc_sellLowLimit.setText(fincSellLowLimitColon);

		// 最低持有份额
		String fincMyFincHoldQutyLowLimit = (String) fincControl.fundDetails
				.get(Finc.FINC_HOLDLOWCOUNT);
		if (StringUtil.isNull(fincMyFincHoldQutyLowLimit)) {
			fincMyFincHoldQutyLowLimit = "-";
		} else {
			fincMyFincHoldQutyLowLimit = StringUtil.parseStringCodePattern(
					currencyStr, fincMyFincHoldQutyLowLimit, 2);
		}
		tv_finc_myfinc_holdQutyLowLimit.setText(fincMyFincHoldQutyLowLimit);

		// 单日赎回上限
		String forexMyFincDayTopLimit = (String) fincControl.fundDetails
				.get(Finc.FINC_DAY_TOPLIMIT);
		// if(StringUtil.isNull(forexMyFincDayTopLimit) ||
		// "0".equals(forexMyFincDayTopLimit)){
		// forexMyFincDayTopLimitLayout.setVisibility(View.GONE);
		// }else{
		forexMyFincDayTopLimit = StringUtil.parseStringCodePattern(currencyStr,
				forexMyFincDayTopLimit, 2);
		tv_finc_myfinc_day_toplimit.setText(forexMyFincDayTopLimit);
		// };

		// 最近可赎回日期
		String fincLatelyCanRansomDate = (String) fincControl.fundDetails
				.get(Finc.FINC_DATE_Lately);
		if ("Y".equals(fincControl.fundDetails.get(Finc.FINC_IS_SHORT_FUND))) {
			finc_lately_can_ransom_ll.setVisibility(View.VISIBLE);
			if (StringUtil.isNull(fincLatelyCanRansomDate)) {
				fincLatelyCanRansomDate = "-";
			}
			tv_finc_lately_can_ransom.setText(fincLatelyCanRansomDate);
		} else {
			finc_lately_can_ransom_ll.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 初始化购买属性数据
	 */
	private void initBuyPropViewData() {
		// 首次认购下限
		String fincFundFirstBuyFloor = (String) fincControl.fundDetails
				.get(Finc.FINC_ORDERLOWLIMIT);
		if (StringUtil.isNull(fincFundFirstBuyFloor)) {
			tv_finc_fundfirst_buy_floor.setText("-");
		} else {
			fincFundFirstBuyFloor = StringUtil.parseStringCodePattern(
					currencyStr, fincFundFirstBuyFloor, 2);
			tv_finc_fundfirst_buy_floor.setText(fincFundFirstBuyFloor);
		}

		// 追加认购下限
		String fincFundAddBuyFloor = (String) fincControl.fundDetails
				.get(Finc.FINC_BUY_ADD_LOW_LMT);
		if (StringUtil.isNull(fincFundAddBuyFloor)) {
			tv_finc_fundadd_buy_floor.setText("-");
		} else {
			fincFundAddBuyFloor = StringUtil.parseStringCodePattern(
					currencyStr, fincFundAddBuyFloor, 2);
			tv_finc_fundadd_buy_floor.setText(fincFundAddBuyFloor);
		}

		// 申购下限
		String fincRansomFloor = (String) fincControl.fundDetails
				.get(Finc.I_APPLYLOWLIMIT);
		if (StringUtil.isNull(fincRansomFloor)) {
			fincRansomFloor = "-";
		} else {
			fincRansomFloor = StringUtil.parseStringCodePattern(currencyStr,
					fincRansomFloor, 2);
		}
		tv_finc_ransom_floor.setText(fincRansomFloor);

		// 定期定额申购下限
		String fincSchedubuyLimitColon = (String) fincControl.fundDetails
				.get(Finc.FINC_SCHEDULEAPPLYLOWLIMIT);
		if (StringUtil.isNull(fincSchedubuyLimitColon)) {
			fincSchedubuyLimitColon = "-";
		} else {
			fincSchedubuyLimitColon = StringUtil.parseStringCodePattern(
					currencyStr, fincSchedubuyLimitColon, 2);
		}
		tv_finc_schedubuyLimit_colon.setText(fincSchedubuyLimitColon);

		// 单日购买上限
		String fincDayLimit = (String) fincControl.fundDetails
				.get(Finc.FINC_DAYMAXSUMBUY);
		// if(StringUtil.isNull(fincDayLimit) || "0".equals(fincDayLimit)){
		// fincDayLimitLayout.setVisibility(View.GONE);
		// }else{
		fincDayLimit = StringUtil.parseStringCodePattern(currencyStr,
				fincDayLimit, 2);
		tv_finc_daylimit.setText(fincDayLimit);
		// };

		// 收费方式
		String feeType = (String) fincControl.fundDetails
				.get(Finc.FINC_FEETYPE_REQ);
		if (StringUtil.isNull(feeType)) {
			feeType = "-";
		} else {
			feeType = LocalData.fundfeeTypeCodeToStr.get(feeType);
		}
		tv_finc_feetype.setText(feeType);
	}

	/**
	 * 初始化产品属性数据
	 */
	private void initProductPropViewData() {
		// 产品类型
		String fnTypeStr = StringUtil.valueOf1((String) fincControl.fundDetails
				.get(Finc.FINC_FNTYPE));
		if ("06".equals(fnTypeStr)) { // 货币型基金
			fincn_daynetvaluerate_label
					.setText(getString(R.string.finc_everytenthousand_colon));
			finc_totlevalue_label
					.setText(getString(R.string.finc_fundIncomeRatio_colon));

			// 每万份基金单位收益
			String everytenthousandStr = StringUtil
					.valueOf1((String) fincControl.fundDetails
							.get(Finc.FINC_FUNDINCOMEUNIT));
			tv_fincn_daynetvaluerate.setText(everytenthousandStr);

			// 七日年化收益率
			String fundIncomeRatioStr = StringUtil
					.valueOf1((String) fincControl.fundDetails
							.get(Finc.SEVEN_DAY_YIELD));
			if ("-".equals(fundIncomeRatioStr)) {
				tv_finc_totlevalue.setText(fundIncomeRatioStr);
			} else {
				fundIncomeRatioStr = String.valueOf(Double
						.parseDouble(fundIncomeRatioStr) * 100);
				tv_finc_totlevalue.setText(fundIncomeRatioStr + "%");
			}

		} else { // 非货币型基金
			fincn_daynetvaluerate_label
					.setText(getString(R.string.fincn_daynetvaluerate_colon));
			finc_totlevalue_label
					.setText(getString(R.string.finc_totlevalue_colon));

			String daynetvaluerateStr = StringUtil
					.valueOf1((String) fincControl.fundDetails
							.get(Finc.FINC_DAYINCOMERATIO));
			if ("-".equals(daynetvaluerateStr)) {
				tv_fincn_daynetvaluerate.setText(daynetvaluerateStr);
			} else {
				if (daynetvaluerateStr.contains("%")) {
					tv_fincn_daynetvaluerate.setText(daynetvaluerateStr);
				} else {
					tv_fincn_daynetvaluerate.setText(daynetvaluerateStr + "%");
				}
			}


			String totlevalueStr = (String) fincControl.fundDetails
					.get(Finc.FINC_ADDUPNETVAL);

			tv_finc_totlevalue.setText(StringUtil.parseStringPattern(
					totlevalueStr, 4));
		}
		tv_finc_producttype
				.setText(LocalData.fundProductTypeMap.get(fnTypeStr));

		// 净值截止日期
		String fincMyFincNetPriceDate = StringUtil
				.valueOf1((String) fincControl.fundDetails
						.get(Finc.FINC_ENDDATE));
		tv_finc_myfinc_netPriceDate.setText(fincMyFincNetPriceDate);

		// 交易币种
		currencyStr = (String) fincControl.fundDetails.get(Finc.FINC_CURRENCY);
		String cashFlagCode = (String) fincControl.fundDetails
				.get(Finc.FINC_CASHFLAG);
		tv_finc_fundcurrency_type.setText(FincControl.fincCurrencyAndCashFlag(
				currencyStr, cashFlagCode));

		// 产品种类
		String productKindStr = StringUtil
				.valueOf1((String) fincControl.fundDetails
						.get(Finc.COMBINQUERY_FNTKIND));
		if ("-".equals(productKindStr)) {
			tv_finc_productkind.setText(productKindStr);
		} else {
			if ("02".equals(productKindStr) || "03".equals(productKindStr)
					|| "07".equals(productKindStr)) {
				tv_finc_productkind
						.setText((String) LocalData.fincFundTypeCodeToStr
								.get("07"));
			} else {
				tv_finc_productkind
						.setText((String) LocalData.fincFundTypeCodeToStr
								.get(productKindStr));
			}
		}

		// 基金状态
		String fincFundStateStr = StringUtil
				.valueOf1((String) fincControl.fundDetails
						.get(Finc.FINC_FUNDSTATE));
		if ("-".equals(fincFundStateStr)) {
			tv_finc_productstatus.setText(fincFundStateStr);
		} else {
			tv_finc_productstatus
					.setText((String) LocalData.fincFundStateCodeToStr
							.get(fincFundStateStr));
		}

		// 默认分红方式
		String defaultBonus = StringUtil
				.valueOf1((String) fincControl.fundDetails
						.get(Finc.FINC_DEFAULT_BONUS));
		if ("-".equals(defaultBonus)) {
			tv_finc_share_way.setText(defaultBonus);
		} else {
			tv_finc_share_way.setText((String) LocalData.bonusTypeMap
					.get(defaultBonus));
		}

		// 手续费率
		String chargeRate = StringUtil
				.valueOf1((String) fincControl.fundDetails
						.get(Finc.FINC_CHARGE_RATE));
		tv_finc_procedure_rate.setText(chargeRate);

		// 优惠信息
		String discount = StringUtil.valueOf1((String) fincControl.fundDetails
				.get(Finc.FINC_DISCOUNT));
		tv_finc_privilege_info.setText(discount);

	}

	/**
	 * 初始化主布局数据
	 */
	private void initMainViewData() {
		// 基金名称和代码
		String fundNameStr = StringUtil
				.valueOf1((String) fincControl.fundDetails
						.get(Finc.FINC_FUNDNAME));
		fundCodeStr = StringUtil.valueOf1((String) fincControl.fundDetails
				.get(Finc.FINC_FUNDCODE));
		StringBuffer sb = new StringBuffer();
		sb.append(fundNameStr);
		sb.append("(");
		sb.append(fundCodeStr);
		sb.append(")");
		fundNameAndCodeTextView.setText(sb.toString());

		// 基金单位净值
		String netValueStr = (String) fincControl.fundDetails
				.get(Finc.FINC_NETPRICE);
		fundNetValueTextView.setText(StringUtil.parseStringPattern(netValueStr,
				4));

		// 基金状态
		String fundStatusStr = LocalData.fincFundStateCodeToStr
				.get(fincControl.fundDetails.get(Finc.FINC_FUNDSTATE));
		int istart = 2;
		SpannableStringBuilder istyle = new SpannableStringBuilder(
				fundStatusStr);
		istyle.setSpan(
				new BackgroundColorSpan(getResources().getColor(
						R.color.orange_new)), 0, istart,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		istyle.setSpan(new ForegroundColorSpan(Color.WHITE), 0, istart,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		fundStatus.setText(istyle);

		// 基金公司
		fundCompanyTextView.setText(StringUtil
				.valueOf1((String) fincControl.fundDetails
						.get(Finc.FINC_FUNDCOMPANYNAME)));

		canBuy = StringUtil.parseStrToBoolean((String) fincControl.fundDetails
				.get(Finc.CANBUY));
		if (canBuy) { // 可买入
			buyButton.setVisibility(View.VISIBLE);

		} else {
			buyButton.setVisibility(View.GONE);
		}

		canScheduleBuy = StringUtil
				.parseStrToBoolean((String) fincControl.fundDetails
						.get(Finc.CANSCHEDULEBUY));
		if (canScheduleBuy) { // 可定投
			inversButton.setVisibility(View.VISIBLE);
		} else {
			inversButton.setVisibility(View.GONE);
		}

		setAttentionIconAndText(FincControl.getInstance().getAttentionFlag());

	}

	/**
	 * 设置关注状态
	 * 
	 * @param attentionFlag
	 */
	private void setAttentionIconAndText(boolean attentionFlag) {
		if (attentionFlag) { // 已关注
			fundAttentionImage.setBackgroundResource(R.drawable.attentioned);
			fundAttentionTextView
					.setText(getString(R.string.finc_attentioned_underline));
		} else { // 未关注
			fundAttentionImage.setBackgroundResource(R.drawable.attention);
			fundAttentionTextView
					.setText(getString(R.string.finc_attention_underline));
		}
	}
	
	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		// 随机数获取异常
		if (Finc.FINC_ATTENTIONQUERYLIST.equals(((BiiResponse) resultObj)
				.getResponse().get(0).getMethod())) {
			if (((BiiResponse) resultObj).isBiiexception()) {// 代表返回数据异常
				FincControl.getInstance().setAttentionFlag(false);
				setAttentionIconAndText(false);
				BaseHttpEngine.dissMissProgressDialog();
				return true;
			}
			return false;// 没有异常
		} else {
			return super.httpRequestCallBackPre(resultObj);
		}
	}

	/**
	 * 为body_layout添加view
	 * 
	 * @param view
	 */
	private void setBodyLayoutChildView(View view) {
		body_layout.removeAllViews();
		body_layout.addView(view);
		body_layout.invalidate();
	}

	/**
	 * 初始化赎回属性布局
	 */
	private void initRedeemPropView() {
		redeemView = mainInflater.inflate(
				R.layout.finc_funddetail_activity4_new, null);
		tv_finc_sellLowLimit = (TextView) redeemView
				.findViewById(R.id.tv_finc_sellLowLimit);
		tv_finc_myfinc_holdQutyLowLimit = (TextView) redeemView
				.findViewById(R.id.tv_finc_myfinc_holdQutyLowLimit);
		tv_finc_myfinc_day_toplimit = (TextView) redeemView
				.findViewById(R.id.tv_finc_myfinc_day_toplimit);
		finc_lately_can_ransom_ll = (LinearLayout) redeemView
				.findViewById(R.id.finc_lately_can_ransom_ll);
		tv_finc_lately_can_ransom = (TextView) redeemView
				.findViewById(R.id.tv_finc_lately_can_ransom);

		setOnShowAllTextListener(tv_finc_sellLowLimit,
				tv_finc_myfinc_holdQutyLowLimit, tv_finc_myfinc_day_toplimit,
				tv_finc_lately_can_ransom);
	}

	/**
	 * 初始化购买属性布局
	 */
	private void initBuyPropView() {
		buyView = mainInflater.inflate(R.layout.finc_funddetail_activity3_new,
				null);
		tv_finc_fundfirst_buy_floor = (TextView) buyView
				.findViewById(R.id.tv_finc_fundfirst_buy_floor);
		tv_finc_fundadd_buy_floor = (TextView) buyView
				.findViewById(R.id.tv_finc_fundadd_buy_floor);
		tv_finc_ransom_floor = (TextView) buyView
				.findViewById(R.id.tv_finc_ransom_floor);
		tv_finc_schedubuyLimit_colon = (TextView) buyView
				.findViewById(R.id.tv_finc_schedubuyLimit_colon);
		tv_finc_daylimit = (TextView) buyView
				.findViewById(R.id.tv_finc_daylimit);
		tv_finc_feetype = (TextView) buyView.findViewById(R.id.tv_finc_feetype);

		setOnShowAllTextListener(tv_finc_fundfirst_buy_floor,
				tv_finc_fundadd_buy_floor, tv_finc_ransom_floor,
				tv_finc_schedubuyLimit_colon, tv_finc_daylimit);
	}

	/**
	 * 初始化产品属性布局
	 * 
	 */
	private void initProductPropView() {

		productView = mainInflater.inflate(
				R.layout.finc_funddetail_activity2_new, null);

		fincn_daynetvaluerate_label = (TextView) productView
				.findViewById(R.id.fincn_daynetvaluerate_label);
		tv_fincn_daynetvaluerate = (TextView) productView
				.findViewById(R.id.tv_fincn_daynetvaluerate);

		finc_totlevalue_label = (TextView) productView
				.findViewById(R.id.finc_totlevalue_label);
		tv_finc_totlevalue = (TextView) productView
				.findViewById(R.id.tv_finc_totlevalue);

		tv_finc_myfinc_netPriceDate = (TextView) productView
				.findViewById(R.id.tv_finc_myfinc_netPriceDate);
		tv_finc_fundcurrency_type = (TextView) productView
				.findViewById(R.id.tv_finc_fundcurrency_type);
		tv_finc_productkind = (TextView) productView
				.findViewById(R.id.tv_finc_productkind);
		tv_finc_producttype = (TextView) productView
				.findViewById(R.id.tv_finc_producttype);

		finc_productstatus_ll = (LinearLayout) productView
				.findViewById(R.id.finc_productstatus_ll);
		tv_finc_productstatus = (TextView) productView
				.findViewById(R.id.tv_finc_productstatus);
		tv_finc_share_way = (TextView) productView
				.findViewById(R.id.tv_finc_share_way);
		tv_finc_procedure_rate = (TextView) productView
				.findViewById(R.id.tv_finc_procedure_rate);
		tv_finc_privilege_info = (TextView) productView
				.findViewById(R.id.tv_finc_privilege_info);
		
//		if (fundStatus.isShown()) {
//			finc_productstatus_ll.setVisibility(View.GONE);
//		} else {
//			finc_productstatus_ll.setVisibility(View.VISIBLE);
//		}

		setOnShowAllTextListener(tv_fincn_daynetvaluerate, tv_finc_totlevalue,
				tv_finc_myfinc_netPriceDate, tv_finc_fundcurrency_type,
				tv_finc_productkind, tv_finc_producttype, tv_finc_share_way,
				tv_finc_procedure_rate);

	}

	/**
	 * 初始化主布局
	 */
	private void initMainView() {
		setRightToMainHome();
		mainView = mainInflater.inflate(R.layout.finc_funddetail_activity1_new,
				null);
		tabcontent.addView(mainView);
		tabcontent.setPadding(0, 0, 0, 0);
		setTitle(R.string.finc_title_funddetails);

		// 右上角登陆按钮
//		login = (Button) findViewById(R.id.ib_top_right_login_btn);

		fundNameAndCodeTextView = (TextView) mainView
				.findViewById(R.id.tv_fund_name_and_code);
		fundNetValueTextView = (TextView) mainView
				.findViewById(R.id.tv_finc_netvalue);
		fundAttentionLayout = (LinearLayout) mainView
				.findViewById(R.id.finc_attention_ll);
		fundAttentionImage = (ImageView) mainView
				.findViewById(R.id.finc_attention_imageFlag);
		fundAttentionTextView = (TextView) mainView
				.findViewById(R.id.tv_finc_attention);
		fundAttentionTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		fundAttentionTextView.getPaint().setAntiAlias(true);
		fundValueChartLayout = (LinearLayout) mainView
				.findViewById(R.id.finc_valuechart_ll);
		fundValueChartTextView = (TextView) mainView
				.findViewById(R.id.tv_finc_valuechart);
		fundValueChartTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		fundValueChartTextView.getPaint().setAntiAlias(true);
		fundStatus = (TextView) mainView.findViewById(R.id.tv_finc_status);
		fundCompanyTextView = (TextView) mainView
				.findViewById(R.id.tv_finc_fundcompany);
		inversButton = (Button) mainView.findViewById(R.id.invers);
		buyButton = (Button) mainView.findViewById(R.id.btnbuy);
		radioGroup = (RadioGroup) mainView.findViewById(R.id.radioGroup);
		productButton = (RadioButton) mainView
				.findViewById(R.id.product_button);
		byButton = (RadioButton) mainView.findViewById(R.id.by_button);
		redeemButton = (RadioButton) mainView.findViewById(R.id.redeem_button);

		body_layout = (LinearLayout) mainView.findViewById(R.id.body_layout);

		setOnShowAllTextListener(fundCompanyTextView);
	}

	public void setOnShowAllTextListener(TextView... textViews) {
		for (TextView textView : textViews) {
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
					textView);
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.product_button: // 产品属性
			setBodyLayoutChildView(productView);
			break;

		case R.id.by_button: // 购买属性
			setBodyLayoutChildView(buyView);
			break;

		case R.id.redeem_button: // 赎回属性
			setBodyLayoutChildView(redeemView);
			break;

		default:
			break;
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		isLogin = BaseDroidApp.getInstanse().isLogin();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case ConstantGloble.ACTIVITY_REQUEST_LOGIN_CODE:
			switch (resultCode) {
			case RESULT_OK:
				switch (operation_flag) {
				case OPERATION_NONE:
					ActivityTaskManager.getInstance().removeAllSecondActivity();
					Intent intent = new Intent(FincFundDetailActivityNew.this,FundPricesActivityNew.class);
					startActivity(intent);
					break;

				default:
					isLoginCallback = true ;
					BaseHttpEngine.showProgressDialog();
					doCheckRequestPsnInvestmentManageIsOpen();
					break;
				}
				break;

			default:
				break;
			}
			break;
				
		case ConstantGloble.ACTIVITY_REQUEST_MANAGE_CODE:// 开通理财服务
			switch (resultCode) {
			case RESULT_OK:
				fincControl.ifInvestMent = true;
				if (!fincControl.ifhaveaccId) {// 如果还没有基金账户
					getPopup();
				}
				break;

			default:
				fincControl.ifInvestMent = false;
				getPopup();
				break;
			}
			break;
		case ConstantGloble.ACTIVITY_REQUEST_SETFINCSACC_CODE:// 开通基金账户
			switch (resultCode) {
			case RESULT_OK:
				fincControl.ifhaveaccId = true;
				BaseHttpEngine.showProgressDialog();
				attentionFundQuery();
				break;

			default:
				fincControl.ifhaveaccId = false;
				getPopup();
				break;
			}
			break;
		case InvestConstant.FUNDRISK:// 基金风险评估
			switch (resultCode) {
			case RESULT_OK:
				fincControl.ifdorisk = true;
				switch (operation_flag) {
				case OPERATION_BUY: // 买入
					startBuyActivity();
					break;
				case OPERATION_INVERS: // 定投
					startInversActivity();
					break;
				default:
					break;
				}
				break;
			default:
				fincControl.ifdorisk = false;
				getPopupForRisk();
				break;
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void doCheckRequestQueryInvtBindingInfoCallback(
			Object resultObj) {
		super.doCheckRequestQueryInvtBindingInfoCallback(resultObj);
		if(!fincControl.ifhaveaccId || !fincControl.ifInvestMent) return;
		if (fincControl.ifInvestMent && fincControl.ifhaveaccId) {
			attentionFundQuery();
		}

	}

	@Override
	public void attentionFundQueryCallback(Object resultObj) {
		super.attentionFundQueryCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (!StringUtil.isNullOrEmpty(resultMap)) {
			if (!StringUtil.isNullOrEmpty(resultMap
					.get(Finc.FINC_ATTENTIONQUERYLIST_ATTENTIONLIST))) {
				fincControl.attentionFundList = (List<Map<String, Object>>) resultMap
						.get(Finc.FINC_ATTENTIONQUERYLIST_ATTENTIONLIST);
				for (Map<String, Object> map : fincControl.attentionFundList) {
					// 已关注的基金
					String fundCodeAttentioned = (String) map
							.get(Finc.FINC_FUNDCODE);
					// 当前选择的基金
					String fundCodeCurrent = (String) fincControl.fundDetails
							.get(Finc.FINC_FUNDCODE);
					if (fundCodeAttentioned.equals(fundCodeCurrent)) {// 如果已关注
						fincControl.setAttentionFlag(true);
					}
				}
			}
		}
		if (StringUtil.isNullOrEmpty(fincControl.attentionFundList)) {
			FincControl.getInstance().setAttentionCount(0);
		} else {
			FincControl.getInstance().setAttentionCount(
					fincControl.attentionFundList.size());
		}
		setAttentionIconAndText(fincControl.getAttentionFlag());

		// 重新查询登录后的基金详情
		getFincFund(fundCodeStr);
	}
	
	public void getFincFundCallback(Object resultObj) {
		super.attentionFundQueryCallback(resultObj);
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		BiiHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					getString(R.string.acc_transferquery_null));
			return;	
		}
		fincControl.fundDetails = resultMap;
		switch (operation_flag) {
		case OPERATION_ATTENTION: // 关注
			modifyAttentionStatus();
			break;
		case OPERATION_BUY: // 买入
			if(isLoginCallback){
				/**基金买入，判断是否未风险评估*/
				BaseHttpEngine.showProgressDialog();
				doCheckRequestPsnFundRiskEvaluationQueryResult();
			}else{
				startBuyActivity();
			}
			break;
		case OPERATION_INVERS: // 定投
			if(isLoginCallback){
				/**基金定投，判断是否未风险评估*/
				BaseHttpEngine.showProgressDialog();
				doCheckRequestPsnFundRiskEvaluationQueryResult();
			}else{
				startInversActivity();
			}
			
			break;
		// case OPERATION_ADD_TA_ACCOUNT:// 添加基金ta账户
		// fincControl.tradeFundDetails = fincControl.fundDetails;
		// startActivity(new Intent(this, FincFundTaSettingActivity.class));
		// break;
		default:
			break;
		}
	}
	
	/**
	 * 检查是否做了风险认证的回调处理
	 * 
	 * @param resultObj
	 */
	public void doCheckRequestPsnFundRiskEvaluationQueryResultCallback(
			Object resultObj) {
		super.doCheckRequestPsnFundRiskEvaluationQueryResultCallback(resultObj);
		if(fincControl.ifdorisk){
			switch (operation_flag) {
			case OPERATION_BUY: // 买入
				startBuyActivity();
				break;
			case OPERATION_INVERS: // 定投
				startInversActivity();
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		fincControl.fundDetails = null;
	}
}

