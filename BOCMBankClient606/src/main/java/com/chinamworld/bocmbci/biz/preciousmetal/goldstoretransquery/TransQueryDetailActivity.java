package com.chinamworld.bocmbci.biz.preciousmetal.goldstoretransquery;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.preciousmetal.NewTitleAndContent;
import com.chinamworld.bocmbci.biz.preciousmetal.PreciousmetalBaseActivity;
import com.chinamworld.bocmbci.biz.preciousmetal.PreciousmetalDataCenter;
import com.chinamworld.bocmbci.constant.DictionaryData;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.llbt.userwidget.NewLabelTextView;

import java.util.Map;

/**
 * 贵金属积存 交易查询详情页面
 * Created by linyl on 2016/8/24.
 */
public class TransQueryDetailActivity extends PreciousmetalBaseActivity{
    /**交易金额**/
    LinearLayout ll_traAmount;
    /**动态加载元素**/
    NewTitleAndContent myContainerLayout;
    /**交易金额文本显示**/
    TextView tv_traAmount;
    /**交易克重**/
    NewLabelTextView traWeight_lable_tv;
    /**交易查询接口列表选中项返回数据map**/
    Map<String,Object> traQueryDetailMap;
    /**交易类型**/
    String traType;
    TextView tv_traAmountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goldstore_tranquery_detail);
        getPreviousmeatalBackgroundLayout().setTitleText("详情");
        getPreviousmeatalBackgroundLayout().setMetalRightVisibility(View.GONE);
        ll_traAmount = (LinearLayout) findViewById(R.id.ll_tra_amount);
        tv_traAmount = (TextView) findViewById(R.id.tra_amount);
        tv_traAmountType = (TextView) findViewById(R.id.tv_tra_amount);
        myContainerLayout = (NewTitleAndContent) findViewById(R.id.mycontainerLayout);

        traWeight_lable_tv = (NewLabelTextView) findViewById(R.id.tra_weight);

        traQueryDetailMap = PreciousmetalDataCenter.getInstance().TransQueryDetailMap;
        traType = (String) traQueryDetailMap.get("transType");
//        if(Integer.valueOf(traType.trim()) == 1){//购买
//            tv_traAmountType.setText("购买金额（人民币元）");
//            tv_traAmount.setText(StringUtil.parseStringPattern((String)traQueryDetailMap.get("transAmount"),2));
//        }else if(Integer.valueOf(traType.trim()) == 2){//赎回
//            tv_traAmountType.setText("赎回金额（人民币元）");
//            tv_traAmount.setText(StringUtil.parseStringPattern(String.valueOf(Double.parseDouble((String)traQueryDetailMap.get("transWeight"))*Double.parseDouble((String)traQueryDetailMap.get("price"))),2));
//        }else{//提货
//            ll_traAmount.setVisibility(View.GONE);
//        }
//        traWeight_lable_tv.setLabelText(R.string.traweight);
//        traWeight_lable_tv.setValueText((String) traQueryDetailMap.get("transWeight")+" 克");
        initDetailView(Integer.valueOf(traType.trim()));
    }

    /**
     * 加载详情页面元素
     * @param type
     */
    private void initDetailView(int type){
        switch (type) {
            case 1://购买
                tv_traAmountType.setText("购买金额（人民币元）");
                tv_traAmount.setText(StringUtil.parseStringPattern((String)traQueryDetailMap.get("transAmount"),2));
                traWeight_lable_tv.setLabelText("购买(克)");
                traWeight_lable_tv.setValueText(StringUtil.parseStringPattern((String) traQueryDetailMap.get("transWeight"),4));
                myContainerLayout.addView(createNewLabelTextView(R.string.metal_buyprice,StringUtil.parseStringPattern((String) traQueryDetailMap.get("price"),2) + "元/克"));
                myContainerLayout.addView(createNewLabelTextView(R.string.transType,(String) traQueryDetailMap.get("currCodeName")));
                myContainerLayout.addView(createNewLabelTextView(R.string.jiaoyi_type,"购买交易"));
                myContainerLayout.addView(createNewLabelTextView(R.string.transUnit,
                        DictionaryData.getKeyByValue((String) traQueryDetailMap.get("transUnit"),PreciousmetalDataCenter.transUnitList)));

//                myContainerLayout.addView(createNewLabelTextView(R.string.transDate,(String) traQueryDetailMap.get("transDate")));
                myContainerLayout.addView(createNewLabelTextView(R.string.transTime,(String) traQueryDetailMap.get("transDate") + "  " + (String) traQueryDetailMap.get("transTime")));
                myContainerLayout.addView(createNewLabelTextView(R.string.debitCard,StringUtil.getForSixForString((String) traQueryDetailMap.get("debitCard"))));
                myContainerLayout.addView(createNewLabelTextView(R.string.metal_status,
                        DictionaryData.getKeyByValue((String) traQueryDetailMap.get("status"),PreciousmetalDataCenter.statusList)));
                myContainerLayout.addView(createNewLabelTextView(R.string.chennal,
                        DictionaryData.getKeyByValue((String) traQueryDetailMap.get("chennal"),PreciousmetalDataCenter.chennalList)));
                break;
            case 2://赎回
                tv_traAmountType.setText("赎回金额（人民币元）");
                tv_traAmount.setText(StringUtil.parseStringPattern(String.valueOf(Double.parseDouble((String)traQueryDetailMap.get("transWeight"))*Double.parseDouble((String)traQueryDetailMap.get("price"))),2));
                traWeight_lable_tv.setLabelText("赎回(克)");
                traWeight_lable_tv.setValueText(StringUtil.parseStringPattern((String) traQueryDetailMap.get("transWeight"),4));
                myContainerLayout.addView(createNewLabelTextView(R.string.shuhuiprice,StringUtil.parseStringPattern((String) traQueryDetailMap.get("price"),2) + "元/克"));
                myContainerLayout.addView(createNewLabelTextView(R.string.transType,(String) traQueryDetailMap.get("currCodeName")));
                myContainerLayout.addView(createNewLabelTextView(R.string.jiaoyi_type,"赎回交易"));
//                myContainerLayout.addView(createNewLabelTextView(R.string.transDate,(String) traQueryDetailMap.get("transDate")));
                myContainerLayout.addView(createNewLabelTextView(R.string.transTime,(String) traQueryDetailMap.get("transDate") + "  " + (String) traQueryDetailMap.get("transTime")));
                myContainerLayout.addView(createNewLabelTextView(R.string.debitCard,StringUtil.getForSixForString((String) traQueryDetailMap.get("debitCard"))));
                myContainerLayout.addView(createNewLabelTextView(R.string.metal_status,
                        DictionaryData.getKeyByValue((String) traQueryDetailMap.get("status"),PreciousmetalDataCenter.statusList)));
                myContainerLayout.addView(createNewLabelTextView(R.string.chennal,
                        DictionaryData.getKeyByValue((String) traQueryDetailMap.get("chennal"),PreciousmetalDataCenter.chennalList)));
                break;
            case 3://提货
                ll_traAmount.setVisibility(View.GONE);
//                myContainerLayout.addView(createNewLabelTextView(R.string.varieties,(String) traQueryDetailMap.get("currCodeName")));
                traWeight_lable_tv.setLabelText("提货(克)");
                traWeight_lable_tv.setValueText(StringUtil.parseStringPattern((String) traQueryDetailMap.get("transWeight"),4));
                myContainerLayout.addView(createNewLabelTextView(R.string.transType,(String) traQueryDetailMap.get("currCodeName")));
                myContainerLayout.addView(createNewLabelTextView(R.string.jiaoyi_type,"提货交易"));
//                myContainerLayout.addView(createNewLabelTextView(R.string.transDate,(String) traQueryDetailMap.get("transDate")));
                myContainerLayout.addView(createNewLabelTextView(R.string.transTime,(String) traQueryDetailMap.get("transDate")));
                myContainerLayout.addView(createNewLabelTextView(R.string.debitCard,StringUtil.getForSixForString((String) traQueryDetailMap.get("debitCard"))));
                myContainerLayout.addView(createNewLabelTextView(R.string.metal_status,
                        DictionaryData.getKeyByValue((String) traQueryDetailMap.get("status"),PreciousmetalDataCenter.statusList)));
                myContainerLayout.addView(createNewLabelTextView(R.string.pickupSite,(String) traQueryDetailMap.get("pickupSite")));
                break;
        }
    }


}
