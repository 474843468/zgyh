package com.chinamworld.bocmbci.biz.preciousmetal.goldstoremainpage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.GoldBonus;
import com.chinamworld.bocmbci.biz.goldbonus.busitrade.BusiTradeAvtivity;
import com.chinamworld.bocmbci.biz.goldbonus.goldbonusoutside.GoldBonusOutLayActivity;
import com.chinamworld.bocmbci.biz.preciousmetal.PreciousmetalBaseActivity;
import com.chinamworld.bocmbci.biz.preciousmetal.PreciousmetalDataCenter;
import com.chinamworld.bocmbci.biz.preciousmetal.goldstoreaccount.GoldstoreAccountResetActivity;
import com.chinamworld.bocmbci.biz.preciousmetal.goldstoreaccount.GoldstoreAccountSetActivity;
import com.chinamworld.bocmbci.biz.preciousmetal.goldstorebuy.BuyMainActivity;
import com.chinamworld.bocmbci.biz.preciousmetal.goldstoreransom.RansomMainActivity;
import com.chinamworld.bocmbci.biz.preciousmetal.goldstoretransquery.TransQuryActivity;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.llbt.userwidget.dialogview.MessageDialog;

public class GoldstoreMoreActivity extends PreciousmetalBaseActivity {

    private RelativeLayout buy_layout;
    private RelativeLayout back_layout;
    private RelativeLayout check_layout;
    private RelativeLayout accmanager_layout;
    private RelativeLayout problem_layout;
    private boolean isLogin;
    private TextView account_number;
    private View.OnClickListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goldstore_more);
        setTitle("更多");
        listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        buy_layout=(RelativeLayout) findViewById(R.id.buy_layout);
        back_layout=(RelativeLayout)findViewById(R.id.back_layout);
        check_layout=(RelativeLayout)findViewById(R.id.chaeck_layout);
        accmanager_layout=(RelativeLayout)findViewById(R.id.accmanager_layout);
        problem_layout=(RelativeLayout)findViewById(R.id.problem_layout);
        account_number=(TextView)findViewById(R.id.account_number);
       TextView metalRight=(TextView)findViewById(R.id.ib_top_right_btn);
        metalRight.setVisibility(View.GONE);
        isLogin= BaseDroidApp.getInstanse().isLogin();
        if(isLogin){
            account_number.setText(StringUtil
                    .getForSixForString(PreciousmetalDataCenter.getInstance().AccountNumberOld));
            //是否为停牌或无牌价的状态true为不是停牌
            if(PreciousmetalDataCenter.getInstance().loginIsHave){
                //如果登陆，改进哪个界面进那个界面
                buy_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent();
                        intent.setClass(GoldstoreMoreActivity.this, BuyMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                back_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //积存量为空报错
                        if(StringUtil.isNullOrEmpty(PreciousmetalDataCenter.getInstance().AMOUNTLIST)){
                            MessageDialog.showMessageDialog(GoldstoreMoreActivity.this,"您暂无持仓，无法赎回",listener);
                            return;
                        }else {

                            String amount=null;
                            Boolean isNull=false;
                            for(int i=0;i<PreciousmetalDataCenter.getInstance().AMOUNTLIST.size();i++){
                                amount=(String)PreciousmetalDataCenter.getInstance().AMOUNTLIST.get(i).get(PreciousmetalDataCenter.AMOUNT);
                                if((StringUtil.isNullOrEmpty(amount)||(0==Double.parseDouble(amount)))){
                                    isNull=true;
                                }
                            }
                            if(isNull){
//                            MessageDialog.showMessageDialog(GoldstoreMoreActivity.this,"您暂无持仓，无法赎回");
                                MessageDialog.showMessageDialog(GoldstoreMoreActivity.this, "您暂无持仓，无法赎回", listener);
                                return;
                            }else{
                                Intent intent=new Intent();
                                intent.setClass(GoldstoreMoreActivity.this, RansomMainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        }
                    }
                });
            }else{
                buy_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MessageDialog.showMessageDialog(GoldstoreMoreActivity.this, "暂停交易或未获取产品与牌价", listener);
                        return;
                    }
                });
                back_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MessageDialog.showMessageDialog(GoldstoreMoreActivity.this, "暂停交易或未获取产品与牌价", listener);
                        return;
                    }
                });

            }



            check_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent();
                    intent.setClass(GoldstoreMoreActivity.this, TransQuryActivity.class);
                    startActivity(intent);
                    finish();

                }
            });
            accmanager_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PreciousmetalDataCenter.getInstance().BUYFLAG_CHANGE="1";
                    Intent intent=new Intent();
                    intent.setClass(GoldstoreMoreActivity.this, GoldstoreAccountResetActivity.class);
                    startActivity(intent);
                    finish();

                }
            });
        }else{//如果未登陆，先跳登陆界面，进行首页的流程然后进入相应的界面
            account_number.setVisibility(View.INVISIBLE);
            //是否为无牌价的情况true为不是,falsewei为无牌家
            if(PreciousmetalDataCenter.getInstance().NologinIsHave){
                buy_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseActivity.getLoginUtils(GoldstoreMoreActivity.this).exe(new LoginTask.LoginCallback() {

                            @Override
                            public void loginStatua(boolean arg0) {
                                // TODO Auto-generated method stub
                                PreciousmetalDataCenter.getInstance().BUYFLAG="0";
                                Intent intent = new Intent(
                                        GoldstoreMoreActivity.this,
                                        GoldstoreMainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });



                    }
                });
                back_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseActivity.getLoginUtils(GoldstoreMoreActivity.this).exe(new LoginTask.LoginCallback() {

                            @Override
                            public void loginStatua(boolean arg0) {
                                // TODO Auto-generated method stub
                                PreciousmetalDataCenter.getInstance().BACKFLAG="0";
                                Intent intent = new Intent(
                                        GoldstoreMoreActivity.this,
                                        GoldstoreMainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                });
            }else{
                buy_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MessageDialog.showMessageDialog(GoldstoreMoreActivity.this, "暂停交易或未获取产品与牌价",listener);
                        return;
                    }
                });

                back_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MessageDialog.showMessageDialog(GoldstoreMoreActivity.this, "暂停交易或未获取产品与牌价", listener);
                        return;
                    }
                });


            }

            check_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity.getLoginUtils(GoldstoreMoreActivity.this).exe(new LoginTask.LoginCallback() {

                        @Override
                        public void loginStatua(boolean arg0) {
                            // TODO Auto-generated method stub
                            PreciousmetalDataCenter.getInstance().CHECKFLAG="0";
                            Intent intent = new Intent(
                                    GoldstoreMoreActivity.this,
                                    GoldstoreMainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            });
            accmanager_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity.getLoginUtils(GoldstoreMoreActivity.this).exe(new LoginTask.LoginCallback() {

                        @Override
                        public void loginStatua(boolean arg0) {
                            // TODO Auto-generated method stub
                            PreciousmetalDataCenter.getInstance().ACCOUNTMANAGERFLAG="0";
                            Intent intent = new Intent(
                                    GoldstoreMoreActivity.this,
                                    GoldstoreMainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            });
        }
        problem_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(GoldstoreMoreActivity.this,GoldstoreNormalProblemActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
