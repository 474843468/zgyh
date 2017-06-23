package com.chinamworld.bocmbci.biz.blpt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Blpt;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.bii.constant.Setting;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

/**
 * 账单支付UI基类
 * 
 * @author panwe
 * 
 */
@SuppressWarnings("ResourceType")
public class BillPaymentBaseActivity extends BaseActivity {

	public static final String TAG = "BillPaymentBaseActivity";

	/** 返回主页按钮 */
	protected Button btnBack;
	/** 右上角按钮 */
	protected Button btnRight;
	/** 右侧按钮点击事件 */
	private OnClickListener rightBtnClick;
	/** 内容布局 */
	protected LinearLayout layoutContent;
	/*** 布局属性 */
	private LinearLayout.LayoutParams wiParams;
	private LinearLayout.LayoutParams textParams;
	/** TextView控件id **/
	private final int TEXTVIEW_ID = 1;
	/** editText id **/
	private final int EDITTEXT_ID = 2;
	/** spinner id **/
	private final int SPINNER_ID = 3;
	/** 输入框高度 **/
	private int editHeight;
	/** 下拉框高度 */
	private int spHeight;
	/** 右边距 **/
	private int rightPading;
	/** 头间距 */
	private int topMargin;
	/** 下拉框右边距 */
	// private int spRightMargin;
	/** 下拉框左边距 */
	private int spLeftMargin;
	/** 字体大小 **/
	private float textSize;
	/** 输入框键盘类型限制 */
//	private String[] ctType;
	/*** type 为右边显示控件类型 1--普通输入框 2--密码框 3--大文本输入对 4--下拉框 5--隐藏文本 6--用于显示的文本 **/
	private final String TYPE_EDIT = "1";
	private final String TYPE_PASEDIT = "2";
	private final String TYPE_BGEDIT = "3";
	private final String TYPE_SPINNER = "4";
	private final String TYPE_HIDDEN = "5";
	private final String TYPE_TEXT = "6";
	/** 校验类型 1--非空 2--手机号 3--身份证 */
	private final String CHECK_UNNULL = "1";
	private final String CHECK_PHONE = "2";
	private final String CHECK_IDTYPE = "3";
	private final String CHECKTYPE_PHONE = "shoujiH_11_15";
	private final String CHECKTYPE_IDTYPE = "safetyIdentityNumber";
	/** map key */
	private final String CHECK = "check";
	private final String CHECKTYPE = "checktype";
	private final String CONTENT = "content";
	private final String CHECKTIP = "tip";
	/*** 缴费卡号 */
	private String titleNameDispName;
	private String accNum;
	/** 当前选中的卡号 **/
	public int accCurPosition;
	public boolean checkOk = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);

		wiParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		textParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);

		editHeight = this.getResources().getDimensionPixelSize(
				R.dimen.layout_height);
		spHeight = this.getResources().getDimensionPixelSize(
				R.dimen.layout_height);
		rightPading = this.getResources().getDimensionPixelSize(
				R.dimen.layout_padding_left_right);
		topMargin = this.getResources().getDimensionPixelSize(
				R.dimen.dialogbtn_marginTop);
		// spRightMargin = this.getResources().getDimensionPixelSize(
		// R.dimen.common_row_margin_half);
		spLeftMargin = this.getResources().getDimensionPixelSize(
				R.dimen.layout_margin_top);
		textSize = Float.parseFloat(this
				.getString(R.string.textsize_default_blpt));

		wiParams.setMargins(0, topMargin, 0, 0);
		textParams.weight = 4.8f;
		textParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;

//		ctType = new String[] { this.getString(R.string.blpt_phone),
//				this.getString(R.string.blpt_postcode),
//				this.getString(R.string.blpt_money) };

		// 隐藏左侧展示按钮
		invisible();
		// 初始化弹窗按钮
		initPulldownBtn();
		// 初始化底部菜单栏
		initFootMenu();
		// 初始化控件
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		btnBack = (Button) findViewById(R.id.ib_back);
		btnBack.setOnClickListener(backClick);
		btnRight = (Button) findViewById(R.id.ib_top_right_btn);
		layoutContent = (LinearLayout) this.findViewById(R.id.sliding_body);
	}

	/** 返回Listener */
	private OnClickListener backClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	};

	/** 返回主页面 */
	public OnClickListener rightBtnBackmainClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ActivityTaskManager.getInstance().removeAllActivity();
			BlptUtil.getInstance().clearAllData();
		}
	};

	/** 通讯失败 **/
	public OnClickListener errorClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			BaseDroidApp.getInstanse().dismissErrorDialog();
			finish();
		}
	};

	/**
	 * 在slidingbody中引入自己布局文件
	 * 
	 * @param resource
	 * @return 引入布局
	 */
	protected void addView(View v) {
		layoutContent.addView(v);
	}

	/**
	 * 设置右侧按钮文字
	 * 
	 * @param title
	 */
	public void setText(String title) {
		btnRight.setVisibility(View.VISIBLE);
		btnRight.setText(title);
		btnRight.setTextColor(Color.WHITE);
		btnRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (rightBtnClick != null) {
					rightBtnClick.onClick(v);
				}
			}
		});
	}

	/**
	 * 右上角按钮clikListener
	 * 
	 * @param rightBtnClick
	 */
	public void setRightBtnClick(OnClickListener rightBtnClick) {
		this.rightBtnClick = rightBtnClick;
	}

	protected void invisible() {
		Button btnhide = (Button) findViewById(R.id.btn_show);
		btnhide.setVisibility(View.GONE);
	}

	/** 隐藏右上角按钮 */
	public void gonRightBtn() {
		btnRight.setVisibility(View.GONE);
	}

	/**
	 * 封装上传数据
	 * 
	 * @param exelist
	 * @param map
	 */
	public void putData(List<Map<String, Object>> exelist,
			List<Map<String, Object>> spclist, Map<String, String> layoutData,
			Map<String, Object> map) {
		int index = 0;
		if (exelist != null && exelist.size() != 0) {
			for (int i = 0; i < exelist.size(); i++) {
				Map<String, Object> data = exelist.get(i);
				// 存在左边
				if (data.containsKey(Blpt.SIGN_PAY_EXELEFT_RE)) {
					@SuppressWarnings("unchecked")
					Map<String, Object> leftMap = (Map<String, Object>) data
							.get(Blpt.SIGN_PAY_EXELEFT_RE);
					String type = (String) leftMap.get(Blpt.SIGN_PAY_TYPE);
					// type：5 隐藏字段
					if (!StringUtil.isNullOrEmpty(type)
							&& type.equals(TYPE_HIDDEN)) {
						if (!StringUtil.isNullOrEmpty((String) leftMap
								.get(Blpt.SIGN_PAY_VALUE))) {
							map.put((String) leftMap.get(Blpt.EXE_DATA),
									(String) leftMap.get(Blpt.SIGN_PAY_VALUE));
						} else {
							map.put((String) leftMap.get(Blpt.EXE_DATA),
									(String) leftMap
											.get(Blpt.SIGN_PAY_INITVALUE));
						}
						// type：2 加密控件
//					} else if (!StringUtil.isNullOrEmpty(type)
//							&& type.equals(TYPE_PASEDIT)) {
//						map.put((String) leftMap.get(Blpt.EXE_DATA),
//								layoutData.get("data" + index));
//						map.put((String) leftMap.get(Blpt.EXE_DATA) + "_RC",
//								layoutData.get("datapas" + index));
//						SipBoxUtils.setSipBoxParams(map);
//						index++;
						// type:4下拉框
					} else if (!StringUtil.isNullOrEmpty(type)
							&& type.equals(TYPE_SPINNER)) {
						String initOptionValue = getInitOptionValue(
								(String) leftMap.get(Blpt.SPINNER_INITVALUE),
								layoutData.get("data" + index));
						map.put((String) leftMap.get(Blpt.EXE_DATA),
								initOptionValue);
						index++;
						// 其他显示字段
					} else {
						//下拉反显-上传
						if (!StringUtil.isNull((String)leftMap.get(Blpt.SPINNER_INITVALUE))) {
							map.put((String) leftMap.get(Blpt.EXE_DATA), (String) leftMap.get(Blpt.NEWAPPLY_CONFIEM_VALUE_RP));
						}else{
							if (StringUtil.isNullOrEmpty(layoutData.get("data"
									+ index))) {
								map.put((String) leftMap.get(Blpt.EXE_DATA), "");
							} else {
								map.put((String) leftMap.get(Blpt.EXE_DATA),
										layoutData.get("data" + index));
							}
						}
						index++;
					}
				}
				// 存在右边
				if (data.containsKey(Blpt.SIGN_PAY_EXERIGHT_RE)) {
					@SuppressWarnings("unchecked")
					Map<String, Object> rightMap = (Map<String, Object>) data
							.get(Blpt.SIGN_PAY_EXERIGHT_RE);
					String type = (String) rightMap.get(Blpt.SIGN_PAY_TYPE);
					// type：5 隐藏字段
					if (!StringUtil.isNullOrEmpty(type)
							&& type.equals(TYPE_HIDDEN)) {
						if (!StringUtil.isNullOrEmpty((String) rightMap
								.get(Blpt.SIGN_PAY_VALUE))) {
							map.put((String) rightMap.get(Blpt.EXE_DATA),
									(String) rightMap.get(Blpt.SIGN_PAY_VALUE));
						} else {
							map.put((String) rightMap.get(Blpt.EXE_DATA),
									(String) rightMap
											.get(Blpt.SIGN_PAY_INITVALUE));
						}
					} else {
						if (!StringUtil.isNullOrEmpty(layoutData)) {
							// type：2 加密控件
							/*if (!StringUtil.isNullOrEmpty(type)
									&& type.equals(TYPE_PASEDIT)) {
								map.put((String) rightMap.get(Blpt.EXE_DATA),
										layoutData.get("data" + index));
								map.put((String) rightMap.get(Blpt.EXE_DATA) + "_RC",
										layoutData.get("datapas" + index));
								SipBoxUtils.setSipBoxParams(map);
								index++;
								// type:4下拉框
							} else*/ if (!StringUtil.isNullOrEmpty(type)
									&& type.equals(TYPE_SPINNER)) {
								String initOptionValue = getInitOptionValue(
										(String) rightMap.get(Blpt.SPINNER_INITVALUE),
										layoutData.get("data" + index));
								map.put((String) rightMap.get(Blpt.EXE_DATA),
										initOptionValue);
								index++;
								// 其他显示字段
							} else {
								if (!StringUtil.isNull((String)rightMap.get(Blpt.SPINNER_INITVALUE))) {
									map.put((String) rightMap.get(Blpt.EXE_DATA), (String) rightMap.get(Blpt.NEWAPPLY_CONFIEM_VALUE_RP));
								}else{
									if (StringUtil.isNullOrEmpty(layoutData.get("data"
											+ index))) {
										map.put((String) rightMap.get(Blpt.EXE_DATA), "");
									} else {
										map.put((String) rightMap.get(Blpt.EXE_DATA),
												layoutData.get("data" + index));
									}
								}
								index++;
							}
						}
					}
				}
				// 无left、right
				if (!data.containsKey(Blpt.SIGN_PAY_EXELEFT_RE)
						&& !data.containsKey(Blpt.SIGN_PAY_EXERIGHT_RE)) {
					String type = (String) data.get(Blpt.SIGN_PAY_TYPE);
					// type：5 隐藏字段
					if (!StringUtil.isNullOrEmpty(type)
							&& type.equals(TYPE_HIDDEN)) {
						if (!StringUtil.isNullOrEmpty((String) data
								.get(Blpt.SIGN_PAY_VALUE))) {
							map.put((String) data.get(Blpt.EXE_DATA),
									(String) data.get(Blpt.SIGN_PAY_VALUE));
						} else {
							map.put((String) data.get(Blpt.EXE_DATA),
									(String) data.get(Blpt.SIGN_PAY_INITVALUE));
						}
					} else{
						if (!StringUtil.isNullOrEmpty(layoutData)) {
							// type：2 加密控件
							/*if (!StringUtil.isNullOrEmpty(type)
									&& type.equals(TYPE_PASEDIT)) {
								map.put((String) data.get(Blpt.EXE_DATA),
										layoutData.get("data" + index));
								map.put((String) data.get(Blpt.EXE_DATA) + "_RC",
										layoutData.get("datapas" + index));
								SipBoxUtils.setSipBoxParams(map);
								index++;
								// type:4下拉框
							} else*/ if (!StringUtil.isNullOrEmpty(type)
									&& type.equals(TYPE_SPINNER)) {
								String initOptionValue = getInitOptionValue(
										(String) data.get(Blpt.SPINNER_INITVALUE),
										layoutData.get("data" + index));
								map.put((String) data.get(Blpt.EXE_DATA),
										initOptionValue);
								index++;
								// 其他显示字段
							} else {
								if (!StringUtil.isNull((String)data.get(Blpt.SPINNER_INITVALUE))) {
									map.put((String) data.get(Blpt.EXE_DATA), (String) data.get(Blpt.NEWAPPLY_CONFIEM_VALUE_RP));
								}else{
									if (StringUtil.isNullOrEmpty(layoutData.get("data"
											+ index))) {
										map.put((String) data.get(Blpt.EXE_DATA), "");
									} else {
										map.put((String) data.get(Blpt.EXE_DATA),
												layoutData.get("data" + index));
									}
								}
								index++;
							}
						}
					}
				}
			}
		}

		if (spclist != null && spclist.size() != 0) {
			for (int i = 0; i < spclist.size(); i++) {
				Map<String, Object> data = spclist.get(i);
				String type = (String) data.get(Blpt.SIGN_PAY_TYPE);
				// type：5 隐藏字段
				if (!StringUtil.isNullOrEmpty(type) && type.equals(TYPE_HIDDEN)) {
					if (!StringUtil.isNullOrEmpty((String) data
							.get(Blpt.SIGN_PAY_VALUE))) {
						map.put((String) data.get(Blpt.EXE_DATA),
								(String) data.get(Blpt.SIGN_PAY_VALUE));
					} else {
						map.put((String) data.get(Blpt.EXE_DATA),
								(String) data.get(Blpt.SIGN_PAY_INITVALUE));
					}
				} else{
					if (!StringUtil.isNullOrEmpty(layoutData)) {
						// type：2 加密控件
						/*if (!StringUtil.isNullOrEmpty(type)
								&& type.equals(TYPE_PASEDIT)) {
							map.put((String) data.get(Blpt.EXE_DATA),
									layoutData.get("data" + index));
							map.put((String) data.get(Blpt.EXE_DATA) + "_RC",
									layoutData.get("datapas" + index));
							SipBoxUtils.setSipBoxParams(map);
							index++;
							// type:4下拉框
						} else*/ if (!StringUtil.isNullOrEmpty(type)
								&& type.equals(TYPE_SPINNER)) {
							String initOptionValue = getInitOptionValue(
									(String) data.get(Blpt.SPINNER_INITVALUE),
									layoutData.get("data" + index));
							map.put((String) data.get(Blpt.EXE_DATA),
									initOptionValue);
							index++;
							// 其他显示字段
						} else {
							if (!StringUtil.isNull((String)data.get(Blpt.SPINNER_INITVALUE))) {
								map.put((String) data.get(Blpt.EXE_DATA), (String) data.get(Blpt.NEWAPPLY_CONFIEM_VALUE_RP));
							}else{
								if (StringUtil
										.isNullOrEmpty(layoutData.get("data" + index))) {
									map.put((String) data.get(Blpt.EXE_DATA), "");
								} else {
									map.put((String) data.get(Blpt.EXE_DATA),
											layoutData.get("data" + index));
								}
							}
							index++;
						}
					}
				}
			}
		}
	}

	/**
	 * 下拉框取值
	 * 
	 * @param initValue
	 *            返回字段数据
	 * @param index
	 *            下拉选标志位
	 * @return 上传数据
	 */
	private String getInitOptionValue(String initValue, String index) {
		if (StringUtil.isNull(initValue)) {
			return "";
		}
		String[] strArray = initValue.split("#");
		int postion = Integer.valueOf(index);
		if (postion < strArray.length) {
			return strArray[postion];
		}
		return "";
	}

	/**
	 * 根据返回数据动态添加布局
	 * 
	 * @param acList
	 * @param exeList
	 * @param spcList
	 * @return
	 */
	@SuppressWarnings("ResourceType")
	public LinearLayout addViews(List<Map<String, Object>> acList,
								 List<Map<String, Object>> exeList,
								 List<Map<String, Object>> spcList, boolean isSpinner) {
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		LinearLayout childLayout = new LinearLayout(this);
		childLayout.setLayoutParams(layoutParams);
		childLayout.setOrientation(LinearLayout.VERTICAL);

		/***
		 * type 为右边显示控件类型 1--普通输入框 2--密码框 3--大文本输入对 4--下拉框 5--隐藏文本 6--用于显示的文本
		 **/
		// 帐号列表
		if (acList != null && acList.size() != 0) {
			// 为一条数据时使用文本显示
			if (acList.size() > 1) {
				isSpinner = true;
			} else {
				isSpinner = false;
			}
			if (isSpinner) {
				if (acList != null && acList.size() != 0) {
					LinearLayout layout = createSpinner(
							this.getString(R.string.blpt_spiner_info), "");
					childLayout.addView(layout);
					Spinner spContent = (Spinner) layout.getChildAt(1);

					ArrayList<String> spList = new ArrayList<String>();
					for (int i = 0; i < acList.size(); i++) {
						spList.add(StringUtil
								.getForSixForString((String) acList.get(i).get(
										Blpt.ACC_NUMBER)));
					}
					// spinner适配器
					com.chinamworld.bocmbci.biz.crcd.adapter.SpinnerAdapter spinnerAdapter = new com.chinamworld.bocmbci.biz.crcd.adapter.SpinnerAdapter(
							this, R.layout.custom_spinner_item, spList);
					spinnerAdapter
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spContent.setAdapter(spinnerAdapter);

				}
			} else {
				String strtitle;
				if (StringUtil.isNullOrEmpty((String) acList.get(0).get(
						Blpt.SIGN_PAY_TITLENAME))) {
					strtitle = this.getString(R.string.blpt_had_new_list_acct);
				} else {
					strtitle = (String) acList.get(0).get(
							Blpt.SIGN_PAY_TITLENAME);
				}
				LinearLayout layout = createTextView(strtitle, "");
				childLayout.addView(layout);

				TextView tvContent;
				if (layout.getChildAt(1).getId() == 1005) {
					LinearLayout playout = (LinearLayout) layout.getChildAt(1);
					tvContent = (TextView) playout.getChildAt(0);
				} else {
					tvContent = (TextView) layout.getChildAt(1);
				}
				if (acList.get(0).containsKey(Blpt.ACC_NUMBER)) {
					tvContent.setText(StringUtil
							.getForSixForString((String) acList.get(0).get(
									Blpt.ACC_NUMBER)));
				} else if (!StringUtil.isNullOrEmpty((String) acList.get(0)
						.get(Blpt.SIGN_PAY_VALUE))) {
					tvContent.setText(StringUtil
							.getForSixForString((String) acList.get(0).get(
									Blpt.SIGN_PAY_VALUE)));
				} else {
					tvContent.setText(StringUtil
							.getForSixForString((String) acList.get(0).get(
									Blpt.SIGN_PAY_INITVALUE)));
				}
				PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvContent);
			}
		}

		if (exeList != null && exeList.size() != 0) {
			for (int j = 0; j < exeList.size(); j++) {

				if (exeList.get(j).containsKey(Blpt.SIGN_PAY_EXELEFT_RE)) {
					// left
					@SuppressWarnings("unchecked")
					Map<String, Object> leftMap = (Map<String, Object>) exeList
							.get(j).get(Blpt.SIGN_PAY_EXELEFT_RE);
					createView(childLayout, leftMap);
				}
				if (exeList.get(j).containsKey(Blpt.SIGN_PAY_EXERIGHT_RE)) {
					// right
					@SuppressWarnings("unchecked")
					Map<String, Object> rightMap = (Map<String, Object>) exeList
							.get(j).get(Blpt.SIGN_PAY_EXERIGHT_RE);
					createView(childLayout, rightMap);
				}
				if (!exeList.get(j).containsKey(Blpt.SIGN_PAY_EXELEFT_RE)
						&& !exeList.get(j).containsKey(
								Blpt.SIGN_PAY_EXERIGHT_RE)) {
					Map<String, Object> map = exeList.get(j);
					createView(childLayout, map);
				}
			}
		}

		if (spcList != null && spcList.size() != 0) {
			for (int i = 0; i < spcList.size(); i++) {
				Map<String, Object> map = spcList.get(i);
				createView(childLayout, map);
			}
		}
		return childLayout;
	}

	/**
	 * 根据map数据创建布局控件
	 * 
	 * @param childLayout
	 * @param map
	 */
	private void createView(LinearLayout childLayout, Map<String, Object> map) {
		String type = (String) map.get(Blpt.SIGN_PAY_TYPE);
		if (StringUtil.isNullOrEmpty(type)) {
			return;
		}
		String warn = (String) map.get(Blpt.SIGN_PAY_POSTNAME);
		// 输入框
		if (type.equals(TYPE_EDIT) || type.equals(TYPE_BGEDIT) || type.equals(TYPE_PASEDIT)) {

			if (!StringUtil.isNullOrEmpty(warn) && warn.length() > 4) {
				LinearLayout layout = createEditText(
						(String) map.get(Blpt.SIGN_PAY_EXE_CHECK),
						((String) map.get(Blpt.EXE_TITLE_NAME)).trim(),
						(String) map.get(Blpt.MAXLENG), "", type);
				childLayout.addView(layout);

				LinearLayout tipLayout = createTipTextView(warn);
				childLayout.addView(tipLayout);
			} else {
				LinearLayout layout = createEditText(
						(String) map.get(Blpt.SIGN_PAY_EXE_CHECK),
						((String) map.get(Blpt.EXE_TITLE_NAME)).trim(),
						(String) map.get(Blpt.MAXLENG), warn, type);
				childLayout.addView(layout);

			}
			// 密码框
//		} else if (!StringUtil.isNullOrEmpty(type) && type.equals(TYPE_PASEDIT)) {
//			if (!StringUtil.isNullOrEmpty(warn) && warn.length() > 4) {
//
//				LinearLayout layout = createPasEditText(
//						(String) map.get(Blpt.EXE_TITLE_NAME),
//						(String) map.get(Blpt.MAXLENG), "");
//				childLayout.addView(layout);
//
//				LinearLayout tipLayout = createTipTextView(warn);
//				childLayout.addView(tipLayout);
//			} else {
//				LinearLayout layout = createPasEditText(
//						(String) map.get(Blpt.EXE_TITLE_NAME),
//						(String) map.get(Blpt.MAXLENG), warn);
//				childLayout.addView(layout);
//			}

			// 下拉框
		} else if (!StringUtil.isNullOrEmpty(type) && type.equals(TYPE_SPINNER)) {
			LinearLayout splayout;
			if (!StringUtil.isNullOrEmpty(warn) && warn.length() > 4) {
				splayout = createSpinner(
						((String) map.get(Blpt.EXE_TITLE_NAME)).trim(), "");
				childLayout.addView(splayout);

				LinearLayout tipLayout = createTipTextView(warn);
				childLayout.addView(tipLayout);
			} else {
				splayout = createSpinner(
						((String) map.get(Blpt.EXE_TITLE_NAME)).trim(), warn);
				childLayout.addView(splayout);
			}
			Spinner spContent = (Spinner) splayout.getChildAt(1);

//			if (!StringUtil
//					.isNullOrEmpty((String) map.get(Blpt.SIGN_PAY_VALUE))) {
//				str = (String) map.get(Blpt.SIGN_PAY_VALUE);
//			} else {
//				str = (String) map.get(Blpt.SIGN_PAY_INITVALUE);
//			}
			// spinner适配器
			@SuppressWarnings({ "unchecked", "rawtypes" })
			ArrayAdapter<ArrayList<String>> spinnerAdapter = new ArrayAdapter(
					this, R.layout.blpt_spinner_item, initSpinnerData(map));
			spinnerAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spContent.setAdapter(spinnerAdapter);
			// 文本显示框
		} else if (!StringUtil.isNullOrEmpty(type) && type.equals(TYPE_TEXT)) {
			LinearLayout tvlayout;
			if (!StringUtil.isNullOrEmpty(warn) && warn.length() > 4) {
				tvlayout = createTextView(
						(String) map.get(Blpt.SIGN_PAY_TITLENAME), "");
				childLayout.addView(tvlayout);

				LinearLayout tipLayout = createTipTextView(warn);
				childLayout.addView(tipLayout);
			} else {
				tvlayout = createTextView(
						(String) map.get(Blpt.SIGN_PAY_TITLENAME), warn);
				childLayout.addView(tvlayout);
			}
			TextView tvContent;
			if (tvlayout.getChildAt(1).getId() == 1005) {
				LinearLayout playout = (LinearLayout) tvlayout.getChildAt(1);
				tvContent = (TextView) playout.getChildAt(0);
			} else {
				tvContent = (TextView) tvlayout.getChildAt(1);
			}
			//下拉选反显
			if (!StringUtil.isNull((String) map.get(Blpt.SPINNER_INITVALUE)) &&
					!StringUtil.isNull((String) map.get(Blpt.SIGN_PAY_VALUE))) {
				tvContent.setText(getInitOptionNameByValue(map));
			}else{
				if (!StringUtil
						.isNullOrEmpty((String) map.get(Blpt.SIGN_PAY_VALUE))) {
					String dataName = (String) map.get(Blpt.EXE_DATA);
					if (dataName.equals(Blpt.ACC_NUMBER) || dataName.equals(Blpt.NEWAPPLY_CONFIRM_ACCNUMBER_DISP)
							||dataName.equals(Blpt.NEWAPPLY_CONFIRM_ACCNUMBER_REAL)) {
						tvContent.setText(StringUtil
								.getForSixForString((String) map
										.get(Blpt.SIGN_PAY_VALUE)));
						accNum = (String) map.get(Blpt.SIGN_PAY_VALUE);
						titleNameDispName = (String) map.get(Blpt.SIGN_PAY_TITLENAME);
					} else {
						if ((((String) map.get(Blpt.SIGN_PAY_VALUE)).trim())
								.equals("")) {
							// tvContent.setText("-");
							tvContent.setText("");
						} else {
							tvContent.setText(((String) map
									.get(Blpt.SIGN_PAY_VALUE)).trim());
						}
					}
				} else {
					String dataName = (String) map.get(Blpt.EXE_DATA);
					if (dataName.equals(Blpt.ACC_NUMBER) || dataName.equals(Blpt.NEWAPPLY_CONFIRM_ACCNUMBER_DISP)
							||dataName.equals(Blpt.NEWAPPLY_CONFIRM_ACCNUMBER_REAL)) {
						tvContent.setText(StringUtil
								.getForSixForString((String) map
										.get(Blpt.SIGN_PAY_INITVALUE)));
						accNum = (String) map.get(Blpt.SIGN_PAY_INITVALUE);
						titleNameDispName = (String) map.get(Blpt.SIGN_PAY_TITLENAME);
					} else {
						if (StringUtil.isNull((String) map
								.get(Blpt.SIGN_PAY_INITVALUE))) {
							// tvContent.setText("-");
							tvContent.setText("");
						} else {
							tvContent.setText(((String) map
									.get(Blpt.SIGN_PAY_INITVALUE)).trim());
						}
					}
				}
			}
			PopupWindowUtils.getInstance().setOnShowAllTextListener(this, tvContent);
		}
	}
	
	/**
	 * 初始化下拉选数据
	 * @param map
	 * @return
	 */
	private String[] initSpinnerData(Map<String, Object> map){
		String optionValue = (String) map.get(Blpt.SPINNER_INITVALUE);
		String dispName = (String) map.get(Blpt.SIGN_PAY_INITVALUE);
		String[] optionValues = optionValue.split("#");
		String[] dispNames = dispName.split("#");
		if (optionValues.length < dispNames.length) {
			String[] result = new String[optionValues.length];
			for (int i = 0; i < optionValues.length; i++) {
				result[i] = dispNames[i];
			}
			return result;
		}
		return dispNames;
	}
	
	/**
	 * 下拉选反显
	 * @param map
	 * @return
	 */
	private String getInitOptionNameByValue(Map<String, Object> map){
		int index = 0;
		String strValue = (String)map.get(Blpt.NEWAPPLY_CONFIEM_VALUE_RP);
		String[] arrOptionValue = ((String)map.get(Blpt.SPINNER_INITVALUE)).split("#");
		String[] arrValueDispName = ((String)map.get(Blpt.SIGN_PAY_INITVALUE)).split("#");
		for (int i = 0; i < arrOptionValue.length; i++) {
			if (!StringUtil.isNull(strValue) && arrOptionValue[i].equals(strValue)) {
				index = i; break;
			}
		}
		if (index < arrValueDispName.length) {
			return arrValueDispName[index];
		}
		return "";
	}
	
	/**
	 * 读取选中卡信息
	 * @param map
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getBankAcctInfo(Map<String, Object> map,int key) {
		List<String> mList = null;
		if (mList == null) {
			mList = new ArrayList<String>();
		}
		mList.clear();
		Map<String, Object> curData = (Map<String, Object>) map.get("key" + key);
		List<Map<String, Object>> acctList = null;
		if (curData.containsKey(Blpt.NEWAPPLY_PRE_ACCLIST)) {
			acctList = (List<Map<String, Object>>) curData.get(Blpt.NEWAPPLY_PRE_ACCLIST);
		}
		if (curData.containsKey(Blpt.SIGN_PAY_ACC_RE)) {
			acctList = (List<Map<String, Object>>) curData.get(Blpt.SIGN_PAY_ACC_RE);
		}
		if (StringUtil.isNullOrEmpty(acctList)) {
			return null;
		}
		String accNum = (String) acctList.get(accCurPosition).get(Blpt.ACC_NUMBER);
		String accId = (String) acctList.get(accCurPosition).get(
				Blpt.SIGN_PA_ACC_ID_RE);
		mList.add(accNum);
		mList.add(accId);
		return mList;
	}

	/**
	 * 从动态页面中取出数据
	 * 
	 * @param mainLayout
	 * @return
	 */
	public Map<String, String> getTextFromLayout(LinearLayout curLayout,
			int index) {
		int dataIndex = index;
		int layoutCount = curLayout.getChildCount();
		Map<String, String> dataMap = new HashMap<String, String>();
		List<Map<String, String>> checkList = new ArrayList<Map<String, String>>();

		if (index == 1) {
			LinearLayout accLayout = (LinearLayout) curLayout.getChildAt(0);
			if (accLayout.getChildAt(1).getId() == SPINNER_ID) {
				Spinner acsp = (Spinner) accLayout.getChildAt(1);
				accCurPosition = acsp.getSelectedItemPosition();
			} else {
				accCurPosition = 0;
			}
		}
		for (int j = index; j < layoutCount; j++) {
			LinearLayout childlayout = (LinearLayout) curLayout.getChildAt(j);
			if (childlayout.getId() == 1001) {
			} else {
				int id = childlayout.getChildAt(1).getId();
				String content = "";
				String sipRan = "";
//				boolean ispas = false;
				// 文本控件
				if (id == TEXTVIEW_ID) {
//					ispas = false;
					TextView textview = (TextView) childlayout.getChildAt(1);
					if ((((TextView) childlayout.getChildAt(0)).getText()
							.toString()).equals(titleNameDispName+"：")) {
						content = accNum;
					} else {
						content = textview.getText().toString();
					}
					// 输入框控件
				} else if (id == EDITTEXT_ID) {
//					ispas = false;
					EditText edit = (EditText) childlayout.getChildAt(1);
					content = edit.getText().toString().trim();
					String check = (String) edit.getTag();
					/*** 动态步骤校验 1--非空 2--手机号 3--身份证 **/
					Map<String, String> map = new HashMap<String, String>();
					String tv = ((TextView) childlayout.getChildAt(0))
							.getText().toString();
					String strTip = null;
					if (tv.contains("*")) {
						strTip = tv.substring(1, tv.length() - 1);
					} else {
						strTip = tv.substring(0, tv.length() - 1);
					}
					if (!StringUtil.isNull(check)) {
						if (check.contains(CHECK_UNNULL)) {
							if (check.contains(CHECK_PHONE)) {
								map.put(CHECK, CHECK_PHONE);
								map.put(CHECKTYPE, CHECKTYPE_PHONE);
								map.put(CONTENT, content);
								map.put(CHECKTIP, strTip);
								checkList.add(map);
							} else if (check.contains(CHECK_IDTYPE)) {
								map.put(CHECK, CHECK_IDTYPE);
								map.put(CHECKTYPE, CHECKTYPE_IDTYPE);
								map.put(CONTENT, content);
								map.put(CHECKTIP, strTip);
								checkList.add(map);
							} else {
								map.put(CHECK, CHECK_UNNULL);
								map.put(CONTENT, content);
								map.put(CHECKTIP, strTip);
								checkList.add(map);
							}
						} else {
							if (!StringUtil.isNull(content)) {
								if (check.contains(CHECK_PHONE)) {
									map.put(CHECK, CHECK_PHONE);
									map.put(CHECKTYPE, CHECKTYPE_PHONE);
									map.put(CONTENT, content);
									map.put(CHECKTIP, strTip);
									checkList.add(map);
								} else if (check.contains(CHECK_IDTYPE)) {
									map.put(CHECK, CHECK_IDTYPE);
									map.put(CHECKTYPE, CHECKTYPE_IDTYPE);
									map.put(CONTENT, content);
									map.put(CHECKTIP, strTip);
									checkList.add(map);
								} else {
									map.put(CHECK, CHECK_UNNULL);
									map.put(CONTENT, content);
									map.put(CHECKTIP, strTip);
									checkList.add(map);
								}
							}
						}
					}
					// 下拉框控件
				} else if (id == SPINNER_ID) {
//					ispas = false;
					Spinner sp = (Spinner) childlayout.getChildAt(1);
					content = String.valueOf(sp.getSelectedItemPosition());
					// 密码加密控件
//				} else if (id == 10002) {
//					ispas = true;
//					String tv = ((TextView) childlayout.getChildAt(0))
//							.getText().toString();
//					SipBox sipBox = (SipBox) childlayout.getChildAt(1);
//					try {
//						content = sipBox.getValue().getEncryptPassword();
//						sipRan = sipBox.getValue().getEncryptRandomNum();
//					} catch (CodeException e) {
//						LogGloble.exceptionPrint(e);
//					}
//					Map<String, String> map = new HashMap<String, String>();
//					map.put(CHECK, "1001");
//					// map.put("checktype", ConstantGloble.SIPBOXPSW);
//					map.put(CONTENT, sipBox.getText().toString());
//					map.put(CHECKTIP, tv.substring(0, tv.length() - 1));
//					checkList.add(map);
				} else if (id == 1005) {
//					ispas = false;
					LinearLayout tvlayout = (LinearLayout) childlayout
							.getChildAt(1);
					TextView textview = (TextView) tvlayout.getChildAt(0);
					if ((((TextView) childlayout.getChildAt(0)).getText()
							.toString()).equals(titleNameDispName+"：")) {
						content = accNum;
					} else {
						content = textview.getText().toString();
					}
				}
				if (index == 1) {
//					if (ispas) {
//						dataMap.put("datapas" + (dataIndex - 1), sipRan);
//					}
					dataMap.put("data" + (dataIndex - 1), content);
				} else {
//					if (ispas) {
//						dataMap.put("datapas" + dataIndex, sipRan);
//					}
					dataMap.put("data" + dataIndex, content);
				}
				dataIndex++;
			}
		}
		// 校验
		dataCheck(checkList);
		return dataMap;
	}

	/***
	 * 输入框内容校验
	 * 
	 * @param data
	 * @return
	 */
	private void dataCheck(List<Map<String, String>> data) {
		checkOk = false;
		try {
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			if (data != null && data.size() != 0) {
				for (int i = 0; i < data.size(); i++) {
					// 非空校验
					if (!StringUtil.isNullOrEmpty(data.get(i).get(CHECK))
							&& data.get(i).get(CHECK).equals(CHECK_UNNULL)
							&& StringUtil.isNullOrEmpty(data.get(i)
									.get(CONTENT))) {
						RegexpBean reb = new RegexpBean(data.get(i).get(
								CHECKTIP), data.get(i).get(CONTENT), "", false);
						lists.add(reb);
						// 数字、证件校验
					} else if ((!StringUtil.isNullOrEmpty(data.get(i)
							.get(CHECK)) && data.get(i).get(CHECK)
							.equals(CHECK_PHONE))
							|| !StringUtil
									.isNullOrEmpty(data.get(i).get(CHECK))
							&& data.get(i).get(CHECK).equals(CHECK_IDTYPE)) {
						RegexpBean rebisR = new RegexpBean(data.get(i).get(
								CHECKTIP), data.get(i).get(CONTENT), data
								.get(i).get(CHECKTYPE));
						lists.add(rebisR);
						// 加密控件校验
//					} else if (!StringUtil
//							.isNullOrEmpty(data.get(i).get(CHECK))
//							&& data.get(i).get(CHECK).equals("1001")
//							&& StringUtil.isNullOrEmpty(data.get(i)
//									.get(CONTENT))) {
//						RegexpBean reb = new RegexpBean(data.get(i).get(
//								CHECKTIP), data.get(i).get(CONTENT), "", false);
//						lists.add(reb);
					}
				}
			}
			if (RegexpUtils.regexpDate(lists)) {// 校验通过
				checkOk = true;
			}
		} catch (Exception e) {
			BaseDroidApp.getInstanse().createDialog(null, "");
			LogGloble.exceptionPrint(e);
			checkOk = false;
		}
	}

	/**
	 * 创建文本布局
	 * 
	 * @param titleType
	 * @param warn
	 * 
	 * @return
	 */
	@SuppressWarnings("ResourceType")
	public LinearLayout createTextView(String titleType, String warn) {

		LinearLayout.LayoutParams textTitleParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams textContentParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		textTitleParams.weight = 3.2f;

		LinearLayout childLayout = new LinearLayout(this);
		childLayout.setOrientation(LinearLayout.HORIZONTAL);
		childLayout.setLayoutParams(wiParams);

		TextView tvTitle = new TextView(this, null, R.style.comm_view_left);
		TextView tvContent = new TextView(this, null, R.style.comm_view_left);

		LinearLayout warnLayout = null;
		if (!StringUtil.isNullOrEmpty(warn)) {
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			LinearLayout.LayoutParams postParams = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			warnLayout = new LinearLayout(this);
			warnLayout.setOrientation(0);
			layoutParams.weight = 2.3f;
			warnLayout.setLayoutParams(layoutParams);
			warnLayout.setId(1005);
			TextView postTitle = new TextView(this, null,
					R.style.comm_view_left);
			postTitle.setLayoutParams(postParams);
			postTitle.setGravity(Gravity.LEFT);
			postTitle.setTextColor(Color.GRAY);
			postTitle.setSingleLine(true);
			postTitle.setTextSize(textSize);
			postTitle.setText(warn);
			postTitle.setPadding(spLeftMargin, 0, 0, 0);
			tvContent.setLayoutParams(postParams);
			warnLayout.addView(tvContent);
			warnLayout.addView(postTitle);
		} else {
			textContentParams.weight = 2.3f;
			tvContent.setLayoutParams(textContentParams);
		}

		tvTitle.setSingleLine(true);
		tvTitle.setLayoutParams(textTitleParams);
		tvTitle.setText(titleType.trim() + "：");
//		if (titleType.trim().length() > 7) {
//			tvTitle.setGravity(Gravity.CENTER);
//			tvTitle.setEllipsize(TruncateAt.MIDDLE);
//		} else {
//			tvTitle.setGravity(Gravity.RIGHT);
//		}
		tvTitle.setGravity(Gravity.RIGHT);
		tvTitle.setEllipsize(TruncateAt.MIDDLE);
		
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tvTitle);
		tvTitle.setPadding(0, 0, rightPading, 0);
		tvTitle.setTextSize(textSize);

		tvContent.setGravity(Gravity.CENTER_VERTICAL);
		tvContent.setId(TEXTVIEW_ID);
		tvContent.setTextSize(textSize);
		tvContent.setSingleLine(true);

		childLayout.addView(tvTitle);
		if (warnLayout != null) {
			childLayout.addView(warnLayout);
		} else {
			childLayout.addView(tvContent);
		}

		return childLayout;
	}

	/**
	 * 创建普通输入框
	 * 
	 * @param check
	 * @param tvType
	 * @param maxleng
	 * @param postName
	 * @param type
	 * @return
	 */
	public LinearLayout createEditText(String check, String tvType,
			String maxleng, String postName, String type) {

		LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, editHeight);
		LinearLayout.LayoutParams bigEditParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		editParams.leftMargin = spLeftMargin;
		editParams.rightMargin = spLeftMargin;
		bigEditParams.leftMargin = spLeftMargin;
		bigEditParams.rightMargin = spLeftMargin;

		LinearLayout childLayout = new LinearLayout(this);
		childLayout.setOrientation(LinearLayout.HORIZONTAL);
		childLayout.setLayoutParams(wiParams);

		TextView tvTitle = new TextView(this, null, R.style.comm_view_left);
		EditText edContent = new EditText(this);
		TextView postTitle = null;
		if (!StringUtil.isNullOrEmpty(postName)) {
			editParams.weight = 4.4f;
			bigEditParams.weight = 4.4f;
			LinearLayout.LayoutParams postParams = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			postTitle = new TextView(this, null, R.style.comm_view_left);
			postParams.weight = 6.4f;
			postTitle.setLayoutParams(postParams);
			postTitle.setGravity(Gravity.LEFT);
			postTitle.setTextColor(Color.GRAY);
			postTitle.setSingleLine(true);
			postTitle.setTextSize(textSize);
			postTitle.setText(postName);
		} else {
			editParams.weight = 3.0f;
			bigEditParams.weight = 3.0f;
		}

		tvTitle.setLayoutParams(textParams);
		tvTitle.setGravity(Gravity.RIGHT);
		tvTitle.setSingleLine(true);
		tvTitle.setTextSize(textSize);
		if (check.contains(CHECK_UNNULL)) {
			tvTitle.setText("*" + tvType + "：");
		} else {
			tvTitle.setText(tvType + "：");
		}
//		if (tvType.length() > 7) {
			tvTitle.setEllipsize(TruncateAt.MIDDLE);
//		}
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tvTitle);

		if (type.equals(TYPE_BGEDIT)) {
			edContent.setLayoutParams(bigEditParams);
			edContent.setSingleLine(false);
			edContent.setLines(3);
			edContent.setGravity(Gravity.TOP);
		} else {
			edContent.setLayoutParams(editParams);
			edContent.setSingleLine(true);
			edContent.setGravity(Gravity.CENTER_VERTICAL);
		}
		edContent.setBackgroundResource(R.drawable.bg_for_edittext);
		edContent.setTextSize(textSize);
		edContent.setId(EDITTEXT_ID);
		edContent.setTag(check);

		// 输入最大限制
		if (!StringUtil.isNullOrEmpty(maxleng)) {
			int maxLeng = BlptUtil.getInstance().stringToint(maxleng);
			edContent
					.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
							maxLeng) });
		}

		if (!StringUtil.isNullOrEmpty(check) && check.indexOf(CHECK_PHONE) > 0) {
			edContent.setInputType(InputType.TYPE_CLASS_PHONE);

		} else if (!StringUtil.isNullOrEmpty(check) && check.indexOf("4") > 0) {
			edContent.setInputType(InputType.TYPE_CLASS_NUMBER);
		}
		//密码
		if (type.equals(TYPE_PASEDIT)) {
			edContent.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		}

		// 特殊匹配限制
//		for (int i = 0; i < ctType.length; i++) {
//			if (tvType.indexOf(ctType[i]) > 0 || tvType.equals(ctType[i])
//					|| tvType.indexOf(ctType[i]) == 0) {
//				if (tvType.indexOf(ctType[0]) > 0
//						|| tvType.indexOf(ctType[0]) == 0) {
//					edContent.setInputType(InputType.TYPE_CLASS_PHONE);
//				} else if (tvType.contains(ctType[2])) {
//					edContent.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
//				} else {
//					edContent.setInputType(InputType.TYPE_CLASS_NUMBER);
//				}
//				if (tvType.equals(ctType[1])) {
//					// edContent
//					// .setFilters(new InputFilter[] { new
//					// InputFilter.LengthFilter(
//					// 6) });
//				}
//			} else if (tvType.indexOf("英文") >= 0) {
//				edContent.setInputType(InputType.TYPE_CLASS_TEXT);
//			}
//		}

		childLayout.addView(tvTitle);
		childLayout.addView(edContent);
		if (postTitle != null) {
			childLayout.addView(postTitle);
		}

		return childLayout;
	}

	/**
	 * 创建密码输入框
	 * 
	 * @param title
	 *            项名
	 * @param tipmsg
	 * @param maxleng
	 * @return
	 */
//	public LinearLayout createPasEditText(String strTitle, String maxleng,
//			String tipmsg) {
//
//		LinearLayout childLayout = new LinearLayout(this);
//		childLayout.setOrientation(LinearLayout.HORIZONTAL);
//		childLayout.setLayoutParams(wiParams);
//
//		TextView tvTitle = new TextView(this, null, R.style.comm_view_left);
//		tvTitle.setLayoutParams(textParams);
//		tvTitle.setGravity(Gravity.RIGHT);
//		tvTitle.setSingleLine(true);
//		tvTitle.setTextSize(textSize);
//		tvTitle.setText(strTitle + "：");
//
//		SipBox sipBox = new SipBox(this, null);
//		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
//				LayoutParams.MATCH_PARENT, editHeight);
//		param.leftMargin = spLeftMargin;
//		param.rightMargin = spLeftMargin;
//		sipBox.setLayoutParams(param);
//		sipBox.setTextColor(getResources().getColor(android.R.color.black));
//		sipBox.setOutputValueType(ConstantGloble.OUT_PUT_VALUE_TYPE_OTP);
//		sipBox.setGravity(Gravity.CENTER_VERTICAL);
//		sipBox.setPasswordMinLength(1);
//		sipBox.setId(10002);
//		sipBox.setKeyBoardType(ConstantGloble.KEYBOARDTYPE_NUMBOER);
//		// 输入最大限制
//		if (!StringUtil.isNullOrEmpty(maxleng)) {
//			int intMaxLeng = BlptUtil.getInstance().stringToint(maxleng);
//			sipBox.setPasswordMaxLength(intMaxLeng);
//		}
//		sipBox.setBackgroundResource(R.drawable.bg_for_edittext);
//		sipBox.setPasswordRegularExpression(ConstantGloble.REGULAR_EXPRESSION);
//		sipBox.setSipDelegator(this);
//		sipBox.setRandomKey_S(BlptUtil.getInstance().getRandomNumber());
//
//		TextView postTitle = null;
//		if (!StringUtil.isNullOrEmpty(tipmsg)) {
//			param.weight = 4.4f;
//			LinearLayout.LayoutParams postParams = new LinearLayout.LayoutParams(
//					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//			postTitle = new TextView(this, null, R.style.comm_view_left);
//			postParams.weight = 6.4f;
//			postTitle.setLayoutParams(postParams);
//			postTitle.setGravity(Gravity.LEFT);
//			postTitle.setTextColor(Color.GRAY);
//			postTitle.setSingleLine(true);
//			postTitle.setTextSize(textSize);
//			postTitle.setText(tipmsg);
//		} else {
//			param.weight = 3.0f;
//		}
//
//		childLayout.addView(tvTitle);
//		childLayout.addView(sipBox);
//		if (postTitle != null) {
//			childLayout.addView(postTitle);
//		}
//
//		return childLayout;
//	}

	/**
	 * 创建下拉框
	 * 
	 * @return
	 */
	private LinearLayout createSpinner(String tvType, String warn) {
		LinearLayout.LayoutParams spParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, spHeight);
		LinearLayout childLayout = new LinearLayout(this);
		childLayout.setOrientation(LinearLayout.HORIZONTAL);
		spParams.leftMargin = spLeftMargin;
		spParams.rightMargin = spLeftMargin;
		childLayout.setLayoutParams(wiParams);

		TextView tvTitle = new TextView(this, null, R.style.comm_view_left);
		Spinner spContent = new Spinner(this);

		TextView postTitle = null;
		if (!StringUtil.isNullOrEmpty(warn)) {
			spParams.weight = 4.4f;
			LinearLayout.LayoutParams postParams = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			postTitle = new TextView(this, null, R.style.comm_view_left);
			postParams.weight = 6.4f;
			postTitle.setLayoutParams(postParams);
			postTitle.setGravity(Gravity.LEFT);
			postTitle.setTextColor(Color.GRAY);
			postTitle.setSingleLine(true);
			postTitle.setTextSize(textSize);
			postTitle.setText(warn);
		} else {
			spParams.weight = 3.0f;
		}

		tvTitle.setLayoutParams(textParams);
		tvTitle.setGravity(Gravity.CENTER_VERTICAL);
		tvTitle.setSingleLine(true);
		tvTitle.setText(tvType + "：");
		tvTitle.setGravity(Gravity.RIGHT);
		if (tvType.length() > 7) {
			tvTitle.setGravity(Gravity.LEFT);
		}
		tvTitle.setEllipsize(TruncateAt.MIDDLE);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				tvTitle);
		tvTitle.setTextSize(textSize);

		spContent.setLayoutParams(spParams);
		spContent.setBackgroundResource(R.drawable.bg_spinner);
		spContent.setId(SPINNER_ID);

		childLayout.addView(tvTitle);
		childLayout.addView(spContent);
		if (postTitle != null) {
			childLayout.addView(postTitle);
		}

		return childLayout;
	}

	/**
	 * 创建提示信息文本
	 * 
	 * @param msg
	 * @return
	 */
	private LinearLayout createTipTextView(String msg) {
		LinearLayout.LayoutParams tipParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		tipParams.weight = 3.0f;

		LinearLayout childLayout = new LinearLayout(this);
		childLayout.setLayoutParams(wiParams);
		childLayout.setId(1001);

		TextView hideView = new TextView(this, null, R.style.comm_view_left);
		hideView.setLayoutParams(textParams);
		hideView.setVisibility(View.INVISIBLE);
		TextView tipView = new TextView(this, null, R.style.comm_view_left);
		tipView.setLayoutParams(tipParams);
		tipView.setGravity(Gravity.CENTER_VERTICAL);
		// tipView.setSingleLine(true);
		tipView.setText(msg);
		tipView.setGravity(Gravity.LEFT);
		tipView.setTextSize(textSize);
		tipView.setTextColor(Color.GRAY);
		tipView.setEllipsize(TruncateAt.END);
		childLayout.addView(hideView);
		childLayout.addView(tipView);

		return childLayout;
	}

	// 发送短信验证码
	public void sendMSCToMobile() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Setting.SET_SENDMSC);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setParams(null);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "sendMSCToMobileCallback");
	}

	public void sendMSCToMobileCallback(Object resultObj) {
		// 通讯结束,关闭通讯框
		BiiHttpEngine.dissMissProgressDialog();
	}

	/**
	 * 请求密码控件随机数
	 */
	public void requestRandomNumber() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Login.COMM_RANDOM_NUMBER_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		HttpManager.requestBii(biiRequestBody, this, "randomNumberCallBack");
	}

	/**
	 * 请求密码控件随机数 回调
	 * 
	 * @param resultObj
	 */
	public void randomNumberCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String randomNum = (String) biiResponseBody.getResult();
		if (StringUtil.isNull(randomNum)) {
			return;
		}
		// 保存随机数
		BlptUtil.getInstance().setRandomNumber(randomNum);
	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		return ActivityTaskType.OneTask;
	}
}
