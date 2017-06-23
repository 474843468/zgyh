package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 查询客户风险等级与产品风险等级是否匹配
 * Created by zhx on 2016/11/10
 */
public class XpadQueryRiskMatchViewModel implements Parcelable {
    /**
     * 周期性产品系列编号{选输周期性产品输入此项，产品代码与产品数字代码可不输}
     */
    private String serialCode;
    /**
     * 产品代码{选输
     * 周期性产品系列编号与产品数字代码均未输入，则此项必输
     * }
     */
    private String productCode;
    /**
     * 产品数字代码{全输0表示此项未输入
     * 电话银行渠道可输入此项，产品代码与周期性产品系列编号可不输
     * }
     */
    private String digitalCode;
    /**
     * 账号缓存标识{必输	36位长度}
     */
    private String accountKey;

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public String getDigitalCode() {
        return digitalCode;
    }

    public void setDigitalCode(String digitalCode) {
        this.digitalCode = digitalCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getSerialCode() {
        return serialCode;
    }

    public void setSerialCode(String serialCode) {
        this.serialCode = serialCode;
    }

    //======================================//
    // 下面大致对应接口响应的字段
    //======================================//

    /**
     * 是否周期性产品	String	0：是
     * 1：否
     */

    private String isPeriod;

    /**
     * 产品代码	String	如果请求包输入周期性产品系列编号，但未输入产品代码与产品数字代码，则输出当期产品代码
     */
    private String productId;
    /**
     * 客户与产品风险匹配状况	String	0：匹配
     * 1：不匹配但允许交易
     * （即客户风险低于产品风险，但系统不强制风险匹配，可提示客户确认风险和交易）
     * 2：不匹配且拒绝交易
     * （即客户风险低于产品风险，且系统强制风险匹配，拒绝交易）
     */
    private String riskMatch;
    /**
     * 风险揭示信息	String	0: 本理财产品有投资风险，只能保证获得合同明确承诺的收益，您应充分认识投资风险，谨慎投资
     * 1: 本理财产品有投资风险，只保障理财资金本金，不保证理财收益，您应当充分认识投资风险，谨慎投资
     * 2: 本产品为非保本浮动收益理财产品，并不保证理财资金本金和收益，投资者可能会因市场变动而蒙受不同程度的损失，投资者应充分认识投资风险，谨慎投资
     */
    private String riskMsg;
    /**
     * 产品数字代码	String	与产品代码对应
     */
    private String digitId;
    /**
     * 客户风险等级	String	1：保守型投资者
     * 2：稳健型投资者
     * 3：平衡型投资者
     * 4：成长型投资者
     * 5：进取型投资者
     * 未做过风险评估 /不存在此客户返回空格
     */
    private String custRisk;
    /**
     * 产品风险级别	String	0：低风险产品
     * 1：中低风险产品
     * 2：中等风险产品
     * 3：中高风险产品
     * 4：高风险产品
     */
    private String proRisk;


    public String getCustRisk() {
        return custRisk;
    }

    public void setCustRisk(String custRisk) {
        this.custRisk = custRisk;
    }

    public String getDigitId() {
        return digitId;
    }

    public void setDigitId(String digitId) {
        this.digitId = digitId;
    }

    public String getIsPeriod() {
        return isPeriod;
    }

    public void setIsPeriod(String isPeriod) {
        this.isPeriod = isPeriod;
    }

    public String getProRisk() {
        return proRisk;
    }

    public void setProRisk(String proRisk) {
        this.proRisk = proRisk;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getRiskMatch() {
        return riskMatch;
    }

    public void setRiskMatch(String riskMatch) {
        this.riskMatch = riskMatch;
    }

    public String getRiskMsg() {
        return riskMsg;
    }

    public void setRiskMsg(String riskMsg) {
        this.riskMsg = riskMsg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.serialCode);
        dest.writeString(this.productCode);
        dest.writeString(this.digitalCode);
        dest.writeString(this.accountKey);
        dest.writeString(this.isPeriod);
        dest.writeString(this.productId);
        dest.writeString(this.riskMatch);
        dest.writeString(this.riskMsg);
        dest.writeString(this.digitId);
        dest.writeString(this.custRisk);
        dest.writeString(this.proRisk);
    }

    public XpadQueryRiskMatchViewModel() {
    }

    private XpadQueryRiskMatchViewModel(Parcel in) {
        this.serialCode = in.readString();
        this.productCode = in.readString();
        this.digitalCode = in.readString();
        this.accountKey = in.readString();
        this.isPeriod = in.readString();
        this.productId = in.readString();
        this.riskMatch = in.readString();
        this.riskMsg = in.readString();
        this.digitId = in.readString();
        this.custRisk = in.readString();
        this.proRisk = in.readString();
    }

    public static final Parcelable.Creator<XpadQueryRiskMatchViewModel> CREATOR = new Parcelable.Creator<XpadQueryRiskMatchViewModel>() {
        public XpadQueryRiskMatchViewModel createFromParcel(Parcel source) {
            return new XpadQueryRiskMatchViewModel(source);
        }

        public XpadQueryRiskMatchViewModel[] newArray(int size) {
            return new XpadQueryRiskMatchViewModel[size];
        }
    };
}