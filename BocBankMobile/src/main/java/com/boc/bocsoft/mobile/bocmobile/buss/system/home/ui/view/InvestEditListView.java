package com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 理财编辑listview
 * Created by lxw on 2016/8/14 0014.
 */
public class InvestEditListView extends ListView {

    private Context mContext;
    TextView headerTextView;

    public InvestEditListView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public InvestEditListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public InvestEditListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }


    private void init(){
        headerTextView = new TextView(mContext);
        headerTextView.setText("基金");
        this.addHeaderView(headerTextView);
    }
}
