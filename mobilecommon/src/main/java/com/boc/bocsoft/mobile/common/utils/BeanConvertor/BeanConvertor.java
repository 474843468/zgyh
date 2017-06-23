package com.boc.bocsoft.mobile.common.utils.BeanConvertor;

import com.boc.bocsoft.mobile.common.utils.BeanConvertor.anno.ConvertIfDiff;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.anno.ListItemType;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.anno.NotConvert;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

/**
 * Created by lxw
 * date 15/11/12
 */
public class BeanConvertor extends Convertor {

    public static <R, D> D fromBean(R sourceBean, D destination) {

        return converterBean(sourceBean, destination, CopyType.from);
    }

    public static <R, D> D toBean(R sourceBean, D destination) {
        return converterBean(sourceBean, destination, CopyType.to);
    }

    private static <R, D> D converterBean(R sourceBean, D destination, CopyType copyType) {
        if (sourceBean == null || destination == null) {
            return null;
        }
        Field[] fieldList = sourceBean.getClass().getDeclaredFields();
        for (Field field : fieldList) {
            if (!Modifier.isStatic(field.getModifiers())) {
                parseField(field, sourceBean, destination, copyType);
            }
        }
        return destination;
    }

    private static <R, D> void parseField(Field field, R sourceBean, D destination,
            CopyType copyType) {
        if (!field.isAnnotationPresent(NotConvert.class)) {
            String fieldName = field.getName();
            String methodName =
                    "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            try {
                setData(methodName, fieldName, field, sourceBean, destination, copyType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static <R, D> void setData(String methodName, String fieldName, Field field,
            R sourceBean, D destination, CopyType copyType) throws Exception {
        //内部类的field有一个是指向外部类的引用，需要过滤掉
        if ("this$0".equals(fieldName)) {
            return;
        }
        Method method = sourceBean.getClass().getMethod(methodName, new Class<?>[] {});
        Object sourceFileObj = method.invoke(sourceBean, new Object[] {});
        Class returnType = method.getReturnType();
        if (sourceFileObj != null) {
            Field targetField = destination.getClass().getDeclaredField(fieldName);
            targetField.setAccessible(true);
            Class targetType = targetField.getType();
            if (sourceFileObj instanceof List) {
                // edit by lxw
                List sourceList = (List) sourceFileObj;
                ListItemType itemType;
                if (copyType == CopyType.to) {
                    itemType = targetField.getAnnotation(ListItemType.class);
                } else {
                    itemType = field.getAnnotation(ListItemType.class);
                }

                if (itemType == null) {
                    return;
                }
                Class itemClazz = itemType.instantiate();

                /**
                 * list 为string时
                 */
                if (itemClazz == String.class || itemClazz == LocalDate.class
                        || itemClazz == LocalDateTime.class || itemClazz == LocalTime.class) {
                    targetField.set(destination, sourceFileObj);
                    return;
                }

                List result = new ArrayList();
                for (int i = 0; i < sourceList.size(); i++) {
                    Object item = itemClazz.newInstance();
                    converterBean(sourceList.get(i), item, copyType);
                    result.add(item);
                }
                targetField.set(destination, result);
            } else if (targetType == returnType) {
                targetField.set(destination, sourceFileObj);
            } else {
                boolean convertIfDiff =
                        (copyType == CopyType.to ? targetField : field).isAnnotationPresent(
                                ConvertIfDiff.class);
                if (convertIfDiff) {
                    Object targetObject = targetType.newInstance();
                    converterBean(sourceFileObj, targetObject, copyType);
                    targetField.set(destination, targetObject);
                } else {
                    targetField.set(destination, sourceFileObj.toString());
                }
            }
        }
    }

    /**
     * Safe version of <code>isInstance</code>, returns <code>false</code> if
     * any of the arguments is <code>null</code>.
     */
    public static boolean isInstanceOf(Object object, Class target) {
        if (object == null || target == null) {
            return false;
        }
        return target.isInstance(object);
    }

    /**
     * Invokes any method of a class, even private ones.
     *
     * @param c class to examine
     * @param obj object to inspect
     * @param method method to invoke
     * @param paramClasses parameter types
     * @param params parameters
     */
    public static Object invokeDeclared(Class c, Object obj, String method, Class[] paramClasses,
            Object[] params)
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Method m = c.getDeclaredMethod(method, paramClasses);
        m.setAccessible(true);
        return m.invoke(obj, params);
    }

    /**
     * 主要用于l列表元素的转换或者两个类型不同但数据相同的类的转换过程，在此过程中，copyType表示以哪个类的标记为准
     */
    private enum CopyType {
        from,
        to
    }
}
