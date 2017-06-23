package com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by feibin on 2016/6/22.
 */
public class CombinListBean implements Serializable {
    /**
     * id: 安全因子id
     */
    private String id;
    /**
     * name:安全因子名称
     */
    private String name;
    private List<String> safetyFactorList;
    private int weight;

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSafetyFactorList() {
        return safetyFactorList;
    }

    public void setSafetyFactorList(List<String> safetyFactorList) {
        this.safetyFactorList = safetyFactorList;
    }

    @Override
    public String toString() {
        return "CombinListBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", safetyFactorList=" + safetyFactorList +
                '}';
    }
}
