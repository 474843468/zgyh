package com.chinamworld.bocmbci.utils;

import com.chinamworld.bocmbci.constant.DictionaryData;
import com.chinamworld.bocmbci.constant.LocalData;

import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

/**
 * Created by Administrator on 2016/10/31.
 */
public  class  ListUtils {

    public interface ISynchronousList<T,E>{
        boolean doSomeThing(T t,E e);
    }

    /**
     * 用于同步两个不同的List集合的数据
     * @param source  主数据源 .数据刷新以后,最新的数据都存于此列表中
     * @param newList 从数据,主要用于更新主数据源中的数据
     * @param compareFunc 匹配方法.如果匹配上,则返回true,否则返回false..在此方法中,还需要处理数据刷新赋值逻辑
     * @param <T>
     * @param <E>
     */
    public static <T,E>  void  synchronousList(List<T> source, List<E> newList, ISynchronousList<T,E> compareFunc){
        if(source == null || newList == null || compareFunc == null)
            return;
        for(T itemT :source){
            for(E itemE : newList){
                if(compareFunc.doSomeThing(itemT,itemE)){
                    break;
                }
            }

        }

    }


    /**
     * 用于同步两个拥有相同的数据结构，不同的List集合的数据。数据以最新的数据为准，最终数据与最新数据一致
     * @param source  主数据源 .数据刷新以后,最新的数据都存于此列表中.
     * @param newList 从数据,主要用于更新主数据源中的数据
     * @param compareFunc 匹配方法.如果匹配上,则返回true,否则返回false..在此方法中,还需要处理数据刷新赋值逻辑
     * @param <T>
     */
    public static <T>  void  synchronousListWithSameStruct(List<T> source, List<T> newList, ISynchronousList<T,T> compareFunc){
        if(newList == null)
            return;
        if(source == null || newList == null || compareFunc == null)
            return;

        boolean isContain = false;
        for(T itemE : newList){
            isContain = false;
            for(T itemT :source){
                if(compareFunc.doSomeThing(itemT,itemE)){
                    isContain = true;
                    break;
                }
            }
            if(isContain == false){  // 数据未包含于源数据中
                source.add(itemE);
            }
        }
        for(int i = 0; i < source.size();i++){
            isContain = false;
            for(T itemE : newList){
                if(compareFunc.doSomeThing(source.get(i),itemE)){
                    isContain = true;
                    break;
                }
            }
            if(isContain == false){  // 数据未包含于源数据中
                source.remove(i);
                i--;
            }
        }

    }


    /**
     * 双向宝，贵金属，外汇，按照指定的顺序排序
     * @param list
     * @param compare
     * @param <T>
     */
    public static <T> void sortForInvest(List<T> list,ISynchronousList<T,KeyAndValueItem> compare){
        if(compare == null || list == null)
            return;
        List<KeyAndValueItem> currencyList = DictionaryData.getInvestCurrencyCodeList();
        List<T> l = new ArrayList<T>();
        for(T t : list){
            l.add(t);
        }
        list.clear();
        for(KeyAndValueItem  item : currencyList){
            for(T t : l){
                if(compare.doSomeThing(t,item)){
                    list.add(t);
                    break;
                }
            }
        }
    }



}
