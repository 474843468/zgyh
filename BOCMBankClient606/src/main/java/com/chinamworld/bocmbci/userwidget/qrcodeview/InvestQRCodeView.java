package com.chinamworld.bocmbci.userwidget.qrcodeview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.TwoDimentionCodeUtils;

/**
 * 理财二维码页面控件
 * Created by linyl on 2016/11/7.
 */
public class InvestQRCodeView extends LinearLayout{

    private Context mcontext;
    private View rootView;
    private TextView tv_qrCodeTitle;
    private ImageView ima_qrCode;

    public InvestQRCodeView (Context context){
        super(context);
        initView(context);
    }

    public InvestQRCodeView (Context context, AttributeSet attrs){
        super(context,attrs);
        initView(context);
    }

    private void initView(Context context){
        this.mcontext = context;
        LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(param);
        LayoutInflater inflater = LayoutInflater.from(context);
        rootView = inflater.inflate(R.layout.invest_qrcode_layout, this, false);
        this.addView(rootView);
        tv_qrCodeTitle = (TextView) rootView.findViewById(R.id.tv_qrcode_title);
        ima_qrCode = (ImageView) rootView.findViewById(R.id.iv_qrcode);

    }

    /**
     * 设置二维码小标题信息
     * @param str 设置二维码页面 产品名称与产品Code
     */
    public void setQRTitle(String str){
        if(StringUtil.isNullOrEmpty(str)){
            tv_qrCodeTitle.setVisibility(View.INVISIBLE);
        }else{
            tv_qrCodeTitle.setText(str);
        }
    }

    /**
     * 设置二维码图片信息
     * @param codestr  对应二维码CODE的字符串信息
     */
    public void setQRCodeInfo(String codestr){
        ima_qrCode.setImageBitmap(TwoDimentionCodeUtils.create2DimentionCode(mcontext,codestr));
    }


}
