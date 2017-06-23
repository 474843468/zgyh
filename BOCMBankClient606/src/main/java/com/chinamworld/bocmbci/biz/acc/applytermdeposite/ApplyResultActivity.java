package com.chinamworld.bocmbci.biz.acc.applytermdeposite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Dept;
import com.chinamworld.bocmbci.biz.BusinessModelControl;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.biz.acc.AccDataCenter;
import com.chinamworld.bocmbci.biz.dept.savereg.SaveRegularActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class ApplyResultActivity extends AccBaseActivity implements
		OnClickListener {

	/**主布局*/
	private View mainView;
	/**账户绑定介质账号*/
	private TextView tv_accnum;
	/**账户绑定介质类型*/
	private TextView tv_acctype;
	/**开户类型*/
	private TextView tv_accinfo;
	/**账号*/
	private TextView tv_account;
	private String account;
	/**进行自主关联*/
	private LinearLayout ll_relevance,ll_relevance_new;
	private TextView tv_relevance;
	/**账户用途*/
	private TextView tv_account_purpose;
	/**账户开立原因*/
	private LinearLayout ll_account_open_reason;
	private TextView tv_open_reason;
	private TextView tv_account_open_reason;
	/**完成按钮*/
	private Button btnFinish;
	/**开户类型信息*/
	private String info;
	/**选中的借记卡信息*/
	private Map<String,Object> accInfo;
	private StringBuffer sbAccountPurpose;
	private StringBuffer sbAccountOpenReason;
	private List<String> purposeList;
	private List<String> reasonList;
	/**开立账户国籍*/
	private Map<String,Object> countryCode;
	private String code;
	/** 存款管理申请账户标示 */
	private int interestRateFlag;
	/** 资金管理标识 */
	private Boolean isManageFlag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**设置标题*/
		setTitle(this.getString(R.string.acc_apply_title));
		/**添加主布局*/
		mainView=addView(R.layout.acc_apply_result);
		back.setVisibility(View.GONE);
		init();
		interestRateFlag = this.getIntent().getIntExtra(Dept.APPLICATION_ACCOUNT_FLAG, 0);
	}
	
	/**
	 * 初始化布局控件
	 */
	public void init(){
		info=getIntent().getStringExtra("info");
		sbAccountPurpose=new StringBuffer();
		sbAccountOpenReason=new StringBuffer();
		purposeList=AccDataCenter.getInstance().getPurposeList();
		reasonList=AccDataCenter.getInstance().getReasonList();
		countryCode=AccDataCenter.getInstance().getCountryCode();
		code=(String) countryCode.get(Acc.ACC_QUERY_COUNTRYCODE);
		tv_accnum=(TextView) mainView.findViewById(R.id.tv_accnum);
		tv_acctype=(TextView) mainView.findViewById(R.id.tv_acctype);
		tv_accinfo=(TextView) mainView.findViewById(R.id.tv_accInfo);
		tv_account=(TextView) mainView.findViewById(R.id.tv_account);
		ll_relevance=(LinearLayout) mainView.findViewById(R.id.ll_relevance);
		ll_relevance_new=(LinearLayout) mainView.findViewById(R.id.ll_relevance_new);
		tv_relevance=(TextView)mainView.findViewById(R.id.tv_relevance);
		tv_account_purpose=(TextView) mainView.findViewById(R.id.tv_account_purpose);
		accountValue(purposeList, tv_account_purpose, sbAccountPurpose);
		ll_account_open_reason=(LinearLayout) mainView.findViewById(R.id.ll_account_open_reason);
		if(!code.equals(CHINA)){
			ll_account_open_reason.setVisibility(View.VISIBLE);
		}
		tv_open_reason=(TextView) mainView.findViewById(R.id.tv_open_reason);
		showTitle(tv_open_reason, code);
		tv_open_reason.setEllipsize(TruncateAt.MIDDLE);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tv_open_reason);
		tv_account_open_reason=(TextView) mainView.findViewById(R.id.tv_account_open_reason);
		accountValue(reasonList,tv_account_open_reason,sbAccountOpenReason);
		btnFinish=(Button) mainView.findViewById(R.id.btnFinish);
		accInfo=AccDataCenter.getInstance().getChooseBankAccount();
		tv_accnum.setText(StringUtil.getForSixForString(accInfo.get(Acc.ACC_ACCOUNTNUMBER_RES).toString()));
		tv_acctype.setText(AccBaseActivity.bankAccountType.get(accInfo.get(Acc.ACC_ACCOUNTTYPE_RES).toString()));
		
		isManageFlag = AccDataCenter.getInstance().isManageFlag();
		if (isManageFlag) {
			tv_accinfo.setText(this.getString(R.string.acc_choose_fixed));
		} else {
			tv_accinfo.setText(info);
		}
		account=AccDataCenter.getInstance().getApplyResultMap()
				.get(Acc.ACC_APPLY_RESULT_ACCOUNTNUM).toString();
		tv_account.setText(account);
		String LinkStatus=(String) AccDataCenter.getInstance().getApplyResultMap()
				.get(Acc.ACC_APPLY_RESULT_LINKSTATUS);
		if(LinkStatus.equals("0")){
			//关联网银失败
//			ll_relevance.setVisibility(View.VISIBLE); 屏蔽自助关联
			ll_relevance_new.setVisibility(View.VISIBLE);// 恢复 注释此行  打开上一行
			tv_relevance.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					Intent intent=new Intent(ApplyResultActivity.this,AccInputRelevanceAccountActivity.class);
//					intent.putExtra("account",accInfo.get(Acc.ACC_ACCOUNTNUMBER_RES).toString());
//					startActivity(intent);
					Map<String, Object> mapData = new HashMap<String, Object>();
					mapData.put("account",accInfo.get(Acc.ACC_ACCOUNTNUMBER_RES));
					if(BusinessModelControl.gotoAccRelevanceAccount(ApplyResultActivity.this, -1, mapData)){
						ActivityTaskManager.getInstance().removeAllActivity();
						finish();	
					}
					
					
				}
			});
			
			
		}
		btnFinish.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if (interestRateFlag == APPLICATION_ACCOUNT) { // 存款管理申请账户
			ActivityTaskManager.getInstance().removeAllActivity();
			Intent intent=new Intent(this,SaveRegularActivity.class);
			startActivityForResult(intent, ConstantGloble.ACTIVITY_REQUEST_ADDNEWACCC_CODE);
			finish();
		} else if (isManageFlag) {
			String resultState = (String) AccDataCenter.getInstance().getApplyResultMap()
					.get(Acc.ACC_APPLY_RESULT_APPLYSTATUS);
			//申请账户成功时，跳转到资产管理相关界面
			if (resultState.equals("1")) {
				setResult(10023);
				finish();
			}
			
		} else {
			ActivityTaskManager.getInstance().removeAllActivity();
			Intent intent=new Intent(this,ApplyTermDepositeActivity.class);
			startActivity(intent);
			finish();
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
}
