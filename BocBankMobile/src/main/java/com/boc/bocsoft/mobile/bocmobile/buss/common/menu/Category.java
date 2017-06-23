package com.boc.bocsoft.mobile.bocmobile.buss.common.menu;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;

/**
 * 菜单分类
 * Created by lxw on 2016/9/12 0012.
 */
public class Category {

    public final static String ATTR_ID = "id";
    public final static String ATTR_TITLE = "title";

    private LinkedTreeMap<String, Item> itemMenuMap = new LinkedTreeMap<>();
    private String id;
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取所有的分组
     */
    public ArrayList<Item> getItemArray(){
        ArrayList<Item> resultArray = new ArrayList<>();
        ArrayList<Item> items  = new ArrayList<>(itemMenuMap.values());

        for(Item item : items){
            resultArray.addAll(getAllChildItem(item));
        }

        return resultArray;
    }

    /**
     * 获取所有子item
     * @param item
     * @return
     */
    private ArrayList<Item> getAllChildItem(Item item){
        ArrayList<Item> result = new ArrayList<>();
        result.add(item);
        if (item.hasChild()) {
            for (Item tmp : item.getAllChild()) {
                result.addAll(getAllChildItem(tmp));
            }

        }
        return result;
    }

    /**
     * 添加一个分组
     * @param item
     */
    public void addItem(Item item){
        if (item != null) {
            itemMenuMap.put(item.getModuleId(), item);
        }
    }
    public void addAllItem(ArrayList<Item> items){
        if (items == null || items.size() ==0) {
            return;
        }

        for (Item item : items){
            addItem(item);
        }
    }

    public ArrayList<Item> getChildItems(){
        return new ArrayList<>(itemMenuMap.values());
    }

    @Override
    public String toString() {
        return "Category{" +
                "groupMenuMap=" + itemMenuMap +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
