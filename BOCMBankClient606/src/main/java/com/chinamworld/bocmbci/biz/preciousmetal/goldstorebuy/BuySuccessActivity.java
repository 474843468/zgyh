package com.chinamworld.bocmbci.biz.preciousmetal.goldstorebuy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.preciousmetal.PreciousmetalBaseActivity;
import com.chinamworld.bocmbci.biz.preciousmetal.goldstoremainpage.GoldstoreMainActivity;
import com.chinamworld.bocmbci.biz.preciousmetal.goldstoretransquery.TransQuryActivity;
import com.chinamworld.bocmbci.boc.ModelBoc;

/**
 * 贵金属积存 购买成功结果页面
 * Created by linyl on 2016/8/24.
 */
public class BuySuccessActivity extends PreciousmetalBaseActivity implements View.OnClickListener {

    TextView tv_ransomSuccessInfo ,tv_queryDetail;
    Button btn_gomain;
    /**您可能需要**/
    LinearLayout ll_needOne,ll_needTwo,ll_needThr,ll_needfour;
    LinearLayout ll_detailInfo,ll_bankerNo;
    TextView tv_webBankNo,tv_bankNo,tv_storeVarieties,tv_storeType,tv_storeCurCode,tv_buyWay,tv_bankerNO;
    String buyType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goldstore_buy_success);
        getPreviousmeatalBackgroundLayout().setMetalRightVisibility(View.GONE);
        getPreviousmeatalBackgroundLayout().setTitleText("操作结果");
        buyType = this.getIntent().getStringExtra("buyType");
        tv_ransomSuccessInfo = (TextView) findViewById(R.id.tv_goldstore_ransom_success);
        tv_queryDetail = (TextView) findViewById(R.id.tv_query_detail);
        btn_gomain = (Button) findViewById(R.id.btn_goto_main);
        ll_detailInfo = (LinearLayout) findViewById(R.id.goldstore_branch_success_detail_lv);
        ll_needOne = (LinearLayout) findViewById(R.id.ransom_need_one);
        ll_needTwo = (LinearLayout) findViewById(R.id.ransom_need_two);
        ll_needThr = (LinearLayout) findViewById(R.id.ransom_need_thr);
        ll_needfour = (LinearLayout) findViewById(R.id.ransom_need_four);
        tv_webBankNo = (TextView) findViewById(R.id.goldstore_branch_success_webtranno);
        tv_bankNo = (TextView) findViewById(R.id.goldstore_branch_success_bankno);
        tv_storeVarieties = (TextView) findViewById(R.id.goldstore_branch_success_pinzhong);
        tv_storeType = (TextView) findViewById(R.id.goldstore_branch_success_leixing);
        tv_storeCurCode = (TextView) findViewById(R.id.goldstore_branch_success_bizhong);
        tv_buyWay = (TextView) findViewById(R.id.goldstore_branch_success_buytype);
        tv_bankerNO = (TextView) findViewById(R.id.goldstore_branch_success_yuangong_no);
        ll_bankerNo = (LinearLayout) findViewById(R.id.ll_bankerNo);
        if("weight".equals(buyType)){//按克重
            tv_ransomSuccessInfo.setText("恭喜您，成功购买"+ this.getIntent().getStringExtra("varietiesName")+this.getIntent().getStringExtra("buyNumStr") +"克");
        }else if("amount".equals(buyType)){//按金额
            tv_ransomSuccessInfo.setText("恭喜您，成功购买"+ this.getIntent().getStringExtra("varietiesName")+this.getIntent().getStringExtra("buyNumStr")+"元");
        }
        tv_queryDetail.setOnClickListener(this);
        btn_gomain.setOnClickListener(this);
        ll_needOne.setOnClickListener(this);
        ll_needTwo.setOnClickListener(this);
        ll_needThr.setOnClickListener(this);
        ll_needfour.setOnClickListener(this);

        /**返回事件 跳转到首页**/
        getPreviousmeatalBackgroundLayout().setMetalBackonClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                goGoldstoreMain();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_query_detail://查看详情
                tv_queryDetail.setVisibility(View.GONE);
                ll_detailInfo.setVisibility(View.VISIBLE);
                tv_webBankNo.setText(this.getIntent().getStringExtra("transactionId"));
                tv_bankNo.setText(this.getIntent().getStringExtra("accountNum"));
                tv_storeVarieties.setText(this.getIntent().getStringExtra("varietiesName"));
                tv_storeType.setText(this.getIntent().getStringExtra("storeType"));
                tv_storeCurCode.setText(this.getIntent().getStringExtra("currency"));
                tv_buyWay.setText("amount".equals(buyType) ? "按金额" : "按克重");
                if(this.getIntent().getStringExtra("bankerNo") == null){
//                    ll_bankerNo.setVisibility(View.GONE);
                    tv_bankerNO.setText("");
                }else{
                    tv_bankerNO.setText(this.getIntent().getStringExtra("bankerNo"));
                }
                break;
            case R.id.btn_goto_main://返回主页  返回到手机银行主页面
//                ActivityTaskManager.getInstance().removeAllSecondActivity();
//                ActivityIntentTools.intentToActivityByClassName(this, "com.chinamworld.bocmbci.biz.MainActivity");
//                finish();
                /**返回行方首页**/
//                ActivityTaskManager.getInstance().removeAllActivity();
//                finish();
                goToMainActivity();
                finish();
                break;
            case R.id.ransom_need_one://继续购买
                Intent intentRansom = new Intent(this,BuyMainActivity.class);
                if("amount".equals(buyType)){
                    intentRansom.putExtra("buyTypeSuccess","amount");
                }
                startActivity(intentRansom);
//                finish();
                break;
            case R.id.ransom_need_two://查看持仓
                Intent intentGoldStore = new Intent(this, GoldstoreMainActivity.class);
                startActivity(intentGoldStore);
//                finish();
                break;
            case R.id.ransom_need_thr://查看账户余额
                Intent intentGoldStoreTwo = new Intent(this, GoldstoreMainActivity.class);
                startActivity(intentGoldStoreTwo);
                ModelBoc.gotoAccountDetailActivity(this,this.getIntent().getStringExtra("accountId"));
//                finish();
                break;
            case R.id.ransom_need_four://查看交易记录
                Intent intentTranquery = new Intent(this, TransQuryActivity.class);
                intentTranquery.putExtra("queryType","buy");
                startActivity(intentTranquery);
//                finish();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        goGoldstoreMain();
    }
}
