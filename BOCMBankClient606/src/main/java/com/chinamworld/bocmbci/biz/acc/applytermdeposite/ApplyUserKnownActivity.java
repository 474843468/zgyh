package com.chinamworld.bocmbci.biz.acc.applytermdeposite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.log.LogGloble;
/**
 * 申请定期活期账户	用户须知页面
 * @author Administrator
 *
 */
public class ApplyUserKnownActivity extends AccBaseActivity implements
		OnClickListener {
	
	/**主界面*/
	private View mainView;
	/**确定按钮*/
	private Button btnSure;
	/** 存款管理申请账户标示 */
	private int interestRateFlag;
	/** 资产管理 */
	private boolean isManageFlag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**为界面标题赋值*/
		setTitle(R.string.acc_apply_title);
		btn_right.setText(R.string.close);
		btn_right.setOnClickListener(closeLis);
		/**添加布局*/
		mainView=addView(R.layout.acc_apply_user_known);
		
		interestRateFlag = this.getIntent().getIntExtra(Dept.APPLICATION_ACCOUNT_FLAG, 0);
		isManageFlag = AccDataCenter.getInstance().isManageFlag();
		requestPsnCountryCodeQuery();
	}
	/**
	 * 关闭按钮的监听事件
	 */
	private OnClickListener closeLis=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			finish();
		}
	};
	/**
	 * 初始化布局控件
	 */
	public void init(){
		btnSure=(Button) mainView.findViewById(R.id.btnConfirm);
		btnSure.setOnClickListener(this);
	}
	/**
	 * 确定按钮的监听事件
	 */
	@Override
	public void onClick(View v) {
		List<Map<String,Object>> count=AccDataCenter.getInstance().getBankAccountList();
		LogGloble.i("Myself","数"+ count.size());
		if(count.size()>=20){
			BaseDroidApp.getInstanse().showMessageDialog(
					this.getString(R.string.acc_open_more), new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							finish();
						}
					});
			return;
		}
		if (interestRateFlag == APPLICATION_ACCOUNT) { // 存款管理申请账户
			Intent intent = new Intent(this,ApplyChooseMsgActivity.class);
			intent.putExtra(Dept.APPLICATION_ACCOUNT_FLAG , interestRateFlag);
			startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
		} else if (isManageFlag){
			Intent intent=new Intent(this,ApplyChooseMsgActivity.class);
			startActivityForResult(intent, 10023);
			finish();
		} else {
			Intent intent=new Intent(this,ApplyChooseMsgActivity.class);
			startActivity(intent);
			finish();
		}
	}
	/**
	 * 查询个人客户国籍信息
	 */
	public void requestPsnCountryCodeQuery() {
		BiiRequestBody biiRequestBody=new BiiRequestBody();
		biiRequestBody.setMethod(Acc.ACC_PSNCOUNTRYCODEQUERY);
		Map<String, Object> parms=new HashMap<String, Object>();
		parms.put(Acc.ACC_QUERY_ACCOUNTID, AccDataCenter.getInstance().getChooseBankAccount()
				.get(Acc.ACC_ACCOUNTID_RES));
		biiRequestBody.setParams(parms);
		HttpManager.requestBii(biiRequestBody, this, "psnCountryCodeQueryCallBack");
	}
	/**
	 * 查询个人客户国籍返回信息
	 */
	public void psnCountryCodeQueryCallBack(Object resultObj){
		BiiResponse biiResponse=(BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys=biiResponse.getResponse();
		BiiResponseBody biiResponseBody=biiResponseBodys.get(0);
		Map<String, Object> countryCode= (Map<String, Object>) biiResponseBody.getResult();
		AccDataCenter.getInstance().setCountryCode(countryCode);
		init();
	}
}
