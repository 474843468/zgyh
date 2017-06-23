package com.boc.bocsoft.mobile.bocmobile.base.widget.shareinfoview;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.shareinfoview.adapter.ListHorizontalAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.shareinfoview.bean.ListHorizontalBean;

import java.util.List;

/**
 * 微信分享信息组件
 * Created by liuweidong on 2016/8/20.
 */
public class ShareInfoView extends LinearLayout {
    private Context context;
    private View rootView;
    private TextView txtShareInfo;
    private ListViewForScrollView listView;
    private TextView txtHeadName;
    private TextView txtHeadValue;
    private TextView txtShareWarnInfo;
    private TextView txtShareSubscribe;
    private ImageView img;
    private Button btnShare;
    private LinearLayout layoutShareImg;
    private ListHorizontalAdapter adapter;
    private ClickListener listener;

    public ShareInfoView(Context context) {
        this(context, null);
    }

    public ShareInfoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShareInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
        initData();
        setListener();
    }

    private void initView() {
        rootView = View.inflate(context, R.layout.boc_fragment_share_info, this);
        txtShareInfo = (TextView) rootView.findViewById(R.id.txt_share_info);
        listView = (ListViewForScrollView) rootView.findViewById(R.id.listview);
        img = (ImageView) rootView.findViewById(R.id.img);
        //ListView添加头部金额信息
        View headView = View.inflate(context, R.layout.boc_fragment_list_horizontal_item, null);
        listView.addHeaderView(headView);
        txtHeadName = (TextView) headView.findViewById(R.id.txt_key);
        txtHeadValue = (TextView) headView.findViewById(R.id.txt_value);
        txtShareWarnInfo = (TextView) rootView.findViewById(R.id.txt_share_warn_info);
        txtShareSubscribe = (TextView) rootView.findViewById(R.id.txt_share_subscribe);
        btnShare = (Button) rootView.findViewById(R.id.btn_share);
        layoutShareImg = (LinearLayout) rootView.findViewById(R.id.layout_share_img);
    }

    private void initData() {
        adapter = new ListHorizontalAdapter(context);
        listView.setAdapter(adapter);
    }

    private void setListener() {
        btnShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.shareWechat();
            }
        });
    }

    public void setTxtShareInfo(String value) {
        txtShareInfo.setText(value);
    }

    /**
     * 设置ListView数据
     *
     * @param list
     */
    public void setListViewData(List<ListHorizontalBean> list) {
        adapter.setData(list);
        adapter.notifyDataSetChanged();
    }

    /**
     * 设置金额信息
     *
     * @param moneyInfo
     */
    public void setMoneyInfo(String[] moneyInfo) {
        txtHeadName.setText(moneyInfo[0]);
        SpannableStringBuilder style = new SpannableStringBuilder(moneyInfo[1]);
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.boc_text_color_red)),
                0, moneyInfo[1].length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new StyleSpan(Typeface.BOLD), 0, moneyInfo[1].length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtHeadValue.setText(style);
    }

    public void setTxtShareWarnInfo(String value) {
        txtShareWarnInfo.setText(value);
    }

    public void setTxtShareSubscribe(String value) {
        txtShareSubscribe.setText(value);
    }

    public void isShowGone(){
        txtShareWarnInfo.setVisibility(View.GONE);
        img.setVisibility(View.GONE);
    }

    public interface ClickListener {
        void shareWechat();
    }

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    public LinearLayout getShareView() {
        return layoutShareImg;
    }
}
