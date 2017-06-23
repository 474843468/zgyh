package com.chinamworld.bocmbci.biz.preciousmetal.goldstoreaccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.preciousmetal.PreciousmetalBaseActivity;
import com.chinamworld.bocmbci.biz.preciousmetal.PreciousmetalDataCenter;
import com.chinamworld.bocmbci.biz.preciousmetal.goldstorebuy.BuyMainActivity;
import com.chinamworld.bocmbci.biz.preciousmetal.goldstoremainpage.GoldstoreMainActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.CommPublicTools;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.RegexpUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.llbt.model.AccountItem;
import com.chinamworld.llbt.model.IActionCall;
import com.chinamworld.llbt.userwidget.dialogview.MessageDialog;
import com.chinamworld.llbt.userwidget.selectaccountview.SelectAccountButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoldstoreAccountResetActivity extends PreciousmetalBaseActivity {
    //原来的账户
    private TextView account_number_old;
    //姓名
    private TextView account_name;
    //电话输入框

    private EditText edtext_phonenumber;
    //地址
    private EditText edtext_address;
    //邮编
    private EditText edtext_mail;
    //确定按钮
    private Button sure_button;
    //对话框
    private CustomDialog customDialog;
    /** 获取到的tokenId 保存 */
    private String tokenId;
    //账户number
    private String accountnumber;
    //账户类型
    private String accounttype;
    //要弹出的对话框
    private View dialogView;
    //自定义控件
    private SelectAccountButton selectButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goldstore_account_reset);
        setTitle(R.string.goldstore_accountreset_tittle);
        initView();
    }
    private void initView(){
        TextView back = (TextView)findViewById(R.id.ib_back);
        // back.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if("0".equals(PreciousmetalDataCenter.getInstance().BUYFLAG_CHANGE)){
                    finish();
                }else{
                    Intent intent=new Intent();
                    intent.setClass(GoldstoreAccountResetActivity.this, GoldstoreMainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
        TextView  metalRight=(TextView)findViewById(R.id.ib_top_right_btn);
        metalRight.setVisibility(View.GONE);
        account_number_old=(TextView)findViewById(R.id.account_number_old);
        account_number_old.setText(StringUtil
                .getForSixForString(PreciousmetalDataCenter.getInstance().AccountNumberOld));
        account_name=(TextView)findViewById(R.id.account_name);
        account_name.setText(PreciousmetalDataCenter.getInstance().AccountName);
        edtext_phonenumber=(EditText)findViewById(R.id.edtext_phonenumber);

        edtext_phonenumber.setText(PreciousmetalDataCenter.getInstance().AccountPhoneMunber);
        edtext_phonenumber.setSelection(edtext_phonenumber.getText().length());
        edtext_address=(EditText)findViewById(R.id.edtext_address);
        edtext_address.setSelection(edtext_address.getText().length());
        edtext_address.setText(PreciousmetalDataCenter.getInstance().AccountAddress);

        edtext_mail=(EditText)findViewById(R.id.edtext_mail);
        edtext_mail.setText(PreciousmetalDataCenter.getInstance().AccountMail);
        edtext_mail.setSelection(edtext_mail.getText().length());
        selectButton=(SelectAccountButton) findViewById(R.id.selectbutton);
        selectButton.setTemplateType(2);
        selectButton.setLeftText(2);
        selectButton.setRequestAccountListCall(new IActionCall() {
            @Override
            public void callBack() {

                // 调用查询账户列表接口
                //回掉中调用gotoSelectedAccountActivity方法,把数据1传入方法中，注意方法类型
                requestPsnCommonQueryAllChinaBankAccount();

            }
        });

        sure_button=(Button)findViewById(R.id.sure_button);
        dialogView = View.inflate(GoldstoreAccountResetActivity.this,
                R.layout.goldstore_accountsetsuccess, null);
        Button dialog_sure=(Button)dialogView.findViewById(R.id.dialog_sure);
        dialog_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //当用户是点击购买进入设定账户时，点击确认返回到购买页面
                //当用户是从别的途径进入设定账户时，点击确认返回到首页
                if("0".equals(PreciousmetalDataCenter.getInstance().BUYFLAG_CHANGE)){
                    customDialog.cancel();
                    Intent intent =new Intent(GoldstoreAccountResetActivity.this,BuyMainActivity.class);
                    startActivity(intent);
                    finish();

                }else{
                    customDialog.cancel();
                    Intent intent =new Intent(GoldstoreAccountResetActivity.this,GoldstoreMainActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        });
        customDialog=new CustomDialog(GoldstoreAccountResetActivity.this,dialogView);
//                customDialog.show();



        sure_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StringUtil.isNullOrEmpty(accountnumber)){
//                    BaseDroidApp.getInstanse().showInfoMessageDialog("请选择账户");
                    MessageDialog.showMessageDialog(GoldstoreAccountResetActivity.this,"请选择交易账户");
                    return;
                }
                if(StringUtil.isNullOrEmpty(edtext_phonenumber.getText())){
//                    BaseDroidApp.getInstanse().showInfoMessageDialog("请输入手机号");
                    MessageDialog.showMessageDialog(GoldstoreAccountResetActivity.this,"请输入电话号码");
                    return;
                }
//                if(StringUtil.isNullOrEmpty(edtext_address.getText())){
////                    BaseDroidApp.getInstanse().showInfoMessageDialog("请输入地址");
//                    MessageDialog.showMessageDialog(GoldstoreAccountResetActivity.this,"请输入地址");
//                    return;
//                }
//                if(StringUtil.isNullOrEmpty(edtext_mail.getText())){
////                    BaseDroidApp.getInstanse().showInfoMessageDialog("请输入邮编");
//                    MessageDialog.showMessageDialog(GoldstoreAccountResetActivity.this,"请输入邮编");
//                    return;
//                }
                ArrayList<RegexpBean> lists = new ArrayList<RegexpBean>();
//                //格式校验
                RegexpBean name = new RegexpBean(PreciousmetalDataCenter.GOLDSTORENUMBER, edtext_phonenumber.getText().toString().trim(), PreciousmetalDataCenter.CUSTPHONENUM);
                lists.add(name);
                if(!"".equals(edtext_address.getText().toString().trim())) {
                    if (!StringUtil.isNullOrEmpty(edtext_address.getText()) || "".equals(edtext_address.getText())) {
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
                    MessageDialog.showMessageDialog(GoldstoreAccountResetActivity.this,"地址信息不得超过150个字符");
                    return;
                }

                //跳转确认界面，没有安全工具那一行
                PreciousmetalDataCenter.getInstance().curAccountItem = selectButton.getData();
                requestCommConversationId();
//                Intent intent= new Intent();
//                intent.setClass(GoldstoreAccountResetActivity.this,GoldstoreAccountComfirActivity.class);
//                //2为变更账户。1为签约
//                intent.putExtra("FLAG","2");
//                intent.putExtra("NUMBER",edtext_phonenumber.getText().toString());
//                intent.putExtra("ADDRESS",edtext_address.getText().toString());
//                intent.putExtra("MAIL",edtext_mail.getText().toString());
//                startActivity(intent);
            }
        });
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
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        List<Map<String, Object>> result = (List<Map<String, Object>>) biiResponseBody
                .getResult();
        if (StringUtil.isNullOrEmpty(result)) {
            MessageDialog.showMessageDialog(this,"您没有其他可进行贵金属积存交易的借记卡或活期一本通");
            return;
        }
        List<AccountItem> itemList=new ArrayList<AccountItem>();
        for(int j=0;j<result.size();j++){
            if(PreciousmetalDataCenter.getInstance().AccountNumberOld.equals(
                    result.get(j).get("accountNumber"))){
                result.remove(j);
            }
        }
        if (StringUtil.isNullOrEmpty(result)) {
            MessageDialog.showMessageDialog(this,"您没有其他可进行贵金属积存交易的借记卡或活期一本通");
            return;
        }
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
    @Override
    protected void onRestart() {
        super.onRestart();

        AccountItem accountItem =selectButton.getData();
        if(!StringUtil.isNullOrEmpty(accountItem)){
            Map<String,Object> map=(Map<String,Object>)accountItem.getSource();
            if(!StringUtil.isNullOrEmpty((String)map.get("accountId"))){
                String accountID=(String)map.get("accountId");
                PreciousmetalDataCenter.getInstance().AccountIDOld=accountID;
            }
            if(!StringUtil.isNullOrEmpty(selectButton.getData().getAccountNum())){
                accountnumber= selectButton.getData().getAccountNum();
                accounttype=(String) map.get("accountType");
                PreciousmetalDataCenter.getInstance().AccountType=accounttype;
                PreciousmetalDataCenter.getInstance().AccountNumberOld=accountnumber;
            }
        }

    }
    @Override
    public void requestCommConversationIdCallBack(Object resultObj) {
        super.requestCommConversationIdCallBack(resultObj);
        //点确定调用贵金属积存账户重设接口
        //请求token值
        pSNGetTokenId();

    }
    /**
     * 获取tokenId
     */
    public void pSNGetTokenId() {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
        biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
                .getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
        BaseHttpEngine.showProgressDialog();
        HttpManager.requestBii(biiRequestBody, this, "aquirePSNGetTokenId");
    }
    /**
     * 处理获取tokenId的数据得到tokenId
     *
     * @param resultObj
     *            服务器返回数据
     */
    public void aquirePSNGetTokenId(Object resultObj) {
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        tokenId = (String) biiResponseBody.getResult();
        if (StringUtil.isNull(tokenId)) {
            BaseHttpEngine.dissMissProgressDialog();
            return;
        }
        //根据不同flag掉不同接口，在接口回掉弹对话框
        //1为账户签约。2为变更账户
//        if("1".equals(intent.getStringExtra("FLAG"))){
////            //调用签约接口
//            requestPsnGoldStoreCreateAccount();
//        }else{
        //账户重设接口
        requestPsnGoldStoreModifyAccount(tokenId);
//        }


    }
    private void requestPsnGoldStoreModifyAccount(String tokenId) {
        // TODO Auto-generated method stub
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("custName",(String)((Map<String,Object>)PreciousmetalDataCenter.getInstance().ACCOUNTQUERYMAP.get(PreciousmetalDataCenter.CUSINFO)).get(PreciousmetalDataCenter.CUSTNAME));// 客户名称
        paramMap.put("custPhoneNum", edtext_phonenumber.getText());// 电话号码
        paramMap.put("custAddress", edtext_address.getText());//地址
        paramMap.put("postCode", edtext_mail.getText());//邮编
        paramMap.put("bankAccountId", ((Map<String,Object>)PreciousmetalDataCenter.getInstance().curAccountItem.getSource()).get("accountId"));//银行账户id
        paramMap.put("formerbankAccountId",((Map<String,Object>)PreciousmetalDataCenter.getInstance().ACCOUNTQUERYMAP.get(PreciousmetalDataCenter.CUSINFO)).get("accountId"));//银行账户number


        paramMap.put("accountNumber",accountnumber);//银行账户number
        paramMap.put("token", tokenId);//防重机制
        getHttpTools().requestHttp(
                PreciousmetalDataCenter.PSNGOLDSTOREMODIFYACCOUNT,
                "requestPsnGoldStoreModifyAccountCallBack", paramMap,
                true);
    }
    public void requestPsnGoldStoreModifyAccountCallBack(Object resultObj) {
        BaseHttpEngine.dissMissProgressDialog();
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
                .getResult();
        customDialog.show();
    }

}
