package com.boc.bocsoft.mobile.bocmobile.base.widget.selectmanagementview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;

/**
 * Created by liuyang on 2016/7/5.
 */
public class SelectManageViewItem extends LinearLayout {

    protected View rootView;
    protected ImageView cardPic;
    protected TextView cardName;
    protected TextView cardNumber;
    protected LinearLayout bocCard;
    private Context context;
    private SelectMangageViewAdapter.ItemClickListener mListener;//被点击的监听
    private AccountBean itemPack;//数据
    private int position;//点击位置


    public SelectManageViewItem(Context context) {
        super(context);
        this.context = context;
        initView();
    }


    public SelectManageViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public SelectManageViewItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SelectManageViewItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        initView();
    }

    private void initView() {
        rootView = LayoutInflater.from(context).inflate(R.layout.boc_fragment_payee_item, this);
        cardPic = (ImageView) rootView.findViewById(R.id.card_pic);
        cardName = (TextView) rootView.findViewById(R.id.card_name);
        cardNumber = (TextView) rootView.findViewById(R.id.card_number);
        bocCard = (LinearLayout) rootView.findViewById(R.id.boc_card);
        rootView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                mListener.onItemClick(position, itemPack);
            }
        });
    }

    public void setCardPic(Drawable drawable) {
        cardPic.setImageDrawable(drawable);
    }

    public void setCardName(String name) {
        cardName.setText(name);
    }

    public void setCardNumber(String number) {
        cardNumber.setText(number);
    }

    public void updateData(AccountBean datapack, int position) {

        this.position = position;
        itemPack = datapack;
        //调用上面方法，设置item
        setCardNumber(itemPack.getAccountNumber());
        setCardNumber(itemPack.getNickName());

    }

    public void setClickListener(SelectMangageViewAdapter.ItemClickListener ls) {
        this.mListener = ls;
    }


}
