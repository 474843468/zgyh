package com.chinamworld.bocmbci.biz.finc.trade;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.biz.finc.FincUtils;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.biz.finc.control.entity.FundCompany;
import com.chinamworld.bocmbci.biz.finc.fundprice.FundFundCompanyActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.ChineseCharToEn;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 基金买入页面
 *
 * @author xyl
 */
public class FincTradeBuyActivity extends FincBaseActivity implements
        OnItemSelectedListener {
    private static final String TAG = "FincTradeBuyActivity";

    /**
     * 基金公司下拉列表
     */
    private TextView fundCompanySpinner;
    private int fundCompanyPos = 0;
    private int oldPosition = 0;

    /**
     * 基金代码下拉列表
     */
    private Spinner fundCodeSpinner;

    /**
     * spinner 基金代码/名称 文本显示
     */
    private TextView CodeTextSpinner;

    /**
     * 基金公司sipnner 是否被点击
     */
    private boolean isClickCompany = false;

    /**
     * 基金代码/名称 sipnner 是否被点击
     */
    private boolean isClickCode = false;

    //第一次初始化的时候  避免显示spinner的值
    private boolean firstInit = false;

    // 详情中买入
    private TextView fundCompanyTextView;
    private TextView fundCodeTextView;
    private TextView fundNameTextView;
    private TextView netPriceTextView;
    private TextView productRiskLevelTextView;
    private TextView feeTypeTextView;
    private TextView orderLowLimitTextView;
    private TextView applyLowLimitTextView;
    private TextView tradeCurrencyTextView;
    private TextView accBalanceTextView;

    private LinearLayout fundCodell;
    private LinearLayout fundNamell;
    private LinearLayout fundCodeSpll;
    private RelativeLayout fincFundCompanySpinnerLayout;
    /**
     * 买入金额 输入框
     */
    private EditText buyAmountEditText;
    /**
     * 下一步按钮
     */
    private Button nextBtn;
    private Button extrDayDelBtn;
    // 页面显示的值
    private String fundCompanyStr;
    private String fundCompanyCodeStr;
    private String fundCodeStr;
    private String fundNameStr;
    private String netPriceStr;
    private String productRiskLevelStr;
    private String feeTypeStr;
    private String orderLowLimitStr;
    private String applyLowLimitStr;
    private String tradeCurrencyStr, cashFlagCode;
    private String accBalanceStr;
    private String fundStateStr;
    //基金类型
    private String fntype;
    //基金类型（是否是短期理财）
    private String isShortFund;
    //判定指定日期执行类型
    private String sizinvt = null;
    /**
     * 买入金额
     */
    private String buyAmoundStr;
    /**
     * 单日购买上限金额
     */
    private TextView DayMaxSumBuyTextView;

    /**
     * 单日购买上限layout
     */
    private View finc_daylimit_layout;


    /**
     * 根据基金代码查询的详情
     */
    private List<Map<String, Object>> fundList;
    private List<String> fundCodeStrList;
    private List<String> fundNameStrList;
    private ArrayList<String> showFundList;
    /**
     * 基金公司名称集合
     */
    private ArrayList<FundCompany> companyList = new ArrayList<FundCompany>();

    private int flag;

    /**
     * 快速交易
     */
    private static final int TURNOVER = 0;
    /**
     * 详情交易
     */
    private static final int TRADE = 1;

    private String canBuy;
    private String mDayMaxSumBuy = null;
    /**
     * 单日购买上限金额
     */

    private Map<String, Object> qccAccBalanceMap;
    private List<Map<String, String>> accBalanceList;
    private boolean isExtrDayDeal;// 是否是指定日期买入 true:指定日期买入 false:买入


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initData();
        switch (flag) {
            case TURNOVER:
                BaseHttpEngine.showProgressDialogCanGoBack();
                getFundCompanyList();
                break;
            case TRADE:
                if (fincControl.accDetailsMap != null) {
                    if (fincControl.accDetailsMap.get(Finc.FINC_ACCOUNTTYPE)
                            .equals(ConstantGloble.ACC_ACTYPEGRCAS)) {// 长信城用卡用QCC
                        // 中银2里面
                        BaseHttpEngine.showProgressDialogCanGoBack();
                        queryQccBanlance(fincControl.accId);
                    } else {
                        BaseHttpEngine.showProgressDialogCanGoBack();
                        requestPsnAccountQueryAccountDetail(fincControl.accId);
                    }
                }
                break;

            default:
                break;
        }

    }

    /**
     * 设定账户余额
     */
    @SuppressWarnings("unchecked")
    private void setAccBalance(String currencyCode) {
        if (fincControl.accDetailsMap != null) {
            if (fincControl.accDetailsMap.get(Finc.FINC_ACCOUNTTYPE).equals(
                    ConstantGloble.ACC_ACTYPEGRCAS)) {
                if (StringUtil.isNullOrEmpty(qccAccBalanceMap)) {
                    BaseHttpEngine.showProgressDialogCanGoBack();
                    queryQccBanlance(fincControl.accId);
                } else {// QCC 暂时只是用第一币种 没有炒汇
                    BaseHttpEngine.dissMissProgressDialog();
                    // 第一币种
                    Map<String, Object> infoA = (Map<String, Object>) qccAccBalanceMap
                            .get(Finc.FINC_QUERYBANCE_QCCBALANCEA);
                    Map<String, String> currencyInfoA = (Map<String, String>) infoA
                            .get(Finc.FINC_QUERYBANCE_CURRENCY);
                    // 第二币种
                    Map<String, Object> infoB = (Map<String, Object>) qccAccBalanceMap
                            .get(Finc.FINC_QUERYBANCE_QCCBALANCEB);
                    Map<String, String> currencyInfoB = (Map<String, String>) infoA
                            .get(Finc.FINC_QUERYBANCE_CURRENCY);
                    if (currencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) {
                        if (currencyCode.equals(currencyInfoA
                                .get(Finc.FINC_QUERYBANCE_CODE))) {
                            accBalanceStr = String.valueOf(infoA
                                    .get(Finc.FINC_QUERYBANCE_CURRENTBALANCE));
                            accBalanceTextView.setText(StringUtil
                                    .parseStringCodePattern(tradeCurrencyStr,
                                            accBalanceStr, 2));
                        } else if (currencyCode.equals(currencyInfoB
                                .get(Finc.FINC_QUERYBANCE_CODE))) {
                            accBalanceStr = String.valueOf(infoB
                                    .get(Finc.FINC_QUERYBANCE_CURRENTBALANCE));
                            accBalanceTextView.setText(StringUtil
                                    .parseStringCodePattern(tradeCurrencyStr,
                                            accBalanceStr, 2));
                        } else {
                            accBalanceStr = ConstantGloble.ZERO;
                            accBalanceTextView.setText(StringUtil
                                    .parseStringCodePattern(tradeCurrencyStr,
                                            accBalanceStr, 2));
                        }
                    } else {// 外币
                        String cashRemit;
                        cashRemit = currencyInfoA
                                .get(Finc.FINC_QUERYBANCE_CASHREMIT);
                        if (currencyCode.equals(currencyInfoA
                                .get(Finc.FINC_QUERYBANCE_CODE))
                                && LocalData.CurrencyCashremit
                                .get(cashFlagCode).equals(
                                        LocalData.CurrencyCashremit
                                                .get(cashRemit))) {
                            accBalanceStr = String.valueOf(infoA
                                    .get(Finc.FINC_QUERYBANCE_CURRENTBALANCE));
                            accBalanceTextView.setText(StringUtil
                                    .parseStringCodePattern(tradeCurrencyStr,
                                            accBalanceStr, 2));
                            return;
                        }
                        cashRemit = currencyInfoB
                                .get(Finc.FINC_QUERYBANCE_CASHREMIT);
                        if (currencyCode.equals(currencyInfoB
                                .get(Finc.FINC_QUERYBANCE_CODE))
                                && LocalData.CurrencyCashremit
                                .get(cashFlagCode).equals(
                                        LocalData.CurrencyCashremit
                                                .get(cashRemit))) {
                            accBalanceStr = String.valueOf(infoB
                                    .get(Finc.FINC_QUERYBANCE_CURRENTBALANCE));
                            accBalanceTextView.setText(StringUtil
                                    .parseStringCodePattern(tradeCurrencyStr,
                                            accBalanceStr, 2));
                            return;
                        }
                        accBalanceStr = ConstantGloble.ZERO;
                        accBalanceTextView.setText(StringUtil
                                .parseStringCodePattern(tradeCurrencyStr,
                                        accBalanceStr, 2));
                    }
                }

            } else {
                if (StringUtil.isNullOrEmpty(accBalanceList)) {
                    requestPsnAccountQueryAccountDetail(fincControl.accId);
                } else {
                    BaseHttpEngine.dissMissProgressDialog();
                    boolean tempTag = false; // 判断是否有该币种的 余额
                    for (Map<String, String> detailmap : accBalanceList) {
                        if (detailmap.get(Finc.FINC_CURRENCYCODE_RES).equals(
                                tradeCurrencyStr)) {
                            if (tradeCurrencyStr
                                    .equals(ConstantGloble.PRMS_CODE_RMB)) {// 人民币
                                accBalanceStr = detailmap
                                        .get(Finc.FINC_AVAILABLEBALANCE_RES);
                                tempTag = true;
                            } else {
                                String cashRimit = detailmap
                                        .get(Finc.FINC_CASEREMIT_RES);
                                if (LocalData.CurrencyCashremit.get(
                                        cashFlagCode).equals(
                                        LocalData.CurrencyCashremit
                                                .get(cashRimit))) {
                                    accBalanceStr = detailmap
                                            .get(Finc.FINC_AVAILABLEBALANCE_RES);
                                    tempTag = true;
                                }
                            }

                        }
                    }
                    if (!tempTag) {// 如果没有该币种的余额
                        accBalanceStr = ConstantGloble.ZERO;
                    }
                    // accBalanceStr = !StringUtil.isNullOrEmpty(accBalanceStr)
                    // ?
                    // accBalanceStr
                    // : "0";
                    accBalanceTextView.setText(StringUtil
                            .parseStringCodePattern(tradeCurrencyStr,
                                    accBalanceStr, 2));
                    // accBalanceTextView.setText(StringUtil.parseStringPattern(
                    // accBalanceStr, 2));
                }

            }
        }
    }

    @Override
    public void requestPsnAccountQueryAccountDetailCallback(Object resultObj) {
        super.requestPsnAccountQueryAccountDetailCallback(resultObj);
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        Map<String, Object> map = (Map<String, Object>) biiResponseBody
                .getResult();
        accBalanceList = (List<Map<String, String>>) map
                .get(Finc.FINC_ACCOUNTDETAILLIST_RES);
        if (StringUtil.isNullOrEmpty(accBalanceList)) {
            BaseHttpEngine.dissMissProgressDialog();
            return;
        }
        setAccBalance(tradeCurrencyStr);
        // boolean tempTag = false; // 判断是否有该币种的 余额
        // for (Map<String, String> detailmap : list) {
        // if (detailmap.get(Finc.FINC_CURRENCYCODE_RES).equals(
        // tradeCurrencyStr)) {
        // accBalanceStr = detailmap.get(Finc.FINC_AVAILABLEBALANCE_RES);
        // tempTag = true;
        // }
        // }
        // if (!tempTag) {// 如果没有该币种的余额
        // accBalanceStr = "0";
        // }
        // accBalanceStr = !StringUtil.isNullOrEmpty(accBalanceStr) ?
        // accBalanceStr
        // : "0";
        // accBalanceTextView.setText(StringUtil.parseStringPattern(accBalanceStr,
        // 2));

    }

    @Override
    public void getFundCompanyListCallback(Object resultObj) {
        super.getFundCompanyListCallback(resultObj);
        BaseHttpEngine.dissMissProgressDialog();
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        fincControl.fundCompanyList = (List<Map<String, String>>) biiResponseBody
                .getResult();
        for (Map<String, String> map : fincControl.fundCompanyList) {
            String companyName = map.get(Finc.FINC_GETFUNDCOMPANCYLIST_FUNDCOMPANYNAME);
            // 为基金公司下拉列表赋值
            FundCompany fundCompany = new FundCompany();
            fundCompany.setFundCompanyName(companyName);
            fundCompany.setFundCompanyCode(map
                    .get(Finc.FINC_GETFUNDCOMPANCYLIST_FUNDCOMPANYCODE));
            fundCompany.setChecked(false);
            fundCompany.setAlpha(ChineseCharToEn.cn2py(companyName));
            companyList.add(fundCompany);

        }
        Collections.sort(companyList, new Comparator<FundCompany>() {

            public int compare(FundCompany o1, FundCompany o2) {
                //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
                if (o2.getAlpha().equals("#")) {
                    return -1;
                } else if (o1.getAlpha().equals("#")) {
                    return 1;
                } else {
                    return o1.getAlpha().compareTo(o2.getAlpha());
                }
            }
        });
        initFundCompany();

    }

    /**
     * 查询账户余额 并显示
     */
    @Override
    public void queryQccBanlanceCallback(Object resultObj) {
        super.queryQccBanlanceCallback(resultObj);
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        qccAccBalanceMap = (Map<String, Object>) biiResponseBody.getResult();
        if (qccAccBalanceMap == null) {
            BaseHttpEngine.dissMissProgressDialog();
            queryQccBanlance(fincControl.accId);
        } else {
            // if (tradeCurrencyStr.equals(ConstantGloble.PRMS_CODE_RMB)) {
            // accBalanceStr = String.valueOf(result
            // .get(Finc.FINC_QUERYBANCE_CURRENTBALANCE));
            // accBalanceTextView.setText(StringUtil.parseStringPattern(
            // accBalanceStr, 2));
            // } else {
            // accBalanceStr = "0";
            // accBalanceTextView.setText(StringUtil.parseStringPattern(
            // accBalanceStr, 2));
            // }
            setAccBalance(tradeCurrencyStr);

        }
        // switch (flag) {
        // case TRADE:
        // BaseHttpEngine.dissMissProgressDialog();
        // break;
        // case TURNOVER:
        // getFundCompanyList();
        // break;
        // default:
        // break;
        // }

    }

    @Override
    public void queryfundDetailByFundComanyCodeCallback(Object resultObj) {
        super.queryfundDetailByFundComanyCodeCallback(resultObj);
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        fundList = (List<Map<String, Object>>) biiResponseBody.getResult();
        BaseHttpEngine.dissMissProgressDialog();

        if (StringUtil.isNullOrEmpty(fundList)) {
            fundCompanySpinner.setText("请选择");
            isClickCompany = false;
            BaseDroidApp.getInstanse().showInfoMessageDialog("该公司暂无基金产品在售，请选择其他公司产品。");
            return;
        }
        //		if (!StringUtil.isNullOrEmpty(fundList)) {// 如果不为空
        //			fundCodeStrList = new ArrayList<String>();
        //			fundNameStrList = new ArrayList<String>();
        //			showFundList = new ArrayList<String>();
        //			for (Map<String, Object> map : fundList) {
        //				showFundList.add((String) map.get(Finc.FINC_FUNDNAME) + "/"
        //						+ (String) map.get(Finc.FINC_FUNDCODE));
        //				fundNameStrList.add((String) map.get(Finc.FINC_FUNDNAME));
        //				fundCodeStrList.add((String) map.get(Finc.FINC_FUNDCODE));
        //			}
        //			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        //					R.layout.dept_spinner, showFundList);
        //			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //			fundCodeSpinner.setAdapter(adapter);
        //			oldPosition = newPosition;
        //		} else {
        //			fundCompanySpinner.setSelection(oldPosition);
        //			BaseDroidApp.getInstanse().showInfoMessageDialog("该基金公司没有基金!");
        //			return;
        //		}

    }

    /**
     * 初始化布局
     *
     * @Author xyl
     */
    private void init() {
        setRightToMainHome();
        View childview = mainInflater.inflate(R.layout.finc_trade_buy_main,
                null);
        tabcontent.addView(childview);
        StepTitleUtils.getInstance().initTitldStep(this,
                fincControl.getStepsFortradeBuy());
        StepTitleUtils.getInstance().setTitleStep(1);
        setTitle(R.string.finc_title_buy);
        fundCompanyTextView = (TextView) childview
                .findViewById(R.id.finc_fundcompany_textView);
        //单位净值
        netPriceTextView = (TextView) childview
                .findViewById(R.id.finc_netvalue_textView);
        //产品风险级别
        productRiskLevelTextView = (TextView) childview
                .findViewById(R.id.finc_productrisklevel_textView);
        //收费方式
        feeTypeTextView = (TextView) childview
                .findViewById(R.id.finc_feetype_textView);
        //认购下限
        orderLowLimitTextView = (TextView) childview
                .findViewById(R.id.finc_rebuyLowLimit_textView);
        //申购下限
        applyLowLimitTextView = (TextView) childview
                .findViewById(R.id.finc_shenbuyLowLimit_textView);
        //单日购买上限
        DayMaxSumBuyTextView = (TextView) childview
                .findViewById(R.id.finc_daylimit);
        //交易币种
        tradeCurrencyTextView = (TextView) childview
                .findViewById(R.id.finc_tradecurrency_textView);
        //资金账户余额
        accBalanceTextView = (TextView) childview
                .findViewById(R.id.finc_accbalance_textView);
        fundCompanySpinner = (TextView) childview
                .findViewById(R.id.finc_fundcompany_spinner);
        fundCodeSpinner = (Spinner) childview
                .findViewById(R.id.finc_fundnameandcode_spinner);
        fundCodeTextView = (TextView) childview
                .findViewById(R.id.finc_fundCode_textview);
        fundNameTextView = (TextView) childview
                .findViewById(R.id.finc_fundName_textview);
        buyAmountEditText = (EditText) childview
                .findViewById(R.id.finc_buyamount_editText);
        CodeTextSpinner = (TextView) childview
                .findViewById(R.id.code_text_spinner);
        nextBtn = (Button) childview.findViewById(R.id.finc_next);
        extrDayDelBtn = (Button) childview
                .findViewById(R.id.finc_extrDayDeal_btn);
        fundCodell = (LinearLayout) findViewById(R.id.finc_fundCode_ll);
        fundNamell = (LinearLayout) findViewById(R.id.finc_fundName_ll);
        fundCodeSpll = (LinearLayout) findViewById(R.id.finc_fundnameandcode_ll);
        fincFundCompanySpinnerLayout = (RelativeLayout) findViewById(R.id.finc_fundcompany_spinner_layout);
        finc_daylimit_layout = findViewById(R.id.finc_daylimit_layout);

        // 增加资金帐户余额浮动提示框
        PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
                accBalanceTextView);

        PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
                DayMaxSumBuyTextView);

        initRightBtnForMain();
    }

    /**
     * 初始化数据
     *
     * @Author xyl
     */
    private void initData() {
        if (StringUtil.isNullOrEmpty(fincControl.tradeFundDetails)) {// 快速交易中的买入
            fastTradeInit();
            flag = TURNOVER;
        } else {// 详情页面中的买入
            detailInit();
            flag = TRADE;
            Map<String, Object> map = fincControl.tradeFundDetails;
            fundCompanyStr = (String) map.get(Finc.FINC_FUNDCOMPANYNAME);
            fundCompanyCodeStr = (String) map.get(Finc.FINC_FUNDCOMPANYCODE);
            fundCodeStr = (String) map.get(Finc.FINC_FUNDCODE);
            fundNameStr = (String) map.get(Finc.FINC_FUNDNAME);
            netPriceStr = (String) map.get(Finc.FINC_NETPRICE);
            productRiskLevelStr = (String) map.get(Finc.FINC_RISKLV);
            feeTypeStr = (String) map.get(Finc.FINC_FEETYPE);// 收费方式.
            fundStateStr = (String) map.get(Finc.FINC_FUNDSTATE);
            canBuy = String.valueOf(map.get(Finc.CANBUY));
            tradeCurrencyStr = (String) map.get(Finc.FINC_CURRENCY);
            StringUtil.setInPutValueByCurrency(buyAmountEditText,
                    tradeCurrencyStr);
            cashFlagCode = (String) map.get(Finc.FINC_CASHFLAG);
            orderLowLimitStr = (String) map.get(Finc.FINC_ORDERLOWLIMIT);
            applyLowLimitStr = (String) map.get(Finc.FINC_APPLYLOWLIMIT);
            fntype = (String) fincControl.fundDetails
                    .get(Finc.FINC_FNTYPE);
            isShortFund = (String) fincControl.fundDetails
                    .get(Finc.FINC_IS_SHORT_FUND);
            // if (fundStateStr.equals(ConstantGloble.FINC_FUNDSTATE_1)) {// 认购期
            // } else if (fundStateStr.equals(ConstantGloble.FINC_FUNDSTATE_0))
            // {// 申购期
            // // 状态判断不全TODO
            // } else {
            // orderOrApplyLowLimitStr = "-";
            // }
            fundCompanyTextView.setText(fundCompanyStr);// 只有详情中进入才显示
            fundNameTextView.setText(fundNameStr); // 只有详情中详情中进入才显示
            fundCodeTextView.setText(fundCodeStr);
            netPriceTextView.setText(StringUtil.parseStringPattern(netPriceStr,
                    4));
            productRiskLevelTextView
                    .setText(LocalData.fincRiskLevelCodeToStrFUND
                            .get(productRiskLevelStr));
            feeTypeTextView.setText(LocalData.fundfeeTypeCodeToStr
                    .get(feeTypeStr));
            orderLowLimitTextView.setText(StringUtil.parseStringCodePattern(
                    tradeCurrencyStr, orderLowLimitStr, 2));
            applyLowLimitTextView.setText(StringUtil.parseStringCodePattern(
                    tradeCurrencyStr, applyLowLimitStr, 2));
            tradeCurrencyTextView.setText(FincControl.fincCurrencyAndCashFlag(
                    tradeCurrencyStr, cashFlagCode));
            // 单日购买上限显示
            if (!StringUtil.isNullOrEmpty(fincControl.tradeFundDetails)) {

                if ("0".equals(fincControl.tradeFundDetails.get(Finc.FINC_DAYMAXSUMBUY))
                        || StringUtil.isNullOrEmpty(fincControl.tradeFundDetails.get(Finc.FINC_DAYMAXSUMBUY))) {
                    finc_daylimit_layout.setVisibility(View.GONE);
                } else {
                    finc_daylimit_layout.setVisibility(View.VISIBLE);
                    mDayMaxSumBuy = StringUtil.parseStringPattern(
                            (String) fincControl.tradeFundDetails
                                    .get(Finc.FINC_DAYMAXSUMBUY), 2);
                    DayMaxSumBuyTextView.setText(mDayMaxSumBuy);
                }
            }
            // 详情页面 进入此页面不需要显示 spinner选项
            fincFundCompanySpinnerLayout.setVisibility(View.GONE);

            initBtn((String) map.get(Finc.ISZISINVT), (String) map.get(Finc.ISZISTBY));
            setOcrmTradeAmount();
        }
        CodeTextSpinner.setOnClickListener(SpinnerTextListener);
        PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
                fundNameTextView);
        PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
                fundCompanyTextView);

    }

    private void setOcrmTradeAmount() {
        if (getIntent().getBooleanExtra(Finc.orcmflag, false)) {
            if (!StringUtil.isNullOrEmpty(fincControl.OcrmProductDetailMap)) {
                String transSum = (String) fincControl.OcrmProductDetailMap
                        .get(Finc.TRANSSUM);
                if (tradeCurrencyTextView.getText().toString()
                        .startsWith(Finc.YEN)) {
                    transSum = FincUtils.getYenIntegerStr(transSum);
                }
                buyAmountEditText.setText(StringUtil.valueOf1(transSum));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        buyflag = true;
    }

    /**
     * 买入防止重复点击
     */
    private boolean buyflag = true;

    @Override
    public void fundCompanyInfoQueryCallback(Object resultObj) {
        BaseHttpEngine.dissMissProgressDialog();
        super.fundCompanyInfoQueryCallback(resultObj);
        if (!isExtrDayDeal) {
            Intent intent = new Intent();
            intent.setClass(this, FincTradeBuyConfirmActivity.class);
            intent.putExtra(Finc.I_BUYAMOUNT, buyAmoundStr);
            intent.putExtra(Finc.I_ACCBALANCE, accBalanceStr);
            intent.putExtra(Finc.I_FUNDCODE, fundCodeStr);
            intent.putExtra(Finc.I_FUNDNAME, fundNameStr);
            intent.putExtra(Finc.I_NETPRICE, netPriceStr);
            intent.putExtra(Finc.I_RISKLEVEL, productRiskLevelStr);
            intent.putExtra(Finc.I_FEETYPE, feeTypeStr);
            intent.putExtra(Finc.I_ORDERLOWLIMIT, orderLowLimitStr);
            intent.putExtra(Finc.I_APPLYLOWLIMIT, applyLowLimitStr);
            intent.putExtra(Finc.I_CURRENCYCODE, tradeCurrencyStr);
            intent.putExtra(Finc.I_CASHFLAG, cashFlagCode);
            intent.putExtra(Finc.FINC_GETFUNDCOMPANCYLIST_FUNDCOMPANYCODE, fundCompanyCodeStr);
            intent.putExtra(Finc.I_FUNDSTATE, fundStateStr);// 成功页面显示
            intent.putExtra(Finc.FINC_FNTYPE, fntype);// 基金类型
            intent.putExtra(Finc.FINC_IS_SHORT_FUND, isShortFund);// 基金类型
            intent.putExtra(Finc.FINC_DAYMAXSUMBUY, mDayMaxSumBuy); //单日购买上限金额
            buyflag = false;
            startActivityForResult(intent, 1);
        } else {
            Intent intent = new Intent();
            intent.setClass(this, FincTradeBuyextrDateActivity.class);
            intent.putExtra(Finc.I_BUYAMOUNT, buyAmoundStr);
            intent.putExtra(Finc.I_ACCBALANCE, accBalanceStr);
            intent.putExtra(Finc.I_FUNDCODE, fundCodeStr);
            intent.putExtra(Finc.I_FUNDNAME, fundNameStr);
            intent.putExtra(Finc.I_NETPRICE, netPriceStr);
            intent.putExtra(Finc.I_RISKLEVEL, productRiskLevelStr);
            intent.putExtra(Finc.I_FEETYPE, feeTypeStr);
            intent.putExtra(Finc.I_ORDERLOWLIMIT, orderLowLimitStr);
            intent.putExtra(Finc.I_APPLYLOWLIMIT, applyLowLimitStr);
            intent.putExtra(Finc.I_CURRENCYCODE, tradeCurrencyStr);
            intent.putExtra(Finc.I_CASHFLAG, cashFlagCode);
            intent.putExtra(Finc.ISZISTBY, sizinvt);
            intent.putExtra(Finc.FINC_GETFUNDCOMPANCYLIST_FUNDCOMPANYCODE, fundCompanyCodeStr);
            intent.putExtra(Finc.I_FUNDSTATE, fundStateStr);// 成功页面显示
            intent.putExtra(Finc.FINC_FNTYPE, fntype);// 基金类型
            intent.putExtra(Finc.FINC_IS_SHORT_FUND, isShortFund);// 基金类型
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                if (requestCode == 101) {
                    fundCompanyPos = data.getIntExtra("fundCompanyPos", 0);
                    if (!isClickCompany)
                        isClickCompany = true;
                    fundCompanySpinner.setText(companyList.get(fundCompanyPos).getFundCompanyName());
                    fundCompanyCodeStr = companyList.get(fundCompanyPos).getFundCompanyCode();
                    CodeTextSpinner.setVisibility(View.VISIBLE);
                    isClickCode = false;
                    BaseHttpEngine.showProgressDialog();
                    initHide();
                    queryfundDetailByFundComanyCode(fundCompanyCodeStr,
                            ConstantGloble.FINC_FILTERFOREX_0);// 查询
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        switch (parent.getId()) {
            case R.id.finc_fundnameandcode_spinner:// 基金代码列表

                Map<String, Object> map = fundList.get(position);// 初始化布局数据
                initBtn((String) map.get(Finc.ISZISINVT), (String) map.get(Finc.ISZISTBY));
                BaseHttpEngine.dissMissProgressDialog();
                if (!firstInit) {
                    firstInit = true;
                    return;
                }

                upDateText(position);
                break;

            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    //根据点击的 基金代码 更新显示界面
    private void upDateText(int position) {
        BaseHttpEngine.showProgressDialog();
        if (!StringUtil.isNullOrEmpty(fundNameStrList)
                && !StringUtil.isNullOrEmpty(fundCodeStrList)) {// 如果列表显示不为空
            fundCodeStr = fundCodeStrList.get(position);
            fundNameStr = fundNameStrList.get(position);
            Map<String, Object> map = fundList.get(position);// 初始化布局数据
            initBtn((String) map.get(Finc.ISZISINVT), (String) map.get(Finc.ISZISTBY));
            netPriceStr = (String) map.get(Finc.FINC_NETPRICE);
            productRiskLevelStr = (String) map.get(Finc.FINC_RISKLV);
            canBuy = String.valueOf(map.get(Finc.CANBUY));
            feeTypeStr = (String) map.get(Finc.FINC_FEETYPE);
            fundStateStr = (String) map.get(Finc.FINC_FUNDSTATE);
            orderLowLimitStr = (String) map.get(Finc.FINC_ORDERLOWLIMIT);
            applyLowLimitStr = (String) map.get(Finc.FINC_APPLYLOWLIMIT);
            tradeCurrencyStr = (String) map.get(Finc.FINC_CURRENCY);
            StringUtil.setInPutValueByCurrency(buyAmountEditText,
                    tradeCurrencyStr);
            cashFlagCode = (String) map.get(Finc.FINC_CASHFLAG);

            fundCodeTextView.setText(fundNameStr + "/" + fundCodeStr);
            netPriceTextView.setText(StringUtil.parseStringPattern(
                    netPriceStr, 4));
            productRiskLevelTextView
                    .setText(LocalData.fincRiskLevelCodeToStrFUND
                            .get(productRiskLevelStr));
            feeTypeTextView.setText(LocalData.fundfeeTypeCodeToStr
                    .get(feeTypeStr));
            applyLowLimitTextView.setText(StringUtil.parseStringPattern(
                    applyLowLimitStr, 2));
            orderLowLimitTextView.setText(StringUtil.parseStringPattern(
                    orderLowLimitStr, 2));
            tradeCurrencyTextView
                    .setText(FincControl.fincCurrencyAndCashFlag(
                            tradeCurrencyStr, cashFlagCode));
            setAccBalance(tradeCurrencyStr);

            String fundCode = (String) map.get(Finc.FINC_FUNDCODE);
            //通过基金代码获取单日购买上限
            getFincFund(fundCode);

        }

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && isClickCode &&
                fundCodeSpinner.getSelectedItemPosition() == 0 && mDayMaxSumBuy == null) {
            upDateText(0);

        }
    }

    /**
     * 基金基本信息查询  回调处理
     */
    @Override
    public void getFincFundCallback(Object resultObj) {
        super.getFincFundCallback(resultObj);
        BaseHttpEngine.dissMissProgressDialog();
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        fincControl.fundDetails = (Map<String, Object>) biiResponseBody
                .getResult();
        mDayMaxSumBuy = (String) fincControl.fundDetails.get(Finc.FINC_DAYMAXSUMBUY);
        if (StringUtil.isNull(mDayMaxSumBuy) || "0".equals(mDayMaxSumBuy)) {
            finc_daylimit_layout.setVisibility(View.GONE);
        } else {
            finc_daylimit_layout.setVisibility(View.VISIBLE);
            DayMaxSumBuyTextView.setText
                    (StringUtil.parseStringPattern(mDayMaxSumBuy, 2));
        }

    }

    /**
     * 快速交易进入初始化
     *
     * @Author xyl
     */
    private void fastTradeInit() {
        fundCodeSpll.setVisibility(View.VISIBLE);
        fundCompanyTextView.setVisibility(View.GONE);
        fundCodeSpinner.setOnItemSelectedListener(this);
        fundCodeSpinner.setVisibility(View.VISIBLE);
        fundNamell.setVisibility(View.GONE);
        fundCodell.setVisibility(View.GONE);

        initHide();

    }


    private void initHide() {
        netPriceTextView.setText("-");
        productRiskLevelTextView.setText("-");
        feeTypeTextView.setText("-");
        orderLowLimitTextView.setText("-");
        applyLowLimitTextView.setText("-");
        finc_daylimit_layout.setVisibility(View.GONE);
        tradeCurrencyTextView.setText("-");
        accBalanceTextView.setText("-");

    }

    /**
     * 详情入口初始化
     *
     * @Author xyl
     */
    private void detailInit() {
        fundCompanySpinner.setVisibility(View.GONE);
        fundCompanyTextView.setVisibility(View.VISIBLE);
        fundNamell.setVisibility(View.VISIBLE);
        fundCodell.setVisibility(View.VISIBLE);
        fundCodeSpll.setVisibility(View.GONE);
    }

    /**
     * 根据是否可指定 布局 按钮
     */
    private void initBtn(String isZisInvt, String isZisTby) {
        if ("1".equals(isZisInvt) || "1".equals(isZisTby)) {// 可以指定申购
            ViewUtils.initBtnParamsTwoLeft(nextBtn, this);
            ViewUtils.initBtnParamsTwoRight(extrDayDelBtn, this);
            extrDayDelBtn.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.VISIBLE);
            nextBtn.setText(getString(R.string.finc_today_deal));
            extrDayDelBtn.setText(getString(R.string.finc_extrDay_deal));
            nextBtn.setOnClickListener(nextOnClickListener);
            extrDayDelBtn.setOnClickListener(nextOnClickListener);
            sizinvt = isZisInvt.equals("1") ? isZisInvt : isZisTby;
        } else if ("2".equals(isZisInvt) || "2".equals(isZisTby)) {// 可指定认购
            ViewUtils.initBtnParamsTwoLeft(nextBtn, this);
            ViewUtils.initBtnParamsTwoRight(extrDayDelBtn, this);
            extrDayDelBtn.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.VISIBLE);
            nextBtn.setText(getString(R.string.finc_today_deal));
            extrDayDelBtn.setText(getString(R.string.finc_extrDay_deal));
//            extrDayDelBtn.setPadding(getResources().getDimensionPixelOffset(R.dimen.fill_margin_left),0,0,0);
            nextBtn.setOnClickListener(nextOnClickListener);
            extrDayDelBtn.setOnClickListener(nextOnClickListener);
            sizinvt = isZisInvt.equals("2") ? isZisInvt : isZisTby;
        } else {
//            ViewUtils.initBtnParams(nextBtn, this);
            extrDayDelBtn.setVisibility(View.GONE);
            nextBtn.setVisibility(View.VISIBLE);
            nextBtn.setText(getString(R.string.next));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                //                api>=16
                nextBtn.setBackground(getResources().getDrawable(R.drawable.fund_button_press_state));
                nextBtn.setTextColor(Color.WHITE);
            } else {
//                api<16
             nextBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.fund_button_press_state));
                nextBtn.setTextColor(Color.WHITE);
            }
            nextBtn.setOnClickListener(nextOnClickListener);
        }
    }

    public OnClickListener nextOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (flag == TURNOVER) {
                if (!isClickCompany) {
                    BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_choose_company));
                    return;
                }
                if (!isClickCode) {
                    BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_choose_code));
                    return;
                }
            }
            ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
            buyAmoundStr = StringUtil.trim(buyAmountEditText.getText().toString());
            RegexpBean regexbuyAmoundStr = StringUtil.getRegexBeanByCurrency(getResources().getString(
                    R.string.finc_fundbuynum), buyAmoundStr, tradeCurrencyStr);
            lists.add(regexbuyAmoundStr);
            String dayLimit = null;
            if (flag == TURNOVER) {
                dayLimit = (String) fincControl.fundDetails
                        .get(Finc.FINC_DAYMAXSUMBUY);
            } else if (flag == TRADE) {
                dayLimit = (String) fincControl.tradeFundDetails
                        .get(Finc.FINC_DAYMAXSUMBUY);
            }

            switch (v.getId()) {
                case R.id.finc_next:// 下一步
                    isExtrDayDeal = false;

                    if (RegexpUtils.regexpDate(lists)) {
                        if (!buyflag) {
                            return;
                        }

                        /**
                         * 2014-11-2 dxd 判断单日购买上限额度
                         */
                        if (!StringUtil.isNull(dayLimit) && !"0".equals(dayLimit)) {

                            if (Double.valueOf(buyAmoundStr) > Double
                                    .valueOf(dayLimit)) {
                                BaseDroidApp.getInstanse().showInfoMessageDialog(
                                        getString(R.string.finc_buy_day_limit_error));
                                return;
                            }

                        }

                        // TODO: 2016/10/26 如果账户余额没输数据 提示获取失败重试
                        String money = accBalanceTextView.getText().toString();
                        if(StringUtil.isNullOrEmpty(money)||money.equals("-")){
                            BaseDroidApp.getInstanse().showMessageDialog(
                                    "账户余额获取失败,请重试", new OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            BaseDroidApp.getInstanse().dismissMessageDialog();
                                            BaseHttpEngine.showProgressDialogCanGoBack();
                                            queryQccBanlance(fincControl.accId);
                                        }
                                    });
                            return;
                        }
                        if (Double.valueOf(accBalanceStr) < Double
                                .valueOf(buyAmoundStr)) {
                            BaseDroidApp.getInstanse().showInfoMessageDialog(
                                    getString(R.string.finc_buy_balance_null_error));
                            return;
                        }
                        if (Double.valueOf(buyAmoundStr) < Double
                                .valueOf(orderLowLimitStr)) {
                            BaseDroidApp.getInstanse().showInfoMessageDialog(
                                    getString(R.string.finc_buy_low_limit_error));
                            return;
                        }


                        if (Double.valueOf(buyAmoundStr) < Double
                                .valueOf(applyLowLimitStr)) {
                            BaseDroidApp.getInstanse().showInfoMessageDialog(
                                    getString(R.string.finc_buy_low_limit_error));
                            return;
                        }

                        if (StringUtil.parseStrToBoolean(canBuy)) {
                            BaseHttpEngine.showProgressDialog();
                            requestFundCompanyInfoQuery(fundCodeStr);
                        } else {
                            BaseDroidApp.getInstanse().showInfoMessageDialog(
                                    getString(R.string.finc_canbuy_error));
                        }

                    }
                    break;
                case R.id.finc_extrDayDeal_btn:// 指定日期查询
                    isExtrDayDeal = true;
                    if (RegexpUtils.regexpDate(lists)) {
                        /**
                         * 2014-11-2 dxd 判断单日购买上限额度
                         */
                        if (!StringUtil.isNull(dayLimit) && !"0".equals(dayLimit)) {
                            if (Double.valueOf(buyAmoundStr) > Double
                                    .valueOf(dayLimit)) {
                                BaseDroidApp.getInstanse().showInfoMessageDialog(
                                        getString(R.string.finc_buy_day_limit_error));
                                return;
                            }

                        }
                        if (StringUtil.parseStrToBoolean(canBuy)) {
                            if (Double.valueOf(buyAmoundStr) < Double
                                    .valueOf(orderLowLimitStr)) {
                                BaseDroidApp.getInstanse().showInfoMessageDialog(
                                        getString(R.string.finc_buy_low_limit_error));
                                return;
                            }
                            if (Double.valueOf(buyAmoundStr) < Double
                                    .valueOf(applyLowLimitStr)) {
                                BaseDroidApp.getInstanse().showInfoMessageDialog(
                                        getString(R.string.finc_buy_low_limit_error));
                                return;
                            }
                            BaseHttpEngine.showProgressDialog();
                            requestFundCompanyInfoQuery(fundCodeStr);
                        } else {
                            BaseDroidApp.getInstanse().showInfoMessageDialog(
                                    getString(R.string.finc_canbuy_error));
                        }

                    }
                    break;
                default:
                    break;
            }


        }
    };


    public OnClickListener SpinnerTextListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.code_text_spinner:

                    if ((!isClickCompany) || StringUtil.isNullOrEmpty(fundList)) {
                        BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.finc_choose_company));
                        return;
                    }

                    if (!isClickCode) {
                        isClickCode = true;
                        CodeTextSpinner.setVisibility(View.GONE);
                    }


                    //初始化基金代码spinner
                    if (!StringUtil.isNullOrEmpty(fundList)) {// 如果不为空
                        fundCodeStrList = new ArrayList<String>();
                        fundNameStrList = new ArrayList<String>();
                        showFundList = new ArrayList<String>();
                        for (Map<String, Object> map : fundList) {
                            showFundList.add((String) map.get(Finc.FINC_FUNDNAME) + "/"
                                    + (String) map.get(Finc.FINC_FUNDCODE));
                            fundNameStrList.add((String) map.get(Finc.FINC_FUNDNAME));
                            fundCodeStrList.add((String) map.get(Finc.FINC_FUNDCODE));
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(FincTradeBuyActivity.this, R.layout.dept_spinner, showFundList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        fundCodeSpinner.setAdapter(adapter);
                        oldPosition = fundCompanyPos;
                    }

                    fundCodeSpinner.performClick();
                    break;
                default:
                    break;
            }
        }
    };

    /***
     * 基金公司
     */
    private void initFundCompany() {

        FundCompany fundCompany = new FundCompany();
        fundCompany.setFundCompanyName("中银基金管理有限公司");
        fundCompany.setFundCompanyCode("50400000");
        fundCompany.setAlpha("推荐");
        fundCompany.setChecked(false);
        companyList.add(0, fundCompany);

        fundCompany = new FundCompany();
        fundCompany.setFundCompanyName("中银国际证券有限责任公司");
        fundCompany.setFundCompanyCode("13190000");
        fundCompany.setAlpha("推荐");
        fundCompany.setChecked(false);
        companyList.add(1, fundCompany);

        fundCompanySpinner.setText("请选择");
        fundCompanySpinner.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FincTradeBuyActivity.this, FundFundCompanyActivity.class);
                intent.putExtra("flag", "no_all");
                intent.putParcelableArrayListExtra("companyList", companyList);
                startActivityForResult(intent, 101);

            }
        });
    }

}
