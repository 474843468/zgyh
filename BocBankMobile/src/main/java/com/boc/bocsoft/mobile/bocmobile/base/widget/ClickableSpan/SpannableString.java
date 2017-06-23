package com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

/**
 * @author wangyang
 *         16/9/22 17:36
 */
public class SpannableString extends TextView {

    public SpannableString(Context context) {
        super(context);
    }

    public SpannableString(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpannableString(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SpannableString(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setContent(String startTitle, String endTitle, String content, boolean isAppend, int titleColor, int contentColor, MClickableSpan.OnClickSpanListener listener, CharacterStyle... characterStyles) {
        if (!isAppend)
            setText("");

        String spannableContent = "";
        if (!StringUtils.isEmptyOrNull(startTitle))
            spannableContent += startTitle;
        if (!StringUtils.isEmptyOrNull(content))
            spannableContent += content;
        if (!StringUtils.isEmptyOrNull(endTitle))
            spannableContent += endTitle;

        android.text.SpannableString spannableStringFir = new android.text.SpannableString(spannableContent);

        if (!StringUtils.isEmptyOrNull(content)) {
            if (listener != null) {
                MClickableSpan clickableSpanFir = new MClickableSpan(content, getContext());
                clickableSpanFir.setColor(getResources().getColor(contentColor));
                spannableStringFir.setSpan(clickableSpanFir, startTitle.length(), startTitle.length() + content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                clickableSpanFir.setListener(listener);
                setMovementMethod(LinkMovementMethod.getInstance());
                setLongClickable(false);
            } else {
                spannableStringFir.setSpan(new ForegroundColorSpan(getResources().getColor(contentColor)), startTitle.length(), startTitle.length() + content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        if (characterStyles != null) {
            for (CharacterStyle style : characterStyles)
                spannableStringFir.setSpan(style, startTitle.length(), startTitle.length() + content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if (!StringUtils.isEmptyOrNull(startTitle)) {
            if (getTypeface() != null)
                spannableStringFir.setSpan(new StyleSpan(getTypeface().getStyle()), 0, startTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableStringFir.setSpan(new ForegroundColorSpan(getResources().getColor(titleColor)), 0, startTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if (!StringUtils.isEmptyOrNull(endTitle)) {
            int start = startTitle.length() + content.length();
            int end = start + endTitle.length();
            if (getTypeface() != null)
                spannableStringFir.setSpan(new StyleSpan(getTypeface().getStyle()), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableStringFir.setSpan(new ForegroundColorSpan(getResources().getColor(titleColor)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        setTypeface(Typeface.DEFAULT);
        setGravity(Gravity.TOP | Gravity.LEFT);
        append(spannableStringFir);
    }

    public void setContent(String startTitle, String endTitle, String content, MClickableSpan.OnClickSpanListener listener, CharacterStyle... characterStyles) {
        setContent(startTitle, endTitle, content, false, R.color.boc_text_color_common_gray, R.color.boc_text_color_red, listener, characterStyles);
    }

    public void setContent(String startTitle, String content, MClickableSpan.OnClickSpanListener listener, CharacterStyle... characterStyles) {
        setContent(startTitle, "", content, false, R.color.boc_text_color_common_gray, R.color.boc_text_color_red, listener, characterStyles);
    }

    public void setContent(String startTitle, String endTitle, String content, int titleColor, int contentColor, CharacterStyle... characterStyles) {
        setContent(startTitle, endTitle, content, false, titleColor, contentColor, null, characterStyles);
    }

    public void setContent(String startTitle, String content, int titleColor, int contentColor, CharacterStyle... characterStyles) {
        setContent(startTitle, "", content, false, titleColor, contentColor, null, characterStyles);
    }

    public void setContent(String startTitle, String content, int contentColor, CharacterStyle... characterStyles) {
        setContent(startTitle, "", content, false, R.color.boc_text_color_common_gray, contentColor, null, characterStyles);
    }

    public void setContent(String content, int contentColor, String endTitle, CharacterStyle... characterStyles) {
        setContent("", endTitle, content, false, R.color.boc_text_color_common_gray, contentColor, null, characterStyles);
    }

    public void setContent(String startTitle, String content, int contentColor, MClickableSpan.OnClickSpanListener listener, CharacterStyle... characterStyles) {
        setContent(startTitle, "", content, false, R.color.boc_text_color_common_gray, contentColor, listener, characterStyles);
    }

    public void setAppendContent(String startTitle, String endTitle, String content, MClickableSpan.OnClickSpanListener listener, CharacterStyle... characterStyles) {
        setContent(startTitle, endTitle, content, true, R.color.boc_text_color_common_gray, R.color.boc_text_color_red, listener, characterStyles);
    }

    public void setAppendContent(String content,int contentColor, MClickableSpan.OnClickSpanListener listener, CharacterStyle... characterStyles) {
        setContent("", "", content, true, R.color.boc_text_color_common_gray, contentColor, listener, characterStyles);
    }

    public void setAppendContent(String startTitle, String content, int contentColor, boolean isNewLine, MClickableSpan.OnClickSpanListener listener, CharacterStyle... characterStyles) {
        if (isNewLine) {
            if (getWidth() < getContentLength(startTitle + content))
                startTitle = "\n" + startTitle;
            else
                startTitle = "\t" + startTitle;
        }
        setContent(startTitle, "", content, true, R.color.boc_text_color_common_gray, contentColor, listener, characterStyles);
    }

    private float getContentLength(String content) {
        content = "\t" + content;
        if (!StringUtil.isNullOrEmpty(getText().toString()))
            content = getText() + content;

        return getPaint().measureText(content);
    }
}

