package com.boc.bocsoft.mobile.bocmobile.buss.common.menu;

import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.Map;

/**
 * 菜单Model
 * Created by lxw on 2016/7/21 0021.
 */
public class Menu {

    private LinkedTreeMap<String, Category> categoryLinkedTreeMap = new LinkedTreeMap<>();


    /**
     * 获取所有的菜单项
     *
     * @return
     */
    public ArrayList<Item> getAllMenuItem() {
        ArrayList<Item> itemArrayList = new ArrayList<>();

        for (Category category : categoryLinkedTreeMap.values()) {
            itemArrayList.addAll(category.getItemArray());
        }
        return itemArrayList;
    }

    public LinkedTreeMap<String, Category> getMenuCategory() {
        return categoryLinkedTreeMap;
    }

    /**
     *
     * @param arg
     * @return
     */
    public LinkedTreeMap<String, Category> getFilterCategory(String[] arg) {
        LinkedTreeMap<String, Category> result = new LinkedTreeMap<>();

        for (String key : arg) {
            result.put(key,categoryLinkedTreeMap.get(key));
        }
        return result;
    }

    /**
     * 根据过滤条件过滤菜单项目列表
     *
     * @param arg
     * @return
     */
    public ArrayList<Item> filterMenuItem(String[] arg) {
        ArrayList<Item> itemArrayList = new ArrayList<>();

        if (arg == null || arg.length == 0) {
            return itemArrayList;
        }

        LinkedTreeMap<String, Item> tmp = new LinkedTreeMap<>();

        for (Category category : categoryLinkedTreeMap.values()) {

            ArrayList<Item> items = category.getItemArray();
            for (Item i : items) {
                tmp.put(i.getModuleId(), i);
            }
        }

        for (String key : arg) {
            Item item = tmp.get(key);
            if (item != null) {
                itemArrayList.add(tmp.get(key));
            }
        }
        return itemArrayList;
    }

    /**
     * 根据Id查找菜单项
     *
     * @return
     */
    public Item findItemById(String id) {
        if (StringUtils.isEmptyOrNull(id)) {
            return null;
        }
        ArrayList<Item> items = getAllMenuItem();
        for (Item item : items) {
            if (id.equals(item.getModuleId())) {
                return item;
            }
        }
        return null;
    }

    /**
     * 根据分类id查询直接的item列表
     *
     * @param id
     * @return
     */
    public ArrayList<Item> getItemArrayByCategoryId(String id) {
        ArrayList<Item> result = new ArrayList<>();
        if (StringUtils.isEmptyOrNull(id)) {
            return result;
        }

        Category category = categoryLinkedTreeMap.get(id);
        return category.getChildItems();
    }

    /**
     * 根据分组ID，获取该组下所有的菜单项目
     * @param groupId
     * @return
     */
//    public Collection<Item> getGroupMenuItem(String groupId){
//        ArrayList<Item> itemArrayList = new ArrayList<>();
//        if (StringUtils.isEmptyOrNull(groupId)) {
//            return itemArrayList;
//        }
//
//        ArrayList<Group> groups = getMenuGroup();
//        for (Group group : groups) {
//            if (groupId.equals(group.getGroupId())) {
//                return group.getItemList();
//            }
//        }
//        return itemArrayList;
//    }

    /**
     * 获取所有的分组
     */
//    public ArrayList<Group> getMenuGroup(){
//        ArrayList arrayList = new ArrayList();
//        for (Category category: categoryLinkedTreeMap.values()){
//            arrayList.addAll(category.getGroup());
//        }
//        return arrayList;
//    }
    public void addCategory(Category category) {
        if (category != null) {
            categoryLinkedTreeMap.put(category.getId(), category);
        }
    }

    @Override
    public String toString() {
        return "Menu{" +
                "categoryLinkedTreeMap=" + categoryLinkedTreeMap +
                '}';
    }
}
