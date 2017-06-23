package com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui;

import com.boc.bocsoft.mobile.bocmobile.base.model.FessBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.FundBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.GoldBean;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.Item;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.AdvertisementModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 主页协议类
 * Created by lxw on 2016/5/21.
 */
public class HomeContract {

    public interface View extends BaseView<Presenter> {
        // 初始化广告页面
        void updateAdsView(List<AdvertisementModel> adsList);
        void updateMainNav(List<Item> items);
        /** 更新基金页面 */
        void updateFundView(Map<String, FundBean> fundBeanMap);
        /** 更新贵金属页面 */
        void updateGoldView(Map<String, GoldBean> goldBeanMap);
        /** 更新结售汇页面 */
        void updateFessView(Map<String, FessBean> fessBeanMap);
        /** 风险优选投资列表 */
        void updateInvestListView(ArrayList<FundBean> funds, ArrayList<GoldBean> golds, ArrayList<FessBean> fesses);

    }

    public interface Presenter extends BasePresenter {
        // 更新广告视图
        void getAdsList();
        // 查询主页面模块列表
        void getHomeModuleList();
        // 更新菜单列表
        void updateMenuList(List<Item> items);
        // 获取基金明细列表
        void getFunds(ArrayList<FundBean> funds);
        // 获取贵金属明细列表
        void getGolds(ArrayList<GoldBean> golds);
        // 获取结购汇明细列表
        void getFesses(ArrayList<FessBean> fesses);
        /** 初始化投资列表 */
        void initInvestListView();

    }
}
