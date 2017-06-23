package com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selecttimerange;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * 筛选时间范围
 * Created by liuweidong on 2016/5/23.
 */
public class SelectTimeRange extends LinearLayout implements View.OnClickListener {
    private View view;
    private Context mContext;
    private TextView txtStart, txtEnd, txtTitle, txtCancel;
    private Button btnLeft, btnRight;
    private ClickListener listener;
    private String start = "";
    private String end = "";

    public SelectTimeRange(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public SelectTimeRange(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public SelectTimeRange(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        view = View.inflate(mContext, R.layout.boc_select_time_range, this);
        txtTitle = (TextView) view.findViewById(R.id.txt_title);
        txtCancel = (TextView) view.findViewById(R.id.txt_cancel);
        txtStart = (TextView) view.findViewById(R.id.txt_start);
        txtEnd = (TextView) view.findViewById(R.id.txt_end);
        btnLeft = (Button) view.findViewById(R.id.btn_left);
        btnRight = (Button) view.findViewById(R.id.btn_right);
        txtStart.setOnClickListener(this);
        txtEnd.setOnClickListener(this);
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        txtCancel.setOnClickListener(this);
        this.start = getResources().getString(R.string.boc_transaction_date_start);
        this.end = getResources().getString(R.string.boc_transaction_date_end);
    }

    public TextView getTxtStart() {
        return txtStart;
    }

    public TextView getTxtEnd() {
        return txtEnd;
    }

    public void setTitleValue(String titleValue) {
        txtTitle.setText(titleValue);
    }

    public void setDefaultDate(String start, String end) {
        this.start = start;
        this.end = end;
        txtStart.setText(start);
        txtEnd.setText(end);
    }

    /**
     * 设置开始日期
     *
     * @param start
     */
    public void setStartDate(String start){
        txtStart.setText(start);
    }

    /**
     * 设置截止日期
     *
     * @param end
     */
    public void setEndDate(String end){
        txtEnd.setText(end);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.txt_start) {
            if (listener != null) {
                listener.startClick();
            }

        } else if (i == R.id.txt_end) {
            if (listener != null) {
                listener.endClick();
            }

        } else if (i == R.id.txt_cancel) {
            if (listener != null) {
                listener.cancelClick();
            }

        } else if (i == R.id.btn_right) {
            if (listener != null) {
                listener.rightClick();
            }

        } else if (i == R.id.btn_left) {
            txtStart.setText(start);
            txtEnd.setText(end);

        }
    }

    public interface ClickListener {
        void startClick();

        void endClick();

        void cancelClick();

        void rightClick();
    }

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }
}
