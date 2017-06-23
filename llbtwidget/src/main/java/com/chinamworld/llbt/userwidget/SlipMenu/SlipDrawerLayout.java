package com.chinamworld.llbt.userwidget.SlipMenu;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.View;

import com.chinamworld.llbtwidget.R;


/**
 * 侧滑菜单,该控件布局需要按照API要求。
 * <p/>
 * <?xml version="1.0" encoding="utf-8"?>
 * <p/>
 * 根布局
 * <com.example.testslidemenu.SlipDrawerLayout
 * xmlns:android="http://schemas.android.com/apk/res/android"
 * android:id="@+id/drawer_layout" android:layout_width="match_parent"
 * android:layout_height="match_parent" >
 * <p/>
 * 这是第一层内容页
 * <TextView android:id="@+id/content_frame" android:layout_width="match_parent"
 * android:layout_height="match_parent" android:text="这是内容页" />
 * <p/>
 * 这是第二层侧边栏,注意关键属性  android:layout_gravity="end"  或者 right|left
 * <TextView android:id="@+id/right_drawer" android:layout_width="match_parent"
 * android:layout_height="match_parent" android:layout_gravity="end"
 * android:background="#111" android:text="这是右边栏"
 * android:textColor="@android:color/white" android:textSize="24sp" />
 * <p/>
 * </com.example.testslidemenu.SlipDrawerLayout>
 *
 * @author wangyang 2016-6-4 上午11:04:54
 */
public class SlipDrawerLayout extends DrawerLayout {
    /**
     * 当前状态,打开 true,关闭 false
     */
    private boolean isOpen;

    public boolean isOpen() {
        return isOpen;
    }

    public SlipDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SlipDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlipDrawerLayout(Context context) {
        super(context);
        init();
    }

    private void init() {
        Activity activity = null;
        if (getContext() instanceof Activity)
            activity = (Activity) getContext();

        // 设置抽屉打开时，主要内容区被自定义阴影覆盖
        setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        setDrawerListener(new DrawerLayoutStateListener());
    }

    /**
     * 开启/关闭侧边栏
     */
    public void toggle() {
        toggle(GravityCompat.END);
    }

    /**
     * 根据位置开启/关闭侧边栏
     */
    public void toggle(int gravity) {
        if (!isOpen)
            openDrawer(gravity);
        else
            closeDrawer(gravity);
    }

    /**
     * 传入侧边栏View,开启/关闭
     *
     * @param view
     */
    public void toggle(View view) {
        if (!isOpen)
            openDrawer(view);
        else
            closeDrawer(view);
    }

    private class DrawerLayoutStateListener extends SimpleDrawerListener {
        /**
         * 当导航菜单打开时执行
         */
        @Override
        public void onDrawerOpened(View drawerView) {
            isOpen = true;
            setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }

        /**
         * 当导航菜单关闭时执行
         */
        @Override
        public void onDrawerClosed(View drawerView) {
            isOpen = false;
            setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

}
