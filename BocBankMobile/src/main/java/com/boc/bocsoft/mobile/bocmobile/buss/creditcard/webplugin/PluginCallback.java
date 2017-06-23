package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.webplugin;

import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.webplugin.model.AccountInfo;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.webplugin.model.CertifInfo3DBean;

/**
 * 信用卡 插件&native交互接口
 * Created by dingeryue on 2016年12月27.
 *
 */

public interface PluginCallback {
  AccountInfo getAccountInfo();
  void showResult(String msg);

  /**
   * 跳转本地界面
   * @param page
   *  * //0：当前页，即先前打开H5页面时原生所处的页面；
  //1：卡详情页
  //2：手机银行首
   */
  void goToNative(String page);

  /**
   * 调用相机获取信用卡号
   * @param functionCode
   */
  void getInfoByCamera(String functionCode);

  /**
   * 全球交易人民币记账功能查询
   * @param accountID
   */
  boolean queryChargeOnRMBAccount(String accountID);

  /**
   *外币账单自动购汇设置查询
   * @param accountId
   * @return
   */
  String  queryForeignPayOffStatus(String accountId);

  /**
   * 3D安全认证查询
   * @param accountId
   * @return
   */
  CertifInfo3DBean query3DCertifInfo(String accountId);
}
