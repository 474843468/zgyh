package com.chinamworld.bocmbci.biz.preciousmetal.goldstoreransom;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.abstracttools.BaseHttpManager;
import com.chinamworld.bocmbci.abstracttools.BaseRUtil;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.preciousmetal.PreciousmetalBaseActivity;
import com.chinamworld.bocmbci.biz.preciousmetal.PreciousmetalDataCenter;
import com.chinamworld.bocmbci.constant.BaseLocalData;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.IHttpResponseCallBack;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.adapter.CommonAdapter;
import com.chinamworld.bocmbci.widget.adapter.ICommonAdapter;
import com.chinamworld.llbt.model.IFunc;
import com.chinamworld.llbt.userwidget.dialogview.LoadingDialog;
import com.chinamworld.llbt.userwidget.dialogview.MessageDialog;
import com.chinamworld.llbt.userwidget.refreshliseview.PullRefreshListView;
import com.chinamworld.llbt.userwidget.refreshliseview.RefreshDataStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 贵金属积存 自提网点主页面
 * Created by linyl on 2016/8/27.
 */
public class BranchQueryActivity extends PreciousmetalBaseActivity
        implements ICommonAdapter<Map<String,Object>>{
    /**贵金属积存 提货网点查询 接口返回 map**/
    Map<String,Object> branchquery;
    /**贵金属积存 更多提货网点 接口返回 map**/
    Map<String,Object> branchlistquery;
    TextView tv_branchName,tv_branchAdd,tv_branchTel,tv_branchMore;
    /**贵金属积存 更多提货网点查询 接口上送**/
    Map<String,Object> requestParamsMap = new HashMap<String, Object>();
    TextView branchTel;
    /**自定义下拉刷新的listview 控件**/
    PullRefreshListView lv_goldStoreBranch;
    /**适配器**/
    private CommonAdapter<Map<String, Object>> adapter;
    List<Map<String,Object>> queryList;
    List<Map<String,Object>> listTotal = new ArrayList<Map<String,Object>>();
    int currentIndex = 0 ;
    int recordNumber = 0 ;
    TextView tv_queryListNull;
    private Map<String,Object> textview = new HashMap<String, Object>();
    /**刷新状态标示**/
    boolean isRefreach = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goldstore_branch_mian);
        getPreviousmeatalBackgroundLayout().setMetalRightVisibility(View.GONE);
        getPreviousmeatalBackgroundLayout().setTitleText("自提网点");
//        tv_branchName = (TextView) findViewById(R.id.goldstore_branch_name);
//        tv_branchAdd = (TextView) findViewById(R.id.goldstore_branch_address);
//        tv_branchTel = (TextView) findViewById(R.id.goldstore_branch_tel);

        branchquery = PreciousmetalDataCenter.getInstance().BranchQueryMap;
//        tv_branchName.setText((String) branchquery.get("branchName"));
//        tv_branchAdd.setText((String) branchquery.get("bankAddr"));
//        tv_branchTel.setText((String) branchquery.get("bankPhone"));
//        tv_queryListNull = (TextView) findViewById(R.id.tv_querylist_null);

//        tv_branchTel.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                Intent myIntentCall = new Intent("android.intent.action.CALL",
//                        Uri.parse("tel:"+tv_branchTel.getText().toString().trim()));
//                startActivity(myIntentCall);
//            }
//        });
        listTotal.add(branchquery);
        listTotal.add(textview);
        lv_goldStoreBranch = (PullRefreshListView) findViewById(R.id.goldstore_branch_listview);
        adapter = new CommonAdapter<Map<String, Object>>(this,listTotal,this);
        lv_goldStoreBranch.initListView(adapter);
        /**隐藏滚动条**/
        lv_goldStoreBranch.setScrollBars();
//        /**更多网点查询**/
//        tv_branchMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                requestCommConversationId();
//                //显示通信框
//                LoadingDialog.showLoadingDialog(BranchQueryActivity.this);
//
//            }
//        });

    }


    @Override
    public void requestCommConversationIdCallBack(Object resultObj) {
        super.requestCommConversationIdCallBack(resultObj);
        requestPsnGoldStoreBranchListQuery(0,true);
    }
    /**
     * 提货网点列表查询
     */
    public void requestPsnGoldStoreBranchListQuery(final int index, final boolean refresh){
        requestParamsMap.put("bankId",(String) ((Map<String,Object>)PreciousmetalDataCenter.getInstance().ACCOUNTQUERYMAP.get("cusInfo"))
                .get("bankId"));//7号接口账户查询 返回 bankid
        requestParamsMap.put("currentIndex",index+"");
        requestParamsMap.put("pageSize","10");//int
        requestParamsMap.put("_refresh",String.valueOf(refresh));
        String conversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
        this.getHttpTools().requestHttpWithConversationId("PsnGoldStoreBranchListQuery", requestParamsMap,
                conversationId,new IHttpResponseCallBack<Map<String,Object>>(){

                    @SuppressWarnings("unchecked")
                    @Override
                    public void httpResponseSuccess(Map<String,Object> result, String method) {
                        tv_branchMore.setVisibility(View.GONE);
                        //关闭通信框
                        LoadingDialog.closeDialog();
                        BaseHttpEngine.dissMissProgressDialog();
                        if(StringUtil.isNullOrEmpty(result)){
                            tv_branchMore.setVisibility(View.GONE);
                            tv_queryListNull.setVisibility(View.VISIBLE);
                            tv_queryListNull.setHeight(getWindowHeight()/3*2);
                            return;
                        }
                        queryList = (List<Map<String,Object>>) result.get("resultList");

                        if(StringUtil.isNullOrEmpty(queryList)){
                            //提示信息
//                            tv_queryListNull.setVisibility(View.VISIBLE);
//                            lv_goldStoreBranch.setVisibility(View.GONE);
                            /**首次调接口 并且无数据返回**/
                            if(!isRefreach){
                                tv_branchMore.setVisibility(View.GONE);
                                tv_queryListNull.setVisibility(View.VISIBLE);
                                tv_queryListNull.setHeight(getWindowHeight()/3*2);
                            }
                            return;
                        }

                        tv_queryListNull.setVisibility(View.GONE);
//                        lv_goldStoreBranch.setVisibility(View.VISIBLE);
//                        lv_goldStoreBranch.initListView(adapter);
                        PreciousmetalDataCenter.getInstance().BranchListQueryMap = queryList;
//                        for(int i = 0; i<queryList.size();i++){
//                            listTotal.add(queryList.get(i));
//                        }
                        recordNumber = Integer.parseInt((String)result.get("recordNumber"));
//                        if(refresh){
//                            adapter.setSourceList(listTotal,-1);
//                        }else {
                            adapter.addSourceList(queryList);
//                            adapter.notifyDataSetChanged();
//                        }

                        if( refresh == true) {
                            /**加载更多**/
                            lv_goldStoreBranch.initData(recordNumber+2, currentIndex, new IFunc<Boolean>() {
                                @Override
                                public Boolean callBack(Object param) {
                                    currentIndex++;
                                    isRefreach = true;
                                    requestPsnGoldStoreBranchListQuery(10*currentIndex,false);

                                    return true;
                                }
                            });
                        }
                        else {
                            if(index > recordNumber){
                                lv_goldStoreBranch.loadmoreCompleted(RefreshDataStatus.NoMoreData);
                            }else{
                                lv_goldStoreBranch.loadmoreCompleted(RefreshDataStatus.Successed);
                            }

                        }



                    }
                });

    }


    @Override
    public View getView(int i, Map<String, Object> currentItem,
                        LayoutInflater Inflater, View convertView, ViewGroup viewGroup) {
        if(currentItem == null) return null;

        if(i == 1){
            convertView = Inflater.inflate(R.layout.goldstore_branch_wenxininfo,null);
            TextView tv_wenxin = (TextView) convertView.findViewById(R.id.tv_wenxin_info);
            tv_wenxin.setText(getForeColorSpan());
            tv_branchMore = (TextView) convertView.findViewById(R.id.goldstore_branch_more);
            tv_queryListNull = (TextView) convertView.findViewById(R.id.tv_querylist_null);
            tv_queryListNull.setVisibility(View.GONE);

            if(listTotal.size() > 2){
                tv_branchMore.setVisibility(View.GONE);
            }
            /**更多网点查询**/
            tv_branchMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_branchMore.setVisibility(View.GONE);
                    requestCommConversationId();
                    //显示通信框
                    LoadingDialog.showLoadingDialog(BranchQueryActivity.this);

                }
            });
//            /**首次调接口 并且无数据返回**/
//            if(tv_branchMore.isShown() && !isRefreach && StringUtil.isNullOrEmpty(queryList)){
//                tv_branchMore.setVisibility(View.GONE);
//                tv_queryListNull.setVisibility(View.VISIBLE);
//                tv_queryListNull.setHeight(getWindowHeight()/3*2);
//            }
            return convertView;
        }
        if(i != 1){
            convertView = Inflater.inflate(R.layout.goldstore_branch_item,null);
            convertView.setBackgroundColor(getResources().getColor(R.color.bg_white));
            TextView branchName = (TextView) convertView.findViewById(R.id.goldstore_branch_name);
            TextView branchAdd = (TextView) convertView.findViewById(R.id.goldstore_branch_address);
            branchTel = (TextView) convertView.findViewById(R.id.goldstore_branch_tel);

//            if(i == 0){
//                tv_branchName.setText((String) branchquery.get("branchName"));
//                tv_branchAdd.setText((String) branchquery.get("bankAddr"));
//                tv_branchTel.setText((String) branchquery.get("bankPhone"));
//            }else if(i >= 2) {
            branchName.setText((String) currentItem.get("branchName"));
            branchAdd.setText((String) currentItem.get("bankAddr"));
            branchTel.setText((String) currentItem.get("bankPhone"));
//            }
            /**电话链接 有电话显示时才可点击拨打电话**/
            if(!StringUtil.isNullOrEmpty(currentItem.get("bankPhone"))){
                branchTel.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent myIntentCall = new Intent("android.intent.action.CALL",
                                Uri.parse("tel:"+branchTel.getText().toString().trim()));
                        startActivity(myIntentCall);
                    }
                });
            }
        }
        return convertView;

    }

    @Override
    public void commonHttpErrorCallBack(final String requestMethod) {
//        super.commonHttpErrorCallBack(requestMethod);
        if(!"Logout".equals(requestMethod)) {
            if(!"PNS001".equals(requestMethod)) {
                String message = this.getResources().getString(BaseRUtil.Instance.getID("R.string.communication_fail"));
                LogGloble.e("BranchQueryActivity", "请求失败的接口名称" + requestMethod);
                MessageDialog.showMessageDialog(BranchQueryActivity.this,message,new View.OnClickListener() {
                    public void onClick(View v) {
                        MessageDialog.closeDialog();
                        if(BaseHttpManager.Instance.getcanGoBack() || BaseLocalData.queryCardMethod.contains(requestMethod)) {
                            BranchQueryActivity.this.finish();
                            BaseHttpManager.Instance.setCanGoBack(false);
                        }
                    }
                });
            }
        }
        if("PsnGoldStoreBranchListQuery".equals(requestMethod) && isRefreach){//刷新状态
            lv_goldStoreBranch.loadmoreCompleted(RefreshDataStatus.Failed);
        }
    }

    /**
     * 设置部分字体颜色
     * @return
     */
    public SpannableString getForeColorSpan() {
        SpannableString spanableInfo = new SpannableString(this.getString(R.string.goldstore_branch_wenxininfo));
        spanableInfo.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.fonts_yellow)), 63, 66, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanableInfo.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.fonts_yellow)), 76, 79, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanableInfo.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.fonts_yellow)), 80, 83, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanableInfo.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.fonts_yellow)), 84, 87, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanableInfo.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.fonts_yellow)), 88, 92, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanableInfo;
    }

}
