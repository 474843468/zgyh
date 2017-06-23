package com.chinamworld.bocmbci.biz.epay.myPayService.treaty.dialog;

import java.util.HashMap;
import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cfca.mobile.sip.SipBox;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.biz.epay.EpayBaseDialog;
import com.chinamworld.bocmbci.biz.epay.constants.PubConstants;
import com.chinamworld.bocmbci.biz.epay.constants.TreatyConstants;
import com.chinamworld.bocmbci.biz.epay.context.Context;
import com.chinamworld.bocmbci.biz.epay.context.TransContext;
import com.chinamworld.bocmbci.biz.epay.myPayService.treaty.TreatyActivity;
import com.chinamworld.bocmbci.biz.epay.myPayService.treaty.detail.MerchantDetailActivity;
import com.chinamworld.bocmbci.biz.epay.observer.PubHttpObserver;
import com.chinamworld.bocmbci.biz.epay.util.EpayUtil;
import com.chinamworld.bocmbci.constant.CheckRegExp;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;

/**
 * 电子支付-取消电子支付确认弹出框
 * 
 */
@SuppressWarnings("ResourceType")
public class DeleteMerchantConfirmDialog extends EpayBaseDialog {
	public static final String TAG = "DeleteMerchantConfirmDialog";

	private LinearLayout ll_otp;
	private LinearLayout ll_smc;
	/** 短信通知 */
	private SipBox sb_note_code;
	/** 动态口令 */
	private SipBox sb_dynamic_code;
	/** 中银E盾 */
	private UsbKeyText usbKeyText;
	private TextView tv_confirm_msg;
	/* 确认按钮 */
	private Button bt_ensure;
	/* 取消按钮 */
	private Button bt_cancel;

	private Button bt_smsbtn;

	private BaseActivity context;

	private PubHttpObserver httpObserver;
	private Context treatyContext;

	private Map<Object, Object> deleteMerchant;
	private Map<String, Object> resultMap;
	private String randomKey;
	private boolean confirmOtp;
	private boolean confirmSmc;
	private String merchantNo;
	private String agreementId;
	private String holderMerId;
	private String merchantName;
	private String cardNo;
	private String cardType;
	private String dailyQuota;
	private String signDate;
	private View view;
	private String otp;
	private String otpRC;
	private String smc;
	private String smcRC;
	private OnClickListener mOnClickListener;

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public void randomKeyCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		randomKey = EpayUtil.getString(httpObserver.getResult(resultObj), "");

		merchantNo = EpayUtil
				.getString(
						deleteMerchant
								.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_BOC_NO),
						"");
		agreementId = EpayUtil
				.getString(
						deleteMerchant
								.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_AGREEMENT_ID),
						"");
		holderMerId = EpayUtil
				.getString(
						deleteMerchant
								.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_HOLDER_MERID),
						"");
		merchantName = EpayUtil
				.getString(
						deleteMerchant
								.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_MERCHANT_NAME),
						"");
		cardNo = EpayUtil
				.getString(
						deleteMerchant
								.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_CARD_NO),
						"");
		cardType = EpayUtil
				.getString(
						deleteMerchant
								.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_CARD_TYPE),
						"");
		dailyQuota = EpayUtil
				.getString(
						deleteMerchant
								.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_DAILY_QUOTA),
						"");
		initCurPage();
	}

	private void initCurPage() {
		/** 跟据返回初始化中银E盾加密控件 */
		initSipBox();
		// ll_otp = (LinearLayout) view.findViewById(R.id.ll_otp);
		// ll_smc = (LinearLayout) view.findViewById(R.id.ll_smc);

		tv_confirm_msg = (TextView) view.findViewById(R.id.tv_confirm_msg);
		tv_confirm_msg.setText(new StringBuffer("您确定要与").append(merchantName)
				.append("解除协议支付的签约吗?"));

		if (confirmOtp) {
			ll_otp = (LinearLayout) view.findViewById(R.id.ll_otp);
			ll_otp.setVisibility(View.VISIBLE);
			/** 动态口令 */
			sb_dynamic_code = (SipBox) view.findViewById(R.id.sb_dynamic_code);
			sb_dynamic_code.setCipherType(SystemConfig.CIPHERTYPE);
			sb_dynamic_code
					.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
			sb_dynamic_code.setId(10002);
			sb_dynamic_code.setPasswordMinLength(6);
			sb_dynamic_code.setPasswordMaxLength(6);
			sb_dynamic_code.setPasswordRegularExpression(CheckRegExp.OTP);
			sb_dynamic_code.setSipDelegator(context);
			sb_dynamic_code
					.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
			sb_dynamic_code.setRandomKey_S(randomKey);
		}

		if (confirmSmc) {
			ll_smc = (LinearLayout) view.findViewById(R.id.ll_smc);
			ll_smc.setVisibility(View.VISIBLE);
			sb_note_code = (SipBox) view.findViewById(R.id.sb_note_code);
			sb_note_code.setCipherType(SystemConfig.CIPHERTYPE);
			sb_note_code
					.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
			sb_note_code.setId(10002);
			sb_note_code.setPasswordMinLength(6);
			sb_note_code.setPasswordMaxLength(6);
			sb_note_code.setPasswordRegularExpression(CheckRegExp.OTP);
			sb_note_code.setSipDelegator(context);
			sb_note_code.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
			sb_note_code.setRandomKey_S(randomKey);

			bt_smsbtn = (Button) view.findViewById(R.id.bt_smsbtn);
			SmsCodeUtils.getInstance().addSmsCodeListner(bt_smsbtn,
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							// 发送手机交易码的请求
							context.sendSMSCToMobile();
						}
					});
		}
		bt_ensure = (Button) view.findViewById(R.id.bt_ensure);
		bt_ensure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 校验提交参数
				// if (!checkSubmitData()) {
				// return;
				// }
				checkDate();

				// BiiHttpEngine.showProgressDialog();
				// httpObserver.req_getToken("getTokenCallback");
			}
		});

		bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
		bt_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				BiiHttpEngine.dissMissProgressDialog();
				BaseDroidApp.getInstanse().dismissErrorDialog();
			}
		});
		BaseDroidApp.getInstanse().showDialog(this.getContentView());
		// for (Object confirmType : factorList) {
		// if (PubConstants.PUB_FIELD_OTP.equals(confirmType)) {
		// ll_otp.setVisibility(View.VISIBLE);
		// confirmOtp = true;
		// } else if (PubConstants.PUB_FIELD_SMC.equals(confirmType)) {
		// confirmSmc = true;
		// ll_smc.setVisibility(View.VISIBLE);
		// }
		// }
	}

	/** 跟据返回初始化中银E盾加密控件 */
	private void initSipBox() {
		usbKeyText = (UsbKeyText) view.findViewById(R.id.sip_usbkey);
		String mmconversationId = (String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		// Map<String, Object> map = (Map<String,
		// Object>)TreatyActivity.resultMap;
		usbKeyText.Init(mmconversationId, randomKey, resultMap, context);
		confirmOtp = usbKeyText.getIsOtp();
		confirmSmc = usbKeyText.getIsSmc();
	}

	/** 安全工具数据校验 */
	private void checkDate() {
		usbKeyText.checkDataUsbKey(sb_dynamic_code, sb_note_code,
				new IUsbKeyTextSuccess() {

					@Override
					public void SuccessCallBack(String result, int errorCode) {
						// TODO Auto-generated method stub
						BiiHttpEngine.showProgressDialog();
						httpObserver.req_getToken("getTokenCallback");
					}
				});
	}

	// private boolean checkSubmitData() {
	//
	// try {
	// if (confirmSmc) {
	// // 验证
	// RegexpBean reb1 = new
	// RegexpBean(context.getResources().getString(R.string.set_smc_no),
	// sb_note_code
	// .getText().toString(), ConstantGloble.SIPSMCPSW);
	// ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
	// lists.add(reb1);
	// boolean regexpDate = RegexpUtils.regexpDate(lists, new OnClickListener()
	// {
	// @Override
	// public void onClick(View v) {
	// if (mOnClickListener != null) {
	// mOnClickListener.onClick(v);
	// }
	// }
	// });
	// if (regexpDate) {
	// smc = sb_note_code.getValue().getEncryptPassword();
	// smcRC = sb_note_code.getValue().getEncryptRandomNum();
	// } else {
	// return false;
	// }
	// }
	// } catch (CodeException e) {
	// BaseDroidApp.getInstanse().showInfoMessageDialog(
	// context.getResources().getText(R.string.tran_acc_smc).toString());
	// return false;
	// }
	//
	// try {
	// if (confirmOtp) {
	// // 验证
	// RegexpBean reb1 = new
	// RegexpBean(context.getResources().getString(R.string.set_otp_no),
	// sb_dynamic_code
	// .getText().toString(), ConstantGloble.SIPOTPPSW);
	// ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
	// lists.add(reb1);
	// boolean regexpDate = RegexpUtils.regexpDate(lists, new OnClickListener()
	// {
	// @Override
	// public void onClick(View v) {
	// if (mOnClickListener != null) {
	// mOnClickListener.onClick(v);
	// }
	// }
	// });
	// if (regexpDate) {
	// otp = sb_dynamic_code.getValue().getEncryptPassword();
	// otpRC = sb_dynamic_code.getValue().getEncryptRandomNum();
	// } else {
	// return false;
	// }
	// }
	// } catch (CodeException e) {
	// BaseDroidApp.getInstanse().showInfoMessageDialog(
	// context.getResources().getText(R.string.tran_acc_opt).toString());
	// return false;
	// }

	// try {
	// if (confirmSmc) {
	// // 验证
	// RegexpBean reb1 = new
	// RegexpBean(context.getResources().getString(R.string.set_smc_no),
	// sb_note_code
	// .getText().toString(), ConstantGloble.SIPSMCPSW);
	// ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
	// lists.add(reb1);
	// boolean regexpDate = RegexpUtils.regexpDate(lists, new OnClickListener()
	// {
	// @Override
	// public void onClick(View v) {
	// if (mOnClickListener != null) {
	// mOnClickListener.onClick(v);
	// }
	// }
	// });
	// if (regexpDate) {
	// smc = sb_note_code.getValue().getEncryptPassword();
	// smcRC = sb_note_code.getValue().getEncryptRandomNum();
	// } else {
	// return false;
	// }
	// }
	// } catch (CodeException e) {
	// BaseDroidApp.getInstanse().showInfoMessageDialog(
	// context.getResources().getText(R.string.tran_acc_smc).toString());
	// return false;
	// }

	// return true;
	// }

	public void getTokenCallback(Object resultObj) {
		String token = EpayUtil
				.getString(httpObserver.getResult(resultObj), "");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(TreatyConstants.METHOD_DELETE_RELATIONS_FIELD_MERCHANT_NO,
				merchantNo);
		params.put(TreatyConstants.METHOD_DELETE_RELATIONS_FIELD_MERCHANT_NAME,
				merchantName);
		params.put(TreatyConstants.METHOD_DELETE_RELATIONS_FIELD_HOLDER_MER_ID,
				holderMerId);
		params.put(TreatyConstants.METHOD_DELETE_RELATIONS_FIELD_AGREEMENT_ID,
				agreementId);
		params.put(TreatyConstants.METHOD_DELETE_RELATIONS_FIELD_CARD_NO,
				cardNo);
		params.put(TreatyConstants.METHOD_DELETE_RELATIONS_FIELD_CARD_TYPE,
				cardType);
		params.put(TreatyConstants.METHOD_DELETE_RELATIONS_FIELD_DAILY_QUOTA,
				dailyQuota);
		params.put(TreatyConstants.METHOD_DELETE_RELATIONS_FIELD_SIGN_DATE,
				signDate);
		params.put(PubConstants.PUB_FIELD_TOKEN_ID, token);

		// if (confirmOtp) {
		// params.put(PubConstants.PUB_FIELD_OTP, otp);
		// params.put(PubConstants.PUB_FIELD_OTP_RC, otpRC);
		// }
		//
		// if (confirmSmc) {
		// params.put(PubConstants.PUB_FIELD_SMC, smc);
		// params.put(PubConstants.PUB_FIELD_SMC_RC, smcRC);
		// }
		usbKeyText.InitUsbKeyResult(params);
		SipBoxUtils.setSipBoxParams(params);
		// 执行解约通讯
		httpObserver.req_deleteMerchantRelation(params,
				"deleteMerchantRelationCallback");
	}

	public void deleteMerchantRelationCallback(Object resultObj) {
		BaseDroidApp.getInstanse().dismissErrorDialog();
		// int recordNumber =
		// EpayUtil.getInt(treatyContext.getData(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_RECORD_NUMBER),
		// 0);
		// recordNumber -= 1;
		BiiHttpEngine.dissMissProgressDialog();
		for (int i = 0; i < treatyContext.getList(
				TreatyConstants.PUB_FEILD_TREATY_MERCHANTS).size(); i++) {
			Map<Object, Object> map = EpayUtil.getMap(treatyContext.getList(
					TreatyConstants.PUB_FEILD_TREATY_MERCHANTS).get(i));
			if (null != merchantNo
					&& merchantNo
							.equals(map
									.get(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_BOC_NO))) {
				// treatyContext.getList(TreatyConstants.PUB_FEILD_TREATY_MERCHANTS).remove(i);
				// map.put(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_STATUS,
				// "D");
				// 修改状态为解约
				EpayUtil.getMap(
						treatyContext.getList(
								TreatyConstants.PUB_FEILD_TREATY_MERCHANTS)
								.get(i))
						.put(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_STATUS,
								"D");
				break;
			}
		}
		BaseDroidApp.getInstanse().showMessageDialog("解约成功",
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						BaseDroidApp.getInstanse().dismissMessageDialog();

						if (context != null
								&& context instanceof TreatyActivity) {// 已开通协议支付页面
							// treatyContext.setRightButtonClick(false);
							((TreatyActivity) context).refreshPageData(); // 是否需要刷新？
							// ((TreatyActivity)
							// context).getRelationMerchantAdapter().notifyDataSetChanged();
						} else if (context != null) {// 详情页面 点击解约
							if (context instanceof MerchantDetailActivity) {
								((MerchantDetailActivity) context)
										.setTreatyStatus("D");
								((MerchantDetailActivity) context)
										.refreshTerminatePage(); // 是否需要刷新？
							}
						}
					}
				});
		// CustomDialog.toastShow(context, "解约成功");
		// treatyContext.setData(TreatyConstants.METHOD_QUERY_TREATY_RELATIONS_FIELD_RECORD_NUMBER,
		// recordNumber);
		// 判断交易记录是否为0 为0返回2级菜单
		// if (recordNumber <= 0) {
		// Intent intent = new Intent(context, EPayMainActivity.class);
		// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// context.startActivity(intent);
		// context.finish();
		// return;
		// }

	}

	public DeleteMerchantConfirmDialog(String signDate,
			Map<String, Object> resultMap, Map<Object, Object> deleteMerchant,
			BaseActivity context) {
		super(context);
		this.context = context;
		this.signDate = signDate;
		this.resultMap = resultMap;
		this.deleteMerchant = deleteMerchant;

		view = LayoutInflater.from(context).inflate(
				R.layout.epay_treaty_delete_confirm_dialog, null);
		treatyContext = TransContext.getTreatyTransContext();
		httpObserver = PubHttpObserver.getInstance(this,
				PubConstants.CONTEXT_TREATY);
		BaseHttpEngine.showProgressDialog();
		httpObserver.req_randomKey("randomKeyCallback");
	}

	public void setErrorOnClickListener(OnClickListener listener) {
		mOnClickListener = listener;
	}

	public View getContentView() {
		return view;
	}

}
