package com.chinamworld.bocmbci.biz.preciousmetal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountOverview.AccountCurrentHead;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.GoldBonus;
import com.chinamworld.bocmbci.biz.goldbonus.busitrade.BusiTradeAvtivity;
import com.chinamworld.bocmbci.biz.goldbonus.goldbonusoutside.GoldBonusOutLayActivity;
import com.chinamworld.bocmbci.biz.preciousmetal.goldstoremainpage.GoldstoreMainActivity;
import com.chinamworld.bocmbci.biz.preciousmetal.goldstoremainpage.GoldstoreNormalProblemActivity;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.llbt.userwidget.dialogview.MessageDialog;
import com.chinamworld.llbt.userwidget.refreshliseview.IRefreshLayoutListener;
import com.chinamworld.llbt.userwidget.refreshliseview.PullDownRefreshLayout;
import com.chinamworld.llbt.userwidget.refreshliseview.PullToRefreshLayout;
import com.chinamworld.llbt.userwidget.refreshliseview.RefreshDataStatus;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 *
 * 贵金属积存外置界面
 * */
public class PreciousmetalLayoutActivity extends PreciousmetalBaseActivity implements IRefreshLayoutListener {
    private TextView buyPrice_head;
    private TextView buyPrice_end;
    private TextView sellPrice_head;
    private TextView sellPrice_end;
    /**更新时间*/
    private TextView beijing_time;
    /**交易查询*/
    private RelativeLayout trade_check;
    /**常见问题*/
    private TextView problem;
    /**购买按钮*/
    private Button buy_button;
    /**登陆按钮*/
    private Button  goldstore_login;
    /**下拉刷新控件*/
    private PullDownRefreshLayout pull_down_layout;
    //积存类型
    private RelativeLayout trade_type;
    //积存种类
    private TextView goldstore_type;
    //右侧箭头
    private ImageView img_arrow_right;
    //要弹出的对话框
    private View dialogView;
    //弹框列表
    private ListView listView;
    //未登录的接口返回值
    private List<Map<String, Object>> resultMap;
    //对话框
    private CustomDialog customDialog;
    //判断是否从下拉刷新编制
    private boolean isrefeshflag=false;
    //暂停交易按钮
    private Button stop_trad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RelativeLayout backGround = (RelativeLayout) findViewById(R.id.main_layout);
        backGround.setBackgroundColor(this.getResources().getColor(R.color.bg_goldstore));
        setContentView(R.layout.activity_preciousmetal_layout);
        initView();
    }
    public void onLoadMore(View pulltorefreshlayout){

    };
    public void initView(){
        pull_down_layout=(PullDownRefreshLayout)findViewById(R.id.pull_down_layout);
        pull_down_layout.setBackgroundColor(1);
        pull_down_layout.setOnRefreshListener(new IRefreshLayoutListener() {
            @Override
            public void onLoadMore(View pullToRefreshLayout) {
                isrefeshflag=true;
                //调用刷新牌价接口
                /**调用未登录的牌价接口*/
                getHttpTools().requestHttpOutlayWithNoDialog(
                        PreciousmetalDataCenter.PSNGOLDSTOREPRICELISTOUTLAY,
                        "requestPsnGoldStorePriceListOutlayCallbackagain",
                        null,false);
            }
        });
        buyPrice_head = (TextView) findViewById(R.id.prms_price_listiterm1_buyprice_head);
        buyPrice_end=(TextView) findViewById(R.id.prms_price_listiterm1_buyprice_end);
        sellPrice_head= (TextView) findViewById(R.id.prms_price_listiterm1_saleprice_head);
        sellPrice_end= (TextView) findViewById(R.id.prms_price_listiterm1_saleprice_end);
        beijing_time=(TextView)findViewById(R.id.beijing_time);
        trade_check=(RelativeLayout)findViewById(R.id.trade_check);
        problem=(TextView)findViewById(R.id.problem);
        problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(PreciousmetalLayoutActivity.this, GoldstoreNormalProblemActivity.class);
                startActivity(intent);
            }
        });
        buy_button=(Button)findViewById(R.id.buy_button);
        stop_trad=(Button)findViewById(R.id.stop_trad);
        trade_type=(RelativeLayout)findViewById(R.id.trade_type);
        goldstore_type=(TextView)findViewById(R.id.goldstore_type);
        img_arrow_right=(ImageView)findViewById(R.id.img_arrow_right);
        goldstore_login=(Button)findViewById(R.id.goldstore_login);
        goldstore_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转登陆
                BaseActivity.getLoginUtils(PreciousmetalLayoutActivity.this).exe(new LoginTask.LoginCallback() {
                    @Override
                    public void loginStatua(boolean b) {
                        Intent intent = new Intent(
										PreciousmetalLayoutActivity.this,
										GoldstoreMainActivity.class);
								startActivity(intent);
								intent.putExtra(GoldBonus.PASSFLAG, PreciousmetalDataCenter.REQUEST_LOGIN_CODE_BUY);
								finish();
                    }
                });
            }
        });
        buy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //从购买进入界面，保存,0代表从购买进去
                    PreciousmetalDataCenter.getInstance().BUYFLAG="0";
                BaseActivity.getLoginUtils(PreciousmetalLayoutActivity.this).exe(new LoginTask.LoginCallback() {

							@Override
							public void loginStatua(boolean arg0) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(
										PreciousmetalLayoutActivity.this,
										GoldstoreMainActivity.class);
								startActivity(intent);
								finish();
							}
						});
            }
        });
        trade_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //从购买进入界面，保存,0代表从w外置查询进去
                PreciousmetalDataCenter.getInstance().CHECKFLAG="0";
                BaseActivity.getLoginUtils(PreciousmetalLayoutActivity.this).exe(new LoginTask.LoginCallback() {

                    @Override
                    public void loginStatua(boolean arg0) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(
                                PreciousmetalLayoutActivity.this,
                                GoldstoreMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
        /**调用未登录的牌价接口*/
        this.getHttpTools().requestHttpOutlay(
                PreciousmetalDataCenter.PSNGOLDSTOREPRICELISTOUTLAY,
                "requestPsnGoldStorePriceListOutlayCallback",
                null, false);
    }


    // 登陆前请求产品信息回调
    @SuppressWarnings("unchecked")
    public void requestPsnGoldStorePriceListOutlayCallback(
            Object resultObj) {
        BaseHttpEngine.dissMissProgressDialog();
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        resultMap = (List<Map<String, Object>>) biiResponseBody
                .getResult();
        if (StringUtil.isNullOrEmpty(resultMap)) {
            BaseHttpEngine.dissMissProgressDialog();
            return;
        }
        PreciousmetalDataCenter.getInstance().NologinIsHave=true;
        buy_button.setVisibility(View.VISIBLE);
        stop_trad.setVisibility(View.GONE);
        goldstore_type.setText((String)resultMap.get(0).get("currCodeName"));
        PreciousmetalDataCenter.getInstance().CODE=(String)resultMap.get(0).get("currCode");
        //根据返回值判断是否能点积存类型
        if(resultMap.size()>1){
            //显示向右箭头
            img_arrow_right.setVisibility(View.VISIBLE);
            //有不只一种积存类型,默认显示吉祥金
            trade_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogView = View.inflate(PreciousmetalLayoutActivity.this,
                            R.layout.goldstore_choselist, null);
                    listView = (ListView) dialogView.findViewById(R.id.chose_list);
                    final ArrayList arrayList = new ArrayList();
                    for (int i = 0; i < resultMap.size(); i++) {
                        arrayList.add((String) (resultMap.get(i).get(PreciousmetalDataCenter.CURRCODENAME)));
                    }
                    listView.setAdapter(new ArrayAdapter<ArrayList>(PreciousmetalLayoutActivity.this,
                            android.R.layout.simple_list_item_1, arrayList));
//                    BaseDroidApp.getInstanse().showDialog(dialogView);
                    customDialog=new CustomDialog(PreciousmetalLayoutActivity.this,dialogView);
                    customDialog.show();
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //保存当前选择的种类code
                            goldstore_type.setText((String)arrayList.get(position));
                            PreciousmetalDataCenter.getInstance().CODE = (String) resultMap.get(position).get(PreciousmetalDataCenter.CURRCODE);
                            buyPrice_head.setText(head((String) resultMap.get(position).get(PreciousmetalDataCenter.BUYPRICE)));
                            buyPrice_end.setText(end((String) resultMap.get(position).get(PreciousmetalDataCenter.BUYPRICE)));
                            sellPrice_head.setText(head((String) resultMap.get(position).get(PreciousmetalDataCenter.SALEPRICE)));
                            sellPrice_end.setText(end((String) resultMap.get(position).get(PreciousmetalDataCenter.SALEPRICE)));
                            beijing_time.setText("数据更新于北京时间"+resultMap.get(position).get(PreciousmetalDataCenter.PRICETIME)+"，具体价格以实际成交为准");
                            customDialog.cancel();

                        }
                    });
                }
            });

        }else{
            //只有一种积存类型，默认为吉祥金
            img_arrow_right.setVisibility(View.INVISIBLE);
            trade_type.setEnabled(false);
        }
        PreciousmetalDataCenter.getInstance().PRICEMAPLAYOUT=resultMap;
        //遍历选中的交易类型code，来判断是显示哪个牌价信息
        for(int i=0;i<resultMap.size();i++){
            if((PreciousmetalDataCenter.getInstance().CODE).equals
                    (resultMap.get(i).get(PreciousmetalDataCenter.CURRCODE))){
                buyPrice_head.setText(head((String) resultMap.get(i).get(PreciousmetalDataCenter.BUYPRICE)));
                buyPrice_end.setText(end((String) resultMap.get(i).get(PreciousmetalDataCenter.BUYPRICE)));
                sellPrice_head.setText(head((String) resultMap.get(i).get(PreciousmetalDataCenter.SALEPRICE)));
                sellPrice_end.setText(end((String) resultMap.get(i).get(PreciousmetalDataCenter.SALEPRICE)));
                beijing_time.setText("数据更新于北京时间"+resultMap.get(i).get(PreciousmetalDataCenter.PRICETIME)+"，具体价格以实际成交为准");

            }
        }
    }
    public void requestPsnGoldStorePriceListOutlayCallbackagain(
            Object resultObj) {
        BaseHttpEngine.dissMissProgressDialog();
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        resultMap = (List<Map<String, Object>>) biiResponseBody
                .getResult();
        if (StringUtil.isNullOrEmpty(resultMap)) {
            BaseHttpEngine.dissMissProgressDialog();
            return;
        }
        PreciousmetalDataCenter.getInstance().NologinIsHave=true;
        buy_button.setVisibility(View.VISIBLE);
        stop_trad.setVisibility(View.GONE);
        if(resultMap.size()>1){
            //显示向右箭头
            img_arrow_right.setVisibility(View.VISIBLE);
            //有不只一种积存类型,默认显示吉祥金
            trade_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogView = View.inflate(PreciousmetalLayoutActivity.this,
                            R.layout.goldstore_choselist, null);
                    listView = (ListView) dialogView.findViewById(R.id.chose_list);
                    final ArrayList arrayList = new ArrayList();
                    for (int i = 0; i < resultMap.size(); i++) {
                        arrayList.add((String) (resultMap.get(i).get(PreciousmetalDataCenter.CURRCODENAME)));
                    }
                    listView.setAdapter(new ArrayAdapter<ArrayList>(PreciousmetalLayoutActivity.this,
                            android.R.layout.simple_list_item_1, arrayList));
//                    BaseDroidApp.getInstanse().showDialog(dialogView);
                    customDialog=new CustomDialog(PreciousmetalLayoutActivity.this,dialogView);
                    customDialog.show();
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //保存当前选择的种类code
                            goldstore_type.setText((String)arrayList.get(position));
                            PreciousmetalDataCenter.getInstance().CODE = (String) resultMap.get(position).get(PreciousmetalDataCenter.CURRCODE);
                            buyPrice_head.setText(head((String) resultMap.get(position).get(PreciousmetalDataCenter.BUYPRICE)));
                            buyPrice_end.setText(end((String) resultMap.get(position).get(PreciousmetalDataCenter.BUYPRICE)));
                            sellPrice_head.setText(head((String) resultMap.get(position).get(PreciousmetalDataCenter.SALEPRICE)));
                            sellPrice_end.setText(end((String) resultMap.get(position).get(PreciousmetalDataCenter.SALEPRICE)));
                            beijing_time.setText("数据更新于北京时间"+resultMap.get(position).get(PreciousmetalDataCenter.PRICETIME)+"，具体价格以实际成交为准");
                            customDialog.cancel();

                        }
                    });
                }
            });

        }else{
            //只有一种积存类型，默认为吉祥金
            img_arrow_right.setVisibility(View.INVISIBLE);
            trade_type.setEnabled(false);
        }
        PreciousmetalDataCenter.getInstance().PRICEMAPLAYOUT=resultMap;
        //遍历选中的交易类型code，来判断是显示哪个牌价信息
        for(int i=0;i<resultMap.size();i++){
            if((PreciousmetalDataCenter.getInstance().CODE).equals
                    ((String)resultMap.get(i).get(PreciousmetalDataCenter.CURRCODE))){
                buyPrice_head.setText(head((String) resultMap.get(i).get(PreciousmetalDataCenter.BUYPRICE)));
                buyPrice_end.setText(end((String) resultMap.get(i).get(PreciousmetalDataCenter.BUYPRICE)));
                sellPrice_head.setText(head((String) resultMap.get(i).get(PreciousmetalDataCenter.SALEPRICE)));
                sellPrice_end.setText(end((String) resultMap.get(i).get(PreciousmetalDataCenter.SALEPRICE)));
                goldstore_type.setText((String)resultMap.get(i).get("currCodeName"));
                beijing_time.setText("数据更新于北京时间"+resultMap.get(i).get(PreciousmetalDataCenter.PRICETIME)+"，具体价格以实际成交为准");

            }
        }
        pull_down_layout.loadmoreCompleted(RefreshDataStatus.Successed);
    }

    public String head(String string){
        String head=string.substring(0,string.indexOf("."));
        return  head;
    }
    public String end(String string){
        String end=string.substring(string.indexOf("."),string.length());
        return  end;
    }
    //网络异常时触发
    @Override
    public void commonHttpErrorCallBack(String requestMethod) {
        if(isrefeshflag){
            pull_down_layout.loadmoreCompleted(RefreshDataStatus.Failed);
        }
        super.commonHttpErrorCallBack(requestMethod);

    }
    //对报错信息进行处理
    @Override
    public boolean doBiihttpRequestCallBackPre(BiiResponse response) {
        BaseHttpEngine.dissMissProgressDialog();
        List<BiiResponseBody> biiResponseBodyList = response.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodyList.get(0);
        if (!StringUtil.isNullOrEmpty(biiResponseBodyList)) {
            for (BiiResponseBody body : biiResponseBodyList) {
                if (PreciousmetalDataCenter.PSNGOLDSTOREPRICELISTOUTLAY
                        .equals(biiResponseBody.getMethod())) {
                    BiiHttpEngine.dissMissProgressDialog();
                    BiiError biiError = biiResponseBody.getError();
                    // 判断是否存在error
                    if (biiError != null && biiError.getCode() != null) {
                    //在这判断，先拉刷新的时候，遇到报错不报错
                        if(isrefeshflag){
                            PreciousmetalDataCenter.getInstance().NologinIsHave=false;
                            //如果无牌价活着牌价暂停，购买按钮不能点，显示暂停交易
                            buy_button.setVisibility(View.GONE);
                            stop_trad.setVisibility(View.VISIBLE);
                            pull_down_layout.loadmoreCompleted(RefreshDataStatus.Failed);
                            return false;
                        }
                        PreciousmetalDataCenter.getInstance().NologinIsHave=false;
                        //如果无牌价活着牌价暂停，购买按钮不能点，显示暂停交易
                        buy_button.setVisibility(View.GONE);
                        stop_trad.setVisibility(View.VISIBLE);
                        return  super.doBiihttpRequestCallBackPre(response);
                    }
                }
            }
        } else {
            return super.doBiihttpRequestCallBackPre(response);
        }
        return super.doBiihttpRequestCallBackPre(response);
    }



}
