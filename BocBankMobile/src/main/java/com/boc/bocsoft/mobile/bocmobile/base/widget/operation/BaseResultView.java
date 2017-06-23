package com.boc.bocsoft.mobile.bocmobile.base.widget.operation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.SpannableString;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseRecycleViewAdapter;

import java.util.LinkedHashMap;

/**
 * 新操作结果页
 * Created by wangyang on 2016/11/25.
 */
public class BaseResultView extends LinearLayout implements View.OnClickListener {

    private ResultHead resultHead;
    private ResultDetail resultDetail;
    private ResultBottom resultBottom;
    private Button btnHome;
    private HomeBackListener listener;

    public BaseResultView(Context context) {
        this(context, null);
    }

    public BaseResultView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private final void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.boc_view_result_base, this);
        resultHead = (ResultHead) view.findViewById(R.id.result_head);
        resultDetail = (ResultDetail) view.findViewById(R.id.result_detail);
        resultBottom = (ResultBottom) view.findViewById(R.id.result_bottom);
        btnHome = (Button) view.findViewById(R.id.btn_home);
        btnHome.setOnClickListener(this);
    }

    /**
     * 更新操作结果
     */
    public void addStatus(ResultHead.Status status, String text) {
        resultHead.addStatus(status, text);
    }

    /**
     * 添加标题
     *
     * @param title
     * @param <T>
     */
    public <T extends CharSequence> void addTitle(T title) {
        resultHead.addTitle(title);
    }

    /**
     * 获取标题组件
     *
     * @return
     */
    public SpannableString getTvTitle() {
        return resultHead.getTitle();
    }

    public void addHeadView(View view) {
        resultHead.addHeadView(view);
    }

    public SpannableString getTvDetail() {
        return resultDetail.getDetail();
    }

    public <T extends CharSequence> void updateDetail(T detail) {
        resultDetail.updateDetail(detail);
    }

    public void addTopDetail(View view) {
        resultDetail.addTopDetail(view);
    }

    public void addTopDetail(LinkedHashMap<String, ? extends CharSequence> map) {
        resultDetail.addTopDetail(map);
    }

    public void addDetail(LinkedHashMap<String, ? extends CharSequence> map) {
        resultDetail.addDetail(map);
    }

    public void addDetail(View view) {
        resultDetail.addDetail(view);
    }

    public void setOnDetailClickListener(ResultDetail.OnDetailClickListener listener) {
        resultDetail.setOnDetailClickListener(listener);
    }

    /**
     * 设置提示文字
     *
     * @param hint
     * @param <T>
     */
    public <T extends CharSequence> void setHint(T hint) {
        resultDetail.setHint(hint);
    }

    /**
     * 详情提示下的LinerLayout添加View
     *
     * @param view
     */
    public void addBottomView(View view) {
        resultBottom.addContentView(view);
    }

    public void setNeedListener(ResultBottom.OnClickListener listener) {
        resultBottom.setNeedListener(listener);
    }

    public void addNeedItem(String name, final int id) {
        resultBottom.addNeedItem(name, id);
    }

    public void addNeedItem(String name, View.OnClickListener listener) {
        resultBottom.addNeedItem(name, listener);
    }

    public void addLikeView(BaseRecycleViewAdapter adapter) {
        resultBottom.addLikeView(adapter);
    }

    public void addLikeView(View view) {
        resultBottom.addLikeView(view);
    }

    public void updateGoHome(CharSequence name) {
        btnHome.setText(name);
    }

    @Override
    public void onClick(View v) {
        if (listener != null)
            listener.onHomeBack();
        else
            ActivityManager.getAppManager().finishActivity();
    }

    /**
     * 底部返回首页的按钮回调函数
     */
    public interface HomeBackListener {
        void onHomeBack();
    }

    /**
     * 底部返回首页的按钮点击事件
     *
     * @param listener
     */
    public void setOnHomeBackClick(HomeBackListener listener) {
        this.listener = listener;
    }
}
