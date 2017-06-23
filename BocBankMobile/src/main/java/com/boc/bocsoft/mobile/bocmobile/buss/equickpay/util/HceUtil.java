package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.util;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.ApplicationModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.hcecardlist.HceCardListQueryViewModel;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.zc.pers.DataManager;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * @author mlp5474
 */
@SuppressLint("NewApi")
public class HceUtil {


    /**
     * 判断android版本是否支持HCE
     *
     * @param androidVersion
     * @return
     */
    public static boolean checkAndroidVersion(String androidVersion) {

        // 判断是否为空
        if (androidVersion == null) {
            return false;
        }

        // 判断是否是5.0或6.0版本,google 5.0以上的版本号，不再是数字的了，google也未做任何解释。
        if (androidVersion.trim().equalsIgnoreCase("M")
                || androidVersion.trim().equalsIgnoreCase("L")) {
            return true;
        }

        String baseVersion = "4.4.1";
        String[] local, service;

        if (!StringUtils.isEmpty(baseVersion)
                && !StringUtils.isEmpty(androidVersion)) {
            local = baseVersion.split("\\.");
            service = androidVersion.split("\\.");

            int len = service.length > 3 ? 3 : service.length;

            for (int i = 0; i < len; i++) {
                try {
                    if (Integer.parseInt(local[i]) < Integer
                            .parseInt(service[i])) {
                        return true;
                    } else if (Integer.parseInt(local[i]) > Integer
                            .parseInt(service[i])) {
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }

            }
        }
        return false;
    }


    //判断手机是否已经root
    public static boolean checkPhoneIsRoot() {

        boolean isRoot = false;
        try {
            if (!new File("/system/bin/su").exists() &&
                    !new File("/system/xbin/su").exists()) {
                isRoot = false;
            } else {
                isRoot = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isRoot;
    }


    /**
     * 检查手机是否支持HCE功能,
     * 要求：
     * 1:手机是否已经root，
     * 2.支持NFC
     * 3.版本号4.4.2以上
     * 4.FEATURE_NFC_HOST_CARD_EMULATION
     *
     * @return
     */
    public static HceConstants.NFC_SUPPORT checkHceFun(Context context) {
        //boolean hceFun = true;
        HceConstants.NFC_SUPPORT hceFun = HceConstants.NFC_SUPPORT.SUPPORT;
        // Check for available NFC Adapter
        NfcAdapter mNfcAdapter;
        mNfcAdapter = NfcAdapter.getDefaultAdapter(context);

        String androidVersion = getSystemVersion();

        PackageManager packageManager = context.getPackageManager();

        if (mNfcAdapter == null) {// NFC检查
            hceFun = HceConstants.NFC_SUPPORT.NOT_SUPPORT_NFC;
        }// 安卓版本检查
        else if (!checkAndroidVersion(androidVersion)) {
            hceFun = HceConstants.NFC_SUPPORT.ANDROID_VERTION_NOT_SUPPORT;
        }// FEATURE_NFC_HOST_CARD_EMULATION 检查
        else if (!packageManager.hasSystemFeature(PackageManager.FEATURE_NFC_HOST_CARD_EMULATION)) {
            hceFun = HceConstants.NFC_SUPPORT.NOT_SUPPORT_NFC;
        }else if(checkPhoneIsRoot()){//手机是否root判断
            hceFun = HceConstants.NFC_SUPPORT.ROOT;
        }
        return HceConstants.NFC_SUPPORT.SUPPORT;
    }


    /**
     * 获得当前安卓系统版本
     *
     * @return 返回手机android的当前版本，类型为String
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }




    /**
     * 清除HCE模块环境（销户）
     */
    public static String clearHceEnv(Context context, int appType) {
        DataManager data = new DataManager(context);
        String rtn = "";

        rtn = data.clearHCEEnvironment(appType);

        return rtn;
    }

    /**
     * 设置HCE默认支付卡
     */
    public static String setHceDefaultApp(Context context, int appType) {
        DataManager data = new DataManager(context);
        String rtn = "";

        rtn = data.setDefaultApp(appType);

        return rtn;
    }

    /**
     * 获取HCE卡序列号
     *
     * @param context
     * @return
     */
    public static String getHceCardNoOrSerialNo(Context context, int appType, String infoTag) {
        DataManager data = new DataManager(context);
        String rtn = "";

        rtn = data.getApplicationInfo(appType, infoTag);

        return rtn;
    }


    /**
     * 获取限制密钥剩余次数（16进制）
     */
    public static int getLimitKeyLetfTimes(Context context, int appType) {
        DataManager data = new DataManager(context);
        // 可再申请限制密钥最大次数
        int leftApplyNum = 0;
        // HCE控件能存储限制密钥的最大次数
        int maxNum = 0;
        // 手机本地剩余限制密码次数
        int leftTimes = 0;

        String leftApplyNumStr = data.getLimitKeyLeftTimes(appType);
        String maxNumStr = data.getLimitKeyMaxSaveNumber(appType);

        if (leftApplyNumStr.startsWith(HceConstants.HCE_SUCCEED) && leftApplyNumStr.length() > 4) {
            leftApplyNum = Integer.parseInt(leftApplyNumStr.substring(4), 16);
        }

        if (maxNumStr.startsWith(HceConstants.HCE_SUCCEED) && maxNumStr.length() > 4) {
            maxNum = Integer.parseInt(maxNumStr.substring(4), 16);
        }

        leftTimes = maxNum - leftApplyNum;

        return leftTimes;
    }

    /**
     * 初始化HCE模块环境（个人化）
     */
    public static String initHceEnv(Context context, ApplicationModel applicationModel, int appType) {
        DataManager data = new DataManager(context);
        InputStream input = null;
        String resultStr = null;
        try {
            byte[] cardFileByte = Base64.decode(applicationModel.getCardData(),Base64.DEFAULT);//此方法有修改
            input = new ByteArrayInputStream(cardFileByte);
            resultStr = data.initHCEEnvironment(input, (ArrayList) applicationModel.getKeys(), appType);
            LogUtil.d("============initHceEnv==============resultStr="+resultStr);
            // 本地化卡数据成功
            if (HceConstants.HCE_SUCCEED.equals(resultStr)) {
                // 获取默认应用
                String defaultApp = getDefaultPayCardBrand(context);
                if (!StringUtils.isEmpty(defaultApp)) {
                    // 已有默认应用
                    data.setDefaultApp(getApplicationType(defaultApp));
                } else {
                    // 无则设置默认应用
                    data.setDefaultApp(appType);
                }

          } else {
                LogUtil.d("==========本地化卡数据成功============resultStr="+resultStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("e 闪付控件异常"+e.getMessage());
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return resultStr;
    }

    /**
     * 获取默认支付卡的卡品牌，返回的卡品牌和ICCD卡列表中卡品牌一致
     *
     * @param context
     * @return
     */
    public static String getDefaultPayCardBrand(Context context) {
        String cardBrand = "";
        DataManager data = new DataManager(context);
        String defaultApp = data.getDefaultApp();
        Log.i("defaultApp", defaultApp);
        /**
         * 设置控件卡品牌映射到卡列表里面的卡品牌
         */
        if (defaultApp != null && defaultApp.length() > 4) {
            /**
             * ICCD卡列表返回卡组织：01-MasterCard贷记；11-MasterCard借记；02-PBOC贷记；12-PBOC借记；
             * 03-VISA贷记；13-VISA借记
             */
            if (defaultApp.startsWith(HceConstants.HCE_SUCCEED
                    + HceConstants.Master_APP)) {
                cardBrand = HceConstants.MasterTypeStr;
            } else if (defaultApp.startsWith(HceConstants.HCE_SUCCEED
                    + HceConstants.PbocCredit_APP)) {
                cardBrand = HceConstants.PbocCreditTypeTypeStr;
            } else if (defaultApp.startsWith(HceConstants.HCE_SUCCEED
                    + HceConstants.Visa_APP)) {
                cardBrand = HceConstants.VisaTypeStr;
            } else if (defaultApp.startsWith(HceConstants.HCE_SUCCEED
                    + HceConstants.PbocDebit_APP)) {
                cardBrand = HceConstants.PbocDebitTypeTypeStr;
            }
        }
        return cardBrand;
    }

    /**
     * 检查该品牌HCE卡卡号
     *
     * @param cardBrand
     * @return
     */
    public static String checkIfCanceled(List<HceCardListQueryViewModel> items, String cardBrand) {  //MasterCard, PBOC_Credit, VISA, PBOC_Debit

        String hceCardNo = null;
        String cardOrg = "";

        for (int i = 0; i < items.size(); i++) {
            //cardOrg = items.get(i).getAfficardOrg();
            cardOrg = items.get(i).getCardBrand();

            if (getCardOrg(cardBrand).equals(cardOrg)) {
                //hceCardNo = items.get(i).getAfficardNo();
                hceCardNo = items.get(i).getSlaveCardNo();
            }
        }

        return hceCardNo;
    }



    /**
     * 由TSM控件中卡品牌  转为卡  开户和卡列表中申请卡品牌（即卡组织）
     *
     * @param applicationType
     * @return
     */
    public static String getCardOrg(String applicationType) {
        String cardOrg = "";

        if (applicationType.equals(HceConstants.CardOrg.MasterCard.toString())) {
            cardOrg = HceConstants.MasterTypeStr;
        } else if (applicationType.equals(HceConstants.CardOrg.PBOC_Credit
                .toString())) {
            cardOrg = HceConstants.PbocCreditTypeTypeStr;
        } else if (applicationType.equals(HceConstants.CardOrg.PBOC_Debit
                .toString())) {
            cardOrg = HceConstants.PbocDebitTypeTypeStr;
        } else if (applicationType.equals(HceConstants.CardOrg.VISA.toString())) {
            cardOrg = HceConstants.VisaTypeStr;
        }

        return cardOrg;
    }

    /**
     * 获取应用类型，01-MasterCard，02-PBOC贷记，03-VISA，04-PBOC借记
     */
    public static int getApplicationType(String cardOrg) {
        int applicationType = 0;

        if ("01".equals(cardOrg)) {
            applicationType = HceConstants.CardOrg.MasterCard.ordinal() + 1;
        } else if ("02".equals(cardOrg)) {
            applicationType = HceConstants.CardOrg.PBOC_Credit.ordinal() + 1;
        } else if ("12".equals(cardOrg)) {
            applicationType = HceConstants.CardOrg.PBOC_Debit.ordinal() + 1;
        } else if ("03".equals(cardOrg)) {
            applicationType = HceConstants.CardOrg.VISA.ordinal() + 1;
        }

        return applicationType;
    }


    /**
     * 获取设备id  手机的IMEI序列
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager teleManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return teleManager.getDeviceId();
    }



    //返回已经申请过hce卡的银行卡列表
    public static List<AccountBean> hasHceCardAccountList(Context context, List<AccountBean> accountList, List<HceCardListQueryViewModel> biHceList){

        //含hce卡得银行卡列表
        List<AccountBean> hasHceCardAccountList = new ArrayList<>();
        //有效的hce卡列表数据
        List<HceCardListQueryViewModel> hceCardList = validHceCardList(context, biHceList);

        //遍历哪些银行卡含有hce卡，返回给账户列表页面
        for(int i = 0; i < hceCardList.size(); i++){
            for (int j = 0; j < accountList.size(); j++){
                if(hceCardList.get(i).getMasterCardNo().equals(accountList.get(j).getAccountNumber())){
                    hasHceCardAccountList.add(accountList.get(i));
                    break;
                }
            }
        }
        return hasHceCardAccountList;
    }


    /*
   *账户列表传参过来的从后台获取的hce卡列表，和本地的比较，获取有效的hce卡列表;
   *只需要把后台返回的hce卡列表中，本地没有的卡 剔除，就可以
   */
    private static List<HceCardListQueryViewModel> validHceCardList(Context context,List<HceCardListQueryViewModel> biHceCardList){

        String cardBrand = "";
        // 卡列表非空
        if (biHceCardList.size() > 0) {
            // 遍历支持的所有卡品牌
            for (HceConstants.CardOrg itCardOrg : HceConstants.CardOrg.values()) {//4个
                //卡品牌
                cardBrand = itCardOrg.toString();

                String hceCardNo = HceUtil.checkIfCanceled(biHceCardList, cardBrand);//和遍历得到的卡品牌相同的从卡卡号
                // TSM应用类型
                int appType = HceConstants.CardOrg.valueOf(cardBrand).ordinal() + 1;

                // HCE控件获取卡号返回字符串
                String tsmHceCardNo = "";

                // 判断本地是否已有
                try {
                    //获取HCE卡序列号
                    tsmHceCardNo = getHceCardNoOrSerialNo(context, appType, HceConstants.HCE_CARD_FLAG);
                } catch (Exception ex) {
                    return biHceCardList;
                }

                //本地没有，后台有，把后台的剔除掉就可以。
                if(!tsmHceCardNo.startsWith(HceConstants.HCE_SUCCEED)){
                    if (hceCardNo != null) {

                        // 从返回的卡列表中去掉该卡，然后去后台注销该卡
                        for (int i = 0; i < biHceCardList.size(); i++) {
                            if (biHceCardList.get(i).getSlaveCardNo().equals(hceCardNo)) {
                                biHceCardList.remove(i);
                            }
                        }

                    }
                }
            }
        }

        return biHceCardList;
    }


}
