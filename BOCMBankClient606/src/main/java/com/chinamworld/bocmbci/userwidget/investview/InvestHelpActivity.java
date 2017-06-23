package com.chinamworld.bocmbci.userwidget.investview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.epay.context.Context;

import org.w3c.dom.Text;

public class InvestHelpActivity extends Activity {
    public static TextView helpMessage;
    private TextView back;
    private static String helpStr;
    private static String mTitleText = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_invest_help);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.help_message_tittlelayout);
        helpMessage=(TextView) findViewById(R.id.helpmessage);
        back=(TextView) findViewById(R.id.ib_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        helpMessage.setText(Html.fromHtml(helpStr));
        if(mTitleText != null)
            ((TextView)findViewById(R.id.tv_title)).setText(mTitleText);

    }

    public static void  showHelpMessage(Activity context,String message){
        Intent intent =new Intent(context,InvestHelpActivity.class);
        context.startActivity(intent);
        helpStr = message;

    }
    public static void  showHelpMessage(Activity context,String message,String titleText){
        mTitleText = titleText;
        showHelpMessage(context,message);
    }
}
