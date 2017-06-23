package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.presenter;

import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAptitudeTreatyApplyVerify.PsnXpadAptitudeTreatyApplyVerifyParam;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductListQuery.PsnXpadProductListQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadSignResult.PsnXpadSignResultParam;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.ProtocolModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.WealthConst;

/**
 * 封装投资协议申请接口参数--请求
 * Created by liuweidong on 2016/11/6.
 */
public class BuildRequestParams {
    /**
     * 智能协议申请预交易
     *
     * @return
     */
    public static PsnXpadAptitudeTreatyApplyVerifyParam buildPsnXpadAptitudeTreatyApplyVerifyParams(ProtocolModel viewModel) {
        PsnXpadAptitudeTreatyApplyVerifyParam params = new PsnXpadAptitudeTreatyApplyVerifyParam();
        params.setAccountId(viewModel.getAccountList().get(0).getAccountId());
        params.setAgrCode(viewModel.getAgrCode());// 产品协议编号
        params.setAmountType(viewModel.getAmountType());// 投资金额模式
        params.setAmount(viewModel.getAmount());// 单期购买金额/单次购买金额
        params.setMinAmount(viewModel.getMinAmount());// 账户保留余额
        params.setMaxAmount(viewModel.getMaxAmount());// 最大购买金额
        params.setUnit(viewModel.getUnit());// 赎回份额
        params.setIsControl(viewModel.getIsControl());// 是否不限期
        params.setTotalPeriod(viewModel.getTotalPeriod());// 购买期数/赎回期数
        params.setCharCode(viewModel.getCharCode());// 钞汇类型
        return params;
    }


    /**
     * 周期滚续投资 结果请求
     *
     * @return
     */
    public static PsnXpadSignResultParam buildPsnXpadSignResultParam(ProtocolModel viewModel) {
        PsnXpadSignResultParam param = new PsnXpadSignResultParam();
        param.setAccountId(viewModel.getAccountId());
        param.setSerialName(viewModel.getSerialName());
        param.setSerialCode(viewModel.getSerialCode());
        param.setXpadCashRemit(viewModel.getXpadCashRemit());
        param.setTotalPeriod(viewModel.getTotalPeriod());
        param.setAmountTypeCode(viewModel.getAmountTypeCode());
        param.setCurCode(viewModel.getCurCode());
        param.setBaseAmount(viewModel.getBaseAmount());
        param.setMinAmount(viewModel.getMinAmount());
        param.setMaxAmount(viewModel.getMaxAmount());
        param.setDealCode(viewModel.getDealCode());// 理财推荐
        return param;
    }


    public static PsnXpadProductListQueryParams buildProductListQueryParams(ProtocolModel viewModel) {
        PsnXpadProductListQueryParams params = new PsnXpadProductListQueryParams();
        params.setProductRiskType("0");
        params.setProductKind(viewModel.getProductKind());
        params.setProductCurCode(viewModel.getCurCode());
        params.setPageSize(String.valueOf(ApplicationConst.WEALTH_PAGE_SIZE));
        params.setIsLockPeriod(WealthConst.YES_1);// 是否支持业绩基准产品查询（支持）
        params.setAccountId(viewModel.getAccountList().get(0).getAccountId());// 账户ID
        params.setXpadStatus("1");
        params.setIssueType("0");// 产品类型
        params.setProdTimeLimit("0");// 产品期限（月）
        params.setDayTerm("0");// 产品期限（天）
        params.setProRisk("0");// 风险等级
        params.setSortFlag("0");// 排序方式
        params.setSortType("0");// 排序条件
        params.setCurrentIndex("0");
        params.set_refresh("true");
        return params;
    }
}
