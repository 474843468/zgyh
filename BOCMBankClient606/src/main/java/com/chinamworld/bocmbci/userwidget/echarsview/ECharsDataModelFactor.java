package com.chinamworld.bocmbci.userwidget.echarsview;

import java.util.List;

/**
 * Created by Administrator on 2016/11/1.
 */
public class ECharsDataModelFactor {



    ECharsDataModelFactor(){}
    private static ECharsDataModelFactor instance;
    public static ECharsDataModelFactor getInstance(){
        if(instance == null)
         instance = new ECharsDataModelFactor();
        return instance;
    }

    /**
     * 创建折线图
     * @param xList ： 水平左边值
     * @param yList ： Y轴坐标值
     * @param type ： 折线图类型
     * @return
     */
    public ECharsDataModel createECharsDataModel(ECharsType type,IECharsData... data){
        ECharsDataModel model = new ECharsDataModel(data);
        if(type == ECharsType.ZheXian){
            model.initZheXian();
        }
        else if(type == ECharsType.ZheXianDian){
            model.initZheXianDian();
        }
        else if(type == ECharsType.MutilLine){
            model.initMutilLine();
        }

        return model;
    }
}
