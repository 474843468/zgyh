package com.chinamworld.bocmbci.biz.finc.finc_p606;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.base.application.CommonApplication;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.accmanager.FincAccManagerMenuActivity;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.biz.finc.control.entity.FundCompany;
import com.chinamworld.bocmbci.biz.finc.finc_p606.selectview.Content;
import com.chinamworld.bocmbci.biz.finc.finc_p606.selectview.SelectTypeData;
import com.chinamworld.bocmbci.biz.finc.finc_p606.selectview.SelectTypeView;
import com.chinamworld.bocmbci.biz.finc.finc_p606.util.FincUtil;
import com.chinamworld.bocmbci.biz.finc.finc_p606.util.PullableFrameLayout;
import com.chinamworld.bocmbci.biz.finc.finc_p606.util.SyncHorizontalScrollView;
import com.chinamworld.bocmbci.biz.finc.fundprice.FundFundCompanyActivity;
import com.chinamworld.bocmbci.biz.finc.myfund.MyFincBalanceActivity;
import com.chinamworld.bocmbci.biz.finc.orcm.OrcmProductListActivity;
import com.chinamworld.bocmbci.biz.finc.query.FundQueryEffectiveActivity;
import com.chinamworld.bocmbci.biz.finc.query.FundqueryMenuActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.ErrorCode;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.queryMultipleFund.QueryMultipleFundRequestParams;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.queryMultipleFund.QueryMultipleFundResponseData;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.queryMultipleFund.QueryMultipleFundResult;
import com.chinamworld.bocmbci.net.GsonTools;
import com.chinamworld.bocmbci.net.HttpHelp;
import com.chinamworld.bocmbci.net.model.BaseResponseData;
import com.chinamworld.bocmbci.net.model.IHttpErrorCallBack;
import com.chinamworld.bocmbci.net.model.IHttpResponseCallBack;
import com.chinamworld.bocmbci.net.model.IOkHttpErrorCode;
import com.chinamworld.bocmbci.userwidget.investview.InvestPriceView;
import com.chinamworld.bocmbci.utils.ActivityIntentTools;
import com.chinamworld.bocmbci.utils.ChineseCharToEn;
import com.chinamworld.bocmbci.utils.ListUtils;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.adapter.CommonAdapter;
import com.chinamworld.bocmbci.widget.adapter.ICommonAdapter;
import com.chinamworld.llbt.userwidget.SlipMenu.SlipDrawerLayout;
import com.chinamworld.llbt.userwidget.dialogview.MessageDialog;
import com.chinamworld.llbt.userwidget.refreshliseview.IRefreshLayoutListener;
import com.chinamworld.llbt.userwidget.refreshliseview.RefreshDataStatus;
import com.chinamworld.llbt.userwidget.refreshliseview.RefreshLayout;
import com.chinamworld.llbt.userwidget.scrollview.NewScrollView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/21 0021.
 * 基金行情页面
 */
public class FundPricesActivity extends FincBaseActivity {
    SlipDrawerLayout mSlipDrawerLayout;
    private ImageView finc_more_item_img,finc_more_item_img_selected;//更多箭头
    private LinearLayout finc_list_layout,finc_item_second_layout,finc_item_three_layout;//类型选项布局
    private LinearLayout my_select_layout;
    /**  选中项*/
    String selectType = "全部";
    //列表左侧布局
    private ListView leftListView;
    //列表右侧布局
    private ListView rightListView;
    private SyncHorizontalScrollView titleHorsv;//水平滚动的标题
    private SyncHorizontalScrollView contentHorsv;
    private SelectTypeView stvType;
    /** 是否登录 */
    private boolean isLogin = false;
    /** 基金公司名称集合 */
    private ArrayList<FundCompany> companyList= new ArrayList<FundCompany>() ;
    /** 每页显示记录条数--默认10 */
    private int pageSize = 10;
    /** 起始索引 */
    private int currentIndex;
    /** 币种--默认上送人民币 001 */
    private String currencyCode = "001";
    /** 风险等级--默认上送"" */
    private String riskLevelStr = "";
    /** 产品种类--默认上送"00" */
    private String fundKindType = "00";
    /** 产品类型--默认上送"00" */
    private String fundType = "00";
    /** 基金公司代码--默认上送"" */
    private String fundCompanyCode = "";
    /**基金状态 默认正常开放0*/
    private String fundState = "0";
    /** 当前排序字段 1:单位净值；3:日净值增长率 4:每万份收益 5:七日年化收益率*/
    private String current_sortfield;
    /** 当前排序方式 1:正序；2:逆序 */
    private String currSortFlag ;
    private String tenThousand_sortFlag="",weekly_profit_sortFlag="";//排序标识：每万份收益,七日年化收益率
    /** 查询结果 */
    private List<Map<String, Object>> combinateList = new ArrayList<Map<String, Object>>();
    /** 合并BII和四方的数据 */
    private List<Map<String, Object>> biiFundList = new ArrayList<Map<String, Object>>();
    /**适配器**/
    private CommonAdapter<Map<String, Object>> leftAdapter;
    private CommonAdapter<Map<String, Object>> rightAdapter;
    private CommonAdapter<Map<String, Object>> currencyListAdapter;
    /** 总记录条数--默认0 */
    private int recordNumber = 0;
    /** 是否刷新 */
    private boolean isRefresh = false;
    /** 基金公司 */
    private TextView fundCompanySpinner;
    private int fundCompanyPos = -1;
    private InvestPriceView finc_head;
    //登陆按钮
    private View loginbtn;
    private PullableFrameLayout pullableFrameLayout;
    private RefreshLayout refreshLayout;//上拉加载
    /**刷新状态标示**/
    boolean isRefreshFlag = false;
    //货币型列表
    private ListView currency_type_listview;
    private RefreshLayout currency_pull_refresh;
    private LinearLayout currency_layout;
    //理财推荐
    private LinearLayout command_layout;
    private int balanceListFlag = 1;//持仓列表标识 1：上送参数为空时返回列表 2：上送code时返回列表
    private  String isFdyk ;
    private TextView finc_recomend_link;//查看按钮
 //   private boolean isToMyProduct = false;//是否进入持仓
    /** 全部的基金持仓  */
    public List<Map<String, Object>> fundBalancListAll;
    private int intentFlag = 0;//1:交易查询 2：账户管理 3：撤单 默认为0 页面跳转前重置为0，以便返回时刷新页面数据
    private ImageView week_profit_img,ten_thousand_profit_img;//七日年化收益，万份收益排序图标
    private ImageView item_title_img2,item_title_img3,item_title_img4,item_title_img5,item_title_img6,
            item_title_img7,item_title_img8;//非货币型列表头排序图标
    //排序标识
    private String dwjz_sortFlag="",change_of_da_sortFlag="",change_of_month_sortFlag = "",change_of_quarter_sortFlag="",
                    change_of_half_year_sortFlag="",change_of_year_sortFlag="",this_year_priceChange_sortFlag="";
    private int pageNumber = 1;//页码，默认为1
    private final String FINC_PAGE_SIZE = "10";//每页大小
    private boolean isAllSifangData = false;//是否全部使用四方数据标识，默认为否,排序后全部使用四方数据
    private List<Map<String, Object>> resultList;//每次请求返回数据BII
    private String fundAmt;//参考市值
    //查询基金推荐产品后是否进去基金推荐产品页面，由于从基金返回页面返回时会清空数据，所以每次点击查看重新查询一次基金推荐
    private boolean isEnterRecommand = false;
    private NewScrollView fund_newScrollView;//上滑控件
    private ScrollView content_scrollview;
    private RelativeLayout fund_first_title_layout;//标题布局
    private TextView finc_ib_back;
    private TextView tv_title;
    private TextView finc_ib_top_right_btn;
    private TextView txt_select;//筛选
    private List<String> currentSelectIds;//筛选选中项的id
    private List<SelectTypeData> mSelectList;
    private boolean isSelectFirst = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finc_first_activity_p606,false);
        activityTaskManager.addActivit(this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
        init();
        initItem();
        initListDatas();
        sortview();
    }

    private void init(){
        mSlipDrawerLayout = (SlipDrawerLayout)findViewById(R.id.drawerLayout);
        isLogin = BaseDroidApp.getInstanse().isLogin();
        /**  --------筛选----------*/
        stvType = (SelectTypeView)findViewById(R.id.stv_type);
        stvType.setDrawerLayout(mSlipDrawerLayout);
        txt_select = (TextView) findViewById(R.id.txt_select);
        stvType.setSelectView(true, 3);
        mSelectList = buildSelectData();
        stvType.setData(mSelectList,1,"基金公司");
        currentSelectIds = new ArrayList<>();
        for (SelectTypeData item : mSelectList) {
            currentSelectIds.add(item.getDefaultId());
        }
        txt_select.setOnClickListener(new View.OnClickListener() {//筛选
            @Override
            public void onClick(View view) {
                if(!"自选".equals(selectType)){
                    for (int m = 0; m < mSelectList.size(); m++) {
                        for (int n = 0; n < mSelectList.get(m).getList().size(); n++) {
                            Content item = mSelectList.get(m).getList().get(n);
                            if (currentSelectIds.get(m).equals(item.getContentNameID())) {
                                item.setSelected(true);
                            } else {
                                item.setSelected(false);
                            }
                        }
                    }
                    stvType.getAdapter().notifyDataSetChanged();
                    mSlipDrawerLayout.toggle();
                }
            }
        });
//        initItem();
        /**  --------标题布局----------*/
        fund_first_title_layout = (RelativeLayout) findViewById(R.id.fund_first_title_layout);
        finc_ib_back = (TextView) findViewById(R.id.finc_ib_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        finc_ib_top_right_btn = (TextView) findViewById(R.id.finc_ib_top_right_btn);
        /**  --------头部布局----------*/
        finc_head = (InvestPriceView) findViewById(R.id.finc_head);
        finc_head.setLoginLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLogin){//如果已登录且有持仓点击进入持仓
                    if(!StringUtil.isNullOrEmpty(fundBalancListAll)){
                        fincControl.fundBalancList = fundBalancListAll;
                        ActivityIntentTools.intentToActivity(FundPricesActivity.this,MyFincBalanceActivity.class);
                    }
                }
            }
        });
        loginbtn = finc_head.findViewById(R.id.login_bt);
        finc_head.setBackGoundWithColor(getResources().getColor(R.color.finc_backgroud));
        finc_head.setLoginSuccessCallBack(new LoginTask.LoginCallback() {//登陆成功回调
            @Override
            public void loginStatua(boolean b) {
                CommonApplication.getInstance().setCurrentAct(FundPricesActivity.this);
                isLogin = true;
                setLoginData();
            }
        });
        finc_head.setLoginOutAdvertiseImage(R.drawable.fund_adv_img);
        finc_head.setHelpMessage(getString(R.string.fund_help_info));
        finc_head.setRefvalueText("参考市值（元）");
        /**  --------列表布局----------*/
        leftListView = (ListView) findViewById(R.id.left_container_listview);
        rightListView = (ListView) findViewById(R.id.right_container_listview);
        titleHorsv = (SyncHorizontalScrollView) findViewById(R.id.title_horsv);
        contentHorsv = (SyncHorizontalScrollView) findViewById(R.id.content_horsv);
        // 设置两个水平控件的联动
        titleHorsv.setScrollView(contentHorsv);
        contentHorsv.setScrollView(titleHorsv);
        leftListView.setOnItemClickListener(new ListItemClickListener());
        rightListView.setOnItemClickListener(new ListItemClickListener());
        content_scrollview = (ScrollView) findViewById(R.id.content_scrollview);
        //向上滑动
        fund_newScrollView = (NewScrollView) findViewById(R.id.fund_newScrollView);
        fund_newScrollView.setCanHoriontalScroll(true);
        //判断是否滑动到顶部
        fund_newScrollView.setINewScrollViewListener(new NewScrollView.INewScrollViewListener() {
            @Override
            public boolean isToTop() {
                if (content_scrollview.getScrollY() <= 0) {
                    return true;
                }
                return false;
            }
        });
        fund_newScrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                fund_newScrollView.setPreScrollHeight(fund_first_title_layout.getMeasuredHeight());
            }
        },20);

        fund_newScrollView.setIScrollChangedListener(new NewScrollView.IScrollChangedListener() {
            @Override
            public boolean onScrollChanged(int x, int y) {
                float headHeight = finc_head.getMeasuredHeight();
                float titleHeight = fund_first_title_layout.getMeasuredHeight();
                float scrollHeight = headHeight-titleHeight;
                if(y/scrollHeight<0.5){
                    finc_ib_back.setCompoundDrawables(
                            FincUtil.getDrawable(FundPricesActivity.this,R.drawable.share_left_arrow),null,null,null);
                    finc_ib_top_right_btn.setCompoundDrawables(
                            null,null,FincUtil.getDrawable(FundPricesActivity.this,R.drawable.share_more_small),null);
                    tv_title.setTextColor(getResources().getColor(R.color.bg_white));
                    //fund_first_title_layout.setBackgroundColor(getResources().getColor(R.color.transparent_00));
                    //fund_first_title_layout.setAlpha(1);
                }else{
                    finc_ib_back.setCompoundDrawables(
                            FincUtil.getDrawable(FundPricesActivity.this,R.drawable.llbt_new_back_icon),null,null,null);
                    finc_ib_top_right_btn.setCompoundDrawables(
                            null,null,FincUtil.getDrawable(FundPricesActivity.this,R.drawable.llbt_point_more),null);
                    tv_title.setTextColor(getResources().getColor(R.color.fonts_black));
                    //fund_first_title_layout.setBackgroundColor(getResources().getColor(R.color.bg_white));
                }
                findViewById(R.id.finc_title_bg).setAlpha(y/scrollHeight*1.0f);
                return false;
            }
        });
            /**  --------导航栏按钮----------*/
        finc_ib_back.setOnClickListener(new MyOnClickListener());
        findViewById(R.id.finc_ib_top_share_btn).setOnClickListener(new MyOnClickListener());
        finc_ib_top_right_btn.setOnClickListener(new MyOnClickListener());
        /**  --------功能按钮----------*/
        findViewById(R.id.txt_query).setOnClickListener(new MyOnClickListener());
        findViewById(R.id.txt_protocol).setOnClickListener(new MyOnClickListener());
        findViewById(R.id.txt_cancel).setOnClickListener(new MyOnClickListener());
        /**  --------搜索输入框。点击进入搜索页面----------*/
        findViewById(R.id.clear_edit_search).setOnClickListener(new View.OnClickListener() {//搜索
            @Override
            public void onClick(View view) {
                intentFlag = 0;//跳转之前重置为0，返回时刷新页面使用
                ActivityIntentTools.intentToActivity(FundPricesActivity.this,FincSearchActivity.class);
            }
        });
//        initListDatas();
        fundCompanySpinner = (TextView) stvType.findViewById(R.id.company_tv);
        stvType.setClickListener(new SelectTypeView.SelectListener() {// 筛选
            @Override
            public void onClick(List<String> selectIds) {
                currentSelectIds = selectIds;
                if(isSelectFirst){
                    txt_select.setTextColor(getResources().getColor(R.color.fonts_pink));
                    Drawable drawable = getResources().getDrawable(R.drawable.fund_selected);
                    txt_select.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                    isSelectFirst = false;
                }
                currencyCode = selectIds.get(0).equals("000")?"":selectIds.get(0);
                riskLevelStr = selectIds.get(1).equals("0")?"":selectIds.get(1);
                fundState = selectIds.get(2).equals("00")?"":selectIds.get(2);
                if(!"自选".equals(selectType)){
                    clearListData();
                    if(isAllSifangData){
                        getQueryMultipleFund(fundType,fundState,currencyCode,riskLevelStr,fundCompanyCode,current_sortfield
                                ,sifangSortFlag(currSortFlag),pageNumber+"",FINC_PAGE_SIZE,"");
                    }else{
                        getOutlayOrLoginData();
                    }
                }
            }
            @Override
            public void companyOnClick() {//基金公司
                Intent intent = new Intent(FundPricesActivity.this , FundFundCompanyActivity.class);
                intent.putExtra("flag", "all");
                intent.putParcelableArrayListExtra("companyList", companyList);
                startActivityForResult(intent, 101);
            }
        });
        pullableFrameLayout = (PullableFrameLayout) findViewById(R.id.pullableFramelayout);
        pullableFrameLayout.setCurListView(leftListView);
        refreshLayout = (RefreshLayout) findViewById(R.id.pull_refresh);
        refreshLayout.setOnRefreshListener(new IRefreshLayoutListener(){
            @Override
            public void onLoadMore(View pullToRefreshLayout) {
                //TODO： 加载更多
                isRefreshFlag = true;
                if("自选".equals(selectType)){
                    refreshLayout.loadmoreCompleted(RefreshDataStatus.NoMoreData);
                }else{
                    if(isAllSifangData){
                        pageNumber++;
                        getQueryMultipleFund(fundType,fundState,currencyCode,riskLevelStr,fundCompanyCode,current_sortfield
                                ,sifangSortFlag(currSortFlag),pageNumber+"",FINC_PAGE_SIZE,"");
                    }else{
                        currentIndex+= 10;
                        if (isLogin) {
                            combainQueryFundInfos(currentIndex, pageSize, currencyCode,
                                    fundCompanyCode, riskLevelStr, fundKindType, fundType,fundState,currSortFlag,current_sortfield);
                        } else {
                            combainQueryFundInfosOutlay(currentIndex, pageSize, currencyCode,
                                    fundCompanyCode, riskLevelStr, fundKindType, fundType,fundState,currSortFlag,current_sortfield);
                        }
                    }
                }

            }
        });
        currency_type_listview = (ListView) findViewById(R.id.currency_type_listview);
        currency_type_listview.setOnItemClickListener(new ListItemClickListener());
        currency_pull_refresh = (RefreshLayout) findViewById(R.id.currency_pull_refresh);
        currency_layout = (LinearLayout) findViewById(R.id.currency_layout);
        currency_pull_refresh.setOnRefreshListener(new IRefreshLayoutListener(){
            @Override
            public void onLoadMore(View pullToRefreshLayout) {
                //TODO： 加载更多
                isRefreshFlag = true;
                currentIndex+= 10;
                if (isLogin) {
                    combainQueryFundInfos(currentIndex, pageSize, currencyCode,
                            fundCompanyCode, riskLevelStr, fundKindType, fundType,fundState,currSortFlag,current_sortfield);
                } else {
                    combainQueryFundInfosOutlay(currentIndex, pageSize, currencyCode,
                            fundCompanyCode, riskLevelStr, fundKindType, fundType,fundState,currSortFlag,current_sortfield);
                }
            }
        });
        /**  --------基金推荐----------*/
        command_layout = (LinearLayout) findViewById(R.id.command_layout);
        finc_recomend_link = (TextView) findViewById(R.id.finc_recomend_link);
        //查看
        finc_recomend_link.setOnClickListener(new View.OnClickListener() {//基金推荐
            @Override
            public void onClick(View view) {
//                FincControl.isRecommend=true;
                isEnterRecommand = true;
                BaseHttpEngine.showProgressDialog();
                requestCommConversationId();
                //ActivityIntentTools.intentToActivityForResult(FundPricesActivity.this,OrcmProductListActivity.class,1,null);
            }
        });
        //关闭
        findViewById(R.id.finc_recomend_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command_layout.setVisibility(View.GONE);
            }
        });

    }

    /**
     * 排序
     */
    private void sortview(){
        //货币型
        week_profit_img = (ImageView) findViewById(R.id.week_profit_img);
        ten_thousand_profit_img = (ImageView) findViewById(R.id.ten_thousand_profit_img);
        findViewById(R.id.week_profit_layout).setOnClickListener(new SortViewClickListener());
        findViewById(R.id.ten_thousand_profit_layout).setOnClickListener(new SortViewClickListener());
        //非货币型
        findViewById(R.id.item_title_layout2).setOnClickListener(new SortViewClickListener());
        findViewById(R.id.item_title_layout3).setOnClickListener(new SortViewClickListener());
        findViewById(R.id.item_title_layout4).setOnClickListener(new SortViewClickListener());
        findViewById(R.id.item_title_layout5).setOnClickListener(new SortViewClickListener());
        findViewById(R.id.item_title_layout6).setOnClickListener(new SortViewClickListener());
        findViewById(R.id.item_title_layout7).setOnClickListener(new SortViewClickListener());
        findViewById(R.id.item_title_layout8).setOnClickListener(new SortViewClickListener());
        item_title_img2 = (ImageView) findViewById(R.id.item_title_img2);
        item_title_img3 = (ImageView) findViewById(R.id.item_title_img3);
        item_title_img4 = (ImageView) findViewById(R.id.item_title_img4);
        item_title_img5 = (ImageView) findViewById(R.id.item_title_img5);
        item_title_img6 = (ImageView) findViewById(R.id.item_title_img6);
        item_title_img7 = (ImageView) findViewById(R.id.item_title_img7);
        item_title_img8 = (ImageView) findViewById(R.id.item_title_img8);

    }

    /**
     * 设置排序图标和标识
     * @param imageView 排序图标
     * @param sortFlag 排序标识
     * @return
     */
    private String setSortImg(ImageView imageView,String sortFlag){
        switch(sortFlag){
            case "":
                sortFlag = "1";
                imageView.setImageResource(R.drawable.prms_gold_rise);
                break;
            case "1":
                sortFlag = "2";
                imageView.setImageResource(R.drawable.prms_gold_fall);
                break;
            case "2":
                sortFlag = "";
                imageView.setImageResource(R.drawable.finc_paixu);
                break;
        }
        return sortFlag;
    }

    /**
     * 排序字段点击事件
     */
    public class SortViewClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.week_profit_layout:////七日年化收益
                    clearListData();
                    current_sortfield = "5";
                    tenThousand_sortFlag = "";
                    ten_thousand_profit_img.setImageResource(R.drawable.finc_paixu);
                    weekly_profit_sortFlag = setSortImg(week_profit_img,weekly_profit_sortFlag);
                    currSortFlag = weekly_profit_sortFlag;
                    if(currSortFlag == null ||"".equals(currSortFlag)){
                        current_sortfield = "";
                    }
                    getOutlayOrLoginData();
                    break;
                case R.id.ten_thousand_profit_layout:////万份收益
                    clearListData();
                    current_sortfield = "4";
                    weekly_profit_sortFlag = "";
                    week_profit_img.setImageResource(R.drawable.finc_paixu);
                    tenThousand_sortFlag = setSortImg(ten_thousand_profit_img,tenThousand_sortFlag);
                    currSortFlag = tenThousand_sortFlag;
                    if(currSortFlag == null ||"".equals(currSortFlag)){
                        current_sortfield = "";
                    }
                    getOutlayOrLoginData();
                    break;
                case R.id.item_title_layout2://单位净值
//                    clearListData();
                    if(!"自选".equals(selectType)){
                        clearListData();
                        setSortImageGray(null);
                        current_sortfield = "1";
                        isAllSifangData = false;
                        //除点击的类型外其他的排序标识全置为默认""
                        change_of_da_sortFlag="";change_of_month_sortFlag = "";change_of_quarter_sortFlag="";
                        change_of_half_year_sortFlag="";change_of_year_sortFlag="";this_year_priceChange_sortFlag="";
                        dwjz_sortFlag = setSortImg(item_title_img2,dwjz_sortFlag);
                        currSortFlag = dwjz_sortFlag;
                        if(currSortFlag == null ||"".equals(currSortFlag)){
                            current_sortfield = "";
                        }
                        getOutlayOrLoginData();
                    }

                    break;
                case R.id.item_title_layout3://日涨跌幅
//                    clearListData();
                    if(!"自选".equals(selectType)&&!"资管计划产品".equals(selectType)&&!"信托产品".equals(selectType)){
                        pageNumber = 1;
                        //combinateList.clear();
                        clearListData();
                        isAllSifangData = true;
                        setSortImageGray(null);
                        dwjz_sortFlag="";change_of_month_sortFlag = "";change_of_quarter_sortFlag="";
                        change_of_half_year_sortFlag="";change_of_year_sortFlag="";this_year_priceChange_sortFlag="";
                        current_sortfield ="curr_percent_diff";
                        change_of_da_sortFlag = setSortImg(item_title_img3,change_of_da_sortFlag);
                        currSortFlag = change_of_da_sortFlag;
                        if(currSortFlag == null ||"".equals(currSortFlag)){
                            current_sortfield = "";
                        }
                        getQueryMultipleFund(fundType,fundState,currencyCode,riskLevelStr,fundCompanyCode,current_sortfield
                                ,sifangSortFlag(currSortFlag),pageNumber+"",FINC_PAGE_SIZE,"");
                    }
                    break;
                case R.id.item_title_layout4://月涨跌幅
//                    clearListData();
                    if(!"自选".equals(selectType)&&!"资管计划产品".equals(selectType)&&!"信托产品".equals(selectType)){
                        pageNumber = 1;
                        clearListData();
//                        combinateList.clear();
//                        leftAdapter = null;
//                        rightAdapter = null;
                        isAllSifangData = true;
                        setSortImageGray(null);
                        dwjz_sortFlag="";change_of_da_sortFlag="";change_of_quarter_sortFlag="";
                        change_of_half_year_sortFlag="";change_of_year_sortFlag="";this_year_priceChange_sortFlag="";
                        current_sortfield ="change_of_month";
                        change_of_month_sortFlag = setSortImg(item_title_img4,change_of_month_sortFlag);
                        currSortFlag = change_of_month_sortFlag;
                        if(currSortFlag == null ||"".equals(currSortFlag)){
                            current_sortfield = "";
                        }
                        getQueryMultipleFund(fundType,fundState,currencyCode,riskLevelStr,fundCompanyCode,current_sortfield
                                ,sifangSortFlag(currSortFlag),pageNumber+"",FINC_PAGE_SIZE,"");
                    }
                    break;
                case R.id.item_title_layout5://季涨跌幅
//                    clearListData();
                    if(!"自选".equals(selectType)&&!"资管计划产品".equals(selectType)&&!"信托产品".equals(selectType)){
                        pageNumber = 1;
//                        combinateList.clear();
                        clearListData();
                        isAllSifangData = true;
                        setSortImageGray(null);
                        dwjz_sortFlag="";change_of_da_sortFlag="";change_of_month_sortFlag = "";
                        change_of_half_year_sortFlag="";change_of_year_sortFlag="";this_year_priceChange_sortFlag="";
                        current_sortfield ="change_of_quarter";
                        change_of_quarter_sortFlag = setSortImg(item_title_img5,change_of_quarter_sortFlag);
                        currSortFlag = change_of_quarter_sortFlag;
                        if(currSortFlag == null ||"".equals(currSortFlag)){
                            current_sortfield = "";
                        }
                        getQueryMultipleFund(fundType,fundState,currencyCode,riskLevelStr,fundCompanyCode,current_sortfield
                                ,sifangSortFlag(currSortFlag),pageNumber+"",FINC_PAGE_SIZE,"");
                    }
                    break;
                case R.id.item_title_layout6://半年涨跌幅
//                    clearListData();
                    if(!"自选".equals(selectType)&&!"资管计划产品".equals(selectType)&&!"信托产品".equals(selectType)){
                        pageNumber = 1;
//                        combinateList.clear();
                        clearListData();
                        isAllSifangData = true;
                        setSortImageGray(null);
                        dwjz_sortFlag="";change_of_da_sortFlag="";change_of_month_sortFlag = "";
                        change_of_quarter_sortFlag="";change_of_year_sortFlag="";this_year_priceChange_sortFlag="";
                        current_sortfield ="change_of_half_year";
                        change_of_half_year_sortFlag = setSortImg(item_title_img6,change_of_half_year_sortFlag);
                        currSortFlag = change_of_half_year_sortFlag;
                        if(currSortFlag == null ||"".equals(currSortFlag)){
                            current_sortfield = "";
                        }
                        getQueryMultipleFund(fundType,fundState,currencyCode,riskLevelStr,fundCompanyCode,current_sortfield
                                ,sifangSortFlag(currSortFlag),pageNumber+"",FINC_PAGE_SIZE,"");
                    }
                    break;
                case R.id.item_title_layout7://年涨跌幅
//                    clearListData();
                    if(!"自选".equals(selectType)&&!"资管计划产品".equals(selectType)&&!"信托产品".equals(selectType)){
                        pageNumber = 1;
//                        combinateList.clear();
                        clearListData();
                        isAllSifangData = true;
                        setSortImageGray(null);
                        dwjz_sortFlag="";change_of_da_sortFlag="";change_of_month_sortFlag = "";
                        change_of_quarter_sortFlag="";change_of_half_year_sortFlag="";this_year_priceChange_sortFlag="";
                        current_sortfield ="change_of_year";
                        change_of_year_sortFlag = setSortImg(item_title_img7,change_of_year_sortFlag);
                        currSortFlag = change_of_year_sortFlag;
                        if(currSortFlag == null ||"".equals(currSortFlag)){
                            current_sortfield = "";
                        }
                        getQueryMultipleFund(fundType,fundState,currencyCode,riskLevelStr,fundCompanyCode,current_sortfield
                                ,sifangSortFlag(currSortFlag),pageNumber+"",FINC_PAGE_SIZE,"");
                    }
                    break;
                case R.id.item_title_layout8://今年涨跌幅
//                    clearListData();
                    if(!"自选".equals(selectType)&&!"资管计划产品".equals(selectType)&&!"信托产品".equals(selectType)){
                        pageNumber = 1;
//                        combinateList.clear();
                        clearListData();
                        isAllSifangData = true;
                        setSortImageGray(null);
                        dwjz_sortFlag="";change_of_da_sortFlag="";change_of_month_sortFlag = "";
                        change_of_quarter_sortFlag="";change_of_half_year_sortFlag="";change_of_year_sortFlag="";
                        current_sortfield ="this_year_priceChange";
                        this_year_priceChange_sortFlag = setSortImg(item_title_img8,this_year_priceChange_sortFlag);
                        currSortFlag = this_year_priceChange_sortFlag;
                        if(currSortFlag == null ||"".equals(currSortFlag)){
                            current_sortfield = "";
                        }
                        getQueryMultipleFund(fundType,fundState,currencyCode,riskLevelStr,fundCompanyCode,current_sortfield
                                ,sifangSortFlag(currSortFlag),pageNumber+"",FINC_PAGE_SIZE,"");
                    }
                    break;
            }
        }
    }

    /**
     * 把BII排序标识转化为四方排序标识
     * @param sortFlag 1：升序 2：降序
     * @return
     */
    private String sifangSortFlag(String sortFlag){
        if(!StringUtil.isNullOrEmpty(sortFlag)){
            if("1".equals(sortFlag)){
                sortFlag = "asc";
            }
            if("2".equals(sortFlag)){
                sortFlag = "desc";
            }
        }
        return sortFlag;
    }
    /**
     * 把排序图标全部置为默认黑色图标
     * @param isvisiable 0：显示图标 1：隐藏图标 null不作处理
     */
    private void setSortImageGray(String isvisiable){
        if(StringUtil.isNullOrEmpty(isvisiable)){
            item_title_img2.setImageResource(R.drawable.finc_paixu);
            item_title_img3.setImageResource(R.drawable.finc_paixu);
            item_title_img4.setImageResource(R.drawable.finc_paixu);
            item_title_img5.setImageResource(R.drawable.finc_paixu);
            item_title_img6.setImageResource(R.drawable.finc_paixu);
            item_title_img7.setImageResource(R.drawable.finc_paixu);
            item_title_img8.setImageResource(R.drawable.finc_paixu);
        }
        if("0".equals(isvisiable)){
            item_title_img2.setVisibility(View.VISIBLE);
            item_title_img3.setVisibility(View.VISIBLE);
            item_title_img4.setVisibility(View.VISIBLE);
            item_title_img5.setVisibility(View.VISIBLE);
            item_title_img6.setVisibility(View.VISIBLE);
            item_title_img7.setVisibility(View.VISIBLE);
            item_title_img8.setVisibility(View.VISIBLE);
        }
        if("1".equals(isvisiable)){
            item_title_img2.setVisibility(View.GONE);
            item_title_img3.setVisibility(View.GONE);
            item_title_img4.setVisibility(View.GONE);
            item_title_img5.setVisibility(View.GONE);
            item_title_img6.setVisibility(View.GONE);
            item_title_img7.setVisibility(View.GONE);
            item_title_img8.setVisibility(View.GONE);
        }
        if("2".equals(isvisiable)){//资管及信托隐藏四方排序图标
            item_title_img3.setVisibility(View.GONE);
            item_title_img4.setVisibility(View.GONE);
            item_title_img5.setVisibility(View.GONE);
            item_title_img6.setVisibility(View.GONE);
            item_title_img7.setVisibility(View.GONE);
            item_title_img8.setVisibility(View.GONE);
        }
    }

    /**
     * 重置全部排序标识
     */
    private void setSortFlag(){
        isAllSifangData = false;
        tenThousand_sortFlag="";weekly_profit_sortFlag="";
        dwjz_sortFlag="";change_of_da_sortFlag="";change_of_month_sortFlag = "";change_of_quarter_sortFlag="";
        change_of_half_year_sortFlag="";change_of_year_sortFlag="";this_year_priceChange_sortFlag="";
        current_sortfield = "";
        currSortFlag = "";
    }

    @Override
    public void commonHttpErrorCallBack(String requestMethod) {
        super.commonHttpErrorCallBack(requestMethod);
        if(isLogin){
            if("PsnQueryFundDetail".equals(requestMethod) && isRefreshFlag){//刷新状态
                if("货币型".equals(selectType)||"理财型".equals(selectType)){
                    currency_pull_refresh.loadmoreCompleted(RefreshDataStatus.Failed);
                }else{
                    refreshLayout.loadmoreCompleted(RefreshDataStatus.Failed);
                }
            }
        }else{
            if("PsnFundQueryOutlay".equals(requestMethod) && isRefreshFlag){//刷新状态
                if("货币型".equals(selectType)||"理财型".equals(selectType)){
                    currency_pull_refresh.loadmoreCompleted(RefreshDataStatus.Failed);
                }else{
                    refreshLayout.loadmoreCompleted(RefreshDataStatus.Failed);
                }

            }
        }

    }

    /**
     * 登陆后的数据处理
     */
    private void setLoginData(){
        clearListData();
        fincControl.cleanAllData();
        isAllSifangData = false;
        setSortImageGray(null);
        pageNumber = 1;
        currentIndex = 0;
        currencyCode = "001";
        riskLevelStr = "";
        fundKindType = "00";
        fundType = "00";
        fundCompanyCode = "";
        fundState = "0";
        isRefresh = false;
        isRefreshFlag = false;
        balanceListFlag = 1;
        intentFlag = 0;
        setSortFlag();
        allHiden();
        selectType = "全部";
        setClickStyle(selectType);
        my_select_layout.setVisibility(View.VISIBLE);
        FincUtil.setVisibilityAndTextColor(FundPricesActivity.this,all_tv,false,all_img,false);
        isSelectFirst = true;
        //重置筛选
        mSelectList = buildSelectData();
        stvType.setData(mSelectList,1,"基金公司");
        currentSelectIds = new ArrayList<>();
        for (SelectTypeData item : mSelectList) {
            currentSelectIds.add(item.getDefaultId());
        }
        txt_select.setTextColor(getResources().getColor(R.color.fonts_dark_gray));
        Drawable drawable = getResources().getDrawable(R.drawable.fund_select);
        txt_select.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        initListDatas();
    }

    /**
     * 初始化筛选页面数据
     */
    private void initListDatas() {
        isLogin = BaseDroidApp.getInstanse().isLogin();
        isRefresh = true;
        // 获取基金公司列表
        if (isLogin) {
            findViewById(R.id.ll_tab).setVisibility(View.VISIBLE);
            BaseHttpEngine.showProgressDialogCanGoBack();
            BaseHttpEngine.showProgressDialog();
  //          requestPsnAssetBalanceQuery();
            doCheckRequestPsnInvestmentManageIsOpen();
        } else {
            BaseHttpEngine.showProgressDialog();
            getFundCompanyListOutlay();
        }

    }

    @Override
    public void doCheckRequestQueryInvtBindingInfoCallback(
            Object resultObj) {
        super.doCheckRequestQueryInvtBindingInfoCallback(resultObj);
        if(!fincControl.ifhaveaccId || !fincControl.ifInvestMent) return;
        if(intentFlag == 0){
            getFundCompanyList();
        }
        if(intentFlag == 1){
            intentFlag = 0;//跳转之前重置为0，返回时刷新页面使用
            ActivityIntentTools.intentToActivity(FundPricesActivity.this,FundqueryMenuActivity.class);
        }
        if(intentFlag == 2){
            intentFlag = 0;//跳转之前重置为0，返回时刷新页面使用
            ActivityIntentTools.intentToActivity(FundPricesActivity.this,FincAccManagerMenuActivity.class);
        }
        if(intentFlag == 3){
            requestCommConversationId();
        }

    }

    /**
     * 获取基金公司列表
     */
    private void getFundCompanyListOutlay() {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Finc.FINC_GETFUNDCOMPANCYLIST_OUTLAY);
        biiRequestBody.setParams(null);
        HttpManager.requestOutlayBii(biiRequestBody, this,
                "getFundCompanyListCallback");
    }

    /**
     * 公司列表回调
     * @param
     */
    @Override
    public void getFundCompanyListCallback(Object resultObj) {
        super.getFundCompanyListCallback(resultObj);
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        fincControl.fundCompanyList = (List<Map<String, String>>) biiResponseBody.getResult();
        List<Map<String, String>> list =  fincControl.fundCompanyList;
        for (Map<String, String> map : list) {
            String companyName = map.get(Finc.FINC_GETFUNDCOMPANCYLIST_FUNDCOMPANYNAME);
            // 为基金公司下拉列表赋值
            FundCompany fundCompany = new FundCompany();
            fundCompany.setFundCompanyName(companyName);
            fundCompany.setFundCompanyCode(map
                    .get(Finc.FINC_GETFUNDCOMPANCYLIST_FUNDCOMPANYCODE));
            fundCompany.setChecked(false);
            fundCompany.setAlpha(ChineseCharToEn.cn2py(companyName));
            companyList.add(fundCompany);

        }
        Collections.sort(companyList, new Comparator<FundCompany>() {

            public int compare(FundCompany o1, FundCompany o2) {
                //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
                if (o2.getAlpha().equals("#")) {
                    return -1;
                } else if (o1.getAlpha().equals("#")) {
                    return 1;
                } else {
                    return o1.getAlpha().compareTo(o2.getAlpha());
                }
            }});
        initFundCompany();
        if (isLogin) {
//            requestCommConversationId();
            requestPsnFundQueryFundBalance();
        } else {
            combainQueryFundInfosOutlay(currentIndex, pageSize, currencyCode,
                    fundCompanyCode, riskLevelStr, fundKindType, fundType,fundState,currSortFlag,current_sortfield);
        }

    }

    @Override
    public void requestCommConversationIdCallBack(Object resultObj) {
        super.requestCommConversationIdCallBack(resultObj);
        if(intentFlag == 0){
            requestPsnOcrmProductQuery("01", "", ConstantGloble.FOREX_PAGESIZE, "0", true);
        }
        if(intentFlag == 3){
            fundQueryEnTrust(0, 10, true);
        }

 //       requestPsnFundQueryFundBalance();
//        this.getHttpTools().registAllErrorCode("");
//        requestPsnOcrmProductQuery("01", "", ConstantGloble.FOREX_PAGESIZE, "0", true);
//        combainQueryFundInfos(currentIndex, pageSize, currencyCode,
//                    fundCompanyCode, riskLevelStr, fundKindType, fundType,fundState,currSortFlag,current_sortfield);

    }

    @Override
    public void ocrmProductQueryCallBack(Object resultObj) {
        super.ocrmProductQueryCallBack(resultObj);
        if(isEnterRecommand){
            BaseHttpEngine.dissMissProgressDialog();
            FincControl.isRecommend=true;
            ActivityIntentTools.intentToActivityForResult(FundPricesActivity.this,OrcmProductListActivity.class,1,null);
        }else{
            //显示推荐基金链接
            if (StringUtil.isNullOrEmpty(fincControl.OcrmProductMap)
                    || StringUtil.isNullOrEmpty(fincControl.OcrmProductList)) {
            }else{
                command_layout.setVisibility(View.VISIBLE);
            }
            combainQueryFundInfos(currentIndex, pageSize, currencyCode,
                    fundCompanyCode, riskLevelStr, fundKindType, fundType,fundState,currSortFlag,current_sortfield);
        }
    }
    /** 获取未登录基金行情 */
    private void combainQueryFundInfosOutlay(int currentIndex, int pageSize,
                                             String currencyCode, String fundCompanyCode, String risklv,
                                             String fntype, String fundProductTypeStr,String fundState,
                                             String sortFlag ,String sortField) {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Finc.PSNQUERYFUNDDETAIL_OUTLAY);
        Map<String, String> params = new HashMap<String, String>();
        params.put(Finc.COMBINQUERY_CURRENTINDEX, String.valueOf(currentIndex));
        params.put(Finc.COMBINQUERY_PAGESIZE, String.valueOf(pageSize));
        params.put(Finc.PSNQUERYFUNDDETAIL_CURRENCYCODE, currencyCode);
        params.put(Finc.PSNQUERYFUNDDETAIL_COMPANY, fundCompanyCode);
        params.put(Finc.PSNQUERYFUNDDETAIL_RISKGRADE, risklv);
        params.put(Finc.PSNQUERYFUNDDETAIL_FUNDKIND, fntype);
        params.put(Finc.PSNQUERYFUNDDETAIL_FUNDTYPE, fundProductTypeStr);
        params.put(Finc.PSNQUERYFUNDDETAIL_FUNDSTATE, fundState);
        params.put(Finc.PSNQUERYFUNDDETAIL_SORTFLAG, sortFlag);
        params.put(Finc.PSNQUERYFUNDDETAIL_SORTFIELD, sortField);
        biiRequestBody.setParams(params);
        HttpManager.requestOutlayBii(biiRequestBody, this,
                "combainQueryFundInfosCallback");

    }

    /**
     * 在途交易查询回调
     * @param resultObj
     */
    @Override
    public void fundQueryEnTrustCallback(Object resultObj) {
        super.fundQueryEnTrustCallback(resultObj);
        BaseHttpEngine.dissMissProgressDialog();
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
                .getResult();
        if (StringUtil.isNullOrEmpty((List<Map<String, Object>>) resultMap
                .get(Finc.FINC_QUERYEXTRADAY_RESULTLIST))) {
//            BaseDroidApp.getInstanse().showInfoMessageDialog(
//                    getString(R.string.finc_query_noresult_error));
            MessageDialog.showMessageDialog(this,getString(R.string.finc_query_noresult_error));
            return;
        }
        BaseDroidApp.getInstanse().getBizDataMap().put(Finc.D_ENTRUST_RESULTMAP, resultMap);
        intentFlag = 0;//跳转之前重置为0，返回时刷新页面使用
        Intent intent = new Intent();
        intent.setClass(this,FundQueryEffectiveActivity.class);
        startActivity(intent);
    }

    public void combainQueryFundInfosCallback(Object resultObj) {
        BaseHttpEngine.dissMissProgressDialog();
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
                .getResult();
        if (StringUtil.isNullOrEmpty(resultMap)) {
            BiiHttpEngine.dissMissProgressDialog();
//            BaseDroidApp.getInstanse().showInfoMessageDialog(
//                    getString(R.string.acc_transferquery_null));
            MessageDialog.showMessageDialog(this,getString(R.string.acc_transferquery_null));
            return;
        }
        recordNumber = Integer.valueOf((String) resultMap
                .get(Acc.RECORDNUMBER_RES));
        if (isRefresh) {
            isRefresh = false;
            combinateList.clear();
        }
        combinateList.addAll((List<Map<String, Object>>) resultMap
                .get(Finc.COMBINQUERY_LIST));
        if(StringUtil.isNullOrEmpty(combinateList)){
            MessageDialog.showMessageDialog(this,getString(R.string.acc_transferquery_null));
        }
        if("货币型".equals(selectType)||"理财型".equals(selectType)){//不同类型加载不同布局
            if(currencyListAdapter == null){
                currencyListAdapter = new CommonAdapter<Map<String, Object>>(this,combinateList,myCurrencyListAdapter);
                currency_type_listview.setAdapter(currencyListAdapter);
            }else{
                currencyListAdapter.notifyDataSetChanged();
                if(currentIndex > recordNumber){
                    currency_pull_refresh.loadmoreCompleted(RefreshDataStatus.NoMoreData);
                }else{
                    currency_pull_refresh.loadmoreCompleted(RefreshDataStatus.Successed);
                }
            }
        }else{
            if(leftAdapter == null && rightAdapter == null){
                leftAdapter = new CommonAdapter<Map<String, Object>>(this,combinateList,myLeftAdapter);
                rightAdapter = new CommonAdapter<Map<String, Object>>(this,combinateList,myRightAdapter);
                leftListView.setAdapter(leftAdapter);
                rightListView.setAdapter(rightAdapter);
            }else{
                leftAdapter.notifyDataSetChanged();
                rightAdapter.notifyDataSetChanged();
                if(currentIndex > recordNumber){
                    refreshLayout.loadmoreCompleted(RefreshDataStatus.NoMoreData);
                }else{
                    refreshLayout.loadmoreCompleted(RefreshDataStatus.Successed);
                }
            }
        }
        /*List<Map<String, Object>>*/ resultList = new ArrayList<Map<String, Object>>();
        resultList = (List<Map<String, Object>>) resultMap.get(Finc.COMBINQUERY_LIST);
        if(!StringUtil.isNullOrEmpty(resultList)) {
            String multipleFundBakCode = "";
            for (int i = 0; i < resultList.size(); i++) {//获取上送的fundcode，用&连接
                if (i == 0) {
                    multipleFundBakCode = (String) resultList.get(i).get("fundCode");
                } else {
                    multipleFundBakCode = multipleFundBakCode + "&" + (String) resultList.get(i).get("fundCode");
                }
            }
            //请求四方数据
            getQueryMultipleFund(fundType,fundState,currencyCode,riskLevelStr,fundCompanyCode,current_sortfield
                    ,sifangSortFlag(currSortFlag),pageNumber+"",FINC_PAGE_SIZE,multipleFundBakCode);
        }/*else{
            if(!isRefresh){
                if("货币型".equals(selectType)){//不同类型加载不同布局
                    currency_pull_refresh.loadmoreCompleted(RefreshDataStatus.NoMoreData);
                }else{
                    refreshLayout.loadmoreCompleted(RefreshDataStatus.NoMoreData);
                }
            }
        }*/


    }



    /**
     * 3.1 基金列表查询
     * @param fundType 基金类型
     * @param fundStatus 基金状态
     * @param transCurrency 交易币种
     * @param levelOfRisk 风险级别
     * @param fundCompanyCode 基金公司代码
     * @param sortFile 排序字段
     * @param sortType 排序方式
     * @param pageNo 页码
     * @param pageSize 每页大小
     * @param
     */
    private void getQueryMultipleFund(String fundType, String fundStatus, String transCurrency, String levelOfRisk,
                                      String fundCompanyCode, String sortFile, String sortType, final String pageNo,
                                      String pageSize,String multipleFundBakCode){
        QueryMultipleFundRequestParams queryMultipleFund;
        if(isAllSifangData){//全部使用四方数据是不上送基金代码
            BaseHttpEngine.showProgressDialog();
            queryMultipleFund = new QueryMultipleFundRequestParams(fundType,fundStatus,transCurrency,
                    levelOfRisk, fundCompanyCode,sortFile,sortType,pageNo, pageSize);
        }else{//使用BII返回的基金代码查询四方的涨跌幅
            queryMultipleFund = new QueryMultipleFundRequestParams(multipleFundBakCode);
        }
        HttpHelp h= HttpHelp.getInstance();
        h.postHttpFromSF(this,queryMultipleFund);
        h.setHttpErrorCallBack(null);
        h.setHttpResponseCallBack(new IHttpResponseCallBack() {
            @Override
            public boolean responseCallBack(String response, Object extendParams) {
                QueryMultipleFundResponseData data = GsonTools.fromJson(response,QueryMultipleFundResponseData.class);
                QueryMultipleFundResult body = data.getBody();
                List<QueryMultipleFundResult.QueryMultipleFundItem> itemList = body.getItems();
                if(!StringUtil.isNullOrEmpty(itemList)){
                    if(StringUtil.isNullOrEmpty(fincControl.fincList)){
                        fincControl.fincList = itemList;
                    }else{
                        fincControl.fincList.addAll(itemList);
                    }
                }else{
                    fincControl.fincList = itemList;
                }
                if(StringUtil.isNullOrEmpty(combinateList)&&isAllSifangData){
                    resultList.clear();
                }
                String isNextPage = body.getIsNextPage();
                if(StringUtil.isNullOrEmpty(resultList)){
                    if(isAllSifangData){//全部使用四方数据
                        BaseHttpEngine.dissMissProgressDialog();
                        for(QueryMultipleFundResult.QueryMultipleFundItem item : itemList){
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("fundCode",item.getFundBakCode());
                            map.put("fundName",item.getFundName());
                            map.put("netPrice",item.getDwjz());
                            map.put("endDate",item.getJzTime());
                            map.put("currPercentDiff",item.getCurrPercentDiff());
                            map.put("changeOfMonth",item.getChangeOfMonth());
                            map.put("changeOfQuarter",item.getChangeOfQuarter());
                            map.put("changeOfHalfYear",item.getChangeOfHalfYear());
                            map.put("changeOfYear",item.getChangeOfYear());
                            map.put("thisYearPriceChange",item.getThisYearPriceChange());
                            map.put("changeOfWeek",item.getChangeOfWeek());
                            combinateList.add(map);
                        }

                    }
                    //combinateList.addAll(resultList);
                }else{
                    //将四方数据根据基金代码匹配后加入BII数据中
                    ListUtils.synchronousList(combinateList, itemList, new ListUtils.ISynchronousList<Map<String, Object>, QueryMultipleFundResult.QueryMultipleFundItem>() {
                        @Override
                        public boolean doSomeThing(Map<String, Object> stringObjectMap, QueryMultipleFundResult.QueryMultipleFundItem queryMultipleFundItem) {
                            String fundCode = (String) stringObjectMap.get("fundCode");//BII基金代码
                            String fundBakCode = queryMultipleFundItem.getFundBakCode();//四方基金代码
                            if(fundCode.equals(fundBakCode)){
                                if(isAllSifangData){
                                    stringObjectMap.put("fundCode",fundBakCode);
                                    stringObjectMap.put("fundName",queryMultipleFundItem.getFundName());
                                    stringObjectMap.put("netPrice",queryMultipleFundItem.getDwjz());
                                    stringObjectMap.put("endDate",queryMultipleFundItem.getJzTime());
                                }
                                stringObjectMap.put("currPercentDiff",queryMultipleFundItem.getCurrPercentDiff());
                                stringObjectMap.put("changeOfMonth",queryMultipleFundItem.getChangeOfMonth());
                                stringObjectMap.put("changeOfQuarter",queryMultipleFundItem.getChangeOfQuarter());
                                stringObjectMap.put("changeOfHalfYear",queryMultipleFundItem.getChangeOfHalfYear());
                                stringObjectMap.put("changeOfYear",queryMultipleFundItem.getChangeOfYear());
                                stringObjectMap.put("thisYearPriceChange",queryMultipleFundItem.getThisYearPriceChange());
                                return true;
                            }
                            return false;
                        }
                    });
                }
//                combinateList.addAll(resultList);
                if("货币型".equals(selectType)||"理财型".equals(selectType)){//不同类型加载不同布局
                    if(currencyListAdapter == null){
                        currencyListAdapter = new CommonAdapter<Map<String, Object>>(FundPricesActivity.this,combinateList,myCurrencyListAdapter);
                        currency_type_listview.setAdapter(currencyListAdapter);
                    }else{
                        currencyListAdapter.notifyDataSetChanged();
//                        if(isAllSifangData){
//                            if("Y".equals(isNextPage)){
//                                currency_pull_refresh.loadmoreCompleted(RefreshDataStatus.NoMoreData);
//                            }else{
//                                currency_pull_refresh.loadmoreCompleted(RefreshDataStatus.Successed);
//                            }
//                        }else{
//                            if(currentIndex > recordNumber){
//                                currency_pull_refresh.loadmoreCompleted(RefreshDataStatus.NoMoreData);
//                            }else{
//                                currency_pull_refresh.loadmoreCompleted(RefreshDataStatus.Successed);
//                            }
//                        }
                    }
                }else{
                    if(leftAdapter == null && rightAdapter == null){
                        leftAdapter = new CommonAdapter<Map<String, Object>>(FundPricesActivity.this,combinateList,myLeftAdapter);
                        rightAdapter = new CommonAdapter<Map<String, Object>>(FundPricesActivity.this,combinateList,myRightAdapter);
                        leftListView.setAdapter(leftAdapter);
                        rightListView.setAdapter(rightAdapter);
                    }else{
                        leftAdapter.notifyDataSetChanged();
                        rightAdapter.notifyDataSetChanged();
                        if(isAllSifangData){
                            if("N".equals(isNextPage)){
                                refreshLayout.loadmoreCompleted(RefreshDataStatus.NoMoreData);
                            }else{
                                refreshLayout.loadmoreCompleted(RefreshDataStatus.Successed);
                            }
                        }/*else{
                            if(currentIndex > recordNumber){
                                refreshLayout.loadmoreCompleted(RefreshDataStatus.NoMoreData);
                            }else{
                                refreshLayout.loadmoreCompleted(RefreshDataStatus.Successed);
                            }
                        }*/

                    }
                }
                return false;
            }
        });
        h.setOkHttpErrorCode(new IOkHttpErrorCode() {
            @Override
            public boolean handlerErrorCode(BaseResponseData responseData, Object extendParams) {
                MessageDialog.closeDialog(); //关闭通讯框
                return true;
            }
        });
        h.setHttpErrorCallBack(new IHttpErrorCallBack() {
            @Override
            public boolean onError(String exceptionMessage, Object extendParams) {
                MessageDialog.closeDialog(); //关闭通讯框
                return true;
            }
        });


    }

//    /**
//     * 3.2 基金详情接口（基本信息）
//     * @param fundId 基金Id（上送fundId为内部唯一标识，不是给客户展示的基金code）,后来修改为等同于code
//     */
//    private void getQueryFundBasicDetail(String fundId){
//        QueryFundBasicDetailRequestParams requestParams = new QueryFundBasicDetailRequestParams(fundId);
//        HttpHelp h= new HttpHelp();
//        h.postHttpFromSF(this,requestParams);
//        h.setHttpErrorCallBack(null);
//        h.setHttpResponseCallBack(new IHttpResponseCallBack() {
//            @Override
//            public boolean responseCallBack(String response, Object extendParams) {
//                QueryFundBasicDetailResponseData data = GsonTools.fromJson(response,QueryFundBasicDetailResponseData.class);
//                QueryFundBasicDetailResult body = data.getBody();
//                fincControl.fundDetails.put("siFangFundDetail",body);
//                List<QueryFundBasicDetailResult.FeeRatioListItem> itemList = body.getFeeRatioList();
//                if (isLogin) {
//                    balanceListFlag = 2 ;
//                    BaseHttpEngine.showProgressDialog();
//                    attentionFundQuery();
//                } else {
//                    BaseHttpEngine.showProgressDialog();
//                    if("自选".equals(selectType)){//后来修改为登录前没有自选，理论上不会执行if条件里面的语句
//                        String fundCode = (String) fincControl.fundDetails.get(Finc.FINC_FUNDCODE);
//                        //通过基金代码 获取到基金的基本信息
//                        getFincFund(fundCode);
//                    }else{
//                        BaseHttpEngine.dissMissProgressDialog();
//                        intentFlag = 0;//跳转之前重置为0，返回时刷新页面使用
//                        BaseHttpEngine.dissMissProgressDialog();
//                        ActivityIntentTools.intentToActivity(FundPricesActivity.this,FincProductDetailActivity.class);
//                    }
//                }
//                return false;
//            }
//        });
//    }

    /**
     * 封装筛选数据
     */
    private List<SelectTypeData> buildSelectData() {
        String[] name = {"交易币种", "风险级别", "基金状态"};
        String[] value0 = {"全部", "人民币元", "英镑", "港币","美元", "日元", "欧元"};
        String[] value1 = {"全部", "低风险", "中低风险", "中风险", "中高风险", "高风险"};
        String[] value2 = {"全部", "正常开放", "可认购", "暂停交易", "暂停申购", "暂停赎回"};
        String[] id0 = {"000", "001", "012", "013", "014", "027","038"};
        String[] id1 = {"0", "1", "2", "3", "4", "5"};
        String[] id2 = {"00", "0", "1", "4", "5", "6"};
        List<String[]> contentList = new ArrayList<>();
        contentList.add(value0);
        contentList.add(value1);
        contentList.add(value2);
 //       contentList.add(value3);

        List<SelectTypeData> list = new ArrayList<>();
        for (int i = 0; i < name.length; i++) {
            SelectTypeData item = new SelectTypeData();
            List<Content> itemList = new ArrayList<>();
            String[] tempValues = contentList.get(i);
            for (int j = 0; j < tempValues.length; j++) {
                Content content = new Content();
                content.setName(tempValues[j]);
                if(i == 0){//币种
                    content.setContentNameID(id0[j]);
                    item.setDefaultId(id0[1]);
                    if(j==1){
                        content.setSelected(true);
                    }
                }
                if(i == 1){//风险级别
                    item.setDefaultId(id1[0]);
                    content.setContentNameID(id1[j]);
                    if(j==0){
                        content.setSelected(true);
                    }
                }
                if(i == 2){//基金状态
                    item.setDefaultId(id2[1]);
                    content.setContentNameID(id2[j]);
                    if(j==1){
                        content.setSelected(true);
                    }
                }
                itemList.add(content);
            }
            item.setTitle(name[i]);
            item.setList(itemList);
            list.add(item);
        }
        return list;
    }

    /**
     * 列表点击事件
     */
    class ListItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            FincControl.getInstance().fundListSelect= combinateList.get(i);
            FincControl.getInstance().fundDetails = combinateList.get(i);
            FincControl.getInstance().setAttentionFlag(false);
            //先请求BII详情数据，然后再请求四方详情数据
            if(isLogin){
                BaseHttpEngine.showProgressDialog();
                getFincFund((String)combinateList.get(i).get("fundCode"));
            }else{
                BaseHttpEngine.showProgressDialog();
                getPsnFundDetailQueryOutlay((String)combinateList.get(i).get("fundCode"));
            }
//            if(!StringUtil.isNullOrEmpty(fincControl.fincList)){
//                fincControl.item = fincControl.fincList.get(i);
//                getQueryFundBasicDetail((String)fincControl.fundDetails.get("fundCode"));
//            }

        }
    }

    /**
     * 登录前详情回调
     * @param resultObj
     */
    @Override
    public void getPsnFundDetailQueryOutlayCallback(Object resultObj) {
        super.getPsnFundDetailQueryOutlayCallback(resultObj);
//        BaseHttpEngine.dissMissProgressDialog();
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
                .getResult();
        if (StringUtil.isNullOrEmpty(resultMap)) {
            return;
        }
        fincControl.fundDetails = resultMap;
        //请求四方数据
 //       getQueryFundBasicDetail((String)fincControl.fundDetails.get("fundCode"));
//        BaseHttpEngine.showProgressDialog();
//        if("自选".equals(selectType)){//后来修改为登录前没有自选，理论上不会执行if条件里面的语句
//            String fundCode = (String) fincControl.fundDetails.get(Finc.FINC_FUNDCODE);
//            //通过基金代码 获取到基金的基本信息
//            getFincFund(fundCode);
//        }else{
//            BaseHttpEngine.dissMissProgressDialog();
            intentFlag = 0;//跳转之前重置为0，返回时刷新页面使用
            BaseHttpEngine.dissMissProgressDialog();
            if(StringUtil.isNullOrEmpty(fincControl.fundDetails)){
                Map map = new HashMap();
                map.put("selectType",selectType);
                ActivityIntentTools.intentToActivityWithData(FundPricesActivity.this,FincProductDetailActivity.class,map);
            }else{
                ActivityIntentTools.intentToActivity(FundPricesActivity.this,FincProductDetailActivity.class);
            }
//        }
    }

    /**
     * 基金基本信息查询  回调处理
     */
    @Override
    public void getFincFundCallback(Object resultObj) {
        super.getFincFundCallback(resultObj);
        BaseHttpEngine.dissMissProgressDialog();
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//        fincControl.fincFundDetails= (Map<String, Object>) biiResponseBody
//                .getResult();
//        intentFlag = 0;//跳转之前重置为0，返回时刷新页面使用
//        if("自选".equals(selectType)){
//            Intent intent = new Intent(FundPricesActivity.this,
//                    FincProductDetailActivity.class);
//            intent.putExtra(Finc.I_ATTENTIONFLAG, FincFundDetailActivity.ATTENTION);
//            FincControl.getInstance().isAttentionFlag = true;
//            FincControl.getInstance().setAttentionFlag(true);
//            startActivity(intent);
//        }else{
            fincControl.fundDetails = (Map<String, Object>) biiResponseBody.getResult();
            //请求四方详情数据 修改为进入页面后再请求
//            getQueryFundBasicDetail((String)fincControl.fundDetails.get("fundCode"));
            balanceListFlag = 2 ;
            BaseHttpEngine.showProgressDialog();
            attentionFundQuery();
//        }
    }
    @Override
    public void attentionFundQueryCallback(Object resultObj) {
        super.attentionFundQueryCallback(resultObj);
 //       BaseHttpEngine.dissMissProgressDialog();
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
                .getResult();
        if (!StringUtil.isNullOrEmpty(resultMap)) {
            if (!StringUtil.isNullOrEmpty(resultMap
                    .get(Finc.FINC_ATTENTIONQUERYLIST_ATTENTIONLIST))) {
                FincControl.getInstance().attentionFundList = (List<Map<String, Object>>) resultMap
                        .get(Finc.FINC_ATTENTIONQUERYLIST_ATTENTIONLIST);
                for (Map<String, Object> map : FincControl.getInstance().attentionFundList) {
                    // 已关注的基金
                    String fundCodeAttentioned = (String) map.get(Finc.FINC_FUNDCODE);
                    // 当前选择的基金
                    String fundCodeCurrent = (String) fincControl.fundDetails
                            .get(Finc.FINC_FUNDCODE);
                    if (fundCodeAttentioned.equals(fundCodeCurrent)) {// 如果已关注
                        FincControl.getInstance().setAttentionFlag(true);
                    }
                }
            }
        }
        if (StringUtil.isNullOrEmpty(fincControl.attentionFundList)) {
            FincControl.getInstance().setAttentionCount(0);
        } else {
            FincControl.getInstance().setAttentionCount(
                    fincControl.attentionFundList.size());
        }
        requestPsnFundQueryFundBalance(StringUtil.valueOf1((String) fincControl.fundDetails
                .get(Finc.FINC_FUNDCODE)));
        balanceListFlag = 2 ;
    }

    @Override
    public void requestPsnFundQueryFundBalanceCallback(Object resultObj) {
        super.requestPsnFundQueryFundBalanceCallback(resultObj);
        if(!fincControl.ifhaveaccId || !fincControl.ifInvestMent) return;
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        /**
         * PsnFundQueryFund查询基金持仓信息
         */
        Map<String, Object> result = (Map<String, Object>) biiResponseBody
                .getResult();
        if (result == null || StringUtil.isNullOrEmpty(((List<Map<String,Object>>) result
                .get(Finc.FINC_FUNDBALANCE_REQ))) == true) {
            fincControl.fundBalancList = null;
        } else {
            fincControl.fundBalancList = (List<Map<String, Object>>) result
                    .get(Finc.FINC_FUNDBALANCE_REQ);
        }

        if(balanceListFlag == 2){
//            if("自选".equals(selectType)){
//                String fundCode = (String) fincControl.fundDetails.get(Finc.FINC_FUNDCODE);
//                //通过基金代码 获取到基金的基本信息
//                getFincFund(fundCode);
//            }else{
//                BaseHttpEngine.dissMissProgressDialog();
            if(!StringUtil.isNullOrEmpty(fincControl.fundBalancList)){//不为空时进行浮动盈亏试算
                Map<String, Object> fundBalancList_new = (Map<String, Object>) fincControl.fundBalancList.get(0)
                        .get(Finc.FINC_FUNDINFO_REQ);
                String isFdyk=(String) fundBalancList_new.get("isFdyk");
                if(isFdyk.equals("Y")){//
                    requestSystemDateTime();
                }else{
                    intentFlag = 0;//跳转之前重置为0，返回时刷新页面使用
                    BaseHttpEngine.dissMissProgressDialog();
                    ActivityIntentTools.intentToActivity(FundPricesActivity.this,FincProductDetailActivity.class);
                }
            }else{
                intentFlag = 0;//跳转之前重置为0，返回时刷新页面使用
                BaseHttpEngine.dissMissProgressDialog();
                ActivityIntentTools.intentToActivity(FundPricesActivity.this,FincProductDetailActivity.class);
            }
//            }
        }
        if(balanceListFlag == 1){
            if(!StringUtil.isNullOrEmpty(fincControl.fundBalancList)){//计算参考市值
                fundAmt = FincUtil.getTotalValueList(fincControl.fundBalancList);
                if(!StringUtil.isNullOrEmpty(fundAmt)){
                    finc_head.setAmountText(fundAmt);
                }else{
                    finc_head.setAmountText("0.00");
                }
            }else{
                if(!StringUtil.isNullOrEmpty(fundAmt)&&"--".equals(fundAmt)){
                    //finc_head.setAmountText(fundAmt);
                }else{
                    finc_head.setAmountText("0.00");
                }
            }
            fundBalancListAll = fincControl.fundBalancList;
            requestCommConversationId();
        }
    }

    /**
     * 系统时间回调
     * @param resultObj
     */
    @Override
    public void requestSystemDateTimeCallBack(Object resultObj) {
        super.requestSystemDateTimeCallBack(resultObj);
        String startDateStr = QueryDateUtils.getsysDateOneYear(dateTime);//获得一年前日期
        String endDateStr = QueryDateUtils.getFincLastDay(dateTime);//系统日期前一天
        // 系统当前时间格式化
        String currenttime = QueryDateUtils.getcurrentDate(dateTime);
        getFDYKList(StringUtil.valueOf1((String) fincControl.fundDetails
                .get(Finc.FINC_FUNDCODE)), startDateStr, endDateStr);
    }

    @Override
    public void getFDYKListCallback(Object resultObj) {
        super.getFDYKListCallback(resultObj);
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        List<Map<String, Object>> fdyklResultList = (List<Map<String, Object>>) biiResponseBody.getResult();
        String middleFloat = "";
        if(!StringUtil.isNullOrEmpty(fdyklResultList)){
            middleFloat = (String)fdyklResultList.get(0).get(Finc.FINC_FLOATPROFITANDLOSS_MIDDLEFLOAT);
            fincControl.fundDetails.put("middleFloat",middleFloat);
        }
        intentFlag = 0;//跳转之前重置为0，返回时刷新页面使用
        BaseHttpEngine.dissMissProgressDialog();
        if(StringUtil.isNullOrEmpty(fincControl.fundDetails)){
            Map map = new HashMap();
            map.put("selectType",selectType);
            ActivityIntentTools.intentToActivityWithData(FundPricesActivity.this,FincProductDetailActivity.class,map);
        }else{
            ActivityIntentTools.intentToActivity(FundPricesActivity.this,FincProductDetailActivity.class);
        }
    }

    /**
     * 设置菜单隐藏或展开
     * ture展开 false 收起
     */
    public void isItemVisible(boolean isVisibility){
        if(isVisibility){
            finc_more_item_img.setVisibility(View.GONE);
            finc_more_item_img_selected.setVisibility(View.VISIBLE);
            finc_list_layout.setVisibility(View.GONE);
            finc_item_second_layout.setVisibility(View.VISIBLE);
            finc_item_three_layout.setVisibility(View.VISIBLE);
        }else{
            finc_more_item_img.setVisibility(View.VISIBLE);
            finc_more_item_img_selected.setVisibility(View.GONE);
            finc_list_layout.setVisibility(View.VISIBLE);
            finc_item_second_layout.setVisibility(View.GONE);
            finc_item_three_layout.setVisibility(View.GONE);
        }
    }

    /**
     * 得到登录前或登陆后列表数据
     */
    private void getOutlayOrLoginData(){
        currentIndex = 0;
        combinateList.clear();
        currencyListAdapter = null;
        leftAdapter = null;
        rightAdapter = null;
        BaseHttpEngine.showProgressDialog();
        if (isLogin) {
            combainQueryFundInfos(currentIndex, pageSize, currencyCode,
                    fundCompanyCode, riskLevelStr, fundKindType, fundType,fundState,currSortFlag,current_sortfield);
        }else{
            combainQueryFundInfosOutlay(currentIndex, pageSize, currencyCode,
                    fundCompanyCode, riskLevelStr, fundKindType, fundType,fundState,currSortFlag,current_sortfield);
        }
    }

    /**
     * 点击事件
     */
    public class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.txt_query://交易查询
                    intentFlag = 1;
                    if (isLogin) {
                        ActivityIntentTools.intentToActivity(FundPricesActivity.this,FundqueryMenuActivity.class);
                    }else{
                        //跳转登陆
                        getLoginUtils(FundPricesActivity.this).exe(new LoginTask.LoginCallback() {
                            @Override
                            public void loginStatua(boolean b) {
//                                BaseHttpEngine.showProgressDialog();
                                //登陆成功后进入onResume()方法中检查任务并刷新页面在此不再作处理
//                                doCheckRequestPsnInvestmentManageIsOpen();
                            }
                        });
                    }
                    break;
                case R.id.txt_protocol://账户管理
                    intentFlag = 2;
                    if (isLogin) {
                        ActivityIntentTools.intentToActivity(FundPricesActivity.this,FincAccManagerMenuActivity.class);
                    }else{
                        //跳转登陆
                        getLoginUtils(FundPricesActivity.this).exe(new LoginTask.LoginCallback() {
                            @Override
                            public void loginStatua(boolean b) {
                                //登陆成功后进入onResume()方法中检查任务并刷新页面在此不再作处理
//                                BaseHttpEngine.showProgressDialog();
//                                doCheckRequestPsnInvestmentManageIsOpen();
                            }
                        });
                    }
                    break;
                case R.id.txt_cancel://撤单
                    intentFlag = 3;
                    if (isLogin) {
                        BaseHttpEngine.showProgressDialog();
                        requestCommConversationId();
                    }else{
                        //跳转登陆
                        getLoginUtils(FundPricesActivity.this).exe(new LoginTask.LoginCallback() {
                            @Override
                            public void loginStatua(boolean b) {
                                //登陆成功后进入onResume()方法中检查任务并刷新页面在此不再作处理
//                              BaseHttpEngine.showProgressDialog();
//                                doCheckRequestPsnInvestmentManageIsOpen();
//                                ActivityIntentTools.intentToActivity(FundPricesActivity.this,FundDQDEMenuActivity.class);
                            }
                        });
                    }
                    break;
                case R.id.finc_ib_back://返回
                    FundPricesActivity.this.finish();
                    break;
                case R.id.finc_ib_top_share_btn://分享

                    break;
                case R.id.finc_ib_top_right_btn://更多
                    intentFlag = 0;//跳转之前重置为0，返回时刷新页面使用
                    ActivityIntentTools.intentToActivity(FundPricesActivity.this,FincMenuMoreActivity.class);
                    break;
                case R.id.finc_all_layout://全部
                    if(all_img.getVisibility() == View.INVISIBLE){
                        clearListData();
                        allHiden();
                        FincUtil.setVisibilityAndTextColor(FundPricesActivity.this,all_tv,false,all_img,false);
                        FincUtil.setImageHeigthAndWidth(FundPricesActivity.this,ima_type_currency_line,50,3);
                        setSortImageGray("0");//显示排序按钮
                        selectType = "全部";
                        finc_type_currency_tv.setText("货币型");
                        setClickStyle(selectType);
                        fundKindType = "00";
                        fundType = "00";
                        setSortImageGray(null);
                        setSortFlag();
                        getOutlayOrLoginData();
                    }
                    break;
                case R.id.my_select_layout://自选
                    if(my_img.getVisibility() == View.INVISIBLE){
                        clearListData();
                        allHiden();
                        FincUtil.setVisibilityAndTextColor(FundPricesActivity.this,my_tv,false,my_img,false);
                        FincUtil.setImageHeigthAndWidth(FundPricesActivity.this,ima_type_currency_line,50,3);
                        setSortImageGray("1");//隐藏排序按钮
                        setSortFlag();
                        selectType = "自选";
                        finc_type_currency_tv.setText("货币型");
                        setClickStyle(selectType);
                        queryAttentionFundQuery();
                    }
                    break;
                case R.id.finc_typeStock_layout://股票型
                    if(ima_typeStock_line.getVisibility() == View.INVISIBLE){
                        clearListData();
                        allHiden();
                        FincUtil.setVisibilityAndTextColor(FundPricesActivity.this,finc_typeStock_tv,false,ima_typeStock_line,false);
                        FincUtil.setImageHeigthAndWidth(FundPricesActivity.this,ima_type_currency_line,50,3);
                        setSortImageGray("0");//显示排序按钮
                        selectType = "股票型";
                        finc_type_currency_tv.setText("货币型");
                        setClickStyle(selectType);
                        fundKindType = "01";
                        fundType = "07";
                        setSortImageGray(null);
                        setSortFlag();
                        getOutlayOrLoginData();
                    }
                    break;
                case R.id.finc_type_bond_layout://债券型
                    if(ima_type_bond_line.getVisibility() == View.INVISIBLE){
                        clearListData();
                        allHiden();
                        FincUtil.setVisibilityAndTextColor(FundPricesActivity.this,finc_type_bond_tv,false,ima_type_bond_line,false);
                        FincUtil.setImageHeigthAndWidth(FundPricesActivity.this,ima_type_currency_line,50,3);
                        setSortImageGray("0");//显示排序按钮
                        selectType = "债券型";
                        finc_type_currency_tv.setText("货币型");
                        setClickStyle(selectType);
                        fundKindType = "01";
                        fundType = "08";
                        setSortImageGray(null);
                        setSortFlag();
                        getOutlayOrLoginData();
                    }
                    break;
                case R.id.finc_type_currency_layout://货币
                    if(ima_type_currency_line.getVisibility() == View.INVISIBLE){
                        clearListData();
                        allHiden();
                        FincUtil.setVisibilityAndTextColor(FundPricesActivity.this,finc_type_currency_tv,false,ima_type_currency_line,false);
                        FincUtil.setImageHeigthAndWidth(FundPricesActivity.this,ima_type_currency_line,50,3);
                        selectType = "货币型";
                        finc_type_currency_tv.setText("货币型");
                        setClickStyle(selectType);
                        fundKindType = "01";
                        fundType = "06";
                        setSortImageGray(null);
                        setSortFlag();
                        getOutlayOrLoginData();
                    }
                    break;
                case R.id.finc_type_hunhe_layout://混合型
                    if(ima_fundinvest_hunhe_line.getVisibility() == View.GONE){
                        clearListData();
                        allHiden();
                        FincUtil.setVisibilityAndTextColor(FundPricesActivity.this,finc_type_currency_tv,false,ima_type_currency_line,false);
                        FincUtil.setImageHeigthAndWidth(FundPricesActivity.this,ima_type_currency_line,50,3);
                        setSortImageGray("0");//显示排序按钮
                        finc_type_currency_tv.setText("混合型");
                        selectType = "混合型";
                        setClickStyle(selectType);
                        fundKindType = "01";
                        fundType = "09";
                        setSortImageGray(null);
                        setSortFlag();
                        getOutlayOrLoginData();}
                    break;
                case R.id.finc_type_qd_layout://QDII
                    if(ima_fundinvest_qd_line.getVisibility() == View.GONE){
                        clearListData();
                        allHiden();
                        FincUtil.setVisibilityAndTextColor(FundPricesActivity.this,finc_type_currency_tv,false,ima_type_currency_line,false);
                        FincUtil.setImageHeigthAndWidth(FundPricesActivity.this,ima_type_currency_line,50,3);
                        setSortImageGray("0");//显示排序按钮
                        finc_type_currency_tv.setText("QDII");
                        selectType = "QDII";
                        setClickStyle(selectType);
                        fundKindType = "01";
                        fundType = "02";
                        setSortImageGray(null);
                        setSortFlag();
                        getOutlayOrLoginData();
                    }
                    break;
                case R.id.finc_type_baoben_layout://保本
                    if(ima_fundinvest_type_baoben_line.getVisibility() == View.GONE){
                        clearListData();
                        selectType = "保本型";
                        allHiden();
                        FincUtil.setVisibilityAndTextColor(FundPricesActivity.this,finc_type_currency_tv,false,ima_type_currency_line,false);
                        FincUtil.setImageHeigthAndWidth(FundPricesActivity.this,ima_type_currency_line,50,3);
                        setSortImageGray("0");//显示排序按钮
                        finc_type_currency_tv.setText("保本型");
                        setClickStyle(selectType);
                        fundKindType = "01";
                        fundType = "04";
                        setSortImageGray(null);
                        setSortFlag();
                        getOutlayOrLoginData();
                    }
                    break;
                case R.id.finc_type_zhishu_layout://指数型
                    if(ima_fundinvest_type_zhishu_line.getVisibility() == View.GONE){
                        clearListData();
                        selectType = "指数型";
                        allHiden();
                        FincUtil.setVisibilityAndTextColor(FundPricesActivity.this,finc_type_currency_tv,false,ima_type_currency_line,false);
                        FincUtil.setImageHeigthAndWidth(FundPricesActivity.this,ima_type_currency_line,50,3);
                        setSortImageGray("0");//显示排序按钮
                        finc_type_currency_tv.setText("指数型");
                        setClickStyle(selectType);
                        fundKindType = "01";
                        fundType = "05";
                        setSortImageGray(null);
                        setSortFlag();
                        getOutlayOrLoginData();
                    }
                    break;
                case R.id.finc_type_licai_layout://理财型
                    if(ima_fundinvest_type_licai_line.getVisibility() == View.GONE){
                        clearListData();
                        selectType = "理财型";
                        allHiden();
                        FincUtil.setVisibilityAndTextColor(FundPricesActivity.this,finc_type_currency_tv,false,ima_type_currency_line,false);
                        FincUtil.setImageHeigthAndWidth(FundPricesActivity.this,ima_type_currency_line,50,3);
                        setSortImageGray("0");//显示排序按钮
                        finc_type_currency_tv.setText("理财型");
                        setClickStyle(selectType);
                        fundKindType = "01";
                        fundType = "01";
                        setSortImageGray(null);
                        setSortFlag();
                        getOutlayOrLoginData();
                    }
                    break;
                case R.id.finc_type_etf_layout://ETF
                    if(ima_fundinvest_etf_line.getVisibility() == View.GONE){
                        clearListData();
                        selectType = "ETF";
                        allHiden();
                        FincUtil.setVisibilityAndTextColor(FundPricesActivity.this,finc_type_currency_tv,false,ima_type_currency_line,false);
                        FincUtil.setImageHeigthAndWidth(FundPricesActivity.this,ima_type_currency_line,50,3);
                        setSortImageGray("0");//显示排序按钮
                        finc_type_currency_tv.setText("ETF");
                        setClickStyle(selectType);
                        fundKindType = "01";
                        fundType = "03";
                        setSortImageGray(null);
                        setSortFlag();
                        getOutlayOrLoginData();}
                    break;
                case R.id.finc_type_zq_layout://资管计划产品
                    if(ima_fundinvest_type_zg_line.getVisibility() == View.GONE){
                        clearListData();
                        selectType = "资管计划产品";
                        allHiden();
                        FincUtil.setVisibilityAndTextColor(FundPricesActivity.this,finc_type_currency_tv,false,ima_type_currency_line,false);
                        FincUtil.setImageHeigthAndWidth(FundPricesActivity.this,ima_type_currency_line,100,3);
                        setSortImageGray("0");//显示排序按钮
                        setSortImageGray("2");//隐藏排序按钮
                        finc_type_currency_tv.setText("资管计划产品");
                        setClickStyle(selectType);
                        fundKindType = "07";
                        fundType = "00";
                        setSortImageGray(null);
                        setSortFlag();
                        getOutlayOrLoginData();
                    }
                    break;
                case R.id.finc_type_xintuo_layout://信托产品
                    if(ima_fundinvest_type_xintuo_line.getVisibility() == View.GONE){
                        clearListData();
                        selectType = "信托产品";
                        allHiden();
                        FincUtil.setVisibilityAndTextColor(FundPricesActivity.this,finc_type_currency_tv,false,ima_type_currency_line,false);
                        FincUtil.setImageHeigthAndWidth(FundPricesActivity.this,ima_type_currency_line,80,3);
                        setSortImageGray("0");//显示排序按钮
                        setSortImageGray("2");//隐藏排序按钮
                        finc_type_currency_tv.setText("信托产品");
                        setClickStyle(selectType);
                        fundKindType = "04";
                        fundType = "00";
                        setSortImageGray(null);
                        setSortFlag();
                        getOutlayOrLoginData();
                    }
                    break;
                case R.id.finc_type_other_layout://其他
                    if(ima_fundinvest_type_other_line.getVisibility() == View.GONE){
                        clearListData();
                        selectType = "其他";
                        allHiden();
                        FincUtil.setVisibilityAndTextColor(FundPricesActivity.this,finc_type_currency_tv,false,ima_type_currency_line,false);
                        FincUtil.setImageHeigthAndWidth(FundPricesActivity.this,ima_type_currency_line,50,3);
                        setSortImageGray("0");//显示排序按钮
                        finc_type_currency_tv.setText("其他");
                        setClickStyle(selectType);
                        fundKindType = "01";
                        fundType = "10";
                        setSortImageGray(null);
                        setSortFlag();
                        getOutlayOrLoginData();
                    }
                    break;
            }
        }
    }

    /**
     * 设置显示的布局
     * @param selectType 选中项
     */
    private void setClickStyle(String selectType){
        isItemVisible(false);
        currencyListAdapter = null;
        leftAdapter = null;
        rightAdapter = null;
        if("货币型".equals(selectType)||"理财型".equals(selectType)){
            finc_list_layout.setVisibility(View.GONE);
            currency_layout.setVisibility(View.VISIBLE);
        }else{
            finc_list_layout.setVisibility(View.VISIBLE);
            currency_layout.setVisibility(View.GONE);
        }

    }
    /** 查询关注的基金 */
    public void queryAttentionFundQuery() {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Finc.FINC_ATTENTIONQUERYLIST);
        biiRequestBody.setConversationId(null);
        biiRequestBody.setParams(null);
        BaseHttpEngine.showProgressDialog();
        HttpManager.requestBii(biiRequestBody, this,
                "queryAttentionFundQueryCallback");
    }

    public void queryAttentionFundQueryCallback(Object resultObj) {
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
                .getResult();
//        if (StringUtil.isNullOrEmpty(resultMap)) {
//            BiiHttpEngine.dissMissProgressDialog();
//            BaseDroidApp.getInstanse().showInfoMessageDialog(
//                    getString(R.string.acc_transferquery_null));
//            return;
//        }
        combinateList.clear();
        combinateList.addAll((List<Map<String, Object>>) resultMap
                .get(Finc.FINC_ATTENTIONQUERYLIST_ATTENTIONLIST));
        BiiHttpEngine.dissMissProgressDialog();
//        if (StringUtil.isNullOrEmpty(combinateList)) {
//            BiiHttpEngine.dissMissProgressDialog();
//            BaseDroidApp.getInstanse().showInfoMessageDialog(
//                    getString(R.string.acc_transferquery_null));
//        }
        leftAdapter = new CommonAdapter<Map<String, Object>>(this,combinateList,myLeftAdapter);
        rightAdapter = new CommonAdapter<Map<String, Object>>(this,combinateList,myRightAdapter);
        leftListView.setAdapter(leftAdapter);
        rightListView.setAdapter(rightAdapter);
    }

    /**
     * 下标全部重置为隐藏
     */
    public void allHiden(){
        all_img.setVisibility(View.INVISIBLE);
        my_img.setVisibility(View.INVISIBLE);
        ima_typeStock_line.setVisibility(View.INVISIBLE);
        ima_type_bond_line.setVisibility(View.INVISIBLE);
        ima_type_currency_line.setVisibility(View.INVISIBLE);

        ima_fundinvest_hunhe_line.setVisibility(View.GONE);
        ima_fundinvest_qd_line.setVisibility(View.GONE);
        ima_fundinvest_type_baoben_line.setVisibility(View.GONE);
        ima_fundinvest_type_zhishu_line.setVisibility(View.GONE);
        ima_fundinvest_type_licai_line.setVisibility(View.GONE);

        ima_fundinvest_etf_line.setVisibility(View.GONE);
        ima_fundinvest_type_zg_line.setVisibility(View.GONE);
        ima_fundinvest_type_xintuo_line.setVisibility(View.GONE);
        ima_fundinvest_type_other_line.setVisibility(View.GONE);

        all_tv.setTextColor(getResources().getColor(R.color.fonts_dark_gray));
        my_tv.setTextColor(getResources().getColor(R.color.fonts_dark_gray));
        finc_typeStock_tv.setTextColor(getResources().getColor(R.color.fonts_dark_gray));
        finc_type_bond_tv.setTextColor(getResources().getColor(R.color.fonts_dark_gray));
        finc_type_currency_tv.setTextColor(getResources().getColor(R.color.fonts_dark_gray));
        finc_type_hunhe_tv.setTextColor(getResources().getColor(R.color.fonts_dark_gray));
        finc_type_qd_tv.setTextColor(getResources().getColor(R.color.fonts_dark_gray));
        finc_type_baoben_tv.setTextColor(getResources().getColor(R.color.fonts_dark_gray));
        finc_type_zhishu_tv.setTextColor(getResources().getColor(R.color.fonts_dark_gray));
        finc_type_licai_tv.setTextColor(getResources().getColor(R.color.fonts_dark_gray));
        finc_type_etf_tv.setTextColor(getResources().getColor(R.color.fonts_dark_gray));
        finc_type_zq_tv.setTextColor(getResources().getColor(R.color.fonts_dark_gray));
        finc_type_xintuo_tv.setTextColor(getResources().getColor(R.color.fonts_dark_gray));
        finc_type_other_tv.setTextColor(getResources().getColor(R.color.fonts_dark_gray));
    }
    private TextView all_tv,my_tv,finc_typeStock_tv,finc_type_bond_tv,finc_type_currency_tv,
            finc_type_hunhe_tv,finc_type_qd_tv,finc_type_baoben_tv,finc_type_zhishu_tv,finc_type_licai_tv
            ,finc_type_etf_tv,finc_type_zq_tv,finc_type_xintuo_tv,finc_type_other_tv;
    private ImageView all_img,my_img,ima_typeStock_line,ima_type_bond_line,ima_type_currency_line,
            ima_fundinvest_hunhe_line,ima_fundinvest_qd_line,ima_fundinvest_type_baoben_line,ima_fundinvest_type_zhishu_line,ima_fundinvest_type_licai_line
            ,ima_fundinvest_etf_line,ima_fundinvest_type_zg_line,ima_fundinvest_type_xintuo_line,ima_fundinvest_type_other_line;

    /**
     * 初始化类型选项
     */
    private void initItem(){
        my_select_layout = (LinearLayout) findViewById(R.id.my_select_layout);
        if(isLogin){
            my_select_layout.setVisibility(View.VISIBLE);
        }
        finc_more_item_img = (ImageView) findViewById(R.id.finc_more_item_img);
        finc_more_item_img_selected = (ImageView) findViewById(R.id.finc_more_item_img_selected);
        finc_list_layout = (LinearLayout) findViewById(R.id.finc_list_layout);
        finc_item_second_layout = (LinearLayout) findViewById(R.id.finc_item_second_layout);
        finc_item_three_layout = (LinearLayout) findViewById(R.id.finc_item_three_layout);
        //全部
        all_tv = (TextView) findViewById(R.id.finc_all_tv);
        all_img = (ImageView) findViewById(R.id.ima_all_line);
        findViewById(R.id.finc_all_layout).setOnClickListener(new MyOnClickListener());
        //自选
        my_tv = (TextView) findViewById(R.id.finc_my_tv);
        my_img = (ImageView) findViewById(R.id.ima_my_line);
        findViewById(R.id.my_select_layout).setOnClickListener(new MyOnClickListener());
        //股票型
        finc_typeStock_tv = (TextView) findViewById(R.id.finc_typeStock_tv);
        ima_typeStock_line = (ImageView) findViewById(R.id.ima_typeStock_line);
        findViewById(R.id.finc_typeStock_layout).setOnClickListener(new MyOnClickListener());
        //债券型
        finc_type_bond_tv = (TextView) findViewById(R.id.finc_type_bond_tv);
        ima_type_bond_line = (ImageView) findViewById(R.id.ima_type_bond_line);
        findViewById(R.id.finc_type_bond_layout).setOnClickListener(new MyOnClickListener());
        //货币型
        finc_type_currency_tv = (TextView) findViewById(R.id.finc_type_currency_tv);
        ima_type_currency_line = (ImageView) findViewById(R.id.ima_type_currency_line);
        findViewById(R.id.finc_type_currency_layout).setOnClickListener(new MyOnClickListener());
        //混合型
        finc_type_hunhe_tv = (TextView) findViewById(R.id.finc_type_hunhe_tv);
        ima_fundinvest_hunhe_line = (ImageView) findViewById(R.id.ima_fundinvest_hunhe_line);
        findViewById(R.id.finc_type_hunhe_layout).setOnClickListener(new MyOnClickListener());
        //QDII
        finc_type_qd_tv = (TextView) findViewById(R.id.finc_type_qd_tv);
        ima_fundinvest_qd_line = (ImageView) findViewById(R.id.ima_fundinvest_qd_line);
        findViewById(R.id.finc_type_qd_layout).setOnClickListener(new MyOnClickListener());
        //保本型
        finc_type_baoben_tv = (TextView) findViewById(R.id.finc_type_baoben_tv);
        ima_fundinvest_type_baoben_line = (ImageView) findViewById(R.id.ima_fundinvest_type_baoben_line);
        findViewById(R.id.finc_type_baoben_layout).setOnClickListener(new MyOnClickListener());
        //指数型
        finc_type_zhishu_tv = (TextView) findViewById(R.id.finc_type_zhishu_tv);
        ima_fundinvest_type_zhishu_line = (ImageView) findViewById(R.id.ima_fundinvest_type_zhishu_line);
        findViewById(R.id.finc_type_zhishu_layout).setOnClickListener(new MyOnClickListener());
        //理财型
        finc_type_licai_tv = (TextView) findViewById(R.id.finc_type_licai_tv);
        ima_fundinvest_type_licai_line = (ImageView) findViewById(R.id.ima_fundinvest_type_licai_line);
        findViewById(R.id.finc_type_licai_layout).setOnClickListener(new MyOnClickListener());

        //ETF
        finc_type_etf_tv = (TextView) findViewById(R.id.finc_type_etf_tv);
        ima_fundinvest_etf_line = (ImageView) findViewById(R.id.ima_fundinvest_etf_line);
        findViewById(R.id.finc_type_etf_layout).setOnClickListener(new MyOnClickListener());
        //资管计划产品
        finc_type_zq_tv = (TextView) findViewById(R.id.finc_type_zq_tv);
        ima_fundinvest_type_zg_line = (ImageView) findViewById(R.id.ima_fundinvest_type_zg_line);
        findViewById(R.id.finc_type_zq_layout).setOnClickListener(new MyOnClickListener());
        //信托产品
        finc_type_xintuo_tv = (TextView) findViewById(R.id.finc_type_xintuo_tv);
        ima_fundinvest_type_xintuo_line = (ImageView) findViewById(R.id.ima_fundinvest_type_xintuo_line);
        findViewById(R.id.finc_type_xintuo_layout).setOnClickListener(new MyOnClickListener());
        //其他
        finc_type_other_tv = (TextView) findViewById(R.id.finc_type_other_tv);
        ima_fundinvest_type_other_line = (ImageView) findViewById(R.id.ima_fundinvest_type_other_line);
        findViewById(R.id.finc_type_other_layout).setOnClickListener(new MyOnClickListener());
        //点击箭头展现更多item
        finc_more_item_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isItemVisible(true);
                if(!"货币型".equals(finc_type_currency_tv.getText().toString())&&!"理财型".equals(finc_type_currency_tv.getText().toString())){
                    allHiden();
                    switch(selectType){
                        case "全部":
                            FincUtil.setVisibilityAndTextColor(FundPricesActivity.this,all_tv,false,all_img,false);
                            break;
                        case "自选":
                            FincUtil.setVisibilityAndTextColor(FundPricesActivity.this,my_tv,false,my_img,false);
                            break;
                        case "股票型":
                            FincUtil.setVisibilityAndTextColor(FundPricesActivity.this,finc_typeStock_tv,false,ima_typeStock_line,false);
                            break;
                        case "债券型":
                            FincUtil.setVisibilityAndTextColor(FundPricesActivity.this,finc_type_bond_tv,false,ima_type_bond_line,false);
                            break;
                        case "混合型":
                            FincUtil.setVisibilityAndTextColor(FundPricesActivity.this,finc_type_hunhe_tv,false,ima_fundinvest_hunhe_line,false);
                            break;
                        case "QDII":
                            FincUtil.setVisibilityAndTextColor(FundPricesActivity.this,finc_type_qd_tv,false,ima_fundinvest_qd_line,false);
                            break;
                        case "保本型":
                            FincUtil.setVisibilityAndTextColor(FundPricesActivity.this,finc_type_baoben_tv,false,ima_fundinvest_type_baoben_line,false);
                            break;
                        case "指数型":
                            FincUtil.setVisibilityAndTextColor(FundPricesActivity.this,finc_type_zhishu_tv,false,ima_fundinvest_type_zhishu_line,false);
                            break;
                        case "理财型":
                            FincUtil.setVisibilityAndTextColor(FundPricesActivity.this,finc_type_licai_tv,false,ima_fundinvest_type_licai_line,false);
                            break;
                        case "ETF":
                            FincUtil.setVisibilityAndTextColor(FundPricesActivity.this,finc_type_etf_tv,false,ima_fundinvest_etf_line,false);
                            break;
                        case "资管计划产品":
                            FincUtil.setVisibilityAndTextColor(FundPricesActivity.this,finc_type_zq_tv,false,ima_fundinvest_type_zg_line,false);
                            break;
                        case "信托产品":
                            FincUtil.setVisibilityAndTextColor(FundPricesActivity.this,finc_type_xintuo_tv,false,ima_fundinvest_type_xintuo_line,false);
                            break;
                        case "其他":
                            FincUtil.setVisibilityAndTextColor(FundPricesActivity.this,finc_type_other_tv,false,ima_fundinvest_type_other_line,false);
                            break;
                    }
                    finc_type_currency_tv.setText("货币型");
                }else{
                    if("理财型".equals(finc_type_currency_tv.getText().toString())){
                        allHiden();
                        FincUtil.setVisibilityAndTextColor(FundPricesActivity.this,finc_type_licai_tv,false,ima_fundinvest_type_licai_line,false);
                        finc_type_currency_tv.setText("货币型");
                    }
                    finc_list_layout.setVisibility(View.GONE);
                    currency_layout.setVisibility(View.GONE);
                }
            }
        });
        finc_more_item_img_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isItemVisible(false);
                if(!StringUtil.isNullOrEmpty(selectType)&&!"全部".equals(selectType)&&!"自选".equals(selectType)
                        &&!"股票型".equals(selectType)&&!"债券型".equals(selectType)){
                    finc_type_currency_tv.setText(selectType);
                    FincUtil.setVisibilityAndTextColor(FundPricesActivity.this,finc_type_currency_tv,false,ima_type_currency_line,false);
                    if("货币型".equals(selectType)||"理财型".equals(selectType)){
                        finc_list_layout.setVisibility(View.GONE);
                        currency_layout.setVisibility(View.VISIBLE);
                    }
                }

            }
        });
    }

    /***
     * 基金公司
     */
    private void initFundCompany(){
        FundCompany fundCompany = new FundCompany();
        fundCompany.setFundCompanyName("中银基金管理有限公司");
        fundCompany.setFundCompanyCode("50400000");
        fundCompany.setAlpha("推荐");
        fundCompany.setChecked(false);
        companyList.add(0,fundCompany);

        fundCompany = new FundCompany();
        fundCompany.setFundCompanyName("中银国际证券有限责任公司");
        fundCompany.setFundCompanyCode("13190000");
        fundCompany.setAlpha("推荐");
        fundCompany.setChecked(false);
        companyList.add(1,fundCompany);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 101:
                if (resultCode == RESULT_OK) {
                    fundCompanyPos = data.getIntExtra("fundCompanyPos", -1);
                    if (fundCompanyPos >= 0) {
                        fundCompanySpinner.setText(companyList.get(fundCompanyPos).getFundCompanyName());
                        fundCompanyCode = companyList.get(fundCompanyPos).getFundCompanyCode();
                    } else {
                        fundCompanySpinner.setText("全部");
                        fundCompanyCode = "";
                    }
                    fundCompanySpinner.postInvalidate();
                }
                break;
            case ConstantGloble.ACTIVITY_REQUEST_MANAGE_CODE:// 开通理财服务
                switch (resultCode) {
                    case RESULT_OK:
                        fincControl.ifInvestMent = true;
                        if (!fincControl.ifhaveaccId) {// 如果还没有基金账户
                            getPopup();
                        }else{
                            BaseHttpEngine.showProgressDialog();
                            getFundCompanyList();
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
                            BaseHttpEngine.showProgressDialog();
                            getFundCompanyList();
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

    private ICommonAdapter<Map<String,Object>> myLeftAdapter = new ICommonAdapter<Map<String,Object>>(){

        @Override
        public View getView(int i, final Map<String, Object> currentItem, LayoutInflater layoutInflater, View convertView, ViewGroup viewGroup) {
            ViewHold viewHold;
            if (convertView==null) {
                viewHold=new ViewHold();
                convertView=layoutInflater.inflate(R.layout.finc_item_layout_p606, null);
                viewHold.left_item_layout = (LinearLayout) convertView.findViewById(R.id.left_item_layout);
                viewHold.right_item_layout = (LinearLayout) convertView.findViewById(R.id.right_item_layout);
                viewHold.left_item_layout.setVisibility(View.GONE);
                viewHold.right_item_layout.setVisibility(View.VISIBLE);
                viewHold.leftTextview=(TextView) convertView.findViewById(R.id.left_container_textview0);
                viewHold.finc_code=(TextView) convertView.findViewById(R.id.finc_code);
                convertView.setTag(viewHold);
            }else {
                viewHold=(ViewHold) convertView.getTag();
            }
            viewHold.left_item_layout.setVisibility(View.VISIBLE);
            viewHold.right_item_layout.setVisibility(View.GONE);
            if("自选".equals(selectType)){
                viewHold.leftTextview.setText((String) currentItem.get(Finc.FINC_ATTENTIONQUERYLIST_FUNDSHORTNAME));
            }else{
                viewHold.leftTextview.setText((String) currentItem.get(Finc.FINC_FUNDNAME));
            }
            viewHold.finc_code.setText((String) currentItem.get(Finc.FINC_FUNDCODE));
            return convertView;
        }
    };
    private ICommonAdapter<Map<String,Object>> myRightAdapter = new ICommonAdapter<Map<String,Object>>(){

        @Override
        public View getView(int i, final Map<String, Object> currentItem, LayoutInflater layoutInflater, View convertView, ViewGroup viewGroup) {
            ViewHold viewHold;
            if (convertView==null) {
                viewHold=new ViewHold();
                convertView=layoutInflater.inflate(R.layout.finc_item_layout_p606, null);
                viewHold.left_item_layout = (LinearLayout) convertView.findViewById(R.id.left_item_layout);
                viewHold.right_item_layout = (LinearLayout) convertView.findViewById(R.id.right_item_layout);
                viewHold.left_item_layout.setVisibility(View.GONE);
                viewHold.right_item_layout.setVisibility(View.VISIBLE);
                viewHold.textView1=(TextView) convertView.findViewById(R.id.item_title_tv2);
                viewHold.tv_date=(TextView) convertView.findViewById(R.id.tv_date);
                viewHold.textView2=(TextView) convertView.findViewById(R.id.item_title_tv3);
                viewHold.textView3=(TextView) convertView.findViewById(R.id.item_title_tv4);
                viewHold.textView4=(TextView) convertView.findViewById(R.id.item_title_tv5);
                viewHold.textView5=(TextView) convertView.findViewById(R.id.item_title_tv6);
                viewHold.textView6=(TextView) convertView.findViewById(R.id.item_title_tv7);
                viewHold.textView7=(TextView) convertView.findViewById(R.id.item_title_tv8);
                convertView.setTag(viewHold);
            }else {
                viewHold=(ViewHold) convertView.getTag();
            }
            viewHold.left_item_layout.setVisibility(View.GONE);
            viewHold.right_item_layout.setVisibility(View.VISIBLE);
            if("自选".equals(selectType)){
                viewHold.textView1.setText(StringUtil.isNullOrEmpty((String) currentItem.get("netprice"))?"--":StringUtil.append2Decimals((String) currentItem.get("netprice"),4));
                String netValEndDate = FincUtil.getDateFormat((String) currentItem.get("netValEndDate"));
                if(!StringUtil.isNullOrEmpty(netValEndDate)&&netValEndDate.length()== 10){
                    viewHold.tv_date.setText(netValEndDate.substring(5,10));
                }else{
                    viewHold.tv_date.setText(StringUtil.isNullOrEmpty(netValEndDate)?"--":netValEndDate);
                }
            }else{
//                if("全部".equals(selectType)&&!StringUtil.isNullOrEmpty(currentItem.get("fntype"))
//                        &&("06".equals((String)currentItem.get("fntype"))||"01".equals((String)currentItem.get("fntype")))){//货币型基金单位净值固定显示1.0000
//                    viewHold.textView1.setText("1.0000");
//                }else{
                    viewHold.textView1.setText(StringUtil.isNullOrEmpty((String) currentItem.get(Finc.FINC_NETPRICE))?"--":StringUtil.append2Decimals((String) currentItem.get(Finc.FINC_NETPRICE),4));
//                }
                String endDate = FincUtil.getDateFormat((String) currentItem.get("endDate"));
                if(!StringUtil.isNullOrEmpty(endDate)&&endDate.length()== 10){
                    viewHold.tv_date.setText(endDate.substring(5,10));
                }else{
                    viewHold.tv_date.setText(StringUtil.isNullOrEmpty(endDate)?"--":endDate);
                }
            }
            String currPercentDiff = (String) currentItem.get("currPercentDiff");
            viewHold.textView2.setTextColor(getResources().getColor(R.color.fonts_black));
            if(!StringUtil.isNullOrEmpty(currPercentDiff)&&Double.parseDouble(currPercentDiff)!= 0){
                viewHold.textView2.setTextColor(Double.parseDouble(currPercentDiff)>0?getResources().getColor(R.color.boc_text_color_red)
                        :getResources().getColor(R.color.boc_text_color_green));
            }
            viewHold.textView2.setText(setCent(currPercentDiff));
            String changeOfMonth = (String) currentItem.get("changeOfMonth");
            viewHold.textView3.setTextColor(getResources().getColor(R.color.fonts_black));
            if(!StringUtil.isNullOrEmpty(changeOfMonth)&&Double.parseDouble(changeOfMonth)!= 0){
                viewHold.textView3.setTextColor(Double.parseDouble(changeOfMonth)>0?getResources().getColor(R.color.boc_text_color_red)
                        :getResources().getColor(R.color.boc_text_color_green));
            }
            viewHold.textView3.setText(setCent(changeOfMonth));
            String changeOfQuarter = (String) currentItem.get("changeOfQuarter");
            viewHold.textView4.setTextColor(getResources().getColor(R.color.fonts_black));
            if(!StringUtil.isNullOrEmpty(changeOfQuarter)&&Double.parseDouble(changeOfQuarter)!= 0){
                viewHold.textView4.setTextColor(Double.parseDouble(changeOfQuarter)>0?getResources().getColor(R.color.boc_text_color_red)
                        :getResources().getColor(R.color.boc_text_color_green));
            }
            viewHold.textView4.setText(setCent(changeOfQuarter));
            viewHold.textView5.setTextColor(getResources().getColor(R.color.fonts_black));
            String changeOfHalfYear = (String) currentItem.get("changeOfHalfYear");
            if(!StringUtil.isNullOrEmpty(changeOfHalfYear)&&Double.parseDouble(changeOfHalfYear)!= 0){
                viewHold.textView5.setTextColor(Double.parseDouble(changeOfHalfYear)>0?getResources().getColor(R.color.boc_text_color_red)
                        :getResources().getColor(R.color.boc_text_color_green));
            }
            viewHold.textView5.setText(setCent(changeOfHalfYear));
            String changeOfYear = (String) currentItem.get("changeOfYear");
            viewHold.textView6.setTextColor(getResources().getColor(R.color.fonts_black));
            if(!StringUtil.isNullOrEmpty(changeOfYear)&&Double.parseDouble(changeOfYear)!= 0){
                viewHold.textView6.setTextColor(Double.parseDouble(changeOfYear)>0?getResources().getColor(R.color.boc_text_color_red)
                        :getResources().getColor(R.color.boc_text_color_green));
            }
            viewHold.textView6.setText(setCent((String) currentItem.get("changeOfYear")));
            String thisYearPriceChange = (String) currentItem.get("thisYearPriceChange");
            viewHold.textView7.setTextColor(getResources().getColor(R.color.fonts_black));
            if(!StringUtil.isNullOrEmpty(thisYearPriceChange)&&Double.parseDouble(thisYearPriceChange)!= 0){
                viewHold.textView7.setTextColor(Double.parseDouble(thisYearPriceChange)>0?getResources().getColor(R.color.boc_text_color_red)
                        :getResources().getColor(R.color.boc_text_color_green));
            }
            viewHold.textView7.setText(setCent(thisYearPriceChange));
            return convertView;
        }
    };


    private String setCent(String cent){
        if(StringUtil.isNullOrEmpty(cent)){
            cent = "--";
        }else{
            cent = StringUtil.append2Decimals(Double.parseDouble(cent)*100+"",4)+"%";
        }
        return cent;
    }
    static class ViewHold{

        TextView leftTextview,finc_code,textView1,tv_date,textView2,textView3,textView4,textView5,textView6,textView7;
        LinearLayout left_item_layout,right_item_layout;
        //货币型列表
        TextView finc_name,currency_finc_code,finc_week_profit,finc_ten_thousand_profit,currency_tv_date;
    }

    /**
     * 货币型列表布局
     */
    private ICommonAdapter<Map<String,Object>> myCurrencyListAdapter = new ICommonAdapter<Map<String,Object>>(){

        @Override
        public View getView(int i, final Map<String, Object> currentItem, LayoutInflater layoutInflater, View convertView, ViewGroup viewGroup) {
            ViewHold viewHold;
            if (convertView==null) {
                viewHold=new ViewHold();
                convertView=layoutInflater.inflate(R.layout.finc_currency_list_item, null);
                viewHold.finc_name = (TextView) convertView.findViewById(R.id.finc_name);
                viewHold.currency_finc_code = (TextView) convertView.findViewById(R.id.finc_code);
                viewHold.finc_week_profit = (TextView) convertView.findViewById(R.id.finc_week_profit);
                viewHold.finc_ten_thousand_profit = (TextView) convertView.findViewById(R.id.finc_ten_thousand_profit);
                viewHold.currency_tv_date = (TextView) convertView.findViewById(R.id.tv_date);
                convertView.setTag(viewHold);
            }else {
                viewHold=(ViewHold) convertView.getTag();
            }
            viewHold.finc_name.setText((String) currentItem.get(Finc.FINC_FUNDNAME));
            viewHold.currency_finc_code.setText((String) currentItem.get(Finc.FINC_FUNDCODE));
            // 七日年化收益率
            String fundIncomeRatioStr = StringUtil
                    .valueOf1((String) currentItem.get(Finc.SEVEN_DAY_YIELD));
            if ("-".equals(fundIncomeRatioStr)||StringUtil.isNullOrEmpty(fundIncomeRatioStr)) {
                viewHold.finc_week_profit.setText("--");
            } else {
                viewHold.finc_week_profit.setText(FincUtil.setCent(fundIncomeRatioStr));
                viewHold.finc_week_profit.setTextColor(getResources().getColor(R.color.fonts_black));
                if(!StringUtil.isNullOrEmpty(fundIncomeRatioStr)&&Double.parseDouble(fundIncomeRatioStr)!= 0){
                    viewHold.finc_week_profit.setTextColor(Double.parseDouble(fundIncomeRatioStr)>0?getResources().getColor(R.color.boc_text_color_red)
                            :getResources().getColor(R.color.boc_text_color_green));
                }
            }
            // 每万份基金单位收益
            String everytenthousandStr = StringUtil.valueOf1((String) currentItem.get(Finc.FINC_FUNDINCOMEUNIT));
            String endDate = (String) currentItem.get("endDate");
            if(!StringUtil.isNullOrEmpty(endDate)&&endDate.length()== 10){
                viewHold.currency_tv_date.setText(endDate.substring(5,10));
            }else{
                viewHold.currency_tv_date.setText(FincUtil.setvalue(endDate));
            }
            viewHold.finc_ten_thousand_profit.setText(FincUtil.setvalue(everytenthousandStr));
            return convertView;
        }
    };

//    @Override
//    public boolean httpRequestCallBackPre(Object resultObj) {
//        super.httpRequestCallBackPre(resultObj);
//        BiiResponse biiResponse = (BiiResponse) resultObj;
//        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//        if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
//            if (biiResponseBody.getError().getCode()
//                    .equals(ErrorCode.FINC_ACCCHECIN_ERROR)
//                    || biiResponseBody.getError().getCode()
//                    .equals(ErrorCode.FINC_ACCNO_ERROR)||biiResponseBody.getError().getCode()
//                    .equals(ErrorCode.FINC_ACCNO_ERROR_2)) {
//                fincControl.ifhaveaccId = false;
//                fincControl.ifdorisk = false;
//                BaseHttpEngine.dissMissProgressDialog();
//                getPopup();
//                return true;
//            }
//            if (Finc.FINC_ATTENTIONQUERYLIST
//                    .equals(biiResponseBody.getMethod())) {
//                if (biiResponse.isBiiexception()) {// 代表返回数据异常
//                    BiiHttpEngine.dissMissProgressDialog();
//                    BiiError biiError = biiResponseBody.getError();
//                    // 判断是否存在error
//                    if (biiError != null) {
//                        if (biiError.getCode() != null) {
//                            if (LocalData.timeOutCode.contains(biiError
//                                    .getCode())) {// 表示回话超时 要重新登录
//                                BaseDroidApp.getInstanse().showMessageDialog(
//                                        biiError.getMessage(),
//                                        new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                BaseDroidApp.getInstanse()
//                                                        .dismissErrorDialog();
//                                                ActivityTaskManager
//                                                        .getInstance()
//                                                        .removeAllSecondActivity();
////												Intent intent = new Intent();
////												intent.setClass(FundPricesActivityNew.this,
////														LoginActivity.class);
////												startActivityForResult(
////														intent,
////														ConstantGloble.ACTIVITY_RESULT_CODE);
//                                                BaseActivity.getLoginUtils(FundPricesActivity.this).exe(new LoginTask.LoginCallback() {
//
//                                                    @Override
//                                                    public void loginStatua(boolean isLogin) {
//
//                                                    }
//                                                });
//                                            }
//                                        });
//                                // return true;
//                            } else
//                                return false;// 会话未超时
//                        }
//                    }
//                }
//                return false;// 没有异常
//            }
//
//        }else if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
//            if(Finc.METHOD_PSNOCRMPRODUCTQUERY.equals(biiResponseBody.getMethod())) {
//                BiiHttpEngine.dissMissProgressDialog();
//                BiiError biiError = biiResponseBody.getError();
//                // 判断是否存在error
//                if (biiError != null) {
//                    findViewById(R.id.command_layout).setVisibility(View.GONE);
//
//                    if (biiError.getCode() != null) {
//                        if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
//                            BaseDroidApp.getInstanse().showMessageDialog(
//                                    biiError.getMessage(),
//                                    new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            BaseDroidApp.getInstanse()
//                                                    .dismissErrorDialog();
//                                            ActivityTaskManager
//                                                    .getInstance()
//                                                    .removeAllSecondActivity();
//                                            BaseActivity.getLoginUtils(FundPricesActivity.this).exe(new LoginTask.LoginCallback() {
//
//                                                @Override
//                                                public void loginStatua(boolean isLogin) {
//
//                                                }
//                                            });
//                                        }
//                                    });
//                        }
//                    }
//                }
//            }
//        }
//        return super.httpRequestCallBackPre(resultObj);
//
//    }

    @Override
    public boolean httpRequestCallBackPre(Object resultObj) {
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
            if (Finc.FINC_ATTENTIONQUERYLIST
                    .equals(biiResponseBody.getMethod())) {
                if (biiResponse.isBiiexception()) {// 代表返回数据异常
                    BiiHttpEngine.dissMissProgressDialog();
                    BiiError biiError = biiResponseBody.getError();
                    // 判断是否存在error
                    if (biiError != null) {
                        if (biiError.getCode() != null) {
                            if (LocalData.timeOutCode.contains(biiError
                                    .getCode())) {// 表示回话超时 要重新登录
                                showTimeOutDialog(biiError.getMessage());
//                                BaseDroidApp.getInstanse().showMessageDialog(
//                                        biiError.getMessage(),
//                                        new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                BaseDroidApp.getInstanse()
//                                                        .dismissErrorDialog();
//                                                ActivityTaskManager
//                                                        .getInstance()
//                                                        .removeAllSecondActivity();
////												Intent intent = new Intent();
////												intent.setClass(FundPricesActivityNew.this,
////														LoginActivity.class);
////												startActivityForResult(
////														intent,
////														ConstantGloble.ACTIVITY_RESULT_CODE);
//                                                BaseActivity.getLoginUtils(FundPricesActivity.this).exe(new LoginTask.LoginCallback() {
//
//                                                    @Override
//                                                    public void loginStatua(boolean isLogin) {
//
//                                                    }
//                                                });
//                                            }
//                                        });
                                // return true;
                            } else
                                return false;// 会话未超时
                        }
                    }
                }
                return false;// 没有异常
            }else if(Finc.METHOD_PSNOCRMPRODUCTQUERY.equals(biiResponseBody.getMethod())) {
                BiiHttpEngine.dissMissProgressDialog();
                BiiError biiError = biiResponseBody.getError();
                // 判断是否存在error
                if (biiError != null) {
                    findViewById(R.id.command_layout).setVisibility(View.GONE);
//                    if (firstInit) {
////						firstInit = false;
//                        BaseHttpEngine.showProgressDialog();
//                        combainQueryFundInfos(currentIndex, pageSize, currencyCode,
//                                fundCompanyCode, riskLevelStr, fundKindType, fundType,fundState,currSortFlag,current_sortfield);
//                    }
                    if (biiError.getCode() != null) {
                        if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
                            showTimeOutDialog(biiError.getMessage());
//                            BaseDroidApp.getInstanse().showMessageDialog(
//                                    biiError.getMessage(),
//                                    new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            BaseDroidApp.getInstanse()
//                                                    .dismissErrorDialog();
//                                            ActivityTaskManager
//                                                    .getInstance()
//                                                    .removeAllSecondActivity();
////											Intent intent = new Intent();
////											intent.setClass(FundPricesActivityNew.this,
////													LoginActivity.class);
////											startActivityForResult(
////													intent,
////													ConstantGloble.ACTIVITY_RESULT_CODE);
//                                            BaseActivity.getLoginUtils(FundPricesActivity.this).exe(new LoginTask.LoginCallback() {
//
//                                                @Override
//                                                public void loginStatua(boolean isLogin) {
//
//                                                }
//                                            });
//                                        }
//                                    });
                        }else{
                            return false;
                        }
                    }else {
//						largest_exchange_layout.setVisibility(View.VISIBLE);
                    }
                }

//				initViewInfos();
//				BaseDroidApp.getInstanse().createDialog("",
//						biiError.getMessage(), new OnClickListener() {
//							@Override
//							public void onClick(View v) {
//								BaseDroidApp.getInstanse()
//										.dismissErrorDialog();
//
//							}
//						});
                return true;
            }else if("PsnFundQueryFundBalance".equals(biiResponseBody.getMethod())||
                    "PsnQueryFloatProfitAndLoss".equals(biiResponseBody.getMethod())) {
                fundAmt = "--";
                return false;
            } else {	// add
                String errorCode = biiResponseBody.getError().getCode();
                if (errorCode.equals(ErrorCode.FINC_ACCCHECIN_ERROR)
                        ||errorCode.equals(ErrorCode.FINC_ACCNO_ERROR)
                        ||errorCode.equals(ErrorCode.FINC_ACCNO_ERROR_2)) {
                    fincControl.ifhaveaccId = false;
                    fincControl.ifdorisk = false;
                    BaseHttpEngine.dissMissProgressDialog();
                    getPopup();
                    return true;
                }
//                if(errorCode.equals("FUND.E004")){
//                    return false;
//                }
                String method = biiResponseBody.getMethod();
                if("PsnOcrmProductQuery".equals(method)){
                    if("MCIS.SDERR".equals(errorCode)){
                        BaseDroidApp.getInstanse().showMessageDialog(
                                biiResponseBody.getError().getMessage(),
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        BaseDroidApp.getInstanse()
                                                .dismissErrorDialog();
                                        BaseHttpEngine.dissMissProgressDialog();
                                    }
                                });
                        return true;
                    }
                }
            }
        }
        return super.httpRequestCallBackPre(resultObj);
    }

//    /**
//     * 参考市值
//     */
//    protected void requestPsnAssetBalanceQuery() {
//        Map<String, Object> parms_map=new HashMap<String, Object>();
//        ShowDialogTools.Instance.showProgressDialog();
//        getHttpTools().requestHttp("PsnAssetBalanceQuery", "requestPsnAssetBalanceQueryCallBack", parms_map, false);
//    }
//
//    public void requestPsnAssetBalanceQueryCallBack(Object resultObj){
//        if (StringUtil.isNullOrEmpty(resultObj)) {
//            return;
//        }
//        Map<String, Object> map_result = getHttpTools().getResponseResult(resultObj);
//        fundAmt = String.valueOf(map_result.get("fundAmt"));
//        finc_head.setAmountText(fundAmt);
//        doCheckRequestPsnInvestmentManageIsOpen();
//    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean  isLoginBack = BaseDroidApp.getInstanse().isLogin();
        if(!isLogin&&isLoginBack){//跳转别的页面之前未登录，返回时已登录刷新页面数据
            if(intentFlag == 0){
                loginbtn.performClick();
            }
            if(intentFlag == 1||intentFlag == 2||intentFlag == 3){
                BaseHttpEngine.showProgressDialog();
                doCheckRequestPsnInvestmentManageIsOpen();
            }
        }
    }

    /**
     * 当侧拉菜单展开时点击返回键收起侧拉菜单
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode){
		    case KeyEvent.KEYCODE_BACK:
                if(mSlipDrawerLayout.isOpen()){
                    mSlipDrawerLayout.toggle();
                    return true;
                }
		}
		return super.onKeyDown(keyCode, event);
    }

    /**
     * 切换时清空列表
     */
    private void clearListData(){
        combinateList.clear();
        if("货币型".equals(selectType)||"理财型".equals(selectType)) {//不同类型加载不同布局
            if(currencyListAdapter != null){
                currencyListAdapter.notifyDataSetChanged();
            }
        }else{
            if(leftAdapter != null&&rightAdapter != null){
                leftAdapter.notifyDataSetChanged();
                rightAdapter.notifyDataSetChanged();
            }
        }

    }
}
