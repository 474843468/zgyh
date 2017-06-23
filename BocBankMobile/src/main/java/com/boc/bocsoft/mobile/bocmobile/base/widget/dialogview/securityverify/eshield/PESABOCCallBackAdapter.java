package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.eshield;

import com.boc.keydriverinterface.MEBBOCCallBack;

/**
 * 音频key回调接口适配器
 *
 * @author lxw
 */
public class PESABOCCallBackAdapter extends MEBBOCCallBack {
    /**
     * 签名完成后，回调执行该方法
     * <p>
     * key设备签名成功，并返回签名后的结果
     *
     * @param signRetData 经过Base64后的签名结果数据
     * @param sessionId   调用签名方法时传入的会话ID
     **/
    @Override
    public void keyDidSignSuccess(String signRetData, String sessionId) {

    }

    /**
     * 签名完成后，回调执行该方法
     * <p>
     * key设备签名时出错的回调
     *
     * @param errerMessage 签名出错时的错误信息
     * @param sessionId    调用签名方法时传入的会话ID
     **/
    public void keyDidSignFailWithError(String errerMessage, String sessionId) {
    }

    /**
     * 签名完成后，回调执行该方法
     * <p>
     * key设备修改PIN码成功的回调
     *
     * @param sessionId 调用修改PIN码方法时传入的会话ID
     **/
    @Override
    public void keyDidModifyPINSuccess(String sessionId) {
    }

    /**
     * 签名完成后，回调执行该方法
     * <p>
     * key设备修改PIN码失败的回调
     *
     * @param errorMessage 修改PIN操作时的错误信息
     * @param sessionId    调用修改PIN码方法时传入的会话ID
     **/
    public void keyDidModifyPINFailWithError(String errorMessage,
                                             String sessionId) {
    }

    /**
     * 签名过程中，提示用户对签名信息进行核对的事件
     *
     * @param sessionId 调用签名方法时传入的会话ID
     **/
    @Override
    public void keySignNeedConfirm(String sessionId) {
    }

    /**
     * 签名过程中，用户点击key设备“OK”键，完成对签名信息核对的事件
     *
     * @param sessionId 调用签名方法时传入的会话ID
     **/
    @Override
    public void keySignDidConfirm(String sessionId) {
    }

    /**
     * 签名过程中，用户点击key设备“C”键，取消签名操作的事件
     *
     * @param sessionId 调用签名方法时传入的会话ID
     **/
    @Override
    public void keySignDidCancel(String sessionId) {
    }

    /**
     * 修改PIN码过程中，提示用户对修改PIN码操作进行核对的事件
     *
     * @param remainNumber 剩余的修改PIN码操作可重试次数
     * @param sessionId    调用修改PIN码方法时传入的会话ID
     **/
    @Override
    public void keyModifyPinNeedConfirm(int remainNumber, String sessionId) {
    }

    /**
     * 修改PIN码过程中，用户点击key设备“OK”键，确认修改的事件
     *
     * @param sessionId 调用修改PIN码方法时传入的会话ID
     **/
    @Override
    public void keyModifyPinDidConfirm(String sessionId) {
    }

    /**
     * 修改PIN码过程中，用户点击key设备“C”键，取消修改操作的事件
     *
     * @param sessionId 调用修改PIN码方法时传入的会话ID
     **/
    @Override
    public void keyModifyPinDidCancel(String sessionId) {
    }

    /**
     * 操作PIN码过程中（签名或修改PIN码），当用户PIN码输入错误次数达到预警值时，提示用户点按Key设备“OK“或”C“键的回调
     *
     * @param remainNumber 剩余的PIN码可输入次数
     * @param sessionId    操作PIN码过程中时传入的会话ID
     */
    public void keyPinWarningNeedConfirm(String remainNumber, String sessionId) {
    }

    /**
     * 操作PIN码过程中（签名或修改PIN码），PIN码输入预警提醒时，用户点击key设备“OK”键确认的回调
     *
     * @param sessionId 操作PIN码过程中（签名或修改PIN码）传入的会话ID
     */
    public void keyPinWarningDidConfirm(String sessionId) {
    }

    /**
     * 操作PIN码过程中（签名或修改PIN码），PIN码输入预警提醒时，用户点击key设备“C”键确认的回调
     *
     * @param sessionId 操作PIN码过程中（签名或修改PIN码）传入的会话ID
     */
    public void keyPinWarningDidCancel(String sessionId) {
    }

    /**
     * 操作PIN码过程中（签名),PIN码输入预警提醒时,出现错误的回调（如设备断开，没电等情况）
     *
     * @param error 错误Id
     * @param sID   操作PIN码过程中（签名）传入的会话ID
     */
    @Override
    public void keyPinWarningFailWithError(String error, String sID) {

    }
}
