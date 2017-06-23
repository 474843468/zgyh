package com.chinamworld.bocmbci.net.model;


/**
 * 四方公司返回报文结构
 * Created by yuht on 2016/10/19.
 */
public class SFResponseData<T>  extends BaseResponseData{

    /** 报文头 */
    private SFHead head;
    private T body;

    public SFHead getHead() {
        return head;
    }

    public void setHead(SFHead head1) {
        this.head = head1;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }


    @Override
    public boolean isError() {
        return head == null ? false : head.isError();
    }

    @Override
    public String getErrorMessage() {
        return head == null  ? "" : head.getErrorMessage();
    }

    @Override
    public String getErrorCode() {
        return head == null ?"": head.getErrorCode();
    }







}
