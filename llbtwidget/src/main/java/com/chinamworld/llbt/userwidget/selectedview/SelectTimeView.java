package com.chinamworld.llbt.userwidget.selectedview;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chinamworld.llbt.userwidget.datetimepicker.CustomDatePickerDialog;
import com.chinamworld.llbt.userwidget.dialogview.MessageDialog;
import com.chinamworld.llbtwidget.R;

import java.util.Date;

/**
 * Created by Administrator on 2016/9/14.
 */
public class SelectTimeView extends FrameLayout implements View.OnClickListener {
    private Context mContext;
    private TextView startTimeView, endTimeView;
    CustomDatePickerDialog startDatePickerDialog,endDatePickerDialog;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    private String startTime,endTime;


    public String getStartTimeDate(){
        return startTimeView.getText().toString();
    }

    public String getEndTimeDate(){
        return endTimeView.getText().toString();
    }



    public SelectTimeView(Context context) {
        super(context);
        initView(context);
    }

    public SelectTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SelectTimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.llbt_select_time_view,this,true);
        startTimeView = (TextView)findViewById(R.id.txt_start);
        endTimeView = (TextView)findViewById(R.id.txt_end);
        startTimeView.setOnClickListener(this);
        endTimeView.setOnClickListener(this);

    }

    private Date maxStartDate,minStartDate;
    /***
     * 设置最大日期，和最小日期
     * @param maxTime
     * @param minTime
     */
    public void setStartDateLimit(Date maxTime, Date minTime){
        this.maxStartDate = maxTime;
        this.minStartDate = minTime;
    }
    private Date maxEndDate,minEndDate;
    /***
     * 设置结束最大日期，和最小日期
     * @param maxTime
     * @param minTime
     */
    public void setEndDateLimit(Date maxTime, Date minTime){
        this.maxEndDate = maxTime;
        this.minEndDate = minTime;
    }

    private Date stringToDate(String timeStr){
        int year = Integer.parseInt(timeStr.substring(0,4));
        int month = Integer.parseInt(timeStr.substring(5,7));
        int day = Integer.parseInt(timeStr.substring(8,10));
        Date date = new Date(year,month,day);
        return date;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.txt_start){
            if(startDatePickerDialog == null)
//                startDatePickerDialog =  getDatePick(startTimeView,stringToDate(startTime));
                startDatePickerDialog =  getDatePick(startTimeView,stringToDate(startTimeView.getText().toString()));
            startDatePickerDialog.setDateLimit(maxStartDate,minStartDate);
            startDatePickerDialog.show();
        }
        else if(v.getId() == R.id.txt_end){
            if(endDatePickerDialog == null)
//                endDatePickerDialog =  getDatePick(endTimeView,stringToDate(endTime));
                endDatePickerDialog =  getDatePick(endTimeView,stringToDate(endTimeView.getText().toString()));
            endDatePickerDialog.setDateLimit(maxEndDate,minEndDate);
            endDatePickerDialog.show();
        }
        startDatePickerDialog = null;
        endDatePickerDialog = null;
    }

    private CustomDatePickerDialog getDatePick(final TextView timeText, Date date){

        int startYear = date.getYear();
        int startMonthOfYear =date.getMonth();
        final int startDayOfMonth = date.getDate();
        CustomDatePickerDialog datePickerDialog = new CustomDatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener(){

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                StringBuilder bf = new StringBuilder();
                bf.append(String.valueOf(year));
                bf.append("/");
                int month = monthOfYear + 1;
                bf.append(((month < 10) ? ("0" + month): (month + "")));
                bf.append("/");
                bf.append(((dayOfMonth < 10) ? ("0" + dayOfMonth): (dayOfMonth + "")));
                timeText.setText(bf);
                timeText.setTag(new Date(year,monthOfYear,dayOfMonth));
            }

        },startYear, startMonthOfYear-1,startDayOfMonth);

        return datePickerDialog;
    }

    public void resetDate(){
        startTimeView.setText("请选择起始日期");
        endTimeView.setText("请选择截止日期");
    }

    /**
     * 设置起止日期
     * @param starttime
     * @param endtime
     */
    public void resetDate(String starttime, String endtime){
        startTimeView.setText(starttime);
        endTimeView.setText(endtime);
//        startDatePickerDialog = null;
//        endDatePickerDialog = null;
    }

    public boolean checkDate(){
        if(startTimeView.getText().toString().equals("请选择起始日期")){
            MessageDialog.showMessageDialog(mContext,"请选择起始日期");
            return false;
        }
        if(endTimeView.getText().toString().equals("请选择截止日期")){
            MessageDialog.showMessageDialog(mContext,"请选择截止日期");
            return false;
        }
        return true;
    }
}
