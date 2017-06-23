package com.chinamworld.bocmbci.biz.preciousmetal.goldstoreransom;

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
 * 贵金属积存 赎回确认页面
 * Created by linyl on 2016/8/24.
 */
public class RansomConfirmActivity extends PreciousmetalBaseActivity
        implements View.OnClickListener {
    Button btn_confirm;
    NewTitleAndContent mycontainer;
    String ransomNumStr;
    String conversationId ;
    /**积存账户信息 cusinfo **/
    Map<String,Object> accountCusInfo = null;
    List<Map<String ,Object>> storeList = null;
    Map<String,Object> pricelistMap = null;
    String currency;
    String currencyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goldstore_ransom_confirm);
        getPreviousmeatalBackgroundLayout().setTitleText("确认信息");
        getPreviousmeatalBackgroundLayout().setMetalRightVisibility(View.GONE);
        btn_confirm = (Button) findViewById(R.id.btn_goldstore_ransom_confirm);
        btn_confirm.setOnClickListener(this);
        mycontainer = (NewTitleAndContent) findViewById(R.id.mycontainerLayout);
        accountCusInfo = (Map<String,Object>) PreciousmetalDataCenter.getInstance().ACCOUNTQUERYMAP.get("cusInfo");
        storeList = (List<Map<String ,Object>>)PreciousmetalDataCenter.getInstance().ACCOUNTQUERYMAP.get("storeList");
//        pricelistMap = (Map<String,Object>) PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(0);
        pricelistMap = (Map<String,Object>) PreciousmetalDataCenter.getInstance().LOGINPRICEMAPITEM;
        currencyCode = (String) ((Map<String,Object>)pricelistMap.get("currency")).get("code");
        currency = DictionaryData.getKeyByValue(currencyCode,PreciousmetalDataCenter.currencyList);
        ransomNumStr = this.getIntent().getStringExtra("ransomNum");
        initDetailView();
    }

    /**
     * 初始化 页面元素
     */
    private void initDetailView(){
        mycontainer.addView(createNewLabelTextView(R.string.debitCard,this.getIntent().getStringExtra("accountNum")));
//        mycontainer.addView(createNewLabelTextView(R.string.varieties, DictionaryData.getKeyByValue((String) storeList.get(0).get("varieties"), PreciousmetalDataCenter.varietiesList)));
        mycontainer.addView(createNewLabelTextView(R.string.varieties, (String) pricelistMap.get("varietiesName")));
        mycontainer.addView(createNewLabelTextView(R.string.goldstore_curcode,currency));
        mycontainer.addView(createNewLabelTextView(R.string.goldstore_branchtype_2,(String) pricelistMap.get("currCodeName")));
        mycontainer.addView(createNewLabelTextView(R.string.goldstore_shuhui_unit, StringUtil.parseStringPattern(ransomNumStr,4)));
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
     * 赎回提交交易
     */
    private void requestPsnGoldStoreTrans(String tokenId){
        //TODO...
        Map<String,Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("accountId",(String) accountCusInfo.get("accountId"));
        paramsMap.put("tranType","2");//2---赎回
        paramsMap.put("tranStyle","2");// 2---按克重
        paramsMap.put("tranAmount","0.00");//交易金额
        //TODO... 此处需再分析上送值
        paramsMap.put("buyPrice", "0.00");
//        paramsMap.put("salePrice", PreciousmetalDataCenter.getInstance().SALEPRICE_LONGIN);//客户卖出价
        paramsMap.put("salePrice", this.getIntent().getStringExtra("ransomPrice"));//客户卖出价
        paramsMap.put("tranWeight",ransomNumStr);
        paramsMap.put("trsWeight","0.00");
        paramsMap.put("currency",currencyCode);//币种
//        paramsMap.put("currCode",(String)PreciousmetalDataCenter.getInstance().curDetail.get("currCode"));
        paramsMap.put("currCode",((String) pricelistMap.get("currCode")));
        paramsMap.put("tellId","");//银行员工号
        paramsMap.put("tranWay","2");//交易渠道
//        paramsMap.put("varietiesName",(String)PreciousmetalDataCenter.getInstance().curDetail.get("varietiesName"));
        paramsMap.put("varietiesName",(String) pricelistMap.get("varietiesName"));
        paramsMap.put("token",tokenId);
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
                        Intent intent = new Intent(RansomConfirmActivity.this,RansomSuccessActivity.class);
                        intent.putExtra("transactionId",transactionId);
                        intent.putExtra("ransomNumStr",StringUtil.parseStringPattern(ransomNumStr,4));
                        intent.putExtra("varietiesName",(String) pricelistMap.get("varietiesName"));
                        intent.putExtra("currency",currency);
                        intent.putExtra("currCodeName",(String) pricelistMap.get("currCodeName"));
                        intent.putExtra("accountNum",RansomConfirmActivity.this.getIntent().getStringExtra("accountNum"));
                        intent.putExtra("accountId",RansomConfirmActivity.this.getIntent().getStringExtra("accountId"));
                        intent.putExtra("ransomMax",RansomConfirmActivity.this.getIntent().getStringExtra("ransomMax"));
                        startActivity(intent);
                    }
                });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_goldstore_ransom_confirm://确认
                BaseHttpEngine.showProgressDialogCanGoBack();
                requestCommConversationId();
//                LoadingDialog.showLoadingDialog(this);
                break;

        }
    }
}
