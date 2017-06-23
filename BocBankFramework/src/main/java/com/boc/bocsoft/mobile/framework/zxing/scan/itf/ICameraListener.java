package com.boc.bocsoft.mobile.framework.zxing.scan.itf;

import com.boc.bocsoft.mobile.framework.zxing.camera.CameraManager;

/**
 * Created by XieDu on 2016/6/15.
 */
public interface ICameraListener {

    public void onCamaraOpened(CameraManager cameraManager);
    public void onCamaraOpenError();
}
