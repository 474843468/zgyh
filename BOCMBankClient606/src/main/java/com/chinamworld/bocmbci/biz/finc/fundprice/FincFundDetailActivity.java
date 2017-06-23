package com.chinamworld.bocmbci.biz.finc.fundprice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
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
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.biz.finc.trade.FincTradeBuyActivity;
import com.chinamworld.bocmbci.biz.finc.trade.FincTradeScheduledBuyActivity;
import com.chinamworld.bocmbci.biz.invest.constant.InvestConstant;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 显示查询到的基金详情页面
 * 
 * @author xyl
 * 
 */
public class FincFundDetailActivity extends FincBaseActivity implements OnCheckedChangeListener{
	private static final String TAG = "FincFundDetailActivity";
	/**
	 * 基金它按钮
	 */
	//	private TextView fundTATextView;
	/**
	 * 基金代码显示文本
	 */
	private TextView fundCodeTextView;
	/**
	 * 基金名字显示文本
	 */
	private TextView fundNameTextView;
	/**
	 * 基金公司显示文本
	 */
	private TextView fundcompanyTextView;
	/**
	 * 币种显示文本
	 */
	private TextView currencyTextView;
	/**
	 * 单位净值显示文本
	 */
	private TextView netvalueTextView;
	/**
	 * 单日购买上限金额
	 */
	//private TextView DayMaxSumBuyTextView;
	/**
	 * 单位净值增长率显示文本
	 */
	private TextView netvalueChTextView;
	/**单位净值增长 LinearLayout*/
	private LinearLayout finc_netvaluech_title;
	/**
	 * 总净值显示文本
	 */
	private TextView totalvalueTextView;
	/**总净值 LinearLayout*/
	private LinearLayout confirm_textview_style_title;
	/**
	 * 每万份基金单位收益
	 */
	private TextView everytenthousendTextView;
	/**
	 * 买入按钮
	 */
	private Button buyBtn;
	/**
	 * 定投安妮
	 */
	private Button inversBtn;
	/**
	 * 关注按钮
	 */
	private LinearLayout attentionBtn;
	/**
	 * 净值走势图按钮
	 */
//	private ImageView valuechartBtn;

	/**产品种类 */
	private TextView finc_fundtype_colon;


	/**单位净值增长率左边字段*/
	private TextView finc_netvaluech_tv;
	/**每万份基金单位收益title字段*/
	private LinearLayout finc_everytenthousand_title;
	/**七日年化收益率title字段*/
	private LinearLayout finc_fundIncomeRatio_title;
	/**七日年化收益率*/
	private TextView finc_fundIncomeRatio;
	/**产品类型*/
	private TextView finc_product_type;

	/**基金状态*/
	private TextView finc_fundstate_colon;
	/**净值截止日期*/
	private TextView finc_myfinc_netPriceDate;

	/**默认分红方式*/
	private TextView finc_share_way;

	/**手续费率*/
	private TextView finc_procedure_rate;

	/**优惠信息*/
	private TextView finc_privilege_info;

	/**显示的body view*/
	private LinearLayout body_layout;
	/**
	 * 当前基金代码
	 */
	private String fundCodeStr;
	private String fundNameStr;
	private String fundCompanyStr;
	private String currencyStr, cashFlagCode;
	private String netValueStr;
	private String netValueChStr;
	private String totalValueStr;
	private String everyTenThousandStr;
	//七日年化收益率
	private String fundIncomeRatio;

	private ImageView statImg;

	/** 是否可买入 */
	private boolean canBuy;
	/** 是否可定投 */
	private boolean canScheduleBuy;
	/** 当前点击是 买入or 定投 */
	private int current_opertion = 1;
	/** 买入 */
	private final int OPERATION_BUY = 1;
	/** 定投 */
	private final int OPERATION_INVERS = 2;

	/**单日赎回上限layout*/
	private View forexMyFincDayTopLimitLayout;



	/** 从基金关注进入　 */
	public static final int ATTENTION = 2;

	private RadioGroup radioGroup;

	/**产品属性 按钮*/
	private RadioButton product_button;
	/**购买属性 按钮*/

	private RadioButton by_button;
	/**赎回属性 按钮*/
	private RadioButton redeem_button;



	//////////////////////      购买属性         //////////////////////
	/** * 申购下限*/
	private  TextView fincRansomFloorView;

	/*** 单日购买上限 */
	private TextView fincDayLimitView;

	/** * 定期定额申购下限 */
	private TextView fincSchedubuyLimitColonView;

	/**首次认购下限*/
	private TextView finc_fundfirst_buy_floor;
	/**追加认购下限*/
	private TextView finc_fundadd_buy_floor;
	/**收费方式*/
	private TextView finc_fund_toll_type;

	/**单日购买上限layout*/
	private View fincDayLimitLayout;

	//////////////////////      赎回属性         //////////////////////
	/**赎回下限 */
	private TextView fincSellLowLimitColonView;
	/*** 最低持有份额*/
	private TextView fincMyFincHoldQutyLowLimitView;
	/*** 单日赎回上限*/
	private TextView forexMyFincDayTopLimitView;
	/**最近可赎回日期layout*/
	private View finc_lately_can_ransom_layout;
	/**最近可赎回日期*/
	private TextView fincLatelyCanRansomView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		try {
			initData();
		} catch (Exception e) {
			LogGloble.d(TAG, e.toString());
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
		if (FincControl.getInstance().getAttentionFlag()) {// 已经关注
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
		FincControl.getInstance().setAttentionFlag(true);
		statImg.setImageResource(R.drawable.img_star_small);

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
		FincControl.getInstance().setAttentionFlag(false);
		FincControl.getInstance().minusAttentionCount1();
		statImg.setImageResource(R.drawable.img_guanzhu);
		CustomDialog.toastShow(this,
				getResources()
				.getString(R.string.finc_myfinc_follow_cancelfinc));
	}

	/**
	 * 设定关注按钮上的星星 是否可见
	 * 
	 * @param attentionFlag
	 */
	private void setAttentionIcon(boolean attentionFlag) {
		if (attentionFlag) {// 如果已关注
			statImg.setImageResource(R.drawable.img_star_small);
		} else {
			statImg.setImageResource(R.drawable.img_guanzhu);
		}
	}

	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		// 随机数获取异常
		if (Finc.FINC_ATTENTIONQUERYLIST.equals(((BiiResponse) resultObj)
				.getResponse().get(0).getMethod())) {
			if (((BiiResponse) resultObj).isBiiexception()) {// 代表返回数据异常
				FincControl.getInstance().setAttentionFlag(false);
				statImg.setImageResource(R.drawable.img_guanzhu);
				BaseHttpEngine.dissMissProgressDialog();
				return true;
			}
			return false;// 没有异常
		} else {
			return super.httpRequestCallBackPre(resultObj);
		}
	}


	/**
	 * 初始化布局
	 * 
	 * @Author xyl
	 */
	View childview1;
	private void init() {
		View childview = mainInflater.inflate(
				R.layout.finc_funddetail_activity1, null);
		tabcontent.addView(childview);
		setTitle(R.string.finc_title_funddetails);

		body_layout = (LinearLayout) childview.findViewById(R.id.body_layout);

		childview1 = mainInflater.inflate(
				R.layout.finc_funddetail_activity2, null);

		body_layout.addView(childview1);
		fundCodeTextView = (TextView) childview
				.findViewById(R.id.finc_fundcode);
		fundNameTextView = (TextView) childview
				.findViewById(R.id.finc_fundname);
		fundcompanyTextView = (TextView) childview
				.findViewById(R.id.finc_fundcompany);
		currencyTextView = (TextView) childview
				.findViewById(R.id.finc_fundcurrency_type);
		netvalueTextView = (TextView) childview
				.findViewById(R.id.finc_netvalue);

		/*DayMaxSumBuyTextView=(TextView) childview
				.findViewById(R.id.finc_daylimit);*/

		netvalueChTextView = (TextView) childview
				.findViewById(R.id.finc_netvaluech);
		totalvalueTextView = (TextView) childview
				.findViewById(R.id.finc_totlevalue);
		everytenthousendTextView = (TextView) childview
				.findViewById(R.id.finc_everytenthousand);
		finc_fundtype_colon = (TextView) childview.findViewById(R.id.finc_fundtype_colon);
		finc_fundstate_colon = (TextView) childview.findViewById(R.id.finc_fundstate_colon);


		finc_myfinc_netPriceDate = (TextView) childview.findViewById(R.id.finc_myfinc_netPriceDate);
		finc_product_type = (TextView)childview.findViewById(R.id.finc_product_type);
		/**单位净值增长率左边字段*/
		finc_netvaluech_tv=(TextView) findViewById(R.id.finc_netvaluech_tv);
         finc_everytenthousand_title=(LinearLayout) findViewById(R.id.finc_everytenthousand_title);
//         finc_netvaluech_title=(LinearLayout) findViewById(R.id.finc_netvaluech_title);
//         confirm_textview_style_title=(LinearLayout) findViewById(R.id.confirm_textview_style_title);
         finc_netvaluech_title=(LinearLayout) findViewById(R.id.finc_netvaluech_layout);
         confirm_textview_style_title=(LinearLayout) findViewById(R.id.finc_totlevalue_layout);
         
       finc_fundIncomeRatio_title=(LinearLayout) findViewById(R.id.finc_fundIncomeRatio_title);
		finc_everytenthousand_title=(LinearLayout) findViewById(R.id.finc_everytenthousand_title);

		finc_fundIncomeRatio_title=(LinearLayout) findViewById(R.id.finc_fundIncomeRatio_title);
		finc_fundIncomeRatio=(TextView) findViewById(R.id.finc_fundIncomeRatio);
		product_button = (RadioButton) findViewById(R.id.product_button);
		by_button = (RadioButton) findViewById(R.id.by_button);
		redeem_button = (RadioButton) findViewById(R.id.redeem_button);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		finc_share_way = (TextView) findViewById(R.id.finc_share_way);
		finc_procedure_rate = (TextView) findViewById(R.id.finc_procedure_rate);
		finc_privilege_info = (TextView) findViewById(R.id.finc_privilege_info);

		//		FincUtils.setOnShowAllTextListener(this , finc_product_type,fundNameTextView, fundcompanyTextView,finc_fundtype_colon,finc_fundstate_colon,finc_fundfirst_buy_floor,
		//				finc_fundadd_buy_floor,finc_myfinc_netPriceDate,
		//				currencyTextView, netvalueTextView, netvalueChTextView, totalvalueTextView,
		//				fincRansomFloorView, fincDayLimitView, fincSellLowLimitColonView,
		//				fincMyFincHoldQutyLowLimitView, forexMyFincDayTopLimitView, fincSchedubuyLimitColonView, fincLatelyCanRansomView );
		//		finc_ransom_floor_layout =  childview.findViewById(R.id.finc_ransom_floor_layout);
		//		finc_daylimit_layout =  childview.findViewById(R.id.finc_daylimit_layout);
		//		finc_sellLowLimit_colon_layout =  childview.findViewById(R.id.finc_sellLowLimit_colon_layout);
		//		finc_myfinc_holdQutyLowLimit_layout =  childview.findViewById(R.id.finc_myfinc_holdQutyLowLimit_layout); 
		//		forex_myfinc_day_toplimit_layout =  childview.findViewById(R.id.forex_myfinc_day_toplimit_layout);
		//		finc_schedubuyLimit_colon_layout = childview.findViewById(R.id.finc_schedubuyLimit_colon_layout);


		buyBtn = (Button) childview.findViewById(R.id.finc_buy);
		inversBtn = (Button) childview.findViewById(R.id.finc_invesment);
		attentionBtn = (LinearLayout) childview
				.findViewById(R.id.finc_attention);
//		valuechartBtn = (ImageView) childview
//				.findViewById(R.id.finc_valuechart);
		statImg = (ImageView) childview
				.findViewById(R.id.finc_attention_imageFlag);



		buyBtn.setOnClickListener(this);
		inversBtn.setOnClickListener(this);
		attentionBtn.setOnClickListener(this);
//		valuechartBtn.setOnClickListener(this);
		radioGroup.setOnCheckedChangeListener(this);
		//		product_button.setChecked(true);
		//		fundTATextView.setOnClickListener(this);
		//		initRightBtnForMain();
	}

	private void initByView(){

		fincRansomFloorView = (TextView) findViewById(R.id.finc_ransom_floor);
		fincDayLimitView = (TextView) findViewById(R.id.finc_daylimit);
		fincSchedubuyLimitColonView = (TextView) findViewById(R.id.finc_schedubuyLimit_colon);
		finc_fundfirst_buy_floor = (TextView) findViewById(R.id.finc_fundfirst_buy_floor);
		finc_fundadd_buy_floor = (TextView) findViewById(R.id.finc_fundadd_buy_floor);
		finc_fund_toll_type = (TextView) findViewById(R.id.finc_fund_toll_type);
		fincDayLimitLayout = findViewById(R.id.finc_daylimit_layout);
		attentionBtn = (LinearLayout) findViewById(R.id.finc_attention);
//		valuechartBtn = (ImageView) findViewById(R.id.finc_valuechart);
//		valuechartBtn.setOnClickListener(this);
		statImg = (ImageView) findViewById(R.id.finc_attention_imageFlag);
		attentionBtn.setOnClickListener(this);
		setAttentionIcon(FincControl.getInstance().getAttentionFlag());
	};

	private void initByData(){

		int flag = getIntent().getIntExtra(Finc.I_ATTENTIONFLAG, -1);
		if(flag == ATTENTION){
			//			fincControl.fundDetails = (Map<String, Object>) fincControl.fundDetails.get(Finc.FINC_FUNDINFO);
			fincControl.fundDetails = fincControl.fincFundDetails;  
		}

		String fincRansomFloor = (String) FincControl.getInstance().fundDetails.get(Finc.I_APPLYLOWLIMIT);
		if(StringUtil.isNull(fincRansomFloor)){
			fincRansomFloor = "-";
		}else{
			fincRansomFloor = StringUtil.parseStringCodePattern(currencyStr, fincRansomFloor, 2);
		};
		fincRansomFloorView.setText(fincRansomFloor);


		String fincDayLimit = (String) FincControl.getInstance().fundDetails.get(Finc.FINC_DAYMAXSUMBUY);
		if(StringUtil.isNull(fincDayLimit) || "0".equals(fincDayLimit)){
			fincDayLimitLayout.setVisibility(View.GONE);
		}else{
			fincDayLimit = StringUtil.parseStringCodePattern(currencyStr, fincDayLimit, 2);
			fincDayLimitView.setText(fincDayLimit);
		};

		String fincSchedubuyLimitColon = (String) FincControl.getInstance().fundDetails.get(Finc.FINC_SCHEDULEAPPLYLOWLIMIT);
		if(StringUtil.isNull(fincSchedubuyLimitColon)){
			fincSchedubuyLimitColon = "-";
		}else{
			fincSchedubuyLimitColon = StringUtil.parseStringCodePattern(currencyStr, fincSchedubuyLimitColon, 2);
		};
		fincSchedubuyLimitColonView.setText(fincSchedubuyLimitColon);

		String fincFundFirstBuyFloor =	(String) FincControl.getInstance().fundDetails.get(Finc.FINC_ORDERLOWLIMIT);
		if(StringUtil.isNull(fincFundFirstBuyFloor)){
			finc_fundfirst_buy_floor.setText("-");
		}else{
			fincFundFirstBuyFloor = StringUtil.parseStringCodePattern(currencyStr, fincFundFirstBuyFloor, 2);
			finc_fundfirst_buy_floor.setText(fincFundFirstBuyFloor);
		}


		String fincFundAddBuyFloor = (String) FincControl.getInstance().fundDetails.get(Finc.FINC_BUY_ADD_LOW_LMT);
		if(StringUtil.isNull(fincFundAddBuyFloor)){
			finc_fundadd_buy_floor.setText("-");
		}else{
			fincFundAddBuyFloor = StringUtil.parseStringCodePattern(currencyStr, fincFundAddBuyFloor, 2);
			finc_fundadd_buy_floor.setText(fincFundAddBuyFloor);
		}

		String feeType = (String) FincControl.getInstance().fundDetails.get(Finc.FINC_FEETYPE_REQ);
		if(StringUtil.isNull(feeType)){
			feeType = "-";
		}else{
			feeType = LocalData.fundfeeTypeCodeToStr.get(feeType);
		}
		finc_fund_toll_type.setText(feeType);


	}

	/**
	 * 初始化数据
	 * 
	 * @Author xyl
	 */
	private void initData() throws Exception{
		int flag = getIntent().getIntExtra(Finc.I_ATTENTIONFLAG, -1);
		if(flag == ATTENTION){
			//			fincControl.fundDetails = (Map<String, Object>) fincControl.fundDetails.get(Finc.FINC_FUNDINFO);

			fincControl.fundDetails = fincControl.fincFundDetails;  
		}

		//		if (flag == ATTENTION) {
		//			fundCodeStr = (String) fincControl.fundDetails
		//					.get(Finc.FINC_FUNDCODE);
		//			fundNameStr = (String) fincControl.fundDetails
		//					.get(Finc.FINC_ATTENTIONQUERYLIST_FUNDSHORTNAME);
		//			currencyStr = (String) fincControl.fundDetails
		//					.get(Finc.FINC_CURRENCY);
		cashFlagCode = (String) fincControl.fundDetails
				.get(Finc.FINC_CASHFLAG);
		//			fundCompanyStr = (String) fincControl.fundDetails
		//					.get(Finc.FINC_FUNDCOMPANYNAME);
		//			netValueStr = (String) fincControl.fundDetails
		//					.get(Finc.FINC_ATTENTIONQUERYLIST_NETPRICE);
		//			netvalueTextView.setText(StringUtil.valueOf1(netValueStr));
		//			currencyTextView.setText(FincControl.fincCurrencyAndCashFlag(
		//					currencyStr, cashFlagCode));
		//			fundcompanyTextView.setText(fundCompanyStr);
		//			fundCodeTextView.setText(fundCodeStr);
		//			fundNameTextView.setText(fundNameStr);
		//			attentionFlag = true;
		//			if (StringUtil.isNullOrEmpty(fincControl.attentionFundList)) {
		//				attentionCount = 0;
		//			} else {
		//				attentionCount = fincControl.attentionFundList.size();
		//			}
		//			setAttentionIcon(attentionFlag);
		//			netValueChStr = (String) fincControl.fundDetails
		//					.get(Finc.FINC_DAYINCOMERATIO);
		//			totalValueStr = (String) fincControl.fundDetails
		//					.get(Finc.FINC_ADDUPNETVAL);
		//			everyTenThousandStr = (String) fincControl.fundDetails
		//					.get(Finc.FINC_FUNDINCOMEUNIT);
		//			netvalueChTextView.setText(StringUtil.valueOf1(netValueChStr));
		//			totalvalueTextView.setText(StringUtil.valueOf1(totalValueStr));
		//			everytenthousendTextView.setText(StringUtil
		//					.valueOf1(everyTenThousandStr));
		//			BaseHttpEngine.showProgressDialog();
		//			getFundDetailByFundCode(fundCodeStr);
		//		} else {
		//			if (!StringUtil.isNullOrEmpty(fincControl.fundDetails)) {//
		fundCodeStr = (String) fincControl.fundDetails
				.get(Finc.FINC_FUNDCODE);
		fundNameStr = (String) fincControl.fundDetails
				.get(Finc.FINC_FUNDNAME);
		currencyStr = (String) fincControl.fundDetails
				.get(Finc.FINC_CURRENCY);
		fundCompanyStr = (String) fincControl.fundDetails
				.get(Finc.FINC_FUNDCOMPANYNAME);
		fundIncomeRatio=(String) fincControl.fundDetails
				.get(Finc.FINC_FUNDINCOMERATIO);
		fundCodeTextView.setText(fundCodeStr);
		fundNameTextView.setText(fundNameStr);
		/*DayMaxSumBuyTextView.setText(StringUtil.parseStringPattern((String) fincControl.fundDetails
						.get(Finc.FINC_DAYMAXSUMBUY),2 ));*/
		currencyTextView.setText(FincControl.fincCurrencyAndCashFlag(
				currencyStr, cashFlagCode));
		fundcompanyTextView.setText(fundCompanyStr);
		// if (fincControl.fundDetails.get(Finc.CANBUY) == null
		// || fincControl.fundDetails.get(Finc.CANSCHEDULEBUY) == null)
		// {// 不是从行情进来的没有个判定字段
		// BaseHttpEngine.showProgressDialog();
		// getFundDetailByFundCode(fundCodeStr);
		// } else {// 从行情接口进入 不用调用接口鸟.
		canBuy = StringUtil
				.parseStrToBoolean((String) fincControl.fundDetails
						.get(Finc.CANBUY));
		canScheduleBuy = StringUtil
				.parseStrToBoolean((String) fincControl.fundDetails
						.get(Finc.CANSCHEDULEBUY));
		netValueStr = (String) fincControl.fundDetails
				.get(Finc.FINC_NETPRICE);
		netvalueTextView.setText(StringUtil.parseStringPattern(netValueStr, 4));

		// }
		netValueChStr = (String) fincControl.fundDetails
				.get(Finc.FINC_DAYINCOMERATIO);
		totalValueStr = (String) fincControl.fundDetails
				.get(Finc.FINC_ADDUPNETVAL);
		everyTenThousandStr = (String) fincControl.fundDetails
				.get(Finc.FINC_FUNDINCOMEUNIT);


		//产品类型  FINC_FNTYPE = "fntype";
		String  fntype=(String) fincControl.fundDetails
				.get(Finc.FINC_FNTYPE);
		if("06".equals(fntype)|| "Y".equals(FincControl.getInstance().fundDetails.get(Finc.FINC_IS_SHORT_FUND))){
			//			finc_fundIncomeRatio_tv      finc_everytenthousand_tv
			finc_everytenthousand_title.setVisibility(View.VISIBLE);
			finc_fundIncomeRatio_title.setVisibility(View.VISIBLE);
			finc_netvaluech_title.setVisibility(View.GONE);
			confirm_textview_style_title.setVisibility(View.GONE);
			everytenthousendTextView.setText(StringUtil.valueOf1(everyTenThousandStr));
			finc_fundIncomeRatio.setText(fundIncomeRatio);
		}else{
			finc_everytenthousand_title.setVisibility(View.GONE);
			finc_fundIncomeRatio_title.setVisibility(View.GONE);
			finc_netvaluech_title.setVisibility(View.VISIBLE);
			confirm_textview_style_title.setVisibility(View.VISIBLE);
		}


		setAttentionIcon(FincControl.getInstance().getAttentionFlag());


		//货币型基金和短期理财型基金 不显示日净值增长率  累计净值
		if("06".equals(fntype) || "Y".equals(FincControl.getInstance().fundDetails.get(Finc.FINC_IS_SHORT_FUND))){
			findViewById(R.id.finc_netvaluech_layout).setVisibility(View.GONE);
			findViewById(R.id.finc_totlevalue_layout).setVisibility(View.GONE);
		}else{
			netvalueChTextView.setText(StringUtil.valueOf1(netValueChStr));
			totalvalueTextView.setText(StringUtil.parseStringPattern(totalValueStr, 4));
		}


		//				BaseHttpEngine.showProgressDialog();
		//				attentionFundQuery();
		//			}
		//		}
		//		TextView everyTenThousandTv = (TextView) findViewById(R.id.finc_everytenthousand_tv);
		//		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
		//				everyTenThousandTv);
		/**02：一对多  03：券商   07：资管计划产品     均展示为“资管计划产品”）*/
		if(!StringUtil.isNullOrEmpty( FincControl.getInstance().fundDetails)){
			String fincMyfincNetPriceDate =	(String) FincControl.getInstance().fundDetails.get(Finc.COMBINQUERY_FNTKIND);

			if(StringUtil.isNull(fincMyfincNetPriceDate) || StringUtil.isNull(LocalData.fincFundTypeCodeToStr.get(fincMyfincNetPriceDate))){
				finc_fundtype_colon.setText("-");
			}else {
				if("02".equals(fincMyfincNetPriceDate)||"03".equals(fincMyfincNetPriceDate)||"07".equals(fincMyfincNetPriceDate)){
					finc_fundtype_colon.setText("资管计划产品");
				}else{
					finc_fundtype_colon.setText(LocalData.fincFundTypeCodeToStr.get(fincMyfincNetPriceDate));
				}
			}
		}


		String fincFundStateColon =	(String) FincControl.getInstance().fundDetails.get(Finc.FINC_FUNDSTATE);
		if(StringUtil.isNull(fincFundStateColon)){
			finc_fundstate_colon.setText("-");
		}else{
			finc_fundstate_colon.setText(LocalData.fincFundStateCodeToStr.get(fincFundStateColon));
		}


		String fincMyFincNetPriceDate =	(String) FincControl.getInstance().fundDetails.get(Finc.FINC_ENDDATE);
		if(StringUtil.isNull(fincMyFincNetPriceDate)){
			finc_myfinc_netPriceDate.setText("-");
		}else{
			finc_myfinc_netPriceDate.setText(fincMyFincNetPriceDate);
		}

		String fincMyfincNetPriceDate =	(String) FincControl.getInstance().fundDetails.get(Finc.COMBINQUERY_FNTKIND);
		if("01".equals(fincMyfincNetPriceDate)){
			findViewById(R.id.finc_product_type_layout).setVisibility(View.VISIBLE);
			String fincProductType = (String) FincControl.getInstance().fundDetails.get(Finc.COMBINQUERY_FNTYPE);
			if(StringUtil.isNull(fincProductType)){
				finc_product_type.setText("-");
			}else{
				finc_product_type.setText
				(StringUtil.isNull(LocalData.fundProductTypeMap.get(fincProductType))?
						getString(R.string.finc_else) : LocalData.fundProductTypeMap.get(fincProductType));
			}
		}

		String defaultBonus = (String) FincControl.getInstance().fundDetails.get(Finc.FINC_DEFAULT_BONUS);
		if(StringUtil.isNull(defaultBonus)){
			defaultBonus = "-";
		}else{
			defaultBonus = LocalData.bonusTypeMap.get(defaultBonus);
		}
		finc_share_way.setText(defaultBonus);

		String chargeRate = (String) FincControl.getInstance().fundDetails.get(Finc.FINC_CHARGE_RATE);
		if(StringUtil.isNull(chargeRate)){
			chargeRate = "-";
		}
		finc_procedure_rate.setText(chargeRate);

		String discount = (String) FincControl.getInstance().fundDetails.get(Finc.FINC_DISCOUNT);
		if(StringUtil.isNull(discount)){
			discount = "-";
			finc_privilege_info.setText(discount);
		}else{
			finc_privilege_info.setText(discount);
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this, finc_privilege_info);
			
		}


	}

	/**
	 * 根据基金代码查询基金详情， FundInfo wms 基金
	 * 
	 * @param fundCode
	 *            要查询的基金代码
	 */
	private void queryfundDetailByFundCode(String fundCode) {
		List<String> list = new ArrayList<String>();
		list.add(fundCode);
		queryfundDetailByFundCode(list);
	}


	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		String currentTime = QueryDateUtils.getcurrentDate(dateTime);
		getKChartDate(fundCodeStr, currentTime);
	}

	@Override
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			if(requestCode == InvestConstant.FUNDRISK){
				switch (current_opertion) {
				case OPERATION_BUY:
					fincControl.tradeFundDetails = fincControl.fundDetails;
					Intent intent = new Intent();
					intent.setClass(this, FincTradeBuyActivity.class);
					intent.putExtra(Finc.I_ATTENTIONFLAG, ATTENTION);
					startActivityForResult(intent, 1);
					break;
				case OPERATION_INVERS:
					fincControl.tradeFundDetails = fincControl.fundDetails;
					intent = new Intent();
					intent.setClass(this, FincTradeScheduledBuyActivity.class);
					startActivityForResult(intent, 1);
					break;
				}
				break;
			}
			setResult(RESULT_OK);
			finish();
			break;

		default:
			if(requestCode == InvestConstant.FUNDRISK ){// 基金风险评估
				fincControl.ifdorisk = false;
				getPopupForRisk();
				break;
			}
			break;
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.finc_buy:// 买入
			if (canBuy) {// 可以买入
				current_opertion = OPERATION_BUY;
				/**基金买入，判断是否未风险评估*/
				BaseHttpEngine.showProgressDialog();
				doCheckRequestPsnFundRiskEvaluationQueryResult();
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getString(R.string.finc_canbuy_error));
				return;
			}
			break;
		case R.id.finc_fundta:// 添加基金ta账户
			fincControl.tradeFundDetails = fincControl.fundDetails;
			startActivity(new Intent(this, FincFundTaSettingActivity.class));
			break;
		case R.id.finc_invesment:// 定投
			if (canScheduleBuy) {
				current_opertion = OPERATION_INVERS;
				/**基金定投，判断是否未风险评估*/
				BaseHttpEngine.showProgressDialog();
				doCheckRequestPsnFundRiskEvaluationQueryResult();
			} else {
				BaseDroidApp.getInstanse().showInfoMessageDialog(
						getString(R.string.finc_cansecheduedbuy_error));
				return;
			}
			break;
		case R.id.finc_valuechart:// 走势图
			requestSystemDateTime();
			BiiHttpEngine.showProgressDialog();
			// KchartUtils.showKLine(xData, yData);
			// // 传入数据
			// startActivity(new Intent(this,
			// FincFundPricesChartActivity.class));
			break;
		case R.id.finc_attention:// 关注
			if (FincControl.getInstance().getAttentionFlag()) {// 已经关注
				BaseDroidApp.getInstanse().showErrorDialog(
						getResources().getString(
								R.string.finc_attention_conern_confirm),
								R.string.cancle, R.string.confirm,
								new OnClickListener() {
							@Override
							public void onClick(View v) {
								switch (Integer.parseInt(v.getTag() + "")) {
								case CustomDialog.TAG_SURE:
									// 确认取消关注
									BaseDroidApp.getInstanse()
									.dismissErrorDialog();
									requestCommConversationId();
									BaseHttpEngine.showProgressDialog();
									break;
								case CustomDialog.TAG_CANCLE:
									BaseDroidApp.getInstanse()
									.dismissErrorDialog();
									break;
								}
							}
						});
			} else {// 未关注
				if (FincControl.getInstance().getAttentionCount() >= 10) {
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							getString(R.string.finc_setattention_num_error));
				} else {
					requestCommConversationId();
					BaseHttpEngine.showProgressDialog();
				}

			}
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		fincControl.fundDetails = null;
	}


	View childview2;
	View childview3;
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.product_button:
			if(StringUtil.isNullOrEmpty(childview1)){
				childview1 = mainInflater.inflate(
						R.layout.finc_funddetail_activity2, null);
				body_layout.removeAllViews();
				body_layout.addView(childview1);
				init();
				try {
					initData();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				body_layout.removeAllViews();
				body_layout.addView(childview1);
			}
			body_layout.invalidate();

			break;
		case R.id.by_button:
			if(StringUtil.isNullOrEmpty(childview2)){
				childview2 = mainInflater.inflate(
						R.layout.finc_funddetail_activity3, null);
				body_layout.removeAllViews();
				body_layout.addView(childview2);
				initByView();
				initByData();
			}else{
				body_layout.removeAllViews();
				body_layout.addView(childview2);
			}
			body_layout.invalidate();

			break;
		case R.id.redeem_button:

			if(StringUtil.isNullOrEmpty(childview3)){
				childview3 = mainInflater.inflate(
						R.layout.finc_funddetail_activity4, null);
				body_layout.removeAllViews();
				body_layout.addView(childview3);
				initRedeemView();
				initRedeemData();
			}else{
				body_layout.removeAllViews();
				body_layout.addView(childview3);
			}
			body_layout.invalidate();
			break;
		default:
			break;
		}

	}

	private void initRedeemView(){
		fincSellLowLimitColonView = (TextView) findViewById(R.id.finc_sellLowLimit_colon);
		fincMyFincHoldQutyLowLimitView = (TextView) findViewById(R.id.finc_myfinc_holdQutyLowLimit);
		forexMyFincDayTopLimitView = (TextView) findViewById(R.id.forex_myfinc_day_toplimit);
		finc_lately_can_ransom_layout =  findViewById(R.id.finc_lately_can_ransom_layout);
		fincLatelyCanRansomView = (TextView)  findViewById(R.id.finc_lately_can_ransom);
		forexMyFincDayTopLimitLayout = findViewById(R.id.forex_myfinc_day_toplimit_layout);
		attentionBtn = (LinearLayout) findViewById(R.id.finc_attention);
//		valuechartBtn = (ImageView) findViewById(R.id.finc_valuechart);
		statImg = (ImageView) findViewById(R.id.finc_attention_imageFlag);
		attentionBtn.setOnClickListener(this);
//		valuechartBtn.setOnClickListener(this);
		setAttentionIcon(FincControl.getInstance().getAttentionFlag());
	}

	private void initRedeemData(){

		int flag = getIntent().getIntExtra(Finc.I_ATTENTIONFLAG, -1);
		if(flag == ATTENTION){
			//			fincControl.fundDetails = (Map<String, Object>) fincControl.fundDetails.get(Finc.FINC_FUNDINFO);
			fincControl.fundDetails = fincControl.fincFundDetails;  
		}


		String fincSellLowLimitColon = (String) FincControl.getInstance().fundDetails.get(Finc.FINC_SELLLOWLIMIT);
		if(StringUtil.isNull(fincSellLowLimitColon)){
			fincSellLowLimitColon = "-";
		}else{
			fincSellLowLimitColon = StringUtil.parseStringCodePattern(currencyStr, fincSellLowLimitColon, 2);
		};
		fincSellLowLimitColonView.setText(fincSellLowLimitColon);


		String fincMyFincHoldQutyLowLimit = (String) FincControl.getInstance().fundDetails.get(Finc.FINC_HOLDLOWCOUNT);
		if(StringUtil.isNull(fincMyFincHoldQutyLowLimit)){
			fincMyFincHoldQutyLowLimit = "-";
		}else{
			fincMyFincHoldQutyLowLimit = StringUtil.parseStringCodePattern(currencyStr, fincMyFincHoldQutyLowLimit, 2);
		};
		fincMyFincHoldQutyLowLimitView.setText(fincMyFincHoldQutyLowLimit);

		String forexMyFincDayTopLimit = (String) FincControl.getInstance().fundDetails.get(Finc.FINC_DAY_TOPLIMIT);
		if(StringUtil.isNull(forexMyFincDayTopLimit) || "0".equals(forexMyFincDayTopLimit)){
			forexMyFincDayTopLimitLayout.setVisibility(View.GONE);
		}else{
			forexMyFincDayTopLimit = StringUtil.parseStringCodePattern(currencyStr, forexMyFincDayTopLimit, 2);
			forexMyFincDayTopLimitView.setText(forexMyFincDayTopLimit);
		};


		String fincLatelyCanRansomDate = (String)FincControl.getInstance().fundDetails.get(Finc.FINC_DATE_Lately);
		if("Y".equals(FincControl.getInstance().fundDetails.get(Finc.FINC_IS_SHORT_FUND))){
			finc_lately_can_ransom_layout.setVisibility(View.VISIBLE);
			if(!StringUtil.isNull(fincLatelyCanRansomDate)){
				//						fincLatelyCanRansomDate = QueryDateUtils.getCurDate(Long.parseLong(fincLatelyCanRansomDate));
			}else{
				fincLatelyCanRansomDate = "-";
			}
			fincLatelyCanRansomView.setText(fincLatelyCanRansomDate);
		}else{
			finc_lately_can_ransom_layout.setVisibility(View.GONE);
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
			switch (current_opertion) {
			case OPERATION_BUY:
				fincControl.tradeFundDetails = fincControl.fundDetails;
				Intent intent = new Intent();
				intent.setClass(this, FincTradeBuyActivity.class);
				intent.putExtra(Finc.I_ATTENTIONFLAG, ATTENTION);
				startActivityForResult(intent, 1);
				break;
			case OPERATION_INVERS:
				fincControl.tradeFundDetails = fincControl.fundDetails;
				intent = new Intent();
				intent.setClass(this, FincTradeScheduledBuyActivity.class);
				startActivityForResult(intent, 1);
				break;
			default:
				break;
			}
		}
	}

}
