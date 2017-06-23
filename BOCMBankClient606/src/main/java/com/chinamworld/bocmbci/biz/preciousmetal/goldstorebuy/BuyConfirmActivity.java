package com.chinamworld.bocmbci.biz.preciousmetal.goldstorebuy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.CommonApplication;
import com.chinamworld.bocmbci.biz.preciousmetal.NewTitleAndContent;
import com.chinamworld.bocmbci.biz.preciousmetal.PreciousmetalBaseActivity;
import com.chinamworld.bocmbci.biz.preciousmetal.PreciousmetalDataCenter;
import com.chinamworld.bocmbci.constant.DictionaryData;
import com.chinamworld.bocmbci.http.IHttpResponseCallBack;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 贵金属积存 购买确认页面
 * Created by linyl on 2016/8/24.
 */
public class BuyConfirmActivity extends PreciousmetalBaseActivity
        implements View.OnClickListener {
    Button btn_confirm;
    NewTitleAndContent mycontainer;
    /**购买方式  克数/金额 **/
    String buyType;
    String conversationId;
    /**积存账户信息 cusinfo **/
    Map<String,Object> accountCusInfo = null;
    List<Map<String ,Object>> storeList = null;
    Map<String,Object> pricelistMap = null;
    String currencyCode;
    String currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goldstore_ransom_confirm);//可公用赎回的确认页面 动态加载页面元素
        getPreviousmeatalBackgroundLayout().setTitleText("确认信息");
        getPreviousmeatalBackgroundLayout().setMetalRightVisibility(View.GONE);
        btn_confirm = (Button) findViewById(R.id.btn_goldstore_ransom_confirm);

        mycontainer = (NewTitleAndContent) findViewById(R.id.mycontainerLayout);

        accountCusInfo = (Map<String,Object>) PreciousmetalDataCenter.getInstance().ACCOUNTQUERYMAP.get("cusInfo");
        buyType = this.getIntent().getStringExtra("buyType");
        pricelistMap = (Map<String,Object>) PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(0);
        currencyCode = (String) ((Map<String,Object>)pricelistMap.get("currency")).get("code");
        currency = DictionaryData.getKeyByValue(currencyCode,PreciousmetalDataCenter.currencyList);
        initDetailView(buyType);


        btn_confirm.setOnClickListener(this);

//        /**返回事件 跳转到首页**/
//        getPreviousmeatalBackgroundLayout().setMetalBackonClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });

    }

    /**
     * 加载页面元素
     * type 购买方式
     */
    private void initDetailView(String type){
        mycontainer.addView(createNewLabelTextView(R.string.buy_account,this.getIntent().getStringExtra("accountNum")));
        mycontainer.addView(createNewLabelTextView(R.string.buy_var,this.getIntent().getStringExtra("varietiesName")));
        mycontainer.addView(createNewLabelTextView(R.string.buy_currency,currency));
        mycontainer.addView(createNewLabelTextView(R.string.buy_type,this.getIntent().getStringExtra("storeType")));
        if("weight".equals(type)){//按克重
            mycontainer.addView(createNewLabelTextView(R.string.buy_way,"按克重"));
            mycontainer.addView(createNewLabelTextView(R.string.buy_gram,
                    StringUtil.parseStringPattern(this.getIntent().getStringExtra("etNum"),4)));
            mycontainer.addView(createNewLabelTextView(R.string.buy_money,this.getIntent().getStringExtra("cankaoAmount")));
        }else if ("amount".equals(type)){//按金额
            mycontainer.addView(createNewLabelTextView(R.string.buy_way,"按金额"));
            mycontainer.addView(createNewLabelTextView(R.string.buy_goumai_money,
                    StringUtil.parseStringPattern(this.getIntent().getStringExtra("etNum"),2)));
        }
//        if(this.getIntent().getBooleanExtra("Flag",false)){
//            mycontainer.addView(createNewLabelTextView(R.string.buy_num,this.getIntent().getStringExtra("bankerNo")));
//        }
        mycontainer.addView(createNewLabelTextViewTwo(R.string.buy_num,StringUtil.isNullOrEmpty(this.getIntent().getStringExtra("bankerNo")) ? "" : this.getIntent().getStringExtra("bankerNo")));
    }

    @Override
    public void requestCommConversationIdCallBack(Object resultObj) {
        super.requestCommConversationIdCallBack(resultObj);
        conversationId = (String) CommonApplication.getInstance().getBizDataMap().get("conversationId");
        requestPSNGetTokenId(conversationId);
    }

    @Override
    public void requestPSNGetTokenIdCallBack(Object resultObj) {
        super.requestPSNGetTokenIdCallBack(resultObj);
        String tokenId = (String) CommonApplication.getInstance().getBizDataMap().get("TokenId");
        //调#3买卖交易接口
        requestPsnGoldStoreTrans(tokenId);
    }

    /**
     * 积存买卖交易（购买）
     * @param token
     */
    private void requestPsnGoldStoreTrans(String token){
        Map<String,Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("accountId",(String) accountCusInfo.get("accountId"));
        paramsMap.put("tranType","1");//1---购买
        if("amount".equals(buyType)){//按金额
            paramsMap.put("tranStyle", "1");
            paramsMap.put("tranAmount", StringUtil.append2Decimals(this.getIntent().getStringExtra("etNum"),2));//交易金额  不能上送千分位格式化数据
            paramsMap.put("tranWeight","0.00");//交易克重
        }else {//按克重
            paramsMap.put("tranStyle", "2");// 2---按克重

            paramsMap.put("tranAmount","0.00");//交易金额
            paramsMap.put("tranWeight", StringUtil.append2Decimals(this.getIntent().getStringExtra("etNum"),4));//交易克重
        }

        paramsMap.put("buyPrice", this.getIntent().getStringExtra("buyPrice"));//客户买入价
        paramsMap.put("currency",currencyCode);//币种
        paramsMap.put("currCode",this.getIntent().getStringExtra("currCode"));
        paramsMap.put("tellId",StringUtil.isNullOrEmpty(this.getIntent().getStringExtra("bankerNo")) ? "" : this.getIntent().getStringExtra("bankerNo"));//银行员工号
        paramsMap.put("tranWay","2");//交易渠道
        paramsMap.put("trsWeight","0.00");
//        paramsMap.put("varietiesName",(String)PreciousmetalDataCenter.getInstance().curDetail.get("varietiesName"));
        paramsMap.put("varietiesName",this.getIntent().getStringExtra("varietiesName"));
        //TODO... 此处需再分析上送值
        paramsMap.put("salePrice","0.00");//交易时客户买入价
        paramsMap.put("token",token);


        this.getHttpTools().requestHttpWithConversationId("PsnGoldStoreTrans",paramsMap,conversationId,
                new IHttpResponseCallBack<Map<String,Object>>(){

                    @SuppressWarnings("unchecked")
                    @Override
                    public void httpResponseSuccess(Map<String,Object> result, String method) {
                        //关闭通信框
//                        LoadingDialog.closeDialog();
                        BiiHttpEngine.dissMissProgressDialog();
                        if(StringUtil.isNullOrEmpty(result)) return;
                        String transactionId = (String) result.get("transactionId");
                        Intent intent = new Intent(BuyConfirmActivity.this,BuySuccessActivity.class);
                        intent.putExtra("transactionId",transactionId);
                        if("amount".equals(buyType)){
                            intent.putExtra("buyNumStr",
                                    StringUtil.parseStringPattern(BuyConfirmActivity.this.getIntent().getStringExtra("etNum"),2));
                        }else if("weight".equals(buyType)){
                            intent.putExtra("buyNumStr",
                                    StringUtil.parseStringPattern(BuyConfirmActivity.this.getIntent().getStringExtra("etNum"),4));
                        }
                        intent.putExtra("varietiesName",BuyConfirmActivity.this.getIntent().getStringExtra("varietiesName"));
                        intent.putExtra("currency",currency);
                        intent.putExtra("storeType",BuyConfirmActivity.this.getIntent().getStringExtra("storeType"));
                        intent.putExtra("accountNum",BuyConfirmActivity.this.getIntent().getStringExtra("accountNum"));
                        intent.putExtra("accountId",BuyConfirmActivity.this.getIntent().getStringExtra("accountId"));
                        intent.putExtra("buyType",buyType);//购买方式
                        if(BuyConfirmActivity.this.getIntent().getBooleanExtra("Flag",false)){
                            intent.putExtra("bankerNo",BuyConfirmActivity.this.getIntent().getStringExtra("bankerNo"));//银行员工号
                        }
                        startActivity(intent);
                    }
                });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_goldstore_ransom_confirm:
                BaseHttpEngine.showProgressDialogCanGoBack();
                requestCommConversationId();
//                LoadingDialog.showLoadingDialog(this);
                break;
        }

    }

//    @Override
//    public void onBackPressed() {
//        finish();
//    }
}
