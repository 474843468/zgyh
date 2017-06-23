package com.chinamworld.bocmbci.biz.tran.twodimentrans;

import java.util.Hashtable;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.BitmapSaveUtils;
import com.chinamworld.bocmbci.utils.BitmapTools;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.StepTitleUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * 生成二维码确认
 * 
 * @author
 * 
 */
public class GenerateDimenConfirmActivity extends TranBaseActivity implements
		OnClickListener {
	private static final String TAG = "GenerateDimenConfirmActivity";
	private Button mLastBtn, mNextBtn;
	private TextView accountNameTv, accountTv;
	private TextView accountNumberTv;
	private Bitmap accInfoPic;
	private ImageView mAccInfoPicIv;
	private Button mTopRightBtn;

	// customName

	private String customName = "";
	private String accountNumber = "";
	private String nickName = "";
	private String accountType;
	/** 加密后的字符串 */
	private String encryptStr;
	private View scroll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(this.getString(R.string.two_dimen_generate));
		View view = mInflater.inflate(
				R.layout.tran_2dimen_generate_confirm_activity, null);
		tabcontent.removeAllViews();
		tabcontent.addView(view);
		// 显示头部右边按钮，发起新转账
		mTopRightBtn = (Button) findViewById(R.id.ib_top_right_btn);
		mTopRightBtn.setVisibility(View.VISIBLE);
		mTopRightBtn.setText(this.getString(R.string.two_dimen_generate_again));
		scroll = (View) view.findViewById(R.id.scroll);
		getData();
		setupView();

	}

	/**
	 * 初始化加载XML里的控件
	 */
	private void setupView() {

		StepTitleUtils.getInstance().initTitldStep(this,
				this.getResources().getStringArray(R.array.twodimen_generate));
		StepTitleUtils.getInstance().setTitleStep(2);
		encryptStr = this.getIntent().getStringExtra(
				ConstantGloble.ACC_POSITION);
		accInfoPic = create2DimentionCode(encryptStr);

		mLastBtn = (Button) findViewById(R.id.btn_last_2dimen_generate_confirm);
		mNextBtn = (Button) findViewById(R.id.btn_next_2dimen_generate_confirm);
		accountNameTv = (TextView) findViewById(R.id.tv_acc_2dimen_generate_confirm);
		accountNameTv.setText(LocalData.AccountType.get(accountType));
		accountNumberTv = (TextView) findViewById(R.id.tran_acc_no_tv);
		accountNumberTv.setText(StringUtil.getForSixForString(accountNumber));

		accountTv = (TextView) findViewById(R.id.tv_accName_2dimen_generate_confirm);
		accountTv.setText(customName);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
				accountNameTv);
		PopupWindowUtils.getInstance()
				.setOnShowAllTextListener(this, accountTv);
		mAccInfoPicIv = (ImageView) findViewById(R.id.iv_pic_2dime_generate_confirm);
		mAccInfoPicIv.setImageBitmap(accInfoPic);
		mTopRightBtn.setOnClickListener(this);
		mLastBtn.setOnClickListener(this);
		mNextBtn.setOnClickListener(this);
	}

	/**
	 * 得到用户选择的数据
	 * 
	 */
	private void getData() {
		// accountName 用customName
		@SuppressWarnings("unchecked")
		Map<String, Object> data = (Map<String, Object>) BaseDroidApp
				.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA);
		customName = (String) data.get(Login.CUSTOMER_NAME);

		Map<String, Object> accOutInfoMap = TranDataCenter.getInstance()
				.getAccOutInfoMap();
		accountNumber = (String) accOutInfoMap.get(Comm.ACCOUNTNUMBER);
		nickName = (String) accOutInfoMap.get(Comm.NICKNAME);
		accountType = (String) accOutInfoMap.get(Comm.ACCOUNT_TYPE);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ib_top_right_btn:// 头部右侧重新生成
			// ActivityTaskManager.getInstance().removeAllActivity();
			Intent intent0 = new Intent(GenerateDimenConfirmActivity.this,
					SelectAccountActivity.class);
			startActivity(intent0);
			finish();
			break;
		case R.id.btn_last_2dimen_generate_confirm:// 取消
			finish();
			break;
		case R.id.btn_next_2dimen_generate_confirm:// 下一步
			String strSave = GenerateDimenConfirmActivity.this
					.getString(R.string.save_gallary);
			mNextBtn.setText(strSave);
			mNextBtn.setOnClickListener(savePicListener);
			String genOther = this.getString(R.string.gen_other_two_dimen);
			mTopRightBtn.setText(genOther);
			back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ActivityTaskManager.getInstance().removeAllActivity();
					Intent intent = new Intent();
					intent.setClass(GenerateDimenConfirmActivity.this,
							TwoDimenTransActivity1.class);
					startActivity(intent);
					GenerateDimenConfirmActivity.this.finish();
					if (!StringUtil.isNullOrEmpty(accInfoPic)) {
						BitmapTools.clearBitmap(accInfoPic);
					}
				}
			});
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mNextBtn.getText()
					.equals(this.getString(R.string.save_gallary))) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(GenerateDimenConfirmActivity.this,
						TwoDimenTransActivity1.class);
				startActivity(intent);
				GenerateDimenConfirmActivity.this.finish();
				if (!StringUtil.isNullOrEmpty(accInfoPic)) {
					BitmapTools.clearBitmap(accInfoPic);
				}
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 保存到相册
	 */
	private OnClickListener savePicListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				// ContentResolver cr = getContentResolver();
				// MediaStore.Images.Media.insertImage(cr, accInfoPic,
				// "genterate2Dimen.jpg", "this is a Photo");
				boolean issuccess = BitmapSaveUtils.createViewForBitmap(
						GenerateDimenConfirmActivity.this, scroll);
				if (!issuccess) {
					// 提示
					BaseDroidApp
							.getInstanse()
							.showInfoMessageDialog(
									GenerateDimenConfirmActivity.this
											.getString(R.string.save_dimen_to_gallary_fail));
					return;
				}
			} catch (Exception e) {
				LogGloble.exceptionPrint(e);
			}
			CustomDialog.toastShow(GenerateDimenConfirmActivity.this,
					GenerateDimenConfirmActivity.this
							.getString(R.string.save_dimen_to_gallary));
		}
	};
	/**
	 * 将制定内容生成二维码
	 * 
	 * @param codeInfo
	 *            文本内容
	 * @return bitmap二维码图片
	 */
	public Bitmap create2DimentionCode(String codeInfo) {
		try {
			QRCodeWriter writer = new QRCodeWriter();

			if (codeInfo == null || "".equals(codeInfo)
					|| codeInfo.length() < 1) {
				return null;
			}

			// 把输入的文本转为二维码
			BitMatrix martix = writer.encode(codeInfo, BarcodeFormat.QR_CODE,
					LayoutValue.QR_WIDTH, LayoutValue.QR_HEIGHT);

			LogGloble.i(TAG,
					"w:" + martix.getWidth() + "h:" + martix.getHeight());

			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			BitMatrix bitMatrix = new QRCodeWriter().encode(codeInfo,
					BarcodeFormat.QR_CODE, LayoutValue.QR_WIDTH,
					LayoutValue.QR_HEIGHT, hints);
			int[] pixels = new int[LayoutValue.QR_WIDTH * LayoutValue.QR_HEIGHT];
			for (int y = 0; y < LayoutValue.QR_HEIGHT; y++) {
				for (int x = 0; x < LayoutValue.QR_WIDTH; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * LayoutValue.QR_WIDTH + x] = 0xff000000;
					} else {
						pixels[y * LayoutValue.QR_WIDTH + x] = 0xffffffff;
					}

				}
			}

			Bitmap bitmap = Bitmap.createBitmap(LayoutValue.QR_WIDTH,
					LayoutValue.QR_HEIGHT, Bitmap.Config.ARGB_8888);

			bitmap.setPixels(pixels, 0, LayoutValue.QR_WIDTH, 0, 0,
					LayoutValue.QR_WIDTH, LayoutValue.QR_HEIGHT);
			// 添加Logo,logo绘制二维码上的比例
			int scale = 7;
			Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.logo_boc);
			Canvas canvas = new Canvas(bitmap);
			Rect srcRect = new Rect(0, 0, logoBitmap.getWidth(),
					logoBitmap.getHeight());
			int bitmapWidht = bitmap.getWidth();
			int bitmapHeight = bitmap.getHeight();
			int logoDstBitmapWidth = bitmapWidht / scale;
			int logoDstBitmapHeight = bitmapHeight / scale;
			int logoOffsetLeft = (bitmapWidht - logoDstBitmapWidth) / 2;
			int logoOffsetTop = (bitmapHeight - logoDstBitmapHeight) / 2;
			Rect dstRect = new Rect(logoOffsetLeft, logoOffsetTop,
					logoOffsetLeft + logoDstBitmapWidth, logoOffsetTop
							+ logoDstBitmapHeight);
			// Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			// paint.setAntiAlias(true);
			// canvas.drawARGB(0, 0, 0, 0);
			// paint.setColor(Color.TRANSPARENT);
			// paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
			// canvas.drawCircle(logoOffsetLeft + logoDstBitmapWidth / 2,
			// logoOffsetLeft + logoDstBitmapWidth / 2,
			// logoDstBitmapWidth / 2, null);
			canvas.drawBitmap(logoBitmap, srcRect, dstRect, null);
			// paint.setXfermode(null);
			return bitmap;
		} catch (WriterException e) {
			LogGloble.e(TAG, "code create error");
			return null;
		}
	}
}
