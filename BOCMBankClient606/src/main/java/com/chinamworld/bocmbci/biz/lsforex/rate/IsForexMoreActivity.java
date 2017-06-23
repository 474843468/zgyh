package com.chinamworld.bocmbci.biz.lsforex.rate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseNewActivity;
import com.chinamworld.bocmbci.biz.lsforex.bail.IsForexBailInfoActivity;
import com.chinamworld.bocmbci.biz.lsforex.manageacc.IsForexBailProduceActivity;
import com.chinamworld.bocmbci.biz.lsforex.myrate.IsForexMyRateInfoActivity;
import com.chinamworld.bocmbci.biz.lsforex.query.IsForexQueryMenuActivity;
import com.chinamworld.bocmbci.biz.lsforex.trade.IsForexTradeSubmitActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.userwidget.investview.InvestHelpActivity;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zxj on 2016/10/18.
 * 更多页面
 */
public class IsForexMoreActivity extends IsForexBaseNewActivity implements View.OnClickListener {
    //快速交易
    private RelativeLayout way_fast_layout;
    //双向宝账户
    private RelativeLayout way_account_layout;
    //签约管理
    private RelativeLayout contract_layout;
    //交易查询
    private RelativeLayout query_layout;
    //保证金存入、转出
    private RelativeLayout margin_layout;
    //撤单
    private RelativeLayout withdrawable_layout;
    //未登录 点击 标识
    private int btnly = -1;
    /**返回按钮*/
    private TextView isforex_ib_back;
//    /** 处理后---源货币对代码 */
//    private List<String> sourceCodeDealCodeList = null;
//    /** 处理后---目标货币对代码 */
//    private List<String> targetCodeDealCodeList = null;
    /** 处理后---货币对代码名称 */
    private List<String> codeDealCodeNameList = null;
    /** 处理后---货币对代码 */
    private List<String> codeDealCodeList = null;
    private String vfgType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.isforex_more);
        setTitle("更多");
        initView();
        initClickListener();
    }
    private void initView(){
        way_fast_layout = (RelativeLayout)findViewById(R.id.way_fast_layout);
        way_account_layout = (RelativeLayout)findViewById(R.id.way_account_layout);
        contract_layout = (RelativeLayout)findViewById(R.id.contract_layout);
        query_layout = (RelativeLayout)findViewById(R.id.query_layout);
        margin_layout = (RelativeLayout)findViewById(R.id.margin_layout);
        withdrawable_layout = (RelativeLayout)findViewById(R.id.withdrawable_layout);
//        isforex_ib_back = (TextView)findViewById(R.id.isforex_ib_back);
//        getBackGroundLayout().setOnLeftButtonClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
    }

    private void initClickListener(){
        way_fast_layout.setOnClickListener(this);
        way_account_layout.setOnClickListener(this);
        contract_layout.setOnClickListener(this);
        query_layout.setOnClickListener(this);
        margin_layout.setOnClickListener(this);
        withdrawable_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.withdrawable_layout://帮助
                IsForexMoreActivity.this.finish();
                InvestHelpActivity.showHelpMessage(IsForexMoreActivity.this,getString(R.string.isforex_help_message));
                break;
            default:
                break;
        }
        if(BaseDroidApp.getInstance().isLogin()){

            switch (view.getId()){
                case R.id.way_fast_layout://快速交易
                    customerOrTrade = 1;
                    BaseHttpEngine.showProgressDialog();
                    requestAllRate("");
                    break;
                case R.id.way_account_layout: //双向宝
                    IsForexMoreActivity.this.finish();
                    Intent intent = new Intent();
                    intent.setClass(IsForexMoreActivity.this, IsForexMyRateInfoActivity.class);
                    startActivity(intent);
                    break;
                case R.id.margin_layout://交易查询
                    IsForexMoreActivity.this.finish();
                    Intent intentMargin = new Intent(IsForexMoreActivity.this, IsForexQueryMenuActivity.class);
                    startActivity(intentMargin);
                    break;
                case R.id.contract_layout://保证金交易
                    IsForexMoreActivity.this.finish();
                    Intent intentContract = new Intent(IsForexMoreActivity.this, IsForexBailInfoActivity.class);
                    startActivity(intentContract);
                    break;
                case R.id.query_layout://签约管理
                    IsForexMoreActivity.this.finish();
                    Intent intentQuery = new Intent(IsForexMoreActivity.this, IsForexBailProduceActivity.class);
                    startActivity(intentQuery);
                    break;

//                case R.id.withdrawable_layout://帮助
//                    InvestHelpActivity.showHelpMessage(IsForexMoreActivity.this,getString(R.string.isforex_help_message));
//                    break;
                default:
                    break;
            }

        }else {
            switch (view.getId()){
                case R.id.way_fast_layout:
                    btnly = 0;
                    break;
                case R.id.way_account_layout:
                    btnly = 1;
                    break;
                case R.id.margin_layout:
                    btnly = 2;
                    break;
                case R.id.contract_layout:
                    btnly = 3;
                    break;
                case R.id.query_layout:
                    btnly = 4;
                    break;
//                case R.id.withdrawable_layout:
//                    InvestHelpActivity.showHelpMessage(IsForexMoreActivity.this,getString(R.string.isforex_help_message));
//                    break;
                default:
                    break;
            }
            if((R.id.withdrawable_layout)!=view.getId()){
                exeLogin();
            }
        }

    }
    @Override
    public void requestAllRateCallback(Object resultObj) {
        super.requestAllRateCallback(resultObj);
        if (codeResultList == null || codeResultList.size() <= 0) {
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(
                    IsForexMoreActivity.this.getResources().getString(R.string.isForex_trade_code));
            return;
        }
            // 查询结算币种dealCodeDate
            dealCodeDate();
            String 	codeCodeName =	codeDealCodeNameList.get(0);
            String codes[] = codeCodeName.split("/");
            String sourceCurrency = codes[0];
            String targetCurrency = codes[1];
//		人民币金、银、铂、钯、美元金、银、铂、钯   贵金属的币种
            if(LocalData.goldLists.contains(sourceCurrency)||LocalData.goldLists.contains(targetCurrency)){
                vfgType = "G";
            }else {
                vfgType = "F";
            }
        // 查询结算币种
        requestPsnVFGGetRegCurrency(vfgType);
    }
    // 查询结算币种----回调
    @Override
    public void requestPsnVFGGetRegCurrencyCallback(Object resultObj) {
        super.requestPsnVFGGetRegCurrencyCallback(resultObj);
        if (vfgRegCurrencyList == null || vfgRegCurrencyList.size() <= 0) {
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(
                    IsForexMoreActivity.this.getResources().getString(R.string.isForex_trade_jcCode));
            return;
        }
        IsForexMoreActivity.this.finish();
        Intent intent = new Intent(IsForexMoreActivity.this, IsForexTradeSubmitActivity.class);
        intent.putExtra(ConstantGloble.ISFOREX_REQUEXTTRADE_KEY, ConstantGloble.ISFOREX_MYRATE_TRADE_ACTIVITY);// 101
        startActivityForResult(intent, ConstantGloble.ISFOREX_TRADE_CODE_ACTIVITY);
    }
    /** 处理货币对 */
    private void dealCodeDate() {
        int len = codeResultList.size();
//        sourceCodeDealCodeList = new ArrayList<String>();
//        targetCodeDealCodeList = new ArrayList<String>();
        codeDealCodeNameList = new ArrayList<String>();
        codeDealCodeList = new ArrayList<String>();
        for (int i = 0; i < len; i++) {
            Map<String, Object> map = codeResultList.get(i);
            String sourceCurrencyCode = (String) map.get(IsForex.ISFOREX_SOURCECURRENCYCODE_RES);
            String targetCurrencyCode = (String) map.get(IsForex.ISFOREX_TARGETCURRENCYCODE_RES);
            if (!StringUtil.isNull(sourceCurrencyCode) && LocalData.Currency.containsKey(sourceCurrencyCode)
                    && !StringUtil.isNull(targetCurrencyCode) && LocalData.Currency.containsKey(targetCurrencyCode)) {
//                sourceCodeDealCodeList.add(sourceCurrencyCode);
//                targetCodeDealCodeList.add(targetCurrencyCode);
                String source = LocalData.Currency.get(sourceCurrencyCode);
                String target = LocalData.Currency.get(targetCurrencyCode);
                codeDealCodeList.add(sourceCurrencyCode + "/" + targetCurrencyCode);
                codeDealCodeNameList.add(source + "/" + target);
            }
        }
    }

    private void exeLogin(){

        BaseActivity.getLoginUtils(IsForexMoreActivity.this).exe(new LoginTask.LoginCallback() {

            @Override
            public void loginStatua(boolean isLogin) {
                // TODO Auto-geneonrated method stub
                if (isLogin) {
                    commConversationId = (String)BaseDroidApp.getInstance().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
                    // 第一次进入自选
                    BaseHttpEngine.showProgressDialogCanGoBack();
                    if (StringUtil.isNull(commConversationId)) {
                        // 请求登录后的CommConversationId
                        requestCommConversationId();
                    } else {
                        // 判断用户是否开通投资理财服务
                        requestPsnInvestmentManageIsOpen();
                    }
                }
            }
        });

    }

    @Override
    public void requestCommConversationIdCallBack(Object resultObj) {
        super.requestCommConversationIdCallBack(resultObj);
        commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
        if (StringUtil.isNullOrEmpty(commConversationId)) {
            BaseHttpEngine.dissMissProgressDialog();
            return;
        } else {
            // 判断用户是否开通投资理财服务
            requestPsnInvestmentManageIsOpen();
        }
    }

    /** 判断是否开通投资理财服务--二级菜单----回调 */
    @Override
    public void requestMenuIsOpenCallback(Object resultObj) {
        super.requestMenuIsOpenCallback(resultObj);
        if (isOpen) {
            // 查询是否签约保证金产品
            searchBaillAfterOpen = 1;
            requestPsnVFGBailListQueryCondition();
        } else {
            BaseHttpEngine.dissMissProgressDialog();
            searchBaillAfterOpen = 2;
            getPop();
        }
    }

    /** 查询是否签约保证金产品--------回调 */
    @Override
    public void requestPsnVFGBailListQueryConditionCallback(Object resultObj) {
        super.requestPsnVFGBailListQueryConditionCallback(resultObj);
        List<Map<String, String>> result = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap()
                .get(ConstantGloble.ISFOREX_RESULT_SIGN);
        if (result == null || result.size() <= 0) {
            isSign = false;
            isSettingAcc = false;
            BaseHttpEngine.dissMissProgressDialog();
            // 弹出任提示框
            getPop();
            return;
        } else {
            isSign = true;
            isCondition = true;
            // 是否设置双向宝账户
            requestPsnVFGGetBindAccount();
        }
    }
    /** 判断是否设置外汇双向宝账户 --------条件判断 */
    @Override
    public void requestConditionAccountCallback(Object resultObj) {
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        // 得到response
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        Map<String, String> accReaultMap = (Map<String, String>) biiResponseBody.getResult();
        if (StringUtil.isNullOrEmpty(accReaultMap)) {
            isSettingAcc = false;
        } else {
            String accountNumber = accReaultMap.get(IsForex.ISFOREX_ACCOUNTNUMBER_REQ);
            if (StringUtil.isNull(accountNumber)) {
                isSettingAcc = false;
            } else {
                isSettingAcc = true;
                BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_ACCREAULTMAP, accReaultMap);
            }
        }

        if (isOpen && isSign && isSettingAcc) {
            // /////////////////////功能外置数据
            switch (btnly) {
                case 0://快速交易
                    customerOrTrade = 1;
                    BaseHttpEngine.showProgressDialog();
                    requestAllRate("");
                    break;
                case 1://双向宝账户
                    IsForexMoreActivity.this.finish();
                    Intent intent = new Intent();
                    intent.setClass(IsForexMoreActivity.this, IsForexMyRateInfoActivity.class);
                    startActivity(intent);
                    break;
                case 2: //交易查询
                    IsForexMoreActivity.this.finish();
                    Intent intentMargin = new Intent(IsForexMoreActivity.this, IsForexQueryMenuActivity.class);
                    startActivity(intentMargin);
                    break;
                case 3://保证金交易
                    IsForexMoreActivity.this.finish();
                    Intent intentContract = new Intent(IsForexMoreActivity.this, IsForexBailInfoActivity.class);
                    startActivity(intentContract);
                    break;
                case 4://签约管理
                    IsForexMoreActivity.this.finish();
                    Intent intentQuery = new Intent(IsForexMoreActivity.this, IsForexBailProduceActivity.class);
                    startActivity(intentQuery);
                    break;
//                case 5://帮助
//                    InvestHelpActivity.showHelpMessage(IsForexMoreActivity.this,getString(R.string.isforex_help_message));
//                    break;
                default:
                    break;
            }
        } else {
            BaseHttpEngine.dissMissProgressDialog();
            // 弹出任提示框
            getPop();
            return;
        }
    }

}
