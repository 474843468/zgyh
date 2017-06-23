package com.chinamworld.bocmbci.biz.lsforex;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.abstracttools.BaseHttpManager;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.NewStyleBaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.base.application.CommonApplication;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.invest.activity.InvesAgreeActivity;
import com.chinamworld.bocmbci.biz.lsforex.acc.IsForexSettingBindAccActivity;
import com.chinamworld.bocmbci.biz.lsforex.manageacc.IsForexProduceRadeActivity;
import com.chinamworld.bocmbci.biz.lsforex.rate.IsForexTwoWayTreasureNewActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.ErrorCode;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.http.thread.PollingRequestThread;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.ActivityIntentTools;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zxj on 2016/10/20.
 */
public class IsForexBaseNewActivity extends NewStyleBaseActivity{

    public static final String TAG = "IsForexBaseNewActivity";
    /** 登陆后的ConversationId */
    public String commConversationId = null;
    /** isOpen:是否开通投资理财服务 */
    public static boolean isOpen = false;
    /** 双向宝交易账户----是否是判断条件 */
    public boolean isCondition = false;
    /** 是否设置外汇双向宝账户 */
    public static boolean isSettingAcc = false;
    /** 是否签约保证金 */
    public static boolean isSign = false;
    /** 全部货币对 list */
    public List<Map<String, Object>> codeResultList = null;
    /** 1-我的双向宝，2-保证金交易，3-交易状况查询 4-双向宝行情,5-签约管理 ,6-快速交易,8交易购买(功能位置添加)*/
    public int taskTag = 1;
    /** 用于区别是我的外汇汇率还是交易发出的请求1-交易，2-我的外汇汇率 3-全部汇率 */
    public int customerOrTrade = 1;
    /** 1-已开通投资理财查询签约产品，2-先开投资理财，在查询签约产品 */
    public static int searchBaillAfterOpen = 1;
    private View moneyButtonView = null;
    private View moneOpenyButtonView = null;
    private View signNoButtonView = null;
    private View signOpenButtonView = null;
    private View accNoButtonView = null;
    private View accOpenButtonView = null;
    /** 任务提示框顶部TextView */
    private TextView popTopText = null;
    /** 结算币种代码 list */
    public List<String> vfgRegCurrencyList = null;
    //快速交易  点击完成 回退的页面 0 行情页面 1是详情页面 其他情况会退到主页面
    public static int isFlagGoWay = -1;
    /** 交易状况查询是否完成任务提示框 false-没有，true-是 */
    public boolean isSearchFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
    }
    @Override
    public ActivityTaskType getActivityTaskType() {
        // TODO Auto-generated method stub
        return ActivityTaskType.TwoTask;
    }
    /**
     * 判断是否开通投资理财服务--二级菜单
     */
    public void requestPsnInvestmentManageIsOpen() {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_API);
        biiRequestBody.setConversationId(commConversationId);
        biiRequestBody.setParams(null);
        HttpManager.requestBii(biiRequestBody, this, "requestMenuIsOpenCallback");
    }
    /** 判断是否开通投资理财服务--二级菜单----回调 */
    public void requestMenuIsOpenCallback(Object resultObj) {
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        // 得到response
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        // 得到result
        String isOpenOr = (String) biiResponseBody.getResult();
        // isOpen
        if ("false".equals(isOpenOr)) {
            isOpen = false;
        } else {
            isOpen = true;
        }
    }

    /** 查询保证金账户列表（签约产品列表）----任务提示框 */
    public void requestPsnVFGBailListQueryCondition() {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGBAILLISTQUERY_API);
        biiRequestBody.setConversationId(commConversationId);
        HttpManager.requestBii(biiRequestBody, this, "requestPsnVFGBailListQueryConditionCallback");
    }
    /** 查询保证金账户列表（签约产品列表）----任务提示框--------回调 */
    public void requestPsnVFGBailListQueryConditionCallback(Object resultObj) {
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        // 得到response
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        List<Map<String, String>> result = (List<Map<String, String>>) biiResponseBody.getResult();
        BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_RESULT_SIGN, result);
    }

    /**
     * 双向宝账户信息
     *
     * 判断是否设置外汇双向宝账户 --------二级菜单
     */
    public void requestPsnVFGGetBindAccount() {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGGETBINDACCOUNT_KEY);
        biiRequestBody.setConversationId(commConversationId);
        biiRequestBody.setParams(null);
        if (isCondition) {
            // 双向宝交易账户回调----做为条件
            HttpManager.requestBii(biiRequestBody, this, "requestConditionAccountCallback");
        } else {
            // 双向宝交易账户回调----不做为条件
            HttpManager.requestBii(biiRequestBody, this, "requestMenuAccountCallback");
        }

    }

    /**
     * 双向宝账户信息
     *
     * 判断是否设置外汇双向宝账户 --------二级菜单----回调
     */
    public void requestConditionAccountCallback(Object resultObj) {
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        // 得到response
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        Map<String, String> accReaultMap = (Map<String, String>) biiResponseBody.getResult();
        if (StringUtil.isNullOrEmpty(accReaultMap)) {
            isSettingAcc = false;
            isSign = false;
            return;
        } else {
            String accountNumber = accReaultMap.get(IsForex.ISFOREX_ACCOUNTNUMBER_REQ);
            if (StringUtil.isNull(accountNumber)) {
                isSettingAcc = false;
                isSign = false;
            } else {
                isSettingAcc = true;
                isSign = true;
                BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_ACCREAULTMAP, accReaultMap);
            }
        }

    }

    /** 查询全部汇率----货币对 */
    public void requestPsnVFGGetAllRate(String vfgType) {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        Map<String, String> params = new HashMap<String, String>();
        params.put("vfgType", vfgType);
        biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGGETALLRATE_API);
        biiRequestBody.setConversationId(commConversationId);
        biiRequestBody.setParams(params);
        HttpManager.requestBii(biiRequestBody, this, "requestPsnVFGGetAllRateCallback");
    }

    /** 查询全部汇率----货币对-----回调 */
    public void requestPsnVFGGetAllRateCallback(Object resultObj) {
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        // 得到response
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        codeResultList = (List<Map<String, Object>>) biiResponseBody.getResult();
        BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_CODERESULTLIST_KEY, codeResultList);
    }
    /** 客户定制汇率查询 双向宝*/
    public void requestPsnVFGCustomerSetRate() {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGCUSTOMERSETRATE_API);
        biiRequestBody.setConversationId(commConversationId);
        biiRequestBody.setParams(null);
        HttpManager.requestBii(biiRequestBody, this, "requestPsnVFGCustomerSetRateCallback");
    }

    /** 客户定制汇率查询---回调 */
    public void requestPsnVFGCustomerSetRateCallback(Object resultObj) {

    }
    public void requestSelfCustomer(){
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGCUSTOMERSETRATE_API);
        biiRequestBody.setConversationId(commConversationId);
        biiRequestBody.setParams(null);
        HttpManager.requestBii(biiRequestBody, this, "requestSelfCustomerCallback");
    }
    public void requestSelfCustomerCallback(Object resultObj){

    }

    /** 客户定制汇率查询   双向宝*/
    public void requestCustomerSetRate() {//requestPsnVFGCustomerSetRate
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGCUSTOMERSETRATE_API);
        biiRequestBody.setConversationId(commConversationId);
        biiRequestBody.setParams(null);
        HttpManager.requestBii(biiRequestBody, this, "requestCustomerSetRateCallback");
    }

    /** 客户定制汇率查询---回调 */
    public void requestCustomerSetRateCallback(Object resultObj) {

    }

    /** 双向宝持仓信息 */
    public void requestPsnVFGPositionInfo() {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGPOSITIONINFO_KEY);
        biiRequestBody.setConversationId(commConversationId);
        biiRequestBody.setParams(null);
        HttpManager.requestBii(biiRequestBody, this, "requestPsnVFGPositionInfoCallbank");
    }

    /** 双向宝持仓信息----回调 */
    public void requestPsnVFGPositionInfoCallbank(Object resultObj) {

    }

    /** 查询全部汇率----货币对---快速交易 *///PsnVFGGetAllRat  PsnVFGGetAllRate
    public void requestAllRate(String vfgType) {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        Map<String, String> params = new HashMap<String, String>();
        params.put("vfgType", vfgType);
        biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGGETALLRATE_API);
        biiRequestBody.setConversationId(commConversationId);
        biiRequestBody.setParams(params);
        HttpManager.requestBii(biiRequestBody, this, "requestAllRateCallback");
    }

    /** 查询全部汇率----货币对----快速交易----回调 */
    public void requestAllRateCallback(Object resultObj) {
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        // 得到response
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        codeResultList = (List<Map<String, Object>>) biiResponseBody.getResult();
        BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_CODERESULTLIST_KEY, codeResultList);
    }

    /** 获得结算币种 */
    public void requestPsnVFGGetRegCurrency(String vfgType) {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGGETREGCURRENCY_KRY);
        biiRequestBody.setConversationId(commConversationId);
        Map<String, String> params = new HashMap<String, String>();
        params.put("vfgType", vfgType);
        biiRequestBody.setParams(params);
        HttpManager.requestBii(biiRequestBody, this, "requestPsnVFGGetRegCurrencyCallback");
    }

    /** 获得结算币种----回调 */
    public void requestPsnVFGGetRegCurrencyCallback(Object resultObj) {
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        // 得到response
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        if (vfgRegCurrencyList != null && vfgRegCurrencyList.size() > 0) {
            vfgRegCurrencyList.clear();
        }
        vfgRegCurrencyList = (List<String>) biiResponseBody.getResult();
        BaseDroidApp.getInstanse().getBizDataMap()
                .put(ConstantGloble.ISFOREX_VFGREGCURRENCYLISTTLIST_KEY, vfgRegCurrencyList);
    }

    public void getPop() {
        View popupView = LayoutInflater.from(this).inflate(R.layout.isforex_task_notify, null);
        ImageView taskPopCloseButton = (ImageView) popupView.findViewById(R.id.top_right_close);
        // moneyButtonView:理财服务按钮
        moneyButtonView = popupView.findViewById(R.id.forex_money_button_show);
        moneOpenyButtonView = popupView.findViewById(R.id.forex_money_text_hide);
        signNoButtonView = popupView.findViewById(R.id.forex_sign_button_show);
        signOpenButtonView = popupView.findViewById(R.id.forex_sign_text_hide);
        accNoButtonView = popupView.findViewById(R.id.forex_acc_button_show);
        accOpenButtonView = popupView.findViewById(R.id.forex_acc_text_hide);
        popTopText = (TextView) popupView.findViewById(R.id.tv_acc_account_accountState);
        if (searchBaillAfterOpen == 2) {
            // 未开通投资理财服务
            popTopText.setText(getResources().getString(R.string.isForex_task_only_open_title));
            moneyButtonView.setVisibility(View.VISIBLE);
            moneOpenyButtonView.setVisibility(View.GONE);
            signNoButtonView.setVisibility(View.INVISIBLE);
            signOpenButtonView.setVisibility(View.GONE);
            accNoButtonView.setVisibility(View.INVISIBLE);
            accOpenButtonView.setVisibility(View.GONE);
        } else if (searchBaillAfterOpen == 1) {
            // 已开通投资理财服务
            popTopText.setText(getResources().getString(R.string.isForex_task_title));
            if (isOpen) {
                // 不显示开通投资理财
                moneyButtonView.setVisibility(View.GONE);
                moneOpenyButtonView.setVisibility(View.GONE);
                if (isSign) {
                    // 签约成功
                    signNoButtonView.setVisibility(View.GONE);
                    signOpenButtonView.setVisibility(View.VISIBLE);
                    // 判断是否设置账户
                    if (isSettingAcc) {
                        // 设置账户
                        accNoButtonView.setVisibility(View.GONE);
                        accOpenButtonView.setVisibility(View.VISIBLE);
                    } else {
                        // 未设置账户
                        accNoButtonView.setVisibility(View.VISIBLE);
                        accOpenButtonView.setVisibility(View.GONE);
                    }
                } else {
                    // 未签约成功,必须未设置账户
                    signNoButtonView.setVisibility(View.VISIBLE);
                    signOpenButtonView.setVisibility(View.GONE);
                    accNoButtonView.setVisibility(View.VISIBLE);
                    accOpenButtonView.setVisibility(View.GONE);
                }

            }
        }

        // 开通投资理财
        moneyButtonView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isOpen) {
                    // 跳转到投资理财服务协议页面
                    Intent gotoIntent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(), InvesAgreeActivity.class);
                    startActivityForResult(gotoIntent, ConstantGloble.ACTIVITY_REQUEST_MANAGE_CODE);
                }

            }
        });
        // 签约账户
        signNoButtonView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isOpen) {
                    if (!isSign) {
                        // 跳转到签约协议页面
                        Intent gotoIntent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(),
                                IsForexProduceRadeActivity.class);
                        startActivityForResult(gotoIntent, ConstantGloble.ACTIVITY_REQUEST_SETPRMSACC_CODE);// 签约协议页面
                    }
                } else {
                    // 没有开通投资理财，签约账户
                    CustomDialog.toastInCenter(BaseDroidApp.getInstanse().getCurrentAct(), BaseDroidApp.getInstanse()
                            .getCurrentAct().getString(R.string.bocinvt_task_toast_1));
                }
            }
        });
        // 登记账户
        accNoButtonView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isOpen && isSign) {
                    if (!isSettingAcc) {
                        // 跳转到登记账户页面
                        Intent gotoIntent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(),
                                IsForexSettingBindAccActivity.class);
                        startActivityForResult(gotoIntent, ConstantGloble.ACTIVITY_REQUEST_INVTBINDING_CODE);// 登记账户
                    }
                } else {
                    // 必须先完成以上两个任务，才可以登记账户
                    CustomDialog.toastInCenter(BaseDroidApp.getInstanse().getCurrentAct(), BaseDroidApp.getInstanse()
                            .getCurrentAct().getString(R.string.isForex_task_toast_2));
                }
            }
        });
        // 右上角的关闭按钮
        taskPopCloseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 强制关闭任务提示框，返回到九宫格
                BaseDroidApp.getInstanse().dismissMessageDialogFore();
//				ActivityTaskManager.getInstance().removeAllActivity();
                ActivityTaskManager.getInstance().removeAllSecondActivity();
//				Intent intent = new Intent();
//				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), MainActivity.class);
//				BaseDroidApp.getInstanse().getCurrentAct().startActivity(intent);
                finish();
            }
        });
        BaseDroidApp.getInstanse().showForexMessageDialog(popupView);
    }

    protected void requestPsnAssetBalanceQuery() {

        Map<String, Object> parms_map=new HashMap<String, Object>();
        getHttpTools().requestHttpWithNoDialog("PsnAssetBalanceQuery", "requestPsnAssetBalanceQueryCallBack", parms_map, false);
    }

    public void requestPsnAssetBalanceQueryCallBack(Object resultObj){

    }
    public static String parseStringPattern(String text, int scale) {
        if(text != null && !"".equals(text) && !"null".equals(text)) {
            if(!text.contains(",") && !text.contains("，")) {
                if(text.matches("^.*[.].*[.].*$")) {
                    return text;
                } else {
                    String temp = "#######################0";
                    if(scale > 0) {
                        temp = temp + ".";
                    }

                    for(int e = 0; e < scale; ++e) {
                        temp = temp + "0";
                    }

                    try {
                        DecimalFormat var6 = new DecimalFormat(temp);
                        BigDecimal d = new BigDecimal(text);
                        return var6.format(d).toString();
                    } catch (Exception var5) {
                        return text;
                    }
                }
            } else {
                return text;
            }
        } else {
            return "-";
        }
    }
    public void requestisQrcodeCommConversationId() {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod("PSNCreatConversation");
        BaseHttpManager.Instance.requestBii(biiRequestBody, this, "requestisQrcodeCommConversationIdCallBack");
    }
    public void requestisQrcodeCommConversationIdCallBack(Object resultObj) {
        BiiResponse biiResponse = (BiiResponse)resultObj;
        List biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = (BiiResponseBody)biiResponseBodys.get(0);
        String commConversationId = (String)biiResponseBody.getResult();
        CommonApplication.getInstance().getBizDataMap().put("conversationId", commConversationId);
        this.callHttpCallBack(resultObj);
    }
    /** 查询保证金账户列表 */
    public void requestPsnVFGBailListQuery() {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGBAILLISTQUERY_API);
        biiRequestBody.setConversationId(commConversationId);
        HttpManager.requestBii(biiRequestBody, this, "requestPsnVFGBailListQueryCallback");
    }

    /** 查询保证金账户列表--------回调 */
    public void requestPsnVFGBailListQueryCallback(Object resultObj) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {

            case RESULT_OK:// 成功
                switch (requestCode) {
                    case ConstantGloble.ACTIVITY_REQUEST_MANAGE_CODE:// 开通投资理财3
                        moneyButtonView.setVisibility(View.GONE);
                        moneOpenyButtonView.setVisibility(View.GONE);
                        isOpen = true;
                        if (searchBaillAfterOpen == 1) {
                            // 已经开通投资理财
                            if (isSign) {
                                // 已签约
                                signNoButtonView.setVisibility(View.GONE);
                                signOpenButtonView.setVisibility(View.VISIBLE);
                                // 判断是否设置账户
                                if (isSettingAcc) {
                                    // 设置账户
                                    accNoButtonView.setVisibility(View.GONE);
                                    accOpenButtonView.setVisibility(View.VISIBLE);
                                    BaseDroidApp.getInstanse().dismissMessageDialogFore();
                                    BaseHttpEngine.showProgressDialog();
                                    switch (taskTag) {
                                        case 1:// 我的双向宝
                                            // 查询双向宝持仓信息
                                            requestPsnVFGPositionInfo();
                                            break;
                                        case 2:// 保证金交易
                                            // 查询双向宝交易账户信息
                                            isCondition = false;
                                            requestPsnVFGGetBindAccount();
                                            break;
                                        case 3:// 交易状况查询
                                            isSearchFinish = true;
                                            requestCommConversationId();
                                            break;
                                        case 4:// 双向宝行情
                                            requestPsnVFGCustomerSetRate();
                                        case 5:// 签约
                                            // 已做完任务提示，查询保证金账户列表
                                            requestPsnVFGBailListQuery();
                                            break;
                                        case 6:// 双向宝行情
                                            BaseDroidApp.getInstanse().dismissMessageDialogFore();
                                            BaseHttpEngine.showProgressDialog();
                                            requestAllRate("");
                                            break;

                                        default:
                                            break;
                                    }
                                } else {
                                    // 未设置账户
                                    accNoButtonView.setVisibility(View.VISIBLE);
                                    accOpenButtonView.setVisibility(View.GONE);
                                }
                            } else {
                                // 未签约,未登记
                                signNoButtonView.setVisibility(View.VISIBLE);
                                signOpenButtonView.setVisibility(View.GONE);
                                accNoButtonView.setVisibility(View.VISIBLE);
                                accOpenButtonView.setVisibility(View.GONE);

                            }
                        } else if (searchBaillAfterOpen == 2) {
                            BaseDroidApp.getInstanse().dismissMessageDialogFore();
                            searchBaillAfterOpen = 1;
                            // 查询签约产品，弹出任务提示框
                            BaseHttpEngine.showProgressDialog();
                            requestPsnVFGBailListQueryCondition();
                        }

                        break;
                    case ConstantGloble.ACTIVITY_REQUEST_SETPRMSACC_CODE:// 签约协议页面4
                        isOpen = true;
                        isSign = true;
                        searchBaillAfterOpen = 1;
                        signNoButtonView.setVisibility(View.GONE);
                        signOpenButtonView.setVisibility(View.VISIBLE);
                        // 判断是否设置账户
                        if (isSettingAcc) {
                            // 设置账户
                            accNoButtonView.setVisibility(View.GONE);
                            accOpenButtonView.setVisibility(View.VISIBLE);
                            BaseDroidApp.getInstanse().dismissMessageDialogFore();
                            BaseHttpEngine.showProgressDialog();
                            switch (taskTag) {
                                case 1:// 我的双向宝
                                    // 查询双向宝持仓信息
                                    requestPsnVFGPositionInfo();
                                    break;
                                case 2:// 保证金交易
                                    // 查询双向宝交易账户信息
                                    isCondition = false;
                                    requestPsnVFGGetBindAccount();
                                    break;
                                case 3:// 交易状况查询
                                    isSearchFinish = true;
                                    requestCommConversationId();
                                    break;
                                case 4:// 双向宝行情
                                    requestPsnVFGCustomerSetRate();
                                    break;
                                case 5:// 签约
                                    // 已做完任务提示，查询保证金账户列表
                                    requestPsnVFGBailListQuery();
                                    break;

                                default:
                                    break;
                            }
                        } else {
                            // 未设置账户
                            accNoButtonView.setVisibility(View.VISIBLE);
                            accOpenButtonView.setVisibility(View.GONE);
                        }

                        break;
                    case ConstantGloble.ACTIVITY_REQUEST_INVTBINDING_CODE:// 登记账户5
                        searchBaillAfterOpen = 1;
                        isOpen = true;
                        isSign = true;
                        isSettingAcc = true;
                        BaseDroidApp.getInstanse().dismissMessageDialogFore();
                        BaseHttpEngine.showProgressDialog();
                        switch (taskTag) {
                            case 1:// 我的双向宝
                                // 查询双向宝持仓信息
                                requestPsnVFGPositionInfo();
                                break;
                            case 2:// 保证金交易
                                // 查询双向宝交易账户信息
                                isCondition = false;
                                requestPsnVFGGetBindAccount();
                                break;
                            case 3:// 交易状况查询
                                isSearchFinish = true;
                                requestCommConversationId();
                                break;
                            case 4:// 双向宝行情
                                requestPsnVFGCustomerSetRate();
                                break;
                            case 5:// 签约
                                // 已做完任务提示，查询保证金账户列表
                                requestPsnVFGBailListQuery();
                                break;

                            default:
                                break;
                        }

                        break;
                    default:
                        break;
                }

                break;
            case RESULT_CANCELED:// 失败

                break;
            default:
                break;
        }
    }

    public boolean httpRequestCallBackPre(Object resultObj) {
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
            if (IsForex.ISFOREX_PSNVFGGETALLRATE_API.equals(biiResponseBody.getMethod())
                    || IsForex.ISFOREX_PSNVFGCUSTOMERSETRATE_API.equals(biiResponseBody.getMethod())) {
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
                                BaseDroidApp.getInstanse().createDialog("", biiError.getMessage(),
                                        new View.OnClickListener() {

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
                return false;// 没有异常------查询异常
            } else if (IsForex.ISFOREX_PSNVFGBAILLISTQUERY_API.equals(biiResponseBody.getMethod())) {
                // 没有可签约保证金产品
                // 返回的是错误码
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
                            } else if (ErrorCode.ISFOREX_NO_SIGN.equals(biiError.getCode())) {
                                isSign = false;
                                isSettingAcc = false;
                                BaseHttpEngine.dissMissProgressDialog();
                                getPop();
                                return true;
                            } else {
                                // 查询版的用户
                                BaseHttpEngine.dissMissProgressDialog();
                                BaseDroidApp.getInstanse().createDialog("", biiError.getMessage(),
                                        new View.OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                BaseDroidApp.getInstanse().dismissMessageDialog();
//												ActivityTaskManager.getInstance().removeAllSecondActivity();
//												Intent intent = new Intent();
//												intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
//														SecondMainActivity.class);
//												startActivity(intent);
                                                goToMainActivity();
                                                finish();

                                            }
                                        });

                            }
                        }
                    }
                    return true;
                }
                return false;// 没有异常
            } else {
                return super.httpRequestCallBackPre(resultObj);
            }
        }// 返回错误码
        // 随机数获取异常
        return super.httpRequestCallBackPre(resultObj);
    };

    /**
     * 跳转到双向宝首页
     */
    public static void intent_to_IsForexTwoWayTreasureNewActivity(Context context){
        ActivityIntentTools.intentToActivity(context, IsForexTwoWayTreasureNewActivity.class);
    }
}
