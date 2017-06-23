package com.chinamworld.bocmbci.biz.preciousmetal.goldstoremainpage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.GoldBonus;
import com.chinamworld.bocmbci.biz.goldbonus.GoldbonusLocalData;
import com.chinamworld.bocmbci.biz.preciousmetal.PreciousmetalBaseActivity;
import com.chinamworld.bocmbci.biz.preciousmetal.PreciousmetalDataCenter;
import com.chinamworld.bocmbci.biz.preciousmetal.goldstoreaccount.GoldstoreAccountResetActivity;
import com.chinamworld.bocmbci.biz.preciousmetal.goldstoreaccount.GoldstoreAccountSetActivity;
import com.chinamworld.bocmbci.biz.preciousmetal.goldstorebuy.BuyMainActivity;
import com.chinamworld.bocmbci.biz.preciousmetal.goldstoreransom.RansomMainActivity;
import com.chinamworld.bocmbci.biz.preciousmetal.goldstoretransquery.TransQuryActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpObserver;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.llbt.userwidget.dialogview.LoadingDialog;
import com.chinamworld.llbt.userwidget.dialogview.MessageDialog;
import com.chinamworld.llbt.userwidget.refreshliseview.IRefreshLayoutListener;
import com.chinamworld.llbt.userwidget.refreshliseview.PullDownRefreshLayout;
import com.chinamworld.llbt.userwidget.refreshliseview.RefreshDataStatus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoldstoreMainActivity extends PreciousmetalBaseActivity {
    private static final String TAG = "PsnGoldStorePriceListQuery";
    private TextView buyPrice_head;
    private TextView buyPrice_end;
    private TextView sellPrice_head;
    private TextView sellPrice_end;
    private TextView beijing_time;
    //是否开通投资理财的标志
    private boolean isOpen;
    //未作投资理财的提示信息
    private TextView message;
    //积存量布局文件
    private LinearLayout acount_layout;
    //积存数量
    private TextView amount;
    //参考市值
    private TextView reminder_value;
    //赎回按钮
    private Button pay_back;
    //向右的箭头
    private ImageView right_arrow;
    //购买按钮
    private Button buy_button;
    //积存量
    private String jicunAmount;
    //交易查询
    private RelativeLayout trade_check;
    //选择种类
    private RelativeLayout trade_type;
    //产品名称
    private TextView type_name;
    //弹框列表
    private ListView listView;
    //要弹出的对话框
    private View dialogView;
    //7号接口的list集合
    private List<Map<String, Object>> amountList;
    //7号接口返回数据
    private Map<String, Object> resultMapseven;
    //对话框
    private CustomDialog customDialog;
    /**常见问题*/
    private TextView problem;
    //暂停交易按钮
    private Button stop_trad;
    //7秒刷新只一次
    private Boolean isFirstRefresh=true;
    /**下拉刷新控件*/
    private PullDownRefreshLayout pull_down_layout;
    //判断是否从下拉刷新编制
    private boolean isrefeshflag=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Map<String, Object> resultMap=(Map<String, Object>) (BaseDroidApp.getInstanse().getBizDataMap()
                .get(ConstantGloble.BIZ_LOGIN_DATA));
        //先判断是否为查询版
        if ("10".equals((String)resultMap.get("segmentId"))) {
            //查询版先调用别的接口在判断开通投资理财
            BaseHttpEngine.showProgressDialogCanGoBack();
            this.getHttpTools().requestHttpWithNoDialog(PreciousmetalDataCenter.PSNGOLDSTOREACCOUNTQUERY,
                    "requestPsnGoldStoreAccountQueryCallbackAgain", null,
                    false);
        }else{
            BaseHttpEngine.showProgressDialogCanGoBack();
            getHttpTools()
                    .requestHttpWithNoDialog(Comm.PSNINVESTMENTMANAGEISOPEN_API,
                            "requestPsnInvestmentisOpenBeforeCallback", null, false);
        }


    }
    public void requestPsnGoldStoreAccountQueryCallbackAgain(
            Object resultObj) {
        BaseHttpEngine.dissMissProgressDialog();
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        Map<String, Object> resultMapseven = (Map<String, Object>) biiResponseBody
                .getResult();
        if (StringUtil.isNullOrEmpty(resultMapseven)) {
            return;
        }
        //登陆后，先判断投资理财，如果没开通
        /**先调用开通投资理财的界面，不显示界面元素*/
//        LoadingDialog.showLoadingDialog(GoldstoreMainActivity.this);
        getHttpTools()
                .requestHttp(Comm.PSNINVESTMENTMANAGEISOPEN_API,
                        "requestPsnInvestmentisOpenBeforeCallback", null, false);
    }
    public void initView() {
        pull_down_layout=(PullDownRefreshLayout)findViewById(R.id.pull_down_layout);
        pull_down_layout.setBackgroundColor(1);
        pull_down_layout.setOnRefreshListener(new IRefreshLayoutListener() {
            @Override
            public void onLoadMore(View pullToRefreshLayout) {
                isrefeshflag=true;
                //下拉刷新4号接口
                getHttpTools().requestHttpWithNoDialog(PreciousmetalDataCenter.PSNGOLDSTOREPRICELIST,
                        "requestPsnGoldStorePriceListCallbackagain",
                        null,false);
            }
        });


        TextView back = (TextView)findViewById(R.id.ib_back);
        // back.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                ActivityTaskManager.getInstance().removeAllSecondActivity();
            }
        });

        buyPrice_head = (TextView) findViewById(R.id.prms_price_listiterm1_buyprice_head);
        buyPrice_end=(TextView) findViewById(R.id.prms_price_listiterm1_buyprice_end);
        sellPrice_head= (TextView) findViewById(R.id.prms_price_listiterm1_saleprice_head);
        sellPrice_end= (TextView) findViewById(R.id.prms_price_listiterm1_saleprice_end);
        beijing_time = (TextView) findViewById(R.id.beijing_time);
//            message=(TextView)findViewById(R.id.message);
        acount_layout = (LinearLayout) findViewById(R.id.acount_layout);
        amount = (TextView) findViewById(R.id.amount);
        reminder_value = (TextView) findViewById(R.id.reminder_value);
        pay_back = (Button) findViewById(R.id.pay_back);
        buy_button = (Button) findViewById(R.id.buy_button);
        stop_trad=(Button)findViewById(R.id.stop_trad);
        right_arrow=(ImageView)findViewById(R.id.right_arrow);
        trade_check = (RelativeLayout) findViewById(R.id.trade_check);
        trade_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent();
                intent.setClass(GoldstoreMainActivity.this,TransQuryActivity.class);
                startActivity(intent);
            }
        });
        trade_type = (RelativeLayout) findViewById(R.id.trade_type);
        problem=(TextView)findViewById(R.id.problem);
        problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(GoldstoreMainActivity.this, GoldstoreNormalProblemActivity.class);
                startActivity(intent);
            }
        });
        type_name = (TextView) findViewById(R.id.type_name);
        LoadingDialog.showLoadingDialog(GoldstoreMainActivity.this);
        /**调用牌价接口7s刷新，*/
        goldStorePriceListPollingQuery();

//             BaseHttpEngine.showProgressDialogCanGoBack();//此为老式通信框，需要更换

    }

    /**
     * 判断是否开通投资理财服务---回调
     */
    public void requestPsnInvestmentisOpenBeforeCallback(Object resultObj) {
        String isOpenresult = String.valueOf(HttpTools
                .getResponseResult(resultObj));
        if (StringUtil.isNull(isOpenresult)) {
            LoadingDialog.closeDialog();
            return;
        }
        LoadingDialog.closeDialog();
        BiiHttpEngine.dissMissProgressDialog();
        isOpen = Boolean.valueOf(isOpenresult);
        if (isOpen) {
            /**调用查询积存量的接口，即第7接口*/

            //7号接口判断是否关联网银，签约账户
            this.getHttpTools().requestHttp(PreciousmetalDataCenter.PSNGOLDSTOREACCOUNTQUERY,
                    "requestPsnGoldStoreAccountQueryCallback", null,
                    false);

        } else {
            //没开的话点击购买跳转开通页面，并且主界面显示签约按钮，不显示交易查询那一栏，开通完成返回主界面
            //为开通投资理财不显示交易查询
//            trade_check.setVisibility(View.INVISIBLE);
//            buy_button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(GoldstoreMainActivity.this, NotOpenFincActivity.class);
            startActivity(intent);
//                }
//            });
        }

    }

    @SuppressWarnings("unchecked")
    public void requestPsnGoldStoreAccountQueryCallback(
            Object resultObj) {
        BaseHttpEngine.dissMissProgressDialog();
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        resultMapseven = (Map<String, Object>) biiResponseBody
                .getResult();
        if (StringUtil.isNullOrEmpty(resultMapseven)) {
            return;
        }

        PreciousmetalDataCenter.getInstance().ACCOUNTQUERYMAP = resultMapseven;
        //存储账户number和name
        PreciousmetalDataCenter.getInstance().AccountNumberOld=(String)((Map<String,Object>)resultMapseven.get(PreciousmetalDataCenter.CUSINFO)).get(PreciousmetalDataCenter.ACCOUNTMUN);
        PreciousmetalDataCenter.getInstance().AccountName=(String)((Map<String,Object>)resultMapseven.get(PreciousmetalDataCenter.CUSINFO)).get(PreciousmetalDataCenter.CUSTNAME);
        PreciousmetalDataCenter.getInstance().AccountMail=(String)((Map<String,Object>)resultMapseven.get(PreciousmetalDataCenter.CUSINFO)).get(PreciousmetalDataCenter.POSTCODE);
        PreciousmetalDataCenter.getInstance().AccountPhoneMunber=(String)((Map<String,Object>)resultMapseven.get(PreciousmetalDataCenter.CUSINFO)).get(PreciousmetalDataCenter.CUSTPHONENUM);
                PreciousmetalDataCenter.getInstance().AccountAddress=(String)((Map<String,Object>)resultMapseven.get(PreciousmetalDataCenter.CUSINFO)).get(PreciousmetalDataCenter.CUSTADDRESS);


        //此处显示是否有积存量，判断按钮显示是否有赎回
        amountList = (List<Map<String, Object>>) resultMapseven.get(PreciousmetalDataCenter.STORELIST);
        PreciousmetalDataCenter.getInstance().AMOUNTLIST=amountList;
        Map<String, Object> result = (Map<String, Object>) resultMapseven.get(PreciousmetalDataCenter.CUSINFO);
        String setGoldStoreAccount = (String) result.get(PreciousmetalDataCenter.SETGOLDSTOREACCOUNT);
        String linkedAccount = (String) result.get(PreciousmetalDataCenter.LINKEDACCOUNT);//0是未关联，1是已关联


        if ("0".equals(setGoldStoreAccount)) {
            // 未设定登记账户，先进入签约流程
            //如果未关联账户，关联网银跳转签约账户界面
//            buy_button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(GoldstoreMainActivity.this, GoldstoreAccountSetActivity.class);
            startActivity(intent);
//                }
//            });
        }
        else if (linkedAccount.equals("0")) {
            //此处判断是否有关联账户，并且关联账户，未关联网银，跳转关联网银界面
//            buy_button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(GoldstoreMainActivity.this, NotContentNetBankActivity.class);
            startActivity(intent);
            finish();
//                }
//            });
        }  else {//正常界面
            LoadingDialog.closeDialog();
            setContentView(R.layout.activity_goldstore_main);
            setTitle(R.string.main_menu34);
            RelativeLayout backGround = (RelativeLayout) findViewById(R.id.main_layout);
            backGround.setBackgroundColor(this.getResources().getColor(R.color.bg_goldstore));
            //初始化页面元素
            initView();
            //请求6号接口
            getHttpTools().requestHttp(PreciousmetalDataCenter.PSNGOLDSTOREGOODSLIST,
                    "requestPsnGoldStoreGoodsListCallback", null,
                    false);


            }

            //如果关联账户并且，关联网银跳转购买或赎回界面
            buy_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent();
                    intent.setClass(GoldstoreMainActivity.this, BuyMainActivity.class);
                    startActivity(intent);
                }
            });
            pay_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent();
                    intent.setClass(GoldstoreMainActivity.this, RansomMainActivity.class);
                    startActivity(intent);
                }
            });
        }


    public void requestPsnGoldStoreGoodsListCallback(
            Object resultObj) {
        BaseHttpEngine.dissMissProgressDialog();
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        final List<Map<String, Object>> resultMap = (List<Map<String, Object>>) biiResponseBody
                .getResult();
        if (StringUtil.isNullOrEmpty(resultMap)) {
            return;
        }
        PreciousmetalDataCenter.getInstance().CODE=((String)resultMap.get(0).get(PreciousmetalDataCenter.CURRCODE));
        type_name.setText((String)resultMap.get(0).get(PreciousmetalDataCenter.CURRCODENAME));
        PreciousmetalDataCenter.getInstance().GoodsListT=resultMap;
        //有一种以上才能点击类型选择
        if (resultMap.size() > 1) {
            //有向右的箭头
            right_arrow.setVisibility(View.VISIBLE);
            //选择积存类型的点击事件
            trade_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //调用6号接口
                    dialogView = View.inflate(GoldstoreMainActivity.this,
                            R.layout.goldstore_choselist, null);
                    listView = (ListView) dialogView.findViewById(R.id.chose_list);
                    //填充listview
                    final ArrayList arrayList = new ArrayList();
                    for (int i = 0; i < resultMap.size(); i++) {
                        arrayList.add((String) (resultMap.get(i).get(PreciousmetalDataCenter.CURRCODENAME)));
                    }
                    listView.setAdapter(new ArrayAdapter<ArrayList>(GoldstoreMainActivity.this,
                            android.R.layout.simple_list_item_1, arrayList));
//                        BaseDroidApp.getInstanse().showDialog(dialogView);
                    customDialog = new CustomDialog(GoldstoreMainActivity.this, dialogView);
                    customDialog.show();
                   listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //根据code来显示牌价积存量等
                            PreciousmetalDataCenter.getInstance().CODE = (String) PreciousmetalDataCenter.getInstance().GoodsListT.get(position).get(PreciousmetalDataCenter.CURRCODE);
                            //遍历4号和7号接口，分别显示积存量和参考市值，买入价和卖出价,先遍历4号接口
                            if(!StringUtil.isNullOrEmpty(PreciousmetalDataCenter.getInstance().LOGINPRICEMAP)){
                                for (int i = 0; i < PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.size(); i++) {
                                    if (((String) resultMap.get(position).get(PreciousmetalDataCenter.CURRCODE)).equals
                                            (PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(i).get(PreciousmetalDataCenter.CURRCODE))) {
                                        //最新规则，如果没有买入价则购买按钮置灰，不能点击，如果没有卖出价则赎回按钮置灰，不能点击，
                                        if(StringUtil.isNullOrEmpty((String) (PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(position).get(PreciousmetalDataCenter.BUYPRICE)))){
//                                        buy_button.setEnabled(false);
//                                        buy_button.setBackgroundColor(getResources().getColor(R.color.storegold_cantgray));
                                            stop_trad.setVisibility(View.VISIBLE);
                                            buy_button.setVisibility(View.GONE);
                                            pay_back.setVisibility(View.GONE);
                                        }else{
                                            stop_trad.setVisibility(View.GONE);
                                            buy_button.setVisibility(View.VISIBLE);
                                            pay_back.setVisibility(View.VISIBLE);
                                            buy_button.setEnabled(true);
                                            buy_button.setBackgroundColor(getResources().getColor(R.color.goldstore_red));
                                            buyPrice_head.setText(head((String) (PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(position).get(PreciousmetalDataCenter.BUYPRICE))));
                                            buyPrice_end.setText(end((String) (PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(position).get(PreciousmetalDataCenter.BUYPRICE))));
                                        }
                                        if(StringUtil.isNullOrEmpty((String) (PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(position).get(PreciousmetalDataCenter.SALEPRICE)))){
//                                        pay_back.setEnabled(false);
//                                        pay_back.setBackgroundColor(getResources().getColor(R.color.storegold_cantgray));
                                            stop_trad.setVisibility(View.VISIBLE);
                                            buy_button.setVisibility(View.GONE);
                                            pay_back.setVisibility(View.GONE);
                                        }else{
                                            stop_trad.setVisibility(View.GONE);
                                            buy_button.setVisibility(View.VISIBLE);
                                            pay_back.setVisibility(View.VISIBLE);
                                            pay_back.setEnabled(true);
                                            pay_back.setBackgroundColor(getResources().getColor(R.color.white));
                                            sellPrice_head.setText(head((String) (PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(position).get(PreciousmetalDataCenter.SALEPRICE))));
                                            sellPrice_end.setText(end((String) (PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(position).get(PreciousmetalDataCenter.SALEPRICE))));
                                        }

                                        PreciousmetalDataCenter.getInstance().LOGINPRICEMAPITEM = PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(position);

                                    }

                                }
                            }
                            //判断4号接口是否为空，如果是则牌价显示--，积存类型正常，积存量正常，参考市值--积存主页从有牌价到无牌价是，切换积存类型的修改
                            if(StringUtil.isNullOrEmpty(PreciousmetalDataCenter.getInstance().LOGINPRICEMAP)){
                                stop_trad.setVisibility(View.VISIBLE);
                                buy_button.setVisibility(View.GONE);
                                pay_back.setVisibility(View.GONE);
                                reminder_value.setText("参考市值（元）--");
                                buyPrice_head.setText("--");
                                buyPrice_end.setText("");
                                sellPrice_head.setText("--");
                                sellPrice_end.setText("");
                                if(!StringUtil.isNullOrEmpty(amountList)) {
                                    //遍历7号接口
                                    for (int i = 0; i < amountList.size(); i++) {
                                        if (PreciousmetalDataCenter.getInstance().CODE.equals(amountList.get(i).get(PreciousmetalDataCenter.CURRCODE))) {
                                            jicunAmount = (String) (amountList.get(i).get(PreciousmetalDataCenter.AMOUNT));
                                            if (0 == Double.parseDouble(jicunAmount)) {
                                                amount.setText("0.0000");

                                            } else {
                                                amount.setText(StringUtil.parseStringPattern(jicunAmount, 4));
                                            }

                                        } else {
                                            amount.setText("0.0000");
                                        }

                                    }
                                }
                            }else{
                                if(!StringUtil.isNullOrEmpty(amountList)){
                                    //遍历7号接口
                                    for(int i=0;i<amountList.size();i++){
                                        if(PreciousmetalDataCenter.getInstance().CODE.equals(amountList.get(i).get(PreciousmetalDataCenter.CURRCODE))){
                                            jicunAmount=(String) (amountList.get(i).get(PreciousmetalDataCenter.AMOUNT));
                                            if(0==Double.parseDouble(jicunAmount)){
                                                amount.setText("0.0000");
                                                reminder_value.setText("参考市值（元）" + "0.00");
                                                pay_back.setVisibility(View.GONE);
                                            }else {
                                                amount.setText(StringUtil.parseStringPattern(jicunAmount,4));
                                                pay_back.setVisibility(View.VISIBLE);
                                                reminder_value.setText("参考市值（元）" + StringUtil.parseStringPattern(String.valueOf(Double.parseDouble(jicunAmount)
                                                        * (Double.parseDouble((String) (PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(position).get(PreciousmetalDataCenter.SALEPRICE))))), 2));//寄存克数乘以当前卖出价,保留两位小数
                                            }

                                        }else{
                                            amount.setText("0.0000");
                                            reminder_value.setText("参考市值（元）" + "0.00");
                                            pay_back.setVisibility(View.GONE);
                                        }

                                    }
                                }else{
                                    amount.setText("0.0000");
                                    reminder_value.setText("参考市值（元）" + "0.00");
                                    pay_back.setVisibility(View.GONE);
                                }
                            }


                            type_name.setText((String) PreciousmetalDataCenter.getInstance().GoodsListT.get(position).get(PreciousmetalDataCenter.CURRCODENAME));
                            customDialog.cancel();
                        }
                    });
                }
            });
        }else{
            //没有向右的箭头
            right_arrow.setVisibility(View.INVISIBLE);

        }
        //通过遍历7号接口取得积存量
        if(!StringUtil.isNullOrEmpty(amountList)){
            for(int i=0;i<amountList.size();i++){
                if(PreciousmetalDataCenter.getInstance().CODE.equals
                        (amountList.get(i).get(PreciousmetalDataCenter.CURRCODE))){
                    jicunAmount = (String) (amountList.get(i).get(PreciousmetalDataCenter.AMOUNT));
                    PreciousmetalDataCenter.getInstance().JiCunAmount=jicunAmount;
                    if(0==Double.parseDouble(jicunAmount)){
                        amount.setText("0.0000");
                        reminder_value.setText("参考市值（元）" + "0.00");
                        pay_back.setVisibility(View.GONE);
                    }else{
                        amount.setText(StringUtil.parseStringPattern(jicunAmount,4));
                        pay_back.setVisibility(View.VISIBLE);
                        PreciousmetalDataCenter.getInstance().StoreListItem = amountList.get(i);
                    }
                }else{
                    amount.setText("0.0000");
                    reminder_value.setText("参考市值（元）" + "0.00");
                    pay_back.setVisibility(View.GONE);
                }
            }

        }else{
            amount.setText("0.0000");
            reminder_value.setText("参考市值（元）" + "0.00");
            pay_back.setVisibility(View.GONE);
        }

    }


    /**
     * 7秒轮询4号牌价接口
     */
    private void goldStorePriceListPollingQuery() {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(PreciousmetalDataCenter.PSNGOLDSTOREPRICELIST);
        // //先调用一次
        HttpManager.requestPollingBii(biiRequestBody, pollingHandler,
                ConstantGloble.FOREX_REFRESH_TIMES);

    }

    private Handler pollingHandler = new Handler() {
        @SuppressWarnings("unchecked")
        public void handleMessage(android.os.Message msg) {
            // http状态码
            String resultHttpCode = (String) ((Map<String, Object>) msg.obj)
                    .get(ConstantGloble.HTTP_RESULT_CODE);
            // 返回数据
            Object resultObj = ((Map<String, Object>) msg.obj)
                    .get(ConstantGloble.HTTP_RESULT_DATA);
            // 回调对象
            HttpObserver callbackObject = (HttpObserver) ((Map<String, Object>) msg.obj)
                    .get(ConstantGloble.HTTP_CALLBACK_OBJECT);
            // 回调方法
            String callBackMethod = (String) ((Map<String, Object>) msg.obj)
                    .get(ConstantGloble.HTTP_CALLBACK_METHOD);

            switch (msg.what) {
                case ConstantGloble.HTTP_STAGE_CONTENT:
                    // 执行全局前拦截器
                    if (BaseDroidApp.getInstanse()
                            .httpRequestCallBackPre(resultObj)) {
                        break;
                    }
                    // 执行callbackObject回调前拦截器
                    if (httpRequestCallBackPre(resultObj)) {
                        break;
                    }
                    // 清空更新时间
                    // clearTimes();

                    BiiResponse biiResponse = (BiiResponse) ((Map<String, Object>) msg.obj)
                            .get(ConstantGloble.HTTP_RESULT_DATA);
                    List<BiiResponseBody> biiResponseBodys = biiResponse
                            .getResponse();
                    BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

                    List<Map<String, Object>> resultMap = (List<Map<String, Object>>) biiResponseBody
                            .getResult();
                    //再此处刷新6号接口
                    //如果6号接口报错，则重新调用此接口
                    if(!isFirstRefresh){
                        getHttpTools().requestHttpWithNoDialog(PreciousmetalDataCenter.PSNGOLDSTOREGOODSLIST,
                                "requestPsnGoldStoreGoodsListCallback", null,
                                false);
                    }

                    if (StringUtil.isNullOrEmpty(resultMap)) {
                        PreciousmetalDataCenter.getInstance().LOGINPRICEMAP=null;
                        return;
                    }
                    PreciousmetalDataCenter.getInstance().LOGINPRICEMAP = resultMap;
                    buy_button.setVisibility(View.VISIBLE);
                    stop_trad.setVisibility(View.GONE);
                    PreciousmetalDataCenter.getInstance().loginIsHave=true;

                    for (int i = 0; i < resultMap.size(); i++) {
                        if ((PreciousmetalDataCenter.getInstance().CODE.equals((String) resultMap.get(i).get(PreciousmetalDataCenter.CURRCODE)))) {

                            PreciousmetalDataCenter.getInstance().BUYPRICE_LONGIN = (String) (resultMap.get(i).get(PreciousmetalDataCenter.BUYPRICE));
                            PreciousmetalDataCenter.getInstance().SALEPRICE_LONGIN = (String) (resultMap.get(i).get(PreciousmetalDataCenter.SALEPRICE));
                            PreciousmetalDataCenter.getInstance().curDetail = resultMap.get(i);
                            beijing_time.setText("数据更新于北京时间" + (String) (resultMap.get(i).get(PreciousmetalDataCenter.PRICETIME)) + "，具体价格以实际成交为准");
                            if(StringUtil.isNullOrEmpty((String) resultMap.get(i).get(PreciousmetalDataCenter.BUYPRICE))){
                                stop_trad.setVisibility(View.VISIBLE);
                                buy_button.setVisibility(View.GONE);
                                pay_back.setVisibility(View.GONE);
                            }else{
                                stop_trad.setVisibility(View.GONE);
                                buy_button.setVisibility(View.VISIBLE);
//                                pay_back.setVisibility(View.VISIBLE);
                                buy_button.setEnabled(true);
                                buy_button.setBackgroundColor(getResources().getColor(R.color.goldstore_red));
                                buyPrice_head.setText(head((String) resultMap.get(i).get(PreciousmetalDataCenter.BUYPRICE)));
                                buyPrice_end.setText(end((String) resultMap.get(i).get(PreciousmetalDataCenter.BUYPRICE)));
                            }
                           if(StringUtil.isNullOrEmpty((String) resultMap.get(i).get(PreciousmetalDataCenter.SALEPRICE))){
//                               pay_back.setEnabled(false);
//                               pay_back.setBackgroundColor(getResources().getColor(R.color.storegold_cantgray));
                               stop_trad.setVisibility(View.VISIBLE);
                               buy_button.setVisibility(View.GONE);
                               pay_back.setVisibility(View.GONE);
                           }else{
                               stop_trad.setVisibility(View.GONE);
                               buy_button.setVisibility(View.VISIBLE);
//                               pay_back.setVisibility(View.VISIBLE);
                               pay_back.setEnabled(true);
                               pay_back.setBackgroundColor(getResources().getColor(R.color.white));
                               sellPrice_head.setText(head((String) resultMap.get(i).get(PreciousmetalDataCenter.SALEPRICE)));
                               sellPrice_end.setText(end((String) resultMap.get(i).get(PreciousmetalDataCenter.SALEPRICE)));
                           }


                        }
                    }
                    //遍历7号接口，code一样显示对应参考市值
                    //7号接口的遍历，更改积存量和参考市值
                    if(!StringUtil.isNullOrEmpty(amountList)){
                        for (int i = 0; i < amountList.size(); i++) {
                            if ((PreciousmetalDataCenter.getInstance().CODE).equals
                                    (amountList.get(i).get(PreciousmetalDataCenter.CURRCODE))) {
                                    jicunAmount = (String) (amountList.get(i).get(PreciousmetalDataCenter.AMOUNT));
                                //买入价如果为空。直接不显示赎回
                                    if((StringUtil.isNullOrEmpty(buyPrice_head.getText())||(StringUtil.isNullOrEmpty(buyPrice_end.getText())))){
                                        pay_back.setVisibility(View.GONE);
                                        buy_button.setVisibility(View.GONE);
                                        stop_trad.setVisibility(View.VISIBLE);
                                    }else{
                                        if(0==Double.parseDouble(jicunAmount)){
                                            amount.setText("0.0000");
                                            reminder_value.setText("参考市值（元）" + "0.00");
                                            pay_back.setVisibility(View.GONE);
                                        }else{
                                            amount.setText(StringUtil.parseStringPattern(jicunAmount,4));
                                            pay_back.setVisibility(View.VISIBLE);
                                            reminder_value.setText("参考市值（元）" + StringUtil.parseStringPattern(String.valueOf(Double.parseDouble(jicunAmount)
                                                    * (Double.parseDouble(PreciousmetalDataCenter.getInstance().SALEPRICE_LONGIN.toString()))), 2));//寄存克数乘以当前卖出价,保留两位小数
                                        }
                                    }

                            }
                        }
                    }else{
                            amount.setText("0.0000");
                            reminder_value.setText("参考市值（元）" + "0.00");
                            pay_back.setVisibility(View.GONE);
                    }

                    //在这里判断是否从外置的更多进入,自动进入下一个界面
                    //从外置进入购买，在都完成的情况下，进入购买，0代表从外置进入购买
                    if("0".equals(PreciousmetalDataCenter.getInstance().BUYFLAG)){
                        Intent intent=new Intent();
                        intent.setClass(GoldstoreMainActivity.this, BuyMainActivity.class);
                        PreciousmetalDataCenter.getInstance().BUYFLAG="1";
                        startActivity(intent);
                    }
                    if(("0".equals(PreciousmetalDataCenter.getInstance().BACKFLAG))){
                        //从外置进入判断积存量是否为空为空在此页面报错，停在此页面
                        if(StringUtil.isNullOrEmpty(amountList)){
                            MessageDialog.showMessageDialog(GoldstoreMainActivity.this,"您暂无持仓，无法赎回");
                        }else {
                            String amount=null;
                            Boolean isNull=true;
                            for(int i=0;i<amountList.size();i++){
                                amount=(String)amountList.get(i).get(PreciousmetalDataCenter.AMOUNT);
                                if(!(StringUtil.isNullOrEmpty(amount)||("0".equals(amount)))){
                                    isNull=false;
                                }
                            }
                            if(isNull){
                                MessageDialog.showMessageDialog(GoldstoreMainActivity.this,"您暂无持仓，无法赎回");
                            }else{
                                Intent intent=new Intent();
                                intent.setClass(GoldstoreMainActivity.this, RansomMainActivity.class);
                                PreciousmetalDataCenter.getInstance().BACKFLAG="1";
                                startActivity(intent);
                            }

                        }

                    }
                    if("0".equals(PreciousmetalDataCenter.getInstance().CHECKFLAG)){
                        Intent intent=new Intent();
                        intent.setClass(GoldstoreMainActivity.this, TransQuryActivity.class);
                        PreciousmetalDataCenter.getInstance().CHECKFLAG="1";
                        startActivity(intent);
                    }
                    if("0".equals(PreciousmetalDataCenter.getInstance().ACCOUNTMANAGERFLAG)){
                        Intent intent=new Intent();
                        intent.setClass(GoldstoreMainActivity.this, GoldstoreAccountResetActivity.class);
                        PreciousmetalDataCenter.getInstance().ACCOUNTMANAGERFLAG="1";
                        startActivity(intent);
                    }

                        LoadingDialog.closeDialog();
                        // 执行callbackObject回调后拦截器
                        if (httpRequestCallBackAfter(resultObj)) {
                            break;
                        }

                        /// 执行全局后拦截器
                        if (BaseDroidApp.getInstanse().httpRequestCallBackAfter(
                                resultObj)) {
                            break;
                        }
                        break;
                        case ConstantGloble.HTTP_STAGE_CODE:
                            // 执行code error 全局前拦截器
                            if (BaseDroidApp.getInstanse().httpCodeErrorCallBackPre(
                                    resultHttpCode)) {
                                break;
                            }
                            // 执行callbackObject error code 回调前拦截器
                            if (callbackObject.httpCodeErrorCallBackPre(resultHttpCode)) {

                                break;
                            }
                            Method httpCodeCallbackMethod = null;
                            try {
                                // 回调
                                httpCodeCallbackMethod = callbackObject.getClass()
                                        .getMethod(callBackMethod, String.class);
                                httpCodeCallbackMethod.invoke(callbackObject,
                                        resultHttpCode);
                            } catch (SecurityException e) {
                                LogGloble.e(TAG, "SecurityException ", e);
                            } catch (NoSuchMethodException e) {
                                LogGloble.e(TAG, "NoSuchMethodException ", e);
                            } catch (IllegalArgumentException e) {
                                LogGloble.e(TAG, "IllegalArgumentException ", e);
                            } catch (IllegalAccessException e) {
                                LogGloble.e(TAG, "IllegalAccessException ", e);
                            } catch (InvocationTargetException e) {
                                LogGloble.e(TAG, "InvocationTargetException ", e);
                            } catch (NullPointerException e) {
                                LogGloble.e(TAG, "NullPointerException ", e);
                                throw e;
                            } catch (ClassCastException e) {
                                LogGloble.e(TAG, "ClassCastException ", e);
                                throw e;
                            }
                            // 执行code error 全局后拦截器
                            if (BaseDroidApp.getInstanse().httpCodeErrorCallBackAfter(
                                    resultHttpCode)) {
                                break;
                            }
                            // 执行callbackObject code error 后拦截器
                            if (callbackObject.httpCodeErrorCallBackAfter(resultHttpCode)) {
                                break;
                            }
                            break;

                        default:
                            break;
                    }
            }
            ;

    };

            //牌价字体格式化
            public SpannableStringBuilder SpanString(String string){
                SpannableStringBuilder stringBuilder=new SpannableStringBuilder(string);
                stringBuilder.setSpan(new TextAppearanceSpan(null, 0, 70, null, null),
                        4, 6, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                return stringBuilder;
            }
    public String head(String string){
        String head=string.substring(0,string.indexOf("."));
        return  head;
    }
    public String end(String string){
        String end=string.substring(string.indexOf("."),string.length());
        return  end;
    }


        @Override
        protected void onPause() {
            super.onPause();
            if (!StringUtil.isNullOrEmpty(PreciousmetalDataCenter.getInstance().LOGINPRICEMAP)) {
                HttpManager.stopPolling();
            }


        }
    //对报错信息进行处理
    @Override
    public boolean doBiihttpRequestCallBackPre(BiiResponse response) {
        BaseHttpEngine.dissMissProgressDialog();
        List<BiiResponseBody> biiResponseBodyList = response.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodyList.get(0);
        if (!StringUtil.isNullOrEmpty(biiResponseBodyList)) {
            for (BiiResponseBody body : biiResponseBodyList) {
                //6号接口报错只报一次
                if(PreciousmetalDataCenter.PSNGOLDSTOREGOODSLIST
                        .equals(biiResponseBody.getMethod())) {
                    BiiHttpEngine.dissMissProgressDialog();
                    BiiError biiError = biiResponseBody.getError();
                    // 判断是否存在error
                    if (biiError != null && biiError.getCode() != null) {
                        if (isFirstRefresh) {
                            isFirstRefresh = false;
                            return super.doBiihttpRequestCallBackPre(response);
                        } else {
                            return false;
                        }
                    }
                }
                else if (PreciousmetalDataCenter.PSNGOLDSTOREACCOUNTQUERY
                        .equals(biiResponseBody.getMethod())) {
                    BiiHttpEngine.dissMissProgressDialog();
                    BiiError biiError = biiResponseBody.getError();
                    // 判断是否存在error
                    if (biiError != null && biiError.getCode() != null) {

                        if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
                            return super.doBiihttpRequestCallBackPre(response);
                        }
                        final View.OnClickListener listener=new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityTaskManager.getInstance().removeAllSecondActivity();
                            }
                        };
                        if (biiError.getCode().equals("role.user_has_not_enough_rights")) {
                            MessageDialog.showMessageDialog(GoldstoreMainActivity.this,"您尚未开通此服务",listener);
                            return false;
                        }

                        return super.doBiihttpRequestCallBackPre(response);
                    }
                    //4号接口报错，按钮不能点击并且置灰
                }else if(PreciousmetalDataCenter.PSNGOLDSTOREPRICELIST
                        .equals(biiResponseBody.getMethod())){
                    BiiError biiError = biiResponseBody.getError();
                    if (biiError != null && biiError.getCode() != null) {
                        //无牌价或者暂停牌价的报错，屏蔽4号牌价接口
                    if(biiError.getCode().equals("XPADG.G040")){
                        if(isrefeshflag){
                            pull_down_layout.loadmoreCompleted(RefreshDataStatus.Failed);
                        }
                        stop_trad.setVisibility(View.VISIBLE);
                        buy_button.setVisibility(View.GONE);
                        pay_back.setVisibility(View.GONE);
                        reminder_value.setText("参考市值（元）--");
                        buyPrice_head.setText("--");
                        buyPrice_end.setText("");
                        sellPrice_head.setText("--");
                        sellPrice_end.setText("");
                        PreciousmetalDataCenter.getInstance().loginIsHave=false;
                        return false;
                    }
                        if(biiError.getCode().equals("XPADG.G041")){
                            stop_trad.setVisibility(View.VISIBLE);
                            buy_button.setVisibility(View.GONE);
                            pay_back.setVisibility(View.GONE);
                            reminder_value.setText("参考市值（元）--");
                            buyPrice_head.setText("--");
                            buyPrice_end.setText("");
                            sellPrice_head.setText("--");
                            sellPrice_end.setText("");
                            PreciousmetalDataCenter.getInstance().loginIsHave=false;
                            return false;
                        }
                        //如果6号接口报错，集训调用6号接口
                    }
//                    else if(PreciousmetalDataCenter.PSNGOLDSTOREGOODSLIST
//                            .equals(biiResponseBody.getMethod())){
//                        getHttpTools().requestHttp(PreciousmetalDataCenter.PSNGOLDSTOREGOODSLIST,
//                                "requestPsnGoldStoreGoodsListCallback", null,
//                                false);
//                    }
                }
            }

        } else {
            return super.doBiihttpRequestCallBackPre(response);
        }

        return super.doBiihttpRequestCallBackPre(response);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HttpManager.stopPolling();
    }

    public void requestPsnGoldStorePriceListCallbackagain(
            Object resultObj) {
        BaseHttpEngine.dissMissProgressDialog();
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        List<Map<String, Object>> resultMap = (List<Map<String, Object>>) biiResponseBody
                .getResult();
        if (StringUtil.isNullOrEmpty(resultMap)) {
            BaseHttpEngine.dissMissProgressDialog();
            return;
        }
        //如果6号接口报错，则重新调用此接口
        if(!isFirstRefresh){
            getHttpTools().requestHttpWithNoDialog(PreciousmetalDataCenter.PSNGOLDSTOREGOODSLIST,
                    "requestPsnGoldStoreGoodsListCallback", null,
                    false);
        }

        if (StringUtil.isNullOrEmpty(resultMap)) {
            PreciousmetalDataCenter.getInstance().LOGINPRICEMAP=null;
            return;
        }
        PreciousmetalDataCenter.getInstance().LOGINPRICEMAP = resultMap;
        buy_button.setVisibility(View.VISIBLE);
        stop_trad.setVisibility(View.GONE);
        PreciousmetalDataCenter.getInstance().loginIsHave=true;

        for (int i = 0; i < resultMap.size(); i++) {
            if ((PreciousmetalDataCenter.getInstance().CODE.equals((String) resultMap.get(i).get(PreciousmetalDataCenter.CURRCODE)))) {

                PreciousmetalDataCenter.getInstance().BUYPRICE_LONGIN = (String) (resultMap.get(i).get(PreciousmetalDataCenter.BUYPRICE));
                PreciousmetalDataCenter.getInstance().SALEPRICE_LONGIN = (String) (resultMap.get(i).get(PreciousmetalDataCenter.SALEPRICE));
                PreciousmetalDataCenter.getInstance().curDetail = resultMap.get(i);
                beijing_time.setText("数据更新于北京时间" + (String) (resultMap.get(i).get(PreciousmetalDataCenter.PRICETIME)) + "，具体价格以实际成交为准");
                if(StringUtil.isNullOrEmpty((String) resultMap.get(i).get(PreciousmetalDataCenter.BUYPRICE))){
                    stop_trad.setVisibility(View.VISIBLE);
                    buy_button.setVisibility(View.GONE);
                    pay_back.setVisibility(View.GONE);
                }else{
                    stop_trad.setVisibility(View.GONE);
                    buy_button.setVisibility(View.VISIBLE);
//                                pay_back.setVisibility(View.VISIBLE);
                    buy_button.setEnabled(true);
                    buy_button.setBackgroundColor(getResources().getColor(R.color.goldstore_red));
                    buyPrice_head.setText(head((String) resultMap.get(i).get(PreciousmetalDataCenter.BUYPRICE)));
                    buyPrice_end.setText(end((String) resultMap.get(i).get(PreciousmetalDataCenter.BUYPRICE)));
                }
                if(StringUtil.isNullOrEmpty((String) resultMap.get(i).get(PreciousmetalDataCenter.SALEPRICE))){
//                               pay_back.setEnabled(false);
//                               pay_back.setBackgroundColor(getResources().getColor(R.color.storegold_cantgray));
                    stop_trad.setVisibility(View.VISIBLE);
                    buy_button.setVisibility(View.GONE);
                    pay_back.setVisibility(View.GONE);
                }else{
                    stop_trad.setVisibility(View.GONE);
                    buy_button.setVisibility(View.VISIBLE);
//                               pay_back.setVisibility(View.VISIBLE);
                    pay_back.setEnabled(true);
                    pay_back.setBackgroundColor(getResources().getColor(R.color.white));
                    sellPrice_head.setText(head((String) resultMap.get(i).get(PreciousmetalDataCenter.SALEPRICE)));
                    sellPrice_end.setText(end((String) resultMap.get(i).get(PreciousmetalDataCenter.SALEPRICE)));
                }
            }
        }
        //遍历7号接口，code一样显示对应参考市值
        //7号接口的遍历，更改积存量和参考市值
        if(!StringUtil.isNullOrEmpty(amountList)){
            for (int i = 0; i < amountList.size(); i++) {
                if ((PreciousmetalDataCenter.getInstance().CODE).equals
                        (amountList.get(i).get(PreciousmetalDataCenter.CURRCODE))) {
                    jicunAmount = (String) (amountList.get(i).get(PreciousmetalDataCenter.AMOUNT));
                    //买入价如果为空。直接不显示赎回
                    if((StringUtil.isNullOrEmpty(buyPrice_head.getText())||(StringUtil.isNullOrEmpty(buyPrice_end.getText())))){
                        pay_back.setVisibility(View.GONE);
                        buy_button.setVisibility(View.GONE);
                        stop_trad.setVisibility(View.VISIBLE);
                    }else{
                        if(0==Double.parseDouble(jicunAmount)){
                            amount.setText("0.0000");
                            reminder_value.setText("参考市值（元）" + "0.00");
                            pay_back.setVisibility(View.GONE);
                        }else{
                            amount.setText(StringUtil.parseStringPattern(jicunAmount,4));
                            pay_back.setVisibility(View.VISIBLE);
                            reminder_value.setText("参考市值（元）" + StringUtil.parseStringPattern(String.valueOf(Double.parseDouble(jicunAmount)
                                    * (Double.parseDouble(PreciousmetalDataCenter.getInstance().SALEPRICE_LONGIN.toString()))), 2));//寄存克数乘以当前卖出价,保留两位小数
                        }
                    }

                }
            }
        }else{
            amount.setText("0.0000");
            reminder_value.setText("参考市值（元）" + "0.00");
            pay_back.setVisibility(View.GONE);
        }
        pull_down_layout.loadmoreCompleted(RefreshDataStatus.Successed);
    }
    //网络异常时触发
    @Override
    public void commonHttpErrorCallBack(String requestMethod) {
        if(isrefeshflag){
            pull_down_layout.loadmoreCompleted(RefreshDataStatus.Failed);
        }
        super.commonHttpErrorCallBack(requestMethod);

    }
}

