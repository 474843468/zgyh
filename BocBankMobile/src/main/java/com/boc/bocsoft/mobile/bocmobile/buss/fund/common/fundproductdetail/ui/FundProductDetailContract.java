package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.ui;

import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundAttentionAdd.PsnFundAttentionAddParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundAttentionAdd.PsnFundAttentionAddResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundAttentionCancel.PsnFundAttentionCancelParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundAttentionCancel.PsnFundAttentionCancelResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundAttentionQueryList.PsnFundAttentionQueryListParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundAttentionQueryList.PsnFundAttentionQueryListResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundDetailQueryOutlay.PsnFundDetailQueryOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundDetailQueryOutlay.PsnFundDetailQueryOutlayResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.BIFundDetailParamsViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.BIFundDetailResultViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.FundNewsListViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.FundNoticeViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.JzTendencyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.RankTendencyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.WFSSFundBasicDetailParamsViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.WFSSFundBasicDetailResultViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.YieldOfWanFenTendencyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.YieldOfWeekTendencyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model.YieldRateTendencyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.ui.FundUserContract;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * 基金详情页
 * Created by liuzc on 2016/11/25.
 */
public class FundProductDetailContract {

    public interface View extends FundUserContract.View {
        //登陆前基金详情查询成功
        void queryWFSSFundDetailSuccess(WFSSFundBasicDetailResultViewModel result);
        //登陆前基金详情查询失败
        void queryWFSSFundDetailFail(BiiResultErrorException biiResultErrorException);

        //登录前从BI获取基金详情成功
        void queryBiFundDetailNLogSuccess(PsnFundDetailQueryOutlayResult result);
        //登录前从BI获取基金详情失败
        void queryBiFundDetailNLogFail(BiiResultErrorException biiResultErrorException);

        //登陆后从BI获取基金详情成功
        void getBiFundDetailLoginSuccess(BIFundDetailResultViewModel result);
        //登陆后从BI获取基金详情失败
        void getBiFundDetailLoginFail(BiiResultErrorException biiResultErrorException);

        //查询基金净值走势成功
        void queryJzTendencySuccss(JzTendencyViewModel result);
        //查询基金净值走势失败
        void queryJzTendencyFail(BiiResultErrorException biiResultErrorException);

        //查询基金累计收益率走势成功
        void queryYieldRateTendencySuccess(YieldRateTendencyViewModel result);
        //查询基金累计收益率走势失败
        void queryYieldRateTendencyFail(BiiResultErrorException biiResultErrorException);

        //查看排名变化成功
        void queryRrankTendencySuccess(RankTendencyViewModel result);
        //查看排名变化失败
        void queryRrankTendencyFail(BiiResultErrorException biiResultErrorException);

        //查看七日年化收益率成功
        void queryYieldOfWeekTendencySuccess(YieldOfWeekTendencyViewModel result);
        //查看七日年化收益率失败
        void queryYieldOfWeekTendencyFail(BiiResultErrorException biiResultErrorException);

        //查看万份收益率成功
        void queryYieldOfWanFenTendencySuccess(YieldOfWanFenTendencyViewModel result);
        //查看万份收益率失败
        void queryYieldOfWanFenTendencyFail(BiiResultErrorException biiResultErrorException);

        //查询公告成功
        void queryFundNoticesSuccess(FundNoticeViewModel result);
        //查询公告失败
        void queryFundNoticesFail(BiiResultErrorException biiResultErrorException);

        //查询新闻列表成功
        void queryNewsListSuccess(FundNewsListViewModel result);
        //查询新闻列表失败
        void queryNewsListFail(BiiResultErrorException biiResultErrorException);

        //查询关注基金列表结果
        void queryFundAttentionListSuccess(PsnFundAttentionQueryListResult result);
        void queryFundAttentionListFail(BiiResultErrorException biiResultErrorException);

        //增加基金关注结果
        void addFundAttentionSuccess(PsnFundAttentionAddResult result);
        void addFundAttentionFail(BiiResultErrorException biiResultErrorException);

        //取消基金关注结果
        void cancelFundAttentionSuccess(PsnFundAttentionCancelResult result);
        void cancelFundAttentionFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends FundUserContract.Presenter {
        /**
         * 从WFSS获取基金详情数据
         */
        void queryWFSSFundDetail(WFSSFundBasicDetailParamsViewModel model);

        /**
         * 登录前从BI获取基金详情数据
         * @param params
         */
        void queryBiFundDetailNLog(PsnFundDetailQueryOutlayParams params);

        /**
         * 登陆后基金详情查询-从BI获取
         * @param model
         */
        void getBiFundDetailLogin(BIFundDetailParamsViewModel model);

        /**
         * 查询基金净值走势
         * @param params
         */
        void queryJzTendency(JzTendencyViewModel params);

        /**
         * 查询基金累计收益率走势
         * @param params
         */
        void queryYieldRateTendency(YieldRateTendencyViewModel params);

        /**
         * 查看排名变化
         */
        void queryRrankTendency(RankTendencyViewModel params);

        /**
         * 查看七日年化收益率
         */
        void queryYieldOfWeekTendency(YieldOfWeekTendencyViewModel params);

        /**
         * 查看万份收益率走势
         */
        void queryYieldOfWanFenTendency(YieldOfWanFenTendencyViewModel params);

        /**
         * 查询公告内容
         * @param params
         */
        void queryFundNotices(FundNoticeViewModel params);

        /**
         * 查询新闻列表
         * @param params
         */
        void queryFundNewsList(FundNewsListViewModel params);

        /**
         *查询关注基金列表
         */
        void queryFundAttentionList(PsnFundAttentionQueryListParams params);

        /**
         * 增加基金关注
         * @param params
         */
        void addFundAttention(PsnFundAttentionAddParams params);

        /**
         * 删除基金关注
         * @param params
         */
        void cancelFundAttention(PsnFundAttentionCancelParams params);

    }
}
