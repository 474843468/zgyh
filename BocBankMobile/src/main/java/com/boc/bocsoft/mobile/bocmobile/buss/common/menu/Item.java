package com.boc.bocsoft.mobile.bocmobile.buss.common.menu;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 菜单项
 * Created by lxw on 2016/7/21 0021.
 */
public class Item implements Parcelable{
    public final static String NO_ID = "-1";//不是有效的id ， 更多

    public final static String ATTR_ID = "id";
    public final static String ATTR_TITLE = "title";
    public final static String ATTR_ICON = "icon";
    public final static String ATTR_TYPE = "type";
    public final static String ATTR_OLD= "old";
    public final static String ATTR_LOGIN = "login";

    private ArrayList<Item> list = new ArrayList<>();

    // 模块ID
    private String moduleId;
    // 图标ID
    private String iconId;
    // 显示的标题
    private String title;
    // 类型 1：一级菜单 2：二级菜单
    private String type;
    // 是否是旧的功能
    private String old;
    // 是否需要登录
    private String login;

    private int order;

    public Item(){}

    public Item(String title, String moduleId) {
        this.title = title;
        this.moduleId = moduleId;
    }

    protected Item(Parcel in) {
        moduleId = in.readString();
        iconId = in.readString();
        title = in.readString();
        type = in.readString();
        old = in.readString();
        login = in.readString();
        order = in.readInt();
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(moduleId);
        dest.writeString(iconId);
        dest.writeString(title);
        dest.writeString(type);
        dest.writeString(old);
        dest.writeString(login);
        dest.writeInt(order);
    }

    @Override public int describeContents() {
        return 0;
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
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

    public String getOld() {
        return old;
    }

    public void setOld(String old) {
        this.old = old;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * 是否包含子节点
     * @return
     */
    public boolean hasChild(){
        if(list != null && list.size() > 0){
            return true;
        } else {
            return false;
        }
    }

    /**
     * 返回所有的子节点
     * @return
     */
    public ArrayList<Item> getAllChild(){

        return list;
    }

    public Item addChild(Item item){
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(item);
        return item;
    }

    @Override public boolean equals(Object obj) {
        if(this == obj)return true;
        if(obj == null ||!( obj instanceof Item)){
            return  false;
        }
        Item other = (Item) obj;
        if(other.getModuleId() == getModuleId()){
            return true;
        }
        if(getModuleId()!=null && getModuleId().equals(other.getModuleId())){
            return true;
        }

        return super.equals(other);
    }

    @Override
    public String toString() {
        return "Item{" +
                "list=" + list +
                ", moduleId='" + moduleId + '\'' +
                ", iconId='" + iconId + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", old='" + old + '\'' +
                ", login='" + login + '\'' +
                ", order=" + order +
                '}';
    }
}
