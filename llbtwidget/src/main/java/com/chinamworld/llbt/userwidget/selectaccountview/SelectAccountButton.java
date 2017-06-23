package com.chinamworld.llbt.userwidget.selectaccountview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.llbt.model.AccountItem;
import com.chinamworld.llbt.model.IActionCall;
import com.chinamworld.llbt.utils.AccountInfoTransferTools;
import com.chinamworld.llbtwidget.R;

import java.io.StringBufferInputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 选择账户按钮
 * Created by XieDu on 2016/7/5.
 */
public class SelectAccountButton extends LinearLayout {
    protected ImageView ivPic;
    protected TextView tvNumber,tvNumber1,nick_name;
    protected TextView tvName;
    protected ImageView ivArrow,ivArrow1;
    private ViewGroup layout1,layout2;
    protected TextView tvTips;
    private AccountItem mAccountBean;
    private Context mContext;
    /**当前账户列表*/
    private List<AccountItem> mAccountList = null;

    /** 请求账户列表事件回调 */
    private IActionCall requestAccountListCall;

    /**
     *
     * 如果没有账户列表数据源，需要设置此回调方法。请求账户列表
     * 注意：调用完账户列表请求接口后，需要在接口回调方法后面调用gotoSelectedAccountActivity()方法，才能自动跳转到选择账户列表页面
     * @param call :回调方法
     */
    public void setRequestAccountListCall(IActionCall call){
        requestAccountListCall = call;
    }
    public SelectAccountButton(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public SelectAccountButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public SelectAccountButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SelectAccountButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    /**
     * 设置选中完成后的显示模板
     * @param type ： 1 ：有图片和别名   2：仅有卡号
     */
    public void setTemplateType(int type){

        layout1.setVisibility(type == 1 ? View.VISIBLE : View.GONE);
        layout2.setVisibility(type == 1 ? View.GONE : View.VISIBLE);

    }
    /**
     * 设置左侧文字，1为账户签约显示交易账户，2为重设账户显示新交易账户
     */
    public void setLeftText(int type){
        TextView left_text=(TextView) findViewById(R.id.left_text);
        left_text.setVisibility(View.VISIBLE);
        if(type==1){
            left_text.setText("交易账户");
        }else{
            left_text.setText("新交易账户");
        }


    }

    private void initView() {
        View rootView = inflate(getContext(), R.layout.llbt_account_selected_button_view_layout, this);
        layout1 = (ViewGroup)rootView.findViewById(R.id.layout1);
        layout2 = (ViewGroup)rootView.findViewById(R.id.layout2);
        ivPic = (ImageView) rootView.findViewById(R.id.iv_pic);
        tvNumber = (TextView) rootView.findViewById(R.id.tv_number);
        tvNumber1 =(TextView) rootView.findViewById(R.id.tv_number1);
        nick_name=(TextView) findViewById(R.id.nick_name);
        tvName = (TextView) rootView.findViewById(R.id.tv_name);
        ivArrow = (ImageView) rootView.findViewById(R.id.iv_arrow);
        ivArrow1 = (ImageView) rootView.findViewById(R.id.iv_arrow1);
        tvTips = (TextView) rootView.findViewById(R.id.tv_tips);

        rootView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSelectedAccountActivity(mAccountList);
            }
        });
    }

    /**
     * 进入到选择卡片页面
     * @param list
     */
    public void gotoSelectedAccountActivity(List<AccountItem> list) {
        setAccountList(list);
        if(mAccountList == null){
            if(requestAccountListCall != null)
                requestAccountListCall.callBack();
            return;
        }
        Intent it = new Intent(mContext, SelectAccoutActivity.class);
        mContext.startActivity(it);
        SelectAccoutActivity.setSelectAccountButton(this);
    }

    /** 设置账户列表数据源 */
    public void setAccountList(List<AccountItem> list){
        mAccountList  = list;
    }

    /**
     * 获得账户列表数据源
     */
    public List<AccountItem> getAccountList(){
        return mAccountList;
    }

    public AccountItem getData() {
        return mAccountBean;
    }

    /** 设置卡片当前选中数据项*/
    public void setData(AccountItem accountBean) {
        if (accountBean == null) {
            resetView(true);
            return;
        }
        resetView(false);
        mAccountBean = accountBean;
        tvNumber.setText(AccountInfoTransferTools.getForSixForString(mAccountBean.getAccountNum()));
        tvNumber1.setText(AccountInfoTransferTools.getForSixForString(mAccountBean.getAccountNum()));
        //先显示nickName，如果没有显示type所对应的中文
        if(((Map<Object,String>)mAccountBean.getSource()).get("nickName").equals("")){
            tvName.setText(Accountstyle.get(mAccountBean.getAccountType()));
            nick_name.setText(Accountstyle.get(mAccountBean.getAccountType()));
        }else{
            tvName.setText((String)(((Map<Object,String>)mAccountBean.getSource()).get("nickName")));
            nick_name.setText((String)(((Map<Object,String>)mAccountBean.getSource()).get("nickName")));
        }

        ivPic.setImageDrawable(getResources().getDrawable(AccountInfoTransferTools.getCardPic(mAccountBean)));
    }


    /**
     * 设置箭头是否可见
     *
     * @param visible 是否可见
     */
    public void setArrowVisible(boolean visible) {
        if (visible) {
            ivArrow.setVisibility(View.VISIBLE);
            ivArrow1.setVisibility(View.VISIBLE);
        } else {
            ivArrow.setVisibility(INVISIBLE);
            ivArrow1.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 是否重置视图
     *
     * @param reset 是否重置
     */
    public void resetView(boolean reset) {
        int visibleTips = reset ? VISIBLE : INVISIBLE;
        int visibleData = reset ? INVISIBLE : VISIBLE;
        tvTips.setVisibility(visibleTips);
        ivPic.setVisibility(visibleData);
        tvNumber.setVisibility(visibleData);
        tvName.setVisibility(visibleData);
    }
    public static Map<String, String> Accountstyle = new LinkedHashMap<String, String>() {

        private static final long serialVersionUID = 1L;

        {//
            put("188", "活期一本通");
            put("119", "借记卡");
        }
    };

}
