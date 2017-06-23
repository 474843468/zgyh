package com.boc.bocsoft.mobile.bii.bus.global.model.CurrentDeviceCheck;

/**
 * Created by feibin on 2016/6/7.
 * 检查本机是否绑定
 */
public class CurrentDeviceCheckResult {
    //1：已绑定 0：未绑定
    private String hasBindingDevice;
    //1：已绑定本设备 0：未绑定本设备
    private String currentDeviceFlag;

    public String getHasBindingDevice() {
        return hasBindingDevice;
    }

    public void setHasBindingDevice(String hasBindingDevice) {
        this.hasBindingDevice = hasBindingDevice;
    }

    public String getCurrentDeviceFlag() {
        return currentDeviceFlag;
    }

    public void setCurrentDeviceFlag(String currentDeviceFlag) {
        this.currentDeviceFlag = currentDeviceFlag;
    }
}
