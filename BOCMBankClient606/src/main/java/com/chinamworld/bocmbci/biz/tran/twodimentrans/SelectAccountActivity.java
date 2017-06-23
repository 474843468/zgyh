package com.chinamworld.bocmbci.biz.tran.twodimentrans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.twodimentrans.adapter.SelectAccAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 生成二维码 选择
 * 
 * @author
 * 
 */
public class SelectAccountActivity extends TranBaseActivity {

	private ListView mAccListLv = null;
	private List<Map<String, Object>> queryList = null;

	// private ImageView grayItemIv = null;
	private Button nextBtn = null;

	private int itemPosition = -1;

	private SelectAccAdapter adapter;
	private String customName = "";
	private String accountNumber = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.two_dimen_generate));
		View view = mInflater.inflate(R.layout.tran_2dimen_select_acc_activity,
				null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		toprightBtn();
		getData();
		setupView();
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(SelectAccountActivity.this,
						TwoDimenTransActivity1.class);
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ActivityTaskManager.getInstance().removeAllActivity();
			Intent intent = new Intent();
			intent.setClass(SelectAccountActivity.this,
					TwoDimenTransActivity1.class);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 初始化加载XML里的控件
	 */
	private void setupView() {
		StepTitleUtils.getInstance().initTitldStep(this,
				this.getResources().getStringArray(R.array.twodimen_generate));
		StepTitleUtils.getInstance().setTitleStep(1);
		queryList = TranDataCenter.getInstance().getAccountOutList();
		mAccListLv = (ListView) findViewById(R.id.lv_2dimen_select_acc);
		adapter = new SelectAccAdapter(SelectAccountActivity.this, queryList,
				-1);
		mAccListLv.setAdapter(adapter);
		mAccListLv.setOnItemClickListener(listener);
		// grayItemIv = (ImageView) findViewById(R.id.tran_imageViewright);
		nextBtn = (Button) findViewById(R.id.btn_next_2dimen_generate_select);
		nextBtn.setOnClickListener(nextListener);
	}

	/**
	 * 下一步 点击监听
	 */
	private OnClickListener nextListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (itemPosition < 0) {
				// 提示信息
				BaseDroidApp.getInstanse().createDialog(null,
						R.string.choose_account);
				return;
			}
			
			requestPsnAccountInfoEncrypt();
//			

		}
	};
	private void getData() {
		// accountName 用customName
		@SuppressWarnings("unchecked")
		Map<String, Object> data = (Map<String, Object>) BaseDroidApp
				.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA);
		customName = (String) data.get(Login.CUSTOMER_NAME);
	}
	/**
	 * 账户信息加密
	 */
	private void requestPsnAccountInfoEncrypt() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.PSN_ACCOUNT_INFO_ENCRYPT);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Tran.CUSTNAME, customName);
		map.put(Tran.CUSTACTNUM, accountNumber);
		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnAccountInfoEncryptCallBack");
	}

	/**
	 * 账户信息加密返回
	 * 
	 * @param resultObj
	 */
	public void requestPsnAccountInfoEncryptCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		if(StringUtil.isNullOrEmpty(result)){
			return;
		}
		String encryptStr = (String) result.get(Tran.ENCRYPTSTR);
		Intent intent = new Intent(SelectAccountActivity.this,
				GenerateDimenConfirmActivity.class);
		intent.putExtra(ConstantGloble.ACC_POSITION, encryptStr);
		startActivity(intent);
	}

	/**
	 * listview item点击事件
	 */
	private OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Map<String, Object> accOutInfoMap = queryList.get(position);
			TranDataCenter.getInstance().setAccOutInfoMap(accOutInfoMap);
			accountNumber = (String) accOutInfoMap.get(Comm.ACCOUNTNUMBER);
			itemPosition = position;
			adapter.dataChanaged(queryList, position);

		}
	};
}
