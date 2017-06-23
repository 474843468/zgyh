package com.chinamworld.llbt.userwidget.securityview;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.chinamworld.llbt.userwidget.dialogview.BaseDialog;


/**
 * Created by Administrator on 2016/8/30.
 */
public class SecurityDialog extends BaseDialog {
    public SecurityDialog(Context context) {
        super(context);
    }

    @Override
    protected View initView() {
        return null;
    }

    public void showSecurityDialog(Activity context){
        SecurityDialog dialog = new SecurityDialog(context);

        dialog.show();
    }
}
