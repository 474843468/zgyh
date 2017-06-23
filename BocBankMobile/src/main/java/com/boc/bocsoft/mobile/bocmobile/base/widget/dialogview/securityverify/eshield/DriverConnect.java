package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.eshield;

import android.app.Activity;
import android.text.TextUtils;

import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldSequenceDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;
import com.boc.device.key.KeyConst;
import com.boc.keydriverinterface.MEBBocKeyMerchantType;
import com.boc.keydriverinterface.MEBKeyDriverCommonModel;
import com.boc.keydriverinterface.MEBKeyDriverInterface;

/**
 * Created by wangtong on 2016/7/6.
 */
public class DriverConnect implements EShieldVerify.Command {
	
	private final static String LOG_TAG = DriverConnect.class.getSimpleName();
	
    private Activity activity;
    //厂商序列号
    private String mSn;

    private boolean isCommandSucceed = true;

    private EShieldSequenceDialog dialog;

    public DriverConnect(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean execute() {
    	LogUtils.i(LOG_TAG,  "execute: 开始执行....");
        String defaultSn = EShieldVerify.getInstance(activity).getSequence();
        LogUtils.i(LOG_TAG,  "defaultSn: 保存的SN为:" + defaultSn);
        if (TextUtils.isEmpty(defaultSn)) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                	LogUtils.i(LOG_TAG,  "当前保存的SN为空:" + "请用户输入6为的sn号");
                    dialog = new EShieldSequenceDialog(activity);
                    dialog.show();
                }
            });

            try {
                EShieldVerify.waitThread();
            } catch (InterruptedException e) {
                e.printStackTrace();
                isCommandSucceed = false;
            }
            mSn = dialog.getSequence();
            LogUtils.i(LOG_TAG,  "用户输入的SN号为:" + mSn);
        } else {
            mSn = defaultSn;
        }

        initDriver();

        return isCommandSucceed;
    }

    @Override
    public void stop() {
        isCommandSucceed = false;
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }

    /**
     * 对driver 进行初始化
     */
    public void initDriver() {
        // 如果序列号为空自动匹配厂商，否则根据序列号批判
        MEBKeyDriverInterface driver = EShieldVerify.getInstance(activity).getKeyDriverInterface();
        EShieldVerify.getInstance(activity).initCACallBack();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EShieldVerify.getInstance(activity).showLoadingDialog();
            }
        });

        if (driver == null) {
        	LogUtils.i(LOG_TAG,  "driver:为空...");
            if (TextUtils.isEmpty(mSn)) {
            	LogUtils.i(LOG_TAG,  "mSn:为空...");
                driver = new MEBKeyDriverInterface(activity);
                EShieldVerify.getInstance(activity).setKeyDriverInterface(driver);
            } else {
            	LogUtils.i(LOG_TAG,  "mSn:不为空...");
                
                if (mSn != null && mSn.length() >= 6) {
                    String tmp = mSn.substring(5, 6);
                    if ("4".equals(tmp) || "5".equals(tmp) || "6".equals(tmp)) {
                    	MEBBocKeyMerchantType type = findMerchantType(tmp);
                       // driver.changeDriverBySn(mSn);
                    	driver = new MEBKeyDriverInterface(activity, type);
                    } else {
                    	driver = new MEBKeyDriverInterface(activity);
                    }
                } else {
                	driver = new MEBKeyDriverInterface(activity);
                }
                EShieldVerify.getInstance(activity).setKeyDriverInterface(driver);
            }
        }

        if (driver != null) {
            // 判断是否需要修改密码
            final MEBKeyDriverCommonModel model = driver.getKeyDriverInfoBeforeLogin(activity);

            // 连接失败
            if (model.mError.getErrorId() != KeyConst.BOC_SUCCESS) {
                isCommandSucceed = false;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        EShieldVerify.getInstance(activity).closeProgressDialog();
                        EShieldVerify.getInstance(activity).showErrorDialog(model.mError.getErrorMessage());
                    }
                });
            } else {
                // 是否修改pin码
                boolean isPinNeedModify = model.mInfo.getIsPinNeedModify();
                String sn = model.mInfo.getKeySN();
                LogUtils.i(LOG_TAG,  "isPinNeedModify：" + "是否首次使用" + isPinNeedModify);
                LogUtils.i(LOG_TAG,  "sn：" + "设备的sn号：" + sn);
                if (null == sn || "".equals(sn.trim())) {
                	 isCommandSucceed = false;
                     activity.runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             EShieldVerify.getInstance(activity).closeProgressDialog();
                             EShieldVerify.getInstance(activity).showErrorDialog("设备连接失败，请稍后再试");
                         }
                     });
                } else {
                	 isCommandSucceed = true;
                	 EShieldVerify.getInstance(activity).setNeedChangePassword(isPinNeedModify);
                     EShieldVerify.getInstance(activity).setSequence(sn);
                     activity.runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             EShieldVerify.getInstance(activity).closeProgressDialog();
                         }
                     });
                }
               
            }
        } else {
            isCommandSucceed = false;
        }
    }
    
    private MEBBocKeyMerchantType findMerchantType(String snType){
    	if ("4".equals(snType)) {
    		return MEBBocKeyMerchantType.BOCKeyMerchantWatchData;
    	} else if ("5".equals(snType)){
    		return MEBBocKeyMerchantType.BOCKeyMerchantFeiTian;
    	} else if ("6".equals(snType)){
    		return MEBBocKeyMerchantType.BOCKeyMerchantWenDingChuang;
    	} else {
    		return MEBBocKeyMerchantType.BOCKeyMerchantUnknown;
    	}
    	
    }
}
