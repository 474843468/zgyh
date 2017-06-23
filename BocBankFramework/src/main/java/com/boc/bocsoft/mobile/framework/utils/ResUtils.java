package com.boc.bocsoft.mobile.framework.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static com.boc.bocsoft.mobile.common.utils.PublicUtils.checkNotNull;

/**
 * Created by XieDu on 2016/5/19.
 */
public class ResUtils {

    /**
     * 加载资源配置
     *
     * @param context context
     * @param name 要加载的资源名（在assets里）
     */
    public static Properties loadProperties(Context context, String name) {
        InputStream ins = null;
        Properties properties = new Properties();
        try {
            ins = context.getAssets().open(name);
            properties.load(new InputStreamReader(ins, "utf-8"));
            ins.close();
        } catch (Exception exp) {
        } finally {
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException e) {
                    ins = null;
                }
            }
        }
        return properties;
    }
    /**
     * 加载保留文件排序的资源配置
     *
     * @param mContext
     * @param name
     */
    public static OrderProperties loadOrderedProperties(Context mContext,
                                                        String name) {
        InputStream ins = null;
        OrderProperties properties = new OrderProperties();
        try {
            ins = mContext.getAssets().open(name);
            properties.load(ins);
            ins.close();
        } catch (Exception exp) {
        } finally {
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException e) {
                    ins = null;
                }
            }
        }
        return properties;
    }
    /**
     * 根据文件名取出property中键或者值的集合
     *
     * @param mContext
     * @param fileName
     *            property加载的文件名
     * @param flag
     *            0键，1值
     * @return
     */
    public static List<String> getOrderedPropertyList(Context mContext,
                                                      String fileName, int flag) {
        List<String> elementList = new ArrayList<String>();
        if (StringUtils.isEmpty(fileName)) {
            return elementList;
        }
        OrderProperties properties = loadOrderedProperties(
                mContext, fileName);
        if (flag == 0) {
            return properties.getKeys();
        } else if (flag == 1) {
            Map<String, String> keyValue = properties.getKeyValue();
            for (String key : properties.getKeys()) {
                String valueString = keyValue.get(key);
                if (valueString != null) {
                    elementList.add(valueString);
                }
            }
        }
        return elementList;
    }
    public static Map<String, String> loadPropertiesToMap(Context context, String name) {

        Properties properties = loadProperties(context, name);

        Map<String, String> resultMap = new HashMap<String, String>();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            resultMap.put((String) entry.getKey(), (String) entry.getValue());
        }

        return resultMap;
    }

    /**
     * 将dp转换成px
     *
     * @param context context
     * @param dipValue dp
     * @return px
     */
    public static int dip2px(Context context, float dipValue) {
        return (int) (dipValue * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(Context context, float pxValue) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) (pxValue * dm.scaledDensity + 0.5f);
    }

    public static void setBackground(View view, Drawable drawable) {
        checkNotNull(view, "view==null");
        checkNotNull(drawable, "drawable==null");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    public static WindowManager getWindowManager(Context context) {
        return (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager(context).getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager(context).getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static Rect getAppRect(Activity activity) {
        Rect outRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        return outRect;
    }

    public static Rect getDrawingRect(Activity activity) {
        Rect outRect = new Rect();
        activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(outRect);
        return outRect;
    }

    // 隐藏系统键盘
    public static void hideSoftInputMethod(EditText editText) {
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            editText.setInputType(InputType.TYPE_NULL);
        } else {
            ((Activity) editText.getContext()).getWindow()
                                              .setSoftInputMode(
                                                      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> clazz = EditText.class;
                Method method = null;
                String[] methods = {
                        "setShowSoftInputOnFocus", "setSoftInputShowOnFocus",
                        "setSoftInputShownOnFocus"
                };
                for (String methodString : methods) {
                    try {
                        method = clazz.getMethod(methodString, boolean.class);
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (method != null) {
                    method.setAccessible(true);
                    method.invoke(editText, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void hideUnderline(EditText editText) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            editText.setBackground(null);
        } else {
            editText.setBackgroundDrawable(null);
        }
    }
}
