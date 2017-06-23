package com.boc.bocsoft.mobile.bocmobile.base.widget.operation;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseRecycleViewAdapter;


/**
 * 操作结果底部按钮
 * Created by niuguobin on 2016/6/2.
 */
public class ResultBottom extends LinearLayout {

    private LinearLayout llNeed, llLike,llContent;

    private OnClickListener needListener;

    public ResultBottom(Context context) {
        super(context);
        init();
    }

    public ResultBottom(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.boc_view_result_bottom, this);
        llNeed = (LinearLayout) view.findViewById(R.id.ll_need);
        llLike = (LinearLayout) view.findViewById(R.id.ll_like);
//        rvLike = (RecyclerView) view.findViewById(R.id.rv_like);
        llContent = (LinearLayout) view.findViewById(R.id.ll_content);
    }

    public void setNeedListener(OnClickListener listener){
        this.needListener = listener;
    }

    public void addContentView(View view){
        llContent.setVisibility(VISIBLE);
        llContent.addView(view);
    }

    public void addNeedItem(String name, final int id) {
        llNeed.setVisibility(VISIBLE);
        EditChoiceWidget editChoiceWidget = new EditChoiceWidget(getContext());
        editChoiceWidget.getChoiceNameTextView().setTypeface(Typeface.DEFAULT_BOLD);
        editChoiceWidget.setChoiceTextName(name);
        editChoiceWidget.setNameWidth();
        editChoiceWidget.setBottomLineVisibility(true);
        llNeed.addView(editChoiceWidget);
        editChoiceWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (needListener != null)
                    needListener.onClick(id);
            }
        });
    }

    public void addNeedItem(String name, View.OnClickListener listener) {
        llNeed.setVisibility(VISIBLE);
        EditChoiceWidget editChoiceWidget = new EditChoiceWidget(getContext());
        editChoiceWidget.getChoiceNameTextView().setTypeface(Typeface.DEFAULT_BOLD);
        editChoiceWidget.setNameWidth();
        editChoiceWidget.setMinimumHeight(getResources().getDimensionPixelOffset(R.dimen.boc_space_between_88px));
        editChoiceWidget.setChoiceTextName(name);
        editChoiceWidget.setBottomLineVisibility(true);
        llNeed.addView(editChoiceWidget);
        editChoiceWidget.setOnClickListener(listener);
    }

    public void addLikeView(BaseRecycleViewAdapter adapter){
        llLike.setVisibility(VISIBLE);
        RecyclerView rvLike = initRecyclerView();
        LinearLayoutManager layoutManager = new FullyLinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        rvLike.setLayoutManager(layoutManager);
        rvLike.setAdapter(adapter);
        llLike.addView(rvLike);
    }

    @NonNull
    private RecyclerView initRecyclerView() {
        RecyclerView rvLike = new RecyclerView(getContext());
        rvLike.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rvLike.setBackgroundColor(getResources().getColor(R.color.boc_main_bg_color));
        int padding8px = getContext().getResources().getDimensionPixelOffset(R.dimen.boc_space_between_8px);
        rvLike.setPadding(padding8px,0,padding8px,0);
        return rvLike;
    }

    public void addLikeView(View view){
        llLike.setVisibility(VISIBLE);
        llLike.addView(view);
    }

    public interface OnClickListener {
        void onClick(int id);
    }

}
