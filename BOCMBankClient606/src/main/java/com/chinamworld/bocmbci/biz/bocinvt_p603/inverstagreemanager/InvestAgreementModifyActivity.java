package com.chinamworld.bocmbci.biz.bocinvt_p603.inverstagreemanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.bocinvt_p603.BocInvtBaseActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.util.Trans2ChineseNumber;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.userwidget.LabelTextView;
import com.chinamworld.bocmbci.userwidget.LabelTextView.TextColor;
import com.chinamworld.bocmbci.userwidget.TitleAndContentLayout;
import com.chinamworld.bocmbci.utils.EditTextUtils;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;

/**
 * 中银理财 投资协议管理 协议修改处理页面
 * @author linyl
 *
 */
public class InvestAgreementModifyActivity extends BocInvtBaseActivity implements OnClickListener{

	public static final String TAG = "InvestAgreementModifyActivity";
	/**修改协议信息 、确认页面与成功 页面**/
	private TitleAndContentLayout mTitleAndContentLayout,mConfirmTitleAndContentLayout;
	/**存储协议详情信息 map对象**/
	private Map<String,Object> mAgrInfoMap,mAgrQueryMap,mItemMap;
	/**协议类型、投资方式**/
	private String mAgrType,mInstType;
	/**修改协议 字段布局**/
	private LinearLayout mPeriod_ll,mAmount_ll,mAmounttype_ll,mAmountdinge_ll,mAmountbudinge_ll;
	/**修改信息填写、确认、完成页面布局**/
	private LinearLayout mModifyInfo_ll,mModifyConfirm_ll;
	/**确认、成功页面的按钮布局**/
	private LinearLayout mBtnModifyConfirm_ll,mBtnModifyfinish_ll;
	/**成功页面的成功图标布局**/
	private LinearLayout mImaModifyfinish_ll;
	/**动态添加的控件**/
	private LinearLayout mMyContainer,mConfirmInfoTitle,mMyContainerInput;
	/**修改项 输入框 **/
	private EditText mEtPeriod,mEt_Amount,mEt_MinAmount,mEt_MaxAmount;
	/**修改项 下拉框 **/
	private Spinner mSp_AmountType;
	/**修改协议的字段信息**/
	private String mEtPeriodText,mEt_AmountText,mAmountTypeText;
	/**修改协议 上送字段 **/
	private String mAmountType,mAccountKey,mAgrCode,mIsNeedPur,mUnit,mCharCode,mProId,mSerialCode,mCustAgrCode;
	/**页面按钮**/
	public Button mBtnConfirmOne,mBtnConfirmNext,mBtnFinish;
	/**定义修改页面的字段**/
	public String mMinAmount,mMaxAmount,mBuyPeriod,mFinishperiod,mRemaindperiod;
	/**修改预交易 修改字段变量声明 **/
	private String mMinAmountRes,mMaxAmountRes,mAmountRes,mPeriodRes;
	/**修改与交易的接口返回结果**/
	public Map<String,Object> agrModifyVerifyMap,agrModifyCommitMap;
	/**修改信息页面 修改字段名**/
	private TextView mAmountTv,mMinAmountTv,mMaxAmountTv;
	/**协议交易方向**/
	private String mTradeCodeRes;
	/**协议维护 上送字段**/
	private String mProName,mproCur,mPeriodAge,mPeriodAgeValue,mPeriodAgeUnit;
	/** 服务器时间 */
	private String dateTime,mFirstDateRed;
	/**tokenId**/
	private String tokenId;
	/** 周期性产品协议修改上送字段 **/
	private String mAgrName;
	/** 修改页面 字段值的文本textview **/
	public TextView tv_amountType,tv_period,tv_amount,tv_minAmount,tv_maxAmount;
	/** 修改页面 金额输入回显金额 大写文本 **/
	public TextView tv_for_amout,tv_for_minamount,tv_for_maxamout,tv_xysgje,tv_maxamountinfo;
	private int temp;
	/**期数标量  -1 不限期**/
	private String periodFlag = "" ;
	/** 协议份额标量    -1全额赎回 **/
	private String amountFlag = "" ;
	/** 协议申购金额布局 **/
	private LinearLayout ll_xysgje;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogGloble.d(TAG, "onCreate");
		getBackgroundLayout().setTitleText(R.string.boc_invest_agrmodify_title);
		getBackgroundLayout().setRightButtonText("主界面");
		getBackgroundLayout().setPadding(0, 0, 0, 0);
		setContentView(R.layout.boc_invest_agrmodify);
		init();
	}

	/**
	 * 初始化页面
	 */
	@SuppressWarnings("unchecked")
	private void init() {
		mModifyInfo_ll = (LinearLayout) findViewById(R.id.agr_modify_main);
		mModifyConfirm_ll = (LinearLayout) findViewById(R.id.modifyInfoconfirm_ll);
		mConfirmInfoTitle = (LinearLayout) findViewById(R.id.modify_confirm_info_title);
		mTitleAndContentLayout = (TitleAndContentLayout) findViewById(R.id.agr_modify_main_info);
		mTitleAndContentLayout.setTitleVisibility(View.GONE);
		mAgrInfoMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.BOCINVT_AGREEMENTINFOQUERY_MAP);
		mAgrQueryMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.BOCINVT_CAPACITYQUERY_MAP);
		mItemMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BOCINVT_CAPACITYQUERY_MAP);
		mAgrType = String.valueOf(mAgrInfoMap.get(BocInvt.BOCINVT_AGRINFOQUERY_AGRTYPE_RES));
		mInstType = String.valueOf(mAgrInfoMap.get(BocInvt.BOCINVT_AGRINFOQUERY_INSTTYPE_RES));
		mAmountType = String.valueOf(mAgrInfoMap.get(BocInvt.BOCINVT_AGRINFOQUERY_AMOUNTTYPE_RES));
		mEtPeriodText = String.valueOf(mAgrInfoMap.get(BocInvt.BOCINVT_AGRINFOQUERY_PERIODTOTAL_RES));
		mEt_AmountText = String.valueOf(mAgrInfoMap.get(BocInvt.BOCINVT_AGRINFOQUERY_AMOUNT_RES));
		mAmountTypeText = LocalData.bociAmountTypeMap.get(mAmountType);
		mMinAmount = String.valueOf(mAgrInfoMap.get(BocInvt.BOCINVT_AGRINFOQUERY_MINANOUNT_RES));
		mMaxAmount = String.valueOf(mAgrInfoMap.get(BocInvt.BOCINVT_AGRINFOQUERY_MAXAMOUNT_RES));
		mBuyPeriod = String.valueOf(mAgrInfoMap.get(BocInvt.BOCINVT_AGRINFOQUERY_BUYPERIOD_RES));
		mFinishperiod = String.valueOf(mAgrInfoMap.get(BocInvt.BOCINVT_AGRINFOQUERY_FINISHPERIOD_RES));
		mRemaindperiod = String.valueOf(mAgrInfoMap.get(BocInvt.BOCINVT_AGRINFOQUERY_REMAINDEPERIOD_RES));
		mIsNeedPur = String.valueOf(mAgrInfoMap.get(BocInvt.BOCINVT_AGRINFOQUERY_ISNEEDPUR_RES));
		mTradeCodeRes = String.valueOf(mAgrInfoMap.get(BocInvt.BOCINVT_AGRINFOQUERY_TRADECODE_RES));
		//上送字段赋值
		mAccountKey = String.valueOf(mAgrQueryMap.get(BocInvt.BOCINVT_CAPACITYQUERY_ACCOUNTKEY_RES));
		mCustAgrCode = String.valueOf(mAgrQueryMap.get(BocInvt.BOCINVT_CAPACITYQUERY_CUSTAGECODE_RES));
		mAgrCode = String.valueOf(mAgrInfoMap.get(BocInvt.BOCINVT_AGRINFOQUERY_AGRCODE_RES));
		mUnit = String.valueOf(mAgrInfoMap.get(BocInvt.BOCINVT_AGRINFOQUERY_UNIT_RES));
		mCharCode = String.valueOf(mAgrInfoMap.get(BocInvt.BOCINVT_AGRINFOQUERY_CASHREMIT_RES));
		mProId = String.valueOf(mAgrInfoMap.get(BocInvt.BOCINVT_AGRINFOQUERY_PROID_RES));
		mSerialCode = String.valueOf(mAgrInfoMap.get(BocInvt.BOCINVT_AGRINFOQUERY_SERIALCODE_RES));
		mProName = String.valueOf(mAgrInfoMap.get(BocInvt.BOCINVT_AGRINFOQUERY_PRONAME_RES));
		mproCur = String.valueOf(mAgrInfoMap.get(BocInvt.BOCINVT_AGRINFOQUERY_PROCUR_RES));
		mPeriodAge = String.valueOf(mAgrInfoMap.get(BocInvt.BOCINVT_AGRINFOQUERY_PERIODAGE_RES));
		mPeriodAgeValue =  mPeriodAge.trim().substring(0, mPeriodAge.trim().length()-1);
		mPeriodAgeUnit = mPeriodAge.trim().substring(mPeriodAge.trim().length()-1, mPeriodAge.trim().length());
		mAgrName = String.valueOf(mAgrInfoMap.get(BocInvt.BOCINVT_AGRINFOQUERY_AGRNAME_RES));
		mFirstDateRed = String.valueOf(mAgrInfoMap.get(BocInvt.BOCINVT_AGRINFOQUERY_FIRSTDATERED_RES));
		//修改协议字段页面布局
		View modifyView = LayoutInflater.from(this).inflate(R.layout.boc_agrmodifyinput_layout, null);
		mTitleAndContentLayout.addView(modifyView);
		mPeriod_ll = (LinearLayout) modifyView.findViewById(R.id.agr_modify_period);
		mAmount_ll = (LinearLayout) modifyView.findViewById(R.id.agr_modify_amount);
		mAmounttype_ll = (LinearLayout) modifyView.findViewById(R.id.agr_modify_amounttype);
		mAmountdinge_ll = (LinearLayout) modifyView.findViewById(R.id.amount_dinge_ll);
		mAmountbudinge_ll = (LinearLayout) modifyView.findViewById(R.id.amount_budinge_ll);
		mMyContainerInput = (LinearLayout) findViewById(R.id.mycontainerLayout_input);
		mMyContainerInput.addView(createLabelTextView(R.string.boc_invest_finishperiod, mFinishperiod));
        if(mRemaindperiod.equals("-1") || mRemaindperiod.equals("-000001")){//剩余期数 不限期
        	mMyContainerInput.addView(createLabelTextView(R.string.boc_invest_remaindperiod, "不限期"));
		}else{
			mMyContainerInput.addView(createLabelTextView(R.string.boc_invest_remaindperiod, mRemaindperiod));
		}
        ll_xysgje = (LinearLayout) modifyView.findViewById(R.id.ll_xysgje);
        tv_xysgje = (TextView) modifyView.findViewById(R.id.tv_agrsgje);//协议申购金额回显值
		mAmountTv = (TextView) modifyView.findViewById(R.id.tv_agr_modify_amount);
		mMinAmountTv = (TextView) modifyView.findViewById(R.id.tv_agr_modify_amount_min);
		mMaxAmountTv = (TextView) modifyView.findViewById(R.id.tv_agr_modify_amount_max);
		mEtPeriod = (EditText) modifyView.findViewById(R.id.et_agr_modify_period);
		mEt_Amount = (EditText) modifyView.findViewById(R.id.et_agr_modify_amount);
		tv_for_amout = (TextView) modifyView.findViewById(R.id.tv_for_amount_one);
//		mEt_AmountText = StringUtil.parseStringCodePattern(mproCur,mEt_AmountText,2);
		if(Integer.valueOf(mAgrType) == 2 && mTradeCodeRes.equals("0")){//定时定额 赎回时
			Trans2ChineseNumber.relateNumInputAndChineseShower(mEt_Amount, tv_for_amout);
		}else{
			EditTextUtils.relateNumInputToChineseShower(mEt_Amount,tv_for_amout);
		}
		mSp_AmountType =  (Spinner) modifyView.findViewById(R.id.sp_agr_modify_amounttype);
		mEt_MinAmount = (EditText) modifyView.findViewById(R.id.et_agr_modify_amount_min);
		mEt_MaxAmount = (EditText) modifyView.findViewById(R.id.et_agr_modify_amount_max);
		tv_for_minamount = (TextView) modifyView.findViewById(R.id.tv_for_minamount);
		tv_for_maxamout = (TextView) modifyView.findViewById(R.id.tv_for_maxamount);
		tv_maxamountinfo = (TextView) modifyView.findViewById(R.id.tv_maxamountinfo);
		EditTextUtils.relateNumInputToChineseShower(mEt_MinAmount,tv_for_minamount);
		EditTextUtils.relateNumInputToChineseShower(mEt_MaxAmount,tv_for_maxamout);
		tv_amountType = (TextView) modifyView.findViewById(R.id.tv_amounttype);
		tv_period = (TextView) modifyView.findViewById(R.id.tv_period);
		tv_amount = (TextView) modifyView.findViewById(R.id.tv_amount);
		tv_minAmount = (TextView) modifyView.findViewById(R.id.tv_minamount);
		tv_maxAmount = (TextView) modifyView.findViewById(R.id.tv_maxamount);
		if(!StringUtil.isNull(mBuyPeriod)){
			if(mBuyPeriod.equals("-000001") || mBuyPeriod.equals("-1")){
				periodFlag = "-1";
				mBuyPeriod = "不限期";
				mEtPeriod.setHint(R.string.bocinvt_noperiod);
			}else{
				mEtPeriod.setText(mBuyPeriod);
			}
		} 
		if(!StringUtil.isNull(mRemaindperiod)){
			if(mRemaindperiod.equals("-000001") || mRemaindperiod.equals("-1")){
				mRemaindperiod = "不限期";
			}
		}
//		mMaxAmount = StringUtil.parseStringCodePattern(mproCur,mMaxAmount,2);
//		mMinAmount = StringUtil.parseStringCodePattern(mproCur,mMinAmount,2);
		tv_amountType.setText(mAmountTypeText);
		tv_period.setText(mBuyPeriod);
		tv_amount.setText(mEt_AmountText);
		tv_minAmount.setText(mMinAmount);
		tv_maxAmount.setText(mMaxAmount);
//		tv_minAmount.setText(mMaxAmount);
//		tv_maxAmount.setText(mMinAmount);
		if(mproCur.equals("027")){
			mEt_AmountText = StringUtil.parseStringPattern3(mEt_AmountText, 0);
			mMaxAmount = StringUtil.parseStringPattern3(mMaxAmount, 0);
			mMinAmount = StringUtil.parseStringPattern3(mMinAmount, 0);
		}
		createLabelTextView(Integer.valueOf(mAgrType));
		getBackgroundLayout().setLeftButtonClickListener(backClickListener);
		mBtnConfirmOne = (Button) findViewById(R.id.agrmodifymain_confirm);
		mBtnConfirmOne.setOnClickListener(this);
//		requestCommConversationId();
//		BaseHttpEngine.showProgressDialogCanGoBack();
	}

//	/**
//	 * 重写父类方法   请求conversationId 接口回调
//	 */
//	@Override
//	public void requestCommConversationIdCallBack(Object resultObj) {
//		super.requestCommConversationIdCallBack(resultObj);
//		requestPSNGetTokenId();
//	}
//	
//	/**
//	 * 请求TokenId 
//	 */
//	public void requestPSNGetTokenId(){
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
//		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
//				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
//		HttpManager.requestBii(biiRequestBody, this, "aquirePSNGetTokenId");
//	}
//	/**
//	 * 请求TokenId 回调
//	 * @param resultObj
//	 */
//	public void aquirePSNGetTokenId(Object resultObj){
//		tokenId = (String) SafetyUtils.httpResponseDeal(resultObj);
//		if (StringUtil.isNullOrEmpty(tokenId)) {
//			BaseHttpEngine.dissMissProgressDialog(); return;
//		}
//		BaseHttpEngine.dissMissProgressDialog();
//	}
	
	/**
	 * 加载页面 (取值默认反显)
	 * @param type   协议类型
	 */
	private void createLabelTextView(int type){
		switch (type) {
		case 1://智能投资
//			mPeriod_ll.setVisibility(View.VISIBLE);
//			mEtPeriod.setText(mBuyPeriod);
			mSp_AmountType.setVisibility(View.GONE);
			tv_amountType.setVisibility(View.VISIBLE);
			mEt_Amount.setVisibility(View.GONE);
			tv_amount.setVisibility(View.VISIBLE);
			mEt_MinAmount.setVisibility(View.GONE);
			tv_minAmount.setVisibility(View.VISIBLE);
			mEt_MaxAmount.setVisibility(View.GONE);
			tv_maxAmount.setVisibility(View.VISIBLE);
			mEt_AmountText = StringUtil.parseStringCodePattern(mproCur,mEt_AmountText,2);
			tv_amount.setText(mEt_AmountText);
			mMinAmount = StringUtil.parseStringCodePattern(mproCur,mMinAmount,2);
			mMaxAmount = StringUtil.parseStringCodePattern(mproCur,mMaxAmount,2);
			tv_minAmount.setText(mMinAmount);
			tv_maxAmount.setText(mMaxAmount);
			if(mInstType.equals("4")){//多次赎回协议  
				mAmountTv.setText(R.string.boc_invest_amountexec_unit);
				if(mIsNeedPur.equals("0")){//是否申购为  0  期初申购
					ll_xysgje.setVisibility(View.VISIBLE);
					tv_xysgje.setText(mEt_AmountText);
					//协议赎回份额 取消日元格式化  固定两位小数显示
					mEt_AmountText = String.valueOf(mAgrInfoMap.get(BocInvt.BOCINVT_AGRINFOQUERY_AMOUNT_RES));
					mEt_AmountText = StringUtil.parseStringPattern(mEt_AmountText,2);
//					tv_amount.setText(mEt_AmountText);
					tv_amount.setText(StringUtil.parseStringPattern(mUnit,2));//取协议赎回份额
				}else if(mIsNeedPur.equals("1")){//不申购
					ll_xysgje.setVisibility(View.GONE);
					tv_amount.setText(StringUtil.parseStringPattern(mUnit,2));//取协议赎回份额
				}
			}
			break;
		case 2://定时定额
//			mPeriod_ll.setVisibility(View.VISIBLE);
//			mAmount_ll.setVisibility(View.VISIBLE);
//			mAmountdinge_ll.setVisibility(View.VISIBLE);
			if(mTradeCodeRes.equals("0")){//交易方向为赎回时 字段名显示 投资份额  反之默认显示投资金额
				mAmountTv.setText(R.string.boc_invest_tzfe);
				/**Bii返回 -1 代表全额赎回**/
//				mEt_AmountText = "全额赎回";
				mEt_Amount.setHint("全额赎回");
				if(mEt_AmountText.equals("-1.00") || mEt_AmountText.equals("-1")){//日元币种情况下 会处理成-1 ，其他情况是返回-1.00
					amountFlag = "-1";
//					mEt_AmountText = "全额赎回";
//					mEt_Amount.setHint(mEt_AmountText);
				}else{
					mEt_AmountText = String.valueOf(mAgrInfoMap.get(BocInvt.BOCINVT_AGRINFOQUERY_AMOUNT_RES));
					mEt_AmountText = StringUtil.parseStringPattern3(mEt_AmountText,2);
					mEt_Amount.setText(mEt_AmountText);
				}
			}else if(mTradeCodeRes.equals("1")){//购买
				mAmountTv.setText(R.string.boc_invest_amountexec_two);
				mEt_Amount.setText(mEt_AmountText);
			}
			mSp_AmountType.setVisibility(View.GONE);
			tv_amountType.setVisibility(View.VISIBLE);
//			mEtPeriod.setText(mBuyPeriod);
//			mEt_Amount.setText(mEt_AmountText);
			mEt_MinAmount.setVisibility(View.GONE);
			tv_minAmount.setVisibility(View.VISIBLE);
			mEt_MaxAmount.setVisibility(View.GONE);
			tv_maxAmount.setVisibility(View.VISIBLE);
			break;
		case 3://周期滚续
		case 5://业绩基准周期滚续
			mMinAmountTv.setText(R.string.minAmount);
			mMaxAmountTv.setText(R.string.maxAmount);
			if(Integer.valueOf(mInstType) == 7){//周期滚续 表内产品
//				mPeriod_ll.setVisibility(View.VISIBLE);
//				mAmount_ll.setVisibility(View.VISIBLE);
//				mAmounttype_ll.setVisibility(View.VISIBLE);
				mEtPeriod.setVisibility(View.VISIBLE);
				tv_period.setVisibility(View.GONE);
//				mEtPeriod.setText(mBuyPeriod);
//				if(!StringUtil.isNull(mBuyPeriod)){
//					if(mBuyPeriod.equals("-000001") || mBuyPeriod.equals("-1") || mBuyPeriod.equals("不限期")){
//						mBuyPeriod = "不限期";
//						mEtPeriod.setHint(R.string.bocinvt_noperiod);
//					}else{
//						mEtPeriod.setText(mBuyPeriod);
//					}
//				} 
				mSp_AmountType.setVisibility(View.GONE);
				tv_amountType.setVisibility(View.VISIBLE);
//				mSp_AmountType.setVisibility(View.VISIBLE);
//				tv_amountType.setVisibility(View.GONE);
//				mSp_AmountType.setSelection(Integer.valueOf(mAmountType));
////				createLabelTextView(4);
//				//不同金额模式 动态显示对应的金额
//				mSp_AmountType.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//					@Override
//					public void onItemSelected(AdapterView<?> parent,
//							View view, int position, long id) {
//						if(position == 0){//定额
//							mAmountdinge_ll.setVisibility(View.VISIBLE);
//							mEt_Amount.setText(mEt_AmountText);
//							mAmountbudinge_ll.setVisibility(View.GONE);
//							return;
//						}else if(position == 1){//不定额
//							mAmountdinge_ll.setVisibility(View.GONE);
//							mAmountbudinge_ll.setVisibility(View.VISIBLE);
//							mEt_MinAmount.setText(mMaxAmount);
//							mEt_MaxAmount.setText(mMinAmount);
//							return;
//						}
//					}
//
//					@Override
//					public void onNothingSelected(AdapterView<?> parent) {
//						
//					}
//				});
				if(Integer.valueOf(mAmountType) == 0){//定额
					mEt_Amount.setText(mEt_AmountText);
				}else if(Integer.valueOf(mAmountType) == 1){//不定额
					mEt_MinAmount.setText(mMinAmount);
					mEt_MaxAmount.setText(mMaxAmount);
				}
			}else if(Integer.valueOf(mInstType) == 8){//业绩基准 周期滚续
//				mPeriod_ll.setVisibility(View.VISIBLE);
//				mEtPeriod.setText(mBuyPeriod);
				mSp_AmountType.setVisibility(View.GONE);
				tv_amountType.setVisibility(View.VISIBLE);
				mEt_Amount.setVisibility(View.GONE);
				tv_amount.setVisibility(View.VISIBLE);
				mEt_MinAmount.setVisibility(View.GONE);
				tv_minAmount.setVisibility(View.VISIBLE);
				mEt_MaxAmount.setVisibility(View.GONE);
				tv_maxAmount.setVisibility(View.VISIBLE);
				mEt_AmountText = StringUtil.parseStringCodePattern(mproCur,mEt_AmountText,2);
				tv_amount.setText(mEt_AmountText);
			}
			break;
		case 4://余额理财
//			mAmount_ll.setVisibility(View.VISIBLE);
			if(Integer.valueOf(mAmountType) == 0){//定额
//				mAmountdinge_ll.setVisibility(View.VISIBLE);
//				mAmountbudinge_ll.setVisibility(View.GONE);
				mSp_AmountType.setVisibility(View.GONE);
				tv_amountType.setVisibility(View.VISIBLE);
				mEtPeriod.setVisibility(View.GONE);
				tv_period.setVisibility(View.VISIBLE);
				mEt_Amount.setText(mEt_AmountText);
				mEt_MinAmount.setVisibility(View.GONE);
				tv_minAmount.setVisibility(View.VISIBLE);
				mEt_MaxAmount.setVisibility(View.GONE);
				tv_maxAmount.setVisibility(View.VISIBLE);
			}else if(Integer.valueOf(mAmountType) == 1){//不定额
//				mAmountdinge_ll.setVisibility(View.GONE);
//				mAmountbudinge_ll.setVisibility(View.VISIBLE);
//				if(Integer.valueOf(mAgrType) == 3){
//					mMinAmountTv.setText(R.string.minAmount);
//					mMaxAmountTv.setText(R.string.maxAmount);
//				}
				
				mMaxAmountTv.setText(R.string.boc_invest_minamount);
				mMinAmountTv.setText(R.string.boc_invest_maxamount);
				tv_maxamountinfo.setVisibility(View.VISIBLE);
				mSp_AmountType.setVisibility(View.GONE);
				tv_amountType.setVisibility(View.VISIBLE);
				mEtPeriod.setVisibility(View.GONE);
				tv_period.setVisibility(View.VISIBLE);
				mEt_Amount.setVisibility(View.GONE);
				tv_amount.setVisibility(View.VISIBLE);
				mEt_MinAmount.setText(mMinAmount);
				mEt_MaxAmount.setText(mMaxAmount);
			}
			break;
		}
		if(!StringUtil.isNull(mBuyPeriod)){
			if(mBuyPeriod.equals("-000001") || mBuyPeriod.equals("-1") || mBuyPeriod.equals("不限期")){
				periodFlag = "-1";
				mBuyPeriod = "不限期";
				mEtPeriod.setHint(R.string.bocinvt_noperiod);
			}else{
				mEtPeriod.setText(mBuyPeriod);
			}
		} 
		if(Integer.valueOf(mAmountType) == 0){//定额
			mAmountbudinge_ll.setVisibility(View.GONE);
		}else if(Integer.valueOf(mAmountType) == 1){//不定额
			mAmountdinge_ll.setVisibility(View.GONE);
		}
	}

	@Override
	/**
	 * 按钮点击事件 处理
	 * @param v
	 */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.agrmodifymain_confirm://预修改确认按钮
			ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
			//输入信息校验
			if(mEtPeriod.isShown()){// 校验期数
				if(StringUtil.isNullOrEmpty(mEtPeriod.getText().toString().trim()) && !periodFlag.equals("-1")){
					BaseDroidApp.getInstanse().showInfoMessageDialog("请输入签约期数");
					return;
				}
				if(mEtPeriod.getText().toString().trim().equals("")){//不限期
					
				}else{//有限期
					RegexpBean reb0 = new RegexpBean("签约期数", mEtPeriod.getText().toString().trim(), "sixNumberAndOne");
					lists.add(reb0);
				}
			}
			
			if(mEt_Amount.isShown()){//校验定额 单期购买金额
				if(!(Integer.valueOf(mAgrType) == 2 && mTradeCodeRes.equals("0"))){//非定时定额 赎回的情况 校验空
					if(StringUtil.isNullOrEmpty(mEt_Amount.getText().toString().trim())){
						   BaseDroidApp.getInstanse().showInfoMessageDialog("请输入"+mAmountTv.getText().toString().substring(0, mAmountTv.getText().toString().length()-1));
						   return;
						}
				}
				if(mEt_Amount.getText().toString().trim().equals("")){//赎回 全额赎回  默认校验通过
					amountFlag = "-1";
					tv_for_amout.setVisibility(View.GONE);
				}else{
//					if(Integer.valueOf(mAgrType) == 2 && mTradeCodeRes.equals("0")){//定时定额 赎回 
//						
//					}
					RegexpBean reb1 = checkJapReg(mproCur,
							mAmountTv.getText().toString().substring(0, mAmountTv.getText().toString().length()-1),
									mEt_Amount.getText().toString().trim());
					lists.add(reb1);
				}
			}
			if(mEt_MinAmount.isShown()){//交易不定额模式的金额
				if(StringUtil.isNullOrEmpty(mEt_MinAmount.getText().toString().trim())){
					BaseDroidApp.getInstanse().showInfoMessageDialog("请输入"+mMinAmountTv.getText().toString().substring(0, mMinAmountTv.getText().toString().length()-1));
					return;
				}
				if(StringUtil.isNullOrEmpty(mEt_MaxAmount.getText().toString().trim())){
					BaseDroidApp.getInstanse().showInfoMessageDialog("请输入"+mMaxAmountTv.getText().toString().substring(0, mMaxAmountTv.getText().toString().length()-1));
					return;
				}
				RegexpBean reb2 = checkJapReg(mproCur,
						mMinAmountTv.getText().toString().substring(0, mMinAmountTv.getText().toString().length()-1),
						mEt_MinAmount.getText().toString().trim());
				RegexpBean reb3 = checkJapReg(mproCur,
						mMaxAmountTv.getText().toString().substring(0, mMaxAmountTv.getText().toString().length()-1),
						mEt_MaxAmount.getText().toString().trim());
				lists.add(reb2);
				lists.add(reb3);
			}
			
			if (!RegexpUtils.regexpDate(lists)) {// 校验不通过
				return;
			}else{//正则校验格式 校验通过
				//前端 新增余额理财 购买触发金额大于等于赎回触发金额
				if(Integer.valueOf(mAgrType) == 4 && Integer.valueOf(mAmountType) == 1){//余额理财 不定额
					if(Double.parseDouble(mEt_MaxAmount.getText().toString()) < Double.parseDouble(mEt_MinAmount.getText().toString())){//不满足要求 return
						BaseDroidApp.getInstanse().showInfoMessageDialog("购买触发金额不能低于赎回触发金额");
						return;
					}
				}
//				mModifyInfo_ll.setVisibility(View.GONE);
//				mModifyConfirm_ll.setVisibility(View.VISIBLE);
				initConfirmView();
				requestPsnXpadQueryRiskMatch();
				BiiHttpEngine.showProgressDialogCanGoBack();
//				initConfirmView();
			}
//			initConfirmView();
			break;
		case R.id.agrmodify_infoconfirm_confirm://确认页面的确认按钮
			temp = 2;
			requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
					.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
//			if(Integer.valueOf(mAgrType) == 1){//智能投资协议修改
//				temp = 1;
//				requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
//						.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
////				requestPsnXpadInvestAgreementModifyCommit();
//			}else if(Integer.valueOf(mAgrType) == 2){//定时定额 投资协议修改
//				requestPsnXpadAutomaticAgreementMaintainResult("0","0",tokenId);
//			}else if(Integer.valueOf(mAgrType) == 3){//周期滚续 投资协议修改
//				if (StringUtil.isNull(tokenId)) {
//					BaseHttpEngine.dissMissProgressDialog();
//					return;
//				}
//				if(mInstType.equals("7")){//周期滚续表内
//					requestPsnXpadAgreementModifyResult(tokenId);
//				}else if(mInstType.equals("8")){//周期滚续业绩基准
//					requestPsnXpadBenchmarkMaintainResult(tokenId,"1");
//				}
//			}else if(Integer.valueOf(mAgrType) == 4){//余额理财 投资协议修改
//				requestPsnXpadAutomaticAgreementMaintainResult("1","0",tokenId);
//			}
			break;
		}
	}
	public RegexpBean checkJapReg(String currency, String text1, String text2) {
		RegexpBean rebnew = null;
		if(Integer.valueOf(mAgrType) != 4){//非余额理财协议  不可以输入0
			if(Integer.valueOf(mAgrType) == 2 && mTradeCodeRes.equals("0")){//定时定额 赎回 不用校验日元格式
				rebnew = new RegexpBean(text1, text2, "amount");
			}else{
				if (checkJap(currency)) {
					rebnew = new RegexpBean(text1, text2, "spetialAmount2");
				} else { 
					rebnew = new RegexpBean(text1, text2, "amount");
				}
			}
		}else{//余额理财协议   0可以过校验
			if (checkJap(currency)) {
				rebnew = new RegexpBean(text1, text2, "spetialAmount3");
			} else {
				rebnew = new RegexpBean(text1, text2, "amount1");
			}
		}
		return rebnew;
	}
	public boolean checkJap(String currency) {
		if (!StringUtil.isNull(currency)) {
			if (japList.contains(currency)) {
				return true;
			}
		}
		return false;
	}
	/** 日元币种 */
	public static final List<String> japList = new ArrayList<String>() {
		{
			add("027");
			add("JPY");
			add("088");
			add("KRW");
			add("064");
			add("VND");
		}
	};
	/**
	 * 初始化修改信息确认与成功页面
	 */
	private void initConfirmView() {
		mConfirmTitleAndContentLayout = (TitleAndContentLayout) findViewById(R.id.agr_modify_info_confirm);
		mMyContainer = (LinearLayout) mConfirmTitleAndContentLayout.findViewById(R.id.mycontainerLayout);
		mBtnModifyConfirm_ll = (LinearLayout) findViewById(R.id.agrmodify_infoconfirm_btn_ll);
		mBtnModifyfinish_ll = (LinearLayout) findViewById(R.id.agrmodify_success_btn_ll);
//		mConfirmTitleAndContentLayout.setTitleText(R.string.boc_invest_agrinfo_title1);//去掉协议信息标题
		mConfirmTitleAndContentLayout.setTitleVisibility(View.GONE);
		mBtnConfirmNext = (Button) findViewById(R.id.agrmodify_infoconfirm_confirm);
		mBtnConfirmNext.setOnClickListener(this); 
//		//判断协议类型 调用不同的修改接口
//		if(Integer.valueOf(mAgrType) == 1){//智能投资协议修改
//			temp = 3;
//			requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
//					.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
//			requestPsnXpadInvestAgreementModifyVerify(tokenId);
//		}else if(Integer.valueOf(mAgrType) == 2){//定时定额 投资协议修改
//			setTitleAndContentValue(2);
//			initConfirmAndResultView(2);
//			requestSystemDateTime();
//			BaseHttpEngine.showProgressDialog();
//		}else if(Integer.valueOf(mAgrType) == 3){//周期滚续 投资协议修改
//			setTitleAndContentValue(3);
//			initConfirmAndResultView(3);
//			requestSystemDateTime();
//			BaseHttpEngine.showProgressDialog();
//		}else if(Integer.valueOf(mAgrType) == 4){//余额理财 投资协议修改
//			setTitleAndContentValue(4);
//			initConfirmAndResultView(4);
//			requestSystemDateTime();
//			BaseHttpEngine.showProgressDialog();
//		}else{
//			setTitleAndContentValue(Integer.valueOf(mAgrType));
//			initConfirmAndResultView(Integer.valueOf(mAgrType));
////			requestSystemDateTime();
////			BaseHttpEngine.showProgressDialog();
//		}
	}

	/**
	 * 智能投资协议 修改预交易 接口
	 */
	private void requestPsnXpadInvestAgreementModifyVerify(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.BOCINVT_PNSXPADINVESTAGREEMENTMODIFYVERIFY_API);
		biiRequestBody.setConversationId(String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID)));
		Map<String,Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put(BocInvt.BOCINVT_AGRMODIFYVERIFY_ACCOUNTKEY_REQ, mAccountKey);
		paramsMap.put(BocInvt.BOCINVT_AGRMODIFYVERIFY_AGRCODE_REQ, mAgrCode);
		paramsMap.put(BocInvt.BOCINVT_AGRMODIFYVERIFY_CUSTAGRCODE_REQ, mCustAgrCode);
		paramsMap.put(BocInvt.BOCINVT_AGRMODIFYVERIFY_AMOUNTTYPE_REQ, mAmountType);
		if(!StringUtil.isNull(mBuyPeriod)){
			if(mBuyPeriod.equals("-000001") || mBuyPeriod.equals("-1") || mEtPeriod.getText().toString().trim().equals("")){//是不限期
				paramsMap.put(BocInvt.BOCINVT_AGRMODIFYVERIFY_ISCONTROL_REQ, "1");
				paramsMap.put(BocInvt.BOCINVT_AGRMODIFYVERIFY_TOTALPERIOD_REQ, "-1");
			}else{//限期
				paramsMap.put(BocInvt.BOCINVT_AGRMODIFYVERIFY_ISCONTROL_REQ, "0");
				paramsMap.put(BocInvt.BOCINVT_AGRMODIFYVERIFY_TOTALPERIOD_REQ, mEtPeriod.getText().toString().equals("不限期") ? "-1" : mEtPeriod.getText().toString());
			}
		}
//		if(Integer.valueOf(mAmountType) == 0 || Integer.valueOf(mAgrType) == 2){//定额
////			paramsMap.put(BocInvt.BOCINVT_AGRMODIFYVERIFY_AMOUNT_REQ, mEt_Amount.getText().toString());
//			paramsMap.put(BocInvt.BOCINVT_AGRMODIFYVERIFY_AMOUNT_REQ,tv_amount.getText().toString());
//		}
//		if(Integer.valueOf(mAmountType) == 1){//不定额
//			paramsMap.put(BocInvt.BOCINVT_AGRMODIFYVERIFY_MINAMOUNT_REQ,tv_minAmount.getText().toString());
//			paramsMap.put(BocInvt.BOCINVT_AGRMODIFYVERIFY_MAXAMOUNT_REQ, tv_maxAmount.getText().toString());
//		}
//		if(Integer.valueOf(mInstType) == 4){//多次赎回
//			paramsMap.put(BocInvt.BOCINVT_AGRMODIFYVERIFY_NUIT_REQ, mUnit);
//		}
		if(!StringUtil.isNullOrEmpty(mEt_AmountText)){
			mEt_AmountText = mEt_AmountText.replaceAll(",", "");
		}
		if(!StringUtil.isNullOrEmpty(mMinAmount)){
			mMinAmount = mMinAmount.replaceAll(",", "");
		}
		if(!StringUtil.isNullOrEmpty(mMaxAmount)){
			mMaxAmount = mMaxAmount.replaceAll(",", "");
		}
		if(!StringUtil.isNullOrEmpty(mUnit)){
			mUnit = mUnit.replaceAll(",", "");
		}
		if(mInstType.equals("4")){//多次赎回协议  
			if(mIsNeedPur.equals("0")){//是否申购为  0  期初申购
				
			}
		}
		paramsMap.put(BocInvt.BOCINVT_AGRMODIFYVERIFY_AMOUNT_REQ,mEt_Amount.isShown() ? tv_amount.getText().toString() : mEt_AmountText);
		paramsMap.put(BocInvt.BOCINVT_AGRMODIFYVERIFY_MINAMOUNT_REQ,mEt_MinAmount.isShown() ? tv_minAmount.getText().toString() : mMinAmount);
		paramsMap.put(BocInvt.BOCINVT_AGRMODIFYVERIFY_MAXAMOUNT_REQ, mEt_MaxAmount.isShown() ? tv_maxAmount.getText().toString() : mMaxAmount);
		paramsMap.put(BocInvt.BOCINVT_AGRMODIFYVERIFY_NUIT_REQ, mUnit);
		paramsMap.put(BocInvt.BOCINVT_AGRMODIFYVERIFY_CHARCODE_REQ, mCharCode);
		paramsMap.put(BocInvt.BOCINVT_AGRMODIFYVERIFY_TOKEN_REQ, tokenId);
		if(mInstType.equals("4")){//多次赎回协议  
			if(mIsNeedPur.equals("0")){//是否申购为  0  期初申购
				paramsMap.put(BocInvt.BOCINVT_AGRMODIFYVERIFY_NUIT_REQ, "");
			}
			if(mIsNeedPur.equals("1")){
				paramsMap.put(BocInvt.BOCINVT_AGRMODIFYVERIFY_AMOUNT_REQ, "");
			}
		}
		biiRequestBody.setParams(paramsMap);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestPnsXpadInvestAgreementModifyVerifyCallBack");
	}
	/**
	 * 智能协议修改预交易接口返回 
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void requestPnsXpadInvestAgreementModifyVerifyCallBack(Object resultObj){
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> responseList = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = responseList.get(0);
		BiiHttpEngine.dissMissProgressDialog();
		agrModifyVerifyMap = (Map<String, Object>) biiResponseBody.getResult();
		if(StringUtil.isNullOrEmpty(agrModifyVerifyMap)) return;
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.BOCINVT_AGREEMENTMODIFYVERIFY_MAP, agrModifyVerifyMap);
//		setTitleAndContentValue(Integer.valueOf(mAgrType));
		setTitleAndContentValue(1);//智能投资
//		initConfirmAndResultView(Integer.valueOf(mAgrType));
		initConfirmAndResultView(1);//智能投资
	}

	/**智能投资协议 修改交易提交 接口调用**/
	public void requestPsnXpadInvestAgreementModifyCommit(String token){
		BiiRequestBody biiResRequestBody = new BiiRequestBody();
		BiiHttpEngine.showProgressDialog();
		biiResRequestBody.setMethod(BocInvt.BOCINVT_PSNXPADINVESTAGREEMENTMODIFYCOMMIT_API);
		Map<String,Object> paramsMap = new HashMap<String, Object>();
		biiResRequestBody.setConversationId(String.valueOf(BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID)));
		paramsMap.put(BocInvt.BOCINVT_AGRMODIFYCOMMIT_ACCOUNTKEY_REQ, mAccountKey);
		paramsMap.put(BocInvt.BOCINVT_AGRMODIFYCOMMIT_AGRCODE_REQ, mAgrCode);
		paramsMap.put(BocInvt.BOC_MODIFY_RES_TOKEN_REQ, token);
		biiResRequestBody.setParams(paramsMap);
		HttpManager.requestBii(biiResRequestBody, this, "requestPsnXpadInvestAgreementModifyCommitCallBack");
	}

	/**
	 * 智能投资协议 修改交易提交 接口回调  
	 * @param resultObj 服务端返回数据
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnXpadInvestAgreementModifyCommitCallBack(Object resultObj){
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> responseList = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = responseList.get(0);
		BiiHttpEngine.dissMissProgressDialog();
		agrModifyCommitMap = (Map<String, Object>) biiResponseBody.getResult();
		if(StringUtil.isNullOrEmpty(agrModifyCommitMap)) return ;
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.BOCINVT_AGREEMENTMODIFYCOMMIT_MAP, agrModifyCommitMap);
		initConfirmSuccessView();
	}
	
//	@Override
//	public void requestSystemDateTimeCallBack(Object resultObj) {
//		super.requestSystemDateTimeCallBack(resultObj);
//		BaseHttpEngine.dissMissProgressDialog();
//		if (!StringUtil.isNull(dateTime)) {
//			requestPsnXpadQueryRiskMatch();
//		}
//	}
	/**
	 * 请求查询客户风险等级与产品风险等级是否匹配
	 */
	public void requestPsnXpadQueryRiskMatch(){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.BOCINVT_PSNXPADQUERYRISKMATCH_API);
		Map<String,Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put(BocInvt.BOCINVT_MATCH_ACCOUNTKEY_REQ, mAccountKey);
		paramsMap.put(BocInvt.BOCINVT_MATCH_PRODUCTCODE_REQ, mProId);
		paramsMap.put(BocInvt.BOCINVT_MATCH_DIGITALCODE_REQ, "0");
//		paramsMap.put(BocInvt.BOCINVT_MATCH_SERIALCODE_REQ, mSerialCode);
		biiRequestBody.setParams(paramsMap);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnXpadQueryRiskMatchCallBack");
	}
	
	/**
	 * 请求查询客户风险等级与产品风险等级是否匹配 接口回调
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnXpadQueryRiskMatchCallBack(Object resultObj){
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		BiiHttpEngine.dissMissProgressDialog();
		Map<String, Object> riskMatchMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(riskMatchMap)) {
			return;
		}
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.BOCINVT_RISKMATCH_MAP, riskMatchMap);
		String riskMatch = (String) riskMatchMap
				.get(BocInvt.BOCINVT_MATCH_RISKMATCH_RES);
		if(riskMatch.equals("0")){//匹配
			requestCommConversationId();
			BiiHttpEngine.showProgressDialog();
		}else if(riskMatch.equals("1")){//不匹配但允许交易 （即客户风险低于产品风险，但系统不强制风险匹配，可提示客户确认风险和交易）
			BaseDroidApp
			.getInstanse()
			.showErrorDialog(
					InvestAgreementModifyActivity.this
							.getString(R.string.bocinvt_error_noriskExceed),
					R.string.cancle, R.string.confirm,
					new OnClickListener() {

						@Override
						public void onClick(View v) {

							switch (Integer.parseInt(v.getTag() + "")) {
							case CustomDialog.TAG_SURE://确认操作
								BaseDroidApp.getInstanse()
										.dismissErrorDialog();
								requestCommConversationId();
								BiiHttpEngine.showProgressDialog();
								break;
							case CustomDialog.TAG_CANCLE:// 取消操作
								BaseDroidApp.getInstanse()
										.dismissErrorDialog();
								break;
							}
						}
					});
		}else if(riskMatch.equals("2")){//不匹配且拒绝交易 （即客户风险低于产品风险，且系统强制风险匹配，拒绝交易）
			BaseDroidApp.getInstanse().showInfoMessageDialog(
					InvestAgreementModifyActivity.this
							.getString(R.string.bocinvt_error_cannotBuy));
			return;
		}
	}
	
	@Override
	/**
	 * 请求ConversationId 接口回调
	 */
	public void requestCommConversationIdCallBack(Object resultObj) {
		super.requestCommConversationIdCallBack(resultObj);
		BiiHttpEngine.dissMissProgressDialog();
		requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
	}
	
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		switch (temp) {
		case 1:
			tokenId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
			requestPsnXpadInvestAgreementModifyCommit(tokenId);
			break;
		case 2:
			tokenId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
			if(Integer.valueOf(mAgrType) == 1){//智能投资协议修改
				temp = 1;
				requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
						.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
//				requestPsnXpadInvestAgreementModifyCommit();
			}else if(Integer.valueOf(mAgrType) == 2){//定时定额 投资协议修改
				requestPsnXpadAutomaticAgreementMaintainResult("0","0",tokenId);
			}else if(Integer.valueOf(mAgrType) == 3 || Integer.valueOf(mAgrType) == 5){//周期滚续 投资协议修改
				if (StringUtil.isNull(tokenId)) {
					BaseHttpEngine.dissMissProgressDialog();
					return;
				}
				if(mInstType.equals("7")){//周期滚续表内
					requestPsnXpadAgreementModifyResult(tokenId);
				}else if(mInstType.equals("8")){//周期滚续业绩基准
					requestPsnXpadBenchmarkMaintainResult(tokenId,"1");
				}
			}else if(Integer.valueOf(mAgrType) == 4){//余额理财 投资协议修改
				requestPsnXpadAutomaticAgreementMaintainResult("1","0",tokenId);
			}
			break;
		case 3:
			tokenId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
			requestPsnXpadInvestAgreementModifyVerify(tokenId);
			break;
		default:
			tokenId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
			if(Integer.valueOf(mAgrType) == 1){//智能投资协议修改
				temp = 3;
				requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
						.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
			}else{
				setTitleAndContentValue(Integer.valueOf(mAgrType));
				initConfirmAndResultView(Integer.valueOf(mAgrType));
			}
//			if(Integer.valueOf(mAgrType) == 1){//智能投资协议修改
//				temp = 1;
//				requestPSNGetTokenId((String) BaseDroidApp.getInstanse()
//						.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
////				requestPsnXpadInvestAgreementModifyCommit();
//			}else if(Integer.valueOf(mAgrType) == 2){//定时定额 投资协议修改
//				requestPsnXpadAutomaticAgreementMaintainResult("0","0",tokenId);
//			}else if(Integer.valueOf(mAgrType) == 3){//周期滚续 投资协议修改
//				if (StringUtil.isNull(tokenId)) {
//					BaseHttpEngine.dissMissProgressDialog();
//					return;
//				}
//				if(mInstType.equals("7")){//周期滚续表内
//					requestPsnXpadAgreementModifyResult(tokenId);
//				}else if(mInstType.equals("8")){//周期滚续业绩基准
//					requestPsnXpadBenchmarkMaintainResult(tokenId,"1");
//				}
//			}else if(Integer.valueOf(mAgrType) == 4){//余额理财 投资协议修改
//				requestPsnXpadAutomaticAgreementMaintainResult("1","0",tokenId);
//			}
//			initConfirmView();
			break;
		}
			
	}
	/**
	 * 请求 协议维护 接口调用(余额理财协议修改)
	 * @param agreementType 投资方式
	 * @param maintainFlag 维护标示 
	 * @param tokenId 
	 */
	public void requestPsnXpadAutomaticAgreementMaintainResult(String agreementType,String maintainFlag,String tokenId){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody
				.setMethod(BocInvt.BOCINVT_PSNXPADAUTOMATICAGREEMENTMAINTAINRESULT_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> paramsmap = new HashMap<String, String>();
		//TODO 待确认项...
		paramsmap.put(BocInvt.BOC_AUTO_CONTRACTSEQ_REQ, mCustAgrCode);
		paramsmap.put(BocInvt.BOC_AUTO_SERIALCODE_REQ, mProId);
		paramsmap.put(BocInvt.BOC_AUTO_SERIALNAME_REQ, mProName);//产品名称
		paramsmap.put(BocInvt.BOC_AUTO_CURCODE_REQ, mproCur);
		paramsmap.put(BocInvt.BOC_AUTO_CASHREMIT_REQ, mCharCode.substring(1));
		paramsmap.put(BocInvt.BOC_AUTO_AGREEMENTTYPE_REQ, agreementType);//0 定时定额  ， 1自动投资（余额理财）
		paramsmap.put(BocInvt.BOC_AUTO_MAINTAINFLAG_REQ, maintainFlag);//0 修改，1暂停，2开始，3撤销
		paramsmap.put(BocInvt.BOC_AUTO_PERIODTYPE_REQ, mTradeCodeRes.equals("0") ? "1" : "0");//详情返回 0赎回 1购买 ；维护上送 0购买 1 赎回 
		if(agreementType.equals("0")){//定时定额 可修改期数
			paramsmap.put(BocInvt.BOC_AUTO_TOTALPERIOD_REQ,  mEtPeriod.getText().toString().trim().equals("") ? "-1" : mEtPeriod.getText().toString());
		}else if(agreementType.equals("1")){//余额理财 不可修改期数
			paramsmap.put(BocInvt.BOC_AUTO_TOTALPERIOD_REQ,tv_period.getText().toString().equals("不限期") ? "-1" : tv_period.getText().toString());
		}
		if(agreementType.equals("0") && mTradeCodeRes.equals("0") && amountFlag.equals("-1") && mEt_Amount.getText().toString().trim().equals("")){//定时定额 赎回 全额赎回 上送0
			paramsmap.put(BocInvt.BOC_AUTO_BASEAMOUNT_REQ, "0");
		}else{
			paramsmap.put(BocInvt.BOC_AUTO_BASEAMOUNT_REQ, mEt_Amount.getText().toString());
		}
		paramsmap.put(BocInvt.BOC_AUTO_PERIODSEQ_REQ, mPeriodAgeValue);//定投频率 传投资周期的数字
		paramsmap.put(BocInvt.BOC_AUTO_PERIODSEQTYPE_REQ, mPeriodAgeUnit);//定投频率类型  传投资周期的单位
		paramsmap.put(BocInvt.BOC_AUTO_LASTDATE_REQ, mFirstDateRed);
		paramsmap.put(BocInvt.BOC_AUTO_MINAMOUNT_REQ, mEt_MinAmount.getText().toString());
		paramsmap.put(BocInvt.BOC_AUTO_MAXAMOUNT_REQ, mEt_MaxAmount.getText().toString());
//		paramsmap.put(BocInvt.BOC_AUTO_MINAMOUNT_REQ, mEt_MaxAmount.getText().toString());
//		paramsmap.put(BocInvt.BOC_AUTO_MAXAMOUNT_REQ, mEt_MinAmount.getText().toString());
		paramsmap.put(BocInvt.BOC_AUTO_ACCOUNTKEY_REQ, mAccountKey);
		paramsmap.put(BocInvt.BOC_AUTO_TOKEN_REQ, tokenId);
		biiRequestBody.setParams(paramsmap);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnXpadAutomaticAgreementMaintainResultCallBack");
	}
	
	/** 请求协议维护（余额理财协议）回调 */
	@SuppressWarnings("unchecked")
	public void requestPsnXpadAutomaticAgreementMaintainResultCallBack(
			Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		BiiHttpEngine.dissMissProgressDialog();
		Map<String, Object> autoMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(autoMap)) {
			return;
		}
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.BOCINVT_AUTORESULT_MAP, autoMap);
		initConfirmSuccessView();
	}
	
	/**
	 * 修改周期性产品续约协议提交 
	 * @param tokenId
	 */
	public void requestPsnXpadAgreementModifyResult(String tokenId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody
				.setMethod(BocInvt.BOCINVT_PSNXPADAGREEMENTMODIFYRESULT_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String,Object> paramsmap = new HashMap<String, Object>();
		//TODO...
		paramsmap.put(BocInvt.BOC_MODIFY_RES_CONTRACTSEQ_REQ, mCustAgrCode);
		paramsmap.put(BocInvt.BOC_MODIFY_RES_TOTALPERIOD_REQ, mPeriodRes);
		paramsmap.put(BocInvt.BOC_MODIFY_RES_AMOUNTTYPECODE_REQ, mAmountTypeText.trim().equals("定额") ? "0" : "1");
		paramsmap.put(BocInvt.BOC_MODIFY_RES_SERIALNAME_REQ, mAgrName);
		paramsmap.put(BocInvt.BOC_MODIFY_RES_CURRENCYCODE_REQ, mproCur);
		paramsmap.put(BocInvt.BOC_MODIFY_RES_XPADCASHREMIT_REQ, mCharCode);
		paramsmap.put(BocInvt.BOC_MODIFY_RES_ADDAMOUNT_REQ, "0");
		paramsmap.put(BocInvt.BOC_MODIFY_RES_CONTAMTMODE_REQ, "0");
		paramsmap.put(BocInvt.BOC_MODIFY_RES_BASEAMOUNT_REQ, mAmountRes);
		paramsmap.put(BocInvt.BOC_MODIFY_RES_MINAMOUNT_REQ, mMinAmountRes);
		paramsmap.put(BocInvt.BOC_MODIFY_RES_MAXAMOUNT_REQ, mMaxAmountRes);
		paramsmap.put(BocInvt.BOC_MODIFY_RES_ACCOUNTKEY_REQ, mAccountKey);
		paramsmap.put(BocInvt.BOC_MODIFY_RES_TOKEN_REQ, tokenId);
		biiRequestBody.setParams(paramsmap);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnXpadAgreementModifyResultCallBack");
	}
	/**
	 * 修改周期性产品续约协议提交  接口回调
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnXpadAgreementModifyResultCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		BiiHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.BOCINVT_AGRMODIFYRESULT_MAP, resultMap);
		initConfirmSuccessView();
	}
	
	/**
	 * 业绩基准周期滚续产品更新
	 * @param TokenId
	 * @param opt 操作类型
	 */
	public void requestPsnXpadBenchmarkMaintainResult(String TokenId,String opt){
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(BocInvt.BOCINVT_PSNXPADBENCHMARKMAINTAINRESULT_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		Map<String,Object>paramsMap = new HashMap<String, Object>();
		paramsMap.put(BocInvt.BOCINVT_BENCHMARK_CUSTAGRCODE_REQ, mCustAgrCode);
		paramsMap.put(BocInvt.BOCINVT_BENCHMARK_ACCOUNTKEY_REQ, mAccountKey);
		paramsMap.put(BocInvt.BOCINVT_BENCHMARK_PRODUCTNAME_REQ, mAgrName);//暂定协议名称
		paramsMap.put(BocInvt.BOCINVT_BENCHMARK_CURRENCYCODE_REQ, mproCur);
		paramsMap.put(BocInvt.BOCINVT_BENCHMARK_CASHREMIT_REQ, mCharCode);
		paramsMap.put(BocInvt.BOCINVT_BENCHMARK_TATALPERIOD_REQ, mEtPeriod.getText().toString().trim());
		paramsMap.put(BocInvt.BOCINVT_BENCHMARK_AMOUNTTYPE_REQ, mAmountType);
		if(!StringUtil.isNullOrEmpty(mEt_AmountText)){
			mEt_AmountText = mEt_AmountText.replaceAll(",", "");
		}
		paramsMap.put(BocInvt.BOCINVT_BENCHMARK_BASEAMOUNT_REQ, mEt_AmountText);
		paramsMap.put(BocInvt.BOCINVT_BENCHMARK_OPT_REQ, opt);
		paramsMap.put(BocInvt.BOCINVT_BENCHMARK_TOKEN_REQ, TokenId);
		biiRequestBody.setParams(paramsMap);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestPsnXpadBenchmarkMaintainResultCallBack");
	}
	/**
	 *  业绩基准周期滚续产品更新 回调
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnXpadBenchmarkMaintainResultCallBack(Object resultObj){
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> responseList = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = responseList.get(0);
		BiiHttpEngine.dissMissProgressDialog();
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		if (StringUtil.isNullOrEmpty(resultMap)) {
			return;
		}
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.BOCINVT_AGRMODIFYRESULT_MAP, resultMap);
		initConfirmSuccessView();
	}
	
	/**
	 * 初始化修改成功页面
	 */
	private void initConfirmSuccessView(){
		mConfirmTitleAndContentLayout.setTitleVisibility(View.GONE);
		getBackgroundLayout().setLeftButtonText(null);
		mImaModifyfinish_ll = (LinearLayout)mConfirmTitleAndContentLayout.findViewById(R.id.modify_success_ima);
		mImaModifyfinish_ll.setVisibility(View.VISIBLE);
		mConfirmInfoTitle.setVisibility(View.GONE);
		mBtnModifyConfirm_ll.setVisibility(View.GONE);
		mBtnModifyfinish_ll.setVisibility(View.VISIBLE);
		mBtnFinish = (Button) findViewById(R.id.agrmodify_finish);
		mBtnFinish.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//数据是使用Intent返回
                Intent intent = new Intent(InvestAgreementModifyActivity.this,InvestInvalidAgreeQueryActivity.class);
//                //把返回数据存入Intent
//                intent.putExtra("mAmount", mAmountRes);
//                intent.putExtra("mMinAmount", mMinAmountRes);
//                intent.putExtra("mMaxAmount", mMaxAmountRes);
//                intent.putExtra("mBuyPeriod", mPeriodRes);
//				setResult(1001,intent);
                startActivity(intent);
                Intent intent2 = new Intent();
                setResult(RESULT_OK, intent2);
				InvestAgreementModifyActivity.this.finish();
				
//				String agrTypeReq = (String) mItemMap.get(BocInvt.BOCINVT_CAPACITYQUERY_AGRTYPE_RES);
//				String custAgrCodeReq = (String) mItemMap.get(BocInvt.BOCINVT_CAPACITYQUERY_CUSTAGECODE_RES);
//				InvestInvalidAgreeQueryActivity.getInstance().requestPsnXpadAgreementInfoQuery(custAgrCodeReq,agrTypeReq);
			}
		});	
	}

	/**
	 * 各协议类型  修改协议信息
	 * @param type
	 */
	private void setTitleAndContentValue(int type) {
		mModifyInfo_ll.setVisibility(View.GONE);
		mModifyConfirm_ll.setVisibility(View.VISIBLE);
		switch (type) {
		case 1://智能投资
			mPeriodRes = String.valueOf(agrModifyVerifyMap.get(BocInvt.BOCINVT_AGRMODIFYVERIFY_TOTALPERIOD_RES));
			break;
		case 2://定时定额
//			mPeriodRes = String.valueOf(agrModifyVerifyMap.get(BocInvt.BOCINVT_AGRMODIFYVERIFY_TOTALPERIOD_RES));
//			mAmountRes = StringUtil.parseStringCodePattern(mproCur,(String)agrModifyVerifyMap
//					.get(BocInvt.BOCINVT_AGRMODIFYVERIFY_AMOUNT_RES), 2);
			mPeriodRes = mEtPeriod.getText().toString();
			if(amountFlag.equals("-1") && mEt_Amount.getText().toString().trim().equals("")){
				mAmountRes = "0";
			}else{
				if(mTradeCodeRes.equals("0")){//交易方向 赎回
					mAmountRes = StringUtil.parseStringPattern(mEt_Amount.getText().toString(),2);
				}else{
					mAmountRes = StringUtil.parseStringCodePattern(mproCur,mEt_Amount.getText().toString(),2);
				}

			}
			break;
		case 3://周期滚续
		case 5://业绩基准周期滚续
			if(Integer.valueOf(mInstType) == 7){//表内产品
//				mAmountTypeText = mSp_AmountType.getSelectedItem().toString();
//				mPeriodRes = String.valueOf(agrModifyVerifyMap.get(BocInvt.BOCINVT_AGRMODIFYVERIFY_TOTALPERIOD_RES));
				mPeriodRes = mEtPeriod.getText().toString();
//				setTitleAndContentValue(4);
//				if(mSp_AmountType.getSelectedItemPosition() == 0){//定额
//					mAmountRes = StringUtil.parseStringCodePattern(mproCur,mEt_Amount.getText().toString(),2);
//				}else if(mSp_AmountType.getSelectedItemPosition() == 1){//不定额
//					mMinAmountRes = StringUtil.parseStringCodePattern(mproCur,mEt_MaxAmount.getText().toString(),2);
//					mMaxAmountRes = StringUtil.parseStringCodePattern(mproCur,mEt_MinAmount.getText().toString(),2);
//				}
				if(Integer.valueOf(mAmountType) == 0){//定额
					mAmountRes = StringUtil.parseStringCodePattern(mproCur,mEt_Amount.getText().toString(),2);
				}else if(Integer.valueOf(mAmountType) == 1){//不定额
					mMinAmountRes = StringUtil.parseStringCodePattern(mproCur,mEt_MinAmount.getText().toString(),2);
					mMaxAmountRes = StringUtil.parseStringCodePattern(mproCur,mEt_MaxAmount.getText().toString(),2);
				}
				
			}else if(Integer.valueOf(mInstType) == 8){//业绩基准 
//				mPeriodRes = String.valueOf(agrModifyVerifyMap.get(BocInvt.BOCINVT_AGRMODIFYVERIFY_TOTALPERIOD_RES));
				mPeriodRes = mEtPeriod.getText().toString();
			}
			break;
		case 4://余额理财
			if(Integer.valueOf(mAmountType) == 0){//定额
//				mAmountRes = StringUtil.parseStringCodePattern(mproCur,(String)agrModifyVerifyMap
//						.get(BocInvt.BOCINVT_AGRMODIFYVERIFY_AMOUNT_RES), 2);
				mAmountRes = StringUtil.parseStringCodePattern(mproCur,mEt_Amount.getText().toString(),2);
			}else if(Integer.valueOf(mAmountType) == 1){//不定额
//				mMinAmountRes = StringUtil.parseStringCodePattern(mproCur,(String)agrModifyVerifyMap
//						.get(BocInvt.BOCINVT_AGRMODIFYVERIFY_MINAMOUNT_RES), 2);
//				mMaxAmountRes = StringUtil.parseStringCodePattern(mproCur,(String)agrModifyVerifyMap
//						.get(BocInvt.BOCINVT_AGRMODIFYVERIFY_MAXAMOUNT_RES), 2);
				mMaxAmountRes = StringUtil.parseStringCodePattern(mproCur,mEt_MaxAmount.getText().toString(),2);
				mMinAmountRes = StringUtil.parseStringCodePattern(mproCur,mEt_MinAmount.getText().toString(),2);
			}
			break;
		}
	}
	/**
	 * 初始化修改确认、结果页面信息
	 */
	private void initConfirmAndResultView(int type) {
		mMyContainer.removeAllViews();
		if(mModifyConfirm_ll.isShown() && mBtnModifyConfirm_ll.isShown()){//确认页面
			mConfirmInfoTitle.setVisibility(View.VISIBLE);
		}
		mMyContainer.addView(createLabelTextView(R.string.boc_invest_amounttype,mAmountTypeText));
		//修改字段 页面显示 + "新xxx"
		switch (type) {
		case 1://智能投资  仅可修改期数
			if(Integer.valueOf(mInstType) == 4){//多次赎回协议  定额
				if(mIsNeedPur.equals("1")){//不申购
					mEt_AmountText = mUnit;
					mEt_AmountText = StringUtil.parseStringPattern(mEt_AmountText,2);
				}else{
					mEt_AmountText = mUnit;
					mEt_AmountText = StringUtil.parseStringPattern(mEt_AmountText,2);
				}
				mMyContainer.addView(createLabelTextView(R.string.boc_invest_amountexec_unit, mEt_AmountText));//协议赎回份额 固定两位小数显示
			}else{//周期连续、周期不连续、多次购买
				if(Integer.valueOf(mAmountType) == 0){
					mEt_AmountText = StringUtil.parseStringCodePattern(mproCur,mEt_AmountText,2);
					mMyContainer.addView(createLabelTextView(R.string.boc_invest_amountexec, mEt_AmountText));
				}else if(Integer.valueOf(mAmountType) == 1){
					mMinAmount = StringUtil.parseStringCodePattern(mproCur,mMinAmount,2);
					mMaxAmount = StringUtil.parseStringCodePattern(mproCur,mMaxAmount,2);
					mMyContainer.addView(createLabelTextView(R.string.bocinvt_tv_55, mMinAmount));
					mMyContainer.addView(createLabelTextView(R.string.bocinvt_tv_56, mMaxAmount));
				}
			}
			
			break;
		case 4://余额理财
			mPeriodRes = mBuyPeriod;
			if(Integer.valueOf(mAmountType) == 0){//定额 仅修改Amount
//				mEt_AmountText = StringUtil.parseStringCodePattern(mproCur,mEt_AmountText,2);
//				mAmountRes = StringUtil.parseStringCodePattern(mproCur,mEt_Amount.getText().toString(),2);
				mMyContainer.addView(createLabelTextView(mEt_AmountText.equals(mEt_Amount.getText().toString()) ? R.string.boc_invest_amountexec : R.string.boc_invest_amountexecnew, 
						StringUtil.parseStringCodePattern(mproCur,mEt_Amount.getText().toString(),2)));
//				mMinAmountRes = mMinAmount;
//				mMaxAmountRes = mMaxAmount;
			}else if(Integer.valueOf(mAmountType) == 1){//不定额   仅可修改 minAmount 和 maxAmount 
				mMinAmountRes = mEt_MinAmount.getText().toString();
				mMaxAmountRes = mEt_MaxAmount.getText().toString();
//				mMinAmountRes = StringUtil.parseStringCodePattern(mproCur,mEt_MinAmount.getText().toString(),2);
//				mMaxAmountRes = StringUtil.parseStringCodePattern(mproCur,mEt_MaxAmount.getText().toString(),2);
				mMyContainer.addView(createLabelTextView(mMinAmount.equals(mMinAmountRes) ? R.string.boc_invest_maxamount : R.string.boc_invest_maxamountnew, StringUtil.parseStringCodePattern(mproCur,mEt_MinAmount.getText().toString(),2)));
				mMyContainer.addView(createLabelTextView(mMaxAmount.equals(mMaxAmountRes) ? R.string.boc_invest_minamount : R.string.boc_invest_minamountnew, StringUtil.parseStringCodePattern(mproCur,mEt_MaxAmount.getText().toString(),2)));
			}
			break;
		case 2://定时定额
//			mEt_AmountText = StringUtil.parseStringCodePattern(mproCur,mEt_AmountText,2);
			if(mTradeCodeRes.equals("0")){//交易方向 赎回
				if(amountFlag.equals("-1") && mAmountRes.equals("0")){
					mMyContainer.addView(createLabelTextView(((mEt_AmountText.equals("-1.00") || mEt_AmountText.equals("-1")) && mEt_Amount.getText().toString().trim().equals("")) ? R.string.boc_invest_tzfe : R.string.boc_invest_tzfenew,"全额赎回"));
				}else{
					if(!mEt_AmountText.equals("-")){
//						mEt_AmountText = StringUtil.parseStringCodePattern(mproCur,mEt_AmountText,2);
//						mAmountRes = StringUtil.parseStringCodePattern(mproCur,mAmountRes,2);
						mAmountRes = StringUtil.parseStringPattern(mAmountRes,2);//协议赎回份额 固定两位小数显示
					}
					mMyContainer.addView(createLabelTextView(mEt_AmountText.equals(mEt_Amount.getText().toString()) ? R.string.boc_invest_tzfe : R.string.boc_invest_tzfenew, mAmountRes));
				}
			}else if(mTradeCodeRes.equals("1")){//交易方向 购买
				mEt_AmountText = StringUtil.parseStringCodePattern(mproCur,mEt_AmountText,2);
				mMyContainer.addView(createLabelTextView(mEt_AmountText.equals(mEt_Amount.getText().toString()) ? R.string.boc_invest_amountexec_two : R.string.boc_invest_amountexecnew_two, mAmountRes));
			}
			break;
		case 3://周期滚续  
		case 5://业绩基准周期滚续
			if(Integer.valueOf(mInstType) == 7){//表内周期滚续  
				/**根据修改后的金额模式动态显示修改的金额**/
//				if(mSp_AmountType.getSelectedItemPosition() == 0){//定额
				/** 修改需求 周期滚续不可修改金额模式 **/
				if(Integer.valueOf(mAmountType) == 0){//定额	
//					mAmountRes = StringUtil.parseStringCodePattern(mproCur,mEt_Amount.getText().toString(),2);
//					mEt_AmountText = StringUtil.parseStringCodePattern(mproCur,mEt_AmountText,2);
					mMyContainer.addView(createLabelTextView(mEt_AmountText.equals(mEt_Amount.getText().toString()) ? R.string.boc_invest_amountexec : R.string.boc_invest_amountexecnew,
							StringUtil.parseStringCodePattern(mproCur,mEt_Amount.getText().toString(),2)));
//				}else if(mSp_AmountType.getSelectedItemPosition() == 1){//不定额
				}else if(Integer.valueOf(mAmountType) == 1){//不定额	
					mMinAmountRes = StringUtil.parseStringCodePattern(mproCur,mEt_MinAmount.getText().toString(),2);
					mMaxAmountRes = StringUtil.parseStringCodePattern(mproCur,mEt_MaxAmount.getText().toString(),2);
					mMyContainer.addView(createLabelTextView(mMinAmount.equals(mEt_MinAmount.getText().toString()) ? R.string.minAmount : R.string.minAmountnew, mMinAmountRes));
					mMyContainer.addView(createLabelTextView(mMaxAmount.equals(mEt_MaxAmount.getText().toString()) ? R.string.maxAmount : R.string.maxAmountnew, mMaxAmountRes));
				}
			}else if(Integer.valueOf(mInstType) == 8){//业绩基准周期滚续  根据接口返回的金额模式 显示对应的金额  仅修改期数
				if(Integer.valueOf(mAmountType) == 0){
					mEt_AmountText = StringUtil.parseStringCodePattern(mproCur,mEt_AmountText,2);
					mMyContainer.addView(createLabelTextView(R.string.boc_invest_amountexec, mEt_AmountText));
				}else if(Integer.valueOf(mAmountType) == 1){
					mMinAmount = StringUtil.parseStringCodePattern(mproCur,mMinAmount,2);
					mMaxAmount = StringUtil.parseStringCodePattern(mproCur,mMaxAmount,2);
					mMyContainer.addView(createLabelTextView(R.string.minAmount, mMinAmount));
					mMyContainer.addView(createLabelTextView(R.string.maxAmount, mMaxAmount));
				}
			}
			
			break;
		}
		if(mPeriodRes.trim().equals("") || mPeriodRes.trim().equals("-1") || mPeriodRes.trim().equals("-000001") || mPeriodRes.trim().equals("不限期")){//不限期
			mMyContainer.addView(createLabelTextView(R.string.boc_invest_buyperiod,"不限期"));
		}else{//有限期
			mMyContainer.addView(createLabelTextView(mBuyPeriod.equals(mPeriodRes) ? R.string.boc_invest_buyperiod : R.string.boc_invest_buyperiodnew, mPeriodRes));
		}
		mMyContainer.addView(createLabelTextView(R.string.boc_invest_finishperiod, mFinishperiod));
//		mMyContainer.addView(createLabelTextView(R.string.boc_invest_remaindperiod, mRemaindperiod));//剩余期数
		/**智能投资协议 多次赎回协议 协议申购金额 动态显示 控制**/
		if(mAgrType.equals("1") && mInstType.equals("4") && mIsNeedPur.equals("0")){//多次赎回协议   是否申购为  0  期初申购
			mEt_AmountText = StringUtil.parseStringCodePattern(mproCur,String.valueOf(mAgrInfoMap.get(BocInvt.BOCINVT_AGRINFOQUERY_AMOUNT_RES)),2);
			mMyContainer.addView(createLabelTextView(R.string.boc_invest_agrsgamount, mEt_AmountText));
			}
		}
	

	/**
	 * 创建LabelTextView控件 
	 * @param resid
	 * @param valueText
	 * @return
	 */
	private LabelTextView createLabelTextView(int resid, String valueText,Object... obj){
		LabelTextView v = new LabelTextView(this);
		v.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		v.setLabelText(resid);
		v.setValueText(valueText);
		if(obj != null && obj.length > 0){
			if(obj[0] instanceof String){//设置控件weight
				v.setWeightShowRate((String)obj[0]);
			}
			if(obj[0] instanceof TextColor){//设置字体颜色
				v.setValueTextColor((TextColor)obj[0]);
			}
		}
		return v;
	}

	/**返回键处理事件**/
	OnClickListener backClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			backClick();
		}
	};

	@Override
	public void onBackPressed() {
		backClick();
	}
	/**
	 * 返回事件的处理
	 */
	public void backClick() {
//		if(mModifyConfirm_ll.isShown() && mBtnModifyfinish_ll.isShown()){
//			//数据是使用Intent返回
//            Intent intent = new Intent();
//            //把返回数据存入Intent
//            intent.putExtra("mAmount", mAmountRes);
//            intent.putExtra("mMinAmount", mMinAmountRes);
//            intent.putExtra("mMaxAmount", mMaxAmountRes);
//            intent.putExtra("mBuyPeriod", mPeriodRes);
//			setResult(1001,intent);
//		}
		if(mModifyInfo_ll.isShown() ||(mModifyConfirm_ll.isShown() && mBtnModifyfinish_ll.isShown())){//修改信息填写 和 修改完成 页面 
			InvestAgreementModifyActivity.this.finish();
		}else if(mModifyConfirm_ll.isShown() && mBtnModifyConfirm_ll.isShown()){//修改信息 确认页面
			mModifyConfirm_ll.setVisibility(View.GONE);
			mModifyInfo_ll.setVisibility(View.VISIBLE);
			temp = 0 ;
		}
	};

}
