package com.chinamworld.bocmbci.biz.preciousmetal.goldstoremainpage;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.biz.preciousmetal.PreciousmetalBaseActivity;

/**
 * 未开通网银的界面*/
public class NotContentNetBankActivity extends PreciousmetalBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_content_net_bank);
        TextView back = (TextView)findViewById(R.id.ib_back);
        // back.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                ActivityTaskManager.getInstance().removeAllSecondActivity();
            }
        });
        //未关联网银点击确定进入贵金属首页
        Button sure_button=(Button)findViewById(R.id.sure_button);
        sure_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityTaskManager.getInstance().removeAllSecondActivity();
            }
        });

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
            ActivityTaskManager.getInstance().removeAllSecondActivity();
        }

        return super.onKeyDown(keyCode, event);

    }
}
