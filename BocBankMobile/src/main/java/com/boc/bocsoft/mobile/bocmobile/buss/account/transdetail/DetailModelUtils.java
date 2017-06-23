package com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryTransferDetail.PsnAccountQueryTransferDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryTransferDetail.PsnAccountQueryTransferDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICTransferDetail.PsnFinanceICTransferDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICTransferDetail.PsnFinanceICTransferDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICTransferDetail.TransDetail;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnInquiryRangeQuery.PsnInquiryRangeQueryResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnMedicalInsurAcctTransferDetailQuery.PsnMedicalInsurAcctTransferDetailQueryParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnMedicalInsurAcctTransferDetailQuery.PsnMedicalInsurAcctTransferDetailQueryResult;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.model.AccountDetailViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.model.FinanceICTransferViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.model.InquiryRangeQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.model.MedicalTransferDetailQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.model.TransDetailViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 将BII的model与View的Model进行转换
 * Created by wangf on 2016/7/15.
 */
public class DetailModelUtils {

    /**
     * ------------------------------------------查询最大跨度和最长时间范围 Model封装 开始------------------------------------------
     */

    public static InquiryRangeQueryViewModel copyInquiryRangeResult2UIModel(PsnInquiryRangeQueryResult rangeQueryResult) {
        InquiryRangeQueryViewModel rangeQueryViewModel = new InquiryRangeQueryViewModel();
        rangeQueryViewModel.setMaxMonths(rangeQueryResult.getMaxMonths());
        rangeQueryViewModel.setMaxYears(rangeQueryResult.getMaxYears());

        return rangeQueryViewModel;
    }

    /** ------------------------------------------查询最大跨度和最长时间范围 Model封装 结束------------------------------------------ */


    /**
     * ------------------------------------------查询账户详情 Model封装 开始------------------------------------------
     */


    public static AccountDetailViewModel copyAccountDetail2ViewModel(
            PsnAccountQueryAccountDetailResult psnAccountQueryAccountDetailResult) {
        AccountDetailViewModel model = new AccountDetailViewModel();
        if (psnAccountQueryAccountDetailResult != null) {
            model.setAccOpenBank(psnAccountQueryAccountDetailResult.getAccOpenBank());
            model.setAccOpenDate(psnAccountQueryAccountDetailResult.getAccOpenDate());
            model.setAccountStatus(psnAccountQueryAccountDetailResult.getAccountStatus());
            model.setAccountType(psnAccountQueryAccountDetailResult.getAccountType());

            List<AccountDetailViewModel.AccountDetaiListBean> listDeatilBeen = new ArrayList<AccountDetailViewModel.AccountDetaiListBean>();
            for (PsnAccountQueryAccountDetailResult.AccountDetaiListBean accountDetaiListBean :
                    psnAccountQueryAccountDetailResult.getAccountDetaiList()) {
                AccountDetailViewModel.AccountDetaiListBean detailList = new AccountDetailViewModel.AccountDetaiListBean();

                detailList.setCurrencyCode(accountDetaiListBean.getCurrencyCode());
                detailList.setCashRemit(accountDetaiListBean.getCashRemit());
                detailList.setBookBalance(accountDetaiListBean.getBookBalance());
                detailList.setAvailableBalance(accountDetaiListBean.getAvailableBalance());
                detailList.setVolumeNumber(accountDetaiListBean.getVolumeNumber());
                detailList.setType(accountDetaiListBean.getType());
                detailList.setInterestRate(accountDetaiListBean.getInterestRate());
                detailList.setStatus(accountDetaiListBean.getStatus());
                detailList.setMonthBalance(accountDetaiListBean.getMonthBalance());
                detailList.setCdNumber(accountDetaiListBean.getCdNumber());
                detailList.setCdPeriod(accountDetaiListBean.getCdPeriod());
                detailList.setOpenDate(accountDetaiListBean.getOpenDate());
                detailList.setInterestStartsDate(accountDetaiListBean.getInterestStartsDate());
                detailList.setInterestEndDate(accountDetaiListBean.getInterestEndDate());
                detailList.setSettlementDate(accountDetaiListBean.getSettlementDate());
                detailList.setConvertType(accountDetaiListBean.getConvertType());
                detailList.setPingNo(accountDetaiListBean.getPingNo());
                detailList.setHoldAmount(accountDetaiListBean.getHoldAmount());
                detailList.setAppointStatus(accountDetaiListBean.getAppointStatus());

                listDeatilBeen.add(detailList);
            }
            model.setAccountDetaiList(listDeatilBeen);
        }

        return model;
    }


    /** ------------------------------------------查询账户详情 Model封装 结束------------------------------------------ */


    /** ----------------------------------------------活期查询 交易明细 Model封装 结束---------------------------------------------- */

    /**
     * 封装交易明细请求参数 -- 活期
     *
     * @param transDetailViewModel
     * @return
     */
    public static PsnAccountQueryTransferDetailParams buildTransDetailParams(TransDetailViewModel transDetailViewModel) {
        PsnAccountQueryTransferDetailParams detailParams = new PsnAccountQueryTransferDetailParams();
        detailParams.setAccountId(transDetailViewModel.getAccountId());
        detailParams.setCurrency(transDetailViewModel.getCurrency());
        detailParams.setCashRemit(transDetailViewModel.getCashRemit());
        detailParams.setStartDate(transDetailViewModel.getStartDate());
        detailParams.setEndDate(transDetailViewModel.getEndDate());
        detailParams.set_refresh(transDetailViewModel.get_refresh());
        detailParams.setPageSize(transDetailViewModel.getPageSize());
        detailParams.setCurrentIndex(transDetailViewModel.getCurrentIndex());
        return detailParams;
    }


    /**
     * 转换交易明细数据到UI层model -- 活期
     */
    public static TransDetailViewModel copyDetailResult2UIModel(PsnAccountQueryTransferDetailResult detailResult) {
        TransDetailViewModel transDetailViewModel = new TransDetailViewModel();
        transDetailViewModel.setRecordNumber(detailResult.getRecordNumber());

        List<TransDetailViewModel.ListBean> viewListBeanList = new ArrayList<TransDetailViewModel.ListBean>();
        for (int i = 0; i < detailResult.getList().size(); i++) {
            TransDetailViewModel.ListBean listBean = new TransDetailViewModel.ListBean();
            PsnAccountQueryTransferDetailResult.ListBean item = detailResult.getList().get(i);
            listBean.setAmount(item.getAmount());
            listBean.setBalance(item.getBalance());
            listBean.setBusinessDigest(item.getBusinessDigest());
            listBean.setCashRemit(item.getCashRemit());
            listBean.setChargeBack(item.isChargeBack());
            listBean.setChnlDetail(item.getChnlDetail());
            listBean.setCurrency(item.getCurrency());
            listBean.setFurInfo(item.getFurInfo());
            listBean.setPayeeAccountName(item.getPayeeAccountName());
            listBean.setPayeeAccountNumber(item.getPayeeAccountNumber());
            listBean.setPaymentDate(item.getPaymentDate());
            listBean.setTransChnl(item.getTransChnl());

            viewListBeanList.add(listBean);
        }
        transDetailViewModel.setList(viewListBeanList);
        return transDetailViewModel;
    }


    /** ----------------------------------------------活期查询 交易明细 Model封装 结束---------------------------------------------- */

    /** ----------------------------------------------医保账户 交易明细 Model封装 开始---------------------------------------------- */

    /**
     * 封装交易明细请求参数 -- 医保账户
     *
     * @param detailQueryViewModel
     * @return
     */
    public static PsnMedicalInsurAcctTransferDetailQueryParams buildMedicalTransferParams(MedicalTransferDetailQueryViewModel detailQueryViewModel) {
        PsnMedicalInsurAcctTransferDetailQueryParams queryParams = new PsnMedicalInsurAcctTransferDetailQueryParams();
        queryParams.setAccountId(detailQueryViewModel.getAccountId());
        queryParams.setCurrency(detailQueryViewModel.getCurrency());
        queryParams.setCashRemit(detailQueryViewModel.getCashRemit());
        queryParams.setStartDate(detailQueryViewModel.getStartDate());
        queryParams.setEndDate(detailQueryViewModel.getEndDate());
        queryParams.set_refresh(detailQueryViewModel.get_refresh());
        queryParams.setPageSize(detailQueryViewModel.getPageSize());
        queryParams.setCurrentIndex(detailQueryViewModel.getCurrentIndex());

        return queryParams;
    }


    /**
     * 转换交易明细数据到UI层model -- 医保账户
     */
    public static MedicalTransferDetailQueryViewModel copyMedicalTransferResult2UIModel(PsnMedicalInsurAcctTransferDetailQueryResult detailResult) {
        MedicalTransferDetailQueryViewModel transDetailViewModel = new MedicalTransferDetailQueryViewModel();
        transDetailViewModel.setRecordNumber(detailResult.getRecordNumber());

        List<MedicalTransferDetailQueryViewModel.ListBean> viewListBeanList = new ArrayList<MedicalTransferDetailQueryViewModel.ListBean>();
        for (int i = 0; i < detailResult.getList().size(); i++) {
            MedicalTransferDetailQueryViewModel.ListBean listBean = new MedicalTransferDetailQueryViewModel.ListBean();
            PsnMedicalInsurAcctTransferDetailQueryResult.ListBean item = detailResult.getList().get(i);

            listBean.setAmount(item.getAmount());
            listBean.setBalance(item.getBalance());
            listBean.setBusinessDigest(item.getBusinessDigest());
            listBean.setCashRemit(item.getCashRemit());
            listBean.setChargeBack(item.isChargeBack());
            listBean.setCurrency(item.getCurrency());
            listBean.setFurInfo(item.getFurInfo());
            listBean.setPayeeAccountNumber(item.getPayeeAccountNumber());
            listBean.setPayeeAccountName(item.getPayeeAccountName());
            listBean.setPaymentDate(item.getPaymentDate());
            listBean.setTransChnl(item.getTransChnl());
            listBean.setChnlDetail(item.getChnlDetail());

            viewListBeanList.add(listBean);
        }
        transDetailViewModel.setList(viewListBeanList);
        return transDetailViewModel;
    }

    /** -------------------------------------------医保账户 交易明细 Model封装 结束------------------------------------------- */

    /** ------------------------------------------电子现金账户 交易明细 Model封装 开始---------------------------------------- */

    /**
     * 封装交易明细请求参数 -- 电子现金账户
     *
     * @param financeICTransferViewModel
     * @return
     */
    public static PsnFinanceICTransferDetailParams buildFinanceICTransferParams(FinanceICTransferViewModel financeICTransferViewModel) {
        PsnFinanceICTransferDetailParams queryParams = new PsnFinanceICTransferDetailParams();
        queryParams.setAccountId(financeICTransferViewModel.getAccountId());
        queryParams.setStartDate(financeICTransferViewModel.getStartDate());
        queryParams.setEndDate(financeICTransferViewModel.getEndDate());
        queryParams.setPageSize(financeICTransferViewModel.getPageSize());
        queryParams.setCurrentIndex(financeICTransferViewModel.getCurrentIndex());

        return queryParams;
    }


    /**
     * 转换交易明细数据到UI层model -- 电子现金账户
     */
    public static FinanceICTransferViewModel copyFinanceICTransferResult2UIModel(PsnFinanceICTransferDetailResult detailResult) {
        FinanceICTransferViewModel transDetailViewModel = new FinanceICTransferViewModel();
        transDetailViewModel.setRecordNumber(detailResult.getRecordNumber());

        List<FinanceICTransferViewModel.ListBean> viewListBeanList = new ArrayList<FinanceICTransferViewModel.ListBean>();
        for (int i = 0; i < detailResult.getTransDetails().size(); i++) {
            FinanceICTransferViewModel.ListBean listBean = new FinanceICTransferViewModel.ListBean();
            TransDetail item = detailResult.getTransDetails().get(i);

            listBean.setAmount(item.getAmount());
            listBean.setBalance(item.getBalance());
            listBean.setCurrency(item.getCurrency());
            listBean.setAmountFlag(item.isAmountFlag());
            listBean.setReturnDate(item.getReturnDate());
            listBean.setCashRemit(item.getCashRemit());
            listBean.setChargeBack(item.isChargeBack());
            listBean.setTransferType(item.getTransferType());

            viewListBeanList.add(listBean);
        }
        transDetailViewModel.setListBeen(viewListBeanList);
        return transDetailViewModel;
    }


    /** ------------------------------------------电子现金账户 交易明细 Model封装 结束--------------------------------------- */



    /**
     * 电子现金账户 交易类型 转换
     * @param transferType
     * @return
     */
    public static String getTransferTypeString(String transferType) {
        switch (transferType) {
            case "201":
            case "404":
            case "711":
            case "721":
            case "731":
            case "751":
            case "760":
            case "770":
            case "791":
                return "圈存";
            case "502":
            case "506":
            case "552":
            case "556":
            case "560":
                return "换卡";
            case "514":
            case "518":
            case "568":
            case "564":
            case "572":
                return "补卡";
            case "528":
            case "530":
            case "531":
            case "BTI":
            case "BTO":
                return "转卡";
            case "781":
            case "782":
            case "784":
            case "785":
            case "803":
            case "805":
            case "807":
            case "809":
                return "圈提";
            case "790":
            case "PCS":
                return "消费";
            case "811":
                return "回收";
            case "812":
            case "813":
                return "调账";
            case "741":
                return "补登";
            case "820":
            case "821":
            case "002":
                return "退货";
            case "738":
            case "729":
                return "跨行指定账户圈存";
            case "FEE":
                return "手续费";
            case "798":
            case "799":
                return "其他";
        }
        return transferType;
    }

}
