package com.boc.bocsoft.mobile.bii.bus.global.model.PsnCustomerCombinInfo;

/**
 * Created by feib on 16/7/19.
 * 登录后获取客户合并后信息
 */
public class PsnCustomerCombinInfoResult {
    /**
     * 客户姓名(从)
     */
    private String custNameOld;
    /**
     * 客户姓名(主)
     */
    private String custNameNew;
    /**
     * 网银渠道用户登录名称(从)
     */
    private String name1Old;
    /**
     * 网银渠道用户登录名称(主)
     */
    private String name1New;
    /**
     * 手机渠道用户登录名称(从)
     */
    private String name2Old;
    /**
     * 手机渠道用户登录名称(主)
     */
    private String name2New;
    /**
     * 手机银行透传方式登录号码(从) 多手机号以竖线分割
     */
    private String terminalsIdOld;
    /**
     * 手机银行透传方式登录号码(主) 多手机号以竖线分割
     */
    private String terminalsIdNew;
    /**
     * 合并后客户证件类型（主）
     * 1－居民身份证
     2－临时身份证
     8－护照
     3－户口簿
     4－军人身份证
     5－武装警察身份证
     10－港澳台居民往来内地通行证
     11－外交人员身份证
     12－外国人居留许可证
     13－边民出入境通行证
     9－其它
     47－港澳居民来往内地通行证（香港）
     48－港澳居民来往内地通行证（澳门）
     49－台湾居民来往大陆通行证
     */
    private String identityTypeNew;
    /**
     * 合并后客户证件号码（主）
     */
    private String identityNumNew;

    public String getCustNameOld() {
        return custNameOld;
    }

    public void setCustNameOld(String custNameOld) {
        this.custNameOld = custNameOld;
    }

    public String getCustNameNew() {
        return custNameNew;
    }

    public void setCustNameNew(String custNameNew) {
        this.custNameNew = custNameNew;
    }

    public String getName1Old() {
        return name1Old;
    }

    public void setName1Old(String name1Old) {
        this.name1Old = name1Old;
    }

    public String getName1New() {
        return name1New;
    }

    public void setName1New(String name1New) {
        this.name1New = name1New;
    }

    public String getName2Old() {
        return name2Old;
    }

    public void setName2Old(String name2Old) {
        this.name2Old = name2Old;
    }

    public String getName2New() {
        return name2New;
    }

    public void setName2New(String name2New) {
        this.name2New = name2New;
    }

    public String getTerminalsIdOld() {
        return terminalsIdOld;
    }

    public void setTerminalsIdOld(String terminalsIdOld) {
        this.terminalsIdOld = terminalsIdOld;
    }

    public String getTerminalsIdNew() {
        return terminalsIdNew;
    }

    public void setTerminalsIdNew(String terminalsIdNew) {
        this.terminalsIdNew = terminalsIdNew;
    }

    public String getIdentityTypeNew() {
        return identityTypeNew;
    }

    public void setIdentityTypeNew(String identityTypeNew) {
        this.identityTypeNew = identityTypeNew;
    }

    public String getIdentityNumNew() {
        return identityNumNew;
    }

    public void setIdentityNumNew(String identityNumNew) {
        this.identityNumNew = identityNumNew;
    }
}
