package com.boc.bocsoft.mobile.bocmobile.buss.system.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 基金，贵金属，结构汇View基类
 * Created by gwluo on 2016/9/8.
 */
public class BaseItemView<T> extends LinearLayout {

    public BaseItemView(Context context) {
        super(context);
    }

    public BaseItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected ReloadCallBack mCallback;

    public void setCallBack(ReloadCallBack callBack) {
        mCallback = callBack;
    }

    public interface ReloadCallBack<S> {
        public void onReload(S bean);
    }

    protected T itemData;

    public void setData(T data) {
        this.itemData = data;
    }
}
