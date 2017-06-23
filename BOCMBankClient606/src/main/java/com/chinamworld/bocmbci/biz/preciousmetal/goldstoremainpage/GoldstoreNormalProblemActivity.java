package com.chinamworld.bocmbci.biz.preciousmetal.goldstoremainpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.preciousmetal.PreciousmetalBaseActivity;

public class GoldstoreNormalProblemActivity extends PreciousmetalBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goldstore_normal_problem);
        setTitle("帮助");
        TextView  metalRight=(TextView)findViewById(R.id.ib_top_right_btn);
        metalRight.setVisibility(View.GONE);
        TextView back = (TextView)findViewById(R.id.ib_back);
        // back.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
//                Intent intent=new Intent(GoldstoreNormalProblemActivity.this, GoldstoreMainActivity.class);
//                startActivity(intent);
                finish();
            }
        });
    }
}
