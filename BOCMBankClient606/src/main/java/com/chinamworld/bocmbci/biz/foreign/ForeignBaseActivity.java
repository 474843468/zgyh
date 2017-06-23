package com.chinamworld.bocmbci.biz.foreign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.NewStyleBaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Forex;
import com.chinamworld.bocmbci.biz.forex.customer.ForexCustomerRateInfoActivity;
import com.chinamworld.bocmbci.biz.forex.quash.ForexQuashQueryActivity;
import com.chinamworld.bocmbci.biz.forex.rate.ForexAccSettingActivity;
import com.chinamworld.bocmbci.biz.forex.rate.ForexQuickCurrentSubmitActivity;
import com.chinamworld.bocmbci.biz.forex.rate.ForexQuickTradeSubmitActivity;
import com.chinamworld.bocmbci.biz.forex.rate.ForexRateInfoOutlayActivity;
import com.chinamworld.bocmbci.biz.forex.strike.ForexStrikeQueryActivity;
import com.chinamworld.bocmbci.biz.invest.activity.InvesAgreeActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.http.thread.PollingRequestThread;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.server.LocalDataService;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * 外汇基类 @see ForeignBaseActivity
 * @author luqp 2016年9月22日
 */
public class ForeignBaseActivity extends NewStyleBaseActivity {
    public ActivityTaskType getActivityTaskType() {
        return ActivityTaskType.TwoTask;
    }
    /** 请求标志 0-快速交易 1-银行买价 2-银行卖价 */
    public int requestTag = -1;
    /** 点击快速交易还是银行卖价，1-快速交易，2-银行卖价,3-银行买价 */
    public int quickOrRateSell = 1;
    public static final String TAG = "ForexBaseActivity";
    /** isOpen:是否开通投资理财服务*/
    public static boolean isOpen = false;
    /** 是否设置外汇账户 */
    public static boolean isSettingAcc = false;
    /**
     * 判断用户是否设置外汇交易账户
     * @investBindingInfo:交易账户信息
     */
    public Map<String, String> investBindingInfo = null;
    /** 设置账户区域 */
    public View accButtonView = null;
    /** 理财服务区域 */
    public View moneyButtonView = null;
    public View accTextView = null;
    public View moneyTextView = null;
    /** taskPopCloseButton:任务提示框右上角关闭按钮*/
    public ImageView taskPopCloseButton = null;
    /** resultList：存储所有账户信息*/
    public List<Map<String, String>> resultList = null;
    /**
     * 快速交易、我的外汇、成交状况操作条件判断标志,1-快速交易,2-我的外汇，3-成交查询,4-外汇行情,5-我的外汇汇率,6-全部汇率,7-委托查询
     * ，8交易购买(功能位置添加)
     */
    public int taskTag = 1;
    /** 存折册号 */
    public List<String> volumeNumberList = null;
    /** 用于区分是二级菜单还是交易请求开通投资理财，1-交易，2-二级菜单 */
    public int menuOrTrade = 1;
    /** 判断买入币种是否存在 */
    public List<Map<String, String>> tradeBuyCodeResultList = null;
    /** 1-定期，2-活期 调用不同的卖出币种回调方法 **/
    public int tradeConditionFixOrCurr = 1;
    /** 活期账户---卖出币种是否存在 false-不存在 */
    public boolean tradeConditionSellTag = false;
    /** 定期账户---卖出币种是否存在 false-不存在 */
    public boolean tradeConditionFixSellTag = false;
    /** 用于区别是我的外汇汇率还是交易发出的请求1-交易，2-我的外汇汇率 3-全部汇率 */
    public int customerOrTrade = 1;
    /** 登陆后的ConversationId */
    public String commConversationId = null;
    public String tokenId = null;
    /** 活期----卖出币种结果集合 */
    public List<Map<String, Object>> tradeSellCurrResultList = null;
    /** 账户类型 */
    public String accountType = null;
    /** 保留0位小数 */
    public int twoNumber = 0;
    /** 保留2位小数 */
    public int fourNumber = 2;
    /** 用于区分是定期还是活期，1-活期，2-定期 */
    public int currencyOrFixTag = 1;

    /**
     * @用于区别是外汇行情还是我的外汇、成交状况查询
     * @外汇行情不用查询账户类型
     * @1-外汇行情，2-是其余的两个二级菜单
     */
    public int isRateInfo = 1;
    /** 用于区分是任务提示框的重设账户还是我的外汇重设账户，false-任务提示框，true-我的外汇 */
    public boolean isCustomer = false;
    /** 特殊币种 日元、港元 */
    public List<String> spetialCodeList = new ArrayList<String>() {
        private static final long serialVersionUID = 1L;

        {
            add("027");
            add("JPY");
            add("013");
            add("HKD");
        }
    };
    /** 没有小数点的币种 */
    public List<String> codeNoNumberName = new ArrayList<String>() {
        private static final long serialVersionUID = 1L;

        {
            add("日元");
            add("韩元");
            add("越南盾");
        }
    };
    /** 外汇交易，无论买卖币种，含有日元，限价汇率保留2位小数 */
    public List<String> tradeCheckCodeNoNumber = new ArrayList<String>() {
        private static final long serialVersionUID = 1L;

        {
            add("027");
            add("JPY");
        }
    };
    /** 交易金额 */
    // public String moneyEmg = ConstantGloble.FOREX_AMOUNT;
    /** 限价汇率 */
    public String rateEmg = ConstantGloble.FOREX_RATE;
    /** 主视图布局 */
    public LinearLayout tabcontent;// 主Activity显示
    /** 外汇账户是否是否可以作外汇二选一 */
    public String canTwoSided = null;
    /** 1-当前有效，2-历史有效 */
    public int currentOrHistory = 1;
    /** 0-全部货币对，1-用户定制的货币战对 */
    public int allOrCustomerReq = -1;
    /** 用于区别是定期还是活期请求查询买入币种列表信息 1-活期 2-定期 */
    public int fixOrCurrency = -1;
    /** 用户选择的 数据 */
    public Map<String, Object> selectedMap;
    /** 银行买价 目标货币对 买入币种代码 */
    public String sourceCurCde = null;
    /** 银行卖价 目标货币对 卖出币种代码 */
    public String targetCode = null;
    /** 处理前，买入币种代码 */
    public List<String> buyCodeList = null;
    /** 处理后，买入币种名称 */
    public List<String> buyCodeDealList = null;


    /** 涨跌幅升=1、降序=2、不排序=0 标识*/
    public int quoteChangeId = 0;
    /** 查询K线图 时间*/
    public String kTypes = "OK";
    /** 查询趋势图 时间*/
    public String tendencyType = "O";
    public String cardTypes = "F";
    /** 查询R-实盘*/
    public String cardClasss = "R";
    /** 跳转标识 1:交易查询  2:账户管理&我的持仓 3:委托交易查询*/
    public int taskMark = 0;
    /** 是否关闭通讯框*/
    public Boolean isCloseDialog = true;
    /** 点击涨跌幅标识*/
    public Boolean isSorting = false;
    /**v 是否已收藏标识*/
    public Boolean isCollection = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * 判断是否开通投资理财服务
     */
    public void requestPsnInvestmentManageIsOpen() {
        if (menuOrTrade == 2) {
            getHttpTools().requestHttp(Comm.PSNINVESTMENTMANAGEISOPEN_API, "requestMenuIsOpenCallback", null, true);
        } else {
            // 快速交易
            getHttpTools().requestHttp(Comm.PSNINVESTMENTMANAGEISOPEN_API, "requestPsnInvestmentManageIsOpenCallback", null, true);
        }
    }

    /**
     * 判断是否开通投资理财服务---回调
     *
     * @param resultObj
     */
    public void requestPsnInvestmentManageIsOpenCallback(Object resultObj) {
        String isOpenOr = getHttpTools().getResponseResult(resultObj);
        // isOpen
        if ("false".equals(isOpenOr)) {
            isOpen = false;
        } else {
            isOpen = true;
        }
        // TODO-------------------------------------------------------------------------
        // isOpen = false;
    }



    /**
     * 外汇交易账户信息03---回调 交易使用
     *
     * @param resultObj
     *            :返回结果
     */
    @SuppressWarnings("unchecked")
    public void requestPsnForexActIssetCallback(Object resultObj) {
        Map<String, Object> result = getHttpTools().getResponseResult(resultObj);
        if (StringUtil.isNullOrEmpty(result)) {
            BaseHttpEngine.dissMissProgressDialog();
            isSettingAcc = false;
            return;
        } else {
            if (StringUtil.isNullOrEmpty(result.get(Forex.FOREX_RATE_INVESTBINDINGINFO_RES))) {
                isSettingAcc = false;
            } else {
                investBindingInfo = (Map<String, String>) result.get(Forex.FOREX_RATE_INVESTBINDINGINFO_RES);
                BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_RATE_INVESTBINDINGINFO_RES, investBindingInfo);

                if (StringUtil.isNullOrEmpty(investBindingInfo)) {
                    isSettingAcc = false;
                } else {
                    isSettingAcc = true;
                }
            }
        }
        // TODO-----------------------------------------------------------------------------------
        // isSettingAcc = false;
        if (!isOpen || !isSettingAcc) {
            BaseHttpEngine.dissMissProgressDialog();
            getPopup();
            return;
        }
    }

    /**
     * 任务提示框
     */
    public void getPopup() {
        // popupView:任务提示框的布局
        View popupView = LayoutInflater.from(this).inflate(R.layout.forex_task_notify, null);
        taskPopCloseButton = (ImageView) popupView.findViewById(R.id.top_right_close);
        // accButtonView:设置账户按钮
        accButtonView = popupView.findViewById(R.id.forex_acc_button_show);
        // moneyButtonView:理财服务按钮
        moneyButtonView = popupView.findViewById(R.id.forex_money_button_show);
        // accTextView:设置账户文本框
        accTextView = popupView.findViewById(R.id.forex_acc_text_hide);
        // moneyTextView:理财服务文本框
        moneyTextView = popupView.findViewById(R.id.forex_money_text_hide);
        if (isOpen) {
            // 开通投资理财服务
            moneyButtonView.setVisibility(View.GONE);
            moneyTextView.setVisibility(View.VISIBLE);
            if (isSettingAcc) {
                // 用户定义外汇账户
                accButtonView.setVisibility(View.GONE);
                accTextView.setVisibility(View.VISIBLE);

            } else {
                // 用户没有定义外汇账户,必须显示设定账户按钮
                accButtonView.setVisibility(View.VISIBLE);
                accTextView.setVisibility(View.GONE);
                accButtonView.setClickable(true);
                // 外汇账户设定
            }
        } else {
            // 没有开通投资理财服务
            moneyButtonView.setVisibility(View.VISIBLE);
            moneyTextView.setVisibility(View.GONE);
            if (isSettingAcc) {
                // 用户定义外汇账户
                accButtonView.setVisibility(View.GONE);
                accTextView.setVisibility(View.VISIBLE);
            } else {
                // 没有设置账户
                accButtonView.setVisibility(View.VISIBLE);
                accTextView.setVisibility(View.GONE);
            }
        }
        // 设置账户
        accButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    // 查询所有的账户----ConstantGloble.FOREX_RATE_QUICK_ACC
                    BaseHttpEngine.showProgressDialog();
                    isCustomer = false;
                    requestPsnForexActAvai();

                } else {
                    // 没有开通投资理财，不允许设定账户
                    CustomDialog.toastInCenter(BaseDroidApp.getInstanse().getCurrentAct(), BaseDroidApp.getInstanse().getCurrentAct().getString(R.string.bocinvt_task_toast_1));
                }
            }
        });

        // 开通投资理财服务
        moneyButtonView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isOpen) {
                    // 跳转到投资理财服务协议页面
                    Intent gotoIntent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(), InvesAgreeActivity.class);
                    startActivityForResult(gotoIntent, ConstantGloble.ACTIVITY_RESULT_CODE);
                }
            }
        });
        // 右上角的关闭按钮
        taskPopCloseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 强制关闭任务提示框，返回到九宫格
                BaseHttpEngine.dissMissProgressDialog();
                BaseDroidApp.getInstanse().dismissMessageDialogFore();
//				ActivityTaskManager.getInstance().removeAllSecondActivity();
//				Intent intent = new Intent(ForexBaseActivity.this, SecondMainActivity.class);
//				startActivity(intent);
                goToMainActivity();
                finish();
            }
        });
        BaseDroidApp.getInstanse().showForexMessageDialog(popupView);
    }

    /**
     * 第一次进入外汇行情页面 查询用户定制的外汇汇率信息
     */
    public void requestInitPsnCustomerRate() {
        getHttpTools().requestHttp(Forex.FOREX_CUSTOMER_RATE, "requestInitPsnCustomerRateCallback", null, true);
    }

    public void requestInitPsnCustomerRateCallback(Object resultObj) {
    }

    /**
     * 外汇交易账户列表-01----设置外汇账户 查询所有的账户
     */
    public void requestPsnForexActAvai() {
        getHttpTools().requestHttp(Forex.FOREX_PSNFOREXACTAVAI_API, "requestPsnForexActAvaiCallback", null, true);
    }

    /**
     * 外汇交易账户列表----回调---设置外汇账户
     *
     * @param resultObj
     *            :返回结果
     */
    public void requestPsnForexActAvaiCallback(Object resultObj) {
        BaseHttpEngine.dissMissProgressDialog();
        resultList = getHttpTools().getResponseResult(resultObj);
        // 得到result
        if (StringUtil.isNullOrEmpty(resultList)) {
            BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.forex_set_no_acc));
            return;
        }
        BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_ACTAVI_RESULT_KEY, resultList);
        // 跳转到账户设置页面
        Intent intent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(), ForexAccSettingActivity.class);
        startActivityForResult(intent, ConstantGloble.FOREX_RATE_QUICK_ACC);
    }

    /**
     * 账户分活期和定期 查询外汇账户类型--进行外汇买卖 外汇交易使用 我的外汇---活期页面
     */
    public void ratePsnForexActIsset() {
        getHttpTools().requestHttp(Forex.FOREX_RATE_ACTISSET, "ratePsnForexActIssetCallback", null, true);
    }

    /**
     * 查询外汇账户类型---回调 活期或者是定期 外汇买卖交易使用 快速交易
     * @param resultObj:返回结果
     */
    @SuppressWarnings("unchecked")
    public void ratePsnForexActIssetCallback(Object resultObj) {
        Map<String, Object> result = getHttpTools().getResponseResult(resultObj);
        String accountType = null;
        if (StringUtil.isNullOrEmpty(result)) {
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(this.
                    getResources().getString(R.string.forex_rateinfo_sell_codes));
            return;
        } else {
            canTwoSided = (String) result.get(Forex.FOREX_RATE_CANTWOSIDED_RES);
            BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_RATE_CANTWOSIDED_RES, canTwoSided);
            // 不含有InvestBindingInfo键
            if (!result.containsKey(Forex.FOREX_RATE_INVESTBINDINGINFO_RES)) {
                BaseHttpEngine.dissMissProgressDialog();
                BaseDroidApp.getInstanse().showInfoMessageDialog(this.
                        getResources().getString(R.string.forex_rateinfo_sell_codes));
                return;
            }
            Map<String, String> investBindingInfo = (Map<String, String>) result.get(Forex.FOREX_RATE_INVESTBINDINGINFO_RES);
            if (StringUtil.isNullOrEmpty(investBindingInfo)) {
                BaseHttpEngine.dissMissProgressDialog();
                BaseDroidApp.getInstanse().showInfoMessageDialog(this.
                        getResources().getString(R.string.forex_rateinfo_sell_codes));
                return;
            } else {
                String investAccount = investBindingInfo.get(Forex.FOREX_RATE_INVESTACCOUNT_RES);
                // 存储投资账号
                BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_RATE_INVESTACCOUNT_RES, investAccount);
                accountType = investBindingInfo.get(Forex.FOREX_RATE_ACCOUNTTYPE_RES);
                if (StringUtil.isNull(accountType)) {
                    BaseHttpEngine.dissMissProgressDialog();
                    BaseDroidApp.getInstanse().showInfoMessageDialog(this.
                            getResources().getString(R.string.forex_rateinfo_sell_codes));
                    return;
                }
            }
            if (!accountType.equals(ConstantGloble.FOREX_ACCTYPE_DQYBT)) {
                accIsCurr();  // 跳转到活期交易页面
            } else {
                if (result.containsKey(Forex.FOREX_VOLUMENUMBERLIST_RES)) {
                    if (StringUtil.isNullOrEmpty(result.get(Forex.FOREX_VOLUMENUMBERLIST_RES))) {
                        BaseHttpEngine.dissMissProgressDialog();
                        BaseDroidApp.getInstanse().showInfoMessageDialog(this
                                .getResources().getString(R.string.forex_rateinfo_sell_codes));
                        return;
                    } else {
                        volumeNumberList = (List<String>) result.get(Forex.FOREX_VOLUMENUMBERLIST_RES);
                        if (volumeNumberList == null || volumeNumberList.size() <= 0) {
                            BaseHttpEngine.dissMissProgressDialog();
                            BaseDroidApp.getInstanse().showInfoMessageDialog(this.
                                    getResources().getString(R.string.forex_rateinfo_sell_codes));
                            return;
                        } else {
                            BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_VOLUMENUMBERLIST_RES, volumeNumberList);
                        }
                    }
                }
                accIsFix();
            }
        }
    }

    /** 绑定的账户是活期 */
    private void accIsCurr() {
        tradeConditionFixOrCurr = 2;
        fixOrCurrency = 1;
        switch (requestTag) {
            case ConstantGloble.FOREX_QUICK_REQUEST:// 快速交易
                quickOrRateSell = 1;
                break;
            case ConstantGloble.FOREX_BUY_REQUEST:// 银行买价 目标货币对
                quickOrRateSell = 3;
                switch (allOrCustomerReq) {
                    case ConstantGloble.FOREX_ALL:// 0----所有的货币对
                        // 得到买入币种
                        Map<String, Object> map1 = selectedMap;// allRateList.get(buySelectedPosition);
                        sourceCurCde = (String) map1.get(Forex.FOREX_RATE_SOURCECODE_RES);
                        targetCode = (String) map1.get(Forex.FOREX_RATE_TARGETCODE_RES);
                        break;
                    case ConstantGloble.FOREX_CUSTOMER:// 1---用户定制
                        // 得到买入币种
                        Map<String, Object> map2 = selectedMap;// customerRateList.get(buySelectedPosition);
                        sourceCurCde = (String) map2.get(Forex.FOREX_RATE_SOURCECODE_RES);
                        targetCode = (String) map2.get(Forex.FOREX_RATE_TARGETCODE_RES);
                        break;

                    default:
                        break;
                }
                break;
            case ConstantGloble.FOREX_SELL_REQUEST:// 银行卖价 目标货币对
                quickOrRateSell = 2;
                switch (allOrCustomerReq) {
                    case ConstantGloble.FOREX_ALL:// 0----所有的货币对
                        // 得到卖出币种
                        Map<String, Object> map1 = selectedMap;// allRateList.get(sellSelectedPosition);
                        targetCode = (String) map1.get(Forex.FOREX_RATE_TARGETCODE_RES);
                        sourceCurCde = (String) map1.get(Forex.FOREX_RATE_SOURCECODE_RES);
                        break;
                    case ConstantGloble.FOREX_CUSTOMER:// 1---用户定制
                        // 得到卖出币种
                        Map<String, Object> map2 = selectedMap;// customerRateList.get(sellSelectedPosition);
                        targetCode = (String) map2.get(Forex.FOREX_RATE_TARGETCODE_RES);
                        sourceCurCde = (String) map2.get(Forex.FOREX_RATE_SOURCECODE_RES);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        // 查询卖出币种是否存在
        tradeConditionPsnForexQueryBlanceCucyList();
    }

    /** 外汇买卖条件----查询卖出币种信息 */
    public void tradeConditionPsnForexQueryBlanceCucyList() {
        if (tradeConditionFixOrCurr == 1) {
            // 定期
            getHttpTools().requestHttp(Forex.FOREX_PSNFOREXQUERYBLANCECUCYLIST_API, "tradeConditionFixBlanceCucyListCallback", null, true);
        } else if (tradeConditionFixOrCurr == 2) {
            // 活期
            getHttpTools().requestHttp(Forex.FOREX_PSNFOREXQUERYBLANCECUCYLIST_API, "tradeConditionCurrencyBlanceCucyListCallback", null, true);
        }
    }

    /** 定期-----卖出币种 */
    @SuppressWarnings("unchecked")
    public void tradeConditionFixBlanceCucyListCallback(Object resultObj) {
        Map<String, Object> result = getHttpTools().getResponseResult(resultObj);
        if (StringUtil.isNullOrEmpty(result)) {
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(this.getResources().getString(R.string.forex_rateinfo_sell_codes));
            return;
        }
        if (!result.containsKey(Forex.FORX_TERMSUBACCOUNT_RES)) {
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(this.getResources().getString(R.string.forex_rateinfo_sell_codes));
            return;
        }
        if (StringUtil.isNullOrEmpty(result.get(Forex.FORX_TERMSUBACCOUNT_RES))) {
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(this.getResources().getString(R.string.forex_rateinfo_sell_codes));
            return;
        }
        List<Map<String, Object>> termSubAccountList = (List<Map<String, Object>>) result.get(Forex.FORX_TERMSUBACCOUNT_RES);
        if (termSubAccountList == null || termSubAccountList.size() == 0) {
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(this.getResources().getString(R.string.forex_rateinfo_sell_codes));
            return;
        } else {
            BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FORX_TERMSUBACCOUNT_RES, termSubAccountList);
            int len2 = termSubAccountList.size();
            for (int j = 0; j < len2; j++) {
                Map<String, Object> termSubAccountMap = termSubAccountList.get(j);
                String status = (String) termSubAccountMap.get(Forex.FOREX_STATUS_RES);
                String type = (String) termSubAccountMap.get(Forex.FOREX_TYPE_RES);  // 存单类型
                // 账户状态正常,存单类型为定活两遍，整存整取
                if (ConstantGloble.FOREX_ACCTYPE_NORMAL.equals(status) && (ConstantGloble.FOREX_ACCTYPE_ZCZQ.equals(type) || ConstantGloble.FOREX_ACCTYPE_DHLB.equals(type))) {
                    // 得到可用余额，并判断是否大于0
                    Map<String, Object> balanceMap = (Map<String, Object>) termSubAccountMap.get(Forex.FOREX_BALANCE_RES);
                    if (!StringUtil.isNullOrEmpty(balanceMap)) {
                        String balance = (String) balanceMap.get(Forex.FOREX_AVAILABLEBALANCE_RES);
                        double b = Double.valueOf(balance);
                        if (b > 0) {  // 可用余额大于0
                            tradeConditionFixSellTag = true;
                            break;
                        }
                    }
                }
            }
        }

        if (!tradeConditionFixSellTag) {   // 不存在卖出币种
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.forex_rateinfo_sell_codes));
            return;
        } else {
            switch (quickOrRateSell) {
                case 1:// 快速交易
                    // 查询买入币种信息
                    requestPsnForexQueryBuyCucyList(); // 跳转到定期交易页面
                    break;
                case 2:// 点击银行卖价，查询卖出币种信息
                    isInFixResult(targetCode); // 卖出币种是否在卖出币种集合里面
                    break;
                case 3:// 点击银行买价，查询卖出币种信息
                    isInFixResult(sourceCurCde); // 卖出币种是否在卖出币种集合里面
                    break;
                default:
                    break;
            }
        }
    }

    /** 活期------卖出币种 */
    @SuppressWarnings("unchecked")
    public void tradeConditionCurrencyBlanceCucyListCallback(Object resultObj) {
        Map<String, Object> result = getHttpTools().getResponseResult(resultObj);
        tradeSellCurrResultList = new ArrayList<Map<String, Object>>();
        if (StringUtil.isNullOrEmpty(result)) {
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(this.getResources().getString(R.string.forex_rateinfo_sell_codes));
            return;
        }
        if (!result.containsKey(Forex.FOREX_SELLLIST_RES)) { // 不包含sellList键
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(this.getResources().getString(R.string.forex_rateinfo_sell_codes));
            return;
        }

        if (StringUtil.isNullOrEmpty(result.get(Forex.FOREX_SELLLIST_RES))) {   // sellList值为空
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(this.getResources().getString(R.string.forex_rateinfo_sell_codes));
            return;
        }
        List<Map<String, Object>> sellList = (List<Map<String, Object>>) result.get(Forex.FOREX_SELLLIST_RES);
        if (sellList == null || sellList.size() == 0) {
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(this.getResources().getString(R.string.forex_rateinfo_sell_codes));
            return;
        } else {
            // 存储卖出币种的sellList
            BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_SELLCODE_SELLLIST_KEY, sellList);
            int len = sellList.size();
            for (int i = 0; i < len; i++) {
                String status = (String) sellList.get(i).get(Forex.FOREX_STATUS_RES);  // 账户状态
                String normal = ConstantGloble.FOREX_ACCTYPE_NORMAL;  // 子账户状态正常
                if (normal.equals(status)) {
                    Map<String, Object> balance = (Map<String, Object>) sellList.get(i).get(Forex.FOREX_BALANCE_RES);
                    if (!StringUtil.isNullOrEmpty(balance)) {
                        String availableBalance = (String) balance.get(Forex.FOREX_AVAILABLEBALANCE_RES);
                        double moneyBolance = Double.valueOf(availableBalance);
                        Map<String, String> currency = (Map<String, String>) balance.get(Forex.FOREX_CURRENCY_RES);
                        if (!StringUtil.isNullOrEmpty(currency)) {  // 币种存在
                            String code = currency.get(Forex.FOREX_CURRENCY_CODE_RES).trim();
                            // 币种不允许为人民币
                            if (!StringUtil.isNull(code) && (!code.equals(ConstantGloble.FOREX_RMB_TAG1) || !code.equalsIgnoreCase(ConstantGloble.FOREX_RMB_CNA_TAG2))) {
                                if (moneyBolance > 0) {  // 账户余额大于0
                                    tradeSellCurrResultList.add(sellList.get(i));
                                }
                            }

                        }

                    }

                }
            }
            if (tradeSellCurrResultList.size() <= 0 || tradeSellCurrResultList == null) {
                tradeConditionSellTag = false;
                BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.forex_rateinfo_sell_codes));

                return;
            } else {
                BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_CURR_TRADESELLCODERESULTLIST, tradeSellCurrResultList);
                tradeConditionSellTag = true;
            }

        }

        if (!tradeConditionSellTag) {
            BaseHttpEngine.dissMissProgressDialog();  // 活期卖出不币种存在
            BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.forex_rateinfo_sell_codes));
            return;
        } else {
            switch (quickOrRateSell) {
                case 1:// 快速交易
                    // 活期卖出币种存在
                    requestPsnForexQueryBuyCucyList(); // 查询买入币种信息
                    break;
                case 2:// 银行卖价，卖出币种必须存在，否则不再进入到外汇交易页面中
                    isInResult(targetCode); // 判断卖出币种是否存在
                    break;
                case 3:// 银行买价，卖出币种必须存在，否则不再进入到外汇交易页面中
                    isInResult(sourceCurCde); // 判断卖出币种是否存在
                    break;
                default:
                    break;
            }
        }
    }

    /** 绑定的账户是定期 */
    private void accIsFix() {
        tradeConditionFixOrCurr = 1; // 定期 外汇行情交易
        fixOrCurrency = 2;
        BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_VOLUMENUMBERLIST_RES, volumeNumberList);
        switch (requestTag) {
            case ConstantGloble.FOREX_QUICK_REQUEST:// 快速交易
                quickOrRateSell = 1;
                break;
            case ConstantGloble.FOREX_BUY_REQUEST:// 银行买价 目标货币对
                quickOrRateSell = 3;
                switch (allOrCustomerReq) {
                    case ConstantGloble.FOREX_ALL:// 0----所有的货币对
                        Map<String, Object> map1 = selectedMap;// allRateList.get(buySelectedPosition);
                        sourceCurCde = (String) map1.get(Forex.FOREX_RATE_SOURCECODE_RES);
                        targetCode = (String) map1.get(Forex.FOREX_RATE_TARGETCODE_RES);
                        break;
                    case ConstantGloble.FOREX_CUSTOMER:// 1---用户定制
                        Map<String, Object> map = selectedMap;// customerRateList.get(buySelectedPosition);
                        sourceCurCde = (String) map.get(Forex.FOREX_RATE_SOURCECODE_RES);
                        targetCode = (String) map.get(Forex.FOREX_RATE_TARGETCODE_RES);
                        break;
                    default:
                        break;
                }
                break;
            case ConstantGloble.FOREX_SELL_REQUEST:// 银行卖价 目标货币对
                quickOrRateSell = 2;
                switch (allOrCustomerReq) {
                    case ConstantGloble.FOREX_ALL:// 0----所有的货币对
                        // 得到卖出币种
                        Map<String, Object> map1 = selectedMap;
                        targetCode = (String) map1.get(Forex.FOREX_RATE_TARGETCODE_RES);
                        sourceCurCde = (String) map1.get(Forex.FOREX_RATE_SOURCECODE_RES);
                        break;
                    case ConstantGloble.FOREX_CUSTOMER:// 1---用户定制
                        // 得到卖出币种
                        Map<String, Object> map = selectedMap;
                        targetCode = (String) map.get(Forex.FOREX_RATE_TARGETCODE_RES);
                        sourceCurCde = (String) map.get(Forex.FOREX_RATE_SOURCECODE_RES);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        // 查询卖出币种是否存在
        tradeConditionPsnForexQueryBlanceCucyList();
    }

    /**
     * 根据不同的账户跳转到不同的页面 活期我的外汇汇率页面用于判断是否跳转到定期页面
     */
    public void tradetPsnForexActIsset() {
        getHttpTools().requestHttp(Forex.FOREX_RATE_ACTISSET, "tradetPsnForexActIssetCallback", null, true);
    }

    /** 根据不同的账户跳转到不同的页面----回调 */
    @SuppressWarnings({ "unchecked" })
    public void tradetPsnForexActIssetCallback(Object resultObj) {
        BaseHttpEngine.dissMissProgressDialog();
        Map<String, Object> result = getHttpTools().getResponseResult(resultObj);
        String accountType = null;
        if (StringUtil.isNullOrEmpty(result)) {
            return;
        } else {
            if (StringUtil.isNullOrEmpty(result.get(Forex.FOREX_RATE_INVESTBINDINGINFO_RES))) {
                return;
            } else {
                investBindingInfo = (Map<String, String>) result.get(Forex.FOREX_RATE_INVESTBINDINGINFO_RES);
                // 存储investBindingInfo
                BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_RATE_INVESTBINDINGINFO_RES, investBindingInfo);
                accountType = investBindingInfo.get(Forex.FOREX_RATE_ACCOUNTTYPE_RES);
                if (StringUtil.isNull(accountType)) {
                    return;
                }
                BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_RATE_ACCOUNTTYPE_RES, accountType);
            }
            if (result.containsKey(Forex.FOREX_VOLUMENUMBERLIST_RES)) {
                volumeNumberList = (List<String>) result.get(Forex.FOREX_VOLUMENUMBERLIST_RES);
                // 存储volumeNumberList
                BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_VOLUMENUMBERLIST_RES, volumeNumberList);
            }
            if (StringUtil.isNullOrEmpty(investBindingInfo)) {
                return;
            } else {
                String accountId = investBindingInfo.get(Forex.FOREX_RATE_ACCOUNTID_RES);
                // 存储accountId
                BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_RATE_ACCOUNTID_RES, accountId);
                String investAccount = investBindingInfo.get(Forex.FOREX_RATE_INVESTACCOUNT_RES);
                // 存储投资账号investAccount
                BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_RATE_INVESTACCOUNT_RES, investAccount);
                String account = investBindingInfo.get(Forex.Forex_RATE_ACCOUNT_RES);
                // 存储账号
                BaseDroidApp.getInstanse().getBizDataMap().put(Forex.Forex_RATE_ACCOUNT_RES, account);
            }
        }
    }

    // 选择二级菜单 左侧
    @Override
    protected boolean selectedMenuItemHandler(Activity context, ImageAndText menuItem) {
        super.selectedMenuItemHandler(context, menuItem);
        menuOrTrade = 2;
        String menuId = menuItem.MenuID;
        if(menuId.equals("forexStorageCash_1")){// 外汇行情页面
            isRateInfo = 1;
            taskTag = 4;
            if (isOpen && isSettingAcc) {
                // 进入到外汇行情页面
                ActivityTaskManager.getInstance().removeAllSecondActivity();
                Intent intent = new Intent();
                intent.setClass(this, ForexRateInfoOutlayActivity.class);
                context.startActivity(intent);
                finish();
            } else {
                // 是否开通理财服务
                BaseHttpEngine.showProgressDialogCanGoBack();
                requestPsnInvestmentManageIsOpen();
            }

        }
        else if(menuId.equals("forexStorageCash_2")){// 我的外汇
            isRateInfo = 2;
            taskTag = 2;
            if (isOpen && isSettingAcc) {
                // 默认进入到 活期--我的外汇
                ActivityTaskManager.getInstance().removeAllSecondActivity();
                Intent intent3 = new Intent();
                intent3.setClass(this, ForexCustomerRateInfoActivity.class);
                context.startActivity(intent3);
                finish();
            } else {
                // 是否开通理财服务
                BaseHttpEngine.showProgressDialogCanGoBack();
                requestPsnInvestmentManageIsOpen();
            }

        }
        else if(menuId.equals("forexStorageCash_3")){// 成交状况查询
            isRateInfo = 2;
            taskTag = 3;
            if (isOpen && isSettingAcc) {
                ActivityTaskManager.getInstance().removeAllSecondActivity();
                Intent intent3 = new Intent();
                intent3.setClass(this, ForexStrikeQueryActivity.class);
                context.startActivity(intent3);
                finish();
            } else {
                // 是否开通理财服务
                BaseHttpEngine.showProgressDialogCanGoBack();
                requestPsnInvestmentManageIsOpen();

            }

        }
        else if(menuId.equals("forexStorageCash_4")){// 委托查询-----新功能401
            isRateInfo = 2;
            taskTag = 7;
            if (isOpen && isSettingAcc) {
//                ActivityTaskManager.getInstance().removeAllSecondActivity();
                Intent intent3 = new Intent();
                intent3.setClass(this, ForexQuashQueryActivity.class);
                context.startActivity(intent3);
                finish();
            } else {
                // 是否开通理财服务
                BaseHttpEngine.showProgressDialogCanGoBack();
                requestPsnInvestmentManageIsOpen();

            }

        }
        return true;




//		super.setSelectedMenu(clickIndex);
//		menuOrTrade = 2;
//		switch (clickIndex) {
//		case 0:// 外汇行情页面
//			isRateInfo = 1;
//			taskTag = 4;
//
//			if (isOpen && isSettingAcc) {
//				// 进入到外汇行情页面
//				ActivityTaskManager.getInstance().removeAllSecondActivity();
//				Intent intent = new Intent();
//				intent.setClass(this, ForexRateInfoOutlayActivity.class);
//				startActivity(intent);
//			} else {
//				// 是否开通理财服务
//				BaseHttpEngine.showProgressDialogCanGoBack();
//				requestPsnInvestmentManageIsOpen();
//			}
//			break;
//		case 1:// 我的外汇
//			isRateInfo = 2;
//			taskTag = 2;
//			if (isOpen && isSettingAcc) {
//				// 默认进入到 活期--我的外汇
//				ActivityTaskManager.getInstance().removeAllSecondActivity();
//				Intent intent3 = new Intent();
//				intent3.setClass(this, ForexCustomerRateInfoActivity.class);
//				startActivity(intent3);
//			} else {
//				// 是否开通理财服务
//				BaseHttpEngine.showProgressDialogCanGoBack();
//				requestPsnInvestmentManageIsOpen();
//			}
//
//			break;
//
//		case 2:// 成交状况查询
//			isRateInfo = 2;
//			taskTag = 3;
//			if (isOpen && isSettingAcc) {
//				ActivityTaskManager.getInstance().removeAllSecondActivity();
//				Intent intent3 = new Intent();
//				intent3.setClass(this, ForexStrikeQueryActivity.class);
//				startActivity(intent3);
//
//			} else {
//				// 是否开通理财服务
//				BaseHttpEngine.showProgressDialogCanGoBack();
//				requestPsnInvestmentManageIsOpen();
//
//			}
//			break;
//		case 3:// 委托查询-----新功能401
//			isRateInfo = 2;
//			taskTag = 7;
//			if (isOpen && isSettingAcc) {
//				ActivityTaskManager.getInstance().removeAllSecondActivity();
//				Intent intent3 = new Intent();
//				intent3.setClass(this, ForexQuashQueryActivity.class);
//				startActivity(intent3);
//
//			} else {
//				// 是否开通理财服务
//				BaseHttpEngine.showProgressDialogCanGoBack();
//				requestPsnInvestmentManageIsOpen();
//
//			}
//			break;
//		default:
//
//			break;
//		}

    }

    /** 二级菜单判断是否开通投资理财 */
    public void requestMenuIsOpenCallback(Object resultObj) {
        String isOpenOr = getHttpTools().getResponseResult(resultObj);
        // isOpen
        if ("false".equals(isOpenOr)) {
            isOpen = false;
        } else {
            isOpen = true;
        }
        // TODO-----------------------------------------------
        // isOpen = false;
        // 查询账户
        requestPsnForexActIsset();
    }

    /***
     * 判断用户是否已设置外汇账户---是定期还是活期
     */
    public void requestPsnForexActIsset() {
        if (menuOrTrade == 2) {
            // 二级菜单
            getHttpTools().requestHttp(Forex.FOREX_RATE_ACTISSET, "requestMenuActIssetCallback", null, true);
        } else {
            // 快速交易
            getHttpTools().requestHttp(Forex.FOREX_RATE_ACTISSET, "requestPsnForexActIssetCallback", null, true);
        }
    }

    /** 二级菜单---查询是否设置账户----回调 */
    @SuppressWarnings("unchecked")
    public void requestMenuActIssetCallback(Object resultObj) {
        BaseHttpEngine.dissMissProgressDialog();
        Map<String, Object> result = getHttpTools().getResponseResult(resultObj);
        if (StringUtil.isNullOrEmpty(result)) {
            isSettingAcc = false;
        } else {
            if (StringUtil.isNullOrEmpty(result.get(Forex.FOREX_RATE_INVESTBINDINGINFO_RES))) {
                isSettingAcc = false;
            } else {
                investBindingInfo = (Map<String, String>) result.get(Forex.FOREX_RATE_INVESTBINDINGINFO_RES);
                BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_RATE_INVESTBINDINGINFO_RES, investBindingInfo);
                isSettingAcc = true;
                accountType = investBindingInfo.get(Forex.FOREX_RATE_ACCOUNTTYPE_RES);
                if (StringUtil.isNull(accountType)) {
                    return;
                }
                if (result.containsKey(Forex.FOREX_VOLUMENUMBERLIST_RES)) {
                    volumeNumberList = (List<String>) result.get(Forex.FOREX_VOLUMENUMBERLIST_RES);
                    // 存储volumeNumberList
                    BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_VOLUMENUMBERLIST_RES, volumeNumberList);
                }

                if (StringUtil.isNullOrEmpty(investBindingInfo)) {
                    return;
                } else {
                    String accountId = investBindingInfo.get(Forex.FOREX_RATE_ACCOUNTID_RES);
                    // 存储accountId
                    BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_RATE_ACCOUNTID_RES, accountId);
                    String investAccount = investBindingInfo.get(Forex.FOREX_RATE_INVESTACCOUNT_RES);
                    // 存储投资账号investAccount
                    BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_RATE_INVESTACCOUNT_RES, investAccount);
                    String account = investBindingInfo.get(Forex.Forex_RATE_ACCOUNT_RES);
                    // 存储账号
                    BaseDroidApp.getInstanse().getBizDataMap().put(Forex.Forex_RATE_ACCOUNT_RES, account);
                }
            }
        }
        // TODO------------------------------------------------
        if (!isOpen || !isSettingAcc) {
            getPopup();
            return;
        }
        if (isOpen && isSettingAcc) {// 开通理财服务和设置外汇账户
            switch (taskTag) {
                case 4:// 外汇行情
//                    ActivityTaskManager.getInstance().removeAllSecondActivity();
                    Intent intent = new Intent();
                    intent.setClass(this, ForexRateInfoOutlayActivity.class);
                    curActivity.startActivity(intent);
                    finish();
                    break;
                case 3:// 成交状况查询
//                    ActivityTaskManager.getInstance().removeAllSecondActivity();
                    Intent intent3 = new Intent();
                    intent3.setClass(this, ForexStrikeQueryActivity.class);
                    curActivity.startActivity(intent3);
                    finish();
                    break;
                case 2:// 我的外汇
//                    ActivityTaskManager.getInstance().removeAllSecondActivity();
                    Intent intent4 = new Intent();
                    intent4.setClass(this, ForexCustomerRateInfoActivity.class);
                    curActivity.startActivity(intent4);
                    finish();
                    break;
                case 7: // 委托交易状况查询
//                    ActivityTaskManager.getInstance().removeAllSecondActivity();
                    Intent intent6 = new Intent();
                    intent6.setClass(this, ForexQuashQueryActivity.class);
                    curActivity.startActivity(intent6);
                    finish();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BaseDroidApp.getInstanse().setCurrentAct(this);
        switch (resultCode) {
            case RESULT_OK:
                switch (requestCode) {
                    case ConstantGloble.ACTIVITY_RESULT_CODE:// 投资理财1
                        // 开通成功的响应
                        moneyButtonView.setVisibility(View.GONE);
                        moneyTextView.setVisibility(View.VISIBLE);
                        isOpen = true;
                        if (isSettingAcc) {
                            // 已经设定账户
                            switch (taskTag) {
                                case 4:// 外汇行情
                                    BaseDroidApp.getInstanse().dismissMessageDialogFore();
                                    BaseHttpEngine.showProgressDialog();
                                    requestInitPsnCustomerRate();
                                    break;
                                case 2:// 我的外汇
                                    BaseDroidApp.getInstanse().dismissMessageDialogFore();
                                    BaseHttpEngine.showProgressDialog();
                                    customerPsnForexActIsset();
                                    break;
                                case 3:// 成交状况查询
                                    BaseDroidApp.getInstanse().dismissMessageDialogFore();
                                    BaseHttpEngine.showProgressDialog();
                                    requestSystemDateTime();
                                    break;
                                // TODO------快速交易
                                case 1:// 快速交易
                                    BaseDroidApp.getInstanse().dismissMessageDialogFore();
                                    BaseHttpEngine.showProgressDialog();
                                    ratePsnForexActIsset();
                                    break;
                                case 5:// 我的外汇汇率
                                    BaseDroidApp.getInstanse().dismissMessageDialogFore();
                                    BaseHttpEngine.showProgressDialog();
                                    // 查询用户定制的外汇汇率
                                    requestPsnCustomerRate();
                                    break;
                                case 6:
                                    BaseDroidApp.getInstanse().dismissMessageDialogFore();
                                    BaseHttpEngine.showProgressDialog();
                                    requestPsnAllRates();
                                    break;
                                case 7:// 委托
                                    BaseDroidApp.getInstanse().dismissMessageDialogFore();
                                    BaseHttpEngine.showProgressDialog();
                                    // 查询当前有效委托
                                    requestPsnForexAllTransQuery1(null, null, ConstantGloble.FOREX_CURRENTTYPE, ConstantGloble.FOREX_PAGESIZE, ConstantGloble.FOREX_CURRENTINDEX, ConstantGloble.FOREX_REFRESH);
                                    break;
                            }

                        } else {
                            // 没设定账户
                            // getPopup();
                            accButtonView.setVisibility(View.VISIBLE);
                            accTextView.setVisibility(View.GONE);
                        }
                        break;
                    case ConstantGloble.FOREX_RATE_QUICK_ACC:// 请求账户11
                        isSettingAcc = true;
                        isOpen = true;
                        switch (taskTag) {
                            case 4:// 外汇行情
                                BaseDroidApp.getInstanse().dismissMessageDialogFore();
                                BaseHttpEngine.showProgressDialog();
                                requestInitPsnCustomerRate();
                                break;
                            case 2:// 我的外汇-活期-默认
                                BaseDroidApp.getInstanse().dismissMessageDialogFore();
                                BaseHttpEngine.showProgressDialog();
                                customerPsnForexActIsset();
                                break;
                            case 3:// 成交状况查询
                                BaseDroidApp.getInstanse().dismissMessageDialogFore();
                                BaseHttpEngine.showProgressDialog();
                                requestSystemDateTime();
                                break;
                            case 1:// 快速交易
                                BaseDroidApp.getInstanse().dismissMessageDialogFore();
                                BaseHttpEngine.showProgressDialog();
                                ratePsnForexActIsset();
                                break;
                            case 5:// 我的外汇汇率
                                BaseDroidApp.getInstanse().dismissMessageDialogFore();
                                // 查询用户定制的外汇汇率
                                BaseHttpEngine.showProgressDialog();
                                requestPsnCustomerRate();
                                break;
                            case 7:// 委托
                                BaseDroidApp.getInstanse().dismissMessageDialogFore();
                                BaseHttpEngine.showProgressDialog();
                                // 查询当前有效委托
                                requestPsnForexAllTransQuery1(null, null, ConstantGloble.FOREX_CURRENTTYPE, ConstantGloble.FOREX_PAGESIZE, ConstantGloble.FOREX_CURRENTINDEX, ConstantGloble.FOREX_REFRESH);
                                break;
                        }

                        break;

                }
                break;
            case RESULT_CANCELED:
                // 开通失败的响应，不能再调用getPopup()
                break;
        }
    }

    // 外汇买卖必须判断买入币种、卖出是否存在、以及用户绑定的账户是定期还是活期
    /** 外汇买卖条件----查询买入币种是否存在 */
    public void tradeConditionPsnForexQueryBuyCucyList() {
        getHttpTools().requestHttp(Forex.FOREX_PSNFOREXQUERYBUYCUCYLIST_API, "tradeConditionPsnForexQueryBuyCucyListCallback", null, true);
    }

    /** 外汇买卖条件----查询买入币种是否存在---回调 */
    public void tradeConditionPsnForexQueryBuyCucyListCallback(Object resultObj) {
        // BiiResponse biiResponse = (BiiResponse) resultObj;
        // List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        // // 得到response
        // BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        // // 得到result
        // if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
        // BaseHttpEngine.dissMissProgressDialog();
        // BaseDroidApp.getInstanse().showInfoMessageDialog(
        // getResources().getString(R.string.forex_rateinfo_buy_codes));
        // return;
        //
        // }
        // tradeBuyCodeResultList = (List<Map<String, String>>)
        // biiResponseBody.getResult();
        // BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_BUYCODERESULTLIST,
        // tradeBuyCodeResultList);
    }





    /** 卖出币种是否在卖出币种集合里面 */
    @SuppressWarnings("unchecked")
    private void isInFixResult(String sellCodeCode) {
        List<Map<String, Object>> termSubAccountList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap().get(Forex.FORX_TERMSUBACCOUNT_RES);
        int len2 = termSubAccountList.size();
        List<Map<String, Object>> littleResult = new ArrayList<Map<String, Object>>();
        for (int j = 0; j < len2; j++) {
            Map<String, Object> termSubAccountMap = termSubAccountList.get(j);
            if (StringUtil.isNullOrEmpty(termSubAccountMap)) {
                continue;
            }
            String type = (String) termSubAccountMap.get(Forex.FOREX_TYPE_RES);
            String cashRemit = (String) termSubAccountMap.get(Forex.FOREX_CASHREMIT_RES);
            if (StringUtil.isNull(cashRemit)) {
                continue;
            }
            String cash = LocalData.CurrencyCashremit.get(cashRemit);
            Map<String, Object> balanceMap = (Map<String, Object>) termSubAccountMap.get(Forex.FOREX_BALANCE_RES);
            if (StringUtil.isNullOrEmpty(balanceMap)) {
                continue;
            }
            Map<String, String> currency = (Map<String, String>) balanceMap.get(Forex.FOREX_CURRENCY_RES);
            if (StringUtil.isNullOrEmpty(currency)) {
                continue;
            }
            String code = currency.get(Forex.FOREX_CURRENCY_CODE_RES);
            if (StringUtil.isNull(code)) {
                continue;
            }
            String codeName = null;
            String codeCodeName = null;
            if (LocalData.Currency.containsKey(code) && LocalData.Currency.containsKey(sellCodeCode)) {
                codeName = LocalData.Currency.get(code);
                codeCodeName = LocalData.Currency.get(sellCodeCode);
            }
            // 整存整取-----不判断钞汇
            if (!StringUtil.isNull(type) && ConstantGloble.FOREX_ACCTYPE_ZCZQ.equals(type)) {
                if (codeName.equals(codeCodeName)) {
                    littleResult.add(termSubAccountMap);
                }
            }
        }
        if (littleResult == null || littleResult.size() <= 0) {
            // 卖出币种不存在
            String codeName = LocalData.Currency.get(sellCodeCode);
            String info1 = getResources().getString(R.string.forex_curr_acc_code_info1);
            String info2 = getResources().getString(R.string.forex_curr_acc_code_info2);
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(info1 + codeName + info2);
            return;
        }
        getReturnValue(littleResult); // 卖出币种存在
    }

    /** 卖出币种存在时，判断可用余额、账户状态 */
    @SuppressWarnings("unchecked")
    private void getReturnValue(List<Map<String, Object>> list) {
        int len = list.size();
        boolean t = false;
        boolean k = false;
        Map<String, String> currency = null;
        String codeName = null;
        for (int i = 0; i < len; i++) {
            Map<String, Object> termSubAccountMap = list.get(i);
            if (StringUtil.isNullOrEmpty(termSubAccountMap)) {
                continue;
            }
            String status = (String) termSubAccountMap.get(Forex.FOREX_STATUS_RES);
            Map<String, Object> balanceMap = (Map<String, Object>) termSubAccountMap.get(Forex.FOREX_BALANCE_RES);
            if (StringUtil.isNullOrEmpty(balanceMap)) {
                continue;
            }
            currency = (Map<String, String>) balanceMap.get(Forex.FOREX_CURRENCY_RES);
            if (StringUtil.isNullOrEmpty(currency)) {
                continue;
            }
            String code = currency.get(Forex.FOREX_CURRENCY_CODE_RES);
            codeName = LocalData.Currency.get(code);
            String availableBalance = (String) balanceMap.get(Forex.FOREX_AVAILABLEBALANCE_RES);

            if (StringUtil.isNull(availableBalance) || (Double.valueOf(availableBalance) <= 0)) {
                t = false;
                continue;

            } else {
                t = true;
                if (ConstantGloble.FOREX_ACCTYPE_NORMAL.equals(status)) {
                    String volumeNumber = (String) termSubAccountMap.get(Forex.FOREX_VOLUMENUMBER_RES);
                    String cdnumber = (String) termSubAccountMap.get(Forex.FOREX_CDNUMBER_RES);
                    k = true;
                    BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_VOLUMENUMBER_RES, volumeNumber);
                    BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_CDNUMBER_RES, cdnumber);

                    break;
                } else {
                    k = false;
                }
            }
        }
        if (!t) {
            // 账户余额
            String info1 = getResources().getString(R.string.forex_curr_acc_info1);
            String info2 = getResources().getString(R.string.forex_curr_acc_info2);
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(info1 + codeName + info2);
            return;
        }
        if (!k) {
            // 账户状态
            String info = getResources().getString(R.string.forex_curr_acc_status);
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(codeName + info);
            return;
        }
        if (t && k) {
            requestPsnForexQueryBuyCucyList();  // 查询买入币种信息
        }
    }

    /** 判断卖出币种是否在卖出result里面 */
    @SuppressWarnings("unchecked")
    private void isInResult(String sellCodeCode) {
        List<Map<String, Object>> sellList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.FOREX_SELLCODE_SELLLIST_KEY);
        int lens = sellList.size();
        boolean tt = false;
        boolean k = false;
        boolean b = false;
        for (int i = 0; i < lens; i++) {
            Map<String, Object> map = sellList.get(i);
            if (StringUtil.isNullOrEmpty(map)) {
                continue;
            }
            String cashRemit = (String) map.get(Forex.FOREX_CASHREMIT_RES);
            String status = (String) map.get(Forex.FOREX_STATUS_RES);   // 账户状态
            if (StringUtil.isNullOrEmpty(status)) {
                continue;
            }
            Map<String, Object> balance = (Map<String, Object>) map.get(Forex.FOREX_BALANCE_RES);
            if (StringUtil.isNullOrEmpty(balance)) {
                continue;
            }
            String availableBalance = (String) balance.get(Forex.FOREX_AVAILABLEBALANCE_RES);
            Map<String, String> currency = (Map<String, String>) balance.get(Forex.FOREX_CURRENCY_RES);
            if (StringUtil.isNullOrEmpty(currency)) {
                continue;
            }
            String code = currency.get(Forex.FOREX_CURRENCY_CODE_RES).trim();
            String codeName = null;
            String codeCodeName = null;
            if (!StringUtil.isNull(code) && LocalData.Currency.containsKey(code) && LocalData.Currency.containsKey(sellCodeCode)) {
                codeName = LocalData.Currency.get(code);
                codeCodeName = LocalData.Currency.get(sellCodeCode);
            }
            // 币种不为空，code和sourceCurCde相同
            if (codeName.equals(codeCodeName)) {
                tt = true;  // 币种存在
                if (StringUtil.isNull(availableBalance) || (Double.valueOf(availableBalance) <= 0)) {
                    // 账户余额
                    String info1 = getResources().getString(R.string.forex_curr_acc_info1);
                    String info2 = getResources().getString(R.string.forex_curr_acc_info2);
                    BaseHttpEngine.dissMissProgressDialog();
                    BaseDroidApp.getInstanse().showInfoMessageDialog(info1 + codeName + info2);
                    b = false;
                    break;
                } else {
                    b = true;
                    //  状态子账户状态正常
                    String normal = ConstantGloble.FOREX_ACCTYPE_NORMAL;
                    if (normal.equals(status)) {
                        k = true;
                    } else {
                        String info = getResources().getString(R.string.forex_curr_acc_status);
                        BaseHttpEngine.dissMissProgressDialog();
                        BaseDroidApp.getInstanse().showInfoMessageDialog(codeName + info);
                        k = false;
                        break;
                    }

                }
            }

        }

        if (!tt) {
            String codeName = LocalData.Currency.get(sellCodeCode);
            String info1 = getResources().getString(R.string.forex_curr_acc_code_info1);
            String info2 = getResources().getString(R.string.forex_curr_acc_code_info2);
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(info1 + codeName + info2);
            return;
        } else {
            if (k && b) {
                switch (quickOrRateSell) {
                    case 2:// 银行卖价
                        int position = getCurrSellCodeInListPosition(targetCode);
                        if (position < 0) {
                            String info1 = getResources().getString(R.string.forex_curr_acc_code_info1);
                            String info2 = getResources().getString(R.string.forex_curr_acc_code_info2);
                            BaseHttpEngine.dissMissProgressDialog();
                            String code = LocalData.Currency.get(targetCode);
                            BaseDroidApp.getInstanse().showInfoMessageDialog(info1 + code + info2);
                            return;
                        } else {
                            requestPsnForexQueryBuyCucyList();  // 查询买入币种信息
                        }
                        break;
                    case 3:// 银行买价
                        int po = getCurrSellCodeInListPosition(sourceCurCde);
                        if (po < 0) {
                            String info1 = getResources().getString(R.string.forex_curr_acc_code_info1);
                            String info2 = getResources().getString(R.string.forex_curr_acc_code_info2);
                            BaseHttpEngine.dissMissProgressDialog();
                            String code = LocalData.Currency.get(sourceCurCde);
                            BaseDroidApp.getInstanse().showInfoMessageDialog(info1 + code + info2);
                        } else {
                            requestPsnForexQueryBuyCucyList();  // 查询买入币种信息
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /** 活期账户 银行买价 查询买入币种列表信息 **/
    private void requestPsnForexQueryBuyCucyList() {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Forex.FOREX_PSNFOREXQUERYBUYCUCYLIST_API);
        biiRequestBody.setConversationId(commConversationId);
        biiRequestBody.setParams(null);
        HttpManager.requestBii(biiRequestBody, this, "requestPsnForexQueryBuyCucyListCallback");
    }

    /** 活期 查询买入币种列表信息---回调 */
    @SuppressWarnings("unchecked")
    public void requestPsnForexQueryBuyCucyListCallback(Object resultObj) {
        BaseHttpEngine.dissMissProgressDialog();
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {  // 弹出不可以买的信息
            BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.forex_rateinfo_buy_codes));
            return;
        }
        List<Map<String, String>> buyCodeResultList = (List<Map<String, String>>) biiResponseBody.getResult();
        if (buyCodeResultList == null || buyCodeResultList.size() == 0) {
            // 弹出不可以买的信息
            BaseDroidApp.getInstanse().showInfoMessageDialog(this.getString(R.string.forex_rateinfo_buy_codes));
            return;
        } else { // 存储买入币种信息
            BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_BUYCODERESULTLIST, buyCodeResultList);
            getBuyCode(); // 处理买入币种数据
        }
    }

    /** 得到卖出币种在tradeSellCurrResultList中的position */
    @SuppressWarnings("unchecked")
    private int getCurrSellCodeInListPosition(String sellCodeCode) {
        int position = -1;
        int len = tradeSellCurrResultList.size();
        for (int i = 0; i < len; i++) {
            Map<String, Object> map = tradeSellCurrResultList.get(i);
            if (StringUtil.isNullOrEmpty(map)) {
                continue;
            }
            Map<String, String> currency = (Map<String, String>) map.get(Forex.FOREX_CURRENCY_RES);
            if (StringUtil.isNullOrEmpty(currency)) {
                continue;
            }
            String code = currency.get(Forex.FOREX_CURRENCY_CODE_RES).trim();
            if (StringUtil.isNull(code)) {
                continue;
            }
            String cashRemit = (String) map.get(Forex.FOREX_CASHREMIT_RES);
            String cash = null;
            if (!StringUtil.isNull(cashRemit) && LocalData.CurrencyCashremit.containsKey(cashRemit)) {
                cash = LocalData.CurrencyCashremit.get(cashRemit);
            }
            // 外汇行情页面，所有币种的钞汇标志都是现汇
            if (code.equals(sellCodeCode)) {
                position = i;  // 卖出币种在用户账户中
                BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_SELLINLIST_POSITION, i);
                break;
            }
        }
        return position;
    }

    /** 处理买入币种信息 */
    @SuppressWarnings("unchecked")
    private void getBuyCode() {
        List<Map<String, String>> buyCodeResultList = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.FOREX_BUYCODERESULTLIST);
        int len = buyCodeResultList.size();
        buyCodeList = new ArrayList<String>();
        buyCodeDealList = new ArrayList<String>();
        for (int i = 0; i < len; i++) {
            Map<String, String> buyCodeResulMap = buyCodeResultList.get(i);
            String buyCodeResul = buyCodeResulMap.get(Forex.FOREX_BUY_CODE_RES).trim();
            String code = null;
            if (LocalData.Currency.containsKey(buyCodeResul)) {
                code = LocalData.Currency.get(buyCodeResul);
                buyCodeList.add(buyCodeResul);
                buyCodeDealList.add(code);
            } else {
                continue;
            }
        }
        if (buyCodeDealList == null || buyCodeDealList.size() <= 0 || buyCodeList == null || buyCodeList.size() <= 0) {
            BaseHttpEngine.dissMissProgressDialog();
            return;
        } else {
            BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_BUYCODEDEALLIST_KEY, buyCodeDealList);
            BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_BUYCODELIST_KEY, buyCodeList);
            switchTag();
        }
    }

    private void switchTag() {
        stopPollingFlag();
        if (!isFinishing()) {
            switch (quickOrRateSell) {
                case 1:// 快速交易
                    switch (fixOrCurrency) {
                        case 1:// 活期
                            Intent intent3 = new Intent();
                            intent3.setClass(this, ForexQuickCurrentSubmitActivity.class);
                            intent3.putExtra(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG, ConstantGloble.FOREX_TRADE_QUICK);
                            startActivityForResult(intent3, ConstantGloble.FOREX_RATE_TRADE_TAG);
                            finish();
                            break;
                        case 2:// 定期
                            Intent intent2 = new Intent();
                            intent2.setClass(this, ForexQuickTradeSubmitActivity.class);
                            intent2.putExtra(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG, ConstantGloble.FOREX_TRADE_QUICK);// -1
                            startActivityForResult(intent2, ConstantGloble.FOREX_RATE_TRADE_TAG);
                            finish();
                            break;
                        default:
                            break;
                    }
                    break;
                case 2:// 银行卖价
                    buy_isInBuyResultPosition(sourceCurCde);
                    switch (fixOrCurrency) {
                        case 1:// 活期
                            Intent intent3 = new Intent();
                            intent3.setClass(this, ForexQuickCurrentSubmitActivity.class);
                            intent3.putExtra(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG,
                                    ConstantGloble.FOREX_TRADE_QUICK_SELL_CURR);
                            startActivityForResult(intent3, ConstantGloble.FOREX_RATE_TRADE_TAG);
                            finish();
                            break;
                        case 2:// 定期
                            Intent intent = new Intent();
                            intent.setClass(this, ForexQuickTradeSubmitActivity.class);
                            intent.putExtra(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG,
                                    ConstantGloble.FOREX_TRADE_QUICK_SELL_FIX);// 601
                            startActivityForResult(intent, ConstantGloble.FOREX_RATE_TRADE_TAG);
                            finish();
                            break;
                    }
                    break;
                case 3:// 银行买价
                    // 买入币种是否存在买入币种集合里面
                    buy_isInBuyResultPosition(targetCode);
                    switch (fixOrCurrency) {
                        case 1:// 活期
                            Intent intent31 = new Intent();
                            intent31.setClass(this, ForexQuickCurrentSubmitActivity.class);
                            intent31.putExtra(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG,
                                    ConstantGloble.FOREX_TRADE_QUICK_RATEINFO_CURR);
                            startActivity(intent31);
                            finish();
                            break;
                        case 2:// 定期
                            Intent intent = new Intent();
                            intent.setClass(this, ForexQuickTradeSubmitActivity.class);
                            intent.putExtra(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG,
                                    ConstantGloble.FOREX_TRADE_QUICK_RATEINFO);// 301
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
    }

    /** 银行买价-买入币种是否在买入币种列表里面以及位置 */
    private void buy_isInBuyResultPosition(String buyCodeCode) {
        int len = buyCodeList.size();
        boolean tt = false;
        for (int i = 0; i < len; i++) {
            String buyCodeResul = buyCodeList.get(i);
            if (buyCodeCode.equals(buyCodeResul)) {
                tt = true;
                BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_BUYINLIST_POSITION, i);
                break;
            }
            if (!tt) {
                BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_BUYINLIST_POSITION, -1);
            }
        }
    }

    /**
     * 我的外汇汇率按钮响应事件. 查询用户定制的外汇汇率信息
     */
    public void requestPsnCustomerRate() {
        getHttpTools().requestHttp(Forex.FOREX_CUSTOMER_RATE, "requestPsnCustomerRateCallback", null, true);
    }

    /** 查询用户定制的外汇汇率信息---回调 */
    public void requestPsnCustomerRateCallback(Object resultObj) {
    }

    /**
     * 查询全部外汇行情--12
     */
    public void requestPsnAllRates() {
        getHttpTools().requestHttp(Forex.FOREX_ALLRATE, "requestPsnAllRateCallback", null, true);
    }

    public void requestPsnAllRateCallback(Object resultObj) {
    }

    /** 全部未登录时汇率 外置请求全部汇率*/
    public void requestAllRatesOutlay() {
        BaseHttpEngine.showProgressDialog();
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Forex.FOREX_PsnGetAllExchangeRatesOutlay);
        Map<String, String> params = new HashMap<String, String>();
        // 客户登记的交易账户的省行联行号
        params.put(Forex.FOREX_ibknum, LocalDataService.getInstance().getIbkNum(ConstantGloble.Forex));
        // 牌价类型(外汇f： 黄金：G)
        params.put(Forex.FOREX_paritiesType, "F");
        params.put(Forex.FOREX_offerType, "R");
        biiRequestBody.setParams(params);
        HttpManager.requestOutlayBii(biiRequestBody, this, "requestAllRatesOutlayCallback");
    }

    /** 全部未登录时汇率 ----回掉*/
    public void requestAllRatesOutlayCallback(Object resultObj){

    }

    /** 查询全部外汇行情--12 -- 下拉刷新无通讯框*/
    public void requestNoDialogPsnAllRates() {
        getHttpTools().requestHttpWithNoDialog(Forex.FOREX_ALLRATE, "requestPsnAllRateCallback", null, true);
    }

    /** 查询自选外汇行情--12 -- 下拉刷新无通讯框*/
    public void requestNoDialogPsnCustomerRate() {
        getHttpTools().requestHttpWithNoDialog(Forex.FOREX_CUSTOMER_RATE, "requestPsnCustomerRateCallback", null, true);
    }

    /** 查询未登录全部外汇行情--12 -- 下拉刷新无通讯框*/
    public void requestNoDialogAllRatesOutlay() {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Forex.FOREX_PsnGetAllExchangeRatesOutlay);
        Map<String, String> params = new HashMap<String, String>();
        // 客户登记的交易账户的省行联行号
        params.put(Forex.FOREX_ibknum, LocalDataService.getInstance().getIbkNum(ConstantGloble.Forex));
        // 牌价类型(外汇f： 黄金：G)
        params.put(Forex.FOREX_paritiesType, "F");
        params.put(Forex.FOREX_offerType, "R");
        biiRequestBody.setParams(params);
        HttpManager.requestOutlayBii(biiRequestBody, this, "requestAllRatesOutlayCallback");
    }

    /** 定期---外汇买卖回调 */
    public void requestPsnForexTradeCallback(Object resultObj) {
    }

    /**
     * 活期-----外汇买卖确认---提交
     *
     * @param investAccount
     *            :投资账号
     * @param bCurrency
     *            :买入币别
     * @param sCurrency
     *            :卖出币别
     * @param transFlag
     *            :买卖标志,0 买入，1 卖出
     * @param sAmount
     *            :卖出金额
     * @param bAmount
     *            :买入金额
     * @param exchangeType
     *            :交易类型,03 获利委托 04 止损委托 05 二选一委托 07 限价即时 08 市价即时
     * @param exchangeRate
     *            :市价汇率
     * @param limitRate
     *            :限价汇率
     * @param cashRemit
     *            :钞汇标识
     * @param volumeNumber
     *            :定一本册号
     * @param cdNumber
     *            :存单号
     * @param winRate
     *            :获利汇率
     * @param cDType
     *            :储种
     * @param loseRate
     *            :止损汇率
     * @param token
     * @param consignDays
     *            :委托截至日期
     * @param consignHour
     *            :委托截至时刻
     */
    public void requestCurrencyPsnForexTrade(String investAccount, String bCurrency, String sCurrency, String transFlag, String sAmount, String bAmount, String exchangeType, String exchangeRate, String limitRate, String cashRemit, String volumeNumber, String cdNumber, String winRate, String cDType, String loseRate, String token, String consignDays, String consignHour) {
        Map<String, Object> map = new Hashtable<String, Object>();
        map.put(Forex.FOREX_INVESTACCOUNT_REQ, investAccount);
        map.put(Forex.FOREX_BCURRENCY_REQ, bCurrency);
        map.put(Forex.FOREX_SCURRENCYT_REQ, sCurrency);
        map.put(Forex.FOREX_TRANSFLAG_REQ, transFlag);
        map.put(Forex.FOREX_EXCHANGETYPET_REQ, exchangeType);

        map.put(Forex.FOREX_CASHREMIT_REQ, cashRemit);
        map.put(Forex.FOREX_TOKEN_REQ, token);

        int type = Integer.valueOf(exchangeType);
        LogGloble.d(TAG + " type", String.valueOf(type));
        String buyTag = LocalData.forexTradeSellOrBuyList.get(0);
        switch (currencyOrFixTag) {
            case 2:// 定期
                map.put(Forex.FOREX_VOLUMENUMBER_REQ, volumeNumber);
                map.put(Forex.FOREX_CDNUMBER_REQ, cdNumber);
                map.put(Forex.FOREX_CDTYPE_REQ, cDType);
                break;

            default:// 活期
                if (buyTag.equals(transFlag)) {
                    map.put(Forex.FOREX_BAMOUNT_REQ, bAmount);
                    // 买入
                    switch (type) {
                        case ConstantGloble.FOREX_TRADE_SEVEN:// 限价
                            map.put(Forex.FOREX_EXCHANGERATE, exchangeRate);
                            map.put(Forex.FOREX_LIMITRATE_REQ, limitRate);
                            LogGloble.d(TAG + " 0", "7");
                            break;
                        case ConstantGloble.FOREX_TRADE_EIGHT:// 市价
                            map.put(Forex.FOREX_EXCHANGERATE, exchangeRate);
                            LogGloble.d(TAG + " 0", "8");
                            break;
                        case ConstantGloble.FOREX_TRADE_THREE:// 获利委托
                            map.put(Forex.FOREX_WINRATE_REQ, winRate);
                            map.put(Forex.FOREX_CONSIGNAYS_REQ, consignDays);
                            map.put(Forex.FOREX_CONSIGNHOUR_REQ, consignHour);
                            break;
                        case ConstantGloble.FOREX_TRADE_FOUR:// 止损委托
                            map.put(Forex.FOREX_LOSERATE_REQ, loseRate);
                            map.put(Forex.FOREX_CONSIGNAYS_REQ, consignDays);
                            map.put(Forex.FOREX_CONSIGNHOUR_REQ, consignHour);
                            break;
                        case ConstantGloble.FOREX_TRADE_FIVE:// 二选一委托
                            map.put(Forex.FOREX_WINRATE_REQ, winRate);
                            map.put(Forex.FOREX_LOSERATE_REQ, loseRate);
                            map.put(Forex.FOREX_CONSIGNAYS_REQ, consignDays);
                            map.put(Forex.FOREX_CONSIGNHOUR_REQ, consignHour);
                            break;
                        default:

                            break;
                    }

                } else {
                    map.put(Forex.FOREX_SAMOUNT_REQ, sAmount);
                    // 卖出
                    switch (type) {

                        case ConstantGloble.FOREX_TRADE_SEVEN:// 限价
                            map.put(Forex.FOREX_EXCHANGERATE, exchangeRate);
                            map.put(Forex.FOREX_LIMITRATE_REQ, limitRate);
                            LogGloble.d(TAG + " 1", "7");
                            break;
                        case ConstantGloble.FOREX_TRADE_EIGHT:// 市价
                            map.put(Forex.FOREX_EXCHANGERATE, exchangeRate);
                            LogGloble.d(TAG + " 1", "8");
                            break;
                        case ConstantGloble.FOREX_TRADE_THREE:// 获利委托
                            map.put(Forex.FOREX_WINRATE_REQ, winRate);
                            map.put(Forex.FOREX_CONSIGNAYS_REQ, consignDays);
                            map.put(Forex.FOREX_CONSIGNHOUR_REQ, consignHour);
                            break;
                        case ConstantGloble.FOREX_TRADE_FOUR:// 止损委托
                            map.put(Forex.FOREX_LOSERATE_REQ, loseRate);
                            map.put(Forex.FOREX_CONSIGNAYS_REQ, consignDays);
                            map.put(Forex.FOREX_CONSIGNHOUR_REQ, consignHour);
                            break;
                        case ConstantGloble.FOREX_TRADE_FIVE:// 二选一委托
                            map.put(Forex.FOREX_WINRATE_REQ, winRate);
                            map.put(Forex.FOREX_LOSERATE_REQ, loseRate);
                            map.put(Forex.FOREX_CONSIGNAYS_REQ, consignDays);
                            map.put(Forex.FOREX_CONSIGNHOUR_REQ, consignHour);
                            break;
                        case ConstantGloble.FOREX_TRADE_ELEVEN:// 追击止损委托
                            map.put(Forex.FOREX_LOSERATE_REQ, loseRate);
                            map.put(Forex.FOREX_CONSIGNAYS_REQ, consignDays);
                            map.put(Forex.FOREX_CONSIGNHOUR_REQ, consignHour);
                            break;
                        default:
                            break;
                    }
                }
                break;
        }
        getHttpTools().requestHttp(Forex.FOREX_PSNFOREXTRADE_API, "requestCurrencyPsnForexTradeCallback", map, true);
    }

    /** 活期-----外汇买卖确认---提交 */
    public void requestCurrencyPsnForexTradeCallback(Object resultObj) {

    }

    /**
     * 根据投资交易类型，查询交易账户
     */
    public void requestQueryInvtBindingInfo() {
        BaseHttpEngine.showProgressDialogCanGoBack();
        Map<String, Object> map = new Hashtable<String, Object>();
        map.put(Forex.FOREX_INVTTYPE_REQ, Forex.FOREX_TEN);
        getHttpTools().requestHttp(Forex.FOREX_QUERYINVTBINDINGINFO_API, "requestQueryInvtBindingInfoCallback", map, true);
    }

    /**
     * 根据投资交易类型，查询交易账户---回调
     *
     * @param resultObj
     */
    public void requestQueryInvtBindingInfoCallback(Object resultObj) {
    }

    /** 我的外汇--页面初始化时需判断是定期还是活期 */
    public void customerPsnForexActIsset() {
        getHttpTools().requestHttp(Forex.FOREX_RATE_ACTISSET, "customerPsnForexActIssetCallback", null, true);
    }

    /** 我的外汇--页面初始化时需判断是定期还是活期 ---回调 */
    public void customerPsnForexActIssetCallback(Object resultObj) {
    }

    /** 外汇自选 详情页面调用*/
    public void requestOptionalQuotes() {
        getHttpTools().requestHttpWithNoDialog(Forex.FOREX_CUSTOMER_RATE, "requestPsnCustomerRateCallback", null, true);
    }

    /** 在时间后面添加秒数 */
    public String dealTimes(String date) {
        String[] startStr = date.toString().trim().split(":");
        String hours = null;
        if (startStr.length == 2) {
            StringBuilder sb = new StringBuilder(date);
            sb.append(":");
            sb.append("00");
            hours = sb.toString().trim();
        }
        return hours;
    }

    /** 获取小时数 */
    public String getHour(String date) {
        String hours = null;
        String[] startStr = date.toString().trim().split(":");
        if (startStr.length > 1) {
            hours = startStr[0];
        }
        return hours;
    }

    public boolean httpRequestCallBackPre(Object resultObj) {
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        LogGloble.d(TAG, "异常----------");
        if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
            if (Forex.FOREX_PSNFOREXALLTRANSQUERY_API.equals(biiResponseBody.getMethod()) || Forex.FOREX_CUSTOMER_RATE.equals(biiResponseBody.getMethod()) || Forex.FOREX_ALLRATE.equals(biiResponseBody.getMethod())) {
                if (biiResponse.isBiiexception()) {// 代表返回数据异常
                    BiiHttpEngine.dissMissProgressDialog();
                    BiiError biiError = biiResponseBody.getError();
                    // 判断是否存在error
                    if (biiError != null) {
                        if (biiError.getCode() != null) {
                            if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
                                if (HttpManager.mPollingRequestThread != null && PollingRequestThread.pollingFlag) {
                                    LogGloble.e(TAG, "onPause() mPollingRequestThread  stopPolling()  ");
                                    HttpManager.stopPolling();
                                } // 要重新登录
                                showTimeOutDialog(biiError.getMessage());
                            } else {
                                // 非会话超时错误拦截
                                BaseDroidApp.getInstanse().createDialog("", biiError.getMessage(), new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        BaseDroidApp.getInstanse().dismissMessageDialog();

                                    }
                                });
                                return true;
                            }
                        }
                    }
                    return true;
                }
                return false;// 没有异常
            } else {
                return super.httpRequestCallBackPre(resultObj);
            }
        }
        // 随机数获取异常
        return super.httpRequestCallBackPre(resultObj);
    };

    /** 停止轮询 */
    public void stopPollingFlag() {
        if (HttpManager.mPollingRequestThread != null && PollingRequestThread.pollingFlag) {
            LogGloble.e(TAG, "onPause() mPollingRequestThread  stopPolling()  ");
            HttpManager.stopPolling();
        }
    }

    /**
     * 委托-----成交状况查询
     */
    public void requestPsnForexAllTransQuery1(String startDate, String endDate, String queryType, String pageSize, String currentIndex, String refresh) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Forex.FOREX_ENDDATE_REQ, endDate);
        map.put(Forex.FOREX_STARTDATE_REQ, startDate);
        map.put(Forex.FOREX_QUERYTYPE_REQ, queryType);
        map.put(Forex.FOREX_PAGESIZE_REQ, pageSize);
        map.put(Forex.FOREX_CURRENTINDEX_REQ, currentIndex);
        map.put(Forex.FOREX_REFRESH_REQ, refresh);
        if (currentOrHistory == 1) {
            // 当前
            getHttpTools().requestHttp(Forex.FOREX_PSNFOREXALLTRANSQUERY_API, "requestPsnCurrentTransQueryCallback", map, true);
        } else if (currentOrHistory == 2) {
            // 历史
            getHttpTools().requestHttp(Forex.FOREX_PSNFOREXALLTRANSQUERY_API, "requestPsnHistoryTransQueryCallback", map, true);
        }
    }

    /**
     * 查询单笔外汇行情
     *
     * @param bCurrency
     *            :买入货币
     * @param sCurrency
     *            :卖出货币
     */
    public void requestPsnForexQuerySingleRate(String bCurrency, String sCurrency) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Forex.FOREX_BCURRENCY_RES, bCurrency);
        map.put(Forex.FOREX_SCURRENCY_RES, sCurrency);
        getHttpTools().requestHttp(Forex.FOREX_PSNFOREXQUERYSINGLERATE_API, "requestPsnForexQuerySingleRateCallback", map, true);
    }

    /** 查询单笔外汇行情-----回调 */
    public void requestPsnForexQuerySingleRateCallback(Object resultObj) {
    }
    @Override
    protected void onResume() {
        super.onResume();
        boolean login = BaseDroidApp.getInstanse().isLogin();
        onResumeFromLogin(login);
    }


    public String getCurrencyPair(Map<String,Object> map){
        String currencyPair = "";
        String sourceCurrencyCode = (String) map.get(Forex.FOREX_RATE_SOURCECODE_RES);
        String targetCurrencyCode = (String) map.get(Forex.FOREX_RATE_TARGETCODE_RES);
        if (StringUtil.isNull(sourceCurrencyCode) && StringUtil.isNull(targetCurrencyCode)){
            return currencyPair;
        }
        return  currencyPair = sourceCurrencyCode + targetCurrencyCode;
    }

    /**  处理货币对，返回的货币对可能没有对应的名称，将其除去*/
    public List<Map<String, Object>> getTrueDate(List<Map<String, Object>> list) {
        int len = list.size();
        List<Map<String, Object>> dateList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < len; i++) {
            Map<String, Object> map = (Map<String, Object>) list.get(i);
            String sourceCurrencyCode = (String) map.get(Forex.FOREX_RATE_SOURCECODE_RES); // 得到源货币的代码
            String targetCurrencyCode = (String) map.get(Forex.FOREX_RATE_TARGETCODE_RES);
            if (!StringUtil.isNull(sourceCurrencyCode) && !StringUtil.isNull(targetCurrencyCode) &&
                    LocalData.Currency.containsKey(sourceCurrencyCode) && LocalData.Currency.containsKey
                    (targetCurrencyCode)) {
                dateList.add(map);
            }
        }
        return dateList;
    }

    /**
     * 提取支持的货币对
     * @param listMap 原货币对
     * @return 返回过滤后货币对
     */
    public List<Map<String,Object>> getCurrencyCodeList(List<Map<String,Object>> listMap){
        List<Map<String,Object>> listMapNew = new ArrayList<Map<String,Object>>();
        for (int i = 0; i < listMap.size(); i++) {
            Map<String,Object> map = listMap.get(i);
            String sourceCurrencyCode = (String) map.get(Forex.FOREX_RATE_SOURCECODE_RES);
            String targetCurrencyCode = (String) map.get(Forex.FOREX_RATE_TARGETCODE_RES);
            String code = sourceCurrencyCode+targetCurrencyCode;
            // 外汇只支持以下币种
            if (StringUtil.isNullOrEmpty(code) || LocalData.ForeignCurrencyCodeList.contains(code)) {
                listMapNew.add(map);
            }
        }
        return listMapNew;
    }

    /** 获取到请求是的货币对信息*/
    public List<String> getCcygrpList(List<Map<String,Object>> CcygrpList){
        List<String> list = new ArrayList<String>();
        for (int i=0;i<CcygrpList.size();i++){
            Map<String,Object> map = CcygrpList.get(i);
            String sourceCurrencyCode = (String) map.get(Forex.FOREX_RATE_SOURCECODE_RES);
            String targetCurrencyCode = (String) map.get(Forex.FOREX_RATE_TARGETCODE_RES);
            String code = sourceCurrencyCode+targetCurrencyCode;
            list.add(code);
        }
        return list;
    }

    /** 获取到请求是的货币对信息*/
    public Boolean getCurrency(Map<String,Object> map){
            String sourceCurrencyCode = (String) map.get(Forex.FOREX_RATE_SOURCECODE_RES);
            String targetCurrencyCode = (String) map.get(Forex.FOREX_RATE_TARGETCODE_RES);
//            String selectBuyRate = (String) map.get(Forex.FOREX_RATE_BUYRATE_RES); // 买入价
//            String selectSellRate = (String) map.get(Forex.FOREX_RATE_SELLRATE_RES); // 卖出价
        if (sourceCurrencyCode.equals("027") || targetCurrencyCode.equals("027")) {
            return true;
        }
        return false;
    }
}
