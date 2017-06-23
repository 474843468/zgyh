package com.boc.bocsoft.mobile.bocyun.common.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by dingeryue on 2016年10月17.
 */

public class YunTools {

  private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
  private static SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");

  private static SimpleDateFormat fullFormat = new SimpleDateFormat("yyyyMMddHHmmss");


  public static String getFormatDate(Calendar instance){
    return dateFormat.format(instance.getTime());

  }

  public static String getFormatTime(Calendar instance){
    return timeFormat.format(instance.getTime());
  }

  public static String getFullFormatTime(Calendar instance){
    return fullFormat.format(instance.getTime());
  }

  private static SimpleDateFormat fullallFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");

  public static String getTransId(Calendar calendar){
    String random = String.format("%06d",(int) (Math.random() * 1000000));
    return fullallFormat.format(calendar.getTime())+random;
  }
}
