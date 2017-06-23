package com.boc.bocsoft.mobile.bocmobile.base.widget.edittext;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.BaseDialog;


/**
 * Created by wangf on 2016/5/31.
 */
public class ClipBoardWidget extends BaseDialog implements View.OnClickListener {


    private View contentView;

    /**
     * 待复制的内容
     */
    private String strCopyText;

    /**
     * 待复制内容的TextView
     */
    private TextView tvClipBoard;
    /**
     * 确认按钮
     */
    private TextView tvSubmit;


    public ClipBoardWidget(Context context) {
        super(context);
    }


    public ClipBoardWidget(Context context, String strCopyText) {
        super(context);
        this.strCopyText = strCopyText;
        setData();
    }

    /**
     * 页面控件的初始化
     */
    @Override
    protected void initView() {
        tvClipBoard = (TextView) contentView.findViewById(R.id.tv_clipboard);
        tvSubmit = (TextView) contentView.findViewById(R.id.tv_clipboard_submit);
    }


    @Override
    protected View onAddContentView() {
        contentView = inflateView(R.layout.boc_clipboard_view);
        return contentView;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        tvSubmit.setOnClickListener(this);
    }


    /**
     * 设置数据
     */
    private void setData() {
        tvClipBoard.setText(strCopyText);
    }


    /**
     * 设置复制按钮是否显示
     * @param visibility
     */
    public ClipBoardWidget setCopyBtnVisibility(boolean visibility){
        if (visibility){
            tvSubmit.setVisibility(View.VISIBLE);
        }else{
            tvSubmit.setVisibility(View.GONE);
        }
        return this;
    }


    /**
     * 复制数据到系统剪贴板
     *
     * @param context  上下文对象
     * @param copyText 待复制的内容
     */
    private void copyData(Context context, String copyText) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", copyText);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.tv_clipboard_submit) {
            copyData(mContext, strCopyText);
            dismiss();

        }

    }
}
