package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOcrmProductQuery;

/**
 * 指令交易产品查询_参数
 * Created by Wan mengxin on 2016/9/26.
 */
public class PsnOcrmProductQueryParams {

    //会话id
    private String conversationId;
    /**
     * 交易分类 00全部01基金02理财03不使用，若该项为03，则交易类型为必输项，否则，交易按产品分类返回相关数据。
     */
    private String protpye;
    /**
     * 交易类型:
     * 指令交易范围（8支）：
     01.对私基金认购和申购
     02.对私基金赎回
     03.基金定投购买
     04.基金定投赎回
     05.中银理财计划I产品购买交易
     06.中银理财计划I产品赎回交易
     07.中银理财计划II购买
     08.中银理财计划II赎回
     当交易分类为00、01、02时此项置为空，03时此项必输。

     */
    private String tradeType;
    //页面大小	String
    private String pageSize;
    //当前页索引	String
    private String currentIndex;

    /**
     * true：重新查询结果(在交易改变查询条件时是需要重新查询的,
     * currentIndex需上送0)
     * false:不需要重新查询，使用缓存中的结果	String
     **/
    private String _refresh;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getProtpye() {
        return protpye;
    }

    public void setProtpye(String protpye) {
        this.protpye = protpye;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
    }

    public String get_refresh() {
        return _refresh;
    }

    public void set_refresh(String _refresh) {
        this._refresh = _refresh;
    }
}
