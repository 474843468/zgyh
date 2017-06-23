package com.boc.bocsoft.mobile.bocmobile.base.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Contacts.People;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.DisplayMetrics;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.cfca.mobile.device.DeviceSecCryptor;
import com.cfca.mobile.device.SecResult;
import com.cfca.mobile.log.CodeException;
import java.io.File;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by wangtong on 2016/7/14.
 */
public class DeviceInfoUtils {

    private static TelephonyManager tm;
    private static WifiManager wifi;
    private static DisplayMetrics displayMetrics;
    private static String loc;
    private static LocationManager locationManager;
    //通信录个数
    private static int mContractCount;
    //基站cid
    private static String mphoneDateCid;
    private static ContentResolver resolver;
    private static TelephonyManager mTelNet;

    private static int lastStrength = 0;

    public static DeviceInfoModel getDeviceInfo(Context context, String random) {
        String operatorId = ApplicationContext.getInstance().getUser().getOperatorId();
        return getDeviceInfo(context, random, operatorId);
    }


    public static DeviceInfoModel getDeviceInfo(Context context, String random, String operatorId) {
        return  getDeviceInfo(ApplicationConst.CIPHERTYPE,ApplicationConst.OUT_PUT_VALUE_TYPE,context,random,operatorId);
    }

    public static DeviceInfoModel getDeviceInfo(int cipherType,int outPutValueType, Context context, String random, String operatorId) {

        DeviceSecCryptor instance = DeviceSecCryptor.getInstatnce();
        DeviceInfoModel deviceInfoModel = new DeviceInfoModel();
        LogUtils.d("dding",getMac()+"--device:::"+getIMEI(context)+"   wifimac:"+getMac());

        try {
            SecResult secResult = instance.getEncryptedInfo(cipherType, outPutValueType, random, getIMEI(context)+operatorId, getMac()+operatorId);

            deviceInfoModel.setDeviceInfo(secResult.getEncryptedInfo());
            deviceInfoModel.setDeviceInfo_RC(secResult.getEncryptedRC());

            return deviceInfoModel;
        } catch (CodeException e) {
            e.printStackTrace();
        }
        return  null;
    }

    /**
     * 初始化系统信息类参数
     *
     * @param act Activity对象
     */
    private static void initActFirst(Activity act) {
        tm = (TelephonyManager) act.getSystemService(Context.TELEPHONY_SERVICE);
        wifi = (WifiManager) act.getSystemService(Context.WIFI_SERVICE);
        // int sdk_level = android.os.Build.VERSION.SDK_INT ;
        /*
         * // if(sdk_level>14){ wifiP2p =
		 * (WifiP2pManager)act.getSystemService(Context.WIFI_P2P_SERVICE);
		 * channel = wifiP2p.initialize(act, act.getMainLooper(),null); // }
		 */
        displayMetrics = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // 设备主要语言
        loc = act.getResources().getConfiguration().locale.getCountry();
        locationManager = (LocationManager) act
                .getSystemService(Context.LOCATION_SERVICE);
        mTelNet = (TelephonyManager) act
                .getSystemService(Context.TELEPHONY_SERVICE);
        resolver = act.getContentResolver();
        WifiInfo mWifiInfo = wifi.getConnectionInfo();
        lastStrength = mWifiInfo.getRssi();// 获取wifi信号强度 int linkSpeed =
        // mWifiInfo.getLinkSpeed()

    }

    /**
     * 获取手机各种信息 反欺诈上送(每次发送请求都要上从最新的信息)
     * 调用该方法之前先调用initActFirst(Activityact)，对应用的设备信息类初始化
     */
    public static String getDevicePrint(Activity activity) {
        initActFirst(activity);
        StringBuffer sb = new StringBuffer();
        try {
            getPhoneDateCid();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            // IMEI(国际移动设备身份码)
            sb.append("hardwareId=").append(
                    tm.getDeviceId() == null ? "" : tm.getDeviceId()).append(
                    ";");

            // WIFI MAC
            WifiInfo info = wifi.getConnectionInfo();
            sb.append("wiFiMacAddress=").append(
                    info.getMacAddress() == null ? "" : info.getMacAddress())
                    .append(";");
            // 设备型号 Device Model
            sb.append("deviceModel=").append(
                    android.os.Build.MANUFACTURER == null ? ""
                            : android.os.Build.MANUFACTURER).append(";");

            // 设备序列号 Device SN
            sb.append("osId=").append(
                    tm.getDeviceId() == null ? "" : tm.getDeviceId()).append(
                    ";");
            // 设备主要语言
            sb.append("Languages=").append(loc == null ? "" : loc).append(";");

            // IMSI(国际移动用户识别码)
            sb.append("simId=").append(
                    tm.getSimSerialNumber() == null ? "" : tm
                            .getSimSerialNumber()).append(";");
            // 手机分辨率 screenSize

            String stringFenBian = displayMetrics.widthPixels + "*"
                    + displayMetrics.heightPixels;

            sb.append("screenSize=").append(stringFenBian).append(";");
            // // 手机IP地址
            // sb.append("IP:").append(
            // getLocalIpAddress() == null ? "" : getLocalIpAddress()).append(
            // ";");
            // 支持多任务
            sb.append("deviceMultiTaskingSupported=").append("true")
                    .append(";");
            // 手机操作系统
            sb.append("deviceSystemName=").append("Android").append(";");
            // 内核版本号 OS Version
            sb.append("deviceSystemVersion=").append(
                    android.os.Build.VERSION.RELEASE == null ? ""
                            : android.os.Build.VERSION.RELEASE).append(";");
            // 获取基站cid
            sb.append("cellTowerID=").append(mphoneDateCid).append(";");
            // 手机运营商代码
            String numeric = tm.getSimOperator();
            try {
                String mnc = numeric.substring(3, numeric.length());
                sb.append("mnc=").append(mnc == null ? "" : mnc).append(";");
            } catch (Exception e2) {
                // TODO: handle exception
                sb.append("mnc=").append("").append(";");
            }
            // 手机国家码
            sb.append("mcc=").append(
                    tm.getNetworkCountryIso() == null ? "" : tm
                            .getNetworkCountryIso()).append(";");
            // 通信录个数
            sb.append("numberOfAddressBookEntries=").append(mContractCount)
                    .append(";");
            // 手机号
            // 获取邻近的小区号
            List<NeighboringCellInfo> infos = tm.getNeighboringCellInfo();
            String cid = "";
            if (infos != null && infos.size() > 0) {
                cid = "" + infos.get(0).getCid();
            }
            sb.append("locationAreaCode=").append(cid).append(";");
            try {

                // 经纬度
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setAltitudeRequired(false);
                criteria.setBearingRequired(false);
                criteria.setCostAllowed(false);
                criteria.setPowerRequirement(Criteria.POWER_LOW);
                String provider = locationManager.getBestProvider(criteria,
                        true);
                if (provider != null) {
                    Location location = locationManager
                            .getLastKnownLocation(provider);
                    if (location != null) {
                        double lat = location.getLatitude();
                        double lng = location.getLongitude();
                        double mAccuracy = location.getAccuracy();
                        double haiba = location.getAltitude();

                        // 经度
                        sb.append("longitude=").append("" + lng).append(";");
                        // 纬度
                        sb.append("latitude=").append("" + lat).append(";");
                        // 横向精度
                        sb.append("horizontalAccuracy=").append("" + mAccuracy)
                                .append(";");
                        // 海拔
                        sb.append("Altitude=").append("" + haiba).append(";");

                        // 时间戳 改： 使用如下方法 要求得到13位
                        sb.append("timestamp=").append(
                                "" + System.currentTimeMillis()).append(";");

                    } else {
                        // 经度
                        sb.append("longitude=").append("").append(";");
                        // 纬度
                        sb.append("latitude=").append("").append(";");
                        // 横向精度
                        sb.append("horizontalAccuracy=").append("").append(";");
                        // 海拔
                        sb.append("Altitude=").append("").append(";");

                        // 时间戳 改： 使用如下方法 要求得到13位
                        sb.append("timestamp=").append(
                                "" + System.currentTimeMillis()).append(";");
                    }
                } else {
                    // 经度
                    sb.append("longitude=").append("").append(";");
                    // 纬度
                    sb.append("latitude=").append("").append(";");
                    // 横向精度
                    sb.append("horizontalAccuracy=").append("").append(";");
                    // 海拔
                    sb.append("Altitude=").append("").append(";");

                    // 时间戳 改： 使用如下方法 要求得到13位
                    sb.append("timestamp=").append(
                            "" + System.currentTimeMillis()).append(";");
                }
            } catch (Exception e) {
                // TODO: handle exception
                // 经度
                sb.append("longitude=").append("").append(";");
                // 纬度
                sb.append("latitude=").append("").append(";");
                // 横向精度
                sb.append("horizontalAccuracy=").append("").append(";");
                // 海拔
                sb.append("Altitude=").append("").append(";");

                // 时间戳 改： 使用如下方法 要求得到13位
                sb.append("timestamp=").append("" + System.currentTimeMillis())
                        .append(";");
            }
            // 信号强度
            sb.append("SignalStrength=").append("" + lastStrength).append(";");

            try {
                WifiInfo wifiInfo = wifi.getConnectionInfo();
                // ssid
                sb.append("SSID=").append(
                        wifiInfo.getSSID() == null ? "" : wifiInfo.getSSID())
                        .append(";");
                // bbsid
                sb.append("BBSID=").append(
                        wifiInfo.getBSSID() == null ? "" : wifiInfo.getBSSID())
                        .append(";");

            } catch (Exception e) {
                // TODO: handle exception
                // ssid
                sb.append("SSID=").append("").append(";");
                // bbsid
                sb.append("BBSID=").append("").append(";");

            }
            // channel
            sb.append("Channel=").append("");

            LogUtils.d("info", "设备指纹--- " + sb.toString());
            return URLEncoder.encode(sb.toString(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取基站cid
     */
    public static void getPhoneDateCid() {
        try {
            GsmCellLocation location = (GsmCellLocation) mTelNet
                    .getCellLocation();
            mphoneDateCid = String.valueOf(location.getCid());
        } catch (Exception e) {
            mphoneDateCid = "";
            e.printStackTrace();
        }

    }

    /**
     * 获取通信录个数
     */
    public static void getPhoneContracts() {
        try {

            // 获取手机联系人
            @SuppressWarnings("deprecation")
            Cursor phoneCursor = resolver.query(People.CONTENT_URI, null, null,
                    null, null); // 传入正确的uri
            mContractCount = phoneCursor.getCount();
            phoneCursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            mContractCount = 0;
        }
    }

  /**
   * 6.0 之上获取到的mac地址
   */
  private static String MAC_DEFAULT = "02:00:00:00:00:00";

  /**
   * 获取WiFi mac
   * @return
   */
  public static String getMac(){
        String wifiMac = getWifiMac(ApplicationContext.getAppContext());
        if(wifiMac == null || wifiMac.length()==0 || MAC_DEFAULT.equalsIgnoreCase(wifiMac)){
            return getMac2();
        }
        return wifiMac;
    }

    private static String getMac2(){
        try {
            ArrayList<NetworkInterface> list =
                Collections.list(NetworkInterface.getNetworkInterfaces());
            for(NetworkInterface nif:list){
                if(!"wlan0".equalsIgnoreCase(nif.getName()))continue;
                byte[] macBytes = nif.getHardwareAddress();
                if(macBytes == null){
                    return MAC_DEFAULT;
                }

                StringBuilder sb =new StringBuilder();
                for(byte b:macBytes){
                    sb.append(String.format("%02X:",b));
                }
                if(sb.length()>0){
                    sb.deleteCharAt(sb.length()-1);
                }
                return  sb.toString().toLowerCase();
            }

        }catch (Exception e){
        }

        return MAC_DEFAULT;

    }

    /**
     * 获取mac地址
     *
     * @param context
     * @param
     * @return
     */

    private static String getWifiMacAddress(Context context){
//		String tmp;

        WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        // TODO 必须要先有android.permission.ACCESS_WIFI_STATE权限才可以操作WifiManager类，否则报java.lang.SecurityException
//	    if(wifi.isWifiEnabled() == false){
//	    	wifi.setWifiEnabled(true); // 需要权限android.permission.CHANGE_WIFI_STATE
//	    }

        WifiInfo info = wifi.getConnectionInfo(); // android.permission.ACCESS_WIFI_STATE
        String mac = info.getMacAddress();
//		if(mac == null){
//			mac = "No Wifi Device";
//			return null;
//		}
//		tmp = "MAC：" + mac + "\n";
//		tmp += "IP：" + info.getIpAddress() + "\n";
//		tmp += "SSID：" +  info.getSSID() + "\n";
//		List<WifiConfiguration> l =  wifi.getConfiguredNetworks();
        return mac;
    }
    public static String getWifiMac(Context context){
        String mac = getWifiMacAddress(context);
        if(mac != null)
            return mac;
        WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        // TODO 必须要先有android.permission.ACCESS_WIFI_STATE权限才可以操作WifiManager类，否则报java.lang.SecurityException
        if(wifi.isWifiEnabled() == false){
            wifi.setWifiEnabled(true); // 需要权限android.permission.CHANGE_WIFI_STATE
            mac = getWifiMacAddress(context);
            wifi.setWifiEnabled(false);
        }

        return mac;
    }

    public static String getIMEI(Context context) {
        TelephonyManager telManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telManager.getDeviceId(); // 手机上的IMEI号
        if (deviceId == null || "".equals(deviceId)) {
            return "";
        } else {
            return deviceId;
        }
    }


    /**
     * 获取加密后的网银用户号(存在本地)
     *
     * @param context
     * @param type  1 imei ,2 mac
     * @return
     */


    public static String getLocalCAOperatorId(Context context,String operatorId,int type) {

		DeviceSecCryptor instance = DeviceSecCryptor.getInstatnce();
        try {
            if(type==1){
				SecResult result=instance.getEncryptedInfo(getIMEI(context)+operatorId);
                return result.getEncryptedInfo();
            }else if(type==2){
				SecResult result=instance.getEncryptedInfo(getWifiMac(context)+operatorId);
                return result.getEncryptedInfo();
            }

        } catch (CodeException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = (long)stat.getBlockSize();
        long availableBlocks = (long)stat.getAvailableBlocks();
        long totalBlocks = (long)stat.getBlockCount();
        boolean isInteger = false;
        return formatFileSize(totalBlocks * blockSize - availableBlocks * blockSize, isInteger);
    }
    private static DecimalFormat fileIntegerFormat = new DecimalFormat("#0");
    private static DecimalFormat fileDecimalFormat = new DecimalFormat("#0.#");

    public static String formatFileSize(long size, boolean isInteger) {
        DecimalFormat df = isInteger?fileIntegerFormat:fileDecimalFormat;
        String fileSizeString = "0M";
        if(size < 1024L && size > 0L) {
            fileSizeString = df.format((double)size) + "B";
        } else if(size < 1048576L) {
            fileSizeString = df.format((double)size / 1024.0D) + "K";
        } else if(size < 1073741824L) {
            fileSizeString = df.format((double)size / 1048576.0D) + "M";
        } else {
            fileSizeString = df.format((double)size / 1.073741824E9D) + "G";
        }

        return fileSizeString;
    }
}
