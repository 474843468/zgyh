package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model;

import android.text.TextUtils;

import com.boc.bocsoft.mobile.bocmobile.base.utils.PinYinUtil;

/**
 * 收款人实体
 * Created by zhx on 2016/7/19
 */
public class PayeeEntity implements Comparable<PayeeEntity> {
    private String name;
    private String pinyin;
    private String bankName;
    private String cardNum;

    public PayeeEntity(String name) {
        this.name = name;
        // 在数据创建的时候去处理，而不是使用的时候去处理
        String pinYin = PinYinUtil.getPinYin(name);
//        Log.e("ljljlj", name + " ----- " + pinYin);

        pinYin = pinYin.trim();

        this.setPinyin(TextUtils.isEmpty(pinYin) ? "z" : pinYin.toUpperCase()); // 优化效率

//        Log.e("ljljlj", name + " ----- " + pinYin);
        // TODO 对pinyin进行判断
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    @Override
    public int compareTo(PayeeEntity another) {
        return this.getPinyin().compareTo(another.getPinyin());
    }
}
