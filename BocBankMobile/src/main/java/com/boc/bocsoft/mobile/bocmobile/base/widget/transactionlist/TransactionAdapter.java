package com.boc.bocsoft.mobile.bocmobile.base.widget.transactionlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.ResultStatusUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.TextStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 查询列表公共组件适配器
 * Created by liuweidong on 2016/6/1.
 */
public class TransactionAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<TransactionBean> list;// 交易列表集合
    private TransactionView.ClickListener listener;

    public TransactionAdapter(Context context) {
        this.mContext = context;
        list = new ArrayList<TransactionBean>();
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<TransactionBean> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolderContent holderContent = null;
        if (convertView == null) {
            holderContent = new ViewHolderContent();
            switch (list.get(position).getTitleID()) {
                case TransactionView.TITLE_BILL_TYPE:
                case TransactionView.TITLE_DATE_TYPE1:
                case TransactionView.TITLE_ATM_DRAW_TYPE:
                    convertView = inflater.inflate(R.layout.boc_item_transaction_content_1, null);
                    holderContent.layoutTitle = (LinearLayout) convertView.findViewById(R.id.layout_title);
                    holderContent.txtTitle = (TextView) convertView.findViewById(R.id.txt_title);
                    /*设置左右背景色*/
                    holderContent.layoutContent = (RelativeLayout) convertView.findViewById(R.id.layout_content);
                    holderContent.rlRight = (RelativeLayout) convertView.findViewById(R.id.rl_right);
                    /*内容区域的显示*/
                    holderContent.txtDay = (TextView) convertView.findViewById(R.id.txt_day);
                    holderContent.txtWeek = (TextView) convertView.findViewById(R.id.txt_week);
                    holderContent.txtContentLeftAbove = (TextView) convertView.findViewById(R.id.txt_content_left_above);
                    holderContent.txtContentLeftBelow = (TextView) convertView.findViewById(R.id.txt_content_left_below);
                    holderContent.txtContentRightAbove = (TextView) convertView.findViewById(R.id.txt_content_right_above);
                    holderContent.txtContentRightBelow = (TextView) convertView.findViewById(R.id.txt_content_right_below);
                    break;
                default:
                    convertView = inflater.inflate(R.layout.boc_item_transaction_content, null);
                    holderContent.layoutTitle = (LinearLayout) convertView.findViewById(R.id.layout_title);
                    holderContent.txtTitle = (TextView) convertView.findViewById(R.id.txt_title);
                    /*设置左右背景色*/
                    holderContent.layoutContent = (RelativeLayout) convertView.findViewById(R.id.layout_content);
                    holderContent.llRight = (LinearLayout) convertView.findViewById(R.id.ll_right);
                    /*内容区域的显示*/
                    holderContent.rlCenter = (RelativeLayout) convertView.findViewById(R.id.rl_content_center);
                    holderContent.txtDay = (TextView) convertView.findViewById(R.id.txt_day);
                    holderContent.txtWeek = (TextView) convertView.findViewById(R.id.txt_week);
                    holderContent.txtContentLeftAbove = (TextView) convertView.findViewById(R.id.txt_content_left_above);
                    holderContent.txtContentLeftBelow = (TextView) convertView.findViewById(R.id.txt_content_left_below);
                    holderContent.txtContentLeftBelowAgain = (TextView) convertView.findViewById(R.id.txt_content_left_below_again);
                    holderContent.txtContentRightAbove = (TextView) convertView.findViewById(R.id.txt_content_right_above);
                    holderContent.txtContentRightBelow = (TextView) convertView.findViewById(R.id.txt_content_right_below);
                    break;
            }
            convertView.setTag(holderContent);
        } else {
            holderContent = (ViewHolderContent) convertView.getTag();
        }

        holderContent.layoutContent.setOnClickListener(new View.OnClickListener() {// 监听
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClickListener(position);
                }
            }
        });

        LocalDate localDate = list.get(position).getTime();// 时间（年月日）
        // MM月/yyyy
        String formatTime = "";
        if (localDate != null) {
            formatTime = localDate.format(DateFormatters.monthFormatter1);
        }
        String tempTime = "";
        if (position > 0) {
            tempTime = list.get(position - 1).getTime().format(DateFormatters.monthFormatter1);
        }
        if (tempTime.equals(formatTime)) {// 设置内容
            holderContent.layoutTitle.setVisibility(View.GONE);
        } else {// 设置标题及内容
            holderContent.layoutTitle.setVisibility(View.VISIBLE);
            holderContent.txtTitle.setText(formatTime);
        }

        String tempDate = "";
        if (position > 0) {
            tempDate = list.get(position - 1).getTime().toString();
        }

        if (tempDate.equals(localDate.toString())) {// 判断日和星期是否显示
            holderContent.txtDay.setText("");
            holderContent.txtWeek.setText("");
        } else {
            holderContent.txtDay.setText(localDate.toString().substring(8, localDate.toString().length()));
            String week = localDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.CHINA);
            week = week.replaceAll("星期", "周");
            holderContent.txtWeek.setText(week);
        }
        displayView(position, holderContent);
        return convertView;
    }

    /**
     * 展示需要显示的view
     */
    private void displayView(int position, ViewHolderContent holderContent) {
        switch (list.get(position).getTitleID()) {
            case TransactionView.TITLE_DATE_TYPE:
            case TransactionView.TITLE_BILL_TYPE1:
                holderContent.rlCenter.setVisibility(View.GONE);// 隐藏中间部分
                holderContent.txtContentRightAbove.setTextAppearance(mContext, R.style.BocTextViewMoney);
                if (list.get(position).isChangeColor()) {// 更改金额颜色
                    int color = PublicUtils.changeTextColor(mContext, list.get(position).getContentRightAbove());
                    holderContent.txtContentRightAbove.setTextColor(color);
                }

                String temp = list.get(position).getContentRightAbove();

                // 虚拟卡去掉金额符号
                if (TransactionView.TITLE_BILL_TYPE1 == list.get(position).getTitleID())
                    temp = list.get(position).getContentRightAbove().trim().replace("-", "");

                holderContent.txtContentLeftAbove.setText(list.get(position).getContentLeftAbove());
                holderContent.txtContentRightAbove.setText(temp);
                break;
            case TransactionView.TITLE_BILL_TYPE:
            case TransactionView.TITLE_DATE_TYPE1:
                // 设置背景色（左低右高）
                holderContent.layoutContent.setBackgroundResource(R.color.boc_item_bg_color);
                holderContent.rlRight.setBackgroundResource(R.color.boc_common_cell_color);
                if (StringUtils.isEmptyOrNull(list.get(position).getContentLeftBelow())) {// 左下数据
                    holderContent.txtContentLeftBelow.setVisibility(View.GONE);
                } else {
                    holderContent.txtContentLeftBelow.setVisibility(View.VISIBLE);
                    holderContent.txtContentLeftBelow.setTextColor(mContext.getResources().getColor(R.color.boc_text_color_money_count));
                    holderContent.txtContentLeftBelow.setText(list.get(position).getContentLeftBelow());
                }
                holderContent.txtContentRightBelow.setVisibility(View.VISIBLE);

                // 设置页面数据
                if (list.get(position).getContentLeftAbove() != null) {
                    holderContent.txtContentLeftAbove.setText(list.get(position).getContentLeftAbove());
                }

                holderContent.txtContentRightBelow.setText("");
                holderContent.txtContentRightBelow.setText(list.get(position).getContentRightBelow());// 币种

                temp = list.get(position).getContentRightAbove();

                // 虚拟卡去掉金额符号
                if (TransactionView.TITLE_BILL_TYPE == list.get(position).getTitleID())
                    temp = list.get(position).getContentRightAbove().trim().replace("-", "");
                holderContent.txtContentRightAbove.setText(temp);

                if (list.get(position).isChangeColor()) {// 更改金额颜色
                    holderContent.txtContentRightAbove.setTextColor(PublicUtils.changeTextColor(mContext, list.get(position).getContentRightAbove()));
                }
                break;
            case TransactionView.TITLE_ATM_DRAW_TYPE:
                holderContent.txtContentLeftBelow.setTextColor(ResultStatusUtils.changeTextBackground(mContext, list.get(position).getContentLeftBelowAgain()));
                // 设置页面数据
                holderContent.txtContentLeftAbove.setText(list.get(position).getContentLeftAbove());
                holderContent.txtContentLeftBelow.setText(list.get(position).getContentLeftBelowAgain());
                holderContent.txtContentRightAbove.setText(list.get(position).getContentRightBelow());
                break;
            default:
                break;
        }
    }

    private class ViewHolderContent {
        private LinearLayout layoutTitle;// 标题区容器
        private RelativeLayout layoutContent;// 内容区容器
        private RelativeLayout rlRight;
        private LinearLayout llRight;
        private RelativeLayout rlCenter;

        private TextView txtTitle;// 标题
        private TextView txtDay;// 日
        private TextView txtWeek;// 星期

        private TextView txtContentLeftAbove;// 左上内容
        private TextView txtContentLeftBelow;// 左下内容
        private TextView txtContentLeftBelowAgain;// 左下下内容
        private TextView txtContentRightAbove;// 右上内容
        private TextView txtContentRightBelow;// 右下内容
    }

    /**
     * 设置监听事件
     *
     * @param listener
     */
    public void setOnClickListener(TransactionView.ClickListener listener) {
        this.listener = listener;
    }
}
