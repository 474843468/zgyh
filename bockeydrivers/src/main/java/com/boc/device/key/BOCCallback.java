package com.boc.device.key;

/**
 * 签名回调接口
 * 
 * @author Pactera
 * 
 */
public interface BOCCallback {

	/**
	 * 签名完成后，回调执行该方法
	 * 
	 * key设备签名成功，并返回签名后的结果
	 * 
	 * @param signedData
	 *            经过Base64后的签名结果数据
	 * @param sID
	 *            调用签名方法时传入的会话ID
	 * 
	 **/
	public void keyDidSignSuccess(String signRetData, String sessionId);

	/**
	 * 签名完成后，回调执行该方法
	 * 
	 * key设备签名时出错的回调
	 * 
	 * @param error
	 *            签名出错时的错误信息（error.code的值为统一的错误码）
	 * @param sID
	 *            调用签名方法时传入的会话ID
	 * 
	 **/
	public void keyDidSignFailWithError(int errorCode, String sessionId);

	/**
	 * 签名完成后，回调执行该方法
	 * 
	 * key设备修改PIN码成功的回调
	 * 
	 * @param sID
	 *            调用修改PIN码方法时传入的会话ID
	 * 
	 **/
	public void keyDidModifyPINSuccess(String sessionId);

	/**
	 * 签名完成后，回调执行该方法
	 * 
	 * key设备修改PIN码失败的回调
	 * 
	 * @param error
	 *            修改PIN操作时的错误信息（error.code的值为统一的错误码）
	 * @param sID
	 *            调用修改PIN码方法时传入的会话ID
	 * 
	 **/
	public void keyDidModifyPINFailWithError(int errorCode, String sessionId);

	/**
	 * 签名过程中，提示用户对签名信息进行核对的事件
	 * 
	 * @param sessionID
	 *            调用签名方法时传入的会话ID
	 **/
	public void keySignNeedConfirm(String sessionId);

	/**
	 * 签名过程中，用户点击key设备“OK”键，完成对签名信息核对的事件
	 * 
	 * @param sessionID
	 *            调用签名方法时传入的会话ID
	 **/
	public void keySignDidConfirm(String sessionId);

	/**
	 * 签名过程中，用户点击key设备“C”键，取消签名操作的事件
	 * 
	 * @param sessionID
	 *            调用签名方法时传入的会话ID
	 **/
	public void keySignDidCancel(String sessionId);

	/**
	 * 修改PIN码过程中，提示用户对修改PIN码操作进行核对的事件
	 * 
	 * @param remainNumber
	 *            剩余的修改PIN码操作可重试次数
	 * @param sID
	 *            调用修改PIN码方法时传入的会话ID
	 **/
	public void keyModifyPinNeedConfirm(int remainNumber, String sessionId);

	/**
	 * 修改PIN码过程中，用户点击key设备“OK”键，确认修改的事件
	 * 
	 * @param sessionID
	 *            调用修改PIN码方法时传入的会话ID
	 **/
	public void keyModifyPinDidConfirm(String sessionId);

	/**
	 * 修改PIN码过程中，用户点击key设备“C”键，取消修改操作的事件
	 * 
	 * @param sessionID
	 *            调用修改PIN码方法时传入的会话ID
	 **/
	public void keyModifyPinDidCancel(String sessionId);
	
	/*******************************************************
	 * add beta1.1
	 *******************************************************/
	
	/**
	 *  操作PIN码过程中（签名或修改PIN码），当用户PIN码输入错误次数达到预警值时，提示用户点按Key设备“OK“或”C“键的回调
	 *
	 *  @param remainNumber 剩余的PIN码可输入次数
	 *  @param sessionId    操作PIN码过程中时传入的会话ID
	 */
	public void keyPinWarningNeedConfirm(String remainNumber, String sessionId);

	/**
	 *  操作PIN码过程中（签名或修改PIN码），PIN码输入预警提醒时，用户点击key设备“OK”键确认的回调
	 *  @param sessionID   操作PIN码过程中（签名或修改PIN码）传入的会话ID
	 */
	public void keyPinWarningDidConfirm(String sessionId);

	/**
	 *  操作PIN码过程中（签名或修改PIN码），PIN码输入预警提醒时，用户点击key设备“C”键确认的回调
	 *  @param sessionID   操作PIN码过程中（签名或修改PIN码）传入的会话ID
	 */
	public void keyPinWarningDidCancel(String sessionId);
	
	/**
	 *  操作PIN码过程中（签名),PIN码输入预警提醒时,出现错误的回调（如设备断开，没电等情况）
	 *  @param error 错误Id
	 *  @param sID   操作PIN码过程中（签名）传入的会话ID
	 */
	public void keyPinWarningFailWithError(int errorCode, String sessionID);

}
