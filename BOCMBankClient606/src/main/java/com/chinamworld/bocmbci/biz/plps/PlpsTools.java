package com.chinamworld.bocmbci.biz.plps;

import android.app.Activity;
import android.content.Intent;

import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.base.application.CommonApplication;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.peopleservice.ui.BTCUiActivity;
import com.chinamworld.bocmbci.biz.plps.interprovincial.InterproLodisActivity;
import com.chinamworld.bocmbci.biz.plps.liveservice.CommServiceActivity;
import com.chinamworld.bocmbci.biz.plps.payment.project.AddCommonServiceDialogActivity;
import com.chinamworld.bocmbci.biz.plps.payment.project.dialogactivity.PaymentMainDialogActivity;
import com.chinamworld.bocmbci.biz.plps.payment.project.dialogactivity.PensionServiceDialogActivity;
import com.chinamworld.bocmbci.biz.plps.prepaid.PrepaidCardDialogActivity;
import com.chinamworld.bocmbci.biz.plps.smallservice.LowServiceActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.IHttpResponseCallBack;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utiltools.HttpHandle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zxj on 2016/8/30.
 * 常用缴费 点击小类跳转入口
 * 全部缴费 点击小类跳转入口
 */
public class PlpsTools extends HttpHandle {

//    private HttpTools httpTools;
    private Activity mActivity;
    public static boolean isAddCommService = false;

    public PlpsTools(Activity activity){
        super(activity);
        mActivity = activity;
        CommonApplication.getInstance().setCurrentAct(activity); // 从软件中心模块跳过来，需要先保存当前的activity
    }

    /**
     * 常用缴费项目 小类 点击入口
     * prvcShortName 小类省简称
     * flowFileIdt 缴费商户名称id 即商户id
     * catName 小类名称
     * menuName小类对应的服务大类名
     * merchantName 商户名称
     * conversationId 会话id需要跟PsnPlpsQueryHistoryRecords接口会话保持一致*/
    public void gotoComonService(Activity activity, String prvcShortName,String flowFileId, String catName, String menuName,String merchantName,String conversationId){
        BTCUiActivity.IntentToParseXML(activity, prvcShortName,flowFileId, catName, menuName, merchantName,null,null,null, conversationId);
    }
    /**
     * 全部缴费项目 小类 点击入口
     *List<Map<String, Object>> commPaymentList 常用缴费项目列表
     * catId 小类id
     * catName 小类名
     * isAlailiable 置灰标识
     * prvcDispName 省名称
     * cityDispName 城市名
     * cityDispNo市多语言代码
     * menuName小类对应的服务大类名
     * prvcShortName 省简称
     **/
    String isAlailiablet = null;
    public void gotoAllPayment(Activity activity, List<Map<String, Object>> commPaymentList,String catId, String catName,String isAlailiable, String prvcDispName,String cityDispNo, String cityDispName,String menuName, String prvcShortName){
//        httpTools = new HttpTools(activity,this);
        this.mActivity = activity;
        PlpsDataCenter.getInstance().setCommonPaymentList(commPaymentList);
        PlpsDataCenter.getInstance().setCatName(catName);
        PlpsDataCenter.getInstance().setItemId(catId);;
        isAlailiablet = isAlailiable;
        PlpsDataCenter.getInstance().setPrvcDispName(prvcDispName);
        PlpsDataCenter.getInstance().setCityDispName(cityDispName);
        PlpsDataCenter.getInstance().setDisplayNo(cityDispNo);
        PlpsDataCenter.getInstance().setMenuName(menuName);
        PlpsDataCenter.getInstance().setPrvcShortName(prvcShortName);
        BaseHttpEngine.showProgressDialog();
        httpTools.requestHttp(Comm.CONVERSATION_ID_API, "requestCommConversationIdCallBack", null);

    }
    public void requestCommConversationIdCallBack(Object resultObj){
        isAddCommService = false;
        String conversationId = (String)httpTools.getResponseResult(resultObj);
        BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.CONVERSATION_ID,conversationId);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(Plps.ITEMID, PlpsDataCenter.getInstance().getItemId());
        params.put(Plps.QUERYTYPET, "3");
        params.put(Plps.PRVCSHORTNAME, PlpsDataCenter.getInstance().getPrvcShortName());
        params.put(Plps.CITYDISPNO, PlpsDataCenter.getInstance().getDisplayNo());
        httpTools.requestHttpWithConversationId(Plps.QUERYFLOWFILEA, params, conversationId, new IHttpResponseCallBack<Map<String, Object>>() {
            @Override
            public void httpResponseSuccess(Map<String, Object> result, String s) {
                BaseHttpEngine.dissMissProgressDialog();
                List<Map<String, Object>> queryFlowFile = new ArrayList<Map<String,Object>>();
                if(!StringUtil.isNullOrEmpty(result)){
                    queryFlowFile = (List<Map<String, Object>>) result
                            .get(Plps.FLOWFILELIST);
                }
                if(StringUtil.isNullOrEmpty(queryFlowFile)){return;}
                PlpsDataCenter.getInstance().setQueryFlowFile(queryFlowFile);
                Intent intent = new Intent(mActivity, LowServiceActivity.class);
                intent.putExtra("isAvailable", isAlailiablet);
                mActivity.startActivity(intent);
            }
        });
    }
    /**本地小类缴费入口
     * localId 本地缴费标识
     *localId=-1 养老金服务
     *  localId=-2签约代缴服务  prvcShortName省简称(签约)
     *  localId=-3 预付卡
     *  localId=-4 交通异地罚款*/
    public void gotoLocalService(Activity activity, int localId, String prvcShortName){
        this.mActivity = activity;
        Intent intent = new Intent();
        if(localId == -1){
            intent.setClass(mActivity, PensionServiceDialogActivity.class);
        }else if (localId == -2){
            PlpsDataCenter.getInstance().setPrvcShortName(prvcShortName);
            intent.setClass(mActivity, PaymentMainDialogActivity.class);
        }else if(localId == -3){
           intent.setClass(mActivity, PrepaidCardDialogActivity.class);
        }else if(localId == -4){
            intent.setClass(mActivity, InterproLodisActivity.class);
        }
        mActivity.startActivity(intent);
    }
    /**缴费记录查询跳转
     * 我的民生二级菜单 liveMenus
     * */
    public void gotoCommService(Activity activity){
        this.mActivity = activity;
        requestPlpsMenu();
    }
    /**
     * 请求民生服务菜单
     */
    private void requestPlpsMenu() {
        BaseHttpEngine.showProgressDialog();
        httpTools.requestHttp(Plps.METHODLIVESERVICEMENUS, "plpsMenuCallBack", null);
    }

    public void plpsMenuCallBack(Object resultObj){
        BaseHttpEngine.dissMissProgressDialog();
        List<Map<String, Object>> mList = (List<Map<String, Object>>)httpTools.getResponseResult(resultObj);
        if (StringUtil.isNullOrEmpty(mList))
            return;
        PlpsDataCenter.getInstance().setLiveMenus(mList);
        Intent intent = new Intent();
        intent.setClass(mActivity, CommServiceActivity.class);
        mActivity.startActivity(intent);
    }

    /**添加常用入口
     * List<Map<String, Object>> commPaymentList 常用缴费项目列表
     * List<Map<String, Object>> allPaymentList 所有缴费项目列表
     * prvcShortName 省简称
     * cityDispNo 市多语言代码
     * prvcDispName 省名称
     * cityDispName 城市名
     *
     * */
    public void addCommService(Activity activity, List<Map<String, Object>> commPaymentList,List<Map<String, Object>> allPaymentList, String prvcShortName, String cityDispNo,String prvcDispName, String cityDispName,String conversationId ){
        this.mActivity = activity;
        PlpsDataCenter.getInstance().setCommonPaymentList(commPaymentList);
        PlpsDataCenter.getInstance().setAllPaymentList(allPaymentList);
        PlpsDataCenter.getInstance().setPrvcShortName(prvcShortName);
        PlpsDataCenter.getInstance().setCityDispName(cityDispName);
        PlpsDataCenter.getInstance().setPrvcDispName(prvcDispName);
        PlpsDataCenter.getInstance().setDisplayNo(cityDispNo);
        BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.CONVERSATION_ID, conversationId);
        isAddCommService = true;

        requestPlpsMenuService();
    }
    /**
     * 请求民生服务菜单
     */
    private void requestPlpsMenuService() {
        BaseHttpEngine.showProgressDialog();
        httpTools.requestHttp(Plps.METHODLIVESERVICEMENUS, "requestPlpsMenuServiceCallBack", null);
    }
    public void requestPlpsMenuServiceCallBack(Object resultObj){
        BaseHttpEngine.dissMissProgressDialog();
        List<Map<String, Object>> mList = (List<Map<String, Object>>)httpTools.getResponseResult(resultObj);
        if (StringUtil.isNullOrEmpty(mList))
            return;
        PlpsDataCenter.getInstance().setLiveMenus(mList);
        Intent intent = new Intent();
        intent.setClass(mActivity, AddCommonServiceDialogActivity.class);
        mActivity.startActivity(intent);
    }
}
