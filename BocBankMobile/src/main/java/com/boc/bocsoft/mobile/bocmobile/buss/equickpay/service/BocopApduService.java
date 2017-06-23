package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.service;


import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocop.sdk.util.Logger;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;

import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.util.HceConstants;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.zc.card.Card;
import com.zc.card.DCPSE;
import com.zc.card.MasterCard;
import com.zc.card.PbocCard;
import com.zc.card.VisaCard;
import com.zc.utils.BasicUtil;
import com.zc.utils.HexCoder;
import com.zc.utils.ISO7816;

import java.io.File;
import java.io.FileOutputStream;


/**
 * Created by gengjunying on 2016/12/5.
 */


@SuppressLint("NewApi")
public class BocopApduService extends HostApduService {
    public Card card;

    private byte[] hceCommunicatePos(byte[] apdubuf) {
        String response = "";
        String hex = "";//StringUtils.bytesToHexString(apdubuf);
        LogUtil.d("apdubuf = " + hex);
        saveInfo2File(hex + "\n");

        short sClaIns = BasicUtil.getShort(apdubuf, ISO7816.OFFSET_CLA);

        if (sClaIns == (short) 0x00A4) {
            short sLc = apdubuf[ISO7816.OFFSET_LC];
            LogUtil.d("sClaIns == (short) 0x00A4 Ok");
            saveInfo2File("sClaIns == (short) 0x00A4 Ok" + "\n");
            String aid = BasicUtil.bytesToHexString(apdubuf,
                    ISO7816.OFFSET_CDATA, sLc);

            LogUtil.d("aid = " + aid);
            saveInfo2File("aid = " + aid + "\n");
            if (aid.compareToIgnoreCase(HceConstants.PPSE_AID) == 0) {

                DCPSE ppse = new DCPSE(getApplicationContext());

                try {
                    response = ppse.process(apdubuf);
                    LogUtil.d("PPSE_AID response = " + response);
                    saveInfo2File("PPSE_AID response = " + response + "\n");
                } catch (Exception e) {
                    response = e.getMessage();
                }

            } else {

                if (aid.compareToIgnoreCase(HceConstants.MasterCard_AID) == 0) {

                    card = new MasterCard(getApplicationContext());

                } else if (aid.compareToIgnoreCase(HceConstants.VISA_AID) == 0) {

                    card = new VisaCard(getApplicationContext());

                } else if (aid.compareToIgnoreCase(HceConstants.PBOC_Credit_AID) == 0) {

                    card = new PbocCard(getApplicationContext(), true);

                } else if (aid.compareToIgnoreCase(HceConstants.PBOC_Debit_AID) == 0) {

                    card = new PbocCard(getApplicationContext(), false);

                } else {
                    return HexCoder.decode("6A82");
                }

                try {
                    response = card.process(apdubuf);
                    LogUtil.d("sClaIns == (short) 0x00A4 response = " + response);
                    saveInfo2File("sClaIns == (short) 0x00A4 response = " + response + "\n");
                } catch (Exception e) {
                    response = e.getMessage();
                }
            }

        } else {

            try {
                response = card.process(apdubuf);
                LogUtil.d("sClaIns != (short) 0x00A4 response = " + response);
                saveInfo2File("sClaIns != (short) 0x00A4 response = " + response + "\n");
            } catch (Exception e) {
                response = e.getMessage();
            }
        }

        Logger.d("apdu out : ", response);

        return HexCoder.decode(response);
    }


    /**
     * 保存错误信息到文件中
     *
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private boolean saveInfo2File(String str) {

        try {
            long timestamp = System.currentTimeMillis();
            String fileName = "hce-" + timestamp + ".log";
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory().getPath() + "/hce/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(str.getBytes());
                fos.close();
            }
            return true;
        } catch (Exception e) {
            Log.e("TAG", "an error occured while writing file...", e);
        }
        return false;
    }

    @Override
    public byte[] processCommandApdu(byte[] apdubuf, Bundle extras) {
        /**
         * 支付时是否需要手机屏幕解锁，true-需要；false-不需要
         */
//        boolean requireDeviceUnlock = SharedPreferenceUtils
//                .getBooleanValueFromSP(SPKeys.spName,
//                        SPKeys.requireDeviceUnlock, false);
        boolean requireDeviceUnlock = false;

        if (!requireDeviceUnlock) {
            LogUtils.d("不需要手机解锁即可支付");
            return hceCommunicatePos(apdubuf);
        } else {
            /**
             * 判断手机屏幕是否已解锁
             */
            LogUtils.d("需要手机解锁才可支付");
            KeyguardManager mKeyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

            // true-表示有两种状态：a、屏幕是黑的 b、目前正处于解锁状态;false-表示目前未锁屏
            boolean flag = mKeyguardManager.inKeyguardRestrictedInputMode();

            if (!flag) {
                LogUtils.d("手机已经解锁");
                return hceCommunicatePos(apdubuf);
            } else {
                LogUtils.d("手机并未解锁，不能支付");
                return HexCoder.decode("6A82");
            }
        }
    }

    @Override
    public void onDeactivated(int reason) {

    }


}
