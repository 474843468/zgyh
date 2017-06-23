package com.chinamworld.bocmbci.userwidget.securityview;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDeviceSubmit.PsnSvrRegisterDeviceSubmitParams;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DeviceInfoUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.ui.ChooseOpenBankContact;
import com.cfca.mobile.device.SecResult;
import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.base.application.CommonApplication;
import com.chinamworld.bocmbci.utils.DeviceInfoTools;
import com.chinamworld.bocmbci.utils.SipBoxUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/20.
 */
public class SelectSecurityView extends FrameLayout implements View.OnClickListener,SecurityVerity.VerifyCodeResultListener{
    Activity mActivity;

    CombinListBean mCurCombinListBean;

    /**
     * 获得当前选中的安全因子
     * @return
     */
    public CombinListBean getCurCombinListBean(){
        return mCurCombinListBean;
    }
    private String mRandom;

    public SelectSecurityView(Context context) {
        super(context);
        initView(context);
    }

    public SelectSecurityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SelectSecurityView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private TextView selectSecurityTV;

    private ISecurityChooseListener mISecurityChooseListener;
    public void setISecurityChooseListener(ISecurityChooseListener listener){
        mISecurityChooseListener = listener;
    }
    public interface ISecurityChooseListener{
        void onEncryptDataReturned(String factorId, String[] randomNums, String[] encryptPasswords,Map<String,Object> security);
    }

    private void initView(Context context){
        mActivity = (Activity)context;
        LayoutInflater.from(context).inflate(R.layout.llbt_select_security_view,this,true);
        selectSecurityTV = (TextView)findViewById(R.id.select_tv);
        selectSecurityTV.setOnClickListener(this);
        findViewById(R.id.changed_view).setOnClickListener(this);
        SecurityVerity.getInstance(mActivity).setSecurityVerifyListener(this);
        mCurCombinListBean = getDefaultSecurityFactorId(CommonApplication.getInstance().getSecurityIdList(),CommonApplication.getInstance().getSecurityNameList(),CommonApplication.getInstance().getDefaultCombinId());
        selectSecurityTV.setText(mCurCombinListBean == null ? "请选择":mCurCombinListBean.getName());
    }

    /**
     * 更换安全工具认证方式
     */
    private void selectSecurityType(){
        //更换安全认证方式
        SecurityVerity.getInstance(mActivity).selectSecurityType();
    }

    public CombinListBean  getDefaultSecurityFactorId(ArrayList<String> securityIdList, ArrayList<String> securityNameList, String defaultId){
        LLBTSecurityFactorModel llbtSecurityFactorModel = new LLBTSecurityFactorModel(securityIdList,securityNameList,defaultId);
        return  SecurityVerity.getInstance(mActivity).getDefaultSecurityFactorId(llbtSecurityFactorModel);
    }

    /**
     * 弹出选择的安全认证工具
     * @param conversationID
     * @param random 随机数
     * @param preMap 预交易返回数据
     */
    public void showSecurityDialog(String conversationID,String random,Map<String,Object> preMap){
        //弹出选择的安全认证工具
        mRandom = random;
        if(mCurCombinListBean != null && "4".equals(mCurCombinListBean.getId())){
            if(preMap != null && preMap.containsKey("_plainData"))
                EShieldVerify.getInstance(mActivity).setmPlainData((String)preMap.get("_plainData"));
        }
        SecurityVerity.getInstance().setConversationId(conversationID);
        SecurityVerity.getInstance().showSecurityDialog(mRandom);

    }



    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.changed_view){
            selectSecurityType();
        }
    }

    @Override
    public void onSecurityTypeSelected(CombinListBean bean) {
        mCurCombinListBean = bean;
        selectSecurityTV.setText(bean.getName());
    }

    Map<String,Object> securityMap;
    public Map<String,Object> getSecurityMap(){
        return securityMap;
    }
    @Override
    public void onEncryptDataReturned(String factorId, String[] randomNums, String[] encryptPasswords) {
        securityMap = initSecurityParams(factorId,randomNums,encryptPasswords);
        if(mISecurityChooseListener != null){
            mISecurityChooseListener.onEncryptDataReturned(factorId,randomNums, encryptPasswords,securityMap);
        }
    }

    /**
     * 封装安全工具参数
     */
    private Map<String, Object> initSecurityParams(
            String factorId, String[] randomNums, String[] encryptPasswords) {

        Map<String, Object> mParams = new HashMap<String, Object>();
        if ("8".equals(factorId)) {
            //动态口令
            mParams.put("Otp", encryptPasswords[0]);
            mParams.put("Otp_RC", randomNums[0]);
        } else if ("32".equals(factorId)) {
            //短信验证码
            mParams.put("Smc", encryptPasswords[0]);
            mParams.put("Smc_RC", randomNums[0]);
        }
        else if ("96".equals(factorId)) {
            //短信验证码
            mParams.put("Smc", encryptPasswords[0]);
            mParams.put("Smc_RC", randomNums[0]);
            DeviceInfoModel deviceInfoModel = DeviceInfoUtils.getDeviceInfo(mActivity, mRandom);
            mParams.put("deviceInfo", deviceInfoModel.getDeviceInfo());
            mParams.put("deviceInfo_RC", deviceInfoModel.getDeviceInfo_RC());
        }
        else if ("40".equals(factorId)) {
            //动态口令+短信验证码
            mParams.put("Otp", encryptPasswords[0]);
            mParams.put("Otp_RC", randomNums[0]);
            mParams.put("Smc", encryptPasswords[1]);
            mParams.put("Smc_RC", randomNums[1]);
        }
        else if ("4".equals(factorId)) {
            //动态口令+短信验证码
            mParams.put("_signedData", randomNums[0]);
        }

        mParams.put("activ", ApplicationContext.getInstance().getActive());
        mParams.put("state", ApplicationContext.getInstance().getState());

        return mParams;
    }


    @Override
    public void onSignedReturn(String signRetData) {
        securityMap =new HashMap<String, Object>();
        securityMap.put("_signedData", signRetData);
        if(mISecurityChooseListener != null){
            mISecurityChooseListener.onEncryptDataReturned(mCurCombinListBean.getId(),null, null,securityMap);
        }
    }
}
