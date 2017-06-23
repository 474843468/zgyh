package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.widget;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.BaseDialog;

/**
 */
public class DoubleDataDialog extends BaseDialog {

    private View contentView;
    private Button enterBtn;
    private LinearLayout ll_content_container;

    public DoubleDataDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected View onAddContentView() {
        contentView = inflateView(R.layout.dialog_doubledata);
        return contentView;
    }

    @Override
    protected void initView() {
        ll_content_container=(LinearLayout)contentView.findViewById(R.id.ll_content_container);
        enterBtn = (Button) contentView
                .findViewById(R.id.btn_dialog_error_enter);
        setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
    }

    @Override
    protected void initData() {
        // 关闭按钮是否显示
        // showCloseOutside(false);
    }

    @Override
    protected void setListener() {

        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                cancel();
                if (mListener != null) {
                    mListener.onBottomViewClick();
                }
            }
        });
    }



    /**
    *  添加item
    */
    public DoubleDataDialog addData(String leftMsg,String rightMsg){
        DoubleDataItem dataItem=new DoubleDataItem(mContext);
        dataItem.setContent(leftMsg, rightMsg);
        ll_content_container.addView(dataItem);
        return this;
    }

    /**
     * 设置按钮文字
     *
     * @param btnText
     */
    public DoubleDataDialog setBtnText(String btnText) {
        // 按钮文字
        enterBtn.setText(btnText);
        return this;
    }

    private OnBottomViewClickListener mListener;

    /**
     * 底部按钮view的回调接口监听方法实现
     */
    public void setOnBottomViewClickListener(
            OnBottomViewClickListener mListener) {
        this.mListener = mListener;
    }

    /**
     * 点击事件回调接口监听
     */
    public interface OnBottomViewClickListener {
        /**
         * 点击事件传递
         */
        abstract void onBottomViewClick();

    }
}
