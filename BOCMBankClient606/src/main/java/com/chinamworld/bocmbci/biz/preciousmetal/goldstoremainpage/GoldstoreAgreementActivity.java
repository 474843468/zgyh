package com.chinamworld.bocmbci.biz.preciousmetal.goldstoremainpage;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.biz.preciousmetal.PreciousmetalBaseActivity;
import com.chinamworld.bocmbci.biz.preciousmetal.PreciousmetalDataCenter;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoldstoreAgreementActivity extends PreciousmetalBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //先调用12号接口，取出银行名称，然后再显示界面元素
        setContentView(R.layout.activity_goldstore_agreement);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //把背景标题栏去掉
        RelativeLayout main_layout=(RelativeLayout) findViewById(R.id.main_layout);
        main_layout.setVisibility(View.GONE);
        TextView jia_name=(TextView)findViewById(R.id.jia_name);
        jia_name.setText(PreciousmetalDataCenter.getInstance().AccountName);
        TextView two=(TextView) findViewById(R.id.two);
        two.setText("中国银行股份有限公司");
        Button knowIt=(Button)findViewById(R.id.sure);
        knowIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }











}
