package com.chinamworld.bocmbci.biz.finc.finc_p606;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincIntentNew;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.biz.finc.finc_p606.util.FincUtil;
import com.chinamworld.bocmbci.biz.finc.finc_p606.util.StarBar;
import com.chinamworld.bocmbci.biz.finc.myfund.MyFincSellSubmitActivity;
import com.chinamworld.bocmbci.biz.finc.trade.FincTradeBuyActivity;
import com.chinamworld.bocmbci.biz.finc.trade.FincTradeScheduledBuyActivity;
import com.chinamworld.bocmbci.biz.invest.constant.InvestConstant;
import com.chinamworld.bocmbci.biz.preciousmetal.NewTitleAndContent;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.mode.IActionCall;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.fundnotice.FundnoticeRequestParams;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.fundnotice.FundnoticeResponseData;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.fundnotice.FundnoticeResult;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.jzTendency.JzTendencyRequestParams;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.jzTendency.JzTendencyResponseData;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.jzTendency.JzTendencyResult;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.ljYieldRateTendency.LjYieldRateTendencyRequestParams;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.ljYieldRateTendency.LjYieldRateTendencyResponseData;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.ljYieldRateTendency.LjYieldRateTendencyResult;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.newsContent.NewsContentRequestParams;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.newsContent.NewsContentResponseData;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.newsContent.NewsContentResult;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.newsList.NewsListRequestParams;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.newsList.NewsListResponseData;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.newsList.NewsListResult;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.queryFundBasicDetail.QueryFundBasicDetailRequestParams;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.queryFundBasicDetail.QueryFundBasicDetailResponseData;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.queryFundBasicDetail.QueryFundBasicDetailResult;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.queryMultipleFund.QueryMultipleFundRequestParams;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.queryMultipleFund.QueryMultipleFundResponseData;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.queryMultipleFund.QueryMultipleFundResult;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.rankTendency.RankTendencyRequestParams;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.rankTendency.RankTendencyResponseData;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.rankTendency.RankTendencyResult;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.yieldOfWanFenTendency.YieldOfWanFenTendencyRequestParams;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.yieldOfWanFenTendency.YieldOfWanFenTendencyResponseData;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.yieldOfWanFenTendency.YieldOfWanFenTendencyResult;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.yieldOfWeekTendency.YieldOfWeekTendencyRequestParams;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.yieldOfWeekTendency.YieldOfWeekTendencyResponseData;
import com.chinamworld.bocmbci.model.httpmodel.sifang.fund.yieldOfWeekTendency.YieldOfWeekTendencyResult;
import com.chinamworld.bocmbci.net.GsonTools;
import com.chinamworld.bocmbci.net.HttpHelp;
import com.chinamworld.bocmbci.net.RequestEngine;
import com.chinamworld.bocmbci.net.model.BaseResponseData;
import com.chinamworld.bocmbci.net.model.IHttpErrorCallBack;
import com.chinamworld.bocmbci.net.model.IHttpResponseCallBack;
import com.chinamworld.bocmbci.net.model.IOkHttpErrorCode;
import com.chinamworld.bocmbci.userwidget.echarsview.ECharsType;
import com.chinamworld.bocmbci.userwidget.qrcodeview.InvestQRCodeActivity;
import com.chinamworld.bocmbci.userwidget.sfkline.SFKLineView;
import com.chinamworld.bocmbci.utils.ActivityIntentTools;
import com.chinamworld.bocmbci.utils.CommPublicTools;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.bocmbci.widget.adapter.CommonAdapter;
import com.chinamworld.bocmbci.widget.adapter.ICommonAdapter;
import com.chinamworld.llbt.userwidget.NewLabelTextView;
import com.chinamworld.llbt.userwidget.dialogview.MessageDialog;
import com.chinamworld.llbt.userwidget.tabview.TabView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/8 0008.
 * 基金详情页面
 */
public class FincProductDetailActivity extends FincBaseActivity {
    /** 无操作 */
    private final int OPERATION_NONE = 0;
    /** 关注 */
    private final int OPERATION_ATTENTION = 1;
    /** 买入 */
    private final int OPERATION_BUY = 2;
    /** 定投 */
    private final int OPERATION_INVERS = 3;
    /** 定投 */
    private final int OPERATION_REDEEM = 4;
    /** 从基金关注进入　 */
    public static final int ATTENTION = 2;
    //产品属性的textview
//    NewLabelTextView product_attribute_tv1,product_attribute_tv2,product_attribute_tv3,product_attribute_tv4,product_attribute_tv5
//            ,product_attribute_tv6,product_attribute_tv7,product_attribute_tv8,product_attribute_tv9;
    /**动态加载元素**/
    NewTitleAndContent myContainerLayout;
    /**动态加载元素 产品属性**/
    NewTitleAndContent product_attribute_mycontainerLayout;
    NewLabelTextView fund_scale_tv;//基金规模
    //NewLabelTextView fund_grade_tv;//评级机构
    TextView fund_grade_tv;
    //净值走势、基金累计收益率、排名变化
    private TextView textView20,textView21,textView22;
    private ImageView ima_zoushi_line1,ima_zoushi_line2,ima_zoushi_line3;
    /**购买属性，赎回属性 */
    private ImageView ima_buyline,ima_remitline;
    private TextView textView16,textView17;
    /**公告，新闻按钮 */
    private ImageView ima_gonggaoline,ima_newsline;
    private TextView textView18,textView19;
    private ListView listview;
    /**采用通用的Adapter接口来实现数据适配功能**/
    private CommonAdapter<FundnoticeResult.FundnoticeItem> noticeAdapter;//公告
    private CommonAdapter<NewsListResult.NewsListItem> newsAdapter;//新闻
    List<Map<String, Object>> list,list2;
    /**列表更多项布局**/
    private RelativeLayout load_more;

    /**更多按钮**/
    private TextView mBtnLoadmore;
    private int loadNumber = 0;
    private int recordNumber = 0;

    private TextView buy_fee_tv;//申购费率
    private LinearLayout zoushi_layout1,zoushi_layout2,zoushi_layout3;//走势图类型切换按钮
    /** 币种 */
    private String currencyStr;
    /** 基金代码 */
    private String fundCodeStr;
    /**基金名称**/
    private String fundNameStr;
    //持仓信息
    private LinearLayout hold_info_layout;
    /** 是否登录 */
    private boolean isLogin = false;
    //产品类型
    private String fnTypeStr;
    //近一月 近三月 近半年 近一年
    private RadioGroup radioGroup_layout;
    private RadioButton one_month_radio,three_month_radion,six_month_radion,one_year_radion;
    private String fundCycleFlag="m";//m-一个月 q-三个月 h-半年 Y-一年

    /**
     * 定投按钮
     */
    private Button inversButton;
    /**
     * 购买按钮
     */
    private Button buyButton;
    /**
     * 赎回按钮
     */
    private Button redeemButton;
    /** 是否可买入 */
    private boolean canBuy;
    /** 是否可定投 */
    private boolean canScheduleBuy;
    /** 是否可赎回 */
    private boolean canRedeem;
    /** 操作标识--默认无操作 */
    private int operation_flag = OPERATION_NONE;
    private ImageView fundAttentionImage;//关注
    /**是否登录后立马进入买入/定投页面*/
    private boolean isLoginCallback = false ;
    //分红方式
    String bonusType;
    private TextView finc_fntype,product_place;//产品类型 风险级别
    private ImageView finc_risklv;
    private String isNewsOrNotice="1";//0:公告 1：新闻
    /** K线图*/
    private SFKLineView queryKTendencyView;
    private TabView mTabView;
//    private String fundId;
    private String fundCode;
    private int kLineFlag = 0;//折线图类别标识 0：净值走势 1：基金累计收益率 2：排名变化 3：七日年化收益 4：万份收益
    private List<FundnoticeResult.FundnoticeItem> noticeList = new ArrayList<>();//公告列表
    private List<NewsListResult.NewsListItem> newsList = new ArrayList<>();//新闻列表
    private String isNextPageNotice = "N";//公告是否有下一页
    private String isNextPageNews = "N";//新闻是否有下一页
    private QueryFundBasicDetailResult fundBasicDetailResult;//四方详情数据
    private TextView month_up_or_down;//近一月涨幅
    private TextView month_up_or_down_value;//近一月涨幅显示值
    private TextView look_history_value;//查看历史净值
    private String historyActivitTtypeFlag = "1";//页面类型 1：历史净值 2：历史累计收益率 3：历史排名 4：七日年化收益 5：万份收益
    List<JzTendencyResult.JzTendencyItem> jzItemList,jzItemListFirst;
    List<LjYieldRateTendencyResult.LjYieldRateTendencyItem> ljItemList;
    List<RankTendencyResult.RankTendencyItem> rankItemList;
    List<YieldOfWeekTendencyResult.YieldOfWeekTendencyItem> yieldOfWeekItemList;
    List<YieldOfWanFenTendencyResult.YieldOfWanFenTendencyItem> yieldOfWanFenItemList;
    private String isFromOther = "0";//默认为0 1表示从其他页面跳转到详情 2:从搜索页面跳转进来
    private String fromOtherFundCode;//表示从其他页面跳转到详情的code
    private TextView fund_zhexian3;//上证综指收益率
    private LinearLayout fund_zhexian_mark;//折线图上方标注
    private RequestEngine requestEngine;
    private boolean isFirst = true;//第一次进入保存历史净值数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTaskManager.addActivit(this);
        setTitle("基金详情");
        Intent intent = getIntent();
        if (StringUtil.isNullOrEmpty(intent)) {
            return;
        }else{
            isFromOther = intent.getStringExtra("isFromOther");
            fromOtherFundCode = intent.getStringExtra("fromOtherFundCode");
        }
        if ("1".equals(isFromOther)||"2".equals(isFromOther)) {
            new FincIntentNew(FincProductDetailActivity.this).fincIntent(FincProductDetailActivity.this, fromOtherFundCode, new IActionCall(){
                @Override
                public void callBack() {
                    init();
                }
            });
        }else {
            init();
        }
    }

    public void init(){
        setContentView(R.layout.finc_product_detail_p606);
//        setTitle("基金详情");
        getBackGroundLayout().setRightButtonVisibility(View.VISIBLE);
        getBackGroundLayout().setRightButtonImage(getResources().getDrawable(R.drawable.share_qr_code));
        /**跳转到基金 二维码页面**/
        getBackGroundLayout().setOnRightButtonClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                InvestQRCodeActivity.goToInvestQRCodeActivity(FincProductDetailActivity.this,
                        "基金二维码","boc://bocphone?type=2&fundCode="+fundCodeStr,fundNameStr + " "+ fundCodeStr);
            }
        });
        isLogin = BaseDroidApp.getInstanse().isLogin();
        int flag = getIntent().getIntExtra(Finc.I_ATTENTIONFLAG, -1);
        if(flag == ATTENTION){
            fincControl.fundDetails = fincControl.fincFundDetails;
        }
        //fundCode = (String)fincControl.fundDetails.get("fundCode");
        //考虑到行情页面BII数据可能为空四方数据不为空的情况，所以从行情进入时从选中列表取code值，从其他页面跳转过来的直接取传过来的值
        if ("1".equals(isFromOther)||"2".equals(isFromOther)) {
            fundCode = fromOtherFundCode;
        }else{
            fundCode = (String)fincControl.fundListSelect.get("fundCode");
        }
        // 交易币种
        currencyStr = (String) fincControl.fundDetails.get(Finc.FINC_CURRENCY);
        //BII数据为空进入详情时类型取选中的传过来的类型
        if(StringUtil.isNullOrEmpty(fincControl.fundDetails)){
            String selectType = getIntent().getStringExtra("selectType");
            fnTypeStr=FincDataCenter.fincFntypeStr.get(selectType);
        }else{
            fnTypeStr = StringUtil.valueOf1((String) fincControl.fundDetails.get(Finc.FINC_FNTYPE));
        }
        canBuy = StringUtil.parseStrToBoolean((String) fincControl.fundDetails.get(Finc.CANBUY));
        canRedeem = StringUtil.parseStrToBoolean((String) fincControl.fundDetails.get("isSale"));
        canScheduleBuy = StringUtil.parseStrToBoolean((String) fincControl.fundDetails.get(Finc.CANSCHEDULEBUY));
        fundAttentionImage = (ImageView) findViewById(R.id.finc_attention_img);
        fundAttentionImage.setOnClickListener(mAttentionListener);//关注
        if(isLogin){
            fundAttentionImage.setVisibility(View.VISIBLE);
            if(fincControl.getAttentionFlag()){//已关注
                fundAttentionImage.setImageDrawable(getResources().getDrawable(R.drawable.share_already_collect));
            }else{
                fundAttentionImage.setImageDrawable(getResources().getDrawable(R.drawable.share_not_collected));
            }
        }
        ima_buyline = (ImageView) findViewById(R.id.ima_buyline);
        ima_remitline = (ImageView) findViewById(R.id.ima_remitline);
        ima_gonggaoline = (ImageView) findViewById(R.id.ima_gonggaoline);
        ima_newsline = (ImageView) findViewById(R.id.ima_newsline);
        textView16 = (TextView) findViewById(R.id.textView16);
        textView17 = (TextView) findViewById(R.id.textView17);
        textView18 = (TextView) findViewById(R.id.textView18);
        textView19 = (TextView) findViewById(R.id.textView19);
        listview = (ListView) findViewById(R.id.listview);
        buy_fee_tv = (TextView) findViewById(R.id.buy_fee_tv);
        inversButton = (Button) findViewById(R.id.btn_left);
        redeemButton = (Button) findViewById(R.id.btn_middle);
        buyButton = (Button) findViewById(R.id.btn_right);
        inversButton.setOnClickListener(mInversListener);
        buyButton.setOnClickListener(mBuyListener);
        redeemButton.setOnClickListener(mRedeemListener);
        initHeadViewData();
        finc_fntype = (TextView) findViewById(R.id.finc_fntype);
        finc_risklv = (ImageView) findViewById(R.id.finc_risklv);
     //   product_place = (TextView) findViewById(R.id.product_place);
        finc_fntype.setText(FincDataCenter.fincFntypeStr.get(fincControl.fundDetails.get("fntype")));
        if(!StringUtil.isNullOrEmpty(fincControl.fundDetails.get("risklv"))){
            switch ((String) fincControl.fundDetails.get("risklv")){
                case "1":
                    finc_risklv.setImageDrawable(getResources().getDrawable(R.drawable.inverst_difengxian));
                    break;
                case "2":
                    finc_risklv.setImageDrawable(getResources().getDrawable(R.drawable.inverst_zhongdi));
                    break;
                case "3":
                    finc_risklv.setImageDrawable(getResources().getDrawable(R.drawable.inverst_zhongfengxian));
                    break;
                case "4":
                    finc_risklv.setImageDrawable(getResources().getDrawable(R.drawable.inverst_zhonggao));
                    break;
                case "5":
                    finc_risklv.setImageDrawable(getResources().getDrawable(R.drawable.inverst_gaofengxian));
                    break;
            }
        }

        product_attribute_mycontainerLayout = (NewTitleAndContent) findViewById(R.id.product_attribute_mycontainerLayout);
        //持仓信息
        hold_info_layout = (LinearLayout) findViewById(R.id.hold_info_layout);
        if(isLogin&&!StringUtil.isNullOrEmpty(fincControl.fundBalancList)){
            hold_info_layout.setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.finc_hold_tv1)).setText(StringUtil.parseStringPattern((String)fincControl.
                    fundBalancList.get(0).get("currentCapitalisation"), 2));
            ((TextView)findViewById(R.id.finc_hold_tv2)).setText(StringUtil.parseStringPattern((String)fincControl.
                    fundBalancList.get(0).get("totalBalance"), 2));
            String middleFloat = (String)fincControl.fundDetails.get("middleFloat");
            if(!StringUtil.isNullOrEmpty(middleFloat)){
                ((TextView)findViewById(R.id.finc_hold_tv3)).setText(middleFloat);
                if(!StringUtil.isNullOrEmpty(middleFloat)&&Double.parseDouble(middleFloat)!= 0){
                    ((TextView)findViewById(R.id.finc_hold_tv3)).setTextColor(Double.parseDouble(middleFloat)>0?getResources().getColor(R.color.boc_text_color_red)
                            :getResources().getColor(R.color.boc_text_color_green));
                }
            }
            bonusType = (String)fincControl.fundBalancList.get(0).get("bonusType");
            if (canRedeem) { // 可赎回
                redeemButton.setVisibility(View.VISIBLE);
            } else {
                redeemButton.setVisibility(View.GONE);
            }
            buyButton.setText("继续购买");
        }
        initProductPropViewData();
        //走势图
        zoushi_layout1 = (LinearLayout) findViewById(R.id.zoushi_layout1);
        zoushi_layout2 = (LinearLayout) findViewById(R.id.zoushi_layout2);
        zoushi_layout3 = (LinearLayout) findViewById(R.id.zoushi_layout3);
        fund_zhexian_mark = (LinearLayout) findViewById(R.id.fund_zhexian_mark);
        zoushi_layout1.setOnClickListener(new MyOnClickListener());
        zoushi_layout2.setOnClickListener(new MyOnClickListener());
        zoushi_layout3.setOnClickListener(new MyOnClickListener());
        textView20 = (TextView) findViewById(R.id.textView20);
        textView21 = (TextView) findViewById(R.id.textView21);
        textView22 = (TextView) findViewById(R.id.textView22);
        fund_zhexian3 = (TextView) findViewById(R.id.fund_zhexian3);
        ima_zoushi_line1 = (ImageView) findViewById(R.id.ima_zoushi_line1);
        ima_zoushi_line2 = (ImageView) findViewById(R.id.ima_zoushi_line2);
        ima_zoushi_line3 = (ImageView) findViewById(R.id.ima_zoushi_line3);
        month_up_or_down = (TextView)findViewById(R.id.month_up_or_down);
        month_up_or_down_value = (TextView)findViewById(R.id.month_up_or_down_value);
        look_history_value = (TextView)findViewById(R.id.look_history_value);
        look_history_value.setOnClickListener(new View.OnClickListener() {//查看历史净值
            @Override
            public void onClick(View view) {
                if(!StringUtil.isNullOrEmpty(fincControl.historyList)){
                    fincControl.historyList.clear();
                }
                switch (historyActivitTtypeFlag){
                    case "1":
                        fincControl.historyList = FincUtil.siFangListToList(jzItemList);
                        break;
                    case "2":
                        fincControl.historyList = FincUtil.siFangListToList2(ljItemList);
                        break;
                    case "3":
                        fincControl.historyList = FincUtil.siFangListToList3(rankItemList);
                        break;
                    case "4":
                        fincControl.historyList = FincUtil.siFangListToList4(yieldOfWeekItemList);
                        break;
                    case "5":
                        fincControl.historyList = FincUtil.siFangListToList5(yieldOfWanFenItemList);
                        break;
                }
                Map map = new HashMap();
                map.put("historyActivitTtypeFlag",historyActivitTtypeFlag);
                ActivityIntentTools.intentToActivityWithData(FincProductDetailActivity.this,FundLookHistoryValueActivity.class,map);
            }
        });
        one_month_radio = (RadioButton) findViewById(R.id.one_month_radio);
        three_month_radion = (RadioButton) findViewById(R.id.three_month_radion);
        six_month_radion = (RadioButton) findViewById(R.id.six_month_radion);
        one_year_radion = (RadioButton) findViewById(R.id.one_year_radion);
        radioGroup_layout = (RadioGroup) findViewById(R.id.radioGroup_layout);
        radioGroup_layout.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId){
                    case R.id.one_month_radio:
                        setKLineChange();
                        if("06".equals(fnTypeStr)||"01".equals(fnTypeStr)){
                            fundCycleFlag = "w";
                            month_up_or_down.setText("近七日涨幅(%):");
                            month_up_or_down_value.setText(StringUtil.isNullOrEmpty(fincControl.fundListSelect)?"--":FincUtil.setCent2((String)fincControl.fundListSelect.get("changeOfWeek")));
                        }else {
                            fundCycleFlag = "m";
                            month_up_or_down.setText("近一月涨幅(%):");
                            month_up_or_down_value.setText(StringUtil.isNullOrEmpty(fincControl.fundListSelect)?"--":FincUtil.setCent2((String)fincControl.fundListSelect.get("changeOfMonth")));
                        }
                        break;
                    case R.id.three_month_radion:
                        setKLineChange();
                        fundCycleFlag = "06".equals(fnTypeStr)||"01".equals(fnTypeStr)?"m":"q";
                        if("06".equals(fnTypeStr)||"01".equals(fnTypeStr)){
                            month_up_or_down.setText("近一月涨幅(%):");
                            month_up_or_down_value.setText(StringUtil.isNullOrEmpty(fincControl.fundListSelect)?"--":FincUtil.setCent2((String)fincControl.fundListSelect.get("changeOfMonth")));
                        }else {
                            month_up_or_down.setText("近三月涨幅(%):");
                            month_up_or_down_value.setText(StringUtil.isNullOrEmpty(fincControl.fundListSelect)?"--":FincUtil.setCent2((String)fincControl.fundListSelect.get("changeOfQuarter")));
                        }
                        break;
                    case R.id.six_month_radion://货币型隐藏该按钮
                        setKLineChange();
                        fundCycleFlag = "h";
                        month_up_or_down.setText("近六月涨幅(%):");
                        month_up_or_down_value.setText(StringUtil.isNullOrEmpty(fincControl.fundListSelect)?"--":FincUtil.setCent2((String)fincControl.fundListSelect.get("changeOfHalfYear")));
                        break;
                    case R.id.one_year_radion:
                        setKLineChange();
                        fundCycleFlag = "06".equals(fnTypeStr)||"01".equals(fnTypeStr)?"t":"y";
                        if("06".equals(fnTypeStr)||"01".equals(fnTypeStr)){
                            month_up_or_down.setText("近三月涨幅(%):");
                            month_up_or_down_value.setText(StringUtil.isNullOrEmpty(fincControl.fundListSelect)?"--":FincUtil.setCent2((String)fincControl.fundListSelect.get("changeOfQuarter")));
                        }else {
                            month_up_or_down.setText("近一年涨幅(%):");
                            month_up_or_down_value.setText(StringUtil.isNullOrEmpty(fincControl.fundListSelect)?"--":FincUtil.setCent2((String)fincControl.fundListSelect.get("changeOfYear")));
                        }
                        break;
                }
                useWhichRequest(fundCycleFlag,kLineFlag);
            }
        });
        if ("06".equals(fnTypeStr)||"01".equals(fnTypeStr)) { // 货币型基金或理财型基金
            fund_zhexian3.setText("01".equals(fnTypeStr)?"区间存款利率":"上证综指收益率");
            kLineFlag = 3;
            historyActivitTtypeFlag = "4";//货币基金默认为4：七日年化收益
            zoushi_layout1.setVisibility(View.GONE);
            FincUtil.setVisibilityAndTextColor(FincProductDetailActivity.this,textView21,false,ima_zoushi_line2,false);
            //七日年化收益率
            textView21.setText("七日年化收益率");
            textView22.setText("万份收益");
            month_up_or_down.setText("近七日涨幅(%):");
            look_history_value.setText("查看历史七日年化收益率");
            six_month_radion.setVisibility(View.GONE);
            one_month_radio.setText("近七日");
            three_month_radion.setText("近一月");
            one_year_radion.setText("近三月");
        }

        queryKTendencyView = (SFKLineView) findViewById(R.id.KLine_view);
        queryKTendencyView.setTitleContentVisibility(View.GONE);
        queryKTendencyView.setShowType(2);
        queryKTendencyView.setImaLandscapePortraitVisibility(View.GONE);
        queryKTendencyView.setmBocTrendViewVisibility(View.GONE);
        queryKTendencyView.setSfkLineEmptyVisibility(View.VISIBLE);
        if("06".equals(fnTypeStr)||"01".equals(fnTypeStr)){
            getYieldOfWeekTendency(fundCode,"w");
        }else{
            getJzTendency(fundCode,"m");
        }
        findViewById(R.id.textView16_layout).setOnClickListener(new MyOnClickListener());//赎回属性
        findViewById(R.id.textView17_layout).setOnClickListener(new MyOnClickListener());
        textView18.setOnClickListener(new MyOnClickListener());
        textView19.setOnClickListener(new MyOnClickListener());
        myContainerLayout = (NewTitleAndContent) findViewById(R.id.mycontainerLayout);
        initBuyPropViewData();

        load_more = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.finc_productdetail_listview_more, null);
        mBtnLoadmore = (TextView) load_more.findViewById(R.id.more_tv);
        mBtnLoadmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FincProductDetailActivity.this,FundNewsAndNoticeActivity.class);
                intent.putExtra("isNewsOrNotice",isNewsOrNotice);
                startActivity(intent);
            }
        });
//        getFundnotice(fundCode,"1","5");
        getQueryFundBasicDetail(fundCode);
        getQueryMultipleFund(fundCode);
        getNewsList(fundCode,"1","5");
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(isNewsOrNotice.equals("0")){
                    fincControl.noticeItem = noticeList.get(i);
                    getNewsContent(noticeList.get(i).getIoriid());
                }else{
                    fincControl.newsItem = newsList.get(i);
                    getNewsContent(newsList.get(i).getContentId());
                }
            }
        });
        buy_fee_tv.getPaint().setAntiAlias(true);//抗锯齿
        buy_fee_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//设置中划线
        //只显示一个按钮时按钮背景为红色inversButton;buyButton;redeemButton;
        if(inversButton.getVisibility()== View.VISIBLE && redeemButton.getVisibility()== View.GONE
                &&buyButton.getVisibility()== View.GONE){
            inversButton.setBackgroundResource(R.drawable.share_red_button);
            inversButton.setTextColor(getResources().getColor(R.color.btn_white));
        }
        if(redeemButton.getVisibility()== View.VISIBLE && inversButton.getVisibility()== View.GONE
                && buyButton.getVisibility()== View.GONE){
            redeemButton.setBackgroundResource(R.drawable.share_red_button);
            redeemButton.setTextColor(getResources().getColor(R.color.btn_white));
        }
//        findViewById(R.id.finc_detail_help).setOnClickListener(new View.OnClickListener() {//帮助
//            @Override
//            public void onClick(View view) {
//                InvestHelpActivity.showHelpMessage(FincProductDetailActivity.this,getString(R.string.fund_help_message));
//            }
//        });
    }

    /**
     * 根据折线图表示判断调用哪个接口
     * @param fundCycleFlag 时间标识
     * @param kLineFlag 折线图标识
     */
    private void useWhichRequest(String fundCycleFlag,int kLineFlag){
        if(kLineFlag == 0){
            getJzTendency(fundCode,fundCycleFlag);
        }
        if(kLineFlag == 1){
            getLjYieldRateTendency(fundCode,fundCycleFlag);
        }
        if(kLineFlag == 2){
            getRankTendency(fundCode,fundCycleFlag);
        }
        if(kLineFlag == 3){
            getYieldOfWeekTendency(fundCode,fundCycleFlag);
        }
        if(kLineFlag == 4){
            getYieldOfWanFenTendency(fundCode,fundCycleFlag);
        }
    }

    /** 关注 */
    private View.OnClickListener mAttentionListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (isLogin) {
                modifyAttentionStatus();
            } else {
                operation_flag = OPERATION_ATTENTION;
                startLogin();
            }
        }
    };

    /** 修改基金关注状态 */
    protected void modifyAttentionStatus() {
        if (fincControl.getAttentionFlag()) {// 已经关注
            BaseDroidApp.getInstanse().showErrorDialog(
                    getResources().getString(
                            R.string.finc_attention_conern_confirm),
                    R.string.cancle, R.string.confirm, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (Integer.parseInt(v.getTag() + "")) {
                                case CustomDialog.TAG_SURE:
                                    // 确认取消关注
                                    BaseDroidApp.getInstanse().dismissErrorDialog();
                                    requestCommConversationId();
                                    BaseHttpEngine.showProgressDialog();
                                    break;
                                case CustomDialog.TAG_CANCLE:
                                    operation_flag = OPERATION_NONE;
                                    BaseDroidApp.getInstanse().dismissErrorDialog();
                                    break;
                            }
                        }
                    });
        } else {// 未关注
            if (fincControl.getAttentionCount() >= 10) {
//                BaseDroidApp.getInstanse().showInfoMessageDialog(
//                        getString(R.string.finc_setattention_num_error));
                MessageDialog.showMessageDialog(this,getString(R.string.finc_setattention_num_error));
            } else {
                BaseHttpEngine.showProgressDialog();
                requestCommConversationId();
            }
        }
    }

    @Override
    public void requestCommConversationIdCallBack(Object resultObj) {
        super.requestCommConversationIdCallBack(resultObj);
        requestPSNGetTokenId();
    }

    @Override
    public void requestPSNGetTokenIdCallback(Object resultObj) {
        super.requestPSNGetTokenIdCallback(resultObj);
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        String tokenId = (String) biiResponseBody.getResult();
        if (StringUtil.isNullOrEmpty(tokenId)) {
            return;
        }
        if (fincControl.getAttentionFlag()) {// 已经关注，取消关注
            attentionFundConsern(fundCodeStr, tokenId);
        } else {// 未关注，关注
            attentionFundAdd(fundCodeStr, tokenId);

        }
    }

    @Override
    public void attentionFundAddCallback(Object resultObj) {
        BiiHttpEngine.dissMissProgressDialog();
        super.attentionFundAddCallback(resultObj);
        FincControl.getInstance().addAttentionCount1();
        CustomDialog.toastShow(this,
                getResources()
                        .getString(R.string.finc_zixuan_add));
        fincControl.setAttentionFlag(true);
        // fincControl.addAttentionCount1();
        operation_flag = OPERATION_NONE;
        fundAttentionImage.setImageDrawable(getResources().getDrawable(R.drawable.share_already_collect));
        setAttentionIconAndText(fincControl.getAttentionFlag());

    }

    @Override
    public void attentionFundConsernCallback(Object resultObj) {
        BaseHttpEngine.dissMissProgressDialog();
        super.attentionFundConsernCallback(resultObj);
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        // Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
        // .getResult();
        // String fundCode = (String) resultMap.get(Finc.FINC_FUNDCODE);
        fincControl.setAttentionFlag(false);
        fincControl.minusAttentionCount1();
        operation_flag = OPERATION_NONE;
        setAttentionIconAndText(fincControl.getAttentionFlag());
        fundAttentionImage.setImageDrawable(getResources().getDrawable(R.drawable.share_not_collected));
        CustomDialog.toastShow(this,
                getResources()
                        .getString(R.string.finc_zixuan_cancle));
    }

    /**
     * 设置关注状态
     *
     * @param attentionFlag
     */
    private void setAttentionIconAndText(boolean attentionFlag) {
        if (attentionFlag) { // 已关注
            fundAttentionImage.setImageDrawable(getResources().getDrawable(R.drawable.share_already_collect));
        } else { // 未关注
            fundAttentionImage.setImageDrawable(getResources().getDrawable(R.drawable.share_not_collected));
        }
    }

    /** 买入 */
    private View.OnClickListener mBuyListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            operation_flag = OPERATION_BUY;
            if (isLogin) {
                /**基金买入，判断是否未风险评估*/
                BaseHttpEngine.showProgressDialog();
                doCheckRequestPsnFundRiskEvaluationQueryResult();
            } else {
                startLogin();
            }
        }
    };

    /** 赎回 */
    private View.OnClickListener mRedeemListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            operation_flag = OPERATION_REDEEM;
            if (isLogin) {
                /**基金买入，判断是否未风险评估*/
                BaseHttpEngine.showProgressDialog();
                doCheckRequestPsnFundRiskEvaluationQueryResult();
            } else {
                startLogin();
            }
        }
    };

    protected void startBuyActivity() {
        operation_flag = OPERATION_NONE;
        if (canBuy) {
            fincControl.tradeFundDetails = fincControl.fundDetails;
            Intent intent = new Intent();
            intent.setClass(this, FincTradeBuyActivity.class);
            intent.putExtra(Finc.I_ATTENTIONFLAG, ATTENTION);
            startActivityForResult(intent, 1);
        } else {
//            BaseDroidApp.getInstanse().showInfoMessageDialog(
//                    getString(R.string.finc_canbuy_error));
            MessageDialog.showMessageDialog(this,getString(R.string.finc_canbuy_error));
            return;
        }
    }

    /**
     * 赎回
     */
    protected void startRedeemActivity() {
        operation_flag = OPERATION_NONE;
        fincControl.tradeFundDetails = fincControl.fundDetails;
        Intent intent = new Intent(FincProductDetailActivity.this,
                MyFincSellSubmitActivity.class);
        startActivityForResult(intent, 1);

    }

    /** 定投 */
    private View.OnClickListener mInversListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            operation_flag = OPERATION_INVERS;
            if (isLogin) {
                /**基金买入，判断是否未风险评估*/
                BaseHttpEngine.showProgressDialog();
                doCheckRequestPsnFundRiskEvaluationQueryResult();
            } else {
                startLogin();
            }
        }
    };

    protected void startLogin() {
        BaseActivity.getLoginUtils(FincProductDetailActivity.this).exe(new LoginTask.LoginCallback() {

            @Override
            public void loginStatua(boolean isLogin) {
                if(isLogin){
                    switch (operation_flag) {
                        case OPERATION_NONE:
                            ActivityTaskManager.getInstance().removeAllSecondActivity();
                            Intent intent = new Intent(FincProductDetailActivity.this,FundPricesActivity.class);
                            startActivity(intent);
                            break;
                        default:
                            isLoginCallback = true ;
                            BaseHttpEngine.showProgressDialog();
                            doCheckRequestPsnInvestmentManageIsOpen();
                            break;
                    }
                }
            }
        });
    }

    /**
     * 定投
     */
    protected void startInversActivity() {
        operation_flag = OPERATION_NONE;
        if (canScheduleBuy) {
            fincControl.tradeFundDetails = fincControl.fundDetails;
            Intent intent = new Intent();
            intent.setClass(this, FincTradeScheduledBuyActivity.class);
            startActivityForResult(intent, 1);
        } else {
//            BaseDroidApp.getInstanse().showInfoMessageDialog(
//                    getString(R.string.finc_cansecheduedbuy_error));
            MessageDialog.showMessageDialog(this,getString(R.string.finc_cansecheduedbuy_error));
            return;
        }
    }

    /**
     * 获得基金公告列表，分页数据
     * @param fundId 基金Id
     * @param pageNo 页数
     * @param pageSize 每页最大条数
     */
    private void getFundnotice(String fundId,String pageNo,String pageSize){
        FundnoticeRequestParams requestParams = new FundnoticeRequestParams(fundId,pageNo,pageSize);
        HttpHelp h= HttpHelp.getInstance();
        h.postHttpFromSF(this,requestParams);
        h.setHttpErrorCallBack(null);
        h.setHttpResponseCallBack(new IHttpResponseCallBack() {
            @Override
            public boolean responseCallBack(String response, Object extendParams) {
                FundnoticeResponseData data = GsonTools.fromJson(response,FundnoticeResponseData.class);
                FundnoticeResult body = data.getBody();
                List<FundnoticeResult.FundnoticeItem> itemList = body.getItem();
                noticeList = itemList;
                isNextPageNotice = body.getIsNextPage();

//                if("Y".equals(isNextPageNotice)){
//                    listview.addFooterView(load_more);
//                }else{
//                    listview.removeFooterView(load_more);
//                }
                getNewsList(fundCode,"1","5");
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

    /**
     * 获得基金新闻列表，分页数据
     * @param tags 标签、关键字
     * @param pageNo 页数
     * @param pageSize 每页最大条数
     */
    private void getNewsList(String tags,String pageNo,String pageSize){
        NewsListRequestParams requestParams = new NewsListRequestParams(tags,pageNo,pageSize);
        HttpHelp h= HttpHelp.getInstance();
        h.postHttpFromSF(this,requestParams);
        h.setHttpErrorCallBack(null);
        h.setHttpResponseCallBack(new IHttpResponseCallBack() {
            @Override
            public boolean responseCallBack(String response, Object extendParams) {
                NewsListResponseData data = GsonTools.fromJson(response,NewsListResponseData.class);
                NewsListResult body = data.getBody();
                List<NewsListResult.NewsListItem> itemList = body.getItem();
                newsList = itemList;
                isNextPageNews = body.getIsNextPage();
                //同时为空时不作处理，默认不显示
                //公告为空，新闻不为空
                if(/*StringUtil.isNullOrEmpty(noticeList) &&*/ !StringUtil.isNullOrEmpty(newsList)){
                    //因为只有新闻，暂不使用新闻公告切换布局
//                    findViewById(R.id.notice_and_news_layout).setVisibility(View.VISIBLE);
//                    findViewById(R.id.newList_layout).setVisibility(View.VISIBLE);
//                    FincUtil.setVisibilityAndTextColor(FincProductDetailActivity.this,textView19,false,ima_newsline,false);
                    findViewById(R.id.finc_news_tv).setVisibility(View.VISIBLE);
                    listview.setFocusable(false);
                    newsAdapter = new CommonAdapter<NewsListResult.NewsListItem>(FincProductDetailActivity.this,itemList,myNewsAdapter);
                    listview.setAdapter(newsAdapter);
                    if("Y".equals(isNextPageNews)){
                        listview.addFooterView(load_more);
                    }
                }
                //公告不为空，由于没有数据 暂时屏蔽公告
//                if(!StringUtil.isNullOrEmpty(noticeList)){
//                    findViewById(R.id.notice_and_news_layout).setVisibility(View.VISIBLE);
//                    findViewById(R.id.notice_layout).setVisibility(View.VISIBLE);
//                    noticeAdapter = new CommonAdapter<FundnoticeResult.FundnoticeItem>(FincProductDetailActivity.this,noticeList,myNoticeAdapter);
//                    listview.setAdapter(noticeAdapter);
//                    //新闻不为空
//                    if(!StringUtil.isNullOrEmpty(newsList)){
//                        findViewById(R.id.newList_layout).setVisibility(View.VISIBLE);
//                    }
//                    if("Y".equals(isNextPageNotice)){
//                        listview.addFooterView(load_more);
//                    }
//                }

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

    /**
     * 获取基金新闻内容
     * @param contentId
     */
    private void getNewsContent(String contentId){
        NewsContentRequestParams requestParams = new NewsContentRequestParams(contentId);
        HttpHelp h= HttpHelp.getInstance();
        h.postHttpFromSF(this,requestParams);
        h.setHttpErrorCallBack(null);
        h.setHttpResponseCallBack(new IHttpResponseCallBack() {
            @Override
            public boolean responseCallBack(String response, Object extendParams) {
                NewsContentResponseData data = GsonTools.fromJson(response,NewsContentResponseData.class);
                NewsContentResult body = data.getBody();
                fincControl.contentDetail = body;
                Map map = new HashMap();
                map.put("isNewsOrNotice",isNewsOrNotice);
                ActivityIntentTools.intentToActivityWithData(FincProductDetailActivity.this,FundNewsAndNoticeDetailActivity.class,map);
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

    /**
     * 净值走势-趋势图、历史净值
     * @param fundId 后统一修改为fundcode
     * @param fundCycle
     */
    private void getJzTendency(String fundId,String fundCycle){
        JzTendencyRequestParams requestParams = new JzTendencyRequestParams(fundId,fundCycle);
        HttpHelp h= HttpHelp.getInstance();
        requestEngine = h.postHttpFromSF(this,requestParams);
        h.setHttpErrorCallBack(null);
        h.setHttpResponseCallBack(new IHttpResponseCallBack() {
            @Override
            public boolean responseCallBack(String response, Object extendParams) {
                JzTendencyResponseData data = GsonTools.fromJson(response,JzTendencyResponseData.class);
                JzTendencyResult body = data.getBody();
                jzItemList = body.getItem();
                if(isFirst){
                    isFirst = false;
                    jzItemListFirst = body.getItem();
                }
                if(StringUtil.isNullOrEmpty(jzItemList)){
                    look_history_value.setVisibility(View.GONE);
                    queryKTendencyView.setQuShiData("yyyy\nMM-dd","yyyy-MM-dd","单位净值：","日期：","单位净值",3,"yyyy-MM-dd","暂无数据");
                    queryKTendencyView.setECharsViewData(ECharsType.QuShi, body);
                    return false;
                }else{
                    look_history_value.setVisibility(View.VISIBLE);
                }
                //近一月涨幅 (（最后一个数减去第一个数）/第一个数)*100 后修改为从接口中取值
//                String monthValue = "--";
//                String firstValueStr = jzItemList.get(0).getDwjz();
//                String lastValueStr = jzItemList.get(jzItemList.size()-1).getDwjz();
//                if(!StringUtil.isNullOrEmpty(firstValueStr)&&!StringUtil.isNullOrEmpty(lastValueStr)){
//                    if(Double.parseDouble(firstValueStr) != 0){
//                        monthValue = StringUtil.append2Decimals((Double.parseDouble(lastValueStr)-
//                                Double.parseDouble(firstValueStr)) /Double.parseDouble(firstValueStr)*100+"",4)+"%";
//                    }
//                }
//                month_up_or_down_value.setText(monthValue);
                queryKTendencyView.setQuShiData("yyyy\nMM-dd","yyyy-MM-dd","单位净值：","日期：","单位净值",3,"yyyy-MM-dd","暂无数据");
                queryKTendencyView.setECharsViewData(ECharsType.QuShi, body);
                return false;
            }
        });
        h.setOkHttpErrorCode(new IOkHttpErrorCode() {
            @Override
            public boolean handlerErrorCode(BaseResponseData responseData, Object extendParams) {
                MessageDialog.closeDialog(); //关闭通讯框
                look_history_value.setVisibility(View.GONE);
                //queryKTendencyView.setECharsViewData(ECharsType.QuShi, null);
                return true;
            }
        });
        h.setHttpErrorCallBack(new IHttpErrorCallBack() {
            @Override
            public boolean onError(String exceptionMessage, Object extendParams) {
                MessageDialog.closeDialog(); //关闭通讯框
                look_history_value.setVisibility(View.GONE);
                return true;
            }
        });
    }

    /**
     * 基金累计收益率--趋势图、历史累计收益
     * @param fundId
     * @param fundCycle
     */
    private void getLjYieldRateTendency(String fundId,String fundCycle){
        LjYieldRateTendencyRequestParams requestParams = new LjYieldRateTendencyRequestParams(fundId,fundCycle);
        HttpHelp h= HttpHelp.getInstance();
        requestEngine = h.postHttpFromSF(this,requestParams);
        h.setHttpErrorCallBack(null);
        h.setHttpResponseCallBack(new IHttpResponseCallBack() {
            @Override
            public boolean responseCallBack(String response, Object extendParams) {
                LjYieldRateTendencyResponseData data = GsonTools.fromJson(response,LjYieldRateTendencyResponseData.class);
                LjYieldRateTendencyResult body = data.getBody();
                ljItemList = body.getItem();
                if(StringUtil.isNullOrEmpty(ljItemList)){
                    look_history_value.setVisibility(View.GONE);
//                    fund_zhexian_mark.setVisibility(View.GONE);
                    //return false;
                }else{
                    look_history_value.setVisibility(View.VISIBLE);
//                    fund_zhexian_mark.setVisibility(View.VISIBLE);
                }
                if(!StringUtil.isNullOrEmpty(fnTypeStr)&&"01".equals(fnTypeStr)){
                    queryKTendencyView.setECharsViewData(ECharsType.MutilLine,body.getLine12(),body.getLine32(),body.getLine4());
                }else{
                    queryKTendencyView.setECharsViewData(ECharsType.MutilLine,body.getLine1(),body.getLine3(),body.getLine2());
                }
                return false;
            }
        });
        h.setOkHttpErrorCode(new IOkHttpErrorCode() {
            @Override
            public boolean handlerErrorCode(BaseResponseData responseData, Object extendParams) {
                MessageDialog.closeDialog(); //关闭通讯框
                look_history_value.setVisibility(View.GONE);
                return true;
            }
        });
        h.setHttpErrorCallBack(new IHttpErrorCallBack() {
            @Override
            public boolean onError(String exceptionMessage, Object extendParams) {
                MessageDialog.closeDialog(); //关闭通讯框
                look_history_value.setVisibility(View.GONE);
                return true;
            }
        });
    }

    /**
     * 排名变化--趋势图、历史排名变化
     * @param fundId
     * @param fundCycle
     */
    private void getRankTendency(String fundId,String fundCycle){
        RankTendencyRequestParams requestParams = new RankTendencyRequestParams(fundId,fundCycle);
        HttpHelp h= HttpHelp.getInstance();
        requestEngine = h.postHttpFromSF(this,requestParams);
        h.setHttpErrorCallBack(null);
        h.setHttpResponseCallBack(new IHttpResponseCallBack() {
            @Override
            public boolean responseCallBack(String response, Object extendParams) {
                RankTendencyResponseData data = GsonTools.fromJson(response,RankTendencyResponseData.class);
                RankTendencyResult body = data.getBody();
                rankItemList = body.getItem();
                if(StringUtil.isNullOrEmpty(rankItemList)){
                    look_history_value.setVisibility(View.GONE);
                    //return false;
                }else {
                    look_history_value.setVisibility(View.VISIBLE);
                }
                queryKTendencyView.setECharsViewData(ECharsType.MutilTitleLine,body.getLine1(),body.getLine2(),body.getLine3());
                return false;
            }
        });
        h.setOkHttpErrorCode(new IOkHttpErrorCode() {
            @Override
            public boolean handlerErrorCode(BaseResponseData responseData, Object extendParams) {
                MessageDialog.closeDialog(); //关闭通讯框
                look_history_value.setVisibility(View.GONE);
                return true;
            }
        });
        h.setHttpErrorCallBack(new IHttpErrorCallBack() {
            @Override
            public boolean onError(String exceptionMessage, Object extendParams) {
                MessageDialog.closeDialog(); //关闭通讯框
                look_history_value.setVisibility(View.GONE);
                return true;
            }
        });
    }

    /**
     * 七日年化收益率--趋势图、历史
     * @param fundId
     * @param fundCycle
     */
    private void getYieldOfWeekTendency(String fundId,String fundCycle){
        YieldOfWeekTendencyRequestParams requestParams = new YieldOfWeekTendencyRequestParams(fundId,fundCycle);
        HttpHelp h= HttpHelp.getInstance();
        requestEngine = h.postHttpFromSF(this,requestParams);
        h.setHttpErrorCallBack(null);
        h.setHttpResponseCallBack(new IHttpResponseCallBack() {
            @Override
            public boolean responseCallBack(String response, Object extendParams) {
                YieldOfWeekTendencyResponseData data = GsonTools.fromJson(response,YieldOfWeekTendencyResponseData.class);
                YieldOfWeekTendencyResult body = data.getBody();
                yieldOfWeekItemList = body.getItem();
                if(StringUtil.isNullOrEmpty(yieldOfWeekItemList)){
                    look_history_value.setVisibility(View.GONE);
                    //return false;
                }else {
                    look_history_value.setVisibility(View.VISIBLE);
                }
                //queryKTendencyView.setECharsViewData(ECharsType.ZheXian, body);
                queryKTendencyView.setQuShiData("yyyy\nMM-dd","yyyy-MM-dd","七日年化收益率(%)：","日期：","七日年化收益率",3,"yyyy-MM-dd","暂无数据");
                queryKTendencyView.setECharsViewData(ECharsType.QuShi, body);
                return false;
            }
        });
        h.setOkHttpErrorCode(new IOkHttpErrorCode() {
            @Override
            public boolean handlerErrorCode(BaseResponseData responseData, Object extendParams) {
                MessageDialog.closeDialog(); //关闭通讯框
                look_history_value.setVisibility(View.GONE);
                return true;
            }
        });
        h.setHttpErrorCallBack(new IHttpErrorCallBack() {
            @Override
            public boolean onError(String exceptionMessage, Object extendParams) {
                MessageDialog.closeDialog(); //关闭通讯框
                look_history_value.setVisibility(View.GONE);
                return true;
            }
        });
    }

    /**
     * 万份收益率--趋势图、历史
     * @param fundId
     * @param fundCycle
     */
    private void getYieldOfWanFenTendency(String fundId,String fundCycle){
        YieldOfWanFenTendencyRequestParams requestParams = new YieldOfWanFenTendencyRequestParams(fundId,fundCycle);
        HttpHelp h= HttpHelp.getInstance();
        requestEngine = h.postHttpFromSF(this,requestParams);
        h.setHttpErrorCallBack(null);
        h.setHttpResponseCallBack(new IHttpResponseCallBack() {
            @Override
            public boolean responseCallBack(String response, Object extendParams) {
                YieldOfWanFenTendencyResponseData data = GsonTools.fromJson(response,YieldOfWanFenTendencyResponseData.class);
                YieldOfWanFenTendencyResult body = data.getBody();
                yieldOfWanFenItemList = body.getItem();
                if(StringUtil.isNullOrEmpty(yieldOfWanFenItemList)){
                    look_history_value.setVisibility(View.GONE);
                    //return false;
                }else{
                    look_history_value.setVisibility(View.VISIBLE);
                }
                //queryKTendencyView.setECharsViewData(ECharsType.ZheXian, body);
                queryKTendencyView.setQuShiData("yyyy\nMM-dd","yyyy-MM-dd","万份收益：","日期：","万份收益",3,"yyyy-MM-dd","暂无数据");
                queryKTendencyView.setECharsViewData(ECharsType.QuShi, body);
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

    private ICommonAdapter<FundnoticeResult.FundnoticeItem> myNoticeAdapter = new ICommonAdapter<FundnoticeResult.FundnoticeItem>(){

        @Override
        public View getView(int i, FundnoticeResult.FundnoticeItem item, LayoutInflater layoutInflater, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder = new ViewHolder();
            if (convertView == null) {
                convertView = layoutInflater.inflate(
                        R.layout.finc_productdetail_listview_item, null);
                viewHolder.title = (TextView) convertView
                        .findViewById(R.id.item_titile);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.title.setText(item.getStrcaption());
            return convertView;
        }
    };

    private ICommonAdapter<NewsListResult.NewsListItem> myNewsAdapter = new ICommonAdapter<NewsListResult.NewsListItem>(){

        @Override
        public View getView(int i, NewsListResult.NewsListItem item, LayoutInflater layoutInflater, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder = new ViewHolder();
            if (convertView == null) {
                convertView = layoutInflater.inflate(
                        R.layout.finc_productdetail_listview_item, null);
                viewHolder.title = (TextView) convertView
                        .findViewById(R.id.item_titile);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.title.setText(item.getTitle());
            return convertView;
        }
    };

    private class ViewHolder {
        /** 标题 */
        public TextView title;
    }
    public class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.zoushi_layout1://净值走势 货币型时隐藏
                    look_history_value.setVisibility(View.GONE);
                    setKLineChange();
                    fund_zhexian_mark.setVisibility(View.GONE);
                    kLineFlag = 0;
                    fundCycleFlag = "06".equals(fnTypeStr)||"01".equals(fnTypeStr)?"w":"m";
                    one_month_radio.setChecked(true);
                    look_history_value.setText("查看历史净值");
                    historyActivitTtypeFlag = "1";
                    FincUtil.setVisibilityAndTextColor(FincProductDetailActivity.this,textView20,false,ima_zoushi_line1,false);
                    FincUtil.setVisibilityAndTextColor(FincProductDetailActivity.this,textView21,true,ima_zoushi_line2,true);
                    FincUtil.setVisibilityAndTextColor(FincProductDetailActivity.this,textView22,true,ima_zoushi_line3,true);
                    useWhichRequest(fundCycleFlag,kLineFlag);
                    break;
                case R.id.zoushi_layout2:
                    queryKTendencyView.setmBocTrendViewVisibility(View.GONE);
                    look_history_value.setVisibility(View.GONE);
                    setKLineChange();
                    if(!"06".equals(fnTypeStr)&&!"01".equals(fnTypeStr)){
                        fund_zhexian_mark.setVisibility(View.VISIBLE);
                    }
                    look_history_value.setText("06".equals(fnTypeStr)||"01".equals(fnTypeStr) ?"查看历史七日年化收益率":"查看历史累计收益率");
                    kLineFlag = "06".equals(fnTypeStr)||"01".equals(fnTypeStr) ? 3:1;
                    historyActivitTtypeFlag = "06".equals(fnTypeStr)||"01".equals(fnTypeStr) ? "4":"2";
                    fundCycleFlag = "06".equals(fnTypeStr)||"01".equals(fnTypeStr)?"w":"m";
                    one_month_radio.setChecked(true);
                    FincUtil.setVisibilityAndTextColor(FincProductDetailActivity.this,textView20,true,ima_zoushi_line1,true);
                    FincUtil.setVisibilityAndTextColor(FincProductDetailActivity.this,textView21,false,ima_zoushi_line2,false);
                    FincUtil.setVisibilityAndTextColor(FincProductDetailActivity.this,textView22,true,ima_zoushi_line3,true);
                    useWhichRequest(fundCycleFlag,kLineFlag);
                    break;
                case R.id.zoushi_layout3:
                    queryKTendencyView.setmBocTrendViewVisibility(View.GONE);
                    look_history_value.setVisibility(View.GONE);
                    setKLineChange();
                    fund_zhexian_mark.setVisibility(View.GONE);
                    look_history_value.setText("06".equals(fnTypeStr)||"01".equals(fnTypeStr) ?"查看历史万份收益":"查看历史排名");
                    kLineFlag = "06".equals(fnTypeStr)||"01".equals(fnTypeStr) ? 4:2;
                    historyActivitTtypeFlag = "06".equals(fnTypeStr)||"01".equals(fnTypeStr) ? "5":"3";
                    fundCycleFlag = "06".equals(fnTypeStr)||"01".equals(fnTypeStr)?"w":"m";//点击恢复默认选中第一项
                    one_month_radio.setChecked(true);
                    FincUtil.setVisibilityAndTextColor(FincProductDetailActivity.this,textView20,true,ima_zoushi_line1,true);
                    FincUtil.setVisibilityAndTextColor(FincProductDetailActivity.this,textView21,true,ima_zoushi_line2,true);
                    FincUtil.setVisibilityAndTextColor(FincProductDetailActivity.this,textView22,false,ima_zoushi_line3,false);
                    useWhichRequest(fundCycleFlag,kLineFlag);
                    break;
                case R.id.textView16_layout://购买属性
                    FincUtil.setVisibilityAndTextColor(FincProductDetailActivity.this,textView16,false,ima_buyline,false);
                    FincUtil.setVisibilityAndTextColor(FincProductDetailActivity.this,textView17,true,ima_remitline,true);
                    myContainerLayout.removeAllViews();
                    initBuyPropViewData();
                    break;
                case R.id.textView17_layout://赎回属性
                    FincUtil.setVisibilityAndTextColor(FincProductDetailActivity.this,textView16,true,ima_buyline,true);
                    FincUtil.setVisibilityAndTextColor(FincProductDetailActivity.this,textView17,false,ima_remitline,false);
                    myContainerLayout.removeAllViews();
                    initRedeemPropViewData();
                    break;
                case R.id.textView18://公告
                    isNewsOrNotice="0";
                    if(ima_gonggaoline.getVisibility() == View.INVISIBLE){
                        noticeAdapter = new CommonAdapter<FundnoticeResult.FundnoticeItem>(FincProductDetailActivity.this,noticeList,myNoticeAdapter);
                        listview.setAdapter(noticeAdapter);
                        if(listview.getFooterViewsCount()> 0&&"N".equals(isNextPageNotice)){
                            listview.removeFooterView(load_more);
                        }else{
                            listview.addFooterView(load_more);
                        }
                    }
                    FincUtil.setVisibilityAndTextColor(FincProductDetailActivity.this,textView18,false,ima_gonggaoline,false);
                    FincUtil.setVisibilityAndTextColor(FincProductDetailActivity.this,textView19,true,ima_newsline,true);
//                    getFundnotice(fundCode,"1","5");

                    break;
                case R.id.textView19://新闻
                    isNewsOrNotice="1";
                    if(ima_newsline.getVisibility() == View.INVISIBLE){
                        newsAdapter = new CommonAdapter<NewsListResult.NewsListItem>(FincProductDetailActivity.this,newsList,myNewsAdapter);
                        listview.setAdapter(newsAdapter);
                        if(listview.getFooterViewsCount()> 0&&"N".equals(isNextPageNews)){
                            listview.removeFooterView(load_more);
                        }else{
                            listview.addFooterView(load_more);
                        }
                    }
                    FincUtil.setVisibilityAndTextColor(FincProductDetailActivity.this,textView18,true,ima_gonggaoline,true);
                    FincUtil.setVisibilityAndTextColor(FincProductDetailActivity.this,textView19,false,ima_newsline,false);
//                    getNewsList(fundCode,"1","5");

                    break;
            }
        }
    }

    TextView finc_detail_head_tv1_value,fund_latest_value/*,product_place*/;
    /**
     * 初始化头部布局数据及底部按钮
     */
    private void initHeadViewData(){
     //   product_place = (TextView)findViewById(R.id.product_place);//香港产品
        finc_detail_head_tv1_value = (TextView)findViewById(R.id.finc_detail_head_tv1_value);
        fund_latest_value = (TextView)findViewById(R.id.fund_latest_value);
        // 基金名称和代码
        fundNameStr = StringUtil
                .valueOf1((String) fincControl.fundDetails
                        .get(Finc.FINC_FUNDNAME));
        fundCodeStr = StringUtil.valueOf1((String) fincControl.fundDetails
                .get(Finc.FINC_FUNDCODE));
        ((TextView) findViewById(R.id.finc_productName)).setText(fundNameStr);
        ((TextView) findViewById(R.id.finc_productCode)).setText("("+fundCodeStr+")");
//        ((TextView) findViewById(R.id.finc_productName_and_code)).setText(sb.toString());
        // 产品类型
//        String fnTypeStr = StringUtil.valueOf1((String) fincControl.fundDetails
//                .get(Finc.FINC_FNTYPE));
        if ("06".equals(fnTypeStr)||"01".equals(fnTypeStr)) { // 货币型基金
         //   product_place.setVisibility(View.VISIBLE);
            //七日年化收益率
            ((TextView)findViewById(R.id.finc_detail_head_tv1))
                    .setText("七日年化收益率");
            ((TextView)findViewById(R.id.finc_detail_head_tv2)).setText("万份收益");
            // 七日年化收益率
            String fundIncomeRatioStr = StringUtil
                    .valueOf1((String) fincControl.fundDetails
                            .get(Finc.SEVEN_DAY_YIELD));
            if ("-".equals(fundIncomeRatioStr)||StringUtil.isNullOrEmpty(fundIncomeRatioStr)) {
                finc_detail_head_tv1_value.setText("--");
            } else {
                finc_detail_head_tv1_value.setText(FincUtil.setCent(fundIncomeRatioStr));
                finc_detail_head_tv1_value.setTextColor(getResources().getColor(R.color.fonts_black));
                if(!StringUtil.isNullOrEmpty(fundIncomeRatioStr)&&Double.parseDouble(fundIncomeRatioStr)!= 0){
                    finc_detail_head_tv1_value.setTextColor(Double.parseDouble(fundIncomeRatioStr)>0?getResources().getColor(R.color.boc_text_color_red)
                            :getResources().getColor(R.color.boc_text_color_green));
                }
            }
            // 每万份基金单位收益
            String everytenthousandStr = StringUtil
                    .valueOf1((String) fincControl.fundDetails
                            .get(Finc.FINC_FUNDINCOMEUNIT));
            fund_latest_value.setText(FincUtil.setvalue(everytenthousandStr));
        } else { // 非货币型基金

            // 基金单位净值
            String netValueStr = (String) fincControl.fundDetails.get(Finc.FINC_NETPRICE);
            fund_latest_value.setText(StringUtil.isNullOrEmpty(netValueStr)?"--":StringUtil.parseStringPattern(netValueStr, 4));
            fund_latest_value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!StringUtil.isNullOrEmpty(fincControl.historyList)){
                        fincControl.historyList.clear();
                    }
                    if(!"06".equals(fnTypeStr)&&!"01".equals(fnTypeStr)){
                        if(!StringUtil.isNullOrEmpty(jzItemListFirst)){
                            fincControl.historyList = FincUtil.siFangListToList(jzItemListFirst);
                            Map map = new HashMap();
                            map.put("historyActivitTtypeFlag","1");
                            ActivityIntentTools.intentToActivityWithData(FincProductDetailActivity.this,FundLookHistoryValueActivity.class,map);
                        }
                    }
                }
            });
        }
        String endDate = (String) fincControl.fundDetails.get("endDate");
        if(!StringUtil.isNullOrEmpty(endDate)&&endDate.length()== 10){
            ((TextView)findViewById(R.id.finc_detail_head_tv2_value)).setText(endDate.substring(5,10));
        }else{
            ((TextView)findViewById(R.id.finc_detail_head_tv2_value)).setText(StringUtil.isNullOrEmpty(endDate)?"--":endDate);
        }
        String buystart="--";
        String isBuy = (String) fincControl.fundDetails.get("isBuy");
        String gIsBuy = (String) fincControl.fundDetails.get("gIsBuy");
        if("Y".equals(isBuy)||"Y".equals(gIsBuy)){
            buystart = (String) fincControl.fundDetails.get("orderLowLimit");
        }else{
            buystart = (String) fincControl.fundDetails.get("applyLowLimit");
        }
        ((TextView)findViewById(R.id.finc_detail_head_tv3_value)).setText(FincUtil.setvalue(StringUtil.parseStringCodePattern(
                currencyStr, buystart, 2)));
        if (canScheduleBuy) { // 可定投
            inversButton.setVisibility(View.VISIBLE);
        } else {
            inversButton.setVisibility(View.GONE);
        }

        if (canBuy) { // 可购买
            buyButton.setVisibility(View.VISIBLE);
        } else {
            buyButton.setVisibility(View.GONE);
        }
        }
    /**
     * 初始化赎回属性数据
     */
    private void initRedeemPropViewData() {
        // 最近可赎回日期
        String fincLatelyCanRansomDate = (String) fincControl.fundDetails
                .get(Finc.FINC_DATE_Lately);
        if ("Y".equals(fincControl.fundDetails.get(Finc.FINC_IS_SHORT_FUND))) {
            if (StringUtil.isNull(fincLatelyCanRansomDate)) {
                fincLatelyCanRansomDate = "--";
            }
            myContainerLayout.addView(createNewLabelTextView(R.string.finc_remit_attribute_tv1,fincLatelyCanRansomDate));
        }
        // 赎回下限
        String fincSellLowLimitColon = (String) fincControl.fundDetails
                .get(Finc.FINC_SELLLOWLIMIT);
        if (StringUtil.isNull(fincSellLowLimitColon)) {
            fincSellLowLimitColon = "--";
        } else {
            fincSellLowLimitColon = StringUtil.parseStringCodePattern(
                    currencyStr, fincSellLowLimitColon, 2);
        }
        myContainerLayout.addView(createNewLabelTextView(R.string.finc_remit_attribute_tv2,fincSellLowLimitColon));

        // 最低持有份额
        String fincMyFincHoldQutyLowLimit = (String) fincControl.fundDetails
                .get(Finc.FINC_HOLDLOWCOUNT);
        if (StringUtil.isNull(fincMyFincHoldQutyLowLimit)) {
            fincMyFincHoldQutyLowLimit = "--";
        } else {
            fincMyFincHoldQutyLowLimit = StringUtil.parseStringCodePattern(
                    currencyStr, fincMyFincHoldQutyLowLimit, 2);
        }
        myContainerLayout.addView(createNewLabelTextView(R.string.finc_remit_attribute_tv3,fincMyFincHoldQutyLowLimit));

        // 单日赎回上限
        String forexMyFincDayTopLimit = (String) fincControl.fundDetails
                .get(Finc.FINC_DAY_TOPLIMIT);
        forexMyFincDayTopLimit = StringUtil.parseStringCodePattern(currencyStr,
                forexMyFincDayTopLimit, 2);
        myContainerLayout.addView(createNewLabelTextView(R.string.finc_remit_attribute_tv4,FincUtil.setvalue(forexMyFincDayTopLimit)));
    }

    /**
     * 初始化购买属性数据
     */
    private void initBuyPropViewData() {
        // 首次认购下限
        String fincFundFirstBuyFloor = (String) fincControl.fundDetails
                .get(Finc.FINC_ORDERLOWLIMIT);
        if (StringUtil.isNull(fincFundFirstBuyFloor)) {
            fincFundFirstBuyFloor = "--";
        } else {
            fincFundFirstBuyFloor = StringUtil.parseStringCodePattern(
                    currencyStr, fincFundFirstBuyFloor, 2);
        }
        myContainerLayout.addView(createNewLabelTextView(R.string.finc_buy_attribute_tv1,FincUtil.setvalue(fincFundFirstBuyFloor)));
        // 追加认购下限
        String fincFundAddBuyFloor = (String) fincControl.fundDetails
                .get(Finc.FINC_BUY_ADD_LOW_LMT);
        if (StringUtil.isNull(fincFundAddBuyFloor)) {
            fincFundAddBuyFloor="--";
        } else {
            fincFundAddBuyFloor = StringUtil.parseStringCodePattern(
                    currencyStr, fincFundAddBuyFloor, 2);
        }
        myContainerLayout.addView(createNewLabelTextView(R.string.finc_buy_attribute_tv2,FincUtil.setvalue(fincFundAddBuyFloor)));
        // 申购下限
        String fincRansomFloor = (String) fincControl.fundDetails
                .get(Finc.I_APPLYLOWLIMIT);
        if (StringUtil.isNull(fincRansomFloor)) {
            fincRansomFloor = "-";
        } else {
            fincRansomFloor = StringUtil.parseStringCodePattern(currencyStr,
                    fincRansomFloor, 2);
        }
        myContainerLayout.addView(createNewLabelTextView(R.string.finc_buy_attribute_tv3,FincUtil.setvalue(fincRansomFloor)));

        // 定期定额申购下限
        String fincSchedubuyLimitColon = (String) fincControl.fundDetails
                .get(Finc.FINC_SCHEDULEAPPLYLOWLIMIT);
        if (StringUtil.isNull(fincSchedubuyLimitColon)) {
            fincSchedubuyLimitColon = "-";
        } else {
            fincSchedubuyLimitColon = StringUtil.parseStringCodePattern(
                    currencyStr, fincSchedubuyLimitColon, 2);
        }
        myContainerLayout.addView(createNewLabelTextView(R.string.finc_buy_attribute_tv4,FincUtil.setvalue(fincSchedubuyLimitColon)));

        // 单日购买上限
        String fincDayLimit = (String) fincControl.fundDetails
                .get(Finc.FINC_DAYMAXSUMBUY);
        fincDayLimit = StringUtil.parseStringCodePattern(currencyStr,
                fincDayLimit, 2);
        myContainerLayout.addView(createNewLabelTextView(R.string.finc_buy_attribute_tv5,FincUtil.setvalue(fincDayLimit)));
    }

    /**
     * 初始化产品属性数据
     */
    private void initProductPropViewData() {
        // 产品种类
        String productKindStr = StringUtil
                .valueOf1((String) fincControl.fundDetails
                        .get(Finc.COMBINQUERY_FNTKIND));
        if ("-".equals(productKindStr)) {
            product_attribute_mycontainerLayout.addView(createNewLabelTextView(R.string.finc_product_attribute_tv1,FincUtil.setvalue(productKindStr)));
        } else {
            product_attribute_mycontainerLayout.addView(createNewLabelTextView(R.string.finc_product_attribute_tv1,
                    StringUtil.isNullOrEmpty(productKindStr)?"--":(String) LocalData.fincFundTypeCodeToStr.get(productKindStr)));
        }
        // 基金公司
        product_attribute_mycontainerLayout.addView(createNewLabelTextView(R.string.finc_product_attribute_tv2,
                FincUtil.setvalue(StringUtil.valueOf1((String) fincControl.fundDetails.get(Finc.FINC_FUNDCOMPANYNAME)))));
        // 交易币种
        currencyStr = (String) fincControl.fundDetails.get(Finc.FINC_CURRENCY);
        String cashFlagCode = (String) fincControl.fundDetails.get(Finc.FINC_CASHFLAG);
        product_attribute_mycontainerLayout.addView(createNewLabelTextView(R.string.finc_product_attribute_tv3,
                FincUtil.setvalue(FincControl.fincCurrencyAndCashFlag(currencyStr, cashFlagCode))));
        // 收费方式
        String feeType = (String) fincControl.fundDetails.get(Finc.FINC_FEETYPE_REQ);
        product_attribute_mycontainerLayout.addView(createNewLabelTextView(R.string.finc_product_attribute_tv4,
                StringUtil.isNullOrEmpty(feeType)?"--":LocalData.fundfeeTypeCodeToStr.get(feeType)));
        // 默认分红方式
        if(StringUtil.isNullOrEmpty(bonusType)){
            bonusType = StringUtil.valueOf1((String) fincControl.fundDetails.get(Finc.FINC_DEFAULT_BONUS));
        }
 //       String defaultBonus = StringUtil.valueOf1((String) fincControl.fundDetails.get(Finc.FINC_DEFAULT_BONUS));
        product_attribute_mycontainerLayout.addView(createNewLabelTextView(R.string.finc_product_attribute_tv5,
                StringUtil.isNullOrEmpty(bonusType)?"--":(String) LocalData.bonusTypeMap.get(bonusType)));
        //产品成立日期
        String fundSetDate = (String) fincControl.fundDetails.get("fundSetDate");
        product_attribute_mycontainerLayout.addView(createNewLabelTextView(R.string.finc_product_attribute_tv6,
                StringUtil.isNullOrEmpty(fundSetDate)?"--":fundSetDate));
        //基金规模
        fund_scale_tv = createNewLabelTextView(R.string.finc_product_attribute_tv7,
                "--");
        product_attribute_mycontainerLayout.addView(fund_scale_tv);

        //风险评价
        String risklv = (String) fincControl.fundDetails.get("risklv");
        product_attribute_mycontainerLayout.addView(createNewLabelTextView(R.string.finc_product_attribute_tv8,
                StringUtil.isNullOrEmpty(risklv)?"--":(String) FincDataCenter.fincRisklvStr
                        .get(risklv)));
    }

    /**
     * 检查是否做了风险认证的回调处理
     *
     * @param resultObj
     */
    public void doCheckRequestPsnFundRiskEvaluationQueryResultCallback(
            Object resultObj) {
        super.doCheckRequestPsnFundRiskEvaluationQueryResultCallback(resultObj);
        if(fincControl.ifdorisk){
            switch (operation_flag) {
                case OPERATION_BUY: // 买入
                    startBuyActivity();
                    break;
                case OPERATION_INVERS: // 定投
                    startInversActivity();
                    break;
                case OPERATION_REDEEM://赎回
                    getFincFund(fundCodeStr);
   //                 startRedeemActivity();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isLogin = BaseDroidApp.getInstanse().isLogin();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
//            case ConstantGloble.ACTIVITY_REQUEST_LOGIN_CODE:
//                switch (resultCode) {
//                    case RESULT_OK:
//                        switch (operation_flag) {
//                            case OPERATION_NONE:
//                                ActivityTaskManager.getInstance().removeAllSecondActivity();
//                                Intent intent = new Intent(FincProductDetailActivity.this,FundPricesActivity.class);
//                                startActivity(intent);
//                                break;
//
//                            default:
//                                isLoginCallback = true ;
//                                BaseHttpEngine.showProgressDialog();
//                                doCheckRequestPsnInvestmentManageIsOpen();
//                                break;
//                        }
//                        break;
//
//                    default:
//                        break;
//                }
//                break;

            case ConstantGloble.ACTIVITY_REQUEST_MANAGE_CODE:// 开通理财服务
                switch (resultCode) {
                    case RESULT_OK:
                        fincControl.ifInvestMent = true;
                        if (!fincControl.ifhaveaccId) {// 如果还没有基金账户
                            getPopup();
                        }else{
                            BaseHttpEngine.showProgressDialog();
                            attentionFundQuery();
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
                        BaseHttpEngine.showProgressDialog();
                        attentionFundQuery();
                        break;

                    default:
                        fincControl.ifhaveaccId = false;
                        getPopup();
                        break;
                }
                break;
            case InvestConstant.FUNDRISK:// 基金风险评估
                switch (resultCode) {
                    case RESULT_OK:
                        fincControl.ifdorisk = true;
                        switch (operation_flag) {
                            case OPERATION_BUY: // 买入
                                startBuyActivity();
                                break;
                            case OPERATION_INVERS: // 定投
                                startInversActivity();
                                break;
                            case OPERATION_REDEEM://赎回
                                startRedeemActivity();
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        fincControl.ifdorisk = false;
                        getPopupForRisk();
                        break;
                }
                break;
            case 1://购买，定投，赎回等页面跳转
                switch (resultCode){
                    //有行情页面 完成后进入行情页面，反之进入详情页
                    case RESULT_OK:
                        if(!("1".equals(isFromOther))){
                            setResult(RESULT_OK);
                            finish();
                        }
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void doCheckRequestQueryInvtBindingInfoCallback(
            Object resultObj) {
        super.doCheckRequestQueryInvtBindingInfoCallback(resultObj);
        if(!fincControl.ifhaveaccId || !fincControl.ifInvestMent) return;
        if (fincControl.ifInvestMent && fincControl.ifhaveaccId) {
            attentionFundQuery();
        }

    }

    @Override
    public void attentionFundQueryCallback(Object resultObj) {
        super.attentionFundQueryCallback(resultObj);
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
                .getResult();
        if (!StringUtil.isNullOrEmpty(resultMap)) {
            if (!StringUtil.isNullOrEmpty(resultMap
                    .get(Finc.FINC_ATTENTIONQUERYLIST_ATTENTIONLIST))) {
                fincControl.attentionFundList = (List<Map<String, Object>>) resultMap
                        .get(Finc.FINC_ATTENTIONQUERYLIST_ATTENTIONLIST);
                for (Map<String, Object> map : fincControl.attentionFundList) {
                    // 已关注的基金
                    String fundCodeAttentioned = (String) map
                            .get(Finc.FINC_FUNDCODE);
                    // 当前选择的基金
                    String fundCodeCurrent = (String) fincControl.fundDetails
                            .get(Finc.FINC_FUNDCODE);
                    if (fundCodeAttentioned.equals(fundCodeCurrent)) {// 如果已关注
                        fincControl.setAttentionFlag(true);
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
        setAttentionIconAndText(fincControl.getAttentionFlag());

        // 重新查询登录后的基金详情
        getFincFund(fundCodeStr);
    }

    public void getFincFundCallback(Object resultObj) {
        super.attentionFundQueryCallback(resultObj);
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
                .getResult();
        BiiHttpEngine.dissMissProgressDialog();
        if (StringUtil.isNullOrEmpty(resultMap)) {
//            BaseDroidApp.getInstanse().showInfoMessageDialog(
//                    getString(R.string.acc_transferquery_null));
            MessageDialog.showMessageDialog(this,getString(R.string.acc_transferquery_null));
            return;
        }
        fincControl.fundDetails = resultMap;
        switch (operation_flag) {
            case OPERATION_REDEEM://赎回
                fincControl.fincFundBasicDetails=resultMap;
            default:
                break;
        }
        requestPsnFundQueryFundBalance(StringUtil.valueOf1((String) fincControl.fundDetails
                .get(Finc.FINC_FUNDCODE)));
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
        refreshHoldAndAttention();
        switch (operation_flag) {
            case OPERATION_ATTENTION: // 关注
                modifyAttentionStatus();
                break;
            case OPERATION_BUY: // 买入
                if(isLoginCallback){
                    /**基金买入，判断是否未风险评估*/
                    BaseHttpEngine.showProgressDialog();
                    doCheckRequestPsnFundRiskEvaluationQueryResult();
                }else{
                    startBuyActivity();
                }
                break;
            case OPERATION_INVERS: // 定投
                if(isLoginCallback){
                    /**基金定投，判断是否未风险评估*/
                    BaseHttpEngine.showProgressDialog();
                    doCheckRequestPsnFundRiskEvaluationQueryResult();
                }else{
                    startInversActivity();
                }
                break;
            case OPERATION_REDEEM://赎回
                if(isLoginCallback){
                    /**基金定投，判断是否未风险评估*/
                    BaseHttpEngine.showProgressDialog();
                    doCheckRequestPsnFundRiskEvaluationQueryResult();
                }else{
                    startRedeemActivity();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 登陆后刷新关注按钮和持仓
     */
    private void refreshHoldAndAttention(){
        if(isLogin){
            fundAttentionImage.setVisibility(View.VISIBLE);
        }
        if(isLogin&&!StringUtil.isNullOrEmpty(fincControl.fundBalancList)){
            hold_info_layout.setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.finc_hold_tv1)).setText(StringUtil.parseStringPattern((String)fincControl.
                    fundBalancList.get(0).get("currentCapitalisation"), 2));
            ((TextView)findViewById(R.id.finc_hold_tv2)).setText(StringUtil.parseStringPattern((String)fincControl.
                    fundBalancList.get(0).get("totalBalance"), 2));
            bonusType = (String)fincControl.fundBalancList.get(0).get("bonusType");
            canRedeem = StringUtil.parseStrToBoolean((String) fincControl.fundDetails.get("isSale"));
            if (canRedeem) { // 可赎回
                redeemButton.setVisibility(View.VISIBLE);
            } else {
                redeemButton.setVisibility(View.GONE);
            }
            buyButton.setText("继续购买");
        }
        initHeadViewData();
        myContainerLayout.removeAllViews();
        product_attribute_mycontainerLayout.removeAllViews();
        initProductPropViewData();
        initBuyPropViewData();
    }

    @Override
    public boolean httpRequestCallBackPre(Object resultObj) {
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
            // 随机数获取异常
            if (Finc.FINC_ATTENTIONQUERYLIST.equals(biiResponseBody.getMethod())) {
                if (((BiiResponse) resultObj).isBiiexception()) {// 代表返回数据异常
                    FincControl.getInstance().setAttentionFlag(false);
                    setAttentionIconAndText(false);
                    BaseHttpEngine.dissMissProgressDialog();
                    return true;
                }
                return false;// 没有异常
            }else if("PsnFundQueryFundBalance".equals(biiResponseBody.getMethod())) {
                return false;
            } else {
                return super.httpRequestCallBackPre(resultObj);
            }
        }
        return super.httpRequestCallBackPre(resultObj);
    }

    /**
     * 3.2 基金详情接口（基本信息）
     * @param fundId 基金Id或code
     */
    private void getQueryFundBasicDetail(String fundId){
        QueryFundBasicDetailRequestParams requestParams = new QueryFundBasicDetailRequestParams(fundId);
        HttpHelp h= HttpHelp.getInstance();
        h.postHttpFromSF(this,requestParams);
        h.setHttpErrorCallBack(null);
        h.setHttpResponseCallBack(new IHttpResponseCallBack() {
            @Override
            public boolean responseCallBack(String response, Object extendParams) {
                QueryFundBasicDetailResponseData data = GsonTools.fromJson(response,QueryFundBasicDetailResponseData.class);
                QueryFundBasicDetailResult body = data.getBody();
                if(!StringUtil.isNullOrEmpty(body)&&!StringUtil.isNullOrEmpty(fincControl.fundDetails)){
                    fincControl.fundDetails.put("siFangFundDetail",body);
                    setSifangData();
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

    /**
     * 查询四方行情数据
     * @param multipleFundBakCode 基金代码
     */
    private void getQueryMultipleFund(String multipleFundBakCode){
        QueryMultipleFundRequestParams queryMultipleFund = new QueryMultipleFundRequestParams(multipleFundBakCode);
        HttpHelp h= HttpHelp.getInstance();
        h.postHttpFromSF(this,queryMultipleFund);
        h.setHttpErrorCallBack(null);
        h.setHttpResponseCallBack(new IHttpResponseCallBack() {
            @Override
            public boolean responseCallBack(String response, Object extendParams) {
                QueryMultipleFundResponseData data = GsonTools.fromJson(response, QueryMultipleFundResponseData.class);
                QueryMultipleFundResult body = data.getBody();
                List<QueryMultipleFundResult.QueryMultipleFundItem> itemList = body.getItems();
                if(!StringUtil.isNullOrEmpty(itemList)){
                    fincControl.fundListSelect = new HashMap<String, Object>();
                    for(QueryMultipleFundResult.QueryMultipleFundItem item : itemList){
                        fincControl.fundListSelect.put("currPercentDiff",item.getCurrPercentDiff());
                        fincControl.fundListSelect.put("changeOfMonth",item.getChangeOfMonth());
                        fincControl.fundListSelect.put("changeOfQuarter",item.getChangeOfQuarter());
                        fincControl.fundListSelect.put("changeOfHalfYear",item.getChangeOfHalfYear());
                        fincControl.fundListSelect.put("changeOfYear",item.getChangeOfYear());
                        fincControl.fundListSelect.put("thisYearPriceChange",item.getThisYearPriceChange());
                        fincControl.fundListSelect.put("changeOfWeek",item.getChangeOfWeek());
                    }
                }
                    if("06".equals(fnTypeStr)||"01".equals(fnTypeStr)){
                        month_up_or_down_value.setText(StringUtil.isNullOrEmpty(fincControl.fundListSelect)?"--":FincUtil.setCent2((String)fincControl.fundListSelect.get("changeOfWeek")));
                    }else {
                        month_up_or_down_value.setText(StringUtil.isNullOrEmpty(fincControl.fundListSelect)?"--":FincUtil.setCent2((String)fincControl.fundListSelect.get("changeOfMonth")));
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

    /**
     * 四方数据返回后刷新页面四方数据
     */
    private void setSifangData(){
        fundBasicDetailResult = (QueryFundBasicDetailResult) fincControl.fundDetails.get("siFangFundDetail");
        //日涨幅
        String daynetvaluerateStr = fundBasicDetailResult.getCurrPercentDiff();
        if (!"06".equals(fnTypeStr)&&!"01".equals(fnTypeStr)) { // 货币型基金
            if ("-".equals(daynetvaluerateStr)||StringUtil.isNullOrEmpty(daynetvaluerateStr)) {
                finc_detail_head_tv1_value.setText("--");
            } else {
                if (daynetvaluerateStr.contains("%")) {
                    finc_detail_head_tv1_value.setText(daynetvaluerateStr);
                } else {
                    finc_detail_head_tv1_value.setText(FincUtil.setCent(daynetvaluerateStr));
                    finc_detail_head_tv1_value.setTextColor(getResources().getColor(R.color.fonts_black));
                    if(!StringUtil.isNullOrEmpty(daynetvaluerateStr)&&Double.parseDouble(daynetvaluerateStr)!= 0){
                        finc_detail_head_tv1_value.setTextColor(Double.parseDouble(daynetvaluerateStr)>0?getResources().getColor(R.color.boc_text_color_red)
                                :getResources().getColor(R.color.boc_text_color_green));
                    }
                }
            }
        }
        String feeText = "";//申购费率
        if(!StringUtil.isNullOrEmpty(fundBasicDetailResult)&&!StringUtil.isNullOrEmpty(fundBasicDetailResult.getFeeRatioList())){
            List<QueryFundBasicDetailResult.FeeRatioListItem> feeRatioList = fundBasicDetailResult.getFeeRatioList();
            String feeTextMax = "";
            double feeTextMaxTemp = 0;
            //循环取出申购手续费的最大值
            for(QueryFundBasicDetailResult.FeeRatioListItem item : feeRatioList){
                String feeRadio = item.getFeeRatio();
                if(!StringUtil.isNullOrEmpty(item.getStrExpType())&&"申购费".equals(item.getStrExpType())){
                    if(!StringUtil.isNullOrEmpty(feeRadio)&&Double.parseDouble(feeRadio)>=feeTextMaxTemp){
                        feeTextMaxTemp = Double.parseDouble(feeRadio);
                        feeTextMax = feeRadio;
                    }
                }
            }
            feeText = feeTextMax;
        }
        if(StringUtil.isNullOrEmpty(feeText)){//为空时不显示申购费率
            findViewById(R.id.finc_fee_buy_layout).setVisibility(View.GONE);
        }else{
            //判断是否含有中文 含有中文或者=0时不显示申购费率
            if(CommPublicTools.getLength(feeText)>feeText.length()||Double.parseDouble(feeText)==0){
                findViewById(R.id.finc_fee_buy_layout).setVisibility(View.GONE);
            }else{
                findViewById(R.id.finc_fee_buy_layout).setVisibility(View.VISIBLE);
                buy_fee_tv.setText(feeText+"%");
                //打折后费率
                double discountFee = Double.parseDouble(feeText)*0.6;
                ((TextView) findViewById(R.id.buy_fee_tv2)).setText(StringUtil.append2Decimals(discountFee+"",2)+"%");
            }
        }
        if(!StringUtil.isNullOrEmpty(fundBasicDetailResult)){//基金规模
            fund_scale_tv.setValueText(FincUtil.setvalue(StringUtil.parseStringPattern(fundBasicDetailResult.getFundScale(),2)));
        }
        //银河评级
        if(!StringUtil.isNullOrEmpty(fundBasicDetailResult.getGradeOrg())){
            fund_grade_tv = (TextView) findViewById(R.id.grade_org);
            fund_grade_tv.setVisibility(View.VISIBLE);
            fund_grade_tv.setText(fundBasicDetailResult.getGradeOrg());
            StarBar starBar = (StarBar) findViewById(R.id.starBar);
            //starBar.setVisibility(View.VISIBLE);
            findViewById(R.id.satrBar_layout).setVisibility(View.VISIBLE);
            String starNum = fundBasicDetailResult.getGradeLevel();
            if(!StringUtil.isNullOrEmpty(starNum)){
                starBar.setStarMark(Float.parseFloat(starNum));
            }
//            fund_grade_tv = createNewLabelTextView(R.string.finc_product_attribute_tv9,
//                    fundBasicDetailResult.getGradeLevel());
//            fund_grade_tv.setLabelText(fundBasicDetailResult.getGradeOrg());
//            product_attribute_mycontainerLayout.addView(fund_grade_tv);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fincControl.fundListSelect = null;
        fincControl.fundDetails = null;
    }

    /**
     * 折线图切换时相关处理
     */
    private void setKLineChange(){
        requestEngine.cancel();
        queryKTendencyView.resetData();
    }
}
