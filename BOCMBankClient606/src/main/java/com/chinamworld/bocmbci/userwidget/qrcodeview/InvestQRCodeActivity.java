package com.chinamworld.bocmbci.userwidget.qrcodeview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.TwoDimentionCodeUtils;

/**
 * 理财二维码页面（公共样式）
 * Created by Administrator on 2016/11/7.
 */
public class InvestQRCodeActivity extends Activity{
    private TextView tvTitle,back;
    private TextView tv_qrCodeTitle;
    private ImageView ima_qrCode;
    private static String codestr,mactivityTitleText,mcontentTitleTxte;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.invest_qrcode_layout);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.help_message_tittlelayout);
        back = (TextView) findViewById(R.id.ib_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tv_qrCodeTitle = (TextView) findViewById(R.id.tv_qrcode_title);
        ima_qrCode = (ImageView) findViewById(R.id.iv_qrcode);
        tvTitle.setText(mactivityTitleText);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(mcontentTitleTxte == null){
            tv_qrCodeTitle.setVisibility(View.INVISIBLE);
        }else{
            tv_qrCodeTitle.setVisibility(View.VISIBLE);
            tv_qrCodeTitle.setText(mcontentTitleTxte);
        }
        ima_qrCode.setImageBitmap(TwoDimentionCodeUtils.create2DimentionCode(this,codestr));

    }

    /**
     * 跳转二维码页面链接
     * @param context 上下文
     * @param activityTitleText 页面标题信息
     * @param qrCodeInfo 二维码文本信息
     * @param contentTitleText 内容标题信息（产品名称和产品代码）---选传（默认隐藏）
     */
    public static void goToInvestQRCodeActivity(Context context,String activityTitleText,String qrCodeInfo,String... contentTitleText){
        Intent intent =new Intent(context,InvestQRCodeActivity.class);
        context.startActivity(intent);
        codestr = qrCodeInfo;
        mactivityTitleText = activityTitleText;
        if(StringUtil.isNullOrEmpty(contentTitleText)|| contentTitleText[0] == ""){
            mcontentTitleTxte = null;
        }else{
            mcontentTitleTxte = contentTitleText[0];
        }
    }

}
