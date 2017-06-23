package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.BaseDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.eshield.PESAGifMovieView;

/**
 * Created by wangtong on 2016/5/31.
 */
public class SecurityAnimationDialog extends BaseDialog {
    public static final int ANIM_MODIFY_PASSWORD = 0;
    public static final int ANIM_TRADE = 1;
    public static final int ANIM_COMMON = 2;

    private View rootView;
    private TextView titleText;
    private PESAGifMovieView animation;

    public SecurityAnimationDialog(Context context) {
        super(context);
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
    }

    @Override
    protected View onAddContentView() {
        rootView = inflater.inflate(R.layout.boc_security_animation, null);
        return rootView;
    }

    public void showAnimationDialog(String title, int type) {
        titleText.setText(title);
        if (type == ANIM_MODIFY_PASSWORD) {
            animation.setMovieResource(R.drawable.boc_eshield_anim_password);
        } else if (type == ANIM_TRADE) {
            animation.setMovieResource(R.drawable.boc_eshield_anim_trade);
        } else if (type == ANIM_COMMON) {
            animation.setMovieResource(R.drawable.boc_eshield_anim_common);
        }
        show();
    }

    @Override
    protected void initView() {
        titleText = (TextView) rootView.findViewById(R.id.title);
        animation = (PESAGifMovieView) rootView.findViewById(R.id.e_shield_animation);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
    }

}
