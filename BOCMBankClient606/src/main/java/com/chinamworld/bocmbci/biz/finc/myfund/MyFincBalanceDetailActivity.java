package com.chinamworld.bocmbci.biz.finc.myfund;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.FincUtils;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.biz.finc.trade.FincTradeBuyActivity;
import com.chinamworld.bocmbci.biz.finc.trade.FincTradeThrowActivity;
import com.chinamworld.bocmbci.biz.invest.constant.InvestConstant;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.ViewUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基金持仓--基金详情
 *
 * @author xiaoyl
 */
public class MyFincBalanceDetailActivity extends FincBaseActivity {
    private final String TAG = "MyFincFollowDetailActivity";
    /**
     * 基金持仓主view
     */
    private View myFincView = null;
    /**
     * 基金代码
     */
    private TextView fincCodeText = null;
    /**
     * 基金名称
     */
    private TextView fincNameText = null;
    /**
     * 币种
     */
    private TextView fincCurrencyText = null;
    /**
     * 单位净值
     */
    private TextView netPriceText = null;
    /**
     * 净值截止日期
     */
    private TextView finMyFincNetPriceDate = null;
    /**
     * 实际份额
     */
    private TextView fincRealityShare = null;
    /**
     * 持有份额
     */
    private TextView finc_reality_share_left = null;
    /**
     * 可用份额
     */
    private TextView fincMyFincTotalCount = null;
    //	/** 钞/汇 */
    //	private TextView fincMoneyColonRemit  = null;
    /**2014-11-2
     * 单日赎回上限
     */
    //private TextView dayTopLimit=null;
    /**
     * 当前市值
     */
    private TextView currentCapitalisationText = null;
    private Button btn1, btn3;
    private Button btn2;
    // /** 赎回 */
    // private Button sellButton = null;
    // /** 定期定额赎回 */
    // private Button scheduledSellButton = null;
    // /** 浮动盈亏 */
    // private Button fdykButton = null;
    // /** 基金转换 */
    // private Button giveButton = null;
    // /** 分红方式 */
    // private Button bonusTypeButton = null;
    // /** 净值走势图 */
    // private Button kButton = null;
    /** 基金详细信息 */
    /**
     * 基金状态
     */
    private String fundState = null;
    /**
     * 基金代码
     */
    private String foundCode = null;
    /**
     * 基金名称
     */
    private String foundName = null;
    /**
     * 分红方式
     */
    private String bonusType = null;
    /**
     * 基金交易详情
     */
    private Map<String, Object> fundBalanceMap;

    /**
     * 　是否显示分红方式
     */
    private boolean isShareRed;
    /**
     * 　判断是否显示赎回
     */
    private boolean isSale;
    /**
     * 　判断是否显示快速赎回
     */
    private boolean isFastSale;
    /**
     * 　是否显示定期定额赎回
     */
    private boolean isAddSale;
    /**
     * 　是否可以转出
     */
    private boolean isChangeOut;

    private String totalAvailableBalance;
    private boolean mIsFastSale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        getViewValue();
    }

    /**
     * 初始化控件
     */
    private void init() {
        setRightToMainHome();
        myFincView = mainInflater.inflate(R.layout.finc_myfinc_detail, null);
        tabcontent.addView(myFincView);
        setTitle(getResources().getString(R.string.finc_myfinc_balance));
        fincCodeText = (TextView) findViewById(R.id.finc_fincCode);
        fincNameText = (TextView) findViewById(R.id.finc_fincName);
        fincCurrencyText = (TextView) findViewById(R.id.finc_fincCurrency);
        netPriceText = (TextView) findViewById(R.id.finc_netPrice);
        currentCapitalisationText = (TextView) findViewById(R.id.finc_currentCapitalisation);
        finMyFincNetPriceDate = (TextView) findViewById(R.id.finc_myfinc_netPriceDate);
        fincRealityShare = (TextView) findViewById(R.id.finc_reality_share);
        //将实际份额左边字段修改为持有份额
        finc_reality_share_left = (TextView) findViewById(R.id.finc_reality_share_left);
        finc_reality_share_left.setText(getResources().getText(R.string.finc_reality_share_new));
        fincMyFincTotalCount = (TextView) findViewById(R.id.finc_myfinc_totalCount);
        //		fincMoneyColonRemit = (TextView) findViewById(R.id.finc_money_colon_remit);
        //单日赎回上限
        //	dayTopLimit=(TextView) findViewById(R.id.finc_dayTopLimit);
        btn1 = (Button) findViewById(R.id.finc_btn1);
        btn3 = (Button) findViewById(R.id.finc_btn3);
        btn2 = (Button) findViewById(R.id.finc_btn2);
        ViewUtils.initBtnParamsTwoLeft(btn1, this);
        //		ViewUtils.initBtnParamsTwoLeft(btn3, this);
        ViewUtils.initBtnParamsTwoRight(btn2, this);
        initRightBtnForMain();
        FincUtils.setOnShowAllTextListener(this, fincNameText, fincCodeText, fincNameText, netPriceText
                , currentCapitalisationText, finMyFincNetPriceDate, fincRealityShare, fincMyFincTotalCount);
    }

    /**
     * 为控件赋值
     */
    @SuppressWarnings("unused")
    private void getViewValue() {
        if (!StringUtil.isNullOrEmpty(fincControl.fundDetails)) {
            fundBalanceMap = fincControl.fundDetails;
            String currentCapitalisation = (String) fundBalanceMap
                    .get(Finc.FINC_CURRENTCAPITALISATION_REQ);
            bonusType = (String) fundBalanceMap.get(Finc.FINC_BOUNDSTYPE_REQ);
            //基金代码
            foundCode = String.valueOf(fundBalanceMap.get(Finc.FINC_FUNDCODE_REQ));
            totalAvailableBalance = (String) fundBalanceMap
                    .get(Finc.FINC_TOTALAVAILABLEBALANCE);
            //实际份额
            String fincRealityShareValue = (String) fundBalanceMap
                    .get(Finc.FINC_SELLALLLIMIT);
            if (StringUtil.isNull(foundCode)) {
                foundCode = "-";
                fincCodeText.setText(foundCode);
            } else {
                fincCodeText.setText(foundCode);
            }

            if (StringUtil.isNullOrEmpty(totalAvailableBalance)) {
                totalAvailableBalance = "-";
                fincMyFincTotalCount.setText(totalAvailableBalance);
            } else {
                //可用份额
                fincMyFincTotalCount.setText(StringUtil.parseStringPattern(
                        totalAvailableBalance, 2));
            }

            if (StringUtil.isNullOrEmpty(fincRealityShareValue)) {
                fincRealityShareValue = "-";
                fincRealityShare.setText(fincRealityShareValue);
            } else {
                //实际份额
                fincRealityShare.setText(StringUtil.parseStringPattern(
                        fincRealityShareValue, 2));
            }


            if (StringUtil.isNull(currentCapitalisation)) {
                currentCapitalisation = "-";
            }
            //当前市值
            currentCapitalisationText.setText(currentCapitalisation);
            //            返回的数据
            Map<String, String> fundInfoMap = (Map<String, String>) fundBalanceMap
                    .get(Finc.FINC_FUNDINFO_REQ);
            //			Map<String, String> fundInfoMap = new HashMap<String, String>();
            //			for (String key : fincControl.fincFundBasicDetails.keySet()) {
            //				fundInfoMap.put(key, (String)fincControl.fincFundBasicDetails.get(key));
            //			}
            String currency = null;
            String cashFlagCode = null;
            String netPrice = null;
            String endDate = null;
            String cashFlag = null;
            if (fundInfoMap == null) {//基金相关信息为空时取消操作按钮
                btn1.setVisibility(View.INVISIBLE);
                btn2.setVisibility(View.INVISIBLE);
                //				return;


            } else {
                foundName = fundInfoMap.get(Finc.FINC_FUNDNAME_REQ);

                currency = fundInfoMap.get(Finc.FINC_CURRENCY_REQ);
                cashFlagCode = fundInfoMap.get(Finc.FINC_CASHFLAG);
                netPrice = fundInfoMap.get(Finc.FINC_NETPRICE_REQ);
                endDate = (String) fincControl.fincFundBasicDetails.get(Finc.FINC_ENDDATE);
                cashFlag = fundInfoMap.get(Finc.FINC_ATTENTIONQUERYLIST_CASHFLAG);
                fundState = fundInfoMap.get(Finc.FINC_FUNDSTATE_REQ);

                // 根据字段 显示快速赎回按钮
                if (fundInfoMap.get(Finc.ISFASTSALE) != null) {
                    mIsFastSale = StringUtil.parseStrToBoolean(fundInfoMap.get(Finc.ISFASTSALE));
                    if (mIsFastSale) {
                        btn3.setVisibility(View.VISIBLE);
                        ViewUtils.initBtnParamsTwo606(btn3, this);
                        //                    ViewUtils.initBtnParamsTwoLeft(btn3, this);
                        btn3.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mIsFastSale) {
                                    fincControl.tradeFundDetails = fincControl.fundDetails;
                                    Intent mIntent = new Intent(MyFincBalanceDetailActivity.this,
                                            MyFincFastSellSubmitActivity.class);
                                    startActivityForResult(mIntent, 1);
                                }
                            }
                        });

                    }
                }
            }


            //钞汇标识
            //
            //			if(StringUtil.isNull(cashFlag)){
            //				cashFlag = "-";
            //			}else{
            //				cashFlag = LocalData.CurrencyCashremit.get(cashFlag);
            //			}
            //			fincMoneyColonRemit.setText(cashFlag);
            //净值截止日期
            if (StringUtil.isNull(endDate)) {
                endDate = "-";
            }
            finMyFincNetPriceDate.setText(endDate);

            //基金名称

            if (StringUtil.isNull(foundName)) {
                foundName = "-";
            }
            fincNameText.setText(foundName);
            /*
             * 2014-11-2
			 * 设置当日赎回上限
			 * */
            /*if (!StringUtil.isNullOrEmpty(fincControl.fincFundDetails)) {

			 dayTopLimit.setText(StringUtil.
					 parseStringPattern((String)fincControl.fincFundDetails.
							              get(Finc.FINC_DAY_TOPLIMIT), 2));
			}*/

            //基金币种
            if (StringUtil.isNull(currency)) {
                fincCurrencyText.setText("-");
            } else {
                fincCurrencyText.setText(FincControl.fincCurrencyAndCashFlag(
                        currency, cashFlagCode));
            }
            //单位净值
            if (StringUtil.isNull(netPrice)) {
                netPriceText.setText("-");
            } else {
                netPriceText.setText(StringUtil.parseStringPattern(netPrice, 4));
            }

            if (StringUtil.isNullOrEmpty(fundInfoMap)) {
                return;
            }
            final String[] selectors1 = getSelectors(fundInfoMap);
            final String[] selectors2 = getSelectors(fundInfoMap);
            final String[] selectors = getSelectors(selectors2);
            if (selectors1.length == 0) {
                btn1.setVisibility(View.GONE);
                btn2.setVisibility(View.GONE);
                return;
            }
            OnClickListener onClickListener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO 执行点击事件，抽离重复代码
                    executeClickCommand(getBtnMap().get(selectors1[0]));
                }
            };

            btn1.setText(selectors1[0]);
            btn1.setOnClickListener(onClickListener);
            if (selectors.length == 0) {// 只有一个按钮 肯定是净值走势图
                btn2.setVisibility(View.GONE);
                // TODO 只有一个按钮时，样式与后续一致
                LayoutParams params = (LayoutParams) btn1.getLayoutParams();
                params.setMargins(0, 0,
                        -(getResources().getDimensionPixelSize(R.dimen.fill_margin_right)), 0);
                btn1.setLayoutParams(params);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    //                api>=16
                    btn1.setBackground(getResources().getDrawable(R.drawable.fund_button_press_state));
                    btn1.setTextColor(Color.WHITE);
                } else {
                    //                api<16
                    btn1.setBackgroundDrawable(getResources().getDrawable(R.drawable.fund_button_press_state));
                    btn1.setTextColor(Color.WHITE);
                }
                //
                // TODO 屏蔽基金走势图
                //				btn1.setVisibility(View.GONE);
                //				btn1.setOnClickListener(new OnClickListener() {
                //					@Override
                //					public void onClick(View v) {
                //						//TODO 基金走势图 (后台没有数据，暂时屏蔽)
                //						requestSystemDateTime();
                //						BiiHttpEngine.showProgressDialog();
                //					}
                //				});
            } else if (selectors.length == 1) {// 两个按钮
                btn2.setText(selectors[0]);
                btn1.setOnClickListener(onClickListener);
                btn2.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //TODO 基金走势图 (后台没有数据，暂时屏蔽)
                        for (int i = 0; i < selectors.length; i++) {
//                            if (getString(R.string.finc_myfinc_button_fastsell).equals(selectors[i])) {
//                                //								快速赎回
//                                fincControl.tradeFundDetails = fincControl.fundDetails;
//                                Intent intent = new Intent(
//                                        MyFincBalanceDetailActivity.this,
//                                        MyFincFastSellSubmitActivity.class);
//                                startActivityForResult(intent, 1);
//                            } else
                        if (getString(R.string.finc_myfinc_button_sell).equals(selectors[i])) {
                                //赎回
                                fincControl.tradeFundDetails = fincControl.fundDetails;
                                Intent intent = new Intent(MyFincBalanceDetailActivity.this,
                                        MyFincSellSubmitActivity.class);
                                startActivityForResult(intent, 1);
                            } else if (getString(R.string.finc_myfinc_button_add_buy).equals(selectors[i])) {
                                //追加购买
                                BaseHttpEngine.showProgressDialog();
                                doCheckRequestPsnFundRiskEvaluationQueryResult();
                            } else if (getString(R.string.finc_myfinc_button_scheduledsell).equals(selectors[i])) {
                                //定期定额赎回
                                fincControl.tradeFundDetails = fundBalanceMap;
                                Intent intent = new Intent(MyFincBalanceDetailActivity.this,
                                        MyFincScheduSellActivity.class);
                                startActivityForResult(intent, 1);
                            } else if (getString(R.string.finc_myfinc_button_give).equals(selectors[i])) {
                                //基金转换
                                fincControl.tradeFundDetails = fundBalanceMap;
                                Intent intent = new Intent(MyFincBalanceDetailActivity.this,
                                        FincTradeThrowActivity.class);
                                startActivityForResult(intent, 1);
                            } else if (getString(R.string.finc_myfinc_button_bonusType).equals(selectors[i])) {
                                //分红方式
                                fincControl.tradeFundDetails = fincControl.fundDetails;
                                Intent intent = new Intent(
                                        MyFincBalanceDetailActivity.this,
                                        MyFincBoundsTypeSubmitActivity.class);
                                intent.putExtra(Finc.FINC_FUNDCODE_REQ, foundCode);
                                intent.putExtra(Finc.FINC_FUNDNAME_REQ, foundName);
                                intent.putExtra(Finc.FINC_BOUNDSTYPE_REQ, bonusType);
                                startActivityForResult(intent, 1);
                            } else if ("净值走势图".equals(selectors[i])) {
                                //净值走势图
                                requestSystemDateTime();
                                BiiHttpEngine.showProgressDialog();
                            }
                        }
                    }
                });
            } else {
                PopupWindowUtils.getInstance().setshowMoreChooseUpListener(
                        this, btn2, selectors, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Integer tag = (Integer) v.getTag();
                                String selector = selectors[tag];
                                //TODO 执行点击事件，抽离重复代码
                                executeClickCommand(getBtnMap().get(selector));
                            }
                        });
            }
        }

    }

    @Override
    public void requestSystemDateTimeCallBack(Object resultObj) {
        super.requestSystemDateTimeCallBack(resultObj);
        String currentTime = QueryDateUtils.getcurrentDate(dateTime);
        getKChartDate(foundCode, currentTime);
    }

    @Override
    public void getKChartDateCallback(Object resultObj) {
        super.getKChartDateCallback(resultObj);
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


    private String[] getSelectors(String[] strs) {
        if (strs.length == 0)
            return new String[0];

        String[] resultStrs = new String[strs.length - 1];
        for (int i = 0; i < strs.length - 1; i++) {
            resultStrs[i] = strs[i + 1];
        }
        return resultStrs;
    }

    //    根据返回字段添加String[]中
    private String[] getSelectors(Map<String, String> fundInfoMap) {
        String[] selectors = new String[getBtnMap().size()];
        isShareRed = StringUtil.parseStrToBoolean(fundInfoMap
                .get(Finc.ISSHARERED));
        isSale = StringUtil.parseStrToBoolean(fundInfoMap.get(Finc.ISSALE));
        isFastSale = StringUtil.parseStrToBoolean(fundInfoMap.get(Finc.ISFASTSALE));
        isAddSale = StringUtil.parseStrToBoolean(fundInfoMap
                .get(Finc.ISADDSALE));
        isChangeOut = StringUtil.parseStrToBoolean(fundInfoMap
                .get(Finc.ISCHANGEOUT));
        int i = 0;
        if (FincControl.myfincOperationFlag == FincControl.FINCINFO) {
            if (isSale) {
                selectors[i] = getString(R.string.finc_myfinc_button_sell);
                i += 1;
            }
            /**P601添加 追加购买*/
            selectors[i] = getString(R.string.finc_myfinc_button_add_buy);
            i += 1;
//            if (isFastSale) {//是否允许快速赎回isFastSale
//                selectors[i] = getString(R.string.finc_myfinc_button_fastsell);
//                i += 1;
//            }
            if (isAddSale) {
                selectors[i] = getString(R.string.finc_myfinc_button_scheduledsell);
                i += 1;
            }
            if (isShareRed) {
                selectors[i] = getString(R.string.finc_myfinc_button_bonusType);
                i += 1;
            }
            if (isChangeOut) {
                selectors[i] = getString(R.string.finc_myfinc_button_give);
                i += 1;
            }
        }

        //		if(FincControl.myfincOperationFlag == FincControl.FINCTRANS){
        //			if (isChangeOut) {
        //				selectors[i] = getString(R.string.finc_myfinc_button_give);
        //				i += 1;
        //			}
        //		}
        //		if(FincControl.myfincOperationFlag == FincControl.SETBOUNDS){
        //			if (isShareRed) {
        //				selectors[i] = getString(R.string.finc_myfinc_button_bonusType);
        //				i += 1;
        //			}
        //		}
        //TODO 屏蔽基金走势图
        //		selectors[i] = getString(R.string.finc_valuechart);


        //		String[] result = new String[i + 1];
        String[] result = new String[i];
        //		for (int j = 0; j <= i; j++) {
        for (int j = 0; j < i; j++) {
            result[j] = selectors[j];
        }
        return result;
    }


    /**
     * 执行点击事件，抽离重复代码
     *
     * @param value 判断条件
     */
    private void executeClickCommand(int value) {
        // TODO 添加更多按钮点击事件
        Intent intent;
        switch (value) {
            case 7:
                /**追加购买 P601新增*/
                /**基金买入，判断是否未风险评估*/
                BaseHttpEngine.showProgressDialog();
                doCheckRequestPsnFundRiskEvaluationQueryResult();
                break;
            case 1:
                // 赎回
                fincControl.tradeFundDetails = fincControl.fundDetails;
                intent = new Intent(MyFincBalanceDetailActivity.this,
                        MyFincSellSubmitActivity.class);
                startActivityForResult(intent, 1);
                break;
            case 6:
                // 快速赎回
                fincControl.tradeFundDetails = fincControl.fundDetails;
                intent = new Intent(MyFincBalanceDetailActivity.this,
                        MyFincFastSellSubmitActivity.class);
                startActivityForResult(intent, 1);
                break;
            case 2:
                // 定期定额赎回
                fincControl.tradeFundDetails = fundBalanceMap;
                intent = new Intent(MyFincBalanceDetailActivity.this,
                        MyFincScheduSellActivity.class);
                startActivityForResult(intent, 1);
                break;
            case 3:
                // 基金转换
                fincControl.tradeFundDetails = fundBalanceMap;
                intent = new Intent(MyFincBalanceDetailActivity.this,
                        FincTradeThrowActivity.class);
                startActivityForResult(intent, 1);
                break;
            case 4:
                // 分红方式
                fincControl.tradeFundDetails = fincControl.fundDetails;
                intent = new Intent(
                        MyFincBalanceDetailActivity.this,
                        MyFincBoundsTypeSubmitActivity.class);
                intent.putExtra(Finc.FINC_FUNDCODE_REQ, foundCode);
                intent.putExtra(Finc.FINC_FUNDNAME_REQ, foundName);
                intent.putExtra(Finc.FINC_BOUNDSTYPE_REQ, bonusType);
                startActivityForResult(intent, 1);
                break;
            case 5:
                //TODO 基金走势图 (后台没有数据，暂时屏蔽)
                requestSystemDateTime();
                BiiHttpEngine.showProgressDialog();
                break;


            default:
                break;
        }
    }

    /**
     * 所有按钮
     *
     * @return
     */
    private Map<String, Integer> getBtnMap() {
        Map<String, Integer> map = new HashMap<String, Integer>() {
            {
                // TODO 添加更多按钮
                put(getString(R.string.finc_myfinc_button_sell), 1);
                put(getString(R.string.finc_myfinc_button_add_buy), 7);
//                put(getString(R.string.finc_myfinc_button_fastsell), 6);
                put(getString(R.string.finc_myfinc_button_scheduledsell), 2);
                put(getString(R.string.finc_myfinc_button_give), 3);
                put(getString(R.string.finc_myfinc_button_bonusType), 4);
                put(getString(R.string.finc_valuechart), 5);
            }
        };
        return map;

    }

    /**
     * 检查是否做了风险认证的回调处理
     *
     * @param resultObj
     */
    public void doCheckRequestPsnFundRiskEvaluationQueryResultCallback(
            Object resultObj) {
        super.doCheckRequestPsnFundRiskEvaluationQueryResultCallback(resultObj);
        if (fincControl.ifdorisk) {
            fincControl.tradeFundDetails = fincControl.fincFundBasicDetails;
            Intent intent = new Intent();
            intent.setClass(this, FincTradeBuyActivity.class);
            intent.putExtra(Finc.I_ATTENTIONFLAG, 2);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                if (requestCode == InvestConstant.FUNDRISK) {// 基金风险评估
                    fincControl.ifdorisk = true;
                    fincControl.tradeFundDetails = fincControl.fincFundBasicDetails;
                    Intent intent = new Intent();
                    intent.setClass(this, FincTradeBuyActivity.class);
                    intent.putExtra(Finc.I_ATTENTIONFLAG, 2);
                    startActivityForResult(intent, 1);
                    break;
                }
                setResult(RESULT_OK);
                finish();
                break;

            default:
                if (requestCode == InvestConstant.FUNDRISK) {// 基金风险评估
                    fincControl.ifdorisk = false;
                    getPopupForRisk();
                    break;
                }
                break;
        }
    }
}
