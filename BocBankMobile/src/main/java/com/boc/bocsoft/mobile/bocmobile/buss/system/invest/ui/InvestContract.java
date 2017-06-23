package com.boc.bocsoft.mobile.bocmobile.buss.system.invest.ui;

import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.Item;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.AdvertisementModel;
import com.boc.bocsoft.mobile.bocmobile.buss.system.invest.model.AssetDetailVo;
import com.boc.bocsoft.mobile.bocmobile.buss.system.invest.model.AssetVo;
import com.boc.bocsoft.mobile.bocmobile.buss.system.invest.model.InvestItemVo;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;
import java.util.ArrayList;
import java.util.List;

/**
 * 投资
 * Created by dingeryue on 2016年08月17.
 */
public class InvestContract {

  public interface View extends BaseView<Presenter> {

    void changUILogin();

    void changUINoLoin();

    void updateAdData(List<AdvertisementModel> adModelList);
    void updateMenus(List<Item> items);
    void updateProductData(List<InvestItemVo> investItemVos);
    void updateItemProduct(InvestItemVo vo,int pos);
    //资产信心加载成功
    void onAssetLoadSuccess();
    //资产加载失败
    void onAssetLoadFail();

    void setOptimalListViewLoadingState(boolean visiable ,boolean isLoading);
  }

  public interface Presenter extends BasePresenter {

    /**
     * 是否登录
     */
    boolean isLogin();

    /**
     * 更新登录状态 ， 登录状态刷新后需要刷新UI
     */
    void uptateLoginState();

    ArrayList<Item> getAllMenuModels();

    void saveMenuSort(List<Item> chooseDatas);

    /**
     * 加载优选投资
     */
    void loadInvestList();

    /**
     * 加载推荐产品的某一条
     * int pos
     * @param vo
     */
    void loadProductDetail(int pos,InvestItemVo vo);

    /**
     * 加载优选投资类型
     */
    //void loadInvestDetailList();

    /**
     * 加载广告
     */
    //Observable<CRgetPosterListResult> loadAd();

    /**
     * 加载个人投资情况
     */
    void loadMinAsset();

    AssetVo userAsset();
    List<AssetDetailVo> userAssetDetailList();
  }


}
