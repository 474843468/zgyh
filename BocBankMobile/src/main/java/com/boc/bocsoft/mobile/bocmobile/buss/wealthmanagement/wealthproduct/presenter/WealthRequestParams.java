package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.presenter;

import com.boc.bocsoft.mobile.bii.bus.financing.model.PsnXadProductQueryOutlay.PsnXadProductQueryOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.financing.model.PsnXpadProductDetailQueryOutlay.PsnXpadProductDetailQueryOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductDetailQuery.PsnXpadProductDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductInvestTreatyQuery.PsnXpadProductInvestTreatyQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductListQuery.PsnXpadProductListQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProfitCount.PsnXpadProfitCountParams;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.WealthConst;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.DetailsRequestBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthProfitBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthProductFragment;

/**
 * 封装理财接口参数--请求
 * Created by liuweidong on 2016/10/24.
 */
public class WealthRequestParams {
    /**
     * 查询理财产品列表（登录前）
     *
     * @return
     */
    public static PsnXadProductQueryOutlayParams buildPsnXadProductQueryOutlayParams(int index, String prodCode, boolean isSearch) {
        WealthViewModel viewModel = WealthProductFragment.getInstance().getViewModel();
        viewModel.setSearch(isSearch);
        viewModel.setLoginBeforeI(true);
        PsnXadProductQueryOutlayParams params = new PsnXadProductQueryOutlayParams();
        if (isSearch) {
            params.setProductSta(WealthConst.ALL_0);// 产品销售状态（全部）
            params.setProductCurCode("000");
        } else {
            params.setProductSta(WealthConst.PRODUCT_STA_1);// 产品销售状态（在售）
            params.setProductCurCode(viewModel.getProductCurCode());// 币种
        }
        params.setPageSize(String.valueOf(ApplicationConst.WEALTH_PAGE_SIZE));
        params.setProductType(WealthConst.ALL_0);// 产品属性分类（全部）
        params.setProductKind(WealthConst.PRODUCT_KIND_0);// 产品性质
        params.setIsLockPeriod(WealthConst.YES_1);// 是否支持业绩基准产品查询
        /*页面选择的数据*/
        params.setProductCode(prodCode);// 产品代码
        params.setIssueType(viewModel.getIssueType());// 产品类型
        params.setProdTimeLimit("0");// 产品期限（月）
        params.setDayTerm(viewModel.getDayTerm());// 产品期限（天）
        params.setProductRiskType(viewModel.getProductRiskType());// 收益类型
        params.setProRisk(viewModel.getProRisk());// 风险等级
        if (viewModel.isFirst()) {// 第一次进入列表默认排序方式
//            viewModel.setFirst(false);
            params.setSortFlag("1");// 排序方式 正序或倒序
            params.setSortSite("0");// 排序字段
        } else {
            params.setSortFlag(viewModel.getSortFlag());// 排序方式 正序或倒序
            params.setSortSite(viewModel.getSortType());// 排序字段
        }
        params.setCurrentIndex("" + index);
        String refresh = "true";
        if (index == 0) {
            refresh = "true";
        } else {
            refresh = "false";
        }
        params.set_refresh(refresh);
        return params;
    }

    /**
     * 查询理财产品列表（登录后）
     *
     * @return
     */
    public static PsnXpadProductListQueryParams buildPsnXpadProductListQueryParams(int index, String prodCode, boolean isSearch) {
        WealthViewModel viewModel = WealthProductFragment.getInstance().getViewModel();
        viewModel.setSearch(isSearch);
        viewModel.setLoginBeforeI(false);
        PsnXpadProductListQueryParams params = new PsnXpadProductListQueryParams();
        if (isSearch) {
            params.setXpadStatus(WealthConst.ALL_0);
            params.setProductCurCode("000");// 币种
        } else {
            params.setXpadStatus(WealthConst.PRODUCT_STA_1);// 产品销售状态（在售）
            params.setProductCurCode(viewModel.getProductCurCode());// 币种
        }
        params.setPageSize(String.valueOf(ApplicationConst.WEALTH_PAGE_SIZE));
        params.setProductKind(WealthConst.ALL_0);// 产品性质（全部）
        params.setIsLockPeriod(WealthConst.YES_1);// 是否支持业绩基准产品查询（支持）
        /*页面选择的数据*/
        params.setProductCode(prodCode);// 产品代码
        if (viewModel.getAccountBean() != null) {
            params.setAccountId(viewModel.getAccountBean().getAccountId());// 账户ID
        }
        params.setIssueType(viewModel.getIssueType());// 产品类型
        params.setProdTimeLimit("0");// 产品期限（月）
        params.setDayTerm(viewModel.getDayTerm());// 产品期限（天）
        params.setProductRiskType(viewModel.getProductRiskType());// 收益类型
        params.setProRisk(viewModel.getProRisk());// 风险等级
        if (viewModel.isFirst()) {
//            viewModel.setFirst(false);
            params.setSortFlag("1");// 排序方式 正序或倒序
            params.setSortType("0");// 排序字段
        } else {
            params.setSortFlag(viewModel.getSortFlag());// 排序方式 正序或倒序
            params.setSortType(viewModel.getSortType());// 排序字段
        }
        params.setCurrentIndex("" + index);
        String refresh = "true";
        if (index == 0) {
            refresh = "true";
        } else {
            refresh = "false";
        }
        params.set_refresh(refresh);
        return params;
    }

    /**
     * 查询产品详情（登录前）
     *
     * @return
     */
    public static PsnXpadProductDetailQueryOutlayParams buildPsnXpadProductDetailQueryOutlayParams(DetailsRequestBean requestBean) {
        PsnXpadProductDetailQueryOutlayParams params = new PsnXpadProductDetailQueryOutlayParams();
        params.setProductCode(requestBean.getProdCode());// 产品代码
        params.setProductKind(requestBean.getProdKind());// 产品性质
        params.setIbknum("");// 省行联行号
        return params;
    }


    /**
     * 查询产品详情（登录后）
     *
     * @return
     */
    public static PsnXpadProductDetailQueryParams buildPsnXpadProductDetailQueryParams(DetailsRequestBean requestBean) {
        PsnXpadProductDetailQueryParams params = new PsnXpadProductDetailQueryParams();
        params.setProductCode(requestBean.getProdCode());// 产品代码
        params.setProductKind(requestBean.getProdKind());// 产品性质
        params.setIbknum(requestBean.getIbknum());// 省行联行号
        return params;
    }

    /**
     * 收益试算
     *
     * @return
     */
    public static PsnXpadProfitCountParams buildPsnXpadProfitCountParams(WealthProfitBean profitBean) {
        PsnXpadProfitCountParams params = new PsnXpadProfitCountParams();
        params.setProId(profitBean.getProId());// 产品代码
        params.setExyield(profitBean.getExyield());// 预计年收益率（%）
        params.setDayTerm(profitBean.getDayTerm());// 产品期限（天数）
        params.setPuramt(profitBean.getPuramt());// 投资金额
        params.setTotalPeriod(profitBean.getTotalPeriod());// 购买期数
        params.setCommonExyield(profitBean.getCommonExyield());
        params.setCommonDayTerm(profitBean.getCommonDayTerm());
        params.setCommonPurAmt(profitBean.getCommonPurAmt());
        params.setOpt("1");// 操作类型
        return params;
    }

    /**
     * 产品投资协议查询
     *
     * @return
     */
    public static PsnXpadProductInvestTreatyQueryParams buildProductInvestQueryParams(String accountID, String prodCode) {
        PsnXpadProductInvestTreatyQueryParams params = new PsnXpadProductInvestTreatyQueryParams();
        params.setAccountId(accountID);// 账户ID
        params.setProId(prodCode);// 产品代码
        params.setAgrType("0");// 协议类型 0：全部1：智能投资2：定时定额投资3：周期滚续投资4：余额理财投资
        params.setPageSize(ApplicationConst.WEALTH_PAGE_SIZE + "");
        params.setCurrentIndex("0");
        params.setInstType("0");// 投资方式
        params.set_refresh("true");
        return params;
    }
}
