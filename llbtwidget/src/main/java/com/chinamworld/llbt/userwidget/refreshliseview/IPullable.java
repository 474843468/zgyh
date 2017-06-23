package com.chinamworld.llbt.userwidget.refreshliseview;

/**
 * 拉动刷新布局是否可以拉动实现接口
 * Created by yuht on 2016/10/14.
 */
public interface IPullable {
    /**
     * 判断是否可以拉动刷新，如果不需要拉动功能可以直接return false
     *
     * @return true如果可以上拉否则返回false
     */
    boolean canPull();
}
