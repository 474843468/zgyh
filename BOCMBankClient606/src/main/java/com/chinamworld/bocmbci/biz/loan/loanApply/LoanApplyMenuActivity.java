package com.chinamworld.bocmbci.biz.loan.loanApply;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Loan;
import com.chinamworld.bocmbci.biz.loan.LoanBaseActivity;
import com.chinamworld.bocmbci.biz.loan.LoanDataCenter;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 贷款申请三级菜单
 * 
 * @author dxd
 * 
 */
public class LoanApplyMenuActivity extends LoanBaseActivity {

	private Context context = this;
	private LinearLayout tabcontent;// 主Activity显示
	private View view = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.loan_apply_loan_title);
		Button btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		btnRight.setVisibility(View.GONE);
		// 添加布局
		setLeftSelectedPosition("loan_4");
		init();
	}

	private void init() {
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		view = mInflater.inflate(R.layout.loan_apply_menu, null);
		tabcontent.addView(view);
		LinearLayout llyt_loan_repay = (LinearLayout) findViewById(R.id.llyt_loan_repay);
		LinearLayout llyt_loan_ChangeLoanRepayAccount = (LinearLayout) findViewById(R.id.llyt_loan_ChangeLoanRepayAccount);
		// 在线申请
		llyt_loan_repay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				requestPsnOnLineLoanProvinceQry();
//				Intent intent=new Intent(context, LoanApplyChooseActivity.class);
//				startActivity(intent);
			}
		});
		// 进度查询
		llyt_loan_ChangeLoanRepayAccount.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				007 PsnOnLineLoanAppliedQry 查询贷款记录列表
				Intent intent = new Intent(context, LoanApplyQueryInfoActivity.class);
				startActivity(intent);
			}
		});
	}
	/**贷款申请------ 请求省份信息 列表*/
	public void requestPsnOnLineLoanProvinceQry() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Loan.LOAN_PSNONLINELOANPROVINCE_QRY);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestPsnOnLineLoanProvinceQryCallBack");
	}

	/**
	 * 贷款申请------ 请求省份信息 回调
	 * @param resultObj
	 */
	public void requestPsnOnLineLoanProvinceQryCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody.getResult();
		
		List<Map<String, Object>> resultList = (List<Map<String, Object>>)resultMap.get("list");
		if (StringUtil.isNullOrEmpty(resultList)) {
			BaseHttpEngine.dissMissProgressDialog();
			return;
		}
//		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.LOAN_RESAPPLYPROVINCE, resultList);
		LoanDataCenter.getInstance().setResApplyprovinceList(resultList);
		// 通讯结束,关闭通讯框
		BaseHttpEngine.dissMissProgressDialog();
		Intent intent=new Intent(context, LoanApplyChooseActivity.class);
		startActivity(intent);
	}
	
	
}
