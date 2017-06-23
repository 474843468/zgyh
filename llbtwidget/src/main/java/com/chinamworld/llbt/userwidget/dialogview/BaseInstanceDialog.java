package com.chinamworld.llbt.userwidget.dialogview;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * 单例模式的对话框基类
 * Created by yuht on 2016/9/12.
 */
public abstract class BaseInstanceDialog extends BaseDialog {

    private static List<BaseInstanceDialog> curListDialog = new ArrayList<BaseInstanceDialog>();
    public BaseInstanceDialog(Context context) {
        super(context);
        closeDialog(this.getClass());
//        BaseInstanceDialog dialog = getCurInstanceDialog(this.getClass());
//        if(dialog != null) {
//            dialog.dismiss();
//        }
        setCurInstanceDialog();
    }

    public static BaseInstanceDialog getCurInstanceDialog(Class<?> type){
        for(BaseInstanceDialog dialog : curListDialog){
            if(dialog.getClass() == type){
                return dialog;
            }
        }
        return null;
    }

    public void setCurInstanceDialog(){
        BaseInstanceDialog dialog;
        for(int i = 0; i < curListDialog.size();i++){
            dialog = curListDialog.get(i);
            if(dialog.getClass() == this.getClass()){
                curListDialog.remove(dialog);
                i--;
            }

        }
        curListDialog.add(this);
    }
    /**
     * 关闭弹出框
     */
    protected static void closeDialog(Class<?> type){
        try{
            BaseInstanceDialog dialog = getCurInstanceDialog(type);
            if(dialog != null && dialog.isShowing())
                dialog.dismiss();
            curListDialog.remove(dialog);
        }
        catch (Exception e){

        }

    }




}
