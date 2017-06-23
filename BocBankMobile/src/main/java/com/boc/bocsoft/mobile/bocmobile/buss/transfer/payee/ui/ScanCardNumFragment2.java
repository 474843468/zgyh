package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.intsig.ccrengine.CCREngine;
import com.intsig.ccrengine.CCREngine.ResultData;
import com.intsig.ccrengine.ISCardScanActivity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 扫描银行卡（保留的是原来的代码，以防不测）
 * 不要用这个，请用ScanCardNumFragment
 * Created by zhx on 2016/9/12
 */
public class ScanCardNumFragment2 extends BussFragment implements Camera.PreviewCallback {
    private static final String TAG = "PreviewActivity";
    private boolean openFlashLight = false; // 闪光灯是否打开

    public static final String EXTRA_KEY_APP_KEY = "EXTRA_KEY_APP_KEY";

    private DetectThread mDetectThread = null;
    private Preview mPreview = null;
    private Camera mCamera = null;
    private int numberOfCameras;

    // The first rear facing camera
    private int defaultCameraId;

    private float mDensity = 2.0f;

    private CCREngine mCCREngine = null;
    private boolean needNumberImg = false;
    private String needTrimedImg = null;
    private String needOriginalImg = null;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {

        mDensity = mActivity.getResources().getDisplayMetrics().density;
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Hide the window title.
        //        mActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Create a RelativeLayout container that will hold a SurfaceView,
        // and set it as the content of our activity.
        mPreview = new Preview(mActivity);

        // Find the total number of cameras available
        numberOfCameras = Camera.getNumberOfCameras();
        // Find the ID of the default camera
        CameraInfo cameraInfo = new CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
                defaultCameraId = i;
            }
        }

        mCCREngine = new CCREngine();
        //        Intent intent = mActivity.getIntent();
        //        needNumberImg = intent.getBooleanExtra(ISCardScanActivity.EXTRA_KEY_GET_NUMBER_IMG, false);
        //        needTrimedImg = intent.getStringExtra(ISCardScanActivity.EXTRA_KEY_GET_TRIMED_IMG);
        //        needOriginalImg = intent.getStringExtra(ISCardScanActivity.EXTRA_KEY_GET_ORIGINAL_IMG);
        //        final String appkey = intent.getStringExtra(EXTRA_KEY_APP_KEY);

        needNumberImg = true;
        needTrimedImg = "/sdcard/trimedcard.jpg";
        needOriginalImg = "/sdcard/origianlcard.jpg";
        final String appkey = ApplicationConst.APP_KEY;
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                int ret = mCCREngine.init(mActivity, appkey);
                return ret;
            }

            @Override
            protected void onPostExecute(Integer result) {
                if (result != 0) {
                    /**
                     * 101 包名错误, 授权APP_KEY与绑定的APP包名不匹配；
                     * 102 appKey错误，传递的APP_KEY填写错误；
                     * 103 超过时间限制，授权的APP_KEY超出使用时间限制；
                     * 104 达到设备上限，授权的APP_KEY使用设备数量达到限制；
                     * 201 签名错误，授权的APP_KEY与绑定的APP签名不匹配；
                     * 202 其他错误，其他未知错误，比如初始化有问题；
                     * 203 服务器错误，第一次联网验证时，因服务器问题，没有验证通过；
                     * 204 网络错误，第一次联网验证时，没有网络连接，导致没有验证通过；
                     * 205 包名/签名错误，授权的APP_KEY与绑定的APP包名和签名都不匹配；
                     */
                    new AlertDialog.Builder(mActivity)
                            .setMessage("Error " + result)
                            .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).setCancelable(false).create().show();
                }
            }
        }.execute();

        mPreview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //                ToastUtils.show("触摸我了");

                mHandler.sendEmptyMessageDelayed(MSG_REINIT_CAMERA, 500);

                return false;
            }
        });

        return mPreview;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("ljljlj", "ScanCardNumFragment ------ onResume()");

        try {
            mCamera = Camera.open(defaultCameraId);
        } catch (Exception e) {
            e.printStackTrace();
            showFailedDialogAndFinish();
            return;
        }
        mPreview.setCamera(mCamera);
        setDisplayOrientation();
        mCamera.setOneShotPreviewCallback(this);


    }

    @Override
    public void onPause() {
        super.onPause();

        Log.e("ljljlj", "ScanCardNumFragment ------ onPause()");
        // Because the Camera object is a shared resource, it's very
        // important to release it when the activity is paused.
        try {
            if (mCamera != null) {
                Camera camera = mCamera;
                mCamera = null;
                camera.setOneShotPreviewCallback(null);
                mPreview.setCamera(null);
                camera.release();
                camera = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("ljljlj", "ScanCardNumFragment ------ onDestroy()");

        if (mCCREngine != null) {
            mCCREngine.release();
        }
        if (mDetectThread != null) {
            mDetectThread.stopRun();
        }
        mHandler.removeMessages(MSG_AUTO_FOCUS);
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        try {
            Size size = camera.getParameters().getPreviewSize();
            if (mDetectThread == null) {
                mDetectThread = new DetectThread();
                mDetectThread.start();
                // 两秒聚焦
                mHandler.sendEmptyMessageDelayed(MSG_AUTO_FOCUS, 100);
            }
            mDetectThread.addDetect(data, size.width, size.height);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showFailedDialogAndFinish() {
        new AlertDialog.Builder(mActivity)
                .setMessage("无法连接到相机,请检查权限设置") // TODO
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ScanCardNumFragment2.this.pop();
                    }
                })
                .create()
                .show();
    }

    // thread to detect and recognize.
    private class DetectThread extends Thread {
        private ArrayBlockingQueue<byte[]> mPreviewQueue = new ArrayBlockingQueue<byte[]>(1);
        private int width;
        private int height;

        public void stopRun() {
            addDetect(new byte[]{0}, -1, -1);
        }

        @Override
        public void run() {
            int j = 0;

            try {
                while (true) {
                    long id = Thread.currentThread().getId();
                    if (j == 0) {
                        Log.e("ljljlj", "thread id = " + id);
                    }
                    j++;

                    byte[] data = mPreviewQueue.take(); // block here, if no data in the queue.
                    if (data.length == 1) {// quit the thread, if we got special byte array put by stopRun().
                        return;
                    }
                    float left, top, right, bottom;
                    int newWidth = height;
                    int newHeight = width;

                    if (false) {// vertical
                        float dis = 1 / 16f;
                        left = newWidth * dis;
                        right = newWidth - left;
                        top = (newHeight - (newWidth - left - left) * 0.618f) / 2;
                        bottom = newHeight - top;
                    } else {// horizental
                        float dis = 1 / 8f;// 10
                        left = newWidth * dis;
                        right = newWidth - left;
                        top = (newHeight - (newWidth - left - left) / 0.618f) / 2;
                        bottom = newHeight - top;
                    }
                    //                    System.out.println("left > " + left + ", top > " + top + ", right > " + right + ", bottom > " + bottom);

                    // the (left, top, right, bottom) is base on preview image's
                    // coordinate. that's different with ui coordinate.
                    int[] out = mCCREngine.detectBorder(data, width, height, (int) top, (int) (height - right), (int) bottom, (int) (height - left));
                    // if activity is port mode then (x,y)->(preview.screenHeight-y,x)
                    if (out != null) {// find border
                        //                        System.out.println("DetectCard >>>>>>>>>>>>> " + Arrays.toString(out));
                        int[] border = new int[8];
                        for (int i = 0; i < 8; i++) {
                            border[i] = out[i];
                        }
                        for (int i = 0; i < 4; i++) {
                            int tmp = out[0 + i * 2];
                            out[0 + i * 2] = height - out[1 + i * 2];
                            out[1 + i * 2] = tmp;
                        }
                        boolean match = isMatch((int) left, (int) top, (int) right, (int) bottom, out);
                        mPreview.showBorder(out, match);
                        if (match) { // get matched border
                            ResultData result = mCCREngine.recognize(data, width, height);
                            if (result != null && result.getCode() > 0) {
                                showResult(data, result, border);
                                return;
                            }
                        }
                    } else { // no find border, continue to preview;
                        mPreview.showBorder(null, false);
                    }
                    // continue to preview;
                    resumePreviewCallback();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int continue_match_time = 0;

        public boolean isMatch(int left, int top, int right, int bottom, int[] qua) {
            int dif = 120;
            int num = 0;

            if (Math.abs(left - qua[6]) < dif && Math.abs(top - qua[7]) < dif) {
                num++;
            }
            if (Math.abs(right - qua[0]) < dif && Math.abs(top - qua[1]) < dif) {
                num++;
            }
            if (Math.abs(right - qua[2]) < dif && Math.abs(bottom - qua[3]) < dif) {
                num++;
            }
            if (Math.abs(left - qua[4]) < dif && Math.abs(bottom - qua[5]) < dif) {
                num++;
            }
            //            System.out.println("inside " + Arrays.toString(qua) + " <>" + left + ", " + top + ", "
            //                    + right + ", " + bottom + "           " + num);
            if (num > 2) {
                continue_match_time++;
                if (continue_match_time >= 1)
                    return true;
            } else {
                continue_match_time = 0;
            }
            return false;
        }

        public void addDetect(byte[] data, int width, int height) {
            if (mPreviewQueue.size() == 1) {
                mPreviewQueue.clear();
            }
            mPreviewQueue.add(data);
            this.width = width;
            this.height = height;
        }

        // 显示结果（注意：showResult方法是在子线程中被调用的）
        private void showResult(byte[] data, ResultData result, int[] border) {
            if (!TextUtils.isEmpty(needOriginalImg)) {
                try {
                    // 保存原图 YUV to jpg
                    YuvImage yuv = new YuvImage(data, ImageFormat.NV21, width, height, null);
                    yuv.compressToJpeg(new Rect(0, 0, width, height), 80, new FileOutputStream(needOriginalImg));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (!TextUtils.isEmpty(needTrimedImg) && border != null) {
                try {
                    Bitmap bmp = mCCREngine.ProcessImage(data, width, height, border, result.getRotateAngle());
                    if (bmp != null) {
                        bmp.compress(CompressFormat.JPEG, 80, new FileOutputStream(needTrimedImg));
                        bmp.recycle();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 剪裁图像
            Bitmap numBmp = null;
            if (needNumberImg) {
                numBmp = mCCREngine.ProcessImage(data, width, height, result.getCardNumPos(), result.getRotateAngle());
            }
            Intent intent = new Intent();
            intent.putExtra(ISCardScanActivity.EXTRA_KEY_RESULT, result);
            // 剪裁bitmap?
            intent.putExtra(ISCardScanActivity.EXTRA_KEY_GET_NUMBER_IMG, numBmp);

            Log.e("ljljlj", "result.getCardNumber() = " + result.getCardNumber());

            Message message = new Message();
            message.what = MSG_POP_FRAGMENT;
            message.obj = result.getCardNumber();
            mHandler.sendMessage(message);
        }
    }

    private void resumePreviewCallback() {
        if (mCamera != null) {
            mCamera.setOneShotPreviewCallback(this);
        }
    }

    private void setDisplayOrientation() {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(defaultCameraId, info);
        int rotation = mActivity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result = (info.orientation - degrees + 360) % 360;
        mCamera.setDisplayOrientation(result);

        Camera.Parameters params = mCamera.getParameters();
        String focusMode = Parameters.FOCUS_MODE_AUTO;
        if (!TextUtils.equals("samsung", android.os.Build.MANUFACTURER)) {
            focusMode = Parameters.FOCUS_MODE_CONTINUOUS_PICTURE;
        }
        if (!isSupported(focusMode, params.getSupportedFocusModes())) {
            // For some reasons, the driver does not support the current
            // focus mode. Fall back to auto.
            if (isSupported(Parameters.FOCUS_MODE_AUTO, params.getSupportedFocusModes())) {
                focusMode = Parameters.FOCUS_MODE_AUTO;
            } else {
                focusMode = params.getFocusMode();
            }
        }
        focusMode = Parameters.FOCUS_MODE_CONTINUOUS_PICTURE; // 临时
        params.setFocusMode(focusMode);
        mCamera.setParameters(params);
        if (!TextUtils.equals(focusMode, Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            Log.e("ljljlj", "!TextUtils.equals(focusMode, Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)");
            mCamera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上
            mHandler.sendEmptyMessageDelayed(MSG_AUTO_FOCUS, 1000);
        }
    }

    public boolean isSupported(String value, List<String> supported) {
        return supported == null ? false : supported.indexOf(value) >= 0;
    }

    private static final int MSG_AUTO_FOCUS = 100; // Message，自动对焦
    private static final int MSG_POP_FRAGMENT = 101; // Message，返回结果
    private static final int MSG_REINIT_CAMERA = 102; // Message，执行一次onPause、onResume中执行的方法
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == MSG_AUTO_FOCUS) {
                autoFocus();
            } else if (msg.what == MSG_POP_FRAGMENT) {
                Bundle bundle = new Bundle();
                bundle.putString("cardNumber", (String) msg.obj);
                setFramgentResult(Activity.RESULT_OK, bundle);
                ScanCardNumFragment2.this.pop();
            } else if(msg.what == MSG_REINIT_CAMERA) {
                try {
                    mCamera = Camera.open(defaultCameraId);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                mPreview.setCamera(mCamera);
                setDisplayOrientation();
                mCamera.setOneShotPreviewCallback(ScanCardNumFragment2.this);
                // ------------------------
                if (mCamera != null) {
                    try {
                        mCamera.autoFocus(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mCamera.cancelAutoFocus();
                    }
                }
            }
        }
    };

    private void autoFocus() {
        if (mCamera != null) {
            try {
                mCamera.autoFocus(null);
                Log.e("ljljlj", "autoFocus() ...");
            } catch (Exception e) {
                e.printStackTrace();
                mCamera.cancelAutoFocus();
            }
            mHandler.sendEmptyMessageDelayed(MSG_AUTO_FOCUS, 1000);
        }
    }

    /**
     * A simple wrapper around a Camera and a SurfaceView that renders a centered preview of the
     * Camera to the surface. We need to center the SurfaceView because not all devices have cameras
     * that support preview sizes at the same aspect ratio as the device's display.
     */
    private class Preview extends ViewGroup implements SurfaceHolder.Callback {
        private final String TAG = "Preview";
        private SurfaceView mSurfaceView = null;
        private SurfaceHolder mHolder = null;
        private Size mPreviewSize = null;
        private List<Size> mSupportedPreviewSizes = null;
        private Camera mCamera = null;
        private DetectView mDetectView = null;
        private TextView mInfoView = null;
        private TextView mCopyRight = null;
        private ImageView mSplash = null; // 闪光灯
        private ImageView mCloseButton = null; // 关闭按钮
        private final View mBgView;
        private final int screenWidth;
        private final int screenHeight;

        public Preview(Context context) {
            super(context);
            mSurfaceView = new SurfaceView(context);
            addView(mSurfaceView);

            mInfoView = new TextView(context);
            mInfoView.setGravity(Gravity.RIGHT);
            //            mInfoView.setText("上海合合信息科技发展有限公司\\n Copyright© 2009-2016 版权所有");
            mInfoView.setText("啦啦啦");
            addView(mInfoView);

            mDetectView = new DetectView(context);
            addView(mDetectView);

            DisplayMetrics metric = new DisplayMetrics();
            mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
            screenWidth = metric.widthPixels;
            screenHeight = metric.heightPixels;
            mBgView = View.inflate(context, R.layout.layout_scan_card_num, null);
            addView(mBgView);


            // TODO 闪光灯
            mSplash = new ImageView(mActivity);
            //            mSplash.setScaleType(ImageView.ScaleType.FIT_XY);
            mSplash.setImageResource(R.drawable.icon_qrpay_scan_close);
            addView(mSplash);
            mSplash.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    openFlashLight = !openFlashLight;
                    if(mCamera != null) {
                        Parameters parameters = mCamera.getParameters();
                        parameters.setFlashMode(openFlashLight ? Parameters.FLASH_MODE_TORCH : Parameters.FLASH_MODE_OFF);
                        mCamera.setParameters(parameters);
                        mSplash.setImageResource(openFlashLight ? R.drawable.icon_qrpay_scan_open : R.drawable.icon_qrpay_scan_close);
                    }
                }
            });

            // TODO 关闭按钮
            mCloseButton = new ImageView(mActivity);
            //            mSplash.setScaleType(ImageView.ScaleType.FIT_XY);
            mCloseButton.setImageResource(R.drawable.ic_del_cha);
            addView(mCloseButton);
            mCloseButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ScanCardNumFragment2.this.pop();
                }
            });

            // TODO 版权
            mCopyRight = new TextView(mActivity);
            mCopyRight.setGravity(Gravity.RIGHT);
            mCopyRight.setRotation(90.0F);
            mCopyRight.setText("请将银行卡放于识别范围内");
            addView(mCopyRight);

            mHolder = mSurfaceView.getHolder();
            mHolder.addCallback(this);
        }

        public void setCamera(Camera camera) {
            mCamera = camera;
            if (mCamera != null) {
                mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
                requestLayout();
            }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            // We purposely disregard child measurements because act as a
            // wrapper to a SurfaceView that centers the camera preview instead
            // of stretching it.
            try {
                final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
                final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
                setMeasuredDimension(width, height);

                if (mSupportedPreviewSizes != null) {
                    mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, height, width);// 竖屏模式，寬高颠倒
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 参考QRPayScanFragment
        private void setFlashLight(boolean openFlashLight) {
            int flashLightDrawable = openFlashLight ? R.drawable.icon_qrpay_scan_close : R.drawable.icon_qrpay_scan_open;
            mSplash.setImageResource(flashLightDrawable);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            try {
                if (changed && getChildCount() > 0) {
                    final View child = getChildAt(0);

                    final int width = r - l;
                    final int height = b - t;

                    int previewWidth = width;
                    int previewHeight = height;
                /*if (mPreviewSize != null) {
                    previewWidth = mPreviewSize.screenHeight;
                    previewHeight = mPreviewSize.screenWidth;
                }*/

                    // Center the child SurfaceView within the parent.
                    if (width * previewHeight > height * previewWidth) {
                        final int scaledChildWidth = previewWidth * height / previewHeight;
                        child.layout((width - scaledChildWidth) / 2, 0, (width + scaledChildWidth) / 2,
                                height);
                        mDetectView.layout((width - scaledChildWidth) / 2, 0,
                                (width + scaledChildWidth) / 2, height);
                    } else {
                        final int scaledChildHeight = previewHeight * width / previewWidth;
                        child.layout(0, (height - scaledChildHeight) / 2, width,
                                (height + scaledChildHeight) / 2);
                        mDetectView.layout(0, (height - scaledChildHeight) / 2, width,
                                (height + scaledChildHeight) / 2);
                    }
                    getChildAt(1).layout(l, t, r, b);

                    Log.e("ljljlj", "l = " + l + " ,t = " + t + " ,r = " + r + ", b = " + b);

                    // 闪光灯所在的位置
                    mSplash.layout((int)(r - 40 * mDensity), (int)(t + 30 * mDensity), (int)(r - 20 * mDensity), (int)(t + 50 * mDensity));
                    // 关闭按钮所在的位置
                    mCloseButton.layout((int)(r - 40 * mDensity), (int)(b - 50 * mDensity), (int)(r - 25 * mDensity), (int)(b - 35 * mDensity));
                    //                    mSplash.layout((int) (l - (r - l) / 2 + 10 * mDensity), (int) (t + (b - t) / 2 - 100 * mDensity), (int) (l + (r - l) / 2 + 2 * mDensity), (int) (b - (b - t) / 2 - 52 * mDensity));
                    // 提示信息所处的位置
                    //                mCopyRight.layout(l, (int) (b - 48 * mDensity), (int) (r - 8 * mDensity), b);
                    //                    mCopyRight.layout(l, (int) (b - 48 * mDensity), (int) (r - 8 * mDensity), b);
                    //                    mCopyRight.layout((int) (l - (r - l) / 2), (int) (t + (b - t) / 2 - 48 * mDensity), (int) (l + (r - l) / 2 - 8 * mDensity), (int) (b - (b - t) / 2));
                    mCopyRight.layout((int) (l - (r - l) / 2 + 10 * mDensity), (int) (t + (b - t) / 2 - 100 * mDensity), (int) (l + (r - l) / 2 + 2 * mDensity), (int) (b - (b - t) / 2 - 52 * mDensity));
                    mBgView.layout(0, 0, screenWidth, screenHeight);
                    //                mCopyRight.layout(l, t, r, b);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void surfaceCreated(SurfaceHolder holder) {
            // The Surface has been created, acquire the camera and tell it where to draw.
            try {
                if (mCamera != null) {
                    mCamera.setPreviewDisplay(holder);
                }
            } catch (IOException exception) {
                Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // Surface will be destroyed when we return, so stop the preview.
            if (mCamera != null) {
                mCamera.stopPreview();
            }
        }

        private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
            final double ASPECT_TOLERANCE = 0.1;
            double targetRatio = (double) w / h;
            if (sizes == null)
                return null;
            Size optimalSize = null;
            double minDiff = Double.MAX_VALUE;

            int targetHeight = h;

            // Try to find an size match aspect ratio and size
            for (Size size : sizes) {
                double ratio = (double) size.width / size.height;
                if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                    continue;
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }

            // Cannot find the one match the aspect ratio, ignore the
            // requirement
            if (optimalSize == null) {
                minDiff = Double.MAX_VALUE;
                for (Size size : sizes) {
                    if (Math.abs(size.height - targetHeight) < minDiff) {
                        optimalSize = size;
                        minDiff = Math.abs(size.height - targetHeight);
                    }
                }
            }
            return optimalSize;
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            if (mCamera != null) {
                // Now that the size is known, set up the camera parameters and begin the preview.
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
                parameters.setPreviewFormat(ImageFormat.NV21);
                requestLayout();
                mDetectView.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
                //                mInfoView.setText("preview：" + mPreviewSize.screenWidth + "," + mPreviewSize.screenHeight);
                //                mInfoView.setText("上海合合信息科技发展有限公司\\n Copyright© 2009-2016 版权所有");
                mInfoView.setText("");
                mCamera.setParameters(parameters); // 此处报错
                mCamera.startPreview();
            }
        }

        public void showBorder(int[] border, boolean match) {
            mDetectView.showBorder(border, match);
        }
    }

    /**
     * the view show bank card border.
     */
    private class DetectView extends View {
        private Paint paint = null;
        private int[] border = null;
        private String result = null;
        private boolean match = false;
        private int previewWidth;
        private int previewHeight;

        public void showBorder(int[] border, boolean match) {
            this.border = border;
            this.match = match;
            postInvalidate();
        }

        public DetectView(Context context) {
            super(context);
            paint = new Paint();
            paint.setColor(0xffff0000);
        }

        public void setPreviewSize(int width, int height) {
            this.previewWidth = width;
            this.previewHeight = height;
        }

        @Override
        public void onDraw(Canvas c) {
            if (border != null) {
                paint.setStrokeWidth(3);
                int height = getWidth();
                float scale = getWidth() / (float) previewHeight;
                c.drawLine(border[0] * scale, border[1] * scale, border[2] * scale, border[3]
                        * scale, paint);
                c.drawLine(border[2] * scale, border[3] * scale, border[4] * scale, border[5]
                        * scale, paint);
                c.drawLine(border[4] * scale, border[5] * scale, border[6] * scale, border[7]
                        * scale, paint);
                c.drawLine(border[6] * scale, border[7] * scale, border[0] * scale, border[1]
                        * scale, paint);

            }
            if (match) {
                paint.setColor(0xff00ff00);
                paint.setStrokeWidth(20);
            } else {
                paint.setColor(0xffff0000);
                paint.setStrokeWidth(3);
            }

            float left, top, right, bottom;
            if (false) {// vertical
                float dis = 1 / 16f;
                left = getWidth() * dis;
                right = getWidth() - left;

                top = (getHeight() - (getWidth() - left - left) * 0.618f) / 2;
                bottom = getHeight() - top;
            } else {
                float dis = 1 / 8f;
                left = getWidth() * dis;
                right = getWidth() - left;

                top = (getHeight() - (getWidth() - left - left) / 0.618f) / 2;
                bottom = getHeight() - top;
            }

            c.save();
            c.restore();
            paint.setColor(0xff000fff);
            paint.setStyle(Paint.Style.STROKE);
            c.drawRect(left, top, right, bottom, paint);
        }
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }
}