package com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui;

import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.Item;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.ArrayList;

/**
 * 模块列表协议类
 * Created by lxw on 2016/8/8 0008.
 */
public class ModuleContract {

    public interface View extends BaseView<Presenter> {
        // 更新页面
        void updateView(ArrayList<Item> items);

    }

    public interface Presenter extends BasePresenter {
        // 获取列表数据
        void getListData();
    }
}
