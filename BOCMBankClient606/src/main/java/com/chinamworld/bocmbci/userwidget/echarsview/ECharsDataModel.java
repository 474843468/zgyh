package com.chinamworld.bocmbci.userwidget.echarsview;

import java.util.ArrayList;
import java.util.List;

/**
 * EChars图形控件上送数据结构
 * Created by yuht on 2016/10/25.
 */
class ECharsDataModel {

    public ECharsDataModel(IECharsData... datas){
        if(datas == null || datas.length <= 0){
            return;
        }
        XAxisEChars xAxisEChars = new XAxisEChars();
        xAxis.add(xAxisEChars);
        xAxisEChars.data = datas[0].getXList();

        double max = -9999999 ,min = 99999999,tmp;

        for(IECharsData data :datas){
            SeriesEChars seriesEChars = new SeriesEChars();
            seriesEChars.data = data.getYList();
            if(data.getTitle() != null  && data.getTitle().length() > 0)
                seriesEChars.name = data.getTitle();
            series.add(seriesEChars);
            tmp = getMax(data.getYList());
            if(max < tmp)
                max = tmp;
            tmp = getMin(data.getYList());
            if(tmp < min)
                min = tmp;
        }

        YAxisEChars yAxisEChars = new YAxisEChars();
        yAxis.add(yAxisEChars);
        yAxisEChars.max = ((int)max) + 1;
        yAxisEChars.min = (int)min;

    }
    private double getMax(List<String> list){
        double max = -9999999,tmp;
        for(String str :list){
            try{
                tmp = Double.parseDouble(str);
                if(tmp > max)
                    max = tmp;
            }
            catch(Exception e){
            }

        }
        return max;
    }

    private double getMin(List<String> list){
        double min = 9999999,tmp;
        for(String str :list){
            try{
                tmp = Double.parseDouble(str);
                if(tmp < min)
                    min = tmp;
            }
            catch (Exception e){

            }

        }
        return min;
    }
    public void initZheXian(){
        series.get(0).itemStyle = null;
    }
    public void initZheXianDian(){
        series.get(0).itemStyle = null;
        series.get(0).symbol= null;
    }

    public void initMutilLine(){
        tooltip.formatter = "{b}</br>{a}:{c}%</br>{a1}:{c1}%</br>{a2}:{c2}%";
    }



    private boolean  calculable = false;
    private ToolTipEChars tooltip = new ToolTipEChars();
    class ToolTipEChars{
        public String trigger = "axis";
        public String formatter = null;
    }
    //    private DataZoomEChars dataZoom = new DataZoomEChars();
    class DataZoomEChars{
        public boolean show = true;
        public boolean realtime = true;
        public int start = 0;
        public int end = 100;
    }
    private GridEChars grid = new GridEChars();

    private List<XAxisEChars> xAxis = new ArrayList<XAxisEChars>();

    private List<YAxisEChars> yAxis = new ArrayList<YAxisEChars>();

    class YAxisEChars{
        private String type = "value";
//        private SplitAreaEChars splitArea = new SplitAreaEChars();
        public double max;
        public double min;
    }

    private List<SeriesEChars> series = new ArrayList<SeriesEChars>();



    class GridEChars{
        public int borderWidth = 0;
        private int x = 40;
        private int y = 5;
        private int x2 = 40;
    }

    class XAxisEChars{
        public String type = "category";
        public boolean boundaryGap = false;
        private List<String> data;

    }



    class SplitAreaEChars{
        private boolean show = true;
    }
    class SeriesEChars{
        public String name = "";
        private String type = "line";
        public List<String> data;
        //        private boolean showAllSymbol = true;
        public String symbol = "none";
        public ItemStyleEChars itemStyle = new ItemStyleEChars();

        class ItemStyleEChars{
            private NormalEChars normal = new NormalEChars();
            class NormalEChars{
                private LineStyleEChars lineStyle = new LineStyleEChars();
                class LineStyleEChars{
                    private String  color = "rgba(64, 140, 245, 1)";
                }

                private AreaStyleEChars areaStyle = new AreaStyleEChars();
                class AreaStyleEChars{
                    private String type = "default";
                    private String color = "rgba(201, 220, 249, 1)";
                }
            }
        }

    }




}
