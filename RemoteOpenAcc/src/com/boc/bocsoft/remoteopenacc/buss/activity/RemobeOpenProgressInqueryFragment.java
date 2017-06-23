package com.boc.bocsoft.remoteopenacc.buss.activity;

import android.content.Intent;
import android.os.Environment;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import cfca.mobile.sip.CFCASipDelegator;
import cfca.mobile.sip.SipBox;
import cfca.mobile.sip.SipResult;

import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.remoteopenacc.R;
import com.boc.bocsoft.remoteopenacc.buss.model.getrandomnum.GetRandomNumParamsModel;
import com.boc.bocsoft.remoteopenacc.buss.model.getrandomnum.GetRandomNumResponseModel;
import com.boc.bocsoft.remoteopenacc.buss.model.msgcode.SendMobileIdentifyCodeParamsModel;
import com.boc.bocsoft.remoteopenacc.buss.model.msgcode.SendMobileIdentifyCodeResponseModel;
import com.boc.bocsoft.remoteopenacc.buss.model.queryopenaccountprogress.QueryOpenAccountProgressParamsModel;
import com.boc.bocsoft.remoteopenacc.buss.model.queryopenaccountprogress.QueryOpenAccountProgressResponseModel;
import com.boc.bocsoft.remoteopenacc.buss.model.resendopenaccount.ResendOpenAccountParamsModel;
import com.boc.bocsoft.remoteopenacc.buss.model.resendopenaccount.ResendOpenAccountResponseModel;
import com.boc.bocsoft.remoteopenacc.buss.service.CommonService;
import com.boc.bocsoft.remoteopenacc.buss.service.RemoteOpenAccService;
import com.boc.bocsoft.remoteopenacc.buss.view.RemobeOpenProgressInqueryResultView;
import com.boc.bocsoft.remoteopenacc.buss.view.RemobeTimeButtonView;
import com.boc.bocsoft.remoteopenacc.buss.view.RemobeTimeButtonView.EndTimeListener;
import com.boc.bocsoft.remoteopenacc.common.RemoteOpenAccConstant;
import com.boc.bocsoft.remoteopenacc.common.activity.BaseFragment;
import com.boc.bocsoft.remoteopenacc.common.regex.RegexResult;
import com.boc.bocsoft.remoteopenacc.common.regex.RegexUtils;
import com.boc.bocsoft.remoteopenacc.common.view.BaseEditText;
import com.cfca.mobile.log.CodeException;
import com.intsig.idcardscan.sdk.ISCardScanActivity;
import com.intsig.idcardscan.sdk.ResultData;

/**
 * 远程开户进度查询页面
 * 
 * @author fb
 * 
 */
public class RemobeOpenProgressInqueryFragment extends BaseFragment {

	private View mRoot;
	// 获取短信验证码code
	private final static int CODE_MSG_QUERY = 0x11;
	// 获取短信验证码code
	private final static int CODE_OPEN_ACC_PROGRESS = 0x12;
	private final int CODE_RESEND = 0x13;// 在线申请重发
	private final int CODE_QUERY_GET_RANDOM_NUM = 0x14;// 随机数
	// 姓名
	private BaseEditText et_name;
	// 身份证号
	private BaseEditText et_identity_num;
	// 手机号
	private BaseEditText et_phone_num;
	// 短信验证码
	private SipBox et_msg_num;
	// 倒计时按钮
	private RemobeTimeButtonView btn_msg_code;
	// 确认按钮
	private Button btn_query;
	private ImageView iv_id;
	private FrameLayout fl_slid_root_view;

	private RemoteOpenAccActivity mAccActivity;
	private RemoteOpenAccService mRemoteOpenAccService;
	private CommonService commonService;
	private TranslateAnimation alertAnimation;
	private RemobeOpenProgressInqueryResultView mProgressInqueryResultView;

	@Override
	public View onCreateView(LayoutInflater inflater) {
		mRoot = inflater.inflate(
				R.layout.bocroa_fragment_remote_open_progress_inquery, null,
				false);
		return mRoot;
	}

	@Override
	protected void initView() {
		et_name = (BaseEditText) mRoot.findViewById(R.id.et_name);
		et_identity_num = (BaseEditText) mRoot
				.findViewById(R.id.et_identity_num);
		et_phone_num = (BaseEditText) mRoot.findViewById(R.id.et_phone_num);
		et_msg_num = (SipBox) mRoot.findViewById(R.id.et_msg_num);
		// et_msg_num.setEditImeOptions(EditorInfo.IME_ACTION_DONE);
		btn_msg_code = (RemobeTimeButtonView) mRoot
				.findViewById(R.id.btn_msg_code);
		// et_msg_num.setEditTextPasswordType(true);
		btn_query = (Button) mRoot.findViewById(R.id.btn_query);
		iv_id = (ImageView) mRoot.findViewById(R.id.iv_id);
		fl_slid_root_view = (FrameLayout) mRoot
				.findViewById(R.id.fl_slid_root_view);
		mProgressInqueryResultView = new RemobeOpenProgressInqueryResultView(
				mContext, this);
	}

	@Override
	protected void initData() {
		mRemoteOpenAccService = new RemoteOpenAccService(mContext, this);
		commonService = new CommonService(mContext, this);
		mAccActivity = (RemoteOpenAccActivity) getActivity();
		et_msg_num.setKeyBoardType(RemoteOpenAccConstant.KEY_BOARD_TYPE);// 0完全键盘，1数字键盘
		et_msg_num
				.setOutputValueType(RemoteOpenAccConstant.OUT_PUT_VALUE_TYPE_MSG);
		et_msg_num
				.setPasswordMinLength(RemoteOpenAccConstant.VERIFY_MIN_LENGTH);
		et_msg_num
				.setPasswordMaxLength(RemoteOpenAccConstant.VERIFY_MIN_LENGTH);
		et_msg_num.setPasswordRegularExpression("^[0-9]{6}$");
		et_msg_num.setCipherType(RemoteOpenAccConstant.cipherType);// 0国密算法sm2,1国际算法rsa
	}

	@Override
	public void onTaskSuccess(Message result) {
		switch (result.what) {
		// 获取短信验证码 0x11
		case CODE_MSG_QUERY:
			closeProgressDialog();
			// 请求成功后开始倒计时
			btn_msg_code.startTimer(100);
			handlequeryMessageCode((SendMobileIdentifyCodeResponseModel) result.obj);
			break;
		// 查询开户申请进度 0x12
		case CODE_OPEN_ACC_PROGRESS:
			closeProgressDialog();
			et_msg_num.clearText();
			// 计时器重置
			resetTimer();
			handleOpenAccProgress((QueryOpenAccountProgressResponseModel) result.obj);
			break;
		case CODE_RESEND:// 重发
			closeProgressDialog();
			handResend(result);
			break;
		case CODE_QUERY_GET_RANDOM_NUM:
			handRandomNum(result);
			break;
		default:
			break;
		}
	}

	private void handRandomNum(Message result) {
		GetRandomNumResponseModel resultModel = (GetRandomNumResponseModel) result.obj;
		queryOpenAccProgress(resultModel.serverRandom);
	}

	private void resetTimer() {
		btn_msg_code.resetTimer();
		btn_msg_code.setText("重新获取");
		et_msg_num.clearText();
	}

	/**
	 * 处理重发
	 * 
	 * @param result
	 */
	private void handResend(Message result) {
		ResendOpenAccountResponseModel resultModel = (ResendOpenAccountResponseModel) result.obj;
		String responseCode = resultModel.responseCode;
		if ("0000000".equalsIgnoreCase(responseCode)) {
			// 重发成功 处理请求
			mProgressInqueryResultView.onResend(true, "");
		} else {
			mProgressInqueryResultView.onResend(false, resultModel.responseMsg);
		}
	}

	@Override
	public void onTaskFault(Message result) {
		resetTimer();
		super.onTaskFault(result);
	}

	private final int REQ_CODE_CAPTURE = 100;

	/**
	 * 识别
	 */
	private void openDescern() {
		Intent intent = new Intent(getActivity(), ISCardScanActivity.class);
		boolean equals = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (equals) {
			// 设置保存图片路径
			intent.putExtra(ISCardScanActivity.EXTRA_KEY_IMAGE_FOLDER,
					Environment.getExternalStorageDirectory().toString()
							+ RemoteOpenAccConstant.ID_PATH);
			intent.putExtra(ISCardScanActivity.EXTRA_KEY_ORIENTATION,
					ISCardScanActivity.ORIENTATION_VERTICAL);
			intent.putExtra(ISCardScanActivity.EXTRA_KEY_APP_KEY,
					RemoteOpenAccConstant.APP_KEY);
			intent.putExtra(ISCardScanActivity.EXTRA_KEY_TIPS, "请将身份证置于框内将自动识别");
			startActivityForResult(intent, REQ_CODE_CAPTURE);
		} else {
			showErrorDialog("内存卡不可用");
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == FragmentActivity.RESULT_OK
				&& requestCode == REQ_CODE_CAPTURE) {
			ResultData result = (ResultData) data
					.getSerializableExtra(ISCardScanActivity.EXTRA_KEY_RESULT_DATA);
			if (StringUtil.isNullOrEmpty(result.getId())
					&& StringUtil.isNullOrEmpty(result.getName())) {
				showErrorDialog("请识别身份证正面信息！");
			} else {
				et_identity_num.setText(result.getId());
				et_identity_num.requestFocus();
				et_name.setText(result.getName());
			}
		}
	}

	@Override
	protected void setListener() {
		btn_query.setOnClickListener(queryClickListener);
		btn_msg_code.setOnClickListener(queryClickListener);
		et_msg_num.setSipDelegator(new CFCASipDelegator() {

			@Override
			public void beforeKeyboardShow(SipBox arg0, int arg1) {

			}

			@Override
			public void afterKeyboardHidden(SipBox arg0, int arg1) {

			}

			@Override
			public void afterClickDown(SipBox arg0) {
				et_msg_num.hideSecurityKeyBoard();
			}
		});
		iv_id.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openDescern();
			}
		});
		btn_msg_code.setEndTimeListener(new EndTimeListener() {
			@Override
			public void onEndTimeListener() {
				// 倒计时结束点击事件
				btn_msg_code.setText("重新获取");
			}
		});

		// 姓名监听
		et_name.setOnEditFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				// 失去焦点时触发
				if (!hasFocus) {
					et_name.setText(changeHalfToFull(et_name.getText()
							.toString().trim()));
				}
			}
		});

		et_msg_num.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					if (checkProgressInqueryRegex()) {
						getRandomNum();
						// 点击确认按钮，查询申请进度
						// queryOpenAccProgress();
					}
				}
				return false;
			}

		});
	}

	private void getRandomNum() {
		showProgressDialog();
		GetRandomNumParamsModel params = new GetRandomNumParamsModel();
		params.systemFlag = "bocnet";
		commonService.getRandomNum(params, CODE_QUERY_GET_RANDOM_NUM);
	}

	/**
	 * 点击事件监听
	 */
	private OnClickListener queryClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.btn_query) {
				if (checkProgressInqueryRegex()) {
					getRandomNum();
					// 点击确认按钮，查询申请进度
					// queryOpenAccProgress();
				}
			} else if (v.getId() == R.id.btn_msg_code) {
				et_phone_num.clearFocus();
				// 手机号校验
				RegexResult regextelephone = RegexUtils.check(mContext,
						RegexUtils.STYLE_TELEPHONE, et_phone_num.getText()
								.toString().trim(), true);
				if (!regextelephone.isAvailable) {
					showErrorDialog(regextelephone.errorTips);
				} else {
					// 获取验证码
					queryMessageCode();
				}
			}
		}
	};

	/**
	 * 查询短信验证码
	 */
	private void queryMessageCode() {
		showProgressDialog(false);
		SendMobileIdentifyCodeParamsModel mParams = new SendMobileIdentifyCodeParamsModel();
		mParams.mobile = et_phone_num.getText().toString().trim();
		mParams.transType = "02";
		mRemoteOpenAccService.sendmobileidentifycode(mParams, CODE_MSG_QUERY);
	}

	/**
	 * 查询短信验证码结果处理
	 */
	private void handlequeryMessageCode(
			SendMobileIdentifyCodeResponseModel resultModel) {

	}

	/**
	 * 查询开户进度
	 */
	private void queryOpenAccProgress(String randomNum) {
		QueryOpenAccountProgressParamsModel mParams = new QueryOpenAccountProgressParamsModel();
		mParams.custName = et_name.getText().toString().trim();
		if (mParams.custName != null && 1 == mParams.custName.length()) {
			mParams.custName = mParams.custName + "　";
		}
		mParams.certNo = et_identity_num.getText().toString().trim();
		mParams.cardNo = "";
		mParams.mobile = et_phone_num.getText().toString().trim();
		// p604改造 lgw 2016.6.12>>>>
		SipResult sipValue = null;
		try {
			et_msg_num.setRandomKey_S(randomNum);// set cfca random num
			sipValue = et_msg_num.getValue();
			mParams.validCode = sipValue.getEncryptPassword();
			mParams.validRs = sipValue.getEncryptRandomNum();
		} catch (CodeException e) {
			// get error code
			String hexString = Integer.toHexString(e.getCode());
			showErrorDialog(getResources().getString(
					R.string.bocroa_message_messagecode_regex));
			return;
		}
		mParams.rs = randomNum;
		mParams.flag = RemoteOpenAccConstant.CHANNEL_FLAG;// 渠道标识 String(7) Y 1
															// WEB 2 APP 3 手机银行
		// p604改造 lgw 2016.6.12<<<<
		mRemoteOpenAccService.queryOpenAccountProgress(mParams,
				CODE_OPEN_ACC_PROGRESS);

	}

	/**
	 * 查询开户进度结果处理
	 */
	private void handleOpenAccProgress(
			QueryOpenAccountProgressResponseModel resultModel) {
		QueryOpenAccountProgressResponseModel mParams = resultModel;
		if (mParams.list.size() != 0) {
			mProgressInqueryResultView.initData(mParams, mAccActivity, this);
			alertSelectView(mProgressInqueryResultView);
			tv_titleText.setText(getResources().getString(
					R.string.bocroa_apply_progress));
		}
	}

	/**
	 * 重发
	 */
	public void getResendOpenAccount(String reUuid1, String reUuid2) {
		showProgressDialog();
		ResendOpenAccountParamsModel params = new ResendOpenAccountParamsModel();
		params.reUuid1 = reUuid1;
		params.reUuid2 = reUuid2;
		mRemoteOpenAccService.getResendOpenAccountParamsModel(params,
				CODE_RESEND);
	}

	@Override
	public String getMainTitleText() {
		return getResources().getString(R.string.bocroa_progress_inquery);
	}

	/**
	 * 弹出窗口从右往左弹出
	 * 
	 * @param belongBankView
	 */
	private final long ANIMATION_DURATION = 500;

	public void alertSelectView(View view) {
		fl_slid_root_view.addView(view);
		if (alertAnimation == null) {
			alertAnimation = new TranslateAnimation(
					TranslateAnimation.RELATIVE_TO_SELF, 1.0f,
					TranslateAnimation.RELATIVE_TO_SELF, 0,
					TranslateAnimation.RELATIVE_TO_SELF, 0,
					TranslateAnimation.RELATIVE_TO_SELF, 0);
			alertAnimation.setDuration(ANIMATION_DURATION);
		}
		fl_slid_root_view.startAnimation(alertAnimation);
		fl_slid_root_view.setVisibility(View.VISIBLE);
	}

	/**
	 * 弹出窗口从左往右隐藏
	 */
	private TranslateAnimation hidAnimation;

	public void hidAlertSelectView() {
		if (fl_slid_root_view.getVisibility() == View.VISIBLE) {
			if (hidAnimation == null) {
				hidAnimation = new TranslateAnimation(
						TranslateAnimation.RELATIVE_TO_SELF, 0,
						TranslateAnimation.RELATIVE_TO_SELF, 1.0f,
						TranslateAnimation.RELATIVE_TO_SELF, 0,
						TranslateAnimation.RELATIVE_TO_SELF, 0);
				hidAnimation.setDuration(ANIMATION_DURATION);
				hidAnimation.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {

					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						fl_slid_root_view.setVisibility(View.GONE);
						fl_slid_root_view.removeAllViews();
					}
				});
			}
			fl_slid_root_view.startAnimation(hidAnimation);
		}
	}

	/**
	 * 进度查询页面校验
	 * 
	 * @return
	 */
	private boolean checkProgressInqueryRegex() {
		// 姓名
		RegexResult regexName = RegexUtils.check(mContext,
				RegexUtils.STYLE_NAME, et_name.getText().toString().trim(),
				true);
		if (!regexName.isAvailable) {
			showErrorDialog(regexName.errorTips);
			return false;
		}

		// 身份证号
		RegexResult regexCertNo = RegexUtils.check(mContext,
				RegexUtils.STYLE_CERT_NO, et_identity_num.getText().toString()
						.trim(), true);
		if (!regexCertNo.isAvailable) {
			showErrorDialog(regexCertNo.errorTips);
			return false;
		}
		// 手机号
		RegexResult regextelephone = RegexUtils.check(mContext,
				RegexUtils.STYLE_TELEPHONE, et_phone_num.getText().toString()
						.trim(), true);
		if (!regextelephone.isAvailable) {
			showErrorDialog(regextelephone.errorTips);
			return false;
		}
		// 短信验证码
		// try {
		// et_msg_num.getValue();
		// } catch (CodeException e) {
		// // String hexString = Integer.toHexString(e.getCode());
		// showErrorDialog(getResources().getString(
		// R.string.bocroa_message_messagecode_regex));
		// return false;
		// }
		// RegexResult regexmessagecode = RegexUtils.check(mContext,
		// RegexUtils.STYLE_MESSAGECODE, et_msg_num.getText().toString()
		// .trim(), true);
		// if (!regexmessagecode.isAvailable) {
		// showErrorDialog(regexmessagecode.errorTips);
		// return false;
		// }
		return true;
	}

	/**
	 * @param srcString
	 */
	private String changeHalfToFull(String srcString) {
		return srcString.replace(".", "·");
	}

	public boolean isViewShow() {
		return fl_slid_root_view.getVisibility() == View.VISIBLE;
	}

	@Override
	public boolean onGoBackFragment() {
		if (isViewShow()) {
			hidAlertSelectView();
			tv_titleText.setText(getResources().getString(
					R.string.bocroa_progress_inquery));
			return false;
		} else {
			jumpToHome();
			return false;
		}
	}
}
