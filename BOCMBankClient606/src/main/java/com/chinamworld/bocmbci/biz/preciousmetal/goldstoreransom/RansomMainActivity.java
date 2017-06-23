package com.chinamworld.bocmbci.biz.preciousmetal.goldstoreransom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.biz.preciousmetal.PreciousmetalBaseActivity;
import com.chinamworld.bocmbci.biz.preciousmetal.PreciousmetalDataCenter;
import com.chinamworld.bocmbci.http.IHttpResponseCallBack;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.llbt.userwidget.dialogview.MessageDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 贵金属积存 赎回主页面
 * Created by linyl on 2016/8/24.
 */
public class RansomMainActivity extends PreciousmetalBaseActivity
        implements View.OnClickListener {
    Button btn_next;
    EditText et_ransomNum;
    /**积存账户信息 cusinfo **/
    Map<String,Object> accountCusInfo;
    Map<String,Object> storeList;
    /**账号 464 显示 **/
    String accountNum = null;
    String accountId = null;
    String ransomtype;
    TextView tv_bankNum;
    TextView tv_amount;
    String amount; // 积存量
    TextView tv_sellPrice;
    TextView tv_allRansom;
    /**积存类型list数据**/
    List<Map<String, Object>> listmap;
    ImageView ima_right;
    //要弹出的对话框
    View dialogView;
    ListView listView;
    CustomDialog customDialog;
    TextView tv_ransomType;
    //7号接口 storeList的数据
    List<Map<String,Object>> storeListMap;
    ArrayList arrayList;
    private LinearLayout acc_type_layout;
    LinearLayout ll_mian;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goldstore_ransom_main);
        getPreviousmeatalBackgroundLayout().setTitleText("赎回");
        /**右边图标文本信息 我要提货**/
        getPreviousmeatalBackgroundLayout().setMetalRightText("我要提货");
//        getPreviousmeatalBackgroundLayout().setMetalRightImage(null);//隐藏默认图标
//        getPreviousmeatalBackgroundLayout().setMetalRightVisibility(View.GONE);

        /**我要提货 按钮点击事件**/
        getPreviousmeatalBackgroundLayout().setMetalRightonClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                /**调#12接口 网点提前信息查询  成功跳转到自提网点页面**/
                requestPsnGoldStoreBranchQuery();

            }
        });
        acc_type_layout=(LinearLayout)findViewById(R.id.acc_type_layout);
        btn_next = (Button) findViewById(R.id.btn_goldstore_ransom_next);
        et_ransomNum = (EditText) findViewById(R.id.et_goldstorenum);
        tv_bankNum = (TextView) findViewById(R.id.goldstore_branch_accno);
        tv_amount = (TextView) findViewById(R.id.goldstore_branch_num);
        tv_sellPrice = (TextView) findViewById(R.id.goldstore_jicun_price);
        tv_allRansom = (TextView) findViewById(R.id.tv_goldstore_shuhui_all);
        tv_ransomType = (TextView) findViewById(R.id.tv_storetype_value);
        ima_right = (ImageView) findViewById(R.id.ima_right);
        ll_mian = (LinearLayout) findViewById(R.id.ll_ransom_main);
        ll_mian.setOnTouchListener(touchListener);
        /**限制小数位数输入 到4位后 不能再输入小数**/
        et_ransomNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = et_ransomNum.getText().toString().trim();
                if(str.contains(".")){//输入的包括小数点
                    if((str.length()-(str.indexOf(".")+1)) > 4){
                        et_ransomNum.setText(str.substring(0,str.indexOf(".")+5));
                    }
                }
                et_ransomNum.setSelection(et_ransomNum.getText().toString().length());
            }
        });

        requsetPsnGoldStoreAccountQuery();


    }


    @Override
    public void requestPsnGoldStoreAccountQueryCallback(Object resultObj) {
        super.requestPsnGoldStoreAccountQueryCallback(resultObj);
        BaseHttpEngine.dissMissProgressDialog();
        accountCusInfo = (Map<String,Object>) PreciousmetalDataCenter.getInstance().ACCOUNTQUERYMAP.get("cusInfo");

//         storeList = PreciousmetalDataCenter.getInstance().StoreListItem;
//        ？
        accountNum = StringUtil.getForSixForString((String) (accountCusInfo).get("accountNum"));
        accountId = (String) (accountCusInfo).get("accountId");
        tv_bankNum.setText(accountNum);

//        tv_sellPrice.setText(PreciousmetalDataCenter.getInstance().SALEPRICE_LONGIN+"");
        tv_allRansom.setOnClickListener(this);
        btn_next.setOnClickListener(this);

        listmap = PreciousmetalDataCenter.getInstance().GoodsListT;
//        for(int i=0;i<listmap.size();i++){
//            if(PreciousmetalDataCenter.getInstance().CODE.equals(listmap.get(i).get(PreciousmetalDataCenter.CURRCODE))){
//                tv_ransomType.setText((String)listmap.get(i).get("currCodeName"));
//                tv_amount.setText((String)storeListMap.get(i).get("amount"));
//            }
//
//        }


        storeListMap = (List<Map<String, Object>>) PreciousmetalDataCenter.getInstance().ACCOUNTQUERYMAP.get(PreciousmetalDataCenter.STORELIST);
        arrayList = new ArrayList();
//        for (int i = 0; i < listmap.size(); i++) {
        //过滤无积存量的积存类型项
        if(!StringUtil.isNullOrEmpty(storeListMap)){
            for (int j = 0; j < storeListMap.size(); j++) {
                if(StringUtil.isNullOrEmpty(storeListMap.get(j).get(PreciousmetalDataCenter.AMOUNT)) ||
                        "0.0000".equals(StringUtil.append2Decimals((String) storeListMap.get(j).get(PreciousmetalDataCenter.AMOUNT),4))){
                    storeListMap.remove(j);
                    return;
                }
            }
        }
        tv_ransomType.setText((String)storeListMap.get(0).get("currCodeName"));
        setAmountTv((String)storeListMap.get(0).get("amount"));
        for (int i = 0; i < storeListMap.size();i++){
            if(!(0==(Double.parseDouble((String)storeListMap.get(i).get(PreciousmetalDataCenter.AMOUNT))))){
                arrayList.add(storeListMap.get(i).get("currCodeName"));
            }

        }

        if(arrayList.size()<2){
            ima_right.setVisibility(View.INVISIBLE);
        }else{
            ima_right.setVisibility(View.VISIBLE);
            acc_type_layout.setOnClickListener(imaRightListener);
        }
        initTypeInfo();
        /**返回事件 跳转到首页**/
        getPreviousmeatalBackgroundLayout().setMetalBackonClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                goGoldstoreMain();
            }
        });
    }

    private void setAmountTv(String amountString){
        amount = amountString;
        tv_amount.setText(StringUtil.parseStringPattern(amount,4));
    }
    /**
     * 积存类型 可选事件
     */
    View.OnClickListener imaRightListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            dialogView = View.inflate(RansomMainActivity.this,
                    R.layout.goldstore_choselist, null);
            listView = (ListView) dialogView.findViewById(R.id.chose_list);

            listView.setAdapter(new ArrayAdapter<ArrayList>(RansomMainActivity.this,
                    android.R.layout.simple_list_item_1, arrayList));
            customDialog = new CustomDialog(RansomMainActivity.this, dialogView);
            customDialog.show();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    PreciousmetalDataCenter.getInstance().CODE = (String) PreciousmetalDataCenter.getInstance().GoodsListT.get(position).get(PreciousmetalDataCenter.CURRCODE);
                    for (int i = 0; i < PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.size(); i++) {
                        if (((String) storeListMap.get(position).get(PreciousmetalDataCenter.CURRCODE)).equals
                                (PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(i).get(PreciousmetalDataCenter.CURRCODE))) {
                            PreciousmetalDataCenter.getInstance().BUYPRICE_LONGIN = (String) (PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(position).get(PreciousmetalDataCenter.BUYPRICE));
                            PreciousmetalDataCenter.getInstance().SALEPRICE_LONGIN = (String) (PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(position).get(PreciousmetalDataCenter.SALEPRICE));
                            PreciousmetalDataCenter.getInstance().LOGINPRICEMAPITEM = PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(position);
                            tv_ransomType.setText((String) PreciousmetalDataCenter.getInstance().LOGINPRICEMAPITEM.get("currCodeName"));
                            tv_sellPrice.setText((String) PreciousmetalDataCenter.getInstance().LOGINPRICEMAPITEM.get("salePrice"));

                            //遍历storeList 取得积存量
                            setAmountTv((String)storeListMap.get(position).get("amount"));

                        }
                    }
                    PreciousmetalDataCenter.getInstance().CODE=(String)storeListMap.get(position).get(PreciousmetalDataCenter.CURRCODE);
                    customDialog.cancel();
                }
            });
        }
    };

    /**初始化积存类型对应信息**/
    private void initTypeInfo(){
        if(StringUtil.isNullOrEmpty(PreciousmetalDataCenter.getInstance().LOGINPRICEMAP)){
            tv_ransomType.setText("-");
            tv_sellPrice.setText("-");
            ima_right.setVisibility(View.INVISIBLE);
            setAmountTv(null);
            btn_next.setClickable(false);
            tv_allRansom.setClickable(false);
        }else {
            for (int i = 0; i < PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.size(); i++) {
                if (((PreciousmetalDataCenter.getInstance().CODE)).equals
                        (PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(i).get(PreciousmetalDataCenter.CURRCODE))) {
//                PreciousmetalDataCenter.getInstance().BUYPRICE_LONGIN = (String) (PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(position).get(PreciousmetalDataCenter.BUYPRICE));
//                PreciousmetalDataCenter.getInstance().SALEPRICE_LONGIN = (String) (PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(position).get(PreciousmetalDataCenter.SALEPRICE));
                PreciousmetalDataCenter.getInstance().LOGINPRICEMAPITEM = PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(i);

                    tv_ransomType.setText((String) ((Map<String, Object>) PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(i)).get("currCodeName"));
                    tv_sellPrice.setText((String) ((Map<String, Object>) PreciousmetalDataCenter.getInstance().LOGINPRICEMAP.get(i)).get("salePrice"));

                    //遍历storeList 取得积存量
                    setAmountTv((String) storeListMap.get(i).get("amount"));
                    btn_next.setClickable(true);
                    tv_allRansom.setClickable(true);
                }
            }
        }
    }

    /**
     * #12接口 网点提前信息查询
     */
    private void requestPsnGoldStoreBranchQuery(){
        Map<String,Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("bankId",
                (String) ((Map<String,Object>)PreciousmetalDataCenter.getInstance().ACCOUNTQUERYMAP.get("cusInfo"))
                        .get("bankId"));//7号接口账户查询 返回 bankid
//        LoadingDialog.showLoadingDialog(this);
        this.getHttpTools().requestHttpWithConversationId("PsnGoldStoreBranchQuery",paramsMap,null,
                new IHttpResponseCallBack<Map<String,Object>>(){

                    @SuppressWarnings("unchecked")
                    @Override
                    public void httpResponseSuccess(Map<String,Object> result, String method) {
                        //关闭通信框
//                        LoadingDialog.closeDialog();
                        BaseHttpEngine.dissMissProgressDialog();
                        if(StringUtil.isNullOrEmpty(result)){
                            //提示信息
                            MessageDialog.showMessageDialog(RansomMainActivity.this,"暂无自提网店");
                            return;
                        }
                        PreciousmetalDataCenter.getInstance().BranchQueryMap = result;
                        Intent intent = new Intent(RansomMainActivity.this,BranchQueryActivity.class);
                        startActivity(intent);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        goGoldstoreMain();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_goldstore_ransom_next://下一步
                ArrayList<RegexpBean> list = new ArrayList<RegexpBean>();
                if(StringUtil.isNullOrEmpty(et_ransomNum.getText().toString().trim())){//空
                    MessageDialog.showMessageDialog(this,"请输入赎回克重");
                    return;
                }
//                //格式校验
//                RegexpBean reb = new RegexpBean("",et_ransomNum.getText().toString().trim(),"srmsRedeem2");
//                list.add(reb);
//                if (!RegexpUtils.regexpDate(list)) return;//校验不过

                if(et_ransomNum.getText().toString().trim().startsWith(".") ||
                        et_ransomNum.getText().toString().trim().startsWith("00") ||
                        Double.parseDouble(et_ransomNum.getText().toString().trim()) <= 0){
                    MessageDialog.showMessageDialog(this,"请输入大于0的数");
                    et_ransomNum.setSelection(et_ransomNum.getText().toString().length());
                    return;
                }
                if(Double.valueOf(et_ransomNum.getText().toString().trim()) > Double.valueOf("999999")){
                    MessageDialog.showMessageDialog(this,"单笔赎回克重不能超过999999克");
//                    et_ransomNum.setText("");
                    et_ransomNum.setSelection(et_ransomNum.getText().toString().length());
                    return;
                }
                //与最大积存量校验
                if(Double.valueOf(et_ransomNum.getText().toString().trim()) > Double.valueOf(amount)){
                    MessageDialog.showMessageDialog(this,"您没那么多持仓，赎回克重已超积存余额");
//                    et_ransomNum.setText("");
                    et_ransomNum.setSelection(et_ransomNum.getText().toString().length());
                    return;
                }
                Intent intent = new Intent(RansomMainActivity.this,RansomConfirmActivity.class);
                intent.putExtra("ransomPrice",tv_sellPrice.getText().toString());
                intent.putExtra("ransomNum",et_ransomNum.getText().toString().trim());
                intent.putExtra("accountNum",accountNum);
                intent.putExtra("accountId",accountId);
                intent.putExtra("ransomMax",amount);//积存量
                intent.putExtra("ransomType",tv_ransomType.getText().toString());//积存类型

                startActivity(intent);
                break;
            case R.id.tv_goldstore_shuhui_all://全部赎回
                et_ransomNum.setText(amount);
                if(Double.parseDouble(amount) > 999999){
                    et_ransomNum.setText("999999");
                }
                et_ransomNum.setSelection(et_ransomNum.getText().toString().length());
                break;
        }
    }

    /**
     * 点击键盘完成键  事件监听  将输入框的数量 自动校验成4位小数
     *
     */
    @Override
    public boolean dispatchKeyEvent(android.view.KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
	            /*隐藏软键盘*/
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if(inputMethodManager.isActive()){
                inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
            }
//            if(!"".equals(et_ransomNum.getText().toString().trim()) ||
//                    !"0".equals(et_ransomNum.getText().toString().trim()) ||
//                    !et_ransomNum.getText().toString().trim().startsWith("00") ||
//                    ((et_ransomNum.getText().toString().trim().length()>=2 && !et_ransomNum.getText().toString().trim().startsWith("0.")))){
            if(!StringUtil.isNullOrEmpty(et_ransomNum.getText().toString().trim()) &&
                    !et_ransomNum.getText().toString().trim().startsWith(".") &&
//                    !et_ransomNum.getText().toString().trim().startsWith("00") &&
                    Double.parseDouble(et_ransomNum.getText().toString().trim()) >= 0){
                et_ransomNum.setText(StringUtil.append2Decimals(et_ransomNum.getText().toString().trim(),4));
                et_ransomNum.setSelection(et_ransomNum.getText().toString().length());
                return true;
            }else{
                return false;
            }
        };
        return super.dispatchKeyEvent(event);
    }

    OnTouchListener touchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
              /*隐藏软键盘*/
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if(inputMethodManager.isActive()){
                inputMethodManager.hideSoftInputFromWindow(RansomMainActivity.this.getCurrentFocus().getWindowToken(), 0);
            }
            if(!StringUtil.isNullOrEmpty(et_ransomNum.getText().toString().trim()) &&
                    !et_ransomNum.getText().toString().trim().startsWith(".") &&
//                    !et_ransomNum.getText().toString().trim().startsWith("00") &&
                    Double.parseDouble(et_ransomNum.getText().toString().trim()) >= 0){
                et_ransomNum.setText(StringUtil.append2Decimals(et_ransomNum.getText().toString().trim(),4));
                et_ransomNum.setSelection(et_ransomNum.getText().toString().length());
                return true;
            }else{
                return false;
            }
        }
    };


}
