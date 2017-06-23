package com.chinamworld.bocmbci.biz.blpt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.Blpt;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 账单支付省列表
 * 
 * @author panwe
 * 
 */
public class BillPaymentProvinceActivity extends BaseActivity {
	/** 主界面 */
	private LinearLayout mainLayout;
	/** 子界面 */
	private View viewContent;
	/** 省份 */
	private ListView lvProvince;
	/** 关闭按钮 */
	private Button btnClose;
	/** 省简称 */
	private String proShotName;
	private String proName;
	private int tag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);
		viewContent = (View) LayoutInflater.from(this).inflate(
				R.layout.blpt_province_list, null);
		setTitle(this.getString(R.string.blpt_province_title));
		initPulldownBtn();
		invisible();
		init();
	}

	private void init() {
		tag = getIntent().getIntExtra(Blpt.KEY_TAG, 0);
		mainLayout = (LinearLayout) this.findViewById(R.id.sliding_body);
		mainLayout.addView(viewContent);
		Button btnBack = (Button) findViewById(R.id.ib_back);
		btnBack.setVisibility(View.GONE);
		btnClose = (Button) findViewById(R.id.ib_top_right_btn);
		btnClose.setVisibility(View.VISIBLE);
		btnClose.setText(this.getString(R.string.close));
		btnClose.setOnClickListener(closeClick);

		lvProvince = (ListView) viewContent.findViewById(R.id.blpt_lv_province);
		lvProvince.setOnItemClickListener(itemClick);
		setProvListData();
	}

	/** List点击事件 */
	private OnItemClickListener itemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			proShotName = (String) BlptUtil.getInstance().getProvList().get(position)
			.get(Blpt.PROVINCE_SNAME);
			proName = (String) BlptUtil.getInstance().getProvList().get(position)
					.get(Blpt.PROVINCE_NAME);
			getBillCity(proShotName);
		}

	};
	
	//显示省数据
	private void setProvListData(){
		int[] ids = new int[] { R.id.blpt_province_name };
		String[] content = new String[] { Blpt.PROVINCE_NAME };
		SimpleAdapter mAdapter = new SimpleAdapter(this, BlptUtil.getInstance().getProvList(),
				R.layout.blpt_province_item, content, ids);
		lvProvince.setAdapter(mAdapter);
	}
	
	/**
	 * 获取城市列表
	 */
	private void getBillCity(String sn) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Blpt.METHOD_CODE_CITY);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Blpt.CITY_SHORTNAME, sn);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "cityListCallBack");
	}

	/**
	 * 城市列表返回
	 * 
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void cityListCallBack(Object resultObj) {
		List<Map<String, Object>> cityList = HttpTools.getResponseResult(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(cityList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("对不起，"+proName+"暂时不能进行缴费");
			return;
		}
		BlptUtil.getInstance().setCityList(cityList);
		Intent it = new Intent(BillPaymentProvinceActivity.this,
				BillPaymentCityActivity.class);
		it.putExtra(Blpt.KEY_PROVICESHORTNAME, proShotName);
		it.putExtra(Blpt.KEY_TAG, tag);
		startActivity(it);
	}

	/** 隐藏侧面和底部按钮 */
	private void invisible() {
		Button btnhide = (Button) findViewById(R.id.btn_show);
		View view = findViewById(R.id.menu_popwindow);
		btnhide.setVisibility(View.GONE);
		view.setVisibility(View.GONE);
	}

	/** 返回主页Listener */
	private OnClickListener closeClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};

	@Override
	public ActivityTaskType getActivityTaskType() {
		return ActivityTaskType.OneTask;
	}
}
