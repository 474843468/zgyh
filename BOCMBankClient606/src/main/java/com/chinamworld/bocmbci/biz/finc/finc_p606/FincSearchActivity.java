package com.chinamworld.bocmbci.biz.finc.finc_p606;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincIntentNew;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.boc.ModelBoc;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.interfacemodule.IActionTwo;
import com.chinamworld.bocmbci.utils.ActivityIntentTools;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.adapter.CommonAdapter;
import com.chinamworld.bocmbci.widget.adapter.ICommonAdapter;
import com.chinamworld.llbt.userwidget.dialogview.MessageDialog;
import com.chinamworld.llbt.userwidget.refreshliseview.IRefreshLayoutListener;
import com.chinamworld.llbt.userwidget.refreshliseview.RefreshDataStatus;
import com.chinamworld.llbt.userwidget.refreshliseview.RefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/20 0020.
 * 基金搜索页面
 */
public class FincSearchActivity extends FincBaseActivity implements View.OnClickListener{
    public static final String FUND_EXTRA_KEY_KEYWORD = "fund_extra_key_keyword";
    public static final String FUND_KEY_SEARCH_HISTORY_KEYWORD = "fund_key_search_history_keyword";
    private EditText mKeywordEt;//输入框
    private TextView mOperationTv;//搜索
    private ArrayAdapter<String> mArrAdapter;//历史记录
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;
    private LinearLayout mSearchHistoryLl;//历史记录
    private LinearLayout searchLayout;//搜索页面
    private List<String> mHistoryKeywords;
    private ListView finc_search_history_listview;
    private ListView  fastListView;//查询结果
    /** 是否登录 */
    private boolean isLogin;
    /** 起始索引 */
    private int fastCurrentIndex;
    private List<Map<String, Object>> fastList = new ArrayList<Map<String, Object>>();
    /**适配器**/
    private CommonAdapter<Map<String, Object>> fastAdapter;
    private RefreshLayout fast_pull_refresh;
    /**刷新状态标示**/
    boolean isRefreshFlag = false;
    /** 总记录条数--默认0 */
    private int recordNumber = 0;
    /** 是否刷新 */
    private boolean isRefresh = false;
    String fastFundCodeOrName;//基金代码或名称
    private LinearLayout finc_search_list_layout;//快速查询列表布局
    private LinearLayout recommend_search_layout;//热门搜索
    private ArrayAdapter<String> recommendArrAdapter;//热门搜索
    private List<String> recommendKeywords;//
    private ListView finc_search_recommend_listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isLogin = BaseDroidApp.getInstanse().isLogin();
        mPref =  getSharedPreferences("fund", Activity.MODE_PRIVATE);
        mEditor = mPref.edit();
        mHistoryKeywords = new ArrayList<String>();
        setContentView(R.layout.finc_search_layout,false);
        mKeywordEt = (EditText) findViewById(R.id.finc_editText);
        mKeywordEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    return true;
                }
                return false;
            }
        });
        mKeywordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    if (mHistoryKeywords.size() > 0) {
                        mSearchHistoryLl.setVisibility(View.VISIBLE);
                    } else {
                        mSearchHistoryLl.setVisibility(View.GONE);
                    }
                } else {
                    mSearchHistoryLl.setVisibility(View.GONE);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mKeywordEt.requestFocus();
        init();
    }

    private void init(){
        isRefresh=true;
        findViewById(R.id.finc_search_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        finc_search_list_layout = (LinearLayout) findViewById(R.id.finc_search_list_layout);
        fastListView = (ListView) findViewById(R.id.fast_query_list);
        fastListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("fromOtherFundCode", (String)fastList.get(i).get("fundCode"));
                map.put("isFromOther", "2");
                ActivityIntentTools.intentToActivityForResult(FincSearchActivity.this,FincProductDetailActivity.class,1001,map);
               // FincIntentNew.getIntent().fincIntent(FincSearchActivity.this, (String)fastList.get(i).get("fundCode"));
//                FincControl.getInstance().fundDetails = fastList.get(i);
//                ActivityIntentTools.intentToActivity(FincSearchActivity.this,FincProductDetailActivity.class);
            }
        });
        mOperationTv = (TextView) findViewById(R.id.finc_search_top_right_btn);
        mSearchHistoryLl = (LinearLayout) findViewById(R.id.finc_history_search_layout);
        searchLayout = (LinearLayout) findViewById(R.id.finc_search_layout);
        mOperationTv.setOnClickListener(this);
        findViewById(R.id.finc_search_clear).setOnClickListener(this);
        String history = mPref.getString(FUND_KEY_SEARCH_HISTORY_KEYWORD,"");
        finc_search_history_listview = (ListView) findViewById(R.id.finc_search_history_listview);
        if (!TextUtils.isEmpty(history)){
            List<String> list = new ArrayList<String>();
            for(Object o : history.split(",")) {
                list.add((String)o);
            }
            mHistoryKeywords = list;
        }
        if (mHistoryKeywords.size() > 0) {
            mSearchHistoryLl.setVisibility(View.VISIBLE);
        } else {
            mSearchHistoryLl.setVisibility(View.GONE);
        }
        mArrAdapter = new ArrayAdapter<String>(this, R.layout.finc_serach_item, mHistoryKeywords);
        finc_search_history_listview.setAdapter(mArrAdapter);
        finc_search_history_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mKeywordEt.setText(mHistoryKeywords.get(i));
                mKeywordEt.setSelection(mHistoryKeywords.get(i).length());
               // mSearchHistoryLl.setVisibility(View.GONE);
                searchLayout.setVisibility(View.GONE);
            }
        });
        mArrAdapter.notifyDataSetChanged();
        fast_pull_refresh = (RefreshLayout) findViewById(R.id.fast_pull_refresh);
        fast_pull_refresh.setOnRefreshListener(new IRefreshLayoutListener(){
            @Override
            public void onLoadMore(View pullToRefreshLayout) {
                //TODO： 加载更多
                isRefreshFlag = true;
                fastCurrentIndex+= 20;
                if (isLogin) {
                    fastQuery(fastFundCodeOrName, fastCurrentIndex,"","","");
                } else {
                    fastQueryOutlay(fastFundCodeOrName, fastCurrentIndex,"","","");
                }
            }
        });
        recommend_search_layout = (LinearLayout) findViewById(R.id.recommend_search_layout);
        finc_search_recommend_listview = (ListView) findViewById(R.id.finc_search_recommend_listview);
        ModelBoc.getFundProductList(new IActionTwo() {
            @Override
            public void callBack(Object param1, Object param2) {
            if((Boolean)param2==true&&!StringUtil.isNullOrEmpty(param1)){
                recommend_search_layout.setVisibility(View.VISIBLE);
                List<Map<String,Object>> list = (List<Map<String, Object>>) param1;
                recommendKeywords = new ArrayList<String>();
                for(Map map : list){
                    recommendKeywords.add((String)map.get("productName"));
                }
                recommendArrAdapter = new ArrayAdapter<String>(FincSearchActivity.this, R.layout.finc_serach_item, recommendKeywords);
                finc_search_recommend_listview.setAdapter(recommendArrAdapter);
            }
            }
        });
        finc_search_recommend_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mKeywordEt.setText(recommendKeywords.get(i));
                mKeywordEt.setSelection(recommendKeywords.get(i).length());
                searchLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void commonHttpErrorCallBack(String requestMethod) {
        super.commonHttpErrorCallBack(requestMethod);
        if(isLogin){
            if("PsnQueryFundDetail".equals(requestMethod) && isRefreshFlag){//刷新状态
                fast_pull_refresh.loadmoreCompleted(RefreshDataStatus.Failed);
            }
        }else{
            if("PsnFundQueryOutlay".equals(requestMethod) && isRefreshFlag){//刷新状态
                fast_pull_refresh.loadmoreCompleted(RefreshDataStatus.Failed);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.finc_search_clear:
                mEditor.clear();
                mEditor.commit();
                mHistoryKeywords.clear();
                mArrAdapter.notifyDataSetChanged();
                mSearchHistoryLl.setVisibility(View.GONE);
                break;
            case R.id.finc_search_top_right_btn:
                fastCurrentIndex = 0;
                isRefresh=true;
                fastList.clear();
                save();
                break;
            default:
                break;
        }
    }
    public void save() {
        fastFundCodeOrName = StringUtil.trim(mKeywordEt.getText().toString());
        if (StringUtil.isNull(fastFundCodeOrName)) {
            //BaseDroidApp.getInstanse().showInfoMessageDialog("请输入基金代码或名称");
            MessageDialog.showMessageDialog(this,"请输入基金代码或名称");
            return;
        }
        if (!setFincinputRegexpBean(fastFundCodeOrName, "")) {
            return;
        }
        if (StringUtil.isNullOrEmpty(fastFundCodeOrName)) {
            MessageDialog.showMessageDialog(this,getString(R.string.forex_no_fundcode_error));
//            BaseDroidApp.getInstanse().showInfoMessageDialog(
//                    getString(R.string.forex_no_fundcode_error));
            return;
        }
        String oldText = mPref.getString(FUND_KEY_SEARCH_HISTORY_KEYWORD,"");
        if (!TextUtils.isEmpty(fastFundCodeOrName) /*&& !oldText.contains(fastFundCodeOrName)*/) {
            boolean isAdd = true;
            for(int i = 0;i<mHistoryKeywords.size();i++){
                if(fastFundCodeOrName.equals(mHistoryKeywords.get(i))){
                    isAdd = false;
                }
            }
            if(isAdd) {
                if(mHistoryKeywords.size()>4){//大于5条时移除第一条
                    mHistoryKeywords.remove(4);
                }
                mHistoryKeywords.add(0,fastFundCodeOrName);
                String tmp=mHistoryKeywords.get(0);
                for(int i = 1; i<  mHistoryKeywords.size();i++){
                    tmp += "," + mHistoryKeywords.get(i);
                }
                mEditor.putString(FUND_KEY_SEARCH_HISTORY_KEYWORD,tmp);
                mEditor.commit();

            }
        }
        mArrAdapter.notifyDataSetChanged();
        BaseHttpEngine.showProgressDialog();
        /**快速查询，基金状态上送全部*/
        if (isLogin) {
            fastQuery(fastFundCodeOrName, fastCurrentIndex,"","","");
        } else {
            fastQueryOutlay(fastFundCodeOrName, fastCurrentIndex,"","","");
        }
        searchLayout.setVisibility(View.GONE);
        finc_search_list_layout.setVisibility(View.VISIBLE);
        fastListView.setVisibility(View.VISIBLE);
    }

    private void fastQueryOutlay(String fundInfo,Integer currentIndex,String fundState,
                                 String sortFlag ,String sortField) {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Finc.PSNQUERYFUNDDETAIL_OUTLAY);
        Map<String, String> params = new HashMap<String, String>();
        params.put(Finc.PSNQUERYFUNDDETAIL_FUNDINFO, fundInfo);
        params.put(Finc.COMBINQUERY_CURRENTINDEX, String.valueOf(currentIndex));
        params.put(Finc.COMBINQUERY_PAGESIZE, String.valueOf(20));
        params.put(Finc.PSNQUERYFUNDDETAIL_FUNDTYPE, "00");
        params.put(Finc.PSNQUERYFUNDDETAIL_FUNDKIND, "00");
        params.put(Finc.PSNQUERYFUNDDETAIL_FUNDSTATE, fundState);
        params.put(Finc.PSNQUERYFUNDDETAIL_SORTFLAG, sortFlag);
        params.put(Finc.PSNQUERYFUNDDETAIL_SORTFIELD, sortField);
        biiRequestBody.setParams(params);
        HttpManager.requestOutlayBii(biiRequestBody, this, "fastQueryCallback");
    }

    public void fastQueryCallback(Object resultObj) {
        BaseHttpEngine.dissMissProgressDialog();
        super.fastQueryCallback(resultObj);
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
                .getResult();
        List<Map<String, Object>> resultList = (List<Map<String, Object>>) resultMap
                .get(Finc.COMBINQUERY_LIST);
        if(StringUtil.isNull(((String) resultMap
                .get(Finc.COMBINQUERY_RECORDNUMBER))) == false){
            recordNumber = Integer.valueOf((String) resultMap.get(Finc.COMBINQUERY_RECORDNUMBER));
        }
        if (StringUtil.isNullOrEmpty(resultList)&&StringUtil.isNullOrEmpty(fastList)) {
            MessageDialog.showMessageDialog(this,getString(R.string.finc_query_noresult_error));
            searchLayout.setVisibility(View.VISIBLE);
            fastListView.setVisibility(View.GONE);
            finc_search_list_layout.setVisibility(View.GONE);
            if(fastAdapter != null){
                fastAdapter.notifyDataSetChanged();
            }
            return;
        } else {
            if(isRefresh){
                isRefresh=false;
                fastList.clear();
            }
            fastList.addAll(resultList);
            if(fastAdapter == null){
                fastAdapter = new CommonAdapter<Map<String, Object>>(this,fastList,fastListAdapter);
                fastListView.setAdapter(fastAdapter);
            }else{
                fastAdapter.notifyDataSetChanged();
                if(fastCurrentIndex > recordNumber){
                    fast_pull_refresh.loadmoreCompleted(RefreshDataStatus.NoMoreData);
                }else{
                    fast_pull_refresh.loadmoreCompleted(RefreshDataStatus.Successed);
                }
            }
        }
    }
    // 用于验证用户输入基金代码或基金名称。 基金代码或基金名称由数字、字母、汉字组成，且不能超过50个字符
    // <rule type="fundInput" pattern="^[a-zA-Z0-9\u4e00-\u9fa5]{0,50}$"
    // tip="基金代码或基金名称由数字、字母、汉字组成，且不能超过50个字符" />

    private boolean setFincinputRegexpBean(String repayAmount, String massage) {
        RegexpBean reb1 = new RegexpBean(massage, repayAmount,
                "fincinputcheckout");
        ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
        lists.add(reb1);
        if (RegexpUtils.regexpDate(lists)) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        String keyword = intent.getStringExtra(FUND_EXTRA_KEY_KEYWORD);
        if (!TextUtils.isEmpty(keyword)) {
            mKeywordEt.setText(keyword);
        }
    }

    /**
     * 基金行情查询 401 快速查询
     *
     * @param fundInfo
     *            基金代码或名称
     */
    public void fastQuery(String fundInfo,Integer currentIndex,String fundState,
                          String sortFlag ,String sortField) {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Finc.PSNQUERYFUNDDETAIL);
        Map<String, String> params = new HashMap<String, String>();
        params.put(Finc.PSNQUERYFUNDDETAIL_FUNDINFO, fundInfo);
        params.put(Finc.COMBINQUERY_CURRENTINDEX, String.valueOf(currentIndex));
        params.put(Finc.COMBINQUERY_PAGESIZE, String.valueOf(20));
        params.put(Finc.PSNQUERYFUNDDETAIL_FUNDTYPE, "00");
        params.put(Finc.PSNQUERYFUNDDETAIL_FUNDKIND, "00");
        params.put(Finc.PSNQUERYFUNDDETAIL_FUNDSTATE, fundState);
        params.put(Finc.PSNQUERYFUNDDETAIL_SORTFLAG, sortFlag);
        params.put(Finc.PSNQUERYFUNDDETAIL_SORTFIELD, sortField);
        biiRequestBody.setParams(params);
        HttpManager.requestBii(biiRequestBody, this, "fastQueryCallback");
    }

    private ICommonAdapter<Map<String,Object>> fastListAdapter = new ICommonAdapter<Map<String,Object>>(){

        @Override
        public View getView(int i, Map<String, Object> currentItem, LayoutInflater layoutInflater, View convertView, ViewGroup viewGroup) {
            ViewHold viewHold;
            if (convertView==null) {
                viewHold=new ViewHold();
                convertView=layoutInflater.inflate(R.layout.finc_search_result_item, null);
                viewHold.tv_fund_name_and_code = (TextView) convertView.findViewById(R.id.finc_name);
                viewHold.finc_code = (TextView) convertView.findViewById(R.id.finc_code);
                convertView.setTag(viewHold);
            }else {
                viewHold=(ViewHold) convertView.getTag();
            }
            // 基金名称和代码
            viewHold.tv_fund_name_and_code.setText((String) currentItem.get(Finc.FINC_FUNDNAME));
            viewHold.finc_code.setText((String) currentItem.get(Finc.FINC_FUNDCODE));
            return convertView;
        }
    };
    static class ViewHold{

        TextView tv_fund_name_and_code,finc_code/*,type_tv,start_amount_tv,item_tv1,item_tv2,item_tv3,item_tv4,item_tv5,item_tv6*/;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                finish();
                break;
        }
    }
}
