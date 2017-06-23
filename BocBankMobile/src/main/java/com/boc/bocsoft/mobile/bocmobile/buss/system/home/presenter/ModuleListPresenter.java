package com.boc.bocsoft.mobile.bocmobile.buss.system.home.presenter;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.Item;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.Menu;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui.ModuleContract;

import java.util.ArrayList;

/**
 * 模块列表
 * Created by lxw on 2016/8/8 0008.
 */
public class ModuleListPresenter implements ModuleContract.Presenter{

    ModuleContract.View view;

    public ModuleListPresenter(ModuleContract.View view){
        this.view = view;
    }

    @Override
    public void getListData() {
        Menu menu = ApplicationContext.getInstance().getMenu();
        ArrayList<Item> items = menu.filterMenuItem(HomeDefaultConfig.HOME_MODULE_LIST);
        view.updateView(items);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }
}
