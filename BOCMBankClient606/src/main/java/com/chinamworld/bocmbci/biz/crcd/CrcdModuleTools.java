package com.chinamworld.bocmbci.biz.crcd;

import android.app.Activity;
import android.content.Intent;

import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.base.application.CommonApplication;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.MyCardmessageAndSetActivity;
import com.chinamworld.bocmbci.biz.crcd.mycrcd.MyCreditCardActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.IHttpResponseCallBack;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utiltools.HttpHandle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/15.
 */

public class CrcdModuleTools extends HttpHandle {
    public Activity mActivity;
    String crcdPoint;// 信用卡积分
    String accountId;
    String cardType;
    int position;

    public CrcdModuleTools(Activity activity) {
        super(activity);
        mActivity = activity;
        CommonApplication.getInstance().setCurrentAct(activity); // 从软件中心模块跳过来，需要先保存当前的activity

    }

    public void gotoCardmessageAndSetActivity(Activity context,List<Map<String, Object>> bankAccountList ,String accountSeq) {
        // TODO Auto-generated method stub
            this.accountId=accountSeq;
            for(int i=0;i<bankAccountList.size();i++){
                String id=(String)bankAccountList.get(i).get("accountId");
               if(accountId.equals(id)){
                   position=i;
                   break;
               }
            }
            MyCreditCardActivity.crcdAccount = bankAccountList.get(position);
            cardType = String.valueOf(MyCreditCardActivity.crcdAccount.get(Crcd.CRCD_ACCOUNTTYPE_RES));
            BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.IS_EBANK_0, 2);
            BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.CRCD_BANKACCOUNTLIST, bankAccountList);
            requestPsnQueryCrcdPoint();


    }
    // 请求信用卡积分
    private void requestPsnQueryCrcdPoint() {
        HashMap<String, Object> paramsmap = new HashMap<String, Object>();
        paramsmap.put(Crcd.CRCD_ACCOUNTID_RES, accountId);

        httpTools.requestHttp(Crcd.CRCD_PSNQUERYCRCDPOINT, paramsmap, new IHttpResponseCallBack<Map<String, Object>>() {

            public void httpResponseSuccess(Map<String, Object> result, String method) {

                if (StringUtil.isNullOrEmpty(result)) {
                    crcdPoint = "-";
                } else {
                    crcdPoint = (String) result.get(Crcd.CRCD_CONSUMPTIONPOINT);
                }

                requestPsnCrcdQueryGeneralInfo();
            }
        });
        httpTools.registAllErrorCode(Crcd.CRCD_PSNQUERYCRCDPOINT);
    }


    /** 信用卡综合信息查询 */
    private void requestPsnCrcdQueryGeneralInfo() {
        Map<String, Object> paramsmap = new HashMap<String, Object>();
        paramsmap.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
        httpTools.requestHttp(Crcd.CRCD_PSNCRCDQUERYGENERALINFO,paramsmap, new IHttpResponseCallBack<Map<String, Object>>(){
            @Override
            public void httpResponseSuccess(Map<String, Object> result, String method) {
                BaseHttpEngine.dissMissProgressDialog();
                TranDataCenter.getInstance().setCrcdGeneralInfo(result);
                Intent it = new Intent(mActivity, MyCardmessageAndSetActivity.class);
                it.putExtra(Crcd.CRCD_ACCOUNTID_RES, accountId);// 账户ID
                it.putExtra(Crcd.CRCD_ACCOUNTTYPE_RES, cardType);//  卡类型
                it.putExtra(Crcd.CRCD_CRCDPOINT, crcdPoint);//  积分
                TranDataCenter.getInstance().setAccInInfoMap(MyCreditCardActivity.crcdAccount);
                it.putExtra(ConstantGloble.ACC_POSITION, position);
                mActivity.startActivity(it);
            }
        } );
    }
}
