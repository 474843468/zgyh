package com.boc.bocsoft.mobile.bocmobile.base.widget.expandableitemview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taoyongzhen on 2016/12/6.
 * 该组件点击显示；点击隐藏
 */

public class ClickExpandableView extends LinearLayout {

    private Context mContext;
    private View rootView;
    private TextView dateHint;
    private TextView firstUnit;
    private TextView firstNum;
    private ImageView investEye;
    private ImageView moreIndicate;
    private TableLayout tableLayout;

    private int columnNum = 2;
    private String stuffChars = "****";
    private List<DateBean> contens = new ArrayList<>();

    private DateBean firstBean;

    public ClickExpandableView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public ClickExpandableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public ClickExpandableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.boc_fund_position_head, this);
        dateHint = (TextView) rootView.findViewById(R.id.date_hint);
        firstUnit = (TextView) rootView.findViewById(R.id.tv_unit);
        firstNum = (TextView) rootView.findViewById(R.id.tv_num);
        investEye = (ImageView) rootView.findViewById(R.id.invest_eye);
        moreIndicate = (ImageView) rootView.findViewById(R.id.img_more);
        tableLayout = (TableLayout) rootView.findViewById(R.id.tableLayout);
        setListener();
    }

    private void setListener() {
        investEye.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewDisplay();
            }
        });

        moreIndicate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moreIndicate.isSelected()){//打开
                    tableLayout.setVisibility(VISIBLE);
                }else{
                    tableLayout.setVisibility(GONE);
                }
            }
        });
    }

    public void setStuffChars(String stuffChars) {
        this.stuffChars = stuffChars;
    }

    public void viewDisplay(){
        initFirstView();
        initTableLayout();
    }

    public void setColumnNum(int columnNum) {
        this.columnNum = columnNum;
    }

    public void setDateBeans(List<DateBean> allDateBeans){
        firstBean = allDateBeans.get(0);
        allDateBeans.remove(0);
        contens = allDateBeans;
        viewDisplay();
    }

    public void setFirstBean(DateBean firstBean) {
        this.firstBean = firstBean;
    }

    private void initFirstView(){
        if (firstBean != null){
            firstUnit.setText(firstBean.dateUnit);
            if (investEye.isSelected()){
                firstNum.setText(stuffChars);
            }else{
                firstNum.setText(firstBean.dateNum);
            }
        }
    }


    private void initTableLayout() {
        if (moreIndicate.isSelected()){
            tableLayout.setVisibility(GONE);
            return;
        }
        if (contens == null || contens.size() <= 0){
            return;
        }
        tableLayout.setShrinkAllColumns(true);
        if (tableLayout.getChildCount() <= 0) {
            int tableRomNum = contens.size() % columnNum == 0 ? contens.size() / columnNum : contens.size() / columnNum + 1;
            int position = 0;
            for (int i = 0; i < tableRomNum; i++) {
                TableRow tableRow = new TableRow(tableLayout.getContext());
                tableRow.setWeightSum(columnNum);
                tableRow.setOrientation(HORIZONTAL);
                for (int j=0;j<columnNum;j++){
                    if (position < contens.size()){//均分tableRow  是指子view的weight为1
                        TextView textView = new TextView(tableRow.getContext());
                        TableRow.LayoutParams params = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.weight = 1;
                        textView.setLayoutParams(params);
                        textView.setTextAppearance(tableRow.getContext(),R.style.tv_common);
                        textView.setTextColor(tableRow.getContext().getResources().getColor(R.color.boc_common_cell_color));
                        if (investEye.isSelected()){
                            textView.setText(contens.get(position).getCloseContent());
                        }else{
                            textView.setText(contens.get(position).getOpenContent());
                        }
                        textView.setTag(Integer.valueOf(position));
                        tableRow.addView(textView);
                    }
                    position ++;
                }
                tableLayout.addView(tableRow);
            }
        }else{
            for (int i = 0;i<tableLayout.getChildCount();i++){
                ViewGroup tableRow = (ViewGroup) tableLayout.getChildAt(i);
                for (int j=0;j < tableRow.getChildCount();j++){
                    TextView textView = (TextView) tableRow.getChildAt(j);
                    Integer integer = (Integer) textView.getTag();
                    if (investEye.isSelected()){
                        textView.setText(contens.get(integer.intValue()).getCloseContent());
                    }else{
                        textView.setText(contens.get(integer.intValue()).getOpenContent());
                    }
                }
            }
        }

    }

    public void setReferDate(String date){
        if (StringUtil.isNullOrEmpty(date)){
            LocalDate localDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate();
            date = localDate.format(DateFormatters.dateFormatter1);
        }
        dateHint.setText(mContext.getString(R.string.boc_fund_position_refer_value,date));

    }

    public List<DateBean> getContens() {
        return contens;
    }

    public void setContens(List<DateBean> contens) {
        this.contens = contens;
    }


    public static class DateBean {
        String dateUnit;
        String dateNum;
        String stuffChars = "****";

        public DateBean() {
        }

        public DateBean(String dateUnit, String dateNum) {
            this.dateUnit = dateUnit;
            this.dateNum = dateNum;
        }
        // 基金持仓显示方式
        public String getOpenContent(){
            return dateUnit + " " +dateNum;
        }

        public String getCloseContent(){
            return dateUnit + " " + stuffChars;
        }

        public String getDateUnit() {
            return dateUnit;
        }

        public void setDateUnit(String dateUnit) {
            this.dateUnit = dateUnit;
        }

        public String getDateNum() {
            return dateNum;
        }

        public void setDateNum(String dateNum) {
            this.dateNum = dateNum;
        }
    }


}
