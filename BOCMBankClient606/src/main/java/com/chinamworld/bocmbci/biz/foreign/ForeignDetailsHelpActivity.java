package com.chinamworld.bocmbci.biz.foreign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.foreign.ForeignBaseActivity;
import com.chinamworld.bocmbci.log.LogGloble;

/**
 * 外汇买卖 行情帮助页
 * 功能外置 @see ForeignDetailsHelpActivity
 * @author luqp 2016年9月22日
 */
public class ForeignDetailsHelpActivity extends ForeignBaseActivity implements View.OnClickListener {
    private final String TAG = "ForeignDetailsHelpActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foreign_price_details_help);
        clickIncident(); // title点击事件处理
    }

    /** Title点击事件统一处理*/
    public void clickIncident(){
//        getControls().setTitleText("帮助");
//        getControls().setShareVisibility(View.GONE); // 设置右边分享图标隐藏
//        getControls().setMoreVisibility(View.GONE); // 设置右边图标隐藏
    }

    public void onClick(View view){
        switch (view.getId()){
//            case R.id.foreign_landing: //登录
//                break;
//            default:
//                break;
        }
    }
}
