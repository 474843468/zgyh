package com.chinamworld.llbt.userwidget.dialogview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.llbt.utils.AnimUtils;
import com.chinamworld.llbtwidget.R;

/**
 * Created by Administrator on 2016/8/25.
 */
public class LoadingDialog extends BaseInstanceDialog {

    protected ImageView ivProgressbar;
    protected TextView tvText;
    View btnClose;
    private View contentView;
    private Animation loadingAnim;

    private LoadingDialog(Context context){
        super(context);
    }

    @Override
    protected View initView() {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.loading_layout,null);
        loadingAnim = AnimUtils.getRotateCircleAnimation(mContext);
        ivProgressbar = (ImageView) contentView.findViewById(R.id.iv_progressbar);
        tvText = (TextView) contentView.findViewById(R.id.tv_text);
        btnClose = contentView.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialog.this.dismiss();
            }
        });
        return contentView;
    }



    private void startAnimation() {
        ivProgressbar.startAnimation(loadingAnim);
    }

    /**
     * 显示通讯框
     */
    public static void showLoadingDialog(Context context) {
        LoadingDialog dialog = new LoadingDialog(context);
        dialog.startAnimation();
        dialog.show();
    }

    /**
     * 关闭弹出框
     */
    public static void closeDialog(){
        BaseInstanceDialog.closeDialog(LoadingDialog.class);
    }

}
