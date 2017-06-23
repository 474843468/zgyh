package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.eshield;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;

import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldTipDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;

/**
 * Created by wangtong on 2016/7/6.
 */
public class AvailableCheck implements EShieldVerify.Command {

    private Activity activity;
    //事务是否停止
    private boolean isCommandSucceed = true;

    public AvailableCheck(Activity activity) {
        this.activity = activity;
    }

    //获取当前耳机状态
    private boolean getEshieldState() {
        boolean ret = false;
        AudioManager manager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        ret = manager.isWiredHeadsetOn();
        return ret;
    }

    @Override
    public boolean execute() {
        isCommandSucceed = getEshieldState();
        if (!isCommandSucceed) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new EShieldTipDialog(activity).show();
                }
            });
        }
        return isCommandSucceed;
    }

    @Override
    public void stop() {
        isCommandSucceed = false;
    }
}
