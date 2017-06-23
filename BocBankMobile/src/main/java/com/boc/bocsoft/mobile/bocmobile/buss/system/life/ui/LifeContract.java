package com.boc.bocsoft.mobile.bocmobile.buss.system.life.ui;

import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.AdvertisementModel;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.model.LifeMenuModel;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.model.LifeVo;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;
import java.util.List;
import java.util.Map;

/**
 * 生活协议类
 * Created by eyding on 16/7/29.
 *
 */
public class LifeContract {


  public interface View extends BaseView<Presenter> {

    /**
     * 更新广告
     * @param advertisementModelList
     */
    void updateBanner(List<AdvertisementModel> advertisementModelList);

    /**
     * 更新菜单
     * @param models
     */
    void updateMenu(List<LifeMenuModel> models);
    /**
     * 更新纪念币销售状态
     * @param info 副标题
     * @param isOpen 是否销售
     */
    void updateCoinStatus(String info,boolean isOpen);

    void endLoadMenus(boolean isSuccess);

    void showLoading(String msg);
    void endLoading();
    void endLocation(LifeVo.CityVo vo);
    void showLocationChoosePage();
  }

  public interface LocationChooseView extends BaseView<Presenter>{
    void updateProvinceData(List<LifeVo.ProvinceVo> provinceVoList);
    void updateHotCities(List<LifeVo.CityVo> cityVos);
    void updateCityData(List<LifeVo.CityVo> cityVoList);
    void onCityChoose(LifeVo.CityVo cityVo);

    void showLoading(String loading);
    void closeLoading();
  }

  public interface MoreView extends BaseView<Presenter>{
    void showLoading(String loading);
    void endLoading();
    void onItemDelSuccess(LifeMenuModel menuModel);

    /**
     * 刷新个人常用 结束回调
     * @param isSuccess 是否刷新成功
     * @param list 最新的个人常用, 失败则为null
     */
    void onLoadCommonUseEnd(boolean isSuccess,List<LifeMenuModel> list);
  }

  public interface Presenter extends BasePresenter {


    /**
     * 初始化数据 ,
     */
    void initData();


    /**
     * 根据省查询城市
     * @param provinceCode
     */
    void loadCitysByProvince(LifeVo.ProvinceVo provinceCode,boolean isHot);

    /**
     * 更具定位数据加载数据（手动选择位置）
     * @param cityVo
     */
    void loadDataByLocation(LifeVo.CityVo cityVo);

    /**
     * 网络,加载用户常用菜单
     */
    void loadCommonUseMenus();

    boolean isLogin();

    /**
     * 获取当前位置
     * @return
     */
    LifeVo.CityVo currentLocation();

    /**
     * 保存菜单
     * @param menuModels
     */
    void saveMenuSort(List<LifeMenuModel> menuModels);

    List<LifeMenuModel> getAllMenuModels();
    List<LifeMenuModel> getAllDisplayMenuModels();
    List<Map<String,Object>> getMapParamsCommonUse();
    List<Map<String,Object>> getMapParamsCity();


    String getMenuNameById(String id);
    String getConversitionId();

    /**
     * 删除个人常用
     * @param menuModel
     */
    void delCommonUse(LifeMenuModel menuModel);

    /**
     * 加载个人常用
     * @param currentList
     */
    //void loadUserCommonUse(List<LifeMenuModel> currentList);
  }

}
