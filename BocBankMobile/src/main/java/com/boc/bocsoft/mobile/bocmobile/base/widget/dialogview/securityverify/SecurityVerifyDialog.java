package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.BaseDialog;

/**
 * Created by wangtong on 2016/5/31.
 */
public class SecurityVerifyDialog extends BaseDialog implements CFCAEditTextView.SecurityKeyboardListener {

    protected Activity activity;
    protected View rootView;
    //随机数加密数据
    protected String[] encryptRandomNums;
    //密码加密数据
    protected String[] encryptPasswords;

    protected int scrollerY = 0;

    public SecurityVerifyDialog(Context context) {
        super(context, R.style.dialog_normal);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected View onAddContentView() {
        return null;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        //如果有确定和取消按钮，监听按钮事件
        ImageView cancel = (ImageView) findViewById(R.id.btn_dialog_cancel);
        if (cancel != null) {
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancel();
                }
            });
        }

        TextView ok = (TextView) findViewById(R.id.btn_dialog_ok);
        if (ok != null) {
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputCompleted();
                }
            });
        }

        TextView btnCanNotReceive = (TextView) findViewById(R.id.can_not_receive);
        if (btnCanNotReceive != null) {
            btnCanNotReceive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivity(new Intent(activity, CannotReceivePage.class));
                }
            });
        }
    }

    protected void inputCompleted() {
        if (SecurityVerity.getInstance().verifyCodeResult != null) {
            if (getEncryptResults()) {
                String id = SecurityVerity.getInstance().getCurrentSecurityVerifyTypeId();
                SecurityVerity.getInstance().verifyCodeResult.onEncryptDataReturned(id,
                        encryptRandomNums, encryptPasswords);
                cancel();
            }
        }
    }

    protected void hiddenSecurityKeyboard() {
    }

    protected boolean getEncryptResults() {
        return false;
    }

    public void sendSmsResult(boolean isSucceed) {
    }

    private int getScrollerY(int keyBoardHeight) {
        int scrollDy;
        int inputViewHeight, screenHeight;
        int[] location = new int[2];
        rootView.getLocationOnScreen(location);

        inputViewHeight = location[1] + rootView.getHeight();
        screenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        scrollDy = (inputViewHeight + keyBoardHeight) - screenHeight;
        return scrollDy;
    }

    @Override
    public void onKeyBoardShow(int keyBoardHeight) {
        if (scrollerY == 0) {
            scrollerY = getScrollerY(keyBoardHeight);
        }

        if (scrollerY > 0) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.y = 0 - scrollerY;
            getWindow().setAttributes(params);
        }
    }

    @Override
    public void onKeyBoardHidden() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.y = 0;
        getWindow().setAttributes(params);
    }
}
