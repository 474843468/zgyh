package com.boc.bocsoft.mobile.bocyun.service;

import com.boc.bocsoft.mobile.bocyun.common.client.YunClient;
import com.boc.bocsoft.mobile.bocyun.model.UBAS000001.UserPortraitGetParam;
import com.boc.bocsoft.mobile.bocyun.model.UBAS000001.UserPortraitGetRespone;
import com.boc.bocsoft.mobile.bocyun.model.UBAS000001.UserPortraitGetResult;
import com.boc.bocsoft.mobile.bocyun.model.UBAS000002.UserPortraitUpdateParam;
import com.boc.bocsoft.mobile.bocyun.model.UBAS000002.UserPortraitUpdateRespone;
import com.boc.bocsoft.mobile.bocyun.model.UBAS000005.UserLastAccountIdGetParam;
import com.boc.bocsoft.mobile.bocyun.model.UBAS000005.UserLastAccountIdGetRespone;
import com.boc.bocsoft.mobile.bocyun.model.UBAS000005.UserLastAccountIdGetResult;
import com.boc.bocsoft.mobile.bocyun.model.UBAS000006.UserLastAccountIdUpdateParam;
import com.boc.bocsoft.mobile.bocyun.model.UBAS000006.UserLastAccountIdUpdateRespone;
import com.boc.bocsoft.mobile.bocyun.model.UBAS000009.UBAS000009Param;
import com.boc.bocsoft.mobile.bocyun.model.UBAS000009.UBAS000009Respone;
import com.boc.bocsoft.mobile.bocyun.model.UBAS000009.UBAS000009Result;
import com.boc.bocsoft.mobile.bocyun.model.UBAS010001.UBAS010001Param;
import com.boc.bocsoft.mobile.bocyun.model.UBAS010001.UBAS010001Respone;
import com.boc.bocsoft.mobile.bocyun.model.UBAS010001.UBAS010001Result;
import com.boc.bocsoft.mobile.bocyun.model.UBAS020001.UserLoginInfoUpdateParam;
import com.boc.bocsoft.mobile.bocyun.model.UBAS020001.UserLoginInfoUpdateRespone;
import rx.Observable;

/**
 * 云接口
 */
public class YunService {

  /**
   * UBAS000001  查询客户头像
   * @param params
   * @return
   */
  public Observable<UserPortraitGetResult> getUserPortrait (UserPortraitGetParam params) {
    return YunClient.instance.post("UBAS000001", params,  UserPortraitGetRespone.class);
  }

  /**
   * UBAS000002   更新客户头像
   * @param params
   * @return
   */
  public Observable<Integer> updateUserPortrait (UserPortraitUpdateParam params) {
    return YunClient.instance.post("UBAS000002", params,  UserPortraitUpdateRespone.class);
  }

  /**
   * UBAS000005	 查询客户上一次交易使用的账户ID
   * @param params
   * @return
   */
  public Observable<UserLastAccountIdGetResult> getUserLastAccountId (UserLastAccountIdGetParam params) {
    return YunClient.instance.post("UBAS000005", params,  UserLastAccountIdGetRespone.class);
  }

  /**
   * UBAS000006  更新客户上一次交易使用的账户ID
   * @param params
   * @return
   */
  public Observable<Integer> updateUserLastAccountId (UserLastAccountIdUpdateParam params) {
    return YunClient.instance.post("UBAS000006", params,  UserLastAccountIdUpdateRespone.class);
  }

  /**
   * UBAS000009   查询客户云备份信息所需交易
   * @param params
   * @return
   */
  public Observable<UBAS000009Result> getUserCloudInfo (UBAS000009Param params) {
    return YunClient.instance.post("UBAS000009", params,  UBAS000009Respone.class);
  }

  /**
   *UBAS010001  查询码表参数
   * @param params
   * @return
   */
  public Observable<UBAS010001Result> getDirct (UBAS010001Param params) {
    return YunClient.instance.post("UBAS010001", params,  UBAS010001Respone.class);
  }

  /**
   * UBAS020001	 信息采集-客户登录信息
   * @param params
   * @return
   */
  public Observable<Integer> updateLoginInfo(UserLoginInfoUpdateParam params) {
    return YunClient.instance.post("ubas020001", params,  UserLoginInfoUpdateRespone.class);
  }
}
