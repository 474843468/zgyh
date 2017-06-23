package com.chinamworld.bocmbci.biz.preciousmetal.goldstorebuy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.GoldBonus;
import com.chinamworld.bocmbci.biz.preciousmetal.PreciousmetalBaseActivity;
import com.chinamworld.bocmbci.biz.preciousmetal.PreciousmetalDataCenter;
import com.chinamworld.bocmbci.biz.preciousmetal.goldstoreaccount.GoldstoreAccountResetActivity;
import com.chinamworld.bocmbci.boc.ModelBoc;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.llbt.userwidget.dialogview.DialogButtonType;
import com.chinamworld.llbt.userwidget.dialogview.MessageDialog;
import com.chinamworld.llbt.userwidget.dialogview.UserDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 贵金属积存 购买主页面
 * Created by linyl on 2016/8/24.
 */
public class BuyMainActivity extends PreciousmetalBaseActivity
        implements View.OnClickListener {

    Button btn_Next;
    TextView tv_byWeight,tv_byAmount,tv_buyprice,tv_bankNo,tv_upDate,tv_addNo,tv_update,tv_wenxinInfo,tv_selStoreType;
    ImageView ima_DOWN,ima_UP;
    ImageView ima_weightLine,ima_amountLine;
    EditText et_BuyNum;
    /**提示信息 购买的克数、金额、余额不足去转账**/
    TextView tv_Info,tv_zhuanzhuang;
    LinearLayout ll_zhuanzhang;
    /**购买方式**/
    String buyType = "weight";
    /**积存账户信息 cusinfo **/
    Map<String,Object> accountCusInfo;
    Map<String,Object> storeList;
    /**账号 464 显示 **/
    String accountNum = null;
    //人民币子账户集合
    List<Map<String, Object>> rmbAccountList;
    /**步长  金额  克重 **/
    String amountStep,weightStep;
    /**最大购买数量**/
    String maxBuyAmoubtStr,maxBuyWeightStr;
    /**判断是否首次点 +/- 便于自动矫正输入框的信息  true 是；false 否**/
    boolean isFirst = true;//默认是首次点击
    /**是否有输入银行员工号  true 有**/
    boolean Flag = false;
    /**银行员工号输入框**/
    EditText et_bnakerNo;
    View bankerNoView;
    String bankerNoStr;
    LinearLayout ll_update;
    ImageView ima_update;
    View.OnClickListener listener;
    /**积存类型list数据**/
    List<Map<String, Object>> listmap;
    ImageView ima_right;
    //要弹出的对话框
    View dialogView;
    ListView listView;
    CustomDialog customDialog;
    Double mAmount,mWeight,et_Num;
    private RelativeLayout relativeLayout7;
    String etWeight_last = "1";
    String etAmount_last = "300";
    String str_selVarietiesName;
    String str_currCode;
    String accountID;
    ScrollView rootView;
    String mcurrcode = PreciousmetalDataCenter.getInstance().CODE;
    /**从转账模块跳到购买页面**/
//    boolean FromTransFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_main);
        getPreviousmeatalBackgroundLayout().setTitleText("购买");
        getPreviousmeatalBackgroundLayout().setMetalRightVisibility(View.GONE);

        /**返回事件 跳转到首页**/
        getPreviousmeatalBackgroundLayout().setMetalBackonClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                goGoldstoreMain();
            }
        });
        relativeLayout7=(RelativeLayout)findViewById(R.id.relativeLayout7);
        rootView = (ScrollView) findViewById(R.id.sc_rootview);
        btn_Next = (Button) findViewById(R.id.button2);
        tv_byWeight = (TextView) findViewById(R.id.textView16);
        tv_byAmount = (TextView) findViewById(R.id.textView17);
        ima_DOWN = (ImageView) findViewById(R.id.imageView4);
        ima_UP = (ImageView) findViewById(R.id.imageView5);
        et_BuyNum = (EditText) findViewById(R.id.et_buyNum);
        tv_Info = (TextView) findViewById(R.id.textView15);
        tv_buyprice = (TextView) findViewById(R.id.textView19);
        tv_bankNo = (TextView) findViewById(R.id.textView22);
        tv_upDate = (TextView) findViewById(R.id.textView23);
        tv_addNo = (TextView) findViewById(R.id.textView24);
        ll_update = (LinearLayout) findViewById(R.id.ll_update);
        ima_update = (ImageView) findViewById(R.id.ima_update);
        tv_update = (TextView) findViewById(R.id.tv_banker_update);
        tv_wenxinInfo = (TextView) findViewById(R.id.textView27);
        ima_weightLine = (ImageView) findViewById(R.id.ima_byweighttline);
        ima_amountLine = (ImageView) findViewById(R.id.ima_byamountline);
        ll_zhuanzhang = (LinearLayout) findViewById(R.id.ll_zhuanzhang);
        tv_zhuanzhuang = (TextView) findViewById(R.id.tv_zhuanzhang);
        ima_right = (ImageView) findViewById(R.id.ima_right);
        tv_selStoreType = (TextView) findViewById(R.id.tv_storetype_select);
        //设置输入框光标至末尾
        et_BuyNum.setSelection(et_BuyNum.getText().toString().length());

        btn_Next.setOnClickListener(this);
        tv_byWeight.setOnClickListener(this);
        tv_byAmount.setOnClickListener(this);
        ima_UP.setOnClickListener(this);
        ima_DOWN.setOnClickListener(this);
        tv_upDate.setOnClickListener(this);
        tv_zhuanzhuang.setOnClickListener(this);
        rootView.setOnTouchListener(touchListener);

        if("amount".equals(this.getIntent().getStringExtra("buyTypeSuccess"))){
            tv_byAmount.performClick();
        }

        /**自动弹出软键盘**/
        autoShowSoftInput(et_BuyNum);

//        LoadingDialog.showLoadingDialog(this);
        requsetPsnGoldStoreAccountQuery();
        BaseHttpEngine.showProgressDialogCanGoBack();
        listmap = PreciousmetalDataCenter.getInstance().GoodsListT;
        /**无牌价的情况**/
        if(StringUtil.isNullOrEmpty(listmap) || StringUtil.isNullOrEmpty(PreciousmetalDataCenter.getInstance().LOGINPRICEMAP)) {
            ima_right.setVisibility(View.INVISIBLE);
            tv_selStoreType.setText("-");
            accountCusInfo =  (Map<String,Object>) PreciousmetalDataCenter.getInstance().ACCOUNTQUERYMAP.get("cusInfo");
            accountNum = StringUtil.getForSixForString((String) (accountCusInfo).get("accountNum"));
            tv_bankNo.setText(accountNum);
            tv_addNo.setText(getClickableSpan());
            tv_addNo.setMovementMethod(LinkMovementMethod.getInstance());
            DataCheck();
            return;
        }
        if(listmap.size()<=1){
            ima_right.setVisibility(View.INVISIBLE);
        }else{
            ima_right.setVisibility(View.VISIBLE);
//            relativeLayout7.setOnClickListener(imaRightListener);
            ima_right.setOnClickListener(imaRightListener);
        }

        /**监听输入框的信息变化 动态展示提示信息 最大购买数量 和 余额不足 去转账**/
        et_BuyNum.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                //比较输入框的数量与最大购买数量的大小

                if("".equals(et_BuyNum.getText().toString().trim())){
                    et_Num = Double.valueOf("0");
                    ima_DOWN.setClickable(false);
                    ima_DOWN.setImageDrawable(getResources().getDrawable(R.drawable.goldstore_jian));
                }else{
                    et_Num = Double.parseDouble(et_BuyNum.getText().toString().trim());
                }
                //动态监控加减状态
                if("weight".equals(buyType)){//按克重
                    if(et_Num <= 1){
                        ima_DOWN.setClickable(false);
                        ima_DOWN.setImageDrawable(getResources().getDrawable(R.drawable.goldstore_jian));
                    }else{
                        ima_DOWN.setClickable(true);
                        ima_DOWN.setImageDrawable(getResources().getDrawable(R.drawable.goldstore_jian_one));
                    }
                    //输入框信息与最大购买提示信息联动
                    if(StringUtil.isNullOrEmpty(PreciousmetalDataCenter.getInstance().availbalance)){
                        tv_Info.setVisibility(View.GONE);
                        ll_zhuanzhang.setVisibility(View.VISIBLE);
                    } else {
                        String maxBuyWeight = String.valueOf((long) ((Double.parseDouble(PreciousmetalDataCenter.getInstance()
                                .availbalance) / Double.parseDouble(tv_buyprice.getText().toString()))));
                        if (StringUtil.isNullOrEmpty(maxBuyWeight) || Double.parseDouble(maxBuyWeight) < 1) {//余额不足
                            tv_Info.setVisibility(View.GONE);
                            ll_zhuanzhang.setVisibility(View.VISIBLE);
                        } else {
                            tv_Info.setVisibility(View.VISIBLE);
                            ll_zhuanzhang.setVisibility(View.GONE);
                            //计算最大可购买的克数  回显
                            long j = (long) ((Double.parseDouble(maxBuyWeight) - 1) / Double.parseDouble(weightStep));
                            Double maxBuyWeight_D = 1 + j * Double.parseDouble(weightStep);
                            maxBuyWeightStr = String.valueOf(maxBuyWeight_D);
                            tv_Info.setText("最多可购买" + StringUtil.parseStringPattern(maxBuyWeightStr, 0) + " 克");
                            mWeight = Double.parseDouble(StringUtil.append2Decimals(maxBuyWeightStr, 4));
                            if (et_Num > mWeight) {
                                tv_Info.setVisibility(View.GONE);
                                ll_zhuanzhang.setVisibility(View.VISIBLE);
                            } else {
                                tv_Info.setVisibility(View.VISIBLE);
                                ll_zhuanzhang.setVisibility(View.GONE);
                            }
                        }
                    }
                    /**加号限制点击**/
                    if(et_Num >= ((long)((long)((9999999999999L- 1)/Double.parseDouble(weightStep)) * Double.parseDouble(weightStep)) + 1)){//加号置灰不可点击
                        ima_UP.setImageDrawable(getResources().getDrawable(R.drawable.goldstore_add_one));
                        ima_UP.setClickable(false);
                    }else{
                        ima_UP.setImageDrawable(getResources().getDrawable(R.drawable.goldstore_add));
                        ima_UP.setClickable(true);
                    }
                }else if("amount".equals(buyType)){//按金额
                    if(et_Num <= 300){
                        ima_DOWN.setClickable(false);
                        ima_DOWN.setImageDrawable(getResources().getDrawable(R.drawable.goldstore_jian));
                    }else{
                        ima_DOWN.setClickable(true);
                        ima_DOWN.setImageDrawable(getResources().getDrawable(R.drawable.goldstore_jian_one));
                    }
                    //输入框信息与最大购买提示信息联动
                    if(StringUtil.isNullOrEmpty(rmbAccountList) ||
                            StringUtil.isNullOrEmpty(PreciousmetalDataCenter.getInstance().availbalance) ||
                            Double.valueOf(PreciousmetalDataCenter.getInstance().availbalance) < 300){//余额不足
                        tv_Info.setVisibility(View.GONE);
                        ll_zhuanzhang.setVisibility(View.VISIBLE);
                    }else {
                        tv_Info.setVisibility(View.VISIBLE);
                        ll_zhuanzhang.setVisibility(View.GONE);
                        //最多可购买多少元  （人民币余额-起购数）/步长
                        long i = (long)(((Double.parseDouble(PreciousmetalDataCenter.getInstance().availbalance)-300)/Double.parseDouble(amountStep)));
                        Double maxBuyAmount = 300 + i * Double.parseDouble(amountStep);
                        maxBuyAmoubtStr = String.valueOf(maxBuyAmount);
                        tv_Info.setText("最多可购买" + StringUtil.parseStringPattern(maxBuyAmoubtStr,0) + " 元");
                        mAmount = Double.parseDouble(StringUtil.append2Decimals(maxBuyAmoubtStr,2));
                        if(et_Num > mAmount){
                            tv_Info.setVisibility(View.GONE);
                            ll_zhuanzhang.setVisibility(View.VISIBLE);
                        }else{
                            tv_Info.setVisibility(View.VISIBLE);
                            ll_zhuanzhang.setVisibility(View.GONE);
                        }
                    }
                    /**加号限制点击**/
                    if(et_Num >= ((long)(((long)((999999999999999L-300)/Double.parseDouble(amountStep))) * Double.parseDouble(amountStep)) + 300)){//加号置灰不可点击
                        ima_UP.setImageDrawable(getResources().getDrawable(R.drawable.goldstore_add_one));
                        ima_UP.setClickable(false);
                    }else{
                        ima_UP.setImageDrawable(getResources().getDrawable(R.drawable.goldstore_add));
                        ima_UP.setClickable(true);
                    }
                }

                //设置输入框光标至末尾
                et_BuyNum.setSelection(et_BuyNum.getText().toString().length());
            }
        });



    }

    /**
     * 点击屏幕 收起键盘
     */
    View.OnTouchListener touchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            InputMethodManager imm = (InputMethodManager) BuyMainActivity.this
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(et_BuyNum.getWindowToken(), 0);
            return false;
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        /**从账户重设进购买主页**/
        requsetPsnGoldStoreAccountQuery();

        listmap = PreciousmetalDataCenter.getInstance().GoodsListT;
        if(StringUtil.isNullOrEmpty(listmap) || StringUtil.isNullOrEmpty(PreciousmetalDataCenter.getInstance().LOGINPRICEMAP)) {
            ima_right.setVisibility(View.INVISIBLE);
            tv_selStoreType.setText("-");
            accountCusInfo =  (Map<String,Object>) PreciousmetalDataCenter.getInstance().ACCOUNTQUERYMAP.get("cusInfo");
            accountNum = StringUtil.getForSixForString((String) (accountCusInfo).get("accountNum"));
            tv_bankNo.setText(accountNum);
            tv_addNo.setText(getClickableSpan());
            tv_addNo.setMovementMethod(LinkMovementMethod.getInstance());
            DataCheck();
            return;
        }
        if(listmap.size()<=1){
            ima_right.setVisibility(View.INVISIBLE);
        }else{
            ima_right.setVisibility(View.VISIBLE);
//            relativeLayout7.setOnClickListener(imaRightListener);
            ima_right.setOnClickListener(imaRightListener);
        }

        /**监听输入框的信息变化 动态展示提示信息 最大购买数量 和 余额不足 去转账**/
        et_BuyNum.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                refershEtBuyNumInfo();
            }
        });
    }

    /**
     * 监听输入框变化 刷新页面信息
     */
    private void refershEtBuyNumInfo() {
        //比较输入框的数量与最大购买数量的大小

        if("".equals(et_BuyNum.getText().toString().trim())){
            et_Num = Double.valueOf("0");
            ima_DOWN.setClickable(false);
            ima_DOWN.setImageDrawable(getResources().getDrawable(R.drawable.goldstore_jian));
        }else{
            et_Num = Double.parseDouble(et_BuyNum.getText().toString().trim());
        }
        //动态监控加减状态
        if("weight".equals(buyType)){//按克重
            if(et_Num <= 1){
                ima_DOWN.setClickable(false);
                ima_DOWN.setImageDrawable(getResources().getDrawable(R.drawable.goldstore_jian));
            }else{
                ima_DOWN.setClickable(true);
                ima_DOWN.setImageDrawable(getResources().getDrawable(R.drawable.goldstore_jian_one));
            }
            //输入框信息与最大购买提示信息联动
            if(StringUtil.isNullOrEmpty(PreciousmetalDataCenter.getInstance().availbalance)){
                tv_Info.setVisibility(View.GONE);
                ll_zhuanzhang.setVisibility(View.VISIBLE);
            } else {
                String maxBuyWeight = String.valueOf((int) ((Double.parseDouble(PreciousmetalDataCenter.getInstance()
                        .availbalance) / Double.parseDouble(tv_buyprice.getText().toString()))));
                if (StringUtil.isNullOrEmpty(maxBuyWeight) || Double.parseDouble(maxBuyWeight) < 1) {//余额不足
                    tv_Info.setVisibility(View.GONE);
                    ll_zhuanzhang.setVisibility(View.VISIBLE);
                } else {
                    tv_Info.setVisibility(View.VISIBLE);
                    ll_zhuanzhang.setVisibility(View.GONE);
                    //计算最大可购买的克数  回显
                    int j = (int) ((Double.parseDouble(maxBuyWeight) - 1) / Double.parseDouble(weightStep));
                    Double maxBuyWeight_D = 1 + j * Double.parseDouble(weightStep);
                    maxBuyWeightStr = String.valueOf(maxBuyWeight_D);
                    tv_Info.setText("最多可购买" + StringUtil.parseStringPattern(maxBuyWeightStr, 0) + " 克");
                    mWeight = Double.parseDouble(StringUtil.append2Decimals(maxBuyWeightStr, 4));
                    if (et_Num > mWeight) {
                        tv_Info.setVisibility(View.GONE);
                        ll_zhuanzhang.setVisibility(View.VISIBLE);
                    } else {
                        tv_Info.setVisibility(View.VISIBLE);
                        ll_zhuanzhang.setVisibility(View.GONE);
                    }
                }
            }
        }else if("amount".equals(buyType)){//按金额
            if(et_Num <= 300){
                ima_DOWN.setClickable(false);
                ima_DOWN.setImageDrawable(getResources().getDrawable(R.drawable.goldstore_jian));
            }else{
                ima_DOWN.setClickable(true);
                ima_DOWN.setImageDrawable(getResources().getDrawable(R.drawable.goldstore_jian_one));
            }
            //输入框信息与最大购买提示信息联动
            if(StringUtil.isNullOrEmpty(rmbAccountList) ||
                    StringUtil.isNullOrEmpty(PreciousmetalDataCenter.getInstance().availbalance) ||
                    Double.valueOf(PreciousmetalDataCenter.getInstance().availbalance) < 300){//余额不足
                tv_Info.setVisibility(View.GONE);
                ll_zhuanzhang.setVisibility(View.VISIBLE);
            }else {
                tv_Info.setVisibility(View.VISIBLE);
                ll_zhuanzhang.setVisibility(View.GONE);
                //最多可购买多少元  （人民币余额-起购数）/步长
                int i = (int)(((Double.parseDouble(PreciousmetalDataCenter.getInstance().availbalance)-300)/Double.parseDouble(amountStep)));
                Double maxBuyAmount = 300 + i * Double.parseDouble(amountStep);
                maxBuyAmoubtStr = String.valueOf(maxBuyAmount);
                tv_Info.setText("最多可购买" + StringUtil.parseStringPattern(maxBuyAmoubtStr,0) + " 元");
                mAmount = Double.parseDouble(StringUtil.append2Decimals(maxBuyAmoubtStr,2));
                if(et_Num > mAmount){
                    tv_Info.setVisibility(View.GONE);
                    ll_zhuanzhang.setVisibility(View.VISIBLE);
                }else{
                    tv_Info.setVisibility(View.VISIBLE);
                    ll_zhuanzhang.setVisibility(View.GONE);
                }
            }
        }

        //设置输入框光标至末尾
        et_BuyNum.setSelection(et_BuyNum.getText().toString().length());
    }

    @Override
    public void requestPsnGoldStoreAccountQueryCallback(Object resultObj) {
        super.requestPsnGoldStoreAccountQueryCallback(resultObj);
        BaseHttpEngine.dissMissProgressDialog();
        accountCusInfo = (Map<String,Object>) PreciousmetalDataCenter.getInstance().ACCOUNTQUERYMAP.get("cusInfo");
        storeList = PreciousmetalDataCenter.getInstance().StoreListItem;
        if(StringUtil.isNullOrEmpty(storeList)){
            if(PreciousmetalDataCenter.getInstance().ACCOUNTQUERYMAP.get("storeList") != null)
                storeList = ((List<Map<String,Object>>)PreciousmetalDataCenter.getInstance().ACCOUNTQUERYMAP.get("storeList")).get(0);
        }
        accountID = String.valueOf(accountCusInfo.get("accountId"));
        //TODO...调公共接口 查询账户人民币余额
        Map<String, Object> ParamMapthis = new HashMap<String, Object>();
        ParamMapthis.put(GoldBonus.ACCOUNTID, accountID);
        getHttpTools().requestHttp(GoldBonus.PSNACCOUNTQUERYACCOUNTDETAIL,
                "requestPsnAccountQueryAccountDetailCallBack", ParamMapthis,
                false);



    }

    public void requestPsnAccountQueryAccountDetailCallBack(Object resultObj) {
        BaseHttpEngine.dissMissProgressDialog();
//        LoadingDialog.closeDialog();
        Map<String, Object> resultMap = HttpTools.getResponseResult(resultObj);
        if (StringUtil.isNullOrEmpty(resultMap)) {
            return;
        }
        List<Map<String, Object>> AccDetailList = (List<Map<String, Object>>) resultMap
                .get("accountDetaiList");
        rmbAccountList=new ArrayList<Map<String,Object>>();
        for (int i = 0; i < (AccDetailList).size(); i++) {
            if (AccDetailList.get(i).get("currencyCode").equals("001")) {
                PreciousmetalDataCenter.getInstance().availbalance = (String) AccDetailList
                        .get(i).get("availableBalance");
                rmbAccountList.add(AccDetailList.get(i));
            }
        }

        accountNum = StringUtil.getForSixForString((String) (accountCusInfo).get("accountNum"));
        tv_bankNo.setText(accountNum);
        tv_addNo.setText(getClickableSpan());
        tv_addNo.setMovementMethod(LinkMovementMethod.getInstance());
//        listmap = PreciousmetalDataCenter.getInstance().GoodsListT;
        //根据首页所选code，显示对应寄存类型
        for(int i=0;i<listmap.size();i++){
            if(mcurrcode.equals(listmap.get(i).get(PreciousmetalDataCenter.CURRCODE))){

                tv_selStoreType.setText((String)listmap.get(i).get("currCodeName"));
                str_selVarietiesName = (String)listmap.get(i).get("varietiesName");
                str_currCode = (String)listmap.get(i).get("currCode");
            }
        }
        for (int j=0;j<PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.size();j++){
            if(mcurrcode.equals(PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(j).get(PreciousmetalDataCenter.CURRCODE))){
                amountStep = (String) PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(j).get("amountStep");
                weightStep = (String) PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(j).get("weightStep");
            }
        }

//        tv_buyprice.setText(PreciousmetalDataCenter.getInstance().BUYPRICE_LONGIN+"");
        initTypeInfo();
//        tv_bankNo.setText(accountNum);
//        amountStep = (String) PreciousmetalDataCenter.getInstance().LOGINPRICEMAPITEM.get("amountStep");
//        weightStep = (String) PreciousmetalDataCenter.getInstance().LOGINPRICEMAPITEM.get("weightStep");

//        tv_addNo.setText(getClickableSpan());
//        tv_addNo.setMovementMethod(LinkMovementMethod.getInstance());

//        tv_wenxinInfo.setText("1. 交易的有效时间一般为周一至周五6:00至20:00，公共节假日除外。\n2. 按克重购买时，最小购买单位为1克，步长为"+StringUtil.parseStringPattern(weightStep,0)+"克，按金额购买时最小购买金额为300人民币元，步长为"+StringUtil.parseStringPattern(amountStep,0)+"人民币元");
        tv_wenxinInfo.setText("1. 手机银行APP端交易时间为周一早7:00至晚20:00，周二至周五早6:00至晚20:00。\n2. 按克重购买时，最小购买单位为1克，追加克重必须是"+StringUtil.parseStringPattern(weightStep,0)+"克的整数倍；按金额购买时，最小购买金额为300人民币元，追加金额必须是"+StringUtil.parseStringPattern(amountStep,0)+"元的整数倍。");
        /*********************************************************/
//        /**默认选择 按克购买**/
//        if(StringUtil.isNullOrEmpty(PreciousmetalDataCenter.getInstance().availbalance)){
//            tv_Info.setVisibility(View.GONE);
//            ll_zhuanzhang.setVisibility(View.VISIBLE);
//        }else {
//            String maxBuyWeight = String.valueOf((int) ((Double.parseDouble(PreciousmetalDataCenter.getInstance()
//                    .availbalance) / Double.parseDouble(tv_buyprice.getText().toString()))));
//            if (StringUtil.isNullOrEmpty(maxBuyWeight) || Double.parseDouble(maxBuyWeight) < 1) {//余额不足
//                tv_Info.setVisibility(View.GONE);
//                ll_zhuanzhang.setVisibility(View.VISIBLE);
//            } else {
//                tv_Info.setVisibility(View.VISIBLE);
//                ll_zhuanzhang.setVisibility(View.GONE);
//                //计算最大可购买的克数  回显
//                int j = (int) ((Double.parseDouble(maxBuyWeight) - 1) / Double.parseDouble(weightStep));
//                Double maxBuyWeight_D = 1 + j * Double.parseDouble(weightStep);
//                maxBuyWeightStr = String.valueOf(maxBuyWeight_D);
//                tv_Info.setText("最多可购买" + StringUtil.parseStringPattern(maxBuyWeightStr, 0) + " 克");
//                mWeight = Double.parseDouble(StringUtil.append2Decimals(maxBuyWeightStr, 4));
//                if (et_Num > mWeight) {
//                    tv_Info.setVisibility(View.GONE);
//                    ll_zhuanzhang.setVisibility(View.VISIBLE);
//                } else {
//                    tv_Info.setVisibility(View.VISIBLE);
//                    ll_zhuanzhang.setVisibility(View.GONE);
//                }
//            }
//        }
        /****************************************/
        refershEtBuyNumInfo();
//        if(listmap.size()<=1){
//            ima_right.setVisibility(View.GONE);
//        }else{
//            ima_right.setVisibility(View.VISIBLE);
//            relativeLayout7.setOnClickListener(imaRightListener);
//        }

//        btn_Next.setOnClickListener(this);
//        tv_byWeight.setOnClickListener(this);
//        tv_byAmount.setOnClickListener(this);
//        ima_UP.setOnClickListener(this);
//        ima_DOWN.setOnClickListener(this);
//        tv_upDate.setOnClickListener(this);
//        tv_zhuanzhuang.setOnClickListener(this);
//        /**监听输入框的信息变化 动态展示提示信息 最大购买数量 和 余额不足 去转账**/
//        et_BuyNum.addTextChangedListener(new TextWatcher(){
//
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//            @Override
//            public void afterTextChanged(Editable editable) {
//                //比较输入框的数量与最大购买数量的大小
//
//                if("".equals(et_BuyNum.getText().toString().trim())){
//                    et_Num = Double.valueOf("0");
//                    ima_DOWN.setClickable(false);
//                    ima_DOWN.setImageDrawable(getResources().getDrawable(R.drawable.goldstore_jian));
//                }else{
//                    et_Num = Double.parseDouble(et_BuyNum.getText().toString().trim());
//                }
//
//                if("weight".equals(buyType)){
//                    if(et_Num <= 1){
//                        ima_DOWN.setClickable(false);
//                        ima_DOWN.setImageDrawable(getResources().getDrawable(R.drawable.goldstore_jian));
//                    }else{
//                        ima_DOWN.setClickable(true);
//                        ima_DOWN.setImageDrawable(getResources().getDrawable(R.drawable.goldstore_jian_one));
//                    }
//
//                }else if("amount".equals(buyType)){
//                    if(et_Num <= 300){
//                        ima_DOWN.setClickable(false);
//                        ima_DOWN.setImageDrawable(getResources().getDrawable(R.drawable.goldstore_jian));
//                    }else{
//                        ima_DOWN.setClickable(true);
//                        ima_DOWN.setImageDrawable(getResources().getDrawable(R.drawable.goldstore_jian_one));
//                    }
//                }
//                if(StringUtil.isNullOrEmpty(rmbAccountList) ||
//                        Double.valueOf(PreciousmetalDataCenter.getInstance().availbalance) < 300){//余额不足
//                    tv_Info.setVisibility(View.GONE);
//                    ll_zhuanzhang.setVisibility(View.VISIBLE);
//                }else {
//                    tv_Info.setVisibility(View.VISIBLE);
//                    ll_zhuanzhang.setVisibility(View.GONE);
//                    //最多可购买多少元  （人民币余额-起购数）/步长
//                    int i = (int)(((Double.parseDouble(PreciousmetalDataCenter.getInstance().availbalance)-300)/Double.parseDouble(amountStep)));
//                    Double maxBuyAmount = 300 + i * Double.parseDouble(amountStep);
//                    maxBuyAmoubtStr = String.valueOf(maxBuyAmount);
//                    tv_Info.setText("最多可购买" + StringUtil.parseStringPattern(maxBuyAmoubtStr,0) + " 元");
//                    mAmount = Double.parseDouble(StringUtil.append2Decimals(maxBuyAmoubtStr,2));
//                    if(et_Num > mAmount){
//                        tv_Info.setVisibility(View.GONE);
//                        ll_zhuanzhang.setVisibility(View.VISIBLE);
//                    }else{
//                        tv_Info.setVisibility(View.VISIBLE);
//                        ll_zhuanzhang.setVisibility(View.GONE);
//                    }
//                }
//
//
//                //设置输入框光标至末尾
//                et_BuyNum.setSelection(et_BuyNum.getText().toString().length());
//            }
//        });
//        if(StringUtil.isNullOrEmpty(rmbAccountList) ||
//                Double.valueOf(PreciousmetalDataCenter.getInstance().availbalance) < 300){//余额不足
//            tv_Info.setVisibility(View.GONE);
//            ll_zhuanzhang.setVisibility(View.VISIBLE);
//        }else {
//            tv_Info.setVisibility(View.VISIBLE);
//            ll_zhuanzhang.setVisibility(View.GONE);
//            //最多可购买多少元  （人民币余额-起购数）/步长
//            int i = (int)(((Double.parseDouble(PreciousmetalDataCenter.getInstance().availbalance)-300)/Double.parseDouble(amountStep)));
//            Double maxBuyAmount = 300 + i * Double.parseDouble(amountStep);
//            maxBuyAmoubtStr = String.valueOf(maxBuyAmount);
//            tv_Info.setText("最多可购买" + StringUtil.parseStringPattern(maxBuyAmoubtStr,0) + " 元");
//        }
//        mAmount = Double.parseDouble(StringUtil.append2Decimals(maxBuyAmoubtStr,2));
//        if(et_Num > mAmount){
//            tv_Info.setVisibility(View.GONE);
//            ll_zhuanzhang.setVisibility(View.VISIBLE);
//        }else{
//            tv_Info.setVisibility(View.VISIBLE);
//            ll_zhuanzhang.setVisibility(View.GONE);
//        }

//        initTypeInfo();

//        if(FromTransFlag){
//            refershEtBuyNumInfo();
//        }

    }

    /**
     * 积存类型 可选事件
     */
    View.OnClickListener imaRightListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            dialogView = View.inflate(BuyMainActivity.this,
                    R.layout.goldstore_choselist, null);
            listView = (ListView) dialogView.findViewById(R.id.chose_list);
            final ArrayList arrayList = new ArrayList();
            for (int i = 0; i < listmap.size(); i++) {
                arrayList.add((String) (listmap.get(i).get(PreciousmetalDataCenter.CURRCODENAME)));
            }
            listView.setAdapter(new ArrayAdapter<ArrayList>(BuyMainActivity.this,
                    android.R.layout.simple_list_item_1, arrayList));
            customDialog = new CustomDialog(BuyMainActivity.this, dialogView);
            customDialog.show();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    PreciousmetalDataCenter.getInstance().CODE = (String) PreciousmetalDataCenter.getInstance().GoodsListT.get(position).get(PreciousmetalDataCenter.CURRCODE);
                    mcurrcode = (String) PreciousmetalDataCenter.getInstance().GoodsListT.get(position).get(PreciousmetalDataCenter.CURRCODE);
                    for (int i = 0; i < PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.size(); i++) {
                        if (((String) listmap.get(position).get(PreciousmetalDataCenter.CURRCODE)).equals
                                (PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(i).get(PreciousmetalDataCenter.CURRCODE))) {
                            PreciousmetalDataCenter.getInstance().BUYPRICE_LONGIN = (String) (PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(position).get(PreciousmetalDataCenter.BUYPRICE));
                            PreciousmetalDataCenter.getInstance().SALEPRICE_LONGIN = (String) (PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(position).get(PreciousmetalDataCenter.SALEPRICE));
                            PreciousmetalDataCenter.getInstance().LOGINPRICEMAPITEM = PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(position);
                            tv_selStoreType.setText((String)PreciousmetalDataCenter.getInstance().LOGINPRICEMAPITEM.get("currCodeName"));
                            str_selVarietiesName = (String)PreciousmetalDataCenter.getInstance().LOGINPRICEMAPITEM.get("varietiesName");
                            str_currCode = (String) PreciousmetalDataCenter.getInstance() .LOGINPRICEMAPITEM.get("currCode");
                            tv_buyprice.setText((String)PreciousmetalDataCenter.getInstance().LOGINPRICEMAPITEM.get("buyPrice"));
                            amountStep = (String) PreciousmetalDataCenter.getInstance().LOGINPRICEMAPITEM.get("amountStep");
                            weightStep = (String) PreciousmetalDataCenter.getInstance().LOGINPRICEMAPITEM.get("weightStep");
//                            tv_wenxinInfo.setText("1. 交易的有效时间一般为周一至周五6:00至20:00，公共节假日除外。\n2. 按克重购买时，最小购买单位为1克，步长为"+StringUtil.parseStringPattern(weightStep,0)+"克，按金额购买时最小购买金额为300人民币元，步长为"+StringUtil.parseStringPattern(amountStep,0)+"人民币元");
                            tv_wenxinInfo.setText("1. 手机银行APP端交易时间为周一早7:00至晚20:00，周二至周五早6:00至晚20:00。\n2. 按克重购买时，最小购买单位为1克，追加克重必须是"+StringUtil.parseStringPattern(weightStep,0)+"克的整数倍；按金额购买时，最小购买金额为300人民币元，追加金额必须是"+StringUtil.parseStringPattern(amountStep,0)+"元的整数倍。");
                            //动态更新 最多购买数量
                            if("weight".equals(buyType)){
                                if(StringUtil.isNullOrEmpty(PreciousmetalDataCenter.getInstance().availbalance)){
                                    tv_Info.setVisibility(View.GONE);
                                    ll_zhuanzhang.setVisibility(View.VISIBLE);
                                }else {
                                    String maxBuyWeight = String.valueOf((int) ((Double.valueOf(PreciousmetalDataCenter.getInstance()
                                            .availbalance) / Double.valueOf(tv_buyprice.getText().toString()))));
                                    if (StringUtil.isNullOrEmpty(maxBuyWeight) || Double.valueOf(maxBuyWeight) < 1) {//余额不足
                                        tv_Info.setVisibility(View.GONE);
                                        ll_zhuanzhang.setVisibility(View.VISIBLE);
                                    } else {
                                        tv_Info.setVisibility(View.VISIBLE);
                                        ll_zhuanzhang.setVisibility(View.GONE);
                                        //计算最大可购买的克数  回显
                                        int j = (int) ((Double.valueOf(maxBuyWeight) - 1) / Double.valueOf(weightStep));
                                        Double maxBuyWeight_D = 1 + j * Double.valueOf(weightStep);
                                        maxBuyWeightStr = String.valueOf(maxBuyWeight_D);
                                        tv_Info.setText("最多可购买" + StringUtil.parseStringPattern(maxBuyWeightStr, 0) + " 克");
                                    }
                                }
                            }else if("amount".equals(buyType)){
                                if(StringUtil.isNullOrEmpty(rmbAccountList) ||
                                        StringUtil.isNullOrEmpty(PreciousmetalDataCenter.getInstance().availbalance) ||
                                        Double.parseDouble(PreciousmetalDataCenter.getInstance().availbalance) < 300){//余额不足
                                    tv_Info.setVisibility(View.GONE);
                                    ll_zhuanzhang.setVisibility(View.VISIBLE);
                                }else {
                                    tv_Info.setVisibility(View.VISIBLE);
                                    ll_zhuanzhang.setVisibility(View.GONE);
                                    //最多可购买多少元  （人民币余额-起购数）/步长
                                    int k = (int) ((Double.parseDouble(PreciousmetalDataCenter.getInstance().availbalance)-300)/Double.parseDouble(amountStep));
                                    Double maxBuyAmount = 300 + k * Double.parseDouble(amountStep);
                                    maxBuyAmoubtStr = String.valueOf(maxBuyAmount);
                                    tv_Info.setText("最多可购买" + StringUtil.parseStringPattern(maxBuyAmoubtStr,0) + " 元");
                                }
                            }
//                            customDialog.cancel();
                        }
                    }
                    customDialog.cancel();
                }

            });
        }
    };

    /**
     * 初始化积存类型 对应的信息
     */
    private void initTypeInfo(){

        for (int i = 0; i < PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.size(); i++) {
            if ((mcurrcode).equals
                    (PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(i).get(PreciousmetalDataCenter.CURRCODE))) {
//                PreciousmetalDataCenter.getInstance().BUYPRICE_LONGIN = (String) (PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(position).get(PreciousmetalDataCenter.BUYPRICE));
//                PreciousmetalDataCenter.getInstance().SALEPRICE_LONGIN = (String) (PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(position).get(PreciousmetalDataCenter.SALEPRICE));
//                PreciousmetalDataCenter.getInstance().LOGINPRICEMAPITEM = PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(i);
                tv_selStoreType.setText((String) ((Map<String,Object>)PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(i)).get("currCodeName"));
                str_selVarietiesName = (String) ((Map<String,Object>)PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(i)).get("varietiesName");
                str_currCode = (String) ((Map<String,Object>)PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(i)).get("currCode");
                tv_buyprice.setText((String) ((Map<String,Object>)PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(i)).get("buyPrice"));
                amountStep = (String) ((Map<String,Object>)PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(i)).get("amountStep");
                weightStep = (String) ((Map<String,Object>)PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(i)).get("weightStep");
//                tv_wenxinInfo.setText("1. 交易的有效时间一般为周一至周五6:00至20:00，公共节假日除外。\n2. 按克重购买时，最小购买单位为1克，步长为"+StringUtil.parseStringPattern(weightStep,0)+"克，按金额购买时最小购买金额为300人民币元，步长为"+StringUtil.parseStringPattern(amountStep,0)+"人民币元");
                tv_wenxinInfo.setText("1. 手机银行APP端交易时间为周一早7:00至晚20:00，周二至周五早6:00至晚20:00。\n2. 按克重购买时，最小购买单位为1克，追加克重必须是"+StringUtil.parseStringPattern(weightStep,0)+"克的整数倍；按金额购买时，最小购买金额为300人民币元，追加金额必须是"+StringUtil.parseStringPattern(amountStep,0)+"元的整数倍。");
                //动态更新 最多购买数量
                if("weight".equals(buyType)){
                    if(StringUtil.isNullOrEmpty(PreciousmetalDataCenter.getInstance().availbalance)){
                        tv_Info.setVisibility(View.GONE);
                        ll_zhuanzhang.setVisibility(View.VISIBLE);
                    }else {
                        String maxBuyWeight = String.valueOf((int) ((Double.valueOf(PreciousmetalDataCenter.getInstance()
                                .availbalance) / Double.valueOf(tv_buyprice.getText().toString()))));
                        if (StringUtil.isNullOrEmpty(maxBuyWeight) || Double.valueOf(maxBuyWeight) < 1) {//余额不足
                            tv_Info.setVisibility(View.GONE);
                            ll_zhuanzhang.setVisibility(View.VISIBLE);
                        } else {
                            tv_Info.setVisibility(View.VISIBLE);
                            ll_zhuanzhang.setVisibility(View.GONE);
                            //计算最大可购买的克数  回显
                            int j = (int) ((Double.valueOf(maxBuyWeight) - 1) / Double.valueOf(weightStep));
                            Double maxBuyWeight_D = 1 + j * Double.valueOf(weightStep);
                            maxBuyWeightStr = String.valueOf(maxBuyWeight_D);
                            tv_Info.setText("最多可购买" + StringUtil.parseStringPattern(maxBuyWeightStr, 0) + " 克");
                        }
                    }
                }else if("amount".equals(buyType)){
                    if(StringUtil.isNullOrEmpty(rmbAccountList) ||
                            StringUtil.isNullOrEmpty(PreciousmetalDataCenter.getInstance().availbalance) ||
                            Double.parseDouble(PreciousmetalDataCenter.getInstance().availbalance) < 300){//余额不足
                        tv_Info.setVisibility(View.GONE);
                        ll_zhuanzhang.setVisibility(View.VISIBLE);
                    }else {
                        tv_Info.setVisibility(View.VISIBLE);
                        ll_zhuanzhang.setVisibility(View.GONE);
                        //最多可购买多少元  （人民币余额-起购数）/步长
                        int k = (int) ((Double.parseDouble(PreciousmetalDataCenter.getInstance().availbalance)-300)/Double.parseDouble(amountStep));
                        Double maxBuyAmount = 300 + k * Double.parseDouble(amountStep);
                        maxBuyAmoubtStr = String.valueOf(maxBuyAmount);
                        tv_Info.setText("最多可购买" + StringUtil.parseStringPattern(maxBuyAmoubtStr,0) + " 元");
                    }
                }

            }
        }
    }

    /**
     *
     * 添加营销代码事件
     * @return
     */
    private SpannableString getClickableSpan() {
        SpannableString spanableInfo = new SpannableString("如果该产品是由银行员工向您推荐，可以 ⊕ 营销代码");
        spanableInfo.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.boc_main_button_color)), 18, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bankerNoView = LayoutInflater.from(BuyMainActivity.this).inflate(R.layout.goldstore_bankerno_layout,null);
                et_bnakerNo = (EditText) bankerNoView.findViewById(R.id.et_banker);

                /**自动弹出软键盘**/
                autoShowSoftInput(et_bnakerNo);
                UserDialog.showDialogWithTwoButton(BuyMainActivity.this,bankerNoView,new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        if(view.getTag() == DialogButtonType.secondBt){
                            if("".equals(et_bnakerNo.getText().toString())){
                                //隐藏营销代码回显信息  显示添加提示文本信息
                                tv_addNo.setVisibility(View.VISIBLE);
                                ll_update.setVisibility(View.GONE);
                                bankerNoStr = "";
                                UserDialog.closeDialog();
//                                InputMethodManager imm = (InputMethodManager) BuyMainActivity.this
//                                        .getSystemService(Context.INPUT_METHOD_SERVICE);
//                                imm.hideSoftInputFromWindow(et_BuyNum.getWindowToken(), 0);
                                return;
                            }
//                            if(et_bnakerNo.getText().toString().length() < 7){
////                                MessageDialog.showMessageDialog(BuyMainActivity.this,"营销代码为7位数字");
//                                BaseDroidApp.getInstanse().showInfoMessageDialog("营销代码为7位数字");
//                                UserDialog.closeDialog();
//                                return;
//                            }
                            bankerNoStr = et_bnakerNo.getText().toString();
                            //隐藏添加提示文本信息 显示营销代码回显信息
                            tv_addNo.setVisibility(View.GONE);
                            ll_update.setVisibility(View.VISIBLE);
                            tv_update.setText("营销代码为 " + et_bnakerNo.getText().toString().trim()+"   ");
                            /**修改营销代码**/
                            ima_update.setOnClickListener(listener);
                            Flag = true;
                        }
                        UserDialog.closeDialog();
//                        InputMethodManager imm = (InputMethodManager) BuyMainActivity.this
//                                .getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.hideSoftInputFromWindow(et_BuyNum.getWindowToken(), 0);
                    }
                });
            }
        };
        spanableInfo.setSpan(new Clickable(listener), 18, 21, Spanned.SPAN_MARK_MARK);
        return spanableInfo;
    }


    class Clickable extends ClickableSpan implements View.OnClickListener {
        private final View.OnClickListener mListener;

        public Clickable(View.OnClickListener listener) {
            mListener = listener;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false); //去掉下划线
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button2:
                if("".equals(et_BuyNum.getText().toString().trim())){
                    if("weight".equals(buyType)){
                        MessageDialog.showMessageDialog(this,"请输入购买克重");
//                        BaseDroidApp.getInstanse().showInfoMessageDialog("请输入购买克重");
                    }else if("amount".equals(buyType)){
                        MessageDialog.showMessageDialog(this,"请输入购买金额");
//                        BaseDroidApp.getInstanse().showInfoMessageDialog("请输入购买金额");
                    }
                    return;
                }
                //TODO... 页面数据异常处理  避免闪退
                if(DataCheck()) return;
                if(!reg_etNum()) return;
                if(ll_update.getVisibility() == View.VISIBLE){
                    if(et_bnakerNo.getText().toString().length() < 7){
                                MessageDialog.showMessageDialog(BuyMainActivity.this,"请输入7位纯数字营销代码");
//                        BaseDroidApp.getInstanse().showInfoMessageDialog("请输入7位纯数字营销代码");
                    UserDialog.closeDialog();
                        return;
                    }
                }
                Intent intent = new Intent(this,BuyConfirmActivity.class);
                intent.putExtra("accountNum",accountNum);
                intent.putExtra("accountId",accountID);
                intent.putExtra("etNum",et_BuyNum.getText().toString().trim());
                intent.putExtra("cankaoAmount",
                        StringUtil.parseStringPattern(String.valueOf((Double.valueOf(et_BuyNum.getText().toString().trim()))*(Double.valueOf(tv_buyprice.getText().toString().trim()))),2));
                intent.putExtra("Flag",Flag);
                intent.putExtra("buyType",buyType);
                intent.putExtra("storeType",tv_selStoreType.getText().toString());//积存类型
                intent.putExtra("varietiesName",str_selVarietiesName);//积存品种
                intent.putExtra("currCode",str_currCode);
                intent.putExtra("buyPrice",tv_buyprice.getText().toString());
                if(Flag && bankerNoStr != null){
                    intent.putExtra("bankerNo",bankerNoStr);
                }
                startActivity(intent);
//                finish();
                break;
            case R.id.textView16://按克重购买
                //动态控制输入框的长度
                et_BuyNum.setFilters(new InputFilter[]{
                        new InputFilter.LengthFilter(13)
                });
                etAmount_last = et_BuyNum.getText().toString();
                buyType = "weight";
                tv_byWeight.setTextColor(getResources().getColor(R.color.boc_text_color_red));
                tv_byAmount.setTextColor(getResources().getColor(R.color.boc_text_color_dark_gray));
                ima_weightLine.setVisibility(View.VISIBLE);
                ima_amountLine.setVisibility(View.GONE);
                et_BuyNum.setText(etWeight_last);
                if(DataCheck()) return;
                if(StringUtil.isNullOrEmpty(PreciousmetalDataCenter.getInstance().availbalance)){
                    tv_Info.setVisibility(View.GONE);
                    ll_zhuanzhang.setVisibility(View.VISIBLE);
                }else {
                    String maxBuyWeight = String.valueOf((int) ((Double.parseDouble(PreciousmetalDataCenter.getInstance()
                            .availbalance) / Double.parseDouble(tv_buyprice.getText().toString()))));
                    if (StringUtil.isNullOrEmpty(maxBuyWeight) || Double.parseDouble(maxBuyWeight) < 1) {//余额不足
                        tv_Info.setVisibility(View.GONE);
                        ll_zhuanzhang.setVisibility(View.VISIBLE);
                    } else {
                        //计算最大可购买的克数  回显
                        int j = (int) ((Double.parseDouble(maxBuyWeight) - 1) / Double.parseDouble(weightStep));
                        Double maxBuyWeight_D = 1 + j * Double.parseDouble(weightStep);
                        maxBuyWeightStr = String.valueOf(maxBuyWeight_D);
                        //比较输入框数量与最大购买数量
                        if(!"".equals(et_BuyNum.getText().toString()) && (Double.parseDouble(et_BuyNum.getText().toString()) > maxBuyWeight_D)){
                            tv_Info.setVisibility(View.GONE);
                            ll_zhuanzhang.setVisibility(View.VISIBLE);
                        }else{
                            tv_Info.setVisibility(View.VISIBLE);
                            ll_zhuanzhang.setVisibility(View.GONE);
                            tv_Info.setText("最多可购买" + StringUtil.parseStringPattern(maxBuyWeightStr, 0) + " 克");
                        }
                    }
                }
                et_BuyNum.setSelection(et_BuyNum.getText().toString().length());
//                etWeight_last = et_BuyNum.getText().toString();
                break;
            case R.id.textView17://按金额购买
                //动态控制输入框的长度
                et_BuyNum.setFilters(new InputFilter[]{
                        new InputFilter.LengthFilter(15)
                });
                etWeight_last = et_BuyNum.getText().toString();
                buyType = "amount";
                tv_byAmount.setTextColor(getResources().getColor(R.color.boc_text_color_red));
                tv_byWeight.setTextColor(getResources().getColor(R.color.boc_text_color_dark_gray));
                ima_weightLine.setVisibility(View.GONE);
                ima_amountLine.setVisibility(View.VISIBLE);
                et_BuyNum.setText(etAmount_last);
                if(DataCheck()) return;
                if(StringUtil.isNullOrEmpty(rmbAccountList) ||
                        StringUtil.isNullOrEmpty(PreciousmetalDataCenter.getInstance().availbalance) ||
                        Double.parseDouble(PreciousmetalDataCenter.getInstance().availbalance) < 300){//余额不足
                    tv_Info.setVisibility(View.GONE);
                    ll_zhuanzhang.setVisibility(View.VISIBLE);
                }else {
                    //最多可购买多少元  （人民币余额-起购数）/步长
                    int i = (int) ((Double.parseDouble(PreciousmetalDataCenter.getInstance().availbalance)-300)/Double.parseDouble(amountStep));
                    Double maxBuyAmount = 300 + i * Double.parseDouble(amountStep);
                    maxBuyAmoubtStr = String.valueOf(maxBuyAmount);

                    if(!"".equals(et_BuyNum.getText().toString()) &&(Double.parseDouble(et_BuyNum.getText().toString()) > maxBuyAmount)){//输入框的数量大于最大可购买数量
                        tv_Info.setVisibility(View.GONE);
                        ll_zhuanzhang.setVisibility(View.VISIBLE);
                    }else{
                        tv_Info.setVisibility(View.VISIBLE);
                        ll_zhuanzhang.setVisibility(View.GONE);
                        tv_Info.setText("最多可购买" + StringUtil.parseStringPattern(maxBuyAmoubtStr,0) + " 元");
                    }
                }
                et_BuyNum.setSelection(et_BuyNum.getText().toString().length());
//                etAmount_last = et_BuyNum.getText().toString();
                break;
            case R.id.imageView5://加
                if(DataCheck()) return;
                ima_DOWN.setClickable(true);
                ima_DOWN.setImageDrawable(getResources().getDrawable(R.drawable.goldstore_jian_one));
                et_BuyNum.setText(update_etNum(true));
                et_BuyNum.setSelection(et_BuyNum.getText().toString().length());
                break;
            case R.id.imageView4://减
                if(DataCheck()) return;
                String str = update_etNum(false);
                if(str == null){
                    ima_DOWN.setClickable(false);
                    ima_DOWN.setImageDrawable(getResources().getDrawable(R.drawable.goldstore_jian));
                    return;
                }else{
                    et_BuyNum.setText(str);
                }
                et_BuyNum.setSelection(et_BuyNum.getText().toString().length());
                break;
            case R.id.textView23://更改账户
                PreciousmetalDataCenter.getInstance().BUYFLAG_CHANGE="0";
                Intent intent2 = new Intent(this, GoldstoreAccountResetActivity.class);
                startActivity(intent2);
//                finish();
                break;
            case R.id.tv_zhuanzhang://去转账（转账完成后回到购买页面执行onResume）
//                FromTransFlag = true;
                ModelBoc.gotoTransferActivity(this,accountID);
                break;
        }

    }

    /**
     * 修正输入框信息的方法 自动符合数据要求
     * isAdd  + true；- false
     * @return
     */
    private String update_etNum(boolean isAdd){
        //基数
        int b = "weight".equals(buyType) ? 1 : 300;
        //步长
        double s = "weight".equals(buyType) ? Double.valueOf(weightStep) : Double.valueOf(amountStep);
        //输入框数据
        if("".equals(et_BuyNum.getText().toString().trim())){
            et_BuyNum.setText("0");
        }
        long i = Long.valueOf(et_BuyNum.getText().toString().trim());
        if(isAdd){//加
            if (i-b <s) {
                i = (long) (b +s);
            }else{
                i = (long)(((long)((i-b)/s))*s +b +s);
            }
        }else{//减
            if(i <= b) return null;
            if(i-b < s){
                i = b;
            }else{
                if ((i-b)%s != 0) {
                    i = (long)(i-(i-b)%s);
                }else{
                    i = (long)(((long)((i-b)/s))*s +b -s);
                }
            }

        }
        return String.valueOf(i);
    }

    /**
     * 下一步 校验
     */
    private boolean reg_etNum(){
        //基数
        int b = "weight".equals(buyType) ? 1 : 300;
        //输入框数据
        double i = Double.valueOf(et_BuyNum.getText().toString().trim());

        if("weight".equals(buyType)) {
            if(i < b){
//                BaseDroidApp.getInstanse().showInfoMessageDialog("请输入大于0的整数");
                MessageDialog.showMessageDialog(this,"请输入大于0的整数");
                return false;
            }else if(ll_zhuanzhang.isShown()){
                MessageDialog.showMessageDialog(this,"您的当前账户余额不足，您可以先去转账再继续购买");
//                BaseDroidApp.getInstanse().showInfoMessageDialog("您的当前账户余额不足，您可以先去转账再继续购买");
                return false;
            } else if (i > b && ((i-b)%(Integer.parseInt(StringUtil.parseStringPattern(weightStep,0))) != 0)) {
                MessageDialog.showMessageDialog(this,"最小购买数量为1克，步长为"+ StringUtil.parseStringPattern(weightStep,0) +"克");
//                BaseDroidApp.getInstanse().showInfoMessageDialog("最小购买数量为1克，步长为"+ StringUtil.parseStringPattern(weightStep,0) +"克");
                return false;
            }
        }else{
            if(i < b){
//                BaseDroidApp.getInstanse().showInfoMessageDialog("请输入大于等于300的整数");
                MessageDialog.showMessageDialog(this,"请输入大于等于300的整数");
                return false;
            }else if(ll_zhuanzhang.isShown()){
                MessageDialog.showMessageDialog(this,"您的当前账户余额不足，您可以先去转账再继续购买");
//                BaseDroidApp.getInstanse().showInfoMessageDialog("您的当前账户余额不足，您可以先去转账再继续购买");
                return false;
            } else if (i > b && ((i-b)%(Integer.parseInt(StringUtil.parseStringPattern(amountStep,0))) != 0)) {
                MessageDialog.showMessageDialog(this,"最小购买金额为300元，步长为"+ StringUtil.parseStringPattern(amountStep,0) +"元");
//                BaseDroidApp.getInstanse().showInfoMessageDialog("最小购买金额为300元，步长为"+ StringUtil.parseStringPattern(amountStep,0) +"元");
                return false;
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        goGoldstoreMain();
    }

    /**
     * 数据异常处理
     */
    private boolean DataCheck(){
        //无牌价数据
        if(StringUtil.isNullOrEmpty(tv_buyprice.getText().toString().trim()) || "-".equals(tv_buyprice.getText().toString().trim())) {
            tv_buyprice.setText("-");
//            tv_wenxinInfo.setText("1. 交易的有效时间一般为周一至周五6:00至20:00，公共节假日除外。\n2. 按克重购买时，最小购买单位为1克，步长为-克，按金额购买时最小购买金额为300人民币元，步长为-人民币元");
            tv_wenxinInfo.setText("1. 手机银行APP端交易时间为周一早7:00至晚20:00，周二至周五早6:00至晚20:00。\n2. 按克重购买时，最小购买单位为1克，追加克重必须是-克的整数倍；按金额购买时，最小购买金额为300人民币元，追加金额必须是-元的整数倍。");
            if("weight".equals(buyType)){
//                tv_Info.setText("最多可购买-克");
                ll_zhuanzhang.setVisibility(View.VISIBLE);
            }else if("amount".equals(buyType)){
//                tv_Info.setText("最多可购买-元");
                ll_zhuanzhang.setVisibility(View.VISIBLE);
            }
            return true;
        }
        //无步长数据
        if(StringUtil.isNullOrEmpty(amountStep) || "0".equals(amountStep)) return true;

        if(StringUtil.isNullOrEmpty(weightStep) || "0".equals(weightStep)) return true;
        return false;
    }




    /**
     * PsnAccountQueryAccountDetail 接口报错后执行
     * @param response
     * @return
     */
    @Override
    public boolean doBiihttpRequestCallBackPre(BiiResponse response) {
        BaseHttpEngine.dissMissProgressDialog();
        List<BiiResponseBody> biiResponseBodyList = response.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodyList.get(0);
        if (!StringUtil.isNullOrEmpty(biiResponseBodyList)) {
            for (BiiResponseBody body : biiResponseBodyList) {
                if ("PsnAccountQueryAccountDetail"
                        .equals(biiResponseBody.getMethod())) {
                    BiiHttpEngine.dissMissProgressDialog();
                    BiiError biiError = biiResponseBody.getError();
                    // 判断是否存在error
                    if (biiError != null) {//不屏蔽后台错误，且显示查询前页面的温馨提示
                        //PsnAccountQueryAccountDetail 接口报错后执行
                        accountNum = StringUtil.getForSixForString((String) (accountCusInfo).get("accountNum"));
//        listmap = PreciousmetalDataCenter.getInstance().GoodsListT;
                        //根据首页所选code，显示对应寄存类型
                        /**无牌价的情况**/
                        if(StringUtil.isNullOrEmpty(listmap) || StringUtil.isNullOrEmpty(PreciousmetalDataCenter.getInstance().LOGINPRICEMAP)) {
                            ima_right.setVisibility(View.INVISIBLE);
                            tv_selStoreType.setText("-");
                            accountCusInfo =  (Map<String,Object>) PreciousmetalDataCenter.getInstance().ACCOUNTQUERYMAP.get("cusInfo");
                            accountNum = StringUtil.getForSixForString((String) (accountCusInfo).get("accountNum"));
                            tv_bankNo.setText(accountNum);
                            tv_addNo.setText(getClickableSpan());
                            tv_addNo.setMovementMethod(LinkMovementMethod.getInstance());
                            DataCheck();
                        }else {
                            for (int i = 0; i < listmap.size(); i++) {
                                if (mcurrcode.equals(listmap.get(i).get(PreciousmetalDataCenter.CURRCODE))) {

                                    tv_selStoreType.setText((String) listmap.get(i).get("currCodeName"));
                                    str_selVarietiesName = (String) listmap.get(i).get("varietiesName");
                                    str_currCode = (String) listmap.get(i).get("currCode");
                                }
                            }
                            for (int j = 0; j < PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.size(); j++) {
                                if (mcurrcode.equals(PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(j).get(PreciousmetalDataCenter.CURRCODE))) {
                                    amountStep = (String) PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(j).get("amountStep");
                                    weightStep = (String) PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(j).get("weightStep");
                                }
                            }
//                        tv_buyprice.setText(PreciousmetalDataCenter.getInstance().BUYPRICE_LONGIN+"");
                        initTypeInfo();
                            tv_bankNo.setText(accountNum);
//        amountStep = (String) PreciousmetalDataCenter.getInstance().LOGINPRICEMAPITEM.get("amountStep");
//        weightStep = (String) PreciousmetalDataCenter.getInstance().LOGINPRICEMAPITEM.get("weightStep");

                            tv_addNo.setText(getClickableSpan());
                            tv_addNo.setMovementMethod(LinkMovementMethod.getInstance());

//                            tv_wenxinInfo.setText("1. 交易的有效时间一般为周一至周五6:00至20:00，公共节假日除外。\n2. 按克重购买时，最小购买单位为1克，步长为" + StringUtil.parseStringPattern(weightStep, 0) + "克，按金额购买时最小购买金额为300人民币元，步长为" + StringUtil.parseStringPattern(amountStep, 0) + "人民币元");
                            tv_wenxinInfo.setText("1. 手机银行APP端交易时间为周一早7:00至晚20:00，周二至周五早6:00至晚20:00。\n2. 按克重购买时，最小购买单位为1克，追加克重必须是"+StringUtil.parseStringPattern(weightStep,0)+"克的整数倍；按金额购买时，最小购买金额为300人民币元，追加金额必须是"+StringUtil.parseStringPattern(amountStep,0)+"元的整数倍。");
                        }
                        /**默认选择 按克购买**/
                        if(StringUtil.isNullOrEmpty(PreciousmetalDataCenter.getInstance().availbalance)){
                            tv_Info.setVisibility(View.GONE);
                            ll_zhuanzhang.setVisibility(View.VISIBLE);
                        }else {
                            String maxBuyWeight = String.valueOf((int) ((Double.parseDouble(PreciousmetalDataCenter.getInstance()
                                    .availbalance) / Double.parseDouble(tv_buyprice.getText().toString()))));
                            if (StringUtil.isNullOrEmpty(maxBuyWeight) || Double.parseDouble(maxBuyWeight) < 1) {//余额不足
                                tv_Info.setVisibility(View.GONE);
                                ll_zhuanzhang.setVisibility(View.VISIBLE);
                            } else {
                                tv_Info.setVisibility(View.VISIBLE);
                                ll_zhuanzhang.setVisibility(View.GONE);
                                //计算最大可购买的克数  回显
                                int j = (int) ((Double.parseDouble(maxBuyWeight) - 1) / Double.parseDouble(weightStep));
                                Double maxBuyWeight_D = 1 + j * Double.parseDouble(weightStep);
                                maxBuyWeightStr = String.valueOf(maxBuyWeight_D);
                                tv_Info.setText("最多可购买" + StringUtil.parseStringPattern(maxBuyWeightStr, 0) + " 克");
                                mWeight = Double.parseDouble(StringUtil.append2Decimals(maxBuyWeightStr, 4));
                                if (et_Num > mWeight) {
                                    tv_Info.setVisibility(View.GONE);
                                    ll_zhuanzhang.setVisibility(View.VISIBLE);
                                } else {
                                    tv_Info.setVisibility(View.VISIBLE);
                                    ll_zhuanzhang.setVisibility(View.GONE);
                                }
                            }
                        }
//                        initTypeInfo();
                    }
                }
            }
        }
        return super.doBiihttpRequestCallBackPre(response);
    }

    /**
     * 自动弹出软键盘
     * @param et
     */
    private void autoShowSoftInput(final EditText et){
        et.setFocusable(true);
        et.setFocusableInTouchMode(true);
        et.requestFocus();

        Timer timer = new Timer();
        timer.schedule(new TimerTask()
                       {
                           public void run()
                           {
                               InputMethodManager inputManager =
                                       (InputMethodManager)et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(et, 0);
                           }

                       },
                400);
    }


}
