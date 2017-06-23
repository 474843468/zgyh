package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLoanXpadPledgeParameterQry;

import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.common.utils.date.GsonDateFormat;
import org.threeten.bp.LocalDate;

/**
 * 查询理财质押贷款相关参数
 * Created by XieDu on 2016/8/2.
 */
@GsonDateFormat(dateFormat = DateFormatters.DATE_FORMAT_V2_3)
public class PsnLoanXpadPledgeParameterQryParams {

    private String conversationId;
    /**
     * 理财产品代码
     */
    private String fincCode;
    /**
     * 产品到期日
     */
    private LocalDate prodEnd;
    /**
     * 质押率
     */
    private String pledgeRate;
    /**
     * 资金账号
     */
    private String bancAccount;

    public String getFincCode() {
        return fincCode;
    }

    public void setFincCode(String fincCode) {
        this.fincCode = fincCode;
    }

    public LocalDate getProdEnd() {
        return prodEnd;
    }

    public void setProdEnd(LocalDate prodEnd) {
        this.prodEnd = prodEnd;
    }

    public String getPledgeRate() {
        return pledgeRate;
    }

    public void setPledgeRate(String pledgeRate) {
        this.pledgeRate = pledgeRate;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getBancAccount() {
        return bancAccount;
    }

    public void setBancAccount(String bancAccount) {
        this.bancAccount = bancAccount;
    }
}
