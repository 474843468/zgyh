package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.loadingDialog;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.BaseDialog;
import com.boc.bocsoft.mobile.framework.utils.AnimUtils;

/**
 * 全局加载对话框,通过构造函数构造对象使用。
 * 提供显示{@link #show()}、取消{@link #dismiss()}、
 * 设置提示文字{@link #setText(String)}
 * 设置是否启用返回键{@link #setCancelable(boolean)}方法
 * <p>使用者可通过设置取消监听器{@link #setOnCancelListener(OnCancelListener)}
 * 或者消失监听器{@link #setOnDismissListener(OnDismissListener)}来监听对话框关闭事件
 *
 * <p>全局加载对话框如果设置禁止取消{@link #setCancelable(boolean)}，则不显示关闭按钮。后退键取消和关闭按钮关闭都是{@link #cancel()}
 * 对应cancelOnClickListener和onDismissListner
 * <p>dismiss函数对应 onDismissListner
 * <p>3查询类型的功能可以取消全局加载对话框，交易类禁止取消
 * <p>Created by Administrator on 2016/5/26.
 */
public class GlobalLoadingDialog extends BaseDialog {

    private ImageView ivProgressbar;
    private TextView tvText;
    private ImageView btnClose;
    private View contentView;
    private Animation loadingAnim;
    private CloseLoadingDialogListener mCloseLoadingDialogListener;

    public GlobalLoadingDialog(Context context) {
        super(context);
    }

    @Override
    protected View onAddContentView() {
        contentView = inflateView(R.layout.boc_dialog_loading);
        return contentView;
    }

    @Override
    protected void initView() {
        loadingAnim = AnimUtils.getRotateCircleAnimation(mContext);
        ivProgressbar = (ImageView) contentView.findViewById(R.id.iv_progressbar);
        tvText = (TextView) contentView.findViewById(R.id.tv_text);
        btnClose = (ImageView) contentView.findViewById(R.id.btn_close);
        setText(mContext.getString(R.string.boc_loading));
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    /**
     * 设置全局加载对话框的提示文字
     *
     * @param text 提示文字
     */
    public void setText(String text) {
        tvText.setText(text);
    }

    /**
     * 展示全局加载对话框，开启logo的旋转动画
     */
    @Override
    public void show() {
        super.show();
        ivProgressbar.startAnimation(loadingAnim);
    }

    /**
     * 关闭全局加载对话框，关闭logo的旋转动画
     */
    @Override
    public void dismiss() {
        super.dismiss();
        ivProgressbar.clearAnimation();
    }

    /**
     * 设置是否使用返回键取消对话框的功能，如果使用的话，关闭按钮可见；如果不使用的话，关闭按钮不可见。
     *
     * @param flag true为返回键可以取消对话框，false为不可。
     */
    @Override
    public void setCancelable(boolean flag) {
        //默认的setCancelable方法设置的mCancelable只用于了onBackPressed里判断要不要cancel
        //直接调用cancel方法的话是可以直接取消的。
        super.setCancelable(flag);
        if (flag) {
            btnClose.setVisibility(View.VISIBLE);
        } else {
            btnClose.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCloseLoadingDialogListener != null) {
            mCloseLoadingDialogListener.onCloseLoadingDialog();
        }
    }

    public void setCloseLoadingDialogListener(
            CloseLoadingDialogListener closeLoadingDialogListener) {
        mCloseLoadingDialogListener = closeLoadingDialogListener;
    }

    public interface CloseLoadingDialogListener {
        void onCloseLoadingDialog();
    }
}
