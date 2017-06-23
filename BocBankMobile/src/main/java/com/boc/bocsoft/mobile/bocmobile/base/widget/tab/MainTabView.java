package com.boc.bocsoft.mobile.bocmobile.base.widget.tab;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.util.AttributeSet;
import android.widget.TabWidget;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;

/**
 * 主导航Tab
 * Created by ly on 2016/5/23.
 */
public class MainTabView extends FragmentTabHost {


    private Context mContext;

    public MainTabView(Context context) {
        this(context,null);
    }

    public MainTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

    }

    public void initTab(ArrayList<TabIndicatorView> tabs){

        for (TabIndicatorView tab : tabs) {
            TabSpec  spec = newTabSpec(tab.getTagId());
            spec.setIndicator(tab);

            addTab(spec, tab.getFragmentClass(), null);
        }
    }

    /**
     * 设置当前tab页，重新父类方法，
     * @param index
     */
    @Override
    public void setCurrentTab(int index){

        if(onChangedTabBefore(index)){
            super.setCurrentTab(index);
        }
    }


    /**
     * 改变tab前置处理
     * @param index
     * @return
     */
    protected boolean onChangedTabBefore(int index){

        return true;
    }

    @Override public void setup(Context context, FragmentManager manager) {
        super.setup(context, manager);
        //反射调用
        reflectInvoke();
        //干掉分割线
        getTabWidget().setDividerDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override public void setup(Context context, FragmentManager manager, int containerId) {
        super.setup(context, manager, containerId);
        reflectInvoke();
        getTabWidget().setDividerDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void reflectInvoke(){
        TabWidget tabWidget = getTabWidget();
        try {

            Field listenerField =
                tabWidget.getClass().getDeclaredField("mSelectionChangedListener");
            listenerField.setAccessible(true);

           final Object listenerObject =  listenerField.get(tabWidget);

            Object proxy = Proxy.newProxyInstance(tabWidget.getClass().getClassLoader(),
                listenerObject.getClass().getInterfaces(), new InvocationHandler() {
                    @Override public Object invoke(Object proxy, Method method, Object[] args)
                        throws Throwable {
                        LogUtils.d("MainTabView","--- tab changed invoke:"+args[0]+" "+args[1]);
                        if(beforeTabSelectionChanged == null
                            || !beforeTabSelectionChanged.onBeforeTabSelectionChanged((int)args[0],(boolean)args[1])){

                            return method.invoke(listenerObject,args);
                        }
                        return null;
                    }
                });

            //使用代理替换原先object
            listenerField.set(tabWidget,proxy);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private OnBeforeTabSelectionChanged beforeTabSelectionChanged;

    public void setBeforeTabSelectionChanged(
        OnBeforeTabSelectionChanged beforeTabSelectionChanged) {
        this.beforeTabSelectionChanged = beforeTabSelectionChanged;
    }

    public static interface OnBeforeTabSelectionChanged {

      /**
       *  TabSelectionChanged 前拦截
       * @param tabIndex
       * @param clicked
       * @return   true ： 拦截 不切换 ，false 不拦截，tab切换
       */
        boolean onBeforeTabSelectionChanged(int tabIndex, boolean clicked);
    }

}
