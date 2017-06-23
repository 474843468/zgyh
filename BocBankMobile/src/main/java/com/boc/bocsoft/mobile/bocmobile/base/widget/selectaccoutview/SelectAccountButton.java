package com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.squareup.picasso.Picasso;

import static com.boc.bocsoft.mobile.bocmobile.base.utils.AccountUtils.getCardPic;

/**
 * 选择账户按钮
 * Created by XieDu on 2016/7/5.
 */
public class SelectAccountButton extends LinearLayout {
    protected ImageView ivPic;
    protected TextView tvNumber;
    protected TextView tvName;
    protected View rootView;
    protected ImageView ivArrow;
    protected TextView tvTips;
    private AccountBean mAccountBean;

    public SelectAccountButton(Context context) {
        super(context);
        initView();
    }

    public SelectAccountButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SelectAccountButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SelectAccountButton(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        View rootView = inflate(getContext(), R.layout.boc_button_select_account, this);
        ivPic = (ImageView) rootView.findViewById(R.id.iv_pic);
        tvNumber = (TextView) rootView.findViewById(R.id.tv_number);
        tvName = (TextView) rootView.findViewById(R.id.tv_name);
        ivArrow = (ImageView) rootView.findViewById(R.id.iv_arrow);
        tvTips = (TextView) rootView.findViewById(R.id.tv_tips);
    }

    public AccountBean getData() {
        return mAccountBean;
    }

    public void setData(AccountBean accountBean) {
        if (accountBean == null) {
            resetView(true);
            return;
        }
        resetView(false);
        mAccountBean = accountBean;
        Picasso.with(getContext()).load(getCardPic(mAccountBean)).into(ivPic);
        tvNumber.setText(NumberUtils.formatCardNumber(mAccountBean.getAccountNumber()));
        tvName.setText(mAccountBean.getNickName());
    }

    /**
     * 设置箭头是否可见
     *
     * @param visible 是否可见
     */
    public void setArrowVisible(boolean visible) {
        if (visible) {
            ivArrow.setVisibility(View.VISIBLE);
        } else {
            ivArrow.setVisibility(INVISIBLE);
        }
    }

    /**
     * 是否重置视图
     *
     * @param reset 是否重置
     */
    public void resetView(boolean reset) {
        int visibleTips = reset ? VISIBLE : INVISIBLE;
        int visibleData = reset ? INVISIBLE : VISIBLE;
        tvTips.setVisibility(visibleTips);
        ivPic.setVisibility(visibleData);
        tvNumber.setVisibility(visibleData);
        tvName.setVisibility(visibleData);
    }
}
