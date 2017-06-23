package com.chinamworld.bocmbci.biz.tran;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.atmremit.AtmRemitChooseActivity;
import com.chinamworld.bocmbci.biz.tran.atmremit.AtmThirdMenu;
import com.chinamworld.bocmbci.biz.tran.collect.CollectMainActivity;
import com.chinamworld.bocmbci.biz.tran.ecard.TransferEcardActivity;
import com.chinamworld.bocmbci.biz.tran.managetrans.ManageTransActivity;
import com.chinamworld.bocmbci.biz.tran.mobiletrans.MobileTranConfirmActivity;
import com.chinamworld.bocmbci.biz.tran.mobiletrans.MobileTransThirdMenu;
import com.chinamworld.bocmbci.biz.tran.mytransfer.creditcardrepay.RelCreditCardRemitConfirmActivity1;
import com.chinamworld.bocmbci.biz.tran.mytransfer.creditcardrepay.RelSelfCreditCardConfirmActivity1;
import com.chinamworld.bocmbci.biz.tran.mytransfer.currenttran.ConfirmInfoActivity1;
import com.chinamworld.bocmbci.biz.tran.mytransfer.currenttran.NoRelBankOtherConfirmInfoActivity1;
import com.chinamworld.bocmbci.biz.tran.mytransfer.currenttran.NoRelConfirmInfoActivity1;
import com.chinamworld.bocmbci.biz.tran.mytransfer.currenttran.RelConfirmInfoActivity1;
import com.chinamworld.bocmbci.biz.tran.mytransfer.currenttran.TransferManagerActivity1;
import com.chinamworld.bocmbci.biz.tran.remit.RemitThirdMenu;
import com.chinamworld.bocmbci.biz.tran.twodimentrans.TwoDimenSacnResultActivity;
import com.chinamworld.bocmbci.biz.tran.twodimentrans.TwoDimenTransActivity1;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.Dictionary;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.entity.ImageAndText;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.CaptureActivity;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.source.RGBLuminanceSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
/**
 * 我要转账基类，初始化左边二级菜单、底部菜单栏和头部按钮，处理通信
 * 
 * @author
 * 
 */
public class TranBaseActivity extends BaseActivity implements OnClickListener {

	private final static String TAG = TranBaseActivity.class.getSimpleName();

	public LayoutInflater mInflater;
	/** 加载布局 */
	public LinearLayout tabcontent = null;
	/** 头部右边按钮 */
	protected Button mTopRightBtn = null;
	/** 头部返回按钮 */
	protected Button back;
	/** 主界面按钮 */
	// protected Button mainBtn;

	/** PsnGetSecurityFactor获得安全因子 根据操作员与服务码查询 安全因子Id */
	// private String combinId = "";

	// modify by wjp
	/** 关联账户转账 */
	protected static final int TRANTYPE_REL_ACCOUNT = 1;
	/** 本人关联信用卡还款 */
	protected static final int TRANTYPE_REL_CRCD_REPAY = 2;
	/** 信用卡购汇还款 */
	protected static final int TRANTYPE_REL_CRCD_BUY = 3;
	/** 行内 */
	protected static final int TRANTYPE_NOREL_BANKIN = 4;
	/** 跨行 */
	protected static final int TRANTYPE_NOREL_BANKOTHER = 5;
	/** 非关联信用卡 */
	protected static final int TRANTYPE_NOREL_CRCD = 6;
	/** 手机号转账 */
	protected static final int TRANTYPE_MOBILE_TRAN = 7;
	/** 行内定向转账 */
	protected static final int TRANTYPE_DIR_BANKIN = 8;
	/** 跨行定向转账 */
	protected static final int TRANTYPE_DIR_BANKOTHER = 9;
	/** 非关联信用卡定向转账 */
	protected static final int TRANTYPE_DIR_CRCD = 10;
	/** 跨行实时转账 */
	protected static final int TRANTYPE_SHISHI_BANKOTHER = 11;
	/** 跨行实时定向付款 */
	protected static final int TRANTYPE_SHISHI_DIR_BANKOTHER = 12;
	/** 专属理财账户 */
	protected static final int TRANTYPE_BOC_ACCOUNT = 13;
	protected static final String TRANS_TYPE = "tranType";

	// 二级菜单标识
	protected int secondMenuFlag = 0;
	protected static final int MY_TRAN = 1;
	protected static final int MOBILE_TRAN = 2;
	protected static final int TWO_DIMEN_TRAN = 3;
	protected static final int MANAGE_TRAN = 4;
	protected static final String SENCOND_MENU_KEY = "sencondMenu";

	/** 转账方式 */
	protected int tranTypeFlag;
	/** 区分查询开户行网点通讯activity 是哪个activity调用查询开户行网点的 */
	protected int psnVestOrgFlag = 1;// 默认是从新增收款人过来
	protected static final int VEST_ADD_NEW_PAYEE = 1;
	protected static final int VEST_TWO_DIMEN_SCAN = 2;
	/** T43从收款人管理进入的新增收款人 */
	protected static final int VEST_ADD_PAYEE = 3;
	protected static final String OPENCHANGEBOOKING = "O";
	protected static final String ATMSERVICEID = "PB045";
	/** 可以撤销的状态 */
	protected static final String CANCANCELSTATUS = "OU";
	protected static final String CANCANCELSTATUS1 = "CR";
	protected static final String CANCANCELSTATUS2 = "L3";
	protected static final String CANCANCELSTATUS3 = "L6";
	/** 查询 */
	protected static final String FREEREMITTRSTYPEQU = "QU";
	// P403 普通账户详情列表信息
	public List<Map<String, Object>> accountDetailList;
	// P403 信用卡详情列表信息
	public List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();

	// T43 标记是否有所属银行和开户行
	// 是否有所属银行
	public static final String ISHAVEBANKNAME = "ishaveBankNameClient";
	// 是否有开户行
	public static final String ISHAVEADDRESS = "ishaveAddressClient";
	public static final String ISFROMMANAGE = "isFromManage";
	// 是否为加急转账
	public static final String ISSHISHITYPE = "isshishitrantype";
	// 是否修改收款人
	public static final String ISMODIFYPAYEE = "isModifyPayee";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biz_activity_layout);
		initPulldownBtn(); // 加载上边下拉菜单
		initFootMenu(); // 加载底部菜单栏
		initLeftSideList(this, LocalData.tranManagerLeftList); // 加载左边菜单栏
		tabcontent = (LinearLayout) this.findViewById(R.id.sliding_body);
		mInflater = LayoutInflater.from(this);
		// 主界面按钮
		// mainBtn = (Button) findViewById(R.id.ib_top_right_btn);
		// 头部左边返回按钮
		back = (Button) findViewById(R.id.ib_back);
		back.setOnClickListener(this);
		// 显示头部右边按钮，发起新转账
		mTopRightBtn = (Button) findViewById(R.id.ib_top_right_btn);
		mTopRightBtn.setText(this.getString(R.string.new_myTrans));
		mTopRightBtn.setOnClickListener(this);
	}

	public void toprightBtn() {
		mTopRightBtn.setText(this.getString(R.string.go_main));
		mTopRightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), MainActivity.class);
//				BaseDroidApp.getInstanse().getCurrentAct().startActivity(intent);
				goToMainActivity();
			}
		});
	}

	public void toprightBtnAtm() {
		mTopRightBtn.setText(this.getString(R.string.tran_new_atm_remit));
		mTopRightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(), AtmRemitChooseActivity.class);
				BaseDroidApp.getInstanse().getCurrentAct().startActivity(intent);
			}
		});
	}

	/**
	 * 在slidingbody中引入自己布局文件
	 * 
	 * @param resource
	 * @return 引入布局
	 */
	public View addView(int resource) {
		View view = LayoutInflater.from(this).inflate(resource, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		return view;
	}
	@Override
	protected void onResume() {
		super.onResume();
		hineLeftSideMenu();
	}
	/**
	 * 头部左侧和右侧按钮
	 * 
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:
			finish();
			break;
		case R.id.ib_top_right_btn:// 头部右边按钮，发起新转账
			// 用户重新发起新转账，关闭所有活动的Activity，除了MainActivity
			ActivityTaskManager.getInstance().removeAllActivity();
			Intent intent = new Intent();
			intent.setClass(TranBaseActivity.this, TransferManagerActivity1.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	
	/**
	 * 左边菜单栏
	 */
	@Override
	protected boolean selectedMenuItemHandler(Activity context, ImageAndText menuItem) {
		super.selectedMenuItemHandler(context, menuItem);
//		Class<?> clz = null;
//		ImageAndText iat = LocalData.tranManagerLeftList.get(clickIndex);
//		try {
//			clz = Class.forName(iat.getClassName());
//			if(!BaseDroidApp.getInstanse().getCurrentAct().getClass().getSimpleName().equals(clz.getSimpleName())) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent(this, clz);
//				this.startActivity(intent);
//			}
//		} catch (ClassNotFoundException e) {
//			LogGloble.exceptionPrint(e);
//		}
		String menuId = menuItem.MenuID;
		if(menuId.equals("tranManager_1") ){
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof TransferManagerActivity1)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent0 = new Intent();
				intent0.setClass(this, TransferManagerActivity1.class);
				context.startActivity(intent0);
			}
		}
		else if(menuId.equals("tranManager_2")){
			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof MobileTransThirdMenu)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent1 = new Intent();
				intent1.setClass(this, MobileTransThirdMenu.class);
				context.startActivity(intent1);
			}
		}
       else if(menuId.equals("tranManager_3")){
    	   if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof TwoDimenTransActivity1)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent2 = new Intent();
				intent2.setClass(this, TwoDimenTransActivity1.class);
				context.startActivity(intent2);
			}
		}
       else if(menuId.equals("tranManager_4")){
    	   if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof ManageTransActivity)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent3 = new Intent();
				intent3.setClass(this, ManageTransActivity.class);
				context.startActivity(intent3);
			}
		}
       else if(menuId.equals("tranManager_5")){
    	   if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof AtmThirdMenu)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent4 = new Intent();
				intent4.setClass(this, AtmThirdMenu.class);
				context.startActivity(intent4);
			}
		}
       else if(menuId.equals("tranManager_6")){
    	   if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof RemitThirdMenu)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent5 = new Intent();
				intent5.setClass(this, RemitThirdMenu.class);
				context.startActivity(intent5);
			}
		}
       else if(menuId.equals("tranManager_7")){
    		if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof CollectMainActivity)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent6 = new Intent();
				intent6.setClass(this, CollectMainActivity.class);
				context.startActivity(intent6);
			}
		}
       else if(menuId.equals("tranManager_8")){
    		if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof TransferEcardActivity)) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent7 = new Intent();
				intent7.setClass(this, TransferEcardActivity.class);
				context.startActivity(intent7);
			}
		}
     
		return true;
		

//		switch (clickIndex) {
//		case ConstantGloble.MYTRANS:
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof TransferManagerActivity1)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent0 = new Intent();
//				intent0.setClass(TranBaseActivity.this, TransferManagerActivity1.class);
//				startActivity(intent0);
//			}
//			break;
//		case ConstantGloble.MOBILETRANS:
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof MobileTransThirdMenu)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent1 = new Intent();
//				intent1.setClass(TranBaseActivity.this, MobileTransThirdMenu.class);
//				startActivity(intent1);
//			}
//
//			break;
//		case ConstantGloble.TWODIMENTRANSA:
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof TwoDimenTransActivity1)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent2 = new Intent();
//				intent2.setClass(TranBaseActivity.this, TwoDimenTransActivity1.class);
//				startActivity(intent2);
//			}
//			break;
//		case ConstantGloble.MANAGETRANS:
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof ManageTransActivity)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent3 = new Intent();
//				intent3.setClass(TranBaseActivity.this, ManageTransActivity.class);
//				startActivity(intent3);
//			}
//
//			break;
//		case 4:
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof AtmThirdMenu)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent4 = new Intent();
//				intent4.setClass(TranBaseActivity.this, AtmThirdMenu.class);
//				startActivity(intent4);
//			}
//			break;
//		case 5:
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof RemitThirdMenu)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent5 = new Intent();
//				intent5.setClass(TranBaseActivity.this, RemitThirdMenu.class);
//				startActivity(intent5);
//			}
//			break;
//		case 6:
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof CollectMainActivity)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent6 = new Intent();
//				intent6.setClass(TranBaseActivity.this, CollectMainActivity.class);
//				startActivity(intent6);
//			}
//			break;
//			
//		case 7:
//			if (!(BaseDroidApp.getInstanse().getCurrentAct() instanceof TransferEcardActivity)) {
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent7 = new Intent();
//				intent7.setClass(TranBaseActivity.this, TransferEcardActivity.class);
//				startActivity(intent7);
//			}
//			break;
//		default:
//			break;
//		}

	}

	/**
	 * 手续费试算异常拦截 本方法有待改进
	 */
	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		if (resultObj instanceof BiiResponse) {
			BiiResponse biiResponse = (BiiResponse) resultObj;
			List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
			BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
			if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码
				// 手续费试算分为 1.关联账户转账手续费试算 2.行内转账手续费试算 3.跨行手续费试算
				// 行内和关联账户手续费试算是同一个接口
				// 跨行是另一个接口
				if (Tran.TRANSFER_COMMISSIONCHARGE_API.equals(biiResponseBody.getMethod())) {// 行内手续费试算
					if (biiResponse.isBiiexception()) {// 代表返回数据异常
						BiiHttpEngine.dissMissProgressDialog();
						BiiError biiError = biiResponseBody.getError();
						// 判断是否存在error
						if (biiError != null) {
							if (biiError.getCode() != null) {
								Map<String, Object> chargeMissionMap = TranDataCenter.getInstance()
										.getCommissionChargeMap();
								if (!StringUtil.isNullOrEmpty(chargeMissionMap)) {
									chargeMissionMap.clear();
								}
								if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
									// 要重新登录
									showTimeOutDialog(biiError.getMessage());
								} else {// 非会话超时错误拦截
									if (tranTypeFlag == TRANTYPE_REL_ACCOUNT) {// 关联账户转账
										BaseHttpEngine.dissMissProgressDialog();
										Intent intent0 = new Intent(BaseDroidApp.getInstanse().getCurrentAct(),
												RelConfirmInfoActivity1.class);
										startActivity(intent0);
									} else if (tranTypeFlag == TRANTYPE_NOREL_BANKIN || tranTypeFlag == TRANTYPE_DIR_BANKIN) {// 行内
	
										Intent nextIntent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(),
												NoRelConfirmInfoActivity1.class);
										nextIntent.putExtra(TRANS_TYPE, tranTypeFlag);
										startActivity(nextIntent);
									} else if (tranTypeFlag == TRANTYPE_NOREL_CRCD || tranTypeFlag == TRANTYPE_DIR_CRCD) {// 非关联信用卡
										Intent intent = new Intent();
										intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
												ConfirmInfoActivity1.class);
										intent.putExtra(TRANS_TYPE, tranTypeFlag);
										startActivity(intent);
									} else if (tranTypeFlag == TRANTYPE_MOBILE_TRAN) {// 手机号转账
										Intent intent = new Intent();
										intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
												MobileTranConfirmActivity.class);
										startActivity(intent);
									}else if(tranTypeFlag == TRANTYPE_REL_CRCD_REPAY){// 信用卡还款
										Intent intent = new Intent();
										intent.putExtra("crcdflag", true);
										intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
												RelSelfCreditCardConfirmActivity1.class);
										startActivity(intent);
										
									}
								}
							}
						}
						return true;
					}
					return false;// 没有异常
				} else if (Tran.PSNTRANS_GETNATIONAL_TRANSFER_COMMISSIONCHARGE.equals(biiResponseBody.getMethod())) {// 跨行手续费试算
					// 代表返回数据异常
					// 行内手续费试算
					if (biiResponse.isBiiexception()) {// 代表返回数据异常
						BiiHttpEngine.dissMissProgressDialog();
						BiiError biiError = biiResponseBody.getError();
						// 判断是否存在error
						if (biiError != null) {
							if (biiError.getCode() != null) {
								Map<String, Object> chargeMissionMap = TranDataCenter.getInstance()
										.getCommissionChargeMap();
								if (!StringUtil.isNullOrEmpty(chargeMissionMap)) {
									chargeMissionMap.clear();
								}
								if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
									// 要重新登录
									showTimeOutDialog(biiError.getMessage());
								} else {// 非会话超时错误拦截
									Intent intent = new Intent();
									intent.setClass(BaseDroidApp.getInstanse().getCurrentAct(),
											NoRelBankOtherConfirmInfoActivity1.class);
									intent.putExtra(TRANS_TYPE, tranTypeFlag);
									startActivity(intent);
								}
							}
						}
						return true;
					}
					return false;// 没有异常
				} else if (Tran.PSNQUERY_ACCOUNT_VESTORG.equals(biiResponseBody.getMethod())) {// 查询开户行网店
					// 代表返回数据异常
					if (biiResponse.isBiiexception()) {// 代表返回数据异常
						BiiHttpEngine.dissMissProgressDialog();
						BiiError biiError = biiResponseBody.getError();
						// 判断是否存在error
						if (biiError != null) {
							if (biiError.getCode() != null) {
								if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
									// 要重新登录
									showTimeOutDialog(biiError.getMessage());
								} else if (biiError.getCode().equals("not.support.yet")) {// 非会话超时错误拦截
									if (psnVestOrgFlag == VEST_ADD_NEW_PAYEE) {
										setResult(102);// 行内转账结果码
										finish();
									} else if (psnVestOrgFlag == VEST_TWO_DIMEN_SCAN) {
										Intent intent = getIntent();
										intent.setClass(this, TwoDimenSacnResultActivity.class);
										startActivity(intent);
									}
								} else {
									return super.httpRequestCallBackPre(resultObj);
								}
							}
						}
						// }
						return true;
					}
					return false;// 没有异常
	
				}else if(Crcd.CRCD_PSNCRCDFOREIGNOFFFEE.equals(biiResponseBody.getMethod())){
					if (biiResponse.isBiiexception()) {// 代表返回数据异常
						BiiHttpEngine.dissMissProgressDialog();
						BiiError biiError = biiResponseBody.getError();
						// 判断是否存在error
						if (biiError != null) {
							if (biiError.getCode() != null) {
								Map<String, Object> chargeMissionMap = TranDataCenter.getInstance()
										.getCommissionChargeMap();
								if (!StringUtil.isNullOrEmpty(chargeMissionMap)) {
									chargeMissionMap.clear();
								}
								if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
									// 要重新登录
									showTimeOutDialog(biiError.getMessage());
								} else {// 非会话超时错误拦截
									
										Intent intent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(),
												RelCreditCardRemitConfirmActivity1.class);
										startActivity(intent);	
									
								}
							}
						}
					}
				} 
				else {
					return super.httpRequestCallBackPre(resultObj);
				}
			}
			// 随机数获取异常
		}
		return super.httpRequestCallBackPre(resultObj);
	}

//	public void showTimeOutDialog(BiiError biiError) {
//		BaseDroidApp.getInstanse().showMessageDialog(biiError.getMessage(), new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				BaseDroidApp.getInstanse().dismissErrorDialog();
//				ActivityTaskManager.getInstance().removeAllActivity();
//				Intent intent = new Intent();
//				intent.setClass(TranBaseActivity.this, LoginActivity.class);
//				startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
//			}
//		});
//	}

//	/**
//	 * 隐藏左侧菜单栏
//	 */
//	public void goneLeftView() {
//		// 隐藏左侧菜单
//		LinearLayout slidingTab = (LinearLayout) findViewById(R.id.sliding_tab);
//		Button btn_show = (Button) findViewById(R.id.btn_show);
//		Button btn_hide = (Button) findViewById(R.id.btn_hide);
//		Button btn_fill_show = (Button) findViewById(R.id.btn_fill_show);
//		slidingTab.setVisibility(View.GONE);
//		btn_show.setVisibility(View.GONE);
//		setLeftButtonPopupGone();
//		btn_hide.setVisibility(View.GONE);
//		btn_fill_show.setVisibility(View.GONE);
//	}

	/** ATM汇款状态 */
	public static final Map<String, Object> AtmStatusMap = new HashMap<String, Object>() {
		{
			put("OU", "已预约未取现");
			put("CR", "已预约未取现");
			put("OK", "已成功取现");
			put("CL", "已撤销预约");
			put("L3", "密码错3次锁定");
			put("L6", "永久锁定");
		}
	};

	/**
	 * 改变输入
	 * 
	 * @param currency
	 */
	public void checkCurrency(String currency, EditText et) {
		if (!StringUtil.isNull(currency)) {
			if (japList.contains(currency)) {
				// et.setInputType(InputType.TYPE_CLASS_NUMBER);
			}
		}

	}

	/**
	 * 是否是日元
	 * 
	 * @param currency
	 * @return
	 */
	public boolean checkJap(String currency) {
		if (!StringUtil.isNull(currency)) {
			if (japList.contains(currency)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 日元校验
	 * 
	 * @param currency
	 *            币种
	 * @param text1
	 *            校验一
	 * @param text2
	 *            校验二
	 * @return 校验
	 */
	public RegexpBean checkJapReg(String currency, String text1, String text2) {
		RegexpBean rebnew = null;
		if (checkJap(currency)) {
			rebnew = new RegexpBean(text1, text2, "spetialAmount");
		} else {
			rebnew = new RegexpBean(text1, text2, "amount");
		}
		return rebnew;
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

	/** 日元 ,韩元 币种 */
	public static final List<String> japandckList = new ArrayList<String>() {
		{
			add("027");
			add("JPY");
			add("088");
			add("KRW");
		
		}
	};
	
	/** 是否 日元 ,韩元 币种 */
	public boolean checkJaporCk(String currency) {
		if (!StringUtil.isNull(currency)) {
			if (japandckList.contains(currency)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 日元，韩元 校验
	 * 
	 * @param currency
	 *            币种
	 * @param text1
	 *            校验一
	 * @param text2
	 *            校验二
	 * @return 校验
	 */
	public RegexpBean checkJapCkReg(String currency, String text1, String text2) {
		RegexpBean rebnew = null;
		if (checkJap(currency)) {
			rebnew = new RegexpBean(text1, text2, "spetialAmountJPCK");
		} else {
			rebnew = new RegexpBean(text1, text2, "amount");
		}
		return rebnew;
	}
	
	// 请求
	/** 402 请求账户列表信息 */
	/** 请求所有汇款笔数套餐卡账户列表信息 */
	public void requestRemitAccBankAccountList() {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.QRY_ACC_LIST_API);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		List<String> paramslist = new ArrayList<String>();
		// 传递能作为转出账户的列表
		paramslist.add(ConstantGloble.ACC_TYPE_BRO);
		paramslist.add(ConstantGloble.ACC_TYPE_ORD);
		paramslist.add(ConstantGloble.ACC_TYPE_RAN);
		paramsmap.put(Acc.ACC_ACCOUNTTYPE_REQ, paramslist);
		biiRequestBody.setParams(paramsmap);
		// 通讯开始,展示通讯框
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestRemitAccBankAccountListCallBack");
	}

	/**
	 * 请求所有汇款笔数套餐卡账户列表信息回调
	 * 
	 * @param resultObj
	 */
	public void requestRemitAccBankAccountListCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		List<Map<String, Object>> bankAccountList = (List<Map<String, Object>>) (biiResponseBody.getResult());
		BaseHttpEngine.dissMissProgressDialog();
		if (bankAccountList == null || bankAccountList.size() == 0) {
			BaseDroidApp.getInstanse().showMessageDialog(this.getString(R.string.trans_no_remit_choose),
					new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					BaseDroidApp.getInstanse().dismissErrorDialog();
				}
			});
			return;
		}
		TranDataCenter.getInstance().setAccountList(bankAccountList);
	}

	// 403请求余额
	/**
	 * 根据币种和钞汇标志找到对应的余额
	 * 
	 * @param tran_success_out_balance
	 *            转出账户余额的LinearLayout
	 * @param tv_tran_success_out_balance
	 *            转出账户余额字段的TextView
	 * @param accountDetailList
	 *            请求回来的账户余额数据
	 * @param currency
	 *            转出币种
	 * @param cashRemit
	 *            转出钞汇
	 */
	public void refreshTranOutBalance(boolean isCrcd, LinearLayout tran_success_out_balance,
			TextView tv_tran_success_out_balance, List<Map<String, Object>> accountDetailList, String currency,
			String cashRemit) {
		if (isCrcd) {
			Map<String, Object> detailMap = new HashMap<String, Object>();
			detailMap = accountDetailList.get(0);
			final String currentBalance = (String) detailMap.get(Crcd.CRCD_LOANBALANCELIMIT);
			String currentflag = (String) detailMap.get(Crcd.CRCD_CURRENTBALANCEFLAG);
			tran_success_out_balance.setVisibility(View.VISIBLE);
			if (StringUtil.isNull(currentflag)) {
				tv_tran_success_out_balance.setText(StringUtil.parseStringCodePattern(currency, currentBalance, 2));
			} else {
				if (currentflag.equals(ConstantGloble.ACC_CURRENTINDEX_VALUE)) {
					tv_tran_success_out_balance.setText(StringUtil.parseStringCodePattern(currency, currentBalance, 2));
							//+ ConstantGloble.JIEYU);
				} else if (currentflag.equals(ConstantGloble.LOAN_CURRENTINDEX_VALUE)) {
					tv_tran_success_out_balance.setText(StringUtil.parseStringCodePattern(currency, currentBalance, 2));
							//+ ConstantGloble.TOUZHI);
				} else {
					tv_tran_success_out_balance.setText(StringUtil.parseStringCodePattern(currency, currentBalance, 2));
				}
			}

		} else {
			for (int i = 0; i < accountDetailList.size(); i++) {
				String currencyout = (String) accountDetailList.get(i).get(Acc.DETAIL_CURRENCYCODE_RES);
				String cashremitout = (String) accountDetailList.get(i).get(Acc.DETAIL_CASHREMIT_RES);
				String balance = (String) accountDetailList.get(i).get(Acc.DETAIL_AVAILABLEBALANCE_RES);
				if (!StringUtil.isNullOrEmpty(currencyout) && currencyout.equals(currency)) {
					if ((ConstantGloble.PRMS_CODE_RMB).equals(currency)) {
						tran_success_out_balance.setVisibility(View.VISIBLE);
						tv_tran_success_out_balance.setText(StringUtil.parseStringCodePattern(currencyout, balance, 2));
						break;
					} else {
						if (!StringUtil.isNullOrEmpty(cashremitout) && cashremitout.equals(cashRemit)) {
							tran_success_out_balance.setVisibility(View.VISIBLE);
							tv_tran_success_out_balance.setText(StringUtil.parseStringCodePattern(currencyout, balance,
									2));
							break;
						}
					}

				}
			}
		}

	}

	/**
	 * 请求账户余额
	 */
	public void requestAccBankOutDetail(String accountId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.QRY_ACC_BALANCE_API);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Acc.DETAIL_ACC_ACCOUNTID_REQ, accountId);
		BiiHttpEngine.showProgressDialog();
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestAccBankOutDetailCallback");

	}

	/**
	 * 请求账户余额回调
	 * 
	 * @param resultObj
	 *            返回结果
	 */
	public void requestAccBankOutDetailCallback(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> callbackmap = (Map<String, Object>) (biiResponseBody.getResult());
		if (StringUtil.isNullOrEmpty(callbackmap)) {
			BiiHttpEngine.dissMissProgressDialog();
			return;
		}
		BiiHttpEngine.dissMissProgressDialog();
		accountDetailList = (List<Map<String, Object>>) (callbackmap.get(ConstantGloble.ACC_DETAILIST));
	}

	/**
	 * 请求信用卡余额信息
	 * 
	 * @param accountId
	 *            账户ID
	 * @param currency
	 *            查询币种
	 */
	public void requestPsnCrcdOutDetail(String accountId, String currency) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Crcd.CRCD_ACCOUNTDETAIL_API);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Crcd.CRCD_ACCOUNTID_RES, accountId);
		paramsmap.put(Crcd.CRCD_CURRENCY, currency);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "requestPsnCrcdOutDetailCallBack");
	}

	/**
	 * 请求信用卡余额信息返回
	 * 
	 * @param resultObj
	 */
	public void requestPsnCrcdOutDetailCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		Map<String, Object> resultDetail = (Map<String, Object>) biiResponseBody.getResult();
		BiiHttpEngine.dissMissProgressDialog();
		if (StringUtil.isNullOrEmpty(resultDetail)) {
			return;
		}
		Map<String, Object> detailMap = new HashMap<String, Object>();
		detailList = (List<Map<String, Object>>) resultDetail.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
		if (StringUtil.isNullOrEmpty(detailList)) {
			return;
		}
		detailMap = detailList.get(0);
		if (StringUtil.isNullOrEmpty(detailMap)) {
			return;
		}
	}

	// TODO 公共请求处理
	/**
	 * 公共请求处理
	 * 
	 * @param request
	 *            请求方法名
	 * @param map
	 *            上传数据
	 * @param successCallBack
	 *            成功回调方法名
	 */
	public void requestBIIForTran(String request, Map<String, Object> map, String successCallBack) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setMethod(request);
		biiRequestBody.setParams(map);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, successCallBack);
	}

	public BiiResponseBody callBackResponse(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		return biiResponseBody;
	}

	/**
	 * 实时转账——所属银行模糊查询
	 */
	public void psnEbpsQueryAccountOfBank(int currentIndex, int pageSize) {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.PSNEBPSQUERYACCOUNTOFBANK_API);
		Map<String, String> map = new HashMap<String, String>();
		// 银行名称
		map.put(Tran.EBPSQUERY_BANKNAME_REQ, "");
		// 当前页
		map.put(Tran.EBPSQUERY_CURRENTINDEX_REQ, currentIndex + "");
		// 每页显示条数
		map.put(Tran.EBPSQUERY_pageSize_REQ, pageSize + "");
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "psnEbpsQueryAccountOfBankCallBack");
	}




	/**
	 * 实时转账——跨行转账 查询常用银行
	 */
	public void PsnOtherBankQueryForTransToAccount(String type) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
	biiRequestBody.setMethod(Tran.PSNOTHERBANKQUERYFORTRANSTOACCOUNT);
	Map<String, String> map = new HashMap<String, String>();
	// 银行名称
	map.put(Tran.TRANS_TYPE_RES, type);
	biiRequestBody.setParams(map);
	HttpManager.requestBii(biiRequestBody, this, "PsnOtherBankQueryForTransToAccountCallBack");
	}


	public void PsnOtherBankQueryForTransToAccountCallBack(Object resultObj){

	}


	/**
	 * 实时转账——跨行转账 查询其他银行
	 */
	public void PsnOtherBankQueryForTransToAccount2(String type) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
	biiRequestBody.setMethod(Tran.PSNOTHERBANKQUERYFORTRANSTOACCOUNT);
	Map<String, String> map = new HashMap<String, String>();
	// 银行名称
	map.put(Tran.TRANS_TYPE_RES, type);
	biiRequestBody.setParams(map);
	HttpManager.requestBii(biiRequestBody, this, "PsnOtherBankQueryForTransToAccountCallBack2");
	}

	public void PsnOtherBankQueryForTransToAccountCallBack2(Object resultObj){

	}
	/**
	 * 国内跨行实时汇款预交易
	 */
	public void psnEbpsRealTimePaymentConfirm(String isSendSmc, String payeeMobile, String amount, String memo,
			String combineId) {

		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance().getAccOutInfoMap();
		String fromAccountId = (String) accOutInfoMap.get(Comm.ACCOUNT_ID);
		Map<String, Object> accInInfoMap = TranDataCenter.getInstance().getAccInInfoMap();
		String payeeActno = (String) accInInfoMap.get(Tran.EBPSQUERY_PAYEEACTNO_REQ);
		String payeeName = (String) accInInfoMap.get(Tran.EBPSQUERY_PAYEEACTNAME_REQ);
		String bankName = (String) accInInfoMap.get(Tran.EBPSQUERY_PAYEEBANKNAME_REQ);
		String toOrgName = (String) accInInfoMap.get(Tran.EBPSQUERY_PAYEEBANKNAME_REQ);
		String cnapsCode = (String) accInInfoMap.get(Tran.EBPSQUERY_PAYEECNAPS_REQ);

		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		// 跨行
		biiRequestBody.setMethod(Tran.PSNEBPSREALTIMEPAYMENTCONFIRM_API);
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.CONVERSATION_ID));
		Map<String, String> map = new HashMap<String, String>();
		if (isSendSmc.equals(ConstantGloble.FALSE)) {
			payeeMobile = "";
		} else {
			map.put(Tran.EBPSREAL_SENDMSGFLAG_REQ, "1");
		}
		map.put(Tran.TRANS_BOCNATIONAL_FROMACCOUNTID_REQ, fromAccountId);
		map.put(Tran.EBPSQUERY_PAYEEACTNO2_REQ, payeeActno);
		map.put(Tran.EBPSQUERY_PAYEEACTNO_REQ, payeeActno);
		map.put(Tran.SAVEEBPS_PAYEENAME_REQ, payeeName);
		map.put(Tran.EBPSQUERY_PAYEEBANKNAME_REQ, bankName);
		map.put(Tran.EBPSQUERY_PAYEEORGNAME_REQ, toOrgName);
		map.put(Tran.EBPSQUERY_PAYEECNAPS_REQ, cnapsCode);
		map.put(Tran.TRANS_BOCNATIONAL_REMITTANCEINFO_REQ, "");
		map.put(Tran.TRANS_BOCNATIONAL_AMOUNT_REQ, amount);
		map.put(Tran.TRANS_BOCNATIONAL_PAYEEMOBILE_REQ, payeeMobile);
		map.put(Tran.EBPSREAL_MEMO_REQ, memo);

		map.put(Tran.TRANS_BOCNATIONAL_CURRENCY_REQ, ConstantGloble.PRMS_CODE_RMB);
		map.put(Tran.TRANS_BOCNATIONAL_EXECUTETYPE_REQ, ConstantGloble.NOWEXE);
		map.put(Tran.TRANS_BOCNATIONAL_EXECUTEDATE_REQ, "");
		map.put(Tran.TRANS_BOCNATIONAL__COMBINID_REQ, combineId);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "psnEbpsRealTimePaymentConfirmCallBack");
	}

	/** 信用卡类型 */
	public List<String> outTypeList = new ArrayList<String>() {
		{
			add(ConstantGloble.GREATWALL);
			add(ConstantGloble.ZHONGYIN);
			add(ConstantGloble.SINGLEWAIBI);
		}
	};

	// TODO 校验转出账户有无人民币
	public boolean checkTranOutAccount() {
		/** 转出账户详情 */
		Map<String, Object> outDetailMap = TranDataCenter.getInstance().getCurrOutDetail();
		/** 转出账户信息 */
		Map<String, Object> outAccountMap = TranDataCenter.getInstance().getAccOutInfoMap();
		String tranOutType = (String) outAccountMap.get(Comm.ACCOUNT_TYPE);
		if (outTypeList.contains(tranOutType)) {
			// 信用卡 肯定有人民币  单外币没有人民币
			if(ConstantGloble.SINGLEWAIBI.equals(tranOutType)){
				return false;	
			}else{
				return true;	
			}
			
		} else {
			@SuppressWarnings("unchecked")
			List<Map<String, String>> detialList = (List<Map<String, String>>) outDetailMap
			.get(ConstantGloble.ACC_DETAILIST);
			if (StringUtil.isNullOrEmpty(detialList)) {
				return false;
			}
			for (int i = 0; i < detialList.size(); i++) {
				Map<String, String> map = detialList.get(i);
				String currency = map.get(Comm.CURRENCYCODE);
				if (currency.equals(ConstantGloble.PRMS_CODE_RMB)) {
					return true;
				}
			}
		}
		return false;
	}

	// 校验有无人民币，如果没有弹出提示信息
	public boolean checkRMBMessage() {
		if (!checkTranOutAccount()) {
			BaseDroidApp.getInstanse().showInfoMessageDialog("转出账户无人民币币种，暂不支持此交易");
			return false;
		}
		return true;
	}

	// /////////////////////////////////////
	// TODO 402 汇款笔数套餐
	// 数据字典
	// 套餐类型上送字典
	public static final List<String> remitSetMealTypeResList = new ArrayList<String>() {
		{

			add("100/1/486");
			add("500/3/1886");
			add("1500/6/3886");
			add("3000/12/5886");
			add("10000/12/8886");
			add("100/12/1086");
		}
	};
	// 套餐类型显示字典
	public static final List<String> remitSetMealTypeVisList = new ArrayList<String>() {
		{

			add("100笔/1月-486元");
			add("500笔/3月-1886元");
			add("1500笔/6月-3886元");
			add("3000笔/12月-5886元");
			add("10000笔/12月-8886元");
			add("100笔/12月-1086元");
		}
	};
	// 是否是自动续约套餐
	public static final Map<String, String> extensionTypeFlag = new HashMap<String, String>() {
		{
			put("Y", "是");
			put("N", "否");
		}
	};
	// 套餐类型显示字典-套餐类型上送字典
	public static final Dictionary<String, String, Object> RemitSetMealTypeResDic = new Dictionary<String, String, Object>() {
		{
			put("100笔/1月-486元", "100/1/486");
			put("500笔/3月-1886元", "500/3/1886");
			put("1500笔/6月-3886元", "1500/6/3886");
			put("3000笔/12月-5886元", "3000/12/5886");
			put("10000笔/12月-8886元", "10000/12/8886");
			put("100笔/12月-1086元", "100/12/1086");
		}
	};

	// 汇款套餐属性(选择收款套餐及双向套餐时显示)
	public static final Dictionary<String, String, Boolean> RemitSetMealProducDic = new Dictionary<String, String, Boolean>() {
		{
			put("付款方套餐", "1", false);
			put("收款方套餐", "2", true);
			put("双向套餐", "3", true);
		}
	};

	/**
	 * 套餐类型   格式   100笔/1月/100.00元  
	 * 套餐签约  套餐修改
	 * @param type
	 * @return
	 */
	public static String encodeMealType(String type) {
		String result = null;
		if (!TextUtils.isEmpty(type)) {
			String[] split = type.split("/");
			result = String.format("%s笔/%s月-%s元", split[0], split[1], split[2]);
		}
		return result;
	}

	/**
	 * @Description: 是否上传共享账户[编号第一位为7不上传共享账户]
	 * @param remitSetMealProducPropertyId 套餐ID
	 */
	protected boolean isLoadSharedAccountList(String remitSetMealProducPropertyId) {
		if (TextUtils.isEmpty(remitSetMealProducPropertyId)) {
			return false;
		}
		return remitSetMealProducPropertyId.indexOf('7') != 0;
	}

	/**
	 * 反显 套餐类型 格式化元小数点后面位数 
	 * 返回结果100.000      显示100.00
	 * 汇款套餐查询    解除自动续约
	 * @param type 类型
	 * @return
	 */
	public static String encodeMealTypEcutout2(String type) {
		String result = null;
		if (!TextUtils.isEmpty(type)) {
			String[] split = type.split("/");
			String amount = split[2];
			String mount = amount.substring(0, amount.lastIndexOf(".")+3);
			result = String.format("%s笔/%s月-%s元", split[0], split[1], mount);
		}
		return result;
	}

	/**
	 * 过滤金额千位符里面逗号","
	 * 并且格式化金额小数位最多为两位
	 * @param mount
	 * @return
	 */
	public static String formatMount(String mount){
		if(TextUtils.isEmpty(mount)){
			return mount;
		}
		mount = mount.replace(",", "");
		return StringUtil.append2Decimals(mount,2);
	}
//	
//	/**
//	 * 微信抽奖  进入微信应用
//	 */
//	public void skipWeixin(){
//		try {
//			/* 判断微信是否安装 */
//			if ((TranBaseActivity.this.getPackageManager().getPackageInfo(
//					"com.tencent.mm", 64)) == null) {
//				Toast.makeText(TranBaseActivity.this, "微信未安装！", Toast.LENGTH_SHORT)
//						.show();
//				return;
//			}
//
//		} catch (NameNotFoundException e1) {
//			e1.printStackTrace();
//			Toast.makeText(TranBaseActivity.this, "微信未安装！", Toast.LENGTH_SHORT)
//					.show();
//			return;
//		}
//		try {
//			TranBaseActivity.this.startActivity(TranBaseActivity.this
//					.getPackageManager().getLaunchIntentForPackage(
//							"com.tencent.mm"));// 打开微信，com.tencent.mm是微信的包名
//			LogGloble.i("MainActivity", "start weixin");
//		} catch (Exception e) {
//			Toast.makeText(TranBaseActivity.this, "请检查是否安装微信！", Toast.LENGTH_SHORT)
//					.show();
//
//		}
//	}
	
//	/***
//	 * 消息推送取票接口
//	 * @param tranTime 交易时间
//	 * @param strTransaction 交易号
//	 */
//	public void sendPsnGetTicketForMessage(String strTransaction ){
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(Tran.PSNGETTICKETFORMESSAGE);
//		Map<String,String> params = new HashMap<String ,String>();
//		params.put("ticketId", strTransaction);
//		biiRequestBody.setParams(params);
//		HttpManager.requestBii(biiRequestBody, TranBaseActivity.this, "requestPsnGetTicketForMessageCallBack");
//	}
//	
//	/***
//	 * 消息推送取票接口 回调
//	 * @param resultObj
//	 */
//	public void requestPsnGetTicketForMessageCallBack(Object resultObj){
//		Map<String, Object> result = HttpTools.getResponseResult(resultObj);
//		String ticket = result.get("ticket")+"";
////		sendInsertTranSeq(_strTransaction,_tranTime,ticket);
//	}
//	
//	/***
//	 * PsnGetTicketForMessage
//	 * @param seqNo  流水号
//	 * @param tranTime 交易时间
//	 * @param ticket 票内容
//	 */
//	public void sendInsertTranSeq(String seqNo,String tranTime,String ticket){
////		BaseHttpEngine.showProgressDialog();
//		// 参数
//		Map<String, Object> paramsMap = new HashMap<String, Object>();
//		/**PsnCommonQueryOprLoginInfo 查询操作员信息接口返回数据*/
//		HashMap<String, Object> loginInfo =(HashMap<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BIZ_LOGIN_DATA);
//		paramsMap.put(Tran.WEIXIN_CUSTID, loginInfo.get("cifNumber")+"");
//		paramsMap.put(Tran.WEIXIN_NAME, loginInfo.get("customerName")+"");
//		paramsMap.put(Tran.WEIXIN_PHONENO, loginInfo.get("mobile")+"");
//		paramsMap.put(Tran.WEIXIN_SEQNO, seqNo);
//		paramsMap.put(Tran.WEIXIN_TRANTIME, tranTime);
//		paramsMap.put(Tran.WEIXIN_TICKET, ticket);;
//		
//		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(Tran.INSERTTRANSEQ);
//		biiRequestBody.setParams(paramsMap);
//		HttpManager.requestBii(SystemConfig.WEIBANKPUBLICMODULE_PATH,biiRequestBody, this, "sendInsertTranSeqCallback");
//
//	}
//	/***
//	 * PsnGetTicketForMessage 回调
//	 * @param resultObj
//	 */
//	public void sendInsertTranSeqCallback(Object resultObj){
//		
//	}
	
	/**
	 * 
	 * @param flag 0 抽奖图标提示语 ；1完成/主界面 提示语
	 * @param transactionId 交易序号
	 * @return 提示语
	 */
//	public String getWXRaffleCue(int flag ,String transactionId){
//		StringBuffer sb = new StringBuffer();
//		if(flag == 0){
//			sb.append("\t\t\t亲爱的用户：您的转账交易金额已符合抽奖条件，请按照以下提示操作。\n\t\t\t1、记录并妥善保存您当前转账的交易序号");
//			sb.append("\t\t\t\n"+transactionId+"\n");
//			sb.append("作为抽奖凭证，点击“确定”即可进入微信。在公众号中搜索“中国银行微银行”进行关注后，点击页面下方“微生活”频道选择“幸运抽奖”进入活动页面，按提示输入交易序号后进行抽奖。\n\t\t\t2、欲了解更多活动信息，请关注我行微信公众号“中国银行微银行”。 ");
//		}else{
//			sb.append("亲爱的用户：您的本次转账交易金额已符合抽奖条件，可关闭本页面后，再点击“幸运大抽奖”图标进行操作。");
////			sb.append("\t\t\t\n"+transactionId+"\n");
////			sb.append("并阅读以下提示：\n\t\t\t在参与活动前，请确保您当前使用的手机已下载并开通微信客户端，如果您尚未关注我行微信公众号“中国银行微银行”，请按以下步骤操作（如您已关注我行微信，请忽略步骤二）：\n\t\t\t步骤一，使用您当前进行转账交易的手机进入微信客户端；\n\t\t\t步骤二，在公众号中搜索中国银行微银行进行关注；\n\t\t\t步骤三，点击页面下方“微生活”频道选择“幸运抽奖”进入活动页面，按提示输入交易序号后进行抽奖");
//		}
//		return sb.toString();
//	}
	
	/** 查询跨行实时转账单笔记录 */
	public void requestSingleTransQueryTransferRecord(String transId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.PSNSINGLETRANQUERYTRANSFERRECORD_API);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(Tran.TRANS_ID, transId );
		biiRequestBody.setParams(paramsmap);
		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestSingleTransQueryTransferRecordCallBack");
	}
	
	public void requestSingleTransQueryTransferRecordCallBack(Object resultObj){
		
	}
	
	
	/** 查询跨行实时转账单笔记录  轮询专用 */
	public void requestSingleTransQueryTransferRecordDedicated(String transId) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.PSNSINGLETRANQUERYTRANSFERRECORD_API);
		Map<String, Object> paramsmap = new HashMap<String, Object>();
		paramsmap.put(Tran.TRANS_ID, transId );
		biiRequestBody.setParams(paramsmap);
//		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "requestSingleTransQueryTransferRecordDedicatedCallBack");
	}
	
	public void requestSingleTransQueryTransferRecordDedicatedCallBack(Object resultObj){
		
	}
	/**
	 * 扫描二维码
	 */
	public void scan2DimentionCode() {
		Intent intent = new Intent();
		intent.setClass(this, CaptureActivity.class);
		startActivity(intent);
	}
	
	/**
	 * 解析二维码
	 * 
	 * @param bitmap
	 *            二维码图片
	 * @return String 文本内容
	 */
	public String decode2DimentionCode(Bitmap bitmap) {
		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
		// 获得待解析的图片
		RGBLuminanceSource source = new RGBLuminanceSource(bitmap);

		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		Result result;
		try {
			result = reader.decode(bitmap1, hints);
			// 得到解析后的文字
			return result.getText();
		} catch (NotFoundException e) {
			LogGloble.e(TAG, "decode2DimentionCode NotFoundException");
			LogGloble.exceptionPrint(e);

			return null;
		} catch (ChecksumException e) {
			LogGloble.e(TAG, "decode2DimentionCode ChecksumException");
			LogGloble.exceptionPrint(e);
			return null;
		} catch (FormatException e) {
			LogGloble.e(TAG, "decode2DimentionCode FormatException");
			LogGloble.exceptionPrint(e);
			return null;
		}
	}
	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.OneTask;
	}
}
