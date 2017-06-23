package com.chinamworld.llbt.userwidget.dialogview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.llbtwidget.R;


/**
 * 提示信息弹出框
 * Created by yuht on 2016/8/24.
 */
public class MessageDialog extends BaseInstanceDialog implements View.OnClickListener{

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_dialog_error_enter || v.getId() == R.id.btn_Ok){
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

    /** 提示信息文本 */
    private TextView mMessageTextView;
    private Button mOK,mCancel,mOK2;
    private ViewGroup oneLayout,twoLayout;

    private View.OnClickListener mOnClickListener;

    private MessageDialog(Context context)  {
        super(context);
    }

    @Override
    protected View initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.llbt_message_dialog_layout, null);
        oneLayout = (ViewGroup)view.findViewById(R.id.onelayout);
        twoLayout = (ViewGroup)view.findViewById(R.id.twoLayout);
        mMessageTextView = (TextView)view.findViewById(R.id.message_tv);
        mOK = (Button) view.findViewById(R.id.btn_dialog_error_enter);
        mOK.setText("确定");
        mOK.setOnClickListener(this);
        mCancel = (Button)view.findViewById(R.id.btn_dialog_error_cancel);
        mCancel.setText("取消");
        mCancel.setOnClickListener(this);
        mOK2 = (Button)view.findViewById(R.id.btn_Ok);
        mOK2.setOnClickListener(this);
        return view;
    }

    private void setOnClickListener(View.OnClickListener onClickListener){
        mOnClickListener = onClickListener;
    }

    private void setButtonLayoutVisibility(int type){
        oneLayout.setVisibility(type == 1 ? View.VISIBLE : View.GONE);
        twoLayout.setVisibility(type == 1 ? View.GONE : View.VISIBLE);
    }
    /**
     * 弹出提示信息
     * @param context : 当前activity
     * @param message ： 提示信息
     */
    public static void showMessageDialog(Context context, String message){
        try{
            MessageDialog dialog = new MessageDialog(context);
            dialog.setButtonLayoutVisibility(1);
            dialog.mMessageTextView.setText(message);
            dialog.show();
        }
        catch(Exception e){

        }

    }

    /**
     * 弹出提示信息
     * @param context : 当前activity
     * @param message ： 提示信息
     */
    public static void showMessageDialog(Context context, String message,int gravity){
        try{
            MessageDialog dialog = new MessageDialog(context);
            dialog.setButtonLayoutVisibility(1);
            dialog.mMessageTextView.setText(message);
            dialog.mMessageTextView.setGravity(gravity);
            dialog.show();
        }
        catch(Exception e){

        }

    }

    /**
     * 弹出提示信息
     * @param context :当前ａｃｔｉｖｉｔｙ
     * @param message :提示信息
     * @param onListener ：“确定”按钮点击事件
     */
    public static void showMessageDialog(Context context, String message, View.OnClickListener onListener){
        try{
            MessageDialog dialog = new MessageDialog(context);
            dialog.setButtonLayoutVisibility(1);
            dialog.mMessageTextView.setText(message);
            dialog.setOnClickListener(onListener);
            dialog.show();
        }
        catch(Exception e){

        }

    }

    /**
     * 两个按钮的提示框
     * @param context :上下文
     * @param message :提示信息内容
     * @param onListener : 点击事件监听
     */
    public static void showMessageDialogWithTwoButton(Context context, String message, View.OnClickListener onListener){
        try{
            MessageDialog dialog = new MessageDialog(context);
            dialog.mMessageTextView.setText(message);
            dialog.setButtonLayoutVisibility(2);
            dialog.setOnClickListener(onListener);
            dialog.show();
        }
        catch(Exception e){

        }

    }

    /**
     * 个按钮的提示框
     * @param context :上下文
     * @param message:提示信息内容
     * @param firstButtonText :第一个按钮文字
     * @param secondButtonText　：第二个按钮文字
     * @param onListener: 点击事件监听
     */
    public static void showMessageDialogWithTwoButton(Context context, String message, String firstButtonText, String secondButtonText , View.OnClickListener onListener){

        try{
            MessageDialog dialog = new MessageDialog(context);
            dialog.mMessageTextView.setText(message);
            dialog.mCancel.setText(firstButtonText);
            dialog.mOK.setText(secondButtonText);
            dialog.setOnClickListener(onListener);
            dialog.setButtonLayoutVisibility(2);
            dialog.show();
        }

        catch(Exception e){

        }
    }

    /**
     * 关闭弹出框
     */
    public static void closeDialog(){
        BaseInstanceDialog.closeDialog(MessageDialog.class);
    }

}
