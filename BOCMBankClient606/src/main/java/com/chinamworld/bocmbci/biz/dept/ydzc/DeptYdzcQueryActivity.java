package com.chinamworld.bocmbci.biz.dept.ydzc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.dept.DeptBaseActivity;
import com.chinamworld.bocmbci.biz.dept.adapter.DeptYdzcQueryAdapter;
import com.chinamworld.bocmbci.biz.investTask.BocInvestTask;
import com.chinamworld.bocmbci.biz.investTask.IAction;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

/** 定期约定转存----查询页面 */
public class DeptYdzcQueryActivity extends DeptBaseActivity {
	private static final String TAG = "DeptYdzcQueryActivity";
	private LinearLayout tabcontent;// 主Activity显示
	private Spinner accSpinner = null;
	private Button queryButton = null;
	private ListView accListView = null;
	private View queryView = null;
	private List<Map<String, Object>> resultList = null;
	private List<String> accList = null;
	private String accountId = null;
	/**账户详情list*/
	private List<Map<String, String>> accdetailList = null;
	private DeptYdzcQueryAdapter adapter = null;
	private String accountNumber = null;
	private int position = -1;
    private int detailPosition=-1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		if ((LinearLayout) findViewById(R.id.sliding_body) != null) {
			((LinearLayout) findViewById(R.id.sliding_body)).setPadding(0, 0, 0, 0);
		}
		Button leftButton = (Button) findViewById(R.id.ib_back);
		leftButton.setVisibility(View.VISIBLE);
		ibRight.setVisibility(View.GONE);
		setTitle(getResources().getString(R.string.dept_dqyzc_title));
		leftButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), MainActivity.class);
//				startActivity(intent);
				goToMainActivity();
			}
		});
		setLeftSelectedPosition("deptStorageCash_3");
		
		// add lqp 2015年12月14日11:53:50 判断是否开通投资理财!
		BocInvestTask task = BocInvestTask.getInstance(this);
		registActivityEvent(task);
		task.doTask(new IAction() {

			@Override
			public void SuccessCallBack(Object param) {
				// 查询定期账户
				BaseHttpEngine.dissMissProgressDialog();
				BaseHttpEngine.showProgressDialogCanGoBack();
				String[] s = { ConstantGloble.ACC_TYPE_REG };
				requestPsnCommonQueryAllChinaBankAccount(s);
			}
		},null);
		
		// 修改判断是否开通投资理财!
		// 查询定期账户
//		BaseHttpEngine.dissMissProgressDialog();
//		BaseHttpEngine.showProgressDialogCanGoBack();
//		String[] s = { ConstantGloble.ACC_TYPE_REG };
//		requestPsnCommonQueryAllChinaBankAccount(s);
	}

	/** 查询定期账户列表---回调 */
	@Override
	public void requestPsnCommonQueryAllChinaBankAccountCallBack(Object resultObj) {
		super.requestPsnCommonQueryAllChinaBankAccountCallBack(resultObj);
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = response.getResponse();
		BiiResponseBody body = biiResponseBodys.get(0);
		resultList = (List<Map<String, Object>>) body.getResult();
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseHttpEngine.canGoBack=true;
			BaseDroidApp.getInstanse().showMessageDialog(
					getResources().getString(R.string.acc_transferquery_null),
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
//							ActivityTaskManager.getInstance()
//									.removeAllActivity();
//							Intent intent = new Intent();
//							intent.setClass(BaseDroidApp.getInstanse()
//									.getCurrentAct(), MainActivity.class);
//							BaseDroidApp.getInstanse().getCurrentAct()
//									.startActivity(intent);
							goToMainActivity();
						}
					});
			return;
		}
		getAccList();
		if (StringUtil.isNullOrEmpty(accList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseHttpEngine.canGoBack=true;
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		init();
		initOnClick();
		BaseHttpEngine.dissMissProgressDialog();
		BaseHttpEngine.canGoBack=false;
	}

	private void getAccList() {
		accList = new ArrayList<String>();
		int len = resultList.size();
		for (int i = 0; i < len; i++) {
			Map<String, Object> map = resultList.get(i);
			String accountNumber = (String) map.get(Comm.ACCOUNTNUMBER);
			String accNum = StringUtil.getForSixForString(accountNumber);
			accList.add(accNum);
		}

	}

	private void init() {
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		queryView = LayoutInflater.from(this).inflate(R.layout.dept_dqydzc_query, null);
		tabcontent.addView(queryView);
		accSpinner = (Spinner) findViewById(R.id.spinner_acc);
		queryButton = (Button) findViewById(R.id.dept_query_deal_query);
		accListView = (ListView) findViewById(R.id.dept_list_view);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.dept_spinner, accList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		accSpinner.setAdapter(adapter);
		accSpinner.setSelection(0);
		position = 0;
		accountNumber = accSpinner.getSelectedItem().toString();
	}

	private void initOnClick() {
		accSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int positions, long id) {
				position = positions;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		// 查询按钮
		queryButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Map<String, Object> map = resultList.get(position);
				accountId = (String) map.get(Comm.ACCOUNT_ID);
				String accountNumbers = (String) map.get(Comm.ACCOUNTNUMBER);
				accountNumber = StringUtil.getForSixForString(accountNumbers);
				// 查询账户详情
				if (!StringUtil.isNullOrEmpty(accdetailList)) {
					accdetailList.clear();
					adapter.refshDate(new ArrayList<Map<String, String>>());
				}
				accListView.setVisibility(View.INVISIBLE);
				BaseHttpEngine.showProgressDialog();
				requsetPsnAccountQueryAccountDetail(accountId);
			}
		});

	}

	/** 查询账户详情---回调 */
	@Override
	public void requsetPsnAccountQueryAccountDetailCallback(Object resultObj) {
		super.requsetPsnAccountQueryAccountDetailCallback(resultObj);
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = response.getResponse();
		BiiResponseBody body = biiResponseBodys.get(0);
		Map<String, Object> result = (Map<String, Object>) body.getResult();
		if (StringUtil.isNullOrEmpty(result)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		List<Map<String, String>> list = (List<Map<String, String>>) result.get(Dept.DEPT_ACCOUNTDETAILIST_RES);
		if (StringUtil.isNullOrEmpty(list)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		dealACCdetail(list);
		if (StringUtil.isNullOrEmpty(accdetailList)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		accListView.setVisibility(View.VISIBLE);
		BaseHttpEngine.dissMissProgressDialog();
		adapter = new DeptYdzcQueryAdapter(this, accdetailList);
		accListView.setAdapter(adapter);
		adapter.setOnItemClickListener(onItemClickListener);
	}

	/** 账户详情---支持整存整取、状态为正常、自动续存的人民币和外币存单。 */
	private void dealACCdetail(List<Map<String, String>> list) {
		int len = list.size();
		accdetailList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < len; i++) {
			Map<String, String> map = list.get(i);
			String status = map.get(Dept.DEPT_STATUS_RES);
			String type = map.get(Dept.DEPT_TYPE_RES);
			String convertType = map.get(Dept.DEPT_CONVERTTYPE_RES);
			if (!StringUtil.isNull(status) && !StringUtil.isNull(type) && !StringUtil.isNull(convertType)
					&& ConstantGloble.FOREX_ACCTYPE_NORMAL.equals(status)
					&& ConstantGloble.FOREX_ACCTYPE_ZCZQ.equals(type)
					&& ConstantGloble.CONVERTTYPE_R.equals(convertType)) {
				accdetailList.add(map);
			}
		}
	}

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			Map<String, String> map = accdetailList.get(position);
			detailPosition=position;
			String appointStatus = map.get(Dept.DEPT_APPOINTSTATUS_RES);
			String volumeNumber = map.get(Dept.DEPT_VOLUMENUMBER_RES);
			String cdNumber = map.get(Dept.DEPT_CDNUMBER_RES);
			BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.DEPT_ACCDETAILLIST, accdetailList);
			if (ConstantGloble.COMBINE_FLAG_N.equals(appointStatus)) {
				// N-未建立约定
				Intent intent = new Intent();
				intent.putExtra(ConstantGloble.POSITION, position);
				intent.putExtra(Comm.ACCOUNT_ID, accountId);
				intent.putExtra(Comm.ACCOUNTNUMBER, accountNumber);
				intent.setClass(DeptYdzcQueryActivity.this, DeptYdzcQueryDetailActivity.class);				
				startActivity(intent);
			} else if (ConstantGloble.COMBINE_FLAG_Y.equals(appointStatus)) {
				// Y-已建立约定
				BaseHttpEngine.showProgressDialog();
				requsetPsnTimeDepositAppointQuery(accountId, volumeNumber, cdNumber);
			}

		}
	};

	/** 定期约定转存查询-----回调 */
	@Override
	public void requsetPsnTimeDepositAppointQueryCallback(Object resultObj) {
		super.requsetPsnTimeDepositAppointQueryCallback(resultObj);
		BiiResponse response = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = response.getResponse();
		BiiResponseBody body = biiResponseBodys.get(0);
		Map<String, String> resultMap = (Map<String, String>) body.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			BaseHttpEngine.dissMissProgressDialog();
			BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
			return;
		}
		Intent intent = new Intent();
		intent.setClass(DeptYdzcQueryActivity.this, DeptYdzcAppointQueryActivity.class);
		intent.putExtra(Comm.ACCOUNT_ID, accountId);
		intent.putExtra(Comm.ACCOUNTNUMBER, accountNumber);
		intent.putExtra(ConstantGloble.POSITION, detailPosition);
		intent.putExtra(Comm.ACCOUNT_ID, accountId);
		intent.putExtra(Comm.ACCOUNTNUMBER, accountNumber);
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.DEPT_RESULTMAP, resultMap);
		startActivity(intent);
		BaseHttpEngine.dissMissProgressDialog();
	}

	public boolean httpRequestCallBackPre(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		LogGloble.d(TAG, "异常----------");
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
			if (Comm.QRY_ALL_BANK_ACCOUNT.equals(biiResponseBody.getMethod())) {
				if (biiResponse.isBiiexception()) {// 代表返回数据异常
					BiiHttpEngine.dissMissProgressDialog();
					BiiError biiError = biiResponseBody.getError();
					// 判断是否存在error
					if (biiError != null) {
						if (biiError.getCode() != null) {
							if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
								// 要重新登录
								showTimeOutDialog(biiError.getMessage());
							} else {
								// 非会话超时错误拦截
								BaseDroidApp.getInstanse().createDialog("", biiError.getMessage(),
										new OnClickListener() {

											@Override
											public void onClick(View v) {
												BaseDroidApp.getInstanse().dismissMessageDialog();
//												ActivityTaskManager.getInstance().removeAllActivity();
//												Intent intent = new Intent();
//												intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
//														MainActivity.class);
//												startActivity(intent);
												goToMainActivity();
											}
										});
								return true;
							}
						}
					}
					return true;
				}
				return false;// 没有异常
			} else {
				return super.httpRequestCallBackPre(resultObj);
			}
		}
		// 随机数获取异常
		return super.httpRequestCallBackPre(resultObj);
	};

//	private void showTimeOutDialog(BiiError biiError) {
//		BaseDroidApp.getInstanse().showMessageDialog(biiError.getMessage(), new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				BaseDroidApp.getInstanse().dismissErrorDialog();
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(DeptYdzcQueryActivity.this, LoginActivity.class);
//				startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
//			}
//		});
//	}
}
