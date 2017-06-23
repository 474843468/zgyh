package com.chinamworld.llbt.userwidget.dialogview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chinamworld.llbtwidget.R;

/**
 * Created by Administrator on 2016/9/12.
 */
public class UserDialog extends BaseInstanceDialog implements View.OnClickListener{

    private Button mOK,mCancel;

    private View.OnClickListener mOnClickListener;


    /** 自定义内容布局容器 */
    private ViewGroup contentView;

    /** 按钮所在区域布局 */
    private ViewGroup btLayout;

    public UserDialog(Context context) {
        super(context);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_dialog_error_enter){
            if(mOnClickListener != null) {
                v.setTag(DialogButtonType.secondBt);
                mOnClickListener.onClick(v);
            }
            else
               closeDialog();
        }
        else if(v.getId() == R.id.btn_dialog_error_cancel){
            if(mOnClickListener != null) {
                v.setTag(DialogButtonType.firstBt);
                mOnClickListener.onClick(v);
            }
            else
                closeDialog();
        }
    }

    @Override
    protected View initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.llbt_userdialog_layout, null);
        contentView = (ViewGroup) view.findViewById(R.id.containLayout);
        btLayout = (ViewGroup)view.findViewById(R.id.btLayout);
        mOK = (Button) view.findViewById(R.id.btn_dialog_error_enter);
        mOK.setText("确定");
        mOK.setOnClickListener(this);
        mCancel = (Button)view.findViewById(R.id.btn_dialog_error_cancel);
        mCancel.setText("取消");
        mCancel.setOnClickListener(this);
        return view;
    }

    /**
     * 个按钮的提示框
     * @param context :上下文
     * @param contentView:自定义的View
     * @param onListener: 点击事件监听
     */
    public static void showDialogWithTwoButton(Context context, View contentView, View.OnClickListener onListener){
        UserDialog dialog = new UserDialog(context);
        dialog.contentView.addView(contentView);
        dialog.mOnClickListener = onListener;
        dialog.show();
    }
    /**
     * 个按钮的提示框
     * @param context :上下文
     * @param contentView:自定义的View
     * @param firstButtonText :第一个按钮文字
     * @param secondButtonText　：第二个按钮文字
     * @param onListener: 点击事件监听
     */
    public static void showDialogWithTwoButton(Context context, View contentView, String firstButtonText, String secondButtonText , View.OnClickListener onListener){
        UserDialog dialog = new UserDialog(context);
        dialog.contentView.addView(contentView);
        dialog.mCancel.setText(firstButtonText);
        dialog.mOK.setText(secondButtonText);
        dialog.mOnClickListener = onListener;
        dialog.show();
    }


    /**
     * 完全自定义布局
     * @param context ： 上下文
     * @param contentView ： 布局文件
     */
    public static void showDialogWithAllUser(Context context, View contentView){
        UserDialog dialog = new UserDialog(context);
        dialog.contentView.addView(contentView);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
        dialog.btLayout.setLayoutParams(lp);
        dialog.show();

    }

    /**
     * 关闭弹出框
     */
    public static void closeDialog(){
      BaseInstanceDialog.closeDialog(UserDialog.class);
    }

}
