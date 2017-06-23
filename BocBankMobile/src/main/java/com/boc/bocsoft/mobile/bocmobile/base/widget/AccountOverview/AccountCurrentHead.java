package com.boc.bocsoft.mobile.bocmobile.base.widget.AccountOverview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.AccountUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.model.AccountListItemViewModel;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * 活期一本通头部
 * Created by niuguobin on 2016/6/16.
 */
public class AccountCurrentHead extends LinearLayout {

    /**
     * 按钮组回调事件对象
     */
    BtnCallback btnCallback;
    /**
     * 跳转电子现金或医保账户回调事件对象
     */
    ButtonOtherCallback buttonOtherCallback;
    /**
     * 右侧说明文案回调事件对象
     */
    ImgMessageCallback imgMessageCallback;
    /**
     * 隐藏显示金额回调事件对象
     */
    ImgShowSumCallback imgShowSumCallback;

    private Context mContext;
    private View rootView;

    /**
     * 展开按钮标识
     */
    private static boolean IMG_SHOW = false;

    /**
     * 动态按钮布局
     */
    private LinearLayout btn_layout;
    /**
     * 动态余额详情布局
     */
    private LinearLayout details_layout;
    /**
     * 头部币种和金额
     */
    private TextView tv_sum_title, tv_currency, tv_sum, tv_usable_money, tv_btn_other, money_icon;
    /**
     * 向下伸展，显示详情图片按钮
     */
    private ImageView img_show;
    /**
     * 提示信息按钮
     */
    private ImageView img_message;
    /**
     * 列表标题
     */
    private TextView tvDetail;
    /**
     * 电子现金账户,医保账户面板。电子现金账户,医保账户
     */
    private ViewGroup llAccountPanel;
    private TextView tvFinance, tvMedical;

    public AccountCurrentHead(Context context) {
        this(context, null, 0);
    }

    public AccountCurrentHead(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AccountCurrentHead(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    public void initView() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.boc_view_account_current_head, this);
        btn_layout = (LinearLayout) rootView.findViewById(R.id.btn_layout);
        details_layout = (LinearLayout) rootView.findViewById(R.id.details_layout);
        tv_sum_title = (TextView) rootView.findViewById(R.id.tv_sum_title);
        tv_currency = (TextView) rootView.findViewById(R.id.tv_currency);
        tv_sum = (TextView) rootView.findViewById(R.id.tv_sum);
        tv_usable_money = (TextView) rootView.findViewById(R.id.tv_usable_money);
        tv_btn_other = (TextView) rootView.findViewById(R.id.tv_btn_other);
        tv_btn_other.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonOtherCallback != null) {
                    buttonOtherCallback.onClick(v);
                }
            }
        });
        money_icon = (TextView) rootView.findViewById(R.id.tv_money_icon);
        img_message = (ImageView) rootView.findViewById(R.id.img_message);
        img_message.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgMessageCallback != null) {
                    imgMessageCallback.onClick(v);
                }
            }
        });
        tvDetail = (TextView) rootView.findViewById(R.id.tv_detail);
        img_show = (ImageView) rootView.findViewById(R.id.img_show);

        llAccountPanel = (ViewGroup) rootView.findViewById(R.id.ll_account_panel);
        tvFinance = (TextView) rootView.findViewById(R.id.tv_finance);
        tvMedical = (TextView) rootView.findViewById(R.id.tv_medical);

        isShowImgMessage(false);
    }

    public void showFinancePanel(OnClickListener clickListener) {
        llAccountPanel.setVisibility(VISIBLE);
        tvFinance.setVisibility(VISIBLE);
        tvFinance.setOnClickListener(clickListener);
    }

    public void showMedicalPanel(OnClickListener clickListener) {
        llAccountPanel.setVisibility(VISIBLE);
        tvMedical.setVisibility(VISIBLE);
        tvMedical.setOnClickListener(clickListener);
    }

    /**
     * 是否显示提示信息按钮
     *
     * @param isShow false为不显示
     */
    public void isShowImgMessage(boolean isShow) {
        if (!isShow) {
            img_message.setVisibility(View.GONE);
        } else {
            img_message.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示查看电子现金或医保账户按钮
     *
     * @param isShow:true 显示
     */
    public void showTextBtnOther(boolean isShow, String btnText) {
        if (isShow) {
            tv_btn_other.setVisibility(View.VISIBLE);
            tv_btn_other.setText(btnText);
        }
    }

    /**
     * 是否显示展开按钮
     *
     * @param isShow：true为显示
     */
    public void imgShow(boolean isShow) {
        if (isShow) {
            img_show.setVisibility(View.VISIBLE);
            img_show.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (IMG_SHOW) {
                        img_show.setImageResource(R.drawable.boc_view_icon_current_open);
                        IMG_SHOW = false;
                        details_layout.setVisibility(View.GONE);
                    } else {
                        details_layout.setVisibility(View.VISIBLE);
                        img_show.setImageResource(R.drawable.boc_view_icon_current_close);
                        IMG_SHOW = true;
                    }
                }
            });
        } else {
            img_show.setVisibility(View.GONE);
        }
    }

    /**
     * 设置头部币种和金额信息
     * 不显示可用余额
     *
     * @param currency：币种
     * @param sum：金额
     */
    public void setDataSum(String currency, String sum) {
        tv_usable_money.setVisibility(View.GONE);
        tv_currency.setText(currency);
        tv_sum.setText(sum);
    }

    /**
     * 设置头部币种和金额信息
     * 如果余额与可用余额数字一样则不显示可用余额
     *
     * @param currency：币种
     * @param sum：金额
     * @param usableSum:可用金额
     */
    public void setData(String currency, String sum, String cashRemit, String usableSum, boolean isShowUsableMoney) {
        money_icon.setVisibility(View.GONE);

        if (!StringUtils.isEmptyOrNull(cashRemit))
            currency = currency + "/" + cashRemit;
        tv_currency.setText(currency);
        tv_sum.setText(sum);
        if (sum.equals(usableSum)) {
            tv_usable_money.setVisibility(View.GONE);
        } else {
            if (!isShowUsableMoney)
                return;
            tv_usable_money.setVisibility(View.VISIBLE);
            tv_usable_money.setText("(" + usableSum + "可用)");
            isShowImgMessage(true);
        }
    }

    /**
     * 设置title
     */
    public void setHeadTitle(String title) {
        tv_sum_title.setText(title);
    }

    public void setDetails(List<AccountListItemViewModel.CardAmountViewModel> list) {
        setDetails(list, true);
    }

    /**
     * 动态添加余额币种详情
     *
     * @param list
     */
    public void setDetails(List<AccountListItemViewModel.CardAmountViewModel> list, boolean isShowUsableMoney) {
        if (list != null && list.size() > 0) {

            if (list.size() > 1) {
                img_show.setVisibility(View.VISIBLE);

                //金额排序
                Collections.sort(list);
            }
            details_layout.removeAllViews();
            boolean isHaveTitle = false;
            int number = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getCurrencyCode().equals(ApplicationConst.CURRENCY_CNY)) {
                    setData(
                            PublicCodeUtils.getCurrency(mContext, list.get(i).getCurrencyCode()),
                            MoneyUtils.transMoneyFormat(list.get(i).getBookBalance(), list.get(i).getCurrencyCode()),
                            AccountUtils.getCashRemit(list.get(i).getCashRemit()),
                            MoneyUtils.transMoneyFormat(list.get(i).getAmount(), list.get(i).getCurrencyCode()),isShowUsableMoney
                    );
                    number = i;
                    isHaveTitle = true;
                }
            }

            for (int i = 0; i < list.size(); i++) {
                //如果余额列表中有人民币把人民币设为第一项
                if (isHaveTitle) {
                    if (i == number) {
                        continue;
                    }
                    createView(list, i);
                } else {

                    if (i == 0) {
                        setData(
                                PublicCodeUtils.getCurrency(mContext, list.get(0).getCurrencyCode()),
                                MoneyUtils.transMoneyFormat(list.get(0).getBookBalance(), list.get(0).getCurrencyCode()),
                                AccountUtils.getCashRemit(list.get(i).getCashRemit()),
                                MoneyUtils.transMoneyFormat(list.get(0).getAmount(), list.get(0).getCurrencyCode()),isShowUsableMoney
                        );
                    } else {
                        createView(list, i);
                    }

                }

            }
        }
    }

    private void createView(List<AccountListItemViewModel.CardAmountViewModel> list, int i) {
        View detailsView = LayoutInflater.from(mContext).inflate(R.layout.boc_view_account_head_details, null);
        //币种
        TextView tvCurrency = (TextView) detailsView.findViewById(R.id.tv_currency);
        //"钞"图标
        TextView tvMoneyIcon = (TextView) detailsView.findViewById(R.id.tv_money_icon);
        //当前余额
        TextView tvBalance = (TextView) detailsView.findViewById(R.id.tv_balance);
        //可用余额
        TextView tvUsableBalance = (TextView) detailsView.findViewById(R.id.tv_usable_balance);

        tvMoneyIcon.setVisibility(View.GONE);
        String cashRemit = AccountUtils.getCashRemit(list.get(i).getCashRemit());
        if (StringUtils.isEmptyOrNull(cashRemit))
            tvCurrency.setText(PublicCodeUtils.getCurrency(mContext, list.get(i).getCurrencyCode()));
        else
            tvCurrency.setText(PublicCodeUtils.getCurrency(mContext, list.get(i).getCurrencyCode()) + "/" + cashRemit);

        //当前余额
        String bookBalance = MoneyUtils.transMoneyFormat(list.get(i).getBookBalance(), list.get(i).getCurrencyCode());

        //可用余额
        String availableBalance = MoneyUtils.transMoneyFormat(list.get(i).getAmount(), list.get(i).getCurrencyCode());
        tvBalance.setText(bookBalance);
        tvUsableBalance.setText("(" + availableBalance + "可用)");
        //如果当前余额和可用余额相等就不显示可用余额，否则显示
        if (bookBalance.equals(availableBalance)) {
            tvUsableBalance.setVisibility(View.GONE);
        }
        details_layout.addView(detailsView);
    }

    /**
     * 隐藏金额显示信息，把具体数字变为"*"
     *
     * @param list:金额币种信息集合

    public void updateDetails(List<BalanceModel> list) {
    tv_sum.setText("****");
    tv_usable_money.setText("(****可用)");
    if (list != null && list.size() > 0) {
    if (details_layout != null) {
    for (int i = 0; i < list.size(); i++) {
    TextView tvBalance = (TextView) details_layout.getChildAt(i).findViewById(R.id.tv_balance);
    TextView tvUsableBalance = (TextView) details_layout.getChildAt(i).findViewById(R.id.tv_usable_balance);
    tvBalance.setText("****");
    tvUsableBalance.setText("****");
    }
    }
    }

    }*/

    /**
     * 动态添加底部按钮，按钮最多为3个
     *
     * @param list：ButtonModel集合，包含图标和按钮文字
     */
    public void setButton(final List<ButtonModel> list) {
        if (list != null && list.size() > 0) {
            btn_layout.removeAllViews();
            for (int i = 0; i < list.size(); i++) {

                View btnView = LayoutInflater.from(mContext).inflate(R.layout.boc_view_account_head_btn, null);
                ImageView imgBtn = (ImageView) btnView.findViewById(R.id.img_btn);
                TextView tvName = (TextView) btnView.findViewById(R.id.tv_name);
                final int tag = list.get(i).getId();
                //动态添加按钮点击事件
                btnView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnCallback.onClickListener(tag);
                    }
                });
                LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1);
                btnView.setLayoutParams(lp);
                imgBtn.setImageResource(list.get(i).getImgName());
                tvName.setText(list.get(i).getName());
                btn_layout.addView(btnView);
            }
        }
    }

    /**
     * 快捷按钮回调函数
     */
    public interface BtnCallback {
        public void onClickListener(int tag);
    }

    /**
     * 快捷按钮点击事件
     *
     * @param btnCallback
     */
    public void setOnclick(BtnCallback btnCallback) {
        this.btnCallback = btnCallback;
    }

    /**
     * 跳转电子现金或医保账户回调
     */
    public interface ButtonOtherCallback {
        void onClick(View v);
    }

    /**
     * 跳转电子现金或医保账户点击事件
     *
     * @param buttonOtherCallback
     */
    public void buttonOtherOnclick(ButtonOtherCallback buttonOtherCallback) {
        this.buttonOtherCallback = buttonOtherCallback;
    }

    /**
     * 提示信息按钮回调函数
     */
    public interface ImgMessageCallback {
        void onClick(View v);
    }

    /**
     * 提示信息按钮点击事件
     *
     * @param imgMessageCallback
     */
    public void setImgMessageOnclick(ImgMessageCallback imgMessageCallback) {
        this.imgMessageCallback = imgMessageCallback;
    }

    /**
     * 显示或影藏金额的图标按钮回调函数
     */
    public interface ImgShowSumCallback {
        void onClick(View v);
    }

    /**
     * 显示或影藏金额的图标按钮点击事件
     *
     * @param imgShowSumCallback
     */
    public void setImgShowSumCallback(ImgShowSumCallback imgShowSumCallback) {
        this.imgShowSumCallback = imgShowSumCallback;
    }

    /**
     * 是否显示list列表标题
     *
     * @param isShow:true为显示
     * @param text：标题内容
     */
    public void showListTitle(Boolean isShow, String text) {
        if (isShow) {
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            int space20px = getResources().getDimensionPixelOffset(R.dimen.boc_space_between_20px);
//            params.setMargins(space20px, space20px * 2, space20px, space20px);
//            tvDetail.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
//            tvDetail.setLayoutParams(params);
            tvDetail.setVisibility(View.VISIBLE);
            tvDetail.setText(text);
        }
    }

    public void showCenterListTitle(Boolean isShow, String text) {
        if (isShow) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.boc_space_between_200px));
            tvDetail.setLayoutParams(params);
            tvDetail.setGravity(Gravity.CENTER);
            tvDetail.setVisibility(View.VISIBLE);
            tvDetail.setText(text);
        }
    }
}
