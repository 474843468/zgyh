package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.presenter;

import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAptitudeTreatyApplyVerify.PsnXpadAptitudeTreatyApplyVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAptitudeTreatyDetailQuery.PsnXpadAptitudeTreatyDetailQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadProductListQuery.PsnXpadProductListQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryRiskMatch.PsnXpadQueryRiskMatchResult;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.ProtocolIntelligentConfirmBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.ProtocolIntelligentDetailsBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.ProtocolModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.ArrayList;

/**
 * 封装投资协议申请接口参数--响应
 * Created by liuweidong on 2016/11/6.
 */
public class BuildResponseResult {
    /**
     * 智能协议详情
     *
     * @param result
     * @return
     */
    public static ProtocolIntelligentDetailsBean copyResultToViewModel(PsnXpadAptitudeTreatyDetailQueryResult result) {
        ProtocolIntelligentDetailsBean detailsBean = new ProtocolIntelligentDetailsBean();
        detailsBean.setAccNo(result.getAccNo());// 银行账号
        detailsBean.setProductCode(result.getProductCode());
        detailsBean.setProductName(result.getProductName());
        detailsBean.setAgrType(result.getAgrType());// 协议类型
        detailsBean.setInvestType(result.getInvestType());// 投资方式
        detailsBean.setAgrCode(result.getAgrCode());// 协议代码
        detailsBean.setAgrName(result.getAgrName());// 协议名称
        detailsBean.setAgrPeriod(result.getAgrPeriod());// 协议总期数
        detailsBean.setAgrCurrPeriod(result.getAgrCurrPeriod());// 协议当前期数
        detailsBean.setPeriodAgr(result.getPeriodAgr());// 投资周期
        detailsBean.setMinInvestPeriod(result.getMinInvestPeriod());// 最小投资期数
        detailsBean.setSingleInvestPeriod(result.getSingleInvestPeriod());// 单周期投资期数
        detailsBean.setPeriodPur(result.getPeriodPur());// 购买周期
        detailsBean.setFirstDatePur(result.getFirstDatePur());// 下次购买日
        detailsBean.setPeriodRed(result.getPeriodRed());// 赎回周期
        detailsBean.setFirstDateRed(result.getFirstDateRed());// 下次赎回日
        detailsBean.setRate(result.getRate());// 预计年收益率（%）
        detailsBean.setAgrPurStart(result.getAgrPurStart());// 协议投资起点金额
        detailsBean.setRateDetail(result.getRateDetail());// 预计年收益率（%）(最大值)
        detailsBean.setMemo(result.getMemo());
        detailsBean.setIsNeedPur(result.getIsNeedPur());// 购买频率
        detailsBean.setIsNeedRed(result.getIsNeedRed());// 赎回频率
        detailsBean.setIsQuota(result.getIsQuota());// 是否允许不定额方式
        detailsBean.setKind(result.getKind());// 产品性质
        return detailsBean;
    }

    /**
     * 风险匹配
     *
     * @param result
     * @param viewModel
     */
    public static void copyResultToViewModel(PsnXpadQueryRiskMatchResult result, ProtocolModel viewModel) {
        viewModel.setRiskMatch(result.getRiskMatch());
        viewModel.setProRisk(result.getProRisk());
        viewModel.setCustRisk(result.getCustRisk());
        viewModel.setIsPeriod(result.getIsPeriod());
        viewModel.setProductId(result.getProductId());
        viewModel.setDigitId(result.getDigitId());
        viewModel.setRiskMsg(result.getRiskMsg());
    }

    /**
     * 智能协议申请预交易
     *
     * @param result
     * @return
     */
    public static ProtocolIntelligentConfirmBean copyResultToViewModel(PsnXpadAptitudeTreatyApplyVerifyResult result) {
        ProtocolIntelligentConfirmBean confirmBean = new ProtocolIntelligentConfirmBean();
        confirmBean.setAccNo(result.getAccNo());
        confirmBean.setAgrCode(result.getAgrCode());
        confirmBean.setAgrName(result.getAgrName());
        confirmBean.setPeriodPur(result.getPeriodPur());
        confirmBean.setFirstDatePur(result.getFirstDatePur());
        confirmBean.setPeriodRed(result.getPeriodRed());
        confirmBean.setFirstDateRed(result.getFirstDateRed());
        confirmBean.setAmount(result.getAmount());
        confirmBean.setMinAmount(result.getMinAmount());
        confirmBean.setMaxAmount(result.getMaxAmount());
        confirmBean.setPeriodAgr(result.getPeriodAgr());
        confirmBean.setIsControl(result.getIsControl());
        confirmBean.setTotalPeriod(result.getTotalPeriod());
        confirmBean.setEndDate(result.getEndDate());
        confirmBean.setLastDays(result.getLastDays());
        confirmBean.setWillPurCount(result.getWillPurCount());
        confirmBean.setWillRedCount(result.getWillRedCount());
        confirmBean.setEachpbalDays(result.getEachpbalDays());
        confirmBean.setOneTmredAmt(result.getOneTmredAmt());
        confirmBean.setFailMax(result.getFailMax());
        confirmBean.setIsNeedPur(result.getIsNeedPur());
        confirmBean.setIsNeedRed(result.getIsNeedRed());
        return confirmBean;
    }


    public static ArrayList<WealthListBean> generateWealthListBeans(String prodCode, PsnXpadProductListQueryResult result) {
        if (result == null || result.getList() == null)
            return null;

        ArrayList<WealthListBean> list = new ArrayList<>();
        for (int i = 0; i < result.getList().size(); i++) {
            if (!StringUtils.isEmptyOrNull(prodCode)) {
                if (prodCode.equals(result.getList().get(i).getProdCode())) {
                    continue;
                }
            }
            PsnXpadProductListQueryResult.ListBean resultItem = result.getList().get(i);
            WealthListBean item = new WealthListBean();
            item.setIssueType(resultItem.getIssueType());// 产品类型
            item.setProdName(resultItem.getProdName());// 产品名称
            item.setProdCode(resultItem.getProdCode());// 产品代码
            item.setCurCode(resultItem.getCurCode());// 产品币种
            item.setSubPayAmt(resultItem.getSubPayAmt());// 购买起点金额
            item.setPeriedTime(resultItem.getPeriedTime());// 产品期限
            item.setIsLockPeriod(resultItem.getIsLockPeriod());// 是否为业绩基准产品
            item.setTermType(resultItem.getTermType());// 产品期限特性
            item.setYearlyRR(resultItem.getYearlyRR());// 预计年收益率%
            item.setRateDetail(resultItem.getRateDetail());// 预计年收益率（%）(最大值)
            item.setStatus(resultItem.getStatus());// 产品销售状态
            item.setIsBuy(resultItem.getIsBuy());// 是否可购买
            item.setIsAgreement(resultItem.getIsAgreement());// 是否允许投资协议申请
            item.setImpawnPermit(resultItem.getImpawnPermit());// 是否允许组合购买
            item.setProductKind(resultItem.getProductKind());// 产品性质
            item.setAvailableAmt(resultItem.getAvailableAmt());// 剩余额度
            item.setPrice(resultItem.getPrice());// 单位净值
            item.setIsProfiTest(resultItem.getIsProfitest());// 是否可以收益试算
            item.setRemainCycleCount(resultItem.getRemainCycleCount());
            list.add(item);
        }
        return list;
    }

}
