package com.chinamworld.bocmbci.biz.preciousmetal.goldstoretransquery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.abstracttools.BaseHttpManager;
import com.chinamworld.bocmbci.abstracttools.BaseRUtil;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.CommonApplication;
import com.chinamworld.bocmbci.biz.preciousmetal.PreciousmetalDataCenter;
import com.chinamworld.bocmbci.biz.preciousmetal.goldstoremainpage.GoldstoreMainActivity;
import com.chinamworld.bocmbci.constant.BaseLocalData;
import com.chinamworld.bocmbci.http.IHttpResponseCallBack;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.ActivityIntentTools;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.llbt.userwidget.SlipMenu.SlipDrawerLayout;
import com.chinamworld.llbt.userwidget.dialogview.LoadingDialog;
import com.chinamworld.llbt.userwidget.dialogview.MessageDialog;
import com.chinamworld.llbt.userwidget.refreshliseview.IRefreshLayoutListener;
import com.chinamworld.llbt.userwidget.refreshliseview.RefreshDataStatus;
import com.chinamworld.llbt.userwidget.selectedview.FilterView;
import com.chinamworld.llbt.userwidget.selectedview.SelectTimeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 贵金属积存交易查询主界面
 * 因有筛选页面 侧面滑出 不再继承积存基类，直接继承BaseActivity
 * Created by linyl on 2016/8/24.
 */
public class TransQuryActivity extends BaseActivity
        implements View.OnClickListener,PinnedSectionListView.IXListViewListener {
    TextView tv_buy,tv_ransom,tv_branch;
    ImageView ima_buyline,ima_ransomline,ima_branchline;
    PullPinnedSectionListView listView;
    ArrayList<PinnedSectionBean> real_data;
    WarnDetailAdapter mAdapter;
    List<Messages> mData;
    List<Messages> mDataLoad = new ArrayList<Messages>();
    List<Map<String, Object>> sourceList = new ArrayList<Map<String, Object>>() ;
    LinearLayout ll_tran_null;
    String conversationId;
    String queryType = "1";
    /**接口上送字段 map**/
    Map<String,Object> requestParamsMap = new HashMap<String, Object>();
    /**开始日期、结束日期**/
    String startTime,endTime,systemTime;
    List<Map<String,Object>> querylist,querylistmore;
    String recordNumber = "0";
    SlipDrawerLayout mSlipDrawerLayout;
    SelectTimeView mSelectTimeView;
//    ImageView ima_back;
    TextView tv_back;
    TextView tv_choose,tv_choose_sel;
    TextView tv_tra_null;
    int currentIndex = 0 ;
    /**刷新状态标示**/
    boolean isRefreshFlag = false;
    String title_new = null;
    /**保存上次选择的起止日期**/
    String startTime_sel,endTime_sel;
    @Override
    public ActivityTaskType getActivityTaskType() {
        return ActivityTaskType.TwoTask;
    }

    GoldPullToRefreshLayout mPullToRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goldstore_traquery_main);
//        getPreviousmeatalBackgroundLayout().setTitleText("交易查询");
//        getPreviousmeatalBackgroundLayout().setMetalRightText("筛选");
//        getPreviousmeatalBackgroundLayout().setMetalRightImage(getResources().getDrawable(R.drawable.goldstore_choose_one));
        mSlipDrawerLayout = (SlipDrawerLayout)findViewById(R.id.drawerLayout);
        mPullToRefreshLayout = (GoldPullToRefreshLayout)findViewById(R.id.refreshLayout);
//        if(Integer.parseInt(recordNumber) > 10){
        mPullToRefreshLayout.setOnRefreshListener(new IRefreshLayoutListener(){
            @Override
            public void onLoadMore(View pullToRefreshLayout) {
                //TODO： 加载更多
                isRefreshFlag = true;
                currentIndex++;
                requestPsnGoldStoreTransQuery(currentIndex*10+"",queryType,false);
            }
        });
//        }


//        getPreviousmeatalBackgroundLayout().setMetalRightonClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSelectTimeView.resetDate(startTime,endTime);
//                mSlipDrawerLayout.toggle();
//
//            }
//        });

//        /**返回事件 跳转到首页**/
//        getPreviousmeatalBackgroundLayout().setMetalBackonClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                goGoldstoreMain();
//            }
//        });

        FilterView filterView = (FilterView)findViewById(R.id.filterView);
        filterView.setOnFilterViewClickListener(new FilterView.IFilterViewClickListener() {
            @Override
            public boolean onClick(View v, FilterView.ClickButtonType clickType) {
                if(clickType == FilterView.ClickButtonType.ok){
                    // 查询
//                    if(mSelectTimeView.checkDate() == false){
//                        return true;
//                    }
                    startTime = mSelectTimeView.getStartTimeDate();
                    endTime = mSelectTimeView.getEndTimeDate();
                    startTime_sel = mSelectTimeView.getStartTimeDate();
                    endTime_sel = mSelectTimeView.getEndTimeDate();
                    tv_choose.setVisibility(View.GONE);
                    tv_choose_sel.setVisibility(View.VISIBLE);
                    if(dateTimeSimpleCheck() == false){
                        return true;
                    }
                    requestPsnGoldStoreTransQuery("0",queryType,true);
                }
                else if(clickType == FilterView.ClickButtonType.reset){
                    startTime = QueryDateUtils.getlastOneMonth(TransQuryActivity.this.systemTime).trim();
                    endTime = QueryDateUtils.getcurrentDate(systemTime);
                    mSelectTimeView.resetDate(startTime,endTime);//重置为初始一个月日期
                }

                return false;
            }
        });

        mSelectTimeView = (SelectTimeView)findViewById(R.id.selectTimeView);

//        ima_back = (ImageView) findViewById(R.id.ima_back);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_choose = (TextView) findViewById(R.id.tv_tra_choose);
        tv_buy = (TextView) findViewById(R.id.textView16);
        tv_ransom = (TextView) findViewById(R.id.textView17);
        tv_branch = (TextView) findViewById(R.id.textView18);

        tv_choose_sel = (TextView) findViewById(R.id.tv_tra_choose_sel);

        ima_buyline = (ImageView) findViewById(R.id.ima_trabuyline);
        ima_ransomline = (ImageView) findViewById(R.id.ima_tanransomline);
        ima_branchline = (ImageView) findViewById(R.id.ima_trabranchline);
        listView = mPullToRefreshLayout.getPullPinnedSectionListView();
//        listView = (PullPinnedSectionListView) findViewById(R.id.list);
//        ((ViewGroup)findViewById(R.id.contentLayout)).removeAllViews();
//        mPullToRefreshLayout.setListLayout(listView);
//        PullToRefreshLayout footview = new PullToRefreshLayout(this);
//        listView.addFooterView(footview);

        ll_tran_null = (LinearLayout) findViewById(R.id.ll_querylist_null);
        tv_tra_null = (TextView) findViewById(R.id.tv_tra_null);
        //取进入交易查询页面的交易方式的值
        if("ransom".equals(this.getIntent().getStringExtra("queryType"))){//赎回成功跳交易查询  默认调赎回数据
            queryType = "2";
            tv_ransom.setTextColor(getResources().getColor(R.color.boc_text_color_red));
            tv_buy.setTextColor(getResources().getColor(R.color.boc_text_color_dark_gray));
            tv_branch.setTextColor(getResources().getColor(R.color.boc_text_color_dark_gray));
            ima_ransomline.setVisibility(View.VISIBLE);
            ima_buyline.setVisibility(View.GONE);
            ima_branchline.setVisibility(View.GONE);
//            requestPsnGoldStoreTransQuery("0",queryType,true);
        }else if("buy".equals(this.getIntent().getStringExtra("queryType"))){//购买成功跳交易查询
            queryType = "1";
//            requestPsnGoldStoreTransQuery("0",queryType,true);

        }

        tv_buy.setOnClickListener(this);
        tv_ransom.setOnClickListener(this);
        tv_branch.setOnClickListener(this);

        tv_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                goGoldstoreMain();
            }
        });

        tv_choose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(startTime_sel == null || endTime_sel == null){
                    mSelectTimeView.resetDate(startTime,endTime);
                }else{
                    mSelectTimeView.resetDate(startTime_sel,endTime_sel);
                }
                mSlipDrawerLayout.toggle();
            }
        });
        tv_choose_sel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(startTime_sel == null || endTime_sel == null){
                    mSelectTimeView.resetDate(startTime,endTime);
                }else{
                    mSelectTimeView.resetDate(startTime_sel,endTime_sel);
                }
                mSlipDrawerLayout.toggle();
            }
        });
        requestCommConversationId();
        LoadingDialog.showLoadingDialog(this);

    }

    @Override
    public void requestCommConversationIdCallBack(Object resultObj) {
        super.requestCommConversationIdCallBack(resultObj);
        conversationId = (String) CommonApplication.getInstance().getBizDataMap().get("conversationId");
        requestSystemDateTime();
    }

    @Override
    public void requestSystemDateTimeCallBack(Object resultObj) {
        super.requestSystemDateTimeCallBack(resultObj);
        BiiHttpEngine.dissMissProgressDialog();
        systemTime = dateTime;
        startTime = QueryDateUtils.getlastOneMonth(this.systemTime).trim();
        endTime = QueryDateUtils.getcurrentDate(systemTime);
        mSelectTimeView.setStartTime(startTime);
        mSelectTimeView.setEndTime(endTime);
        requestPsnGoldStoreTransQuery("0",queryType,true);
    }

    /**
     * 贵金属积存交易结果查询
     * @param currentIndex
     * @param type
     * @param _refresh
     */
    public void requestPsnGoldStoreTransQuery( final String currentIndex,String type,final boolean _refresh){
        requestParamsMap.put("startDate",startTime);
        requestParamsMap.put("endDate",endTime);
        requestParamsMap.put("queryType",type);
        requestParamsMap.put("currentIndex",currentIndex);
        requestParamsMap.put("pageSize","10");
        requestParamsMap.put("_refresh",_refresh ? "true":"false");
        this.getHttpTools().requestHttpWithConversationId("PsnGoldStoreTransQuery", requestParamsMap, conversationId,
                new IHttpResponseCallBack<Map<String,Object>>() {
                    @Override
                    public void httpResponseSuccess(Map<String,Object> result, String s) {
                        LoadingDialog.closeDialog();
                        BaseHttpEngine.dissMissProgressDialog();
                        if(_refresh) {
                            if (StringUtil.isNullOrEmpty(result)) {
                                mPullToRefreshLayout.setVisibility(View.GONE);
                                ll_tran_null.setVisibility(View.VISIBLE);
                                if (QueryDateUtils.getlastOneMonth(TransQuryActivity.this.systemTime).trim().equals(startTime) &&
                                        QueryDateUtils.getcurrentDate(systemTime).equals(endTime)) {
                                    tv_tra_null.setText("您最近一月内未有交易记录，请点击筛选进行查询");
                                } else {
                                    tv_tra_null.setText("没有查到符合条件的记录");
                                }
                                return;
                            }
                            sourceList = (List<Map<String, Object>>) result.get("List");
                            if (StringUtil.isNullOrEmpty(sourceList)) {
                                mPullToRefreshLayout.setVisibility(View.GONE);
                                ll_tran_null.setVisibility(View.VISIBLE);
                                if (QueryDateUtils.getlastOneMonth(TransQuryActivity.this.systemTime).trim().equals(startTime) &&
                                        QueryDateUtils.getcurrentDate(systemTime).equals(endTime)) {
                                    tv_tra_null.setText("您最近一月内未有交易记录，请点击筛选进行查询");
                                } else {
                                    tv_tra_null.setText("没有查到符合条件的记录");
                                }
                                return;
                            }
                            recordNumber = (String) result.get("recordNumber");
                            mPullToRefreshLayout.setVisibility(View.VISIBLE);
                            ll_tran_null.setVisibility(View.GONE);

                            mData = transferData(sourceList);
//                            real_data = new ArrayList<PinnedSectionBean>();
//                            real_data = PinnedSectionBean.getData(mData);
//                            listView.setXListViewListener(TransQuryActivity.this);
//                            mAdapter = new WarnDetailAdapter(real_data, TransQuryActivity.this);
//                            listView.setAdapter(mAdapter);

                            real_data = new ArrayList<PinnedSectionBean>();
                            real_data = PinnedSectionBean.getData(mData);

                            listView.setXListViewListener(TransQuryActivity.this);

                            mAdapter = new WarnDetailAdapter(real_data, TransQuryActivity.this);
                            listView.setAdapter(mAdapter);
                        }else {
//                            if(StringUtil.isNullOrEmpty(result) || StringUtil.isNullOrEmpty((List<Map<String, Object>>) result.get("List"))){
//                                mPullToRefreshLayout.loadmoreCompleted(GoldPullToRefreshLayout.MoreLoadStatus.NoMoreData);
//                            }else{
//                                querylistmore = (List<Map<String, Object>>) result.get("List");
//                                mData = transferData(addSourceList(querylistmore));
//                                mPullToRefreshLayout.loadmoreCompleted(GoldPullToRefreshLayout.MoreLoadStatus.Successed);
//                            }
                            if(Integer.parseInt(currentIndex) > Integer.parseInt(recordNumber)){
                                mPullToRefreshLayout.loadmoreCompleted(RefreshDataStatus.NoMoreData);
                            }else{
                                title_new = null;
                                querylistmore = (List<Map<String, Object>>) result.get("List");
                                // 后台返回数据未经过排序时 每次进行数据重组list
//                                mData = transferData(addSourceList(querylistmore));
                                //后台返回数据有排序时 直接将数据转化为Messages后进行添加到mData
//                                if(!StringUtil.isNullOrEmpty(querylistmore)) {
//                                    List<Messages> ListMoreData = transferData(querylistmore);
//                                    if(!StringUtil.isNullOrEmpty(mData)) {
//                                        for (int i = 0; i < querylistmore.size(); i++) {
//                                            mData.add(ListMoreData.get(i));
//                                       }
//                                     }
//                                }
                                mAdapter.addSourceList(PinnedSectionBean.getData(transferData(querylistmore)));

                                for(int i = 0;i<mAdapter.getList().size();i++) {
                                    if (PinnedSectionBean.SECTION == mAdapter.getList().get(i).getType()) {

                                        if (!mAdapter.getList().get(i).getMessages().getTitle().equals(title_new)) {
                                            title_new = mAdapter.getList().get(i).getMessages().getTitle();
                                        } else {
//                                            mAdapter.getList().get(i).getMessages().setTitle(null);
                                            mAdapter.getList().remove(i);
                                        }
                                    }
                                }
                                mAdapter.notifyDataSetChanged();

                                mPullToRefreshLayout.loadmoreCompleted(RefreshDataStatus.Successed);
                            }
                        }
//                        real_data = new ArrayList<PinnedSectionBean>();
//                        real_data = PinnedSectionBean.getData(mData);
//
//                        listView.setXListViewListener(TransQuryActivity.this);
//
//                        mAdapter = new WarnDetailAdapter(real_data, TransQuryActivity.this);
//                        listView.setAdapter(mAdapter);

                        /**跳转至详情页面**/
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int positiom, long l) {
                                //数据类型转换 Map
                                //如果是点击title  不让跳转  return
                                if(PinnedSectionBean.SECTION == mAdapter.getItem(positiom).getType()) return;
                                PreciousmetalDataCenter.getInstance().TransQueryDetailMap = (Map<String,Object>) mAdapter.getItem(positiom).getMessages().getItemMap();
                                ActivityIntentTools.intentToActivity(TransQuryActivity.this,TransQueryDetailActivity.class);
                            }
                        });
                    }
                });
    }

    private List<Messages> transferData(List<Map<String, Object>> list){
        //数据类型转换。将list数据转换为mData数据类型
        mData = new ArrayList<Messages>();
        Messages tmp = null;
        for(int i = 0;i < list.size();i++){
            tmp = new Messages();
            //年月
            tmp.setTitle(TimeManagement.getYearAndMonth((String)list.get(i).get("transDate")));
            //几号
            tmp.setDate(TimeManagement.getDay((String)list.get(i).get("transDate")));
            //星期
            String week = null;
            try{
                week = TimeManagement.getWeekOfDate((String)list.get(i).get("transDate"));
            } catch (Exception e){

            }
            tmp.setWeek(week);
            //列表项内容
            if("1".equals(queryType)){//购买
                tmp.setWeightNum(StringUtil.parseStringPattern((String)list.get(i).get("transWeight"),4) + "克");
                tmp.setAddr_price("买入价格"+StringUtil.parseStringPattern((String)list.get(i).get("price"),2) + "元/克");
                tmp.setAmount(StringUtil.parseStringPattern((String)list.get(i).get("transAmount"),2)+"元");
                tmp.setType((String)list.get(i).get("currCodeName"));
            }else if("2".equals(queryType)){//赎回
                tmp.setWeightNum(StringUtil.parseStringPattern((String)list.get(i).get("transWeight"),4) + "克");
                tmp.setAddr_price("赎回价格"+StringUtil.parseStringPattern((String)list.get(i).get("price"),2) + "元/克");
                tmp.setAmount(StringUtil.parseStringPattern(String.valueOf(Double.parseDouble((String)list.get(i).get("transWeight"))*Double.parseDouble((String)list.get(i).get("price"))),2)+"元");
                tmp.setType((String)list.get(i).get("currCodeName"));
            }else{//提货
                tmp.setWeightNum(StringUtil.parseStringPattern((String)list.get(i).get("transWeight"),4) + "克");
                tmp.setAddr_price((String)list.get(i).get("pickupSite"));
                tmp.setType((String)list.get(i).get("currCodeName"));
            }
            tmp.setItemMap(list.get(i));
            mData.add(tmp);
        }

        return mData;
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.textView16://购买
                queryType = "1";
                tv_buy.setTextColor(getResources().getColor(R.color.boc_text_color_red));
                tv_ransom.setTextColor(getResources().getColor(R.color.boc_text_color_dark_gray));
                tv_branch.setTextColor(getResources().getColor(R.color.boc_text_color_dark_gray));
                ima_buyline.setVisibility(View.VISIBLE);
                ima_ransomline.setVisibility(View.GONE);
                ima_branchline.setVisibility(View.GONE);
                tv_choose.setVisibility(View.VISIBLE);
                tv_choose_sel.setVisibility(View.GONE);
//                listView.setVisibility(View.VISIBLE);
//                ll_tran_null.setVisibility(View.GONE);
//                initData();
                /**点击table项均按默认一个月时间查询**/
                startTime = QueryDateUtils.getlastOneMonth(this.systemTime).trim();
                endTime = QueryDateUtils.getcurrentDate(systemTime);
                currentIndex = 0;
                requestPsnGoldStoreTransQuery("0",queryType,true);
                break;
            case R.id.textView17://赎回
                queryType = "2";
                tv_ransom.setTextColor(getResources().getColor(R.color.boc_text_color_red));
                tv_buy.setTextColor(getResources().getColor(R.color.boc_text_color_dark_gray));
                tv_branch.setTextColor(getResources().getColor(R.color.boc_text_color_dark_gray));
                ima_ransomline.setVisibility(View.VISIBLE);
                ima_buyline.setVisibility(View.GONE);
                ima_branchline.setVisibility(View.GONE);
                tv_choose.setVisibility(View.VISIBLE);
                tv_choose_sel.setVisibility(View.GONE);
//                listView.setVisibility(View.GONE);
//                ll_tran_null.setVisibility(View.VISIBLE);
                startTime = QueryDateUtils.getlastOneMonth(this.systemTime).trim();
                endTime = QueryDateUtils.getcurrentDate(systemTime);
                currentIndex = 0;
                requestPsnGoldStoreTransQuery("0",queryType,true);
                break;
            case R.id.textView18://提货
                queryType = "3";
                tv_branch.setTextColor(getResources().getColor(R.color.boc_text_color_red));
                tv_ransom.setTextColor(getResources().getColor(R.color.boc_text_color_dark_gray));
                tv_buy.setTextColor(getResources().getColor(R.color.boc_text_color_dark_gray));
                ima_branchline.setVisibility(View.VISIBLE);
                ima_ransomline.setVisibility(View.GONE);
                ima_buyline.setVisibility(View.GONE);
                tv_choose.setVisibility(View.VISIBLE);
                tv_choose_sel.setVisibility(View.GONE);
//                listView.setVisibility(View.GONE);
//                ll_tran_null.setVisibility(View.VISIBLE);
                startTime = QueryDateUtils.getlastOneMonth(this.systemTime).trim();
                endTime = QueryDateUtils.getcurrentDate(systemTime);
                currentIndex = 0;
                requestPsnGoldStoreTransQuery("0",queryType,true);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        goGoldstoreMain();
    }

    /**
     * 返回贵金属积存 首页的方法
     **/
    private void goGoldstoreMain() {
        ActivityTaskManager.getInstance().removeAllSecondActivity();
        Intent intent = new Intent(this, GoldstoreMainActivity.class);
        startActivity(intent);
        finish();
    }

    public List<Map<String,Object>> addSourceList(List<Map<String,Object>> list) {
        if(list != null && list.size() != 0) {
            if(this.sourceList == null) {
                this.sourceList = list;
            } else {
                Iterator var3 = list.iterator();

                while(var3.hasNext()) {
                    Map<String,Object> item = (Map<String,Object>)var3.next();
                    this.sourceList.add(item);
                }
            }
        }
        return sourceList;
    }

    /**
     * 下拉加载更多时 捕获网络异常处理事件
     * @param requestMethod
     */
    @Override
    public void commonHttpErrorCallBack(final String requestMethod) {
//        super.commonHttpErrorCallBack(requestMethod);
        if(!"Logout".equals(requestMethod)) {
            if(!"PNS001".equals(requestMethod)) {
                String message = this.getResources().getString(BaseRUtil.Instance.getID("R.string.communication_fail"));
                LogGloble.e("TransQuryActivity", "请求失败的接口名称" + requestMethod);
                MessageDialog.showMessageDialog(TransQuryActivity.this,message,new View.OnClickListener() {
                    public void onClick(View v) {
                        MessageDialog.closeDialog();
                        if(BaseHttpManager.Instance.getcanGoBack() || BaseLocalData.queryCardMethod.contains(requestMethod)) {
                            TransQuryActivity.this.finish();
                            BaseHttpManager.Instance.setCanGoBack(false);
                        }
                    }
                });
            }
        }
        if("PsnGoldStoreTransQuery".equals(requestMethod) && isRefreshFlag){//刷新状态
            mPullToRefreshLayout.loadmoreCompleted(RefreshDataStatus.Failed);
        }
    }

    /**筛选日期校验**/
    private boolean dateTimeSimpleCheck(){
        /**允许1年内数据   可查1个月**/
        return commQueryStartAndEndDateReg(TransQuryActivity.this, startTime, endTime, systemTime, 1, 1);
    }

    public boolean commQueryStartAndEndDateReg(Context context, String startDate, String endDate, String sysTime, int maxYears, int maxMonths) {
        HashMap messageInfo = new HashMap();
        messageInfo.put("1", "一");
        messageInfo.put("2", "两");
        messageInfo.put("3", "三");
        messageInfo.put("6", "六");
        if(!QueryDateUtils.compareDateMaxYears(startDate, sysTime, maxYears)) {
            MessageDialog.showMessageDialog(context,"起始日期需要在当前日期一年以内");
            return false;
        } else if(!QueryDateUtils.compareDate(endDate, sysTime)) {
            MessageDialog.showMessageDialog(context,"结束日期需要早于当前日期");
            return false;
        } else if(!QueryDateUtils.compareDate(startDate, endDate)) {
            MessageDialog.showMessageDialog(context,"结束日期不应早于起始日期");
            return false;
        } else if(!QueryDateUtils.compareDateMaxMonths(startDate, endDate, maxMonths)) {
            MessageDialog.showMessageDialog(context,"查询时间范围最大为" + (String)messageInfo.get(String.valueOf(maxMonths)) + "个月");
            return false;
        } else {
            return true;
        }
    }

}
