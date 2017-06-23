package com.chinamworld.bocmbci.biz.epay;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.constants.WithoutCardContants;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.biz.epay.myPayService.withoutcard.WithoutCardActivity;
import com.chinamworld.bocmbci.biz.epay.myPayService.withoutcard.setPaymentAcc.SetPaymentAccSelectActivity;
import com.chinamworld.bocmbci.biz.epay.observer.PubHttpObserver;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.PublicTools;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * @ClassName: EPaySecondMainActivity
 * @Description:
 * @author sunh
 * @date 2015-8-31 下午03:39:03
 * 银联跨行支付业务开通关闭
 * 
 *      
 *       
 */
public class EPaySecondMainActivity  extends EPayBaseActivity{
	
	private PubHttpObserver httpObserver;
	private Button ib_top_right_btn;
	private RelativeLayout rl_ele_pay;//银联跨行无卡自助消费
	private RelativeLayout rl_wc_pay;//银联跨行代收
	
	private List<Object> allAccountList;
	private List<Object> dredgedAccountList;
	private List<Object> unDredgedAccountList;
	private int curAccountIndex = 0;
	private int accountListSize;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		httpObserver = PubHttpObserver.getInstance(this, PubConstants.CONTEXT_WITHOUT_CARD);
		super.setType(0);
		super.setShowBackBtn(true);
		super.setTitleName(PubConstants.TITLE_SECOND_MAIN);
		super.setContentView(LayoutInflater.from(this).inflate(R.layout.epay_menu2, null));
		super.onCreate(savedInstanceState);
		ib_top_right_btn = (Button) findViewById(R.id.ib_top_right_btn);
		ib_top_right_btn.setVisibility(View.GONE);
		TextView tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setTextSize(getResources().getDimensionPixelSize(R.dimen.textsize_one_five)/getResources().getDisplayMetrics().density);
		// 初始化当前页

		initDisplay();
	}



	private void initDisplay() {
		rl_ele_pay = (RelativeLayout) findViewById(R.id.rl_ele_pay);
		rl_ele_pay.setOnClickListener(menuClickLinstener);
		rl_wc_pay = (RelativeLayout) findViewById(R.id.rl_wc_pay);
		rl_wc_pay.setOnClickListener(menuClickLinstener);
	}
	private View.OnClickListener menuClickLinstener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			if (!BaseDroidApp.getInstanse().isLogin()) {// 没有登录就跳转到登录页面
//				Intent intent = new Intent(EPaySecondMainActivity.this, LoginActivity.class);
//				startActivity(intent);
				BaseActivity.getLoginUtils(EPaySecondMainActivity.this).exe(new LoginTask.LoginCallback() {

					@Override
					public void loginStatua(boolean isLogin) {

					}
				});
				return;
			}

			switch (v.getId()) {
			case R.id.rl_ele_pay:
				serviceType=1;
				TransContext.getBomContext().clear(null);
				dredgedAccountList = new ArrayList<Object>();
				getWithoutCardStatus();
				break;
			case R.id.rl_wc_pay:
				serviceType=2;
				TransContext.getWithoutCardContext().clear(null);
				dredgedAccountList = new ArrayList<Object>();
				getWithoutCardStatus();
				break;

			}
		}
	};

	/**
	 * 获取银联跨行无卡支付开通状态
	 */
	public void getWithoutCardStatus() {
		BiiHttpEngine.showProgressDialog();
		List<Object> accTypes = new ArrayList<Object>();
		accTypes.add("119");
		httpObserver.req_queryAllAccount(accTypes, "queryWithCardAllAccountCallback");
	}
	
	/**
	 * 查询无卡支付所有账户
	 * 
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void queryWithCardAllAccountCallback(Object resultObj) {
		Object result = httpObserver.getResult(resultObj);
		allAccountList = EpayUtil.getList(result);
		TransContext.getWithoutCardContext().setData(PubConstants.CONTEXT_FIELD_ALL_ACCLIST, allAccountList);
		accountListSize = allAccountList.size();

		if (0 == accountListSize) {
			BiiHttpEngine.dissMissProgressDialog();

			// StringBuffer sb = new
			// StringBuffer("您尚未关联任何账户!是否立即关联新账户？\n支持开通银联跨行无卡支付功能的卡类型有：");
			StringBuffer sb=new StringBuffer();
			if(serviceType==1){
				sb = new StringBuffer(getText(R.string.no_open_boc_payment_is_correlation1).toString());
			}else if(serviceType==2){
				sb = new StringBuffer(getText(R.string.no_open_boc_payment_is_correlation2).toString());
			}
			
//			sb.append("“").append(LocalData.AccountType.get("119")).append("”");

			BaseDroidApp.getInstanse().showErrorDialog(null, sb.toString(), new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					BaseDroidApp.getInstanse().dismissErrorDialog();
					switch (PublicTools.getInt(v.getTag(), 0)) {
					case CustomDialog.TAG_SURE:
//						goRelevanceAccount();
						finish();
						break;
					}
				}
			});
			return;
		}

		queryWithoutCardPayStatusByIndex(this.curAccountIndex);
	}
	/**
	 * 查询账户是否开通无卡支付
	 * 获取服务开通状态及客户信息sunh
	 * @param curAccountIndex
	 */
	@SuppressWarnings("unchecked")
	public void queryWithoutCardPayStatusByIndex(int curAccountIndex) {
		Map<Object, Object> account = EpayUtil.getMap(allAccountList.get(curAccountIndex));
		String accountId = EpayUtil.getString(account.get(PubConstants.METHOD_QUERY_ALL_ACCOUNT_FIELD_ACCOUNT_ID), "");
		httpObserver.req_queryWithoutCardPayStatus(accountId, "queryWithoutCardPayStatus");
	}
	/**
	 * 查询账户开通无卡支付状态回调方法
	 * 获取服务开通状态及客户信息回调方法sunh
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void queryWithoutCardPayStatus(Object resultObj) {
		Object result = httpObserver.getResult(resultObj);
		Map<Object, Object> map = EpayUtil.getMap(result);
		String status = EpayUtil.getString(map.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_STATUS), "");
		String collectStatus= EpayUtil.getString(map.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_COLLECTSTATUS), "");
		String quota = EpayUtil.getString(map.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_QUOTA), "");
		String collectQuota = EpayUtil.getString(map.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_COLLECTQUOTA), "");
		String custName = EpayUtil.getString(map.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_CUSTNAM), "");
//		String identityType = EpayUtil.getString(map.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_IDENTITYTYPE),
//				"");
//		String identityNum = EpayUtil.getString(map.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_IDENTITYNUM),
//				"");
		String acctNum = EpayUtil.getString(map.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_ACCNUM), "");
//		String cif = EpayUtil.getString(map.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_CIF), "");
//		String phone = EpayUtil.getString(map.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_PHONE), "");

		Map<Object, Object> account = EpayUtil.getMap(allAccountList.get(curAccountIndex));
		if(serviceType==1){
			account.put(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_QUOTA, quota);	
			account.put(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_STATUS, status);
		}else if(serviceType==2){
			account.put(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_COLLECTQUOTA, collectQuota);	
			account.put(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_COLLECTSTATUS, collectStatus);
		}
		account.put(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_CUSTNAM, custName);
//		account.put(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_IDENTITYTYPE, identityType);
//		account.put(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_IDENTITYNUM, identityNum);
		account.put(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_ACCNUM, acctNum);
//		account.put(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_CIF, cif);
//		account.put(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_PHONE, phone);
		if(serviceType==1){
			if (WithoutCardContants.METHOD_IS_OPEN_NC_PAY_YES.equals(status)) {
				dredgedAccountList.add(account);
			}
		}else if(serviceType==2){
			if (WithoutCardContants.METHOD_IS_OPEN_NC_PAY_YES.equals(collectStatus)) {
				dredgedAccountList.add(account);
			}
		}
		++this.curAccountIndex;
		if (this.curAccountIndex < this.accountListSize) {
			queryWithoutCardPayStatusByIndex(this.curAccountIndex);
		} else {
			isOpenWithoutCardPaymentService();
			this.curAccountIndex = 0;
		}
	}
	
	/**
	 * 判断是否开通无卡支付
	 */
	private void isOpenWithoutCardPaymentService() {
		unDredgedAccountList = EpayUtil.filterAccList(allAccountList, dredgedAccountList);
		boolean isOpen = false;
		// 如果已开通账户数大于0
		if (dredgedAccountList.size() > 0) {
			isOpen = true;
			// 保存已开通账户
			TransContext.getWithoutCardContext().setData(PubConstants.CONTEXT_FIELD_DREDGED_LIST, dredgedAccountList);
		}
		TransContext.getWithoutCardContext().setData(PubConstants.CONTEXT_FIELD_UN_DREDGED_ACCOUNT_LIST,
				unDredgedAccountList);
		BiiHttpEngine.dissMissProgressDialog();

		TransContext.getWithoutCardContext().setRightButtonClick(false);
		if (isOpen) {
			Intent intent = new Intent(this, WithoutCardActivity.class);
			this.startActivity(intent);
		} else {
			String message = null;
			if(serviceType==1){
				message="您还没开通银联在线支付功能账户，是否立即开通？";	
			}else if(serviceType==2){
				message="您还没开通银联跨行代扣功能账户，是否立即开通？";		
			}
			BaseDroidApp.getInstanse().showErrorDialog(null, message, new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					BaseDroidApp.getInstanse().dismissErrorDialog();
					switch (PublicTools.getInt(v.getTag(), 0)) {
					case CustomDialog.TAG_SURE:
						// 测试代码
						// TransContext.getWithoutCardContext().clear(PubConstants.CONTEXT_FIELD_UN_DREDGED_ACCOUNT_LIST);
						if (0 == TransContext.getWithoutCardContext()
								.getList(PubConstants.CONTEXT_FIELD_UN_DREDGED_ACCOUNT_LIST).size()) {
							// BiiHttpEngine.dissMissProgressDialog();
							StringBuffer sb=new StringBuffer();
							if(serviceType==1){
								sb = new StringBuffer(getText(
										R.string.no_open_boc_payment_is_correlation1).toString());
							}else if(serviceType==2){
								sb = new StringBuffer(getText(
										R.string.no_open_boc_payment_is_correlation2).toString());
							}
//							String temp = "";
//							for (Object accTypeStr : EpayUtil.getAccTypeList()) {
//								String accTypeName = LocalData.AccountType.get(accTypeStr);
//								if (!temp.equals(accTypeName)) {
//									temp = accTypeName;
//									sb.append("“").append(accTypeName).append("”、");
//								}
//							}
							BaseDroidApp.getInstanse().showErrorDialog(null, sb.toString(), new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									BaseDroidApp.getInstanse().dismissErrorDialog();
									switch (PublicTools.getInt(v.getTag(), 0)) {
									case CustomDialog.TAG_SURE:
//										goRelevanceAccount();
										finish();
										break;
									}
								}
							});

							return;
						}

						Intent intent = new Intent(EPaySecondMainActivity.this, SetPaymentAccSelectActivity.class);
						startActivity(intent);
						break;
					}
				}
			});
		}
	}
}
