package com.boc.bocsoft.mobile.wfss.buss.common;

/**
 * Created by gwluo on 2016/11/5.
 */

public abstract class WFSSBaseParamsModel {
    /**
     * 获取该接口的模块路径，外汇贵金属forex，基金fund，通用comm
     *
     * @return
     */
    public abstract String getModuleName();

    /**
     * 获取类对应接口的名称，通过将类的名称去掉后面的Params将首字母小写
     *
     * @return
     */
    public abstract String getMethodName();
//    {
//        String nameEnd = "Params";
//        String simpleName = getClass().getSimpleName();
//        if (!simpleName.endsWith(nameEnd) || simpleName.length() <= nameEnd.length()) {
//            throw new RuntimeException("class name format err,must be end with '" +
//                    nameEnd + "' and length > '" + nameEnd + "' length");
//        }
//        return simpleName.substring(0, 1).toLowerCase() +
//                simpleName.substring(1, simpleName.length() - nameEnd.length());
//    }

    /**
     * 模块名称和方法名称组成的接口路径
     *
     * @return
     */
    public String getPath() {
        return getModuleName() + getMethodName();
    }
}
