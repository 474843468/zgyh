package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * 作者：xwg on 16/11/17 18:01
 * 立即还款单选条
 */
public class SelectTabRow extends LinearLayout implements View.OnClickListener {

    private Context mContext;
    private LinearLayout rootView;
    private ImageView ivCheck;
    private TextView tvContent;
    private TextView tvName;
    /**
    *  钞汇标识  默认为现汇
    */
    private String cashRemit="02";

    /**
     * 是否可选
     */
    private boolean checkable=true;
    /**
     * 选中状态
     */
    private boolean checked=false;

    /**
     *  单选框背景图片，包含 未选中、选中、不可选 三种状态
     */
    private int ivBackgrouds[]=new int[]{R.drawable.tabrow_unselected,R.drawable.tabrow_selected};

    private View divider_line;
    private LinearLayout ll_content,ll_cashRemitBox;
    private LinearLayout ll_remitBox,ll_cashBox;
    private TextView tvNotice;


    public SelectTabRow(Context context) {
        this(context,null);
    }

    public SelectTabRow(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SelectTabRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
        initView();
    }

    private void initView() {
        rootView=(LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.view_select_tabrow,this);

        ll_content=(LinearLayout)rootView.findViewById(R.id.ll_content);
        ll_cashRemitBox=(LinearLayout)rootView.findViewById(R.id.ll_cashRemitBox);

        ivCheck=(ImageView)rootView.findViewById(R.id.ivCheck);
        tvName=(TextView)findViewById(R.id.tvTabName);
        tvNotice=(TextView)findViewById(R.id.tv_notice);
        tvContent=(TextView)findViewById(R.id.tvTableContent);

        ll_cashBox=(LinearLayout)rootView.findViewById(R.id.cash_box);
        ll_remitBox=(LinearLayout)rootView.findViewById(R.id.remit_box);

        divider_line=findViewById(R.id.divider_line);
        ivCheck.setImageResource(ivBackgrouds[0]);

        tvName.setTextColor(getResources().getColor(R.color.boc_text_color_dark_gray));
        tvContent.setTextColor(getResources().getColor(R.color.boc_text_color_gray));

        setListener();
    }

    private void setListener() {
        ll_cashBox.setOnClickListener(this);
        ll_remitBox.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.cash_box){
            ll_cashBox.setBackgroundResource(R.drawable.boc_textview_bg_light);
            ll_remitBox.setBackgroundResource(R.drawable.boc_textview_bg_default);
            ImageView remit_image= (ImageView) rootView.findViewById(R.id.remit_image);
            ImageView cash_image= (ImageView) rootView.findViewById(R.id.cash_image);
            cash_image.setVisibility(VISIBLE);
            remit_image.setVisibility(GONE);

            TextView tvCash=(TextView)rootView.findViewById(R.id.cash_text);
            TextView tvRemit=(TextView)rootView.findViewById(R.id.remit_text);
            tvCash.setTextColor(getResources().getColor(R.color.boc_text_color_red));
            tvRemit.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));

            cashRemit="01";

            if (cashRemitClickListener!=null)
                cashRemitClickListener.onCashRemitBoxClicked(v,cashRemit);
        }else if (v.getId()==R.id.remit_box){
             ll_remitBox.setBackgroundResource(R.drawable.boc_textview_bg_light);
            ll_cashBox.setBackgroundResource(R.drawable.boc_textview_bg_default);
            ImageView remit_image= (ImageView) rootView.findViewById(R.id.remit_image);
            ImageView cash_image= (ImageView) rootView.findViewById(R.id.cash_image);
            cash_image.setVisibility(GONE);
            remit_image.setVisibility(VISIBLE);

            TextView tvCash=(TextView)rootView.findViewById(R.id.cash_text);
            TextView tvRemit=(TextView)rootView.findViewById(R.id.remit_text);
            tvRemit.setTextColor(getResources().getColor(R.color.boc_text_color_red));
            tvCash.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));

            cashRemit="02";

            if (cashRemitClickListener!=null)
                cashRemitClickListener.onCashRemitBoxClicked(v,cashRemit);
        }
    }

    public String getCashRemit(){
        return cashRemit;
    }

    public void setTabHeight(int height){
        ll_content.getLayoutParams().height=height;
    }

    public void setContent(String name){
        setContent(name,"");
    }
    /**
     * 设置tab 大字文本和小字文本内容
     */
    public void setContent(String name, String content){
        tvName.setText(name);
        tvContent.setText(content);
    }
    /**
     * 设置tab是否可选
     * 默认为可选择
     */
    public void setTableCheckable(boolean checkable){
        this.checkable=checkable;
        if (checkable){
            tvNotice.setVisibility(GONE);
            tvName.setTextColor(getResources().getColor(R.color.boc_text_color_dark_gray));
            if (checked)
                ivCheck.setImageResource(ivBackgrouds[1]);
            else
                ivCheck.setImageResource(ivBackgrouds[0]);
        }else{
            tvName.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
            ivCheck.setImageResource(ivBackgrouds[0]);
            tvNotice.setVisibility(VISIBLE);
        }
    }
    /**
     * 设置tab选中状态,默认不选中
     */
    public  void setTabChecked(boolean checked){
        if (!checkable)
            return;
        if (checked){
            ivCheck.setImageResource(ivBackgrouds[1]);
        }else{
            ivCheck.setImageResource(ivBackgrouds[0]);
        }
        this.checked=checked;
        invalidate();
    }

    public boolean isChecked(){
        return checkable&&checked;
    }
    public boolean isCheckable(){
        return checkable;
    }


    /**
     *  是否显示下划线
     *  默认为显示
     */
    public void showUnderLine(boolean show){
        if (show){
            divider_line.setVisibility(VISIBLE);
        }else {
            divider_line.setVisibility(GONE);
        }
    }


    public String getTabContentText(){
        return tvContent.getText().toString();
    }

    /**
     * 设置钞汇选择标识选择框显示和隐藏
     */
    public void setCashRemitBoxDisplay(boolean isShow){
        if (isShow)
            ll_cashRemitBox.setVisibility(VISIBLE);
        else
            ll_cashRemitBox.setVisibility(GONE);
    }


    public interface CashRemitClickListener{
        void onCashRemitBoxClicked(View view,String cashRemit);
    }
    private CashRemitClickListener cashRemitClickListener;

    public void setCashRemitBoxListener(CashRemitClickListener listener){
        this.cashRemitClickListener=listener;
    }

}
