package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.model.customModel;

/**
 * 货币对顺序 model
 * <p>
 * Created by yx on 2016/12/20.
 */

public class MoneyForOrderModel {
    private int order;//货币对顺序
    private String currentyToNum;//货币对
    private String currentyTocharacter;//货币对符号
    private String firstCurrency;//首货币//暂时无用
    private String endingCurrency;//次货币//暂时无用
    private String currencyName;//货币名称
    private String effectivDecimalPlace;//有效小数位
    private String whatZoom;//第几位放大//暂时无用

    //--------set------------

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public void setCurrentyTocharacter(String currentyTocharacter) {
        this.currentyTocharacter = currentyTocharacter;
    }

    public void setCurrentyToNum(String currentyToNum) {
        this.currentyToNum = currentyToNum;
    }

    public void setEffectivDecimalPlace(String effectivDecimalPlace) {
        this.effectivDecimalPlace = effectivDecimalPlace;
    }

    public void setEndingCurrency(String endingCurrency) {
        this.endingCurrency = endingCurrency;
    }

    public void setFirstCurrency(String firstCurrency) {
        this.firstCurrency = firstCurrency;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setWhatZoom(String whatZoom) {
        this.whatZoom = whatZoom;
    }
    //--------get------------

    public int getOrder() {
        return order;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public String getCurrentyTocharacter() {
        return currentyTocharacter;
    }

    public String getCurrentyToNum() {
        return currentyToNum;
    }

    public String getEffectivDecimalPlace() {
        return effectivDecimalPlace;
    }

    public String getEndingCurrency() {
        return endingCurrency;
    }

    public String getFirstCurrency() {
        return firstCurrency;
    }

    public String getWhatZoom() {
        return whatZoom;
    }
}
