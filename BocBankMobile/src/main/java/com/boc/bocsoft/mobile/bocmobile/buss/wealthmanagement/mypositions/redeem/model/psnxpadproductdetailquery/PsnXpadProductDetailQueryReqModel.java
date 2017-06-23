package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.redeem.model.psnxpadproductdetailquery;

/**
 * I42-4.40 040产品详情查询PsnXpadProductDetailQuery{请求model}
 *
 * @author yx
 * @description 中银理财-我的持仓-赎回
 * @date 2016/9/06
 */
public class PsnXpadProductDetailQueryReqModel {

    /**
     * ibknum : 40740
     * productKind : 0
     * productCode : yjjz-wy-p603-zdsy003
     */
    private String ibknum;//省行联行号{返回项需展示(剩余额度、工作时间、挂单时间)，此项必输根据PsnXpadAccountQuery接口的返回项进行上送}
    private String productKind;//产品性质{0:结构性理财产品1:类基金理财产品可不上送，不送时“产品性质”默认为“0:结构性理财”}
    private String productCode;//产品代码

    /**
     * 省行联行号(选输){返回项需展示(剩余额度、工作时间、挂单时间)，此项必输根据PsnXpadAccountQuery接口的返回项进行上送}
     *
     * @param ibknum
     */
    public void setIbknum(String ibknum) {
        this.ibknum = ibknum;
    }

    /**
     * 产品性质（选输）{0:结构性理财产品1:类基金理财产品可不上送，不送时“产品性质”默认为“0:结构性理财”}
     *
     * @param productKind
     */
    public void setProductKind(String productKind) {
        this.productKind = productKind;
    }

    /**
     * 产品代码{必输}
     *
     * @param productCode
     */
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    /**
     * 省行联行号(选输){返回项需展示(剩余额度、工作时间、挂单时间)，此项必输根据PsnXpadAccountQuery接口的返回项进行上送}
     *
     * @return
     */
    public String getIbknum() {
        return ibknum;
    }

    /**
     * 产品性质（选输）{0:结构性理财产品1:类基金理财产品可不上送，不送时“产品性质”默认为“0:结构性理财”}
     *
     * @return
     */
    public String getProductKind() {
        return productKind;
    }

    /**
     * 产品代码{必输}
     *
     * @return
     */
    public String getProductCode() {
        return productCode;
    }
}
