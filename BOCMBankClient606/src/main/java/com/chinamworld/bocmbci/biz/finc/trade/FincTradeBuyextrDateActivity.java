package com.chinamworld.bocmbci.biz.finc.trade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.biz.finc.FincBaseActivity;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;

/**
 * 基金买入 指定日期交易页面
 * 
 * @author xyl
 * 
 */
public class FincTradeBuyextrDateActivity extends FincBaseActivity {
	private static final String TAG = "FincTradeBuyextrDateActivity";

	private Button nextBtn;
	private Button lastBtn;


	private TextView assignedDateTv;
	/**指定日期显示布局*/
	private LinearLayout ll1;
	/**固定日期显示布局*/
	private LinearLayout ll2;
	/**固定日期spinner*/
	private Spinner finc_fundcompany_spinner;
	/**固定日期返回集合*/
	private List<String> dealDateList;
	/**用户选中的固定日期下标*/
	private int selectPosition = -1;
	/**指定日期执行方式标识     1 、3个月内任意日期   2、固定日期*/
	String sizinvt = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BaseHttpEngine.showProgressDialogCanGoBack();
		requestSystemDateTime();
	}
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		init();
	}

	/**
	 * 初始化布局
	 * 
	 * @Author xyl
	 */
	private void init() {
		View childview = mainInflater
				.inflate(R.layout.finc_fundbuy_extra, null);
		tabcontent.addView(childview);
		
		OnItemSelectedListener  listener = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				selectPosition = position;
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

				
			}
		};
		StepTitleUtils.getInstance().initTitldStep(this,
				fincControl.getStepsForExtradeDayBuy1());
		StepTitleUtils.getInstance().setTitleStep(2);
		setTitle(R.string.finc_title_buy);
		nextBtn = (Button) childview.findViewById(R.id.finc_next);
		lastBtn = (Button) childview.findViewById(R.id.finc_last);
		ll1 = (LinearLayout) childview.findViewById(R.id.ll1);
		ll2 = (LinearLayout) childview.findViewById(R.id.ll2);
		Intent intent  = getIntent();
		sizinvt = intent.getStringExtra(Finc.ISZISTBY);
		if("1".equals(sizinvt)){
			//指定3个月内任意日期执行
			ll2.setVisibility(View.GONE);
			assignedDateTv = (TextView) childview.findViewById(R.id.finc_extrdate_tv);
			assignedDateTv.setText(QueryDateUtils.getOneDayLater(dateTime));
			assignedDateTv.setOnClickListener(this);
			BaseHttpEngine.dissMissProgressDialog();
		}else{
			//指定固定日期执行
			ll1.setVisibility(View.GONE);
			childview.findViewById(R.id.tv_content).setVisibility(View.GONE);;
			ll2.setVisibility(View.VISIBLE);
			finc_fundcompany_spinner = (Spinner) findViewById(R.id.finc_fundcompany_spinner);
			finc_fundcompany_spinner.setOnItemSelectedListener(listener);
			
			//查询固定日期
			requestPsnFundCanDealDateQuery(intent.getStringExtra(Finc.I_FUNDCODE),"0");

		}



		nextBtn.setOnClickListener(this);
		lastBtn.setOnClickListener(this);
		
		initRightBtnForMain();
	}


	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.finc_last:// 上一步
			this.finish();
			break;
		case R.id.finc_next:// 下一步
			Intent intent =getIntent();
			intent.putExtra("11", "11");
			intent.setClass(this, FincTradeBuyConfirmActivity.class);
			if("1".equals(sizinvt)){
				if(QueryDateUtils.compareDate(assignedDateTv.getText().toString(), dateTime)){//不能在系统日期之前
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							"执行日期必须晚于系统当前日期");
					return; 
				}
				if(!QueryDateUtils.compareDateThree(dateTime, assignedDateTv.getText().toString())){
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							"执行日期不能超过当前日期3个月");
					return;
				}
				intent.putExtra(Finc.I_ASSIGNEDDATE, assignedDateTv.getText().toString());
			}else {
				intent.putExtra(Finc.I_ASSIGNEDDATE, dealDateList.get(selectPosition));

			}
			
			startActivityForResult(intent, 1);
			break;
		case  R.id.finc_extrdate_tv:
			Calendar c = QueryDateUtils.getCalendarWithDate(assignedDateTv.getText().toString());
			DatePickerDialog dialog = new DatePickerDialog(this, new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					StringBuffer date = new StringBuffer();
					date.append(String.valueOf(year));
					date.append("/");
					int month = monthOfYear + 1;
					date.append(((month < 10) ? ("0" + month) : (month + "")));
					date.append("/");
					date.append(((dayOfMonth < 10) ? ("0" + dayOfMonth)
							: (dayOfMonth + "")));
					assignedDateTv.setText(date.toString());
				}
			}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
			c.get(Calendar.DAY_OF_MONTH));
			dialog.show();
			break;
		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;

		default:
			break;
		}
	}


	@Override
	public void requestPsnFundCanDealDateQueryCallback(Object resultObj) {
		super.requestPsnFundCanDealDateQueryCallback(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> Result =(List<Map<String, Object>>) biiResponseBody.getResult();
		dealDateList = new ArrayList<String>();
		for(int i =0; i < Result.size(); i++){
			dealDateList.add((String) Result.get(i).get(Finc.FINC_QUERYEXTRADAY_DEALDATE));
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(FincTradeBuyextrDateActivity.this,R.layout.dept_spinner, dealDateList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		finc_fundcompany_spinner.setAdapter(adapter);
	}
	
	
	
}
