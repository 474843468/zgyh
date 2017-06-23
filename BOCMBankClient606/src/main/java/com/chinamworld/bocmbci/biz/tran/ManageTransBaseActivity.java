package com.chinamworld.bocmbci.biz.tran;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 转账管理基类，初始化左边二级菜单、底部菜单栏和头部按钮，处理通信
 * 
 * @author
 * 
 */
public class ManageTransBaseActivity extends TranBaseActivity {

	public final static int TAG_CLOSE = 0;
	public final static int TAG_PWD = 1;
	public final static int TAG_SUBMIT = 2;
	public final static int TAG_EXIT = 3;
	public final static int TAG_CONFIRM = 4;
	public final static int TAG_RETRY = 5;
	public final static int TAG_SURE = 6;
	public final static int TAG_CANCLE = 7;
	public final static int TAG_BACK = 8;

	public final static int TAG_RELA_ACC_TRAN = 9;
	public final static int TAG_COMMON_RECEIVER_TRAN = 10;
	public final static int TAG_TRAN_BOCMOBILE = 11;
	public final static int TAG_CONFIRM_BOC = 12;
	/** 预约日期查询 */
	protected static final int PRE_DATE = 1;
	/** 执行日期查询 */
	protected static final int EXE_DATE = 0;

	public LayoutInflater mInflater;
	/** 加载布局 */
	public LinearLayout tabcontent = null;
	/** 头部返回按钮 */
	protected Button back = null;
	/** 转账预约管理 类型 */
	public String manageTransQueryTypeStr = "";
	/**401 显示失败原因的状态*/
	public String showFailReson1 = "B";
	public String showFailReson2 = "F";
	public String showFailReson3 = "12";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);
		initPulldownBtn();
		initFootMenu();
		initLeftSideList(this, LocalData.tranManagerLeftList);
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		mInflater = LayoutInflater.from(this);
		// 头部左边返回按钮
		// mainBtn = (Button) findViewById(R.id.ib_top_right_btn);
		back = (Button) findViewById(R.id.ib_back);
		clickTopLeftBtn();

	}

	/**
	 * 头部左边返回按钮
	 */
	private void clickTopLeftBtn() {
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ManageTransBaseActivity.this.onBackPressed();
			}
		});
	}

	// //////////////////////////////////////////////////////////////////////////PsnTransManagePayeeDeletePayee删除收款人
	/**
	 * 修改收款人手机号 请求conversationId
	 */
	private String payeeId = "";

	public void requestManagePayeeConversationId(String payeeId) {
		this.payeeId = payeeId;

		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.CONVERSATION_ID_API);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"managePayeeConversationIdCallBack");
	}

	/**
	 * 请求conversation 回调
	 * 
	 * @param resultObj
	 */
	public void managePayeeConversationIdCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String commConversationId = (String) biiResponseBody.getResult();
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.CONVERSATION_ID, commConversationId);

		if (StringUtil.isNullOrEmpty(commConversationId)) {
			return;
		}

		requestForGetTokenId();
	}

	/**
	 * 获取tokenId
	 */
	public void requestForGetTokenId() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.GETTOKENID);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, "getTokenIdCallBack");
	}

	/**
	 * 获取tokenId的数据得到tokenId
	 * 
	 * @return tokenId
	 */
	public void getTokenIdCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String tokenId = (String) biiResponseBody.getResult();
		requestForDeletePayee(tokenId);
	}

	/**
	 * PsnTransManagePayeeDeletePayee删除收款人 req
	 */
	public void requestForDeletePayee(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.DELETEPAYEE);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));

		// Map<String,String>preDateAndExeTypeMap = (Map<String, String>)
		// BaseDroidApp.getInstanse().getBizDataMap().get("preDateAndExeTypeMap");
		// String prAndExeQueryFlag =
		// preDateAndExeTypeMap.get("preAndExeQueryType");
		String[] payees = { payeeId };
		Map<String, Object> map = new HashMap<String, Object>();
		// map.put(Tran.MANAGE_PAYEEDEL_PAYEEID_REQ,payeeId);
		map.put(Tran.MANAGE_PAYEEDEL_PAYEEID_REQ, payees);
		map.put(Tran.MANAGE_CANCLEPREDATE_TOKEN_REQ, tokenId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "deletePayeeCallBack");
	}

	/**
	 * PsnTransManagePayeeDeletePayee删除收款人 res
	 */
	public void deletePayeeCallBack(Object resultObj) {
		// BaseHttpEngine.dissMissProgressDialog();
		//
		// BiiResponse biiResponse = (BiiResponse) resultObj;
		// List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		// BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

		CustomDialog.toastShow(ManageTransBaseActivity.this,
				ManageTransBaseActivity.this
						.getString(R.string.del_payee_success));
		setResult(105);
		finish();
	}

}
