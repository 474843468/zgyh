package com.chinamworld.bocmbci.biz.sbremit.mysbremit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.SBRemit;
import com.chinamworld.bocmbci.biz.investTask.BocInvestTask;
import com.chinamworld.bocmbci.biz.investTask.IAction;
import com.chinamworld.bocmbci.biz.sbremit.SBRemitBaseActivity;
import com.chinamworld.bocmbci.biz.sbremit.SBRemitDataCenter;
import com.chinamworld.bocmbci.biz.sbremit.SBRemitSpecialTipsActivity;
import com.chinamworld.bocmbci.biz.sbremit.mysbremit.adapter.SBRemitAccListAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

public class ChooseAccountActivity extends SBRemitBaseActivity implements
		OnClickListener {

	private static final String TAG = "ChooseAccountActivity";
	/** 账户的证件号类型 */
	private String identityType;
	/** 账户的证件号 */
	private String identityNumber;
	public static final int REQUEST_EXCHANGE_IS_OPEN = 10002;
	/** 账户列表 */
	private ListView cardList;
	/** 下一步按钮 */
	private Button nextBtn;
	/** 列表adapter */
	private SBRemitAccListAdapter sbremitAccListAdapter;
	/** 当前选中账户的位置 */
	private int mCurrentPosition = -1;
	/** 账户标识 */
	private String acountId;
	/** 筛选后的数据列表 */
	List<Map<String, Object>> otherData;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.i(TAG, "onCreate");
		// P502个人体验优化
		initViews();
		BiiHttpEngine.showProgressDialogCanGoBack();
		//判断是否开通投资理财
		BocInvestTask task = BocInvestTask.getInstance(this);
		registActivityEvent(task);
		task.doTask(new IAction(){

			@Override
			public void SuccessCallBack(Object param) {
				// TODO Auto-generated method stub
				BaseHttpEngine.showProgressDialogCanGoBack();
				requestExchangeIsOpen();
			}
			
		},null);
		
	}

	public void requestExchangeIsOpenCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
			return;
		}
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		boolean isOpen = Boolean.parseBoolean((String) resultMap
				.get(SBRemit.STATUS));
		if (isOpen) {
			requestSbremitCommConversationId();
			BaseHttpEngine.showProgressDialogCanGoBack();
		} else {
			openSBRemitSpecialTips();
		}

	}

	protected void openSBRemitSpecialTips() {
		Intent intent = new Intent();
		intent.setClass(this, SBRemitSpecialTipsActivity.class);
		startActivityForResult(intent, REQUEST_EXCHANGE_IS_OPEN);
	}

	private void initViews() {
		// 为界面标题赋值
		setTitle(this.getString(R.string.my_sbremit));
		// 添加布局
		View view = addView(R.layout.acc_financeic_trans_choose);
		btn_right.setVisibility(View.INVISIBLE);
		back.setOnClickListener(this);

		setLeftSelectedPosition("sbRemit_1");
		((TextView) findViewById(R.id.tv_financeic_choose_title))
				.setText(getString(R.string.acc_financeic_choose_title_null_new));
		((TextView) findViewById(R.id.alert_info)).setVisibility(View.VISIBLE);
		cardList = (ListView) this.findViewById(R.id.acc_accountlist);
		cardList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// P502修改,点击选中再点击取消
				if (((SBRemitAccListAdapter.ViewHolder) view.getTag()).selected) {
					sbremitAccListAdapter.setSelectedPosition(position);
					mCurrentPosition = -1;
				} else {
					sbremitAccListAdapter.setSelectedPosition(position);
					mCurrentPosition = position;
					SBRemitDataCenter.getInstance().setAccInfo(
							otherData.get(mCurrentPosition));
					acountId = (String) otherData.get(mCurrentPosition).get(
							Comm.ACCOUNT_ID);
					String acountType = (String) SBRemitDataCenter
							.getInstance().getAccInfo().get(Comm.ACCOUNT_TYPE);
					if (SBRemitDataCenter.dissMissAcc.contains(acountType)) {
						DENGZHI = "";
						MEIYUAN = "人民币";
					} else {
						DENGZHI = "等值";
						MEIYUAN = "美元";
					}
				}
//				AccId = otherData.get(position).get("identityType").toString();
//				identityNumber = otherData.get(position).get("identityNumber")
//						.toString();
				identityNumber=(String) resultAccMap.get(SBRemit.IDENTITYNUMBER);
				identityType=(String) resultAccMap.get(SBRemit.IDENTITYTYPE);
			}
		});
		
		nextBtn = (Button) this.findViewById(R.id.btnNext);
		nextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mCurrentPosition < 0) 
				{
					BaseDroidApp.getInstanse().showInfoMessageDialog(
							ChooseAccountActivity.this.getString(R.string.choose_your_exchange_acc));
					return;
				}
				if (identityType.equals("01")||identityType.equals("03") || identityType.equals("47")
						|| identityType.equals("48")|| identityType.equals("49")) {
						excuseNext();
				}  else {
					BaseDroidApp
							.getInstanse()
							.showInfoMessageDialog(
									"您开户所用身份证件暂不支持在电子银行渠道进行结汇购汇交易，您可携带本人有效身份证件及银行卡/存折/现金至我行任意网点办理结汇购汇业务。感谢您的使用。 ");
				}

			}
			
		});

	}

	/**
	 * @Title: excuseNext
	 * @Description: 执行下一步操作
	 * @param
	 * @return void
	 */
	private void excuseNext() {
		if (!StringUtil.isNullOrEmpty(otherData)) {
			
			requestForAccRemain();
		}
	}

	/**
	 * @Title: requestForAccRemain
	 * @Description: 请求账户余额列表
	 * @param
	 * @return void
	 */
	public void requestForAccRemain() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(SBRemit.SBREMIT_ACC_REMAIN);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put("accountId", acountId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this,
				"requestForAccRemainCallBack");
	}

	/**
	 * @Title: requestForAccRemainCallBack
	 * @Description: 请求账户余额列表回调
	 * @param @param resultObj
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	public void requestForAccRemainCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> accRemainList = (List<Map<String, Object>>) biiResponseBody
				.getResult();
		SBRemitDataCenter.getInstance().accRemainList = accRemainList;
		if (StringUtil
				.isNullOrEmpty(SBRemitDataCenter.getInstance().accRemainList))
			return;
		Intent intent = new Intent(this, AccountRemainActivity.class);// 账户余额页
		intent.putExtra("identityType", identityType);
		intent.putExtra("identityNumber", identityNumber);
		startActivityForResult(intent, SREMIT_OPERATION);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_EXCHANGE_IS_OPEN:
			switch (resultCode) {
			case RESULT_OK:
				// initViews();
				requestSbremitCommConversationId();
				break;
			case RESULT_CANCELED:
				finish();
				break;
			default:
				break;
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void communicationCallBack(int flag) {
		switch (flag) {
		case GET_ACCOUNT_IN_CALLBACK:
			BaseHttpEngine.dissMissProgressDialog();
			otherData = new ArrayList<Map<String, Object>>();
			otherData.addAll(accList);

			setListView(otherData);
			if (StringUtil.isNullOrEmpty(sbremitAccListAdapter
					.getAccList(otherData))) {
				BaseDroidApp.getInstanse().showMessageDialog(
						"因您名下的关联账户中无可用的活一本或主账户是活一本的借记卡,故无法使用该功能服务。",
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								finish();
							}
						});
			}
			break;
		default:
			break;
		}
	}

	/**
	 * @Title: setListView
	 * @Description: 填充视图的数据
	 * @param
	 * @return void
	 */
	private void setListView(List<Map<String, Object>> accountList) {
		if (sbremitAccListAdapter == null) {
			sbremitAccListAdapter = new SBRemitAccListAdapter(this, accountList);
			cardList.setAdapter(sbremitAccListAdapter);
		} else {
			sbremitAccListAdapter.setData(accountList);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:
			setResult(RESULT_CANCELED);
			ActivityTaskManager.getInstance().removeAllActivity();
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 请求CommconversationId返回
	 * 
	 * @param resultObj
	 */
	public void requestSbremitCommConversationIdCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String commConversationId = (String) biiResponseBody.getResult();
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.CONVERSATION_ID, commConversationId);
		communicationCallBack(RESULT_OK);
		requestForCardList();
	}

}
