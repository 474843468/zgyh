package com.boc.bocsoft.mobile.bocmobile.base.widget.details;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.view.View;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * 最近交易明细显示的title
 * Created by niuguobin on 2016/7/5.
 */
public class AccountDetailHead extends LinearLayout{
    private View rootView;
    private Context mContext;
    private LinearLayout layoutBody;
    private TextView title;
    public AccountDetailHead(Context context) {
        this(context , null, 0);
    }

    public AccountDetailHead(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public AccountDetailHead(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.
        initView();
    }

    private void initView() {
        rootView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.boc_view_detail_account_title, this);
        title  =(TextView) rootView.findViewById(R.id.title);
    }

    public void setTitle(String text){
        title.setText(text);
    }

}
