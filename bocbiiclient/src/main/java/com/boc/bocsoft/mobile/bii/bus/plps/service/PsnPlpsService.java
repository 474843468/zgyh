package com.boc.bocsoft.mobile.bii.bus.plps.service;

import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsDeleteCommonUsedPaymentList.PsnPlpsDeleteCommonUsedPaymentListParams;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsDeleteCommonUsedPaymentList.PsnPlpsDeleteCommonUsedPaymentListResponse;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsDeleteCommonUsedPaymentList.PsnPlpsDeleteCommonUsedPaymentListResult;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsGetCityListByPrvcShortName.PsnPlpsGetCityListByPrvcShortNameParams;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsGetCityListByPrvcShortName.PsnPlpsGetCityListByPrvcShortNameResponse;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsGetCityListByPrvcShortName.PsnPlpsGetCityListByPrvcShortNameResult;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsGetProvinceList.PsnPlpsGetProvinceListParams;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsGetProvinceList.PsnPlpsGetProvinceListResponse;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsGetProvinceList.PsnPlpsGetProvinceListResult;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsQueryAllPaymentList.PsnPlpsQueryAllPaymentListParams;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsQueryAllPaymentList.PsnPlpsQueryAllPaymentListResponse;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsQueryAllPaymentList.PsnPlpsQueryAllPaymentListResult;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsQueryChildrenMenus.PsnPlpsQueryChildrenMenusParams;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsQueryChildrenMenus.PsnPlpsQueryChildrenMenusResponse;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsQueryChildrenMenus.PsnPlpsQueryChildrenMenusResult;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsQueryCommonUsedPaymentList.PsnPlpsQueryCommonUsedPaymentListParams;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsQueryCommonUsedPaymentList.PsnPlpsQueryCommonUsedPaymentListResponse;
import com.boc.bocsoft.mobile.bii.bus.plps.model.PsnPlpsQueryCommonUsedPaymentList.PsnPlpsQueryCommonUsedPaymentListResult;
import com.boc.bocsoft.mobile.bii.common.client.BIIClient;
import java.util.List;
import rx.Observable;
import rx.functions.Func1;

/**
 * 民生缴费
 * Created by eyding on 16/8/1.
 */
public class PsnPlpsService {


  /**
   * PsnPlpsGetProvinceList查询有民生缴费项目的省列表
   * @param params 参数
   * @return Observable<PsnPlpsQueryCommonUsedPaymentListResult>
   */
  public Observable<PsnPlpsGetProvinceListResult> psnPlpsGetProvinceList(PsnPlpsGetProvinceListParams params) {

   return BIIClient.instance.post("PsnPlpsGetProvinceList", params, PsnPlpsGetProvinceListResponse.class).map(
        new Func1<List<PsnPlpsGetProvinceListResult.ProvinceBean>, PsnPlpsGetProvinceListResult>() {
          @Override
          public PsnPlpsGetProvinceListResult call(List<PsnPlpsGetProvinceListResult.ProvinceBean> provinceBeen) {
            PsnPlpsGetProvinceListResult result = new PsnPlpsGetProvinceListResult();
            result.setProvinceList(provinceBeen);
            return result;
          }
        });

  }


  /**
   * 查询某省已开通民生缴费项目的城市列表
   * @param params 参数
   * @return Observable<PsnPlpsDeleteCommonUsedPaymentListResult>
   */
  public Observable<PsnPlpsGetCityListByPrvcShortNameResult> psnPlpsGetCityListByPrvcShortName(PsnPlpsGetCityListByPrvcShortNameParams params) {
    return BIIClient.instance.post("PsnPlpsGetCityListByPrvcShortName", params, PsnPlpsGetCityListByPrvcShortNameResponse.class);
  }


  /**
   * 查询常用缴费项
   * @param params 参数
   * @return Observable<PsnPlpsQueryCommonUsedPaymentListResult>
   */
  public Observable<PsnPlpsQueryCommonUsedPaymentListResult> psnPlpsQueryCommonUsedPaymentList(PsnPlpsQueryCommonUsedPaymentListParams params) {
    //return  BIIClient.instance.post("http://192.168.0.114:9912/life?type=comm","PsnPlpsQueryCommonUsedPaymentList", params, PsnPlpsQueryCommonUsedPaymentListResponse.class);
    return BIIClient.instance.post("PsnPlpsQueryCommonUsedPaymentList", params, PsnPlpsQueryCommonUsedPaymentListResponse.class);
  }


  /**
   * 查询某地区所有缴费项目
   * @param params 参数
   * @return Observable<PsnPlpsQueryCommonUsedPaymentListResult>
   */
  public Observable<PsnPlpsQueryAllPaymentListResult> psnPlpsQueryAllPaymentList(PsnPlpsQueryAllPaymentListParams params) {
    return BIIClient.instance.post("PsnPlpsQueryAllPaymentList", params, PsnPlpsQueryAllPaymentListResponse.class);
    //return  BIIClient.instance.post("http://192.168.0.114:9912/life?type=all&code="+params.getCityDispNo()+"&pcode="+params.getPrvcShortName(),"PsnPlpsQueryAllPaymentList", params, PsnPlpsQueryAllPaymentListResponse.class);
  }

  /**
   * 3.1 001 PsnPlpsQueryChildrenMenus查询民生缴费子菜单
   * @param params
   * @return
   */
  public Observable<List<PsnPlpsQueryChildrenMenusResult.ChildrenMenuBean>> psnPlpsQueryChildrenMenus(PsnPlpsQueryChildrenMenusParams params) {
    return BIIClient.instance.post("PsnPlpsQueryChildrenMenus", params, PsnPlpsQueryChildrenMenusResponse.class);
  }

  /**
   * 3.16 016  PsnPlpsDeleteCommonUsedPaymentList删除常用缴费项目
   * @param params
   * @return
   */
  public Observable<PsnPlpsDeleteCommonUsedPaymentListResult> deleteCommonUsedPaymentList(PsnPlpsDeleteCommonUsedPaymentListParams params) {
    return BIIClient.instance.post("PsnPlpsDeleteCommonUsedPaymentList", params, PsnPlpsDeleteCommonUsedPaymentListResponse.class);
  }

}