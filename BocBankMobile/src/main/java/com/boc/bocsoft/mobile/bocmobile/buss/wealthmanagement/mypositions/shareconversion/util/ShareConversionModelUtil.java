package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.shareconversion.util;

import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryRiskMatch.PsnXpadQueryRiskMatchParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadQueryRiskMatch.PsnXpadQueryRiskMatchResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadShareTransitionCommit.PsnXpadShareTransitionCommitParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadShareTransitionCommit.PsnXpadShareTransitionCommitResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadShareTransitionVerify.PsnXpadShareTransitionVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadShareTransitionVerify.PsnXpadShareTransitionVerifyResult;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.PsnXpadQuantityDetail.PsnXpadQuantityDetailResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.model.psnxpadproductbalancequery.PsnXpadProductBalanceQueryResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.shareconversion.model.PsnXpadQueryRiskMatch.PsnXpadQueryRiskMatchResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.shareconversion.model.PsnXpadShareTransitionCommit.PsnXpadShareTransitionCommitResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.shareconversion.model.PsnXpadShareTransitionVerify.PsnXpadShareTransitionVerifyResModel;

/**
 * 接口Result转换界面Model/Model转换数据
 * <p/>
 * Created by zn on 2016/9/13.
 *
 * @date 2016-9-12 11:21:00
 */
public class ShareConversionModelUtil {
//=====================================份额转换  查询客户风险等级  =================================

    /**
     * 查询客户风险等级与产品风险等级是否匹配  PsnXpadQueryRiskMatch  转换成Bii 请求Model
     *
     * @param serialCode
     * @param productCode
     * @param digitalCode
     * @return
     */
    public static PsnXpadQueryRiskMatchParams buildPsnXpadQueryRiskMatchBiiParams(
            String digitalCode, String productCode, String serialCode, String accountKey) {
        PsnXpadQueryRiskMatchParams mParams = new PsnXpadQueryRiskMatchParams();
        mParams.setDigitalCode(digitalCode);
        mParams.setProductCode(productCode);
        mParams.setSerialCode(serialCode);
        mParams.setAccountKey(accountKey);
        return mParams;
    }

    /**
     * 查询客户风险等级与产品风险等级是否匹配  PsnXpadQueryRiskMatch-- 返回数据 -- 转换成View 响应层Model
     *
     * @param mResult bii层model
     * @return view层model
     */
    public static PsnXpadQueryRiskMatchResModel transverterPsnXpadQueryRiskMatchViewModel(
            PsnXpadQueryRiskMatchResult mResult) {
        PsnXpadQueryRiskMatchResModel mViewModel = new PsnXpadQueryRiskMatchResModel();
        mViewModel.setIsPeriod(mResult.getIsPeriod());
        mViewModel.setProductId(mResult.getProductId());
        mViewModel.setRiskMatch(mResult.getRiskMatch());
        mViewModel.setRiskMsg(mResult.getRiskMsg());
        mViewModel.setDigitId(mResult.getDigitId());
        mViewModel.setCustRisk(mResult.getCustRisk());
        mViewModel.setProRisk(mResult.getProRisk());
        return mViewModel;
    }
//=====================================份额转换  预交易  ===========================================

    /**
     * 4.70 070份额转换预交易  PsnXpadShareTransitionVerify-- 请求数据 -- 转换成bii 请求Model
     * accountKey 帐号缓存标识
     * proId      产品代码
     * tranUnit   转换份额
     * charCode   钞汇类型
     * serialNo   持仓流水号
     */
    public static PsnXpadShareTransitionVerifyParams buildPsnXpadShareTransitionVerifyBiiParams(
            PsnXpadQuantityDetailResModel.ListEntity mListInfo,
            String mContentMoney,String productCode) {
        PsnXpadShareTransitionVerifyParams mParams = new PsnXpadShareTransitionVerifyParams();
        mParams.setAccountKey("");
        mParams.setProId(productCode);
        mParams.setTranUnit(mContentMoney);
        mParams.setCharCode(mListInfo.getCashRemit());
        mParams.setSerialNo(""+mListInfo.getTranSeq());
        return mParams;
    }

    /**
     * 4.70 070份额转换预交易  PsnXpadShareTransitionVerify-- 返回数据 -- 转换成View 响应层Model
     *
     * @param mResult bii层model
     * @return view层model
     */
    public static PsnXpadShareTransitionVerifyResModel transverterPsnXpadShareTransitionVerifyViewModel(
            PsnXpadShareTransitionVerifyResult mResult) {
        PsnXpadShareTransitionVerifyResModel mViewModel = new PsnXpadShareTransitionVerifyResModel();
        mViewModel.setAccNo(mResult.getAccNo());
        mViewModel.setProId(mResult.getProId());
        mViewModel.setProNam(mResult.getProNam());
        mViewModel.setProCur(mResult.getProCur());
        mViewModel.setCharCode(mResult.getCharCode());
        mViewModel.setExyield(mResult.getExyield());
        mViewModel.setTranUnit(mResult.getTranUnit());
        mViewModel.setTranDate(mResult.getTranDate());
        mViewModel.setTrsseq(mResult.getTrsseq());
        return mViewModel;
    }
//=====================================份额转换  确认提交===========================================

    /**
     * **
     * 4.71 071 份额转换  确认提交  PsnXpadShareTransitionCommit  转换请求Model
     * Created by zn on 2016-9-13
     *
     *  token
     *  accountkey
     *  conversationId
     *
     */
    public static PsnXpadShareTransitionCommitParams buildPsnXpadShareTransitionCommitBiiParms(
            PsnXpadProductBalanceQueryResModel BalanceDate, String conversationId,String token) {
        PsnXpadShareTransitionCommitParams mParams = new PsnXpadShareTransitionCommitParams();
        mParams.setToken(token);
        mParams.setAccountKey(BalanceDate.getBancAccountKey());
        mParams.setConversationId(conversationId);
        return mParams;
    }

    /**
     * **
     * 4.71 071 份额转换  确认提交  PsnXpadShareTransitionCommit  转换为响应Model
     * Created by zn on 2016/9/13.
     *
     * @param mResult
     * @return
     */
    public static PsnXpadShareTransitionCommitResModel transverterPsnXpadShareTransitionCommitViewModel(PsnXpadShareTransitionCommitResult mResult) {
        PsnXpadShareTransitionCommitResModel mViewModel = new PsnXpadShareTransitionCommitResModel();
        mViewModel.setProNam(mResult.getProNam());
        mViewModel.setProCur(mResult.getProCur());
        mViewModel.seteDate(mResult.geteDate());
        mViewModel.setMakNo(mResult.getMakNo());
        mViewModel.setProid(mResult.getProid());
        mViewModel.setStDate(mResult.getStDate());
        mViewModel.setTransactionId(mResult.getTransactionId());
        mViewModel.setTranUnit(mResult.getTranUnit());
        mViewModel.setTrsDate(mResult.getTrsDate());
        mViewModel.setCharCode(mResult.getCharCode());
        return mViewModel;
    }
}
