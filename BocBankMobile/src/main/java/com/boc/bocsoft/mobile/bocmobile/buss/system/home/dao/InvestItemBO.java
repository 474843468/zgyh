package com.boc.bocsoft.mobile.bocmobile.buss.system.home.dao;

import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.InvestType;

/**
 * 优选投资用户自定义的条目
 * Created by lxw on 2016/9/7 0007.
 */
public class InvestItemBO {


    public InvestItemBO(){

    }

    public InvestItemBO(String investId, String investName, int orderId){
        this.investId = investId;
        this.investName = investName;
        this.orderId = orderId;
    }

    private String investId;

    private String investName;

    private int orderId;

    // 投资类型 01:基金 02:账户贵金属 03:结构汇
    private InvestType type;

    public String getInvestId() {
        return investId;
    }

    public void setInvestId(String investId) {
        this.investId = investId;
    }

    public String getInvestName() {
        return investName;
    }

    public void setInvestName(String investName) {
        this.investName = investName;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public InvestType getType() {
        return type;
    }

    public void setType(InvestType type) {
        this.type = type;
    }
    /**
     * 转行投资类型到编码，插入时使用
     * @param type
     * @return
     */
    public static String convertTypeToCode(InvestType type){
        switch (type) {
            case fund:
                return "01";
            case gold:
                return "02";
            case fess:
                return "03";
            default:
                return "";
        }
    }

    /**
     * 转换编码到类型，查询返回时使用
     * @param code
     * @return
     */
    public static InvestType convertCodeToType(String code){
        if ("01".equals(code)){
            return InvestType.fund;
        } else if ("02".equals(code)){
            return InvestType.gold;
        } else if ("03".equals(code)){
            return InvestType.fess;
        } else {
            return null;
        }
    }

}
