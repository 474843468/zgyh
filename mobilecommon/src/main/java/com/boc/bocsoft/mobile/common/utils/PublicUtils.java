package com.boc.bocsoft.mobile.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 公用工具类
 * Created by XieDu on 2016/3/2.
 */
public class PublicUtils {

    /**
     * 判断列表是否为空
     *
     * @param list 待判断的列表
     * @return 是否为空
     */
    public static boolean isEmpty(List list) {
        return null == list || list.isEmpty();
    }

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 检测是否为空
     *
     * @param value 待检测的对象
     * @param message 错误信息
     * @param <T> 待检测的对象类型
     * @return 被检测完毕的对象
     */
    public static <T> T checkNotNull(T value, String message) {
        if (value == null) {
            throw new NullPointerException(message);
        }
        return value;
    }

    public static <T> ArrayList<T> convertToArrayList(List<T> list) {
        ArrayList<T> result;
        if (isEmpty(list)) {
            return null;
        }
        if (list instanceof ArrayList) {
            result = (ArrayList<T>) list;
        } else {
            result = new ArrayList<>(list.size());
            for (T t : list) {
                result.add(t);
            }
        }
        return result;
    }
}
