package com.chinamworld.bocmbci.biz.preciousmetal.goldstoremainpage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.biz.invest.activity.InvesAgreeFirstActivity;
import com.chinamworld.bocmbci.biz.preciousmetal.PreciousmetalBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.ActivityIntentTools;

/**未开通投资理财的界面*/
public class NotOpenFincActivity extends PreciousmetalBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_open_finc);
        TextView back = (TextView)findViewById(R.id.ib_back);
        // back.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                ActivityTaskManager.getInstance().removeAllSecondActivity();
            }
        });
        TextView metalRight=(TextView)findViewById(R.id.ib_top_right_btn);
        metalRight.setVisibility(View.INVISIBLE);

        Button sure_button=(Button)findViewById(R.id.sure_button);
        sure_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到投资理财服务协议页面
                ActivityIntentTools.intentToActivityForResult(NotOpenFincActivity.this, InvesAgreeFirstActivity.class, ConstantGloble.ACTIVITY_RESULT_CODE,null);
                NotOpenFincActivity.this.overridePendingTransition(R.anim.push_up_in,
                        R.anim.no_animation);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
                switch (resultCode) {
            case RESULT_OK:
                if (requestCode == ConstantGloble.ACTIVITY_RESULT_CODE) {
                    // 开通成功的响应
                    Intent intent=new Intent();
                    intent.setClass(NotOpenFincActivity.this,GoldstoreMainActivity.class);
                    startActivity(intent);
                }else if(requestCode == 100){
                    //开通失败未作处理
                }
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
            ActivityTaskManager.getInstance().removeAllSecondActivity();
        }

        return super.onKeyDown(keyCode, event);

    }
}
