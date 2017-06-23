package com.boc.keydriverinterface;

/**
 * 返回连接信息
 * 
 * @author cland
 * 
 */
public class MEBKeyDriverCommonModel {
	public KeyDriverError mError;
	public KeyDriverCommonInfo mInfo;

	public MEBKeyDriverCommonModel() {
		mError = new KeyDriverError();
		mInfo = new KeyDriverCommonInfo();
	}

	public static class KeyDriverError {
		// type
		private MEBBocKeyRegisterType type;
		// 错误信息id
		private int errorId;
		// 错误信息string
		private String errorMessage;

		public KeyDriverError() {

		}

		public MEBBocKeyRegisterType getType() {
			return type;
		}

		public void setType(MEBBocKeyRegisterType type) {
			this.type = type;
		}

		public int getErrorId() {
			return errorId;
		}

		public void setErrorId(int errorId) {
			this.errorId = errorId;
		}

		public String getErrorMessage() {
			return errorMessage;
		}

		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}
	}

	public static class KeyDriverCommonInfo {
		// 是否需要强制修改PIN码(true:需要强制修改false:不需要)
		private boolean isPinNeedModify;

		// PIN码状态(0-5:0代表锁定)
		private int pinStatus;

		// 设备序列号
		private String keySN;

		// 低电状态 (0:电量低 1:电量正常)
		private int batteryStatus;

		// 证书颁发者
		private String publisher;

		// 证书所有者
		private String owner;

		// 证书开始日期(日期 YYMMDD)
		private String startDate;

		// 证书过期日期(YYMMDD)
		private String expiredDate;

		// 版本
		private String driverVerion;
		// 证书剩余次数
		private int remainNumbers;

		public KeyDriverCommonInfo() {

		}

		public String getPublisher() {
			return publisher;
		}

		public void setPublisher(String publisher) {
			this.publisher = publisher;
		}

		public String getOwner() {
			return owner;
		}

		public void setOwner(String owner) {
			this.owner = owner;
		}

		public String getStartDate() {
			return startDate;
		}

		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}

		public String getExpiredDate() {
			return expiredDate;
		}

		public void setExpiredDate(String expiredDate) {
			this.expiredDate = expiredDate;
		}

		public int getBatteryStatus() {
			return batteryStatus;
		}

		public void setBatteryStatus(int batteryStatus) {
			this.batteryStatus = batteryStatus;
		}

		public boolean getIsPinNeedModify() {
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

		public String getDriverVerion() {
			return driverVerion;
		}

		public void setDriverVerion(String driverVerion) {
			this.driverVerion = driverVerion;
		}

		public int getRemainNumbers() {
			return remainNumbers;
		}

		public void setRemainNumbers(int remainNumbers) {
			this.remainNumbers = remainNumbers;
		}

	}
}
