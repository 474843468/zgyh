package com.chinamworld.bocmbci.biz.finc.finc_p606;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.finc.accmanager.FincAccManagerMenuActivity;
import com.chinamworld.bocmbci.biz.finc.fundprice.FundPricesMenuActivity;
import com.chinamworld.bocmbci.biz.finc.myfund.MyFincMainActivity;
import com.chinamworld.bocmbci.biz.finc.orcm.OrcmProductListActivity;
import com.chinamworld.bocmbci.biz.finc.query.FundDQDEMenuActivity;
import com.chinamworld.bocmbci.biz.finc.query.FundqueryMenuActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.userwidget.investview.InvestHelpActivity;
import com.chinamworld.bocmbci.utils.ActivityIntentTools;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * Created by Administrator on 2016/10/10 0010.
 * 更多菜单页面
 */
public class FincMenuMoreActivity extends FincBaseActivity implements View.OnClickListener{
    private boolean isLogin;
    private  Class<?> intentActivity;
    private String intentFlag="0" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finc_main_more);
        setTitle("更多");
 //       getBackGroundLayout().setTitleBackground(R.color.bg_red);
        isLogin= BaseDroidApp.getInstanse().isLogin();
        findViewById(R.id.more_tv1).setOnClickListener(this);
 //       findViewById(R.id.more_tv2).setOnClickListener(this);
        findViewById(R.id.more_tv3).setOnClickListener(this);
        findViewById(R.id.more_tv4).setOnClickListener(this);
        findViewById(R.id.more_tv5).setOnClickListener(this);
        findViewById(R.id.more_tv6).setOnClickListener(this);
//        findViewById(R.id.more_tv7).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.more_tv1://我的基金
                intentActivity = MyFincMainActivity.class;
                intentToActivity(isLogin,intentActivity);
                break;
//            case R.id.more_tv2://行情查询及购买
//                intentToActivity(isLogin,FundPricesMenuActivity.class);
//                break;
            case R.id.more_tv3://基金定投管理
                intentActivity = FundDQDEMenuActivity.class;
                intentToActivity(isLogin,intentActivity);
                break;
            case R.id.more_tv4://账户管理
                intentActivity = FincAccManagerMenuActivity.class;
                intentToActivity(isLogin,intentActivity);
                break;
            case R.id.more_tv5://交易查询
                intentActivity = FundqueryMenuActivity.class;
                intentToActivity(isLogin,intentActivity);
                break;
            case R.id.more_tv6://基金推荐
                intentActivity = OrcmProductListActivity.class;
                intentFlag = "6";
                intentToActivity(isLogin,intentActivity);
                break;
//            case R.id.more_tv7://帮助
// //               intentToActivity(isLogin,MyFincMainActivity.class);
//                InvestHelpActivity.showHelpMessage(FincMenuMoreActivity.this,getString(R.string.fund_help_message));
//                finish();
//                break;
        }
    }

    private void intentToActivity(boolean isLogin,final Class<?> classType){
        if(isLogin){
            BaseHttpEngine.showProgressDialog();
            doCheckRequestPsnInvestmentManageIsOpen();
        }else{
            //跳转登陆
            getLoginUtils(FincMenuMoreActivity.this).exe(new LoginTask.LoginCallback() {
                @Override
                public void loginStatua(boolean b) {
                    BaseHttpEngine.showProgressDialog();
                    doCheckRequestPsnInvestmentManageIsOpen();
//                    ActivityIntentTools.intentToActivity(FincMenuMoreActivity.this,classType);
//                    finish();
                }
            });
        }
    }

    @Override
    public void doCheckRequestQueryInvtBindingInfoCallback(
            Object resultObj) {
        super.doCheckRequestQueryInvtBindingInfoCallback(resultObj);
        BaseHttpEngine.dissMissProgressDialog();
        if(!fincControl.ifhaveaccId || !fincControl.ifInvestMent) return;
        intentToActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ConstantGloble.ACTIVITY_REQUEST_MANAGE_CODE:// 开通理财服务
                switch (resultCode) {
                    case RESULT_OK:
                        fincControl.ifInvestMent = true;
                        if (!fincControl.ifhaveaccId) {// 如果还没有基金账户
                            getPopup();
                        }else{
                            intentToActivity();
                        }
                        break;

                    default:
                        fincControl.ifInvestMent = false;
                        getPopup();
                        break;
                }
                break;
            case ConstantGloble.ACTIVITY_REQUEST_SETFINCSACC_CODE:// 开通基金账户
                switch (resultCode) {
                    case RESULT_OK:
                        fincControl.ifhaveaccId = true;
                        if (!fincControl.ifInvestMent) {// 如果还没有基金账户
                            getPopup();
                        }else{
                            intentToActivity();
                        }
                        break;

                    default:
                        fincControl.ifhaveaccId = false;
                        getPopup();
                        break;
                }
                break;
            default:
                break;
        }
    }

    /**
     * 进入要跳转的页面并销毁本页面
     */
    private void intentToActivity(){
        if(intentFlag.equals("6")){
            if(StringUtil.isNullOrEmpty(fincControl.OcrmProductList)){
                fincControl.OcrmProductMap = null;
            }else{
                fincControl.OcrmProductMap = null;
                fincControl.OcrmProductList = null;
            }
        }
        ActivityIntentTools.intentToActivity(FincMenuMoreActivity.this,intentActivity);
        finish();
    }

}
