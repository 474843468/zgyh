package com.chinamworld.bocmbci.biz.preciousmetal.goldstoretransquery;

import java.util.Map;

/**
 * Created by linyl on 2016/9/13.
 */
public class Messages {
    private String time; // 操作时间
    private String content; // 内容

    private String title;
    private String date;
    private String week;
    private String weightNum;
    private String addr_price;
    private String amount;
    private Map<String,Object> itemMap;//保存选中项的数据
    private String type;

    public Map<String,Object> getItemMap() {
        return itemMap;
    }

    public void setItemMap(Map<String,Object> itemMap) {
        this.itemMap = itemMap;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getWeightNum() {
        return weightNum;
    }

    public void setWeightNum(String weightNum) {
        this.weightNum = weightNum;
    }

    public String getAddr_price() {
        return addr_price;
    }

    public void setAddr_price(String addr_price) {
        this.addr_price = addr_price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}