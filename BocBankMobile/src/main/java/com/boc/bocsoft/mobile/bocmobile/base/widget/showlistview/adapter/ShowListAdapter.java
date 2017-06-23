package com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.adapter;


import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.PinnedSectionListView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.ShowListConst;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.bean.ShowListBean;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.ResultStatusUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.TextStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by liuweidong on 2016/10/10.
 */
public class ShowListAdapter extends ArrayAdapter implements PinnedSectionListView.PinnedSectionListAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<ShowListBean> mList = new ArrayList<>();// 数据集合
    private PinnedSectionListView.ClickListener listener;
    private boolean positionType = false;

    public ShowListAdapter(Context context, int resource) {
        super(context, resource);
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }

    /**
     * 设置数据
     *
     * @param list
     */
    public void setData(List<ShowListBean> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        switch (type) {
            case ShowListBean.GROUP:
                if (convertView == null)
                    convertView = inflater.inflate(R.layout.boc_item_list_group, null);
                TextView txtTitle = (TextView) convertView.findViewById(R.id.txt_title);
                txtTitle.setText(mList.get(position).getGroupName());
                break;
            case ShowListBean.CHILD:
                ViewHolderContent holderContent = null;
                if (convertView == null) {
                    holderContent = new ViewHolderContent();
                    switch (mList.get(position).getTitleID()) {
                        case ShowListConst.TITLE_LEFT_2_RIGHT_1:
                        case ShowListConst.TITLE_MOBILE_TYPE:// 汇出查询
                        case ShowListConst.TITLE_LEFT_1_RIGHT_2:
                            convertView = inflater.inflate(R.layout.boc_item_list_child_atm, null);
                            holderContent.rlContent = (RelativeLayout) convertView.findViewById(R.id.layout_content);
                            holderContent.rlRight = (RelativeLayout) convertView.findViewById(R.id.rl_right);
                            holderContent.txtDay = (TextView) convertView.findViewById(R.id.txt_day);
                            holderContent.txtWeek = (TextView) convertView.findViewById(R.id.txt_week);
                            holderContent.txtContentLeftAbove = (TextView) convertView.findViewById(R.id.txt_content_left_above);
                            holderContent.txtContentLeftBelow = (TextView) convertView.findViewById(R.id.txt_content_left_below);
                            holderContent.txtContentRightAbove = (TextView) convertView.findViewById(R.id.txt_content_right_above);
                            holderContent.txtContentRightBelow = (TextView) convertView.findViewById(R.id.txt_content_right_below);
                            holderContent.llDivideLine = (LinearLayout) convertView.findViewById(R.id.ll_divide_line);
                            break;
                        case ShowListConst.TITLE_LONG_SHORT_FOEX:
                            convertView = inflater.inflate(R.layout.boc_item_list_long_short_forex, null);
                            /*设置左右背景色*/
                            holderContent.rlContent = (RelativeLayout) convertView.findViewById(R.id.layout_content);
                            holderContent.llRight = (LinearLayout) convertView.findViewById(R.id.ll_right);
                            /*内容区域的显示*/
                            holderContent.rlCenter = (RelativeLayout) convertView.findViewById(R.id.rl_content_center);
                            holderContent.rlBottom = (RelativeLayout) convertView.findViewById(R.id.rl_content_bottom);
                            holderContent.txtDay = (TextView) convertView.findViewById(R.id.txt_day);
                            holderContent.txtWeek = (TextView) convertView.findViewById(R.id.txt_week);
                            holderContent.txtContentLeftAbove = (TextView) convertView.findViewById(R.id.txt_content_left_above);
                            holderContent.txtContentLeftBelow = (TextView) convertView.findViewById(R.id.txt_content_left_center);
                            holderContent.txtContentLeftBelowAgain = (TextView) convertView.findViewById(R.id.txt_content_left_bottom);
                            holderContent.txtContentRightAbove = (TextView) convertView.findViewById(R.id.txt_content_right_center);
                            holderContent.txtContentRightBelow = (TextView) convertView.findViewById(R.id.txt_content_right_bottom);
                            holderContent.llDivideLine = (LinearLayout) convertView.findViewById(R.id.ll_divide_line);
                            break;

                        case  ShowListConst.TITLE_CRCD_BILL:
                            convertView = inflater.inflate(R.layout.boc_item_list_child_crcd_bill, null);
                            /*设置左右背景色*/
                            holderContent.rlContent = (RelativeLayout) convertView.findViewById(R.id.layout_content);
                            holderContent.llRight = (LinearLayout) convertView.findViewById(R.id.ll_right);
                            /*内容区域的显示*/
                            holderContent.rlCenter = (RelativeLayout) convertView.findViewById(R.id.rl_content_center);
                            holderContent.txtDay = (TextView) convertView.findViewById(R.id.txt_day);
                            holderContent.txtWeek = (TextView) convertView.findViewById(R.id.txt_week);
                            holderContent.txtContentLeftAbove = (TextView) convertView.findViewById(R.id.txt_content_left_above);
                            holderContent.txtContentLeftBelow = (TextView) convertView.findViewById(R.id.txt_content_left_below);
                            holderContent.txtContentRightAbove = (TextView) convertView.findViewById(R.id.txt_content_right_above);
                            holderContent.txtContentRightBelow = (TextView) convertView.findViewById(R.id.txt_content_right_below);
                            holderContent.llDivideLine = (LinearLayout) convertView.findViewById(R.id.ll_divide_line);
                            break;
                        default:
                            convertView = inflater.inflate(R.layout.boc_item_list_child, null);
                            /*设置左右背景色*/
                            holderContent.rlContent = (RelativeLayout) convertView.findViewById(R.id.layout_content);
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
                            holderContent.llDivideLine = (LinearLayout) convertView.findViewById(R.id.ll_divide_line);
                            break;
                    }
                    convertView.setTag(holderContent);
                } else {
                    holderContent = (ViewHolderContent) convertView.getTag();
                }

                final int index = getIndex(position);
                holderContent.rlContent.setOnClickListener(new View.OnClickListener() {// 内容区域监听
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            if(positionType){
                                listener.onItemClickListener(position);
                            }else{
                                listener.onItemClickListener(index);
                            }
                        }
                    }
                });

                LocalDate localDate = mList.get(position).getTime();// 时间（年月日）
                if (localDate != null) {
                    String week = localDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.CHINA);
                    week = week.replaceAll("星期", "周");
                    if (position > 0) {
                        int typeName = mList.get(position - 1).type;
                        if (typeName == ShowListBean.GROUP) {
                            holderContent.txtDay.setText(localDate.toString().substring(8, localDate.toString().length()));
                            holderContent.txtWeek.setText(week);
                        } else {
                            String tempDate = mList.get(position - 1).getTime().toString();
                            if (tempDate.equals(localDate.toString())) {// 判断日和星期是否显示
                                holderContent.txtDay.setText("");
                                holderContent.txtWeek.setText("");
                            } else {
                                holderContent.txtDay.setText(localDate.toString().substring(8, localDate.toString().length()));
                                holderContent.txtWeek.setText(week);
                            }
                        }
                    }
                }

                if (position > 0 && position < mList.size() - 1) {
                    if (mList.get(position + 1).type == ShowListBean.GROUP) {
                        holderContent.llDivideLine.setVisibility(View.GONE);
                    } else {
                        holderContent.llDivideLine.setVisibility(View.VISIBLE);
                    }
                }

                displayView(position, holderContent);
                break;
            default:
                break;
        }
        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return ShowListBean.TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).type;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        boolean type = false;
        switch (viewType) {
            case ShowListBean.GROUP:
                type = true;
                break;
            case ShowListBean.CHILD:
                type = false;
                break;
            default:
                type = false;
                break;
        }
        return type;
    }

    /**
     * 展示需要显示的view
     */
    private void displayView(int position, ViewHolderContent holderContent) {
        ShowListBean item = mList.get(position);
        switch (item.getTitleID()) {
            case ShowListConst.TITLE_DATE_TYPE:
                holderContent.rlCenter.setVisibility(View.GONE);// 隐藏中间部分
                holderContent.txtContentRightAbove.setTextAppearance(mContext, R.style.BocTextViewMoney);
                holderContent.txtContentLeftAbove.setText(item.getContentLeftAbove());
                holderContent.txtContentRightAbove.setText(item.getContentRightAbove());
                if (item.isChangeColor()) {// 更改金额颜色
                    holderContent.txtContentRightAbove.setTextColor(PublicUtils.changeTextColor(mContext, item.getContentRightAbove()));
                }
                break;
            case ShowListConst.TITLE_DETAIL_TYPE:
                /*view显示*/
                holderContent.txtContentLeftAbove.setText(item.getContentLeftAbove());
                holderContent.txtContentRightAbove.setText(item.getContentRightAbove());
                holderContent.txtContentRightBelow.setText(item.getContentRightBelow());
                break;
            case ShowListConst.TITLE_SEND_TYPE:
                holderContent.txtContentLeftBelowAgain.setVisibility(View.VISIBLE);
                holderContent.txtContentRightAbove.setVisibility(View.GONE);
                /*左右背景*/
                holderContent.rlContent.setBackgroundResource(R.color.boc_item_bg_color);
                holderContent.llRight.setBackgroundResource(R.color.boc_common_cell_color);
                holderContent.txtContentLeftBelow.setTextColor(mContext.getResources().getColor(R.color.boc_text_mobile_color));
                holderContent.txtContentLeftBelow.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                /*view显示*/
                holderContent.txtContentLeftAbove.setText(item.getContentLeftAbove());
                holderContent.txtContentLeftBelow.setText(item.getContentLeftBelow());
                holderContent.txtContentLeftBelowAgain.setText(item.getContentLeftBelowAgain());
                holderContent.txtContentRightBelow.setText(item.getContentRightBelow());
                holderContent.txtContentLeftBelowAgain.setTextColor(ResultStatusUtils.changeTextBackground(mContext, item.getContentLeftBelowAgain()));
                break;
            case ShowListConst.TITLE_BILL_TYPE:
                if (StringUtils.isEmptyOrNull(item.getContentLeftBelow())) {
                    holderContent.txtContentLeftBelow.setVisibility(View.GONE);
                } else {
                    holderContent.txtContentLeftBelow.setVisibility(View.VISIBLE);
                }
                /*左右背景*/
                holderContent.rlContent.setBackgroundResource(R.color.boc_item_bg_color);
                holderContent.llRight.setBackgroundResource(R.color.boc_common_cell_color);
                holderContent.txtContentLeftBelow.setTextColor(mContext.getResources().getColor(R.color.boc_text_color_money_count));
                /*view显示*/
                holderContent.txtContentLeftAbove.setText(item.getContentLeftAbove());
                holderContent.txtContentLeftBelow.setText(item.getContentLeftBelow());
                holderContent.txtContentRightAbove.setText(item.getContentRightAbove());
                if (item.getContentRightBelow().contains("-")) {
                    String temp = item.getContentRightBelow().trim().replace("-", "");
                    holderContent.txtContentRightBelow.setText(temp);
                } else {
                    holderContent.txtContentRightBelow.setText(item.getContentRightBelow());
                }
                if (item.isChangeColor()) {// 更改金额颜色
                    holderContent.txtContentRightBelow.setTextColor(PublicUtils.changeTextColor(mContext, item.getContentRightBelow()));
                }
                break;
            case ShowListConst.TITLE_MOBILE_TYPE:
                /*左右背景*/
                holderContent.rlContent.setBackgroundResource(R.color.boc_item_bg_color);
                holderContent.rlRight.setBackgroundResource(R.color.boc_common_cell_color);
                /*属性设置*/
                holderContent.txtContentLeftBelow.setTextColor(mContext.getResources().getColor(R.color.boc_text_mobile_color));
                holderContent.txtContentLeftBelow.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                /*view显示*/
                holderContent.txtContentLeftAbove.setText(item.getContentLeftAbove());
                holderContent.txtContentLeftBelow.setText(item.getContentLeftBelow());
                holderContent.txtContentRightAbove.setText(item.getContentRightAbove());
                break;
            case ShowListConst.TITLE_LEFT_2_RIGHT_1:
                holderContent.txtContentLeftBelow.setTextColor(ResultStatusUtils.changeTextBackground(mContext, item.getContentLeftBelow()));
                /*view显示*/
                holderContent.txtContentLeftAbove.setText(item.getContentLeftAbove());
                holderContent.txtContentLeftBelow.setText(item.getContentLeftBelow());
                holderContent.txtContentRightAbove.setText(item.getContentRightAbove());
                break;
            case ShowListConst.TITLE_LEFT_1_RIGHT_2:
                holderContent.txtContentLeftBelow.setVisibility(View.GONE);
                holderContent.txtContentRightBelow.setVisibility(View.VISIBLE);
                /*左右背景*/
                holderContent.rlContent.setBackgroundResource(R.color.boc_item_bg_color);
                holderContent.rlRight.setBackgroundResource(R.color.boc_common_cell_color);
                /*view显示*/
                holderContent.txtContentLeftAbove.setText(item.getContentLeftAbove());
                holderContent.txtContentRightBelow.setText(item.getContentRightBelow());
                holderContent.txtContentRightAbove.setText(item.getContentRightAbove());
                break;
            case ShowListConst.TITLE_ORDER_MANAGE:
                /*左右背景*/
                holderContent.rlContent.setBackgroundResource(R.color.boc_item_bg_color);
                holderContent.llRight.setBackgroundResource(R.color.boc_common_cell_color);
                /*属性设置*/
                holderContent.txtContentLeftBelow.setTextColor(ResultStatusUtils.changeTextBackground(mContext, item.getContentLeftBelow()));
                holderContent.txtContentLeftBelow.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
//                holderContent.txtContentLeftBelow.setTextAppearance(mContext, R.style.BocTextViewState);
                /*view显示*/
                holderContent.txtContentLeftAbove.setText(item.getContentLeftAbove());
                holderContent.txtContentLeftBelow.setText(item.getContentLeftBelow());
                holderContent.txtContentRightBelow.setText(item.getContentRightBelow());
                holderContent.txtContentRightAbove.setText(item.getContentRightAbove());
                break;
            case ShowListConst.TITLE_WEALTH:
                /*左右背景*/
                holderContent.rlContent.setBackgroundResource(R.color.boc_item_bg_color);
                holderContent.llRight.setBackgroundResource(R.color.boc_common_cell_color);
                /*属性设置*/
                holderContent.txtContentRightAbove.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
                holderContent.txtContentRightAbove.setTextColor(ResultStatusUtils.changeTextColor(mContext, item.getContentRightAbove()));
                setDimenColor(holderContent.txtContentLeftBelow, 13, mContext.getResources().getColor(R.color.boc_text_color_common_gray));
                /*view显示*/
                holderContent.txtContentLeftAbove.setText(item.getContentLeftAbove());
                holderContent.txtContentLeftBelow.setText(item.getContentLeftBelow());
                holderContent.txtContentRightAbove.setText(item.getContentRightAbove());
                holderContent.txtContentRightBelow.setText(item.getContentRightBelow());
                break;
            case ShowListConst.TITLE_QRPAY_TYPE:
                // 二维码支付-支付记录查询 added by wangf on 2016-11-7 19:55:40
                /*左右背景*/
                holderContent.rlContent.setBackgroundResource(R.color.boc_item_bg_color);
                holderContent.llRight.setBackgroundResource(R.color.boc_common_cell_color);
                /*view显示*/
                holderContent.txtContentLeftAbove.setText(item.getContentLeftAbove());
                holderContent.txtContentLeftBelow.setText(item.getContentLeftBelow());
                holderContent.txtContentRightAbove.setText(item.getContentRightAbove());
                holderContent.txtContentRightBelow.setText(item.getContentRightBelow());
                holderContent.txtContentRightAbove.setTextColor(ResultStatusUtils.changeQrpayTextColor(mContext, item.getContentRightAbove()));
                if (item.isChangeColor()) {// 更改金额颜色
                    holderContent.txtContentRightBelow.setTextColor(PublicUtils.changeTextColor(mContext, item.getContentRightBelow()));
                }
                break;
            case ShowListConst.TITLE_LONG_SHORT_FOEX:
                /*左右背景*/
                holderContent.rlContent.setBackgroundResource(R.color.boc_item_bg_color);
                holderContent.llRight.setBackgroundResource(R.color.boc_common_cell_color);
                /*属性设置*/
                holderContent.txtContentRightAbove.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
                holderContent.txtContentRightAbove.setTextColor(ResultStatusUtils.changeTextColor(mContext, item.getContentRightAbove()));
                setDimenColor(holderContent.txtContentLeftBelow, 13, mContext.getResources().getColor(R.color.boc_text_color_common_gray));
                /*view显示*/
                holderContent.txtContentLeftAbove.setText(item.getContentLeftAbove());
                holderContent.txtContentLeftBelow.setText(item.getContentLeftBelow());
                holderContent.txtContentLeftBelowAgain.setText(item.getContentLeftBelowAgain());
                holderContent.txtContentRightAbove.setText(item.getContentRightAbove());
                holderContent.txtContentRightBelow.setText(item.getContentRightBelow());
                break;
            case  ShowListConst.TITLE_LEFT_ABOVE_RIGHT_BELOW:
                holderContent.txtContentRightAbove.setVisibility(View.GONE);
                holderContent.txtContentLeftBelow.setVisibility(View.GONE);
                /*左右背景*/
                holderContent.rlContent.setBackgroundResource(R.color.boc_item_bg_color);
                holderContent.llRight.setBackgroundResource(R.color.boc_common_cell_color);

                holderContent.txtContentLeftAbove.setText(item.getContentLeftAbove());
                holderContent.txtContentRightBelow.setText(item.getContentRightBelow());
                if(item.getContentRightBelow().contains("存入")){
                    holderContent.txtContentRightBelow.setTextColor(getContext().getResources().getColor(R.color.boc_text_color_green));
                } else {
                    holderContent.txtContentRightBelow.setTextColor(getContext().getResources().getColor(R.color.boc_text_color_dark_gray));
                }
                break;
            case  ShowListConst.TITLE_CRCD_BILL:
                holderContent.txtContentRightAbove.setVisibility(View.INVISIBLE);
                if (item.isChangeColor()){
                    holderContent.txtContentLeftBelow.setText(item.getContentLeftBelow());
                    holderContent.txtContentLeftBelow.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
                    holderContent.txtContentLeftBelow.setTextColor(getContext().getResources().getColor(R.color.boc_text_color_green));
                    holderContent.txtContentRightBelow.setTextColor(getContext().getResources().getColor(R.color.boc_text_color_green));
                }else{
                    holderContent.txtContentRightBelow.setTextColor(getContext().getResources().getColor(R.color.boc_text_color_dark_gray));
                    holderContent.txtContentLeftBelow.setVisibility(View.INVISIBLE);
                }
                /*左右背景*/
                holderContent.rlContent.setBackgroundResource(R.color.boc_item_bg_color);
                holderContent.llRight.setBackgroundResource(R.color.boc_common_cell_color);
                holderContent.txtContentLeftAbove.setText(item.getContentLeftAbove());
                holderContent.txtContentRightBelow.setText(item.getContentRightBelow());
                break;
            default:
                break;
        }
    }

    private class ViewHolderContent {
        private RelativeLayout rlContent;
        private LinearLayout llRight;
        private RelativeLayout rlRight;
        private RelativeLayout rlCenter;
        private RelativeLayout rlBottom;
        private TextView txtDay;// 日
        private TextView txtWeek;// 星期
        private TextView txtContentLeftAbove;// 左上内容
        private TextView txtContentLeftBelow;// 左下内容
        private TextView txtContentLeftBelowAgain;// 左下下内容
        private TextView txtContentRightAbove;// 右上内容
        private TextView txtContentRightBelow;// 右下内容
        private LinearLayout llDivideLine;
    }

    /**
     * 设置监听事件
     *
     * @param listener
     */
    public void setOnClickListener(PinnedSectionListView.ClickListener listener) {
        this.listener = listener;
    }

    private void setDimenColor(TextView textView, float dimen, int color) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dimen);// 设置字体大小
        textView.setTextColor(color);
    }

    public void setPositionType(boolean positionType) {
        this.positionType = positionType;
    }

    /**
     * 去除所有分组占用的下标
     *
     * @param position
     * @return
     */
    private int getIndex(int position) {
        int sum = 0;
        for (int i = 0; i < position; i++) {
            if (mList.get(i).type == ShowListBean.GROUP) {
                ++sum;
            }
        }
        return position - sum;
    }

}
