package com.chinamworld.bocmbci.biz.preciousmetal.goldstoreaccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.base.model.BaseSubmitBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DeviceInfoUtils;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.acc.AccBaseActivity;
import com.chinamworld.bocmbci.biz.preciousmetal.PreciousmetalBaseActivity;
import com.chinamworld.bocmbci.biz.preciousmetal.PreciousmetalDataCenter;
import com.chinamworld.bocmbci.biz.preciousmetal.goldstorebuy.BuyMainActivity;
import com.chinamworld.bocmbci.biz.preciousmetal.goldstoremainpage.GoldstoreMainActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.GetPhoneInfo;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.IHttpResponseCallBack;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.userwidget.securityview.SelectSecurityView;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.SipBoxUtils;
import com.chinamworld.bocmbci.utils.SmsCodeUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.bocmbci.widget.IUsbKeyTextSuccess;
import com.chinamworld.bocmbci.widget.UsbKeyText;
import com.chinamworld.llbt.model.AccountItem;
import com.chinamworld.llbt.userwidget.dialogview.LoadingDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cfca.mobile.sip.SipBox;

public class GoldstoreAccountComfirActivity extends PreciousmetalBaseActivity implements  SelectSecurityView.ISecurityChooseListener{
    //账户
    private TextView account_number;



    //姓名
    private TextView account_name;
    //电话号码
    private TextView account_phone_number;
    //地址
    private TextView account_address;
    //邮编
    private TextView account_mail;
    //确认按钮
    private Button sure_button;
    //上一步按钮
    private Button back_button;
    private  Intent intent;
    //账户类型
    private TextView acc_type;
    //要弹出的对话框
    private View dialogView;
    //对话框
    private CustomDialog customDialog;
    //安全工具layout
    private LinearLayout safetool_layout;
    /** 获取到的tokenId 保存 */
    private String tokenId;
    /**random随机数*/
    private String random;
    /**预交易返回安全工具*/
    private Map<String, Object> resultPsnGoldStore;
    /**动态口令是否存在*/
    private Boolean isOtp;
    /**是否存在手机交易码*/
    private Boolean isSmc;
//    /*中银E盾**/
//    private UsbKeyText usbKeyText;
//    /**动态口令*/
//    private SipBox otpSipBxo;
//    /**短信验证码*/
//    private SipBox smcSipBxo;
    SelectSecurityView mSelectSecurityView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goldstore_account_comfir);
        setTitle(R.string.goldstore_accountconfirm_tittle);
        TextView  metalRight=(TextView)findViewById(R.id.ib_top_right_btn);
        metalRight.setVisibility(View.GONE);
        mSelectSecurityView = (SelectSecurityView)findViewById(R.id.securityView);
        mSelectSecurityView.setISecurityChooseListener(this);
        account_number=(TextView)findViewById(R.id.account_number);
        account_name=(TextView)findViewById(R.id.account_name);
        account_phone_number=(TextView)findViewById(R.id.account_phone_number);
        account_address=(TextView)findViewById(R.id.account_address);
        PopupWindowUtils.getInstance().setOnShowAllTextListener(this,
                account_address);
        account_mail=(TextView)findViewById(R.id.account_mail);
        sure_button=(Button)findViewById(R.id.sure_button);
        back_button=(Button)findViewById(R.id.back_button);
        safetool_layout=(LinearLayout)findViewById(R.id.safetool_layout);
        acc_type=(TextView) findViewById(R.id.acc_type);
        if(StringUtil.isNullOrEmpty(PreciousmetalDataCenter.getInstance().AccNikeName)){
            acc_type.setText(PreciousmetalDataCenter.Accountstyle.get(PreciousmetalDataCenter.getInstance().AccountType));
        }else{
            acc_type.setText((PreciousmetalDataCenter.getInstance().AccNikeName));
        }

          intent=getIntent();
        //1为账户签约。2为变更账户

            account_number.setText(StringUtil.getForSixForString(intent.getStringExtra("ACCOUNTNUMBER")));
            account_name.setText(intent.getStringExtra("NAME"));
            account_phone_number.setText(intent.getStringExtra("NUMBER"));
            account_address.setText(intent.getStringExtra("ADDRESS"));
            account_mail.setText(intent.getStringExtra("MAIL"));
            random = intent.getStringExtra("random");
            resultPsnGoldStore = PreciousmetalDataCenter.getInstance().resultPsnGoldStore;
//           initSipBox();


        sure_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                requestCommConversationId();
//                if("1".equals(intent.getStringExtra("FLAG"))) {
//                    //先调预交易接口
//                    requestCommConversationId();
//
//
//                }else {
//                    requestCommConversationId();
//                }
                requestPsnGoldStoreCreateAccountConfirm();
                //弹出对话框并且保存老帐户数据，用于变更账户
                PreciousmetalDataCenter.getInstance().AccountName=intent.getStringExtra("NAME");
                PreciousmetalDataCenter.getInstance().AccountPhoneMunber=intent.getStringExtra("NUMBER");
                PreciousmetalDataCenter.getInstance().AccountAddress=intent.getStringExtra("ADDRESS");
                PreciousmetalDataCenter.getInstance().AccountMail=intent.getStringExtra("MAIL");
                dialogView = View.inflate(GoldstoreAccountComfirActivity.this,
                        R.layout.goldstore_accountsetsuccess, null);
                Button dialog_sure=(Button)dialogView.findViewById(R.id.dialog_sure);
                dialog_sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //当用户是点击购买进入设定账户时，点击确认返回到购买页面
                        //当用户是从别的途径进入设定账户时，点击确认返回到首页
                        if("0".equals(PreciousmetalDataCenter.getInstance().BUYFLAG)){
                            Intent intent =new Intent();
                            intent.setClass(GoldstoreAccountComfirActivity.this,BuyMainActivity.class);
                            startActivity(intent);

                        }else{
                            Intent intent2 =new Intent();
                            intent2.setClass(GoldstoreAccountComfirActivity.this,GoldstoreMainActivity.class);
                            startActivity(intent2);
                        }
                        customDialog.cancel();
                    }
                });
                customDialog=new CustomDialog(GoldstoreAccountComfirActivity.this,dialogView);
//                customDialog.show();
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private View.OnClickListener smcOnclickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            sendSMSCToMobile();
        }
    };

    @Override
    public void requestCommConversationIdCallBack(Object resultObj) {
        super.requestCommConversationIdCallBack(resultObj);

        requestGetSecurityFactor("PB149");

    }
    @Override
    public void requestGetSecurityFactorCallBack(Object resultObj) {
        super.requestGetSecurityFactorCallBack(resultObj);
//        BaseDroidApp.getInstanse().showSeurityChooseDialog(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        BaseHttpEngine.showProgressDialog();
//                        /**随机数请求*/
//                        requestPsnGoldStoreCreateAccountConfirm();
//                    }
//
//                });
        requestPsnGoldStoreCreateAccountConfirm();


    }
    private void requestPsnGoldStoreCreateAccountConfirm(){
        Map<String, Object> params = new HashMap<>();

        params.put("accountId",PreciousmetalDataCenter.getInstance().AccountIDOld);
        params.put("accountNumber", intent.getStringExtra("ACCOUNTNUMBER"));
        params.put("_combinId",mSelectSecurityView.getCurCombinListBean().getId());
        httpTools.requestHttpWithConversationId(PreciousmetalDataCenter.PSNGOLDSTORECREATEACCOUNTCONFIRM, params, (String) BaseDroidApp.getInstanse()
                .getBizDataMap().get(ConstantGloble.CONVERSATION_ID), new IHttpResponseCallBack<Map<String, Object>>() {
            @Override
            public void httpResponseSuccess(Map<String, Object> result, String method) {
                PreciousmetalDataCenter.getInstance().resultPsnGoldStore = result;
                BaseHttpEngine.dissMissProgressDialog();
                /** 音频Key安全工具认证 */
                mSelectSecurityView.showSecurityDialog((String) BaseDroidApp.getInstanse()
                        .getBizDataMap().get(ConstantGloble.CONVERSATION_ID),random,result);
            }
        });
    }
    @Override
    public void requestPSNGetTokenIdCallBack(Object resultObj) {
        super.requestPSNGetTokenIdCallBack(resultObj);
        tokenId = (String) BaseDroidApp.getInstanse().getBizDataMap()
                .get(ConstantGloble.TOKEN_ID);
        if (StringUtil.isNull(tokenId)) {
            BaseHttpEngine.dissMissProgressDialog();
            return;
        }
            requestPsnGoldStoreCreateAccount(tokenId);
    }

    private void requestPsnGoldStoreCreateAccount(String tokenId) {
        // TODO Auto-generated method stub
//        Map<String, Object> paramMap = new HashMap<String, Object>();

        Map<String, Object> paramMap = mSelectSecurityView.getSecurityMap();
        GetPhoneInfo getPhoneInfo=new GetPhoneInfo();
        getPhoneInfo.initActFirst(GoldstoreAccountComfirActivity.this);
        getPhoneInfo.addPhoneInfo(paramMap);
//        paramMap.put("devicePrint",DeviceInfoUtils.getDevicePrint(this));
        paramMap.put("custName",(String)((Map<String,Object>)PreciousmetalDataCenter.getInstance().ACCOUNTQUERYMAP.get(PreciousmetalDataCenter.CUSINFO)).get(PreciousmetalDataCenter.CUSTNAME));// 客户名称
        paramMap.put("custPhoneNum", account_phone_number.getText());// 电话号码
        paramMap.put("custAddress", account_address.getText());//地址
        paramMap.put("postCode", account_mail.getText());//邮编
        paramMap.put("accountId", PreciousmetalDataCenter.getInstance().AccountIDOld);//银行账户id
        paramMap.put("accountNumber",PreciousmetalDataCenter.getInstance().AccountNumberOld);//银行账户number
        paramMap.put("token", tokenId);//防重机制
        getHttpTools().requestHttp(
                PreciousmetalDataCenter.PSNGOLDSTORECREATEACCOUNT,
                "requestPsnGoldStoreCreateAccountCallBack", paramMap,
                true);
    }
    public void requestPsnGoldStoreCreateAccountCallBack(Object resultObj) {
        BaseHttpEngine.dissMissProgressDialog();
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        Map<String, Object> resultMap = (Map<String, Object>) biiResponseBody
                .getResult();
        customDialog.show();
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



    @Override
    public void onEncryptDataReturned(String factorId, String[] randomNums, String[] encryptPasswords, Map<String, Object> security) {
//        LoadingDialog.showLoadingDialog(this);
        BaseHttpEngine.showProgressDialog();
        requestPSNGetTokenId((String) BaseDroidApp
                                            .getInstanse().getBizDataMap()
                                            .get(ConstantGloble.CONVERSATION_ID));
    }
}
