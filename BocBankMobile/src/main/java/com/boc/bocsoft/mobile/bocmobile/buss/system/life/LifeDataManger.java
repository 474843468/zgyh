package com.boc.bocsoft.mobile.bocmobile.buss.system.life;

import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsQueryAllPaymentList.PsnPlpsQueryAllPaymentListResult;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsQueryChildrenMenus.PsnPlpsQueryChildrenMenusResult;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsQueryCommonUsedPaymentList.PsnPlpsQueryCommonUsedPaymentListResult;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.model.LifeMenuModel;
import java.util.ArrayList;
import java.util.List;

import static com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsQueryCommonUsedPaymentList.PsnPlpsQueryCommonUsedPaymentListResult.CommonPaymentBean;

/**
 * Created by dingeryue on 2016年11月14.
 */

public class LifeDataManger {

  private List<CommonPaymentBean> useCommonList;//个人常用缴费
  private List<PsnPlpsQueryAllPaymentListResult.PaymentBean> allPaymentList;//地区全部缴费

  private List<LifeMenuModel> allListMenuModels;//全菜单数据 (个人 + 地区 + 全国)

  private List<PsnPlpsQueryChildrenMenusResult.ChildrenMenuBean> childrenMenuBeanList;
  private List<LifeMenuModel> allDisplayMenuModels;

  public void removeUseCommon(LifeMenuModel data){
    if(useCommonList == null)return;

    CommonPaymentBean tmp = null;
    for(CommonPaymentBean bean:useCommonList){
      if(isSameData(data,bean)){
        tmp = bean;
        break;
      }
    }
    if(tmp != null){
      useCommonList.remove(tmp);
      resetUseCommonMenuModel(useCommonList);
    }
  }

  /**
   * 更具个人常用菜单重置 菜单model
   * @param newUseCommons
   */
  public void resetUseCommonMenuModel(List<CommonPaymentBean> newUseCommons){
    if(newUseCommons == null){
      newUseCommons = new ArrayList<>();
    }
    //先清除当前的个人常用
    if(allListMenuModels == null){
      allListMenuModels = new ArrayList<>();
    }

    //移除个人常用
    removeUseCommonLifeMenus();

    List<LifeMenuModel> menuModels = new LifeTools().buildUserCommonLifeMenus(newUseCommons);
    //添加新的个人常用
    allListMenuModels.addAll(0,menuModels);
  }

  private void removeUseCommonLifeMenus(){
    for(;;){
      if(allListMenuModels.size() == 0)break;
      LifeMenuModel menuModel = allListMenuModels.get(0);
      if(LifeMenuModel.TYPE_CITY.equals(menuModel.getTypeId()) || LifeMenuModel.TYPE_COUNTRY.equals(menuModel.getTypeId())){
        break;
      }else{
        allListMenuModels.remove(0);
      }
    }
  }


  private boolean isSameData(LifeMenuModel data,CommonPaymentBean bean) {

    if (data != null
        && useCommonList != null
        && data.getCatId() != null
        && data.getMenuId() != null
        && data.getFlowFileId() != null /*&& data.getMerchantName()!=null*/) {

      if (data.getCatId().equals(bean.getCatId())
          && data.getMenuId().equals(bean.getMenuId())
          && data.getFlowFileId().equals(data.getFlowFileId()) /*&& data.getMerchantName().equals(bean.getMerchantName())*/) {

        return true;
      }
    }
    return false;
  }


  public List<PsnPlpsQueryCommonUsedPaymentListResult.CommonPaymentBean> getUseCommonList() {
    return useCommonList;
  }

  public List<PsnPlpsQueryAllPaymentListResult.PaymentBean> getAllPaymentList() {
    return allPaymentList;
  }

  public List<PsnPlpsQueryChildrenMenusResult.ChildrenMenuBean> getChildrenMenuBeanList() {
    return childrenMenuBeanList;
  }

  public List<LifeMenuModel> getAllListMenuModels() {
    return allListMenuModels;
  }

  public void setAllPaymentList(List<PsnPlpsQueryAllPaymentListResult.PaymentBean> allPaymentList) {
    this.allPaymentList = allPaymentList;
  }

  public void setUseCommonList(
      List<PsnPlpsQueryCommonUsedPaymentListResult.CommonPaymentBean> useCommonList) {
    this.useCommonList = useCommonList;
    resetUseCommonMenuModel(useCommonList);
  }

  public void setChildrenMenuBeanList(
      List<PsnPlpsQueryChildrenMenusResult.ChildrenMenuBean> childrenMenuBeanList) {
    this.childrenMenuBeanList = childrenMenuBeanList;
  }

  public void setAllListMenuModels(List<LifeMenuModel> allListMenuModels) {
    this.allListMenuModels = allListMenuModels;
  }

  public List<LifeMenuModel> getAllDisplayMenuModels() {
    return allDisplayMenuModels;
  }
}
