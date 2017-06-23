package com.chinamworld.bocmbci.biz.crcd.mycrcd.transquery;

import java.util.ArrayList;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.MainActivity;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.biz.crcd.adapter.CrcdDividedQueryAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 分期历史查询及结清查询前
 * 
 * @author huangyuchao
 * 
 */
public class CrcdDividedHistoryQueryDetail extends CrcdBaseActivity {
	private static final String TAG = "CrcdDividedHistoryQueryDetail";
	ListView lv_acc_query_result;

	/** 分期历史查询界面视图 */
	private View view;
	/** 请求回来的分期列表 */
	protected static List<Map<String, Object>> divideList;

	TextView tv_divided_date, tv_divided_num, tv_divided_money;

	RelativeLayout load_more;

	CrcdDividedQueryAdapter adapter;

	int currentIndex = 0;
	int pageSize = 10;
	int maxNum;
	private String accountId = null;
	/** 分期历史查询结果 */
	protected List<Map<String, Object>> crcdTransInfo;
	private boolean isRefresh = true;
	/** 用户选择的查询结果 */
	protected static Map<String, Object> dividedMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		// 为界面标题赋值
		setTitle(this.getString(R.string.mycrcd_setup_history_queryandjq));
		// 右上角按钮赋值
		setText(this.getString(R.string.acc_rightbtn_go_main));
		view = addView(R.layout.crcd_divided_history_query_detail);
		// 右上角按钮点击事件
		setRightBtnClick(rightBtnClick);
		// 初始化界面
		init();
		crcdTransInfo = new ArrayList<Map<String, Object>>();
		accountId = this.getIntent().getStringExtra(Crcd.CRCD_ACCOUNTID_RES);
		setDate();
	}

	/** 获取上一页面传递的数据 */
	private void setDate() {
		String recordNum = this.getIntent().getStringExtra(Crcd.CRCD_RECORDNUMBER);
		List<Map<String, Object>> lists = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(Crcd.CRCD_SUPPLYLIIST);
		getDate(lists, Integer.valueOf(recordNum));

	}

	/** 分期历史查询及结清查询 */
	private void PsnCrcdDividedPayHisQry() {
		// 通讯开始,展示通讯框
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		String commConversationId = String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setConversationId(commConversationId);
		biiRequestBody.setMethod(Crcd.CRCD_PSNCRCDICIDEPAYHISQRY);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		params.put(Crcd.CRCD_CURRENTINDEX, String.valueOf(currentIndex));
		params.put(Crcd.CRCD_PAGESIZE, String.valueOf(pageSize));
		params.put(Crcd.CRCD_REFRESH, ConstantGloble.FALSE);
		biiRequestBody.setParams(params);
		HttpManager.requestBii(biiRequestBody, this, "PsnCrcdDividedPayHisQryCallBack");
	}

	public void PsnCrcdDividedPayHisQryCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> list = response.getResponse();
		BiiResponseBody body = list.get(0);
		Map<String, Object> returnList = (Map<String, Object>) body.getResult();
		if (StringUtil.isNullOrEmpty(returnList)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.mycrcd_total_result_empty));
			return;
		}
		String recordNum = String.valueOf(returnList.get(Crcd.CRCD_RECORDNUMBER));
		maxNum = Integer.valueOf(recordNum);
		LogGloble.d(TAG + " maxNum", maxNum + "");
		if (!returnList.containsKey(Crcd.CRCD_SUPPLYLIIST)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.mycrcd_total_result_empty));
			return;
		}
		List<Map<String, Object>> lists = (List<Map<String, Object>>) returnList.get(Crcd.CRCD_SUPPLYLIIST);

		if (StringUtil.isNullOrEmpty(lists)) {
			BaseDroidApp.getInstanse().showInfoMessageDialog(getString(R.string.mycrcd_total_result_empty));
			return;
		}
		getDate(lists, maxNum);
	}

	/** 得到查询结果，判断是否显示更多按钮 */
	private void getDate(List<Map<String, Object>> lists, int maxNum) {
		crcdTransInfo.addAll(lists);
		if (maxNum > pageSize && isRefresh) {
			lv_acc_query_result.addFooterView(load_more);
		}
		if (!isRefresh) {
			if (currentIndex + pageSize >= maxNum) {
				lv_acc_query_result.removeFooterView(load_more);
			}
		}
		if (isRefresh) {
			adapter = new CrcdDividedQueryAdapter(this, crcdTransInfo);
			lv_acc_query_result.setAdapter(adapter);
		} else {
			adapter.dataChanged(crcdTransInfo);
		}
		lv_acc_query_result.setOnItemClickListener(lvItemClickListener);
	}

	/** 右侧按钮点击事件 */
	OnClickListener rightBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 回主页面
//			Intent intent = new Intent(CrcdDividedHistoryQueryDetail.this, MainActivity.class);
//			startActivity(intent);
			goToMainActivity();
		}
	};

	private void init() {
		tv_divided_date = (TextView) view.findViewById(R.id.boci_product_name);
		tv_divided_num = (TextView) view.findViewById(R.id.boci_yearlyRR);
		tv_divided_money = (TextView) view.findViewById(R.id.boci_timeLimit);

		tv_divided_date.setText(this.getString(R.string.mycrcd_divided_date));
		tv_divided_num.setText(this.getString(R.string.mycrcd_divided_num));
		tv_divided_money.setText(this.getString(R.string.mycrcd_divided_money));

		lv_acc_query_result = (ListView) findViewById(R.id.lv_acc_query_result);

		load_more = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.query_list_footer, null);
		TextView btn_load_more = (TextView) load_more.findViewById(R.id.finc_listiterm_tv1);
		btn_load_more.setOnClickListener(goMoreClickListener);

	}

	/** 点击更多按钮 */
	OnClickListener goMoreClickListener = new OnClickListener() {
		public void onClick(View v) {
			isRefresh = false;
			currentIndex += pageSize;
			// 分期历史查询
			BaseHttpEngine.showProgressDialog();
			PsnCrcdDividedPayHisQry();
		};
	};
	OnItemClickListener lvItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// 进入详情页
			Intent it = new Intent(CrcdDividedHistoryQueryDetail.this, CrcdDividedDetailListDetail.class);
			dividedMap = crcdTransInfo.get(position);
			startActivity(it);
		}
	};

}
