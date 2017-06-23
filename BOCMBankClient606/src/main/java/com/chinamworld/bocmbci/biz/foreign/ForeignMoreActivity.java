package com.chinamworld.bocmbci.biz.foreign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Forex;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.userwidget.investview.InvestHelpActivity;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.Map;

/**
 * 外汇更多 @see ForeignMoreActivity
 * @author luqp 2016年9月22日
 */
public class ForeignMoreActivity extends ForeignBaseActivity implements View.OnClickListener{
    private final String TAG = "ForeignMoreActivity";
    /** 快速交易*/
    private RelativeLayout fastTradeRl;
    /** 我的外汇*/
    private RelativeLayout myForeignExchangeRl;
    /** 成交状况查询*/
    private RelativeLayout transactionStatusQueryRl;
    /** 委托状况查询*/
    private RelativeLayout commissionedStatusQueryRl;
    /** 帮助*/
    private RelativeLayout helpRl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foreign_more);
        setTitle("更多");
        fastTradeRl = (RelativeLayout) findViewById(R.id.rl_fast_trade);
        myForeignExchangeRl = (RelativeLayout) findViewById(R.id.rl_my_foreign_exchange);
        transactionStatusQueryRl = (RelativeLayout) findViewById(R.id.rl_transaction_status_query);
        commissionedStatusQueryRl = (RelativeLayout) findViewById(R.id.rl_commissioned_status_query);
        helpRl = (RelativeLayout) findViewById(R.id.rl_help);

        /** 返回按钮点击事件*/
        getBackGroundLayout().setOnLeftButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fastTradeRl.setOnClickListener(this);
        myForeignExchangeRl.setOnClickListener(this);
        transactionStatusQueryRl.setOnClickListener(this);
        commissionedStatusQueryRl.setOnClickListener(this);
        helpRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_fast_trade: //快速交易
                LogGloble.i(TAG,"点击快速交易......");
                taskTag = 1;
                menuOrTrade = 1;
                if (BaseDroidApp.getInstanse().isLogin()) { // 已登录显示
                    commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
                    BaseHttpEngine.showProgressDialogCanGoBack();
                    if (StringUtil.isNull(commConversationId)) {
                        requestCommConversationId();  // 请求登录后的CommConversationId
                    } else {
                        requestPsnInvestmentManageIsOpen(); // 判断用户是否开通投资理财服务
                    }
                } else {
                    BaseActivity.getLoginUtils(ForeignMoreActivity.this).exe(new LoginTask.LoginCallback() { // 登陆跳转
                        @Override
                        public void loginStatua(boolean b) {
                            commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
                            BaseHttpEngine.showProgressDialogCanGoBack();
                            if (StringUtil.isNull(commConversationId)) {
                                requestCommConversationId();  // 请求登录后的CommConversationId
                            } else {
                                requestPsnInvestmentManageIsOpen(); // 判断用户是否开通投资理财服务
                            }
                        }
                    });
                }
                break;
            case R.id.rl_my_foreign_exchange: //我的外汇
                LogGloble.i(TAG,"点击我的外汇......");
                taskTag = 2;
                menuOrTrade = 2;
                if (BaseDroidApp.getInstanse().isLogin()) { // 已登录显示
                    commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
                    BaseHttpEngine.showProgressDialogCanGoBack();
                    if (StringUtil.isNull(commConversationId)) {
                        requestCommConversationId();  // 请求登录后的CommConversationId
                    } else {
                        requestPsnInvestmentManageIsOpen(); // 判断用户是否开通投资理财服务
                    }
                } else {
                    BaseActivity.getLoginUtils(ForeignMoreActivity.this).exe(new LoginTask.LoginCallback() { // 登陆跳转
                        @Override
                        public void loginStatua(boolean b) {
                            commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
                            BaseHttpEngine.showProgressDialogCanGoBack();
                            if (StringUtil.isNull(commConversationId)) {
                                requestCommConversationId();  // 请求登录后的CommConversationId
                            } else {
                                requestPsnInvestmentManageIsOpen(); // 判断用户是否开通投资理财服务
                            }
                        }
                    });
                }
                break;
            case R.id.rl_transaction_status_query: //成交状况查询
                LogGloble.i(TAG,"点击成交状况查询......");
                taskTag = 3;
                menuOrTrade = 2;
                if (BaseDroidApp.getInstanse().isLogin()) { // 已登录显示
                    commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
                    BaseHttpEngine.showProgressDialogCanGoBack();
                    if (StringUtil.isNull(commConversationId)) {
                        requestCommConversationId();  // 请求登录后的CommConversationId
                    } else {
                        requestPsnInvestmentManageIsOpen(); // 判断用户是否开通投资理财服务
                    }
                } else {
                    BaseActivity.getLoginUtils(ForeignMoreActivity.this).exe(new LoginTask.LoginCallback() { // 登陆跳转
                        @Override
                        public void loginStatua(boolean b) {
                            commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
                            BaseHttpEngine.showProgressDialogCanGoBack();
                            if (StringUtil.isNull(commConversationId)) {
                                requestCommConversationId();  // 请求登录后的CommConversationId
                            } else {
                                requestPsnInvestmentManageIsOpen(); // 判断用户是否开通投资理财服务
                            }
                        }
                    });
                }
                break;
            case R.id.rl_commissioned_status_query: //委托状况查询
                LogGloble.i(TAG,"委托状况查询......");
                taskTag = 7;
                menuOrTrade = 2;
                if (BaseDroidApp.getInstanse().isLogin()) { // 已登录显示
                    commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
                    BaseHttpEngine.showProgressDialogCanGoBack();
                    if (StringUtil.isNull(commConversationId)) {
                        requestCommConversationId();  // 请求登录后的CommConversationId
                    } else {
                        requestPsnInvestmentManageIsOpen(); // 判断用户是否开通投资理财服务
                    }
                } else {
                    BaseActivity.getLoginUtils(ForeignMoreActivity.this).exe(new LoginTask.LoginCallback() { // 登陆跳转
                        @Override
                        public void loginStatua(boolean b) {
                            commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
                            BaseHttpEngine.showProgressDialogCanGoBack();
                            if (StringUtil.isNull(commConversationId)) {
                                requestCommConversationId();  // 请求登录后的CommConversationId
                            } else {
                                requestPsnInvestmentManageIsOpen(); // 判断用户是否开通投资理财服务
                            }
                        }
                    });
                }
                break;
            case R.id.rl_help: //帮助
                Intent intent = new Intent();
                intent.setClass(ForeignMoreActivity.this,ForeignHelp.class);
                startActivity(intent);
//                InvestHelpActivity.showHelpMessage(ForeignMoreActivity.this,getString(R.string.foregin_help_message));
//                finish();

                break;
            default:
                break;
        }
    }

    @Override
    public void requestCommConversationIdCallBack(Object resultObj) {
        super.requestCommConversationIdCallBack(resultObj);
        commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
        if (StringUtil.isNullOrEmpty(commConversationId)) {
            BaseHttpEngine.dissMissProgressDialog();
            return;
        } else {  // 判断用户是否开通投资理财服务
            requestPsnInvestmentManageIsOpen();
        }
    }

    /**
     * 判断是否开通投资理财服务---回调
     * @param resultObj
     */
    public void requestPsnInvestmentManageIsOpenCallback(Object resultObj) {
        super.requestPsnInvestmentManageIsOpenCallback(resultObj);
        String isOpenOr = getHttpTools().getResponseResult(resultObj);
        // isOpen
        if ("false".equals(isOpenOr)) {
            isOpen = false;
        } else {
            isOpen = true;
        }
        requestPsnForexActIsset();  // 设定账户的请求
    }

    /**
     * 交易条件
     * @任务提示框----判断是否设置账户
     * @param resultObj
     *            :返回结果
     */
    @SuppressWarnings("unchecked")
    public void requestPsnForexActIssetCallback(Object resultObj) {
        Map<String, Object> result = getHttpTools().getResponseResult(resultObj);
        if (StringUtil.isNullOrEmpty(result)) {
            BaseHttpEngine.dissMissProgressDialog();
            isSettingAcc = false;
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
        if (!isOpen || !isSettingAcc) {
            BaseHttpEngine.dissMissProgressDialog();
            getPopup();
            return;
        }
        if (isOpen && isSettingAcc) {
            switch (taskTag) {
                case 1:// 快速交易
                    requestTag = 0;
                    customerOrTrade = 1;
                    quickOrRateSell = 1;
                    menuOrTrade = 1;
                    ratePsnForexActIsset();
                    break;
                case 5:// 我的外汇汇率
                    requestPsnCustomerRate();
                    break;
                case 4:// 外汇行情初始化时使用
                    // 查询我的外汇汇率信息
                    requestInitPsnCustomerRate();
                    break;
                case 6:// 全部汇率
                    requestPsnAllRates();
                    break;
                case 8:// 交易购买(功能位置添加)
                    requestPsnCustomerRate();
                    ratePsnForexActIsset();
                    break;
                default:
                    break;
            }
        }
    }
}
