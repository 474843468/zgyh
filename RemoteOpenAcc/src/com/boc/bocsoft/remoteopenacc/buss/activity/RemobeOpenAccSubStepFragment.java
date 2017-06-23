package com.boc.bocsoft.remoteopenacc.buss.activity;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.bocma.global.OPURL;
import com.boc.bocsoft.remoteopenacc.R;
import com.boc.bocsoft.remoteopenacc.buss.model.applyonlineopenaccount.ApplyOnlineOpenAccountParamsModel;
import com.boc.bocsoft.remoteopenacc.buss.model.applyonlineopenaccount.ApplyOnlineOpenAccountResponseModel;
import com.boc.bocsoft.remoteopenacc.buss.model.cardbinquerybycardno.CardBinQueryByCardNoParamsModel;
import com.boc.bocsoft.remoteopenacc.buss.model.cardbinquerybycardno.CardBinQueryByCardNoResponseModel;
import com.boc.bocsoft.remoteopenacc.buss.model.getrandomnum.GetRandomNumParamsModel;
import com.boc.bocsoft.remoteopenacc.buss.model.getrandomnum.GetRandomNumResponseModel;
import com.boc.bocsoft.remoteopenacc.buss.model.queryelecaccopeningbank.QueryElecAccOpeningBankParamsModel;
import com.boc.bocsoft.remoteopenacc.buss.model.queryelecaccopeningbank.QueryElecAccOpeningBankResponseModel;
import com.boc.bocsoft.remoteopenacc.buss.model.queryelecaccopeningbank.QueryElecAccOpeningBankResponseModel.BocBankInfo;
import com.boc.bocsoft.remoteopenacc.buss.model.queryothopenbankbybankid.QueryOthOpenBankByBankIdParamsModel;
import com.boc.bocsoft.remoteopenacc.buss.model.queryothopenbankbybankid.QueryOthOpenBankByBankIdResponseModel;
import com.boc.bocsoft.remoteopenacc.buss.model.queryprovinceandcity.QueryProvinceAndCityParamsModel;
import com.boc.bocsoft.remoteopenacc.buss.model.queryprovinceandcity.QueryProvinceAndCityResponseModel;
import com.boc.bocsoft.remoteopenacc.buss.model.queryprovinceandcity.QueryProvinceAndCityResponseModel.DistMapping;
import com.boc.bocsoft.remoteopenacc.buss.model.resendopenaccount.ResendOpenAccountParamsModel;
import com.boc.bocsoft.remoteopenacc.buss.service.CommonService;
import com.boc.bocsoft.remoteopenacc.buss.service.RemoteOpenAccService;
import com.boc.bocsoft.remoteopenacc.buss.view.BaseSideListView;
import com.boc.bocsoft.remoteopenacc.buss.view.BaseSideListView.BaseSideListViewBtnListener;
import com.boc.bocsoft.remoteopenacc.buss.view.BaseSideListView.BaseSideListViewListener;
import com.boc.bocsoft.remoteopenacc.buss.view.RemobeOpenAccSettingPasswordView;
import com.boc.bocsoft.remoteopenacc.buss.view.RemobeOpenAccSettingPasswordView.SettingPasswordCallback;
import com.boc.bocsoft.remoteopenacc.buss.view.RemobeQueryElecacCattrView;
import com.boc.bocsoft.remoteopenacc.buss.view.RemobeQueryElecacCattrView.QueryElecacCattrViewListener;
import com.boc.bocsoft.remoteopenacc.common.RemoteOpenAccConstant;
import com.boc.bocsoft.remoteopenacc.common.activity.BaseFragment;
import com.boc.bocsoft.remoteopenacc.common.regex.RegexResult;
import com.boc.bocsoft.remoteopenacc.common.regex.RegexUtils;
import com.boc.bocsoft.remoteopenacc.common.util.BocroaUtils;
import com.boc.bocsoft.remoteopenacc.common.util.CardTextWatcher;
import com.boc.bocsoft.remoteopenacc.common.view.BaseEditText;
import com.boc.bocsoft.remoteopenacc.common.view.MessageDialogTwoBtn;
import com.intsig.ccrengine.CCREngine;
import com.intsig.ccrengine.CCREngine.ResultData;
import com.intsig.ccrengine.ISCardScanActivity;

/**
 * 申请三步fragment
 * 
 * @author gwluo
 * 
 */
public class RemobeOpenAccSubStepFragment extends BaseFragment implements
		OnClickListener {
	public View rootView;
	// private static final int TAKE_PICTURE_FRONT = 1;
	// private static final int TAKE_PICTURE_BACK = 2;
	private static final int CARD_NUM_DESCERN = 3;
	private TextView iv_step_one;
	private TextView iv_step_two;
	private TextView iv_step_three;
	private ImageView arraw_to_two;
	private ImageView arraw_to_three;
	private ImageView iv_id_front;
	private ImageView iv_id_back;
	private BaseEditText et_identity_num;
	private Button btn_step_to_next;
	private LinearLayout step_one;
	private ViewGroup step_two;
	private RelativeLayout step_three;
	private RelativeLayout rl_shot_front_float;
	private ViewGroup rl_shot_front;
	private ViewGroup rl_shot_back;
	private RelativeLayout rl_shot_back_float;
	// private LinearLayout ll_belong_bank;
	private LinearLayout ll_open_bank;
	private LinearLayout ll_city;
	private LinearLayout ll_national;
	// private LinearLayout ll_industry;
	private LinearLayout ll_profession;
	// private LinearLayout ll_month_income;
	// private LinearLayout ll_picture_layout;
	private ImageView iv_card;
	// private ImageView tv_count;
	private AnimationDrawable toStepTwoAnimation;
	private AnimationDrawable toStepThreeAnimation;
	// private TextView tv_belong_bank;
	private TextView tv_open_bank;
	private TextView tv_city;
	private TextView tv_profession;
	private TextView tv_national;
	// private TextView tv_industry;
	// private TextView tv_id_notice;// 提示语，保证拍摄照片清晰
	// private TextView tv_month_income;
	private FrameLayout fl_slid_root_view;
	private TranslateAnimation alertAnimation;
	private final int CODE_QUERY_COMMON_OTHERBANK = 001;
	private final int CODE_QUERY_OPENBANK = 002;
	// 生成服务器随机数
	private final int CODE_QUERY_GET_RANDOM_NUM = 003;
	// 电子账户归属地查询 -省
	private final int CODE_QUERY_ACC_OPENING_PROVINCE = 004;
	// 电子账户归属地查询 -市
	private final int CODE_QUERY_ACC_OPENING_CITY = 005;
	// 电子账户开户行查询
	private final int CODE_QUERY_ELEC_ACC_OPENING_BANK = 006;
	// 身份证上传
	// private final int CODE_UPLOAD_ID_CARD = 007;
	// 在线开户
	private final int CODE_APPLY_ONLINE_OPEN_ACCOUNT = 010;
	// 查询所属地
	private final int CODE_QUERY_PROVINCE = 011;
	private final int CODE_QUERY_CITY = 012;
	private final int CODE_QUERY_PROVINCE_PAGE_TWO = 013;// 个人信息页面请求
	private final int CODE_QUERY_CITY_PAGE_TWO = 014;// 个人信息页面请求
	private final int CODE_QUERY_CARD_BIN = 015;// 非农补卡BIN信息
	private final int CODE_QUERY_BANK_CARD_BIN = 016;// 非农补卡BIN开户行
	private final int CODE_RESEND = 017;// 重发
	private RemobeQueryElecacCattrView remobeQueryElecacCattrView;
	// private String spellNameLeft = "";// 姓氏拼音
	// private String spellNameRight = "";// 名字拼音
	private String id;// 身份证号
	private String validity_start;// 签发
	private String validity_end;// 截止
	// private String validity;// 有效期

	private String national;// 民族
	private String address;// 地址
	private String card2nd;// 二类账户账号 X(32)
	private String card3rd;// 三类账户账号 X(32)

	// private String IDFrontPath;// 身份证正面
	// private String IDBackPath;// 身份证反面
	// private String front_pic = "id_front";
	// private String back_pic = "id_reverse";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		remoteOpenAccService = new RemoteOpenAccService(mContext, this);
		commonService = new CommonService(mContext, this);
		Bundle arguments = getArguments();
		if (arguments != null) {
			// spellNameLeft = arguments.getString("spellNameLeft");
			// spellNameRight = arguments.getString("spellNameRight");
			id = arguments.getString("id");
			validity_start = arguments.getString("validity_start");
			validity_end = arguments.getString("validity_end");
			national = arguments.getString("national");
			address = arguments.getString("address");
			card2nd = arguments.getString("card2nd");
			card3rd = arguments.getString("card3rd");
			// IDFrontPath = arguments.getString("IDFrontPath");
			// if (!StringUtil.isNullOrEmpty(IDFrontPath)) {
			// front_pic = IDFrontPath.substring(
			// IDFrontPath.lastIndexOf("/") + 1, IDFrontPath.length());
			// }
			// IDBackPath = arguments.getString("IDBackPath");
			// if (!StringUtil.isNullOrEmpty(IDBackPath)) {
			// back_pic = IDBackPath.substring(
			// IDBackPath.lastIndexOf("/") + 1, IDBackPath.length());
			// }
		}
	}

	@Override
	protected View onCreateView(LayoutInflater mInflater) {
		rootView = mInflater.inflate(R.layout.bocroa_fragment_substep, null);
		return rootView;
	}

	protected void initView() {
		iv_step_one = (TextView) rootView.findViewById(R.id.iv_step_one);
		iv_step_two = (TextView) rootView.findViewById(R.id.iv_step_two);
		iv_step_three = (TextView) rootView.findViewById(R.id.iv_step_three);
		arraw_to_two = (ImageView) rootView.findViewById(R.id.arraw_to_two);
		arraw_to_three = (ImageView) rootView.findViewById(R.id.arraw_to_three);
		iv_id_front = (ImageView) rootView.findViewById(R.id.iv_id_front);
		iv_id_back = (ImageView) rootView.findViewById(R.id.iv_id_back);
		et_identity_num = (BaseEditText) rootView
				.findViewById(R.id.et_identity_num);
		step_one = (LinearLayout) rootView.findViewById(R.id.step_one);
		step_two = (ViewGroup) rootView.findViewById(R.id.step_two);
		step_three = (RelativeLayout) rootView.findViewById(R.id.step_three);
		btn_step_to_next = (Button) rootView
				.findViewById(R.id.btn_step_to_next);
		// tv_belong_bank = (TextView)
		// rootView.findViewById(R.id.tv_belong_bank);
		tv_open_bank = (TextView) rootView.findViewById(R.id.tv_open_bank);
		tv_city = (TextView) rootView.findViewById(R.id.tv_city);
		tv_profession = (TextView) rootView.findViewById(R.id.tv_profession);
		tv_national = (TextView) rootView.findViewById(R.id.tv_national);
		// et_compony_name = (BaseEditText) rootView
		// .findViewById(R.id.et_compony_name);
		// tv_industry = (TextView) rootView.findViewById(R.id.tv_industry);
		// tv_id_notice = (TextView) rootView.findViewById(R.id.tv_id_notice);
		// tv_month_income = (TextView) rootView
		// .findViewById(R.id.tv_month_income);
		tv_name = (TextView) rootView.findViewById(R.id.tv_name);
		// et_full_name = (BaseEditText)
		// rootView.findViewById(R.id.et_full_name);
		et_card_num = (BaseEditText) rootView.findViewById(R.id.et_card_num);
		et_validity_start = (BaseEditText) rootView
				.findViewById(R.id.et_validity_start);
		et_validity_end = (BaseEditText) rootView
				.findViewById(R.id.et_validity_end);
		// et_region = (BaseEditText) rootView.findViewById(R.id.et_region);
		// et_doorplate = (BaseEditText)
		// rootView.findViewById(R.id.et_doorplate);
		// et_post_code = (BaseEditText)
		// rootView.findViewById(R.id.et_post_code);
		iv_back_shot = (ImageView) rootView.findViewById(R.id.iv_back_shot);
		iv_front_shot = (ImageView) rootView.findViewById(R.id.iv_front_shot);
		tv_notice_step_one = (TextView) rootView
				.findViewById(R.id.tv_notice_step_one);
		fl_slid_root_view = (FrameLayout) rootView
				.findViewById(R.id.fl_slid_root_view);
		stepThreeView = new RemobeOpenAccSettingPasswordView(mContext);
		rl_shot_front_float = (RelativeLayout) rootView
				.findViewById(R.id.rl_shot_front_float);
		rl_shot_front = (ViewGroup) rootView.findViewById(R.id.rl_shot_front);
		rl_shot_back = (ViewGroup) rootView.findViewById(R.id.rl_shot_back);
		rl_shot_back_float = (RelativeLayout) rootView
				.findViewById(R.id.rl_shot_back_float);
		// ll_belong_bank = (LinearLayout) rootView
		// .findViewById(R.id.ll_belong_bank);
		ll_open_bank = (LinearLayout) rootView.findViewById(R.id.ll_open_bank);
		ll_city = (LinearLayout) rootView.findViewById(R.id.ll_city);
		ll_national = (LinearLayout) rootView.findViewById(R.id.ll_national);
		// ll_industry = (LinearLayout) rootView.findViewById(R.id.ll_industry);
		ll_profession = (LinearLayout) rootView
				.findViewById(R.id.ll_profession);
		// ll_month_income = (LinearLayout) rootView
		// .findViewById(R.id.ll_month_income);
		// ll_picture_layout = (LinearLayout) rootView
		// .findViewById(R.id.ll_picture_layout);
		iv_card = (ImageView) rootView.findViewById(R.id.iv_card);
	}

	private void initAnimationDrawable() {
		toStepTwoAnimation = (AnimationDrawable) arraw_to_two.getBackground();
		toStepThreeAnimation = (AnimationDrawable) arraw_to_three
				.getBackground();
	}

	// private String provinceCity;

	protected void initData() {
		step_three.addView(stepThreeView);
		// add by lxw
		stepThreeView.setmCallBack(settingPasswordCallback);
		iv_step_one.setSelected(true);
		initAnimationDrawable();
		tv_name.setText(((RemoteOpenAccActivity) getActivity()).getNameStr());
		// et_full_name.setHint(String.format(
		// mContext.getResources().getString(
		// R.string.bocroa_input_full_name), tv_name.getText()
		// .toString()));
		// et_full_name.setTransformationMethod(new InputLowerToUpper());
		applyOnlineparams = new ApplyOnlineOpenAccountParamsModel();
		applyOnlineparams.systemFlag = "bocnet";
		applyOnlineparams.channelFlag = "6";// 渠道标识p601由原来的04改为6
		applyOnlineparams.clientId = OPURL.APPKEY;
		applyOnlineparams.custNo = "";

		setTitle("账户绑定");
		setNoticeText();
		// try {
		// provinceCity = BocroaUtils.readStringFromInputStream(getResources()
		// .getAssets().open(RemoteOpenAccConstant.CITY));
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

	}

	private void setNoticeText() {
		/*
		 * SpannableString spannableString = new SpannableString(
		 * "审核更快：\n请绑定您经常使用的非中行借记卡账户（暂不支持信用卡）。"); spannableString.setSpan(new
		 * ForegroundColorSpan(getResources()
		 * .getColor(R.color.bocroa_gray_b3bbc2)), 0, 10,
		 * SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
		 * spannableString.setSpan(new ForegroundColorSpan(getResources()
		 * .getColor(R.color.bocroa_ef3636)), 10, 14,
		 * SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
		 * spannableString.setSpan(new ForegroundColorSpan(getResources()
		 * .getColor(R.color.bocroa_gray_b3bbc2)), 14, 15,
		 * SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
		 * spannableString.setSpan(new ForegroundColorSpan(getResources()
		 * .getColor(R.color.bocroa_ef3636)), 15, 21,
		 * SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
		 * spannableString.setSpan(new ForegroundColorSpan(getResources()
		 * .getColor(R.color.bocroa_gray_b3bbc2)), 21, spannableString
		 * .length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
		 * tv_notice_step_one.setText(spannableString);
		 */
		// tv_notice_step_one.setText("请绑定您经常使用的非中行借记卡或账户，暂不支持信用卡。");
		tv_notice_step_one
				.setText("请绑定本人名下的常用账户（暂不支持信用卡）\n目前支持：中国银行、中国工商银行、中国农业银行、中国建设银行、交通银行");
		tv_notice_step_one.setTextColor(getResources().getColor(
				R.color.bocroa_gray_b3bbc2));
	}

	// private List<ProvinceCity> provinceCityList;

	// /**
	// * 从省市json取出数据
	// *
	// * @return
	// */
	// private List<ProvinceCity> getProvinceCity() {
	// if (provinceCity != null) {
	// if (provinceCityList == null) {
	// provinceCityList = new ArrayList<ProvinceCity>();
	// try {
	// JSONArray jsonArray = new JSONArray(provinceCity);
	// for (int i = 0; i < jsonArray.length(); i++) {
	// ProvinceCity provinceCity = new ProvinceCity();
	// JSONObject itemJsonObject = jsonArray.getJSONObject(i);
	// provinceCity.provinceName = (String) itemJsonObject
	// .get("name");
	// JSONArray citys = (JSONArray) itemJsonObject
	// .get("citys");
	// provinceCity.cityArray = new String[citys.length()];
	// for (int j = 0; j < citys.length(); j++) {
	// provinceCity.cityArray[j] = (String) citys.get(j);
	// }
	// provinceCityList.add(provinceCity);
	// }
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// }
	// return provinceCityList;
	// } else {
	// return new ArrayList<ProvinceCity>();
	// }
	// }

	// private String[] getProvince() {
	// List<ProvinceCity> provinceCity2 = getProvinceCity();
	// String[] province = new String[provinceCity2.size()];
	// for (int i = 0; i < provinceCity2.size(); i++) {
	// province[i] = provinceCity2.get(i).provinceName;
	// }
	// return province;
	// }

	// private String[] getCitys(String proince) {
	// for (ProvinceCity provinceCity : getProvinceCity()) {
	// if (proince.trim().equalsIgnoreCase(provinceCity.provinceName)) {
	// return provinceCity.cityArray;
	// }
	// }
	// return new String[1];
	// }

	/**
	 * 省市Bean
	 * 
	 * @author gwluo
	 * 
	 */
	public class ProvinceCity {
		public String provinceName;
		public String[] cityArray;
	}

	protected void setListener() {
		iv_id_front.setOnClickListener(this);
		iv_id_back.setOnClickListener(this);
		rl_shot_front_float.setOnClickListener(this);
		rl_shot_front.setOnClickListener(this);
		rl_shot_back.setOnClickListener(this);
		rl_shot_back_float.setOnClickListener(this);
		btn_step_to_next.setOnClickListener(this);
		iv_step_one.setOnClickListener(this);
		iv_step_two.setOnClickListener(this);
		iv_step_three.setOnClickListener(this);
		// ll_belong_bank.setOnClickListener(this);
		ll_open_bank.setOnClickListener(this);
		ll_profession.setOnClickListener(this);
		ll_city.setOnClickListener(this);
		ll_national.setOnClickListener(this);
		// ll_industry.setOnClickListener(this);
		// ll_month_income.setOnClickListener(this);
		iv_back_shot.setOnClickListener(this);
		iv_front_shot.setOnClickListener(this);
		iv_card.setOnClickListener(this);
		// et_card_num.setEditImage(R.drawable.bocroa_id_have_shut);
		// et_card_num.setBaseEditCallBack(new BaseEditCallBack() {
		//
		// @Override
		// public void onJump2PicDescern() {
		// jumpToDescern();
		// }
		//
		// });
		et_card_num.addEditTextWatcher(new CardTextWatcher(et_card_num
				.getEditText()) {
			@Override
			public void textChangeCallBack(String str) {
				// 账号改变清空保存开户行名数据
				othCardBankName = "";
				othCardTopCnaps = "";
				tv_open_bank.setText("");
			}
		});
	}

	private void openDescern() {
		Intent intent = new Intent(
				RemobeOpenAccSubStepFragment.this.getActivity(),
				ISCardScanActivity.class);
		intent.putExtra(ISCardScanActivity.EXTRA_KEY_ORIENTATION,
				ISCardScanActivity.ORIENTATION_VERTICAL);
		intent.putExtra(ISCardScanActivity.EXTRA_KEY_GET_NUMBER_IMG, true);
		intent.putExtra(ISCardScanActivity.EXTRA_KEY_GET_TRIMED_IMG,
				"/sdcard/trimedcard.jpg");
		intent.putExtra(ISCardScanActivity.EXTRA_KEY_GET_ORIGINAL_IMG,
				"/sdcard/origianlcard.jpg");
		intent.putExtra(ISCardScanActivity.EXTRA_KEY_APP_KEY,
				RemoteOpenAccConstant.APP_KEY);
		intent.putExtra(ISCardScanActivity.EXTRA_KEY_TIPS, "请将银行卡置于框内将自动识别");
		startActivityForResult(intent, CARD_NUM_DESCERN);
	}

	// private final String INDUSTRYTITLE = "industry";// 行业
	private final String NATIONALTITLE = "national";// 民族
	private final String OPENBANKTITLE = "openbank";// 开户行
	private final String PROFESSIONTITLE = "profession";// 职业
	private final String PROVINCETITLE = "province";// 省、直辖市
	private final String CITYTITLE = "city";// 市

	// private final String INCOMETITLE = "income";// 收入

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.rl_shot_front || id == R.id.iv_id_front
				|| id == R.id.iv_front_shot) {// 打开相机
			// startCamera2(front_pic, TAKE_PICTURE_FRONT);
		} else if (id == R.id.rl_shot_front_float) {// 预览
			// showImagePreview(front_pic);
		} else if (id == R.id.rl_shot_back_float) {// 预览
			// showImagePreview(back_pic);
		} else if (id == R.id.iv_id_back || id == R.id.rl_shot_back
				|| id == R.id.iv_back_shot) {// 打开相机
			// startCamera2(back_pic, TAKE_PICTURE_BACK);
		} else if (id == R.id.btn_step_to_next) {// 下一步
			if (step_one.getVisibility() == View.VISIBLE) {
				// 开户行查询过并且和所选一致跳转
				jumpToStep(2);
			} else if (step_two.getVisibility() == View.VISIBLE) {
				jumpToStep(3);
			}
		} else if (id == R.id.iv_step_one) {// 他行账户
			jumpToStep(1);
		} else if (id == R.id.iv_step_two) {// 个人信息
			if (iv_step_two.isSelected()) {
				jumpToStep(2);
			}
		} else if (id == R.id.iv_step_three) {// 开户信息
			if (iv_step_three.isSelected()) {
				jumpToStep(3);
			}
		} else if (id == R.id.ll_open_bank) {// 开户行
			if (BocroaUtils.isEmpty(et_card_num.getText().toString().trim())) {
				showErrorDialog("请输入您的银行卡号。");
			} else if (et_card_num.getText().toString().replaceAll(" ", "")
					.length() < 16) {
				showErrorDialog("请检查您的银行卡号位数。");
			} else {
				showOpenBankView();
			}
		} else if (id == R.id.ll_profession) {// 职业
			showProfessionView();
		}
		// else if (id == R.id.ll_city) {// 省份和城市
		// getProvince();
		// }
		else if (id == R.id.ll_national) {// 民族
			showNationalView();
		}
		// else if (id == R.id.ll_industry) {// 行业
		// showIndustryView();
		// }
		// else if (id == R.id.ll_month_income) {// 收入
		// showIncomeView();
		// }
		else if (id == R.id.ll_belong_bank) {// 所属银行
			// belongBankView = new BelongBankView(mContext);
			// belongBankView.setBelongBankCallBack(new BelongBankCallBack() {
			//
			// @Override
			// public void onItemClick(String name, String code) {
			// selectOthCardTopCnaps = code;
			// setBelongBank(name);
			// onSelectViewSelected();
			// }
			// });
			// alertSelectView(belongBankView, "选择所属银行");
			// getQueryCommonOtherbankList();
		} else if (id == R.id.iv_card) {// 卡号识别
			openDescern();
		}
	}

	private boolean isJump = false;// 开户行查询成功是否跳转下一页
	private boolean isOcrCall = false;// 成功后是否回显卡号

	/**
	 * 1.2.14 【SA9180】查询非农补卡BIN信息接口 成功后查 1.2.15 【SA9205】非农补卡BIN信息查询开户行接口
	 * 
	 * @param jump
	 *            成功后是否跳转
	 * @param isOcrCall
	 *            是否ocr识别
	 */
	private void getCardBin(boolean jump, boolean isSetCard) {
		isJump = jump;
		this.isOcrCall = isSetCard;
		showProgressDialog();
		CardBinQueryByCardNoParamsModel params = new CardBinQueryByCardNoParamsModel();
		if (isSetCard) {
			params.cardNo = cardNum.replaceAll(" ", "");
		} else {
			params.cardNo = et_card_num.getText().toString()
					.replaceAll(" ", "");
		}
		remoteOpenAccService.getCardBinQueryByCardNo(params,
				CODE_QUERY_CARD_BIN);
	}

	/**
	 * 照片预览
	 * 
	 * @param bitmap
	 */
	// private void showImagePreview(String name) {
	// Intent intent = new Intent();
	// intent.setClass(mContext, RemoteOpenAccImageViewActivity.class);
	// intent.putExtra("i", getPhotopath(name));
	// startActivity(intent);
	// }

	protected void setBelongBank(String othCardBankName) {
		// tv_belong_bank.setText(othCardBankName);
	}

	// private final int default_pagenum = 1;// 默认起始数
	// private int current_pagenum = 1;// 当前起始数
	// private final int default_pagesize = 10;// 默认条数
	// private String openBankTerm = "";// 开户行的搜索条件
	// private boolean isOpenBankFresh = false;// 是否是刷新表示
	private String openBankOthCardCnaps = "";// 开户行人行行号

	/**
	 * 所属银行
	 */
	// private void showOpenBankView() {
	// openBankView = new OpenBankView(mContext);
	// openBankView.setOpenBankCallBack(new OpenBankViewCallBack() {
	//
	// @Override
	// public void onSearch(String term) {
	// MessageDialog messageDialog = new MessageDialog(mContext);
	// messageDialog.setOnOkClick(new OnOkClick() {
	//
	// @Override
	// public boolean OnOKClick() {
	// openBankView.alertInputMethod();
	// return false;
	// }
	// });
	// if (term.trim().length() == 0) {
	// messageDialog.setErrorData("开户行名称栏位不可为空，请填写。");
	// if (!messageDialog.isShowing()) {
	// messageDialog.show();
	// }
	// return;
	// }
	// RegexResult regexValidity = RegexUtils.check(mContext,
	// RegexUtils.STYLE_OPENBANKQUERY, term, true);
	// if (!regexValidity.isAvailable) {
	// messageDialog.setErrorData(regexValidity.errorTips);
	// if (!messageDialog.isShowing()) {
	// messageDialog.show();
	// }
	// return;
	// }
	// isOpenBankFresh = false;
	// openBankTerm = term;
	// getQueryOpeningBank(term, default_pagenum, default_pagesize);
	// }
	//
	// @Override
	// public void onItemClick(int position) {
	// setOpenbank(openBankList.get(position).othCardBankName);
	// openBankOthCardCnaps = openBankList.get(position).othCardCnaps;
	// onSelectViewSelected();
	// }
	// });
	// openBankView.setXListViewFreshListener(new IXListViewListener2() {
	//
	// @Override
	// public void onRefresh(XListView xListView) {
	//
	// }
	//
	// @Override
	// public void onLoadMore(XListView xListView) {
	// isOpenBankFresh = true;
	// if (openBankListCount < default_pagesize) {
	// openBankView.endRefresh();
	// return;
	// }
	// current_pagenum = current_pagenum + default_pagenum;
	// getQueryOpeningBank(openBankTerm, current_pagenum,
	// default_pagesize);
	// }
	// });
	// alertSelectView(openBankView, "搜索开户行");
	// }

	// private void showIncomeView() {
	// BaseSideListView incomeView = new BaseSideListView(mContext);
	// incomeLists = mContext.getResources().getStringArray(
	// R.array.bocroa_income);
	// List<Map<String, Object>> incomeDataList = new ArrayList<Map<String,
	// Object>>();
	// for (String incomeItem : incomeLists) {
	// Map<String, Object> map = new HashMap<String, Object>();
	// map.put(INCOMETITLE, incomeItem);
	// incomeDataList.add(map);
	// }
	// String[] incomeTitle = new String[1];
	// incomeTitle[0] = INCOMETITLE;
	// int[] incomeId = new int[1];
	// incomeId[0] = R.id.tv_is_cbr_se_base_list_item;
	// incomeView.setData(incomeDataList, incomeTitle, incomeId);
	// alertSelectView(incomeView, "月收入");
	// incomeView.setCallBackListener(new BaseSideListViewListener() {
	//
	// @Override
	// public void onItemClickListener(int pos) {
	// // setIncome(incomeLists[pos]);
	// onSelectViewSelected();
	// }
	// });
	// }

	private String currentProvinceName = "";// 记录当前省份

	private void showProvinceCityView(final List<DistMapping> list) {
		BaseSideListView provinceView = new BaseSideListView(mContext);
		// provinceLists = getProvince();
		List<Map<String, Object>> provinceDataList = new ArrayList<Map<String, Object>>();
		for (DistMapping provinceItem : list) {
			// for (String provinceItem : provinceLists) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(PROVINCETITLE, provinceItem.distName);
			provinceDataList.add(map);
		}
		String[] provinceTitle = new String[1];
		provinceTitle[0] = PROVINCETITLE;
		int[] provinceId = new int[1];
		provinceId[0] = R.id.tv_is_cbr_se_base_list_item;
		provinceView.setData(provinceDataList, provinceTitle, provinceId);
		alertSelectView(provinceView, "居住城市");
		provinceView.setCallBackListener(new BaseSideListViewListener() {

			@Override
			public void onItemClickListener(int pos) {
				currentProvinceName = list.get(pos).distName;
				getCity(list.get(pos).distCode);
				// showCityView(provinceLists[pos]);
			}
		});
	}

	/**
	 * 请求城市
	 * 
	 * @param distCode
	 */
	protected void getCity(String distCode) {
		showProgressDialog();
		QueryProvinceAndCityParamsModel params = new QueryProvinceAndCityParamsModel();
		params.distType = "1";
		params.distCode = distCode;
		params.pageFlag = "0";
		remoteOpenAccService.queryProvinceAndCity(params,
				CODE_QUERY_CITY_PAGE_TWO);
	}

	/**
	 * 请求省份和直辖市
	 */
	// private void getProvince() {
	// showProgressDialog();
	// QueryProvinceAndCityParamsModel params = new
	// QueryProvinceAndCityParamsModel();
	// params.distType = "0";
	// params.distCode = "";
	// params.pageFlag = "0";
	// remoteOpenAccService.queryProvinceAndCity(params,
	// CODE_QUERY_PROVINCE_PAGE_TWO);
	// }

	protected void showCityView(final String province,
			final List<DistMapping> cityList) {
		final BaseSideListView cityView = new BaseSideListView(mContext);
		cityView.setTitle(province);
		// cityLists = getCitys(province);
		// 直辖市没有下一级
		if (cityList.size() == 0) {
			setProvinceAndCity(province);
			onSelectViewSelected();
			return;
		}
		// 第二次弹出窗口
		List<Map<String, Object>> cityDataList = new ArrayList<Map<String, Object>>();
		for (DistMapping cityItem : cityList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(CITYTITLE, cityItem.distName);
			cityDataList.add(map);
		}
		String[] cityTitle = new String[1];
		cityTitle[0] = CITYTITLE;
		int[] cityId = new int[1];
		cityId[0] = R.id.tv_is_cbr_se_base_list_item;
		cityView.setData(cityDataList, cityTitle, cityId);
		alertSelectView(cityView, "");// 标题传入“”防止标题错误
		cityView.setCallBackListener(new BaseSideListViewListener() {

			@Override
			public void onItemClickListener(int pos) {
				// 控制直辖市显示
				if (province.contains("北京") || province.contains("天津")
						|| province.contains("上海") || province.contains("重庆")) {
					setProvinceAndCity(province);
				} else {
					setProvinceAndCity(province + cityList.get(pos).distName);
				}
				cityView.setTitle(province + cityList.get(pos).distName);
				onSelectViewSelected();
			}
		});
	}

	public Properties loadProperties(String name) {
		InputStream ins = null;
		Properties properties = new Properties();
		ins = getClass().getClassLoader().getResourceAsStream(name);
		try {
			properties.load(new InputStreamReader(ins, "utf-8"));
			ins.close();
		} catch (Exception e) {
		} finally {
			if (ins != null) {
				try {
					ins.close();
				} catch (IOException e) {
					ins = null;
				}
			}
		}
		return properties;
	}

	// private void setIndustry(String industry) {
	// // tv_industry.setText(industry);
	// }

	private void setNational(String national) {
		tv_national.setText(national);
	}

	/**
	 * 行业
	 */
	// private void showIndustryView() {
	// BaseSideListView professionView = new BaseSideListView(mContext);
	// industryList = mContext.getResources().getStringArray(
	// R.array.bocroa_industry);
	// List<Map<String, Object>> industryDataList = new ArrayList<Map<String,
	// Object>>();
	// for (String industryItem : industryList) {
	// Map<String, Object> map = new HashMap<String, Object>();
	// map.put(INDUSTRYTITLE, industryItem);
	// industryDataList.add(map);
	// }
	// String[] industryTitle = new String[1];
	// industryTitle[0] = INDUSTRYTITLE;
	// int[] id = new int[1];
	// id[0] = R.id.tv_is_cbr_se_base_list_item;
	// professionView.setData(industryDataList, industryTitle, id);
	// alertSelectView(professionView, "选择行业");
	// professionView.setCallBackListener(new BaseSideListViewListener() {
	//
	// @Override
	// public void onItemClickListener(int pos) {
	// setIndustry(industryList[pos]);
	// onSelectViewSelected();
	// }
	// });
	// }

	/**
	 * 显示开户行view
	 */
	private void showOpenBankView() {
		BaseSideListView openBankView = new BaseSideListView(mContext);
		openBankList = mContext.getResources().getStringArray(
				R.array.bocroa_open_bank);
		List<Map<String, Object>> openBankDataList = new ArrayList<Map<String, Object>>();
		for (String openBankItem : openBankList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(OPENBANKTITLE, openBankItem);
			openBankDataList.add(map);
		}
		String[] openBankTitle = new String[1];
		openBankTitle[0] = OPENBANKTITLE;
		int[] openBankId = new int[1];
		openBankId[0] = R.id.tv_is_cbr_se_base_list_item;
		openBankView.setData(openBankDataList, openBankTitle, openBankId);
		alertSelectView(openBankView, "选择开户银行");
		openBankView.setCallBackListener(new BaseSideListViewListener() {

			@Override
			public void onItemClickListener(int pos) {
				setOpenbank(openBankList[pos]);
				onSelectViewSelected();
			}
		});
	}

	/**
	 * 显示民族view
	 */
	private void showNationalView() {
		BaseSideListView professionView = new BaseSideListView(mContext);
		nationalList = mContext.getResources().getStringArray(
				R.array.bocroa_national);
		List<Map<String, Object>> nationalDataList = new ArrayList<Map<String, Object>>();
		for (String nationalItem : nationalList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(NATIONALTITLE, nationalItem);
			nationalDataList.add(map);
		}
		String[] professionTitle = new String[1];
		professionTitle[0] = NATIONALTITLE;
		int[] professionId = new int[1];
		professionId[0] = R.id.tv_is_cbr_se_base_list_item;
		professionView.setData(nationalDataList, professionTitle, professionId);
		alertSelectView(professionView, "选择民族");
		professionView.setCallBackListener(new BaseSideListViewListener() {

			@Override
			public void onItemClickListener(int pos) {
				setNational(nationalList[pos]);
				onSelectViewSelected();
			}
		});
	}

	/**
	 * 显示职业view
	 */
	private void showProfessionView() {
		BaseSideListView professionView = new BaseSideListView(mContext);
		professionLists = mContext.getResources().getStringArray(
				R.array.bocroa_profession);
		List<Map<String, Object>> professionDataList = new ArrayList<Map<String, Object>>();
		for (String professionItem : professionLists) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(PROFESSIONTITLE, professionItem);
			professionDataList.add(map);
		}
		String[] professionTitle = new String[1];
		professionTitle[0] = PROFESSIONTITLE;
		int[] professionId = new int[1];
		professionId[0] = R.id.tv_is_cbr_se_base_list_item;
		professionView.setData(professionDataList, professionTitle,
				professionId);
		alertSelectView(professionView, "选择职业");
		professionView.setCallBackListener(new BaseSideListViewListener() {

			@Override
			public void onItemClickListener(int pos) {
				setProfession(professionLists[pos]);
				onSelectViewSelected();
			}
		});
	}

	/**
	 * 设置省份,市
	 * 
	 * @param string
	 */
	protected void setProvinceAndCity(String string) {
		tv_city.setText(string);
	}

	/**
	 * 设置收入
	 * 
	 * @param string
	 */
	// protected void setIncome(String string) {
	// // tv_month_income.setText(string);
	// }

	/**
	 * 设置职业
	 * 
	 * @param string
	 */
	protected void setProfession(String string) {
		tv_profession.setText(string);
	}

	/**
	 * 设置开户行
	 * 
	 * @param string
	 */
	protected void setOpenbank(String string) {
		tv_open_bank.setText(string);
	}

	private TranslateAnimation hidAnimation;
	private TranslateAnimation selectedAnimation;
	// private BaseEditText et_full_name;
	private BaseEditText et_card_num;
	private BaseEditText et_validity_start;// 有效期
	private BaseEditText et_validity_end;// 有效期
	// private BaseEditText et_region;// 区县
	// private BaseEditText et_doorplate;// 街道
	// private BaseEditText et_post_code;// 邮编
	// private BaseEditText et_compony_name;// 单位名称
	private TextView tv_name;
	private ImageView iv_back_shot;
	private ImageView iv_front_shot;
	private TextView tv_notice_step_one;
	// private String[] industryList;
	private String[] nationalList;
	private String[] openBankList;
	private String[] professionLists;

	// private String[] provinceLists;
	// private String[] cityLists;
	// private String[] incomeLists;

	/**
	 * 弹出窗口选中后从上往下隐藏
	 */
	public void onSelectViewSelected() {
		if (fl_slid_root_view.getVisibility() == View.VISIBLE) {
			if (selectedAnimation == null) {
				selectedAnimation = new TranslateAnimation(
						TranslateAnimation.RELATIVE_TO_SELF, 0,
						TranslateAnimation.RELATIVE_TO_SELF, 0,
						TranslateAnimation.RELATIVE_TO_SELF, 0,
						TranslateAnimation.RELATIVE_TO_SELF, 1.0f);
				selectedAnimation.setDuration(ANIMATION_DURATION);
				selectedAnimation.setAnimationListener(new AnimationListener() {

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
			fl_slid_root_view.startAnimation(selectedAnimation);
			returnBeforTitle();
			hidSoftInput(fl_slid_root_view);
		}
	}

	/**
	 * 弹出窗口从右往左弹出
	 * 
	 * @param belongBankView
	 */
	public void alertSelectView(View view, String title) {
		fl_slid_root_view.removeAllViews();
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
		if (title.length() != 0) {
			setTitle(title);
		}
		hidSoftInput(fl_slid_root_view);
	}

	private void hidSoftInput(View view) {
		InputMethodManager inputManager = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(view.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 查询常用他行列表
	 */
	// private void getQueryCommonOtherbankList() {
	// showProgressDialog();
	// QueryCommonOtherBanklistParamsModel params = new
	// QueryCommonOtherBanklistParamsModel();
	// params.othCardBankName = "";
	// params.othCardTopCnaps = "";
	// params.pageNum = "0";
	// params.pageSize = "0";
	// remoteOpenAccService.queryCommonOtherbankList(params,
	// CODE_QUERY_COMMON_OTHERBANK);
	// }

	private final long ANIMATION_DURATION = 500;

	/**
	 * 查询开户行，开户行模糊查询
	 */
	// public void getQueryOpeningBank(String term, int pNum, int pSize) {
	// if (!isOpenBankFresh) {
	// showProgressDialog();
	// }
	// QueryOpeningBankParamsModel params = new QueryOpeningBankParamsModel();
	// params.othCardBankName = term;
	// params.othCardTopCnaps = selectOthCardTopCnaps;
	// params.pageNum = pNum + "";
	// params.pageSize = pSize + "";
	// remoteOpenAccService.queryOpeningBank(params, CODE_QUERY_OPENBANK);
	// }

	/**
	 * 弹出窗口从左往右隐藏
	 */
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
			returnBeforTitle();
		}
	}

	public boolean isViewShow() {
		return fl_slid_root_view.getVisibility() == View.VISIBLE;
	}

	/**
	 * 开启动画
	 * 
	 * @param animation
	 */
	private void startArrawAnimation(AnimationDrawable animation) {
		animation.stop();// 动画有可能是在运行，先停止再运行
		animation.start();
	}

	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	// /**
	// * 打开系统自带相机应用
	// *
	// * @param openCameraIntent
	// * @param filename
	// * @param type
	// */
	// private void startCamera(Intent openCameraIntent, String filename, int
	// type) {
	// File out = new File(getPhotopath(filename));
	// Uri uri = Uri.fromFile(out);
	// openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
	// startActivityForResult(openCameraIntent, type);
	// }

	/**
	 * 打开自定义相机
	 * 
	 * @param openCameraIntent
	 * @param filename
	 * @param type
	 */
	// private void startCamera2(String filename, int type) {
	// Intent intent = new Intent();
	// intent.setClass(mContext, CameraActivity.class);
	// intent.putExtra("fileName", filename);
	// startActivityForResult(intent, type);
	// }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == FragmentActivity.RESULT_CANCELED) {
			return;
		}
		switch (requestCode) {
		// case TAKE_PICTURE_FRONT:// 正面照片
		// bmFront = ImageUtil.decodeSampledBitmapFromResource(
		// getPhotopath(front_pic), iv_id_front.getWidth(),
		// iv_id_front.getHeight());
		// Bitmap rotateBitmap2 = ImageUtil.getRotateBitmap(bmFront, -90);
		// setFrontImage(rotateBitmap2);
		// break;
		// case TAKE_PICTURE_BACK:// 反面照片
		// bmBack = ImageUtil.decodeSampledBitmapFromResource(
		// getPhotopath(back_pic), iv_id_back.getWidth(),
		// iv_id_back.getHeight());
		// Bitmap rotateBitmap = ImageUtil.getRotateBitmap(bmBack, -90);
		// setBackImage(rotateBitmap);
		// break;
		case CARD_NUM_DESCERN:// 卡号识别
			handCardNumDescern(resultCode, data);
			break;
		}
	}

	// private void setBackImage(Bitmap rotateBitmap) {
	// iv_id_back.setImageBitmap(rotateBitmap);
	// rl_shot_back_float.setVisibility(View.VISIBLE);
	// rl_shot_back.setVisibility(View.GONE);
	// resetPicLayout(rotateBitmap);
	// }

	// private void setFrontImage(Bitmap rotateBitmap2) {
	// iv_id_front.setImageBitmap(rotateBitmap2);
	// rl_shot_front_float.setVisibility(View.VISIBLE);
	// rl_shot_front.setVisibility(View.GONE);
	// resetPicLayout(rotateBitmap2);
	// }
	private String cardNum = "";// 保存ocr识别回来的卡号

	/**
	 * 处理卡号识别结果
	 */
	private void handCardNumDescern(int resultCode, Intent data) {
		if (resultCode == FragmentActivity.RESULT_OK) {
			ResultData result = (ResultData) data
					.getSerializableExtra(ISCardScanActivity.EXTRA_KEY_RESULT);
			if (CCREngine.CCR_TYPE_CREDIT_CARD == result.getBankCardType()
					|| CCREngine.CCR_TYPE_QUASI_CREDIT_CARD == result
							.getBankCardType()) {
				showErrorDialog(getString(R.string.bocroa_not_support_card));
				return;
			}
			cardNum = result.getCardNumber();// 保存卡号
			getCardBin(false, true);
			// String tmp = result.getCardInsName();
			// if (tmp != null) {
			// tv_open_bank.setText(tmp);
			// }
		}
	}

	// private void resetPicLayout(Bitmap i) {
	// int hei = DisplayUtil.getScreenMetrics(mContext).x / 2 * i.getHeight()
	// / i.getWidth() + BocroaUtils.dip2px(mContext, 6);
	// ll_picture_layout.setLayoutParams(new LinearLayout.LayoutParams(
	// LinearLayout.LayoutParams.MATCH_PARENT, hei));
	// }

	/**
	 * 跳到第几步
	 * 
	 * @param to
	 *            跳转到第几页
	 */
	private void jumpToStep(int to) {
		if (!checkPage(to)) {
			if (RemoteOpenAccConstant.isCheck) {
				return;
			}
		}
		switch (to) {
		case 1:
			step_one.setVisibility(View.VISIBLE);
			step_two.setVisibility(View.GONE);
			step_three.setVisibility(View.GONE);
			btn_step_to_next.setVisibility(View.VISIBLE);
			iv_step_one.setSelected(true);
			setTitle("账户绑定");
			break;
		case 2:
			initPageOneParams();
			step_one.setVisibility(View.GONE);
			step_two.setVisibility(View.VISIBLE);
			step_three.setVisibility(View.GONE);
			startArrawAnimation(toStepTwoAnimation);
			btn_step_to_next.setVisibility(View.VISIBLE);
			iv_step_two.setSelected(true);
			setTitle("完善个人信息");
			// if (TextUtils.isEmpty(et_validity.getText().toString().trim())) {
			// et_validity.setText(validity);
			// }
			if (TextUtils.isEmpty(tv_national.getText().toString())) {
				tv_national.setText(national);
			}
			if (TextUtils.isEmpty(et_validity_start.getText().toString())) {
				et_validity_start.setText(validity_start);
			}
			if (TextUtils.isEmpty(et_validity_end.getText().toString())) {
				et_validity_end.setText(validity_end);
			}
			et_identity_num.setText(id);
			// Bitmap fbmp = ImageUtil.loadBitmap(IDFrontPath);
			// if (fbmp != null) {
			// setFrontImage(fbmp);
			// rl_shot_front.setEnabled(false);
			// iv_id_front.setEnabled(false);
			// rl_shot_front_float.setEnabled(false);
			// iv_front_shot.setVisibility(View.GONE);
			// }
			// Bitmap bbmp = ImageUtil.loadBitmap(IDBackPath);
			// if (bbmp != null) {
			// setBackImage(bbmp);
			// rl_shot_back.setEnabled(false);
			// iv_id_back.setEnabled(false);
			// rl_shot_back_float.setEnabled(false);
			// iv_back_shot.setVisibility(View.GONE);
			// }
			// if (fbmp != null && bbmp != null) {
			// tv_id_notice.setVisibility(View.GONE);
			// }
			break;
		case 3:
			initPageOneParams();
			initPageTwoParams();
			// 开始跳转到设置密码画面
			beginJumpToSettingPasswordView();
			// upLoadIdCard(getPhotopath(front_pic), getPhotopath(back_pic));
			break;

		default:
			break;
		}
	}

	/**
	 * 将页面二的值付给提交参数
	 */
	private void initPageTwoParams() {
		// 605 日期格式变化 上送参数修改 lgw 2016.6.27
		String start_date = et_validity_start.getText().toString()
				.replaceAll("\\.", "");
		if (start_date.equals(RemoteOpenAccConstant.ID_DATE_LONGTIME1)
				|| start_date.equals(RemoteOpenAccConstant.ID_DATE_LONGTIME2)) {
			start_date = RemoteOpenAccConstant.ID_DEFAULT_DATE;
		}
		applyOnlineparams.certValidDateFrom = start_date;
		String end_date = et_validity_end.getText().toString()
				.replaceAll("\\.", "");
		if (end_date.equals(RemoteOpenAccConstant.ID_DATE_LONGTIME1)
				|| end_date.equals(RemoteOpenAccConstant.ID_DATE_LONGTIME2)) {
			end_date = RemoteOpenAccConstant.ID_DEFAULT_DATE;
		}
		applyOnlineparams.certValidDateTo = end_date;
		applyOnlineparams.custAddress1 = address;// 识别地址
		applyOnlineparams.custAddress2 = "EZACC";
		applyOnlineparams.custAddress3 = "EZACC";
		applyOnlineparams.zipCode = "999999";// 邮编
		// applyOnlineparams.zipCode = et_post_code.getText().toString();
		applyOnlineparams.nation = loadProperties("natinals").getProperty(
				tv_national.getText().toString());// 民族
		applyOnlineparams.vocation = loadProperties("vocation").getProperty(
				tv_profession.getText().toString());// 职业
		// applyOnlineparams.companyType =
		// loadProperties("industry").getProperty(
		// tv_industry.getText().toString());// 行业
		// 单位名称
		// applyOnlineparams.companyName = et_compony_name.getText().toString()
		// .trim();
		// applyOnlineparams.salary = loadProperties("salary").getProperty(
		// tv_month_income.getText().toString());// 月收入
	}

	/**
	 * 将页面一的值付给提交参数
	 */
	private void initPageOneParams() {
		applyOnlineparams.uuid = ((RemoteOpenAccActivity) getActivity())
				.getUuidStr();
		applyOnlineparams.custNameSpell = "EZACC";// 姓拼音
		applyOnlineparams.custNameSpells = "EZACC";// 名拼音
		applyOnlineparams.othCardNo = et_card_num.getText().toString()
				.replaceAll(" ", "").trim();// 他行卡账号,去掉空格
		applyOnlineparams.othCardTopCnaps = othCardTopCnaps;// 他行卡开户银行人行行号总行人行号
		applyOnlineparams.othCardBankName = tv_open_bank.getText().toString()
				.trim();// 他行开户行名称
	}

	private String[] titleArray = new String[2];

	/**
	 * 设置标题
	 * 
	 * @param string
	 */
	private void setTitle(String string) {
		titleArray[0] = titleArray[1];
		titleArray[1] = string;
		tv_titleText.setText(titleArray[titleArray.length - 1]);
	}

	/**
	 * 返回上次的标题
	 * 
	 */
	private void returnBeforTitle() {
		titleArray[1] = titleArray[0];
		titleArray[0] = "";
		tv_titleText.setText(titleArray[titleArray.length - 1]);
	}

	/**
	 * @param to
	 *            要跳转到的页面
	 * @return
	 */
	private boolean checkPage(int to) {
		int i = 0;
		if (step_one.getVisibility() == View.VISIBLE) {
			i = 1;
		} else if (step_two.getVisibility() == View.VISIBLE) {
			i = 2;
		} else if (step_three.getVisibility() == View.VISIBLE) {
			i = 3;
		}
		// 跳转当前页面不校验
		if (i == to) {
			return false;
		}
		switch (i) {
		case 1:
			// 姓名
			RegexResult regexName = RegexUtils.check(mContext,
					RegexUtils.STYLE_NAME, tv_name.getText().toString().trim(),
					true);
			if (!regexName.isAvailable) {
				showErrorDialog(regexName.errorTips);
				return false;
			}
			// 拼音
			// RegexResult regexNameSpell = RegexUtils.check(mContext,
			// RegexUtils.STYLE_NAMESPELL, et_full_name.getText()
			// .toString().trim().toUpperCase(), true);
			// if (!regexNameSpell.isAvailable) {
			// showErrorDialog(regexNameSpell.errorTips);
			// return false;
			// }
			// 卡号账号
			RegexResult regexBankNum = RegexUtils.check(mContext,
					RegexUtils.STYLE_OTHERBANKCARKNUM, et_card_num.getText()
							.toString().replaceAll(" ", "").trim(), true);
			if (!regexBankNum.isAvailable) {
				showErrorDialog(regexBankNum.errorTips);
				return false;
			}
			// if (tv_belong_bank.getText().toString().length() == 0) {
			// showErrorDialog("所属银行栏位不可为空，请选择。");
			// return false;
			// }
			if (TextUtils.isEmpty(tv_open_bank.getText().toString())) {
				showErrorDialog("请选择您的开户行。");
				return false;
			}
			// 开户行请求成功并且和所选开户行不同
			if (TextUtils.isEmpty(othCardBankName)) {
				getCardBin(true, false);
				return false;
			} else {
				if (!othCardBankName.equals(tv_open_bank.getText().toString())) {
					showErrorDialog(getString(R.string.bocroa_check_open_bank));
					return false;
				}
			}

			break;
		case 2:
			// if (iv_id_front.getDrawable() == null) {
			// showErrorDialog("请拍摄身份证照片");
			// return false;
			// }
			// if (iv_id_back.getDrawable() == null) {
			// showErrorDialog("请拍摄身份证照片");
			// return false;
			// }
			// 有效期
			// RegexResult regexValidity = RegexUtils.check(mContext,
			// RegexUtils.STYLE_VALIDITY, et_validity.getText().toString()
			// .trim(), true);
			// if (!regexValidity.isAvailable) {
			// showErrorDialog(regexValidity.errorTips);
			// return false;
			// }
			//
			// if (!validateDate(et_validity.getText().toString().trim())) {
			// showErrorDialog("有效日期已过期。");
			// return false;
			// }

			String sysDate = ((RemoteOpenAccActivity) getActivity())
					.getSystemDateStr();
			// 605 日期格式修改，校验修改 lgw 2016.6.27
			// 签发日期不是长期和长期有效
			if (!et_validity_start.getText().toString().trim()
					.equals(RemoteOpenAccConstant.ID_DATE_LONGTIME1)
					&& !et_validity_start.getText().toString().trim()
							.equals(RemoteOpenAccConstant.ID_DATE_LONGTIME2)) {
				// 不带点格式的日期
				String noFormatStartData = BocroaUtils.transDateFormat(
						et_validity_start.getText().toString().trim(),
						"yyyy.MM.dd", "yyyyMMdd");
				RegexResult regexValidityStart = RegexUtils.check(mContext,
						RegexUtils.STYLE_VALIDITY_START, noFormatStartData,
						true);
				if (!regexValidityStart.isAvailable) {
					showErrorDialog(regexValidityStart.errorTips);
					return false;
				}
			}
			// 截止日期不是长期和长期有效
			if (!et_validity_end.getText().toString().trim()
					.equals(RemoteOpenAccConstant.ID_DATE_LONGTIME1)
					&& !et_validity_end.getText().toString().trim()
							.equals(RemoteOpenAccConstant.ID_DATE_LONGTIME2)) {
				String noFormatEndData = BocroaUtils.transDateFormat(
						et_validity_end.getText().toString().trim(),
						"yyyy.MM.dd", "yyyyMMdd");
				RegexResult regexValidityEnd = RegexUtils.check(mContext,
						RegexUtils.STYLE_VALIDITY_END, noFormatEndData, true);
				if (!regexValidityEnd.isAvailable) {
					showErrorDialog(regexValidityEnd.errorTips);
					return false;
				}
			}
			// 永久身份证不校验
			if (!RemoteOpenAccConstant.ID_DATE_LONGTIME1
					.equalsIgnoreCase(et_validity_start.getText().toString()
							.trim())
					&& !RemoteOpenAccConstant.ID_DATE_LONGTIME2
							.equalsIgnoreCase(et_validity_start.getText()
									.toString().trim())) {
				// 不带点格式的日期
				String noFormatStartData = BocroaUtils.transDateFormat(
						et_validity_start.getText().toString().trim(),
						"yyyy.MM.dd", "yyyyMMdd");
				// 签发日期
				int startDateResult = compareDate(noFormatStartData, sysDate);
				if (startDateResult == 1) {
					showErrorDialog("证件签发日期晚于当前日期，请修改！");
					return false;
				}
			}
			if (!RemoteOpenAccConstant.ID_DATE_LONGTIME1
					.equalsIgnoreCase(et_validity_end.getText().toString()
							.trim())
					&& !RemoteOpenAccConstant.ID_DATE_LONGTIME2
							.equalsIgnoreCase(et_validity_end.getText()
									.toString().trim())) {
				String noFormatEndData = BocroaUtils.transDateFormat(
						et_validity_end.getText().toString().trim(),
						"yyyy.MM.dd", "yyyyMMdd");
				// 不带点格式的日期
				String noFormatStartData = BocroaUtils.transDateFormat(
						et_validity_start.getText().toString().trim(),
						"yyyy.MM.dd", "yyyyMMdd");
				// 截止日期
				int endDateResult = compareDate(noFormatEndData, sysDate);
				if (endDateResult == -1) {
					showErrorDialog("证件截止日期已过期。");
					return false;
				}
				// 证件签发日期晚于截止日期
				int dateResult = compareDate(noFormatStartData, noFormatEndData);
				if (dateResult == 1) {
					showErrorDialog("证件签发日期晚于截止日期，请修改！");
					return false;
				}
			}

			if (tv_national.getText().toString().length() == 0) {
				showErrorDialog("民族栏位不可为空，请选择。");
				return false;
			}
			// if (tv_city.getText().toString().length() == 0) {
			// showErrorDialog("居住城市栏位不可为空，请选择。");
			// return false;
			// }
			// 所在区县
			// RegexResult regexRegion = RegexUtils.check(mContext,
			// RegexUtils.STYLE_REGION, et_region.getText().toString()
			// .trim(), true);
			// if (!regexRegion.isAvailable) {
			// showErrorDialog(regexRegion.errorTips);
			// return false;
			// }
			// 详细地址
			// RegexResult regexDetailAdd = RegexUtils.check(mContext,
			// RegexUtils.STYLE_DETAILADDRESS, et_doorplate.getText()
			// .toString().trim(), true);
			// if (!regexDetailAdd.isAvailable) {
			// showErrorDialog(regexDetailAdd.errorTips);
			// return false;
			// }
			// 邮政编码
			// RegexResult regexPostCode = RegexUtils.check(mContext,
			// RegexUtils.STYLE_POSTCODE, et_post_code.getText()
			// .toString().trim(), true);
			// if (!regexPostCode.isAvailable) {
			// showErrorDialog(regexPostCode.errorTips);
			// return false;
			// }
			if (tv_profession.getText().toString().length() == 0) {
				showErrorDialog("职业栏位不可为空，请选择。");
				return false;
			}
			// 行业,当职业代码不为Z0：无职业活动人员时，必输；其他情况可输
			// if
			// (!"Z0".equalsIgnoreCase(loadProperties("industry").getProperty(
			// tv_industry.getText().toString()))) {
			// if (tv_industry.getText().toString().length() == 0) {
			// showErrorDialog("行业栏位不可为空，请选择。");
			// return false;
			// }
			// }
			// 单位名称,当职业代码不为Z0：无职业活动人员时，必输；其他情况可输
			// boolean comNameIsMust = true;// 公司名称是否为必输
			// if
			// (!"Z0".equalsIgnoreCase(loadProperties("vocation").getProperty(
			// tv_profession.getText().toString()))) {
			// comNameIsMust = true;
			// } else {
			// comNameIsMust = false;
			// }
			// RegexResult regexCompanyName = RegexUtils.check(mContext,
			// RegexUtils.STYLE_COMPANY_NAME, et_compony_name.getText()
			// .toString().trim(), comNameIsMust);
			// if (!regexCompanyName.isAvailable) {
			// showErrorDialog(regexCompanyName.errorTips);
			// return false;
			// }
			// 月收入
			// RegexResult regexSalary = RegexUtils.check(
			// mContext,
			// RegexUtils.STYLE_SALARY,
			// loadProperties("salary").getProperty(
			// tv_month_income.getText().toString().trim()), true);
			// if (!regexSalary.isAvailable) {
			// showErrorDialog(regexSalary.errorTips);
			// return false;
			// }
			break;
		case 3:
			// 校验密码页面

			break;

		default:
			break;
		}
		return true;
	}

	/**
	 * 验证身份证有效期
	 * 
	 * @param date
	 * @return 1date大，0相等，-1date小，2其他情况
	 */
	public int compareDate(String date, String systemDateStr) {
		// try {
		// String systemDateStr = ((RemoteOpenAccActivity) getActivity())
		// .getSystemDateStr();
		// if (systemDateStr == null) {
		// return true;// 如果为空先认为有效期正确，
		// }
		Date systemDate = null;
		Date inputDate = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd",
				Locale.getDefault());
		try {
			systemDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.getDefault()).parse(systemDateStr);
			systemDate = simpleDateFormat.parse(simpleDateFormat
					.format(systemDate));
			inputDate = simpleDateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return 2;
		}
		return inputDate.compareTo(systemDate);
		// systemDateStr = new SimpleDateFormat("yyyyMMdd", Locale.getDefault())
		// .format(systemDate);
		// if (Integer.parseInt(date) > Integer.parseInt(systemDateStr)) {
		// return 1;
		// } else if (Integer.parseInt(date) == Integer.parseInt(systemDateStr))
		// {
		// return 0;
		// } else if (Integer.parseInt(date) < Integer.parseInt(systemDateStr))
		// {
		// return -1;
		// }
		// return 2;
		// } catch (ParseException e) {
		// }
	}

	// /**
	// * 打开系统自带相机应用
	// *
	// * @param openCameraIntent
	// * @param filename
	// * @param type
	// */
	// private void startCamera(Intent openCameraIntent, String filename, int
	// type) {
	// File out = new File(getPhotopath(filename));
	// Uri uri = Uri.fromFile(out);
	// openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
	// startActivityForResult(openCameraIntent, type);
	// }

	// private String picPath = "/bocroaPic/";
	private RemobeOpenAccSettingPasswordView stepThreeView;
	private RemoteOpenAccService remoteOpenAccService;
	// private BelongBankView belongBankView;// 此类可删除
	// private String selectOthCardTopCnaps = "";// 选中的总行机构号
	// private List<OtherBankInfo> othBankInfoList;;// 此类可删除
	// private OpenBankView openBankView;;// 此类可删除
	private CommonService commonService;

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

	// private Handler handler = new Handler() {
	// public void handleMessage(Message msg) {
	// switch (msg.what) {
	// case 1:
	//
	// break;
	//
	// default:
	// break;
	// }
	// // String path = getPhotopath("copy");
	// // byte[] decode = Base64.decode((String) msg.obj, Base64.DEFAULT);
	// // saveBitmap(BitmapFactory.decodeByteArray(decode, 0,
	// // decode.length),
	// // path);
	// }
	// };

	/**
	 * 保存Bitmap到sdcard
	 * 
	 * @param b
	 */
	public void saveBitmap(Bitmap b, String path) {
		try {
			FileOutputStream fout = new FileOutputStream(path);
			BufferedOutputStream bos = new BufferedOutputStream(fout);
			b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// /**
	// * 身份证照片上传
	// */
	// public void upLoadIdCard(final String filepathA, final String filepathB)
	// {
	// showProgressDialog();
	// new Thread() {
	// @Override
	// public void run() {
	// UploadidcardParamsModel params = new UploadidcardParamsModel();
	// params.imageNameA = "身份证号_A.jpeg";
	// params.imageA = imgToBase64(filepathA);
	// params.imageNameB = "身份证号_B.jpeg";
	// params.imageB = imgToBase64(filepathB);
	// remoteOpenAccService.uploadidcard(params, CODE_UPLOAD_ID_CARD);
	// // Message msMessage = Message.obtain();
	// // msMessage.obj = params.imageA;
	// // handler.sendMessage(msMessage);
	// }
	// }.start();
	// }

	// public String imgToBase64(String imgPath) {
	// Bitmap bitmap = null;
	// if (imgPath != null && imgPath.length() > 0) {
	// bitmap = readBitmap(imgPath);
	// }
	// if (bitmap == null) {
	// // bitmap not found!!
	// return "";
	// }
	// // int MCount = bitmap.getByteCount()/1024/1024;
	// double MCount = bitmap.getRowBytes() * bitmap.getHeight() / 1024 /
	// 1024;// 得到以M为单位的图片大小
	// int compressRate = 100;
	// if (MCount > 1) {
	// compressRate = (int) (1 / MCount * 100);// 压缩图片
	// }
	// ByteArrayOutputStream out = null;
	// try {
	// out = new ByteArrayOutputStream();
	// bitmap.compress(Bitmap.CompressFormat.JPEG, compressRate, out);
	// out.flush();
	// out.close();
	// byte[] imgBytes = out.toByteArray();
	// return Base64.encodeToString(imgBytes, Base64.DEFAULT).replaceAll(
	// "[\\t\\n\\r]", "");
	// } catch (Exception e) {
	// return null;
	// } finally {
	// try {
	// out.flush();
	// out.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }

	// private Bitmap readBitmap(String imgPath) {
	// try {
	// return BitmapFactory.decodeFile(imgPath);
	// } catch (Exception e) {
	// return null;
	// }
	//
	// }

	protected void showToast(String text) {
		Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onTaskSuccess(Message result) {
		closeProgressDialog();
		switch (result.what) {
		case CODE_QUERY_BANK_CARD_BIN:// 非农补卡BIN信息开户行
			handBankCardBin(result);
			break;
		case CODE_QUERY_CARD_BIN:// 非农补卡BIN信息
			handGetCardBin(result);
			break;
		case CODE_QUERY_COMMON_OTHERBANK:// 所属银行
			// handCommonOtherBank(result);
			break;
		case CODE_QUERY_OPENBANK:// 开户行
			// handOpenBank(result);
			break;
		case CODE_QUERY_GET_RANDOM_NUM:// 随机数
			endJumpToSettingPasswordView(result);
			break;
		case CODE_QUERY_ACC_OPENING_PROVINCE:
			endQueryElecAccAttrProvince(result);
			break;
		case CODE_QUERY_ACC_OPENING_CITY:
			endQueryElecAccAttrCity(result);
			break;
		case CODE_QUERY_ELEC_ACC_OPENING_BANK:
			endQueryElecAccoOpeningBank(result);
			break;
		case CODE_APPLY_ONLINE_OPEN_ACCOUNT:// 在线开户
			handOnlineOpenAcc(result);
			break;
		// case CODE_UPLOAD_ID_CARD:
		// beginJumpToSettingPasswordView();
		// break;
		case CODE_QUERY_PROVINCE:
			// 查询所属地返回
			endQueryProvinceAndCity(result, "0");
			break;
		case CODE_QUERY_PROVINCE_PAGE_TWO:// 个人信息页面请求省份
			showProvinceCityView(handProvinceAndCity(result));
			break;
		case CODE_QUERY_CITY:
			endQueryProvinceAndCity(result, "1");
			break;
		case CODE_QUERY_CITY_PAGE_TWO:// 个人信息页面请求城市
			showCityView(currentProvinceName, handProvinceAndCity(result));
			break;
		case CODE_RESEND:// 重发
			closeProgressDialog();
			handResend(result);
			break;
		default:
			break;
		}
	}

	/**
	 * 处理重发
	 * 
	 * @param result
	 */
	private void handResend(Message result) {
		jumpToFragment(new RemobeApplayOnlineAccResultFragment());
	}

	private RemobeOpenAccNavigationFragment navigationFragment;

	/**
	 * 处理在线开户结果
	 * 
	 * @param result
	 */
	private void handOnlineOpenAcc(Message result) {
		final ApplyOnlineOpenAccountResponseModel resultModel = (ApplyOnlineOpenAccountResponseModel) result.obj;
		if ("0".equalsIgnoreCase(resultModel.isOnlineSubmit)) {// 联机提交，当时可看到提交结果
			String isReapply = isReApply(resultModel);
			if (!"".equals(isReapply)) {// 开户失败，重新申请
				RemobeApplayOnlineAccResultFragment reApplyFragment = new RemobeApplayOnlineAccResultFragment() {
					@Override
					public boolean onBtnClick() {
						if (navigationFragment == null) {
							navigationFragment = new RemobeOpenAccNavigationFragment();
						}
						((RemoteOpenAccActivity) getActivity()).jump2Fragment(
								navigationFragment, false);
						return true;
					}

					@Override
					public String getMainTitleText() {
						return "开户失败";
					}
				};
				Bundle reApply = new Bundle();
				reApply.putInt("drawable", R.drawable.bocroa_progress_fail);
				reApply.putString("title", "开户失败");
				reApply.putString("notice", isReapply);
				reApply.putString("btnName", "重新申请");
				reApplyFragment.setArguments(reApply);
				jumpToFragment(reApplyFragment);
			} else if (!"".equals(isResend(resultModel))) {// 开户失败，重发,重发后还是走调度
				RemobeApplayOnlineAccResultFragment resendFragment = new RemobeApplayOnlineAccResultFragment() {
					@Override
					public boolean onBtnClick() {
						String uuid = resultModel.inputInfSaveUuid;
						String reUuid1 = "";// 二类（二、三类）
						String reUuid2 = "";// 三类
						if ("01".equals(openTypeString)) {// 二类
							reUuid1 = uuid;
						} else if ("02".equals(openTypeString)) {// 三类
							reUuid2 = uuid;
						} else if ("03".equals(openTypeString)) {// 二类和三类
							reUuid1 = uuid;
						}
						getResendOpenAccount(reUuid1, reUuid2);
						return true;
					}

					@Override
					public String getMainTitleText() {
						return "开户失败";
					}
				};
				Bundle reSend = new Bundle();
				reSend.putInt("drawable", R.drawable.bocroa_progress_fail);
				reSend.putString("title", "开户失败");
				reSend.putString("notice", isResend(resultModel));
				reSend.putString("btnName", "重新发送");
				resendFragment.setArguments(reSend);
				jumpToFragment(resendFragment);
			} else {// 开户成功
				RemobeApplayOnlineAccResultFragment succOpenAcc = new RemobeApplayOnlineAccResultFragment() {
					@Override
					public boolean onBtnClick() {
						RemoteOpenAccActivity mActivity = (RemoteOpenAccActivity) getActivity();
						mActivity.setResult(Activity.RESULT_OK);
						mActivity.finish();
						return true;
					}

					@Override
					public String getMainTitleText() {
						return "开户成功";
					}
				};
				Bundle succ = new Bundle();
				succ.putInt("drawable", R.drawable.bocroa_progress_success);
				succ.putString("title", "开户成功");
				succ.putString("notice", getSuccMsg(resultModel));
				succ.putString("btnName", "立即登录手机银行");
				succOpenAcc.setArguments(succ);
				jumpToFragment(succOpenAcc);
			}
		} else if ("1".equalsIgnoreCase(resultModel.isOnlineSubmit)) {// 走调试，需要到进度查询查看
			endApplyOnlineOpenAccount(result);
		}
	}

	private String getSuccMsg(ApplyOnlineOpenAccountResponseModel model) {
		// 如果失败原因（借用此字段，其实是提示信息）不为空显示此字段
		if (!BocroaUtils.isEmpty(model.failReason)) {
			return model.failReason;
		}
		if ("1000123".equals(model.resultCode)) {// 开通e财
			return String.format(getString(R.string.bocroa_sub_succ1),
					model.vcardNo, applyOnlineparams.orgName);
		} else if ("1000124".equals(model.resultCode)) {// 开通e捷
			return String.format(getString(R.string.bocroa_sub_succ2),
					model.vcardExNo, applyOnlineparams.orgName);
		} else if ("1000121".equals(model.resultCode)) {// 开通e财和e捷
			return String.format(getString(R.string.bocroa_sub_succ3),
					model.vcardNo, applyOnlineparams.orgName, model.vcardExNo,
					applyOnlineparams.orgName);
		}
		return "";
	}

	/**
	 * 重发
	 */
	public void getResendOpenAccount(String reUuid1, String reUuid2) {
		showProgressDialog();
		ResendOpenAccountParamsModel params = new ResendOpenAccountParamsModel();
		params.reUuid1 = reUuid1;
		params.reUuid2 = reUuid2;
		remoteOpenAccService.getResendOpenAccountParamsModel(params,
				CODE_RESEND);
	}

	/**
	 * 是否重新发送
	 * 
	 * @param resultModel
	 * @return
	 */
	private String isResend(ApplyOnlineOpenAccountResponseModel resultModel) {
		String resuCode = resultModel.resultCode;
		if ("1000116".equalsIgnoreCase(resuCode)) {
			return "您的中银E财账户开户申请失败，原因是：您的账户信息异常，您可以点击重新申请，我行将根据您之前提交的开户信息为您重新申请中银E财账户。";
		} else if ("1000117".equalsIgnoreCase(resuCode)) {
			return "您的中银E捷账户开户申请失败，原因是：您的账户信息异常，您可以点击重新申请，我行将根据您之前提交的开户信息为您重新申请中银E捷账户。";
		} else if ("1000118".equalsIgnoreCase(resuCode)) {
			return "您的中银E财、中银E捷账户开户申请失败，原因是：您的账户信息异常，您可以点击重新申请，我行将根据您之前提交的开户信息为您重新申请中银E财、中银E捷账户。";
		}
		return "";
	}

	/**
	 * 是否重新申请
	 * 
	 * @param resultModel
	 * @return
	 */
	private String isReApply(ApplyOnlineOpenAccountResponseModel resultModel) {
		String resuCode = resultModel.resultCode;
		if ("1000008".equalsIgnoreCase(resuCode)) {// 开户信息不完整，请重新提交开户信息
			return "开户信息不完整，请重新提交开户信息";
		} else if ("1000162".equalsIgnoreCase(resuCode)) {// 您的中银E财账户开户申请失败，原因是：绑定的I类账户异常。您可以进入首页点击“我要开户”进行申请！
			return "您的中银E财账户开户申请失败，原因是：绑定的I类账户异常。您可以进入首页点击“我要开户”进行申请！";
		} else if ("1000175".equalsIgnoreCase(resuCode)) {// 您的开户的身份证号，姓名与核心预留的不一致，请到中国银行网点去办理。
			return "您的开户的身份证号，姓名与核心预留的不一致，请到中国银行网点去办理。";
		} else if ("1000163".equalsIgnoreCase(resuCode)) {// 您的中银E财账户开户申请失败，原因是：您的个人客户信息异常。您可以进入首页点击“我要开户”进行申请！
			return "您的中银E财账户开户申请失败，原因是：您的个人客户信息异常。您可以进入首页点击“我要开户”进行申请！";
		} else if ("1000135".equalsIgnoreCase(resuCode)) {// 对不起，您未提交中银E财账户申请。您可以进入首页点击“我要开户”进行申请！
			return "对不起，您未提交中银E财账户申请。您可以进入首页点击“我要开户”进行申请！";
		} else if ("1000164".equalsIgnoreCase(resuCode)) {// 您的中银E捷账户开户申请失败，原因是：绑定的I类账户异常。您可以进入首页点击“我要开户”进行申请！
			return "您的中银E捷账户开户申请失败，原因是：绑定的I类账户异常。您可以进入首页点击“我要开户”进行申请！";
		} else if ("1000165".equalsIgnoreCase(resuCode)) {// 您的中银E捷账户开户申请失败，原因是：您的个人客户信息异常。您可以进入首页点击“我要开户”进行申请！
			return "您的中银E捷账户开户申请失败，原因是：您的个人客户信息异常。您可以进入首页点击“我要开户”进行申请！";
		} else if ("1000136".equalsIgnoreCase(resuCode)) {// 对不起，您未提交中银E捷账户申请。您可以进入首页点击“我要开户”进行申请！
			return "对不起，您未提交中银E捷账户申请。您可以进入首页点击“我要开户”进行申请！";
		} else if ("1000166".equalsIgnoreCase(resuCode)) {// 您的中银E财、中银E捷账户开户申请失败，原因是：绑定的I类账户异常。您可以进入首页点击“我要开户”进行申请！
			return "您的中银E财、中银E捷账户开户申请失败，原因是：绑定的I类账户异常。您可以进入首页点击“我要开户”进行申请！";
		} else if ("1000167".equalsIgnoreCase(resuCode)) {// 您的中银E财、中银E捷账户开户申请失败，原因是：您的个人客户信息异常。您可以进入首页点击“我要开户”进行申请！
			return "您的中银E财、中银E捷账户开户申请失败，原因是：您的个人客户信息异常。您可以进入首页点击“我要开户”进行申请！";
		} else if ("1000007".equalsIgnoreCase(resuCode)) {// 对不起，您未提交中银E财，中银E捷账户申请。您可以进入首页点击“我要开户”进行申请！
			return "对不起，您未提交中银E财，中银E捷账户申请。您可以进入首页点击“我要开户”进行申请！";
		}
		return "";
	}

	/**
	 * 开户行查询返回总行机构号 String (16) Y
	 */
	private String othCardTopCnaps;//
	/**
	 * 开户行查询返回他行开户行名称 String (50) Y
	 */
	private String othCardBankName;//

	private void handBankCardBin(Message result) {
		QueryOthOpenBankByBankIdResponseModel resultModel = (QueryOthOpenBankByBankIdResponseModel) result.obj;
		if (TextUtils.isEmpty(resultModel.othCardBankName)) {// 没有查到结果
			showErrorDialog(getString(R.string.bocroa_not_support_card));
		} else {
			// ocr识别回显卡号，输入不设置
			if (isOcrCall) {
				et_card_num.setText(cardNum);
			}
			// 设置卡号会清空返回结果，不能换顺序
			othCardTopCnaps = resultModel.othCardTopCnaps;
			othCardBankName = resultModel.othCardBankName;
			// 开户行为空才并且开户行是五大行才回显
			if (!"-1".equalsIgnoreCase(othCardTopCnaps)
					&& TextUtils.isEmpty(tv_open_bank.getText().toString())) {
				setOpenbank(resultModel.othCardBankName);
			}
			// 请求成功，比较所选和请求结果是否匹配
			if (tv_open_bank.getText().toString().equals(othCardBankName)) {
				if (isJump) {
					jumpToStep(2);
				}
			} else {
				if (!TextUtils.isEmpty(tv_open_bank.getText().toString())) {
					showErrorDialog(getString(R.string.bocroa_check_open_bank));
				}
			}
		}
	}

	/**
	 * @param result
	 */
	private void handGetCardBin(Message result) {
		CardBinQueryByCardNoResponseModel resultModel = (CardBinQueryByCardNoResponseModel) result.obj;
		getBankOfCardBin(resultModel.bankId);
	}

	/**
	 * 非农补卡BIN信息查询开户行
	 * 
	 * @param bankId
	 */
	private void getBankOfCardBin(String bankId) {
		QueryOthOpenBankByBankIdParamsModel params = new QueryOthOpenBankByBankIdParamsModel();
		params.othCardTopCnaps = bankId;
		remoteOpenAccService.getQueryOthOpenBankByBankId(params,
				CODE_QUERY_BANK_CARD_BIN);
	}

	/**
	 * 取出省份或者城市集合返回
	 * 
	 * @param result
	 * @return
	 */
	private List<DistMapping> handProvinceAndCity(Message result) {
		QueryProvinceAndCityResponseModel resultModel = (QueryProvinceAndCityResponseModel) result.obj;
		List<DistMapping> distMappingList = resultModel.distMappingList;
		return distMappingList;
	}

	@Override
	public void onTaskFault(Message msg) {
		closeProgressDialog();
		switch (msg.what) {
		case CODE_QUERY_COMMON_OTHERBANK:
			break;
		case CODE_QUERY_OPENBANK:
			// openBankView.endRefresh();
			break;
		// case CODE_UPLOAD_ID_CARD:
		// break;
		default:
			break;
		}
		super.onTaskFault(msg);
	}

	// private final String BANKNAME = "bankName";
	// private List<QueryOpeningBankResponseModel.OtherBankInfo> openBankList =
	// new ArrayList<QueryOpeningBankResponseModel.OtherBankInfo>();
	// private int openBankListCount = -1;

	/**
	 * 开户行
	 * 
	 * @param result
	 */
	// private void handOpenBank(Message result) {
	// QueryOpeningBankResponseModel resultModel =
	// (QueryOpeningBankResponseModel) result.obj;
	// openBankListCount = resultModel.othBankInfoList.size();
	// if (isOpenBankFresh) {
	// openBankList.addAll(resultModel.othBankInfoList);
	// } else {
	// openBankList = resultModel.othBankInfoList;
	// }
	// // 将数据转为适合适配器使用的集合
	// List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	// for (QueryOpeningBankResponseModel.OtherBankInfo item : openBankList) {
	// Map<String, Object> map = new HashMap<String, Object>();
	// map.put(BANKNAME, item.othCardBankName);
	// list.add(map);
	// }
	// String[] contents = new String[1];
	// contents[0] = BANKNAME;
	// int[] viewId = new int[1];
	// viewId[0] = R.id.tv_is_cbr_se_base_list_item;
	// openBankView.setData(list, contents, viewId);
	// openBankView.endRefresh();
	// if (list.size() > 0) {
	// hidSoftInput(fl_slid_root_view);
	// }
	// }

	/**
	 * 处理普通开户行
	 * 
	 * @param result
	 */
	// private void handCommonOtherBank(Message result) {
	// QueryCommonOtherBanklistResponseModel resultModel =
	// (QueryCommonOtherBanklistResponseModel) result.obj;
	// othBankInfoList = resultModel.othBankInfoList;
	// List<SortModel> commOtherBanks = new ArrayList<SortModel>();
	// for (int i = 0; i < othBankInfoList.size(); i++) {
	// SortModel sortModel = new SortModel();
	// sortModel.setCode(othBankInfoList.get(i).othCardTopCnaps);
	// sortModel.setName(othBankInfoList.get(i).othCardBankName);
	// commOtherBanks.add(sortModel);
	// }
	// if (belongBankView != null) {
	// belongBankView.setData(commOtherBanks);
	// }
	// }

	@Override
	public String getMainTitleText() {
		return "账户绑定";
	}

	/**
	 * 开始跳转到设置密码画面
	 */
	protected void beginJumpToSettingPasswordView() {
		GetRandomNumParamsModel params = new GetRandomNumParamsModel();
		params.systemFlag = "bocnet";
		commonService.getRandomNum(params, CODE_QUERY_GET_RANDOM_NUM);
	}

	/**
	 * 网络请求返回，页面进行跳转
	 */
	protected void endJumpToSettingPasswordView(Message result) {
		GetRandomNumResponseModel resultModel = (GetRandomNumResponseModel) result.obj;
		applyOnlineparams.rs = resultModel.serverRandom;
		stepThreeView.initSipbox(resultModel.serverRandom);
		btn_step_to_next.setVisibility(View.GONE);
		step_one.setVisibility(View.GONE);
		step_two.setVisibility(View.GONE);
		step_three.setVisibility(View.VISIBLE);
		iv_step_three.setSelected(true);
		startArrawAnimation(toStepThreeAnimation);
		setTitle("设置开户信息");
	}

	/**
	 * 开始电子账户归属地查询
	 */
	// protected void beginQueryElecAccAttr(QueryElecAccAttrParamsModel params,
	// int requestCod) {
	// showProgressDialog();
	// remoteOpenAccService.queryElecAccAttr(params, requestCod);
	// }

	/**
	 * 电子账户归属地查询返回处理
	 */
	protected void endQueryElecAccAttrProvince(Message result) {
		QueryElecAccOpeningBankResponseModel resultModel = (QueryElecAccOpeningBankResponseModel) result.obj;

		closeProgressDialog();

		if (remobeQueryElecacCattrView == null) {
			remobeQueryElecacCattrView = new RemobeQueryElecacCattrView(
					mContext);

			remobeQueryElecacCattrView
					.showOpeningBankView(resultModel.bocBankInfoList);

			remobeQueryElecacCattrView
					.setmQueryCityViewListener(new RemobeQueryElecacCattrView.QueryCityViewListener() {

						@Override
						public void onQueryCityItemClickListener(
								DistMapping dist) {
							QueryProvinceAndCityParamsModel params = new QueryProvinceAndCityParamsModel();
							params.distType = "1";
							params.distCode = dist.distCode;
							params.pageFlag = "1";
							beginQueryCity(params);

						}
					});
		} else {
			remobeQueryElecacCattrView
					.showOpeningBankView(resultModel.bocBankInfoList);
		}

		// RemobeQueryElecacCattrView viewe = new RemobeQueryElecacCattrView(
		// / mContext);
		// viewe.showProvinceView(resultModel.elecAccAttrInfoList);

		alertSelectView(remobeQueryElecacCattrView, "");

	}

	protected void endQueryElecAccAttrCity(Message result) {
		QueryElecAccOpeningBankResponseModel resultModel = (QueryElecAccOpeningBankResponseModel) result.obj;
		if (resultModel == null || resultModel.bocBankInfoList == null
				|| resultModel.bocBankInfoList.size() == 0) {
			stepThreeView.setSelectedOpenBank(remobeQueryElecacCattrView
					.getCurrentBocBank());
			onSelectViewSelected();
		} else {
			remobeQueryElecacCattrView
					.showOpeningBankView(resultModel.bocBankInfoList);
		}

	}

	/**
	 * 开始电子账户开户行查询
	 */
	protected void beginQueryElecAccoOpeningBank(
			QueryElecAccOpeningBankParamsModel params) {
		showProgressDialog();
		remoteOpenAccService.queryElecAccoOeningBank(params,
				CODE_QUERY_ELEC_ACC_OPENING_BANK);
	}

	/**
	 * 电子账户开户行查询返回处理
	 */
	protected void endQueryElecAccoOpeningBank(Message result) {
		closeProgressDialog();
		QueryElecAccOpeningBankResponseModel resultModel = (QueryElecAccOpeningBankResponseModel) result.obj;
		remobeQueryElecacCattrView
				.showOpeningBankView(resultModel.bocBankInfoList);

	}

	/**
	 * 开始在线开户申请
	 */
	protected void beginApplyOnlineOpenAccount() {
		showProgressDialog();
		remoteOpenAccService.applyonlineopenaccount(applyOnlineparams,
				CODE_APPLY_ONLINE_OPEN_ACCOUNT);
	}

	/**
	 * 在线开户返回处理
	 */
	protected void endApplyOnlineOpenAccount(Message result) {
		closeProgressDialog();
		jumpToFragment(new RemobeApplayOnlineAccResultFragment());
	}

	/**
	 * 开始在线开户申请
	 */
	protected void beginQueryProvince(QueryProvinceAndCityParamsModel params) {
		showProgressDialog();
		remoteOpenAccService.queryProvinceAndCity(params, CODE_QUERY_PROVINCE);
	}

	/**
	 * 开始在线开户申请
	 */
	protected void beginQueryCity(QueryProvinceAndCityParamsModel params) {
		showProgressDialog();
		remoteOpenAccService.queryProvinceAndCity(params, CODE_QUERY_CITY);
	}

	/**
	 * 在线开户返回处理
	 */
	protected void endQueryProvinceAndCity(Message result, final String distType) {
		closeProgressDialog();
		QueryProvinceAndCityResponseModel resultModel = (QueryProvinceAndCityResponseModel) result.obj;

		// 设置省份
		if ("0".equals(distType)) {
			if (remobeQueryElecacCattrView == null) {
				remobeQueryElecacCattrView = new RemobeQueryElecacCattrView(
						mContext);

				remobeQueryElecacCattrView
						.showProvinceOrCityView(resultModel.distMappingList);

				remobeQueryElecacCattrView
						.setmQueryCityViewListener(new RemobeQueryElecacCattrView.QueryCityViewListener() {

							@Override
							public void onQueryCityItemClickListener(
									DistMapping dist) {
								if ("0".equals(dist.distType)) {
									QueryProvinceAndCityParamsModel params = new QueryProvinceAndCityParamsModel();
									params.distType = "1";
									params.distCode = dist.distCode;
									params.pageFlag = "1";
									beginQueryCity(params);
								} else {
									QueryElecAccOpeningBankParamsModel params = new QueryElecAccOpeningBankParamsModel();
									params.provinceCode = dist.parDist;
									params.cityCode = dist.distCode;
									beginQueryElecAccoOpeningBank(params);
								}

							}
						});

				remobeQueryElecacCattrView
						.setQueryElecacCattrViewListener(new QueryElecacCattrViewListener() {

							@Override
							public void onOpeningBankItemClickListener(
									BocBankInfo pos) {
								stepThreeView.setSelectedOpenBank(pos);
								applyOnlineparams.orgName = pos.orgName;// 开户网点
								onSelectViewSelected();
							}
						});
			} else {
				remobeQueryElecacCattrView.clearTitle();
				remobeQueryElecacCattrView
						.showProvinceOrCityView(resultModel.distMappingList);
			}
			alertSelectView(remobeQueryElecacCattrView,
					getString(R.string.bocroa_select_open_bank_title));
		} else {
			remobeQueryElecacCattrView
					.showProvinceOrCityView(resultModel.distMappingList);
			alertSelectView(remobeQueryElecacCattrView, "");
		}
	}

	public SettingPasswordCallback settingPasswordCallback = new SettingPasswordCallback() {

		/**
		 * 查询开户行
		 */
		@Override
		public void onQueryOpenAccBank() {
			// 查询所属地
			QueryProvinceAndCityParamsModel params = new QueryProvinceAndCityParamsModel();
			params.distType = "0";
			params.distCode = "";
			params.pageFlag = "1";
			beginQueryProvince(params);

		}

		/**
		 * 设置密码提交
		 */
		@Override
		public void onSettingPassword(String password, String password_RC,
				String affirmPass, String affirmPass_RC, BocBankInfo bankInfo) {
			if (applyOnlineparams != null) {
				applyOnlineparams.password = password;
				applyOnlineparams.password_RC = password_RC;
				applyOnlineparams.affirmPass = affirmPass;
				applyOnlineparams.affirmPass_RC = affirmPass_RC;
				// applyOnlineparams.purpose = accUse;
				// applyOnlineparams.reason = openAccReason;
				applyOnlineparams.openBocnet = "1";// 是否开通网银 X(1) 0不开通 1开通 Y
				// applyOnlineparams.bindBocnet = "0";// 是否绑定网银 X(1) 0不绑定 1绑定 Y
				applyOnlineparams.openEz = "1";// 是否开通易商 X(1) 0不开通 1开通 Y
				applyOnlineparams.bindEz = "1";// 是否绑定易商 X(1) 0不绑定 1绑定 Y
				applyOnlineparams.bindExEz = "1";// 同bindEz
				applyOnlineparams.openPhone = "0";// 是否开通电话银行 X(1) 0不开通 1开通 Y
				// 2016-05-26 20:11:20 yx delete 删除 改造
				// applyOnlineparams.bindPhone = "0";// 是否绑定电话银行 X(1) 0不绑定 1绑定 Y
				// if (!accUse.contains("1")) {// p601账户用途校验
				// RemobeOpenAccSubStepFragment.this
				// .showErrorDialog(getString(R.string.bocroa_select_acc_use_null_message));
				// return;
				// }
				// if (!openAccReason.contains("1")) {// p601开户原因校验
				// RemobeOpenAccSubStepFragment.this
				// .showErrorDialog(getString(R.string.bocroa_select_open_reason_null_message));
				// return;
				// }
				if (bankInfo == null) {
					RemobeOpenAccSubStepFragment.this
							.showErrorDialog(getString(R.string.bocroa_select_open_bank_null_message));
					return;
				} else {
					applyOnlineparams.orgCode = bankInfo.orgCode;
				}
				beginApplyOnlineOpenAccount();
			}

		}

		@Override
		public void onError(String errorCode, String errorMessage) {
			RemobeOpenAccSubStepFragment.this.showErrorDialog(errorMessage);
		}

		// @Override
		// public void onAccUse() {
		// showAccUseChoice("账户用途");
		// }

		@Override
		public void onOpenAccType() {
			showAccType("选择账户类型");
		}

	};

	// private boolean initAccUse = true;// 打开侧滑界面是否初始化数据
	// private boolean initOpenAccReason = true;// 打开侧滑界面是否初始化数据
	// private String accUse = "";// 账户用途
	// private String openAccReason = "";// 开户原因

	// /**
	// * 账户用途
	// */
	// protected void showAccUseChoice(String title) {
	// if (accUseView == null) {
	// accUseView = new BaseSideListView(mContext);
	// String[] dataList = null;
	// dataList = mContext.getResources().getStringArray(
	// R.array.bocroa_acc_use);
	// accUseDataLists = new ArrayList<String>();
	// for (String useItem : dataList) {
	// accUseDataLists.add(useItem);
	// }
	// accUseAdapter = new UseAndReasonAdapter();
	// accUseAdapter.setData(accUseDataLists,
	// initSelect(accUseCheckList, accUseDataLists.size()));
	// accUseView.setBtnClickListener(new BaseSideListViewBtnListener() {
	//
	// @Override
	// public void onClickListener(List<String> dataList,
	// List<Boolean> isCheckedList) {
	// String isCheckStr = "";// 0和1组成的字符串
	// String checkStr = "";// 文字字符串
	// accUseCheckList = isCheckedList;
	// for (int i = 0; i < isCheckedList.size(); i++) {
	// if (isCheckedList.get(i)) {
	// isCheckStr = isCheckStr + "1";
	// // 取出选中的数据，加上逗号
	// if (StringUtil.isNullOrEmpty(checkStr)) {
	// checkStr = dataList.get(i);
	// } else {
	// checkStr = checkStr + "," + dataList.get(i);
	// }
	// } else {
	// isCheckStr = isCheckStr + "0";
	// }
	// }
	// if (!isCheckStr.contains("1")) {
	// showErrorDialog("至少选择一个账户用途！");
	// return;
	// }
	// accUse = isCheckStr + "000";// 补齐10位
	// stepThreeView.setOpenAccType(checkStr);// 设置回显
	// onSelectViewSelected();
	// }
	// });
	// } else {
	// accUseAdapter.setData(accUseDataLists, accUseCheckList);//
	// }
	// accUseView.setListViewAdapter(accUseAdapter);
	// alertSelectView(accUseView, title);
	// }

	/**
	 * 将是否选中的集合初始化，第一条默认选中
	 * 
	 * @param list
	 * @param size
	 * @return
	 */
	private List<Boolean> initSelect(List<Boolean> list, int size) {
		list = new ArrayList<Boolean>();
		for (int i = 0; i < size; i++) {
			if (i == 0 || i == 1) {
				list.add(true);
			} else {
				list.add(false);
			}
		}
		return list;
	}

	private List<Boolean> openReasonCheckList;
	// private List<Boolean> accUseCheckList;
	String openTypeString = "";// 记录开通账户类型,01开立二类账户02开立三类账户03同时开立二、三类账户

	/**
	 * 账户类型
	 */
	protected void showAccType(String title) {
		if (accType == null) {
			accType = new BaseSideListView(mContext);
			String[] dataList = null;
			dataList = mContext.getResources().getStringArray(
					R.array.bocroa_acc_type);
			// dataList = mContext.getResources().getStringArray(
			// R.array.bocroa_open_acc_reason);
			accTypeDataLists = new ArrayList<String>();
			// 根据身份验证结果判断是否添加
			if (BocroaUtils.isEmpty(card2nd)) {
				accTypeDataLists.add(dataList[0]);
			}
			if (BocroaUtils.isEmpty(card3rd)) {
				accTypeDataLists.add(dataList[1]);
			}
			openAccReaAdapter = new UseAndReasonAdapter();
			openAccReaAdapter.setData(accTypeDataLists,
					initSelect(openReasonCheckList, accTypeDataLists.size()));
			accType.setBtnClickListener(new BaseSideListViewBtnListener() {

				@Override
				public void onClickListener(List<String> dataList,
						List<Boolean> isCheckedList) {
					if (isCheckedList == null || !isHaveTrue(isCheckedList)) {
						showErrorDialog("至少选择一个账户类型！");
						return;
					}
					String typeString = "";
					if (isCheckedList.size() == 1) {
						if (isCheckedList.get(0) == true) {
							if (dataList.get(0).startsWith("中银E财账户")) {
								openTypeString = "01";
								typeString = "中银E财账户";
							} else if (dataList.get(0).startsWith("中银E捷账户")) {
								openTypeString = "02";
								typeString = "中银E捷账户";
							}
						}
					} else if (isCheckedList.size() == 2) {
						if (isCheckedList.get(0) == true) {
							openTypeString = "01";
							typeString = "中银E财账户";
							if (isCheckedList.get(1) == true) {
								openTypeString = "03";
								typeString = typeString + "，" + "中银E捷账户";
							}
						} else {
							if (isCheckedList.get(1) == true) {
								openTypeString = "02";
								typeString = "中银E捷账户";
							}
						}
					}
					// }
					// String isCheckStr = "";// 0和1组成的字符串
					// String checkStr = "";// 文字字符串
					// openReasonCheckList = isCheckedList;
					// for (int i = 0; i < isCheckedList.size(); i++) {
					// if (isCheckedList.get(i)) {
					// // 取出选中的数据，加上逗号
					// if (StringUtil.isNullOrEmpty(checkStr)) {
					// checkStr = dataList.get(i);
					// } else {
					// checkStr = checkStr + ","
					// + dataList.get(i);
					// }
					// isCheckStr = isCheckStr + "1";
					// } else {
					// isCheckStr = isCheckStr + "0";
					// }
					// }
					// if (!isCheckStr.contains("1")) {
					// showErrorDialog("至少选择一个开卡类型！");
					// // showErrorDialog("至少选择一个开户原因！");
					// return;
					// }
					// openAccReason = isCheckStr + "00000";// 补齐10位
					stepThreeView.setOpenAccType(typeString);// 设置回显
					applyOnlineparams.openAccountType = openTypeString;
					onSelectViewSelected();
				}

				/**
				 * 是否有true
				 * 
				 * @param isCheckedList
				 * @return
				 */
				private boolean isHaveTrue(List<Boolean> isCheckedList) {
					boolean result = false;
					for (int i = 0; i < isCheckedList.size(); i++) {
						boolean item = isCheckedList.get(i);
						result = result || item;
					}
					return result;
				}
			});
		} else {
			openAccReaAdapter.setData(accTypeDataLists, openReasonCheckList);//
		}
		accType.setListViewAdapter(openAccReaAdapter);
		alertSelectView(accType, title);
	}

	private ApplyOnlineOpenAccountParamsModel applyOnlineparams;// 提交model
	// private Bitmap bmFront;
	// private Bitmap bmBack;
	private BaseSideListView accType;
	// private BaseSideListView accUseView;
	// private List<String> accUseDataLists;
	// private UseAndReasonAdapter accUseAdapter;
	private UseAndReasonAdapter openAccReaAdapter;
	private List<String> accTypeDataLists;
	private boolean isBack = false;

	@Override
	public boolean onGoBackFragment() {
		if (isViewShow()) {// 是否有弹出窗
			hidAlertSelectView();
			return false;
		} else {
			if (!isBack) {
				showTwoBtnDialog();
			}
			return isBack;
		}
	}

	MessageDialogTwoBtn msgDialog;

	private void showTwoBtnDialog() {
		if (msgDialog == null) {
			msgDialog = new MessageDialogTwoBtn(getActivity());
			msgDialog.setOnBtnClickListener(new MessageDialogTwoBtn(
					getActivity()).new OnBtnClick() {

				@Override
				public boolean OnOKClick() {
					isBack = true;
					onBackIconClick(rootView);
					isBack = false;
					return false;
				}
			});
		}
		msgDialog.setMsg("返回后此页面所有数据将被清空，确定要返回？");
		if (!msgDialog.isShowing()) {
			msgDialog.show();
		}
	}

	public class UseAndReasonAdapter extends BaseAdapter {
		List<String> list = new ArrayList<String>();
		List<Boolean> isCheckList = new ArrayList<Boolean>();

		@Override
		public int getCount() {
			return list.size();
		}

		public void setData(List<String> dataLists, List<Boolean> checkList) {
			if (dataLists != null && checkList != null) {
				list = dataLists;
				isCheckList.clear();
				for (int i = 0; i < checkList.size(); i++) {
					isCheckList.add(checkList.get(i));
				}
				notifyDataSetChanged();
			}
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = View.inflate(mContext,
						R.layout.bocroa_list_item_view_multiple_choice, null);
				viewHolder.textView = (TextView) convertView
						.findViewById(R.id.tv_list_item);
				viewHolder.checkBox = (CheckBox) convertView
						.findViewById(R.id.cb_check);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.checkBox.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (viewHolder.checkBox.isChecked()) {
						isCheckList.add(position, true);
					} else {
						isCheckList.add(position, false);
					}

				}
			});
			viewHolder.textView.setText(list.get(position));
			viewHolder.checkBox.setChecked(isCheckList.get(position));
			return convertView;
		}

		public class ViewHolder {
			private TextView textView;
			private CheckBox checkBox;
		}
	}
}
