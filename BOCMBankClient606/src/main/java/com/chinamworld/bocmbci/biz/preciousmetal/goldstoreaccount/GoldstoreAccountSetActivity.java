package com.chinamworld.bocmbci.biz.preciousmetal.goldstoreaccount;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.preciousmetal.PreciousmetalBaseActivity;
import com.chinamworld.bocmbci.biz.preciousmetal.PreciousmetalDataCenter;
import com.chinamworld.bocmbci.biz.preciousmetal.goldstoremainpage.GoldstoreAgreementActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.IHttpResponseCallBack;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.CommPublicTools;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.llbt.model.AccountItem;
import com.chinamworld.llbt.model.IActionCall;
import com.chinamworld.llbt.userwidget.dialogview.MessageDialog;
import com.chinamworld.llbt.userwidget.selectaccountview.SelectAccountButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//签约账户的主界面
public class GoldstoreAccountSetActivity extends PreciousmetalBaseActivity {

    private TextView agreement;
    private Button next_button;
    //姓名
    private TextView edtext_name;
    //电话号码
    private EditText edtext_phonenumber;
    //地址
    private EditText edtext_address;
    //邮编
    private EditText edtext_mail;
    //勾选框
    private CheckBox acc_checkbox;
    //自定义控件
    private SelectAccountButton selectButton;
    //账户number
    private String accountnumber;
    //账户ID
    private String accountID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goldstore_account_set);
        setTitle(R.string.goldstore_accountset_tittle);
        initView();
    }
    public void initView(){
        TextView  metalRight=(TextView)findViewById(R.id.ib_top_right_btn);
        metalRight.setVisibility(View.GONE);

        agreement = (TextView) findViewById(R.id.agreement);
        String head = "我已仔细阅读理解";
        String middle = "《中国银行股份有限公司贵金属积存业务协议书》";
        String end = "协议，完全同意和接受协议书中全部条款和内容，愿意履行和承担该协议书中约定的协议和任务。";
        SpannableString sp = new SpannableString(head + middle + end);
        TextViewNoSigned myStringSpan = new TextViewNoSigned();
        sp.setSpan(myStringSpan, head.length(), head.length() + middle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        agreement.setText(sp);
        agreement.setMovementMethod(LinkMovementMethod.getInstance());
        edtext_name=(TextView)findViewById(R.id.edtext_name);
        edtext_name.setText(PreciousmetalDataCenter.getInstance().AccountName);
        edtext_phonenumber=(EditText)findViewById(R.id.edtext_phonenumber);
        edtext_phonenumber.setText(PreciousmetalDataCenter.getInstance().AccountPhoneMunber);
        edtext_phonenumber.setSelection(edtext_phonenumber.getText().length());
        edtext_address =(EditText)findViewById(R.id.edtext_address);
        edtext_address.setText(PreciousmetalDataCenter.getInstance().AccountAddress);
        edtext_address.setSelection(edtext_address.getText().length());
        edtext_mail=(EditText)findViewById(R.id.edtext_mail);
        edtext_mail.setText(PreciousmetalDataCenter.getInstance().AccountMail);
        edtext_mail.setSelection(edtext_mail.getText().length());
        acc_checkbox=(CheckBox)findViewById(R.id.acc_checkbox);
        acc_checkbox.setChecked(true);
        TextView back=(TextView) findViewById(R.id.ib_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityTaskManager.getInstance().removeAllSecondActivity();
            }
        });


        selectButton=(SelectAccountButton) findViewById(R.id.selectbutton);
        selectButton.setTemplateType(2);
        selectButton.setLeftText(1);
        selectButton.setRequestAccountListCall(new IActionCall() {
            @Override
            public void callBack() {

                // 调用查询账户列表接口
                //回掉中调用gotoSelectedAccountActivity方法,把数据1传入方法中，注意方法类型
                requestPsnCommonQueryAllChinaBankAccount();

            }
        });


        next_button=(Button) findViewById(R.id.next_button);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //判断各项输入数据是否为空
                if(StringUtil.isNullOrEmpty(accountnumber)){
//                    BaseDroidApp.getInstanse().showInfoMessageDialog("请选择账户");
                    MessageDialog.showMessageDialog(GoldstoreAccountSetActivity.this,"请选择交易账户");
                    return;
                }
                if(StringUtil.isNullOrEmpty(edtext_phonenumber.getText())){
//                    BaseDroidApp.getInstanse().showInfoMessageDialog("请输入手机号");
                    MessageDialog.showMessageDialog(GoldstoreAccountSetActivity.this,"请输入电话号码");
                    return;
                }
//                if(StringUtil.isNullOrEmpty(edtext_address.getText())){
////                    BaseDroidApp.getInstanse().showInfoMessageDialog("请输入地址");
//                    MessageDialog.showMessageDialog(GoldstoreAccountSetActivity.this,"请输入地址");
//                    return;
//                }
//                if(StringUtil.isNullOrEmpty(edtext_mail.getText())){
////                    BaseDroidApp.getInstanse().showInfoMessageDialog("请输入邮编");
//                    MessageDialog.showMessageDialog(GoldstoreAccountSetActivity.this,"请输入邮编");
//                    return;
//                }

//                if(StringUtil.isNullOrEmpty(edtext_name.getText())){
////                    BaseDroidApp.getInstanse().showInfoMessageDialog("请输入姓名");
//                    MessageDialog.showMessageDialog(GoldstoreAccountSetActivity.this,"请输入姓名");
//                    return;
//                }
                if(!acc_checkbox.isChecked()){
//                    BaseDroidApp.getInstanse().showInfoMessageDialog(
//                            getResources().getString(
//                                    R.string.goldbonus_remind_message));
                    MessageDialog.showMessageDialog(GoldstoreAccountSetActivity.this,"请仔细阅读协议并勾选后再确认提交");
                    return;
                }
                ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
//                //格式校验
                RegexpBean name = new RegexpBean(PreciousmetalDataCenter.GOLDSTORENUMBER, edtext_phonenumber.getText().toString().trim(), PreciousmetalDataCenter.CUSTPHONENUM);
                lists.add(name);
                if(!"".equals(edtext_address.getText().toString().trim())){
                    if(!StringUtil.isNullOrEmpty(edtext_address.getText())){
                        RegexpBean address = new RegexpBean(PreciousmetalDataCenter.GOLDSTOREADDRESS, edtext_address.getText().toString().trim(), PreciousmetalDataCenter.CUSTADDRESS);
                        lists.add(address);
                    }

                }

                if(!StringUtil.isNullOrEmpty(edtext_mail.getText())){
                    RegexpBean post = new RegexpBean(PreciousmetalDataCenter.GOLDSTOREMAIL, edtext_mail.getText().toString().trim(), PreciousmetalDataCenter.POSTCODE);
                    lists.add(post);
                }
                if(!RegexpUtils.regexpData(lists)) return;//校验不过

                if(CommPublicTools.getLength(edtext_address.getText().toString())>150){
                    MessageDialog.showMessageDialog(GoldstoreAccountSetActivity.this,"地址信息不得超过150个字符");
                    return;
                }
                requestCommConversationId();
////                /**随机数请求*/
////                requestForRandomNumber();
//                //签约账户
//                requestGetSecurityFactor("PB149");

            }
        });

    }
    @Override
    public void requestCommConversationIdCallBack(Object resultObj) {
        super.requestCommConversationIdCallBack(resultObj);
        //签约账户
        requestGetSecurityFactor("PB149");
    }
    @Override
    public void requestGetSecurityFactorCallBack(Object resultObj) {
        super.requestGetSecurityFactorCallBack(resultObj);

        /**随机数请求*/
        requestForRandomNumber();
//        BaseDroidApp.getInstanse().showSeurityChooseDialog(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        BaseHttpEngine.showProgressDialog();;
//                        requestPsnGoldStoreCreateAccountConfirm();
//                    }
//
//                });

    }
    private void requestPsnGoldStoreCreateAccountConfirm(){
        Map<String, Object> params = new HashMap<>();
        AccountItem accountItem =selectButton.getData();
        Map<String, Object> accountMap = (Map<String, Object>) accountItem.getSource();
        params.put("accountId",accountMap.get("accountId"));
        params.put("accountNumber", accountnumber);
        params.put("_combinId", BaseDroidApp.getInstanse().getSecurityChoosed());
        httpTools.requestHttpWithConversationId(PreciousmetalDataCenter.PSNGOLDSTORECREATEACCOUNTCONFIRM, params, (String) BaseDroidApp.getInstanse()
                .getBizDataMap().get(ConstantGloble.CONVERSATION_ID), new IHttpResponseCallBack<Map<String, Object>>() {
            @Override
            public void httpResponseSuccess(Map<String, Object> result, String method) {
                PreciousmetalDataCenter.getInstance().resultPsnGoldStore = result;
                /**随机数请求*/
                requestForRandomNumber();
            }
        });
    }
    private void requestForRandomNumber(){
        httpTools.requestHttpWithConversationId(Login.COMM_RANDOM_NUMBER_API, null, (String) BaseDroidApp.getInstanse()
                .getBizDataMap().get(ConstantGloble.CONVERSATION_ID), new IHttpResponseCallBack<String>() {
            @Override
            public void httpResponseSuccess(String result, String s) {
                Intent intent = new Intent();
                intent.setClass(GoldstoreAccountSetActivity.this,GoldstoreAccountComfirActivity.class);
                //1为签约。2为重设账户
                intent.putExtra("FLAG","1");
                intent.putExtra("NAME",edtext_name.getText().toString());
                intent.putExtra("NUMBER",edtext_phonenumber.getText().toString());
                intent.putExtra("ADDRESS",edtext_address.getText().toString());
                intent.putExtra("MAIL",edtext_mail.getText().toString());
                intent.putExtra("ACCOUNTNUMBER",accountnumber);
                intent.putExtra("random", result);
                startActivity(intent);
            }
        });
    }




    /** 点击这里 请求账户列表跳转Activity */
    private class TextViewNoSigned extends ClickableSpan {

        @Override
        public void onClick(View widget) {
            //点击字体跳转协议界面
            Intent intent =new Intent();
            intent.setClass(GoldstoreAccountSetActivity.this,GoldstoreAgreementActivity.class);
            startActivity(intent);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(getResources().getColor(R.color.red)); // 设置字体颜色
            ds.setUnderlineText(false); // 设置下划线 true显示下划线 false为不显示下划线.
        }
    }
    /**
     * 查询用户的所有账户
     */
    public void requestPsnCommonQueryAllChinaBankAccount() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String[] s = { "188", "119" };
        paramMap.put(Crcd.CRCD_ACCOUNTTYPE_REQ, s);
        getHttpTools().requestHttp(Crcd.QRY_CRCD_LIST_API,
                "requestPsnCommonQueryAllChinaBankAccountCallBack", paramMap,
                false);
    }
    /** 获取借记卡列表----回调 */
    public void requestPsnCommonQueryAllChinaBankAccountCallBack(
            Object resultObj) {
        BaseHttpEngine.dissMissProgressDialog();
        List<Map<String, Object>> result =  HttpTools.getResponseResult(resultObj);
        if (StringUtil.isNullOrEmpty(result)) {
            MessageDialog.showMessageDialog(GoldstoreAccountSetActivity.this,"您没有可进行贵金属积存交易的借记卡或活期一本通，请到柜台关联");
            return;
        }
        List<AccountItem> itemList=new ArrayList<AccountItem>();

        for (int i=0;i<result.size();i++){
            AccountItem accountItem=new AccountItem(result.get(i));
            if(StringUtil.isNullOrEmpty(result.get(i).get("nickName"))){
                    accountItem.setAccountName(Accountstyle.get((String)result.get(i).get("accountType")));
            }else{
                    accountItem.setAccountName((String)result.get(i).get("nickName"));
            }
            itemList.add(accountItem);
        }
        selectButton.gotoSelectedAccountActivity(itemList);
    }

    /** 校验 */
    private boolean submitRegexp(boolean required) {
        ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
        if (onlyRegular(required, edtext_phonenumber.getText().toString().trim())) {
            RegexpBean name = new RegexpBean(PreciousmetalDataCenter.GOLDSTORENUMBER, edtext_phonenumber.getText().toString().trim(), PreciousmetalDataCenter.CUSTPHONENUM);
            lists.add(name);

        }
        if (onlyRegular(required, edtext_address.getText().toString().trim())) {
            RegexpBean address = new RegexpBean(PreciousmetalDataCenter.GOLDSTOREADDRESS, edtext_address.getText().toString().trim(), PreciousmetalDataCenter.CUSTADDRESS);
            lists.add(address);
        }
        if (onlyRegular(required, edtext_mail.getText().toString().trim())) {
            RegexpBean post = new RegexpBean(PreciousmetalDataCenter.GOLDSTOREMAIL, edtext_mail.getText().toString().trim(), PreciousmetalDataCenter.POSTCODE);
            lists.add(post);
        }
        if (!RegexpUtils.regexpDate(lists)) {
            return false;
        }
        return true;
    }

    /** 只作正则校验 */
    private boolean onlyRegular(Boolean required, String content) {
        if ((!required && !StringUtil.isNull(content)) || required) {
            return true;
        }
        return false;
    }


    @Override
    protected void onRestart() {
        super.onRestart();

        AccountItem accountItem =selectButton.getData();
        if(!StringUtil.isNullOrEmpty(accountItem)){
            Map<String,Object> map=(Map<String,Object>)accountItem.getSource();
            if(!StringUtil.isNullOrEmpty((String)map.get("accountId"))){
                accountID=(String)map.get("accountId");
                PreciousmetalDataCenter.getInstance().AccountIDOld=accountID;
            }
            if(!StringUtil.isNullOrEmpty(selectButton.getData().getAccountNum())){
                accountnumber= selectButton.getData().getAccountNum();
                String accnickname=(String)map.get("nickName");
                String accounttype=(String) map.get("accountType");
                PreciousmetalDataCenter.getInstance().AccNikeName=accnickname;
                PreciousmetalDataCenter.getInstance().AccountType=accounttype;
                PreciousmetalDataCenter.getInstance().AccountNumberOld=accountnumber;
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
            ActivityTaskManager.getInstance().removeAllSecondActivity();
        }

        return super.onKeyDown(keyCode, event);

    }

    @Override
    protected void onResume() {
        super.onResume();
        BaseHttpEngine.dissMissProgressDialog();
    }



}
