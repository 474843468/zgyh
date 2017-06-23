package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import cfca.mobile.sip.SipBox;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnSendSMSCodeToMobile.PsnSendSMSCodeParams;
import com.boc.bocsoft.mobile.bii.bus.global.service.GlobalService;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.Exception.BIIBaseSubscriber;
import com.boc.bocsoft.mobile.bocmobile.base.model.FactorBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.BaseDialog;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.framework.rx.SchedulersCompat;
import com.boc.bocsoft.mobile.framework.rx.lifecycle.RxLifecycleManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by wangtong on 2016/5/27.
 * 使用说明：在获取安全因子之后，必须调用{@link #getDefaultSecurityFactorId}来给安全组件设置安全工具列表
 */
public class SecurityVerity {

    public static final String SECURITY_VERIFY_STATE = "41943040";
    private static final String SECURITY_VERIFY_TYPE = "security_type";
    public static final int SECURITY_VERIFY_SMS = 32;
    public static final int SECURITY_VERIFY_TOKEN = 8;
    public static final int SECURITY_VERIFY_SMS_AND_TOKEN = 40;
    public static final int SECURITY_VERIFY_E_TOKEN = 4;
    public static final int SECURITY_VERIFY_DEVICE = 96;
    public static final String SECURITY_VERIFY_PASSWORD = "-1";
    private static Activity activity;
    //加密随机数
    private String randomNum;
    //会话ID
    private String conversationId;
    private SecurityVerifyDialog dialog;
    private static SecurityVerity securityVerity = null;

    //安全认证监听回掉
    public VerifyCodeResultListener verifyCodeResult;
    //安全认证数据模型
    private SecurityFactorModel factorResult;
    //默认安全认证方式
    private String defaultSecurityVerifyTypeId;
    //用户选择的安全认证方式
    private String currentSecurityVerifyTypeId;

    /**
     * 返回安全认证的安全码
     */
    public interface VerifyCodeResultListener {
        //返回选择的安全认证方式
        public void onSecurityTypeSelected(CombinListBean bean);

        //返回加密随机数和加密密文
        public void onEncryptDataReturned(String factorId, String[] randomNums,
                String[] encryptPasswords);

        //返回音频key签名数据
        public void onSignedReturn(String signRetData);
    }

    private SecurityVerity(Activity activity) {
        SecurityVerity.activity = activity;
    }

    /***
     * 获取验证实例，首次调用需要传入参数
     */
    public static SecurityVerity getInstance(Activity activity) {
        if (securityVerity == null) {
            securityVerity = new SecurityVerity(activity);
        }

        if (activity != null) {
            SecurityVerity.activity = activity;
        }
        return securityVerity;
    }

    /***
     * 获取验证实例，如何不是首次调用可以忽略参数
     */
    public static SecurityVerity getInstance() {
        return securityVerity;
    }

    public void setSecurityVerifyListener(VerifyCodeResultListener listener) {
        verifyCodeResult = listener;
    }

    private String getLastVerityType() {
        SharedPreferences preferences =
                activity.getSharedPreferences(activity.getString(R.string.app_name),
                        Context.MODE_PRIVATE);
        return preferences.getString(SECURITY_VERIFY_TYPE, "");
    }

    public void setSecurityVerifyType(String value) {
        SharedPreferences preferences =
                activity.getSharedPreferences(activity.getString(R.string.app_name),
                        Context.MODE_PRIVATE);
        preferences.edit().putString(SECURITY_VERIFY_TYPE, value).commit();
    }

    /**
     * 获取安全因子
     */
    public SecurityFactorModel getFactorSecurity() {
        return factorResult;
    }

    /**
     * 获取随机数
     */
    public String getRandomNum() {
        return randomNum;
    }

    /**
     * 获取默认安全认证方式
     */
    private CombinListBean defaultSecurityFactorIdReturned() {
        CombinListBean result = null;
        CombinListBean defaultCombin = factorResult.getDefaultCombin();
        List<CombinListBean> combinListBeen = factorResult.getCombinList();
        if (null != combinListBeen && combinListBeen.size() > 0) {
            if ((null != defaultCombin) && (contains(defaultCombin.getId()))) {
                defaultSecurityVerifyTypeId = defaultCombin.getId();
            } else if ((!"".equals(getLastVerityType())) && (contains(getLastVerityType()))) {
                defaultSecurityVerifyTypeId = getLastVerityType();
            } else {
                defaultSecurityVerifyTypeId = combinListBeen.get(0).getId();
            }
            setCurrentSecurityVerifyTypeId(defaultSecurityVerifyTypeId);
        }
        return getCombinListBean(combinListBeen);
    }

    private CombinListBean getCombinListBean(List<CombinListBean> combinListBeen) {
        CombinListBean result = null;
        for (int i = 0; i < combinListBeen.size(); i++) {
            if (combinListBeen.get(i).getId().equals(defaultSecurityVerifyTypeId)) {
                result = combinListBeen.get(i);
                break;
            }
        }
        return result;
    }

    /**
     * 安全因子列表中是否包含特定ID
     *
     * @param id: 要查询的ID
     */
    private boolean contains(String id) {
        boolean result = false;
        for (CombinListBean combin : factorResult.getCombinList()) {
            if (combin.getId().equals(id)) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * 获取安全认证方式
     */
    public String getDefaultSecurityVerifyTypeId() {
        return defaultSecurityVerifyTypeId;
    }

    /**
     * 获取用户当前选择的安全认证方式
     */
    public String getCurrentSecurityVerifyTypeId() {
        return currentSecurityVerifyTypeId;
    }

    /**
     * 设置用户当前安全认证方式
     */
    public void setCurrentSecurityVerifyTypeId(String currentSecurityVerifyTypeId) {
        this.currentSecurityVerifyTypeId = currentSecurityVerifyTypeId;
    }

    /**
     * 获取安全因子(必须调用)
     *
     * @param model 安全因子
     */
    public CombinListBean getDefaultSecurityFactorId(SecurityFactorModel model) {
        factorResult = model;
        for (CombinListBean bean : model.getCombinList()) {
            String factorId = bean.getId();
            String factorName = "";
            if (factorId.equals("32")) {
                factorName = activity.getString(R.string.security_sms_name);
                bean.setWeight(1);
            } else if (factorId.equals("8")) {
                bean.setWeight(3);
                factorName = activity.getString(R.string.security_auto_token);
            } else if (factorId.equals("40")) {
                bean.setWeight(4);
                factorName = activity.getString(R.string.security_token_phone);
            } else if (factorId.equals("4")) {
                bean.setWeight(5);
                factorName = activity.getString(R.string.security_e_shield);
            } else if (factorId.equals("96")) {
                bean.setWeight(2);
                factorName = activity.getString(R.string.security_sms_name);
            }
            bean.setName(factorName);
        }

        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object lhs, Object rhs) {
                CombinListBean left = (CombinListBean) lhs;
                CombinListBean right = (CombinListBean) rhs;
                if (left.getWeight() < right.getWeight()) {
                    return -1;
                } else if (left.getWeight() == right.getWeight()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        };
        Collections.sort(model.getCombinList(), comparator);

        return defaultSecurityFactorIdReturned();
    }

    /**
     * 显示安全认证选择对话框
     */
    public void selectSecurityType() {
        BaseDialog dialog = new SecurityVerifyChangeDialog(activity);
        dialog.show();
    }

    /**
     * 预交易后，再次确认安全因子
     *
     * @param factorList 预交易返回的安全因子
     */
    public boolean confirmFactor(List<FactorListBean> factorList) {

        if (factorList == null) {
            return false;
        }

        List<String> factorNameArray = new ArrayList<String>();
        for (FactorListBean factorItem : factorList) {
            factorNameArray.add(factorItem.getField().getName());
        }

        if (factorNameArray.size() == 1) {
            //TODO 单一安全认证方式
            if (factorNameArray.get(0).equals("Smc")) {
                currentSecurityVerifyTypeId = "32";
            } else if (factorNameArray.get(0).equals("Otp")) {
                currentSecurityVerifyTypeId = "8";
            } else if (factorNameArray.get(0).equals("_signedData")) {
                currentSecurityVerifyTypeId = "4";
            }
        } else if (factorNameArray.size() == 2) {
            //TODO 组合安全认证方式
            if (factorNameArray.contains("Smc") && factorNameArray.contains("Otp")) {
                currentSecurityVerifyTypeId = "40";
            } else if (factorNameArray.contains("Smc") && factorNameArray.contains("deviceInfo")) {
                currentSecurityVerifyTypeId = "96";
            }
        } else {
            //TODO 无安全认证方式
            currentSecurityVerifyTypeId = "-1";
        }

        return currentSecurityVerifyTypeId.equals("-1") ? false : true;
    }

    /**
     * 显示安全认证对话框
     *
     * @param random 随机数
     */
    public void showSecurityDialog(String random) {
        randomNum = random;
        int type = Integer.parseInt(currentSecurityVerifyTypeId);
        switch (type) {
            case SECURITY_VERIFY_SMS:
                dialog = new SmsVerifyDialog(activity);
                dialog.show();
                break;
            case SECURITY_VERIFY_TOKEN:
                dialog = new AutoTokenDialog(activity);
                dialog.show();
                break;
            case SECURITY_VERIFY_SMS_AND_TOKEN:
                dialog = new TokenAndSmsVerifyDialog(activity);
                dialog.show();
                break;
            case SECURITY_VERIFY_E_TOKEN:
                EShieldVerify.getInstance(activity).startEShield();
                break;
            case SECURITY_VERIFY_DEVICE:
                dialog = new SmsVerifyDialog(activity);
                dialog.show();
                break;
            default:
                break;
        }
    }

    /**
     * 显示取款密码输入框
     */
    public void showTransPasswordDialog(String type) {
        PasswordDialog dialog = new PasswordDialog(activity, type);
        dialog.setPasswordType(type);
        dialog.show();
    }

    public String getCfcaVersion() {
        return SipBox.getVersion() + "";
    }

    /**
     * 短信认证的时候，需要会话ID
     */
    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    /**
     * 获取手机验证码
     */
    public void psnSendSMSCodeToMobile() {
        final RxLifecycleManager rxLifecycleManager = new RxLifecycleManager();
        PsnSendSMSCodeParams params = new PsnSendSMSCodeParams();

        params.setConversationId(conversationId);
        GlobalService.psnSendSMSCodeToMobile(params)
                     .compose(rxLifecycleManager.<String>bindToLifecycle())
                     .compose(SchedulersCompat.<String>applyIoSchedulers())
                     .subscribe(new BIIBaseSubscriber<String>() {
                         @Override
                         public void handleException(
                                 BiiResultErrorException biiResultErrorException) {
                             dialog.sendSmsResult(false);
                             Toast.makeText(activity, biiResultErrorException.getErrorMessage(),
                                     Toast.LENGTH_SHORT).show();
                         }

                         @Override
                         public void onCompleted() {
                         }

                         @Override
                         public void onNext(String s) {
                             dialog.sendSmsResult(true);
                         }

                         @Override
                         public void commonHandleException(
                                 BiiResultErrorException biiResultErrorException) {

                         }
                     });
    }

    public static List<FactorListBean> copyFactorList(List<FactorBean> factorList) {
        List<FactorListBean> result = new ArrayList<>();
        for (FactorBean factorBean : factorList) {
            result.add(BeanConvertor.fromBean(factorBean, new FactorListBean()));
        }
        return result;
    }
}
