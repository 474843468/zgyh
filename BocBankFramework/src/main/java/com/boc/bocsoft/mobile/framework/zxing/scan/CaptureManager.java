package com.boc.bocsoft.mobile.framework.zxing.scan;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RelativeLayout;

import com.boc.bocsoft.mobile.framework.zxing.camera.CameraManager;
import com.boc.bocsoft.mobile.framework.zxing.camera.FrontLightMode;
import com.boc.bocsoft.mobile.framework.zxing.scan.itf.ICameraListener;
import com.boc.bocsoft.mobile.framework.zxing.scan.itf.ICaptureHandlerHelper;
import com.boc.bocsoft.mobile.framework.zxing.scan.itf.ICapturer;
import com.boc.bocsoft.mobile.framework.zxing.utils.InactivityTimer;
import com.boc.bocsoft.mobile.framework.zxing.utils.QrcodeConstants;
import com.google.zxing.Result;

/**
 * 管理CameraView和ScanBoxView，是供外界控制扫描的管理器
 * Created by XieDu on 2016/6/15.
 */
public class CaptureManager implements ICameraListener, ICaptureHandlerHelper {
    private ICapturer capturer;
    private CaptureHandler handler;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;

    private CameraView cameraView;
    private RelativeLayout viewScanBox;
    private RelativeLayout scanContainer;
    private Rect mCropRect = null;

    private boolean isDestroyed = false;
    public static FrontLightMode frontLightMode = FrontLightMode.OFF;

    /**
     * @param cameraView    摄像
     * @param viewScanBox   扫描框
     * @param scanContainer 扫描层：包含扫描框、阴影
     */
    public CaptureManager(ICapturer capturer, CameraView cameraView, RelativeLayout viewScanBox, RelativeLayout scanContainer) {
        this.capturer = capturer;
        this.cameraView = cameraView;
        this.viewScanBox = viewScanBox;
        this.scanContainer = scanContainer;
        init();
    }

    private void init() {
        inactivityTimer = new InactivityTimer(new Runnable() {
            @Override
            public void run() {
                capturer.finish();
            }
        });
        beepManager = new BeepManager(capturer.getActivity());
        cameraView.setCameraListener(this);
    }

    public void start() {
        handler = null;
        cameraView.startCamara();
        inactivityTimer.start();
    }

    public void stop() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.cancel();
        beepManager.close();
        cameraView.closeCamara();
    }

    public void destroy() {
        isDestroyed = true;
        inactivityTimer.cancel();
    }


    @Override
    public void onCamaraOpened(CameraManager cameraManager) {
        if (handler == null) {
            handler = new CaptureHandler(this, cameraManager);
        }
        initCrop();
    }

    @Override
    public void onCamaraOpenError() {
        if (capturer.getActivity().isFinishing() || this.isDestroyed) {
            return;
        }
        capturer.onCamaraOpenError();
    }


    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult The contents of the barcode.
     * @param bundle    The extras
     */
    @Override
    public void handleDecode(Result rawResult, Bundle bundle) {
        inactivityTimer.start();
        beepManager.playBeepSoundAndVibrate();
        capturer.handleDecode(rawResult, bundle);
    }


    @Override
    public Handler getCaptureHandler() {
        return handler;
    }

    public Rect getCropRect() {
        return mCropRect;
    }

    /**
     * 初始化截取的矩形区域
     */
    private void initCrop() {
        int cameraWidth = cameraView.getCameraManager().getCameraResolution().y;
        int cameraHeight = cameraView.getCameraManager().getCameraResolution().x;

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = viewScanBox.getLeft() * cameraWidth / scanContainer.getWidth();
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = viewScanBox.getTop() * cameraHeight / scanContainer.getHeight();

        /** 计算最终截取的矩形的宽度 */
        int width = viewScanBox.getWidth() * cameraWidth / scanContainer.getWidth();
        /** 计算最终截取的矩形的高度 */
        int height = viewScanBox.getHeight() * cameraHeight / scanContainer.getHeight();

        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(QrcodeConstants.RESTART_PREVIEW, delayMS);
        }
    }

    /**
     * 设置闪光灯
     */
    public void setFlashlight(boolean open) {
        frontLightMode =open? FrontLightMode.ON:FrontLightMode.OFF;
        cameraView.setFlashlight(open);
    }

}
