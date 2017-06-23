package com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.fundqueryoutlay;

import com.boc.bocsoft.mobile.bocmobile.base.model.FundBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.LoadMoreListHelper;

/**
 * I41 4.74 074 PsnFundQueryOutlay 登录前基金行情查询
 * Created by gwluo on 2016/8/31.
 */
public class FundQueryOutlayParams extends LoadMoreListHelper.ViewModel<FundBean> implements Cloneable {
    /**
     * company :
     * currencyCode : 001
     * currentIndex : 0
     * fundKind : 00
     * fundState : 0
     * fundType : 00
     * id : 196071715
     * pageSize : 10
     * riskGrade :
     */

    private String fundInfo;//基金代码或名称	String	O	快速查询时用
    private String company;//基金公司代码	String	O	通过接口PsnFundCompanyQueryOutlay查询	上送空查全部
    /**
     * 交易币种	String	O	001=人民币
     * 012=英镑
     * 013=港币
     * 014=美元
     * 027=日元
     * 038=欧元	前端默认上送人民币，查全部上送””
     */
    private String currencyCode;
    /**
     * 产品种类	String	M	00：全部
     * 01：开放式基金产品
     * 04：信托产品
     * 07：资管计划产品	当前端上送“07：资管计划产品”时，查询结果包含以下三种产品
     * 02：一对多
     * 03：券商
     * 07：资管计划产品
     */
    private String fundKind;
    /**
     * 产品类型	String	M	00：全部
     * 01：理财型基金
     * 02：QDII基金
     * 03：ETF基金
     * 04：保本型基金
     * 05：指数型基金
     * 06：货币型基金
     * 07：股票型基金
     * 08：债券型基金
     * 09：混合型基金
     * 10：其他基金
     * （当产品种类fundKind上送不是01时，fundType上送00）
     */
    private String fundType;
    /**
     * 风险等级	String	O	1：保守型（低风险）
     * 2：稳健型（中低风险）
     * 3：平衡型（中风险）
     * 4：成长型（中高风险）
     * 5：进取型（高风险）	查全部,上送””
     */
    private String riskGrade;
    /**
     * 基金状态	String	O	0-正常开放
     * 1-可认购
     * 2-发行成功
     * 3-发行失败
     * 4-暂停交易
     * 5-暂停申购
     * 6-暂停赎回
     * 7-权益登记
     * 8-红利发放
     * 9-基金封闭
     * a-基金终止
     * b-停止开户
     * <p>
     * 上送空，查询全部	P601任务
     */
    private String fundState;
    /**
     * 排序方式	String	O	1:正序
     * 2:逆序
     * 上送空，默认排序	P601任务
     */
    private String sortFlag;
    /**
     * 排序字段	String	O	1:单位净值
     * 2:累计净值
     * 3:日净值增长率
     * 4:每万份收益
     * 5:七日年化收益率
     * 6:年化收益率
     * 上送空，默认排序	1.当sortFlag送非空时，sortField必须非空
     * <p>
     * 2.当sortFlag送空时，sortField也送空
     * <p>
     * P601任务
     */
    private String sortField;

    public String getFundInfo() {
        return fundInfo;
    }

    public void setFundInfo(String fundInfo) {
        this.fundInfo = fundInfo;
    }

    public String getSortFlag() {
        return sortFlag;
    }

    public void setSortFlag(String sortFlag) {
        this.sortFlag = sortFlag;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }


    public String getFundKind() {
        return fundKind;
    }

    public void setFundKind(String fundKind) {
        this.fundKind = fundKind;
    }

    public String getFundState() {
        return fundState;
    }

    public void setFundState(String fundState) {
        this.fundState = fundState;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public String getRiskGrade() {
        return riskGrade;
    }

    public void setRiskGrade(String riskGrade) {
        this.riskGrade = riskGrade;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
