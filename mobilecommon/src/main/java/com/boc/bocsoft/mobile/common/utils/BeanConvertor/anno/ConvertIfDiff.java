package com.boc.bocsoft.mobile.common.utils.BeanConvertor.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作者：XieDu
 * 创建时间：2016/8/19 15:42
 * 描述：即使源类和目标类类型不同也要转换，适用于两个类类名不同但属性相同的情形
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConvertIfDiff {
}
