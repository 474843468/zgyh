package com.boc.bocsoft.mobile.common.utils.date;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作者：XieDu
 * 创建时间：2016/10/9 19:49
 * 描述：gson转换时，给被转换类加上该注解，统一指明该类涉及的时间格式。
 * 注意是对类进行注解，不是对字段进行注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface GsonDateFormat {

    String dateTimeFormat() default DateFormatters.DATETIME_FORMAT;
    String dateFormat() default DateFormatters.DATE_FORMAT_V2_1;
    boolean instantFormat() default true;

}
