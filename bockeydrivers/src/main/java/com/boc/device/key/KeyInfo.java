package com.boc.device.key;

/**
 * 音频key信息
 * @author lxw
 *
 */
public class KeyInfo {

	// 是否需要强制修改PIN码(true:需要强制修改false:不需要)
	private boolean isPinNeedModify;
	
	// PIN码状态(0-5:0代表锁定)
	private int pinStatus;
	
	// 设备序列号
	private String keySN;
	
	// 低电状态 (0:电量低 1:电量正常)
	private int batteryStatus;
	

	public boolean isPinNeedModify() {
		return isPinNeedModify;
	}

	public void setPinNeedModify(boolean isPinNeedModify) {
		this.isPinNeedModify = isPinNeedModify;
	}

	public int getPinStatus() {
		return pinStatus;
	}

	public void setPinStatus(int pinStatus) {
		this.pinStatus = pinStatus;
	}

	public String getKeySN() {
		return keySN;
	}

	public void setKeySN(String keySN) {
		this.keySN = keySN;
	}

	public int getBatteryStatus() {
		return batteryStatus;
	}

	public void setBatteryStatus(int batteryStatus) {
		this.batteryStatus = batteryStatus;
	}

	
	
}
