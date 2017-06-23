package com.boc.bocsoft.mobile.bocmobile.base.model;


import android.os.Parcelable;
import android.support.v4.app.Fragment;

/**
 * 模块接口
 * Created by lxw on 2016/6/4.
 */
public class Module {


    // 模块id
    private String moduleId;
    // 模块名称
    private String moduleName;
    // 父模块id
    private String parentId;
    // 父模块名称
    private String parentName;
    // 图标ID
    private String iconId;

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    @Override
    public String toString() {
        return "Module{" +
                "moduleId='" + moduleId + '\'' +
                ", moduleName='" + moduleName + '\'' +
                ", parentId='" + parentId + '\'' +
                ", parentName='" + parentName + '\'' +
                ", iconId='" + iconId + '\'' +
                '}';
    }
}
