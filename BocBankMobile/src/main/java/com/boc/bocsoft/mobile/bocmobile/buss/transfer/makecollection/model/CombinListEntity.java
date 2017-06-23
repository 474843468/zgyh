package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model;

import java.io.Serializable;
import java.util.List;

/**
 * Contract:安全因子实体（与CombinListBean完全对应）
 * Created by zhx on 2016/8/11
 */
public class CombinListEntity implements Serializable {
    /**
     * id: 安全因子id
     */
    private String id;
    /**
     * name:安全因子名称
     */
    private String name;
    private List<String> safetyFactorList;

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
        return "CombinListEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", safetyFactorList=" + safetyFactorList +
                '}';
    }
}
