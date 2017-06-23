package com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.ErrorDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.model.ScanResultAccountModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.presenter.QrcodeScanPresenter;
import com.boc.bocsoft.mobile.framework.zxing.decode.QRCodeDecoder;
import com.boc.bocsoft.mobile.framework.zxing.scan.CameraView;
import com.boc.bocsoft.mobile.framework.zxing.scan.CaptureManager;
import com.boc.bocsoft.mobile.framework.zxing.scan.ScanBoxView;
import com.boc.bocsoft.mobile.framework.zxing.scan.itf.ICapturer;
import com.boc.bocsoft.mobile.framework.zxing.utils.BitmapUtils;
import com.google.zxing.Result;

import java.io.File;

/**
 * 获取二维码（扫描、从相册获取）
 */
public class QrcodeScanFragment extends MvpBussFragment<QrcodeScanPresenter>
        implements View.OnClickListener, ICapturer, QrcodeScanContract.View {

    private static final String TAG = QrcodeScanFragment.class.getSimpleName();
    public static final int RESULT_DECODE_SUCCESS = 101;
    public static final String RESULT_KEY = "result_model";

    protected CameraView cameraPreview;
    protected ImageView leftIconIv;
    protected ImageView rightIconIv;
    protected ScanBoxView viewScanBox;
    protected TextView btnGallery;
    protected TextView btnMyQrcode;
    protected RelativeLayout scanContainer;
    private View rootView;

    private CaptureManager captureManager;

    private boolean openFlashLight;

    private final int RESULT_CHOOSE_FROM_GALLERY = 1;
    private boolean isStartForResult;

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        Window window = getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        rootView = View.inflate(mContext, R.layout.fragment_qrcode_scan, null);
        return rootView;
    }

    /**
     * 初始化数据
     */
    @Override
    public void initView() {
        cameraPreview = (CameraView) rootView.findViewById(R.id.camera_preview);
        leftIconIv = (ImageView) rootView.findViewById(R.id.leftIconIv);
        rightIconIv = (ImageView) rootView.findViewById(R.id.rightIconIv);
        viewScanBox = (ScanBoxView) rootView.findViewById(R.id.view_scan_box);
        btnGallery = (TextView) rootView.findViewById(R.id.btn_gallery);
        btnMyQrcode = (TextView) rootView.findViewById(R.id.btn_my_qrcode);
        scanContainer = (RelativeLayout) rootView.findViewById(R.id.scan_container);
    }

    @Override
    public void initData() {
        Bundle arguments = getArguments();
        isStartForResult = arguments.getBoolean("isStartForResult", false);
        captureManager = new CaptureManager(this, cameraPreview, viewScanBox, scanContainer);
    }

    @Override
    public void setListener() {
        leftIconIv.setOnClickListener(this);
        rightIconIv.setOnClickListener(this);
        btnGallery.setOnClickListener(this);
        btnMyQrcode.setOnClickListener(this);
    }

    /**
     * 是否显示标题栏
     */
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        captureManager.start();
    }

    @Override
    public void onPause() {
        captureManager.stop();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        captureManager.destroy();
        setFlashLight(openFlashLight = false);
        super.onDestroy();
    }

    @Override
    protected QrcodeScanPresenter initPresenter() {
        return new QrcodeScanPresenter(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.leftIconIv) {
            titleLeftIconClick();
        } else if (id == R.id.rightIconIv) {
            titleRightIconClick();
        } else if (id == R.id.btn_gallery) {
            Intent intent =
                    new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, RESULT_CHOOSE_FROM_GALLERY);
        } else if (id == R.id.btn_my_qrcode) {
            start(new QrcodeMeFragment());
        } else {
        }
    }

    /**
     * 标题栏右侧图标点击事件
     * 设置闪光灯
     */
    protected void titleRightIconClick() {
        openFlashLight = !openFlashLight;
        setFlashLight(openFlashLight);
    }

    private void setFlashLight(boolean openFlashLight) {
        int flashLightDrawable = openFlashLight ? R.drawable.boc_flashlight_opened
                : R.drawable.boc_flashlight_closed;
        rightIconIv.setImageResource(flashLightDrawable);
        captureManager.setFlashlight(openFlashLight);
    }

    @Override
    public void finish() {
        //结束fragment
        pop();
    }

    @Override
    public void handleDecode(Result rawResult, Bundle bundle) {
        showLoadingDialog();
        startPresenter();
        getPresenter().decodeQrcode(rawResult.getText());
    }

    @Override
    public void onCamaraOpenError() {
        ErrorDialog errorDialog = new ErrorDialog(getActivity());
        errorDialog.setErrorData(getString(R.string.boc_camera_open_failed))
                .setBtnText(getString(R.string.boc_common_confirm))
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                });
        errorDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_CHOOSE_FROM_GALLERY:
                if (resultCode == Activity.RESULT_OK && null != data) {
                    String picturePath;
                    try {
                        Uri selectedImage = data.getData();
                        String[] filePathColumns = {MediaStore.Images.Media.DATA};
                        Cursor c = getActivity().getContentResolver()
                                .query(selectedImage, filePathColumns, null, null,
                                        null);
                        c.moveToFirst();
                        int columnIndex = c.getColumnIndex(filePathColumns[0]);
                        picturePath = c.getString(columnIndex);
                        c.close();
                    } catch (Exception e) {
                        picturePath = data.getData().getPath();
                    }
                    if (new File(picturePath).exists()) {
                        QRCodeDecoder.decodeQRCode(BitmapUtils.readBitmap(picturePath),
                                new QRCodeDecoder.DecodeListener() {
                                    @Override
                                    public void onDecodeQRCodeSuccess(String result) {
                                        //                                        Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT)
                                        //                                             .show();
                                        showLoadingDialog();
                                        startPresenter();
                                        getPresenter().decodeQrcode(result);
                                    }

                                    @Override
                                    public void onDecodeQRCodeFailure() {
                                        Toast.makeText(getActivity(), "未发现二维码", Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                });
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDecodeSuccess(ScanResultAccountModel model) {
        //TODO 进入二维码扫描成功转账填写页面
        closeProgressDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable(QrcodeScanFragment.RESULT_KEY, model);
        if (isStartForResult) {
            setFramgentResult(QrcodeTransFragment.QRCODE_SCAN_REQUEST_CODE, bundle);
            pop();
        } else {
            QrcodeTransFragment fragment = new QrcodeTransFragment();
            fragment.setArguments(bundle);
            startWithPop(fragment);
        }
    }

    @Override
    public void onDecodeFailed(String message) {
        closeProgressDialog();
        ErrorDialog errorDialog = new ErrorDialog(getActivity());
        errorDialog.setErrorData(message)
                .setBtnText(getString(R.string.boc_common_confirm))
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        captureManager.restartPreviewAfterDelay(1000);
                    }
                });
        errorDialog.show();
    }

    @Override
    public void onIsMyAccount() {
        onDecodeFailed(getString(R.string.boc_qrcode_scan_wrong));
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            setFlashLight(false);
        } else {
            setFlashLight(openFlashLight);
        }
    }
}