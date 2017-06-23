package com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selecttimerangenew;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.Content;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.SelectGridView;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * 筛选时间范围（包含时间选择按钮）
 * Created by liuweidong on 2016/5/23.
 */
public class SelectTimeRangeNew extends LinearLayout implements View.OnClickListener {
    private Context mContext;
    private View rootView;
    private TextView txtCancel;// 取消
    private TextView txtTitle;// 标题
    private SelectGridView singleSelect;// 日期范围的选择按钮
    private TextView txtStart, txtEnd;// 开始日期与截止日期的文本
    private Button btnLeft, btnRight;// 重置与确认按钮
    private LinearLayout llAddToLayout;// 添加额外布局的父容器
    private LinearLayout llAddToTopLayout;
    private LinearLayout ll_time_layout;
    private ClickListener listener;// 监听
    private ResetClickListener resetListener;

    private List<Content> singleSelectList;// 日期范围的选择数据源
    private List<Content> curSelectStatus = new ArrayList<Content>();// 保存日期选中数据源副本
    private String selectedContent;// 被选中内容
    private String[] defaultData = new String[2];
    private String defaultStartDate;// 默认开始日期
    private String defaultEndDate;// 默认结束日期
    private int dateShowWay = 0;// 日期文本的显示方式
    private int curPosition = 2;
    public static final int START_DATE_BY_CURDATE = 1;// 开始日期为当前日期
    public static final int END_DATE_BY_CURDATE = 2;// 结束日期为当前日期

    public SelectTimeRangeNew(Context context) {
        this(context, null);
    }

    public SelectTimeRangeNew(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectTimeRangeNew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
        initData();
        setListener();
    }

    /**
     * 布局初始化
     */
    private void initView() {
        rootView = View.inflate(mContext, R.layout.boc_select_time_range_new, this);

        txtCancel = (TextView) rootView.findViewById(R.id.txt_cancel);
        txtTitle = (TextView) rootView.findViewById(R.id.txt_title);
        singleSelect = (SelectGridView) rootView.findViewById(R.id.single_select);
        txtStart = (TextView) rootView.findViewById(R.id.txt_start);
        txtEnd = (TextView) rootView.findViewById(R.id.txt_end);
        btnLeft = (Button) rootView.findViewById(R.id.btn_left);
        btnRight = (Button) rootView.findViewById(R.id.btn_right);

        llAddToLayout = (LinearLayout) rootView.findViewById(R.id.ll_add_layout);
        llAddToTopLayout = (LinearLayout) rootView.findViewById(R.id.ll_add_layout_top);
        ll_time_layout = (LinearLayout) rootView.findViewById(R.id.ll_time_layout);
    }

    /**
     * 数据初始化
     */
    private void initData() {
        String[] singleSelectData = {"近1周", "近1月", "近3月"};
        dateShowWay = 0;
        singleSelectList = new ArrayList<Content>();
        for (int i = 0; i < singleSelectData.length; i++) {// 初始化日期范围单选组件数据
            Content item = new Content();
            item.setName(singleSelectData[i]);
            if (i == (singleSelectData.length - 1)) {
                curPosition = 2;
                item.setSelected(true);// 默认勾选近三月
            }
            singleSelectList.add(item);
        }
        singleSelect.setData(singleSelectList);
    }

    /**
     * 设置监听
     */
    private void setListener() {
        txtCancel.setOnClickListener(this);
        txtStart.setOnClickListener(this);
        txtEnd.setOnClickListener(this);
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);

        singleSelect.setListener(new SelectGridView.ClickListener() {
            @Override
            public void setItemClick(AdapterView<?> parent, View view, int position, long id) {
                curPosition = position;
                for (int i = 0; i < singleSelectList.size(); i++) {
                    singleSelectList.get(i).setSelected(false);
                    if (i == position) {
                        singleSelectList.get(position).setSelected(true);
                    }
                }
                if (dateShowWay == START_DATE_BY_CURDATE) {
                    setStartDateByCurdate(position);
                    return;
                }
                setEndDateByCurdate(position);
            }
        });
    }

    /**
     * 设置默认的日期（必须调用）
     *
     * @param startDate
     * @param endDate
     */
    public void setDefaultDate(String startDate, String endDate) {
        defaultData[0] = startDate;
        defaultData[1] = endDate;
        defaultStartDate = startDate;
        defaultEndDate = endDate;
        txtStart.setText(startDate);
        txtEnd.setText(endDate);
        saveCurDateStatus();
    }

    /**
     * 设置默认勾选的时间范围
     */
    public void setDefaultSelect(int location) {
        singleSelectList.get(location).setSelected(true);
        singleSelectList.get(2).setSelected(false);
        singleSelect.getAdapter().notifyDataSetChanged();
        saveCurDateStatus();
    }

    /**
     * 设置默认的日期
     *
     * @param location
     * @param startDate
     * @param endDate
     */
    public void setDefaultDate(int location, String startDate, String endDate) {
        for (int i = 0; i < singleSelectList.size(); i++) {
            singleSelectList.get(i).setSelected(false);
        }
        if (location == 0 || location == 1 || location == 2) {
            curPosition = location;
            singleSelectList.get(location).setSelected(true);
        } else {
            curPosition = -1;
        }
        singleSelect.getAdapter().notifyDataSetChanged();
        txtStart.setText(startDate);
        txtEnd.setText(endDate);
    }

    /**
     * （预约管理调用）
     */
    public void setDefaultDateStatus() {
//        setSelected(2);
        cancelSelected();
        singleSelect.getAdapter().notifyDataSetChanged();
        txtStart.setText(defaultStartDate);
        txtEnd.setText(defaultEndDate);
    }

    /**
     * 设置默认的勾选状态（交易明细调用）
     */
    public void setDefaultSelectStatus() {
        singleSelectList.get(2).setSelected(false);
        singleSelect.setData(singleSelectList);
        saveCurDateStatus();
    }

    /**
     * 每次点击筛选时设置界面的默认数据
     */
    public void setClickSelectDefaultData() {
        for (int j = 0; j < curSelectStatus.size(); j++) {
            singleSelectList.get(j).setSelected(curSelectStatus.get(j).getSelected());
        }
        singleSelect.getAdapter().notifyDataSetChanged();
        txtStart.setText(defaultStartDate);
        txtEnd.setText(defaultEndDate);
    }

    /**
     * 设置开始日期为当前日期
     *
     * @param position
     */
    private void setStartDateByCurdate(int position) {
        LocalDate curDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate();
        txtStart.setText(curDate.format(DateFormatters.dateFormatter1));
        switch (position) {
            case 0:
                txtEnd.setText(curDate.plusWeeks(1).format(DateFormatters.dateFormatter1));
                break;
            case 1:
                txtEnd.setText(curDate.plusMonths(1).format(DateFormatters.dateFormatter1));
                break;
            case 2:
                txtEnd.setText(curDate.plusMonths(3).format(DateFormatters.dateFormatter1));
                break;
        }
    }

    /**
     * 设置结束日期为当前日期
     *
     * @param position
     */
    private void setEndDateByCurdate(int position) {
        LocalDate curDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate();
        txtEnd.setText(curDate.format(DateFormatters.dateFormatter1));
        switch (position) {
            case 0:
                txtStart.setText(curDate.plusWeeks(-1).plusDays(1).format(DateFormatters.dateFormatter1));
                break;
            case 1:
                txtStart.setText(curDate.plusMonths(-1).plusDays(1).format(DateFormatters.dateFormatter1));
                break;
            case 2:
                txtStart.setText(curDate.plusMonths(-3).plusDays(1).format(DateFormatters.dateFormatter1));
                break;
        }
    }

    /**
     * 添加额外布局的父容器
     *
     * @return
     */
    public LinearLayout getAddToLayout() {
        return llAddToLayout;
    }


    public LinearLayout getTimeLayout() {
        return ll_time_layout;
    }

    /**
     * 在头部添加额外布局的父容器
     *
     * @return
     */
    public LinearLayout getAddToTopLayout() {
        return llAddToTopLayout;
    }

    /**
     * 设置标题值
     *
     * @param titleValue
     */
    public void setTitleValue(String titleValue) {
        txtTitle.setText(titleValue);
    }

    /**
     * 设置开始日期
     *
     * @param startDate
     */
    public void setStartDate(String startDate) {
        txtStart.setText(startDate);
    }

    /**
     * 设置截止日期
     *
     * @param endDate
     */
    public void setEndDate(String endDate) {
        txtEnd.setText(endDate);
    }

    /**
     * 查询开始日期
     */
    public String getStartDate() {
        return txtStart.getText().toString();
    }

    /**
     * 查询截止日期
     */
    public String getEndDate() {
        return txtEnd.getText().toString();
    }

    /**
     * 是否显示日期快捷按键
     *
     * @param isVisible
     */
    public void isShowDateBtn(boolean isVisible) {
        if (isVisible) {
            singleSelect.setVisibility(View.VISIBLE);
        } else {
            singleSelect.setVisibility(View.GONE);
        }
    }

    /**
     * 设置日期的显示方式
     *
     * @param dateShowWay
     */
    public void setDateShowWay(int dateShowWay) {
        this.dateShowWay = dateShowWay;
    }

    /**
     * 判断日期选择按钮是否选中
     *
     * @return
     */
    private boolean judgeSelectDateStatus() {
        boolean haveSelected = false;
        for (Content item : singleSelectList) {
            if (item.getSelected()) {
                haveSelected = true;
                selectedContent = item.getName();
                break;
            }
        }
        return haveSelected;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.txt_start) {// 开始日期
            cancelSelected();
            if (listener != null) {
                listener.startClick();
            }
        } else if (i == R.id.txt_end) {// 截止日期
            cancelSelected();
            if (listener != null) {
                listener.endClick();
            }
        } else if (i == R.id.txt_cancel) {// 取消
            if (listener != null) {
                listener.cancelClick();
            }
        } else if (i == R.id.btn_right) {// 确认
            saveCurDateStatus();
            if (listener != null) {
                listener.rightClick(judgeSelectDateStatus(), selectedContent);
            }
        } else if (i == R.id.btn_left) {// 重置
            for (int j = 0; j < curSelectStatus.size(); j++) {
                singleSelectList.get(j).setSelected(curSelectStatus.get(j).getSelected());
            }
            singleSelect.getAdapter().notifyDataSetChanged();
            txtStart.setText(defaultStartDate);
            txtEnd.setText(defaultEndDate);
//            curPosition = 2;
//            singleSelectList.get(0).setSelected(false);
//            singleSelectList.get(1).setSelected(false);
//            singleSelectList.get(2).setSelected(true);
//            singleSelect.getAdapter().notifyDataSetChanged();
//            txtStart.setText(defaultData[0]);
//            txtEnd.setText(defaultData[1]);

            if (resetListener != null) {
                resetListener.resetClick();
            }
        }
    }

    /**
     * 取消选中
     */
    private void cancelSelected() {
        curPosition = -1;
        for (Content item : singleSelectList) {
            if (item.getSelected()) {
                item.setSelected(false);
                singleSelect.getAdapter().notifyDataSetChanged();
            }
        }
    }

    /**
     * 设置某一项选中
     *
     * @param position
     */
    private void setSelected(int position) {
        for (int i = 0; i < singleSelectList.size(); i++) {
            if (i == position) {
                singleSelectList.get(i).setSelected(true);
                continue;
            }
            singleSelectList.get(i).setSelected(false);
        }
    }

    /**
     * 保存当前的日期状态
     */
    private void saveCurDateStatus() {
        curSelectStatus.clear();
        defaultStartDate = txtStart.getText().toString();
        defaultEndDate = txtEnd.getText().toString();
        for (int i = 0; i < singleSelectList.size(); i++) {
            Content content = new Content();
            content.setSelected(singleSelectList.get(i).getSelected());
            curSelectStatus.add(content);
        }
    }

    public int getCurPosition() {
        return curPosition;
    }

    public interface ClickListener {
        void startClick();

        void endClick();

        void cancelClick();

        void rightClick(boolean haveSelected, String content);
    }


    public interface ResetClickListener {
        void resetClick();
    }

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    public void setResetListener(ResetClickListener listener) {
        this.resetListener = listener;
    }

    //单选框显示
    public void setSingleSelect(boolean visible){
        if (!visible){
            singleSelect.setVisibility(View.GONE);
        }else if(visible){
            singleSelect.setVisibility(View.VISIBLE);
        }

    }
}
