package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.ErrorDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.loadingDialog.GlobalLoadingDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.loadingDialog.LoadingDialogHelper;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.eshield.AvailableCheck;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.eshield.DriverConnect;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.eshield.EditPassword;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.eshield.InputPassword;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.eshield.PESABOCCallBackAdapter;
import com.boc.keydriverinterface.MEBKeyDriverInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangtong on 2016/6/2.
 */
public class EShieldVerify implements Runnable {

    private static final String LOG_TGA = EShieldVerify.class.getSimpleName();

    private static final String SN = "sequence";

    private static EShieldVerify eShieldVerify;
    //正在执行的事务
    private Command executting;
    //事务列表
    private List<Command> commands;

    private static Activity activity;
    //音频key状态监听
    private HeadsetReceiver receiver;
    //音频key对象
    private MEBKeyDriverInterface keyDriverInterface;
    //音频key回调
    private PESABOCCallBackAdapter mBOCCallBack;
    //是否需要修改音频key密码
    private volatile boolean isNeedChangePassword = false;
    //线程同步锁
    private static Object lock = new Object();
    //交易动画
    private SecurityAnimationDialog gifDialog;
    //预交易传入
    private String mPlainData;
    //错误提示对话框
    private ErrorDialog errorDialog;
    //加载对话框
    private GlobalLoadingDialog mLoadingDialog;

    //事务接口
    public interface Command {
        //开始执行事务
        public boolean execute();

        //停止事务
        public void stop();
    }

    private EShieldVerify(Activity activity) {

        EShieldVerify.activity = activity;
        commands = new ArrayList<Command>();
        initDialogs();
    }

    public static EShieldVerify getInstance(Activity activity) {
        if (eShieldVerify == null) {
            eShieldVerify = new EShieldVerify(activity);
        }

        if (activity != null && EShieldVerify.activity != activity) {
            EShieldVerify.activity = activity;
            eShieldVerify.initDialogs();
        }

        return eShieldVerify;
    }

    private void initDialogs() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gifDialog = new SecurityAnimationDialog(activity);
                errorDialog = new ErrorDialog(activity);
                errorDialog.setBtnText("确认");
                mLoadingDialog = LoadingDialogHelper.getLoadingDialog(activity, "加载中...", false);
            }
        });
    }

    public static void waitThread() throws InterruptedException {
        synchronized (lock) {
            lock.wait();
        }
    }

    public static void notifyThread() {
        synchronized (lock) {
            lock.notify();
        }
    }

    public void showErrorDialog(String errorMessage) {
        errorDialog.setErrorData(errorMessage);
        if (!errorDialog.isShowing()) {
            errorDialog.show();
        }
    }

    public void showLoadingDialog() {
        mLoadingDialog.show();
    }

    public void closeProgressDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.cancel();
        }
    }

    /**
     * 开始音频key验证
     */
    public void startEShield() {
        new Thread(this).start();
    }

    /**
     * 初始化事务
     */
    private void initCommands() {
        commands.add(new AvailableCheck(activity));
        commands.add(new DriverConnect(activity));
        commands.add(new EditPassword(activity));
        commands.add(new InputPassword(activity));
        registerHeadSetReceiver();
    }

    /**
     * 反初始化
     */
    private void destoryCommands() {
        commands.clear();
        unRegisterHeadSetReceiver();
        keyDriverInterface = null;
    }

    private void registerHeadSetReceiver() {
        receiver = new HeadsetReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.HEADSET_PLUG");
        activity.registerReceiver(receiver, filter);
    }

    /**
     * 反注册耳机监听广播
     */
    public void unRegisterHeadSetReceiver() {
        activity.unregisterReceiver(receiver);
    }

    @Override
    public void run() {
        execute();
    }

    /**
     * 开始执行事务
     */
    private void execute() {
        initCommands();
        for (int i = 0; i < commands.size(); i++) {
            executting = commands.get(i);
            if (!executting.execute()) {
                break;
            }
        }
        destoryCommands();
    }

    public PESABOCCallBackAdapter getmBOCCallBack() {
        return mBOCCallBack;
    }

    public boolean isNeedChangePassword() {
        return isNeedChangePassword;
    }

    public void setNeedChangePassword(boolean needChangePassword) {
        isNeedChangePassword = needChangePassword;
    }

    public String getmPlainData() {
        return mPlainData;
    }

    public void setmPlainData(String mPlainData) {
        this.mPlainData = mPlainData;
    }

    public class HeadsetReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra("state", 0);
            if (state == 0) {
                //e盾插口没插好
                if (executting != null) {
                    executting.stop();
                    if (keyDriverInterface != null) {
                        keyDriverInterface.disconnect();
                        keyDriverInterface = null;
                    }

                    closeProgressDialog();
                    dismissGifDialog();
                    showErrorDialog(context.getString(R.string.boc_ca_background_cancel));

                }
            } else if (state == 1) {
                //e盾插重新插好
            }
        }
    }

    public MEBKeyDriverInterface getKeyDriverInterface() {
        return keyDriverInterface;
    }

    public void setKeyDriverInterface(MEBKeyDriverInterface keyDriverInterface) {
        this.keyDriverInterface = keyDriverInterface;
    }

    private void dismissGifDialog() {
        gifDialog.dismiss();
    }

    public void showGifDialog(String title, int type) {
        gifDialog.showAnimationDialog(title, type);
    }

    public String getSequence() {
        String appName = activity.getString(R.string.app_name);
        SharedPreferences preferences = activity.getSharedPreferences(appName, 0);
        return preferences.getString(SN, "");
    }

    public void setSequence(String value) {
        String appName = activity.getString(R.string.app_name);
        SharedPreferences preferences = activity.getSharedPreferences(appName, 0);
        preferences.edit().putString(SN, value).commit();
    }

    /**
     * ca签名的回调进行初始化
     */
    public void initCACallBack() {
        mBOCCallBack = new PESABOCCallBackAdapter() {
            /**
             * 签名过程中，提示用户对签名信息进行核对的事件
             *
             * @param sessionID 调用签名方法时传入的会话ID
             **/
            @Override
            public void keySignNeedConfirm(String sessionID) {
                LogUtils.i(LOG_TGA, "keySignNeedConfirm start");
                // 启动动画
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        closeProgressDialog();
                        showGifDialog(activity.getString(R.string.security_e_shield),
                                SecurityAnimationDialog.ANIM_TRADE);


                    }
                });
                LogUtils.i(LOG_TGA, "keySignNeedConfirm end");
            }

            /**
             * 签名过程中，用户点击key设备“OK”键，完成对签名信息核对的事件
             *
             * @param sessionID 调用签名方法时传入的会话ID
             **/
            @Override
            public void keySignDidConfirm(String sessionID) {
                LogUtils.i(LOG_TGA, "keySignDidConfirm start");
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        dismissGifDialog();
                        closeProgressDialog();

                    }
                });
                LogUtils.i(LOG_TGA, "keySignDidConfirm end");
            }

            /**
             * 签名过程中，用户点击key设备“C”键，取消签名操作的事件
             *
             * @param sessionID 调用签名方法时传入的会话ID
             **/
            @Override
            public void keySignDidCancel(String sessionID) {
                LogUtils.i(LOG_TGA, "keySignDidCancel start");
                // 关闭动画 gif
                activity.runOnUiThread(new Runnable() {
                    public void run() {

                        dismissGifDialog();
                        closeProgressDialog();
                        showErrorDialog(activity.getString(R.string.boc_ca_sign_cancel));

                    }
                });
                LogUtils.i(LOG_TGA, "keySignDidCancel end");
            }

            /**
             * 点击ok,签名成功
             */
            @Override
            public void keyDidSignSuccess(final String signRetData, String sessionId) {
                LogUtils.i(LOG_TGA, "keyDidSignSuccess start");
                activity.runOnUiThread(new Runnable() {
                    public void run() {

                        dismissGifDialog();
                        closeProgressDialog();
                        if (SecurityVerity.getInstance().verifyCodeResult != null) {
                            SecurityVerity.getInstance().verifyCodeResult.onSignedReturn(signRetData);

                        }

                    }
                });
                LogUtils.i(LOG_TGA, "keyDidSignSuccess end");
            }

            /**
             * 点击ok 失败的回调
             */
            @Override
            public void keyDidSignFailWithError(final String errerMessage, String sessionId) {
                LogUtils.i(LOG_TGA, "keyDidSignFailWithError start");
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        dismissGifDialog();
                        closeProgressDialog();
                        showErrorDialog(errerMessage);
                    }
                });
                LogUtils.i(LOG_TGA, "keyDidSignFailWithError end");
            }

            /**
             * 修改PIN码过程中，提示用户对修改PIN码操作进行核对的事件
             *
             * @param remainNumber 剩余的修改PIN码操作可重试次数
             * @param sessionId    调用修改PIN码方法时传入的会话ID
             */
            @Override
            public void keyModifyPinNeedConfirm(final int remainNumber, final String sessionId) {
                LogUtils.i(LOG_TGA, "keyModifyPinNeedConfirm start");
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        closeProgressDialog();
                        showGifDialog(activity.getString(R.string.security_e_shield),
                                SecurityAnimationDialog.ANIM_MODIFY_PASSWORD);

                    }
                });
                LogUtils.i(LOG_TGA, "keyModifyPinNeedConfirm end");
            }

            @Override
            public void keyPinWarningNeedConfirm(final String remainNumber, String sessionId) {
                LogUtils.i(LOG_TGA, "keyPinWarningNeedConfirm start");
                activity.runOnUiThread(new Runnable() {
                    public void run() {

                        // 密码错误次数提醒
                        dismissGifDialog();
                        closeProgressDialog();
                        showGifDialog(activity.getString(R.string.boc_catips_gif_title_pwd_err, remainNumber),
                                SecurityAnimationDialog.ANIM_COMMON);

                    }
                });
                LogUtils.i(LOG_TGA, "keyPinWarningNeedConfirm end");
            }

            @Override
            public void keyPinWarningFailWithError(final String errerMessage, String arg1) {
                LogUtils.i(LOG_TGA, "keyPinWarningFailWithError start");
                activity.runOnUiThread(new Runnable() {
                    public void run() {

                        dismissGifDialog();
                        closeProgressDialog();
                        executting.stop();
                        showErrorDialog(errerMessage);

                    }
                });
                LogUtils.i(LOG_TGA, "keyPinWarningFailWithError end");
            }

            @Override
            public void keyPinWarningDidConfirm(String sessionId) {
                LogUtils.i(LOG_TGA, "keyPinWarningDidConfirm start");
                // 密码错误次数提醒 -- 确认 继续交易关闭dialog
                activity.runOnUiThread(new Runnable() {
                    public void run() {

                        dismissGifDialog();
                        closeProgressDialog();

                    }
                });
                LogUtils.i(LOG_TGA, "keyPinWarningDidConfirm end");

            }

            @Override
            public void keyPinWarningDidCancel(String sessionId) {
                LogUtils.i(LOG_TGA, "keyPinWarningDidCancel start");
                activity.runOnUiThread(new Runnable() {
                    public void run() {

                        dismissGifDialog();
                        closeProgressDialog();
                        showErrorDialog(activity.getString(R.string.boc_ca_sign_cancel));

                    }
                });
                LogUtils.i(LOG_TGA, "keyPinWarningDidCancel end");
            }

            @Override
            public void keyDidModifyPINSuccess(String s) {
                LogUtils.i(LOG_TGA, "keyDidModifyPINSuccess start");
                activity.runOnUiThread(new Runnable() {
                    public void run() {

                        dismissGifDialog();
                        closeProgressDialog();
                        Toast.makeText(activity, activity.getString(R.string.boc_password_modify_success),
                                Toast.LENGTH_SHORT).show();

                    }
                });
                LogUtils.i(LOG_TGA, "keyDidModifyPINSuccess end");
            }

            @Override
            public void keyModifyPinDidConfirm(String s) {
                LogUtils.i(LOG_TGA, "keyModifyPinDidConfirm start");
                activity.runOnUiThread(new Runnable() {
                    public void run() {

                        dismissGifDialog();
                        closeProgressDialog();
                    }
                });
                LogUtils.i(LOG_TGA, "keyModifyPinDidConfirm end");
            }

            @Override
            public void keyModifyPinDidCancel(String s) {
                LogUtils.i(LOG_TGA, "keyModifyPinDidCancel start");
                activity.runOnUiThread(new Runnable() {
                    public void run() {

                        dismissGifDialog();
                        closeProgressDialog();
                        executting.stop();
                        showErrorDialog(activity.getString(R.string.boc_ca_sign_cancel));

                    }
                });
                LogUtils.i(LOG_TGA, "keyModifyPinDidCancel end");
            }

            @Override
            public void keyDidModifyPINFailWithError(final String message,
                                                     String sessionId) {
                LogUtils.i(LOG_TGA, "keyModifyPinDidCancel start");
                activity.runOnUiThread(new Runnable() {
                    public void run() {

                        dismissGifDialog();
                        closeProgressDialog();
                        showErrorDialog(message);

                    }
                });
                LogUtils.i(LOG_TGA, "keyModifyPinDidCancel end");

            }
        };
    }
}
