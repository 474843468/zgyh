package com.boc.bocsoft.mobile.bocmobile.base.widget.edittext;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.BaseDialog;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.io.UnsupportedEncodingException;

/**
 * 对话框中的输入框，可以判断输入的字数的多少
 * Created by wangf on 2016/5/24.
 */
public class EditDialogWidget extends BaseDialog implements View.OnClickListener {

    private Context mContext;

    private View contentView;
    /**
     * 自定义的输入框
     */
    private ClearEditText mClearEditText;
    /**
     * 显示已经输入数量的TextView
     */
    private TextView textViewInput;
    /**
     * 显示允许输入数量的TextView
     */
    private TextView textViewAll;
    /**
     * 确定按钮
     */
    private TextView textViewSubmit;
    /**
     * 取消按钮
     */
    private TextView textViewCancel;
    /**
     * 允许输入的最大数量
     */
    private int iAllNumPermit;

    /**
     * 是否按照中文字符长度计算（一个汉字按两个字符计算）
     */
    private boolean isChineseCharLen = false;

    private EditDialogCallBack dialogCallBack;
    private TextView tv_division;


    /**
     * 构造方法
     *
     * @param context 上下文对象
     */
    public EditDialogWidget(Context context) {
        super(context);
        mContext = context;
        setData();
    }

    /**
     * 构造方法
     *
     * @param context       上下文对象
     * @param iAllNumPermit 允许输入的最大数量
     */
    public EditDialogWidget(Context context, int iAllNumPermit) {
        super(context);
        mContext = context;
        this.iAllNumPermit = iAllNumPermit;
        setData();
    }


    /**
     * 构造方法
     *
     * @param context          上下文对象
     * @param iAllNumPermit    允许输入的最大数量
     * @param isChineseCharLen 一个汉字是否按两个字符计算
     */
    public EditDialogWidget(Context context, int iAllNumPermit, boolean isChineseCharLen) {
        super(context);
        mContext = context;
        this.iAllNumPermit = iAllNumPermit;
        this.isChineseCharLen = isChineseCharLen;
        this.setCanceledOnTouchOutside(false);
        setData();
    }

    @Override
    protected View onAddContentView() {
        contentView = inflateView(R.layout.boc_dialog_edit_view);
        return contentView;
    }

    public TextView getTextViewDivision() {
        return tv_division;
    }

    /**
     * 初始化页面控件
     */
    protected void initView() {
        mClearEditText = (ClearEditText) contentView.findViewById(R.id.dialog_edit_context);
        textViewAll = (TextView) contentView.findViewById(R.id.tv_num_all);
        textViewInput = (TextView) contentView.findViewById(R.id.tv_num_input);
        tv_division = (TextView) contentView.findViewById(R.id.tv_division);
        textViewSubmit = (TextView) contentView.findViewById(R.id.dialog_edit_submit);
        textViewCancel = (TextView) contentView.findViewById(R.id.dialog_edit_cancel);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        mClearEditText.addTextChangedListener(new TextChangeListener());
        textViewSubmit.setOnClickListener(this);
        textViewCancel.setOnClickListener(this);
    }

    public void setRightStyle(int backgroundColor,int textColor){
        textViewSubmit.setBackgroundColor(backgroundColor);
        textViewSubmit.setTextColor(textColor);
    }

    /**
     * 为组件设置数据
     */
    private void setData() {
        int inputNum = 0;
        if (isChineseCharLen) {
            inputNum = StringUtils.getStringLength(mClearEditText.getText().toString());
        } else {
            inputNum = mClearEditText.getText().toString().length();
        }
        textViewInput.setText(inputNum + "");
        textViewAll.setText(iAllNumPermit + "");
        if (!isChineseCharLen) {
            //设置输入框允许输入的最大数量
            mClearEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(iAllNumPermit)});
        }
    }

    /**
     * 获取输入框控件
     *
     * @return
     */
    public ClearEditText getClearEditText() {
        return mClearEditText;
    }


    /**
     * 设置输入框允许输入字符的最大数量
     *
     * @param iAllNumPermit
     */
    public void setIAllNumPermit(int iAllNumPermit) {
        this.iAllNumPermit = iAllNumPermit;
        textViewAll.setText("" + iAllNumPermit);
        if (!isChineseCharLen) {
            //设置输入框允许输入的最大数量
            mClearEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(iAllNumPermit)});
        }
    }


    /**
     * 为输入框组件中的输入框设置初始化内容
     *
     * @param strClearTextContent
     */
    public void setClearEditTextContent(String strClearTextContent) {
        if (StringUtils.isEmpty(strClearTextContent)) {
            return;
        }
        mClearEditText.setText(strClearTextContent);
        //将光标放在EditText中文字的末尾处
        mClearEditText.setSelection(mClearEditText.getText().toString().length());

        int inputNum = 0;
        if (isChineseCharLen) {
            inputNum = StringUtils.getStringLength(strClearTextContent);
        } else {
            inputNum = strClearTextContent.length();
        }
        textViewInput.setText(inputNum + "");
    }

    public TextView getTextViewInput() {
        return textViewInput;
    }

    public TextView getTextViewAll() {
        return textViewAll;
    }

    /**
     * 设置输入框组件的提示信息
     *
     * @param strHint
     */
    public void setEditWidgetHint(String strHint) {
        if (strHint == null) {
            return;
        }
        mClearEditText.setHint(strHint);
    }

    /**
     * 输入内容中的汉字是否按照一个汉字两个字符计算
     *
     * @param isChineseCharLen
     */
    public void setIsChineseChar(boolean isChineseCharLen) {
        this.isChineseCharLen = isChineseCharLen;
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.dialog_edit_submit) {
            if (dialogCallBack != null) {
                dialogCallBack.onClick(mClearEditText.getText().toString());
            }

        } else if (i == R.id.dialog_edit_cancel) {
            dismiss();

        }
    }

    /**
     * 输入框中输入的内容变化的监听
     */
    class TextChangeListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            int inputNum = 0;
            if (isChineseCharLen) {
                inputNum = StringUtils.getStringLength(s.toString());
                if (inputNum > iAllNumPermit) {
                    String newStr = getAppointLengthString(s.toString(), iAllNumPermit);
                    mClearEditText.setText(newStr);
                    mClearEditText.setSelection(newStr.length());
                    textViewInput.setText(StringUtils.getStringLength(newStr) + "");
                } else {
                    textViewInput.setText(inputNum + "");
                }

            } else {
                inputNum = s.toString().length();
                textViewInput.setText(inputNum + "");
            }

            if (inputNum >= iAllNumPermit) {
                textViewAll.setTextColor(mContext.getResources().getColor(R.color.boc_text_color_red));
            }else{
                textViewAll.setTextColor(mContext.getResources().getColor(R.color.boc_text_color_light_gray));
            }
        }
    }


    /**
     * 回调的初始化
     *
     * @param dialogListener
     */
    public void setEditDialogListener(EditDialogCallBack dialogListener) {
        this.dialogCallBack = dialogListener;
    }

    /**
     * 确定按钮 及 输入 的回调
     */
    public interface EditDialogCallBack {
        void onClick(String strEditTextContent);
    }



    /**
     * 获取文本框内容
     *
     * @return
     */
    public String getStringText() {
        String text = "";
        text = mClearEditText.getText().toString();
        return text;
    }


    /**
     * 截取指定长度字符串，一个中文算两个字符，一个英文算一个字符。如果截取长度将一个中文截取一半，则将这个中文全部截取。例如“我在boc工作”
     * 截取10个字符则是截取半个“作”，次方法截取9个结果为“我在boc工”
     *
     * @param str 要截取的字符串
     * @param len 要截取的长度
     * @return 返回""表示不需要截取，否则返回截取后的字符串
     */
    public static String getAppointLengthString(String str, int len) {
        if (StringUtils.getStringLength(str) <= len) {
            return "";
        }
        byte[] arr = null;// 定义字节数组用于存放字符串转化的字节
        try {
            arr = str.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
        int count = 0;// 定义计数器用于记录从截取位置开始练习的负数个数
        for (int x = len - 1; x >= 0; x--) {
            if (arr[x] < 0)// 如果字节是负数则自增
                count++;
            else
                // 如果不是跳出循环
                break;
        }
        if (count % 2 == 0)
            try {
                return new String(arr, 0, len, "GBK");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }
        else
            try {
                return new String(arr, 0, len - 1, "GBK");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }
    }


}
