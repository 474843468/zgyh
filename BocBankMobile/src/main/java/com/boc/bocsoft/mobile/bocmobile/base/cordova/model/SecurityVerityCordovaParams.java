package com.boc.bocsoft.mobile.bocmobile.base.cordova.model;

import com.boc.bocsoft.mobile.bocmobile.base.model.FactorBean;
import java.util.List;

/**
 * 作者：XieDu
 * 创建时间：2016/12/12 10:59
 * 描述：
 */
public class SecurityVerityCordovaParams {

    /**
     * 是否对输入的Token加密
     * true：加密
     * false：非加密
     * 目前手机银行只有加密一个选项
     */
    private boolean cipher;

    /**
     * 加密算法，CFCA控件设置参数
     * O	0 : SM2
     * 1 : RSA
     * 目前手机银行不处理
     */
    private String cipherType;
    /**
     * 随机数，必传
     */
    private String randomKey;
    /**
     * 预交易/确认接口返回,必传
     */
    private String plainData;

    /**
     * 预交易/确认接口返回,目前没用到
     */
    private String _certDN;
    /**
     * 加密方式,待使用时约定。
     * 1 : 加密后hash
     * 2：仅加密
     * 目前手机银行不处理
     */
    private String outputValueType;
    /**
     * 正则校验
     * 目前不处理
     */
    private String passwordRegularExpression;
    /**
     * 输入最小长度
     * 目前不处理
     */
    private String passwordMinLength;
    /**
     * 输入最大长度
     * 目前不处理
     */
    private String passwordMaxLength;
    /**
     * 键盘类型
     * 0：全键盘
     * 1：数字键盘
     * 目前不处理
     */
    private String keyboardType;
    /**
     * 手机验证码有效时间4
     * 预交易/确认接口返回
     */
    private String smcTrigerInterval;
    private String conversationId;
    /**
     * 预交易/确认接口返回的factorList数组
     */
    private List<FactorBean> pageClass;

    public boolean isCipher() {
        return cipher;
    }

    public void setCipher(boolean cipher) {
        this.cipher = cipher;
    }

    public String getCipherType() {
        return cipherType;
    }

    public void setCipherType(String cipherType) {
        this.cipherType = cipherType;
    }

    public String getRandomKey() {
        return randomKey;
    }

    public void setRandomKey(String randomKey) {
        this.randomKey = randomKey;
    }

    public String getPlainData() {
        return plainData;
    }

    public void setPlainData(String plainData) {
        this.plainData = plainData;
    }

    public String get_certDN() {
        return _certDN;
    }

    public void set_certDN(String _certDN) {
        this._certDN = _certDN;
    }

    public String getOutputValueType() {
        return outputValueType;
    }

    public void setOutputValueType(String outputValueType) {
        this.outputValueType = outputValueType;
    }

    public String getPasswordRegularExpression() {
        return passwordRegularExpression;
    }

    public void setPasswordRegularExpression(String passwordRegularExpression) {
        this.passwordRegularExpression = passwordRegularExpression;
    }

    public String getPasswordMinLength() {
        return passwordMinLength;
    }

    public void setPasswordMinLength(String passwordMinLength) {
        this.passwordMinLength = passwordMinLength;
    }

    public String getPasswordMaxLength() {
        return passwordMaxLength;
    }

    public void setPasswordMaxLength(String passwordMaxLength) {
        this.passwordMaxLength = passwordMaxLength;
    }

    public String getKeyboardType() {
        return keyboardType;
    }

    public void setKeyboardType(String keyboardType) {
        this.keyboardType = keyboardType;
    }

    public String getSmcTrigerInterval() {
        return smcTrigerInterval;
    }

    public void setSmcTrigerInterval(String smcTrigerInterval) {
        this.smcTrigerInterval = smcTrigerInterval;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public List<FactorBean> getPageClass() {
        return pageClass;
    }

    public void setPageClass(List<FactorBean> pageClass) {
        this.pageClass = pageClass;
    }
}
