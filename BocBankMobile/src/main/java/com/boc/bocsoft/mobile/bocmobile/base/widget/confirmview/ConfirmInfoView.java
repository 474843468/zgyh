package com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.MClickableSpan;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.SpannableString;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailRow;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRowButton;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 确认信息页面
 * <p/>
 * Created by wangyang on 2016/8/10.
 */
public class ConfirmInfoView extends LinearLayout implements View.OnClickListener, DetailTableRowButton.BtnCallback {

    private LinearLayout llContent;

    private View llTitle;

    private SpannableString tvTitle, tvTitle1, tvContent;

    private SpannableString tvHint, tvBottomHint;

    private DetailTableRowButton btnSecurity;

    private Button btnOk;

    private OnClickListener onClickListener;

    public ConfirmInfoView(Context context) {
        this(context, null);
    }

    public ConfirmInfoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ConfirmInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        setListener();
    }

    private void setListener() {
        btnSecurity.setOnclick(this);
        btnOk.setOnClickListener(this);
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.boc_view_confirm, this);
        llContent = (LinearLayout) view.findViewById(R.id.ll_content);
        btnSecurity = (DetailTableRowButton) view.findViewById(R.id.btn_security);
        btnOk = (Button) view.findViewById(R.id.btn_ok);
        tvHint = (SpannableString) view.findViewById(R.id.tv_hint);
        tvBottomHint = (SpannableString) view.findViewById(R.id.tv_hint_bottom);
        btnSecurity.setBodyHeight(getResources().getDimensionPixelOffset(R.dimen.boc_button_height_88px));
    }

    /**
     * 设置温馨提示
     *
     * @param content
     */
    public void setHint(String content) {
        if (StringUtils.isEmptyOrNull(content)) {
            tvHint.setVisibility(GONE);
            return;
        }
        tvHint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        setHint(getResources().getString(R.string.boc_confirm_hint), content, R.color.boc_text_color_red, new StyleSpan(Typeface.DEFAULT.getStyle()));
    }

    /**
     * 设置提示
     *
     * @param content
     * @param textColor
     */
    public void setHint(String content, int textColor) {
        if (StringUtils.isEmptyOrNull(content)) {
            tvHint.setVisibility(GONE);
            return;
        }
        setHint("", content, textColor);
    }

    /**
     * 设置提示
     *
     * @param title
     * @param clickTitle
     * @param listener
     * @param characterStyles
     */
    public void setClickHint(String title, String clickTitle, MClickableSpan.OnClickSpanListener listener, CharacterStyle... characterStyles) {
        if (StringUtils.isEmptyOrNull(clickTitle) || StringUtils.isEmptyOrNull(title)) {
            tvHint.setVisibility(GONE);
            return;
        }

        tvHint.setContent(title, "", clickTitle, false, R.color.boc_text_color_common_gray, R.color.boc_main_button_color, listener, characterStyles);
        tvHint.setVisibility(VISIBLE);
    }

    /**
     * 设置提示
     *
     * @param content
     */
    public void setHint(String title, String content, int textColor, CharacterStyle... characterStyles) {
        if (StringUtils.isEmptyOrNull(content) && StringUtils.isEmptyOrNull(title)) {
            tvHint.setVisibility(GONE);
            return;
        }
        tvHint.setContent(title, content, textColor, textColor, characterStyles);
        tvHint.setVisibility(VISIBLE);
    }

    public void setBottomHint(String content, CharacterStyle... characterStyles) {
        if (StringUtils.isEmptyOrNull(content)) {
            tvBottomHint.setVisibility(GONE);
            return;
        }
        tvBottomHint.setContent("", content, R.color.boc_text_color_cinerous, R.color.boc_text_color_cinerous, characterStyles);
        tvBottomHint.setVisibility(VISIBLE);
    }

    /**
     * @param title
     * @param textColor
     */
    public <T extends CharSequence> void setHeadValue(T title, int textColor) {
        setHeadValue(title, textColor, true);
    }

    /**
     * 设置头部信息,默认显示与顶部灰色间距
     *
     * @param title
     * @param textColor
     */
    public <T extends CharSequence> void setHeadValue(T title, int textColor, boolean isMarginTop) {
        initTitle();

        if (isMarginTop)
            ((LinearLayout.LayoutParams) llTitle.getLayoutParams()).setMargins(0, getResources().getDimensionPixelOffset(R.dimen.boc_space_between_20px), 0, 0);

        llTitle.setBackgroundColor(getResources().getColor(R.color.boc_common_cell_color));
        tvContent.setVisibility(GONE);
        tvTitle.setVisibility(GONE);
        tvTitle1.setVisibility(VISIBLE);
        tvTitle1.setTextColor(getResources().getColor(textColor));
        tvTitle1.setText(title);
        llTitle.setVisibility(VISIBLE);
    }

    /**
     * 初始化标题
     */
    private void initTitle() {
        llTitle = View.inflate(getContext(), R.layout.boc_view_confirm_title, null);
        tvTitle = (SpannableString) llTitle.findViewById(R.id.tv_title);
        tvTitle1 = (SpannableString) llTitle.findViewById(R.id.tv_title1);
        tvContent = (SpannableString) llTitle.findViewById(R.id.tv_content);
        llContent.addView(llTitle);
    }

    /**
     * 设置头部信息,默认显示与顶部灰色间距
     *
     * @param title
     * @param content
     */
    public <T extends CharSequence> void setHeadValue(T title, T content) {
        setHeadValue(title, content, true);
    }

    /**
     * 设置头部信息
     *
     * @param title       标题
     * @param content     内容
     * @param isMarginTop 与顶部灰色间距
     */
    public <T extends CharSequence> void setHeadValue(T title, T content, boolean isMarginTop) {
        initTitle();

        if (isMarginTop)
            ((LinearLayout.LayoutParams) llTitle.getLayoutParams()).setMargins(0, getResources().getDimensionPixelOffset(R.dimen.boc_space_between_20px), 0, 0);
        llTitle.setBackgroundColor(getResources().getColor(R.color.boc_common_cell_color));
        tvTitle1.setVisibility(GONE);
        llTitle.setVisibility(VISIBLE);
        tvTitle.setText(title);
        tvContent.setText(content);
    }

    /**
     * 添加布局,默认不添加 顶部间距,底部分割线
     *
     * @param viewGroup
     */
    public void addContentView(ViewGroup viewGroup) {
        addContentView(viewGroup, false, false);
    }

    /**
     * 添加View
     *
     * @param view
     * @param isMarginTop   距离顶部是否有灰色间隔
     * @param isShowDivider 底部是否显示分割线
     */
    public void addContentView(ViewGroup view, boolean isMarginTop, boolean isShowDivider) {
        int margin30px = getResources().getDimensionPixelOffset(R.dimen.boc_space_between_30px);
        if (isMarginTop)
            view.setPadding(margin30px, getResources().getDimensionPixelOffset(R.dimen.boc_space_between_20px), margin30px, 0);
        else
            view.setPadding(margin30px, 0, margin30px, 0);

        isShowDivider(isShowDivider, view);

        llContent.addView(view);
    }

    /**
     * 添加数据,默认显示与顶部间距和底部分割线
     *
     * @param datas
     */
    public void addData(LinkedHashMap<String, ? extends CharSequence> datas) {
        addData(datas, true, true);
    }

    /**
     * 添加数据,默认不显示与顶部间距
     *
     * @param datas
     * @param isShowDivider
     */
    public void addData(LinkedHashMap<String, ? extends CharSequence> datas, boolean isShowDivider) {
        addData(datas, false, isShowDivider);
    }

    /**
     * @param datas
     * @param isMarginTop   距离顶部是否有灰色间隔
     * @param isShowDivider 底部是否显示分割线
     */
    public void addData(LinkedHashMap<String, ? extends CharSequence> datas, boolean isMarginTop, boolean isShowDivider) {
        LinearLayout linearLayout = initLayoutContent();

        isShowMarginTop(isMarginTop, linearLayout);

        addData(datas, linearLayout);

        isShowDivider(isShowDivider, linearLayout);

        llContent.addView(linearLayout);
    }

    @NonNull
    private LinearLayout initLayoutContent() {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(VERTICAL);
        linearLayout.setBackgroundColor(getResources().getColor(R.color.boc_common_cell_color));
        linearLayout.setPadding(0, getResources().getDimensionPixelOffset(R.dimen.boc_space_between_17px), 0, 0);
        return linearLayout;
    }

    private void addData(LinkedHashMap<String, ? extends CharSequence> datas, LinearLayout linearLayout) {
        for (Map.Entry<String, ? extends CharSequence> data : datas.entrySet()) {
            DetailRow detailTableRow = new DetailRow(getContext());
            int padding13px = getResources().getDimensionPixelOffset(R.dimen.boc_space_between_13px);
            detailTableRow.setPadding(0, padding13px, 0, padding13px);
            detailTableRow.getViewTreeObserver().addOnGlobalLayoutListener(detailTableRow);
            detailTableRow.updateData(data.getKey(), data.getValue());
            linearLayout.addView(detailTableRow);
        }
    }

    private void isShowMarginTop(boolean isMarginTop, View view) {
        if (isMarginTop) {
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, getResources().getDimensionPixelOffset(R.dimen.boc_space_between_20px), 0, 0);
            view.setLayoutParams(lp);
        }
    }

    private void isShowDivider(boolean isShowDivider, ViewGroup viewGroup) {
        View divider = View.inflate(getContext(), R.layout.boc_divide_line, null);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.boc_divider_1px));
        layoutParams.setMargins(0, getResources().getDimensionPixelOffset(R.dimen.boc_space_between_37px), 0, 0);
        divider.setLayoutParams(layoutParams);

        if (isShowDivider)
            divider.setVisibility(VISIBLE);
        else
            divider.setVisibility(INVISIBLE);

        viewGroup.addView(divider);
    }

    /**
     * 此方法已废弃,安全控件默认不显示,在updateSecurity时候会自动显示
     *
     * 设置是否显示安全控件,默认显示
     *
     * @param isShowSecurity
     */
    @Deprecated
    public void isShowSecurity(boolean isShowSecurity) {
        if (isShowSecurity)
            btnSecurity.setVisibility(VISIBLE);
        else
            btnSecurity.setVisibility(GONE);
    }

    /**
     * 修改安全因子名称
     *
     * @author wangyang
     * @time 16/9/20 01:09
     */
    public void updateSecurity(String name) {
        updateSecurity(getContext().getString(R.string.security_title), name);
    }

    /**
     * 修改安全因子名称 和 title
     *
     * @author yx
     * @time 2016年10月25日 18:52:32
     */
    public void updateSecurity(String SecurityTitle, String name) {
        btnSecurity.setVisibility(VISIBLE);
        btnSecurity.addTextBtn(SecurityTitle, name, getContext().getString(R.string.security_change_verify));
    }

    /**
     * 修改按钮名称
     *
     * @param name
     * @param <T>
     */
    public <T extends CharSequence> void updateButton(T name) {
        btnOk.setText(name);
    }

    @Override
    public void onClick(View v) {
        if (v == btnOk)
            onClickListener.onClickConfirm();
    }

    @Override
    public void onClickListener() {
        onClickListener.onClickChange();
    }

    public interface OnClickListener {
        void onClickConfirm();

        void onClickChange();
    }

    public void setListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
