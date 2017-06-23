package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.loadingDialog;

/**
 * 全局加载对话框控制的相关操作
 * Created by feibin on 2016/5/28.
 */
public interface  LoadingDialogControl {
    /**
     * 关闭全局加载对话框
     */
    void closeProgressDialog();

    /**
     * 展示全局加载对话框
     * @return 全局加载对话框
     */
    GlobalLoadingDialog showLoadingDialog();
    
    /**
     * 展示全局加载对话框
     * @return 全局加载对话框
     */
    GlobalLoadingDialog showLoadingDialog(boolean flag);
    
    /**
     * 展示全局加载对话框，设置提示文字
     * @param text 提示文字
     * @return 设置了指定文字的全局加载对话框
     */
    GlobalLoadingDialog showLoadingDialog(String text);
    
    /**
     * 展示全局加载对话框，设置提示文字
     * @param message
     * @param flag
     * @return
     */
    GlobalLoadingDialog showLoadingDialog(String message, boolean flag);
}
