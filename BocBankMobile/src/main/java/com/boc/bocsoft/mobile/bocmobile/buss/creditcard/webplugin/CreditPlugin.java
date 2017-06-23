package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.webplugin;

import android.content.Intent;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.userwidget.echarsview.GsonTools;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.webplugin.model.AccountInfo;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.webplugin.model.CertifInfo3DBean;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dingeryue on 2016年12月27.
 */

public class CreditPlugin extends CordovaPlugin {
  private final String TAG = "CreditPlugin";
  /**
   * 获取账户信息
   */
  private final String ACTION_GETACCOUNTINFO = "getAccountInfo";
  /**
   * 设置回显结果
   */
  private final String ACTION_SHOWRESULT= "showResult";

  /**
   * 调摄像头获取指定信息
   */
  private final String ACTION_GOTONATIVE = "goToNative";

  private final String ACTION_GETINFOBYCAMERA  = "getInfoByCamera";

  /**
   * 全球交易人民币记账功能查询
   */
  private final String ACTION_QUERYCHARGEONRMBACCOUNT  = "queryChargeOnRMBAccount";

  /**
   * 外币账单自动购汇设置查询
   */
  private final String ACTION_QUERYFOREIGNPAYOFFSTATUS  = "queryForeignPayOffStatus";

  private final String ACTION_QUERY3DCERTIFINFO = "query3DCertifInfo";

  private PluginCallback uiCallback;

  @Override public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);
    LogUtils.d(TAG,"插件初始化:"+this.getClass());
  }

  @Override public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext)
      throws JSONException {

    LogUtils.d(TAG,"exec:"+action+"  args:"+args.get(0));

    switch (action){
      case ACTION_GETACCOUNTINFO:
        //获取账户信息
        getAccountInfo(args,callbackContext);
        return true;
      case ACTION_SHOWRESULT://回显操作结果
        showResult(args,callbackContext);
        return true;
      case ACTION_GOTONATIVE://调用本地页面
        goToNative(args,callbackContext);
        return true;
      case ACTION_GETINFOBYCAMERA://调用相机
        getInfoByCamera(args,callbackContext);
        return true;
      case ACTION_QUERYCHARGEONRMBACCOUNT:
        //全球交易人民币记账功能查询
        queryChargeOnRMBAccount(args,callbackContext);
        return true;
      case ACTION_QUERYFOREIGNPAYOFFSTATUS:
        //外币账单自动购汇设置查询
        queryForeignPayOffStatus(args,callbackContext);
        return true;
      case ACTION_QUERY3DCERTIFINFO:
        query3DCertifInfo(args,callbackContext);
        return true;
      default:
        //TODO
        cameraResultCallBack.error("no action "+action+" invoke exist");
        return true;
    }
  }

  private void getAccountInfo(CordovaArgs args, final CallbackContext callbackContext){

    if(uiCallback == null){
      return;
    }
    AccountInfo accountInfo = uiCallback.getAccountInfo();
    if(accountInfo == null){
      callbackContext.error("获取数据失败");
      return;
    }
    callbackContext.success(GsonTools.toJson(accountInfo));
  }

  private void showResult(final CordovaArgs args, final CallbackContext callbackContext){

    JSONObject jsonObject = args.optJSONObject(0);
    final String msg = jsonObject.optString("showResultStr");

    execOnMainThread(new Runnable() {
      @Override public void run() {
        if(uiCallback == null){

        }else{
          uiCallback.showResult(msg);
          callbackContext.success();
        }
      }
    });
  }


  private void goToNative(final CordovaArgs args, final CallbackContext callbackContext){
    JSONObject jsonObject = args.optJSONObject(0);
    final String page =  jsonObject.optString("page");
    //0：当前页，即先前打开H5页面时原生所处的页面；
    //1：卡详情页
    //2：手机银行首
    if(uiCallback == null){
      //TODO

      return;
    }
    execOnMainThread(new Runnable() {
      @Override public void run() {
        uiCallback.goToNative(page);
      }
    });

  }

  private void getInfoByCamera(final CordovaArgs args, final CallbackContext callbackContext){
    JSONObject jsonObject = args.optJSONObject(0);
    final String functionCode =  jsonObject.optString("functionCode");
    if(uiCallback == null){
      //TODO
      return;
    }
    execOnMainThread(new Runnable() {
      @Override public void run() {
        cameraResultCallBack = callbackContext;
        uiCallback.getInfoByCamera(functionCode);
      }
    });
  }

  /**
   * 全球交易人民币记账功能查询
   * @param args
   * @param callbackContext
   */
  private void queryChargeOnRMBAccount( CordovaArgs args, final CallbackContext callbackContext){
    JSONObject jsonObject = args.optJSONObject(0);
    String accountId = jsonObject.optString("accountId");
    if(uiCallback == null){
      //TODO
      return;
    }
    boolean b = uiCallback.queryChargeOnRMBAccount(accountId);
    JSONObject result = new JSONObject();
    try {
      result.put("openFlag",b);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    callbackContext.success(result.toString());
  }

  /**
   * 外币账单自动购汇设置查询
   * @param args
   * @param callbackContext
   */
  private void queryForeignPayOffStatus(CordovaArgs args, final CallbackContext callbackContext){
    JSONObject jsonObject = args.optJSONObject(0);
    String accountId = jsonObject.optString("accountId");
    if(uiCallback == null){
      //TODO
      return;
    }
    String s = uiCallback.queryForeignPayOffStatus(accountId);
    JSONObject result = new JSONObject();
    try {
      result.put("foreignPayOffStatus",s);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    callbackContext.success(result.toString());
  }

  /**
   * 3D安全认证查询
   * @param args
   * @param callbackContext
   */
  private void query3DCertifInfo(CordovaArgs args, final CallbackContext callbackContext){
    JSONObject jsonObject = args.optJSONObject(0);
    String accountId = jsonObject.optString("accountId");
    if(uiCallback == null){
      //TODO
      return;
    }
    CertifInfo3DBean certifInfo3DBean = uiCallback.query3DCertifInfo(accountId);
    if(certifInfo3DBean == null){
      callbackContext.error("获取数据失败");
    }else{
      callbackContext.success(GsonTools.toJson(certifInfo3DBean));
    }

  }



  private CallbackContext cameraResultCallBack;
  private void onGetInfoByCameraResult(boolean isSuccess,String msg){
    if(cameraResultCallBack != null){
      if(isSuccess){
        cameraResultCallBack.success("{creditCardNum="+msg+"}");
      }else{
        cameraResultCallBack.error(msg);
      }
    }
  }

  private void execOnMainThread(Runnable runnable){
    this.cordova.getActivity().runOnUiThread(runnable);
  }

  /**
   * 设置callback， 使插件可以调用Native（UI 或者 Presenter）方法
   * @param callBack
   */
  public void setPluginCallBack(PluginCallback callBack){
    this.uiCallback = callBack;
  }
  @Override public Boolean shouldAllowBridgeAccess(String url) {
    LogUtils.d("TAG","shouldAllowBridgeAccess,url:"+url);
    return true;
  }

  @Override public void onNewIntent(Intent intent) {
    LogUtils.d(TAG,"-onNewIntent--");
    super.onNewIntent(intent);
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);
  }
}
