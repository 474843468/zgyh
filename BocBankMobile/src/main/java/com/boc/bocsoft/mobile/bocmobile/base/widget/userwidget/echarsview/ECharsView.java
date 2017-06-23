package com.boc.bocsoft.mobile.bocmobile.base.widget.userwidget.echarsview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import java.util.List;

/**
 * Created by Administrator on 2016/10/25.
 */
public class ECharsView extends WebView {
  public ECharsView(Context context) {
    super(context);
    initView();
  }

  public ECharsView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initView();
  }

  public ECharsView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initView();
  }
//趋势图，折线下有阴影
  ECharsType mECharsType = ECharsType.QuShi;

  public void setECharsType(ECharsType type) {
    mECharsType = type;
  }

  private void initView() {
    // 设置字符集编码
    getSettings().setDefaultTextEncodingName("UTF-8");
    // 开启JavaScript支持
    getSettings().setJavaScriptEnabled(true);
  }

  private IECharsData[] mData;

  public void setData(IECharsData... data) {
    mData = data;
    // 加载assets目录下的文件
    loadUrl("file:///android_asset/EChart/index.html");
    setWebChromeClient(new WebChromeClient() {
      @Override public void onProgressChanged(WebView view, int newProgress) {
        String result = "";
        if (mECharsType == ECharsType.MutilLine) {
          result = initThreeData(mData);
        } else if (mECharsType == ECharsType.MutilTitleLine) {
          result = initMutilTitleLineData(mData);
        } else if (mECharsType == ECharsType.QuShi) {
          result = initQuShiData(mData);
        } else if (mECharsType == ECharsType.ZheXian) {
          result = initZheXianData(mData);
        } else if (mECharsType == ECharsType.ZheXianDian) {
          result = initZheXianDianData(mData);
        }
        //                else {
        //                    ECharsDataModel eCharsDataModel;
        //                    if(mData == null){
        //                        eCharsDataModel =  ECharsDataModelFactor.getInstance().createECharsDataModel(mECharsType,mData);
        //                    }
        //                    else {
        //                        eCharsDataModel  =  ECharsDataModelFactor.getInstance().createECharsDataModel(mECharsType,mData);
        //                    }
        //                    result = GsonTools.toJson(eCharsDataModel);
        //                }

        // 页面加载完成后再调用JS方法
        if (newProgress == 100) {
          loadUrl("javascript:draw('" + result + "','" + "default" + "')");
        }
        super.onProgressChanged(view, newProgress);
      }
    });
  }

  private String initThreeData(IECharsData... data) {
    if (data == null || data.length < 3) return "";
    getMaxAndMin(data);
    String s =
        "{\"tooltip\":{\"trigger\":\"axis\",\"axisPointer\":{\"type\":\"line\",\"lineStyle\" : {\"color\": \"rgba(202,202,202,1)\",\"width\": 1,\"type\": \"solid\"}},"
            + "\"formatter\":\"{b}</br>{a}:{c}%%</br>{a1}:{c1}%%</br>{a2}:{c2}%%\",\"backgroundColor\":\"rgba(175,198,248,0.9)\",\"textStyle\":{\"color\":\"rgba(255,255,255,1)\",\"fontSize\":9}},\"calculable\":false,"
            + "\"grid\":{\"borderWidth\":0,\"x\":40,\"y\":10,\"x2\":40,\"y2\":20},"
            + "\"xAxis\":[{\"type\":\"category\",\"boundaryGap\":false,\"splitLine\":{\"show\":false},\"axisTick\":{\"show\":false},\"axisLine\":{\"lineStyle\": {\"color\":\"rgba(165,161,161,1)\",\"width\":0.5}},"
            + "\"axisLabel\": {\"formatter\":\"{value}\",\"textStyle\":{\"color\":\"rgba(165,161,161,0.9)\",\"fontSize\":9}},\"axisTick\":{\"show\":false},\"data\":%s}],\"yAxis\":[{\"max\":%d,\"min\":%d,\"type\":\"value\", \"splitNumber\":4,\"splitLine\":{\"show\":false},"
            + "\"axisTick\":{\"show\":false},\"axisLine\":{\"lineStyle\": {\"color\":\"rgba(165,161,161,1)\",\"width\":0.5}},\"axisLabel\": {\"formatter\":\"{value}%%\",\"textStyle\":{\"color\":\"rgba(207,207,207,0.9)\",\"fontSize\":9}}}],"
            + "\"series\":[{\"name\":\"累计收益率\",\"itemStyle\": {\"normal\": {\"color\":\"rgba(229,105,254,1)\"}},\"type\":\"line\",\"data\":%s,\"symbol\":\"none\"},"
            + "{\"name\":\"业绩比较基准收益率\",\"itemStyle\": {\"normal\": {\"color\":\"rgba(94,195,247,1)\"}},\"type\":\"line\",\"data\":%s,\"symbol\":\"none\"},"
            + "{\"name\":\"%s\",\"itemStyle\": {\"normal\": {\"color\":\"rgba(255,200,52,1)\"}},\"type\":\"line\",\"data\":%s,\"symbol\":\"none\"}]}";

    String result = String.format(s, GsonTools.toJson(data[0].getXList()), mMax, mMin,
        GsonTools.toJson(data[0].getYList()), GsonTools.toJson(data[1].getYList()),
        data[2].getTitle(), GsonTools.toJson(data[2].getYList()));

    return result;
  }

  private String initQuShiData(IECharsData... data) {
    if (data == null || data.length < 1) return "";
    getMaxAndMin(data);
    String s =
        "{\"tooltip\":{\"trigger\":\"axis\",\"axisPointer\":{\"type\":\"line\",\"lineStyle\" : {\"color\": \"rgba(202,202,202,1)\",\"width\": 1,\"type\": \"solid\"}},"
            + "\"formatter\":\"{b}</br>{a}:{c}\", \"backgroundColor\":\"rgba(175,198,248,0.9)\",\"textStyle\":{\"color\":\"rgba(255,255,255,1)\",\"fontSize\":9}},\"calculable\":false,"
            + "\"grid\":{\"borderWidth\":0,\"x\":40,\"y\":10,\"x2\":40,\"y2\":20},"
            + "\"xAxis\":[{\"type\":\"category\",\"boundaryGap\":false,\"splitLine\":{\"show\":false},\"axisTick\":{\"show\":false},\"axisLine\":{\"lineStyle\": {\"color\":\"rgba(202,202,202,1)\",\"width\":0.5}},"
            + "\"axisLabel\": {\"formatter\":\"{value}\",\"textStyle\":{\"color\":\"rgba(165,161,161,0.9)\",\"fontSize\":9}},\"axisTick\":{\"show\":false},\"data\":%s}],\"yAxis\":[{\"type\":\"value\",\"splitNumber\":4,\"splitLine\":{\"show\":false},"
            + "\"axisTick\":{\"show\":false},\"axisLine\":{\"lineStyle\": {\"color\":\"rgba(202,202,202,1)\",\"width\":0.5}},\"axisLabel\": {\"formatter\":\"{value}\",\"textStyle\":{\"color\":\"rgba(165,161,161,0.9)\",\"fontSize\":9}}}],"
            + "\"series\":[{\"name\":\"七日年化收益率\",\"type\":\"line\",\"symbol\":\"none\",\"itemStyle\": {\"normal\":{\"lineStyle\":{\"color\":\"rgba(154,181,238,1)\"},\"areaStyle\":{\"type\":\"default\",\"color\":\"rgba(201,220,249,1)\"}}},\"data\":%s}]}";

    String result = String.format(s, GsonTools.toJson(data[0].getXList()),
        GsonTools.toJson(data[0].getYList()));

    return result;
  }

  private String initZheXianData(IECharsData... data) {
    if (data == null || data.length < 1) return "";
    getMaxAndMin(data);
    String s =
        "{\"tooltip\":{\"trigger\":\"axis\",\"axisPointer\":{\"type\":\"line\",\"lineStyle\" : {\"color\": \"rgba(202,202,202,1)\",\"width\": 1,\"type\": \"solid\"}},"
            + "\"formatter\":\"{b}</br>{a}:{c}\", \"backgroundColor\":\"rgba(175,198,248,0.9)\",\"textStyle\":{\"color\":\"rgba(255,255,255,1)\",\"fontSize\":9}},\"calculable\":false,"
            + "\"grid\":{\"borderWidth\":0,\"x\":40,\"y\":10,\"x2\":40,\"y2\":20},"
            + "\"xAxis\":[{\"type\":\"category\",\"boundaryGap\":false,\"splitLine\":{\"show\":false},\"axisTick\":{\"show\":false},\"axisLine\":{\"lineStyle\": {\"color\":\"rgba(202,202,202,1)\",\"width\":0.5}},"
            + "\"axisLabel\": {\"formatter\":\"{value}\",\"textStyle\":{\"color\":\"rgba(165,161,161,0.9)\",\"fontSize\":9}},\"axisTick\":{\"show\":false},\"data\":%s}],\"yAxis\":[{\"type\":\"value\",\"splitNumber\":4,\"splitLine\":{\"show\":false},"
            + "\"axisTick\":{\"show\":false},\"axisLine\":{\"lineStyle\": {\"color\":\"rgba(202,202,202,1)\",\"width\":0.5}},\"axisLabel\": {\"formatter\":\"{value}\",\"textStyle\":{\"color\":\"rgba(165,161,161,0.9)\",\"fontSize\":9}}}],"
            + "\"series\":[{\"name\":\"七日年化收益率\",\"type\":\"line\",\"symbol\":\"none\",\"itemStyle\": {\"normal\":{\"lineStyle\":{\"color\":\"rgba(154,181,238,1)\"},\"areaStyle\":{\"type\":\"default\",\"color\":\"rgba(255,255,255,1)\"}}},\"data\":%s}]}";

    String result = String.format(s, GsonTools.toJson(data[0].getXList()),
        GsonTools.toJson(data[0].getYList()));

    return result;
  }

  private String initZheXianDianData(IECharsData... data) {
    if (data == null || data.length < 1) return "";
    getMaxAndMin(data);
    String s =
        "{\"tooltip\":{\"trigger\":\"axis\",\"axisPointer\":{\"type\":\"line\",\"lineStyle\" : {\"color\": \"rgba(202,202,202,1)\",\"width\": 1,\"type\": \"solid\"}},"
            + "\"formatter\":\"{b}</br>{a}:{c}\", \"backgroundColor\":\"rgba(175,198,248,0.9)\",\"textStyle\":{\"color\":\"rgba(255,255,255,1)\",\"fontSize\":9}},\"calculable\":false,"
            + "\"grid\":{\"borderWidth\":0,\"x\":40,\"y\":10,\"x2\":40,\"y2\":20},"
            + "\"xAxis\":[{\"type\":\"category\",\"boundaryGap\":false,\"splitLine\":{\"show\":false},\"axisTick\":{\"show\":false},\"axisLine\":{\"lineStyle\": {\"color\":\"rgba(202,202,202,1)\",\"width\":0.5}},"
            + "\"axisLabel\": {\"formatter\":\"{value}\",\"textStyle\":{\"color\":\"rgba(165,161,161,0.9)\",\"fontSize\":9}},\"axisTick\":{\"show\":false},\"data\":%s}],\"yAxis\":[{\"type\":\"value\",\"splitNumber\":4,\"splitLine\":{\"show\":false},"
            + "\"axisTick\":{\"show\":false},\"axisLine\":{\"lineStyle\": {\"color\":\"rgba(202,202,202,1)\",\"width\":0.5}},\"axisLabel\": {\"formatter\":\"{value}\",\"textStyle\":{\"color\":\"rgba(165,161,161,0.9)\",\"fontSize\":9}}}],"
            + "\"series\":[{\"name\":\"七日年化收益率\",\"type\":\"line\",\"symbol\":\"circle\",\"symbolSize\":1,\"itemStyle\": {\"normal\":{\"lineStyle\":{\"color\":\"rgba(154,181,238,1)\"},\"areaStyle\":{\"type\":\"default\",\"color\":\"rgba(255,255,255,1)\"}}},\"data\":%s}]}";

    String result = String.format(s, GsonTools.toJson(data[0].getXList()),
        GsonTools.toJson(data[0].getYList()));

    return result;
  }

  private String initMutilTitleLineData(IECharsData... data) {
    if (data == null || data.length < 3) return "";
    getMaxAndMin(data);
    String s =
        "{\"tooltip\":{\"trigger\":\"axis\",\"axisPointer\":{\"type\":\"line\",\"lineStyle\" : {\"color\": \"rgba(202,202,202,1)\",\"width\": 1,\"type\": \"solid\"}},\"formatter\":\"{b}</br>{a}:{c}</br>{a1}:{c1}/{c2}\", \"backgroundColor\":\"rgba(175,198,248,0.9)\",\"textStyle\":{\"color\":\"rgba(255,255,255,1)\",\"fontSize\":9}},\"calculable\":false,\"grid\":{\"borderWidth\":0,\"x\":40,\"y\":10,\"x2\":40,\"y2\":20},\"xAxis\":[{\"type\":\"category\",\"boundaryGap\":false,\"splitLine\":{\"show\":false},"
            +
            "\"axisTick\":{\"show\":false},\"axisLine\":{\"lineStyle\": {\"color\":\"rgba(165,161,161,1)\",\"width\":0.5}},\"axisLabel\": {\"formatter\":\"{value}\",\"textStyle\":{\"color\":\"rgba(165,161,161,0.9)\",\"fontSize\":9}},\"axisTick\":{\"show\":false},\"data\":%s}],\"yAxis\":[{ \"max\":%d,\"min\":%d,\"type\":\"value\",\"splitNumber\":4,\"splitLine\":{\"show\":false},\"axisTick\":{\"show\":false},\"axisLine\":{\"lineStyle\": {\"color\":\"rgba(165,161,161,1)\",\"width\":0.5}},\"axisLabel\": {\"formatter\":\"{value}\","
            +
            "\"textStyle\":{\"color\":\"rgba(165,161,161,0.9)\",\"fontSize\":9}}}],\"series\":[{\"name\":\"排名分位数\",\"showAllSymbol\":true,\"type\":\"line\",\"data\":%s,\"symbol\":\"circle\",\"symbolSize\":1,\"itemStyle\": {\"normal\": {\"color\":\"rgba(255,200,52,1)\"}},\"lineStyle\": {\"normal\": {\"color\":\"rgba(250,255,255,1)\"}},\"areaStyle\": {\"normal\": {\"color\":\"rgba(255,255,250,1)\"}}},{\"name\":\"排名\",\"type\":\"line\",\"data\":%s,\"symbol\":\"none\",\"symbolSize\":1,\"itemStyle\": {\"normal\": {\"color\":\"rgba(250,255,255,0)\"}},"
            +
            "\"lineStyle\":{\"normal\": {\"color\":\"rgba(250,255,255,0)\"}},\"areaStyle\": {\"normal\": {\"color\":\"rgba(255,255,250,0)\"}}},{\"name\":\"总排名\",\"type\":\"line\",\"data\":%s,\"symbol\":\"none\",\"symbolSize\":1,\"itemStyle\": {\"normal\": {\"color\":\"rgba(250,255,255,0)\"}},\"lineStyle\": {\"normal\": {\"color\":\"rgba(250,255,255,0)\"}},\"areaStyle\": {\"normal\": {\"color\":\"rgba(255,255,250,0)\"}}}]}";

    String result = String.format(s, GsonTools.toJson(data[0].getXList()), mMax, mMin,
        GsonTools.toJson(data[0].getYList()), GsonTools.toJson(data[1].getYList()),
        GsonTools.toJson(data[2].getYList()));

    return result;
  }

  int mMax, mMin;

  private void getMaxAndMin(IECharsData... datas) {
    double max = -9999999, min = 99999999, tmp;

    for (IECharsData data : datas) {

      tmp = getMax(data.getYList());
      if (max < tmp) max = tmp;
      tmp = getMin(data.getYList());
      if (tmp < min) min = tmp;
    }
    this.mMax = ((int) max) + 1;
    this.mMin = (int) min;
  }

  private double getMax(List<String> list) {
    double max = -9999999, tmp;
    for (String str : list) {
      try {
        tmp = Double.parseDouble(str);
        if (tmp > max) max = tmp;
      } catch (Exception e) {
      }
    }
    return max;
  }

  private double getMin(List<String> list) {
    double min = 9999999, tmp;
    for (String str : list) {
      try {
        tmp = Double.parseDouble(str);
        if (tmp < min) min = tmp;
      } catch (Exception e) {

      }
    }
    return min;
  }
}
