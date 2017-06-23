package com.boc.bocsoft.mobile.framework.zxing.scan;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.boc.bocsoft.mobile.framework.zxing.camera.CameraManager;
import com.boc.bocsoft.mobile.framework.zxing.scan.itf.ICameraListener;

import java.io.IOException;

/**
 * 控制摄像头预览内容的显示
 * Created by XieDu on 2016/6/15.
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback {
    private CameraManager cameraManager;
    private boolean isHasSurface = false;
    private ICameraListener cameraListener;

    public CameraView(Context context) {
        super(context);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CameraView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setCameraListener(ICameraListener cameraListener) {
        this.cameraListener = cameraListener;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public void startCamara() {
        cameraManager = new CameraManager(getContext().getApplicationContext());
        if (isHasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(getHolder());
        } else {
            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            getHolder().addCallback(this);
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(getClass().getSimpleName(),
                    "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            cameraListener.onCamaraOpened(cameraManager);
        } catch (IOException ioe) {
            cameraListener.onCamaraOpenError();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            cameraListener.onCamaraOpenError();
        }
    }

    public void closeCamara() {
        cameraManager.closeDriver();
        if (!isHasSurface) {
            getHolder().removeCallback(this);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!isHasSurface) {
            isHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isHasSurface = false;
        getHolder().removeCallback(this);
    }

    /**
     * 设置闪光灯
     *
     * @param open 是否打开
     */
    public void setFlashlight(boolean open) {

        cameraManager.setTorch(open);
    }
}
