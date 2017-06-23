package com.boc.bocsoft.remoteopenacc.buss.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import cfca.mobile.sip.CFCASipDelegator;
import cfca.mobile.sip.SipBox;
import cfca.mobile.sip.SipResult;

import com.boc.bocma.exception.MAOPException;
import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.remoteopenacc.R;
import com.boc.bocsoft.remoteopenacc.buss.model.checkpersonidentity.CheckPersonIdentityParamsModel;
import com.boc.bocsoft.remoteopenacc.buss.model.checkpersonidentity.CheckPersonIdentityResponseModel;
import com.boc.bocsoft.remoteopenacc.buss.model.getrandomnum.GetRandomNumParamsModel;
import com.boc.bocsoft.remoteopenacc.buss.model.getrandomnum.GetRandomNumResponseModel;
import com.boc.bocsoft.remoteopenacc.buss.model.msgcode.SendMobileIdentifyCodeParamsModel;
import com.boc.bocsoft.remoteopenacc.buss.model.msgcode.SendMobileIdentifyCodeResponseModel;
import com.boc.bocsoft.remoteopenacc.buss.model.remoteopenacconlinecheck2pivs.RemoteOpenAccOnLineCheck2PivsParamsModel;
import com.boc.bocsoft.remoteopenacc.buss.model.remoteopenacconlinecheck2pivs.RemoteOpenAccOnLineCheck2PivsResponsModel;
import com.boc.bocsoft.remoteopenacc.buss.model.resendopenaccount.ResendOpenAccountParamsModel;
import com.boc.bocsoft.remoteopenacc.buss.model.resendopenaccount.ResendOpenAccountResponseModel;
import com.boc.bocsoft.remoteopenacc.buss.model.uploadidcard.UploadidcardParamsModel;
import com.boc.bocsoft.remoteopenacc.buss.model.uploadidcard.UploadidcardResponseModel;
import com.boc.bocsoft.remoteopenacc.buss.service.CommonService;
import com.boc.bocsoft.remoteopenacc.buss.service.RemoteOpenAccService;
import com.boc.bocsoft.remoteopenacc.buss.view.RemobeTimeButtonView;
import com.boc.bocsoft.remoteopenacc.buss.view.RemobeTimeButtonView.EndTimeListener;
import com.boc.bocsoft.remoteopenacc.common.RemoteOpenAccConstant;
import com.boc.bocsoft.remoteopenacc.common.activity.BaseFragment;
import com.boc.bocsoft.remoteopenacc.common.regex.RegexResult;
import com.boc.bocsoft.remoteopenacc.common.regex.RegexUtils;
import com.boc.bocsoft.remoteopenacc.common.util.BocroaUtils;
import com.boc.bocsoft.remoteopenacc.common.util.DisplayUtil;
import com.boc.bocsoft.remoteopenacc.common.util.ImageUtil;
import com.boc.bocsoft.remoteopenacc.common.view.BaseEditText;
import com.boc.bocsoft.remoteopenacc.common.view.MessageDialogTwoBtn;
import com.cfca.mobile.log.CodeException;
import com.intsig.idcardscan.sdk.ISCardScanActivity;
import com.intsig.idcardscan.sdk.ResultData;

/**
 * 远程开户身份验证页面
 * 
 * @author gwluo
 */
public class RemobeOpenAuthenticationFragment extends BaseFragment implements
		OnClickListener {

	private View mRoot;
	RemobeOpenAccSubStepFragment subStepFragment;
	// 获取短信验证码code
	private final static int CODE_MSG_QUERY = 0x11;
	// 身份验证code
	private final static int CODE_AUTHENTICATION = 0x12;
	private final int CODE_RESEND = 0x13;// 在线申请重发
	private final int CODE_UPLOAD_ID_CARD = 0x14;// 身份证上传
	private final int CODE_UPLOAD_NET_CHECK = 0x15;// 远程开户单笔联网核查
	private final int CODE_QUERY_GET_RANDOM_NUM = 0x16;// 随机数
	// 姓氏
	private BaseEditText et_name_left;
	// 名字
	private BaseEditText et_name_right;
	// 身份证号
	private BaseEditText et_identity_num;
	// 截止日期
	// private BaseEditText et_validity_end;
	// 签发日期
	// private BaseEditText et_validity_start;
	// 手机号
	private BaseEditText et_phone_num;
	// 短信验证码
	private SipBox et_msg_num;
	// 倒计时按钮
	private RemobeTimeButtonView btn_msg_code;
	// 确认按钮
	private Button btn_confirm;
	// 协议勾选框
	private ImageButton imagebtn;
	private ImageView iv_id;// 身份证识别
	// private ImageView iviv;//
	// private ImageView iv_validity;// 有效期识别
	// 协议链接
	private TextView tv_agreement;
	// private BaseEditText et_name_left_spell;// 姓氏拼音
	// private BaseEditText et_name_right_spell;// 名字拼音
	private RemoteOpenAccService mRemoteOpenAccService;
	private CommonService commonService;
	private RelativeLayout rl_shot_front_float;
	private ViewGroup rl_shot_front;
	private ViewGroup rl_shot_back;
	private RelativeLayout rl_shot_back_float;
	private LinearLayout ll_picture_layout;
	private ImageView iv_back_shot;
	private ImageView iv_front_shot;
	private String rightName = "";
	private String rightNameSpell = "";// 名的拼音
	private boolean isAgree;

	@Override
	public View onCreateView(LayoutInflater inflater) {
		mRoot = inflater.inflate(
				R.layout.bocroa_fragment_remote_open_authentication, null,
				false);
		return mRoot;
	}

	@Override
	protected void initView() {
		et_name_left = (BaseEditText) mRoot.findViewById(R.id.et_name_left);
		et_name_right = (BaseEditText) mRoot.findViewById(R.id.et_name_right);
		et_identity_num = (BaseEditText) mRoot
				.findViewById(R.id.et_identity_num);
		// et_validity_end = (BaseEditText)
		// mRoot.findViewById(R.id.et_validity);
		// et_validity_start = (BaseEditText) mRoot
		// .findViewById(R.id.et_validity_start);
		et_phone_num = (BaseEditText) mRoot.findViewById(R.id.et_phone_num);
		et_msg_num = (SipBox) mRoot.findViewById(R.id.et_msg_num);
		// et_msg_num.setEditTextPasswordType(true);
		btn_msg_code = (RemobeTimeButtonView) mRoot
				.findViewById(R.id.btn_msg_code);
		btn_confirm = (Button) mRoot
				.findViewById(R.id.btn_authentication_confirm);
		imagebtn = (ImageButton) mRoot.findViewById(R.id.imagebtn_agreement);
		imagebtn = (ImageButton) mRoot.findViewById(R.id.imagebtn_agreement);
		iv_id = (ImageView) mRoot.findViewById(R.id.iv_id);
		// iviv = (ImageView) mRoot.findViewById(R.id.iviv);
		// iv_validity = (ImageView) mRoot.findViewById(R.id.iv_validity);
		imagebtn.setSelected(false);
		tv_agreement = (TextView) mRoot.findViewById(R.id.tv_agreement);
		// et_name_left_spell = (BaseEditText) mRoot
		// .findViewById(R.id.et_name_left_spell);
		// et_name_right_spell = (BaseEditText) mRoot
		// .findViewById(R.id.et_name_right_spell);
		// et_identity_num.setEditImage(R.drawable.bocroa_id_have_shut);
		rl_shot_front_float = (RelativeLayout) mRoot
				.findViewById(R.id.rl_shot_front_float);
		rl_shot_front = (ViewGroup) mRoot.findViewById(R.id.rl_shot_front);
		rl_shot_back = (ViewGroup) mRoot.findViewById(R.id.rl_shot_back);
		rl_shot_back_float = (RelativeLayout) mRoot
				.findViewById(R.id.rl_shot_back_float);
		ll_picture_layout = (LinearLayout) mRoot
				.findViewById(R.id.ll_picture_layout);
		iv_back_shot = (ImageView) mRoot.findViewById(R.id.iv_back_shot);
		iv_front_shot = (ImageView) mRoot.findViewById(R.id.iv_front_shot);
		iv_id_front = (ImageView) mRoot.findViewById(R.id.iv_id_front);
		iv_id_back = (ImageView) mRoot.findViewById(R.id.iv_id_back);
	}

	@Override
	protected void initData() {
		mRemoteOpenAccService = new RemoteOpenAccService(mContext, this);
		commonService = new CommonService(mContext, this);
		// et_name_left.setMaxLenth(13);
		// et_name_right.setMaxLenth(13);
		et_msg_num
				.setOutputValueType(RemoteOpenAccConstant.OUT_PUT_VALUE_TYPE_MSG);
		et_msg_num
				.setPasswordMinLength(RemoteOpenAccConstant.VERIFY_MIN_LENGTH);
		et_msg_num
				.setPasswordMaxLength(RemoteOpenAccConstant.VERIFY_MIN_LENGTH);
		et_msg_num.setPasswordRegularExpression("^[0-9]{6}$");// TODO 正则
		et_msg_num.setKeyBoardType(RemoteOpenAccConstant.KEY_BOARD_TYPE);// 0完全键盘，1数字键盘
		et_msg_num.setCipherType(RemoteOpenAccConstant.cipherType);// 0国密算法sm2,1国际算法rsa
	}

	@Override
	public void onStart() {
		if (!TextUtils.isEmpty(IDFrontPath)) {
			Bitmap frontBitmap = ImageUtil.loadBitmap(IDFrontPath);
			iv_id_front.setImageBitmap(frontBitmap);
			rl_shot_front_float.setVisibility(View.VISIBLE);
			rl_shot_front.setVisibility(View.GONE);
			resetPicLayout(frontBitmap);
		}
		if (!TextUtils.isEmpty(IDBackPath)) {
			Bitmap backBitmap = ImageUtil.loadBitmap(IDBackPath);
			iv_id_back.setImageBitmap(backBitmap);
			rl_shot_back_float.setVisibility(View.VISIBLE);
			rl_shot_back.setVisibility(View.GONE);
			resetPicLayout(backBitmap);
		}
		imagebtn.setSelected(isAgree);
		super.onStart();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == FragmentActivity.RESULT_OK
				&& requestCode == REQ_CODE_CAPTURE) {
			// 身份证图片
			String src_image_path = data
					.getStringExtra(ISCardScanActivity.EXTRA_KEY_RESULT_IMAGE);
			ResultData result = (ResultData) data
					.getSerializableExtra(ISCardScanActivity.EXTRA_KEY_RESULT_DATA);
			// 身份证姓名
			String resultName = result.getName();
			if (resultName != null && resultName.length() == 1) {
				setReturnName(et_name_left, result, true);
			} else if (resultName != null && resultName.length() > 1) {
				setReturnName(et_name_left, result, true);
				setReturnName(et_name_right, result, false);
			}

			// 身份证头像绝对路径
			// String avatar_path = data
			// .getStringExtra(ISCardScanActivity.EXTRA_KEY_RESULT_AVATAR);
			if (!TextUtils.isEmpty(src_image_path)) {
				Bitmap bmp = ImageUtil.loadBitmap(src_image_path);
				if (bmp != null) {
					if (result.isFront()) {
						if (!StringUtil.isNullOrEmpty(result.getId())) {
							et_identity_num.setEditEnable(false);
							et_identity_num.setText("");// 激活文本监听让×消失
							et_identity_num.setText(result.getId());
						}
						national = result.getNational() + "族";
						// String address = result.getAddress();
						// int indexOfCity = address.indexOf("市");
						// int indexOfZhou = address.indexOf("自治州");
						// String add1 = address.substring(0, indexOfCity);
						address = result.getAddress();
						IDFrontPath = src_image_path;
						iv_id_front.setImageBitmap(bmp);
						rl_shot_front_float.setVisibility(View.VISIBLE);
						rl_shot_front.setVisibility(View.GONE);
						// Log.i("lgw front", src_image_path);
					} else {
						IDBackPath = src_image_path;
						iv_id_back.setImageBitmap(bmp);
						rl_shot_back_float.setVisibility(View.VISIBLE);
						rl_shot_back.setVisibility(View.GONE);
						String validity = result.getValidity();
						if (validity != null) {
							if (validity.contains("-")) {
								String[] split = validity.split("-");
								// validity_start = split[0].replaceAll("\\.",
								// "");
								// validity_end = split[1].replaceAll("\\.",
								// "");
								// 605修改为带点的日期格式，lgw 2016.6.27
								validity_start = split[0];
								validity_end = split[1];
								// 605长期有效不再显示数字型日期，lgw 2016.6.27
								// if ("长期".equalsIgnoreCase(validity_end)) {
								// validity_end =
								// RemoteOpenAccConstant.ID_DEFAULT_DATE;
								// }
							} else if (RemoteOpenAccConstant.ID_DATE_LONGTIME2
									.equalsIgnoreCase(validity)) {
								// 605长期有效不再显示数字型日期，lgw 2016.6.27
								validity_start = validity;
								validity_end = validity;
								// validity_start =
								// RemoteOpenAccConstant.ID_DEFAULT_DATE;
								// validity_end =
								// RemoteOpenAccConstant.ID_DEFAULT_DATE;
							}
						}
						// Log.i("lgw back", src_image_path);
					}
					resetPicLayout(bmp);
				}
			}
		} else {

		}
	}

	private void resetPicLayout(Bitmap i) {
		int hei = DisplayUtil.getScreenMetrics(mContext).x / 2 * i.getHeight()
				/ i.getWidth() + BocroaUtils.dip2px(mContext, 6);
		ll_picture_layout.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, hei));
	}

	/**
	 * 设置姓名
	 * 
	 * @param editText
	 * @param result
	 * @param isFirstName
	 *            true姓，false名
	 */
	private void setReturnName(BaseEditText editText, ResultData result,
			boolean isFirstName) {
		String name = "";
		String resultName = changeAllHalfToFull(result.getName());
		if (isFirstName) {
			if (resultName.contains("·")) {
				int index = resultName.indexOf("·");
				name = resultName.substring(0, index + 1);
			} else {
				if (resultName.length() >= 4) {// 复姓的情况
					name = resultName.substring(0, 2);
				} else {
					name = resultName.substring(0, 1);
				}
			}
		} else {
			int index = et_name_left.getText().toString().indexOf("·");
			if (index == -1) {
				if (resultName.length() >= 4) {// 复姓的情况
					name = resultName.substring(2, resultName.length());
				} else {
					name = resultName.substring(1, resultName.length());
				}
			} else {
				name = resultName.substring(index + 1, resultName.length());
			}
		}
		editText.setText(name);
	}

	private final int REQ_CODE_CAPTURE = 100;// 启动activity的返回码

	@Override
	protected void setListener() {
		iv_back_shot.setOnClickListener(this);
		iv_front_shot.setOnClickListener(this);
		rl_shot_front.setOnClickListener(this);
		rl_shot_back.setOnClickListener(this);
		iv_id.setOnClickListener(this);
		// iv_validity.setOnClickListener(this);
		btn_confirm.setOnClickListener(this);
		btn_msg_code.setOnClickListener(this);
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
		// et_identity_num.setBaseEditCallBack(new BaseEditCallBack() {
		//
		// @Override
		// public void onJump2PicDescern() {
		// openDescern();
		// }
		//
		// });
		btn_msg_code.setEndTimeListener(new EndTimeListener() {
			@Override
			public void onEndTimeListener() {
				// 倒计时结束，监听事件
				btn_msg_code.setText("重新获取");
			}
		});
		imagebtn.setOnClickListener(this);
		tv_agreement.setOnClickListener(this);
		// 姓氏监听
		et_name_left.setOnEditFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// 失去焦点时触发
				if (!hasFocus) {
					et_name_left.setText(changeHalfToFull(et_name_left
							.getText().toString().trim()));
				}
			}
		});
		// 名字监听
		et_name_right.setOnEditFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				// 失去焦点时触发
				if (!hasFocus) {
					et_name_right.setText(changeHalfToFull(et_name_right
							.getText().toString().trim()));
				}
			}
		});

		et_msg_num.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					et_name_left.clearFocus();
					et_name_right.clearFocus();
					if (checkOpenAuthenticationRegex()) {
						getRandomNum();
						// 身份验证
						// queryOpenAuthentication();
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

	/**
	 * 点击事件监听
	 */
	// private OnClickListener queryClickListener = new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// if (v.getId() == R.id.btn_authentication_confirm) {
	// et_name_left.clearFocus();
	// et_name_right.clearFocus();
	// if (checkOpenAuthenticationRegex()) {
	// // 身份验证
	// queryOpenAuthentication();
	// }
	// } else if (v.getId() == R.id.btn_msg_code) {// 获取验证码
	// // 手机号失去焦点
	// et_phone_num.clearFocus();
	// // 手机号校验
	// RegexResult regextelephone = RegexUtils.check(mContext,
	// RegexUtils.STYLE_TELEPHONE, et_phone_num.getText()
	// .toString().trim(), true);
	// if (!regextelephone.isAvailable) {
	// showErrorDialog(regextelephone.errorTips);
	// } else {
	// // 获取验证码
	// queryMessageCode();
	// }
	// } else if (v.getId() == R.id.imagebtn_agreement) {
	// // 勾选协议按钮
	// imagebtn.setSelected(!imagebtn.isSelected());
	// isAgree = imagebtn.isSelected();
	// } else if (v.getId() == R.id.tv_agreement) {
	// jumpToFragment(new RemobeContractFragment());
	// }
	// }
	// };

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
		// 身份验证 0x12
		case CODE_AUTHENTICATION:
			// closeProgressDialog();
			handleOpenAuthentication((CheckPersonIdentityResponseModel) result.obj);
			break;
		case CODE_RESEND:// 重发
			closeProgressDialog();
			handResend(result);
			break;
		case CODE_UPLOAD_ID_CARD:// 身份证上传
			UploadidcardResponseModel uploadId = (UploadidcardResponseModel) result.obj;
			onLineCheck(uploadId.picVersion);
			break;
		case CODE_UPLOAD_NET_CHECK:// 远程开户单笔联网核查
			closeProgressDialog();
			resetTimer();
			handOnlineCheck(result);
			break;
		case CODE_QUERY_GET_RANDOM_NUM:// 随机数
			handRandomNum(result);
			break;
		default:
			break;
		}
	}

	/**
	 * 随机数
	 * 
	 * @param result
	 */
	private void handRandomNum(Message result) {
		GetRandomNumResponseModel resultModel = (GetRandomNumResponseModel) result.obj;
		queryOpenAuthentication(resultModel.serverRandom);
	}

	/**
	 * 处理远程开户单笔联网核查
	 * 
	 * @param result
	 */
	private void handOnlineCheck(Message result) {
		RemoteOpenAccOnLineCheck2PivsResponsModel resultModel = (RemoteOpenAccOnLineCheck2PivsResponsModel) result.obj;
		// 1. 更新成功。2. 更新失败
		if ("1".equals(resultModel.success)) {
			jumpToSubStep(mUuid);
		} else if ("2".equals(resultModel.success)) {

		}
	}

	/**
	 * 远程开户单笔联网核查
	 */
	private void onLineCheck(String picVersion) {
		RemoteOpenAccOnLineCheck2PivsParamsModel params = new RemoteOpenAccOnLineCheck2PivsParamsModel();
		params.idNo = addBlankTo(et_identity_num.getText().toString(), 32);
		// 证件类型代码 证件类型描述
		// 01 居民身份证
		// 02 临时身份证
		// 03 护照
		// 04 户口簿
		// 05 军人身份证
		// 06 武装警察身份证
		// 47 港澳居民来往内地通行证（香港）
		// 48 港澳居民来往内地通行证（澳门）
		// 49 台湾居民来往内地通行证
		// 08 外交人员身份证
		// 09 外国人居留许可证
		// 10 边民出入境通行证
		// 11 其它
		params.idType = addBlankTo("01", 2);
		params.name = addBlankTo(TextUtils.isEmpty(resultName) ? et_name_left
				.getText().toString() + et_name_right.getText().toString()
				: resultName, 160);
		params.picId = mPiCid;
		params.picVer = picVersion;
		params.siaChannelFlag = RemoteOpenAccConstant.SIA_CHANNEL_FLAG;// 固定值
		params.siaOrgidt = RemoteOpenAccConstant.SIA_ORGIDT;// 固定值
		mRemoteOpenAccService.getRemoteOpenAccOnLineCheck(params,
				CODE_UPLOAD_NET_CHECK);
	}

	/**
	 * 增加空格以达到长度
	 */
	private String addBlankTo(String str, int i) {
		if (TextUtils.isEmpty(str)) {
			return "";
		}
		int len = i - str.length();
		// 要达到长度还没字符串长返回字符串
		if (len <= 0) {
			return str;
		}
		StringBuilder idBuilder = new StringBuilder();
		idBuilder.append(str);
		for (int j = 0; j < len; j++) {
			idBuilder.append(" ");
		}
		return idBuilder.toString();
	}

	@Override
	public void onTaskFault(Message result) {
		closeProgressDialog();
		// 计时器重置
		resetTimer();
		switch (result.what) {
		// 身份验证 0x12
		case CODE_AUTHENTICATION:
			MAOPException exp = (MAOPException) result.obj;
			showErrorDialog(exp.getMessage());
			break;
		default:
			super.onTaskFault(result);
			break;
		}

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
			// 重发成功
			jumpToFragment(new RemobeApplayOnlineAccResultFragment());
		} else {
			showErrorDialog(resultModel.responseMsg);
		}
	}

	/**
	 * 开户前身份验证
	 */
	private void queryOpenAuthentication(String randomNum) {
		CheckPersonIdentityParamsModel mParams = new CheckPersonIdentityParamsModel();
		mParams.custSurname = et_name_left.getText().toString().trim();
		mParams.custName = rightName;
		mParams.certNo = et_identity_num.getText().toString().trim();
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
		// p604改造 lgw 2016.6.12<<<<
		mParams.orgIdt = "";
		mParams.flag = RemoteOpenAccConstant.CHANNEL_FLAG;
		mRemoteOpenAccService.checkPersonIdentity(mParams, CODE_AUTHENTICATION);
	}

	private String card2nd;// 二类账户账号 X(32)
	private String card3rd;// 三类账户账号 X(32)

	/**
	 * 开户前身份身份验证处理结果
	 * 
	 * @param resultModel
	 */
	private void handleOpenAuthentication(
			CheckPersonIdentityResponseModel resultModel) {
		String responseCode = resultModel.responseCode;
		if ("1000083".equalsIgnoreCase(responseCode)) {
			showMsgTwoBtnDialog(
					"系统检测到您已提交过开户资料，但存在开卡或者关联失败情况，需要重新发送开户申请，是否重新发送？",
					resultModel.reUuid1, resultModel.reUuid2);
		} else if ("0000000".equalsIgnoreCase(responseCode)) {
			// 保存值第三个页面使用
			this.card2nd = resultModel.card2nd;
			this.card3rd = resultModel.card3rd;
			mPiCid = resultModel.piCid;
			mUuid = resultModel.uuid;
			resultName = resultModel.name;// 接口返回姓名为空用识别的姓名
			// 身份证号|姓名拼接
			String meatData = et_identity_num.getText().toString()
					+ "|"
					+ (TextUtils.isEmpty(resultName) ? et_name_left.getText()
							.toString() + et_name_right.getText().toString()
							: resultName);
			// card2nd或者card3rd有一个不为空说明开成功过不上传图片直接下一步
			if (!BocroaUtils.isEmpty(card2nd) || !BocroaUtils.isEmpty(card3rd)) {
				closeProgressDialog();
				resetTimer();
				jumpToSubStep(mUuid);
			} else {
				upLoadIdCard(resultModel.operType, resultModel.piCid, meatData);
			}
			// jumpToSubStep(resultModel);
		} else {
			showErrorDialog(resultModel.responseMsg);
		}

	}

	// private String getPhotopath(String filename) {
	// // 照片全路径
	// String fileName = "";
	// // 文件夹路径
	// String pathUrl = Environment.getExternalStorageDirectory()
	// + RemoteOpenAccConstant.ID_PATH;
	// String imageName = filename + ".jpeg";
	// File file = new File(pathUrl);
	// if (!file.exists()) {
	// file.mkdirs();// 创建文件夹
	// }
	// fileName = pathUrl + imageName;
	// return fileName;
	// }

	/**
	 * 身份证照片上传
	 */
	public void upLoadIdCard(final String operType, final String piCid,
			final String meatData) {
		new Thread() {
			@Override
			public void run() {
				// String front_pic = IDFrontPath.substring(
				// IDFrontPath.lastIndexOf("/") + 1, IDFrontPath.length());
				// String back_pic = IDBackPath.substring(
				// IDBackPath.lastIndexOf("/") + 1, IDBackPath.length());
				UploadidcardParamsModel params = new UploadidcardParamsModel();
				params.imageNameA = et_identity_num.getText().toString()
						+ "_a.jpeg";
				params.imageA = imgToBase64(IDFrontPath);
				params.imageNameB = et_identity_num.getText().toString()
						+ "_b.jpeg";
				params.imageB = imgToBase64(IDBackPath);
				params.metaData = meatData;
				params.operType = operType;
				params.piCid = piCid;
				mRemoteOpenAccService.uploadidcard(params, CODE_UPLOAD_ID_CARD);
				// Message msMessage = Message.obtain();
				// msMessage.obj = params.imageB;
				// handler.sendMessage(msMessage);
			}
		}.start();
	}

	// private Handler handler = new Handler() {
	// public void handleMessage(Message msg) {
	// String a = (String) msg.obj;
	// byte[] decode = Base64.decode(a, Base64.DEFAULT);
	// Bitmap decodeByteArray = BitmapFactory.decodeByteArray(decode, 0,
	// decode.length);
	// iviv.setImageBitmap(decodeByteArray);
	// };
	// };

	/**
	 * 将bitmap转为string
	 * 
	 * @param bitmap
	 * @return
	 */
	public String imgToBase64(Bitmap bitmap) {
		if (bitmap == null) {
			// bitmap not found!!
			return "";
		}
		double MCount = bitmap.getRowBytes() * bitmap.getHeight() / 1024 / 1024;// 得到以M为单位的图片大小
		int compressRate = 100;
		if (MCount > 1) {
			compressRate = (int) (1 / MCount * 100);// 压缩图片
		}
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, compressRate, out);
			out.flush();
			out.close();
			byte[] imgBytes = out.toByteArray();
			return Base64.encodeToString(imgBytes, Base64.DEFAULT).replaceAll(
					"[\\t\\n\\r]", "");
		} catch (Exception e) {
			return null;
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String imgToBase64(String imgPath) {
		Bitmap bitmap = null;
		if (imgPath != null && imgPath.length() > 0) {
			bitmap = readBitmap(imgPath);
		}
		if (bitmap == null) {
			// bitmap not found!!
			return "";
		}
		// int MCount = bitmap.getByteCount()/1024/1024;
		double MCount = bitmap.getRowBytes() * bitmap.getHeight() / 1024 / 1024;// 得到以M为单位的图片大小
		int compressRate = 100;
		if (MCount > 1) {
			compressRate = (int) (1 / MCount * 100);// 压缩图片
		}
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, compressRate, out);
			out.flush();
			out.close();
			byte[] imgBytes = out.toByteArray();
			return Base64.encodeToString(imgBytes, Base64.DEFAULT).replaceAll(
					"[\\t\\n\\r]", "");
		} catch (Exception e) {
			return null;
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private Bitmap readBitmap(String imgPath) {
		try {
			return BitmapFactory.decodeFile(imgPath);
		} catch (Exception e) {
			return null;
		}

	}

	// private String id;// 身份证号
	private String validity_end;// 截止日期
	private String validity_start;// 签发日期
	private String national;// 民族
	private String address;// 地址
	private String IDFrontPath;// 身份证正面
	private String IDBackPath;// 身份证反面

	private void jumpToSubStep(String uuid) {
		subStepFragment = new RemobeOpenAccSubStepFragment();
		((RemoteOpenAccActivity) getActivity()).setNameStr(et_name_left
				.getText().toString().trim()
				+ et_name_right.getText().toString().trim());
		((RemoteOpenAccActivity) getActivity()).setUuidStr(uuid);
		// 将姓名拼音传递
		Bundle args = new Bundle();
		args.putString("spellNameRight", rightNameSpell);
		// 民族、有效期传递
		args.putString("id", et_identity_num.getText().toString().trim());
		args.putString("validity_start", validity_start);
		args.putString("validity_end", validity_end);
		args.putString("national", national);
		args.putString("address", address);
		args.putString("IDFrontPath", IDFrontPath);
		args.putString("IDBackPath", IDBackPath);
		args.putString("card2nd", card2nd);
		args.putString("card3rd", card3rd);
		subStepFragment.setArguments(args);
		// 跳转前将参数传入下一fragment
		((RemoteOpenAccActivity) getActivity()).jump2Fragment(subStepFragment);
	}

	/**
	 * 重发
	 */
	protected void getResendOpenAccount(String reUuid1, String reUuid2) {
		showProgressDialog();
		ResendOpenAccountParamsModel params = new ResendOpenAccountParamsModel();
		params.reUuid1 = reUuid1;
		params.reUuid2 = reUuid2;
		mRemoteOpenAccService.getResendOpenAccountParamsModel(params,
				CODE_RESEND);
	}

	private MessageDialogTwoBtn msgDialog;

	private ImageView iv_id_front;
	private ImageView iv_id_back;
	private String resultName;
	/**
	 * 影像ID String(32) Y 分配的影像ID
	 */
	private String mPiCid;
	/**
	 * uuid String(32) Y 在线开户申请接口需上送该uuid
	 */
	private String mUuid;

	/**
	 * 显示信息
	 * 
	 * @param msg
	 */
	public void showMsgTwoBtnDialog(String msg, final String reUuid1,
			final String reUuid2) {
		if (msgDialog == null) {
			msgDialog = new MessageDialogTwoBtn(getActivity());
			msgDialog.setOnBtnClickListener(new MessageDialogTwoBtn(
					getActivity()).new OnBtnClick() {

				@Override
				public boolean OnOKClick() {
					getResendOpenAccount(reUuid1, reUuid2);
					return false;
				}
			});
		}
		msgDialog.setMsg(msg);
		closeProgressDialog();
		if (!msgDialog.isShowing()) {
			msgDialog.show();
		}
	}

	/**
	 * 查询短信验证码
	 */
	private void queryMessageCode() {
		showProgressDialog(false);
		SendMobileIdentifyCodeParamsModel mParams = new SendMobileIdentifyCodeParamsModel();
		mParams.mobile = et_phone_num.getText().toString().trim();
		mParams.transType = "01";
		mRemoteOpenAccService.sendmobileidentifycode(mParams, CODE_MSG_QUERY);
	}

	/**
	 * 查询短信验证码结果处理
	 */
	private void handlequeryMessageCode(
			SendMobileIdentifyCodeResponseModel resultModel) {
	}

	@Override
	public String getMainTitleText() {
		return getResources().getString(R.string.bocroa_anthentication);
	}

	/**
	 * 身份验证页面校验
	 * 
	 * @return
	 */
	private boolean checkOpenAuthenticationRegex() {
		if (iv_id_front.getDrawable() == null) {
			showErrorDialog("请拍摄身份证照片");
			return false;
		}
		if (iv_id_back.getDrawable() == null) {
			showErrorDialog("请拍摄身份证照片");
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
		// 姓氏
		RegexResult regexFamilyName = RegexUtils.check(mContext,
				RegexUtils.STYLE_FAMILYNAME, et_name_left.getText().toString()
						.trim(), true);
		if (!regexFamilyName.isAvailable) {
			showErrorDialog(regexFamilyName.errorTips);
			return false;
		}
		// add by lxw 姓为1个汉字时，名为空时补全角空格
		String name = et_name_left.getText().toString().trim();
		rightName = et_name_right.getText().toString().trim();
		// rightNameSpell =
		// et_name_right_spell.getText().toString().toUpperCase();// 传参数
		if (1 == name.length()) {// 当姓氏一个字符名可以为空
			// 名和名的拼音不能同时为空
			if (!StringUtil.isNullOrEmpty("")) {// 名的拼音不为空，名也不能为空
				// if (!StringUtil.isNullOrEmpty(et_name_right_spell.getText()
				// .toString().trim())) {// 名的拼音不为空，名也不能为空
				// 名字
				RegexResult regexFirstName = RegexUtils.check(mContext,
						RegexUtils.STYLE_FIRSTNAME, rightName, true);
				if (!regexFirstName.isAvailable) {
					showErrorDialog(regexFirstName.errorTips);
					return false;
				}
			} else {
				if ("".equals(rightNameSpell)) {
					rightNameSpell = "　";
				}
			}
			if ("".equals(rightName)) {
				rightName = "　";
			}
		} else {
			// 名字
			RegexResult regexFirstName = RegexUtils.check(mContext,
					RegexUtils.STYLE_FIRSTNAME, rightName, true);
			if (!regexFirstName.isAvailable) {
				showErrorDialog(regexFirstName.errorTips);
				return false;
			}
		}
		// 姓氏拼音
		RegexResult regexNameSpellLeft = RegexUtils.check(mContext,
				RegexUtils.STYLE_NAMESPELL, "EZACC", true);
		// RegexResult regexNameSpellLeft = RegexUtils.check(mContext,
		// RegexUtils.STYLE_NAMESPELL, et_name_left_spell.getText()
		// .toString().trim().toUpperCase(), true);
		if (!regexNameSpellLeft.isAvailable) {
			showErrorDialog(regexNameSpellLeft.errorTips);
			return false;
		}
		boolean isNullNameSpell = false;// 名拼音是否为空
		if (StringUtil.isNullOrEmpty("EZACC")) {
			isNullNameSpell = false;
		} else {
			isNullNameSpell = true;
		}
		// 名字拼音
		RegexResult regexNameSpellRight = RegexUtils.check(mContext,
				RegexUtils.STYLE_NAMESPELLS, "EZACC", isNullNameSpell);
		// RegexResult regexNameSpellRight = RegexUtils.check(mContext,
		// RegexUtils.STYLE_NAMESPELLS, et_name_right_spell.getText()
		// .toString().trim().toUpperCase(), isNullNameSpell);
		if (!regexNameSpellRight.isAvailable) {
			showErrorDialog(regexNameSpellRight.errorTips);
			return false;
		}
		// // 截止日期
		// RegexResult regexValidityEnd = RegexUtils.check(mContext,
		// RegexUtils.STYLE_VALIDITY_END, et_validity_start.getText()
		// .toString().trim(), true);
		// if (!regexValidityEnd.isAvailable) {
		// showErrorDialog(regexValidityEnd.errorTips);
		// return false;
		// }
		// // 签发日期
		// RegexResult regexValidityStart = RegexUtils.check(mContext,
		// RegexUtils.STYLE_VALIDITY_START, et_validity_end.getText()
		// .toString().trim(), true);
		// if (!regexValidityStart.isAvailable) {
		// showErrorDialog(regexValidityStart.errorTips);
		// return false;
		// }
		// String sysDate = ((RemoteOpenAccActivity) getActivity())
		// .getSystemDateStr();
		// int endDateResult = compareDate(et_validity_end.getText().toString()
		// .trim(), sysDate);
		// if (endDateResult == -1) {
		// showErrorDialog("证件截止日期已过期。");
		// return false;
		// }
		// int startDateResult = compareDate(et_validity_start.getText()
		// .toString().trim(), sysDate);
		// if (startDateResult == 1) {
		// showErrorDialog("证件签发日期晚于当前日期，请修改！");
		// return false;
		// }
		// int dateResult = compareDate(et_validity_start.getText().toString()
		// .trim(), et_validity_end.getText().toString().trim());
		// if (dateResult == 1) {
		// showErrorDialog("证件签发日期晚于截止日期，请修改！");
		// return false;
		// }
		// 手机号
		RegexResult regextelephone = RegexUtils.check(mContext,
				RegexUtils.STYLE_TELEPHONE, et_phone_num.getText().toString()
						.trim(), true);
		if (!regextelephone.isAvailable) {
			showErrorDialog(regextelephone.errorTips);
			return false;
		}
		// 短信验证码
		// String msgCode = et_msg_num.getText().toString().trim();
		// if (StringUtils.isEmptyOrNull(msgCode)) {
		// showErrorDialog(getResources().getString(
		// R.string.bocroa_message_messagecode_null));
		// return false;
		// }
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
		// return false;//TODO
		// }
		// 协议勾选
		if (!imagebtn.isSelected()) {
			showErrorDialog("请选择同意《中国银行股份有限公司个人电子账户服务协议》");
			return false;
		}
		// if (iv_id_front.getDrawable() == null) {
		// showErrorDialog("请拍摄身份证照片");
		// return false;
		// }
		// if (iv_id_back.getDrawable() == null) {
		// showErrorDialog("请拍摄身份证照片");
		// return false;
		// }
		return true;
	}

	// /**
	// * 验证身份证有效期
	// *
	// * @param date
	// * @return 1date大，0相等，-1date小，2其他情况
	// */
	// public int compareDate(String date, String systemDateStr) {
	// // try {
	// // String systemDateStr = ((RemoteOpenAccActivity) getActivity())
	// // .getSystemDateStr();
	// // if (systemDateStr == null) {
	// // return true;// 如果为空先认为有效期正确，
	// // }
	// Date systemDate = null;
	// try {
	// systemDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
	// Locale.getDefault()).parse(systemDateStr);
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	// systemDateStr = new SimpleDateFormat("yyyyMMdd", Locale.getDefault())
	// .format(systemDate);
	// if (Integer.parseInt(date) > Integer.parseInt(systemDateStr)) {
	// return 1;
	// } else if (Integer.parseInt(date) == Integer.parseInt(systemDateStr)) {
	// return 0;
	// } else if (Integer.parseInt(date) < Integer.parseInt(systemDateStr)) {
	// return -1;
	// }
	// return 2;
	// // } catch (ParseException e) {
	// // }
	// }

	/**
	 * @param srcString
	 */
	private String changeHalfToFull(String srcString) {
		srcString = srcString.replace(".", "·");
		return srcString.replace("•", "·");
	}

	private String changeAllHalfToFull(String srcString) {
		srcString = srcString.replaceAll("\\.", "·");
		return srcString.replaceAll("•", "·");
	}

	@Override
	public boolean onGoBackFragment() {
		jumpToHome();
		return false;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		FragmentActivity activity = this.getActivity();
		if (activity != null) {

			InputMethodManager imm = (InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mRoot.getApplicationWindowToken(), 0);
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public void onClick(View v) {
		// 打开识别
		if (v.getId() == R.id.rl_shot_back || v.getId() == R.id.rl_shot_front
				|| v.getId() == R.id.iv_front_shot
				|| v.getId() == R.id.iv_back_shot) {
			openDescern();
		} else if (v.getId() == R.id.btn_authentication_confirm) {
			et_name_left.clearFocus();
			et_name_right.clearFocus();
			if (checkOpenAuthenticationRegex()) {
				getRandomNum();
			}
		} else if (v.getId() == R.id.btn_msg_code) {// 获取验证码
			// 手机号失去焦点
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
		} else if (v.getId() == R.id.imagebtn_agreement) {
			// 勾选协议按钮
			imagebtn.setSelected(!imagebtn.isSelected());
			isAgree = imagebtn.isSelected();
		} else if (v.getId() == R.id.tv_agreement) {
			jumpToFragment(new RemobeContractFragment());
		}

		// 打开识别
		// if (v.getId() == R.id.iv_id || v.getId() == R.id.iv_validity) {
		// openDescern();
		// }
	}
}
