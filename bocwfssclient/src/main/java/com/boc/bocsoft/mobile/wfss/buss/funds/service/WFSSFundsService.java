package com.boc.bocsoft.mobile.wfss.buss.funds.service;

import com.boc.bocsoft.mobile.wfss.buss.funds.model.fundnotice.WFSSFundnoticeParams;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.fundnotice.WFSSFundnoticeResponse;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.fundnotice.WFSSFundnoticeResult;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.jztendency.WFSSJzTendencyParams;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.jztendency.WFSSJzTendencyResponse;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.jztendency.WFSSJzTendencyResult;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.ljyieldratetendency.WFSSLjYieldRateTendencyParams;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.ljyieldratetendency.WFSSLjYieldRateTendencyResponse;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.ljyieldratetendency.WFSSLjYieldRateTendencyResult;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.newscontent.WFSSNewsContentParams;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.newscontent.WFSSNewsContentResponse;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.newscontent.WFSSNewsContentResult;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.newslist.WFSSNewsListParams;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.newslist.WFSSNewsListResponse;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.newslist.WFSSNewsListResult;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.queryfundbasicdetail.WFSSQueryFundBasicDetailParams;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.queryfundbasicdetail.WFSSQueryFundBasicDetailResponse;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.queryfundbasicdetail.WFSSQueryFundBasicDetailResult;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.querymultiplefund.WFSSQueryMultipleFundParams;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.querymultiplefund.WFSSQueryMultipleFundResponse;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.querymultiplefund.WFSSQueryMultipleFundResult;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.ranktendency.WFSSRankTendencyParams;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.ranktendency.WFSSRankTendencyResponse;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.ranktendency.WFSSRankTendencyResult;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.yieldofwanfentendency.WFSSYieldOfWanFenTendencyParams;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.yieldofwanfentendency.WFSSYieldOfWanFenTendencyResponse;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.yieldofwanfentendency.WFSSYieldOfWanFenTendencyResult;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.yieldofweektendency.WFSSYieldOfWeekTendencyParams;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.yieldofweektendency.WFSSYieldOfWeekTendencyResponse;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.yieldofweektendency.WFSSYieldOfWeekTendencyResult;
import com.boc.bocsoft.mobile.wfss.common.client.WFSSClient;

import rx.Observable;

/**
 * 基金service
 * Created by gwluo on 2016/10/24.
 */

public class WFSSFundsService {

    // 查询基金信息
    public static final String URL_QUERY_FUND_BASIC_DETAIL = "fund/queryFundBasicDetail";
    public static final String URL_QUERY_MULTIPLE_FUND = "fund/queryMultipleFund";
    public static final String URL_JZ_TENDENCY = "fund/jzTendency";
    public static final String URL_LJYIELD_RATE_TENDENCY = "fund/ljYieldRateTendency";
    public static final String URL_RANK_TENDENCY = "fund/rankTendency";
    public static final String URL_YIELD_OFWEEK_TENDENCY = "fund/yieldOfWeekTendency";
    public static final String URL_FUNDNOTICE = "fund/fundnotice";
    public static final String URL_NEWS_LIST = "fund/newsList";
    public static final String URL_NEWS_CONTENT = "fund/newsContent";
    public static final String URL_YIELD_OF_WANFEN_TENDENCY = "fund/yieldOfWanFenTendency";

    /**
     * 3.1 基金列表查询
     */
    public Observable<WFSSQueryMultipleFundResult> queryMultipleFund(WFSSQueryMultipleFundParams params) {
        return WFSSClient.instance.post(URL_QUERY_MULTIPLE_FUND, params, WFSSQueryMultipleFundResponse.class);
    }

    /**
     * 3.2 基金详情接口（基本信息）
     */
    public Observable<WFSSQueryFundBasicDetailResult> queryFundBasicDetail(WFSSQueryFundBasicDetailParams params) {
        return WFSSClient.instance.post(URL_QUERY_FUND_BASIC_DETAIL, params, WFSSQueryFundBasicDetailResponse.class);
    }

    /**
     * 3.3 基金详情接口2（净值走势-趋势图、历史净值）
     */
    public Observable<WFSSJzTendencyResult> jzTendency(WFSSJzTendencyParams params) {
        return WFSSClient.instance.post(URL_JZ_TENDENCY, params, WFSSJzTendencyResponse.class);
    }

    /**
     * 3.4 基金详情接口3（基金累计收益率--趋势图、历史累计收益率）
     */
    public Observable<WFSSLjYieldRateTendencyResult> ljYieldRateTendency(WFSSLjYieldRateTendencyParams params) {
        return WFSSClient.instance.post(URL_LJYIELD_RATE_TENDENCY, params, WFSSLjYieldRateTendencyResponse.class);
    }

    /**
     * 3.5 基金详情接口4（排名变化--趋势图、历史排名变化)
     */
    public Observable<WFSSRankTendencyResult> rankTendency(WFSSRankTendencyParams params) {
        return WFSSClient.instance.post(URL_RANK_TENDENCY, params, WFSSRankTendencyResponse.class);
    }

    /**
     * 3.6 基金详情接口5（七日年化收益率--趋势图、历史)
     */
    public Observable<WFSSYieldOfWeekTendencyResult> yieldOfWeekTendency(WFSSYieldOfWeekTendencyParams params) {
        return WFSSClient.instance.post(URL_YIELD_OFWEEK_TENDENCY, params, WFSSYieldOfWeekTendencyResponse.class);
    }

    /**
     * 3.7 基金详情接口7（基金公告列表，分页)
     */
    public Observable<WFSSFundnoticeResult> fundnotice(WFSSFundnoticeParams params) {
        return WFSSClient.instance.post(URL_FUNDNOTICE, params, WFSSFundnoticeResponse.class);
    }

    /**
     * 3.8 基金详情接口8（基金新闻列表，分页)
     */
    public Observable<WFSSNewsListResult> newsList(WFSSNewsListParams params) {
        return WFSSClient.instance.post(URL_NEWS_LIST, params, WFSSNewsListResponse.class);
    }

    /**
     * 3.9 基金详情接口9（基金新闻内容)
     */
    public Observable<WFSSNewsContentResult> newsContent(WFSSNewsContentParams params) {
        return WFSSClient.instance.post(URL_NEWS_CONTENT, params, WFSSNewsContentResponse.class);
    }

    /**
     * 3.10 基金详情接口10（万份收益率--趋势图、历史)
     */
    public Observable<WFSSYieldOfWanFenTendencyResult> yieldOfWanFenTendency(WFSSYieldOfWanFenTendencyParams params) {
        return WFSSClient.instance.post(URL_YIELD_OF_WANFEN_TENDENCY, params, WFSSYieldOfWanFenTendencyResponse.class);
    }
}
