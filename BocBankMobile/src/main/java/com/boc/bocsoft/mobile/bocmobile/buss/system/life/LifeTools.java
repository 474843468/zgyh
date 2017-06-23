package com.boc.bocsoft.mobile.bocmobile.buss.system.life;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsGetCityListByPrvcShortName.PsnPlpsGetCityListByPrvcShortNameResult;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsGetProvinceList.PsnPlpsGetProvinceListResult;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsQueryAllPaymentList.PsnPlpsQueryAllPaymentListResult;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsQueryCommonUsedPaymentList.PsnPlpsQueryCommonUsedPaymentListResult;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.User;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MapUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.AdvertisementModel;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.model.LifeMenuListModel;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.model.LifeMenuModel;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.model.LifeVo;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.model.LifeVo.ProvinceVo;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.gson.GsonUtils;
import com.boc.bocsoft.mobile.cr.bus.ad.model.CRgetPosterList.CRgetPosterListResult;
import com.chinamworld.boc.commonlib.ModuleManager;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsQueryCommonUsedPaymentList.PsnPlpsQueryCommonUsedPaymentListResult.CommonPaymentBean;

/**
 * 生活菜单工具类
 * Created by eyding on 16/8/13.
 */
public class LifeTools {
  {

    put("安徽","AH");
    put("北京","BJ");
    put("重庆","CQ");
    put("福建", "FJ");
    put("广东", "GD");
    put("甘肃", "GS");
    put("广西", "GX");
    put("贵州", "GZ");
    put("河南", "HA");
    put("湖北", "HB");
    put("河北", "HE");
    put("海南", "HI");
    put("深圳", "SZ");
    put("黑龙江", "HL");
    put("湖南", "HN");
    put("吉林", "JL");
    put("江苏", "JS");
    put("江西", "JX");
    put("辽宁", "LN");
    put("浙江", "ZJ");
    put("内蒙古", "NM");
    put("宁夏", "NX");
    put("青海", "QH");
    put("四川", "SC");
    put("山东", "SD");
    put("上海", "SH");
    put("陕西", "SN");
    put("天津", "TJ");
    put("山西", "SX");
    put("云南", "YN");
    put("新疆", "XJ");
    put("西藏","XZ");
  }

  List<ProvinceVo> list;
  private  void put(String name,String value){
    if(list == null) list = new ArrayList();
    list.add(new ProvinceVo(name, value));
  }



  /**
   * 获取备选省列表
   */
  public List<ProvinceVo> getAllProvinceDatas() {
    return list;
  }

/*  public List<ProvinceVo> getHotCities2(){

    List<ProvinceVo> provinceVos = new ArrayList<>();
    provinceVos.add(new ProvinceVo("北京","BJ"));
    provinceVos.add(new ProvinceVo("上海","SH"));
    provinceVos.add(new ProvinceVo("天津","TJ"));
    provinceVos.add(new ProvinceVo("重庆","CQ"));
    provinceVos.add(new ProvinceVo("深圳","SZ"));

    return provinceVos;
  }*/

  /**
   * 获取常用城市
   */
  public List<LifeVo.CityVo> getHotCities() {

    List<LifeVo.CityVo> cityVoList = new ArrayList<>();
    cityVoList.add(new LifeVo.CityVo("北京", "C1000", "北京", "BJ"));
    cityVoList.add(new LifeVo.CityVo("上海", "CQ", "上海", "SH"));
    cityVoList.add(new LifeVo.CityVo("天津", "CQ", "天津", "TJ"));
    cityVoList.add(new LifeVo.CityVo("重庆", "C001", "重庆", "CQ"));
    cityVoList.add(new LifeVo.CityVo("深圳", "CQ", "深圳", "SZ"));

    return cityVoList;
  }

  public LifeVo.CityVo getDefauleCity() {
    return new LifeVo.CityVo("北京", "C1000", "北京", "BJ");
  }

  /**
   * 生活频道默认的广告页面
   */
  public List<AdvertisementModel> getDefaultAds() {
    List<AdvertisementModel> adList = new ArrayList<>();
    for (int position = 0; position < 1; position++) {
      AdvertisementModel model = new AdvertisementModel();
      model.setPosterUrl("");
      model.setPosterType("1");
      model.setImageUrl("");
      model.setPlaceHolderImageRes(R.drawable.life_ad_default);
      adList.add(model);
    }

    return adList;
  }

  /**
   * 获取未登录时默认菜单
   */
  public List<LifeMenuModel> getDefaultLifeMenus() {
    List<LifeMenuModel> list = new ArrayList<>();

    list.add(getCommonMenu("就医挂号", "49", "50"));
    list.add(getCommonMenu("社会保障", "53", "54"));
    list.add(getCommonMenu("移动通讯", "8", "9"));

    list.add(getCommonMenu("公积金服务", "51", "52"));
    list.add(getCommonMenu("燃气费", "1", "4"));
    list.add(getCommonMenu("取暖费", "1", "5"));

    list.add(getCommonMenu("工资单查询", "41", "42"));
    //list.add(getCommonMenu("有线电视费", "1", "7"));
    //list.add(getCommonMenu("固话", "8", "10"));
    //
    //list.add(getCommonMenu("水费", "1", "2"));
    //list.add(getCommonMenu("交通卡", "20", "21"));
    //list.add(getCommonMenu("电费", "1", "3"));
    //
    //list.add(getCommonMenu("宽带使用", "8", "11"));
    //list.add(getCommonMenu("党费", "45", "46"));
    //list.add(getCommonMenu("电影卡充值", "26", "755"));



    //list.addAll(getCountryMenus());

    list.add(getMoreMenu());

    return list;
  }

  /**
   * 获取全国菜单
   */
  public List<LifeMenuModel> getCountryMenus() {
    List<LifeMenuModel> list = new ArrayList<>();
    list.add(getCountryMenu("icon_life_local_yanglaojin", "养老金服务", "-1"));
    list.add(getCountryMenu("icon_life_local_qianyuedaijiao", "签约代缴服务", "-2"));
    list.add(getCountryMenu("icon_life_local_yufuka", "预付卡", "-3"));
    list.add(getCountryMenu("icon_life_local_jiaotongyidifakuan", "交通异地罚款", "-4"));

    return list;
  }

  /**
   * 重置 全缴费项目中的  个人常用
   * @param datas
   * @param commonUseList
   * @return
   */
  public static void resetCommonUseInAll(List<LifeMenuModel> datas,List<LifeMenuModel> commonUseList){
    if(datas == null){
      return;
    }

    LifeMenuModel menuModel;
    for(;;){
      if(datas.size() == 0)break;
      menuModel = datas.get(0);
      if(!LifeMenuModel.TYPE_CITY.equals(menuModel.getTypeId())){
        //不是城市菜单 移除
        datas.remove(0);
      }else{
        break;
      }
    }
    datas.addAll(0,commonUseList);
  }

  private LifeMenuModel getCountryMenu(String resName, String name, String id) {
    LifeMenuModel lifeMenuModel = new LifeMenuModel();
    lifeMenuModel.setTypeId(LifeMenuModel.TYPE_COUNTRY);
    lifeMenuModel.setCatName(name);
    lifeMenuModel.setCatId(id);
    lifeMenuModel.setMenuId(id);
    lifeMenuModel.setResName(resName);
    return lifeMenuModel;
  }

  private LifeMenuModel getCommonMenu(String name, String menuId, String catId) {
    LifeMenuModel lifeMenuModel = new LifeMenuModel();
    lifeMenuModel.setTypeId(LifeMenuModel.TYPE_CITY);
    lifeMenuModel.setCatName(name);
    lifeMenuModel.setMenuId(menuId);
    lifeMenuModel.setCatId(catId);
    return lifeMenuModel;
  }


  private final static int DEFAULT_ICON = R.drawable.icon;

  /**
   *
   * @param id catid
   * @return
   */
  private static int getResByName(String id) {
    Resources resources = ApplicationContext.getInstance().getResources();

    try {
      int ids = resources.getIdentifier("icon_life_" + id, "drawable",
          ApplicationContext.getAppContext().getPackageName());
      return ids > 0 ? ids : R.drawable.icon;
    } catch (Exception e) {
      return DEFAULT_ICON;
    }
  }

  private static int getCountryResIcon(String id){
    switch (id){
      case "-1":
        return R.drawable.icon_life_local_yanglaojin;
      case "-2":
        return R.drawable.icon_life_local_qianyuedaijiao;
      case "-3":
        return R.drawable.icon_life_local_yufuka;
      case "-4":
        return R.drawable.icon_life_local_jiaotongyidifakuan;
      default:
          return DEFAULT_ICON;
    }

  }

  public static int getResIcon(LifeMenuModel menuModel) {
    if (menuModel == null){
      return DEFAULT_ICON;
    }

    if(isMoreMenu(menuModel)){
      return R.drawable.boc_menu_home_more;
    }

    if (LifeMenuModel.TYPE_COUNTRY.equals(menuModel.getTypeId())) {
      //全国菜单图标
      return LifeTools.getCountryResIcon((menuModel.getCatId()));
    } else if(LifeMenuModel.TYPE_CITY.equals(menuModel.getTypeId())){
      return LifeTools.getResByName(menuModel.getCatId());
    }else if(isRecordMenu(menuModel)){
      //缴费记录
      return R.drawable.icon_life_9;
    }else{
      //个人常用
     return LifeTools.getResByName(menuModel.getCatId());
    }
  }

  /**
   * 获取 首页显示菜单,直接截取前7个菜单 + 更多菜单
   * @param list
   * @return
   */
  public List<LifeMenuModel> getHomeMenus(List<LifeMenuModel> list){
    List<LifeMenuModel> homeModels = new ArrayList<>();
    if(list == null || list.size() == 0){
      //do nothing
    }else{
      for(LifeMenuModel menuModel:list){
        if(menuModel.isDisplay()){
          homeModels.add(menuModel);
        }
        if(homeModels.size()==7)break;
      }
    }
    
    homeModels.add(getMoreMenu());

    return homeModels;
  }

  /**
   * 根据保存顺序 过滤出显示的菜单
   */
  public List<LifeMenuModel> filterPayment(List<LifeMenuModel> lifeMenuModels,
      List<LifeMenuModel> saveMenus) {
    List<LifeMenuModel> homeModels = new ArrayList<>(8);
    if (lifeMenuModels == null || lifeMenuModels.size() == 0) {
      return homeModels;
    }

    //保存过菜单
    if(saveMenus != null && saveMenus.size() >0){
      List<String> ids = new ArrayList<>();
      for (LifeMenuModel menuModel : lifeMenuModels) {
        ids.add(menuModel.generateId());
      }

      for (int index = 0; index < saveMenus.size(); index++) {
        LifeMenuModel homeMenu = saveMenus.get(index);

        if (homeModels.size() >= 7) {
          break;
        }
        int i = ids.indexOf(homeMenu.generateId());
        if (i >= 0) {
          homeModels.add(lifeMenuModels.get(i));
        }
      }
    }else{
      // 没有设置过 需要补足7个
      for (int index = 0; index < lifeMenuModels.size(); index++) {
        if (index >= 7) break;
        homeModels.add(lifeMenuModels.get(index));
      }
    }
    //添加更多
    homeModels.add(getMoreMenu());

    return homeModels;
  }

  public static LifeMenuModel getMoreMenu() {
    LifeMenuModel menuModel = new LifeMenuModel();
    menuModel.setMenuId("-111");
    menuModel.setCatId("-111");
    menuModel.setCatName("更多");
    return menuModel;
  }

  /**
   * 创建缴费记录菜单
   * @return
   */
  public static LifeMenuModel getRecordMenu(){
    LifeMenuModel menuModel = new LifeMenuModel();
    menuModel.setMenuId("-999");
    menuModel.setCatId("-999");
    menuModel.setCatName("缴费记录");
    menuModel.setResName("icon_life_9");
    menuModel.setTypeId(LifeMenuModel.TYPE_RECORD);
    return menuModel;
  }

  public static boolean isRecordMenu(LifeMenuModel menuModel){
    if(menuModel == null)return false;

    if("-999".equals(menuModel.getMenuId())){
      return true;
    }
    return false;
  }

  public static boolean isMoreMenu(LifeMenuModel model){
    if("-111".equals(model.getMenuId()) && "-111".equals(model.getCatId())){
      return true;
    }
    return false;
  }

  public static List<LifeMenuModel> fliterMoreMenus(List<LifeMenuModel> list){


    if(list == null || list.size() == 0){
      return new ArrayList<>();
    }
    List<LifeMenuModel> resultList = new ArrayList<>(list);
    int i = resultList.indexOf(getMoreMenu());
    if(i>=0)resultList.remove(i);
    return resultList;
  }

  public List<ProvinceVo> buildProvinceListData(PsnPlpsGetProvinceListResult result) {
    List<ProvinceVo> provinceVoList = new ArrayList<>();
    if (result == null
        || result.getProvinceList() == null
        || result.getProvinceList().size() == 0) {

      return provinceVoList;
    }

    for (PsnPlpsGetProvinceListResult.ProvinceBean bean : result.getProvinceList()) {
      ProvinceVo vo = new ProvinceVo();
      vo.setName(bean.getPrvcName());
      vo.setShortName(bean.getPrvcShortName());
      provinceVoList.add(vo);
    }

    return provinceVoList;
  }

  public List<LifeVo.CityVo> buildCityListData(ProvinceVo provinceVo,
      PsnPlpsGetCityListByPrvcShortNameResult result) {

    List<LifeVo.CityVo> cityVoList = new ArrayList<>();
    if (result == null || result.getCityList() == null || result.getCityList().size() == 0) {
      return cityVoList;
    }

    for (PsnPlpsGetCityListByPrvcShortNameResult.CityBean cityBean : result.getCityList()) {
      LifeVo.CityVo cityVo = new LifeVo.CityVo();
      cityVo.setProvinceVo(provinceVo);
      cityVo.setName(cityBean.getCityName());
      cityVo.setCode(cityBean.getDisplayNo());
      cityVoList.add(cityVo);
    }

    return cityVoList;
  }

  public List<AdvertisementModel> buildAdList(
      List<CRgetPosterListResult.PosterBean> posterBeanList) {
    List<AdvertisementModel> advertisementModelList = new ArrayList<>();
    if (posterBeanList == null || posterBeanList.size() == 0) {
      return advertisementModelList;
    }

    for (CRgetPosterListResult.PosterBean bean : posterBeanList) {
      if (bean == null) continue;
      AdvertisementModel model = new AdvertisementModel();
      model.setPosterType(bean.getPosterType());
      model.setPosterUrl(bean.getPosterUrl());
      model.setPlaceHolderImageRes(R.drawable.boc_default_ad_bg);
      model.setProductNature(bean.getProductNature());
      model.setImageUrl(bean.getImageUrl());
      model.setPosterName(bean.getPosterName());
      model.setProductCode(bean.getProductCode());
      model.setPosterOrder(bean.getPosterOrder());
      model.setRegion(bean.getRegion());
      //bean.getReleasePosition()
      advertisementModelList.add(model);
    }

    return advertisementModelList;
  }

  /**
   * 缴费地区全部 --> 菜单类型
   */
  public List<LifeMenuModel> buildAllPaymentLifeMenus(
      List<PsnPlpsQueryAllPaymentListResult.PaymentBean> allPaymentList, LifeVo.CityVo cityVo) {

    List<LifeMenuModel> models = new ArrayList<>();
    if (allPaymentList == null || allPaymentList.size() == 0) {
      return models;
    }

    for (PsnPlpsQueryAllPaymentListResult.PaymentBean paymentBean : allPaymentList) {
      LifeMenuModel lifeMenuModel = new LifeMenuModel();

      lifeMenuModel.setMenuId(paymentBean.getMenuId());
      lifeMenuModel.setCatId(paymentBean.getCatId());
      lifeMenuModel.setCatName(paymentBean.getCatName());
      lifeMenuModel.setIsAvalid(paymentBean.getIsAvailable());

      lifeMenuModel.setCityDispNo(cityVo.getCode());
      lifeMenuModel.setCityDispName(cityVo.getName());
      lifeMenuModel.setPrvcDispName(cityVo.getProvinceVo().getName());
      lifeMenuModel.setPrvcShortName(cityVo.getProvinceVo().getShortName());

      lifeMenuModel.setOrderIndex(models.size());
      lifeMenuModel.setTypeId(LifeMenuModel.TYPE_CITY);
      models.add(lifeMenuModel);
    }

    return models;
  }

  public List<LifeMenuModel> buildUserCommonLifeMenus(List<CommonPaymentBean> paymentList) {

    if (paymentList == null || paymentList.size() == 0) {
      return new ArrayList<>();
    }
    List<LifeMenuModel> models = new ArrayList<>(paymentList.size());
    //
    User user = ApplicationContext.getInstance().getUser();
    String uid = user == null || user.getLoginName() == null ? "" : user.getLoginName();

    for (PsnPlpsQueryCommonUsedPaymentListResult.CommonPaymentBean bean : paymentList) {

      LifeMenuModel lifeMenuModel = new LifeMenuModel();

      lifeMenuModel.setMenuId(bean.getMenuId());
      lifeMenuModel.setCatId(bean.getCatId());
      lifeMenuModel.setCatName(bean.getCatName());
      lifeMenuModel.setIsAvalid(bean.getIsAvalid());

      lifeMenuModel.setPrvcDispName(bean.getPrvcDispName());
      lifeMenuModel.setPrvcShortName(bean.getPrvcShortName());
      lifeMenuModel.setCityDispName(bean.getCityDispName());
      lifeMenuModel.setCityDispNo(bean.getCityDispNo());

      lifeMenuModel.setOrderIndex(models.size());
      lifeMenuModel.setMerchantName(bean.getMerchantName());
      lifeMenuModel.setFlowFileId(bean.getFlowFileId());

      //设置一下类型
      lifeMenuModel.setTypeId(uid);
      lifeMenuModel.setDisplayFlag(bean.getDisplayFlag());

      models.add(lifeMenuModel);
    }

    return models;
  }

  public List<Map<String, Object>> buildCommListmap(List<CommonPaymentBean> paymentList) {

    List<Map<String, Object>> list = new ArrayList<>();

    if (paymentList == null) return list;

    for (CommonPaymentBean bean : paymentList) {
      list.add(MapUtils.clzzField2Map(bean));
    }
    return list;
  }

  /**
   * 地区 - 连龙map
   * @param paymentList
   * @return
   */
  public List<Map<String, Object>> buildAllListmap(List<PsnPlpsQueryAllPaymentListResult.PaymentBean> paymentList) {

    List<Map<String, Object>> list = new ArrayList<>();

    if (paymentList == null) return list;

    for (PsnPlpsQueryAllPaymentListResult.PaymentBean bean : paymentList) {
      list.add(MapUtils.clzzField2Map(bean));
    }
    return list;
  }

  public static String getMenuName(LifeMenuModel menuModel){
    if(menuModel == null)return "";
    if(LifeMenuModel.TYPE_CITY.equals(menuModel.getCatName()) || LifeMenuModel.TYPE_COUNTRY.equals(menuModel.getCatName())){
      return menuModel.getCatName();
    }

    if(menuModel.getCityDispName()!=null && menuModel.getMerchantName() != null){
      //return menuModel.getCatName()+"("+menuModel.getMerchantName()+" - "+menuModel.getCityDispName()+")";
      return "("+menuModel.getCityDispName().trim()+")"+menuModel.getMerchantName();
    }
    return menuModel.getCatName();
  }


  /*public static boolean isLifeMenuExist(LifeMenuModel menuModel,List<LifeMenuModel> list){

    if(menuModel == null)return false;
     if(list == null || list.size()==0)return false;

    for(LifeMenuModel item:list){
      if(item.)
    }

  }*/

  private static String getLifeMenusPath(){
    File lifemenus = ApplicationContext.getAppContext().getDir("lifemenus", Context.MODE_PRIVATE);
    return lifemenus.getPath();
  }

  /**
   * 保存菜单项目
   * @param list
   * @param cityVo
   * @return
   */
  public static boolean saveLocalMenus2(List<LifeMenuModel> list,LifeVo.CityVo cityVo){
    LogUtils.d("dding","---保存地区:");
    String json = null;
    if(list == null ||list.size() ==0 || cityVo == null){
      json = "";
    }else{
      LifeMenuListModel listModel = new LifeMenuListModel();
      listModel.setList(list);
      listModel.setCityVo(cityVo);
      json = GsonUtils.getGson().toJson(listModel);
    }

    LogUtils.d("dding","---保存地区:"+json);

    return FileUtils.saveString2File(json,getLifeMenusPath());
  }

  /**
   * 更具城市 查询保存的菜单项目
   * @param cityVo
   * @return
   */
  public static LifeMenuListModel  getLocalMenus2(LifeVo.CityVo cityVo){

    //城市为空则返回null
    if(cityVo == null || StringUtils.isEmpty(cityVo.getCode())){
      return null;
    }
    String json = FileUtils.readStringFromFile(getLifeMenusPath());
    if(StringUtils.isEmpty(json))return null;
    try {
      LifeMenuListModel listModel = GsonUtils.getGson().fromJson(json, LifeMenuListModel.class);

      LifeVo.CityVo cacheCity = listModel.getCityVo();
      if(cityVo.getCode().equals(cacheCity.getCode())){
        return listModel;
      }

      return null;

    } catch (Exception e) {
    }
    return null;
  }



  /**
   * 常用缴费项目 小类 点击入口
   * prvcShortName 小类省简称
   * flowFileIdt 缴费商户名称id 即商户id
   * catName 小类名称
   * menuName小类对应的服务大类名
   * merchantName 商户名称
   * conversationId 会话id需要跟PsnPlpsQueryHistoryRecords接口会话保持一致
   */
  public static void gotoComonService(Activity activity, String prvcShortName, String flowFileId,
      String catName, String menuName, String merchantName, String conversationId) {
    LogUtils.d("dding", "-----跳转连龙常用缴费菜单:prvcShortName="
        + prvcShortName
        + ",flowFileId:"
        + flowFileId
        + ",catName="
        + catName
        + ",menuName="
        + menuName
        + ",merchantName="
        + merchantName
        + ",conversationId="
        + conversationId);
    ModuleManager.instance.gotoComonService(activity, prvcShortName, flowFileId, catName, menuName,
        merchantName, conversationId);
  }

  /**
   * 全部缴费项目 小类 点击入口
   *List<Map<String, Object>> commPaymentList 常用缴费项目列表
   * catId 小类id
   * catName 小类名
   * isAlailiable 置灰标识
   * prvcDispName 省名称
   * cityDispName 城市名
   * cityDispNo市多语言代码
   * menuName小类对应的服务大类名
   * prvcShortName 省简称
   **/
  public static void gotoAllPayment(Activity activity, List<Map<String, Object>> commPaymentList,
      String catId, String catName,String isAlailiable ,String prvcDispName, String cityDispNo, String cityDispName,
      String menuName, String prvcShortName) {
    LogUtils.d("dding", "-----跳转连龙缴费菜单:catId="
        + catId
        + ",catName="
        + catName
        + ",prvcDispName="
        + prvcDispName
        + ",cityDispName = "
        + cityDispName
        + " ,prvcShortName = "
        + prvcShortName
        + " ,menuName="
        + menuName
        + ",all="
        + commPaymentList);
    ModuleManager.instance.gotoAllPayment(activity, commPaymentList,catId, catName,isAlailiable ,prvcDispName,
        cityDispNo, cityDispName, menuName, prvcShortName);
  }

  /**
   * 本地小类缴费入口
   * localId 本地缴费标识
   * localId=-1 养老金服务
   * localId=-2签约代缴服务
   * localId=-3 预付卡
   * localId=-4 交通异地罚款
   *
   * @param shorName 省简称
   *
   */
  public static void gotoCountry(Activity activity, String localId,String shorName) {
    LogUtils.d("dding", "-----全国菜单:" + localId+" shortName:"+shorName);
    try {
      int i = Integer.parseInt(localId);
      ModuleManager.instance.gotoLocalService(activity, i,shorName);
    } catch (Exception e) {
    }
  }


  /**添加常用入口
   * List<Map<String, Object>> commPaymentList 常用缴费项目列表
   * List<Map<String, Object>> allPaymentList 所有缴费项目列表
   * prvcShortName 省简称
   * cityDispNo 市多语言代码
   * prvcDispName 省名称
   * cityDispName 城市名
   * */
  public static void goToAddCommService(Activity activity, List<Map<String, Object>> commPaymentList,List<Map<String, Object>> allPaymentList, String prvcShortName, String cityDispNo,String prvcDispName, String cityDispName,String conversationId) {

    LogUtils.d("dding","跳转连龙添加常用。。。。");
    try {
      ModuleManager.instance.addCommService(activity,commPaymentList,allPaymentList,prvcShortName,cityDispNo,prvcDispName,cityDispName,conversationId);
    } catch (Exception e) {
    }
  }

  /**
   * 跳转预约排队
   * @param activity
   */
  public static void gotoBranchOrder(Activity activity){
    try {
      ModuleManager.instance.gotoBranchOrderActivity(activity);
    } catch (Exception e) {
    }
  }

  /**缴费记录查询跳转
   * 我的民生二级菜单 liveMenus
   * */
  public static void gotoRecordService(Activity activity) {
    try {
      ModuleManager.instance.gotoCommService(activity);
    }catch (Exception e){
    }
  }
}
