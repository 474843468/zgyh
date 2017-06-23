package com.boc.bocsoft.mobile.bocmobile.base.cordova;

import android.content.Context;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bocmobile.base.cordova.model.DevicePrintResult;
import com.boc.bocsoft.mobile.bocmobile.base.cordova.model.SecurityVerityCordovaParams;
import com.boc.bocsoft.mobile.bocmobile.base.cordova.model.SecurityVerityCordovaResult;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DeviceInfoUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.common.utils.gson.GsonUtils;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 作者：XieDu
 * 创建时间：2016/12/9 15:40
 * 描述：
 */
public class CommonBridgePlugin extends CordovaPlugin {

    // 显示token
    private final static String ACTION_SHOW_SECURITY_VERITY_PAGE = "showSecurityVerifyPage";
    private final static String ACTION_GET_DEVICE_PRINT = "getDevicePrint";
    private Context mContext;

    @Override
    public boolean execute(String action, CordovaArgs args, final CallbackContext callbackContext)
            throws JSONException {

        try {
            mContext = this.webView.getContext();
            // 调用token输入画面
            if (ACTION_SHOW_SECURITY_VERITY_PAGE.equals(action)) {
                showSecurityVerifyPage(args, callbackContext);
            } else if (ACTION_GET_DEVICE_PRINT.equals(action)) {
                getDevicePrint(args, callbackContext);
            }

            while (!callbackContext.isFinished()) {
                return true;
            }
            return false;
        } catch (Exception ex) {
            callbackContext.error(-1);
        }
        return false;
    }

    private void showSecurityVerifyPage(final CordovaArgs args,
            final CallbackContext callbackContext) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SecurityVerityCordovaParams params = null;
                final SecurityVerityCordovaResult result = new SecurityVerityCordovaResult();
                JSONObject obj;
                try {
                    obj = args.getJSONObject(0);
                    params = GsonUtils.getGson()
                                      .fromJson(obj.toString(), SecurityVerityCordovaParams.class);
                } catch (JSONException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                if (params == null) {
                    return;
                }
                EShieldVerify.getInstance(ActivityManager.getAppManager().currentActivity())
                             .setmPlainData(params.getPlainData());
                SecurityVerity.getInstance(ActivityManager.getAppManager().currentActivity()).setConversationId(params.getConversationId());
                SecurityVerity.getInstance(ActivityManager.getAppManager().currentActivity())
                              .setSecurityVerifyListener(
                                      new SecurityVerity.VerifyCodeResultListener() {
                                          @Override
                                          public void onSecurityTypeSelected(CombinListBean bean) {

                                          }

                                          @Override
                                          public void onEncryptDataReturned(String factorId,
                                                  String[] randomNums, String[] encryptPasswords) {
                                              result.setState(SecurityVerity.SECURITY_VERIFY_STATE);
                                              result.setActiv(SecurityVerity.getInstance()
                                                                            .getCfcaVersion());
                                              int type = Integer.parseInt(factorId);
                                              switch (type) {
                                                  case SecurityVerity.SECURITY_VERIFY_SMS:
                                                      result.setSms(encryptPasswords[0]);
                                                      result.setSms_RC(randomNums[0]);
                                                      break;
                                                  case SecurityVerity.SECURITY_VERIFY_TOKEN:
                                                      result.setOtp(encryptPasswords[0]);
                                                      result.setOtp_RC(randomNums[0]);

                                                      break;
                                                  case SecurityVerity.SECURITY_VERIFY_SMS_AND_TOKEN:
                                                      result.setOtp(encryptPasswords[0]);
                                                      result.setOtp_RC(randomNums[0]);
                                                      result.setSms(encryptPasswords[1]);
                                                      result.setSms_RC(randomNums[1]);
                                                      break;
                                                  case SecurityVerity.SECURITY_VERIFY_DEVICE:
                                                      result.setSms(encryptPasswords[0]);
                                                      result.setSms_RC(randomNums[0]);
                                                      DeviceInfoModel deviceInfoModel =
                                                              DeviceInfoUtils.getDeviceInfo(
                                                                      mContext,
                                                                      SecurityVerity.getInstance()
                                                                                    .getRandomNum());
                                                      result.setDeviceInfo(
                                                              deviceInfoModel.getDeviceInfo());
                                                      result.setDeviceInfo_RC(
                                                              deviceInfoModel.getDeviceInfo_RC());
                                                      result.setDevicePrint(
                                                              DeviceInfoUtils.getDevicePrint(
                                                                      ActivityManager.getAppManager()
                                                                                     .currentActivity()));
                                                      break;
                                                  default:
                                                      break;
                                              }
                                              callbackContext.success(
                                                      GsonUtils.getGson().toJson(result));
                                          }

                                          @Override
                                          public void onSignedReturn(String signRetData) {
                                              result.set_signedData(signRetData);
                                              callbackContext.success(
                                                      GsonUtils.getGson().toJson(result));
                                          }
                                      });

                // 显示安全认证对话框
                if (SecurityVerity.getInstance()
                                  .confirmFactor(
                                          SecurityVerity.copyFactorList(params.getPageClass()))) {
                    SecurityVerity.getInstance().showSecurityDialog(params.getRandomKey());
                }
            }
        });
    }

    private void getDevicePrint(CordovaArgs args, final CallbackContext callbackContext) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DevicePrintResult result = new DevicePrintResult();
                result.setDevicePrint(DeviceInfoUtils.getDevicePrint(
                        ActivityManager.getAppManager().currentActivity()));
                callbackContext.success(GsonUtils.getGson().toJson(result));
            }
        });
    }

    @Override
    public Boolean shouldAllowBridgeAccess(String url) {
        return true;
    }
}
