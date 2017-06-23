package com.chinamworld.bocmbci.userwidget.investview;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.llbt.userwidget.dialogview.MessageDialog;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 理财类首页参考市值组件
 * Created by Administrator on 2016/10/14.
 */
public class InvestPriceView extends FrameLayout implements View.OnClickListener{

    private Context mContext;
    private View loginBt;
    private View rootLayout,helpImage;
    private ImageView openEyeImage;
    private View mLoginLayout,mLoginOutLayout;
    private TextView amountTV1,amountTV2;
    private String amountStr,formatAmountStr1,formatAmountStr2;
    private OnClickListener mLoginLayoutClickListener = null;
    /** 当前是否是睁眼状态 */
    boolean isOpenEye = true;
    private String contextName;
    private TextView tv_refvalue;
    private FrameLayout mPriceViewLayout,mMypositioned;
    private RelativeLayout mMypositionLayout;
    private OnClickListener mMypositionClickListener = null;
    //登录后显示布局内容
    public enum LoginLayoutType{
        /**参考市值**/
        InvestPriceLayout,
        /**我的持仓**/
        MyPositionLayout,
    }

    public InvestPriceView(Context context) {
        super(context);
        initView(context);

    }

    public InvestPriceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public InvestPriceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void resetWH(){
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        int h = width * 388 / 720;
        LayoutParams l = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,h);
        mLoginOutLayout.setLayoutParams(l);
        mPriceViewLayout.setLayoutParams(l);
        mPriceViewLayout.setPadding(0,h - dip2px(mContext,388) + dip2px(mContext,56) ,0,0);

    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void initView(Context context){

        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.llbt_invest_price_view,this,true);
        rootLayout = findViewById(R.id.rootLayout);
        loginBt = findViewById(R.id.login_bt);
        loginBt.setOnClickListener(this);
        mLoginLayout = findViewById(R.id.loginLayout);
        mLoginOutLayout = findViewById(R.id.loginOutLayout);
        mPriceViewLayout = (FrameLayout) findViewById(R.id.priceview_layout);
        mMypositioned = (FrameLayout) findViewById(R.id.fl_mypositioned);
        mMypositionLayout = (RelativeLayout) findViewById(R.id.my_positioned);
        openEyeImage = (ImageView)findViewById(R.id.img_open);
        tv_refvalue = (TextView) findViewById(R.id.tv_refvalue);
        openEyeImage.setOnClickListener(this);

        String contextString = mContext.toString();
        contextName = contextString.substring(contextString.lastIndexOf(".") + 1, contextString.indexOf("@"));

        /**
         * 参考市值布局点击事件
         */
        mPriceViewLayout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(mLoginLayoutClickListener == null) return;
                mLoginLayoutClickListener.onClick(view);
                return;
            }
        });
        //配合外置广告页面 获取焦点滑动事件
        mLoginOutLayout.setClickable(true);
        //我的持仓点击事件
        mMypositionLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(mMypositionClickListener == null) return;
                mMypositionClickListener.onClick(view);
                return;
            }
        });

        helpImage = findViewById(R.id.ima_help);
//        helpImage.getBackground().setAlpha(120);
        helpImage.setOnClickListener(this);
        amountTV1 = (TextView)findViewById(R.id.amount_tv1);
        amountTV2 = (TextView)findViewById(R.id.amount_tv2);
//        findViewById(R.id.lyt_name).getBackground().setAlpha(120);
        setAmountText(amountStr);
        refreshView();
//        resetWH();
    }

    private void setOpenEyeStatus(boolean openStatus){
        isOpenEye = openStatus;
        openEyeImage.setImageDrawable(isOpenEye ? getResources().getDrawable(R.drawable.llbt_ima_openeye) : getResources().getDrawable(R.drawable.llbt_ima_openeye_close));
        if(isOpenEye == true){
            setAmountText(amountTV1,formatAmountStr1);
            setAmountText(amountTV2,formatAmountStr2);
        }
        else {
            setAmountText(amountTV1,"****");
            setAmountText(amountTV2,null);
        }
    }

    private void setAmountText(TextView tv, String amount){
        if(amount == null){
            tv.setVisibility(View.GONE);
            return;
        }
        tv.setVisibility(View.VISIBLE);
        tv.setText(amount);
    }

    private boolean getLogin(){
        return BaseDroidApp.getInstanse().isLogin();
    }

    /**
     * 刷新页面数据
     */
    public void refreshView(){
        mLoginLayout.setVisibility(getLogin() ? View.VISIBLE : View.GONE);
        mLoginOutLayout.setVisibility(getLogin() == false ? View.VISIBLE : View.GONE);
        setOpenEyeStatus(getStoreEyeStatus());
    }

    private LoginTask.LoginCallback mLoginCallBack;
    /** 设置登录完成后回调*/
    public void setLoginSuccessCallBack(LoginTask.LoginCallback callBack){
        mLoginCallBack = callBack;
    }
    
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.login_bt){
            BaseActivity.getLoginUtils((Activity)mContext).exe(new LoginTask.LoginCallback() {
                @Override
                public void loginStatua(boolean b) {
                    refreshView();
                    if(mLoginCallBack != null)
                        mLoginCallBack.loginStatua(true);
                }
            });

        }
        else if(v.getId() == R.id.img_open) {
            setOpenEyeStatus(!isOpenEye);
            storeEyeStatus(isOpenEye);
        }
        else if(v.getId() == R.id.ima_help){
            showHelpMessage();
        }
    }


    private String formatAmount(String text, int scale) {
        if(text == null || text.length() <= 0)
            return null;
        if(text.contains(",") || text.contains("，"))
            text = text.replace(",","").replace("，","");

        if(text.matches("^.*[.].*[.].*$")) {
            return text;
        } else {
            String temp = "###,###,###,###,###,###,###,##0";
            if(scale > 0) {
                temp = temp + ".";
            }
            for(int e = 0; e < scale; ++e) {
                temp = temp + "0";
            }
            try {
                DecimalFormat df = new DecimalFormat(temp);
                BigDecimal d = new BigDecimal(text);
                return df.format(d).toString();
            } catch (Exception var5) {
                return text;
            }
        }


    }


    /** 显示帮组信息 */
    private void showHelpMessage(){
        if(helpMessage == null || helpMessage.length() <= 0)
            return;
        MessageDialog.showMessageDialog(mContext,helpMessage, Gravity.LEFT);
//        TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.llbt_pop_text_layout,null);
//        tv.setText(helpMessage);
//
//        PopupWindow popupWindow = new PopupWindow(tv, ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//        popupWindow.setOutsideTouchable(true);
//        popupWindow.setFocusable(true);
//        popupWindow.showAsDropDown(helpImage,-20,0);


    }

    /** 设置金额文本 */
    public void setAmountText(String amount){
        amountStr =  amount;
        String tmp = formatAmount(amount,2);
        if(tmp == null){
            formatAmountStr1 = "--";
            formatAmountStr2 = null;
            return;
        }
        if(tmp.length() <= 3){
            formatAmountStr1 = tmp;
            formatAmountStr2 = null;
            return;
        }
        formatAmountStr1 = tmp.substring(0,tmp.length() - 3);
        formatAmountStr2 = tmp.substring(tmp.length() - 3);
        refreshView();
    }

    private String helpMessage;
    /** 设置帮组信息 */
    public void setHelpMessage(String helpMessage){
        this.helpMessage = helpMessage;
    }

    /** 设置控件背景 */
    public void setBackGroundWithResource(@DrawableRes int resid){
        rootLayout.setBackgroundResource(resid);
    }


    /** 设置控件背景 */
    public void setBackGoundWithColor(int color){
        rootLayout.setBackgroundColor(color);
    }

    /**
     * 参考市值 LoginLayout 点击事件
     * @param listener
     */
    public void setLoginLayoutClickListener(View.OnClickListener listener){
        mLoginLayoutClickListener = listener;
    }

    /**
     * 我的持仓 点击事件
     * @param listener
     */
    public void setmMypositionClickListener(View.OnClickListener listener){
        mMypositionClickListener = listener;
    }
    /**
     * 本地存储眼睛状态（各模块独立）
     * @param b
     */
    private void storeEyeStatus(boolean b){
        SharedPreferences sp = mContext.getSharedPreferences("EyeStatusSP",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(contextName,b);
        editor.commit();
    }

    /**
     * 读取sp中的眼睛状态
     */
    private boolean getStoreEyeStatus(){
        SharedPreferences sp = mContext.getSharedPreferences("EyeStatusSP",Context.MODE_PRIVATE);
        isOpenEye = sp.getBoolean(contextName,true);
        return isOpenEye;
    }


    /** 设置登录前的广告图片 */
    public void setLoginOutAdvertiseImage(int resId){
        mLoginOutLayout.setBackgroundResource(resId);
    }

    /**
     * 控件参考市值文本内容设置
     * @param str
     */
    public void setRefvalueText(String str){
        if(StringUtil.isNullOrEmpty(str)) return;
        tv_refvalue.setText(str);
    }

    /**
     * 设置登录后的显示布局（参考市值 or 我的持仓）
     * @param type
     */
    public void setLoginLayoutType(LoginLayoutType type){
        if(type == LoginLayoutType.InvestPriceLayout){
            mPriceViewLayout.setVisibility(View.VISIBLE);
            mMypositioned.setVisibility(View.GONE);
        }else if(type == LoginLayoutType.MyPositionLayout){
            mMypositioned.setVisibility(View.VISIBLE);
            mPriceViewLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 设置我的持仓的背景颜色
     * @param resid
     */
    public void setMyPositionBackground(int resid){
        mMypositionLayout.setBackgroundResource(resid);
    }

}
