package com.boc.bocsoft.mobile.bocmobile.yun.other;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DeviceInfoUtils;
import com.boc.bocsoft.mobile.bocyun.common.tools.YunTools;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Created by dingeryue on 2016年10月28.
 */

public class YunUtils {


  public static String getAppVersion(){
    return ApplicationContext.getInstance().getVersion();
  }

  /**
   * 总运存大小
   * @return
   */
  public static String getRam(){
    return getTotalMemory(ApplicationContext.getInstance());
  }


  /**
   * 获取分辨率
   * @return
   */
  public static String getResolution(){
    DisplayMetrics metrics =  ApplicationContext.getAppContext().getResources().getDisplayMetrics();
    return  metrics.widthPixels+"*"+metrics.heightPixels;
  }

  public static String getFullFormatDate(Calendar calender){
    return  YunTools.getFullFormatTime(calender);
  }


  public static String[]  getRomMemory2(){
    String[] romInfo = new String[2];

    String path = Environment.getExternalStorageDirectory().getPath();
    StatFs statFs = new StatFs(path);

    long blockSize = statFs.getBlockSize();
    long totalBlock = statFs.getBlockCount();

    long useed = totalBlock - statFs.getAvailableBlocks();

    romInfo[0] = format.format(totalBlock*blockSize /1024.f/1024/1024);
    romInfo[1] = format.format(useed * blockSize/1024.f/1024/1024);

    return romInfo;
  }

  private final static DecimalFormat format = new DecimalFormat("##0.00");

/*  private static long getAvailMemory(Context context){
    // 获取Android当前可用内存大小
    ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
    am.getMemoryInfo(mi);
    //mi.availMem; 当前系统的可用内存
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
      float v = mi.totalMem / 1024.f / 1024;
    }
    return mi.availMem/(1024*1024);
  }*/

  //获取总运存大小

  private static String getTotalMemory(Context context){
    String str1 = "/proc/meminfo";// 系统内存信息文件
    String str2;
    String[] arrayOfString;
    long initial_memory = 0;
    try
    {
      FileReader localFileReader = new FileReader(str1);
      BufferedReader localBufferedReader = new BufferedReader(
          localFileReader, 8192);
      str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

      arrayOfString = str2.split("\\s+");

      initial_memory = Integer.valueOf(arrayOfString[1]).longValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
      localBufferedReader.close();

    } catch (IOException e) {
    }
    return format.format(initial_memory/(1024.f*1024*1024));
  }


  public static String getDeivceId(){

    ApplicationContext context = ApplicationContext.getInstance();
    try {
      String deviceId = DeviceInfoUtils.getIMEI(context);

      if(deviceId != null && deviceId.length()>0)return deviceId;
    }catch (Exception e){}

    String wifiMac = DeviceInfoUtils.getMac();

    if(wifiMac != null && wifiMac.length()>0 && !"02:00:00:00:00:00".equalsIgnoreCase(wifiMac))return wifiMac;

    String serialNumber = Build.SERIAL;
    if(serialNumber!=null && serialNumber.length()>0)return serialNumber;

    return "000000000";
  }


}
