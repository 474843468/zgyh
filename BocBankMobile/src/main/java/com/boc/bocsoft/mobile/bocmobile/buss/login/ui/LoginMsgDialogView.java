package com.boc.bocsoft.mobile.bocmobile.buss.login.ui;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.GlobalMsgBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.BaseDialog;

import java.util.List;

/**
 * Created by feib on 16/7/25.
 * 登录强制弹出消息diolog
 */
public class LoginMsgDialogView extends BaseDialog implements
        View.OnClickListener {
    protected TextView titleTv;
    protected TextView noticeTv;
    protected Button beforBtn;
    protected Button nextBtn;
    protected Button confirmBtn;
    private View rootView;
    private List<GlobalMsgBean> globalMsgList;
    protected Context mContext;
    /**
     * 消息条数
     */
    private int messageSize;
    /**
     * 消息索引
     */
    private int messageIndex;

    public LoginMsgDialogView(Context context) {
        super(context);
        setCanceledOnTouchOutside(false);
    }

    public void setMsgData(List<GlobalMsgBean> globalMsg) {
        globalMsgList = globalMsg;
        disPlayMsg();
    }

    @Override
    protected View onAddContentView() {
        rootView = inflater.inflate(R.layout.boc_dialog_login_msg, null);
        return rootView;
    }

    @Override
    protected void initView() {
        titleTv = (TextView) rootView.findViewById(R.id.titleTv);
        noticeTv = (TextView) rootView.findViewById(R.id.noticeTv);
        beforBtn = (Button) rootView.findViewById(R.id.beforBtn);
        nextBtn = (Button) rootView.findViewById(R.id.nextBtn);
        confirmBtn = (Button) rootView.findViewById(R.id.confirmBtn);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        beforBtn.setOnClickListener(LoginMsgDialogView.this);
        nextBtn.setOnClickListener(LoginMsgDialogView.this);
        confirmBtn.setOnClickListener(LoginMsgDialogView.this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.beforBtn) {
            diaplayMessageByIndex(messageIndex,-1);
        } else if (view.getId() == R.id.nextBtn) {
            diaplayMessageByIndex(messageIndex,1);
        } else if (view.getId() == R.id.confirmBtn) {
            this.dismiss();
            btnClickCallBack.onConfirmBtnClick(view);
        }
    }

    /**
     * 展示必读消息
     */
    private void disPlayMsg() {
        if (null == globalMsgList||0==globalMsgList.size()) {
            return;
        } else {
            messageSize = globalMsgList.size();
        }

        if (1 == messageSize) {
            beforBtn.setVisibility(View.GONE);
            nextBtn.setVisibility(View.GONE);
            confirmBtn.setVisibility(View.VISIBLE);

        } else {
            beforBtn.setVisibility(View.GONE);
            nextBtn.setVisibility(View.VISIBLE);
            confirmBtn.setVisibility(View.GONE);
        }
        diaplayFirstMessage();
    }


    /**
     * 展示第一条信息
     */
    private void diaplayFirstMessage() {
        messageIndex = 0;
        GlobalMsgBean needReadMessage = globalMsgList.get(0);
        titleTv.setText(needReadMessage.getSubject());
        noticeTv.setText(needReadMessage.getContent());
    }

    /**
     * 展示上一条或下一条信息
     * @param index
     * @param increment 增量
     */
    private void diaplayMessageByIndex(int index,int increment){
        if (0<=index+increment&&index+increment<=messageSize-1) {
            messageIndex=index+increment;
            GlobalMsgBean needReadMessage = globalMsgList.get(messageIndex);
            titleTv.setText(needReadMessage.getSubject());
            noticeTv.setText(needReadMessage.getContent());
        }

        if(2==messageSize){
            if(0==messageIndex){
                beforBtn.setVisibility(View.GONE);
                nextBtn.setVisibility(View.VISIBLE);
                confirmBtn.setVisibility(View.GONE);
            }else{
                beforBtn.setVisibility(View.VISIBLE);
                nextBtn.setVisibility(View.GONE);
                confirmBtn.setVisibility(View.VISIBLE);
            }

        }else{
            if(0==messageIndex){
                beforBtn.setVisibility(View.GONE);
                nextBtn.setVisibility(View.VISIBLE);
                confirmBtn.setVisibility(View.GONE);
            }else if(messageIndex==messageSize-1){
                beforBtn.setVisibility(View.VISIBLE);
                nextBtn.setVisibility(View.GONE);
                confirmBtn.setVisibility(View.VISIBLE);
            }else{
                beforBtn.setVisibility(View.VISIBLE);
                nextBtn.setVisibility(View.VISIBLE);
                confirmBtn.setVisibility(View.GONE);
            }
        }
    }

    private DialogBtnClickCallBack btnClickCallBack;

    /**
     * 设置按钮点击监听
     *
     * @param callBack
     */
    public void setDialogBtnClickListener(DialogBtnClickCallBack callBack) {
        btnClickCallBack = callBack;
    }

    public interface DialogBtnClickCallBack {
        public void onConfirmBtnClick(View view);
    }
}
