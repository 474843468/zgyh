package com.chinamworld.llbt.model;

/**
 * 带返回值的回调接口
 * @author Administrator
 *
 * @param <T>
 */
public interface IFunc< T> {
        T callBack(Object param);
}
