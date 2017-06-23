package com.google.zxing;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Tran;
import com.chinamworld.bocmbci.biz.tran.TranBaseActivity;
import com.chinamworld.bocmbci.biz.tran.TranDataCenter;
import com.chinamworld.bocmbci.biz.tran.twodimentrans.TwoDimenSacnResultActivity;
import com.chinamworld.bocmbci.biz.tran.twodimentrans.TwoDimenTransActivity1;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LayoutValue;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.BitmapTools;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.google.zxing.camera.CameraManager;
import com.google.zxing.decoding.CaptureActivityHandler;
import com.google.zxing.decoding.InactivityTimer;
import com.google.zxing.view.ViewfinderView;

public class CaptureActivity extends TranBaseActivity implements Callback {
	public static final String TAG = CaptureActivity.class.getSimpleName();

	public static final int OPEN_PIC_CODE = 1;

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	// private TextView txtResult;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	public static String RESULT_MESSAGE = null;
	public static Bitmap RESULT_BITMAP = null;
	/** 账户名称 */
	private String accountName;
	/** 账户账号 */
	private String accountNumber;

	private Bitmap photo;

	private String twoDimenStr;
	/** 扫描结果 转入账户信息 */
	private Map<String, Object> accInInfoMap;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.capture);
		// ��ʼ�� CameraManager
		//设置不可向右滑动
		containsSwipeListView = true;
		CameraManager.init(getApplication());

		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		// txtResult = (TextView) findViewById(R.id.txtResult);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		// 打开相册
		Button openPic = (Button) findViewById(R.id.buttonLaser);
		openPic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
				getAlbum.setType("image/*");
				startActivityForResult(getAlbum, OPEN_PIC_CODE);
			}
		});
		// 返回
		Button backBtn = (Button) findViewById(R.id.btnBack);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActivityTaskManager.getInstance().removeAllActivity();
				Intent intent = new Intent();
				intent.setClass(CaptureActivity.this,
						TwoDimenTransActivity1.class);
				startActivity(intent);
				finish();
			}
		});
		// 标记扫描二维码 查询网点
		psnVestOrgFlag = VEST_TWO_DIMEN_SCAN;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ActivityTaskManager.getInstance().removeAllActivity();
			Intent intent = new Intent();
			intent.setClass(CaptureActivity.this,
					TwoDimenTransActivity1.class);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onResume() {
		super.onResume();
		/**
		 * �������һϵ�г�ʼ�����view�Ĺ��
		 */
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		/**
		 * ������ǿ����Ƿ����������ģʽ������ǾͲ�����������ǣ��򲻲���
		 */
		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		/**
		 * ��ʼ������
		 */
		initBeepSound();
		/**
		 * �Ƿ���
		 */
		vibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		/**
		 * �ص����ص�����̣߳����looper������message
		 */
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();// �ص����
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * ��ʼ�����
	 */
	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			/**
			 * �½�������handler
			 */
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		/**
		 * ��ʼ�����
		 */
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	/**
	 * ������ʾ��view
	 */
	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	/**
	 * ���ش���������handler
	 */
	public Handler getHandler() {
		return handler;
	}

	/**
	 * ���view����ǰɨ��ɹ���ͼƬ
	 */
	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	/**
	 * 二维码扫描成功 处理
	 * 
	 * @param obj
	 * @param barcode
	 */
	public void handleDecode(Result obj, Bitmap barcode) {
		/**
		 * ���¼�ʱ
		 */
		inactivityTimer.onActivity();
		/**
		 * �������Ƶ�view��
		 */
		// ��ʼ�� CameraManager
		// viewfinderView.drawResultBitmap(barcode);
		/**
		 * ����jeep����
		 */
		playBeepSoundAndVibrate();
		/**
		 * ��ʾ�����ַ�
		 */
		String result = obj.getText();
		LogGloble.i(TAG, "result =" + result);
		requestPsnAccountInfoDecipher(result);
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	/**
	 * ��ʱ��
	 */
	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		/**
		 * ��������
		 */
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		/**
		 * ��
		 */
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case OPEN_PIC_CODE:
			if (resultCode == RESULT_OK) {
				try {
					Uri uri = data.getData();
					photo = BitmapTools.readBitmap(uri, LayoutValue.SCREEN_WIDTH, LayoutValue.SCREEN_HEIGHT);

					// photo = MediaStore.Images.Media.getBitmap(
					// getContentResolver(), data.getData());
				} catch (Exception e) {
					LogGloble.exceptionPrint(e);
				}
				if (photo == null) {
					BaseDroidApp.getInstanse().createDialog(null,
							R.string.not_two_dimen);return;
				}
				photo = Bitmap.createScaledBitmap(photo, LayoutValue.QR_WIDTH,
						LayoutValue.QR_HEIGHT, true);
				twoDimenStr = decode2DimentionCode(photo);
				BitmapTools.clearBitmap(photo);
				if (twoDimenStr == null) {
					BaseDroidApp.getInstanse().createDialog(null,
							R.string.not_two_dimen);
					return;
				}
				LogGloble.i(TAG, "decode qrcode = " + twoDimenStr);
				// 发通讯 解码
				requestPsnAccountInfoDecipher(twoDimenStr);
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 解密账户信息
	 * 
	 * @param content
	 */
	private void requestPsnAccountInfoDecipher(String content) {
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		Map<String, String> paramsmap = new HashMap<String, String>();
		biiRequestBody.setMethod(Tran.PSN_ACCOUNT_INFO_DECIPHER);
		paramsmap.put(Tran.ENCRYPTSTR, content);
		biiRequestBody.setParams(paramsmap);

		BaseHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnAccountInfoDecipherCallBack");
	}

	/**
	 * 二维码解密返回
	 * 
	 * @param resultObj
	 */
	public void requestPsnAccountInfoDecipherCallBack(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
				.getResult();
		// 非银行卡二维码图片 在此处理
		accountName = (String) resultMap.get(Tran.CUSTNAME);
		accountNumber = (String) resultMap.get(Tran.CUSTACTNUM);

		// 如果此用户在关联账户列表中
		List<Map<String, Object>> accList = TranDataCenter.getInstance()
				.getAccountOutList();
		for (int i = 0; i < accList.size(); i++) {
			String accName = (String) accList.get(i).get(Comm.ACCOUNT_NAME);
			String accNumber = (String) accList.get(i).get(Comm.ACCOUNTNUMBER);
			if (!StringUtil.isNullOrEmpty(accName)
					&& accName.equals(accountName)
					&& !StringUtil.isNullOrEmpty(accNumber)
					&& accNumber.equals(accountNumber)) {
				// 提示信息待确认
				BaseHttpEngine.dissMissProgressDialog();
				// BaseDroidApp.getInstanse().createDialog(null,
				// R.string.two_dimen_scan_wrong);
				BaseDroidApp.getInstanse().showMessageDialog(
						this.getString(R.string.two_dimen_scan_wrong),
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								BaseDroidApp.getInstanse().dismissErrorDialog();
								handler.restartPreviewAndDecode();
							}
						});
				return;
			}
		}
		// // 查询卡类型
		// requestQueryCardType();
		accInInfoMap = new HashMap<String, Object>();
		accInInfoMap.put(Comm.ACCOUNT_NAME, accountName);
		accInInfoMap.put(Comm.ACCOUNTNUMBER, accountNumber);
		TranDataCenter.getInstance().setAccInInfoMap(accInInfoMap);
		// // 查询开户行网点
		reqeustQueryAccountVestOrg();
	}

	/**
	 * 查询卡类型
	 */
	// private void requestQueryCardType() {
	// BaseHttpEngine.showProgressDialog();
	// BiiRequestBody biiRequestBody = new BiiRequestBody();
	// biiRequestBody.setMethod(Acc.ACC_PSNQUERYCARDTYPEBYCARDBIN_API);
	// Map<String, String> map = new HashMap<String, String>();
	// // 银行行号
	// map.put(Comm.ACCOUNTNUMBER, accountNumber);
	// biiRequestBody.setParams(map);
	// HttpManager.requestBii(biiRequestBody, this,
	// "requestQueryCardTypeCallBack");
	// }

	// public void requestQueryCardTypeCallBack(Object resultObj) {
	// BiiResponse biiResponse = (BiiResponse) resultObj;
	// List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
	// BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
	// @SuppressWarnings("unchecked")
	// Map<String, Object> result = (Map<String, Object>) biiResponseBody
	// .getResult();
	// String accountType = (String) result.get(Comm.ACCOUNT_TYPE);
	// LogGloble.i(TAG, "accountType =" + accountType);
	// // 对accountType进行判断 如果是信用卡 不查网店
	// if (accountType.equals(ConstantGloble.ACC_TYPE_GRE)
	// || accountType.equals(ConstantGloble.SINGLEWAIBI) // 单外币信用卡
	// || accountType.equals(ConstantGloble.ZHONGYIN)) {
	// Map<String, Object> accInInfoMap = new HashMap<String, Object>();
	// accInInfoMap.put(Comm.ACCOUNT_NAME, accountName);
	// accInInfoMap.put(Comm.ACCOUNTNUMBER, accountNumber);
	// TranDataCenter.getInstance().setAccInInfoMap(accInInfoMap);
	// Intent intent = getIntent();
	// intent.setClass(this, TwoDimenSacnResultActivity.class);
	// startActivity(intent);
	// } else {
	// // 查询开户行网店
	// // PsnQueryAccountVestOrg
	// reqeustQueryAccountVestOrg();
	// }
	// }

	/**
	 * 查询开户行网点
	 */
	private void reqeustQueryAccountVestOrg() {
		BaseHttpEngine.showProgressDialog();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Tran.PSNQUERY_ACCOUNT_VESTORG);
		Map<String, String> map = new HashMap<String, String>();
		// 银行行号
		map.put(Comm.ACCOUNTNUMBER, accountNumber);
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"reqeustQueryAccountVestOrgCallBack");
	}

	/**
	 * 查询开户网点返回
	 * 
	 * @param resultObj
	 */
	public void reqeustQueryAccountVestOrgCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		@SuppressWarnings("unchecked")
		Map<String, Object> result = (Map<String, Object>) biiResponseBody
				.getResult();
		// Map<String, Object> accInInfoMap = new HashMap<String, Object>();
		// accInInfoMap.put(Comm.ACCOUNT_NAME, accountName);
		// accInInfoMap.put(Comm.ACCOUNTNUMBER, accountNumber);
		accInInfoMap.put(Tran.ACCOUNTIBKNUM_RES,
				result.get(Tran.BANCS_BRANCHNUMBER));
		accInInfoMap.put(Tran.TRANS_BANKNAME_RES,
				result.get(Tran.BANCS_BRANCHNAME));
		TranDataCenter.getInstance().setAccInInfoMap(accInInfoMap);
		Intent intent = getIntent();
		intent.setClass(this, TwoDimenSacnResultActivity.class);
		startActivity(intent);
	}

	/**
	 * 手续费试算异常拦截 本方法有待改进
	 */
	@Override
	public boolean httpRequestCallBackPre(Object resultObj) {
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		if (biiResponseBody.getStatus().equals(ConstantGloble.STATUS_FAIL)) {// 返回的是错误码

			// 二维码解密接口
			if (Tran.PSN_ACCOUNT_INFO_DECIPHER.equals(biiResponseBody
					.getMethod())) {
				// 查询开户行网店
				// 代表返回数据异常
				if (biiResponse.isBiiexception()) {// 代表返回数据异常
					BiiHttpEngine.dissMissProgressDialog();
					BiiError biiError = biiResponseBody.getError();
					// 判断是否存在error
					if (biiError != null) {
						if (biiError.getCode() != null) {
							if (LocalData.timeOutCode.contains(biiError
									.getCode())) {// 表示回话超时 要重新登录
								showTimeOutDialog(biiError.getMessage());
							} else {
								// TODO 显示提示框 对确定按钮进行处理
								BaseDroidApp.getInstanse().showMessageDialog(
										biiError.getMessage(),
										new OnClickListener() {

											@Override
											public void onClick(View v) {
												BaseDroidApp.getInstanse()
														.dismissErrorDialog();
												handler.restartPreviewAndDecode();
											}
										});
							}
						}
					}
					// }
					return true;
				}
				return false;// 没有异常
			} else if (Tran.PSNQUERY_ACCOUNT_VESTORG.equals(biiResponseBody
					.getMethod())) {// 查询开户行网店
				// 代表返回数据异常
				if (biiResponse.isBiiexception()) {// 代表返回数据异常
					BiiHttpEngine.dissMissProgressDialog();
					BiiError biiError = biiResponseBody.getError();
					// 判断是否存在error
					if (biiError != null) {
						if (biiError.getCode() != null) {
							if (LocalData.timeOutCode.contains(biiError
									.getCode())) {// 表示回话超时 要重新登录
								showTimeOutDialog(biiError.getMessage());
							} else if (biiError.getCode().equals(
									"not.support.yet")) {// 非会话超时错误拦截
								Intent intent = getIntent();
								intent.setClass(this,
										TwoDimenSacnResultActivity.class);
								startActivity(intent);

							} else {
								// TODO 显示提示框 对确定按钮进行处理
								BaseDroidApp.getInstanse().showMessageDialog(
										biiError.getMessage(),
										new OnClickListener() {

											@Override
											public void onClick(View v) {
												BaseDroidApp.getInstanse()
														.dismissErrorDialog();
												handler.restartPreviewAndDecode();
											}
										});
							}
						}
					}
					// }
					return true;
				}
				return false;// 没有异常

			} else {
				return super.httpRequestCallBackPre(resultObj);
			}
		}
		// 随机数获取异常
		return super.httpRequestCallBackPre(resultObj);
	}


}