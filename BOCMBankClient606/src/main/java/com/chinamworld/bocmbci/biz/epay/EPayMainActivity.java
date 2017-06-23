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

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.constants.TreatyConstants;
import com.chinamworld.bocmbci.biz.epay.constants.WithoutCardContants;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.biz.epay.myPayService.bomobile.BankOfMobileActivity;
import com.chinamworld.bocmbci.biz.epay.myPayService.bomobile.adapter.MenuListAdapter;
import com.chinamworld.bocmbci.biz.epay.myPayService.bomobile.servOpen.ServOpenAgreementActivity;
import com.chinamworld.bocmbci.biz.epay.myPayService.treaty.TreatyActivity;
import com.chinamworld.bocmbci.biz.epay.myPayService.treaty.setMerchantRelation.SelecteMerchantActivity;
import com.chinamworld.bocmbci.biz.epay.myPayService.withoutcard.WithoutCardActivity;
import com.chinamworld.bocmbci.biz.epay.myPayService.withoutcard.setPaymentAcc.SetPaymentAccSelectActivity;
import com.chinamworld.bocmbci.biz.epay.observer.PubHttpObserver;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.PublicTools;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * @ClassName: EPayMainActivity
 * @Description:
 * @author lql
 * @date 2013-9-11 下午05:27:03
 *
 *
 *       XXX 标签标记 acclistFlag
 *       申请虚拟卡-虚拟卡申请成功-点击开通手机银行支付不会传送acclistFlag，更加acclistFlag来区分显示虚拟卡还是卡列表
 */
public class EPayMainActivity extends EPayBaseActivity {
	// 电子支付菜单页
	private View epay_menu;
//	// 菜单列表视图
//	private ListView menu_list;
	// 菜单列表适配器
	private MenuListAdapter menu_adapter;
	// 菜单内容
	private List<Map<String, Object>> menu_list_content;

	private PubHttpObserver httpObserver;

	private Button ib_top_right_btn;

	private int curAccountIndex = 0;
	private int accountListSize;

	private RelativeLayout rl_ele_pay;
	private RelativeLayout rl_wc_pay;
	private RelativeLayout rl_treaty_pay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		httpObserver = PubHttpObserver.getInstance(this, PubConstants.CONTEXT_WITHOUT_CARD);
		super.setType(0);
		super.setShowBackBtn(true);
		super.setTitleName(PubConstants.TITLE_MAIN);
		super.setContentView(LayoutInflater.from(this).inflate(R.layout.epay_menu, null));
		super.onCreate(savedInstanceState);
		ib_top_right_btn = (Button) findViewById(R.id.ib_top_right_btn);
		ib_top_right_btn.setVisibility(View.GONE);
		// 初始化当前页
		initDisplay();
	}

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
	 * 获取电子支付开通状态
	 */
	public void getBomCardStatus() {
		BiiHttpEngine.showProgressDialog();
		httpObserver.req_bomServiceOpenStatus("bomServiceOpenStatus");
	}

	/**
	 * 获取协议支付开通状态
	 */
	private void getTreatyStatus() {
		BiiHttpEngine.showProgressDialog();
		requestCommConversationId();
	}

	/**
	 * 获取conversationId回调方法
	 */
	@Override
	public void requestCommConversationIdCallBack(Object resultObj) {
		httpObserver.setConversationId(EpayUtil.getString(httpObserver.getResult(resultObj), ""));
		// 查询已开通协议支付的商户
		httpObserver.req_queryTreatyRelations(0, true, "queryTreatyRelationsCallback");
	}

	private void initDisplay() {

		rl_ele_pay = (RelativeLayout) findViewById(R.id.rl_ele_pay);
		rl_ele_pay.setOnClickListener(menuClickLinstener);
		rl_wc_pay = (RelativeLayout) findViewById(R.id.rl_wc_pay);
		rl_wc_pay.setOnClickListener(menuClickLinstener);
		rl_treaty_pay = (RelativeLayout) findViewById(R.id.rl_treaty_pay);
		rl_treaty_pay.setOnClickListener(menuClickLinstener);
	}

	private View.OnClickListener menuClickLinstener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			if (!BaseDroidApp.getInstanse().isLogin()) {// 没有登录就跳转到登录页面
//				Intent intent = new Intent(EPayMainActivity.this, LoginActivity.class);
//				startActivity(intent);
				BaseActivity.getLoginUtils(EPayMainActivity.this).exe(new LoginTask.LoginCallback() {

					@Override
					public void loginStatua(boolean isLogin) {

					}
				});
				return;
			}

			switch (v.getId()) {
			case R.id.rl_ele_pay:
				TransContext.getBomContext().clear(null);
				getBomCardStatus();
				break;
			case R.id.rl_wc_pay:

				Intent intent = new Intent(EPayMainActivity.this, EPaySecondMainActivity.class);
				startActivity(intent);

//				TransContext.getWithoutCardContext().clear(null);
//				dredgedAccountList = new ArrayList<Object>();
//				getWithoutCardStatus();
				break;
			case R.id.rl_treaty_pay:
				TransContext.getTreatyTransContext().clear(null);
				getTreatyStatus();
				break;
			}
		}
	};

	/**
	 * 查询电子支付开通状态
	 *
	 * @param resultObj
	 */
	public void bomServiceOpenStatus(Object resultObj) {
		Object result = httpObserver.getResult(resultObj);
		boolean isOpenBomPayService = EpayUtil.getBoolean(result);
		TransContext.getBomContext().setRightButtonClick(false);
		BiiHttpEngine.dissMissProgressDialog();
		if (isOpenBomPayService) {
			Intent intent = new Intent(this, BankOfMobileActivity.class);
			this.startActivity(intent);
		} else {

			BaseDroidApp.getInstanse().showErrorDialog(null, "您尚未开通电子支付功能，是否立即开通？", new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					BaseDroidApp.getInstanse().dismissErrorDialog();
					switch (PublicTools.getInt(v.getTag(), 0)) {
					case CustomDialog.TAG_SURE:
						Intent intent = new Intent(EPayMainActivity.this, ServOpenAgreementActivity.class);
						startActivity(intent);
						break;
					}
				}
			});
			// BaseDroidApp.getInstanse().showAddressDialog(new
			// com.chinamworld.bocmbci.biz.epay.myPayService.bomobile.dialog.OpenConfirmDialog(this).getContentView());
		}
	}

	private List<Object> allAccountList;
	private List<Object> dredgedAccountList;
	private List<Object> unDredgedAccountList;// = new ArrayList<Object>();

	/**
	 * 查询无卡支付所有账户
	 *
	 * @param resultObj
	 */
	public void queryWithCardAllAccountCallback(Object resultObj) {
		Object result = httpObserver.getResult(resultObj);
		allAccountList = EpayUtil.getList(result);
		TransContext.getWithoutCardContext().setData(PubConstants.CONTEXT_FIELD_ALL_ACCLIST, allAccountList);
		accountListSize = allAccountList.size();

		if (0 == accountListSize) {
			BiiHttpEngine.dissMissProgressDialog();

			// StringBuffer sb = new
			// StringBuffer("您尚未关联任何账户!是否立即关联新账户？\n支持开通银联跨行无卡支付功能的卡类型有：");
			StringBuffer sb = new StringBuffer(getText(R.string.no_open_electronic_payment_is_correlation).toString());
			sb.append("“").append(LocalData.AccountType.get("119")).append("”");

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
	 *
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
	 *
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void queryWithoutCardPayStatus(Object resultObj) {
		Object result = httpObserver.getResult(resultObj);
		Map<Object, Object> map = EpayUtil.getMap(result);
		String status = EpayUtil.getString(map.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_STATUS), "");
		String quota = EpayUtil.getString(map.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_QUOTA), "");
		String custName = EpayUtil.getString(map.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_CUSTNAM), "");
//		String identityType = EpayUtil.getString(map.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_IDENTITYTYPE),
//				"");
//		String identityNum = EpayUtil.getString(map.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_IDENTITYNUM),
//				"");
		String acctNum = EpayUtil.getString(map.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_ACCNUM), "");
//		String cif = EpayUtil.getString(map.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_CIF), "");
//		String phone = EpayUtil.getString(map.get(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_PHONE), "");

		Map<Object, Object> account = EpayUtil.getMap(allAccountList.get(curAccountIndex));
		account.put(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_QUOTA, quota);
		account.put(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_CUSTNAM, custName);
//		account.put(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_IDENTITYTYPE, identityType);
//		account.put(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_IDENTITYNUM, identityNum);
		account.put(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_ACCNUM, acctNum);
//		account.put(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_CIF, cif);
//		account.put(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_PHONE, phone);
		account.put(WithoutCardContants.METHOD_IS_OPEN_NC_PAY_FIELD_STATUS, status);
		if (WithoutCardContants.METHOD_IS_OPEN_NC_PAY_TRUE.equals(status)) {
			dredgedAccountList.add(account);
		}
		// else {
		// unDredgedAccountList.add(account);
		// }

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

			BaseDroidApp.getInstanse().showErrorDialog(null, "您还没有开通银联跨行无卡支付功能的账户，是否立即开通？", new View.OnClickListener() {
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
							StringBuffer sb = new StringBuffer(getText(
									R.string.no_open_electronic_payment_is_correlation).toString());
							String temp = "";
							for (Object accTypeStr : EpayUtil.getAccTypeList()) {
								String accTypeName = LocalData.AccountType.get(accTypeStr);
								if (!temp.equals(accTypeName)) {
									temp = accTypeName;
									sb.append("“").append(accTypeName).append("”、");
								}
							}
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

						Intent intent = new Intent(EPayMainActivity.this, SetPaymentAccSelectActivity.class);
						startActivity(intent);
						break;
					}
				}
			});
		}
	}

	/**
	 * 查询签约商户
	 *
	 * @param resultObj
	 */
	public void queryTreatyRelationsCallback(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		Object result = httpObserver.getResult(resultObj);

		Map resultMap = EpayUtil.getMap(result);

		int recordNum = PublicTools.getInt(
				resultMap.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_RECORD_NUMBER), 0);
		List<Object> treatyMerchants = EpayUtil.getList(resultMap
				.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_LIST));
		if (0 >= recordNum) {
			BaseDroidApp.getInstanse().showErrorDialog(null, "您尚未签约任何商户的协议支付服务，赶快签约协议支付体验极速支付服务",
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							BaseDroidApp.getInstanse().dismissErrorDialog();
							switch (PublicTools.getInt(v.getTag(), 0)) {
							case CustomDialog.TAG_SURE:
								BiiHttpEngine.showProgressDialog();
								httpObserver.req_queryMerchants(0, "queryMerchantsCallback");
								break;
							}
						}
					});
		} else {
			TransContext.getTreatyTransContext().setData(TreatyConstants.PUB_FEILD_TREATY_MERCHANTS, treatyMerchants);
			TransContext.getTreatyTransContext().setData(
					TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_RECORD_NUMBER, recordNum);
			Intent intent = new Intent(EPayMainActivity.this, TreatyActivity.class);
			this.startActivity(intent);
		}
	}

	/**
	 * 查询可签约商户
	 *
	 * @param resultObj
	 */
	public void queryMerchantsCallback(Object resultObj) {
		BiiHttpEngine.dissMissProgressDialog();
		Object result = httpObserver.getResult(resultObj);
		List<Object> merchantList = EpayUtil.getList(result);

		if (merchantList.isEmpty()) {
			final BaseDroidApp bdApp = BaseDroidApp.getInstanse();
			bdApp.showMessageDialog("没有可供选择的商户，请联系客服", new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					bdApp.dismissMessageDialog();
				}
			});

			return;
		}
		TransContext.getTreatyTransContext().setData(TreatyConstants.PUB_FEILD_TREATY_UN_MERCHANTS, merchantList);
		Intent intent = new Intent(EPayMainActivity.this, SelecteMerchantActivity.class);
		startActivity(intent);
	}
}
