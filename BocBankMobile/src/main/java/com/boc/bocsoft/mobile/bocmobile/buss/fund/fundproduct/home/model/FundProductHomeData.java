package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.model;

import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryOutlay.PsnFundQueryOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQueryFundDetail.PsnQueryFundDetailResult;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selecttype.SelectTypeData;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;

import java.util.List;

/**
 * 基金首页所需数据
 * Created by lzc4524 on 2016/11/28.
 */
public class FundProductHomeData{
    private boolean bInvestmentIsOpen = false; //是否开通投资理财
    private boolean bHasBindingInfo = false; //查询投资交易账号是否绑定

    private PsnFundQueryOutlayResult fundProductListNLog = null; //FUNDS系统登录前基金行情数据(最新请求的一页)
    private PsnQueryFundDetailResult fundProductListLog = null; //FUNDS系统登陆后基金行情数据(最新请求的一页)
    private InvstBindingInfoViewModel bindingInfoModel = null; //账户绑定信息

    private String fundTypeCode = "00"; //当前选中的基金类型,默认为全部：“00”

    private String wfssSortField = ""; //wfss排序字段
    private String wfssSortType = ""; //wfss排序方式

    private String biSortField = ""; //bi排序字段
    private String biSortType = ""; //bi排序方式

    private int investmentOpenState = DataUtils.INVESTMENT_OPEN_STATE_UNCHECK; //投资理财开通状态

    private int recordNumber = 0; //基金记录总数
    private List<FundProductShowModel> mProductShowModelList = null;// 基金展现列表数据

    /**
     * 筛选相关数据
     */
    private List<SelectTypeData> mFilterList;// 筛选
    private String filterCurrency = ""; //筛选币种
    private String filterRiskLevel = "";//筛选：风险级别
    private String filterFundStatus = "";//筛选：基金状态

    private String filterFundCompCode = "";//筛选：基金公司代码

    private FundCompanyListViewModel fundCompanyListViewModel = null; //基金公司列表

    public InvstBindingInfoViewModel getBindingInfoModel() {
        return bindingInfoModel;
    }

    public void setBindingInfoModel(InvstBindingInfoViewModel bindingInfoModel) {
        this.bindingInfoModel = bindingInfoModel;
    }

    public boolean isbInvestmentIsOpen() {
        return bInvestmentIsOpen;
    }

    public void setbInvestmentIsOpen(boolean bInvestmentIsOpen) {
        this.bInvestmentIsOpen = bInvestmentIsOpen;
    }

    public boolean isbHasBindingInfo() {
        return bHasBindingInfo;
    }

    public void setbHasBindingInfo(boolean bHasBindingInfo) {
        this.bHasBindingInfo = bHasBindingInfo;
    }

    public PsnFundQueryOutlayResult getFundProductListNLog() {
        return fundProductListNLog;
    }

    public void setFundProductListNLog(PsnFundQueryOutlayResult fundProductListNLog) {
        this.fundProductListNLog = fundProductListNLog;
    }

    public PsnQueryFundDetailResult getFundProductListLog() {
        return fundProductListLog;
    }

    public void setFundProductListLog(PsnQueryFundDetailResult fundProductListLog) {
        this.fundProductListLog = fundProductListLog;
    }

    public String getFundTypeCode() {
        return fundTypeCode;
    }

    public void setFundTypeCode(String fundTypeCode) {
        this.fundTypeCode = fundTypeCode;
    }

    public String getWfssSortField() {
        return wfssSortField;
    }

    public void setWfssSortField(String wfssSortField) {
        this.wfssSortField = wfssSortField;
    }

    public String getWfssSortType() {
        return wfssSortType;
    }

    public void setWfssSortType(String wfssSortType) {
        this.wfssSortType = wfssSortType;
    }

    public String getBiSortType() {
        return biSortType;
    }

    public void setBiSortType(String biSortType) {
        this.biSortType = biSortType;
    }

    public String getBiSortField() {
        return biSortField;
    }

    public void setBiSortField(String biSortField) {
        this.biSortField = biSortField;
    }

    public List<SelectTypeData> getmFilterList() {
        return mFilterList;
    }

    public void setmFilterList(List<SelectTypeData> mFilterList) {
        this.mFilterList = mFilterList;
    }

    public String getFilterCurrency() {
        return filterCurrency;
    }

    public void setFilterCurrency(String filterCurrency) {
        this.filterCurrency = filterCurrency;
    }

    public String getFilterRiskLevel() {
        return filterRiskLevel;
    }

    public void setFilterRiskLevel(String filterRiskLevel) {
        this.filterRiskLevel = filterRiskLevel;
    }

    public String getFilterFundStatus() {
        return filterFundStatus;
    }

    public void setFilterFundStatus(String filterFundStatus) {
        this.filterFundStatus = filterFundStatus;
    }

    public FundCompanyListViewModel getFundCompanyListViewModel() {
        return fundCompanyListViewModel;
    }

    public void setFundCompanyListViewModel(FundCompanyListViewModel fundCompanyListViewModel) {
        this.fundCompanyListViewModel = fundCompanyListViewModel;
    }

    public String getFilterFundCompCode() {
        return filterFundCompCode;
    }

    public void setFilterFundCompCode(String filterFundCompCode) {
        this.filterFundCompCode = filterFundCompCode;
    }

    public int getInvestmentOpenState() {
        return investmentOpenState;
    }

    public void setInvestmentOpenState(int investmentOpenState) {
        this.investmentOpenState = investmentOpenState;
    }

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public List<FundProductShowModel> getmProductShowModelList() {
        return mProductShowModelList;
    }

    public void setmProductShowModelList(List<FundProductShowModel> mProductShowModelList) {
        this.mProductShowModelList = mProductShowModelList;
    }
}
