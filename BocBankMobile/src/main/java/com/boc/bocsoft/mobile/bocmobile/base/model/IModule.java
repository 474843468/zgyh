package com.boc.bocsoft.mobile.bocmobile.base.model;


import android.os.Parcelable;
import android.support.v4.app.Fragment;

/**
 * 模块接口
 * Created by lxw on 2016/6/4.
 */
public interface IModule extends Parcelable{

    // 获取模块名称
    public String getModuleName();

    // 获取模块ID
    public String getModuleId();

    // 获取Fragment class
    public Class<Fragment> getModuleClass();

}
